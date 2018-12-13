package com.jacana.toguzkorgool;

import java.util.Random;

/**
 * This class represents a non human player.
 * It can be extended with a more advanced logic system behind the
 * bot moves.
 */
public class BotPlayer extends Player {

    private Random random = new Random();

    /**
     * Creates a new bot.
     * @param board The board it's playing in.
     * @param id The id of the bot player.
     */
    public BotPlayer(Board board, int id) {
        super(board, id);
    }

    /**
     * The bot makes its move when this method is called.
     */
    public void act() {
        int randomIndex = random.nextInt(holes.length);
        super.makeMove(randomIndex);
    }

}
