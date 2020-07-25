//2Dec1999 - localized

//...createAlso: pointOnShapeContour, min/max PointDistanceFromShapeControur

package gr.cti.eslate.stage.constraints;

import gr.cti.eslate.stage.constraints.base.AbstractMasterSlaveConstraint;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.stage.ControlPoint;
import gr.cti.eslate.stage.objects.PhysicsObject;

public class PointIntoShapeConstraint
 extends AbstractMasterSlaveConstraint
{

 static final long serialVersionUID = 7455633625138900484L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("PointIntoShape");} //29Jul1999

 protected void force(PropertyChangeEvent e){
  //((ControlPoint)slave).setLocation2D((Point2D)e.getNewValue());
 }

 public PhysicsObject getMaster(){
  return (PhysicsObject)members[0];
 }

 public ControlPoint getSlave(){
  return (ControlPoint)members[1];
 }

 public boolean isValid(PropertyChangeEvent e){
  //if(e.getName.equals(LOCATION_PROPERTY))
  return getMaster().getShape().contains((Point2D)e.getNewValue());
 }

 public boolean holds(){
  return getMaster().getShape().contains(getSlave().getLocation2D());
 }

}
