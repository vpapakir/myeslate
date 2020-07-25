package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration ReadyStateConstants.
 */
public class ReadyStateConstants extends ComEnumeration
{
    public static final int amvUninitialized = 0;
    public static final int amvLoading = 1;
    public static final int amvInteractive = 3;
    public static final int amvComplete = 4;

    public ReadyStateConstants()
    {
    }

    public ReadyStateConstants(long val)
    {
        super(val);
    }

    public ReadyStateConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new ReadyStateConstants(this);
    }
}