package wmp.mediaplayer;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface IMediaPlayer2.
 */
public interface IMediaPlayer2 extends IMediaPlayer
{
    public static final String INTERFACE_IDENTIFIER = "{20D4F5E0-5475-11D2-9774-0000F80855E6}";

    IMediaPlayerDvd getDVD()
        throws ComException;

    BStr getMediaParameter(
        Int32 /*[in]*/ EntryNum,
        BStr /*[in]*/ bstrParameterName)
        throws ComException;

    BStr getMediaParameterName(
        Int32 /*[in]*/ EntryNum,
        Int32 /*[in]*/ Index)
        throws ComException;

    Int32 getEntryCount()
        throws ComException;

    Int32 getCurrentEntry()
        throws ComException;

    void setCurrentEntry(
        Int32 /*[in]*/ EntryNumber)
        throws ComException;

    void showDialog(
        MPShowDialogConstants /*[in]*/ mpDialogIndex)
        throws ComException;
}
