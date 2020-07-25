package gr.cti.eslate.canvas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gr.cti.utils.Graphix;

public class CanvasCanonicalPolygon extends CustomCanvasTool{
 int cx=0, cy=0; //initial center for right clicks
 int rx=0,ry=0;
 Polygon p;
 int edges=3;

public String getToolName(){return "CANONICAL POLYGON";};

public CanvasCanonicalPolygon(int edges){
 super();
 if (edges>0) this.edges=edges; else edges=1; //<=0 would cause errors!!!
}

public void Plot(int cx,int cy, int x,int y){ //sets p
 p=Graphix.calcCanonicalPolygon(cx,cy,x,y,edges);
 gx.Polygon(p);
 gx.moveTo(cx,cy); //Polygon can't calc the center to store as last point, so move there ourselves
}

public void mousePressed(MouseEvent e){ //MouseListener//
// System.out.println("polygon:MousePressed");
 if (SwingUtilities.isRightMouseButton(e))
  {cx=gx.getX(); cy=gx.getY();} //get last polygon's center (or last point as center)
 else {cx=e.getX(); cy=e.getY();} //get current point as center
 gx.setXOR();
 rx=e.getX(); ry=e.getY(); //keep these: Graphix keeps as last point of a polygon it's center
 Plot(cx,cy,rx,ry);
 c.repaint();
}

public void mouseReleased(MouseEvent e){ //MouseListener//
// System.out.println("MouseReleased");
 gx.setPAINT();
 gx.Polygon(p); //just draw the last XOR polygon with its final color
 //ignore e.getX(),e.getY() - last polygon was drawn with (rx,ry) for sure
 DidSomeChange(); //15-8-1998: only here the shape stops changing - this repaints too
}

public void mouseDragged(MouseEvent e){ //MouseMotionListener//
// System.out.println("MouseDragged");
 gx.setXOR();
 gx.Polygon(p); //erase old polygon (redraw it in XOR mode)
 rx=e.getX(); ry=e.getY();
 Plot(cx,cy,rx,ry); //draw new polygon
 c.repaint(); 
}
    
}
