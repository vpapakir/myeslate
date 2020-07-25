package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Agent plugs bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 15-May-2000
 */
public class BundlePlugs extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"host",                    "Agent"},
        {"vectorin",                "Direction and velocity"},
        {"vectorout",               "Resultant velocity vector"},
        {"direction",               "Direction"},
        {"distance",                "Distance"},
        {"time",                    "Time"},
        {"abslatlong",              "Absolute Lat-Long coordinates"},
        {"layerobjectof",           "Near-by object ID of"},
        {"nearbyrecord",            "Near-by object record"},
        {"clock",                   "Clock tick"},
        {"recordof",                "Descriptive data of near-by object of"},
    };
}
