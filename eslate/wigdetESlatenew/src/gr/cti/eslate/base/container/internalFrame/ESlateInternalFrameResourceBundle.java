package gr.cti.eslate.base.container.internalFrame;

import java.util.ListResourceBundle;

/**
 * The ESlateInternalFrame Resource Bundle.
 * @author  Giorgos Vasiliou
 * @version 1.0
 */

public class ESlateInternalFrameResourceBundle extends ListResourceBundle {

    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Maximize",                "Maximize"},
        {"Iconify",                 "Hide"},
        {"Close",                   "Close"},
        {"Restore",					"Restore"},
        {"ShowComponentMenu",	  	"Show component menu"},			
        {"frame",                   "Frame"},
        {"north",                   "North pane"},
        {"south",                   "South pane"},
        {"east",                    "East pane"},
        {"west",                    "West pane"},
        {"northOverWest",           "North over West"},
        {"eastOverNorth",           "East over North"},
        {"southOverEast",           "South over East"},
        {"westOverSouth",           "West over South"},
        {"northVisible",            "Panel North visible"},
        {"southVisible",            "Panel South visible"},
        {"eastVisible",             "Panel East visible"},
        {"westVisible",             "Panel West visible"},
        {"draggableFromNorth",      "Drag frame from North"},
        {"draggableFromSouth",      "Drag frame from South"},
        {"draggableFromEast",       "Drag frame from East"},
        {"draggableFromWest",       "Drag frame from West"},
        {"ok",                      "OK"},
        {"layoutcustomizer",        "Layout customizer"},
        {"fooLayoutCustomizer",     "Layout"},
        {"fooLayoutCustomizertip",  "Controls the layout of the surrounding panels."},
        {"change",                  "Change"},
        {"closeButtonVisible",      "Button \"close\" visible"},
        {"minMaxButtonVisible",     "Buttons \"maximize/iconify\" visible"},
        {"helpButtonVisible",       "Button \"help\" visible"},
        {"infoButtonVisible",       "Button \"info\" visible"},
        {"plugButtonVisible",       "Button \"plugs\" visible"},
        {"maxSizeRespected",        "Restrict maximum component size"},
        {"minSizeRespected",        "Restrict minimum component size"},
        {"maxSizeRespectedTip",     "The window's maximum size is restricted by the component's maximum size"},
        {"minSizeRespectedTip",     "The window's minimum size is restricted by the component's minimum size"},
        {"modal",                   "Modal"},
        {"modalTip",                "The window's  is modal, i.e. always on top and activated"},
//        {"borderActivationInsets",  "Resize border sensitivity"},
//        {"borderActivationInsetsTip","Adjusts the area on the window which triggers the appearance of the resize border"},
        {"FrameMsg1",               "Are you sure you want to remove component \""},
        {"FrameMsg2",               "Remove component"},
        {"FrameMsg3",               "\"?"},

        //SkinPane
        {"background",              "Background: Color"},
        {"backgroundImage",         "Background: Image"},
        {"preferredSize",           "Preferred size"},
        {"opaque",                  "Opaque"},
        {"centered",                "CENTERED"},
        {"stretched",               "STRETCHED"},
        {"tiled",                   "TILED"},
        {"customSize",              "Custom size"},
        {"customSizeTip",           "This is the size of the panel when custom size is enabled"},
        {"useCustomSize",           "Custom size enabled"},
        {"useCustomSizeTip",        "When enabled the size is not the preferred one but the one given"},
        {"MicroworldLockedException", "The microworld is locked. It's settings cannot be changed"},

        {"setEffect",               "Effect"},
        {"setEffectTip",            "Select effect to be applied to the component"},
        {"noneEffect",              "None"},
        {"alphaCompositeEffect",    "Alpha composite"},
        {"clippingEffect",          "Clipping"},
        {"intersectionEffect",      "Intersection"},
        {"edit",                    "Edit"},
        {"effectsDialogTitle",      "Edit effect"},
        {"okButton",                "OK"},
        {"cancelButton",            "Cancel"},
        {"antiAliasingCheck",       "Antialiasing"},
        {"renderingQualityCheck",   "Rendering quality"},
        {"numberOfSteps",           "Number of steps"},
        {"direction",               "Direction"},
        {"heightDecrease",          "Height decrease"},
        {"heightIncrease",          "Height increase"},
        {"widthDecrease",           "Width decrease"},
        {"widthIncrease",           "Width increase"},

        {"clipShapeType",           "Shape"},
        {"clipShapeTypeTip",        "Speficy the shape for the ESlateInternalFrame"},
    };
}
