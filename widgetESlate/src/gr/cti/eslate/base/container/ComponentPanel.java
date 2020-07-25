package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.event.ESlateComponentAdapter;
import gr.cti.eslate.base.container.event.ESlateComponentEvent;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicBorders;

import com.objectspace.jgl.Array;
//import gr.cti.utils.OnOffButtonGroup;


public class ComponentPanel extends JPanel {
  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;
    ESlateContainer container = null;
    Array compoIcons = new Array();
    int iconWidth = ComponentIcon.NORMAL_WIDTH;
    final static int MARGIN = 2;
    JPopupMenu iconPopupMenu;
    JMenuItem minimize, maximize, minimizeAll, close, restore, removeTitleBarAll;
    JCheckBoxMenuItem titleBarVisible;
    ESlateComponent clickedComponent = null;
    /** This is the listener that triggers the appearance of the pop-up menu with operations on the
     *  active component, when any of the component icons in the ComponentPanel is right-clicked.
     *  This listener should be disabled, when the microworld prohibits the appearance of this
     *  pop-up menu. When this is the case, the listener is removed from the component icons.
     */
    MouseListener iconRightClickListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
//                    setSelection(true);
//                    takeAction();
//                    java.awt.Color backColor = getBackground();
                ComponentIcon icon = (ComponentIcon) e.getSource();
                showIconPopup(icon, e.getX(), e.getY());
            }
        }
    };
    /** This variable reports if the right-click pop-up menu of the ComponentIcons is disabled
     *  or enabled.
     */
    boolean iconPopupMenuEnabled = true;

    private JPanel buttonPanel;

    public ComponentPanel(ESlateContainer container) {
        super(true);
        if (container == null)
            throw new NullPointerException();
        this.container = container;
        if (container.microworld != null)
            iconPopupMenuEnabled = container.microworld.eslateComponentBarMenuEnabled;

        buttonPanel=new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        setLayout(new BorderLayout());
        add(buttonPanel,BorderLayout.CENTER);
        if (container instanceof ESlateComposer) {
	        final JButton btnAdd=new JButton(new ImageIcon(ComponentPanel.class.getResource("images/eslateLogo.gif")));
	        btnAdd.setMargin(new Insets(6,10,6,10));
	        btnAdd.setFocusPainted(false);
	        btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ComponentPaletteMenu cpm=new ComponentPaletteMenu(ComponentPanel.this.container,"");
                                        // I'm not sure what setExpandUp()
                                        // does, but setExpandUp(true) makes
                                        // the menu appear way up there, while
                                        // setExpandUp(false) makes it appear
                                        // at the rign place.
					//cpm.setExpandUp(true);
					cpm.setExpandUp(false);
					JPopupMenu pop=cpm.getPopupMenu();
					pop.setInvoker(btnAdd);
					Point p=new Point(0,0);
					SwingUtilities.convertPointToScreen(p,btnAdd);
					cpm.setLocation(p.x,p.y);
					cpm.populateComponentPaletteMenu();
                                        // This seems to have been copied from
                                        // the ComponentPaletteMenu
                                        // constructor for the expandUp=true
                                        // case, but does not seem to have any
                                        // effect in either case.
					//cpm.setMenuLocation(p.x,p.y-Math.min(cpm.getAvailableMenuHeight(),cpm.getMaximumMenuHeight()));
					pop.setVisible(true);
				}
	        });
	        add(btnAdd,BorderLayout.WEST);
        }
        buttonPanel.add(Box.createGlue());
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                //adjustSize();
                synchronizeComponentPanel();
            }
        });
    }

	/**
	 * @see javax.swing.JPanel#updateUI()
	 */
	public void updateUI() {
		super.updateUI();
		setBorder(BasicBorders.getInternalFrameBorder());
		setBackground(UIManager.getColor("controlShadow"));
	}

    public void synchronizeComponentPanel() {
        for (int i=0; i<container.mwdComponents.size(); i++) {
//1           if (!container.mwdComponents.components.get(i).visualBean)
//1                continue;
//1            ESlateInternalFrame fr = container.mwdComponents.components.get(i).frame;
            ESlateComponent ecomponent = container.mwdComponents.components.get(i);
//0        for (int i=0; i<container.componentFrames.size(); i++) {
//0            ESlateInternalFrame fr = (ESlateInternalFrame) container.componentFrames.at(i);
            ComponentIcon ci = getComponentIcon(ecomponent.handle.getComponentName());
            if (ci != null)
                ecomponent.removeESlateComponentListener(ci.eslateComponentListener);
        }
        clickedComponent = null;
        compoIcons.clear();
        buttonPanel.removeAll();
        buttonPanel.add(Box.createGlue());
        adjustSize();

//0        for (int i=0; i<container.componentFrames.size(); i++) {
//0            ESlateInternalFrame fr = (ESlateInternalFrame) container.componentFrames.at(i);
        for (int i=0; i<container.mwdComponents.size(); i++) {
//1            if (!container.mwdComponents.components.get(i).visualBean)
//1                continue;
//1            ESlateInternalFrame fr = container.mwdComponents.components.get(i).frame;
            ESlateComponent ecomponent = container.mwdComponents.components.get(i);
            String compoName = ecomponent.handle.getComponentName(); //((ESlateHandle) container.eSlateHandles.at(i)).getComponentName();
            if (indexOf(compoName) != -1)
                continue;
  //          addComponentIcon(compoName);
            ComponentIcon ci = new ComponentIcon(compoName, this, ecomponent);
            buttonPanel.add(ci, (compoIcons.size()*2));
            ci.horizontalStrut = Box.createHorizontalStrut(MARGIN);
            buttonPanel.add(ci.horizontalStrut, (compoIcons.size()*2));
            compoIcons.add(ci);
        }
        adjustIconWidth(false);
  /*      for (int i=0; i<compoIcons.size(); i++) {
            String compoName = ((ComponentIcon) compoIcons.at(i)).getName();
            if (container.currentMicroworld.getComponentHandle(compoName) == null) {
                removeComponentIcon((ComponentIcon) compoIcons.at(i));
                i--;
            }
        }
  */
        ESlateComponent activeComponent = container.mwdComponents.activeComponent;
        if (activeComponent != null) { //1 && activeComponent.frame != null) {
//0        if (container.activeFrame != null) {
            String activeComponentName = activeComponent.handle.getComponentName();
            ((ComponentIcon) compoIcons.at(indexOf(activeComponentName))).setSelection(true);
        }

        revalidate();
        repaint();
    }

    public ComponentIcon addComponentIcon(String compoName) {
//1        ESlateInternalFrame fr = container.getComponentFrame(compoName);
        ESlateComponent ecomponent = container.getComponent(compoName);
        ComponentIcon ci = new ComponentIcon(compoName, this, ecomponent);
        buttonPanel.add(ci, (compoIcons.size()*2));
        ci.horizontalStrut = Box.createHorizontalStrut(MARGIN);
        buttonPanel.add(ci.horizontalStrut, (compoIcons.size()*2));
        compoIcons.add(ci);
        adjustIconWidth(false);
        revalidate();
        repaint();
        return ci;
    }

    public void removeComponentIcon(String compoName) {
        int index = indexOf(compoName);
//        System.out.println("removeComponentIcon() index: " + index + ", compoName: " + compoName);
        if (index != -1) {
            removeComponentIcon((ComponentIcon) compoIcons.at(index));
        }
    }

    public void removeComponentIcon(ComponentIcon icon) {
//        java.awt.Component[] comps = getComponents();
//        int index = -1;
//        for (int i=0; i<comps.length; i++) {
//            if (comps[i].equals(icon)) {
//                index = i;
//                break;
//            }
//        }
//        System.out.println("index: " + index);
//        if (index != -1) {
//            remove(index+1); // The horizontal strut
//1          ESlateInternalFrame fr = container.getComponentFrame(icon.compoName);
        ESlateComponent ecomponent = container.getComponent(icon.compoName);
        if (ecomponent != null) {
        	ecomponent.removeESlateComponentListener(icon.eslateComponentListener);
        	if (clickedComponent == ecomponent)
        		clickedComponent = null;
        }

        buttonPanel.remove(icon.horizontalStrut);
        buttonPanel.remove(icon);
        compoIcons.remove(icon);
        adjustIconWidth(true);
//      }
    }

    public int indexOf(String compoName) {
        if (compoName == null) throw new NullPointerException("You have to supply the name of a component");
//        System.out.println("indexOf() compoName: " + compoName);
        for (int i=0; i<compoIcons.size(); i++) {
//            System.out.println("((ComponentIcon) compoIcons.at(i)).getName(): " + ((ComponentIcon) compoIcons.at(i)).getName());
            if (((ComponentIcon) compoIcons.at(i)).getName().equals(compoName))
                return i;
        }
        return -1;
    }

    public void selectComponentIcon(String compoName) {
        int index = indexOf(compoName);
        if (index != -1) {
            ((ComponentIcon) compoIcons.at(index)).setSelection(true);
        }
    }

    public ComponentIcon getComponentIcon(String compoName) {
        int index = indexOf(compoName);
        if (index != -1) {
            return (ComponentIcon) compoIcons.at(index);
        }else{
            return null;
        }
    }

    public void setIconName(String newName, String oldName) {
        int index = indexOf(oldName);
        if (index != -1) {
            ((ComponentIcon) compoIcons.at(index)).setName(newName);
        }
    }

    public void adjustSize() {
        int width = container.getSize().width;
        Dimension size = new Dimension(width, 27);
//        System.out.println("ComponentPanel size: " + size());
//        setMaximumSize(size);
//        setMinimumSize(size);
        setPreferredSize(size);
        adjustIconWidth(true);
    }

    public void buttonPressed(ComponentIcon ci) {
        if (ci.isSelected()) {
  //          System.out.println("Selected: " + ci.getName());
            for (int i=0; i<compoIcons.size(); i++) {
                ComponentIcon compIcon = (ComponentIcon) compoIcons.at(i);
                if (!(ci == compIcon)) {
                    compIcon.setSelected(false);
//                    System.out.println("2. Deselecting " + compIcon.getName());
                }
            }
  //          System.out.println("1. Deselecting " + ci.getName());
  //          ci.setSelected(false);
            return;
        }else{
            ci.setSelected(false);
//            System.out.println("1. Deselecting " + ci.getName());
        }
    }

    public void adjustIconWidth(boolean revalidate) {
        int numOfIcons = compoIcons.size();
        int panelWidth = container.getSize().width;

        int occupiedSpace = numOfIcons * (iconWidth+MARGIN);
        if (occupiedSpace == 0) {
            if (revalidate)
                revalidate();
            return;
        }

        if (occupiedSpace <= panelWidth) {
            iconWidth = panelWidth / numOfIcons;
            if (iconWidth > ComponentIcon.NORMAL_WIDTH)
                iconWidth = ComponentIcon.NORMAL_WIDTH;
        }else if (occupiedSpace > panelWidth) {
            iconWidth = (panelWidth )/ numOfIcons;   // - (numOfIcons * MARGIN)
        }

        iconWidth = iconWidth - MARGIN;

        Dimension dim = new Dimension(iconWidth, 23);
        for (int i=0; i<compoIcons.size(); i++) {
            ComponentIcon icon = (ComponentIcon) compoIcons.at(i);
            icon.setMaximumSize(dim);
            icon.setMinimumSize(dim);
            icon.setPreferredSize(dim);
        }

        if (revalidate)
            revalidate();
    }

    /** Enables or disables the pop-up menu which is displayed when a ComponentIcon
     *  is right-clicked.
     */
    final void setIconPopupMenuEnabled(boolean enabled) {
        if (iconPopupMenuEnabled == enabled) return;

        iconPopupMenuEnabled = enabled;
        if (iconPopupMenuEnabled) {
            for (int i=0; i<compoIcons.size(); i++)
                ((ComponentIcon) compoIcons.at(i)).addMouseListener(iconRightClickListener);
        }else{
            for (int i=0; i<compoIcons.size(); i++)
                ((ComponentIcon) compoIcons.at(i)).removeMouseListener(iconRightClickListener);
        }
    }

    final boolean isIconPopupMenuEnabled() {
        return iconPopupMenuEnabled;
    }

    void showIconPopup(final ComponentIcon icon, final int x, final int y) {
        if (iconPopupMenu == null)
            getIconPopupMenu();

        if (clickedComponent != null) {
            for (int i=0; i<compoIcons.size(); i++) {
System.out.println("((ComponentIcon) compoIcons.at(i)): " + ((ComponentIcon) compoIcons.at(i)));
System.out.println("name: " + ((ComponentIcon) compoIcons.at(i)).getName());
System.out.println("clickedComponent: " + clickedComponent);
System.out.println("clickedComponent.handle: " + clickedComponent.handle);
                if (((ComponentIcon) compoIcons.at(i)).getName().equals(clickedComponent.handle.getComponentName())) {
//                    ((ComponentIcon) compoIcons.at(i)).setBackground(java.awt.Color.lightGray);
                    ((ComponentIcon) compoIcons.at(i)).setBackground(UIManager.getColor("ToggleButton.background"));
                    break;
                }
            }
        }

        clickedComponent = container.getComponent(icon.getName());

        if (clickedComponent == null) {
            minimize.setEnabled(false);
            maximize.setEnabled(false);
            close.setEnabled(false);
            minimizeAll.setEnabled(false);
            titleBarVisible.setSelected(false);
            titleBarVisible.setEnabled(false);
            removeTitleBarAll.setEnabled(false);
        }else{
//            if (container.componentFrozenStateChangeAllowed)
//                removeTitleBar.setEnabled(true);
//            else
//                removeTitleBar.setEnabled(false);
            if (clickedComponent.desktopItem.isClosable())
                close.setEnabled(true);
            else
                close.setEnabled(false);
            if (clickedComponent.desktopItem.isIconifiable() && !clickedComponent.desktopItem.isIcon())
                minimize.setEnabled(true);
            else
                minimize.setEnabled(false);
            if (clickedComponent.desktopItem.isMaximum() || clickedComponent.desktopItem.isIcon())
                restore.setEnabled(true);
            else
              restore.setEnabled(false);
            int componentCount = container.mwdComponents.size();
            boolean minimizeAllEnabled = false;
            for (int i=0; i<componentCount; i++) {
                if (!container.mwdComponents.components.get(i).desktopItem.isIcon()) {
                    minimizeAllEnabled = true;
                    break;
                }
            }
            minimizeAll.setEnabled(minimizeAllEnabled);

            if (clickedComponent.frame == null) { // This is the icon of an invisible component
                maximize.setEnabled(false);
                titleBarVisible.setEnabled(false);
                titleBarVisible.setSelected(false);
                removeTitleBarAll.setEnabled(false);
            }else{
                if (clickedComponent.desktopItem.isMaximizable())
                    maximize.setEnabled(true);
                else
                    maximize.setEnabled(false);
                if (clickedComponent.frame != null && clickedComponent.frame.isTitlePanelVisible())
                    titleBarVisible.setSelected(true);
                else
                    titleBarVisible.setSelected(false);

    //            if (container.componentFrozenStateChangeAllowed)
                    removeTitleBarAll.setEnabled(true);
    //            else
    //                removeTitleBarAll.setEnabled(false);
                boolean existsUnfrozen = false;
    //0            for (int i=0; i<container.componentFrames.size(); i++) {
                ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
                for (int i=0; i<frames.length; i++) {
    //1                if (!container.mwdComponents.components.get(i).visualBean)
    //1                    continue;
    //1                ESlateInternalFrame fr = container.mwdComponents.components.get(i).frame;
    //0                if (!((ESlateInternalFrame) container.componentFrames.at(i)).isTitlePanelVisible()) {
                    if (!frames[i].isTitlePanelVisible()) {
                        existsUnfrozen = true;
                        break;
                    }
                }
                if (existsUnfrozen)
                    removeTitleBarAll.setText(ESlateContainer.containerBundle.getString("BarOnAll"));
                else
                    removeTitleBarAll.setText(ESlateContainer.containerBundle.getString("BarOffAll"));
            }
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iconPopupMenu.show(icon, x, y);
                icon.setBackground(UIManager.getColor("ToggleButton.background").darker());
//                icon.setBackground(java.awt.Color.gray);
            }
        });
    }
    
	private void getIconPopupMenu() {
        iconPopupMenu = new JPopupMenu();
        iconPopupMenu.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (clickedComponent != null) {
                    for (int i=0; i<compoIcons.size(); i++) {
                        if (((ComponentIcon) compoIcons.at(i)).getName().equals(clickedComponent.handle.getComponentName())) {
//                            ((ComponentIcon) compoIcons.at(i)).setBackground(java.awt.Color.lightGray);
                            ((ComponentIcon) compoIcons.at(i)).setBackground(UIManager.getColor("ToggleButton.background"));
                            break;
                        }
                    }
                }
            }
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });


        // RESTORE COMPONENT
        String menuString = ESlateContainer.containerBundle.getString("Restore");
        restore = (JMenuItem) iconPopupMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            restore.setFont(container.greekUIFont);

        restore.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (clickedComponent != null) {
                    restoreIconifiedComponent(clickedComponent);
                }
            }
        });

        // HIDE COMPONENT
        menuString = ESlateContainer.containerBundle.getString("Minimize");
	minimize = (JMenuItem) iconPopupMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            minimize.setFont(container.greekUIFont);

        minimize.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (clickedComponent != null) {
                    try{ clickedComponent.desktopItem.setIcon(true); } catch (Exception exc) {}
                }
            }
        });

        // MAXIMIZE COMPONENT
        menuString = ESlateContainer.containerBundle.getString("Maximize");
	maximize = (JMenuItem) iconPopupMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            maximize.setFont(container.greekUIFont);

        maximize.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (clickedComponent != null) {
                    try{ clickedComponent.desktopItem.setMaximum(true); } catch (Exception exc) {}
                }
            }
        });

        // CLOSE COMPONENT
        menuString = ESlateContainer.containerBundle.getString("Close");
	close = (JMenuItem) iconPopupMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            close.setFont(container.greekUIFont);

        close.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (clickedComponent != null) {
                    try{
                        container.removeComponent(clickedComponent, true, true);
                    } catch (Exception exc) {}
                }
            }
        });

        // REMOVE COMPONENT TITLE BAR
        menuString = ESlateContainer.containerBundle.getString("BarOff");
	titleBarVisible = (JCheckBoxMenuItem) iconPopupMenu.add(new JCheckBoxMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            titleBarVisible.setFont(container.greekUIFont);

        titleBarVisible.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (clickedComponent != null && clickedComponent.frame != null) {
                    try{
                        /* This checks for old-type frozen components and unfreezes them */
                        ESlateInternalFrame frame = clickedComponent.frame;
                        if (frame == null) return;
//if                        if (frame.isFrozen())
//if                            frame.setFrozen(false);
                        frame.setTitlePanelVisible(titleBarVisible.isSelected());
                    } catch (Exception exc) {}
                }
            }
        });

        iconPopupMenu.addSeparator();

        // MINIMIZE ALL COMPONENTS
        menuString = ESlateContainer.containerBundle.getString("MinimizeAll");
	minimizeAll = (JMenuItem) iconPopupMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            minimizeAll.setFont(container.greekUIFont);

        minimizeAll.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                //ESlateInternalFrame fr = null;
