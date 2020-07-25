package wmp.mediaplayer.impl;

import wmp.mediaplayer._IAsyncPProtEvents;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM dispinterface _IAsyncPProtEvents.
 */
public class _IAsyncPProtEventsImpl extends IDispatchImpl
    implements _IAsyncPProtEvents
{
    public static final String INTERFACE_IDENTIFIER = "{3DA2AA3C-3D96-11D2-9BD2-204C4F4F5020}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _IAsyncPProtEventsImpl()
    {
    }

    protected _IAsyncPProtEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _IAsyncPProtEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _IAsyncPProtEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _IAsyncPProtEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _IAsyncPProtEventsImpl(this);
    }
}
