package gr.cti.eslate.mediaPlayer;

import java.util.EventObject;

/**
 * Event triggered from changes at media player. The event's
 * getSource() method will return the E-Slate handle of the media player.
 * 
 * @author augril
 */
public class MediaPlayerEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a media player event.
	 * 
	 * @param source
	 *            The source of the event.
	 */
	public MediaPlayerEvent(Object source) {
		super(source);
	}
}
