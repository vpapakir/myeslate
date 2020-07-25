package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.geom.Heading;

import java.awt.geom.Point2D;

/**
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 17-May-2000
 */
public interface IAgentHost {
    /**
     * Informs the host that the agent's location has been changed.
     * @param   agent       The agent.
     */
    public abstract void locationChanged(gr.cti.eslate.protocol.IAgent agent);
    /**
     * Informs the host that the agent's location has been changed.
     * @param   agent       The agent.
     * @param   repaint     The agent wish to be repainted or not.
     */
    public abstract void locationChanged(gr.cti.eslate.protocol.IAgent agent,boolean repaint);
    /**
     * Forces the host to repaint the agent. The agent may need this when it changes
     * its face or whenever it thinks that the host has an inconsistent image of its.
     */
    public abstract void repaintAgent(gr.cti.eslate.protocol.IAgent agent);
    /**
     * Forces the host to repaint the path of the agent. The agent may need this when it clears
     * its trail or whenever it thinks that the host has an inconsistent trail of its.
     */
    public abstract void repaintTrail(gr.cti.eslate.protocol.IAgent agent);
    /**
     * Informs the host that the agent's orientation has been changed.
     * @param   agent   The agent.
     */
    public abstract void orientationChanged(gr.cti.eslate.protocol.IAgent agent);
    /**
     * Tells the host to make a graphic effect to show where the agent is.
     */
    public abstract void locateAgent(gr.cti.eslate.protocol.IAgent agent);
    /**
     * Informs the host for a change in the path properties.
     */
    public abstract void pathPropertiesChanged(gr.cti.eslate.protocol.IAgent agent);
    /**
     * @return  The map the agent will travel on on this host.
     */
    public abstract IMapView getMap();
    /**
     * @return  The layer which is considered the "road" layer, of whatever type it is.
     */
    public abstract ILayerView getRoadLayer();
    /**
     * @return  The layer which is considered the "railway" layer, of whatever type it is.
     */
    public abstract ILayerView getRailwayLayer();
    /**
     * @return  The layer which is considered the "sea" layer, of whatever type it is.
     */
    public abstract ILayerView getSeaLayer();
    /**
     * @return  The layer which is considered the "airway" layer, of whatever type it is.
     */
    public abstract ILayerView getAirwayLayer();
    /**
     * @return  The motion layer whith the given ID, of whatever type it is.
     */
    public abstract ILayerView getCustomMotionLayer(String id);
    /**
     * Informs the host that the agent embarked another agent.
     */
    public abstract void embarkedAgent(IAgent hostAgent,IAgent embarkedAgent);
    /**
     * Informs the host that the agent disembarked an agent.
     */
    public abstract void disembarkedAgent(IAgent hostAgent,IAgent disembarkedAgent);
    /**
     * The coordinate system. May be polar or cartesian.
     * @return  One of IRegionView.COORDINATE_CARTESIAN,IRegionView.COORDINATE_TERRESTRIAL.
     */
    public abstract int getCoordinateSystem();
    /**
     * Coordinate units per meter.
     */
    public abstract double getUnitsPerMeter();
    /**
     * Meters per pixel.
     */
    public abstract double getMetersPerPixel();
	/**
	 * The zoom in (1 is 100%).
	 */
	public abstract double getZoom();
    /**
     * Calculates a distance between to given points.
     */
    public abstract double calculateDistance(double long1,double lat1,double long2,double lat2);
    /**
     * @return  If agent <code>agent</code> can embark on the agent named <code>agentName</code>,
     * the function returns a handle to agent <code>agentName</code>. Otherwise it returns <code>null</code>.
     */
    public abstract IAgent agentCanEmbarkOn(IAgent agent,String agentName);
    /**
     * Returns a handle to the agent named <code>agentName</code>. This method is useful when an object
     * wants to get a handle to an agent hosted by this host.
     */
    public abstract IAgent getAgent(String agentName);




	/**
	 * Asks for the location after moving for the given amount of meters,
	 * starting from the location in <code>start</code> point and heading
	 * to <code>heading</code>. The new location is written on the <code>start</code>
	 * point and the method returns the number of meters that have actually been
	 * covered. If 0, the <code>start</code> point remains intact.
	 * @param   meters  The distance to travel.
	 * @param   start   The starting point.
	 * @param   heading The heading.
	 * @param   continueAsFar   Tell to continue as far as possible, possibly moving on more than one objects.
	 * @param   layer   The layer to move on.
	 * @param   preference  The preferred geographic object to move on. May be null
	 *                      upon calling but in return it shows the object used to do the motion.
	 * @param   nodePoints  Actually a return array which keeps node points that should be added to the path.
	 * @param   motrep      A reusable report that is used to return information to the caller.
     * @return  A <code>MotionReport</code> object with aspects of the motion.
	 */
	public MotionReport goForMeters(double meters,Point2D start,double heading,boolean continueAsFar,ILayerView layer,Heading preference,Point2D[] nodePoints,MotionReport motrep);
}
