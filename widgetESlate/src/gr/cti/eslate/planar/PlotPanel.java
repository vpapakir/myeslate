//BUG: cut-paste loses the connections among the cut&pasted points

package gr.cti.eslate.planar;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import gr.cti.eslate.sharedObject.VectorData;
import gr.cti.utils.*;

/**
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Kriton Kyrimis"
 * @version     2.0.1, 23-Jan-2008
 */
public class PlotPanel extends JComponent
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 public int mode;
 public static final int MODE_EDIT=0;
 public static final int MODE_SELECT=1;

 public static final int MAPPING_CARTESIAN=0;
 public static final int MAPPING_POLAR=1;

 public static final int GRID_NONE=0;
 public static final int GRID_DOTS=1;
 public static final int GRID_LINES=2;

 public Color COLOR_AXIS=Color.darkGray;  //axis
 public Color COLOR_GRID=Color.lightGray; //grid
 public Color COLOR_LINE=Color.black;     //lines
 public Color COLOR_SELECTOR=Color.blue;  //selector
 public Color COLOR_POINT=Color.magenta;  //control points
 public Color COLOR_CURRENT=Color.blue;   //the point Slider manipulates (setting a point as current sends value to slider)
 public Color COLOR_START=Color.green;    //start of playback
 public Color COLOR_END=Color.red;        //end of playback

 //////////////////////////////////////

 public PlotPanel(VectorData v){
  enableEvents(ComponentEvent.COMPONENT_RESIZED); //put at start cause affects listeners

  vect = v;
  setLayout(null); //don't use a layout manager
  setDoubleBuffered(true);

  addMouseListener(new ListenerMouse());
  addMouseMotionListener(new ListenerMouseMotion());
 }

 //////////////////////////////////////

 public Selector selector=new Selector(0,0,0,0);
 {selector.setForeground(COLOR_SELECTOR);}

 //////////////////////////////////////

 public void remap(){
  synchronized(getTreeLock()){ //useful?
   for(int i=getComponentCount();i-->0;){ //25-2-1999: optimized loop checking
    Component c=getComponent(i);
    if (c instanceof PlotPoint) //remap selector too???
      ((PlotPoint)c).remap();
   }
   repaint(); //remap-repaint the axis&grid too
  }
 }

 public void setMapping(int mapping){
  //remap(); //don't use remap... it would transform the shape... just want to change the pos values to polar
 }

