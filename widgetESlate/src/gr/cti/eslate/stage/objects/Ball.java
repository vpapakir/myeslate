//Title:        Physics
//Version:      23Apr2000
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
import gr.cti.typeArray.*;
import gr.cti.eslate.protocol.*;
import gr.cti.eslate.base.*;
import java.awt.*;

import gr.cti.eslate.utils.*;
import java.io.*;


//31Oct1999: now Ball object implements the AsBall interface (also found out that it wasn't
//declaring that it implemented HasWidth/HasHeight interfaces: now AsBall extends these)
public class Ball extends PhysicsObject implements AsBall{

  //19Oct1999: serial-version, shouldn't need it cause the ancestor (PhysicsObject) does the
  //save/load, but Java seems to need it at all descendents if we change the ancestor's code
  static final long serialVersionUID = 68128468572810510L;
  //8Nov1999: reuse a Circle2D object per Ball instance
  private Circle2D shape=new Circle2D(0,0,0);
  private ControlPoint center,pointOnCircumference;
  public static final int CP_CENTER=0;
  public static final int CP_POINTONCIRCUMFERENCE=1;

  // ActorInterface
  IntBaseArray varValues;
  AnimatedPropertyStructure animatedPropertyStructure;
  private MaleSingleIFMultipleConnectionProtocolPlug plug;
  int plugCount = 1;

  // LogoScriptable
  public String[] getSupportedPrimitiveGroups(){
    String[] p1=super.getSupportedPrimitiveGroups();
    int len=p1.length;
    String[] p2=new String[len+4];
    System.arraycopy(p1,0,p2,0,len);
    p2[len]="gr.cti.eslate.scripting.logo.RadiusPrimitives";
    p2[len+1]="gr.cti.eslate.scripting.logo.WidthPrimitives"; //30Aug1999
    p2[len+2]="gr.cti.eslate.scripting.logo.HeightPrimitives"; //30Aug1999
    p2[len+3]="gr.cti.eslate.scripting.logo.AnglePrimitives"; //23Apr2000
    return p2;
  }

  public Ball() {
    this(50,50,100);
  }

  public Ball(double radius){
    this(0,0,radius);
  }

  public Ball(double cx,double cy,double radius){
    //28Aug1999: call super in order to register as scriptable
    super();
    //for PhysicsObject's IControlPointContainer implementation
    setControlPoints(new ControlPoint[]{new ControlPoint(cx,cy), //center
                                        new ControlPoint(cx+radius,cy)});
  }

