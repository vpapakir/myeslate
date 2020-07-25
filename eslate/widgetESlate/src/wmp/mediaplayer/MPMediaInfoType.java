package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPMediaInfoType.
 */
public class MPMediaInfoType extends ComEnumeration
{
    public static final int mpShowFilename = 0;
    public static final int mpShowTitle = 1;
    public static final int mpShowAuthor = 2;
    public static final int mpShowCopyright = 3;
    public static final int mpShowRating = 4;
    public static final int mpShowDescription = 5;
    public static final int mpShowLogoIcon = 6;
    public static final int mpClipFilename = 7;
    public static final int mpClipTitle = 8;
    public static final int mpClipAuthor = 9;
    public static final int mpClipCopyright = 10;
    public static final int mpClipRating = 11;
    public static final int mpClipDescription = 12;
    public static final int mpClipLogoIcon = 13;
    public static final int mpBannerImage = 14;
    public static final int mpBannerMoreInfo = 15;
    public static final int mpWatermark = 16;

    public MPMediaInfoType()
    {
    }

    public MPMediaInfoType(long val)
    {
        super(val);
    }

    public MPMediaInfoType(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPMediaInfoType(this);
    }
}