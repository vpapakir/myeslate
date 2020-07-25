package gr.cti.eslate.navigator.gui;

import gr.cti.eslate.navigator.models.INavigator;
import gr.cti.eslate.navigator.gui.NavigatorGUI;
import gr.cti.eslate.navigator.Res;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * Navigator menu bar.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.0, 2-Jun-2003
 */
public class NavigatorMenuBar extends JMenuBar
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  INavigator navigator;
  JMenu mnuNavigation = new JMenu();
  JMenuItem mBack = new JMenuItem();
  JMenuItem mForward = new JMenuItem();
  JMenuItem mHome = new JMenuItem();
  JMenu mnuView = new JMenu();
  JCheckBoxMenuItem cbmAddressBar = new JCheckBoxMenuItem();
  JCheckBoxMenuItem cbmToolBar = new JCheckBoxMenuItem();
  JMenuItem mRefresh = new JMenuItem();
  JCheckBoxMenuItem cbmStatusBar = new JCheckBoxMenuItem();
  JMenuItem mStop = new JMenuItem();

  public void setNavigator(INavigator nav)
  {
   navigator=nav;
  }

  //////

  public NavigatorMenuBar()
  {
    try {
      jbInit();
      // 15Mar2000: heavy-weight menus
      mnuNavigation.getPopupMenu().setLightWeightPopupEnabled(false);
      //15Mar2000: heavy-weight menus
      mnuView.getPopupMenu().setLightWeightPopupEnabled(false);
      enableBack(false);
      enableForward(false);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setBorderPainted(true); // ?

    // Navigation menu

    mnuNavigation.setText(Res.localize("Navigation"));

    mBack.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Back.gif")
    ));
    mBack.setText(Res.localize("Back"));
    mBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        mBack_actionPerformed(e);
      }
    });

    mForward.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource(
        "images/Forward.gif"
      )
    ));
    mForward.setText(Res.localize("Forward"));
    mForward.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        mForward_actionPerformed(e);
      }
    });

    mHome.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Home.gif")
    ));
    mHome.setText(Res.localize("Home"));
    mHome.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        mHome_actionPerformed(e);
      }
    });

    // View menu

    mStop.setText(Res.localize("Stop"));
    mStop.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Stop.gif")
    ));
    mStop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        mStop_actionPerformed(e);
      }
    });

    mRefresh.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Refresh.gif")
    ));
    mRefresh.setText(Res.localize("Refresh"));
    mRefresh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        mRefresh_actionPerformed(e);
      }
    });

    mnuView.setText(Res.localize("View"));
    mnuView.addMenuListener(new javax.swing.event.MenuListener() {
      public void menuSelected(MenuEvent e)
      {
        mnuView_menuSelected(e);
      }
      public void menuDeselected(MenuEvent e)
      {
      }
      public void menuCanceled(MenuEvent e)
      {
      }
    });

    cbmAddressBar.setText(Res.localize("Address bar"));
    cbmAddressBar.addChangeListener(new javax.swing.event.ChangeListener(){
      public void stateChanged(ChangeEvent e)
      {
        cbmAddressBar_stateChanged(e);
      }
    });

    cbmToolBar.setSelected(true);
    cbmToolBar.setText(Res.localize("Toolbar"));
    cbmToolBar.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e)
      {
        cbmToolBar_stateChanged(e);
      }
    });

    cbmStatusBar.setText(Res.localize("Status bar"));
    cbmStatusBar.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e)
      {
        cbmStatusBar_stateChanged(e);
      }
    });

    ////////

    this.add(mnuNavigation);
    this.add(mnuView);

    mnuNavigation.add(mBack);
    mnuNavigation.add(mForward);
    mnuNavigation.addSeparator();
    mnuNavigation.add(mHome);

    mnuView.add(mStop);
    mnuView.add(mRefresh);
    mnuView.addSeparator();
    mnuView.add(cbmAddressBar);
    mnuView.add(cbmToolBar);
    mnuView.add(cbmStatusBar);

  }

  void cbmAddressBar_stateChanged(ChangeEvent e) {
   getGUI().setAddressBarVisible(cbmAddressBar.isSelected()); // 16Jun2000
  }

  void cbmToolBar_stateChanged(ChangeEvent e)
  {
    getGUI().setToolBarVisible(cbmToolBar.isSelected());
  }

  void cbmStatusBar_stateChanged(ChangeEvent e)
  {
    getGUI().setStatusBarVisible(cbmStatusBar.isSelected()); // 16Jun2000
  }

  private NavigatorGUI getGUI()
  {
    // go up 2 levels deep in the hierarchy
    return (NavigatorGUI)getParent().getParent();
  }

  void mForward_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.forward();
    }
  }

  void mBack_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.back();
    }
  }

  void mHome_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.home();
    }
  }

  void mStop_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.stop();
    }
  }

  void mRefresh_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.refresh();
    }
  }

  // 16Jun2000: synchronize the checkboxes with the current state
  void mnuView_menuSelected(MenuEvent e)
  {
    // System.out.println("View menu selected");
    cbmToolBar.setSelected(getGUI().isToolBarVisible());
    cbmAddressBar.setSelected(getGUI().isAddressBarVisible());
    cbmStatusBar.setSelected(getGUI().isStatusBarVisible());
  }

  void enableBack(boolean flag)
  {
    mBack.setEnabled(flag);
  }

  void enableForward(boolean flag)
  {
    mForward.setEnabled(flag);
  }
}
