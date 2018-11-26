package com.jacana.toguzkorgool;

import java.awt.Color;

public class Board {
    private HumanPlayer player; // light and dark
    private BotPlayer bot;
    private Player currentPlayer;

    public Board() {
        this.player = new HumanPlayer(this, Color.lightGray);
        this.bot = new BotPlayer(this, Color.darkGray);
        this.currentPlayer = player;
    }

    public Player getPlayer() {
        return currentPlayer;
    }

    public Player getOpponent() {
        if (currentPlayer == player){
            return bot;
        }
        return player;
    }

    public boolean opponentHasTuz(){
        Player opponent = getOpponent();
        return opponent.hasTuz();
    }

    public void setTuzInOpponentField(int index){
        Player opponent = getOpponent();
        opponent.setTuz(index);
    }

    public int getKorgoolsInOpponentHole(int index){
        Player opponent = getOpponent();
        return opponent.getKorgoolsInHole(index);
    }

    public void clearOpponentHole(int index){
        Player opponent = getOpponent();
        opponent.clearHole(index);
    }

    public void addToOpponentHole(int index, int amount){
        Player opponent = getOpponent();
        opponent.addToHole(index, amount);
    }

    public void addToOpponentKazan(int amount){
        Player opponent = getOpponent();
        opponent.addToKazan(amount);
    }

    // getOpponentStuff...

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

    public void resetBoard() {
        player.resetPlayer();
        bot.resetPlayer();
        currentPlayer = player;
    }
}
