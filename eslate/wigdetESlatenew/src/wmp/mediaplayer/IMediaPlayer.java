package wmp.mediaplayer;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.types.Date;

/**
 * Represents Java interface for COM interface IMediaPlayer.
 */
public interface IMediaPlayer extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{22D6F311-B0F6-11D0-94AB-0080C74C7E95}";

    DoubleFloat getCurrentPosition()
        throws ComException;

    void setCurrentPosition(
        DoubleFloat /*[in]*/ pCurrentPosition)
        throws ComException;

    DoubleFloat getDuration()
        throws ComException;

    Int32 getImageSourceWidth()
        throws ComException;

    Int32 getImageSourceHeight()
        throws ComException;

    Int32 getMarkerCount()
        throws ComException;

    VariantBool getCanScan()
        throws ComException;

    VariantBool getCanSeek()
        throws ComException;

    VariantBool getCanSeekToMarkers()
        throws ComException;

    Int32 getCurrentMarker()
        throws ComException;

    void setCurrentMarker(
        Int32 /*[in]*/ pCurrentMarker)
        throws ComException;

    BStr getFileName()
        throws ComException;

    void setFileName(
        BStr /*[in]*/ pbstrFileName)
        throws ComException;

    BStr getSourceLink()
        throws ComException;

    Date getCreationDate()
        throws ComException;

    BStr getErrorCorrection()
        throws ComException;

    Int32 getBandwidth()
        throws ComException;

    Int32 getSourceProtocol()
        throws ComException;

    Int32 getReceivedPackets()
        throws ComException;

    Int32 getRecoveredPackets()
        throws ComException;

    Int32 getLostPackets()
        throws ComException;

    Int32 getReceptionQuality()
        throws ComException;

    Int32 getBufferingCount()
        throws ComException;

    VariantBool getIsBroadcast()
        throws ComException;

    Int32 getBufferingProgress()
        throws ComException;

    BStr getChannelName()
        throws ComException;

    BStr getChannelDescription()
        throws ComException;

    BStr getChannelURL()
        throws ComException;

    BStr getContactAddress()
        throws ComException;

    BStr getContactPhone()
        throws ComException;

    BStr getContactEmail()
        throws ComException;

    DoubleFloat getBufferingTime()
        throws ComException;

    void setBufferingTime(
        DoubleFloat /*[in]*/ pBufferingTime)
        throws ComException;

    VariantBool getAutoStart()
        throws ComException;

    void setAutoStart(
        VariantBool /*[in]*/ pAutoStart)
        throws ComException;

    VariantBool getAutoRewind()
        throws ComException;

    void setAutoRewind(
        VariantBool /*[in]*/ pAutoRewind)
        throws ComException;

    DoubleFloat getRate()
        throws ComException;

    void setRate(
        DoubleFloat /*[in]*/ pRate)
        throws ComException;

    VariantBool getSendKeyboardEvents()
        throws ComException;

    void setSendKeyboardEvents(
        VariantBool /*[in]*/ pSendKeyboardEvents)
        throws ComException;

    VariantBool getSendMouseClickEvents()
        throws ComException;

    void setSendMouseClickEvents(
        VariantBool /*[in]*/ pSendMouseClickEvents)
        throws ComException;

    VariantBool getSendMouseMoveEvents()
        throws ComException;

    void setSendMouseMoveEvents(
        VariantBool /*[in]*/ pSendMouseMoveEvents)
        throws ComException;

    Int32 getPlayCount()
        throws ComException;

    void setPlayCount(
        Int32 /*[in]*/ pPlayCount)
        throws ComException;

    VariantBool getClickToPlay()
        throws ComException;

    void setClickToPlay(
        VariantBool /*[in]*/ pClickToPlay)
        throws ComException;

    VariantBool getAllowScan()
        throws ComException;

    void setAllowScan(
        VariantBool /*[in]*/ pAllowScan)
        throws ComException;

    VariantBool getEnableContextMenu()
        throws ComException;

    void setEnableContextMenu(
        VariantBool /*[in]*/ pEnableContextMenu)
        throws ComException;

    Int32 getCursorType()
        throws ComException;

    void setCursorType(
        Int32 /*[in]*/ pCursorType)
        throws ComException;

    Int32 getCodecCount()
        throws ComException;

    VariantBool getAllowChangeDisplaySize()
        throws ComException;

    void setAllowChangeDisplaySize(
        VariantBool /*[in]*/ pAllowChangeDisplaySize)
        throws ComException;

    VariantBool getIsDurationValid()
        throws ComException;

    Int32 getOpenState()
        throws ComException;

    VariantBool getSendOpenStateChangeEvents()
        throws ComException;

    void setSendOpenStateChangeEvents(
        VariantBool /*[in]*/ pSendOpenStateChangeEvents)
        throws ComException;

    VariantBool getSendWarningEvents()
        throws ComException;

    void setSendWarningEvents(
        VariantBool /*[in]*/ pSendWarningEvents)
        throws ComException;

    VariantBool getSendErrorEvents()
        throws ComException;

    void setSendErrorEvents(
        VariantBool /*[in]*/ pSendErrorEvents)
        throws ComException;

    MPPlayStateConstants getPlayState()
        throws ComException;

    VariantBool getSendPlayStateChangeEvents()
        throws ComException;

    void setSendPlayStateChangeEvents(
        VariantBool /*[in]*/ pSendPlayStateChangeEvents)
        throws ComException;

    MPDisplaySizeConstants getDisplaySize()
        throws ComException;

    void setDisplaySize(
        MPDisplaySizeConstants /*[in]*/ pDisplaySize)
        throws ComException;

    VariantBool getInvokeURLs()
        throws ComException;

    void setInvokeURLs(
        VariantBool /*[in]*/ pInvokeURLs)
        throws ComException;

    BStr getBaseURL()
        throws ComException;

    void setBaseURL(
        BStr /*[in]*/ pbstrBaseURL)
        throws ComException;

    BStr getDefaultFrame()
        throws ComException;

    void setDefaultFrame(
        BStr /*[in]*/ pbstrDefaultFrame)
        throws ComException;

    VariantBool getHasError()
        throws ComException;

    BStr getErrorDescription()
        throws ComException;

    Int32 getErrorCode()
        throws ComException;

    VariantBool getAnimationAtStart()
        throws ComException;

    void setAnimationAtStart(
        VariantBool /*[in]*/ pAnimationAtStart)
        throws ComException;

    VariantBool getTransparentAtStart()
        throws ComException;

    void setTransparentAtStart(
        VariantBool /*[in]*/ pTransparentAtStart)
        throws ComException;

    Int32 getVolume()
        throws ComException;

    void setVolume(
        Int32 /*[in]*/ pVolume)
        throws ComException;

    Int32 getBalance()
        throws ComException;

    void setBalance(
        Int32 /*[in]*/ pBalance)
        throws ComException;

    MPReadyStateConstants getReadyState()
        throws ComException;

    DoubleFloat getSelectionStart()
        throws ComException;

    void setSelectionStart(
        DoubleFloat /*[in]*/ pValue)
        throws ComException;

    DoubleFloat getSelectionEnd()
        throws ComException;

    void setSelectionEnd(
        DoubleFloat /*[in]*/ pValue)
        throws ComException;

    VariantBool getShowDisplay()
        throws ComException;

    void setShowDisplay(
        VariantBool /*[in]*/ Show)
        throws ComException;

    VariantBool getShowControls()
        throws ComException;

    void setShowControls(
        VariantBool /*[in]*/ Show)
        throws ComException;

    VariantBool getShowPositionControls()
        throws ComException;

    void setShowPositionControls(
        VariantBool /*[in]*/ Show)
        throws ComException;

    VariantBool getShowTracker()
        throws ComException;

    void setShowTracker(
        VariantBool /*[in]*/ Show)
        throws ComException;

    VariantBool getEnablePositionControls()
        throws ComException;

    void setEnablePositionControls(
        VariantBool /*[in]*/ Enable)
        throws ComException;

    VariantBool getEnableTracker()
        throws ComException;

    void setEnableTracker(
        VariantBool /*[in]*/ Enable)
        throws ComException;

    VariantBool getEnabled()
        throws ComException;

    void setEnabled(
        VariantBool /*[in]*/ pEnabled)
        throws ComException;

    VB_OLE_COLOR getDisplayForeColor()
        throws ComException;

    void setDisplayForeColor(
        VB_OLE_COLOR /*[in]*/ ForeColor)
        throws ComException;

    VB_OLE_COLOR getDisplayBackColor()
        throws ComException;

    void setDisplayBackColor(
        VB_OLE_COLOR /*[in]*/ BackColor)
        throws ComException;

    MPDisplayModeConstants getDisplayMode()
        throws ComException;

    void setDisplayMode(
        MPDisplayModeConstants /*[in]*/ pValue)
        throws ComException;

    VariantBool getVideoBorder3D()
        throws ComException;

    void setVideoBorder3D(
        VariantBool /*[in]*/ pVideoBorderWidth)
        throws ComException;

    Int32 getVideoBorderWidth()
        throws ComException;

    void setVideoBorderWidth(
        Int32 /*[in]*/ pVideoBorderWidth)
        throws ComException;

    VB_OLE_COLOR getVideoBorderColor()
        throws ComException;

    void setVideoBorderColor(
        VB_OLE_COLOR /*[in]*/ pVideoBorderWidth)
        throws ComException;

    VariantBool getShowGotoBar()
        throws ComException;

    void setShowGotoBar(
        VariantBool /*[in]*/ pbool)
        throws ComException;

    VariantBool getShowStatusBar()
        throws ComException;

    void setShowStatusBar(
        VariantBool /*[in]*/ pbool)
        throws ComException;

    VariantBool getShowCaptioning()
        throws ComException;

    void setShowCaptioning(
        VariantBool /*[in]*/ pbool)
        throws ComException;

    VariantBool getShowAudioControls()
        throws ComException;

    void setShowAudioControls(
        VariantBool /*[in]*/ pbool)
        throws ComException;

    BStr getCaptioningID()
        throws ComException;

    void setCaptioningID(
        BStr /*[in]*/ pstrText)
        throws ComException;

    VariantBool getMute()
        throws ComException;

    void setMute(
        VariantBool /*[in]*/ vbool)
        throws ComException;

    VariantBool getCanPreview()
        throws ComException;

    VariantBool getPreviewMode()
        throws ComException;

    void setPreviewMode(
        VariantBool /*[in]*/ pPreviewMode)
        throws ComException;

    VariantBool getHasMultipleItems()
        throws ComException;

    Int32 getLanguage()
        throws ComException;

    void setLanguage(
        Int32 /*[in]*/ pLanguage)
        throws ComException;

    Int32 getAudioStream()
        throws ComException;

    void setAudioStream(
        Int32 /*[in]*/ pStream)
        throws ComException;

    BStr getSAMIStyle()
        throws ComException;

    void setSAMIStyle(
        BStr /*[in]*/ pbstrStyle)
        throws ComException;

    BStr getSAMILang()
        throws ComException;

    void setSAMILang(
        BStr /*[in]*/ pbstrLang)
        throws ComException;

    BStr getSAMIFileName()
        throws ComException;

    void setSAMIFileName(
        BStr /*[in]*/ pbstrFileName)
        throws ComException;

    Int32 getStreamCount()
        throws ComException;

    BStr getClientId()
        throws ComException;

    Int32 getConnectionSpeed()
        throws ComException;

    VariantBool getAutoSize()
        throws ComException;

    void setAutoSize(
        VariantBool /*[in]*/ pbool)
        throws ComException;

    VariantBool getEnableFullScreenControls()
        throws ComException;

    void setEnableFullScreenControls(
        VariantBool /*[in]*/ pbVal)
        throws ComException;

    IDispatch getActiveMovie()
        throws ComException;

    IDispatch getNSPlay()
        throws ComException;

    VariantBool getWindowlessVideo()
        throws ComException;

    void setWindowlessVideo(
        VariantBool /*[in]*/ pbool)
        throws ComException;

    void play()
        throws ComException;

    void stop()
        throws ComException;

    void pause()
        throws ComException;

    DoubleFloat getMarkerTime(
        Int32 /*[in]*/ MarkerNum)
        throws ComException;

    BStr getMarkerName(
        Int32 /*[in]*/ MarkerNum)
        throws ComException;

    void aboutBox()
        throws ComException;

    VariantBool getCodecInstalled(
        Int32 /*[in]*/ CodecNum)
        throws ComException;

    BStr getCodecDescription(
        Int32 /*[in]*/ CodecNum)
        throws ComException;

    BStr getCodecURL(
        Int32 /*[in]*/ CodecNum)
        throws ComException;

    BStr getMoreInfoURL(
        MPMoreInfoType /*[in]*/ MoreInfoType)
        throws ComException;

    BStr getMediaInfoString(
        MPMediaInfoType /*[in]*/ MediaInfoType)
        throws ComException;

    void cancel()
        throws ComException;

    void open(
        BStr /*[in]*/ bstrFileName)
        throws ComException;

    VariantBool isSoundCardEnabled()
        throws ComException;

    void next()
        throws ComException;

    void previous()
        throws ComException;

    void streamSelect(
        Int32 /*[in]*/ StreamNum)
        throws ComException;

    void fastForward()
        throws ComException;

    void fastReverse()
        throws ComException;

    BStr getStreamName(
        Int32 /*[in]*/ StreamNum)
        throws ComException;

    Int32 getStreamGroup(
        Int32 /*[in]*/ StreamNum)
        throws ComException;

    VariantBool getStreamSelected(
        Int32 /*[in]*/ StreamNum)
        throws ComException;
}
