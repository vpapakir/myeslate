package gr.cti.eslate.webWindow;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;


class MainPanel extends Container {

    MainPanel() {
        bBorder = false;
    }

    MainPanel(boolean border) {
        bBorder = false;
        bBorder = border;
    }

    public void paint(Graphics g) {
        Dimension d = getSize();

        //g.setColor(SystemColor.control);
        //g.fillRect(0, 0, d.width, d.height);
        if (bBorder) {
            g.setColor(Color.black);
            g.drawRect(4, 4, d.width - 9, d.height - 9);
        }
        super.paint(g);
    }

    public void update(Graphics g) {
        paint(g);
    }

    boolean bBorder;
}
