package com.jacana.toguzkorgool;

/**
 * The application class that creates the GameController instance.
 */
public class ToguzKorgool {

    public static void main(String[] args) {
        GameController.getInstance();
    }

    /**
     * Terminate the application, closing any open GUI.
     */
    public static void exitApplication() {
        GameController.destroyInstance();
    }

}