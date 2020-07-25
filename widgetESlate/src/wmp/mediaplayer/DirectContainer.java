package wmp.mediaplayer;

import wmp.mediaplayer.impl.IDirectContainerImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass DirectContainer.
 */
public class DirectContainer extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{39A2C2A9-4778-11D2-9BDB-204C4F4F5020}");

    public DirectContainer()
    {
    }

    public DirectContainer(DirectContainer that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IDirectContainer create(ClsCtx dwClsContext) throws ComException
    {
        final IDirectContainerImpl intf = new IDirectContainerImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IDirectContainer</code> interface from IUnknown instance.
     */
    public static IDirectContainer queryInterface(IUnknown unknown) throws ComException
    {
        final IDirectContainerImpl result = new IDirectContainerImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new DirectContainer(this);
    }
}