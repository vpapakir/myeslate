package wmp.mediaplayer.impl;

import wmp.mediaplayer.IAsyncPProt;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IAsyncPProt.
 */
public class IAsyncPProtImpl extends IDispatchImpl
    implements IAsyncPProt
{
    public static final String INTERFACE_IDENTIFIER = IAsyncPProt.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IAsyncPProtImpl()
    {
    }

    protected IAsyncPProtImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IAsyncPProtImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IAsyncPProtImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IAsyncPProtImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IAsyncPProtImpl(this);
    }
}
