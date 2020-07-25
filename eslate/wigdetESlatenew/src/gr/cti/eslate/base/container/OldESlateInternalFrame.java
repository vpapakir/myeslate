package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.container.event.ESlateComponentEvent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;


final public class OldESlateInternalFrame extends JInternalFrame implements DesktopItem {
    public static final Dimension MIN_SIZE = new Dimension(58, 20);
    public static final int NORTHPANE_HEIGHT = 18;
    public final static Color TITLE_BG_COLOR = new Color(49, 170, 152);
    public final static Color ACTIVATION_COLOR = new Color(204, 153, 0);
    public final static Color BORDER_COLOR = Color.gray;
    public final static Border WEST_BORDER = new CompoundBorder(new EmptyBorder(3, 0, 3, 3), new DegradatedColorMatteBorder(0, 3, 0, 0));
    public final static Border EAST_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 3, 0), new DegradatedColorMatteBorder(0, 0, 0, 3));
    public final static Border NORTH_BORDER = new CompoundBorder(new EmptyBorder(0, 3, 3, 3), new DegradatedColorMatteBorder(3, 0, 0, 0));
    public final static Border SOUTH_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 0, 3), new DegradatedColorMatteBorder(0, 0, 3, 0));
    public final static Border SOUTH_WEST_BORDER = new CompoundBorder(new EmptyBorder(3, 0, 0, 3), new DegradatedColorMatteBorder(0, 3, 3, 0));
    public final static Border SOUTH_EAST_BORDER = new CompoundBorder(new EmptyBorder(3, 3, 0, 0), new DegradatedColorMatteBorder(0, 0, 3, 3));
    public final static Border NORTH_EAST_BORDER = new CompoundBorder(new EmptyBorder(0, 3, 3, 0), new DegradatedColorMatteBorder(3, 0, 0, 3));
    public final static Border NORTH_WEST_BORDER = new CompoundBorder(new EmptyBorder(0, 0, 3, 3), new DegradatedColorMatteBorder(3, 3, 0, 0));
    public final static Border NORTH_SOUTH_WEST_EAST_BORDER = new DegradatedColorMatteBorder(3, 3, 3, 3);
    private final static LineBorder ORANGE_LINE_BORDER = new LineBorder(Color.orange, 1);
    private static final LineBorder GRAY_LINE_BORDER = new LineBorder(Color.lightGray, 1);
//    CompoundBorder frameNormalBorder = new CompoundBorder(new BevelBorder(BevelBorder.RAISED, new Color(230, 230, 230), Color.black), grayLineBorder);
    public static final Border FRAME_NORMAL_BORDER = new EmptyBorder(3,3,3,3); //new LineBorder(new Color(0,0,0,0), 2);
    public static final Border FRAME_SELECTED_BORDER = new CompoundBorder(new EmptyBorder(2,2,2,2), ORANGE_LINE_BORDER);

    //    JButton iconButton, maxButton, closeButton;
//    Component nameLabel;
    JPanel compPanel, intFrameControlPanel, northPaneContentPanel;
    JComponent northPane;
    boolean closable = true, iconifiable = true, maximizable = true;
    boolean titlePanelVisible = true;
    ESlateContainer container;
    ESlateHandle eSlateHandle;
    ESlateComponent eslateComponent = null;
    NameLabelMouseListener nameLabelMouseEventDispatcher;
    OldESlateInternalFrameAdapter frameAdapter;
//    NameLabelMouseListener2 nameLabelMouseEventDispatcher2;
    /* The size the frame has, when not iconified or maximized.
     */
    Dimension realSize;
    /* The location of the frame, when not iconified or maximized.
     */
    Point realLocation;
    boolean frozen = false;
    static Icon eCloseIcon = null;
    static Icon eIconIcon = null; //UIManager.getIcon("InternalFrame.iconifyIcon");
    static Icon eMaxIcon = null; //UIManager.getIcon("InternalFrame.maximizeIcon");
	  static Icon eMinIcon = null; //UIManager.getIcon("InternalFrame.minimizeIcon");
    JButton eCloseButton, eMaxButton, eIconButton;
    private boolean _icon = false;
    private boolean _maximum = false;
    private boolean _active = false;
    private boolean _activatedOnMousePress = true;
    protected boolean disableMaximizeChange = false;
    ESlateBasicInternalFrameUI frameUI = null;
    boolean frameDragDisabledTemporarily = false;
    /* Remembers whether 'FRAME_NORMAL_BORDER' or 'FRAME_SELECTED_BORDER' was last
     * applied to this ESlateInternalFrame.
     */
    private Border prevBorder = FRAME_NORMAL_BORDER;
    /* The size a BeanXporter bean had, before its iconification. BeanXporter beans
     * do not get iconified by calling setVisible(false), but rather by calling
     * setSize(0, 0). This happens because setVisible(false) crashes the 1.3 VM, when
     * the component that is iconified is the Navigator BeanXporter bean.
     */
     Dimension sizeBeforeIconification = null;


    /* This constructor exists only for the ESlateContainer's EMPTY_FRAME.
     */
    OldESlateInternalFrame(String title) {
        super(title);
    }

    public OldESlateInternalFrame(ESlateContainer container, ESlateHandle eSlateHandle) {
        super();
        this.container = container;
        this.eSlateHandle = eSlateHandle;
        setUI(frameUI = new ESlateBasicInternalFrameUI(this));
        setMinimumSize(MIN_SIZE);

//        setUI(new ESlateInternalFrameUI(this));

        northPane = ((BasicInternalFrameUI) this.getUI()).getNorthPane();

        /* The following listener makes sure that the component won't get dragged around
         * when the right mouse button is pressed.
         */
        northPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) &&
                   ((ESlateContainerWindowsDesktopManager) OldESlateInternalFrame.this.container.lc.getDesktopManager()).frameDragEnabled) {
                    ((ESlateContainerWindowsDesktopManager) OldESlateInternalFrame.this.container.lc.getDesktopManager()).frameDragEnabled = false;
                    frameDragDisabledTemporarily = true;
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (frameDragDisabledTemporarily) {
                    ((ESlateContainerWindowsDesktopManager) OldESlateInternalFrame.this.container.lc.getDesktopManager()).frameDragEnabled = true;
                    frameDragDisabledTemporarily = false;
                }
            }
        });

        /* Install a component listener to the ESlateInternalFrame, which notifies the frame, whenevr
         * the size of the component it encloses changes.
         */
