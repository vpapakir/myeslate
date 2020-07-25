package gr.cti.eslate.canvas;

//bugs when clicking both buttons (XOR mask trails are left)

import java.awt.event.*;
import javax.swing.*;

public class CanvasOval extends CustomCanvasTool{
 boolean oddClick=false; //zero clicks at start
 int x0,y0; //for single clicks
 int x1,y1; //used by MousePressed/MouseReleased/MouseDragged

public String getToolName(){return "OVAL";};

//this has a problem is user drags the mouse at start of lines (missing clicks)
public void mouseClicked(MouseEvent e){ //MouseListener//
//System.out.println("oval:MouseClicked");
 if (!clickingEnabled()) return;  
 if (SwingUtilities.isLeftMouseButton(e)) {
  oddClick=!oddClick;
  if (oddClick) {x0=e.getX(); y0=e.getY();}
  else {
   gx.setPAINT();
   gx.Oval(x0,y0,e.getX(),e.getY());
   DidSomeChange();    
  }
 }
}

public void mousePressed(MouseEvent e){ //MouseListener//
 super.mousePressed(e);
//System.out.println("oval:MousePressed");
 if (SwingUtilities.isRightMouseButton(e)) {x1=gx.getX(); y1=gx.getY();}
 //right mouse click starts from last graphix point (maybe drawn by the previous tool)
 else {x1=e.getX(); y1=e.getY();}
 gx.setXOR();
 gx.Oval(x1,y1,e.getX(),e.getY());
 c.repaint();
}

public void mouseReleased(MouseEvent e){ //MouseListener//
//System.out.println("oval:MouseReleased");
 gx.setPAINT();
 gx.Oval(x1,y1,e.getX(),e.getY()); //just draw the last XOR line with its final color
 DidSomeChange();
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
//System.out.println("oval:MouseDragged");
 super.mouseDragged(e); //fixes a bug
 if (SwingUtilities.isLeftMouseButton(e) && oddClick) //fix for when user drags the second point after a single Left click
  {oddClick=false; gx.setPAINT(); gx.Oval(x0,y0,e.getX(),e.getY());} //17-12-1998: fixed-bug: the oval tool was drawing a rectangle in a rare case!
 else{ //?
  gx.setXOR();
  gx.Oval(x1,y1,gx.getX(),gx.getY()); //erase old line (redraw it in XOR mode)
  gx.Oval(x1,y1,e.getX(),e.getY()); //draw new line
 }
 c.repaint();
}

}