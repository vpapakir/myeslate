package gr.cti.eslate.set;

import java.awt.*;
import javax.swing.*;

/**
 * This class provides a method for filling shapes with color or an image.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 * @see         gr.cti.eslate.set.Set
 */
class Fill
{
  /**
   * Only static methods are provided, so make the constructor private, to
   * prevent it from being invoked.
   */
  private Fill()
  {
  }

  /**
   * Draws a shape filled.
   * @param     g       The graphics context in which to draw the shape.
   * @param     icon    An optional image to use for filling the shape. If
   *                    <code>null</code>, the current color of <code>g</code>
   *                    will be used.
   */
  static void fill(Graphics g, Shape s, Icon icon)
  {
    fill(g, s, icon, 0);
  }

  static void fill(Graphics g, Shape s, Icon icon, int yOffset)
  {
    Graphics2D g2 = (Graphics2D)g;

    if (icon == null) {
      g2.fill(s);
    }else{
      Shape oldClip = g2.getClip();
      Rectangle r = s.getBounds();
      int w = r.x + r.width;
      int h = r.y + r.height - yOffset;
      g2.setClip(s);
      int iw = icon.getIconWidth();
      int ih = icon.getIconHeight();
      int yStart = -ih;
      int yEnd = h - yOffset;
      for (int y=yStart; y<yEnd; y+= ih) {
        for (int x=0; x<w; x+= iw) {
          //if (r.intersects(x, y, iw, ih)) {
            icon.paintIcon(null, g, x, y+yOffset);
          //}
        }
      }
      g2.setClip(oldClip);
    }
  }

}
