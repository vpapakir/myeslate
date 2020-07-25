package gr.cti.eslate.base.help;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.help.*;
import javax.help.plaf.basic.*;

/**
 * UI for the help's index.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 19-May-2006
 */
public class ESlateIndexNavigatorUI extends BasicIndexNavigatorUI
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public ESlateIndexNavigatorUI(JHelpIndexNavigator b)
  {
    super(b);
  }

  public static ComponentUI createUI(JComponent x)
  {
    return new ESlateIndexNavigatorUI((JHelpIndexNavigator)x);
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
    tree.setBackground(UIManager.getColor("Panel.background"));

    JComponent c = (JComponent)(tree.getCellRenderer());
    if (c instanceof BasicIndexCellRenderer) {
      BasicIndexCellRenderer r = (BasicIndexCellRenderer)c;
      r.setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
      r.setBackgroundSelectionColor(UIManager.getColor("control"));
      r.setTextNonSelectionColor(UIManager.getColor("controlText"));
      r.setTextSelectionColor(UIManager.getColor("controlText"));
    }
  }
}
