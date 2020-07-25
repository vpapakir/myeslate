package gr.cti.eslate.database;

import gr.cti.eslate.tableModel.event.*;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.typeArray.IntBaseArray;

import javax.swing.JOptionPane;
import javax.swing.JDialog;
import java.beans.PropertyVetoException;


/**
 * Title:        Database
 * Description:  Your description
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 * @version
 */

public class RemoteDatabaseTableModelListener implements DatabaseTableModelListener {
    Table remoteTable = null, localTable = null;;
    Database dBase;
    Object remoteTableOwner = null;

    public RemoteDatabaseTableModelListener(Database dBase, Table remoteTable, Table localTable, Object remoteTableOwner) {
        this.remoteTable = remoteTable;
        this.localTable = localTable;
        this.dBase = dBase;
        this.remoteTableOwner = remoteTableOwner;
    }

    public void columnKeyChanged(ColumnKeyChangedEvent event) {
        Table ctable = (Table) event.getSource();
        AbstractTableField f = null;
        try{
            f = ctable.getTableField(event.getColumnName());
        }catch (InvalidFieldNameException exc) {}
        if (f == null) return;

        if (ctable.isPartOfTableKey(f)/*f.isKey()*/) {
            try{
                localTable.addToKey(f.getName());
/*                            if (activeDBTable.headerIconsVisible) {
                            ((HeaderRenderer) col.getHeaderRenderer()).setIcon(f.getFieldType(), !f.isKey(), f.isDate(), activeDBTable);
                            activeDBTable.jTable.getTableHeader().paintImmediately(activeDBTable.jTable.getTableHeader().getVisibleRect());
                        }
                        activeDBTable.refresh();
*/
            }catch (FieldAlreadyInKeyException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnKeyChanged(): (1)"); return;}
             catch (FieldContainsEmptyCellsException e1) {
                 ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
             }
             catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnKeyChanged(): (2)"); return;}
             catch (TableNotExpandableException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
             }
             catch (InvalidKeyFieldException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, dBase.dbComponent.infoBundle.getString("DatabaseMsg23"), dBase.dbComponent.warningStr, JOptionPane.WARNING_MESSAGE);
             }
             catch (AttributeLockedException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
             }
        }else{
             try{
                localTable.removeFromKey(f.getName());
             }catch (FieldIsNotInKeyException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnKeyChanged(): (3)"); return;}
              catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnKeyChanged(): (4)"); return;}
              catch (TableNotExpandableException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
              }
              catch (AttributeLockedException e1) {
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
              }
        }
    }

    public void calcColumnReset(CalcColumnResetEvent event) {
        Table ctable = (Table) event.getSource();
        try{
            localTable.switchCalcFieldToNormal(event.getColumnName());
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener calcColumnReset(): (1)"); return;}
         catch (AttributeLockedException e1) {
             ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
         }
    }

    public void calcColumnFormulaChanged(CalcColumnFormulaChangedEvent event) {
        try{
            if (!localTable.changeCalcFieldFormula(event.getColumnName(), event.getFormula())) {
                Object[] ok = {dBase.infoBundle.getString("OK")};
                JOptionPane pane = new JOptionPane(dBase.infoBundle.getString("DatabaseMsg27") + event.getColumnName() + dBase.infoBundle.getString("DatabaseMsg28"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, javax.swing.UIManager.getIcon("OptionPane.errorIcon"), ok, dBase.infoBundle.getString("OK"));
                JDialog dialog = pane.createDialog(dBase.dbComponent, dBase.errorStr);
                dialog.show();
            }
        }catch (InvalidFieldNameException exc) {}
         catch (InvalidFormulaException exc) {}
         catch (AttributeLockedException exc) {}
    }

    public void columnEditableStateChanged(ColumnEditableStateChangedEvent event) {
        try{
            localTable.setFieldEditable(event.getColumnName(), event.isEditable());
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnEditableStateChanged(): (17)");}
         catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
         }
    }

    public void columnRemovableStateChanged(ColumnRemovableStateChangedEvent event) {
        try{
            localTable.setFieldRemovable(event.getColumnName(), event.isRemovable());
        }catch (InvalidFieldNameException exc) {
            System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnRemovableStateChanged(): (1)");
        }catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
         }
    }

    public void columnHiddenStateChanged(ColumnHiddenStateChangedEvent event) {
        try{
            localTable.setFieldHidden(event.getColumnName(), event.isHidden());
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnHiddenStateChanged(): (1)");}
         catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.errorStr, JOptionPane.ERROR_MESSAGE);
         }
    }

    public void recordAdded(RecordAddedEvent event) {
        int recIndex = event.getRecordIndex();
        try{
            localTable.addRecord(((Table) event.getSource()).getRecord(recIndex), false);
        }catch (Exception e) {
            System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener recordAdded(): (1)");
        }
    }

    public void emptyRecordAdded(RecordAddedEvent event) {
        DBTable table = dBase.getDBTableOfTable(localTable);
        table.newRecord();
    }

    public void recordRemoved(RecordRemovedEvent event) {
        int rowIndex = event.getRowIndex();
        int recIndex = event.getRecordIndex();
        boolean isChanging = event.isChanging();
        try{
            localTable.removeRecord(recIndex, rowIndex, isChanging);
        }catch (TableNotExpandableException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error") + "1", JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (InvalidRecordIndexException e1) {
            System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener recordRemoved(): (1)");
            return;
         }
    }

    public void activeRecordChanged(ActiveRecordChangedEvent event) {
        Table ctable = (Table) event.getSource();
        int activeRecord = event.getActiveRecord();
        if (activeRecord < 0 || activeRecord >= ctable.recordIndex.size())
            localTable.setActiveRecord(-1);
        else{
//            scrollToActiveRecord = false;
            localTable.setActiveRecord(activeRecord);
//            scrollToActiveRecord = true;
        }
    }

    public void tableHiddenStateChanged(TableHiddenStateChangedEvent event) {
        try{
            localTable.setHidden(event.isHidden());
        }catch (AttributeLockedException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.errorStr, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void currencyFieldChanged(ColumnEvent event) {
    }

    public void cellValueChanged(CellValueChangedEvent event) {
//        System.out.println("CellValueChangedEvent " + event.getColumnName() + ",  " + event.getRecordIndex() + ", " + event.getNewValue());
        try{
            Object newValue = ((Table) event.getSource()).getTableField(event.getColumnName()).getCellObject(event.getRecordIndex());
            localTable.setCell(event.getColumnName(), event.getRecordIndex(), newValue);
        }catch (InvalidFieldNameException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (InvalidDataFormatException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (NullTableKeyException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (InvalidCellAddressException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }catch (DuplicateKeyException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
         catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void columnTypeChanged(ColumnTypeChangedEvent event) {
        String newDataTypeStr = event.getNewType();
        String newDataTypeCode = null;
        Class newDataType = null;
        try{
            newDataType = Class.forName(newDataTypeStr);
            newDataTypeCode = AbstractTableField.getInternalDataTypeName(newDataType);
        }catch (Exception exc) {
            return;
        }

        System.out.println("newDataType: " + newDataTypeStr + ", newDataTypeCode: " + newDataTypeCode);
        try{
            AbstractTableField f = localTable.getTableField(event.getColumnName());
            System.out.println("f.getName(): " + f.getName() + ", f.getFieldType(): " + f.getDataType());
        }catch (Exception exc) {}
        try{
            localTable.changeFieldType(event.getColumnName(), newDataType, true);
        }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnTypeChanged: (2)"); return;}
         catch (InvalidFieldTypeException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (FieldIsKeyException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (InvalidTypeConversionException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (FieldNotEditableException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (DependingCalcFieldsException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (AttributeLockedException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (DataLossException e1) {}
    }

    public void columnRenamed(ColumnRenamedEvent event) {
        try{
            localTable.renameField(event.getOldName(), event.getNewName());
        }catch (FieldNameInUseException e1) {
            Object[] ok = {dBase.infoBundle.getString("OK")};
            JOptionPane pane = new JOptionPane(e1.message, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, javax.swing.UIManager.getIcon("OptionPane.errorIcon"), ok, dBase.infoBundle.getString("OK"));
            JDialog dialog = pane.createDialog(dBase.dbComponent, dBase.errorStr);
            dialog.show();

            if (!(ESlateOptionPane.showConfirmDialog(dBase.dbComponent, dBase.infoBundle.getString("DatabaseMsg21"), dBase.infoBundle.getString("DatabaseMsg22"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION))
                return;
        }
         catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnRenamed(): (2)"); return;}
    }

    public void columnAdded(ColumnAddedEvent event) {
        Table ctable = (Table) event.getSource();
        AbstractTableField f = null;
        try{
            f = ctable.getTableField(event.getColumnIndex());
        }catch (Exception exc) {}
        if (f == null) return;

        try{
            if (f.isCalculated())
                localTable.addCalculatedField(f.getName(),
                                               f.getTextFormula(),
//                                               f.isKey(),
                                               f.isRemovable(),
                                               false);
             else
                localTable.addField(f.getName(),
                                     /*AbstractTableField.getInternalDataTypeName(*/f.getDataType(),
                                     f.isEditable(),
//                                     f.isKey(),
                                     f.isRemovable(),
                                     false);

        }catch (InvalidFieldNameException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (InvalidKeyFieldException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (InvalidFieldTypeException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (InvalidFormulaException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (IllegalCalculatedFieldException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (AttributeLockedException e1) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
    }

    public void columnReplaced(ColumnReplacedEvent event) {
    }

    public void columnRemoved(ColumnRemovedEvent event) {
        String fieldName = event.getColumnName();
        try{
//            System.out.println("removeColumn1");
            localTable.removeField(fieldName);
//            System.out.println("removeColumn2");
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in RemoteDatabaseTableModelListener columnRemoved() : (1)");}
         catch (TableNotExpandableException e) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (CalculatedFieldExistsException e) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (FieldNotRemovableException e) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e.message, dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}
         catch (AttributeLockedException e) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e.getMessage(), dBase.dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);}

        DBTable dbTable = dBase.getDBTableOfTable(localTable);
        if (dbTable == null) {
            System.out.println("Inconsistency error in RemoteDatabaseTableModelListener columnRemoved()");
            return;
        }
        for (int m=0; m<dbTable.colSelectionStatus.size(); m++)
            dbTable.colSelectionStatus.set(m, Boolean.FALSE);
        dbTable.jTable.getColumnModel().getSelectionModel().clearSelection();

        if (dbTable.jTable.getColumnCount() == 0 && dBase.addRecordAction.isEnabled())
            dBase.addRecordAction.setEnabled(false);
    }

    public void selectedRecordSetChanged(SelectedRecordSetChangedEvent event) {
        Table ctable = (Table) event.getSource();
        localTable.setSelectedSubset(ctable.getSelectedSubset());
    }

    public void rowOrderChanged(RowOrderChangedEvent event) {
        localTable.recordIndex = (IntBaseArray) ((Table) event.getSource()).recordIndex.clone();
        localTable.rowOrderChanged();
    }

    public void tableRenamed(TableRenamedEvent event) {
        try{
            localTable.setTitle(event.getNewTitle());
        }catch (InvalidTitleException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.errorStr, JOptionPane.ERROR_MESSAGE);
            return;
        }
         catch (PropertyVetoException exc) {
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), dBase.errorStr, JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}