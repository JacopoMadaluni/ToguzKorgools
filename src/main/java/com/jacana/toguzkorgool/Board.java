package com.jacana.toguzkorgool;

import java.awt.Color;

public class Board {
    private HumanPlayer lightPlayer; // light and dark
    private BotPlayer darkPlayer;
    private Player currentPlayer;

    public Board() {
        this.lightPlayer = new HumanPlayer(this, Color.lightGray);
        this.darkPlayer = new BotPlayer(this, Color.darkGray);
        this.currentPlayer = lightPlayer;
    }

    // ---- getters -----

    /**
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return The opponent of the current player
     */
    public Player getCurrentOpponent() {
        if (currentPlayer == lightPlayer){
            return darkPlayer;
        }
        return lightPlayer;
    }

    /**
     * @return The number of korgools in light player's kazan
     */
    public int getLightKazanCount(){
        return lightPlayer.getKazanCount();
    }

    /**
     * @return The number of korgools in dark player's kazan
     */
    public int getDarkKazanCount(){
        return darkPlayer.getKazanCount();
    }

    /**
     * @return The index of the light player tuz.
     * If he has no tuz, returns -1
     */
    public int getLightTuzIndex(){
        if (lightPlayer.hasTuz()){
            return lightPlayer.getTuzIndex();
        }
        return -1;
    }

    /**
     * @return The index of the light player tuz.
     * If he has no tuz, returns -1
     */
    public int getDarkTuzIndex(){
        if (darkPlayer.hasTuz()){
            return darkPlayer.getTuzIndex();
        }
        return -1;
    }

    /**
     * @param index The index of the hole.
     * @return Returns the number of korgools of the light player in the hole index
     */
    public int getLightHoleKorgoolCount(int index){
        return lightPlayer.getKorgoolsInHole(index);
    }

    /**
     * @param index The index of the hole.
     * @return Returns the number of korgools of the dark player in the hole index
     */
    public int getDarkHoleKorgoolCount(int index){
        return darkPlayer.getKorgoolsInHole(index);
    }

    /**
     * @param index The index of the hole
     * @return The number of korgools in the index hole relative to the current
     * player's opponent.
     */
    public int getOpponentHoleKorgoolCount(int index){
        Player opponent = getCurrentOpponent();
        return opponent.getKorgoolsInHole(index);
    }

    public int getOpponentTuz() {
        return getCurrentOpponent().getTuzIndex();
    }

    /**
     * @return True if the current player's opponent has a tuz
     */
    public boolean opponentHasTuz(){
        Player opponent = getCurrentOpponent();
        return opponent.hasTuz();
    }

    /**
     * @return True if the current player has won
     */
    public boolean currentPlayerHasWon(){
        return currentPlayer.hasWon();
    }

    // ---- setters -----

    /**
     * Sets the light player's kazan count.
     * @param numberOfKorgools New nuber of korgools in kazan.
     */
    public void setLightKazanCount(int numberOfKorgools){
        lightPlayer.setKazanCount(numberOfKorgools);
    }
    /**
     * Sets the dark player's kazan count.
     * @param numberOfKorgools New nuber of korgools in kazan.
     */
    public void setDarkKazanCount(int numberOfKorgools){
        darkPlayer.setKazanCount(numberOfKorgools);
    }

    /**
     * Sets the number of korgools in a light hole.
     * @param index The index of the hole
     * @param numberOfKorgools The new number of korgools
     */
    public void setLightHoleCount(int index, int numberOfKorgools){
        lightPlayer.clearHole(index);
        lightPlayer.addToHole(index, numberOfKorgools);
    }
    /**
     * Sets the number of korgools in a dark hole.
     * @param index The index of the hole
     * @param numberOfKorgools The new number of korgools
     */
    public void setDarkHoleCount(int index, int numberOfKorgools){
        darkPlayer.clearHole(index);
        darkPlayer.addToHole(index, numberOfKorgools);
    }

    /**
     * Set the index hole of the dark player to be the tuz of the light player.
     * If the player already has a tuz, the previous tuz is removed and the new hole
     * is promoted to be tuz.
     * @param index The hole index
     */
    public void setLightPlayerTuz(int index){
        if (darkPlayer.hasTuz()){
            int oldTuzIndex = darkPlayer.getTuzIndex();
            Hole oldTuz = darkPlayer.getHole(oldTuzIndex);
            oldTuz.setTuz(false);
        }
        darkPlayer.getHole(index).setTuz(true);
    }
    /**
     * Set the index hole of the light player to be the tuz of the light player.
     * If the player already has a tuz, the previous tuz is removed and the new hole
     * is promoted to be tuz.
     * @param index The hole index
     */
    public void setDarkPlayerTuz(int index){
        if (lightPlayer.hasTuz()){
            int oldTuzIndex = lightPlayer.getTuzIndex();
            Hole oldTuz = lightPlayer.getHole(oldTuzIndex);
            oldTuz.setTuz(false);
        }
        lightPlayer.getHole(index).setTuz(true);
    }

    /**
     * Sets a hole in the opponent field to be a tuz, if possible.
     * @param index The index of the hole.
     */
    public void setOpponentTuz(int index){
        Player opponent = getCurrentOpponent();
        opponent.setTuz(index);
    }

    public void clearOpponentHole(int index){
        Player opponent = getCurrentOpponent();
        opponent.clearHole(index);
    }

    public void addToOpponentHole(int index, int amount){
        Player opponent = getCurrentOpponent();
        opponent.addToHole(index, amount);
    }

    public void addToOpponentKazan(int amount){
        Player opponent = getCurrentOpponent();
        opponent.addToKazan(amount);
    }

    // getOpponentStuff...

    public Player getOpponentOf(Player other){
        if (lightPlayer.equals(other)){
            return darkPlayer;
        }
        return lightPlayer;
    }

    public void changePlayer() {
        if (currentPlayer == lightPlayer){
            currentPlayer = darkPlayer;
        } else {
            currentPlayer = lightPlayer;
        }
    }

    public void resetBoard() {
        lightPlayer.resetPlayer();
        darkPlayer.resetPlayer();
        currentPlayer = lightPlayer;
    }
}
