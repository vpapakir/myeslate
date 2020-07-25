package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.plaf.UIResource;
import javax.swing.tree.DefaultMutableTreeNode;


public class ESlateMenuBar extends JMenuBar implements ESlatePart, Externalizable {

    private ESlateHandle handle;
    private static final int FORMAT_VERSION = 2;
    int scrollSpeed = 200;
    PathChangedEventMulticaster pathChangedListener = new PathChangedEventMulticaster();
    JMenuItem oldSelection;
    public String[] reverseList;
    static final long serialVersionUID = -8268553931984670312L;
    private static ResourceBundle bundleMessages;
    boolean enabled = true;
    boolean menuItemCanSentPathEventAgain = false;
    private final static String version = "2.0.4";

    /**
     * Timer which measures the time required for loading the state of the
     * component.
     */
    PerformanceTimer loadTimer;

    /**
     * Timer which measures the time required for saving the state of the
     * component.
     */
    PerformanceTimer saveTimer;

    /**
     * Timer which measures the time required to construct the component.
     */
    PerformanceTimer constructorTimer;

    /**
     * Timer which measures the time required for starting the E-Slate aspect
     * of the component.
     */
    PerformanceTimer initESlateAspectTimer;

    /**
     * Timer which measures the time it takes to populate the MenuSystem.
     */
    PerformanceTimer setMenuStructureTimer;

    /**
     * The listener that notifies about changes to the state of the
     * Performance Manager.
     */
    PerformanceListener perfListener = null;

    /**

     * Constructs a new ESlateMenuSystem (MenuBar)
     */

    public ESlateMenuBar() {
        super();
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateMenuBar.BundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);

        setOpaque(true);
        setBorder(new NoTopOneLineBevelBorder(0));
        setPreferredSize(new Dimension(400, 25));
        pm.constructionEnded(this);
        pm.stop(constructorTimer);
        pm.displayTime(constructorTimer, bundleMessages.getString("ConstructorTimer"), "ms");
    }

    /**
     * Returns the component's handle
     * @return handle The ESlateHandle
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();
            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);
            handle = ESlate.registerPart(this);
            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        for (int i = getMenuCount() - 1; i >= 0; i--) {
                            ESlateMenu menu = (ESlateMenu) getMenu(i);
                            disposeMenuStructure(menu);
                            ESlateMenuBar.this.remove(menu);
                            menu.menuBar = null;
                        }
                        pathChangedListener.clear();
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();
                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );
            

            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateMenuBar"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.MenuBarPrimitives");
            handle.setInfo(getInfo());
            pm.eSlateAspectInitEnded(this);
            pm.stop(initESlateAspectTimer);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    public MenuItemNode getMenuStructure() {
        return reconstructStructure();
    }

    public void setMenuStructure(MenuItemNode root) {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(setMenuStructureTimer);
        removeAll();
        setInitialMenus(root);
        makeMenuBarKnownToAll(this);
        repaint();
        revalidate();
        pm.stop(setMenuStructureTimer);
        pm.displayTime(setMenuStructureTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Reads from the ESlateFieldMap to restore stored values and properties
     */

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(loadTimer);
        Object firstObj = in.readObject();
