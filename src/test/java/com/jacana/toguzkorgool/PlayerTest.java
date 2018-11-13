package com.jacana.toguzkorgool;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void testMakeMove() {
        Board board = new Board();
        Player player = board.getCurrentPlayer();
        Player bot = board.getNextPlayer();
        player.makeMove(1);
        assertEquals(player.getHole(0).getKorgools(), 1); // hole one has one korgool
        for (int i = 1; i < 9; ++i) {
            // holes two to nine have ten korgools each
            assertEquals(player.getHole(i).getKorgools(), 10);
        }
        player.makeMove(1);
        assertEquals(player.getHole(0).getKorgools(), 0); // hole one has zero korgools
        assertEquals(player.getHole(1).getKorgools(), 11); // hole two has eleven korgools
        player.makeMove(2);
        assertEquals(player.getHole(1).getKorgools(), 1); // hole two has one korgool
        for (int i = 2; i < 9; ++i) {
            // holes three to nine have eleven korgools each
            assertEquals(player.getHole(i).getKorgools(), 10);
        }
        for (int i = 0; i < 3; ++i) {
            // opponent holes one to three have ten korgools each
            assertEquals(bot.getHole(i).getKorgools(), 10);
        }
        player.makeMove(9);
        assertEquals(player.getHole(8).getKorgools(), 1); // hole nine has one korgool
        for (int i = 0; i < 3; ++i) {
            // opponent holes one to three have eleven korgools each
            assertEquals(bot.getHole(i).getKorgools(), 11);
        }
        for (int i = 3; i < 9; ++i) {
            // opponent holes four to nine have ten korgools each
            assertEquals(bot.getHole(i).getKorgools(), 10);
        }
        assertEquals(player.getHole(0).getKorgools(), 1); // hole one has one korgool
    }

}
