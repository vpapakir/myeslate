// @created by Drossos Nicolas 1/2K
// Listener for Dynamic Named Structure
package gr.cti.eslate.namedDataSet.event;

import java.util.EventListener;

public interface DataSetChangedListener extends EventListener {
    public abstract void dataSetNameChanged(DataSetEvent e);
    public abstract void dataAdded(DataSetEvent e);
    public abstract void dataChanged(DataSetEvent e);
    public abstract void dataRemoved(DataSetEvent e);
    public abstract void typeChanged(DataSetEvent e);

}






