package gr.cti.eslate.navigator.gui;

import java.awt.event.*;
import javax.swing.*;

import gr.cti.eslate.navigator.models.INavigator;

/**
 * Navigator status bar.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2003
 */
public class NavigatorStatusBar extends JToolBar
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  //FIELDs//
  private JLabel lblStatus = new JLabel();
  //private INavigator navigator;
  JLabel lblSizeHelper = new JLabel();

  //METHODs//
  public void setStatusText(String text)
  {
    lblStatus.setText(text);
  }

/*
  // If secure page show small lock icon at the left (use the lblStatus and
  // set an icon to it to show at its left-side)
  public void setSecureIconVisible(boolean flag){}
  public boolean isSecureIconVisible(){}

  //...should also show download progress bar etc.
*/

  ///////////

  public void setNavigator(INavigator nav)
  {
    //navigator = nav;
  }

  public NavigatorStatusBar()
  {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setBorder(BorderFactory.createLoweredBevelBorder());
    this.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        this_mouseClicked(e);
      }
    });
    lblStatus.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        lblStatus_mouseClicked(e);
      }
    });
    // Use this so that the status bar keeps its height when it shows not
    // status text.
    lblSizeHelper.setText(" ");
    this.add(lblSizeHelper, null);
    this.add(lblStatus, null);
    setFloatable(false);
  }

  void lblStatus_mouseClicked(MouseEvent e)
  {
    clicked();
  }

  void this_mouseClicked(MouseEvent e)
  {
    clicked();
  }

  private void clicked()
  {
    //System.out.println("StatusBar clicked");
    setVisible(false); //16Jun2000: hide statusbar if clicked
  }

}
