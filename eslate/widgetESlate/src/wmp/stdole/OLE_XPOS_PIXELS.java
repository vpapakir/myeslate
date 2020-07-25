package wmp.stdole;

import com.jniwrapper.Int32;

/**
 * Represents COM alias OLE_XPOS_PIXELS.
 */
public class OLE_XPOS_PIXELS extends Int32
{
    public OLE_XPOS_PIXELS()
    {
    }

    public OLE_XPOS_PIXELS(OLE_XPOS_PIXELS that)
    {
        super(that);
    }

    public Object clone()
    {
        return new OLE_XPOS_PIXELS(this);
    }
}