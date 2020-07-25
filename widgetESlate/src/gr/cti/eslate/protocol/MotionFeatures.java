package gr.cti.eslate.protocol;

import gr.cti.typeArray.DblBaseArray;

public class MotionFeatures {
	/**
	 * Trip validity.
	 */
	public boolean isValidTrip;
	/**
	 * Moving on earth against moving on a cartesian plane.
	 */
	public boolean isMovingOnEarth;
	/**
	 * A sorted array providing all the geographic objects near the agent.
	 */
	public GeographicObject onObject;
	/**
	 * Actual starting location, with the proper snappings done.
	 */
	public double startLongt;
	/**
	 * Actual starting location, with the proper snappings done.
	 */
	public double startLat;
	/**
	 * Actual starting location, with the proper snappings done.
	 */
	public double endLongt;
	/**
	 * Actual starting location, with the proper snappings done.
	 */
	public double endLat;
	/**
	 * The host that will perform the motion.
	 */
	public IAgentHost host;
	/**
	 * Start segment.
	 */
	public int startSegment;
	/**
	 * End segment.
	 */
	public int endSegment;
	/**
	 * Current segment in an incomplete line motion.
	 */
	public int currentSegment;
	/**
	 * Units per pixel.
	 */
	public double upp;
	/**
	 * Units per meter.
	 */
	public double upm;
	/**
	 * Distance.
	 */
	public double distance=Double.MAX_VALUE;
	/**
	 * If a point motion has been left incomplete, these are the x points left to go to.
	 */
	public DblBaseArray xPointsLeftToTravel;
	/**
	 * If a point motion has been left incomplete, these are the y points left to go to.
	 */
	public DblBaseArray yPointsLeftToTravel;
}

