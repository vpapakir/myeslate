//Version: 19Apr2000

package gr.cti.utils;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.Box;

import java.awt.Component;
//import java.awt.event.ItemListener;
//import java.awt.event.ItemEvent;

public class FontToolBar extends JToolBar
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 
 private FontTypeComboBox fontTypeComboBox=new FontTypeComboBox();
 private FontSizeComboBox fontSizeComboBox=new FontSizeComboBox();
 private FontStyleToolbar fontStyleToolbar=new FontStyleToolbar();

 private Component separator1=Box.createHorizontalStrut(8); //4Apr2000: added fixed-height separators //19Apr2000: fixed-bug: now using fixed-width separators (HorizontalStrut), not fixed-height ones (VetricalStrut)
 private Component separator2=Box.createHorizontalStrut(8);
 private Component separator3=Box.createHorizontalStrut(8);

 public FontToolBar(){
  try{ jbInit(); }
  catch(Exception e){ e.printStackTrace(); }
 }

 public FontToolBar(JComponent customerComponent){
  this();
  setCustomer(customerComponent);
 }

 public void setCustomer(JComponent customerComponent){
  fontTypeComboBox.setCustomer(customerComponent);
  fontSizeComboBox.setCustomer(customerComponent);
  fontStyleToolbar.setCustomer(customerComponent);
 }

 public Dimension getMaximumHeight(){ //19Apr2000: ???
  Dimension size=getSize();
  return new Dimension(size.width,25);
 }

 private void jbInit(){ //19Apr2000: made editable in JBuilder's designer
    fontStyleToolbar.setAlignmentY((float) 0.5);
    fontStyleToolbar.setBorder(null);
  fontStyleToolbar.setFloatable(false); //23Jul1999

    add(fontTypeComboBox);
  add(separator1);
  add(fontSizeComboBox);
  add(separator2);
  add(fontStyleToolbar);
  add(separator3);
 }

}

