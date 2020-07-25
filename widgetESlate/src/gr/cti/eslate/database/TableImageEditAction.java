/*
* Created by IntelliJ IDEA.
* User: tsironis
* Date: 30 Οκτ 2002
* Time: 8:34:40 μμ
* To change template for new class use
* Code Style | Class Templates options (Tools | IDE Options).
*/
package gr.cti.eslate.database;

import java.awt.event.ActionEvent;
import javax.swing.table.TableColumn;
import javax.swing.*;
import java.util.ArrayList;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.imageEditor.ImageEditor;
import gr.cti.eslate.imageEditor.ImageEditorDialog;
//import com.objectspace.jgl.Array;

public class TableImageEditAction extends AbstractAction {
    DBTable dbTable = null;

    public TableImageEditAction(DBTable dbTable, String name){
        super(name);
        this.dbTable = dbTable;
    }


    public void actionPerformed(ActionEvent e) {
        execute();
    }


    public void execute() {
        int row = dbTable.activeRow;
        int column = dbTable.jTable.getSelectedColumn();
        Table table = dbTable.table;
        JTable jTable = dbTable.jTable;
        DBTableColumnBaseArray tableColumns = dbTable.tableColumns;

        if (row >= 0 && row < table.getRecordCount() &&
                column >= 0 && column < table.getFieldCount()) {
            int fieldIndex = tableColumns.get(column).tableColumn.getModelIndex();
            CImageIcon icon = (CImageIcon) table.riskyGetCell(fieldIndex, table.recordIndex.get(dbTable.activeRow));
            ImageEditor imageEditor;
            ImageEditorDialog imageEditorDialog;

            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getAncestorOfClass(java.awt.Frame.class, dbTable);
            if (icon != null && icon.getIcon() != null) {
                //try{
                imageEditorDialog = new ImageEditorDialog(parentFrame, 32,32);
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
                imageEditorDialog = new ImageEditorDialog(parentFrame, 32,32);
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
                    table.setCell(fieldIndex, table.recordIndex.get(dbTable.activeRow), newIcon);
                    jTable.paintImmediately(jTable.getCellRect(table.recordIndex.get(dbTable.activeRow), fieldIndex, true));
                    try{
                        AbstractTableField fld = table.getTableField(fieldIndex);
                        if (fld.containsLinksToExternalData()) {
//                                newIcon.setReferenceToExternalFile(dbTable, fld, true);
                            if (newIcon.getReference() == null || !imageEditor.isImageSaved())
                                ESlateOptionPane.showMessageDialog(dbTable, dbTable.bundle.getString("IconEditActionMsg1"), dbTable.bundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                        }
                    }catch (InvalidFieldIndexException exc) {
                        System.out.println("Serious inconsistency error in DBTable iconEditAction(): (1)");
                    }
                }catch (InvalidDataFormatException exc) {
                    ESlateOptionPane.showMessageDialog(dbTable, exc.getMessage(), dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }catch (NullTableKeyException exc) {
                    ESlateOptionPane.showMessageDialog(dbTable, exc.getMessage(), dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }catch (InvalidCellAddressException exc) {
                    ESlateOptionPane.showMessageDialog(dbTable, exc.getMessage(), dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }catch (DuplicateKeyException exc) {
                    ESlateOptionPane.showMessageDialog(dbTable, exc.getMessage(), dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
                catch (AttributeLockedException e1) {
                    ESlateOptionPane.showMessageDialog(dbTable, e1.getMessage(), dbTable.bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

}
