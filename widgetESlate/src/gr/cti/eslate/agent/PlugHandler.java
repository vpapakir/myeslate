package gr.cti.eslate.agent;

import gr.cti.eslate.base.BadPlugAliasException;
import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.IProtocolPlug;
import gr.cti.eslate.base.LeftMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.MultipleInputMultipleOutputPlug;
import gr.cti.eslate.base.MultipleInputPlug;
import gr.cti.eslate.base.MultipleOutputPlug;
import gr.cti.eslate.base.NoSuchPlugException;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.ProtocolPlug;
import gr.cti.eslate.base.RightMultipleConnectionProtocolPlug;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.SingleInputPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.event.DatabaseAdapter;
import gr.cti.eslate.database.engine.event.TableRemovedEvent;
import gr.cti.eslate.mapModel.LayerAdapter;
import gr.cti.eslate.mapModel.LayerEvent;
import gr.cti.eslate.mapModel.LayerListener;
import gr.cti.eslate.mapModel.MapAdapter;
import gr.cti.eslate.mapModel.MapEvent;
import gr.cti.eslate.mapModel.MapListener;
import gr.cti.eslate.mapModel.RegionAdapter;
import gr.cti.eslate.mapModel.RegionEvent;
import gr.cti.eslate.mapModel.RegionListener;
import gr.cti.eslate.mapModel.geom.Heading;
import gr.cti.eslate.protocol.EnchancedMapListener;
import gr.cti.eslate.protocol.IAgent;
import gr.cti.eslate.protocol.IAgentHost;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IVectorLayerView;
import gr.cti.eslate.protocol.MotionFeatures;
import gr.cti.eslate.protocol.MotionReport;
import gr.cti.eslate.sharedObject.Direction;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.sharedObject.Tick;
import gr.cti.eslate.sharedObject.VectorData;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This is a helper class to clean-up the Agent class code. It just creates the Agent's plugs.
 */
class PlugHandler implements EnchancedMapListener {
	private Agent agent;
	private boolean block;
	private int lplug;
	private Object[][]layerPlugs;
	private ProtocolPlug host;
	//The plugCacheMap keeps the last activated object to save from informing again
	private HashMap plugCacheMap;
	//Many vectors may provide the resultand
	private Plug vectorIn;
	private Plug tickIn;
	private VectorData latLongSO;
	/**
	 * Point to make tranfsers between agent and hosts.
	 */
	private Point2D.Double carrier=new Point2D.Double();
    /**
     * Reusable MotionReport object.
     */
    private MotionReport motrep=new MotionReport();

