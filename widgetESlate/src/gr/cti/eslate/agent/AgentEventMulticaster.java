package gr.cti.eslate.agent;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Title:        Agent
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:      CTI
 * @author Giorgos Vasiliou
 * @version 1.0
 */

public class AgentEventMulticaster extends HashSet<AgentListener> implements AgentListener {

    public AgentEventMulticaster() {
        super();
    }

    public void locationChanged(AgentLocationChangedEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((AgentListener) it.next()).locationChanged(e);
    }
	/**
	 * Invoked when the agent meets another agent in a host. Meeting is equivalent to
	 * sprite collision, thus two agents meet in different distances when viewed in
	 * different map scales.
	 */
	public void agentMeeting(AgentMeetingEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((AgentListener) it.next()).agentMeeting(e);
	}

	/**
	 * Invoked when the agent touches a geographic object in a layer for the first time.
	 */
	public void geographicObjectTouched(GeoObjectAgentEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((AgentListener) it.next()).geographicObjectTouched(e);
	}

	/**
	 * Invoked when the agent is moving on a motion layer with objects (lines or polygons)
	 * and the object on which the agent sit changes.
	 */
	public void motionObjectChanged(GeoObjectAgentEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((AgentListener) it.next()).motionObjectChanged(e);
	}
	/**
	 * Invoked when the agent was moving and is now stopped, either
	 * because a stop command has been invoked or the agent has nowhere else to go.
	 */
	public void agentStopped(AgentEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((AgentListener) it.next()).agentStopped(e);
	}

	/**
	 * Invoked when the agent is going to make a step.
	 */
	public void agentToMakeStep(AgentEvent e) {
		Iterator it=iterator();
		while (it.hasNext())
			((AgentListener) it.next()).agentToMakeStep(e);
	}
}