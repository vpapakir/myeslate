package gr.cti.eslate.database;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.imageEditor.ImageEditor;
import gr.cti.eslate.imageEditor.ImageEditorDialog;
import com.objectspace.jgl.Array;

public class ImageEditAction extends AbstractAction {

    Icon iconDisabled, iconEnabled;
    Database dBase;
    String actionName;

    public ImageEditAction(Database db, String name){
        iconEnabled = new ImageIcon(getClass().getResource("images/toolbar/iconEditor.gif"));
        iconDisabled = new ImageIcon(getClass().getResource("images/toolbar/iconEditorDisabled.gif"));
        dBase = db;
        actionName = name;
    }

    public ImageEditAction(Database db, String name, Icon iconEnabled, Icon iconDisabled) {
        dBase = db;
        this.iconDisabled = iconDisabled;
        this.iconEnabled = iconEnabled;
        actionName = name;
    }

    public void actionPerformed(ActionEvent e) {
        if (dBase.visiblePopupMenu != null && dBase.visiblePopupMenu.isVisible())
            dBase.visiblePopupMenu.setVisible(false);
        dBase.activeDBTable.imageEditAction.execute(); //dBase.activeDBTable.activeRow, dBase.activeDBTable.jTable.getSelectedColumn());
    }

    public Icon getEnabledIcon() {
        return iconEnabled;
    }

    public Icon getDisabledIcon() {
        return iconDisabled;
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        dBase.standardToolbarController.setImageEditEnabled(b);
    }

/*    public void execute(int row, int column) {
        DBTable dbTable = dBase.activeDBTable;
        Table ctable = dbTable.table;
        JTable table = dbTable.jTable;
        ArrayList tableColumns = dbTable.tableColumns;

        if (row >= 0 && row < ctable.getRecordCount() &&
            column >= 0 && column < ctable.getFieldCount()) {
                int fieldIndex = ((TableColumn) tableColumns.get(column)).getModelIndex();
                CImageIcon icon = (CImageIcon) ctable.riskyGetCell(fieldIndex, ctable.recordIndex.get(dbTable.activeRow));
                ImageEditor imageEditor;
                ImageEditorDialog imageEditorDialog;
                if (icon != null && icon.getIcon() != null) {
                    //try{
                        imageEditorDialog = new ImageEditorDialog(dBase.topLevelFrame, 32,32);
                        imageEditor = imageEditorDialog.getImageEditor();
                        imageEditorDialog.setImage(icon.getImage());
                        if (icon.getForegroundColor() != null) {
                            imageEditor.setForegroundColor(icon.getForegroundColor(), icon.getForegroundTransparency());
                        }
                        if (icon.getBackgroundColor() != null) {
                            imageEditor.setBackgroundColor(icon.getBackgroundColor(), icon.getBackgroundTransparency());
                        }
                        imageEditor.setFileName(icon.getFileName());
                    //}catch (IconTooBigException exc) {
                    //    ESlateOptionPane.showMessageDialog(dbComponent, exc.getMessage(), dbComponent.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    //    return;
                    //}
                }else{
                    imageEditorDialog = new ImageEditorDialog(dBase.topLevelFrame, 32,32);
                    imageEditor = imageEditorDialog.getImageEditor();
                }

                imageEditorDialog.showDialog(dbTable);

                if (imageEditorDialog.getReturnCode() == ImageEditorDialog.IMAGE_EDITOR_OK) {
                    CImageIcon newIcon = new CImageIcon();
                    newIcon.setImage(imageEditorDialog.getImage());
                    newIcon.setFileName(imageEditor.getFileName());
                    newIcon.setForegroundColor(imageEditor.getForegroundColor(), imageEditor.getForegroundTransparency());
                    newIcon.setBackgroundColor(imageEditor.getBackgroundColor(), imageEditor.getBackgroundTransparency());

                    try{
                        ctable.setCell(fieldIndex, ctable.recordIndex.get(dbTable.activeRow), newIcon);
                        table.paintImmediately(table.getCellRect(ctable.recordIndex.get(dbTable.activeRow), fieldIndex, true));
                        try{
                            AbstractTableField fld = ctable.getTableField(fieldIndex);
                            if (fld.containsLinksToExternalData()) {
//                                newIcon.setReferenceToExternalFile(dbTable, fld, true);
                                if (newIcon.getReference() == null || !imageEditor.isImageSaved())
                                    ESlateOptionPane.showMessageDialog(dBase, dBase.infoBundle.getString("IconEditActionMsg1"), dBase.infoBundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                            }
                        }catch (InvalidFieldIndexException exc) {
                            System.out.println("Serious inconsistency error in DBTable iconEditAction(): (1)");
                        }
                    }catch (InvalidDataFormatException exc) {
                        ESlateOptionPane.showMessageDialog(dBase, exc.getMessage(), dBase.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }catch (NullTableKeyException exc) {
                        ESlateOptionPane.showMessageDialog(dBase, exc.getMessage(), dBase.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }catch (InvalidCellAddressException exc) {
                        ESlateOptionPane.showMessageDialog(dBase, exc.getMessage(), dBase.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }catch (DuplicateKeyException exc) {
                        ESlateOptionPane.showMessageDialog(dBase, exc.getMessage(), dBase.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }
                     catch (AttributeLockedException e1) {
                        ESlateOptionPane.showMessageDialog(dBase, e1.getMessage(), dBase.infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
    }
*/

}
