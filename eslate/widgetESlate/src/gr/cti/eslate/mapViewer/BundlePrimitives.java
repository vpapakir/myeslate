package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

/**
 * MapViewer primitives bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 7-Dec-2000
 */
public class BundlePrimitives extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"setup",                       "Loaded Map Browser primitives"},
        {"layervisibilityunchanged",    "Layer visibility not changed. Layer does not exist or parameters are incorrect."},
        {"layerdoesnotexist",           "Couldn't find layer."},
        {"layerincorrectorder",         "Incorrect layer order."},
        {"agentdoesnotexist",           "Agent does not exist."},
    };
}
