package gr.cti.eslate.base.container.internalFrame;

import gr.cti.eslate.base.ComponentNameChangedEvent;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.container.BeanInfoFactory;
import gr.cti.eslate.base.container.ESlateComposer;
import gr.cti.eslate.base.container.ESlateContainer;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * This is the default title panel for all E-Slate internal frames.
 * @author  Giorgos Vasiliou
 * @version 1.0
 */

public class ESlateInternalFrameTitlePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3369819362884508286L;
	public static final int PANEL_HEIGHT = 18;
    /**
     * The color the title has when it is inactive.
     */
    public static Color INACTIVE_COLOR=new Color(49,170,156);
    /**
     * The color the title has when it is active.
     */
    public static Color ACTIVE_COLOR=new Color(206,154,0);
    /**
     * A reference to the frame that holds the title.
     */
    public ESlateInternalFrame frame;
    /**
     * Keeps the icon of the frame.
     */
    JLabel icon;
    /**
     * The maximize button.
     */
    JButton maximizeButton;
    /**
     * The iconify button.
     */
    JButton iconifyButton;
    /**
     * The close button.
     */
    JButton closeButton;
    /**
     * Groups the frame buttons together.
     */
    JPanel btns;
    /**
     * The icon for iconify/restore button which shows iconification.
     */
    private static Icon ICONIFY_ICON = UIManager.getIcon("InternalFrame.iconifyIcon"); //new ImageIcon(ESlateInternalFrameTitlePanel.class.getResource("images/minimizeIcon.gif"));;
    /**
     * The icon for maximize/restore button which shows maximization.
     */
    private static Icon MAXIMIZE_ICON = UIManager.getIcon("InternalFrame.maximizeIcon"); //new ImageIcon(ESlateInternalFrameTitlePanel.class.getResource("images/maximizeIcon.gif"));;
    /**
     * The icon for maximize/restore button which shows restoration.
     */
    private static Icon RESTORE_ICON = UIManager.getIcon("InternalFrame.minimizeIcon"); //new ImageIcon(ESlateInternalFrameTitlePanel.class.getResource("images/restoreIcon.gif"));;
    /**
     * The icon for close button.
     */
    private static Icon CLOSE_ICON = UIManager.getIcon("InternalFrame.closeIcon"); //new ImageIcon(ESlateInternalFrameTitlePanel.class.getResource("images/closeIcon.gif"));;
    /** This listener forwards the events received by the 'nameLabel' of the ESlate
     *  menu bar to the ESlateInternalFrameTitlePanel, so that the frame can be dragged
     *  for the 'nameLabel'.
     */
    NameLabelMouseListener nameLabelListener = new NameLabelMouseListener();
    /* The array of MouseListeners added to the ESlateInternalFrameTitlePanel. These
     * listeners are not directly added to the ESlateInternalFrameTitlePanel. Rather
     * their methods are called by the InternalFrameDragAroundListener. If these
     * listeners were added directly to ESlateInternalFrameTitlePanel, then they would
     * steal events from the InternalFrameDragAroundListener, which belongs to the
     * ESlateInternalFrame and is responsible for the frame dragging.
     */
    ArrayList mouseListeners = new ArrayList();
    MouseListener iconListener = null;
    private static JDesktopPane foo=new JDesktopPane();


    public ESlateInternalFrameTitlePanel(ESlateInternalFrame eif, boolean visible) {
        super();
        this.frame=eif;
        removeAll();
        setBackground(INACTIVE_COLOR);

        if (visible) //frame.isTitlePanelVisible())
            createUI();
    }

    JInternalFrame barProvider;
    BasicInternalFrameTitlePane biftpui;
    
    void createUI() {
    	// If the UI has already been created, don't recreate it
    	if (barProvider!=null)
    		return;
    	if (frame==null || frame.componentHandle==null)
    		return;
    	if (icon != null) return;
    	java.awt.Image img = null;
    	try {
    		img=BeanInfoFactory.get16x16BeanIcon(frame.componentHandle.getComponent().getClass());
    	} catch(Exception ex) {}
    	//First try to create a l&f compatible title bar by stealing one from an internal frame.
    	//If the l&f is not compatible with the process use the old title bar for a last resort.
    	barProvider=new JInternalFrame("",true,true,true,true) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3526441998601501868L;

			public String getTitle() {
				try {
					String sup=super.getTitle();
					String com=frame.componentHandle.getComponentName();
					//To adjust the size of the title label
					if (!com.equals(sup))
						barProvider.setTitle(com);
					Object o=frame.componentHandle.getComponent();
					if (o instanceof Component) {
						Component c=(Component) o;
						barProvider.setSize(c.getWidth(),c.getHeight());
					}
					return com;
				} catch(NullPointerException npe) {
				}
				return super.getTitle();
			}

			public boolean isSelected() {
				if (ESlateInternalFrameTitlePanel.this!=null)
					return ACTIVE_COLOR.equals(ESlateInternalFrameTitlePanel.this.getBackground());
				else
					return super.isSelected();
			}

//			public boolean isIcon() {
//				try {
//					return false;
//				} catch(NullPointerException npe) {
//					//In init
//					return super.isIcon();
//				}
//			}
//
//			public boolean isMaximum() {
//				try {
//					return maximum;
//				} catch(NullPointerException npe) {
//					//In init
//					return super.isMaximum();
//				}
//			}
    	};
    	if (barProvider.getUI() instanceof BasicInternalFrameUI) {
    		try {
	    		setOpaque(false);
	    		biftpui=(BasicInternalFrameTitlePane) ((BasicInternalFrameUI) barProvider.getUI()).getNorthPane();
	    		if (img != null)
	    			barProvider.setFrameIcon(new NewRestorableImageIcon(img));
	    		barProvider.setTitle(frame.componentHandle.getComponentName());
	    		boolean componentMenuAdded=false;
	    		//Identify components and add listeners
	    		Component[] c=biftpui.getComponents();
	    		for (int i=0;i<c.length;i++) {
	    			JButton b=null;
	    			Action a=null;
	    			if (UIManager.getString("InternalFrameTitlePane.closeButtonAccessibleName").equals
	    					(c[i].getAccessibleContext().getAccessibleName())) {
	    				b=(JButton) c[i];
	    		        a=frame.closeAction;
	    			} else if (UIManager.getString("InternalFrameTitlePane.maximizeButtonAccessibleName").equals
	    					(c[i].getAccessibleContext().getAccessibleName())) {
	    				b=(JButton) c[i];
	    				a=frame.maximizeAction;
		    		} else if (UIManager.getString("InternalFrameTitlePane.iconifyButtonAccessibleName").equals
		    				(c[i].getAccessibleContext().getAccessibleName())) {
		    			b=(JButton) c[i];
		    			a=frame.iconifyAction;
 		    		} else if (c[i].getAccessibleContext().getAccessibleName()==null && c[i] instanceof JLabel) {
 		    			JLabel iconLabel=(JLabel) c[i];
 		    			addComponentMenuFunctionality(iconLabel);
	    				componentMenuAdded=true;
 		    		}
	    			if (b!=null) {
	    				//Disable default functionality
	    				ActionListener[] al=(ActionListener[]) b.getListeners(ActionListener.class);
	    				if (al!=null)
	    					for (int j=0;j<al.length;j++)
	    						b.removeActionListener(al[j]);
	    				b.addActionListener(a);
	    			}
	    		}
	    		//Clean other listeners
	    		MouseMotionListener[] mml=(MouseMotionListener[]) barProvider.getListeners(MouseMotionListener.class);
	    		if (mml!=null)
	    			for (int i=0;i<mml.length;i++)
	    				barProvider.removeMouseMotionListener(mml[i]);
	    		mml=(MouseMotionListener[]) biftpui.getListeners(MouseMotionListener.class);
	    		if (mml!=null)
	    			for (int i=0;i<mml.length;i++)
	    				biftpui.removeMouseMotionListener(mml[i]);
	    		MouseListener[] ml=(MouseListener[]) barProvider.getListeners(MouseListener.class);
	    		if (ml!=null)
	    			for (int i=0;i<ml.length;i++)
	    				barProvider.removeMouseListener(ml[i]);
	    		ml=(MouseListener[]) biftpui.getListeners(MouseListener.class);
	    		if (ml!=null)
	    			for (int i=0;i<ml.length;i++)
	    				biftpui.removeMouseListener(ml[i]);
	    		Object o=frame.componentHandle.getComponent();
	    		//Repaint on name changes
	    		frame.componentHandle.addESlateListener(new ESlateAdapter() {
					public void componentNameChanged(ComponentNameChangedEvent arg0) {
						biftpui.repaint();
					}
	    		});
	    		if (!componentMenuAdded) {
	    			if (LABEL_BOUNDS==null) {
	    				computeLabelBounds();
	    				if (LABEL_BOUNDS==null)
	    					//LABEL_BOUNDS = new Rectangle(10,10,16,16);
	    					throw new Exception("Could not find the component icon on the bar for this skin");
	    			}
	    			ComponentMenuListener cml=new ComponentMenuListener(biftpui,LABEL_BOUNDS);
	    			biftpui.addMouseListener(cml);        
	    			biftpui.addMouseMotionListener(cml);        
	    		}
	    		//Remove the possible existing title bar
	    		if (frame.componentHandle.getComponent() instanceof Container)
	    			((Container) frame.componentHandle.getComponent()).remove(frame.componentHandle.getMenuPanel());
	    		disableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
	    		setTooltips();
	    		setActive(frame.isActive());
	    		setLayout(new BorderLayout());
	    		add(biftpui,BorderLayout.CENTER);
	    		foo.add(barProvider);
	    		return;
    		} catch(Exception ex) {
    			//Incompatible L&F, proceed to compatibility title bar
    			ex.printStackTrace();
    		}
    	}
    	//Incompatible L&F, proceed to compatibility title bar
    	barProvider=null;
		setOpaque(true);
