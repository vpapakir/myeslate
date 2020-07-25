package wmp.mediaplayer.impl;

import wmp.mediaplayer.IRadioBand;

import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IRadioBand.
 */
public class IRadioBandImpl extends IDispatchImpl
    implements IRadioBand
{
    public static final String INTERFACE_IDENTIFIER = IRadioBand.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IRadioBandImpl()
    {
    }

    protected IRadioBandImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IRadioBandImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IRadioBandImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IRadioBandImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void create(
        Int32 /*[in]*/ phwnd,
        Int32 /*[in]*/ hwndParent)
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                phwnd == null ? (Parameter)PTR_NULL : new Pointer.Const(phwnd),
                hwndParent
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IRadioBandImpl(this);
    }
}
