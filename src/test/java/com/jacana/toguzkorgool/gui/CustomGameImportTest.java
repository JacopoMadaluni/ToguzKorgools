package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Board;
import org.junit.Test;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import java.awt.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CustomGameImportTest extends AbstractCustomGameTest {

    /**
     * Ensure the import dialog shows up when the menu item is interacted with.
     */
    @Test
    public void testImportDialog() {
        this.swinger.clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:importMenuItem")
                .pause(750);
        assertThat(CustomGameDialog.getInstance().getImportFileChooser(), is(notNullValue()));
        Component fileDialog = getFileDialog(CustomGameDialog.getInstance().getImportFileChooser());
        assertThat(fileDialog, is(notNullValue()));
        this.swinger.setRoot(fileDialog);
        this.swinger.clickOn("text:Cancel").pause(250);
    }

    /**
     * Ensure importing a basic board results in the custom game GUI updating to reflect the imported data.
     */
    @Test
    public void testImportBasic() {
        Board basicBoard = new Board();
        basicBoard.setKorgoolsInHole(0, 0, 12);
        basicBoard.setKorgoolsInHole(1, 0, 8);
        CustomGameDialog.getInstance().loadUser(basicBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(basicBoard.getPlayer(1));
        assertThat(((JSpinner) getComponent("Player0Hole0")).getValue(), is(equalTo(12)));
        assertThat(((JSpinner) getComponent("Player1Hole0")).getValue(), is(equalTo(8)));
    }

    /**
     * Ensure importing a board with a lot of custom data results in the custom game GUI updating to reflect the imported data.
     */
    @Test
    public void testImportComplex() {
        Board complexBoard = new Board();
        complexBoard.setKorgoolsInHole(0, 0, 8);
        complexBoard.setKorgoolsInHole(1, 0, 10);
        complexBoard.setKorgoolsInKazan(0, 1);
        complexBoard.setTuz(0, 1);
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(1));
        assertThat(((JComboBox) getComponent("Player0Tuz")).getSelectedIndex(), is(equalTo(2)));
        assertThat(((JComboBox) getComponent("Player1Tuz")).getSelectedIndex(), is(equalTo(0)));
        assertThat(((JSpinner) getComponent("Player0Kazan")).getValue(), is(equalTo(1)));
    }

}