package com.jacana.toguzkorgool.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Hole;
import com.jacana.toguzkorgool.HumanPlayer;
import com.jacana.toguzkorgool.Player;

import java.lang.reflect.Type;

public class BoardSerializer implements JsonSerializer<Board> {

    @Override
    public JsonElement serialize(final Board board, final Type type, final JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonBoard = new JsonObject();
        Player currentPlayer = board.getCurrentPlayer() instanceof HumanPlayer ? board.getCurrentPlayer() : board.getCurrentOpponent();
        Player otherPlayer = board.getOpponentOf(currentPlayer);
        jsonBoard.add("player", serializeUser(currentPlayer));
        jsonBoard.add("opponent", serializeUser(otherPlayer));
        return jsonBoard;
    }

    private static JsonObject serializeUser(final Player player) {
        JsonObject jsonUserHoles = new JsonObject();
        for (int i = 0; i < player.getHoleCount(); i++) {
            jsonUserHoles.add(String.valueOf(i + 1), serializeHole(player.getHole(i)));
        }
        JsonObject jsonUser = new JsonObject();
        jsonUser.add("holes", jsonUserHoles);
        jsonUser.addProperty("kazan", player.getKazanCount());
        return jsonUser;
    }

    private static JsonObject serializeHole(Hole hole) {
        JsonObject jsonHole = new JsonObject();
        jsonHole.addProperty("korgools", hole.getKorgools());
        if (hole.isTuz()) jsonHole.addProperty("tuz", true);
        return jsonHole;
    }

}
