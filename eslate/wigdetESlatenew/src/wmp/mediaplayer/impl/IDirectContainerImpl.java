package wmp.mediaplayer.impl;

import wmp.mediaplayer.IDirectContainer;

import com.jniwrapper.Const;
import com.jniwrapper.Function;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IServiceProvider;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IDirectContainer.
 */
public class IDirectContainerImpl extends IUnknownImpl
    implements IDirectContainer
{
    public static final String INTERFACE_IDENTIFIER = IDirectContainer.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IDirectContainerImpl()
    {
    }

    protected IDirectContainerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IDirectContainerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IDirectContainerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IDirectContainerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void createControl(
        BStr /*[in]*/ bszClsid,
        UInt32 /*[in]*/ dwClsContext,
        IUnknown /*[in]*/ ppunk,
        UInt32 /*[in]*/ dwWindowStyle)
        throws ComException
    {
        invokeStandardVirtualMethod(
            3,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bszClsid == null ? (Parameter)PTR_NULL : new Const(bszClsid),
                dwClsContext,
                ppunk == null ? (Parameter)PTR_NULL : new Pointer.Const((Parameter)ppunk),
                dwWindowStyle
            }
        );
    }

    public void setServiceProvider(
        IServiceProvider /*[in]*/ pspSet)
        throws ComException
    {
        invokeStandardVirtualMethod(
            4,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pspSet == null ? (Parameter)PTR_NULL : new Const((Parameter)pspSet)
            }
        );
    }

    public void setIInputObjectSite(
        IUnknown /*[in]*/ pios)
        throws ComException
    {
        invokeStandardVirtualMethod(
            5,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pios == null ? (Parameter)PTR_NULL : new Const((Parameter)pios)
            }
        );
    }

    public void showControl()
        throws ComException
    {
        invokeStandardVirtualMethod(
            6,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void hideControl()
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void isControlCreated()
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void destroyControl()
        throws ComException
    {
        invokeStandardVirtualMethod(
            9,
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
        return new IDirectContainerImpl(this);
    }
}
