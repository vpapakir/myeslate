package wmp.mediaplayer;

import wmp.mediaplayer.impl.IRadioViewImpl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass RadioView.
 */
public class RadioView extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{847B4DF5-4B61-11D2-9BDB-204C4F4F5020}");

    public RadioView()
    {
    }

    public RadioView(RadioView that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IRadioView create(ClsCtx dwClsContext) throws ComException
    {
        final IRadioViewImpl intf = new IRadioViewImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IRadioView</code> interface from IUnknown instance.
     */
    public static IRadioView queryInterface(IUnknown unknown) throws ComException
    {
        final IRadioViewImpl result = new IRadioViewImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new RadioView(this);
    }
}