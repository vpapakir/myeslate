package gr.cti.eslate.panel;

import java.awt.*;
import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * This class implements a JMenuItem with a reference to an E-Slate handle or
 * a component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
class PanelMenuItem extends JMenuItem
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private ESlateHandle handle;
  private Component component;

  /**
   * Creates the menu item.
   * @param     text    The text of the menu item.
   * @param     h       The E-Slate handle associated with the menu item.
   */
  PanelMenuItem(String text, ESlateHandle h)
  {
    super(text);
    handle = h;
    component = null;
  }

  /**
   * Creates the menu item.
   * @param     text    The text of the menu item.
   * @param     c       The component associated with the menu item.
   */
  PanelMenuItem(String text, Component c)
  {
    super(text);
    component = c;
    handle = null;
  }

  /**
   * Returns the E-Slate handle associated with this item.
   * @return    The requested handle.
   */
  ESlateHandle getHandle()
  {
    return handle;
  }
  
  /**
   * Returns the component associated with this item.
   * @return    The requested component.
   */
  Component getCompo()
  {
    return component;
  }
}
