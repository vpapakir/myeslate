package gr.cti.eslate.scripting;


/**
 * This interface describes the functionality of the Animation component
 * that is available to the Logo scripting mechanism.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 24-Jun-2002
 * @see	gr.cti.eslate.animation.Animation
 */

public interface AsAnimation
{
  /**
   * Start playing from a frame.
   * @param startFrame Start frame.
   */
  public void playFromFrame(int startFrame);

  /**
   * Playing from a frame to another.
   * @param startFrame Start frame.
   * @param endFrame   End frame.
   */
  public void playFromFrameToFrame(int startFrame, int endFrame);

  /**
   * Start playing from a label.
   * @param startLabel Start label.
   */
  public void playFromLabel(String startLabel);

  /**
   * Playing from a label to another.
   * @param startLabel Start label.
   * @param endLabel   End label.
   */
  public void playFromLabelToLabel(String startLabel, String endLabel);

  /**
   * Going to a frame.
   * @param frame The frame to go.
   */
  public void goToFrame(int frame);
 }