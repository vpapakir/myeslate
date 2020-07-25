package gr.cti.eslate.mediaPlayer;

import java.io.File;

/**
 * Media player interface.
 * 
 * @author augril
 */
public interface IPlayer {
	/**
	 * Open a media file.
	 * 
	 * @param file
	 *            The file to open.
	 */
	public void open(File file);

	/**
	 * Exit media player.
	 */
	public void exit();
	
	/**
	 * Start media player.
	 */
	public void start();
	
	/**
	 * Pause media player.
	 */
	public void pause();
	
	/**
	 * Stop media player.
	 */
	public void stop();
	
	/**
	 * Close media file.
	 */
	public void close();
}
