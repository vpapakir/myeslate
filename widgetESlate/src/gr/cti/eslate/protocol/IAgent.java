package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.geom.Heading;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Iterator;

public interface IAgent extends DistanceControlInterface,SteeringControlInterface,TimeControlInterface {
	/**
	 * @return  The name of the agent.
	 */
	public String getName();
	/**
	 * Sets the latitude and longitude of the agent. The agent may refuse to accept the change.
	 */
	public void setLongLat(double longt,double lat) throws gr.cti.eslate.agent.AgentRefusesToChangePositionException;
	/**
	 * @return <em>True</em> if the agent has been positioned.
	 */
	public abstract boolean isPositioned();
	/**
	 * @return The agent latitude and longitude or null if the agent is not positioned.
	 */
	public abstract Point2D.Double getLongLat();
	/**
	 * @return The agent latitude or DOUBLE.MAX_VALUE if the agent is not positioned.
	 */
	public abstract double getLatitude();
	/**
	 * @return The agent longitude or DOUBLE.MAX_VALUE if the agent is not positioned.
	 */
	public abstract double getLongitude();
	/**
	 * @return The tilt angle from the upright in [0,360) degrees anti-clockwise.
	 */
	public abstract double getTiltFromUpright();
	/**
	 * Sets the tilt angle from the upright.
	 */
	public abstract void setTiltFromUpright(double tilt);
	/**
	 * Turns the agent <code>t</code> degrees. If <code>t</code> is positive the agent turns anti-clockwise.
	 * If <code>t</code> is negative the agen turns clockwise.
	 */
	public abstract void turn(double t);
	/**
	 * @return The Path of the Agent.
	 */
	public abstract gr.cti.eslate.agent.Path getPath();
	/**
	 * Paints the current face (according to current tilt) of the agent in the given Graphics context.
	 * The agent will draw itself from (0,0).
	 */
	public abstract void paintFace(Graphics g);
	/**
	 * Paints the current face (according to the given angle in degrees) of the agent in the given Graphics context.
	 * The agent will draw itself from (0,0).
	 */
	public abstract void paintFace(Graphics g,double angle);
	/**
	 * Paints the current face (according to the given angle in degrees) of the agent in the given Graphics context.
	 * The agent will draw itself from (0,0) and use the given frame, if animated.
	 */
	public abstract void paintFace(Graphics g,double angle,int frame);
	/**
	 * @return  The size of the rectangle in pixels needed by the agent to paint its face in the current tilt.
	 */
	public abstract Dimension getFaceSize();
	/**
	 * @return  The size of the rectangle in pixels needed by the agent to paint its face in the given angle.
	 * @param   angle In degrees.
	 */
	public abstract Dimension getFaceSize(double angle);
	/**
	 * @return  One of the <code>TRAVELS_xxx</code> constants, identifying where the agent wants to walk on.
	 */
	public abstract int travelsOn();
	/**
	 * @return  If <code>travelsOn()</code> returns <code>TRAVELS_ON_CUSTOM_MOTION_LAYER</code>, this method returns the id of the needed motion layer.
	 */
	public abstract String getTravellingOnMotionLayerID();
	/**
	 * @return  True, if the agent can embark on agent <code>agent</code>.
	 */
	public abstract boolean canEmbarkOn(IAgent agent);
	/**
	 * @return  If the agent can embark on the agent named <code>agentName</code>, the function returns
	 *          a handle to the agent. Otherwise it returns <code>null</code>.
	 */
	public abstract IAgent canEmbarkOn(String agentName);
	/**
	 * Tells the agent to embark agent <code>agent</code>.
	 */
	public abstract void embark(IAgent agent);
	/**
	 * Tells the agent to disembark agent <code>agent</code>.
	 * @return  True, if the agent was embarked and disembarked successfuly.
	 */
	public abstract boolean disembark(IAgent agent);
	/**
	 * Tells the agent to disembark agent named <code>agentName</code>.
	 * @return  True, if the agent was embarked and disembarked successfuly.
	 */
	public abstract boolean disembark(String agentName);
	/**
	 * @return  One of the <code>TYPE_xxx</code> constants, identifying what the agent is.
	 */
	public abstract int type();
	/**
	 * Gets the id of the agent type.
	 */
	public abstract String getType();
	/**
	 * An Iterator of the embarked agents, if any.
	 */
	public abstract Iterator getEmbarkedAgents();
	/**
	 * How many embarked agents, if any.
	 */
	public abstract int getEmbarkedAgentsCount();
	/**
	 * <code>True</code>, if the agent is embarked in another agent.
	 */
	public abstract boolean isEmbarked();
	/**
	 * <code>True</code>, if the agent has other agents embarked on it.
	 */
	public abstract boolean hasAgentsEmbarked();
	/**
	 * @return  The agent that carries this agent.
	 */
	public abstract IAgent getCarryingAgent();
	/**
	 * Sets the agent that carries this agent.
	 */
	public abstract void setCarryingAgent(IAgent agent);
	/**
	 * Called by the carrier agent on the embarked agent.
	 * @param   longt   The new longitude position.
	 * @param   lat     The new latitude position.
	 */
	public abstract void carrierAgentMoved(double longt,double lat);
	/**
	 * Tells the agent to ask all its hosts and reply if this is a valid location.
	 */
	public abstract boolean isValidLocation(double longt,double lat);
	/**
	 * @return  The measure of the velocity the agent has in the moment of the method call. (In km/h)
	 */
	public abstract double getVelocity();
	/**
	 * Sets the measure of the velocity the agent has in the moment of the method call. (In km/h)
	 */
	public abstract void setVelocity(double d);
	/**
	 * @return  The maximum value of the velocity. (In km/h)
	 */
	public abstract double getMaxVelocity();
	/**
	 * Sets the maximum value of the velocity. (In km/h)
	 */
	public abstract void setMaxVelocity(double d);
	/**
	 * @return  The minimum value of the velocity. (In km/h)
	 */
	public abstract double getMinVelocity();
	/**
	 * Sets the minimum value of the velocity. (In km/h)
	 */
	public abstract void setMinVelocity(double d);
	/**
	 * Starts motion with the currently set properties of velocity and heading.
	 */
	public abstract void go();
	/**
	 * Tells the agent to go to the given lat-long position, walking through all the intermediate points.
	 */
	public abstract void goTo(double longt,double lat);
	/**
	 * Tells the agent to go to the given lat-long position, walking through all the intermediate points.
	 * The host parameter identifies which host wants to move the agent. If the simple <code>goTo</code>
	 * function is used, the agent will move on any of the hosts, probably producing an unwanted effect,
	 * e.g. when the agent travels on two maps, on one inside a polygon and on the other on a line,
	 * the user clicking on the map with the lines may see the agent travelling in the polygon.
	 */
	public abstract void goTo(IAgentHost host,double longt,double lat);
	/**
	 * Tells the agent to leave a trail or not.
	 */
	public abstract void setTrailOn(boolean trail);
	/**
	 * Asks the agent if it leaves a trail or not.
	 */
	public abstract boolean isTrailOn();
	/**
	 * Clears the trail.
	 */
	public abstract void clearTrail();
	/**
	 * Clears location and trail. After calling this method, the agent is unpositioned, in the initial state.
	 * It disconnects from all the hosts.
	 */
	public abstract void forgetEverything();
	/**
	 * This property indicates when this agent wants to be always visible. This means that a component
	 * hosting this agent should (but is not forced to) make it visible wherever it is. E.g. if the
	 * agent is hosted in a map, the map should scroll if the agent goes out of the visible map area.
	 */
	public boolean isAlwaysVisible();
	/**
	 * Informs the agent that it has met with another agent. This method is
	 * usually called by an agent host.
	 */
	public void agentMetWithAgent(IAgent metAgent);
	/**
	 * Stops the agent from anything it does.
	 */
	public void stop();
    /**
     * Initializes the last "travelled on object" to the first line the agent is positioned.
     * @param head  The heading.
     */
    public void setLastTravelledOnObject(Heading head);
	/**
	 * Makes the agent "sleep" (wait) for the given amount of days.
	 * @param   days    The amount of time to sleep.
	 */
	public void sleepDays(int days);
	/**
	 * Makes the agent "sleep" (wait) for the given amount of seconds.
	 * @param   sec The amount of time to sleep.
	 */
	public void sleep(long sec);
	/**
	 * Wakes the agent up if it sleeps.
	 */
	public void wake();
	/**
	 * Property that tells the agent to stop at road crossings.
	 */
	public void setStopAtCrossings(boolean stop);
	/**
	 * Property that tells the agent to stop at road crossings.
	 */
	public boolean getStopAtCrossings();
	/**
	 * Property that tells if the agent uses an animated icon when walking.
	 * @return  Animated when true.
	 */
	public boolean isAnimated();


