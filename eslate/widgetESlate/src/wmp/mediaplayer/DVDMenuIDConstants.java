package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration DVDMenuIDConstants.
 */
public class DVDMenuIDConstants extends ComEnumeration
{
    public static final int dvdMenu_Title = 2;
    public static final int dvdMenu_Root = 3;
    public static final int dvdMenu_Subpicture = 4;
    public static final int dvdMenu_Audio = 5;
    public static final int dvdMenu_Angle = 6;
    public static final int dvdMenu_Chapter = 7;

    public DVDMenuIDConstants()
    {
    }

    public DVDMenuIDConstants(long val)
    {
        super(val);
    }

    public DVDMenuIDConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new DVDMenuIDConstants(this);
    }
}