package wmp.mediaplayer;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;

/**
 * Represents Java interface for COM interface IMediaPlayerListener.
 */
public interface IMediaPlayerListener extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{33222211-5E5E-11D2-9E8E-0000F8085981}";

    void playStateChanged(
        Int32 /*[in]*/ lNewState)
        throws ComException;

    void buffering(
        VariantBool /*[in]*/ fStart)
        throws ComException;

    void bufferPercent(
        Int32 /*[in]*/ lBufferPercent)
        throws ComException;

    void openStateChanged(
        Int32 /*[in]*/ lOpenState)
        throws ComException;

    void mediaInfoChanged(
        BStr /*[in]*/ bstrShowTitle,
        BStr /*[in]*/ bstrClipTitle,
        BStr /*[in]*/ bstrClipAuthor,
        BStr /*[in]*/ bstrClipCopyright,
        BStr /*[in]*/ bstrStationURL)
        throws ComException;

    void qualityChanged(
        Int32 /*[in]*/ lQuality)
        throws ComException;

    void error(
        BStr /*[in]*/ bstrError)
        throws ComException;
}
