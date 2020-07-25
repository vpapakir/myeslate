package gr.cti.utils;

import java.awt.Font;
import java.awt.Color;
import javax.swing.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class FontTypeComboBox extends JComboBox
                              implements ItemListener,
                                         PropertyChangeListener //19Aug1999: now listening for "font" changes of our customer component
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public Font getCustomerFont(){
  return (customerComponent!=null)?customerComponent.getFont():null;
 }

 public FontTypeComboBox(){
  this(selfgr,selbgr);
 }

 public FontTypeComboBox(Color selectionForeColor, Color selectionBackColor){
  addItemListener(this);
  setRenderer(new FontTypeCellRenderer(this,selectionForeColor,selectionBackColor));
  addItem("Helvetica");
  addItem("Dialog");
  addItem("DialogInput");
  addItem("SansSerif");
  addItem("Serif");
  addItem("Monospaced");
 }

 public FontTypeComboBox(JComponent customerComponent){
  this(customerComponent,selfgr,selbgr);
 }

 public FontTypeComboBox(JComponent customerComponent,Color selectionForeColor, Color selectionBackColor){
  this(selectionForeColor, selectionBackColor);
  setCustomer(customerComponent);
 }

/*
 public Dimension getMaximumSize(){ //19Apr2000: setting the maximum height of the combobox
  Dimension size=getSize();
  return new Dimension(size.width,25); //max height=25 //must have the same height value as the maximum height for the FontSizeComboBox's "getMaximumSize"
 }
*/

 ////////////////////

  public void setCustomer(JComponent customer){
  if(customerComponent!=null) customerComponent.removePropertyChangeListener(this); //19Apr2000: stop listening to any old customer
  customerComponent=customer;
  if(customer!=null){
   setSelectedItem(getCustomerFont().getFamily()); //19Aug1999
   customer.addPropertyChangeListener(/*"font",*/this); //give the "font" param? (no, doesn't work)
  }
 }

 ///////////////////

 private boolean ignore=false; //can't edit a font without making a new one

 public void itemStateChanged(ItemEvent e){
  if(customerComponent==null) return; //19Apr2000

   Font font=customerComponent.getFont(); //21Jun1999: fix: get customer's font, not our font
   try{
    Font newFont=new Font((String)e.getItem(),font.getStyle(),font.getSize());
    //if(!font.getFamily().equals(newFont.getFamily())){ //19Aug1999: to avoid stack-overflow errors (doesn't work: added code to avoid spurious events)
     ignore=true;
     customerComponent.setFont(newFont);
     ignore=false;
     //setFont(getCustomerFont()); //(should set same Font, but use default size);
     customerComponent.repaint(); //redraw the customer component
     //repaint(); //refresh the combo box
    //}
   }catch(Exception ex){} //ignore if selected item is not a string
 }

 public void propertyChange(PropertyChangeEvent evt){ //19Aug1999: we know it's a font change event, cause we registered only for "font" property changes
  if(!ignore && evt.getPropertyName().equals("font")){
  //System.out.println("font change");
   Font f=(Font)evt.getNewValue();
   ignore=true;                    //avoid spurious events caused by setSelectedItem setting the font again
   setSelectedItem(f.getFamily());
   ignore=false;
  }
 }

 //////////////////////////

 private JComponent customerComponent;

 private static final Color selfgr=Color.white;
 private static final Color selbgr=Color.blue;

}

