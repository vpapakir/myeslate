package gr.cti.eslate.database.query;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;

/* This is the class of the panel which holds the QBE view of each table in
 * the QueryComponent.
 */
public class QTablePanel extends JPanel {
    private JTable qTable;

    public QTablePanel(JTable qTable) {
        super();
        this.qTable = qTable;
        JScrollPane scrollpane = new JScrollPane(qTable);
        setLayout(new BorderLayout());
        add(scrollpane, BorderLayout.CENTER);
    }

    public JTable getQTable() {
        return qTable;
    }

    public QTableModel getQTableModel(){
        return (QTableModel) qTable.getModel();
    }
}