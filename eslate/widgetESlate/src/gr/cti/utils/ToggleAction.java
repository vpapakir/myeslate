//Title:        ToggleAction
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  An Action for Toggling/Untoggling a button (On/Off)


package gr.cti.utils;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public abstract class ToggleAction extends AbstractAction
                                   implements ChangeListener
{

 protected boolean selected = false;

 public boolean isSelected() {
        return selected;
 }

 public synchronized void setSelected(boolean newValue) {
        boolean oldValue = this.selected;
//System.out.println("ToggleAction:setSelected("+newValue+") from selected="+oldValue);
        this.selected = newValue;
//don't add this: previous button stucks for a while when auto-depressed//      if (oldValue!=newValue) //13-1-1999: added this, still seems to fire twice though
   firePropertyChange("selected",
                           new Boolean(oldValue), new Boolean(newValue));
  if (oldValue!=newValue) //13-1-1999: seemed to fire twice when Action was placed in two containers
   act(selected); //12-1-1999: passing on "selected" state //13-1-1999: moved here
 }

 public abstract void act(boolean selected); //12-1-1999: added "boolean selected"

//////////////////////

 public void actionPerformed(ActionEvent e){
  //System.out.println("action");
 };

 public void stateChanged(ChangeEvent e){
  //System.out.println("ToggleAction:stateChanged");
  //Object o=e.getSource();
  setSelected(((AbstractButton)e.getSource()).getModel().isSelected());
 }

}