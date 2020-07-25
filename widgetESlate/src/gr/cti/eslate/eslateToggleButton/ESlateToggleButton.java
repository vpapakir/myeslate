package gr.cti.eslate.eslateToggleButton;

import javax.swing.*;
import gr.cti.eslate.utils.*;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.plaf.*;
import java.util.*;
import java.io.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;


public class ESlateToggleButton extends JToggleButton implements ESlatePart, Externalizable {

    private StringSO stringSO;
    private ColorSO colorSO;
    private BooleanSO pressedIndicator;
    SharedObjectPlug plug, plug2, plug3;
    private ESlateHandle handle;
    private static final int FORMAT_VERSION = 1;
    private static boolean plugsCreated = false;
    static final long serialVersionUID = -95374377892289582L;

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

    private static ResourceBundle bundleMessages;
    private final static String version = "2.0.5";

    /**
     * Returns Copyright information.
     * @return	The Copyright information.
     */
    private ESlateInfo getInfo() {
        String[] info = {
                bundleMessages.getString("part"),
                bundleMessages.getString("development"),
                //bundleMessages.getString("funding"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }

    /**

     * Constructs a new ESlateToggleButton
     *
     */

    public ESlateToggleButton() {
        this(null,null, false);
    }

    public ESlateToggleButton(String s, Icon i){
        this(s,i,false);
    }
    public ESlateToggleButton(String s, boolean selected){
        this(s,null, selected);
    }
    public ESlateToggleButton(Icon i, boolean selected){
        this(null,i,selected);
    }
    public ESlateToggleButton(String s){
        this(s,null, false);
    }
    public ESlateToggleButton(Icon i){
        this(null,i,false);
    }
    public ESlateToggleButton(Action a){
        this(null,null,false);
        setAction(a);
    }

    public ESlateToggleButton(String s, Icon i, boolean selected) {
        super(s,i,selected);
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateToggleButton.BundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setOpaque(true);
        setPreferredSize(new Dimension(100, 60));
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
            handle.setInfo(getInfo());
            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateToggleButton"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }

            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ToggleButtonPrimitives");

            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();

                        pm.removePerformanceListener(perfListener);
                        perfListener = null;

                    }
                }
            );

            addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (pressedIndicator != null) {
                            if (isSelected() == true && isEnabled() == true) {
                                pressedIndicator.setBoolean(true);
                            } else if (isSelected() == false && isEnabled() == true) {
                                pressedIndicator.setBoolean(false);
                            }
                        }
                    }
                }
            );

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            setPlugsUsed(true);

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
        super.setBackground(c);
        if (isEnabled() == true) {
            if (colorSO != null)
                colorSO.setColor(c);
        }
    }

    /**
     * Sets the button to be selected or unselected.
     */


    public void setSelected(boolean b) {
        super.setSelected(b);
        if (isEnabled() == true) {
            if (pressedIndicator != null)
                pressedIndicator.setBoolean(b);
        }
    }

    /**
     * Sets the button's text.
     * @param s The text
     */

    public void setText(String s) {
        if (getText() != null && getText().equals(s)) return;
        super.setText(s);
        if (isEnabled() == true) {
            if (stringSO != null)
                stringSO.setString(s);
        }
    }

    /**
     * Turns an Icon into a NewRestorableImageIcon (in case its not)
     * @param icon The icon to be transformed to NewRestorableImageIcon
     */

    public Icon toRestorableImageIcon(Icon icon) {
        if (icon == null || icon instanceof NewRestorableImageIcon)
            return icon;
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

        icon.paintIcon(this, b.getGraphics(), 0, 0);
        return new NewRestorableImageIcon(b);
    }

    /**
     * Reads from the ESlateFieldMap to restore stored values and properties
     */

    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(loadTimer);
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
        setEnabled(fieldMap.get("Enabled", isEnabled()));

        setText(fieldMap.get("Text", getText()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setHorizontalAlignment(fieldMap.get("HorizontalAlignment", getHorizontalAlignment()));
        setHorizontalTextPosition(fieldMap.get("HorizontalTextPosition", getHorizontalTextPosition()));
        setVerticalAlignment(fieldMap.get("VerticalAlignment", getVerticalAlignment()));
        setVerticalTextPosition(fieldMap.get("VerticalTextPosition", getVerticalTextPosition()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        setActionCommand(fieldMap.get("ActionCommand", getActionCommand()));

        setMargin((Insets) fieldMap.get("Margin", getMargin()));

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));

        setDisabledIcon(fieldMap.get("DisabledIcon", getDisabledIcon()));
        setDisabledSelectedIcon(fieldMap.get("DisabledSelectedIcon", getDisabledSelectedIcon()));
        setIcon(fieldMap.get("Icon", getIcon()));
        setPressedIcon(fieldMap.get("PressedIcon", getPressedIcon()));
        setRolloverIcon(fieldMap.get("RolloverIcon", getRolloverIcon()));
        setSelectedIcon(fieldMap.get("SelectedIcon", getSelectedIcon()));
        setRolloverSelectedIcon(fieldMap.get("RolloverSelectedIcon", getRolloverSelectedIcon()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));

        if (fieldMap.containsKey("Border")) {
            BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");
            setBorder(bd.getBorder());
        }

        /*        if (fieldMap.containsKey("Border")){
         if (fieldMap.get("Border")==null)
         setBorder(null);
         else{
         try{
         BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");
         System.out.println("not null?");
         setBorder(bd.getBorder());
         }catch (Throwable thr) {thr.printStackTrace();}
         }
         }
         */

        pm.stop(loadTimer);
        pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
    }

    //    public void setBorder(javax.swing.border.Border border) {
    //         super.setBorder(border);
    //    }

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

        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());

        fieldMap.put("DisabledIcon", toRestorableImageIcon(getDisabledIcon()));
        fieldMap.put("DisabledSelectedIcon", toRestorableImageIcon(getDisabledSelectedIcon()));
        fieldMap.put("Icon", toRestorableImageIcon(getIcon()));
        fieldMap.put("PressedIcon", toRestorableImageIcon(getPressedIcon()));
        fieldMap.put("RolloverIcon", toRestorableImageIcon(getRolloverIcon()));
        fieldMap.put("SelectedIcon", toRestorableImageIcon(getSelectedIcon()));
        fieldMap.put("RolloverSelectedIcon", toRestorableImageIcon(getRolloverSelectedIcon()));
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
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }

        out.writeObject(fieldMap);
        pm.stop(saveTimer);
        pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
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
            SharedObjectListener sol3 = new SharedObjectListener() {
                    public void handleSharedObjectEvent(SharedObjectEvent e) {
                        setSelected(((BooleanSO) e.getSharedObject()).getBooleanValue());
                    }
                };

            plug3 = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Pressed", new Color(240, 230, 140),
                        gr.cti.eslate.sharedObject.BooleanSO.class,
                        pressedIndicator, sol3);
            plug3.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            BooleanSO so = (BooleanSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setSelected(so.getBooleanValue());
                        }
                    }
                }
            );
            handle.addPlug(plug3);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}
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

    public void setOpaque(boolean opaque){
        super.setOpaque(opaque);
        setContentAreaFilled(opaque);
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
}
