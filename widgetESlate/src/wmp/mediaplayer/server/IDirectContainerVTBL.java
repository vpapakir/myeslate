package wmp.mediaplayer.server;

import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.impl.IServiceProviderImpl;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IUnknownVTBL;

/**
 * Represents VTBL for COM interface IDirectContainer.
 */
public class IDirectContainerVTBL extends IUnknownVTBL
{
    public IDirectContainerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "createControl",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new UInt32(),
                        new Pointer.Const(new IUnknownImpl()),
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "setServiceProvider",
                    new HResult(),
                    new Parameter[] {
                        new IServiceProviderImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "setIInputObjectSite",
                    new HResult(),
                    new Parameter[] {
                        new IUnknownImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "showControl",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "hideControl",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "isControlCreated",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "destroyControl",
                    new HResult(),
                    new Parameter[] {
                    }
                )
            }
        );
    }
}