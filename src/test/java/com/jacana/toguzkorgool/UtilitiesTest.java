package com.jacana.toguzkorgool;

import org.junit.Test;

import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilitiesTest {

    @Test
    public void testTryParseIntPositive() {
        OptionalInt parsedInt = Utilities.tryParseInt("1");
        assertTrue(parsedInt.isPresent());
        assertEquals(parsedInt.getAsInt(), 1);
    }

    @Test
    public void testTryParseIntNegative() {
        OptionalInt parsedInt = Utilities.tryParseInt("-1");
        assertTrue(parsedInt.isPresent());
        assertEquals(parsedInt.getAsInt(), -1);
    }

    @Test
    public void testTryParseIntNull() {
        OptionalInt parsedInt = Utilities.tryParseInt(null);
        assertFalse(parsedInt.isPresent());
    }

    @Test
    public void testTryParseIntNonNumeric() {
        OptionalInt parsedInt = Utilities.tryParseInt("Test");
        assertFalse(parsedInt.isPresent());
    }

}
