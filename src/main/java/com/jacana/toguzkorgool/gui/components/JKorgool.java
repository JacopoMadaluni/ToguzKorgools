package com.jacana.toguzkorgool.gui.components;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;

public class JKorgool extends JComponent {

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.black);
    g.fillOval(0, 0, this.getWidth(), this.getHeight());
  }

}
