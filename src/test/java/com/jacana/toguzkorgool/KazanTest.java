package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KazanTest {

    @Test
    public void testInitialState() {
        Kazan kazan = new Kazan();
        assertEquals(0, kazan.getKorgools());
    }

    @Test
    public void testAdd() {
        Kazan kazan = new Kazan();
        kazan.add(1);
        assertEquals(1, kazan.getKorgools());
    }

    @Test
    public void testClear() {
        Kazan kazan = new Kazan();
        kazan.add(1);
        kazan.clear();
        assertEquals(0, kazan.getKorgools());
    }

    @Test
    public void testSetKorgools() {
        Kazan kazan = new Kazan();
        kazan.add(1);
        kazan.setKorgools(3);
        assertEquals(3, kazan.getKorgools());
    }

}