package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class CopyAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public CopyAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/copy.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/copyDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public CopyAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
        if (dBase.activeDBTable != null)
            dBase.activeDBTable.copyCellContents(dBase.activeDBTable.activeRow, dBase.activeDBTable.jTable.getSelectedColumn());
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setCopyEnabled(b);
    }
}
