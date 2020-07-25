//Title:        Selection utility
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  Add this to a JComponent in order to have a selector tool

package gr.cti.utils;

import java.awt.*;
import java.util.*;
import javax.swing.*;

//////////////////////////////////////////////////////////////////////////////////////////

public class Selector extends JComponent
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 /**
  * Make a new selection
  */
 public Selector() { }

 /**
  * Make a new selection for the provided area
  */
 public Selector(int x,int y,int width,int height){
  super();
  setBounds(x,y,width,height);
 }

 public void setArea(int x1,int y1, int x2,int y2){ //1Mar1999
  setStartingPoint(x1,y1);
  setEndingPoint(x2,y2);
 }

 public void setStartingPoint(int x,int y){ //1Mar1999
  start=new Point(x,y);
  if (end==null) end=start; //initally a zero sized selection
  setBounds(Graphix.xyxy2rect(x,y,end.x,end.y));
 }

 /**
  * Set the starting point
  */
 public void setEndingPoint(int x,int y){ //1Mar1999
  end=new Point(x,y);
  if (start==null) start=end; //initally a zero sized selection
  setBounds(Graphix.xyxy2rect(start.x,start.y,x,y));
 }

 /**
  * Get the components that are inside the selection (or intersected by it) in a Vector
  */
 @SuppressWarnings(value={"unchecked"})
 public Vector getSelectedComponents(){
  Vector selection=new Vector();
  Container p=getParent();
  if (p!=null){ //when the selector is not added to some parent it returns an empty vector (so to disable a selection, just remove from parent, to enable again add it again)
   Component[] children=p.getComponents();
   //if (children!=null) //not needed, we're a child as well, so it's always #children>=1
   for (int i=0;i<children.length;i++){
    Component c=children[i];
    if (!(c instanceof Selector) && getBounds().intersects(c.getBounds())){
     //System.out.println(c.getName());
     selection.addElement(c);
    }
   }
  }
  return selection;
 }

 /**
  * Paint the Component
  */
 public void paintComponent(Graphics g){
  g=getComponentGraphics(g); //it will set our fg and bg colors
  g.drawRect(0,0,getWidth()-1,getHeight()-1);
 }

 /**
  * Select All
  */
 public void selectAll(){ //1Mar1999
  setBounds(calculateBrethremBoundingBox()); //make large enough and at appropriate location to contain all other components in our container
 }                                           //couldn't do a setBounds(getParent().getSize()), cause some brothers may be at negative offsets in our parent etc.

 private Rectangle calculateBrethremBoundingBox(){ //1Mar1999: calculate bounding box containing all brothers
  Rectangle bounds=new Rectangle();
  Container p=getParent();
  if (p!=null)
   for(int i=p.getComponentCount();i-->0;){ //optimized loop checking
    Component c=p.getComponent(i);
    if (!(c instanceof Selector)) //don't calc in our bounds too
     bounds.add(c.getBounds());
   }
  return bounds;
 }

 ////////

 private Point start,end;

}
