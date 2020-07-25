package gr.cti.eslate.database.query;

import java.awt.Component;
import java.util.Vector;
import javax.swing.*;

public class Toolbar extends JPanel {
    Vector tools=new Vector();
    Vector visible=new Vector();

    public Component add(Component c) {
        int i=tools.indexOf(c);
        if (i==-1) {
            super.add(c);
            tools.addElement(c);
            visible.addElement(new Boolean(true));
        } else {
            visible.setElementAt(new Boolean(true),i);
            removeAll();
            for (int k=0;k<tools.size();k++) {
                if (((Boolean)visible.elementAt(k)).booleanValue())
                    super.add((Component)tools.elementAt(k));
            }
            invalidate();
            revalidate();
        }
        return c;
    }

    public void remove(Component c) {
        int i;
        i=tools.indexOf(c);
        visible.setElementAt(new Boolean(false),i);
        removeAll();
        for (int k=0;k<tools.size();k++) {
            if (((Boolean)visible.elementAt(k)).booleanValue())
                super.add((Component)tools.elementAt(k));
        }
        invalidate();
        revalidate();
    }

}

