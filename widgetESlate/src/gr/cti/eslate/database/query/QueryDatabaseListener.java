package gr.cti.eslate.database.query;

import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugNotConnectedException;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.database.engine.event.*;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.DBase;
import gr.cti.eslate.database.engine.TableField;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidTitleException;
import gr.cti.eslate.database.query.QueryComponent;


public class QueryDatabaseListener extends DatabaseAdapter {

    private QueryComponent queryComponent;

    public QueryDatabaseListener(QueryComponent query) {
        queryComponent=query;
    }
    /*fixed*/
    public void activeTableChanged(ActiveTableChangedEvent e) {
        int index = e.getToIndex();
        DBase db = (DBase) e.getSource();
        Table ctable = db.getTableAt(index);
        int localIndex = queryComponent.queryViewStructure.getTableIndex(ctable);

        if (localIndex == -1) {
            return;
        }

        if (localIndex == queryComponent.queryViewStructure.activeTableIndex)
            return;

        if (queryComponent.tabs!= null) {
            queryComponent.iterateEvent = false;
            queryComponent.activateTableAt(localIndex);
            queryComponent.iterateEvent = true;
        }
    }

    /* Probably fixed*/
    public void tableAdded(TableAddedEvent e) {
        DBase cdb = (DBase) e.getSource();
        Table newTable = e.getTable();
        newTable.addTableModelListener(queryComponent.tableModelListener);
        newTable.addPropertyChangeListener(queryComponent.tablePcl);
        if (!cdb.isHiddenTablesDisplayed() && newTable.isHidden()) {
            return;
        }

        queryComponent.addTable(newTable, queryComponent.queryViewStructure.size());
    }

    /* Probably fixed */
    public void tableRemoved(TableRemovedEvent e) {
        DBase dBase = (DBase) e.getSource();
//System.out.println("QueryDatabaseListener " + e.getRemovedTable().getTitle() + ", index: " + e.getTableIndex());
        if (e.getTableIndex() == -1) return;
        Table ctable = dBase.getTableAt(e.getTableIndex());
        ctable.removeTableModelListener(queryComponent.tableModelListener);
        ctable.removePropertyChangeListener(queryComponent.tablePcl);
//System.out.println("ctable: " + ctable.getTitle() + ", index2: " + queryComponent.queryViewStructure.getTableIndex(ctable) + ", #tables in view structure: " + queryComponent.queryViewStructure.size());
        int index = queryComponent.queryViewStructure.getTableIndex(ctable);
        if (index == -1) return;

        /* First activate another table, if one exists
         */
        if (queryComponent.queryViewStructure.size() > 1) {
            if (index==0) {
                queryComponent.activateTableAt(0);
            } else {
                queryComponent.activateTableAt(index-1);
            }
        }

        queryComponent.queryViewStructure.removeTable(ctable);
        queryComponent.tabs.removeTabAt(index);
        queryComponent.tabs.validate();
        queryComponent.tabs.repaint();

        if (queryComponent.tabs.getTabCount() == 0) {
            queryComponent.queryViewStructure.activeTableIndex = -1;
            if (queryComponent.emptyDB == null) {
                queryComponent.createEmptyDBPanel();
              //  queryComponent.emptyDB.setBounds(0, 0, queryComponent.getSize().width, queryComponent.getSize().height); //-(menu.getHeight()));
            }

            queryComponent.componentPanel.remove(queryComponent.mainPanel);
            queryComponent.mainPanel = null;
            queryComponent.tabs = null;
            queryComponent.componentPanel.add(queryComponent.emptyDB, BorderLayout.CENTER);
            queryComponent.emptyDBVisible = true;

            queryComponent.execute.setEnabled(false);
            queryComponent.clearQuery.setEnabled(false);

            queryComponent.headerStatusButton.setVisible(false);
            queryComponent.addRow.setVisible(false);
            queryComponent.removeRow.setVisible(false);
            queryComponent.queryPane.setSelected(false);
            queryComponent.queryPane.setEnabled(false);
        } else {
        }
        // Destroy this table's protocol pin.
        queryComponent.destroyTableHandle(ctable.getTitle());// index);

        queryComponent.refreshQueryComponent();

    }

    public void tableReplaced(TableReplacedEvent e) {
    }

    public void databaseRenamed(DatabaseRenamedEvent e) {
        String dbTitle = queryComponent.db.getTitle();
        if (dbTitle == null || dbTitle.length() == 0) {
            dbTitle = queryComponent.infoBundle.getString("QueryComponentMsg16");
        }
    }
}
