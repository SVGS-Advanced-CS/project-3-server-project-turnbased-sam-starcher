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
            FillBoxRequest box = gson.fromJson(req.body(), FillBoxRequest.class);
            String cell = box.boxToFill;
            //grr I don't know how to transfer from the fillBoxRequest to the gameState field
            if(gameState.cell == null){
                gameState.cell = gameState.calculateScore(cell);
            }
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