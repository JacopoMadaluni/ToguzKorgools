package com.jacana.toguzkorgool;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Player> players;
    private HumanPlayer lightPlayer; // light and dark
    private BotPlayer darkPlayer;
    private Player currentPlayer;

    public Board() {
        this.lightPlayer = new HumanPlayer(this, 0, Color.lightGray);
        this.darkPlayer = new BotPlayer(this, 1, Color.darkGray);
        players = new HashMap<>();
        players.put(0, lightPlayer);
        players.put(1, darkPlayer);
        this.currentPlayer = lightPlayer;
    }

    // ---- getters -----

    public Player getPlayer(int id) {
        return this.players.get(id);
    }

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
        return players.get((currentPlayer.getId()+1) % players.size());
    }

    /**
     * @param playerId The id of the player
     * @return The number of korgools in the player's kazan.
     */
    public int getKazanCount(int playerId){
        return players.get(playerId).getKazanCount();
    }

    /**
     * TODO remove
     * @return The number of korgools in light player's kazan
     */
    public int getLightKazanCount(){
        return lightPlayer.getKazanCount();
    }

    /**
     * TODO remove
     * @return The number of korgools in dark player's kazan
     */
    public int getDarkKazanCount(){
        return darkPlayer.getKazanCount();
    }

    /**
     *
     * @param playerId The player's id
     * @return The index of the tuz on player's side (beneficial to opponent player).
     */
    public int getTuzIndex(int playerId){
        Player player = players.get(playerId);
        if (player.hasTuz()){
            return player.getTuzIndex();
        }
        return -1;
    }

    /**
     * TODO remove
     * @return The index of the light player's tuz.
     * If he has no tuz, returns -1
     */
    public int getLightPlayerTuzIndex(){
        if (darkPlayer.hasTuz()){
            return darkPlayer.getTuzIndex();
        }
        return -1;
    }

    /**
     * TODO remove
     * @return The index of the dark player's tuz.
     * If he has no tuz, returns -1
     */
    public int getDarkPlayerTuzIndex(){
        if (lightPlayer.hasTuz()){
            return lightPlayer.getTuzIndex();
        }
        return -1;
    }

    /**
     *
     * @param playerId The player's id
     * @param index The hole index
     * @return The number of korgools in the hole
     */
    public int getHoleKorgoolCount(int playerId, int index){
        return players.get(playerId).getKorgoolsInHole(index);
    }

    /**
     * TODO remove
     * @param index The index of the hole.
     * @return Returns the number of korgools of the light player in the hole index
     */
    public int getLightHoleKorgoolCount(int index){
        return lightPlayer.getKorgoolsInHole(index);
    }

    /**
     * TODO remove
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

    /**
     * @return The index of the tuz in the opponent field.
     */
    public int getOpponentTuz() {
        return getCurrentOpponent().getTuzIndex();
    }

    /**
     * @return True if the current player's opponent has a tuz in his field.
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
     * Sets the kazan count of the player to the input number.
     * @param playerId The player's id
     * @param numberOfKorgools The new number of korgools
     */
    public void setKazanCount(int playerId, int numberOfKorgools){
        players.get(playerId).setKazanCount(numberOfKorgools);
    }
    /**
     * TODO remove
     * Sets the light player's kazan count.
     * @param numberOfKorgools New nuber of korgools in kazan.
     */
    public void setLightKazanCount(int numberOfKorgools){
        lightPlayer.setKazanCount(numberOfKorgools);
    }
    /**
     * TODO remove
     * Sets the dark player's kazan count.
     * @param numberOfKorgools New nuber of korgools in kazan.
     */
    public void setDarkKazanCount(int numberOfKorgools){
        darkPlayer.setKazanCount(numberOfKorgools);
    }

    /**
     * Sets the hole count of the player to the new number
     * @param playerId The player's id
     * @param index The hole's index
     * @param numberOfKorgools The new number of korgools in the hole
     */
    public void setHoleCout(int playerId, int index, int numberOfKorgools){
        Player player = players.get(playerId);
        player.clearHole(index);
        player.addToHole(index, numberOfKorgools);
    }
    /**
     * TODO remove
     * Sets the number of korgools in a light hole.
     * @param index The index of the hole
     * @param numberOfKorgools The new number of korgools
     */
    public void setLightHoleCount(int index, int numberOfKorgools){
        lightPlayer.clearHole(index);
        lightPlayer.addToHole(index, numberOfKorgools);
    }
    /**
     * TODO remove
     * Sets the number of korgools in a dark hole.
     * @param index The index of the hole
     * @param numberOfKorgools The new number of korgools
     */
    public void setDarkHoleCount(int index, int numberOfKorgools){
        darkPlayer.clearHole(index);
        darkPlayer.addToHole(index, numberOfKorgools);
    }

    /**
     * Sets the index hole on the player's side to be a tuz.
     * If the player already has a tuz, the previous tuz is removed and the new hole
     * is promoted to be tuz.
     * @param playerId The player's id.
     * @param holeIndex The hole's index.
     */
    public void setTuz(int playerId, int holeIndex){
        Player player = players.get(playerId);
        if (player.hasTuz()){
            int oldTuzIndex = player.getTuzIndex();
            Hole oldTuz = player.getHole(oldTuzIndex);
            oldTuz.setTuz(false);
        }
        player.getHole(holeIndex).setTuz(true);
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
    public Player getOpponentOf(int playerId){
        return players.get((playerId+1) % players.size());
    }

    public Player getOpponentOf(Player other){
        if (lightPlayer.equals(other)){
            return darkPlayer;
        }
        return lightPlayer;
    }

    public void changePlayer() {
        currentPlayer = players.get((currentPlayer.getId()+1) % players.size());
    }

    public void resetBoard() {
        for (int playerId = 0; playerId < players.size(); playerId++){
            players.get(playerId).resetPlayer();
        }
        currentPlayer = players.get(0);
    }
}