//System.out.println("createUI() frame.componentHandle: " + frame.componentHandle);

        setLayout(new BorderLayout(0,0));
        setBorder(new ESlateInternalFrameTitlePanelBorder(frame));
        Dimension northPaneDim = new Dimension(0, PANEL_HEIGHT);
        setPreferredSize(northPaneDim);
        setMinimumSize(northPaneDim);
        setMaximumSize(northPaneDim);

        //Build the UI.
        icon=new JLabel(); //frame.getFrameIcon());
        if (img != null)
        	icon.setIcon(new NewRestorableImageIcon(img));
//            w.setFrameIcon(new NewRestorableImageIcon(img)); //.getBeanIcon(eSlateHandle.getComponent().getClass(), BeanInfo.ICON_COLOR_16x16);
//        add(icon,BorderLayout.WEST);

        Insets buttonMargin = new Insets(0, 0, 0, 0);
        //Make the internal frame buttons (maximize, iconify, close, restore)
        iconifyButton=new NoFocusButton(ICONIFY_ICON);
        iconifyButton.setMargin(buttonMargin);
        iconifyButton.addActionListener(frame.iconifyAction);
        iconifyButton.setToolTipText(ESlateInternalFrame.bundle.getString("Iconify"));
        setIcon(frame.isIcon);

        maximizeButton=new NoFocusButton(MAXIMIZE_ICON);
        maximizeButton.setMargin(buttonMargin);
        maximizeButton.addActionListener(frame.maximizeAction);
        maximizeButton.setToolTipText(ESlateInternalFrame.bundle.getString("Maximize"));
        setMaximum(frame.isMaximum);


        closeButton=new NoFocusButton(CLOSE_ICON);
        closeButton.setMargin(buttonMargin);
        closeButton.addActionListener(frame.closeAction);
        closeButton.setToolTipText(ESlateInternalFrame.bundle.getString("Close"));

        Dimension ctrlButtonDim = new Dimension(16, 14); //12, 11);
        iconifyButton.setPreferredSize(ctrlButtonDim);
        iconifyButton.setMaximumSize(ctrlButtonDim);
        iconifyButton.setMinimumSize(ctrlButtonDim);
        maximizeButton.setPreferredSize(ctrlButtonDim);
        maximizeButton.setMaximumSize(ctrlButtonDim);
        maximizeButton.setMinimumSize(ctrlButtonDim);
        closeButton.setPreferredSize(ctrlButtonDim);
        closeButton.setMaximumSize(ctrlButtonDim);
        closeButton.setMinimumSize(ctrlButtonDim);

        JPanel eslateMenuPanel=frame.componentHandle.getMenuPanel();
        //Make the menu panel completely opaque
        eslateMenuPanel.setOpaque(false);
        Component[] c=eslateMenuPanel.getComponents();
        for (int i=0;i<c.length;i++) {
            Component[] c2=((Container) c[i]).getComponents();
            for (int j=0;j<c2.length;j++)
                if (c2[j] instanceof JComponent)
                    ((JComponent) c2[j]).setOpaque(false);
            ((JComponent) c[i]).setOpaque(false);
        }
        eslateMenuPanel.setBorder(null);
        frame.componentHandle.getMenuPanel().getNameLabel().addMouseListener(nameLabelListener);
        frame.componentHandle.getMenuPanel().getNameLabel().addMouseMotionListener(nameLabelListener);
