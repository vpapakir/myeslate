/**
 * Created by IntelliJ IDEA.
 * User: Manolis Koutlis
 * Date: Mar 12, 2003
 * Time: 2:47:27 PM
 * To change this template use Options | File Templates.
 */
package gr.cti.eslate.database;

import gr.cti.eslate.eslateMenuBar.ESlatePopupMenu;
import gr.cti.eslate.eslateMenuBar.ESlateCheckMenuItem;
import gr.cti.eslate.eslateMenuBar.ESlateMenuItem;
import gr.cti.eslate.eslateMenuBar.ESlateMenu;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.RenamingForbiddenException;

import javax.swing.*;
import java.awt.*;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

public class DBTablePopupMenu extends ESlatePopupMenu implements Externalizable {
    ESlateMenuItem cutMItem, copyMItem, pasteMItem, iconEditMItem, findMItem, findNextMItem, findPrevMItem;
    ESlateMenuItem addRecMItem, fieldNewMItem2, promoteSelRecsMItem, removeRecMItem, removeSelRecMItem;
    ESlateMenuItem tablePropertiesMItem, tableDescriptionMItem, tablePreferencesMItem;
    ESlateMenu gridChooserMenu;
    /** The DBTable which owns this pop-up menu. */
    DBTable dbTable = null;

    /**
     * Exists only for the Externalization mechanism.
     */
    public DBTablePopupMenu() {
    }

    /**
     * Constucts a DBTablePopupMenu with all the default menus and menu items.
     * @param dbTable The DBTable for which the pop-up menu is created.
     */
    public DBTablePopupMenu(DBTable dbTable) {
        this.dbTable = dbTable;
//        tableEditableItem = new ESlateCheckMenuItem(tableEditableAction);
//        tablePopup.add(tableEditableItem);

        cutMItem = new ESlateMenuItem(dbTable.cutAction);
        cutMItem.setAccelerator((KeyStroke) dbTable.cutAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) cutMItem);

