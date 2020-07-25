package gr.cti.eslate.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * This class implements the base actor.
 * @author	Augustine Grillakis
 * @version	1.0.0, 26-Jun-2002
 * @see     gr.cti.eslate.animation.Animation
 */
public abstract class BaseActor implements Externalizable {
  final static String SEGMENTS = "segments";
  final static String version = "1.0.0";
  final static int storageVersion = 1;

  ArrayList actorListeners = new ArrayList();

  Animation animation;
  BaseSegment startSegment;
  BaseSegment activeSegment = null;
  BaseSegment currentSegment = null;

  boolean onStageFlag = false;
  boolean offStageFlag = false;

  /**
   * Add a listener for actor events.
   * @param	listener	The listener to add.
   */
  public void addActorListener(ActorListener listener)
  {
    synchronized (actorListeners) {
      if (!actorListeners.contains(listener)) {
        actorListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from actor events.
   * @param	listener	The listener to remove.
   */
  public void removeActorListener(ActorListener listener)
  {
    synchronized (actorListeners) {
      int ind = actorListeners.indexOf(listener);
      if (ind >= 0) {
        actorListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for actor (segment added) events.
   * @param	segment  The segment that added.
   */
  private void fireSegmentAddedActorListeners(BaseSegment segment)
  {
    ArrayList listeners;
    synchronized(actorListeners) {
      listeners = (ArrayList)(actorListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      ActorListener l = (ActorListener)(listeners.get(i));
      ActorEvent e = new ActorEvent(this, segment);
      l.segmentAdded(e);
    }
  }

  /**
   * Fires all listeners registered for actor (segment removed) events.
   * @param	segment    The segment that removed.
   */
  private void fireSegmentRemovedActorListeners(BaseSegment segment)
  {
    ArrayList listeners;
    synchronized(actorListeners) {
      listeners = (ArrayList)(actorListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      ActorListener l = (ActorListener)(listeners.get(i));
      ActorEvent e = new ActorEvent(this, segment);
      l.segmentRemoved(e);
    }
  }

  /**
   * Add a segment.
   * @param	segment   The segment to add.
   * @return	The true or false.
   */
  public boolean addSegment(BaseSegment segment) {
    /**
     * If there are no segments in actor make it start segment.
     */
    if (startSegment == null) {
      startSegment = segment;
      startSegment.previous = null;
      startSegment.next = null;
      segment.actor = this;
      fireSegmentAddedActorListeners(segment);
      return true;
    }
    BaseSegment previousSegment = null;
    BaseSegment nextSegment = startSegment;
    /**
     * Locate previous and next segment than that to insert.
     */
    while ((nextSegment != null) && (nextSegment.startMilestone.when <= segment.endMilestone.when)) {
      previousSegment = nextSegment;
      nextSegment = nextSegment.next;
    }
    /**
     * Return false in case of intersection.
     */
    if ((previousSegment != null) && (previousSegment.endMilestone.when > segment.startMilestone.when))
      return false;
    /**
     * Check if the segment is the first in list.
     */
    if (previousSegment != null) {
      previousSegment.next = segment;
      segment.previous = previousSegment;
    }
    else startSegment = segment;
    /**
     * Check if segment is the last in list.
     */
    if (nextSegment != null) {
      nextSegment.previous = segment;
      segment.next = nextSegment;
    }
    segment.actor = this;
    fireSegmentAddedActorListeners(segment);
    return true;
  }

  /**
   * Add a segment.
   * @param	start   The start of time frame.
   * @param	end     The end of segment.
   * @return	The true or false.
   */
  public boolean addSegment(int start, int end) {
    BaseSegment segment = new Segment(this, start, end);
    return (addSegment(segment));
  }

  /**
   * Remove a segment.
   * @param	segment   The segment to remove.
   * @return	The true or false.
   */
  public boolean removeSegment(BaseSegment segment) {
    /**
     * Return false if try to remove a segment from wrong actor.
     */
    if (this != segment.getActor()) {
      System.out.println("Wrong actor!");
      return false;
    }
    BaseSegment iterator = startSegment;
    /**
     * Locate segment to remove.
     */
    while (segment != iterator)
      iterator = iterator.next;
    /**
     * Return false if there isn't the segment.
     */
    if (iterator == null)
      return false;
    BaseSegment previousSegment = iterator.previous;
    BaseSegment nextSegment = iterator.next;
    /**
     * Check if this is the first segment in list.
     */
    if (previousSegment != null)
      previousSegment.next = iterator.next;
    else startSegment = iterator.next;
    /**
     * Check if this is the last segment in list.
     */
    if (nextSegment != null)
      nextSegment.previous = iterator.previous;
    segment.actor = null;
/*    segment.previous = null;
    segment.next = null;*/
    fireSegmentRemovedActorListeners(segment);
    return true;
  }

  /**
   * Sets the actor's start segment.
   * @param	newStartSegment   Actor's start segment.
   */
  void setStartSegment(BaseSegment newStartSegment) {
    startSegment = newStartSegment;
  }

  /**
   * Returns the actor's start segment.
   * @return	The start segment.
   */
  public BaseSegment getStartSegment() {
    return startSegment;
  }

  /**
   * Returns the actor's start time.
   * @return	The start time.
   */
  public int getStart() {
    return startSegment.startMilestone.when;
  }

  /**
   * Returns the actor's end time.
   * @return	The end time.
   */
  public int getEnd() {
    BaseSegment endSegment = startSegment;
    while (endSegment.next != null)
      endSegment = endSegment.next;
    return endSegment.endMilestone.when;
  }

  /**
   * Returns the animation that the actor belongs to.
   * @return	The animation.
   */
  public Animation getAnimation() {
    return animation;
  }

  /**
   * Returns the segment that the time belongs to.
   * @param	time  The time to search.
   * @return	The segment.
   */
  public BaseSegment getSegment(int time) {
    if (activeSegment != null) {
      /**
       * If time is in active segment return the segment.
       */
      if (activeSegment.isTimeInSegment(time))
        return activeSegment;
      if ((time > activeSegment.endMilestone.when) && (activeSegment.next != null)) {
        /**
          * Else check if time is between the active segment and its next one
          * in order to return null.
          */
        if (time < activeSegment.next.startMilestone.when)
          return null;
        /**
         * Else if time is in active segment's next make active the next one
         * and return it.
         */
        if (activeSegment.next.isTimeInSegment(time)) {
          activeSegment = activeSegment.next;
          return activeSegment;
        }
      }
    }
    /**
     * If active segment doesn't exist search for it else return null.
     */
    BaseSegment iterator = startSegment;
    while (iterator != null) {
      if (iterator.isTimeInSegment(time)) {
        activeSegment = iterator;
        return activeSegment;
      }
      iterator = iterator.next;
    }
    activeSegment = null;
    return null;
  }

  /**
   * Checks if the time is in the period that starts at start
   * and lasts as long as the duration.
   * @param	time  The time to search for.
   * @param	start The start time to start.
   * @param	end   The duration to last.
   * @return	The true or false.
   */
  public static boolean isTimeInPeriod(int time, int start, int end) {
    if ((time >= start) && (time <= end))
      return true;
    return false;
  }

  /**
   * Returns the segment's index.
   * @param	segment   The segment to find the index of.
   * @return	The segment's index.
   */
  public int indexOf(BaseSegment segment) {
    int index = 0;
    BaseSegment iterator = startSegment;
    /**
     * Locate the segment.
     */
    while ((iterator != segment) && (iterator.next != null)) {
      iterator = iterator.next;
      index++;
    }
    /**
     * If there isn't the segment return -1.
     */
    if (iterator == null)
      return -1;
    /**
     * Else return the index of segment.
     */
    return index;
  }

  /**
   * Returns the number of actor's segments.
   * @return	The number of segments.
   */
  public int getSegmentCount() {
    int count = 0;
    BaseSegment iterator = startSegment;
    /**
     * Count the segments and return their number.
     */
    while (iterator != null) {
      iterator = iterator.next;
      count++;
    }
    return count;
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws     IOException
   */
  public abstract void writeExternal(ObjectOutput oo) throws IOException;

  /**
   * Load the component's state.
   * @param	oi	The stream from where the state should be loaded.
   * @throws     IOException
   * @throws     ClassNotFoundException
   */
  public abstract void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException;
}
