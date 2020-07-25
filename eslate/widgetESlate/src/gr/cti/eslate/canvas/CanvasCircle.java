package gr.cti.eslate.canvas;

import java.awt.event.*;
import javax.swing.*;

public class CanvasCircle extends CustomCanvasTool{
 int cx=0, cy=0; //initial center for right clicks
 int rx=0,ry=0;

public String getToolName(){return "CIRCLE";};

public void mousePressed(MouseEvent e){ //MouseListener//
// System.out.println("circle:MousePressed");
 if (SwingUtilities.isRightMouseButton(e))
  {cx=gx.getX(); cy=gx.getY();} //get last circle's center (or last point as center)
 else {cx=e.getX(); cy=e.getY();} //get current point as center
 gx.setXOR();
 rx=e.getX(); ry=e.getY(); //keep these: Graphix keeps as last point of a circle it's center
 gx.Circle(cx,cy,rx,ry);
 c.repaint();
}

public void mouseReleased(MouseEvent e){ //MouseListener//
// System.out.println("MouseReleased");
 gx.setPAINT();
 gx.Circle(cx,cy,rx,ry); //just draw the last XOR circle with its final color
 //ignore e.getX(),e.getY() - last circle was drawn with (rx,ry) for sure
 DidSomeChange(); 
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
// System.out.println("MouseDragged");
 gx.setXOR();
 gx.Circle(cx,cy,rx,ry); //erase old circle (redraw it in XOR mode)
 rx=e.getX(); ry=e.getY();
 gx.Circle(cx,cy,rx,ry); //draw new circle
 c.repaint(); 
}
    
}