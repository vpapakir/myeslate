package gr.cti.eslate.database.query;

import java.util.ArrayList;
import gr.cti.eslate.database.engine.*;

/* This class maps the tables which are created as the results of queries and
 * exported by the table plugs of the Query component, to the tables from which
 * they were generated, i.e. the tables the queries were executed on. For each
 * source table, only 1 result table is created.
 */
public class QueryResultStructure {
    private ArrayList sourceTables = new ArrayList();
    private ArrayList resultTables = new ArrayList();

    public QueryResultStructure() {
    }

    public void addResultTable(Table sourceTable, TableView resultTable) {
        sourceTables.add(sourceTable);
        resultTables.add(resultTable);
    }

    public void removeResultTable(TableView resultTable) {
        int index = resultTables.indexOf(resultTable);
        if (index != -1) {
            resultTables.remove(index);
            sourceTables.remove(index);
        }
    }

    public void removeSourceTable(Table sourceTable) {
        int index = sourceTables.indexOf(sourceTable);
        if (index != -1) {
            resultTables.remove(index);
            sourceTables.remove(index);
        }
    }

    public void removeSourceTable(String sourceTableName) {
        for (int i=0; i<sourceTables.size(); i++) {
            if ( ((Table) sourceTables.get(i)).getTitle().equals(sourceTableName)) {
                sourceTables.remove(i);
                resultTables.remove(i);
                i--;
            }
        }
    }

    public Table getSourceTable(TableView resultTable) {
        int index = resultTables.indexOf(resultTable);
        if (index != -1)
            return (Table) sourceTables.get(index);
        return null;
    }

    public TableView getResultTable(Table sourceTable) {
        for (int i=0; i<sourceTables.size(); i++) {
            if (sourceTables.get(i) == sourceTable)
                return (TableView) resultTables.get(i);
        }
        return null;
    }

/*    public void updateResultTableOfSourceTable(Table sourceTable,
                                               Table newResultTable) {
        for (int i=0; i<sourceTables.size(); i++) {
            if (sourceTables.get(i) == sourceTable)
                ((Table) resultTables.get(i)).setTable(newResultTable);
        }
    }
*/
}