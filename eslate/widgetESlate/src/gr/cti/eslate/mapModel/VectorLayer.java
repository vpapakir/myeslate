package gr.cti.eslate.mapModel;

import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.mapModel.geom.PolyLine;
import gr.cti.eslate.mapModel.geom.VectorGeographicObject;
import gr.cti.eslate.protocol.CannotAddObjectException;
import gr.cti.eslate.protocol.CannotRemoveObjectException;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IncompatibleObjectTypeException;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * An implementation of a vector-type layer. Vector layers contain geographical objects which are
 * represented in a vector format, thus making "infinite" zooming possible.
 *
 * @author Giorgos Vasiliou
 * @version 1.0.0
 */

public abstract class VectorLayer extends Layer {
	/**
	 * Sets the layer table.
	 */
	public void setTable(Table table) throws InvalidLayerDataException {
		if ((rtree!=null) && (table!=null) && (rtree.getFeatureCount()>table.getRecordCount()))
			throw new InvalidLayerDataException("Invalid Table.");
		//No need to mess up when the table is the same. Ignore the call.
		if ((this.table==null && table==null) || (this.table!=null && this.table.equals(table)))
			return;
		super.setTable(table);
	}
	/**
	 * Sets the layer R-Tree.
	 * @exception InvalidLayerDataException Thrown when the R-Tree is null, or when the table record cound and the R-Tree feature count don't match.
	 */
	protected void setRTree(RTree rtree) throws InvalidLayerDataException {
		if ((rtree!=null) && (table!=null) && (rtree.getFeatureCount()>table.getRecordCount()))
			throw new InvalidLayerDataException("Invalid Table.");
		else if (rtree==null)
			throw new InvalidLayerDataException("Null R-Tree.");
		this.rtree=rtree;
		activeObject=oldActiveObject=null;
		pendingActive=-1;
		saved=false;
	}
	/**
	 * Sets the layer R-Tree.
	 * @rtree The R-Tree.
	 * @table The associated table.
	 * @exception InvalidLayerDataException Thrown when the table is null, or when the table record cound and the R-Tree feature count don't match.
	 */
	protected void setRTree(RTree rtree,Table table) throws InvalidLayerDataException {
		if ((rtree!=null) && (table!=null) && (rtree.getFeatureCount()>table.getRecordCount()))
			throw new InvalidLayerDataException("Invalid Table.");
		else if (rtree==null)
			throw new InvalidLayerDataException("Null R-Tree.");
		this.rtree=rtree;
		activeObject=oldActiveObject=null;
		pendingActive=-1;
		//Remove previous listener, if any.
		if (this.table!=null)
			this.table.removeTableModelListener(defaultTMListener);

		this.table=table;
		if (table!=null) {
			if (defaultTMListener==null)
				defaultTMListener=new LayerTableListener();
			table.addTableModelListener(defaultTMListener);
			//Set the active object. Useful for the first time shown, if an object is already selected.
			if (table.getActiveRecord()!=-1) {
				oldActiveObject=null;
				activeObject=(GeographicObject) getGeographicObjects(false).get(table.getActiveRecord());
			} else {
				oldActiveObject=null;
				activeObject=null;
			}
		} else {
			oldActiveObject=null;
			activeObject=null;
		}
		saved=false;
	}
	/**
	 * Sets the pending active object when a layer is not loaded. It will be actually set
	 * as soon as the layer is loaded.
	 */
	void setPendingActiveGeographicObject(int id) {
		if (loaded)
			setActiveGeographicObject((GeographicObject)getGeographicObjects(false).get(id));
		else
			pendingActive=id;
	}
	/**
	 * Sets the active geographic object.
	 */
	protected void setActiveGeographicObject(GeographicObject active) {
		super.setActiveGeographicObject(active);
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerActiveGeographicObjectChanged(oldActiveObject,activeObject);
	}
	/**
	 * Adds a rectangle to the selection bounding rectangle.
	 */
	protected void addSelectionRectangle(double x1,double y1,double x2,double y2) {
		if (selection==null)
			selection=new Rectangle.Double(x1,y1,x2-x1,y2-y1);
		else {
			double x=Math.min(x1,selection.getX()); double y=Math.min(y1,selection.getY());
			selection.setRect(x,y,Math.max(x2,selection.getWidth()+selection.getX())-x,Math.max(y2,selection.getHeight()+selection.getY())-y);
		}
	}
	/**
	 * Gets the selection bounding rectangle. If null, no selection is available.
	 */
	protected Rectangle2D.Double getSelectionBoundingRectangle() {
		return selection;
	}
	/**
	 * Clears the selection bounding rectangle.
	 */
	protected void clearSelectionBoundingRectangle() {
		selection=null;
	}
	/**
	 * Sets the selection.
	 * @return  The IDs pf the objects that where actually selected.
	 */
	protected void setSelectedGeographicObjects(ArrayList selected,boolean hasMore) {
		if (rtree==null)
			return;
		super.setSelectedGeographicObjects(selected,hasMore);
		//Clear selection rectangle but keep backup
		Rectangle2D.Double previous=selection;
		selection=null;
		//Create selection bounding rectangle
		if (selected!=null)
			for (int i=selected.size()-1;i>-1;i--) {
				VectorGeographicObject g=(VectorGeographicObject) selected.get(i);
				addSelectionRectangle(g.getBoundingMinX(),g.getBoundingMinY(),g.getBoundingMaxX(),g.getBoundingMaxY());
			}
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerSelectionChanged(previous,selection,hasMore);
	}
	/**
	 * Deselects the given objects. Other selected objects remain unchanged.
	 * @return  The IDs pf the objects that where actually selected.
	 */
	protected void deselectGeographicObjects(ArrayList desel) {
		deselectGeographicObjects(desel,false);
	}
	/**
	 * Deselects the given objects. Other selected objects remain unchanged.
	 * @return  The IDs pf the objects that where actually selected.
	 */
	protected void deselectGeographicObjects(ArrayList desel,boolean hasMore) {
		super.deselectGeographicObjects(desel);
		//Clear selection rectangle but keep backup
		Rectangle2D.Double previous=selection;
		selection=null;
		//Create selection bounding rectangle, must check ALL objects
		for (int i=rtree.getGeographicObjects().size()-1;i>-1;i--) {
			VectorGeographicObject g=(VectorGeographicObject) rtree.getGeographicObjects().get(i);
			addSelectionRectangle(g.getBoundingMinX(),g.getBoundingMinY(),g.getBoundingMaxX(),g.getBoundingMaxY());
		}
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerSelectionChanged(previous,selection,hasMore);
	}
	/**
	 * Expands the selection. If an object in the selected array is already selected,
	 * it is turned to not selected.
	 */
	protected void addToSelectedGeographicObjects(ArrayList selected,boolean hasMore) {
		super.addToSelectedGeographicObjects(selected,hasMore);
		//Keep selection rectangle but make a backup
		Rectangle2D.Double previous;
		if (selection==null)
			previous=null;
		else
			previous=new Rectangle2D.Double(selection.getX(),selection.getY(),selection.getWidth(),selection.getHeight());
		//Create selection bounding rectangle
		if (selected!=null)
			for (int i=selected.size()-1;i>-1;i--) {
				VectorGeographicObject g=(VectorGeographicObject) selected.get(i);
				addSelectionRectangle(g.getBoundingMinX(),g.getBoundingMinY(),g.getBoundingMaxX(),g.getBoundingMaxY());
			}
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerSelectionChanged(previous,selection,hasMore);
	}
	/**
	 * All the layer data.
	 */
	protected ArrayList getGeographicObjects(boolean constrain) {
		checkNload();
		if (rtree==null)
			return new ArrayList();
		if (!constrain || (!hideUnselected && showBlankRecordObjects))
			return rtree.getGeographicObjects();
		else
			return applyConstraints(rtree.getGeographicObjects());
	}
	/**
	 * Get the objects that their bounding rectangle resides into or intersects with the given rectangle.
	 */
	protected ArrayList getGeographicObjects(java.awt.geom.Rectangle2D rectangle,boolean constrain) {
		checkNload();
		if (rtree==null)
			return new ArrayList();
		if (!constrain || (!hideUnselected && showBlankRecordObjects))
			return rtree.search(rectangle.getX(),rectangle.getY(),rectangle.getX()+rectangle.getWidth(),rectangle.getY()+rectangle.getHeight());
		else
			return applyConstraints(rtree.search(rectangle.getX(),rectangle.getY(),rectangle.getX()+rectangle.getWidth(),rectangle.getY()+rectangle.getHeight()));
	}
	/**
	 * Get the objects that their bounding rectangle resides into or intersects with the given rectangle
	 * and the "hotspot" rectangle intersects with the actual shape.
	 */
	protected ArrayList getGeographicObjects(java.awt.geom.Rectangle2D rectangle,Shape hotspot,boolean constrain) {
		checkNload();
		if (rtree==null)
			return new ArrayList();
		ArrayList a=rtree.search(rectangle.getX(),rectangle.getY(),rectangle.getX()+rectangle.getWidth(),rectangle.getY()+rectangle.getHeight(),hotspot);
		if (!constrain || (!hideUnselected && showBlankRecordObjects))
			return a;
		else
			return applyConstraints(a);
	}
	/**
	 * Checks if the layer geographic data are loaded. Loads if not.
	 */
	void checkNload() {
		if (!loaded && (sf!=null)) {
			//Read the layer data
			try {
				sf.changeToRootDir();
				sf.changeDir("layers");
				sf.changeDir(getGUID());
				if (version>1) {
					loadObjects(new ObjectInputStream(new BufferedInputStream(sf.openInputFile("layerdata"))));
				} else {
					Object arr=(new ObjectInputStream(new BufferedInputStream(sf.openInputFile("layerdata")))).readObject();
					if (arr instanceof ArrayList) {
						loadOldObjects((ArrayList) arr);
					} else {
						//Convert Array to ArrayList
						com.objectspace.jgl.Array arr1=(com.objectspace.jgl.Array) arr;
						ArrayList arr2=new ArrayList(arr1.size());
						for (int i=0;i<arr1.size();i++)
							arr2.add(arr1.at(i));
						loadOldObjects(arr2);
					}
				}
				//Make sure that the selection rectangle initializes correctly after loading
				//If the layer was loaded previously, the selection is not changed
				if (selection==null) {
					//Create selection bounding rectangle
					for (int i=rtree.getGeographicObjects().size()-1;i>-1;i--) {
						VectorGeographicObject g=(VectorGeographicObject) rtree.getGeographicObjects().get(i);
						if (g.isSelected())
							addSelectionRectangle(g.getBoundingMinX(),g.getBoundingMinY(),g.getBoundingMaxX(),g.getBoundingMaxY());
					}
				}
			} catch(ClassNotFoundException e) {
				System.err.println("MAP#200004172006: Could not load layer data.");
				e.printStackTrace();
			} catch(IOException e) {
				System.err.println("MAP#200004171958: Could not load layer data.");
				e.printStackTrace();
			}
		}
		if (pendingActive>-1) {
			setActiveGeographicObject((GeographicObject)rtree.getGeographicObjects().get(pendingActive));
			pendingActive=-1;
		}
		if (sf==null)
			loaded=true;
	}

