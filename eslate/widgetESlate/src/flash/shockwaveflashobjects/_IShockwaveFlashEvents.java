package flash.shockwaveflashobjects;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;

/**
 * Represents Java interface for COM interface _IShockwaveFlashEvents.
 */
public interface _IShockwaveFlashEvents extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{D27CDB6D-AE6D-11CF-96B8-444553540000}";

    static final int DISPID_onReadyStateChange = -609;
    static final int DISPID_onProgress = 1958;
    static final int DISPID_FSCommand = 150;
    static final int DISPID_flashCall = 197;


    void onReadyStateChange(
        Int32 /*[in]*/ newState);

    void onProgress(
        Int32 /*[in]*/ percentDone);

    void FSCommand(
        BStr /*[in]*/ command,
        BStr /*[in]*/ args);

    void flashCall(
        BStr /*[in]*/ request);
}
