package com.jacana.toguzkorgool;

/**
 * A class representing a player playing on a board.
 * <p>
 * The player holds information on the holes and kazan on their side.
 * <p>
 * The player can perform actions such as making a move.
 */
public abstract class Player {

    // The board instance
    protected Board board;

    protected final int id;

    protected Kazan kazan;
    protected Hole[] holes;

    /**
     * Construct a Player instance
     *
     * @param board The board the player is playing on
     * @param id The ID of the player
     */
    public Player(Board board, int id) {
        this.id = id;
        this.kazan = new Kazan();
        this.holes = new Hole[Constants.CONSTRAINT_HOLES_PER_PLAYER];
        for (int i = 0; i < this.holes.length; ++i) {
            this.holes[i] = new Hole();
        }
        this.board = board;
    }

    /* Getters */

    public Board getBoard() {
        return board;
    }

    public int getId() {
        return id;
    }

    /**
     * @param index The index of the hole
     * @return The Hole instance at the index
     */
    public Hole getHole(int index) {
        return holes[index];
    }

    /**
     * @return The number of holes on the player's side of the board
     */
    public int getHoleCount() {
        return holes.length;
    }

    /**
     * @return The number of korgools in the player's kazan
     */
    public int getKazanCount() {
        return kazan.getKorgools();
    }

    public Kazan getKazan() {
        return kazan;
    }

    /**
     * @param index The index of the hole
     * @return The number of korgools in the hole at the index
     */
    public int getKorgoolsInHole(int index) {
        return holes[index].getKorgools();
    }

    /**
     * @return The index of the hole marked as a tuz, if any. Returns -1 if there is no tuz.
     */
    public int getTuzIndex() {
        for (int i = 0; i < holes.length; ++i) {
            if (holes[i].isTuz()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return True if there is a hole on the player's side that is marked as a tuz.
     */
    public boolean hasTuz() {
        for (Hole hole : holes) {
            if (hole.isTuz()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    public boolean hasWon() {
        return kazan.getKorgools() > holes.length * holes.length;
    }

    /* Setters */

    /**
     * Mark the hole at the index as a tuz.
     *
     * @param holeIndex The index of the hole
     * @return True if the hole was successfully marked as a tuz
     */
    public boolean setTuz(int holeIndex) {
        return setTuz(holeIndex, true);
    }

    /**
     * Mark/unmark the hole at the index as a tuz.
     * <p>
     * If the hole index is -1 and the tuz flag is set to false, unmark the tuz hole.
     *
     * @param holeIndex The index of the hole
     * @param tuzFlag Whether or not the hole should become a tuz
     * @return True if the hole was successfully marked/unmarked as a tuz
     */
    public boolean setTuz(int holeIndex, boolean tuzFlag) {
        Player opponent = board.getOpponentOf(id);
        if (holeIndex == -1) {
            if (!tuzFlag) {
                for (Hole hole : holes) {
                    hole.setTuz(false);
                }
                return true;
            }
        } else {
            if (holeIndex != 8 && tuzFlag != hasTuz() && opponent.getTuzIndex() != holeIndex) {
                holes[holeIndex].setTuz(tuzFlag);
                return true;
            }
        }
        return false;
    }

    /**
     * Set the number of korgools in the kazan.
     *
     * @param korgools The number of korgools
     */
    public void setKazanCount(int korgools) {
        kazan.setKorgools(korgools);
    }

    /* General methods */

    /**
     * Add korgools to the kazan.
     *
     * @param amount The number of korgools to add
     */
    public void addToKazan(int amount) {
        kazan.add(amount);
    }

    /**
     * Add korgools to the hole at the index.
     *
     * @param index The index of the hole
     * @param amount The number of korgools to add
     */
    public void addToHole(int index, int amount) {
        holes[index].add(amount);
    }

    /**
     * Clear the korgools in the hole at the index.
     *
     * @param index The index of the hole
     */
    public void clearHole(int index) {
        holes[index].clear();
    }

    /**
     * Reset the Player instance to its original state.
     * <p>
     * This clears the korgools in the kazan and resets the number of korgools in each hole to its default amount.
     * <p>
     * This also clears the tuz state if present.
     */
    public void resetPlayer() {
        kazan.clear();
        for (int i = 0; i < holes.length; ++i) {
            holes[i].setKorgools(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE);
            holes[i].setTuz(false);
        }
    }

    /* Actions */

    /**
     * Simulate the player making a move, interacting with a hole
     *
     * @param holeIndex The index of the hole
     */
    public void makeMove(int holeIndex) {
        int korgools = holes[holeIndex].getKorgools();
        holes[holeIndex].clear();
        if (korgools == 1) {
            if (holeIndex == 9 - 1) {
                korgools = moveOpponent(korgools);
            } else {
                Hole hole = holes[holeIndex + 1];
                if (hole.isTuz()) {
                    board.addToOpponentKazan(1);
                } else {
                    holes[holeIndex + 1].add(1);
                }
                --korgools;
            }
        }
        while (korgools > 0) {
            while (holeIndex < Constants.CONSTRAINT_HOLES_PER_PLAYER && korgools > 0) {
                Hole hole = holes[holeIndex];
                if (hole.isTuz()) {
                    board.addToOpponentKazan(1);
                } else {
                    holes[holeIndex].add(1);
                }
                ++holeIndex;
                --korgools;
            }
            if (korgools > 0) {
                korgools = moveOpponent(korgools);
                holeIndex = 0;
            }
        }
    }

    /**
     * Move korgools over to the opponent's side of the board.
     *
     * @param korgools The korgools left to move
     * @return The number of korgools left to move after performing this action
     */
    private int moveOpponent(int korgools) {
        int holeIndex = 0;
        int opponentTuz = board.getOpponentTuz();
        while (holeIndex < Constants.CONSTRAINT_HOLES_PER_PLAYER && korgools > 0) {
            if (opponentTuz != -1 && holeIndex == opponentTuz) {
                kazan.add(1);
            } else {
                board.addToOpponentHole(holeIndex, 1);
            }
            --korgools;
            ++holeIndex;
        }
        int korgoolsInOpponentHole = board.getKorgoolsInOpponentHole(holeIndex - 1);
        if (korgools == 0 && korgoolsInOpponentHole % 2 == 0) {
            kazan.add(korgoolsInOpponentHole);
            board.clearOpponentHole(holeIndex - 1);
        }

        int korgoolsInLastHole = board.getKorgoolsInOpponentHole(holeIndex - 1);
        if (korgools == 0 && korgoolsInLastHole == 3) {
            if (!board.doesOpponentHaveTuz()) {
                board.setOpponentTuz(holeIndex - 1, true);
                board.clearOpponentHole(holeIndex - 1);
                kazan.add(korgoolsInLastHole);
            }
        }
        return korgools;
    }

}
