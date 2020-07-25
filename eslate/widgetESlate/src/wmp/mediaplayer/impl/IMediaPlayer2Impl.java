package wmp.mediaplayer.impl;

import wmp.mediaplayer.IMediaPlayer2;
import wmp.mediaplayer.IMediaPlayerDvd;
import wmp.mediaplayer.MPShowDialogConstants;

import com.jniwrapper.Const;
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IMediaPlayer2.
 */
public class IMediaPlayer2Impl extends IMediaPlayerImpl
    implements IMediaPlayer2
{
    public static final String INTERFACE_IDENTIFIER = IMediaPlayer2.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IMediaPlayer2Impl()
    {
    }

    protected IMediaPlayer2Impl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IMediaPlayer2Impl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IMediaPlayer2Impl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IMediaPlayer2Impl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IMediaPlayerDvd getDVD()
        throws ComException
    {
        IMediaPlayerDvdImpl ppdispatch = new IMediaPlayerDvdImpl();
        invokeStandardVirtualMethod(
            185,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ppdispatch == null ? (Parameter)PTR_NULL : new Pointer((Parameter)ppdispatch)
            }
        );
        return ppdispatch;
    }

    public BStr getMediaParameter(
        Int32 /*[in]*/ EntryNum,
        BStr /*[in]*/ bstrParameterName)
        throws ComException
    {
        BStr pbstrParameterValue = new BStr();
        invokeStandardVirtualMethod(
            186,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                EntryNum,
                bstrParameterName == null ? (Parameter)PTR_NULL : new Const(bstrParameterName),
                pbstrParameterValue == null ? (Parameter)PTR_NULL : new Pointer(pbstrParameterValue)
            }
        );
        return pbstrParameterValue;
    }

    public BStr getMediaParameterName(
        Int32 /*[in]*/ EntryNum,
        Int32 /*[in]*/ Index)
        throws ComException
    {
        BStr pbstrParameterName = new BStr();
        invokeStandardVirtualMethod(
            187,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                EntryNum,
                Index,
                pbstrParameterName == null ? (Parameter)PTR_NULL : new Pointer(pbstrParameterName)
            }
        );
        return pbstrParameterName;
    }

    public Int32 getEntryCount()
        throws ComException
    {
        Int32 pNumberEntries = new Int32();
        invokeStandardVirtualMethod(
            188,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pNumberEntries == null ? (Parameter)PTR_NULL : new Pointer(pNumberEntries)
            }
        );
        return pNumberEntries;
    }

    public Int32 getCurrentEntry()
        throws ComException
    {
        Int32 pEntryNumber = new Int32();
        invokeStandardVirtualMethod(
            189,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pEntryNumber == null ? (Parameter)PTR_NULL : new Pointer(pEntryNumber)
            }
        );
        return pEntryNumber;
    }

    public void setCurrentEntry(
        Int32 /*[in]*/ EntryNumber)
        throws ComException
    {
        invokeStandardVirtualMethod(
            190,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                EntryNumber
            }
        );
    }

    public void showDialog(
        MPShowDialogConstants /*[in]*/ mpDialogIndex)
        throws ComException
    {
        invokeStandardVirtualMethod(
            191,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                mpDialogIndex
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IMediaPlayer2Impl(this);
    }
}
