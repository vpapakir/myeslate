package wmp.mediaplayer.impl;

import wmp.mediaplayer.IAsyncMHandler;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IAsyncMHandler.
 */
public class IAsyncMHandlerImpl extends IDispatchImpl
    implements IAsyncMHandler
{
    public static final String INTERFACE_IDENTIFIER = IAsyncMHandler.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IAsyncMHandlerImpl()
    {
    }

    protected IAsyncMHandlerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IAsyncMHandlerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IAsyncMHandlerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IAsyncMHandlerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IAsyncMHandlerImpl(this);
    }
}
