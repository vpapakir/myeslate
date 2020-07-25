package org.cbook.cbookif;

/**
 * All constants used in CBook interfaces.
 * @author Wim van Velthoven
 */
public interface Constants {
// context properties
	/** 
	 * Contextproperty for Font.
	 */
	String FONT = "font";
	/**
	 * Contextproperty for background color.
	 */
	String BACKGROUND = "background";
	/**
	 * Contextproperty for foreground color.
	 */
	String FOREGROUND = "foreground";
// commands and parameters
	/**
	 * CBookEvent command with a message.
	 */
	String USER_INPUT = "input";
	/**
	 * Contextproperty (Boolean) and CBookEvent Command.
	 * If true, send logging events with parameters LOG_ID, SUCCESS_STATUS, SCORE and USER_INPUT.
	 */
	String LOGGING  = "logOption"; // option/action
	/**
	 * Contextproperty and parameter CBookEvent. The ID of a widgetinstance for logging.
	 * String.
	 */
	String LOG_ID  = "logID";
	/**
	 * The succes status of a logging event.
	 * @see SuccessStatus
	 */
	String SUCCESS_STATUS = "success_status";
	/**
	 * The score of a logging event. Number.
	 */
	String SCORE = "score";
	/**
	 * Contextproperty (Number) for maximum score.
	 */
	String MAX_SCORE = "scoreMax";
	/**
	 * Identifier of a student. Context property (String)
	 */
	String LEARNER_ID = "learner_id"; // taken from imsglobal scorm 2004
	/**
	 * Full name of a student. Context property (String)
	 * Format: surname, givenname
	 */
	String LEARNER_NAME = "learner_name"; // taken from imsglobal scorm 2004
	/**
	 * Unique identifier for widget instances. Context propertiy (String)
	 */
	String UUID = "UUID";
	/**
	 * Dimension of the instance. Context property (Dimension)
	 */
	String  INSTANCE_SIZE = "instanceSize";
	/**
	 * The resource manager. Context property.
	 * @see org.cbook.cbookif.rm.ResourceManager
	 */
	String RESOURCE_MANAGER = "resourceManager";
	/**
	 * The widget instance has changed its score. CBookEvent.
	 */
	String CHANGED = "changed";
	/**
	 * The widget instance is checked. The user pressed a 'check' button. Implies <em>changed</em>.
	 * CBookEvent.
	 * @see #CHANGED
	 */
	String CHECKED = "checked";
	/**
	 * Force a check. The user pressed a global 'check' button.
	 * CBookEvent.
	 */
	String CHECK = "check";
}
