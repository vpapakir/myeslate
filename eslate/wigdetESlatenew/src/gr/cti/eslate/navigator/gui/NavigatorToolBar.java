package gr.cti.eslate.navigator.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import gr.cti.eslate.navigator.models.INavigator;

/**
 * Navigator Tool bar.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.4, 11-Dec-2007
 */
public class NavigatorToolBar extends JToolBar
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JButton btnHome = new JButton();
  private JButton btnBack = new JButton();
  private JButton btnForward = new JButton();
  private JButton btnStop = new JButton();
  private JButton btnRefresh = new JButton();  

  private NavigatorAddressBar addressBar = new NavigatorAddressBar();
  private INavigator navigator;

  private Component strut1;
  private Component strut2;
  private Component strut3;

  public NavigatorAddressBar getAddressBar() // 16Jun2000
  {
    return addressBar;
  }

  public void setAddressBarText(String text) // 15Mar2000
  {
    addressBar.setAddressText(text);
  }

  public void setNavigator(INavigator nav)
  {
    navigator=nav;
    addressBar.setNavigator(nav);
  }

  public NavigatorToolBar()
  {
    try {
      jbInit();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    strut1 = Box.createHorizontalStrut(8);
    strut2 = Box.createHorizontalStrut(8);
    strut3 = Box.createHorizontalStrut(8);

    //btnHome.setBorder(BorderFactory.createEtchedBorder());
    btnHome.setFocusPainted(false);
    btnHome.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Home.gif")
    ));
    //btnHome.setText(Res.localize("Home"));
    btnHome.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        btnHome_actionPerformed(e);
      }
    });

    //btnBack.setBorder(BorderFactory.createEtchedBorder());
    btnBack.setFocusPainted(false);
    btnBack.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Back.gif")
    ));
    //btnBack.setText(Res.localize("Back"));
    btnBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        btnBack_actionPerformed(e);
      }
    });

    //btnForward.setBorder(BorderFactory.createEtchedBorder());
    btnForward.setFocusPainted(false);
    btnForward.setHorizontalTextPosition(SwingConstants.LEFT);
    btnForward.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Forward.gif")
    ));
    //btnForward.setText(Res.localize("Forward"));
    btnForward.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        btnForward_actionPerformed(e);
      }
    });

    //btnStop.setBorder(BorderFactory.createEtchedBorder());
    btnStop.setFocusPainted(false);
    btnStop.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Stop.gif")
    ));
    //btnStop.setText(Res.localize("Stop"));
    btnStop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        btnStop_actionPerformed(e);
      }
    });

    //btnRefresh.setBorder(BorderFactory.createEtchedBorder());
    btnRefresh.setFocusPainted(false);
    btnRefresh.setIcon(new ImageIcon(
      gr.cti.eslate.navigator.Navigator.class.getResource("images/Refresh.gif")
    ));
    //btnRefresh.setText(Res.localize("Refresh"));
    btnRefresh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        btnRefresh_actionPerformed(e);
      }
    });

    //this.setBorder(null);
    //this.setBorderPainted(false);
    this.setFloatable(false);

    //addressBar.setBorder(BorderFactory.createLineBorder(Color.black));
    //addressBar.setBorderPainted(false);
    addressBar.setFloatable(false);

    this.add(btnHome, null);
    this.add(strut1, null);
    this.add(btnBack, null);
    this.add(btnForward, null);
    this.add(strut2, null);
    this.add(btnStop, null);
    this.add(btnRefresh, null);    
    this.add(strut3, null);
    this.add(addressBar, null);
  }

  void btnHome_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.home();
    }
  }

  void btnBack_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.back();
    }
  }

  void btnForward_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.forward();
    }
  }

  void btnStop_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.stop();
    }
  }

  void btnRefresh_actionPerformed(ActionEvent e)
  {
    if (navigator != null) {
      navigator.refresh();
    }
  }

  void enableBack(boolean flag)
  {
    btnBack.setEnabled(flag);
  }

  void enableForward(boolean flag)
  {
    btnForward.setEnabled(flag);
  }

  void enableHome(boolean flag)
  {
    btnHome.setEnabled(flag);
  }
}
