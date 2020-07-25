package gr.cti.eslate.utils;

import java.util.*;

/**
 * This interface must be implemented by components wishing to listen for
 * events triggered by the the modification of the E-Slate clipboard.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ESlateClipboardChangedListener extends EventListener
{
  /**
   * Handle the E-Slate clipboard changed event.
   * @param     e       The event to handle.
   */
  public void
    handleESlateClipboardChangedEvent(ESlateClipboardChangedEvent e);
}
