package com.jacana.toguzkorgool.gui.components;

import com.jacana.toguzkorgool.Hole;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;

/**
 * @author Rehman, Faris (k1764099), Bass, Alexander (k1763794).
 * <p>
 * JHole is a graphical representaion of a hole on a Teguz Korgool board.
 * It is responsible of displaying the Korgools that are stored in the
 * corresponding back-end Hole object.
 */
public class JHole extends JPanel
{
    private final Hole hole;
    
    public JHole(Hole hole)
    {
        super();
        this.hole = hole;
        
        setLayout(new GridLayout(3, 3));
        setBorder(new LineBorder(Color.black, 1, true));
        
        internalUpdate();
    }
    
    public Hole getHole()
    {
        return hole;
    }
    
    /**
     * Update the display of Korgools after every move, in order to account
     * for possible changes to their number.
     */
    private void internalUpdate()
    {
        int numberOfKorgools = hole.getKorgools();
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
    
    /**
     * Wrapper method to carryout the graphical update of the component.
     */
    public void updateHole()
    {
        removeAll();
        internalUpdate();
        updateUI();
    }
}
