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
        currentPlayer = player1;
    }

    /**
     * Add korgools to a hole on the current opponent's field.
     *
     * @param index The hole index
     * @param amount The number of korgools to add
     */
    public void addToOpponentHole(int index, int amount) {
        Player opponent = getCurrentOpponent();
        opponent.addToHole(index, amount);
    }

    /**
     * Add korgools to the current opponent's kazan.
     *
     * @param amount The number of korgools to add
     */
    public void addToOpponentKazan(int amount) {
        Player opponent = getCurrentOpponent();
        opponent.addToKazan(amount);
    }

    /**
     * Clear the korgools in a hole on the current opponent's field.
     *
     * @param index The hole index
     */
    public void clearOpponentHole(int index) {
        Player opponent = getCurrentOpponent();
        opponent.clearHole(index);
    }

    /**
     * Change the current player to the next player.
     */
    public void changePlayer() {
        currentPlayer = players.get((currentPlayer.getId() + 1) % players.size());
    }

    /**
     * @return True if the current player has won
     */
    public boolean currentPlayerHasWon() {
        return currentPlayer.hasWon();
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
        return getOpponentOf(currentPlayer.getId());
    }

    /**
     * @param playerId The player's ID
     * @param index The hole index
     * @return The number of korgools in the hole of the player
     */
    public int getHoleKorgoolCount(int playerId, int index) {
        Player player = players.get(playerId);
        if (player == null) return -1;
        return player.getKorgoolsInHole(index);
    }

    /**
     * @param playerId The ID of the player
     * @return The number of korgools in the player's kazan
     */
    public int getKazanCount(int playerId) {
        return players.get(playerId).getKazanCount();
    }

    /**
     * @param playerId The player ID
     * @return The opponent of the player
     */
    public Player getOpponentOf(int playerId) {
        return players.get((playerId + 1) % players.size());
    }

    /**
     * @param index The index of the hole
     * @return The number of korgools in the hole on the opponent's field.
     */
    public int getOpponentHoleKorgoolCount(int index) {
        Player opponent = getCurrentOpponent();
        return opponent.getKorgoolsInHole(index);
    }

    /**
     * @return The hole index of the tuz in the opponent's field
     */
    public int getOpponentTuz() {
        return getCurrentOpponent().getTuzIndex();
    }

    /**
     * @param id The player ID
     * @return The player instance with the specified ID
     */
    public Player getPlayer(int id) {
        return this.players.get(id);
    }

    /**
     * @return A list of all players in the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players.values());
    }

    /**
     * @return The number of players in the game
     */
    public int getPlayerCount() {
        return this.players.size();
    }

    /**
     * @param playerId The player's ID
     * @return The hole index of the tuz on the player's side of the board
     */
    public int getTuzIndex(int playerId) {
        Player player = players.get(playerId);
        if (player == null) return -1;
        if (player.hasTuz()) {
            return player.getTuzIndex();
        }
        return -1;
    }

    /**
     * @return True if the current opponent has a tuz in their side of the field
     */
    public boolean opponentHasTuz() {
        Player opponent = getCurrentOpponent();
        return opponent.hasTuz();
    }

    /**
     * @param playerId The player ID
     * @return True if the player has won
     */
    public boolean playerHasWon(int playerId) {
        Player player = players.get(playerId);
        if (player == null) return false;
        return player.hasWon();
    }

    /**
     * Reset the board, resetting the number of korgools in all holes and kazans.
     * Set the current player to player 1.
     */
    public void resetBoard() {
        for (Player player : players.values()) {
            player.resetPlayer();
        }
        currentPlayer = players.get(0);
    }

    /**
     * Set the number of korgools in a hole on a player's field.
     *
     * @param playerId The player ID
     * @param index The hole index
     * @param numberOfKorgools The new number of korgools
     */
    public void setHoleCount(int playerId, int index, int numberOfKorgools) {
        Player player = players.get(playerId);
        if (player == null) return;
        player.clearHole(index);
        player.addToHole(index, numberOfKorgools);
    }

    /**
     * Set the number of korgools in the kazan of a player.
     *
     * @param playerId The player ID
     * @param numberOfKorgools The new number of korgools
     */
    public void setKazanCount(int playerId, int numberOfKorgools) {
        Player player = players.get(playerId);
        if (player == null) return;
        player.setKazanCount(numberOfKorgools);
    }

    /**
     * Set a hole in the opponent's field to be/to not be a tuz.
     *
     * @param index The index of the hole. If -1 and tuzFlag is false, set all holes to no longer be a tuz.
     * @param tuzFlag Whether or not the hole should become a tuz.
     * @return True if the tuz flag was set. May return false if an invalid hole was passed, e.g. hole 9 (index 8)
     */
    public boolean setOpponentTuz(int index, boolean tuzFlag) {
        Player opponent = getCurrentOpponent();
        return opponent.setTuz(index, tuzFlag);
    }

    /**
     * Set a hole on the player's side to be a tuz.
     * If the player already has a tuz, the previous tuz is removed and the new hole
     * is promoted to be tuz.
     *
     * @param playerId The player ID
     * @param holeIndex The index of the hole
     */
    public void setTuz(int playerId, int holeIndex) {
        Player player = players.get(playerId);
        if (player == null) return;
        if (player.hasTuz()) {
            int oldTuzIndex = player.getTuzIndex();
            Hole oldTuz = player.getHole(oldTuzIndex);
            oldTuz.setTuz(false);
        }
        player.getHole(holeIndex).setTuz(true);
    }

}
