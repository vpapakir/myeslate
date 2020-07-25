//Version: 3Apr2001

package gr.cti.eslate.stage.models;

import gr.cti.shapes.DoublePoint2D;

public interface HasAcceleration2D
 extends HasVelocity2D
{
  public DoublePoint2D getAcceleration();
  public void setAcceleration(DoublePoint2D value);
}

