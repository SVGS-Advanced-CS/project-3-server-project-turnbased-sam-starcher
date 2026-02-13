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

        //fix so you can only fillBox if you are the player whose turn it is
        post("/fillBox", (req, res) -> {
            if(gameState.rollNumber == 1){
                return "You must roll first before filling a box";
            }

            FillBoxRequest request = gson.fromJson(req.body(), FillBoxRequest.class);
            String cell = request.boxToFill;
            String scoringPlayer = request.player;

            Player currentPlayer = gameState.players.get(gameState.playerTurn);
            String currentPlayerName = gameState.players.get(gameState.playerTurn).name;

            if(scoringPlayer.equals(currentPlayerName)){
            int score = gameState.calculateCell(currentPlayer.scorecard, cell);
            applyScore(score, cell, currentPlayer.scorecard);

            //checks if boxes are full, maybe playerGameOver, maybe gameOver???
            currentPlayer.playerGameOver = allBoxesFilled(currentPlayer.scorecard);
            if(currentPlayer.playerGameOver){
                currentPlayer.scorecard.calculateFinals();
            }

            if(currentPlayer.playerGameOver && gameState.players.get((gameState.playerTurn + 1) % 2).playerGameOver){
                gameState.gameOver = true;
            }

            gameState.rollNumber = 1;
            gameState.playerTurn = (gameState.playerTurn + 1) % 2;

            return "Successfully applied score";
            }

            return "You may only apply a score on your turn";
        });

        post("/resetGame", (req, res) -> {
            initializeGameState();
            return "";
        });


        post("/gameState", (req, res) -> {
            res.type("application/json");
            gameState.diceState = gson.fromJson(req.body(), RollRequest.class);
            return gson.toJson(gameState);
        });

        post("/rollDice", (req,res) -> {
            res.type("application/json");
            RollRequest request = gson.fromJson(req.body(), RollRequest.class);
            int[] toRoll = request.diceToRoll;
            String rollingPlayer = request.player;

            String currentPlayer = gameState.players.get(gameState.playerTurn).name;

            if(rollingPlayer.equals(currentPlayer)){
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
            }
        }
        return "";
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

    //applies the score, fix yahtzee mechanic
    public static void applyScore(int score, String cell, Scorecard card){
        if(cell.equals("ace")){
            card.ace = score;
        }else if(cell.equals("two")){
            card.two = score;
        }else if(cell.equals("three")){
            card.three = score;
        }else if(cell.equals("four")){
            card.four = score;
        }else if(cell.equals("five")){
            card.five = score;
        }else if(cell.equals("six")){
            card.six = score;
        }else if(cell.equals("threeOfAKind")){
            card.threeOfAKind = score;
        }else if(cell.equals("fourOfAKind")){
            card.fourOfAKind = score;
        }else if(cell.equals("fullHouse")){
            card.fullHouse = score;
        }else if(cell.equals("smallStraight")){
            card.smallStraight = score;
        }else if(cell.equals("largeStraight")){
            card.largeStraight = score;
        }else if(cell.equals("yahtzee")){
            card.yahtzee = score;
        }else if(cell.equals("chance")){
            card.chance = score;
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