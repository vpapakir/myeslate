//Version: 29Mar2001

package gr.cti.eslate.stage.constraints.base;

import gr.cti.eslate.stage.constraints.exception.*;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.stage.ControlPoint;
import gr.cti.utils.ObjectHash;
import gr.cti.eslate.utils.*;

public abstract class AbstractPointDistanceFromPointConstraint
 extends AbstractMasterPointSlavePointConstraint //8Aug1999: changed superclass
{

 static final long serialVersionUID = 1109286116471744579L; //8Jun2000: serial-version, so that new vers load OK
 //public final static String STORAGE_VERSION="1"; //29Aug2000
 public final static int STORAGE_VERSION=1; //6/6/2002
 protected double distance; //storable

 public void setDistance(double value){ //8Jun2000
  distance=value;
 }

//maybe when slave tries to get an invalid pos, try to place the slave at the new point, then enforce constraint, so that the slave can to be dragged arround the center point???

 protected void storeMembers(Object[] o) throws InvalidConstraintMembersException{ //29Jul1999
  super.storeMembers(o);
  distance=getMasterPoint().distance(getSlavePoint());
 }

 protected void force(PropertyChangeEvent e){ //the event tells us which constraint member has changed, but since we only listen for slave changes and got one slave we don't need to check "e"
  if(!holds()){ //8Aug1999: use this force routine only if it doesn't hold (so that a min constraint won't have the slave follow the master when the master gets away from it, the same goes for the max constraint when the master gets near the slave)
   //should place the slave point on the master-slave axis, at the same direction, but at "distance" distance
   ControlPoint slavePoint=getSlavePoint();
   Point2D m=getMasterPoint().getLocation2D();
   Point2D s=slavePoint.getLocation2D();
   double curdist=m.distance(s);
   if (curdist!=0){ //if the master-slave form a line and are not just two coinciding points
    double d=distance/curdist;  //8Aug1999: placed this in a "if(curdist!=0)" block, cause if master conincided with slave, a "division by zero" error would be thrown
    slavePoint.setLocation(
     (s.getX()-m.getX())*d+m.getX(),
     (s.getY()-m.getY())*d+m.getY()
    );
   } //if dist==0 can't force
  }
 }

 //

 public static final String DISTANCE_PROPERTY="distance"; //8Jun2000

/*
 public ObjectHash getProperties(){
  ObjectHash properties=super.getProperties(); //get any ancestor properties too
  if(properties==null) properties=new ObjectHash();

  properties.putDouble(DISTANCE_PROPERTY,distance);

  return properties;
 } //descendents should override that to return a hashtable with all the properties' name-value pairs (they should call super.getProperties first and add entries to the hashtable it returns and return that one - if super.getProperties() returns null, then create and return a new Hashtable instance)
*/

 public void setProperties(ObjectHash properties){ //29Mar2001-FINAL-VERSION: compatibility with old persistence code
  if(properties==null) return;
  super.setProperties(properties); //allow ancestor's to set their properties too from the same hash

  try{setDistance(properties.getDouble(DISTANCE_PROPERTY));}
  catch(Exception e){ setDistance(0); }


 } //descendents should override that to set all the properties for the hashtable with the name-value pairs (they should call super.setProperties too)

 public ESlateFieldMap2 getProperties(){
  ESlateFieldMap2 properties=super.getProperties(); //get any ancestor properties too
  if(properties==null)
    properties=new ESlateFieldMap2(STORAGE_VERSION);

  properties.put("DISTANCE_PROPERTY",distance);

  return properties;
 } //descendents should override that to return a hashtable with all the properties' name-value pairs (they should call super.getProperties first and add entries to the hashtable it returns and return that one - if super.getProperties() returns null, then create and return a new Hashtable instance)

 public void setProperties(StorageStructure properties){
  if(properties==null) return;
  super.setProperties(properties); //allow ancestor's to set their properties too from the same hash

  setDistance(properties.get("DISTANCE_PROPERTY",0));


 } //descendents should override that to set all the properties for the hashtable with the name-value pairs (they should call super.setProperties too)


}
