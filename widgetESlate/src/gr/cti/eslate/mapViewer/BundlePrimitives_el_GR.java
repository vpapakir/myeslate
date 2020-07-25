package gr.cti.eslate.mapViewer;

import java.util.ListResourceBundle;

/**
 * MapViewer primitives bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 7-Dec-2000
 */
public class BundlePrimitives_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"setup",                       "Εντολές Προβολέα Χαρτών αναγνώστηκαν"},
        {"layervisibilityunchanged",    "Η κατάσταση προβολής του επιπέδου δεν άλλαξε. Δεν υπάρχει το επίπεδο ή οι παράμετροι που δώσατε είναι λάθος."},
        {"layerdoesnotexist",           "Το επίπεδο δεν βρέθηκε."},
        {"layerincorrectorder",         "Λάθος σειρά επιπέδου."},
        {"agentdoesnotexist",           "Η οντότητα δεν υπάρχει."},
    };
}
