package com.jacana.toguzkorgool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jacana.toguzkorgool.json.BoardDeserializer;
import com.jacana.toguzkorgool.json.BoardSerializer;

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

}
