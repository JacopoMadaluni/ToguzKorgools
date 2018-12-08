package com.jacana.toguzkorgool;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Integer, Player> players;
    private Player currentPlayer;

    public Board() {
        Player player1 = new HumanPlayer(this, 0, Color.lightGray);
        Player player2 = new BotPlayer(this, 1, Color.darkGray);
        players = new HashMap<>();
        players.put(player1.getId(), player1);
        players.put(player2.getId(), player2);
        this.currentPlayer = player1;
    }

    // ---- getters -----

    public Player getPlayer(int id) {
        return this.players.get(id);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(this.players.values());
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
     *
     * @param playerId The player's id
     * @return The index of the tuz on player's side (beneficial to opponent player).
     */
    public int getTuzIndex(int playerId){
        Player player = players.get(playerId);
        if (player == null) return -1;
        if (player.hasTuz()){
            return player.getTuzIndex();
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
        Player player = players.get(playerId);
        if (player == null) return -1;
        return player.getKorgoolsInHole(index);
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
        Player player = players.get(playerId);
        if (player == null) return;
        player.setKazanCount(numberOfKorgools);
    }

    /**
     * Sets the hole count of the player to the new number
     * @param playerId The player's id
     * @param index The hole's index
     * @param numberOfKorgools The new number of korgools in the hole
     */
    public void setHoleCount(int playerId, int index, int numberOfKorgools){
        Player player = players.get(playerId);
        if (player == null) return;
        player.clearHole(index);
        player.addToHole(index, numberOfKorgools);
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
        if (player == null) return;
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
        return players.get((playerId + 1) % players.size());
    }

    public void changePlayer() {
        currentPlayer = players.get((currentPlayer.getId() + 1) % players.size());
    }

    public void resetBoard() {
        for (Player player : players.values()){
            player.resetPlayer();
        }
        currentPlayer = players.get(0);
    }

}
