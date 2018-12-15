package com.jacana.toguzkorgool;

/**
 * This class is provides the template for a Korgool container.
 */
public abstract class KorgoolContainer {

    protected int korgools = 0;

    /**
     * Adds up korgools in the container.
     *
     * @param korgoolsIn The number of korgools to be added.
     */
    public void add(int korgoolsIn) {
        this.korgools += korgoolsIn;
    }

    /**
     * Sets the number of korgools in this container to 0.
     */
    public void clear() {
        korgools = 0;
    }

    /**
     * @return The current number of korgools in the container.
     */
    public int getKorgools() {
        return korgools;
    }

    /**
     * Sets a new number of korgools for the container.
     *
     * @param korgools The new number of korgools.
     */
    public void setKorgools(int korgools) {
        this.korgools = korgools;
    }

}
