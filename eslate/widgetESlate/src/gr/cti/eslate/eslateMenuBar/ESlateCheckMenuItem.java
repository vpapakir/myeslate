package gr.cti.eslate.eslateMenuBar;

import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.IntBaseArray;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;


public class ESlateCheckMenuItem extends JCheckBoxMenuItem implements Externalizable {

    //public static final int ITEM_HEIGHT = 19;
    public static final int ICON_WIDTH = 16;
    final static int ICON_TEXT_GAP = 4; //This is an assumption which currently is valid, but may change.
    public final Insets ITEM_WITHOUT_ICON_MARGIN = new Insets(0, ICON_WIDTH + ICON_TEXT_GAP, 0, 0);

    Dimension prefSize = new Dimension();
    private static final int FORMAT_VERSION = 1;
    ESlateMenuBar menuBar;
    ESlateMenu parentMenu;
    static final long serialVersionUID = -3454641202602986261L;

    String actionName;

    /**

     * Constructs a new ESlateMenuItem
     */


    public ESlateCheckMenuItem() {
        super();
    }

    public ESlateCheckMenuItem(String s) {
        super(s);
    }

    public ESlateCheckMenuItem(String s, Icon i) {
        super(s, i);
    }

    public ESlateCheckMenuItem(Icon i) {
        super(i);
    }

    public ESlateCheckMenuItem(Action a) {
        super(a);
    }


    /**
     * Constructs a new ESlateMenuItem,belonging to a specific menuBar
     * @param menuBar The parent menuBar
     */

    public ESlateCheckMenuItem(final ESlateMenuBar menuBar) {
        super();
        this.menuBar = menuBar;
        //Localize
        //ESlateMenuBar bar = menuBar;
        //addActionListener();
    }

    /**
     * Constructs a new ESlateMenuItem, with specific text and belonging to a specific menuBar
     * @param menuBar The parent menuBar
     * @param text The items text
     */

    public ESlateCheckMenuItem(final String text, final ESlateMenuBar menuBar) {
        super(text);
        //Localize
        this.menuBar = menuBar;
//        addActionListener();
    }

    /**
     * Constructs a new ESlateMenuItem, with specific text and belonging to a specific menu
     * @param menu The parent menu
     * @param text The items text
     */

    public ESlateCheckMenuItem(ESlateMenu menu, final String text) {
        super(text);
        this.menuBar = menu.menuBar;
        this.parentMenu = menu;
        java.awt.FontMetrics fm = getFontMetrics(getFont());
        int width = fm.stringWidth(text) + 33 + ICON_WIDTH + ICON_TEXT_GAP;

        if (width > menu.getWidth())
            prefSize.width = getWidth();
        prefSize.width = this.getWidth();
        setMargin(ITEM_WITHOUT_ICON_MARGIN);
        //addActionListener();
    }

    protected void fireActionPerformed(ActionEvent e) {
        super.fireActionPerformed(e);
        if (menuBar != null)
            menuBar.pathHasChanged(ESlateCheckMenuItem.this);
//        else
//            System.out.println("FATAL ERROR : Can't find parent MenuBar");
    }

    /*private void addActionListener() {
        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (menuBar != null)
                        menuBar.pathHasChanged(ESlateCheckMenuItem.this);

                }
            }
        );
    }*/

