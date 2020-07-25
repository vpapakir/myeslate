//Title:        utilsBirb
//Version:      2Dec1999
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import javax.swing.Icon;
import java.awt.event.ActionEvent;

public class DummyAction extends javax.swing.AbstractAction
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 
 public DummyAction(){
  super();
 }

 public DummyAction(String name){
  super(name);
 }

 public DummyAction(String name, Icon icon) {
  super(name,icon);
 }

 public void actionPerformed(ActionEvent e){} //do nothing

}