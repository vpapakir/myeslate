package wmp.mediaplayer.impl;

import wmp.mediaplayer.IMediaBindStream;

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
 * Represents COM interface IMediaBindStream.
 */
public class IMediaBindStreamImpl extends IDispatchImpl
    implements IMediaBindStream
{
    public static final String INTERFACE_IDENTIFIER = IMediaBindStream.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IMediaBindStreamImpl()
    {
    }

    protected IMediaBindStreamImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IMediaBindStreamImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IMediaBindStreamImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IMediaBindStreamImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void loadMoniker(
        BStr /*[in]*/ bszTransferContext,
        BStr /*[in]*/ bszUrl)
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bszTransferContext == null ? (Parameter)PTR_NULL : new Const(bszTransferContext),
                bszUrl == null ? (Parameter)PTR_NULL : new Const(bszUrl)
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IMediaBindStreamImpl(this);
    }
}
