package wmp.mediaplayer;

import wmp.mediaplayer.impl.IDirectControlImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass DirectControl.
 */
public class DirectControl extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{39A2C2A6-4778-11D2-9BDB-204C4F4F5020}");

    public DirectControl()
    {
    }

    public DirectControl(DirectControl that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IDirectControl create(ClsCtx dwClsContext) throws ComException
    {
        final IDirectControlImpl intf = new IDirectControlImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IDirectControl</code> interface from IUnknown instance.
     */
    public static IDirectControl queryInterface(IUnknown unknown) throws ComException
    {
        final IDirectControlImpl result = new IDirectControlImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new DirectControl(this);
    }
}