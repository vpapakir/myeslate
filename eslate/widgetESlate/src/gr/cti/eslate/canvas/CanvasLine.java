package gr.cti.eslate.canvas;

import java.awt.event.*;
import javax.swing.*;

public class CanvasLine extends CustomCanvasTool{
 boolean oddClick=false; //zero clicks at start
 int x0,y0; //for single clicks
 int x1,y1; //used by MousePressed/MouseReleased/MouseDragged

public String getToolName(){return "LINE";};

/*
public CanvasLine(Component c,Graphix gx){
 super(c,gx); 
 names=new names[4];
 values=new values[4];
 names[0]="X1"; names[1]="Y1"; names[2]="X2"; names[3]="Y2";
}
*/

//this has a problem is user drags the mouse at start of lines (missing clicks)
public void mouseClicked(MouseEvent e){ //MouseListener//
// System.out.println("MouseClicked");
 if (!clickingEnabled()) return; //fixes a bug
 if (SwingUtilities.isLeftMouseButton(e)) {
  oddClick=!oddClick;
  if (oddClick) {x0=e.getX(); y0=e.getY();}
  else {
   gx.setPAINT();
   gx.Line(x0,y0,e.getX(),e.getY());
   DidSomeChange(); //15-8-1998: DidSomeChange now repaints
  }
 }
}

public void mousePressed(MouseEvent e){ //MouseListener//
// System.out.println("MousePressed");
 super.mousePressed(e);  //fixes a bug
 if (SwingUtilities.isRightMouseButton(e)) {x1=gx.getX(); y1=gx.getY();}
 else {x1=e.getX(); y1=e.getY();}
 gx.setXOR();
 gx.Line(x1,y1,e.getX(),e.getY());
 c.repaint(); //not using DidSomeChange, cause line hasn't been finished yet and is still changing
}

public void mouseReleased(MouseEvent e){ //MouseListener//
// System.out.println("MouseReleased");
 gx.setPAINT();
 gx.Line(x1,y1,e.getX(),e.getY()); //just draw the last XOR line with its final color
 DidSomeChange(); //15-8-1998: DidSomeChange now repaints
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
// System.out.println("MouseDragged");
 super.mouseDragged(e); //fixes a bug
 if (SwingUtilities.isLeftMouseButton(e) && oddClick) //fix for when user drags the second point after a single Left click
  {oddClick=false; gx.setPAINT(); gx.Line(x0,y0,e.getX(),e.getY());}
 gx.setXOR();
 gx.Line(x1,y1,gx.getX(),gx.getY()); //erase old line (redraw it in XOR mode)
 gx.Line(x1,y1,e.getX(),e.getY()); //draw new line
 c.repaint(); //not using DidSomeChange, cause line hasn't been finished yet and is still changing
}
    
}