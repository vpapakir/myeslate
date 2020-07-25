package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPDisplayModeConstants.
 */
public class MPDisplayModeConstants extends ComEnumeration
{
    public static final int mpTime = 0;
    public static final int mpFrames = 1;

    public MPDisplayModeConstants()
    {
    }

    public MPDisplayModeConstants(long val)
    {
        super(val);
    }

    public MPDisplayModeConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPDisplayModeConstants(this);
    }
}