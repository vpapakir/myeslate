package gr.cti.eslate.utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gr.cti.eslate.base.*;

/**
 * Provides an auxiliary window for a component, keeping track of
 * whether the window is currently open or closed.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */

public class AuxFrame extends Frame implements WindowListener
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Indicates whether the window is currently displayed on screen or not.
   */
  public boolean shown;
  private Dimension WinSize;

  /**
   * Creates a window with a given title and size.
   * The window is not displayed on screen.
   * @param     title   The window's title.
   * @param     size    The window's size.
   */
  public AuxFrame(String title, Dimension size) {
    super(title);
    if (!UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
      setBackground(Color.lightGray);
    }else{
      // A kludge, if I ever saw one!!
      setBackground(new JPanel().getBackground());
    }
    setForeground(Color.black);
    setIconImage(ESlate.getIconImage());
    WinSize = size;
    addWindowListener(this);
    shown = false;
  }

  public void windowOpened(WindowEvent event) {
    shown = true;
  }

  public void windowClosing(WindowEvent event) {
    setVisible(false);
    shown = false;
  }

  public void windowClosed(WindowEvent event) {
    setVisible(false);
    shown = false;
  }

  public void windowIconified(WindowEvent event) {
    shown = false;
  }

  public void windowDeiconified(WindowEvent event) {
    shown = true;
  }

  public void windowActivated(WindowEvent event) {
  }

  public void windowDeactivated(WindowEvent event) {
  }

  public void actionPerformed(ActionEvent event) {
  }

  public Dimension getMinimumSize(){
    return WinSize;
  }

  public Dimension getPreferredSize(){
    return WinSize;
  }

  public void setSize(Dimension s) {
    super.setSize(s);
    WinSize = s;
  }

}