//0                for (int i=0; i<container.componentFrames.size(); i++) {
//0                    fr = (ESlateInternalFrame) container.componentFrames.at(i);
                for (int i=0; i<container.mwdComponents.size(); i++) {
                    try{
                        container.mwdComponents.components.get(i).desktopItem.setIcon(true);
                    } catch (Exception exc) {}
                }
            }
        });

        // REMOVE TITLE BAR ALL COMPONENTS
        menuString = ESlateContainer.containerBundle.getString("BarOnAll");
	removeTitleBarAll = (JMenuItem) iconPopupMenu.add(new JMenuItem(menuString));
//        if (container.containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            removeTitleBarAll.setFont(container.greekUIFont);

        removeTitleBarAll.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                boolean existsUnfrozen = removeTitleBarAll.getText().equals(ESlateContainer.containerBundle.getString("BarOffAll"));
//                System.out.println("existsUnfrozen: " + existsUnfrozen);
                //ESlateInternalFrame fr = null;
                if (existsUnfrozen) {
//0                    for (int i=0; i<container.componentFrames.size(); i++) {
//0                        ((ESlateInternalFrame) container.componentFrames.at(i)).setTitlePanelVisible(false);
//0                    }
                    for (int i=0; i<container.mwdComponents.size(); i++) {
                        if (!container.mwdComponents.components.get(i).visualBean)
                            continue;
                        container.mwdComponents.components.get(i).frame.setTitlePanelVisible(false);
                    }
                }else{
//0                    for (int i=0; i<container.componentFrames.size(); i++) {
//0                        ESlateInternalFrame ifr = (ESlateInternalFrame) container.componentFrames.at(i);
                    for (int i=0; i<container.mwdComponents.size(); i++) {
                        if (!container.mwdComponents.components.get(i).visualBean)
                            continue;
                        ESlateInternalFrame ifr = container.mwdComponents.components.get(i).frame;
                        /* This checks for old-type frozen components and unfreezes them */
//if                        if (ifr.isFrozen())
//if                            ifr.setFrozen(false);
                        ifr.setTitlePanelVisible(true);
                    }
                }
            }
        });

    }

    void restoreIconifiedComponent(ESlateComponent eslateComponent) {
        try {
            DesktopItem desktopItem = eslateComponent.desktopItem;
            if (desktopItem.isIcon()) {
                /* If the frame was maximized before it was iconified, then maximize it again.
                 * Otherwise restore the frame.
                 */
                if (desktopItem.getDesktopItemSize().equals(container.scrollPane.getViewport().getSize()) &&
                    desktopItem.getDesktopItemLocation().equals(container.scrollPane.getViewport().getViewPosition()))
                    desktopItem.setMaximum(true);
                else{
                    desktopItem.setIcon(false);
                }
            }else{
                if (desktopItem.isMaximum())
                    desktopItem.setMaximum(false);
            }
        } catch (java.beans.PropertyVetoException e6) { }
    }

}


