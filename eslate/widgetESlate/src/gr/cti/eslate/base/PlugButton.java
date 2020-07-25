package gr.cti.eslate.base;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;

import javax.swing.*;

/**
 * Implements the icon representing a plug that is used to select a plug from
 * a menu in an E-Slate component.
 * @author      Petros Kourouniotis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PlugButton implements Icon, ImageObserver
{
  public PlugButton(ImageIcon ic)
  {
        this.icon = ic;
  }

  public void paintIcon(Component c, Graphics gr, int x0, int y0)
  {
    int w = getIconWidth();
    int h = getIconHeight();
    gr.drawImage(icon.getImage(), x0, y0, w, h, this);
  }

  public int getIconWidth()
  {
    return 26;
  }

  public int getIconHeight()
  {
    return 23;
  }

  public boolean imageUpdate(Image img, int infoflags, int x, int y,
                             int width, int height)
  {
    return false;
  }

  private ImageIcon icon;
}
