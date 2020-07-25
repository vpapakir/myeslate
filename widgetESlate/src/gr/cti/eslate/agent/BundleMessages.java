package gr.cti.eslate.agent;

import java.util.ListResourceBundle;

/**
 * Messages bundle.
 * <P>
 *
 * @author	Giorgos Vasiliou
 * @version	1.0.0, 17-May-2000
 */
public class BundleMessages extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
        {"componentname",           "Agent"},
        {"isEmbarked1",             "<html><body><p><font face=Arial >Agent </font><font face=Arial color=#00FF00><strong>"},
        {"isEmbarked2",             "</strong></font></p><p><font face=Arial>is embarked on agent </font></p><p><font face=Arial color=#FF8000><strong>"},
        {"isEmbarked3",             "</strong></font><font face=Arial>, so it cannot</font></p><p><font face=Arial>accept commands. Move agent</font></p><p><font face=Arial color=#FF8000><strong>"},
        {"isEmbarked4",             "</strong></font><font face=Arial> to move</font></p><p><font face=Arial>agent <strong><font face=Arial color=#00FF00>"},
        {"isEmbarked5",             "</strong></font><font face=Arial> as well.</font></p></body></html>"},
        {"cannotembark",            "Cannot embark!"},
        {"cannotdisembark",         "Cannot disembark!"},
        {"notpositioned",           "Not positioned"},
    };
}
