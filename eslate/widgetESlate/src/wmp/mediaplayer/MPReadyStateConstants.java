package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPReadyStateConstants.
 */
public class MPReadyStateConstants extends ComEnumeration
{
    public static final int mpReadyStateUninitialized = 0;
    public static final int mpReadyStateLoading = 1;
    public static final int mpReadyStateInteractive = 3;
    public static final int mpReadyStateComplete = 4;

    public MPReadyStateConstants()
    {
    }

    public MPReadyStateConstants(long val)
    {
        super(val);
    }

    public MPReadyStateConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPReadyStateConstants(this);
    }
}