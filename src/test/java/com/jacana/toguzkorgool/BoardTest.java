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
        Player player1 = board.getCurrentPlayer();
        Player bot = board.getNextPlayer();
        board.changePlayer();
        assert(board.getCurrentPlayer().equals(bot));
        board.changePlayer();
        assert(board.getCurrentPlayer().equals(player1));
    }
}
