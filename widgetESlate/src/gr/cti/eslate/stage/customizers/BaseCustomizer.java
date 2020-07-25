//Title:        Stage/BaseCustomizer
//Version:      17May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Customizer pages' ancestor

package gr.cti.eslate.stage.customizers;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

public abstract class BaseCustomizer
 extends JPanel
 implements ICustomizer, SwingConstants
{

 private ICustomizer parent;

 public void setParent(ICustomizer parent){
  this.parent=parent;
 }

 public void updateParent(){
  if(parent!=null) parent.setupWidgetsFromObject(); //don't call "parent.setObject(getObject())" instead: it would dump the saved state and keep the object's state as the saved state
 }

} 
