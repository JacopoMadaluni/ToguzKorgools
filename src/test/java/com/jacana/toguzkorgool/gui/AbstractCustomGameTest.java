package com.jacana.toguzkorgool.gui;

import com.athaydes.automaton.Swinger;
import com.jacana.toguzkorgool.GameController;
import org.junit.After;
import org.junit.Before;

import javax.swing.JFileChooser;
import java.awt.Component;
import java.lang.reflect.Field;

public abstract class AbstractCustomGameTest {

    private static Field fieldDialog = null;

    static {
        try {
            fieldDialog = JFileChooser.class.getDeclaredField("dialog");
            fieldDialog.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    protected Swinger swinger = null;

    @Before
    public void setUp() {
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

    @After
    public void tearDown() {
        this.swinger = null;
        GameController.destroyInstance();
    }

    protected static Field getFieldDialog() {
        return fieldDialog;
    }

    protected static Component getFileDialog(JFileChooser jFileChooser) {
        if (fieldDialog == null) return null;
        try {
            return (Component) fieldDialog.get(jFileChooser);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static Component getComponent(String name) {
        return CustomGameDialog.getInstance().getComponentMap().get(name);
    }

}
