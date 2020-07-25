package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import gr.cti.eslate.database.engine.*;
import javax.swing.JOptionPane;
import gr.cti.eslate.utils.*;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.base.*;

public class TableHiddenAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    Database dBase;
    String actionName;
    String errorStr;

    public TableHiddenAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public TableHiddenAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        try{
            if (dBase.activeDBTable != null)
                dBase.activeDBTable.table.setHidden(dBase.menu.miTableHidden.isSelected());
        }catch (AttributeLockedException exc) {
              ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
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
        dBase.menu.miTableHidden.setEnabled(b);
    }
}
