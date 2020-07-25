package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import gr.cti.eslate.database.engine.*;
import java.util.Locale;
import java.util.ResourceBundle;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import javax.swing.JOptionPane;

public class RenameAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;
    transient ResourceBundle infoBundle;
    transient Locale locale;
    String errorStr;

    public RenameAction(Database db, String name){
        iconEnabled = null;
        iconDisabled = null;
        dBase = db;
        actionName = name;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public RenameAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        actionName = name;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        locale=ESlateMicroworld.getCurrentLocale();
        infoBundle=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", locale);
        errorStr = infoBundle.getString("Error");
    }

    public void actionPerformed(ActionEvent e) {
        String currTitle = dBase.db.getTitle();
        String newTitle = (String) ESlateOptionPane.showInputDialog(dBase.dbComponent,
                              infoBundle.getString("DatabaseMsg4"),
                              infoBundle.getString("DatabaseMsg5"),
                              JOptionPane.QUESTION_MESSAGE,
                              null,
                              null,
                              currTitle);

        if (newTitle != null) {
            /*if (dBase.eSlateHandle != null) {
                try{
                    dBase.eSlateHandle.setComponentName(newTitle);
                }catch (NameUsedException exc) {
                    ESlateOptionPane.showMessageDialog(dBase.dbComponent, exc.getMessage(), errorStr, JOptionPane.ERROR_MESSAGE);
                    try{
                        dBase.eSlateHandle.setUniqueComponentName(newTitle);
                    }catch (RenamingForbiddenException exc1) {}
                    return;
                }catch (RenamingForbiddenException exc) {}
                 catch (IllegalArgumentException exc) {
                    System.out.println("Handle not renamed: " + exc.getMessage());
                    //Name contains dots
                }
            }*/
            dBase.db.setTitle(newTitle);
        }
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        dBase.menu.miDBRename.setEnabled(b);
    }
}
