package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.Swinger;
import com.jacana.toguzkorgool.Board;
import com.jacana.toguzkorgool.GameController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;


public class CustomGameDialogTest {
    
    private static final boolean DEBUG = false;
    private Swinger swinger;
    private final Runnable applyAction = this::clickApply;
    private final Runnable cancelAction = this::clickCancel;
    
    
    @Before
    public void setup() {
        //open the application
        GameController.getInstance();
        //open the custom game dialog
        Swinger.setDEFAULT(com.athaydes.automaton.Speed.VERY_FAST);
        Swinger.forSwingWindow().pause(250)
                .clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:customGameMenuItem")
                .pause(250);
        //set the custom game dialog to be the subject
        Swinger.setDEFAULT(com.athaydes.automaton.Speed.VERY_FAST);
        swinger = Swinger.getUserWith(CustomGameDialog.getCustomGameDialogInstance());
    }
    
    @After
    public void tareDown() throws InterruptedException {
        GameController.destroyInstance();
        TimeUnit.SECONDS.sleep(1);
    }
    
    private void performTest(int[][] params, Runnable controlAction, Runnable assertion) {
        inputParams(params);
        
        controlAction.run();
        
        //print error messages if any
        if (DEBUG) {
            if (CustomGameDialog.areErrorsPresent()) {
                for (String errMsg : CustomGameDialog.getErrors())
                    System.out.println(errMsg);
            }
        }
        
        assertion.run();
        
        //check that the back-end is intact.
        checkBackend(params);
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
    
    private Component getComponentFromMap(Map<String, Component> components, String name) {
        return components.get(name);
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
    
    @Test
    public void guiShouldStartWithDefaultValues() {
        //default params
        int[][] params = {{9, 9, 9, 9, 9, 9, 9, 9, 9, 0, -1},
                {9, 9, 9, 9, 9, 9, 9, 9, 9, 0, -1}};
        
        Map<String, Component> components = CustomGameDialog.getCustomGameDialogInstance().getComponentMap();
        
        for (int j = 0; j < params.length; j++) {
            int[] sideParam = params[j];
            String playerId = "Player" + j;
            for (int i = 0; i < sideParam.length - 2; i++) {
                //get spinner hole value
                int frontendHole = (int) ((JSpinner) getComponentFromMap(components, playerId + "Hole" + i)).getValue();
                assertThat(frontendHole, is(equalTo(sideParam[i])));
            }
            //get spinner kazan Value
            int frontendKazan = (int) ((JSpinner) getComponentFromMap(components, playerId + "Kazan")).getValue();
            assertThat(frontendKazan, is(equalTo(sideParam[sideParam.length - 2])));
            //get spinner tuz Value
            int frontendTuz = ((JComboBox<String>) getComponentFromMap(components, playerId + "Tuz")).getSelectedIndex() - 1;
            assertThat(frontendTuz, is(equalTo(sideParam[sideParam.length - 1])));
        }
    }
    
    @Test
    public void testValidInput() {
        //all valid input
        int[][] params = {{12, 12, 4, 13, 1, 2, 12, 3, 13, 14, -1},  //player 1 prams
                {13, 2, 11, 11, 2, 12, 0, 12, 1, 12, -1}}; //player 2 prams
        performTest(params, applyAction, () -> assertFalse(CustomGameDialog.areErrorsPresent()));
    }
}