//        ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
        StorageStructure fieldMap = (StorageStructure) firstObj;
        ESlateMenu menu;
        if (fieldMap.containsKey("SubElements")) {
            if (fieldMap.getDataVersionID() == 2) {
                try {
                    System.out.println("MenuSystem Uses 2.0 StoreVersion");
                    Object[] objectArray = (Object[]) fieldMap.get("SubElements", (Object) null);
                    for (int i = 0; i < objectArray.length; i++) {
                        menu = new ESlateMenu(this);
                        menu.applyState((StorageStructure) objectArray[i]);
                        add(menu);
                    }
                    makeMenuBarKnownToAll(this);
                } catch (Throwable thr) {
                    thr.printStackTrace();
                }
            } else {
                if (fieldMap.getDataVersion().equals("2.0")){
                    try{
                        System.out.println("MenuSystem Uses 2.0 StoreVersion, but this is an older fieldmap");
                        Object[] objectArray = (Object[]) fieldMap.get("SubElements", (Object) null);
                        for (int i = 0; i < objectArray.length; i++) {
                            menu = new ESlateMenu(this);
                            menu.applyState((StorageStructure) objectArray[i]);
                            add(menu);
                        }
                        makeMenuBarKnownToAll(this);
                    } catch (Throwable thr) {
                        thr.printStackTrace();
                    }
                }else{
                    try {
                        System.out.println("MenuSystem Uses 1.0 StoreVersion");
                        Object[] objectArray = (Object[]) fieldMap.get("SubElements", (Object) null);
                        for (int i = 0; i < objectArray.length; i++) {
                            add((ESlateMenu) objectArray[i]);
                        }
                        makeMenuBarKnownToAll(this);
                    } catch (Throwable thr) {
                        thr.printStackTrace();
                    }
                }
            }
        }
        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));
        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));
        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setRequestFocusEnabled(fieldMap.get("RequestFocusEnabled", isRequestFocusEnabled()));
        setVisible(fieldMap.get("Visible", isVisible()));
        setAutoscrolls(fieldMap.get("Autoscrolls", getAutoscrolls()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        setMargin((Insets) fieldMap.get("Margin", getMargin()));
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));
        setLocation((Point) fieldMap.get("Location", getLocation()));
        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));
        setScrollSpeed(fieldMap.get("ScrollSpeed", getScrollSpeed()));
        setItemCapableOfMultiplePathChangedEvents(fieldMap.get("SendPathEventsAgain", getItemCapableOfMultiplePathChangedEvents()));
        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");
                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }
        pm.stop(loadTimer);
        pm.displayElapsedTime(loadTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Writes to the ESlateFieldMap values and properties to be stored
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        pm.init(saveTimer);
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        Object[] objectArray = new Object[getMenuCount()];

        if (fieldMap.getDataVersionID() == 2) {
            for (int i = 0; i < getMenuCount(); i++) {
                objectArray[i] = ((ESlateMenu) getMenu(i)).recordState();
            }
        } else {
            if (fieldMap.getDataVersion().equals("2.0")){
                for (int i = 0; i < getMenuCount(); i++) {
                    objectArray[i] = ((ESlateMenu) getMenu(i)).recordState();
                }
            }else{
                for (int i = 0; i < getMenuCount(); i++) {
                    objectArray[i] = (ESlateMenu) getMenu(i);
                }
            }
        }
        fieldMap.put("SubElements", objectArray);
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());
        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());
        fieldMap.put("BorderPainted", isBorderPainted());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("RequestFocusEnabled", isRequestFocusEnabled());
        fieldMap.put("Visible", isVisible());
        fieldMap.put("Autoscrolls", getAutoscrolls());
        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        fieldMap.put("Margin", getMargin());
        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());
        fieldMap.put("Location", getLocation());
        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());
        fieldMap.put("ScrollSpeed", getScrollSpeed());
        fieldMap.put("SendPathEventsAgain", getItemCapableOfMultiplePathChangedEvents());
        JMenuBar test = new JMenuBar();
        if (getBorder() != null && !getBorder().equals(test.getBorder())) {
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
        pm.stop(saveTimer);
        pm.displayElapsedTime(saveTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Returns a JMenuItem that is at the end of the given path.If the path given is invalid
     * then null value is returned
     * . The path must not start from the menubar, but from the top level menu that is the ansestor
     * of the JMenuItem we want to get.
     * @param path The path to the item given in string array
     * @deprecated Use getLastPathComponent(int[] path instead)
     */

    public JMenuItem getLastPathComponent(String[] path) {
        boolean found = false;
        JMenuItem nextItem = null;
        JMenuItem menu = getMenu(path[1]);
        if (menu == null)
            return null;
        if (path.length == 2) {
            found = true;
            nextItem = menu;
        }
        for (int i = 2; i < path.length; i++) {
            found = false;
            nextItem = getSubMenuItem(menu, path[i]);
            menu = nextItem;
            if (nextItem == null)
                found = false;
            else {
                found = true;
            }
        }
        if (found == true) {
            return nextItem;
        } else
            return null;
    }

    public Component getLastPathComponent(int[] path) {
        if (path.length == 0 || path == null)
            return null;
        Component nextItem = null;
        JMenuItem menu = getMenu(path[0]);
        if (path.length == 1)
            return menu;
        boolean found = false;
        for (int i = 1; i < path.length; i++) {
            found = false;
            nextItem = getSubMenuItem(menu, path[i]);
            if (nextItem instanceof JSeparator && i == path.length - 1)
                return nextItem;
            menu = (JMenuItem) nextItem;
            if (nextItem == null)
                found = false;
            else {
                found = true;
            }
        }
        if (found == true) {
            return nextItem;
        } else
            return null;
    }

    /**
     * Returns a JMenu that is one of the top level menus of the menubar. If the name used to find the menu is
     * invalid then null is returned.
     * @param name The name of the JMenu we want to get.
     */

    public JMenu getMenu(String name) {
        JMenu returnMenu = null;
        for (int i = 0; i < getMenuCount(); i++) {
            if (getMenu(i).getText().equals(name)) {
                returnMenu = getMenu(i);
                break;
            }
        }
        return returnMenu;
    }

    /**
     * Returns a JMenuItem that is a child of a specified JMenuItem. If the name used to find the menuItem is
     * invalid then null is returned.
     * @param item The JMenuItem that is the parent of the item we are looking for.
     * @param childName The name of the JMenuItem we want to get.
     */

    JMenuItem getSubMenuItem(JMenuItem item, String childName) {
        JMenuItem returnItem = null;
        if (item instanceof ESlateMenu) {
            for (int i = 0; i < ((ESlateMenu) item).menuEntries.size(); i++) {
                if (((JMenuItem) ((ESlateMenu) item).menuEntries.get(i)).getText().equals(childName)) {
                    returnItem = (JMenuItem) ((ESlateMenu) item).menuEntries.get(i);
                    break;
                }
            }
            return returnItem;
        } else {

            if (item.getText().equals(childName))
                return item;
            else return null;
        }
    }

    Component getSubMenuItem(Component item, int position) {
        if (item instanceof ESlateMenu)
            return ((ESlateMenu) item).getMenuComponent(position);
        else return null;
    }

    /**
     * Adds a new PathChangedListener
     * @param l The new PathChangedListener
     */


    public void addPathChangedListener(PathChangedListener l) {
        if (pathChangedListener != null)
            pathChangedListener.add(l);
    }

    /**
     * Removes a PathChangedListener
     * @param l The PathChangedListener to be removed
     */

    public void removePathChangedListener(PathChangedListener l) {
        if (pathChangedListener != null)
            pathChangedListener.remove(l);
    }

    /**
     * Fires a pathChangedEvent
     * @param selectedItem The JMenuItem that was selected (and caused the selected path to change)
     * @deprecate use setPath(JMenuItem selectedItem) instead
     */

    void pathHasChanged(JMenuItem selectedItem) {
        if (pathChangedListener != null && (oldSelection != selectedItem || menuItemCanSentPathEventAgain)) {
            oldSelection = selectedItem;
            ArrayList path = new ArrayList();
            discoverItemStringPath(selectedItem, path);
            int l = 0;
            if (path != null) l = path.size();
            reverseList = new String[l];
            int counter = 0;
            for (int i = l - 1; i >= 0; i--) {
                reverseList[counter++] = (String) path.get(i);
            }
            //printPath(reverseList);
            firePathHasChanged(reverseList, selectedItem);
        }
    }

    void firePathHasChanged(String[] path, JMenuItem selectedItem) {
        PathChangedEvent e = new PathChangedEvent(this, PathChangedEvent.PATH_CHANGED, path, selectedItem);
        pathChangedListener.pathChanged(e);
    }

    public void setSelectionPath(int[] path) {
        Component item = getLastPathComponent(path);
        if (item != null || !(item instanceof JSeparator)) {
            firePathHasChanged(null, (JMenuItem) item);
        }
    }

    /**
     * Sets the current selected path
     * @param path The selected path to change)
     * @deprecated use setSelectionPath(int[] path) instead
     */

    public void setSelectionPath(String[] path) {
        //printPath(path);
        String[] reformPath = new String[path.length + 1];
        reformPath[0] = " ";
        for (int i = 0; i < path.length; i++)
            reformPath[i + 1] = path[i];
        //printPath(reformPath);
        Component item = getLastPathComponent(reformPath);
        if (item != null && !(item instanceof JSeparator) && item != oldSelection) {
            firePathHasChanged(path, (JMenuItem) item);
        }
    }

    /**
     * Fires a pathChangedEvent
     * @param path The selected path to change)
     * @deprecated use setPath(String[] path) instead
     */

    public void pathHasChanged(String[] path) {
        //printPath(path);
        firePathHasChanged(path, null);
    }

    public void setItemCapableOfMultiplePathChangedEvents(boolean b) {
        menuItemCanSentPathEventAgain = b;
    }

    public boolean getItemCapableOfMultiplePathChangedEvents() {
        return menuItemCanSentPathEventAgain;
    }

    private void printPath(String[] path) {
        //         MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();

        System.out.print("Selected path: ");
        int l = 0;

        if (path != null) l = path.length;
        for (int i = 0; i < l; i++)
            System.out.print(path[i] + "-->");
        System.out.println();
    }

    /**
     * This method is used to make the menuBar known to the sub JMenuItems
     * It uses makeMenuBarKnownToSubElements for the menuItems under the second level
     * @param menuBar The ESlateMenuBar
     */

    private void makeMenuBarKnownToAll(ESlateMenuBar menuBar) {
        int k = getMenuCount();
        ESlateMenu menu;
        for (int i = 0; i < k; i++) {
            menu = (ESlateMenu) getMenu(i);
            menu.setMenuBar(this);
            if (menu.getMenuComponentCount() != 0)
                makeMenuBarKnownToSubElements(menu);
        }
    }

    /**
     * This method is used to make the menuBar known to the sub JMenuItems of a top level menu
     * @param menu The ESlateMenu under which the menubar is going to be "known"
     */

    private void makeMenuBarKnownToSubElements(ESlateMenu menu) {
        int k = menu.getMenuComponentCount();
        JMenuItem item;
        for (int i = 0; i < k; i++) {
            item = (JMenuItem) menu.getMenuComponent(i);
            if (item instanceof ESlateMenu) {
                makeMenuBarKnownToSubElements((ESlateMenu) item);
                ((ESlateMenu) item).setMenuBar(this);
            } else if (item instanceof ESlateMenuItem) {
                ((ESlateMenuItem) item).setMenuBar(this);
            }
        }
    }

    /**
     * This method is used to "fill" an array with the path to the specified JMenuItem
     * If the component given is not a JMenuItem null array is returned
     * @param component The Component to be "examined", and whose the path will de found
     * @param array The array which is going to be the path to the component.
     */

    void discoverItemStringPath(Component component, ArrayList array) {
        if (component == null || array == null) return;

        if (component.getClass() != ESlateMenuBar.class) {
            Component parent = null;

            if (JPopupMenu.class.isAssignableFrom(component.getClass()))
                parent = ((JPopupMenu) component).getInvoker();
            else {
                parent = component.getParent();
                array.add(component.getName());
            }
            discoverItemStringPath(parent, array);
        }
    }

    /**
     * This method adds an item to a menuItem at a specific position. If the item is not a menu the it
     * is removed and a menu with the same name replaces it.
     * If the path is invalid then nothing is added to nothing and a n error message appears at the console.
     * The path must not start from the menubar, but from the top level menu that is the ansestor
     * of the JMenuItem we want to get to.
     * @param menuitem  The name of the menuItem to be added.
     * @param path The path to the parent item where the item will be added.
     * @param position The position at which the new item will be added.
     * @deprecated Use addItem(String newItemName,int[] path,int position) instead
     */

    public void addItem(String newItemName, String[] path, int position) {

        String[] nodesToLastPath = new String[path.length + 1];

        nodesToLastPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();
        for (int i = 1; i < path.length + 1; i++) {
            nodesToLastPath[i] = path[i - 1];
        }

        String[] nodesToPreviousPath = new String[path.length];

        nodesToPreviousPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();
        for (int i = 1; i < path.length; i++) {
            nodesToPreviousPath[i] = path[i - 1];
        }
        try {
            JMenuItem lastNode = getLastPathComponent(nodesToLastPath);

            if (lastNode == null) {
                throw new Exception("No such path found ");
            } else {
                if (lastNode instanceof ESlateMenu) {
                    if (position > ((ESlateMenu) lastNode).menuEntries.size())
                        ((ESlateMenu) lastNode).add(new ESlateMenuItem(newItemName, this));
                    else
                        ((ESlateMenu) lastNode).insert(new ESlateMenuItem(newItemName, this), position);
                } else {
                    int previousPosition = 0;
                    String text = lastNode.getText();
                    ESlateMenu previousNode = (ESlateMenu) getLastPathComponent(nodesToPreviousPath);

                    for (int i = 0; i < previousNode.getItemCount(); i++) {
                        if (previousNode.getItem(i).getText().equals(text)) {
                            previousPosition = i;
                            break;
                        }
                    }
                    previousNode.remove(lastNode);
                    ESlateMenu newMenu = new ESlateMenu(text, this);

                    newMenu.add(new ESlateMenuItem(newItemName, this));
                    previousNode.insert(newMenu, previousPosition);
                }

            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }

    public void addItem(String newItemName, int[] path, int position) {

        if (path.length == 0 || path == null) {
            add(new ESlateMenu(newItemName, this), position);
            return;
        }
        int[] nodesToPreviousPath = new int[path.length - 1];

        for (int i = 0; i < path.length - 1; i++) {
            nodesToPreviousPath[i] = path[i];
        }
        try {
            Component lastNode = getLastPathComponent(path);

            if (lastNode == null) {
                throw new Exception("No such path found ");
            } else {
                if (lastNode instanceof ESlateMenu) {
                    if (position > ((ESlateMenu) lastNode).menuEntries.size())
                        ((ESlateMenu) lastNode).add(new ESlateMenuItem(newItemName, this));
                    else
                        ((ESlateMenu) lastNode).insert(new ESlateMenuItem(newItemName, this), position);
                } else if (lastNode instanceof ESlateMenuItem || lastNode instanceof ESlateCheckMenuItem) {
                    int previousPosition = 0;
                    String text = ((JMenuItem) lastNode).getText();
                    ESlateMenu previousNode = (ESlateMenu) getLastPathComponent(nodesToPreviousPath);

                    previousPosition = path[path.length - 1];
                    previousNode.remove(previousPosition);
                    ESlateMenu newMenu = new ESlateMenu(text, this);

                    newMenu.add(new ESlateMenuItem(newItemName, this));
                    previousNode.insert(newMenu, previousPosition);
                }
            }
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        repaint();
        revalidate();
    }

    /**
     * This method deletes an item
     * If the path is invalid then nothing is deleted from nothing and an error message appears at the console.
     * The path must not start from the menubar, but from the top level menu that is the ansestor
     * of the JMenuItem we want to get to.
     * @param path The path to the item that will be deleted.
     * @deprecated Use deleteItem(int[] path) instead
     */


    public void deleteItem(String[] path) {
        String[] nodesToLastPath = new String[path.length + 1];

        nodesToLastPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();

        for (int i = 1; i < path.length + 1; i++) {
            nodesToLastPath[i] = path[i - 1];
        }

        String[] nodesToPreviousPath = new String[path.length];

        nodesToPreviousPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();
        for (int i = 1; i < path.length; i++) {
            nodesToPreviousPath[i] = path[i - 1];
        }
        String[] nodesToPreviousPreviousPath = null;

        if (nodesToPreviousPath.length > 1) {
            nodesToPreviousPreviousPath = new String[path.length - 1];
            nodesToPreviousPreviousPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();
            for (int i = 1; i < path.length - 1; i++) {
                nodesToPreviousPreviousPath[i] = path[i - 1];
            }
        }

        JMenuItem lastNode = getLastPathComponent(nodesToLastPath);

        try {
            if (lastNode == null) {
                throw new Exception("No such path found. No deletion occured! ");
            } else {
                if (nodesToPreviousPath.length == 1) {
                    remove(lastNode);
                } else {
                    ESlateMenu previousNode = (ESlateMenu) getLastPathComponent(nodesToPreviousPath);

                    previousNode.remove(lastNode);
                    if (previousNode.getMenuComponentCount() == 0 && nodesToPreviousPreviousPath.length != 1 && nodesToPreviousPath.length != 1 && nodesToPreviousPreviousPath != null) {
                        String previousNodeText = previousNode.getText();
                        ESlateMenu previousPreviousNode = (ESlateMenu) getLastPathComponent(nodesToPreviousPreviousPath);

                        previousPreviousNode.remove(previousNode);
                        previousPreviousNode.add(new ESlateMenuItem(previousNodeText, this));
                    }
                }
            }
            revalidate();
            repaint();
        } catch (Throwable  thr) {
            thr.printStackTrace();
        }
    }

    public void deleteItem(int[] path) {

        if (path.length == 0 || path == null)
            return;
        if (path.length == 1) {
            remove(path[0]);
            return;
        }
        int[] nodesToPreviousPath = new int[path.length - 1];

        for (int i = 0; i < path.length - 1; i++) {
            nodesToPreviousPath[i] = path[i];
        }
        int[] nodesToPreviousPreviousPath = null;

        if (nodesToPreviousPath.length > 1) {
            nodesToPreviousPreviousPath = new int[path.length - 2];
            for (int i = 0; i < path.length - 2; i++) {
                nodesToPreviousPreviousPath[i] = path[i];
            }
        }

        Component lastNode = getLastPathComponent(path);

        try {
            if (lastNode == null) {
                throw new Exception("No such path found. No deletion occured! ");
            } else {
                if (nodesToPreviousPath.length == 1) {
                    remove(path[path.length - 1]);
                } else {
                    ESlateMenu previousNode = (ESlateMenu) getLastPathComponent(nodesToPreviousPath);

                    previousNode.remove(path[path.length - 1]);
                    if (previousNode.getMenuComponentCount() == 0 && nodesToPreviousPreviousPath.length != 1 && nodesToPreviousPath.length != 1 && nodesToPreviousPreviousPath != null) {
                        String previousNodeText = previousNode.getText();
                        ESlateMenu previousPreviousNode = (ESlateMenu) getLastPathComponent(nodesToPreviousPreviousPath);

                        previousPreviousNode.remove(path[path.length - 2]);
                        previousPreviousNode.add(new ESlateMenuItem(previousNodeText, this));
                    }
                }
            }
            revalidate();
            repaint();
        } catch (Throwable  thr) {
            thr.printStackTrace();
        }
    }


    /**
     * This method enables or disables a menuItem
     * If the path is invalid the no menuItem  is enabled or disabled and an error message appears at the console.
     * The path must not start from the menubar, but from the top level menu that is the ansestor
     * of the JMenuItem we want to get to.
     * @param path The path to the item which we want to enable or disable
     */

    public void setPathEnabled(String[] path, boolean enabled) {
        String[] nodesToLastPath = new String[path.length + 1];

        nodesToLastPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();

        for (int i = 1; i < path.length + 1; i++) {
            nodesToLastPath[i] = path[i - 1];
        }

        JMenuItem lastNode = getLastPathComponent(nodesToLastPath);

        try {
            if (lastNode == null)
                System.out.println("No such path found. ");
            else
                lastNode.setEnabled(enabled);
        } catch (Throwable  thr) {
            thr.printStackTrace();
        }

    }


    /**
     * This method returns a JTree that contains the structure under a specific JMenuItem
     * If the path is invalid the null is returned and an error message appears at the console.
     * The path must not start from the menubar, but from the top level menu that is the ansestor
     * of the JMenuItem we want to get to.
     * @param path The path to the item which we want to get its sub menu structure
     */

    public JTree getMenuSubTree(String[] path) {
        String[] nodesToLastPath = new String[path.length + 1];

        nodesToLastPath[0] = getESlateHandle().getMenuPanel().getNameLabel().getText();

        for (int i = 1; i < path.length + 1; i++)
            nodesToLastPath[i] = path[i - 1];
        JMenuItem lastNode = getLastPathComponent(nodesToLastPath);

        if (lastNode == null) {
            System.out.println("No such node found");
            return null;
        } else if (lastNode instanceof JMenu) {
            MenuItemNode root = new MenuItemNode(lastNode);

            root.setUserObject(lastNode);
            JTree subTree = new JTree(root);

            reconstructSubNodes((ESlateMenu) lastNode, root);
            return subTree;
        } else return null;
    }

    public void setEnabled(boolean b) {
        for (int i = 0; i < getMenuCount(); i++)
            getMenu(i).setEnabled(b);
        enabled = b;
    }

    public boolean isEnabled() {
        return enabled;
    }


    /**
     * Sets the menu scrolling speed (in case the menu is larger than screen end)
     * @param speed The scrolling speed in milliseconds
     */

    public void setScrollSpeed(int speed) {
        if (speed > 1000)
            scrollSpeed = 1000;
        else if (speed < 100)
            scrollSpeed = 100;
        else
            scrollSpeed = speed;
        passScrollSpeedToMenus(scrollSpeed);
    }

    /**
     * Returns the menu scrolling speed
     * @return scrollSpeed The scrolling speed in milliseconds
     */

    public int getScrollSpeed() {
        return scrollSpeed;
    }

    /**
     * "Passes" the scrolling speed to top level menus
     * @param speed The scrolling speed in milliseconds
     */

    void passScrollSpeedToMenus(int speed) {
        for (int i = 0; i < getMenuCount(); i++) {
            ((ESlateMenu) getMenu(i)).scrollSpeed = speed;
            passScrollSpeedToSubMenus((ESlateMenu) getMenu(i), speed);
        }
    }

    /**
     * "Distributes" the scrolling speed to sub menus
     * @param menu The menu under which the scroling speed value is "distributed"
     * @param speed The scrolling speed in milliseconds
     */

    void passScrollSpeedToSubMenus(ESlateMenu menu, int speed) {
        for (int i = 0; i < menu.menuEntries.size(); i++) {
            if (menu.menuEntries.get(i) instanceof ESlateMenu) {
                passScrollSpeedToSubMenus((ESlateMenu) menu.menuEntries.get(i), speed);
            } else continue;
        }
    }

/**********************************************************************************/
/*                      Internal methods below this point                         */
/**********************************************************************************/

    /**
     * Returns Copyright information.
     * @return	The Copyright information.
     */
    private ESlateInfo getInfo() {
        String[] info = {
                bundleMessages.getString("part"),
                bundleMessages.getString("design"),
                bundleMessages.getString("development"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }

    /**
     * This method creates and adds a PerformanceListener to the E-Slate's
     * Performance Manager. The PerformanceListener attaches the component's
     * timers when the Performance Manager becomes enabled.
     */
    private void createPerformanceManagerListener(PerformanceManager pm) {
        if (perfListener == null) {
            perfListener = new PerformanceAdapter() {
                        public void performanceManagerStateChanged(PropertyChangeEvent e) {
                            boolean enabled = ((Boolean) e.getNewValue()).booleanValue();

                            // When the Performance Manager is enabled, try to attach the
                            // timers.
                            if (enabled) {
                                attachTimers();
                            }
                        }
                    };
            pm.addPerformanceListener(perfListener);
        }
    }

    /**
     * This method creates and attaches the component's timers. The timers are
     * created only once and are assigned to global variables. If the timers
     * have been already created, they are not re-created. If the timers have
     * been already attached, they are not attached again.
     * This method does not create any timers while the PerformanceManager is
     * disabled.
     */
    private void attachTimers() {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();
        boolean pmEnabled = pm.isEnabled();

        // If the performance manager is disabled, install a listener which will
        // re-invoke this method when the performance manager is enabled.
        if (!pmEnabled && (perfListener == null)) {
            createPerformanceManagerListener(pm);
        }

        // Do nothing if the PerformanceManager is disabled.
        if (!pmEnabled) {
            return;
        }

        boolean timersCreated = (loadTimer != null);

        // If the timers have already been constructed and attached, there is
        // nothing to do.
        if (!timersCreated) {
            // Get the performance timer group for this component.
            PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);

            // Construct and attach the component's timers.
            constructorTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("ConstructorTimer"), true
                    );
            loadTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("LoadTimer"), true
                    );
            saveTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("SaveTimer"), true
                    );
            initESlateAspectTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, bundleMessages.getString("InitESlateAspectTimer"), true
                    );
            setMenuStructureTimer = (PerformanceTimer) pm.createPerformanceTimerGroup(
                        compoTimerGroup, "SetMenuStructureTimer", true
                    );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.CONSTRUCTOR, constructorTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.LOAD_STATE, loadTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.SAVE_STATE, saveTimer, this
            );
            pm.registerPerformanceTimerGroup(
                PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
            );
        }
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

    void reconstructSubNodes(ESlateMenu menu, MenuItemNode node) {
        for (int j = 0; j < menu.menuEntries.size(); j++) {
            if (menu.menuEntries.get(j) instanceof ESlateMenu) {
                MenuItemNode subnode = new MenuItemNode();
                subnode.setUserObject(menu.menuEntries.get(j));
                node.add(subnode);
                reconstructSubNodes((ESlateMenu) menu.menuEntries.get(j), subnode);
            } else if (menu.menuEntries.get(j) instanceof ESlateMenuItem){
                MenuItemNode leaf = new MenuItemNode();
                leaf.setUserObject(menu.menuEntries.get(j));
                node.add(leaf);
            } else if (menu.menuEntries.get(j) instanceof ESlateCheckMenuItem) {
                CheckMenuItemNode leaf = new CheckMenuItemNode();
                leaf.setUserObject(menu.menuEntries.get(j));
                node.add(leaf);
            } else {
                CustomSeparator separator = new CustomSeparator(bundleMessages.getString("<<Separator>>"));
                node.add(separator);
            }
        }
    }

    void disposeMenuStructure(ESlateMenu menu) {
        for (int i = 0; i < menu.menuEntries.size(); i++) {
            if (menu.menuEntries.get(i) instanceof ESlateMenu)
                disposeMenuStructure((ESlateMenu) menu.menuEntries.get(i));
        }
        menu.menuEntries.clear();
        menu.removeAll();
        if (menu.rollUp != null){
            menu.rollDown.menu = null;
            menu.rollUp.menu = null;
            menu.rollDown = null;
            menu.rollUp = null;
        }
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
    private void createMenusFromTree(DefaultMutableTreeNode node, ESlateMenu menu) {
        //menu.menuEntries.clear();
        DefaultMutableTreeNode child;
        JMenuItem menuitem;
        ESlateMenu newmenu;
        int k = node.getChildCount();
        menu.menuEntries.clear();
        for (int i = 0; i < k; i++) {
            child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (child instanceof CustomSeparator) {
                menu.add(new JSeparator());
            } else if (child.isLeaf() == true) {
                if (child.getUserObject() instanceof ESlateMenuItem) {
                    menuitem = (ESlateMenuItem) child.getUserObject();
                } else if (child.getUserObject() instanceof ESlateCheckMenuItem) {
                    menuitem = (ESlateCheckMenuItem) child.getUserObject();
                } else {
                    if (child instanceof CheckMenuItemNode)
                        menuitem = new ESlateCheckMenuItem(child.toString(),this);
                    else{
                        menuitem = new ESlateMenuItem(child.toString(),this);
                    }
                }
                menu.add(menuitem);
            } else {
                if (child.getUserObject() instanceof ESlateMenu)
                    newmenu = (ESlateMenu) child.getUserObject();
                else
                    newmenu = new ESlateMenu(child.toString(),this);

                menu.add(newmenu);
                createMenusFromTree(child, newmenu);
            }
        }
    }

    /**
     * This method creates the top level menus of the menubar from a JTree's first level nodes
     * It then uses createMenusFromTree to create the sub menu structure
     * under the top level menus.
     * @param tree The JTree its structure is going to be used
     */

    void setInitialMenus(JTree tree) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        DefaultMutableTreeNode child;
        String text = "";
        ESlateMenu menu;

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            child = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            text = child.getUserObject().toString();
            if (child.getUserObject() instanceof JMenuItem) {
                menu = (ESlateMenu) child.getUserObject();
            } else {
                menu = new ESlateMenu(this);
                menu.setText(text);
            }
            //menu.menuEntries.clear();
            add(menu);
            createMenusFromTree(child, menu);
        }
        //repaint();
    }

    void setInitialMenus(MenuItemNode rootNode) {
        DefaultMutableTreeNode child;
        ESlateMenu menu;
        int k = rootNode.getChildCount();

        for (int i = 0; i <k;  i++) {
            child = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if (child.getUserObject() instanceof JMenuItem) {
                menu = (ESlateMenu) child.getUserObject();
            } else {
                menu = new ESlateMenu(child.getUserObject().toString(),this);
            }
            add(menu);
            createMenusFromTree(child, menu);

        }
    }

    /**
     * This method creates the top level nodes of a JTree from the menubar's first level menus
     * It then uses reconstructSubNodes to create the sub node structure
     * under the top level nodes.
     */

    MenuItemNode reconstructStructure() {
        MenuItemNode rootNode = new MenuItemNode(handle.getMenuPanel().getNameLabel().getText());
        for (int i = 0; i < this.getMenuCount(); i++) {
            MenuItemNode node = new MenuItemNode();
            node.setUserObject(getMenu(i));
            rootNode.add(node);
            reconstructSubNodes((ESlateMenu) this.getMenu(i), node);
        }
        return rootNode;
    }

    public void applyState(StorageStructure fieldMap){
        removeAll();
        ESlateMenu menu;
        if (fieldMap.containsKey("SubElements")) {
            if (fieldMap.getDataVersionID() == 2) {
                try {
                    System.out.println("MenuSystem Uses 2.0 StoreVersion");
                    Object[] objectArray = (Object[]) fieldMap.get("SubElements", (Object) null);
                    for (int i = 0; i < objectArray.length; i++) {
                        menu = new ESlateMenu(this);
                        menu.applyState((StorageStructure) objectArray[i]);
                        add(menu);
                    }
                    makeMenuBarKnownToAll(this);
                } catch (Throwable thr) {
                    thr.printStackTrace();
                }
            } else {
                if (fieldMap.getDataVersion().equals("2.0")){
                    try{
                        System.out.println("MenuSystem Uses 2.0 StoreVersion, but this is an older fieldmap");
                        Object[] objectArray = (Object[]) fieldMap.get("SubElements", (Object) null);
                        for (int i = 0; i < objectArray.length; i++) {
                            menu = new ESlateMenu(this);
                            menu.applyState((StorageStructure) objectArray[i]);
                            add(menu);
                        }
                        makeMenuBarKnownToAll(this);
                    } catch (Throwable thr) {
                        thr.printStackTrace();
                    }
                }else{
                    try {
                        System.out.println("MenuSystem Uses 1.0 StoreVersion");
                        Object[] objectArray = (Object[]) fieldMap.get("SubElements", (Object) null);
                        for (int i = 0; i < objectArray.length; i++) {
                            add((ESlateMenu) objectArray[i]);
                        }
                        makeMenuBarKnownToAll(this);
                    } catch (Throwable thr) {
                        thr.printStackTrace();
                    }
                }
            }
        }
        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));
        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));
        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setRequestFocusEnabled(fieldMap.get("RequestFocusEnabled", isRequestFocusEnabled()));
        setVisible(fieldMap.get("Visible", isVisible()));
        setAutoscrolls(fieldMap.get("Autoscrolls", getAutoscrolls()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        setMargin((Insets) fieldMap.get("Margin", getMargin()));
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));
        setLocation((Point) fieldMap.get("Location", getLocation()));
        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));
        setScrollSpeed(fieldMap.get("ScrollSpeed", getScrollSpeed()));
        setItemCapableOfMultiplePathChangedEvents(fieldMap.get("SendPathEventsAgain", getItemCapableOfMultiplePathChangedEvents()));
        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");
                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }
        repaint();
        revalidate();
    }

    public StorageStructure recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);
        Object[] objectArray = new Object[getMenuCount()];

        if (fieldMap.getDataVersionID() == 2) {
            for (int i = 0; i < getMenuCount(); i++) {
                objectArray[i] = ((ESlateMenu) getMenu(i)).recordState();
            }
        } else {
            if (fieldMap.getDataVersion().equals("2.0")){
                for (int i = 0; i < getMenuCount(); i++) {
                    objectArray[i] = ((ESlateMenu) getMenu(i)).recordState();
                }
            }else{
                for (int i = 0; i < getMenuCount(); i++) {
                    objectArray[i] = (ESlateMenu) getMenu(i);
                }
            }
        }
        fieldMap.put("SubElements", objectArray);
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());
        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());
        fieldMap.put("BorderPainted", isBorderPainted());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("RequestFocusEnabled", isRequestFocusEnabled());
        fieldMap.put("Visible", isVisible());
        fieldMap.put("Autoscrolls", getAutoscrolls());
        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        fieldMap.put("Margin", getMargin());
        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());
        fieldMap.put("Location", getLocation());
        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());
        fieldMap.put("ScrollSpeed", getScrollSpeed());
        fieldMap.put("SendPathEventsAgain", getItemCapableOfMultiplePathChangedEvents());
        JMenuBar test = new JMenuBar();
        if (getBorder() != null && !getBorder().equals(test.getBorder())) {
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
        return fieldMap;
    }
}

