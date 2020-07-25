package org.cbook.cbookif;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.JComponent;

/**
 * Interface to a CBook authoring component.
 * @author Wim van Velthoven
 *
 */
public interface CBookWidgetEditIF {
/**
 * Return the swing component associated with this interface
 * @return a swing component
 */
	JComponent asComponent();

	/**
	 * Get the configuration data for a widget instance.
	 * The map should be JSON compatible. 
	 * That is, it should only contain Numbers, Booleans, Strings, Lists and Maps
	 * @return map with configuration
	 */
	Map<String,?> getLaunchData();
	
	/**
	 * Restore the configuration data previously got.
	 * @param data
	 */
	void setLaunchData(Map<String,?> data);
	
	/**
	 * The the maximum score an instance will return.
	 * @return maximum score
	 */
	int getMaxScore();
	
	/**
	 * Start the editor.
	 */
	void start();
	/**
	 * Stop the editor.
	 */
	void stop();

//	/**
//	 * Set all configuration to default.
//	 * @deprecated not used at all!
//	 */
//	void clear();
	/**
	 * Set the desired instance width.
	 * If the editor displays a sample instance. It should adapt its size.
	 * @param width
	 */
	void setInstanceWidth(int width);    // size of widget-instance 
	/**
	 * Set the desired instance height.
	 * If the editor displays a sample instance. It should adapt its size.
	 * @param height
	 */
	void setInstanceHeight(int height);  // if editor displays a sample instance. 
	/**
	 * Get the size of an instance widget.
	 */
	Dimension getInstanceSize();		 // preferred size 

// for wiring	
	/**
	 * Get all commands that are accepted
	 * @return array of commands
	 */
	String[] getAcceptedCmds();
	/**
	 * Get all commands that are fired.
	 * @return array of commands
	 */
	String[] getSendCmds();

}
