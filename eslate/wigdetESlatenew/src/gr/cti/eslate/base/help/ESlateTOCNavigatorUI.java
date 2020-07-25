package gr.cti.eslate.base.help;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.help.*;
import javax.help.plaf.basic.*;

/**
 * UI for the help's table of contents.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ESlateTOCNavigatorUI extends BasicTOCNavigatorUI
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public ESlateTOCNavigatorUI(JHelpTOCNavigator b)
  {
    super(b);
  }

  public static ComponentUI createUI(JComponent x)
  {
    return new ESlateTOCNavigatorUI((JHelpTOCNavigator)x);
  }

  public void installUI(JComponent c)
  {
    super.installUI(c);
    updateColors();
  }

  /**
   * Set the correct colors for the component, based on the currently
   * installed look and feel.
   */
  public void updateColors()
  {
    tree.setBackground(UIManager.getColor("Tree.background"));

    JComponent c = (JComponent)(tree.getCellRenderer());
    if (c instanceof BasicTOCCellRenderer) {
      BasicTOCCellRenderer r = (BasicTOCCellRenderer)c;
      r.setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));
      r.setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
      r.setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));
      r.setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));
    }
  }
}
