package com.jacana.toguzkorgool;

/**
 * This class represents a hole.
 */
public class Hole extends KorgoolContainer {

    private boolean tuz = false;

    public Hole() {
        super();
        this.korgools = Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE;
    }

    /**
     * @return True if the hole is a tuz.
     */
    public boolean isTuz() {
        return tuz;
    }

    /**
     * Sets the hole to be (or not to be) a tuz.
     *
     * @param tuzValue True -> set to tuz. False -> set to normal hole.
     */
    public void setTuz(boolean tuzValue) {
        tuz = tuzValue;
    }

}
