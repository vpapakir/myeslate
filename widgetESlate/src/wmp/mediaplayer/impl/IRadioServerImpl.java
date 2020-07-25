package wmp.mediaplayer.impl;

import wmp.mediaplayer.IRadioPlayer;
import wmp.mediaplayer.IRadioServer;

import com.jniwrapper.Const;
import com.jniwrapper.Function;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IRadioServer.
 */
public class IRadioServerImpl extends IDispatchImpl
    implements IRadioServer
{
    public static final String INTERFACE_IDENTIFIER = IRadioServer.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IRadioServerImpl()
    {
    }

    protected IRadioServerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IRadioServerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IRadioServerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IRadioServerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IRadioPlayer bindToRadio(
        BStr /*[in]*/ wszRadio)
        throws ComException
    {
        IRadioPlayerImpl ppServer = new IRadioPlayerImpl();
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                wszRadio == null ? (Parameter)PTR_NULL : new Const(wszRadio),
                ppServer == null ? (Parameter)PTR_NULL : new Pointer((Parameter)ppServer)
            }
        );
        return ppServer;
    }

    public void isRadioExists(
        BStr /*[in]*/ wszRadio)
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                wszRadio == null ? (Parameter)PTR_NULL : new Const(wszRadio)
            }
        );
    }

    public void launchStandardUrl(
        BStr /*[in]*/ bszUrl,
        IUnknown /*[in]*/ pBrowser)
        throws ComException
    {
        invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bszUrl == null ? (Parameter)PTR_NULL : new Const(bszUrl),
                pBrowser == null ? (Parameter)PTR_NULL : new Const((Parameter)pBrowser)
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IRadioServerImpl(this);
    }
}
