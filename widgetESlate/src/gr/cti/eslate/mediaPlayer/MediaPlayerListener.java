package gr.cti.eslate.mediaPlayer;

import java.util.EventListener;

/**
 * The listener interface for receiving events about changes at the media
 * player.
 * 
 * @author augril
 */
public interface MediaPlayerListener extends EventListener {
	/**
	 * Invoked when media player starts.
	 * 
	 * @param e
	 *            The media player event.
	 */
	public void playerStarted(MediaPlayerEvent e);
	
	/**
	 * Invoked when media player pauses.
	 * 
	 * @param e
	 *            The media player event.
	 */
	public void playerPaused(MediaPlayerEvent e);

	/**
	 * Invoked when media player stops.
	 * 
	 * @param e
	 *            The media player event.
	 */
	public void playerStopped(MediaPlayerEvent e);
	
	/**
	 * Invoked when media player finishes play.
	 * 
	 * @param e
	 *            The media player event.
	 */
	public void playerFinished(MediaPlayerEvent e);
}
