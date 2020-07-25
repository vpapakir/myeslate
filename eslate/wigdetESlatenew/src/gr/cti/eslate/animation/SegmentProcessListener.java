package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about entering/exiting a segment.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-May-2002
 */
public interface SegmentProcessListener extends EventListener
{
  /**
   * Invoked when enter a segment.
   * @param   e   The segment process event.
   */
  public void segmentEntered(SegmentProcessEvent e);

  /**
   * Invoked when exit a segment.
   * @param   e   The segment process event.
   */
  public void segmentExited(SegmentProcessEvent e);
}

