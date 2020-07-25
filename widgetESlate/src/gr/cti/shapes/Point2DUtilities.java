// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Point2DUtilities.java

package gr.cti.shapes;

import java.awt.geom.Point2D;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public abstract class Point2DUtilities
{

    public Point2DUtilities()
    {
    }

    public static Point2D getScaledCopy(Point2D p, double scale)
    {
        Point2D pclone = (Point2D)p.clone();
        scalePoint(pclone, scale);
        return pclone;
    }

    public static void scalePoint(Point2D p, double scale)
    {
        p.setLocation(p.getX() * scale, p.getY() * scale);
    }

    public static Point2D getOffsetedCopy(Point2D p, Point2D offset)
    {
        Point2D pclone = (Point2D)p.clone();
        offsetPoint(pclone, offset);
        return pclone;
    }

    public static void offsetPoint(Point2D p, Point2D offset)
    {
        p.setLocation(p.getX() + offset.getX(), p.getY() + offset.getY());
    }

    public static Point2D getOffset(Point2D p, Point2D fromPoint)
    {
        return new java.awt.geom.Point2D.Double(fromPoint.getX() - p.getX(), fromPoint.getY() - p.getY());
    }

    public static double getMagnitude(Point2D p)
    {
        double x = p.getX();
        double y = p.getY();
        return Math.sqrt(x * x + y * y);
    }

    public static double getAngle(Point2D p1, Point2D p2)
    {
        double p2x = p2.getX();
        double p1x = p1.getX();
        double angle;
        if(p2x != p1x)
            angle = Math.atan((p2.getY() - p1.getY()) / (p2x - p1x));
        else
            angle = 1.5707963267948966D;
        if(p2x < p1x)
            angle += 3.1415926535897931D;
        return angle;
    }

    public static Point2D sumPoints(Point2D points[])
    {
        double sumx = 0.0D;
        double sumy = 0.0D;
        for(int i = points.length; i-- > 0;)
        {
            sumx += points[i].getX();
            sumy += points[i].getY();
        }

        return new java.awt.geom.Point2D.Double(sumx, sumy);
    }

    public static Point2D sumPointsRaizedToPower(Point2D points[], double power)
    {
        double sumx = 0.0D;
        double sumy = 0.0D;
        for(int i = points.length; i-- > 0;)
        {
            sumx += Math.pow(points[i].getX(), power);
            sumy += Math.pow(points[i].getY(), power);
        }

        return new java.awt.geom.Point2D.Double(sumx, sumy);
    }

    public static void raizeToPower(Point2D point, double power)
    {
        point.setLocation(Math.pow(point.getX(), power), Math.pow(point.getY(), power));
    }

    public static Point2D getPower(Point2D point, double power)
    {
        return new java.awt.geom.Point2D.Double(Math.pow(point.getX(), power), Math.pow(point.getY(), power));
    }

    public static Point2D getArithmeticMedianPoint(Point2D points[])
    {
        Point2D median = sumPoints(points);
        double count = points.length;
        median.setLocation(median.getX() / count, median.getY() / count);
        return median;
    }

    public static Point2D getGeometricMedianPoint(Point2D points[])
    {
        double count = points.length;
        Point2D median = sumPointsRaizedToPower(points, count);
        raizeToPower(median, (double)1 / count);
        median.setLocation(median.getX() / count, median.getY() / count);
        return median;
    }
}
