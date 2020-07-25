// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ShapeUtilities.java

package gr.cti.shapes;

import java.awt.geom.*;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public abstract class ShapeUtilities
{

    public ShapeUtilities()
    {
    }

    public static Rectangle2D pointsToRectangle(Point2D aPoint, Point2D theOppositePoint)
    {
        return pointsToRectangle(aPoint.getX(), aPoint.getY(), theOppositePoint.getX(), theOppositePoint.getY());
    }

    public static Rectangle2D pointsToRectangle(double x1, double y1, double x2, double y2)
    {
        double dx = x2 - x1;
        double dy = y2 - y1;
        Rectangle2D r = new java.awt.geom.Rectangle2D.Double();
        double x = dx >= (double)0 ? x1 : x2;
        double y = dy >= (double)0 ? y1 : y2;
        double width = Math.abs(dx);
        double height = Math.abs(dy);
        r.setFrame(x, y, width, height);
        return r;
    }

    public static RoundRectangle2D pointsToRoundRectangle(double x1, double y1, double x2, double y2, 
            double arcWidth, double arcHeight)
    {
        double dx = x2 - x1;
        double dy = y2 - y1;
        RoundRectangle2D r = new java.awt.geom.RoundRectangle2D.Double();
        double x = dx >= (double)0 ? x1 : x2;
        double y = dy >= (double)0 ? y1 : y2;
        double width = Math.abs(dx);
        double height = Math.abs(dy);
        r.setRoundRect(x, y, width, height, arcWidth, arcHeight);
        return r;
    }
}
