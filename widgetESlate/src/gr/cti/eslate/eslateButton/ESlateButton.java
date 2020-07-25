package gr.cti.eslate.eslateButton;


import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import gr.cti.eslate.utils.*;

import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;
import java.io.*;
import java.beans.PropertyChangeEvent;

import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;


public class ESlateButton extends JButton implements ESlatePart, Externalizable/*,UIResource */ {

    private StringSO stringSO;
    private ColorSO colorSO;
    private BooleanSO pressedIndicator;
    private ESlateHandle handle = null;
    private static final int FORMAT_VERSION = 1;
    boolean componentClosed;
    private boolean plugsCreated = false;
    static final long serialVersionUID = -3547164432222503547L;
    SharedObjectPlug plug3, plug2, plug;

    private static ResourceBundle bundleMessages;
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
     * Timer which measures the time required for the construction of the
     * component.
     */
    PerformanceTimer constructorTimer;

    /**
     * Timer which measures the time required for initializing the E-Slate
     * aspect of the component.
     */
    PerformanceTimer initESlateAspectTimer;

    /**
     * The listener that notifies about changes to the state of the
     * Performance Manager.
     */
    PerformanceListener perfListener = null;

    /**
     * Returns Copyright information.
     * @return	The Copyright information.
     */
    private ESlateInfo getInfo() {
        String[] info = {
                bundleMessages.getString("part"),
                bundleMessages.getString("development"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }

    /**
     * Constructs a new ESlateButton
     *
     */
    public ESlateButton() {
        this(null,null);
    }

    public ESlateButton(Action a){
        this(null,null);
        setAction(a);
    }

    public ESlateButton(String s){
        this(s,null);
    }

    public ESlateButton(Icon i){
        this(null,i);
    }

    public ESlateButton(String s, Icon i){
        super(s,i);
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateButton.BundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setOpaque(true);
        setPreferredSize(new Dimension(100, 60));
        setRequestFocusEnabled(false);

        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
    }

    /**
     * Returns the handle.
     * @return handle The eslate handle
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();

            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);
            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateButton"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ButtonPrimitives");
            handle.setInfo(getInfo());
            attachTimers();

            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {

                        stringSO = null;
                        colorSO = null;
                        pressedIndicator = null;
                        PerformanceManager perfman = PerformanceManager.getPerformanceManager();

                        perfman.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            setPlugsUsed(true);

            addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (isEnabled() == true && componentClosed == false && pressedIndicator != null) {
                            pressedIndicator.setBoolean(true);
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        if (isEnabled() == true && componentClosed == false && pressedIndicator != null) {
                            pressedIndicator.setBoolean(false);
                        }
                    }
                }
            );
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
    }

    /**
     * Sets the background color.
     * @param c The background color
     */
    public void setBackground(Color c) {
        if (isEnabled() == true) {
            super.setBackground(c);
            if (colorSO != null)
                colorSO.setColor(c);
        }
        repaint();
    }

    /**
     * Sets the button to be selected or unselected.
     */
    public void setSelected(boolean b) {
        if (isEnabled() == true && componentClosed == false) {
            super.setSelected(b);
            if (pressedIndicator != null)
                pressedIndicator.setBoolean(b);
        }
    }

    /**
     * Sets the button's text.
     * @param s The text
     */
    public void setText(String s) {
        if (isEnabled() == true) {
            super.setText(s);
            if (stringSO != null) {
                stringSO.setString(s);
            }
        }
    }

    /**
     * Produces a "button click".
     */
    public void doClick() {
        if (isEnabled() == true && componentClosed == false) {
            super.doClick();
            if (pressedIndicator != null ) {
                pressedIndicator.setBoolean(true);
                pressedIndicator.setBoolean(false);
            }
        }
    }

    /**
     * Turns an Icon into a NewRestorableImageIcon (in case its not)
     * @param icon The icon to be transformed to NewRestorableImageIcon
     */
    public NewRestorableImageIcon toRestorableImageIcon(Icon icon) {
        
        if (icon == null){
            return null;
        }
        if (icon instanceof NewRestorableImageIcon)
            return new NewRestorableImageIcon(((NewRestorableImageIcon) icon) .getImage());
        if (icon instanceof ImageIcon){
            return new NewRestorableImageIcon(((ImageIcon) icon).getImage());
        }
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
        icon.paintIcon(this, g, 0, 0);
        g.dispose();
        return new NewRestorableImageIcon(b);
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
        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setBorderPainted(fieldMap.get("BorderPainted", isBorderPainted()));
        setContentAreaFilled(fieldMap.get("ContentAreaFilled", isContentAreaFilled()));
        setDefaultCapable(fieldMap.get("DefaultCapable", isDefaultCapable()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setFocusPainted(fieldMap.get("FocusPainted", isFocusPainted()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setRolloverEnabled(fieldMap.get("RolloverEnabled", isRolloverEnabled()));
        setSelected(fieldMap.get("Selected", isSelected()));

        setText(fieldMap.get("Text", getText()));

        setEnabled(fieldMap.get("Enabled", isEnabled()));

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

        setMaximumSize(fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize(fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize(fieldMap.get("PreferredSize", getPreferredSize()));

        setDisabledIcon(fieldMap.get("DisabledIcon", getDisabledIcon()));
        setDisabledSelectedIcon(fieldMap.get("DisabledSelectedIcon", getDisabledSelectedIcon()));
        setIcon(fieldMap.get("Icon", getIcon()));
        setPressedIcon(fieldMap.get("PressedIcon", getPressedIcon()));
        setRolloverIcon(fieldMap.get("RolloverIcon", getRolloverIcon()));
        setSelectedIcon(fieldMap.get("SelectedIcon", getSelectedIcon()));
        setRequestFocusEnabled(fieldMap.get("RequestFocusEnabled", isRequestFocusEnabled()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {
                System.out.println(thr.getMessage());
//                thr.printStackTrace();
            }
        }

        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    }

    /**
     * Writes to the ESlateFieldMap values and properties to be stored
     */
    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(saveTimer);

        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION);

        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());
        fieldMap.put("BorderPainted", isBorderPainted());
        fieldMap.put("ContentAreaFilled", isContentAreaFilled());
        fieldMap.put("DefaultCapable", isDefaultCapable());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("FocusPainted", isFocusPainted());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("RolloverEnabled", isRolloverEnabled());
        fieldMap.put("Selected", isSelected());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("HorizontalAlignment", getHorizontalAlignment());
        fieldMap.put("HorizontalTextPosition", getHorizontalTextPosition());
        fieldMap.put("VerticalAlignment", getVerticalAlignment());
        fieldMap.put("VerticalTextPosition", getVerticalTextPosition());

        fieldMap.put("Text", getText());
        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        fieldMap.put("ActionCommand", getActionCommand());

        fieldMap.put("Margin", getMargin());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());

        fieldMap.put("DisabledIcon", toRestorableImageIcon(getDisabledIcon()));
        fieldMap.put("DisabledSelectedIcon", toRestorableImageIcon(getDisabledSelectedIcon()));
        fieldMap.put("Icon", toRestorableImageIcon(getIcon()));
        fieldMap.put("PressedIcon", toRestorableImageIcon(getPressedIcon()));
        fieldMap.put("RolloverIcon", toRestorableImageIcon(getRolloverIcon()));
        fieldMap.put("SelectedIcon", toRestorableImageIcon(getSelectedIcon()));
        //      fieldMap.put("RolloverSelectedIcon",toRestorableImageIcon(getRolloverSelectedIcon()));
        fieldMap.put("RequestFocusEnabled", isRequestFocusEnabled());
        fieldMap.put("PlugsUsed", getPlugsUsed());

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

        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
    }

    public void setPlugsUsed(boolean create) {
        if (plugsCreated==create)
            return;
        if (handle != null) {
            if (create)
                createPlugs();
            else
                destroyPlugs();
            plugsCreated = create;
        } else
            plugsCreated = false;
    }

    public boolean getPlugsUsed() {
        return plugsCreated;
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

        // If the component's timers have not been constructed yet, then
        // construct them. During construction, the timers are also attached.
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

    private void createPlugs() {
        if (handle == null)
            return;
        stringSO = new StringSO(handle);
        colorSO = new ColorSO(handle);
        pressedIndicator = new BooleanSO(handle);
        colorSO.setColor(getBackground());
        try {
            SharedObjectListener sol = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        setText(((StringSO) e.getSharedObject()).getString());
                    }
                };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Title", new Color(139, 117, 0),
                        gr.cti.eslate.sharedObject.StringSO.class,
                        stringSO, sol);
            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            StringSO so = (StringSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setText(so.getString());
                        }
                    }
                }
            );
            handle.addPlug(plug);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

        try {
            SharedObjectListener sol2 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        setBackground(((ColorSO) e.getSharedObject()).getColor());
                    }
                };

            plug2 = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Color", new Color(200, 245, 100),
                        gr.cti.eslate.sharedObject.ColorSO.class,
                        colorSO, sol2);
            plug2.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            ColorSO so = (ColorSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setBackground(so.getColor());
                        }
                    }
                }
            );
            handle.addPlug(plug2);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

        try {
            plug3 = new MultipleOutputPlug(handle, bundleMessages, "Pressed", new Color(240, 230, 140),
                        gr.cti.eslate.sharedObject.BooleanSO.class,
                        pressedIndicator);
            handle.addPlug(plug3);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}
    }

    private void destroyPlugs() {
        try {
            if (plug3 != null)
                handle.removePlug(plug3);
            if (plug2 != null)
                handle.removePlug(plug2);
            if (plug != null)
                handle.removePlug(plug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug3 = null;
        plug2 = null;
        plug = null;
        stringSO = null;
        colorSO = null;
        pressedIndicator = null;
    }
    
    public void setOpaque(boolean opaque){
        super.setOpaque(opaque);
        setContentAreaFilled(opaque);
    }

    /// UTILITY METHOD - UNCOMMENT WHEN NEEDED///

    /*   public void printListeners(){
     EventListener[] l = getListeners(MouseListener.class);
     for (int i=0;i<l.length;i++)
     System.out.println(l[i]);
     System.out.println(l.length);
     }
     */

    /****************************ADDITIONS FOR 3D**************************/

    /**
     * Gets the shadow's direction.
     *
     */

    /*     public int getShadowDirection()
     {
     return shadowDirection;
     }

     /**
     * Gets the text's border color.
     *
     */

    /*     public Color getBorderColor()
     {
     return borderColor;
     }

     /**
     * Gets the text's border width.
     *
     */

    /*     public int getBorderWidth()
     {
     return borderWidth;
     }

     /**
     * Gets the text's shadow color.
     *
     */

    /*     public Color getShadowColor()
     {
     return shadowColor;
     }

     /**
     * Gets the text's shadow width.
     *
     */

    /*     public int getShadowWidth()
     {
     return shadowWidth;
     }

     /**
     * Sets the text's shadow direction.
     * @param i direction
     */

    /*     public void setShadowDirection(int i)
     {
     if(i < 9 && i > 0)
     {
     int _tmp = shadowDirection;
     shadowDirection = i;
     }
     repaint();
     }
     */

    /**
     * Sets the text's border color.
     * @param color Color
     */

    /*    public void setBorderColor(Color color)
     {
     Color _tmp = borderColor;
     borderColor = color;
     repaint();
     }
     /**
     * Sets the text's border width.
     * @param i Width
     */

    /*    public void setBorderWidth(int i)
     {
     if(i >= 0)
     {
     int _tmp = borderWidth;
     borderWidth = i;
     repaint();
     }
     }

     /**
     * Sets the text's shadow color.
     * @param color The shadow's color
     */

    /*    public void setShadowColor(Color color)
     {
     Color _tmp = shadowColor;
     shadowColor = color;
     repaint();
     }

     /**
     * Sets the text's shadow width.
     * @param i The shadow's width
     */

    /*    public void setShadowWidth(int i)
     {
     if(i >= 0)
     {
     int _tmp = shadowWidth;
     shadowWidth = i;
     }
     repaint();
     }
     */     /**
 * Sets the background color.
 * @param color The background color
 */


    /*  private synchronized void measure()
     {
     FontMetrics fontmetrics = getToolkit().getFontMetrics(getFont());
     line_height = fontmetrics.getHeight();
     line_ascent = fontmetrics.getAscent();
     max_width = 0;
     for(int i = 0; i < num_lines; i++)
     {
     line_width[i] = fontmetrics.stringWidth(lines[i]);
     if(line_width[i] > max_width)
     max_width = line_width[i];
     }

     measured = true;
     }

     private void newLabel()
     {
     StringTokenizer stringtokenizer = new StringTokenizer(text, "\n\r");
     num_lines = stringtokenizer.countTokens();
     lines = new String[num_lines];
     line_width = new int[num_lines];
     for(int i = 0; i < num_lines; i++)
     lines[i] = stringtokenizer.nextToken();

     }

     protected String layoutCL(
     JButton label,
     FontMetrics fontMetrics,
     String text,
     Icon icon,
     Rectangle viewR,
     Rectangle iconR,
     Rectangle textR)
     {
     return SwingUtilities.layoutCompoundLabel(
     (JComponent) label,
     fontMetrics,
     text,
     icon,
     label.getVerticalAlignment(),
     label.getHorizontalAlignment(),
     label.getVerticalTextPosition(),
     label.getHorizontalTextPosition(),
     viewR,
     iconR,
     textR,
     label.getIconTextGap());
     }
     */

    /**
     * Paint clippedText at textX, textY with the labels foreground color.
     *
     * @see #paint
     * @see #paintDisabledText
     */

    /*    protected void paintEnabledText(JButton l, Graphics g, String s, int textX, int textY, Color color)
     {
     int accChar = -1;// l.getDisplayedMnemonic();
     g.setColor(color);
     BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
     }

     */

    /**
     * Paint clippedText at textX, textY with background.lighter() and then
     * shifted down and to the right by one pixel with background.darker().
     *
     * @see #paint
     * @see #paintEnabledText
     */

    /*    protected void paintDisabledText(JButton l, Graphics g, String s, int textX, int textY)
     {
     int accChar = -1;//l.getDisplayedMnemonic();
     Color background = l.getBackground();
     g.setColor(background.brighter());
     BasicGraphicsUtils.drawString(g, s, accChar, textX + 1, textY + 1);
     g.setColor(background.darker());
     BasicGraphicsUtils.drawString(g, s, accChar, textX, textY);
     }

     private static Rectangle paintIconR = new Rectangle();
     private static Rectangle paintTextR = new Rectangle();
     private static Rectangle paintViewR = new Rectangle();
     private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

     public void paintComponent(Graphics g)
     {
     super.paintComponent(g);
     Icon icon = (isEnabled()) ? getIcon() : getDisabledIcon();
     int textX=0;
     int textY=0;
     if ((icon == null) && (text == null)) {
     return;
     }

     FontMetrics fm = g.getFontMetrics();
     FontMetrics fontmetrics = fm;
     Dimension dimension = getSize();
     if (!measured)
     measure();
     int i = num_lines * fm.getHeight()+(num_lines-1);
     int j;
     int forIconTextGap = 0;
     if (icon!=null &(shadowDirection==4 || shadowDirection==5 || shadowDirection==6))
     forIconTextGap =  shadowWidth;
     int forTextIconGap = 0;
     if (icon!=null &(shadowDirection==1 || shadowDirection==2 || shadowDirection==8))
     forTextIconGap =  shadowWidth;

     paintViewInsets = this.getInsets(paintViewInsets);
     paintViewR.x = paintViewInsets.left;
     paintViewR.y = paintViewInsets.top;
     paintViewR.width = this.getWidth() - (paintViewInsets.left + paintViewInsets.right);
     paintViewR.height = this.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);

     if (isOpaque()){
     g.setColor(getBackground());
     g.fillRect(paintViewR.x,paintViewR.y,paintViewR.width,paintViewR.height);
     }

     paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
     paintTextR.y = paintTextR.width = paintTextR.height = 0;

     paintTextR.x = fontmetrics.charWidth('-');
     String clippedText =
     layoutCL(this, fm, text, icon, paintViewR, paintIconR, paintTextR);
     paintIconR.x += forTextIconGap;

     if (icon != null) {
     icon.paintIcon(this,g,paintIconR.x,paintIconR.y);
     }
     if (text != null) {
     View v = (View) this.getClientProperty(BasicHTML.propertyKey);
     if (v != null) {
     v.paint(g, paintTextR);
     } else {
     textX = paintTextR.x + forIconTextGap*2;
     textY = paintTextR.y + fm.getAscent();
     j = textY;

     if (num_lines >=1)
     {
     for (int k = 0; k < num_lines; k++)
     {
     int l = 0;
     switch(l = textX)
     {
     case 0: // '\0'
     l = fontmetrics.charWidth('-');
     break;

     case 1: // '\001'
     l = (dimension.width - fontmetrics.stringWidth(lines[k])) / 2;
     break;

     case 2: // '\002'
     l = dimension.width - fontmetrics.stringWidth(lines[k]) - fontmetrics.charWidth('-');
     break;

     case 3: // '\003'
     l = 0;
     break;

     case 4: // '\004'
     l = dimension.width - fontmetrics.stringWidth(lines[k]);
     break;

     }

     if (isEnabled()) {
     Color color = shadowColor;

     for (int i1 = 1; i1<=2* shadowWidth; i1++)
     {
     Rectangle paintTextR1 = new Rectangle();
     Rectangle paintIconR1 = new Rectangle();
     paintTextR1.x = paintTextR1.width = paintTextR1.height = 0;
     paintIconR1.x = paintIconR1.y = paintIconR1.width = paintIconR1.height = 0;

     paintTextR1.y = (j-directXY[shadowDirection-1][1])/2;
     String clippedText1 =
     layoutCL(this, fm, lines[k], icon, paintViewR, paintIconR1, paintTextR1);
     paintEnabledText(this,g, clippedText1,l-directXY[shadowDirection-1][0]*i1,j-directXY[shadowDirection-1][1]*i1,color);
     }
     color = borderColor;
     for (int j1 = -borderWidth; j1<=borderWidth; j1++)
     {
     String clippedText2 =
     layoutCL(this, fm, lines[k], icon, paintViewR, paintIconR, paintTextR);
     for (int k1 = -borderWidth; k1<=borderWidth; k1++)
     if (j1!=0 || k1!=0)
     paintEnabledText(this,g, clippedText2, l+j1,j+k1,color);
     }
     color = getForeground();
     if (shadowWidth == 0 & borderWidth == 0)
     paintEnabledText(this, g, clippedText, textX, textY, color);
     else
     paintEnabledText(this, g, clippedText, l, j, color);
     j+=fm.getHeight();
     }
     }
     }
     else {
     paintDisabledText(this, g, clippedText, textX, textY);
     }
     }
     }
     }

     protected int directXY[][] = {
     {
     -1, 0
     }, {
     -1, 1
     }, {
     0, 1
     }, {
     1, 1
     }, {
     1, 0
     }, {
     1, -1
     }, {
     0, -1
     }, {
     -1, -1
     }
     };
     */

    /****************************END FOR 3D**************************/


}
