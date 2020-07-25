package wmp.mediaplayer.impl;

import wmp.mediaplayer.IDirectControl;

import com.jniwrapper.Const;
import com.jniwrapper.Function;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IDirectControl.
 */
public class IDirectControlImpl extends IDispatchImpl
    implements IDirectControl
{
    public static final String INTERFACE_IDENTIFIER = IDirectControl.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IDirectControlImpl()
    {
    }

    protected IDirectControlImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IDirectControlImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IDirectControlImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IDirectControlImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void createView(
        BStr /*[in]*/ bszClsid)
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bszClsid == null ? (Parameter)PTR_NULL : new Const(bszClsid)
            }
        );
    }

    public void destroyAllViews()
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IDirectControlImpl(this);
    }
}
