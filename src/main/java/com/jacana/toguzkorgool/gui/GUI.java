package com.jacana.toguzkorgool.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

/**
 * GUI is the main class which brings all other GUI components together and
 * constructs the main JFrame.
 */
public class GUI extends JFrame {

    private GamePane gamePane;
    private EndingPane endPane;

    /**
     * Create a new GUI, initialising the frame and populating the content pane.
     */
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
     * Reload the UI, removing the end pane and adding the game pane back to the root content pane.
     */
    public void restart() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.remove(this.endPane);
        contentPane.add(this.gamePane);
        contentPane.updateUI();
    }

    /**
     * Display the victory screen, removing the game pane.
     */
    public void loadVictoryScreen() {
        JPanel contentPane = (JPanel) getContentPane();

        endPane.setWin();
        contentPane.remove(this.gamePane);
        contentPane.add(endPane);
        contentPane.updateUI();
    }

    /**
     * Display the defeat screen, removing the game pane.
     */
    public void loadDefeatScreen() {
        JPanel contentPane = (JPanel) getContentPane();
        endPane.setLose();
        contentPane.remove(this.gamePane);
        contentPane.add(endPane);
        contentPane.updateUI();
    }

    /**
     * Update the UI to show the correct number of korgools in the kazans and each hole, and display the state of each tuz.
     */
    public void update() {
        gamePane.updateGamePane();
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.updateUI();
    }

    /**
     * Encapsulation method for populating the main frame with its components.
     */
    private void populatePane() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setJMenuBar(constructMenuBar());

        this.gamePane = new GamePane();
        this.endPane = new EndingPane();
        contentPane.add(this.gamePane, BorderLayout.CENTER);
    }

    private JMenuItem customGameMenuItem;
    private JMenuItem restartMenuItem;

    /**
     * Constructs the menu bar for the GUI.
     *
     * @return The menu bar constructed
     */
    private JMenuBar constructMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("fileMenu");

        restartMenuItem = new JMenuItem("Restart", KeyEvent.VK_R);
        restartMenuItem.setName("restartMenuItem");
        restartMenuItem.getAccessibleContext().setAccessibleDescription("Restart the game");
        fileMenu.add(restartMenuItem);

        customGameMenuItem = new JMenuItem("Custom", KeyEvent.VK_C);
        customGameMenuItem.setName("customGameMenuItem");
        customGameMenuItem.getAccessibleContext().setAccessibleDescription("Create custom game");
        fileMenu.add(customGameMenuItem);

        menuBar.add(fileMenu);

        return menuBar;
    }

    public JMenuItem getCustomGameMenuItem() {
        return customGameMenuItem;
    }

    public JMenuItem getRestartMenuItem() {
        return restartMenuItem;
    }

    public GamePane getGamePane() {
        return this.gamePane;
    }

    public EndingPane getEndPane() {
        return endPane;
    }

}
