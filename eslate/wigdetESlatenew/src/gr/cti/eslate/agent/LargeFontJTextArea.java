package gr.cti.eslate.agent;

import java.awt.*;
import javax.swing.*;

/**
 * A <code>JTextArea</code> with a font size 2 points larger than the default,
 * which is too small.
 */
class LargeFontJTextArea extends JTextArea
{
  /**
   * Create a <code>LargeFontJTextArea</code> instance.
   */
  LargeFontJTextArea()
  {
    super();
    setFont(new Font("Dialog", Font.PLAIN, 12));
  }
}
