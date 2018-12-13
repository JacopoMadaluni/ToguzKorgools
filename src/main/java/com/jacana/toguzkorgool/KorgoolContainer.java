package com.jacana.toguzkorgool;

public abstract class KorgoolContainer {

    protected int korgools = 0;

    public void add(int korgoolsIn) {
        this.korgools += korgoolsIn;
    }

    public void clear() {
        korgools = 0;
    }

    public int getKorgools() {
        return korgools;
    }

    public void setKorgools(int korgools) {
        this.korgools = korgools;
    }

}
