package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;

public class PasteAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public PasteAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/paste.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/pasteDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public PasteAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
        if (dBase.activeDBTable != null)
            dBase.activeDBTable.pasteCellContents(dBase.activeDBTable.activeRow, dBase.activeDBTable.jTable.getSelectedColumn());
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setPasteEnabled(b);
    }

}