/*        if (Component.class.isAssignableFrom(eSlateHandle.getComponent().getClass())) {
            Component comp = (Component) eSlateHandle.getComponent();
            comp.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    int w = java.lang.Math.abs(ESlateInternalFrame.this.getSize().width - componentSize.width);
                    int h = java.lang.Math.abs(ESlateInternalFrame.this.getSize().height - componentSize.height);
                    if (w > 0 && h > 0) {
                        Dimension newSize  = ((Component) e.getSource()).getSize();
                        ESlateInternalFrame.this.setSize(newSize.width+w, newSize.height+h);
                    }
                }
            });
        }
*/
        nameLabelMouseEventDispatcher = new NameLabelMouseListener();
        compPanel = eSlateHandle.getMenuPanel();
        compPanel.addMouseListener(nameLabelMouseEventDispatcher);
        compPanel.addMouseMotionListener(nameLabelMouseEventDispatcher);
//        nameLabelMouseEventDispatcher2 = new NameLabelMouseListener2();
//        compPanel.addMouseListener(nameLabelMouseEventDispatcher2);
//        compPanel.addMouseMotionListener(nameLabelMouseEventDispatcher2);
//System.out.println("Constructor nameLabel: " + ((ESlateHandle$MenuPanel) compPanel).getNameLabel().getText());
        eSlateHandle.getMenuPanel().getNameLabel().addMouseListener(nameLabelMouseEventDispatcher);
        eSlateHandle.getMenuPanel().getNameLabel().addMouseMotionListener(nameLabelMouseEventDispatcher);
//        ((ESlateHandle.MenuPanel) compPanel).getGlue().addMouseListener(nameLabelMouseEventDispatcher);
//        ((ESlateHandle.MenuPanel) compPanel).getGlue().addMouseMotionListener(nameLabelMouseEventDispatcher);
        setBorder(FRAME_NORMAL_BORDER);

//        System.out.println("Content pane: " + getContentPane());
        /* The internal frame has to be opaque.
         */
        ((JPanel) getContentPane()).setOpaque(false);
        getRootPane().setOpaque(false);
        setOpaque(false);
//        setAutoscrolls(true);
//        System.out.println("getAutoscrolls(): " + getAutoscrolls());
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        if (this.frozen == frozen)
            return;

        this.frozen = frozen;
        if (!frozen) {
            Component comp = (Component) this.eSlateHandle.getComponent();
            /* The location and size of the component may have changed (i.e. by the propery editor)
             * so recalculate the location and size of the ESlateInternalFrame.
             */
            Rectangle compBounds = comp.getBounds();
            /* The actual frame location is 18(NORTHPANE_HEIGHT) pixels higher than the component location
             */
            Insets borderInsets = getBorder().getBorderInsets(this);
            Rectangle frameBounds = new Rectangle();
            frameBounds.x = compBounds.x - borderInsets.left;
            if (titlePanelVisible) {
                frameBounds.y = compBounds.y - NORTHPANE_HEIGHT - borderInsets.top;
                frameBounds.height = compBounds.height + borderInsets.bottom + borderInsets.top + NORTHPANE_HEIGHT;
            }else{
                frameBounds.y = compBounds.y - borderInsets.top;
                frameBounds.height = compBounds.height + borderInsets.bottom + borderInsets.top;
            }
            frameBounds.width = compBounds.width + borderInsets.left + borderInsets.right;

            container.lc.remove(comp);
            getContentPane().add(comp);

            if (titlePanelVisible)
                showTitlePanel();

            setBounds(frameBounds);

            if (_icon)
                setVisible(false);
            else
                setVisible(true);
        }else{
//            System.out.println("Freezing component");
            boolean repaintDesktop = false;
            Component comp = this.getContentPane().getComponent(0);

            Insets borderInsets = getBorder().getBorderInsets(this);
            Rectangle compBounds = SwingUtilities.convertRectangle(this, comp.getBounds(), container.lc);
            compBounds.x = compBounds.x + borderInsets.left;
            if (titlePanelVisible)
                compBounds.y = compBounds.y + NORTHPANE_HEIGHT + borderInsets.top;
            else
                compBounds.y = compBounds.y + borderInsets.top;

            getContentPane().remove(comp);

            container.lc.add(comp, new Integer(3));
            if (!_icon) {
                repaintDesktop = true;
                setVisible(true);
            }else{
                setVisible(false);
            }
            comp.setBounds(compBounds);
            comp.validate();

            if (repaintDesktop) {
                container.lc.revalidate();
                container.lc.repaint();
            }
        }
    }

    protected void setTitleColor(Color color) {
        if (frozen) return;
        if (northPaneContentPanel != null) {
            northPaneContentPanel.setBackground(color);
        }
        if (compPanel != null) {
            compPanel.setBackground(color);
        }
        if (intFrameControlPanel != null) {
            intFrameControlPanel.setBackground(color);
        }

/*        northPane.getComponent(0).setBackground(color);
        ((Container) northPane.getComponent(0)).getComponent(0).setBackground(color);
        if (((Container) northPane.getComponent(0)).getComponentCount() > 1)
            ((Container) northPane.getComponent(0)).getComponent(1).setBackground(color);
*/
    }

    public String getTitle() {
        if (eSlateHandle != null)
            return eSlateHandle.getComponentName();
        else
            return null;
    }

    protected class NameLabelMouseListener implements javax.swing.event.MouseInputListener {
        public void mousePressed(MouseEvent e) {
//            System.out.println("Mouse pressed on title panel");
            if (container instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) container;
                if (composer.componentPopupMenu != null)
                    composer.componentPopupMenu.setVisible(false);
            }
            retargetNameLabelMouseEvent(northPane, e);
        }

        public void mouseReleased(MouseEvent e) {
            if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                retargetNameLabelMouseEvent(northPane, e);
                Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getX(), e.getY(), OldESlateInternalFrame.this);
