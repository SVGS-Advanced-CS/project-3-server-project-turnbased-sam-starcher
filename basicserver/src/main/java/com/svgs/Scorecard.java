package com.svgs;

public class Scorecard {
    //alterable cells 
    Integer ace;
    Integer two;
    Integer three;
    Integer four;
    Integer five;
    Integer six;
    Integer threeOfAKind;
    Integer fourOfAKind;
    Integer fullHouse;
    Integer smallStraight;
    Integer largeStraight;
    int[] yahtzee;
    Integer chance;

    //cell stuff
    int bonus;
    int upperBeforeBonus;
    int upperTotal;
    int lowerTotal;
    int grandTotal;

    public void calculateFinals(){
        upperBeforeBonus = ace + two + three + four + five + six;
        if(upperBeforeBonus >= 63){
            bonus = 35;
        }else{
            bonus = 0;
        }
        upperTotal = upperBeforeBonus + bonus;
        if(yahtzee.length > 0){
            //uhh do something so the score can be stored as well
            //yahtzee = 50 + 100 * (yahtzee.length-1);
        }else{
            //yahtzee = 0;
        }
        lowerTotal = threeOfAKind + fourOfAKind + fullHouse + smallStraight + largeStraight + chance; //+ yahtzee scores I'll figure out at some point
        grandTotal = upperTotal + lowerTotal;
    }

}
