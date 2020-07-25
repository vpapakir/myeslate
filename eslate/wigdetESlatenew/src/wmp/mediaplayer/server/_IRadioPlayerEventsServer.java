package wmp.mediaplayer.server;

import wmp.mediaplayer._IRadioPlayerEvents;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

/**
 * Adapter for server implementation of _IRadioPlayerEvents
 */
public class _IRadioPlayerEventsServer extends IDispatchServer
    implements _IRadioPlayerEvents
{
    public _IRadioPlayerEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_IRadioPlayerEvents.class);
    }

    public void stateChange(
        BStr /*[in]*/ bszUrl,
        VariantBool /*[in]*/ fPlay,
        Int32 /*[in]*/ lVolume,
        VariantBool /*[in]*/ fMute)
        throws ComException
    {
    }

}