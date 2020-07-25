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
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;


public class TableUnionAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String errorStr;
    String actionName;
    final static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    final static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    transient ResourceBundle infoBundle;
    transient Locale locale;

    public TableUnionAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public TableUnionAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
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

        ArrayList tableTitles = new ArrayList();
        ArrayList CTables = dBase.db.getTables();
        for (int i=0; i<CTables.size(); i++) {
            if (((Table) CTables.get(i)).isHidden())
                continue;
            tableTitles.add(((Table) CTables.get(i)).getTitle());
        }

        if (currTableTitle == null || currTableTitle.trim().length() == 0)
            dialogMessage = infoBundle.getString("DatabaseMsg54");
        else{
            dialogMessage = infoBundle.getString("DatabaseMsg55") + currTableTitle + infoBundle.getString("DatabaseMsg56");
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

        dBase.statusToolbarController.setMessageLabelInWaitState();
        dBase.dbComponent.setCursor(waitCursor);
        Table secTable = dBase.db.getTable(secTableTitle);

        try{
            if (!dBase.db.union(secTable)) {
                dBase.dbComponent.setCursor(defaultCursor);
                dBase.statusToolbarController.setMessageLabelColor(Color.white);
                dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
                ESlateOptionPane.showMessageDialog(dBase.dbComponent, infoBundle.getString("DatabaseMsg16"), errorStr, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }catch (UnionUnableToPerformException e1) {
            dBase.dbComponent.setCursor(defaultCursor);
            dBase.statusToolbarController.setMessageLabelColor(Color.white);
            dBase.updateNumOfSelectedRecords(dBase.activeDBTable);
            ESlateOptionPane.showMessageDialog(dBase.dbComponent, e1.message, errorStr, JOptionPane.ERROR_MESSAGE);
            return;
         }
         catch (UnionTablesNotIdenticalException e1) {
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

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.menu.miTableUnion.setEnabled(b);
    }
}
