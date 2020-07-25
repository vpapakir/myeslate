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
import gr.cti.eslate.stage.models.*;
import gr.cti.typeArray.*;
import gr.cti.eslate.protocol.*;
import gr.cti.eslate.base.*;
import java.awt.*;

import gr.cti.eslate.utils.*;
import java.io.*;

//now boxes implement the AsBox interface (it extends just the HasWidth & HasHeight for now)
public class Box extends PhysicsObject implements AsBox{

  static final long serialVersionUID = 1205306230022921737L; //1Jun1999
  //15Nov1999: reuse a Rectangle2D object
  private RectangularShape shape=makeCacheShape();
  protected ControlPoint center, edge;
  public static final int CP_CENTER=0;
  public static final int CP_EDGE=1;

  // ActorInterface
  IntBaseArray varValues;
  AnimatedPropertyStructure animatedPropertyStructure;
  private MaleSingleIFMultipleConnectionProtocolPlug plug;
  int plugCount = 1;

  // LogoScriptable
  public String[] getSupportedPrimitiveGroups(){
    String[] p1=super.getSupportedPrimitiveGroups();
    int len=p1.length;
    String[] p2=new String[len+2];
    System.arraycopy(p1,0,p2,0,len);
    p2[len]="gr.cti.eslate.scripting.logo.WidthPrimitives";
    p2[len+1]="gr.cti.eslate.scripting.logo.HeightPrimitives";
    return p2;
  }

  public Box() {
    this(50,50,80,80);
  }

  public Box(double side){
    this(0,0,side,side);
  }

  public Box(double cx,double cy,double width,double height){
    super(); //28Aug1999: call super in order to register as scriptable
    ControlPoint cp1 = new ControlPoint(cx,cy);
    ControlPoint cp2 = new ControlPoint(cx+width,cy+height);
    setControlPoints(new ControlPoint[]{cp1, //center
                                      cp2}); //edge
    //for PhysicsObject's IControlPointContainer implementation
  }

  //OVERRIDEs
  public void setControlPoints(ControlPoint[] p){ //15May2000
    center=p[CP_CENTER];
    center.setRole("CP_CENTER");
    edge=p[CP_EDGE];
    edge.setRole("CP_EDGE");
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
            if (((ControlPoint)((ESlateHandle)objects[0]).getComponent()).getRole().equalsIgnoreCase("CP_CENTER")){
                tempCP[CP_CENTER] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
                tempCP[CP_EDGE] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
            }
            else{
                tempCP[CP_CENTER] = (ControlPoint)((ESlateHandle)objects[1]).getComponent();
                tempCP[CP_EDGE] = (ControlPoint)((ESlateHandle)objects[0]).getComponent();
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
    return Res.localize("Box"); //30Nov1999: localized
  }

  //15Nov1999: descendents should override this to make the "cache" rectangular shape
  protected RectangularShape makeCacheShape(){
    return new Rectangle2D.Double();
  }

  //30Jun1999: now caching is done at ancestor
  public Shape calculateShape(){
    //System.out.println(center.getLocationX()+":"+center.getLocationY()+
                      //"-"+radius.getLocationX()+":"+edge.getLocationY());
    /*
    double cx=center.getLocationX();
    double cy=center.getLocationY();
    double dx=cx-edge.getLocationX();
    double dy=cy-edge.getLocationY();
    return ShapeUtilities.pointsToRectangle(cx-dx,cy-dy,cx+dx,cy+dy);
    */
    //15Nov1999: reuse a Rectangle2D object
    shape.setFrameFromCenter(center.getLocationX(),center.getLocationY(),edge.getLocationX(),
                                edge.getLocationY());
    return shape;
  }

  //HasWidth
  public double getObjectWidth(){
    return Math.abs(edge.getLocationX()-center.getLocationX())*2;
  }

  public void setObjectWidth(double value){
    //1Jun1999: if set to current value, ignore
    if(value!=getObjectWidth()){
      //28Nov1999: bug-fix: setObjectWidth(0) caused subsequent failure of setWidth(x),
      //with x<>0: this is cause NumberUtil.sign(0) or NumberUtil.sign(0) returned 0: will
      //fix that, but not using NumberUtils.sign anymore: the new code should be faster
      //bug:removed// edge.setLocation(center.getLocationX()+
      //gr.cti.utils.NumberUtil.sign(edge.getLocationX()-center.getLocationX())*value/2,
      //edge.getLocationY());

      //28Nov1999: using adapted code from setObjectHeight, since that one's setHeight(0) works OK
      //use a double local var, so that Java won't cast it to an int
      double direction=(edge.getLocationX()<center.getLocationX())?-1d:1d;
      edge.setLocation(center.getLocationX()+direction*value/2,edge.getLocationY());
      update();
    }
  }

  //HasHeight
  public double getObjectHeight(){
    return Math.abs(edge.getLocationY()-center.getLocationY())*2;
  }

  public void setObjectHeight(double value){
    //1Jun1999: if set to current value, ignore
    if(value!=getObjectHeight()){
      //use a double local var, so that Java won't cast it to an int
      double direction=(edge.getLocationY()<center.getLocationY())?-1d:1d;
      edge.setLocation(edge.getLocationX(),center.getLocationY()+direction*value/2);
      update();
    }
  }

  public void createProtocolPlug () {
    try {
      plug = new MaleSingleIFMultipleConnectionProtocolPlug(handle, null, "BoxPlug",new Color(0,100,255));
      plug.setNameLocaleIndependent("BoxPlug"+plugCount);
      plug.setName("BoxPlug"+plugCount);

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
      varValues.add((int)edge.getLocationX());
      varValues.add((int)edge.getLocationY());
      varValues.add((int)center.getLocationX());
      varValues.add((int)center.getLocationY());
      varValues.add((int)getObjectWidth());
      varValues.add((int)getObjectHeight());
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
      edge.setLocationX(varValues.get(i));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(1)).getPropertyID())) {
      edge.setLocationY(varValues.get(i));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(2)).getPropertyID())) {
      center.setLocationX(varValues.get(i));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(3)).getPropertyID())) {
      center.setLocationY(varValues.get(i));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(4)).getPropertyID())) {
      setObjectWidth(varValues.get(i));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(5)).getPropertyID())) {
      setObjectHeight(varValues.get(i));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(6)).getPropertyID())) {
      setLocation2D(new Point2D.Double(varValues.get(i), getLocation2D().getY()));
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(7)).getPropertyID())) {
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
      AnimatedPropertyDescriptor aniProDesc1 = new AnimatedPropertyDescriptor(0, "Edge X");
      AnimatedPropertyDescriptor aniProDesc2 = new AnimatedPropertyDescriptor(1, "Edge Y");
      AnimatedPropertyDescriptor aniProDesc3 = new AnimatedPropertyDescriptor(2, "Center X");
      AnimatedPropertyDescriptor aniProDesc4 = new AnimatedPropertyDescriptor(3, "Center Y");
      AnimatedPropertyDescriptor aniProDesc5 = new AnimatedPropertyDescriptor(4, "Width");
      AnimatedPropertyDescriptor aniProDesc6 = new AnimatedPropertyDescriptor(5, "Height");
      AnimatedPropertyDescriptor aniProDesc7 = new AnimatedPropertyDescriptor(6, "X position");
      AnimatedPropertyDescriptor aniProDesc8 = new AnimatedPropertyDescriptor(7, "Y position");
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc1);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc2);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc3);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc4);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc5);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc6);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc7);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc8);
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
