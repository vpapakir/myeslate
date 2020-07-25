package wmp.mediaplayer;

import com.jniwrapper.IntegerParameter;
import com.jniwrapper.win32.com.types.ComEnumeration;

/**
 * Represents COM enumeration MPShowDialogConstants.
 */
public class MPShowDialogConstants extends ComEnumeration
{
    public static final int mpShowDialogHelp = 0;
    public static final int mpShowDialogStatistics = 1;
    public static final int mpShowDialogOptions = 2;
    public static final int mpShowDialogContextMenu = 3;

    public MPShowDialogConstants()
    {
    }

    public MPShowDialogConstants(long val)
    {
        super(val);
    }

    public MPShowDialogConstants(IntegerParameter t)
    {
        super(t);
    }

    public Object clone()
    {
        return new MPShowDialogConstants(this);
    }
}