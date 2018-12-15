package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KazanTest {

    /**
     * Ensure the construction of a kazan results in the expected number of korgools.
     */
    @Test
    public void testInitialState() {
        Kazan kazan = new Kazan();
        assertEquals(0, kazan.getKorgools());
    }

    /**
     * Ensure adding korgools to a kazan results in the expected number of korgools.
     */
    @Test
    public void testAdd() {
        Kazan kazan = new Kazan();
        kazan.add(1);
        assertEquals(1, kazan.getKorgools());
    }

    /**
     * Ensure clearing all the korgools from a kazan results in the expected number of korgools.
     */
    @Test
    public void testClear() {
        Kazan kazan = new Kazan();
        kazan.add(1);
        kazan.clear();
        assertEquals(0, kazan.getKorgools());
    }

    /**
     * Ensure setting the number of korgools in a kazan results in the expected number of korgools.
     */
    @Test
    public void testSetKorgools() {
        Kazan kazan = new Kazan();
        kazan.add(1);
        kazan.setKorgools(3);
        assertEquals(3, kazan.getKorgools());
    }

}