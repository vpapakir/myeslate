/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 6 Íןו 2002
 * Time: 4:07:40 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database.engine;

public class CellAddress {
    public int fieldIndex = -1;
    public int recordIndex = -1;

    public CellAddress() {
    }

    public CellAddress(int recordIndex, int fieldIndex) {
        this.fieldIndex = fieldIndex;
        this.recordIndex = recordIndex;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer("Cell at record: ");
        buff.append(recordIndex);
        buff.append(", field: ");
        buff.append(fieldIndex);
        return buff.toString();
    }
}
