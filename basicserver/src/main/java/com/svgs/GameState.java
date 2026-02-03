package com.svgs;

import java.util.*;

public class GameState {
    boolean gameOver;
    int playerTurn;
    ArrayList<Player> players;
    Die[] dice;
    int rollNumber;
    boolean gameFull;

    //TO DO: fix the straights and make this prettier
    public int calculateCell(Scorecard card, String boxToFill){
        int score = 0;

        //counts up the number Of Numbers and puts it at that # index
        int[] nON = new int[7];
        for(Die die : dice){
            nON[die.number]++;
        }

        if(boxToFill.equals("ace")){ 
            score = nON[1];
        }
        else if(boxToFill.equals("two")){
            score = nON[2] * 2;
        }
        else if(boxToFill.equals("three")){
            score = nON[3] * 3;
        }
        else if (boxToFill.equals("four")){
            score = nON[4] * 4;
        }
        else if (boxToFill.equals("five")){
            score = nON[5] * 5;
        }
        else if(boxToFill.equals("six")){
            score = nON[6] * 6;
        }

        else if(boxToFill.equals("threeOfAKind")){
            for (int i = 1; i <= 6; i++){
                if(nON[i] >= 3){
                    score = sumAll();
                }
            }

            /*int count = 0;
            for(int i = 0; i < 5; i++){
                for(int j = i + 1; j < 5; j++){
                    if(dice[i].number == dice[j].number){
                        count ++;
                        if(count == 3){
                            for(Die single : dice){
                                score+=single.number;
                            }
                        }
                    }
                }
            }*/
        }

        else if(boxToFill.equals("fourOfAKind")){
            for (int i = 1; i <= 6; i++){
                if(nON[i] >= 4){
                    score = sumAll();
                }
            }
        }

        else if(boxToFill.equals("fullHouse")){
            boolean three = false;
            boolean two = true;

            for(int i = 1; i <= 6; i++){
                if(nON[i] == 3){
                    three = true;
                }if(nON[i] == 2){
                    two = true;
                }
            }
            if(three && two){
                score = 25;
            }
        }

        else if(boxToFill.equals("smallStraight")){
            //idrk how to do this efficiently
            score = 30;
        }

        else if(boxToFill.equals("largeStraight")){
            //haha lol
            score = 40;
        }

        else if (boxToFill.equals("yahtzee")){
            for(int i = 1; i <= 6; i++){
                if(nON[i] == 5){
                    score = 50;
                }
            }
        }

        else if(boxToFill.equals("chance")){
            score = sumAll();
        }

        return score;
    }

    private int sumAll(){
        int sum = 0;
        for(Die die : dice){
            sum += die.number;
        }
        return sum;
    }
}

