package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.ObjectBaseArray;

import java.awt.Component;
import java.awt.Font;
import java.io.Externalizable;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.MenuElement;
import javax.swing.plaf.UIResource;
import javax.swing.tree.DefaultMutableTreeNode;


public class ESlatePopupMenu extends JPopupMenu implements ESlatePart, Externalizable{

    private ESlateHandle handle;

    //FIXME
    private static final int FORMAT_VERSION = 2;
    static final long serialVersionUID = -8312L;
    private final static String version = "1.0.2";

    JComponent owner ;

    public ESlatePopupMenu(){
    }
/*
    public ESlatePopupMenu(JComponent owner) {
        this.owner = owner;
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateMenuBar.PopupBundleMessages", Locale.getDefault());
    }
*/
    /**
     * Returns the component's handle
     * @return handle The ESlateHandle
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            handle = ESlate.registerPart(this);
            try {
                handle.setUniqueComponentName("ESlatePopupMenu");
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.setInfo(getInfo());
        }
        return handle;
    }

    public MenuItemNode getMenuStructure() {
        MenuItemNode root = new MenuItemNode();
        root.setUserObject(this);
        reconstructTreeStructure(root, this);
        return root;
    }

    public void setMenuStructure(MenuItemNode root) {
        removeAll();
        createMenusFromTree(root,this);
        invalidate();
        doLayout();
        revalidate();
    }

    /**
     * This method creates menus and menuItems from a JTree's second level nodes
     * using the tree structure below the second level. It is used by setInitialMenus which
     * finds all the first-level nodes of a JTree and creates the top level menus of the menubar.
     * When all top level menus have been found createMenusFromTree is called to create the sub menu structure
     * under the top level menu. As soon as the method finds new menus it calls itself recursively
     * until it discovers the entire tree structure and adds the menus and menuItems to the right places
     * @param node The DefaultMutableTreeNode under which the tree structure will be discovered
     * @param menu The menu where the sub menus and menuitems representing the discovered structure will be added
     */
    private void createMenusFromTree(DefaultMutableTreeNode node, JComponent menu) {
        //menu.menuEntries.clear();
        DefaultMutableTreeNode child;
        JMenuItem menuitem;
        ESlateMenu newmenu;
        int k = node.getChildCount();
        if (menu instanceof ESlateMenu)
            ((ESlateMenu) menu).menuEntries.clear();
        for (int i = 0; i < k; i++) {
            child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (child instanceof CustomSeparator) {
                if (menu instanceof ESlateMenu)
                    ((ESlateMenu) menu).add(new JSeparator());
                else // menu is the popupMenu so use addSeparator
                    ((JPopupMenu) menu).addSeparator();
            } else if (child.isLeaf() == true) {
                if (child.getUserObject() instanceof ESlateMenuItem) {
                    menuitem = (ESlateMenuItem) child.getUserObject();
                } else if (child.getUserObject() instanceof ESlateCheckMenuItem) {
                    menuitem = (ESlateCheckMenuItem) child.getUserObject();
                } else {
                    if (child instanceof CheckMenuItemNode)
                        menuitem = new ESlateCheckMenuItem(child.toString());
                    else{
                        menuitem = new ESlateMenuItem(child.toString());
                    }
                }

                if (menu instanceof ESlateMenu){
                    ((ESlateMenu) menu).add(menuitem);
                }else
                    menu.add(menuitem);
            } else {
                if (child.getUserObject() instanceof ESlateMenu){
                    newmenu = (ESlateMenu) child.getUserObject();
                }else{
                    newmenu = new ESlateMenu(child.toString());
                }
                if (menu instanceof ESlateMenu)
                    ((ESlateMenu)menu).add(newmenu);
                else
                    menu.add(newmenu);
                createMenusFromTree(child, newmenu);
            }
        }
    }
    /*
    private void printPath(String[] path) {
        System.out.print("Selected path: ");
        int l = 0;

        if (path != null) l = path.length;
        for (int i = 0; i < l; i++)
            System.out.print(path[i] + "-->");
        System.out.println();
    }
    */

/**********************************************************************************/
/*                      Internal methods below this point                         */
/**********************************************************************************/

    /**
     * Returns Copyright information.
     * @return	The Copyright information.
     */
    private ESlateInfo getInfo() {
        String[] info = {
                "part",
                "design",
                "development",
                "copyright"
            };

        return new ESlateInfo(
                "componentName" + " " +
                "version" + " " + version,
                info);
    }



