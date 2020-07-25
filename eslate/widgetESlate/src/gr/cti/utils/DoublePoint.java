//Copyright:    Copyright (c) 1999 CTI [portions copyright: Sun Microsystems]
//Company:      Computer Technology Institute

package gr.cti.utils; //in JDK1.2 we can use Point2D.Double, but anyway that one is not Serializable

/*
 * version 1.0, 26Feb1999
 * version 1.1, 15Apr1999 - removed serialUID which had copied from java.awt.Point code
 * version 2.0, 20Oct1999 - added serialVersionUID field again
 *                         - added getX(), setX() and getY(), setY() methods 
 */
/**
 * The <code>Point</code> class represents a location in a
 * two-dimensional (<i>x</i>,&nbsp;<i>y</i>) coordinate space.
 *
 * @version 2.0.0, 19-May-2006
 * @author      George Birbilis
 */

public class DoublePoint implements java.io.Serializable {

 static final long serialVersionUID = 20101999L; //20Oct1999: added serial-version, so that new versions load OK

    /**
     * The <i>x</i> coordinate.
     */
    public double x;

    /**
     * The <i>y</i> coordinate.
     */
    public double y;

////// Birb: x,y accessor methods /////////

    public double getX(){ //20Oct1999
     return x;
    }

    public void setX(double x){ //20Oct1999
     this.x=x;
    }

    public double getY(){ //20Oct1999
     return y;
    }

    public void setY(double y){ //20Oct1999
     this.y=y;
    }

////////////////////////////////////////

    /**
     * Constructs and initializes a point at the origin
     * (0,&nbsp;0) of the coordinate space.
     */
    public DoublePoint() {
        this(0, 0);
    }

    /**
     * Constructs and initializes a point with the same location as
     * the specified <code>DoublePoint</code> object.
     * @param       p a point.
     */
    public DoublePoint(DoublePoint p) {
        this(p.x, p.y);
    }

    /**
     * Constructs and initializes a point with the same location as
     * the specified <code>Point</code> object.
     * @param       p a point.
     */
    public DoublePoint(java.awt.Point p) {
        this(p.x, p.y);
    }

    /**
     * Constructs and initializes a point at the specified 
     * (<i>x</i>,&nbsp;<i>y</i>) location in the coordinate space. 
     * @param       x   the <i>x</i> coordinate.
     * @param       y   the <i>y</i> coordinate.
     */
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the location of this point.
     * This method is included for completeness, to parallel the
     * <code>getLocation</code> method of <code>Component</code>.
     * @return      a copy of this point, at the same location.
     */
    public DoublePoint getLocation() {
        return new DoublePoint(x, y);
    }

    /**
     * Sets the location of the point to the specificed location.
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * @param       p  a point, the new location for this point.
     */
    public void setLocation(DoublePoint p) {
        setLocation(p.x, p.y);
    }

    /**
     * Changes the point to have the specificed location.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * Its behavior is identical with <code>move(double,&nbsp;double)</code>.
     * @param       x  the <i>x</i> coordinate of the new location.
     * @param       y  the <i>y</i> coordinate of the new location.
     */
    public void setLocation(double x, double y) {
        move(x, y);
    }

    /**
     * Moves this point to the specificed location in the
     * (<i>x</i>,&nbsp;<i>y</i>) coordinate plane. This method
     * is identical with <code>setLocation(double,&nbsp;double)</code>.
     * @param       x  the <i>x</i> coordinate of the new location.
     * @param       y  the <i>y</i> coordinate of the new location.
     */
    public void move(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates this point, at location (<i>x</i>,&nbsp;<i>y</i>), 
     * by <code>dx</code> along the <i>x</i> axis and <code>dy</code> 
     * along the <i>y</i> axis so that it now represents the point 
     * (<code>x</code>&nbsp;<code>+</code>&nbsp;<code>dx</code>, 
     * <code>y</code>&nbsp;<code>+</code>&nbsp;<code>dy</code>). 
     * @param       x   the distance to move this point 
     *                            along the <i>x</i> axis.
     * @param       y   the distance to move this point 
     *                            along the <i>y</i> axis.
     */
    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
    }   

    /**
     * Returns the hashcode for this point.
     * @return      a hash code for this point.
     */
    public int hashCode() {
        return (int)(x*y+x+y*31);
    }

    /**
     * Determines whether two points are equal. Two instances of
     * <code>Point</code> are equal if the values of their 
     * <code>x</code> and <code>y</code> member fields, representing
     * their position in the coordinate space, are the same.
     * @param      obj   an object to be compared with this point.
     * @return     <code>true</code> if the object to be compared is
     *                     an instance of <code>Point</code> and has
     *                     the same values; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof DoublePoint) {
            DoublePoint pt = (DoublePoint)obj;
            return (x == pt.x) && (y == pt.y);
        }
        return false;
    }

    /**
     * Returns a representation of this point and its location
     * in the (<i>x</i>,&nbsp;<i>y</i>) coordinate space as a string.
     * @return    a string representation of this point, 
     *                 including the values of its member fields.
     */
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
}
