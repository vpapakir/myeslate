// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoublePoint2D.java

package gr.cti.shapes;

import java.awt.geom.Point2D;
import java.io.*;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class DoublePoint2D extends Point2D.Double
    implements Externalizable
{

    public DoublePoint2D()
    {
    }

    public DoublePoint2D(double x, double y)
    {
        super(x, y);
    }

    public DoublePoint2D(Point2D point)
    {
        super(point.getX(), point.getY());
    }

    public void readExternal(ObjectInput in)
        throws ClassNotFoundException, IOException
    {
        double x = ((java.lang.Double)in.readObject()).doubleValue();
        double y = ((java.lang.Double)in.readObject()).doubleValue();
        setLocation(x, y);
    }

    public void writeExternal(ObjectOutput out)
        throws IOException
    {
        out.writeObject(new java.lang.Double(getX()));
        out.writeObject(new java.lang.Double(getY()));
    }

    public void scale(double s)
    {
        Point2DUtilities.scalePoint(this, s);
    }

    public void offset(Point2D offset)
    {
        Point2DUtilities.offsetPoint(this, offset);
    }

    static final long serialVersionUID = 0x6126e006afb68fb3L;
}
