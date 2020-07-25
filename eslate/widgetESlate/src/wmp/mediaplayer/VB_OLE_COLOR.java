package wmp.mediaplayer;

import com.jniwrapper.UInt32;

/**
 * Represents COM alias VB_OLE_COLOR.
 */
public class VB_OLE_COLOR extends UInt32
{
    public VB_OLE_COLOR()
    {
    }

    public VB_OLE_COLOR(VB_OLE_COLOR that)
    {
        super(that);
    }

    public Object clone()
    {
        return new VB_OLE_COLOR(this);
    }
}