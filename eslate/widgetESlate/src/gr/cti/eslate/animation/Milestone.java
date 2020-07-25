package gr.cti.eslate.animation;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class implements a milestone of a segment.
 * @author	Augustine Grillakis
 * @version	1.0.0, 7-Mar-2002
 * @see     gr.cti.eslate.animation.Segment
 */
public class Milestone extends BaseMilestone implements Externalizable {
  private final static String ANIVARVALUES = "aniVarValues";

  // Array of milestone animated variables values.
  IntBaseArray aniVarValues = new IntBaseArray();

  /**
   * Create a milestone.
   */
  public Milestone() {
  }

  /**
   * Create a milestone.
   * @param   segment   The milestone's segment.
   */
  public Milestone(BaseSegment segment) {
    this.segment = segment;
  }

  /**
   * Create a milestone.
   * @param   segment   The milestone's segment.
   * @param   when        The milestone's time.
   */
  public Milestone(BaseSegment segment, int when) {
    this.segment = segment;
    this.when = when;
  }

  /**
   * Sets the values of the variables of the milestone.
   * @param    newAniVarValues   The values of the variables of the milestone.
   */
  public void setAniVarValues(IntBaseArray newAniVarValues) {
/*    this.varValues.setSize(segment.actor.varCount);
    int i=0;
    for (int j=0;j<segment.actor.varCount;j++) {
      if (segment.actor.animatedVariables.get(j) == true ) {
        this.varValues.set(j,newAniVarValues.get(i));
        i++;
      }
    }*/
    this.aniVarValues = (IntBaseArray)newAniVarValues.clone();
  }

  /**
   * Returns the values of the variables of the milestone that to be processed.
   * @return  The values of the variables of the milestone.
   */
  public IntBaseArray getAniVarValues() {
/*    aniVarValues = new IntBaseArray();
    int i=0;
    for (int j=0;j<segment.actor.getVarCount();j++) {
      if (segment.actor.animatedVariables.get(j) == true ) {
        aniVarValues.add(varValues.get(j));
        i++;
      }
    }*/
    return aniVarValues;
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
    map.put(ANIVARVALUES, aniVarValues);

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
    aniVarValues = (IntBaseArray)map.get(ANIVARVALUES);
  }
}