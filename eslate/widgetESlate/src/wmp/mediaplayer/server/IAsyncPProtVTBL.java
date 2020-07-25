package wmp.mediaplayer.server;

import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IAsyncPProt.
 */
public class IAsyncPProtVTBL extends IDispatchVTBL
{
    public IAsyncPProtVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
            }
        );
    }
}