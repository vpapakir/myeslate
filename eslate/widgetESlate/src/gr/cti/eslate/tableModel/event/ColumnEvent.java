package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: yiorgos
 * Date: 30 Ιουλ 2003
 * Time: 5:47:30 μμ
 * To change this template use Options | File Templates.
 */
public class ColumnEvent extends EventObject {
    private String columnName = null;

    public ColumnEvent(Object source, String columnName) {
        super(source);
        this.columnName = columnName;
    }

    /** Returns the name of the numeric field whose currency status or currency changed.
     */
    public String getColumnName() {
        return columnName;
    }
}
