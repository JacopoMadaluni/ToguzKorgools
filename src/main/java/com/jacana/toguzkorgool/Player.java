package com.jacana.toguzkorgool;


public abstract class Player {
    private Kazan kazan;
    private Hole[] holes;
    private Board board;

    public Player(Board board){
        this.kazan = new Kazan();
        this.holes = new Hole[9];
        for (int i = 0; i < 9; ++i){
            holes[i] = new Hole();
        }
        this.board = board;
    }


    public Hole getHole(int index) {
        return holes[index];
    }

    public int getHoleCount() {
        return holes.length;
    }

    public Kazan getKazan() {
        return kazan;
    }

    public int getKorgoolsInHole(int index){
        return holes[index].getKorgools();
    }

    public boolean hasTuz() {
        for (Hole hole : holes){
            if (hole.isTuz()){
                return true;
            }
        }
        return false;
    }

    public int getTuzIndex(){
        for (int i = 0; i < holes.length; ++i){
            if (holes[i].isTuz()){
                return i;
            }
        }
        return -1;
    }

    // -- setters

    public void setTuz(int holeNumber) {
        Player opponent = board.getOpponentOf(this);
        if (holeNumber != 8 && !hasTuz() && opponent.getTuzIndex() != holeNumber){
            holes[holeNumber].setTuz();
        }
        // ... cannot set tuz
    }

    public void addToKazan(int amount){
        kazan.add(amount);
    }

    public void addToHole(int index, int amount){
        holes[index].add(amount);
    }

    public void clearHole(int index){
        holes[index].clear();
    }


    // -- actions

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
            board.addToOpponentHole(holeNumber-1, 1);
            --korgools;
            ++holeNumber;
        }
        int korgoolsInOpponentHole = board.getKorgoolsInOpponentHole(holeNumber-2);
        if (korgools == 0 && korgoolsInOpponentHole % 2 == 0){
            kazan.add(korgoolsInOpponentHole);
            board.clearOpponentHole(holeNumber-2);
        }

        int korgoolsInLastHole = board.getKorgoolsInOpponentHole(holeNumber-2);
        if (korgools == 0 && korgoolsInLastHole == 3){
            if (!board.opponentHasTuz()){
                board.setTuzInOpponentField(holeNumber-2);
                board.clearOpponentHole(holeNumber-2);
                kazan.add(korgoolsInLastHole);
            }
        }
        return korgools;
    }

    private boolean hasWon(){
        if (kazan.getKorgools() > holes.length*holes.length){
            return true;
        }
        return false;
    }
}