package gr.cti.eslate.tableModel.event;

import java.util.EventObject;
import gr.cti.typeArray.IntBaseArray;

    /** This event carries information about any change that occurs in the selected
     *  record set of a TableModel. When the receiver of a <i>SelectedRecordSetChangedEvent</i>
     *  needs to find out the exact records whose selection status has changed, the following
     *  steps have to be followed for maximum efficiency:
     *  <ul>
     *  <li>First the type of the change has to be checked. If the type of the
     *      change is <i>RECORDS_REMOVED_FROM_SELECTION</i>, then
     *      <i>getRecordsRemovedFromSelection()</i> will return the records that
     *      have been removed. In the case of the Database Engine Table this method
     *      will return null for any other type of record selection change.
     *      If the type of the change is <i>RECORDS_ADDED_TO_SELECTION</i>, then
     *      <i>getRecordsAddedToSelection()</i> will return the records that
     *      have been added to the selected set. This method will return null for
     *      any other type of record selection change in the case of Database Engine
     *      Table.
     *  </li>
     *  <li>The most common type of change is <i>RECORD_SELECTION_CHANGED</i>. This
     *      occurs when records have both been added and removed from the selected
     *      record set. In this case to find out the exact recods whose selection
     *      status has changed the previous selected set has to be acquired using the
     *      <i>getPreviousSelectedSubset()</i>. If the records belong to a Database
     *      Engine Table, then the currently selected record set can be retreived.
     *      Then depending on whether the records that have been added to selection
     *      or removed from selection are needed the method <i>clearArray()</i> of the
     *      Database Engine Table can be used on the previous and the new selected
     *      record set. To get the removed records use <i>clearArray(previousSelectedSet,
     *      newSelectedSet</i>. To get the added records use <i>clearArray(newSelectedSet,
     *      previousSelectedSet</i>.
     *  </li>
     *  </ul>
     */
public class SelectedRecordSetChangedEvent extends EventObject {
    /* Declares that records where removed from the Table's selected recordset */
    public static final int RECORDS_REMOVED_FROM_SELECTION = 0;
    /* Declares that records where added to the Table's selected recordset */
    public static final int RECORDS_ADDED_TO_SELECTION = 1;
    /* Declares that records where both added to and removed from the Table's selected recordset */
    public static final int RECORD_SELECTION_CHANGED = 2;
//t    int tableIndex;
    private String queryString;
    private IntBaseArray recordsRemovedFromSelection, recordsAddedToSelection, previousSelectedSubset;
    private int id = RECORD_SELECTION_CHANGED;

    public SelectedRecordSetChangedEvent(Object source, int id, String queryString,
    IntBaseArray previousSelectedSubset, IntBaseArray recordsAddedToSelection,
    IntBaseArray recordsRemovedFromSelection) {
        super(source);
        if (id != RECORDS_REMOVED_FROM_SELECTION && id != RECORDS_ADDED_TO_SELECTION
        && id != RECORD_SELECTION_CHANGED)
            throw new IllegalArgumentException("Invalid id for SelectedRecordSetChangedEvent");
        this.id = id;
        this.queryString = queryString;
        this.previousSelectedSubset = previousSelectedSubset;
        this.recordsAddedToSelection = recordsAddedToSelection;
        this.recordsRemovedFromSelection = recordsRemovedFromSelection;
    }

    /** The string of the query that caused the change in the record selection. The string
     *  may be null, if the record selection was not caused by a query.
     */
    public String getQueryString() {
        return queryString;
    }

    /** Returns the type of selection that occured in the Table's selected record set.
     *  This should be checked when the listener needs to find out exactly what the
     *  change in the selected record set was. To find out the changed records efficiently
     *  the listener has to first check for the type of the change.
     */
    public int getID() {
        return id;
    }

    /** Returns the records which have been added to the selected record set. This
     *  method should be used only when the change in the selected recordset is of type
     *  <i>RECORDS_ADDED_TO_SELECTION</i>.
     */
    public IntBaseArray getRecordsAddedToSelection() {
        return recordsAddedToSelection;
    }

    /** Returns the records which have been removed to the selected record set. This
     *  method should be used only when the change in the selected recordset is of type
     *  <i>RECORDS_REMOVED_FROM_SELECTION</i>.
     */
    public IntBaseArray getPreviousSelectedSubset() {
        return previousSelectedSubset;
    }

    /** Returns the selected record set before is was changed.
     */
    public IntBaseArray getRecordsRemovedFromSelection() {
        return recordsRemovedFromSelection;
    }
}
