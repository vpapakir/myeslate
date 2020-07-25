package gr.cti.eslate.imageEditor;

import gr.cti.eslate.iconPalette.IconPalette;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Preview extends JPanel implements MouseListener, MouseMotionListener {
	JPanel previewCanvas;

	ImageEditor imageEditor;

	Font dialogFont;

	Font dialogFont2;

	boolean clipboardEmpty=true;

	protected ResourceBundle previewBundle=ResourceBundle.getBundle("gr.cti.eslate.imageEditor.IEBundle",Locale.getDefault());

	BufferedImage previewImage;

	final int PREVIEW_SIZE=64;

	AffineTransform scalingTransformation, existingTransform;

	double scalingFactorX=1;

	double scalingFactorY=1;

	Image openHand=loadImageIcon("Images/panCursor.gif"," ").getImage();

	Image closeHand=loadImageIcon("Images/closeHand.gif"," ").getImage();

	Cursor openHandCursor, closeHandCursor;

	IconPalette iconPalette;

	public Preview(Dimension parentDim) {
		super(false);
		this.disableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setLayout(new BorderLayout());
		setMaximumSize(parentDim);
		setMinimumSize(parentDim);
		setPreferredSize(parentDim);

		// initialize the scaling transformation as 1:1
		scalingTransformation=AffineTransform.getScaleInstance(scalingFactorX,scalingFactorY);
		addMouseListener(this);
		addMouseMotionListener(this);
		dialogFont=new Font("dialog",Font.PLAIN,10);
		dialogFont2=new Font("dialog",Font.PLAIN,11);
		// openHandCursor = Toolkit.getDefaultToolkit().createCustomCursor(openHand, new Point(13,23),
		// "openHandCursor");
		openHandCursor=Toolkit.getDefaultToolkit().createCustomCursor(openHand,new Point(0,0),"openHandCursor");
		// closeHandCursor = Toolkit.getDefaultToolkit().createCustomCursor(closeHand, new Point(13,23),
		// "closeHandCursor");
		closeHandCursor=Toolkit.getDefaultToolkit().createCustomCursor(closeHand,new Point(0,0),"closeHandCursor");
	}

	public void destroyImages() {
		if (previewImage != null)
			previewImage.flush();
		previewImage=null;
	}

	public void enableIconPaletteSupport(IconPalette iPalette) {
		iconPalette=iPalette;
	}

	// changes the scalingTransformation according to the new x & y factors
	private void setScalingFactors(double factorX,double factorY) {
		scalingFactorX=1 / factorX;
		scalingFactorY=1 / factorY;
		scalingTransformation=AffineTransform.getScaleInstance(scalingFactorX,scalingFactorY);
	}

	// sets the image to preview and calculates the amount of scaling for x & y
	public void setPreviewImage(BufferedImage im) {
		int imageWidth=im.getWidth();
		int imageHeight=im.getHeight();
		double scaleX=(imageWidth > PREVIEW_SIZE) ? (double) imageWidth / (double) PREVIEW_SIZE : 1;
		double scaleY=(imageHeight > PREVIEW_SIZE) ? (double) imageHeight / (double) PREVIEW_SIZE : 1;
		setScalingFactors(scaleX,scaleY);
		previewImage=im;
		repaint();
	}

	public ImageIcon loadImageIcon(String filename,String description) {
		try {
			URL u=this.getClass().getResource(filename);
			if (u != null)
				return new ImageIcon(u,description);
			else
				return null;
		} catch (Exception e) {
			return null;
		}
	}// loadImageIcon

	public void mouseClicked(MouseEvent e) {
		;
	}

	public void mouseEntered(MouseEvent e) {
		this.setCursor(openHandCursor);
	}

	public void mouseExited(MouseEvent e) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseReleased(MouseEvent e) {
		this.setCursor(openHandCursor);
		setCursorToRightPanel(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		iconPalette.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		Point p=e.getPoint();
		Container c=iconPalette.getParent().getParent();
		SwingUtilities.convertPointToScreen(p,this);
		SwingUtilities.convertPointFromScreen(p,c);
		BufferedImage clone=new BufferedImage(previewImage.getWidth(),previewImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2=clone.createGraphics();
		g2.drawImage(previewImage,0,0,null);

		if (c.contains(p)) {
			ImageIcon im=new ImageIcon(clone);
			iconPalette.addIcon(im);
		}
	}

	public void mousePressed(MouseEvent e) {
		this.setCursor(closeHandCursor);
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		Cursor cursor=Toolkit.getDefaultToolkit().createCustomCursor(previewImage,new Point(13,23),"draggedImage");
		this.setCursor(cursor);
		iconPalette.setCursor(cursor);
		setCursorToRightPanel(cursor);
	}

	private void setCursorToRightPanel(Cursor cursor) {
		JPanel hostPanel=(JPanel) this.getParent().getParent();
		hostPanel.setCursor(cursor);
	}

	private Point centerDrawingImage() {
		int imageWidth=previewImage.getWidth();
		int imageHeight=previewImage.getHeight();
		int scaledImageWidth=imageWidth * (int) scalingFactorX;
		int scaledImageHeight=imageHeight * (int) scalingFactorY;
		int xDraw=(imageWidth > PREVIEW_SIZE) ? 0 : (PREVIEW_SIZE - scaledImageWidth) / 2 - 2;
		int yDraw=(imageHeight > PREVIEW_SIZE) ? 0 : (PREVIEW_SIZE - scaledImageHeight) / 2 - 2;
		return new Point(xDraw,yDraw);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		existingTransform=g2.getTransform();
		existingTransform.concatenate(scalingTransformation);
		g2.setTransform(existingTransform);
		Point p=centerDrawingImage();
		g2.drawImage(previewImage,p.x,p.y,null);
	}
}
