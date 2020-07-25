package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.EnchancedRegionListener;
import gr.cti.eslate.protocol.ILayer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IMapBackground;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IRegion;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.MotionInfo;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RegionView implements IRegionView, Externalizable {

	RegionView() {
		listenerCount=0;
	}

	public RegionView(Region region,MapView parentMapView) {
		this();
		this.region=region;
		this.parentMapView=parentMapView;
		region.addView(this);

		//Create layer view objects and build layer quick reference hash.
		layers=new LayerView[region.getLayers().length];
		for (int i=0;i<layers.length;i++)
			createLayerViewFor(region.getLayers()[i]);
	}
	/**
	 * The associated region.
	 */
	public IRegion getRegion() {
		return region;
	}
	/**
	 * Adds a RegionListener to the active Region. All the other listener additions
	 * (layer) must be done on code. A listener can only be added once.
	 */
	public void addRegionListener(RegionListener l) {
		if (!listeners.contains(l)) {
			if (regionListener==null)
				regionListener=new RegionEventMulticaster();
			regionListener.add(l);
			listeners.add(l);
			//Keep track of how many objects need the region image data. Add one when this
			//view gets its first listener.
			listenerCount++;
			if (listenerCount==1)
				region.increaseNeed();
		}
	}
	/**
	 * Adds a RegionListener to the active Region. All listener additions
	 * (layer) are handled automaticaly. A listener can only be added once.
	 */
	public void addEnchancedRegionListener(EnchancedRegionListener irv) {
		if (!enchanced.contains(irv)) {
			if (regionListener==null)
				regionListener=new RegionEventMulticaster();
			regionListener.add(irv.getRegionListener());
			listeners.add(irv.getRegionListener());
			enchanced.add(irv);
			for (int i=0;i<layers.length;i++)
				layers[i].addLayerListener(irv.getLayerListener());
			//Keep track of how many objects need the region image data. Add one when this
			//view gets its first listener.
			listenerCount++;
			if (listenerCount==1)
				region.increaseNeed();
		}
	}
	/**
	 * Removes a RegionListener from the active Region. All the other listener removals
	 * (layer) must be done on code.
	 */
	public void removeRegionListener(RegionListener l) {
		if (regionListener==null || regionListener.size()==0)
			return;
		regionListener.remove(l);
		listeners.remove(l);
		//Keep track of how many objects need the region image data. Subtract one when this
		//view loses its last listener.
		listenerCount--;
		if (listenerCount<0)
			listenerCount=0;
		else if (listenerCount==0)
			region.decreaseNeed();
	}
	/**
	 * Removes a RegionListener from the active Region. All other listener removals
	 * (layer) are handled automaticaly.
	 */
	public void removeEnchancedRegionListener(EnchancedRegionListener irv) {
		if (regionListener==null || regionListener.size()==0)
			return;
		regionListener.remove(irv.getRegionListener());
		listeners.remove(irv.getRegionListener());
		enchanced.remove(irv);
		for (int i=0;i<layers.length;i++)
			layers[i].removeLayerListener(irv.getLayerListener());
		//Keep track of how many objects need the region image data. Subtract one when this
		//view loses its last listener.
		listenerCount--;
		if (listenerCount<0)
			listenerCount=0;
		else if (listenerCount==0)
			region.decreaseNeed();
	}
	/**
	 * Gets all the LayerView objects.
	 * @return The layers (their particular views) ordered in this region view order.
	 */
	public ILayerView[] getLayerViews() {
		return layers;
	}
	/**
	 * Gets a LayerView object.
	 * @return The first layer (its particular view) with the given name.
	 */
	public ILayerView getLayerView(String name) {
		for (int i=0;i<layers.length;i++)
			if (layers[i].getName().equals(name))
				return layers[i];
		return null;
	}
	/**
	 * Gets a LayerView object.
	 * @return The layer (its particular view) in the given z-order.
	 */
	public ILayerView getLayerView(int i) {
		return layers[i];
	}
	/**
	 *
	 */
	public void addChildRegion(String name,Rectangle2D bounds,Image background,int coordinateSystem,double unitsPerMeter) {
		Region r=new Region(name);
		MapNode mn=new MapNode(r);
		mn.setMap(region.getMapNode().getMap());
		r.setBoundingRect(bounds);
		r.addBackground(background);
		r.setCoordinateSystem(coordinateSystem);
		r.setUnitsPerMeter(unitsPerMeter);
		region.getMapNode().getMap().insertNodeInto(mn,region.getMapNode(),region.getMapNode().getChildCount());
	}
	/**
	 * Delegates the call to <code>Map.getInvisibilityCriteriaObj(Layer)</code>.
	 * @param l The layer.
	 * @return  The InvisibilityCriteria object associated to Layer l in this region.
	 */
	public InvisibilityCriteria getInvisibilityCriteriaObj(Layer l) {
		return parentMapView.getInvisibilityCriteriaObj(l);
	}
	/**
	 * Delegates the call to <code>Map.getInvisibilityCriteriaArray(Layer)</code>.
	 * @param l The layer.
	 * @return  An integer array with the id's.
	 */
	public int[] getInvisibilityCriteriaArray(Layer l) {
		return parentMapView.getInvisibilityCriteriaArray(l);
	}
	/**
	 * @return The background of the region. If the map is time aware, the corresponding background is returned.
	 */
	public IMapBackground getBackground() {
		if (((MapView) parentMapView).getDateFrom()==null || ((MapView) parentMapView).getDateTo()==null) {
			if (background!=null)
				return background;
			else
				return region.getDefaultBackground();
		} else {
			//Make sure that the stored background does not belong in a different time era.
			if (background!=null) {
				String[] s=getBackgroundNames();
				for (int i=0;i<s.length;i++)
					if (s[i].equals(background.getFilename()))
						return background;
				//The background belongs to a previous time era.
				background=null;
			}
			return region.getBackgroundOn(((MapView) parentMapView).getDateFrom(),((MapView) parentMapView).getDateTo());
		}
	}
	/**
	 * @return The name of the background of the region. If the map is time aware, the corresponding background name is returned.
	 */
	public String getBackgroundName() {
		if (((MapView) parentMapView).getDateFrom()==null || ((MapView) parentMapView).getDateTo()==null) {
			if (background!=null)
				return background.getFilename();
			else
				return region.getDefaultBackgroundName();
		} else {
			if (background!=null) {
				String[] s=getBackgroundNames();
				for (int i=0;i<s.length;i++)
					if (s[i].equals(background.getFilename()))
						return background.getFilename();
				//The background belongs to a previous time era.
				background=null;
			}
			return region.getBackgroundOnName(((MapView) parentMapView).getDateFrom(),((MapView) parentMapView).getDateTo());
		}
	}
	/**
	 * Set the background of the region view.
	 */
	public IMapBackground getBackground(String name) {
		return region.getBackground(name);
	}
	/**
	 * Sets the background named <code>name</code> as the current background of the region view.
	 */
	public void setBackground(String name) {
		MapBackground old=background;
		background=region.getBackground(name);
		brokerRegionDefaultBackgroundImageChanged(old,background);
	}
	/**
	 * @return A list of all the possible backgrounds of the region. If the map is time aware, the corresponding background names are returned.
	 */
	public String[] getBackgroundNames() {
		if (((MapView) parentMapView).getDateFrom()==null || ((MapView) parentMapView).getDateTo()==null)
			return region.getBackgroundNames();
		else
			return region.getBackgroundNamesOn(((MapView) parentMapView).getDateFrom(),((MapView) parentMapView).getDateTo());
	}
	/**
	 * @return The bounding rectangle of the region.
	 */
	public Rectangle2D getBoundingRect() {
		return region.getBoundingRect();
	}
	/**
	 * The method delegates to the corresponding MapNode. The zoom rectangle that
	 * is shown on the parent node may be different from the bounding rectangle of
	 * the region itself, e.g. when they have different coordinate systems.
	 * @return The zoom rectangle of the region.
	 */
	public Rectangle2D getZoomRectangle() {
		return region.mapNode.getZoomRectangle();
	}
	/**
	 * Swaps the layers with the given indices in this view only.
	 */
	public void swapLayers(int pos1,int pos2) {
		brokerRegionSwapLayers(layers[pos1].layer,layers[pos2].layer);
	}
	/**
	 * Reorders the layers with the given indices in this view only.
	 */
	public void reorderLayers(int[] order) {
		//Fire event only if there is a change
		boolean changed=false;
		for (int i=0;i<order.length;i++)
			if (order[i]!=i) {
				changed=true;
				break;
			}
		if (changed)
			brokerRegionLayersReordered(order);
	}
	/**
	 * @return The scale of the region.
	 */
	public String getScale() {
		return region.getScale();
	}
	/**
	 * The region name.
	 */
	public void setName(String s) {
		region.setName(s);
	}
	/**
	 * @return The region name.
	 */
	public String getName() {
		return region.getName();
	}
	/**
	 * @return <em>True</em> if the region contains the Layer.
	 */
	public boolean contains(ILayerView l) {
		if (l==null)
			return false;
		for (int i=0;i<layers.length;i++)
			if (l.equals(layers[i]))
				return true;
		return false;
	}
	/**
	 * Adds a layer in the region.
	 * @return The ILayerView object created for the layer added.
	 */
	public ILayerView addLayer(ILayer l) {
		region.addLayer((Layer)l);

		return (ILayerView) layerHash.get(l);
	}
	/**
	 * @return  The IMapView containing this region.
	 */
	public IMapView getMapView() {
		return parentMapView;
	}
	/**
	 * @return  The default road motion layer.
	 */
	public ILayerView getRoadLayer() {
		for (int i=0;i<layers.length;i++)
			if (layers[i].isMotionLayer() && layers[i].getMotionID().equals(MotionInfo.MOTION_ROAD_KEY))
				return layers[i];
		return null;
	}
	/**
	 * @return  The default railway motion layer.
	 */
	public ILayerView getRailwayLayer() {
		for (int i=0;i<layers.length;i++)
			if (layers[i].isMotionLayer() && layers[i].getMotionID().equals(MotionInfo.MOTION_RAILWAY_KEY))
				return layers[i];
		return null;
	}
	/**
	 * @return  The default sea motion layer.
	 */
	public ILayerView getSeaLayer() {
		for (int i=0;i<layers.length;i++)
			if (layers[i].isMotionLayer() && layers[i].getMotionID().equals(MotionInfo.MOTION_SEA_KEY))
				return layers[i];
		return null;
	}
	/**
	 * @return  The default air motion layer.
	 */
	public ILayerView getAirwayLayer() {
		for (int i=0;i<layers.length;i++)
			if (layers[i].isMotionLayer() && layers[i].getMotionID().equals(MotionInfo.MOTION_AIR_KEY))
				return layers[i];
		return null;
	}
	/**
	 * @return  The custom motion layer with the specified ID.
	 */
	public ILayerView getCustomMotionLayer(String id) {
		for (int i=0;i<layers.length;i++)
			if (layers[i].isMotionLayer() && layers[i].getMotionID().equals(id))
				return layers[i];
		return null;
	}
	/**
	 * @return  The coordinate system. One of COORDINATE_CARTESIAN, COORDINATE_TERRESTRIAL.
	 */
	public int getCoordinateSystem() {
		return region.getCoordinateSystem();
	}
	/**
	 * @return  The units per meter fraction of the map. This value is valid only if polar (terrestrial) coordinates are not used.
	 */
	public double getUnitsPerMeter() {
		return region.getUnitsPerMeter();
	}
	/**
	 * Measures the distance between two points in the coordinate space.
	 */
	public double measureDistance(double x1,double y1,double x2,double y2) {
		return region.measureDistance(x1,y1,x2,y2);
	}
	/**
	 * Answers if a neighbouring region exists for this region in the specified direction.
	 * The direction parameters are static values. They are defined UP, DOWN, LEFT, RIGHT
	 * relevant to the rectangle shown in a viewer and not NORTH, etc, because it would be
	 * inconsistent in cases where region north was not straightly upwards.
	 *
	 * @return  The neighbouring region view. If <code>null</code> no region exists.
	 */
	public IRegionView getNeighbour(int staticValue) {
		MapNode mn=region.getNeighbour(staticValue);
		if (mn==null)
			return null;
		return ((MapView) parentMapView).getRegionViewFor(mn.getRegion());
	}
	/**
	 * Gets the child region views.
	 */
	public IRegionView[] getChildRegionViews() {
		IRegionView[] m=new IRegionView[region.getMapNode().getChildCount()];
		for (int i=0;i<m.length;i++)
			m[i]=((MapView) parentMapView).getRegionViewFor(((MapNode) region.getMapNode().getChildAt(i)).getRegion());
		return m;
	}
	/**
	 * Get a specific child view.
	 */
	public IRegionView getChildRegionView(int i) {
		return getChildRegionViews()[i];
	}
	/**
	 * Gets a child region view by name.
	 */
	public IRegionView getChildRegionView(String name) {
		for (int i=region.getMapNode().getChildCount();i>-1;i--)
			if (((MapNode) region.getMapNode().getChildAt(i)).getRegion().getName().equals(name))
				return ((MapView) parentMapView).getRegionViewFor(((MapNode) region.getMapNode().getChildAt(i)).getRegion());
		return null;
	}
	/**
	 * Get the orientation.
	 */
	public double getOrientation() {
		return region.getOrientation();
	}




	//** START OF BROKER METHODS
	//** These methods broadcast the given event to all the viewers.
	protected void brokerRegionRenamed(String old,String fresh) {
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_RENAMED,old,fresh);
			regionListener.regionRenamed(e);
		}
	}
	/**/
	protected void brokerRegionBoundingRectChanged(java.awt.geom.Rectangle2D old,java.awt.geom.Rectangle2D fresh) {
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_BOUNDING_RECT_CHANGED,old,fresh);
			regionListener.regionBoundingRectChanged(e);
		}
	}
	/**/
	protected void brokerRegionLayerAdded(Layer layer,boolean postponeEvent) {
		//This call will handle all structures arrangement as well as listener management.
		createLayerViewFor(layer);
		//Then inform about the change
		if (regionListener!=null && regionListener.size()!=0 && !postponeEvent) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_LAYER_ADDED,null,layers[layers.length-1]);
			regionListener.regionLayerAdded(e);
		}
		//Add layer to enchanced listeners.
		java.util.Iterator it=enchanced.iterator();
		while (it.hasNext()) {
			EnchancedRegionListener imv=(EnchancedRegionListener) it.next();
			layers[layers.length-1].addLayerListener(imv.getLayerListener());
		}
	}
	/**/
	protected void brokerRegionLayerRemoved(Layer layer,boolean postponeEvent) {
		//First arrange the local structures
		int index=-1;
		for (int i=0;i<layers.length;i++)
			if (layers[i].layer.equals(layer)) {
				index=i;
				break;
			}
		if (index!=-1) {
			LayerView deleted=layers[index];
			LayerView[] temp=new LayerView[layers.length-1];
			System.arraycopy(layers,0,temp,0,index);
			System.arraycopy(layers,index+1,temp,index,layers.length-1-index);
			layers=temp;
			//Then inform about the change
			if (regionListener!=null && regionListener.size()!=0 && !postponeEvent) {
				RegionEvent e=new RegionEvent(this,RegionEvent.REGION_LAYER_REMOVED,deleted,null);
				regionListener.regionLayerRemoved(e);
			}
			//Remove layer from enchanced listeners.
			java.util.Iterator it=enchanced.iterator();
			while (it.hasNext()) {
				EnchancedRegionListener imv=(EnchancedRegionListener) it.next();
				deleted.removeLayerListener(imv.getLayerListener());
			}
		}
	}
	/**/
	protected void brokerRegionSwapLayers(Layer l1,Layer l2) {
		int pos1=0,pos2=0;
		for (int i=0;i<layers.length;i++) {
			if (layers[i].layer==l1)
				pos1=i;
			if (layers[i].layer==l2)
				pos2=i;
		}
		LayerView temp=layers[pos1];
		layers[pos1]=layers[pos2];
		layers[pos2]=temp;
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_LAYERS_SWAPPED,layers[pos2],layers[pos1]);
			regionListener.regionLayersSwapped(e);
		}
	}
	/**/
	protected void brokerRegionLayersReordered(int[] order) {
		LayerView[] lv=new LayerView[layers.length];
		System.arraycopy(layers,0,lv,0,layers.length);
		for (int i=0;i<order.length;i++)
			layers[i]=lv[order[i]];
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_LAYERS_REORDERED,null,order);
			regionListener.regionLayersReordered(e);
		}
	}
	/**/
	protected void brokerRegionOrientationChanged(Integer old,Integer fresh) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_ORIENTATION_CHANGED,old,fresh);
			regionListener.regionOrientationChanged(e);
		}
	}
	/**/
	protected void brokerRegionScaleChanged(Long old,Long fresh) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_SCALE_CHANGED,old,fresh);
			regionListener.regionScaleChanged(e);
		}
	}
	/**/
	protected void brokerRegionBackgroundImageAdded(MapBackground mbg) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_BACKGROUND_IMAGE_ADDED,null,mbg);
			regionListener.regionBackgroundImageAdded(e);
		}
	}
	/**/
	protected void brokerRegionBackgroundImageRemoved(MapBackground mbg) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_BACKGROUND_IMAGE_REMOVED,null,mbg);
			regionListener.regionBackgroundImageRemoved(e);
		}
	}
	/**/
	protected void brokerRegionDefaultBackgroundImageChanged(IMapBackground old,IMapBackground fresh) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_DEFAULT_BACKGROUND_IMAGE_CHANGED,old,fresh);
			regionListener.regionDefaultBackgroundImageChanged(e);
		}
	}
	/**/
	protected void brokerRegionCoordinateSystemChanged() {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_COORDINATE_SYSTEM_CHANGED);
			regionListener.regionCoordinateSystemChanged(e);
		}
	}
	/**/
	protected void brokerRegionActiveLayerViewChanged(ILayerView old,ILayerView fresh) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_ACTIVE_LAYER_CHANGED,old,fresh);
			regionListener.regionActiveLayerViewChanged(e);
		}
	}
	/**/
	protected void brokerRegionLinkChanged(MapNode old,MapNode fresh) {
		//Inform about the change
		if (regionListener!=null && regionListener.size()!=0) {
			RegionEvent e=new RegionEvent(this,RegionEvent.REGION_LINK_CHANGED,old,fresh);
			regionListener.regionLinkChanged(e);
		}
	}
	//** END OF BROKER METHODS

	/**
	 *
	 */
	public String toString() {
		return "RegionView: "+region.getName()+" depth "+region.mapNode.getDepth();
	}

	/**
	 * Creates a new LayerView object and updates all the data structures.
	 * @return The new LayerView created.
	 */
	private LayerView createLayerViewFor(Layer l) {
		LayerView lv;
		if (l instanceof PointLayer)
			lv=new PointLayerView((PointLayer) l,this);
		else if (l instanceof PolyLineLayer)
			lv=new PolyLineLayerView((PolyLineLayer) l,this);
		else if (l instanceof PolygonLayer)
			lv=new PolygonLayerView((PolygonLayer) l,this);
		else if (l instanceof RasterLayer)
			lv=new RasterLayerView((RasterLayer) l,this);
		else
			throw new RuntimeException("Unknown Layer type.");

		//Null entries exist in the inialization. This is actually
		//an optimization, to avoid rebuilding the layers array when its length known.
		int found=-1;
		for (int i=0;i<layers.length;i++)
			if (layers[i]==null) {
				found=i;
				break;
			}
		if (found!=-1)
			layers[found]=lv;
		else {
			//Reconstruct the layers structure so that it contains the new layer.
			LayerView[] temp=new LayerView[layers.length+1];
			System.arraycopy(layers,0,temp,0,layers.length);
			layers=temp;
			layers[layers.length-1]=lv;
		}
		//Finally update the hastable.
		layerHash.put(l,lv);

		//Add layer to enchanced listeners.
		java.util.Iterator it=enchanced.iterator();
		while (it.hasNext()) {
			EnchancedRegionListener irv=(EnchancedRegionListener) it.next();
			lv.addLayerListener(irv.getLayerListener());
		}
		return lv;
	}
	/**
	 * Gets the depth of the region in the map tree.
	 */
	public int getDepthInTree() {
		return region.getMapNode().getDepth();
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		String aL=(String) ht.get("activeLayer");
		//Read the layer views
		for (int i=0;i<layers.length;i++) {
			layers[i].readExternal(in);
			if (layers[i].layer.getGUID().equals(aL))
				activeLayer=layers[i];
		}
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//If there are no data, don't save anything. The new map will be recreated by code.
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		if (activeLayer!=null)
			ht.put("activeLayer",((Layer)activeLayer.getLayer()).getGUID());
		out.writeObject(ht);
		//Write the layer views
		for (int i=0;i<layers.length;i++)
			layers[i].writeExternal(out);
	}

	/**
	 * The array of the layer view objects.
	 */
	private LayerView[] layers;
	/**
	 * Quick reference to the layers of this view.
	 */
	private HashMap layerHash;
	/**
	 * The associated region.
	 */
	protected Region region;
	/**
	 * The RegionEventMulticaster.
	 */
	private RegionEventMulticaster regionListener;
	/**
	 * Holds an array of the listeners because the Multicaster doesn't provide one.
	 */
	private ArrayList listeners;
	/**
	 * Counts how many listeners have been added to the view.
	 */
	private int listenerCount;
	/**
	 * Keeps track of "enchanced" listeners (in the meaning given in method definitions).
	 */
	private HashSet enchanced;
	/**
	 * The IMapView containing this region.
	 */
	protected MapView parentMapView;
	/**
	 * If !=null this is the background of this view.
	 */
	private MapBackground background;
	/**
	 * The active layer. "Active" is the layer that listens to mouse activations. If null, all layers listen to activations.
	 */
	private LayerView activeLayer;
	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;

	//Initializations
	{
		layers=new LayerView[0];
		listeners=new ArrayList();
		layerHash=new HashMap();
		enchanced=new HashSet();
	}
}
