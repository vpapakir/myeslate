package gr.cti.eslate.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * This class implements the base milestone.
 * @author	Augustine Grillakis
 * @version	1.0.0, 26-Jun-2002
 * @see     gr.cti.eslate.animation.BaseSegment
 */
public abstract class BaseMilestone implements Externalizable {
  final static String WHEN = "when";
  final static String version = "1.0.0";
  final static int storageVersion = 1;

  ArrayList milestoneListeners = new ArrayList();

  BaseSegment segment;
  int when = 0;
  BaseMilestone previous=null;
  BaseMilestone next=null;

  /**
   * Add a listener for milestone events.
   * @param	listener	The listener to add.
   */
  public void addMilestoneListener(MilestoneListener listener)
  {
    synchronized (milestoneListeners) {
      if (!milestoneListeners.contains(listener)) {
        milestoneListeners.add(listener);
      }
    }
  }

  /**
   * Remove a listener from milestone events.
   * @param	listener	The listener to remove.
   */
  public void removeMilestoneListener(MilestoneListener listener)
  {
    synchronized (milestoneListeners) {
      int ind = milestoneListeners.indexOf(listener);
      if (ind >= 0) {
        milestoneListeners.remove(ind);
      }
    }
  }

  /**
   * Fires all listeners registered for milestone events.
   * @param	when  The time that changed.
   */
  public void fireMilestoneListeners(int when)
  {
    ArrayList listeners;
    synchronized(milestoneListeners) {
      listeners = (ArrayList)(milestoneListeners.clone());
    }
    int size = listeners.size();
    for (int i=0; i<size; i++) {
      MilestoneListener l = (MilestoneListener)(listeners.get(i));
      MilestoneEvent e = new MilestoneEvent(this, when);
      l.whenChanged(e);
    }
  }

  /**
   * Sets the milestone's time of appearance in the segment.
   * @param	newWhen   Milestone's time of appearance.
   */
  public void setWhen(int newWhen) {
    this.when = newWhen;
    fireMilestoneListeners(newWhen);
  }

  /**
   * Gets the milestone's time of appearance in the segment.
   * @return	The milestone's time of appearance.
   */
  public int getWhen() {
    return when;
  }

  /**
   * Gets the segment that milestone belongs to.
   * @return	The segment.
   */
  public BaseSegment getSegment() {
    return segment;
  }

  /**
   * Gets milestone's previous.
   * @return	The previous milestone.
   */
  public BaseMilestone getPrevious() {
    return previous;
  }

  /**
   * Gets milestone's next.
   * @return	The next milestone.
   */
  public BaseMilestone getNext() {
    return next;
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