	void loadObjects(ObjectInputStream in) throws IOException,ClassNotFoundException {
		if (in==null)
			throw new IOException("Null objects to import!");
		int objs=((Integer) in.readObject()).intValue();
		for (int i=0;i<objs;i++) {
			IVectorGeographicObject go=(IVectorGeographicObject) in.readObject();
			try {
				rtree.insert(go.getBoundingMinX(),go.getBoundingMinY(),go.getBoundingMaxX(),go.getBoundingMaxY(),go);
			} catch(IncompatibleObjectTypeException e) {
				System.err.println("MAP#2002051720000: Error reconstructing layer.");
			}
		}
		loaded=true;
		//If some of the objects should be invisible, update the array.
		updateInvisibleArray();
	}

	void loadOldObjects(ArrayList objs) throws IOException {
		if (objs==null)
			throw new IOException("Null objects to import!");
		for (int i=0;i<objs.size();i++) {
			IVectorGeographicObject go=(IVectorGeographicObject) objs.get(i);
			IVectorGeographicObject newgo=null;
			if (go instanceof point)
				newgo=new gr.cti.eslate.mapModel.geom.Point.Double(((point) go).getX(),((point) go).getY(),((point) go).getID());
			else if (go instanceof polyLine) {
				polyLine p=(polyLine) go;
				double[] points=new double[2*p.getNumberOfSegments()];
				int[] parts=new int[p.getNumberOfParts()];
				PathIterator pi=p.getPathIterator(null);
				double[] cords=new double[6];
				int o=0; int ip=0;
				while (o<points.length) {
					int type=pi.currentSegment(cords);
					points[o++]=cords[0]; points[o++]=cords[1];
					if (type==PathIterator.SEG_MOVETO)
						parts[ip++]=o/2-1;
					pi.next();
				}
				newgo=new PolyLine.Double(p.getID(),parts,points,p.getBoundingMinX(),p.getBoundingMinY(),p.getBoundingMaxX(),p.getBoundingMaxY());
			} else if (go instanceof polygon) {
				polygon p=(polygon) go;
				double[] points=new double[2*p.getNumberOfSegments()];
				int[] parts=new int[p.getNumberOfParts()];
				PathIterator pi=p.getPathIterator(null);
				double[] cords=new double[6];
				int o=0; int ip=0;
				while (!pi.isDone()) {
					int type=pi.currentSegment(cords);
					points[o++]=cords[0]; points[o++]=cords[1];
					if (type==PathIterator.SEG_MOVETO)
						parts[ip++]=o/2-1;
					pi.next();
				}
				newgo=new gr.cti.eslate.mapModel.geom.Polygon.Double(p.getID(),parts,points,p.getBoundingMinX(),p.getBoundingMinY(),p.getBoundingMaxX(),p.getBoundingMaxY());
			} else
				System.err.println("MAP#200205170142: Error reconstructing layer.");
			try {
				rtree.insert(newgo.getBoundingMinX(),newgo.getBoundingMinY(),newgo.getBoundingMaxX(),newgo.getBoundingMaxY(),newgo);
			} catch(IncompatibleObjectTypeException e) {
				System.err.println("MAP#2000041720000: Error reconstructing layer.");
			}
		}
		loaded=true;
		saved=false;
		//If some of the objects should be invisible, update the array.
		updateInvisibleArray();
	}
	/**
	 * Whenever a VectorLayer is created, this method is called so that for the timers
	 * of the Layer are passed to it.
	 * @param map
	 */
	private PerformanceTimer checkNLoadTimer, checkNLoadChangeDirTimer, checkNLoadReadObjectTimer, checkNLoadLoadObjectsTimer;
	void setTimers(Map map) {
		checkNLoadTimer = map.checkNLoadTimer;
		checkNLoadChangeDirTimer = map.checkNLoadChangeDirTimer;
		checkNLoadReadObjectTimer = map.checkNLoadReadObjectTimer;
		checkNLoadLoadObjectsTimer = map.checkNLoadLoadObjectsTimer;
	}
	/**
	 * Gets the number of objects in the layer.
	 */
	public int getObjectCount() {
		//Return the cached value
		if (loaded==false && count!=Integer.MAX_VALUE)
			return count;
		increaseNeed();
		checkNload();
		if (rtree==null) {
			decreaseNeed();
			return 0;
		}
		decreaseNeed();
		return getGeographicObjects(false).size();
	}
	//////////GETTER-SETTER METHODS//////////
	/**
	 * Sets fill color.
	 * @param color The new color.
	 */
	public void setNormalFillColor(Color color) {
		//Update only when there is a change.
		if (((color!=null) && (color.equals(normalFillColor))) || ((color==null) && (normalFillColor==null)))
			return;
		normalFillColor=color;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerColoringChanged();
	}
	/**
	 * Gets fill color.
	 * @return The color.
	 */
	public Color getNormalFillColor() {
		return normalFillColor;
	}
	/**
	 * Sets outline color.
	 * @color The new color.
	 */
	public void setNormalOutlineColor(Color color) {
		//Update only when there is a change.
		if (((color!=null) && (color.equals(normalOutlineColor))) || ((color==null) && (normalOutlineColor==null)))
			return;
		normalOutlineColor=color;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerColoringChanged();
	}
	/**
	 * Gets outline color.
	 * @return The color.
	 */
	public Color getNormalOutlineColor() {
		return normalOutlineColor;
	}
	/**
	 * Sets selected outline color.
	 * @color The new color.
	 */
	public void setSelectedOutlineColor(Color color) {
		//Update only when there is a change.
		if (((color!=null) && (color.equals(selectedOutlineColor))) || ((color==null) && (selectedOutlineColor==null)))
			return;
		selectedOutlineColor=color;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerColoringChanged();
	}
	/**
	 * Gets selected outline color.
	 * @return The color.
	 */
	public Color getSelectedOutlineColor() {
		return selectedOutlineColor;
	}
	/**
	 * Sets selected fill color.
	 * @color The new color.
	 */
	public void setSelectedFillColor(Color color) {
		//Update only when there is a change.
		if (((color!=null) && (color.equals(selectedFillColor))) || ((color==null) && (selectedFillColor==null)))
			return;
		selectedFillColor=color;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerColoringChanged();
	}
	/**
	 * Gets selected fill color.
	 * @return The color.
	 */
	public Color getSelectedFillColor() {
		return selectedFillColor;
	}
	/**
	 * Sets highlight outline color.
	 * @color The new color.
	 */
	public void setHighlightedOutlineColor(Color color) {
		//Update only when there is a change.
		if (((color!=null) && (color.equals(highlightedOutlineColor))) || ((color==null) && (highlightedOutlineColor==null)))
			return;
		highlightedOutlineColor=color;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerColoringChanged();
	}
	/**
	 * Gets highlight outline color.
	 * @return The color.
	 */
	public Color getHighlightedOutlineColor() {
		return highlightedOutlineColor;
	}
	/**
	 * Sets highlight fill color.
	 * @color The new color.
	 */
	public void setHighlightedFillColor(Color color) {
		//Update only when there is a change.
		if (((color!=null) && (color.equals(highlightedFillColor))) || ((color==null) && (highlightedFillColor==null)))
			return;
		highlightedFillColor=color;
		saved=false;
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerColoringChanged();
	}
	/**
	 * Gets highlight fill color.
	 * @return The color.
	 */
	public Color getHighlightedFillColor() {
		return highlightedFillColor;
	}
	/**
	 * Tries to add an object to the layer.
	 */
	protected void addObject(double tlX,double tlY,double brX,double brY,GeographicObject go) throws IncompatibleObjectTypeException,CannotAddObjectException {
		if (!editable)
			throw new CannotAddObjectException("Layer "+name+" is not editable.");
		try {
			rtree.insert(tlX,tlY,brX,brY,go);
			try {
				if ((table!=null) && (rtree!=null) && (rtree.getFeatureCount()>table.getRecordCount())) {
					table.getRecordEntryStructure().startRecordEntry();
					table.getRecordEntryStructure().commitNewRecord(false);
				}
			} catch(Exception e) {
				System.err.println("MAP#200003211719: Cannot add record to the layer table: "+name);
				e.printStackTrace();
			}
			saved=false;
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerGeographicObjectAdded(go);
		} catch(Exception e) {
			throw new CannotAddObjectException("Unknown problem with Layer "+name+" R-Tree.");
		}
	}
	/**
	 * Tries to add an object to the layer and sets a field value in the newly created record in the table.
	 */
	public void addObject(double tlX,double tlY,double brX,double brY,GeographicObject go,String fieldName,Object value) throws IncompatibleObjectTypeException,CannotAddObjectException {
		if (!editable)
			throw new CannotAddObjectException("Layer "+name+" is not editable.");
		try {
			rtree.insert(tlX,tlY,brX,brY,go);
			try {
				if (table!=null) {
					if ((rtree!=null) && (rtree.getFeatureCount()>table.getRecordCount())) {
						table.getRecordEntryStructure().startRecordEntry();
						table.getRecordEntryStructure().commitNewRecord(false);
					}
					if (value!=null) {
						ignoreEvent=true;
						table.setCell(fieldName,go.getID(),value);
					}
				}
			} catch(Exception e) {
				System.err.println("MAP#200003211720: Cannot add record to the layer table: "+name);
				e.printStackTrace();
			}
			saved=false;
			//Inform all Listeners
			for (int i=0;i<views.size();i++)
				((LayerView) views.get(i)).brokerLayerGeographicObjectAdded(go);
		} catch(Exception e) {
			throw new CannotAddObjectException("Unknown problem with Layer "+name+" R-Tree.");
		}
	}
	/**
	 * Internal use only.
	 */
	void __removeObject(GeographicObject go) throws CannotRemoveObjectException {
		if (!(go instanceof IVectorGeographicObject))
			throw new CannotRemoveObjectException("This object cannot exist in a vector layer!");
		try {
			rtree.delete((IVectorGeographicObject) go);
		} catch(CouldntDeleteFeatureException e) {
			throw new CannotRemoveObjectException("Object couldn't be removed from the layer.");
		}
		//Arrange object IDs.
		for (int i=go.getID();i<getGeographicObjects(false).size();i++)
			((GeographicObject) getGeographicObjects(false).get(i)).setID(((GeographicObject) getGeographicObjects(false).get(i)).getID()-1);

		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerGeographicObjectRemoved(go);
	}
	/**
	 * Repositions an object in the layer.
	 */
	public abstract void repositionObject(IVectorGeographicObject go,double xOffset,double yOffset);

