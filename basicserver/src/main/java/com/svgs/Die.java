package com.svgs;

public class Die {
    private int number;

    public int roll(){
        number = (int)(Math.random()*5)+1;
        return number;
    }
}