//        frame.componentHandle.getMenuPanel().addMouseListener(frame.dragAroundListener);
//        frame.componentHandle.getMenuPanel().addMouseMotionListener(frame.dragAroundListener);
        icon.addMouseListener(nameLabelListener);
        icon.addMouseMotionListener(nameLabelListener);

        frame.componentHandle.getMenuPanel().setRenamingAllowed(frame.isComponentNameChangeableFromMenuBar());

        JPanel IDPanel = new JPanel(true);
        IDPanel.setOpaque(false);
        IDPanel.setLayout(new BoxLayout(IDPanel, BoxLayout.X_AXIS));
//        if (beanImg != null) {
            IDPanel.add(icon, BorderLayout.WEST);
            IDPanel.add(Box.createHorizontalStrut(3));
//        }
        IDPanel.add(eslateMenuPanel);

        btns = new JPanel(true);
        btns.setOpaque(false);
        btns.setLayout(new BoxLayout(btns, BoxLayout.X_AXIS));

        btns.add(iconifyButton);
        btns.add(maximizeButton);
        btns.add(closeButton);

        add(IDPanel, BorderLayout.CENTER);
        add(btns, BorderLayout.EAST);

        iconListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() > 1 && !SwingUtilities.isRightMouseButton(e)) {
                    frame.closeAction.actionPerformed(new ActionEvent(ESlateInternalFrameTitlePanel.this, 0, ""));
                }
            }
        };
        icon.addMouseListener(iconListener);
