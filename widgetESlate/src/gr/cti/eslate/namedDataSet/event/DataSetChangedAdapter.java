// @created by Drossos Nicolas 1/2K
// Listener for Dynamic Named Structure
package gr.cti.eslate.namedDataSet.event;

import java.util.EventListener;

public class DataSetChangedAdapter implements DataSetChangedListener {
    public void dataSetNameChanged(DataSetEvent e) {}
    public void dataAdded(DataSetEvent e) {}
    public void dataChanged(DataSetEvent e) {}
    public void dataRemoved(DataSetEvent e) {}
    public void typeChanged(DataSetEvent e) {}
}
