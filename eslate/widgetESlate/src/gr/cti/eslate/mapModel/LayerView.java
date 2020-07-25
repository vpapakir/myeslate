package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.protocol.CannotRemoveObjectException;
import gr.cti.eslate.protocol.FieldDoesntExistException;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.ILayer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.LabelFont;
import gr.cti.eslate.protocol.LayerVisibilityEvent;
import gr.cti.eslate.protocol.MotionInfo;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This class wraps a map layer in the communication mechanism.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 30-Nov-1999
 * @see		gr.cti.eslate.mapModel.MapView
 * @see		gr.cti.eslate.mapModel.RegionView
 */

public abstract class LayerView implements ILayerView, Externalizable {

	LayerView() {
		listeners=0;
	}
	/**
	 * Constructs a LayerView object for layer.
	 * @param layer The Layer itself.
	 */
	LayerView(Layer layer,RegionView parentRegionView) {
		this();
		this.layer=layer;
		this.parentRegionView=parentRegionView;
		layer.addView(this);
		visible=layer.getDefaultVisibility();
		objectSelectable=layer.getCanSelectObjects();
	}
	/**
	 * Adds a LayerListener.
	 */
	public void addLayerListener(LayerListener l) {
		if (layerListener==null)
			layerListener=new LayerEventMulticaster();
		layerListener.add(l);
		//Keep track of how many objects need the layer data. Add one when this
		//view gets its first listener.
		listeners++;
		if (listeners==1)
			layer.increaseNeed();
	}
	/**
	 * Removes a LayerListener.
	 */
	public void removeLayerListener(LayerListener l) {
		if (layerListener==null || layerListener.size()==0)
			return;
		layerListener.remove(l);
		//Keep track of how many objects need the layer data. Subtract one when this
		//view loses its last listener.
		listeners--;
		if (listeners<0)
			listeners=0;
		else if (listeners==0)
			layer.decreaseNeed();
	}
	/**
	 * All the layer data.
	 */
	public ArrayList getGeographicObjects() {
		int[] invisible=parentRegionView.getInvisibilityCriteriaArray(layer);
		ArrayList a=layer.getGeographicObjects(true);
		//No time period limitations
		if (invisible==null || invisible.length==0)
			return a;
		else {
			ArrayList ex=new ArrayList();
			for (int i=0;i<a.size();i++)
				if (Arrays.binarySearch(invisible,((GeographicObject) a.get(i)).getID())<0)
					ex.add(a.get(i));
			return ex;
		}
	}
	/**
	 * Get the objects that reside into or intersect with the given shape.
	 */
	public ArrayList getGeographicObjects(java.awt.Shape shape, boolean constrain) {
		int[] invisible=parentRegionView.getInvisibilityCriteriaArray(layer);
		ArrayList a=layer.getGeographicObjects(shape.getBounds2D(),shape,constrain);
		//No time period limitations
		if (invisible==null || invisible.length==0)
			return a;
		else {
			ArrayList ex=new ArrayList();
			for (int i=0;i<a.size();i++)
				if (Arrays.binarySearch(invisible,((GeographicObject) a.get(i)).getID())<0)
					ex.add(a.get(i));
			return ex;
		}
	}
	/**
	 * Changes the era of time this layer is in. <code>getGeographicObjects</code> will
	 * return only the objects existing in the era given. Setting the era to <code>null,null</code>
	 * will make the layer not to constrain the objects (Default).
	 * @param   from    The "from" component of the "from-to" interval.
	 * @param   to      The "to" component of the "from-to" interval.
	 */
	public void setViewDateInterval(Date from,Date to) {
		setViewDateInterval(from,to,true);
	}
	/**
	 * Changes the era of time this layer is in. <code>getGeographicObjects</code> will
	 * return only the objects existing in the era given. Setting the era to <code>null,null</code>
	 * will make the layer not to constrain the objects (Default).
	 * @param   from    The "from" component of the "from-to" interval.
	 * @param   to      The "to" component of the "from-to" interval.
	 * @param   sendEvent   If <code>false</code>, no event will be sent to ILayerView viewers.
	 */
	public void setViewDateInterval(Date from,Date to,boolean sendEvent) {
		if (layer.getTable()==null || layer.getDateFromBase()==null || layer.getDateToBase()==null)
			return;
		if (oldFrom!=null && oldFrom.equals(from) && oldTo!=null && oldTo.equals(to))
			return;
		oldFrom=from; oldTo=to;
		SimpleDateFormat sdf=layer.getTable().getDateFormat();
		try {
			StringBuffer query=new StringBuffer();
			query.append("(");
			query.append(layer.getDateFromBase().getName());
			query.append(" >= \"");
			query.append(sdf.format(to));
			query.append("\") ");
			query.append(Map.bundleMessages.getString("OR"));
			query.append(" (");
			query.append(layer.getDateToBase().getName());
			query.append(" <= \"");
			query.append(sdf.format(from));
			query.append("\")");
			//now time periods form their own query and the query is added as an invisibilityCondition
			//to the invisibility criteria for this layer view.
			//WARNING: There is no care for dublicate criteria names.
			InvisibilityCondition ic=getInvisibilityCriteria().getCondition(TIME_MACHINE_CRITERIA_NAME);
			if (ic==null)
				getInvisibilityCriteria().add(TIME_MACHINE_CRITERIA_NAME,new String(query),true);
			else
				ic.setQueryString(new String(query));
		} catch(Throwable e) {
			getInvisibilityCriteria().remove(TIME_MACHINE_CRITERIA_NAME);
		}
		//Change the selected subset only when there is a listener to listen to the change
		//if (sendEvent && listeners>0 && parentRegionView.getInvisibilityCriteriaArray(layer)!=null && parentRegionView.getInvisibilityCriteriaArray(layer).length!=0 && layer.getHideUnselected())
		//	layer.setSelectedGeographicObjects(constrain(layer.getSelectedGeographicObjects()));
	}
	/**
	 * Checks if this layer is affected by changes in time era.
	 */
	public boolean isTimeEraAware() {
		return (layer.getDateFromBase()!=null && layer.getDateToBase()!=null);
	}
	/**
	 * Checks if the layer may have labels on the GeographicObjects.
	 */
	public boolean mayHaveLabels() {
		return layer.labelBase!=null && layer.table!=null;
	}

