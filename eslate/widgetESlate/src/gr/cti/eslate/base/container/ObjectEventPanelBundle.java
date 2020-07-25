package gr.cti.eslate.base.container;

import java.util.ListResourceBundle;


public class ObjectEventPanelBundle extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"DialogTitle",     "Events"},
        {"Edit",            "Define/Edit script"},
        {"CompileFailureMessage", "Compilation resulted to unexpected errors. Probably the classpath is not set correctly"},
        {"CompileFailureMessage2","Unable to compile. Cannot locate the \"jikes\" compiler"},
        {"Close",           "Close"},
        {"Error",           "Error"},
        {"NoComponent",     "No active component"},
    };
}
