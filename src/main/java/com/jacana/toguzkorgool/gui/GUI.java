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
    private EndingPane ending;

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

    public void loadVictoryScreen(){
        JPanel contentPane = (JPanel) getContentPane();

        ending.setWin();
        contentPane.remove(this.gamePane);
        contentPane.add(ending);
        contentPane.updateUI();

    }

    public void loadDefeatScreen(){
        JPanel contentPane = (JPanel) getContentPane();
        ending.setLose();
        contentPane.remove(this.gamePane);
        contentPane.add(ending);
        contentPane.updateUI();
    }

    public void restart(){
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.remove(ending);
        contentPane.add(this.gamePane);
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
        this.ending = new EndingPane();
        contentPane.add(this.gamePane, BorderLayout.CENTER);
        //ending.setLose();
        //contentPane.add(ending, BorderLayout.CENTER);
    }

    private JMenuItem restartMenuItem;
    private JMenuItem customMenuItem;
    private JMenuItem exitMenuItem;

    /**
     * Constructs the overarching menu bar for he GUI and returns it.
     *
     * @return the JMenuBar object constructed
     */
    private JMenuBar constructMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");

        restartMenuItem = new JMenuItem("Restart", KeyEvent.VK_R);
        restartMenuItem.getAccessibleContext().setAccessibleDescription("Restart the game");
        fileMenu.add(restartMenuItem);

        customMenuItem = new JMenuItem("Custom", KeyEvent.VK_C);
        restartMenuItem.getAccessibleContext().setAccessibleDescription("Create custom game");
        fileMenu.add(customMenuItem);

        // Exit menu
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.getAccessibleContext().setAccessibleDescription("Exit the game");

        menuBar.add(fileMenu);
        menuBar.add(exitMenuItem);

        return menuBar;
    }

    public JMenuItem getCustomMenuItem() {
        return customMenuItem;
    }

    public JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public GamePane getGamePane() {
        return this.gamePane;
    }

    public JMenuItem getRestartMenuItem() {
        return restartMenuItem;
    }

    public EndingPane getEnding() {
        return ending;
    }
}
