package com.jacana.toguzkorgool.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class GUI extends JFrame {

  public GUI() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    setTitle("Toguz Korgool");
    setResizable(true);
    setPreferredSize(new Dimension(640, 380));
    setMinimumSize(new Dimension(400, 200));
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    populatePane();

    setVisible(true);
  }

  private void populatePane() {
    JPanel contentPane = (JPanel) getContentPane();
    contentPane.setLayout(new BorderLayout(5, 5));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.add(new GamePane(this), BorderLayout.CENTER);
  }

}
