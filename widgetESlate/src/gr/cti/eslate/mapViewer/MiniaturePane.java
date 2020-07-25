package gr.cti.eslate.mapViewer;

import gr.cti.eslate.protocol.IMapView;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

class MiniaturePane extends JPanel {

	MiniaturePane(MapPane mapPane) {
		this.mapPane=mapPane;
		setOpaque(false);
		setBorder(new CompoundBorder(new LineBorder(Color.black),new LineBorder(Color.white)));
		setPreferredSize(new Dimension(0,0));
		enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		if (img!=null)
			img.paintIcon(this,g,0,0);
		else {
			//TODO: Change the default white color.
			g.setColor(Color.white);
			g.fillRect(0,0,getWidth(),getHeight());
		}
		//Find the coordinates of the visible rectangle of the mapPane.
		int x,y,width,height;
		java.awt.Rectangle r=mapPane.getVisibleMapRect();
		x=(int) (r.x*factor)+2;
		y=(int) (r.y*factor)+2;
		width=(int) (r.width*factor)-5;
		height=(int) (r.height*factor)-5;
		//Paint a yellow antialiased rectangle with a semitransparent black outline to show the current view.
		g2.setStroke(new BasicStroke(3));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new Color(0,0,0,150));
		g2.drawRect(x,y,width,height);
		g2.setStroke(new BasicStroke(1));
		g2.setPaint(Color.yellow);
		g2.drawRect(x,y,width,height);
	}

	protected void imageChanged(IMapView mv) {
		//Free resources
		clearImage();
		Dimension pd=getParent().getSize();
		//Find in which axis the one third will be used
		double nw,nh;
		Icon ic=mv.getActiveRegionView().getBackground();
		if (ic!=null) {
			BufferedImage im=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(ic.getIconWidth(),ic.getIconHeight(),Transparency.TRANSLUCENT);
			ic.paintIcon(this,im.getGraphics(),0,0);
			int iw=ic.getIconWidth();
			int ih=ic.getIconHeight();
			if (iw/ih>pd.getWidth()/pd.getHeight()) {
				//Use the x axis
				nw=pd.getWidth()/3;
				nh=ih*(factor=nw/iw);
			} else {
				//Use the y axis
				nh=pd.getHeight()/3;
				nw=iw*(factor=nh/ih);
			}
			img=new ImageIcon(im.getScaledInstance((int) nw,(int) nh,Image.SCALE_DEFAULT));
			im.flush();
		} else {
			//TODO: Customize the 500x500
			if (500/500>pd.getWidth()/pd.getHeight()) {
				//Use the x axis
				nw=pd.getWidth()/3;
				nh=500*(factor=nw/500);
			} else {
				//Use the y axis
				nh=pd.getHeight()/3;
				nw=500*(factor=nh/500);
			}
		}
		setSize((int) nw,(int) nh);
		getParent().repaint();
	}

	protected void clearImage() {
		if (img!=null) {
			img.getImage().flush();
			img=null;
		}
	}

	private double factor;
	private ImageIcon img;
	private MapPane mapPane;
	protected gr.cti.eslate.mapModel.MapListener mapListener=new gr.cti.eslate.mapModel.MapAdapter() {
		public void mapActiveRegionChanged(gr.cti.eslate.mapModel.MapEvent e) {
			imageChanged((IMapView) e.getSource());
		}
	};
}
