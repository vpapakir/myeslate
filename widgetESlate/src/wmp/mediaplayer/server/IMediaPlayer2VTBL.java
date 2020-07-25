package wmp.mediaplayer.server;

import wmp.mediaplayer.MPShowDialogConstants;
import wmp.mediaplayer.impl.IMediaPlayerDvdImpl;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IMediaPlayer2.
 */
public class IMediaPlayer2VTBL extends IMediaPlayerVTBL
{
    public IMediaPlayer2VTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getDVD",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IMediaPlayerDvdImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getMediaParameter",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new BStr(),
                        new Pointer(new BStr())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getMediaParameterName",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new Int32(),
                        new Pointer(new BStr())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getEntryCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCurrentEntry",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setCurrentEntry",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "showDialog",
                    new HResult(),
                    new Parameter[] {
                        new MPShowDialogConstants()
                    }
                )
            }
        );
    }
}