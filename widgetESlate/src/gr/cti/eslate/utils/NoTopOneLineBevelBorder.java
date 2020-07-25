package gr.cti.eslate.utils;

import java.awt.*;
import java.awt.Component;
import javax.swing.border.*;

/**
 * This is a thinner BevelBorder class with no top line.
 * Useful as a component border.
 *
 * @author      Giorgos Vasiliou
 * @version     2.0.0, 18-May-2006
 */
public class NoTopOneLineBevelBorder extends BevelBorder
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    public NoTopOneLineBevelBorder(int type) {
        super(type);
    }

    /**
     * Creates a bevel border with the specified type, highlight and
     * shadow colors.
     * @param bevelType the type of bevel for the border
     * @param highlight the color to use for the bevel highlight
     * @param shadow the color to use for the bevel shadow
     */
    public NoTopOneLineBevelBorder(int bevelType, Color highlight, Color shadow) {
        super(bevelType, highlight, highlight, shadow, shadow);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(1,1,1,1);
    }

    public Insets getBorderInsets(Component c, Insets i) {
        i.top=i.left=i.right=i.bottom=1;
        return i;
    }

    protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height)  {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getHighlightOuterColor(c));
        g.drawLine(0, 0, 0, h-1);

        g.setColor(getShadowOuterColor(c));
        g.drawLine(1, h-1, w-1, h-1);
        g.drawLine(w-1, 1, w-1, h-2);

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

    protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height)  {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getShadowOuterColor(c));
        g.drawLine(0, 0, 0, h-1);

        g.setColor(getHighlightOuterColor(c));
        g.drawLine(1, h-1, w-1, h-1);
        g.drawLine(w-1, 1, w-1, h-2);

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}
