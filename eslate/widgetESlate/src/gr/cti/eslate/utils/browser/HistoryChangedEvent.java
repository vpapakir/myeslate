package gr.cti.eslate.utils.browser;

import java.util.*;

/**
 * Event sent when the browser's history is modified.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class HistoryChangedEvent extends EventObject
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  /**
   * Create a history changed event.
   * @param     browser The browser whose history was modified. This can be
   *                    obtained from the event by invoking its getSource()
   *                    method.
   */
  public HistoryChangedEvent(ESlateBrowser browser)
  {
    super(browser);
  }
}
