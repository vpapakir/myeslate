//Title:        utilsBirb
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.event.*;
import java.beans.*;

public class RememberActionHandler implements ActionListener, //for AbstractButton
                                              PropertyChangeListener //for Action
{
 private Object lastSource;

 RememberActionHandler(){
  super();
 }

 //

 public void actionPerformed(ActionEvent e) {
  lastSource=e.getSource();
  //System.out.println("RemembererActionHandler: new lastSource="+lastSource);
 }

 public void propertyChange(PropertyChangeEvent e){
  lastSource=e.getSource();
 }

 //

 public Object getLastSource(){
  return lastSource;
 }

}
