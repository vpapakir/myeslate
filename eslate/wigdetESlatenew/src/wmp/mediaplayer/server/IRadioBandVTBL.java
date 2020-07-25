package wmp.mediaplayer.server;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.Pointer;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;

/**
 * Represents VTBL for COM interface IRadioBand.
 */
public class IRadioBandVTBL extends IDispatchVTBL
{
    public IRadioBandVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "create",
                    new HResult(),
                    new Parameter[] {
                        new Pointer.Const(new Int32()),
                        new Int32()
                    }
                )
            }
        );
    }
}