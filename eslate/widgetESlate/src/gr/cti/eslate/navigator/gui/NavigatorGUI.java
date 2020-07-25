package gr.cti.eslate.navigator.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import gr.cti.eslate.navigator.models.INavigator;

/**
 * Navigator GUI.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.4, 11-Dec-2007
 */
public class NavigatorGUI extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  BorderLayout borderLayout1 = new BorderLayout();
  INavigator navigator;
  Component display;
  JPanel menus = new JPanel();
  NavigatorMenuBar menubar = new NavigatorMenuBar();
  NavigatorToolBar toolbar = new NavigatorToolBar();
  BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder titledBorder1;
  NavigatorStatusBar statusbar = new NavigatorStatusBar();
  Component component1;

  public void setAddressBarText(String text) //15Mar2000
  {
    toolbar.setAddressBarText(text);
  }

  public void setStatusBarText(String text)
  {
    statusbar.setStatusText(text); //16Jun2000
  }

  public void setTitle(String text) //14Jun2000
  {
    // NOP
  }

  public void setNavigator(Component display, INavigator nav)
  {
    // 14Jun2000: fixed-bug: was checking for "navigator!=null" instead of
    // "display!=null"
    if (this.display != null) {
      remove(this.display);
    }
    navigator = nav;
    this.display = display;
    if (display != null) { //14Jun2000
      add(display, BorderLayout.CENTER);
    }

    menubar.setNavigator(nav);
    toolbar.setNavigator(nav);

    revalidate();
  }

  public void enableBack(boolean enable)
  {
    toolbar.enableBack(enable);
    menubar.enableBack(enable);
  }

  public void enableForward(boolean enable)
  {
    toolbar.enableForward(enable);
    menubar.enableForward(enable);
  }

  // menuBarVisible //

  public void setMenuBarVisible(boolean flag)
  {
    menubar.setVisible(flag);
    revalidate();
  }

  public boolean isMenuBarVisible()
  {
    return menubar.isVisible();
  }

  // toolBarVisible //

  public void setToolBarVisible(boolean flag)
  {
    toolbar.setVisible(flag);
    revalidate();
   }

  public boolean isToolBarVisible()
  {
    return toolbar.isVisible();
  }

  // addressBarVisible //

  public void setAddressBarVisible(boolean flag) //16Jun2000
  {
    toolbar.getAddressBar().setVisible(flag);
    revalidate();
  }

  public boolean isAddressBarVisible() //16Jun2000
  {
    return toolbar.getAddressBar().isVisible();
  }

  // statusBarVisible //

  public void setStatusBarVisible(boolean flag) //16Jun2000
  {
    statusbar.setVisible(flag);
    revalidate();
  }

  public boolean isStatusBarVisible() //16Jun2000
  {
    return statusbar.isVisible();
  }

  /**
   * Enables or disables the "home" button in the navigator toolbar.
   * @param     enabled True to enable the "home" button, false to disable it.
   */
  public void enableHomeButton(boolean enabled)
  {
    toolbar.enableHome(enabled);
  }

  /////////////////

  public NavigatorGUI()
  {
    try {
      jbInit();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    titledBorder1 = new TitledBorder("");
    component1 = Box.createHorizontalStrut(8);
    this.setLayout(borderLayout1);
    menus.setLayout(borderLayout2);
    menubar.setBorder(null);
    menus.setBorder(BorderFactory.createEtchedBorder());
    this.add(menus, BorderLayout.NORTH);
    menus.add(menubar, BorderLayout.NORTH);
    menus.add(toolbar, BorderLayout.SOUTH);
    menus.add(component1, BorderLayout.CENTER);
    this.add(statusbar, BorderLayout.SOUTH);
  }
}
