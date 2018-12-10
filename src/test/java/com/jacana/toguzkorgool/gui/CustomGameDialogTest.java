package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.GameController;
import org.junit.Before;
import org.junit.Test;
import com.athaydes.automaton.Swinger;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class CustomGameDialogTest {
    
    private Swinger swinger;

    
    @Before
    public void setup() {
        //open the application
        GameController.getInstance();
        //open the custom game dialog
        Swinger.forSwingWindow().pause(250)
                .clickOn("name:fileMenu")
                .pause(250)
                .clickOn("name:customGameMenuItem")
                .pause(250);
        //set the custom game dialog to be the subject
        swinger = Swinger.getUserWith(CustomGameDialog.getCustomGameDialogInstance());
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
    private void clickOut() {
        swinger.clickOn("CustomGameDialog");
    }
    
    
    //IMPORTANT Test gives inconsistent results (maybe cos of JSpinners)
    @Test
    public void testNormalUse() {
        //increase a hole by 1 and decrease another by 1 on both sides
        typeValueTo("Player0Hole0", "10");
        typeValueTo("Player0Hole1", "8");
        typeValueTo("Player1Hole0", "10");
        typeValueTo("Player1Hole1", "8");
        
        clickOut();
        //click "Apply"
        clickApply();
        
        //check that errors are not present
        if (CustomGameDialog.areErrorsPresent()) {
            for (String errMsg : CustomGameDialog.getErrors()) System.out.println(errMsg);
        }
        assertThat(CustomGameDialog.areErrorsPresent(), is(equalTo(false)));
        
        //check that the back-end is intact.
    }
    
    @Test
    public void testNormalUseWithTuz() {
    
    }
    
    @Test
    public void testInvalidInput() {
    
    }
}
