package com.jacana.toguzkorgool;

public abstract class KorgoolContainer {
    protected int korgools;

    public KorgoolContainer() {

    }

    public int getKorgools() {
        return korgools;
    }

    public void add(int korgoolsIn) {
        this.korgools += korgoolsIn;
    }

    public void clear() {
        korgools = 0;
    }
}
