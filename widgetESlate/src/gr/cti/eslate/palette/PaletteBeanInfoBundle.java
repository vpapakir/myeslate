package gr.cti.eslate.palette;

import java.util.ListResourceBundle;


public class PaletteBeanInfoBundle extends ListResourceBundle {
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

        {"border",                "Border"},
        {"borderTip",             "Adjust the visibility of Border"},

        {"foregroundColor", "Pen color"},
        {"backgroundColor", "Background color"},
        {"fillColor", "Fill color"},

        {"foregroundColorTip", "Select Pen color"},
        {"backgroundColorTip", "Select Background color"},
        {"fillColorTip", "Select Fill color"}

    };
}

