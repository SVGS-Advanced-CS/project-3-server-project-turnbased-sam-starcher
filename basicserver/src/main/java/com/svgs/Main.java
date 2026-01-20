package com.svgs;

import static spark.Spark.*;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Main {

    private static Gson gson = new Gson();
    private static GameState gameState = new GameState();

    public static void main(String[] args) {
        disableCORS();
        initializeGameState();

        post("/newGame", (req, res) -> {
            res.status(201);
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