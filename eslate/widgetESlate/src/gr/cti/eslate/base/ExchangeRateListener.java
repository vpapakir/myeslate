package gr.cti.eslate.base;

import java.util.*;

/**
 * This interface must be implemented by components wishing to listen for
 * events triggered by the change of a currency exchange rate.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ExchangeRateListener extends EventListener
{
  /**
   * Invoked when a currency exchange rate changes.
   * @param     e       The event signalling the change.
   */
  public void exchangeRateChanged(ExchangeRateEvent e);
}
