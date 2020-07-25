package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Messages bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 17-May-2000
 */
public class BundleMessages_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"componentname",           "Οντότητα"},
        {"isEmbarked1",             "<html><body><p><font face=Arial >Η οντότητα </font><font face=Arial color=#00FF00><strong>"},
        {"isEmbarked2",             "</strong></font></p><p><font face=Arial>έχει επιβίβαστεί στην οντότητα</font></p><p><font face=Arial color=#FF8000><strong>"},
        {"isEmbarked3",             "</strong></font><font face=Arial> και δεν μπορεί να</font></p><p><font face=Arial>δεχθεί εντολές. Κινείστε την οντότητα</font></p><p><font face=Arial color=#FF8000><strong>"},
        {"isEmbarked4",             "</strong></font><font face=Arial> για να μετακινήσετε</font></p><p><font face=Arial>και την οντότητα <strong><font face=Arial color=#00FF00>"},
        {"isEmbarked5",             "</strong></font><font face=Arial>.</font></p></body></html>"},
        {"cannotembark",            "Δεν είναι δυνατή η επιβίβαση!"},
        {"cannotdisembark",         "Δεν είναι δυνατή η αποβίβαση!"},
        {"notpositioned",           "Μη τοποθετημένη"},
    };
}
