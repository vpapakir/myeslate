package wmp.mediaplayer;

import wmp.mediaplayer.impl.IAsyncPProtImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass AsyncPProt.
 */
public class AsyncPProt extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{3DA2AA3B-3D96-11D2-9BD2-204C4F4F5020}");

    public AsyncPProt()
    {
    }

    public AsyncPProt(AsyncPProt that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IAsyncPProt create(ClsCtx dwClsContext) throws ComException
    {
        final IAsyncPProtImpl intf = new IAsyncPProtImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IAsyncPProt</code> interface from IUnknown instance.
     */
    public static IAsyncPProt queryInterface(IUnknown unknown) throws ComException
    {
        final IAsyncPProtImpl result = new IAsyncPProtImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new AsyncPProt(this);
    }
}