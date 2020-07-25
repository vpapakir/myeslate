//Title:        utilsBirb
//Version:      28Jan2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.*;
import javax.swing.*;

public class ColoredStatePanel extends JPanel
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 //FIELDs//

 private Color[] colors;
 private int state;

 //CONSTRUCTORs//

 public ColoredStatePanel() {
  setOpaque(true);
  setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
 }

 public ColoredStatePanel(Color[] stateColors) {
  this();
  colors=stateColors;
  updateDisplay();
 }

 public void updateDisplay(){
  setBackground(colors[getCurrentState()]);
  repaint();
 }

 //PROPERTIES//

 public static final String CURRENT_STATE_PROPERTY = "currentState";

 //StateCount property//

 public int getStateCount(){
  return colors.length;
 }

 //CurrentState property//

 public int getCurrentState(){
  return state;
 }

 public void setCurrentState(int newState) throws IndexOutOfBoundsException {
  int oldState=getCurrentState();
  if(oldState!=newState){
   state=newState;
   updateDisplay();   
   firePropertyChange(CURRENT_STATE_PROPERTY,oldState,newState);
  }
 }

 //Color[i] property//

 public void setColor(Color c, int index) throws IndexOutOfBoundsException {
  colors[index]=c;
  if (getCurrentState()==index) repaint();
 }

 public Color getColor(int index) throws IndexOutOfBoundsException {
  return colors[index];
 }

 //VERBS//

 public void selectPreviousState(){
  setCurrentState( (getCurrentState()-1) % getStateCount() );
 }

 public void selectNextState(){
  setCurrentState( (getCurrentState()+1) % getStateCount() );
 }

}