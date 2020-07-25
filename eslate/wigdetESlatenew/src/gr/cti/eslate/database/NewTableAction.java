package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import java.awt.Cursor;
import java.awt.Color;
import javax.swing.JFrame;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.database.engine.TableField;
import javax.swing.JOptionPane;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewTableAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    Database dBase;
    String actionName;
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    String errorStr;
    public static final int NOVICE_USER_MODE = 0;

    public NewTableAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public NewTableAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        dBase.dbComponent.setCursor(waitCursor);
        dBase.statusToolbarController.setMessageLabelInWaitState();

        if (dBase.userMode == NOVICE_USER_MODE) {
            dBase.addEmptyTable(dBase.db, 2, 1);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            dBase.dbComponent.setCursor(defaultCursor);
        }else{
            NewTableDialog ntd = new NewTableDialog(dBase.topLevelFrame, dBase.activeDBTable, dBase.dbComponent);
            if (ntd.clickedButton == 1) {
                dBase.statusToolbarController.setMessageLabelInWaitState();
                dBase.dbComponent.setCursor(waitCursor);
                Table newTable = new Table(ntd.tableName);

                for (int i=0; i<ntd.fieldNameList.size(); i++) {
                    if (!(((Boolean) ntd.calculatedList.at(i)).booleanValue())) {
                        try{
                          newTable.addField((String) ntd.fieldNameList.at(i),
                                        /*TableField.getInternalDataTypeName(*/(Class) ntd.dataTypeList.at(i),
//                                        (String) ntd.dataTypeList.at(i),
                                        ((Boolean) ntd.editableList.at(i)).booleanValue(),
//                                        ((Boolean) ntd.keyList.at(i)).booleanValue(),
                                        ((Boolean) ntd.removableList.at(i)).booleanValue(),
                                        false);
                        }catch (InvalidFieldNameException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                         catch (InvalidKeyFieldException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                         catch (InvalidFieldTypeException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                         catch (AttributeLockedException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);}
                    }else{
                        try{
                            newTable.addCalculatedField((String) ntd.fieldNameList.at(i),
                                                       (String) ntd.formulaList.at(i),
//                                                       ((Boolean) ntd.keyList.at(i)).booleanValue(),
                                                         ((Boolean) ntd.removableList.at(i)).booleanValue(),
                                                       false);
                        }catch (IllegalCalculatedFieldException e1) {
                            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                        }
                        catch (InvalidFormulaException e2) {
                            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e2.message, errorStr, JOptionPane.ERROR_MESSAGE);
                        }
                        catch (InvalidFieldNameException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                        catch (InvalidKeyFieldException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);}
                        catch (AttributeLockedException e1) {ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);}
                    }
                }
                try{
                    dBase.db.addTable(newTable, newTable.getTitle(), false);
                }catch (InvalidTitleException exc) {
                    ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                    newTable = null;
                }
                 catch (InsufficientPrivilegesException exc) {
                    ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                    newTable = null;
                }
            }
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            dBase.dbComponent.setCursor(defaultCursor);
        }
        if (dBase.activeDBTable != null)
            dBase.tableRemoveAction.setEnabled(true);
        else
            dBase.tableRemoveAction.setEnabled(false);
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableNew.setEnabled(b);
    }
}
