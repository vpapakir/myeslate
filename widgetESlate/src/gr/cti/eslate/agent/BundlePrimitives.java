package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Agent BeanInfo bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class BundlePrimitives extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"setup",           "Loaded Agent primitives"},
        {"notpositioned",   "Agent has not been positioned."},
        {"refusejump",      "Agent refuses to jump to the given location."},
    };
}

