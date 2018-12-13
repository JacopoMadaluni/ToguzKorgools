package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testChangePlayer(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        board.changePlayer();
        assertEquals(bot, board.getCurrentPlayer());
        board.changePlayer();
        assertEquals(player1, board.getCurrentPlayer());
    }

    @Test
    public void testPlayer1IdInitialization(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int actualId = player1.getId();
        int expectedId = 0;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testPlayer2IdInitialization(){
        Board board = new Board();
        Player player2 = board.getCurrentOpponent();
        int actualId = player2.getId();
        int expectedId = 1;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetHoleKorgoolCount(){
        Board board = new Board();
        assertEquals(9, board.getKorgoolsInHole(0,0));
        board.setKorgoolsInHole(0,0,3);
        assertEquals(3, board.getKorgoolsInHole(0,0));
    }

    @Test
    public void testGetHoleKorgoolCountWithInvalidPlayer(){
        Board board = new Board();
        int invalidId = 5;
        assertEquals(-1, board.getKorgoolsInHole(invalidId, 0));
    }

    @Test
    public void testSetTuzWhenThereIsNoTuz(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int id = player1.getId();
        int tuz_index = 0;
        board.setTuz(id, tuz_index);
        assertEquals(0, player1.getTuzIndex());
    }

    @Test
    public void testSetTuzWhenThereIsTuzAlready(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int id = player1.getId();
        int first_tuz_index = 0;
        board.setTuz(id, first_tuz_index);
        int second_tuz_index = 1;
        board.setTuz(id, second_tuz_index);
        assertEquals(second_tuz_index, board.getTuzIndex(id));
    }

    @Test
    public void testSetTuzWithInvalidPlayer(){
        Board board = new Board();
        int invalidId = 4;
        board.setTuz(invalidId, 3);
        assertEquals(-1, board.getTuzIndex(0));
        assertEquals(-1, board.getTuzIndex(1));
    }

    @Test
    public void testGetTuzIndexWhenThereIsNoTuz(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int id = player1.getId();
        assertEquals(-1, board.getTuzIndex(id));
    }

    @Test
    public void testGetTuzIndexWhereThereIsTuz(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int id = player1.getId();
        int tuz_index = 3;
        board.setTuz(id, tuz_index);
        assertEquals(tuz_index, board.getTuzIndex(id));
    }

    @Test
    public void testGetTuzIndexOfInvalidPlayer(){
        Board board = new Board();
        int invalidId = 3;
        int actual = board.getTuzIndex(invalidId);
        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetKazanCountWhenEmpty(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int id = player1.getId();
        int kazanCount = board.getKorgoolsInKazan(id);
        int expected = 0;
        assertEquals(expected, kazanCount);
    }

    @Test
    public void testGetKazanCountWhenNotEmpty(){
        Board board = new Board();
        Player player1 = board.getCurrentPlayer();
        int expectedCount = 3;
        player1.setKazanCount(expectedCount);
        int id = player1.getId();
        int actualCount = board.getKorgoolsInKazan(id);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testSetKazanCountWithInvalidPlayer(){
        Board board = new Board();
        int invalidId = 10;
        board.setKorgoolsInKazan(invalidId, 30);
        assertEquals(0, board.getKorgoolsInKazan(0));
        assertEquals(0, board.getKorgoolsInKazan(1));
    }

    @Test
    public void testSetHoleCountWithInvalidPlayer(){
        Board board = new Board();
        int invalidId = 10;
        board.setKorgoolsInHole(invalidId, 3,30);
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        for (int i = 0; i < 9; ++i){
            assertEquals(9, player1.getHole(i).getKorgools());
            assertEquals(9, player2.getHole(i).getKorgools());
        }
    }

    @Test
    public void testResetBoard(){
        Board board = new Board();
        board.setKorgoolsInKazan(0, 30);
        board.setKorgoolsInHole(0, 3, 3);
        board.setTuz(1, 3);
        board.resetBoard();

        assertEquals(0, board.getKorgoolsInKazan(0));
        assertEquals(0, board.getKorgoolsInKazan(1));
        assertEquals(9, board.getKorgoolsInHole(0, 3));
        assertEquals(-1, board.getTuzIndex(0));
        assertEquals(-1, board.getTuzIndex(1));
    }

    @Test
    public void testCurrentPlayerHasWonWhenHasWon(){
        Board board = new Board();
        board.setKorgoolsInKazan(0, 82);
        assertTrue(board.hasCurrentPlayerWon());
    }

    @Test
    public void testCurrentPlayerHasWonWhenHasNotWon(){
        Board board = new Board();
        assertFalse(board.hasCurrentPlayerWon());
    }

    @Test
    public void testPlayerHasWon(){
        Board board = new Board();
        board.setKorgoolsInKazan(1, 82);
        assertTrue(board.hasPlayerWon(1));
    }

    @Test
    public void testPlayerHasWonWithInvalidPlayer(){
        Board board = new Board();
        int invalidId = 3;
        assertFalse(board.hasPlayerWon(invalidId));
    }

}
