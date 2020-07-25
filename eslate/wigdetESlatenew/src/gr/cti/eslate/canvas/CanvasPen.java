package gr.cti.eslate.canvas;

import java.awt.event.*;
import javax.swing.*;

public class CanvasPen extends CustomCanvasTool{

 public String getToolName(){return "PEN";}

 public int Width=3;

 public void mousePressed(MouseEvent e){ //MouseListener//
  if (SwingUtilities.isRightMouseButton(e))
   gx.LineTo(e.getX(),e.getY()); //continue old path (if first click make a new one)
  else gx.Point(e.getX(),e.getY()); //start new path
  DidSomeChange(); //15-8-1998 
 }

 public void mouseDragged(MouseEvent e){ //MouseMotionListener//
  gx.LineTo(e.getX(),e.getY()); //continue drawing-path (if new one it is started)
  DidSomeChange(); //15-8-1998
 }
    
}