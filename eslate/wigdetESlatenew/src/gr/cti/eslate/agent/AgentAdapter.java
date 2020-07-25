package gr.cti.eslate.agent;

/**
 * Adapter class for AgentListener. All the methods do nothing.
 *
 * @author  Giorgos Vasiliou
 * @version 1.0, Feb 17, 2003
 * @since   1.3.2
 */
public class AgentAdapter implements AgentListener {
	public void locationChanged(AgentLocationChangedEvent e) {
	}

	/**
	 * Invoked when the agent meets another agent in a host. Meeting is equivalent to
	 * sprite collision, thus two agents meet in different distances when viewed in
	 * different map scales.
	 */
	public void agentMeeting(AgentMeetingEvent e) {
	}

	/**
	 * Invoked when the agent touches a geographic object in a layer for the first time.
	 */
	public void geographicObjectTouched(GeoObjectAgentEvent e) {
	}

	/**
	 * Invoked when the agent is moving on a motion layer with objects (lines or polygons)
	 * and the object on which the agent sit changes.
	 */
	public void motionObjectChanged(GeoObjectAgentEvent e) {
	}
	/**
	 * Invoked when the agent was moving and is now stopped, either
	 * because a stop command has been invoked or the agent has nowhere else to go.
	 */
	public void agentStopped(AgentEvent e) {
	}

	/**
	 * Invoked when the agent is going to make a step.
	 */
	public void agentToMakeStep(AgentEvent e) {
	}
}
