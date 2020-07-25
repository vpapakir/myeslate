package gr.cti.eslate.mapModel;

import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.ImageTableField;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.mapModel.geom.Point;
import gr.cti.eslate.protocol.IMapView;
import gr.cti.eslate.protocol.IPointLayerView;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.protocol.IVectorGeographicObject;
import gr.cti.eslate.protocol.IncompatibleObjectTypeException;
import gr.cti.eslate.utils.ESlateHashtable;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * A point layer. A point layer may have single or multiple icons to represent the points.
 * The implementation ensures that only point features will be held in such layers.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 9-Aug-1999
 * @see		gr.cti.eslate.mapModel.VectorLayer
 * @see		gr.cti.eslate.mapModel.Layer
 */
public class PointLayer extends VectorLayer {
	/**
	 * Parameterless constructor.
	 */
	PointLayer() {
		guid=TYPE_ID+hashCode();
		try {
			setRTree(new PointRTree.Double());
		} catch(InvalidLayerDataException e) {}
	}
	/**
	 * Constructs a layer with the given name.
	 */
	public PointLayer(String name) {
		this();
		setName(name);
	}
	/**
	 * Constructs a layer with the given link to the structured file.
	 */
	public PointLayer(Access sf,int precision) {
		guid=TYPE_ID+hashCode();
		if (precision==IMapView.SINGLE_PRECISION)
			try {
				setRTree(new PointRTree.Float());
			} catch(InvalidLayerDataException e) {}
		else if (precision==IMapView.DOUBLE_PRECISION)
			try {
				setRTree(new PointRTree.Double());
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
	public PointLayer(URL shapefile,URL shapefileIndex,Table table,int precision) throws InvalidLayerDataException,CannotCreateRTreeException,IOException {
		if (precision!=IMapView.SINGLE_PRECISION && precision!=IMapView.DOUBLE_PRECISION)
			throw new InvalidLayerDataException("Invalid precision parameter.");
		guid=TYPE_ID+hashCode();
		/* REMOVED 20000419. A layer may not have an associated table.
		if (table==null)
			throw new InvalidLayerDataException("Invalid Table.");*/
		setRTree(RTree.createRTree(shapefile,shapefileIndex,precision),table);
		setPaintMode(IPointLayerView.PAINT_AS_CIRCLE);
	}
	/**
	 * Layer Constructor.
	 * @param rtree The layer to the layer shapefile.
	 * @param table The layer Table.
	 * @param map The map that contains this layer.
	 * @exception InvalidLayerDataException Thrown when either the rtree or the table is null, or when their sizes don't match.
	 */
	public PointLayer(PointRTree rtree,Table table) throws InvalidLayerDataException {
		guid=TYPE_ID+hashCode();
		if (rtree==null)
			throw new InvalidLayerDataException("Null R-Tree.");
		/* REMOVED 20000419. A layer may not have an associated table.
		if (table==null)
			throw new InvalidLayerDataException("Null table.");*/
		setRTree(rtree,table);
		setPaintMode(IPointLayerView.PAINT_AS_CIRCLE);
	}
	/**
	 * Sets the layer R-Tree.
	 * @exception InvalidLayerDataException Thrown when the R-Tree is null, or when the table record cound and the R-Tree feature count don't match.
	 */
	protected void setRTree(RTree rtree) throws InvalidLayerDataException {
		if (!(rtree instanceof PointRTree))
			throw new InvalidLayerDataException("Invalid RTree. Should be of Point type.");
		super.setRTree(rtree);
	}
	/**
	 * Sets the layer R-Tree.
	 * @exception InvalidLayerDataException Thrown when the R-Tree is null, or when the table record cound and the R-Tree feature count don't match.
	 */
	protected void setRTree(RTree rtree,Table table) throws InvalidLayerDataException {
		if (!(rtree instanceof PointRTree))
			throw new InvalidLayerDataException("Invalid RTree. Should be of Point type.");
		super.setRTree(rtree,table);
	}
	/**
	 * Creates a new view.
	 * @return The new view.
	 */
	public PointLayerView addView(IRegionView parentRegionView) {
		for (int i=0;i<views.size();i++) {
			if (((LayerView) views.get(i)).parentRegionView==parentRegionView && ((LayerView) views.get(i)).layer==this)
				return (PointLayerView) views.get(i);
		}
		//LayerView constructor adds the view itself as a side-effect.
		Object foo=(new PointLayerView(this,(RegionView)parentRegionView));
		return (PointLayerView) views.get(views.size()-1);
	}
	/**
	 * Creates a restorable icon from the given (God-knows-what-it-is) icon.
	 */
	private NewRestorableImageIcon createRestorableIcon(Icon icon) {
		if (icon==null)
			return null;
		Image img=new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(new JLabel(),img.getGraphics(),0,0);
		return new NewRestorableImageIcon(img);
	}
	/**
	 * Adds a "normal" icon in the icons hashtable.
	 * @param key The key for the hashtable. Should be an icon base
	 * field value for multi-iconed layers, or <em>normal</em>
	 * for the normal icon in a single icon layer.
	 */
	public void addNormalIcon(Object key,Icon icon) {
		if (key==null || icon==null)
			return;
		saved=false;
		normalIcons.put(key,createRestorableIcon(icon));
//		keySet.add(key);
	}
	/**
	 * Adds a "selected" icon in the icons hashtable.
	 * @param key The key for the hashtable. Should be an icon base
	 * field value for multi-iconed layers, or <em>selected</em>
	 * for the selected icon in a single icon layer.
	 */
	public void addSelectedIcon(Object key,Icon icon) {
		if (key==null || icon==null)
			return;
		saved=false;
		selectedIcons.put(key,createRestorableIcon(icon));
//		keySet.add(key);
	}
	/**
	 * Adds a "highlighted" icon in the icons hashtable.
	 * @param key The key for the hashtable. Should be an icon base
	 * field value for multi-iconed layers, or <em>highlighted</em>
	 * for the highlighted icon in a single icon layer.
	 */
	public void addHighlightedIcon(Object key,Icon icon) {
		if (key==null || icon==null)
			return;
		saved=false;
		highlightedIcons.put(key,createRestorableIcon(icon));
//		keySet.add(key);
	}
	/**
	 * Sets the tip base field. The tip base is a field
	 * whose value appears as a tooltip.
	 * @param tipBase The field name.
	 */
	public void setTipBase(AbstractTableField tipBase) {
		super.setTipBase(tipBase);
	}
	/**
	 * Sets the icon base field for multiIconed layers.
	 * @param base The field name.
	 */
	public void setIconBase(AbstractTableField base) {
		saved=false;
		iconBase=base;
	}
	/**
	 * Gets the icon base field name for multi-iconed layers.
	 */
	public AbstractTableField getIconBase() {
		return iconBase;
	}
	/**
	 * Gets the normal icon for the given point. In PAINT_AS_MULTIPLE_ICONS paint mode,
	 * if an icon exists for the given point, return it. Else return the single icon.
	 * This is done to help put a "default" icon in the layer and some special ones for this mode.
	 */
	protected Icon getNormalIcon(Point p) {
		if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			return normalIcon;
		else if (p==null)
			return null;
		else if (getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
			//In multiple icons paint mode, if an icon exists for the given point, return it.
			//Else return the single icon. This is done to help put a "default" icon in the layer
			//and some special ones.
			try {
				Icon ic=null;
				if (iconBase.getDataType()==ImageTableField.DATA_TYPE)
					ic=((ImageTableField) iconBase).getCell(p.getID()).getIcon();
				else
					ic=(Icon) normalIcons.get(getField(p,iconBase.getName()));
				if (ic!=null)
					return ic;
				else
					return normalIcon;
			} catch(Exception e) {
				return normalIcon;
			}
		}
		return null;
	}
	/**
	 * Gets the whole hash map of icons.
	 */
	protected java.util.Map getNormalIcons() {
		return normalIcons;
	}
	/**
	 * Gets the selected icon for the given point. In PAINT_AS_MULTIPLE_ICONS paint mode,
	 * if an icon exists for the given point, return it. Else return the single icon.
	 * This is done to help put a "default" icon in the layer and some special ones for this mode.
	 */
	protected Icon getSelectedIcon(Point p) {
		if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			return selectedIcon;
		else if (p==null)
			return null;
		else if (getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
			//In multiple icons paint mode, if an icon exists for the given point, return it.
			//Else return the single icon. This is done to help put a "default" icon in the layer
			//and some special ones.
			try {
				Icon ic=null;
				if (iconBase.getDataType()==ImageTableField.DATA_TYPE)
					//A new class that paints a border in the selected color
					ic=new MapRestorableImageIcon(((ImageTableField)iconBase).getCell(p.getID()).getIcon(),getSelectedOutlineColor());
				else
					ic=(Icon) selectedIcons.get(getField(p,iconBase.getName()));
				if (ic!=null)
					return ic;
				else
					return selectedIcon;
			} catch(Exception e) {
				return normalIcon;
			}
		}
		return null;
	}
	/**
	 * Gets the whole hash map of icons.
	 */
	protected java.util.Map getSelectedIcons() {
		return selectedIcons;
	}
	/**
	 * Gets the highlighted icon for the given point. In PAINT_AS_MULTIPLE_ICONS paint mode,
	 * if an icon exists for the given point, return it. Else return the single icon.
	 * This is done to help put a "default" icon in the layer and some special ones for this mode.
	 */
	protected Icon getHighlightedIcon(Point p) {
		if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			return highlightedIcon;
		else if (p==null)
			return null;
		else if (getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
			//In multiple icons paint mode, if an icon exists for the given point, return it.
			//Else return the single icon. This is done to help put a "default" icon in the layer
			//and some special ones.
			try {
				Icon ic=null;
				if (iconBase.getDataType()==ImageTableField.DATA_TYPE)
					ic=new MapRestorableImageIcon(((ImageTableField)iconBase).getCell(p.getID()).getIcon(),getHighlightedOutlineColor());
				else
					ic=(Icon) highlightedIcons.get(getField(p,iconBase.getName()));
				if (ic!=null)
					return ic;
				else
					return highlightedIcon;
			} catch(Exception e) {
				return normalIcon;
			}
		}
		return null;
	}
	/**
	 * Gets the whole hash map of icons.
	 */
	protected java.util.Map getHighlightedIcons() {
		return highlightedIcons;
	}
	/**
	 * Sets the normal icon for PAINT_AS_SAME_ICONS paint mode.
	 */
	protected void setSingleNormalIcon(Icon ic) {
		saved=false;
		normalIcon=createRestorableIcon(ic);
		//Inform all Listeners
		if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			for (int i=0;i<getViews().size();i++)
				((LayerView) getViews().get(i)).brokerLayerPaintPropertiesChanged();
	}
	/**
	 * Gets the normal icon for PAINT_AS_SAME_ICONS paint mode.
	 */
	protected Icon getSingleNormalIcon() {
		return normalIcon;
	}
	/**
	 * Sets the selected icon for PAINT_AS_SAME_ICONS paint mode.
	 */
	protected void setSingleSelectedIcon(Icon ic) {
		saved=false;
		selectedIcon=createRestorableIcon(ic);
		//Inform all Listeners
		if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			for (int i=0;i<getViews().size();i++)
				((LayerView) getViews().get(i)).brokerLayerPaintPropertiesChanged();
	}
	/**
	 * Gets the selected icon for PAINT_AS_SAME_ICONS paint mode.
	 */
	protected Icon getSingleSelectedIcon() {
		return selectedIcon;
	}
	/**
	 * Sets the highlighted icon for PAINT_AS_SAME_ICONS paint mode.
	 */
	protected void setSingleHighlightedIcon(Icon ic) {
		saved=false;
		highlightedIcon=createRestorableIcon(ic);
		//Inform all Listeners
		if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS)
			for (int i=0;i<getViews().size();i++)
				((LayerView) getViews().get(i)).brokerLayerPaintPropertiesChanged();
	}
	/**
	 * Gets the highlighted icon for PAINT_AS_SAME_ICONS paint mode.
	 */
	protected Icon getSingleHighlightedIcon() {
		return highlightedIcon;
	}
	/**
	 * Gets all the keys in the multiple icon hashtable.
	 */
	public Set getKeys() {
		if (normalIcons!=null)
			return normalIcons.keySet();
		else
			return null;
//		return keySet;
	}

	/**
	 * Clears all the icons from this layer.
	 */
	protected void clearIcons() {
		normalIcons=new ESlateHashtable();
		selectedIcons=new ESlateHashtable();
		highlightedIcons=new ESlateHashtable();
//		keySet=new HashSet();
	}
	/**
	 * Sets the circle radius for "CIRCLE" paint mode.
	 */
	protected void setCircleRadius(int radius) {
		saved=false;
		circleRadius=radius;
	}
	/**
	 * Gets the circle radius for "CIRCLE" paint mode.
	 */
	public int getCircleRadius() {
		return circleRadius;
	}
	/**
	 * Sets whether the circle for painting the points will be filled.
	 */
	public void setCircleFilled(boolean filled) {
		saved=false;
		circleFilled=filled;
	}
	/**
	 * Whether the circle for painting the points will be filled.
	 */
	public boolean isCircleFilled() {
		return circleFilled;
	}
	/**
	 * Sets how the layer will be painted.
	 */
	public void setPaintMode(int pm) {
		if ((pm!=IPointLayerView.PAINT_AS_CIRCLE) && (pm!=IPointLayerView.PAINT_AS_SAME_ICONS) && (pm!=IPointLayerView.PAINT_AS_MULTIPLE_ICONS))
			throw new IllegalArgumentException("Illegal paint mode for layer "+this);
		saved=false;
		super.setPaintMode(pm);
	}
	/**
	 * Repositions an object in the layer.
	 */
	public void repositionObject(IVectorGeographicObject go,double xOffset,double yOffset) {
		if (!(go instanceof Point) || (go==null))
			return;
		try {
			rtree.delete(go);
		} catch(CouldntDeleteFeatureException e) {
			System.err.println("MAP#200003230001: Cannot delete object on repositioning!");
			return;
		}
		Point2D.Double oldPos=new Point2D.Double(((Point) go).getX(),((Point) go).getY());
		((Point) go).setXY(((Point) go).getX()+xOffset,((Point) go).getY()+yOffset);
		try {
			rtree.insert(((Point) go).getX(),((Point) go).getY(),((Point) go).getX(),((Point) go).getY(),go);
		} catch(IncompatibleObjectTypeException e) {
			System.err.println("MAP#200003230000: Cannot reposition object!");
		}
		//Inform all Listeners
		for (int i=0;i<getViews().size();i++)
			((LayerView) getViews().get(i)).brokerLayerGeographicObjectRepositioned(oldPos,go);
	}
	/**
	 * This type of layer currently ignores the motion type and can never be a motion layer.
	 */
	protected void setMotionID(String id) {
		motionInfo=null;
	}
	/**
	 * This type of layer currently ignores the motion type and can never be a motion layer.
	 */
	protected boolean isMotionLayer() {
		return false;
	}
	/**
	 * Point objects do not have a bounding rectangle as they occupy no area in space.
	 * The selection rectangle calculated by the layer may not fit all the point icons
	 * in the graphics context they will be painted. This method passes scaling values
	 * and letter sizes and enlarges the selection rectangle to fit all the area the
	 * Point objects actually occupy in a given graphics context.
	 */
	public java.awt.geom.Rectangle2D enlargeSelectionRectangle(double scalex,double scaley) {
		if (selection==null)
			return null;
		ArrayList go=getGeographicObjects(true);
		Point p; double w,h,maxh,lw,y1,y2; String s;
		if (getPaintMode()==IPointLayerView.PAINT_AS_CIRCLE) {
			w=circleRadius/scalex; h=circleRadius/scaley;
			for (int i=go.size()-1;i>-1;i--) {
				lw=0;
				p=(Point) go.get(i);
				if (p.isSelected()) {
					y1=p.getY()-h;
					y2=p.getY()+h;
					addSelectionRectangle(p.getX()-w,y1,p.getX()+w+lw,y2);
				}
			}
		} else if (getPaintMode()==IPointLayerView.PAINT_AS_SAME_ICONS) {
			w=selectedIcon.getIconWidth()/scalex/2; h=selectedIcon.getIconHeight()/scaley/2;
			for (int i=go.size()-1;i>-1;i--) {
				lw=0;
				p=(Point) go.get(i);
				if (p.isSelected()) {
					y1=p.getY()-h;
					y2=p.getY()+h;
					addSelectionRectangle(p.getX()-w,y1,p.getX()+w+lw,y2);
				}
			}
		} else if (getPaintMode()==IPointLayerView.PAINT_AS_MULTIPLE_ICONS) {
			Icon ic;
			for (int i=go.size()-1;i>-1;i--) {
				lw=0;
				p=(Point) go.get(i);
				if (p.isSelected()) {
					//Enlarge by the icon
					ic=getSelectedIcon(p);
					if (ic!=null) {
						w=ic.getIconWidth()/scalex/2;
						h=ic.getIconHeight()/scaley/2;
					} else {
						w=circleRadius/scalex;
						h=circleRadius/scaley;
					}
					y1=p.getY()-h;
					y2=p.getY()+h;
					addSelectionRectangle(p.getX()-w,y1,p.getX()+w+lw,y2);
				}
			}
		}
		return selection;
	}

	/**
	 * Creates a FieldMap with all the necessary layer information, which doesnot include shapes.
	 */
	StorageStructure layerAttributes() {
		StorageStructure ss=super.layerAttributes();
		ss.put("circleRadius",circleRadius);
		ss.put("circleFilled",circleFilled);
		if (iconBase!=null) {
			ss.put("iconBase",iconBase.getName());
			ss.put("normalIcons",normalIcons);
			ss.put("selectedIcons",selectedIcons);
			ss.put("highlightedIcons",highlightedIcons);
		}
//		ss.put("keySet",keySet);
		if (normalIcon!=null)
			ss.put("normalIcon",normalIcon);
		if (selectedIcon!=null)
			ss.put("selectedIcon",selectedIcon);
		if (highlightedIcon!=null)
			ss.put("highlightedIcon",highlightedIcon);
		return ss;
	}
	/**
	 * Restores the layer attibutes which have been saved using <code>layerAttributes</code>.
	 */
	void restoreLayerAttributes(StorageStructure ss,Map map) {
		super.restoreLayerAttributes(ss,map);
		restoreAdditionalLayerAttributes(ss,map);
	}
	/**
	 * Restores from the StorageStructure additional layer attributes,
	 * specific to this layer type.
	 * @param ss    The StorageStructure.
	 */
	private void restoreAdditionalLayerAttributes(StorageStructure ss,Map map) {
		circleRadius=ss.get("circleRadius",circleRadius);
		circleFilled=ss.get("circleFilled",circleFilled);
		if (ss.containsKey("normalIcons")) {
			normalIcons=(ESlateHashtable) ss.get("normalIcons",normalIcons);
			selectedIcons=(ESlateHashtable) ss.get("selectedIcons",selectedIcons);
			highlightedIcons=(ESlateHashtable) ss.get("highlightedIcons",highlightedIcons);
		}
//		keySet=(HashSet) ss.get("keySet",keySet);
		normalIcon=(NewRestorableImageIcon) ss.get("normalIcon",normalIcon);
		selectedIcon=(NewRestorableImageIcon) ss.get("selectedIcon",selectedIcon);
		highlightedIcon=(NewRestorableImageIcon) ss.get("highlightedIcon",highlightedIcon);
	}
	/**
	 * Makes the database table associations when restoring layer attributes.
	 */
	void restoreTableAssociations(Table t,StorageStructure ss) {
		super.restoreTableAssociations(t,ss);
		if (t!=null)
			try {
				String s=(String) ss.get("iconBase");
				if (s==null)
					iconBase=null;
				else
					iconBase=getTable().getTableField(s);
			} catch(Exception e) {
				System.err.println("MAP#200004141510: Couldn't restore layer database associations. "+name);
				e.printStackTrace();
			}
	}
	/**
	 * Reads a layer from a structured stream.
	 */
	public void readStream(Access sf,Map map) throws ClassNotFoundException,IOException {
		super.readStream(sf,map);
		//Old way of saving layer specific data. Now, all data are included in the
		//same file, using the overridden layerAttributes method.
		if (sf.fileExists("pointlayer")) {
			ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(sf.openInputFile("pointlayer")));
			StorageStructure ss=(StorageStructure) in.readObject();
			restoreAdditionalLayerAttributes(ss,map);
			saved=false;
		}
	}
	/**
	 * Writes a layer to a structured stream.
	 */
	public void writeStream(Access sf) throws IOException {
		super.writeStream(sf);
		if (sf.fileExists("pointlayer"))
			sf.deleteFile("pointlayer");
	}

	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
	/**
	 * Type id.
	 */
	public static final String TYPE_ID="01";
	/**
	 * The circle radius for "CIRCLE" paint mode.
	 */
	private int circleRadius;
	/**
	 * Whether the circle for "CIRCLE" paint mode is filled.
	 */
	private boolean circleFilled;
	/**
	 * The icon base field.
	 */
	private AbstractTableField iconBase;
	/**
	 * Tallies the normal icons with the iconbase values.
	 */
	private ESlateHashtable normalIcons;
	/**
	 * Tallies the selected icons with the iconbase values.
	 */
	private ESlateHashtable selectedIcons;
	/**
	 * Tallies the highlighted icons with the iconbase values.
	 */
	private ESlateHashtable highlightedIcons;
	/**
	 * Set of all the keys for multiple icons.
	 */
//	private HashSet keySet;
	/**
	 * The icons for PAINT_AS_SAME_ICONS paint mode.
	 */
	private NewRestorableImageIcon normalIcon,selectedIcon,highlightedIcon;
	/**
	 * This id is suggested to be used as the default hidden field name of icon base when
	 * creating a layer such as a user object layer.
	 */
	public static final String ICON_ID="#__E-Slate_Icon_ID__#";