	/**
	 * Frees up the memory hungry data.
	 */
	protected void freeUpResources() {
		//Keep a back up of the number of objects in the layer
		count=getGeographicObjects(false).size();
		rtree.clear();
		loaded=false;
	}
	/**
	 * Reads a layer from a structured stream.
	 */
	public void readStream(Access sf,Map map) throws ClassNotFoundException,IOException {
		ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(sf.openInputFile("layer")));
		restoreLayerAttributes((StorageStructure) in.readObject(),map);
		//The layer should be saved using version 2 format
		if (version==1)
			saved=false;
		else
			saved=true;
	}
	/**
	 * Writes a layer to a structured stream.
	 */
	public void writeStream(Access sf) throws IOException {
		ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile("layer")));
		out.writeObject(layerAttributes());
		out.close();
		//First load the layer data. If the layer is not loaded
		//when saving in the same file as the layer loads, data cannot be read.
		ArrayList objs=getGeographicObjects(false);
		//Write the layer data in a seperate file
		ObjectOutputStream out2=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile("layerdata")));
		out2.writeObject(new Integer(rtree.getFeatureCount()));
		for (int i=0;i<objs.size();i++)
			out2.writeObject(objs.get(i));

		//out2.flush();
		out2.close();

		//Remove its data from memory, if noone needs them.
		if (need==0)
			freeUpResources();

		//Change save version
		version=LAST_VERSION;
		saved=true;

		/*
			Version 1.0.1: Added date from, date to.
		*/
	}
	StorageStructure layerAttributes() {
		StorageStructure ht=super.layerAttributes();
		ht.put("version",LAST_VERSION); //Version number. As of the creation of Point, PolyLine and Polygon to decide which loadObjects should be used
		ht.put("normalFillColor",normalFillColor);
		ht.put("normalOutlineColor",normalOutlineColor);
		ht.put("selectedFillColor",selectedFillColor);
		ht.put("selectedOutlineColor",selectedOutlineColor);
		ht.put("highlightedFillColor",highlightedFillColor);
		ht.put("highlightedOutlineColor",highlightedOutlineColor);
		if (this instanceof PointLayer)
			ht.put("layertype",ShapeFileInfo.POINT_LAYER);
		else if (this instanceof PolygonLayer)
			ht.put("layertype",ShapeFileInfo.POLYGON_LAYER);
		else if (this instanceof PolyLineLayer)
			ht.put("layertype",ShapeFileInfo.POLYLINE_LAYER);
		return ht;
	}
	void restoreLayerAttributes(StorageStructure ht,Map map) {
		super.restoreLayerAttributes(ht,map);
		version=ht.get("version",1);
		normalFillColor=ht.get("normalFillColor",normalFillColor);
		normalOutlineColor=ht.get("normalOutlineColor",normalOutlineColor);
		selectedFillColor=ht.get("selectedFillColor",selectedFillColor);
		selectedOutlineColor=ht.get("selectedOutlineColor",selectedOutlineColor);
		highlightedFillColor=ht.get("highlightedFillColor",highlightedFillColor);
		highlightedOutlineColor=ht.get("highlightedOutlineColor",highlightedOutlineColor);
		//Normalize at the edges of the alpha space. Old rounding error.
		if (normalFillColor.getAlpha()<5)
			normalFillColor=new Color(normalFillColor.getRed(),normalFillColor.getGreen(),normalFillColor.getBlue(),0);
		else if (normalFillColor.getAlpha()>250)
			normalFillColor=new Color(normalFillColor.getRed(),normalFillColor.getGreen(),normalFillColor.getBlue(),255);
		if (normalOutlineColor.getAlpha()<5)
			normalOutlineColor=new Color(normalOutlineColor.getRed(),normalOutlineColor.getGreen(),normalOutlineColor.getBlue(),0);
		else if (normalOutlineColor.getAlpha()>250)
			normalOutlineColor=new Color(normalOutlineColor.getRed(),normalOutlineColor.getGreen(),normalOutlineColor.getBlue(),255);
		if (selectedFillColor.getAlpha()<5)
			selectedFillColor=new Color(selectedFillColor.getRed(),selectedFillColor.getGreen(),selectedFillColor.getBlue(),0);
		else if (selectedFillColor.getAlpha()>250)
			selectedFillColor=new Color(selectedFillColor.getRed(),selectedFillColor.getGreen(),selectedFillColor.getBlue(),255);
		if (selectedOutlineColor.getAlpha()<5)
			selectedOutlineColor=new Color(selectedOutlineColor.getRed(),selectedOutlineColor.getGreen(),selectedOutlineColor.getBlue(),0);
		else if (selectedOutlineColor.getAlpha()>250)
			selectedOutlineColor=new Color(selectedOutlineColor.getRed(),selectedOutlineColor.getGreen(),selectedOutlineColor.getBlue(),255);
		if (highlightedFillColor.getAlpha()<5)
			highlightedFillColor=new Color(highlightedFillColor.getRed(),highlightedFillColor.getGreen(),highlightedFillColor.getBlue(),0);
		else if (highlightedFillColor.getAlpha()>250)
			highlightedFillColor=new Color(highlightedFillColor.getRed(),highlightedFillColor.getGreen(),highlightedFillColor.getBlue(),255);
		if (highlightedOutlineColor.getAlpha()<5)
			highlightedOutlineColor=new Color(highlightedOutlineColor.getRed(),highlightedOutlineColor.getGreen(),highlightedOutlineColor.getBlue(),0);
		else if (highlightedOutlineColor.getAlpha()>250)
			highlightedOutlineColor=new Color(highlightedOutlineColor.getRed(),highlightedOutlineColor.getGreen(),highlightedOutlineColor.getBlue(),255);
	}

	int version; //Save file version
	private Color normalFillColor,normalOutlineColor,selectedFillColor,selectedOutlineColor,highlightedFillColor,highlightedOutlineColor;
	/**
	 * If the layer objects are not loaded when the layer is read from the disk
	 * save the id of the active object and set it as soon as the objects are loaded.
	 */
	private int pendingActive;
	/**
	 * The bounding rectangle of the current selection.
	 */
	Rectangle2D.Double selection;
	private static final int LAST_VERSION=2;
	/*
		Initializations
	*/
	{
		pendingActive=-1;
		normalOutlineColor=new Color(64,64,255);
		normalFillColor=new Color(128,128,255);
		selectedOutlineColor=new Color(255,64,255);
		selectedFillColor=new Color(255,128,255);
		highlightedOutlineColor=new Color(255,255,128);
		highlightedFillColor=new Color(128,255,128);
	}
}