package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Constants;
import com.jacana.toguzkorgool.Player;
import org.junit.Test;

import java.awt.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomGameExportTest extends AbstractCustomGameTest {

    @Test
    public void testExportDialog() {
        this.swinger.clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:exportMenuItem")
                .pause(750);
        assertNotNull(CustomGameDialog.getInstance().getExportFileChooser());
        Component fileDialog = getFileDialog(CustomGameDialog.getInstance().getExportFileChooser());
        assertNotNull(fileDialog);
        this.swinger.setRoot(fileDialog);
        this.swinger.clickOn("text:Cancel").pause(250);
    }

    @Test
    public void testExportBasic() {
        Board customBoard = new Board();
        for (Player player : customBoard.getPlayers()) {
            CustomGameDialog.getInstance().saveUser(player);
        }
        for (Player player : customBoard.getPlayers()) {
            for (int i = 0; i < player.getNumberOfHoles(); i++) {
                assertEquals(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE, player.getKorgoolsInHole(i));
            }
            assertEquals(0, player.getKorgoolsInKazan());
            assertEquals(-1, player.getTuzIndex());
        }
    }

    @Test
    public void testExportCustomHoles() {
        this.swinger.doubleClickOn("name:Player0Hole1").pause(250).type(String.valueOf(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE + 1)).pause(250);
        this.swinger.doubleClickOn("name:Player0Hole2").pause(250).type(String.valueOf(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE - 1)).pause(250);
        this.swinger.doubleClickOn("name:Player1Hole3").pause(250).type(String.valueOf(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE + 1)).pause(250);
        this.swinger.doubleClickOn("name:Player1Hole4").pause(250).type(String.valueOf(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE - 1)).pause(250);
        this.swinger.clickOn("name:Player0Tuz").pause(250);

        Board customBoard = new Board();
        for (Player player : customBoard.getPlayers()) {
            CustomGameDialog.getInstance().saveUser(player);
        }

        assertEquals(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE, customBoard.getPlayer(0).getKorgoolsInHole(0));
        assertEquals(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE + 1, customBoard.getPlayer(0).getKorgoolsInHole(1));
        assertEquals(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE - 1, customBoard.getPlayer(0).getKorgoolsInHole(2));
        assertEquals(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE + 1, customBoard.getPlayer(1).getKorgoolsInHole(3));
        assertEquals(Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE - 1, customBoard.getPlayer(1).getKorgoolsInHole(4));
    }

    @Test
    public void testExportCustomTuz() {
        this.swinger.clickOn("name:Player0Tuz").pause(250).clickOn("text:Hole 1").pause(250);
        this.swinger.clickOn("name:Player1Tuz").pause(250).clickOn("text:Hole 2").pause(250);

        Board customBoard = new Board();
        for (Player player : customBoard.getPlayers()) {
            CustomGameDialog.getInstance().saveUser(player);
        }

        assertEquals(0, customBoard.getTuzIndex(0));
        assertEquals(1, customBoard.getTuzIndex(1));
    }

    @Test
    public void testExportCustomKazan() {
        this.swinger.doubleClickOn("name:Player0Kazan").pause(250).type("2").pause(250);
        this.swinger.clickOn("name:Player0Tuz").pause(250);

        Board customBoard = new Board();
        for (Player player : customBoard.getPlayers()) {
            CustomGameDialog.getInstance().saveUser(player);
        }

        assertEquals(2, customBoard.getKorgoolsInKazan(0));
        assertEquals(0, customBoard.getKorgoolsInKazan(1));
    }

    @Test
    public void testExportComplex() {
        int[][] playerKorgools = new int[Constants.CONSTRAINT_PLAYER_COUNT][Constants.CONSTRAINT_HOLES_PER_PLAYER];
        for (int playerId = 0; playerId < playerKorgools.length; playerId++) {
            for (int holeIndex = 0; holeIndex < playerKorgools[playerId].length; holeIndex++) {
                int korgools = Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE;
                if (playerId == 0) {
                    korgools = holeIndex * 2;
                } else if (playerId == 1) {
                    korgools = holeIndex * 2 + 1;
                }
                playerKorgools[playerId][holeIndex] = korgools;
                this.swinger.doubleClickOn("name:Player" + playerId + "Hole" + holeIndex).pause(250).type(String.valueOf(korgools)).pause(250);
            }
        }
        this.swinger.clickOn("name:Player0Tuz").pause(250);

        Board customBoard = new Board();
        for (Player player : customBoard.getPlayers()) {
            CustomGameDialog.getInstance().saveUser(player);
        }

        for (int playerId = 0; playerId < playerKorgools.length; playerId++) {
            for (int holeIndex = 0; holeIndex < playerKorgools[playerId].length; holeIndex++) {
                assertEquals(playerKorgools[playerId][holeIndex], customBoard.getKorgoolsInHole(playerId, holeIndex));
            }
        }
    }

}