	{
		normalIcons=new ESlateHashtable();
		selectedIcons=new ESlateHashtable();
		highlightedIcons=new ESlateHashtable();
//		keySet=new HashSet();
		circleRadius=6;
		circleFilled=true;
		setPaintMode(IPointLayerView.PAINT_AS_CIRCLE);
	}
	/**
	 * A wrapper for the NewRestorableImageIcon which draws a rectangle in a specified
	 * color around the icon.
	 */
	public class MapRestorableImageIcon extends NewRestorableImageIcon {
		private Icon ic;
		private Color cl;
		private Stroke stroke=new BasicStroke(2);

		public MapRestorableImageIcon(Icon ic,Color cl) {
			this.ic=ic;
			this.cl=cl;
		}

		public int getIconWidth() {
			return ic.getIconWidth()+4;
		}
		public int getIconHeight() {
			return ic.getIconHeight()+4;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			ic.paintIcon(c,g,x+2,y+2);
			Stroke old=((Graphics2D) g).getStroke();
			Color oldc=g.getColor();
			((Graphics2D) g).setStroke(stroke);
			g.setColor(cl);
			g.drawRect(x,y,ic.getIconWidth()+3,ic.getIconHeight()+3);
			((Graphics2D) g).setStroke(old);
			g.setColor(oldc);
		}
	}
}
