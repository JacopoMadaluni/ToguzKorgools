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

    @Test
    public void testImportComplex() {
        Board complexBoard = new Board();
        complexBoard.setKorgoolsInHole(0, 0, 0);
        complexBoard.setKorgoolsInHole(1, 0, 8);
        complexBoard.setTuz(0, 1);
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(0));
        CustomGameDialog.getInstance().loadUser(complexBoard.getPlayer(1));
        assertThat(((JComboBox) getComponent("Player0Tuz")).getSelectedIndex(), is(equalTo(2)));
        assertThat(((JComboBox) getComponent("Player1Tuz")).getSelectedIndex(), is(equalTo(0)));
    }

}