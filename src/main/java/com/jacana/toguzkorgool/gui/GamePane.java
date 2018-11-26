package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Player;
import com.jacana.toguzkorgool.gui.components.JHole;
import com.jacana.toguzkorgool.gui.components.JKazan;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * GamePane is the custom GUI representation of a Toguz Korgool game board.
 * It contains 4 distinct fields: a Kazan and a Hole field for each of the
 * two players on the board.
 */
public class GamePane extends JPanel {

    //the opposite sides of the board.
    private JPanel botPanel = null;
    private JKazan botKazan = null;
    private JPanel playerPanel = null;
    private JKazan playerKazan = null;

    //arrays to store object references to GUI Hole components for both
    //players.
    private List<JHole> botHoles = new ArrayList<>();
    private List<JHole> playerHoles = new ArrayList<>();

    public GamePane() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.populatePane();
    }

    /**
     * Function which encapsulates the creation of GamePane's components.
     */
    private void populatePane() {
        botHoles.clear();
        playerHoles.clear();

        JPanel kazanPanel = new JPanel();
        kazanPanel.setLayout(new BoxLayout(kazanPanel, BoxLayout.X_AXIS));
        kazanPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        kazanPanel.setAlignmentX(CENTER_ALIGNMENT);

        botKazan = createKazanPanel();
        playerKazan = createKazanPanel();

        kazanPanel.add(botKazan);
        kazanPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        kazanPanel.add(playerKazan);

        botPanel = new JPanel();
        botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));
        botPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        botPanel.setAlignmentX(CENTER_ALIGNMENT);

        playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
        playerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        playerPanel.setAlignmentX(CENTER_ALIGNMENT);

        add(botPanel);
        add(kazanPanel);
        add(playerPanel);
    }

    private JKazan createKazanPanel() {
        return new JKazan(null);
    }

    public List<JHole> getBotHoles() {
        return botHoles;
    }

    public JKazan getBotKazan() {
        return botKazan;
    }

    public List<JHole> getPlayerHoles() {
        return playerHoles;
    }

    public JKazan getPlayerKazan() {
        return playerKazan;
    }

    /**
     * A construction method for initializing a players Holes on the board.
     *
     * @param isPlayer Boolean which specifies whether the player is a Bot
     *                 or a Human.
     * @param player the reference to the player object.
     */
    public void initialisePanel(boolean isPlayer, Player player) {
        List<JHole> holes = isPlayer ? playerHoles : botHoles;
        JPanel panel = isPlayer ? playerPanel : botPanel;
        if (holes.isEmpty()) {
            for (int i = 0; i < player.getHoleCount(); ++i) {
                int k = isPlayer ? i : player.getHoleCount() - 1 - i;
                JHole holePanel = new JHole(player.getHole(k));
                panel.add(holePanel);
                if (i != player.getHoleCount()-1) {
                    panel.add(Box.createRigidArea(new Dimension(5, 0)));
                }
                holes.add(holePanel);
            }
        }
    }

    public void updateHoles(boolean isPlayer) {
        List<JHole> holePanels = isPlayer ? playerHoles : botHoles;
        for (JHole holePanel : holePanels) {
            holePanel.updateHole();
        }
        if (isPlayer) playerPanel.updateUI();
        else botPanel.updateUI();
    }

    public void updateKazan(boolean isPlayer) {
        if (isPlayer) playerKazan.updateKazan();
        else botKazan.updateKazan();
    }

}
