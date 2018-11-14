package com.jacana.toguzkorgool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    @Test
    public void testMakeMove() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getNextPlayer();
        player.makeMove(1);
        assertEquals(1, player.getHole(0).getKorgools()); // hole one has one korgool
        for (int i = 1; i < 9; ++i) {
            // holes two to nine have ten korgools each
            assertEquals(10, player.getHole(i).getKorgools());
        }
        player.makeMove(1);
        assertEquals(0, player.getHole(0).getKorgools()); // hole one has zero korgools
        assertEquals(11, player.getHole(1).getKorgools()); // hole two has eleven korgools
        player.makeMove(2);
        assertEquals( 1, player.getHole(1).getKorgools()); // hole two has one korgool
        for (int i = 2; i < 9; ++i) {
            // holes three to nine have eleven korgools each
            assertEquals(11, player.getHole(i).getKorgools());
        }
        for (int i = 0; i < 3; ++i) {
            // opponent holes one to three have ten korgools each
            assertEquals(10, bot.getHole(i).getKorgools());
        }
        player.makeMove(9);
        assertEquals(1, player.getHole(8).getKorgools()); // hole nine has one korgool
        for (int i = 0; i < 3; ++i) {
            // opponent holes one to three have eleven korgools each
            assertEquals(11, bot.getHole(i).getKorgools());
        }
        for (int i = 3; i < 9; ++i) {
            // opponent holes four to nine have ten korgools each
            assertEquals(10, bot.getHole(i).getKorgools());
        }
        assertEquals(1, player.getHole(0).getKorgools()); // hole one has one korgool
    }

}
