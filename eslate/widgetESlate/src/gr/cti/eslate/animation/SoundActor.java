package gr.cti.eslate.animation;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * This class implements the sound actor that participates in animation.
 * @author	Augustine Grillakis
 * @version	1.0.0, 27-Jun-2002
 * @see     gr.cti.eslate.animation.Animation
 */
public class SoundActor extends BaseActor implements Externalizable {

  /**
   * Create an actor.
   */
  public SoundActor() {
  }

  /**
   * Create an actor.
   * @param	animation    The actor's animation.
   */
  public SoundActor(Animation animation) {
    this.animation = animation;
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
