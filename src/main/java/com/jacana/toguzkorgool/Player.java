package com.jacana.toguzkorgool;


import java.awt.Color;

public abstract class Player {
    private final int id;
    private Kazan kazan;
    private Hole[] holes;
    private Board board;
    private Color boardColour;

    public Player(Board board, int id, Color boardColour) {
        this.id = id;
        this.kazan = new Kazan();
        this.holes = new Hole[9];
        for (int i = 0; i < 9; ++i) {
            holes[i] = new Hole();
        }
        this.board = board;
        this.boardColour = boardColour;
    }

    public Board getBoard(){
        return board;
    }

    public Color getBoardColour() {
        return boardColour;
    }

    public int getId(){
        return id;
    }

    public Hole getHole(int index) {
        return holes[index];
    }

    /**
     * @return The number of holes of the player
     */
    public int getHoleCount() {
        return holes.length;
    }

    public int getKazanCount() {
        return kazan.getKorgools();
    }

    public Kazan getKazan() {
        return kazan;
    }

    public int getKorgoolsInHole(int index) {
        return holes[index].getKorgools();
    }

    public boolean hasTuz() {
        for (Hole hole : holes) {
            if (hole.isTuz()) {
                return true;
            }
        }
        return false;
    }

    public int getTuzIndex() {
        for (int i = 0; i < holes.length; ++i) {
            if (holes[i].isTuz()) {
                return i;
            }
        }
        return -1;
    }

    // -- setters

    public void setTuz(int holeNumber) {
        Player opponent = board.getOpponentOf(id);
        if (holeNumber == -1) {
            for (Hole hole : holes) {
                hole.setTuz(false);
            }
        } else {
            if (holeNumber != 8 && !hasTuz() && opponent.getTuzIndex() != holeNumber) {
                holes[holeNumber].setTuz(true);
            }
        }
    }

    public void setKazanCount(int count) {
        kazan.clear();
        kazan.add(count);
    }

    public void addToKazan(int amount) {
        kazan.add(amount);
    }

    public void addToHole(int index, int amount) {
        holes[index].add(amount);
    }

    public void clearHole(int index) {
        holes[index].clear();
    }


    // -- actions

    public void makeMove(int holeNumber) {
        int korgools = holes[holeNumber - 1].getKorgools();
        holes[holeNumber - 1].clear();
        if (korgools == 1) {
            if (holeNumber == 9) {
                korgools = moveOpponent(korgools);
            } else {
                Hole hole = holes[holeNumber];
                if (hole.isTuz()) {
                    board.addToOpponentKazan(1);
                } else {
                    holes[holeNumber].add(1);
                }
                --korgools;
            }
        }
        while (korgools > 0) {
            while (holeNumber <= 9 && korgools > 0) {
                Hole hole = holes[holeNumber - 1];
                if (hole.isTuz()) {
                    board.addToOpponentKazan(1);
                } else {
                    holes[holeNumber - 1].add(1);
                }
                ++holeNumber;
                --korgools;
            }
            if (korgools > 0) {
                korgools = moveOpponent(korgools);
                holeNumber = 1;
            }
        }
    }

    private int moveOpponent(int korgools) {
        int holeNumber = 1;
        int opponentTuz = board.getOpponentTuz() + 1;
        while (holeNumber <= 9 && korgools > 0) {
            if (opponentTuz != -1 && holeNumber == opponentTuz) {
                kazan.add(1);
            } else {
                board.addToOpponentHole(holeNumber - 1, 1);
            }
            --korgools;
            ++holeNumber;
        }
        int korgoolsInOpponentHole = board.getOpponentHoleKorgoolCount(holeNumber - 2);
        if (korgools == 0 && korgoolsInOpponentHole % 2 == 0) {
            kazan.add(korgoolsInOpponentHole);
            board.clearOpponentHole(holeNumber - 2);
        }

        int korgoolsInLastHole = board.getOpponentHoleKorgoolCount(holeNumber - 2);
        if (korgools == 0 && korgoolsInLastHole == 3) {
            if (!board.opponentHasTuz()) {
                board.setOpponentTuz(holeNumber - 2);
                board.clearOpponentHole(holeNumber - 2);
                kazan.add(korgoolsInLastHole);
            }
        }
        return korgools;
    }

    public boolean hasWon() {
        if (kazan.getKorgools() > holes.length * holes.length) {
            return true;
        }
        return false;
    }

    public void resetPlayer() {
        kazan.clear();
        for (int i = 0; i < 9; ++i) {
            holes[i].clear();
            holes[i].add(9);
            holes[i].setTuz(false);
        }
    }
}
