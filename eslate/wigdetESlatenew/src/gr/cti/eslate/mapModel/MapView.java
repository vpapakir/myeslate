package gr.cti.eslate.mapModel;

import gr.cti.eslate.base.DisconnectionEvent;
import gr.cti.eslate.base.DisconnectionListener;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.ProtocolPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.IntBaseArrayDesc;
import gr.cti.eslate.database.engine.LogicalExpression;
import gr.cti.eslate.protocol.EnchancedMapListener;
import gr.cti.eslate.protocol.EnchancedRegionListener;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IInteractiveMapViewer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IZoomRect;
import gr.cti.eslate.sharedObject.TimeMachineSO;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class is used to establish communication between a Map and
 * a Map viewer. It holds all the temporal viewing information. A viewer should NEVER have
 * access to the actual data but only to the MapView object.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 23-Aug-1999
 */
public class MapView implements IMapView, Externalizable {
	/**
	 * This is the default parameterless constructor.
	 * This one is called by the serialization mechanism.
	 */
	MapView() {
	}
	/**
	 * This one should be called from code.
	 */
	public MapView(Map map) {
		this.map=map;
		map.addView(this);

		refresh();
	}
	/**
	 * Adds a MapListener to the Map. All the other listener additions
	 * (region and layer) must be done on code. A listener can only be added once.
	 */
	public void addMapListener(MapListener l) {
		if (!listeners.contains(l)) {
			if (mapListener==null)
				mapListener=new MapEventMulticaster();
			mapListener.add(l);
			listeners.add(l);
		}
	}
	/**
	 * Adds a MapListener to the Map. All listener additions
	 * (region and layer) are handled automaticaly provided that the mapListener
	 * comes from an EnchancedMapListener or an EnchancedRegionListener object.
	 * A listener can only be added once.
	 */
	public void addEnchancedMapListener(EnchancedMapListener imv) {
		if (!enchanced.contains(imv)) {
			if (mapListener==null)
				mapListener=new MapEventMulticaster();
			mapListener.add(imv.getMapListener());
			listeners.add(imv.getMapListener());
			enchanced.add(imv);
			activeRegion.addEnchancedRegionListener(imv);
		}
	}
	/**
	 * Removes a MapListener from the Map. All the other listener removals
	 * (region and layer) must be done on code.
	 */
	public void removeMapListener(MapListener l) {
		if (mapListener==null || mapListener.size()==0)
			return;
		mapListener.remove(l);
		listeners.remove(l);
	}
	/**
	 * Removes a MapListener from the Map. All other listener removals
	 * (region and layer) are handled automaticaly provided that the mapListener
	 * comes from an EnchancedMapListener object.
	 */
	public void removeEnchancedMapListener(EnchancedMapListener imv) {
		if (mapListener==null || mapListener.size()==0)
			return;
		mapListener.remove(imv.getMapListener());
		listeners.remove(imv.getMapListener());
		enchanced.remove(imv);
		if (activeRegion!=null)
			activeRegion.removeEnchancedRegionListener(imv);
	}
	/**
	 * @return The root Region of the Map.
	 */
	public IRegionView getMapRoot() {
		return (IRegionView) regionHash.get(map.getMapRoot().getRegion());
	}
	/**
	 * Makes the entry region the active region of the map.
	 */
	public IRegionView getEntryRegion() {
		return (IRegionView) regionHash.get(map.getEntryRegion());
	}
	/**
	 * @return The active region view object.
	 */
	public IRegionView getActiveRegionView() {
		return activeRegion;
	}
	/**
	 * Sets the active region view object.
	 */
	public void setActiveRegionView(IRegionView regionView) {
		if (regionView==activeRegion)
			return;
		if (regionHash.containsValue(regionView)) {
			RegionView old=activeRegion;
			activeRegion=(RegionView) regionView;
			Iterator it=enchanced.iterator();
			while (it.hasNext()) {
				EnchancedMapListener mv=(EnchancedMapListener) it.next();
				activeRegion.addEnchancedRegionListener(mv);
				old.removeEnchancedRegionListener(mv);
			}
			brokerMapActiveRegionChanged(old,regionView);
		}
	}
	/**
	 * Sets the active region view object.
	 */
	public void setActiveRegionView(IZoomRect zoomRect) {
		setActiveRegionView((IRegionView) regionHash.get(((ZoomRect)zoomRect).getRegion()));
	}
	/**
	 * The name of the Map.
	 */
	public void setName(String s) {
		map.setName(s);
	}
	/**
	 * @return The name of the Map.
	 */
	public String getName() {
		return map.getName();
	}
	/**
	 * @return The creation date of the Map.
	 */
	public String getCreationDate() {
		return map.getCreationDate();
	}
	/**
	 * @return The author of the Map.
	 */
	public String getAuthor() {
		return map.getAuthor();
	}
	/**
	 * @return The comments about the Map.
	 */
	public String getComments() {
		return map.getComments();
	}
	/**
	 * @return The Database of this map.
	 */
	public DBase getDatabase() {
		return map.getDatabase();
	}
	/**
	 * @return The bounding rectangle of this map.
	 */
	public Rectangle2D getBoundingRect() {
		return map.getBoundingRect();
	}
	/**
	 * @return The inner regions of <em>rv</em> in the tree hierarchy, if any.
	 */
	public IZoomRect[] getInnerRegions(IRegionView rv) {
		return (IZoomRect[]) map.getInnerRegions((Region)((RegionView) rv).getRegion());
	}
	/**
	 * @return The outer region of <em>rv</em> in the tree hierarchy, if any.
	 */
	public IZoomRect getOuterRegion(IRegionView rv) {
		return (IZoomRect) map.getOuterRegion((Region)((RegionView) rv).getRegion());
	}
	/**
	 * @return The names of the inner regions in the tree hierarchy, if any.
	 */
	public String[] getInnerRegionNames(IRegionView rv) {
		return map.getInnerRegionNames((Region)((RegionView) rv).getRegion());
	}
	/**
	 * @return The name of the outer region in the tree hierarchy, if any.
	 */
	public String getOuterRegionName(IRegionView rv) {
		Region r=(Region)((RegionView) rv).getRegion();
		if (r.getMapNode().getRegionParent()==null)
			return null;
		return r.getMapNode().getRegionParent().getRegion().getName();
	}
	/**
	 * Checks whether <em>rv</em> has inner regions in the tree hierarchy.
	 */
	public boolean hasInnerRegions(IRegionView rv) {
		return !((Region)((RegionView) rv).getRegion()).getMapNode().isLeaf();
	}
	/**
	 * Gets the map.
	 */
	protected Map getMap() {
		return map;
	}
	/**
	 * Gets the RegionView for the given Region.
	 */
	RegionView getRegionViewFor(Region r) {
		return (RegionView) regionHash.get(r);
	}
	/**
	 * Creates a new or returns the existing invisibility criteria object
	 * associated to this layer. All LayerViews of the same layer in the
	 * same MapView share the same invisibility criteria object.
	 * @param l The layer.
	 * @return  The InvisibilityCriteria object associated to Layer l in this region.
	 */
	public InvisibilityCriteria getInvisibilityCriteriaObj(Layer l) {
		if (invCritHash==null)
			invCritHash=new HashMap();
		if (invCritArrayHash==null)
			invCritArrayHash=new HashMap();
		if (invCritHash.containsKey(l.getGUID()))
			return (InvisibilityCriteria) invCritHash.get(l.getGUID());
		InvisibilityCriteria nic=new InvisibilityCriteria(this,l);
		invCritHash.put(l.getGUID(),nic);
		invCritArrayHash.put(l.getGUID(),new int[0]);
		return nic;
	}
	/**
	 * Returns the existing invisibility criteria array, which keeps the
	 * id's of all objects of the layer that should be invisible
	 * according to the criteria.
	 * @param l The layer.
	 * @return  An integer array with the id's.
	 */
	public int[] getInvisibilityCriteriaArray(Layer l) {
		if (invCritArrayHash==null)
			return null;
		return (int[]) invCritArrayHash.get(l.getGUID());
	}

