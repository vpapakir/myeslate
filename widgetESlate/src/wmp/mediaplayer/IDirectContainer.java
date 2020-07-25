package wmp.mediaplayer;

import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IServiceProvider;
import com.jniwrapper.win32.com.IUnknown;

/**
 * Represents Java interface for COM interface IDirectContainer.
 */
public interface IDirectContainer extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{39A2C2A8-4778-11D2-9BDB-204C4F4F5020}";

    void createControl(
        BStr /*[in]*/ bszClsid,
        UInt32 /*[in]*/ dwClsContext,
        IUnknown /*[in]*/ ppunk,
        UInt32 /*[in]*/ dwWindowStyle)
        throws ComException;

    void setServiceProvider(
        IServiceProvider /*[in]*/ pspSet)
        throws ComException;

    void setIInputObjectSite(
        IUnknown /*[in]*/ pios)
        throws ComException;

    void showControl()
        throws ComException;

    void hideControl()
        throws ComException;

    void isControlCreated()
        throws ComException;

    void destroyControl()
        throws ComException;
}
