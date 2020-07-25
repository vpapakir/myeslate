package gr.cti.eslate.animation;

import java.io.*;

import gr.cti.eslate.utils.*;

/**
 * This class implements the frame label structure.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 21-Jun-2002
 * @see		gr.cti.eslate.animation.Animation
 */
public class FrameLabel implements Externalizable {
  private final static String FRAME = "frame";
  private final static String LABEL = "label";
  private final static int storageVersion = 1;

  int frame;
  String label;

  /**
   * Create a frame label structure.
   */
  public FrameLabel() {
  }

  /**
   * Create a frame label structure.
   * @param frame  The frame.
   * @param label  The frame name.
   */
  public FrameLabel(int frame, String label) {
    this.frame = frame;
    this.label = label;
  }

  /**
   * Get the frame.
   * @return The frame.
   */
  public int getFrame() {
    return frame;
  }

  /**
   * Get the frame's label.
   * @return The frame's label.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Save the component's state.
   * @param	oo	The stream where the state should be saved.
   * @throws    IOException
   */
  public void writeExternal(ObjectOutput oo) throws IOException
  {
    ESlateFieldMap2 map = new ESlateFieldMap2(storageVersion);

    map.put(LABEL, label);
    map.put(FRAME, frame);

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

    frame = map.get(FRAME, 0);
    label = (String)map.get(LABEL);
  }
}