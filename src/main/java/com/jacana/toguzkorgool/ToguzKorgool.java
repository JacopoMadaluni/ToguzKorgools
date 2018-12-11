package com.jacana.toguzkorgool;

public class ToguzKorgool {

    public static void main(String[] args) {
        GameController.getInstance();
    }
    
    public static void exitApplication() { GameController.destroyInstance(); }

}