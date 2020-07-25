package gr.cti.eslate.animation;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class implements a sound milestone of a sound segment.
 * @author	Augustine Grillakis
 * @version	1.0.0, 27-Jun-2002
 * @see     gr.cti.eslate.animation.SoundSegment
 */
public class SoundMilestone extends BaseMilestone implements Externalizable {

  /**
   * Create a milestone.
   */
  public SoundMilestone() {
  }

  /**
   * Create a milestone.
   * @param   segment   The milestone's segment.
   */
  public SoundMilestone(BaseSegment segment) {
    this.segment = segment;
  }

  /**
   * Create a milestone.
   * @param   segment   The milestone's segment.
   * @param   when        The milestone's time.
   */
  public SoundMilestone(BaseSegment segment, int when) {
    this.segment = segment;
    this.when = when;
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws    IOException
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion);

    map.put(WHEN, when);

    oo.writeObject(map);
  }

  /**
   * Load the component's state.
   * @param	oi	The stream from where the state should be loaded.
   * @throws    IOException
   * @throws    ClassNotFoundException
   */
  public void readExternal(ObjectInput oi)
    throws IOException, ClassNotFoundException
  {
    StorageStructure map = (StorageStructure)(oi.readObject());

    when = map.get(WHEN,0);
  }
}