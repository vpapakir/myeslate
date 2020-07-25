package gr.cti.eslate.tableInspector;

import java.util.ListResourceBundle;

/**
 * TableInspector primitives bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 29-Nov-2000
 */
public class BundlePrimitives extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"setup",           "Loaded Record Browser primitives"},
    };
}
