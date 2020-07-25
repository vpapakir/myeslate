package wmp.mediaplayer.server;

import wmp.mediaplayer._IRadioViewEvents;

import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

/**
 * Adapter for server implementation of _IRadioViewEvents
 */
public class _IRadioViewEventsServer extends IDispatchServer
    implements _IRadioViewEvents
{
    public _IRadioViewEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_IRadioViewEvents.class);
    }

}