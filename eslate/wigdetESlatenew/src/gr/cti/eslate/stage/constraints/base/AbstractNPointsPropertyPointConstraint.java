//break this in one more ancestor, AbstractNPointsConstraint (with abstract getMasters() and getSlaves())

package gr.cti.eslate.stage.constraints.base;

import gr.cti.eslate.stage.constraints.exception.*;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.stage.ControlPoint;

public abstract class AbstractNPointsPropertyPointConstraint
 extends AbstractMastersSlavesConstraint //11Aug1999
{

 static final long serialVersionUID = 8062000L; //8Jun2000: serial-version, so that new vers load OK

 public Object[] getMasters(){
  return getMembersPart(0,members.length-1);
 }

 public Object[] getSlaves(){
  int last=members.length-1;
  return getMembersPart(last,last);
 }

 public Point2D[] getMasterPointsLocations(){ //can't cache the locations, must get them from the master points, cause they change
  ControlPoint[] masters=getMasterPoints(); //getMasters may return a cached array copy of the masters, so don't modify the array entries
  Point2D[] p=new Point2D[masters.length];
  for(int i=p.length;i-->0;)
   p[i]=masters[i].getLocation2D();
  return p;
 }

 public ControlPoint[] getMasterPoints(){ //the ancestor's getMasters() returns Object[], this returns ControlPoint[]
  int count=members.length-1;
  ControlPoint[] masters=new ControlPoint[count];
  System.arraycopy(members,0,masters,0,count);
  return masters;
 } //maybe should cache the master points array and not recalculate it at getMasterPoints()

 public ControlPoint getSlavePoint(){
  return (ControlPoint)members[members.length-1];
 }

 protected void storeMembers(Object[] o) throws InvalidConstraintMembersException{ //maybe should cache the master points array and not recalculate it at getMasterPoints()
  for(int i=o.length;i-->0;)
   if (!(o[i] instanceof ControlPoint))
    throw new InvalidConstraintMembersException("A Points constraint uses only ControPoints",this,o);
  super.storeMembers(o);
 }

 public boolean isValid(PropertyChangeEvent e){
  //if(e.getName.equals(LOCATION_PROPERTY))
  return isValid(
   getMasterPointsLocations(), //masters
   (java.awt.geom.Point2D)e.getNewValue()  //slave
  );
 }

 public boolean holds(){
  return isValid(
   getMasterPointsLocations(),   //masters
   getSlavePoint().getLocation2D() //slave
  );
 }

 ////////

 protected void force(PropertyChangeEvent e){
  getSlavePoint().setLocation2D(getSlavePropertyPointLocation(getMasterPointsLocations()));
 }

 public boolean isValid(Point2D[] masters, Point2D slave){ //8Aug1999:changed param order
  return(slave.equals(getSlavePropertyPointLocation(masters)));
 };

 public abstract Point2D getSlavePropertyPointLocation(Point2D[] masters); //ancestors can just implement this one to say where the property point should be (given the master points' locations)

}
