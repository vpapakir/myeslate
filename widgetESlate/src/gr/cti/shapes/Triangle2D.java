// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Triangle2D.java

package gr.cti.shapes;

import java.awt.geom.Point2D;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class Triangle2D extends PolyLine2D
{

    public Triangle2D(double width, double height)
    {
        this(0.0D, 0.0D, width, 0.0D, width, height);
    }

    public Triangle2D(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        super(new Point2D[] {
            new java.awt.geom.Point2D.Double(x1, y1), new java.awt.geom.Point2D.Double(x2, y2), new java.awt.geom.Point2D.Double(x3, y3)
        });
    }

    public Triangle2D(Point2D p1, Point2D p2, Point2D p3)
    {
        super(new Point2D[] {
            p1, p2, p3
        });
    }
}
