package gr.cti.eslate.scripting;

import gr.cti.eslate.set.*;

/**
 * This interface describes the functionality of the Set component that
 * is available to the Logo scripting mechanism.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 23-May-2006
 * @see         gr.cti.eslate.set.Set
 */

public interface AsSet
{
  /**
   * Selects the subset at the given coordinates.
   * @param     x       X coordinate of the subset.
   * @param     y       Y coordinate of the subset.
   */
  public void selectSubset(int x, int y);

  /**
   * Selects a subset.
   * @param     a       Subset belongs in the first (top) oval.
   * @param     b       Subset belongs in the second (left) oval.
   * @param     c       Subset belongs in the third (right) oval.
   */
  public void selectSubset(boolean a, boolean b, boolean c);

  /**
   * Clears the selected subset.
   */
  public void clearSelectedSubset();

  /**
   * Returns the description of the selected subset.
   * @return    The requested description.
   */                                     
  public String getSelText();

  /**
   * Deletes the ellipse(s) at the given coordinates.
   * @param     x       X coordinate of the ellipse(s) to delete.
   * @param     y       Y coordinate of the ellipse(s) to delete.
   */
  public void deleteEllipse(int x, int y);

  /**
   * Deletes one or more ellipses.
   * @param     a       Delete the first (top ellipse).
   * @param     b       Delete the second (left) ellipse.
   * @param     c       Delete the third (right) ellipse.
   */
  public void deleteEllipse(boolean a, boolean b, boolean c);

  /**
   * Select the field projected onto the component.
   * @param     name    The name of the field to project.
   * @exception SetException    Thrown if the requested field does not exist.
   */
  public void setProjectionField(String name) throws SetException;

  /**
   * Return the name of the field projected onto the component.
   * @return    The requested name.
   */
  public String getProjectionField();

  /**
   * Return the names of the fields that can be projected onto the component.
   * @return    An array containing the requested names.
   */
  public String[] getProjectionFields();

  /**
   * Select the calculation operation to perform.
   * @param     name    The name of the operation.
   * @exception SetException    Thrown if the requested operation is not
   *                            supported.
   */
  public void setCalcOp(String name) throws SetException;

  /**
   * Return the name of the calculation operation that is being performed.
   * @return    The requested name.
   */
  public String getCalcOp();

  /**
   * Return the names of the calculation operations that can be performed.
   * @return    An array containing the requested names.
   */
  public String[] getCalcOps();

  /**
   * Select the table to display.
   * @param     name    The name of the table.
   * @exception SetException    Thrown if the requested table does not exist.
   */
  public void setSelectedTable(String name) throws SetException;

  /**
   * Return the name of the displayed table.
   * @return    The requested name.
   */
  public String getSelectedTable();

  /**
   * Return the names of the tables that can be displayed.
   * @return    An array containing the requested names.
   */
  public String[] getTables();

  /**
   * Select the field on which calculations will be performed.
   * @param     name    The name of the calculation field.
   * @exception SetException    Thrown if the field does not exist, or the
   *                            currently selected operation cannot be
   *                            performed on this field.
   */
  public void setCalcKey(String name) throws SetException;

  /**
   * Return the name of the field on which calculations are being performed.
   * @return    The requested name.
   */
  public String getCalcKey();

  /**
   * Return the names of the fields on which calculations can be performed.
   * @return    An array containing the requested names.
   */
  public String[] getCalcKeys();

  /**
   * Specify whether the selected projection field should be displayed.
   * @param     status  True if yes, false otherwise.
   */
  public void setProject(boolean status);

  /**
   * Return whether the selected projection field is being displayed.
   * @return    True if yes, false if no.
   */
  public boolean isProjecting();

  /**
   * Specify whether calculations should be displayed.
   * @param     status  True if yes, false otherwise.
   */
  public void setCalculate(boolean status);

  /**
   * Returns whether calculations are displayed.
   * @return    True if yes, false if no.
   */
  public boolean isCalculating();

  /**
   * Creates and activates a new ellipse.
   */
  public void newEllipse();

  /**
   * Activates the ellipse at the given coordinates.
   * @param     x       The x coordinate of the ellipse.
   * @param     y       The y coordinate of the ellipse.
   */
  public void activateEllipse(int x, int y);

  /**
   * Activates an ellipse.
   * @param     n       The ellipse to activate: 0=top, 1=bottom left,
   *                    2=bottom right.
   */
  public void activateEllipse(int n);

  /**
   * Deactivates the active ellipse.
   */
  public void deactivateEllipse();
}
