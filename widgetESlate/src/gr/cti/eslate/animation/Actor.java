package gr.cti.eslate.animation;

import gr.cti.eslate.protocol.ActorInterface;
import gr.cti.eslate.protocol.AnimationSession;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.BoolBaseArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.StringBaseArray;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * This class implements an actor that participates in animation.
 * @author	Augustine Grillakis
 * @version	1.0.0, 7-Mar-2002
 * @see     gr.cti.eslate.animation.Animation
 */
public class Actor extends BaseActor implements Externalizable {
  private final static String ANIMATIONSTATUS = "animationStatus";

  ActorInterface actorInterface;

  // Animation session for the connection.
  AnimationSession animationSession;
  // Animated variables property IDs.
  IntBaseArray aniPropertyIDs = new IntBaseArray();
  // Animation status.
  BoolBaseArray animationStatus;
  // Variables number.
  int varCount;
  // Animated variables number.
  int aniVarCount;

  /**
   * Create an actor.
   */
  public Actor() {
  }

  /**
   * Create an actor.
   * @param	animation    The actor's animation.
   */
  public Actor(Animation animation) {
    this.animation = animation;
  }

  /**
   * Create an actor.
   * @param	animation         The actor's animation.
   * @param	actorInterface   The actor interface to link.
   */
  public Actor(Animation animation, ActorInterface actorInterface) {
    this.animation = animation;
    this.actorInterface = actorInterface;
  }

  /**
   * Add a segment.
   * @param	start   The start of time frame.
   * @param	end     The end of segment.
   * @return	The true or false.
   */
/*  public boolean addSegment(int start, int end) {
    Segment segment = new Segment(this, start, end);
    return (addSegment(segment));
  }*/

  /**
   * Returns the actor's animation session.
   * @return    The actor's animation session.
   */
  public AnimationSession getAnimationSession() {
    return animationSession;
  }

  /**
   * Returns the number of the actor's variables.
   * @return    The number of the actor's variables.
   */
  public int getVarCount() {
    return varCount;
  }

  /**
   * Set the number of the actor's variables.
   * @param  varCount  The number of the actor's variables.
   */
  public void setVarCount(int varCount) {
    this.varCount = varCount;
  }

  /**
   * Returns the number of the actor's animated variables.
   * @return    The number of the actor's animated variables.
   */
  public int getAniVarCount() {
/*    int aniVarCount = 0;
    for (int i=0;i<actorInterface.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().size();i++) {
      if (animationSession.isAnimated(((AnimatedPropertyDescriptor)actorInterface.getAnimatedPropertyStructure().getAnimatedPropertyDescriptors().get(i)).getPropertyID()))
        aniVarCount++;
    }*/
    return aniVarCount;
  }

  /**
   * Set the number of the actor's animated variables.
   * @param  aniVarCount  The number of the actor's animated variables.
   */
  public void setAniVarCount(int aniVarCount) {
    this.aniVarCount = aniVarCount;
  }

  /**
   * Returns the actor's animated variables property IDs.
   * @return	The property IDs.
   */
  public IntBaseArray getAniPropertyIDs() {
    return aniPropertyIDs;
  }

  /**
   * Returns the actor's animated variables property names.
   * @return	The property names.
   */
  public StringBaseArray getAniPropertyNames() {
    StringBaseArray aniPropertyNames = new StringBaseArray();
    for (int i=0;i<aniPropertyIDs.size();i++) {
      aniPropertyNames.add(actorInterface.getAnimatedPropertyStructure().getPropertyName(aniPropertyIDs.get(i)));
    }
    return aniPropertyNames;
  }

  /**
   * Get the index number of the animated property ID.
   * @param propertyID  The animated property ID to find the index of.
   * @return The index of the animated property ID.
   */
  public int indexOfAnimatedPropertyID(int propertyID) {
    for (int i=0;i<aniPropertyIDs.size();i++) {
      if (aniPropertyIDs.get(i) == propertyID)
        return i;
    }
    System.err.println("No animated variable found!");
    return -1;
  }

  /**
   * Returns the actor's interface.
   * @return    The actor's interface.
   */
  public ActorInterface getActorInterface() {
    return actorInterface;
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws     IOException
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    // Construct an arraylist of actor's segments beginning from 1st
    // segment in order to save it.
    ArrayList segments = new ArrayList();
    BaseSegment iterator = startSegment;
    while (iterator != null) {
      segments.add(iterator);
      iterator = iterator.next;
    }

    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion);

    map.put(SEGMENTS, segments);
    map.put(ANIMATIONSTATUS, animationSession.getAnimationStatus());

    oo.writeObject(map);
  }

  /**
   * Load the component's state.
   * @param	oi	The stream from where the state should be loaded.
   * @throws     IOException
   * @throws     ClassNotFoundException
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    ArrayList segments = null;
    BaseSegment previousSegment = null;
    BaseSegment actualSegment = null;

    StorageStructure map = (StorageStructure)(oi.readObject());

    segments = (ArrayList)map.get(SEGMENTS);
    animationStatus = (BoolBaseArray)((BoolBaseArray)map.get(ANIMATIONSTATUS)).clone();

    // Construct the custom linked list of actor's segments from the
    // loaded arraylist of segments.
    for (int i=0;i<segments.size();i++) {
      if (i==0) {
        startSegment = (BaseSegment)segments.get(0);
        startSegment.actor = this;
        previousSegment = startSegment;
        actualSegment = startSegment;
      }
      else {
        actualSegment = (BaseSegment)segments.get(i);
        actualSegment.actor = this;
        previousSegment.next = actualSegment;
        actualSegment.previous = previousSegment;
        previousSegment = (BaseSegment)segments.get(i);
      }
    }
  }
}
