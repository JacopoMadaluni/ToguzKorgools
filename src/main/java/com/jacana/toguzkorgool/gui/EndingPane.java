package com.jacana.toguzkorgool.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EndingPane extends JPanel {

    private JButton restart;
    private JButton quit;
    private JLabel message;
    private JPanel buttons;

    public EndingPane(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
       // this.setBorder(new EmptyBorder(5, 5, 5, 5));
        initializeComponents();

    }

    private void initializeComponents(){

        restart = new JButton("New Game");
        restart.setHorizontalAlignment(JButton.CENTER);
        quit = new JButton("Quit");
        quit.setHorizontalAlignment(JButton.CENTER);
        message = new JLabel();
        message.setHorizontalAlignment(JLabel.CENTER);
        //message.setVerticalAlignment(JLabel.CENTER);
        message.setFont(new Font("Serif", Font.PLAIN, 32));
        buttons.add(restart);
        buttons.add(quit);
       // buttons.setBorder(new EmptyBorder(5,290,100,5));

        this.add(Box.createVerticalGlue());

        this.add(message);
        this.add(Box.createRigidArea(new Dimension(230, 10)));

        this.add(restart);
        this.add(quit);
        this.add(Box.createVerticalGlue());
    }

    public JButton getQuitButton() {
        return quit;
    }

    public JButton getRestartButton() {
        return restart;
    }

    public void setWin(){
        message.setText("Congratulations! You win!");
    }
    public void setLose(){
        message.setText("Game Over");
    }

}
