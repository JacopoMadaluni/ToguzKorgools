package com.jacana.toguzkorgool;

public class Hole extends KorgoolContainer{
    private boolean tuz;

    public Hole() {
        super();
        korgools = 9;
        this.tuz = false;
    }

    public boolean isTuz() {
        return tuz;
    }

    public void setTuz() {
        tuz = true;
    }
}
