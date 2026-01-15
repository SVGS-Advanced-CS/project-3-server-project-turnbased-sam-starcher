package com.svgs;

import static spark.Spark.*;

import com.google.gson.Gson;

public class Main {

    private static Gson gson = new Gson();

    public static void main(String[] args) {
        disableCORS();

        post("/newGame", (req, res) -> {
            //somewthiong something newGame
            res.status(201);
            return "";
        });
        get("/gameState", (req, res) -> {
            res.type("application/json");
            return gson.toJson(something...);
        });
        
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