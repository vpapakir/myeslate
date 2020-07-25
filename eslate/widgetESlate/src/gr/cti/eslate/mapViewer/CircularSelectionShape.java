package gr.cti.eslate.mapViewer;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;


/**
 * A circular selection shape.
 *
 * @author Giorgos Vasiliou
 * @version 2.0, 17 Aug 2002
 */
public class CircularSelectionShape extends Ellipse2D.Double implements SelectionShape {
	/**
	 * Constructs a circular selection shape.
	 * @param x     The starting x-coordinate of the bounding rectangle of the shape.
	 * @param y     The starting y-coordinate of the bounding rectangle of the shape.
	 * @param ex    The ending x-coordinate of the bounding rectangle of the shape.
	 * @param ey    The ending y-coordinate of the bounding rectangle of the shape.
	 * @param mapPane   The MapPane holding the selection shape.
	 */
	public CircularSelectionShape(double x,double y,double ex,double ey,MapPane mapPane){
		super(Math.min(x,ex),Math.min(y,ey),Math.abs(ex-x),Math.abs(ey-y));
		this.mapPane=mapPane;
	}
	/**
	 * The same constructor as the above with the coordinates in a double array.
	 * @param coords    The coordinates.
	 * @param mapPane   The MapPane holding the selection shape.
	 */
	public CircularSelectionShape(double[] coords,MapPane mapPane) {
		this(Math.min(coords[0],coords[2]),Math.min(coords[1],coords[3]),Math.abs(coords[2]-coords[0]),Math.abs(coords[3]-coords[1]),mapPane);
	}
	/**
	 * Moves the selection shape to another center.
	 * @param center    The new center.
	 */
	public void setCenter(Point2D.Double center) {
		setCenter(center.x,center.y);
	}
	/**
	 * Moves the selection shape to another center.
	 * @param x The x-axis coordinate of the new center.
	 * @param y The y-axis coordinate of the new center.
	 */
	public void setCenter(double x,double y) {
		setFrame(x-getWidth()/2,y-getHeight()/2,getWidth(),getHeight());
		update();
	}
	/**
	 * Changes the radius of the selection shape, keeping the same center.
	 * @param r The new radius.
	 */
	public void setRadius(double r){
		double cx=getX()+getWidth()/2;
		double cy=getY()+getHeight()/2;
		setFrame(cx-r,cy-r,2*r,2*r);
		update();
	}
	/**
	 * The selection shape center.
	 */
	public Point2D.Double getCenter(){
		return new Point2D.Double(getX()+getWidth()/2,getY()+getHeight()/2);
	}
	/**
	 * The selection shape radius.
	 */
	public double getRadius(){
		return getWidth()/2;
	}
	/**
	 * Gets the ending x-axis coordinate of the bounding rectangle of the shape.
	 * @return  The coordinate.
	 */
	public double getEndX() {
		return getX()+getWidth();
	}
	/**
	 * Gets the ending y-axis coordinate of the bounding rectangle of the shape.
	 * @return  The coordinate.
	 */
	public double getEndY() {
		return getY()+getHeight();
	}
	/**
	 * Updates the shape in the mapPane.
	 */
	private void update() {
		if (mapPane!=null)
			mapPane.updateSelectionShape();
		fireShapeGeometryChanged();
	}
	/**
	 * Sets the bounding rectangle of the selection shape. Coordinates are
	 * given in (startX,startY,endX,endY) format and not in the usual
	 * (startX,startY,width,height).
	 * @param coords    An array containing the four coordinate numbers.
	 */
	public void setCoords(double[] coords) {
		setFrame(Math.min(coords[0],coords[2]),Math.min(coords[1],coords[3]),Math.abs(coords[2]-coords[0]),Math.abs(coords[3]-coords[1]));
		update();
	}
	/**
	 * Gets the bounding rectangle of the selection shape. Coordinates are
	 * given in (startX,startY,endX,endY) format and not in the usual
	 * (startX,startY,width,height).
	 */
	public double[] getCoords() {
		return new double[]{getX(),getY(),getX()+getWidth(),getY()+getHeight()};
	}
	/**
	 * Adds a SelectionShapeListener to listen to shape changes.
	 * @param listener The listener.
	 */
	public void addSelectionShapeListener(SelectionShapeListener listener){
		selectionShapeListener.add(listener);
	}
	/**
	 * Removes a SelectionShapeListener to listen to shape changes.
	 * @param listener The listener.
	 */
	public void removeSelectionShapeListener(SelectionShapeListener listener){
		selectionShapeListener.remove(listener);
	}
	/**
	 * Fires the event.
	 */
	public void fireShapeGeometryChanged() {
		if (selectionShapeListener!=null) {
			SelectionShapeEvent en=new SelectionShapeEvent(this,SelectionShapeEvent.SHAPE_GEOMETRY_CHANGED);
			selectionShapeListener.shapeGeometryChanged(en);
		}
	}

	private SelectionShapeEventMulticaster selectionShapeListener=new SelectionShapeEventMulticaster();
	private MapPane mapPane;

	static final long serialVersionUID=3000L;
}
