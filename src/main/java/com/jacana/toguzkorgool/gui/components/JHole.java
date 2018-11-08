package com.jacana.toguzkorgool.gui.components;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;

public class JHole extends JPanel {

  public JHole() {
    super();

    this.setLayout(new GridLayout(3, 3));
    this.setBorder(new LineBorder(Color.black, 1, true));

    this.internalUpdate();
  }

  private void internalUpdate() {
    int numberOfKorgools = 9;
    GridLayout layout = (GridLayout) this.getLayout();
    layout.setRows((int) Math.max(3, Math.ceil((double) numberOfKorgools / layout.getColumns())));
    for (int j = 0; j < numberOfKorgools; ++j) {
      this.add(new JKorgool());
    }
    int maxFillers = layout.getColumns() * layout.getRows();
    for (int j = numberOfKorgools; j < maxFillers; ++j) {
      this.add(new JEmptyComponent());
    }
  }

  public void updateHole() {
    this.removeAll();
    this.internalUpdate();
    this.updateUI();
  }

}
