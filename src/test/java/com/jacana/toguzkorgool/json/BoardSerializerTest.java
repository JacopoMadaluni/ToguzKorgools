package com.jacana.toguzkorgool.json;

import com.google.gson.JsonObject;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardSerializerTest {

    private Board board = null;
    private BoardSerializer boardSerializer = null;

    @Before
    public void setUp() {
        this.board = new Board();
        this.boardSerializer = new BoardSerializer();
    }

    @After
    public void tearDown() {
        this.boardSerializer = null;
        this.board = null;
    }

    /**
     * Ensure serializing a simple (newly constructed) board results in a JsonObject containing the correct fields.
     */
    @Test
    public void testSimpleBoard() {
        JsonObject serializedBoard = (JsonObject) this.boardSerializer.serialize(this.board, JsonObject.class, null);
        for (Player player : this.board.getPlayers()) {
            assertTrue(serializedBoard.has(String.valueOf(player.getId())));
            JsonObject jsonPlayer = serializedBoard.getAsJsonObject(String.valueOf(player.getId()));
            assertTrue(jsonPlayer.has("holes"));
            assertTrue(jsonPlayer.has("kazan"));
            JsonObject jsonHoles = jsonPlayer.getAsJsonObject("holes");
            for (int holeId = 1; holeId <= player.getNumberOfHoles(); holeId++) {
                assertTrue(jsonHoles.has(String.valueOf(holeId)));
                JsonObject jsonHole = jsonHoles.getAsJsonObject(String.valueOf(holeId));
                assertTrue(jsonHole.has("korgools"));
            }
        }
    }

    /**
     * Ensure serializing a simple board results in a JsonObject containing the correct data for the number of korgools in a hole.
     */
    @Test
    public void testSimpleBoardHoles() {
        JsonObject serializedBoard = (JsonObject) this.boardSerializer.serialize(this.board, JsonObject.class, null);
        for (Player player : this.board.getPlayers()) {
            JsonObject jsonPlayer = serializedBoard.getAsJsonObject(String.valueOf(player.getId()));
            JsonObject jsonHoles = jsonPlayer.getAsJsonObject("holes");
            for (int holeId = 1; holeId <= player.getNumberOfHoles(); holeId++) {
                JsonObject jsonHole = jsonHoles.getAsJsonObject(String.valueOf(holeId));
                assertEquals(9, jsonHole.getAsJsonPrimitive("korgools").getAsInt());
            }
        }
    }

    /**
     * Ensure serializing a simple board results in a JsonObject not containing the "tuz" field in any hole.
     */
    @Test
    public void testSimpleBoardWithoutTuz() {
        JsonObject serializedBoard = (JsonObject) this.boardSerializer.serialize(this.board, JsonObject.class, null);
        for (Player player : this.board.getPlayers()) {
            JsonObject jsonPlayer = serializedBoard.getAsJsonObject(String.valueOf(player.getId()));
            JsonObject jsonHoles = jsonPlayer.getAsJsonObject("holes");
            for (int holeId = 1; holeId <= player.getNumberOfHoles(); holeId++) {
                JsonObject jsonHole = jsonHoles.getAsJsonObject(String.valueOf(holeId));
                assertFalse(jsonHole.has("tuz"));
            }
        }
    }

    /**
     * Ensure serializing a simple board with a hole marked as a tuz results in a JsonObject containing the "tuz" field in that hole.
     */
    @Test
    public void testSimpleBoardWithTuz() {
        this.board.setTuz(0, 0);
        JsonObject serializedBoard = (JsonObject) this.boardSerializer.serialize(this.board, JsonObject.class, null);
        for (Player player : this.board.getPlayers()) {
            JsonObject jsonPlayer = serializedBoard.getAsJsonObject(String.valueOf(player.getId()));
            JsonObject jsonHoles = jsonPlayer.getAsJsonObject("holes");
            for (int holeId = 1; holeId <= player.getNumberOfHoles(); holeId++) {
                JsonObject jsonHole = jsonHoles.getAsJsonObject(String.valueOf(holeId));
                if (player.getId() == 0 && holeId == 1) {
                    assertTrue(jsonHole.has("tuz"));
                }
            }
        }
    }

    /**
     * Ensure serializing a simple board results in a JsonObject containing the correct data for the number of korgools in a kazan.
     */
    @Test
    public void testSimpleBoardKazan() {
        JsonObject serializedBoard = (JsonObject) this.boardSerializer.serialize(this.board, JsonObject.class, null);
        for (Player player : this.board.getPlayers()) {
            JsonObject jsonPlayer = serializedBoard.getAsJsonObject(String.valueOf(player.getId()));
            assertEquals(0, jsonPlayer.getAsJsonPrimitive("kazan").getAsInt());
        }
    }

    /**
     * Ensure serializing a board with a custom number of korgools in the kazan and each hole results in a JsonObject containing the correct data.
     */
    @Test
    public void testComplexBoard() {
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            this.board.getPlayer(0).clearHole(holeIndex);
            this.board.getPlayer(0).addToHole(holeIndex, holeIndex + 1);
        }
        this.board.getPlayer(0).addToKazan(10);

        JsonObject serializedBoard = (JsonObject) this.boardSerializer.serialize(this.board, JsonObject.class, null);

        JsonObject jsonPlayer = serializedBoard.getAsJsonObject("0");
        JsonObject jsonHoles = jsonPlayer.getAsJsonObject("holes");
        for (int holeId = 1; holeId <= 9; holeId++) {
            JsonObject jsonHole = jsonHoles.getAsJsonObject(String.valueOf(holeId));
            assertEquals(holeId, jsonHole.getAsJsonPrimitive("korgools").getAsInt());
        }
        assertEquals(10, jsonPlayer.getAsJsonPrimitive("kazan").getAsInt());
    }

}
