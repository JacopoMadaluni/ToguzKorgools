package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    /**
     * Ensure the Player instance initialises correctly with the expected number of korgools in the kazan and each hole.
     */
    @Test
    public void testPlayerInitialisation() {
        Board expectedBoard = new Board();
        int expectedId = 10;
        Player aPlayer = new HumanPlayer(expectedBoard, expectedId);
        int actualId = aPlayer.getId();
        Board actualBoard = aPlayer.getBoard();
        assertEquals(expectedBoard, actualBoard);
        assertEquals(expectedId, actualId);
        for (int i = 0; i < 9; ++i) {
            assertEquals(9, aPlayer.getHole(i).getKorgools());
        }
        assertEquals(0, aPlayer.getKorgoolsInKazan());
    }

    /**
     * Ensure the hole marked as a tuz cannot be hole 9.
     */
    @Test
    public void testSetTuzWhenHoleNumberIsEight() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        player.setTuz(8);
        assertFalse(player.hasTuz());
    }

    /**
     * Ensure more than one hole cannot be marked as a tuz.
     */
    @Test
    public void testSetTuzWhenHasTuz() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        player.setTuz(5);
        assertEquals(5, player.getTuzIndex());
        player.setTuz(6);
        assertFalse(player.getHole(6).isTuz());
        assertEquals(5, player.getTuzIndex());
    }

    /**
     * Ensure the opponent player cannot have the same hole marked as a tuz as the current player.
     */
    @Test
    public void testSetTuzWhenOpponentTuzIndexIsSame() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.setTuz(5);
        bot.setTuz(5);
        assertFalse(bot.hasTuz());
    }

    /**
     * Ensure making a move on the player's last hole when it contains one korgool results in the correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveOnHoleNineAndOneKorgool() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(8);
        player.addToHole(8, 1);
        player.makeMove(8);
        for (int i = 0; i < 8; ++i) {
            // Player holes 1 to 8 have 9 korgools each
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(0, player.getHole(8).getKorgools()); // Player hole 9 has 0 korgools
        assertEquals(0, bot.getHole(0).getKorgools()); // Bot hole 1 has 0 korgools
        for (int i = 1; i < 9; ++i) {
            // Bot holes 2 to 9 have 9 korgools each
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertEquals(10, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move on a hole with one korgool that lands in a non-tuz hole on the player's side results in the correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveOnAnyHoleAndOneKorgoolNoTuz() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(7);
        player.addToHole(7, 1);
        player.makeMove(7);
        for (int i = 0; i < 7; ++i) {
            // Player holes 1 to 7 have 9 korgools each
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(0, player.getHole(7).getKorgools()); // player hole 8 has 0 korgools
        assertEquals(10, player.getHole(8).getKorgools()); // player hole 9 has 10 korgools
        for (int i = 0; i < 9; ++i) {
            // Bot holes 1 to 9 have 9 korgools each
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertEquals(0, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move on a hole with one korgool that lands in a tuz on the player's side results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveOnAnyHoleAndOneKorgoolAndTuz() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(6);
        player.addToHole(6, 1);
        player.clearHole(7);
        player.setTuz(7);
        player.makeMove(6);
        assertEquals(0, player.getHole(6).getKorgools());
        assertEquals(0, player.getHole(7).getKorgools());
        assertEquals(1, bot.getKorgoolsInKazan());
        for (int i = 0; i < 9; ++i) {
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        for (int i = 0; i < 9; ++i) {
            if (i != 6 && i != 7) assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(0, player.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move that doesn't spill over to the opponent's side on a hole with multiple korgools and no tuz set results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveWithMultipleKorgoolsOneSideNoTuz() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.makeMove(0);
        assertEquals(1, player.getHole(0).getKorgools());
        for (int i = 1; i < 9; ++i) {
            // Player holes 2 to 8 have 10 korgools each
            assertEquals(10, player.getHole(i).getKorgools());
        }
        for (int i = 0; i < 9; ++i) {
            // Bot holes 1 to 9 have 9 korgools each
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertEquals(0, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move that doesn't spill over to the opponent's side on a hole with multiple korgools and passes over a tuz results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveWithMultipleKorgoolsOneSideWithTuz() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(5);
        player.setTuz(5);
        player.makeMove(0);
        for (int i = 0; i < 9; ++i) {
            if (i == 0) assertEquals(1, player.getHole(i).getKorgools());
            else if (i == 5) assertEquals(0, player.getHole(i).getKorgools());
            else assertEquals(10, player.getHole(i).getKorgools());
        }
        assertEquals(0, player.getKorgoolsInKazan());
        assertEquals(1, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move that spills over to the opponent's side and passes over a tuz results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveOnBothSidesAndPassesOverTuz() {
        // When moveOpponent() is called and korgool placement passes over a tuz
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(8);
        player.addToHole(8, 4);
        bot.clearHole(1);
        bot.setTuz(1);
        player.makeMove(8);
        for (int i = 0; i < 8; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(8).getKorgools());
        assertEquals(10, bot.getHole(0).getKorgools());
        assertEquals(0, bot.getHole(1).getKorgools());
        assertEquals(0, bot.getHole(2).getKorgools());
        for (int i = 3; i < 9; ++i) {
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertTrue(bot.hasTuz());
        assertFalse(player.hasTuz());
        assertEquals(11, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move that spills over to the opponent's side, where the last hole to have korgools placed contains even number of korgools, results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveOnBothSidesWithEvenLastHole() {
        // When moveOpponent() is called and last hole has an even number of korgools
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(8);
        player.addToHole(8, 3);
        bot.clearHole(1);
        bot.addToHole(1, 3);
        player.makeMove(8);
        for (int i = 0; i < 8; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(8).getKorgools());
        assertEquals(10, bot.getHole(0).getKorgools());
        assertEquals(0, bot.getHole(1).getKorgools());
        for (int i = 2; i < 9; ++i) {
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertFalse(bot.hasTuz());
        assertFalse(player.hasTuz());
        assertEquals(4, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move that spills over to the opponent's side, where the last hole to have korgools placed contains an odd number of korgools that isn't 3, results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveOnBothSidesWithOddLastHole() {
        // When moveOpponent() is called and last hole has an odd (not 3) number of korgools
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(8);
        player.addToHole(8, 3);
        bot.clearHole(1);
        bot.addToHole(1, 4);
        player.makeMove(8);
        for (int i = 0; i < 8; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(8).getKorgools());
        assertEquals(10, bot.getHole(0).getKorgools());
        assertEquals(5, bot.getHole(1).getKorgools());
        for (int i = 2; i < 9; ++i) {
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertFalse(bot.hasTuz());
        assertFalse(player.hasTuz());
        assertEquals(0, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
    }

    /**
     * Ensure making a move that spills over to the opponent's side, who doesn't have a tuz, where the last hole to have korgools placed contains 3 korgools, results in correct number of korgools in each hole and kazan and a correctly set tuz.
     */
    @Test
    public void testMoveOnBothSidesWithThreeInLastHoleAndNoTuz() {
        // When moveOpponent() is called and last hole has 3 korgools and tuz is not set
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(8);
        player.addToHole(8, 3);
        bot.clearHole(1);
        bot.addToHole(1, 2);
        player.makeMove(8);
        for (int i = 0; i < 8; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(8).getKorgools());
        assertEquals(10, bot.getHole(0).getKorgools());
        assertEquals(0, bot.getHole(1).getKorgools());
        for (int i = 2; i < 9; ++i) {
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertEquals(3, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
        assertTrue(bot.getHole(1).isTuz());
    }

    /**
     * Ensure making a move that spills over to the opponent's side, who has a tuz, where the last hole to have korgools placed contains 3 korgools, results in correct number of korgools in each hole and kazan and there is no change in tuz position.
     */
    @Test
    public void testMoveOnBothSidesWithThreeInLastHoleAndTuz() {
        // When moveOpponent() is called and last hole has 3 korgools but tuz is already set
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.clearHole(8);
        player.addToHole(8, 3);
        bot.clearHole(1);
        bot.addToHole(1, 2);
        bot.clearHole(2);
        bot.setTuz(2);
        player.makeMove(8);
        for (int i = 0; i < 8; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(8).getKorgools());
        assertEquals(10, bot.getHole(0).getKorgools());
        assertEquals(3, bot.getHole(1).getKorgools());
        assertEquals(0, bot.getHole(2).getKorgools());
        for (int i = 3; i < 9; ++i) {
            assertEquals(9, bot.getHole(i).getKorgools());
        }
        assertEquals(0, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
        assertFalse(bot.getHole(1).isTuz());
        assertTrue(bot.getHole(2).isTuz());
    }

    /**
     * Ensure making a move that spills over to the opponent's side and back to the player's side results in correct number of korgools in each hole and kazan.
     */
    @Test
    public void testMoveWhenKorgoolsSpillBackToPlayer() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.addToHole(8, 2); // hole 9 has 11 korgools
        player.makeMove(8);
        for (int i = 0; i < 9; ++i) {
            assertEquals(10, bot.getHole(i).getKorgools());
        }
        assertEquals(10, player.getHole(0).getKorgools());
        for (int i = 1; i < 8; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(8).getKorgools());
        assertEquals(0, player.getKorgoolsInKazan());
        assertEquals(0, bot.getKorgoolsInKazan());
        assertFalse(bot.hasTuz());
        assertFalse(player.hasTuz());
    }

    /**
     * Ensure hasWon returns false when the win condition is not met.
     */
    @Test
    public void testHasWonWhenHasNotWon() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        assertFalse(player.hasWon());
        assertFalse(bot.hasWon());
    }

    /**
     * Ensure hasWon returns true when the win condition is met.
     */
    @Test
    public void testHasWonWhenHasWon() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getCurrentOpponent();
        player.addToKazan(player.getNumberOfHoles() * player.getNumberOfHoles() + 1);
        assertTrue(player.hasWon());
        assertFalse(bot.hasWon());
    }

    /**
     * Ensure the player's holes are reset to their initialisation values, kazan is emptied and tuz removed.
     */
    @Test
    public void testResetPlayer() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        for (int i = 0; i < 9; ++i) {
            player.addToHole(i, 1);
            assertEquals(10, player.getHole(i).getKorgools());
        }
        player.addToKazan(1);
        assertEquals(1, player.getKorgoolsInKazan());
        player.setTuz(3);
        assertTrue(player.hasTuz());
        assertEquals(3, player.getTuzIndex());
        player.resetPlayer();
        for (int i = 0; i < 9; ++i) {
            assertEquals(9, player.getHole(i).getKorgools());
        }
        assertFalse(player.hasTuz());
        assertEquals(0, player.getKorgoolsInKazan());
    }

    /**
     * Ensure that when cleared, tuz is no longer set to one of the holes.
     */
    @Test
    public void testClearTuz() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        player.setTuz(1);
        player.setTuz(-1, false);
        assertEquals(-1, player.getTuzIndex());
    }

}
