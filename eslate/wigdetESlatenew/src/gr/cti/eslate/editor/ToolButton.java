package gr.cti.eslate.editor;

import java.awt.*;
import javax.swing.*;

/**
 * This class implements the buttons of the editor's tool bar.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */

class ToolButton extends JButton
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  /**
   * Construct a ToolButton.
   * @param     icon    The button's icon.
   */
  ToolButton(Icon icon)
  {
    super(icon);
    setMargin(new Insets(0, 0, 0, 0));
    setFocusPainted(false);
  }
}