	protected void updateInvisibleRecords(Layer l) {
		try {
			String qs=getInvisibilityCriteriaObj(l).getQueryString();
			//If there is no query, none of the objects should be invisible.
			//LogicalExpression returns all objects if the query string is empty.
			if (qs==null || qs.equals("")) {
				clearInvisibleRecords(l);
				return;
			}
			LogicalExpression query=new LogicalExpression(l.getTable(),getInvisibilityCriteriaObj(l).getQueryString(),LogicalExpression.NEW_SELECTION,false);
			IntBaseArrayDesc results=query.getQueryResults();
			int[] invisible=new int[results.size()];
			for (int i=0;i<invisible.length;i++)
				invisible[i]=results.get(i);
			Arrays.sort(invisible);
			invCritArrayHash.put(l.getGUID(),invisible);
			Iterator it=getRegionViews();
			while (it.hasNext()) {
				LayerView[] lvs=(LayerView[])((RegionView)it.next()).getLayerViews();
				for (int i=0;i<lvs.length;i++)
					if (l==lvs[i].layer)
						lvs[i].brokerLayerObjectVisibilityChanged();
			}
		} catch(Exception exc) {
			//exc.printStackTrace();
		}
	}
	/**
	 * Clears all invisible tags in objects. All objects will become visible again.
	 */
	protected void clearInvisibleRecords(Layer l) {
		if (invCritArrayHash!=null)
			invCritArrayHash.remove(l.getGUID());
	}
	/**
	 * Does clean-up before a view closing.
	 */
	protected void cleanUp() {
	}
	/**
	 * Rebuilds the regions hash after, say, a root change. Also clears the listener list.
	 */
	protected void refresh() {
		regionHash.clear();
		listeners.clear();
		enchanced.clear();
		//Build region quick reference hash.
		if (map.getMapRoot()!=null) {
			Enumeration e=map.getMapRoot().breadthFirstEnumeration();
			Region r;
			while (e.hasMoreElements()) {
				r=((MapNode) e.nextElement()).getRegion();
				regionHash.put(r,new RegionView(r,this));
			}
			activeRegion=(RegionView) regionHash.get(map.getEntryRegion());
		}
	}
	/**
	 * Gets an iterator of all the regions contained in the map tree.
	 */
	public Iterator getRegionViews() {
		return new Iterator() {
			Enumeration e=map.getMapRoot().breadthFirstEnumeration();
			public boolean hasNext() {
				return e.hasMoreElements();
			}
			public Object next() {
				return regionHash.get(((MapNode) e.nextElement()).getRegion());
			}
			public void remove() {
			}
		};
	}

