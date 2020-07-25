package gr.cti.eslate.timeMachine2;

import java.util.EventObject;

/**
 * Event triggered from changes at time machine. The event's getSource() method
 * will return the E-Slate handle of the media player.
 * 
 * @author augril
 */
public class ScaleEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an event.
	 * 
	 * @param source
	 *            The source of the event.
	 */
	public ScaleEvent(Object source) {
		super(source);
	}
}
