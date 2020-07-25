//Title:        VetoableCheckBox
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  allows vetoable listeners to veto against a state change

package gr.cti.utils;

import javax.swing.*;

public class VetoableCheckBox extends JCheckBox
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public VetoableCheckBox(){} //provide an empty constructor

 public VetoableCheckBox(boolean selected){
  super("",selected); //there's no JCheckBox(boolean) constructor
 }

 /**
   * Sets the selected state of the button.
   * @param b true selects the check box,
   *          false deselects the check box.
   */
 public void setSelected(boolean b) {
   boolean old=getModel().isSelected();
   if(old!=b)
    try{
    fireVetoableChange("selected",new Boolean(old),new Boolean(b));  //birb
    super.setSelected(b);
   }catch(java.beans.PropertyVetoException e){} //don't change the "selected" state if we got a veto for the proposed change
 }

}
