package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.CImageIcon;
import gr.cti.eslate.database.engine.DoubleTableField;
import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.RecordEntryStructure;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.protocol.CannotRemoveObjectException;
import gr.cti.eslate.protocol.GeographicObject;
import gr.cti.eslate.protocol.IRasterGeographicObject;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

/**
 * An implementation of a raster-type layer. Raster information come from a digital image, the colors
 * of which represent a different value. In this implementation, 256 different colors may exist, thus
 * making the Compuserve GIF format the most appropriate picture format for picture layers.
 *
 * @author Giorgos Vasiliou
 * @version 1.0.0
 */

public class RasterLayer extends Layer {
	/**
	 * Parameterless constructor.
	 */
	RasterLayer() {
		guid=TYPE_ID+hashCode();
		raster=null;
	}
	/**
	 * Constructs a layer with the given name.
	 */
	public RasterLayer(String name) {
		this();
		setName(name);
	}
	/**
	 * Constructs a layer with the given link to the structured file.
	 */
	public RasterLayer(Access sf) {
		this();
		this.sf=sf;
	}
	/**
	 * Layer Constructor.
	 * @param file The URL to the layer gif image.
	 * @param table The layer Table.
	 * @param map The map that contains this layer.
	 * @exception InvalidLayerDataException Thrown when either the gif or the table is null, or when their sizes don't match.
	 * @exception IOException Thrown when the gif file cannot be found.
	 */
	public RasterLayer(URL file,Table table) throws InvalidLayerDataException,IOException {
		guid=TYPE_ID+hashCode();
		try {
			setRaster(loadRaster(file.openStream()),table);
		} catch(Throwable t) {
			throw new InvalidLayerDataException("Unable to create Raster Layer. Either the file is not a gif image, or the table is incompatible.");
		}
	}
	/**
	 * Layer Constructor.
	 * @param image The raster image.
	 * @param table The layer table.
	 * @exception InvalidRTreeDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public RasterLayer(BufferedImage image,Table table) throws InvalidLayerDataException {
		guid=TYPE_ID+hashCode();
		setRaster(image,table);
	}
	/**
	 * Sets the raster data.
	 * @param image The raster image.
	 * @exception InvalidRTreeDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public void setRaster(RasterImage image) throws InvalidLayerDataException {
		setRaster((BufferedImage) image,null);
	}
	/**
	 * Sets the raster data.
	 * @param image The raster image.
	 * @exception InvalidRTreeDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public void setRaster(BufferedImage image) throws InvalidLayerDataException {
		setRaster(image,null);
	}
	/**
	 * Sets the raster data.
	 * @param image The raster image.
	 * @param table The layer table.
	 * @exception InvalidRTreeDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public void setRaster(RasterImage image,Table table) throws InvalidLayerDataException {
		setRaster((BufferedImage) image,table);
	}
	/**
	 * Sets the raster data.
	 * @param image The raster image.
	 * @param table The layer table.
	 * @exception InvalidRTreeDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public void setRaster(BufferedImage image,Table table) throws InvalidLayerDataException {
		if (image==null || image.getType()!=BufferedImage.TYPE_BYTE_INDEXED || (table!=null && table.getRecordCount()<((IndexColorModel)image.getColorModel()).getMapSize()))
			throw new InvalidLayerDataException("Null Image, incorrect type, or incompatible Table.");
		try {
			//Make a local copy of the image to avoid interference only when it is not a RasterImage
			//which is local.
			if (!(image instanceof RasterImage)) {
				if (raster!=null)
					raster.flush();
				raster=new RasterImage(image.getWidth(),image.getHeight(),tuneTransparency((IndexColorModel) image.getColorModel()));
				raster.getGraphics().drawImage(image,0,0,null);
			} else
				raster=(RasterImage) image;
			saved=false;
			int mapSize=((IndexColorModel)image.getColorModel()).getMapSize();
			//Create the geographic objects
			objects.clear();
			for (int i=0;i<mapSize;i++)
				objects.add(new RasterGeographicObject(i));
			//Create a deletion track.
			deleted=new boolean[objects.size()];
			for (int i=0;i<objects.size();i++)
				deleted[i]=false;

			if (table!=null)
				setTable(table);
		} catch(Throwable t) {
			throw new InvalidLayerDataException("Unable to build raster layer.");
		}
	}
	/**
	 * Creates a new view.
	 * @return The new view.
	 */
	public RasterLayerView addView(IRegionView parentRegionView) {
		for (int i=0;i<views.size();i++) {
			if (((LayerView) views.get(i)).parentRegionView==parentRegionView && ((LayerView) views.get(i)).layer==this)
				return (RasterLayerView) views.get(i);
		}
		//LayerView constructor adds the view itself as a side-effect.
		Object foo=(new RasterLayerView(this,(RegionView) parentRegionView));
		return (RasterLayerView) views.get(views.size()-1);
	}
	/**
	 * Sets the layer table.
	 */
	public void setTable(Table table) throws InvalidLayerDataException {
		try {
			if (table!=null && table.getRecordCount()<objects.size())
				throw new InvalidLayerDataException("The given table is null or does not have a correct number of records.");
		} catch(Exception e) {
			table=null;
			throw new InvalidLayerDataException("The given table is null or does not have a correct number of records.");
		}
		super.setTable(table);
	}
	/**
	 * Method that loads the raster if needed which returns it casted to RasterImage.
	 * Actually a coding shortcut.
	 */
	private RasterImage myRaster() {
		return (RasterImage) getRaster();
	}
	/**
	 * Checks if the layer geographic data are loaded. Loads if not.
	 */
	private synchronized void checkNload() {
		if (!loaded && (sf!=null)) {
			//Read the image data
			try {
				sf.changeToRootDir();
				sf.changeDir("layers");
				sf.changeDir(getGUID());
				//Restore layer geographic objects
				objects.clear();
				ArrayList objs=(ArrayList) (new ObjectInputStream(new BufferedInputStream(sf.openInputFile("layerobjects")))).readObject();
				//Objects have been put in a separate Array because objects as an instance of an inner class
				//of RasterLayer, would serialize RasterLayer as well!
				for (int i=0;i<objs.size();i++)
					objects.add(objs.get(i));
				StorageStructure fm=(StorageStructure) (new ObjectInputStream(new BufferedInputStream(sf.openInputFile("rasterdata")))).readObject();
				//Restore deletion data
				deleted=(boolean[]) fm.get("deleted");
				//Restore Image info
				int width=fm.get("width",0);
				int height=fm.get("height",0);
				//Restore ColorModel
				IndexColorModel cm=new IndexColorModel(8,fm.get("noColors",0),(byte[]) fm.get("reds"),(byte[]) fm.get("greens"),(byte[]) fm.get("blues"),(byte[]) fm.get("alphas"));
				//Restore pixel data
				byte[] databyte=new byte[fm.get("numbytes",0)];
				ZipInputStream zip=new ZipInputStream(new BufferedInputStream(sf.openInputFile("rawdata")));
				DataInputStream din=new DataInputStream(zip);
				zip.getNextEntry();
				din.readFully(databyte);
				zip.closeEntry();
				zip.close();
				din.close();
				int[] data=new int[databyte.length];
				for (int i=0;i<data.length;i++)
					data[i]=databyte[i];
				raster=new RasterImage(width,height,cm);
				raster.getRaster().setSamples(0,0,width,height,0,data);

				//Because gifs trim the color model, use this method call to add the missing colors
				raster.colorModel=tuneTransparency(cm);
				loaded=true;
			} catch(Throwable e) {
				System.err.println("MAP#200103301241: Could not load layer data.");
				e.printStackTrace();
			}
		}
		if (sf==null)
			loaded=true;
	}
	/**
	 * Loads the raster data from disk.
	 */
	private RasterImage loadRaster(InputStream is) throws Throwable {
		BufferedImage im=ImageIO.read(is);
		MediaTracker mt=new MediaTracker(new javax.swing.JLabel());
		mt.addImage(im,0);
		//Wait 30 sec max for the image to load
		mt.waitForID(0,30000);
		//Create the raster
		RasterImage ri=new RasterImage(im.getWidth(null),im.getHeight(null),(IndexColorModel) im.getColorModel());
		ri.getGraphics().drawImage(im,0,0,null);
		im.flush();
		return ri;
	}
	/**
	 * The raster. If it is swapped out, the method loads it as well.
	 */
	protected BufferedImage getRaster() {
		checkNload();
		return raster;
	}
	/**
	 * The transparency level of the color areas when in normal view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	protected int getNormalViewTransparencyLevel() {
		return normalTransp;
	}
	/**
	 * The transparency level of the color areas when in selected view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	protected int getSelectedViewTransparencyLevel() {
		return selectedTransp;
	}
	/**
	 * The transparency level of the color areas when in highlighted view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	protected int getHighlightedViewTransparencyLevel() {
		return highTransp;
	}
	/**
	 * The transparency level of the color areas when in normal view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	protected void setNormalViewTransparencyLevel(int tl) {
		int old=normalTransp;
		normalTransp=tl;
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerRasterTransparencyLevelChanged(LayerEvent.LAYER_NORMAL_TRANSPARENCY_LEVEL_CHANGED,old,highTransp);
	}
	/**
	 * The transparency level of the color areas when in selected view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	protected void setSelectedViewTransparencyLevel(int tl) {
		int old=normalTransp;
		selectedTransp=tl;
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerRasterTransparencyLevelChanged(LayerEvent.LAYER_SELECTED_TRANSPARENCY_LEVEL_CHANGED,old,highTransp);
	}
	/**
	 * The transparency level of the color areas when in highlighted view.
	 * It is given between [0,255], with 0 meaning transparent and 255 meaning opaque.
	 */
	protected void setHighlightedViewTransparencyLevel(int tl) {
		int old=highTransp;
		highTransp=tl;
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerRasterTransparencyLevelChanged(LayerEvent.LAYER_HIGHLIGHTED_TRANSPARENCY_LEVEL_CHANGED,old,highTransp);
	}
	/**
	 * Tunes the transparency of the <code>in</code> IndexColorModel colors to the defined levels.
	 * It returns a new ColorModel. If the ColorModel has less colors than the parameter, add
	 * colors to the Model.
	 * @param   in      The input Color Model.
	 */
	IndexColorModel tuneTransparency(IndexColorModel in) {
		byte[] cmap=new byte[4*in.getMapSize()]; //3 for rgb plus 1 for alpha
		byte[] ctmp=new byte[in.getMapSize()];
		int tt;

		//When deleting items, the map size may be greater than the objects size.
		int end=objects.size();
		//Copy Red components
		in.getReds(ctmp);
		for (int j=0;j<end;j++) {
			tt=objectToColor(j);
			cmap[4*tt]=ctmp[tt];
		}
		//Copy Green components
		in.getGreens(ctmp);
		for (int j=0;j<end;j++) {
			tt=objectToColor(j);
			cmap[4*tt+1]=ctmp[tt];
		}
		//Copy Blue components
		in.getBlues(ctmp);
		for (int j=0;j<end;j++) {
			tt=objectToColor(j);
			cmap[4*tt+2]=ctmp[tt];
		}
		//Put alpha values
		for (int j=0;j<end;j++) {
			tt=objectToColor(j);
			if (deleted[tt])
				cmap[4*tt+3]=(byte) 0;
			else if (!shouldBeVisible((GeographicObject) objects.get(j)))
				cmap[4*tt+3]=(byte) 0;
			else if (activeObject!=null && j==activeObject.getID())
				cmap[4*tt+3]=(byte) highTransp;
			else if (((GeographicObject) objects.get(j)).isSelected())
				cmap[4*tt+3]=(byte) selectedTransp;
			else
				cmap[4*tt+3]=(byte) normalTransp;
		}

		IndexColorModel out=new IndexColorModel(8,in.getMapSize(),cmap,0,true,in.getTransparentPixel());
		return out;
	}
	/**
	 * Provides the array position of an object index when objects have been
	 * deleted, destroying the one-to-one relationship between colors and objects.
	 * Remember that colors are never actualy removed.
	 */
	private int objectToColor(int inx) {
		int o=0; int i=0;
		inx++; //Because index starts from 0
		while (o<inx) {
			if (!deleted[i])
				o++;
			i++;
		}
		return --i;
	}
	/**
	 * Provides the object index position of a color when objects have been
	 * deleted, destroying the one-to-one relationship between colors and objects.
	 * Remember that colors are never actualy removed.
	 */
	private int colorToObject(int cinx) {
		int o=0;
		for (int i=0;i<cinx;i++)
			if (!deleted[i])
				o++;
		return o;
	}
	/**
	 * Frees up the memory hungry data.
	 */
	protected void freeUpResources() {
		if (raster!=null)
			raster.flush();
		raster=null;
		objects.clear();
		loaded=false;
	}
	/**
	 * Gets the number of objects in the layer.
	 */
	public int getObjectCount() {
		return objects.size();
	}
	/**
	 * Sets the active geographic object.
	 */
	protected void setActiveGeographicObject(GeographicObject active) {
		super.setActiveGeographicObject(active);
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerActiveGeographicObjectChanged(oldActiveObject,activeObject);
	}
	/**
	 * Sets the selection.
	 * @return  The IDs pf the objects that where actually selected.
	 */
	protected void setSelectedGeographicObjects(ArrayList selected) {
		if (getRaster()==null)
			return;
		super.setSelectedGeographicObjects(selected);
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerSelectionChanged(null,null);
	}
	/**
	 * Deselects the given objects. Other selected objects remain unchanged.
	 * @return  The IDs pf the objects that where actually selected.
	 */
	protected void deselectGeographicObjects(ArrayList desel) {
		super.deselectGeographicObjects(desel);
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerSelectionChanged(null,null);
	}
	/**
	 * Expands the selection. If an object in the selected array is already selected,
	 * it is turned to not selected.
	 */
	protected void addToSelectedGeographicObjects(ArrayList selected) {
		super.addToSelectedGeographicObjects(selected);
		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		//Inform all Listeners
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerSelectionChanged(null,null);
	}
	/**
	 * All the layer data.
	 */
	protected ArrayList getGeographicObjects(boolean constrain) {
		checkNload();
		if (raster==null)
			return new ArrayList();
		if (!constrain || (!hideUnselected && showBlankRecordObjects))
			return objects;
		else
			return applyConstraints(objects);
	}
	/**
	 * Get the objects that their bounding rectangle resides into or intersects with the given rectangle.
	 */
	protected ArrayList getGeographicObjects(java.awt.geom.Rectangle2D rectangle,boolean constrain) {
		return getGeographicObjects(rectangle,rectangle,constrain);
	}
	/**
	 * Get the objects that their bounding rectangle resides into or intersects with the given rectangle
	 * and the "hotspot" rectangle intersects with the actual shape.
	 */
	protected ArrayList getGeographicObjects(java.awt.geom.Rectangle2D rectangle,Shape hotspot,boolean constrain) {
		return applyConstraints(objects);
	}
	/**
	 * Gets the GeographicObject in the given coordinates.
	 * @return  The GeographicObject.
	 */
	protected GeographicObject getGeographicObjectAt(double xo,double yo,boolean constrain) {
		//Searching outside the layer area
		Rectangle2D.Double r=getBoundingRect();
		if (xo<r.x || xo>r.x+r.width || yo<r.y || yo>r.y+r.height)
			return null;
		//Find the pixel using planar geometry, not spherical
		int px=(int) ((xo-r.x)*getRaster().getWidth()/r.width);
		int py=(int) (getRaster().getHeight()-(yo-r.y)*getRaster().getHeight()/r.height);

		int sample=getRaster().getRaster().getSample(px,py,0);
		//This "object" has been deleted
		if (deleted[sample])
			return null;
		GeographicObject go=(GeographicObject) objects.get(colorToObject(sample));
		return (shouldBeVisible(go)?go:null);
	}
	/**
	 * Internal use only.
	 */
	void __removeObject(GeographicObject go) throws CannotRemoveObjectException {
		if (!(go instanceof IRasterGeographicObject))
			throw new CannotRemoveObjectException("This object cannot exist in a vector layer!");
		objects.remove(go);
		//Arrange object IDs.
		for (int i=go.getID();i<objects.size();i++)
			((GeographicObject) objects.get(i)).setID(((GeographicObject) objects.get(i)).getID()-1);

		myRaster().colorModel=tuneTransparency(myRaster().colorModel);
		saved=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((LayerView) views.get(i)).brokerLayerGeographicObjectRemoved(go);
	}
	/**
	 * Sets the motion ID for this layer. The MotionInfo object changes accordingly.
	 * WARNING: Raster layers do not support motion for now.
	 */
	protected void setMotionID(String id) {
		motionInfo=null;
	}
	/**
	 * Helper method that produces a new database table for this layer. It does not add it.
	 */
	protected Table produceNewTable() throws Exception {
		Table newtable=new Table();
		newtable.addField(MapCreator.bundleCreator.getString("color"),ImageTableField.DATA_TYPE,true,true,false);
		newtable.addField(MapCreator.bundleCreator.getString("value"),DoubleTableField.DATA_TYPE,true,true,false);
		int c=objects.size();
		int max=Math.max(objects.size(),myRaster().colorModel.getMapSize());
		byte[] r=new byte[max];
		myRaster().colorModel.getReds(r);
		byte[] g=new byte[max];
		myRaster().colorModel.getGreens(g);
		byte[] b=new byte[max];
		myRaster().colorModel.getBlues(b);
		for (int i=0;i<c;i++) {
			//Make a small icon with the color
			BufferedImage bi=new BufferedImage(20,10,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2=(Graphics2D) bi.getGraphics();
			g2.setPaint(new Color((r[i]<0?r[i]+256:r[i]),(g[i]<0?g[i]+256:g[i]),(b[i]<0?b[i]+256:b[i])));
			g2.fillRect(0,0,21,11);
			RecordEntryStructure re=newtable.getRecordEntryStructure();
			re.startRecordEntry();
			CImageIcon ci=new CImageIcon();
			ci.setImage(bi);
			re.setCell(re.getField(0),ci);
			re.setCell(((DoubleTableField)re.getField(1)),0);
			re.commitNewRecord((i==c-1)?false:true);
		}
		return newtable;
	}
	/**
	 * Restores the layer attibutes which have been saved using <code>layerAttributes</code>.
	 */
	void restoreLayerAttributes(StorageStructure ht,Map map) {
		super.restoreLayerAttributes(ht,map);
		normalTransp=ht.get("normalTransp",normalTransp);
		selectedTransp=ht.get("selectedTransp",selectedTransp);
		highTransp=ht.get("highTransp",highTransp);
	}

	StorageStructure layerAttributes() {
		StorageStructure ht=super.layerAttributes();
		ht.put("layertype",ShapeFileInfo.RASTER_LAYER);
		ht.put("normalTransp",normalTransp);
		ht.put("selectedTransp",selectedTransp);
		ht.put("highTransp",highTransp);

		return ht;
	}
	/**
	 * Reads a layer from a structured stream.
	 */
	public void readStream(Access sf,Map map) throws ClassNotFoundException,IOException {
		ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(sf.openInputFile("layer")));
		restoreLayerAttributes((StorageStructure) in.readObject(),map);
		saved=true;
	}
	/**
	 * Writes a layer to a structured stream.
	 */
	public void writeStream(Access sf) throws IOException {
		ObjectOutputStream out=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile("layer")));
		out.writeObject(layerAttributes());
		out.close();
		//Write the layer data in a separate file

		ObjectOutputStream out2=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile("rasterdata")));
		//Image has to get rid of transparency information. Temporarily set the color model
		//to the original one.
		ESlateFieldMap2 fm=new ESlateFieldMap2(1);
		IndexColorModel cm=myRaster().colorModel;
		byte[] reds=new byte[cm.getMapSize()];
		byte[] greens=new byte[cm.getMapSize()];
		byte[] blues=new byte[cm.getMapSize()];
		byte[] alphas=new byte[cm.getMapSize()];
		cm.getReds(reds);
		cm.getGreens(greens);
		cm.getBlues(blues);
		cm.getAlphas(alphas);
		//Store Image info
		fm.put("width",raster.getWidth());
		fm.put("height",raster.getHeight());
		//Store ColorModel
		fm.put("reds",reds);
		fm.put("greens",greens);
		fm.put("blues",blues);
		fm.put("alphas",alphas);
		fm.put("noColors",cm.getMapSize());
		fm.put("transparent",cm.getTransparentPixel());
		//Store deletion data
		fm.put("deleted",deleted);
		//Store pixel data
		int[] dataint=null;
		dataint=raster.getData().getSamples(0,0,raster.getWidth(),raster.getHeight(),0,dataint);
		fm.put("numbytes",dataint.length);
		out2.writeObject(fm);
		out2.close();
		byte[] data=new byte[dataint.length];
		for (int i=0;i<data.length;i++)
			data[i]=(byte) dataint[i];
		ZipOutputStream zip=new ZipOutputStream(new BufferedOutputStream(sf.openOutputFile("rawdata")));
		zip.setLevel(5);
		zip.putNextEntry(new ZipEntry("bytearray"));
		zip.write(data);
		zip.closeEntry();
		zip.close();

		//Write layer geographic objects
		out=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile("layerobjects")));
		//Put objects in a separate Array because as an instance of an inner class
		//of RasterLayer, Java wants to serialize it as well!
		ArrayList a=new ArrayList();
		for (int i=0;i<objects.size();i++)
			a.add(objects.get(i));
		out.writeObject(a);
		out.close();

		//Remove its data from memory, if noone needs them.
		if (need==0)
			freeUpResources();

		saved=true;
	}
	/**
	 * The raster data. Should not be used directly as the data may have been swapped out of memory.
	 * <code>getRaster()</code> should be used instead.
	 */
	private RasterImage raster;
	/**
	 * The geographic objects that represent the different raster colors.
	 */
	private ArrayList objects;
	/**
	 * Type id.
	 */
	public static final String TYPE_ID="99";
	/**
	 * Transparency level for view.
	 */
	private int normalTransp, selectedTransp, highTransp;
	/**
	 * Says which colors have been deleted from the color model.
	 */
	private boolean[] deleted;

	/**
	 * BufferedImage with a public indexed color model.
	 */
	public class RasterImage extends BufferedImage {
		public IndexColorModel colorModel;
		public IndexColorModel originalCM;
		public RasterImage(int width,int height,IndexColorModel colorModel) {
			super(width,height,BufferedImage.TYPE_BYTE_INDEXED,colorModel);
			this.colorModel=colorModel;
			this.originalCM=colorModel;
		}
		public ColorModel getColorModel() {
			return colorModel;
		}
		/**
		 * Needs to be reimplemented because the superclass implementation provides the
		 * subimage with wrong color model, not the one used by this RasterImage, but
		 * one with no alpha channel.
		 */
		public BufferedImage getSubimage(int x,int y,int w,int h) {
			return new BufferedImage (colorModel,
								  RasterImage.this.getRaster().createWritableChild(x, y, w, h,
															 0, 0, null),
								  colorModel.isAlphaPremultiplied(),
								  null);
		}
	}

	{
		normalTransp=32;
		selectedTransp=64;
		highTransp=128;
		/*A special Array. It keeps track of deleted objects using the deleted array */
		objects=new ArrayList() {
			public boolean remove(Object o) {
				GeographicObject go=(GeographicObject) o;
				boolean b=super.remove(o);
				deleted[objectToColor(go.getID())]=true;
				return b;
			}
		};
	}
}