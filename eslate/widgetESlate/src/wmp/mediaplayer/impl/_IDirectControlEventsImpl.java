package wmp.mediaplayer.impl;

import wmp.mediaplayer._IDirectControlEvents;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM dispinterface _IDirectControlEvents.
 */
public class _IDirectControlEventsImpl extends IDispatchImpl
    implements _IDirectControlEvents
{
    public static final String INTERFACE_IDENTIFIER = "{39A2C2A7-4778-11D2-9BDB-204C4F4F5020}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _IDirectControlEventsImpl()
    {
    }

    protected _IDirectControlEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _IDirectControlEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _IDirectControlEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _IDirectControlEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _IDirectControlEventsImpl(this);
    }
}
