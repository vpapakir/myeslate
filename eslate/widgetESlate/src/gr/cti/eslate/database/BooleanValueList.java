package gr.cti.eslate.database;


import java.util.ListResourceBundle;

public class BooleanValueList extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"true",      "True"},
        {"false",     "False"}
    };
}
