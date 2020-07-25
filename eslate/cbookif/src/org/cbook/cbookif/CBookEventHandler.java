package org.cbook.cbookif;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Supporting class for the event wiring.
 * @author Wim van Velthoven
 *
 */
public class CBookEventHandler {
	private Object source;
	private Map<String,Set<CBookEventListener>> listeners;
	

	public CBookEventHandler(Object source) {
		super();
		this.source = source;
		this.listeners = new HashMap<String, Set<CBookEventListener>>();
	}
	
	/**
	 * Add a listener. The listerer will be invoked if the <em>command</em> is sent.
	 * <tt>null</tt> means all commands.
	 * @param listener the listener
	 * @param command the command listener interested in.
	 */
	public void addCBookEventListener(CBookEventListener listener, String command)
	{
		Set<CBookEventListener> list = listeners.get(command);
		if (list == null) {
			listeners.put(command, list = new HashSet<CBookEventListener>());
		}
 		if(listener != null) list.add(listener);
	}

	/**
	 * Remove a listener. Use same command.
	 * @param listener
	 * @param command
	 */
	public void removeCBookEventListener(CBookEventListener listener, String command)
	{
		Set<?> list = listeners.get(command);
		if (list == null) {
			return;
		}
 		list.remove(listener);
	}
	
	/**
	 * Fire a CBookEvent. Invoke all interested listeners.
	 * @param event the event
	 * @see CBookEventListener#acceptCBookEvent(CBookEvent)
	 */
	public void fire(CBookEvent event) {
		HashSet<CBookEventListener> set = new HashSet<CBookEventListener>();
		Set<CBookEventListener> list = listeners.get(event.getCommand());
		if(list != null) set.addAll(list);
		list = listeners.get(null);
		if(list != null) set.addAll(list);
		for (Iterator<CBookEventListener> iterator = set.iterator(); iterator.hasNext();) {
			CBookEventListener l = iterator.next();
			l.acceptCBookEvent(event);
		}
	}

	/**
	 * Fire a command without parameters
	 * @param command
	 */
	public void fire (String command) {
		fire(new CBookEvent(source, command));
	}

	/**
	 * Fire a cbook event with command and parameters
	 * @param command
	 * @param parameters
	 */
	public void fire (String command, Map<String, ?> parameters) {
		fire(new CBookEvent(source, command, parameters));		
	}
	
	/**
	 * Fire a command with a single parameter.
	 * @param command
	 * @param key
	 * @param value
	 */
	public void fire (String command, String key, Object value) {
		Map<String,?> parameters = Collections.singletonMap(key, value);
		fire(command, parameters);
	}
	/**
	 * Fire a command with a single message.
	 * @param command
	 * @param message
	 */
	public void fire (String command, String message) {
		
		fire(new CBookEvent(source, command, message));
	}
	
}
