package com.jacana.toguzkorgool;

public class Board {
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;

    public Board(){
        this.playerOne = new Player();
        this.playerTwo = new Player();
        this.currentPlayer = playerOne;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getNextPlayer(){
        if (currentPlayer == playerOne){
            return playerTwo;
        }
        return playerOne;
    }

    public void changePlayer(){
        if (currentPlayer == playerOne){
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }
}
