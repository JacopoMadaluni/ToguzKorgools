package com.jacana.toguzkorgool;

import org.junit.Test;

public class BoardTest {

    @Test
    public void sampleTest(){
        // ...
    }

    @Test
    public void testChangePlayer(){
        Board board = new Board();
        Player player1 = board.getPlayer();
        Player bot = board.getOpponent();
        board.changePlayer();
        assert(board.getPlayer().equals(bot));
        board.changePlayer();
        assert(board.getPlayer().equals(player1));
    }
}
