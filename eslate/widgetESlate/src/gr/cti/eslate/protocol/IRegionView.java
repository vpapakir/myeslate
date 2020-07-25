package gr.cti.eslate.protocol;

import gr.cti.eslate.mapModel.RegionListener;

import java.awt.geom.Rectangle2D;

/**
 * An interface that describes a particular view of a map region.
 * Each region in a map, as well as its layers and maps themselves,
 * have multiple views, through which viewers see the regions.
 * @author Giorgos Vasiliou
 * @version 2.0, 5 Nov 2002
 */
public interface IRegionView {
	/**
	 * Adds a RegionListener to the active Region. All the other listener additions
	 * (layer) must be done on code.
	 */
	public abstract void addRegionListener(RegionListener regionListener);
	/**
	 * Adds a RegionListener to the active Region. All listener additions
	 * (layer) are handled automaticaly.
	 */
	public abstract void addEnchancedRegionListener(EnchancedRegionListener irv);
	/**
	 * Removes a RegionListener from the active Region. All the other listener removals
	 * (layer) must be done on code.
	 */
	public abstract void removeRegionListener(RegionListener regionListener);
	/**
	 * Removes a RegionListener from the active Region. All other listener removals
	 * (layer) are handled automaticaly provided that the regionListener
	 * comes from an EnchancedMapListener object.
	 */
	public abstract void removeEnchancedRegionListener(EnchancedRegionListener irv);
	/**
	 * Swaps the layers with the given indices in this view only.
	 */
	public abstract void swapLayers(int pos1,int pos2);
	/**
	 * Reorders the layers in this view only.
	 * @order   An array with the previous order numbers reordered, i.e. 3,0,1,4,2,5.
	 */
	public abstract void reorderLayers(int[] order);
	/**
	 * Gets all LayerView objects.
	 * @return The layers (their particular views) ordered in this region view order.
	 */
	public abstract ILayerView[] getLayerViews();
	/**
	 * Gets a LayerView object.
	 * @return The first layer (its particular view) with the given name.
	 */
	public abstract ILayerView getLayerView(String name);
	/**
	 * Gets a LayerView object.
	 * @return The layer (its particular view) in the given z-order.
	 */
	public abstract ILayerView getLayerView(int i);
	/**
	 * @return The Region associated to this view. This method should be used cautiously as a viewer should only talk to the view object.
	 */
	public abstract IRegion getRegion();
	/**
	 * @return The background of the region. If the map is time aware, the corresponding background is returned.
	 */
	public abstract IMapBackground getBackground();
	/**
	 * @return The background named <code>name</code> of the region.
	 */
	public abstract IMapBackground getBackground(String name);
	/**
	 * @return Set the background of the region view.
	 */
	public abstract void setBackground(String name);
	/**
	 * @return The name of the background of the region. If the map is time aware, the corresponding background name is returned.
	 */
	public abstract String getBackgroundName();
	/**
	 * @return A list of all the possible backgrounds of the region. If the map is time aware, the corresponding background names are returned.
	 */
	public abstract String[] getBackgroundNames();
	/**
	 * @return The bounding rectangle of the region.
	 */
	public abstract Rectangle2D getBoundingRect();
	/**
	 * The method delegates to the corresponding MapNode. The zoom rectangle that
	 * is shown on the parent node may be different from the bounding rectangle of
	 * the region itself, e.g. when they have different coordinate systems.
	 * @return The zoom rectangle of the region.
	 */
	public abstract Rectangle2D getZoomRectangle();
	/**
	 * @return The scale of the region.
	 */
	public abstract String getScale();
	/**
	 * The region name.
	 */
	public abstract void setName(String s);
	/**
	 * @return The region name.
	 */
	public abstract String getName();
	/**
	 * @return <em>True</em> if the region contains the Layer.
	 */
	public abstract boolean contains(ILayerView l);
	/**
	 * Adds a layer in the region.
	 * @return The ILayerView object created for the layer added.
	 */
	public abstract ILayerView addLayer(ILayer l);
	/**
	 * @return  The IMapView containing this region.
	 */
	public abstract IMapView getMapView();
	/**
	 * @return  The default road motion layer.
	 */
	public abstract ILayerView getRoadLayer();
	/**
	 * @return  The default railway motion layer.
	 */
	public abstract ILayerView getRailwayLayer();
	/**
	 * @return  The default sea motion layer.
	 */
	public abstract ILayerView getSeaLayer();
	/**
	 * @return  The default air motion layer.
	 */
	public abstract ILayerView getAirwayLayer();
	/**
	 * @return  The custom motion layer with the specified ID.
	 */
	public abstract ILayerView getCustomMotionLayer(String id);
	/**
	 * @return  The coordinate system. One of COORDINATE_CARTESIAN, COORDINATE_TERRESTRIAL.
	 */
	public abstract int getCoordinateSystem();
	/**
	 * The units per meter fraction of the region. This value is valid only if polar (terrestrial) coordinates are not used.
	 */
	public abstract double getUnitsPerMeter();
	/**
	 * Gets the child region views.
	 */
	public abstract IRegionView[] getChildRegionViews();
	/**
	 * Gets a child region view.
	 */
	public abstract IRegionView getChildRegionView(int i);
	/**
	 * Gets a child region view by name.
	 */
	public abstract IRegionView getChildRegionView(String name);
	/**
	 * Measures the distance between two points in the coordinate space.
	 */
	public abstract double measureDistance(double x1,double y1,double x2,double y2);
	/**
	 * Answers if a neighbouring region exists for this region in the specified direction.
	 * The direction parameters are static values. They are defined UP, DOWN, LEFT, RIGHT
	 * relevant to the rectangle shown in a viewer and not NORTH, etc, because it would be
	 * inconsistent in cases where region north was not straightly upwards.
	 *
	 * @return  The neighbouring region view. If <code>null</code> no region exists.
	 */
	public abstract IRegionView getNeighbour(int staticValue);

	public abstract double getOrientation();
	/**
	 * Gets the depth of the region in the map tree.
	 */
	public abstract int getDepthInTree();

	public static final int COORDINATE_CARTESIAN=0;
	public static final int COORDINATE_TERRESTRIAL=1;
	public static final int NEIGHBOUR_UP=0;
	public static final int NEIGHBOUR_DOWN=1;
	public static final int NEIGHBOUR_LEFT=2;
	public static final int NEIGHBOUR_RIGHT=3;
}
