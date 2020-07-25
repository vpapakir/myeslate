 package gr.cti.eslate.canvas;
 
 import java.io.*;
 import java.awt.*;

 import gr.cti.utils.Graphix;

 public class LineData implements Serializable //11-1-1999: fixed: had forgotten to make it Serializable
 { //4-12-1998: moved this here, out of TurtlePanel (for code clarity)

  static final long serialVersionUID = 2711999L; //27-1-1999: serial-version, so that new vers load OK

  protected int x1,y1,x2,y2;
  protected Color color;
  protected transient Rectangle boundsRect; //29Mar2000
  protected transient boolean dirty; //29Mar2000: unused for now since one can't change the line data after constructing it

  public static final double accuracy=1E-1; //static!!! (31-6-1998:decreased accuracy [easier selection]) //4-12-1998:moved here from TurtlePanel

  public LineData(int x1,int y1,int x2,int y2,Color color){
   this.x1=x1;
   this.y1=y1;
   this.x2=x2;
   this.y2=y2;
   this.color=color;
  };

  public void draw(Graphics g){ //don't need to check cliping here, it's been checked already if we got called by LineGroup.draw and anyway, the Graphics object will do such checking/cliping
   g.setColor(color);
   g.drawLine(x1,y1,x2,y2);
  }

  public boolean contains(int x,int y){
   int dxx,dyy;
   dxx=x-x1;  dyy=y-y1;  double dCA=Math.sqrt(dxx*dxx+dyy*dyy);
   dxx=x-x2;  dyy=y-y2;  double dCB=Math.sqrt(dxx*dxx+dyy*dyy);
   dxx=x1-x2; dyy=y1-y2; double dAB=Math.sqrt(dxx*dxx+dyy*dyy);
   return (dCA+dCB-dAB<accuracy);
  }

  public Rectangle getBoundingRectangle(){ //2-12-1998 //29Mar2000: renamed to "getBoundingRectangle" from "getRectangle"
   if(boundsRect==null || dirty){
    Rectangle r=Graphix.xyxy2rect(x1,y1,x2,y2);
    r.grow(1,1); //Graphics returns a (-1,-1) sized rect
    boundsRect=r; //one-step-assigment: thread-safe (instead of using "boundsRect=... and boundsRect.grow")
   }
   return boundsRect;
  }

 }
