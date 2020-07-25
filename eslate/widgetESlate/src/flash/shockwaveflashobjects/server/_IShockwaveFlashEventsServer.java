package flash.shockwaveflashobjects.server;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

import flash.shockwaveflashobjects._IShockwaveFlashEvents;

/**
 * Adapter for server implementation of _IShockwaveFlashEvents
 */
public class _IShockwaveFlashEventsServer extends IDispatchServer
    implements _IShockwaveFlashEvents
{
    public _IShockwaveFlashEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_IShockwaveFlashEvents.class);
    }

    public void onReadyStateChange(
        Int32 /*[in]*/ newState)
    {
    }

    public void onProgress(
        Int32 /*[in]*/ percentDone)
    {
    }

    public void FSCommand(
        BStr /*[in]*/ command,
        BStr /*[in]*/ args)
    {
    }

    public void flashCall(
        BStr /*[in]*/ request)
    {
    }

}