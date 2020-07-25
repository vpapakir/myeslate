package wmp.mediaplayer;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.com.ComException;

/**
 * Represents Java interface for COM interface IRadioBand.
 */
public interface IRadioBand extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{8E718881-423F-11D2-876E-00A0C9082467}";

    void create(
        Int32 /*[in]*/ phwnd,
        Int32 /*[in]*/ hwndParent)
        throws ComException;
}
