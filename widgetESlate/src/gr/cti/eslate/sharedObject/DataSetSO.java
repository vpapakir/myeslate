package gr.cti.eslate.sharedObject;

import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.namedDataSet.*;

/**
 * Date shared object.
 *
 * @author      Nicolas Drossos
 * @author      Kriton Kyrimis
 * @version     5.0.0, 19-May-2006
 */
public class DataSetSO extends SharedObject {
    public static final int SET_CHANGED = 0;
    public static final int NAME_CHANGED = 1;
    public static final int DATA_CHANGED = 2;
    public static final int DATA_ADDED = 3;
    public static final int DATA_REMOVED = 4;

    private int eventOccured;

    //DefaultDataSet sharedDataSet;
    INamedDataSet sharedDataSet;

    public DataSetSO(gr.cti.eslate.base.ESlateHandle app) {
        super(app);
    }

    public int getEventOccurence() {
        return eventOccured;
    }

    //public void setDataSet(DefaultDataSet dataSet)
    @SuppressWarnings(value={"deprecation"})
    public void setDataSet(INamedDataSet dataSet)
    {
        if (areDifferent(sharedDataSet, dataSet)) {
            sharedDataSet = dataSet;
            eventOccured = SET_CHANGED;
            SharedObjectEvent soe =
              new SharedObjectEvent(getHandle().getComponent(), this);
            fireSharedObjectChanged(soe);       // Notify the listeners
        }
    }

    //public DefaultDataSet getDataSet() {
    public INamedDataSet getDataSet() {
                return sharedDataSet;
    }
}
