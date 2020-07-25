/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 * @version
 */

package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.container.event.ESlateComponentEvent;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.BreakIterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class UILessComponentIcon extends JPanel implements DesktopItem, Externalizable {
    static final long serialVersionUID = 12;
    public static final String STR_FORMAT_VERSION = "1.0";
    static final int FORMAT_VERSION = 2;
    public static final int WIDTH = 80;
    public static final int PREFERRED_HEIGHT = 62;
    public static final int ICON_HEIGHT = 32;
    public static final int ICON_TEXT_GAP = 0;
    public static final int AREA_PREFERRED_HEIGHT = PREFERRED_HEIGHT-ICON_HEIGHT-ICON_TEXT_GAP;
    public static final Dimension INITIAL_SIZE = new Dimension(WIDTH, PREFERRED_HEIGHT);
    public static final Dimension GAP_SIZE = new Dimension(WIDTH, ICON_TEXT_GAP);
    static final int BORDER_SIDE = 1;
    public static final Border BASIC_AREA_BORDER = new EmptyBorder(2*BORDER_SIDE,2*BORDER_SIDE,2*BORDER_SIDE,2*BORDER_SIDE);
    public static final float LEFT_INDENT = 0f;
    public static final float RIGHT_INDENT = 0f;
    public static final float SPACE_ABOVE = 0f;
    public static final float SPACE_BELOW = 0f;
    public static final float LINE_SPACING = 0f;

    Color activeAreaBgr = new Color(0, 0, 128);
    Color activeAreaFgr = new Color(0, 0, 128);
    Color inactiveAreaBgr = new Color(0, 0, 128);
    Color inactiveAreaFgr = new Color(0, 0, 128);
    static Icon puzzleIcon = null;
    ESlateHandle handle;
    JTextPane nameArea;
    Icon componentIcon = null;
    JLabel componentIconLabel = null;
    boolean active = false, iconified = true, maximum = false;
    boolean iconifiable = true, resizable = false, closable = true;
    /* Specifies if the name of the component can be changed through its UI */
    boolean nameEditable = true;
    int layer = LayerInfo.DEFAULT_LAYER_Z_ORDER.intValue();
    Point realLocation = null;
    boolean editModeOn = false;
    FontMetrics nameAreaFontMetrics = null;
    boolean caretListenerShouldAdjustSize = true;
    ESlateComponent eslateComponent = null;
    boolean eventResent = false;
    Font nameAreaFont = null;

/*    ActionListener renameListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            UILessComponentIcon icon = (UILessComponentIcon) e.getSource();
        }
    };
*/
    MouseListener nameAreaMouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            if (eslateComponent.container.lc.isModalFrameVisible()) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                return;
            }
            final JTextPane area = (JTextPane) e.getSource();
            final MouseEvent mouseEvent = e;
            UILessComponentIcon icon = (UILessComponentIcon) area.getParent();
            if (!icon.active)
                icon.setActive(true);
            else{
                if (!icon.isEditModeOn()) {
                    icon.setEditModeOn(true);
                }
            }
            if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                icon.setEditModeOn(false);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (eslateComponent.container instanceof ESlateComposer)
                            ((ESlateComposer) eslateComponent.container).showInvisibleComponentPopup(area, mouseEvent.getX(), mouseEvent.getY());
//                        retargetMouseEvent(mouseEvent, area);
                    }
                });
            }
        }
    };
    MouseInputListener iconMouseListener = new MouseInputAdapter() {
        final Point INITIAL_POINT = new Point(-1, -1);
        Point prevPos = INITIAL_POINT;

        public void mousePressed(MouseEvent e) {
            if (eslateComponent.container.lc.isModalFrameVisible()) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                return;
            }

            final JLabel iconLabel = (JLabel) e.getSource();
            final UILessComponentIcon icon = (UILessComponentIcon) iconLabel.getParent();
            final MouseEvent mouseEvent = e;
            if (!icon.active)
                icon.setActive(true);
            else{
                if (icon.isEditModeOn()) {
                    icon.setEditModeOn(false);
                }
            }
            if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (eslateComponent.container instanceof ESlateComposer)
                            ((ESlateComposer) eslateComponent.container).showInvisibleComponentPopup(iconLabel, mouseEvent.getX(), mouseEvent.getY());
//                        retargetMouseEvent(mouseEvent, area);
                    }
                });
                return;
            }
            prevPos = e.getPoint();
