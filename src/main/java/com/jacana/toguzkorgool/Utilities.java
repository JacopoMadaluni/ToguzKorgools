package com.jacana.toguzkorgool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jacana.toguzkorgool.json.BoardDeserializer;
import com.jacana.toguzkorgool.json.BoardSerializer;

import java.util.OptionalInt;

/**
 * A utility class containing static methods.
 */
public class Utilities {

    private static Gson gson = null;

    private Utilities() {
    }

    /**
     * @return The Gson instance
     */
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

    /**
     * Try to parse an integer represented as a String.
     *
     * @param strNumber The integer as a String
     * @return An OptionalInt representing the parsed number. <p>If the string is an invalid integer (e.g. non-numeric), OptionalInt.empty() is returned.
     */
    public static OptionalInt tryParseInt(String strNumber) {
        if (strNumber == null) return OptionalInt.empty();
        try {
            return OptionalInt.of(Integer.parseInt(strNumber));
        } catch (NumberFormatException ignored) {
        }
        return OptionalInt.empty();
    }

}