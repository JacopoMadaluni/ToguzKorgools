package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Constants;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CustomGameValidation {

    /**
     * Test validating a valid default (newly constructed) board.
     */
    @Test
    public void testValidateBoardValid() {
        Board validBoard = new Board();
        assertNull(CustomGameDialog.validateBoard(validBoard));
    }

    /**
     * Test validating a default board with an invalid number of korgools in a hole resulting in an error.
     */
    @Test
    public void testValidateBoardInvalidHole() {
        Board invalidBoard = new Board();
        invalidBoard.setKorgoolsInHole(0, 0, -1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
        invalidBoard.setKorgoolsInHole(0, 0, Constants.CONSTRAINT_TOTAL_KORGOOLS + 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    /**
     * Test validating a default board with a player having 2 holes marked as a tuz resulting in an error.
     */
    @Test
    public void testValidateBoardMultipleTuz() {
        Board invalidBoard = new Board();
        invalidBoard.getPlayer(0).getHole(0).setTuz(true);
        invalidBoard.getPlayer(0).getHole(1).setTuz(true);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    /**
     * Test validating a default board with a player having hole 9 marked as a tuz resulting in an error.
     */
    @Test
    public void testValidateBoardTuzHole9() {
        Board invalidBoard = new Board();
        invalidBoard.setTuz(0, 8);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    /**
     * Test validating a default board with 2 players having the same hole marked as a tuz resulting in an error.
     */
    @Test
    public void testValidateBoardSameTuz() {
        Board invalidBoard = new Board();
        invalidBoard.setTuz(0, 1);
        invalidBoard.setTuz(1, 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    /**
     * Test validating a default board with an invalid number of korgools in a kazan resulting in an error.
     */
    @Test
    public void testValidateBoardInvalidKazan() {
        Board invalidBoard = new Board();
        invalidBoard.setKorgoolsInKazan(0, -1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
        invalidBoard.setKorgoolsInKazan(0, Constants.CONSTRAINT_TOTAL_KORGOOLS + 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

}
