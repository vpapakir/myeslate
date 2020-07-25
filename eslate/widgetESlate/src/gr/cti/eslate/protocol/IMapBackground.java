package gr.cti.eslate.protocol;

import java.util.Date;

/**
 * A Region Background image.
 * @author Giorgos Vasiliou
 * @version 1.0, 20-Aug-2002
 */

public interface IMapBackground extends javax.swing.Icon {
	/**
	 * Clears the resources kept by the background. The resources will
	 * be automatically reloaded when needed.
	 */
	public abstract void clearImageData();
	/**
	 * Fetches the image if it has not been loaded.
	 * This method, as well as <code>paintIcon()</code>, ensures that the image is fully loaded.
	 */
	public abstract java.awt.Image getImage();
	/**
	 * Sets the date from which this background exists.
	 * @param   from  The date or null if this is not a time-aware background.
	 */
	public abstract void setDateFrom(Date from);
	/**
	 * Gets the date from which this background exists.
	 * @return  The date or null if this is not a time-aware background.
	 */
	public abstract Date getDateFrom();
	/**
	 * Sets the date until which this background exists.
	 * @param   to  The date or null if this is not a time-aware background.
	 */
	public abstract void setDateTo(Date to);
	/**
	 * Gets the date until which this background exists.
	 * @return  The date or null if this is not a time-aware background.
	 */
	public abstract Date getDateTo();
	/**
	 * Gets the filename of the background.
	 * @return  A String which is the filename of the background.
	 */
	public String getFilename();
}