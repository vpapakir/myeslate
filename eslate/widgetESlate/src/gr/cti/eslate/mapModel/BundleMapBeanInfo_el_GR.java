package gr.cti.eslate.mapModel;

import java.util.ListResourceBundle;

/**
 * MapBeanInfo Bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	3.0.0, 05-Apr-2000
 */
public class BundleMapBeanInfo_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"name",                "Όνομα"},
        {"nametip",             "Το όνομα του χάρτη"},
        {"menubarVisible",      "Μενού επιλογών ορατό"},
        {"menubarVisibletip",   "Ελέγχει αν θα εμφανίζεται το μενού επιλογών"},
        {"border",              "Περίγραμμα"},
        {"bordertip",           "Ελέγχει το περίγραμμα της ψηφίδας"},
        {"root",                "Κορυφαία Περιοχή"},
    };
}
