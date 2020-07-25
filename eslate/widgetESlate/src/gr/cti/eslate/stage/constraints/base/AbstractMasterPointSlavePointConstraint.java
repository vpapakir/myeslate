//2Dec1999 - localized
//19Apr2000 - "storeMembers method doesn't throw "ArrayOutOfBoundsException" when the number of members passed to it is not 2, but instead throws IllegalMembersException with a localized explanation method

package gr.cti.eslate.stage.constraints.base;

import gr.cti.eslate.stage.constraints.exception.*;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.stage.ControlPoint;

public abstract class AbstractMasterPointSlavePointConstraint
 extends AbstractMasterSlaveConstraint
{ //8Aug1999

 static final long serialVersionUID = -7568984125657761573L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("UnknownPointPoint");} //2Dec1999

 public ControlPoint getMasterPoint(){
  return (ControlPoint)members[0];
 }

 public ControlPoint getSlavePoint(){
  return (ControlPoint)members[1];
 }

 protected void storeMembers(Object[] o) throws InvalidConstraintMembersException{
  if (o.length!=2 || //19Apr2000: in case less than two members are passed in, throw an appropriate exception with a localized message, instead of an ArrayOutOfBoundsException (which would be caused by o[0] or o[1]). Also throw exception if more than 2 members are passed in
      !(o[0] instanceof ControlPoint && o[1] instanceof ControlPoint))
   throw new InvalidConstraintMembersException(MessagesBundle.localize("need2points"),this,o);
  super.storeMembers(o);
 }

 public boolean isValid(PropertyChangeEvent e){
  //if(e.getName.equals(LOCATION_PROPERTY))
  return isValid(
   getMasterPoint().getLocation2D(), //master
   (java.awt.geom.Point2D)e.getNewValue()  //slave
  );
 }

 public boolean holds(){ //29Jul1999
  return isValid(
   getMasterPoint().getLocation2D(),   //master
   getSlavePoint().getLocation2D() //slave
  );
 }

 public abstract boolean isValid(Point2D master, Point2D slave); //8Aug1999: changed param order

}
