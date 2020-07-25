package gr.cti.eslate.registry.event;

import java.util.*;

/**
 * Listener for registry modification events.
 *
 * @version     2.0.0, 25-May-2006
 * @author      Kriton Kyrimis
 */
public interface RegistryListener extends EventListener
{
  /**
   * Invoked when the value of a variable changes.
   * @param     e       The event signaling the change.
   */
  public void valueChanged(RegistryEvent e);

  /**
   * Invoked when the comment associated with a variable changes.
   * @param     e       The event signaling the change.
   */
  public void commentChanged(RegistryEvent e);

  /**
   * Invoked when the persistence state of a variable changes.
   * @param     e       The event signaling the change.
   */
  public void persistenceChanged(RegistryEvent e);

  /**
   * Invoked when a variable is added to the registry.
   * @param     e       The event signaling the addition.
   */
  public void variableAdded(RegistryEvent e);

  /**
   * Invoked when a variable is removed from the registry.
   * @param     e       The event signaling the removal.
   */
  public void variableRemoved(RegistryEvent e);

  /**
   * Invoked when the registry is cleared.
   * @param     e       The event signaling the clearing of the registry.
   */
  public void registryCleared(RegistryEvent e);
  
}
