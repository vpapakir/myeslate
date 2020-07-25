package gr.cti.eslate.utils;

import java.awt.*;
import javax.swing.*;

/**
 * This class extends JPopupMenu in such a way that it
 * always appears inside the screen.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class ESlateJPopupMenu extends JPopupMenu
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Constructs an ESlateJPopupMenu without an "invoker".
   */
  public ESlateJPopupMenu()
  {
    super();
  }

  /**
   * Constructs an ESlateJPopupMenu with the specified title.
   * @param     label   The string that a UI may use to display as a title for
   *                    the popup menu.
   */
  public ESlateJPopupMenu(String label)
  {
    super(label);
  }

  /**
   * Displays the popup menu at the position x,y in the coordinate space of
   * the component invoker. X and y as taken as hints, and are adjusted so
   * that the popup menu appears inside the screen.
   * @param     invoker The component in whose space the popup menu is to
   *                    appear.
   * @param     x       The x coordinate in invoker's coordinate space at
   *                    which the popup menu is to be displayed
   * @param     y       The y coordinate in invoker's coordinate space at
   *                    which the popup menu is to be displayed
   */
  public void show(Component invoker, int x, int y)
  {
    // Calculate any necessary offsets, to ensure that the popup menu
    // appears inside the screen.
    Dimension popSize = getPreferredSize();
    Point loc = invoker.getLocationOnScreen();
    Dimension screen = getToolkit().getScreenSize();
    int offX, offY;
    if (loc.x + x + popSize.width >= screen.width) {
      offX = -(loc.x + x + popSize.width - screen.width);
    }else{
      offX = 0;
    }
    if (loc.y + y + popSize.height >= screen.height) {
      offY = -(loc.y + y + popSize.height - screen.height);
    }else{
      offY = 0;
    }
    // Display the pop-up menu.
    super.show(invoker, x+offX, y+offY);
  }

}
