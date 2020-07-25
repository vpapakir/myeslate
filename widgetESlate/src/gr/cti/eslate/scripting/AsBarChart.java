package gr.cti.eslate.scripting;

import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.CalculatedFieldExistsException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.FieldNameInUseException;
import gr.cti.eslate.database.engine.FieldNotRemovableException;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidFieldTypeException;
import gr.cti.eslate.database.engine.InvalidKeyFieldException;
import gr.cti.eslate.database.engine.NoFieldsInTableException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.TableNotExpandableException;
import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.StringBaseArray;

/**
 * This interface describes the functionality of the bar chart component
 * that is available to the Logo scripting mechanism.
 * 
 * @version     1.0.6, 17-Jan-2008
 * @author      Kriton Kyrimis
 */
public interface AsBarChart
{
  /**
   * Zoom.
   * @param zoomIn Zoom in or out.
   */
  public void zoom(boolean zoomIn);

  /**
   * Selects the table field to plot in the x axis
   * @param       field   The name of the y axis field.
   * @return      True, if the field was successfully selected, false
   *              otherwise.
   */
  public boolean setXAxisField(String field);

  /**
   * Selects the table field to plot in the y axis.
   * @return      True, if the field was successfully selected, false
   *              otherwise.
   * @param       field   The name of the y axis field.
   */
  public boolean setYAxisField(String field);

  /**
   * Returns the name of the table field that is plotted in the x axis.
   * @return    The name of the table field that is plotted in the x axis.
   */
  public String getXAxisField();

  /**
   * Returns the name of the table field that is plotted in the y axis.
   * @return    The name of the table field that is plotted in the y axis.
   */
  public String getYAxisField();

    /**
   * Returns the minimum value for the y axis.
   * @return    The minimum value for the y axis.
   */
  public double getYAxisMin();

  /**
   * Returns the maximum value for the y axis.
   * @return    The maximum value for the y axis.
   */
  public double getYAxisMax();

  /**
   * Adds a data set (i.e., a field) containing numbers to the local table.
   * @param   title   The name of the data set.
   * @param   data    The data to add.
   * @throws AttributeLockedException 
   * @throws InvalidFieldTypeException 
   * @throws InvalidKeyFieldException 
   * @throws InvalidFieldNameException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   * @throws TableNotExpandableException 
   * @throws NoFieldsInTableException 
   * @throws InvalidFieldIndexException 
   * @exception   IllegalArgumentException    Thrown if a dataset with
   *                  the given title already exists.
   */
  public void addDataSet(String title, double[] data)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException;

  /**
   * Adds a data set (i.e., a field) containing strings to the local table.
   * @param   title   The name of the data set.
   * @param   data    The data to add.
   * @throws AttributeLockedException 
   * @throws InvalidFieldTypeException 
   * @throws InvalidKeyFieldException 
   * @throws InvalidFieldNameException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   * @throws TableNotExpandableException 
   * @throws NoFieldsInTableException 
   * @throws InvalidFieldIndexException 
   * @exception   IllegalArgumentException    Thrown if a dataset with
   *                  the given title already exists.
   */
  public void addDataSet(String title, String[] data)
    throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException;

  /**
   * Updates the data belonging to an existing number data set (i.e., field)
   * in the local table.
   * @param   title   The name of the data set.
   * @param   data    The new data for the data set.
   * @throws InvalidFieldNameException 
   * @throws InvalidFieldNameException 
   * @throws TableNotExpandableException 
   * @throws NoFieldsInTableException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   * @throws InvalidFieldIndexException 
   * @throws IllegalArgumentException    Thrown if there is no data
   *                  set having the given name.
   */
  public void setDataSet(String title, double[] data)
    throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException;

  /**
   * Updates the data belonging to an existing strinig data set (i.e., field)
   * in the local table.
   * @param   title   The name of the data set.
   * @param   data    The new data for the data set.
   * @throws InvalidFieldNameException 
   * @throws InvalidFieldNameException 
   * @throws TableNotExpandableException 
   * @throws NoFieldsInTableException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   * @throws InvalidFieldIndexException 
   * @throws IllegalArgumentException    Thrown if there is no data
   *                  set having the given name.
   */
  public void setDataSet(String title, String[] data)
    throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException, InvalidFieldIndexException;

  /**
   * Sets the value of a numeric element in a given data set (i.e., field)
   * in the
   * local table.
   * @param   title   The title of the data set.
   * @param   pos     The position of the element in the data set.
   * @param   value   The value of the element.
   * @throws  IllegalArgumentException    Thrown if there is no data
   *                  set having the given name.
   * @throws InvalidFieldNameException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   */
  public void setElement(String title, int pos, double value)
    throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException;

