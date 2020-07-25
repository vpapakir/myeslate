package gr.cti.eslate.mapViewer;

import gr.cti.eslate.mapModel.geom.Heading;
import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.IAgentHost;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.MotionReport;

import java.awt.geom.Point2D;

/**
 * This class implement the IAgentHost interface. It is a lightweight class
 * that encapsulates the MotionPane which is created on demand.
 * @author Giorgos Vasiliou
 * @version 1.0
 */

class AgentHostImplementor implements IAgentHost {
	private MapPane mapPane;
	private MotionPane motion;

	public AgentHostImplementor(MapPane mapPane) {
		this.mapPane=mapPane;
	}

	private void checkMotion() {
		motion=mapPane.getMotionPane();
	}

	public void locationChanged(IAgent parm1) {
		checkMotion();
		motion.locationChanged(parm1);
	}

	public void locationChanged(IAgent parm1,boolean repaint) {
		checkMotion();
		motion.locationChanged(parm1,repaint);
	}

	public void repaintAgent(IAgent parm1) {
		checkMotion();
		motion.repaintAgent(parm1);
	}
	public void repaintTrail(IAgent parm1) {
		checkMotion();
		motion.repaintTrail(parm1);
	}
	public void orientationChanged(IAgent parm1) {
		checkMotion();
		motion.orientationChanged(parm1);
	}
	public void locateAgent(IAgent parm1) {
		checkMotion();
		motion.locateAgent(parm1);
	}
	public void pathPropertiesChanged(IAgent parm1) {
		checkMotion();
		motion.pathPropertiesChanged(parm1);
	}
	public IMapView getMap() {
		checkMotion();
		return motion.getMap();
	}
	public ILayerView getRoadLayer() {
		checkMotion();
		return motion.getRoadLayer();
	}
	public ILayerView getRailwayLayer() {
		checkMotion();
		return motion.getRailwayLayer();
	}
	public ILayerView getSeaLayer() {
		checkMotion();
		return motion.getSeaLayer();
	}
	public ILayerView getAirwayLayer() {
		checkMotion();
		return motion.getAirwayLayer();
	}
	public ILayerView getCustomMotionLayer(String parm1) {
		checkMotion();
		return motion.getCustomMotionLayer(parm1);
	}
	public void embarkedAgent(IAgent parm1, IAgent parm2) {
		checkMotion();
		motion.embarkedAgent(parm1,parm2);
	}
	public void disembarkedAgent(IAgent parm1, IAgent parm2) {
		checkMotion();
		motion.disembarkedAgent(parm1,parm2);
	}
	public int getCoordinateSystem() {
		checkMotion();
		return motion.getCoordinateSystem();
	}
	public double getUnitsPerMeter() {
		checkMotion();
		return motion.getUnitsPerMeter();
	}
	public double getMetersPerPixel() {
		checkMotion();
		return motion.getMetersPerPixel();
	}
	public double getZoom() {
		checkMotion();
		return motion.getZoom();
	}
	public double calculateDistance(double parm1, double parm2, double parm3, double parm4) {
		checkMotion();
		return motion.calculateDistance(parm1,parm2,parm3,parm4);
	}
	public IAgent agentCanEmbarkOn(IAgent parm1, String parm2) {
		checkMotion();
		return motion.agentCanEmbarkOn(parm1,parm2);
	}
	public IAgent getAgent(String parm1) {
		checkMotion();
		return motion.getAgent(parm1);
	}

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
	public MotionReport goForMeters(double meters,Point2D start,double heading,boolean continueAsFar,ILayerView layer,Heading preference,Point2D[] nodePoints,MotionReport motrep) {
		checkMotion();
		return motion.goForMeters(meters,start,heading,continueAsFar,layer,preference,nodePoints,motrep);
	}
}