//if                container.showComponentPopup(OldESlateInternalFrame.this, p.x, p.y);
            }else{
                retargetNameLabelMouseEvent(northPane, e);
            }
        }
        public void mouseClicked(MouseEvent e) {
            retargetNameLabelMouseEvent(northPane, e);
        }
        public void mouseEntered(MouseEvent e) {
            retargetNameLabelMouseEvent(northPane, e);
        }
        public void mouseExited(MouseEvent e) {
            retargetNameLabelMouseEvent(northPane, e);
        }
        public void mouseDragged(MouseEvent e) {
            retargetNameLabelMouseEvent(northPane, e);
        }
        public void mouseMoved(MouseEvent e) {
            retargetNameLabelMouseEvent(northPane, e);
        }
        public void retargetNameLabelMouseEvent(Component mouseEventTarget, MouseEvent e) {
            Point p = javax.swing.SwingUtilities.convertPoint((Component) e.getSource(),
                      e.getX(), e.getY(),
                      mouseEventTarget);
            MouseEvent retargeted = new MouseEvent(mouseEventTarget,
                                                   e.getID(),
                                                   e.getWhen(),
                                                   e.getModifiers(),
                                                   p.x,
                                                   p.y,
                                                   e.getClickCount(),
                                                   e.isPopupTrigger());
            mouseEventTarget.dispatchEvent(retargeted);
        }
    }

    protected void formulateTitlePanel() {
//        System.out.println("formulateTitlePanel 0");
        try{
            ((Container) eSlateHandle.getComponent()).remove(eSlateHandle.getMenuPanel());
        }catch (Exception exc) {
            System.out.println("Cannot force menuPanel removal");
        }
//        System.out.println("formulateTitlePanel 1");
        northPane.setLayout(new BorderLayout(3,0));
        Dimension northPaneDim = new Dimension(0, NORTHPANE_HEIGHT);
        northPane.setPreferredSize(northPaneDim);
        northPane.setMinimumSize(northPaneDim);
        northPane.setMaximumSize(northPaneDim);
//        System.out.println("formulateTitlePanel 2");

/*      Component[] comps = northPane.getComponents();
        System.out.println("northPane comps[] size: " + comps.length);
        for (int i=0; i<comps.length; i++) {
            System.out.println();
            System.out.println(comps[i]);
        }
*/
        //Java 1.2 The button order is different
//        iconButton = (JButton) northPane.getComponent(1); //0
        Image beanImg = BeanInfoFactory.get16x16BeanIcon(eSlateHandle.getComponent().getClass()); //.getBeanIcon(eSlateHandle.getComponent().getClass(), BeanInfo.ICON_COLOR_16x16);
//        if (beanImg == null)
//            beanImg = ESlateContainerUtils.getBeanIcon(eSlateHandle.getComponent().getClass(), BeanInfo.ICON_MONO_16x16);


        if (eIconIcon == null)
            eIconIcon = UIManager.getIcon("InternalFrame.iconifyIcon");
        eIconButton = new JButton(eIconIcon);
        eIconButton.setRequestFocusEnabled(false);
        eIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          	    if(isIconifiable()) {
	                  if(!isIcon())
                		    try {
                            setIcon(true);
                        } catch (PropertyVetoException e1) { }
            		    else {
///            		        try {
///		                        setIcon(false);
///                      			if (isMaximizable() && isMaximum()) {
///                      			    setMaximum(false);
///		                      	}
///                		    } catch (PropertyVetoException e1) { }
            		    }
          	    }
            }
        });
//        maxButton = (JButton) northPane.getComponent(2); //1
        if (eMaxIcon == null)
            eMaxIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
        eMaxButton = new JButton(eMaxIcon);
        eMaxButton.setRequestFocusEnabled(false);
        eMaxButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isMaximizable()) {
                    if(!isMaximum()) {
                        try {
//                            container.setFrameSelected(ESlateInternalFrame.this, true);
                            container.setActiveComponent(eslateComponent, true);
                            setMaximum(true);
                        } catch (PropertyVetoException e5) { }
                    } else {
                        try {
                            setMaximum(false);
///                           if (isIconifiable() && isIcon()) {
///                               setIcon(false);
///                           }
                        } catch (PropertyVetoException e6) { }
                    }
                }
            }
        });
//        closeButton = (JButton) northPane.getComponent(3); //2
        if (eCloseIcon == null)
            eCloseIcon = UIManager.getIcon("InternalFrame.closeIcon");
        eCloseButton = new JButton(eCloseIcon);
        eCloseButton.setRequestFocusEnabled(false);
        eCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//1                container.removeComponent(ESlateInternalFrame.this, true, true);
                container.removeComponent(OldESlateInternalFrame.this.eslateComponent, true, true);
            }
        });

        northPane.removeAll();
//        System.out.println("formulateTitlePanel 3");

        northPaneContentPanel = new JPanel(true);
        northPaneContentPanel.setLayout(new BorderLayout());
        northPaneContentPanel.setBorder(new InternalFrameTitleBorder(this));

        Dimension ctrlButtonDim = new Dimension(16, 14); //12, 11);
        eIconButton.setPreferredSize(ctrlButtonDim);
        eIconButton.setMaximumSize(ctrlButtonDim);
        eIconButton.setMinimumSize(ctrlButtonDim);
        eMaxButton.setPreferredSize(ctrlButtonDim);
        eMaxButton.setMaximumSize(ctrlButtonDim);
        eMaxButton.setMinimumSize(ctrlButtonDim);
        eCloseButton.setPreferredSize(ctrlButtonDim);
        eCloseButton.setMaximumSize(ctrlButtonDim);
        eCloseButton.setMinimumSize(ctrlButtonDim);

        JPanel menuPanel = eSlateHandle.getMenuPanel();
        menuPanel.setBorder(null);

        JPanel IDPanel = new JPanel(true);
        IDPanel.setOpaque(false);
        IDPanel.setLayout(new BoxLayout(IDPanel, BoxLayout.X_AXIS));
        if (beanImg != null) {
            IDPanel.add(new JLabel(new ImageIcon(beanImg)), BorderLayout.WEST);
            IDPanel.add(Box.createHorizontalStrut(3));
        }
        IDPanel.add(menuPanel);
//        setBarRenamingEnabled(false);

//        pinButton = (JButton) menuPanel.getComponent(3);
//        helpButton = (JButton) menuPanel.getComponent(4);
//        infoButton = (JButton) menuPanel.getComponent(5);

        intFrameControlPanel = new JPanel(true);
        intFrameControlPanel.setLayout(new BoxLayout(intFrameControlPanel, BoxLayout.X_AXIS));
