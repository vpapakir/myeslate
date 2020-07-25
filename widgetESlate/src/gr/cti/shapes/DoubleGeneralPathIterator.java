// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleGeneralPathIterator.java

package gr.cti.shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * @version     2.0.0, 24-May-2004
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
class DoubleGeneralPathIterator
    implements PathIterator
{

    DoubleGeneralPathIterator(DoubleGeneralPath path)
    {
        this(path, null);
    }

    DoubleGeneralPathIterator(DoubleGeneralPath path, AffineTransform at)
    {
        typeIdx = 0;
        pointIdx = 0;
        this.path = path;
        affine = at;
    }

    public int getWindingRule()
    {
        return path.getWindingRule();
    }

    public boolean isDone()
    {
        return typeIdx >= path.numTypes;
    }

    public void next()
    {
        int type = path.pointTypes[typeIdx++];
        pointIdx += curvesize[type];
    }

    public int currentSegment(double coords[])
    {
        int type = path.pointTypes[typeIdx];
        int numCoords = curvesize[type];
        if(numCoords > 0 && affine != null)
            affine.transform(path.pointCoords, pointIdx, coords, 0, numCoords / 2);
        else
            System.arraycopy(path.pointCoords, pointIdx, coords, 0, numCoords);
        return type;
    }

    public int currentSegment(float coords[])
    {
        int type = path.pointTypes[typeIdx];
        int numCoords = curvesize[type];
        if(numCoords > 0 && affine != null)
        {
            affine.transform(path.pointCoords, pointIdx, coords, 0, numCoords / 2);
        } else
        {
            for(int i = 0; i < numCoords; i++)
                coords[i] = (float)path.pointCoords[pointIdx + i];

        }
        return type;
    }

    int typeIdx;
    int pointIdx;
    DoubleGeneralPath path;
    AffineTransform affine;
    private static final int curvesize[] = {
        2, 2, 4, 6, 0
    };

}