//            if (!icon.activated)
        }
        public void mouseReleased(MouseEvent e) {
            if (!javax.swing.SwingUtilities.isRightMouseButton(e))
            prevPos = INITIAL_POINT;
        }
        public void mouseDragged(MouseEvent e) {
            if (javax.swing.SwingUtilities.isRightMouseButton(e))
                return;
            JLabel iconLabel = (JLabel) e.getSource();
            UILessComponentIcon icon = (UILessComponentIcon) iconLabel.getParent();
//            synchronized(this) {
            if (!icon.active) return;
            Container parent = icon.getParent();
            if (parent == null) return;
            Point newPos = e.getPoint();
            Point prevLocation = icon.getLocation();
            int xDiff = prevPos.x - newPos.x;
            int yDiff = prevPos.y - newPos.y;
            icon.setLocation(prevLocation.x - xDiff, prevLocation.y - yDiff);
//            }
        }
    };

public static long constructorTimer = 0;
    public UILessComponentIcon(ESlateHandle handle) {
long start = System.currentTimeMillis();
        this.handle = handle;
constructorTimer = constructorTimer + (System.currentTimeMillis()-start);
    }

    private void constructGUI() {
        adjustColors();
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setOpaque(false);
        setSize(INITIAL_SIZE);
//        setBorder(new LineBorder(Color.red));

        componentIcon = createComponentIcon();
        int iconWidth = componentIcon.getIconWidth();
        componentIconLabel = new JLabel(componentIcon);
        componentIconLabel.setOpaque(false);
        componentIconLabel.setVerticalAlignment(JLabel.BOTTOM);
        componentIconLabel.setHorizontalAlignment(JLabel.CENTER);
//        componentIconLabel.setBorder(new LineBorder(Color.yellow));
        componentIconLabel.setAlignmentX(CENTER_ALIGNMENT);
        Dimension dim = new Dimension(iconWidth, ICON_HEIGHT);
        componentIconLabel.setMaximumSize(dim);
        componentIconLabel.setPreferredSize(dim);
        componentIconLabel.setMinimumSize(dim);
        componentIconLabel.addMouseMotionListener(iconMouseListener);
        componentIconLabel.addMouseListener(iconMouseListener);
        add(componentIconLabel);

        JLabel gap = new JLabel();
        gap.setMaximumSize(GAP_SIZE);
        gap.setPreferredSize(GAP_SIZE);
        gap.setMinimumSize(GAP_SIZE);
        add(gap);

        DefaultStyledDocument dsd = new DefaultStyledDocument() { //new PlainDocument() {
            /* We override this method so that we can handle the ENTER key. This
             * key causes the 'nameArea' to leave edit mode, instead of inserting
             * a new line character in it.
             */
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
//                System.out.println("insertString str: " + str);
                if (str.charAt(0) == '\n') return;
                super.insertString(offset, str, a);
            }
        };
        nameArea = new JTextPane(dsd) {
            public void updateUI() {
                super.updateUI();
                if (UILessComponentIcon.this == null) return;
                if (active) {
                    setForeground(activeAreaFgr);
                    setBackground(activeAreaBgr);
                }else{
                    setForeground(inactiveAreaFgr);
                    setBackground(inactiveAreaBgr);
                }
//                setOpaque(active);
                setSelectionColor(activeAreaBgr);
                nameAreaFont = UIManager.getFont("TextPane.font");
                nameAreaFont = nameAreaFont.deriveFont(nameAreaFont.getStyle(), nameAreaFont.getSize()-1);
                javax.swing.text.MutableAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setFontFamily(attr, nameAreaFont.getFamily());
                StyleConstants.setFontSize(attr, nameAreaFont.getSize());
                ((DefaultStyledDocument) getDocument()).setParagraphAttributes(0, getText().length(), attr, false);
//                revalidate();
            }
        };
        nameArea.setText(handle.getComponentName());
        nameArea.setSelectionColor(activeAreaBgr);
        nameAreaFont = nameArea.getFont();
        nameAreaFont = nameAreaFont.deriveFont(nameAreaFont.getStyle(), nameAreaFont.getSize()-1);
        javax.swing.text.MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(attr, LINE_SPACING);
        StyleConstants.setSpaceAbove(attr, SPACE_ABOVE);
        StyleConstants.setSpaceBelow(attr, SPACE_BELOW);
        StyleConstants.setLeftIndent(attr, LEFT_INDENT);
        StyleConstants.setRightIndent(attr, RIGHT_INDENT);
        StyleConstants.setFontFamily(attr, nameAreaFont.getFamily());
        StyleConstants.setFontSize(attr, nameAreaFont.getSize());
        dsd.setParagraphAttributes(0, nameArea.getText().length(), attr, false);

