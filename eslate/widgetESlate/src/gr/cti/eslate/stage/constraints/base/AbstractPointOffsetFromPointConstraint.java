//Version: 29Mar2001

package gr.cti.eslate.stage.constraints.base;

import gr.cti.eslate.stage.constraints.exception.*;

import gr.cti.shapes.DoublePoint2D;

import gr.cti.utils.ObjectHash;
import gr.cti.eslate.utils.*;

public abstract class AbstractPointOffsetFromPointConstraint
 extends AbstractMasterPointSlavePointConstraint //6Aug1999
{

 static final long serialVersionUID = -2718326752718929192L; //8Jun2000: serial-version, so that new vers load OK

 //public final static String STORAGE_VERSION="1"; //29Aug2000
 public final static int STORAGE_VERSION=1; //6/6/2002
 protected DoublePoint2D offset; //20Oct1999:changed from "Point2D" to "DoublePoint2D" so that this field is storable

 public void setOffset(double x,double y){ //8Jun2000
  if(offset==null) offset=new DoublePoint2D(x,y);
  else offset.setLocation(x,y);
 }

 protected void storeMembers(Object[] o) throws InvalidConstraintMembersException{ //8Aug1999
  super.storeMembers(o);
  offset=new DoublePoint2D(getMasterPoint().offset(getSlavePoint())); //get the offset needed to be added to the master to get to the slave (that is get the offset for the direction master->slave) //20Oct199: using DoublePoint wrapper for Point2D
 }

 //

 public static final String OFFSET_X_PROPERTY="offsetX"; //8Jun2000
 public static final String OFFSET_Y_PROPERTY="offsetY"; //8Jun2000

/*
 public ObjectHash getProperties(){
  ObjectHash properties=super.getProperties(); //get any ancestor properties too
  if(properties==null) properties=new ObjectHash();

  properties.putDouble(OFFSET_X_PROPERTY,offset.getX());
  properties.putDouble(OFFSET_Y_PROPERTY,offset.getY());

  return properties;
 } //descendents should override that to return a hashtable with all the properties' name-value pairs (they should call super.getProperties first and add entries to the hashtable it returns and return that one - if super.getProperties() returns null, then create and return a new Hashtable instance)
*/

 public void setProperties(ObjectHash properties){ //29Mar2001-FINAL-VERSION: compatibility with old persistence code
  if(properties==null) return;
  super.setProperties(properties); //allow ancestor's to set their properties too from the same hash

  try{setOffset(
       properties.getDouble(OFFSET_X_PROPERTY),
       properties.getDouble(OFFSET_Y_PROPERTY)
      );
  }catch(Exception e){ setOffset(0,0); }


 } //descendents should override that to set all the properties for the hashtable with the name-value pairs (they should call super.setProperties too)

 public ESlateFieldMap2 getProperties(){
  ESlateFieldMap2 properties=super.getProperties(); //get any ancestor properties too
  if(properties==null)
    properties=new ESlateFieldMap2(STORAGE_VERSION);

  properties.put("OFFSET_X_PROPERTY",offset.getX());
  properties.put("OFFSET_Y_PROPERTY",offset.getY());

  return properties;
 } //descendents should override that to return a hashtable with all the properties' name-value pairs (they should call super.getProperties first and add entries to the hashtable it returns and return that one - if super.getProperties() returns null, then create and return a new Hashtable instance)

 public void setProperties(StorageStructure properties){
  if(properties==null) return;
  super.setProperties(properties); //allow ancestor's to set their properties too from the same hash

  try{setOffset(
       properties.get("OFFSET_X_PROPERTY",0),
       properties.get("OFFSET_Y_PROPERTY",0)
      );
  }catch(Exception e){ setOffset(0,0); }


 } //descendents should override that to set all the properties for the hashtable with the name-value pairs (they should call super.setProperties too)

}
