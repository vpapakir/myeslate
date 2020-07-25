package wmp.mediaplayer;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass ppDSFile.
 */
public class ppDSFile extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{1D1237A0-6CD6-11D2-96BA-00104B242E64}");

    public ppDSFile()
    {
    }

    public ppDSFile(ppDSFile that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IUnknown create(ClsCtx dwClsContext) throws ComException
    {
        final IUnknownImpl intf = new IUnknownImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IUnknown</code> interface from IUnknown instance.
     */
    public static IUnknown queryInterface(IUnknown unknown) throws ComException
    {
        final IUnknownImpl result = new IUnknownImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new ppDSFile(this);
    }
}