    /**
     * This method reconstructs a JTree's subnode structure (second level and below)
     * using the menus and items below the menubar's top level menus It is used by reconstructTree which
     * finds all the first-level menus of the menubar and creates the top level nodes of the JTree.
     * When all top level nodes have been created reconstructSubNodes is called to create the sub node structure
     * under the top level nodes. As soon as the method finds new menus it calls itself recursively
     * until it discovers the entire menu structure and adds the nodes to the right places on the JTree
     * @param menu The ESlateMenu under which the menu structure will be discovered
     * @param node The tree node where the sub nodes representing the discovered menu structure will be added
     */

    void reconstructTreeStructure(MenuItemNode node, JComponent menuComponent) {
        try{
        if (menuComponent instanceof ESlateMenu){
            ESlateMenu menu = (ESlateMenu) menuComponent;
            for (int j = 0; j < menu.menuEntries.size(); j++) {
                if (menu.menuEntries.get(j) instanceof ESlateMenu) {
                    MenuItemNode subnode = new MenuItemNode();
                    subnode.setUserObject(menu.menuEntries.get(j));
                    node.add(subnode);
                    reconstructTreeStructure(subnode, (ESlateMenu) menu.menuEntries.get(j));
                } else if (menu.menuEntries.get(j) instanceof ESlateMenuItem){
                    MenuItemNode leaf = new MenuItemNode();
                    leaf.setUserObject(menu.menuEntries.get(j));
                    node.add(leaf);
                } else if (menu.menuEntries.get(j) instanceof ESlateCheckMenuItem) {
                    CheckMenuItemNode leaf = new CheckMenuItemNode();
                    leaf.setUserObject(menu.menuEntries.get(j));
                    node.add(leaf);
                } else {
                    CustomSeparator separator = new CustomSeparator("<<Separator>>");
                    node.add(separator);
                }
            }
        }else{
            ESlatePopupMenu menu = (ESlatePopupMenu) menuComponent;
            for (int j = 0; j < menu.getComponents().length; j++) {
                if (menu.getComponent(j) instanceof ESlateMenu) {
                    MenuItemNode subnode = new MenuItemNode();
                    subnode.setUserObject(menu.getComponent(j));
                    node.add(subnode);
                    reconstructTreeStructure(subnode, (ESlateMenu) menu.getComponent(j));
                } else if (menu.getComponent(j) instanceof ESlateMenuItem){
                    MenuItemNode leaf = new MenuItemNode();
                    leaf.setUserObject(menu.getComponent(j));
                    node.add(leaf);
                } else if (menu.getComponent(j) instanceof ESlateCheckMenuItem) {
                    CheckMenuItemNode leaf = new CheckMenuItemNode();
                    leaf.setUserObject(menu.getComponent(j));
                    node.add(leaf);
                } else {
                    CustomSeparator separator = new CustomSeparator("<<Separator>>");
                    node.add(separator);
                }
            }
        }
        }catch( Throwable thr){
            thr.printStackTrace();
        }
    }

    /**
     * Reads from ESlateFieldMap to restore stored values and properties
     */


