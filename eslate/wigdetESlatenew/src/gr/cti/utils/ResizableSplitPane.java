//Title:        ResizableSplitPane
//Version:      1.0 (22-6-1999)
//Copyright:    Copyright (c) 1999 George Birbilis / CTI
//Author:       George Birbilis
//Company:      CTI
//Description:  a split pane that keeps the view ratio of its two components when resized

package gr.cti.utils;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

//////////////////////////////////////////////////////////////////////////////////////////

public class ResizableSplitPane extends JSplitPane
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private double ratio=2d/3d;
 //private boolean userRatio=true; //first time setup the hard-coded "ratio"

 public void setRatio(double ratio){ //19Aug1999
  this.ratio=ratio;
  //userRatio=true;
  forceRatio();
 }

 public double getRatio(){ //19Aug1999
  return ratio;
 }

 public class ratioforcer implements Runnable{ //3Sep1999
  public void run(){
   //setDividerLocation(300); //???
   forceRatio(); //retry to force ratio
  }
 }

 public void forceRatio(){ //19Aug1999
  //System.out.println("ResizableSplitPane::forceRatio, ratio="+ratio);
  //Trace.printStackTrace();

  //setDividerLocation(ratio); //the setDividerLocation accepts a double (ratio) or an int (position)
  if(getHeight()>=0 && getWidth()>=0) setDividerLocation(ratio); else{
   SwingUtilities.invokeLater(new ratioforcer()); //3Sep1999: retry to force ratio later
   //userRatio=true; //retry with this "ratio" value at next setBounds
  }
 }

 public double calcCurrentRatio(){ //19Aug1999: moved here from "setBounds"
  int size=-getDividerSize();
  if (getOrientation() == VERTICAL_SPLIT) size+=getHeight();
  else size+=getWidth();
  return(size>=0)?((double)getDividerLocation()/(double)size):-1; //give -1 for non-calculatable-ratio
 }

 public double getValidCurrentRatio(){
  //Trace.printStackTrace();
  double r=calcCurrentRatio();
  return(r>=0)?r:ratio; //don't use r!=0, user r>=0, cause negative ratios can't be accepted //changed :0.5 to :ratio (assuming "ratio" has valid values from the start)
 }

 /////////////

/*
 public void setBounds(int x, int y, int width, int height) { //before resize calc the current divider ratio...
  //System.out.println("ResizableSplitPane::setBounds");
  if(userRatio)
   userRatio=false; //user ratio command action effective just for the next resize
  else //if user has recently set a ratio via a command, then use that one
   ratio=getValidCurrentRatio();
  super.setBounds(x,y,width,height);
  //don't call the forceRatio proc from in here, wait to call it at processComponentEvent
 }
*/

 protected void processComponentEvent(ComponentEvent e){ //...after resize, enforce the old divider ratio
  //System.out.println("ResizableSplitPane::processComponentEvent");
  setDividerLocation(300);
  //forceRatio();
 }

// CONSTRUCTORS ////////////////////////////////////////////////////////////////

    public ResizableSplitPane() {
     super();
     enableEvents(ComponentEvent.COMPONENT_RESIZED);
    }

    public ResizableSplitPane(int newOrientation) {
     super(newOrientation);
     enableEvents(ComponentEvent.COMPONENT_RESIZED);
    }

    public ResizableSplitPane(int newOrientation, boolean newContinuousLayout) {
     super(newOrientation, newContinuousLayout);
     enableEvents(ComponentEvent.COMPONENT_RESIZED);
    }

    public ResizableSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent){
     super(newOrientation, newLeftComponent, newRightComponent);
     enableEvents(ComponentEvent.COMPONENT_RESIZED);
    }

    public ResizableSplitPane(int newOrientation, boolean newContinuousLayout,Component newLeftComponent, Component newRightComponent){
     super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
     enableEvents(ComponentEvent.COMPONENT_RESIZED);
    }

}
