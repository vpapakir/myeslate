package wmp.mediaplayer.server;

import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IRadioServerControl.
 */
public class IRadioServerControlVTBL extends IDispatchVTBL
{
    public IRadioServerControlVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
            }
        );
    }
}