//        Dimension dim = new Dimension(48, 16);
//        intFrameControlPanel.setPreferredSize(dim);
//        intFrameControlPanel.setMaximumSize(dim);
//        intFrameControlPanel.setMinimumSize(dim);
//        intFrameControlPanel.setBackground(TITLE_BG_COLOR);

        intFrameControlPanel.add(eIconButton);
        intFrameControlPanel.add(eMaxButton);
        intFrameControlPanel.add(eCloseButton);

        northPaneContentPanel.add(IDPanel, BorderLayout.CENTER);
        northPaneContentPanel.add(intFrameControlPanel, BorderLayout.EAST);

        northPane.add(northPaneContentPanel);

        setTitleColor(TITLE_BG_COLOR);
        northPane.validate();

        revalidate();
    }

    public void adjustTitlePanelToMicroworldSettings() {
        setFrameIconifiable(container.currentView.minimizeButtonVisible);
        setFrameMaximizable(container.currentView.maximizeButtonVisible);
        setFrameClosable(container.currentView.closeButtonVisible);
        setComponentNameChangeableFromMenuBar(container.currentView.controlBarTitleActive);
        setHelpButtonVisible(container.currentView.helpButtonVisible);
        setPinButtonVisible(container.currentView.pinButtonVisible);
        setInfoButtonVisible(container.currentView.infoButtonVisible);
        setResizable(container.currentView.resizeAllowed);
        setTitlePanelVisible(container.currentView.controlBarsVisible);
    }

    public void setResizable(boolean resizable) {
        if (frozen) return;
        if (resizable) {
            if (!isResizable()) {
                super.setResizable(true);
                container.setMicroworldChanged(true);
//                System.out.println("15. microworld changed = true");
            }
        }else{
            if (isResizable()) {
                super.setResizable(false);
                container.setMicroworldChanged(true);
//                System.out.println("16. microworld changed = true");
            }
        }
    }

/*    public void setSelected(boolean selected) throws PropertyVetoException {

        super.setSelected(selected);
        setMaximum(_maximum);
        setIcon(_icon);

    }
*/
    public void setMaximum(boolean maximum) throws PropertyVetoException {
//        System.out.println("setMaximum("+maximum+") was called " + "frame: " + getTitle() + " disableMaximizeChange? " + disableMaximizeChange);
//        System.out.println("isMaximizable: " + isMaximizable());
//        System.out.println("_maximum: " + _maximum + ", maximum: " + maximum);
        if (disableMaximizeChange) return;
        if (_maximum == maximum) return;
        if (!isMaximizable()) return;
//        if (frozen) return;

        if (maximum) {
            /* Store the size and location of the frame, only it it's not iconified.
             */
            if (!_icon) {
                realSize = getSize();
                realLocation = getLocation();
            }
        }
        if (maximum) {
            /* Set the window equal to the view port of the scroll pane of the desktop pane
             * of the container.
             */
            setLocation(container.scrollPane.getViewport().getViewPosition());
            setSize(container.scrollPane.getViewport().getSize());
            /* Nullify the border, or else due to the EmptyBorder the component won't
             * reach the edges of the desktop.
             */
            setBorder(null);
        }else{
            setLocation(realLocation);
            setSize(realSize);
        }
        _maximum = maximum;
        if (!_maximum) {
            /* Re-establish the frame's border: 'frameNormalBorder' if the title bar is
             * visible, 'frameSelectedBorder' if the title bar is invisible. This has to
             * be done after _maximum is set to false, because any call to setBorder() while
             * _maximum is true has no effect.
             */
            if (isTitlePanelVisible())
                setBorder(FRAME_NORMAL_BORDER);
            else
                setBorder(FRAME_SELECTED_BORDER);
        }

        if (_icon && _maximum) {
            setVisible(true);
            _icon = false;
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED);
        }
///        super.setMaximum(maximum);

        // Adjust the icons of the buttons
      	if(isIcon()) {
///      	    eIconButton.setIcon(eMinIcon);
///	          eMaxButton.setIcon(eMaxIcon);
        } else if (isMaximum()) {
      	    eIconButton.setIcon(eIconIcon);
            if (eMinIcon == null)
                eMinIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
	          eMaxButton.setIcon(eMinIcon);
        } else {
      	    eIconButton.setIcon(eIconIcon);
	          eMaxButton.setIcon(eMaxIcon);
        }
    }

    public boolean isMaximum() {
        return _maximum;
    }


    public void setIcon(boolean icon) throws PropertyVetoException {
//        System.out.println(eSlateHandle.getComponentName() + " setIcon("+icon+") was called");
        if (_icon == icon) return;
        if (!isIconifiable()) return;

//        if (frozen) return;
        if (icon) {
            /* Store the size and location of the frame, only if it is not maximized.
             */
            if (!_maximum) {
                realSize = getSize();
                realLocation = getLocation();
            }
        }
///        super.setIcon(icon);
        if (!eSlateHandle.usesBeanXporterBeans())
            setVisible(!icon);
        else{
            if (icon) {
                sizeBeforeIconification = getSize();
                setSize(0,0);
            }else
                setSize(sizeBeforeIconification);
        }

//        System.out.println("Setting _icon: " + icon);
        _icon = icon;
        if (_icon && _maximum)
            _maximum = false;

        // Adjust the button icons
      	if(isIcon()) {
///      	    eIconButton.setIcon(eMinIcon);
///	          eMaxButton.setIcon(eMaxIcon);
        } else if (isMaximum()) {
      	    eIconButton.setIcon(eIconIcon);
            if (eMinIcon == null)
                eMinIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
	          eMaxButton.setIcon(eMinIcon);
        } else {
      	    eIconButton.setIcon(eIconIcon);
	          eMaxButton.setIcon(eMaxIcon);
        }

/*        if (eslateComponent != null) {
            if (icon)
                eslateComponent.fireComponentIconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ICONIFIED, eSlateHandle.getComponentName()));
            else
                eslateComponent.fireComponentDeiconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEICONIFIED, eSlateHandle.getComponentName()));
        }
*/
        if (_icon) {
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_ICONIFIED);
        }else{
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED);
        }
    }

    public boolean isIcon() {
//        System.out.println("ESplateInternalFrame isIcon(): " + _icon);
        return _icon;
    }

    /* setselected() and isSelected() have been overriden and the local private variable
     * 'active' has been introduced because of the requirement that iconified frames
     * cannot be active, i.e. be selected. JDesktopPane and JInternalFrames do not obey
     * this requirement. Therefore we implement it our way.
     */
    public boolean isSelected() {
//        System.out.println("ESlateInternalFrame " + getTitle() + ",  isSelected(): " + active);
        return _active;
    }

    public void setSelected(boolean selected) throws PropertyVetoException {
        super.setSelected(selected);
    }

    public void setActive(boolean active) throws PropertyVetoException {
        if (_active == active) return;
//        System.out.println("ESlateInternalFrame " + getTitle() + ",  _active: " + _active + ", active: " + active);

        if (active) {
            if (_icon)
                return;
//                Thread.currentThread().dumpStack();
//            System.out.println("1 Lightweight? " + JComponent.isLightweightComponent((Component) eSlateHandle.getComponent()));
//            System.out.println("2 Lightweight? " + ((Component) eSlateHandle.getComponent()).isLightweight());

            /* Check if this is a BeanXporter component. If it is, then don't move it
             * to front, because this will cause re-initialization of the ActiveX control.
             * The BeanXporter i/f is a tagging interface with no method declarations.
             */
/*            boolean beanXporterObject = false;
            try{
                Class beanXporterBaseClass = Class.forName("gr.cti.eslate.BeanXporter");
                if (beanXporterBaseClass != null && beanXporterBaseClass.isAssignableFrom(eSlateHandle.getComponent().getClass()))
                    beanXporterObject = true;
            }catch (Throwable thr) {}
*/
            _active = true;
            if (eSlateHandle != null && !eSlateHandle.usesBeanXporterBeans())
                moveToFront();

            if (eslateComponent != null)
//                eslateComponent.setActive(true);
                eslateComponent.fireComponentActivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ACTIVATED, eSlateHandle.getComponentName()));

            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_ACTIVATED);
