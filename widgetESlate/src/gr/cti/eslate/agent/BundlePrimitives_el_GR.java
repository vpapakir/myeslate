package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Agent BeanInfo bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 09-May-2000
 */
public class BundlePrimitives_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"setup",           "Εντολές Οντότητας αναγνώστηκαν"},
        {"notpositioned",   "Η οντότητα δεν έχει τοποθετηθεί."},
        {"refusejump",      "Η οντότητα αρνείται να μεταφερθεί στο δοθέν σημείο."},
    };
}
