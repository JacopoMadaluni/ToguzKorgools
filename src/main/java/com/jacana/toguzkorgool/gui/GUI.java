package com.jacana.toguzkorgool.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

/**
 * GUI is the main class which brings all other GUI components together and
 * constructs the main JFrame.
 */
public class GUI extends JFrame {

    private GamePane gamePane;
    
    public GUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Toguz Korgool");
        setResizable(true);
        setPreferredSize(new Dimension(800, 400));
        setMinimumSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        populatePane();
    }
    
    /**
     * Encapsulation method for populating the main frame with its components.
     */
    private void populatePane() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setJMenuBar(constructMenuBar());
        
        this.gamePane = new GamePane();
        contentPane.add(this.gamePane, BorderLayout.CENTER);
    }
    
    /**
     * Constructs the overarching menu bar for he GUI and returns it.
     * @return the JMenuBar object constructed
     */
    private JMenuBar constructMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu restartMenu, customMenu, exitMenu;
        restartMenu = new JMenu("Restart");
        restartMenu.setMnemonic(KeyEvent.VK_R);
        restartMenu.getAccessibleContext().setAccessibleDescription(
                "Restart the game");
        
        customMenu = new JMenu("Custom");
        customMenu.setMnemonic(KeyEvent.VK_C);
        customMenu.getAccessibleContext().setAccessibleDescription(
                "Load a custom game");
        
        exitMenu = new JMenu("Exit");
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Exit the game");
        
        menuBar.add(restartMenu);
        menuBar.add(customMenu);
        menuBar.add(exitMenu);
        
        return menuBar;
    }

    public GamePane getGamePane() {
        return this.gamePane;
    }

}
