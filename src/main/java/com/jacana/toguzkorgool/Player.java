package com.jacana.toguzkorgool;


public class Player {
    private int kazan;
    private Hole[] holes;
    private Board board;

    public Player(Board board){
        this.kazan = 0;
        this.holes = new Hole[9];
        for (int i = 0; i < 9; ++i){
            holes[i] = new Hole();
        }
        this.board = board;
    }


    public Hole getHole(int index) {
        return holes[index];
    }

    public int getKazan() {
        return kazan;
    }

    public boolean haveTuz() {
        for (Hole hole : holes){
            if (hole.isTuz()){
                return true;
            }
        }
        return false;
    }

    public void setTuz(int holeNumber) {
        if (haveTuz()){
            // ... display error message, can't have more than 1 tuz
            return;
        }
        holes[holeNumber].setTuz();
    }

    public void makeMove(int holeNumber) {
        int korgools = holes[holeNumber-1].getKorgools();
        holes[holeNumber-1].clear();
        if (korgools == 1) {
            if (holeNumber == 9) {
                korgools = moveOpponent(korgools);
            } else {
                holes[holeNumber].add(1);
                --korgools;
            }
        }
        while (korgools > 0) {
            while (holeNumber <= 9 && korgools > 0) {
                holes[holeNumber-1].add(1);
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
        while (holeNumber <= 9 && korgools > 0) {
            board.getNextPlayer().holes[holeNumber-1].add(1);
            --korgools;
            ++holeNumber;
        }
        return korgools;
    }
}
