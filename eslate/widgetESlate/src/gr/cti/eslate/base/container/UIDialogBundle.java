package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class UIDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Border",            "Border"},
        {"Color",             "Color"},
        {"Image",             "Image"},
        {"None",              "None"},
        {"Color1",            "Select color"},
        {"Image1",            "Select image"},
		{"SelectIcon",        "Select icon"},
        {"Title",             "Title"},
        {"Insets",            "Insets"},
        {"MWUI",              "Microwold interface settings"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
        {"borderColor",       "Border color"},
        {"selectBorderImage", "Select border icon"},
		{"selectMicroworldIcon",  "Select microworld icon"},
        {"Up",                "Up"},
        {"Down",              "Down"},
        {"Right",             "Right"},
        {"Left",              "Left"},
        {"OuterBorder",       "Outer border"},
        {"UseOuterBorder",    "Outer border"},
        {"Raised",            "Raised"},
        {"Lowered",           "Lowered"},
        {"Background",        "Background"},
		{"MwdIcon",           "Microworld icon"},
        {"backgroundColor",   "Background color"},
        {"selectBackgroundImage", "Select background icon"},
        {"Center",            "Center"},
        {"Fit",               "Fit"},
        {"Tile",              "Tile"},
        {"UnableToSaveIcon",  "Error while saving file"},
        {"SaveBorderIcon",    "Export border icon"},
        {"SaveBgrIcon",       "Export background icon"},
		{"SaveMwdIcon",       "Export microworld icon"},
        {"L&F",               "Look & Feel"},
    };
}

