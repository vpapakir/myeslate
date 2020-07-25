package org.cbook.cbookif;

/**
 * Listener for CBookEvents.
 * @author Wim van Velthoven
 */

public interface CBookEventListener {
	/**
	 * Accept the command and parameters of this event.
	 * @param event
	 */
	void acceptCBookEvent(CBookEvent event);
}
