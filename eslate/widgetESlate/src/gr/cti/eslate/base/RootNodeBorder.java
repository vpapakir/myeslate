package gr.cti.eslate.base;

import java.awt.*;

import javax.swing.border.*;

/**
 * Implements the border for the root node of plug view windows. This border
 * consists of a line at the top and at the bottom of the component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see         gr.cti.eslate.base.PlugTreeCellRenderer
 */
public class RootNodeBorder implements Border
{
  private Color color;
  private int thickness;

  /**
   * Construct the border.
   * @param     color           The color of the border.
   * @param     thickness       The thickness of the border.
   */
  public RootNodeBorder(Color color, int thickness)
  {
    this.color = color;
    this.thickness = thickness;
  }

  /**
   * Returns the insets of the border.
   * @param     c       Ignored.
   * @return    The requested insets.
   */
  public Insets getBorderInsets(Component c)
  {
    return new Insets(thickness, 0, thickness, 0);
  }

  /**
   * Returns whether or not the border is opaque.
   * @return    True if yes, false if no.
   */
  public boolean isBorderOpaque()
  {
    return true;
  }

  /**
   * Paints the border for the specified component with the specified position
   * and size.
   * @param     c       The component for which this border is being painted.
   * @param     g       The paint graphics.
   * @param     x       The x position of the painted border.
   * @param     y       The y position of the painted border.
   * @param     width   The width of the painted border.
   * @param     height  The height of the painted border.
   */
  public void paintBorder(Component c, Graphics g, int x, int y, int width,
                          int height)
  {
    int iconWidth = ((PlugTreeCellRenderer)c).getIcon().getIconWidth();
    Color oldColor = g.getColor();
    g.setColor(color);
    for (int i=0; i<thickness; i++) {
      g.drawLine(x+iconWidth, y+i, x+width-1, y+i);
      g.drawLine(x+iconWidth, y+height-1-i, x+width-1, y+height-1-i);
    }
    g.setColor(oldColor);
  }
}
