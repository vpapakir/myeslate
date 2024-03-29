package gr.cti.eslate.agent;

/**
 * Title:        Agent
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      CTI
 * @author Giorgos Vasiliou
 * @version 1.0
 */

public interface AgentListener extends java.util.EventListener {
    public abstract void locationChanged(AgentLocationChangedEvent e);
	/**
	 * Invoked when the agent touches a geographic object in a layer for the first time.
	 */
	public abstract void geographicObjectTouched(GeoObjectAgentEvent e);
	/**
	 * Invoked when the agent meets another agent in a host. Meeting is equivalent to
	 * sprite collision, thus two agents meet in different distances when viewed in
	 * different map scales.
	 */
	public abstract void agentMeeting(AgentMeetingEvent e);
	/**
	 * Invoked when the agent is moving on a motion layer with objects (lines or polygons)
	 * and the object on which the agent sit changes.
	 */
	public abstract void motionObjectChanged(GeoObjectAgentEvent e);
	/**
	 * Invoked when the agent was moving and is now stopped, either
	 * because a stop command has been invoked or the agent has nowhere else to go.
	 */
	public abstract void agentStopped(AgentEvent e);
	/**
	 * Invoked when the agent is going to make a step.
	 */
	public abstract void agentToMakeStep(AgentEvent e);
}