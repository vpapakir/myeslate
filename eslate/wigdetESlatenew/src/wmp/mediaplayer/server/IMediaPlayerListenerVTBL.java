package wmp.mediaplayer.server;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IUnknownVTBL;

/**
 * Represents VTBL for COM interface IMediaPlayerListener.
 */
public class IMediaPlayerListenerVTBL extends IUnknownVTBL
{
    public IMediaPlayerListenerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "playStateChanged",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "buffering",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "bufferPercent",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "openStateChanged",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "mediaInfoChanged",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BStr(),
                        new BStr(),
                        new BStr(),
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "qualityChanged",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "error",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                )
            }
        );
    }
}