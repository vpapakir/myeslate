package gr.cti.eslate.protocol;

import gr.cti.typeArray.*;

/**
 * This class implements the animation session structure.
 *
 * @author      Augustine Grillakis
 * @version     3.0.0, 19-May-2006
 * @see         gr.cti.eslate.protocol.AnimationSession
 */
public class AnimationSession {
  //private final static String ANIMATIONSTATUS = "animationStatus";
  //private final static String version = "1.0.0";

  AnimatedPropertyStructure animatedPropertyStructure;
  BoolBaseArray animationStatus;
  int plugID;
  ActorInterface actorInterface;

  /**
   * Create an animation session.
   * @param aniProStruct    The animation property structure of the animation session.
   * @param actorInterface  The actor interface of the animation session.
   */
  public AnimationSession(AnimatedPropertyStructure aniProStruct, ActorInterface actorInterface) {
    this.animatedPropertyStructure = aniProStruct;
    this.actorInterface = actorInterface;
    animationStatus = new BoolBaseArray(animatedPropertyStructure.animatedPropertyDescriptors.size());
  }

  /**
   * Set the plug ID.
   * @param plugID The plug ID.
   */
  public void setPlugID(int plugID) {
    this.plugID = plugID;
  }

  /**
   * Get the plug ID.
   * @return The plug ID.
   */
  public int getPlugID() {
    return plugID;
  }

  /**
   * Get the animated property structure.
   * @return The animated property structure.
   */
  public AnimatedPropertyStructure getAnimatedPropertyStructure() {
    return animatedPropertyStructure;
  }

  /**
   * Get the actor interface.
   * @return The actor interface.
   */
  public ActorInterface getActorInterface() {
    return actorInterface;
  }

  /**
   * Get the animation status.
   * @return The animation status.
   */
  public BoolBaseArray getAnimationStatus() {
    return animationStatus;
  }

  /**
   * Set the animation status.
   * @param animStatus  The animation status.
   */
  public void setAnimationStatus(BoolBaseArray animStatus) {
    this.animationStatus = (BoolBaseArray)animStatus.clone();
  }

  /**
   * Check if a variable is animated or not.
   * @param propertyID  The property ID of the variable.
   * @return  True or false.
   */
  public boolean isAnimated (int propertyID) {
    if (animationStatus.get(animatedPropertyStructure.indexOfAnimatedPropertyDescriptor(propertyID)) == true)
      return true;
    else
      return false;
  }

  /**
   * Set on or off animated a variable.
   * @param propertyID  The property ID of the variable to set.
   * @param animated    True of false, to set on or off animated.
   */
  public void setAnimated(int propertyID, boolean animated) {
    animationStatus.setSize(animatedPropertyStructure.animatedPropertyDescriptors.size());
    animationStatus.set(animatedPropertyStructure.indexOfAnimatedPropertyDescriptor(propertyID),animated);
  }

  /**
   * Reset animation status for all variables to false.
   */
  public void resetAnimationStatus() {
    for (int i=0;i<animatedPropertyStructure.animatedPropertyDescriptors.size();i++) {
      animationStatus.set(i,false);
    }
  }

  /**
   * Get the number of the animated variables of the animation session.
   * @return The number of the animated variables.
   */
  public int getAnimatedPropertiesCount() {
    int count = 0;
    for (int i=0;i<animatedPropertyStructure.animatedPropertyDescriptors.size();i++) {
      if (isAnimated(((AnimatedPropertyDescriptor)animatedPropertyStructure.animatedPropertyDescriptors.get(i)).propertyID))
        count++;
    }
    return count;
  }

  /**
   * Get animated variables.
   * @return The property IDs of the animated variables.
   */
  public IntBaseArray getAnimatedProperties() {
    IntBaseArray animatedProperties = new IntBaseArray();
    for (int i=0;i<animatedPropertyStructure.animatedPropertyDescriptors.size();i++) {
      if (isAnimated(((AnimatedPropertyDescriptor)animatedPropertyStructure.animatedPropertyDescriptors.get(i)).propertyID))
        animatedProperties.add(((AnimatedPropertyDescriptor)animatedPropertyStructure.animatedPropertyDescriptors.get(i)).propertyID);
    }
    return animatedProperties;
  }
}
