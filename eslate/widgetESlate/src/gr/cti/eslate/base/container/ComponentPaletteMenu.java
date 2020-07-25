/* Changes
 * Since version 0.9.7 the ComponentPalleteMenu has been modified so that all the MenuItems
 * are added to the menu. The invisible ones have a zero height. Before 0.9.7 the ComponentPalleteMenu
 * contained only the visible items. The rest were added/removed during scrolling.
 */
/* Additions
 * Each time the popup menu becomes visible 'adjustVisibleMenuItems()' is called, which removes
 * all the items of the menu and then re-adds them. This has to stop.
 */

package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class ComponentPaletteMenu extends JMenu {
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;
    ESlateContainer container;
    boolean valid = false;
    ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();
    ArrayList<JMenu> menus=new ArrayList<JMenu>();
    int firstVisibleItemIndex = -1, lastVisibleItemIndex = -1;
//    RollMenuButton rollUp = new RollMenuButton(this, RollMenuButton.DIRECTION_UP);
//    RollMenuButton rollDown = new RollMenuButton(this, RollMenuButton.DIRECTION_DOWN);
    int menuWidth = 0;
    private boolean expandUp=false;
    /*\InstalledComponentStructure.*/CompoEntry[] menuEntries = null;

    /* This menu supports late menu item loading. The menu items are not added to the
     * menu, until the menu is used for the first time. At that time 'populateComponentPaletteMenu()'
     * is called, which creates all the menu items. These items are not directly inserted in
     * the menu, but they are stored in the 'menuItems' Vector. Then 'adjustVisibleMenuItems()'
     * is called which add to the menu, only these items which fit to the display.
     * All these happen the first time the menu is displayed. The other times, we check if
     * the menu's popup fits vertically in the display and if not its items are readjusted.
     * The Component's icon are loaded asyncronously by another thread, so that the menu
     * first display won't take a lot. Finding the component icons is a heavy operation,
     * cause the components' beaninfos are seeked.
     */
    public ComponentPaletteMenu(ESlateContainer container, String title) {
        super(title);
        this.container = container;
        getPopupMenu().setLayout(new BoxLayout(getPopupMenu(), BoxLayout.Y_AXIS));
        getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (!valid) {
                    menuItems.clear();
                    menus.clear();
                    firstVisibleItemIndex = lastVisibleItemIndex = -1;
                    removeAll();
                    populateComponentPaletteMenu();
                }else{
                    if (getAvailableMenuHeight() < getVisibleMenuItemHeight() ||
                        getAvailableMenuHeight()+30 > getVisibleMenuItemHeight()) {

//                        System.out.println("calling adjustVisibleMenuItems()");
                        adjustVisibleMenuItems();
                        java.awt.Point screenLocation;
                        if (getParent()!=null)
                        	screenLocation=getLocationOnScreen();
                        else
                        	screenLocation=getLocation();
                        screenLocation.x += getSize().width;
                        if (expandUp)
                        	setMenuLocation(screenLocation.x,screenLocation.y-Math.min(getAvailableMenuHeight(),getMaximumMenuHeight()));
                        else
                        	setMenuLocation(screenLocation.x, screenLocation.y);
                        Dimension popupSize = getPopupMenu().getSize();
                        getPopupMenu().setSize(popupSize);
                    }
                }
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

/*    protected synchronized void populateComponentPaletteMenu() {
        Enumeration enum = container.installedComponentNames.keys();
        String componentName, className;
        boolean available;
        firstVisibleItemIndex = 0;
        while (enum.hasMoreElements()) {
            componentName = (String) enum.nextElement();
            className = (String) container.installedComponentNames.get(componentName);
            available = ((Boolean) container.componentAvailability.get(componentName)).booleanValue();
            if (available) {
                JMenuItem mi = createComponentInstantiationMenuItem(componentName, className);
                menuItems.addElement(mi);
            }
        }

        valid = true;
        for (int i=0; i<menuItems.size(); i++) {
            ((PaletteItem) menuItems.elementAt(i)).validateItemWidth();
        }
//        System.out.println("totalHeight: " + h);
        adjustVisibleMenuItems();
//        System.out.println("lastVisibleItemIndex: " + lastVisibleItemIndex);

        Thread thr = new Thread(new Runnable() {

            public void run() {

                attachBeanIcons();

                getPopupMenu().invalidate();

                getPopupMenu().revalidate();

//                System.out.println("thread finished");

            }

        });

        thr.start();
//        h = 0;
    }

*/
    protected void populateComponentPaletteMenu() { //synchronized
        menuEntries = container.installedComponents.getSortedEntries();
//        Enumeration enum = container.installedComponentNames.keys();
        String componentName, className;
//        boolean available;
        firstVisibleItemIndex = 0;
//        while (enum.hasMoreElements()) {
        for (int i=0; i<menuEntries.length; i++) {
            if (menuEntries[i].availability == false) continue;
            componentName = menuEntries[i].name;
            className = menuEntries[i].className;
//            available = ((Boolean) container.componentAvailability.get(componentName)).booleanValue();
//            if (available) {
                JMenuItem mi = createComponentInstantiationMenuItem(componentName, className,menuEntries[i].groupName);
               	menuItems.add(mi);
//            }
        }
        valid = true;
        for (int i=0; i<menuItems.size(); i++) {
            ((PaletteItem) menuItems.get(i)).validateItemWidth();
        }
//        System.out.println("totalHeight: " + h);
        adjustVisibleMenuItems();
//        System.out.println("lastVisibleItemIndex: " + lastVisibleItemIndex);

        Thread thr = new Thread(new Runnable() {

            public void run() {

                attachBeanIcons();

                getPopupMenu().invalidate();

                getPopupMenu().revalidate();

//                System.out.println("thread finished");

            }

        });

        thr.start();
//        h = 0;
    }

    protected void attachBeanIcons() {
        int itemCount = 0;
        for (int i=0; i<menuEntries.length; i++) {
            if (menuEntries[i].availability == false) continue;
            JMenuItem item = (JMenuItem) menuItems.get(itemCount);
            itemCount++;
//            String compoName = item.getText();
            String compoClassName = menuEntries[i].className;
//            System.out.println("compoName: " + compoName + ", compoClassName: " + compoClassName);
            try{
                Class<?> beanClass = Class.forName(compoClassName);

                Image img = BeanInfoFactory.get16x16BeanIcon(beanClass); // ESlateContainerUtils.getBeanIcon(compoClassName, BeanInfo.ICON_COLOR_16x16);
//                System.out.println("attachBeanIcons() " + item.getText() + ": " + img);
                if (img != null)
                    item.setIcon(new ImageIcon(img));
                else
                    item.setIcon(null);
            }catch (Throwable exc) {
            }
        }
        for (int i=0; i<menuItems.size(); i++) {
            ((PaletteItem) menuItems.get(i)).validateItemWidth();
        }
        for (int i=0; i<menus.size(); i++) {
        	((JMenu) menus.get(i)).revalidate();
        	((JMenu) menus.get(i)).repaint();
        }
    }

    protected JMenuItem createComponentInstantiationMenuItem(String componentName, String componentClassName,String componentGroup) {
	      PaletteItem mi = new PaletteItem(this, componentName,componentGroup); //(JMenuItem) add(
//        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
        mi.addActionListener(new ComponentInstantiationAction(componentClassName) {
            public void actionPerformed(ActionEvent e) {
//                containerUtils.forceMenuClose(ESlateContainer.this.getVisibleRect());
                container.createComponent(this.componentClassName, null, null);
            }
        });
        return mi;
    }

    public int getVisibleMenuItemHeight() {
        int compoItemCount = lastVisibleItemIndex-firstVisibleItemIndex+1;
        int visibleMenuItemHeight = compoItemCount * PaletteItem.ITEM_HEIGHT;
//        System.out.println("compoItemCount: " + compoItemCount + ", visibleMenuItemHeight: " + visibleMenuItemHeight);
        return visibleMenuItemHeight;
    }

    public Point getMenuRightEdgeLocation() {
        java.awt.Point screenLocation = getLocation();
        SwingUtilities.convertPointToScreen(screenLocation, getParent());
        screenLocation.x += getSize().width;
        return screenLocation;
    }

    public void setExpandUp(boolean up) {
		expandUp=up;
	}
	
	public boolean getExpandUp() {
		return expandUp;
	}
	
	public int getMaximumMenuHeight() {
		int h=0;
		for (int i=menuItems.size()-1;i>=0;i--)
			h+=((JMenuItem) menuItems.get(i)).getPreferredSize().height;
		return h;
	}

	public int getAvailableMenuHeight() {
        java.awt.Point screenLocation;
        if (getParent()!=null)
        	screenLocation=getLocationOnScreen();
        else
        	screenLocation=getLocation();
        int palleteMenuScreenHeight = screenLocation.y;
        int availableHeight;
        if (expandUp)
        	availableHeight=palleteMenuScreenHeight-30;
        else {
        	int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        	availableHeight=screenHeight - palleteMenuScreenHeight-30;
        }
        return availableHeight;
    }


    public void adjustVisibleMenuItems() {
        int availableHeight = getAvailableMenuHeight();
        removeAll();
        firstVisibleItemIndex = 0;
        lastVisibleItemIndex = -1;
        int h = 0;
//        System.out.println("adjustVisibleMenuItems() menuItems.size(): " + menuItems.size());
        HashMap<String, JMenu> menuhm=new HashMap<String, JMenu>();
        ArrayList<PaletteItem> mis=new ArrayList<PaletteItem>();
        for (int i=0; i<menuItems.size(); i++) {
            PaletteItem mi = (PaletteItem) menuItems.get(i);
            h = h + PaletteItem.ITEM_HEIGHT; //getPreferredSize().height;
            /* If the menu item fits in the screen, then display it, by adding it
             * to the popup menu. If not, then don't display it.
             */
            if (menuEntries[i].groupName!=null && menuEntries[i].groupName.trim().length()>0) {
            	String gn=menuEntries[i].groupName.trim();
            	JMenu menu=(JMenu) menuhm.get(gn);
            	if (menu==null) {
            		menu=new JMenu(gn);
            		menuhm.put(gn,menu);
            	}
            	menu.add(mi);
            } else
            	mis.add(mi);
        	//After making groups, this code is no longer used, because it hides items
//            if (h / availableHeight == 0) {
                mi.setItemVisible(true);
                lastVisibleItemIndex++;
//            }else{
//                System.out.println("12212 Invisible: " + mi.getText() + ", margin: " + mi.getMargin());
//                mi.setItemVisible(false);
//            }
        }
        menus=new ArrayList(menuhm.values());
        Collections.sort(menus,new Comparator() {
			public int compare(Object m0,Object m1) {
				return ((JMenu) m0).getText().compareTo(((JMenu) m1).getText());
			}
		});
        for (int i=0,s=menus.size();i<s;i++)
        	add((JMenu) menus.get(i));
        for (int i=0,s=mis.size();i<s;i++)
        	add((JMenuItem) mis.get(i));
//        insert(rollUp, 0);
//        add(rollDown);

//        System.out.println("adjustVisibleMenuItems() firstVisibleItemIndex: " + firstVisibleItemIndex + ", lastVisibleItemIndex: " + lastVisibleItemIndex);
//        if (firstVisibleItemIndex != 0)
//            rollUp.setItemVisible(true);
//        else
//            rollUp.setItemVisible(false);
//        if (lastVisibleItemIndex != menuItems.size()-1) {
//            rollDown.setItemVisible(true);
//        }else{
//            rollDown.setItemVisible(false);
//        }
    }

    /*public void scroll(boolean upwards) {
        if (upwards) {
            if (firstVisibleItemIndex == 0) return;
            ((PaletteItem) menuItems.get(lastVisibleItemIndex)).setItemVisible(false);
//            remove(getItemCount()-2);
//            insert((JMenuItem)menuItems.elementAt(firstVisibleItemIndex-1), 1);
            ((PaletteItem) menuItems.get(firstVisibleItemIndex-1)).setItemVisible(true);
            firstVisibleItemIndex -=1;
            lastVisibleItemIndex -=1;
            if (!rollDown.isItemVisible() && lastVisibleItemIndex != menuItems.size()-1) {
                rollDown.setItemVisible(true);
                 There is the special case, when there is only one item to roll up to. In
                 * this case the rollDown has to become visible and at the same time the rollUp
                 * has to become invisible. That is in this special case the code enters both
                 * the if-statements "(!rollDown.isItemVisible() && lastVisibleItemIndex != menuItems.size()-1)" and
                 * "(rollUp.isItemVisible() && firstVisibleItemIndex == 0)".
                 * This does not work properly, because of the use of the statement
                 * 'getPopupMenu().getSize()' in the second if block. The returned value
                 * is not right, since the popup menu has to be revalidated first. Therefore for
                 * this special case, the first part of the following if-statement is executed.
                 
                if (rollUp.isItemVisible() && firstVisibleItemIndex == 0) {
                    rollUp.setItemVisible(false);
                    rollUp.rollTimer.stop();
                }else{
                    Dimension popupSize = getPopupMenu().getSize();
                    popupSize.height += RollMenuButton.ITEM_HEIGHT;
                    if (!isHeavyWeightPopup()) {
                        getPopupMenu().pack();
                        getPopupMenu().setPopupSize(popupSize);
                    }
                }
            }
            if (rollUp.isItemVisible() && firstVisibleItemIndex == 0) {
                rollUp.setItemVisible(false);
                rollUp.rollTimer.stop();
                Dimension popupSize = getPopupMenu().getSize();
                popupSize.height -= RollMenuButton.ITEM_HEIGHT;
                if (!isHeavyWeightPopup()) {
                    getPopupMenu().pack();
                    getPopupMenu().setPopupSize(popupSize);
                }
            }
        }else{
            if (lastVisibleItemIndex == menuItems.size()-1) return;
//            remove(1);
            ((PaletteItem)menuItems.get(firstVisibleItemIndex)).setItemVisible(false);
//            insert((JMenuItem)menuItems.elementAt(lastVisibleItemIndex+1), getItemCount()-1); //insertPos);
            ((PaletteItem)menuItems.get(lastVisibleItemIndex+1)).setItemVisible(true);
            firstVisibleItemIndex +=1;
            lastVisibleItemIndex +=1;
            if (!rollUp.isItemVisible()) {
                rollUp.setItemVisible(true);
                 There is the special case, when there is only one item to roll down to. In
                 * this case the rollUp has to become visible and at the same time the rollDown
                 * has to become invisible. That is in this special case the code enters both
                 * the if-statements "(!rollUp.isItemVisible())" and
                 * (rollDown.isItemVisible() && lastVisibleItemIndex == menuItems.size()-1).
                 * This does not work properly, because of the use of the statement
                 * 'getPopupMenu().getSize()' in the second if block. The returned value
                 * is not right, since the popup menu has to be revalidated first. Therefore for
                 * this special case, the first part of the following if-statement is executed.
                 
                if (rollDown.isItemVisible() && lastVisibleItemIndex == menuItems.size()-1) {
                    rollDown.setItemVisible(false);
                    rollDown.rollTimer.stop();
                }else{
//                    Point menuLocation = getMenuRightEdgeLocation();
                    Dimension popupSize = getPopupMenu().getSize();
                    popupSize.height += RollMenuButton.ITEM_HEIGHT;
                    if (!isHeavyWeightPopup()) {
                        getPopupMenu().pack();
                        getPopupMenu().setPopupSize(popupSize);
                    }
//                    setMenuLocation(menuLocation.x, menuLocation.y-RollMenuButton.ITEM_HEIGHT);
                }
            }
            if (rollDown.isItemVisible() && lastVisibleItemIndex == menuItems.size()-1) {
                rollDown.setItemVisible(false);
//                Point menuLocation = getMenuRightEdgeLocation();
                rollDown.rollTimer.stop();
                Dimension popupSize = getPopupMenu().getSize();
                popupSize.height -= RollMenuButton.ITEM_HEIGHT;
                if (!isHeavyWeightPopup()) {
                    getPopupMenu().pack();
                    getPopupMenu().setPopupSize(popupSize);
                }
//                setMenuLocation(menuLocation.x, menuLocation.y);
            }
        }
        getPopupMenu().invalidate();
        getPopupMenu().doLayout();
        getPopupMenu().revalidate();
    }
*/
    public boolean isHeavyWeightPopup() {
        java.awt.Component comp = getPopupMenu().getTopLevelAncestor();
        if (comp.getClass().getName().indexOf("$WindowPopup") != -1) {
//            System.out.println("isHeavyWeightPopup(): " + true);
            return true;
        }else{
//            System.out.println("isHeavyWeightPopup(): " + false);
            return false;
        }
    }

    public JWindow getHeavyWeightPopup() {
        java.awt.Component comp = getPopupMenu().getTopLevelAncestor();
        if (JWindow.class.isAssignableFrom(comp.getClass()))
            return (JWindow) comp;
        return null;
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
    ComponentPaletteMenu menu;
    javax.swing.border.Border border = new javax.swing.border.SoftBevelBorder( javax.swing.border.SoftBevelBorder.LOWERED);
    int direction = DIRECTION_UP;
//    Timer rollTimer;
    private boolean _visible = false;
    JLabel arrowLb;


    public RollMenuButton(ComponentPaletteMenu menu, int direction) {
        super();
        if (direction != DIRECTION_UP && direction != DIRECTION_DOWN)
            throw new IllegalArgumentException("Direction has to be one of DIRECTION_UP/DIRECTION_DOWN");

        this.direction = direction;
        this.menu = menu;
        setOpaque(false);
        setBorder(null);
        setBorderPainted(true);
        setLayout(new BorderLayout());
        if (direction == DIRECTION_UP)
            arrowLb = new JLabel(new ImageIcon(RollMenuButton.class.getResource("images/rollUpArrow.gif")));
        else
            arrowLb = new JLabel(new ImageIcon(RollMenuButton.class.getResource("images/rollDownArrow.gif")));
        add(arrowLb, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (_visible)
                    setBorder(border);
//                rollTimer.start();
            }
            public void mouseExited(MouseEvent e) {
                setBorder(null);
//                rollTimer.stop();
            }
        });

//        rollTimer = new Timer(60, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (RollMenuButton.this.direction == DIRECTION_UP) {
//                    RollMenuButton.this.menu.scroll(true);
////                    System.out.println("Roll up");
//                }else{
////                    System.out.println("Roll down");
//                    RollMenuButton.this.menu.scroll(false);
//                }
//            }
//        });

    }

    public Dimension getPreferredSize() {
        prefSize.width = menu.menuWidth;
        if (_visible) //direction ==DIRECTION_UP
            prefSize.height = ITEM_HEIGHT;
        else
            prefSize.height = 0;
//        System.out.println("RollMenuButton getPreferredSize(): " + prefSize);
        return prefSize;
    }

    public boolean isItemVisible() {
        return _visible;
    }

    public void setItemVisible(boolean visible) {
        if (_visible == visible) return;
        _visible = visible;
        invalidate();
        revalidate();
        if (!_visible) setBorder(null);
        setContentAreaFilled(_visible);
        arrowLb.setVisible(_visible);
    }
}

