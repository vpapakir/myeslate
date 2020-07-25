package wmp.mediaplayer.server;

import wmp.mediaplayer.MPDisplayModeConstants;
import wmp.mediaplayer.MPDisplaySizeConstants;
import wmp.mediaplayer.MPMediaInfoType;
import wmp.mediaplayer.MPMoreInfoType;
import wmp.mediaplayer.MPPlayStateConstants;
import wmp.mediaplayer.MPReadyStateConstants;
import wmp.mediaplayer.VB_OLE_COLOR;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.Date;

/**
 * Represents VTBL for COM interface IMediaPlayer.
 */
public class IMediaPlayerVTBL extends IDispatchVTBL
{
    public IMediaPlayerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getCurrentPosition",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new DoubleFloat())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentPosition",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "getDuration",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new DoubleFloat())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getImageSourceWidth",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getImageSourceHeight",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getMarkerCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCanScan",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCanSeek",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCanSeekToMarkers",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentMarker",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentMarker",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getFileName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setFileName",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getSourceLink",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCreationDate",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Date())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getErrorCorrection",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getBandwidth",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSourceProtocol",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getReceivedPackets",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getRecoveredPackets",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getLostPackets",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getReceptionQuality",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getBufferingCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsBroadcast",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getBufferingProgress",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getChannelName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getChannelDescription",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getChannelURL",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getContactAddress",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getContactPhone",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getContactEmail",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getBufferingTime",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new DoubleFloat())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setBufferingTime",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "getAutoStart",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAutoStart",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getAutoRewind",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAutoRewind",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getRate",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new DoubleFloat())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setRate",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "getSendKeyboardEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendKeyboardEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getSendMouseClickEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendMouseClickEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getSendMouseMoveEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendMouseMoveEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getPlayCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setPlayCount",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getClickToPlay",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setClickToPlay",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getAllowScan",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAllowScan",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getEnableContextMenu",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setEnableContextMenu",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getCursorType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCursorType",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getCodecCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAllowChangeDisplaySize",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAllowChangeDisplaySize",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getIsDurationValid",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getOpenState",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSendOpenStateChangeEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendOpenStateChangeEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getSendWarningEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendWarningEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getSendErrorEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendErrorEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getPlayState",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MPPlayStateConstants())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSendPlayStateChangeEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSendPlayStateChangeEvents",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getDisplaySize",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MPDisplaySizeConstants())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setDisplaySize",
                    new HResult(),
                    new Parameter[] {
                        new MPDisplaySizeConstants()
                    }
                ),
                new VirtualMethodCallback(
                    "getInvokeURLs",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setInvokeURLs",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getBaseURL",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setBaseURL",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getDefaultFrame",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setDefaultFrame",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getHasError",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getErrorDescription",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getErrorCode",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAnimationAtStart",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAnimationAtStart",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getTransparentAtStart",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setTransparentAtStart",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getVolume",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setVolume",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getBalance",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setBalance",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getReadyState",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MPReadyStateConstants())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSelectionStart",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new DoubleFloat())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSelectionStart",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "getSelectionEnd",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new DoubleFloat())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSelectionEnd",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowDisplay",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowDisplay",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowControls",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowControls",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowPositionControls",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowPositionControls",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowTracker",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowTracker",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getEnablePositionControls",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setEnablePositionControls",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getEnableTracker",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setEnableTracker",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getEnabled",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setEnabled",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getDisplayForeColor",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VB_OLE_COLOR())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setDisplayForeColor",
                    new HResult(),
                    new Parameter[] {
                        new VB_OLE_COLOR()
                    }
                ),
                new VirtualMethodCallback(
                    "getDisplayBackColor",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VB_OLE_COLOR())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setDisplayBackColor",
                    new HResult(),
                    new Parameter[] {
                        new VB_OLE_COLOR()
                    }
                ),
                new VirtualMethodCallback(
                    "getDisplayMode",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MPDisplayModeConstants())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setDisplayMode",
                    new HResult(),
                    new Parameter[] {
                        new MPDisplayModeConstants()
                    }
                ),
                new VirtualMethodCallback(
                    "getVideoBorder3D",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setVideoBorder3D",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getVideoBorderWidth",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setVideoBorderWidth",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getVideoBorderColor",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VB_OLE_COLOR())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setVideoBorderColor",
                    new HResult(),
                    new Parameter[] {
                        new VB_OLE_COLOR()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowGotoBar",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowGotoBar",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowStatusBar",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowStatusBar",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowCaptioning",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowCaptioning",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getShowAudioControls",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setShowAudioControls",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getCaptioningID",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCaptioningID",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getMute",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setMute",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getCanPreview",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getPreviewMode",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setPreviewMode",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getHasMultipleItems",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getLanguage",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setLanguage",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getAudioStream",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAudioStream",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getSAMIStyle",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSAMIStyle",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getSAMILang",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSAMILang",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getSAMIFileName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSAMIFileName",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getStreamCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getClientId",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getConnectionSpeed",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAutoSize",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setAutoSize",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getEnableFullScreenControls",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setEnableFullScreenControls",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getActiveMovie",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IDispatchImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getNSPlay",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IDispatchImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getWindowlessVideo",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setWindowlessVideo",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "play",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "stop",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "pause",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "getMarkerTime",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new DoubleFloat())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMarkerName",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "aboutBox",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "getCodecInstalled",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getCodecDescription",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getCodecURL",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMoreInfoURL",
                    new HResult(),
                    new Parameter[] {
                        new MPMoreInfoType(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMediaInfoString",
                    new HResult(),
                    new Parameter[] {
                        new MPMediaInfoType(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "cancel",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "open",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "isSoundCardEnabled",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "next",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "previous",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "streamSelect",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "fastForward",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "fastReverse",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "getStreamName",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getStreamGroup",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new Int32())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getStreamSelected",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Pointer(new VariantBool())
                    },
                    1
                )
            }
        );
    }
}