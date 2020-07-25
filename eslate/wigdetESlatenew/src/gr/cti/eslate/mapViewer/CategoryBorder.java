package gr.cti.eslate.mapViewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * A class which implements an arbitrary border
 * with the addition of a String title in a
 * the left side.
 * <p>
 *
 * @version 1.0 04 Oct 2000
 * @author Giorgos Vasiliou
 */
class CategoryBorder extends AbstractBorder {
    protected String title;
    protected Border border;
    protected Font   titleFont;
    protected Color  titleColor;
    protected int    stripWidth;

    private Point textLoc = new Point();

    /**
     * Creates a CategoryBorder instance.
     *
     * @param title  the title the border should display
     */
    CategoryBorder(String title)     {
        JLabel proto=new JLabel();
        this.title=title;
        titleFont=new Font(proto.getFont().getName(),proto.getFont().getStyle(),(int) (proto.getFont().getSize()*11d/12d));
        titleColor=Color.white;
        stripWidth=proto.getFontMetrics(titleFont).getHeight()+2;
    }
    /**
     * Paints the border for the specified component with the
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        if (getTitle() == null || getTitle().equals(""))
            return;

        g.setFont(titleFont);

        FontMetrics fm=c.getFontMetrics(titleFont);

        //Paint the darkened rectangle
        g.setColor(new Color(0,0,0,160));
        g.fillRect(1,0,stripWidth,height);
        g.drawLine(stripWidth,0,x+width-1,0);
        g.drawLine(x+width-1,0,x+width-1,y+height-1);
        g.drawLine(x+width-1,y+height-1,stripWidth,y+height-1);
        //Paint the label
        g.setColor(Color.white);
        Graphics2D g2=(Graphics2D) g;
        AffineTransform tmp=g2.getTransform();
        g2.transform(AffineTransform.getTranslateInstance(0,height));
        g2.transform(AffineTransform.getRotateInstance(-Math.PI/2,0,0));
        g2.drawString(title,3,fm.getAscent()+1);
        g2.setTransform(tmp);
    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left=stripWidth+2;
        insets.top=insets.bottom=insets.right=2;

        if(c == null || getTitle() == null || getTitle().equals(""))    {
            return insets;
        }

        return insets;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() {
        return false;
    }

    /**
     * Returns the title of the titled border.
     */
    public String getTitle() {
        return title;
    }

}
