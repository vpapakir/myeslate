package gr.cti.eslate.protocol;

import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.mapModel.MapListener;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Iterator;

/**
 * An interface that describes a particular view of a map.
 * Each map, as well as its regions and layers,
 * have multiple views, through which viewers see them.
 * @author Giorgos Vasiliou
 * @version 2.0, 5 Nov 2002
 */
public interface IMapView {
	/**
	 * Adds a MapListener to the Map. All the other listener additions
	 * (region and layer) must be done on code.
	 */
	public abstract void addMapListener(MapListener mapListener);
	/**
	 * Adds an EnchancedMapListener to the Map. All listener additions
	 * (region and layer) are handled automaticaly.
	 */
	public abstract void addEnchancedMapListener(EnchancedMapListener imv);
	/**
	 * Removes a MapListener from the Map. All the other listener removals
	 * (region and layer) must be done on code.
	 */
	public abstract void removeMapListener(MapListener mapListener);
	/**
	 * Removes an EnchancedMapListener from the Map. All other listener removals
	 * (region and layer) are handled automaticaly.
	 */
	public abstract void removeEnchancedMapListener(EnchancedMapListener imv);
	/**
	 * @return The root Region of the Map.
	 */
	public abstract IRegionView getMapRoot();
	/**
	 * Makes the entry region the active region of the map.
	 */
	public abstract IRegionView getEntryRegion();
	/**
	 * @return The active region view object.
	 */
	public abstract IRegionView getActiveRegionView();
	/**
	 * Sets the active region view object.
	 */
	public abstract void setActiveRegionView(IRegionView regionView);
	/**
	 * Sets the active region view object.
	 */
	public abstract void setActiveRegionView(IZoomRect zoomRect);
	/**
	 * The map name.
	 */
	public abstract void setName(String s);
	/**
	 * @return The name of the Map.
	 */
	public abstract String getName();
	/**
	 * @return The creation date of the Map.
	 */
	public abstract String getCreationDate();
	/**
	 * @return The author of the Map.
	 */
	public abstract String getAuthor();
	/**
	 * @return The comments about the Map.
	 */
	public abstract String getComments();
	/**
	 * @return The Database of the Map.
	 */
	public abstract DBase getDatabase();
	/**
	 * @return The bounding rectangle of the Map.
	 */
	public abstract Rectangle2D getBoundingRect();
	/**
	 * @return The inner regions of <em>rv</em> in the tree hierarchy, if any.
	 */
	public abstract IZoomRect[] getInnerRegions(IRegionView rv);
	/**
	 * @return The outer region of <em>rv</em> in the tree hierarchy, if any.
	 */
	public abstract IZoomRect getOuterRegion(IRegionView rv);
	/**
	 * @return The names of the inner regions in the tree hierarchy, if any.
	 */
	public abstract String[] getInnerRegionNames(IRegionView rv);
	/**
	 * @return The name of the outer region in the tree hierarchy, if any.
	 */
	public abstract String getOuterRegionName(IRegionView rv);
	/**
	 * Checks whether <em>rv</em> has inner regions in the tree hierarchy.
	 */
	public abstract boolean hasInnerRegions(IRegionView rv);
	/**
	 * Gets an iterator of all the regions contained in the map tree.
	 */
	public abstract Iterator getRegionViews();
	/**
	 * Gets the view center in map coordinates of the given viewer.
	 */
	public abstract Point2D getViewCenter(IInteractiveMapViewer mv);
	/**
	 * Gets the view center in map coordinates of the first viewer connected to this IMapView.
	 */
	public abstract Point2D getViewCenter();
	/**
	 * Gets the popup message that would pop up in the given location.
	 * @param x The longitude.
	 * @param y The latitude.
	 * @param tolerance Tolerance, radius around the long-lat point given to search.
	 * @return  A possibly multiline tooltip.
	 */
	public abstract String getTip(double x,double y,double tolerance);
	/**
	 * Gets the data precision of the map.
	 * @return  One of <code>Map.SINGLE_PRECISION, DOUBLE_PRECISION</code>.
	 */
	public abstract int getDataPrecision();
	/**
	 * Changes the era of time this <code>MapView</code> is in. This may cause a change in the background
	 * of the regions or/and a change in the visible geographic objects (<code>getGeographicObjects</code>
	 * in LayerViews will return only the objects existing in the era given). Setting the era to
	 * <code>null,null</code> will make the map not to constrained by time (Default).
	 */
	public abstract void setViewDateInterval(Date from,Date to);
	/**
	 * If this view of the map is associated to an era-of-time controller, like a
	 * TimeMachine, the method returns the "from" component of the "from-to" date
	 * interval that the map view is turned to.
	 * @return  The "from" date.
	 */
	public abstract Date getDateFrom();
	/**
	 * If this view of the map is associated to an era-of-time controller, like a
	 * TimeMachine, the method returns the "to" component of the "from-to" date
	 * interval that the map view is turned to.
	 * @return  The "to" date.
	 */
	public abstract Date getDateTo();
	/**
	 * Gets all RegionViews that have the given depth in the map tree.
	 * @param   depth   The depth to look for.
	 * @return  The RegionViews that have the given depth in the map tree.
	 */
	public IRegionView[] getRegionsOfDepth(int depth);
	/**
	 * Single precision data tag.
	 */
	public static final int SINGLE_PRECISION=0;
	/**
	 * Double precision data tag.
	 */
	public static final int DOUBLE_PRECISION=1;
}
