//Title:        Physics
//Version:      15May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

package gr.cti.eslate.stage.objects;

import java.awt.Shape;
import java.awt.geom.*;

import gr.cti.eslate.stage.*;

import gr.cti.eslate.utils.*;
import java.io.*;
import gr.cti.eslate.base.*;

/**
 * @version     2.0.0, 25-May-2006
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class QuadraticCurve extends PhysicsObject{ //30Jun1999


  static final long serialVersionUID = 68128468572810510L; //19Oct1999: serial-version, shouldn't need it cause the ancestor (PhysicsObject) does the save/load, but Java seems to need it at all descendents if we change the ancestor's code
  private ControlPoint start,control,end;

  public static final int CP_START=0;
  public static final int CP_END=1;
  public static final int CP_CONTROL=2;

  public QuadraticCurve() {
   this(50,50,100,100,150,70);
  }

  public QuadraticCurve(double startx,double starty, double controlx,double controly, double endx,double endy){
   super(); //28Aug1999: call super in order to register as scriptable
   setControlPoints(new ControlPoint[]{
    new ControlPoint(startx,starty), //start
    new ControlPoint(endx,endy), //end
    new ControlPoint(controlx,controly), //control
   }); //for PhysicsObject's IControlPointContainer implementation
  }

  // OVERRIDEs //////////////////////////////

  public void setControlPoints(ControlPoint[] p){ //15May2000
   start=p[CP_START];
   start.setRole("CP_START");
   end=p[CP_END];
   end.setRole("CP_END");
   control=p[CP_CONTROL];
   control.setRole("CP_CONTROL");
   //must call this after we do get successfully all the needed control points
   super.setControlPoints(p);
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

              for (int i=0; i < objects.length; i++){
                  if (((ControlPoint)((ESlateHandle)objects[i]).getComponent()).getRole().equalsIgnoreCase("CP_START"))
                      tempCP[CP_START] = (ControlPoint)((ESlateHandle)objects[i]).getComponent();
                  else if (((ControlPoint)((ESlateHandle)objects[i]).getComponent()).getRole().equalsIgnoreCase("CP_END"))
                      tempCP[CP_END] = (ControlPoint)((ESlateHandle)objects[i]).getComponent();
                  else
                      tempCP[CP_CONTROL] = (ControlPoint)((ESlateHandle)objects[i]).getComponent();
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


  public String getBaseName()  //30Jun1999
  {
   return Res.localize("quadCurve");
  }

  public Shape calculateShape(){ //30Jun1999: now caching is done at ancestor
   //System.out.println(center.getLocationX()+":"+center.getLocationY()+"-"+pointOnCircumference.getLocationX()+":"+pointOnCircumference.getLocationY());
    return new QuadCurve2D.Double(
     start.getLocationX(),start.getLocationY(),
     control.getLocationX(),control.getLocationY(),
     end.getLocationX(),end.getLocationY()
    );
  }
}
