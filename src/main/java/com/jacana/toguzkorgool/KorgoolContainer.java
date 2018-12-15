package com.jacana.toguzkorgool;

/**
 * This class provides the template for a Korgool container.
 */
public abstract class KorgoolContainer {

    protected int korgools = 0;

    /**
     * Add korgools to this container.
     *
     * @param korgoolsIn The number of korgools to be added
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
     * Sets a new number of korgools in this container.
     *
     * @param korgools The new number of korgools
     */
    public void setKorgools(int korgools) {
        this.korgools = korgools;
    }

}
