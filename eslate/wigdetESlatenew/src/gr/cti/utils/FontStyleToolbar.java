//Version: 19Apr2000

package gr.cti.utils;

import java.awt.Font;
import javax.swing.*;

//import java.awt.event.ItemListener;
//import java.awt.event.ItemEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class FontStyleToolbar
 extends MyToolBar
 implements PropertyChangeListener //19Apr2000: listen for font changes
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public FontStyleToolbar(){
  try{ jbInit(); }
  catch(Exception e){ e.printStackTrace(); }
 }

 public FontStyleToolbar(JComponent customerComponent){
  this();
  setCustomer(customerComponent);
 }

 public void setCustomer(JComponent customer){
  if(customerComponent!=null) customerComponent.removePropertyChangeListener(this); //19Apr2000: stop listening to any old customer
  customerComponent=customer;
  if(customer!=null){
   syncDisplayWithFont(customer.getFont()); //19Apr2000: display the current font's style
   customer.addPropertyChangeListener(/*"font",*/this); //give the "font" param? (no, doesn't work)
  }
 }

 private boolean ignore=false; //can't edit a font without making a new one

 private void syncDisplayWithFont(Font f){
  setCustomerFontStyle(Font.BOLD,f.isBold());
  setCustomerFontStyle(Font.ITALIC,f.isItalic());
 }

 public void propertyChange(PropertyChangeEvent evt){ //19Aug1999: we know it's a font change event, cause we registered only for "font" property changes
  if(!ignore && evt.getPropertyName().equals("font")){
  //System.out.println("font change");
   Font f=(Font)evt.getNewValue();
   ignore=true;                    //avoid spurious events caused when setting the font again
   syncDisplayWithFont(f);
   ignore=false;
  }
 }

 private void jbInit(){
  add(toggleAction_BOLD);
  add(toggleAction_ITALIC);
  add(Box.createHorizontalStrut(8)); //19Apr2000: added some end padding
 }

//ACTIONS/////////////////////////////////////////////////////////////////////////////////

 private void setupAction(AbstractAction a,String name){
  String text=name; //localize(name);
  //System.out.println(name+"->"+text);
  a.putValue(AbstractAction.NAME,text);
  //putValue(SHORT_DESCRIPTION,text); //?
  //putValue(LONG_DESCRIPTION,text); //?
  ImageIcon icon=loadImageIcon("images/"+name+".gif",text);
  if (icon!=null) a.putValue(AbstractAction.SMALL_ICON,icon); //check for null else will throw exception
 }

 ////////////

 abstract class MyToggleAction extends ToggleAction{
  public MyToggleAction(String name){
   setupAction(this,name); //this localizes the name too
  }
 }

 ///////

 MyToggleAction
  toggleAction_BOLD=new MyToggleAction("Bold")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   
   public void act(boolean selected){
    setCustomerFontStyle(Font.BOLD,selected);
   }
  },
  toggleAction_ITALIC=new MyToggleAction("Italic")
  {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
   
   public void act(boolean selected){
    setCustomerFontStyle(Font.ITALIC,selected);
   }
  };

/////////////

 public void setCustomerFontStyle(int style,boolean selected){
  if(customerComponent!=null){ //04Apr2000
   Font curFont=customerComponent.getFont();
   int curStyle=curFont.getStyle();
   if(selected) curStyle|=style; else curStyle&=(~style); //23Jul1999:fixed-bug:was using &= instead of |= when we wanted to add a style flag to the current style
   customerComponent.setFont(new Font(curFont.getName(),curStyle,curFont.getSize())); //23Jul1999:fixed-bug:was using "style" var instead of "curStyle"
  }
 }

 private JComponent customerComponent;

////////

  public ImageIcon loadImageIcon(String filename, String description) { //got from Canvas
   try{
    java.net.URL u=this.getClass().getResource(filename);
    //System.out.println(u);
    if (u!=null) return new ImageIcon(u,description);
    else return null;
   }catch(Exception e){return null;} //18-12-1998: catching Exception thrown by the MS-JVM when filename not found in .jar
  }

}

