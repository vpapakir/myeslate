package gr.cti.eslate.canvas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CanvasFill extends CustomCanvasTool{

 public String getToolName(){return "FILL";};
 public Thread t=null;
 
public void reset(){ //used when unplugging the tool
 if (t!=null){
  //System.out.println("Killing Fill thread");
  gx.stopFilling=true;
//  t.stop(); //?should be able to stop the thread and not have to set stopFilling
  t=null;   //?
 }
}

public void mousePressed(MouseEvent e){ //MouseListener//
// System.out.println("fill:mousePressed");
// System.out.println(gx.getPoint(e.getX(),e.getY()));
 if (t!=null){reset(); return;} //to kill any old threads
 (t=new Thread(
     new Filler(e.getX(),e.getY(),gx.fgrColor, //used if flooding, ignored if replacing
      SwingUtilities.isRightMouseButton(e)) //replace or flood
    )
 ).start();
}

///////////////////////////////////////////////////////////////////////
 
class Filler implements Runnable{
 int x,y;
 Color boundary;
 boolean replace;
 
 public Filler(int x,int y,Color boundary,boolean replace){
  this.x=x;
  this.y=y;
  this.boundary=boundary;
  this.replace=replace;
 }
 
 public void run(){
  //System.out.println("Starting Fill thread (press mouse button to stop filling)");
  gx.FourFill(c,x,y,boundary,replace);
  DidSomeChange(); //?if no fill done then don't call this
  //System.out.println("Stopped filling");
  t=null;
 }

}

///////////////////////////////////////////////////////////////////////   
   
}
