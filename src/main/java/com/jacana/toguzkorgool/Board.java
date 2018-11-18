package com.jacana.toguzkorgool;

public class Board {
    private Player player;
    private Player bot;
    private Player currentPlayer;

    public Board() {
        this.player = new Player(this);
        this.bot = new Player(this);
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        if (currentPlayer == player){
            return bot;
        }
        return player;
    }

    public Player getOpponentOf(Player other){
        if (player.equals(other)){
            return bot;
        }
        return player;
    }

    public void changePlayer() {
        if (currentPlayer == player){
            currentPlayer = bot;
        } else {
            currentPlayer = player;
        }
    }
}
