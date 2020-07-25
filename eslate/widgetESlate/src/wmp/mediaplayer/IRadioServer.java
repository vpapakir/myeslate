package wmp.mediaplayer;

import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;

/**
 * Represents Java interface for COM interface IRadioServer.
 */
public interface IRadioServer extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{9C2263A0-3E3C-11D2-9BD3-204C4F4F5020}";

    IRadioPlayer bindToRadio(
        BStr /*[in]*/ wszRadio)
        throws ComException;

    void isRadioExists(
        BStr /*[in]*/ wszRadio)
        throws ComException;

    void launchStandardUrl(
        BStr /*[in]*/ bszUrl,
        IUnknown /*[in]*/ pBrowser)
        throws ComException;
}
