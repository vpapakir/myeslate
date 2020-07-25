//Copyright:    Copyright (c) 1999
//Company:      Computer Technology Institute

package gr.cti.utils; //11-2-1999: changed from gr.cti.birbilis package

import java.awt.*;
import java.awt.event.*;
//import javax.swing.JComponent;

//////////////////////////////////////////////////////////////////////////////////////////

/**
 * A Dragger utility
 * <br>Doing a "new gr.cti.utils.Dragger(someComponent)" allows you to drag it around in its parent-container
 * <br>There is no need to keep a reference to the dragger object:
 * <br>it is registering as a MouseListener & MouseMotionListener of its master component, so it's kept alive by its master
 *
 * @version 2.0.0, 19-May-2006
 * @author George Birbilis [birbilis@cti.gr]
 */
public class Dragger extends MouseAdapter //extending MouseAdapter to write less code
                     implements MouseMotionListener
{

//////////////////////////////////////////////////////////////////////////////////////////

 /**
  * Make a new Dragger for a specific component
  * <br>There is no need to keep a reference to the dragger object:
  * <br>it is registering as a MouseListener & MouseMotionListener of its master component, so it's kept alive by its master
  *
  * @param c the component to attatch the new Dragger to
  */
 public Dragger(Component c) {
  this.c=c;
  c.addMouseListener(this);
  c.addMouseMotionListener(this);
 }

//////////////////////////////////////////////////////////////////////////////////////////

 //MouseAdapter//

 public void mousePressed(MouseEvent e){
  offsx=e.getX();
  offsy=e.getY();
 }

 //public void mouseReleased(MouseEvent e){} //don't do anything, redrawing would be doing it twice, since the last mouse drag point will almost always be the same or near the mouse release point

//////////////////////////////////////////////////////////////////////////////////////////

 //MouseMotionListener//
 public void mouseDragged(MouseEvent e){ //17-2-1999: optimized repainting
  /*J*/Component parent=/*(JComponent)*/c.getParent();
  Rectangle curBounds=c.getBounds();
  int x=curBounds.x;
  int y=curBounds.y;
  int width=curBounds.width;
  int height=curBounds.height;
  synchronized(parent.getTreeLock()){ //do all the following in one drawing operation
   parent.repaint(x,y,width,height); //clear the old area
   x+=e.getX()-offsx;
   y+=e.getY()-offsy;
   c.setLocation(x,y);
   parent.repaint(x,y,width,height); //draw the new area
  }
 }

 public void mouseMoved(MouseEvent e){}

 /**
  *  PRIVATE FIELDS
  */
 private int offsx,offsy;
 private Component c;

}
