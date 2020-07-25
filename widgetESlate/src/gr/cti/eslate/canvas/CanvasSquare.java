package gr.cti.eslate.canvas;

//bugs when clicking both buttons (XOR mask trails are left)

import java.awt.event.*;
import javax.swing.*;

public class CanvasSquare extends CustomCanvasTool{
 boolean oddClick=false; //zero clicks at start
 int x0,y0; //for single clicks
 int x1,y1; //used by MousePressed/MouseReleased/MouseDragged

public String getToolName(){return "SQUARE";};

//this has a problem is user drags the mouse at start of lines (missing clicks)
public void mouseClicked(MouseEvent e){ //MouseListener//
//System.out.println("Square:MouseClicked");
 if (!clickingEnabled()) return;
 if (SwingUtilities.isLeftMouseButton(e)) {
  oddClick=!oddClick;
  if (oddClick) {x0=e.getX(); y0=e.getY();}
  else {
   gx.setPAINT();
   gx.Square(x0,y0,e.getX(),e.getY());
   DidSomeChange();
  }
 }
}

public void mousePressed(MouseEvent e){ //MouseListener//
 super.mousePressed(e);
//System.out.println("Square:MousePressed");
 if (SwingUtilities.isRightMouseButton(e)) {x1=gx.getX(); y1=gx.getY();}
 //right mouse click starts from last graphix point (maybe drawn by the previous tool)
 else {x1=e.getX(); y1=e.getY();}
 gx.setXOR();
 gx.Square(x1,y1,e.getX(),e.getY());
 c.repaint();
}

public void mouseReleased(MouseEvent e){ //MouseListener//
//System.out.println("Square:MouseReleased");
 gx.setPAINT();
 gx.Square(x1,y1,e.getX(),e.getY()); //just draw the last XOR line with its final color
 DidSomeChange();
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
//System.out.println("Square:MouseDragged");
 super.mouseDragged(e); //fixes a bug
 if (SwingUtilities.isLeftMouseButton(e) && oddClick) //fix for when user drags the second point after a single Left click
  {oddClick=false; gx.setPAINT(); gx.Square(x0,y0,e.getX(),e.getY());}
 else{ //?
  gx.setXOR();
  gx.Square(x1,y1,gx.getX(),gx.getY()); //erase old line (redraw it in XOR mode)
  gx.Square(x1,y1,e.getX(),e.getY()); //draw new line
 }
 c.repaint(); 
}
    
}