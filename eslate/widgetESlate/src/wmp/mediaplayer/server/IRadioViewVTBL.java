package wmp.mediaplayer.server;

import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IRadioView.
 */
public class IRadioViewVTBL extends IDispatchVTBL
{
    public IRadioViewVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
            }
        );
    }
}