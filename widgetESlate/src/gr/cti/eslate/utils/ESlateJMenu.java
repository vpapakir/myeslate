package gr.cti.eslate.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This class extends JMenu in such a way that the associated popup menu
 * always appears inside the screen.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class ESlateJMenu extends JMenu implements MenuListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Constructs a new ESlateJMenu with no text.
   */
  public ESlateJMenu()
  {
    super();
    addMenuListener(this);
  }

  /**
   * Constructs a new ESlateJMenu with the supplied string as its text.
   * @param     s       The text for the menu label.
   */
  public ESlateJMenu(String s)
  {
    super(s);
    addMenuListener(this);
  }

  /**
   * Constructs a new ESlateJMenu whose properties are taken from the Action
   * supplied.
   * @param     a       An Action.
   */
  public ESlateJMenu(Action a)
  {
    super(a);
    addMenuListener(this);
  }

  /**
   * Constructs a new ESlateJMenu with the supplied string as its text and
   * specified as a tear-off menu or not.
   * @param     s       The text for the menu label.
   * @param     b       Can the menu be torn off (not yet implemented).
   */
  public ESlateJMenu(String s, boolean b)
  {
    super(s, b);
    addMenuListener(this);
  }

  public void menuCanceled(MenuEvent e)
  {
  }

  public void menuDeselected(MenuEvent e)
  {
    JPopupMenu popup = getPopupMenu();
    popup.setVisible(false);
  }

  public void menuSelected(MenuEvent e)
  {
    // Calculate any necessary offsets, to ensure that the pop-up menu
    // appears inside the screen.
    if (!isShowing()) {
      return;
    }
    JPopupMenu popup = getPopupMenu();
    Dimension popupSize = popup.getPreferredSize();
    Dimension menuSize = getSize();
    Point loc = getLocationOnScreen();
    Dimension screen = getToolkit().getScreenSize();
    int offX, offY;
    if (loc.x + menuSize.width + popupSize.width >= screen.width) {
      // Use this offset to move submenu to the left, just enough
      // that it fits in the screen.
      // offX = -(loc.x + popupSize.width - screen.width);
      //
      // Use this offset to move submenu to the left of the parent menu.
      // Due to a bug in the Motif look and feel implementation(?) this
      // does not always work under that look and feel, as the popup menu
      // gets clipped against the parent popup menu.
      offX = -popupSize.width;
    } else {
      offX = menuSize.width;
    }
    if (loc.y + popupSize.height >= screen.height) {
      offY = -(loc.y + popupSize.height - screen.height);
    } else {
      offY = 0;
    }
    // Reposition the pop-up menu.
    popup.show(this, offX, offY);
  }
}
