package wmp.stdole;

import com.jniwrapper.Int32;

/**
 * Represents COM alias OLE_YPOS_PIXELS.
 */
public class OLE_YPOS_PIXELS extends Int32
{
    public OLE_YPOS_PIXELS()
    {
    }

    public OLE_YPOS_PIXELS(OLE_YPOS_PIXELS that)
    {
        super(that);
    }

    public Object clone()
    {
        return new OLE_YPOS_PIXELS(this);
    }
}