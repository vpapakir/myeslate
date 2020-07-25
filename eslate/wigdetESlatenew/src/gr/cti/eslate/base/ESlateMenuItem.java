package gr.cti.eslate.base;

import javax.swing.*;

/**
 * This class extends the JMenuItem class, adding a reference to the plug
 * associated with the particular menu item. This makes it easy to
 * identify the plug associated with this item when the item is selected
 * from a menu.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class ESlateMenuItem extends JMenuItem
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private Plug plug;

  /**
   * Construct an ESlateMenuItem.
   * @param     plug    The plug associated with the menu item.
   * @param     icon    The icon of the menu item.
   */
  public ESlateMenuItem(Plug plug, Icon icon)
  {
    super(plug.getName(), icon);
    this.plug = plug;
  }

  /**
   * Create a PlugJMenuItem.
   * @param   name    The name of the menu item.
   * @param   plug     The plug associated with the menu item.
   */
  public ESlateMenuItem(Plug plug, String name)
  {
    super(name);
    this.plug = plug;
  }
  
  /**
   * Returns the plug associated with the menu item.
   * @return    The plug.
   */
  public Plug getPlug()
  {
    return plug;
  }
}
