package gr.cti.eslate.iconPalette;

import java.awt.Point;
import java.util.EventObject;

import javax.swing.ImageIcon;

public class IconPlacedEvent extends EventObject {

	private ImageIcon icon;

	private Point releasePointScreen;

	private boolean drag;

	public IconPlacedEvent(Object source,ImageIcon icon,Point p,boolean dragging) {
		super(source);
		this.icon=icon;
		releasePointScreen=p;
		drag=dragging;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public boolean isDragging() {
		return drag;
	}

	public Point getPoint() {
		return releasePointScreen;
	}

}
