// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Circle2D.java

package gr.cti.shapes;

import java.awt.geom.Point2D;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class Circle2D extends java.awt.geom.Ellipse2D.Double
{

    public Circle2D(double cx, double cy, double radius)
    {
        super(cx - radius, cy - radius, radius * (double)2, radius * (double)2);
    }

    public Circle2D(double cx, double cy, double rx, double ry)
    {
        this(cx, cy, calculateRadius(cx, cy, rx, ry));
    }

    public Circle2D(Point2D center, double radius)
    {
        this(center.getX(), center.getY(), radius);
    }

    public Circle2D(Point2D center, Point2D pointOnCircumference)
    {
        this(center.getX(), center.getY(), pointOnCircumference.getX(), pointOnCircumference.getY());
    }

    public double getRadius()
    {
        return getWidth() / (double)2;
    }

    public Point2D getCenter()
    {
        double radius = getRadius();
        return new java.awt.geom.Point2D.Double(getX() + radius, getY() + radius);
    }

    public void setRadius(double r)
    {
        double curR = getRadius();
        double cx = getX() + curR;
        double cy = getY() + curR;
        setFrameFromCenter(cx, cy, r);
    }

    public void setCenter(Point2D center)
    {
        setCenter(center.getX(), center.getY());
    }

    public void setCenter(double x, double y)
    {
        setFrameFromCenter(x, y, getRadius());
    }

    public void setCircle(double cx, double cy, double rx, double ry)
    {
        setFrameFromCenter(cx, cy, calculateRadius(cx, cy, rx, ry));
    }

    public void setFrameFromCenter(double cx, double cy, double radius)
    {
        double diameter = radius * (double)2;
        setFrame(cx - radius, cy - radius, diameter, diameter);
    }

    public void setFrameFromCenter(Point2D center, double radius)
    {
        setFrameFromCenter(center.getX(), center.getY(), radius);
    }

    public static double calculateRadius(double cx, double cy, double rx, double ry)
    {
        return (double)Math.round(Math.sqrt((cx - rx) * (cx - rx) + (cy - ry) * (cy - ry)));
    }
}
