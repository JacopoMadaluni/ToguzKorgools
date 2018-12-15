package com.jacana.toguzkorgool.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Constants;
import com.jacana.toguzkorgool.Hole;
import com.jacana.toguzkorgool.Player;
import com.jacana.toguzkorgool.Utilities;

import java.lang.reflect.Type;
import java.util.OptionalInt;

/**
 * A class that handles the deserialization of a JsonObject (a serialized board) into a Board.
 */
public class BoardDeserializer implements JsonDeserializer<Board> {

    @Override
    public Board deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject serializedBoard = jsonElement.getAsJsonObject();
        Board board = new Board();
        for (String strPlayerId : serializedBoard.keySet()) {
            OptionalInt playerId = Utilities.tryParseInt(strPlayerId);
            if (playerId.isPresent()) {
                JsonElement jsonPlayer = serializedBoard.get(strPlayerId);
                if (jsonPlayer.isJsonObject()) {
                    Player player = board.getPlayer(playerId.getAsInt());
                    if (player != null) {
                        updateUser(jsonPlayer.getAsJsonObject(), player);
                    }
                }
            }
        }
        return board;
    }

    private static void updateUser(JsonObject jsonUser, final Player player) {
        JsonObject jsonUserHoles = jsonUser.getAsJsonObject("holes");
        int tuzId = -1;
        for (int i = 0; i < player.getNumberOfHoles(); i++) {
            String strHoleId = String.valueOf(i + 1);
            if (jsonUserHoles.has(strHoleId)) {
                JsonElement jsonHole = jsonUserHoles.get(strHoleId);
                if (jsonHole.isJsonObject()) {
                    final Hole hole = player.getHole(i);
                    updateHole(jsonHole.getAsJsonObject(), hole, i + 1, tuzId);
                    if (hole.isTuz()) tuzId = i;
                }
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
        if (korgoolCount < 0 || korgoolCount > Constants.CONSTRAINT_TOTAL_KORGOOLS) {
            throw new IllegalArgumentException("Number of korgools is too large (" + korgoolCount + ")");
        }
        hole.setKorgools(korgoolCount);
        if (jsonHole.has("tuz")) {
            if (holeId == 9) throw new IllegalArgumentException("Hole 9 can't be a tuz!");
            if (tuzId != -1) throw new IllegalArgumentException("More than one tuz has been found! Tuz ID: " + tuzId);
            hole.setTuz(jsonHole.get("tuz").getAsBoolean());
        } else {
            hole.setTuz(false);
        }
    }

}
