package flash.shockwaveflashobjects.server;

import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IUnknownVTBL;

/**
 * Represents VTBL for COM interface IFlashFactory.
 */
public class IFlashFactoryVTBL extends IUnknownVTBL
{
    public IFlashFactoryVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
            }
        );
    }
}