    /**
     * Reads from ESlateFieldMap to restore stored values and properties
     */

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {

        Object firstObj = in.readObject();
        StorageStructure fieldMap = (StorageStructure) firstObj;
        int dataVersionID = fieldMap.getDataVersionID();
        if (dataVersionID == 2){
            readExternal2(fieldMap);
            return;
        }


        if (fieldMap.containsKey("SubMenuElements")) {
            try {
                Object[] objectArray = (Object[]) fieldMap.get("SubMenuElements", (Object) null);
                for (int i = 0; i < objectArray.length; i++) {
                    if (objectArray[i] instanceof ESlateMenu) {
                        add((ESlateMenu) objectArray[i]);
                    } else if (objectArray[i] instanceof ESlateMenuItem){
                        add((ESlateMenuItem) objectArray[i]);
                    } else if (objectArray[i] instanceof ESlateCheckMenuItem){
                        add((ESlateCheckMenuItem) objectArray[i]);
                    } else {
                        addSeparator();
                    }

                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }
        ESlateHandle parentHandle = getESlateHandle().getParentHandle();
        if (parentHandle != null){
            owner = (JComponent) parentHandle.getComponent();
        }else
            System.out.println("PARENTHANDLE is null!, owner should be set");

        /* Last but not least, bing actions of owner to ESlateMenuItems using the
         * stored in ESlateMenuItems actionNames
         */

        bindActionsWithMenuItems();

      // The following code is used for debuggin purposes only

        //System.out.println("Start of ACTION BINDINGS LISTING");
        //listActionBindings(this);
        //System.out.println("End of ACTION BINDINGS LISTING");

    }


    public void readExternal2(StorageStructure fieldMap) {
        if (fieldMap.containsKey("SubMenuElements")) {
            ESlateMenu menu;
            ESlateCheckMenuItem checkMenuItem;
            ESlateMenuItem menuItem;
            try {
                Object[] objectArray = (Object[]) fieldMap.get("SubMenuElements", (Object) null);
                for (int i = 0; i < objectArray.length; i++) {
                    if (objectArray[i] instanceof String) {
                        add(new JSeparator());
                    } else {
                        if (((StorageStructure) objectArray[i]).get("IsMenu", isMenu())) {
                            menu = new ESlateMenu();
                            add(menu);
                            menu.applyState((StorageStructure) objectArray[i]);
                        } else {
                            if (((StorageStructure) objectArray[i]).get("IsCheckMenuItem", isCheckMenuItem())){
                                checkMenuItem = new ESlateCheckMenuItem();
                                add(checkMenuItem);
                                checkMenuItem.applyState((StorageStructure) objectArray[i]);
                            } else {
                                menuItem = new ESlateMenuItem();
                                add(menuItem);
                                menuItem.applyState((StorageStructure) objectArray[i]);

                            }
                        }
                    }
                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }

        }

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }
        ESlateHandle parentHandle = getESlateHandle().getParentHandle();
        if (parentHandle != null){
            owner = (JComponent) parentHandle.getComponent();
        }else
            System.out.println("PARENTHANDLE is null!, owner should be set");

        /* Last but not least, bing actions of owner to ESlateMenuItems using the
         * stored in ESlateMenuItems actionNames
         */

        bindActionsWithMenuItems();

      // The following code is used for debuggin purposes only

        //System.out.println("Start of ACTION BINDINGS LISTING");
        //listActionBindings(this);
        //System.out.println("End of ACTION BINDINGS LISTING");

    }

    /**
     * Writes to ESlateFieldMap values and properties to be stored
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        Component[] subElements = getComponents();
        Object[] objectArray = new Object[subElements.length];

        for (int i = 0; i < subElements.length; i++) {
            if (subElements[i] instanceof ESlateMenuItem) {
                objectArray[i] = ((ESlateMenuItem) subElements[i]).recordState();
            } else if (subElements[i] instanceof ESlateCheckMenuItem) {
                objectArray[i] = ((ESlateCheckMenuItem) subElements[i]).recordState();
            } else if (subElements[i] instanceof ESlateMenu) {
                objectArray[i] = ((ESlateMenu) subElements[i]).recordState();
            } else
                objectArray[i] = "separator";
        }

        fieldMap.put("SubMenuElements", objectArray);

        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());

        fieldMap.put("BorderPainted", isBorderPainted());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("Enabled", isEnabled());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());

        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());

        fieldMap.put("Margin", getMargin());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

        if (getBorder() != null && !(getBorder() instanceof UIResource)) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        } else if (getBorder() == null) {
            try {
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);

                fieldMap.put("Border", bd);
            } catch (Throwable thr) {}
        }

        out.writeObject(fieldMap);
    }

    void bindActionsWithMenuItems(){
        if (owner != null){
            ActionMap actionMap = owner.getActionMap();
            if (actionMap != null && actionMap.allKeys() != null){
              int ownerRegisteredActionsNum = actionMap.allKeys().length;
              for (int i = 0; i<ownerRegisteredActionsNum;i++)
                  bindAction(actionMap.allKeys()[i], actionMap.get(actionMap.allKeys()[i]), this);
            }
        }
    }
/*
    void listActionBindings(MenuElement c){

      if (c instanceof ESlatePopupMenu){
            MenuElement[] subElements = c.getSubElements();
            for (int i = 0;i<subElements.length;i++){
                if (subElements[i] instanceof ESlateMenuItem){
                    System.out.println("----------------");
                    System.out.println(((ESlateMenuItem) subElements[i]).getText()+":");
                    System.out.println("Registered action name : "+((ESlateMenuItem) subElements[i]).getActionName());
                    System.out.println("Registered action : "+((ESlateMenuItem) subElements[i]).getAction());
                }else if (subElements[i] instanceof ESlateMenu)
                    listActionBindings(subElements[i]);

            }
        }else{
            ObjectBaseArray subElements = ((ESlateMenu) c).menuEntries;
            for (int i = 0;i<subElements.size();i++){

                if (subElements.get(i) instanceof ESlateMenuItem){
                    System.out.println("----------------");
                    System.out.println(((ESlateMenuItem) subElements.get(i)).getText()+":");
                    System.out.println("Registered action name : "+((ESlateMenuItem) subElements.get(i)).getActionName());
                    System.out.println("Registered action : "+((ESlateMenuItem) subElements.get(i)).getAction());
                    System.out.println("----------------");
                }else if (subElements.get(i) instanceof ESlateMenu)
                    listActionBindings((MenuElement) subElements.get(i));

            }
        }

    }
*/
    void bindAction(Object o, Action action, MenuElement c){
        /* Must check back here to see if we can find a way to reduce the code length
         * in this method
         */

        if (c instanceof ESlatePopupMenu){
            MenuElement[] subElements = c.getSubElements();
            for (int i = 0;i<subElements.length;i++){
                if (subElements[i] instanceof ESlateMenuItem){
                    if (((ESlateMenuItem) subElements[i]).getActionName() != null &&
                        ((ESlateMenuItem) subElements[i]).getActionName().equals((String) o)){
                        // The known menuitem bug : when setting an action, text property disappears...
                        // So the follownig workaround is nessesary...

                        String text = ((ESlateMenuItem) subElements[i]).getText();
                        ((ESlateMenuItem) subElements[i]).setAction(action);
                        ((ESlateMenuItem) subElements[i]).setText(text);
                        //break;
                    }
                }else if (subElements[i] instanceof ESlateCheckMenuItem){
                    ESlateCheckMenuItem checkItem = (ESlateCheckMenuItem) subElements[i];
                    if (checkItem.getActionName() != null &&
                        checkItem.getActionName().equals((String) o)){
                        // The known menuitem bug : when setting an action, text property disappears...
                        // So the follownig workaround is nessesary...

                        String text = checkItem.getText();
                        boolean enabled = checkItem.isEnabled();
                        checkItem.setAction(action);
                        checkItem.setText(text);
                        checkItem.setEnabled(enabled);
                        //break;
                    }
                }else if (subElements[i] instanceof ESlateMenu)
                    bindAction(o, action, subElements[i]);

            }
        }else{
            ObjectBaseArray subElements = ((ESlateMenu) c).menuEntries;
            for (int i = 0;i<subElements.size();i++){

                if (subElements.get(i) instanceof ESlateMenuItem){
                    if (((ESlateMenuItem) subElements.get(i) ).getActionName() != null &&
                        ((ESlateMenuItem) subElements.get(i) ).getActionName().equals((String) o)){
                        // The known menuitem bug : when setting an action, text property disappears...
                        // So the follownig workaround is nessesary...

                        String text = ((ESlateMenuItem) subElements.get(i)).getText();
                        ((ESlateMenuItem) subElements.get(i)).setAction(action);
                        ((ESlateMenuItem) subElements.get(i)).setText(text);
                        //break;
                    }
                }else if (subElements.get(i) instanceof ESlateCheckMenuItem){
                    ESlateCheckMenuItem checkItem = (ESlateCheckMenuItem) subElements.get(i);
                    if ( checkItem.getActionName() != null &&
                        checkItem.getActionName().equals((String) o)){
                        // The known menuitem bug : when setting an action, text property disappears...
                        // So the follownig workaround is nessesary...

                        String text = checkItem.getText();
                        boolean enabled = checkItem.isEnabled();
                        checkItem.setAction(action);
                        checkItem.setEnabled(enabled);
                        checkItem.setText(text);
                        //break;
                    }
                }else if (subElements.get(i) instanceof ESlateMenu)
                    bindAction(o, action, (MenuElement) subElements.get(i));

            }
        }
    }

    public boolean isMenu() {
        return false;
    }


    public boolean isCheckMenuItem() {
        return false;
    }
}