        copyMItem = new ESlateMenuItem(dbTable.copyAction);
        copyMItem.setAccelerator((KeyStroke) dbTable.copyAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) copyMItem);

        pasteMItem = new ESlateMenuItem(dbTable.pasteAction);
        pasteMItem.setAccelerator((KeyStroke) dbTable.pasteAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) pasteMItem);

        addSeparator();

        iconEditMItem = new ESlateMenuItem(dbTable.imageEditAction);
        iconEditMItem.setAccelerator((KeyStroke) dbTable.imageEditAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) iconEditMItem);

        addSeparator();

        findMItem = new ESlateMenuItem(dbTable.findAction);
        findMItem.setAccelerator((KeyStroke) dbTable.findAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(findMItem);

        findNextMItem = new ESlateMenuItem(dbTable.findNextAction);
        findNextMItem.setAccelerator((KeyStroke) dbTable.findNextAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(findNextMItem);

        findPrevMItem = new ESlateMenuItem(dbTable.findPrevAction);
        findPrevMItem.setAccelerator((KeyStroke) dbTable.findPrevAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(findPrevMItem);

        addSeparator();

        addRecMItem = new ESlateMenuItem(dbTable.addRecordAction);
        addRecMItem.setAccelerator((KeyStroke) dbTable.addRecordAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(addRecMItem);

        fieldNewMItem2 = new ESlateMenuItem(dbTable.addFieldAction);
        fieldNewMItem2.setAccelerator((KeyStroke) dbTable.addFieldAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add((JMenuItem) fieldNewMItem2);

        promoteSelRecsMItem = new ESlateMenuItem(dbTable.promoteSelRecsAction);
        promoteSelRecsMItem.setAccelerator((KeyStroke) dbTable.promoteSelRecsAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(promoteSelRecsMItem);

        removeRecMItem = new ESlateMenuItem(dbTable.removeRecordAction);
        removeRecMItem.setAccelerator((KeyStroke) dbTable.removeRecordAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(removeRecMItem);

        removeSelRecMItem = new ESlateMenuItem(dbTable.removeSelRecordAction);
        removeSelRecMItem.setAccelerator((KeyStroke) dbTable.removeSelRecordAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(removeSelRecMItem);

        addSeparator();

        gridChooserMenu = new gr.cti.eslate.eslateMenuBar.ESlateMenu(dbTable.bundle.getString("GridChooser"));
        gridChooserMenu.add(dbTable.getGridChooser());
        add(gridChooserMenu);
//System.out.println("item count 1: " + gridChooserMenu.getItemCount() + ", component count: " + gridChooserMenu.getComponentCount());
        tablePropertiesMItem = new ESlateMenuItem(dbTable.tableInfoAction);
        tablePropertiesMItem.setAccelerator((KeyStroke) dbTable.tableInfoAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(tablePropertiesMItem);

        tableDescriptionMItem = new ESlateMenuItem(dbTable.tableDescriptionAction);
        tableDescriptionMItem.setAccelerator((KeyStroke) dbTable.tableDescriptionAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(tableDescriptionMItem);

        tablePreferencesMItem = new ESlateMenuItem(dbTable.tablePreferencesAction);
        tablePreferencesMItem.setAccelerator((KeyStroke) dbTable.tablePreferencesAction.getValue(AbstractAction.ACCELERATOR_KEY));
        add(tablePreferencesMItem);

        setInvoker(dbTable.jTable);
        pack();
        try{
            getESlateHandle().setUniqueComponentName(dbTable.bundle.getString("DBTable Popup Name"));
        }catch (RenamingForbiddenException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * When a DBTablePopupMenu is restored, this method attaches the menu and menu item variables of this class to
     * the restored menus and menu items.
     * @param dbTable
     */
    void locateMenuItems(DBTable dbTable) {
        this.dbTable = dbTable;
        // If the 'tablePopup' was stored and now restored, then we have to assign the global ESlateMenuItem and
        // ESlateMenu variables of the DBTable that have to do with this menu.
        int popUpContentsCount = getComponentCount();
//System.out.println("popUpContentsCount: " + popUpContentsCount);
        for (int i=0; i<popUpContentsCount; i++) {
            Component comp = getComponent(i);
            if (comp instanceof ESlateMenu) {
                ESlateMenu menu = (ESlateMenu) comp;
                if (menu.getText().equals(dbTable.bundle.getString("GridChooser"))) {
                    gridChooserMenu = menu;
                    gridChooserMenu.add(dbTable.getGridChooser());
//System.out.println("locateMenuItems() Adding GridChooser");
                }
            }else if (comp instanceof ESlateCheckMenuItem) {
                ESlateCheckMenuItem item = (ESlateCheckMenuItem) comp;
                Action action = item.getAction();
                if (action == dbTable.tableEditableAction) {
//                    tableEditableItem = item;
System.out.println("Found tableEditableItem");
                }
            }else if (comp instanceof ESlateMenuItem) {
                ESlateMenuItem item = (ESlateMenuItem) comp;
                Action action = item.getAction();
//System.out.println("item: " + item.getText() + ", action: " + action);
                if (action == dbTable.cutAction) {
                    cutMItem = item;
                }else if (action == dbTable.copyAction) {
                    copyMItem = item;
                }else if (action == dbTable.pasteAction) {
                    pasteMItem = item;
                }else if (action == dbTable.imageEditAction) {
                    iconEditMItem = item;
                }else if (action == dbTable.findAction) {
                    findMItem = item;
                }else if (action == dbTable.findNextAction) {
                    findNextMItem = item;
                }else if (action == dbTable.findPrevAction) {
                    findPrevMItem = item;
                }else if (action == dbTable.promoteSelRecsAction) {
                    promoteSelRecsMItem = item;
                }else if (action == dbTable.addRecordAction) {
                    addRecMItem = item;
                }else if (action == dbTable.addFieldAction) {
                    fieldNewMItem2 = item;
                }else if (action == dbTable.removeRecordAction) {
                    removeRecMItem = item;
                }else if (action == dbTable.removeSelRecordAction) {
                    removeSelRecMItem = item;
                }else if (action == dbTable.tableInfoAction) {
                    tablePropertiesMItem = item;
                }else if (action == dbTable.tableDescriptionAction) {
                    tableDescriptionMItem = item;
                }else if (action == dbTable.tablePreferencesAction) {
                    tablePreferencesMItem = item;
                }
            }
        }
    }

    /**
     * Returns the menu item for the cut Action.
     * @return
     */
    public ESlateMenuItem getCutMItem() {
        return cutMItem;
    }

    /**
     * Returns the menu item for the copy Action.
     * @return
     */
    public ESlateMenuItem getCopyMItem() {
        return copyMItem;
    }

    /**
     * Returns the menu item for the paste Action.
     * @return
     */
    public ESlateMenuItem getPasteMItem() {
        return pasteMItem;
    }

    /**
     * Returns the menu item for the icon edit Action.
     * @return
     */
    public ESlateMenuItem getIconEditMItem() {
        return iconEditMItem;
    }

    /**
     * Returns the menu item for the find Action.
     * @return
     */
    public ESlateMenuItem getFindMItem() {
        return findMItem;
    }

    /**
     * Returns the menu item for the find next Action.
     * @return
     */
    public ESlateMenuItem getFindNextMItem() {
        return findNextMItem;
    }

    /**
     * Returns the menu item for the find previous Action.
     * @return
     */
    public ESlateMenuItem getFindPrevMItem() {
        return findPrevMItem;
    }

    /**
     * Returns the menu item for the add record Action.
     * @return
     */
    public ESlateMenuItem getAddRecMItem() {
        return addRecMItem;
    }

    /**
     * Returns the menu item for the new field Action.
     * @return
     */
    public ESlateMenuItem getFieldNewMItem2() {
        return fieldNewMItem2;
    }

    /**
     * Returns the menu item for the promote selected records Action.
     * @return
     */
    public ESlateMenuItem getPromoteSelRecsMItem() {
        return promoteSelRecsMItem;
    }

    /**
     * Returns the menu item for the remove record Action.
     * @return
     */
    public ESlateMenuItem getRemoveRecMItem() {
        return removeRecMItem;
    }

    /**
     * Returns the menu item for the remove selected records Action.
     * @return
     */
    public ESlateMenuItem getRemoveSelRecMItem() {
        return removeSelRecMItem;
    }

    /**
     * Returns the menu item for the table properties Action.
     * @return
     */
    public ESlateMenuItem getTablePropertiesMItem() {
        return tablePropertiesMItem;
    }

    /**
     * Returns the menu item for the table description Action.
     * @return
     */
    public ESlateMenuItem getTableDescriptionMItem() {
        return tableDescriptionMItem;
    }

    /**
     * Returns the menu item for the table preferences Action.
     * @return
     */
    public ESlateMenuItem getTablePreferencesMItem() {
        return tablePreferencesMItem;
    }

    /**
     * Returns the menu item for the grid chooser Action.
     * @return
     */
    public ESlateMenu getGridChooserMenu() {
        return gridChooserMenu;
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        if (gridChooserMenu != null) {
            gridChooserMenu.removeAll();
//System.out.println("writeExternal() removing GridChooser");
        }
        super.writeExternal(objectOutput);
        if (gridChooserMenu != null) {
            gridChooserMenu.add(dbTable.getGridChooser());
//System.out.println("writeExternal() adding GridChooser");
        }
    }

}
