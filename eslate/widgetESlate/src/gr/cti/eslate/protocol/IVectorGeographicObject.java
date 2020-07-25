package gr.cti.eslate.protocol;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * This is the interface implemented by vector geographic objects.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	4.0.0, 5-Apr-2001
 * @see		gr.cti.eslate.protocol.IRasterGeographicObject
 */
public interface IVectorGeographicObject extends GeographicObject,Shape,Cloneable {
    /**
     * Sets the object bounding rectangle top-left point.
     */
    public void setBoundingMin(double x,double y);
    /**
     * Sets the object bounding rectangle top-left point.
     */
    public void setBoundingMax(double x,double y);
    /**
     * Gets the object bounding rectangle bottom-right point.
     */
    public double getBoundingMinX();
    /**
     * Gets the object bounding rectangle bottom-right point.
     */
    public double getBoundingMinY();
    /**
     * Gets the object bounding rectangle bottom-right point.
     */
    public double getBoundingMaxX();
    /**
     * Gets the object bounding rectangle bottom-right point.
     */
    public double getBoundingMaxY();
    /**
     * Transforms the object.
     */
    public IVectorGeographicObject createTransformedShape(AffineTransform t);
    /**
     * Calculates the minimum distance between the given point and the geographic object.
     */
    public double calculateDistance(java.awt.geom.Point2D point);
}