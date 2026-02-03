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

        //counts up the number Of Numbers on the dice roll and puts it at that # index
        //index 0 really isn't used
        int[] nON = new int[7];
        for(Die die : dice){
            nON[die.number]++;
        }

        //1-6, gets the # of that # and multiples it by that #
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

        //if a number has >= 3, then sums all the numbers for the score
        //doesn't matter which #, as per yahtzee rules
        else if(boxToFill.equals("threeOfAKind")){
            for (int i = 1; i <= 6; i++){
                if(nON[i] >= 3){
                    score = sumAll();
                }
            }
        }

        //same logic as threeOfAKind, but with 4
        else if(boxToFill.equals("fourOfAKind")){
            for (int i = 1; i <= 6; i++){
                if(nON[i] >= 4){
                    score = sumAll();
                }
            }
        }

        //if there is 3 of one # and 2 of another
        //if so, score is 25
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

        //there are 3 possibilities, 1234, 2345, and 3456
        else if(boxToFill.equals("smallStraight")){
            if(
                (nON[1] > 0 && nON[2] > 0 && nON[3] > 0 && nON[4] > 0) ||
                (nON[2] > 0 && nON[3] > 0 && nON[4] > 0 && nON[5] > 0) ||
                (nON[3] > 0 && nON[4] > 0 && nON[5] > 0 && nON[6] > 0)
            ){
            score = 30;
            }
        }

        //2 possibilities, 12345 and 23456
        else if(boxToFill.equals("largeStraight")){
            if(
                (nON[1] > 0 && nON[2] > 0 && nON[3] > 0 && nON[4] > 0 && nON[5] > 0) ||
                (nON[2] > 0 && nON[3] > 0 && nON[4] > 0 && nON[5] > 0 && nON[6] > 0) 
            ){
            score = 40;
            }
        }

        //if all numbers are the same, yahtzee!
        else if (boxToFill.equals("yahtzee")){
            for(int i = 1; i <= 6; i++){
                if(nON[i] == 5){
                    score = 50;
                }
            }
        }

        //sums all dice
        else if(boxToFill.equals("chance")){
            score = sumAll();
        }

        return score;
    }

    //sums all dice, used in threeOfAKind, fourOfAKind, and chance
    private int sumAll(){
        int sum = 0;
        for(Die die : dice){
            sum += die.number;
        }
        return sum;
    }

}

