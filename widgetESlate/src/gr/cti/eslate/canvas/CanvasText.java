//@16-7-1998: new behaviour - now user can draw (&move to place) text with left button...
//            ...and with right button draws all the time (for effects)...
//            ...to change the string need to select the Text tool again from the menus/toolbar

package gr.cti.eslate.canvas;

//import gr.cti.avakeeo.utils.*; //for AvakeeoOptionPane

import java.awt.event.*;
import javax.swing.*;

public class CanvasText extends CustomCanvasTool{

public String getToolName(){return "TEXT";};

String text="Use CanvasText(String text) to instantiate!!!";
int lastx,lasty;

public CanvasText(String text){
 super();
 this.text=text;
}

public void PlotString(int x,int y){
 gx.drawString(text,x,y);
 c.repaint();
}

public void mousePressed(MouseEvent e){ //MouseListener//
 if (SwingUtilities.isLeftMouseButton(e)) gx.setXOR(); 
 else gx.setPAINT();
 lastx=e.getX(); lasty=e.getY();
 PlotString(lastx,lasty);
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
 if (SwingUtilities.isLeftMouseButton(e)) {
  gx.setXOR(); 
  gx.drawString(text,lastx,lasty); //this doesn't repaint
//  PlotString(lastx,lasty); //?
 } else {DidSomeChange(); gx.setPAINT();} //15-8-1998
 lastx=e.getX(); lasty=e.getY();
 PlotString(lastx,lasty);
}

public void mouseReleased(MouseEvent e){ 
 if (SwingUtilities.isLeftMouseButton(e)) {
//16-7-1998//  gx.setXOR();
//16-7-1998// gx.drawString(text,lastx,lasty); //this doesn't repaint
  gx.setPAINT();
  PlotString(e.getX(),e.getY()); //this Paste repaints
 }
 DidSomeChange();
}

}
