package wmp.mediaplayer.impl;

import wmp.mediaplayer._IDirectContainerEvents;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM dispinterface _IDirectContainerEvents.
 */
public class _IDirectContainerEventsImpl extends IDispatchImpl
    implements _IDirectContainerEvents
{
    public static final String INTERFACE_IDENTIFIER = "{39A2C2AA-4778-11D2-9BDB-204C4F4F5020}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _IDirectContainerEventsImpl()
    {
    }

    protected _IDirectContainerEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _IDirectContainerEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _IDirectContainerEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _IDirectContainerEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _IDirectContainerEventsImpl(this);
    }
}
