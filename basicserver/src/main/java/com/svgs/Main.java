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
            
            if(!gameState.gameFull){
                if(gameState.players.size() != 0 && req.body().equals(gameState.players.get(0).name)){
                    return "Player of that name is already in game";
                }else{
                    res.status(201);
                    gameState.players.add(new Player(req.body()));
                    if(gameState.players.size()==2){
                        gameState.gameFull = true;
                    }
                    return "Player added";
                }
            }else{
            return "Game is full";
            }
        });

        post("/fillBox", (req, res) -> {
            FillBoxRequest fillRequest = gson.fromJson(req.body(), FillBoxRequest.class);
            String cell = fillRequest.boxToFill;
            Player currentPlayer = gameState.players.get(gameState.playerTurn);

            //check if box has been filled here?
            int score = gameState.calculateCell(currentPlayer.scorecard, cell);
            
            //apply the score
            //check if all boxes are full, and if so set playerGameOver true
            //set roll # to 1 and switch player turns

            //return FillBoxResponse???

            return "";
        });


        get("/gameState", (req, res) -> {
            res.type("application/json");
            return gson.toJson(gameState);
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

    //helper for determining playerGameOver
    //please fix this sam cause why does it want 0 instead of null cause you could have a 0 if you just took the L on one
    public static boolean allBoxesFilled(Scorecard card){
        return card.ace != null && card.two != null &&
        card.three != null && card.four != null &&
        card.five != null && card.six != null &&
        card.threeOfAKind != null && card.fourOfAKind != null &&
        card.fullHouse != null && card.smallStraight != null &&
        card.largeStraight != null && card.yahtzee != null &&
        card.chance != null;
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