/*1            ESlateInternalFrame fr;
//0            for (int i=0; i<container.componentFrames.size(); i++) {
//0                fr = (ESlateInternalFrame) container.componentFrames.at(i);
            for (int i=0; i<container.mwdComponents.size(); i++) {
                if (!container.mwdComponents.components.get(i).visualBean)
                    continue;
                fr = container.mwdComponents.components.get(i).frame;
                if (fr != this)
                    fr.setSelected(false);
            }
1*/
//if            if (!this.equals(container.EMPTY_FRAME))
//if                container.EMPTY_FRAME._active = false;
            getGlassPane().setVisible(false);
        }else{
            _active = false;
            if (eslateComponent != null)
//                eslateComponent.setActive(false);
                eslateComponent.fireComponentDeactivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEACTIVATED, eSlateHandle.getComponentName()));
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_DEACTIVATED);
//            System.out.println("Setting ESlateInternalFrame's " + getTitle() + " glass pane visibility to: " + _activatedOnMousePress);
            if (_activatedOnMousePress)
                getGlassPane().setVisible(true);
            else
                getGlassPane().setVisible(false);

        }
        repaint();
    }

    /* Returns the size of the frame, when not iconified or maximized.
     * If iconified or maximized, it returns the size the frame did
     * have before it was iconified or maximized, respectively.
     */
    public Dimension getRealSize() {
        if (!isIcon() && !isMaximum())
            return getSize();
        return realSize;
    }

    /* Returns the location of the frame, when not iconified or maximized.
     * If iconified or maximized, it returns the location the frame did
     * have before it was iconified or maximized, respectively.
     */
    public Point getRealLocation() {
        if (!isIcon() && !isMaximum())
            return getLocation();
        return realLocation;
    }

    public void setComponentNameChangeableFromMenuBar(boolean enabled) {
        if (frozen) return;
        if (enabled) {
            if (!eSlateHandle.getMenuPanel().isRenamingAllowed()) {
/*                JLabel nameLabel = (JLabel) eSlateHandle.getMenuPanel().getNameLabel(); //getComponent(1);
                nameLabel.removeMouseListener(nameLabelMouseEventDispatcher);
                nameLabel.removeMouseMotionListener(nameLabelMouseEventDispatcher);
*/
                eSlateHandle.getMenuPanel().setRenamingAllowed(true);
                container.setMicroworldChanged(true);
//                System.out.println("17. microworld changed = true");
            }
        }else{
            if (eSlateHandle.getMenuPanel().isRenamingAllowed()) {
/*                JLabel nameLabel = (JLabel) eSlateHandle.getMenuPanel().getNameLabel(); //getComponent(1);
                nameLabel.addMouseListener(nameLabelMouseEventDispatcher);
                nameLabel.addMouseMotionListener(nameLabelMouseEventDispatcher);
*/
                eSlateHandle.getMenuPanel().setRenamingAllowed(false);
                container.setMicroworldChanged(true);
//                System.out.println("18. microworld changed = true");
            }
        }
    }

    public boolean isComponentNameChangeableFromMenuBar() {
        return eSlateHandle.getMenuPanel().isRenamingAllowed();
    }

    public void setIconifiable(boolean iconifiable) {
        setFrameIconifiable(iconifiable);
    }

    public void setFrameIconifiable(boolean minimizable) {
        if (frozen) return;
        if (minimizable) {
            if (!iconifiable) {
                intFrameControlPanel.add(eIconButton, 0);
                northPane.validate();
                iconifiable = true;
                container.setMicroworldChanged(true);
//                System.out.println("19. microworld changed = true");
            }
        }else{
            if (iconifiable) {
                eIconButton.getParent().remove(eIconButton);
                northPane.validate();
                iconifiable = false;
                container.setMicroworldChanged(true);
//                System.out.println("20. microworld changed = true");
            }
        }
    }

    /* The following two method adjust the '_activatedOnMousePress' attribute of
     * the ESlateInternalFrame. When this attribute is true, then frame is active
     * when the user clicks anywhere inside the component. When it is false, the
     * frame is activated only when the user clicks on its title panel. In the latter
     * case, the component responds well to all the mouse events, because it receives
     * then directly, without any intervention of the frame's Glasspane.
     */
    public void setComponentActivatedOnMousePress(boolean activate) {
        _activatedOnMousePress = activate;
        container.setMicroworldChanged(true);
    }

    public boolean isComponentActivatedOnMousePress() {
        return _activatedOnMousePress;
    }

    public boolean isIconifiable() {
        return iconifiable;
    }

    public void setMaximizable(boolean maximizable) {
        setFrameMaximizable(maximizable);
    }

    public void setFrameMaximizable(boolean maximizable) {
        if (frozen) return;
        if (maximizable) {
            if (!this.maximizable) {
                if (iconifiable)
                    intFrameControlPanel.add(eMaxButton, 1);
                else
                    intFrameControlPanel.add(eMaxButton, 0);

                northPane.validate();
                this.maximizable = true;
                container.setMicroworldChanged(true);
//                System.out.println("21. microworld changed = true");
            }
        }else{
            if (this.maximizable) {
                eMaxButton.getParent().remove(eMaxButton);
                northPane.validate();
                this.maximizable = false;
                container.setMicroworldChanged(true);
//                System.out.println("22. microworld changed = true");
            }
        }
    }

    public boolean isMaximizable() {
        return maximizable;
    }

    public void setClosable(boolean closable) {
        setFrameClosable(closable);
    }

    public void setFrameClosable(boolean closable) {
        if (frozen) return;

        if (closable) {
            if (!this.closable) {
                int pos = 0;
                if (iconifiable)
                    pos++;
                if (maximizable)
                    pos++;
                intFrameControlPanel.add(eCloseButton, pos);

                northPane.validate();
                this.closable = true;
                container.setMicroworldChanged(true);
//                System.out.println("23. microworld changed = true");
            }
        }else{
            if (this.closable) {
                eCloseButton.getParent().remove(eCloseButton);
                northPane.validate();
                this.closable = false;
                container.setMicroworldChanged(true);
//                System.out.println("24. microworld changed = true");
            }
        }
    }

    public boolean isClosable() {
        return closable;
    }

    /* Smth is very wrong in Java 1.3. The frames 'dispose()' is called when the frame
     * is closed. 'Dispose()' calls the frame's 'setClosed()', which then calls
     * 'super.setClosed()', which calls the 'internalFrameClosed()' of the
     * registered InternalFrameListeners. Then this 'setClosed()' continues execution,
     * finishes and the control returns back to 'dispose()' which fires a second
     * 'internalFrameClosed()' event. This causes NullPointerExceptions, which have
     * been face up.
     */
    public void setClosed(boolean b) throws PropertyVetoException {
        JLabel lb = eSlateHandle.getMenuPanel().getNameLabel();
        try{
            super.setClosed(b);
            if (b) {
//                System.out.println("Firing event for frame " + eSlateHandle.getComponentName());
                eslateComponent.fireComponentClosed(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_CLOSED, eSlateHandle.getComponentName()));
            }
        }catch (Throwable thr) {
            // This exception occurs when the frane is iconified, when it's being closed.
//            System.out.println("Swing exception while calling super.closed() in the setClosed() of the ESlateInternalFrame. Continuing... ");
            DetailedErrorDialog dialog = new DetailedErrorDialog(container.parentFrame);
            String message = container.containerBundle.getString("ContainerMsg33");
            if (eSlateHandle != null && eSlateHandle.getComponentName() != null)
                message = message + " \"" + eSlateHandle.getComponentName() + "\". ";
            else if (getTitle() != null)
                message = message + " \"" + getTitle() + "\". ";
            else
                message = message + ". ";
            message = message + '\n' + container.containerBundle.getString("ContainerMsg34");

            dialog.setMessage(message);
            dialog.setDetails("Exception while calling super.closed() in the setClosed() of the ESlateInternalFrame. Continuing... " + '\n');
            dialog.appendThrowableMessage(thr);
            dialog.createNewLine();
            dialog.appendThrowableStackTrace(thr);
            ESlateContainerUtils.showDialog(dialog, container, true);
        }
        if (eSlateHandle != null) {
//            compPanel = eSlateHandle.getMenuPanel();
//            System.out.println("compPanel: " + compPanel);
//            System.out.println("2. compPanel.getNameLabel(): " + ((ESlateHandle$MenuPanel) compPanel).getNameLabel());
//            System.out.println("Removing nameLabelMouseEventDispatcher for: " + eSlateHandle);
            compPanel.removeMouseListener(nameLabelMouseEventDispatcher);
            compPanel.removeMouseMotionListener(nameLabelMouseEventDispatcher);
            lb.removeMouseListener(nameLabelMouseEventDispatcher);
            lb.removeMouseMotionListener(nameLabelMouseEventDispatcher);
//            ((ESlateHandle.MenuPanel) compPanel).getGlue().removeMouseListener(nameLabelMouseEventDispatcher);
//            ((ESlateHandle.MenuPanel) compPanel).getGlue().removeMouseMotionListener(nameLabelMouseEventDispatcher);
            removeMyComponentListener(frameAdapter);
            compPanel = null;
            intFrameControlPanel = null;
            northPaneContentPanel.removeAll();
            northPaneContentPanel = null;
            northPane.removeAll();
            northPane = null;
            getContentPane().removeAll();
            removeAll();
        }
        eSlateHandle = null;
        container = null;
    }


    public void setPinButtonVisible(boolean visible) {
        if (frozen) return;
        if (visible) {
            if (!eSlateHandle.getMenuPanel().isPlugButtonVisible()) {
                eSlateHandle.getMenuPanel().setPlugButtonVisible(true);
                northPane.validate();
                container.setMicroworldChanged(true);
//                System.out.println("25. microworld changed = true");
            }
        }else{
            if (eSlateHandle.getMenuPanel().isPlugButtonVisible()) {
                eSlateHandle.getMenuPanel().setPlugButtonVisible(false);
                northPane.validate();
                container.setMicroworldChanged(true);
//                System.out.println("26. microworld changed = true");
            }
        }
    }

    public boolean isPinButtonVisible() {
        return eSlateHandle.getMenuPanel().isPlugButtonVisible();
    }

    public void setHelpButtonVisible(boolean visible) {
        if (frozen) return;
        if (visible) {
            if (!eSlateHandle.getMenuPanel().isHelpButtonVisible()) {
                eSlateHandle.getMenuPanel().setHelpButtonVisible(true);
                northPane.validate();
                container.setMicroworldChanged(true);
//                System.out.println("27. microworld changed = true");
            }
        }else{
            if (eSlateHandle.getMenuPanel().isHelpButtonVisible()) {
                eSlateHandle.getMenuPanel().setHelpButtonVisible(false);
                northPane.validate();
                container.setMicroworldChanged(true);
//                System.out.println("28. microworld changed = true");
            }
        }
    }

    public boolean isHelpButtonVisible() {
        return eSlateHandle.getMenuPanel().isHelpButtonVisible();
    }

    public void setInfoButtonVisible(boolean visible) {
        if (frozen) return;
        if (visible) {
            if (!eSlateHandle.getMenuPanel().isInfoButtonVisible()) {
                eSlateHandle.getMenuPanel().setInfoButtonVisible(true);
                northPane.validate();
                container.setMicroworldChanged(true);
//                System.out.println("29. microworld changed = true");
            }
        }else{
            if (eSlateHandle.getMenuPanel().isInfoButtonVisible()) {
                eSlateHandle.getMenuPanel().setInfoButtonVisible(false);
                northPane.validate();
                container.setMicroworldChanged(true);
//                System.out.println("30. microworld changed = true");
            }
        }
    }

    public boolean isInfoButtonVisible() {
        return eSlateHandle.getMenuPanel().isInfoButtonVisible();
    }

    public void setTitlePanelVisible(boolean visible) {
        if (frozen || visible == titlePanelVisible) {
            titlePanelVisible = visible; // In the frozen == true case
            return;
        }

        if (!visible) {
            hideTitlePanel();
        }else{
            showTitlePanel();
        }
        titlePanelVisible = visible;
        revalidate();
        container.setMicroworldChanged(true);
//        System.out.println("31. microworld changed = true");
    }

    public boolean isTitlePanelVisible() {
        return titlePanelVisible;
    }

    private void hideTitlePanel() {
        Dimension frameSize = getSize();
        Dimension dim = new Dimension(0, 0);
        northPane.setMaximumSize(dim);
        northPane.setMinimumSize(dim);
        northPane.setPreferredSize(dim);

        frameSize.height = frameSize.height - NORTHPANE_HEIGHT;
        setLocation(getLocation().x, getLocation().y + NORTHPANE_HEIGHT);
        if (isActive())
            setBorder(FRAME_SELECTED_BORDER); //new CompoundBorder(((CompoundBorder) getBorder()).getOutsideBorder(), orangeLineBorder));
        else
            setBorder(FRAME_NORMAL_BORDER); //new CompoundBorder(((CompoundBorder) getBorder()).getOutsideBorder(), grayLineBorder));
        setSize(frameSize);
    }

    private void showTitlePanel() {
        Dimension frameSize = getSize();
        Dimension dim = new Dimension(0, NORTHPANE_HEIGHT);
        northPane.setMaximumSize(dim);
        northPane.setMinimumSize(dim);
        northPane.setPreferredSize(dim);
        frameSize.height = frameSize.height + NORTHPANE_HEIGHT;
        setLocation(getLocation().x, getLocation().y - NORTHPANE_HEIGHT);
        if (isActive())
            setTitleColor(ACTIVATION_COLOR);
        else{
            setTitleColor(TITLE_BG_COLOR);
        }
//        setBorder(new CompoundBorder(frameNormalBorder.getOutsideBorder(), grayLineBorder));
        setBorder(FRAME_NORMAL_BORDER);
        setSize(frameSize);
    }

