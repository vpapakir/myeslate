package gr.cti.eslate.mapViewer;

import gr.cti.eslate.agent.Agent;
import gr.cti.eslate.agent.AgentRefusesToChangePositionException;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.IProtocolPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.mapModel.geom.Heading;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.IAgentHost;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IPolyLine;
import gr.cti.eslate.protocol.IPolyLineLayerView;
import gr.cti.eslate.protocol.IPolygonLayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.MotionReport;
import gr.cti.typeArray.IntBaseArray;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 * In this layer agents and their paths are drawn.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
class MotionPane extends JPanel implements IAgentHost,TransparentMouseInput {
	MotionPane(MapPane mpPane) {
		this.mapPane=mpPane;
		setOpaque(false);
		setLayout(null);
		initialized=false;
	}

	void initialize() {
		if (initialized)
			return;
		initialized=true;
		hash=new HashMap();
		visible=new HashMap();
		poscursor=MapViewer.createCustomCursor("images/positionagentcursor.gif",new Point(7,7),new Point(15,15),"positioncursor");
		nocursor=MapViewer.createCustomCursor("images/nocursor.gif",new Point(7,7),new Point(15,15),"nocursor");
		locateThreads=0;
		pending=false;
		//Trick to avoid crashing when Agent classes don't exist
		try {
			Class c=Class.forName("gr.cti.eslate.protocol.IAgent");
            c.toString();
		} catch(ClassNotFoundException e) {
			//**EXIT CONSTRUCTOR!** No Agents!
			return;
		}

		//Add a listener that removes the listeners which place the agent
		//Put in a thread because the protocol implementor (this) is not constructed yet.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mapPane.viewer.agentPlug.addDisconnectionListener(new DisconnectionListener() {
					public void handleDisconnectionEvent(DisconnectionEvent e) {
						if (pml!=null) {
							removeMouseListener(pml);
							removeMouseMotionListener(pml);
							pml=null;
							mapPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
					}
				});
			}
		});
		//This listener brings to front the nearest to the mouse agent representation.
		//This is useful when agents overlap.
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				Iterator it=hash.keySet().iterator();
				IAgent front=null; double dist=Double.MAX_VALUE;
				while (it.hasNext()) {
					IAgent cur=(IAgent) it.next();
					AgentComponent ac=(AgentComponent) hash.get(cur);
					double curdist=Math.sqrt(Math.pow(ac.getX()+(double)ac.getWidth()/2-e.getX(),2)+Math.pow(ac.getY()+(double)ac.getHeight()/2-e.getY(),2));
					if (curdist<dist) {
						dist=curdist;
						front=cur;
					}
				}
				if (front!=null) {
					if (bringToFront(front))
						repaintAgent(front);
				}
			}
		});
	}


	/**
	 * Adds a MouseInputListener. This is different from adding a MouseListener and a MouseMotionListener seperately.
	 */
	public void addMouseInputListener(MouseInputListener l) {
		if (l==null) return;
		if (ml==null)
			ml=new ArrayList();
		ml.add(l);
		addMouseListener(l);
		addMouseMotionListener(l);
	}
	/**
	 * Removes a MouseInputListener.
	 */
	public void removeMouseInputListener(MouseInputListener l) {
		if (ml==null)
			return;
		ml.remove(l);
		removeMouseListener(l);
		removeMouseMotionListener(l);
	}

	/**
	 * Removes all Mouse and MouseMotion Listeners.
	 * @return A list containing the listeners removed.
	 */
	public java.util.List removeAllListeners() {
		ArrayList copy=new ArrayList();
		if (ml==null)
			return copy;
		for (int i=0;i<ml.size();i++) {
			removeMouseListener((MouseListener) ml.get(i));
			removeMouseMotionListener((MouseMotionListener) ml.get(i));
			copy.add(ml.get(i));
		}
		ml.clear();
		return copy;
	}

	/**
	 * Should make public access to processMouseEvent.
	 * @param e The event.
	 */
	public void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
	}

	/**
	 * Should make public access to processMouseMotionEvent.
	 * @param e The event.
	 */
	public void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
	}
	/**
	 * Informs the host that the agent's location has been changed.
	 * @param   a           The agent.
	 */
	public void locationChanged(IAgent a) {
		//If the agent is embarked and doesn't appear in this map, don't do anything
		//An embarked agent appears in all the hosts that do not contain its carrying agent.
		if (a.isEmbarked()) {
			if (hash.get(a)!=null)
				repaintAgent(a);
			return;
		} else
			repaintAgent(a);
	}

	/**
	 * Informs the host that the agent's location has been changed.
	 * @param   a           The agent.
	 * @param   repaint     The agent wish to be repainted.
	 */
	public void locationChanged(IAgent a,boolean repaint) {
		//If the agent is embarked and doesn't appear in this map, don't do anything
		//An embarked agent appears in all the hosts that do not contain its carrying agent.
		if (a.isEmbarked()) {
			if (repaint && hash.get(a)!=null)
				repaintAgent(a);
			return;
		} else {
			if (repaint)
				repaintAgent(a);
		}
	}

	protected double distance(AgentComponent a,AgentComponent b) {
		return Math.sqrt(Math.pow(a.getX()+a.getWidth()/2-b.getX()-b.getWidth()/2,2)+Math.pow(a.getY()+a.getHeight()/2-b.getY()-b.getHeight()/2,2));
	}

	private boolean collide(AgentComponent a,AgentComponent b) {
		Agent agA=((Agent) a.agent);
		double agAtol=agA.getUnitTolerance();
        Rectangle2D.Double agAhotspot=new Rectangle2D.Double(agA.getLongitude()-agAtol,agA.getLatitude()-agAtol,2*agAtol,2*agAtol);

		Agent agB=((Agent) b.agent);
		double agBtol=agB.getUnitTolerance();
        Rectangle2D.Double agBhotspot=new Rectangle2D.Double(agB.getLongitude()-agBtol,agB.getLatitude()-agBtol,2*agBtol,2*agBtol);
		double x11=agAhotspot.getX(); double y11=agAhotspot.getY();
		double x12=agAhotspot.getX()+agAhotspot.getWidth(); double y12=agAhotspot.getY()+agAhotspot.getHeight();
		double x21=agBhotspot.getX(); double y21=agBhotspot.getY();
		double x22=agBhotspot.getX()+agBhotspot.getWidth(); double y22=agBhotspot.getY()+agBhotspot.getHeight();
		if (x22<x11 || y22<y11 || x12<x21 || y12<y21)
			return false;
		return true;
	}
	/**
	 * Informs the host that the agent's orientation has been changed.
	 */
	public void orientationChanged(IAgent a) {
        AgentComponent c=((AgentComponent) hash.get(a));
        if (c!=null)
            //c.repaint();
			repaint(c.getBounds());
	}
	/**
	 * Forces the host to repaint the agent. The agent may need this when it changes
	 * its face or whenever it thinks that the host has an inconsistent image of its.
	 */
	public void repaintAgent(IAgent a) {
		AgentComponent c=((AgentComponent) hash.get(a));
		if (c!=null && mapPane!=null) {
			double tx,ty;
			synchronized (synchPixel) {
				double[] t=toPixel(a.getLongitude(),a.getLatitude());
				tx=t[0];
				ty=t[1];
			}
			//Find the portion of the screen viewed in real coordinates
			synchronized (synchRect) {
				synchRect.setRect(0,0,getWidth(),getHeight());
				mapPane.transformRect(mapPane.getInverseTransform(),synchRect);
                mapPane.transformRect(mapPane.getInversePositionTransform(),synchRect);
			}
			boolean inview;
			if (a.isAlwaysVisible())
				inview=agentInView(a);
			else
				inview=true;
			Dimension d=a.getFaceSize();
			int oldx=c.getX();
			int oldy=c.getY();
			int newx=(int) Math.round(tx-d.width/2d);
			int newy=(int) Math.round(ty-d.height/2d);
			//Break the method when there is no change in pixels
			if (newx!=oldx || newy!=oldy || (a.isAlwaysVisible() &&!inview)) {
				if (a.isAnimated())
					c.animate();
				if (newx!=oldx || newy!=oldy)
					//Check agent collisions
					checkSpriteCollisions(a);

				boolean scrolled=false;
				//If the agent wants to be always visible, try to scroll the view.
				//Scroll only when width and height are > 0 which means that the component is fully
				//initialized and show on the screen.
				if (a.isAlwaysVisible() && getWidth()>0 && getHeight()>0 && !inview) {
					IMapView map=mapPane.map;
					//Check if it will be possible for the agent to be shown in the the current region
					//If not, we will change region
					synchronized (synchRect) {
						synchRect.setRect(map.getActiveRegionView().getBoundingRect());
						double xpoints=Math.abs(getWidth()/VALID_VIEW_FRACTION/mapPane.getPositionTransform().getScaleX());
						double ypoints=Math.abs(getHeight()/VALID_VIEW_FRACTION/mapPane.getPositionTransform().getScaleY());
						synchRect.setRect(synchRect.x+xpoints,synchRect.y+ypoints,synchRect.width-2*xpoints,synchRect.height-2*ypoints);
						if (!synchRect.contains(a.getLongLat())) {
							//Find all the regions of the same depth and choose the one the center of which is closer to the agent.
							int depth=map.getActiveRegionView().getDepthInTree();
							IRegionView[] possible=map.getRegionsOfDepth(depth);

							double closestDist=Double.MAX_VALUE;
							IRegionView closest=null;
							for (int i=0;i<possible.length;i++) {
								Rectangle2D brect=possible[i].getBoundingRect();
								//Check if the agent will be inside the view of the possible map
								double check=a.getLongLat().distance(brect.getCenterX(),brect.getCenterY());
								if (brect.contains(a.getLongLat()) && check<closestDist) {
									closestDist=check;
									closest=possible[i];
								}
							}
							if (closest!=null && closest!=map.getActiveRegionView()) {
								//Inform the listeners that there is a time consuming change in the map
								if (mapPane.viewer.listeners!=null) {
									MapViewerEvent e=new MapViewerEvent(this,MapViewerEvent.MAP_VIEWER_BUSY,new Boolean(true));
									for (int i=0;i<mapPane.viewer.listeners.size();i++) {
										try {
											((MapViewerListener) mapPane.viewer.listeners.get(i)).mapViewerBusyStatusChanged(e);
										} catch (AbstractMethodError err) {}
									}
								}
								map.setActiveRegionView(closest);
								//Inform the listeners that there is a time consuming change in the map
								if (mapPane.viewer.listeners!=null) {
									MapViewerEvent e=new MapViewerEvent(this,MapViewerEvent.MAP_VIEWER_BUSY,new Boolean(false));
									for (int i=0;i<mapPane.viewer.listeners.size();i++) {
										try {
											((MapViewerListener) mapPane.viewer.listeners.get(i)).mapViewerBusyStatusChanged(e);
										} catch (AbstractMethodError err) {}
									}
								}
								synchronized (synchPixel) {
									double[] t=toPixel(a.getLongitude(),a.getLatitude());
									tx=t[0];
									ty=t[1];
								}
								scrolled=true;
							}
						}
						//If the agent remains in the region, see if we must scroll.
						synchRect.setRect(getWidth()/VALID_VIEW_FRACTION,getHeight()/VALID_VIEW_FRACTION,(VALID_VIEW_FRACTION-2)*getWidth()/VALID_VIEW_FRACTION,(VALID_VIEW_FRACTION-2)*getHeight()/VALID_VIEW_FRACTION);
						double oldOff=mapPane.offsetX;
						if (tx<synchRect.x) {
							int dif=(int) Math.round(synchRect.x-tx)+120;
							mapPane.addOffsetX(Math.max(dif,AUTO_SCROLL));
							scrolled=scrolled || oldOff!=mapPane.offsetX;
						} else if (tx>synchRect.x+synchRect.width) {
							int dif=(int) Math.round(tx-synchRect.x-synchRect.width)+120;
							mapPane.addOffsetX(-Math.max(dif,AUTO_SCROLL));
							scrolled=scrolled || oldOff!=mapPane.offsetX;
						}
						oldOff=mapPane.offsetY;
						if (ty<synchRect.y) {
							int dif=(int) Math.round(synchRect.y-ty)+120;
							mapPane.addOffsetY(Math.max(dif,AUTO_SCROLL));
							scrolled=scrolled || oldOff!=mapPane.offsetY;
						} else if (ty>synchRect.y+synchRect.height) {
							int dif=(int) Math.round(ty-synchRect.y-synchRect.height)+120;
							mapPane.addOffsetY(-Math.max(dif,AUTO_SCROLL));
							scrolled=scrolled || oldOff!=mapPane.offsetY;
						}
					}
				}
				if (scrolled) {
					mapPane.layers.viewSizeChanged(true);
					//Prefetch the background image if needed
//					mapPane.layers.prefetchBackImage(mapPane.layers.offscreenView.x,mapPane.layers.offscreenView.y,mapPane.layers.offscreenView.width,mapPane.layers.offscreenView.height);
					repaint(0,0,getWidth(),getHeight());
				} else {
					Rectangle rep=new Rectangle(Math.min(newx,oldx),Math.min(newy,oldy),d.width+Math.abs(newx-oldx),d.height+Math.abs(newy-oldy));
					if (getVisibleRect().intersects(rep)) {
						repaint(rep.x,rep.y,rep.width,rep.height);
					}
				}
//				RepaintManager.currentManager(this).paintDirtyRegions();
			}
		}
	}

    /**
     * Checks if the agent is inside the valid view for an agent.
     */
	private boolean agentInView(IAgent a) {
	    //Consider the agent in the view if it is contained in the centered VALID_VIEW_FRACTION size rectangle
	    synchronized (synchRect) {
		    synchRect.setRect(0,0,getWidth(),getHeight());
		    synchRect.setRect(synchRect.x+synchRect.width/VALID_VIEW_FRACTION,synchRect.y+synchRect.height/VALID_VIEW_FRACTION,(VALID_VIEW_FRACTION-2)*synchRect.width/VALID_VIEW_FRACTION,(VALID_VIEW_FRACTION-2)*synchRect.height/VALID_VIEW_FRACTION);
			AUTO_SCROLL=(int) Math.min(synchRect.width/2,synchRect.height/2);
			double tx,ty;
			synchronized (synchPixel) {
				double[] t=toPixel(a.getLongitude(),a.getLatitude());
				tx=t[0];
				ty=t[1];
			}
			if (synchRect.contains(tx,ty))
				return true;
			else
				return false;
	    }
    }
	/**
	 * Method that tells the MotionPane to repaint the agents that are always visible
	 * when a new area is shown. Called by MapPane.showMap().
	 */
	void centerViewToAgents() {
		//Not inited yet
		if (hash==null)
			return;
		Iterator it=hash.keySet().iterator();
		boolean finish=false;
		while (it.hasNext() && !finish) {
			Agent ag=(Agent) it.next();
			if (ag.isAlwaysVisible() && !agentInView(ag)) {
				Point2D.Double pt=new Point2D.Double();
                mapPane.getPositionTransform().transform(ag.getLongLat(),pt);
				mapPane.getTransform().transform(pt,pt);
				//Place the agent exactly at the center of the window
                mapPane.addOffsetX((int)Math.round(getWidth()/2-pt.x));
                mapPane.addOffsetY((int)Math.round(getHeight()/2-pt.y));
				mapPane.layers.viewSizeChanged(true);
				finish=true;
			}
		}
	}
	/**
	 * Forces the host to repaint the path of the agent. The agent may need this when it clears
	 * its trail or whenever it thinks that the host has an inconsistent trail of its.
	 */
	public void repaintTrail(gr.cti.eslate.protocol.IAgent agent) {
		Component c=((Component) hash.get(agent));
		if (c!=null)
			repaint();
	}
	/**
	 * Makes a graphic effect to show where the agent is.
	 */
	public void locateAgent(final IAgent a) {
		if (a.isEmbarked() || hash.get(a)==null)
			return;
		bringToFront(a);
		Thread t=new Thread() {
			public void run() {
				locateThreads++;
				AgentComponent ac=(AgentComponent) hash.get(a);
				for(int i=0;i<24;i++) {
					ac.setLockPhasePainting(true);
					double tx,ty;
					synchronized (synchPixel) {
						double[] t=toPixel(a.getLongitude(),a.getLatitude());
						tx=t[0];
						ty=t[1];
					}
					Dimension d=a.getFaceSize(a.getTiltFromUpright()+ac.locAngle-15);
					ac.setBounds((int) Math.round(tx-d.getWidth()/2d),(int) Math.round(ty-d.getHeight()/2d),(int) d.getWidth(),(int) d.getHeight());
					ac.rotate(-15);
					try {
						sleep(50);
					} catch(Exception e) {}
				}
				locateThreads--;
				if (locateThreads==0) {
					ac.setLockPhasePainting(false);
					ac.repaint();
				}
			}
		};
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	}
	/**
	 * Informs the host for a change in the path properties.
	 */
	public void pathPropertiesChanged(gr.cti.eslate.protocol.IAgent agent) {
		repaint();
	}
	/**
	 * @return  The map the agent will travel on this host.
	 */
	public IMapView getMap() {
		return mapPane.map;
	}
	/**
	 * @return  The layer which is considered the "road" layer, of whatever type it is.
	 */
	public ILayerView getRoadLayer() {
		if (mapPane.map==null || mapPane.map.getActiveRegionView()==null)
			return null;
		return mapPane.map.getActiveRegionView().getRoadLayer();
	}
	/**
	 * @return  The layer which is considered the "railway" layer, of whatever type it is.
	 */
	public ILayerView getRailwayLayer() {
		if (mapPane.map==null || mapPane.map.getActiveRegionView()==null)
			return null;
		return mapPane.map.getActiveRegionView().getRailwayLayer();
	}
	/**
	 * @return  The layer which is considered the "sea" layer, of whatever type it is.
	 */
	public ILayerView getSeaLayer() {
		if (mapPane.map==null || mapPane.map.getActiveRegionView()==null)
			return null;
		return mapPane.map.getActiveRegionView().getSeaLayer();
	}
	/**
	 * @return  The layer which is considered the "airway" layer, of whatever type it is.
	 */
	public ILayerView getAirwayLayer() {
		if (mapPane.map==null || mapPane.map.getActiveRegionView()==null)
			return null;
		return mapPane.map.getActiveRegionView().getAirwayLayer();
	}
	/**
	 * @return  The motion layer whith the given ID, of whatever type it is.
	 */
	public ILayerView getCustomMotionLayer(String id) {
		if (mapPane.map==null || mapPane.map.getActiveRegionView()==null)
			return null;
		return mapPane.map.getActiveRegionView().getCustomMotionLayer(id);
	}
	/**
	 * Informs the host that the agent embarked another agent.
	 */
	public void embarkedAgent(IAgent hostAgent,IAgent embarkedAgent) {
		((AgentComponent) hash.get(embarkedAgent)).clearMayEmbarkList();
		removeAgent(embarkedAgent);
		//Remove the embarked agent from all the neighboring agents that have it in the "embark" list
		Iterator it=hash.values().iterator();
		while (it.hasNext())
			((AgentComponent) it.next()).setMayEmbark(embarkedAgent,false);
		//Show the embarkation locally
		((AgentComponent) hash.get(hostAgent)).embark(embarkedAgent);
		repaint();
	}
	/**
	 * Informs the host that the agent disembarked an agent.
	 */
	public void disembarkedAgent(IAgent hostAgent,IAgent disembarkedAgent) {
		try {
			addAgent(disembarkedAgent);
			bringToFront(disembarkedAgent);
			checkSpriteCollisions(disembarkedAgent);
		} catch(AgentNotAddedException e) {
			System.err.println("MAPVIEWER#200006141656: Could not disembark agent "+disembarkedAgent.getName()+" to map.");
		}
		((AgentComponent) hash.get(hostAgent)).disembark(disembarkedAgent);
		repaint();
	}
	/**
	 * The coordinate system. May be polar or cartesian.
	 * @return  One of IRegionView.COORDINATE_CARTESIAN,IRegionView.COORDINATE_TERRESTRIAL.
	 */
	public int getCoordinateSystem() {
		return mapPane.map.getActiveRegionView().getCoordinateSystem();
	}
	/**
	 * Returns a handle to the agent named <code>agentName</code>. This method is useful when an object
	 * wants to get a handle to an agent hosted by this host.
	 */
	public IAgent getAgent(String agentName) {
		Iterator it=hash.keySet().iterator();
		while (it.hasNext()) {
			IAgent a=(IAgent) it.next();
			if (a.getName().equalsIgnoreCase(agentName))
				return a;
		}
		return null;
	}

	/**
	 * Returns a handle to the Component that represents the agent named <code>agentName</code>.
	 * This method is useful when an object wants to get a handle to the component of an agent
	 * hosted by this host.
	 */
	public Component getAgentComponent(String agentName) {
		Iterator it=hash.keySet().iterator();
		while (it.hasNext()) {
			IAgent a=(IAgent) it.next();
			if (a.getName().equalsIgnoreCase(agentName))
				return (Component) hash.get(a);
		}
		return null;
	}

	private void checkSpriteCollisions(IAgent a) {
		//Check if there is any sprite collision, only when the agent is not embarked.
		AgentComponent ac=(AgentComponent) hash.get(a);
		Iterator it=hash.keySet().iterator();
		while (it.hasNext()) {
			IAgent ag=(IAgent) it.next();
			AgentComponent c=(AgentComponent) hash.get(ag);

			if (a!=ag) {
				if (collide(ac,c)) {
					if (a.canEmbarkOn(ag))
						ac.setMayEmbark(ag,true);
					if (ag.canEmbarkOn(a))
						c.setMayEmbark(a,true);
					//Lazily create the agent collision tracker object
					if (agentCollision==null)
						agentCollision=new AgentCollision();
					//If the agents must be informed for the collision
					if (!agentCollision.hasPair(a,ag)) {
						agentCollision.add(a,ag);
						a.agentMetWithAgent(ag);
						ag.agentMetWithAgent(a);
					}
				} else {
					//When no collision has occured, tracker object is null
					if (agentCollision!=null)
						agentCollision.remove(a,ag);
					ac.setMayEmbark(ag,false);
					c.setMayEmbark(a,false);
				}
			}
		}
	}
	/**
	 * Makes the transformation from the real coordinate space to the pixel space.
	 */
	private double[] toPixel(double x,double y) {
		synchPixel[0]=x;
		synchPixel[1]=y;
		mapPane.getPositionTransform().transform(synchPixel,0,synchPixel,0,1);
		mapPane.getTransform().transform(synchPixel,0,synchPixel,0,1);
		return synchPixel;
	}
	/**
	 * Makes the transformation from the pixel coordinate space to the real space.
	 */
	private double[] toReal(double x,double y) {
		synchReal[0]=x;
		synchReal[1]=y;
		mapPane.getInverseTransform().transform(synchReal,0,synchReal,0,1);
		mapPane.getInversePositionTransform().transform(synchReal,0,synchReal,0,1);
		return synchReal;
	}

	public void addAgent(final IAgent agent) throws AgentNotAddedException {
		if (agent==null)
			return;
		//Check where the agent wants to sit.
		ILayerView layer=null;
		switch (agent.travelsOn()) {
			case IAgent.TRAVELS_ON_ROADS:
				layer=mapPane.map.getActiveRegionView().getRoadLayer();
				break;
			case IAgent.TRAVELS_ON_RAILWAYS:
				layer=mapPane.map.getActiveRegionView().getRailwayLayer();
				break;
			case IAgent.TRAVELS_ON_SEA:
				layer=mapPane.map.getActiveRegionView().getSeaLayer();
				break;
			case IAgent.TRAVELS_ON_AIR:
				layer=mapPane.map.getActiveRegionView().getAirwayLayer();
				break;
			case IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER:
				layer=mapPane.map.getActiveRegionView().getCustomMotionLayer(agent.getTravellingOnMotionLayerID());
				break;
		}
		//If the map doesn't have the requested layer, reject the agent if the agent is not positioned.
		//If the agent is positioned, ask to insert.
		if (layer==null && agent.travelsOn()!=IAgent.TRAVELS_EVERYWHERE) {
			if (agent.isPositioned()) {
				int ask=JOptionPane.showConfirmDialog(SwingUtilities.getAncestorOfClass(Frame.class,this),MapViewer.messagesBundle.getString("askpositionagent"),agent.getName(),JOptionPane.YES_NO_OPTION);
				if (ask==JOptionPane.NO_OPTION)
					throw new AgentNotAddedException("The map doesn't have the requested by the agent motion layer.",false);
			} else
				throw new AgentNotAddedException("The map doesn't have the requested by the agent motion layer.",true);
		}

		if (agent.isPositioned()) {
			AgentComponent c=new AgentComponent(agent);
			hash.put(agent,c);
			visible.put(agent,new Boolean(true));
			add(c);
			repaintAgent(agent);
			//Inform the viewer for the successful addition without use of position listeners
			pending=false;
			mapPane.viewer.pendingAgentAdded();
		} else {
			//If the agent is not positioned, ask to position first.
			keepNormalState();
			if (mapPane.viewer.getShowAgentPositioningInfoDialog())
				JOptionPane.showMessageDialog(SwingUtilities.getAncestorOfClass(Frame.class,this),MapViewer.messagesBundle.getString("agentnotpositioned"),agent.getName(),JOptionPane.INFORMATION_MESSAGE);
			if (layer==null) {
			//The agent sits everywhere
				mapPane.setCursor(poscursor);
				pending=true;
				pml=new EverywhereAdapter(agent);
				//Remove all listeners first to let Positioning listener take control.
				if (ml!=null) {
					for (int i=ml.size()-1;i>-1;i--)
						removeMouseListener((MouseListener)ml.get(i));
					for (int i=ml.size()-1;i>-1;i--)
						removeMouseMotionListener((MouseMotionListener)ml.get(i));
				}
				addMouseListener(pml);
			} else {
			//The agent sits on specific map locations
				pending=true;
				pml=new LayerAdapter(agent,layer);
				//Remove all listeners first to let Positioning listener take control.
				if (ml!=null) {
					for (int i=ml.size()-1;i>-1;i--)
						removeMouseListener((MouseListener)ml.get(i));
					for (int i=ml.size()-1;i>-1;i--)
						removeMouseMotionListener((MouseMotionListener)ml.get(i));
				}
				addMouseListener(pml);
				addMouseMotionListener(pml);
			}
		}

		if (mapPane.getLegend()!=null)
			mapPane.getLegend().addPath(agent);
		//To show the path
		repaint();
	}
	/**
	 * This and restoreNormalState keep the state of the tools while the component is
	 * in the mode of agent positioning, waiting for a click by the user.
	 */
	void keepNormalState() {
		Component[] c=mapPane.viewer.getToolBar().getComponents();
		toolState=new boolean[c.length];
		for (int i=0;i<c.length;i++) {
			toolState[i]=c[i].isEnabled();
			c[i].setEnabled(false);
		}
	}
	/**
	 * This and keepNormalState keep the state of the tools while the component is
	 * in the mode of agent positioning, waiting for a click by the user. It also
	 * finalizes the positioning state.
	 */
	void restoreNormalState() {
		Component[] c=mapPane.viewer.getToolBar().getComponents();
		for (int i=0;i<c.length;i++)
			c[i].setEnabled(toolState[i]);
		pending=false;
		mapPane.viewer.pendingAgentAdded();
	}
	/**
	 * Indicates that an agent positiong mode is pending.
	 */
	boolean hasPendingAgent() {
		return pending;
	}

	/**
	 * Brings to front an agent.
	 * @param agent
	 * @return True, if a different agent has been brought to front, false if the agent in front is the same.
	 */
	boolean bringToFront(IAgent agent) {
		Component ac=(Component) hash.get(agent);
		//The component is already in front. Avoid this annoying method.
		if (ac.equals(getComponents()[0]))
			return false;
		removeAll();
		add(ac);
		Iterator it=hash.values().iterator();
		Component c;
		while (it.hasNext())
			if (!(c=(Component) it.next()).equals(ac))
				add(c);
		return true;
	}

	/**
	 * Sets the agent that should accept navigation commands from the navigation tool.
	 * @param agent The agent that should accept commands.
	 */
	void setActiveAgent(IAgent agent) {
		if (activeAgent==agent)
			return;
		IAgent old=activeAgent;
		activeAgent=agent;
		repaintAgent(activeAgent);
		if (old!=null)
			repaintAgent(old);
	}
	/**
	 * Tells which agent accepts navigation commands from the navigation tool.
	 * @return  The agent.
	 */
	IAgent getActiveAgent() {
		if (activeAgent==null || (!hash.keySet().contains(activeAgent)))
			activeAgent=((AgentComponent) getComponents()[0]).agent;
		return activeAgent;
	}

	public void removeAgent(IAgent agent) {
		if (hash!=null) {
			Component c=(Component) hash.remove(agent);
			if (c==null)
				return;
			visible.remove(agent);
			remove(c);
			repaint();
		}
	}

	boolean hasPathVisible(IAgent a) {
		return ((Boolean) visible.get(a)).booleanValue();
	}

	void setPathVisible(IAgent a,boolean v) {
		visible.put(a,new Boolean(v));
		repaint();
	}

	Iterator getConnectedAgents() {
		if (!initialized)
			return null;
		return visible.keySet().iterator();
	}

	int getConnectedAgentsCount() {
		if (!initialized)
			return 0;
		return hash.size();
	}

	void moveActiveTo(int pX,int pY) {
		if (getConnectedAgentsCount()>0) {
			double tx,ty;
			synchronized (synchReal) {
				double[] t=toReal(pX,pY);
				tx=t[0];
				ty=t[1];
			}
			getActiveAgent().stop();
			getActiveAgent().goTo(this,tx,ty);
		}
	}

	/**
	 * Coordinate units per meter.
	 */
	public double getUnitsPerMeter() {
		return mapPane.map.getActiveRegionView().getUnitsPerMeter();
	}
	/**
	 * Meters per pixel.
	 */
	public double getMetersPerPixel() {
		Rectangle2D.Double boundRect=(Rectangle2D.Double) mapPane.map.getActiveRegionView().getBoundingRect();
		if (boundRect!=null)
			//Calculate it in the quarter of the line that divides the pane in two horizontaly.
			//This is to avoid measuring a distance greater than half the earth which makes the algorithm fail.
			return mapPane.map.getActiveRegionView().measureDistance(boundRect.getX(),boundRect.getY()+boundRect.getHeight()/2,boundRect.getX()+boundRect.getWidth()/4,boundRect.getY()+boundRect.getHeight()/2)/(mapPane.getLayersPane().getOriginalWidth()/4);
		else
			return 0;
	}
	/**
	 *
	 */
	public double getZoom() {
		return mapPane.getZoom();
	}
	/**
	 * Calculates a distance between two given points.
	 */
	public double calculateDistance(double long1,double lat1,double long2,double lat2) {
		return mapPane.map.getActiveRegionView().measureDistance(long1,lat1,long2,lat2);
	}
	/**
	 * @return  If agent <code>agent</code> can embark on the agent named <code>agentName</code>,
	 * the function returns a handle to agent <code>agentName</code>. Otherwise it returns <code>null</code>.
	 */
	public IAgent agentCanEmbarkOn(IAgent agent,String agentName) {
		return ((AgentComponent) hash.get(agent)).mayEmbark(agentName);
	}
	/**
	 * Known method.
	 */
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			getComponents()[i].setFont(f);
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
		if (layer!=null) {
	        //If motion on a layer
            if (layer instanceof IPolyLineLayerView) {
                IPolyLine.LineAspects la=null;
                double[] p=null;
                if (preference!=null && !((Heading.Line)preference).invalid) {
                    p=((Heading.Line)preference).line.findPointFromDistance(((Heading.Line)preference).getSegment(),start.getX(),start.getY(),((Heading.Line)preference).ascending,meters,getUnitsPerMeter());
                    if (p[3]==0) {
	                    if (preference!=null) {
		                    if (continueAsFar)
			                    preference.invalid=true;
                            else {
                                //Check if there is only one choice to go and follow it
			                    double tolerance=3*mapPane.PIXEL_TOLERANCE*mapPane.getInversePositionTransform().getScaleX();
			                    final ArrayList lines=layer.getGeographicObjectsAt(start.getX(),start.getY(),tolerance);
			                    boolean canContinue=false;
				                if (lines.size()!=0) {
									//Find the lines that have common points with the currently on line, nearby the current position.
									Heading.Line co=(Heading.Line) preference;
									//Exclude the lines that do not have nearby common points with the currently on.
									Point2D p0;
					                if (co.getSegment()==0 && !co.ascending)
										p0=co.line.getPoint(0);
									else if (co.getSegment()==co.line.getNumberOfPoints()-1) {
										p0=co.line.getPoint(co.line.getNumberOfPoints()-1);
									} else
										p0=co.line.getPoint(co.getSegment()+(co.ascending?1:-1));
									for (int i=lines.size()-1;i>-1;i--) {
										int w=((IPolyLine)lines.get(i)).hasPoint(p0);
										//The agent can continue only if the next line is from the start or the end and not in the middle
										if (w!=0 && w!=((IPolyLine)lines.get(i)).getNumberOfPoints()-1)
											lines.remove(i);
									}
					                if (lines.size()==2)
						                canContinue=true;
				                }
			                    if (canContinue)
			                        preference.invalid=true;
			                    else {
									motrep.distance=0;
									motrep.longt=start.getX();
									motrep.lat=start.getY();
									motrep.heading=null;
									return motrep;
			                    }
		                    }
	                    }
                    }
                }
	            //Change the line we are moving on to
                if (preference==null || preference.invalid) {
                    preference=findClosestLineForHeading((IPolyLineLayerView) layer,start,heading,preference);
                    if (preference!=null) {
                        la=((Heading.Line)preference).line.calculateDistanceAndSnap(start);
	                    //If the line is too far stop and wait input by the user
                        double tolerance=3*mapPane.PIXEL_TOLERANCE*mapPane.getInversePositionTransform().getScaleX();
                        if (la.distance<tolerance) {
                            Heading.Line hl=(Heading.Line) preference;
	                        //Serve road loops
							PolyLine line=(PolyLine)hl.line;
							if (line.getPoint(0).equals(line.getPoint(line.getNumberOfPoints()-1))) {
								if (!hl.ascending && la.segment==0) {
									la.segment=line.getNumberOfPoints();
								} else if (hl.ascending && la.segment==line.getNumberOfPoints()) {
									la.segment=0;
								}
							}
                            p=hl.line.findPointFromDistance(la.segment,start.getX(),start.getY(),hl.ascending,meters,getUnitsPerMeter());
                        }
                    }
                }
                if (preference!=null && p!=null) {
					motrep.distance=p[3];
					motrep.longt=p[0];
					motrep.lat=p[1];
					((Heading.Line)preference).setSegment((int)p[2]);
					motrep.heading=preference;
					return motrep;
                }
            } else if (layer instanceof IPolygonLayerView) {
	            //Moving inside a polygon
	            //ToDo: Implement.
            } else
	            throw new RuntimeException("Unsupported layer type!");
        } else {
	        //If free motion, not on a layer
	        //ToDo: Implement.
        }
		motrep.distance=0;
		motrep.longt=start.getX();
		motrep.lat=start.getY();
		motrep.heading=null;
		return motrep;
	}

	/**
	 * Finds a line which is suitable for the given location and heading to move on to.
	 */
    private Heading.Line findClosestLineForHeading(IPolyLineLayerView layer,Point2D start,double heading,Heading currentOn) {
	    double tolerance=3*mapPane.getInversePositionTransform().getScaleX();
	    //Find a line to move onto. It will be passed back to the caller.
	    final ArrayList lines=layer.getGeographicObjectsAt(start.getX(),start.getY(),tolerance);
		if (lines.size()==0)
		    return null;
        Heading.Line phead=new Heading.Line();
        //Find the lines that have common points with the currently on line, nearby the current position.
        if (currentOn!=null) {
            Heading.Line co=(Heading.Line) currentOn;
	        //Exclude the lines that do not have nearby common points with the currently on.
	        Point2D p0;
            if (co.getSegment()==0 && !co.ascending)
		        p0=co.line.getPoint(0);
	        else if (co.getSegment()==co.line.getNumberOfPoints()-1) {
		        p0=co.line.getPoint(co.line.getNumberOfPoints()-1);
	        } else
		        p0=co.line.getPoint(co.getSegment()+(co.ascending?1:-1));
	        IntBaseArray lineSegments=new IntBaseArray();
	        for (int i=lines.size()-1;i>-1;i--) {
		        int idx=-1;
		        if ((idx=((IPolyLine)lines.get(i)).hasPoint(p0))<0)
			        lines.remove(i);
		        else
			        lineSegments.pushFront(idx);
	        }
heading=normAngle(heading+90);
            double closest=Double.MAX_VALUE;
System.out.println("Cands "+lines.size());
            for (int i=0;i<lines.size();i++) {
System.out.println("Line "+lines.get(i));
	            synchronized (synchDiff) {
		            boolean isOnEdge=Math.abs(start.getX()-((IPolyLine)lines.get(i)).getPoint(lineSegments.get(i)).getX())<1E-5 &&
		                                Math.abs(start.getY()-((IPolyLine)lines.get(i)).getPoint(lineSegments.get(i)).getY())<1E-5;
					double[] diff=calculateDifferenceInHeading((IPolyLine)lines.get(i),lineSegments.get(i),heading,isOnEdge);
System.out.println("\t"+diff[0]);
					if (closest-diff[0]>1E-6) {
						closest=diff[0];
						phead.line=(IPolyLine) lines.get(i);
						phead.ascending=diff[1]>0;
						phead.invalid=false;
						phead.setSegment(lineSegments.get(i)+(phead.ascending?0:1));
System.out.println("Case 1");
					} else if (Math.abs(closest-diff[0])<1E-6) {
System.out.println("\tEquality");
						//Look the bounding box
						Rectangle2D r1=phead.line.getBounds2D();
						Rectangle2D r2=((PolyLine)lines.get(i)).getBounds2D();
						double angle1;
						if (start.getX()==r1.getCenterX()) {
							if (start.getY()<r1.getCenterY())
								angle1=90;
							else
								angle1=180;
						} else {
							if (start.getX()<r1.getCenterX())
								angle1=Math.atan((start.getY()-r1.getCenterY())/(start.getX()-r1.getCenterX()))*180/Math.PI;
							else
								angle1=normAngle(Math.atan((start.getY()-r1.getCenterY())/(start.getX()-r1.getCenterX()))*180/Math.PI+180);
						}
						double angle2;
						if (start.getX()==r2.getCenterX()) {
							if (start.getY()<r2.getCenterY())
								angle2=90;
							else
								angle2=180;
						} else {
							if (start.getX()<r2.getCenterX())
								angle2=Math.atan((start.getY()-r2.getCenterY())/(start.getX()-r2.getCenterX()))*180/Math.PI;
							else
								angle2=normAngle(Math.atan((start.getY()-r2.getCenterY())/(start.getX()-r2.getCenterX()))*180/Math.PI+180);
						}
						double diff1=Math.min(normAngle(angle1-heading),normAngle(angle1+180-heading));
						double diff2=Math.min(normAngle(angle2-heading),normAngle(angle2+180-heading));
System.out.println("Heading "+heading);
System.out.println("\tR1 "+r1.getCenterX()+","+r1.getCenterY());
System.out.println("\tR2 "+r2.getCenterX()+","+r2.getCenterY());
System.out.println("Dif "+diff1+" "+diff2);
						if (diff1-diff2>1E-6) {
							closest=diff[0];
							phead.line=(IPolyLine)lines.get(i);
							phead.ascending=diff[1]>0;
							phead.invalid=false;
//							phead.setSegment(la.segment);
							phead.setSegment(lineSegments.get(i)+(phead.ascending?0:1));
System.out.println("Case 2");
						} else {
							//Look the distance from the line
System.out.println("\tTough "+phead.line.calculateDistance(start)+" "+((IPolyLine)lines.get(i)).calculateDistance(start));
	                        if (phead.line.calculateDistance(start)>((IPolyLine)lines.get(i)).calculateDistance(start)) {
		                        closest=diff[0];
		                        phead.line=(IPolyLine)lines.get(i);
		                        phead.ascending=diff[1]>0;
		                        phead.invalid=false;
//		                        phead.setSegment(la.segment);
		                        phead.setSegment(lineSegments.get(i)+(phead.ascending?0:1));
System.out.println("Case 3");
	                        }
						}
					}

	            }
            }
        } else {
            //Find which line is closer to the requested heading.
            double closest=Double.MAX_VALUE;
            for (int i=0;i<lines.size();i++) {
                IPolyLine.LineAspects la=new IPolyLine.LineAspects(((PolyLine)lines.get(i)).calculateDistanceAndSnap(start));
	            synchronized (synchDiff) {
					double[] diff=calculateDifferenceInHeading(((IPolyLine)lines.get(i)).getPoint(la.segment),((IPolyLine)lines.get(i)).getPoint(la.segment+1),heading,0);
					if (closest>diff[0]) {
						closest=diff[0];
						phead.line=(IPolyLine)lines.get(i);
						phead.ascending=diff[1]>0;
						phead.invalid=false;
						phead.setSegment(la.segment);
					}
	            }
            }
        }
        if (phead.line==null) {
            if (currentOn!=null) {
				synchronized (synchDiff) {
					boolean isOnEdge=Math.abs(start.getX()-((Heading.Line)currentOn).line.getPoint(((Heading.Line)currentOn).getSegment()).getX())<1E-5 &&
										Math.abs(start.getY()-((Heading.Line)currentOn).line.getPoint(((Heading.Line)currentOn).getSegment()).getY())<1E-5;
					((Heading.Line)currentOn).ascending=calculateDifferenceInHeading(((Heading.Line)currentOn).line,((Heading.Line)currentOn).getSegment(),heading,isOnEdge)[1]>0;
                    ((Heading.Line)currentOn).invalid=false;
				}
            }
            return (Heading.Line) currentOn;
        } else {
	        //layer.setSelection((GeographicObject)phead.line);
	        return phead;
		}
    }

    /**
     * Calculates the difference in heading. Positive in position 1 means from p0 to p1, negative from p1 to p0;
     */
	private double[] calculateDifferenceInHeading(Point2D p0,Point2D p1,double heading,double mustGo) {
        double zero=1E-8;
        double x0=p0.getX(); double y0=p0.getY(); double x1=p1.getX(); double y1=p1.getY();
	    double a;
	    if (Math.abs(x0-x1)>zero) {
		    //The segment is a function. Find its heading
		    a=normAngle(Math.atan((y0-y1)/(x0-x1))*180/Math.PI);
            //The 2 possible angles
            double a1=tiltAngle(a);
            double a2=tiltAngle(normAngle(a+180));
            //The difference between the heading and the 2 angles
            double dif1=Math.min(Math.abs(heading-a1),Math.abs(heading+360-a1));
            double dif2=Math.min(Math.abs(heading-a2),Math.abs(heading+360-a2));
            //Less difference
            double minAngle=Math.min(a1,a2);
            synchDiff[0]=Math.min(dif1,dif2);
            //Difference from the minimum angle, which is always inside [0,180).
            double difMinAngle=Math.min(Math.abs(heading-minAngle),Math.abs(heading+360-minAngle));
            if (difMinAngle>180)
                difMinAngle=360-difMinAngle;
            //Ascending or descending motion
            if (difMinAngle<90) {
                //If the diference is an oblique angle
                if (x0<x1)
                    synchDiff[1]=DESCENDING;
                else
                    synchDiff[1]=ASCENDING;
            } else {
                //If the diference is an obtuse angle
                if (x0<x1)
                    synchDiff[1]=ASCENDING;
                else
                    synchDiff[1]=DESCENDING;
            }
	    } else {
		    //The segment is not a function. Heading is upright.
            synchDiff[0]=Math.min(
                    heading,
                    Math.min(Math.abs(heading-180),Math.abs(heading+180))
            );

            if (heading<90 || heading>=270) {
                if (y0>y1)
                    synchDiff[1]=DESCENDING;
                else
                    synchDiff[1]=ASCENDING;
            } else {
                if (y0>y1)
                    synchDiff[1]=ASCENDING;
                else
                    synchDiff[1]=DESCENDING;
			}
	    }
	    if ((mustGo==ASCENDING && synchDiff[1]==DESCENDING) || (mustGo==DESCENDING && synchDiff[1]==ASCENDING)) {
	        synchDiff[0]=normAngle(synchDiff[0]+180);
		    synchDiff[1]=-1*synchDiff[1];
		}
	    return synchDiff;
	}

    /**
     * Calculates the difference in heading. Positive in position 1 means from p0 to p1, negative from p1 to p0;
     * It looks the line from the given segment ascending and descending.
     */
	private double[] calculateDifferenceInHeading(IPolyLine line,int segment,double heading,boolean isOnEdge) {
        if (segment==0) {
	        double[] d=calculateDifferenceInHeading(line.getPoint(0),line.getPoint(1),heading,ASCENDING);
	        //This means that the agent has to reverse motion
	        if (!isOnEdge && d[0]>180) {
	            d[0]-=180;
		        d[1]=DESCENDING;
	        } else
                d[1]=ASCENDING;
            return d;
        } else if (segment==line.getNumberOfPoints()-1) {
            double[] d=calculateDifferenceInHeading(line.getPoint(line.getNumberOfPoints()-2),line.getPoint(line.getNumberOfPoints()-1),heading,DESCENDING);
	        //This means that the agent has to reverse motion
	        if (!isOnEdge && d[0]>180) {
	            d[0]-=180;
		        d[1]=ASCENDING;
	        } else
	            d[1]=DESCENDING;
	        return d;
        } /*else if (segment==line.getNumberOfPoints()-2) {
            double[] d=calculateDifferenceInHeading(line.getPoint(line.getNumberOfPoints()-2),line.getPoint(line.getNumberOfPoints()-1),heading,DESCENDING);
	        //This means that the agent has to reverse motion
	        if (!isOnEdge && d[0]>180) {
	            d[0]-=180;
		        d[1]=ASCENDING;
	        } else
	            d[1]=DESCENDING;
	        return d;
        } */else {
            double[] tmp=calculateDifferenceInHeading(line.getPoint(segment),line.getPoint(segment+1),heading,0);
            double d1=tmp[0];
            double ad=tmp[1];
            tmp=calculateDifferenceInHeading(line.getPoint(segment-1),line.getPoint(segment),heading,0);
            if (d1>=tmp[0]) {
                return tmp;
            } else {
                synchDiff[0]=d1;
                synchDiff[1]=ad;
                return synchDiff;
            }
        }
	}

    /**
     * Converts an angle in degrees to agent-tilt angle in [0,360).
     */
    private double tiltAngle(double angle) {
        angle=angle-90;
        return normAngle(angle);
    }

    private boolean valid(double angle) {
        if (angle>=0 && angle<360)
            return true;
        return false;
    }

    /**
     * Normalizes the given angle (in degrees) in the interval [0,360).
     */
    private double normAngle(double angle) {
        if (!valid(angle)) {
            if (angle<0)
                for (;!valid(angle);)
                    angle+=360;
            else
                for (;!valid(angle);)
                    angle-=360;
        }
        return angle;
    }

	public void paintComponent(Graphics g) {
		if (!initialized)
			return;
		Graphics2D g2=(Graphics2D) g;
		AffineTransform orgTrans=new AffineTransform(g2.getTransform());
		//If the current view is smaller than the component view, clipping must occur
		/*if (mapPane.layers.currentView.width!=getWidth() && mapPane.layers.currentView.height!=getHeight()) {
			clipRect.x=(int) Math.round(mapPane.getTransform().getTranslateX());
			clipRect.y=(int) Math.round(mapPane.getTransform().getTranslateY());
			clipRect.width=mapPane.layers.currentView.width;
			clipRect.height=mapPane.layers.currentView.height;
			g2.setClip(clipRect);
		}*/
		g2.setRenderingHints(mapPane.getRenderingHints());
		Iterator it=hash.keySet().iterator();
		gr.cti.eslate.agent.Path path;
		IAgent a; Dimension d;
		BasicStroke bstroke=new BasicStroke();
		while (it.hasNext()) {
			a=(IAgent) it.next();
			path=a.getPath();
			//Position the agent on the screen
			//except when it spins!
			double tx,ty;
			synchronized (synchPixel) {
				double[] t=toPixel(a.getLongitude(),a.getLatitude());
				tx=t[0];
				ty=t[1];
			}
			if (locateThreads==0) {
				d=a.getFaceSize();
				AgentComponent cmp=((AgentComponent) hash.get(a));
				cmp.setBounds((int) Math.round(tx-d.getWidth()/2d),(int) Math.round(ty-d.getHeight()/2d),(int) d.getWidth(),(int) d.getHeight());
				g2.setTransform(new AffineTransform(orgTrans));
				g2.translate(cmp.getX(),cmp.getY());
				a.paintFace(g,a.getTiltFromUpright()+mapPane.map.getActiveRegionView().getOrientation(),cmp.lastPaintedAnimationFrame);
			}
			if (hasPathVisible(a)) {
				g2.setTransform(new AffineTransform(orgTrans));
				g2.transform(mapPane.getTransform());
				//Paint its path
				for (int i=0;i<path.size();i++) {
					g2.setPaint(path.get(i).getPaint());
					g2.setStroke(path.get(i).getStroke());
					g2.draw(mapPane.getPositionTransform().createTransformedShape(path.get(i)));
				}
				g2.setTransform(new AffineTransform(orgTrans));
			}

			//Paint a cross
			if (mapPane.viewer.getPaintCrossOnAgent()) {
				g2.setStroke(bstroke);
				g2.drawLine((int) Math.round(tx),0,(int) Math.round(tx),getHeight());
				g2.drawLine(0,(int) Math.round(ty),getWidth(),(int) Math.round(ty));
			}
		}
//		super.paintComponent(g2);
	}

	/**
	 * Mouse Input Adapter to place the agent anywhere on the map.
	 */
	private class EverywhereAdapter extends MouseInputAdapter {
		private IAgent agent;
		EverywhereAdapter(IAgent agent) {
			this.agent=agent;
		}
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount()>1) return;
			AgentComponent c=new AgentComponent(agent);
			hash.put(agent,c);
			visible.put(agent,new Boolean(true));
			add(c);
			bringToFront(agent);
			try {
				double tx,ty;
				synchronized (synchReal) {
					double[] t=toReal(e.getX(),e.getY());
					tx=t[0];
					ty=t[1];
				}
				agent.setLongLat(tx,ty);
			} catch(AgentRefusesToChangePositionException ex) {
				remove(c);
				IProtocolPlug[] pins=mapPane.viewer.agentPlug.getProtocolPlugs();
				for (int i=0;i<pins.length;i++)
					if (pins[i].getProtocolImplementor().equals(agent)) {
						try {
							mapPane.viewer.agentPlug.disconnectPlug((Plug) pins[i]);
						} catch(Exception ex2) {
							System.err.println("MAPVIEWER#200005291550: Cannot disconnect agent pin.");
						}
						break;
					}
				JOptionPane.showMessageDialog(SwingUtilities.getAncestorOfClass(Frame.class,MotionPane.this),MapViewer.messagesBundle.getString("agentnotconnected"),agent.getName(),JOptionPane.ERROR_MESSAGE);
			}
			removeMouseListener(this);
			//Add all listeners again.
			if (ml!=null) {
				for (int i=0;i<ml.size();i++)
					addMouseListener((MouseListener)ml.get(i));
				for (int i=0;i<ml.size();i++)
					addMouseMotionListener((MouseMotionListener)ml.get(i));
			}
			pml=null;
			mapPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			restoreNormalState();
		}
	};
	/**
	 * Mouse Input Adapter to place the agent on specific locations on the map.
	 */
	private class LayerAdapter extends MouseInputAdapter {
		private IAgent agent;
		private ILayerView layer;
		private double snapX,snapY;
        private GeographicObject geoobj;
        private int seg;
		LayerAdapter(IAgent agent,ILayerView layer) {
			this.agent=agent;
			this.layer=layer;
		}
		public void mouseMoved(MouseEvent e) {
			double tx,ty;
			synchronized (synchReal) {
				double[] t=toReal(e.getX(),e.getY());
				tx=t[0];
				ty=t[1];
			}
			double tol=mapPane.PIXEL_TOLERANCE*mapPane.getInversePositionTransform().getScaleX();
			ArrayList found=layer.getGeographicObjects(new Rectangle2D.Double(tx-tol,ty-tol,2*tol,2*tol),true);
			mapPane.setCursor(nocursor);

			if (found.size()!=0) {
				if (layer instanceof IPolyLineLayerView) {
					double dist=Double.MAX_VALUE;
					double pX=tx;
					double pY=ty;
					geoobj=null;
					Point2D pp=new Point2D.Double(pX,pY);
					for (int i=0;i<found.size();i++) {
						IPolyLine.LineAspects la=((IPolyLine) found.get(i)).calculateDistanceAndSnap(pp);
						if (la.distance<dist && Math.abs(la.distance)<=tol) {
							snapX=la.snapX;
							snapY=la.snapY;
							dist=la.distance;
                            seg=la.segment;
							geoobj=(IPolyLine) found.get(i);
							mapPane.setCursor(poscursor);
						}
					}
				} else {
					//ToDo: #### geoobj is not set.
					mapPane.setCursor(poscursor);
					synchronized (synchReal) {
						double[] t=toReal(e.getX(),e.getY());
						tx=t[0];
						ty=t[1];
					}
					snapX=tx;
					snapY=ty;
				}
			}
		}
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount()>1 || geoobj==null) return;
			AgentComponent c=new AgentComponent(agent);
			hash.put(agent,c);
			visible.put(agent,new Boolean(true));
			add(c);
			bringToFront(agent);
			try {
				agent.setLongLat(snapX,snapY);
                Heading.Line phead=new Heading.Line();
                phead.line=(IPolyLine) geoobj;
                phead.ascending=true;
                phead.invalid=false;
                phead.setSegment(seg);
                agent.setLastTravelledOnObject(phead);
			} catch(AgentRefusesToChangePositionException ex) {
				System.err.println(ex.getMessage());
				remove(c);
				IProtocolPlug[] pins=mapPane.viewer.agentPlug.getProtocolPlugs();
				for (int i=0;i<pins.length;i++)
					if ((pins[i].getProtocolImplementor().equals(agent))) {
						try {
							mapPane.viewer.agentPlug.disconnectPlug((Plug) pins[i]);
						} catch(Exception ex2) {
							System.err.println("MAPVIEWER#200005291550: Cannot disconnect agent pin.");
						}
						break;
					}
				JOptionPane.showMessageDialog(SwingUtilities.getAncestorOfClass(Frame.class,MotionPane.this),MapViewer.messagesBundle.getString("agentnotconnected"),agent.getName(),JOptionPane.ERROR_MESSAGE);
			}
			removeMouseListener(this);
			removeMouseMotionListener(this);
			//Add all listeners again.
			if (ml!=null) {
				for (int i=0;i<ml.size();i++)
					addMouseListener((MouseListener)ml.get(i));
				for (int i=0;i<ml.size();i++)
					addMouseMotionListener((MouseMotionListener)ml.get(i));
			}
			pml=null;
			mapPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			restoreNormalState();
		}
	};
	/**
	 * Exception thrown when the map cannot host an agent.
	 */
	public class AgentNotAddedException extends Exception {
		public boolean showDialog;
		public AgentNotAddedException(String mes,boolean showDialog) {
			super(mes);
			this.showDialog=showDialog;
		}
	}

	/*class TransparentMouseInputListener extends MouseInputAdapter {
		private PathSegment selectedSegment;
		private IAgent agnt;
		public void mousePressed(MouseEvent e) {
			if (((mapPane.viewer.activate!=null && mapPane.viewer.activate.isSelected())  || (mapPane.viewer.browse!=null && mapPane.viewer.browse.isSelected())) && selectedSegment!=null && e.getClickCount()==1) {
				JDialog dialog=selectedSegment.getPropertiesDialog(agnt);
				dialog.setModal(true);
				dialog.setSize(370,420);
				Point p=new Point(getX(),getY());
				SwingUtilities.convertPointToScreen(p,MotionPane.this);
				dialog.setLocation(Math.max(0,p.x+(getWidth()-dialog.getWidth())/2),Math.max(0,p.y+(getHeight()-dialog.getHeight())/2));
				dialog.show();
				repaint();
			}
		}
		public void mouseMoved(MouseEvent e) {
			selectedSegment=null;
			if (initialized && ((mapPane.viewer.activate!=null && mapPane.viewer.activate.isSelected()) || (mapPane.viewer.browse!=null && mapPane.viewer.browse.isSelected()))) {
				Iterator it=hash.keySet().iterator();
				//Create a tolerance rectangle of +-PIXEL_TOLERANCE.
				double[] t1=toReal(e.getX()-mapPane.PIXEL_TOLERANCE,e.getY()-mapPane.PIXEL_TOLERANCE);
				double x=t1[0],y=t1[1];
				double[] t2=toReal(e.getX()+mapPane.PIXEL_TOLERANCE,e.getY()+mapPane.PIXEL_TOLERANCE);
				Rectangle2D.Double r=new Rectangle2D.Double(x,y,t2[0]-x,t2[1]-y);
				gr.cti.eslate.agent.Path path;
				while (it!=null && it.hasNext()) {
					agnt=(IAgent) it.next();
					path=agnt.getPath();
					for (int i=0;i<path.size();i++)
						if (path.get(i).intersects(r)) {
							mapPane.setCursor(pathcursor);
							selectedSegment=path.get(i);
							it=null;
							break;
						}
				}
			}
		}
	}*/

	//Members.
	private HashMap hash,visible;
	MapPane mapPane;
	private Cursor poscursor,nocursor;
	//Keeps a reference to the agent placing listener to remove it if the
	//pin is disconnected before positioning
	private MouseInputListener pml;
	//Listener management variables
	ArrayList ml;
	private int locateThreads;
	//Keeps the state of the viewer tools while in the agent positioning mode.
	private boolean[] toolState;
	private boolean pending;
	private boolean initialized;
	/**
	 * This agent accepts direction commands from the navigation tool.
	 */
	private IAgent activeAgent;
	/**
	 * Keeps track of agent sprite collisions.
	 */
	private AgentCollision agentCollision;
	/**
	 * Any reference to this reusable array or to the results of toPixel() must be synchronized.
	 */
	private double[] synchPixel=new double[2];
	/**
	 * Any reference to this reusable array or to the results of toReal() must be synchronized.
	 */
	private double[] synchReal=new double[2];
	/**
	 * Any reference to this reusable array or to the results of calculateDifferenceInHeading() must be synchronized.
	 */
	private double[] synchDiff=new double[2];
	/**
	 * Any reference to this reusable rectangle must be synchronized.
	 */
	private Rectangle2D.Double synchRect=new Rectangle2D.Double();

    private static final double DESCENDING=-1;
    private static final double ASCENDING=1;
	private static int AUTO_SCROLL=100;
	private static final float VALID_VIEW_FRACTION=4.5f;
}