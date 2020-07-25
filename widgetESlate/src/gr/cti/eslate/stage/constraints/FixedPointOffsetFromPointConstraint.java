//2Dec1999 - localized

package gr.cti.eslate.stage.constraints;

import gr.cti.eslate.stage.constraints.base.AbstractPointOffsetFromPointConstraint;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import gr.cti.shapes.Point2DUtilities;

public class FixedPointOffsetFromPointConstraint
 extends AbstractPointOffsetFromPointConstraint
{

 static final long serialVersionUID = 7325858518510567534L; //8Jun2000: serial-version, so that new vers load OK

 private static final double accuracy=1d;

 public static String getTitle(){return MessagesBundle.localize("FixedPointOffsetFromPoint");} //8Aug1999

 public boolean isValid(Point2D master, Point2D slave){ //8Aug1999:changed param order
  Point2D offs=Point2DUtilities.getOffset(master,slave);
  return (Math.abs(offs.getX()-offset.getX())<accuracy) && (Math.abs(offs.getY()-offset.getY())<accuracy); //19Aug1999:now using an approximate (accuracy) instead of an absolute equality check
 }

 protected void force(PropertyChangeEvent e){
  //System.out.println("PermanentPointOffsetFromPointConstraint::force");
  getSlavePoint().setLocation2D(Point2DUtilities.getOffsetedCopy(getMasterPoint().getLocation2D(),offset)); //place the slave where the master would be if "offset" was added to it
 }

}
