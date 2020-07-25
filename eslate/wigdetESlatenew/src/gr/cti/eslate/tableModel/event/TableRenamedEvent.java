package gr.cti.eslate.tableModel.event;

import java.util.EventObject;

public class TableRenamedEvent extends EventObject {
//t    protected int index;
    protected String oldTitle;
    protected String newTitle;

//t    public TableRenamedEvent(Object source, int index, String oldTitle, String newTitle) {
    public TableRenamedEvent(Object source, String oldTitle, String newTitle) {
        super(source);
//t        this.index = index;
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
    }

/*t    public int getIndex() {
        return index;
    }
t*/
    public String getOldTitle() {
        return oldTitle;
    }

    public String getNewTitle() {
        return newTitle;
    }

}