  //OVERRIDEs
  public void setControlPoints(ControlPoint[] p){ //15May2000
    center=p[CP_CENTER];
    center.setRole("CP_CENTER");
    pointOnCircumference=p[CP_POINTONCIRCUMFERENCE];
    pointOnCircumference.setRole("CP_POINTONCIRCUMFERENCE");
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
              if (((ControlPoint)((ESlateHandle)objects[0]).getComponent()).getRole().equalsIgnoreCase("CP_CENTER")){
                  tempCP[0] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
                  tempCP[1] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
              }
              else{
                  tempCP[0] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
                  tempCP[1] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
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
    return Res.localize("Ball"); //30Nov1999: localized
  }

  //30Jun1999: now caching is done at ancestor
  public Shape calculateShape(){
    //8Nov1999: minimize object allocations
    shape.setCircle(center.getLocationX(),center.getLocationY(),pointOnCircumference.getLocationX(),pointOnCircumference.getLocationY());
    return shape;
  }

  //HasRadius
  public double getRadius(){
    return center.distance(pointOnCircumference);
  }

  public void setRadius(double value){ //move pointOnCircumference on center->pointOnCircumference direction, so that |pointOnCircumference-center|=radius
    if(value!=getRadius()){ //1Jun1999: if set to current value, ignore
      double angle=center.angle(pointOnCircumference);
      pointOnCircumference.setLocation(center.getLocationX()+Math.cos(angle)*value,center.getLocationY()+Math.sin(angle)*value);
      update();
    }
  }

  // HasWidth
  public double getObjectWidth(){
    return getRadius()*2;
  }

  public void setObjectWidth(double value){
    setRadius(value/2);
  }

  // HasHeight
  public double getObjectHeight(){
    return getRadius()*2;
  }

  public void setObjectHeight(double value){
    setRadius(value/2);
  }

  //HasAngle
  protected double getRadianAngle(){ //convenience method
    return center.angle(pointOnCircumference);
  }

  public double getAngle(){ //23Apr2000 //in degrees
    return Math.toDegrees(getRadianAngle());
  }

  //23Apr2000 //in degrees //only "pointOnCircumference" moves when changing this property,
  //the other point stays fixed
  public void setAngle(double angle){
    if (angle!=getAngle()){ //if set to current value, ignore
      //rotate the movingEdge arround the fixedEdge by "newAngle-currentAngle" in radians
      AffineTransform a=AffineTransform.getRotateInstance(Math.toRadians(angle)-getRadianAngle(),
                                                    center.getLocationX(),center.getLocationY());
      pointOnCircumference.transform(a);
      update();
    }
  }

  public void createProtocolPlug () {
    try {
      plug = new MaleSingleIFMultipleConnectionProtocolPlug(handle, null, "BallPlug",new Color(0,100,255));
      plug.setNameLocaleIndependent("BallPlug"+plugCount);
      plug.setName("BallPlug"+plugCount);

      plug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e) {
          plugCount++;
          e.getOwnPlug().removeConnectionListener(this);
          createProtocolPlug();
        }
      });
      plug.addDisconnectionListener(new DisconnectionListener() {
        public void handleDisconnectionEvent(DisconnectionEvent e) {
          try {
            handle.removePlug(e.getOwnPlug());
          }catch (NoSuchPlugException nspe) {}
        }
      });

      handle.addPlug(plug);
    }catch (InvalidPlugParametersException e) {
    }catch (PlugExistsException e) {
        System.out.println("plugExists");
    }
  }

  //ESlatePart
  public ESlateHandle getESlateHandle() {
    if (handle == null){
      super.getESlateHandle();
      createProtocolPlug();
    }
    return handle;
  }


  // Actor Interface

  /**
   * Returns the values of the variables of the actor.
   * @return  The values of the variables of the actor.
   */
  public IntBaseArray getVarValues() {
    if (varValues == null) {
      varValues = new IntBaseArray();
      varValues.add((int)getLocation2D().getX());
      varValues.add((int)getLocation2D().getY());
    }
    return varValues;
  }

  /**
   * Sets the values of the actor's variables.
   * @param     varValues   The values of the actor's variables.
   * @param   animationSession The animation session to set the values.
   */
  public void setVarValues(IntBaseArray varValues, AnimationSession animationSession) {
    int i=0;
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(0)).getPropertyID())) {
      setLocation2D(new Point2D.Double(varValues.get(i), getLocation2D().getY()));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(1)).getPropertyID())) {
      setLocation2D(new Point2D.Double(getLocation2D().getX(), varValues.get(i)));
    }
    update();
  }

  /**
   * Get the animated property structure.
   * @return  The animated property structure.
   */
  public AnimatedPropertyStructure getAnimatedPropertyStructure() {
    if (animatedPropertyStructure == null) {
      animatedPropertyStructure = new AnimatedPropertyStructure();
      AnimatedPropertyDescriptor aniProDesc1 = new AnimatedPropertyDescriptor(0, "X position");
      AnimatedPropertyDescriptor aniProDesc2 = new AnimatedPropertyDescriptor(1, "Y position");
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc1);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc2);
    }
    return animatedPropertyStructure;
  }

  /**
   * Actor is active (on stage).
   */
  public void onStage(AnimationSession animationSession) {
    //setVisible(true);
  }

  /**
   * Actor is inactive (off stage).
   */
  public void offStage(AnimationSession animationSession) {
    //setVisible(false);
  }

  /**
   * Get actor name.
   */
  public String getActorName() {
    return getName();
  }

  /**
   * Get plugs number.
   */
  public int getPlugCount() {
    return plugCount;
  }
}
