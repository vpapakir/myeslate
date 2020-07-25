/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 6:21:16 לל
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.*;
import javax.swing.table.TableColumn;
import javax.swing.*;

import gr.cti.eslate.database.engine.TableField;

public class TableAddFieldAction extends AbstractAction {
    DBTable dbTable;

    public TableAddFieldAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }

    public void actionPerformed(ActionEvent e) {
System.out.println("TableAddFieldAction");
        execute(Database.NOVICE_USER_MODE);
    }

    protected void execute(int mode) {
        dbTable.tipManager.resetTip();
        dbTable.closeDatabasePopupMenu();
        dbTable.stopCellEditing();

        if (!dbTable.table.isFieldAdditionAllowed())
            return;

        if (mode == Database.ADVANCED_USER_MODE) {
    	    //NewFieldDialog fldDialog = new NewFieldDialog(new JFrame(), activeDBTable, false);
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
            NewFieldDialog fldDialog = new NewFieldDialog(topLevelFrame, dbTable, false, dbTable.bundle.getString("NewFieldDialogMsg1"));
            fldDialog.showDialog();

//            if (fldDialog.clickedButton == 0)
            if (fldDialog.getReturnValue() == fldDialog.DIALOG_CANCEL)
                return;

            dbTable.addColumn((String) fldDialog.outputValues.at(0),
                                    ((Boolean) fldDialog.outputValues.at(1)).booleanValue(),
                                    ((Boolean) fldDialog.outputValues.at(2)).booleanValue(),
                                    /*TableField.getInternalDataTypeName(*/fldDialog.getFieldType(),
//                                    (String) fldDialog.outputValues.at(3),
                                    ((Boolean) fldDialog.outputValues.at(4)).booleanValue(),
                                    ((Boolean) fldDialog.outputValues.at(5)).booleanValue(),
                                    (String) fldDialog.outputValues.at(6));
        }else{
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
    	    NewFieldDialogNov fldDialog = new NewFieldDialogNov(topLevelFrame, dbTable, false, dbTable.bundle.getString("NewFieldDialogMsg1"));
            fldDialog.showDialog();

//            if (fldDialog.clickedButton == 0)
            if (fldDialog.getReturnValue() == fldDialog.DIALOG_CANCEL)
                return;

            dbTable.addColumn((String) fldDialog.outputValues.at(0),
                                    ((Boolean) fldDialog.outputValues.at(1)).booleanValue(),
                                    ((Boolean) fldDialog.outputValues.at(2)).booleanValue(),
//                                    (String) fldDialog.outputValues.at(3),
                                    /*TableField.getInternalDataTypeName(*/fldDialog.getFieldType(),
                                    ((Boolean) fldDialog.outputValues.at(4)).booleanValue(),
                                    ((Boolean) fldDialog.outputValues.at(5)).booleanValue(),
                                    (String) fldDialog.outputValues.at(6));
        }
    }

}
