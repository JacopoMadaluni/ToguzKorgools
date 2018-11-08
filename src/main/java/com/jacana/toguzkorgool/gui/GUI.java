package com.jacana.toguzkorgool.gui;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;

public class GUI extends JFrame {

  private Container contentPane;

  public GUI() {
    setTitle("Toguz Korgool");
    setResizable(true);
    setPreferredSize(new Dimension(400, 400));
    setMinimumSize(new Dimension(300, 300));
    contentPane = getContentPane();
    pack();
    setVisible(true);
  }

}
