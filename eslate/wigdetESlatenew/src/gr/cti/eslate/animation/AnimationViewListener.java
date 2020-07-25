package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about changes at
 * at play thread and looped playback.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-Jun-2002
 */
public interface AnimationViewListener extends EventListener
{
  /**
   * Invoked when play thread starts.
   * @param   e   The play thread event.
   */
  public void playThreadStarted(PlayThreadEvent e);

  /**
   * Invoked when play thread stops.
   * @param   e   The play thread event.
   */
  public void playThreadStopped(PlayThreadEvent e);

  /**
   * Invoked when looped playback state changes.
   * @param   e   The loop event.
   */
  public void loopChanged(LoopEvent e);

  /**
   * Invoked when maximum fps change.
   * @param   e   The fps event.
   */
  public void fpsChanged(FpsEvent e);
}

