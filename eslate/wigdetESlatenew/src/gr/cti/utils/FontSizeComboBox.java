package gr.cti.utils;

import java.awt.Font;
import java.awt.Color;
import javax.swing.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.Dimension;

public class FontSizeComboBox extends JComboBox
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

 public FontSizeComboBox(){
  this(selfgr,selbgr);
 }

 public Dimension getMaximumSize(){ //19Apr2000: setting the maximum height of the combobox
  Dimension size=getSize();
  return new Dimension(size.width,25); //max height=25 ////must have the same height value as the maximum height for the FontTypeComboBox's "getMaximumSize"
 }

 public FontSizeComboBox(Color selectionForeColor, Color selectionBackColor){
  addItemListener(this); //19Apr2000: register once as a listener to ourselves for item selection changes
  setEditable(true);
  setRenderer(new FontSizeCellRenderer(this,selectionForeColor,selectionBackColor));
  for(int i=10;i<=30;i++) addItem(Integer.toString(i));
  //setMinimumSize(null); //doesn't help: combos seems to have some minimum size depending on their contents
 }

 public FontSizeComboBox(JComponent customerComponent){
  this(customerComponent,selfgr,selbgr);
 }

 public FontSizeComboBox(JComponent customerComponent,Color selectionForeColor, Color selectionBackColor){
  this(selectionForeColor, selectionBackColor);
  setCustomer(customerComponent);
 }

 /////////////////

 public void setCustomer(JComponent customer){ //19Apr1999
  if(customerComponent!=null) customerComponent.removePropertyChangeListener(this); //19Apr2000: stop listening to any old customer
  customerComponent=customer;
  if(customer!=null) {
   setSelectedItem(Integer.toString(getCustomerFont().getSize()));
   customer.addPropertyChangeListener(/*"font",*/this); //give the "font" param? (no, doesn't work)
  }
 }

 /////////////////////

 public void itemStateChanged(ItemEvent e){
  if(customerComponent==null) return; //19Apr2000
  Font font=customerComponent.getFont();
  try{
   Font newFont=new Font(font.getName(),font.getStyle(),Integer.parseInt((String)e.getItem()));
   //if(newFont.getSize()!=font.getSize()){ //19Aug1999: to avoid stack-overflow errors  (doesn't work: added code to avoid spurious events)
   customerComponent.setFont(newFont);
   //customerComponent.repaint(); //redraw the component
   //}
  }catch(Exception ex){} //ignore if selected item is not a string
 }

 private boolean ignore=false;

 public void propertyChange(PropertyChangeEvent evt){ //19Aug1999: we know it's a font change event, cause we registered only for "font" property changes
  if(!ignore && evt.getPropertyName().equals("font")){
  //System.out.println("font change");
   Font f=(Font)evt.getNewValue();
   ignore=true;                    //avoid spurious events caused by setSelectedItem setting the font again
   setSelectedItem(Integer.toString(f.getSize()));
   ignore=false;
  }
 }

///////////////////////////////////////////////////

 private JComponent customerComponent;

 private static final Color selfgr=Color.white;
 private static final Color selbgr=Color.blue;

}

