package com.jacana.toguzkorgool;

//TODO: #Refactor make Player the parent class of Bot class which in turn should override the makeMove() method for the bot.
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

    public int getHoleCount() {
        return holes.length;
    }

    public Hole[] getHoles() {
        return holes;
    }

    public int getKazan() {
        return kazan;
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

    public void setTuz(int holeNumber) {
        Player opponent = board.getOpponentOf(this);
        if (holeNumber != 8 && !hasTuz() && opponent.getTuzIndex() != holeNumber){
            holes[holeNumber].setTuz();
        }
        // ... cannot set tuz
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
        Player opponent = board.getNextPlayer();
        while (holeNumber <= 9 && korgools > 0) {
            opponent.holes[holeNumber-1].add(1);
            --korgools;
            ++holeNumber;
        }
        if (korgools == 0 && opponent.holes[holeNumber-2].getKorgools() % 2 == 0){
            kazan += opponent.holes[holeNumber-2].getKorgools();
            opponent.holes[holeNumber-2].clear();
        }

        Hole lastHole = board.getNextPlayer().holes[holeNumber-2];
        if (korgools == 0 && lastHole.getKorgools() == 3){
            if (!opponent.hasTuz()){
                opponent.setTuz(holeNumber-2);
                int korgolsToPlayer = lastHole.getKorgools();
                lastHole.clear();
                kazan += korgolsToPlayer;
            }
        }
        return korgools;
    }

    private boolean hasWon(){
        if (kazan > holes.length*holes.length){
            return true;
        }
        return false;
    }
}