	/**
	 * Changes the era of time this <code>MapView</code> is in. This may cause a change in the background
	 * of the regions or/and a change in the visible geographic objects (<code>getGeographicObjects</code>
	 * in LayerViews will return only the objects existing in the era given). Setting the era to
	 * <code>null,null</code> will make the map not to constrained by time (Default).
	 */
	public void setViewDateInterval(Date from,Date to) {
		Iterator it=getRegionViews();
		Date oldFrom=dateFrom;
		Date oldTo=dateTo;
		dateFrom=from;
		dateTo=to;
		if ((dateFrom==oldFrom || (dateFrom!=null && dateFrom.equals(oldFrom))) &&
			(dateTo==oldTo || (dateTo!=null && dateTo.equals(oldTo))))
			//No change
			return;
		while (it.hasNext()) {
			//Send the event only for active RegionView
			IRegionView rv=((IRegionView) it.next());
			ILayerView[] lrs=rv.getLayerViews();
			for (int i=0;i<lrs.length;i++)
				((LayerView)lrs[i]).setViewDateInterval(dateFrom,dateTo,rv==getActiveRegionView());
		}
		brokerViewDateIntervalChanged();
	}

	SharedObjectListener getTimeMachineListener() {
		return new SharedObjectListener() {
			public void handleSharedObjectEvent(SharedObjectEvent e) {
				setViewDateInterval(((TimeMachineSO) e.getSharedObject()).getFrom(),((TimeMachineSO) e.getSharedObject()).getTo());
			}
		};
	}

