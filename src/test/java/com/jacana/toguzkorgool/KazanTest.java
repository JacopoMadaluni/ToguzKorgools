package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KazanTest {

    @Test
    public void testInitialState() {
        Kazan kazan = new Kazan();
        assertEquals(0, kazan.getKorgools());
    }

}