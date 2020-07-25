/*
 * Created by IntelliJ IDEA.
 * User: tsironis
 * Date: 7 Íןו 2002
 * Time: 5:44:04 לל
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

import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateOptionPane;

public class TableFieldPropertiesAction extends AbstractAction {
    DBTable dbTable;

    public TableFieldPropertiesAction(DBTable dbTable, String name){
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK, false));
        this.dbTable = dbTable;
    }


    public void actionPerformed(ActionEvent e) {
        execute(Database.NOVICE_USER_MODE);
    }

    protected void execute(int mode) {
        dbTable.closeDatabasePopupMenu();
        dbTable.stopCellEditing();

        int columnIndex = dbTable.jTable.getSelectedColumn();
        if (columnIndex == -1) return;

        if (!dbTable.table.isFieldPropertyEditingAllowed())
            return;

        int fieldIndex = dbTable.jTable.convertColumnIndexToModel(columnIndex);
        AbstractTableField f = dbTable.table.getFields().get(fieldIndex);

        String name, dataType, formula;
        Class fldType;
        boolean key, calculated, editable, removable, externalData;

        if (mode == Database.ADVANCED_USER_MODE) {
            NewFieldDialog.inputValues.clear();
            NewFieldDialog.inputValues.add(f.getName());
            NewFieldDialog.inputValues.add(new Boolean(dbTable.table.isPartOfTableKey(f)/*f.isKey()*/));
            NewFieldDialog.inputValues.add(new Boolean(f.isCalculated()));
            String type = f.getDataType().getName().substring(f.getDataType().getName().lastIndexOf('.')+1, f.getDataType().getName().length());
/*            if (type.equals("Date") && !f.isDate())
                type = "Time";
            if (type.equals("CImageIcon"))
                type = "Image";
*/
            NewFieldDialog.inputValues.add(type);
            NewFieldDialog.inputValues.add(new Boolean(f.isEditable()));
            NewFieldDialog.inputValues.add(new Boolean(f.isRemovable()));
            NewFieldDialog.inputValues.add(f.getTextFormula());
            NewFieldDialog.inputValues.add(new Boolean(f.containsLinksToExternalData()));
            //NewFieldDialog fldDialog = new NewFieldDialog(fr, activeDBTable, true);
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
            NewFieldDialog fldDialog = new NewFieldDialog(topLevelFrame, dbTable, true, dbTable.bundle.getString("EditFieldDialogMsg"));
            fldDialog.setFieldType(f.getDataType());
            fldDialog.showDialog();

//	    if (NewFieldDialog.clickedButton != 1)
            if (fldDialog.getReturnValue() != fldDialog.DIALOG_OK)
                return;

            name = (String) NewFieldDialog.outputValues.at(0);
            key = ((Boolean) NewFieldDialog.outputValues.at(1)).booleanValue();
            calculated = ((Boolean) NewFieldDialog.outputValues.at(2)).booleanValue();
//	    dataType = (String) NewFieldDialog.outputValues.at(3);
            fldType = fldDialog.getFieldType();
            editable = ((Boolean) NewFieldDialog.outputValues.at(4)).booleanValue();
            removable = ((Boolean) NewFieldDialog.outputValues.at(5)).booleanValue();
            formula = (String) NewFieldDialog.outputValues.at(6);
            externalData = ((Boolean) NewFieldDialog.outputValues.at(7)).booleanValue();
        }else{
            NewFieldDialogNov.inputValues.clear();
            NewFieldDialogNov.inputValues.add(f.getName());
            NewFieldDialogNov.inputValues.add(new Boolean(dbTable.table.isPartOfTableKey(f)/*f.isKey()*/));
            NewFieldDialogNov.inputValues.add(new Boolean(f.isCalculated()));
            String type = f.getDataType().getName().substring(f.getDataType().getName().lastIndexOf('.')+1, f.getDataType().getName().length());
//            if (type.equals("Date") && !f.isDate())
//                type = "Time";
            if (type.equals("CImageIcon"))
                type = "Image";

            NewFieldDialogNov.inputValues.add(type);
            NewFieldDialogNov.inputValues.add(new Boolean(f.isEditable()));
            NewFieldDialogNov.inputValues.add(new Boolean(f.isRemovable()));
            NewFieldDialogNov.inputValues.add(f.getTextFormula());
            NewFieldDialogNov.inputValues.add(new Boolean(f.containsLinksToExternalData()));
            Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, dbTable);
            NewFieldDialogNov fldDialog = new NewFieldDialogNov(topLevelFrame, dbTable, true, dbTable.bundle.getString("EditFieldDialogMsg"));
            fldDialog.setFieldType(f.getDataType());
            fldDialog.showDialog();
//	    if (NewFieldDialogNov.clickedButton != 1)
            if (fldDialog.getReturnValue() != fldDialog.DIALOG_OK)
                return;

            name = (String) NewFieldDialogNov.outputValues.at(0);
            key = ((Boolean) NewFieldDialogNov.outputValues.at(1)).booleanValue();
            calculated = ((Boolean) NewFieldDialogNov.outputValues.at(2)).booleanValue();
