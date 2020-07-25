package wmp.mediaplayer.impl;

import wmp.mediaplayer._IRadioViewEvents;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM dispinterface _IRadioViewEvents.
 */
public class _IRadioViewEventsImpl extends IDispatchImpl
    implements _IRadioViewEvents
{
    public static final String INTERFACE_IDENTIFIER = "{847B4DF6-4B61-11D2-9BDB-204C4F4F5020}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _IRadioViewEventsImpl()
    {
    }

    protected _IRadioViewEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _IRadioViewEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _IRadioViewEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _IRadioViewEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _IRadioViewEventsImpl(this);
    }
}