class PaletteItem extends JMenuItem {
    public static final int ITEM_HEIGHT = 19;
    public static final int ICON_WIDTH = 16;
    Dimension prefSize = new Dimension(0, ITEM_HEIGHT);
    final static int ICON_TEXT_GAP = 4; //This is an assumption which currently is valid, but may change.
    // Have filed a bug report to Sun to give programmatic access to the icon text gap in MenuItems.
    public static final Insets ITEM_WITHOUT_ICON_MARGIN = new Insets(0,ICON_WIDTH + ICON_TEXT_GAP,0,0);
    ComponentPaletteMenu menu;
    private String group;

    public PaletteItem(ComponentPaletteMenu c, String text,String group) {
        super(text);
        this.menu = c;
        java.awt.FontMetrics fm = getToolkit().getFontMetrics(getFont());
        int width = fm.stringWidth(text) + 33 + ICON_WIDTH + ICON_TEXT_GAP;
        if (width > menu.menuWidth)
            menu.menuWidth = width;
        prefSize.width = width;
        setMargin(ITEM_WITHOUT_ICON_MARGIN);
        this.group=group;
    }

    public Dimension getPreferredSize() {
//        System.out.println(getText() + "   prefSize: " + prefSize);
        return prefSize;
    }

    public Dimension getMaximumSize() {
        return prefSize;
    }

    public Dimension getMinimumSize() {
        return prefSize;
    }

    public void setIcon(Icon icon) {
        super.setIcon(icon);
        setMargin(new Insets(0, ICON_WIDTH - icon.getIconWidth(), 0, 0));
    }

    public void setItemVisible(boolean visible) {
        if (visible) {
            if (prefSize.height == 0)
                prefSize.height = ITEM_HEIGHT;
        }else{
            if (prefSize.height != 0)
                prefSize.height = 0;
        }
    }

    public boolean isItemVisible() {
        return (prefSize.height != 0);
    }

    void validateItemWidth() {
        prefSize.width = menu.menuWidth;
    }
}


