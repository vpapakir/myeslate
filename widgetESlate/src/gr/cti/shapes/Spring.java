// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Spring.java

package gr.cti.shapes;

import java.awt.geom.Area;
import java.awt.geom.Point2D;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class Spring extends Area
{

    public Spring(double sx, double sy, double length)
    {
        add(new Area(makeSpring(sx, sy, length, 0.0D, 10D)));
    }

    public static DoubleGeneralPath makeSpring(double sx, double sy, double length, double angle, 
            double spurs)
    {
        DoubleGeneralPath g = new DoubleGeneralPath();
        double r2;
        double r1 = r2 = 5D;
        double beta = 6.2800000000000002D;
        double theta = 6.2800000000000002D;
        double k = 5D;
        theta = angle;
        k = length / (double)10;
        g.moveTo(sx, sy);
        for(int t = 0; (double)t < spurs * (double)10; t++)
        {
            double x = (int)(r1 * Math.cos((beta * (double)t) / 10D) + (k * Math.cos(theta) * (double)t) / 10D + (double)t / 100D);
            double y = (int)(r2 * Math.sin((beta * (double)t) / 10D) + (k * Math.sin(theta) * (double)t) / 10D + (double)t / 100D);
            g.lineTo(sx + x, sy + y);
            g.moveTo(sx + x, sy + y);
        }

        g.closePath();
        return g;
    }

    public static DoubleGeneralPath makeSpring(Point2D from, Point2D to, double spurs)
    {
        return makeSpring(from.getX(), from.getY(), from.distance(to), Point2DUtilities.getAngle(from, to), spurs);
    }
}
