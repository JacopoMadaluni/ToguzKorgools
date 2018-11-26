package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {

    private GUI gui; //front-end
    private Board board; //back-end

    public GameController() {
        board = new Board();
        gui = new GUI();

        initialiseGUI();
        gui.setVisible(true);
    }

    private void initialiseGUI() {
        gui.getGamePane().initialisePanel(true, board.getPlayer());
        gui.getGamePane().initialisePanel(false, board.getOpponent());

        this.initialiseMenuItems();
        this.initialiseHoles();
    }

    private void initialiseMenuItems() {
        gui.getRestartMenuItem().addActionListener(e -> restartGame());
        gui.getExitMenuItem().addActionListener(e -> EventQueue.invokeLater(() -> gui.dispose()));
    }

    private void initialiseHoles() {
        for (int j = 0; j < board.getPlayer().getHoleCount(); j++) {
            int finalJ = j;
            final JHole currentJHole = gui.getGamePane().getPlayerHoles().get(j);
            currentJHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    board.getPlayer().makeMove(finalJ + 1);
                    gui.getGamePane().updateHoles(true);
                    board.changePlayer();
                    if (board.getPlayer() instanceof BotPlayer) {
                        ((BotPlayer) board.getPlayer()).act();
                        gui.getGamePane().updateHoles(false);
                        board.changePlayer();
                    }
                    // TODO: add updateKazans();
                }
            });
        }
    }

    public Board getBoard() {
        return board;
    }

    public GUI getGUI() {
        return gui;
    }

    public void restartGame() {
        board.resetBoard();
        gui.getGamePane().updateHoles(true);
        gui.getGamePane().updateHoles(false);
        // TODO: update kazan front end
    }

}
