// @2/5/98: G.Birbilis - Drag Button: Left=move image, Right=copy image

package gr.cti.eslate.canvas;

import java.awt.event.*;
import javax.swing.*;

public class CanvasPaste extends CustomCanvasTool{

int lastx,lasty;

public String getToolName(){return "PASTE";};

public void Paste(int x,int y){
 gx.paste(x,y);
 c.repaint();
}

public void mousePressed(MouseEvent e){ //MouseListener//
 if (SwingUtilities.isLeftMouseButton(e)) gx.setXOR(); 
 else gx.setPAINT();
 lastx=e.getX(); lasty=e.getY();
 Paste(lastx,lasty);
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
 if (SwingUtilities.isLeftMouseButton(e)) {
  gx.setXOR(); 
  Paste(lastx,lasty); 
 } else gx.setPAINT();  
 lastx=e.getX(); lasty=e.getY();
 Paste(lastx,lasty);
}

public void mouseReleased(MouseEvent e){ 
 if (SwingUtilities.isLeftMouseButton(e)) {
//16-7-1998//  gx.setXOR();
//16-7-1998//  gx.paste(lastx,lasty);    //this paste doesn't repaint
  gx.setPAINT();
  Paste(e.getX(),e.getY()); //this Paste repaints
 }
 DidSomeChange();
}    
   
}