	/**
	 * @return  The nearest geographic object, within a given pixel tolerance.
	 */
	public GeographicObject getGeographicObjectAt(double longitude,double latitude,double tolerance) {
		Rectangle2D.Double r=new Rectangle2D.Double(longitude-tolerance,latitude-tolerance,2*tolerance,2*tolerance);
		ArrayList a=layer.getGeographicObjects(r,r,true);
		int[] invisible=parentRegionView.getInvisibilityCriteriaArray(layer);
		//When only one candidate
		if (a.size()==1 || (!(layer instanceof VectorLayer) && a.size()==1))
			if (invisible!=null && Arrays.binarySearch(invisible,((GeographicObject) a.get(0)).getID())>=0)
			   return null;
			else
			   return (GeographicObject) a.get(0);
		else if (a.size()>1) {
		//When more candidates find the closest one
			double minDist=Double.MAX_VALUE;
			double tmp;
			GeographicObject near=null;
			Point2D.Double clickP=new Point2D.Double(longitude,latitude);
			for (int j=0;j<a.size();j++)
				if (invisible==null || Arrays.binarySearch(invisible,((GeographicObject) a.get(j)).getID())<0) {
					if ((tmp=((IVectorGeographicObject) a.get(j)).calculateDistance(clickP))<minDist) {
						minDist=tmp;
						near=(GeographicObject) a.get(j);
					}
				}
			return near;
		}
		return null;
	}

	/**
	 * Gets all the objects that are near the location given.
	 * @return  The geographic objects.
	 */
	public ArrayList getGeographicObjectsAt(double longitude,double latitude,double tolerance) {
		Rectangle2D.Double r=new Rectangle2D.Double(longitude-tolerance,latitude-tolerance,2*tolerance,2*tolerance);
		ArrayList a=layer.getGeographicObjects(r,r,true);
		int[] invisible=parentRegionView.getInvisibilityCriteriaArray(layer);
		//Check visibility
		if (invisible!=null)
			for (int j=a.size()-1;j>-1;j--)
				if (Arrays.binarySearch(invisible,((GeographicObject) a.get(j)).getID())>=0)
					a.remove(j);
		return a;
	}

