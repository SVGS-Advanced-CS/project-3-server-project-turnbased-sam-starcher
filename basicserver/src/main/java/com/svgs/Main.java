package com.svgs;

import static spark.Spark.*;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Main {

    private static Gson gson = new Gson();
    private static GameState gameState = new GameState();

    public static void main(String[] args) {
        port(1221);
        disableCORS();
        initializeGameState();

        //post endpoints
        post("/newGame", (req, res) -> {
            
            if(gameState.gameFull){
                return "Game is full.";
            }

            NewGameRequest request = gson.fromJson(req.body(), NewGameRequest.class);

            if(gameState.players.size() != 0 && request.playerName.equals(gameState.players.get(0).name)){
                return "Player of that name is already in game";
            }else{
                res.status(201);
                gameState.players.add(new Player(request.playerName));
                if(gameState.players.size()==2){
                    gameState.gameFull = true;
                }
                return "Player added";
            }
        });

        post("/fillBox", (req, res) -> {
            FillBoxRequest request = gson.fromJson(req.body(), FillBoxRequest.class);
            String cell = request.boxToFill;
            Player currentPlayer = gameState.players.get(gameState.playerTurn);

            int score = gameState.calculateCell(currentPlayer.scorecard, cell);
            applyScore(score, cell, currentPlayer.scorecard);

            currentPlayer.playerGameOver = allBoxesFilled(currentPlayer.scorecard);
            if(currentPlayer.playerGameOver && gameState.players.get((gameState.playerTurn + 1) % 2).playerGameOver){
                gameState.gameOver = true;
            }

            gameState.rollNumber = 1;
            gameState.playerTurn = (gameState.playerTurn + 1) % 2;

            return "";
        });

        post("/resetGame", (req, res) -> {
            initializeGameState();
            return "";
        });


        get("/gameState", (req, res) -> {
            res.type("application/json");
            return gson.toJson(gameState);
        });

        get("/rollDice", (req,res) -> {
            res.type("application/json");
            RollRequest request = gson.fromJson(req.body(), RollRequest.class);
            int[] toRoll = request.diceToRoll;

            if(gameState.rollNumber == 1){
                for(int i = 0; i < 5; i++){
                    gameState.dice[i].roll();
                }
                gameState.rollNumber++;
                return "";
            }else{
                for(int index : toRoll){
                    if(index >= 0 && index < 5){
                        gameState.dice[index].roll();
                    }
                }
                gameState.rollNumber++;
                return "";
            }
        });
        
    }

    //default gameState
    public static void initializeGameState(){
        gameState.gameOver = false;
        gameState.playerTurn = 0; //shows index, not player #
        gameState.players = new ArrayList<>();
        gameState.rollNumber = 1;
        gameState.dice = new Die[5];
        //makes set of dice, with numbers 1-5
        for(int i = 0; i < 5; i++){
            gameState.dice[i] = new Die();
            gameState.dice[i].number = i+1;
        }
        gameState.gameFull = false;
    }

    public static boolean allBoxesFilled(Scorecard card){
        return card.ace != null && card.two != null &&
        card.three != null && card.four != null &&
        card.five != null && card.six != null &&
        card.threeOfAKind != null && card.fourOfAKind != null &&
        card.fullHouse != null && card.smallStraight != null &&
        card.largeStraight != null && card.yahtzee != null &&
        card.chance != null;
    }

    //applies the score, finish later
    public static void applyScore(int score, String cell, Scorecard card){
        if(cell.equals("ace")){
            card.ace = score;
        }else if(cell.equals("two")){
            card.two = score;
        }
    }

    public static void disableCORS() {
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    }
}