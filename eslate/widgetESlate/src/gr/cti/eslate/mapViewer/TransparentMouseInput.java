package gr.cti.eslate.mapViewer;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.MouseInputListener;

/**
 * Implemented by all the components of the MapPane hierarchy.
 * @author Giorgos Vasiliou
 * @version 1.0
 */

public interface TransparentMouseInput {
	/**
	 * Adds a MouseInputListener. This is different from adding a MouseListener and a MouseMotionListener seperately.
	 */
	public void addMouseInputListener(MouseInputListener l);
	/**
	 * Removes a MouseInputListener.
	 */
	public void removeMouseInputListener(MouseInputListener l);
	/**
	 * Should make public access to processMouseEvent.
	 * @param e The event.
	 */
	public void processMouseEvent(MouseEvent e);
	/**
	 * Should make public access to processMouseMotionEvent.
	 * @param e The event.
	 */
	public void processMouseMotionEvent(MouseEvent e);
	/**
	 * Removes all Mouse and MouseMotion Listeners.
	 * @return A list containing the listeners removed.
	 */
	public List removeAllListeners();
}