package gr.cti.eslate.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * This class implements the base segment.
 * @author	Augustine Grillakis
 * @version	1.0.0, 26-Jun-2002
 * @see     gr.cti.eslate.animation.BaseActor
 */
public abstract class BaseSegment implements Externalizable {
  final static String MILESTONES = "milestones";
  final static String version = "1.0.0";
  final static int storageVersion = 1;

  ArrayList segmentListeners = new ArrayList();

  BaseMilestone startMilestone;
  BaseMilestone endMilestone;
  BaseActor actor;
  BaseSegment previous;
  BaseSegment next;

  /**
   * Add a listener for segment events.
   * @param	listener	The listener to add.
   */
  public void addSegmentListener(SegmentListener listener)
  {
    synchronized (segmentListeners) {
      if (!segmentListeners.contains(listener)) {
        segmentListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from segment events.
   * @param	listener	The listener to remove.
   */
  public void removeSegmentListener(SegmentListener listener)
  {
    synchronized (segmentListeners) {
      int ind = segmentListeners.indexOf(listener);
      if (ind >= 0) {
        segmentListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for segment (milestone added) events.
   * @param	milestone  The milestone that added.
   */
  public void fireMilestoneAddedSegmentListeners(BaseMilestone milestone)
  {
    ArrayList listeners;
    synchronized(segmentListeners) {
      listeners = (ArrayList)(segmentListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      SegmentListener l = (SegmentListener)(listeners.get(i));
      SegmentEvent e = new SegmentEvent(this, milestone);
      l.milestoneAdded(e);
    }
  }

  /**
   * Fires all listeners registered for segment (milestone removed) events.
   * @param	milestone  The milestone that removed.
   */
  public void fireMilestoneRemovedSegmentListeners(BaseMilestone milestone)
  {
    ArrayList listeners;
    synchronized(segmentListeners) {
      listeners = (ArrayList)(segmentListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      SegmentListener l = (SegmentListener)(listeners.get(i));
      SegmentEvent e = new SegmentEvent(this, milestone);
      l.milestoneRemoved(e);
    }
  }

  /**
   * Sets the segment's start time.
   * @param	newStart   Segment's start time.
   */
  public void setStart(int newStart) {
    startMilestone.when = newStart;
    startMilestone.fireMilestoneListeners(newStart);
  }

  /**
   * Gets the segment's start time.
   * @return	The start time.
   */
  public int getStart() {
    return startMilestone.when;
  }

  /**
   * Sets the segment's end time.
   * @param	newEnd   Segment's end time.
   */
  public void setEnd(int newEnd) {
    endMilestone.when = newEnd;
    endMilestone.fireMilestoneListeners(newEnd);
  }

  /**
   * Gets the segment's end time.
   * @return	The end time.
   */
  public int getEnd() {
    return endMilestone.when;
  }

  /**
   * Link segment with previous segment.
   * @param	newPrevious   Segment's previous segment.
   */
  void setPrevious(BaseSegment newPrevious) {
    this.previous = newPrevious;
  }

  /**
   * Gets the segment's previous segment.
   * @return	The previous segment.
   */
  public BaseSegment getPrevious() {
    return previous;
  }

  /**
   * Link segment with next segment.
   * @param	newNext   Segment's next segment.
   */
  void setNext(BaseSegment newNext) {
    this.next = newNext;
  }

  /**
   * Gets the segment's next segment.
   * @return	The next segment.
   */
  public BaseSegment getNext() {
    return next;
  }

  /**
   * Add a milestone.
   * @param	milestone   The milestone to add.
   * @return	The true or false.
   */
  public boolean addMilestone(BaseMilestone milestone) {
    BaseMilestone previousMilestone = startMilestone;
    BaseMilestone nextMilestone = previousMilestone.next;
    /**
     * Locate previous and next milestone than that to insert.
     */
    while ((nextMilestone != null) && (nextMilestone.when < milestone.when)) {
      previousMilestone = nextMilestone;
      nextMilestone = nextMilestone.next;
    }
    /**
     * Return false if the milestone already exists
     * or its time is out of range of the segment.
     */
    if ((nextMilestone == null) || (milestone.when == nextMilestone.when))
      return false;
    /**
     * Else insert the milestone and return true.
     */
    previousMilestone.next = milestone;
    milestone.previous = previousMilestone;
    nextMilestone.previous = milestone;
    milestone.next = nextMilestone;
    milestone.segment = this;
    fireMilestoneAddedSegmentListeners(milestone);
    return true;
  }

  /**
   * Remove an milestone.
   * @param	milestone   The milestone to remove.
   * @return	The true or false.
   */
  public boolean removeMilestone(BaseMilestone milestone) {
    /**
     * Return false if try to remove a milestone from wrong segment.
     */
    if (this != milestone.getSegment()) {
      System.out.println("Wrong segment!");
      return false;
    }
    BaseMilestone iterator = startMilestone.next;
    /**
     * Locate the milestone to remove.
     */
    while ((iterator != endMilestone) && (milestone != iterator))
      iterator = iterator.next;
    /**
     * Return false if there isn't the milestone.
     */
    if (iterator == endMilestone)
      return false;
    /**
     * Else remove the milestone and return true.
     */
    BaseMilestone previousMilestone = iterator.previous;
    BaseMilestone nextMilestone = iterator.next;
    previousMilestone.next = iterator.next;
    nextMilestone = iterator.previous;
    milestone.segment = null;
    fireMilestoneRemovedSegmentListeners(milestone);
    return true;
  }

  /**
   * Gets the actor that the segment belongs to.
   * @return	The actor.
   */
  public BaseActor getActor() {
    return actor;
  }

   /**
   * Checks if the time is in the segment.
   * @param	time   The time to search for.
   * @return	The true or false.
   */
  public boolean isTimeInSegment(int time) {
    if ((time >= startMilestone.when) && (time <= endMilestone.when))
      return true;
    return false;
  }

  /**
   * Checks if a segment intersects with any segment of the actor.
   * @param	segment  The segment to check.
   * @return	The true or false.
   */
  public boolean intersect(BaseSegment segment) {
    if (segment.isTimeInSegment(startMilestone.when) || segment.isTimeInSegment(endMilestone.when))
      return true;
    return false;
  }

  /**
   * Returns the milestone's index.
   * @param	milestone  The milestone to find the index of.
   * @return	The milestone's index.
   */
  public int indexOf(BaseMilestone milestone) {
    int index = 0;
    BaseMilestone iterator = startMilestone;
    /**
     * Locate the milestone.
     */
    while ((iterator != milestone) && (iterator.next != null)) {
      iterator = iterator.next;
      index++;
    }
    /**
     * If there isn't the milestone return -1.
     */
    if (iterator == null)
      return -1;
    /**
     * Else return the index of milestone.
     */
    return index;
  }

  /**
   * Returns the number of segment's milestones.
   * @return	The number of milestones.
   */
  public int getMilestoneCount() {
    int count = 0;
    BaseMilestone iterator = startMilestone;
    /**
     * Count the milestones and return their number.
     */
    while (iterator != null) {
      iterator = iterator.next;
      count++;
    }
    return count;
  }

  /**
   * Gets the segment's start milestone.
   * @return	The start milestone.
   */
  public BaseMilestone getStartMilestone() {
    return startMilestone;
  }
  /**
   * Gets the segment's end milestone.
   * @return	The end milestone.
   */
  public BaseMilestone getEndMilestone() {
    return endMilestone;
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws    IOException
   */
  public abstract void writeExternal(ObjectOutput oo) throws IOException;

  /**
   * Load the component's state.
   * @param	oi	The stream from where the state should be loaded.
   * @throws    IOException
   * @throws    ClassNotFoundException
   */
  public abstract void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException;
}
