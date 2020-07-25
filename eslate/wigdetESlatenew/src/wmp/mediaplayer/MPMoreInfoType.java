package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPMoreInfoType.
 */
public class MPMoreInfoType extends ComEnumeration
{
    public static final int mpShowURL = 0;
    public static final int mpClipURL = 1;
    public static final int mpBannerURL = 2;

    public MPMoreInfoType()
    {
    }

    public MPMoreInfoType(long val)
    {
        super(val);
    }

    public MPMoreInfoType(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPMoreInfoType(this);
    }
}