package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IMapBackground;
import gr.cti.eslate.protocol.IRegion;
import gr.cti.eslate.protocol.IRegionView;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

/* Papakirikou */
/*import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;*/

/**
 * A Region can be stand alone or part of a Map.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 6-Aug-1999
 * @see		gr.cti.eslate.mapModel.Map
 */
public class Region implements Externalizable,IRegion {
	/**
	 * A Region is constructed as well as its plug.
	 */
	public Region() {
		//Variable initialization
		guid=""+hashCode();
		savedGeneral=false;
		layers=new Layer[0];
		background=new MapBackgroundModel();
		setName(Map.bundleMessages.getString("myregion"));
		orientation=0;
		scale=0;
		need=0;
		coordSyst=IRegionView.COORDINATE_CARTESIAN;
		unitsPM=1;
	}
	/**
	 * A Region is constructed as well as its plug.
	 */
	public Region(String name) {
		this();
		setName(name);
	}
	/*
	 * @return The E-Slate handle.
	 /
	public ESlateHandle getESlateHandle() {
		return handle;
	}*/
	/**
	 * Sets the name of the region. This also changes the E-Slate component name in a way like
	 * Region, Region_1, Region_2 etc.
	 */
	public void setName(String newName) {
		//Update only when there is a change
		if (((name!=null) && (name.equals(newName))) || ((name==null) && (newName==null)))
			return;
		String oldName=name;
		//20000417:Temporarily removed
		//try {
			//handle.setUniqueComponentName(newName);
			name=new String(newName);//handle.getComponentName();
		//} catch(gr.cti.eslate.base.RenamingForbiddenException ex) {
		//    name=newName;
		//}
		savedGeneral=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionRenamed(oldName,newName);
	}
	/**
	 * Gets the name of the region.
	 */
	public String getName() {
		return name;
	}
	/**
	 *
	 */
	public String toString() {
		return "Region: "+getName()+" depth "+mapNode.getDepth();
	}
	/**
	 * Sets the bounding rectangle of the map.
	 */
	protected void setBoundingRect(Rectangle2D newBoundRect) {
	   //Update only when there is a change.
		if (((boundRect!=null) && (boundRect.equals(newBoundRect))) || ((boundRect==null) && (newBoundRect==null)))
			return;
		Rectangle2D old=boundRect;
		if (newBoundRect instanceof Rectangle2DWrapper)
			boundRect=(Rectangle2DWrapper) newBoundRect;
		else
			boundRect=new Rectangle2DWrapper(newBoundRect.getX(),newBoundRect.getY(),newBoundRect.getWidth(),newBoundRect.getHeight());
		savedGeneral=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionBoundingRectChanged(old,boundRect);
	}
	/**
	 * Gets the bounding rectangle of the map.
	 */
	protected Rectangle2D getBoundingRect() {
		return boundRect;
	}
	/**
	 * Gets the views of the region.
	 */
	protected ArrayList getViews() {
		return views;
	}
	/**
	 * Creates a new view.
	 * @return The new view.
	 */
	public RegionView addView(gr.cti.eslate.protocol.IMapView parentMapView) {
		for (int i=0;i<views.size();i++) {
			if (((RegionView) views.get(i)).parentMapView==parentMapView && ((RegionView) views.get(i)).region==this)
				return (RegionView) views.get(i);
		}
		//RegionView constructor adds the view itself as a side-effect.
		//The Listeners need not to be informed because they are informed as a side-effect also.
		new RegionView(this,(MapView)parentMapView);
		return (RegionView) views.get(views.size()-1);
	}
	/**
	 * Adds a view.
	 */
	protected void addView(RegionView rv) {
		for (int i=0;i<views.size();i++)
			if (views.get(i)==rv) {
				return;
			}
		views.add(rv);
	}
	/**
	 * Adds a layer to the map and produces an event for the change.
	 */
	protected void addLayer(Layer layer) {
		Layer[] temp;
		temp=new Layer[layers.length+1];
		System.arraycopy(layers,0,temp,0,layers.length);
		layers=temp;
		layers[layers.length-1]=layer;
		savedGeneral=false;
		//Inform Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionLayerAdded(layer,postponeLayerAddRemEvent);
		//Add layer to linked regions as well
		//The mapNode is null when importing a region
		if (mapNode!=null && mapNode.hasLinks()) {
			for (int i=0;i<mapNode.getNumberOfLinks();i++)
				if (((MapNode) mapNode.getLink(i)).getRegion()!=this)
					((MapNode) mapNode.getLink(i)).getRegion().addLayer(layer);
		}
	}
	/**
	 * Removes a layer from the map and produces an event for the change.
	 */
	protected void removeLayer(Layer layer) {
		if ((layers==null) || (layer==null) || (layers.length==0)) return;
		//Check if the layer exists
		boolean found=false;
		for (int i=0;i<layers.length && !found;i++)
			if (layers[i].equals(layer))
				found=true;
		if (!found)
			return;
		Layer[] temp=new Layer[layers.length-1];
		for (int i=0,j=0;i<layers.length;i++)
			if (!layer.equals(layers[i]))
				temp[j++]=layers[i];
		layers=temp;
		savedGeneral=false;
		//Inform Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionLayerRemoved(layer,postponeLayerAddRemEvent);
		//Remove layer from linked regions as well
		if (mapNode.hasLinks()) {
			for (int i=0;i<mapNode.getNumberOfLinks();i++)
				if (((MapNode) mapNode.getLink(i)).getRegion()!=this)
					((MapNode) mapNode.getLink(i)).getRegion().removeLayer(layer);
		}
	}
	/**
	 * Swaps two layers changing their z-order.
	 * @param   index0 The index of the first layer.
	 * @param   index1 The index of the second layer.
	 */
	protected void swapLayers(int index0,int index1) {
		if ((index0<0) || (index0>=layers.length) || (index1<0) || (index1>=layers.length))
			return;
		savedGeneral=false;
		Layer temp=layers[index0];
		layers[index0]=layers[index1];
		layers[index1]=temp;
		//Inform Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionSwapLayers(layers[index1],layers[index0]);
		//Swap layers to linked regions as well
		if (mapNode.hasLinks()) {
			for (int i=0;i<mapNode.getNumberOfLinks();i++)
				((MapNode) mapNode.getLink(i)).getRegion().swapLayers(index0,index1);
		}
	}
	/**
	 * Sets the link of the region.
	 */
	protected void setLink(MapNode link) {
		MapNode old=mapNode.getLinkedTo();
		if (old!=link) {
			mapNode.setLinkedTo(link);
			//Inform Listeners.
			for (int i=0;i<views.size();i++)
				((RegionView) views.get(i)).brokerRegionLinkChanged(old,link);
		}
	}
	/**
	 * Gets the layers of the map.
	 */
	protected Layer[] getLayers() {
		return layers;
	}
	/**
	 * Sets the default map background.
	 */
	protected void setDefaultBackground(MapBackground defaultB) {
		//Inform only when there is a change.
		if ((defaultB!=null) && (defaultB.equals(background.getDefaultBackground()))) {
			//Show the preview on the map panel. Stupid but the easy way.
			if (mapNode!=null)
				mapNode.map.previewMap();
			return;
		}
		MapBackground old=background.getDefaultBackground();
		if (old!=null)
			old.setDefault(false);
		background.setDefaultBackground(defaultB);
		if (defaultB!=null)
			defaultB.setDefault(true);
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionDefaultBackgroundImageChanged(old,defaultB);
		//Show the preview on the map panel. Stupid but the easy way.
		if (mapNode!=null)
			mapNode.map.previewMap();
	}
	/**
	 * Gets the region background.
	 */
	protected IMapBackground getDefaultBackground() {
		if (background!=null) {
			if (background.getDefaultBackground()!=null)
				return background.getDefaultBackground();
			else
				return null;
		} else
			return null;
	}
	/**
	 * Gets the region background name.
	 */
	protected String getDefaultBackgroundName() {
		if (background!=null) {
			if (background.getDefaultBackground()!=null)
				return background.getDefaultBackground().getFilename();
			else
				return null;
		} else
			return null;
	}
	/**
	 * Gets all the map backgrounds.
	 */
	protected MapBackgroundModel getBackgrounds() {
		return background;
	}
	/**
	 * Gets a background by description.
	 */
	MapBackground getBackground(String s) {
		for (int i=0;i<background.getSize();i++)
			if (s.equals(((MapBackground) background.getElementAt(i)).getFilename()))
				return (MapBackground) background.getElementAt(i);
		return null;
	}
	/**
	 * Gets the map background assosiated with this date or <code>getDefaultBackground</code> if it doesn't exist.
	 */
	protected IMapBackground getBackgroundOn(Date from,Date to) {
		if (background!=null) {
			MapBackground mb;
			if ((mb=background.getBackground(from,to))!=null)
				return mb;
		}
		return getDefaultBackground();
	}
	/**
	 * Gets the map background name assosiated with this date or <code>getDefaultBackgroundName</code> if it doesn't exist.
	 */
	protected String getBackgroundOnName(Date from,Date to) {
		if (background!=null) {
			MapBackground mb;
			if ((mb=background.getBackground(from,to))!=null)
				return mb.getFilename();
		}
		return getDefaultBackgroundName();
	}
	/**
	 * The names of the backgrounds for this region.
	 */
	String[] getBackgroundNames() {
		String[] r=new String[background.getSize()];
		for (int i=0;i<r.length;i++)
			r[i]=((MapBackground) background.getElementAt(i)).getFilename();
		return r;
	}
	/**
	 * The names of the backgrounds for this region on a specific time interval.
	 */
	String[] getBackgroundNamesOn(Date from,Date to) {
		ArrayList tmp=new ArrayList();
		for (int i=0;i<background.getSize();i++) {
			MapBackground mb=(MapBackground) background.getElementAt(i);
			if ((mb.getDateTo()==null && mb.getDateFrom()==null) || ((mb.getDateTo()!=null && mb.getDateFrom()!=null) && (mb.getDateTo().after(from) || mb.getDateTo().equals(from)) && (mb.getDateFrom().before(to) || mb.getDateFrom().equals(to))))
				tmp.add(mb);
		}
		String[] r=new String[tmp.size()];
		for (int i=0;i<r.length;i++)
			r[i]=((MapBackground) tmp.get(i)).getFilename();
		return r;
	}
	/**
	 * Adds a non-time-aware background. The first image inserted is by default the default background.
	 */
	protected void addBackground(Image back) {
		MapBackground old=background.getDefaultBackground();
		MapBackground fresh=new MapBackground(back);
		background.addBackground(fresh);
		if (old==null)
			setDefaultBackground(fresh);
		saveBackground(fresh,background.indexOfBackground(fresh));
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionBackgroundImageAdded(fresh);
	}
	/**
	 * Adds a non-time-aware background. The first image inserted is by default the default background.
	 */
	protected void addBackground(MapBackground back) {
		MapBackground old=background.getDefaultBackground();
		background.addBackground(back);
		if (old==null)
			setDefaultBackground(back);
		saveBackground(back,background.indexOfBackground(back));
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionBackgroundImageAdded(back);
	}
	/**
	 * Saves a background to the default StructFile.
	 */
	private Access saveBackground(MapBackground back,int idx) {
		return saveBackground(back,idx,false);
	}
	/**
	 * Saves a background to the default StructFile.
	 */
	private Access saveBackground(MapBackground back,int idx,boolean temporary) {
		Access sf=mapNode.getMap().getSaveAccess();
		if (sf==null)
			sf=mapNode.getMap().getOpenAccess();
		try {
			sf.changeToRootDir();
			if (!sf.fileExists("regions"))
				sf.createDirectory("regions");
			sf.changeDir("regions");
			if (!sf.fileExists(getGUID()))
				sf.createDirectory(getGUID());
			sf.changeDir(getGUID());
			//Save the image data
			String filename;
			if (temporary)
				filename="tempback";
			else
				filename="back";
			OutputStream out=sf.openOutputFile(filename+idx);
			BufferedImage tosave;
			if (back.getImage() instanceof BufferedImage)
				tosave=(BufferedImage) back.getImage();
			else {
				//Create a BufferedImage for the Codec
				tosave=new BufferedImage(back.getIconWidth(),back.getIconHeight(),BufferedImage.TYPE_INT_RGB);//.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(back.getIconWidth(),back.getIconHeight(),Transparency.OPAQUE);
				tosave.getGraphics().drawImage(back.getImage(),0,0,null);
			}
			/* Papakirikou */
			/*JPEGEncodeParam param=JPEGCodec.getDefaultJPEGEncodeParam(tosave);
			param.setQuality(0.99f,false);
			JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(out,param);
			// JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(new FileOutputStream("C:\\back"+getName()+idx+".jpg"),param);
			encoder.encode(tosave,param);*/

//			NewRestorableImageIcon nri=new NewRestorableImageIcon(((MapBackground) background.getElementAt(idx)).getImage());
//			out.writeObject(nri);
//			out.close();
			if (!(back.getImage() instanceof BufferedImage)) {
				tosave.getGraphics().dispose();
				tosave.flush();
				tosave=null;
			}
			out.close();
			back.version=2;
			back.setSF(sf);
			back.setIconDirectory(getGUID());
			back.setIconFile("back"+idx);
			back.saved=true;
			//Save other info
			saveBackgroundInfo(sf,back,idx,temporary);
			back.clearImageData();
			return sf;
		} catch(Throwable ex) {
			System.err.println("MAP#200209241249: Could not save region background");
			ex.printStackTrace();
			return null;
		}
	}
	private void saveBackgroundInfo(Access sf,MapBackground back,int idx) throws IOException {
		saveBackgroundInfo(sf,back,idx,false);
	}
	private void saveBackgroundInfo(Access sf,MapBackground back,int idx,boolean temporary) throws IOException {
		//Save other info
		String filename;
		if (temporary)
			filename="tempbackinfo";
		else
			filename="backinfo";
		ObjectOutputStream oout2=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile(filename+idx)));
		back.writeExternal(oout2);
		oout2.close();
	}
	/**
	 * Removes a background.
	 */
	protected void removeBackground(MapBackground back) {
		if (back==null)
			return;
		if (back!=null && back.equals(background.getDefaultBackground()))
			setDefaultBackground(null);
		//Delete the file on disk and rename the remaining backgrounds
		int rm=background.removeBackground(back);
		Access sf=back.getSF();
		try {
			sf.changeToRootDir();
			if (!sf.fileExists("regions"))
				sf.createDirectory("regions");
			sf.changeDir("regions");
			if (!sf.fileExists(getGUID()))
				sf.createDirectory(getGUID());
			sf.changeDir(getGUID());
			sf.deleteFile("back"+rm);
			sf.deleteFile("backinfo"+rm);
		} catch(IOException ex) {
			System.err.println("MAP#200208192229: Could not delete removed region background");
			ex.printStackTrace();
		}
		//Rename backgrounds
		boolean fileExists=true; int cnt=rm+1;
		try {
			while (fileExists) {
				if (sf.fileExists("back"+cnt)) {
					sf.renameFile("back"+cnt,"back"+(cnt-1));
					sf.renameFile("backinfo"+cnt,"backinfo"+(cnt-1));
					((MapBackground)background.getElementAt(cnt-1)).setIconFile("back"+(cnt-1));
					cnt++;
				} else
					fileExists=false;
			}
		} catch(IOException ex) {
			System.err.println("MAP#200208192229: Could not rename region background");
			ex.printStackTrace();
		}
		back.clearImageData();
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionBackgroundImageRemoved(back);
		if (back==getDefaultBackground())
			setDefaultBackground(null);
	}
	/**
	 * Gets the orientation in degrees, i.e. the angle between the actual north of the map and the upright position.
	 */
	protected int getOrientation() {
		return orientation;
	}
	/**
	 * Sets the orientation in degrees, i.e. the angle between the actual north of the map and the upright position.
	 */
	protected void setOrientation(int o) {
		//Update only when there is a change
		if (o==orientation)
			return;
		int oldOrientation=orientation;
		orientation=o;
		savedGeneral=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionOrientationChanged(new Integer(oldOrientation),new Integer(orientation));
	}
	/**
	 * Gets the scale of the map. The scale is only informational and not of actual use.
	 * The string returned implies 1/(stringValue).
	 */
	protected String getScale() {
		if (scale>0)
			return ""+scale;
		else
			return null;
	}
	/**
	 * Sets the scale of the region. The scale is only informational and not of actual use.
	 */
	protected void setScale(long s) {
		//Update only when there is a change
		if (s==scale)
			return;
		long oldScale=scale;
		scale=s;
		savedGeneral=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionScaleChanged(new Long(oldScale),new Long(scale));
	}
	/**
	 * Sets the coordinate system.
	 */
	protected void setCoordinateSystem(int cs) {
		if (cs!=IRegionView.COORDINATE_CARTESIAN && cs!=IRegionView.COORDINATE_TERRESTRIAL)
			throw new IllegalArgumentException("MAP#200006211831: Illegal coordinate system.");
		if (coordSyst==cs)
			return;
		coordSyst=cs;
		savedGeneral=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionCoordinateSystemChanged();
	}
	/**
	 * @return  The coordinate system.
	 */
	protected int getCoordinateSystem() {
		return coordSyst;
	}
	/**
	 * @return  The units per meter fraction of the region. This value is valid only if polar (terrestrial) coordinates are not used.
	 */
	protected double getUnitsPerMeter() {
		if (coordSyst==IRegionView.COORDINATE_CARTESIAN)
			return unitsPM;
		else if (boundRect==null)
			return 1;
		else
			//Calculate it in the quarter of the line that divides the map in two horizontaly.
			//This is to avoid measuring a distance greater than half the earth which makes the algorithm fail.
			return (boundRect.getWidth()/4)/measureDistance(boundRect.getX(),boundRect.getY()+boundRect.getHeight()/2,boundRect.getX()+boundRect.getWidth()/4,boundRect.getY()+boundRect.getHeight()/2);
			//return (Math.sqrt(Math.pow(boundRect.getWidth(),2)+Math.pow(boundRect.getHeight(),2)))/measureDistance(boundRect.getX(),boundRect.getY(),boundRect.getX()+boundRect.getWidth(),boundRect.getY()+boundRect.getWidth());
	}
	/**
	 * The units per meter fraction of the region. This value is valid only if polar (terrestrial) coordinates are not used.
	 */
	protected void setUnitsPerMeter(double d) {
		unitsPM=d;
		savedGeneral=false;
		//Inform all Listeners.
		for (int i=0;i<views.size();i++)
			((RegionView) views.get(i)).brokerRegionCoordinateSystemChanged();
	}
	/**
	 * Measures the distance between two points in the coordinate space.
	 */
	public double measureDistance(double x1,double y1,double x2,double y2) {
		if (getCoordinateSystem()==IRegionView.COORDINATE_TERRESTRIAL) {
			//Polar coordinates calculation
			double xx1,yy1,xx2,yy2;
			xx1=x1*Math.PI/180; yy1=y1*Math.PI/180;
			xx2=x2*Math.PI/180; yy2=y2*Math.PI/180;
			return 1852*(((180*60)/Math.PI)*(2*Math.asin(Math.sqrt(Math.pow(Math.sin((yy1-yy2)/2),2)
			+Math.cos(yy1)*Math.cos(yy2)*Math.pow(Math.sin((xx1-xx2)/2),2)))));
		} else
			//Pethagorian theorem
			return (Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2))/getUnitsPerMeter());
	}
	/**
	 * Answers if a neighbouring region exists for this region in the specified direction.
	 * The direction parameters are static values. They are defined UP, DOWN, LEFT, RIGHT
	 * relevant to the rectangle shown in a viewer and not NORTH, etc, because it would be
	 * inconsistent in cases where region north was not straightly upwards.
	 *
	 * @return  The neighbouring node. If <code>null</code> no node exists.
	 */
	MapNode getNeighbour(int staticValue) {
		//Take the parent and all his brothers to find the neighbour
		ArrayList uncles=new ArrayList();
		MapNode parent=(MapNode) mapNode.getParent();
		if (parent!=null) {
			//The parent will be searched first
			uncles.add(parent);
			int depth=parent.getDepth();
			java.util.Enumeration it=mapNode.map.getMapRoot().breadthFirstEnumeration();
			while (it.hasMoreElements()) {
				MapNode node=(MapNode) it.nextElement();
				if (node.getDepth()==depth && node!=parent)
					uncles.add(node);
			}
		}
		MapNode neighbour=null;
		if (uncles.size()>0) {
			Point2D.Double t1,t2;
			switch (staticValue) {
				case IRegionView.NEIGHBOUR_UP:
					t1=getTopLeftCoordinate();
					t2=getTopRightCoordinate();
					for (int j=0;j<uncles.size() && neighbour==null;j++) {
						for (int i=0;i<((MapNode)uncles.get(j)).getChildCount() && neighbour==null;i++) {
							//Skip the region's node
							if (((MapNode)uncles.get(j)).getChildAt(i)==mapNode)
								continue;
							if (t1.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getBottomLeftCoordinate()) &&
								t2.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getBottomRightCoordinate()))
								neighbour=(MapNode) ((MapNode)uncles.get(j)).getChildAt(i);
						}
					}
					break;
				case IRegionView.NEIGHBOUR_DOWN:
					t1=getBottomLeftCoordinate();
					t2=getBottomRightCoordinate();
					for (int j=0;j<uncles.size() && neighbour==null;j++) {
						for (int i=0;i<((MapNode)uncles.get(j)).getChildCount() && neighbour==null;i++) {
							//Skip the region's node
							if (((MapNode)uncles.get(j)).getChildAt(i)==mapNode)
								continue;
							if (t1.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getTopLeftCoordinate()) &&
								t2.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getTopRightCoordinate()))
								neighbour=(MapNode) ((MapNode)uncles.get(j)).getChildAt(i);
						}
					}
					break;
				case IRegionView.NEIGHBOUR_LEFT:
					t1=getTopLeftCoordinate();
					t2=getBottomLeftCoordinate();
					for (int j=0;j<uncles.size() && neighbour==null;j++) {
						for (int i=0;i<((MapNode)uncles.get(j)).getChildCount() && neighbour==null;i++) {
							//Skip the region's node
							if (((MapNode)uncles.get(j)).getChildAt(i)==mapNode)
								continue;
							if (t1.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getTopRightCoordinate()) &&
								t2.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getBottomRightCoordinate()))
								neighbour=(MapNode) ((MapNode)uncles.get(j)).getChildAt(i);
						}
						//When in earth, look if we can wrap around.
						if (neighbour==null && coordSyst==IRegionView.COORDINATE_TERRESTRIAL) {
							Point2D.Double n1,n2;
							for (int i=0;i<((MapNode)uncles.get(j)).getChildCount() && neighbour==null;i++) {
								//Skip the region's node
								if (((MapNode)uncles.get(j)).getChildAt(i)==mapNode)
									continue;
								n1=((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getTopRightCoordinate();
								n2=((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getBottomRightCoordinate();
								if (t1.x==-n1.x && t1.y==n1.y && t2.x==-n2.x && t2.y==n2.y)
									neighbour=(MapNode) ((MapNode)uncles.get(j)).getChildAt(i);
							}
						}
					}
					break;
				case IRegionView.NEIGHBOUR_RIGHT:
					t1=getTopRightCoordinate();
					t2=getBottomRightCoordinate();
					for (int j=0;j<uncles.size() && neighbour==null;j++) {
						for (int i=0;i<((MapNode)uncles.get(j)).getChildCount() && neighbour==null;i++) {
							//Skip the region's node
							if (((MapNode)uncles.get(j)).getChildAt(i)==mapNode)
								continue;
							if (t1.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getTopLeftCoordinate()) &&
								t2.equals(((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getBottomLeftCoordinate()))
								neighbour=(MapNode) ((MapNode)uncles.get(j)).getChildAt(i);
						}
						//When in earth, look if we can wrap around.
						if (neighbour==null && coordSyst==IRegionView.COORDINATE_TERRESTRIAL) {
							Point2D.Double n1,n2;
							for (int i=0;i<((MapNode)uncles.get(j)).getChildCount() && neighbour==null;i++) {
								//Skip the region's node
								if (((MapNode)uncles.get(j)).getChildAt(i)==mapNode)
									continue;
								n1=((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getTopLeftCoordinate();
								n2=((MapNode) ((MapNode)uncles.get(j)).getChildAt(i)).getRegion().getBottomLeftCoordinate();
								if (t1.x==-n1.x && t1.y==n1.y && t2.x==-n2.x && t2.y==n2.y)
									neighbour=(MapNode) ((MapNode)uncles.get(j)).getChildAt(i);
							}
						}
					}
					break;
			}
		}
		return neighbour;
	}
	/**
	 * The top-left coordinate of the region. If north is not upright, it may be different
	 * than the bounding rectangle.
	 */
	Point2D.Double getTopLeftCoordinate() {
		if (boundRect==null)
			return null;
		return new Point2D.Double(boundRect.getX(),boundRect.getY()+boundRect.getHeight());
	}
	/**
	 * The top-right coordinate of the region. If north is not upright, it may be different
	 * than the bounding rectangle.
	 */
	Point2D.Double getTopRightCoordinate() {
		if (boundRect==null)
			return null;
		return new Point2D.Double(boundRect.getX()+boundRect.getWidth(),boundRect.getY()+boundRect.getHeight());
	}
	/**
	 * The bottom-left coordinate of the region. If north is not upright, it may be different
	 * than the bounding rectangle.
	 */
	Point2D.Double getBottomLeftCoordinate() {
		if (boundRect==null)
			return null;
		return new Point2D.Double(boundRect.getX(),boundRect.getY());
	}
	/**
	 * The bottom-right coordinate of the region. If north is not upright, it may be different
	 * than the bounding rectangle.
	 */
	Point2D.Double getBottomRightCoordinate() {
		if (boundRect==null)
			return null;
		return new Point2D.Double(boundRect.getX()+boundRect.getWidth(),boundRect.getY());
	}
	/**
	 * @return The node the region is contained.
	 */
	protected MapNode getMapNode() {
		return mapNode;
	}
	/**
	 * Sets the node the region is contained.
	 */
	protected void setMapNode(MapNode node) {
		mapNode=node;
	}
	/**
	 * Returns an id used in saving.
	 */
	public String getGUID() {
		return guid;
	}
	/**
	 * Used in saving.
	 */
	protected boolean isSaved() {
		boolean bgsaved=true;
		for (int i=0;i<background.getSize() && bgsaved;i++)
			bgsaved=((MapBackground) background.getElementAt(i)).isSaved();
		return savedGeneral && bgsaved;
	}

	protected void destroying() {
		savedGeneral=true;
	}
	/**
	 * Changes the structure file from where the region will request its data.
	 */
	protected void setSF(Access sf) {
		for (int i=0;i<layers.length;i++)
			layers[i].setSF(sf);
		for (int i=0;i<background.getSize();i++)
			((MapBackground) background.getElementAt(i)).setSF(sf);
	}
	/**
	 * Import a region from a file.
	 */
	static Region importRegion(ObjectInputStream in) throws IOException,ClassNotFoundException {
		StorageStructure ht=(StorageStructure) in.readObject();
		Region region=new Region();
		region.restoreRegionAttributes(ht,null);
		//Read the backgrounds
		int b=ht.get("noOfBackgrounds",0); boolean defNotSet=true;
		for (int i=0;i<b;i++) {
			MapBackground mb=new MapBackground((NewRestorableImageIcon) in.readObject());
			//Read other info
			mb.readExternal(in);
			region.background.addBackground(mb);
			if (mb.isDefault()) {
				region.background.setDefaultBackgroundIndex(i);
				defNotSet=false;
			}
		}
		//Old way of storing the default background. Now, the default is saved
		//in the state of the backgrounds to make the region independent of its backgrounds
		if (ht.containsKey("defaultBack") && defNotSet)
			region.background.setDefaultBackgroundIndex(ht.get("defaultBack",0));
		//%%%%MUST change the GUID! Otherwise, the imported region will be treated as the
		//%%%%one from which it had been exported!
		region.guid=""+region.hashCode();
		int lrcount=ht.get("numberofexportedlayers",0);
		for (int i=0;i<lrcount;i++) {
			Layer lr=Layer.importLayer(in);
			region.addLayer(lr);
		}

		return region;
	}
	/**
	 * Export the region to a file.
	 */
	void exportRegion(ObjectOutputStream out,boolean exportLayersAsWell) throws IOException {
		StorageStructure ht=regionAttributes();
		ht.put("noOfBackgrounds",background.getSize());
		ht.put("numberofexportedlayers",(exportLayersAsWell?layers.length:0));
		out.writeObject(ht);
		//Export images
		for (int i=0;i<background.getSize();i++) {
			//Save the image data
			out.writeObject(new NewRestorableImageIcon(((MapBackground) background.getElementAt(i)).getImage()));
			//Save other info
			((MapBackground) background.getElementAt(i)).writeExternal(out);
		}
		if (exportLayersAsWell) {
			for (int i=0;i<layers.length;i++)
				layers[i].exportLayer(out);
		}
	}
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
		if (need>0)
			need--;
		if (need==0 && isSaved())
			clearImageData();
	}
	/**
	 * Clears the background image data from memory.
	 */
	private void clearImageData() {
		for (int i=0;i<background.getSize();i++)
			((MapBackground) background.getElementAt(i)).clearImageData();
	}
	/**
	 * This method reads the region data from a structured stream.
	 * It is assumed that the current directory in the structured file
	 * is totaly used by this region.
	 */
	public void readStream(Access sf,Map map) throws ClassNotFoundException, IOException {
		//Read the pure region data
		ObjectInputStream in=new ObjectInputStream(new BufferedInputStream(sf.openInputFile("region")));
		StorageStructure fm=(StorageStructure) in.readObject();
		restoreRegionAttributes(fm,map);
		//Read layers
		int ls=fm.get("noOfLayers",0);
		String[] lIDs=(String[]) fm.get("layers");
		layers=new Layer[ls];
		for (int i=0;i<ls;i++)
			layers[i]=map.openLayer(sf,lIDs[i]);
		//Read the backgrounds
		sf.changeToRootDir();
		sf.changeDir("regions");
		sf.changeDir(getGUID());
		int cnt=0; boolean defNotSet=true;
		while (sf.fileExists("back"+cnt)) {
			MapBackground mb=new MapBackground(sf,getGUID(),"back"+cnt);
			//Read other info
			mb.readExternal(new ObjectInputStream(new BufferedInputStream(sf.openInputFile("backinfo"+cnt))));
			background.addBackground(mb);
			//Convert the image, if of an old version
			if (mb.version<2) {
				//Save to a temporary file first. The method saves both image and info.
				saveBackground(mb,cnt,true);
				sf.deleteFile("back"+cnt);
				sf.deleteFile("backinfo"+cnt);
				sf.renameFile("tempback"+cnt,"back"+cnt);
				sf.renameFile("tempbackinfo"+cnt,"backinfo"+cnt);
			}
			if (mb.isDefault()) {
				background.setDefaultBackgroundIndex(cnt);
				defNotSet=false;
			}
			cnt++;
		}
		//Old way of storing the default background. Now, the default is saved
		//in the state of the backgrounds to make the region independent of its backgrounds
		if (fm.containsKey("defaultBack") && defNotSet)
			background.setDefaultBackgroundIndex(fm.get("defaultBack",0));

		savedGeneral=true;
	}
	/**
	 * This method saves the region data in a structured stream.
	 * It is assumed that the current directory in the structured file
	 * will be totaly used by this region.
	 */
	public void writeStream(Access sf,Map map) throws IOException {
		if (!savedGeneral) {
			//Save the pure region data
			ObjectOutputStream oout=new ObjectOutputStream(new BufferedOutputStream(sf.openOutputFile("region")));
			StorageStructure fm=regionAttributes();
			oout.writeObject(fm);
			oout.close();

			String[] lIDs=(String[]) fm.get("layers");
			for (int i=0;i<lIDs.length;i++) {
				lIDs[i]=layers[i].getGUID();
				//Save the layers also
				map.saveLayer(sf,layers[i]);
			}
		}
		//Change the link of the backgrounds and save if any is not saved
		for (int i=0;i<background.getSize();i++) {
			MapBackground mb=(MapBackground) background.getElementAt(i);
			mb.setSF(sf);
			if (!mb.isSaved())
				saveBackgroundInfo(sf,mb,i);
		}
		//Remove its image data from memory, if noone needs them.
		if (need==0)
			clearImageData();
		savedGeneral=true;
		/*
			Version 1.0.1: -NEW Coordinate system, units per meter.
		*/
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput in) throws IOException {
	}

	StorageStructure regionAttributes() {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("guid",guid);
		ht.put("name",getName());
		ht.put("boundRect",getBoundingRect());
		ht.put("orientation",getOrientation());
		ht.put("noOfLayers",layers.length);
		ht.put("noOfBackgrounds",background.getSize());
		String[] lIDs=new String[layers.length];
		for (int i=0;i<layers.length;i++)
			lIDs[i]=layers[i].getGUID();
		ht.put("layers",lIDs);
		ht.put("coordSystem",coordSyst);
		ht.put("unitsPerMeter",unitsPM);
		return ht;
	}

	void restoreRegionAttributes(StorageStructure ht,Map map) throws IOException, ClassNotFoundException {
		guid=ht.get("guid","0");
		setName(ht.get("name",""));
		//There has been a change from Rectangle to Rectangle2D
		Object brect=ht.get("boundRect");
		if (brect==null)
			setBoundingRect(null);
		else if (brect instanceof Rectangle2DWrapper)
			setBoundingRect((Rectangle2DWrapper) brect);
		else if (brect instanceof Rectangle2D)
			setBoundingRect((Rectangle2D) brect);
		else if (brect instanceof Rectangle)
			setBoundingRect(new Rectangle2D.Double(((Rectangle)brect).x,((Rectangle)brect).y,((Rectangle)brect).width,((Rectangle)brect).height));
		setOrientation(ht.get("orientation",0));
		setCoordinateSystem(ht.get("coordSystem",coordSyst));
		setUnitsPerMeter(ht.get("unitsPerMeter",unitsPM));
	}

	/**
	 * Used for saving.
	 */
	private String guid;
	/**
	 * Used for saving.
	 */
	private boolean savedGeneral;
	/**
	 * It is an indicator which shows how many objects need this region.
	 * If it gets to 0, there is a structured file and the region images have not
	 * been modified, the image data are removed from memory.
	 */
	private int need;
	/*
	 * The E-Slate handle.
	 */
	//private ESlateHandle handle;
	/**
	 * The name of the region.
	 */
	private String name;
	/**
	 * The bounding rectangle of the region.
	 */
	private Rectangle2DWrapper boundRect;
	/**
	 * This list holds all the different views of the map.
	 */
	private ArrayList views;
	/**
	 * The map node this region belongs to.
	 */
	public MapNode mapNode;
	/**
	 * The Region Layers.
	 */
	private Layer[] layers;
	/**
	 * Set internally to postpone sending too many layer events when changing the link
	 * of the region.
	 */
	protected boolean postponeLayerAddRemEvent;
	/**
	 * The Region backgrounds. Multiple backgrounds for different periods of time may exist.
	 */
	private MapBackgroundModel background;
	/**
	 * Orientation property.
	 */
	private int orientation;
	/**
	 * Scale property.
	 */
	private long scale;
	/**
	 * Coordinate system.
	 */
	private int coordSyst;
	/**
	 * Units per meter, when cartesian coordinates are used.
	 */
	private double unitsPM;
	/**
	 * A region is pending when it is being imported but the process has not finished yet.
	 */
	boolean pending;
	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
	{
		//Localization & E-Slate stuff
		//handle=ESlate.registerPart(this);
		//Variable initialization
		views=new ArrayList(3);
		pending=false;
		postponeLayerAddRemEvent=false;
	}
}