/*
    //25-2-1999: removed, using setFromPoint, setToPoint instead
    public void setXrange(double fromX, double toX){ //allow reverse axis?
     this.fromX=Math.min(fromX,toX);
     this.toX=Math.max(fromX,toX);
     remap();
    }

    public void setYrange(double fromY, double toY){ //allow reverse axis?
     this.fromY=Math.min(fromY,toY);
     this.toY=Math.max(fromY,toY);
     remap();
    }
*/

 //fromX property//

 public double getFromX(){ //29Jun2000
  return fromX;
 }

 public void setFromX(double value){ //29Jun2000
  setFromPoint(value,getFromY());
 }

 //fromY property//

 public double getFromY(){ //29Jun2000
  return fromY;
 }

 public void setFromY(double value){ //29Jun2000
  setFromPoint(getFromX(),value);
 }

 //toX property//

 public double getToX(){ //29Jun2000
  return toX;
 }

 public void setToX(double value){ //29Jun2000
  setToPoint(value,getToY());
 }

 //toY property//

 public double getToY(){ //29Jun2000
  return toY;
 }

 public void setToY(double value){ //29Jun2000
  setToPoint(getToX(),value);
 }

 //

 public void setFromPoint(double x, double y){ //allow reverse axis?
  this.fromX=x;
  this.fromY=y;
  remap();
 }

 public void setToPoint(double x, double y){ //allow reverse axis?
  this.toX=x;
  this.toY=y;
  remap();
 }

 public double xRangeLength(){ return Math.abs(toX-fromX)+1; } //must use abs, cause fromX,toX may be both negative
 public double yRangeLength(){ return Math.abs(toY-fromY+1); } //must use abs, cause fromY,toY may be both negative

 // LOCAL -> WORLD coordinates mapping ///////////////////

 DoublePoint mapLocalToWorld(int x, int y){ //mapping from a [local] Point to a [world] DoublePoint
  //SCALE//
  double xScaler=xRangeLength()/getWidth();
  double yScaler=yRangeLength()/getHeight();

  //TRANSLATE//
  DoublePoint p=new DoublePoint();
  p.x=(int)Math.round(x*xScaler+fromX); //27-3-1999: using rounding instead of truncating
  p.y=(int)Math.round((getHeight()-y)*yScaler+fromY); //reversing the Y axis //27-3-1999: using rounding instead of truncating

  return p;
 }

 // WORLD -> LOCAL coordinates mapping ///////////////////

 Point mapWorldToLocal(double x, double y){ //mapping from a [world] DoublePoint to a [local] Point
  //SCALE//
  double xScaler=getWidth()/xRangeLength();
  double yScaler=getHeight()/yRangeLength();

  //TRANSLATE//
  Point p=new Point();
  p.x=(int)Math.round((x-fromX)*xScaler); //27-3-1999: using rounding instead of truncating
  p.y=(int)Math.round(getHeight()-(y-fromY)*yScaler); //reversing the Y axis //27-3-1999: using rounding instead of truncating

  return p;
 }

 /////////////////////

    public void addPoint(int x, int y, PlotPoint p)
    {
        add(p);
        p.setCenterLocation(x, y); //26-2-1999: using this instead of setLocation, since we want to place the point's center at (x,y)
        p.addMouseListener(pointSelector);
    }

    public void newVectorData(){
      if (curPoint!=null && curPoint!=lastPoint) curPoint.setWorldCoordinates(vect.getX(),vect.getY()); //14May1999: now a new value moves the curPoint only if there is such a point and it's some point older then the last one...
      else newWorldPoint(vect.getX(),vect.getY()); //...else if there is no current point [usually when no points exist], or the curPoint is the last one, add a new point connected to this last one
    }

    public void paintConnections(Graphics g)
    {
       //System.out.println("PlotPanel:paintConnections");
       g.setColor(COLOR_LINE); //17-2-1999: now setting our own color and not using any already set (was in fact using the one set by paintAxis)
       for(int i=getComponentCount();i-->0;) //25-2-1999: optimized loop checking
        {
            Component c= getComponent(i);
            if (c instanceof PlotPoint)
            {
               PlotPoint p = (PlotPoint)c;
               PlotPoint next = p.next;
               if(next != null)
                 g.drawLine(p.getCenterX(), p.getCenterY(), next.getCenterX(), next.getCenterY());
            }
        }

    }

    public void paintAxis(Graphics g)
    {
      //System.out.println("PlotPanel:paintAxis");
        Point p=mapWorldToLocal(0,0); //24-2-1999
        g.setColor(COLOR_AXIS); //17-2-1999
        g.drawLine(0, p.y, getWidth(), p.y);  //X axis
        g.drawLine(p.x, 0, p.x, getHeight()); //Y axis
    }

    public void paintGrid(Graphics g) //26-2-1999: made functional
    {
      //System.out.println("PlotPanel:paintGrid");

        g.setColor(COLOR_GRID);

        for(double x=fromX;x<=toX;x++){ //use x step? //??? seems to eat up a line at the left
         Point p1=mapWorldToLocal(x,fromY);
         Point p2=mapWorldToLocal(x,toY);
         g.drawLine(p1.x,p1.y,p2.x,p2.y);
        }

        for(double y=fromY;y<=toY;y++){ //use y step? //??? seems to eat up a line at the top
         Point p1=mapWorldToLocal(fromX,y);
         Point p2=mapWorldToLocal(toX,y);
         g.drawLine(p1.x,p1.y,p2.x,p2.y);
        }

    }

    public void paintComponent(Graphics g)
    {
        //System.out.println("PlotPanel:paintComponent");
        if (gridVisible) paintGrid(g);
        if (axisVisible) paintAxis(g); //8-4-1999: Axis is now painted over the grid and not under it
        paintConnections(g);
    }

    int pointCount=0;

    public void newGraph(){
     lastPoint=null;
    }

    public void newWorldPoint(double x,double y){ //24May1999: want to add a new point and connect it to the last point
     Point p=mapWorldToLocal(x,y);
     newLocalPoint(p.x,p.y);
    }

    void newLocalPoint(int x, int y)
    {
      synchronized(getTreeLock()){ //useful?
        PlotPoint p = new PlotPoint(mapLocalToWorld(x, y)); //!!!
        /**/p.setName(Integer.toString(pointCount++));
        p.setVisible(controlPointsVisible);
        if(lastPoint != null) //using lastPoint and not curPoint here, so that we generate a single connected line and not a graph (using curPoint in this block allows one to make graphs)
         lastPoint.setNext(p); //this sets a two-directional link between "lastPoint" and "p"
        lastPoint=p; //keep last added point
        addPoint(x, y, p); //this must be after the previous link setting, so that the new point draws a line to its previous point
        setCurrentPoint(p);
        vect.setVectorData(p.pos.x, p.pos.y);
      }
    }

    public void setCurrentPoint(PlotPoint p){ //12-2-1999
     if (curPoint!=null) curPoint.setForeground(COLOR_POINT); //17-2-1999: show that last selector point is no longer selector
     curPoint=p;
     if (curPoint!=null) curPoint.setForeground(COLOR_CURRENT); //change the color of the newly selector point
     vect.setVectorData(curPoint.pos.x, curPoint.pos.y); //17-2-1999: selecting a point sends its data to the slider
    }

    public void remove(PlotPoint c){ //18-2-1999: when removing a point, remove any links to it from other Points
     PlotPoint t;

     t=c.previous;                 //remove link from previous point (if any)
     if (t!=null) t.next=null;

     t=c.next;                     //remove link from next point (if any)
     if (t!=null) t.previous=null;

     super.remove(c); //!!! don't forget this
    }

    //////////////


    Vector<?> clipboard=new Vector<Object>();

    public void selectAll(){
     add(selector); //1-3-1999: add the selection to the panel (in case not added yet)
     selector.selectAll(); //1Mar1999: now not using the PlotPanel's bounds, but the bounding box that includes all PlotPoints inside the PlotPanel 
    }

    /////////

    public void paste(){ //Paste points by inserting at curPoint
     int clipsize=clipboard.size();
     if (clipsize<=0) return; //25-2-1999: if clipboard is empty, then do nothing
     for(int i=clipsize;i-->0;) //25-2-1999: optimized loop checking
      add((PlotPoint)clipboard.elementAt(i)); //(1)add to display
     if (curPoint!=null)              //(2)connect to curPoint
      curPoint.setNext((PlotPoint)clipboard.elementAt(0)); //links in both directions
     repaint(); //do a refresh
    }

    public void copy(){ //Copy selector points to clipboard (don't sort them)
     clipboard=selector.getSelectedComponents(); //(1)copy the selector Points to the clipboard
    }

    public void cut(){ //Cut selector points to clipboard
     copy();  //(1)copy to clipboard
     clear(); //(2)clear from display
    }

    ////////

    public void clear(){ //remove selector points from planar
     Vector<?> selection=selector.getSelectedComponents();
     synchronized(getTreeLock()){ //useful?
      for(int i=selection.size();i-->0;){ //the remove proc must remove the links to this Point from other points as well //25-2-1999: optimized loop checking
       PlotPoint c=(PlotPoint)selection.elementAt(i);
       if (c==curPoint) curPoint=null;
       if (c==lastPoint) newGraph();
       remove(c); //remove from display
      }
      repaint(); //do a refresh
     }
    }

    public void clearAll(){ //remove all points from planar //1Mar1999: reimplemented not to use selectAll+clear
     remove(selector); //remove the selection too (???)
     curPoint=null; //14May1999: before removing all the points, do set the curPoint to null, cause else when some value comes in from the SO via the newVectorData() notification, the curPoint's settings will change and it will not have a parent component to tell to redraw
     removeAll(); //remove the selector too
     repaint(); //do a refresh
    }

    //////////////

    public void setControlPointsVisible(boolean visible){ //24-2-1999: renamed from showControlPoints
     controlPointsVisible=visible;
     synchronized(getTreeLock()){ //useful?
      for(int i=getComponentCount();i-->0;){ //25-2-1999: optimized loop checking
       Component c=getComponent(i);
       if (c instanceof PlotPoint) //don't toggle other elements' visiblity (for example the Selector's)
        c.setVisible(visible);
      }
     }
    }

    public void setGridVisible(boolean visible){ //24-2-1999: renamed from showGrid
     gridVisible=visible;
     repaint();
    }

    public void setGridType(int type){
    //? need gridvisible?
    }

    public void setAxisVisible(boolean visible){ //24-2-1999
     axisVisible=visible;
     repaint();
    }

