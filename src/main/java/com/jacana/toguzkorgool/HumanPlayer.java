package com.jacana.toguzkorgool;

/**
 * This class represents a Human Player.
 * It can be extended with more advanced functionalities in the moment
 * that two human players play against each other.
 */
public class HumanPlayer extends Player {

    /**
     * Create a new Human Player
     *
     * @param board The board he's playing on.
     * @param id The id of the player.
     */
    public HumanPlayer(Board board, int id) {
        super(board, id);
    }

}