	/**
	 * Gets a specific field from a geographic feature.
	 * This call is directed through the Layer in order
	 * to save memory. Otherwise each GeographicObject
	 * should have a pointer to the database.
	 * @param gf The geographic feature.
	 * @param fName The field name.
	 * @return The field.
	 * @exception FieldDoesntExistException Thrown when the field doesn't exist.
	 */
	public Object getField(GeographicObject gf,String fName) throws FieldDoesntExistException {
		return layer.getField(gf,fName);
	}
	/**
	 * Sets a specific field from a geographic feature.
	 * This call is directed through the Layer in order
	 * to save memory. Otherwise each GeographicObject
	 * should have a pointer to the database.
	 * @param gf The geographic feature.
	 * @param fName The field name.
 */
	public void setField(GeographicObject gf,String fName,Object value) throws InvalidFieldNameException,InvalidCellAddressException,NullTableKeyException,InvalidDataFormatException,DuplicateKeyException,AttributeLockedException {
		layer.setField(gf,fName,value);
	}
	/**
	 * Returns an array of all the field names that contain attribute information for
	 * this layer's geographic objects. If this layer is not associated to a database table,
	 * the array has zero length but is never null.
	 */
	public String[] getFieldNames() {
		return layer.getColumnNames();
	}
	/**
	 * Sets the active geographic object.
	 */
	public void setActiveGeographicObject(GeographicObject go) {
		layer.setActiveGeographicObject(go);
	}
	/**
	 * Gets the active geographic object.
	 */
	public GeographicObject getActiveGeographicObject() {
		if (layer.getActiveGeographicObject()!=null && (parentRegionView.getInvisibilityCriteriaArray(layer)!=null && Arrays.binarySearch(parentRegionView.getInvisibilityCriteriaArray(layer),layer.getActiveGeographicObject().getID())>=0))
			return null;
		else
			return layer.getActiveGeographicObject();
	}
	/**
	 * Gets the number of objects in the layer.
	 */
	public int getObjectCount() {
		return layer.getObjectCount();
	}
	/**
	 * Fires the event. Used to inform about a bunch of changes, saving the multiple events
	 * that would be produced otherwise.
	 */
	public void fireLayerGeographicObjectPropertiesChanged(GeographicObject go) {
		layer.fireLayerGeographicObjectPropertiesChanged(go);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean value) {
		/*boolean old=visible;
		visible=value;
		//Send the event only when there is a true change.
		if (layerListener!=null && old!=value) {
			LayerEvent le=new LayerEvent(this,LayerEvent.LAYER_VISIBILITY_CHANGED,new Boolean(old),new Boolean(visible));
			layerListener.layerVisibilityChanged(le);
		}*/
		setVisible(value,false);
	}

