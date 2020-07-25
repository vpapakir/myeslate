//Title:        Physics
//Version:      15May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

package gr.cti.eslate.stage.objects;

import java.awt.Shape;
import java.awt.geom.*;
import gr.cti.shapes.*;
import gr.cti.eslate.stage.*;
import gr.cti.eslate.stage.models.*;
import gr.cti.eslate.utils.*;
import java.io.*;
import gr.cti.eslate.base.*;

/**
 * @version     2.0.0, 26-May-2006
 * @author      George Birbilis
 * @version     Kriton Kyrimis
 */
public class Slope extends PhysicsObject implements AsSlope
{
  static final long serialVersionUID = 5400099072056743207L; //2Jun1999
  /** FIELDS **/

  private ControlPoint slopeEdge1, slopeEdge2;

  public static final int CP_SLOPEEDGE1=0;
  public static final int CP_SLOPEEDGE2=1;


  /** CONSTRUCTORS **/

  public Slope() {
   this(50,50,100,40);
  }

  public Slope(double base,double angle){
   this(new Point2D.Double(0,0),base,angle);
  }

  public Slope(double sx,double sy, double base, double height){
   this(new Point2D.Double(sx,sy),new DoublePoint2D(sx+base,sy+height));
  }

  public Slope(Point2D slopeEdge1, double base, double angle){
   this(slopeEdge1,new Point2D.Double(slopeEdge1.getX()+base,slopeEdge1.getY()+base*Math.sin(angle)));
  }

  public Slope(Point2D slopeEdge1, Point2D slopeEdge2){
   super(); //28Aug1999: call super in order to register as scriptable
   setControlPoints(new ControlPoint[]{
    new ControlPoint(slopeEdge1),
    new ControlPoint(slopeEdge2)
   }); //for PhysicsObject's IControlPointContainer implementation
  }

  /** LogoScriptable **/

  public String[] getSupportedPrimitiveGroups(){
   String[] p1=super.getSupportedPrimitiveGroups();
   int len=p1.length;
   String[] p2=new String[len+4];
   System.arraycopy(p1,0,p2,0,len);
   p2[len]="gr.cti.eslate.scripting.logo.WidthPrimitives";
   p2[len+1]="gr.cti.eslate.scripting.logo.HeightPrimitives";
   p2[len+2]="gr.cti.eslate.scripting.logo.AnglePrimitives";
   p2[len+3]="gr.cti.eslate.scripting.logo.LengthPrimitives";
   return p2;
  }


  /** FUNCTIONALITY **/

  // AsSlope/HasAngle //////

  protected double getRadianAngle(){ //1Jun1999: convenience method
   return slopeEdge1.angle(slopeEdge2);
  }

  public double getAngle(){ //in degrees
   return /*NumberUtil*/Math.toDegrees(getRadianAngle());
  }

  public void setAngle(double angle)  //in degrees
  {
    double x1 = slopeEdge1.getLocationX();
    double y1 = slopeEdge1.getLocationY();
    double x2 = slopeEdge2.getLocationX();
    double y2 = slopeEdge2.getLocationY();
    double w = getObjectWidth();
    double h = w * Math.tan(Math.toRadians(angle));
    if (x1 <= x2 && y1 <= y2) {
      slopeEdge2.setLocation(x2, y1 + h);
      return;
    }
    if (x1 <= x2 && y1 >= y2) {
      slopeEdge1.setLocation(x1, y2 + h);
      return;
    }
    if (x1 >= x2 && y1 >= y2) {
      slopeEdge1.setLocation(x1, y2 + h);
      return;
    }
    if (x1 >= x2 && y1 <= y2) {
      slopeEdge2.setLocation(x2, y1 + h);
      return;
    }
  }

  // AsSlope/HasWidth /////////

  public double getObjectWidth(){
   return Math.abs(slopeEdge2.getLocationX()-slopeEdge1.getLocationX());
  }

  public void setObjectWidth(double value)
  {
     value = Math.abs(value);
     double x1 = slopeEdge1.getLocationX();
     double x2 = slopeEdge2.getLocationX();
     if (x1 <= x2) {
       slopeEdge2.setLocation(x1+value, slopeEdge2.getLocationY());
     }else{
       slopeEdge2.setLocation(x1-value, slopeEdge2.getLocationY());
     }
     update();
  }

