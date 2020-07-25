package wmp.mediaplayer.server;

import wmp.mediaplayer.impl.IRadioPlayerImpl;

import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IRadioServer.
 */
public class IRadioServerVTBL extends IDispatchVTBL
{
    public IRadioServerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "bindToRadio",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new IRadioPlayerImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "isRadioExists",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "launchStandardUrl",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new IUnknownImpl()
                    }
                )
            }
        );
    }
}