	DisconnectionListener getTimeMachineDisconnectionListener() {
		return new DisconnectionListener() {
			public void handleDisconnectionEvent(DisconnectionEvent e) {
				//Delete InvisibilityConditions related to TimeMachine
				if (invCritHash!=null && invCritHash.size()>0) {
					Iterator it=invCritHash.values().iterator();
					while (it.hasNext())
						((InvisibilityCriteria) it.next()).remove(LayerView.TIME_MACHINE_CRITERIA_NAME);
				}
				dateFrom=null;
				dateTo=null;
				brokerViewDateIntervalChanged();
			}
		};
	}

	/**
	 * If this view of the map is associated to an era-of-time controller, like a
	 * TimeMachine, the method returns the "from" component of the "from-to" date
	 * interval that the map view is turned to.
	 * @return  The "from" date.
	 */
	public Date getDateFrom() {
		return dateFrom;
	}
	/**
	 * If this view of the map is associated to an era-of-time controller, like a
	 * TimeMachine, the method returns the "to" component of the "from-to" date
	 * interval that the map view is turned to.
	 * @return  The "to" date.
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/**
	 * Gets the view center in map coordinates of the given viewer.
	 */
	public Point2D getViewCenter(IInteractiveMapViewer mv) {
		IInteractiveMapViewer viewer=null;
		Plug plug=null;
		//Find the plug of this view
		Plug[] pl=map.getESlateHandle().getPlugs();
		for (int i=0;i<pl.length;i++)
			if (pl[i] instanceof ProtocolPlug && ((ProtocolPlug)pl[i]).getProtocolImplementor()==this) {
				plug=pl[i];
				break;
			}
		//Find the viewer in the plug
		if (plug!=null) {
			pl=plug.getDependents();
			for (int i=0;i<pl.length;i++)
				if (pl[i].getHandle().getComponent()==mv) {
					viewer=(IInteractiveMapViewer) pl[i].getHandle().getComponent();
					break;
				}
		}
		//Ask the viewer found
		if (viewer!=null)
			return viewer.getViewCenter();
		else
			return null;
	}
	/**
	 * Gets the view center in map coordinates of the first viewer connected to this IMapView.
	 */
	public Point2D getViewCenter() {
		IInteractiveMapViewer viewer=null;
		Plug plug=null;
		//Find the plug of this view
		Plug[] pl=map.getESlateHandle().getPlugs();
		for (int i=0;i<pl.length;i++)
			if (pl[i] instanceof ProtocolPlug && ((ProtocolPlug)pl[i]).getProtocolImplementor()==this) {
				plug=pl[i];
				break;
			}
		//Find the viewer in the plug
		if (plug!=null) {
			pl=plug.getDependents();
			for (int i=0;i<pl.length;i++)
				if (pl[i].getHandle().getComponent() instanceof IInteractiveMapViewer) {
					viewer=(IInteractiveMapViewer) pl[i].getHandle().getComponent();
					break;
				}
		}
		//Ask the viewer found
		if (viewer!=null)
			return viewer.getViewCenter();
		else
			return null;
	}
	/**
	 * Gets the popup message that would pop up in the given location.
	 * @param x The longitude.
	 * @param y The latitude.
	 * @param tolerance Tolerance, radius around the long-lat point given to search.
	 * @return  A possibly multiline tooltip.
	 */
	public String getTip(double x,double y,double tolerance) {
		String tt=null;
		//Look for the topmost tooltip
		//...or show tooltips from all layers.(N)
		StringBuffer multiLineToolTip=new StringBuffer("<html><font size=2>");
		boolean atLeastOneToolTipExists = false;
		ILayerView[] lv=getActiveRegionView().getLayerViews();
		for (int i=lv.length-1;i>-1;i--)
			if (lv[i].isVisible()) {
				GeographicObject obj=lv[i].getGeographicObjectAt(x,y,tolerance);
				//When only one candidate
				if (obj!=null) {
					tt=lv[i].getTip(obj);
					if (tt!=null){
					   multiLineToolTip.append(tt);
					   multiLineToolTip.append("<P>");
					   atLeastOneToolTipExists = true;
					}
				}
			}
		//Set the tooltip text to the label of the object
		if (atLeastOneToolTipExists) {
			multiLineToolTip.delete(multiLineToolTip.length()-3,multiLineToolTip.length());
			multiLineToolTip.append("</font></html>");
			return multiLineToolTip.toString();
		} else {
			return null;
		}
	}
	/**
	 * Gets the data precision of the map.
	 * @return  One of <code>Map.SINGLE_PRECISION, DOUBLE_PRECISION</code>.
	 */
	public int getDataPrecision() {
		return map.getDataPrecision();
	}

