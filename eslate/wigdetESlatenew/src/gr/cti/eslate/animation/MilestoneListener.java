package gr.cti.eslate.animation;

import java.util.*;

/**
 * The listener interface for receiving events about changes at the milestone.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 25-Apr-2002
 */
public interface MilestoneListener extends EventListener
{
  /**
   * Invoked when time parameter is changed.
   * @param   e   The milestone event.
   */
  public void whenChanged(MilestoneEvent e);
}

