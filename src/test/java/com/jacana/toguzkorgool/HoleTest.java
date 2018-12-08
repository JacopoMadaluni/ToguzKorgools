package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.*;

public class HoleTest {

    @Test
    public void testInitialState() {
        Hole hole = new Hole();
        assertEquals(9, hole.getKorgools());
    }

    @Test
    public void testAdd() {
        Hole hole = new Hole();
        hole.add(1);
        assertEquals(1, hole.getKorgools());
    }

    @Test
    public void testClear() {
        Hole hole = new Hole();
        hole.add(1);
        hole.clear();
        assertEquals(0, hole.getKorgools());
    }

    @Test
    public void testTuz() {
        Hole hole = new Hole();
        hole.setTuz(true);
        assertTrue(hole.isTuz());
    }

    @Test
    public void testTuzToggling() {
        Hole hole = new Hole();
        hole.setTuz(true);
        hole.setTuz(false);
        assertFalse(hole.isTuz());
    }

}
