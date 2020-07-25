package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPPlayStateConstants.
 */
public class MPPlayStateConstants extends ComEnumeration
{
    public static final int mpStopped = 0;
    public static final int mpPaused = 1;
    public static final int mpPlaying = 2;
    public static final int mpWaiting = 3;
    public static final int mpScanForward = 4;
    public static final int mpScanReverse = 5;
    public static final int mpClosed = 6;

    public MPPlayStateConstants()
    {
    }

    public MPPlayStateConstants(long val)
    {
        super(val);
    }

    public MPPlayStateConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPPlayStateConstants(this);
    }
}