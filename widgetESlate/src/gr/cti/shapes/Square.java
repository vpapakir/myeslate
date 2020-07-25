// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Square.java

package gr.cti.shapes;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class Square extends java.awt.geom.Rectangle2D.Double
{

    public Square(double cx, double cy, double sx, double sy)
    {
        double rx = sx - cx;
        double ry = sy - cy;
        double r = Math.max(Math.abs(rx), Math.abs(ry));
        setFrame(ShapeUtilities.pointsToRectangle(cx - r, cy - r, cx + r, cy + r));
    }
}
