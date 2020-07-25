package gr.cti.eslate.database;

import gr.cti.eslate.database.engine.AttributeLockedException;
import gr.cti.eslate.database.engine.DuplicateKeyException;
import gr.cti.eslate.database.engine.InvalidCellAddressException;
import gr.cti.eslate.database.engine.InvalidDataFormatException;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.NullTableKeyException;
import gr.cti.eslate.database.engine.Table;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class InsFilePathAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public InsFilePathAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/insertfilepath.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/insertfilepathdisabled.gif"));
        dBase = db;
        actionName = name;
        fileChooser=new JFileChooser();
        approveText=ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault()).getString("Select File");
    }

    public InsFilePathAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
        fileChooser=new JFileChooser();
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
        int r=fileChooser.showDialog(dBase,approveText);
        if (r==JFileChooser.APPROVE_OPTION) {
        	DBTable t=dBase.getActiveDBTable();
        	try {
				t.getTable().setCell(t.getJTable().getColumnName(t.getJTable().getSelectedColumn()), t.getTable().getActiveRecord(), fileChooser.getSelectedFile().getCanonicalPath());
			} catch (InvalidCellAddressException e1) {
				e1.printStackTrace();
			} catch (NullTableKeyException e1) {
				e1.printStackTrace();
			} catch (InvalidDataFormatException e1) {
				e1.printStackTrace();
			} catch (DuplicateKeyException e1) {
				e1.printStackTrace();
			} catch (AttributeLockedException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InvalidFieldNameException e1) {
				e1.printStackTrace();
			}
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
        dBase.standardToolbarController.setInsertFilePathEnabled(b);
    }
    
    private JFileChooser fileChooser;
    private String approveText;
}
