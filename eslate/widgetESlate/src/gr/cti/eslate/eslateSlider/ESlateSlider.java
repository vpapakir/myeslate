package gr.cti.eslate.eslateSlider;


import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateAdapter;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.HandleDisposalEvent;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleInputMultipleOutputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.utils.BorderDescriptor;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateUtils;
import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Externalizable;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSliderUI;


public class ESlateSlider extends JSlider implements ESlatePart, Externalizable {

    private static final int FORMAT_VERSION = 1;
    private Dictionary labelTable;
    private ESlateHandle handle;
    static final long serialVersionUID = -7201666736261201053L;
    private ResourceBundle bundleMessages;
    private NumberSO sliderValue;
    private final static String version = "3.0.4";
    private boolean ancestorAddedFirstCall = true;
    SharedObjectPlug plug;
    boolean plugsCreated = false;

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
                //bundleMessages.getString("funding"),
                bundleMessages.getString("copyright")
            };

        return new ESlateInfo(
                bundleMessages.getString("componentName") + " " +
                bundleMessages.getString("version") + " " + version,
                info);
    }

    /**

     * Constructs a new ESlateSlider
     */

    public ESlateSlider() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateSlider.BundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setPreferredSize(new Dimension(200, 60));
        setForeground(Color.black);

//        setUI(new ImprovedSliderUI());
        setOpaque(true);
        setBorder(new NoTopOneLineBevelBorder(0));

        addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int position, value;

                    if (getOrientation() == SwingConstants.HORIZONTAL) {
                        position = e.getX();
                        value = ((BasicSliderUI) getUI()).valueForXPosition(position);
                    } else {
                        position = e.getY();
                        value = ((BasicSliderUI) getUI()).valueForYPosition(position);
                    }
