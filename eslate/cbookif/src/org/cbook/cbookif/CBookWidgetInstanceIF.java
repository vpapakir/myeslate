package org.cbook.cbookif;

import java.util.Map;

import javax.swing.JComponent;
/**
 * Interface for a widget instance.<br>
 * Life cycle:
 * <dl>
 * <dt>init<dd>initializes
 * <dt>setLaunchdata<dd>get configuration data
 * <dt>setAssessmentMode<dd>select mode
 * <dt>setState<dd>restore student data from
 * <dt>start<dd>start this widgetinstance
 * <dt>reset
 * <dt>check
 * <dt>zetNagekeken
 *    <dd>events from the system
 * <dt>stop<dd>stop this instance
 * <dt>getState<dd>persist student data 
 * <dt>destroy<dd>cleanup
 * </dl>
 * @author wim
 * @version 1.0
 */
public interface CBookWidgetInstanceIF {
	/**
	 * Get component to insert into the Swing hierarchy.
	 * @return component 
	 */
	JComponent asComponent();
	/**
	 * Set configuration data
	 * @param data
	 * @param randomValues
	 */
	void setLaunchData(Map<String, ?> data, Map<String, Number> randomValues);
	
	/**
	 * Set the mode. A widget instance can behave differently.
	 * @param mode the mode
	 */
	void setAssessmentMode(AssessmentMode mode);
	
	/** load suspend data into this instance.
	 * @param state a map
	 */
	void setState(Map<String,?> state);
	/**
	 * Save the suspend data
	 * @return a hashtable or a json object.
	 */
	Map<String,?>  getState();
	/**
	 * Get the score of this instance
	 * @return an int
	 */
	int getScore();
	
	/**
	 * Get the success status: Passed, failed or unknown.
	 * @return the status
	 */
	SuccessStatus getSuccessStatus(); 
	
	/**
	 * Wiring for events.
	 * Add an eventhandler
	 * @param listener the Listener
	 * @param command  expression matching commands? null = all
	 */
	void addCBookEventListener(CBookEventListener listener, String command);

	/**
	 * Wiring for events.
	 * Remove an eventhandler
	 * @param listener 
	 * @param command  expression matching commands? null = all
	 */	
	void removeCBookEventListener(CBookEventListener listener, String command);
	
	
	// lifecycle things like an applet
	/**
	 * Instance lifecycle method. Initialize the instance.
	 */
	void init();
	void start();
	void stop();
	void destroy();
	/**
	 * User pressed the "reset" button.
	 */
	void reset();

	/**
	 * Get a listener for CBookEvents.
	 * @return the listener (possible this)
	 */
	CBookEventListener asEventListener();
}
