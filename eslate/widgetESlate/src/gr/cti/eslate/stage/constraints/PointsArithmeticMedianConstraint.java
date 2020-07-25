//2Dec1999 - localized
//19Apr2000 - renamed to PointsArithmeticMedianContraint from PointsMedianConstraint

package gr.cti.eslate.stage.constraints;

import gr.cti.eslate.stage.constraints.base.AbstractNPointsPropertyPointConstraint;

import java.awt.geom.Point2D;
import gr.cti.shapes.Point2DUtilities;

public class PointsArithmeticMedianConstraint
 extends AbstractNPointsPropertyPointConstraint
{

 static final long serialVersionUID = 8062000L; //8Jun2000: serial-version, so that new vers load OK

 public static String getTitle(){return MessagesBundle.localize("PointsArithmeticMedianPoint");}

 //possible optimization: implement enforce so that instead of calling isValid and then force and calculate the slave's positionn twice, calculate it only once

 public Point2D getSlavePropertyPointLocation(Point2D[] masters){
  return Point2DUtilities.getArithmeticMedianPoint(masters);
 }

}
