package gr.cti.eslate.mapViewer;

import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class provides a more sophisticated Panel which may have an image background.
 * It also has properties for tiling stretching and so on.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 17-Nov-1999
 */
public class ImageJPanel extends JPanel implements Externalizable {
	public static final long serialVersionUID=3000L;
	public static final int NORMAL=0;
	public static final int STRETCHED=1;
	public static final int TILED=2;
	private Color color;
	private Icon backImage, stretchImage, tileImage;
	private int imageView;

	public ImageJPanel() {
		imageView=NORMAL;

		addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			public void ancestorResized(HierarchyEvent e) {
				if (backImage==null) return;
				//Recreate the Stretch and Tile Images
				if (stretchImage!=null) {
					((ImageIcon) stretchImage).getImage().flush();
					stretchImage=new ImageIcon(((NewRestorableImageIcon) backImage).getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH));
				}
				if (tileImage!=null) {
					((ImageIcon) tileImage).getImage().flush();
					Image ti=ImageJPanel.this.createImage(getWidth(),getHeight());
					Graphics ig=ti.getGraphics();
					int w=getWidth(); int iw=backImage.getIconWidth();
					int h=getHeight(); int ih=backImage.getIconHeight();
					for (int j=0;j<h;j=j+ih)
						for (int i=0;i<w;i=i+iw)
							backImage.paintIcon(ImageJPanel.this,ig,i,j);
					tileImage=new ImageIcon(ti);
				}
			}
		});
	}

	public void setImage(Icon ii) {
		if (ii!=null) {
			if (ii instanceof ImageIcon)
				backImage=new NewRestorableImageIcon(((ImageIcon) ii).getImage());
			else {
				backImage=ii;
				if (!(ii instanceof NewRestorableImageIcon))
					System.err.println("MAPVIEWER#200005071610: Couldn't convert Icon to NewRestorableImageIcon.");
			}
			setOpaque(false);
		} else
			backImage=null;
		repaint();
	}

	public Icon getImage() {
		return backImage;
	}

	public void setImageViewPolicy(int i) {
		if (i==NORMAL) {
			clearStretched();
			clearTiled();
			imageView=i;
		} else if (i==STRETCHED) {
			clearTiled();
			imageView=i;
		} else if (i==TILED) {
			clearStretched();
			imageView=i;
		}
		repaint();
	}

	public int getImageViewPolicy() {
		return imageView;
	}

	private void clearStretched() {
		if (stretchImage!=null)
			((ImageIcon) stretchImage).getImage().flush();
		stretchImage=null;
	}

	public void clearTiled() {
		if (tileImage!=null)
			((ImageIcon) tileImage).getImage().flush();
		tileImage=null;
	}
	/*
	 * Known method.
	 */
	/*Temporarily removed. Don't want to change all the tooltips also.
	  Also removed from ImageJPanel.
	public void setFont(Font f) {
		super.setFont(f);
		for (int i=0;i<getComponentCount();i++)
			((Component) getComponents()[i]).setFont(f);
	}*/

	public void paintComponent(Graphics g) {
		if (backImage!=null) {
			if (imageView==STRETCHED) {
				if (stretchImage==null)
					stretchImage=new ImageIcon(((NewRestorableImageIcon) backImage).getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH));
				stretchImage.paintIcon(this,g,0,0);
			} else if (imageView==TILED) {
				if (tileImage==null) {
					Image ti=this.createImage(getWidth(),getHeight());
					Graphics ig=ti.getGraphics();
					int w=getWidth(); int iw=backImage.getIconWidth();
					int h=getHeight(); int ih=backImage.getIconHeight();
					for (int j=0;j<h;j=j+ih)
						for (int i=0;i<w;i=i+iw)
							backImage.paintIcon(this,ig,i,j);
					tileImage=new ImageIcon(ti);
				}
				tileImage.paintIcon(this,g,0,0);
			} else {
				backImage.paintIcon(this,g,0,0);
			}
		}

		super.paintComponent(g);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		StorageStructure ht=(StorageStructure) in.readObject();
		setOpaque(ht.get("opaque",isOpaque()));
		setImageViewPolicy(ht.get("viewpolicy",NORMAL));
		setImage(ht.get("image",(Icon) null));
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		ESlateFieldMap2 ht=new ESlateFieldMap2(2);
		ht.put("opaque",isOpaque());
		ht.put("viewpolicy",getImageViewPolicy());
		ht.put("image",getImage());
		out.writeObject(ht);
		out.flush();
	}
}
