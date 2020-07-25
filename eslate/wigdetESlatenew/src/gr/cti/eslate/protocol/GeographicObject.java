package gr.cti.eslate.protocol;

/**
 * This is the interface implemented by geographic objects. It is the simplest predecesor
 * of VectorObjects and RasterObjects.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	4.0.0, 5-Apr-2001
 * @see		gr.cti.eslate.protocol.IVectorGeographicObject
 * @see		gr.cti.eslate.protocol.IRasterGeographicObject
 */
public interface GeographicObject {
    /**
     * Sets the selected state of the object.
     * @param value The new selected status.
     */
    public void setSelected(boolean value);
    /**
     * Gets the selected property.
     * @return The selected property.
     */
    public boolean isSelected();
    /**
     * Sets the object id which is its
     * reference to the associated database table.
     */
    public void setID(int i);
    /**
     * Gets the object id which is its
     * reference to the associated database table.
     */
    public int getID();
}







