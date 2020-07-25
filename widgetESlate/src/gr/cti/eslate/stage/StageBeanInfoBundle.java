package gr.cti.eslate.stage;

import java.util.ListResourceBundle;

/**
 * English language resources for the stage component bean info
 *
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 * @version     2.0.9, 21-Nov-2006
 */
public class StageBeanInfoBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"mouseEntered",    "Mouse entered"},
        {"mouseExited",     "Mouse exited"},
        {"mouseMoved",      "Mouse moved"},

        {"propertyChange",  "Property change"},
        {"vetoableChange",  "Vetoable change"},

        {"componentHidden", "Component hidden"},
        {"componentShown",  "Component shown"},

        {"keyPressed", "Key pressed"},
        {"keyReleased", "Key released"},
        {"keyTyped", "Key typed"},

        {"menuBarVisible",        "MenuBar visible"},
        {"menuBarVisibleTip",     "Adjust the visibility of the MenuBar"},
        {"toolBarVisible",        "ToolBar visible"},
        {"toolBarVisibleTip",     "Adjust the visibility of the ToolBar"},
        {"border",                "Border"},
        {"borderTip",             "Adjust the visibility of Border"},

        {"objectMovementEnabled", "Objects moveable via mouse"},
        {"objectMovementEnabledTip", "Allow Objects to be moved with the mouse"},

        {"controlPointMovementEnabled", "Control points moveable via mouse"},
        {"controlPointMovementEnabledTip", "Allow Control points to be moved with the mouse"},

        {"viewMovementEnabled", "View moveable via mouse"},
        {"viewMovementEnabledTip", "Allow view to be moved with the mouse"},

        {"objectsAdjustable", "Objects editable via mouse"},
        {"objectsAdjustableTip", "Allow objects to be edited with the mouse"},

        {"coordinatesVisible", "Coordinates visible"},
        {"coordinatesVisibleTip", "Adjust the visibility of Coordinates"},
        {"gridVisible", "Grid visible"},
        {"gridVisibleTip", "Adjust the visibility of the Grid"},
        {"gridSize", "Grid size"},
        {"gridSizeTip", "Adjust the size of the Grid"},
        {"axisVisible", "Axis visible"},
        {"axisVisibleTip", "Adjust the visibility of Axis"},
        {"controlPointsVisible", "Control Points visible"}, //12May2000
        {"controlPointsVisibleTip", "Adjust the visibility of Control Points"}, //12May2000

        {"marksOverShapes", "Axis & Grid over shapes"},
        {"marksOverShapesTip", "Allow Axis & Grid to lie over shapes"},

        {"image","image"} //12May2000
    };
}