/*    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
//        System.out.println("paintBorder " + eSlateHandle.getComponentName());
    }
*/
    public void addMyComponentListener(OldESlateInternalFrameAdapter eifa) {
        addComponentListener(eifa);
        frameAdapter = eifa;
    }

    public void removeMyComponentListener(OldESlateInternalFrameAdapter eifa) {
        removeComponentListener(eifa);
        frameAdapter = null;
    }

    protected OldESlateInternalFrameAdapter getFrameAdapter() {
        return frameAdapter;
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        /* If the ESlateInternalFrame if frozen, then along with the Frame move the component, too.
         */
        if (frozen)
            ((Component) eSlateHandle.getComponent()).setLocation(x, y);
    }

    public void setVisible(boolean visible) {
        if (!frozen) {
/*            if (eSlateHandle != null && eSlateHandle.usesBeanXporterBeans()) {
                if (visible)
                    setSize(200, 200);
                else
                    setSize(0, 0);
            }else{
*/                super.setVisible(visible);
                if (eSlateHandle != null)
                    ((Component) eSlateHandle.getComponent()).setVisible(visible);
//            }
        }else{
            super.setVisible(false);
            if (eSlateHandle != null)
                ((Component) eSlateHandle.getComponent()).setVisible(visible);
        }
    }

    public void setSize(Dimension d) {
        super.setSize(d);
        if (frozen)
            ((Component) eSlateHandle.getComponent()).setSize(d);
    }

    /* This overriden 'setBorder()' keeps track whether the FRAME_NORMAL_BORDER or
     * the FRAME_SELECTED_BORDER is used.
     */
    public void setBorder(Border b) {
        if (_maximum) {
            super.setBorder(null);
            return;
        }
        super.setBorder(b);
        if (b == null)
            return;
//        System.out.println("b: " + b + ", b.equals(FRAME_NORMAL_BORDER): " + b.equals(FRAME_NORMAL_BORDER));
        if (b.equals(FRAME_NORMAL_BORDER))
            prevBorder = FRAME_NORMAL_BORDER;
        else if (b.equals(FRAME_SELECTED_BORDER))
            prevBorder = FRAME_SELECTED_BORDER;

    }

    /* Restores the last one of the 'FRAME_NORMAL_BORDER'/'FRAME_SELECTED_BORDER' used
     * on this ESlateInternalFrame.
     */
    public void restoreBorder() {
        if (getBorder() != prevBorder)
        setBorder(prevBorder);
    }

    public void setDesktopItemSize(Dimension size) {
        setSize(size);
    }

    public void setDesktopItemSize(int width, int height) {
        setSize(width, height);
    }

    public Dimension getDesktopItemSize() {
        return getSize();
    }

    public Dimension getDesktopItemSize(Dimension d) {
        return getSize(d);
    }

    public void setDesktopItemBounds(Rectangle bounds) {
        setBounds(bounds);
    }

    public void setDesktopItemBounds(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    public Rectangle getDesktopItemBounds() {
        return getBounds();
    }

    public Rectangle getDesktopItemBounds(Rectangle bounds) {
        return getBounds(bounds);
    }

    public void setDesktopItemLocation(Point location) {
        setLocation(location);
    }

    public void setDesktopItemLocation(int x, int y) {
        setLocation(x, y);
    }

    public Point getDesktopItemLocation() {
        return getLocation();
    }

    public Point getDesktopItemLocation(Point p) {
        return getLocation(p);
    }

    public Point getDesktopItemRestoreLocation() {
        return realLocation;
    }

    public Dimension getDesktopItemRestoreSize() {
        return realSize;
    }

    public void setDesktopItemResizable(boolean resizable) {
        setResizable(resizable);
    }

    public boolean isDesktopItemResizable() {
        return isResizable();
    }


    public boolean usesGlassPane() {
        return true;
    }

    /* Method inherited from DesktopItem i/f, which returns if this DesktopItem
     * has the ability to 'carry' the platform's menu bar.
     */
    public boolean displaysESlateMenuBar() {
        return true;
    }

/*    public void setActive(boolean active) throws PropertyVetoException {
        setSelected(active);
    }
*/
    public boolean isActive() {
        return _active;
    }
}

