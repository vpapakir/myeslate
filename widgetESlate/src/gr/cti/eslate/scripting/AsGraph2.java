package gr.cti.eslate.scripting;

import java.awt.Color;

import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.CalculatedFieldExistsException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.FieldNameInUseException;
import gr.cti.eslate.database.engine.FieldNotRemovableException;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidFieldTypeException;
import gr.cti.eslate.database.engine.InvalidKeyFieldException;
import gr.cti.eslate.database.engine.NoFieldsInTableException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.TableNotExpandableException;
import gr.cti.typeArray.ArrayBase;
import gr.cti.typeArray.StringBaseArray;


/**
 * This interface describes the functionality of the graph component
 * that is available to the Logo scripting mechanism.
 * 
 * @version     1.0.6, 21-Jan-2008
 * @author      Augustine Gryllakis
 * @author      Kriton Kyrimis
 */
public interface AsGraph2
{
  /**
   * Add a new function.
   * @param expression Function's expression.
   * @return True for adding the function.
   */
  public boolean addFunction(String expression);
  
  /**
   * Remove a function.
   * @param expression Function's expression.
   */
  public void removeFunction(String expression);
  
  /**
   * Set function's domain.
   * @param expression Function's expression.
   * @param domainFrom Domain's start.
   * @param domainTo Domain's end.
   */
  public void setFunctionDomain
    (String expression, double domainFrom, double domainTo);
  
  /**
   * Set function color.
   * @param expression Function's expression.
   * @param color Function's color.
   */
  public void setFunctionColor(String expression, Color color);
  
  /**
   * Zoom.
   * @param zoomIn Zoom in or out.
   */
  public void zoom(boolean zoomIn);
  
  /**
   * Set domain view window.
   * @param from Domain view start.
   * @param to Domain view end.
   */
  public void setView(double from, double to);
  
  /**
   * Plot a point.
   * @param x
   * @param y
   */
  public void plot(double x, double y);
  
  /**
   * Set plot color.
   * @param color
   */
  public void plotColor(Color color);
  
  /**
   * Clear all plotted points.
   */
  public void plotClear();

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
   * Sets the minimum value for the x axis.
   * @param minX    The minimum value for the x axis.
   */
  public void setXAxisMin(double minX);

  /**
   * Sets the maximum value for the x axis.
   * @param minX    The maximum value for the x axis.
   */
  public void setXAxisMax(double minX);

  /**
   * Returns the minimum value for the x axis.
   * @return    The minimum value for the x axis.
   */
  public double getXAxisMin();

  /**
   * Returns the maximum value for the y axis.
   * @return    The maximum value for the y axis.
   */
  public double getXAxisMax();

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
     * Adds a data set (i.e., a field) to the local table.
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
     * @exception   IllegalArgumentException    Thrown if a dataset with
     *                  the given title already exists.
     */
    public void addDataSet(String title, double[] data)
        throws InvalidFieldNameException, InvalidKeyFieldException, InvalidFieldTypeException, AttributeLockedException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, NoFieldsInTableException, TableNotExpandableException;

    /**
     * Updates the data belonging to an existing data set (i.e., field) in
     * the local table.
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
     * @throws IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     */
    public void setDataSet(String title, double[] data)
        throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException, NoFieldsInTableException, TableNotExpandableException;

    /**
     * Sets the value of an element in a given data set (i.e., field) in the
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
     * Adds a new element at the bottom of a given data set (i.e., field)
     * in the local table.
     * @param   title   The title of the data set.
     * @param   value   The value of the element.
     * @throws InvalidFieldNameException 
     * @throws TableNotExpandableException 
     * @throws DuplicateKeyException 
     * @throws NullTableKeyException 
     * @throws NoFieldsInTableException 
     * @throws InvalidDataFormatException 
     * @exception   IllegalArgumentException    Thrown if there is no data
     *                  set having the given name.
     */
    public void addElement(String title, double value)
      throws InvalidFieldNameException, InvalidDataFormatException, NoFieldsInTableException, NullTableKeyException, DuplicateKeyException, TableNotExpandableException;

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
     * and the last element in the data set is set to <code>null</code>.
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
      throws InvalidFieldNameException, InvalidCellAddressException, NullTableKeyException, InvalidDataFormatException, DuplicateKeyException, AttributeLockedException;

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
