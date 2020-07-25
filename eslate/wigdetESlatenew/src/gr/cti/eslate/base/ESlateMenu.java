package gr.cti.eslate.base;

import java.awt.event.*;
import javax.swing.*;
import gr.cti.eslate.utils.*;

/**
 * This class extends the ESlateJMenu class, adding a reference to the plug
 * associated with the particular menu. This makes it easy to
 * identify the plug associated with this menu when the menu is selected
 * from the plug popup menu or a submenu thereof.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class ESlateMenu extends ESlateJMenu
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private Plug myPlug;

  /**
   * Construct an ESlateMenu.
   * @param     plug    The plug associated with the menu.
   * @param     icon    The icon of the menu.
   */
  public ESlateMenu(Plug plug, Icon icon)
  {
    super(plug.getName());
    setIcon(icon);
    myPlug = plug;
  }

  public void processMouseEvent(
    MouseEvent e, MenuElement[] path, MenuSelectionManager manager)
  {
    super.processMouseEvent(e, path, manager);
    int id = e.getID();
    if (id == MouseEvent.MOUSE_RELEASED) {
      ActionEvent ae = new ActionEvent(this, id, getActionCommand());
      fireActionPerformed(ae);
      fireMenuDeselected();
    }
  }

  /**
   * Returns the plug associated with the menu.
   * @return    The plug.
   */
  public Plug getPlug()
  {
    return myPlug;
  }

}