//        validate();

        adjustMinimizeButton();
        adjustMaximizeButton();
        adjustCloseButton();

        revalidate();
        setActive(frame.isActive());
    }
    
    private void setTooltips() {
		Component[] c=biftpui.getComponents();
		for (int i=0;i<c.length;i++) {
			if (UIManager.getString("InternalFrameTitlePane.closeButtonAccessibleName").equals
					(c[i].getAccessibleContext().getAccessibleName())) {
		        ((JComponent) c[i]).setToolTipText(ESlateInternalFrame.bundle.getString("Close"));
			} else if (UIManager.getString("InternalFrameTitlePane.maximizeButtonAccessibleName").equals
					(c[i].getAccessibleContext().getAccessibleName())) {
				if (barProvider.isMaximum())
					((JComponent) c[i]).setToolTipText(ESlateInternalFrame.bundle.getString("Restore"));
				else
					((JComponent) c[i]).setToolTipText(ESlateInternalFrame.bundle.getString("Maximize"));
    		} else if (UIManager.getString("InternalFrameTitlePane.iconifyButtonAccessibleName").equals
    				(c[i].getAccessibleContext().getAccessibleName())) {
    			if (barProvider.isIcon())
    				((JComponent) c[i]).setToolTipText(ESlateInternalFrame.bundle.getString("Restore"));
    			else
    				((JComponent) c[i]).setToolTipText(ESlateInternalFrame.bundle.getString("Iconify"));
			}
		}
    }
    
    private void addComponentMenuFunctionality(JLabel iconLabel) {
		//Disable default functionality
		MouseListener[] ml=(MouseListener[]) iconLabel.getListeners(MouseListener.class);
		if (ml!=null)
			for (int j=0;j<ml.length;j++)
				iconLabel.removeMouseListener(ml[j]);
		iconLabel.addMouseListener(new ComponentMenuListener(iconLabel,null));        
		iconLabel.setToolTipText(ESlateInternalFrame.bundle.getString("ShowComponentMenu"));
    }
    
    private void computeLabelBounds() {
    	BufferedImage testImg=new BufferedImage(16,16,BufferedImage.TYPE_INT_RGB);
    	Graphics2D tig2=testImg.createGraphics();
    	tig2.setColor(Color.GREEN);
    	tig2.fillRect(0,0,16,16);
    	tig2.dispose();
    	JInternalFrame tester=new JInternalFrame("tester",true,true,true,true);
    	tester.setFrameIcon(new ImageIcon(testImg));
   		BasicInternalFrameTitlePane title=(BasicInternalFrameTitlePane) ((BasicInternalFrameUI) tester.getUI()).getNorthPane();
   		Dimension d=title.getPreferredSize();
   		BufferedImage img=new BufferedImage(d.width,d.height,BufferedImage.TYPE_INT_ARGB);
   		Graphics2D g2=img.createGraphics();
   		title.setSize(d);
   		title.paint(g2);
   		int line=img.getWidth();
   		int[] data=(int[]) img.getRaster().getDataElements(0,0,img.getWidth(),img.getHeight(),null);
   		for (int i=0;i<img.getHeight();i++) {
   			for (int j=0;j<line;j++) {
   				boolean itis=false;
   				if (data[i*line+j]==0xFF00FF00) {
   					itis=startLooking(data,i*line+j,0xFF00FF00,line);
   					if (itis) {
   						LABEL_BOUNDS=new Rectangle(j,i,16,16);
   						break;
   					}
   				}
   				if (itis)
   					break;
   			}
   		}
   		g2.dispose();
    }
    
    private boolean startLooking(int[] data,int index,int value,int line) {
    	try {
    		for (int i=0;i<16;i++)
    			for (int j=0;j<16;j++)
    				if (data[index+i*line+j]!=value)
    					return false;
    	} catch(ArrayIndexOutOfBoundsException ex) {
    		return false;
    	}
    	return true;
    }

    public boolean isEmptySpace(Component comp) {
        if (comp == null) return false;
        if (comp instanceof JPanel || comp instanceof JLabel || comp instanceof BasicInternalFrameTitlePane)
            return true;
        return false;
    }


    /**
     * Sets the icon that is shown on the left end of the title panel.
     */
