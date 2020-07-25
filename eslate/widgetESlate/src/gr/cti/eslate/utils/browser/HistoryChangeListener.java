package gr.cti.eslate.utils.browser;

import java.util.*;

/**
 * Listener for events generated when the browser's history is modified.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface HistoryChangeListener extends EventListener
{
  /**
   * Invoked when the browser's history is modified.
   * @param     e       The event describing the change.
   */
  public void historyChanged(HistoryChangedEvent e);
}
