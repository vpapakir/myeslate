package gr.cti.eslate.mapModel.geom;

import gr.cti.eslate.protocol.IVectorGeographicObject;

/**
 * An abstract implementation of the IVectorGeographicObject interface,
 * an object that exists in vector layers.
 * <P>
 *
 * @author  Giorgos Vasiliou
 * @version 2.0, 15-May-2002
 * @see     gr.cti.eslate.mapModel.point
 * @see     gr.cti.eslate.mapModel.geom.Point
 * @see     gr.cti.eslate.mapModel.polyLine
 * @see     gr.cti.eslate.mapModel.geom.PolyLine
 * @see     gr.cti.eslate.mapModel.polygon
 * @see     gr.cti.eslate.mapModel.geom.Polygon
 */
public abstract class VectorGeographicObject implements IVectorGeographicObject {
//This class has not been made Externalizable to avoid braking the compatibility with the existing
//point, polyLine and polygon classes. Their Externalization is retained.
	public VectorGeographicObject() {
	}

	/**
	 * Sets the selected state of the object.
	 * @param value The new selected status.
	 */
	public void setSelected(boolean value) {
		selected=value;
	}
	/**
	 * Gets the selected property.
	 * @return The selected property.
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * Sets the object id which is its
	 * reference to the associated database table.
	 */
	public void setID(int i) {
		id=i;
	}
	/**
	 * Gets the object id which is its
	 * reference to the associated database table.
	 */
	public int getID() {
		return id;
	}
	public int id;
	public boolean selected;
}