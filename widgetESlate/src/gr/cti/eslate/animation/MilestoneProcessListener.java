package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about finding a milestone.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 13-May-2002
 */
public interface MilestoneProcessListener extends EventListener
{
  /**
   * Invoked when find a milestone.
   * @param   e   The milestone process event.
   */
  public void milestoneFound(MilestoneProcessEvent e);
}

