package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Agent plugs bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 15-May-2000
 */
public class BundlePlugs_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"host",                    "Οντότητα"},
        {"vectorin",                "Κατεύθυνση και ταχύτητα"},
        {"vectorout",               "Συνισταμένη ταχύτητα"},
        {"direction",               "Κατεύθυνση"},
        {"distance",                "Απόσταση"},
        {"time",                    "Χρόνος"},
        {"abslatlong",              "Απόλυτο Γεωγραφικό Μήκος/Πλάτος"},
        {"layerobjectof",           "Α/Α κοντινού αντικειμένου του"},
        {"nearbyrecord",            "Εγγραφή κοντινού αντικειμένου"},
        {"clock",                   "Παλμός χρονιστή"},
        {"recordof",                "Περιγραφικά δεδομένα κοντινού αντικειμένου του"},
    };
}
