package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.IntBaseArrayDesc;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidRecordIndexException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.mapModel.geom.VectorGeographicObject;
import gr.cti.eslate.protocol.AirInfo;
import gr.cti.eslate.protocol.CannotRemoveObjectException;
import gr.cti.eslate.protocol.FieldDoesntExistException;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.ILayer;
import gr.cti.eslate.protocol.ILayerView;
import gr.cti.eslate.protocol.LabelFont;
import gr.cti.eslate.protocol.MotionFeatures;
import gr.cti.eslate.protocol.MotionInfo;
import gr.cti.eslate.protocol.RailwayInfo;
import gr.cti.eslate.protocol.RoadInfo;
import gr.cti.eslate.protocol.SeaInfo;
import gr.cti.eslate.protocol.IPolyLine.LineAspects;
import gr.cti.eslate.tableModel.event.ActiveRecordChangedEvent;
import gr.cti.eslate.tableModel.event.CellValueChangedEvent;
import gr.cti.eslate.tableModel.event.ColumnRemovedEvent;
import gr.cti.eslate.tableModel.event.ColumnRenamedEvent;
import gr.cti.eslate.tableModel.event.DatabaseTableModelAdapter;
import gr.cti.eslate.tableModel.event.DatabaseTableModelListener;
import gr.cti.eslate.tableModel.event.RecordRemovedEvent;
import gr.cti.eslate.tableModel.event.SelectedRecordSetChangedEvent;
import gr.cti.eslate.tableModel.event.TableRenamedEvent;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;
import gr.cti.typeArray.StringBaseArray;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This is the abstract superclass of all types of layers.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 9-Aug-1999
 * @see		gr.cti.eslate.mapModel.PointLayer
 * @see		gr.cti.eslate.mapModel.PolyLineLayer
 * @see		gr.cti.eslate.mapModel.PolygonLayer
 */