//        nameArea.setOpaque(false);
//        font = font.deriveFont(font.getStyle(), font.getSize()-1);
//        nameArea.setFont(font);
//1        nameArea.setForeground(Color.white);
//1        nameArea.setBackground(activeAreaBgr);
        nameArea.addMouseListener(nameAreaMouseListener);
        nameArea.setEditable(false);
        nameArea.setBorder(BASIC_AREA_BORDER);
        nameArea.setAlignmentX(CENTER_ALIGNMENT);
        add(nameArea);//, BorderLayout.CENTER);

        nameArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                JTextPane area = (JTextPane) e.getSource();
                UILessComponentIcon icon = (UILessComponentIcon) area.getParent();
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    icon.acceptNewName();
                    icon.setEditModeOn(false);
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//                    System.out.println("IN ESCAPE KEY RELEASED");
                    int areaHeight = nameArea.getSize().height;
                    if (AREA_PREFERRED_HEIGHT < areaHeight) {
                        setSize(WIDTH, areaHeight + ICON_HEIGHT + ICON_TEXT_GAP);
//                        System.out.println("5. setSize(" + WIDTH + ", " + (areaHeight + ICON_HEIGHT + ICON_TEXT_GAP));
                        nameArea.setSize(WIDTH-4, areaHeight);
                        revalidate();
                    }else{
                        setSize(INITIAL_SIZE);
                    }
                    caretListenerShouldAdjustSize = true;
                }
            }
            public void keyPressed(KeyEvent e) {
                JTextPane area = (JTextPane) e.getSource();
                UILessComponentIcon icon = (UILessComponentIcon) area.getParent();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    caretListenerShouldAdjustSize = false;
                    icon.rejectNewName();
                    icon.setEditModeOn(false);
                }
            }
        });

        nameArea.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                if (caretListenerShouldAdjustSize)
                    adjustNameAreaSize();
            }
        });
        nameArea.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                JTextPane area = (JTextPane) e.getSource();
                UILessComponentIcon icon = (UILessComponentIcon) area.getParent();
                icon.acceptNewName();
                icon.setEditModeOn(false);
            }
        });
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UILessComponentIcon icon = (UILessComponentIcon) e.getSource();
                icon.setEditModeOn(true);
//                nameArea.requestFocus();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private boolean GUIConstructed() {
        return (nameArea != null);
    }

    public String getName() {
        return handle.getComponentName();
//        return nameArea.getText();
    }

    public void setName(String name) {
        if (!handle.getComponentName().equals(name))
            try{
                handle.setComponentName(name);
            }catch (gr.cti.eslate.base.RenamingForbiddenException exc) {return;}
            catch (gr.cti.eslate.base.NameUsedException exc) {return;}
        if (GUIConstructed()) {
            nameArea.setText(name);
            adjustNameAreaSize();
        }
    }

    public void setActive(boolean activate) {
//        System.out.println(this + ", active: " + active + ", activate: " + activate);
        if (editModeOn)
            setEditModeOn(false);
        if (active == activate) return;
        active = activate;
        if (GUIConstructed()) {
            if (active) {
                // Re-insert the panel into its AWT container, so that it will appear on top
                // of all other components.
                Point loc = getLocation();
                Container parent = getParent();
                if (parent != null) {
                  parent.remove(this);
                  parent.add(this, 0);
                }
                setLocation(loc);
                setVisible(true);
    //            nameArea.requestFocus();
//                nameArea.setOpaque(true);
//                nameArea.setBorder(ACTIVE_AREA_BORDER);
                nameArea.setForeground(activeAreaFgr);
                nameArea.setBackground(activeAreaBgr);
            }else{
//                nameArea.setOpaque(false);
                nameArea.setBorder(BASIC_AREA_BORDER);
                nameArea.setForeground(inactiveAreaFgr);
                nameArea.setBackground(inactiveAreaBgr);
            }
    //        eslateComponent.setActive(active);
    //        System.out.println("Setting " + getName() + " activation to " + active +", nameArea.isOpaque(): " + nameArea.isOpaque());
            nameArea.revalidate();
            nameArea.repaint();
        }

        if (eslateComponent != null) {
            if (active)
                eslateComponent.fireComponentActivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ACTIVATED, handle.getComponentName())); // nameArea.getText()));
            else
                eslateComponent.fireComponentDeactivated(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_DEACTIVATED, handle.getComponentName())); //nameArea.getText()));
        }
