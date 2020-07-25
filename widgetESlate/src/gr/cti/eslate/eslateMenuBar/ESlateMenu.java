package gr.cti.eslate.eslateMenuBar;


import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ObjectBaseArray;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.UIResource;

public class ESlateMenu extends JMenu implements Externalizable {

    boolean valid = false;
    int firstVisibleItemIndex = -1, lastVisibleItemIndex = -1;
    RollMenuButton rollUp;// = new RollMenuButton(this, RollMenuButton.DIRECTION_UP);
    RollMenuButton rollDown;// = new RollMenuButton(this, RollMenuButton.DIRECTION_DOWN);
    public ObjectBaseArray menuEntries = new ObjectBaseArray();
    int ITEM_HEIGHT = 0;
    ESlateMenu parentMenu;
    boolean popupMenuAtLeft = false;
    private Icon tempIcon;
    private static final int FORMAT_VERSION = 1;
    ESlateMenuBar menuBar;
    int scrollSpeed = 200;
    static final long serialVersionUID = 8346155894208281837L;
    private int visibleChildrenHeight=0;


    /**
     * Constructs a new ESlateMenu
     */

    public ESlateMenu() {
        super();
        listenForPossibleScroll();
        setBounds(this.getBounds().x,this.getBounds().y,this.getBounds().width,50);
    }

    public ESlateMenu(String s) {

        super(s);
        listenForPossibleScroll();
        setBounds(this.getBounds().x,this.getBounds().y,this.getBounds().width,50);
    }

    /**
     * Constructs a new ESlateMenu, belonging to a specific menuBar
     */

    public ESlateMenu(ESlateMenuBar menuBar) {
        this.menuBar = menuBar;
        listenForPossibleScroll();
        setBounds(this.getBounds().x,this.getBounds().y,this.getBounds().width,50);
    }

    /**
     * Constructs a new ESlateMenu, with specific text, and which belongs to a spe
     */

    public ESlateMenu(String text, ESlateMenuBar menuBar) {
        super(text);
        this.menuBar = menuBar;
        listenForPossibleScroll();
        setBounds(this.getBounds().x,this.getBounds().y,this.getBounds().width,50);
    }



    private Icon toRestorableImageIcon(Icon icon) {
        if (icon == null || icon instanceof NewRestorableImageIcon)
            return icon;
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

        icon.paintIcon(this, b.getGraphics(), 0, 0);
        return new NewRestorableImageIcon(b);
    }

