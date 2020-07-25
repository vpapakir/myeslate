package gr.cti.eslate.protocol;

/**
 * This class implements the animated property descriptor.
 *
 * @author      Augustine Grillakis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.protocol.AnimatedPropertyDescriptor
 */
public class AnimatedPropertyDescriptor {
  int propertyID;
  String propertyName;

  /**
   * Create an animated property descriptor.
   * @param propertyID    The ID of the variable.
   * @param propertyName  The name of the variable.
   */
  public AnimatedPropertyDescriptor(int propertyID, String propertyName) {
    this.propertyID = propertyID;
    this.propertyName = propertyName;
  }

  /**
   * Get the variable's property ID.
   * @return The variable's property ID.
   */
  public int getPropertyID() {
    return propertyID;
  }

  /**
   * Get the variable's name.
   * @return The variable's name.
   */
  public String getPropertyName() {
    return propertyName;
  }
}
