package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.GuiItemNotFound;
import com.athaydes.automaton.Swinger;
import com.jacana.toguzkorgool.GameController;
import com.jacana.toguzkorgool.gui.components.JHole;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GUITest {

    private static final long CLICK_PAUSE = 250L;

    private Swinger swinger = null;

    @Before
    public void setUp() {
        GameController.getInstance();
        Swinger.forSwingWindow().pause(CLICK_PAUSE);

        Swinger.setDEFAULT(com.athaydes.automaton.Speed.VERY_FAST);
        this.swinger = Swinger.getUserWith(GameController.getInstance().getGUI());
    }

    @After
    public void tearDown() {
        this.swinger = null;
        GameController.destroyInstance();
    }

    @Test
    public void testWin() {
        GameController.getInstance().onWin(0);
        this.clickOnComponent("ButtonQuit");
    }

    @Test
    public void testLose() {
        GameController.getInstance().onWin(1);
        this.clickOnComponent("ButtonQuit");
    }

    @Test(expected = GuiItemNotFound.class)
    public void testWinUnknownPlayer() {
        GameController.getInstance().onWin(-1);
        this.clickOnComponent("ButtonQuit");
    }

    @Test
    public void testHoleInteraction() {
        String holeComponentName = "Player0Hole0";
        this.clickOnComponent(holeComponentName).pause(CLICK_PAUSE);
        String holeTooltip = ((JHole) getComponent(holeComponentName)).getToolTipText();
        assertThat(holeTooltip, is(equalTo("Korgools: " + 2)));
    }

    @Test
    public void testRestart() {
        String holeComponentName = "Player0Hole0";
        this.clickOnComponent(holeComponentName).pause(CLICK_PAUSE);

        this.clickOnComponent("fileMenu").pause(CLICK_PAUSE);
        this.clickOnComponent("restartMenuItem").pause(CLICK_PAUSE);

        String holeTooltip = ((JHole) getComponent(holeComponentName)).getToolTipText();
        assertThat(holeTooltip, is(equalTo("Korgools: " + 9)));
    }

    @Test
    public void testNewGameRestart() {
        GameController.getInstance().onWin(0);
        this.clickOnComponent("ButtonNewGame").pause(CLICK_PAUSE);
    }

    private Swinger clickOnComponent(String component) {
        this.swinger.clickOn("name:" + component);
        return this.swinger;
    }

    private Swinger clickOnText(String text) {
        this.swinger.clickOn("text:" + text);
        return this.swinger;
    }

    private Swinger pause() {
        this.swinger.pause(CLICK_PAUSE);
        return this.swinger;
    }

    private Component getComponent(String name) {
        return GameController.getInstance().getGUI().getGamePane().getComponentByName(name);
    }

}