class DegradatedColorMatteBorder extends MatteBorder {
    int top = 0, bottom = 0, left = 0, right = 0;

    public DegradatedColorMatteBorder(int top, int left, int bottom, int right) {
        super(top, left, bottom, right, Color.lightGray);
//Thread.currentThread().dumpStack();
//System.out.println("Constructor OldESlateInternalFrame DegradatedColorMatteBorder()");
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    /**
     * Paints the border for the specified component with the specified
     * position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*        Color oldColor = g.getColor();
        int h = height;
        int w = width;
        g.translate(x, y);

//        System.out.println("x: " + x + ", y: " + y + ", width: " + width + ", height: " + height);
//        Object obj = ((ESlateInternalFrame) c).eSlateHandle.getComponent();
        Color color = ((OldESlateInternalFrame) c).getDesktopPane().getBackground(); //((Component) obj).getBackground();
        if (left != 0) {
          g.setColor(color);
          g.drawLine(2, 0, 2, h);
          g.drawLine(1, 0, 1, h);
          g.setColor(Color.orange); //color.brighter());
          g.drawLine(0, 0, 0, height);
        }
        if (top != 0) {
          g.setColor(color);
          g.drawLine(1, 1, w, 1);
          g.drawLine(1, 2, w, 2);
          g.setColor(Color.orange); //.brighter());
          g.drawLine(0, 0, w, 0);
        }
        if (bottom != 0) {
          g.setColor(color);
          g.drawLine(1, height-2, w, height-2);
          g.drawLine(1, height, w, height);
          g.setColor(Color.orange); //color.brighter());
          g.drawLine(0, height-1, w, height-1);
        }
        if (right != 0) {
          g.setColor(color);
          g.drawLine(width-2, 1, width-2, h-2);
          g.drawLine(width, 1, width, h-2);
          g.setColor(Color.orange); //color.brighter());
          g.drawLine(width-1, 0, width-1, h);
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
*/
    }
}

