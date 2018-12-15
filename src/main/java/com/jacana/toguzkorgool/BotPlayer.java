package com.jacana.toguzkorgool;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a non-human player.
 */
public class BotPlayer extends Player {

    private Random random = new SecureRandom();

    /**
     * Creates a new BotPlayer
     *
     * @param board The board the bot is playing on
     * @param id The ID of the bot
     */
    public BotPlayer(Board board, int id) {
        super(board, id);
    }

    /**
     * Make the bot choose and interact with a hole.
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
