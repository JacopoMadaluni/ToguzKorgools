package com.jacana.toguzkorgool;

import com.jacana.toguzkorgool.gui.CustomGameDialog;
import com.jacana.toguzkorgool.gui.GUI;
import com.jacana.toguzkorgool.gui.components.JHole;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * This class functions as "middle man" between back-end and front-end (board and GUI)
 */
public class GameController {

    private static GameController instance = null;
    private static GUI gui = null;
    private static Board board = null;

    private Thread botThread = null;

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

        if (instance != null) {
            if (instance.getBotThread() != null) {
                instance.getBotThread().interrupt();
            }
        }

        if (gui != null) {
            gui.dispose();
            gui = null;
        }

        board = null;
        instance = null;
    }

    /**
     * Initialize the main GUI components.
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
        gui.getCustomGameMenuItem().addActionListener(e -> {
            if (botThread == null) CustomGameDialog.showCustomGameDialog();
        });
        gui.getRestartMenuItem().addActionListener(e -> restartGame());
    }

    /**
     * Initialize the holes and link them to the back-end.
     */
    private void initialiseHoles() {
        for (int j = 0; j < board.getCurrentPlayer().getNumberOfHoles(); j++) {
            int finalJ = j;
            final JHole currentJHole = gui.getGamePane().getHoles(board.getCurrentPlayer().getId()).get(j);
            currentJHole.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (botThread != null) return;
                    if (board.getKorgoolsInHole(board.getCurrentPlayer().getId(), finalJ) > 0) {
                        board.getCurrentPlayer().makeMove(finalJ);
                        if (board.hasCurrentPlayerWon()) {
                            gui.getGamePane().updateGamePane();
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
                        gui.getGamePane().updateGamePane();
                        botThread = new Thread(() -> {
                            try {
                                Thread.sleep(500L + (new Random().nextInt(500) + 1));
                            } catch (InterruptedException ignored) {
                                botThread = null;
                                return;
                            }
                            EventQueue.invokeLater(() -> {
                                botThread = null;
                                ((BotPlayer) board.getCurrentPlayer()).act();
                                if (board.hasCurrentPlayerWon()) {
                                    onWin(board.getCurrentPlayer().getId());
                                    return;
                                }
                                board.changePlayer();
                                gui.getGamePane().updateGamePane();
                            });
                        });
                        botThread.start();
                    }
                }
            });
        }
    }

    /**
     * Initialize each players' kazan.
     */
    private void initialiseKazans() {
        gui.getGamePane().initialiseKazan(board.getCurrentPlayer());
        gui.getGamePane().initialiseKazan(board.getCurrentOpponent());
    }

    /**
     * Initialize the colour for each side of the board.
     */
    private void initialiseColours() {
        gui.getGamePane().initialiseColour(0, Color.lightGray);
        gui.getGamePane().initialiseColour(1, Color.darkGray);
    }

    public Thread getBotThread() {
        return this.botThread;
    }

    /**
     * Display the victory/defeat screen to the player depending on the winner.
     *
     * @param playerId The player ID of the winner.
     */
    public void onWin(int playerId) {
        if (playerId == 0) {
            gui.loadVictoryScreen();
        } else if (playerId == 1) {
            gui.loadDefeatScreen();
        }
    }

    /**
     * Starts a new game, resetting the state of the board and GUI.
     */
    public void restartGame() {
        if (this.botThread != null) {
            this.botThread.interrupt();
            this.botThread = null;
        }

        board.resetBoard();
        gui.getGamePane().updateGamePane();
        gui.restart();
    }

    public static Board getBoard() {
        return board;
    }

    public static GUI getGUI() {
        return gui;
    }

    /**
     * Update/refresh the GUI components.
     */
    public static void updateGUI() {
        gui.update();
    }

}
