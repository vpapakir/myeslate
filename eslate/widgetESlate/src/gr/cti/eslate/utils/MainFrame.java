package gr.cti.eslate.utils;

import java.awt.*;
import java.awt.event.*;

/**
 * Provides a window if this program is run as an application.
 * Same as <A HREF="gr.cti.eslate.utils.AuxFrame.html">AuxFrame</A>, but exits
 * when the window is closed.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 * @see AuxFrame
 */

public class MainFrame extends AuxFrame
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Creates a window with a given title and size.
   * The window is not displayed on screen.
   * @param     title   The window's title.
   * @param     size    The window's size.
   */
  public MainFrame(String title, Dimension size) {
    super(title, size);
  } 

  public void windowClosed(WindowEvent event) {
    System.exit(0);
  }

  public void windowClosing(WindowEvent event) {
    System.exit(0);
  }

}
