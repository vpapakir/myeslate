package gr.cti.eslate.scripting;

import gr.cti.eslate.base.*;

/**
 * This interface describes the functionality of the Panel component
 * that is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 * @see gr.cti.eslate.panel.PanelComponent
 */

public interface AsPanel
{
  /**
   * Returns a list of the E-Slate handles of all hosted E-Slate components.
   * @return    An array containing references to the E-Slate handles of all
   *            hosted components that have an E-Slate handle associated with
   *            them. Other components are omitted from the list.
   */
  public ESlateHandle[] listHostedESlateHandles();
}
