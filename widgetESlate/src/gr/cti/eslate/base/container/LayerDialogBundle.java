package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class LayerDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",     "Define microworld layers"},
        {"OK",              "OK"},
        {"Cancel",          "Cancel"},
        {"NewLayer",        "New layer"},
        {"UpButtonTip",     "Advance selected layer"},
        {"DownButtonTip",   "Denote selected layer"},
        {"AddButtonTip",    "Insert new layer"},
        {"DeleteButtonTip", "Remove selected layers"},
        {"Error",           "Error"},
        {"Warning",         "Warning"},
        {"SameLayerNamesError","Some layer names are the same. Layers must have unique names."},
        {"LayersMoved0",    "Layer \""},
        {"LayersMoved1",    "\" contains component \""},
        {"LayersMoved2",    "\". The component will be moved to the default layer."},
		{"NewLayerMsg",       "Enter the name of the new layer"},
		{"NewLayerTitle",     "New layer"},
		{"NameExists",        "The layer was not defined. Another layer has the same name"},
		{"RenameLayerMsg",    "Enter the new name of the layer"},
		{"RenameLayerTitle",  "Rename layer"},
    };
}