	/**
	 * Gets all RegionViews that have the given depth in the map tree.
	 * @param   depth   The depth to look for.
	 * @return  The RegionViews that have the given depth in the map tree.
	 */
	public IRegionView[] getRegionsOfDepth(int depth) {
		ArrayList res=new ArrayList();
        MapNode root=map.getMapRoot();
        Enumeration en=root.breadthFirstEnumeration();
		while (en.hasMoreElements()) {
            MapNode mn=(MapNode) en.nextElement();
			if (mn.getDepth()==depth)
                res.add(mn);
			else if (mn.getDepth()>depth)
				break;
		}

		IRegionView[] ret=new IRegionView[res.size()];
		for (int i=0;i<ret.length;i++)
			ret[i]=getRegionViewFor(((MapNode)res.get(i)).getRegion());
		return ret;
	}

	//** START OF BROKER METHODS
	//** These methods broadcast the given event to all the viewers.
	protected void brokerMapChanged() {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_CHANGED);
			mapListener.mapChanged(e);
		}
	}
	/**/
	protected void brokerMapActiveRegionChanged(IRegionView old,IRegionView fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_ACTIVE_REGION_CHANGED,old,fresh);
			mapListener.mapActiveRegionChanged(e);
		}
	}
	/**/
	protected void brokerMapEntryNodeChanged(MapNode old,MapNode fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_ENTRY_NODE_CHANGED,old,fresh);
			mapListener.mapEntryNodeChanged(e);
		}
	}
	/**/
	protected void brokerMapRenamed(String old,String fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_RENAMED,old,fresh);
			mapListener.mapRenamed(e);
		}
	}
	/**/
	protected void brokerMapCreationDateChanged(String old,String fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_CREATION_DATE_CHANGED,old,fresh);
			mapListener.mapCreationDateChanged(e);
		}
	}
	/**/
	protected void brokerMapAuthorChanged(String old,String fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_AUTHOR_CHANGED,old,fresh);
			mapListener.mapAuthorChanged(e);
		}
	}
	/**/
	protected void brokerMapCommentsChanged(String old,String fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_COMMENTS_CHANGED,old,fresh);
			mapListener.mapCommentsChanged(e);
		}
	}
	/**/
	protected void brokerMapDatabaseChanged(DBase old,DBase fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_DATABASE_CHANGED,old,fresh);
			mapListener.mapDatabaseChanged(e);
		}
	}
	/**/
	protected void brokerMapBoundingRectChanged(java.awt.geom.Rectangle2D old,java.awt.geom.Rectangle2D fresh) {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_BOUNDING_RECT_CHANGED,old,fresh);
			mapListener.mapBoundingRectChanged(e);
		}
	}
	/**/
	protected void brokerMapRegionAdded(Region r) {
		regionHash.put(r,new RegionView(r,this));
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_REGION_ADDED,null,regionHash.get(r));
			mapListener.mapRegionAdded(e);
		}
		//Add region to enchanced listeners.
		java.util.Iterator it=enchanced.iterator();
		while (it.hasNext()) {
			EnchancedRegionListener imv=(EnchancedRegionListener) it.next();
			((IRegionView) regionHash.get(r)).addEnchancedRegionListener(imv);
		}
	}
	/**/
	protected void brokerMapRegionRemoved(Region r) {
		IRegionView reg=(IRegionView) regionHash.remove(r);
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_REGION_REMOVED,reg,null);
			mapListener.mapRegionRemoved(e);
		}
		//Remove region from enchanced listeners.
		java.util.Iterator it=enchanced.iterator();
		while (it.hasNext()) {
			EnchancedRegionListener imv=(EnchancedRegionListener) it.next();
			reg.addEnchancedRegionListener(imv);
		}
	}
	/**/
	protected void brokerMapClosed() {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_CLOSED);
			mapListener.mapClosed(e);
		}
	}
	/**/
	protected void brokerViewDateIntervalChanged() {
		if (mapListener!=null && mapListener.size()!=0) {
			MapEvent e=new MapEvent(this,MapEvent.MAP_DATE_INTERVAL_CHANGED);
			mapListener.mapDateIntervalChanged(e);
		}
	}
	//** END OF BROKER METHODS

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		dateFrom=(Date) ht.get("dateFrom");
		dateTo=(Date) ht.get("dateTo");
		String activeGUID=(String) ht.get("activeRegion");
		Iterator it=getRegionViews();
		//It is implied that the order will be the same as saved (breadth-first)
		while (it.hasNext()) {
			RegionView rv=(RegionView) it.next();
			rv.readExternal(in);
			if (rv.getRegion().getGUID().equals(activeGUID))
				activeRegion=rv;
		}
		invCritHash=(HashMap) ht.get("invCritHash",(HashMap)null);
		if (invCritHash!=null) {
			for (Iterator e=invCritHash.keySet().iterator();e.hasNext();) {
				String key=(String) e.next();
				InvisibilityCriteria icr=(InvisibilityCriteria)invCritHash.get(key);
				icr.map=this;
				icr.layer=this.map.getOpenedLayer(icr.layerGUID);
				//Remove the criteria that belong to a layer that has been deleted
				if (icr.layer==null)
					invCritHash.remove(key);
				else
					icr.createQueryString();
			}
		}
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//If there are no data, don't save anything. The new map will be recreated by code.
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		if (activeRegion!=null)
			ht.put("activeRegion",activeRegion.getRegion().getGUID());
		ht.put("dateFrom",dateFrom);
		ht.put("dateTo",dateTo);
		if (invCritHash!=null && invCritHash.size()>0) {
			//Remove useless criteria objects
			Object[] oic=invCritHash.keySet().toArray();
			for (int i=0;i<oic.length;i++) {
				InvisibilityCriteria ic=(InvisibilityCriteria) invCritHash.get(oic[i]);
				if (ic.getNumberOfConditions()==0)
					invCritHash.remove(oic[i]);
			}
			//If there are still more criteria objects, save.
			if (invCritHash.size()>0)
				ht.put("invCritHash",invCritHash);
		}
		out.writeObject(ht);
		Iterator it=getRegionViews();
		while (it.hasNext())
			((RegionView) it.next()).writeExternal(out);
	}

	/**
	 * The associated Map.
	 */
	private Map map;
	/**
	 * Quick reference to the regions of this view.
	 */
	private HashMap regionHash;
	/**
	 * A reference to the active region.
	 */
	private RegionView activeRegion;
	/**
	 * The MapEventMulticaster.
	 */
	private MapEventMulticaster mapListener;
	/**
	 * Holds an array of the listeners because the Multicaster doesn't provide one.
	 */
	private ArrayList listeners;
	/**
	 * Keeps track of "enchanced" listeners (in the meaning given in method definitions).
	 * @see addEnchancedMapListener
	 * @see removeEnchancedMapListener
	 */
	private HashSet enchanced;
	/**
	 * Maps InvisibilityCriteria object to layers.
	 */
	private HashMap invCritHash;
	/**
	 * Maps InvisibilityCriteria result arrays to layers.
	 */
	private HashMap invCritArrayHash;
	/**
	 * The dates the view is between.
	 */
	private Date dateFrom,dateTo;
	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;

	//Initializations
	{
		listeners=new ArrayList();
		enchanced=new HashSet();
		regionHash=new HashMap();
		dateFrom=dateTo=null;
	}
}