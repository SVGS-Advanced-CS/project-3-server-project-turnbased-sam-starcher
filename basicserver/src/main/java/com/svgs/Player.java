package com.svgs;

public class Player {
    String name;
    Scorecard scorecard;
    boolean playerGameOver;

    public Player(String name){
        this.name = name;
        scorecard = new Scorecard();
        playerGameOver = false;
    }
}
