//2Dec1999 - localized

package gr.cti.eslate.stage.constraints;

import gr.cti.eslate.stage.constraints.base.AbstractPointDistanceFromPointConstraint;
import java.awt.geom.Point2D;

public class FixedPointDistanceFromPointConstraint
 extends AbstractPointDistanceFromPointConstraint
{

 static final long serialVersionUID = -3138293736852814277L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("FixedPointDistanceFromPoint");} //11Aug1999

 private static final double accuracy=1d;

 public boolean isValid(Point2D master, Point2D slave){
  return Math.abs(slave.distance(master)-distance)<accuracy; //19Aug1999:now using an approximate (accuracy) instead of an absolute equality check
 };

}
