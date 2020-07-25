package wmp.mediaplayer;

import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface IDirectControl.
 */
public interface IDirectControl extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{39A2C2A5-4778-11D2-9BDB-204C4F4F5020}";

    void createView(
        BStr /*[in]*/ bszClsid)
        throws ComException;

    void destroyAllViews()
        throws ComException;
}
