package gr.cti.eslate.agent;

import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.eslateToolBar.ESlateToolBar;
import gr.cti.eslate.mapModel.geom.Heading;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.protocol.DistanceControlInterface;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.IAgentHost;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.MotionFeatures;
import gr.cti.eslate.protocol.MotionReport;
import gr.cti.eslate.protocol.SteeringControlInterface;
import gr.cti.eslate.protocol.TimeControlInterface;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.typeArray.DblBaseArray;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.thwt.layout.Anchor;
import com.thwt.layout.SmartLayout;

/**
 * Agent class.
 * Overriding methods <em>getFaceSize()</em> and <em>paintFace</em> a descendant
 * class may produce whatever face it wants.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class Agent extends JPanel implements IAgent,ESlatePart,Externalizable,DistanceControlInterface,TimeControlInterface,SteeringControlInterface {

	public Agent() {
		try {
			handle.setUniqueComponentName(bundleMessages.getString("componentname"));
		} catch(Throwable e) {}
		handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.AgentPrimitives");
		//Default icons
		faces=new AgentFace(this,8);
		faces.add(-22.5d,22.5d,loadImageIcon("images/predefined/manN.gif"));
		faces.add(22.5d,67.5d,loadImageIcon("images/predefined/manNW.gif"));
		faces.add(67.5d,112.5d,loadImageIcon("images/predefined/manW.gif"));
		faces.add(112.5d,157.5d,loadImageIcon("images/predefined/manSW.gif"));
		faces.add(157.5d,202.5d,loadImageIcon("images/predefined/manS.gif"));
		faces.add(202.5d,247.5d,loadImageIcon("images/predefined/manSE.gif"));
		faces.add(247.5d,292.5d,loadImageIcon("images/predefined/manE.gif"));
		faces.add(292.5d,337.5d,loadImageIcon("images/predefined/manNE.gif"));
		autoProduce=false;

		nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3);
		latlong=new Point2D.Double(Double.MAX_VALUE,Double.MAX_VALUE);
		lblStatus.setText(bundleMessages.getString("notpositioned"));
		mLayerID=bundleBeanInfo.getString("travelseverywhere");
		travelsOnv=TRAVELS_EVERYWHERE;
		typeID=bundleBeanInfo.getString("man");
		type=TYPE_MAN;
		embarkOnAgents=new String[0];
		embarkedAgents=new ArrayList<IAgent>();
		//Velocity init
		velocity=0;
		potVelocity=2;
		maxVelocity=300000*3600*TO_MS; //Default max velocity the speed of light. To conform to Einstein's law!
		minVelocity=0;
		alwaysVisible=false;
		unitTolerance=0.5d;

		//Initialize static type set
		existingTypes.add(bundleBeanInfo.getString("man"));
		existingTypes.add(bundleBeanInfo.getString("auto"));
		existingTypes.add(bundleBeanInfo.getString("train"));
		existingTypes.add(bundleBeanInfo.getString("ship"));
		existingTypes.add(bundleBeanInfo.getString("plane"));
		existingTypes.add(bundleBeanInfo.getString("all"));

		//General GUI initializations
		setPreferredSize(new Dimension(220,200));
		setBorder(new gr.cti.eslate.utils.NoTopOneLineBevelBorder(javax.swing.border.BevelBorder.RAISED));
		borderChanged=false;
		toolBar.setOrientation(ESlateToolBar.VERTICAL);
		toolBar.setFloatable(false);
		tbnPath=new JToggleTool(loadImageIcon("images/paw.gif"),bundleTooltips.getString("path"),"Help");
		tbnPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create a new segment every time the button is depressed and pressed.
				//If the last segment added has no points, don't add a new one.
				if (tbnPath.isSelected() && (path.getActiveSegment()==null || path.getActiveSegment().hasPoints())) {
					path.addSegment();
					addPoint(latlong.x,latlong.y);
				}
			}
		});
		btnLocate=new JButtonTool(loadImageIcon("images/locate.gif"),bundleTooltips.getString("locate"));
		btnLocate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plugHandler.locateAgent();
			}
		});
		btnInfo=new JButtonTool(loadImageIcon("images/info.gif"),"Tooltip");
		btnInfo.setEnabled(false);
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goDistance(100,false);
			}
		});
		btnInfo.setVisible(false);
		toolBar.add(tbnPath);
		toolBar.add(btnLocate);
		toolBar.add(btnInfo);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//Customize
		scrFace.setViewportView(pnlPhoto);

		//Initialize
		tilt=0;
		path=new Path();
		tbnPath.setSelected(true);
		//Create plugs
		plugHandler=new PlugHandler(this);
		tick=0;//ticks=new TickFIFO();
		stopAtCrossings=false;
		jobs=new JobFIFO(this);
//		jobs.start();
	}
	public void addAgentListener(AgentListener l) {
		listeners.add(l);
	}

	public void removeAgentListener(AgentListener l) {
		listeners.remove(l);
	}

	/**
	 * @return  The name of the agent.
	 */
	public String getName() {
		return handle.getComponentName();
	}

    /**
     * Initializes the last "travelled on object" to the first line the agent is positioned.
     * @param head  The heading.
     */
    public void setLastTravelledOnObject(Heading head) {
		if (plugHandler!=null && listeners!=null && listeners.size()>0 && !((head!=null && lastTravelledOn!=null && ((Heading.Line) head).line==((Heading.Line) lastTravelledOn).line))) {
			GeographicObject go=null;
			if (head!=null)
				go=((Heading.Line) head).line;
			GeographicObject pre=null;
			if (lastTravelledOn!=null)
				pre=((Heading.Line) lastTravelledOn).line;
			listeners.motionObjectChanged(new GeoObjectAgentEvent(this,plugHandler.getMotionLayer(),go,pre));
		}
		if (head==null)
			lastTravelledOn=null;
		else
			lastTravelledOn=(Heading) head.clone();
    }

	/**
	 * Stops the agent and destroys all queued jobs.
	 */
	public void stop() {
        addJob(Job.STOP,null);
	}

	/**
	 * Starts motion with the currently set properties of velocity and heading.
	 */
	public void go() {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		addJob(Job.GO,null);
	}

	/**
	 * Makes the agent "sleep" (wait) for the given amount of days.
	 * @param   days    The amount of time to sleep.
	 */
	public void sleepDays(int days) {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		//The first parameter is the seconds to sleep and the second, the seconds slept.
		addJob(Job.SLEEP,new double[] {days*86400,0});
	}

	/**
	 * Makes the agent "sleep" (wait) for the given amount of seconds.
	 * @param   sec The amount of time to sleep.
	 */
	public void sleep(long sec) {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		//The first parameter is the seconds to sleep and the second, the seconds slept.
		addJob(Job.SLEEP,new double[] {sec,0});
	}

	/**
	 * Wakes the agent up if it sleeps.
	 */
	public void wake() {
		addJob(Job.WAKE,null);
	}

	/**
	 * If the agent sleeps, this method gets the sleeping (waiting) time left.
	 * @return  The waiting time left in seconds.
	 */
	public double getSleepingTimeLeft() {
		if (jobs!=null && jobs.size()>0 && jobs.at(0).type==Job.SLEEP)
			return jobs.at(0).param[0]-jobs.at(0).param[1];
		return 0;
	}

	/**
	 * DistanceControlInterface method. Starts motion.
	 * @param	dist	The distance to travel in meters.
	 * @param	stopAtLandmarks	Indicate whether the component should stop
	 *				at "interesting" locations even though the
	 *				specified distance has not been travelled.
	 */
	public void goDistance(double dist,boolean stopAtLandmarks) {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		if (velocity==0) {
			velocity=potVelocity;
			plugHandler.velocityChanged();
		}
		addJob(Job.MOVE,new double[]{dist});
	}
	/**
	 * TimeControlInterface method. Starts motion.
	 * @param	sec	    The seconds to travel.
	 * @param	stopAtLandMarks	Indicate whether the component should stop
	 *				at "interesting" locations even though the
	 *				specified distance has not been travelled.
	 */
	public void goTime(double sec,boolean stopAtLandMarks) {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		if (velocity==0) {
			velocity=potVelocity;
			plugHandler.velocityChanged();
		}
		goDistance(velocity*sec,false);
	}
	/**
	 * Tells the agent to go to the given lat-long position, walking through all the intermediate points.
	 */
	public void goTo(double longt,double lat) {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		if (velocity==0) {
			velocity=potVelocity;
			plugHandler.velocityChanged();
		}
		addJob(Job.GOTO,new double[]{longt,lat});
	}
	/**
	 * Tells the agent to go to the given lat-long position, walking through all the intermediate points.
	 * The host parameter identifies which host wants to move the agent. If the simple <code>goTo</code>
	 * function is used, the agent will move on any of the hosts, probably producing an unwanted effect,
	 * e.g. when the agent travels on two maps, on one inside a polygon and on the other on a line,
	 * the user clicking on the map with the lines may see the agent travelling in the polygon.
	 */
	public void goTo(IAgentHost host,double longt,double lat) {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		if (velocity==0) {
			velocity=potVelocity;
			plugHandler.velocityChanged();
		}
		addJob(Job.GOTO_ONHOST,new double[]{longt,lat},host);
	}
	/**
	 * Every command to the agent becomes a job added to its queue. These commands are executed
	 * in a JobFIFO order.
	 */
	private void addJob(int type,double[] param) {
	    jobs.put(new Job(type,param));
	}
	/**
	 * Every command to the agent becomes a job added to its queue. These commands are executed
	 * in a JobFIFO order. This command is added by a specific host.
	 */
	private void addJob(int type,double[] param,IAgentHost host) {
		jobs.put(new Job(type,param,host));
	}
	/**
	 * Adds a tick to the stack. The ticks are consumed in a fifo order. Each job splits itself
	 * every time a new tick arrives until it is done.
	 */
	void tickArrived(double t) {
//System.out.println("Agent "+getName()+" "+getVelocity()+" "+getPotentialVelocity());
//if (jobs.size()>0) {
//for (int i=0;i<jobs.size();i++)
//	System.out.print(jobs.at(i)+" ");
//System.out.println();
//}
//if (getName().equals("����������"))
//System.out.println("Tick "+t+" "+(tick+t));
		//No job, no tick!
		if (jobs.size()==0) {
			if (plugHandler.isVectorVelocityGiven() && potVelocity>0)
				goDistance(t*potVelocity,false);
 	  		else
				return;
		}
		if (jobs.size()>0) {
			tick+=t;
			jobs.run();
		}
	}
	/**
	 * Declares if the agent is connected to a clock.
	 */
	boolean isTimeAware() {
		return plugHandler.isTimeAware();
	}
	/**
	 * Sets the latitude and longitude of the agent. The agent may refuse to accept the change.
	 */
	public void setLongLat(double longt,double lat) throws gr.cti.eslate.agent.AgentRefusesToChangePositionException {
		if (isEmbarked()) {
			showDialogIsEmbarked();
			return;
		}
		if (!plugHandler.askValidation(longt,lat))
			throw new AgentRefusesToChangePositionException("Location "+longt+","+lat+" is invalid for agent "+getName());
		addJob(Job.JUMP,new double[]{longt,lat});
	}
	/**
	 * Internal method. The difference is that it controls when the new point must be added to the path
	 * and whether this is the final of a sequence of steps, so the host may do special operations.
	 */
	@SuppressWarnings("unused")
	private void setLongLat(double longt,double lat,boolean addPointToPath,boolean movingOnEarth,boolean repaint) throws gr.cti.eslate.agent.AgentRefusesToChangePositionException {
		//If there is no change in position quit
		if (latlong.x==longt && latlong.y==lat)
			return;

		//If the agent has a previous location adjust the tilt angle properly
		if (movingOnEarth) {
            if (true==false)
	            throw new AgentRefusesToChangePositionException("This is to comply with the throws signature!");
		} else {
			if (latlong.x!=Double.MAX_VALUE && latlong.y!=Double.MAX_VALUE) {
				double x=longt-latlong.x;
				double y=lat-latlong.y;
				if (x!=0)
					tilt=Math.atan(y/x)*TO_DEG;
				if (x==0) {
					if (y<0)
						tilt=180;
					else
						tilt=0;
				} else if (x>0)
					tilt=270+tilt;
				else if (x<0)
					tilt=90+tilt;
				tilt=Helpers.normAngle(tilt);
			}
		}

		latlong.x=longt;
		latlong.y=lat;
		lblStatus.setText(nf.format(longt)+" , "+nf.format(lat));
		plugHandler.locationChanged(true);
		//Add the point to the path
		if (addPointToPath && tbnPath.isSelected())
			addPoint(longt,lat);
	}

	/**
	 * @return  <em>True</em>, if the agent's location has been initialized.
	 */
	public boolean isPositioned() {
		if (latlong.x!=Double.MAX_VALUE && latlong.y!=Double.MAX_VALUE)
			return true;
		return false;
	}
	/**
	 * @return The agent latitude and longitude or null if the agent is not positioned.
	 */
	public Point2D.Double getLongLat() {
		return latlong;
	}
	/**
	 * @return  The agent latitude. If not defined the method returns Double.MAX_VALUE.
	 */
	public double getLatitude() {
		if (latlong!=null)
			return latlong.y;
		else
			return Double.MAX_VALUE;
	}
	/**
	 * @return  The agent longitude. If not defined the method returns Double.MAX_VALUE.
	 */
	public double getLongitude() {
		if (latlong!=null)
			return latlong.x;
		else
			return Double.MAX_VALUE;
	}

	/**
	 * Fires an event.
	 */
	protected void fireGeographicObjectTouchedEvent(ILayerView layer,GeographicObject go) {
		if (listeners.size()>0)
			listeners.geographicObjectTouched(new GeoObjectAgentEvent(this,layer,go));
	}

	/**
	 * Informs the agent that it has met with another agent. This method is
	 * usually called by an agent host.
	 */
	public void agentMetWithAgent(IAgent metAgent) {
		if (listeners.size()>0)
			listeners.agentMeeting(new AgentMeetingEvent(this,(Agent)metAgent));
	}

	/**
	 * Foo property method to provide a button on the property sheet which
	 * positions the agent for the first time.
	 */
	public void setFooInitLoc(Agent a) {
	}
	/**
	 * Foo property method to provide a button on the property sheet which
	 * positions the agent for the first time.
	 */
	public Agent getFooInitLoc() {
		return this;
	}
	/**
	 * @return  The agent tilt from upright. The upright is usually the North. The returned angle is in [0,360) in degrees anti-clockwise.
	 */
	public double getTiltFromUpright() {
		return tilt;
	}
	/**
	 * Sets the agent tilt from upright. The upright is usually the North.
	 */
	public void setTiltFromUpright(double t) {
		addJob(Job.LOOK,new double[]{t});
	}
	/**
	 * Turns the agent <code>t</code> degrees. If <code>t</code> is positive the agent turns anti-clockwise.
	 * If <code>t</code> is negative the agen turns clockwise.
	 */
	public void turn(double t) {
		addJob(Job.TURN,new double[]{t});
	}
	/**
	 * @return  One of the <code>TRAVELS_xxx</code> constants, identifying where the agent wants to walk on.
	 */
	public int travelsOn() {
		return travelsOnv;
	}
	/**
	 * Sets the id of the motion layer.
	 */
	public void setTravellingOnMotionLayerID(String s) {
		mLayerID=s;
		if (AgentBeanInfo.bundle.getString("travelseverywhere").equalsIgnoreCase(s))
			travelsOnv=IAgent.TRAVELS_EVERYWHERE;
		else if (AgentBeanInfo.bundle.getString("travelsonroads").equalsIgnoreCase(s))
			travelsOnv=IAgent.TRAVELS_ON_ROADS;
		else if (AgentBeanInfo.bundle.getString("travelsonrailways").equalsIgnoreCase(s))
			travelsOnv=IAgent.TRAVELS_ON_RAILWAYS;
		else if (AgentBeanInfo.bundle.getString("travelsonsea").equalsIgnoreCase(s))
			travelsOnv=IAgent.TRAVELS_ON_SEA;
		else if (AgentBeanInfo.bundle.getString("travelsonair").equalsIgnoreCase(s))
			travelsOnv=IAgent.TRAVELS_ON_AIR;
		else
			travelsOnv=IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER;
	}
	/**
	 * @return  True, if the agent can embark on agent <code>agent</code>.
	 */
	public boolean canEmbarkOn(IAgent agent) {
		String everyString=AgentBeanInfo.bundle.getString("all");
		for (int i=0;i<embarkOnAgents.length;i++)
			if (embarkOnAgents[i].equalsIgnoreCase(agent.getType()) || embarkOnAgents[i].equalsIgnoreCase(everyString))
				return true;
		return false;
	}
	/**
	 * @return  If the agent can embark on the agent named <code>agentName</code>, the function returns
	 *          a handle to the agent. Otherwise it returns <code>null</code>.
	 */
	public IAgent canEmbarkOn(String agentName) {
		return plugHandler.canEmbarkOnAgent(agentName);
	}
	/**
	 * Tells the agent to disembark agent <code>agent</code>.
	 * @return  True, if the agent was embarked and disembarked successfuly.
	 */
	public boolean disembark(IAgent agent) {
		//No agent to disembark
		boolean found=false;
		for (int i=0;i<embarkedAgents.size() && !found;i++)
			if (embarkedAgents.get(i).equals(agent))
				found=true;
		if (!found)
			return false;
		agent.setCarryingAgent(null);
		//Add a new segment to start ploting from this point on
		if (path.getActiveSegment().hasPoints()) {
			agent.getPath().addSegment();
			agent.getPath().addPoint(agent.getLongitude(),agent.getLatitude());
		}
		embarkedAgents.remove(agent);
		plugHandler.disembark(agent);
		return true;
	}
	/**
	 * Tells the agent to disembark agent named <code>agentName</code>.
	 * @return  True, if the agent was embarked and disembarked successfuly.
	 */
	public boolean disembark(String agentName) {
		for (int i=0;i<embarkedAgents.size() && agentName!=null;i++)
			if (agentName.equalsIgnoreCase(((IAgent) embarkedAgents.get(i)).getName()))
				return disembark((IAgent) embarkedAgents.get(i));
		return false;
	}
	/**
	 * Tells the agent to ask all its hosts and reply if this is a valid location.
	 */
	public boolean isValidLocation(double longt,double lat) {
		return (!isEmbarked() || plugHandler.askValidation(longt,lat));
	}
	/**
	 * Tells the agent to embark agent <code>agent</code>.
	 */
	public void embark(IAgent agent) {
		agent.setCarryingAgent(this);
		embarkedAgents.add(agent);
		plugHandler.embark(agent);
	}
	/**
	 * <code>True</code>, if the agent is embarked in another agent.
	 */
	public boolean isEmbarked() {
		return isEmbarkedOn!=null;
	}
	/**
	 * <code>True</code>, if the agent has other agents embarked on it.
	 */
	public boolean hasAgentsEmbarked() {
		return (embarkedAgents.size()!=0);
	}
	/**
	 * An Iterator of the embarked agents, if any.
	 */
	public Iterator getEmbarkedAgents() {
		return new Iterator() {
			int pos=0;
			public boolean hasNext() {
				return pos<embarkedAgents.size();
			}
			public Object next() {
				return embarkedAgents.get(pos++);
			}
			public void remove() {}
		};
	}
	/**
	 * How many embarked agents, if any.
	 */
	public int getEmbarkedAgentsCount() {
		return embarkedAgents.size();
	}
	/**
	 * @return  The agent that carries this agent.
	 */
	public IAgent getCarryingAgent() {
		return isEmbarkedOn;
	}
	/**
	 * Sets the agent that carries this agent.
	 */
	public void setCarryingAgent(IAgent agent) {
		isEmbarkedOn=agent;
	}
	/**
	 * Called by the carrier agent on the embarked agent.
	 * @param   longt   The new longitude position.
	 * @param   lat     The new latitude position.
	 */
	public void carrierAgentMoved(double longt,double lat) {
		latlong.x=longt;
		latlong.y=lat;
		lblStatus.setText(nf.format(longt)+" , "+nf.format(lat));
		plugHandler.locationChanged(true);
	}
	/**
	 * @return  One of the <code>TYPE_xxx</code> constants, identifying what the agent is.
	 */
	public int type() {
		return type;
	}
	/**
	 * Gets the id of the agent type.
	 */
	public String getType() {
		return typeID;
	}
	/**
	 * Sets the id of the agent type.
	 */
	public void setType(String s) {
		typeID=s;
		if (AgentBeanInfo.bundle.getString("man").equalsIgnoreCase(s))
			type=IAgent.TRAVELS_EVERYWHERE;
		else if (AgentBeanInfo.bundle.getString("auto").equalsIgnoreCase(s))
			type=IAgent.TYPE_AUTO;
		else if (AgentBeanInfo.bundle.getString("train").equalsIgnoreCase(s))
			type=IAgent.TYPE_TRAIN;
		else if (AgentBeanInfo.bundle.getString("ship").equalsIgnoreCase(s))
			type=IAgent.TYPE_TRAIN;
		else if (AgentBeanInfo.bundle.getString("plane").equalsIgnoreCase(s))
			type=IAgent.TYPE_AIRPLANE;
		else
			type=IAgent.TYPE_OTHER;
		existingTypes.add(s);
	}
	/**
	 * @return  If <code>travelsOn()</code> returns <code>TRAVELS_ON_CUSTOM_MOTION_LAYER</code>, this method returns the id of the needed motion layer.
	 */
	public String getTravellingOnMotionLayerID() {
		return mLayerID;
	}
	/**
	 * @return  The agent path.
	 */
	public Path getPath() {
		return path;
	}
	/**
	 * Foo method to display a button to define the phase images of the agent in the property sheet.
	 */
	public void setPhaseImages(Agent a) {
	}
	/**
	 * Foo method to display a button to define the phase images of the agent in the property sheet.
	 */
	public Agent getPhaseImages() {
		return this;
	}
	/**
	 * Foo method to display a button to define where the agent can embark in the property sheet.
	 */
	public void setEmbarksOn(Agent a) {
	}
	/**
	 * Foo method to display a button to define where the agent can embark in the property sheet.
	 */
	public Agent getEmbarksOn() {
		return this;
	}
	/**
	 * Sets the types of agents where this agent can embark.
	 */
	void setEmbarkOnAgents(String[] s) {
		embarkOnAgents=s;
	}
	/**
	 * @return  The types of agents where this agent can embark.
	 */
	String[] getEmbarkOnAgents() {
		return embarkOnAgents;
	}
	/**
	 * Calls repaint on all its hosts.
	 */
	public void callRepaintOnHosts() {
		plugHandler.callRepaintOnHosts();
	}
	/**
	 * Sets the face of the agent. (i.e. the image shown on the component)
	 */
	public void setComponentFace(Icon ic) {
		if (ic!=null) {
			BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
			ic.paintIcon(this,bi.getGraphics(),0,0);
			pnlPhoto.setPhoto(new NewRestorableImageIcon(bi));
		} else
			pnlPhoto.setPhoto(null);
	}
	/**
	 * Sets the face of the agent. (i.e. the image shown on the component)
	 */
	public Icon getComponentFace() {
		return pnlPhoto.getPhoto();
	}
	/**
	 * Clears all the agent phases.
	 */
	protected void clearPhases() {
		faces.clearAll();
		if (animatedFaces!=null)
			animatedFaces.clearAll();
	}
	/**
	 * Clears all the agent phases and changes their number to <em>phases</em>.
	 */
	public void clearPhases(int phases) {
		faces=new AgentFace(this,phases);
		if (animatedFaces!=null)
			animatedFaces=new AgentFaceAnimated(this,phases,4);
	}
	/**
	 * Changes the icon for this phase if it exists or adds the new phase.
	 */
	public void setPhaseIcon(double from,double to,Icon ic) {
		faces.set(from,to,ic);
	}
	/**
	 * Changes the icon for this phase if it exists or adds the new phase.
	 */
	public void setAnimatedPhaseIcon(double from,double to,Icon[] ic) {
		if (animatedFaces==null)
			animatedFaces=new AgentFaceAnimated(this,getNumberOfPhases(),4);
		animatedFaces.set(from,to,ic);
	}
	/**
	 * Sets the number of discernible phases the agent has according to its tilt angle.
	 */
	public void setNumberOfPhases(int np) {
		faces.setCapacity(np);
		if (animatedFaces!=null)
			animatedFaces.setCapacity(np);
	}
	/**
	 * @return The number of discernible phases the agent has according to its tilt angle.
	 */
	public int getNumberOfPhases() {
		return faces.getCapacity();
	}
	/**
	 * Automatically produces faces for the agent using the given icon as the icon
	 * for north direction.
	 * @param ic the icon to set.
	 * @param phases the number of phases to set. Each phase has a different face for the agent.
	 */
	public void automaticallyProducePhases(Icon ic,int phases) {
		clearPhases();
		setNumberOfPhases(phases);
		setAutomaticallyProducePhases(true);
		Icon[] icons=Helpers.producePhases(this,ic,phases);
		if (phases==1)
		    //0 and 360 are the same normalized angle. Trick!
		    setPhaseIcon(0,360-1E-6,icons[0]);
		else {
		    double angleStep=360d/phases;
		    double startAngle=-(angleStep/2);
		    for (int i=0;i<icons.length;i++) {
		        setPhaseIcon(startAngle,startAngle+angleStep,icons[i]);
		        startAngle+=angleStep;
		    }
		}

	}
	/**
	 * Flag showing if the agent produces its phases automatically.
	 */
	protected void setAutomaticallyProducePhases(boolean auto) {
		autoProduce=auto;
	}
	/**
	 * Flag showing if the agent produces its phases automatically.
	 */
	protected boolean getAutomaticallyProducePhases() {
		return autoProduce;
	}
	/**
	 * Overriding this method as well as <em>paintFace</em>, a subclass
	 * may produce whatever face it wants.
	 * @return  The size of the rectangle in pixels needed by the agent to paint its face in the current tilt.
	 */
	public Dimension getFaceSize() {
		return getFaceSize(tilt);
	}
	/**
	 * Overriding this method as well as <em>paintFace</em>, a subclass
	 * may produce whatever face it wants.
	 * @param angle In degrees.
	 * @return  The size of the rectangle in pixels needed by the agent to paint its face in the current tilt.
	 */
	public Dimension getFaceSize(double angle) {
		Icon ic;
		if (isAnimated() && getVelocity()>0 && animatedFaces!=null)
			ic=animatedFaces.get(Helpers.normAngle(angle),0);
		else
			ic=faces.get(Helpers.normAngle(angle));
		if (ic==null) {
			dimCache.width=DEFAULT_SIZE;
			dimCache.height=DEFAULT_SIZE;
		} else {
			dimCache.width=ic.getIconWidth();
			dimCache.height=ic.getIconHeight();
		}
		return dimCache;
	}
	/**
	 * Paints the current face (according to current tilt) of the agent in the given Graphics context.
	 * The agent will draw itself from (0,0).
	 * <p>
	 * Overriding this method as well as <em>getFaceSize</em>, a subclass
	 * may produce whatever face it wants.
	 * @param g The graphics to paint on.
	 * @param angle In degrees.
	 */
	public void paintFace(Graphics g,double angle) {
		paintFace(g,angle,0);
	}

	/**
	 * Paints the current face (according to the given angle in degrees) of the agent in the given Graphics context.
	 * The agent will draw itself from (0,0) and use the given frame, if animated.
	 */
	public void paintFace(Graphics g,double angle,int frame) {
		Icon ic;
		if (isAnimated() && getVelocity()>0 && animatedFaces!=null)
			ic=animatedFaces.get(Helpers.normAngle(angle),frame);
		else
			ic=faces.get(Helpers.normAngle(angle));
		if (ic!=null)
			ic.paintIcon(this,g,0,0);
	}

	/**
	 * Paints the current face (according to the given angle in degrees) of the agent in the given Graphics context.
	 * The agent will draw itself from (0,0).
	 * <p>
	 * Overriding this method as well as <em>getFaceSize</em>, a subclass
	 * may produce whatever face it wants.
	 */
	public void paintFace(Graphics g) {
		paintFace(g,tilt);
	}
	/**
	 * @return  The measure of the velocity the agent will have when it will start moving. (In km/h)
	 */
	public double getPotentialVelocity() {
		return potVelocity*TO_KMH;
	}
	/**
	 * Sets the measure of the velocity the agent will have when it will start moving. (In km/h)
	 */
	public void setPotentialVelocity(double d) {
		d=d*TO_MS;
		potVelocity=d;
		if (d>maxVelocity)
			potVelocity=maxVelocity;
		else if (d<minVelocity)
			potVelocity=minVelocity;
		//If the agent is moving set the velocity also
		if (velocity>0) {
			velocity=potVelocity;
			plugHandler.velocityChanged();
		}
	}
	/**
	 * @return  The measure of the velocity the agent has in the moment of the method call. (In km/h)
	 */
	public double getVelocity() {
		return velocity*TO_KMH;
	}
	/**
	 * Sets the measure of the velocity the agent has in the moment of the method call. (In km/h)
	 */
	public void setVelocity(double d) {
		d=d*TO_MS;
		velocity=d;
		if (d>maxVelocity)
			velocity=maxVelocity;
		else if (d<minVelocity)
			velocity=minVelocity;
		plugHandler.velocityChanged();
	}
	/**
	 * @return  The maximum value of the velocity. (In km/h)
	 */
	public double getMaxVelocity() {
		return maxVelocity*TO_KMH;
	}
	/**
	 * Sets the maximum value of the velocity. (In km/h)
	 */
	public void setMaxVelocity(double d) {
		d=d*TO_MS;
		if (d<0)
			throw new IllegalArgumentException("Cannot have a negative velocity!");
		if (minVelocity>d)
			throw new IllegalArgumentException("The minimum velocity is greater than the maximum!");
		maxVelocity=d;
		//Make sure the values are in the bounds
		if (velocity>d)
			setVelocity(velocity*TO_KMH);
		else
			if (potVelocity>d)
				potVelocity=d;
	}
	/**
	 * @return  The minimum value of the velocity. (In km/h)
	 */
	public double getMinVelocity() {
		return minVelocity*TO_KMH;
	}
	/**
	 * Sets the minimum value of the velocity. (In km/h)
	 */
	public void setMinVelocity(double d) {
		d=d*TO_MS;
		if (d<0)
			throw new IllegalArgumentException("Cannot have a negative velocity!");
		if (d>maxVelocity)
			throw new IllegalArgumentException("The maximum velocity is smaller than the minimum!");
		minVelocity=d;
		//Make sure the values are in the bounds
		if (velocity<d)
			setVelocity(velocity*TO_KMH);
		else
			if (potVelocity<d)
				potVelocity=d;
	}
	/**
	 * Tells the agent to leave a trail or not.
	 */
	public void setTrailOn(boolean trail) {
		tbnPath.setSelected(trail);
	}
	/**
	 * Asks the agent if it leaves a trail or not.
	 */
	public boolean isTrailOn() {
		return tbnPath.isSelected();
	}
	/**
	 * Clears the trail.
	 */
	public void clearTrail() {
		path=new Path();
		plugHandler.repaintTrail();
	}
	/**
	 * Clears any remaining ticks.
	 */
	public void clearTick() {
		synchronized (jobs) {
			tick=0;
		}
	}
	/**
	 * Clears any remaining jobs.
	 */
	public void clearJobs() {
		synchronized (jobs) {
			jobs.clear();
		}
	}
	/**
	 * Clears location and trail. After calling this method, the agent is unpositioned, in the initial state.
	 * It disconnects from all the hosts.
	 */
	public void forgetEverything() {
		path=new Path();
		tilt=0;
		latlong=new Point2D.Double(Double.MAX_VALUE,Double.MAX_VALUE);
		lblStatus.setText(bundleMessages.getString("notpositioned"));
		plugHandler.disconnectAgent();
	}
	/**
	 * Loads an image from the jar file.
	 */
	protected ImageIcon loadImageIcon(String filename) {
		try {
			return new ImageIcon(Agent.class.getResource(filename));
		} catch(Exception e) {
			System.out.println("Error loading Image Icon '"+filename+"'");
			try {
				return new ImageIcon(Agent.class.getResource("images/notfound.gif"));
			} catch(Exception e1) {
			}
		}
		return null;
	}
	/**
	 * @return The E-Slate handle.
	 */
	public ESlateHandle getESlateHandle() {
		return handle;
	}

	private void showDialogIsEmbarked() {
		JOptionPane.showMessageDialog(SwingUtilities.getAncestorOfClass(Frame.class,this),bundleMessages.getString("isEmbarked1")+getName()+bundleMessages.getString("isEmbarked2")+isEmbarkedOn.getName()+bundleMessages.getString("isEmbarked3")+isEmbarkedOn.getName()+bundleMessages.getString("isEmbarked4")+getName()+bundleMessages.getString("isEmbarked5"),"",JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * Known method.
	 */
	public void setBorder(Border b) {
		borderChanged=true;
		super.setBorder(b);
	}
	/**
	 * Controls statusbar visibility.
	 */
	public void setStatusbarVisible(boolean b) {
		lblStatus.setVisible(b);
		if (b) {
			this.add(lblStatus, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
			this.add(scrFace, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 2),
					new com.thwt.layout.EdgeAnchor(toolBar, Anchor.Right, Anchor.Right, Anchor.Left, 1),
					new com.thwt.layout.EdgeAnchor(lblStatus, Anchor.Top, Anchor.Above, Anchor.Bottom, 1)));
		} else {
			remove(lblStatus);
			this.add(scrFace, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 2),
					new com.thwt.layout.EdgeAnchor(toolBar, Anchor.Right, Anchor.Right, Anchor.Left, 1),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 4)));
		}
		revalidate();
		repaint();
	}
	/**
	 * Checks statusbar visibility.
	 */
	public boolean isStatusbarVisible() {
		return lblStatus.isVisible();
	}
	/**
	 * This property indicates when this agent wants to be always visible. This means that a component
	 * hosting this agent should (but is not forced to) make it visible wherever it is. E.g. if the
	 * agent is hosted in a map, the map should scroll if the agent goes out of the visible map area.
	 */
	public void setAlwaysVisible(boolean b) {
		alwaysVisible=b;
	}
	/**
	 * This property indicates when this agent wants to be always visible. This means that a component
	 * hosting this agent should (but is not forced to) make it visible wherever it is. E.g. if the
	 * agent is hosted in a map, the map should scroll if the agent goes out of the visible map area.
	 */
	public boolean isAlwaysVisible() {
		return alwaysVisible;
	}
	/**
	 * Adds a point to the active segment.
	 */
	private void addPoint(double x,double y) {
		if (path.getActiveSegment()!=null) {
			//Add a segment when crossing the 180 longitude
			PathSegment getactive=path.getActiveSegment();
			if ((getactive.lastx<=180 && getactive.lastx>=0 && x>=-180 && x<0 && tilt>=180) ||
				(getactive.lastx>=-180 && getactive.lastx<0 && x<=180 && x>0 && tilt<=180))
				path.addSegment();
			path.getActiveSegment().addPoint(x,y);
		} else {
			path.addSegment();
			path.addPoint(x,y);
		}
	}

	/**
	 * Gets the unit tolerance (in host coordinates), the square around
	 * the location of the agent which forms the "hotspot" that senses the surroundings.
	 * @return  The tolerance in host units.
	 */
	public double getUnitTolerance() {
		return unitTolerance;
	}

	/**
	 * Sets the unit tolerance (in host coordinates), the square around
	 * the location of the agent which forms the "hotspot" that senses the surroundings.
     * @param unitTolerance    The tolerance in host units.
     */
	public void setUnitTolerance(double unitTolerance) {
		if (unitTolerance<=0)
			throw new IllegalArgumentException("Unit tolerance must me a positive, non-zero number.");
		this.unitTolerance=unitTolerance;
	}

	/**
	 * Property that tells the agent to stop at road crossings.
	 */
	public void setStopAtCrossings(boolean stop) {
		stopAtCrossings=stop;
	}

	/**
	 * Property that tells the agent to stop at road crossings.
	 */
	public boolean getStopAtCrossings() {
		return stopAtCrossings;
	}
	/**
	 * Property that tells if the agent uses an animated icon when walking.
	 * @return  Animated when true.
	 */
	public boolean isAnimated() {
		return true;
	}
	/**
	 * Moves the agent everywhere or in a polygon.
	 * @return  The distance travelled. It can be different than that given when the agent
	 * refuses to continue motion.
	 */
	private double moveAlgorithm(double dist,MotionFeatures mf,boolean checkValidityOnEachStep) {
		boolean broken=false;
		if (mf.isMovingOnEarth) {
			int steps=(int) (dist*mf.upm/mf.upp); //In pixels
			double SIJ=dist/steps;
			ReverseGeodesy rg=reverseGeodesy(latlong.x*TO_RAD,latlong.y*TO_RAD,(360-tilt)*TO_RAD,dist);
			double finishX=rg.longt;
			double finishY=rg.lat;
			double finishAz=rg.azimuth;

			for (int i=0;i<steps-1;i++) {
				ReverseGeodesy rg2=reverseGeodesy(latlong.x*TO_RAD,latlong.y*TO_RAD,(360-tilt)*TO_RAD,SIJ);
				double XLONJ=rg2.longt;
				double PHIJ=rg2.lat;
				double AZ=rg2.azimuth;
				try {
					if (XLONJ>Math.PI || XLONJ<=-Math.PI || PHIJ>Math.PI/2 || PHIJ<=-Math.PI/2) {
						path.addSegment();
						addPoint(latlong.x,latlong.y);
					}
					double nox=Helpers.normAngleMinusPiPi(XLONJ*TO_DEG);
					double noy=Helpers.normAngleMinusPiPi(PHIJ*TO_DEG);
					if (checkValidityOnEachStep) {
						if (plugHandler.askValidation(nox,noy)) {
							//Final step is true because otherwise it would be difficult for
							//the host to find where the agent moved to do the repaint.
							setLongLat(nox,noy,true,true,true);
							lookImpl(360-AZ*TO_DEG);
						} else {
							lookImpl(360-AZ*TO_DEG);
							broken=true;
							break;
						}
					} else {
						setLongLat(nox,noy,true,true,true);
						lookImpl(360-AZ*TO_DEG);
					}
				} catch(AgentRefusesToChangePositionException e) {
					System.err.println("AGENT#200006211516: Cannot move agent.");
				}
			}
			//Make the final step, which ensures the correct final location and
			//makes the host repaint the path.
			if (!broken)
				try {
					double nox=Helpers.normAngleMinusPiPi(finishX*TO_DEG);
					double noy=Helpers.normAngleMinusPiPi(finishY*TO_DEG);
					if (checkValidityOnEachStep) {
						if (plugHandler.askValidation(nox,noy)) {
							setLongLat(nox,noy,true,true,true);
							lookImpl(360-finishAz*TO_DEG);
						} else
							broken=true;
					} else {
						setLongLat(nox,noy,true,true,true);
						lookImpl(360-finishAz*TO_DEG);
					}
				} catch(AgentRefusesToChangePositionException e) {}
			//If broken, polygon edges reached.
		} else {
			double onX=Math.cos((tilt+90)*TO_RAD)*dist*mf.upm;
			double onY=Math.sin((tilt+90)*TO_RAD)*dist*mf.upm;
			double finishX=latlong.x+onX;
			double finishY=latlong.y+onY;
			int steps=(int) ((dist*mf.upm)/mf.upp); //In pixels
			double stepX=onX/steps;
			double stepY=onY/steps;
			for (int i=0;i<steps-1;i++)
				try {
					if (checkValidityOnEachStep) {
						if (plugHandler.askValidation(latlong.x+stepX,latlong.y+stepY)) {
							setLongLat(latlong.x+stepX,latlong.y+stepY,false,false,true);
						} else {
							broken=true;
							break;
						}
					} else
						setLongLat(latlong.x+stepX,latlong.y+stepY,false,false,true);
				} catch(AgentRefusesToChangePositionException e) {
					break;
				}
			//Make the final step, which ensures the correct final location and
			//makes the host repaint the path.
			if (!broken)
				try {
					if (checkValidityOnEachStep) {
						if (plugHandler.askValidation(finishX,finishY)) {
							setLongLat(finishX,finishY,false,false,true);
						} else
							broken=true;
					} else
						setLongLat(finishX,finishY,false,false,true);
				} catch(AgentRefusesToChangePositionException e) {}
		}
		//Return the distance travelled
		if (broken) {
			if (mf.isMovingOnEarth)
				return -forwardGeodesy(mf.startLongt*TO_RAD,mf.startLat*TO_RAD,latlong.x*TO_RAD,latlong.y*TO_RAD).s;
			else
				return -pythagorian(mf.startLongt-latlong.x,mf.startLat-latlong.y)/mf.upm;
		} else
			return dist;
	}

	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean jumpImpl(double longt,double lat) {
		if (!plugHandler.askValidation(longt,lat)) {
			Toolkit.getDefaultToolkit().beep();
			return true;
		}
		try {
			setLongLat(longt,lat,true,false,true);
		} catch(AgentRefusesToChangePositionException e) {}
		return true;
	}

	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean stopImpl() {
		setVelocity(0);
		if (lastTravelledOn!=null)
			lastTravelledOn.invalid=true;
		return true;
	}
	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean sleepImpl(Job job) {
		if (plugHandler.isTimeAware()) {
			setVelocity(0);
			job.param[1]+=tick;
			if (job.param[0]-job.param[1]>0) {
				tick=0;
				return false;
			} else {
				//Remaining tick
				tick=job.param[1]-job.param[0];
				return true;
			}
		}
		return true;
	}
	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean goImpl(Job job) {
		if (plugHandler.isTimeAware()) {
			if (velocity==0) {
				//Ignore the request if the agent cannot move.
				if (potVelocity==0)
					return true;

				setVelocity(potVelocity*TO_KMH);
			}
			Point2D[] addToPath=null;
			boolean doMore=true;
			double toTravel=velocity*tick;
			double totTravelled=0;
			double maxStep=100;
			while (toTravel>0 && doMore) {
				double oldVelocity=velocity;
				//Inform the listeners that a step will be taken
				if (listeners!=null) {
					AgentEvent e=new AgentEvent(this);
					listeners.agentToMakeStep(e);
				}
				//The velocity may change during the step
				double stepVelocity=velocity;
				if (stepVelocity==0)
					return true;
				if (stepVelocity!=oldVelocity)
					toTravel=stepVelocity*tick;
                boolean useMaxStep=toTravel>maxStep;
				//Add a max step for ALTG
				MotionReport mr=plugHandler.goForMeters(Math.min(toTravel,maxStep),latlong,getTiltFromUpright(),!getStopAtCrossings(),lastTravelledOn,addToPath);
				synchronized (mr) {
					double travelled=mr.distance;
					totTravelled+=travelled;
					if (travelled!=0) {
						//Add points to the path
						if (addToPath!=null && tbnPath.isSelected())
							for (int i=0;i<addToPath.length;i++)
								addPoint(addToPath[i].getX(),addToPath[i].getY());
						Heading.Line last=(Heading.Line) lastTravelledOn;
						double oldPotVel=potVelocity;
						setLastTravelledOnObject(mr.heading);
						if (potVelocity!=0)
							//Inform for the change in location.
							try {
								setLongLat(mr.longt,mr.lat,false,false,!useMaxStep);
								//A wait action stops asynchronously the go job
								if (jobs.size()>1 && jobs.at(1).type==Job.STOP) {
									tick=Math.max((toTravel-travelled)/stepVelocity,0);
									return true;
								}
								//The remaing tick, if any.
								tick=Math.max((toTravel-travelled)/stepVelocity,0);
								if (tick<1E-3)
									tick=0;
								toTravel=velocity*tick;
								if (toTravel<=0)
									doMore=false;
							} catch (AgentRefusesToChangePositionException e) {
								e.printStackTrace();
								tick=Math.max(toTravel/stepVelocity,0);
								toTravel=0;
							}
						else {
                            setLastTravelledOnObject(last);
							setPotentialVelocity(oldPotVel);
							return true;
						}
					} else {
						//To ensure that the last step is painted
						plugHandler.callRepaintOnHosts();
						doMore=false;
					}
				}
			}
			//A go job is never ending, until a stop is called.
			//It only stops if it cannot move any more (totTravelled=0).
			return (totTravelled<=0);
		}
		return true;
	}

	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean moveImpl(Job job) {
		double dist=job.param[0];
		//Time aware motion splits the job in two, if the time slot is smaller than that needed.
		//The remaining part of the motion is put in the front of the fifo.
		//If the time slot is bigger than needed, the time slot is splitted.
		if (plugHandler.isTimeAware()) {
			double t=tick;//ticks.get();
			tick=0;
			double td=velocity*t;
			if (td>dist) {
				//Split the time slot
				tick=t-(dist/velocity);//ticks.putFirst(t-(dist/velocity));
				if (tbnPath.isSelected())
					addPoint(latlong.x,latlong.y);
			} else if (td<dist) {
				//Split the motion command
				job.type=Job.MOVE;
				job.param[0]=dist-td;
				jobs.put(job);
				dist=td;
			} else {
				if (tbnPath.isSelected())
					addPoint(latlong.x,latlong.y);
			}
		}
		//Destination is not needed so pass location.
		MotionFeatures mf=plugHandler.getMotionFeatures(latlong.x,latlong.y);
		if (mf.onObject!=null && (mf.onObject instanceof gr.cti.eslate.mapModel.geom.PolyLine)) {
			mf.distance=dist;
			//The line traversal algorithm is in _goToCommon. To save from dublicating
			//it is called from here passing the distance parameter.
			_goToCommon(mf);
		} else {
			moveAlgorithm(dist,mf,mf.onObject!=null);
			if (tbnPath.isSelected())
				addPoint(latlong.x,latlong.y);
		}
		return true;
	}

	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean goToImpl(Job job) {
		double longt=job.param[0];
		double lat=job.param[1];
		MotionFeatures mf=plugHandler.getMotionFeatures(longt,lat);
		if (plugHandler.isTimeAware()) {
			double todo=velocity*tick;//ticks.get();
			tick=0;
			mf.distance=todo;
			double trdist=_goToCommon(mf);
			//Make sure that precision errors don't affect motion
			if (Math.abs(Math.abs(trdist)-todo)>0.01d && Math.abs(trdist)<todo)
				tick=(todo-Math.abs(trdist))/velocity;//ticks.putFirst((t odo-Math.abs(trdist))/velocity);
			//Negative travelled distance indicates that the agent wants to stop motion
			//The agent has not arrived yet
			if ((Math.abs(latlong.x-mf.endLongt)>1E-8 || Math.abs(latlong.y-mf.endLat)>1E-8) && trdist>0) {
				if (mf.onObject!=null && mf.onObject instanceof PolyLine) {
					job.type=Job.GOTO_ONLINE_UNFINISHED;
					job.param=null;
					job.mf=mf;
					jobs.putFirst(job);
				} else {
					job.type=Job.GOTO_ONLINE_UNFINISHED;
					job.param[0]=longt;
					job.param[1]=lat;
					jobs.putFirst(job);
				}
			} else {
				if (tbnPath.isSelected())
					addPoint(latlong.x,latlong.y);
			}
		} else {
			_goToCommon(mf);
			if (tbnPath.isSelected())
				addPoint(latlong.x,latlong.y);
		}
		return true;
	}

	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean goToOnHostImpl(Job job) {
		IAgentHost host=job.host;
		double longt=job.param[0];
		double lat=job.param[1];
		MotionFeatures mf=plugHandler.getMotionFeatures(host,longt,lat);
		if (plugHandler.isTimeAware()) {
			if (velocity==0)
				setVelocity(potVelocity*TO_KMH);
			double todo=velocity*tick;//ticks.get();
			tick=0;
			mf.distance=todo;
			double trdist=_goToCommon(mf);
			//Make sure that precision errors don't affect motion
			if (Math.abs(Math.abs(trdist)-todo)>0.01d && Math.abs(trdist)<todo)
				tick=(todo-Math.abs(trdist))/velocity;//ticks.putFirst((t odo-Math.abs(trdist))/velocity);
			//Negative travelled distance indicates that the agent wants to stop motion
			//The agent has not arrived yet
			if ((Math.abs(latlong.x-mf.endLongt)>1E-8 || Math.abs(latlong.y-mf.endLat)>1E-8) && trdist>0) {
				if (mf.onObject!=null && mf.onObject instanceof PolyLine) {
					job.type=Job.GOTO_ONLINE_UNFINISHED;
					job.param=null;
					job.mf=mf;
					job.host=null;
					jobs.putFirst(job);
				} else {
					job.type=Job.GOTO_ONHOST;
					if (job.param==null || job.param.length<2)
						job.param=new double[2];
					job.param[0]=longt;
					job.param[1]=lat;
					job.mf=null;
					job.host=host;
					jobs.putFirst(job);
				}
			} else {
				if (tbnPath.isSelected()) {
					addPoint(latlong.x,latlong.y);
					plugHandler.repaintTrail();
				}
			}
		} else {
			_goToCommon(mf);
			if (tbnPath.isSelected())
				addPoint(latlong.x,latlong.y);
		}
		return true;
	}

	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean goToOnLineUnfinishedImpl(Job job) {
		MotionFeatures mf=job.mf;
		if (plugHandler.isTimeAware()) {
			double todo=velocity*tick;//ticks.get();
			tick=0;
			mf.distance=todo;
			double trdist=_goToCommon(mf);
			//Make sure that precision errors don't affect motion
			if (Math.abs(trdist-todo)>0.01d && trdist<todo)
				tick=(todo-trdist)/velocity;//ticks.putFirst((t odo-trdist)/velocity);
			//The agent has not arrived yet
			if (Math.abs(latlong.x-mf.endLongt)>1E-8 || Math.abs(latlong.y-mf.endLat)>1E-8) {
				job.type=Job.GOTO_ONLINE_UNFINISHED;
				job.mf=mf;
				jobs.putFirst(job);
			}
		} else {
			_goToCommon(mf);
			if (tbnPath.isSelected())
				addPoint(latlong.x,latlong.y);
		}
		return true;
	}

	/**
	 * @return  The distance travelled. If negative, the absolute value is the distance travelled but
	 * the minus sign indicates that the agent wants to stop motion.
	 */
	private double _goToCommon(MotionFeatures mf) {
			double distTravelled=0;
			if (!mf.isValidTrip) {
				Toolkit.getDefaultToolkit().beep();
				return mf.distance;
			}
			if (mf.onObject!=null && (mf.onObject instanceof gr.cti.eslate.mapModel.geom.PolyLine)) {
				int pidx=0;
//				mf.upm=0.00000833333;
//				mf.upp=mf.upm*1000;
				PolyLine line=(PolyLine) mf.onObject;
//				wasOn=line;
				double[] d=new double[6];
				//Two arrays holding the subsequent parts of the motion.
				DblBaseArray x,y;
				//If the arrays are not null, this is the continuation of a stopped line motion.
				if (mf.xPointsLeftToTravel!=null && mf.yPointsLeftToTravel!=null) {
					x=mf.xPointsLeftToTravel;
					y=mf.yPointsLeftToTravel;
				} else {
					//This is a new motion so build all the travel points.
					if (mf.xPointsLeftToTravel!=null) {
						//Reuse the array to avoid garbage collection which produces "hick-ups" to the motion
						mf.xPointsLeftToTravel.clear();
						mf.yPointsLeftToTravel.clear();
						x=mf.xPointsLeftToTravel;
						y=mf.yPointsLeftToTravel;
					} else {
						x=new DblBaseArray(50);
						y=new DblBaseArray(50);
					}
					pidx=0;
					mf.currentSegment=mf.startSegment;
					if (mf.startSegment<mf.endSegment) {
						PathIterator it=line.getPathIterator(null);
						//Put the starting point
						x.add(mf.startLongt);
						y.add(mf.startLat);
						//Skip the preceding points
						int count=0;
						while (count<=mf.startSegment) {
							it.next();
							count++;
						}
						//Add the points of the path
						count=mf.endSegment-mf.startSegment;
						while (count>0) {
							it.currentSegment(d);
							x.add(d[0]);
							y.add(d[1]);
							it.next();
							count--;
						}
						//Put the ending point
						x.add(mf.endLongt);
						y.add(mf.endLat);
					} else if (mf.startSegment>mf.endSegment) {
						//Put the ending point first. The path is constructed vice-versa.
						x.add(mf.endLongt);
						y.add(mf.endLat);
						PathIterator it=line.getPathIterator(null);
						//Skip the preceding points
						int count=0;
						while (count<=mf.endSegment) {
							it.next();
							count++;
						}
						//Add the points of the path
						count=mf.startSegment-mf.endSegment;
						while (count>0) {
							it.currentSegment(d);
							x.pushFront(d[0]);
							y.pushFront(d[1]);
							it.next();
							count--;
						}
						//Put the starting point only if it is not a segment start point, which has been
						//put by the previous loop.
						if (mf.startLongt!=x.get(0) || mf.startLat!=y.get(0)) {
							x.pushFront(mf.startLongt);
							y.pushFront(mf.startLat);
						}
					} else if (mf.startSegment==mf.endSegment) {
						//Put the starting point
						x.add(mf.startLongt);
						y.add(mf.startLat);
						//Put the ending point
						x.add(mf.endLongt);
						y.add(mf.endLat);
					}
					//End building travel points.
				}
				double px=x.get(pidx);//x.popFront();
				double py=y.get(pidx);//y.popFront();
				pidx++;
				boolean broken=false;
				while (pidx<x.size() && !broken) {
					double cx=x.get(pidx);
					double cy=y.get(pidx);
					double segm=pythagorian(cx-px,cy-py);
					int steps=(int) (segm/mf.upp); //In pixels
					double stepX,stepY;
					if (steps!=0) {
						stepX=(cx-px)/steps;
						stepY=(cy-py)/steps;
					} else
						stepX=stepY=0;

					double distStep=pythagorian(stepX,stepY)/mf.upm;
					boolean equalityBroken=false;
					for (int j=0;j<steps-1;j++)
						try {
							if (distTravelled+distStep>mf.distance) {
								double[] mustGo=line.findPointFromDistance(mf.currentSegment,latlong.x,latlong.y,mf.startSegment<=mf.endSegment,mf.distance,mf.upm);
								//OHHHHH! This was a greeeeeeaaat bug! It took me 10 hours to identify!
								//If the mustGo location is the same as agent latlong, it gets in infinite loop.
								//It must break and not define itself broken, because next time the same point will show up.
								if (mustGo[0]==latlong.x && mustGo[1]==latlong.y) {
									System.err.println("AGENT#200009201243: Agent followed a wrong path! Miscalculation!");
									equalityBroken=true;
									break;
								}
								distTravelled+=pythagorian(latlong.x-mustGo[0],latlong.y-mustGo[1]);
								setLongLat(mustGo[0],mustGo[1],false,false,false);
								broken=true;
								break;
							} else {
								distTravelled+=distStep;
								setLongLat(latlong.x+stepX,latlong.y+stepY,false,false,false);
							}
						} catch(AgentRefusesToChangePositionException e) {
							e.printStackTrace();
							break;
						}
						//First check if the final step will make a greater step than that needed.
						//If so, make the motion "broken".
						if (!broken && !equalityBroken) {
							double finalDist=pythagorian(latlong.x-cx,latlong.y-cy)/mf.upm;
							if (distTravelled+finalDist>mf.distance) {
								double[] mustGo=line.findPointFromDistance(mf.currentSegment,latlong.x,latlong.y,mf.startSegment<=mf.endSegment,mf.distance,mf.upm);
								if (mustGo[0]!=latlong.x || mustGo[1]!=latlong.y) {
									distTravelled+=pythagorian(latlong.x-mustGo[0],latlong.y-mustGo[1]);
									try {
										setLongLat(mustGo[0],mustGo[1],false,false,false);
									} catch(AgentRefusesToChangePositionException e) {
										e.printStackTrace();
									}
									broken=true;
								}
							}
						}
						//Now check again if the motion has been broken and take care of the point array.
						if (broken) {
							//Unfortunately the motion didn't end. Push in the stack the current position
							//to continue from that as well as the point never reached.
							x.add(pidx,latlong.x);
							y.add(pidx,latlong.y);
							//x.pushFront(latlong.x);
							//y.pushFront(latlong.y);
							mf.xPointsLeftToTravel=x;
							mf.yPointsLeftToTravel=y;
						} else if (!equalityBroken) {
							//The motion has not been broken, so remove the first elements to travel the next segment.
							pidx++;
							//x.remove(0);
							//y.remove(0);
							//Make the final step, which ensures the correct final location and
							//makes the host repaint the path.
							try {
								distTravelled+=pythagorian(latlong.x-cx,latlong.y-cy)/mf.upm;
								setLongLat(cx,cy,true,true,false);
								if (mf.startSegment<=mf.endSegment)
									mf.currentSegment++;
								else
									mf.currentSegment--;
							} catch(AgentRefusesToChangePositionException e) {
								e.printStackTrace();
							}
							px=cx;
							py=cy;
						} else if (equalityBroken) {
							//Shouldn't get in here. Just to make sure that no infinite loops will happen.
							break;
						}
				}
				return distTravelled/mf.upm;
			} else {
				if (mf.isMovingOnEarth) {
					//Temporary. Cut all motions that exceed half the perimeter of the earth.
					//We must find another algorithm. The cut calculation is not very accurate
					//but is good for the purposes.
					if (Math.sqrt(Math.pow(mf.startLongt-mf.endLongt,2)+Math.pow(mf.startLat-mf.endLat,2))>175) {
						Toolkit.getDefaultToolkit().beep();
						return mf.distance;
					}
					ForwardGeodesy fg=forwardGeodesy(mf.startLongt*TO_RAD,mf.startLat*TO_RAD,mf.endLongt*TO_RAD,mf.endLat*TO_RAD);
					lookImpl(360-fg.aIJ*TO_DEG);
					if (mf.distance>0 && fg.s>mf.distance)
						distTravelled=moveAlgorithm(mf.distance,mf,mf.onObject!=null);
					else
						distTravelled=moveAlgorithm(fg.s,mf,mf.onObject!=null);
					//Ensure correctness of final location
					//jump(mf.endLongt,mf.endLat);
				} else {
					double td=pythagorian(latlong.x-mf.endLongt,latlong.y-mf.endLat)/mf.upm;
					//Turn the agent to the desired location
					lookImpl(Helpers.cartesianAzimuth(latlong.x,latlong.y,mf.endLongt,mf.endLat)*TO_DEG);
					if (mf.distance>0 && td>mf.distance)
						distTravelled=moveAlgorithm(mf.distance,mf,mf.onObject!=null);
					else
						distTravelled=moveAlgorithm(td,mf,mf.onObject!=null);
				}
				return distTravelled;
			}
//		return 0;
	}
	private double pythagorian(double x,double y) {
		return Math.sqrt(x*x+y*y);
	}
	/**
	 * @return  The new XLONJ,PHIJ begining from XLONI,PHII and traveling SIJ meters with AIJ azimuth (in rad).
	 */
	private ReverseGeodesy reverseGeodesy(double XLONI,double PHII,double AIJ,double SIJ) {
		//Semi major axis
		double A=6378137d;
		//Semi minor axis
		double B=A*(1-1d/298.25722210088d);

		double B1=B;

		if (B1<1000)
			B1=A-A/B1;
		double F=(A-B1)/A;
		//double ESQ1=(A*A-B1*B1)/A/A;
		double ESQ2=(A*A-B1*B1)/B1/B1;
		double BONA=B1/A;

		double SA=Math.sin(AIJ);
		double CA=Math.cos(AIJ);
		double SP=Math.sin(PHII)*BONA;
		double CP=Math.cos(PHII);
		double DIAG=Math.sqrt(SP*SP+CP*CP);
		double SU1=SP/DIAG;
		double CU1=CP/DIAG;
		double SSIG1=SU1;
		double CSIG1=CU1*CA;
		double SIG1=Math.atan2(SSIG1,CSIG1);
		double SALF=CU1*SA;

		double USQ=(1-SALF*SALF)*ESQ2;
		double AC=-768+USQ*(320-175*USQ);
		AC=1+USQ*(4096+USQ*AC)/16384;
		double BC=-128+USQ*(74-47*USQ);
		BC=USQ*(256+USQ*BC)/1024;

		//TESTVALUE FOR STOP OF ITERATION
		double DEL=1E-12;

		double DDSIG=0;
		double SIG0=SIJ/B1/AC;
		double SIG=SIG0;
		double IK=-1;

		//START OF ITERATION
		double SSIG,DSIG,DSIGM,CSIG,CDSIG;//SDSIG
		while (true) {
			DSIG=DDSIG;
			DSIGM=2*SIG1+SIG;
			SSIG=Math.sin(SIG);
			CSIG=Math.cos(SIG);
			//SDSIG=Math.sin(DSIGM);
			CDSIG=Math.cos(DSIGM);
			if (IK>0)
				break;
			DDSIG=BC*CDSIG*(-3+4*SSIG*SSIG)*(-3+4*CDSIG*CDSIG)/6;
			DDSIG=BC*SSIG*(CDSIG+BC*(CSIG*(-1+2*CDSIG*CDSIG)-DDSIG)/4);
			SIG=SIG0+DDSIG;
			if (Math.abs(DDSIG-DSIG)<=DEL)
				IK=-IK;
		}

		//COMPUTE LATITUDE OF POINT J
		SP=SU1*CSIG+CU1*SSIG*CA;
		CP=SU1*SSIG-CU1*CSIG*CA;
		CP=(1-F)*Math.sqrt(SALF*SALF+CP*CP);
		double PHIJ=Math.atan2(SP,CP);

		//COMPUTE LONGITUDE AT POINT J
		double SDL=SSIG*SA;
		double CDL=CU1*CSIG-SU1*SSIG*CA;
		double CSQALF=1-SALF*SALF;
		double C=CSQALF*(4+F*(4-3*CSQALF))*F/16;
		double DDL=CDSIG+C*CSIG*(-1+2*CDSIG*CDSIG);
		DDL=(1-C)*F*SALF*(SIG+C*SSIG*DDL);
		double XLONJ=XLONI+Math.atan2(SDL,CDL)-DDL;

		//COMPUTE AZIMUT AT POINT J
		SA=SALF;
		CA=-SU1*SSIG+CU1*CSIG*CA;
		return new ReverseGeodesy(XLONJ,PHIJ,Math.atan2(SA,CA));
	}
	/**
	 * @return  The azimuth from long1,lat1 to long2,lat2 (in radians).
	 */
	private ForwardGeodesy forwardGeodesy(double XLONI,double PHII,double XLONJ,double PHIJ) {
		//GRS80
		double a = 6378137d;
		double b = a * (1 - 1d / 298.25722210088d);
		double B1 = b;
		if (B1 < 1000d)
			B1 = a - a / B1;

		//COMPUTE SOME CONSTANTS
		double Pi = Math.PI;
		double BONA = B1 / a;
		double f = (a - B1) / a;
		//double ESQ1 = (a * a - B1 * B1) / a / a;
		double ESQ2 = (a * a - B1 * B1) / B1 / B1;

		//TESTVALUE FOR STOP OF ITERATION
		double DEL = 1E-12;

		double DLL = XLONJ - XLONI;
		double DL = DLL;
		double DDL = 0;

		double SP1 = Math.sin(PHII) * BONA;
		double CP1 = Math.cos(PHII);
		double SP2 = Math.sin(PHIJ) * BONA;
		double CP2 = Math.cos(PHIJ);
		//TRIG. FUNCTIONS OF REDUCED LATITUDE
		double DIAG = Math.sqrt(SP1 * SP1 + CP1 * CP1);
		double SU1 = SP1 / DIAG;
		double CU1 = CP1 / DIAG;
		DIAG = Math.sqrt(SP2 * SP2 + CP2 * CP2);
		double SU2 = SP2 / DIAG;
		double CU2 = CP2 / DIAG;
		//START ITERATION
		boolean exit=false;
		double SIJ,AIJ,CSIG,SIG,AJI,EDL,SDL,CDL,SQSIG,SSIG,CSQALF,SALF,C,CDSIGM,DPIL,DPHI,EC,FC,AC,BC,USQ,D,DSIG,SANG,SA,CA;
		BC=AC=CA=0;
		do {
			EDL = DDL;
			SDL = Math.sin(DL);
			CDL = Math.cos(DL);
			SQSIG = Math.pow(CU2 * SDL,2) + Math.pow(CU1 * SU2 - SU1 * CU2 * CDL,2);
			SSIG = Math.sqrt(SQSIG);

			if (SSIG - DEL <= 0) {
				SIJ = 0;
				AIJ = 0;
				AJI = 0;
				return new ForwardGeodesy(SIJ,AIJ,AJI);
			} else {
				CSIG = SU1 * SU2 + CU1 * CU2 * CDL;
				SIG = Math.atan2(SSIG, CSIG);
				SALF = CU1 * CU2 * SDL / SSIG;
				CSQALF = 1 - SALF * SALF;
			}
			if (CSQALF - DEL <= 0)
				CDSIGM = CSIG;
			else
				CDSIGM = CSIG - 2 * SU1 * SU2 / CSQALF;

			C = CSQALF * (4 + f * (4 - 3 * CSQALF)) * f / 16;
			DDL = CDSIGM + C * CSIG * (-1 + 2 * CDSIGM * CDSIGM);
			DDL = (1 - C) * f * SALF * (SIG + C * SSIG * DDL);
			DL = DLL + DDL;
			if (Math.abs(DL) > Pi) {
				DPIL = Pi - Math.abs(DLL);
				DPIL = mySign(DPIL, DLL);
				DL = 0;
				CSQALF = 0.5;
				SALF = Math.sqrt(CSQALF);
				DPHI = Math.atan2(SU1,CU1) + Math.atan2(SU2,CU2);
				SIG = Pi - Math.abs(DPHI);
				SSIG = Math.sin(SIG);
				CSIG = Math.cos(SIG);

				do {
					EDL = SALF;
					C = CSQALF * (4 + f * (4 - 3 * CSQALF)) * f / 16;
					CDSIGM = CSIG - 2 * SU1 * SU2 / CSQALF;
					D = CDSIGM + C * CSIG * (-1 + 2 * CDSIGM * CDSIGM);
					D = (1 - C) * f * (SIG + C * SSIG * DDL);
					SALF = (DPIL - DL) / D;
					CSQALF = 1 - SALF * SALF;
					SDL = SALF * SSIG / (CU1 * CU2);
					CDL = -Math.sqrt(1 - SDL * SDL);
					DL = Math.atan2(SDL,-CDL);
					SQSIG = Math.pow(CU2 * SDL,2) + Math.pow(CU1 * SU2 - SU1 * CU2 * CDL,2);
					SSIG = Math.sqrt(SQSIG);
					CSIG = SU1 * SU2 + CU1 * CU2 * CDL;
					SIG = Math.atan2(SSIG,CSIG);
				} while(Math.abs(SALF - EDL) > DEL);
				EC = Math.sqrt(1 + ESQ2 * CSQALF);
				FC = (EC - 1) / (EC + 1);
				AC = (1 + 0.25 * FC * FC) / (1 - FC);
				BC = FC * (1 - FC * FC * 3 / 8);
				exit=true;
			}

			if (!exit && Math.abs(EDL - DDL) <= DEL) {
				USQ = CSQALF * ESQ2;
				AC = -768 + USQ * (320 - 175 * USQ);
				AC = 1 + USQ * (4096 + USQ * AC) / 16384;
				BC = -128 + USQ * (74 - 47 * USQ);
				BC = USQ * (256 + USQ * BC) / 1024;
				SDL = Math.sin(DL);
				CDL = Math.cos(DL);
				exit=true;
			}
		} while (!exit);

		DSIG = BC * SSIG * (CDSIGM + BC * (CSIG * (-1 + 2 * CDSIGM * CDSIGM) - BC * CDSIGM * (-3 + 4 * SQSIG) * (-3 + 4 * CDSIGM * CDSIGM) / 6) / 4);
		SIJ = B1 * AC * (SIG - DSIG);

		//AZIMUTH COMPUTATION

		SANG = CU1 * SU2 - SU1 * CU2 * CDL;
		if (CU1 <= DEL)
			AIJ = Pi;
		else {
			SA = SALF / CU1;
			CA = Math.sqrt(1 - SA * SA);
			CA = mySign(CA, SANG);
			AIJ = Math.atan2(SA,CA);
		}
		SA = SALF;
		CA = -SU1 * SSIG + CU1 * CSIG * CA;
		AJI = Math.atan2(SA,CA);

		return new ForwardGeodesy(SIJ,AIJ,AJI);
	}

	private double mySign(double a,double b) {
		if (b >= 0)
			return Math.abs(a);
		else
			return -(Math.abs(a));
	}
	/**
	 * Job implementation method.
	 * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
	 */
	boolean lookImpl(double t) {
		tilt=Helpers.normAngle(t);
        if (lastTravelledOn!=null && lastTravelledOn instanceof Heading.Line)
            ((Heading.Line) lastTravelledOn).invalid=true;
		plugHandler.tiltChanged();
		return true;
	}
    /**
     * Job implementation method.
     * @return If true, declares that the job has finished. Otherwise, more ticks are needed.
     */
	boolean turnImpl(double t) {
		tilt=Helpers.normAngle(tilt+t);
        if (lastTravelledOn!=null && lastTravelledOn instanceof Heading.Line)
            ((Heading.Line) lastTravelledOn).invalid=true;
		plugHandler.tiltChanged();
	    return true;
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE);
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(100,100);
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		ESlateFieldMap fm=(ESlateFieldMap) in.readObject();
		pnlPhoto.setPhoto(fm.get("photo",(Icon) null));
		latlong.x=fm.get("long",latlong.x);
		latlong.y=fm.get("lat",latlong.y);
		if (latlong.x==Double.MAX_VALUE)
			lblStatus.setText(bundleMessages.getString("notpositioned"));
		else
			lblStatus.setText(nf.format(latlong.x)+" , "+nf.format(latlong.y));
		setTiltFromUpright(fm.get("tilt",tilt));
		travelsOnv=fm.get("travelsOn",travelsOnv);
		if (travelsOnv==IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER)
			setTravellingOnMotionLayerID(fm.get("mLayerID",""));
		else {
			if (travelsOnv==IAgent.TRAVELS_EVERYWHERE)
				mLayerID=bundleBeanInfo.getString("travelseverywhere");
			else if (travelsOnv==IAgent.TRAVELS_ON_ROADS)
				mLayerID=bundleBeanInfo.getString("travelsonroads");
			else if (travelsOnv==IAgent.TRAVELS_ON_RAILWAYS)
				mLayerID=bundleBeanInfo.getString("travelsonrailways");
			else if (travelsOnv==IAgent.TRAVELS_ON_SEA)
				mLayerID=bundleBeanInfo.getString("travelsonsea");
			else if (travelsOnv==IAgent.TRAVELS_ON_AIR)
				mLayerID=bundleBeanInfo.getString("travelsonair");
		}
		int nph=fm.get("numberOfPhases",0);
		autoProduce=fm.get("autoProduce",autoProduce);
		//If autoproduce==true, create the icons again
		if (autoProduce) {
			clearPhases();
			setNumberOfPhases(nph);
			Icon[] icons=Helpers.producePhases(this,fm.get("baseIcon",(Icon) null),nph);
			if (nph==1)
				//0 and 360 are the save normalized angle. Trick!
				setPhaseIcon(0,360-1E-6,icons[0]);
			else {
				double angleStep=360d/nph;
				double startAngle=-(angleStep/2);
				for (int i=0;i<icons.length;i++) {
					setPhaseIcon(startAngle,startAngle+angleStep,icons[i]);
					startAngle+=angleStep;
				}
			}
		//If autoproduce==false, put the icons the stucture
		} else {
			faces=new AgentFace(this,nph);
			faces.faces=(AgentFace.Interval[]) fm.get("icondata",faces.faces);
		}
		path=(Path) fm.get("path");
		type=fm.get("type",type());
		if (type()==IAgent.TYPE_OTHER)
			setType(fm.get("typeID",""));
		else {
			if (type==IAgent.TRAVELS_EVERYWHERE)
				typeID=bundleBeanInfo.getString("man");
			else if (type==IAgent.TYPE_AUTO)
				typeID=bundleBeanInfo.getString("auto");
			else if (type==IAgent.TYPE_TRAIN)
				typeID=bundleBeanInfo.getString("train");
			else if (type==IAgent.TYPE_TRAIN)
				typeID=bundleBeanInfo.getString("ship");
			else if (type==IAgent.TYPE_AIRPLANE)
				typeID=bundleBeanInfo.getString("plane");
		}
		String[] s=(String[]) fm.get("embark",(String[]) null);
		if (s!=null && s.length>0) {
			String[] e=new String[s.length];
			for (int i=0;i<s.length;i++)
				try {
					e[i]=bundleBeanInfo.getString(s[i]);
				} catch(MissingResourceException ex) {
					//It is of custom type
					e[i]=s[i];
				}
			setEmbarkOnAgents(e);
		} else
			setEmbarkOnAgents(new String[0]);
		velocity=fm.get("velocity",velocity);
		plugHandler.velocityChanged();
		potVelocity=fm.get("potentialVelocity",potVelocity);
		maxVelocity=fm.get("maxVelocity",maxVelocity);
		minVelocity=fm.get("minVelocity",minVelocity);
		stopAtCrossings=fm.get("stopAtCrossings",false);

		//Component visual state
		if (fm.containsKey("border")){
			try {
				BorderDescriptor bd=(BorderDescriptor) fm.get("border");
				setBorder(bd.getBorder());
			} catch (Throwable thr) {/*No Border*/}
		}
		tbnPath.setSelected(fm.get("tbnPath",true));

		//Restore the names this agent hosts (are embarked) and re-embark them
		final String[] ns=(String[]) fm.get("embarkedAgentNames");
		if (ns!=null)
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i=0;i<ns.length;i++)
						embark(plugHandler.getAgent(ns[i]));
				}
			});

		setStatusbarVisible(fm.get("statusbarvisible",lblStatus.isVisible()));
		setAlwaysVisible(fm.get("alwaysvisible",alwaysVisible));

		setUnitTolerance(fm.get("unitTolerance",0.5d));

		//Add the location point to the path, if no path is saved
		if (tbnPath.isSelected() && path.size()==0 && latlong.x!=Double.MAX_VALUE)
			addPoint(latlong.x,latlong.y);
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap fm=new ESlateFieldMap("1.0.0");
		fm.put("photo",pnlPhoto.getPhoto());
		fm.put("long",latlong.x);
		fm.put("lat",latlong.y);
		fm.put("tilt",tilt);
		fm.put("travelsOn",travelsOnv);
		if (travelsOnv==IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER)
			fm.put("mLayerID",mLayerID);
		fm.put("numberOfPhases",getNumberOfPhases());
		fm.put("autoProduce",autoProduce);
		//If autoproduce==true, only put the default icon
		if (autoProduce) {
			Icon ic=faces.get(0);
			if (ic instanceof NewRestorableImageIcon)
				fm.put("baseIcon",ic);
			else {
				//If the icon is not restorable, create a restorable one.
				BufferedImage bi=new BufferedImage(ic.getIconWidth(),ic.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
				ic.paintIcon(this,bi.getGraphics(),0,0);
				fm.put("baseIcon",new NewRestorableImageIcon(bi));
			}
		//If autoproduce==false, put all the icons
		} else
			fm.put("icondata",faces.faces);
		fm.put("path",path);
		//Put the type of the agent
		fm.put("type",type());
		if (type()==IAgent.TYPE_OTHER)
			fm.put("typeID",getType());
		//Put the array where the agent may embark. It's a bit complicated just to make sure
		//that microworlds will be read from different language systems
		String[] e=new String[embarkOnAgents.length];
		for (int i=0;i<embarkOnAgents.length;i++) {
			String s=embarkOnAgents[i].toUpperCase();
			if (s.equals(bundleBeanInfo.getString("man")))
				e[i]="man";
			else if (s.equals(bundleBeanInfo.getString("auto")))
				e[i]="auto";
			else if (s.equals(bundleBeanInfo.getString("train")))
				e[i]="train";
			else if (s.equals(bundleBeanInfo.getString("ship")))
				e[i]="ship";
			else if (s.equals(bundleBeanInfo.getString("plane")))
				e[i]="plane";
			else if (s.equals(bundleBeanInfo.getString("all")))
				e[i]="all";
			else
				e[i]=embarkOnAgents[i];
		}
		fm.put("embark",e);
		fm.put("velocity",velocity);
		fm.put("potentialVelocity",potVelocity);
		fm.put("maxVelocity",maxVelocity);
		fm.put("minVelocity",minVelocity);
		fm.put("stopAtCrossings",stopAtCrossings);

		//Component visual state
		if (borderChanged) {
			try {
				BorderDescriptor bd=ESlateUtils.getBorderDescriptor(getBorder(),this);
				fm.put("border",bd);
			} catch (Throwable thr) {}
		}
		fm.put("tbnPath",tbnPath.isSelected());

		//Write the names this agent hosts (are embarked)
		if (embarkedAgents.size()!=0) {
			String[] ns=new String[embarkedAgents.size()];
			for (int i=0;i<ns.length;i++)
				ns[i]=((IAgent) embarkedAgents.get(i)).getName();
			fm.put("embarkedAgentNames",ns);
		}

		fm.put("statusbarvisible",lblStatus.isVisible());
		fm.put("alwaysvisible",isAlwaysVisible());

		fm.put("unitTolerance",unitTolerance);

		out.writeObject(fm);
	}

	private ESlateHandle handle;
	AgentEventMulticaster listeners=new AgentEventMulticaster();
	/**
	 * The (longitude,latitude) cordinates of the agent.
	 */
	Point2D.Double latlong;
	/**
	 * The path.
	 */
	private Path path;
	/**
	 * The tilt from upright.
	 */
	private double tilt;
	/**
	 * Identifies where the agent travels.
	 */
	private int travelsOnv;
	/**
	 * If the agent travels on a custom motion layer, this is the layer's ID.
	 */
	private String mLayerID;
	/**
	 * Identifies the type of the agent.
	 */
	private int type;
	/**
	 * If the agent is of an unknown type this is the agent's type string.
	 */
	private String typeID;
	/**
	 * Plug handler.
	 */
	PlugHandler plugHandler;
	/**
	 * Structure holding the icon faces, if any.
	 */
	private AgentFace faces;
	/**
	 * Structure holding the animated icon faces, if any.
	 * <strong>This property is not saved!</strong>
	 */
	AgentFaceAnimated animatedFaces;
	/**
	 * Flags the automatic production of phases.
	 */
	private boolean autoProduce;
	/**
	 * Where is it embarked?
	 */
	private IAgent isEmbarkedOn;
	/**
	 * The velocity.
	 */
	private double velocity;
	/**
	 * Potential velocity is the velocity the agent will have when it starts moving.
	 */
	private double potVelocity;
	/**
	 * The maximum velocity.
	 */
	private double maxVelocity;
	/**
	 * The minimum velocity.
	 */
	private double minVelocity;
	/**
	 * This property indicates when this agent wants to be always visible. This means that a component
	 * hosting this agent should (but is not forced to) make it visible wherever it is. E.g. if the
	 * agent is hosted in a map, the map should scroll if the agent goes out of the visible map area.
	 */
	private boolean alwaysVisible;
	private static NumberFormat nf;
	private static double TO_MS=1/3.6d;
	private static double TO_KMH=3.6d;
	private static double TO_RAD=Math.PI/180;
	private static double TO_DEG=180/Math.PI;