	/**
	 * Static value to identify where the agent wants to walk on.
	 */
	public static final int TRAVELS_EVERYWHERE=0;
	/**
	 * Static value to identify where the agent wants to walk on.
	 */
	public static final int TRAVELS_ON_ROADS=1;
	/**
	 * Static value to identify where the agent wants to walk on.
	 */
	public static final int TRAVELS_ON_RAILWAYS=2;
	/**
	 * Static value to identify where the agent wants to walk on.
	 */
	public static final int TRAVELS_ON_SEA=3;
	/**
	 * Static value to identify where the agent wants to walk on.
	 */
	public static final int TRAVELS_ON_AIR=4;
	/**
	 * Static value to identify where the agent wants to walk on.
	 */
	public static final int TRAVELS_ON_CUSTOM_MOTION_LAYER=5;
	/**
	 * Static value to identify the type of the agent.
	 */
	public static final int TYPE_MAN=0;
	/**
	 * Static value to identify the type of the agent.
	 */
	public static final int TYPE_AUTO=1;
	/**
	 * Static value to identify the type of the agent.
	 */
	public static final int TYPE_TRAIN=2;
	/**
	 * Static value to identify the type of the agent.
	 */
	public static final int TYPE_SHIP=3;
	/**
	 * Static value to identify the type of the agent.
	 */
	public static final int TYPE_AIRPLANE=4;
	/**
	 * Static value to identify the type of the agent.
	 */
	public static final int TYPE_OTHER=5;
}