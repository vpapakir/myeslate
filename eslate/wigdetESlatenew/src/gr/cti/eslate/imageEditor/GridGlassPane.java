package gr.cti.eslate.imageEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

class GridGlassPane extends JComponent {
	Dimension view, originalDimension;

	int scalingFactor;

	BufferedImage gridImage;

	int adjustX=0;

	int adjustY=0;

	public GridGlassPane(int scaleFactor) {
		view=new Dimension(0,0);
		scalingFactor=scaleFactor;
	}

	public void destroyImages() {
		if (gridImage != null)
			gridImage.flush();
		gridImage=null;
	}

	public void setPaintArea(Dimension dimension) {
		originalDimension=dimension;
		view=new Dimension(dimension.width + scalingFactor,dimension.height + scalingFactor);
		gridImage=new BufferedImage(view.width,view.height,BufferedImage.TYPE_INT_ARGB);
		clearGridImage();
		paintGridImage();
	}

	public void setScalingFactor(int factor) {
		scalingFactor=factor;

		/*
		 * view.width = originalDimension.width + scalingFactor; view.height = originalDimension.height + scalingFactor;
		 * gridImage = new BufferedImage(view.width, view.height, BufferedImage.TYPE_INT_ARGB); clearGridImage();
		 * paintGridImage();
		 */
	}

	public void setAdjustX(float x) {
		adjustX=(int) (x * scalingFactor);
	}

	public void setAdjustY(float y) {
		adjustY=(int) (y * scalingFactor);
	}

	private void clearGridImage() {
		Graphics2D g2=gridImage.createGraphics();
		g2.setBackground(new Color(250,250,250,0));
		g2.clearRect(0,0,gridImage.getWidth(),gridImage.getHeight());
	}

	private void paintGridImage() {
		Graphics2D g2=gridImage.createGraphics();
		g2.setColor(new Color(0,10,10,50));
		for (int i=0;i < view.width;i+=scalingFactor)
			g2.drawLine(i,0,i,view.height);
		for (int j=0;j < view.height;j+=scalingFactor)
			g2.drawLine(0,j,view.width,j);
		// repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		g2.clip(new Rectangle(0,0,view.width - scalingFactor,view.height - scalingFactor));
		g2.drawImage(gridImage,-adjustX,-adjustY,null);
	}
}
