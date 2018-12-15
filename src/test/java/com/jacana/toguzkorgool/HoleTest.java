package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HoleTest {

    /**
     * Ensure the construction of a hole results in the expected number of korgools.
     */
    @Test
    public void testInitialState() {
        Hole hole = new Hole();
        assertEquals(9, hole.getKorgools());
    }

    /**
     * Ensure adding korgools to a hole results in the expected number of korgools.
     */
    @Test
    public void testAdd() {
        Hole hole = new Hole();
        hole.add(1);
        assertEquals(10, hole.getKorgools());
    }

    /**
     * Ensure clearing all the korgools from a hole results in the expected number of korgools.
     */
    @Test
    public void testClear() {
        Hole hole = new Hole();
        hole.add(1);
        hole.clear();
        assertEquals(0, hole.getKorgools());
    }

    /**
     * Ensure setting the number of korgools in a hole results in the expected number of korgools.
     */
    @Test
    public void testSetKorgools() {
        Hole hole = new Hole();
        hole.add(1);
        hole.setKorgools(3);
        assertEquals(3, hole.getKorgools());
    }


    /**
     * Test marking a hole as a tuz.
     */
    @Test
    public void testTuz() {
        Hole hole = new Hole();
        hole.setTuz(true);
        assertTrue(hole.isTuz());
    }

    /**
     * Ensure marking a hole as a tuz then unmarking it results in the hole no longer being marked as a tuz.
     */
    @Test
    public void testTuzToggling() {
        Hole hole = new Hole();
        hole.setTuz(true);
        hole.setTuz(false);
        assertFalse(hole.isTuz());
    }

}
