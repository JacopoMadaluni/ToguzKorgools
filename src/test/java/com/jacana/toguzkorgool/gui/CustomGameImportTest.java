package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.Constants;
import com.jacana.toguzkorgool.GameController;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import java.awt.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomGameImportTest extends AbstractCustomGameTest {

    @Test
    public void testImportDialog() {
        this.swinger.clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:importMenuItem")
                .pause(750);
        assertNotNull(CustomGameDialog.getInstance().getImportFileChooser());
        Component fileDialog = getFileDialog(CustomGameDialog.getInstance().getImportFileChooser());
        assertNotNull(fileDialog);
        this.swinger.setRoot(fileDialog);
        this.swinger.clickOn("text:Cancel").pause(250);
    }

    @Test
    public void testImportBasic() {
        Board basicBoard = new Board();
        basicBoard.setKorgoolsInHole(0, 0, 12);
        basicBoard.setKorgoolsInHole(1, 0, 8);
        CustomGameDialog.getInstance().loadUser(basicBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(basicBoard.getPlayer(1));
        assertEquals(12, ((JSpinner) getComponent("Player0Hole0")).getValue());
        assertEquals(8, ((JSpinner) getComponent("Player1Hole0")).getValue());
    }

    @Test
    public void testImportComplex() {
        Board complexBoard = new Board();
        complexBoard.setKorgoolsInHole(0, 0, 0);
        complexBoard.setKorgoolsInHole(1, 0, 8);
        complexBoard.setTuz(0, 1);
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(1));
        assertEquals(2, ((JComboBox) getComponent("Player0Tuz")).getSelectedIndex());
        assertEquals(0, ((JComboBox) getComponent("Player1Tuz")).getSelectedIndex());
    }

}