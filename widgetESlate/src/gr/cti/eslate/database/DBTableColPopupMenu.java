/**
 * Created by IntelliJ IDEA.
 * User: Manolis Koutlis
 * Date: Mar 12, 2003
 * Time: 3:34:22 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

import gr.cti.eslate.eslateMenuBar.ESlatePopupMenu;
import gr.cti.eslate.eslateMenuBar.ESlateMenuItem;
import gr.cti.eslate.eslateMenuBar.ESlateCheckMenuItem;
import gr.cti.eslate.base.RenamingForbiddenException;

import javax.swing.*;
import java.awt.*;

public class DBTableColPopupMenu extends ESlatePopupMenu {
    ESlateMenuItem fieldPropsMItem, fieldNewMItem, fieldRemoveMItem, fieldSortAscMItem, fieldSortDescMItem;
    ESlateCheckMenuItem currencyFieldMItem = null;

    /**
     * Exists only for the Externalization mechanism.
     */
    public DBTableColPopupMenu() {
    }

    public DBTableColPopupMenu(DBTable dbTable) {
        fieldPropsMItem = new ESlateMenuItem(dbTable.fieldPropertiesAction);
        fieldPropsMItem.setAccelerator((KeyStroke) dbTable.fieldPropertiesAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) fieldPropsMItem);

        fieldNewMItem = new ESlateMenuItem(dbTable.addFieldAction);
        fieldNewMItem.setAccelerator((KeyStroke) dbTable.addFieldAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) fieldNewMItem);

        fieldRemoveMItem = new ESlateMenuItem(dbTable.removeFieldAction);
        fieldRemoveMItem.setAccelerator((KeyStroke) dbTable.removeFieldAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) fieldRemoveMItem);

        addSeparator();

        fieldSortAscMItem = new ESlateMenuItem(dbTable.fieldSortAscAction);
        fieldSortAscMItem.setAccelerator((KeyStroke) dbTable.fieldSortAscAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) fieldSortAscMItem);

        fieldSortDescMItem = new ESlateMenuItem(dbTable.fieldSortDescAction);
        fieldSortDescMItem.setAccelerator((KeyStroke) dbTable.fieldSortDescAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) fieldSortDescMItem);

        addSeparator();
        currencyFieldMItem = new ESlateCheckMenuItem(dbTable.numericFieldCurrencyAction);
        add(currencyFieldMItem);

        pack();
        try{
            getESlateHandle().setUniqueComponentName(dbTable.bundle.getString("Column Popup Name"));
        }catch (RenamingForbiddenException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * When a DBTableColPopupMenu is restored, this method attaches the menu and menu item variables of this class to
     * the restored menus and menu items.
     * @param dbTable
     */
    void locateMenuItems(DBTable dbTable) {
        int popUpContentsCount = getComponentCount();
//System.out.println("columnPopupContentsCount: " + popUpContentsCount);
        for (int i=0; i<popUpContentsCount; i++) {
            Component comp = getComponent(i);
            if (comp instanceof ESlateMenuItem) {
                ESlateMenuItem item = (ESlateMenuItem) comp;
                Action action = item.getAction();
//System.out.println("item: " + item.getText() + ", action: " + action);
                if (action == dbTable.fieldPropertiesAction) {
                    fieldPropsMItem = item;
                }else if (action == dbTable.addFieldAction) {
                    fieldNewMItem = item;
                }else if (action == dbTable.removeFieldAction) {
                    fieldRemoveMItem = item;
                }else if (action == dbTable.fieldSortAscAction) {
                    fieldSortAscMItem = item;
                }else if (action == dbTable.fieldSortDescAction) {
                    fieldSortDescMItem = item;
                }
            }else if (comp instanceof ESlateCheckMenuItem) {
                currencyFieldMItem = (ESlateCheckMenuItem) comp;
            }
        }
    }

    /**
     * Returns the menu item for the field properties Action.
     * @return
     */
    public ESlateMenuItem getFieldPropertiesItem() {
        return fieldPropsMItem;
    }

    /**
     * Returns the menu item for the new field Action.
     * @return
     */
    public ESlateMenuItem getFieldNewItem() {
        return fieldNewMItem;
    }

    /**
     * Returns the menu item for the field remove Action.
     * @return
     */
    public ESlateMenuItem getFieldRemoveItem() {
        return fieldRemoveMItem;
    }

    /**
     * Returns the menu item for the field sort descending Action.
     * @return
     */
    public ESlateMenuItem getFieldSortAscItem() {
        return fieldSortAscMItem;
    }

    /**
     * Returns the menu item for the field sort ascending Action.
     * @return
     */
    public ESlateMenuItem getFieldSortDescItem() {
        return fieldSortDescMItem;
    }

    /**
     * Returns the menu item for turning the 'currency' aspect of a numeric field on and off.
     * @return
     */
    public ESlateCheckMenuItem geCurrencyFieldMItem() {
        return currencyFieldMItem;
    }

}
