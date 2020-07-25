package gr.cti.eslate.agent;

import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.ILayerView;

import java.util.EventObject;

/**
 * Event when the agent touches a geographic object.
 * @author Giorgos Vasiliou
 * @version 1.0, 17-Feb-2003
 * @since 1.3.2
 */

public class GeoObjectAgentEvent extends EventObject {
    /**
     * Constructs a new event.
     * @param agent The source of the event, the agent.
     * @param layer The layer the geographic object belongs to.
     * @param obj   The geographic object.
     */
    public GeoObjectAgentEvent(Agent agent,ILayerView layer,GeographicObject obj) {
        super(agent);
	    this.layer=layer;
	    this.obj=obj;
    }

    /**
     * Constructs a new event.
     * @param agent The source of the event, the agent.
     * @param layer The layer the geographic object belongs to.
     * @param obj   The geographic object.
     * @param prev  The previous geographic object.
     */
    public GeoObjectAgentEvent(Agent agent,ILayerView layer,GeographicObject obj,GeographicObject prev) {
        super(agent);
	    this.layer=layer;
	    this.obj=obj;
	    this.prev=prev;
    }

	/**
	 * The agent on which the Event initially occurred.
	 * @return   The agent on which the Event initially occurred.
	 */
	public Agent getAgent() {
		return (Agent) super.getSource();
	}

    /**
     * Gets the layer that the geographic object belongs to.
     * @return  The layer.
     */
	public ILayerView getLayer() {
		return layer;
	}

	/**
	 * Gets the geographic object.
	 * @return  The geographic object.
	 */
	public GeographicObject getGeographicObject() {
		return obj;
	}

	/**
	 * Gets the previous geographic object, if one is defined for the event.
	 * @return  The geographic object.
	 */
	public GeographicObject getPreviousGeographicObject() {
		return prev;
	}

	/**
	 * The layer.
	 */
	private ILayerView layer;
	/**
	 * The object.
	 */
	private GeographicObject obj;
	/**
	 * The previous object.
	 */
	private GeographicObject prev;
}