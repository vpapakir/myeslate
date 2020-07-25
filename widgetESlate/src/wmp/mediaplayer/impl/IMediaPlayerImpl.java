package wmp.mediaplayer.impl;

import wmp.mediaplayer.IMediaPlayer;
import wmp.mediaplayer.MPDisplayModeConstants;
import wmp.mediaplayer.MPDisplaySizeConstants;
import wmp.mediaplayer.MPMediaInfoType;
import wmp.mediaplayer.MPMoreInfoType;
import wmp.mediaplayer.MPPlayStateConstants;
import wmp.mediaplayer.MPReadyStateConstants;
import wmp.mediaplayer.VB_OLE_COLOR;

import com.jniwrapper.Const;
import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Function;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.Date;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IMediaPlayer.
 */
public class IMediaPlayerImpl extends IDispatchImpl
    implements IMediaPlayer
{
    public static final String INTERFACE_IDENTIFIER = IMediaPlayer.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IMediaPlayerImpl()
    {
    }

    protected IMediaPlayerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IMediaPlayerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IMediaPlayerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IMediaPlayerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public DoubleFloat getCurrentPosition()
        throws ComException
    {
        DoubleFloat pCurrentPosition = new DoubleFloat();
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCurrentPosition == null ? (Parameter)PTR_NULL : new Pointer(pCurrentPosition)
            }
        );
        return pCurrentPosition;
    }

    public void setCurrentPosition(
        DoubleFloat /*[in]*/ pCurrentPosition)
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCurrentPosition
            }
        );
    }

    public DoubleFloat getDuration()
        throws ComException
    {
        DoubleFloat pDuration = new DoubleFloat();
        invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pDuration == null ? (Parameter)PTR_NULL : new Pointer(pDuration)
            }
        );
        return pDuration;
    }

    public Int32 getImageSourceWidth()
        throws ComException
    {
        Int32 pWidth = new Int32();
        invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pWidth == null ? (Parameter)PTR_NULL : new Pointer(pWidth)
            }
        );
        return pWidth;
    }

    public Int32 getImageSourceHeight()
        throws ComException
    {
        Int32 pHeight = new Int32();
        invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pHeight == null ? (Parameter)PTR_NULL : new Pointer(pHeight)
            }
        );
        return pHeight;
    }

    public Int32 getMarkerCount()
        throws ComException
    {
        Int32 pMarkerCount = new Int32();
        invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pMarkerCount == null ? (Parameter)PTR_NULL : new Pointer(pMarkerCount)
            }
        );
        return pMarkerCount;
    }

    public VariantBool getCanScan()
        throws ComException
    {
        VariantBool pCanScan = new VariantBool();
        invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCanScan == null ? (Parameter)PTR_NULL : new Pointer(pCanScan)
            }
        );
        return pCanScan;
    }

    public VariantBool getCanSeek()
        throws ComException
    {
        VariantBool pCanSeek = new VariantBool();
        invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCanSeek == null ? (Parameter)PTR_NULL : new Pointer(pCanSeek)
            }
        );
        return pCanSeek;
    }

    public VariantBool getCanSeekToMarkers()
        throws ComException
    {
        VariantBool pCanSeekToMarkers = new VariantBool();
        invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCanSeekToMarkers == null ? (Parameter)PTR_NULL : new Pointer(pCanSeekToMarkers)
            }
        );
        return pCanSeekToMarkers;
    }

    public Int32 getCurrentMarker()
        throws ComException
    {
        Int32 pCurrentMarker = new Int32();
        invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCurrentMarker == null ? (Parameter)PTR_NULL : new Pointer(pCurrentMarker)
            }
        );
        return pCurrentMarker;
    }

    public void setCurrentMarker(
        Int32 /*[in]*/ pCurrentMarker)
        throws ComException
    {
        invokeStandardVirtualMethod(
            17,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCurrentMarker
            }
        );
    }

    public BStr getFileName()
        throws ComException
    {
        BStr pbstrFileName = new BStr();
        invokeStandardVirtualMethod(
            18,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrFileName == null ? (Parameter)PTR_NULL : new Pointer(pbstrFileName)
            }
        );
        return pbstrFileName;
    }

    public void setFileName(
        BStr /*[in]*/ pbstrFileName)
        throws ComException
    {
        invokeStandardVirtualMethod(
            19,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrFileName == null ? (Parameter)PTR_NULL : new Const(pbstrFileName)
            }
        );
    }

    public BStr getSourceLink()
        throws ComException
    {
        BStr pbstrSourceLink = new BStr();
        invokeStandardVirtualMethod(
            20,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrSourceLink == null ? (Parameter)PTR_NULL : new Pointer(pbstrSourceLink)
            }
        );
        return pbstrSourceLink;
    }

    public Date getCreationDate()
        throws ComException
    {
        Date pCreationDate = new Date();
        invokeStandardVirtualMethod(
            21,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCreationDate == null ? (Parameter)PTR_NULL : new Pointer(pCreationDate)
            }
        );
        return pCreationDate;
    }

    public BStr getErrorCorrection()
        throws ComException
    {
        BStr pbstrErrorCorrection = new BStr();
        invokeStandardVirtualMethod(
            22,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrErrorCorrection == null ? (Parameter)PTR_NULL : new Pointer(pbstrErrorCorrection)
            }
        );
        return pbstrErrorCorrection;
    }

    public Int32 getBandwidth()
        throws ComException
    {
        Int32 pBandwidth = new Int32();
        invokeStandardVirtualMethod(
            23,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBandwidth == null ? (Parameter)PTR_NULL : new Pointer(pBandwidth)
            }
        );
        return pBandwidth;
    }

    public Int32 getSourceProtocol()
        throws ComException
    {
        Int32 pSourceProtocol = new Int32();
        invokeStandardVirtualMethod(
            24,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSourceProtocol == null ? (Parameter)PTR_NULL : new Pointer(pSourceProtocol)
            }
        );
        return pSourceProtocol;
    }

    public Int32 getReceivedPackets()
        throws ComException
    {
        Int32 pReceivedPackets = new Int32();
        invokeStandardVirtualMethod(
            25,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pReceivedPackets == null ? (Parameter)PTR_NULL : new Pointer(pReceivedPackets)
            }
        );
        return pReceivedPackets;
    }

    public Int32 getRecoveredPackets()
        throws ComException
    {
        Int32 pRecoveredPackets = new Int32();
        invokeStandardVirtualMethod(
            26,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRecoveredPackets == null ? (Parameter)PTR_NULL : new Pointer(pRecoveredPackets)
            }
        );
        return pRecoveredPackets;
    }

    public Int32 getLostPackets()
        throws ComException
    {
        Int32 pLostPackets = new Int32();
        invokeStandardVirtualMethod(
            27,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pLostPackets == null ? (Parameter)PTR_NULL : new Pointer(pLostPackets)
            }
        );
        return pLostPackets;
    }

    public Int32 getReceptionQuality()
        throws ComException
    {
        Int32 pReceptionQuality = new Int32();
        invokeStandardVirtualMethod(
            28,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pReceptionQuality == null ? (Parameter)PTR_NULL : new Pointer(pReceptionQuality)
            }
        );
        return pReceptionQuality;
    }

    public Int32 getBufferingCount()
        throws ComException
    {
        Int32 pBufferingCount = new Int32();
        invokeStandardVirtualMethod(
            29,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBufferingCount == null ? (Parameter)PTR_NULL : new Pointer(pBufferingCount)
            }
        );
        return pBufferingCount;
    }

    public VariantBool getIsBroadcast()
        throws ComException
    {
        VariantBool pIsBroadcast = new VariantBool();
        invokeStandardVirtualMethod(
            30,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pIsBroadcast == null ? (Parameter)PTR_NULL : new Pointer(pIsBroadcast)
            }
        );
        return pIsBroadcast;
    }

    public Int32 getBufferingProgress()
        throws ComException
    {
        Int32 pBufferingProgress = new Int32();
        invokeStandardVirtualMethod(
            31,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBufferingProgress == null ? (Parameter)PTR_NULL : new Pointer(pBufferingProgress)
            }
        );
        return pBufferingProgress;
    }

    public BStr getChannelName()
        throws ComException
    {
        BStr pbstrChannelName = new BStr();
        invokeStandardVirtualMethod(
            32,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrChannelName == null ? (Parameter)PTR_NULL : new Pointer(pbstrChannelName)
            }
        );
        return pbstrChannelName;
    }

    public BStr getChannelDescription()
        throws ComException
    {
        BStr pbstrChannelDescription = new BStr();
        invokeStandardVirtualMethod(
            33,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrChannelDescription == null ? (Parameter)PTR_NULL : new Pointer(pbstrChannelDescription)
            }
        );
        return pbstrChannelDescription;
    }

    public BStr getChannelURL()
        throws ComException
    {
        BStr pbstrChannelURL = new BStr();
        invokeStandardVirtualMethod(
            34,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrChannelURL == null ? (Parameter)PTR_NULL : new Pointer(pbstrChannelURL)
            }
        );
        return pbstrChannelURL;
    }

    public BStr getContactAddress()
        throws ComException
    {
        BStr pbstrContactAddress = new BStr();
        invokeStandardVirtualMethod(
            35,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrContactAddress == null ? (Parameter)PTR_NULL : new Pointer(pbstrContactAddress)
            }
        );
        return pbstrContactAddress;
    }

    public BStr getContactPhone()
        throws ComException
    {
        BStr pbstrContactPhone = new BStr();
        invokeStandardVirtualMethod(
            36,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrContactPhone == null ? (Parameter)PTR_NULL : new Pointer(pbstrContactPhone)
            }
        );
        return pbstrContactPhone;
    }

    public BStr getContactEmail()
        throws ComException
    {
        BStr pbstrContactEmail = new BStr();
        invokeStandardVirtualMethod(
            37,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrContactEmail == null ? (Parameter)PTR_NULL : new Pointer(pbstrContactEmail)
            }
        );
        return pbstrContactEmail;
    }

    public DoubleFloat getBufferingTime()
        throws ComException
    {
        DoubleFloat pBufferingTime = new DoubleFloat();
        invokeStandardVirtualMethod(
            38,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBufferingTime == null ? (Parameter)PTR_NULL : new Pointer(pBufferingTime)
            }
        );
        return pBufferingTime;
    }

    public void setBufferingTime(
        DoubleFloat /*[in]*/ pBufferingTime)
        throws ComException
    {
        invokeStandardVirtualMethod(
            39,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBufferingTime
            }
        );
    }

    public VariantBool getAutoStart()
        throws ComException
    {
        VariantBool pAutoStart = new VariantBool();
        invokeStandardVirtualMethod(
            40,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAutoStart == null ? (Parameter)PTR_NULL : new Pointer(pAutoStart)
            }
        );
        return pAutoStart;
    }

    public void setAutoStart(
        VariantBool /*[in]*/ pAutoStart)
        throws ComException
    {
        invokeStandardVirtualMethod(
            41,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAutoStart
            }
        );
    }

    public VariantBool getAutoRewind()
        throws ComException
    {
        VariantBool pAutoRewind = new VariantBool();
        invokeStandardVirtualMethod(
            42,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAutoRewind == null ? (Parameter)PTR_NULL : new Pointer(pAutoRewind)
            }
        );
        return pAutoRewind;
    }

    public void setAutoRewind(
        VariantBool /*[in]*/ pAutoRewind)
        throws ComException
    {
        invokeStandardVirtualMethod(
            43,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAutoRewind
            }
        );
    }

    public DoubleFloat getRate()
        throws ComException
    {
        DoubleFloat pRate = new DoubleFloat();
        invokeStandardVirtualMethod(
            44,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRate == null ? (Parameter)PTR_NULL : new Pointer(pRate)
            }
        );
        return pRate;
    }

    public void setRate(
        DoubleFloat /*[in]*/ pRate)
        throws ComException
    {
        invokeStandardVirtualMethod(
            45,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRate
            }
        );
    }

    public VariantBool getSendKeyboardEvents()
        throws ComException
    {
        VariantBool pSendKeyboardEvents = new VariantBool();
        invokeStandardVirtualMethod(
            46,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendKeyboardEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendKeyboardEvents)
            }
        );
        return pSendKeyboardEvents;
    }

    public void setSendKeyboardEvents(
        VariantBool /*[in]*/ pSendKeyboardEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            47,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendKeyboardEvents
            }
        );
    }

    public VariantBool getSendMouseClickEvents()
        throws ComException
    {
        VariantBool pSendMouseClickEvents = new VariantBool();
        invokeStandardVirtualMethod(
            48,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendMouseClickEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendMouseClickEvents)
            }
        );
        return pSendMouseClickEvents;
    }

    public void setSendMouseClickEvents(
        VariantBool /*[in]*/ pSendMouseClickEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            49,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendMouseClickEvents
            }
        );
    }

    public VariantBool getSendMouseMoveEvents()
        throws ComException
    {
        VariantBool pSendMouseMoveEvents = new VariantBool();
        invokeStandardVirtualMethod(
            50,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendMouseMoveEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendMouseMoveEvents)
            }
        );
        return pSendMouseMoveEvents;
    }

    public void setSendMouseMoveEvents(
        VariantBool /*[in]*/ pSendMouseMoveEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            51,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendMouseMoveEvents
            }
        );
    }

    public Int32 getPlayCount()
        throws ComException
    {
        Int32 pPlayCount = new Int32();
        invokeStandardVirtualMethod(
            52,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pPlayCount == null ? (Parameter)PTR_NULL : new Pointer(pPlayCount)
            }
        );
        return pPlayCount;
    }

    public void setPlayCount(
        Int32 /*[in]*/ pPlayCount)
        throws ComException
    {
        invokeStandardVirtualMethod(
            53,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pPlayCount
            }
        );
    }

    public VariantBool getClickToPlay()
        throws ComException
    {
        VariantBool pClickToPlay = new VariantBool();
        invokeStandardVirtualMethod(
            54,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pClickToPlay == null ? (Parameter)PTR_NULL : new Pointer(pClickToPlay)
            }
        );
        return pClickToPlay;
    }

    public void setClickToPlay(
        VariantBool /*[in]*/ pClickToPlay)
        throws ComException
    {
        invokeStandardVirtualMethod(
            55,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pClickToPlay
            }
        );
    }

    public VariantBool getAllowScan()
        throws ComException
    {
        VariantBool pAllowScan = new VariantBool();
        invokeStandardVirtualMethod(
            56,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAllowScan == null ? (Parameter)PTR_NULL : new Pointer(pAllowScan)
            }
        );
        return pAllowScan;
    }

    public void setAllowScan(
        VariantBool /*[in]*/ pAllowScan)
        throws ComException
    {
        invokeStandardVirtualMethod(
            57,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAllowScan
            }
        );
    }

    public VariantBool getEnableContextMenu()
        throws ComException
    {
        VariantBool pEnableContextMenu = new VariantBool();
        invokeStandardVirtualMethod(
            58,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pEnableContextMenu == null ? (Parameter)PTR_NULL : new Pointer(pEnableContextMenu)
            }
        );
        return pEnableContextMenu;
    }

    public void setEnableContextMenu(
        VariantBool /*[in]*/ pEnableContextMenu)
        throws ComException
    {
        invokeStandardVirtualMethod(
            59,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pEnableContextMenu
            }
        );
    }

    public Int32 getCursorType()
        throws ComException
    {
        Int32 pCursorType = new Int32();
        invokeStandardVirtualMethod(
            60,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCursorType == null ? (Parameter)PTR_NULL : new Pointer(pCursorType)
            }
        );
        return pCursorType;
    }

    public void setCursorType(
        Int32 /*[in]*/ pCursorType)
        throws ComException
    {
        invokeStandardVirtualMethod(
            61,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCursorType
            }
        );
    }

    public Int32 getCodecCount()
        throws ComException
    {
        Int32 pCodecCount = new Int32();
        invokeStandardVirtualMethod(
            62,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCodecCount == null ? (Parameter)PTR_NULL : new Pointer(pCodecCount)
            }
        );
        return pCodecCount;
    }

    public VariantBool getAllowChangeDisplaySize()
        throws ComException
    {
        VariantBool pAllowChangeDisplaySize = new VariantBool();
        invokeStandardVirtualMethod(
            63,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAllowChangeDisplaySize == null ? (Parameter)PTR_NULL : new Pointer(pAllowChangeDisplaySize)
            }
        );
        return pAllowChangeDisplaySize;
    }

    public void setAllowChangeDisplaySize(
        VariantBool /*[in]*/ pAllowChangeDisplaySize)
        throws ComException
    {
        invokeStandardVirtualMethod(
            64,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAllowChangeDisplaySize
            }
        );
    }

    public VariantBool getIsDurationValid()
        throws ComException
    {
        VariantBool pIsDurationValid = new VariantBool();
        invokeStandardVirtualMethod(
            65,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pIsDurationValid == null ? (Parameter)PTR_NULL : new Pointer(pIsDurationValid)
            }
        );
        return pIsDurationValid;
    }

    public Int32 getOpenState()
        throws ComException
    {
        Int32 pOpenState = new Int32();
        invokeStandardVirtualMethod(
            66,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pOpenState == null ? (Parameter)PTR_NULL : new Pointer(pOpenState)
            }
        );
        return pOpenState;
    }

    public VariantBool getSendOpenStateChangeEvents()
        throws ComException
    {
        VariantBool pSendOpenStateChangeEvents = new VariantBool();
        invokeStandardVirtualMethod(
            67,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendOpenStateChangeEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendOpenStateChangeEvents)
            }
        );
        return pSendOpenStateChangeEvents;
    }

    public void setSendOpenStateChangeEvents(
        VariantBool /*[in]*/ pSendOpenStateChangeEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            68,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendOpenStateChangeEvents
            }
        );
    }

    public VariantBool getSendWarningEvents()
        throws ComException
    {
        VariantBool pSendWarningEvents = new VariantBool();
        invokeStandardVirtualMethod(
            69,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendWarningEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendWarningEvents)
            }
        );
        return pSendWarningEvents;
    }

    public void setSendWarningEvents(
        VariantBool /*[in]*/ pSendWarningEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            70,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendWarningEvents
            }
        );
    }

    public VariantBool getSendErrorEvents()
        throws ComException
    {
        VariantBool pSendErrorEvents = new VariantBool();
        invokeStandardVirtualMethod(
            71,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendErrorEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendErrorEvents)
            }
        );
        return pSendErrorEvents;
    }

    public void setSendErrorEvents(
        VariantBool /*[in]*/ pSendErrorEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            72,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendErrorEvents
            }
        );
    }

    public MPPlayStateConstants getPlayState()
        throws ComException
    {
        MPPlayStateConstants pPlayState = new MPPlayStateConstants();
        invokeStandardVirtualMethod(
            73,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pPlayState == null ? (Parameter)PTR_NULL : new Pointer(pPlayState)
            }
        );
        return pPlayState;
    }

    public VariantBool getSendPlayStateChangeEvents()
        throws ComException
    {
        VariantBool pSendPlayStateChangeEvents = new VariantBool();
        invokeStandardVirtualMethod(
            74,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendPlayStateChangeEvents == null ? (Parameter)PTR_NULL : new Pointer(pSendPlayStateChangeEvents)
            }
        );
        return pSendPlayStateChangeEvents;
    }

    public void setSendPlayStateChangeEvents(
        VariantBool /*[in]*/ pSendPlayStateChangeEvents)
        throws ComException
    {
        invokeStandardVirtualMethod(
            75,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pSendPlayStateChangeEvents
            }
        );
    }

    public MPDisplaySizeConstants getDisplaySize()
        throws ComException
    {
        MPDisplaySizeConstants pDisplaySize = new MPDisplaySizeConstants();
        invokeStandardVirtualMethod(
            76,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pDisplaySize == null ? (Parameter)PTR_NULL : new Pointer(pDisplaySize)
            }
        );
        return pDisplaySize;
    }

    public void setDisplaySize(
        MPDisplaySizeConstants /*[in]*/ pDisplaySize)
        throws ComException
    {
        invokeStandardVirtualMethod(
            77,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pDisplaySize
            }
        );
    }

    public VariantBool getInvokeURLs()
        throws ComException
    {
        VariantBool pInvokeURLs = new VariantBool();
        invokeStandardVirtualMethod(
            78,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pInvokeURLs == null ? (Parameter)PTR_NULL : new Pointer(pInvokeURLs)
            }
        );
        return pInvokeURLs;
    }

    public void setInvokeURLs(
        VariantBool /*[in]*/ pInvokeURLs)
        throws ComException
    {
        invokeStandardVirtualMethod(
            79,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pInvokeURLs
            }
        );
    }

    public BStr getBaseURL()
        throws ComException
    {
        BStr pbstrBaseURL = new BStr();
        invokeStandardVirtualMethod(
            80,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrBaseURL == null ? (Parameter)PTR_NULL : new Pointer(pbstrBaseURL)
            }
        );
        return pbstrBaseURL;
    }

    public void setBaseURL(
        BStr /*[in]*/ pbstrBaseURL)
        throws ComException
    {
        invokeStandardVirtualMethod(
            81,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrBaseURL == null ? (Parameter)PTR_NULL : new Const(pbstrBaseURL)
            }
        );
    }

    public BStr getDefaultFrame()
        throws ComException
    {
        BStr pbstrDefaultFrame = new BStr();
        invokeStandardVirtualMethod(
            82,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrDefaultFrame == null ? (Parameter)PTR_NULL : new Pointer(pbstrDefaultFrame)
            }
        );
        return pbstrDefaultFrame;
    }

    public void setDefaultFrame(
        BStr /*[in]*/ pbstrDefaultFrame)
        throws ComException
    {
        invokeStandardVirtualMethod(
            83,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrDefaultFrame == null ? (Parameter)PTR_NULL : new Const(pbstrDefaultFrame)
            }
        );
    }

    public VariantBool getHasError()
        throws ComException
    {
        VariantBool pHasError = new VariantBool();
        invokeStandardVirtualMethod(
            84,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pHasError == null ? (Parameter)PTR_NULL : new Pointer(pHasError)
            }
        );
        return pHasError;
    }

    public BStr getErrorDescription()
        throws ComException
    {
        BStr pbstrErrorDescription = new BStr();
        invokeStandardVirtualMethod(
            85,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrErrorDescription == null ? (Parameter)PTR_NULL : new Pointer(pbstrErrorDescription)
            }
        );
        return pbstrErrorDescription;
    }

    public Int32 getErrorCode()
        throws ComException
    {
        Int32 pErrorCode = new Int32();
        invokeStandardVirtualMethod(
            86,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pErrorCode == null ? (Parameter)PTR_NULL : new Pointer(pErrorCode)
            }
        );
        return pErrorCode;
    }

    public VariantBool getAnimationAtStart()
        throws ComException
    {
        VariantBool pAnimationAtStart = new VariantBool();
        invokeStandardVirtualMethod(
            87,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAnimationAtStart == null ? (Parameter)PTR_NULL : new Pointer(pAnimationAtStart)
            }
        );
        return pAnimationAtStart;
    }

    public void setAnimationAtStart(
        VariantBool /*[in]*/ pAnimationAtStart)
        throws ComException
    {
        invokeStandardVirtualMethod(
            88,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pAnimationAtStart
            }
        );
    }

    public VariantBool getTransparentAtStart()
        throws ComException
    {
        VariantBool pTransparentAtStart = new VariantBool();
        invokeStandardVirtualMethod(
            89,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pTransparentAtStart == null ? (Parameter)PTR_NULL : new Pointer(pTransparentAtStart)
            }
        );
        return pTransparentAtStart;
    }

    public void setTransparentAtStart(
        VariantBool /*[in]*/ pTransparentAtStart)
        throws ComException
    {
        invokeStandardVirtualMethod(
            90,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pTransparentAtStart
            }
        );
    }

    public Int32 getVolume()
        throws ComException
    {
        Int32 pVolume = new Int32();
        invokeStandardVirtualMethod(
            91,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVolume == null ? (Parameter)PTR_NULL : new Pointer(pVolume)
            }
        );
        return pVolume;
    }

    public void setVolume(
        Int32 /*[in]*/ pVolume)
        throws ComException
    {
        invokeStandardVirtualMethod(
            92,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVolume
            }
        );
    }

    public Int32 getBalance()
        throws ComException
    {
        Int32 pBalance = new Int32();
        invokeStandardVirtualMethod(
            93,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBalance == null ? (Parameter)PTR_NULL : new Pointer(pBalance)
            }
        );
        return pBalance;
    }

    public void setBalance(
        Int32 /*[in]*/ pBalance)
        throws ComException
    {
        invokeStandardVirtualMethod(
            94,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pBalance
            }
        );
    }

    public MPReadyStateConstants getReadyState()
        throws ComException
    {
        MPReadyStateConstants pValue = new MPReadyStateConstants();
        invokeStandardVirtualMethod(
            95,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue == null ? (Parameter)PTR_NULL : new Pointer(pValue)
            }
        );
        return pValue;
    }

    public DoubleFloat getSelectionStart()
        throws ComException
    {
        DoubleFloat pValue = new DoubleFloat();
        invokeStandardVirtualMethod(
            96,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue == null ? (Parameter)PTR_NULL : new Pointer(pValue)
            }
        );
        return pValue;
    }

    public void setSelectionStart(
        DoubleFloat /*[in]*/ pValue)
        throws ComException
    {
        invokeStandardVirtualMethod(
            97,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue
            }
        );
    }

    public DoubleFloat getSelectionEnd()
        throws ComException
    {
        DoubleFloat pValue = new DoubleFloat();
        invokeStandardVirtualMethod(
            98,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue == null ? (Parameter)PTR_NULL : new Pointer(pValue)
            }
        );
        return pValue;
    }

    public void setSelectionEnd(
        DoubleFloat /*[in]*/ pValue)
        throws ComException
    {
        invokeStandardVirtualMethod(
            99,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue
            }
        );
    }

    public VariantBool getShowDisplay()
        throws ComException
    {
        VariantBool Show = new VariantBool();
        invokeStandardVirtualMethod(
            100,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show == null ? (Parameter)PTR_NULL : new Pointer(Show)
            }
        );
        return Show;
    }

    public void setShowDisplay(
        VariantBool /*[in]*/ Show)
        throws ComException
    {
        invokeStandardVirtualMethod(
            101,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show
            }
        );
    }

    public VariantBool getShowControls()
        throws ComException
    {
        VariantBool Show = new VariantBool();
        invokeStandardVirtualMethod(
            102,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show == null ? (Parameter)PTR_NULL : new Pointer(Show)
            }
        );
        return Show;
    }

    public void setShowControls(
        VariantBool /*[in]*/ Show)
        throws ComException
    {
        invokeStandardVirtualMethod(
            103,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show
            }
        );
    }

    public VariantBool getShowPositionControls()
        throws ComException
    {
        VariantBool Show = new VariantBool();
        invokeStandardVirtualMethod(
            104,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show == null ? (Parameter)PTR_NULL : new Pointer(Show)
            }
        );
        return Show;
    }

    public void setShowPositionControls(
        VariantBool /*[in]*/ Show)
        throws ComException
    {
        invokeStandardVirtualMethod(
            105,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show
            }
        );
    }

    public VariantBool getShowTracker()
        throws ComException
    {
        VariantBool Show = new VariantBool();
        invokeStandardVirtualMethod(
            106,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show == null ? (Parameter)PTR_NULL : new Pointer(Show)
            }
        );
        return Show;
    }

    public void setShowTracker(
        VariantBool /*[in]*/ Show)
        throws ComException
    {
        invokeStandardVirtualMethod(
            107,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Show
            }
        );
    }

    public VariantBool getEnablePositionControls()
        throws ComException
    {
        VariantBool Enable = new VariantBool();
        invokeStandardVirtualMethod(
            108,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Enable == null ? (Parameter)PTR_NULL : new Pointer(Enable)
            }
        );
        return Enable;
    }

    public void setEnablePositionControls(
        VariantBool /*[in]*/ Enable)
        throws ComException
    {
        invokeStandardVirtualMethod(
            109,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Enable
            }
        );
    }

    public VariantBool getEnableTracker()
        throws ComException
    {
        VariantBool Enable = new VariantBool();
        invokeStandardVirtualMethod(
            110,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Enable == null ? (Parameter)PTR_NULL : new Pointer(Enable)
            }
        );
        return Enable;
    }

    public void setEnableTracker(
        VariantBool /*[in]*/ Enable)
        throws ComException
    {
        invokeStandardVirtualMethod(
            111,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Enable
            }
        );
    }

    public VariantBool getEnabled()
        throws ComException
    {
        VariantBool pEnabled = new VariantBool();
        invokeStandardVirtualMethod(
            112,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pEnabled == null ? (Parameter)PTR_NULL : new Pointer(pEnabled)
            }
        );
        return pEnabled;
    }

    public void setEnabled(
        VariantBool /*[in]*/ pEnabled)
        throws ComException
    {
        invokeStandardVirtualMethod(
            113,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pEnabled
            }
        );
    }

    public VB_OLE_COLOR getDisplayForeColor()
        throws ComException
    {
        VB_OLE_COLOR ForeColor = new VB_OLE_COLOR();
        invokeStandardVirtualMethod(
            114,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ForeColor == null ? (Parameter)PTR_NULL : new Pointer(ForeColor)
            }
        );
        return ForeColor;
    }

    public void setDisplayForeColor(
        VB_OLE_COLOR /*[in]*/ ForeColor)
        throws ComException
    {
        invokeStandardVirtualMethod(
            115,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ForeColor
            }
        );
    }

    public VB_OLE_COLOR getDisplayBackColor()
        throws ComException
    {
        VB_OLE_COLOR BackColor = new VB_OLE_COLOR();
        invokeStandardVirtualMethod(
            116,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                BackColor == null ? (Parameter)PTR_NULL : new Pointer(BackColor)
            }
        );
        return BackColor;
    }

    public void setDisplayBackColor(
        VB_OLE_COLOR /*[in]*/ BackColor)
        throws ComException
    {
        invokeStandardVirtualMethod(
            117,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                BackColor
            }
        );
    }

    public MPDisplayModeConstants getDisplayMode()
        throws ComException
    {
        MPDisplayModeConstants pValue = new MPDisplayModeConstants();
        invokeStandardVirtualMethod(
            118,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue == null ? (Parameter)PTR_NULL : new Pointer(pValue)
            }
        );
        return pValue;
    }

    public void setDisplayMode(
        MPDisplayModeConstants /*[in]*/ pValue)
        throws ComException
    {
        invokeStandardVirtualMethod(
            119,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pValue
            }
        );
    }

    public VariantBool getVideoBorder3D()
        throws ComException
    {
        VariantBool pVideoBorderWidth = new VariantBool();
        invokeStandardVirtualMethod(
            120,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVideoBorderWidth == null ? (Parameter)PTR_NULL : new Pointer(pVideoBorderWidth)
            }
        );
        return pVideoBorderWidth;
    }

    public void setVideoBorder3D(
        VariantBool /*[in]*/ pVideoBorderWidth)
        throws ComException
    {
        invokeStandardVirtualMethod(
            121,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVideoBorderWidth
            }
        );
    }

    public Int32 getVideoBorderWidth()
        throws ComException
    {
        Int32 pVideoBorderWidth = new Int32();
        invokeStandardVirtualMethod(
            122,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVideoBorderWidth == null ? (Parameter)PTR_NULL : new Pointer(pVideoBorderWidth)
            }
        );
        return pVideoBorderWidth;
    }

    public void setVideoBorderWidth(
        Int32 /*[in]*/ pVideoBorderWidth)
        throws ComException
    {
        invokeStandardVirtualMethod(
            123,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVideoBorderWidth
            }
        );
    }

    public VB_OLE_COLOR getVideoBorderColor()
        throws ComException
    {
        VB_OLE_COLOR pVideoBorderWidth = new VB_OLE_COLOR();
        invokeStandardVirtualMethod(
            124,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVideoBorderWidth == null ? (Parameter)PTR_NULL : new Pointer(pVideoBorderWidth)
            }
        );
        return pVideoBorderWidth;
    }

    public void setVideoBorderColor(
        VB_OLE_COLOR /*[in]*/ pVideoBorderWidth)
        throws ComException
    {
        invokeStandardVirtualMethod(
            125,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pVideoBorderWidth
            }
        );
    }

    public VariantBool getShowGotoBar()
        throws ComException
    {
        VariantBool pbool = new VariantBool();
        invokeStandardVirtualMethod(
            126,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool == null ? (Parameter)PTR_NULL : new Pointer(pbool)
            }
        );
        return pbool;
    }

    public void setShowGotoBar(
        VariantBool /*[in]*/ pbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            127,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool
            }
        );
    }

    public VariantBool getShowStatusBar()
        throws ComException
    {
        VariantBool pbool = new VariantBool();
        invokeStandardVirtualMethod(
            128,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool == null ? (Parameter)PTR_NULL : new Pointer(pbool)
            }
        );
        return pbool;
    }

    public void setShowStatusBar(
        VariantBool /*[in]*/ pbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            129,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool
            }
        );
    }

    public VariantBool getShowCaptioning()
        throws ComException
    {
        VariantBool pbool = new VariantBool();
        invokeStandardVirtualMethod(
            130,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool == null ? (Parameter)PTR_NULL : new Pointer(pbool)
            }
        );
        return pbool;
    }

    public void setShowCaptioning(
        VariantBool /*[in]*/ pbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            131,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool
            }
        );
    }

    public VariantBool getShowAudioControls()
        throws ComException
    {
        VariantBool pbool = new VariantBool();
        invokeStandardVirtualMethod(
            132,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool == null ? (Parameter)PTR_NULL : new Pointer(pbool)
            }
        );
        return pbool;
    }

    public void setShowAudioControls(
        VariantBool /*[in]*/ pbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            133,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool
            }
        );
    }

    public BStr getCaptioningID()
        throws ComException
    {
        BStr pstrText = new BStr();
        invokeStandardVirtualMethod(
            134,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pstrText == null ? (Parameter)PTR_NULL : new Pointer(pstrText)
            }
        );
        return pstrText;
    }

    public void setCaptioningID(
        BStr /*[in]*/ pstrText)
        throws ComException
    {
        invokeStandardVirtualMethod(
            135,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pstrText == null ? (Parameter)PTR_NULL : new Const(pstrText)
            }
        );
    }

    public VariantBool getMute()
        throws ComException
    {
        VariantBool vbool = new VariantBool();
        invokeStandardVirtualMethod(
            136,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                vbool == null ? (Parameter)PTR_NULL : new Pointer(vbool)
            }
        );
        return vbool;
    }

    public void setMute(
        VariantBool /*[in]*/ vbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            137,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                vbool
            }
        );
    }

    public VariantBool getCanPreview()
        throws ComException
    {
        VariantBool pCanPreview = new VariantBool();
        invokeStandardVirtualMethod(
            138,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pCanPreview == null ? (Parameter)PTR_NULL : new Pointer(pCanPreview)
            }
        );
        return pCanPreview;
    }

    public VariantBool getPreviewMode()
        throws ComException
    {
        VariantBool pPreviewMode = new VariantBool();
        invokeStandardVirtualMethod(
            139,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pPreviewMode == null ? (Parameter)PTR_NULL : new Pointer(pPreviewMode)
            }
        );
        return pPreviewMode;
    }

    public void setPreviewMode(
        VariantBool /*[in]*/ pPreviewMode)
        throws ComException
    {
        invokeStandardVirtualMethod(
            140,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pPreviewMode
            }
        );
    }

    public VariantBool getHasMultipleItems()
        throws ComException
    {
        VariantBool pHasMuliItems = new VariantBool();
        invokeStandardVirtualMethod(
            141,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pHasMuliItems == null ? (Parameter)PTR_NULL : new Pointer(pHasMuliItems)
            }
        );
        return pHasMuliItems;
    }

    public Int32 getLanguage()
        throws ComException
    {
        Int32 pLanguage = new Int32();
        invokeStandardVirtualMethod(
            142,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pLanguage == null ? (Parameter)PTR_NULL : new Pointer(pLanguage)
            }
        );
        return pLanguage;
    }

    public void setLanguage(
        Int32 /*[in]*/ pLanguage)
        throws ComException
    {
        invokeStandardVirtualMethod(
            143,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pLanguage
            }
        );
    }

    public Int32 getAudioStream()
        throws ComException
    {
        Int32 pStream = new Int32();
        invokeStandardVirtualMethod(
            144,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pStream == null ? (Parameter)PTR_NULL : new Pointer(pStream)
            }
        );
        return pStream;
    }

    public void setAudioStream(
        Int32 /*[in]*/ pStream)
        throws ComException
    {
        invokeStandardVirtualMethod(
            145,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pStream
            }
        );
    }

    public BStr getSAMIStyle()
        throws ComException
    {
        BStr pbstrStyle = new BStr();
        invokeStandardVirtualMethod(
            146,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrStyle == null ? (Parameter)PTR_NULL : new Pointer(pbstrStyle)
            }
        );
        return pbstrStyle;
    }

    public void setSAMIStyle(
        BStr /*[in]*/ pbstrStyle)
        throws ComException
    {
        invokeStandardVirtualMethod(
            147,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrStyle == null ? (Parameter)PTR_NULL : new Const(pbstrStyle)
            }
        );
    }

    public BStr getSAMILang()
        throws ComException
    {
        BStr pbstrLang = new BStr();
        invokeStandardVirtualMethod(
            148,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrLang == null ? (Parameter)PTR_NULL : new Pointer(pbstrLang)
            }
        );
        return pbstrLang;
    }

    public void setSAMILang(
        BStr /*[in]*/ pbstrLang)
        throws ComException
    {
        invokeStandardVirtualMethod(
            149,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrLang == null ? (Parameter)PTR_NULL : new Const(pbstrLang)
            }
        );
    }

    public BStr getSAMIFileName()
        throws ComException
    {
        BStr pbstrFileName = new BStr();
        invokeStandardVirtualMethod(
            150,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrFileName == null ? (Parameter)PTR_NULL : new Pointer(pbstrFileName)
            }
        );
        return pbstrFileName;
    }

    public void setSAMIFileName(
        BStr /*[in]*/ pbstrFileName)
        throws ComException
    {
        invokeStandardVirtualMethod(
            151,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrFileName == null ? (Parameter)PTR_NULL : new Const(pbstrFileName)
            }
        );
    }

    public Int32 getStreamCount()
        throws ComException
    {
        Int32 pStreamCount = new Int32();
        invokeStandardVirtualMethod(
            152,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pStreamCount == null ? (Parameter)PTR_NULL : new Pointer(pStreamCount)
            }
        );
        return pStreamCount;
    }

    public BStr getClientId()
        throws ComException
    {
        BStr pbstrClientId = new BStr();
        invokeStandardVirtualMethod(
            153,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrClientId == null ? (Parameter)PTR_NULL : new Pointer(pbstrClientId)
            }
        );
        return pbstrClientId;
    }

    public Int32 getConnectionSpeed()
        throws ComException
    {
        Int32 plConnectionSpeed = new Int32();
        invokeStandardVirtualMethod(
            154,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                plConnectionSpeed == null ? (Parameter)PTR_NULL : new Pointer(plConnectionSpeed)
            }
        );
        return plConnectionSpeed;
    }

    public VariantBool getAutoSize()
        throws ComException
    {
        VariantBool pbool = new VariantBool();
        invokeStandardVirtualMethod(
            155,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool == null ? (Parameter)PTR_NULL : new Pointer(pbool)
            }
        );
        return pbool;
    }

    public void setAutoSize(
        VariantBool /*[in]*/ pbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            156,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool
            }
        );
    }

    public VariantBool getEnableFullScreenControls()
        throws ComException
    {
        VariantBool pbVal = new VariantBool();
        invokeStandardVirtualMethod(
            157,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbVal == null ? (Parameter)PTR_NULL : new Pointer(pbVal)
            }
        );
        return pbVal;
    }

    public void setEnableFullScreenControls(
        VariantBool /*[in]*/ pbVal)
        throws ComException
    {
        invokeStandardVirtualMethod(
            158,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbVal
            }
        );
    }

    public IDispatch getActiveMovie()
        throws ComException
    {
        IDispatchImpl ppdispatch = new IDispatchImpl();
        invokeStandardVirtualMethod(
            159,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ppdispatch == null ? (Parameter)PTR_NULL : new Pointer((Parameter)ppdispatch)
            }
        );
        return ppdispatch;
    }

    public IDispatch getNSPlay()
        throws ComException
    {
        IDispatchImpl ppdispatch = new IDispatchImpl();
        invokeStandardVirtualMethod(
            160,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ppdispatch == null ? (Parameter)PTR_NULL : new Pointer((Parameter)ppdispatch)
            }
        );
        return ppdispatch;
    }

    public VariantBool getWindowlessVideo()
        throws ComException
    {
        VariantBool pbool = new VariantBool();
        invokeStandardVirtualMethod(
            161,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool == null ? (Parameter)PTR_NULL : new Pointer(pbool)
            }
        );
        return pbool;
    }

    public void setWindowlessVideo(
        VariantBool /*[in]*/ pbool)
        throws ComException
    {
        invokeStandardVirtualMethod(
            162,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbool
            }
        );
    }

    public void play()
        throws ComException
    {
        invokeStandardVirtualMethod(
            163,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void stop()
        throws ComException
    {
        invokeStandardVirtualMethod(
            164,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void pause()
        throws ComException
    {
        invokeStandardVirtualMethod(
            165,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public DoubleFloat getMarkerTime(
        Int32 /*[in]*/ MarkerNum)
        throws ComException
    {
        DoubleFloat pMarkerTime = new DoubleFloat();
        invokeStandardVirtualMethod(
            166,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                MarkerNum,
                pMarkerTime == null ? (Parameter)PTR_NULL : new Pointer(pMarkerTime)
            }
        );
        return pMarkerTime;
    }

    public BStr getMarkerName(
        Int32 /*[in]*/ MarkerNum)
        throws ComException
    {
        BStr pbstrMarkerName = new BStr();
        invokeStandardVirtualMethod(
            167,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                MarkerNum,
                pbstrMarkerName == null ? (Parameter)PTR_NULL : new Pointer(pbstrMarkerName)
            }
        );
        return pbstrMarkerName;
    }

    public void aboutBox()
        throws ComException
    {
        invokeStandardVirtualMethod(
            168,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public VariantBool getCodecInstalled(
        Int32 /*[in]*/ CodecNum)
        throws ComException
    {
        VariantBool pCodecInstalled = new VariantBool();
        invokeStandardVirtualMethod(
            169,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                CodecNum,
                pCodecInstalled == null ? (Parameter)PTR_NULL : new Pointer(pCodecInstalled)
            }
        );
        return pCodecInstalled;
    }

    public BStr getCodecDescription(
        Int32 /*[in]*/ CodecNum)
        throws ComException
    {
        BStr pbstrCodecDescription = new BStr();
        invokeStandardVirtualMethod(
            170,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                CodecNum,
                pbstrCodecDescription == null ? (Parameter)PTR_NULL : new Pointer(pbstrCodecDescription)
            }
        );
        return pbstrCodecDescription;
    }

    public BStr getCodecURL(
        Int32 /*[in]*/ CodecNum)
        throws ComException
    {
        BStr pbstrCodecURL = new BStr();
        invokeStandardVirtualMethod(
            171,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                CodecNum,
                pbstrCodecURL == null ? (Parameter)PTR_NULL : new Pointer(pbstrCodecURL)
            }
        );
        return pbstrCodecURL;
    }

    public BStr getMoreInfoURL(
        MPMoreInfoType /*[in]*/ MoreInfoType)
        throws ComException
    {
        BStr pbstrMoreInfoURL = new BStr();
        invokeStandardVirtualMethod(
            172,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                MoreInfoType,
                pbstrMoreInfoURL == null ? (Parameter)PTR_NULL : new Pointer(pbstrMoreInfoURL)
            }
        );
        return pbstrMoreInfoURL;
    }

    public BStr getMediaInfoString(
        MPMediaInfoType /*[in]*/ MediaInfoType)
        throws ComException
    {
        BStr pbstrMediaInfo = new BStr();
        invokeStandardVirtualMethod(
            173,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                MediaInfoType,
                pbstrMediaInfo == null ? (Parameter)PTR_NULL : new Pointer(pbstrMediaInfo)
            }
        );
        return pbstrMediaInfo;
    }

    public void cancel()
        throws ComException
    {
        invokeStandardVirtualMethod(
            174,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void open(
        BStr /*[in]*/ bstrFileName)
        throws ComException
    {
        invokeStandardVirtualMethod(
            175,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bstrFileName == null ? (Parameter)PTR_NULL : new Const(bstrFileName)
            }
        );
    }

    public VariantBool isSoundCardEnabled()
        throws ComException
    {
        VariantBool pbSoundCard = new VariantBool();
        invokeStandardVirtualMethod(
            176,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbSoundCard == null ? (Parameter)PTR_NULL : new Pointer(pbSoundCard)
            }
        );
        return pbSoundCard;
    }

    public void next()
        throws ComException
    {
        invokeStandardVirtualMethod(
            177,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void previous()
        throws ComException
    {
        invokeStandardVirtualMethod(
            178,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void streamSelect(
        Int32 /*[in]*/ StreamNum)
        throws ComException
    {
        invokeStandardVirtualMethod(
            179,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                StreamNum
            }
        );
    }

    public void fastForward()
        throws ComException
    {
        invokeStandardVirtualMethod(
            180,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void fastReverse()
        throws ComException
    {
        invokeStandardVirtualMethod(
            181,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public BStr getStreamName(
        Int32 /*[in]*/ StreamNum)
        throws ComException
    {
        BStr pbstrStreamName = new BStr();
        invokeStandardVirtualMethod(
            182,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                StreamNum,
                pbstrStreamName == null ? (Parameter)PTR_NULL : new Pointer(pbstrStreamName)
            }
        );
        return pbstrStreamName;
    }

    public Int32 getStreamGroup(
        Int32 /*[in]*/ StreamNum)
        throws ComException
    {
        Int32 pStreamGroup = new Int32();
        invokeStandardVirtualMethod(
            183,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                StreamNum,
                pStreamGroup == null ? (Parameter)PTR_NULL : new Pointer(pStreamGroup)
            }
        );
        return pStreamGroup;
    }

    public VariantBool getStreamSelected(
        Int32 /*[in]*/ StreamNum)
        throws ComException
    {
        VariantBool pStreamSelected = new VariantBool();
        invokeStandardVirtualMethod(
            184,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                StreamNum,
                pStreamSelected == null ? (Parameter)PTR_NULL : new Pointer(pStreamSelected)
            }
        );
        return pStreamSelected;
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IMediaPlayerImpl(this);
    }
}
