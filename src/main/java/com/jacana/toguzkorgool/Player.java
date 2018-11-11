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

    public void makeMove(int holeNumber){
        // ...
    }
}
