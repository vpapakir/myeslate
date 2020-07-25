package gr.cti.eslate.tableModel.event;

/**
 * Title:        TableData structure
 * Description:  Your description
 * Copyright:    Copyright (c) 1999
 * Company:      CTI
 * @author Drossos Nicolas
 * @version
 */
import java.util.EventListener;

public interface UnstructuredTableModelListener extends TableModelListener{

    public abstract void cellAdded(CellAddedEvent e);
    public abstract void cellRemoved(CellRemovedEvent e);

}






