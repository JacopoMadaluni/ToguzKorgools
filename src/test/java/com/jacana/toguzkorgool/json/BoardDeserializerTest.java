package com.jacana.toguzkorgool.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jacana.toguzkorgool.Board;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardDeserializerTest {

    private BoardDeserializer boardDeserializer = null;

    @Before
    public void setUp() {
        this.boardDeserializer = new BoardDeserializer();
    }

    @After
    public void tearDown() {
        this.boardDeserializer = null;
    }

    /**
     * Create a serialized board.
     *
     * @param playersHolesKorgools A mapping of each player to a mapping of each hole ID to the number of korgools
     * @param korgoolsInKazan The number of korgools in the kazan
     * @return A serialized board with the inputted data
     */
    private JsonObject createBoard(Map<Integer, Map<Integer, Integer>> playersHolesKorgools, int korgoolsInKazan) {
        JsonObject jsonBoard = new JsonObject();
        for (int playerId = 0; playerId < 2; playerId++) {
            jsonBoard.add(String.valueOf(playerId), this.createDefaultPlayer(playersHolesKorgools.getOrDefault(playerId, new HashMap<>()), korgoolsInKazan));
        }
        return jsonBoard;
    }

    /**
     * Create a serialized player.
     *
     * @param korgoolsInHoles A mapping of hole ID to the number of korgools in that hole
     * @param korgoolsInKazan The number of korgools in the kazan
     * @return A serialized player with the inputted data
     */
    private JsonObject createDefaultPlayer(Map<Integer, Integer> korgoolsInHoles, int korgoolsInKazan) {
        JsonObject jsonHoles = new JsonObject();
        for (int holeId = 1; holeId <= 9; holeId++) {
            jsonHoles.add(String.valueOf(holeId), this.createDefaultHole(korgoolsInHoles.getOrDefault(holeId, 9)));
        }

        JsonObject jsonPlayer = new JsonObject();
        jsonPlayer.add("holes", jsonHoles);
        jsonPlayer.addProperty("kazan", korgoolsInKazan);
        return jsonPlayer;
    }

    /**
     * Create a serialized hole.
     *
     * @param korgoolsInHole The number of korgools in the hole
     * @return A serialized hole with the inputted data
     */
    private JsonObject createDefaultHole(int korgoolsInHole) {
        JsonObject jsonHole = new JsonObject();
        jsonHole.addProperty("korgools", korgoolsInHole);
        return jsonHole;
    }

    /**
     * Ensure deserializing a serialized simple (newly constructed) board results in the correct number of korgools in the kazan and each hole.
     */
    @Test
    public void testSimpleBoard() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), 0);

        Board resultingBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            assertEquals(9, resultingBoard.getPlayer(0).getKorgoolsInHole(holeIndex));
        }
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            assertEquals(9, resultingBoard.getPlayer(1).getKorgoolsInHole(holeIndex));
        }
        assertFalse(resultingBoard.getPlayer(0).hasTuz());
        assertFalse(resultingBoard.getPlayer(1).hasTuz());
    }

    /**
     * Ensure deserializing a serialized simple board with a hole marked as a tuz results in a Board with a Hole marked as a tuz.
     */
    @Test
    public void testTuz() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), 0);
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").getAsJsonObject("2").addProperty("tuz", true);

        Board resultingBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            assertEquals(9, resultingBoard.getPlayer(0).getKorgoolsInHole(holeIndex));
        }
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            assertEquals(9, resultingBoard.getPlayer(1).getKorgoolsInHole(holeIndex));
        }
        assertTrue(resultingBoard.getPlayer(0).hasTuz());
        assertEquals(2 - 1, resultingBoard.getTuzIndex(0));
        assertFalse(resultingBoard.getPlayer(1).hasTuz());
    }

    /**
     * Ensure deserializing a serialized simple board with 2 holes marked as a tuz results in an {@link IllegalArgumentException} being thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultipleTuz() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), 0);
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").getAsJsonObject("2").addProperty("tuz", true);
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").getAsJsonObject("3").addProperty("tuz", true);

        this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
    }

    /**
     * Ensure deserializing a serialized simple board with hole 9 marked as a tuz results in an {@link IllegalArgumentException} being thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHole9Tuz() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), 0);
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").getAsJsonObject("9").addProperty("tuz", true);

        this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
    }

    /**
     * Ensure deserializing a serialized complex board with a custom number of korgools in each hole results in a Board with the correct data.
     */
    @Test
    public void testComplexBoard() {
        Map<Integer, Map<Integer, Integer>> holesKorgools = new HashMap<Integer, Map<Integer, Integer>>() {{
            this.put(0, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) {
                    this.put(i, i);
                }
            }});
            this.put(1, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) {
                    this.put(i, 9 - (i - 1));
                }
            }});
        }};
        JsonObject serializedBoard = this.createBoard(holesKorgools, 0);

        Board resultingBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            assertEquals((long) holesKorgools.get(0).get(holeIndex + 1), (long) resultingBoard.getPlayer(0).getKorgoolsInHole(holeIndex));
        }
        for (int holeIndex = 0; holeIndex < 9; holeIndex++) {
            assertEquals((long) holesKorgools.get(1).get(holeIndex + 1), (long) resultingBoard.getPlayer(1).getKorgoolsInHole(holeIndex));
        }
        assertFalse(resultingBoard.getPlayer(0).hasTuz());
        assertFalse(resultingBoard.getPlayer(1).hasTuz());
    }

    /**
     * Ensure deserializing a serialized board with too many korgools in a hole results in an {@link IllegalArgumentException} being thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTooManyKorgools() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), -1);

        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").getAsJsonObject("1").addProperty("korgools", 163);
        this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
    }

    /**
     * Ensure deserializing a serialized board with a negative number of korgools in a hole results in an {@link IllegalArgumentException} being thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeKorgools() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), -1);

        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").getAsJsonObject("1").addProperty("korgools", -1);
        this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
    }

    /**
     * Ensure deserializing a serialized board with a negative number of korgools in a kazan results in an {@link IllegalArgumentException} being thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeKazan() {
        JsonObject serializedBoard = this.createBoard(new HashMap<>(), -1);

        this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
    }

    /**
     * Ensure deserializing a serialized board without a hole results in the default number of korgools in that hole.
     */
    @Test
    public void testMissingHole() {
        JsonObject serializedBoard = this.createBoard(new HashMap<Integer, Map<Integer, Integer>>() {{
            this.put(0, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
            this.put(1, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
        }}, 0);
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").remove("3");

        Board deserializedBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        assertEquals(9, deserializedBoard.getKorgoolsInHole(0, 2));
    }

    /**
     * Ensure deserializing a serialized board with a hole incorrectly serialized results in the default number of korgools in that hole.
     */
    @Test
    public void testCorruptHole() {
        JsonObject serializedBoard = this.createBoard(new HashMap<Integer, Map<Integer, Integer>>() {{
            this.put(0, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
            this.put(1, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
        }}, 0);
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").remove("3");
        serializedBoard.getAsJsonObject("0").getAsJsonObject("holes").add("3", new JsonArray());

        Board deserializedBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        assertEquals(9, deserializedBoard.getKorgoolsInHole(0, 2));
    }

    /**
     * Ensure deserializing a serialized board with custom input and an unknown player ID results in a board with the correct data with no errors.
     */
    @Test
    public void testUnknownPlayerID() {
        JsonObject serializedBoard = this.createBoard(new HashMap<Integer, Map<Integer, Integer>>() {{
            this.put(0, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
            this.put(1, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
        }}, 0);
        serializedBoard.remove("0");
        serializedBoard.add("-1", new JsonObject());

        Board deserializedBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        assertEquals(10, deserializedBoard.getKorgoolsInHole(1, 0));
        assertEquals(9, deserializedBoard.getKorgoolsInHole(0, 0));
    }

    /**
     * Ensure deserializing a serialized board with custom input and an invalid player ID results in a board with the correct data with no errors.
     */
    @Test
    public void testInvalidPlayerID() {
        JsonObject serializedBoard = this.createBoard(new HashMap<Integer, Map<Integer, Integer>>() {{
            this.put(0, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
            this.put(1, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
        }}, 0);
        serializedBoard.remove("0");
        serializedBoard.add("Player0", new JsonObject());

        Board deserializedBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        assertEquals(10, deserializedBoard.getKorgoolsInHole(1, 0));
        assertEquals(9, deserializedBoard.getKorgoolsInHole(0, 0));
    }

    /**
     * Ensure deserializing a serialized board with a player incorrectly serialized results in the default data for that player.
     */
    @Test
    public void testCorruptPlayer() {
        JsonObject serializedBoard = this.createBoard(new HashMap<Integer, Map<Integer, Integer>>() {{
            this.put(0, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
            this.put(1, new HashMap<Integer, Integer>() {{
                for (int i = 1; i <= 9; i++) this.put(i, 10);
            }});
        }}, 0);
        serializedBoard.add("0", new JsonArray());

        Board deserializedBoard = this.boardDeserializer.deserialize(serializedBoard, Board.class, null);
        assertEquals(10, deserializedBoard.getKorgoolsInHole(1, 0));
        assertEquals(9, deserializedBoard.getKorgoolsInHole(0, 0));
    }

}