//                    ((BasicSliderUI) getUI()).setBlockIncrement(value);
                    if (value != getValue()) {
                        setValue(value);
                    }

                }
            }
        );

        addAncestorListener(new AncestorListener() {
                public void ancestorAdded(AncestorEvent event) {
                    if (!ancestorAddedFirstCall) return;

                    ancestorAddedFirstCall = false;
                    SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                setOrientation((getOrientation() == JSlider.HORIZONTAL) ? JSlider.VERTICAL : JSlider.HORIZONTAL);
                                setOrientation((getOrientation() == JSlider.HORIZONTAL) ? JSlider.VERTICAL : JSlider.HORIZONTAL);
                            }
                        }
                    );
                }

                public void ancestorRemoved(AncestorEvent event) {}

                public void ancestorMoved(AncestorEvent event) {}
            }
        );
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");
    }

    protected void validateTree() {
    	synchronized(getTreeLock()) {
            super.validateTree();
        }
    }

    /**
     * Returns the components handle
     * @return handle The ESlateHandle
     */


    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();

            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);

            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateSlider"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addESlateListener(new ESlateAdapter() {
                    public void handleDisposed(HandleDisposalEvent e) {
                        PerformanceManager pm = PerformanceManager.getPerformanceManager();

                        pm.removePerformanceListener(perfListener);
                        perfListener = null;
                    }
                }
            );
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.SliderPrimitives");
            handle.setInfo(getInfo());

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
     * Sets the slider's value.
     * @param value The numeric value
     */

    public void setValue(int value) {
        super.setValue(value);
        repaint();
        if (sliderValue != null){
            sliderValue.setValue(value);
        }
    }

    /**
     * Sets the slider's value.
     * @param value The numeric value
     * @param path The path of the connections between components
     * path is nessesary to avoid cyclic value interchange between connected components
     */

    /*
     public void setValue(int value, Vector path) {

     super.setValue(value);
     repaint();
     sliderValue.setValue(value, path);
     }
     */

    /*        public void Value(int integer){
     if (isEnabled() == true){
     super.setValue(integer);
     //             sliderValue.setValue(integer,new Vector());
     // setToolTipText(getValueAsText()); //if remove this move string val to paint
     repaint(); //to show value [see paint()]

     }
     }
     */


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

        if (fieldMap.containsKey("Border")) {
            try {
                BorderDescriptor bd = (BorderDescriptor) fieldMap.get("Border");

                setBorder(bd.getBorder());
            } catch (Throwable thr) {}
        }

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setInverted(fieldMap.get("Inverted", getInverted()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));
        setPaintLabels(fieldMap.get("PaintLabels", getPaintLabels()));
        setPaintTicks(fieldMap.get("PaintTicks", getPaintTicks()));
        setPaintTrack(fieldMap.get("PaintTrack", getPaintTrack()));
        setSnapToTicks(fieldMap.get("SnapToTicks", getSnapToTicks()));

        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));

        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setName(fieldMap.get("Name", getName()));

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        setMaximum(fieldMap.get("Maximum", getMaximum()));
        setMinimum(fieldMap.get("Minimum", getMinimum()));

        setOrientation(fieldMap.get("Orientation", getOrientation()));

        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));

        setLayout((LayoutManager) fieldMap.get("Layout", getLayout()));

        setMajorTickSpacing(fieldMap.get("MajorTickSpacing", getMajorTickSpacing()));
        setMinorTickSpacing(fieldMap.get("MinorTickSpacing", getMinorTickSpacing()));
        setValue(fieldMap.get("Value", getValue()));
        if (fieldMap.containsKey("PlugsUsed"))
            setPlugsUsed(fieldMap.get("PlugsUsed", getPlugsUsed()));
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

        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());

        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Inverted", getInverted());
        fieldMap.put("Opaque", isOpaque());
        fieldMap.put("PaintLabels", getPaintLabels());
        fieldMap.put("PaintTicks", getPaintTicks());
        fieldMap.put("PaintTrack", getPaintTrack());
        fieldMap.put("SnapToTicks", getSnapToTicks());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());

        fieldMap.put("Orientation", getOrientation());

        fieldMap.put("ToolTipText", getToolTipText());
        fieldMap.put("Name", getName());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

        fieldMap.put("Maximum", getMaximum());
        fieldMap.put("Minimum", getMinimum());

        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.get("PreferredSize", getPreferredSize());

        fieldMap.put("MajorTickSpacing", getMajorTickSpacing());
        fieldMap.put("MinorTickSpacing", getMinorTickSpacing());
        fieldMap.put("Value", getValue());
        fieldMap.put("PlugsUsed", getPlugsUsed());
        
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

    /**
     * Pains a slider (without the swing errors)
     */

    public void paint(Graphics g) {
        super.paint(g); //paint first of all, next code might take a bit long...
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String tipText = getValueAsText();
        Dimension size = new Dimension(metrics.stringWidth(tipText), metrics.getHeight() - 2); //-2???
        Dimension size2 = new Dimension(metrics.stringWidth("" + getMaximum()), metrics.getHeight() - 2); //-2???

        if (getPaintLabels()) {
            if (getOrientation() == SwingConstants.VERTICAL && (size.width + size2.width + 40) > getWidth()) {
                g.setColor(getBackground());
                g.fillRect(0, getHeight(), size.width, size.height); //16-7-1998: added +1 to avoid slider trimming (???)
                g.setColor(Color.black);
                g.drawString(tipText, 0, getHeight());//getWidth()/2, pos);
                return;
            }
        } else {
            if (getOrientation() == SwingConstants.VERTICAL && (size.width + 40) > getWidth()) {
                g.setColor(getBackground());
                g.fillRect(0, getHeight(), size.width, size.height); //16-7-1998: added +1 to avoid slider trimming (???)
                g.setColor(Color.black);
                g.drawString(tipText, 0, getHeight());//getWidth()/2, pos);
                return;
            }
        }

        int pos, mm = getMaximum() - getMinimum();
        double val;

        if (getInverted()) val = super.getMaximum() - super.getValue(); //if display inverted, invert pos too
        else val = getValue() - getMinimum();
        if (getOrientation() == SwingConstants.HORIZONTAL) {
            int spacer = 0;

            if (mm != 0)
                pos = (int) (getWidth() * (val / mm)) - size.width / 2; //val must be a double
            else
                pos = 0;
            if (pos < 0) {
                pos = 0;
                spacer = 3;
            } else if (pos > getWidth() - size.width - 1) {
                pos = getWidth() - size.width - 1; //-1 used cause "all left"=0
                spacer = -3;
            }
            g.setColor(getBackground());
            g.fillRect(pos + spacer, getHeight() - size.height - 3, size.width, size.height); //16-7-1998: added +1 to avoid slider trimming (???)
            g.setColor(getForeground()); //g.setColor(Color.red);//
            g.drawString(tipText, pos + spacer, getHeight() - 3);
        } else {
            int spacer = 0;

            if (mm != 0)
                pos = getHeight() - (int) (getHeight() * (val / mm)) - size.height / 2; //val must be a double
            else
                pos = getHeight();
            if (pos < 0) {
                pos = 0;
                spacer = 3;
            } else if (pos > getHeight() - size.height - 3) {
                pos = getHeight() - size.height - 2; //-1 used cause "all left"=0
                spacer = -5;
            }
            g.setColor(getBackground());
            g.fillRect(getWidth() - size.width - 3, pos + spacer, size.width, size.height); //16-7-1998: added +1 to avoid slider trimming (???)
            g.setColor(getForeground());
            g.drawString(tipText, getWidth() - size.width - 4, pos + size.height);//getWidth()/2, pos);
        }
        //now paint the label...

    }

    /**
     * Returns the slider value as text
     */

    public String getValueAsText() {
        return "" + getValue();
    }

    /**
     * Sets the value indicator's and the labels font
     */


    public void setFont(Font f) {
        super.setFont(f);
        if (labelTable == null && getMajorTickSpacing() > 0)
            setLabelTable(createStandardLabels(getMajorTickSpacing()));

    }

    public void setForeground(Color c) {
        super.setForeground(c);
        repaint();
    }

    private void createPlugs() {
        if (handle == null)
            return;
        sliderValue = new NumberSO(handle, 50);
        sliderValue.setValue(getValue());

        SharedObjectListener sol = new SharedObjectListener() {
                public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                    setValue(((NumberSO) e.getSharedObject()).intValue());

                }
            };

        try {
            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Value", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        sliderValue, sol);

            plug.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        // make sure connected component gets value of vector when connected
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                            setValue(so.intValue());
                        }
                    }
                }
            );
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
        sliderValue = null;
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

    // (2006-06-20) Not used anymore.
    
    class ImprovedSliderUI extends javax.swing.plaf.basic.BasicSliderUI {

        int blockIncrement = 1;

        public ImprovedSliderUI() {
            super(ESlateSlider.this);
        }

        public void scrollByBlock(int direction) {
            synchronized (ESlateSlider.this) {

                int oldValue = getValue();
                int delta = blockIncrement * ((direction > 0) ? POSITIVE_SCROLL : NEGATIVE_SCROLL);

                setValue(oldValue + delta);
            }
        }

        protected void setBlockIncrement(int i) {
            blockIncrement = i;
        }

        public void paintLabels(Graphics g) {
            Rectangle labelBounds = labelRect;

            Dictionary dictionary = slider.getLabelTable();

            if (dictionary != null) {
                Enumeration keys = dictionary.keys();

                while (keys.hasMoreElements()) {
                    Integer key = (Integer) keys.nextElement();
                    Component label = (Component) dictionary.get(key);

                    if (slider.getOrientation() == JSlider.HORIZONTAL) {
                        g.translate(0, labelBounds.y);
                        paintHorizontalLabel(g, key.intValue(), label);
                        g.translate(0, -labelBounds.y);
                    } else {
                        int offset = 0;

                        /*if(!javax.swing.plaf.basic.BasicGraphicsUtils.isLeftToRight(ESlateSlider.this)) {
                         offset = labelBounds.width -
                         label.getPreferredSize().width;
                         } */
                        g.translate(labelBounds.x + offset, 0);
                        paintVerticalLabel(g, key.intValue(), label);
                        g.translate(-labelBounds.x - offset, 0);
                    }
                }
            }

        }

        /**
         * Called for every label in the label table.  Used to draw the labels for horizontal sliders.
         * The graphics have been translated to labelRect.y already.
         * @see JSlider#setLabelTable
         */
        protected void paintHorizontalLabel(Graphics g, int value, Component label) {
            int labelCenter = xPositionForValue(value);
            int labelLeft = labelCenter - (label.getPreferredSize().width / 2);

            g.translate(labelLeft, 0);
            g.setFont(ESlateSlider.this.getFont());
            label.paint(g);
            g.translate(-labelLeft, 0);
        }

        /**
         * Called for every label in the label table.  Used to draw the labels for vertical sliders.
         * The graphics have been translated to labelRect.x already.
         * @see JSlider#setLabelTable
         */
        protected void paintVerticalLabel(Graphics g, int value, Component label) {
            int labelCenter = yPositionForValue(value);
            int labelTop = labelCenter - (label.getPreferredSize().height / 2);

            g.translate(0, labelTop);
            label.paint(g);
            g.translate(0, -labelTop);
        }

    }
    
    

    public Hashtable createStandardLabels(int increment, int start) {
        if (start > getMaximum() || start < getMinimum()) {
            throw new IllegalArgumentException("Slider label start point out of range.");
        }

        if (increment <= 0) {
            throw new IllegalArgumentException("Label incremement must be > 0");
        }
        class SmartHashtable extends Hashtable implements java.beans.PropertyChangeListener {
            int increment = 0;
            int start = 0;
            boolean startAtMin = false;

            class LabelUIResource extends JLabel implements javax.swing.plaf.UIResource {
                public LabelUIResource(String text, int alignment) {
                    super(text, alignment);
                    this.setFont(ESlateSlider.this.getFont());
                }
            }

            public SmartHashtable(int increment, int start) {
                super();
                this.increment = increment;
                this.start = start;
                startAtMin = start == getMinimum();
                createLabels();
            }

            public void propertyChange(java.beans.PropertyChangeEvent e) {
                if (e.getPropertyName().equals("minimum") && startAtMin) {
                    start = getMinimum();
                }

                if (e.getPropertyName().equals("minimum") ||
                    e.getPropertyName().equals("maximum")) {

                    Enumeration keys = getLabelTable().keys();
                    Object key = null;
                    Hashtable hashtable = new Hashtable();

                    // Save the labels that were added by the developer
                    while (keys.hasMoreElements()) {
                        key = keys.nextElement();
                        Object value = getLabelTable().get(key);

                        if (!(value instanceof LabelUIResource)) {
                            hashtable.put(key, value);
                        }
                    }

                    clear();
                    createLabels();

                    // Add the saved labels
                    keys = hashtable.keys();
                    while (keys.hasMoreElements()) {
                        key = keys.nextElement();
                        put(key, hashtable.get(key));
                    }

                    ((JSlider) e.getSource()).setLabelTable(this);
                }
            }

            void createLabels() {
                for (int labelIndex = start; labelIndex <= getMaximum(); labelIndex += increment) {
                    put(new Integer(labelIndex), new LabelUIResource("" + labelIndex, JLabel.CENTER));
                }
            }
        }

        SmartHashtable table = new SmartHashtable(increment, start);

        if (getLabelTable() != null && (getLabelTable() instanceof PropertyChangeListener)) {
            removePropertyChangeListener((PropertyChangeListener) getLabelTable());
        }

        addPropertyChangeListener(table);

        return table;
    }

}
