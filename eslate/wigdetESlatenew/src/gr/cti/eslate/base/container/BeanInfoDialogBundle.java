package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class BeanInfoDialogBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",       "Properties"},
        {"Microworld",        "Microworld"},
        {"Close",             "Close"},
        {"NoComponent",       "No active component"},
        {"ObjectHierachy",    "Object hierarchy"},
        {"ComponentHierachy", "Component hierarchy"},
        {"UIHierachy",        "User interface object hierarchy"},
        {"Properties",        "Properties"},
        {"Refresh",           "Refresh"},
        {"Edit",              "Edit"},
        {"Add",               "Add component"},
        {"Remove",            "Remove component"},
        {"Properties",        "Properties"},
        {"Events",            "Events"},
        {"BorderLayoutPos",   "Choose position to insert the component"},
        {"BorderLayoutTitle", "Component position"},
        {"Settings",          "Settings"},
        {"PropertyType",      "Type"},
        {"ReadWrite",         "Read/Write"},
        {"ReadOnly",          "Read Only"},
        {"All",               "All"},
        {"Expert",            "Expert"},
        {"Basic",             "Basic"},
        {"Preferred",         "Preferred"},
        {"Bound",             "Bound"},
        {"Constrained",       "Constrained"},
        {"Hidden",            "Hidden"},
        {"CustomizerMenu",    "Customizer..."},
        {"Customizer",        "Customizer"},
        {"Error",             "Error"},
        {"Customizer1",       "This is not a visual customizer. A customizer is not useful, unless it is visual."},
        {"Customizer2",       "This customizer does not implement the \"java.beans.Customizer\" interface."},
        {"Customizer3",       "Unable to instantiate customizer"},
        {"Message1",          "Unable to instantiate component \""},
        {"Message2",          "Unable to instantiate component."},
        {"Message3",          "No component was selected"},
        {"Message4",          "Only visual components are supported."},
        {"Sounds",            "Sounds"},
        {"Bounds",            "Bounds"},
        {"BoundsTip",         "Enter the bounds of the component"},
        {"Location",          "Location"},
        {"LocationTip",       "Enter the location of the component"},
    };
}
