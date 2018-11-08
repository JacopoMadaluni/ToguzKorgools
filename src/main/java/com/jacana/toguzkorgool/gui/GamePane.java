package com.jacana.toguzkorgool.gui;

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

  private final GUI gui;

  private JPanel botPanel = null;
  private JPanel playerPanel = null;

  private List<JHole> botHoles = new ArrayList<>();
  private List<JHole> playerHoles = new ArrayList<>();

  public GamePane(GUI gui) {
    super();
    this.gui = gui;

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(new EmptyBorder(5, 5, 5, 5));

    this.populatePane();
  }

  private void populatePane() {
    this.botHoles.clear();
    this.playerHoles.clear();

    JPanel kazanPanel = new JPanel();
    kazanPanel.setLayout(new BoxLayout(kazanPanel, BoxLayout.X_AXIS));
    kazanPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    kazanPanel.setAlignmentX(CENTER_ALIGNMENT);

    JPanel botKazanPanel = this.createKazanPanel();
    JPanel userKazanPanel = this.createKazanPanel();

    kazanPanel.add(botKazanPanel);
    kazanPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    kazanPanel.add(userKazanPanel);

    this.botPanel = new JPanel();
    this.botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));
    this.botPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.botPanel.setAlignmentX(CENTER_ALIGNMENT);
    this.initialiseBotPanel();

    this.playerPanel = new JPanel();
    this.playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
    this.playerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.playerPanel.setAlignmentX(CENTER_ALIGNMENT);
    this.initialisePlayerPanel();

    this.add(botPanel);
    this.add(kazanPanel);
    this.add(playerPanel);
  }

  private JPanel createKazanPanel() {
    JPanel kazanPanel = new JPanel();
    kazanPanel.setLayout(new GridLayout(3, 27));
    kazanPanel.setBorder(new LineBorder(Color.black, 1, true));

    return kazanPanel;
  }

  private void initialiseBotPanel() {
    if (this.botHoles.isEmpty()) {
      for (int i = 0; i < 9; ++i) {
        JHole holePanel = new JHole();
        botPanel.add(holePanel);
        if (i != 8) {
          botPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        }
        this.botHoles.add(holePanel);
      }
    }
  }

  private void initialisePlayerPanel() {
    if (this.playerHoles.isEmpty()) {
      for (int i = 0; i < 9; ++i) {
        JHole holePanel = new JHole();
        playerPanel.add(holePanel);
        if (i != 8) {
          playerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        }
        this.playerHoles.add(holePanel);
      }
    }
  }

  private void updateHoles(boolean isPlayer) {
    List<JHole> holePanels = null;
    if (isPlayer) holePanels = this.playerHoles;
    else holePanels = this.botHoles;
    for (JHole holePanel : holePanels) {
      holePanel.updateHole();
    }
  }

}
