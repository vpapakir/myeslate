package wmp.mediaplayer.impl;

import wmp.mediaplayer.ReadyStateConstants;
import wmp.mediaplayer._MediaPlayerEvents;
import wmp.stdole.OLE_XPOS_PIXELS;
import wmp.stdole.OLE_YPOS_PIXELS;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int16;
import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM dispinterface _MediaPlayerEvents.
 */
public class _MediaPlayerEventsImpl extends IDispatchImpl
    implements _MediaPlayerEvents
{
    public static final String INTERFACE_IDENTIFIER = "{2D3A4C40-E711-11D0-94AB-0080C74C7E95}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _MediaPlayerEventsImpl()
    {
    }

    protected _MediaPlayerEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _MediaPlayerEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _MediaPlayerEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _MediaPlayerEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void DVDNotify(
        Int32 /*[in]*/ EventCode,
        Int32 /*[in]*/ EventParam1,
        Int32 /*[in]*/ EventParam2)
    {
        Parameter[] parameters = new Parameter[] {
                EventCode,
                EventParam1,
                EventParam2
            };

        Automation.invokeDispatch(this, "DVDNotify", parameters, void.class);
    }

    public void endOfStream(
        Int32 /*[in]*/ Result)
    {
        Parameter[] parameters = new Parameter[] {
                Result
            };

        Automation.invokeDispatch(this, "endOfStream", parameters, void.class);
    }

    public void keyDown(
        Int16 /*[in]*/ KeyCode,
        Int16 /*[in]*/ ShiftState)
    {
        Parameter[] parameters = new Parameter[] {
                KeyCode,
                ShiftState
            };

        Automation.invokeDispatch(this, "keyDown", parameters, void.class);
    }

    public void keyUp(
        Int16 /*[in]*/ KeyCode,
        Int16 /*[in]*/ ShiftState)
    {
        Parameter[] parameters = new Parameter[] {
                KeyCode,
                ShiftState
            };

        Automation.invokeDispatch(this, "keyUp", parameters, void.class);
    }

    public void keyPress(
        Int16 /*[in]*/ CharacterCode)
    {
        Parameter[] parameters = new Parameter[] {
                CharacterCode
            };

        Automation.invokeDispatch(this, "keyPress", parameters, void.class);
    }

    public void mouseMove(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
        Parameter[] parameters = new Parameter[] {
                Button,
                ShiftState,
                x,
                y
            };

        Automation.invokeDispatch(this, "mouseMove", parameters, void.class);
    }

    public void mouseDown(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
        Parameter[] parameters = new Parameter[] {
                Button,
                ShiftState,
                x,
                y
            };

        Automation.invokeDispatch(this, "mouseDown", parameters, void.class);
    }

    public void mouseUp(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
        Parameter[] parameters = new Parameter[] {
                Button,
                ShiftState,
                x,
                y
            };

        Automation.invokeDispatch(this, "mouseUp", parameters, void.class);
    }

    public void click(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
        Parameter[] parameters = new Parameter[] {
                Button,
                ShiftState,
                x,
                y
            };

        Automation.invokeDispatch(this, "click", parameters, void.class);
    }

    public void dblClick(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y)
    {
        Parameter[] parameters = new Parameter[] {
                Button,
                ShiftState,
                x,
                y
            };

        Automation.invokeDispatch(this, "dblClick", parameters, void.class);
    }

    public void openStateChange(
        Int32 /*[in]*/ OldState,
        Int32 /*[in]*/ NewState)
    {
        Parameter[] parameters = new Parameter[] {
                OldState,
                NewState
            };

        Automation.invokeDispatch(this, "openStateChange", parameters, void.class);
    }

    public void playStateChange(
        Int32 /*[in]*/ OldState,
        Int32 /*[in]*/ NewState)
    {
        Parameter[] parameters = new Parameter[] {
                OldState,
                NewState
            };

        Automation.invokeDispatch(this, "playStateChange", parameters, void.class);
    }

    public void scriptCommand(
        BStr /*[in]*/ scType,
        BStr /*[in]*/ Param)
    {
        Parameter[] parameters = new Parameter[] {
                scType == null ? (Parameter)PTR_NULL : scType,
                Param == null ? (Parameter)PTR_NULL : Param
            };

        Automation.invokeDispatch(this, "scriptCommand", parameters, void.class);
    }

    public void buffering(
        VariantBool /*[in]*/ Start)
    {
        Parameter[] parameters = new Parameter[] {
                Start
            };

        Automation.invokeDispatch(this, "buffering", parameters, void.class);
    }

    public void error()
    {
        Parameter[] parameters = new Parameter[0];

        Automation.invokeDispatch(this, "error", parameters, void.class);
    }

    public void markerHit(
        Int32 /*[in]*/ MarkerNum)
    {
        Parameter[] parameters = new Parameter[] {
                MarkerNum
            };

        Automation.invokeDispatch(this, "markerHit", parameters, void.class);
    }

    public void warning(
        Int32 /*[in]*/ WarningType,
        Int32 /*[in]*/ Param,
        BStr /*[in]*/ Description)
    {
        Parameter[] parameters = new Parameter[] {
                WarningType,
                Param,
                Description == null ? (Parameter)PTR_NULL : Description
            };

        Automation.invokeDispatch(this, "warning", parameters, void.class);
    }

    public void newStream()
    {
        Parameter[] parameters = new Parameter[0];

        Automation.invokeDispatch(this, "newStream", parameters, void.class);
    }

    public void disconnect(
        Int32 /*[in]*/ Result)
    {
        Parameter[] parameters = new Parameter[] {
                Result
            };

        Automation.invokeDispatch(this, "disconnect", parameters, void.class);
    }

    public void positionChange(
        DoubleFloat /*[in]*/ oldPosition,
        DoubleFloat /*[in]*/ newPosition)
    {
        Parameter[] parameters = new Parameter[] {
                oldPosition,
                newPosition
            };

        Automation.invokeDispatch(this, "positionChange", parameters, void.class);
    }

    public void displayModeChange()
    {
        Parameter[] parameters = new Parameter[0];

        Automation.invokeDispatch(this, "displayModeChange", parameters, void.class);
    }

    public void readyStateChange(
        ReadyStateConstants /*[in]*/ ReadyState)
    {
        Parameter[] parameters = new Parameter[] {
                ReadyState
            };

        Automation.invokeDispatch(this, "readyStateChange", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _MediaPlayerEventsImpl(this);
    }
}
