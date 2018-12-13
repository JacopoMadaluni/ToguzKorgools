package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.Swinger;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Constants;
import com.jacana.toguzkorgool.GameController;
import org.junit.Test;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import java.awt.Component;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CustomGameImportTest {

    private static Field fieldDialog = null;

    static {
        try {
            fieldDialog = JFileChooser.class.getDeclaredField("dialog");
            fieldDialog.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private Swinger swinger = null;

    private void setUpGUI() {
        GameController.getInstance();
        Swinger.forSwingWindow().pause(250);

        Swinger.setDEFAULT(com.athaydes.automaton.Speed.VERY_FAST);
        this.swinger = Swinger.getUserWith(GameController.getGUI());
        this.swinger.clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:customGameMenuItem")
                .pause(250);
        this.swinger.setRoot(CustomGameDialog.getInstance());
    }

    private void tearDownGUI() {
        this.swinger = null;
        GameController.destroyInstance();
    }

    @Test
    public void testValidateBoardValid() {
        Board validBoard = new Board();
        assertNull(CustomGameDialog.validateBoard(validBoard));
    }

    @Test
    public void testValidateBoardInvalidHole() {
        Board invalidBoard = new Board();
        invalidBoard.setKorgoolsInHole(0, 0, -1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
        invalidBoard.setKorgoolsInHole(0, 0, Constants.CONSTRAINT_TOTAL_KORGOOLS + 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardMultipleTuz() {
        Board invalidBoard = new Board();
        invalidBoard.getPlayer(0).getHole(0).setTuz(true);
        invalidBoard.getPlayer(0).getHole(1).setTuz(true);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardTuzHole9() {
        Board invalidBoard = new Board();
        invalidBoard.setTuz(0, 8);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardSameTuz() {
        Board invalidBoard = new Board();
        invalidBoard.setTuz(0, 1);
        invalidBoard.setTuz(1, 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testValidateBoardInvalidKazan() {
        Board invalidBoard = new Board();
        invalidBoard.setKorgoolsInKazan(0, -1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
        invalidBoard.setKorgoolsInKazan(0, Constants.CONSTRAINT_TOTAL_KORGOOLS + 1);
        assertNotNull(CustomGameDialog.validateBoard(invalidBoard));
    }

    @Test
    public void testImportDialog() {
        this.setUpGUI();
        this.swinger.clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:importMenuItem")
                .pause(750);
        assertNotNull(CustomGameDialog.getInstance().getImportFileChooser());
        Component fileDialog = getFileDialog(CustomGameDialog.getInstance().getImportFileChooser());
        assertNotNull(fileDialog);
        this.swinger.setRoot(fileDialog);
        this.swinger.clickOn("text:Cancel").pause(250);
        this.tearDownGUI();
    }

    @Test
    public void testImportBasic() {
        this.setUpGUI();
        Board basicBoard = new Board();
        basicBoard.setKorgoolsInHole(0, 0, 12);
        basicBoard.setKorgoolsInHole(1, 0, 8);
        CustomGameDialog.getInstance().loadUser(basicBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(basicBoard.getPlayer(1));
        assertEquals(12, ((JSpinner) getComponent("Player0Hole0")).getValue());
        assertEquals(8, ((JSpinner) getComponent("Player1Hole0")).getValue());
        this.tearDownGUI();
    }

    @Test
    public void testImportComplex() {
        this.setUpGUI();
        Board complexBoard = new Board();
        complexBoard.setKorgoolsInHole(0, 0, 0);
        complexBoard.setKorgoolsInHole(1, 0, 8);
        complexBoard.setTuz(0, 1);
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(1));
        assertEquals(2, ((JComboBox) getComponent("Player0Tuz")).getSelectedIndex());
        assertEquals(0, ((JComboBox) getComponent("Player1Tuz")).getSelectedIndex());
        this.tearDownGUI();
    }

    private static Component getFileDialog(JFileChooser jFileChooser) {
        if (fieldDialog == null) return null;
        try {
            return (Component) fieldDialog.get(jFileChooser);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Component getComponent(String name) {
        return CustomGameDialog.getInstance().getComponentMap().get(name);
    }

}