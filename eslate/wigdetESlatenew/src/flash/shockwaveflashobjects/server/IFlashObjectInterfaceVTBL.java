package flash.shockwaveflashobjects.server;

import com.jniwrapper.win32.automation.server.IDispatchExVTBL;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IFlashObjectInterface.
 */
public class IFlashObjectInterfaceVTBL extends IDispatchExVTBL
{
    public IFlashObjectInterfaceVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
            }
        );
    }
}