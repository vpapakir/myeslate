/**
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: Dec 23, 2002
 * Time: 4:50:03 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TransferableRow implements Transferable {

    public static DataFlavor DBTABLE_ROW_FLAVOR = new DataFlavor(DBTableRow.class,
            "DBTable Row");
    DataFlavor flavors[] = { DBTABLE_ROW_FLAVOR, DataFlavor.getTextPlainUnicodeFlavor() };
//    TreePath treePath;
    DBTableRow row = null;

    public TransferableRow(DBTableRow row) {
        this.row = row;
//        this.treePath = treePath;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i=0; i<flavors.length; i++)
            if (flavors[i].equals(flavor))
                return true;
        return false;
    }

    public synchronized Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
//        if (isDataFlavorSupported(flavor)) {
        if (flavor.equals(DBTABLE_ROW_FLAVOR)) {
            return row; //treePath;
        }else if (flavor.equals(DataFlavor.getTextPlainUnicodeFlavor())) {
            return new String("Row " + row.getRow() + " of Table " + row.getMicroworldPath());
        }else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}

