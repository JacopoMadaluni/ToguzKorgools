package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.CustomGameDialog;
import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.Color;
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

    public static void destroyInstance() {
        CustomGameDialog.destroyInstance();

        gui.dispose();
        gui = null;

        board = null;
        instance = null;
    }

    private void initialiseGUI() {
        for (Player player : board.getPlayers()) {
            gui.getGamePane().initialisePanel(player);
        }

        this.initialiseMenuItems();
        this.initialiseColours();
        this.initialiseHoles();
        this.initialiseKazans();
        this.initializeEnding();
    }

    private void initializeEnding() {
        gui.getEndPane().getRestartButton().addActionListener(e -> restartGame());
        gui.getEndPane().getQuitButton().addActionListener(e -> gui.dispose());
    }

    private void initialiseMenuItems() {
        gui.getRestartMenuItem().addActionListener(e -> restartGame());
    }

    private void initialiseHoles() {
        for (int j = 0; j < board.getCurrentPlayer().getHoleCount(); j++) {
            int finalJ = j;
            final JHole currentJHole = gui.getGamePane().getHoles(board.getCurrentPlayer().getId()).get(j);
            currentJHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    board.getCurrentPlayer().makeMove(finalJ);
                    if (board.currentPlayerHasWon()) {
                        gui.getGamePane().updateGamePane(board.getPlayerCount() - 1);
                        onWin(board.getCurrentPlayer().getId());
                        return;
                    }
                    board.changePlayer();
                    if (board.getCurrentPlayer() instanceof BotPlayer) {
                        ((BotPlayer) board.getCurrentPlayer()).act();
                        if (board.currentPlayerHasWon()) {
                            onWin(board.getCurrentPlayer().getId());
                        }
                        board.changePlayer();
                        gui.getGamePane().updateGamePane(board.getPlayerCount() - 1);
                    }
                }
            });
        }
    }

    private void initialiseKazans() {
        gui.getGamePane().initialiseKazan(board.getCurrentPlayer());
        gui.getGamePane().initialiseKazan(board.getCurrentOpponent());
    }

    private void initialiseColours() {
        gui.getGamePane().initialiseColour(0, Color.lightGray);
        gui.getGamePane().initialiseColour(1, Color.darkGray);
    }

    public GUI getGUI() {
        return gui;
    }

    public void onWin(int playerId) {
        if (playerId == 0) {
            gui.loadVictoryScreen();
        } else if (playerId == 1) {
            gui.loadDefeatScreen();
        }
    }

    public void restartGame() {
        board.resetBoard();
        gui.getGamePane().updateGamePane(board.getPlayerCount() - 1);
        gui.restart();
    }

    public static Board getBoard() {
        return board;
    }

    public static void updateGUI() {
        gui.update(board.getPlayerCount() - 1);
    }

}
