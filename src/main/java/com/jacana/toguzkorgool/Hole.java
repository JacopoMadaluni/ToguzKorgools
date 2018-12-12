package com.jacana.toguzkorgool;

public class Hole extends KorgoolContainer{
    private boolean tuz;

    public Hole() {
        super();
        this.korgools = Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE;
        this.tuz = false;
    }

    public boolean isTuz() {
        return tuz;
    }

    public void setTuz(boolean tuzValue) {
        tuz = tuzValue;
    }
}
