package gr.cti.eslate.canvas;

//any descendent using mouseClicked should
//ask clickingEnabled in order to click and if so
//then in mousePressed call super.mousePressed
//and in mouseDragged call super.mouseDragged
//(both the above must exist in order to work properly)
//in its respective handlers to fix a menu bug
//that sends mouseClicks (how caught: it didn't sent
//mousePressed+mouseReleased, sent just mouseClicked)

import java.awt.Cursor;
import java.awt.event.*;

import gr.cti.utils.Graphix;

public class CustomCanvasTool implements ICanvasTool{
                   //could have used: extends MouseAdapter
 protected Graphix gx;
 public CanvasPage c; //protected throws errors?
  //could use Component instead of JComponent
  //using this cause in debug I need paintImmediately in fill
 protected String name="tool";
// boolean ignoreClick=true;
//protected double[] values;
//protected String[] names;
 boolean clicksEnabled=false; //to fix some false mouseClicked events

 //ICanvasTool specific//
 public CustomCanvasTool(){c=null; gx=null;
}
 public CustomCanvasTool(CanvasPage c,Graphix gx){this.c=c; this.gx=gx;}
 public void reset(){}
 public void setComponent(CanvasPage c){this.c=c;}
 public void setGraphix(Graphix gx){this.gx=gx;}
 public String getToolName(){return "";};
// public void setParamNames(String[] names){this.names=names;}
// public String[] getParamNames(){return names;}
// public void setParamValues(double[] values){this.values=values;}
// public double[] getParamValues(){return values;}

 public boolean clickingEnabled(){
  if (clicksEnabled) {
    clicksEnabled=false;
    return true;
   }
  else return false;
 }

 //MouseListener//
 public void mouseClicked(MouseEvent e){
  clickingEnabled();
 }

 public void mouseEntered(MouseEvent e){c.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));}
 public void mouseExited(MouseEvent e){c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));}

 public void mousePressed(MouseEvent e){
  clicksEnabled=true; //to fix some false mouseClicked events
                   //(a mousePressed must precede a mouseClicked event)
 }

 public void mouseReleased(MouseEvent e){}

 //MouseMotionListener//
 public void mouseDragged(MouseEvent e){
  clicksEnabled=false; //to fix some false mouseClicked events
                   //(a mouseClicked can't follow a mouseDragged without a new mousePressed first)
 }
 public void mouseMoved(MouseEvent e){}

 ///////////////

 protected void DidSomeChange(){c.repaint(); c.DidSomeChange();} //15-8-1998: this now does a repaint too

}
