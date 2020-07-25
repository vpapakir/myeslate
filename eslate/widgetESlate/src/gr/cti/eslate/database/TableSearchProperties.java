/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 6 Íןו 2002
 * Time: 1:13:53 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

/** Structure which stores the preferences for search operations in a DBTable.
 *  @see   DBTable#find(String, TableSearchProperties, int, int, boolean)
 */
public class TableSearchProperties {
    /** Determines if the search will be case sensitive. The default value is false.*/
    public boolean ignoreCase = true;
    /** Determines if the search will be restricted to the selected fields of the DBTable. The default value is false.*/
    public boolean selectedFieldsOnly = false;
    /** Determines if the search will be restricted to the selected records of the DBTable. The default value is false.*/
    public boolean selectedRecordsOnly = false;
    /** Determines the direction of the search. The default value is false, which is downwards.*/
    public boolean upwards = false;
}
