package gr.cti.eslate.database.query;

import java.util.ArrayList;
import javax.swing.JTable;
import gr.cti.typeArray.StringBaseArray;
import gr.cti.eslate.database.engine.Table;

/* This class holds the information regarding the views of all the
 * tables which exist in the database imported in the QueryComponent.
 */
public class QueryViewStructure {
    private StringBaseArray viewModes = new StringBaseArray();
    private QTablePanel[] qTablePanels = new QTablePanel[0];
    int activeTableIndex = -1;
    private ArrayList tables = new ArrayList();
    private ArrayList queryTextViewInfos = new ArrayList();

    public QueryViewStructure() {
    }

    public void addTable(Table table, String mode, QTablePanel panel, QueryTextViewInfo info) {
        tables.add(table);
        viewModes.add(mode);
        queryTextViewInfos.add(info);
        QTablePanel[] tmp = qTablePanels;
        qTablePanels = new QTablePanel[tmp.length+1];
        for (int i=0; i<qTablePanels.length-1; i++)
            qTablePanels[i] = tmp[i];
        qTablePanels[qTablePanels.length-1] = panel;
    }

    public void insertTable(Table table, String mode, QTablePanel panel, QueryTextViewInfo info, int pos) {
        tables.add(pos, table);
        viewModes.add(pos, mode);
        queryTextViewInfos.add(pos, info);
        QTablePanel[] tmp = qTablePanels;
        qTablePanels = new QTablePanel[tmp.length+1];
        for (int i=0; i<pos; i++)
            qTablePanels[i] = tmp[i];
        qTablePanels[pos] = panel;
        for (int i=pos+1; i<qTablePanels.length; i++)
            qTablePanels[i] = tmp[i-1];
    }

    public void removeTable(Table table) {
        int index = tables.indexOf(table);
        if (index != -1) {
            tables.remove(index);
            viewModes.remove(index);
            queryTextViewInfos.remove(index);
            QTablePanel[] tmp = qTablePanels;
            qTablePanels = new QTablePanel[tmp.length-1];
            for (int k=0; k<index; k++)
                qTablePanels[k] = tmp[k];
            for (int k=index; k<qTablePanels.length; k++)
                qTablePanels[k] = tmp[k+1];
        }
    }

    public String getViewMode(Table table) {
        int index = tables.indexOf(table);
        if (index != -1)
            return viewModes.get(index);
        return null;
    }

    /* Returns the view mode of the given table (QBE or TEXT_QUERY)
     */
    public String getViewMode(int index) {
        return viewModes.get(index);
    }

    /* Reports if the view mode is QBE for the given table.
     */
    public boolean isQBEModeActive(Table table) {
        String mode = getViewMode(table);
        return (mode == QueryComponent.QBE);
    }

    /* Reports if the view mode is QBE for the table at the gived index.
     */
    public boolean isQBEModeActive(int index) {
        String mode = getViewMode(index);
        return (mode == QueryComponent.QBE);
    }

    /* Returns whether the view mode is QBE or not for the active table.
     */
    public boolean isQBEModeActive() {
        if (activeTableIndex == -1) return false;
        String mode = getViewMode(activeTableIndex);
        return (mode == QueryComponent.QBE);
    }

    public void inverseViewMode(int index) {
        if (index >=0 && index < tables.size()) {
            if (viewModes.get(index) == QueryComponent.QBE)
                viewModes.set(index, QueryComponent.TEXT_QUERY);
            else
                viewModes.set(index, QueryComponent.QBE);
        }
    }

    public QTablePanel getQTablePanel(Table table) {
        int index = tables.indexOf(table);
        if (index != -1)
            return qTablePanels[index];
        return null;
    }

    public QTablePanel getQTablePanel(int index) {
        return qTablePanels[index];
    }

    public QueryTextViewInfo getTextViewInfo(Table table) {
        int index = tables.indexOf(table);
        if (index != -1)
            return (QueryTextViewInfo) queryTextViewInfos.get(index);
        return null;
    }

    public QueryTextViewInfo getTextViewInfo(int index) {
        return (QueryTextViewInfo) queryTextViewInfos.get(index);
    }

    public int getTableIndex(Table table) {
        for (int i = 0; i < tables.size(); i++) {
            Table table1 = (Table) tables.get(i);
        }
        return tables.indexOf(table);
    }

    public Table getActiveTable() {
        if (activeTableIndex >= 0 && activeTableIndex < tables.size())
            return (Table) tables.get(activeTableIndex);
        return null;
    }

    public Table getTable(int index) {
        return (Table) tables.get(index);
    }

    public void clear() {
        tables.clear();
        qTablePanels = new QTablePanel[0];
        viewModes.clear();
        queryTextViewInfos.clear();
        activeTableIndex = -1;
    }

    public int size() {
        return tables.size();
    }
}