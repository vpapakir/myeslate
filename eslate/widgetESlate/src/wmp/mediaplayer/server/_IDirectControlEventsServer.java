package wmp.mediaplayer.server;

import wmp.mediaplayer._IDirectControlEvents;

import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

/**
 * Adapter for server implementation of _IDirectControlEvents
 */
public class _IDirectControlEventsServer extends IDispatchServer
    implements _IDirectControlEvents
{
    public _IDirectControlEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_IDirectControlEvents.class);
    }

}