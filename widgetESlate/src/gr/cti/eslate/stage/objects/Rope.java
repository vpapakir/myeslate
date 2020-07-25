/*
TO DO:
- use cos/sin for setLength() or the ratios used at AbstractPointDistanceFromPointConstraint's "enable" method implementation?
- add HasAngle implementation
- group all required i/f's for something to be a Rope into a AsRope interface and have this object implement that i/f
*/

//Title:        Stage/Rope
//Version:      15May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Rope object

package gr.cti.eslate.stage.objects;

import java.awt.Shape;
import java.awt.geom.*;

import gr.cti.eslate.stage.*;
import gr.cti.eslate.stage.models.*;
import gr.cti.eslate.utils.*;
import java.io.*;
import gr.cti.eslate.base.*;

public class Rope extends PhysicsObject implements AsRope{

  //19Oct1999: serial-version, shouldn't need it cause the ancestor (PhysicsObject) does the
  //save/load, but Java seems to need it at all descendents if we change the ancestor's code
  static final long serialVersionUID = 68128468572810510L;
  private ControlPoint start,end;

  public static final int CP_START=0;
  public static final int CP_END=1;

  private Line2D shape=new Line2D.Double(0,0,0,0); //15Nov1999: reusing the same Line2D.Double object per Rope instance

  // LogoScriptable ///////////
  public String[] getSupportedPrimitiveGroups(){
   String[] p1=super.getSupportedPrimitiveGroups();
   int len=p1.length;
   String[] p2=new String[len+2];
   System.arraycopy(p1,0,p2,0,len);
   p2[len]="gr.cti.eslate.scripting.logo.LengthPrimitives";
   p2[len+1]="gr.cti.eslate.scripting.logo.AnglePrimitives"; //23Apr2000
   return p2;
  }

  public Rope() {
   this(50,50,100,100);
  }

  public Rope(double length){
   this(0,0,length,length);
  }

  public Rope(double x1,double y1, double x2,double y2){
   super(); //28Aug1999: call super in order to register as scriptable
   setControlPoints(new ControlPoint[]{
    new ControlPoint(x1,y1), //start
    new ControlPoint(x2,y2) //end
   }); //for PhysicsObject's IControlPointContainer implementation
  }

  // OVERRIDEs //////////////////////////////

  public void setControlPoints(ControlPoint[] p){ //15May2000
   start=p[CP_START];
   start.setRole("CP_START");
   end=p[CP_END];
   end.setRole("CP_END");
   super.setControlPoints(p); //must call this after we do get successfully all the needed control points
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
              if (((ControlPoint)((ESlateHandle)objects[0]).getComponent()).getRole().equalsIgnoreCase("CP_START")){
                  tempCP[CP_START] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
                  tempCP[CP_END] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
              }
              else{
                  tempCP[CP_START] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
                  tempCP[CP_END] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
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

  public String getBaseName(){ //30Jun1999
   return Res.localize("Rope"); //30Nov1999: localized
  }

  // HasLength /////////////

  public double getLength(){return start.distance(end);}
  public void setLength(double value){ //move end on start->end direction, so that |pointOnCircumference-center|=radius
   if(value!=getLength()){ //if set to current value, ignore
    double angle=start.angle(end);
    end.setLocation(start.getLocationX()+Math.cos(angle)*value,start.getLocationY()+Math.sin(angle)*value);
    update();
   }
  }

 // HasWidth ///////////////

 //...should set the stroke with here... (use a HasStroke apart from HasShape i/f at Java2D renderer support)


  // HasAngle /////

  protected double getRadianAngle(){ //convenience method
   return start.angle(end);
  }

  public double getAngle(){ //23Apr2000 //in degrees
   return /*NumberUtil*/Math.toDegrees(getRadianAngle());
  }

  public void setAngle(double angle){ //23Apr2000 //in degrees //only "end" moves when changing this property, the other point stays fixed
   if (angle!=getAngle()){ //if set to current value, ignore
    AffineTransform a=AffineTransform.getRotateInstance(/*NumberUtil*/Math.toRadians(angle)-getRadianAngle(),start.getLocationX(),start.getLocationY()); //rotate the movingEdge arround the fixedEdge by "newAngle-currentAngle" in radians
    end.transform(a);
    update();
   }
  }

  // Java2D projector //////////////////////////////
  public Shape calculateShape(){ //30Jun1999: now caching is done at ancestor
   //System.out.println(center.getLocationX()+":"+center.getLocationY()+"-"+pointOnCircumference.getLocationX()+":"+pointOnCircumference.getLocationY());
   //return new Line2D.Double(start.getLocation2D(),end.getLocation2D());
   shape.setLine(start.getLocationX(),start.getLocationY(),end.getLocationX(),end.getLocationY()); //15Nov1999: avoid using Point2D objects, use their x,y elements instead
   return shape; //15Nov1999: reuse the same line shape per Rope instance
  }

}
