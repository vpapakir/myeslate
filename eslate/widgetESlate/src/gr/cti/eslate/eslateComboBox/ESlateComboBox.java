package gr.cti.eslate.eslateComboBox;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import gr.cti.eslate.utils.*;
import java.io.IOException;
import java.awt.*;
import java.util.*;
import java.util.Vector;
import gr.cti.eslate.base.*;
import gr.cti.eslate.sharedObject.*;
import java.io.*;

import java.beans.PropertyChangeEvent;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;


public class ESlateComboBox extends JComboBox implements ESlatePart, Externalizable {

    private ESlateHandle handle;
    private static final int FORMAT_VERSION = 1;
    private StringSO stringSO;
    static final long serialVersionUID = 6153878593645832857L;
    private static ResourceBundle bundleMessages;
    private final static String version = "2.0.5";
    private boolean plugsCreated = false;
    SharedObjectPlug plug;

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

     * Constructs a new ESlateComboBox
     *
     */

    public ESlateComboBox(ComboBoxModel aModel) {
        this();
        setModel(aModel);
    }

    public ESlateComboBox(Object[] items){
        this();
        setModel(new DefaultComboBoxModel(items));
    }

    public ESlateComboBox(Vector items){
        this();
        setModel(new DefaultComboBoxModel(items));
    }

    public ESlateComboBox() {
        super();
        setLightWeightPopupEnabled(false);
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateComboBox.ComboBoxBundle", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setOpaque(true);
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
    }

    /**
     * Sets the selected item's text.
     * @param s The text
     */

    public void setItem(String s) {
        if (isEnabled() == true && stringSO != null) {
            stringSO.setString(s);
        }
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
                handle.setUniqueComponentName(bundleMessages.getString("ESlateComboBox"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ComboBoxPrimitives");
            handle.setInfo(getInfo());
            attachTimers();
            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();

                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );
            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateComboBox"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ComboBoxPrimitives");
            handle.setInfo(getInfo());

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            //setPlugsUsed(true);

            addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (getSelectedIndex() != -1 && isEnabled() == true && stringSO != null)
                            stringSO.setString(getItemAt(getSelectedIndex()).toString());
                    }
                }
            );
            setPlugsUsed(true);
            pm.stop(initESlateAspectTimer);
            pm.eSlateAspectInitEnded(this);
            pm.displayTime(initESlateAspectTimer, handle, "", "ms");
        }
        return handle;
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
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setPopupVisible(fieldMap.get("PopupVisible", isPopupVisible()));
        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setMaximumRowCount(fieldMap.get("MaximumRowCount", getMaximumRowCount()));
        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));
        setActionCommand(fieldMap.get("ActionCommand", getActionCommand()));
        if (fieldMap.containsKey("Model")) {
            try {
                Vector v = (Vector) fieldMap.get("Model", (Object) null);
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);

                setModel(model);
            } catch (Throwable thr) {}
        }
        if (fieldMap.containsKey("SelectedIndex")) {
            try {
                setSelectedIndex(fieldMap.get("SelectedIndex", getSelectedIndex()));
            } catch (Throwable thr) {}
        }
        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));
        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));
        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
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

        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("MaximumRowCount", getMaximumRowCount());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("PopupVisible", isPopupVisible());
        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());

        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());
        fieldMap.put("ActionCommand", getActionCommand());

        if (getModel() != null) {
            Vector v = new Vector();

            for (int i = 0; i < getModel().getSize(); i++) {
                v.add(getModel().getElementAt(i));
            }
            fieldMap.put("Model", v);
        }

        if (getSelectedIndex() != -1) {
            fieldMap.put("SelectedIndex", getSelectedIndex());
        }

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());
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

    private void createPlugs() {
        if (handle == null)
            return;
        stringSO = new StringSO(handle);
        try {

            plug = new MultipleOutputPlug(handle, bundleMessages, "List Element", new Color(139, 117, 0),
                        gr.cti.eslate.sharedObject.StringSO.class,
                        stringSO/*,sol*/);
            handle.addPlug(plug);
        } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}
    }

    private void destroyPlugs() {
        try {
            if (plug != null)
                handle.removePlug(plug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug = null;
        stringSO = null;
    }

    
}
