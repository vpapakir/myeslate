package wmp.mediaplayer.server;

import com.jniwrapper.Parameter;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IDirectControl.
 */
public class IDirectControlVTBL extends IDispatchVTBL
{
    public IDirectControlVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "createView",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "destroyAllViews",
                    new HResult(),
                    new Parameter[] {
                    }
                )
            }
        );
    }
}