	public void setVisible(boolean value, boolean isChanging) {
		boolean old=visible;
		visible=value;
		//Send the event only when there is a true change.
		if (layerListener!=null && old!=value && layerListener.size()!=0) {
			LayerVisibilityEvent le=new LayerVisibilityEvent(this, isChanging);
			layerListener.layerVisibilityChanged(le);
		}
	}
	/**
	 * Sets the selected set of geographic objects of this layer all the
	 * objects that reside into or intersect with the given shape.
	 * @param   shape   The selection shape.
	 */
	public void setSelection(Shape shape) {
		setSelection(shape,false);
	}
	/**
	 * Sets the selected set of geographic objects of this layer all the
	 * objects that reside into or intersect with the given shape.
	 * @param   shape   The selection shape.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public void setSelection(Shape shape,boolean hasMore) {
		if (shape==null)
			layer.setSelectedGeographicObjects(new ArrayList(),hasMore);
		else
			layer.setSelectedGeographicObjects(constrain(layer.getGeographicObjects(shape.getBounds2D(),shape,false)),hasMore);
	}
	/**
	 * Sets the selected objects in this layer. One should use this method in order to inform
	 * all viewers for the change. If the objects contained in the Array are not GeographicObjects,
	 * an IllegalArgumentException will be thrown.
	 */
	public void setSelection(ArrayList obj) {
		setSelection(obj,false);
	}
	/**
	 * Sets the selected objects in this layer. One should use this method in order to inform
	 * all viewers for the change. If the objects contained in the Array are not GeographicObjects,
	 * an IllegalArgumentException will be thrown.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public void setSelection(ArrayList obj,boolean hasMore) {
		if (obj==null)
			layer.setSelectedGeographicObjects(new ArrayList(),hasMore);
		else {
			/*for (int i=0;i<obj.size();i++)
				if (!(obj.get(i) instanceof GeographicObject))
					throw new IllegalArgumentException("Array given to set selection contains non GeographicObjects");*/
			layer.setSelectedGeographicObjects(constrain(obj),hasMore);
		}
	}
	/**
	 * Sets an object the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public void setSelection(GeographicObject go) {
		setSelection(go,false);
	}
	/**
	 * Sets an object the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public void setSelection(GeographicObject go,boolean hasMore) {
		ArrayList a=new ArrayList();
		if (go!=null)
			a.add(go);
		layer.setSelectedGeographicObjects(constrain(a),hasMore);
	}
	/**
	 * Applies the constraints posed by InvisibilityCriteria to
	 * an ArrayList of GeographicObjects.
	 * @param selected  The input GeographicObjects.
	 * @return  The filtered output.
	 */
	private ArrayList constrain(ArrayList selected) {
		int[] invisible=parentRegionView.getInvisibilityCriteriaArray(layer);
		//Constrain the array of objects to the ones that are visible according to the time machine, if any.
		if (invisible!=null && invisible.length!=0) {
			//Count down and remove the invisible due to time machine selected objects
			for (int i=selected.size()-1;i>-1;i--)
				if (Arrays.binarySearch(invisible,((GeographicObject) selected.get(i)).getID())>=0)
					selected.remove(i);
		}
		return selected;
	}
	/**
	 * Adds to the selected set all geographic objects
	 * that reside into or intersect with the given shape.
	 * @param   shape   The selection shape.
	 */
	public void addToSelection(Shape shape) {
		addToSelection(shape,false);
	}
	/**
	 * Adds to the selected set all geographic objects
	 * that reside into or intersect with the given shape.
	 * @param   shape   The selection shape.
	 * @param   hasMore If <code>true</code>, indicates that there are more events to come, so a listener should wait.
	 */
	public void addToSelection(Shape shape,boolean hasMore) {
		if (shape==null)
			return;
		layer.addToSelectedGeographicObjects(constrain(layer.getGeographicObjects(shape.getBounds2D(),shape,false)),hasMore);
	}
	/**
	 * Adds objects to the selected subset of this layer. One should use this method in order to inform all viewers
	 * for the change.
	 */
	public void addToSelection(ArrayList a) {
		layer.addToSelectedGeographicObjects(constrain(a));
	}
	/**
	 * Adds an object to the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public void addToSelection(GeographicObject go) {
		ArrayList a=new ArrayList();
		a.add(go);
		layer.addToSelectedGeographicObjects(constrain(a));
	}
	/**
	 * Removes an object from the selected subset of this layer.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public void removeFromSelection(GeographicObject go) {
		ArrayList a=new ArrayList();
		a.add(go);
		layer.deselectGeographicObjects(a);
	}
	/**
	 * Removes the objects in the Array from the selected subset of this layer.
	 * <p>
	 * If the Array contains non-GeographicObjects, an <code>IllegalArgumentException</code>
	 * is thrown.
	 * <p>
	 * One should use this method in order to inform all viewers
	 * for the change.
	 */
	public void removeFromSelection(ArrayList obj) {
		if (obj==null)
			return;
		for (int i=0;i<obj.size();i++)
			if (!(obj.get(i) instanceof GeographicObject))
				throw new IllegalArgumentException("Array given to set selection contains non GeographicObjects");
		layer.deselectGeographicObjects(obj);
	}
	/**
	 * Returns a label for the GeographicObject, if it is specified by the map author.
	 * @param gf The geographic feature.
	 * @return The label.
	 */
	public String getLabel(GeographicObject gf) {
		return layer.getLabel(gf);
	}
	/**
	 * Returns a "tip" for the GeographicObject, if it is specified by the map author.
	 * @param gf The geographic feature.
	 * @return The tip.
	 */
	public String getTip(GeographicObject gf) {
		return layer.getTip(gf);
	}
	/**
	 * Gets the class of the associated layer.
	 */
	public Class getLayerClass() {
		return layer.getClass();
	}
	/**
	 * The name of the layer.
	 */
	public void setName(String s) {
		layer.setName(s);
	}
	/**
	 * Gets the name of the layer.
	 */
	public String getName() {
		return layer.getName();
	}
	/**
	 * Gets the associated table that contains data about the layer.
	 */
	public Table getTable() {
		return layer.getTable();
	}
	/**
	 * The two points of the rectangle are the top-left and the bottom-right coordinates of the
	 * raster. The others are calculated by the projection.
	 */
	public Rectangle2D.Double getBoundingRect() {
		return layer.getBoundingRect();
	}
	/**
	 * The well known method!
	 */
	public String toString() {
		if (layer!=null)
			return layer.toString();
		return super.toString();
	}
	/**
	 * The well known method!
	 */
	public boolean equals(Object o) {
		if (o==null)
			return false;
		try {
			return layer.equals(((LayerView) o).layer);
		} catch(Exception e) {
			return false;
		}
	}
	/**
	 * Gets an integer which is a constant declaring the paint mode.
	 */
	public int getPaintMode() {
		return layer.getPaintMode();
	}
	/**
	 * If a layer is editable, the user can attach new objects and delete old ones.
	 */
	public boolean isEditable() {
		return layer.isEditable();
	}
	/**
	 * Removes an object from the layer.
	 */
	public void removeObject(GeographicObject go) throws CannotRemoveObjectException {
		layer.removeObject(go);
	}
	/**
	 * Gets the associated layer.
	 */
	public ILayer getLayer() {
		return layer;
	}
	/**
	 * @return  <em>True</em>, if this layer is being used as a motion layer.
	 */
	public boolean isMotionLayer() {
		return layer.isMotionLayer();
	}
	/**
	 * If this layer is being used as a motion layer, this method returns the motion type.
	 * (For the return values look the gr.cti.eslate.protocol.ILayerView interface).
	 * If it's not it returns ILayerView.NOT_A_MOTION_LAYER.
	 */
	public int getMotionType() {
		return layer.getMotionType();
	}
	/**
	 * Returns the MotionInfo object attached to this layer. A MotionInfo object is an
	 * object that provides information to Agents moving on this layer.
	 */
	public MotionInfo getMotionInfo() {
		return layer.getMotionInfo();
	}
	/**
	 * Each motion layer has an ID. This ID is used by agents, who check it to identify
	 * if they can attach to the layer. If a layer is one of the predefined types, it
	 * gets a predefined ID. Otherwise the user must provide one.
	 */
	public String getMotionID() {
		return layer.getMotionID();
	}
	/**
	 * @return  The IRegionView containing this layer.
	 */
	public IRegionView getRegionView() {
		return parentRegionView;
	}
	/**
	 * Flags the potential of selecting objects in this layer. This is a per-view property.
	 * If the layer is set with objects not selectable, this property cannot be changed, until
	 * the layer is set with objects selectable.
	 */
	public boolean isObjectSelectable() {
		if (layer.getCanSelectObjects())
			return objectSelectable;
		else
			return false;
	}
	/**
	 * Flags the potential of selecting objects in this layer. This is a per-view property.
	 * If the layer is set with objects not selectable, this property cannot be changed, until
	 * the layer is set with objects selectable.
	 */
	public void setObjectSelectable(boolean b) {
		if (layer.getCanSelectObjects() && objectSelectable!=b) {
			objectSelectable=b;
			brokerLayerObjectsCanBeSelectedChanged(b);
		}
	}
	/**
	 * If the layer is defined not to be able to select objects, this method returns true. False otherwise.
	 */
	public boolean mayChangeObjectSelectability() {
		return layer.getCanSelectObjects();
	}
	/**
	 * Tells if the layer has an associated database table.
	 */
	public boolean isTableAssociated() {
		return (layer.getTable()!=null);
	}
	/**
	 * Sets the font attributes of the labels.
	 * @param name  The font family name.
	 * @param style The font style.
	 * @param size  The font size.
	 * @param foreground    The color of the letters.
	 * @param background    The color of the background.
	 */
	public void setLabelFont(String name,int style,int size,Color foreground,Color background) {
		layer.setLabelFont(name,style,size,foreground,background);
	}
	/**
	 * Gets the font of the geographic object labels.
	 */
	public LabelFont getLabelFont() {
		return layer.getLabelFont();
	}
	/**
	 * Gets the antialias state of the layer. The layer may ask for the default
	 * antialiasing mode (that of the viewer), may require it off or on.
	 * @return  An Object, one of <code>RenderingHints.VALUE_ANTIALIAS_DEFAULT, RenderingHints.VALUE_ANTIALIAS_ON, RenderingHints.VALUE_ANTIALIAS_OFF</code>.
	 */
	public Object getAntialiasState() {
		switch (layer.getAntialiasState()) {
			case 0:
				return java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
			case 1:
				return java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
			default:
				return java.awt.RenderingHints.VALUE_ANTIALIAS_DEFAULT;
		}
	}

	//** START OF BROKER METHODS
	//** These methods broadcast the given event to all the viewers.
	protected void brokerLayerActiveGeographicObjectChanged(GeographicObject old,GeographicObject fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_ACTIVE_GEOGRAPHIC_OBJECT_CHANGED,old,fresh);
			layerListener.layerActiveGeographicObjectChanged(e);
		}
	}
	/**/
	protected void brokerLayerSelectionChanged(Rectangle2D.Double prevsel,Rectangle2D.Double cursel) {
		brokerLayerSelectionChanged(prevsel,cursel,false);
	}
	/**/
	protected void brokerLayerSelectionChanged(Rectangle2D.Double prevsel,Rectangle2D.Double cursel,boolean isChanging) {
		if (layerListener!=null && layerListener.size()!=0) {
			/*
			 * The selecion event contains the bounding rectangle of the
			 * previous selection and the bounding rectangle of the current
			 * selection to improve drawing performance in the viewers.
			 */
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_SELECTION_CHANGED,prevsel,cursel,isChanging);
			layerListener.layerSelectionChanged(e);
		}
	}
	/**/
	protected void brokerLayerGeographicObjectAdded(GeographicObject go) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_GEOGRAPHIC_OBJECT_ADDED,null,go);
			layerListener.layerGeographicObjectAdded(e);
		}
	}
	/**/
	protected void brokerLayerGeographicObjectRemoved(GeographicObject go) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_GEOGRAPHIC_OBJECT_REMOVED,go,null);
			layerListener.layerGeographicObjectRemoved(e);
		}
	}
	/**/
	protected void brokerLayerGeographicObjectRepositioned(Point2D.Double oldPos,GeographicObject go) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_GEOGRAPHIC_OBJECT_REPOSITIONED,oldPos,go);
			layerListener.layerGeographicObjectRepositioned(e);
		}
	}
	/**/
	protected void brokerLayerColoringChanged() {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_COLORING_CHANGED);
			layerListener.layerColoringChanged(e);
		}
	}
	/**/
	protected void brokerLayerTipBaseChanged(AbstractTableField old,AbstractTableField fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_TIP_BASE_CHANGED,old,fresh);
			layerListener.layerTipBaseChanged(e);
		}
	}
	/**/
	protected void brokerLayerDateFromBaseChanged(AbstractTableField old,AbstractTableField fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_DATE_FROM_BASE_CHANGED,old,fresh);
			layerListener.layerDateFromBaseChanged(e);
		}
	}
	/**/
	protected void brokerLayerDateToBaseChanged(AbstractTableField old,AbstractTableField fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_DATE_TO_BASE_CHANGED,old,fresh);
			layerListener.layerDateToBaseChanged(e);
		}
	}
	/**/
	protected void brokerLayerLabelBaseChanged(AbstractTableField old,AbstractTableField fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_LABEL_BASE_CHANGED,old,fresh);
			layerListener.layerLabelBaseChanged(e);
		}
	}
	/**/
	protected void brokerLayerDefaultVisibilityChanged(boolean fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_DEFAULT_VISIBILITY_CHANGED,new Boolean(!fresh),new Boolean(fresh));
			layerListener.layerDefaultVisibilityChanged(e);
		}
	}
	/**/
	protected void brokerLayerCanBeHiddenChanged(boolean fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_CAN_BE_HIDDEN_CHANGED,new Boolean(!fresh),new Boolean(fresh));
			layerListener.layerCanBeHiddenChanged(e);
		}
	}
	/**/
	protected void brokerLayerObjectsCanBeSelectedChanged(boolean fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_OBJECTS_CAN_BE_SELECTED_CHANGED,new Boolean(!fresh),new Boolean(fresh));
			layerListener.layerObjectsCanBeSelectedChanged(e);
		}
	}
	/**/
	protected void brokerLayerNotSelectedObjectsShownDefaultChanged(boolean fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_NOT_SELECTED_OBJECTS_SHOWN_DEFAULT_CHANGED,new Boolean(!fresh),new Boolean(fresh));
			layerListener.layerNotSelectedObjectsShownDefaultChanged(e);
		}
	}
	/**/
	protected void brokerLayerNotSelectedObjectsShownCanChangeChanged(boolean fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_NOT_SELECTED_OBJECTS_SHOWN_CAN_CHANGE_CHANGED,new Boolean(!fresh),new Boolean(fresh));
			layerListener.layerNotSelectedObjectsShownCanChangeChanged(e);
		}
	}
	/**/
	protected void brokerLayerObjectsWithNoDataShownChanged(boolean fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_OBJECTS_WITH_NO_DATA_SHOWN_CHANGED,new Boolean(!fresh),new Boolean(fresh));
			layerListener.layerObjectsWithNoDataShownChanged(e);
		}
	}
	/**/
	protected void brokerLayerPaintPropertiesChanged() {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_PAINT_PROPERTIES_CHANGED);
			layerListener.layerPaintPropertiesChanged(e);
		}
	}
	/**/
	protected void brokerLayerUnknownPropertyChanged() {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_UNKNOWN_PROPERTY_CHANGED);
			layerListener.layerUnknownPropertyChanged(e);
		}
	}
	/**/
	protected void brokerLayerGeographicObjectPropertiesChanged(GeographicObject go) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_GEOGRAPHIC_OBJECT_PROPERTIES_CHANGED,go,go);
			layerListener.layerGeographicObjectPropertiesChanged(e);
		}
	}
	/**/
	protected void brokerLayerRenamed(String old,String fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_RENAMED);
			layerListener.layerRenamed(e);
		}
	}
	/**/
	protected void brokerLayerDatabaseTableChanged() {
		if (layer.getDateFromBase()==null || layer.getDateToBase()==null)
			getInvisibilityCriteria().createQueryString();
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_DATABASE_TABLE_CHANGED);
			layerListener.layerDatabaseTableChanged(e);
		}
	}
	/**/
	protected void brokerLayerObjectVisibilityChanged() {
//        if (layer.getDateFromBase()==null || layer.getDateToBase()==null)
//            invisible=new int[0];
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_OBJECT_VISIBILITY_CHANGED);
			layerListener.layerObjectVisibilityChanged(e);
		}
	}
	/**/
	protected void brokerLayerRasterTransparencyLevelChanged(int type,int old,int fresh) {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,type,new Integer(old),new Integer(fresh));
			layerListener.layerRasterTransparencyLevelChanged(e);
		}
	}
	/**/
	protected void brokerLayerBoundingRectChanged() {
		if (layerListener!=null && layerListener.size()!=0) {
			LayerEvent e=new LayerEvent(this,LayerEvent.LAYER_BOUNDING_RECT_CHANGED);
			layerListener.layerRasterTransparencyLevelChanged(e);
		}
	}
	//** END OF BROKER METHODS

	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		setVisible(ht.get("visible",visible));
		setObjectSelectable(ht.get("objectSelectable",objectSelectable));
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//If there are no data, don't save anything. The new map will be recreated by code.
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("visible",visible);
		ht.put("objectSelectable",objectSelectable);
		out.writeObject(ht);
	}

/////////////////////////////////////////ADDED CODE FOR INVISIBILITY/////////////////

	public InvisibilityCriteria getInvisibilityCriteria(){
		return parentRegionView.getInvisibilityCriteriaObj(layer);
	}

/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * The associated layer.
	 */
	protected Layer layer;
	boolean visible;
	boolean objectSelectable;
	protected LayerEventMulticaster layerListener;
	private Date oldFrom,oldTo;
	/**
	 * Counts how many listeners have been added to the view.
	 */
	private int listeners;
	static final String TIME_MACHINE_CRITERIA_NAME="&#__TimeMachineSO__#&";

	protected RegionView parentRegionView;

	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
}
