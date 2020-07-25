package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateOptionPane;

public class FieldPropertiesAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public FieldPropertiesAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/fieldProperties.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/fieldPropertiesDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public FieldPropertiesAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        execute();
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setFieldPropertiesEnabled(b);
        dBase.menu.ciFieldEdit.setEnabled(b);
    }

    protected void execute() {
        DBTable dbTable = dBase.activeDBTable;
        if (dbTable != null)
            dbTable.fieldPropertiesAction.execute(dBase.userMode);
/*1        int index;
        if ((index = dBase.activeDBTable.jTable.getSelectedColumn()) == -1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, dBase.infoBundle.getString("DatabaseMsg20"), dBase.warningStr, JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Table table = dBase.activeDBTable.table;
        if (!table.isFieldPropertyEditingAllowed())
            return;

        AbstractTableField f;
        try{
            f = dBase.activeDBTable.table.getTableField(((TableColumn) dBase.activeDBTable.tableColumns.get(index)).getModelIndex()); //tci ((Integer) activeDBTable.tableColumnsIndex.at(index)).intValue());
        }catch (InvalidFieldIndexException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (1)"); return;}

        String name, dataType, formula;
        Class fldType;
        boolean key, calculated, editable, removable, externalData;

        if (dBase.userMode == dBase.ADVANCED_USER_MODE) {
            NewFieldDialog.inputValues.clear();
            NewFieldDialog.inputValues.add(f.getName());
            NewFieldDialog.inputValues.add(new Boolean(table.isPartOfTableKey(f)));
            NewFieldDialog.inputValues.add(new Boolean(f.isCalculated()));
            String type = f.getDataType().getName().substring(f.getDataType().getName().lastIndexOf('.')+1, f.getDataType().getName().length());

            NewFieldDialog.inputValues.add(type);
            NewFieldDialog.inputValues.add(new Boolean(f.isEditable()));
            NewFieldDialog.inputValues.add(new Boolean(f.isRemovable()));
            NewFieldDialog.inputValues.add(f.getTextFormula());
            NewFieldDialog.inputValues.add(new Boolean(f.containsLinksToExternalData()));
            //NewFieldDialog fldDialog = new NewFieldDialog(fr, activeDBTable, true);
            NewFieldDialog fldDialog = new NewFieldDialog(dBase.topLevelFrame, dBase.activeDBTable, true, dBase.infoBundle.getString("EditFieldDialogMsg"));
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
            NewFieldDialogNov.inputValues.add(new Boolean(table.isPartOfTableKey(f)));
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
            NewFieldDialogNov fldDialog = new NewFieldDialogNov(dBase.topLevelFrame, dBase.activeDBTable, true, dBase.infoBundle.getString("EditFieldDialogMsg"));
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

        TableColumn col = (TableColumn) dBase.activeDBTable.tableColumns.get(index);
        if (!name.equals(f.getName())) {
1*/            /* Set the new name of the field.
            */
/*1            try{
                dBase.activeDBTable.table.renameField(f.getName(), name);
            }catch (FieldNameInUseException e1) {
                Object[] ok = {dBase.infoBundle.getString("OK")};
                JOptionPane pane = new JOptionPane(e1.message, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, UIManager.getIcon("OptionPane.errorIcon"), ok, dBase.infoBundle.getString("OK"));
                JDialog dialog = pane.createDialog(dBase.dbComponent, dBase.errorStr);
                dialog.show();

                if (!(ESlateOptionPane.showConfirmDialog(dBase.dbComponent, dBase.infoBundle.getString("DatabaseMsg21"), dBase.infoBundle.getString("DatabaseMsg22"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION))
                    return;
            }
            catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (2)"); return;}

        }

        if (table.isPartOfTableKey(f) != key) {
            dBase.activeDBTable.changeKey(f.getName(), key, true);
        }

        if (calculated != f.isCalculated()) {
            if (!calculated) {
                dBase.activeDBTable.switchCalcFieldToNormal(name);
            }else{
                System.out.println("Serious inconsistency error in Database createMenuBar(): (8)");
                return;
            }
        }

        if (editable != f.isEditable())
            dBase.activeDBTable.setFieldEditable(f.getName(), editable);

        if (removable != f.isRemovable())
            dBase.activeDBTable.setFieldRemovable(f.getName(), removable);


        if (!f.getDataType().equals(fldType) && !f.isCalculated())
            dBase.activeDBTable.changeFieldType(f, fldType , true);

        if (f.isCalculated() && !f.getTextFormula().equals(formula)) {
            try{
                String typ = f.getDataType().getName();
                if (dBase.activeDBTable.table.changeCalcFieldFormula(f.getName(), formula)) {
1*/
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
/*1                }else{
                    Object[] ok = {dBase.infoBundle.getString("OK")};
                    JOptionPane pane = new JOptionPane(dBase.infoBundle.getString("DatabaseMsg27") + f.getName() + dBase.infoBundle.getString("DatabaseMsg28"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, UIManager.getIcon("OptionPane.errorIcon"), ok, dBase.infoBundle.getString("OK"));
                    JDialog dialog = pane.createDialog(dBase.dbComponent, dBase.errorStr);
                    dialog.show();
                }
            }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in Database createMenuBar(): (34)"); return;}
            catch (InvalidFormulaException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.errorStr, JOptionPane.ERROR_MESSAGE);
            }
            catch (AttributeLockedException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.errorStr, JOptionPane.ERROR_MESSAGE);
            }
        }

        try{
            dBase.activeDBTable.table.setFieldContainsLinksToExternalData(f.getName(), externalData);
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in Database fieldEditAction() : (1)");
        }
1*/
    }

}
