package wmp.mediaplayer;

import wmp.mediaplayer.impl.IRadioPlayerImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass RadioServer.
 */
public class RadioServer extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{8E71888A-423F-11D2-876E-00A0C9082467}");

    public RadioServer()
    {
    }

    public RadioServer(RadioServer that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IRadioPlayer create(ClsCtx dwClsContext) throws ComException
    {
        final IRadioPlayerImpl intf = new IRadioPlayerImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IRadioPlayer</code> interface from IUnknown instance.
     */
    public static IRadioPlayer queryInterface(IUnknown unknown) throws ComException
    {
        final IRadioPlayerImpl result = new IRadioPlayerImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new RadioServer(this);
    }
}