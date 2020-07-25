package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class MicroworldBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"Geography",             "Geography"},
        {"Geometry",              "Geometry"},
        {"History",               "History"},
        {"Mathematics",           "Mathematics"},
        {"Physics",               "Physics"},
        {"CrossSubject",          "Cross - Subject"},
        {"InsufficientPriviledge","Insufficient rights to E-Slate priviledged API"},
        {"Setting",               "Setting"},
        {"BlocksAccess",          "blocks access to the requested functionality"},
    };
}