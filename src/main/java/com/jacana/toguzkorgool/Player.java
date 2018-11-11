package com.jacana.toguzkorgool;


public class Player {
    protected int kazan;
    protected Hole[] holes;

    public Player(){
        this.kazan = 0;
        this.holes = new Hole[9];
        for (int i = 0; i < 9; ++i){
            holes[i] = new Hole();
        }
    }


    public Hole getHole(int index){
        return holes[index];
    }

    public int getKazan(){
        return kazan;
    }

    public boolean haveTuz(){
        for (Hole hole : holes){
            if (hole.isTuz()){
                return true;
            }
        }
        return false;
    }

    public void setTuz(int holeNumber){
        if (haveTuz()){
            // ... display error message, can't have more than 1 tuz
            return;
        }
        holes[holeNumber].setTuz();
    }

    public void makeMove(int holeNumber){
        // ...
    }
}
