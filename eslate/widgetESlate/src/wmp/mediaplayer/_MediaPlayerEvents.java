package wmp.mediaplayer;

import wmp.stdole.OLE_XPOS_PIXELS;
import wmp.stdole.OLE_YPOS_PIXELS;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int16;
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;

/**
 * Represents Java interface for COM interface _MediaPlayerEvents.
 */
public interface _MediaPlayerEvents extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{2D3A4C40-E711-11D0-94AB-0080C74C7E95}";

    static final int DISPID_DVDNotify = 1505;
    static final int DISPID_endOfStream = 3002;
    static final int DISPID_keyDown = -602;
    static final int DISPID_keyUp = -604;
    static final int DISPID_keyPress = -603;
    static final int DISPID_mouseMove = -606;
    static final int DISPID_mouseDown = -605;
    static final int DISPID_mouseUp = -607;
    static final int DISPID_click = -600;
    static final int DISPID_dblClick = -601;
    static final int DISPID_openStateChange = 3011;
    static final int DISPID_playStateChange = 3012;
    static final int DISPID_scriptCommand = 3001;
    static final int DISPID_buffering = 3003;
    static final int DISPID_error = 3010;
    static final int DISPID_markerHit = 3006;
    static final int DISPID_warning = 3009;
    static final int DISPID_newStream = 3008;
    static final int DISPID_disconnect = 3004;
    static final int DISPID_positionChange = 2;
    static final int DISPID_displayModeChange = 51;
    static final int DISPID_readyStateChange = -609;


    void DVDNotify(
        Int32 /*[in]*/ EventCode,
        Int32 /*[in]*/ EventParam1,
        Int32 /*[in]*/ EventParam2);

    void endOfStream(
        Int32 /*[in]*/ Result);

    void keyDown(
        Int16 /*[in]*/ KeyCode,
        Int16 /*[in]*/ ShiftState);

    void keyUp(
        Int16 /*[in]*/ KeyCode,
        Int16 /*[in]*/ ShiftState);

    void keyPress(
        Int16 /*[in]*/ CharacterCode);

    void mouseMove(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y);

    void mouseDown(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y);

    void mouseUp(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y);

    void click(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y);

    void dblClick(
        Int16 /*[in]*/ Button,
        Int16 /*[in]*/ ShiftState,
        OLE_XPOS_PIXELS /*[in]*/ x,
        OLE_YPOS_PIXELS /*[in]*/ y);

    void openStateChange(
        Int32 /*[in]*/ OldState,
        Int32 /*[in]*/ NewState);

    void playStateChange(
        Int32 /*[in]*/ OldState,
        Int32 /*[in]*/ NewState);

    void scriptCommand(
        BStr /*[in]*/ scType,
        BStr /*[in]*/ Param);

    void buffering(
        VariantBool /*[in]*/ Start);

    void error();

    void markerHit(
        Int32 /*[in]*/ MarkerNum);

    void warning(
        Int32 /*[in]*/ WarningType,
        Int32 /*[in]*/ Param,
        BStr /*[in]*/ Description);

    void newStream();

    void disconnect(
        Int32 /*[in]*/ Result);

    void positionChange(
        DoubleFloat /*[in]*/ oldPosition,
        DoubleFloat /*[in]*/ newPosition);

    void displayModeChange();

    void readyStateChange(
        ReadyStateConstants /*[in]*/ ReadyState);
}
