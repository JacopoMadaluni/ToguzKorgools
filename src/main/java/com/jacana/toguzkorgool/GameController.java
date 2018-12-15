package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.CustomGameDialog;
import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class functions as "middle man" between back-end and front-end (board and GUI)
 */
public class GameController {

    private static GameController instance = null;
    private static GUI gui = null;
    private static Board board = null;

    private GameController() {
        board = new Board();
        gui = new GUI();

        initialiseGUI();
        gui.setVisible(true);
    }

    /**
     * @return The game controller singleton.
     */
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    /**
     * Destroys the current game controller.
     */
    public static void destroyInstance() {
        CustomGameDialog.destroyInstance();

        if (gui != null) {
            gui.dispose();
            gui = null;
        }

        board = null;
        instance = null;
    }

    /**
     * Initialize the main gui components.
     */
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

    /**
     * Initialize the ending pane action listeners.
     */
    private void initializeEnding() {
        gui.getEndPane().getRestartButton().addActionListener(e -> restartGame());
        gui.getEndPane().getQuitButton().addActionListener(e -> destroyInstance());
    }

    /**
     * Initialize the menu action listeners.
     */
    private void initialiseMenuItems() {
        gui.getRestartMenuItem().addActionListener(e -> restartGame());
    }

    /**
     * Initialize the holes and links them to back-end.
     */
    private void initialiseHoles() {
        for (int j = 0; j < board.getCurrentPlayer().getNumberOfHoles(); j++) {
            int finalJ = j;
            final JHole currentJHole = gui.getGamePane().getHoles(board.getCurrentPlayer().getId()).get(j);
            currentJHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (board.getKorgoolsInHole(board.getCurrentPlayer().getId(), finalJ) > 0) {
                        board.getCurrentPlayer().makeMove(finalJ);
                        if (board.hasCurrentPlayerWon()) {
                            gui.getGamePane().updateGamePane(board.getPlayerCount() - 1);
                            onWin(board.getCurrentPlayer().getId());
                            return;
                        }
                    } else {
                        for (int holeIndex = 0; holeIndex < Constants.CONSTRAINT_HOLES_PER_PLAYER; holeIndex++) {
                            if (board.getKorgoolsInHole(board.getCurrentPlayer().getId(), holeIndex) > 0) {
                                return;
                            }
                        }
                    }
                    board.changePlayer();
                    if (board.getCurrentPlayer() instanceof BotPlayer) {
                        ((BotPlayer) board.getCurrentPlayer()).act();
                        if (board.hasCurrentPlayerWon()) {
                            onWin(board.getCurrentPlayer().getId());
                            return;
                        }
                        board.changePlayer();
                        gui.getGamePane().updateGamePane(board.getPlayerCount() - 1);
                    }
                }
            });
        }
    }

    /**
     * Initialize players' kazans.
     */
    private void initialiseKazans() {
        gui.getGamePane().initialiseKazan(board.getCurrentPlayer());
        gui.getGamePane().initialiseKazan(board.getCurrentOpponent());
    }

    /**
     * Initialize the color for each side of the board.
     */
    private void initialiseColours() {
        gui.getGamePane().initialiseColour(0, Color.lightGray);
        gui.getGamePane().initialiseColour(1, Color.darkGray);
    }

    /**
     * This method get's called when one of the player's wins.
     *
     * @param playerId The id of the current player.
     */
    public void onWin(int playerId) {
        if (playerId == 0) {
            gui.loadVictoryScreen();
        } else if (playerId == 1) {
            gui.loadDefeatScreen();
        }
    }

    /**
     * Starts a brand new game.
     */
    public void restartGame() {
        board.resetBoard();
        gui.getGamePane().updateGamePane(board.getPlayerCount() - 1);
        gui.restart();
    }

    /**
     * @return The game board.
     */
    public static Board getBoard() {
        return board;
    }

    /**
     * @return The game GUI.
     */
    public static GUI getGUI() {
        return gui;
    }

    /**
     * Update GUI components.
     */
    public static void updateGUI() {
        gui.update(board.getPlayerCount() - 1);
    }

}
