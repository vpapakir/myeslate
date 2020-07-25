package gr.cti.eslate.protocol;

import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.mapModel.LayerListener;

import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Date;

/**
 * An interface that describes a particular view of a map layer.
 * Each layer in a map, as well as regions and maps themselves,
 * have multiple views, through which viewers see the layers.
 * @author Giorgos Vasiliou
 * @version 2.0, 5 Nov 2002
 */
public interface ILayerView {
	/**
	 * Adds a LayerListener object to this LayerView.
	 */
	public abstract void addLayerListener(LayerListener layerListener);
	/**
	 * Removes a LayerListener object from this LayerView.
	 */
	public abstract void removeLayerListener(LayerListener layerListener);
	/**
	 * All the layer data.
	 */
	public abstract ArrayList getGeographicObjects();
	/**
	 * Get the objects that reside into or intersect with the given rectangle.
	 */
	public abstract ArrayList getGeographicObjects(Shape shape,boolean constrain);
	/**
	 * @return  The nearest geographic object, within a given tolerance.
	 */
	public abstract GeographicObject getGeographicObjectAt(double longitude,double latitude,double tolerance);
	/**
	 * Gets all the objects that are near the location given.
	 * @return  The geographic objects.
	 */
	public abstract ArrayList getGeographicObjectsAt(double longitude,double latitude,double tolerance);
	/**
	 * Gets a specific field from a geographic feature.
	 * This call is directed through the Layer in order
	 * to save memory. Otherwise each GeographicObject
	 * should have a pointer to the database.
	 * @param gf The geographic feature.
	 * @param fName The field name.
	 * @return The field.
	 * @exception FieldDoesntExistException Thrown when the field doesn't exist.
	 * @see gr.cti.eslate.mapModel.GeographicObject.
	 */
	public abstract Object getField(GeographicObject gf,String fName) throws FieldDoesntExistException;
	/**
	 * Sets a specific field from a geographic feature.
	 * This call is directed through the Layer in order
	 * to save memory. Otherwise each GeographicObject
	 * should have a pointer to the database.
	 * @param gf The geographic feature.
	 * @param fName The field name.
	 * @return The field.
	 * @see gr.cti.eslate.mapModel.GeographicObject.
	 */
	public abstract void setField(GeographicObject gf,String fName,Object value) throws InvalidFieldNameException,InvalidCellAddressException,NullTableKeyException,InvalidDataFormatException,DuplicateKeyException,AttributeLockedException;
	/**
	 * Returns an array of all the field names that contain attribute information for
	 * this layer's geographic objects. If this layer is not associated to a database table,
	 * the array has zero length but is never null.
	 */
	public abstract String[] getFieldNames();
	/**
	 * Sets the active geographic object.
	 */
	public abstract void setActiveGeographicObject(GeographicObject go);
	/**
	 * Gets the active geographic object.
	 */
	public abstract GeographicObject getActiveGeographicObject();
	/**
	 * Gets the number of objects in the layer.
	 */
	public abstract int getObjectCount();
	public abstract boolean isVisible();
	public abstract void setVisible(boolean value);
	public abstract void setVisible(boolean value,boolean isChanging);
	/**
	 * Returns a label for the GeographicObject, if it is specified by the map author.
	 * @param gf The geographic feature.
	 * @return The label.
	 * @see gr.cti.eslate.mapModel.GeographicObject.
	 */
	public abstract String getLabel(GeographicObject gf);
	/**
	 * Returns a "tip" for the GeographicObject, if it is specified by the map author.
	 * @param gf The geographic feature.
	 * @return The tip.
	 * @see gr.cti.eslate.mapModel.GeographicObject.
	 */
	public abstract String getTip(GeographicObject gf);
	/**
	 * Gets the class of the associated layer.
	 */
	public abstract Class getLayerClass();
	/**
	 * The layer name.
	 */
	public abstract void setName(String s);
	/**
	 * The layer name.
	 */
	public abstract String getName();
	/**
	 * The associated table, that contains layer data.
	 */
	public abstract Table getTable();
	/**
	 * Sets the selected set of geographic objects of this layer all the
	 * objects that reside into or intersect with the given shape.
	 */
	public abstract void setSelection(Shape shape);
	/**
	 * Sets the selected set of geographic objects of this layer all the
	 * objects that reside into or intersect with the given shape.
	 * @param   shape   The selection shape.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public abstract void setSelection(Shape shape,boolean hasMore);
	/**
	 * Sets the selected objects in this layer. One should use this method in order to inform
	 * all viewers for the change. If the objects contained in the Array are not GeographicObjects,
	 * an IllegalArgumentException will be thrown.
	 */
	public abstract void setSelection(ArrayList obj);
	/**
	 * Sets the selected objects in this layer. One should use this method in order to inform
	 * all viewers for the change. If the objects contained in the Array are not GeographicObjects,
	 * an IllegalArgumentException will be thrown.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public abstract void setSelection(ArrayList obj,boolean hasMore);
	/**
	 * Sets an object the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public abstract void setSelection(GeographicObject go);
	/**
	 * Sets an object the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public abstract void setSelection(GeographicObject go,boolean hasMore);
	/**
	 * Adds objects to the selected subset of this layer. One should use this method in order to inform all viewers
	 * for the change.
	 */
	public abstract void addToSelection(ArrayList a);
	/**
	 * Adds an object to the selected subset of this layer.
	 * If the object is already selected, it is deselected.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public abstract void addToSelection(GeographicObject go);
	/**
	 * Adds to the selected set all geographic objects
	 * that reside into or intersect with the given shape.
	 */
	public abstract void addToSelection(Shape shape);
	/**
	 * Adds to the selected set all geographic objects
	 * that reside into or intersect with the given shape.
	 * @param   shape   The selection shape.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public abstract void addToSelection(Shape shape,boolean hasMore);
	/**
	 * Removes an object from the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public abstract void removeFromSelection(GeographicObject go);
	/**
	 * Removes the objects in the Array from the selected subset of this layer.
	 * <p>
	 * If the Array contains non-GeographicObjects, an <code>IllegalArgumentException</code>
	 * is thrown.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public abstract void removeFromSelection(ArrayList obj);
	/**
	 * Gets an integer which is a constant declaring the paint mode.
	 */
	public abstract int getPaintMode();
	/**
	 * If a layer is editable, the user can attach new objects and delete old ones.
	 */
	public abstract boolean isEditable();
	/**
	 * Removes an object from the layer.
	 */
	public abstract void removeObject(GeographicObject go) throws CannotRemoveObjectException;
	/**
	 * Gets the associated layer.
	 */
	public abstract ILayer getLayer();
	/**
	 * The two points of the rectangle are the top-left and the bottom-right coordinates of the
	 * raster. The others are calculated by the projection.
	 */
	public abstract java.awt.geom.Rectangle2D.Double getBoundingRect();
	/**
	 * Fires the event. Used to inform about a bunch of changes, saving the multiple events
	 * that would be produced otherwise.
	 */
	public abstract void fireLayerGeographicObjectPropertiesChanged(GeographicObject go);
	/**
	 * @return  <em>True</em>, if this layer is being used as a motion layer.
	 */
	public abstract boolean isMotionLayer();
	/**
	 * If this layer is being used as a motion layer, this method returns the motion type.
	 * (For the return values look the gr.cti.eslate.protocol.ILayerView interface).
	 * If it's not it returns ILayerView.NOT_A_MOTION_LAYER.
	 */
	public abstract int getMotionType();
	/**
	 * Each motion layer has an ID. This ID is used by agents, who check it to identify
	 * if they can attach to the layer. If a layer is one of the predefined types, it
	 * gets a predefined ID. Otherwise the user must provide one.
	 */
	public abstract String getMotionID();
	public abstract MotionInfo getMotionInfo();
	/**
	 * @return  The IRegionView containing this layer.
	 */
	public abstract IRegionView getRegionView();
	/**
	 * Changes the era of time this layer is in. <code>getGeographicObjects</code> will
	 * return only the objects existing in the era given. Setting the era to <code>null,null</code>
	 * will make the layer not to constrain the objects (Default).
	 * @param   from    The "from" component of the "from-to" interval.
	 * @param   to      The "to" component of the "from-to" interval.
	 */
	public abstract void setViewDateInterval(java.util.Date from,java.util.Date to);
	/**
	 * Changes the era of time this layer is in. <code>getGeographicObjects</code> will
	 * return only the objects existing in the era given. Setting the era to <code>null,null</code>
	 * will make the layer not to constrain the objects (Default).
	 * @param   from    The "from" component of the "from-to" interval.
	 * @param   to      The "to" component of the "from-to" interval.
	 * @param   sendEvent   If <code>false</code>, no event will be sent to ILayerView viewers.
	 */
	public void setViewDateInterval(Date from,Date to,boolean sendEvent);
	/**
	 * Flags the potential of selecting objects in this layer. This is a per-view property.
	 * If the layer is set with objects not selectable, this property cannot be changed, until
	 * the layer is set with objects selectable.
	 */
	public abstract boolean isObjectSelectable();
	/**
	 * Flags the potential of selecting objects in this layer. This is a per-view property.
	 * If the layer is set with objects not selectable, this property cannot be changed, until
	 * the layer is set with objects selectable.
	 */
	public abstract void setObjectSelectable(boolean b);
	/**
	 * If the layer is defined not to be able to select objects, this method returns true. False otherwise.
	 */
	public abstract boolean mayChangeObjectSelectability();
	/**
	 * Tells if the layer has an associated database table.
	 */
	public abstract boolean isTableAssociated();
	/**
	 * Checks if the layer is affected by changes in time era.
	 */
	public boolean isTimeEraAware();
	/**
	 * Checks if the layer may have labels on the GeographicObjects.
	 */
	public boolean mayHaveLabels();
	/**
	 * Sets the font attributes of the labels.
	 * @param name  The font family name.
	 * @param style The font style.
	 * @param size  The font size.
	 * @param foreground    The color of the letters.
	 * @param background    The color of the background.
	 */
	public abstract void setLabelFont(String name,int style,int size,Color foreground,Color background);
	/**
	 * Gets the font of the geographic object labels.
	 */
	public abstract LabelFont getLabelFont();
	/**
	 * Gets the antialias state of the layer. The layer may ask for the default
	 * antialiasing mode (that of the viewer), may require it off or on.
	 * @return  An Object, one of <code>RenderingHints.VALUE_ANTIALIAS_DEFAULT, RenderingHints.VALUE_ANTIALIAS_ON, RenderingHints.VALUE_ANTIALIAS_OFF</code>.
	 */
	public abstract Object getAntialiasState();

	public static final int CUSTOM_TYPE_MOTION_LAYER=0;
	public static final int ROAD_MOTION_LAYER=1;
	public static final int RAILWAY_MOTION_LAYER=2;
	public static final int SEA_MOTION_LAYER=3;
	public static final int AIR_MOTION_LAYER=4;
	public static final int NOT_A_MOTION_LAYER=-1;
}
