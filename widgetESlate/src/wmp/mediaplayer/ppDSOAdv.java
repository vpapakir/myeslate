package wmp.mediaplayer;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass ppDSOAdv.
 */
public class ppDSOAdv extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{AE1A5813-5230-11D1-A6E0-006097C4E476}");

    public ppDSOAdv()
    {
    }

    public ppDSOAdv(ppDSOAdv that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IUnknown create(ClsCtx dwClsContext) throws ComException
    {
        final IUnknownImpl intf = new IUnknownImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IUnknown</code> interface from IUnknown instance.
     */
    public static IUnknown queryInterface(IUnknown unknown) throws ComException
    {
        final IUnknownImpl result = new IUnknownImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new ppDSOAdv(this);
    }
}