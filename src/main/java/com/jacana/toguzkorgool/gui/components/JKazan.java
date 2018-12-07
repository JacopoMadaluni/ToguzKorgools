package com.jacana.toguzkorgool.gui.components;

import com.jacana.toguzkorgool.Kazan;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * JKazan is a graphical representation of a kazan on a Toguz Korgool board.
 * It is responsible of displaying the Korgools that are stored in the
 * corresponding back-end Kazan object.
 */
public class JKazan extends JPanel {
    private Kazan kazan;
    private int korgoolsInGui = 0;

    public JKazan(Kazan kazan) {
        super();
        this.kazan = kazan;

        GridLayout layout = new GridLayout(6, 27);
        setLayout(layout);
        setBorder(new LineBorder(Color.black, 1, false));

        internalUpdate();
    }

    public Kazan getKazan() {
        return kazan;
    }

    public void setKazan(Kazan kazan) {
        if (this.kazan != null) throw new IllegalArgumentException("kazan already set");
        this.kazan = kazan;
    }

    /**
     * Update the display of Korgools after every move, in order to account
     * for possible changes to their number.
     */
    private void internalUpdate() {
        int numberOfKorgools = kazan != null ? kazan.getKorgools() : 0;
        if (numberOfKorgools > 0) {
            for (int j = this.korgoolsInGui; j < numberOfKorgools; ++j) {
                remove(this.korgoolsInGui + 1);
                add(new JKorgool(), 0);
                this.korgoolsInGui++;
            }
        } else {
            removeAll();
            GridLayout layout = (GridLayout) getLayout();
            int fillers = layout.getColumns() * layout.getRows();
            for (int j = 0; j < fillers; ++j) {
                add(new JEmptyComponent());
            }
            this.korgoolsInGui = 0;
        }
        this.setToolTipText("Korgools: " + numberOfKorgools);
    }

    /**
     * Wrapper method to carryout the graphical update of the component.
     */
    public void updateKazan() {
        internalUpdate();
    }
}
