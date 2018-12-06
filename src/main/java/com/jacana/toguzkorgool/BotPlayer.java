package com.jacana.toguzkorgool;
import java.awt.Color;
import java.util.Random;
public class BotPlayer extends Player {

    private Random random;

    public BotPlayer(Board board, int id, Color boardColour){
        super(board, id, boardColour);
        this.random = new Random();
    }

    public void act(){
        int randomIndex = random.nextInt(9) + 1;
        super.makeMove(randomIndex);
    }
}
