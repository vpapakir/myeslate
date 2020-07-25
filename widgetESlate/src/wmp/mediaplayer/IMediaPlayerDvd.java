package wmp.mediaplayer;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface IMediaPlayerDvd.
 */
public interface IMediaPlayerDvd extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{746EB440-3835-11D2-9774-0000F80855E6}";

    void buttonSelectAndActivate(
        UInt32 /*[in]*/ uiButton)
        throws ComException;

    void upperButtonSelect()
        throws ComException;

    void lowerButtonSelect()
        throws ComException;

    void leftButtonSelect()
        throws ComException;

    void rightButtonSelect()
        throws ComException;

    void buttonActivate()
        throws ComException;

    void forwardScan(
        DoubleFloat /*[in]*/ dwSpeed)
        throws ComException;

    void backwardScan(
        DoubleFloat /*[in]*/ dwSpeed)
        throws ComException;

    void prevPGSearch()
        throws ComException;

    void topPGSearch()
        throws ComException;

    void nextPGSearch()
        throws ComException;

    void titlePlay(
        UInt32 /*[in]*/ uiTitle)
        throws ComException;

    void chapterPlay(
        UInt32 /*[in]*/ uiTitle,
        UInt32 /*[in]*/ uiChapter)
        throws ComException;

    void chapterSearch(
        UInt32 /*[in]*/ Chapter)
        throws ComException;

    void menuCall(
        DVDMenuIDConstants /*[in]*/ MenuID)
        throws ComException;

    void resumeFromMenu()
        throws ComException;

    void timePlay(
        UInt32 /*[in]*/ uiTitle,
        BStr /*[in]*/ bstrTime)
        throws ComException;

    void timeSearch(
        BStr /*[in]*/ bstrTime)
        throws ComException;

    void chapterPlayAutoStop(
        UInt32 /*[in]*/ ulTitle,
        UInt32 /*[in]*/ ulChapter,
        UInt32 /*[in]*/ ulChaptersToPlay)
        throws ComException;

    void stillOff()
        throws ComException;

    void goUp()
        throws ComException;

    BStr getTotalTitleTime()
        throws ComException;

    UInt32 getNumberOfChapters(
        UInt32 /*[in]*/ ulTitle)
        throws ComException;

    BStr getAudioLanguage(
        UInt32 /*[in]*/ ulStream)
        throws ComException;

    BStr getSubpictureLanguage(
        UInt32 /*[in]*/ ulStream)
        throws ComException;

    Variant getAllGPRMs()
        throws ComException;

    Variant getAllSPRMs()
        throws ComException;

    VariantBool UOPValid(
        UInt32 /*[in]*/ ulUOP)
        throws ComException;

    UInt32 getButtonsAvailable()
        throws ComException;

    UInt32 getCurrentButton()
        throws ComException;

    UInt32 getAudioStreamsAvailable()
        throws ComException;

    UInt32 getCurrentAudioStream()
        throws ComException;

    void setCurrentAudioStream(
        UInt32 /*[in]*/ ulAudioStream)
        throws ComException;

    UInt32 getCurrentSubpictureStream()
        throws ComException;

    void setCurrentSubpictureStream(
        UInt32 /*[in]*/ ulSubpictureStream)
        throws ComException;

    UInt32 getSubpictureStreamsAvailable()
        throws ComException;

    VariantBool getSubpictureOn()
        throws ComException;

    void setSubpictureOn(
        VariantBool /*[in]*/ bSubpictureON)
        throws ComException;

    UInt32 getAnglesAvailable()
        throws ComException;

    UInt32 getCurrentAngle()
        throws ComException;

    void setCurrentAngle(
        UInt32 /*[in]*/ ulAngle)
        throws ComException;

    UInt32 getCurrentTitle()
        throws ComException;

    UInt32 getCurrentChapter()
        throws ComException;

    BStr getCurrentTime()
        throws ComException;

    void setRoot(
        BStr /*[in]*/ pbstrPath)
        throws ComException;

    BStr getRoot()
        throws ComException;

    UInt32 getFramesPerSecond()
        throws ComException;

    UInt32 getCurrentDomain()
        throws ComException;

    UInt32 getTitlesAvailable()
        throws ComException;

    UInt32 getVolumesAvailable()
        throws ComException;

    UInt32 getCurrentVolume()
        throws ComException;

    UInt32 getCurrentDiscSide()
        throws ComException;

    VariantBool getCCActive()
        throws ComException;

    void setCCActive(
        VariantBool /*[in]*/ bCCActive)
        throws ComException;

    UInt32 getCurrentCCService()
        throws ComException;

    void setCurrentCCService(
        UInt32 /*[in]*/ pulService)
        throws ComException;

    BStr getUniqueID()
        throws ComException;

    UInt32 getColorKey()
        throws ComException;

    void setColorKey(
        UInt32 /*[in]*/ pClr)
        throws ComException;
}
