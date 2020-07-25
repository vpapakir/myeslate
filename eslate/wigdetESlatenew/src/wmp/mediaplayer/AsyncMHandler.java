package wmp.mediaplayer;

import wmp.mediaplayer.impl.IAsyncMHandlerImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass AsyncMHandler.
 */
public class AsyncMHandler extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{3DA2AA3E-3D96-11D2-9BD2-204C4F4F5020}");

    public AsyncMHandler()
    {
    }

    public AsyncMHandler(AsyncMHandler that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IAsyncMHandler create(ClsCtx dwClsContext) throws ComException
    {
        final IAsyncMHandlerImpl intf = new IAsyncMHandlerImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IAsyncMHandler</code> interface from IUnknown instance.
     */
    public static IAsyncMHandler queryInterface(IUnknown unknown) throws ComException
    {
        final IAsyncMHandlerImpl result = new IAsyncMHandlerImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new AsyncMHandler(this);
    }
}