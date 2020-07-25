package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPDisplaySizeConstants.
 */
public class MPDisplaySizeConstants extends ComEnumeration
{
    public static final int mpDefaultSize = 0;
    public static final int mpHalfSize = 1;
    public static final int mpDoubleSize = 2;
    public static final int mpFullScreen = 3;
    public static final int mpFitToSize = 4;
    public static final int mpOneSixteenthScreen = 5;
    public static final int mpOneFourthScreen = 6;
    public static final int mpOneHalfScreen = 7;

    public MPDisplaySizeConstants()
    {
    }

    public MPDisplaySizeConstants(long val)
    {
        super(val);
    }

    public MPDisplaySizeConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPDisplaySizeConstants(this);
    }
}