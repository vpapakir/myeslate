package wmp.mediaplayer;

import com.jniwrapper.Int32;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface IRadioPlayer.
 */
public interface IRadioPlayer extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{9C2263AF-3E3C-11D2-9BD3-204C4F4F5020}";

    void bindRadioMemory()
        throws ComException;

    void releaseRadio()
        throws ComException;

    Int32 registerEvent(
        BStr /*[in]*/ bszEvent)
        throws ComException;

    Int32 registerWindow(
        Int32 /*[in]*/ __MIDL_0012,
        UInt32 /*[in]*/ dwMessage,
        UInt32 /*[in]*/ dwCodeSet)
        throws ComException;

    BStr getSection()
        throws ComException;

    void unregister(
        Int32 /*[in]*/ lRegister)
        throws ComException;

    Int32 getInstanceCount()
        throws ComException;

    void play()
        throws ComException;

    void stop()
        throws ComException;

    void setUrl(
        BStr /*[in]*/ param1)
        throws ComException;

    void setVolume(
        Int32 /*[in]*/ param1)
        throws ComException;

    void setMute(
        VariantBool /*[in]*/ param1)
        throws ComException;

    void getStatus(
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
        throws ComException;

    void getState(
        Int32 /*[out]*/ plOpenState,
        Int32 /*[out]*/ pfBuffering,
        Int32 /*[out]*/ plBufferingPercent,
        Int32 /*[out]*/ plQuality)
        throws ComException;
}
