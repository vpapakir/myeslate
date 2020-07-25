package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about changes at the segment.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public interface SegmentListener extends EventListener
{
  /**
   * Invoked when a milestone is added.
   * @param   e   The segment event.
   */
  public void milestoneAdded(SegmentEvent e);

  /**
   * Invoked when a milestone is removed.
   * @param   e   The segment event.
   */
  public void milestoneRemoved(SegmentEvent e);
}

