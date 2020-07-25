// @created by Drossos Nicolas 1/2K
// Interface for Dynamic Tabular Structure
// Updated 30/10/2000
package gr.cti.eslate.tableModel;

import java.util.ArrayList;
import gr.cti.eslate.tableModel.event.TableModelListener;

public interface ITableModel {
    /** Refers to the type of the data in a column. <i>NO_TYPE</i> is used, when
     *  the data type is unkwon or when the data are of multiple types.
     */
    public final byte NO_TYPE = 0;
    /** Refers to the type of the data in a column. <i>NUMERIC_TYPE</i> refers
     *  to data of any sub-type of the <i>java.lang.Number</i> class.
     */
    public final byte NUMERIC_TYPE = 1;
    /** Refers to the type of the data in a column. <i>ALPHANUMERIC_TYPE</i> refers
     *  to data of class <i>java.lang.String</i>.
     */
    public final byte ALPHANUMERIC_TYPE = 2;
    /** Refers to the type of the data in a column. <i>BOOLEAN_TYPE</i> refers
     *  to data of class <i>java.lang.Boolean</i>.
     */
    public final byte BOOLEAN_TYPE = 3;
    /** Refers to the type of the data in a column. <i>DATE_TYPE</i> refers
     *  to data of any java class which is a subclass of <i>java.util.Date</i>. The
     *  E-Slate Database Engine date data are of class <i>gr.cti.eslate.database.engine.CDate</i>.
     */
    public final byte DATE_TYPE = 4;
    /** Refers to the type of the data in a column. <i>TIME_TYPE</i> refers to
     *  data of class <i>gr.cti.eslate.database.engine.CTime</i>.
     */
    public final byte TIME_TYPE = 5;
    /** Refers to the type of the data in a column. <i>URL_TYPE</i> refers to
     *  data of class <i>java.net.URL</i>.
     */
    public final byte URL_TYPE = 6;
    /** Refers to the type of the data in a column. <i>ICON_TYPE</i> refers to
     *  data of any java class which implements the <i>javax.swing.Icon</i> interface.
     *  The E-Slate Database Engine implementation of the above interface is
     *  the class <i>gr.cti.eslate.database.engine.CImageIcon</i>.
     */
    public final byte ICON_TYPE = 7;
    /** Refers to the type of the data in a column. <i>INTEGER_TYPE</i> refers to
     *  data of class <i>java.lang.Integer</i>.
     */
    public final byte INTEGER_TYPE = 8;
    /** Refers to the type of the data in a column. <i>FLOAT_TYPE</i> refers to
     *  data of class <i>java.lang.Float</i>.
     */
    public final byte FLOAT_TYPE = 9;


    /** Returns the value of the cell at the specified location in the table model.
     */
    public Object getCellValue(int internalRowNumber, int col);

    /** Sets the value of the cell at the specified location in the table model.
     */
    public void setCellValue(int internalRowNumber, int col, Object obj, boolean insert);

    /** Returns the contents of the column at the <i>colIndex</i> position in the table
     *  model.
     */
    public ArrayList getColumn(int colIndex);

    /** Returns the contents of the column named <i>colName</i>.
     */
    public ArrayList getColumn(String colName);

    /** Sets the name of the column positioned at <i>colIndex</i>.
     */
    public void setColumnName(int colIndex, String name);

    /** Returns the name of the specified column
     */
    public String getColumnName(int colIndex);

    /** Returns the number of the columns in the table model.
     */
    public int getColumnCount();

    /** Returns the number of the rows of the table model.
     */
    public int getRowCount();

    /** Returns the type of the data of the specified column. The type of the data should
     *  be of one of the data types specified by ITableModel or any of its descendants.
     */
    public Class getColumnType(int colIndex);

    /** Reports the selection status of the records in the table model.
     */
    public boolean isRowSelected(int internalRowNumber);

    /** Adds a <i>TableModelListener</i> which gets notified whenever changes occur in the
     *  table model.
     */
    public void addTableModelListener(TableModelListener tl);

    /** Removes the specified <i>TableModelListener</i> from the list of the table model's
     *  listeners.
     */
    public void removeTableModelListener(TableModelListener tl);

    /** Returns the <i>ITableModel</i> data type for the specified java class. This method
     *  allows mapping of java data types to <i>ITableModel</i> data types. The returned
     *  type is one of the data types specified by <i>ITableModel</i> or any of its descendants.
     */
    public byte getTypeForClass(Class cls);

    /** Returns the index of the column indicated by <i>colName</i>.
     */
    public int getColumnIndex(String colName);

    /** Returns the activeRow of the  <i>ITableModel</i>. The number returned is the
     *  internal sequential row number.
     */
    public int getActiveRow();

    /** Sets the activeRow of the  <i>ITableModel</i>. The supplied row number is
     *  based on the internal to the <i>ITableModel</i> row sequense.
     */
    public void setActiveRow(int internalRowNumber);

    /** This method is provided for <i>ITableModels</i> which keep both an internal
     *  and an external row structure. The clients of these <i>TableModels</i> can
     *  access its data using either the internal row sequense (which usually remains
     *  unaltered), or the external row sequense, which can be changed by the clients.
     *  <i>getExternalRowNumber()</i> provides the external sequential row number for
     *  the row with internal sequential number 'internalRowNumber'.
     */
    public int getExternalRowNumber(int internalRowNumber);

    /** This method is provided for <i>ITableModels</i> which keep both an internal
     *  and an external row structure. The clients of these <i>TableModels</i> can
     *  access its data using either the internal row sequense (which usually remains
     *  unaltered), or the external row sequense, which can be changed by the clients.
     *  <i>getInternalRowNumber()</i> provides the external sequential row number for
     *  the row with external sequential number 'externalRowNumber'.
     */
    public int getInternalRowNumber(int externalRowNumber);

}