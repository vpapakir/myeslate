// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleGeneralPath.java

package gr.cti.shapes;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.*;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public final class DoubleGeneralPath
    implements Shape, Cloneable
{

    public DoubleGeneralPath()
    {
        this(1, 20, 20);
    }

    public DoubleGeneralPath(int rule)
    {
        this(rule, 20, 20);
    }

    public DoubleGeneralPath(int rule, int initialCapacity)
    {
        this(rule, initialCapacity, initialCapacity);
    }

    DoubleGeneralPath(int rule, int initialTypes, int initialCoords)
    {
        setWindingRule(rule);
        pointTypes = new byte[initialTypes];
        pointCoords = new double[initialCoords * 2];
    }

    public DoubleGeneralPath(Shape s)
    {
        this(1, 20, 20);
        PathIterator pi = s.getPathIterator(null);
        setWindingRule(pi.getWindingRule());
        append(pi, false);
    }

    private void needRoom(int newTypes, int newCoords, boolean needMove)
    {
        if(needMove && numTypes == 0)
            throw new IllegalPathStateException("missing initial moveto in path definition");
        int size = pointCoords.length;
        if(numCoords + newCoords > size)
        {
            int grow = size;
            if(grow > 1000)
                grow = 1000;
            if(grow < newCoords)
                grow = newCoords;
            double arr[] = new double[size + grow];
            System.arraycopy(pointCoords, 0, arr, 0, numCoords);
            pointCoords = arr;
        }
        size = pointTypes.length;
        if(numTypes + newTypes > size)
        {
            int grow = size;
            if(grow > 500)
                grow = 500;
            if(grow < newTypes)
                grow = newTypes;
            byte arr[] = new byte[size + grow];
            System.arraycopy(pointTypes, 0, arr, 0, numTypes);
            pointTypes = arr;
        }
    }

    public synchronized void moveTo(double x, double y)
    {
        if(numTypes > 0 && pointTypes[numTypes - 1] == 0)
        {
            pointCoords[numCoords - 2] = x;
            pointCoords[numCoords - 1] = y;
        } else
        {
            needRoom(1, 2, false);
            pointTypes[numTypes++] = 0;
            pointCoords[numCoords++] = x;
            pointCoords[numCoords++] = y;
        }
    }

    public synchronized void lineTo(double x, double y)
    {
        needRoom(1, 2, true);
        pointTypes[numTypes++] = 1;
        pointCoords[numCoords++] = x;
        pointCoords[numCoords++] = y;
    }

    public synchronized void quadTo(double x1, double y1, double x2, double y2)
    {
        needRoom(1, 4, true);
        pointTypes[numTypes++] = 2;
        pointCoords[numCoords++] = x1;
        pointCoords[numCoords++] = y1;
        pointCoords[numCoords++] = x2;
        pointCoords[numCoords++] = y2;
    }

    public synchronized void curveTo(double x1, double y1, double x2, double y2, double x3, double y3)
    {
        needRoom(1, 6, true);
        pointTypes[numTypes++] = 3;
        pointCoords[numCoords++] = x1;
        pointCoords[numCoords++] = y1;
        pointCoords[numCoords++] = x2;
        pointCoords[numCoords++] = y2;
        pointCoords[numCoords++] = x3;
        pointCoords[numCoords++] = y3;
    }

    public synchronized void closePath()
    {
        if(numTypes == 0 || pointTypes[numTypes - 1] != 4)
        {
            needRoom(1, 0, true);
            pointTypes[numTypes++] = 4;
        }
    }

    public void append(Shape s, boolean connect)
    {
        PathIterator pi = s.getPathIterator(null);
        append(pi, connect);
    }

    public void append(PathIterator pi, boolean connect)
    {
        double coords[] = new double[6];
        while(!pi.isDone()) 
        {
            switch(pi.currentSegment(coords))
            {
            default:
                break;

            case 0: // '\0'
                if(!connect || numTypes < 1 || numCoords < 2)
                {
                    moveTo(coords[0], coords[1]);
                    break;
                }
                if(pointTypes[numTypes - 1] != 4 && pointCoords[numCoords - 2] == coords[0] && pointCoords[numCoords - 1] == coords[1])
                    break;
                // fall through

            case 1: // '\001'
                lineTo(coords[0], coords[1]);
                break;

            case 2: // '\002'
                quadTo(coords[0], coords[1], coords[2], coords[3]);
                break;

            case 3: // '\003'
                curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                break;

            case 4: // '\004'
                closePath();
                break;
            }
            pi.next();
            connect = false;
        }
    }

    public synchronized int getWindingRule()
    {
        return windingRule;
    }

    public void setWindingRule(int rule)
    {
        if(rule != 0 && rule != 1)
        {
            throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
        } else
        {
            windingRule = rule;
            return;
        }
    }

    public synchronized Point2D getCurrentPoint()
    {
        if(numTypes < 1 || numCoords < 2)
            return null;
        int index = numCoords;
        if(pointTypes[numTypes - 1] == 4)
        {
            int i = numTypes - 2;
label0:
            do
            {
                if(i <= 0)
                    break;
                switch(pointTypes[i])
                {
                case 4: // '\004'
                default:
                    break;

                case 0: // '\0'
                    break label0;

                case 1: // '\001'
                    index -= 2;
                    break;

                case 2: // '\002'
                    index -= 4;
                    break;

                case 3: // '\003'
                    index -= 6;
                    break;
                }
                i--;
            } while(true);
        }
        return new java.awt.geom.Point2D.Double(pointCoords[index - 2], pointCoords[index - 1]);
    }

    public synchronized void reset()
    {
        numTypes = numCoords = 0;
    }

    public void transform(AffineTransform at)
    {
        at.transform(pointCoords, 0, pointCoords, 0, numCoords / 2);
    }

    public synchronized Shape createTransformedShape(AffineTransform at)
    {
        DoubleGeneralPath gp = (DoubleGeneralPath)clone();
        if(at != null)
            gp.transform(at);
        return gp;
    }

    public Rectangle getBounds()
    {
        return getBounds2D().getBounds();
    }

    public synchronized Rectangle2D getBounds2D()
    {
        int i = numCoords;
        double x1;
        double y1;
        double x2;
        double y2;
        if(i > 0)
        {
            y1 = y2 = pointCoords[--i];
            x1 = x2 = pointCoords[--i];
            do
            {
                if(i <= 0)
                    break;
                double y = pointCoords[--i];
                double x = pointCoords[--i];
                if(x < x1)
                    x1 = x;
                if(y < y1)
                    y1 = y;
                if(x > x2)
                    x2 = x;
                if(y > y2)
                    y2 = y;
            } while(true);
        } else
        {
            x1 = y1 = x2 = y2 = 0.0D;
        }
        return new java.awt.geom.Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }

    public boolean contains(double x, double y)
    {
        return (new Area(this)).contains(x, y);
    }

    public boolean contains(Point2D p)
    {
        return contains(p.getX(), p.getY());
    }

    public boolean contains(double x, double y, double w, double h)
    {
        return (new Area(this)).contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r)
    {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean intersects(double x, double y, double w, double h)
    {
        return (new Area(this)).intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r)
    {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(AffineTransform at)
    {
        return new DoubleGeneralPathIterator(this, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    public Object clone()
    {
        try
        {
            DoubleGeneralPath copy = (DoubleGeneralPath)super.clone();
            copy.pointTypes = (byte[])pointTypes.clone();
            copy.pointCoords = (double[])pointCoords.clone();
            DoubleGeneralPath doublegeneralpath = copy;
            return doublegeneralpath;
        }
        catch(CloneNotSupportedException e)
        {
            throw new InternalError();
        }
    }

    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    //private static final byte SEG_MOVETO = 0;
    //private static final byte SEG_LINETO = 1;
    //private static final byte SEG_QUADTO = 2;
    //private static final byte SEG_CUBICTO = 3;
    //private static final byte SEG_CLOSE = 4;
    byte pointTypes[];
    double pointCoords[];
    int numTypes;
    int numCoords;
    int windingRule;
    static final int INIT_SIZE = 20;
    static final int EXPAND_MAX = 500;
}
