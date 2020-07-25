package gr.cti.eslate.agent;

import java.util.EventObject;

/**
 * Event when the agent meets another agent.
 * @author Giorgos Vasiliou
 * @version 1.0, 17-Feb-2003
 * @since 1.3.2
 */

public class AgentMeetingEvent extends EventObject {
    /**
     * Constructs a new event.
     * @param agent The source of the event, the agent.
     * @param metAgent The other agent, that the source agent met.
     */
    public AgentMeetingEvent(Agent agent,Agent metAgent) {
        super(agent);
	    this.metAgent=metAgent;
    }

	/**
	 * The agent on which the Event initially occurred.
	 * @return   The agent on which the Event initially occurred.
	 */
	public Agent getAgent() {
		return (Agent) super.getSource();
	}

    /**
     * Gets the agent that the source agent has met.
     * @return  The layer.
     */
	public Agent getAgentMet() {
	    return metAgent;
    }

	/**
	 * The agent met.
	 */
	private Agent metAgent;
}