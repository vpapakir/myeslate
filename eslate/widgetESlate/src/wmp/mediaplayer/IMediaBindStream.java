package wmp.mediaplayer;

import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface IMediaBindStream.
 */
public interface IMediaBindStream extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{920F0DE3-91C5-11D2-828F-00C04FC99D4E}";

    void loadMoniker(
        BStr /*[in]*/ bszTransferContext,
        BStr /*[in]*/ bszUrl)
        throws ComException;
}
