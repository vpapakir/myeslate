package gr.cti.eslate.canvas;

import java.util.ListResourceBundle;


/**
 * @version     2.0.7, 14-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class CanvasBeanInfoBundle extends ListResourceBundle {
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

        {"menuBarVisible",        "MenuBar visible"},
        {"toolBarVisible",        "ToolBar visible"},
        {"menuBarVisibleTip",     "Adjust the visibility of the MenuBar"},
        {"toolBarVisibleTip",     "Adjust the visibility of the ToolBar"},

        {"border",                "Border"},
        {"borderTip",             "Adjust the visibility of Border"},

        {"fixedTurtlePageSizes", "Fixed turtle page sizes"},
        {"fixedTurtlePageSizesTip", "Specify if turtle pages have fixed sizes"},

        {"foregroundColor", "Pen Color"},
        {"backgroundColor", "Rubber Color"},
        {"foregroundColorTip", "Select Pen Color"},
        {"backgroundColorTip", "Select Rubber Color"}

    };
}

