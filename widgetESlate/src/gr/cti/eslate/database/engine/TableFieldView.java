/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Nov 21, 2002
 * Time: 2:08:00 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database.engine;

public interface TableFieldView {
    /** Returns the AbstractTableField a TableFieldView is based on.
     *
     * @return The base AbstractTableField.
     */
    public AbstractTableField getBaseField();
}
