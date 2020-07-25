//Title:        PhysicsObject
//Version:      29Mar2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

package gr.cti.eslate.stage.objects;
import gr.cti.utils.ObjectHash;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import gr.cti.shapes.*;
import java.awt.geom.Point2D;
import gr.cti.eslate.base.*;
import gr.cti.eslate.stage.ControlPoint;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.stage.models.AsPhysicsObject;
import gr.cti.eslate.protocol.*;
import gr.cti.typeArray.*;

//26Nov1999: moved all functionality independent of Physics to the new StageObject ancestor
//29Oct1999: moved all base models that make a PhysicsObject into one interface that extends
//them all
/**
 * @version     2.0.2, 11-Jul-2006
 */
public abstract class PhysicsObject extends SceneObject implements Externalizable,
                                                                AsPhysicsObject, ActorInterface{

  //1Jun1999: serial-version, so that new vers load OK
  static final long serialVersionUID = 68128468572810510L;
  //public final static String STORAGE_VERSION="2";
  public final static int STORAGE_VERSION=2;
  private double mass;
  //12Oct1999: now initializing velocity property (if VELOCITY primitive was called from Logo
  //for a new object, a null pointer exception was thrown on the console)
  private DoublePoint2D velocity=new DoublePoint2D(0,0);
  //12Oct1999: now initializing velocity property (if ACCELERATION primitive was called from
  //Logo for a new object, a null pointer exception was thrown on the console)
  private DoublePoint2D acceleration=new DoublePoint2D(0,0);
  private Point2D.Double deferredLocation = null;
  public static final String MASS_PROPERTY="mass";
  public static final String VELOCITY_PROPERTY="velocity";
  public static final String ACCELERATION_PROPERTY="acceleration";
  public static final String LOCATION_PROPERTY = "location";
  public static final String LOCATION_X_PROPERTY = "locationX";
  public static final String LOCATION_Y_PROPERTY = "locationY";

  private ArrayList<ActorNameListener> nameListeners =
    new ArrayList<ActorNameListener>();

  //30Jun1999: override this to provide a base name for a descendent
  public abstract String getBaseName();

  //CONSTRUCTOR
  //24Feb2000: added an empty constructor, might be useful when cloning
  public PhysicsObject(){}

  //LogoScriptable
  public String[] getSupportedPrimitiveGroups(){
    String[] p1=super.getSupportedPrimitiveGroups();
    int len=p1.length;
    String[] p2=new String[len+6];
    System.arraycopy(p1,0,p2,0,len);
    p2[len]="gr.cti.eslate.scripting.logo.KineticEnergyPrimitives";
    p2[len+1]="gr.cti.eslate.scripting.logo.Velocity2DPrimitives";
    p2[len+2]="gr.cti.eslate.scripting.logo.Acceleration2DPrimitives";
    p2[len+3]="gr.cti.eslate.scripting.logo.MassPrimitives";
    p2[len+4]="gr.cti.eslate.scripting.logo.AppliedForcePrimitives";  //2Nov1999
    p2[len+5]="gr.cti.eslate.scripting.logo.AltitudePrimitives";      //2Nov1999
    return p2;
  }

  //HasMass
  public double getMass(){
    return mass;
  }
  public void setMass(double value){
    mass=value;
  }

  // HasVelocity
  public DoublePoint2D getVelocity(){
    return velocity;
  }
  public void setVelocity(DoublePoint2D value){
    velocity=value;
  }

  /*
    public void setVelocityPolar(DoublePoint2D value){ //1Oct1999
      double order=value.getX();
      double angle=value.getY();
      velocity=value;
      velocity.setX(order*Math.cos(angle)); //??? no setX ???
      velocity.setY(order*Math.sin(angle)); //??? no setY ???
    }
  */

  //HasAppliedForce
  public Point2D getAppliedForce(){
    return Point2DUtilities.getScaledCopy(acceleration,getMass());
  } //31Oct1999

  public void setAppliedForce(Point2D force){ //31Oct1999
    double mass=getMass();
    //14Dec1999: now checking for mass==0 to set AppliedForce to zero
    if(mass!=0){
      double inverseOfMass=1/getMass();
      setAcceleration(new DoublePoint2D(force.getX()*inverseOfMass,force.getY()*inverseOfMass));
    }else
      setAcceleration(new DoublePoint2D(0,0)); //or (+-INF,+-INF) ???
  }

  //HasAcceleration
  public DoublePoint2D getAcceleration(){
    return acceleration;
  }

  public void setAcceleration(DoublePoint2D value){
    acceleration=value;
  }

  //HasKineticEnergy (calculated)
  //16Dec1999-fixed: was returning a wrong result (was using distance instead of distanceSq)
  public double getKineticEnergy(){
    return 0.5d*getMass()*getVelocity().distanceSq(0,0);
  }

  //HasAltitude (calculated)
  //2Nov1999 (speaking directly to the 1st control point, instead of doing getLocation2D().getY(),
  //to avoid the creation of a temporary Point2D object for the location)
  public double getAltitude() {
    ControlPoint[] p=getControlPoints();
    if (p!=null && p.length>0)
      return p[0].getAltitude();
    else
      return 0;
  }

  public void setAltitude(double value) { //2Nov1999
    //17May2000: must move all the control points and not just the 1st one, so we must call
    //the "SceneObject.translate" method!
    translate(0,value-getAltitude());
  }


  public ESlateFieldMap2 getProperties(){
      ESlateFieldMap2 properties=new ESlateFieldMap2(STORAGE_VERSION);
      properties.put("PreparedForCopy", isPreparedForCopy());

      properties.put("NAME_PROPERTY",getName());
      properties.put("COLOR_PROPERTY",getColor());

      //Point2D.Double is not serializable, must wrap it in a DoublePoint2D
      //properties.put(LOCATION_PROPERTY,new gr.cti.shapes.DoublePoint2D(point))
;
      Point2D location = getLocation2D();
      properties.put(LOCATION_X_PROPERTY, location.getX());
      properties.put(LOCATION_Y_PROPERTY, location.getY());


      Image img=getImage();
      if(img!=null)
        properties.put("IMAGE_PROPERTY",new NewRestorableImageIcon(img));
      //25Nov1999: descendents should override that to return a hashtable with all the properties'
      //name-value pairs (they should call super.getProperties first and add entries to the hashtable
      //it returns and return that one - if super.getProperties() returns null, then create and
      //return a new Hashtable instance)
      properties.put("MASS_PROPERTY",getMass());
      properties.put("VELOCITY_PROPERTY",getVelocity());
      properties.put("ACCELERATION_PROPERTY",getAcceleration());

      // if it is not prepared for copy then store all the child handles
      if (!isPreparedForCopy()){
        // Check if there are any children of type ControlPoint.
        // If there are, then save them using saveChildren().
        ESlateHandle[] handles = handle.toArray();
        ArrayList<ESlateHandle> handlesToSave = new ArrayList<ESlateHandle>();
        for (int i=0; i < handles.length; i++) {
          if (ControlPoint.class.isAssignableFrom(handles[i].getComponent().getClass())) {
            handlesToSave.add(handles[i]);
          }
        }
        if (handlesToSave.size() != 0){
          ESlateHandle[] handlesToSaveArray = new ESlateHandle[handlesToSave.size()];
          for (int i=0; i < handlesToSaveArray.length; i++){
            handlesToSaveArray[i] = handlesToSave.get(i);
          }
          try{
            handle.saveChildren(properties, "children", handlesToSaveArray);
          }catch(IOException e){
            e.printStackTrace();
          }
        }
      }
      // else store the control points just as objects, because during the paste process these
      // objects will be assigned a handle in the new object.
      else{
        properties.put("CONTROL_POINTS_PROPERTY",getControlPoints());
      }
      return properties;
  }

  public void setProperties(StorageStructure properties){
      if(properties==null)
        return;
      try{
        setName((String)properties.get("NAME_PROPERTY"));
      }catch(Exception e){
        System.out.println("Exception in setProperties...");
        setName(getBaseName())/*will make a unique name from the BaseName*/;
      }

      if (Integer.valueOf(properties.getDataVersion()).intValue() < 2){
        ControlPoint[] tempControlPoints = (ControlPoint[])properties.get("CONTROL_POINTS_PROPERTY");

        for (int i=0; i < tempControlPoints.length; i++)
            getESlateHandle().add(tempControlPoints[i].getESlateHandle());
          //handle.add(tempControlPoints[i].getESlateHandle());
        setControlPoints(tempControlPoints);

        //setControlPoints((ControlPoint[])properties.get("CONTROL_POINTS_PROPERTY"));
      }
      else{
        // Properties after changing internal objects to handles. This means that we have to remove
        // the control point's handles of the Scene that had been added in the constructor, as well
        // as the control Points themselves, and then retrieve the handles using the
        //restoreChildObjects method. All the control point objects are respectively retieved from
        // their handles and finally added to physics object.
/*        for (int i=handle.getChildHandles().length-1; i >= 0; i--)
          handle.remove(handle.getChildHandles()[i]);
        try{
          Object[] objects = getESlateHandle().restoreChildObjects((ESlateFieldMap2)properties, "children");
          ControlPoint[] tempCP = new ControlPoint[objects.length];
          for (int i=0; i < tempCP.length; i++)
            tempCP[i] = (ControlPoint)((ESlateHandle)objects[i]).getComponent();
            //tempCP[i] = (ControlPoint)((ESlateHandle)objects[tempCP.length-1-i]).getComponent();
          setControlPoints(tempCP);
        }catch(IOException e){
          e.printStackTrace();
        }*/
      }

      try{
        setColor((Color)properties.get("COLOR_PROPERTY"));
      }catch(Exception e){
        setColor(Color.red);
      }

      Object im = properties.get("IMAGE_PROPERTY");
      if (im!=null){
        if (im instanceof ImageIcon)
          setImage((ImageIcon)im);
        else
          setImage(((NewRestorableImageIcon)im).getImage());
      }

      try{
        setMass(((Double)properties.get("MASS_PROPERTY")).doubleValue());
      }catch(Exception e){
        e.printStackTrace(); setMass(10.0);/*?10kilos*/
        //must use try-catch for eacb property, since it might be missing (when loading some
        //older saved state)
      }
      try{
        setVelocity((DoublePoint2D)properties.get("VELOCITY_PROPERTY"));
      }catch(Exception e){
        setVelocity(new DoublePoint2D(0,0));
        //explicitly set default values if properties are missing (that is when older-saved-state
        //is loaded): this is because readExternal might be called on a preexisting and not a
        //newly created instance and properties might have been set with values other than the
        //defaults which are set at new instance creation
      }
      try{
        setAcceleration((DoublePoint2D)properties.get("ACCELERATION_PROPERTY"));
      }catch(Exception e){
        setAcceleration(new DoublePoint2D(0,0));
      }
      Point2D.Double loc;
      try{
        loc = new Point2D.Double(
          properties.getDouble(LOCATION_X_PROPERTY),
          properties.getDouble(LOCATION_Y_PROPERTY)
        );
      }catch(Exception e){
        loc = new Point2D.Double(0.0, 0.0);
      }
      if (getLocation2D() == null) {
        // Component has not been completely initialized. Store the location
        // for later initialization.
        setDeferredLocation2D(loc);
      }else{
        setLocation2D(loc);
      }
  }

  private void setDeferredLocation2D(Point2D.Double loc)
  {
    deferredLocation = loc;
  }

  public void restoreDeferredLocation()
  {
    if (deferredLocation != null) {
      setLocation2D(deferredLocation);
      deferredLocation = null;
    }
  }

  public void setProperties(ObjectHash properties){
      if(properties==null)
        return;
      //allow ancestor's to set their properties too from the same hash
      try{
        setName(properties.getString(NAME_PROPERTY));
      }catch(Exception e){
        setName(getBaseName());
      }
      try{
        setControlPoints((ControlPoint[])properties.get(CONTROL_POINTS_PROPERTY));
      }catch(Exception e){
        setControlPoints(null);
      }
      try{
        setColor((Color)properties.get(COLOR_PROPERTY));
      }catch(Exception e){
        setColor(Color.red);
      }
      Object im = properties.get(IMAGE_PROPERTY);
      if (im != null){
        if (im instanceof ImageIcon)
          setImage((ImageIcon)im);
        else
          setImage(((NewRestorableImageIcon)im).getImage());
      }
      //must use try-catch for eacb property, since it might be missing (when loading some
      //older saved state)
      try{
        setMass(properties.getDouble(MASS_PROPERTY));
      }catch(Exception e){
        setMass(10);/*?10kilos*/
      }
      //explicitly set default values if properties are missing (that is when older-saved-state
      //is loaded): this is because readExternal might be called on a preexisting and not a newly
      //created instance and properties might have been set with values other than the defaults
      //which are set at new instance creation
      try{
        setVelocity((DoublePoint2D)properties.get(VELOCITY_PROPERTY));
      }catch(Exception e){
        setVelocity(new DoublePoint2D(0,0));
      }
      try{
        setAcceleration((DoublePoint2D)properties.get(ACCELERATION_PROPERTY));
      }catch(Exception e){
        new DoublePoint2D(0,0);
      }
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    removeAllControlPoints();
    Object o=in.readObject();
    if(o instanceof StorageStructure){ //29Mar2001: compatibility
      StorageStructure properties = (StorageStructure)o;
      setProperties(properties);
    }
    else{
      ObjectHash properties = (ObjectHash)o;
      setProperties(properties);
    }
  }

  public void writeExternal(ObjectOutput out) throws IOException {
      ESlateFieldMap2 properties=getProperties();
      out.writeObject(properties);
  }

  //ESlatePart
  public ESlateHandle getESlateHandle() {
    if (handle == null){
      super.getESlateHandle();
      ControlPoint[] cp = getControlPoints();
      if (cp != null){
        for (int i=0; i < cp.length; i++)
          handle.add(cp[i].getESlateHandle());
      }
    }
    return handle;
  }

  //Cloning
  public Object clone() throws CloneNotSupportedException {
    PhysicsObject o=(PhysicsObject)super.clone();
    o.setMass(getMass());
    o.setVelocity(o.getVelocity());
    o.setAppliedForce(o.getAppliedForce());
    o.setAcceleration(o.getAcceleration());
    return o;
  }

  // Actor Interface

  /**
   * Returns the values of the variables of the actor.
   * @return  The values of the variables of the actor.
   */
  public IntBaseArray getVarValues() {
    /*if (varValues == null) {
      varValues = new IntBaseArray(varCount);
      varValues.add(x);
      varValues.add(y);
      varValues.add(width);
      varValues.add(height);
    }
    return varValues;*/
    return null;
  }

  /**
   * Sets the values of the actor's variables.
   * @param     varValues   The values of the actor's variables.
   * @param   animationSession The animation session to set the values.
   */
  public void setVarValues(IntBaseArray varValues, AnimationSession animationSession) {
    /*int oldX = x;
    int oldY = y;
    int oldWidth = width;
    int oldHeight = height;
    int i=0;
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(0)).getPropertyID())) {
      x = varValues.get(i);
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(1)).getPropertyID())) {
      y = varValues.get(i);
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(2)).getPropertyID())) {
      width = varValues.get(i);
      i++;
    }
    if (animationSession.isAnimated(((AnimatedPropertyDescriptor)animationSession.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(3)).getPropertyID())) {
      height = varValues.get(i);
    }
    setBounds(x,y,width,height);
    display.repaint(oldX,oldY,oldWidth,oldHeight);*/
  }

  /**
   * Get the animated property structure.
   * @return  The animated property structure.
   */
  public AnimatedPropertyStructure getAnimatedPropertyStructure() {
    /*if (animatedPropertyStructure == null) {
      animatedPropertyStructure = new AnimatedPropertyStructure();
      AnimatedPropertyDescriptor aniProDesc1 = new AnimatedPropertyDescriptor(0, display.resources.getString("x"));
      AnimatedPropertyDescriptor aniProDesc2 = new AnimatedPropertyDescriptor(1, display.resources.getString("y"));
      AnimatedPropertyDescriptor aniProDesc3 = new AnimatedPropertyDescriptor(2, display.resources.getString("width"));
      AnimatedPropertyDescriptor aniProDesc4 = new AnimatedPropertyDescriptor(3, display.resources.getString("height"));
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc1);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc2);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc3);
      animatedPropertyStructure.addAnimatedPropertyDescriptor(aniProDesc4);
    }
    return animatedPropertyStructure;*/
    return null;
  }

  /**
   * Actor is active (on stage).
   */
  public void onStage(AnimationSession animationSession) {
    System.out.println("here");//setVisible(true);
  }

  /**
   * Actor is inactive (off stage).
   */
  public void offStage(AnimationSession animationSession) {
    System.out.println("1");
    //setVisible(false);
  }

  /**
   * Get actor name.
   */
  public String getActorName() {
    //return display.resources.getString("button1Name");
    return "p";
  }

  /**
   * Get plugs number.
   */
  public int getPlugCount() {
    //return plugCount;
    return 0;
  }

  /**
   * Add a listener for actor name events.
   * @param   listener        The listener to add.
   */
  public void addActorNameListener(ActorNameListener listener)
  {
    synchronized (nameListeners) {
      if (!nameListeners.contains(listener)) {
        nameListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from actor's name events.
   * @param   listener        The listener to remove.
   */
  public void removeActorNameListener(ActorNameListener listener)
  {
    if (nameListeners != null) {
      synchronized (nameListeners) {
        int i = nameListeners.indexOf(listener);
        if (i >= 0) {
          nameListeners.remove(i);
        }
      }
    }
  }

  /**
   * Fires all listeners registered for actor's name events.
   */
  @SuppressWarnings(value={"unchecked"})
  public void fireActorNameListeners(String actorName)
  {
    ArrayList listeners;
    if (nameListeners != null && nameListeners.size() > 0) {
      synchronized (nameListeners) {
        listeners = (ArrayList<ActorNameListener>)(nameListeners.clone());
      }
      int nListeners = listeners.size();
      if (nListeners > 0) {
        ActorNameEvent e = new ActorNameEvent(this, actorName);
        for (int i=0; i<nListeners; i++) {
          ActorNameListener l = nameListeners.get(i);
          l.actorNameChanged(e);
        }
      }
    }
  }

  /**
   * Sets the object's name, invoking any registered actor name listeners.
   * @param     name    The object's name.
   */
  public void setName(String name)
  {
    super.setName(name);
    fireActorNameListeners(name);
  }
}