//        System.out.println("2. " + this + ", active: " + active + ", activate: " + activate);
    }

    public boolean isActive() {
        return active;
    }

    public void setEditModeOn(boolean on) {
        if (!nameEditable || !handle.getESlateMicroworld().isRenamingAllowed()) return;
        if (editModeOn == on) return;
        editModeOn = on;
        if (!GUIConstructed()) return;
        if (editModeOn) {
            nameArea.setBorder(new CompoundBorder(new LineBorder(inactiveAreaFgr), new EmptyBorder(BORDER_SIDE, BORDER_SIDE, BORDER_SIDE, BORDER_SIDE)));
            nameArea.setEditable(true);
            nameArea.getCaret().setVisible(true);
            nameArea.setBackground(UIManager.getColor("TextField.background"));
            nameArea.setForeground(UIManager.getColor("TextField.foreground"));
            nameArea.requestFocus();
            nameArea.selectAll();
        }else{
            nameArea.setBorder(BASIC_AREA_BORDER);
            nameArea.setEditable(false);
            nameArea.getCaret().setVisible(false);
            nameArea.setBackground(activeAreaBgr);
            nameArea.setForeground(activeAreaFgr);
            nameArea.select(0, 0);
        }
    }

    public boolean isEditModeOn() {
        return editModeOn;
    }

    void acceptNewName() {
        try{
            handle.setComponentName(nameArea.getText());
        }catch (Exception exc) {
            rejectNewName();
        }
    }
    void rejectNewName() {
        nameArea.setText(handle.getComponentName());
    }

    void adjustNameAreaSize() {
        if (!GUIConstructed()) return;
        if (nameAreaFontMetrics == null)
            nameAreaFontMetrics = nameArea.getFontMetrics(nameAreaFont);

        int lineMaxWidth = WIDTH - (int) LEFT_INDENT - (int) RIGHT_INDENT - (4*BORDER_SIDE);
        String stringToExamine = nameArea.getText();
        BreakIterator bi = BreakIterator.getWordInstance();
        bi.setText(stringToExamine);
        int start = bi.first();
        int strLength = 0;
        int tokenStart = 0;
        int currLineNum = 0;
        for (int end = bi.next();
            end != BreakIterator.DONE;
            start = end, end = bi.next()) {
            String token = stringToExamine.substring(start,end);
            int tokenLength = nameAreaFontMetrics.stringWidth(token);
            if (tokenStart != 0 && tokenStart + tokenLength > lineMaxWidth) {
                strLength = strLength + (lineMaxWidth-tokenStart);
            }

            strLength = strLength + tokenLength; // + (numOfTokenLines * ((int)LEFT_INDENT+(int)RIGHT_INDENT+(4*BORDER_SIDE)));
            tokenStart = strLength % lineMaxWidth;
            int numOfLines = strLength / lineMaxWidth;
            for (int i=currLineNum; i<numOfLines; i++) {
                strLength = strLength + (int)LEFT_INDENT+(int)RIGHT_INDENT+(4*BORDER_SIDE);
            }
            currLineNum = numOfLines;
//            System.out.println("strLength: " + strLength + ", tokenStart: " + tokenStart + ", token: " + token);
        }

        int numOfLines = strLength / lineMaxWidth;
        if (strLength % lineMaxWidth != 0)
            numOfLines++;
//System.out.println("strLength: " + strLength + ", numOfLines: " + numOfLines);
//System.out.println();
        int newAreaWidth = strLength + (int) LEFT_INDENT + (int) RIGHT_INDENT + (4*BORDER_SIDE);
        if (newAreaWidth > WIDTH)
            newAreaWidth = WIDTH;
        int newAreaHeight = (int)SPACE_ABOVE + ((numOfLines) * (int)LINE_SPACING) + (numOfLines * nameAreaFontMetrics.getHeight()) + (int) SPACE_BELOW + (4*BORDER_SIDE);
        Dimension newSize = new Dimension(newAreaWidth, newAreaHeight);
//System.out.println("newSize: " + newSize);
        nameArea.setMinimumSize(newSize);
        nameArea.setMaximumSize(newSize);
        nameArea.setPreferredSize(newSize);
        setSize(WIDTH, newSize.height + ICON_HEIGHT + ICON_TEXT_GAP);
        nameArea.validate();
        validate();
    }

    ImageIcon createComponentIcon() {
        Image img = BeanInfoFactory.get32x32BeanIcon(handle.getComponent().getClass());
        if (img == null) {
            if (puzzleIcon == null)
                puzzleIcon = new ImageIcon(getClass().getResource("images/puzzle.gif"));
            return (ImageIcon) puzzleIcon;
        }
        return new ImageIcon(img);
    }

    public boolean isIcon() {
        return iconified;
    }

    public void setIcon(boolean icon) {
        if (iconified == icon) return;
        iconified = icon;
        if (icon) {
            realLocation = getLocation();
            setVisible(false);
            getParent().remove(this);
        }else{
            if (!GUIConstructed()) {
                constructGUI();
                adjustNameAreaSize();
            }
            if (eslateComponent != null)
                eslateComponent.container.lc.add(this, LayerInfo.ICON_LAYER_Z_ORDER);
            if (realLocation != null)
                setLocation(realLocation);
            setVisible(true);
        }
        if (eslateComponent != null) {
            if (icon)
                eslateComponent.fireComponentIconified(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_ICONIFIED, nameArea.getText()));
            else
                eslateComponent.fireComponentRestored(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_RESTORED, nameArea.getText()));
        }
        adjustColors();
    }

    public void setIconifiable(boolean iconifiable) {
        this.iconifiable = iconifiable;
    }

    public boolean isIconifiable() {
        return iconifiable;
    }

    public void setDesktopItemSize(Dimension size) {}
    public Dimension getDesktopItemSize() {
        return getSize();
    }
    public void setDesktopItemSize(int width, int height) {
    }

    public Dimension getDesktopItemSize(Dimension d) {
        return getSize(d);
    }

    public void setDesktopItemResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isDesktopItemResizable() {
        return resizable;
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

    public Point getDesktopItemLocation() {
        return getLocation();
    }

    public Point getDesktopItemLocation(Point p) {
        return getLocation(p);
    }

    /* Returns the location the icon should be placed at, when restored.
     */
    public Point getDesktopItemRestoreLocation() {
        return realLocation;
    }

    /* Returns the size the icon should have, when restored.
     */
    public Dimension getDesktopItemRestoreSize() {
        return getDesktopItemSize();
    }

    public void setDesktopItemLocation(Point p) {
        setLocation(p);
    }

    public void setDesktopItemLocation(int x, int y) {
        setLocation(x, y);
    }

    public void setLayer(int layer) {
//System.out.println("eslateComponent.container: " + eslateComponent.container + ", eslateComponent: " + eslateComponent);
        if (eslateComponent != null) {
            /* This method is an action controlled by a microworld setting. When the setting forbits
             * the action, there is no way the action can be taked by anyone no matter if the microworld
             * is locked or not.
             */
            if (!eslateComponent.container.isMicroworldLoading()) {
                Microworld microworld = eslateComponent.container.microworld;
                if (microworld != null)
                    microworld.checkActionPriviledge(microworld.mwdLayerMgmtAllowed, "mwdLayerMgmtAllowed");
            }
        }
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }

    public boolean usesGlassPane() {
        return false;
    }

    public boolean displaysESlateMenuBar() {
        return false;
    }

    public void setClosed(boolean closed) {// throws PropertyVetoException {
//        if (eslateComponent != null) {
//            if (closed) {
//                System.out.println("Firing event for " + handle.getComponentName());
//                eslateComponent.fireComponentClosed(new ESlateComponentEvent(eslateComponent, ESlateComponentEvent.COMPONENT_CLOSED, nameArea.getText()));
//            }
//        }
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setMaximizable(boolean maximizable) {
    }

    public boolean isMaximizable() {
        return false;
    }

    public boolean isMaximum() {
        return false;
    }

    public void setMaximum(boolean maximum) {}

    public void setTitlePanelVisible(boolean visible) {}
    public boolean isTitlePanelVisible() {
        return false;
    }

    public void setNameEditable(boolean editable) {
        if (editable == nameEditable) return;
        nameEditable = editable;
    }

    public boolean isNameEditable() {
        return nameEditable;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        StorageStructure fm = recordState();
        out.writeObject(fm);
//        System.out.println("UILessComponentIcon writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fm));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        applyState(fieldMap);
//        System.out.println("readExternal() xLocation: " + xLocation + ", yLocation: " + yLocation);
//        System.out.println("readExternal() width: " + width + ", height: " + height);
    }

    StorageStructure recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 19);
        fieldMap.put("Component name", getName());
        fieldMap.put("Closable", closable);
        fieldMap.put("Iconifiable", iconifiable);
        fieldMap.put("Maximizable", isMaximizable());
        fieldMap.put("Name Editable", nameEditable);
        fieldMap.put("Resizable", resizable);
        fieldMap.put("Is Icon", isIcon());
        fieldMap.put("Is Maximum", isMaximum());
        Rectangle bounds = getBounds();
        fieldMap.put("Width", bounds.width);
        fieldMap.put("Height", bounds.height);
        fieldMap.put("X Location", bounds.x);
        fieldMap.put("Y Location", bounds.y);
        fieldMap.put("Real Location", realLocation);
        fieldMap.put("Active", isActive());
        fieldMap.put("Layer", layer);

        return fieldMap;
    }