////////////////////

 public void processComponentEvent(ComponentEvent e){ //24-2-1999
  //System.out.println("Resized");
  super.processComponentEvent(e); //place first!!!
  if (e.getID()==ComponentEvent.COMPONENT_RESIZED)
   remap();
 }

////////////////////////////////////////////////////////////////////////////////////////////

    class ListenerMouse extends MouseAdapter{
     boolean clickFromMenu=true;
     public void mouseReleased(MouseEvent e){
      if (SwingUtilities.isLeftMouseButton(e)) //4Jun1999: only for left mouse button, right button will be drawing continuous lines with mouse clicks
       newGraph();
     } //13May1999: when mouse is released, a new line is started (Xronis asked for it: same as 2DSlider behaviour at "Avakeeo for OpenDoc" )
     public void mousePressed(MouseEvent e){
      clickFromMenu=false;
      if (e.isShiftDown()){ //if shift pressed, then add selection
       int x=e.getX();
       int y=e.getY();
       selector.setBounds(x,y,0,0);
       add(selector);
       selector.setStartingPoint(x,y);
      }
     }
     public void mouseClicked(MouseEvent e){
      if (clickFromMenu) return;
      clickFromMenu=true;
      //System.out.println("Mouse Clicked");
      if (e.isShiftDown()){ //if shift is pressed, then remove selection
       remove(selector); //it has no parent any more, so when asking it for selector components won't give any
       repaint(); //2Mar1999: must repaint
      }else newLocalPoint(e.getX(), e.getY());          //if left click, then add a new point
     }
    }

    class ListenerMouseMotion extends MouseMotionAdapter{
     public void mouseDragged(MouseEvent e){
      //System.out.println("Mouse Dragged");
      int x=e.getX();
      int y=e.getY();
      if (x<0 || y<0 || x>getWidth() || y>getHeight()) return; //13May1999: when dragging out of the visible area, just ignore (Xronis asked for it: else caused Planar to change its XY range, so the connected Slider changed its X&Y ranges too)
      if (mode==MODE_SELECT)
       selector.setEndingPoint(x,y);
       else
        if (!e.isShiftDown()) newLocalPoint(x,y); //if shift pressed during drag, resize selector
        else selector.setEndingPoint(x,y);
     }
    }

////////////////////////////////////////////////////////////////////////////////////////////

    class PointSelector extends MouseAdapter{ //setting this as a listener to each point we make
     public void mousePressed(MouseEvent e){ //use this instead of MouseClicked, should fix some bug where PlotPanel seems to get some drags instead of our component
      //System.out.println("Selected a point!");
      if (SwingUtilities.isLeftMouseButton(e))
       setCurrentPoint((PlotPoint)e.getSource());
      else
       ;//setPlayBackEnd((PlotPoint)e.getSource());
       //...if some end disconnected connect?
     }
    }

    private PointSelector pointSelector=new PointSelector();

////////////////////////////////////////////////////////////////////////////////////////////

    /*private*/ VectorData vect;
    private PlotPoint lastPoint,curPoint;
    private boolean controlPointsVisible,gridVisible,axisVisible;
    private double fromX=-20,toX=20, fromY=-20,toY=20;
}

