//Title:        ControlPoint
//Version:      29Mar2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Physics

//27Aug1999: controlPoint registers to nameService
//29Aug1999: fixed-bug: was doing a register("handle") instead of register(this,"handle") and thus was just registering a string object instead of ourselves!!!
//29Aug1999: renamed to "ControlPoint" from "handle"
//30Nov1999: using a localized base name for control points
//17May2000: fixed-bug: setLocation(int,int) wasn't setting the requested location unless the ControlPoint had listeners attatched to it!
//-Drossos Nicolas
//31May2002: set/getProperties and read/writeExternal were moved inside this class.

package gr.cti.eslate.stage;

import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.beans.PropertyVetoException;
import java.io.*;
import gr.cti.shapes.Point2DUtilities;
import gr.cti.eslate.utils.*;
import gr.cti.utils.ObjectHash;

/**
 * @version     2.0.2, 11-Jul-2006
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class ControlPoint extends BaseObject implements Externalizable
  //12Feb2000: now extending gr.cti.eslate.stage.BaseObject
{
  //16Oct1999: serial-version, so that new vers load OK
  static final long serialVersionUID = 16101999L;
  //public final static String STORAGE_VERSION="1"; //29Aug2000
  public final static int STORAGE_VERSION=1; //6/6/2002
  //have serialVersionUID for this (just in case we change its Externalizable implementation
  //in the future)
  //fields

  //21Apr2000: removed all DoublePoint2D references, while still keeping read compatibility
  //with older saved data (now saving in a Java2D independent format)
  private Point2D.Double point=new Point2D.Double(0,0);
  /**
    * The location property name.
  */
  public static final String LOCATION_PROPERTY = "location";
  public static final String LOCATION_X_PROPERTY = "locationX"; //21Apr2000
  public static final String LOCATION_Y_PROPERTY = "locationY"; //21Apr2000

  private String role = "";

  //CONSTRUCTOR
  public ControlPoint(){
    this(0,0);
  }

  public ControlPoint(Point2D p){
    this(p.getX(),p.getY());
  }

  public ControlPoint(double x,double y){
    //21Apr2000: calling setLocation instead of assigning a "new Double.Point2D(x,y)" to
    //the private "point" field
    point.setLocation(x,y);
  }

  public void setRole(String r){
      role = r;
  }

  public String getRole(){
      return role;
  }

  //OVERRIDEs
  public String getBaseName(){ //11May2000
    return Res.localize("ControlPoint");
  }

  //PERSISTENCE
  /*
    public ObjectHash getProperties(){ //19Apr2000
      ObjectHash properties=super.getProperties(); //get properties from ancestors
      //just in case we have a "bad" ancestor (the BaseComponent is supposed to never return null)
      if(properties==null)
        properties=new ObjectHash();
      //17MayApr2000: removed-old-style-saving (not depending on Java2D Point2D stucture)
      //Point2D.Double is not serializable, must wrap it in a DoublePoint2D
      //properties.put(LOCATION_PROPERTY,new gr.cti.shapes.DoublePoint2D(point));
      properties.putDouble(LOCATION_X_PROPERTY,getLocationX()); //17May2000
      properties.putDouble(LOCATION_Y_PROPERTY,getLocationY()); //17May2000
      return properties;
    }
  */

  //29Mar2001-FINAL-VERSION: compatibility with old persistence code
  public void setProperties(ObjectHash properties){
    if(properties==null)
      return;
    try{
      setName(properties.getString(NAME_PROPERTY));
    }catch(Exception e){
      setName(getBaseName());
      //will make a unique name from the BaseName must use try-catch for eacb property, since it
      //might be missing (when loading some older saved state) set default value in case this
      //property is missing (due to some or old or corrupted/wrong state loading)
    }
    //must use try-catch for each property, since it might be missing (when loading some older
    //saved state)
    try{
      //17May2000
      //batch X and Y setting in one step (generate only one change event, not two)
      setLocation(properties.getDouble(LOCATION_X_PROPERTY),properties.getDouble(LOCATION_Y_PROPERTY));
    }catch(Exception e){
      //17MayApr2000: keep read compatibilty with old saved data
      try{
        setLocation((Point2D.Double)properties.get(LOCATION_PROPERTY));
      }
      //17May2000: do call setLocation, cause it is supposed to do side-effects (e.g. notifies
      //the shape that it has changed, any attatched constraints etc.)
      catch(Exception ex){
        //17May2000: very cunning error: do show error trace in case it happens!
        ex.printStackTrace();
        //batch X and Y setting in one step (generate only one change event, not two)
        setLocation(0,0);
      }
    }
  }

  public ESlateFieldMap2 getProperties(){ //19Apr2000
    //ESlateFieldMap properties=super.getProperties(); //get properties from ancestors
    ESlateFieldMap2 properties=new ESlateFieldMap2(STORAGE_VERSION);
    properties.put("NAME_PROPERTY",getName());
    //just in case we have a "bad" ancestor (the BaseComponent is supposed to never return null)
    //if(properties==null)
    //  properties=new ESlateFieldMap(STORAGE_VERSION);

    //17MayApr2000: removed-old-style-saving (not depending on Java2D Point2D stucture)
    //Point2D.Double is not serialiable, must wrap it in a DoublePoint2D
    //properties.put(LOCATION_PROPERTY,new gr.cti.shapes.DoublePoint2D(point));
    properties.put("LOCATION_X_PROPERTY",getLocationX()); //17May2000
    properties.put("LOCATION_Y_PROPERTY",getLocationY()); //17May2000

    properties.put("ROLE",getRole());
    return properties;
  }

  public void setProperties(StorageStructure properties){ //19Apr2000
    if(properties==null)
      return;
    //super.setProperties(properties);
    //must use try-catch for eacb property, since it might be missing (when loading some older
    //saved state) set default value in case this property is missing (due to some or old or
    //corrupted/wrong state loading)
    try{
      setName((String)properties.get("NAME_PROPERTY"));
    }catch(Exception e){
      System.out.println("Exception in setProperties...");
      setName(getBaseName());//will make a unique name from the BaseName
    }

    //must use try-catch for each property, since it might be missing (when loading some older
    //saved state)
    try{
      //17May2000
      //batch X and Y setting in one step (generate only one change event, not two)
      setLocation(((Double)properties.get("LOCATION_X_PROPERTY")).doubleValue(),
                  ((Double)properties.get("LOCATION_Y_PROPERTY")).doubleValue());
    //17MayApr2000: keep read compatibilty with old saved data
    }catch(Exception e){
      try{
        setLocation((Point2D.Double)properties.get("LOCATION_PROPERTY"));
        //17May2000: do call setLocation, cause it is supposed to do side-effects (e.g.
        //notifies the shape that it has changed, any attatched constraints etc.)
      }
      catch(Exception ex){
        //17May2000: very cunning error: do show error trace in case it happens!
        ex.printStackTrace();
        //batch X and Y setting in one step (generate only one change event, not two)
        setLocation(0,0);
      }
    }
    setRole(properties.get("ROLE", ""));
  }

  //HasLocation2D
  public Point2D getLocation2D(){
    //15Nov1999: don't return a copy! Since setLocation2D is always creating a new
    //Point2D.Double, all those who got some point from us in the past and are using it are
    //not afraid of it changing (unless some other of the client touches the point's data -
    //we're not touching the old point when our location changes, we create & keep a new
    //point object)
    return point; /*new Point2D.Double(point.getX(),point.getY());*/
  }

  public void setLocation2D(Point2D p){
    //don't call "point.setLocation", our implemenation calls update() too
    setLocation(p.getX(),p.getY());
  }

  //HasAltitude
  public double getAltitude() { //2Nov1999
    return getLocationY();
  }

  public void setAltitude(double value) { //2Nov1999
    setLocationY(value);
  }

  /*
  public void setLocalLocation(ControlPoint p){
    if(parentPoint!=null)
      setLocation(parentPoint's pos + p)
    else
      setLocation(p);
  }
  public Point2D getLocalLocation(){
   return new Point2D.Double(pos - parentPoint's pos);
  }
  */

  public void setLocation(ControlPoint p){
    setLocation(p.getLocationX(),p.getLocationY());
  }

  public void setLocation(Point2D p){
    setLocation(p.getX(),p.getY());
  }

  public void setLocation(double x, double y){
    //20Feb2000: unfortunately this won't do any speedup, since all objects are property
    //listeners to their control points in order to set their object as dirty
    if ((point.getX()!=x || point.getY()!=y) && (hasVetoableChangeListeners(LOCATION_PROPERTY)
                                              || hasPropertyChangeListeners(LOCATION_PROPERTY))){
      //if not changed don't fire events, to avoid cyclic event propagation when having
      //double-way control point bindings
      //create new point, can't do else, since we need to also pass the old point to the
      //property listeners
      Point2D.Double newPoint=new Point2D.Double(x,y);
      try{
        fireVetoableChange(LOCATION_PROPERTY, point,newPoint);
        Point2D.Double oldPoint=point;
        point=newPoint;
        firePropertyChange(LOCATION_PROPERTY, oldPoint, point);
      }catch(PropertyVetoException e){}
    }
    else{
      //17May2000: must do this since while restoring from saved data the control point has
      //no property-change or vetoable-change listeners (doesn't belong to some
      //IControlPointContainer object and isn't bound by some constraint yet) and the above code
      //won't execute (optimization: access the existing Point2D object instead of creating a
      //new instance of it)
      point.setLocation(x,y);
    }
  }

  public void setLocationX(double x){
    //don't use getY(), it returns an int (don't touch the point object, call setLocation)
    setLocation(x,getLocationY());
  }

  public void setLocationY(double y){
    //don't use getX(), it returns an int (don't touch the point object, call setLocation)
    setLocation(getLocationX(),y);
  }

  public double getLocationX(){
    return point.getX();
  }

  public double getLocationY(){
    return point.getY();
  }

  public void transform(AffineTransform a){
    setLocation2D(a.transform(point,null));
  }

  public double angle(ControlPoint p){
    //!bad: .getLocation2D() return copy!
    return Point2DUtilities.getAngle(point,p.getLocation2D());
  }

  public double distance(ControlPoint p){
    return point.distance(p.getLocation2D()); //!bad: .getLocation2D() return copy!
  }

  public double distanceSq(ControlPoint p){ //1Jul1999
    return point.distanceSq(p.getLocation2D()); //!bad: .getLocation2D() return copy!
  }

  public Point2D offset(ControlPoint fromPoint){
    //!bad: .getLocation2D() return copy!
    return Point2DUtilities.getOffset(point,fromPoint.getLocation2D());
  }

  public void doOffset(Point2D offset){
    setLocation(Point2DUtilities.getOffsetedCopy(point,offset));
  }

  //BINDING
  //1Jul1999 //how will this be implemented if the "constraints" map goes to PhysicsPanel?
  public void unbindFrom(Object o){
    /*
    Constraint c=((Constraint)constraints.get(o));
    if(c!=null) { //2Jul1999: was throwing exception if not bound to Object "o"
      c.dispose(); //!!!stop listening, else they keep holding us alive (even at Java2, changeSupport uses a Vector to hold listeners, instead of a WeakVector)
      constraints.remove(o);
    }
    */
  }

  /*
  public boolean isSlaveOf(ControlPoint p){
    return (constraints.get(p)!=null) p.isMasterOf(this);
  }

  public boolean isMasterOf(ControlPoint p){
   return changeSupport.hasListener(p,LOCATION_PROPERTY);
  }
  */

  //STATIC utility methods
  public static ControlPoint[] makeControlPointArray(Point2D[] p){
    int len=p.length;
    ControlPoint[] c=new ControlPoint[len];
    for(int i=0;i<len;i++)
      c[i]=new ControlPoint(p[i]);
    return c;
  }

  public static Point2D[] makePoint2DArray(ControlPoint[] c){
    int len=c.length;
    Point2D[] p=new Point2D[len];
    for(int i=0;i<len;i++)
      p[i]=c[i].getLocation2D(); //!bad or wanted???: .getLocation2D() return copy!
    return p;
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    try{
      ESlateFieldMap2 properties=getProperties(); //25Nov1999
      if(properties!=null)
        out.writeObject(properties); //25Nov1999
    }catch(Exception e){
      e.printStackTrace();
    } //19Apr2000: added try-catch
  }

  public void readExternal(ObjectInput in)
  throws IOException, ClassNotFoundException {
    try{
      Object o=in.readObject(); //25Nov1999
      if(o instanceof StorageStructure) //29Mar2001: compatibility
        setProperties((StorageStructure)o);
      else
        setProperties((ObjectHash)o);
    }catch(Exception e){
      e.printStackTrace();
      System.err.println(getClass().getName()+"@BaseObject: Error loading properties hashtable");
    }
  }
}
