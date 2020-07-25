package gr.cti.eslate.agent;

import java.util.EventObject;

/**
 * Title:        Agent
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:      CTI
 * @author Giorgos Vasiliou
 * @version 1.0
 */

public class AgentEvent extends EventObject {

    private Object oldValue,newValue;

    public AgentEvent(Object source) {
        super(source);
    }

    public AgentEvent(Object source,Object oldValue,Object newValue) {
        super(source);
        this.oldValue=oldValue;
        this.newValue=newValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

	public Agent getAgent() {
		if (source instanceof Agent)
			return (Agent) source;
		else
			return null;
	}
}