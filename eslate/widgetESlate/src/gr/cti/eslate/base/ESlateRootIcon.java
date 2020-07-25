package gr.cti.eslate.base;

import java.awt.*;
import javax.swing.*;

/**
 * This class is used to draw the icons for the root of the plug trees in the
 * plug editor. The icon is a 26x23 square with a 16x16 image inside it.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class ESlateRootIcon implements Icon
{
  /**
   * The width of the icon.
   */
  final static int WIDTH = 26;
  /**
   * The height of the icon.
   */
  final static int HEIGHT = 23;
  /**
   * The image inside the icon.
   */
  private Image img;

  /**
   * Create the icon.
   * @param     img     The image to draw inside the icon. If <code>img</code>
   *                    is null, a default image is provided.
   */
  public ESlateRootIcon(Image img)
  {
    if (img == null) {
      this.img = ESlate.getIconImage();
    }else{
      this.img = img;
    }
  }

  /**
   * Return the height of the icon.
   * @return    The requested height.
   */
  public int getIconHeight()
  {
    return HEIGHT;
  }

  /**
   * Return the width of the icon.
   * @return    The requested width.
   */
  public int getIconWidth()
  {
    return WIDTH;
  }

  /**
   * Draw the icon at the specified location.
   * @param     c       The component on which to paint the icon.
   * @param     g       The graphics context on which to paint the icon.
   * @param     x       The horizontal coordinate of the location where the
   *                    icon will be drawn.
   * @param     y       The vertical coordinate of the location where the
   *                    icon will be drawn.
   */
  public void paintIcon(Component c, Graphics g, int x, int y)
  {
    int imageWidth = Math.min(img.getWidth(null), WIDTH-2);
    if (imageWidth < 0) {
      imageWidth = 16;
    }
    int imageHeight = Math.min(img.getHeight(null), HEIGHT-2);
    if (imageHeight < 0) {
      imageHeight = 16;
    }
    int xoff = (WIDTH - imageWidth) / 2;
    int yoff = (HEIGHT - imageHeight) / 2;
    g.drawImage(img, x+xoff, y+yoff, imageWidth, imageHeight, null);
  }
}
