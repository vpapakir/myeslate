package wmp.mediaplayer.server;

import wmp.mediaplayer.ReadyStateConstants;
import wmp.mediaplayer._MediaPlayerEvents;
import wmp.stdole.OLE_XPOS_PIXELS;
import wmp.stdole.OLE_YPOS_PIXELS;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int16;
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IDispatchServer;

/**
 * Adapter for server implementation of _MediaPlayerEvents
 */
public class _MediaPlayerEventsServer extends IDispatchServer
    implements _MediaPlayerEvents
{
    public _MediaPlayerEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(_MediaPlayerEvents.class);
    }

    public void DVDNotify(
        Int32 /*[in]*/ EventCode,
        Int32 /*[in]*/ EventParam1,
        Int32 /*[in]*/ EventParam2)
    {
    }

    public void endOfStream(
        Int32 /*[in]*/ Result)
    {
    }

    public void keyDown(
        Int16 /*[in]*/ KeyCode,
        Int16 /*[in]*/ ShiftState)
    {
    }

    public void keyUp(
        Int16 /*[in]*/ KeyCode,
        Int16 /*[in]*/ ShiftState)
    {
    }

    public void keyPress(
        Int16 /*[in]*/ CharacterCode)
    {
    }

    public void mouseMove(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
    }

    public void mouseDown(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
    }

    public void mouseUp(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
    }

    public void click(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
    }

    public void dblClick(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
    }

    public void openStateChange(
        Int32 /*[in]*/ OldState,
        Int32 /*[in]*/ NewState)
    {
    }

    public void playStateChange(
        Int32 /*[in]*/ OldState,
        Int32 /*[in]*/ NewState)
    {
    }

    public void scriptCommand(
        BStr /*[in]*/ scType,
        BStr /*[in]*/ Param)
    {
    }

    public void buffering(
        VariantBool /*[in]*/ Start)
    {
    }

    public void error()
    {
    }

    public void markerHit(
        Int32 /*[in]*/ MarkerNum)
    {
    }

    public void warning(
        Int32 /*[in]*/ WarningType,
        Int32 /*[in]*/ Param,
        BStr /*[in]*/ Description)
    {
    }

    public void newStream()
    {
    }

    public void disconnect(
        Int32 /*[in]*/ Result)
    {
    }

    public void positionChange(
        DoubleFloat /*[in]*/ oldPosition,
        DoubleFloat /*[in]*/ newPosition)
    {
    }

    public void displayModeChange()
    {
    }

    public void readyStateChange(
        ReadyStateConstants /*[in]*/ ReadyState)
    {
    }

}