public abstract class Layer implements ILayer {
	/**
	 * Adds a view.
	 */
	protected void addView(LayerView mv) {
		for (int i=0;i<views.size();i++)
			if (views.get(i)==mv) {
				return;
			}
		views.add(mv);
	}
	/**
	 * Gets the views of the map.
	 */
	protected ArrayList getViews() {
		return views;
	}
	/**
	 * Returns a label for the GeographicObject, if it is specified by the map author.
	 * @param gf The geographic feature.
	 * @return The label.
	 */
	protected String getLabel(GeographicObject gf) {
		if ((labelBase==null) || (table==null)) return null;
		try {
			return (table.getTableField(labelBase.getName()).getCellObject(gf.getID())).toString();
		} catch(Exception e) {
			return null;
		}
	}
	/**
	 * Returns a "tip" for the GeographicObject, if it is specified by the map author.
	 * @param gf The geographic feature.
	 * @return The tip.
	 */
	protected String getTip(GeographicObject gf) {
		if ((tipBase==null) || (table==null)) return null;
		try {
			return (table.getTableField(tipBase.getName()).getCellObject(gf.getID())).toString();
		} catch(Exception e) {
			return null;
		}
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
		if (table==null)
			return null;
		try {
			return table.getTableField(fName).getCellObject(gf.getID());
		} catch(Exception e) {
			throw new FieldDoesntExistException();
		}
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
		if (table==null)
			return;
		table.setCell(fName,gf.getID(),value);
	}
	/**
	 * Returns an array of all the field names that contain attribute information for
	 * this layer's geographic objects. If this layer is not associated to a database table,
	 * the array has zero length but is never null.
	 */
	public String[] getColumnNames() {
		String[] ret;
		if (table==null)
			ret=new String[0];
		else {
			StringBaseArray fnames=table.getFieldNames();
			ret=new String[fnames.size()];
			for (int i=0;i<ret.length;i++)
				ret[i]=(String) fnames.get(i);
		}
		return ret;
	}
	/**
	 * Sets the layer table.
	 */
	public void setTable(Table table) throws InvalidLayerDataException {
		//Remove previous listeners, if any.
		if (this.table!=null)
			this.table.removeTableModelListener(defaultTMListener);
		this.table=table;
		if (table!=null) {
			if (defaultTMListener==null)
				defaultTMListener=new LayerTableListener();
			this.table.addTableModelListener(defaultTMListener);
		}
		labelBase=null;
		tipBase=null;
		fromBase=null;
		toBase=null;
		saved=false;
		updateInvisibleArray();
		//Set the active record according to the active object.
		if (table!=null) {
			if (activeObject!=null)
				table.setActiveRecord(activeObject.getID());
			else
				table.setActiveRecord(-1);
		}
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerDatabaseTableChanged();
	}
	/**
	 * Removes an object from the layer.
	 */
	public void removeObject(GeographicObject go) throws CannotRemoveObjectException {
		//There is something strange with this method. The object removal is actually done
		//by the database listener (if it exists) because the dbengine sends an activeRecordChangedEvent
		//BEFORE the recordRemovedEvent, so the ignoreEvent flag doesn't work, causing a race condition
		//removing several objects, maybe all!
		if (go==null)
			return;
		if (table!=null)
			try {
				table.removeRecord(go.getID(),-1,false);
			} catch(Exception e) {
				ignoreEvent=false;
				System.err.println("MAP#200003311627: Could not delete the object's record from the table.");
				e.printStackTrace();
			}
		else
			__removeObject(go);
	}
	/**
	 * Internal use only.
	 */
	abstract void __removeObject(GeographicObject go) throws CannotRemoveObjectException;
	/**
	 * All the layer data.
	 */
	protected abstract ArrayList getGeographicObjects(boolean constrain);
	/**
	 * Get the objects that their bounding rectangle resides into or intersects with the given rectangle.
	 */
	protected abstract ArrayList getGeographicObjects(java.awt.geom.Rectangle2D rectangle,boolean constrain);
	/**
	 * Get the objects that their bounding rectangle resides into or intersects with the given rectangle
	 * and the "hotspot" shape intersects with the actual shape.
	 */
	protected abstract ArrayList getGeographicObjects(java.awt.geom.Rectangle2D rectangle,Shape hotspot,boolean constrain);
	/**
	 * Sets the active geographic object.
	 */
	protected void setActiveGeographicObject(GeographicObject active) {
		//If the object to set active is invisible, set to null;
		if (!shouldBeVisible(active)) {
			active=null;
			ignoreActiveRecordChangedEvent=false;
		}
		if (activeObject==active) {
			oldActiveObject=activeObject;
			activeObject=null;
		} else {
			oldActiveObject=activeObject;
			activeObject=active;
		}
		saved=false;
		//Inform DB
		if (!ignoreActiveRecordChangedEvent && table!=null) {
			if (activeObject!=null) {
				if (table.getActiveRecord()!=activeObject.getID()) {
					ignoreEvent=true;
					table.setActiveRecord(activeObject.getID());
				}
			} else {
				if (table.getActiveRecord()!=-1) {
					ignoreEvent=true;
					table.setActiveRecord(-1);
				}
			}
		} else
			ignoreActiveRecordChangedEvent=false;
	}
	/**
	 * Gets the active geographic object.
	 */
	protected GeographicObject getActiveGeographicObject() {
		return activeObject;
	}
	/**
	 * Gets the number of objects in the layer.
	 */
	public abstract int getObjectCount();
	/**
	 * Sets the selection.
	 */
	protected void setSelectedGeographicObjects(ArrayList selected) {
		setSelectedGeographicObjects(selected,false);
	}
	/**
	 * Sets the selection.
	 */
	protected void setSelectedGeographicObjects(ArrayList selected,boolean hasMore) {
		saved=false;
		//Clear previous selection
		for (int i=0;i<getGeographicObjects(false).size();i++)
			try {
				((GeographicObject) getGeographicObjects(false).get(i)).setSelected(false);
			} catch(ClassCastException e) {/*Grrr! Who put something else in the array?*/}
		//Set new selection, if any
		if (selected!=null) {
			if (invisible!=null && invisible.length!=0) {
				//Count down and remove the invisible selected objects
				for (int i=selected.size()-1;i>-1;i--)
					if (Arrays.binarySearch(invisible,((GeographicObject) selected.get(i)).getID())<0)
						((GeographicObject) selected.get(i)).setSelected(true);
					else
						selected.remove(i);
			} else {
				for (int i=selected.size()-1;i>-1;i--)
					((GeographicObject) selected.get(i)).setSelected(true);
			}
		}
		//Inform the db
		ignoreEvent=true;
		if (selected!=null) {
			IntBaseArrayDesc dbarr=new IntBaseArrayDesc(selected.size());
			for (int i=0;i<selected.size();i++)
				dbarr.add(i,((GeographicObject)selected.get(i)).getID());
			if (table!=null)
				table.setSelectedSubset(dbarr);
		}
	}
	/**
	 * Deselects the given objects. Other selected objects remain unchanged.
	 */
	protected void deselectGeographicObjects(ArrayList desel) {
		saved=false;
		//Clear previous selection
		for (int i=0;i<desel.size();i++)
			try {
				((GeographicObject) desel.get(i)).setSelected(false);
			} catch(ClassCastException e) {/*Grrr! Who put something else in the array?*/}
		//Inform the db
		ignoreEvent=true;
		int[] dbarr=new int[desel.size()];
		for (int i=0;i<desel.size();i++)
			dbarr[i]=((GeographicObject)desel.get(i)).getID();
		if (table!=null)
			table.removeFromSelectedSubset(dbarr);
	}
	/**
	 * Expands the selection.
	 */
	protected void addToSelectedGeographicObjects(ArrayList selected) {
		addToSelectedGeographicObjects(selected,false);
	}
	/**
	 * Expands the selection.
	 */
	protected void addToSelectedGeographicObjects(ArrayList selected,boolean hasMore) {
		saved=false;
		IntBaseArrayDesc dbarr=null;
		//Set selected property, if any objects
		if (selected!=null)
			if (table!=null) {
				dbarr=table.getSelectedSubset();
				for (int i=selected.size()-1;i>-1;i--)  {
					((GeographicObject) selected.get(i)).setSelected(true);
					if (((GeographicObject) selected.get(i)).isSelected()) {
						dbarr.add(((GeographicObject)selected.get(i)).getID());
					} else {
						dbarr.removeElements(((GeographicObject)selected.get(i)).getID());
					}
				}
			} else {
				for (int i=selected.size()-1;i>-1;i--)
					((GeographicObject) selected.get(i)).setSelected(true);
			}
		//Inform the db
		ignoreEvent=true;
		if (table!=null && (selected!=null) && (selected.size()!=0))
			table.setSelectedSubset(dbarr);
	}
	protected ArrayList getSelectedGeographicObjects() {
		ArrayList<GeographicObject> a=new ArrayList<GeographicObject>();
		ArrayList o=getGeographicObjects(false);
		for (int i=0;i<o.size();i++)
			if (((GeographicObject) o.get(i)).isSelected())
				a.add((GeographicObject) o.get(i));
		return a;
	}
	/**
	 * The associated Table.
	 */
	protected Table getTable() {
		return table;
	}
	/**
	 * Name setter.
	 * @param newName The new name.
	 */
	protected void setName(String newName) {
		String old=name;
		name=newName;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerRenamed(old,newName);
	}
	/**
	 * Name getter.
	 * @return Layer name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Default visibility setter. This attribute is different
	 * than the visibility attribute. It is used when maps
	 * share the same layer.
	 * @param value The new value.
	 */
	protected void setDefaultVisibility(boolean value) {
		if (defVisibility==value)
			return;
		defVisibility=value;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerDefaultVisibilityChanged(value);
	}
	/**
	 * Default visibility getter.
	 * @return The requested property.
	 */
	public boolean getDefaultVisibility() {
		return defVisibility;
	}
	/**
	 * Defines whether the user can hide this layer.
	 * @param value The new value.
	 */
	protected void setHideable(boolean value) {
		if (hideable==value)
			return;
		hideable=value;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerCanBeHiddenChanged(value);
	}
	/**
	 * Property getter.
	 * @return The requested property.
	 */
	public boolean isHideable() {
		return hideable;
	}
	/**
	 * Defines whether the user can select Objects on this layer.
	 * @param   value The new value.
	 * @param   propagateToAllViews Because this is a per view property, setting this to true changes all the
	 */
	protected void setCanSelectObjects(boolean value,boolean propagateToAllViews) {
		if (canSelectObjects==value)
			return;
		canSelectObjects=value;
		saved=false;
		if (propagateToAllViews) {
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).setObjectSelectable(value);
		} else {
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerObjectsCanBeSelectedChanged(value);
		}
	}
	/**
	 * Property getter.
	 * @return The requested property.
	 */
	public boolean getCanSelectObjects() {
		return canSelectObjects;
	}
	/**
	 * Defines whether the unselected Objects are shown or hidden.
	 * @param value The new value.
	 */
	protected void setHideUnselected(boolean value) {
		if (hideUnselected==value)
			return;
		hideUnselected=value;
		saved=false;
		//Not so good tactic but I avoid method call and inheritance mess.
		if (Layer.this instanceof RasterLayer)
			((RasterLayer.RasterImage) ((RasterLayer) Layer.this).getRaster()).colorModel=((RasterLayer) Layer.this).tuneTransparency((java.awt.image.IndexColorModel) ((RasterLayer) Layer.this).getRaster().getColorModel());
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerNotSelectedObjectsShownDefaultChanged(!value);
	}
	/**
	 * Property getter.
	 * @return The requested property.
	 */
	public boolean getHideUnselected() {
		return hideUnselected;
	}
	/**
	 * Defines whether the user can change the hideUnselected attribute.
	 * @param value The new value.
	 */
	protected void setCanHideUnselected(boolean value) {
		if (canHideUnselected==value)
			return;
		canHideUnselected=value;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerNotSelectedObjectsShownCanChangeChanged(value);
	}
	/**
	 * Property getter.
	 * @return The requested property.
	 */
	public boolean getCanHideUnselected() {
		return canHideUnselected;
	}
	/**
	 * Defines whether the Objects with no attribute information
	 * are visible or not.
	 * @param value The new value.
	 */
	protected void setShowBlankRecordObjects(boolean value) {
		if (showBlankRecordObjects==value)
			return;
		showBlankRecordObjects=value;
		saved=false;
		updateInvisibleArray();
		//Not so good tactic but I avoid method call and inheritance mess.
		if (Layer.this instanceof RasterLayer)
			((RasterLayer.RasterImage) ((RasterLayer) Layer.this).getRaster()).colorModel=((RasterLayer) Layer.this).tuneTransparency((java.awt.image.IndexColorModel) ((RasterLayer) Layer.this).getRaster().getColorModel());
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerObjectsWithNoDataShownChanged(value);
	}
	/**
	 * Checks which objects should be invisible, according to showBlankRecordObjects property.
	 */
	void updateInvisibleArray() {
		//Make the invisible array. The array will keep up-to-date through the database listener.
		if (!showBlankRecordObjects && table!=null) {
			//Reset the invisible array
			ArrayList all=getGeographicObjects(false);
			ArrayList qualified=new ArrayList();
			for (int i=0;i<all.size();i++) {
				try {
					ArrayList record=table.getRecord(((GeographicObject) all.get(i)).getID());
					boolean visible=false;
					for (int j=0;j<record.size();j++)
						try {
							if (!table.getTableField(j).isHidden() && record.get(j)!=null && !record.get(j).equals("")) {
								visible=true;
								break;
							}
						} catch(InvalidFieldIndexException ex) {}
					if (!visible)
						qualified.add(all.get(i));
				} catch(InvalidRecordIndexException ex) {}
			}
			invisible=new int[qualified.size()];
			for (int i=0;i<invisible.length;i++)
				invisible[i]=((GeographicObject) qualified.get(i)).getID();
			Arrays.sort(invisible);
		} else {
			invisible=new int[0];
		}
	}

	/**
	 * Applies the constraints to an array of geographic objects.
	 */
	ArrayList applyConstraints(ArrayList ar) {
		ArrayList a;
		if (hideUnselected) {
			a=new ArrayList();
			for (int i=0;i<ar.size();i++)
				if (((GeographicObject) ar.get(i)).isSelected())
					a.add(ar.get(i));
		} else
			a=ar;
		if (!showBlankRecordObjects) {
			if (table!=null) {
				if (invisible.length!=0) {
					ArrayList ex=new ArrayList();
					for (int i=0;i<a.size();i++)
						if (Arrays.binarySearch(invisible,((GeographicObject) a.get(i)).getID())<0)
							ex.add(a.get(i));
					return ex;
				}
			} else
				return new ArrayList();
		}

		return a;
	}
	/**
	 * Applies the constraints to an array of geographic objects.
	 */
	ArrayList applyConstraints2(ArrayList ar) {  // GT A
		ArrayList a;
		if (hideUnselected) {
			a=new ArrayList();
			for (int i=0;i<ar.size();i++)
				if (((GeographicObject) ar.get(i)).isSelected())
					a.add(ar.get(i));
		} else
			a=ar;
		if (!showBlankRecordObjects) {
			if (table!=null) {
				if (invisible.length!=0) {
					ArrayList ex=new ArrayList();
					for (int i=0;i<a.size();i++)
						if (Arrays.binarySearch(invisible,((GeographicObject) a.get(i)).getID())<0)
							ex.add(a.get(i));
					return ex;
				}
			} else
				return new ArrayList();
		}

		return a;
	}
	/**
	 * Applies the constraints to find if the geographic object should be visible.
	 */
	boolean shouldBeVisible(GeographicObject go) {
		if (go==null)
			return false;
		if (hideUnselected) {
			if (!go.isSelected())
				return false;
		}
		if (!showBlankRecordObjects) {
			if (table!=null) {
				if (invisible.length!=0) {
					if (Arrays.binarySearch(invisible,go.getID())<0)
						return true;
				}
			} else
				return false;
		}

		return true;
	}
	/**
	 * Property getter.
	 * @return The requested property.
	 */
	public boolean getShowBlankRecordObjects() {
		return showBlankRecordObjects;
	}
	/**
	 * Sets the "editable" property. An editable layer can be altered by the users.
	 */
	public void setEditable(boolean value) {
		editable=value;
		saved=false;
	}
	/**
	 * Gets the "editable" property. An editable layer can be altered by the users.
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * Sets the label base field. The label base is a field
	 * whose value appears next to the point, e.g. a city name.
	 * @param labelBase The field name.
	 */
	void setLabelBase(AbstractTableField labelBase) {
		if (labelBase!=this.labelBase) {
			AbstractTableField old=this.labelBase;
			this.labelBase=labelBase;
			saved=false;
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerLabelBaseChanged(old,labelBase);
		}
	}
	/**
	 * Sets the font attributes of the labels.
	 * @param name  The font family name.
	 * @param style The font style.
	 * @param size  The font size.
	 * @param foreground    The color of the letters.
	 * @param background    The color of the background.
	 */
	void setLabelFont(String name,int style,int size,Color foreground,Color background) {
		labelFont=new Font(name,style,size);
		labelFore=foreground;
		labelBack=background;
	}
	/**
	 * Gets the font of the geographic object labels.
	 */
	LabelFont getLabelFont() {
		return null;
	}
	/**
	 * Gets the label base field.
	 */
	public AbstractTableField getLabelBase() {
		return labelBase;
	}
	/**
	 * Sets the tip base field. The tip base is a field
	 * whose value appears as a tooltip.
	 * @param tipBase The field name.
	 */
	void setTipBase(AbstractTableField tipBase) {
		if (tipBase!=this.tipBase) {
			AbstractTableField old=this.tipBase;
			this.tipBase=tipBase;
			saved=false;
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerTipBaseChanged(old,tipBase);
		}
	}
	/**
	 * Gets the tip base field.
	 */
	AbstractTableField getTipBase() {
		return tipBase;
	}
	/**
	 * Sets the date from base field. The date from base is a field
	 * whose value is read when we need to define when an object existed.
	 * @param   fromBase    The field name.
	 */
	void setDateFromBase(AbstractTableField fromBase) {
		if (fromBase!=this.fromBase) {
			AbstractTableField old=this.fromBase;
			this.fromBase=fromBase;
			saved=false;
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerDateFromBaseChanged(old,fromBase);
		}
	}
	/**
	 * Gets the date from base field.
	 */
	AbstractTableField getDateFromBase() {
		return fromBase;
	}
	/**
	 * Sets the date to base field. The date to base is a field
	 * whose value is read when we need to define when an object existed.
	 * @param   toBase    The field name.
	 */
	void setDateToBase(AbstractTableField toBase) {
		if (toBase!=this.toBase) {
			AbstractTableField old=this.toBase;
			this.toBase=toBase;
			saved=false;
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerDateToBaseChanged(old,toBase);
		}
	}
	/**
	 * Gets the date to base field.
	 */
	AbstractTableField getDateToBase() {
		return toBase;
	}
	/**
	 * Gets the whole associated record from a geographic feature.
	 * This call is directed through the Layer in order
	 * to save memory. Otherwise each GeographicObject
	 * should have a pointer to the database.
	 * @param gf The geographic feature.
	 * @return An Array containing all the fields.
	 * @exception RecordDoesntExistException Thrown when the record doesn't exist.
	 */
	public ArrayList getRecord(GeographicObject gf) throws RecordDoesntExistException {
		if (table==null)
			return null;
		try {
			return table.getRecord(gf.getID());
		} catch(InvalidRecordIndexException e2) {
			throw new RecordDoesntExistException();
		}
	}

	private boolean hasBlankRecord(GeographicObject gf) {
		if (table==null)
			return false;
		//This returns true if the record has null values in all VISIBLE fields
		try {
			int tc=table.getFieldCount();
			ArrayList cb=table.getRecord(gf.getID());
			for (int f=0;f<tc;f++) {
				if ((cb.get(f)!=null) && (!table.getTableField(f).isHidden()))
					return false;
			}
		} catch(InvalidRecordIndexException ex){
		} catch(InvalidFieldIndexException ex){
		}
		return true;
	}
	/**
	 * @return <em>True</em> if the object existed in the given date.
	 */
	public boolean objectExistsOn(Date date) {
		//TO DO: Implement
		return true;
	}
	/**
	 * Sets the bounding rectangle of the layer.
	 */
	protected void setBoundingRect(Rectangle2D.Double r) {
		boundRect.x=r.x;
		boundRect.y=r.y;
		boundRect.width=r.width;
		boundRect.height=r.height;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerBoundingRectChanged();
	}
	/**
	 * The two points of the rectangle are the top-left and the bottom-right coordinates of the
	 * raster. The others are calculated by the projection.
	 */
	public Rectangle2D.Double getBoundingRect() {
		return boundRect;
	}
	/**
	 * The well-known method!
	 */
	public String toString() {
		return name;
	}
	/**
	 * Fires the PaintPropertiesChanged event.
	 */
	public void firePaintPropertiesChanged() {
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerPaintPropertiesChanged();
	}
	/**
	 * Fires the event. Used to inform about a bunch of changes, saving the multiple events
	 * that would be produced otherwise.
	 */
	public void fireLayerGeographicObjectPropertiesChanged(GeographicObject go) {
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerGeographicObjectPropertiesChanged(go);
	}
	/**
	 * Sets the antialias state of the layer.
	 * -1 is for Default antialiasing.
	 * 0 is for no antialiasing.
	 * 1 is for antialiasing.
	 */
	protected void setAntialiasState(int i) {
		if (i<-1 || i>1)
			throw new IllegalArgumentException("Unknown antialias state for layer "+getName());
		if (i==antialias)
			return;
		saved=false;
		antialias=i;
	}
	/**
	 * Gets the antialias state of the layer. The layer may ask for the default
	 * antialiasing mode (that of the viewer), may require it off or on.
	 * @return  An int, -1 for default, 0 for off and 1 for on.
	 */
	public int getAntialiasState() {
		return antialias;
	}
	/**
	 * Sets how the layer will be painted.
	 */
	public void setPaintMode(int pm) {
		paintAs=pm;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerPaintPropertiesChanged();
	}
	/**
	 * Gets the paint mode.
	 */
	public int getPaintMode() {
		return paintAs;
	}
	/**
	 * @return  <em>True</em>, if this layer is being used as a motion layer.
	 */
	protected boolean isMotionLayer() {
		return (motionInfo!=null);
	}
	/**
	 * If this layer is being used as a motion layer, this method returns the motion type.
	 * (For the return values look the gr.cti.eslate.protocol.ILayerView interface).
	 * If it's not it returns ILayerView.NOT_A_MOTION_LAYER.
	 */
	protected int getMotionType() {
		if (motionInfo==null)
			return ILayerView.NOT_A_MOTION_LAYER;
		else if (motionInfo instanceof RoadInfo)
			return ILayerView.ROAD_MOTION_LAYER;
		else if (motionInfo instanceof RailwayInfo)
			return ILayerView.RAILWAY_MOTION_LAYER;
		else if (motionInfo instanceof SeaInfo)
			return ILayerView.SEA_MOTION_LAYER;
		else if (motionInfo instanceof AirInfo)
			return ILayerView.AIR_MOTION_LAYER;
		else
			return ILayerView.CUSTOM_TYPE_MOTION_LAYER;
	}
	/**
	 * Returns the MotionInfo object attached to this layer. A MotionInfo object is an
	 * object that provides information to Agents moving on this layer.
	 */
	protected MotionInfo getMotionInfo() {
		return motionInfo;
	}
	/**
	 * Each motion layer has an ID. This ID is used by agents, who check it to identify
	 * if they can attach to the layer. If a layer is one of the predefined types, it
	 * gets a predefined ID. Otherwise the user must provide one.
	 */
	protected String getMotionID() {
		if (motionInfo!=null)
			return motionInfo.getID();
		else
			return null;
	}
	/**
	 * Sets the motion ID for this layer. The MotionInfo object changes accordingly.
	 */
	protected abstract void setMotionID(String id);
	/**
	 * This method increases the indicator which shows the need
	 * for this layer to stay in memory.
	 */
	protected void increaseNeed() {
		need++;
	}
	/**
	 * This method decreases the indicator which shows the need
	 * for this layer to stay in memory.
	 */
	protected void decreaseNeed() {
		if (need>0) {
			need--;
			if (need==0 && sf!=null && isSaved())
				freeUpResources();
		}
	}
	/**
	 * Frees up the memory hungry data.
	 */
	protected abstract void freeUpResources();
	/**
	 * Returns an id used in saving.
	 */
	public String getGUID() {
		return guid;
	}
	/**
	 * Used for saving.
	 */
	protected boolean isSaved() {
		return saved;
	}
	protected void destroying() {
		saved=true;
	}
	/**
	 * Changes the structure file from where the layer will request its data.
	 */
	protected void setSF(Access sf) {
		this.sf=sf;
	}
	/**
	 * Reads a layer from a structured stream.
	 */
	public abstract void readStream(Access sf,Map map) throws ClassNotFoundException,IOException;
	/**
	 * Writes a layer to a structured stream.
	 */
	public abstract void writeStream(Access sf) throws IOException;
	/**
	 * Creates a FieldMap with all the necessary layer information, which doesnot include shapes.
	 */
	StorageStructure layerAttributes() {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("guid",guid);
		ht.put("name",getName());
		if (labelBase!=null)
			ht.put("labelBase",labelBase.getName());
		if (tipBase!=null)
			ht.put("tipBase",tipBase.getName());
		if (fromBase!=null)
			ht.put("fromBase",fromBase.getName());
		if (toBase!=null)
			ht.put("toBase",toBase.getName());
		if (table!=null)
			ht.put("table",table.getTitle());
		ht.put("editable",editable);
		ht.put("defVisibility",defVisibility);
		ht.put("hidable",hideable);
		ht.put("hideUnselected",hideUnselected);
		ht.put("canHideUnselected",canHideUnselected);
		ht.put("canSelectObjects",canSelectObjects);
		ht.put("showBlankRecordObjects",showBlankRecordObjects);
		ht.put("paintAs",paintAs);
		ht.put("antialias",antialias);
		ht.put("motionID",getMotionID());
		ht.put("motionType",getMotionType());
		if (activeObject!=null)
			ht.put("activeObjectID",activeObject.getID());
		if (boundRect!=null) {
			double[] br=new double[]{boundRect.x,boundRect.y,boundRect.width,boundRect.height};
			ht.put("boundRect",br);
		}
		return ht;
	}
	/**
	 * Makes the database table associations when restoring layer attributes.
	 */
	void restoreTableAssociations(Table t,StorageStructure ht) {
		table=t;
		if (table!=null)
			try {
				if (defaultTMListener==null)
					defaultTMListener=new LayerTableListener();
				table.addTableModelListener(defaultTMListener);
				//Load labelbase field
				String s=(String) ht.get("labelBase");
				if (s==null)
					labelBase=null;
				else
					labelBase=table.getTableField(s);
				//Load tipbase field
				s=(String) ht.get("tipBase");
				if (s==null)
					tipBase=null;
				else
					tipBase=table.getTableField(s);
				//Load date from field
				s=(String) ht.get("fromBase");
				if (s==null)
					fromBase=null;
				else
					fromBase=table.getTableField(s);
				//Load date to field
				s=(String) ht.get("toBase");
				if (s==null)
					toBase=null;
				else
					toBase=table.getTableField(s);
			} catch(Exception e) {
				System.err.println("MAP#200004141510: Couldn't restore layer database associations. "+name);
				e.printStackTrace();
			}
	}
	/**
	 * Restores the layer attibutes which have been saved using <code>layerAttributes</code>.
	 */
	void restoreLayerAttributes(StorageStructure ht,Map map) {
		guid=ht.get("guid","0");
		setName(ht.get("name",""));
		//The map is null when the attributes are restored from an import operation.
		//During such an operation the table instance is saved and there is no need to
		//access the map database.
		if (map!=null && map.getDatabase()!=null)
			restoreTableAssociations(map.getDatabase().getTable((String)ht.get("table")),ht);
		editable=ht.get("editable",false);
		defVisibility=ht.get("defVisibility",true);
		hideable=ht.get("hidable",true);
		hideUnselected=ht.get("hideUnselected",false);
		canHideUnselected=ht.get("canHideUnselected",true);
		canSelectObjects=ht.get("canSelectObjects",true);
		showBlankRecordObjects=ht.get("showBlankRecordObjects",true);
		paintAs=ht.get("paintAs",paintAs);
		antialias=ht.get("antialias",-1);
		double[] br=(double[]) ht.get("boundRect");
		if (br!=null)
			boundRect=new Rectangle2D.Double(br[0],br[1],br[2],br[3]);
		String s=ht.get("motionID",(String) null);
		if (s!=null) {
			motionInfo=null;
			int mt=ht.get("motionType",ILayerView.NOT_A_MOTION_LAYER);
			if (mt==ILayerView.ROAD_MOTION_LAYER) {
				motionInfo=new RoadMotionInfo();
				motionInfo.setID(MotionInfo.MOTION_ROAD_KEY);
			} else if (mt==ILayerView.RAILWAY_MOTION_LAYER) {
				motionInfo=new RailwayMotionInfo();
				motionInfo.setID(MotionInfo.MOTION_RAILWAY_KEY);
			} else if (mt==ILayerView.SEA_MOTION_LAYER) {
				motionInfo=new SeaMotionInfo();
				motionInfo.setID(MotionInfo.MOTION_SEA_KEY);
			} else if (mt==ILayerView.AIR_MOTION_LAYER) {
				motionInfo=new AirMotionInfo();
				motionInfo.setID(MotionInfo.MOTION_AIR_KEY);
			} else if (mt==ILayerView.CUSTOM_TYPE_MOTION_LAYER) {
				motionInfo=new DefaultMotionInfo();
				motionInfo.setID(s);
			}
		}
		final int ao;
		if ((ao=ht.get("activeObjectID",-1))!=-1) {
//			if (this instanceof VectorLayer)
//				((VectorLayer) this).setPendingActiveGeographicObject(ao);
//			else
//				setActiveGeographicObject((GeographicObject) getGeographicObjects(false).get(ao));
		}
/*            SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setActiveGeographicObject((GeographicObject) getGeographicObjects(false).at(ao));
				}
			});*/
	}
	/**
	 * Import a layer from a file.
	 */
	static Layer importLayer(ObjectInputStream in) throws IOException,ClassNotFoundException {
		StorageStructure ht=(StorageStructure) in.readObject();
		int type=ht.get("layertype",-1);
		Layer layer=null;
		String newGUID=null;

		switch (type) {
			case ShapeFileInfo.POINT_LAYER:
				layer=new PointLayer();
				newGUID=PointLayer.TYPE_ID+layer.hashCode();
				break;
			case ShapeFileInfo.POLYGON_LAYER:
				layer=new PolygonLayer();
				newGUID=PolygonLayer.TYPE_ID+layer.hashCode();
				break;
			case ShapeFileInfo.POLYLINE_LAYER:
				layer=new PolyLineLayer();
				newGUID=PolyLineLayer.TYPE_ID+layer.hashCode();
				break;
			case ShapeFileInfo.RASTER_LAYER:
				layer=new RasterLayer();
				newGUID=RasterLayer.TYPE_ID+layer.hashCode();
			default:
				;
		}
		if (layer!=null) {
			layer.restoreLayerAttributes(ht,null);
			//%%%%MUST change the GUID! Otherwise, the imported layer will be treated as the
			//%%%%one from which it had been exported!
			layer.guid=newGUID;
			layer.restoreTableAssociations((Table) ht.get("tableinstance"),ht);
			if (layer instanceof VectorLayer) {
				if (((VectorLayer) layer).version>1)
					((VectorLayer) layer).loadObjects(in);
				else
					((VectorLayer) layer).loadOldObjects((ArrayList) ht.get("exportedobjects"));
			}
		}
		return layer;
	}
	/**
	 * Export a layer to a file.
	 */
	void exportLayer(ObjectOutputStream out) throws IOException {
		StorageStructure ht=layerAttributes();
		ht.put("tableinstance",table);
		out.writeObject(ht);
		//Write the layer data
		out.writeObject(new Integer(rtree.getFeatureCount()));
		ArrayList objs=getGeographicObjects(false);
		for (int i=0;i<objs.size();i++)
			out.writeObject(objs.get(i));
	}
	/**
	 * Used for saving.
	 */
	protected String guid;
	/**
	 * Used for saving.
	 */
	protected boolean saved;
	/**
	 * It is an indicator which shows how many objects need this layer.
	 * If it gets to 0, there is a structured file and the layer has not
	 * been modified, its data are removed from memory.
	 */
	int need;
	/**
	 * The structured file from where the layer will get its data when needed.
	 */
	protected Access sf;
	/**
	 * When loaded from disk it flags if the data exist or not.
	 */
	boolean loaded;
	/**
	 * This list holds all the different views of the map.
	 */
	ArrayList<LayerView> views;
	/**
	 * The Layer name.
	 */
	String name;
	/**
	 * The label field.
	 */
	AbstractTableField labelBase;
	/**
	 * The tip field.
	 */
	AbstractTableField tipBase;
	/**
	 * The date from field.
	 */
	AbstractTableField fromBase;
	/**
	 * The date to field.
	 */
	AbstractTableField toBase;
	/**
	 * A pointer to the associated table.
	 */
	Table table;
	/**
	 * Editable property.
	 */
	boolean editable;
	//Xrhsimopoieitai gia na elegxetai h default visibility tou epipedou se ena xarth meta th metabash se ena allo
	boolean defVisibility;
	boolean hideable,hideUnselected,canHideUnselected,canSelectObjects,showBlankRecordObjects;
	/**
	 * Defines how the layer will be painted.
	 */
	private int paintAs;
	/**
	 * The layer R-Tree. Made private in order to ensure that point etc Layers get proper R-Trees.
	 */
	protected RTree rtree;
	/**
	 * The active Geographic object.
	 */
	GeographicObject activeObject;
	GeographicObject oldActiveObject;
	/**
	 * Switch that prevents database events caused by this layer to be served again.
	 */
	boolean ignoreEvent;
	/**
	 * Switch that prevents active record changed events served by this layer to
	 * produce another active record changed event.
	 */
	private boolean ignoreActiveRecordChangedEvent;
	DatabaseTableModelListener defaultTMListener;
	/**
	 * Motion information object.
	 */
	protected MotionInfo motionInfo;
	/**
	 * This array keeps the objects that should be invisible, according to the showBlankRecordObjects property.
	 */
	int[] invisible;
	/**
	 * Keeps a backup of how many are the saved objects. To avoid loading just to see how many they are.
	 * MAX_VALUE is an indication that the variable is not initialized.
	 */
	int count;
	/**
	 * The bounding rectangle of the layer. Very useful in raster layers. Mostly undefined in vector layers.
	 */
	private Rectangle2D.Double boundRect;
	/**
	 * Label font.
	 */
	private Font labelFont;
	/**
	 * Label foreground color.
	 */
	private Color labelFore;
	/**
	 * Label background color.
	 */
	private Color labelBack;
	/**
	 * Antialias state.
	 */
	private int antialias;
	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
	//Variable initialization
	{
		need=0;
		saved=false;
		loaded=false;
		views=new ArrayList();
		name=Map.bundleMessages.getString("mylayer");
		defVisibility=true;
		hideable=true;
		hideUnselected=false;
		canHideUnselected=true;
		canSelectObjects=true;
		showBlankRecordObjects=true;
		ignoreEvent=false;
		ignoreActiveRecordChangedEvent=false;
		invisible=new int[0];
		count=Integer.MAX_VALUE;
		boundRect=new Rectangle2D.Double();
		antialias=-1;
	}

	class LayerTableListener extends DatabaseTableModelAdapter {
		public void activeRecordChanged(ActiveRecordChangedEvent e) {
			if (ignoreEvent) {
			//Event produced by the layer
				ignoreEvent=false;
				return;
			}
			GeographicObject toba;
			if (e.getActiveRecord()!=-1 && getGeographicObjects(false).size()>e.getActiveRecord())
				toba=(GeographicObject) getGeographicObjects(false).get(e.getActiveRecord());
			else
				toba=null;
			//An invisible object cannot be active for the map
			if (toba!=null) {
				if (hideUnselected)
					if (!toba.isSelected())
						toba=null;
				if (!showBlankRecordObjects)
					if (Arrays.binarySearch(invisible,e.getActiveRecord())>=0)
						toba=null;
			}
			ignoreActiveRecordChangedEvent=true;
			setActiveGeographicObject(toba);
			/*//Inform all Listeners.
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerActiveGeographicObjectChanged(oldActiveObject,activeObject);*/
		}

		public void selectedRecordSetChanged(SelectedRecordSetChangedEvent e) {
			if (ignoreEvent) {
			//Event produced by the layer
				ignoreEvent=false;
				return;
			}

			//Clear previous selection
			saved=false;
			for (int i=0;i<getGeographicObjects(false).size();i++)
				try {
					((GeographicObject) getGeographicObjects(false).get(i)).setSelected(false);
				} catch(ClassCastException e1) {/*Grrr! Who put something else in the array?*/}
			//Set new selection, if any
			IntBaseArrayDesc selected=table.getSelectedSubset();
			if (selected!=null)
				for (int i=0;i<selected.size();i++)
					try {
						((GeographicObject) getGeographicObjects(false).get(selected.get(i))).setSelected(true);
					} catch(IndexOutOfBoundsException e1) {
					} catch(ClassCastException e2) {/*Grrr! Who put something else in the array?*/}
					
			Rectangle2D.Double previous=null,selection=null;
			//Not so good tactic but I avoid method call and inheritance mess.
			if (Layer.this instanceof RasterLayer)
				((RasterLayer.RasterImage) ((RasterLayer) Layer.this).getRaster()).colorModel=((RasterLayer) Layer.this).tuneTransparency((java.awt.image.IndexColorModel) ((RasterLayer) Layer.this).getRaster().getColorModel());
			else if (Layer.this instanceof VectorLayer) {
				//Clear selection rectangle but keep backup
				previous=((VectorLayer) Layer.this).getSelectionBoundingRectangle();
				((VectorLayer) Layer.this).clearSelectionBoundingRectangle();
				//Create selection bounding rectangle
				if (selected!=null)
					for (int i=selected.size()-1;i>-1;i--) {
						try {
							VectorGeographicObject g=(VectorGeographicObject) getGeographicObjects(false).get(selected.get(i));
							((VectorLayer) Layer.this).addSelectionRectangle(g.getBoundingMinX(),g.getBoundingMinY(),g.getBoundingMaxX(),g.getBoundingMaxY());
						} catch(IndexOutOfBoundsException e1) {}
					}
				selection=((VectorLayer) Layer.this).getSelectionBoundingRectangle();
			}

			//Inform all Listeners.
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerSelectionChanged(previous,selection,false);
		}

		public void recordRemoved(RecordRemovedEvent e) {
			if (ignoreEvent) {
			//Event produced by the layer
				ignoreEvent=false;
				return;
			}
			//Serve only when a record referring to an object is removed
			if (e.getRecordIndex()<getGeographicObjects(false).size())
				try {
					saved=false;
					__removeObject((GeographicObject) getGeographicObjects(false).get(e.getRecordIndex()));
				} catch(CannotRemoveObjectException ex) {
					System.err.println("MAP#200003311630: Object removal caused by database event failed.");
				}
			//IT DOESN'T WORK
			//if (e.getRecordIndex()==activeObject.getID())
			//    setActiveGeographicObject(null);
		}

		public void columnRemoved(ColumnRemovedEvent e) {
			//Reset the labelBase or tipBase if the field removed is one of the two
			if ((labelBase!=null) && (labelBase.getName().equals(e.getColumnName()))) {
				saved=false;
				AbstractTableField old=labelBase;
				labelBase=null;
				//Inform all Listeners.
				for (int i=0;i<views.size();i++)
					((LayerView) views.get(i)).brokerLayerLabelBaseChanged(old,null);
			}
			if ((tipBase!=null) && (tipBase.getName().equals(e.getColumnName()))) {
				saved=false;
				AbstractTableField old=tipBase;
				tipBase=null;
				//Inform all Listeners.
				for (int i=0;i<views.size();i++)
					((LayerView) views.get(i)).brokerLayerTipBaseChanged(old,null);
			}
			if ((fromBase!=null) && (fromBase.getName().equals(e.getColumnName()))) {
				saved=false;
				AbstractTableField old=fromBase;
				fromBase=null;
				//Inform all Listeners.
				for (int i=0;i<views.size();i++)
					((LayerView) views.get(i)).brokerLayerDateFromBaseChanged(old,null);
			}
			if ((toBase!=null) && (toBase.getName().equals(e.getColumnName()))) {
				saved=false;
				AbstractTableField old=toBase;
				toBase=null;
				//Inform all Listeners.
				for (int i=0;i<views.size();i++)
					((LayerView) views.get(i)).brokerLayerDateToBaseChanged(old,null);
			}
		}

		public void cellValueChanged(CellValueChangedEvent e) {
			if (ignoreEvent) {
			//Event produced by the layer
				ignoreEvent=false;
				return;
			}
			//If showBlankRecordObjects is disabled, look if there is a change in the invisible set.
			try {
				if (!showBlankRecordObjects && !table.getTableField(e.getColumnName()).isHidden()) {
					Object newValue=table.getTableField(e.getColumnName()).getCellObject(e.getRecordIndex());
					if (e.getOldValue()==null && newValue!=null && !newValue.equals("")) {
						int[] tmp=new int[invisible.length-1];
						int ins=Arrays.binarySearch(invisible,e.getRecordIndex());
						if (ins>=0) {
							System.arraycopy(invisible,0,tmp,0,ins);
							System.arraycopy(invisible,ins+1,tmp,ins,invisible.length-ins-1);
							invisible=tmp;
							//Inform all Listeners
							for (int i=0;i<views.size();i++)
								((LayerView) views.get(i)).brokerLayerObjectVisibilityChanged();
						}

					} else if (e.getOldValue()!=null && !e.getOldValue().equals("") && (newValue==null || newValue.equals(""))) {
						updateInvisibleArray();
						//Inform all Listeners
						for (int i=0;i<views.size();i++)
							((LayerView) views.get(i)).brokerLayerObjectVisibilityChanged();
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			//Reset the labelBase, fromBase, toBase or iconbase if the field edited is one of the four
			boolean broker=false;
			if ((labelBase!=null) && (labelBase.getName().equals(e.getColumnName())))
				broker=true;
			if ((fromBase!=null) && (fromBase.getName().equals(e.getColumnName())))
				broker=true;
			if ((toBase!=null) && (toBase.getName().equals(e.getColumnName())))
				broker=true;
			if (Layer.this instanceof PointLayer) {
				PointLayer pl=(PointLayer) Layer.this;
				if ((pl.getIconBase()!=null) && (pl.getIconBase().getName().equals(e.getColumnName())))
					broker=true;
			}
			if (broker)
				//Inform all Listeners.
				for (int i=0;i<views.size();i++)
					((LayerView) views.get(i)).brokerLayerGeographicObjectPropertiesChanged((GeographicObject) rtree.getGeographicObjects().get(e.getRecordIndex()));
		}

		public void tableRenamed(TableRenamedEvent e) {
			saved=false;
		}

		public void columnRenamed(ColumnRenamedEvent e) {
			if ((labelBase!=null) && (labelBase.getName().equals(e.getNewName())))
				saved=false;
			if ((tipBase!=null) && (tipBase.getName().equals(e.getNewName())))
				saved=false;
			if ((fromBase!=null) && (fromBase.getName().equals(e.getNewName())))
				saved=false;
			if ((toBase!=null) && (toBase.getName().equals(e.getNewName())))
				saved=false;
		}
	}

	/**
	 * This is the default class that returns motion information.
	 */
	public class DefaultMotionInfo implements MotionInfo {
		private String id;
		/**
		 * @ return Given an x,y location this function returns the location of the nearest entry point, if any. <em>Null</em> if no entry points are defined.
		 */
		public java.awt.geom.Point2D.Double getEntryPointLocation(double x,double y) {
			return null;
		}
		/**
		 * Given an x,y location this function returns a non-null value if it is a valid position.
		 * This way the implementor of the interface can define special geographic areas such as roads
		 * and sea, and answer e.g. if a given point is in or out of this area. The returned value
		 * is an object on which the motion will take place.
		 */
		public boolean isValidLocation(double x,double y,double tolerance) {
			Rectangle2D.Double r=new Rectangle2D.Double(x-tolerance,y-tolerance,2*tolerance,2*tolerance);
			return ((rtree.search(r.x,r.y,r.x+r.width,r.y+r.height,r)).size()!=0);
		}
		/**
		 * Given a start and end x,y location this function returns a non-null value
		 * with information about the trip (validity, snappings etc).
		 * This way the implementor of the interface can define special geographic areas such as roads
		 * and sea, and answer e.g. if a given point is in or out of this area. The returned value
		 * is an object on which the motion will take place.
		 */
		public MotionFeatures isValidTrip(double startLongt,double startLat,double endLongt,double endLat,double tolerance) {
			MotionFeatures mf=new MotionFeatures();
			mf.isValidTrip=false;
			if (Layer.this instanceof PolygonLayer) {
				Rectangle2D.Double r=new Rectangle2D.Double(startLongt-tolerance,startLat-tolerance,2*tolerance,2*tolerance);
				ArrayList arr=getGeographicObjects(r,r,true);
				if (arr.size()>0)
					mf.onObject=(GeographicObject) arr.get(0);
				r=new Rectangle2D.Double(endLongt-tolerance,endLat-tolerance,2*tolerance,2*tolerance);
				arr=getGeographicObjects(r,r,true);
				mf.isValidTrip=mf.onObject!=null && arr.size()>0;
				mf.startLongt=startLongt;
				mf.startLat=startLat;
				mf.endLongt=endLongt;
				mf.endLat=endLat;
			} else if (Layer.this instanceof PolyLineLayer) {
				Rectangle2D.Double r=new Rectangle2D.Double(startLongt-tolerance,startLat-tolerance,2*tolerance,2*tolerance);
				ArrayList start=getGeographicObjects(r,r,true);
				r=new Rectangle2D.Double(endLongt-tolerance,endLat-tolerance,2*tolerance,2*tolerance);
				ArrayList end=getGeographicObjects(r,r,true);
				ArrayList common=new ArrayList();
				for (int i=0;i<start.size();i++)
					if (end.contains(start.get(i)))
						common.add(start.get(i));

				GeographicObject closest=null;
				double dist=Double.MAX_VALUE;
				for (int i=0;i<common.size();i++) {
					LineAspects d=((PolyLine) common.get(i)).calculateDistanceAndSnap(new Point2D.Double(endLongt,endLat));
					if (d.distance<dist) {
						mf.isValidTrip=true;
						dist=d.distance;
						mf.onObject=(GeographicObject) common.get(i);
						mf.endLongt=d.snapX;
						mf.endLat=d.snapY;
						mf.endSegment=d.segment;
						d=((PolyLine) common.get(i)).calculateDistanceAndSnap(new Point2D.Double(startLongt,startLat));
						mf.startLongt=d.snapX;
						mf.startLat=d.snapY;
						mf.startSegment=d.segment;
					}
				}
			}
			return mf;
/*			MotionFeatures mf=new MotionFeatures();
			mf.isValidTrip=false;
			if (Layer.this instanceof PolygonLayer) {
				Rectangle2D.Double r=new Rectangle2D.Double(startLongt-tolerance,startLat-tolerance,2*tolerance,2*tolerance);
				ArrayList arr=getGeographicObjects(r,r,true);
				if (arr.size()>0)
					mf.onObject=(GeographicObject) arr.get(0);
				r=new Rectangle2D.Double(endLongt-tolerance,endLat-tolerance,2*tolerance,2*tolerance);
				arr=getGeographicObjects(r,r,true);
				mf.isValidTrip=mf.onObject!=null && arr.size()>0;
				mf.startLongt=startLongt;
				mf.startLat=startLat;
				mf.endLongt=endLongt;
				mf.endLat=endLat;
			} else if (Layer.this instanceof PolyLineLayer) {
				Rectangle2D.Double r=new Rectangle2D.Double(startLongt-tolerance,startLat-tolerance,2*tolerance,2*tolerance);
				ArrayList start=getGeographicObjects(r,r,true);
				ArrayList common;
				//When agent moving without knowing the end.
				if (startLongt==endLongt && startLat==endLat) {
					common=start;
					double less=Double.MAX_VALUE; PolyLine lessP=null;
					for (int i=0;i<common.size();i++) {
						PolyLine pol=(PolyLine) common.get(i);
						Point2D st=pol.getPoint(0);
						Point2D en=pol.getPoint(pol.getNumberOfPoints()-1);
						double angle=Math.atan(st.getY()/st.getX());
						angle=(angle-Math.PI/2)*180/Math.PI;
						if (angle<0 || angle>360) {
							if (angle<0)
								for (;angle<0 || angle>360;)
									angle+=360;
							else
								for (;angle<0 || angle>360;)
									angle-=360;
						}
double heading=0;						
						if (Math.abs(angle-heading)<less) {
							less=Math.abs(angle-heading);
							lessP=pol;
						}
					}
					mf.onObject=lessP;
					Point2D st=lessP.getPoint(0);
					Point2D en=lessP.getPoint(lessP.getNumberOfPoints()-1);
					if (st.distance(startLongt,startLat)>en.distance(startLongt,startLat)) {
						mf.startLongt=en.getX();
						mf.startLat=en.getY();
						mf.startSegment=lessP.getNumberOfPoints();
						mf.endLongt=st.getX();
						mf.endLat=st.getY();
						mf.endSegment=0;
					} else {
						mf.startLongt=st.getX();
						mf.startLat=st.getY();
						mf.startSegment=0;
						mf.endLongt=en.getX();
						mf.endLat=en.getY();
						mf.endSegment=lessP.getNumberOfPoints();
					}
					return mf;
				} else {
					r=new Rectangle2D.Double(endLongt-tolerance,endLat-tolerance,2*tolerance,2*tolerance);
					ArrayList end=getGeographicObjects(r,r,true);
					common=new ArrayList();
					for (int i=0;i<start.size();i++)
						if (end.contains(start.get(i)))
							common.add(start.get(i));
				}
				GeographicObject closest=null;
				double dist=Double.MAX_VALUE;
				for (int i=0;i<common.size();i++) {
					//When agent moving without knowing the end.
					LineAspects d=((PolyLine) common.get(i)).calculateDistanceAndSnap(new Point2D.Double(endLongt,endLat));
					if (d.distance<dist) {
						mf.isValidTrip=true;
						dist=d.distance;
						mf.onObject=(GeographicObject) common.get(i);
						mf.endLongt=d.snapX;
						mf.endLat=d.snapY;
						mf.endSegment=d.segment;
						d=((PolyLine) common.get(i)).calculateDistanceAndSnap(new Point2D.Double(startLongt,startLat));
						mf.startLongt=d.snapX;
						mf.startLat=d.snapY;
						mf.startSegment=d.segment;
//						PolyLine pol=(PolyLine) common.get(i);
//						if (mf.onObject!=null && mf.onObject!=pol) {
//							if (mf.startSegment==0) {
//								Point2D pp=pol.getPoint(pol.getNumberOfPoints()-1);
//								mf.endLongt=pp.getX();
//								mf.endLat=pp.getY();
//								mf.endSegment=pol.getNumberOfPoints()-1;
//							} else {
//								Point2D pp=pol.getPoint(0);
//								mf.startLongt=pp.getX();
//								mf.startLat=pp.getY();
//								mf.startSegment=0;
//							}
//						}
					}
				}
			}
			return mf;
*/		}
		/**
		 * The ID of the motion layer. May be one the predefined values or a custom one.
		 */
		public void setID(String id) {
			this.id=id;
		}
		/**
		 * The ID of the motion layer. May be one the predefined values or a custom one.
		 */
		public String getID() {
			return id;
		}
	}
	/**
	 * This is the default class that returns road information.
	 */
	public class RoadMotionInfo extends DefaultMotionInfo implements RoadInfo {
		public GeographicObject getRoadLine(double x,double y) {
			//TODO: Implement
			return null;
		}
		/**
		 * @return All the roads that are adjacent to the given point. Used to decide on crossroads.
		 */
		public GeographicObject[] getAdjacentRoads(double x,double y) {
			//TODO: Implement
			return null;
		}
	}
	/**
	 * This is the default class that returns railway information.
	 */
	public class RailwayMotionInfo extends DefaultMotionInfo implements RailwayInfo {
		public GeographicObject getRailwayLine(double x,double y) {
			//TODO: Implement
			return null;
		}
		/**
		 * @return All the railroad that are adjacent to the given point. Used to decide on crossings.
		 */
		public GeographicObject[] getAdjacentRailways(double x,double y) {
			//TODO: Implement
			return null;
		}
	}
	/**
	 * This is the default class that returns sea information.
	 */
	public class SeaMotionInfo extends DefaultMotionInfo implements SeaInfo {
		/**
		 * @ return Given the <em>depth</em> this function returns <em>true</em> if it is posible to dive in this depth.
		 * (and don't crash on a coral!)
		 */
		public boolean isValidDepth(double depth) {
			//Currently no depth information are available
			return true;
		}
	}
	/**
	 * This is the default class that returns airway information.
	 */
	public class AirMotionInfo extends DefaultMotionInfo implements AirInfo {
		/**
		 * @ return Given the <em>height</em> this function returns <em>true</em> if it is posible to fly in this height.
		 * (and don't crash on a mountain!)
		 */
		public boolean isValidHeight(double height) {
			//Currently no height information are available
			return true;
		}
	}
////////////////////////ADDED CODE BY N\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	public boolean shouldGeographicObjectBeVisible(GeographicObject go){
	  return shouldBeVisible(go);
	}

/////////////////////////////////////////////////////////////////////////////
}
