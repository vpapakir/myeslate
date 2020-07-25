package wmp.mediaplayer.impl;

import wmp.mediaplayer.DVDMenuIDConstants;
import wmp.mediaplayer.IMediaPlayerDvd;

import com.jniwrapper.Const;
import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Function;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IMediaPlayerDvd.
 */
public class IMediaPlayerDvdImpl extends IDispatchImpl
    implements IMediaPlayerDvd
{
    public static final String INTERFACE_IDENTIFIER = IMediaPlayerDvd.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IMediaPlayerDvdImpl()
    {
    }

    protected IMediaPlayerDvdImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IMediaPlayerDvdImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IMediaPlayerDvdImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IMediaPlayerDvdImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void buttonSelectAndActivate(
        UInt32 /*[in]*/ uiButton)
        throws ComException
    {
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                uiButton
            }
        );
    }

    public void upperButtonSelect()
        throws ComException
    {
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void lowerButtonSelect()
        throws ComException
    {
        invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void leftButtonSelect()
        throws ComException
    {
        invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void rightButtonSelect()
        throws ComException
    {
        invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void buttonActivate()
        throws ComException
    {
        invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void forwardScan(
        DoubleFloat /*[in]*/ dwSpeed)
        throws ComException
    {
        invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                dwSpeed
            }
        );
    }

    public void backwardScan(
        DoubleFloat /*[in]*/ dwSpeed)
        throws ComException
    {
        invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                dwSpeed
            }
        );
    }

    public void prevPGSearch()
        throws ComException
    {
        invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void topPGSearch()
        throws ComException
    {
        invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void nextPGSearch()
        throws ComException
    {
        invokeStandardVirtualMethod(
            17,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void titlePlay(
        UInt32 /*[in]*/ uiTitle)
        throws ComException
    {
        invokeStandardVirtualMethod(
            18,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                uiTitle
            }
        );
    }

    public void chapterPlay(
        UInt32 /*[in]*/ uiTitle,
        UInt32 /*[in]*/ uiChapter)
        throws ComException
    {
        invokeStandardVirtualMethod(
            19,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                uiTitle,
                uiChapter
            }
        );
    }

    public void chapterSearch(
        UInt32 /*[in]*/ Chapter)
        throws ComException
    {
        invokeStandardVirtualMethod(
            20,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Chapter
            }
        );
    }

    public void menuCall(
        DVDMenuIDConstants /*[in]*/ MenuID)
        throws ComException
    {
        invokeStandardVirtualMethod(
            21,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                MenuID
            }
        );
    }

    public void resumeFromMenu()
        throws ComException
    {
        invokeStandardVirtualMethod(
            22,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void timePlay(
        UInt32 /*[in]*/ uiTitle,
        BStr /*[in]*/ bstrTime)
        throws ComException
    {
        invokeStandardVirtualMethod(
            23,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                uiTitle,
                bstrTime == null ? (Parameter)PTR_NULL : new Const(bstrTime)
            }
        );
    }

    public void timeSearch(
        BStr /*[in]*/ bstrTime)
        throws ComException
    {
        invokeStandardVirtualMethod(
            24,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bstrTime == null ? (Parameter)PTR_NULL : new Const(bstrTime)
            }
        );
    }

    public void chapterPlayAutoStop(
        UInt32 /*[in]*/ ulTitle,
        UInt32 /*[in]*/ ulChapter,
        UInt32 /*[in]*/ ulChaptersToPlay)
        throws ComException
    {
        invokeStandardVirtualMethod(
            25,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulTitle,
                ulChapter,
                ulChaptersToPlay
            }
        );
    }

    public void stillOff()
        throws ComException
    {
        invokeStandardVirtualMethod(
            26,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public void goUp()
        throws ComException
    {
        invokeStandardVirtualMethod(
            27,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public BStr getTotalTitleTime()
        throws ComException
    {
        BStr bstrTime = new BStr();
        invokeStandardVirtualMethod(
            28,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bstrTime == null ? (Parameter)PTR_NULL : new Pointer(bstrTime)
            }
        );
        return bstrTime;
    }

    public UInt32 getNumberOfChapters(
        UInt32 /*[in]*/ ulTitle)
        throws ComException
    {
        UInt32 ulNumChapters = new UInt32();
        invokeStandardVirtualMethod(
            29,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulTitle,
                ulNumChapters == null ? (Parameter)PTR_NULL : new Pointer(ulNumChapters)
            }
        );
        return ulNumChapters;
    }

    public BStr getAudioLanguage(
        UInt32 /*[in]*/ ulStream)
        throws ComException
    {
        BStr bstrAudioLang = new BStr();
        invokeStandardVirtualMethod(
            30,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulStream,
                bstrAudioLang == null ? (Parameter)PTR_NULL : new Pointer(bstrAudioLang)
            }
        );
        return bstrAudioLang;
    }

    public BStr getSubpictureLanguage(
        UInt32 /*[in]*/ ulStream)
        throws ComException
    {
        BStr bstrSubpictureLang = new BStr();
        invokeStandardVirtualMethod(
            31,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulStream,
                bstrSubpictureLang == null ? (Parameter)PTR_NULL : new Pointer(bstrSubpictureLang)
            }
        );
        return bstrSubpictureLang;
    }

    public Variant getAllGPRMs()
        throws ComException
    {
        Variant vtGPRM = new Variant();
        invokeStandardVirtualMethod(
            32,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                vtGPRM == null ? (Parameter)PTR_NULL : new Pointer(vtGPRM)
            }
        );
        return vtGPRM;
    }

    public Variant getAllSPRMs()
        throws ComException
    {
        Variant vtSPRM = new Variant();
        invokeStandardVirtualMethod(
            33,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                vtSPRM == null ? (Parameter)PTR_NULL : new Pointer(vtSPRM)
            }
        );
        return vtSPRM;
    }

    public VariantBool UOPValid(
        UInt32 /*[in]*/ ulUOP)
        throws ComException
    {
        VariantBool bValid = new VariantBool();
        invokeStandardVirtualMethod(
            34,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulUOP,
                bValid == null ? (Parameter)PTR_NULL : new Pointer(bValid)
            }
        );
        return bValid;
    }

    public UInt32 getButtonsAvailable()
        throws ComException
    {
        UInt32 ulButtonsAvailable = new UInt32();
        invokeStandardVirtualMethod(
            35,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulButtonsAvailable == null ? (Parameter)PTR_NULL : new Pointer(ulButtonsAvailable)
            }
        );
        return ulButtonsAvailable;
    }

    public UInt32 getCurrentButton()
        throws ComException
    {
        UInt32 ulCurrentButton = new UInt32();
        invokeStandardVirtualMethod(
            36,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulCurrentButton == null ? (Parameter)PTR_NULL : new Pointer(ulCurrentButton)
            }
        );
        return ulCurrentButton;
    }

    public UInt32 getAudioStreamsAvailable()
        throws ComException
    {
        UInt32 ulAudioStreamsAvailable = new UInt32();
        invokeStandardVirtualMethod(
            37,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulAudioStreamsAvailable == null ? (Parameter)PTR_NULL : new Pointer(ulAudioStreamsAvailable)
            }
        );
        return ulAudioStreamsAvailable;
    }

    public UInt32 getCurrentAudioStream()
        throws ComException
    {
        UInt32 ulAudioStream = new UInt32();
        invokeStandardVirtualMethod(
            38,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulAudioStream == null ? (Parameter)PTR_NULL : new Pointer(ulAudioStream)
            }
        );
        return ulAudioStream;
    }

    public void setCurrentAudioStream(
        UInt32 /*[in]*/ ulAudioStream)
        throws ComException
    {
        invokeStandardVirtualMethod(
            39,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulAudioStream
            }
        );
    }

    public UInt32 getCurrentSubpictureStream()
        throws ComException
    {
        UInt32 ulSubpictureStream = new UInt32();
        invokeStandardVirtualMethod(
            40,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulSubpictureStream == null ? (Parameter)PTR_NULL : new Pointer(ulSubpictureStream)
            }
        );
        return ulSubpictureStream;
    }

    public void setCurrentSubpictureStream(
        UInt32 /*[in]*/ ulSubpictureStream)
        throws ComException
    {
        invokeStandardVirtualMethod(
            41,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulSubpictureStream
            }
        );
    }

    public UInt32 getSubpictureStreamsAvailable()
        throws ComException
    {
        UInt32 ulNumSubpictureStreams = new UInt32();
        invokeStandardVirtualMethod(
            42,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulNumSubpictureStreams == null ? (Parameter)PTR_NULL : new Pointer(ulNumSubpictureStreams)
            }
        );
        return ulNumSubpictureStreams;
    }

    public VariantBool getSubpictureOn()
        throws ComException
    {
        VariantBool bSubpictureON = new VariantBool();
        invokeStandardVirtualMethod(
            43,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bSubpictureON == null ? (Parameter)PTR_NULL : new Pointer(bSubpictureON)
            }
        );
        return bSubpictureON;
    }

    public void setSubpictureOn(
        VariantBool /*[in]*/ bSubpictureON)
        throws ComException
    {
        invokeStandardVirtualMethod(
            44,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bSubpictureON
            }
        );
    }

    public UInt32 getAnglesAvailable()
        throws ComException
    {
        UInt32 ulAnglesAvailable = new UInt32();
        invokeStandardVirtualMethod(
            45,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulAnglesAvailable == null ? (Parameter)PTR_NULL : new Pointer(ulAnglesAvailable)
            }
        );
        return ulAnglesAvailable;
    }

    public UInt32 getCurrentAngle()
        throws ComException
    {
        UInt32 ulAngle = new UInt32();
        invokeStandardVirtualMethod(
            46,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulAngle == null ? (Parameter)PTR_NULL : new Pointer(ulAngle)
            }
        );
        return ulAngle;
    }

    public void setCurrentAngle(
        UInt32 /*[in]*/ ulAngle)
        throws ComException
    {
        invokeStandardVirtualMethod(
            47,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulAngle
            }
        );
    }

    public UInt32 getCurrentTitle()
        throws ComException
    {
        UInt32 ulTitle = new UInt32();
        invokeStandardVirtualMethod(
            48,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulTitle == null ? (Parameter)PTR_NULL : new Pointer(ulTitle)
            }
        );
        return ulTitle;
    }

    public UInt32 getCurrentChapter()
        throws ComException
    {
        UInt32 ulChapter = new UInt32();
        invokeStandardVirtualMethod(
            49,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulChapter == null ? (Parameter)PTR_NULL : new Pointer(ulChapter)
            }
        );
        return ulChapter;
    }

    public BStr getCurrentTime()
        throws ComException
    {
        BStr bstrTime = new BStr();
        invokeStandardVirtualMethod(
            50,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bstrTime == null ? (Parameter)PTR_NULL : new Pointer(bstrTime)
            }
        );
        return bstrTime;
    }

    public void setRoot(
        BStr /*[in]*/ pbstrPath)
        throws ComException
    {
        invokeStandardVirtualMethod(
            51,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrPath == null ? (Parameter)PTR_NULL : new Const(pbstrPath)
            }
        );
    }

    public BStr getRoot()
        throws ComException
    {
        BStr pbstrPath = new BStr();
        invokeStandardVirtualMethod(
            52,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pbstrPath == null ? (Parameter)PTR_NULL : new Pointer(pbstrPath)
            }
        );
        return pbstrPath;
    }

    public UInt32 getFramesPerSecond()
        throws ComException
    {
        UInt32 ulFps = new UInt32();
        invokeStandardVirtualMethod(
            53,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulFps == null ? (Parameter)PTR_NULL : new Pointer(ulFps)
            }
        );
        return ulFps;
    }

    public UInt32 getCurrentDomain()
        throws ComException
    {
        UInt32 ulDomain = new UInt32();
        invokeStandardVirtualMethod(
            54,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulDomain == null ? (Parameter)PTR_NULL : new Pointer(ulDomain)
            }
        );
        return ulDomain;
    }

    public UInt32 getTitlesAvailable()
        throws ComException
    {
        UInt32 ulTitles = new UInt32();
        invokeStandardVirtualMethod(
            55,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ulTitles == null ? (Parameter)PTR_NULL : new Pointer(ulTitles)
            }
        );
        return ulTitles;
    }

    public UInt32 getVolumesAvailable()
        throws ComException
    {
        UInt32 pulVolumes = new UInt32();
        invokeStandardVirtualMethod(
            56,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pulVolumes == null ? (Parameter)PTR_NULL : new Pointer(pulVolumes)
            }
        );
        return pulVolumes;
    }

    public UInt32 getCurrentVolume()
        throws ComException
    {
        UInt32 pulVolume = new UInt32();
        invokeStandardVirtualMethod(
            57,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pulVolume == null ? (Parameter)PTR_NULL : new Pointer(pulVolume)
            }
        );
        return pulVolume;
    }

    public UInt32 getCurrentDiscSide()
        throws ComException
    {
        UInt32 pulDiscSide = new UInt32();
        invokeStandardVirtualMethod(
            58,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pulDiscSide == null ? (Parameter)PTR_NULL : new Pointer(pulDiscSide)
            }
        );
        return pulDiscSide;
    }

    public VariantBool getCCActive()
        throws ComException
    {
        VariantBool bCCActive = new VariantBool();
        invokeStandardVirtualMethod(
            59,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bCCActive == null ? (Parameter)PTR_NULL : new Pointer(bCCActive)
            }
        );
        return bCCActive;
    }

    public void setCCActive(
        VariantBool /*[in]*/ bCCActive)
        throws ComException
    {
        invokeStandardVirtualMethod(
            60,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bCCActive
            }
        );
    }

    public UInt32 getCurrentCCService()
        throws ComException
    {
        UInt32 pulService = new UInt32();
        invokeStandardVirtualMethod(
            61,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pulService == null ? (Parameter)PTR_NULL : new Pointer(pulService)
            }
        );
        return pulService;
    }

    public void setCurrentCCService(
        UInt32 /*[in]*/ pulService)
        throws ComException
    {
        invokeStandardVirtualMethod(
            62,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pulService
            }
        );
    }

    public BStr getUniqueID()
        throws ComException
    {
        BStr pvtUniqueID = new BStr();
        invokeStandardVirtualMethod(
            63,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pvtUniqueID == null ? (Parameter)PTR_NULL : new Pointer(pvtUniqueID)
            }
        );
        return pvtUniqueID;
    }

    public UInt32 getColorKey()
        throws ComException
    {
        UInt32 pClr = new UInt32();
        invokeStandardVirtualMethod(
            64,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pClr == null ? (Parameter)PTR_NULL : new Pointer(pClr)
            }
        );
        return pClr;
    }

    public void setColorKey(
        UInt32 /*[in]*/ pClr)
        throws ComException
    {
        invokeStandardVirtualMethod(
            65,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pClr
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IMediaPlayerDvdImpl(this);
    }
}
