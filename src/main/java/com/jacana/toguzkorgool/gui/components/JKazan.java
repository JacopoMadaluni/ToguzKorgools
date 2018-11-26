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

    public JKazan(Kazan kazan) {
        super();
        this.kazan = kazan;

        setLayout(new GridLayout(6, 27));
        setBorder(new LineBorder(Color.black, 1, true));

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
        GridLayout layout = (GridLayout) getLayout();
        for (int j = 0; j < numberOfKorgools; ++j) {
            add(new JKorgool());
        }
        int maxFillers = layout.getColumns() * layout.getRows();
        for (int j = numberOfKorgools; j < maxFillers; ++j) {
            add(new JEmptyComponent());
        }
    }

    /**
     * Wrapper method to carryout the graphical update of the component.
     */
    public void updateKazan() {
        removeAll();
        internalUpdate();
    }
}
