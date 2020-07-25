package wmp.mediaplayer.server;

import wmp.mediaplayer._IDirectContainerEvents;

import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

/**
 * Adapter for server implementation of _IDirectContainerEvents
 */
public class _IDirectContainerEventsServer extends IDispatchServer
    implements _IDirectContainerEvents
{
    public _IDirectContainerEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_IDirectContainerEvents.class);
    }

}