//	private static double EARTH_PERIMETER=2*Math.PI*6400000D;
	//Bundles
	protected static ResourceBundle bundlePlugs,bundleSegmentProperties,bundleMessages,bundleTooltips,bundleBeanInfo,bundleInfo;
	//UI Members
	private SmartLayout smartLayout1 = new SmartLayout();
	private ESlateToolBar toolBar=new ESlateToolBar();
	private JToggleTool tbnPath = new JToggleTool();
	private JButtonTool btnLocate = new JButtonTool();
	private JScrollPane scrFace = new JScrollPane();
	private JLabel lblStatus = new JLabel();
	private JButtonTool btnInfo = new JButtonTool();
	private boolean borderChanged;
	/**
	 * A dimension object is cached to avoid creating a new one, each time the size is needed.
	 */
	private Dimension dimCache;
	private final int DEFAULT_SIZE=18;
	/**
	 * This is a static Set where each agent registers its type. In this way all the agents
	 * know the existing types.
	 */
	public static final Set<String> existingTypes=new TreeSet<String>();
	/**
	 * Keeps the types of agents this agent can embark on.
	 */
	private String[] embarkOnAgents;
	/**
	 * Keeps all the agents this agent hosts.
	 */
	private ArrayList<IAgent> embarkedAgents;
	/**
	 * The amount of seconds available as a tick.
	 */
	double tick;
	/**
	 * The map units that are the "hotspot" of the agent, forming a square around its center.
	 */
    private double unitTolerance;
	/**
	 * Keeps a reference to the object that the agent moved on in the last motion.
	 */
	protected Heading lastTravelledOn;
	/**
	 * Stop at crossings.
	 */
	private boolean stopAtCrossings;
    /**
     * Media tracker to load animated gifs.
     */
	private MediaTracker mt=new MediaTracker(this);

	/**
	 * A special panel that puts a photo in its middle and provides methods for it.
	 */
	private class PhotoPanel extends JPanel {
		private JLabel label=new JLabel();
		{
			setOpaque(false);
			setBackground(Color.white);
			setLayout(new BorderLayout());
			label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label,BorderLayout.CENTER);
		}
		void setPhoto(Icon ii) {
			if (ii!=null && !(ii instanceof NewRestorableImageIcon)) {
				BufferedImage bi=new BufferedImage(ii.getIconWidth(),ii.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
				ii.paintIcon(this,bi.getGraphics(),0,0);
				ii=new NewRestorableImageIcon(bi);
			}
			label.setIcon(ii);
			repaint();
		}
		Icon getPhoto() {
			return label.getIcon();
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (label.getIcon()==null) {
				//Paint a smiley
				Graphics2D g2=(Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				int min=Math.min(getWidth(),getHeight());
				int s=(int) (min*0.9f);
				int sX=(getWidth()-s)/2;
				int sY=(getHeight()-s)/2;
				g2.setPaint(new Color(255,255,192));
				g2.fillOval(sX,sY,s,s);
				g2.setPaint(Color.black);
				g2.setStroke(new BasicStroke(0.05f*min));
				//Paint outline
				g2.drawOval(sX,sY,s,s);
				int s2=(int) (min*0.6f);
				int sX2=(getWidth()-s2)/2;
				int sY2=(getHeight()-s2)/2;
				//Paint smile
				g2.drawArc(sX2,sY2,s2,s2,200,140);
				int s3=(int) (0.15f*min);
				int of3=(int) (0.25f*min);
				int of4=(int) (0.1f*min);
				int of5=(int) (0.2f*min);
				//Paint right eye
				g2.fillOval(getWidth()/2-of3,getHeight()/2-of5,s3,s3);
				//Paint left eye
				g2.fillOval(getWidth()/2+of4,getHeight()/2-of5,s3,s3);
			}
		}
	};
	private PhotoPanel pnlPhoto=new PhotoPanel();
	/**
	 * The component version.
	 */
	public static final String version="3";

	{
		handle=ESlate.registerPart(this);
		Locale locale=handle.getLocale();
		dimCache=new Dimension(0,0);
		bundleInfo=java.util.ResourceBundle.getBundle("gr.cti.eslate.agent.BundleInfo",handle.getLocale());
		String[] info={bundleInfo.getString("part"),bundleInfo.getString("development"),bundleInfo.getString("contribution"),bundleInfo.getString("copyright")};
		handle.setInfo(new ESlateInfo(bundleInfo.getString("compo")+" "+version,info));
	   //Localize
		bundleSegmentProperties=ResourceBundle.getBundle("gr.cti.eslate.agent.BundleSegmentProperties",locale);
		bundlePlugs=ResourceBundle.getBundle("gr.cti.eslate.agent.BundlePlugs",locale);
		bundleMessages=ResourceBundle.getBundle("gr.cti.eslate.agent.BundleMessages",locale);
		bundleTooltips=ResourceBundle.getBundle("gr.cti.eslate.agent.BundleTooltips",locale);
		bundleBeanInfo=ResourceBundle.getBundle("gr.cti.eslate.agent.BundleAgentBeanInfo",locale);
	}

	private void jbInit() throws Exception {
		this.setLayout(smartLayout1);
		scrFace.getViewport().setBackground(Color.white);
		lblStatus.setBorder(BorderFactory.createEtchedBorder());
		lblStatus.setText(" ");
		
		this.add(toolBar, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
//		this.add(btnLocate, new com.thwt.layout.LayoutConstraint(
//					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
//					new com.thwt.layout.EdgeAnchor(tbnPath, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
//					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
//					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		this.add(scrFace, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 2),
					new com.thwt.layout.EdgeAnchor(toolBar, Anchor.Right, Anchor.Right, Anchor.Left, 1),
					new com.thwt.layout.EdgeAnchor(lblStatus, Anchor.Top, Anchor.Above, Anchor.Bottom, 1)));
		this.add(lblStatus, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 3),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 3),
					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
//		this.add(btnInfo, new com.thwt.layout.LayoutConstraint(
//					new com.thwt.layout.ContainerAnchor(Anchor.Left, 3),
//					new com.thwt.layout.EdgeAnchor(btnLocate, Anchor.Bottom, Anchor.Below, Anchor.Top, 0),
//					new com.thwt.layout.PreferredDimAnchor(Anchor.Width, 0, 1.0),
//					new com.thwt.layout.PreferredDimAnchor(Anchor.Height, 0, 1.0)));
		scrFace.setBorder(BorderFactory.createLoweredBevelBorder());
	}

	private JobFIFO jobs;
}
