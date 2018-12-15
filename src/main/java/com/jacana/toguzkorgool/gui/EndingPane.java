package com.jacana.toguzkorgool.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;

/**
 * This class represents a panel that displays when a player has won.
 */
public class EndingPane extends JPanel {

    private JButton btnRestart;
    private JButton btnQuit;
    private JLabel lblMessage;

    /**
     * Create a new EndingPane
     */
    public EndingPane() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.initializeComponents();
    }

    /**
     * Initialise the components and populate the content pane.
     */
    private void initializeComponents() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        btnRestart = new JButton("New Game");
        btnRestart.setName("ButtonNewGame");
        btnRestart.setHorizontalAlignment(JButton.CENTER);
        btnQuit = new JButton("Quit");
        btnQuit.setName("ButtonQuit");
        btnQuit.setHorizontalAlignment(JButton.CENTER);
        lblMessage = new JLabel();
        lblMessage.setHorizontalAlignment(JLabel.CENTER);
        lblMessage.setFont(new Font("Serif", Font.PLAIN, 32));

        buttonsPanel.add(btnRestart);
        buttonsPanel.add(btnQuit);

        this.add(Box.createVerticalGlue());

        this.add(lblMessage);
        this.add(Box.createRigidArea(new Dimension(230, 10)));

        this.add(buttonsPanel);
        this.add(Box.createVerticalGlue());
    }

    public JButton getQuitButton() {
        return btnQuit;
    }

    public JButton getRestartButton() {
        return btnRestart;
    }

    /**
     * Set the message displayed to the win message.
     */
    public void setWin() {
        lblMessage.setText("Congratulations! You win!");
    }

    /**
     * Set the message displayed to the lose message.
     */
    public void setLose() {
        lblMessage.setText("Game Over");
    }

}
