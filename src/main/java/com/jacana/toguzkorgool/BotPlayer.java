package com.jacana.toguzkorgool;

import java.util.Random;

public class BotPlayer extends Player {

    private Random random = new Random();

    public BotPlayer(Board board, int id) {
        super(board, id);
    }

    public void act() {
        int randomIndex = random.nextInt(holes.length);
        super.makeMove(randomIndex);
    }
}
