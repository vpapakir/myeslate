package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ComponentLayerDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",       "Component layers"},
        {"MicroworldLayers",  "Microworld layers"},
        {"DragToChangeLayer", "You mau drag the component to another layer"},
        {"OK",                "OK"},
        {"Cancel",            "Cancel"},
    };
}
