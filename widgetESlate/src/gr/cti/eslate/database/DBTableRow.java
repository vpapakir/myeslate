/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Dec 23, 2002
 * Time: 5:18:05 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

/** This class was introduced to support row DnD between the DBTable and other components
 *  in a microworld. An instance of this class carries the hierarchical path of the DBTable in
 *  the microworld and the index of the transferred row.
 */
public class DBTableRow {
    private int row = -1;
    private String dbTableMicroworldPath = null;

    public DBTableRow(String dbTableMicroworldPath, int row) {
        if (dbTableMicroworldPath == null)
            throw new IllegalArgumentException("The microworld path of the DBTableRow can not be null");
        this.dbTableMicroworldPath = dbTableMicroworldPath;
        this.row = row;
    }

    /** Returns the index of the transferred row.
     */
    public int getRow() {
        return row;
    }

    /** Returns the hierarchical path of the DBTable in the microworld.
     */
    public String getMicroworldPath() {
        return dbTableMicroworldPath;
    }
}
