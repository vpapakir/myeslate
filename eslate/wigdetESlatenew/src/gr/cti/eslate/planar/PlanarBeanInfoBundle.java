package gr.cti.eslate.planar;

import java.util.ListResourceBundle;


public class PlanarBeanInfoBundle extends ListResourceBundle {
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
        {"menuBarVisibleTip",     "Adjust the visibility of the MenuBar"},
        {"toolBarVisible",        "ToolBar visible"},
        {"toolBarVisibleTip",     "Adjust the visibility of the ToolBar"},

        {"border",                "Border"},
        {"borderTip",             "Adjust the visibility of Border"},

        {"fromX","From X"}, //29Jun2000
        {"fromXTip","From X"}, //29Jun2000
        {"toX","To X"},     //29Jun2000
        {"toXTip","To X"},     //29Jun2000
        {"fromY","From Y"}, //29Jun2000
        {"fromYTip","From Y"}, //29Jun2000
        {"toY","To Y"},      //29Jun2000
        {"toYTip","To Y"}      //29Jun2000

    };
}

