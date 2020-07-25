package gr.cti.eslate.base;

import java.util.*;

/**
 * The listener interface for receiving events about microworld changes.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface MicroworldChangedListener extends EventListener
{
  /**
   * Invoked when the microworld of a component is successfully changed.
   */
  public void microworldChanged(MicroworldChangedEvent e);
}
