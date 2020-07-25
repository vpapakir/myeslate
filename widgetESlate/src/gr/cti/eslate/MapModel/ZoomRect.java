package gr.cti.eslate.mapModel;

import java.awt.geom.Rectangle2D;

public class ZoomRect extends Rectangle2D.Double implements gr.cti.eslate.protocol.IZoomRect {
	public ZoomRect(double x,double y,double width,double height,Region region) {
		super(x,y,width,height);
		this.region=region;
	}

	public ZoomRect(MapNode n) {
		Rectangle2D loc=n.getZoomRectangle();
		if (loc!=null) {
			x=loc.getX();
			y=loc.getY();
			width=loc.getWidth();
			height=loc.getHeight();
			region=n.getRegion();
		} else {
			x=y=width=height=0;
			region=n.getRegion();
		}
	}

	/**
	 * Sets the X-coordinate of the zoom rectangle.
	 * @param d The X-coordinate.
	 */
	public void setX(double d) {
		x=d;
	}
	/**
	 * Sets the Y-coordinate of the zoom rectangle.
	 * @param d The Y-coordinate.
	 */
	public void setY(double d) {
		y=d;
	}
	/**
	 * Sets the width of the zoom rectangle.
	 * @param d The width.
	 */
	public void setWidth(double d) {
		width=d;
	}
	/**
	 * Sets the height of the zoom rectangle.
	 * @param d The height.
	 */
	public void setHeight(double d) {
		height=d;
	}

	public String getName() {
		return region.getName();
	}

	protected void setRegion(Region region) {
		this.region=region;
	}

	protected Region getRegion() {
		return region;
	}

	public gr.cti.eslate.protocol.IZoomRect createTransformedShape(java.awt.geom.AffineTransform at) {
		double[] a=new double[]{x,y,x+width,y+height};
		at.transform(a,0,a,0,2);
		ZoomRect t=new ZoomRect(Math.min(a[0],a[2]),Math.min(a[1],a[3]),Math.abs(a[2]-a[0]),Math.abs(a[3]-a[1]),region);
		return t;
	}

	private Region region;
}
