// @created by Drossos Nicolas 1/2K
// Event for Dynamic Named Structure
// updated 30/10/2000
package gr.cti.eslate.namedDataSet.event;

import java.util.EventObject;

public class DataSetEvent extends EventObject {
    private int index;
    private Class prevType;
    private Class newType;
    private String newName, prevName;
    private Object newValue, prevValue;

    public DataSetEvent(Object source) {
    	super(source);
    }

    /** This constructor is used when a value in the Dataset changes.
     */
    public DataSetEvent(Object source, int index, Object newValue, Object prevValue) {
    	super(source);
        this.index = index;
        this.newValue = newValue;
        this.prevValue = prevValue;
    }

    /** This constructor is used when a value is removed from the Dataset.
     */
    public DataSetEvent(Object source, int index) {
    	super(source);
        this.index = index;
    }

    /** This constructor is used when the Dataset's data type changes.
     */
    public DataSetEvent(Object source, Class newType, Class prevType) {
    	super(source);
        this.newType = newType;
        this.prevType = prevType;
    }

    /** This constructor is used when the Dataset's name changes.
     */
    public DataSetEvent(Object source, String newName, String prevName) {
    	super(source);
        this.newName = newName;
        this.prevName = prevName;
    }

    public int getIndex() {
        return index;
    }

    public String getNewName() {
        return newName;
    }

    public String getPrevName() {
        return prevName;
    }

    public Class getNewType() {
        return newType;
    }

    public Class getPrevType() {
        return prevType;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getPrevValue() {
        return prevValue;
    }
}