    /**
     * Reads from ESlateFieldMap to restore stored values and properties
     */

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {

        Object firstObj = in.readObject();

//        ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
        StorageStructure fieldMap = (StorageStructure) firstObj;

        if (fieldMap.containsKey("SubMenuElements")) {
            try {
                Object[] objectArray = (Object[]) fieldMap.get("SubMenuElements", (Object) null);

                for (int i = 0; i < objectArray.length; i++) {
                    if (objectArray[i] instanceof String) {
                        add(new JSeparator());
                    } else if (objectArray[i] instanceof ESlateMenu) {
                        add((ESlateMenu) objectArray[i]);
                    } else if (objectArray[i] instanceof ESlateMenuItem) {
                        add((ESlateMenuItem) objectArray[i]);
                    } else if (objectArray[i] instanceof ESlateCheckMenuItem) {
                        add((ESlateCheckMenuItem) objectArray[i]);
                    }

                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }

        //    menuEntries = (ObjectBaseArray) fieldMap.get("SubMenuElements", (Object) null);

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setContentAreaFilled(fieldMap.get("ContentAreaFilled", isContentAreaFilled()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setFocusPainted(fieldMap.get("FocusPainted", isFocusPainted()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setRolloverEnabled(fieldMap.get("RolloverEnabled", isRolloverEnabled()));
        setSelected(fieldMap.get("Selected", isSelected()));

        setEnabled(fieldMap.get("Enabled", isEnabled()));

        setText(fieldMap.get("Text", getText()));

        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setHorizontalAlignment(fieldMap.get("HorizontalAlignment", getHorizontalAlignment()));
        setVerticalAlignment(fieldMap.get("VerticalAlignment", getVerticalAlignment()));
        setHorizontalTextPosition(fieldMap.get("HorizontalTextPosition", getHorizontalTextPosition()));
        setVerticalTextPosition(fieldMap.get("VerticalTextPosition", getVerticalTextPosition()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        setActionCommand(fieldMap.get("ActionCommand", getActionCommand()));

        setMargin((Insets) fieldMap.get("Margin", getMargin()));

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

//        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
//        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
//        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));

        setDisabledIcon(fieldMap.get("DisabledIcon", getDisabledIcon()));
        setDisabledSelectedIcon(fieldMap.get("DisabledSelectedIcon", getDisabledSelectedIcon()));
        setIcon(fieldMap.get("Icon", getIcon()));
        setPressedIcon(fieldMap.get("PressedIcon", getPressedIcon()));
        setRolloverIcon(fieldMap.get("RolloverIcon", getRolloverIcon()));
        setSelectedIcon(fieldMap.get("SelectedIcon", getSelectedIcon()));
        setRolloverSelectedIcon(fieldMap.get("RolloverSelectedIcon", getRolloverSelectedIcon()));

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }

    }

    /**
     * Sets text (and name) to the menu
     * Its important that name and text are the same so that toString() can return the name of the menu
     * and not the menu's string representation
     */

    public void setText(String text) {
        super.setText(text);
        setName(text);
    }

    /**
     * Writes to ESlateFieldMap values and properties to be stored
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("Enabled", isEnabled());

        Object[] objectArray = new Object[menuEntries.size()];

        for (int i = 0; i < menuEntries.size(); i++) {
            if (menuEntries.get(i) instanceof ESlateMenuItem) {
                objectArray[i] = (ESlateMenuItem) menuEntries.get(i);
            } else if (menuEntries.get(i) instanceof ESlateCheckMenuItem) {
                objectArray[i] = (ESlateCheckMenuItem) menuEntries.get(i);
            } else if (menuEntries.get(i) instanceof ESlateMenu) {
                objectArray[i] = (ESlateMenu) menuEntries.get(i);
            } else
                objectArray[i] = "separator";
        }

        fieldMap.put("SubMenuElements", objectArray);
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());

        fieldMap.put("BorderPainted", isBorderPainted());
        fieldMap.put("ContentAreaFilled", isContentAreaFilled());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("FocusPainted", isFocusPainted());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("RolloverEnabled", isRolloverEnabled());
        fieldMap.put("Selected", isSelected());
        fieldMap.put("Enabled", isEnabled());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("HorizontalAlignment", getHorizontalAlignment());
        fieldMap.put("HorizontalTextPosition", getHorizontalTextPosition());
        fieldMap.put("VerticalAlignment", getVerticalAlignment());
        fieldMap.put("VerticalTextPosition", getVerticalTextPosition());

        fieldMap.put("Text", getText());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        fieldMap.put("ActionCommand", getActionCommand());

        fieldMap.put("Margin", getMargin());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

//        fieldMap.put("MaximumSize", getMaximumSize());
//        fieldMap.put("MinimumSize", getMinimumSize());
//        fieldMap.put("PreferredSize", getPreferredSize());

        fieldMap.put("DisabledIcon", toRestorableImageIcon(getDisabledIcon()));
        fieldMap.put("DisabledSelectedIcon", toRestorableImageIcon(getDisabledSelectedIcon()));
        fieldMap.put("Icon", toRestorableImageIcon(getIcon()));
        fieldMap.put("PressedIcon", toRestorableImageIcon(getPressedIcon()));
        fieldMap.put("RolloverIcon", toRestorableImageIcon(getRolloverIcon()));
        fieldMap.put("SelectedIcon", toRestorableImageIcon(getSelectedIcon()));
        fieldMap.put("RolloverSelectedIcon", toRestorableImageIcon(getRolloverSelectedIcon()));

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

    protected StorageStructure recordState() {

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("IsMenu", true);

        fieldMap.put("Enabled", isEnabled());
        Object[] objectArray = new Object[menuEntries.size()];

        for (int i = 0; i < menuEntries.size(); i++) {
            if (menuEntries.get(i) instanceof ESlateMenuItem) {
                objectArray[i] = ((ESlateMenuItem) menuEntries.get(i)).recordState();
            } else if (menuEntries.get(i) instanceof ESlateCheckMenuItem) {
                objectArray[i] = ((ESlateCheckMenuItem) menuEntries.get(i)).recordState();
            } else if (menuEntries.get(i) instanceof ESlateMenu) {
                objectArray[i] = ((ESlateMenu) menuEntries.get(i)).recordState();
            } else
                objectArray[i] = "separator";
        }

        fieldMap.put("SubMenuElements", objectArray);
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());

        fieldMap.put("BorderPainted", isBorderPainted());
        fieldMap.put("ContentAreaFilled", isContentAreaFilled());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("FocusPainted", isFocusPainted());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("RolloverEnabled", isRolloverEnabled());
        fieldMap.put("Selected", isSelected());
        fieldMap.put("Enabled", isEnabled());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("HorizontalAlignment", getHorizontalAlignment());
        fieldMap.put("HorizontalTextPosition", getHorizontalTextPosition());
        fieldMap.put("VerticalAlignment", getVerticalAlignment());
        fieldMap.put("VerticalTextPosition", getVerticalTextPosition());

        fieldMap.put("Text", getText());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        fieldMap.put("ActionCommand", getActionCommand());

        fieldMap.put("Margin", getMargin());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

//        fieldMap.put("MaximumSize", getMaximumSize());
//        fieldMap.put("MinimumSize", getMinimumSize());
//        fieldMap.put("PreferredSize", getPreferredSize());

        fieldMap.put("DisabledIcon", toRestorableImageIcon(getDisabledIcon()));
        fieldMap.put("DisabledSelectedIcon", toRestorableImageIcon(getDisabledSelectedIcon()));
        fieldMap.put("Icon", toRestorableImageIcon(getIcon()));
        fieldMap.put("PressedIcon", toRestorableImageIcon(getPressedIcon()));
        fieldMap.put("RolloverIcon", toRestorableImageIcon(getRolloverIcon()));
        fieldMap.put("SelectedIcon", toRestorableImageIcon(getSelectedIcon()));
        fieldMap.put("RolloverSelectedIcon", toRestorableImageIcon(getRolloverSelectedIcon()));

        if (getBorder() != null && !(getBorder() instanceof UIResource) ) {
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

    protected void applyState(StorageStructure fieldMap) {
        setText(fieldMap.get("Text", getText()));

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
                            menu = new ESlateMenu(getMenuBar());
                            add(menu);
                            menu.applyState((StorageStructure) objectArray[i]);
                        } else {
                            if (((StorageStructure) objectArray[i]).get("IsCheckMenuItem", isCheckMenuItem())) {
                                checkMenuItem = new ESlateCheckMenuItem(getMenuBar());
                                add(checkMenuItem);
                                checkMenuItem.applyState((StorageStructure) objectArray[i]);
                            } else {
                                menuItem = new ESlateMenuItem(getMenuBar());
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

        //    menuEntries = (ObjectBaseArray) fieldMap.get("SubMenuElements", (Object) null);

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setContentAreaFilled(fieldMap.get("ContentAreaFilled", isContentAreaFilled()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setFocusPainted(fieldMap.get("FocusPainted", isFocusPainted()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setRolloverEnabled(fieldMap.get("RolloverEnabled", isRolloverEnabled()));
        setSelected(fieldMap.get("Selected", isSelected()));

        setEnabled(fieldMap.get("Enabled", isEnabled()));

        setText(fieldMap.get("Text", getText()));

        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setHorizontalAlignment(fieldMap.get("HorizontalAlignment", getHorizontalAlignment()));
        setVerticalAlignment(fieldMap.get("VerticalAlignment", getVerticalAlignment()));
        setHorizontalTextPosition(fieldMap.get("HorizontalTextPosition", getHorizontalTextPosition()));
        setVerticalTextPosition(fieldMap.get("VerticalTextPosition", getVerticalTextPosition()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        setActionCommand(fieldMap.get("ActionCommand", getActionCommand()));

        setMargin((Insets) fieldMap.get("Margin", getMargin()));

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

//        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
//        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
//        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));

        setDisabledIcon(fieldMap.get("DisabledIcon", getDisabledIcon()));
        setDisabledSelectedIcon(fieldMap.get("DisabledSelectedIcon", getDisabledSelectedIcon()));
        setIcon(fieldMap.get("Icon", getIcon()));
        setPressedIcon(fieldMap.get("PressedIcon", getPressedIcon()));
        setRolloverIcon(fieldMap.get("RolloverIcon", getRolloverIcon()));
        setSelectedIcon(fieldMap.get("SelectedIcon", getSelectedIcon()));
        setRolloverSelectedIcon(fieldMap.get("RolloverSelectedIcon", getRolloverSelectedIcon()));

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }

    }

    /**
     * Sets the parent menubar to this menu
     * @param menuBar The parent menuBar
     */

    void setMenuBar(ESlateMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    /**
     * Gets the parent menubar to this menu
     */

    ESlateMenuBar getMenuBar() {
        return this.menuBar;
    }

    ///////////////// ADDED CODE - TRYOUTS AREA\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Adds a menuItem
     * @param menuItem The item to be added
     */

    public JMenuItem add(JMenuItem item) {
        menuEntries.add(item);
        if (item instanceof ESlateMenu)
            ((ESlateMenu) item).setParentMenu(this);
        if (item instanceof ESlateMenuItem)
            ((ESlateMenuItem) item).setParentMenu(this);
        if (item instanceof ESlateCheckMenuItem)
            ((ESlateCheckMenuItem) item).setParentMenu(this);
        return item;
    }

    /**
     * Adds a separator
     * @param item The JSeparator to be added
     */

    public void add(JSeparator item) {
        menuEntries.add(item);
    }

    /**
     * Inserts a menuItem at a specified position
     * @param menuItem The item to be inserted
     * @param pos The position
     */

    public JMenuItem insert(JMenuItem item, int pos) {
        menuEntries.add(pos, item);
        return item;
    }

    public JSeparator insert(JSeparator item, int pos) {
        menuEntries.add(pos, item);
        return item;
    }

    /**
     * Removes a menuItem
     * @param menuItem The item to be removed
     */

    public void remove(JMenuItem item) {
        menuEntries.removeElements(item);
    }

    public void remove(JSeparator item) {
        menuEntries.removeElements(item);
    }

    public void remove(int pos) {
        menuEntries.remove(pos);
    }

    public void removeAll(){
        super.removeAll();
        menuEntries.clear();
    }


    boolean whichSideToShowPopupMenu(){
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        boolean b;
        java.awt.Point itemLocation = rollDown.getLocation();
        SwingUtilities.convertPointToScreen(itemLocation, this.getParent());
        int width = computePopupMenuWidth();
        if (width >300)
            width = 300;
        if (itemLocation.x + getWidth()+ width> screenWidth)
            b = false;
        else
            b = true;
        return b;
    }


    void populateComponentPaletteMenu() {
        valid = true;
        adjustVisibleMenuItems();
        getPopupMenu().invalidate();
        getPopupMenu().revalidate();
    }

    /**
     * Returns the height of the visible part of the menu (if the menu goes beyond the screen end)
     */

    int getVisibleMenuItemHeight() {
        int visibleMenuItemHeight = 0;

        for (int i = 0; i < menuEntries.size(); i++) {

            if (menuEntries.get(i) instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) menuEntries.get(i);
                Font f = mi.getFont();
                FontMetrics metrics = getFontMetrics(f);

                visibleMenuItemHeight = visibleMenuItemHeight + mi.getMargin().bottom + mi.getMargin().top + metrics.getHeight();
            } else {
                visibleMenuItemHeight = visibleMenuItemHeight + 2;
            }
        }
        if (firstVisibleItemIndex != 0)
            if (rollUp != null)
                visibleMenuItemHeight += rollUp.getHeight();
        if (firstVisibleItemIndex != menuEntries.size())
            if (rollDown != null)
                visibleMenuItemHeight += rollDown.getHeight();
        return visibleMenuItemHeight;

    }

    int computePopupMenuHeight() {
        int visibleMenuItemHeight = 0;
        int compoItemCount = lastVisibleItemIndex - firstVisibleItemIndex + 1;

        for (int i = 0; i < compoItemCount; i++) {
            if (menuEntries.get(i) instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) menuEntries.get(i);
                Font f = mi.getFont();
                FontMetrics metrics = getFontMetrics(f);

                visibleMenuItemHeight = visibleMenuItemHeight + mi.getMargin().bottom + mi.getMargin().top + metrics.getHeight();
            } else {
                visibleMenuItemHeight = visibleMenuItemHeight + 2;
            }
        }
        if (rollUp != null && super.getMenuComponent(0) instanceof RollMenuButton)
//        if (rollUp != null && rollUp.isVisible())
            visibleMenuItemHeight += RollMenuButton.ITEM_HEIGHT;
//        if (rollDown != null && rollDown.isVisible())
        if (rollDown != null && super.getMenuComponent(super.getMenuComponentCount()-1) instanceof RollMenuButton)
            visibleMenuItemHeight += RollMenuButton.ITEM_HEIGHT;
        return visibleMenuItemHeight;
    }

    /**
     * Returns the point of the menu's right edge location
     */

    Point getMenuRightEdgeLocation() {
        java.awt.Point screenLocation = getLocation();

        SwingUtilities.convertPointToScreen(screenLocation, getParent());
        screenLocation.x = getSize().width;
        return screenLocation;
    }

    public void setParentMenu(ESlateMenu menu) {
        this.parentMenu = menu;
    }

    public ESlateMenu getParentMenu() {
        return parentMenu;
    }

    public int getItemCount() {
        return menuEntries.size();
    }

    public int[] getPath() {
        IntBaseArray path = new IntBaseArray();
        ESlateMenu menu = getParentMenu();
        JMenuItem mi = this;
        int index;

        if (menu == null)
            return new int[] {menuBar.getComponentIndex(menu)};
        index = menu.menuEntries.indexOf(mi);
        path.add(index);
        while (menu.getParentMenu() != null) {
            mi = menu;
            menu = menu.getParentMenu();
            index = menu.menuEntries.indexOf(mi);
            path.add(index);
        }
        if (menu.getParentMenu() == null) {
            index = menuBar.getComponentIndex(menu);
            path.add(index);
        }
        int[] ipath = new int[path.size()];

        for (int i = 0; i < path.size(); i++)
            ipath[path.size() - 1 - i] = path.get(i);
        return ipath;

    }

    public JMenuItem getItem(int pos) {
        if (menuEntries.get(pos) instanceof JMenuItem)
            return (JMenuItem) menuEntries.get(pos);
        else return null;
    }

    public Component getMenuComponent(int pos) {
        return (Component) menuEntries.get(pos);
    }

    /**
     * Returns the available height till the end of the screen to be used for the menu
     */

    int getAvailableMenuHeightFromScreenBottom() {
        java.awt.Point screenLocation = getLocation();

        SwingUtilities.convertPointToScreen(screenLocation, getParent());
        int palleteMenuScreenHeight = screenLocation.y;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int availableHeight = screenHeight - palleteMenuScreenHeight;

        return availableHeight;
    }

    int getAvailableMenuHeightFromScreenTop() {
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int availableHeight = screenHeight - getAvailableMenuHeightFromScreenBottom();

        return availableHeight;
    }

    protected boolean showDownwards() {
        if (getAvailableMenuHeightFromScreenBottom() < getAvailableMenuHeightFromScreenTop() && getAvailableMenuHeightFromScreenBottom() - 50 < getVisibleMenuItemHeight())
            return false;
        else return true;
    }

    int getAvailableMenuHeight() {
        if (showDownwards())
            return getAvailableMenuHeightFromScreenBottom();
        else
            return getAvailableMenuHeightFromScreenTop();
    }

    /**
     * Adjusts the menu children that must be visible
     */

    void adjustVisibleMenuItems() {
        int availableHeight;
        if (showDownwards())
            availableHeight = getAvailableMenuHeightFromScreenBottom()-60;
        else
            availableHeight = getAvailableMenuHeightFromScreenTop()-60;
        super.removeAll();
        firstVisibleItemIndex = 0;
        lastVisibleItemIndex = -1;
        int h;
        if (isTopLevelMenu())
            h = getHeight() + getMargin().bottom + getMargin().top;
        else
            h = 0;
        for (int i=0; i<menuEntries.size(); i++) {
            if (menuEntries.get(i) instanceof JMenuItem){
               JMenuItem mi = (JMenuItem) menuEntries.get(i);
               Font f = ((JMenuItem) menuEntries.get(i)).getFont();
               FontMetrics metrics = getFontMetrics(f);
               h = h +((JMenuItem) mi).getMargin().bottom + ((JMenuItem) mi).getMargin().top + metrics.getHeight();
               if (h/(availableHeight)==0){
                  super.add((JMenuItem) mi);
                  visibleChildrenHeight = h;
//                  mi.setItemVisible(true);
                  lastVisibleItemIndex++;
                  if (availableHeight - h <= 60)
                     break;
               }
            }else if (menuEntries.get(i) instanceof JSeparator){
                h = h + 2;
               if (h/(availableHeight)==0){
                  addSeparator();
                  visibleChildrenHeight = h;
                  lastVisibleItemIndex++;
                  if (availableHeight - h <= 60)
                     break;
               }
            }
        }
        rollUp = new RollMenuButton(this, RollMenuButton.DIRECTION_UP);
        rollDown = new RollMenuButton(this, RollMenuButton.DIRECTION_DOWN);
/*
        super.insert(rollUp, 0);
        super.add(rollDown);

        if (firstVisibleItemIndex != 0)
            rollUp.setItemVisible(true);
        else
            rollUp.setItemVisible(false);
        if (lastVisibleItemIndex < menuEntries.size()-1) {
            rollDown.setItemVisible(true);

        }else{
            rollDown.setItemVisible(false);
        }
*/
        if (firstVisibleItemIndex != 0)
            super.insert(rollUp, 0);
        if (lastVisibleItemIndex < menuEntries.size()-1)
            super.add(rollDown);
    }
    /**
     * Performs upward or downward scrolling of the menu elements
     * @param upwards The direction of scrolling (true = upwards)
     */


    void scroll(boolean upwards) {
        Dimension popupSize = getPopupMenu().getSize();
        java.awt.Point screenLocation = getLocation();
        SwingUtilities.convertPointToScreen(screenLocation, getParent());
        if (upwards) {
            if (firstVisibleItemIndex == 0)
                return;

            if (menuEntries.get(firstVisibleItemIndex - 1) instanceof JMenuItem)
                super.insert((JMenuItem) menuEntries.get(firstVisibleItemIndex - 1), 1);
            else
                insertSeparator(1);
            firstVisibleItemIndex -= 1;
            lastVisibleItemIndex -= 1;
            super.remove(super.getItemCount() - 2);
            if (!(super.getMenuComponent(super.getMenuComponentCount() -1) instanceof RollMenuButton) && (lastVisibleItemIndex != menuEntries.size() - 1)) {
                if (super.getMenuComponent(0) instanceof RollMenuButton && firstVisibleItemIndex == 0) {
                    super.remove(0);
                    popupSize.height-=10;
                    rollUp.rollTimer.stop();
                }
                super.add(rollDown);
                popupSize.height+=10;
            }
            if (super.getMenuComponent(0) instanceof RollMenuButton && firstVisibleItemIndex == 0) {
                super.remove(0);
                popupSize.height-=10;
                rollUp.rollTimer.stop();
            }
            getPopupMenu().setPopupSize(popupSize.width, popupSize.height);
        } else {
            if (lastVisibleItemIndex == menuEntries.size() - 1)
                return;

            if (menuEntries.get(lastVisibleItemIndex + 1) instanceof JMenuItem){
                super.insert((JMenuItem) menuEntries.get(lastVisibleItemIndex + 1), super.getItemCount() - 1); //insertPos);
            }else
                insertSeparator(super.getItemCount() - 1);
            firstVisibleItemIndex += 1;
            lastVisibleItemIndex += 1;
            super.remove(1);
            if (!(super.getMenuComponent(0) instanceof RollMenuButton)){
                java.awt.Point itemLocation = rollDown.getLocation();
                SwingUtilities.convertPointToScreen(itemLocation, rollDown.getParent());
                Point newMouseLocation = new Point(itemLocation.x + (rollDown.getSize().width / 2), itemLocation.y + (rollDown.getSize().height / 2) + RollMenuButton.ITEM_HEIGHT);

                try {
                    if (!rollDown.rollTimer.isRunning()){
                        Robot robot = new Robot();
                        robot.mouseMove(newMouseLocation.x, newMouseLocation.y);

                    }
                } catch (Throwable exc) {
                    System.out.println("AWT exception: " + exc.getMessage());
                }

                super.insert(rollUp,0);
                popupSize.height+=10;

            }
            if (super.getMenuComponent(super.getMenuComponentCount()-1) instanceof RollMenuButton && lastVisibleItemIndex == menuEntries.size() - 1){
                super.remove(super.getMenuComponentCount()-1);
                popupSize.height-=10;
                rollDown.rollTimer.stop();
            }
        }
        if (!showDownwards()){
            if (isTopLevelMenu()){
                screenLocation.y -=popupSize.height;
            }
            int popupWidth  = computePopupMenuWidth();
            if (popupWidth >= 300)
                popupWidth = 300;
            if (!whichSideToShowPopupMenu())
                setMenuLocation(-popupWidth+screenLocation.x, screenLocation.y);
            else{
                if (!isTopLevelMenu())
                    screenLocation.x += getSize().width;
                    setMenuLocation(screenLocation.x, screenLocation.y);
            }
        }
        getPopupMenu().setPopupSize(popupSize.width, popupSize.height);
        getPopupMenu().invalidate();
        getPopupMenu().doLayout();
        getPopupMenu().repaint();
        getPopupMenu().revalidate();
    }

    int computePopupMenuWidth() {
        JMenuItem mi;
        Font f;
        FontMetrics metrics;
        int maxPopupMenuWidth = 0;

        for (int i = 0; i < menuEntries.size(); i++) {
            if (menuEntries.get(i) instanceof JMenuItem) {
                mi = (JMenuItem) menuEntries.get(i);
                f = mi.getFont();
                metrics = getFontMetrics(f);
                int curr = mi.getMargin().left + mi.getMargin().right + metrics.stringWidth(mi.getText());

                if (curr > maxPopupMenuWidth)
                    maxPopupMenuWidth = curr;
            }
        }
        return maxPopupMenuWidth;
    }

    /**
     * Determines what must happen when scrolling is about to happen
     */

 void listenForPossibleScroll(){
        getPopupMenu().setLayout(new BoxLayout(getPopupMenu(), BoxLayout.Y_AXIS));
        getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                int popupWidth = computePopupMenuWidth();
                if (popupWidth>=300)
                    popupWidth = 300;
                if (!valid) {
                    firstVisibleItemIndex = lastVisibleItemIndex = -1;
                    populateComponentPaletteMenu();
                    java.awt.Point screenLocation = getLocation();
                    SwingUtilities.convertPointToScreen(screenLocation, getParent());
                    if (showDownwards()){
                        if (!isTopLevelMenu())
                           screenLocation.y = screenLocation.y - getPopupMenu().getBorder().getBorderInsets(getPopupMenu()).top;
                        else
                           screenLocation.y += getSize().height;
                        if (!whichSideToShowPopupMenu()){
                           setMenuLocation(-popupWidth+screenLocation.x, screenLocation.y);
                        }else{
                            if (!isTopLevelMenu())
                                screenLocation.x += getSize().width;
                           setMenuLocation(screenLocation.x, screenLocation.y);
                        }
                    }else{
//                        System.out.println("popup height computed is :"+computePopupMenuHeight());
                        screenLocation.y -=computePopupMenuHeight()-getPopupMenu().getBorder().getBorderInsets(getPopupMenu()).top;
                        if (getMenuComponent(getMenuComponentCount()-1) instanceof RollMenuButton)
                            screenLocation.y -= RollMenuButton.ITEM_HEIGHT;
                        if (!whichSideToShowPopupMenu()){
                           setMenuLocation(-popupWidth+screenLocation.x, screenLocation.y);
                        }else{
                           if (!isTopLevelMenu())
                                screenLocation.x += getSize().width;
                           setMenuLocation(screenLocation.x, screenLocation.y);
                        }
                    }
                }else{
                    //if (getAvailableMenuHeight() < getVisibleMenuItemHeight() ||
                    //    getAvailableMenuHeight()+60  > getVisibleMenuItemHeight()) {
                        adjustVisibleMenuItems();
                    //}
                    java.awt.Point screenLocation = getLocation();
                    SwingUtilities.convertPointToScreen(screenLocation, getParent());
                    if (showDownwards()){
                        if (!isTopLevelMenu())
                            screenLocation.y = screenLocation.y - getPopupMenu().getBorder().getBorderInsets(getPopupMenu()).top;
                        if (isTopLevelMenu())
                            screenLocation.y += getSize().height;
                        if (!whichSideToShowPopupMenu())
                            setMenuLocation(-popupWidth+screenLocation.x, screenLocation.y);
                        else{
                            if (!isTopLevelMenu())
                                screenLocation.x += getSize().width;
                            setMenuLocation(screenLocation.x, screenLocation.y);
                        }
                    }else{
//                        System.out.println("popup height is :"+getPopupMenu().getSize().height);
                        screenLocation.y = screenLocation.y - computePopupMenuHeight() - getSize().height- 2*getPopupMenu().getBorder().getBorderInsets(getPopupMenu()).top;
                        if (getMenuComponent(0) instanceof RollMenuButton)
                            screenLocation.y -=RollMenuButton.ITEM_HEIGHT;
                        if (getMenuComponent(getMenuComponentCount()-1) instanceof RollMenuButton)
                            screenLocation.y -=RollMenuButton.ITEM_HEIGHT;
                        if (!whichSideToShowPopupMenu())
                            setMenuLocation(-popupWidth+screenLocation.x, screenLocation.y);
                        else{
                            if (!isTopLevelMenu())
                                screenLocation.x += getSize().width;
                            setMenuLocation(screenLocation.x, screenLocation.y);
                        }
                    }
                }
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    private boolean isMenu() {
        return true;
    }

    private boolean isCheckMenuItem() {
        return true;
    }

    public void paint(Graphics g){
        if (!isTopLevelMenu()){
            String text = getText();
            if (tempIcon == null)
                tempIcon = new ImageIcon(ESlateMenu.class.getResource("Images/ScrollRightArrowActive.gif"));

	    Rectangle paintIconR = new Rectangle();
	    Rectangle paintTextR = new Rectangle();
	    Rectangle paintViewR = new Rectangle();
	    Insets paintViewInsets = new Insets(0, 0, 0, 0);

	    paintViewInsets = getInsets(paintViewInsets);
	    paintViewR.x = paintViewInsets.left;
	    paintViewR.y = paintViewInsets.top;
	    paintViewR.width = getWidth() - (paintViewInsets.left + paintViewInsets.right);
            paintViewR.height = getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
            if (paintViewR.width >= 300){
                paintViewR.width = 300 - 30;

	        String clippedText = SwingUtilities.layoutCompoundLabel(
	        (JComponent) this,
		g.getFontMetrics(),
		text,
		tempIcon,
		getVerticalAlignment(),
		getHorizontalAlignment(),
		getVerticalTextPosition(),
		getHorizontalTextPosition(),
		paintViewR,
		paintIconR,
		paintTextR,
		0);
                super.setText(clippedText);
            }
            super.paint(g);
        }else{
            super.paint(g);
        }
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        int width = 0;
        Font f = getFont();
        FontMetrics metrics = getFontMetrics(f);
        width = getMargin().left + getMargin().right + metrics.stringWidth(getText());
        if (width >= 300)
            width = 280;
        if (isTopLevelMenu())
            return new Dimension(width+10,dim.height);
        else
            return new Dimension(width+30,dim.height);


    }

    public Dimension getMinimumSize(){
        return new Dimension(0,19);
    }

}


abstract class ComponentInstantiationAction implements ActionListener {
    String componentClassName;

    public ComponentInstantiationAction(String componentClassName) {
        this.componentClassName = componentClassName;
    }
}


class RollMenuButton extends JMenuItem {
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = 2;
    public static final int ITEM_HEIGHT = 10;
    Dimension prefSize = new Dimension(0, ITEM_HEIGHT);
    ESlateMenu menu;
    javax.swing.border.Border border = new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED);
    int direction = DIRECTION_UP;
    Timer rollTimer;
    JLabel arrowLb;
    Dimension d;


    /**
     * Constructs a rollMenuButton (needed at menu scrolling).
     * @param direction The direction
     * @param menu The ESlateMenu
     */

    RollMenuButton(final ESlateMenu menu, int direction) {
        super();
        if (direction != DIRECTION_UP && direction != DIRECTION_DOWN)
            throw new IllegalArgumentException("Direction has to be one of DIRECTION_UP/DIRECTION_DOWN");
        this.direction = direction;
        this.menu = menu;
        d = new Dimension(menu.computePopupMenuWidth() + 32,ITEM_HEIGHT);
        setMaximumSize(d);
        setMinimumSize(d);
        setPreferredSize(d);
        setOpaque(true);
        setBorder(null);
        //setBackground(Color.red);
        setBorderPainted(true);
        setLayout(new BorderLayout());
        if (direction == DIRECTION_UP)
            arrowLb = new JLabel(new ImageIcon(RollMenuButton.class.getResource("Images/rollUpArrow.gif")));
        else
            arrowLb = new JLabel(new ImageIcon(RollMenuButton.class.getResource("Images/rollDownArrow.gif")));
        add(arrowLb, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    int width = menu.computePopupMenuWidth();
                    if (width>300)
                        width = 300;
                    Dimension d = new Dimension(300, ITEM_HEIGHT);
                    setMaximumSize(d);
                    setMinimumSize(d);
                    setPreferredSize(d);
                    //menu.getPopupMenu().setLayout(new GridLayout(0,1,0,0));
//                    menu.getPopupMenu().setPopupSize(width,menu.getPopupMenu().getSize().height);
                    setBorder(border);
                    rollTimer.start();
                }

                public void mouseExited(MouseEvent e) {
                    setBorder(null);
                    rollTimer.stop();
                    //menu.getPopupMenu().setLayout(new BoxLayout(menu.getPopupMenu(), BoxLayout.Y_AXIS));
                }
            }
        );

        rollTimer = new Timer(100, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            rollTimer.setDelay(80);
                            if (RollMenuButton.this.direction == DIRECTION_UP) {
                                RollMenuButton.this.menu.scroll(true);
                            } else {
                                RollMenuButton.this.menu.scroll(false);
                            }
                        }
                    }
                ){
                    public void stop(){
                        super.stop();
                        setBorder(null);
/*                        menu.getPopupMenu().invalidate();
                        menu.getPopupMenu().doLayout();
                        menu.getPopupMenu().revalidate();
                        menu.getPopupMenu().repaint();
*/
                    }

                    public void start(){
                        super.start();
                        //menu.getPopupMenu().setLayout(new GridLayout(0,1,0,0));
                    }
                 };


    }

    /**
     * Returns the rollMenuButton's preferred size
     */

    public Dimension getPreferredSize() {
        prefSize.width = menu.computePopupMenuWidth() + 32;
        prefSize.height = ITEM_HEIGHT;
        if (prefSize.width>300)
            prefSize.width = 300;
        return new Dimension(prefSize.width,prefSize.height);
    }

}

