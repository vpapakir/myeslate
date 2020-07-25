package gr.cti.eslate.tableInspector;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JPanel;

public class MyJPanel extends JPanel {
    /**
     * Known method.
     */
    public void setFont(Font f) {
        super.setFont(f);
        for (int i=0;i<getComponentCount();i++)
            ((Component) getComponents()[i]).setFont(f);
    }
}