class ComponentIcon extends JToggleButton {
  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1L;
    protected String compoName;
    ComponentPanel compoPanel;
//    protected static Font font = new Font("Helvetica", Font.PLAIN, 10);
//    protected static Font iconifiedFont = new Font("Helvetica", Font.ITALIC, 10);
    //private static final int SMALLEST_WIDTH = 45;
    static final int NORMAL_WIDTH = 80;
    //private static final int MAX_WIDTH = 100;
    java.awt.Component horizontalStrut = null;
    ESlateComponentAdapter eslateComponentListener;

    public ComponentIcon(String compoName, ComponentPanel panel, ESlateComponent ecomponent) {
        super(compoName);
        this.compoName = compoName;
        this.compoPanel = panel;

        Image beanImg = BeanInfoFactory.get16x16BeanIcon(ecomponent.handle.getComponent().getClass()); // .getBeanIcon(fr.eSlateHandle.getComponent().getClass(), java.beans.BeanInfo.ICON_COLOR_16x16);
//        if (beanImg == null)
//            beanImg = ESlateContainerUtils.getBeanIcon(fr.eSlateHandle.getComponent().getClass(), java.beans.BeanInfo.ICON_MONO_16x16);

        if (beanImg != null)
            setIcon(new ImageIcon(beanImg));

//        setBorder(new CompoundBorder(getBorder(), new EmptyBorder(0, 3, 0, 3)));
        setMargin(new java.awt.Insets(0, 0, 0, 0));
        setFocusPainted(false);
        if (ecomponent.desktopItem.isIcon())
            setFont(getFont().deriveFont(Font.ITALIC)/*iconifiedFont*/);
        else
            setFont(getFont().deriveFont(Font.PLAIN)/*font*/);
        setToolTipText(compoName);

        eslateComponentListener = new ESlateComponentAdapter() {
            public void componentIconified(ESlateComponentEvent e) {
                ComponentIcon.this.setFont(ComponentIcon.this.getFont().deriveFont(Font.ITALIC));
//                ComponentIcon.this.setFont(iconifiedFont);
            }
            public void componentRestored(ESlateComponentEvent e) {
                ComponentIcon.this.setFont(ComponentIcon.this.getFont().deriveFont(Font.PLAIN));
//                ComponentIcon.this.setFont(font);
            }
        };
        ecomponent.addESlateComponentListener(eslateComponentListener);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                takeAction();
            }
        });

        if (compoPanel.iconPopupMenuEnabled)
            addMouseListener(compoPanel.iconRightClickListener);
    }

    public String getName() {
        return compoName;
    }

    public void setName(String name) {
        compoName = name;
        setText(name);
    }

    public void setSelection(boolean selected) {
        super.setSelected(selected);
        compoPanel.buttonPressed(this);
    }

    private void takeAction() {
//1        ESlateInternalFrame fr = compoPanel.container.getComponentFrame(ComponentIcon.this.compoName);
        ESlateComponent ecomponent = compoPanel.container.mwdComponents.getComponent(ComponentIcon.this.compoName);
        DesktopItem desktopItem = ecomponent.desktopItem;
//        System.out.println("takeAction fr: " + fr.getTitle());
        try{
//            if (!fr.isFrozen()) {
                compoPanel.buttonPressed(ComponentIcon.this);
                repaint();
                if (!desktopItem.isActive()) {
                    if (desktopItem.isIcon()) {
                        /* If the frame was maximized before it was iconified, then maximize it again.
                         * Otherwise restore the frame.
                         */
                        if (desktopItem.getDesktopItemSize().equals(compoPanel.container.scrollPane.getViewport().getSize()) &&
                            desktopItem.getDesktopItemLocation().equals(compoPanel.container.scrollPane.getViewport().getViewPosition()))
                            desktopItem.setMaximum(true);
                        else{
//                            System.out.println("Setting frame " + fr.getTitle() + " not iconified");
                            desktopItem.setIcon(false);
                        }
                    }
                    desktopItem.setActive(true);
//                    compoPanel.container.setActiveComponent(ecomponent, true);
                }else{
                    if (!desktopItem.isIcon())
                        desktopItem.setIcon(true);
                    else
                        compoPanel.restoreIconifiedComponent(ecomponent);
//                    System.out.println("ComponentPanel desktopItem.isIcon(): " + desktopItem.isIcon());
                }
        }catch (Exception exc) {
            System.out.println("Exception " + exc.getClass() + " in ComponentPanel actionListener()");
        }
    }
}
