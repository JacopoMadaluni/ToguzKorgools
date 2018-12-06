package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {
    
    private static GameController instance;
    private static GUI gui; //front-end
    private static Board board; //back-end

    private GameController() {
        board = new Board();
        gui = new GUI();

        initialiseGUI();
        gui.setVisible(true);
    }
    
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private void initialiseGUI() {
        gui.getGamePane().initialisePanel(true, board.getCurrentPlayer());
        gui.getGamePane().initialisePanel(false, board.getCurrentOpponent());

        this.initialiseMenuItems();
        this.initialiseHoles();
        this.initialiseKazans();
        this.initializeEnding();
    }

    private void initializeEnding(){
        gui.getEnding().getRestartButton().addActionListener(e -> restartGame());
        gui.getEnding().getQuitButton().addActionListener(e -> EventQueue.invokeLater(() -> gui.dispose()));
    }

    private void initialiseMenuItems() {
        gui.getRestartMenuItem().addActionListener(e -> restartGame());
        gui.getExitMenuItem().addActionListener(e -> EventQueue.invokeLater(() -> gui.dispose()));
    }

    private void initialiseHoles() {
        for (int j = 0; j < board.getCurrentPlayer().getHoleCount(); j++) {
            int finalJ = j;
            final JHole currentJHole = gui.getGamePane().getPlayerHoles().get(j);
            currentJHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    board.getCurrentPlayer().makeMove(finalJ + 1);
                    gui.getGamePane().updateHoles(true);
                    gui.getGamePane().updateKazan(true);
                    if (board.currentPlayerHasWon()){
                        gui.loadVictoryScreen();
                        return;
                    }
                    board.changePlayer();
                    if (board.getCurrentPlayer() instanceof BotPlayer) {
                        ((BotPlayer) board.getCurrentPlayer()).act();
                        gui.getGamePane().updateHoles(false);
                        gui.getGamePane().updateKazan(false);
                        if (board.currentPlayerHasWon()){
                            gui.loadDefeatScreen();
                        }
                        board.changePlayer();
                    }
                }
            });
        }
    }

    private void initialiseKazans() {
        gui.getGamePane().initialiseKazan(board.getCurrentPlayer());
        gui.getGamePane().initialiseKazan(board.getCurrentOpponent());
    }

    public static Board getBoard() {
        return board;
    }

    public GUI getGUI() {
        return gui;
    }
    
    public static void updateGUI() {
        gui.update();
    }

    public void restartGame() {
        board.resetBoard();
        gui.getGamePane().updateHoles(true);
        gui.getGamePane().updateHoles(false);
        gui.getGamePane().updateKazan(true);
        gui.getGamePane().updateKazan(false);
        gui.restart();
    }

}
