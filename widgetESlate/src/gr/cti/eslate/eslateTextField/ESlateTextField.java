package gr.cti.eslate.eslateTextField;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import gr.cti.eslate.utils.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;
import java.io.*;
import java.beans.PropertyChangeEvent;
import gr.cti.eslate.base.container.PerformanceManager;
import gr.cti.eslate.base.container.PerformanceTimerGroup;
import gr.cti.eslate.base.container.PerformanceTimer;
import gr.cti.eslate.base.container.event.PerformanceAdapter;
import gr.cti.eslate.base.container.event.PerformanceListener;


public class ESlateTextField extends JTextField implements ESlatePart, Externalizable {

    private StringSO stringSO;
    private NumberSO numberSO;
    private ESlateHandle handle;
    private SharedObjectPlug pin, pin2;
    private static final int FORMAT_VERSION = 1;
    public static final String SIGN = "+-";
    boolean fireOnEnterPress, plugsCreated = false;
    boolean dontAllowRemoveToHappen = false;
    private String validChars;
    SharedObjectPlug plug, plug2;
    public static final String DECIMAL_DIGITS = "0123456789";
    public static final String OCTAL_DIGITS = "01234567";
    public static final String HEXADECIMAL_DIGITS = "ABCDEF0123456789";
    public static final String FLOAT_DIGITS = "0123456789.,";
    public static final String INTEGER_NUMBER = "0123456789+-";
    public static final String FLOAT_NUMBER = "0123456789,.+-";
    static final long serialVersionUID = -6089627477738879986L;
    //public static final String FLOAT_DIGITS=DECIMAL_DIGITS+",";
    //public static final String FLOAT_NUMBER=FLOAT_DIGITS+SIGN;
    int maxDigits = 100;
    boolean numberMode;
    boolean alreadyChanged = false;
    String text = "";
    private int dot;
    private int mark;

    private static ResourceBundle bundleMessages;
    private final static String version = "2.0.6";
    NumberFormat numberFormatter;

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
     * Constructs a new ESlateTextField
     */