  // AsSlope/HasHeight /////////

  public double getObjectHeight(){
   return Math.abs(slopeEdge2.getLocationY()-slopeEdge1.getLocationY());
  }

  public void setObjectHeight(double value)
  {
     value = Math.abs(value);
     double y1 = slopeEdge1.getLocationY();
     double y2 = slopeEdge2.getLocationY();
     if (y2 <= y1) {
       slopeEdge2.setLocation(slopeEdge2.getLocationX(), y1-value);
     }else{
       slopeEdge2.setLocation(slopeEdge2.getLocationX(), y1+value);
     }
     update();
  }

  // AsSlope/HasLength //////////////////////   //1Jun1999

  public double getLength(){
   double width=getObjectWidth();
   double height=getObjectHeight();
   return Math.sqrt(width*width+height*height);
  }

  public void setLength(double value){ //keep base angle same, so change width, not height
   setObjectWidth(Math.cos(getRadianAngle())*value);
  }

  public void setProperties(StorageStructure properties){
      if(properties==null)
        return;

      super.setProperties(properties);
      setPreparedForCopy(properties.get("PreparedForCopy", false));
      // if the object is not prepared for copy then retrieve its child handles normally
      if (!isPreparedForCopy()){
          if (Integer.valueOf(properties.getDataVersion()).intValue() >= 2){
            // Properties after changing internal objects to handles. This means that we have to remove
            // the control point's handles of the Scene that had been added in the constructor, as well
            // as the control Points themselves, and then retrieve the handles using the
            //restoreChildObjects method. All the control point objects are respectively retieved from
            // their handles and finally added to physics object.
            for (int i=handle.getChildHandles().length-1; i >= 0; i--)
              handle.remove(handle.getChildHandles()[i]);
            try{
              Object[] objects = getESlateHandle().restoreChildObjects((ESlateFieldMap2)properties, "children");
              ControlPoint[] tempCP = new ControlPoint[objects.length];
              if (((ControlPoint)((ESlateHandle)objects[0]).getComponent()).getRole().equalsIgnoreCase("CP_SLOPEEDGE1")){
                  tempCP[CP_SLOPEEDGE1] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
                  tempCP[CP_SLOPEEDGE2] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
              }
              else{
                  tempCP[CP_SLOPEEDGE1] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
                  tempCP[CP_SLOPEEDGE2] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
              }
              //for (int i=0; i < tempCP.length; i++)
              //  tempCP[i] = (ControlPoint)((ESlateHandle)objects[i]).getComponent();
              setControlPoints(tempCP);
            }catch(IOException e){
              e.printStackTrace();
            }
          }
      }
      // else don't retrieve any handle. Just restore its controlPoints as objects. When the copied
      // object will be added to the scene the handles will be constructed from scratch for all
      // the sub tree.
      else{
          ControlPoint[] tempControlPoints = (ControlPoint[])properties.get("CONTROL_POINTS_PROPERTY");
          for (int i=0; i < tempControlPoints.length; i++)
              getESlateHandle().add(tempControlPoints[i].getESlateHandle());
          setControlPoints(tempControlPoints);
      }
  }


  /** OVERRIDEs **/

  public void setControlPoints(ControlPoint[] p){ //15May2000
   slopeEdge1=p[CP_SLOPEEDGE1];
   slopeEdge1.setRole("CP_SLOPEEDGE1");
   slopeEdge2=p[CP_SLOPEEDGE2];
   slopeEdge2.setRole("CP_SLOPEEDGE2");
   super.setControlPoints(p); //must call this after we do get successfully all the needed control points
  }

  public String getBaseName(){ //30Jun1999
   return Res.localize("Slope"); //30Nov1999: localized
  }

  public Shape calculateShape(){ //30Jun1999: now caching is done at ancestor
   //System.out.println("Slope:calculateShape");
   double ssy=slopeEdge1.getLocationY();
   double sey=slopeEdge2.getLocationY();
   return new Triangle2D(slopeEdge1.getLocation2D(),slopeEdge2.getLocation2D(),
     (ssy<sey)?
       new DoublePoint2D(slopeEdge2.getLocationX(),ssy) :
       new DoublePoint2D(slopeEdge1.getLocationX(),sey)
    );
  }
}