class OldESlateInternalFrameAdapter extends java.awt.event.ComponentAdapter {
    protected boolean disableMoveListener = false;
}

/* Special BevelBorder border for the title panel of the JInternalFrames. Its difference
 * from the BevelBorder is that the bottom value for its insets is 1 instead of 2.
 */
class InternalFrameTitleBorder extends BevelBorder {
    public final Color HIGHLIGHT_OUTER_INACTIVE = OldESlateInternalFrame.TITLE_BG_COLOR.brighter().brighter();
    public final Color HIGHLIGHT_INNER_INACTIVE = OldESlateInternalFrame.TITLE_BG_COLOR.brighter();
    public final Color SHADOW_INNER_INACTIVE = OldESlateInternalFrame.TITLE_BG_COLOR.darker();
    public final Color SHADOW_OUTER_INACTIVE = OldESlateInternalFrame.TITLE_BG_COLOR.darker().darker();
    public final Color HIGHLIGHT_OUTER_ACTIVE = OldESlateInternalFrame.ACTIVATION_COLOR.brighter().brighter();
    public final Color HIGHLIGHT_INNER_ACTIVE = OldESlateInternalFrame.ACTIVATION_COLOR.brighter();
    public final Color SHADOW_INNER_ACTIVE = OldESlateInternalFrame.ACTIVATION_COLOR.darker();
    public final Color SHADOW_OUTER_ACTIVE = OldESlateInternalFrame.ACTIVATION_COLOR.darker().darker();

    public static final Insets insets = new Insets(2,2,1,2);
    JInternalFrame frame = null;

    public InternalFrameTitleBorder(JInternalFrame fr) {
        super(BevelBorder.RAISED);
        frame = fr;
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        boolean selected = frame.isSelected();

        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor((selected)? HIGHLIGHT_OUTER_ACTIVE: HIGHLIGHT_OUTER_INACTIVE); //getHighlightOuterColor(c));
        g.drawLine(0, 0, 0, h-1);
        g.drawLine(1, 0, w-1, 0);

        g.setColor((selected)? HIGHLIGHT_INNER_ACTIVE: HIGHLIGHT_INNER_INACTIVE); //getHighlightInnerColor(c));
        g.drawLine(1, 1, 1, h-2);
        g.drawLine(2, 1, w-2, 1);

        g.setColor((selected)? SHADOW_OUTER_ACTIVE: SHADOW_OUTER_INACTIVE); //getShadowOuterColor(c));
        g.drawLine(1, h-1, w-1, h-1);
        g.drawLine(w-1, 1, w-1, h-2);

        g.setColor((selected)? SHADOW_INNER_ACTIVE: SHADOW_INNER_INACTIVE); //getShadowInnerColor(c));
//                g.drawLine(2, h-2, w-2, h-2);
        g.drawLine(w-2, 2, w-2, h-3);

        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}

