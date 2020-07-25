// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PolyLine2D.java

package gr.cti.shapes;

import java.awt.geom.Area;
import java.awt.geom.Point2D;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class PolyLine2D extends Area
{

    public PolyLine2D(Point2D p[])
    {
        this(p, false);
    }

    public PolyLine2D(Point2D p[], boolean closed)
    {
        if(p.length == 0)
            return;
        DoubleGeneralPath g = new DoubleGeneralPath();
        g.moveTo(p[0].getX(), p[0].getY());
        for(int i = 1; i < p.length; i++)
            g.lineTo(p[i].getX(), p[i].getY());

        if(closed)
            g.closePath();
        add(new Area(g));
    }
}