//	    dataType = (String) NewFieldDialogNov.outputValues.at(3);
            fldType = fldDialog.getFieldType();
//System.out.println("FieldPropertiesAction fldType: " + fldType);
            editable = ((Boolean) NewFieldDialogNov.outputValues.at(4)).booleanValue();
            removable = ((Boolean) NewFieldDialogNov.outputValues.at(5)).booleanValue();
            formula = (String) NewFieldDialogNov.outputValues.at(6);
            externalData = ((Boolean) NewFieldDialogNov.outputValues.at(7)).booleanValue();
        }

        TableColumn col = dbTable.tableColumns.get(columnIndex).tableColumn;
        if (!name.equals(f.getName())) {
            /* Set the new name of the field.
            */
            try{
                dbTable.table.renameField(f.getName(), name);
            }catch (FieldNameInUseException e1) {
                Object[] ok = {dbTable.bundle.getString("OK")};
                JOptionPane pane = new JOptionPane(e1.message, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, UIManager.getIcon("OptionPane.errorIcon"), ok, dbTable.bundle.getString("OK"));
                JDialog dialog = pane.createDialog(dbTable, dbTable.bundle.getString("Error"));
                dialog.show();

                if (!(ESlateOptionPane.showConfirmDialog(dbTable, dbTable.bundle.getString("DatabaseMsg21"), dbTable.bundle.getString("DatabaseMsg22"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION))
                    return;
            }
            catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (2)"); return;}

        }

        if (dbTable.table.isPartOfTableKey(f)/*f.isKey()*/ != key) {
            dbTable.changeKey(f.getName(), key, true);
        }

        if (calculated != f.isCalculated()) {
            if (!calculated) {
                dbTable.switchCalcFieldToNormal(name);
            }else{
                System.out.println("Serious inconsistency error in Database createMenuBar(): (8)");
                return;
            }
        }

        if (editable != f.isEditable())
            dbTable.setFieldEditable(f.getName(), editable);

        if (removable != f.isRemovable())
            dbTable.setFieldRemovable(f.getName(), removable);


        if (!f.getDataType().equals(fldType) && !f.isCalculated())
            dbTable.changeFieldType(f, fldType /*TableField.getInternalDataTypeName(c)*//*dataType*/, true);

        if (f.isCalculated() && !f.getTextFormula().equals(formula)) {
            try{
                String typ = f.getDataType().getName();
                if (dbTable.table.changeCalcFieldFormula(f.getName(), formula)) {
/*                    ((HeaderRenderer) col.getHeaderRenderer()).setIcon(f.getFieldType(), !f.isKey(), f.isDate(), activeDBTable);
activeDBTable.jTable.getTableHeader().repaint(activeDBTable.jTable.getTableHeader().getVisibleRect());
*/
                    /* If the type of a calculated field has changed as a result of editing its
                    * formula, then make all the arrangements which have to do with the
                    * apperance of the field. These arrangements invlve the column's
                    * "CellRenderer".
                    */
/*                    if (!typ.equals(f.getFieldType().getName())) {
DBCellRenderer renderer = (DBCellRenderer) col.getCellRenderer();
ColumnRendererEditor existing;
int colRendererEditorsIndex = -1;
for (int i=0; i<activeDBTable.colRendererEditors.size(); i++) {
existing = (ColumnRendererEditor) activeDBTable.colRendererEditors.at(i);
if (renderer.equals(existing.renderer)) {
colRendererEditorsIndex = i;
break;
}
}

if (colRendererEditorsIndex == -1) {
System.out.println("Serious inconsistency error in Database ciFieldEdit: (2)");
return;
}

((ColumnRendererEditor) activeDBTable.colRendererEditors.at(colRendererEditorsIndex)).updateColumnRenderer(f);
((ColumnRendererEditor) activeDBTable.colRendererEditors.at(colRendererEditorsIndex)).updateColumnEditor(f);
*/
                    /* Adjust the color of the column's cell renderer
                    */
/*                        activeDBTable.adjustCellRendererColor(f, ((ColumnRendererEditor) activeDBTable.colRendererEditors.at(colRendererEditorsIndex)).renderer);
}
activeDBTable.refreshField(index);
*/
                }else{
                    Object[] ok = {dbTable.bundle.getString("OK")};
                    JOptionPane pane = new JOptionPane(dbTable.bundle.getString("DatabaseMsg27") + f.getName() + dbTable.bundle.getString("DatabaseMsg28"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, UIManager.getIcon("OptionPane.errorIcon"), ok, dbTable.bundle.getString("OK"));
                    JDialog dialog = pane.createDialog(dbTable, dbTable.bundle.getString("Error"));
                    dialog.show();
                }
            }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (34)"); return;}
            catch (InvalidFormulaException e1) {
                ESlateOptionPane.showMessageDialog(dbTable, e1.message, dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }
            catch (AttributeLockedException e1) {
                ESlateOptionPane.showMessageDialog(dbTable, e1.getMessage(), dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }

        try{
            dbTable.table.setFieldContainsLinksToExternalData(f.getName(), externalData);
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in Database fieldEditAction() : (1)");
        }
    }

}
