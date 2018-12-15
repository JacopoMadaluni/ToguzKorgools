package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.GuiItemNotFound;
import com.athaydes.automaton.Swinger;
import com.jacana.toguzkorgool.Constants;
import com.jacana.toguzkorgool.GameController;
import com.jacana.toguzkorgool.gui.components.JHole;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class GUITest {

    private Swinger swinger = null;

    @Before
    public void setUp() {
        GameController.getInstance();
        Swinger.forSwingWindow().pause(250);

        Swinger.setDEFAULT(com.athaydes.automaton.Speed.VERY_FAST);
        this.swinger = Swinger.getUserWith(GameController.getGUI());
    }

    @After
    public void tearDown() {
        this.swinger = null;
        GameController.destroyInstance();
    }

    /**
     * Ensure the ending pane shows when player 1 wins.
     */
    @Test
    public void testWin() {
        GameController.getInstance().onWin(0);
        this.swinger.clickOn("name:ButtonQuit").pause(250);
    }

    /**
     * Ensure the ending pane shows when player 1 loses.
     */
    @Test
    public void testLose() {
        GameController.getInstance().onWin(1);
        this.swinger.clickOn("name:ButtonQuit").pause(250);
    }

    /**
     * Ensure the ending pane does not show when inputting an invalid player.
     */
    @Test(expected = GuiItemNotFound.class)
    public void testWinUnknownPlayer() {
        GameController.getInstance().onWin(-1);
        this.swinger.clickOn("name:ButtonQuit").pause(250);
    }

    /**
     * Ensure interacting with a hole on the current player's side results in a change in the number of korgools in that hole.
     */
    @Test
    public void testHoleInteraction() {
        String holeComponentName = "Player0Hole0";
        this.swinger.clickOn("name:" + holeComponentName).pause(250);
        String holeTooltip = ((JHole) getComponent(holeComponentName)).getToolTipText();
        assertThat(holeTooltip, is(not(equalTo("Korgools: " + Constants.CONSTRAINT_INITIAL_KORGOOLS_PER_HOLE))));
    }

    /**
     * Ensure interacting with a hole then clicking the restart menu item resets the displayed board to its default state.
     */
    @Test
    public void testRestart() {
        String holeComponentName = "Player0Hole0";
        this.swinger.clickOn("name:" + holeComponentName).pause(250);

        this.swinger.clickOn("name:fileMenu").pause(250);
        this.swinger.clickOn("name:restartMenuItem").pause(250);

        String holeTooltip = ((JHole) getComponent(holeComponentName)).getToolTipText();
        assertThat(holeTooltip, is(equalTo("Korgools: " + 9)));
    }

    /**
     * Ensure clicking new game on the ending pane displays the game with a default board.
     */
    @Test
    public void testNewGameRestart() {
        GameController.getInstance().onWin(0);
        this.swinger.clickOn("name:ButtonNewGame").pause(250);
    }

    private Component getComponent(String name) {
        return GameController.getGUI().getGamePane().getComponentByName(name);
    }

}
