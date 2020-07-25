package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IPolygonLayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.MotionInfo;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;

/**
 * A polygon layer.
 * The implementation ensures that only polygonic features will be held in such layers.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 4-Nov-1999
 * @see		gr.cti.eslate.mapModel.VectorLayer
 * @see		gr.cti.eslate.mapModel.Layer
 */
public class PolygonLayer extends VectorLayer {
	/**
	 * Parameterless constructor.
	 */
	PolygonLayer() {
		guid=TYPE_ID+hashCode();
		try {
			setRTree(new PolygonRTree.Double());
		} catch(InvalidLayerDataException e) {}
	}
	/**
	 * Constructs a layer with the given link to the structured file.
	 */
	public PolygonLayer(Access sf,int precision) {
		guid=TYPE_ID+hashCode();
		if (precision==IMapView.SINGLE_PRECISION)
			try {
				setRTree(new PolygonRTree.Float());
			} catch(InvalidLayerDataException e) {}
		else if (precision==IMapView.DOUBLE_PRECISION)
			try {
				setRTree(new PolygonRTree.Double());
			} catch(InvalidLayerDataException e) {}
		else
			throw new RuntimeException("Incorrect data precision constructing layer.");
		this.sf=sf;
	}
	/**
	 * Layer Constructor.
	 * @param shapefile The URL to the layer shapefile.
	 * @param shapefileIndex The URL to the layer shapefile index.
	 * @param table The layer Table.
	 * @param precision The data precision, Map.SINGLE_PRECISION or Map.DOUBLE_PRECISION.
	 * @exception InvalidLayerDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 * @exception CannotCreateRTreeException Thrown when the shapefile is invalid.
	 * @exception IOException Thrown when the shapefile or its index cannot be found.
	 */
	public PolygonLayer(URL shapefile,URL shapefileIndex,Table table,int precision) throws InvalidLayerDataException,CannotCreateRTreeException,IOException {
		if (precision!=IMapView.SINGLE_PRECISION && precision!=IMapView.DOUBLE_PRECISION)
			throw new InvalidLayerDataException("Invalid precision parameter.");
		guid=TYPE_ID+hashCode();
		/* REMOVED 20000419. A layer may not have an associated table.
		if (table==null)
			throw new InvalidLayerDataException("Invalid Table.");*/
		setRTree(RTree.createRTree(shapefile,shapefileIndex,precision),table);
	}
	/**
	 * Layer Constructor.
	 * @param rtree The layer to the layer shapefile.
	 * @param table The layer Table.
	 * @param map The map that contains this layer.
	 * @exception InvalidLayerDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public PolygonLayer(PolygonRTree rtree,Table table) throws InvalidLayerDataException {
		guid=TYPE_ID+hashCode();
		if (rtree==null)
			throw new InvalidLayerDataException("Null RTree.");
		/* REMOVED 20000419. A layer may not have an associated table.
		if (table==null)
			throw new InvalidLayerDataException("Null Table.");*/
		setRTree(rtree,table);
	}
	/**
	 * Sets the layer R-Tree.
	 * @exception InvalidLayerDataException Thrown when the R-Tree is null, or when the table record cound and the R-Tree feature count don't match.
	 */
	protected void setRTree(RTree rtree) throws InvalidLayerDataException {
		if (!(rtree instanceof PolygonRTree))
			throw new InvalidLayerDataException("Invalid RTree. Should be of Polygon type.");
		super.setRTree(rtree);
	}
	/**
	 * Sets the layer R-Tree.
	 * @exception InvalidLayerDataException Thrown when the R-Tree is null, or when the table record cound and the R-Tree feature count don't match.
	 */
	protected void setRTree(RTree rtree,Table table) throws InvalidLayerDataException {
		if (!(rtree instanceof PolygonRTree))
			throw new InvalidLayerDataException("Invalid RTree. Should be of Polygon type.");
		super.setRTree(rtree,table);
	}
	/**
	 * Creates a new view.
	 * @return The new view.
	 */
	public PolygonLayerView addView(IRegionView parentRegionView) {
		for (int i=0;i<views.size();i++) {
			if (((LayerView) views.get(i)).parentRegionView==parentRegionView && ((LayerView) views.get(i)).layer==this)
				return (PolygonLayerView) views.get(i);
		}
		//LayerView constructor adds the view itself as a side-effect.
		Object foo=(new PolygonLayerView(this,(RegionView)parentRegionView));
		return (PolygonLayerView) views.get(views.size()-1);
	}
	/**
	 * Sets how the layer will be painted.
	 */
	public void setPaintMode(int pm) {
		if ((pm!=IPolygonLayerView.PAINT_AS_STRAIGHT_LINE) && (pm!=IPolygonLayerView.PAINT_AS_DASHED_LINE) && (pm!=IPolygonLayerView.PAINT_AS_DOTTED_LINE))
			throw new IllegalArgumentException("Illegal paint mode for layer "+this);
		saved=false;
		super.setPaintMode(pm);
	}
	/**
	 * Gets the line width for the polygon outline.
	 */
	public int getLineWidth() {
		return lineWidth;
	}
	/**
	 * Gets the line width for the polygon outline.
	 */
	protected void setLineWidth(int lw) {
		saved=false;
		lineWidth=lw;
	}
	/**
	 * Whether the polygons will be filled.
	 */
	public boolean isPolygonFilled() {
		return polygonFilled;
	}
	/**
	 * Sets whether the polygons will be filled.
	 */
	protected void setPolygonFilled(boolean pf) {
		saved=false;
		polygonFilled=pf;
	}
	/**
	 * Drawing error tolerance.
	 */
	protected void setErrorTolerance(float f) {
		if (errorTolerance==f)
			return;
		saved=false;
		errorTolerance=f;
		firePaintPropertiesChanged();
	}
	/**
	 * Drawing error tolerance.
	 */
	protected float getErrorTolerance() {
		return errorTolerance;
	}
	/**
	 * Repositions an object in the layer.
	 */
	public void repositionObject(IVectorGeographicObject go,double xOffset,double yOffset) {
	}
	/**
	 * Sets the motion ID for this layer. The MotionInfo object changes accordingly.
	 */
	protected void setMotionID(String id) {
new Throwable().fillInStackTrace().printStackTrace();		
		saved=false;
		if (id==null)
			motionInfo=null;
		else if (id.equals(MapCreator.bundleCreator.getString(MotionInfo.MOTION_ROAD_KEY))) {
			motionInfo=new RoadMotionInfo();
System.out.println("ROADLAYER");			
			motionInfo.setID(MotionInfo.MOTION_ROAD_KEY);
		} else if (id.equals(MapCreator.bundleCreator.getString(MotionInfo.MOTION_RAILWAY_KEY))) {
			motionInfo=new RailwayMotionInfo();
			motionInfo.setID(MotionInfo.MOTION_RAILWAY_KEY);
		} else if (id.equals(MapCreator.bundleCreator.getString(MotionInfo.MOTION_SEA_KEY))) {
			motionInfo=new SeaMotionInfo();
			motionInfo.setID(MotionInfo.MOTION_SEA_KEY);
		} else if (id.equals(MapCreator.bundleCreator.getString(MotionInfo.MOTION_AIR_KEY))) {
			motionInfo=new AirMotionInfo();
			motionInfo.setID(MotionInfo.MOTION_AIR_KEY);
		} else {
			motionInfo=new DefaultMotionInfo();
			motionInfo.setID(id);
		}
	}
	/**
	 * Creates a FieldMap with all the necessary layer information, which doesnot include shapes.
	 */
	StorageStructure layerAttributes() {
		StorageStructure ss=super.layerAttributes();
		ss.put("lineWidth",lineWidth);
		ss.put("polygonFilled",polygonFilled);
		ss.put("errorTolerance",errorTolerance);
		return ss;
	}
	/**
	 * Restores the layer attibutes which have been saved using <code>layerAttributes</code>.
	 */
	void restoreLayerAttributes(StorageStructure ss,Map map) {
		super.restoreLayerAttributes(ss,map);
		restoreAdditionalLayerAttributes(ss);
	}
	/**
	 * Restores from the StorageStructure additional layer attributes,
	 * specific to this layer type.
	 * @param ss    The StorageStructure.
	 */
	private void restoreAdditionalLayerAttributes(StorageStructure ss) {
		lineWidth=ss.get("lineWidth",lineWidth);
		polygonFilled=ss.get("polygonFilled",polygonFilled);
		errorTolerance=ss.get("errorTolerance",-1f);
	}
	/**
	 * Reads a layer from a structured stream.
	 */
	public void readStream(Access sf,Map map) throws ClassNotFoundException,IOException {
		super.readStream(sf,map);
		//Old way of saving layer specific data. Now, all data are included in the
		//same file, using the overridden layerAttributes method.
		if (sf.fileExists("polygonlayer")) {
			ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(sf.openInputFile("polygonlayer")));
			StorageStructure ss=(StorageStructure) in.readObject();
			restoreAdditionalLayerAttributes(ss);
			saved=false;
		}
	}
	/**
	 * Writes a layer to a structured stream.
	 */
	public void writeStream(Access sf) throws IOException {
		super.writeStream(sf);
		if (sf.fileExists("polygonlayer"))
			sf.deleteFile("polygonlayer");
	}

	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
	/**
	 * Type id.
	 */
	public static final String TYPE_ID="05";
	/**
	 * The line width.
	 */
	private int lineWidth;
	/**
	 * Polygon filled?
	 */
	private boolean polygonFilled;
	/**
	 * Local drawing error tolerance value.
	 */
	private float errorTolerance;

	//Initializations
	{
		lineWidth=1;
		polygonFilled=true;
		errorTolerance=-1;
		setPaintMode(IPolygonLayerView.PAINT_AS_STRAIGHT_LINE);
	}
}
