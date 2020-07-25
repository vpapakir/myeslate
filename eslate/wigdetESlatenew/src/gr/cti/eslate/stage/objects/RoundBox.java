//Title:        Physics
//Version:      30Jun1999
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

//should have the angle of the corners as a property (cloneable&storable)

package gr.cti.eslate.stage.objects;

import java.awt.geom.*;

import gr.cti.eslate.stage.*;

public class RoundBox extends Box{

  static final long serialVersionUID = 30061999L; //30Jun1999

  public RoundBox() {
   super();
  }

  public RoundBox(double side){
   super(side);
  }

  public RoundBox(double cx,double cy,double width,double height){
   super(cx,cy,width,height);
  }

  // OVERRIDEs //////////////////////////////

  public String getBaseName(){ //30Jun1999
   return Res.localize("RoundBox"); //30Nov1999: localized
  }

  protected RectangularShape makeCacheShape(){
   return new RoundRectangle2D.Double(0,0,0,0,10,10); //15Nov1999: reuse a RoundRectangle2D object
  }

/* //15Nov1999: now reusing a single RoundRectangle2D object per RoundBox, and our ancestor's "calculateShape" implementation covers us
  public Shape calulateShape(){ //30Jun1999
   //System.out.println(center.getLocationX()+":"+center.getLocationY()+"-"+radius.getLocationX()+":"+edge.getLocationY());
   double cx=center.getLocationX();
   double cy=center.getLocationY();
   double dx=cx-edge.getLocationX();
   double dy=cy-edge.getLocationY();
   return ShapeUtilities.pointsToRoundRectangle(cx-dx,cy-dy,cx+dx,cy+dy,50,50);
  }
*/

}
