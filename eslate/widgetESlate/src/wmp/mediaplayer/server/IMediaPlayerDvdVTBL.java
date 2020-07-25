package wmp.mediaplayer.server;

import wmp.mediaplayer.DVDMenuIDConstants;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IMediaPlayerDvd.
 */
public class IMediaPlayerDvdVTBL extends IDispatchVTBL
{
    public IMediaPlayerDvdVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "buttonSelectAndActivate",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "upperButtonSelect",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "lowerButtonSelect",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "leftButtonSelect",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "rightButtonSelect",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "buttonActivate",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "forwardScan",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "backwardScan",
                    new HResult(),
                    new Parameter[] {
                        new DoubleFloat()
                    }
                ),
                new VirtualMethodCallback(
                    "prevPGSearch",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "topPGSearch",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "nextPGSearch",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "titlePlay",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "chapterPlay",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "chapterSearch",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "menuCall",
                    new HResult(),
                    new Parameter[] {
                        new DVDMenuIDConstants()
                    }
                ),
                new VirtualMethodCallback(
                    "resumeFromMenu",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "timePlay",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "timeSearch",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "chapterPlayAutoStop",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new UInt32(),
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "stillOff",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "goUp",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "getTotalTitleTime",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getNumberOfChapters",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new Pointer(new UInt32())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getAudioLanguage",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getSubpictureLanguage",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new Pointer(new BStr())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getAllGPRMs",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAllSPRMs",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "UOPValid",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getButtonsAvailable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentButton",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAudioStreamsAvailable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentAudioStream",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentAudioStream",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "getCurrentSubpictureStream",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentSubpictureStream",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "getSubpictureStreamsAvailable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSubpictureOn",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSubpictureOn",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getAnglesAvailable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentAngle",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentAngle",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "getCurrentTitle",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentChapter",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentTime",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setRoot",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getRoot",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getFramesPerSecond",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentDomain",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getTitlesAvailable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getVolumesAvailable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentVolume",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentDiscSide",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCCActive",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCCActive",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getCurrentCCService",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentCCService",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "getUniqueID",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getColorKey",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setColorKey",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                )
            }
        );
    }
}