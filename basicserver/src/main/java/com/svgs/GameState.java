package com.svgs;

import java.util.*;

public class GameState {
    boolean gameOver;
    int playerTurn;
    ArrayList<Player> players;
    Die[] dice;
    int rollNumber;
    boolean gameFull;

    public int calculateCell(Scorecard card, String boxToFill){
        int score = 0;

        //surely there's a better way to ace-6..........
        if(cell.equals("ace")){ 
            for(Die single : dice){
                if(single.number == 1){
                    score++;
                }
            }
        }

        else if(cell.equals("two")){
            for(Die single : dice){
                if(single.number == 2){
                    score+=2;
                }
            }
        }

        else if(cell.equals("three")){
            for(Die single : dice){
                if(single.number == 3){
                    score+=3;
                }
            }
        }

        else if (cell.equals("four")){
            for(Die single : dice){
                if(single.number == 3){
                    score+=4;
                }
            }
        }

        else if (cell.equals("five")){
            for(Die single : dice){
                if(single.number == 5){
                    score+=5;
                }
            }
        }

        else if(cell.equals("six")){
            for(Die single : dice){
                if(single.number == 6){
                    score+=6;
                }
            }
        }

        else if(cell.equals("threeOfAKind")){
            int count = 0;
            for(int i = 0; i < 5; i++){
                for(int j = i + 1; j < 5; j++){
                    if(dice[i] == dice[j]){
                        count ++;
                        if(count == 3){
                            for(Die single : dice){
                                score+=single.number;
                            }
                        }
                    }
                }
            }
        }
        
    }

}