    public ESlateTextField() {
        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.eslateTextField.BundleMessages", Locale.getDefault());
        attachTimers();
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.constructionStarted(this);
        pm.init(constructorTimer);
        setOpaque(true);
        numberFormatter = NumberFormat.getInstance(Locale.getDefault());
        if (numberFormatter instanceof DecimalFormat) {
            ((DecimalFormat) numberFormatter).setDecimalSeparatorAlwaysShown(false);
        }

        //numberFormatter.applyPattern("00.0;-00.0");
        //numberFormatter.setDecimalSeparatorAlwaysShown(false);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        char fraction = formatSymbols.getDecimalSeparator();
        String FLOAT_DIGITS = DECIMAL_DIGITS + fraction;
        String FLOAT_NUMBER = FLOAT_DIGITS + SIGN;

        validChars = FLOAT_NUMBER;
        //Localize

        setPreferredSize(new Dimension(100, 30));

        addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    //Get the location in the text.
                    dot = e.getDot();
                    mark = e.getMark();
                }
            }
        );
        pm.stop(constructorTimer);
        pm.constructionEnded(this);
        pm.displayTime(constructorTimer, "", "ms");

    }

    /**
     * Returns the component's handleCopyright information.
     * @return handle	The ESlateHandle
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            PerformanceManager pm = PerformanceManager.getPerformanceManager();

            pm.eSlateAspectInitStarted(this);
            pm.init(initESlateAspectTimer);

            handle = ESlate.registerPart(this);
            try {
                handle.setUniqueComponentName(bundleMessages.getString("ESlateTextField"));
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
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.TextFieldPrimitives");
            handle.setInfo(getInfo());

            getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        if (stringSO != null && isEnabled() == true && fireOnEnterPress == false) {
                            stringSO.setString(getText());
                            if (numberMode == true) {
                                Number num = numberFormatter.parse(getText(), new ParsePosition(0));

                                numberSO.setValue(num);
                            }
                        }
                    }

                    public void insertUpdate(DocumentEvent e) {
                        if (stringSO != null && isEnabled() == true && fireOnEnterPress == false) {
                            stringSO.setString(getText());
                            if (numberMode == true) {
                                Number num = numberFormatter.parse(getText(), new ParsePosition(0));

                                numberSO.setValue(num);
                            }
                            alreadyChanged = true;
                            text = getText();
                        }

                    }

                    public void removeUpdate(DocumentEvent e) {
                        if (!dontAllowRemoveToHappen) {
                            if (stringSO != null && isEnabled() == true && fireOnEnterPress == false && (!alreadyChanged || (alreadyChanged && text.equals(getText())))) {
                                stringSO.setString(getText());
                                if (numberMode == true) {
                                    try {
                                        Number num = numberFormatter.parse(getText(), new ParsePosition(0));
                                        numberSO.setValue(num);
                                    } catch (NumberFormatException excc) {
                                        numberSO.setValue(0.0);
                                        setText("0");
                                    };
                                }
                            } else
                                alreadyChanged = false;

                        }
                    }
                }
            );

            // When handle is created, there is no need for plugs to be created too. If the component is created by
            // a user, then the user is responsible to use the right property method (or editor) to create plus. Plug state is
            // stored and retrieved with the component's state.

            setPlugsUsed(true);

            addKeyListener(new KeyListener() {
                    public void keyReleased(KeyEvent e) {
                        if (stringSO != null && e.getKeyCode() == KeyEvent.VK_ENTER && fireOnEnterPress == true && isEnabled() == true) {
                            stringSO.setString(getText());
                            if (numberMode == true) {
                                Number num = numberFormatter.parse(getText(), new ParsePosition(0));

                                numberSO.setValue(num);
                            }
                        }
                        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && isEnabled() == true && getText().length() == 0 && numberMode == true) {
                            /*When someone wants to enter its own number, allow
                             *to erase ALL digits...including zero
                             */
                            //setText("0");
                            if (numberSO != null)
                                numberSO.setValue(0.0);
                        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && isEnabled() == true && getText().length() == 1) {
                            dontAllowRemoveToHappen = true;
                        } else
                            dontAllowRemoveToHappen = false;
                        if (stringSO != null && e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                            if (stringSO!=null)
                                stringSO.setString(getText());
                            if (numberMode) {
                                Number num = numberFormatter.parse(getText(), new ParsePosition(0));
                                if (numberSO!=null)
                                    numberSO.setValue(num);
                            }
                        }

                    }

                    public void keyPressed(KeyEvent e) {}

                    public void keyTyped(KeyEvent e) {}
                }
            );

            addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent e) {
                        if (stringSO != null){
                            stringSO.setString(getText());
                            if (numberMode == true) {
                                Number num = numberFormatter.parse(getText(), new ParsePosition(0));
                                numberSO.setValue(num);
                            }
                        }
                    }

                    public void focusLost(FocusEvent e) {
                        if(stringSO != null){
                            stringSO.setString(getText());
                            if (numberMode == true) {
                                Number num = numberFormatter.parse(getText(), new ParsePosition(0));
                                numberSO.setValue(num);
                            }
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
     * Sets the field's text
     * @param s The text
     */

    public void setText(String s) {
        if (isEnabled() == true) {
            if (this.numberMode){
                try{
                    Number n = numberFormatter.parse(s);
                    setValue(n);
                }catch(ParseException exc){
                    setValue(new Double(0.0));
                }
            }else
                super.setText(s);

        }
    }

    /**
     * Sets the field's numeric value (in number mode)
     * @param num The numeric value
     */

    public void setValue(Number num) {
        if (isEnabled() == true) {
            if (num != null)
                super.setText(numberFormatter.format(num.doubleValue()));
            else
                super.setText(null);
        }
    }

    public Number getValue(){
        if (numberMode){
            try{
                return numberFormatter.parse(getText());
            }catch (ParseException exc){
                return null;
            }
        }
        return null;
    }

    protected void processComponentKeyEvent(KeyEvent e) {
        char c = e.getKeyChar();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        char ds=dfs.getDecimalSeparator();
        int iof= getText().indexOf(ds);
        try {
            if (numberMode == true &&
                !Character.isISOControl(c) && //needed to catch CR, ESC, BACKSPACE, DEL etc.
                !e.isActionKey() && //17May2000: had to add this in order for arrow keys to not get consumed
                !inString(validChars, c) &&
                getText() != null
            )   e.consume();
            else if(iof!=-1 && e.getKeyChar()==ds)
                e.consume();
            else super.processComponentKeyEvent(e);
        } catch (NumberFormatException exc) {
            e.consume();
        }
    }

    private boolean inString(String s, char c) {

        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == c)
                return true;
        return false;
    }

    /**
     * Sets the field's numeric mode on or off
     * @param mode The numeric mode (true = numeric mode, false = normal mode)
     */

    public void setNumberMode(boolean mode) {
        if (mode == true) {
            numberMode = true;
            if (pin != null){
                pin.setVisible(true);
                pin2.disconnect();
                pin2.setVisible(false);
            }
            setHorizontalAlignment(RIGHT);
            setText("0");
        } else if (pin != null) {
            if (pin.hasProvidersConnected() == true || pin.hasDependentsConnected() == true) {
                int returnValue = JOptionPane.showConfirmDialog(null, bundleMessages.getString("ProviderCase"),
                        bundleMessages.getString("PlugDisconnection"), JOptionPane.YES_NO_OPTION);

                if (returnValue == JOptionPane.YES_OPTION) {
                    pin.disconnect();
                    pin.setVisible(false);
                    setHorizontalAlignment(LEFT);
                    numberMode = false;
                } else {
                    numberMode = true;
                }
            } else {
                pin.setVisible(false);
                pin2.setVisible(true);
                setHorizontalAlignment(LEFT);
                numberMode = false;
            }
        }
    }

    /**
     * Returns whether the field is on numeric mode
     */

    public boolean getNumberMode() {
        return numberMode;
    }

    /**

     * Reads from ESlateTextField to restore stored values and properties
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
        setNumberMode(fieldMap.get("NumberMode", getNumberMode()));

        setAlignmentX(fieldMap.get("AlignmentX", getAlignmentX()));
        setAlignmentY(fieldMap.get("AlignmentY", getAlignmentY()));

        if (fieldMap.containsKey("Font"))
            setFont((Font) fieldMap.get("Font", getFont()));

        setEnabled(fieldMap.get("Enabled", isEnabled()));
        setEditable(fieldMap.get("Editable", isEditable()));
        setDoubleBuffered(fieldMap.get("DoubleBuffered", isDoubleBuffered()));
        setOpaque(fieldMap.get("Opaque", isOpaque()));

        setDebugGraphicsOptions(fieldMap.get("DebugGraphicsOptions", getDebugGraphicsOptions()));
        setColumns(fieldMap.get("Columns", getColumns()));

        setText(fieldMap.get("Text", getText()));
        setSelectionStart(fieldMap.get("SelectionStart", getSelectionStart()));
        setSelectionEnd(fieldMap.get("SelectionEnd", getSelectionEnd()));
        setCaretPosition(fieldMap.get("CaretPosition", getCaretPosition()));
        setName(fieldMap.get("Name", getName()));
        setToolTipText(fieldMap.get("ToolTipText", getToolTipText()));
        setHorizontalAlignment(fieldMap.get("HorizontalAlignment", getHorizontalAlignment()));

        if (fieldMap.containsKey("Background"))
            setBackground(fieldMap.get("Background", getBackground()));
        if (fieldMap.containsKey("Foreground"))
            setForeground(fieldMap.get("Foreground", getForeground()));

        setCaretColor(fieldMap.get("CaretColor", getCaretColor()));
        setDisabledTextColor(fieldMap.get("DisabledTextColor", getDisabledTextColor()));
        setSelectedTextColor(fieldMap.get("SelectedTextColor", getSelectedTextColor()));
        setSelectionColor(fieldMap.get("SelectionColor", getSelectionColor()));

        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MinimumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("PreferredSize", getPreferredSize()));
        setFireOnEnterPress(fieldMap.get("FireOnEnterPress", getFireOnEnterPress()));
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
     * Writes to ESlateTextField to store values and properties
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {
        PerformanceManager pm = PerformanceManager.getPerformanceManager();

        pm.init(saveTimer);
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION,7);

        fieldMap.put("NumberMode", getNumberMode());
        fieldMap.put("AlignmentX", getAlignmentX());
        fieldMap.put("AlignmentY", getAlignmentY());

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", getFont());

        fieldMap.put("Enabled", isEnabled());
        fieldMap.put("Editable", isEditable());
        fieldMap.put("DoubleBuffered", isDoubleBuffered());
        fieldMap.put("Opaque", isOpaque());

        fieldMap.put("DebugGraphicsOptions", getDebugGraphicsOptions());
        fieldMap.put("Columns", getColumns());

        fieldMap.put("Text", getText());
        fieldMap.put("SelectionStart", getSelectionStart());
        fieldMap.put("SelectionEnd", getSelectionEnd());
        fieldMap.put("CaretPosition", getCaretPosition());
        fieldMap.put("Name", getName());
        fieldMap.put("HorizontalAlignment", getHorizontalAlignment());
        fieldMap.put("ToolTipText", getToolTipText());

        if (!(getBackground() instanceof UIResource))
            fieldMap.put("Background", getBackground());
        if (!(getForeground() instanceof UIResource))
            fieldMap.put("Foreground", getForeground());

        fieldMap.put("CaretColor", getCaretColor());
        fieldMap.put("DisabledTextColor", getDisabledTextColor());
        fieldMap.put("SelectedTextColor", getSelectedTextColor());
        fieldMap.put("SelectionColor", getSelectionColor());

        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MinimumSize", getMinimumSize());
        fieldMap.put("PreferredSize", getPreferredSize());
        fieldMap.put("FireOnEnterPress", getFireOnEnterPress());
        fieldMap.put("PlugsUsed", getPlugsUsed());

        if (getBorder() != null && !(getBorder() instanceof UIResource) ) {
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

    public int[] getSelectionPoints() {
        if (dot == mark) {  // no selection
            int[] array = {dot, mark};

            return array;
        } else if (dot < mark) {
            int[] array = {dot, mark};

            return array;
        } else {
            int[] array = {mark, dot};

            return array;
        }
    }

    /**
     * Determines whether text should be distributed to other components on ENTER press
     */

    public void setFireOnEnterPress(boolean fireOnEnter) {
        if (fireOnEnterPress == fireOnEnter) return;
        fireOnEnterPress = fireOnEnter;
    }

    /**
     * Returns whether text is distributed to other components on ENTER press or not
     */

    public boolean getFireOnEnterPress() {
        return fireOnEnterPress;
    }

    /**
     * Sets the string that represents the valid characters on number mode
     */

    public void setValidChars(String s) {
        if (validChars == s) return;
        validChars = s;
    }

    /**
     * Sets the maximum number of digits (on Number mode)
     */

    public void setMaxDigits(int max) {
        if (maxDigits == max) return;
        maxDigits = max;
    }

    private void createPlugs() {
        if (handle == null)
            return;
            stringSO = new StringSO(handle);
            numberSO = new NumberSO(handle, 0);
            numberSO.setNumberFormat(numberFormatter);

        try {
            SharedObjectListener sol = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        if (!getText().equals(((StringSO) e.getSharedObject()).getString()))
                            setText(((StringSO) e.getSharedObject()).getString());
                    }
                };

            plug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Title", new Color(139, 117, 0),
                        gr.cti.eslate.sharedObject.StringSO.class,
                        stringSO, sol);
            pin2 = plug;
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
            SharedObjectListener so2 = new SharedObjectListener() {
                    public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                        setValue(((NumberSO) e.getSharedObject()).value());
                    }
                };

            plug2 = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "NumberValue", new Color(135, 206, 250),
                        gr.cti.eslate.sharedObject.NumberSO.class,
                        numberSO, so2);
            pin = plug2;
            plug2.addConnectionListener(new ConnectionListener() {
                    public void handleConnectionEvent(ConnectionEvent e) {
                        if (e.getType() == Plug.INPUT_CONNECTION) {
                            NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();

                            setValue(so.value());
                        }
                    }
                }
            );
            handle.addPlug(plug2);
            if (numberMode == false)
                plug2.setVisible(false);
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

    private void destroyPlugs() {
        try {
            if (plug2 != null)
                handle.removePlug(plug2);
            if (plug != null)
                handle.removePlug(plug);
        } catch (Exception exc) {
            System.out.println("Plug to be removed not found");
            exc.printStackTrace();
        }
        plug2 = null;
        plug = null;
        stringSO = null;
        numberSO = null;
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
