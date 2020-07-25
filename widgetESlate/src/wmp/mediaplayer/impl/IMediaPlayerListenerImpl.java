package wmp.mediaplayer.impl;

import wmp.mediaplayer.IMediaPlayerListener;

import com.jniwrapper.Const;
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IMediaPlayerListener.
 */
public class IMediaPlayerListenerImpl extends IUnknownImpl
    implements IMediaPlayerListener
{
    public static final String INTERFACE_IDENTIFIER = IMediaPlayerListener.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IMediaPlayerListenerImpl()
    {
    }

    protected IMediaPlayerListenerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IMediaPlayerListenerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IMediaPlayerListenerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IMediaPlayerListenerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void playStateChanged(
        Int32 /*[in]*/ lNewState)
        throws ComException
    {
        invokeStandardVirtualMethod(
            3,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                lNewState
            }
        );
    }

    public void buffering(
        VariantBool /*[in]*/ fStart)
        throws ComException
    {
        invokeStandardVirtualMethod(
            4,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                fStart
            }
        );
    }

    public void bufferPercent(
        Int32 /*[in]*/ lBufferPercent)
        throws ComException
    {
        invokeStandardVirtualMethod(
            5,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                lBufferPercent
            }
        );
    }

    public void openStateChanged(
        Int32 /*[in]*/ lOpenState)
        throws ComException
    {
        invokeStandardVirtualMethod(
            6,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                lOpenState
            }
        );
    }

    public void mediaInfoChanged(
        BStr /*[in]*/ bstrShowTitle,
        BStr /*[in]*/ bstrClipTitle,
        BStr /*[in]*/ bstrClipAuthor,
        BStr /*[in]*/ bstrClipCopyright,
        BStr /*[in]*/ bstrStationURL)
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bstrShowTitle == null ? (Parameter)PTR_NULL : new Const(bstrShowTitle),
                bstrClipTitle == null ? (Parameter)PTR_NULL : new Const(bstrClipTitle),
                bstrClipAuthor == null ? (Parameter)PTR_NULL : new Const(bstrClipAuthor),
                bstrClipCopyright == null ? (Parameter)PTR_NULL : new Const(bstrClipCopyright),
                bstrStationURL == null ? (Parameter)PTR_NULL : new Const(bstrStationURL)
            }
        );
    }

    public void qualityChanged(
        Int32 /*[in]*/ lQuality)
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                lQuality
            }
        );
    }

    public void error(
        BStr /*[in]*/ bstrError)
        throws ComException
    {
        invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bstrError == null ? (Parameter)PTR_NULL : new Const(bstrError)
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IMediaPlayerListenerImpl(this);
    }
}
