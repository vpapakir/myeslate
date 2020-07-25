//either remove the MaximumLength property or setup a constraint to enforce it...
//...new springs would start with a negative MaximumLength (this would mean infinite length allowed - must tell in doc that maxlength<0 means inf)
//...save/load would keep/recreate the constraint (constraint not held with other PhysicsPanel's constriants, but held by the Spring object)

//Title:        Spring model
//Version:      29Mar2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

package gr.cti.eslate.stage.objects;

import java.io.*;
import java.awt.Shape;
import java.awt.geom.*;

import gr.cti.eslate.stage.*;
import gr.cti.eslate.stage.models.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.base.*;

import gr.cti.utils.ObjectHash;

public class Spring extends PhysicsObject implements AsSpring{
  //12Oct1999: serial-version, so that new vers load OK
  static final long serialVersionUID = 12101999L;
  //public final static String STORAGE_VERSION="1"; //29Aug2000
  public final static int STORAGE_VERSION=1; //6/6/2002
  private ControlPoint fixedEdge,movingEdge;

  public static final int CP_FIXEDEDGE=0;
  public static final int CP_MOVINGEDGE=1;

  //property data introduced in class Spring//
  private double springConstant;
  private double naturalLength;
  private double maximumLength;

  public static final String SPRING_CONSTANT_PROPERTY="springConstant";
  public static final String NATURAL_LENGTH_PROPERTY="naturalLength";
  public static final String MAXIMUM_LENGTH_PROPERTY="maximumLength";


  // LogoScriptable ///////////
  public String[] getSupportedPrimitiveGroups(){
   String[] p1=super.getSupportedPrimitiveGroups();
   int len=p1.length;
   String[] p2=new String[len+3]; //2Nov1999: fixed, adding 3 new entries, not just 2
   System.arraycopy(p1,0,p2,0,len);
   p2[len]="gr.cti.eslate.scripting.logo.SpringPrimitives";
   p2[len+1]="gr.cti.eslate.scripting.logo.LengthPrimitives"; //30Aug1999
   p2[len+2]="gr.cti.eslate.scripting.logo.AnglePrimitives"; //31Oct1999 //2Nov1999: fixed: this was replacing the SpringPrimitives entry
   return p2;
  }

  public Spring() {
   this(50,50,40);
  }

  public Spring(double length){
   this(50,50,length);
  }

  public Spring(double sx,double sy,double length){
   super(); //28Aug1999: call super in order to register as scriptable
   setControlPoints(new ControlPoint[]{
    new ControlPoint(sx,sy), //fixedEdge
    new ControlPoint(sx+length,sy) //movingEdge
   }); //for PhysicsObject's IControlPointContainer implementation
  }

  // AsSpring
  public double getSpringConstant(){
    return springConstant;
  }
  public void setSpringConstant(double value){
    springConstant=value;
  }
  public double getNaturalLength(){
    return naturalLength;
  }
  public void setNaturalLength(double value){
    naturalLength=value;
  }
  public double getMaximumLength(){
    return maximumLength;
  }
  public void setMaximumLength(double value){
    maximumLength=value;
  }
  public double getLength(){
   return fixedEdge.distance(movingEdge);
  }

  //move movingEdge on fixedEdge->movingEdge direction, so that |movingEdge-fixedEdge|=spring length
  public void setLength(double value){
   //System.out.println("Spring:setLength");
   if (value!=getLength()){ //1Jun1999: if set to current value, ignore
    double angle=fixedEdge.angle(movingEdge);
    movingEdge.setLocation(fixedEdge.getLocationX()+Math.cos(angle)*value,fixedEdge.getLocationY()+Math.sin(angle)*value);
    update();
   }
  }

  public double getDisplacement(){ return getLength()-getNaturalLength(); } //2Nov1999: calling property "Displacement"
  public void setDisplacement(double value){
   if(value!=getDisplacement()){
    setLength(getNaturalLength()+value); //Length=NaturalLength+dLength
   }
  }

  // AsSpring/HasAngle
  protected double getRadianAngle(){ //convenience method
   return fixedEdge.angle(movingEdge);
  }

  public double getAngle(){ //31Oct1999 //in degrees
   return /*NumberUtil*/Math.toDegrees(getRadianAngle());
  }

