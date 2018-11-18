package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GameController {

    private GUI gui;
    private Board board;

    public GameController() {
        this.board = new Board();
        this.gui = new GUI();

        this.initialiseGUI();
        this.gui.setVisible(true);
    }

    private void initialiseGUI() {
        this.gui.getGamePane().initialisePanel(true, this.board.getCurrentPlayer());
        this.gui.getGamePane().initialisePanel(false, this.board.getNextPlayer());

        this.initialiseHoles(this.gui.getGamePane().getPlayerHoles());
        this.initialiseHoles(this.gui.getGamePane().getBotHoles());
    }

    private void initialiseHoles(List<JHole> jHoles) {
        for (JHole playerHole : this.gui.getGamePane().getPlayerHoles()) {
            final Hole hole = playerHole.getHole();
            playerHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO: Call makeMove on the player
                    // TODO: Call updateHoles on the player in GamePane
                    // TODO: Call makeMove on the bot
                    // TODO: Call updateHoles on the bot in GamePane
                }
            });
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public GUI getGUI() {
        return this.gui;
    }

}
