package gr.cti.eslate.mapViewer;

import java.awt.geom.Rectangle2D;


public class RectangularSelectionShape extends Rectangle2D.Double implements SelectionShape {
	/**
	 * Constructs a rectangular selection shape.
	 * @param x     The starting x-coordinate of the bounding rectangle of the shape.
	 * @param y     The starting y-coordinate of the bounding rectangle of the shape.
	 * @param ex    The ending x-coordinate of the bounding rectangle of the shape.
	 * @param ey    The ending y-coordinate of the bounding rectangle of the shape.
	 * @param mapPane   The MapPane holding the selection shape.
	 */
	public RectangularSelectionShape(double x,double y,double ex,double ey,MapPane mapPane){
		super(Math.min(x,ex),Math.min(y,ey),Math.abs(ex-x),Math.abs(ey-y));
		this.mapPane=mapPane;
	}
	/**
	 * The same constructor as the above with the coordinates in a double array.
	 * @param coords    The coordinates.
	 * @param mapPane   The MapPane holding the selection shape.
	 */
	public RectangularSelectionShape(double[] coords,MapPane mapPane){
		this(Math.min(coords[0],coords[2]),Math.min(coords[1],coords[3]),Math.abs(coords[2]-coords[0]),Math.abs(coords[3]-coords[1]),mapPane);
	}
	/**
	 * Sets the width of the selection shape.
	 * @param width The width.
	 */
	public void setWidth(double width){
		if (width<0)
			throw new IllegalArgumentException("Negative width on selection shape.");
		setRect(getX(),getY(),width,getHeight());
		update();
	}
	/**
	 * Sets the width of the selection shape.
	 * @param height    The height.
	 */
	public void setHeight(double height){
		if (height<0)
			throw new IllegalArgumentException("Negative height on selection shape.");
		setRect(getX(),getY(),getWidth(),height);
		update();
	}
	/**
	 * Sets the starting x-axis coordinate of the bounding rectangle of the shape.
	 * @param x The coordinate.
	 */
	public void setX(double x){
		setRect(Math.min(x,getEndX()),getY(),Math.abs(getEndX()-x),getHeight());
		update();
	}
	/**
	 * Sets the starting y-axis coordinate of the bounding rectangle of the shape.
	 * @param y The coordinate.
	 */
	public void setY(double y){
		setRect(getX(),Math.min(y,getEndY()),getWidth(),Math.abs(getEndY()-y));
		update();
	}
	/**
	 * Sets the ending x-axis coordinate of the bounding rectangle of the shape.
	 * @param x The coordinate.
	 */
	public void setEndX(double x) {
		setRect(Math.min(x,getX()),getY(),Math.abs(getX()-x),getHeight());
		update();
	}
	/**
	 * Sets the ending y-axis coordinate of the bounding rectangle of the shape.
	 * @param y The coordinate.
	 */
	public void setEndY(double y) {
		setRect(getX(),Math.min(y,getY()),getWidth(),Math.abs(getY()-y));
		update();
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
	private void update(){
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
		setRect(Math.min(coords[0],coords[2]),Math.min(coords[1],coords[3]),Math.abs(coords[2]-coords[0]),Math.abs(coords[3]-coords[1]));
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
	public void fireShapeGeometryChanged(){
		if (selectionShapeListener!=null ){
			SelectionShapeEvent en=new SelectionShapeEvent(this,SelectionShapeEvent.SHAPE_GEOMETRY_CHANGED);
			selectionShapeListener.shapeGeometryChanged(en);
		}
	}

	private SelectionShapeEventMulticaster selectionShapeListener=new SelectionShapeEventMulticaster();
	private MapPane mapPane;

	public static final long serialVersionUID=3000L;
}