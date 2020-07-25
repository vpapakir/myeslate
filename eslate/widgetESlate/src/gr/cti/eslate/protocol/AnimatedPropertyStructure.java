package gr.cti.eslate.protocol;

import gr.cti.typeArray.*;

/**
 * This class implements the animated property structure.
 *
 * @author      Augustine Grillakis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.protocol.AnimatedPropertyStructure
 */
public class AnimatedPropertyStructure {
  ObjectBaseArray animatedPropertyDescriptors;
  IntBaseArray animatedVariables = new IntBaseArray();

  /**
   * Create an animated property structure.
   */
  public AnimatedPropertyStructure() {
    animatedPropertyDescriptors = new ObjectBaseArray();
  }

  /**
   * Add an animated property descriptor.
   * @param aniProDesc  The animated property descriptor to add.
   */
  public void addAnimatedPropertyDescriptor (AnimatedPropertyDescriptor aniProDesc) {
    animatedPropertyDescriptors.add(aniProDesc);
  }

  /**
   * Get the animated property descriptors of the animated property structure.
   * @return The animated property descriptors of the animated property structure.
   */
  public ObjectBaseArray getAnimatedPropertyDescriptors() {
    return animatedPropertyDescriptors;
  }

  /**
   * Get the index number of the variable in the animated property descriptors.
   * @param propertyID  The property ID of the variable to find the index of.
   * @return The index of the variable.
   */
  public int indexOfAnimatedPropertyDescriptor(int propertyID) {
    for (int i=0;i<animatedPropertyDescriptors.size();i++) {
      if (((AnimatedPropertyDescriptor)animatedPropertyDescriptors.get(i)).propertyID == propertyID)
        return i;
    }
    System.err.println("No variable found!");
    return -1;
  }

  /**
   * Get the property name from its ID.
   * @param propertyID  The property ID of the variable to find the index of.
   * @return The property name.
   */
  public String getPropertyName(int propertyID) {
    for (int i=0;i<animatedPropertyDescriptors.size();i++) {
      if (((AnimatedPropertyDescriptor)animatedPropertyDescriptors.get(i)).propertyID == propertyID)
        return ((AnimatedPropertyDescriptor)animatedPropertyDescriptors.get(i)).getPropertyName();
    }
    return null;
  }

  /**
   * Get the animated variables.
   * @return  The property IDs of the animated variables.
   */
  public IntBaseArray getAnimatedVariables() {
    return animatedVariables;
  }

  /**
   * Set animated or not a variable.
   * @param propertyID  The property ID of the variable.
   * @param isAnimated  True for set animated.
   */
  public void setAnimated(int propertyID, boolean isAnimated) {
    if (isAnimated) {
      int position = animatedVariables.size();
      for (int k=0;k<animatedVariables.size();k++) {
        if (indexOfAnimatedPropertyDescriptor(propertyID) < indexOfAnimatedPropertyDescriptor(animatedVariables.get(k))) {
          position = k;
          break;
        }
      }
      animatedVariables.add(position, propertyID);
    }
    else
      animatedVariables.remove(animatedVariables.indexOf(propertyID));
  }

  /**
   * Check if a variable is animated.
   * @param propertyID  The property ID of the variable.
   * @return  True if animated.
   */
  public boolean isAnimated(int propertyID) {
    int index = animatedVariables.indexOf(propertyID);
    if (index >= 0)
      return true;
    else
      return false;
  }
}
