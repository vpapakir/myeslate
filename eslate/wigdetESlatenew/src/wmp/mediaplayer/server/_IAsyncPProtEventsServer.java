package wmp.mediaplayer.server;

import wmp.mediaplayer._IAsyncPProtEvents;

import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

/**
 * Adapter for server implementation of _IAsyncPProtEvents
 */
public class _IAsyncPProtEventsServer extends IDispatchServer
    implements _IAsyncPProtEvents
{
    public _IAsyncPProtEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_IAsyncPProtEvents.class);
    }

}