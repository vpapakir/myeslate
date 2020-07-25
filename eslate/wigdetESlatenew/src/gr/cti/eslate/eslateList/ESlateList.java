package gr.cti.eslate.eslateList;

import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleOutputPlug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.Externalizable;
import java.io.IOException;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.UIResource;


public class ESlateList extends JScrollPane implements ESlatePart, Externalizable {

    SelectionChangedEventMulticaster selectionChangedListener = new SelectionChangedEventMulticaster();
    private ESlateHandle handle;
    SharedObjectPlug plug;
    private static final int FORMAT_VERSION = 1;
    private boolean plugsUsed = false;
    private int oldSelectedIndex = -1;
    private StringSO stringSO;
    JList list;

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

    static final long serialVersionUID = 9147707859947305819L;

    private static ResourceBundle bundleMessages;
    private final static String version = "2.0.4";

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

     * Constructs a new ESlateList
     *
     */

    public ESlateList() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateList.ListBundle", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        setOpaque(true);
        setBackground(new Color(255, 255, 255));
        list = new JList();
        list.setOpaque(false);
        list.setBorder(null);
        setViewportView(list);
        getViewport().setOpaque(false);
        setBorder(new NoTopOneLineBevelBorder(0));

        setPreferredSize(new Dimension(140, 150));
        list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (list.getSelectedValue() != null && isEnabled() == true)
                        if (handle != null && stringSO != null)
                            stringSO.setString((String) list.getSelectedValue().toString());
                    fireSelectionChanged();
                }
            }
        );

        list.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMousePressed(newEvent);
                    if (list.getSelectedIndex() != oldSelectedIndex/*|| list.getSelectedValues().length != numOfOldSelectedValues*/)
                        fireSelectionChanged();
                }

                public void mouseReleased(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseReleased(newEvent);
                }

                public void mouseClicked(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseClicked(newEvent);
                }

                public void mouseEntered(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseEntered(newEvent);
                }

                public void mouseExited(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseExited(newEvent);
                }
            }
        );

        list.addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseDragged(newEvent);
                }

                public void mouseMoved(MouseEvent e) {
                    MouseEvent newEvent = new MouseEvent(ESlateList.this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());

                    fireMouseMoved(newEvent);
                }
            }
        );

        list.addVetoableChangeListener(new VetoableChangeListener() {
                public void vetoableChange(PropertyChangeEvent e) {
                    PropertyChangeEvent newEvent = new PropertyChangeEvent(ESlateList.this, e.getPropertyName(), e.getOldValue(), e.getNewValue());

                    fireVetoableChange(newEvent);
                }
            }
        );

        list.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    PropertyChangeEvent newEvent = new PropertyChangeEvent(ESlateList.this, e.getPropertyName(), e.getOldValue(), e.getNewValue());

                    firePropertyChange(newEvent);
                }
            }
        );
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
            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();

                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );
            setPlugsUsed(true);
            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateList"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            //setPlugsUsed(true);

            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.ESlateListPrimitives");
            handle.setInfo(getInfo());
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

        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setValueIsAdjusting(fieldMap.get("ValueIsAdjusting", getValueIsAdjusting()));

        setVisibleRowCount(fieldMap.get("VisibleRowCount", getVisibleRowCount()));

        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setRequestFocusEnabled(fieldMap.get("RequestFocusEnabled", isRequestFocusEnabled()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));

        if (fieldMap.containsKey("Model")) {
            try {
                Vector v = (Vector) fieldMap.get("Model", (Object) null);
                DefaultComboBoxModel model = new DefaultComboBoxModel(v);

                setModel(model);
            } catch (Throwable thr) {}
        }

        if (fieldMap.containsKey("SelectedIndex")) {
            try {
                list.setSelectedIndex(fieldMap.get("SelectedIndex", list.getSelectedIndex()));
            } catch (Throwable thr) {}
        }

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));
        setSelectionBackground(fieldMap.get("SelectionBackground", getSelectionBackground()));
        setSelectionForeground(fieldMap.get("SelectionForeground", getSelectionForeground()));
        setSelectionMode(fieldMap.get("SelectionMode", getSelectionMode()));
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

        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("ValueIsAdjusting", getValueIsAdjusting());

        fieldMap.put("VisibleRowCount", getVisibleRowCount());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("RequestFocusEnabled", isRequestFocusEnabled());

        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());

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
        fieldMap.put("SelectionBackground", getSelectionBackground());
        fieldMap.put("SelectionForeground", getSelectionForeground());
        fieldMap.put("SelectionMode", getSelectionMode());
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

    public void addSelectionChangedListener(SelectionChangedListener listener) {
        selectionChangedListener.add(listener);
    }

    public void removeSelectionChangedListener(SelectionChangedListener listener) {
        selectionChangedListener.remove(listener);
    }

    public void addSelectionInterval(int index0, int index1) {
        if (list != null)
            list.addSelectionInterval(index0, index1);
    }

    public void removeSelectionInterval(int index0, int index1) {
        if (list != null)
            list.removeSelectionInterval(index0, index1);
    }

    public int locationToIndex(Point p){
        if (list != null)
            return list.locationToIndex(p);
        else
            return -1;
    }

    public void ensureIndexIsVisible(int index){
        if (list != null)
            list.ensureIndexIsVisible(index);
    }

    public void fireSelectionChanged() {
        if (selectionChangedListener != null && oldSelectedIndex != getSelectedIndex()) {
            SelectionChangedEvent en = new SelectionChangedEvent(ESlateList.this, SelectionChangedEvent.SELECTION_CHANGED_LAST);
            selectionChangedListener.selectionChanged(en);
            oldSelectedIndex = getSelectedIndex();
        }
    }

    public Object getSelectedValue() {
        return list.getSelectedValue();
    }

    public Object[] getSelectedValues() {
        return list.getSelectedValues();
    }

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public ListModel getModel() {
        return list.getModel();
    }

    public Color getSelectionBackground() {
        return list.getSelectionBackground();
    }

    public Color getSelectionForeground() {
        return list.getSelectionForeground();
    }

    public int getSelectionMode() {
        return list.getSelectionMode();
    }

    public int getVisibleRowCount() {
        return list.getVisibleRowCount();
    }

    public ListSelectionModel getSelectionModel() {
        return list.getSelectionModel();
    }

    public boolean getValueIsAdjusting() {
        return list.getValueIsAdjusting();
    }

    public void setSelectionBackground(Color c) {
        if (list != null && list.getSelectionBackground() == c) return;
        list.setSelectionBackground(c);
    }

    public void clearSelection(){
        if (list != null)
            list.clearSelection();
    }

    public void setSelectionForeground(Color c) {
        if (list != null && list.getSelectionForeground() == c) return;
        list.setSelectionForeground(c);
    }

    public void setSelectionMode(int i) {
        if (list != null &&  list.getSelectionMode() == i) return;
        list.setSelectionMode(i);
    }

    public void setVisibleRowCount(int i) {
        if (list != null &&  list.getVisibleRowCount() == i) return;
        list.setVisibleRowCount(i);
    }

    public void setValueIsAdjusting(boolean b) {
        if (list != null &&  list.getValueIsAdjusting() == b) return;
        list.setValueIsAdjusting(b);
    }

    public void setModel(ListModel l) {
        if (list != null &&  list.getModel() == l) return;
        list.setModel(l);
    }

    public void setSelectionModel(ListSelectionModel l) {
        if (list != null &&  list.getSelectionModel() == l) return;
        list.setSelectionModel(l);
    }

    public void setSelectedIndex(int i) {
        if (list != null &&  list.getSelectedIndex() == i) return;
        list.setSelectedIndex(i);
    }

    public void setSelectedValue(String s) {
        if (list != null &&  list.getSelectedValue() == s) return;
        list.setSelectedValue(s, true);
    }

    public void setFont(Font f) {
        if (list != null && list.getFont() == f) return;
        if (list != null)
            list.setFont(f);
    }

    public void setRequestFocusEnabled(boolean b) {
        if (list != null && list.isRequestFocusEnabled() == b) return;
        if (list != null)
            list.setRequestFocusEnabled(b);
    }


    public boolean isRequestFocusEnabled() {
        if (list != null)
            return list.isRequestFocusEnabled();
        else return false;
    }

    public Font getFont() {
        if (list != null)
            return list.getFont();
        else return null;
    }

    public int getFixedCellWidth() {
        return list.getFixedCellWidth();
    }

    public int getFixedCellHeight() {
        return list.getFixedCellHeight();
    }

    public void setFixedCellWidth(int i) {
        if (list != null &&  list.getFixedCellWidth() == i) return;
        list.setFixedCellWidth(i);
    }

    public void setFixedCellHeight(int i) {
        if (list != null &&  list.getFixedCellHeight() == i) return;
        list.setFixedCellHeight(i);
    }

    public void setOpaque(boolean b) {
        if (super.isOpaque() == b) return;
        super.setOpaque(b);
        if (list != null && list.getCellRenderer() != null)
            ((DefaultListCellRenderer) list.getCellRenderer()).setOpaque(b);
    }

    public boolean isOpaque() {
        return  super.isOpaque();
    }

    public void setForeground(Color c) {
        if (list != null &&  list.getForeground() == c) return;
        if (list != null)
            list.setForeground(c);
    }

    public Color getForeground() {
        if (list != null)
            return list.getForeground();
        else return null;
    }

    public void setBackground(Color c) {
        if (list != null &&  list.getBackground() == c) return;
        if (list != null)
            list.setBackground(c);
    }

    public Color getBackground() {
        if (list != null)
            return list.getBackground();
        else return null;
    }

    public void setEnabled(boolean b) {
        if (super.isEnabled() == b) return;
        super.setEnabled(b);
        list.setEnabled(b);
    }

    public boolean isEnabled() {
        return list.isEnabled();
    }

    public void addMouseListener(MouseListener l) {
        listenerList.add(MouseListener.class, l);
    }

    public void removeMouseListener(MouseListener l) {
        listenerList.remove(MouseListener.class, l);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        listenerList.add(MouseMotionListener.class, l);
    }

    public void removeMouseMotionListener(MouseMotionListener l) {
        listenerList.remove(MouseMotionListener.class, l);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        listenerList.add(PropertyChangeListener.class, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        listenerList.remove(PropertyChangeListener.class, l);
    }

    public void addVetoableChangeListener(VetoableChangeListener l) {
        listenerList.add(VetoableChangeListener.class, l);
    }

    public void removeVetoableChangeListener(VetoableChangeListener l) {
        listenerList.remove(VetoableChangeListener.class, l);
    }

    protected void fireMousePressed(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mousePressed(e);
        }
    }

    protected void fireMouseReleased(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseReleased(e);
        }
    }

    protected void fireMouseClicked(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseClicked(e);
        }
    }

    protected void fireMouseEntered(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseEntered(e);
        }
    }

    protected void fireMouseExited(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseListener) listeners[i]).mouseExited(e);
        }
    }

    protected void fireMouseDragged(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseMotionListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseMotionListener) listeners[i]).mouseDragged(e);
        }
    }

    protected void fireMouseMoved(MouseEvent e) {
        EventListener[] listeners = listenerList.getListeners(MouseMotionListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((MouseMotionListener) listeners[i]).mouseMoved(e);
        }
    }

    protected void fireVetoableChange(PropertyChangeEvent e) {
        EventListener[] listeners = listenerList.getListeners(VetoableChangeListener.class);
        for (int i = 0; i < listeners.length; i++) {
            try {
                ((VetoableChangeListener) listeners[i]).vetoableChange(e);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    protected void firePropertyChange(PropertyChangeEvent e) {
        EventListener[] listeners = listenerList.getListeners(PropertyChangeListener.class);
        for (int i = 0; i < listeners.length; i++) {
            ((PropertyChangeListener) listeners[i]).propertyChange(e);
        }
    }

    public JList getList() {
        return list;
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
        if (plugsUsed == create) 
            return;
        plugsUsed = create;
        if (handle != null) {
            if (create)
                createPlugs();
            else
                destroyPlugs();
        } else
            plugsUsed = false;
    }

    public boolean getPlugsUsed() {
        return plugsUsed;
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
