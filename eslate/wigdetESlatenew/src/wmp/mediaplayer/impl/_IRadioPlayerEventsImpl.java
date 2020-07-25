package wmp.mediaplayer.impl;

import wmp.mediaplayer._IRadioPlayerEvents;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM dispinterface _IRadioPlayerEvents.
 */
public class _IRadioPlayerEventsImpl extends IDispatchImpl
    implements _IRadioPlayerEvents
{
    public static final String INTERFACE_IDENTIFIER = "{9C2263B1-3E3C-11D2-9BD3-204C4F4F5020}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _IRadioPlayerEventsImpl()
    {
    }

    protected _IRadioPlayerEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _IRadioPlayerEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _IRadioPlayerEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _IRadioPlayerEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void stateChange(
        BStr /*[in]*/ bszUrl,
        VariantBool /*[in]*/ fPlay,
        Int32 /*[in]*/ lVolume,
        VariantBool /*[in]*/ fMute)
        throws ComException
    {
        Parameter[] parameters = new Parameter[] {
                bszUrl == null ? (Parameter)PTR_NULL : bszUrl,
                fPlay,
                lVolume,
                fMute
            };

        Automation.invokeDispatch(this, "stateChange", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _IRadioPlayerEventsImpl(this);
    }
}