  /**
   * Sets the value of a string element in a given data set (i.e., field)
   * in the
   * local table.
   * @param   title   The title of the data set.
   * @param   pos     The position of the element in the data set.
   * @param   value   The value of the element.
   * @throws  IllegalArgumentException    Thrown if there is no data
   *                  set having the given name.
   * @throws InvalidFieldNameException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   */
  public void setElement(String title, int pos, String value)
    throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException;

  /**
   * Returns the type of a data set (i.e., field).
   * @param   title   The title of the data set.
   * @return  One of <code>NUMBER</code>, <code>STRING</code>.
   * @throws InvalidFieldNameException 
   * @throws InvalidFieldIndexException 
   */
  public int getDataSetType(String title) throws InvalidFieldNameException, InvalidFieldIndexException;

  /**
   * Adds a new numeric element at the bottom of a given data set
   * (i.e., field) in the local table.
   * @param   title   The title of the data set.
   * @param   value   The value of the element.
   * @throws InvalidFieldNameException 
   * @throws TableNotExpandableException 
   * @throws DuplicateKeyException 
   * @throws NullTableKeyException 
   * @throws NoFieldsInTableException 
   * @throws InvalidDataFormatException 
   * @throws InvalidFieldIndexException 
   * @exception   IllegalArgumentException    Thrown if there is no data
   *                  set having the given name.
   */
  public void addElement(String title, double value)
    throws InvalidFieldNameException, InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException, InvalidFieldIndexException;

  /**
   * Adds a new string element at the bottom of a given data set
   * (i.e., field) in the local table.
   * @param   title   The title of the data set.
   * @param   value   The value of the element.
   * @throws InvalidFieldNameException 
   * @throws TableNotExpandableException 
   * @throws DuplicateKeyException 
   * @throws NullTableKeyException 
   * @throws NoFieldsInTableException 
   * @throws InvalidDataFormatException 
   * @throws InvalidFieldIndexException 
   * @exception   IllegalArgumentException    Thrown if there is no data
   *                  set having the given name.
   */
  public void addElement(String title, String value)
    throws InvalidFieldNameException, InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException, InvalidFieldIndexException;

  /**
   * Removes a data set (i.e., field) from the local table.
   * @param   title   The name of the data set.
   * @throws AttributeLockedException 
   * @throws FieldNotRemovableException 
   * @throws CalculatedFieldExistsException 
   * @throws TableNotExpandableException 
   * @throws InvalidFieldNameException 
   * @throws InvalidFieldNameException 
   */
  public void removeDataSet(String title)
    throws InvalidFieldNameException, TableNotExpandableException, CalculatedFieldExistsException, FieldNotRemovableException, AttributeLockedException;

  /**
   * Removes an element from the given data set (i.e., field) in the local
   * table.
   * Elements below the given element are moved upwards by one position,
   * and the last element in the data set is set to 0.
   * @param   title   The title of the data set.
   * @param   pos     The position of the element in the data set.
   * @throws InvalidFieldNameException 
   * @throws AttributeLockedException 
   * @throws DuplicateKeyException 
   * @throws InvalidDataFormatException 
   * @throws NullTableKeyException 
   * @throws InvalidCellAddressException 
   */
  public void removeElement(String title, int pos)
    throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, InvalidFieldIndexException;

  /**
   * Returns an element in a given data set (i.e., field) in the local
   * table.
   * @param   title   The title of the data set.
   * @param   pos     The position of the element in the data set.
   * @return  The requested element.
   * @throws InvalidFieldNameException 
   * @throws InvalidCellAddressException 
   */
  public Object getElement(String title, int pos)
    throws InvalidFieldNameException, InvalidCellAddressException;

  /**
   * Returns the data in a given data set (i.e., field) in the local table.
   * @param   title   The title of the data set.
   * @return  An list containing the data in the specified data set.
   * @throws InvalidFieldNameException 
   */
  public ArrayBase getDataSet(String title)
    throws InvalidFieldNameException;

  /**
   * Returns the titles of the data sets (i.e., fields) in the local table.
   * @return  A list containing the titles of the data sets in the local
   *          table.
   */
  public StringBaseArray getDataSetTitles();

  /**
   * Changes the title of a data set (i.e., field) in the local table.
   * @param   oldTitle    The old title of the data set.
   * @param   newTitle    The new title of the data set.
   * @throws FieldNameInUseException 
   * @throws InvalidFieldNameException 
   */
  public void setDataSetTitle(String oldTitle, String newTitle)
    throws InvalidFieldNameException, FieldNameInUseException;
}
