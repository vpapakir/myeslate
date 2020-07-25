package gr.cti.eslate.mapModel;

import gr.cti.eslate.protocol.IMapBackground;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Access;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/* Papakirikou */
/*import com.sun.image.codec.jpeg.JPEGCodec;*/

/**
 * This is a backround for a region. It extends Component class because
 * certain calls to the Image API need a component to exist.
 */
public class MapBackground extends Component implements IMapBackground {
	MapBackground(String fileName) {
		ImageIcon ii=new ImageIcon(fileName);
		image=ii.getImage();
		iconWidth=ii.getIconWidth();
		iconHeight=ii.getIconHeight();
		System.gc();
	}
	MapBackground(Image ii) {
		image=new BufferedImage(ii.getWidth(this),ii.getHeight(this),BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(ii,0,0,this);
		iconWidth=ii.getWidth(this);
		iconHeight=ii.getHeight(this);
	}
	MapBackground(Icon ii) {
		image=new BufferedImage(ii.getIconWidth(),ii.getIconHeight(),BufferedImage.TYPE_INT_RGB);
		ii.paintIcon(this,image.getGraphics(),0,0);
		iconWidth=ii.getIconWidth();
		iconHeight=ii.getIconHeight();
	}

	MapBackground(Access sf,String dir,String file) {
		image=null;
		this.sf=sf;
		iconDir=dir;
		iconFile=file;
	}

	public String toString() {
		return "MapBackground [name:"+filename+" directory:"+iconDir+" file:"+iconFile+"]";
	}

	public int getIconWidth() {
		if (iconWidth!=Integer.MAX_VALUE)
			return iconWidth;
		else
			return (iconWidth=getImage().getWidth(this));
	}

	public int getIconHeight() {
		if (iconHeight!=Integer.MAX_VALUE)
			return iconHeight;
		else
			return (iconHeight=getImage().getHeight(this));
	}
	/**
	 * Fetches the image if it has not been loaded and paints it.
	 * This method, as well as <code>getImage()</code>, ensures that the image is fully loaded.
	 */
	public void paintIcon(Component c,Graphics g,int x,int y) {
		if (image==null)
			getImage();
		g.drawImage(image,x,y,c);
	}

	void setDateFormatPattern(String s) {
		if ((s==null && formatPattern==null) || (s!=null && s.equals(formatPattern)))
			return;
		formatPattern=s;
		saved=false;
	}

	String getDateFormatPattern() {
		return formatPattern;
	}

	void setSaveFormat(int i) {
		if (i==saveformat)
			return;
		saveformat=i;
		saved=false;
	}

	int getSaveFormat() {
		return saveformat;
	}

	protected void setFilename(String s) {
		if ((s==null && filename==null) || (s!=null && s.equals(filename)))
			return;
		filename=s;
		saved=false;
	}
	/**
	 * Gets the filename of the background.
	 * @return  A String which is the filename of the background.
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * Sets the date from which this background exists.
	 * @param   from  The date or null if this is not a time-aware background.
	 */
	public void setDateFrom(Date from) {
		if ((from==null && this.from==null) || (from!=null && from.equals(this.from)))
			return;
		this.from=from;
		saved=false;
	}
	/**
	 * Gets the date from which this background exists.
	 * @return  The date or null if this is not a time-aware background.
	 */
	public Date getDateFrom() {
		return from;
	}
	/**
	 * Sets the date until which this background exists.
	 * @param   to  The date or null if this is not a time-aware background.
	 */
	public void setDateTo(Date to) {
		if ((to==null && this.to==null) || (to!=null && to.equals(this.to)))
			return;
		this.to=to;
		saved=false;
	}
	/**
	 * Gets the date until which this background exists.
	 * @return  The date or null if this is not a time-aware background.
	 */
	public Date getDateTo() {
		return to;
	}
	protected void setIconDirectory(String s) {
		if ((s==null && iconDir==null) || (s!=null && s.equals(iconDir)))
			return;
		iconDir=new String(s);
		saved=false;
	}
	protected String getIconDirectory() {
		return iconDir;
	}
	protected void setIconFile(String s) {
		if ((s==null && iconFile==null) || (s!=null && s.equals(iconFile)))
			return;
		iconFile=new String(s);
		saved=false;
	}
	protected String getIconFile() {
		return iconFile;
	}
	protected boolean isDefault() {
		return isDefault;
	}
	protected void setDefault(boolean b) {
		if (isDefault==b)
			return;
		isDefault=b;
		saved=false;
	}

	/**
	 * Fetches the image if it has not been loaded.
	 * This method, as well as <code>paintIcon()</code>, ensures that the image is fully loaded.
	 */
	public Image getImage() {
		if ((image==null) && (iconDir!=null)) {
			//Read the image data
			try {
				sf.changeToRootDir();
				sf.changeDir("regions");
				sf.changeDir(iconDir);
				//Old compatibility mode, using png files
				if (version==1) {
					ObjectInputStream png=new ObjectInputStream(new BufferedInputStream(sf.openInputFile(iconFile)));
					image=((NewRestorableImageIcon) png.readObject()).getImage();

					/*MediaTracker mt=new MediaTracker(this);
					mt.addImage(image,0);
					if (mt.statusID(0,true)!=MediaTracker.COMPLETE)
						while (!mt.checkID(0)) ;*/
					ImageIcon icon=new ImageIcon(image);
					iconWidth=icon.getIconWidth();
					iconHeight=icon.getIconHeight();
				} else {
				//New mode, using jpeg files
					//*** USE THIS FOR JAVA 1.4.X
					//image=javax.imageio.ImageIO.read(new BufferedInputStream(sf.openInputFile(iconFile),32768));
					//*** USE THIS FOR JAVA 1.3.X
					byte count=0;
                    while (image==null) {
	                    count++;
	                    /* Papakirikou */
	                    /*try {
							image=JPEGCodec.createJPEGDecoder(sf.openInputFile(iconFile)).decodeAsBufferedImage();
	                    } catch(Throwable t) {
                            System.err.println("MAP#200206301842: Error loading JPEG file, trying to recover.");
							t.printStackTrace();
		                    if (image!=null)
		                        image.flush();
		                    image=null;
		                    System.gc();
		                    if (count==3) {
			                    System.err.println("MAP#200210291539: Completely failed to load the JPEG file.");
			                    break;
		                    }
	                    }*/
                    }
//					image=(new NewRestorableImageIcon(sf.openInputFile(iconFile))).getImage();
				}
			} catch(Exception e) {
				System.err.println("MAP#200004171844: Could not load background.");
				e.printStackTrace();
				return null;
			}
		}
		return image;
	}
	/**
	 * Clears the image data from memory.
	 */
	public void clearImageData() {
		if (image!=null && iconDir!=null)
			try {
				image.flush();
			} catch(Throwable e) {
				System.out.println("MM: Background resources failed to clear.");
			} finally {
				image=null;
			}
	}
	/**
	 * Sets the structured file from which the data will be loaded.
	 */
	protected void setSF(Access sf) {
		if (sf==this.sf)
			return;
		this.sf=sf;
	}
	/**
	 * Gets the structured file from which the data will be loaded.
	 */
	protected Access getSF() {
		return sf;
	}
	protected boolean isSaved() {
		return saved;
	}
	/**
	 * Externalization input.
	 */
	public void readExternal(ObjectInput in) throws ClassNotFoundException,IOException {
		StorageStructure ht=(StorageStructure) in.readObject();
		from=(Date) ht.get("from",(Date) null);
		to=(Date) ht.get("to",(Date) null);
		filename=ht.get("filename","");
		formatPattern=ht.get("formatPattern","");
		saveformat=ht.get("saveformat",saveformat);
		isDefault=ht.get("isDefault",isDefault);
		version=ht.get("version",1);
		iconWidth=ht.get("iconWidth",iconWidth);
		iconHeight=ht.get("iconHeight",iconHeight);
		if (version<2)
			saved=false;
		else
			saved=true;
	}
	/**
	 * Externalization output.
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(1);
		ht.put("from",from);
		ht.put("to",to);
		ht.put("filename",filename);
		ht.put("formatPattern",formatPattern);
		ht.put("saveformat",saveformat);
		ht.put("isDefault",isDefault);
		//Version is changed in Region as well, for compatibility
		ht.put("version",version);
		ht.put("iconWidth",iconWidth);
		ht.put("iconHeight",iconHeight);
		out.writeObject(ht);
		//out.flush();
	}

	/**
	 * The serial version UID.
	 * Currently 3000.
	 */
	static final long serialVersionUID=3000L;
	private String filename="";
	private Date from,to;
	private Image image;
	private Access sf;
	private String formatPattern;
	private String iconDir;
	private String iconFile;
	private boolean isDefault=false;
	boolean saved=false;
	//Version is changed in Region as well, for compatibility
	int version=2;
	private int saveformat=NewRestorableImageIcon.JPG;
	/**
	 * The icon width. It is stored to be available without loading the image.
	 */
	private int iconWidth=Integer.MAX_VALUE;
	/**
	 * The icon height. It is stored to be available without loading the image.
	 */
	private int iconHeight=Integer.MAX_VALUE;
}
