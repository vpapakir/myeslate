package wmp.mediaplayer.impl;

import wmp.mediaplayer.IRadioPlayer;

import com.jniwrapper.Const;
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IRadioPlayer.
 */
public class IRadioPlayerImpl extends IDispatchImpl
    implements IRadioPlayer
{
    public static final String INTERFACE_IDENTIFIER = IRadioPlayer.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IRadioPlayerImpl()
    {
    }

    protected IRadioPlayerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IRadioPlayerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IRadioPlayerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IRadioPlayerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void bindRadioMemory()
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void releaseRadio()
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public Int32 registerEvent(
        BStr /*[in]*/ bszEvent)
        throws ComException
    {
        Int32 plRegister = new Int32();
        invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bszEvent == null ? (Parameter)PTR_NULL : new Const(bszEvent),
                plRegister == null ? (Parameter)PTR_NULL : new Pointer(plRegister)
            }
        );
        return plRegister;
    }

    public Int32 registerWindow(
        Int32 /*[in]*/ __MIDL_0012,
        UInt32 /*[in]*/ dwMessage,
        UInt32 /*[in]*/ dwCodeSet)
        throws ComException
    {
        Int32 plRegister = new Int32();
        invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                __MIDL_0012,
                dwMessage,
                dwCodeSet,
                plRegister == null ? (Parameter)PTR_NULL : new Pointer(plRegister)
            }
        );
        return plRegister;
    }

    public BStr getSection()
        throws ComException
    {
        BStr bszSection = new BStr();
        invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bszSection == null ? (Parameter)PTR_NULL : new Pointer(bszSection)
            }
        );
        return bszSection;
    }

    public void unregister(
        Int32 /*[in]*/ lRegister)
        throws ComException
    {
        invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                lRegister
            }
        );
    }

    public Int32 getInstanceCount()
        throws ComException
    {
        Int32 plInstances = new Int32();
        invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                plInstances == null ? (Parameter)PTR_NULL : new Pointer(plInstances)
            }
        );
        return plInstances;
    }

    public void play()
        throws ComException
    {
        invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void stop()
        throws ComException
    {
        invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void setUrl(
        BStr /*[in]*/ param1)
        throws ComException
    {
        invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                param1 == null ? (Parameter)PTR_NULL : new Const(param1)
            }
        );
    }

    public void setVolume(
        Int32 /*[in]*/ param1)
        throws ComException
    {
        invokeStandardVirtualMethod(
            17,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                param1
            }
        );
    }

    public void setMute(
        VariantBool /*[in]*/ param1)
        throws ComException
    {
        invokeStandardVirtualMethod(
            18,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                param1
            }
        );
    }

    public void getStatus(
        Int32 /*[out]*/ plVolume,
        Int32 /*[out]*/ pfMute,
        Int32 /*[out]*/ pfPlay,
        BStr /*[out]*/ __MIDL_0013,
        BStr /*[out]*/ __MIDL_0014,
        BStr /*[out]*/ __MIDL_0015,
        BStr /*[out]*/ __MIDL_0016,
        BStr /*[out]*/ __MIDL_0017,
        BStr /*[out]*/ __MIDL_0018,
        BStr /*[out]*/ __MIDL_0019)
        throws ComException
    {
        invokeStandardVirtualMethod(
            19,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                plVolume == null ? (Parameter)PTR_NULL : new Pointer(plVolume),
                pfMute == null ? (Parameter)PTR_NULL : new Pointer(pfMute),
                pfPlay == null ? (Parameter)PTR_NULL : new Pointer(pfPlay),
                __MIDL_0013 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0013),
                __MIDL_0014 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0014),
                __MIDL_0015 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0015),
                __MIDL_0016 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0016),
                __MIDL_0017 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0017),
                __MIDL_0018 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0018),
                __MIDL_0019 == null ? (Parameter)PTR_NULL : new Pointer(__MIDL_0019)
            }
        );
    }

    public void getState(
        Int32 /*[out]*/ plOpenState,
        Int32 /*[out]*/ pfBuffering,
        Int32 /*[out]*/ plBufferingPercent,
        Int32 /*[out]*/ plQuality)
        throws ComException
    {
        invokeStandardVirtualMethod(
            20,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                plOpenState == null ? (Parameter)PTR_NULL : new Pointer(plOpenState),
                pfBuffering == null ? (Parameter)PTR_NULL : new Pointer(pfBuffering),
                plBufferingPercent == null ? (Parameter)PTR_NULL : new Pointer(plBufferingPercent),
                plQuality == null ? (Parameter)PTR_NULL : new Pointer(plQuality)
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IRadioPlayerImpl(this);
    }
}
