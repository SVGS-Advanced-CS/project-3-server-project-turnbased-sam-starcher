package com.svgs;

public class Die {
    public int number;

    public int roll(){
        number = (int)(Math.random()*6)+1;
        return number;
    }
}
