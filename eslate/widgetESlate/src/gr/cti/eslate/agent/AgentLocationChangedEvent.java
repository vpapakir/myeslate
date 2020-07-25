package gr.cti.eslate.agent;

import java.util.EventObject;

/**
 * Event to inform for a change in the location of the agent.
 * @author Giorgos Vasiliou
 * @version 1.0
 * @since 1.4.6
 */

public class AgentLocationChangedEvent extends EventObject {

    public AgentLocationChangedEvent(Object source) {
        super(source);
	    repaint=true;
    }

    public AgentLocationChangedEvent(Object source,boolean repaint) {
        super(source);
	    this.repaint=repaint;
    }

	public Agent getAgent() {
		if (source instanceof Agent)
			return (Agent) source;
		else
			return null;
	}

	/**
	 * Tells to repaint or not.
	 */
	private boolean repaint;
}