package com.jacana.toguzkorgool.gui.components;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Rehman, Faris (k1764099), Bass, Alexander (k1763794).
 * <p>
 * JKorgool is a graphical representation of a single Korgool in the game of
 * Toguz Korgool.
 */
public class JKorgool extends JComponent {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillOval(0, 0, this.getWidth(), this.getHeight());
    }
}
