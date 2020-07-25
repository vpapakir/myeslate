package gr.cti.eslate.tableInspector;

import java.awt.BorderLayout;

import javax.swing.JApplet;

public class TableInspectorApplet extends JApplet {
    public TableInspectorApplet() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new TableInspector(),BorderLayout.CENTER);
    }
}
