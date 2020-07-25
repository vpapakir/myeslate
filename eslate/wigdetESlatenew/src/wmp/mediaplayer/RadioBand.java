package wmp.mediaplayer;

import wmp.mediaplayer.impl.IRadioBandImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass RadioBand.
 */
public class RadioBand extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{8E718888-423F-11D2-876E-00A0C9082467}");

    public RadioBand()
    {
    }

    public RadioBand(RadioBand that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IRadioBand create(ClsCtx dwClsContext) throws ComException
    {
        final IRadioBandImpl intf = new IRadioBandImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IRadioBand</code> interface from IUnknown instance.
     */
    public static IRadioBand queryInterface(IUnknown unknown) throws ComException
    {
        final IRadioBandImpl result = new IRadioBandImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new RadioBand(this);
    }
}