    private boolean isCheckMenuItem() {
        return true;
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

        /*if (!ESlateFieldMap.class.isAssignableFrom(firstObj.getClass())) {
         // Old time readExtermal()
         oldTimeReadExternal(in, firstObj);
         return;
         } */

//        ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
        StorageStructure fieldMap = (StorageStructure) firstObj;

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

        boolean b = fieldMap.get("Enabled", isEnabled());
        setEnabled(b);


        setText(fieldMap.get("Text", getText()));
        if (fieldMap.containsKey("ActionName"))
            setActionName((String) fieldMap.get("ActionName", getActionName()));

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
     * Sets the item's text
     * @param text The text
     */

    public void setText(String text) {
        super.setText(text);
        setName(text);
    }

    /**
     * Writes to the ESlateFieldMap to store values and properties
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

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
        boolean b = isEnabled();
        fieldMap.put("Enabled", b);

        if (getAction()!= null){
            fieldMap.put("ActionName", getAction().getValue(AbstractAction.NAME));
        }else{
            fieldMap.put("ActionName", null);
        }

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

    protected StorageStructure recordState(){
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("IsMenu", isMenu());
        fieldMap.put("IsCheckMenuItem", isCheckMenuItem());
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());
        if (getAction() != null)
            fieldMap.put("ActionName", getAction().getValue(AbstractAction.NAME));
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

        return fieldMap;
    }

    protected void applyState(StorageStructure fieldMap) {
        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));
        if (fieldMap.containsKey("ActionName"))
            setActionName((String) fieldMap.get("ActionName", getActionName()));
        else
            setActionName(null);
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
     * Sets the parent menuBar
     */

    public void setMenuBar(ESlateMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    /**
     * Gets the parent menuBar
     */

    public ESlateMenuBar getMenuBar() {
        return this.menuBar;
    }

    /**
     * Sets the parent menu
     */

    public void setParentMenu(ESlateMenu parentMenu) {
        this.parentMenu = parentMenu;
    }

    /**
     * Gets the parent menu
     */

    public ESlateMenu getParentMenu() {
        return this.parentMenu;
    }

    //////////////////////////////CODE TO BE CHECKED\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /*
     public void validateItemWidth() {
     prefSize.width = parentMenu.getWidth();
     }
     */

    /**
     * Determines whether item should be visible or not
     */

    public void setItemVisible(boolean visible) {

        if (visible) {
            if (getHeight() == 0)
                setSize(getWidth(), prefSize.height);
        } else {
            if (getHeight() != 0)
                prefSize.height = getHeight();
            setSize(getWidth(), 0);
        }
    }

    public boolean isMenu() {
        return false;
    }

    public int[] getPath() {
        IntBaseArray path = new IntBaseArray();
        ESlateMenu menu = getParentMenu();
        JMenuItem mi = this;
        int index = menu.menuEntries.indexOf(mi);

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

    public void paint(Graphics g){
	    Rectangle paintViewR = new Rectangle();
	    Insets paintViewInsets = new Insets(0, 0, 0, 0);

	    paintViewInsets = getInsets(paintViewInsets);
	    paintViewR.x = paintViewInsets.left;
	    paintViewR.y = paintViewInsets.top;

	    paintViewR.width = getWidth() - (paintViewInsets.left + paintViewInsets.right);
            if (paintViewR.width >= 300){
                if (getAccelerator() != null)
                  paintViewR.width = 250 -30;
                else
                  paintViewR.width = 350 - 30;
            }

	    paintViewR.height = getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
            Font font = getFont();
            FontMetrics metrics = getFontMetrics(font);

            KeyStroke accelerator =  getAccelerator();
            String acceleratorText = "";

            if (accelerator != null) {
                int modifiers = accelerator.getModifiers();
                if (modifiers > 0) {
                    acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                    acceleratorText += UIManager.getString("MenuItem.acceleratorDelimiter");
                }
                int keyCode = accelerator.getKeyCode();
                if (keyCode != 0) {
                    acceleratorText += KeyEvent.getKeyText(keyCode);
                } else {
                    acceleratorText += accelerator.getKeyChar();
                }


                if (metrics.stringWidth(getText()+acceleratorText)>300){
                    String clippedText = clipString(getText(), 220- metrics.stringWidth(acceleratorText));
                    super.setText(clippedText+"...");
                }
            }else{
                if (metrics.stringWidth(getText())>300){
                    String clippedText = clipString(getText(), 300);
                    super.setText(clippedText+"...");
                }
            }
            super.paint(g);
            //super.setText(text);
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        int width = 0;
        width = getMargin().left + getMargin().right;
        KeyStroke accelerator =  getAccelerator();
        String acceleratorText = "";

        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText += UIManager.getString("MenuItem.acceleratorDelimiter");
            }
            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                acceleratorText += KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText += accelerator.getKeyChar();
            }
        }

        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);

        if (getText() != null)
            width += metrics.stringWidth(getText());
            width = width+30;
        if (getAccelerator() != null)
            width += 100;
        if (width >= 300)
            width = 300;
        return new Dimension(width,dim.height);
    }

    public Dimension getMinimumSize(){
        return new Dimension(0,19);
    }

    protected void setActionName(String s){
        actionName = s;
    }

    protected String getActionName(){
        return actionName;
    }

    String clipString(String s, int availableWidth){
        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);
        int width = 0;
        int index = s.length();
        for (int i = 0 ; i <s.length();i++){
            char c = s.charAt(i);
            width += metrics.charWidth(c);
            if (width>=availableWidth){
                index = i;
                return s.substring(0,index)+"...";
            }
        }
        return s;
    }


}

