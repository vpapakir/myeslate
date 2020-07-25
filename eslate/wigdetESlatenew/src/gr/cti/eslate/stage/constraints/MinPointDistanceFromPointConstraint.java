//2Dec1999 - localized

package gr.cti.eslate.stage.constraints;

import gr.cti.eslate.stage.constraints.base.AbstractPointDistanceFromPointConstraint;
import java.awt.geom.Point2D;

public class MinPointDistanceFromPointConstraint
 extends AbstractPointDistanceFromPointConstraint
{

 static final long serialVersionUID = 8062000L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("MinPointDistanceFromPoint");} //29Jul1999

 public boolean isValid(Point2D master, Point2D slave){ //8Aug1999:changed param order
  return slave.distance(master)>=distance;
 };

}
