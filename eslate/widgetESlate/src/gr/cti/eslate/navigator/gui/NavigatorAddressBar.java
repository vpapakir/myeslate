//Title:        E-Slate
//Version:      24Jun2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      Computer Technology Institute
//Description:  Navigator (web browser etc.) component for E-Slate

package gr.cti.eslate.navigator.gui;

import java.awt.event.*;
import javax.swing.*;

import gr.cti.eslate.navigator.models.INavigator;
import gr.cti.eslate.navigator.Res;

public class NavigatorAddressBar extends JToolBar
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JTextField edAddress = new JTextField();
  private JButton btnGo = new JButton();
  private INavigator navigator;

  public void setAddressText(String text){ //15Mar2000
   edAddress.setText(text);
  }

  public void setNavigator(INavigator nav){
   navigator=nav;
  }

  public NavigatorAddressBar() {
    try  {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    //btnGo.setBorder(BorderFactory.createEtchedBorder());
    btnGo.setIcon(new ImageIcon(gr.cti.eslate.navigator.Navigator.class.getResource("images/Go!.gif")));
    btnGo.setText(Res.localize("Go!"));
    btnGo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnGo_actionPerformed(e);
      }
    });

    edAddress.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        edAddress_actionPerformed(e);
      }
    });

    //this.setBorder(null);
    this.add(edAddress, null);
    this.add(btnGo, null);
  }

  private void btnGo_actionPerformed(ActionEvent e) {
   navigateToGivenAddress();
  }

  private void edAddress_actionPerformed(ActionEvent e) { //14Mar2000
   navigateToGivenAddress();
  }

  private void navigateToGivenAddress()  //14Mar2000
  {
    if (navigator != null) {
      String addr = edAddress.getText();
      if (addr.indexOf(':') < 0) {
        addr = "http://" + addr;
      }
      try{
        navigator.setCurrentLocation(addr);
      }catch(Exception ex){
        //don't clear the address field on exception, the user might want to edit it in order to fix it and not type it all again from the beginning
      }
    }
  }

}
