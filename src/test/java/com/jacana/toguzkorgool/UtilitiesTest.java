package com.jacana.toguzkorgool;

import org.junit.Test;

import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilitiesTest {

    /**
     * Ensure parsing a positive integer as a String returns the positive integer.
     */
    @Test
    public void testTryParseIntPositive() {
        OptionalInt parsedInt = Utilities.tryParseInt("1");
        assertTrue(parsedInt.isPresent());
        assertEquals(parsedInt.getAsInt(), 1);
    }

    /**
     * Ensure parsing a negative integer as a String returns the positive negative.
     */
    @Test
    public void testTryParseIntNegative() {
        OptionalInt parsedInt = Utilities.tryParseInt("-1");
        assertTrue(parsedInt.isPresent());
        assertEquals(parsedInt.getAsInt(), -1);
    }

    /**
     * Ensure parsing a null String returns an empty OptionalInt.
     */
    @Test
    public void testTryParseIntNull() {
        OptionalInt parsedInt = Utilities.tryParseInt(null);
        assertFalse(parsedInt.isPresent());
    }

    /**
     * Ensure parsing a non-numeric String returns an empty OptionalInt.
     */
    @Test
    public void testTryParseIntNonNumeric() {
        OptionalInt parsedInt = Utilities.tryParseInt("Test");
        assertFalse(parsedInt.isPresent());
    }

}
