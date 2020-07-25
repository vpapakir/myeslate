package wmp.mediaplayer;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface _IRadioPlayerEvents.
 */
public interface _IRadioPlayerEvents extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{9C2263B1-3E3C-11D2-9BD3-204C4F4F5020}";

    static final int DISPID_stateChange = 12;


    void stateChange(
        BStr /*[in]*/ bszUrl,
        VariantBool /*[in]*/ fPlay,
        Int32 /*[in]*/ lVolume,
        VariantBool /*[in]*/ fMute)
        throws ComException;
}
