package gr.cti.eslate.base;

import java.util.*;

/**
 * This interface must be implemented by components wishing to listen for
 * events triggered by the connection of a plug to another plug.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public interface ConnectionListener extends EventListener
{
  public void handleConnectionEvent(ConnectionEvent e);
}
