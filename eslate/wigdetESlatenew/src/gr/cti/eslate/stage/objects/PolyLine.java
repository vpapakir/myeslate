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

import gr.cti.eslate.utils.*;
import java.io.*;
import gr.cti.eslate.base.*;

/**
 * @version     2.0.0, 25-May-2006
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class PolyLine extends PhysicsObject  //30Jun1999
{
  static final long serialVersionUID = 68128468572810510L; //19Oct1999: serial-version, shouldn't need it cause the ancestor (PhysicsObject) does the save/load, but Java seems to need it at all descendents if we change the ancestor's code
  private ControlPoint[] points;

  public PolyLine() {
   this(3);
  }

  public PolyLine(int points){
   this(new DoublePoint2D[]{new DoublePoint2D(50,50),new DoublePoint2D(100,120), new DoublePoint2D(140,30)});
  }

  public PolyLine(Point2D[] points){
   super(); //28Aug1999: call super in order to register as scriptable
   this.points=ControlPoint.makeControlPointArray(points);
   setControlPoints(this.points); //for PhysicsObject's IControlPointContainer implementation
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
              for (int i=0; i < tempCP.length; i++)
                tempCP[i] = (ControlPoint)((ESlateHandle)objects[i]).getComponent();
              points = tempCP;
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


  // OVERRIDEs //////////////////////////////

  public String getBaseName(){
   return Res.localize("pLine");
  }

  public Shape calculateShape(){
   //System.out.println(center.getLocationX()+":"+center.getLocationY()+"-"+pointOnCircumference.getLocationX()+":"+pointOnCircumference.getLocationY());
//    return new java.awt.GeneralPath();
   return new PolyLine2D(ControlPoint.makePoint2DArray(points));
  }
}
