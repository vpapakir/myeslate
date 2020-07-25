//Version: 31Oct1999

package gr.cti.eslate.stage.models;

import java.awt.geom.Point2D; //this is not a storable property (it's calculated as m*a)

public interface HasAppliedForce
 extends HasMass,
         HasAcceleration2D
{
  public Point2D getAppliedForce();
  public void setAppliedForce(Point2D force);
}

