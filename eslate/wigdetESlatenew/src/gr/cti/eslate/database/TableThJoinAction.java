package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Cursor;
import java.awt.Color;
import java.util.ArrayList;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import gr.cti.typeArray.StringBaseArray;

import javax.swing.JComboBox;

public class TableThJoinAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String errorStr;
    String actionName;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    transient ResourceBundle infoBundle;
    transient Locale locale;

    public TableThJoinAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public TableThJoinAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        String currTableTitle = dBase.db.getActiveTable().getTitle();
        String dialogMessage;

        ArrayList tableTitles = dBase.db.getTableTitles(); //new ArrayList();
/*        ArrayList CTables = dBase.db.getTables();
        for (int i=0; i<CTables.size(); i++) {
            if (((Table) CTables.get(i)).isHidden())
                continue;
            CTableTitles.add(((Table) CTables.get(i)).getTitle());
        }
*/
        if (currTableTitle == null || currTableTitle.trim().length() == 0)
            dialogMessage = infoBundle.getString("DatabaseMsg61");
        else{
            dialogMessage = infoBundle.getString("DatabaseMsg55") + currTableTitle + infoBundle.getString("DatabaseMsg62");
            tableTitles.remove(currTableTitle);
        }

        Object[] tableTitlesArray = tableTitles.toArray(); //new Object[CTableTitles.size()];
//        CTableTitles.copyTo(ctableTitles);
        String[] supportedDelimiters = {",", ";"};
        String secTableTitle = (String) ESlateOptionPane.showInputDialog(dBase.dbComponent,
                                  dialogMessage,
                                  infoBundle.getString("DatabaseMsg15"),
                                  JOptionPane.INFORMATION_MESSAGE,
                                  null,
                                  tableTitlesArray,
                                  null);
        if (secTableTitle == null) return;

        Table secTable = dBase.db.getTable(secTableTitle);

      ThJoinDialog pf = new ThJoinDialog(dBase.topLevelFrame, dBase.db.getActiveTable(), secTable, dBase.dbComponent);

      if (ThJoinDialog.clickedButton == 1) {
            dBase.statusToolbarController.setMessageLabelInWaitState();
            dBase.dbComponent.setCursor(waitCursor);
          StringBaseArray fields1 = new StringBaseArray();
          StringBaseArray fields2 = new StringBaseArray();
          ArrayList operators = new ArrayList();
          for (int i=0; i<ThJoinDialog.table1Boxes.size(); i++) {
                fields1.add((String) ((JComboBox) ThJoinDialog.table1Boxes.at(i)).getSelectedItem());
                operators.add((String) ((JComboBox) ThJoinDialog.compBoxes.at(i)).getSelectedItem());
                fields2.add((String) ((JComboBox) ThJoinDialog.table2Boxes.at(i)).getSelectedItem());
            }

            try{
                if (!dBase.db.th_join(secTable, fields1, operators, fields2)) {
                    dBase.dbComponent.setCursor(defaultCursor);
                    dBase.statusToolbarController.setMessageLabelColor(Color.white);
                    dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                    ESlateOptionPane.showMessageDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg19"), errorStr, JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }catch (JoinMissingFieldOrOperatorException e1) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return;
             }
             catch (JoinMissingFieldException e1) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return;
             }
             catch (InvalidFieldNameException e1) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return;
             }
             catch (JoinIncompatibleFieldTypesException e1) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return;
             }
             catch (InvalidOperatorException e1) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return;
             }
             catch (JoinUnableToPerformException e1) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
                return;
             }

            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            dBase.dbComponent.setCursor(defaultCursor);
        }
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableThJoin.setEnabled(b);
    }
}
