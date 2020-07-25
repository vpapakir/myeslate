//Title:        utilsBirb
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JColorChooser;

public class ColorButton extends JButton implements ActionListener
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public static final String COLOR_PROPERTY = "color";

 private Color color=null; //default=transparent

 public ColorButton() {
  addActionListener(this);
 }

 public ColorButton(Color newColor){
  this();
  setColor(newColor);
 }

 //

 public Color getColor(){
  return color;
 }

 public void setColor(Color newColor){
  if(color!=null && color.equals(newColor)) return;
  Color oldColor=color;
  if(newColor==null) setOpaque(false);
  else {
   color=newColor;
   setBackground(newColor);
   if(oldColor==null) setOpaque(true); //if was transparent, then make new color visible
  }
  //repaint();
  firePropertyChange(COLOR_PROPERTY,oldColor,newColor);
 }

 //

 public void actionPerformed(ActionEvent e){
  setColor(JColorChooser.showDialog(this, "Change Color", getColor()));
 }

}
