package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Path segment properties dialog bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 26-Apr-2000
 */
public class BundleSegmentProperties extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"title",           "Path segment properties"},
        {"name",            "Name"},
        {"strokeas",        "Stroke the path as:"},
        {"straightline",    "A straight line."},
        {"dottedline",      "A dotted line."},
        {"width",           "of width"},
        {"paintas",         "Paint the path with:"},
        {"solidcolor",      "One, solid color."},
        {"gradientcolor",   "A gradient color."},
        {"define",          "Define"},
        {"gradientstart",   "Start color"},
        {"gradientend",     "End color"},
        {"ok",              "OK"},
        {"cancel",          "Cancel"},
        {"apply",           "Apply"},
        {"transparency",    "Trasparency"},
        {"colors",          "Colors"},
    };
}

