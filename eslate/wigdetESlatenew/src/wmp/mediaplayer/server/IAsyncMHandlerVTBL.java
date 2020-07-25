package wmp.mediaplayer.server;

import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IAsyncMHandler.
 */
public class IAsyncMHandlerVTBL extends IDispatchVTBL
{
    public IAsyncMHandlerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
            }
        );
    }
}