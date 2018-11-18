package com.jacana.toguzkorgool;

public class Hole {
    private int korgools;
    private boolean tuz;

    public Hole() {
        this.korgools = 9;
        this.tuz = false;
    }

    public int getKorgools() {
        return korgools;
    }

    public boolean isTuz() {
        return tuz;
    }

    public void add(int korgoolsIn) {
        this.korgools += korgoolsIn;
    }

    public void clear() {
        korgools = 0;
    }

    public void setTuz() {
        tuz = true;
    }
}
