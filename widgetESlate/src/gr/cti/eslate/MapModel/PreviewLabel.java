package gr.cti.eslate.mapModel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * This label shows a small icon of the entry region of the map.
 * It has a transformed graphics context to show the image.
 * @author Giorgos Vasiliou
 * @version 1.0
 */
class PreviewLabel extends JLabel {
	private Icon icn;
	private AffineTransform transform;

	PreviewLabel() {
		super();
	}

	/**
	 * Set the icon to preview.
	 * @param   ic  The icon.
	 * @param   sw  The scale factor on x-axis.
	 * @param   sh  The scale factor on y-axis.
	 */
	protected void setIcon(Icon ic,double sw,double sh) {
		icn=ic;
		transform=AffineTransform.getScaleInstance(sw,sh);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (icn!=null) {
			Graphics2D g2=(Graphics2D) g;
			g2.setTransform(transform);
			icn.paintIcon(this,g2,0,0);
		}
	}
}