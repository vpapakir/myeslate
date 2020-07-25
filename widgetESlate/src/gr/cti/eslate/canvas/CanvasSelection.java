// @14/5/98: changed Rectangle to Rect (to avoid a bug caused when
//            user selects with filled shapes on and deselects with it off)

package gr.cti.eslate.canvas;

//bugs when clicking both buttons (XOR mask trails are left)

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CanvasSelection extends CustomCanvasTool{
 boolean oddClick=false; //zero clicks at start
 int x0,y0; //for single clicks
 int x3,y3; //used by MousePressed/MouseReleased/MouseDragged
 
 public boolean selected=false;
 public int x1,y1,x2,y2;

public String getToolName(){return "SELECTION";};

public void reset(){
// c.setSelection(null);
 removeOldSelection();
} //to remove selection when unplugging the tool

public void removeOldSelection(){
/**/ //17-8-1998//
 if (selected){
  gx.setXOR();
  gx.Rect(x1,y1,x2,y2);
  gx.setPAINT();  
  selected=false; //don't alter x1,y1,x2,y2 (needed in DrawingPanel.setImage)
  c.repaint();
 }
/**/
}

public void restoreSelection(){
 selectArea(x1,y1,x2,y2);
}

void selectArea(int sx1,int sy1,int sx2,int sy2){
 sx1=clipX(sx1); sy1=clipY(sy1); //19-1-1999
 sx2=clipX(sx2); sy2=clipY(sy2); //19-1-1999
/**/ //17-8-1998//
 removeOldSelection();
 gx.setXOR();
 gx.Rect(sx1,sy1,sx2,sy2);
 gx.setPAINT();
 x1=sx1; y1=sy1; x2=sx2; y2=sy2;
 selected=true;
 c.repaint();
/**/
// c.setSelection(Graphix.xyxy2rect(sx1,sy1,sx2,sy2));
}

//this has a problem is user drags the mouse at start of lines (missing clicks)
public void mouseClicked(MouseEvent e){ //MouseListener//
//System.out.println("selection:MouseClicked");
 if (!clickingEnabled()) return;
 if (SwingUtilities.isLeftMouseButton(e)) {
  oddClick=!oddClick;
  if (oddClick) {removeOldSelection();x0=e.getX(); y0=e.getY();}
  else selectArea(x0,y0,e.getX(),e.getY());
 }
}

public void mousePressed(MouseEvent e){ //MouseListener//
//System.out.println("selection:MousePressed");
 super.mousePressed(e);
 if (SwingUtilities.isRightMouseButton(e)) {x3=gx.getX(); y3=gx.getY();}
 //right mouse click starts from last graphix point (maybe drawn by the previous tool)
 else {x3=e.getX(); y3=e.getY();}
 selectArea(x3,y3,e.getX(),e.getY());
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
//System.out.println("selection:MouseDragged");
 super.mouseDragged(e); //fixes a bug
 if (SwingUtilities.isLeftMouseButton(e) && oddClick) //fix for when user drags the second point after a single Left click
  {oddClick=false; selectArea(x0,y0,e.getX(),e.getY());}
 else //?
  selectArea(x3,y3,e.getX(),e.getY()); //draw new line
// c.repaint(); //not needed: repaint is called by selectArea
}

public void clear(){
//System.out.println("Selection:clear");
 removeOldSelection();
 gx.ClearRect(x1,y1,x2,y2);
 restoreSelection(); //also repaints
 DidSomeChange(); 
}

public void cut(){copy(); clear();}

public void copy(){
//System.out.println("Selection:copy");
 removeOldSelection();
 gx.copy(x1,y1,x2,y2);
//paste(); //do an immediate paste, otherwise //4-9-1998: reenabled this, but after testing removed it again
  //the image is copied at the time of the first paste (that area might have been changed by then)
 restoreSelection(); //also repaints
}

public void paste(){
//System.out.println("Selection:paste");
 removeOldSelection(); //does a setPAINT() too
 gx.paste(x1,y1,x2,y2); c.repaint();
 restoreSelection();  //also repaints
 DidSomeChange();
}

public void drawImage(Image i){ //commented out
 removeOldSelection(); //removes selection (keeps x1,y1,x2,y2 values)
 gx.setPAINT();
 gx.drawImage(i,x1,y1,x2,y2);
 restoreSelection();  //also repaints
 DidSomeChange();
}

 public int clipX(int x){ //19-1-1999
  if (c instanceof DrawingPanel && x>c.PageWidth)
   return c.PageWidth;
  else return x;
 }

 public int clipY(int y){ //19-1-1999
  if (c instanceof DrawingPanel && y>c.PageWidth)
   return c.PageHeight;
  else return y;
 }

}