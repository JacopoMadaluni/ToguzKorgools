package com.jacana.toguzkorgool;

public class Hole extends KorgoolContainer{

    private boolean tuz = false;

    public Hole() {
        super();
        this.korgools = Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE;
    }

    public boolean isTuz() {
        return tuz;
    }

    public void setTuz(boolean tuzValue) {
        tuz = tuzValue;
    }

}