public static long applyStateTimer = 0;
    void applyState(StorageStructure fieldMap) {
//        setName((String) fieldMap.get("Component name"));
long start = System.currentTimeMillis();
        setClosable(fieldMap.get("Closable", true));
        setIconifiable(fieldMap.get("Iconifiable", true));
        setMaximizable(fieldMap.get("Maximizable", true));
        setNameEditable(fieldMap.get("Name Editable", true));
        setDesktopItemResizable(fieldMap.get("Resizable", true));
        setIcon(fieldMap.get("Is Icon", true));
        setMaximum(fieldMap.get("Is Maximum", false));
        Rectangle bounds = new Rectangle();
        bounds.width = fieldMap.get("Width", 0);
        bounds.height = fieldMap.get("Height", 0);
        bounds.x = fieldMap.get("X Location", 0);
        bounds.y = fieldMap.get("Y Location", 0);
        setDesktopItemBounds(bounds);
        realLocation = (Point) fieldMap.get("Real Location");
        setLayer(fieldMap.get("Layer", 0));
        setActive(fieldMap.get("Active", false));
        setName(fieldMap.get("Component name", handle.getComponentName()));
applyStateTimer = applyStateTimer + (System.currentTimeMillis()-start);
    }

    synchronized void retargetMouseEvent(MouseEvent e, Component target) {
//        System.out.println("retargetMouseEvent eventResent: " + eventResent);
        if (eventResent) {
            eventResent = false;
            return;
        }
        eventResent = true;
        MouseEvent retargeted = new MouseEvent(target,
                                               e.getID(),
                                               e.getWhen(),
                                               e.getModifiers(),
                                               e.getX(),
                                               e.getY() ,
                                               e.getClickCount(),
                                               e.isPopupTrigger());
        target.dispatchEvent(retargeted);
        eventResent = false;
    }

    public String toString() {
        return "UILessComponentIcon " + handle.getComponentName();
    }

    private void adjustColors() {
        activeAreaBgr = UIManager.getColor("TextField.selectionBackground");
        activeAreaFgr = UIManager.getColor("TextField.selectionForeground");
        inactiveAreaBgr = UIManager.getColor("Desktop.background");
        if (Color.RGBtoHSB(inactiveAreaBgr.getRed(),inactiveAreaBgr.getGreen(),inactiveAreaBgr.getBlue(),null)[2]>0.5)
        	inactiveAreaFgr = UIManager.getColor("TextField.foreground");
        else
        	inactiveAreaFgr = UIManager.getColor("TextField.selectionForeground");
        if (nameArea!=null) {
	        if (active) {
	            nameArea.setForeground(activeAreaFgr);
	            nameArea.setBackground(activeAreaBgr);
	        }else{
	        	nameArea.setForeground(inactiveAreaFgr);
	        	nameArea.setBackground(inactiveAreaBgr);
	        }
	        nameArea.setSelectionColor(activeAreaBgr);
        }
    }

    public void updateUI() {
        super.updateUI();
        adjustColors();
    }
}