  public void setAngle(double angle){ //31Oct1999 //in degrees //only movingEdge moves when changing this property, the other point stays fixed //21Dec1999: fixed so that movingEdge rotates and not just moves up-down
   if (angle!=getAngle()){ //if set to current value, ignore
    AffineTransform a=AffineTransform.getRotateInstance(/*NumberUtil*/Math.toRadians(angle)-getRadianAngle(),fixedEdge.getLocationX(),fixedEdge.getLocationY()); //rotate the movingEdge arround the fixedEdge by "newAngle-currentAngle" in radians
    movingEdge.transform(a);
    update();
   }
  }


  // OVERRIDEs //////////////////////////////

  public void setControlPoints(ControlPoint[] p){ //15May2000
   fixedEdge=p[CP_FIXEDEDGE];
   fixedEdge.setRole("CP_FIXEDEDGE");
   movingEdge=p[CP_MOVINGEDGE];
   movingEdge.setRole("CP_MOVINGEDGE");
   super.setControlPoints(p); //must call this after we do get successfully all the needed control points
  }

  public String getBaseName(){ //30Jun1999
   return Res.localize("Spring"); //30Nov1999: localized
  }

  public Shape calculateShape(){ //30Jun1999: now caching is done at ancestor
   //System.out.println(center.getLocationX()+":"+center.getLocationY()+"-"+radius.getLocationX()+":"+radius.getLocationY());
   //System.out.println("Calculating spring's shape");
   return gr.cti.shapes.Spring.makeSpring(fixedEdge.getLocation2D(),movingEdge.getLocation2D(),10/*#spurs+1*/); //must provide full path for gr.cti.shapes.Spring, else it gets mixed with this class which is also called Spring
  }


 public void setProperties(ObjectHash properties){ //29Mar2001-FINAL-VERSION: compatibility with old persistence code
  if(properties==null) return;
  super.setProperties(properties); //allow ancestor's to set their properties too from the same hash
  try{setSpringConstant(properties.getDouble(SPRING_CONSTANT_PROPERTY));}catch(Exception e){setSpringConstant(0);} //must use try-catch for eacb property, since it might be missing (when loading some older saved state)
  try{setNaturalLength(properties.getDouble(NATURAL_LENGTH_PROPERTY));}catch(Exception e){setNaturalLength(0);} //explicitly set default values if properties are missing (that is when older-saved-state is loaded): this is because readExternal might be called on a preexisting and not a newly created instance and properties might have been set with values other than the defaults which are set at new instance creation
  //try{setMaximumLength(properties.getDouble(MAXIMUM_LENGTH_PROPERTY));}catch(Exception e){setMaximumLength(0);}
 }

 //

 public ESlateFieldMap2 getProperties(){ //25Feb2000: using new storage mechanism, old Spring data won't load anymore
  ESlateFieldMap2 properties=super.getProperties(); //get properties from ancestors
  //just in case we have a "bad" ancestor (the BaseComponent is supposed to never return null)
  if(properties==null)
    properties=new ESlateFieldMap2(STORAGE_VERSION);
  properties.put("SPRING_CONSTANT_PROPERTY",getSpringConstant());
  properties.put("NATURAL_LENGTH_PROPERTY",getNaturalLength());
  return properties;
 }

 public void setProperties(StorageStructure properties){
      if(properties==null)
        return;

      super.setProperties(properties);
      setSpringConstant(properties.get("SPRING_CONSTANT_PROPERTY",0.0));
      setNaturalLength(properties.get("NATURAL_LENGTH_PROPERTY",0.0));

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
              if (((ControlPoint)((ESlateHandle)objects[0]).getComponent()).getRole().equalsIgnoreCase("CP_FIXEDEDGE")){
                  tempCP[CP_FIXEDEDGE] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
                  tempCP[CP_MOVINGEDGE] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
              }
              else{
                  tempCP[CP_FIXEDEDGE] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
                  tempCP[CP_MOVINGEDGE] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
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


  // Cloning //

  public Object clone() throws CloneNotSupportedException {
    Spring o=(Spring)super.clone();
    o.setSpringConstant(getSpringConstant());
    o.setNaturalLength(getNaturalLength());
    o.setMaximumLength(getMaximumLength());
    return o;
  }
}
