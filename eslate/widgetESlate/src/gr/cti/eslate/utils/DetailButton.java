package gr.cti.eslate.utils;

import java.awt.*;
import javax.swing.*;

/**
 * This class implements the buttons of the
 * ESlateOptionPane.showDetailMessageDialog class.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class DetailButton extends JButton
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Cached copy of the main panel of the dialog in which the button is
   * embedded.
   */
  JPanel mainPanel;
  /**
   * Cached copy of the detailed message panel of the dialog in which the
   * button is embedded.
   */
  JPanel detailPanel;
  /**
   * Cached copy of a component.
   */
  Component comp1;
  /**
   * Cached copy of another component.
   */
  Component comp2;

  /**
   * Construct a button.
   * @param     icon1   Name of icon shown when button is unpressed, relative
   *                    to this class' directory.
   * @param     icon2   Icon shown when icon is pressed, relative to this
   *                    class' directory.
   * @param     p1      Cached copy of the main panel of the dialog in which
   *                    the button is embedded.
   * @param     p2      Cached copy of the detailed message panel of the
   *                    dialog in which the button is embedded.
   * @param     c1      Cached copy of a component.
   * @param     c2      Cached copy of another component.
   */
  DetailButton(String icon1, String icon2, JPanel p1, JPanel p2,
          Component c1, Component c2)
  {
    super();
    mainPanel = p1;
    detailPanel = p2;
    comp1 = c1;
    comp2 = c2;
    setIcon(new ImageIcon(getClass().getResource(icon1)));
    setPressedIcon(new ImageIcon(getClass().getResource(icon2)));
    setFocusPainted(false);
    setMargin(new Insets(0,0,0,0));
    setBorder(null);
    setBorderPainted(false);
  }
}
