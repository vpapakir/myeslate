package gr.cti.eslate.editor;

import java.awt.*;

/**
 * Dialog utilities.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class DialogUtils
{
  /**
   * Since this class contains only static methods, there is no need to
   * instantiate objects of this class.
   */
  private DialogUtils()
  {
  }

  /**
   * Positions a dialog so that a given component is obscured as little as
   * possible.
   * @param     compo   The component which must not be obscured.
   * @param     d       The dialog to position.
   */
  public static void positionDialog(Component compo, Dialog d)
  {
    Dimension dSize = d.getSize();
    int dw = dSize.width;
    int dh = dSize.height;
    Dimension cSize = compo.getSize();
    int cw = cSize.width;
    int ch = cSize.height;
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    int scrw = scrSize.width;
    int scrh = scrSize.height;
    Point p = compo.getLocationOnScreen();
    int cx = p.x;
    int cy = p.y;

    // Try top side, left.
    int x = Math.max(0, cx);
    int y = Math.max(0, cy - dh);
    if (x+dw >= scrw) {
      x = scrw - dw;
    }
    if (y+dh >= scrh) {
      y = scrh - dh;
    }
    int newX = x;
    int newY = y;
    Rectangle r1 = new Rectangle(cx, cy, cw, ch);
    Rectangle r2 = new Rectangle(x, y, dw, dh);
    Rectangle r = r1.intersection(r2);
    int area = r.width * r.height;

    // Try left side, top.
    x = Math.max(0, cx - dw);
    y = Math.max(0, cy);
    if (x+dw >= scrw) {
      x = scrw - dw;
    }
    if (y+dh >= scrh) {
      y = scrh - dh;
    }
    r2 = new Rectangle(x, y, dw, dh);
    r = r1.intersection(r2);
    int area2 = r.width * r.height;
    if (area2 < area) {
      area = area2;
      newX = x;
      newY = y;
    }

    // Try Right side, bottom.
    x = Math.max(0, cx + cw);
    y = Math.max(0, cy + ch - dh + 1);
    if (x+dw >= scrw) {
      x = scrw - dw;
    }
    if (y+dh >= scrh) {
      y = scrh - dh;
    }
    r2 = new Rectangle(x, y, dw, dh);
    r = r1.intersection(r2);
    area2 = r.width * r.height;
    if (area2 < area) {
      area = area2;
      newX = x;
      newY = y;
    }

    // Try bottom side, right.
    x = Math.max(0, cx + cw - dw + 1);
    y = Math.max(0, cy + ch);
    if (x+dw >= scrw) {
      x = scrw - dw;
    }
    if (y+dh >= scrh) {
      y = scrh - dh;
    }
    r2 = new Rectangle(x, y, dw, dh);
    r = r1.intersection(r2);
    area2 = r.width * r.height;
    if (area2 < area) {
      area = area2;
      newX = x;
      newY = y;
    }

    d.setLocation(newX, newY);
  }

}
