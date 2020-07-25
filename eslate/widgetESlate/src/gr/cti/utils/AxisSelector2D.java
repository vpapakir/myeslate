//??? need to implement Serializable? - check in container if saves/load axis setting //

//Copyright:    Copyright (c) 1998-2000
//Company:      Computer Technology Institute

/*
 28Sep1998 - first creation
 22Sep1999 - changed mouseClicked to mousePressed event hook to detect axis selections (cause mouse clicks aren't fired when user drags the mouse even a little bit when clicking)
 10Jan2000 - made the two axis displays ColoredStatePanels that are chilren of this JPanel and layout out when that one is layed out too
 28Mar2000 - fixed bugs, display now shows OK at any size and responds to mouse presses
           - by clicking to the dot which shows the axis system center, one can deselect any selected axis
           - made selectors a bit larger
           - made axis center selector not overlap with the X,Y selectors
 29Mar2000 - commented out the (static) main method (that method is used for testing)
 15Apr2000 - added serial-version
*/

package gr.cti.utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is a Swing component that shows two cartesian axis
 * and allows you to select one or none of them
 *
 * @version 2.0.0, 19-May-2006
 * @author George Birbilis [birbilis@cti.gr]
 */
public class AxisSelector2D
 extends JPanel
{

 static final long serialVersionUID = 15042000L; //15Apr2000: added serial-version

 public static final int NO_AXIS=0;
 public static final int X_AXIS=1;
 public static final int Y_AXIS=2;

 /**
  * The selectedAxis property name.
  */
 public static final String SELECTED_AXIS_PROPERTY = "selectedAxis"; //must start with "s", not "S" (similar to Swing's JComponent: "font", "maxiumumSize", "minimumSize" etc.)

 private int selectedAxis=NO_AXIS; //29-9-1998: initializing to NO_AXIS

 private static final Color[] colors={Color.red, Color.green};
 private ColoredStatePanel axisX=new ColoredStatePanel(colors),
                           axisY=new ColoredStatePanel(colors);
 private JPanel centerXY=new JPanel();

 /**
  * Constructs a new AxisSelector2D instance with default size (20x20 pixels)
  */
 public AxisSelector2D(){
  setLayout(null);
  setOpaque(false); //non-opaque to allow labels to be shown when underlapping with it
  setSize(20,20);
  setPreferredSize(new Dimension(20,20));
  setMaximumSize(new Dimension(20,20));
  setMinimumSize(new Dimension(20,20));
  //
  centerXY.setOpaque(true);
  centerXY.setBackground(Color.black);
  //  
  add(centerXY);
  add(axisX);
  add(axisY);
  //
  MouseHandler mh=new MouseHandler();
  centerXY.addMouseListener(mh);
  axisX.addMouseListener(mh);
  axisY.addMouseListener(mh);
  //doLayout();
}

 /**
  * Layout the panel
  */
 public void doLayout(){
  //System.out.println("doLayout");
  int w=getWidth();
  int h=getHeight();

  w=Math.min(w,h); //28Mar2000: axis are now always forming a square (have same width&height)
  h=w;

  int w10=w/3;
  int w90=w-w10;
  int h10=h/3;
  int h90=h-h10;

  axisX.setBounds(
   w10, //x
   h90, //y
   w90,   //width
   h10);  //height

  axisY.setBounds(
   0,    //x
   0,    //y
   w10,  //width
   h90); //height

  centerXY.setBounds( //28Mar: added a dot to show the axis center
   0,      //x
   h90,  //y
   w10,  //width
   h10); //height
 }

 /**
  * Set the selected axis
  *
  * @param axis one of NO_AXIS, X_AXIS, Y_AXIS
  */

 public void setSelectedAxis(int axis){
  if(axis!=this.selectedAxis){
   int old=this.selectedAxis;
   this.selectedAxis=axis;
   switch(axis){
    case NO_AXIS: axisX.setCurrentState(0); axisY.setCurrentState(0); break;
    case X_AXIS: axisX.setCurrentState(1); axisY.setCurrentState(0); break;
    case Y_AXIS: axisX.setCurrentState(0); axisY.setCurrentState(1); break;
   }
   //repaint();
   firePropertyChange(SELECTED_AXIS_PROPERTY, old, axis);
  }
 }

 /**
  * Get the selected axis
  *
  * @return one of NO_AXIS, X_AXIS, Y_AXIS
  */
 public int getSelectedAxis(){
  return selectedAxis;
 }

 /**
  * Toggle an axis between selected and non-selected
  * If some axis is selected and a new one gets selected,
  * the old one gets unselected (max number of selected is 1)
  *
  * @param axis one of NO_AXIS, X_AXIS, Y_AXIS
  */
 public void toggleAxis(int axis){
  if (selectedAxis==axis) setSelectedAxis(NO_AXIS);
  else setSelectedAxis(axis);
 }

 public class MouseHandler extends MouseAdapter{ //28Mar2000

  public void mousePressed(MouseEvent e){ //don't use MouseClicked, cause if the user drags the mouse a bit when clicking it doesn't get fired
   //System.out.println("mousePressed");
   Object source=e.getSource();
   toggleAxis((source==axisX)?X_AXIS:(source==axisY)?Y_AXIS:NO_AXIS); //if source=centerXY then deselect any selected axis
  }

 }

/*
 public static void main(String args[]){
  JComponentFrame.startJComponent("gr.cti.utils.AxisSelector2D","AxisSelector2D",new String[]{});
 }
*/

}

