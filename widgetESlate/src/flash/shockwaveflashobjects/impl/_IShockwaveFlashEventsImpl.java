package flash.shockwaveflashobjects.impl;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

import flash.shockwaveflashobjects._IShockwaveFlashEvents;

/**
 * Represents COM dispinterface _IShockwaveFlashEvents.
 */
public class _IShockwaveFlashEventsImpl extends IDispatchImpl
    implements _IShockwaveFlashEvents
{
    public static final String INTERFACE_IDENTIFIER = "{D27CDB6D-AE6D-11CF-96B8-444553540000}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _IShockwaveFlashEventsImpl()
    {
    }

    protected _IShockwaveFlashEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _IShockwaveFlashEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _IShockwaveFlashEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _IShockwaveFlashEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void onReadyStateChange(
        Int32 /*[in]*/ newState)
    {
        Parameter[] parameters = new Parameter[] {
                newState
            };

        Automation.invokeDispatch(this, "onReadyStateChange", parameters, void.class);
    }

    public void onProgress(
        Int32 /*[in]*/ percentDone)
    {
        Parameter[] parameters = new Parameter[] {
                percentDone
            };

        Automation.invokeDispatch(this, "onProgress", parameters, void.class);
    }

    public void FSCommand(
        BStr /*[in]*/ command,
        BStr /*[in]*/ args)
    {
        Parameter[] parameters = new Parameter[] {
                command == null ? (Parameter)PTR_NULL : command,
                args == null ? (Parameter)PTR_NULL : args
            };

        Automation.invokeDispatch(this, "FSCommand", parameters, void.class);
    }

    public void flashCall(
        BStr /*[in]*/ request)
    {
        Parameter[] parameters = new Parameter[] {
                request == null ? (Parameter)PTR_NULL : request
            };

        Automation.invokeDispatch(this, "flashCall", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _IShockwaveFlashEventsImpl(this);
    }
}
