package gr.cti.eslate.canvas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CanvasRubber extends CustomCanvasTool{

//public CanvasRubber(){super();}
//public CanvasRubber(int width){super();gx.rubberSize=width;}
public String getToolName(){return "RUBBER";};

int oldWidth;
Color oldColor;
public void start(){
 oldWidth=gx.getPenSize();
 oldColor=gx.fgrColor;
 gx.setPenSize(gx.getRubberSize());
 gx.setColor(gx.bkgrColor);
}
public void end(){
 gx.setColor(oldColor);
 gx.setPenSize(oldWidth);
}

public void mousePressed(MouseEvent e){ //MouseListener//
 start();
 if (SwingUtilities.isRightMouseButton(e))
  gx.LineTo(e.getX(),e.getY()); //continue old path (if first click make a new one)
 else gx.Point(e.getX(),e.getY()); //start new path
 DidSomeChange(); //15-8-1998
 end();
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
 start();
 gx.LineTo(e.getX(),e.getY()); //continue drawing-path (if new one it is started)
 DidSomeChange(); //15-8-1998
 end();
}

}
