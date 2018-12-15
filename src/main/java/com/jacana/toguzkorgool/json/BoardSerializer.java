package com.jacana.toguzkorgool.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Hole;
import com.jacana.toguzkorgool.Player;

import java.lang.reflect.Type;

/**
 * A class that handles the serialization of a Board into a JsonObject.
 */
public class BoardSerializer implements JsonSerializer<Board> {

    @Override
    public JsonElement serialize(final Board board, final Type type, final JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonBoard = new JsonObject();
        for (Player player : board.getPlayers()) {
            jsonBoard.add(String.valueOf(player.getId()), serializeUser(player));
        }
        return jsonBoard;
    }

    private static JsonObject serializeUser(final Player player) {
        JsonObject jsonUserHoles = new JsonObject();
        for (int i = 0; i < player.getNumberOfHoles(); i++) {
            jsonUserHoles.add(String.valueOf(i + 1), serializeHole(player.getHole(i)));
        }
        JsonObject jsonUser = new JsonObject();
        jsonUser.add("holes", jsonUserHoles);
        jsonUser.addProperty("kazan", player.getKorgoolsInKazan());
        return jsonUser;
    }

    private static JsonObject serializeHole(Hole hole) {
        JsonObject jsonHole = new JsonObject();
        jsonHole.addProperty("korgools", hole.getKorgools());
        if (hole.isTuz()) jsonHole.addProperty("tuz", true);
        return jsonHole;
    }

}
