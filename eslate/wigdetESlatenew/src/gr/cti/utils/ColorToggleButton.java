//Title:        utilsBirb
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

public class ColorToggleButton extends JToggleButton
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public static final String COLOR_PROPERTY = "color";

 public ColorToggleButton() {
  addMouseListener(
   new MouseAdapter(){
    public void mousePressed(MouseEvent e){
     if(SwingUtilities.isRightMouseButton(e)) editColor();
    }
   });
 }

 public ColorToggleButton(Color newColor){
  this();
  setColor(newColor);
 }

 //

 public Color getColor(){
  return getBackground();
 }

 public void setColor(Color newColor){
  if(newColor==null) return; //don't allow null color
  Color oldColor=getColor();
  setBackground(newColor);
  //repaint();
  firePropertyChange(COLOR_PROPERTY,oldColor,newColor);
 }

 //

 public void editColor(){
  setColor(JColorChooser.showDialog(this, "Change Color", getColor()));
 }

}