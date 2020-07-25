package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Tooltips bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 31-May-2000
 */
public class BundleTooltips extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"path",                "Make/Don't make trace"},
        {"locate",              "Locate the agent on all its hosts"},
    };
}
