//version: 29Mar2000

package gr.cti.eslate.canvas;

import gr.cti.eslate.logo.ProcedureCall;
import java.util.*; //ArrayList, Enumeration
import java.io.*;
import java.awt.*;


/**
 * @version     2.0.6, 9-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class LineGroup //26Nov1999: made public!
 implements Serializable //11-1-1999: fixed: had forgotten to make it Serializable
{
  static final long serialVersionUID = 2711999L; //27-1-1999: serial-version, so that new vers load OK

  public transient ProcedureCall call; //proc related to this lineGroup //6-1-1999: not-serialized
  @SuppressWarnings("unchecked")
  public transient Vector savedTurtles=null; //of SavedTurtle class //6-1-1998: not-serialized

  protected ArrayList<LineData> lines; //of LineData class //26Nov1999: made private (make sure it doesn't get renamed at encryption)

  protected transient boolean dirty; //29Mar2000: using "dirty" flag to see whether we want to cache "bounding rectangle" etc.
  protected transient Rectangle boundsRect; //29Mar2000

  ///////////////

  /**
   * Translate the lines in the line group by a given offset.
   * @param     dX      The x offset.
   * @param     dY      The y offset.
   */
  public void translateLines(int dX, int dY)
  {
    int nLines = lines.size();
    for (int i=0; i<nLines; i++) {
      LineData ld = lines.get(i);
      ld.x1 += dX;
      ld.y1 += dY;
      ld.x2 += dX;
      ld.y2 += dY;
    }
  }

  public LineData add(LineData line){ //26Nov1999
   if(line!=null){ //21Dec1999: added safety check
    lines.add(line);
    dirty=true;
   }
   return line;
  }

  public LineGroup(ProcedureCall call){
   this.call=call;
   lines=new ArrayList<LineData>();
  }

  public boolean contains(int x,int y){
   for (int j=lines.size();j-->0;) //search lines from top to bottom
    if (lines.get(j).contains(x,y)) return true;
   return false;
  }

  public boolean isDirty(){return dirty;} //29Mar2000

  public boolean isEmpty(){
   return (lines.size()==0);
  }

  public void setEmpty(){ //30Mar2000: renamed to "setEmpty" from "clearLines"
   lines.clear();
  }

  public Rectangle getBoundingRectangle(){ //2-12-1998 //29Mar2000: renamed to "getBoundingRectangle" from "getRectangle"
   if(boundsRect==null || dirty){
    Rectangle rect=new Rectangle();
    synchronized(lines){ //29Mar2000: made thread-safe: don't have the synchronized block inside the for loop, it will lose too much time aquiring and releasing the object monitor for the "lines" object
     for(int i=lines.size();i-->0;) //29Mar2000: made optimal by removing the Enumeration use
      rect.add( lines.get(i).getBoundingRectangle() );
    }
    boundsRect=rect; //one-step-assigment: more thread-safe (instead of using "boundsRect=... and boundsRect.add")
   }
   return boundsRect;
  }

  public void draw(Graphics g){ //11-2-1999: moved from TurtlePanel //not using cliping
   if(lines==null) { //21Dec1999: added safety check 
    lines=new ArrayList<LineData>();
    return; //29Mar2000
   }

   try{
    //Rectangle clip=g.getClipBounds(); //29Mar2000: added-cliping
    for (int j=0,count=lines.size();j<count;j++){ //draw lines from bottom to top //29Mar2000: made optimal by not calling lines.size() at every loop iteration
     LineData line=lines.get(j);
     // Clipping here works oddly when resizing the component.
     //if (line.getBoundingRectangle().intersects(clip)) //29Mar2000: cliping: draw a line only if its boundingRectangle is inside the current clipBounds
      line.draw(g);
    }
   }catch(Exception e){
    System.err.println("LineGroup::draw throwed an "+e);
   } //21Dec1999: added try-catch
  }

}
