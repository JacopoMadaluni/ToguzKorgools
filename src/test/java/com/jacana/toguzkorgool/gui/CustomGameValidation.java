package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Constants;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CustomGameValidation {

    @Test
    public void testValidateBoardValid() {
        Board validBoard = new Board();
        assertNull(CustomGameDialog.validateBoard(validBoard));
    }

    @Test
    public void testValidateBoardInvalidHole() {
        Board invalidBoard = new Board();
        invalidBoard.setKorgoolsInHole(0, 0, -1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
        invalidBoard.setKorgoolsInHole(0, 0, Constants.CONSTRAINT_TOTAL_KORGOOLS + 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardMultipleTuz() {
        Board invalidBoard = new Board();
        invalidBoard.getPlayer(0).getHole(0).setTuz(true);
        invalidBoard.getPlayer(0).getHole(1).setTuz(true);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardTuzHole9() {
        Board invalidBoard = new Board();
        invalidBoard.setTuz(0, 8);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardSameTuz() {
        Board invalidBoard = new Board();
        invalidBoard.setTuz(0, 1);
        invalidBoard.setTuz(1, 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardInvalidKazan() {
        Board invalidBoard = new Board();
        invalidBoard.setKorgoolsInKazan(0, -1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
        invalidBoard.setKorgoolsInKazan(0, Constants.CONSTRAINT_TOTAL_KORGOOLS + 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

}
