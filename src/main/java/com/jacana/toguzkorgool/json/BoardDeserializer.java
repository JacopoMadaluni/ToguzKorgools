package com.jacana.toguzkorgool.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Hole;
import com.jacana.toguzkorgool.HumanPlayer;
import com.jacana.toguzkorgool.Player;

import java.lang.reflect.Type;

public class BoardDeserializer implements JsonDeserializer<Board> {

    @Override
    public Board deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject serializedBoard = jsonElement.getAsJsonObject();
        Board board = new Board();
        Player currentPlayer = board.getCurrentPlayer() instanceof HumanPlayer ? board.getCurrentPlayer() : board.getCurrentOpponent();
        Player otherPlayer = board.getOpponentOf(currentPlayer);
        if (serializedBoard.has("player")) {
            updateUser(serializedBoard.getAsJsonObject("player"), currentPlayer);
        }
        if (serializedBoard.has("opponent")) {
            updateUser(serializedBoard.getAsJsonObject("opponent"), otherPlayer);
        }
        return board;
    }

    private static void updateUser(JsonObject jsonUser, final Player player) {
        JsonObject jsonUserHoles = jsonUser.getAsJsonObject("holes");
        int tuzId = -1;
        for (int i = 0; i < player.getHoleCount(); i++) {
            String strHoleId = String.valueOf(i + 1);
            if (jsonUserHoles.has(strHoleId)) {
                final Hole hole = player.getHole(i);
                updateHole(jsonUserHoles.getAsJsonObject(strHoleId), hole,  i + 1, tuzId);
                if (hole.isTuz()) tuzId = i;
            }
        }
        int kazanKorgools = jsonUser.get("kazan").getAsInt();
        if (kazanKorgools < 0) {
            throw new IllegalArgumentException("Number of korgools in the kazan (" + kazanKorgools + ") must be >= 0");
        }
        player.setKazanCount(kazanKorgools);
    }

    private static void updateHole(JsonObject jsonHole, final Hole hole, int holeId, final int tuzId) {
        int korgoolCount = jsonHole.get("korgools").getAsInt();
        if (korgoolCount < 0 || korgoolCount > 9 * 9 * 2) {
            throw new IllegalArgumentException("Number of korgools is too large (" + korgoolCount + ")");
        }
        hole.clear();
        hole.add(korgoolCount);
        if (jsonHole.has("tuz")) {
            if (holeId == 9) throw new IllegalArgumentException("Hole 9 can't be a tuz!");
            if (tuzId != -1) throw new IllegalArgumentException("More than one tuz has been found! Tuz ID: " + tuzId);
            hole.setTuz(jsonHole.get("tuz").getAsBoolean());
        } else {
            hole.setTuz(false);
        }
    }

}
