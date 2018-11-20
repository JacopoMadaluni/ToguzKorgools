package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;

public class GameController {

    private GUI gui; //front-end
    private Board board; //back-end

    public GameController() {
        this.board = new Board();
        this.gui = new GUI();

        this.initialiseGUI();
        this.gui.setVisible(true);
    }

    private void initialiseGUI() {
        this.gui.getGamePane().initialisePanel(true, this.board.getCurrentPlayer());
        this.gui.getGamePane().initialisePanel(false, this.board.getNextPlayer());

        this.initialiseHoles();
    }

    private void initialiseHoles() {
        for (int j = 0; j < this.board.getCurrentPlayer().getHoleCount(); j++) {
            int finalJ = j;
            final JHole currentJHole = this.gui.getGamePane().getPlayerHoles().get(j);
           currentJHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    board.getCurrentPlayer().makeMove(finalJ + 1);
                    board.getNextPlayer().makeMove(new Random().nextInt(9)+1);
                    gui.getGamePane().updateHoles(true);
                    gui.getGamePane().updateHoles(false);
                    //TODO: add updateKazans();
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
