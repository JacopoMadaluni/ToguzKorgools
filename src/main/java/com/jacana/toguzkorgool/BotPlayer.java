package com.jacana.toguzkorgool;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a non human player.
 * It can be extended with a more advanced logic system behind the
 * bot moves.
 */
public class BotPlayer extends Player {

    private Random random = new SecureRandom();

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
        List<Integer> nonEmptyHoles = new ArrayList<>();
        for (int holeIndex = 0; holeIndex < holes.length; holeIndex++) {
            if (holes[holeIndex].getKorgools() > 0) {
                nonEmptyHoles.add(holeIndex);
            }
        }
        if (!nonEmptyHoles.isEmpty()) {
            int randomIndex = nonEmptyHoles.get(random.nextInt(nonEmptyHoles.size()));
            super.makeMove(randomIndex);
        }
    }

}
