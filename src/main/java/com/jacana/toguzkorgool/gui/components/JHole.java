package com.jacana.toguzkorgool.gui.components;

import com.jacana.toguzkorgool.Hole;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;

public class JHole extends JPanel {

    private final Hole hole;

    public JHole(Hole hole) {
        super();
        this.hole = hole;

        setLayout(new GridLayout(3, 3));
        setBorder(new LineBorder(Color.black, 1, true));

        internalUpdate();
    }

    public Hole getHole() {
        return hole;
    }

    private void internalUpdate() {
        // TODO: Change variable to use number of korgools in the actual hole.
        int numberOfKorgools = 9;
        GridLayout layout = (GridLayout) getLayout();
        layout.setRows((int) Math.max(3, Math.ceil((double) numberOfKorgools / layout.getColumns())));
        for (int j = 0; j < numberOfKorgools; ++j) {
            add(new JKorgool());
        }
        int maxFillers = layout.getColumns() * layout.getRows();
        for (int j = numberOfKorgools; j < maxFillers; ++j) {
            add(new JEmptyComponent());
        }
    }

    public void updateHole() {
        removeAll();
        internalUpdate();
        updateUI();
    }

}
