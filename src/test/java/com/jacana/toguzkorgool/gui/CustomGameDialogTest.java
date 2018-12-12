package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.Swinger;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.GameController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import java.awt.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;


public class CustomGameDialogTest {

    private Swinger swinger;

    private final Runnable applyAction = this::clickApply;
    private final Runnable cancelAction = this::clickCancel;

    @Before
    public void setUp() {
        // Open the application
        GameController.getInstance();

        // Create the swinger
        Swinger.setDEFAULT(com.athaydes.automaton.Speed.VERY_FAST);
        Swinger.forSwingWindow().pause(250);
        swinger = Swinger.getUserWith(GameController.getInstance().getGUI());

        // Open the custom game dialog
        swinger.clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:customGameMenuItem")
                .pause(250);
        // Set the custom game dialog to be the subject
        swinger.setRoot(CustomGameDialog.getCustomGameDialogInstance());
    }

    @After
    public void tearDown() {
        GameController.destroyInstance();
        swinger = null;
    }

    private void pause() {
        swinger.pause(250);
    }

    private void clickApply() {
        swinger.clickOn("name:ApplyButton");
        pause();
    }

    private void clickCancel() {
        swinger.clickOn("name:CancelButton");
        pause();
    }

    private void typeValueTo(String componentName, String valueToType) {
        swinger.doubleClickOn(componentName);
        pause();
        swinger.type(valueToType);
        pause();
    }

    private void inputParams(int[][] params) {
        for (int j = 0; j < params.length; j++) {
            int[] sideParam = params[j];
            String playerId = "Player" + j;
            for (int i = 0; i < sideParam.length - 2; i++) {
                typeValueTo(playerId + "Hole" + i, String.valueOf(sideParam[i]));
            }
            typeValueTo(playerId + "Kazan", String.valueOf(sideParam[sideParam.length - 2]));

            swinger.clickOn(playerId + "Tuz");
            pause();
            int tuzIndex = sideParam[sideParam.length - 1];
            if (tuzIndex != -1)
                swinger.clickOn("text:Hole " + tuzIndex);
            else
                swinger.clickOn("text:None");
            pause();
        }
    }

    private void checkBackend(int[][] params) {
        Board board = GameController.getBoard();

        for (int j = 0; j < params.length; j++) {
            int[] sideParam = params[j];
            for (int i = 0; i < sideParam.length - 2; i++) {
                int boardValue = board.getHoleKorgoolCount(j, i);
                assertThat(boardValue, is(equalTo(sideParam[i])));
            }
            assertThat(board.getKazanCount(j), is(equalTo(sideParam[sideParam.length - 2])));
            assertThat(board.getTuzIndex(j), is(equalTo(sideParam[sideParam.length - 1])));
        }
    }

    private void performTest(int[][] params, Runnable controlAction, Runnable assertion) {
        inputParams(params);

        controlAction.run();
        assertion.run();

        // Check that the back-end is intact.
        checkBackend(params);
    }

    /**
     * Test that ensures the custom game GUI starts with the default values.
     */
    @Test
    public void testDefaultValues() {
        // Default parameters
        int[][] params = {{9, 9, 9, 9, 9, 9, 9, 9, 9, 0, -1},
                {9, 9, 9, 9, 9, 9, 9, 9, 9, 0, -1}};

        for (int j = 0; j < params.length; j++) {
            int[] sideParam = params[j];
            String playerId = "Player" + j;
            for (int i = 0; i < sideParam.length - 2; i++) {
                // Get spinner hole value
                int frontendHole = (int) ((JSpinner) getComponent(playerId + "Hole" + i)).getValue();
                assertThat(frontendHole, is(equalTo(sideParam[i])));
            }
            // Get spinner kazan value
            int guiKazanKorgools = (int) ((JSpinner) getComponent(playerId + "Kazan")).getValue();
            assertThat(guiKazanKorgools, is(equalTo(sideParam[sideParam.length - 2])));
            // Get spinner tuz value
            int guiTuzIndex = ((JComboBox<String>) getComponent(playerId + "Tuz")).getSelectedIndex() - 1;
            assertThat(guiTuzIndex, is(equalTo(sideParam[sideParam.length - 1])));
        }
    }

    /**
     * Test a set of valid inputs that the user could possibly select in the custom game GUI.
     */
    @Test
    public void testValidInput() {
        int[][] params = {{12, 12, 4, 13, 1, 2, 12, 3, 13, 14, -1},  // Player 1 parameters
                {13, 2, 11, 11, 2, 12, 0, 12, 1, 12, -1}}; // Player 2 parameters
        performTest(params, applyAction, () -> assertFalse(CustomGameDialog.areErrorsPresent()));
    }

    private static Component getComponent(String name) {
        return CustomGameDialog.getCustomGameDialogInstance().getComponentMap().get(name);
    }

}