/*    public void setFrameIcon(Icon ic) {
        if (icon == null) return;
        icon.setIcon(ic);
        revalidate();
    }
*/
    /**
     * Activates or deactivates the title.
     */
    public void setActive(boolean b) {
        setBackground(b?ACTIVE_COLOR:INACTIVE_COLOR);
    }

    /**
     * Sets the maximize button icon according to the frame state.
     */
    void setMaximum(boolean b) {
    	if (barProvider!=null) {
    		try {
				barProvider.setMaximum(b);
				setTooltips();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
    	}
        if (maximizeButton == null) return;
        if (b)
            maximizeButton.setIcon(RESTORE_ICON);
        else
            maximizeButton.setIcon(MAXIMIZE_ICON);
    }
    /**
     * Sets the iconify button icon according to the frame state.
     */
    void setIcon(boolean b) {
    	if (barProvider!=null)
    		try {
    			barProvider.setIcon(b);
    			setTooltips();
    		} catch (PropertyVetoException e) {
    			e.printStackTrace();
    		}
        if (iconifyButton == null) return;
        if (b)
            iconifyButton.setIcon(RESTORE_ICON);
        else
            iconifyButton.setIcon(ICONIFY_ICON);
    }
    /** Returns the title of the frame, which is the title of the
     *  ESlateInternalFrameTitlePanel.
     */
    String getTitle() {
        if (frame.componentHandle != null) {
            return frame.componentHandle.getComponentName();
        }
        return null;
    }

    public void addMouseListener(MouseListener l) {
        if (frame.dragAroundListener == l || nameLabelListener == l)
            super.addMouseListener(l);
        else{
            if (!mouseListeners.contains(l))
                mouseListeners.add(l);
        }
    }

    public void removeMouseListener(MouseListener l) {
        if (frame.dragAroundListener == l || nameLabelListener == l)
            super.removeMouseListener(l);
        else
            mouseListeners.remove(l);
    }

    public ESlateInternalFrame getFrame() {
        return frame;
    }

    public void updateUI() {
    	LABEL_BOUNDS=null;
        ICONIFY_ICON = UIManager.getIcon("InternalFrame.iconifyIcon");
        MAXIMIZE_ICON = UIManager.getIcon("InternalFrame.maximizeIcon");
        RESTORE_ICON = UIManager.getIcon("InternalFrame.minimizeIcon");
        CLOSE_ICON = UIManager.getIcon("InternalFrame.closeIcon");
        if (iconifyButton != null)
            iconifyButton.setIcon(ICONIFY_ICON);
        if (maximizeButton != null && frame != null) {
            if (!frame.isMaximum)
                maximizeButton.setIcon(MAXIMIZE_ICON);
            else
                maximizeButton.setIcon(RESTORE_ICON);
        }
        if (closeButton != null)
            closeButton.setIcon(CLOSE_ICON);
        super.updateUI();
        if (barProvider!=null) {
        	barProvider=null;
        	removeAll();
        	createUI();
        }
    }

    void adjustMinimizeButton() {
    	if (barProvider!=null) 
    		barProvider.setIconifiable(frame.isMinimizeButtonVisible());
    	else if (iconifyButton != null) {
	        iconifyButton.setEnabled(frame.isIconifiable());
	        if (frame.isMinimizeButtonVisible() && iconifyButton.getParent() == null)
	            btns.add(iconifyButton, 0);
	        else if (!frame.isMinimizeButtonVisible() && iconifyButton.getParent() != null)
	            btns.remove(iconifyButton);
	        revalidate();
	        doLayout();
        } 
        repaint();
    }

    void adjustMaximizeButton() {
    	if (barProvider!=null)
    		barProvider.setMaximizable(frame.isMaximizeButtonVisible());
    	else if (maximizeButton != null) {
	        maximizeButton.setEnabled(frame.isMaximizable());
	        if (frame.isMaximizeButtonVisible() && maximizeButton.getParent() == null) {
	            if (iconifyButton.getParent() == null)
	                btns.add(maximizeButton, 0);
	            else
	                btns.add(maximizeButton, 1);
	        }else if (!frame.isMaximizeButtonVisible() && maximizeButton.getParent() != null)
	            btns.remove(maximizeButton);
	        revalidate();
	        doLayout();
    	}
        repaint();
    }

    void adjustCloseButton() {
    	if (barProvider!=null)
    		barProvider.setClosable(frame.isCloseButtonVisible());
    	else if (closeButton != null) {
	        closeButton.setEnabled(frame.isClosable());
	        if (frame.isCloseButtonVisible() && closeButton.getParent() == null) {
	            int pos = 0;
	            if (iconifyButton.getParent() != null) pos++;
	            if (maximizeButton.getParent() != null) pos++;
	            btns.add(closeButton, pos);
	        }else if (!frame.isCloseButtonVisible() && closeButton.getParent() != null)
	            btns.remove(closeButton);
	        revalidate();
	        doLayout();
    	}
        repaint();
    }

    /**
     * Checks which buttons are visible and shows them.
     */
/*    public void revalidateButtons() {
        //First check the e-slate buttons
        frame.host.getESlateHandle().getMenuPanel().setHelpButtonVisible(frame.isHelpButtonVisible());
        frame.host.getESlateHandle().getMenuPanel().setInfoButtonVisible(frame.isInfoButtonVisible());
        frame.host.getESlateHandle().getMenuPanel().setPinButtonVisible(frame.isPlugButtonVisible());
        //Then the standard buttons
        btns.removeAll();
        int width=0;
        int btnCount = 0;
        if (frame.isIconifiable() && frame.isMinMaxButtonVisible()) {
            btns.add(iconifyButton);
            btnCount++;
            width+=iconifyButton.getPreferredSize().width;
        }
        if (frame.isMaximizable() && frame.isMinMaxButtonVisible()) {
            btns.add(maximizeButton);
            btnCount++;
            width+=maximizeButton.getPreferredSize().width;
        }
        if (frame.isClosable() && frame.isCloseButtonVisible()) {
            btns.add(Box.createHorizontalStrut(2));
            btns.add(closeButton);
            btnCount++;
            width+=2+closeButton.getPreferredSize().width;
        }

        Dimension btnsSize = new Dimension(2+btnCount*prefButtonSize.width, prefButtonSize.height);
System.out.println("btns preffered size: " + btnsSize);
        btns.setPreferredSize(btnsSize);
        btns.setMaximumSize(btnsSize);
        btns.setMinimumSize(btnsSize);
//        btns.setPreferredSize(new Dimension(2+width,2+iconifyButton.getPreferredSize().height));
        btns.revalidate();
    }
*/
    /**
     * A NoFocusButton is usually used in frame action buttons (maximize etc).
     */
    private class NoFocusButton extends JButton {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1072710458118513589L;
		public NoFocusButton(Icon ic) {
            super(ic);
            setFocusPainted(false);
            setUI(new BasicButtonUI());
            setBorderPainted(false);
            setMargin(new Insets(0,0,0,0));
        }
        public boolean isFocusTraversable() { return false; }
	public void requestFocus() {};
        public boolean isOpaque() { return true; }
    };

    /**
     * The border.
     */
    class ESlateInternalFrameTitlePanelBorder extends BevelBorder {
        /**
		 * 
		 */
		private static final long serialVersionUID = -546662504251449904L;
		public final Color HIGHLIGHT_OUTER_INACTIVE = INACTIVE_COLOR.brighter().brighter();
        public final Color HIGHLIGHT_INNER_INACTIVE = INACTIVE_COLOR.brighter();
        public final Color SHADOW_INNER_INACTIVE = INACTIVE_COLOR.darker();
        public final Color SHADOW_OUTER_INACTIVE = INACTIVE_COLOR.darker().darker();
        public final Color HIGHLIGHT_OUTER_ACTIVE = ACTIVE_COLOR.brighter().brighter();
        public final Color HIGHLIGHT_INNER_ACTIVE = ACTIVE_COLOR.brighter();
        public final Color SHADOW_INNER_ACTIVE = ACTIVE_COLOR.darker();
        public final Color SHADOW_OUTER_ACTIVE = ACTIVE_COLOR.darker().darker();

        public final Insets insets = new Insets(2,2,1,2);
        ESlateInternalFrame frame = null;

        public ESlateInternalFrameTitlePanelBorder(ESlateInternalFrame fr) {
            super(BevelBorder.RAISED);
            frame = fr;
        }

        public Insets getBorderInsets(Component c) {
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            boolean highlight = frame.isActive() && frame.isActiveStateDisplayed();

            Color oldColor = g.getColor();
            int h = height;
            int w = width;

            g.translate(x, y);

            g.setColor((highlight)? HIGHLIGHT_OUTER_ACTIVE: HIGHLIGHT_OUTER_INACTIVE); //getHighlightOuterColor(c));
            g.drawLine(0, 0, 0, h-1);
            g.drawLine(1, 0, w-1, 0);

            g.setColor((highlight)? HIGHLIGHT_INNER_ACTIVE: HIGHLIGHT_INNER_INACTIVE); //getHighlightInnerColor(c));
            g.drawLine(1, 1, 1, h-2);
            g.drawLine(2, 1, w-2, 1);

            g.setColor((highlight)? SHADOW_OUTER_ACTIVE: SHADOW_OUTER_INACTIVE); //getShadowOuterColor(c));
            g.drawLine(1, h-1, w-1, h-1);
            g.drawLine(w-1, 1, w-1, h-2);

            g.setColor((highlight)? SHADOW_INNER_ACTIVE: SHADOW_INNER_INACTIVE); //getShadowInnerColor(c));
    //                g.drawLine(2, h-2, w-2, h-2);
            g.drawLine(w-2, 2, w-2, h-3);

            g.translate(-x, -y);
            g.setColor(oldColor);
        }
    }

    protected class NameLabelMouseListener implements javax.swing.event.MouseInputListener {
        public void mousePressed(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }

        public void mouseReleased(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }
        public void mouseClicked(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }
        public void mouseEntered(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }
        public void mouseExited(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }
        public void mouseDragged(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }
        public void mouseMoved(MouseEvent e) {
            retargetNameLabelMouseEvent(ESlateInternalFrameTitlePanel.this, e);
        }
        public void retargetNameLabelMouseEvent(Component mouseEventTarget, MouseEvent e) {
            if (!ESlateInternalFrameTitlePanel.this.frame.isSelected()) return;

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

    void dispose() {
        if (closeButton != null) {
            closeButton.removeActionListener(frame.closeAction);
            iconifyButton.removeActionListener(frame.iconifyAction);
            maximizeButton.removeActionListener(frame.maximizeAction);
            frame.componentHandle.getMenuPanel().getNameLabel().removeMouseListener(nameLabelListener);
            frame.componentHandle.getMenuPanel().getNameLabel().removeMouseMotionListener(nameLabelListener);
//            frame.componentHandle.getMenuPanel().removeMouseListener(frame.dragAroundListener);
//            frame.componentHandle.getMenuPanel().removeMouseMotionListener(frame.dragAroundListener);
            icon.removeMouseListener(nameLabelListener);
            icon.removeMouseMotionListener(nameLabelListener);
        }
        mouseListeners.clear();
        setBorder(null);
        frame = null;
    }
    
    private class ComponentMenu extends JPopupMenu {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 4432479113714685792L;

		ComponentMenu(JComponent invoker) {
    		JMenu plugs=frame.componentHandle.getPlugMenu();
    		plugs.setText(ESlateContainer.containerBundle.getString("ComponentPins"));
    		plugs.setIcon(new ImageIcon(ESlatePart.class.getResource("plug1.gif")));
    		if (frame.componentHandle.getMenuPanel().isPlugButtonVisible())
    			add(plugs);
    		JMenuItem help=new JMenuItem(ESlateContainer.containerBundle.getString("ComponentHelp"),new ImageIcon(ESlatePart.class.getResource("help1.gif")));
    		help.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    		        frame.container.showActiveComponentHelp();
    			}
    		});
    		if (frame.componentHandle.getMenuPanel().isHelpButtonVisible())
    			add(help);
    		JMenuItem info=new JMenuItem(ESlateContainer.containerBundle.getString("ComponentAbout"),new ImageIcon(ESlatePart.class.getResource("info1.gif")));
    		info.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				frame.container.showActiveComponentInfo();
    			}
    		});
    		if (frame.componentHandle.getMenuPanel().isInfoButtonVisible())
    			add(info);
    		setLightWeightPopupEnabled(false);
    		setInvoker(invoker);
    	}
    }
    
    private class ComponentMenuListener extends MouseInputAdapter {
    	ComponentMenuListener(JComponent ref,Rectangle rect) {
    		this.ref=ref;
    		this.rect=rect;
    	}
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e))
				return;
			if (rect!=null && !rect.contains(e.getX(),e.getY())) {
				redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
				return;
			}
			ComponentMenu cm=new ComponentMenu(ref);
			Point p;
			if (rect==null)
				p=new Point(0,ref.getHeight());
			else
				p=new Point((int) rect.x,(int) rect.getMaxY());
			SwingUtilities.convertPointToScreen(p,ref);
			cm.setLocation(p.x,p.y);
			cm.setVisible(cm.getComponentCount()>0);
		}
		public void mouseClicked(MouseEvent e) {
			redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
		}
		public void mouseDragged(MouseEvent e) {
			redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
		}
		public void mouseEntered(MouseEvent e) {
			redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
		}
		public void mouseExited(MouseEvent e) {
			biftpui.setToolTipText(null);
			redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
		}
		public void mouseMoved(MouseEvent e) {
			if (LABEL_BOUNDS!=null && LABEL_BOUNDS.contains(e.getX(),e.getY()))
				biftpui.setToolTipText(ESlateInternalFrame.bundle.getString("ShowComponentMenu"));
			else
				biftpui.setToolTipText(null);
			redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
		}
		public void mouseReleased(MouseEvent e) {
			redispatchMouseEvent(ESlateInternalFrameTitlePanel.this,e);
		}
		private void redispatchMouseEvent(Component mouseEventTarget, MouseEvent e) {
            if (!ESlateInternalFrameTitlePanel.this.frame.isSelected()) return;

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
		private JComponent ref;
		private Rectangle rect;
    }
    
    /**
     * This rectangle is calculated for look and feels that do not have a component
     * to display the frame icon. It is calculated heuristically.
     */
    private static Rectangle LABEL_BOUNDS;
}