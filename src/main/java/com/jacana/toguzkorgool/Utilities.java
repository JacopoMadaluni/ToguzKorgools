package com.jacana.toguzkorgool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jacana.toguzkorgool.json.BoardDeserializer;
import com.jacana.toguzkorgool.json.BoardSerializer;

import java.util.OptionalInt;

public class Utilities {

    private static Gson gson = null;

    private Utilities() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Board.class, new BoardDeserializer())
                    .registerTypeAdapter(Board.class, new BoardSerializer())
                    .create();
        }
        return gson;
    }

    public static OptionalInt tryParseInt(String strNumber) {
        if (strNumber == null) return OptionalInt.empty();
        try {
            return OptionalInt.of(Integer.parseInt(strNumber));
        } catch (NumberFormatException ignored) {
        }
        return OptionalInt.empty();
    }

}
