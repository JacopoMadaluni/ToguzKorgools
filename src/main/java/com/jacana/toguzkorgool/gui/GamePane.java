package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Player;
import com.jacana.toguzkorgool.gui.components.JHole;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class GamePane extends JPanel {

    private JPanel botPanel = null;
    private JPanel playerPanel = null;

    private List<JHole> botHoles = new ArrayList<>();
    private List<JHole> playerHoles = new ArrayList<>();

    public GamePane() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.populatePane();
    }

    private void populatePane() {
        botHoles.clear();
        playerHoles.clear();

        JPanel kazanPanel = new JPanel();
        kazanPanel.setLayout(new BoxLayout(kazanPanel, BoxLayout.X_AXIS));
        kazanPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        kazanPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel botKazanPanel = createKazanPanel();
        JPanel userKazanPanel = createKazanPanel();

        kazanPanel.add(botKazanPanel);
        kazanPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        kazanPanel.add(userKazanPanel);

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

    private JPanel createKazanPanel() {
        JPanel kazanPanel = new JPanel();
        kazanPanel.setLayout(new GridLayout(3, 27));
        kazanPanel.setBorder(new LineBorder(Color.black, 1, true));
        return kazanPanel;
    }

    public List<JHole> getBotHoles() {
        return botHoles;
    }

    public List<JHole> getPlayerHoles() {
        return playerHoles;
    }

    public void initialisePanel(boolean isPlayer, Player player) {
        List<JHole> holes = isPlayer ? playerHoles : botHoles;
        JPanel panel = isPlayer ? playerPanel : botPanel;
        if (holes.isEmpty()) {
            for (int i = 0; i < player.getHoleCount(); ++i) {
                JHole holePanel = new JHole(player.getHole(i));
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
    }

}
