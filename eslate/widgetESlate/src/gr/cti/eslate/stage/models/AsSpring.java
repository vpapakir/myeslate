//Version: 31Oct1999

package gr.cti.eslate.stage.models;

public interface AsSpring
 extends AsPhysicsObject, //29Oct1999: now extends AsPhysicsObject too
         /*Iteratable,*/
         HasLength,
         HasAngle //31Oct1999: now extends HasAngle
{
  public double getSpringConstant();
  public void setSpringConstant(double value);

  public double getNaturalLength(); //17Oct1999: changed "physical" to "natural"
  public void setNaturalLength(double value); //17Oct1999: changed "physical" to "natural"

  public double getMaximumLength();
  public void setMaximumLength(double value);

  public double getDisplacement();           //30Aug1999 //2Nov1999: calling this property "Displacement"
  public void setDisplacement(double value); //2Nov1999: calling this property "Displacement"
}





