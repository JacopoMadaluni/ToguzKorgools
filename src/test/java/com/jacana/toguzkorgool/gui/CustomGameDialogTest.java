package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.GuiItemNotFound;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.GameController;
import org.junit.Test;

import javax.swing.JComboBox;
import javax.swing.JSpinner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CustomGameDialogTest extends AbstractCustomGameTest {

    private final Runnable applyAction = this::clickApply;
    private final Runnable cancelAction = this::clickCancel;

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
            if (tuzIndex != -1) swinger.clickOn("text:Hole " + tuzIndex);
            else swinger.clickOn("text:None");
            pause();
        }
    }

    private void checkBackend(int[][] params) {
        Board board = GameController.getBoard();

        for (int j = 0; j < params.length; j++) {
            int[] sideParam = params[j];
            for (int i = 0; i < sideParam.length - 2; i++) {
                int boardValue = board.getKorgoolsInHole(j, i);
                assertThat(boardValue, is(equalTo(sideParam[i])));
            }
            assertThat(board.getKorgoolsInKazan(j), is(equalTo(sideParam[sideParam.length - 2])));
            assertThat(board.getTuzIndex(j), is(equalTo(sideParam[sideParam.length - 1])));
        }
    }

    private void performTest(int[][] frontParams, int[][] backParams, Runnable controlAction, Runnable testsAction, boolean checkBackend) {
        inputParams(frontParams);

        controlAction.run();
        testsAction.run();

        // Check that the back-end is intact.
        if (checkBackend) {
            checkBackend(backParams);
        }
    }

    private void performTest(int[][] params, Runnable controlAction, Runnable testsAction, boolean checkBackend) {
        performTest(params, params, controlAction, testsAction, checkBackend);
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
        swinger.pause(250);
    }

    /**
     * Test that ensures saving a custom game with valid data does not throw errors.
     */
    @Test(expected = GuiItemNotFound.class)
    public void testValidInput() {
        int[][] params = {{12, 12, 4, 13, 1, 2, 12, 3, 13, 14, 2},  // Player 1 parameters
                {13, 2, 11, 11, 2, 12, 0, 12, 1, 12, 3}}; // Player 2 parameters
        performTest(params, applyAction, () -> swinger.clickOn("text:OK"), true);
    }

    /**
     * Test that ensures cancelling a custom game with valid data does not change back-end.
     */
    @Test(expected = GuiItemNotFound.class)
    public void testValidInputButCancel() {
        int[][] frontParams = {{12, 12, 4, 13, 1, 2, 12, 3, 13, 14, 2},  // Player 1 parameters
                {13, 2, 11, 11, 2, 12, 0, 12, 1, 12, 3}}; // Player 2 parameters

        // Default parameters
        int[][] backParams = {{9, 9, 9, 9, 9, 9, 9, 9, 9, 0, -1},
                {9, 9, 9, 9, 9, 9, 9, 9, 9, 0, -1}};
        performTest(frontParams, backParams, cancelAction, () -> swinger.clickOn("text:OK"), true);
    }

    @Test
    public void testKorgoolBoardCountShouldBeConstant() {
        // Total 163 Korgools
        int[][] params = {{13, 12, 4, 13, 1, 2, 12, 3, 13, 14, 2},  // Player 1 parameters
                {13, 2, 11, 11, 2, 12, 0, 12, 1, 12, 3}}; // Player 2 parameters

        performTest(params, applyAction, () -> swinger.clickOn("text:OK"), false);
    }

    /**
     * Test that ensures an error pops up if there is an invalid number of korgools in total.
     */
    @Test
    public void testInvalidTooManyOrTooFewKorgoolsPerSide() {
        int[][] params = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1},           // Player 1 parameters
                {18, 18, 18, 18, 18, 18, 18, 18, 18, 0, -1}}; // Player 2 parameters

        performTest(params, applyAction, () -> swinger.clickOn("text:OK"), false);
    }

    /**
     * Test that ensures an error pops up if a hole is marked as a tuz on both players sides.
     */
    @Test
    public void testBothTuzShouldNotBeInTheSameIndex() {
        int[][] params = {{12, 12, 4, 13, 1, 2, 12, 3, 13, 14, 1},  // Player 1 parameters
                {13, 2, 11, 11, 2, 12, 0, 12, 1, 12, 1}}; // Player 2 parameters
        performTest(params, applyAction, () -> swinger.clickOn("text:OK"), false);
    }

}
