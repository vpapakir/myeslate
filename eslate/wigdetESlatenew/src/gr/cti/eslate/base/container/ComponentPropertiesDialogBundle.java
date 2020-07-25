package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ComponentPropertiesDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",                         "Component settings"},
        {"Component",                           "Component"},
        {"ChooseComponent",                     "Choose component to edit"},
        {"FrameTitleDisplayed",                 "Control bar visible"},
        {"ComponentFrozen",                     "Frozen"},
        {"ComponentResizable",                  "Resizable"},
        {"MinimizeVisible",                     "Minimize button visible"},
        {"MaximizeVisible",                     "Maximize button visible"},
        {"CloseVisible",                        "Close button visible"},
        {"PlugVisible",                         "Plug display enabled"},
        {"HelpVisible",                         "Help display enabled"},
        {"InfoVisible",                         "Information display enabled"},
        {"ComponentActivatableByMousePress",    "Activate component on mouse press"},
        {"ComponentNameChangeableFromMenuBar",  "Enable name change from control bar"},
        {"ComponentProperties",                 "Component settings"},
        {"ComponentBarProperties",              "Component bar settings"},
    };
}
