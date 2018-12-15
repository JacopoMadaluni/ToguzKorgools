package com.jacana.toguzkorgool.gui;

import com.jacana.toguzkorgool.Player;
import com.jacana.toguzkorgool.gui.components.JHole;
import com.jacana.toguzkorgool.gui.components.JKazan;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GamePane is the GUI representation of a Toguz Korgool game board.
 * It contains 4 distinct fields: a Kazan and a Hole field for each of the
 * two players on the board.
 */
public class GamePane extends JPanel {

    private Map<String, Component> componentMap = new HashMap<>();

    private Map<Integer, Color> playerColours = new HashMap<>();
    private Map<Integer, JPanel> playersPanel = new HashMap<>();
    private Map<Integer, JKazan> playersKazan = new HashMap<>();
    private Map<Integer, List<JHole>> playersHoles = new HashMap<>();

    /**
     * Create a game panel.
     */
    public GamePane() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setBackground(Color.black);

        this.populatePane();
    }

    /**
     * Populate the content pane
     */
    private void populatePane() {
        playersHoles.clear();
        componentMap.clear();

        JPanel kazanPanel = new JPanel();
        kazanPanel.setLayout(new BoxLayout(kazanPanel, BoxLayout.X_AXIS));
        kazanPanel.setAlignmentX(CENTER_ALIGNMENT);

        final JKazan player1Kazan = createKazanPanel();
        final JKazan player2Kazan = createKazanPanel();
        playersKazan.put(0, player1Kazan);
        playersKazan.put(1, player2Kazan);

        kazanPanel.add(player2Kazan);
        kazanPanel.add(player1Kazan);

        JPanel player2Panel = createPlayerPanel();
        JPanel player1Panel = createPlayerPanel();
        playersPanel.put(0, player1Panel);
        playersPanel.put(1, player2Panel);

        add(player2Panel);
        add(kazanPanel);
        add(player1Panel);
    }

    /**
     * Add a component to the map of component names to components.
     *
     * @param component The component
     */
    private void addToComponentMap(Component component) {
        this.componentMap.put(component.getName(), component);
    }

    /**
     * Create a default kazan panel.
     *
     * @return The created kazan panel
     */
    private JKazan createKazanPanel() {
        return new JKazan(null);
    }

    /**
     * Create a default player panel
     *
     * @return The created player panel
     */
    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
        playerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        playerPanel.setAlignmentX(CENTER_ALIGNMENT);
        return playerPanel;
    }

    /**
     * Get a GUI component by its name.
     *
     * @param name The name of the component
     * @return The component with the inputted name. Returns null if the component does not exist.
     */
    public Component getComponentByName(String name) {
        return componentMap.get(name);
    }

    /**
     * @param playerId The player ID
     * @return A list of hole panels for a player
     */
    public List<JHole> getHoles(int playerId) {
        return playersHoles.get(playerId);
    }

    /**
     * A construction method for initializing the players side of the board,
     * for example setting up the holes.
     *
     * @param player the reference to the player object.
     */
    public void initialisePanel(Player player) {
        List<JHole> holes = playersHoles.get(player.getId());
        if (holes == null) holes = new ArrayList<>();
        JPanel panel = playersPanel.get(player.getId());
        if (panel == null) return;
        panel.setBackground(playerColours.getOrDefault(player.getId(), panel.getBackground()));

        holes.clear();
        for (int i = 0; i < player.getNumberOfHoles(); ++i) {
            int k = player.getId() == 0 ? i : player.getNumberOfHoles() - 1 - i;
            JHole holePanel = new JHole(player.getHole(k));
            holePanel.setName("Player" + player.getId() + "Hole" + i);
            holePanel.setBackground(playerColours.getOrDefault(player.getId(), holePanel.getBackground()));
            addToComponentMap(holePanel);

            panel.add(holePanel);
            if (i != player.getNumberOfHoles() - 1) {
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
            }
            holes.add(holePanel);
        }
        playersHoles.put(player.getId(), holes);
    }

    /**
     * Initialise the kazan, linking the front-end kazan panel to the back-end kazan.
     *
     * @param player The player
     */
    public void initialiseKazan(Player player) {
        JKazan kazan = playersKazan.get(player.getId());
        if (kazan == null) return;
        kazan.setName("Player" + player.getId() + "Kazan");
        kazan.setKazan(player.getKazan());
        kazan.setBackground(playerColours.getOrDefault(player.getId(), kazan.getBackground()));

        addToComponentMap(kazan);
    }

    /**
     * Set the colour that represents a player and update the hole panels' backgrounds.
     *
     * @param playerId The player ID
     * @param colour The colour
     */
    public void initialiseColour(int playerId, Color colour) {
        this.playerColours.put(playerId, colour);

        JPanel panel = playersPanel.get(playerId);
        if (panel != null) panel.setBackground(colour);
        List<JHole> holes = playersHoles.get(playerId);
        if (holes != null) {
            for (JHole hole : holes) hole.setBackground(colour);
        }
    }

    /**
     * Update all hole panels belonging to the player, reflecting the correct number of korgools in each hole.
     * <p>
     * Refresh the UI to display these changes.
     * @param playerId The player
     */
    public void updateHoles(int playerId) {
        List<JHole> holePanels = playersHoles.get(playerId);
        if (holePanels == null) return;
        for (JHole holePanel : holePanels) {
            holePanel.updateHole();
        }
        playersPanel.get(playerId).updateUI();
    }

    /**
     * Update all the kazan panel belonging to the player, reflecting the correct number of korgools in the kazan.
     * <p>
     * Refresh the UI of the panel to display these changes.
     * @param playerId The player
     */
    public void updateKazan(int playerId) {
        JKazan kazan = playersKazan.get(playerId);
        if (kazan == null) return;
        kazan.updateKazan();
    }

    /**
     * Update all players hole panels and kazan panels.
     * <p>
     * Refresh the UI of the GUI to display these changes.
     */
    public void updateGamePane() {
        for (int playerId : this.playersPanel.keySet()) {
            updateHoles(playerId);
            updateKazan(playerId);
        }
    }

}