	PlugHandler(Agent ag) {
		this.agent=ag;
		ESlateHandle handle=agent.getESlateHandle();
		//Variable init
		layerPlugs=new Object[10][4];
		plugCacheMap=new HashMap();
		lplug=0;
		block=false;
		//To a host
		try {
			Class protocol=Class.forName("gr.cti.eslate.protocol.IAgentHost");
			host=new RightMultipleConnectionProtocolPlug(handle,Agent.bundlePlugs,"host",Color.cyan,protocol,agent);
			host.setHostingPlug(true);
			//Make the agent listener of map events.
			host.addConnectionListener(new ConnectionListener() {
				public void handleConnectionEvent(ConnectionEvent e) {
					IMapView mv=((IAgentHost) ((ProtocolPlug)e.getPlug()).getProtocolImplementor()).getMap();
					mv.addEnchancedMapListener(PlugHandler.this);
					ILayerView[] layers=mv.getActiveRegionView().getLayerViews();
					for (int i=0;i<layers.length;i++)
						try {
							addLayerPlug(mv,layers[i]);
						} catch(Exception e1) {
							System.err.println("AGENT#200006021706:Cannot create plugs for map layers.");
							e1.printStackTrace();
						}
					if (agent.isPositioned())
						lookAround(host.getProtocolPlugs());
				}
			});
			host.addDisconnectionListener(new DisconnectionListener() {
				public void handleDisconnectionEvent(DisconnectionEvent e) {
					IMapView disconn=((IAgentHost) ((ProtocolPlug)e.getPlug()).getProtocolImplementor()).getMap();
					IProtocolPlug[] hosts=host.getProtocolPlugs();
					boolean found=false;

					//Search if the agent is connected to the same view with another viewer
					for (int i=0;i<hosts.length;i++) {
						IMapView map=((IAgentHost) hosts[i].getProtocolImplementor()).getMap();
						if (map.equals(disconn)) {
							found=true;
							break;
						}
					}
					//If not, remove all the related plugs.
					if (!found)
						for (int j=0;j<lplug;j++)
							if (layerPlugs[j][0].equals(disconn))
								try {
									removeLayerPlug((IMapView) layerPlugs[j][0],(ILayerView) layerPlugs[j][1]);
								} catch(Exception e1) {
									//Didn't remove
								}
				}
			});
			handle.addPlug(host);
		} catch (Throwable e) {
			System.err.println("AGENT#200005172131: Cannot create plug to connect to agent host. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Tick input. Provides tick for motion.
		try {
			Class soClass=Class.forName("gr.cti.eslate.sharedObject.Tick");
			SharedObjectListener sol=new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent soe) {
					agent.tickArrived(/*Convert to seconds*/(((Tick) soe.getSharedObject()).getTickLong()/1E6));
				}
			};
			tickIn=new SingleInputPlug(handle,Agent.bundlePlugs,"clock",Color.yellow,soClass,sol);
			handle.addPlug(tickIn);
		} catch (Throwable e) {
			System.err.println("AGENT#200006161444: Cannot create Tick input plug. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Vector plug. Provides absolute lat-long coordinates as input and output.
		try {
			Class soClass=Class.forName("gr.cti.eslate.sharedObject.VectorData");
			latLongSO=new VectorData(handle);
			latLongSO.setVectorData(agent.getLongitude(),agent.getLatitude());
			SharedObjectListener sol=new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent soe) {
					try {
						VectorData newData=(VectorData) soe.getSharedObject();
						agent.setLongLat(newData.getX(),newData.getY());
					} catch(AgentRefusesToChangePositionException e) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			};
			Plug vectorLatLong=new MultipleInputMultipleOutputPlug(handle,Agent.bundlePlugs,"abslatlong",Color.green,soClass,latLongSO,sol);
			handle.addPlug(vectorLatLong);
		} catch (Throwable e) {
			System.err.println("AGENT#200005312035: Cannot create Vector input plug for absolute lat-long coordinates. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Vector input. Provides direction as well as speed.
		try {
			Class soClass=Class.forName("gr.cti.eslate.sharedObject.VectorData");
			SharedObjectListener sol=new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent soe) {
					Plug[] allVectors=vectorIn.getProviders();
					//Find the resultand of all connected vectors
					double x=0; double y=0;
					for (int i=0;i<allVectors.length;i++) {
						x+=((VectorData) ((SharedObjectPlug) allVectors[i]).getSharedObject()).getX();
						y+=((VectorData) ((SharedObjectPlug) allVectors[i]).getSharedObject()).getY();
					}
					//Post the resultand
					double nv=Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
					if (agent.getVelocity()==0)
						agent.setPotentialVelocity(nv);
					else
						agent.setVelocity(nv);
					//Find the angle
					double tilt=0;
					if (x!=0)
						tilt=Math.atan(y/x)*180/Math.PI;
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

					agent.setTiltFromUpright(tilt);
				}
			};
			vectorIn=new MultipleInputPlug(handle,Agent.bundlePlugs,"vectorin",Color.green,soClass,sol);
			handle.addPlug(vectorIn);
		} catch (Throwable e) {
			System.err.println("AGENT#200005151944: Cannot create Vector input plug. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Direction input.
		try {
			Class.forName("gr.cti.eslate.sharedObject.Direction");
			SharedObjectListener sol=new SharedObjectListener() {
				public void handleSharedObjectEvent(SharedObjectEvent soe) {
					Direction d=(Direction) soe.getSharedObject();
					switch (d.getDirection()) {
						case Direction.N:
							agent.setTiltFromUpright(0);
							break;
						case Direction.NW:
							agent.setTiltFromUpright(45);
							break;
						case Direction.W:
							agent.setTiltFromUpright(90);
							break;
						case Direction.SW:
							agent.setTiltFromUpright(135);
							break;
						case Direction.S:
							agent.setTiltFromUpright(180);
							break;
						case Direction.SE:
							agent.setTiltFromUpright(225);
							break;
						case Direction.E:
							agent.setTiltFromUpright(270);
							break;
						case Direction.NE:
							agent.setTiltFromUpright(315);
							break;
					}
				}
			};
			Plug direction=new MultipleInputPlug(handle,Agent.bundlePlugs,"direction",Color.blue,gr.cti.eslate.sharedObject.Direction.class,sol);
			handle.addPlug(direction);
		} catch (Throwable e) {
			System.err.println("AGENT#200005152048: Cannot create Direction input plug. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Distance input.
		try {
			Plug distance=new LeftMultipleConnectionProtocolPlug(handle,Agent.bundlePlugs,"distance",Color.orange,null,agent);
			handle.addPlug(distance);
		} catch (Throwable e) {
			System.err.println("AGENT#200005152112: Cannot create Distance input plug. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Time input.
		try {
			Plug time=new LeftMultipleConnectionProtocolPlug(handle,Agent.bundlePlugs,"time",new Color(250,128,114),null,agent);
			handle.addPlug(time);
		} catch (Throwable e) {
			System.err.println("AGENT#200005152141: Cannot create Time input plug. Information follows. You may continue working.");
			e.printStackTrace();
		}
		//Add path listener
		agent.getPath().addPathListener(new PathListener() {
			public void segmentPropertiesChanged(PathEvent e) {
				IProtocolPlug[] hosts;
				if ((hosts=host.getProtocolPlugs()).length==0)
					return;
				for (int i=0;i<hosts.length;i++) {
					((IAgentHost) hosts[i].getProtocolImplementor()).pathPropertiesChanged(agent);
				}
			}
		});
	}

	boolean isVectorVelocityGiven() {
		if (vectorIn.hasProvidersConnected())
			return true;
		return false;
	}


	void addLayerPlug(IMapView mv,ILayerView lv) throws Exception {
		while (block) ;
		int insPoint=lplug;
		Plug plug=null;
		try {
			Class soClass=Class.forName("gr.cti.eslate.sharedObject.NumberSO");
			NumberSO so=new NumberSO(agent.getESlateHandle(),null);
			plug=new MultipleOutputPlug(agent.getESlateHandle(),null,mv.getName()+": "+Agent.bundlePlugs.getString("layerobjectof")+" \""+lv.getName()+"\"", new Color(135,206,250), soClass, so);
			plug.setNameLocaleIndependent(mv.getName()+": layerobjectof"+lv.getName());
			agent.getESlateHandle().addPlug(plug);
		} catch(Exception e) {
			//Possibly an addition of a table plug
			plug=null;
			for (int i=0;i<lplug && insPoint==lplug;i++)
				if (layerPlugs[i][0].equals(mv) && layerPlugs[i][1].equals(lv))
					insPoint=i;
		}

		TableRecordPlug tableplug=null;
		if (lv.getTable()!=null) {
			Table t=lv.getTable();
			tableplug=new TableRecordPlug(agent.getESlateHandle(),t);
			agent.getESlateHandle().addPlug(tableplug);
			//Set plug aliases
			//The aliases started being necessary when tables became components.
			ESlateMicroworld mwd=agent.getESlateHandle().getESlateMicroworld();
			try {
				//Aliasing for table plug
				mwd.setPlugAliasForLoading(t.getTablePlug(),agent.getESlateHandle(),new String[]{t.getTitle()});
				//Aliasing for current record plug
				mwd.setPlugAliasForLoading(t.getTablePlug(),agent.getESlateHandle(),new String[]{t.getTitle()});
				//Aliasing for table fields
				AbstractTableField tf;
				for (int j=0;j<t.getColumnCount();j++) {
					try {
						tf=t.getTableField(j);
                        mwd.setPlugAliasForLoading(t.getTablePlug().getRecordPlug().getPlug(tf.getName()),agent.getESlateHandle(),new String[]{tf.getName(),"record of "+t.getTitle(),t.getTitle()});
					} catch(InvalidFieldIndexException ex) {
						System.err.println("AGENT#200103231619: Field does not exist.");
					}
				}

			} catch(BadPlugAliasException ex) {
				ex.printStackTrace();
			}

			//Finish table plug addition
			mv.getDatabase().addDatabaseListener(new DatabaseAdapter() {
				public void tableRemoved(TableRemovedEvent e) {
					for (int i=0;i<lplug;i++)
						if (layerPlugs[i][3]!=null && layerPlugs[i][3].equals(e.getRemovedTable())) {
							try {
								agent.getESlateHandle().removePlug((Plug) layerPlugs[i][3]);
								layerPlugs[i][3]=null;
							} catch(NoSuchPlugException ex) {
								System.err.println("AGENT#200101151114: Could not remove table plug from agent.");
							}
						}
				}
			});
		}

		if (plug!=null) {
			layerPlugs[insPoint][0]=mv;
			layerPlugs[insPoint][1]=lv;
			layerPlugs[insPoint][2]=plug;
			lplug++;
		}
		layerPlugs[insPoint][3]=tableplug;
		//Increase the size of the array after releasing the control, not to affect normal execution
		if (lplug==layerPlugs.length) {
			block=true;
			Thread t=new Thread() {
				public void run() {
System.out.println("DDDDDDDDDDDDDDD");					
					Object[][] t=new Object[layerPlugs.length+10][4];
					System.arraycopy(layerPlugs,0,t,0,layerPlugs.length);
					layerPlugs=t;
					block=false;
				}
			};
			t.start();
		}
	}

	void removeLayerPlug(IMapView mv,ILayerView lv) throws Exception {
		while (block) ;
		//Look if the plug already exists.
		for (int i=0;i<lplug;i++)
			if (layerPlugs[i][0].equals(mv) && layerPlugs[i][1].equals(lv)) {
				agent.getESlateHandle().removePlug((Plug) layerPlugs[i][2]);
				if (layerPlugs[i][3]!=null) {
					agent.getESlateHandle().removePlug((Plug) layerPlugs[i][3]);
					((TableRecordPlug) layerPlugs[i][3]).destroy();
				}
				for (int j=i;j<lplug-1;j++)
					layerPlugs[j]=layerPlugs[j+1];
				lplug--;
				break;
			}
	}

	void removeTableRecordPlug(IMapView mv,ILayerView lv) throws Exception {
		while (block) ;
		//Look if the plug already exists.
		for (int i=0;i<lplug;i++)
			if (layerPlugs[i][0].equals(mv) && layerPlugs[i][1].equals(lv)) {
				if (layerPlugs[i][3]!=null) {
					agent.getESlateHandle().removePlug((Plug) layerPlugs[i][3]);
					((TableRecordPlug) layerPlugs[i][3]).destroy();
				}
				break;
			}
	}

	boolean isTimeAware() {
		return tickIn!=null && tickIn.getProviders()!=null && tickIn.getProviders().length>0;
	}

	/**
     * Informs the hosts that the agent has changed location.
	 * @param   repaint Tells the host if it wishes to be repainted
	 */
	protected void locationChanged(boolean repaint) {
		//Set the location plug accordingly
		latLongSO.setVectorData(agent.getLongitude(),agent.getLatitude());

		//Inform the component listeners for the change
		if (agent.listeners.size()>0) {
			AgentLocationChangedEvent e=new AgentLocationChangedEvent(agent,repaint);
			agent.listeners.locationChanged(e);
		}

		//Inform the hosts for the change
		IProtocolPlug[] hosts=host.getProtocolPlugs();
		for (int i=0;i<hosts.length;i++)
			((IAgentHost) hosts[i].getProtocolImplementor()).locationChanged(agent,repaint);

		lookAround(hosts);
		//Tell the embarked agents where they are
		if (agent.hasAgentsEmbarked()) {
			Iterator it=agent.getEmbarkedAgents();
			while (it.hasNext())
				((IAgent) it.next()).carrierAgentMoved(agent.getLongitude(),agent.getLatitude());
		}
	}
	/**
	 * Looks in the hosts for surrounding objects
	 */
	void lookAround(IProtocolPlug[] hosts) {
		for (int i=0;i<hosts.length;i++) {
			ILayerView[] layers=((IAgentHost) hosts[i].getProtocolImplementor()).getMap().getActiveRegionView().getLayerViews();
			for (int j=0;j<layers.length;j++) {
				if (!(layers[j] instanceof IVectorLayerView))
					continue;
				IVectorGeographicObject go=(IVectorGeographicObject)layers[j].getGeographicObjectAt(agent.getLongitude(),agent.getLatitude(),agent.getUnitTolerance());
				for (int k=0;k<lplug;k++) {
					if (layerPlugs[k][0]==layers[j].getRegionView().getMapView() && layerPlugs[k][1].equals(layers[j])) {
						if (go!=null) {
							//The plugCacheMap keeps the last activated object to save from informing again
							if (go!=plugCacheMap.get(layers[j])) {
								((NumberSO) ((SharedObjectPlug) layerPlugs[k][2]).getSharedObject()).setValue(go.getID());
								plugCacheMap.put(layers[j],go);
								//Update the table plugs also
								if (layerPlugs[k][3]!=null)
									((TableRecordPlug) layerPlugs[k][3]).updatePlugValues(go.getID());
								//Post an event
								agent.fireGeographicObjectTouchedEvent(layers[j],go);
							}
						} else {
							if (plugCacheMap.get(layers[j])!=null) {
								((NumberSO) ((SharedObjectPlug) layerPlugs[k][2]).getSharedObject()).setValue(-1);
								//Update the table plugs also
								if (layerPlugs[k][3]!=null)
									((TableRecordPlug) layerPlugs[k][3]).updatePlugValues(-1);
							}
							Object removed=plugCacheMap.remove(layers[j]);
							//Post an event if something has been removed, which means that it is the first (and only) notification
							if (removed!=null)
								agent.fireGeographicObjectTouchedEvent(layers[j],null);
						}
						break;
					}
				}
			}
		}
	}
	/**
	 * Gets a motion layer on the first host that has one available.
	 */
	ILayerView getMotionLayer() {
		boolean accepted=false;
		//Inform the hosts for the change
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return null;
		//See if the change is valid in any of the hosts
		for (int i=0;i<hosts.length && !accepted;i++) {
			IAgentHost host=(IAgentHost) hosts[i].getProtocolImplementor();
			ILayerView layer=null;
			switch (agent.travelsOn()) {
				case IAgent.TRAVELS_EVERYWHERE:
					accepted=true;
					break;
				case IAgent.TRAVELS_ON_ROADS:
					layer=host.getRoadLayer();
					break;
				case IAgent.TRAVELS_ON_RAILWAYS:
					layer=host.getRailwayLayer();
					break;
				case IAgent.TRAVELS_ON_SEA:
					layer=host.getSeaLayer();
					break;
				case IAgent.TRAVELS_ON_AIR:
					layer=host.getAirwayLayer();
					break;
				case IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER:
					layer=host.getCustomMotionLayer(agent.getTravellingOnMotionLayerID());
					break;
			}
			if (layer!=null)
                return layer;
		}
		return null;
	}
	/**
	 * Asks all the hosts if the location is valid.
	 */
	boolean askValidation(double longt,double lat) {
		boolean accepted=false;
		//Inform the hosts for the change
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return true;
		//See if the change is valid in any of the hosts
		for (int i=0;i<hosts.length && !accepted;i++) {
			IAgentHost host=(IAgentHost) hosts[i].getProtocolImplementor();
			ILayerView layer=null;
			switch (agent.travelsOn()) {
				case IAgent.TRAVELS_EVERYWHERE:
					accepted=true;
					break;
				case IAgent.TRAVELS_ON_ROADS:
					layer=host.getRoadLayer();
					break;
				case IAgent.TRAVELS_ON_RAILWAYS:
					layer=host.getRailwayLayer();
					break;
				case IAgent.TRAVELS_ON_SEA:
					layer=host.getSeaLayer();
					break;
				case IAgent.TRAVELS_ON_AIR:
					layer=host.getAirwayLayer();
					break;
				case IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER:
					layer=host.getCustomMotionLayer(agent.getTravellingOnMotionLayerID());
					break;
			}
			if (layer!=null) {
				//The tolerance is one pixel
				double tolerance=host.getMetersPerPixel()*host.getUnitsPerMeter();
                accepted=layer.getGeographicObjectAt(longt,lat,tolerance)!=null;
			}
		}
		return accepted;
	}
	/*
	  Each object of this class is an info about an object seen by the agent,
	  in order to pass it to the embarked agents.
	 /
	private class InfoToEmbarked {
		IMapView map; ILayerView layer; GeographicObject go;
		InfoToEmbarked(IMapView map,ILayerView layer,GeographicObject go) {
			this.map=map;
			this.layer=layer;
			this.go=go;
		}
	}*/

	/**
	 * Asks all the hosts about the location the agent should have after
	 * moving for the given amount of meters. The agent may ask for a
	 * preference to a specific geographic object, if it moves on objects
	 * and wants to keep moving in a specific one.
	 * @param   meters  The distance to travel.
	 * @param   start   The starting point.
	 * @param   heading The heading of the motion.
	 * @param   continueAsFar   Tell to continue as far as possible, possibly moving on more than one objects.
	 * @param   preference  The preferred geographic object to move on.
     * @return  A <code>MotionReport</code> object with aspects of the motion.
	 */
	protected MotionReport goForMeters(double meters,Point2D.Double start,double heading,boolean continueAsFar,Heading preference,Point2D[] pointsForPath) {
		IProtocolPlug[] hosts=host.getProtocolPlugs();
        if (hosts.length==0)
	        return null;
        MotionReport mr=null;
		double travelled=0;
		//See which host can provide for this motion
		for (int i=0;i<hosts.length && travelled==0;i++) {
            mr=goForMeters((IAgentHost) hosts[i].getProtocolImplementor(),meters,start,heading,continueAsFar,preference,pointsForPath);
            travelled=mr.distance;
        }
		return mr;
	}

	/**
	 * Asks a specific host about the location the agent should have after
	 * moving for the given amount of meters. The agent may ask for a
	 * preference to a specific geographic object, if it moves on objects
	 * and wants to keep moving in a specific one.
	 * @param   meters  The distance to travel.
	 * @param   start   The starting point.
	 * @param   heading The heading of the motion.
	 * @param   continueAsFar   Tell to continue as far as possible, possibly moving on more than one objects.
	 * @param   preference  The preferred geographic object to move on.
     * @return  A <code>MotionReport</code> object with aspects of the motion.
	 */
	protected MotionReport goForMeters(IAgentHost host,double meters,Point2D.Double start,double heading,boolean continueAsFar,Heading preference,Point2D[] pointsForPath) {
		//Find the layer the agent travels on.
		ILayerView layer=null;
		switch (agent.travelsOn()) {
			case IAgent.TRAVELS_EVERYWHERE:
				break;
			case IAgent.TRAVELS_ON_ROADS:
				layer=host.getRoadLayer();
				break;
			case IAgent.TRAVELS_ON_RAILWAYS:
				layer=host.getRailwayLayer();
				break;
			case IAgent.TRAVELS_ON_SEA:
				layer=host.getSeaLayer();
				break;
			case IAgent.TRAVELS_ON_AIR:
				layer=host.getAirwayLayer();
				break;
			case IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER:
				layer=host.getCustomMotionLayer(agent.getTravellingOnMotionLayerID());
				break;
		}
		carrier.setLocation(agent.latlong);
		synchronized (motrep) {
			return host.goForMeters(meters,carrier,heading,continueAsFar,layer,preference,pointsForPath,motrep);
		}
	}

	/**
	 * Asks all the hosts about the motion features.
	 */
	MotionFeatures getMotionFeatures(double destLong,double destLat) {
		MotionFeatures mf=null;
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0) {
			mf=new MotionFeatures();
			mf.isValidTrip=false;
			return mf;
		}
		//See in which host the agent will move
		for (int i=0;i<hosts.length && mf==null;i++)
			mf=getMotionFeatures(((IAgentHost) hosts[i].getProtocolImplementor()),destLong,destLat);
		return mf;
	}

	/**
	 * Asks the host about the motion features.
	 */
	MotionFeatures getMotionFeatures(IAgentHost agHost,double destLong,double destLat) {
		MotionFeatures mf=null;
		ILayerView layer=null;
		switch (agent.travelsOn()) {
			case IAgent.TRAVELS_EVERYWHERE:
				mf=new MotionFeatures();
				mf.isValidTrip=true;
				if (agHost.getCoordinateSystem()==IRegionView.COORDINATE_TERRESTRIAL)
					mf.isMovingOnEarth=true;
				else
					mf.isMovingOnEarth=false;
				mf.startLongt=agent.latlong.x;
				mf.startLat=agent.latlong.y;
				mf.endLongt=destLong;
				mf.endLat=destLat;
				mf.host=agHost;
				mf.upm=agHost.getUnitsPerMeter();
				mf.upp=agHost.getMetersPerPixel()*mf.upm;
				mf.distance=agHost.calculateDistance(agent.latlong.x,agent.latlong.y,destLong,destLat);
				break;
			case IAgent.TRAVELS_ON_ROADS:
				layer=agHost.getRoadLayer();
				break;
			case IAgent.TRAVELS_ON_RAILWAYS:
				layer=agHost.getRailwayLayer();
				break;
			case IAgent.TRAVELS_ON_SEA:
				layer=agHost.getSeaLayer();
				break;
			case IAgent.TRAVELS_ON_AIR:
				layer=agHost.getAirwayLayer();
				break;
			case IAgent.TRAVELS_ON_CUSTOM_MOTION_LAYER:
				layer=agHost.getCustomMotionLayer(agent.getTravellingOnMotionLayerID());
				break;
		}

		if (layer!=null) {
			//The tolerance is the quarter of the smallest side of the agent in pixels
			double tolerance=agHost.getMetersPerPixel()*agHost.getUnitsPerMeter();
			mf=layer.getMotionInfo().isValidTrip(agent.getLongitude(),agent.getLatitude(),destLong,destLat,tolerance);
			if (agHost.getCoordinateSystem()==IRegionView.COORDINATE_TERRESTRIAL)
				mf.isMovingOnEarth=true;
			else
				mf.isMovingOnEarth=false;
			mf.host=agHost;
			mf.upm=agHost.getUnitsPerMeter();
			mf.upp=agHost.getMetersPerPixel()*mf.upm;
		}
		return mf;
	}

	void tiltChanged() {
		velocityChanged();
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return;
		for (int i=0;i<hosts.length;i++) {
			((IAgentHost) hosts[i].getProtocolImplementor()).orientationChanged(agent);
		}
	}

	void callRepaintOnHosts() {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return;
		for (int i=0;i<hosts.length;i++) {
			((IAgentHost) hosts[i].getProtocolImplementor()).repaintAgent(agent);
		}
	}

	void locateAgent() {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return;
		for (int i=0;i<hosts.length;i++) {
			((IAgentHost) hosts[i].getProtocolImplementor()).locateAgent(agent);
		}
	}

	void embark(IAgent ag) {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return;
		for (int i=0;i<hosts.length;i++) {
			((IAgentHost) hosts[i].getProtocolImplementor()).embarkedAgent(agent,ag);
		}
	}

	void disembark(IAgent ag) {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return;
		for (int i=0;i<hosts.length;i++) {
			((IAgentHost) hosts[i].getProtocolImplementor()).disembarkedAgent(agent,ag);
		}
	}

	void velocityChanged() {
		/* TODO: Not working properly.
		double an=Helpers.trigAngle(agent.getTiltFromUpright());
		double x=Math.abs(agent.getVelocity()*Math.cos(an));
		double y=Math.abs(agent.getVelocity()*Math.sin(an));
		if (Math.abs(x-((VectorData) velocity.getSharedObject()).getX())>1E-3 || Math.abs(y-((VectorData) velocity.getSharedObject()).getY())>1E-3)
			((VectorData) velocity.getSharedObject()).setVectorData(x,y,new Vector());
		*/
	}

	void repaintTrail() {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return;
		for (int i=0;i<hosts.length;i++) {
			((IAgentHost) hosts[i].getProtocolImplementor()).repaintTrail(agent);
		}
	}

	IAgent canEmbarkOnAgent(String s) {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return null;
		IAgent r=null;
		for (int i=0;i<hosts.length && r==null;i++)
			r=((IAgentHost) hosts[i].getProtocolImplementor()).agentCanEmbarkOn(agent,s);
		return r;
	}

	IAgent getAgent(String s) {
		IProtocolPlug[] hosts;
		if ((hosts=host.getProtocolPlugs()).length==0)
			return null;

		IAgent r=null;
		for (int i=0;i<hosts.length && r==null;i++)
			r=((IAgentHost) hosts[i].getProtocolImplementor()).getAgent(s);
		return r;
	}

	void disconnectAgent() {
		host.disconnect();
	}
	/**
	 * This method gets the MapListener object.
	 */
	public MapListener getMapListener() {
		return mapListen;
	}
	/**
	 * This method gets the RegionListener object.
	 */
	public RegionListener getRegionListener() {
		return regionListen;
	}
	/**
	 * This method gets the LayerListener object.
	 */
	public LayerListener getLayerListener() {
		return layerListen;
	}

	private MapListener mapListen=new MapAdapter() {
		public void mapDatabaseChanged(MapEvent e) {
			//Remove previous plugs if existing
			boolean hasMore=true;
			while (hasMore) {
				hasMore=false;
				for (int i=0;i<lplug;i++)
					if (layerPlugs[i][0].equals(e.getSource()))
						try {
							removeLayerPlug((IMapView) layerPlugs[i][0],(ILayerView) layerPlugs[i][1]);
							hasMore=true;
							break;
						} catch(Exception ex) {
							System.err.println("AGENT#200007251656: Cannot remove map layer plug.");
						}
			}
			layerPlugs=new Object[10][4];
			plugCacheMap=new HashMap();
			lplug=0;
			block=false;
			ILayerView[] layers=((IMapView) e.getSource()).getActiveRegionView().getLayerViews();
			for (int i=0;i<layers.length;i++)
				try {
					addLayerPlug(((IMapView) e.getSource()),layers[i]);
				} catch(Exception e1) {
					System.err.println("AGENT#200006021706: Cannot create plugs for map layers.");
					e1.printStackTrace();
				}
		}
		public void mapActiveRegionChanged(MapEvent parm1) {
			//gr.cti.eslate.mapModel.MapListener method;
			ILayerView[] layers=((IMapView) parm1.getSource()).getActiveRegionView().getLayerViews();
			//Remove plugs of layers that no longer exist
			if (parm1.getOldValue()!=null) {
				boolean found;
				ILayerView[] old=((IRegionView) parm1.getOldValue()).getLayerViews();
				for (int j=0;j<old.length;j++) {
					found=false;
					for (int i=0;i<layers.length;i++) {
						if (old[j].equals(layers[i])) {
							found=true;
							break;
						}
					}
					if (!found) {
						try {
							removeLayerPlug((IMapView) parm1.getSource(),old[j]);
						} catch(Exception e) {
							System.err.println("AGENT#200006021822: Cannot remove layer plug.");
							e.printStackTrace();
						}
					}
				}
			}
			//Add plugs
			for (int i=0;i<layers.length;i++)
				try {
					addLayerPlug((IMapView) parm1.getSource(),layers[i]);
				} catch(Exception e) {
					System.err.println("AGENT#200006021820: Cannot add layer plug.");
					e.printStackTrace();
				}
		}
	};

	private RegionListener regionListen=new RegionAdapter() {
		public void regionLayerAdded(RegionEvent parm1) {
			//gr.cti.eslate.mapModel.RegionListener method;
			try {
				addLayerPlug(((IRegionView) parm1.getSource()).getMapView(),(ILayerView) parm1.getNewValue());
			} catch(Exception e) {
				System.err.println("AGENT#200006021957: Cannot add layer plug.");
			}
		}

		public void regionLayerRemoved(RegionEvent parm1) {
			//gr.cti.eslate.mapModel.RegionListener method;
			try {
				removeLayerPlug(((IRegionView) parm1.getSource()).getMapView(),(ILayerView) parm1.getOldValue());
			} catch(Exception e) {
				System.err.println("AGENT#200006021954: Cannot remove layer plug.");
			}
		}
	};

	private LayerListener layerListen=new LayerAdapter() {
		public void layerRenamed(LayerEvent parm1) {
			//gr.cti.eslate.mapModel.LayerListener method;
			ILayerView lv=(ILayerView) parm1.getSource();
			for (int i=0;i<lplug;i++) {
				IMapView mv=lv.getRegionView().getMapView();
				if (layerPlugs[i][0].equals(mv) && layerPlugs[i][1].equals(lv)) {
					String s=((Plug) layerPlugs[i][2]).getName();
					try {
						((Plug) layerPlugs[i][2]).setName(s.substring(0,s.indexOf('\"'))+"\""+lv.getName()+"\"");
						((Plug) layerPlugs[i][2]).setNameLocaleIndependent(s.substring(0,s.indexOf(':'))+": layerobjectof"+lv.getName());
					} catch(PlugExistsException e) {
						System.err.println("AGENT#200006021939: Cannot rename layer plugs.");
					}
					break;
				}
			}
		}

		public void layerDatabaseTableChanged(LayerEvent e) {
			ILayerView lv=(ILayerView) e.getSource();
			try {
				removeTableRecordPlug(lv.getRegionView().getMapView(),lv);
				if (lv.isTableAssociated())
					addLayerPlug(lv.getRegionView().getMapView(),lv);
			} catch(Exception ex) {
			}
		}
	};
}
