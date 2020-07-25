package gr.cti.eslate.planar;

import javax.swing.JComponent;
import gr.cti.utils.Dragger;
import java.awt.*;
import gr.cti.utils.DoublePoint;

public class PlotPoint extends JComponent //19Aug1999
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    static final int TOTAL_XSIZE=7;
    static final int TOTAL_YSIZE=7;
    static final int HALF_XSIZE=TOTAL_XSIZE/2;
    static final int HALF_YSIZE=TOTAL_YSIZE/2;

    static final int VISIBLE_XSIZE=3;
    static final int VISIBLE_YSIZE=3;

    static int pointIdCounter=0;
    int pointId=0;

/*
    public PlotPoint(Point p)
    {
     this(new DoublePoint(p));
    }
*/    

    public PlotPoint(DoublePoint p){
        //setDoubleBuffered(true);
        pointId=pointIdCounter++;
        setSize(TOTAL_XSIZE, TOTAL_YSIZE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //19Aug1999: showing a HAND cursor over control points
        keepPos(p);
        new Dragger(this);
    }

    public void remap(){
     setWorldCoordinates(pos.x,pos.y); //to remap the point to a new location depending on the container's mapping function
    }

    public DoublePoint getPosition(){ //26-2-1999
     return ((PlotPanel)getParent()).mapLocalToWorld(getX()+HALF_XSIZE,getY()+HALF_YSIZE); //mapping our local-coords CENTER to our world-coords point
    }

    ////////////

    public Rectangle getConnectionToPreviousPointBounds(){ //25-2-1999
     if (previous!=null){
      Rectangle r=new Rectangle(getCenterX(),getCenterY(),1,1);
      r=r.union(new Rectangle(previous.getCenterX(),previous.getCenterY(),1,1)); //count-in line to previous point (optimal: uses rectangles with width=height=1 and (x,y)=center of the point)
      return r;
     }else return null;
    }

    public Rectangle getConnectionToNextPointBounds(){ //25-2-1999
     if (next!=null){
      Rectangle r=new Rectangle(getCenterX(),getCenterY(),1,1);
      r=r.union(new Rectangle(next.getCenterX(),next.getCenterY(),1,1)); //count-in line to next point (optimal: uses rectangles with width=height=1 and (x,y)=center of the point)
      return r;
     }else return null;
    }

    public void repaintLines(){ //17-2-1999
     PlotPanel c=(PlotPanel)getParent();
     if (previous!=null) c.repaint(getConnectionToPreviousPointBounds());
     if (next!=null) c.repaint(getConnectionToNextPointBounds());
    }

    private void keepPos(DoublePoint p){ //15Oct1999: renamed to keepPos from setPos
     pos=p;
     setToolTipText("("+p.x+", "+p.y+")");
    }

    public void setWorldCoordinates(double x,double y){
     try{
      keepPos(new DoublePoint(x,y));
      Point p=((PlotPanel)getParent()).mapWorldToLocal(x,y);
      moveCenter(p.x,p.y); //26-2-1999: using this instead of setLocation, since we want to place the point's center at (x,y)
     }catch(Exception e){System.err.println(e+"\nNote: The point needs to have a parent component of type PlotPanel");} //14May1999: added a try-catch, cause there was a scenario where we moved the curPoint and it had been removed from it's old parent (after a "New" at PLANAR was calling PlotPanel:clearAll() and was forgetting to set PlotPanel:curPoint to null, before doing a removeAll() on the plotPanel)
    }

    private void moveCenter(int x,int y){ //26-2-1999: move CENTER and do REPAINTs
      Rectangle rp=getConnectionToPreviousPointBounds(), //25-2-1999
                rn=getConnectionToNextPointBounds();
      super.setLocation(x-HALF_XSIZE,y-HALF_YSIZE); //26-2-1999: places the CENTER of the point at (x,y)
      PlotPanel c=(PlotPanel)getParent();
      if(rp!=null) c.repaint(rp); //25-2-1999: repaint area over old connection vertex to any previous point
      if(rn!=null) c.repaint(rn); //25-2-1999  repaint area over old connection vertex to any next point
      repaintLines(); //17-2-1999
    }

    public void setCenterLocation(int x,int y){ //26-2-1999: called to place our CENTER and generate a pin event
     setLocation(x-HALF_XSIZE,y-HALF_YSIZE);
    }

    public void setLocation(int x, int y) //
    {
      x+=HALF_XSIZE;
      y+=HALF_YSIZE;
      moveCenter(x,y); //place our CENTER and do REPAINTs
      PlotPanel c=(PlotPanel)getParent();
      DoublePoint p=c.mapLocalToWorld(x, y);
      keepPos(p);
      c.vect.setVectorData(p.x,p.y); //don't call this from setWorldCoordinates
    }

    public int getCenterX()
    {
        return Math.round(getX() + getWidth() / 2);
    }

    public int getCenterY()
    {
        return Math.round(getY() + getHeight() / 2);
    }

    public void paintComponent(Graphics g)
    {
        //System.out.println("PlotPoint::paintComponent "+pointId); //does some extra repaints of other PlotPoints ???
        g.setColor(getForeground()/*Color.red*/); //12-2-1999
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.fillRect(getWidth()/2-VISIBLE_XSIZE/2, getHeight()/2-VISIBLE_YSIZE/2,VISIBLE_XSIZE,VISIBLE_YSIZE);
    }

    public void setPrevious(PlotPoint previous){ //18-2-1999
     PlotPoint c;

     c=previous.next;
     if (c!=null) c.previous=null; //set our new previous point's next point to not point to our previous one any more, since we're setting our point as the next one

     if (previous!=null) previous.next=null; //set our previous point not to point to us any more, since we're setting some other point as our previous one

     previous.next=this;
     this.previous=previous;
    }

    public void setNext(PlotPoint next){ //18-2-1999
     next.setPrevious(this); //reverse the order when implementing the action, to avoid rewriting the complex logic of setPrevious
    }

    DoublePoint pos; //our real 2D data (world coords, not the local [view] X,Y coords)
    PlotPoint next,previous; //17-2-1999: double linked list, so that we can refresh the lines to both the previous and the next point when dragged
}

