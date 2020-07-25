package wmp.mediaplayer.server;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IRadioPlayer.
 */
public class IRadioPlayerVTBL extends IDispatchVTBL
{
    public IRadioPlayerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "bindRadioMemory",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "releaseRadio",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "registerEvent",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new Int32())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "registerWindow",
                    new HResult(),
                    new Parameter[] {
                        new Int32(),
                        new UInt32(),
                        new UInt32(),
                        new Pointer(new Int32())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "getSection",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "unregister",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getInstanceCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
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
                    "setUrl",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "setVolume",
                    new HResult(),
                    new Parameter[] {
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "setMute",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool()
                    }
                ),
                new VirtualMethodCallback(
                    "getStatus",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32()),
                        new Pointer(new Int32()),
                        new Pointer(new Int32()),
                        new Pointer(new BStr()),
                        new Pointer(new BStr()),
                        new Pointer(new BStr()),
                        new Pointer(new BStr()),
                        new Pointer(new BStr()),
                        new Pointer(new BStr()),
                        new Pointer(new BStr())
                    }
                ),
                new VirtualMethodCallback(
                    "getState",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32()),
                        new Pointer(new Int32()),
                        new Pointer(new Int32()),
                        new Pointer(new Int32())
                    }
                )
            }
        );
    }
}