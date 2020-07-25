package gr.cti.eslate.base.container;

import java.awt.Color;

import javax.swing.JPanel;

class GlassPane extends JPanel {
    public static final Color bgrColor = new Color(0, 0, 0, 120);

    public GlassPane() {
        super();
        setBackground(bgrColor);
        setOpaque(true);
    }
}