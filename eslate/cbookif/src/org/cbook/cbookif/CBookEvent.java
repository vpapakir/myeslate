package org.cbook.cbookif;

import java.util.EventObject;
import java.util.Map;

/**
 * A CBook event with a command and parameters.
 * @see CBookEventListener
 * @author Wim van Velthoven
 */
public class CBookEvent extends EventObject {

	private final String command;
	private final String message;
	private final Map<String,?> parameters;

	/**
	 * Construct a CBookEvent.
	 * @param source the cbook widget instance that originated the event.
	 * @param command
	 * @param parameters
	 */
	public CBookEvent(Object source, String command, Map<String,?> parameters) {
		super(source);
		this.command = command;
		this.parameters = parameters;
		this.message = null;
	}
	/**
	 * Construct a CBookEvent.
	 * @param source the cbook widget instance that originated the event.
	 * @param command
	 */
	public CBookEvent(Object source, String command) {
		super(source);
		this.command = command;
		this.parameters = null;
		this.message = null;
	}

	/**
	 * Construct a CBookEvent.
	 * @param source the cbook widget instance that originated the event.
	 * @param command
	 * @param message
	 */
	public CBookEvent(Object source, String command, String message) {
		super(source);
		this.command = command;
		this.parameters = null;
		this.message = message;
	}

	/**
	 * The commmand of the event.
	 * @return a command
	 * @see Constants#USER_INPUT
	 * @see Constants#LOGGING
	 */
	public String getCommand() {
		return command;
	}

	/** 
	 * The Map of parameters.
	 * @return a map
	 */
	public Map<String,?> getParameters() {
		return parameters;
	}

	/**
	 * A single parameter.
	 * @param key name of parameter
	 * @return value of parameter
	 */
	public Object getParameter(String key) {
		if(parameters != null)
			return parameters.get(key);
		return null;
	}

	/**
	 * Get the message.
	 */
	public String getMessage() {
		return message;
	}
}
