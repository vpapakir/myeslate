//@20-10-1998: moved most code to Graphix

package gr.cti.eslate.canvas;

import java.awt.event.*;

public class CanvasSpray extends CustomCanvasTool{
    
private double density;

public CanvasSpray(){
 super();
 this.density=1/2d;
}

public CanvasSpray(double density){
 super();
 this.density=density;
}

public String getToolName(){return "SPRAY";};

public void mousePressed(MouseEvent e){ //MouseListener//
 gx.density=density;
 gx.Spray(e.getX(),e.getY());
 DidSomeChange(); //15-8-1998
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
 gx.density=density;
 gx.Spray(e.getX(),e.getY());
 DidSomeChange(); //15-8-1998
}

}
