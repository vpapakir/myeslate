package gr.cti.eslate.spinButton;


import gr.cti.eslate.base.ConnectionEvent;
import gr.cti.eslate.base.ConnectionListener;
import gr.cti.eslate.base.ESlate;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateInfo;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.InvalidPlugParametersException;
import gr.cti.eslate.base.MultipleInputMultipleOutputPlug;
import gr.cti.eslate.base.MultipleOutputPlug;
import gr.cti.eslate.base.Plug;
import gr.cti.eslate.base.PlugExistsException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.SharedObjectPlug;
import gr.cti.eslate.base.sharedObject.SharedObjectEvent;
import gr.cti.eslate.base.sharedObject.SharedObjectListener;
import gr.cti.eslate.eslateTextField.ESlateTextField;
import gr.cti.eslate.sharedObject.DateSO;
import gr.cti.eslate.sharedObject.NumberSO;
import gr.cti.eslate.sharedObject.StringSO;
import gr.cti.eslate.utils.ESlateFieldMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.MathEF;


public class SpinButton extends JPanel implements ESlatePart, Externalizable {
    private int cursorPos;
    private static ResourceBundle bundleMessages;
    public static final int DATE_DATA_MODEL = 0;
    public static final int TIME_DATA_MODEL = 1;
    public static final int NUMBER_DATA_MODEL = 2;
    public static final int ENUMERATED_DATA_MODEL = 3;
    public DataModelInterface model;// = new TimeDataModel();
    double step = 1;
    ValueChangedEventMulticaster valueChangedListener = new ValueChangedEventMulticaster();
    private ExplicitLayout el = new ExplicitLayout();
    private ArrowButton btnUp; //IncButton btnUp;
    private ArrowButton btnDown; //IncButton btnDown;
    private ESlateTextField field;
    private String STR_FORMAT_VERSION = "1.0.0";
    public int modelType = NUMBER_DATA_MODEL;
    private ESlateHandle handle = null;
    static final long serialVersionUID = -22951453L;
    private final static String version = "3.0.4";
    private SharedObjectPlug textPlug;
    private SharedObjectPlug datePlug;
    private SharedObjectPlug timePlug;
    private SharedObjectPlug numberPlug;

    private StringSO stringSO;
    private DateSO dateSO;
    private DateSO timeSO;
    private NumberSO numberSO;
    boolean continuousEvents = false;
    SpinModelDataEvent cachedEvent = null;

    int tempChange = 0;

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
     * Constructs a new SpinButton using a Numeric Data model.
     *
     */
    public SpinButton() {
        super();
        setOpaque(true);
        //        setBorder(new NoTopOneLineBevelBorder(0));

        field = new ESlateTextField() {
                    public void updateUI() {
                        super.updateUI();
                        if (SpinButton.this != null) {
                            Border b = getBorder();

                            setBorder(null);
                            SpinButton.this.setBorder(b);
                        }
                    }
                };
        Border b = field.getBorder();

        field.setBorder(null);
        setBorder(b);
        field.setValidChars(ESlateTextField.DECIMAL_DIGITS);
        field.setHorizontalAlignment(SwingConstants.RIGHT);
        field.setMaxDigits(4);

        Insets buttonInsets = new Insets(0, 1, 0, 1);

        btnUp = new ArrowButton(SwingConstants.NORTH);// {
        btnUp.setMargin(buttonInsets);

        btnDown = new ArrowButton(SwingConstants.SOUTH); //IncButton();
        btnDown.setMargin(buttonInsets);

        setLayout(el);
        ExplicitConstraints ec1 = new ExplicitConstraints(field);

        ec1.setX(ContainerEF.left(this));
        ec1.setY(ContainerEF.top(this));
        ec1.setWidth(ContainerEF.width(this).subtract(ComponentEF.width(btnUp)));
        ec1.setHeight(ContainerEF.height(this));
        add(field, ec1);

        ExplicitConstraints ec2 = new ExplicitConstraints(btnUp);

        ec2.setOriginX(ExplicitConstraints.RIGHT);
        ec2.setX(ContainerEF.right(this));
        ec2.setY(ContainerEF.top(this));
        ec2.setHeight(ContainerEF.height(this).divide(2));
        Expression width = MathEF.bound(MathEF.constant(16), ContainerEF.widthFraction(this,0.1), MathEF.constant(60));

        ec2.setWidth(width);
        add(btnUp, ec2);

        ExplicitConstraints ec3 = new ExplicitConstraints(btnDown);

        ec3.setOriginX(ExplicitConstraints.RIGHT);
        ec3.setX(ContainerEF.right(this));
        ec3.setOriginY(ExplicitConstraints.BOTTOM);
        ec3.setY(ContainerEF.bottom(this));
        ec3.setWidth(ComponentEF.width(btnUp));
        ec3.setHeight(ContainerEF.height(this).subtract(ComponentEF.height(btnUp)));
        add(btnDown, ec3);

        Dimension d1 = field.getPreferredSize();
        Dimension d2 = btnUp.getPreferredSize();

        setPreferredSize(new Dimension(d1.width + d2.width, d1.height - 7));

        bundleMessages = ResourceBundle.getBundle("gr.cti.eslate.spinButton.SpinButtonBundleMessages", Locale.getDefault());

        setModelType(modelType);

        field.addFocusListener(new FocusListener() {
                public void focusLost(FocusEvent e) {
					setValue(field.getText());
/*                    try {
                        model.setValue(field.getText());
                    } catch (Exception exc) {
						field.setText(model.getStringValue());
//                        field.setForeground(Color.red);
                    }
*/
                }

                public void focusGained(FocusEvent e) {}
            }
        );

        field.setFireOnEnterPress(true);

        field.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    cursorPos = field.getCaretPosition();
                }
            }
        );

        field.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						setValue(field.getText());
/*                        try {
                            model.setValue(field.getText());
                        } catch (Exception exc) {
							field.setText(model.getStringValue());
//                            field.setForeground(Color.red);
                        }
*/
                    }
                }

                public void keyReleased(KeyEvent e) {}

                public void keyTyped(KeyEvent e) {}
            }
        );

        /*        model.addSpinModelDataListener(new SpinModelDataListener(){
         public void spinModelDataChanged(SpinModelDataEvent e){
         if (modelType==ENUMERATED_DATA_MODEL){
         if (model.getValue()!=null)
         field.setText(model.getStringValue());
         }else
         field.setText(model.getStringValue());
         field.setForeground(Color.black);
         if (valueChangedListener !=null ){
         ValueChangedEvent en = new ValueChangedEvent(this,e.getID(), e.getValue(), e.getPreviousValue());
         valueChangedListener.valueChanged(en);
         }
         }
         });
         */
        ///////////////KEY ACTIONS FOR UP KEY\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


        registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    field.grabFocus();
                    javax.swing.ButtonModel bm = btnUp.getModel();

                    bm.setArmed(true);
                    bm.setPressed(true);
                    if (modelType == DATE_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (((DateDataModel) model).getFormat().startsWith("MM")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(1);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(0);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        } else if (((DateDataModel) model).getFormat().startsWith("dd")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(0);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(1);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        }
                    } else if (modelType == TIME_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (cursorPos <= field.getText().indexOf(":"))
                            ((TimeDataModel) model).setTimeRate(2);
                        else if ((cursorPos <= field.getText().lastIndexOf(":")) && (cursorPos > field.getText().indexOf(":")))
                            ((TimeDataModel) model).setTimeRate(1);
                        else  if (cursorPos > field.getText().lastIndexOf(":"))
                            if (field.getText().lastIndexOf(":") == field.getText().indexOf(":"))
                                ((TimeDataModel) model).setTimeRate(1);
                            else
                                ((TimeDataModel) model).setTimeRate(0);
                    }
                    continuousEvents = true;
                    model.increase(step);
                    if (modelType != SpinButton.NUMBER_DATA_MODEL)
                        field.setCaretPosition(cursorPos);
                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0, false), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    javax.swing.ButtonModel bm = btnUp.getModel();

                    bm.setPressed(false);
                    continuousEvents = false;
                    if (cachedEvent != null) {
                        fireValueChangedEvent(cachedEvent, true);
//                                          setValue(cachedEvent.getValue());
                    }
                    cachedEvent = null;

                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0, true), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ////////////////////////END OF KEY ACTIONS FOR UP KEY\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        ///////////////////////KEY ACTIONS FOR DOWN KEY\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    field.grabFocus();
                    javax.swing.ButtonModel bm = btnDown.getModel();

                    bm.setArmed(true);
                    bm.setPressed(true);
                    if (modelType == DATE_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (((DateDataModel) model).getFormat().startsWith("MM")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(1);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(0);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        } else if (((DateDataModel) model).getFormat().startsWith("dd")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(0);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(1);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        }
                    } else if (modelType == TIME_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (cursorPos <= field.getText().indexOf(":"))
                            ((TimeDataModel) model).setTimeRate(2);
                        else if ((cursorPos <= field.getText().lastIndexOf(":")) && (cursorPos > field.getText().indexOf(":")))
                            ((TimeDataModel) model).setTimeRate(1);
                        else  if (cursorPos > field.getText().lastIndexOf(":"))
                            if (field.getText().lastIndexOf(":") == field.getText().indexOf(":"))
                                ((TimeDataModel) model).setTimeRate(1);
                            else
                                ((TimeDataModel) model).setTimeRate(0);
                    }
                    continuousEvents = true;
                    model.decrease(step);
                    if (modelType != SpinButton.NUMBER_DATA_MODEL)
                        field.setCaretPosition(cursorPos);
                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0, false), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        registerKeyboardAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    javax.swing.ButtonModel bm = btnDown.getModel();

                    bm.setPressed(false);
                    continuousEvents = false;
                    if (cachedEvent != null) {
                        fireValueChangedEvent(cachedEvent, true);
//                                          setValue(cachedEvent.getValue());
                    }
                    cachedEvent = null;

                }
            }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0, true), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ////////////////////////////////END OF KEY ACTIONS FOR DOWN KEY\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        btnUp.addMouseListener(new MouseAdapter() {
                SpinUpThread it;
                public void mousePressed(MouseEvent e) {
                    if (modelType == DATE_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (((DateDataModel) model).getFormat().startsWith("MM")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(1);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(0);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        } else if (((DateDataModel) model).getFormat().startsWith("dd")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(0);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(1);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        }
                    } else if (modelType == TIME_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (cursorPos <= field.getText().indexOf(":"))
                            ((TimeDataModel) model).setTimeRate(2);
                        else if ((cursorPos <= field.getText().lastIndexOf(":")) && (cursorPos > field.getText().indexOf(":")))
                            ((TimeDataModel) model).setTimeRate(1);
                        else  if (cursorPos > field.getText().lastIndexOf(":"))
                            if (field.getText().lastIndexOf(":") == field.getText().indexOf(":"))
                                ((TimeDataModel) model).setTimeRate(1);
                            else
                                ((TimeDataModel) model).setTimeRate(0);
                    }
                    continuousEvents = true;
                    model.increase(step);
                    it = new SpinUpThread();
                    it.start();
                    if (modelType != SpinButton.NUMBER_DATA_MODEL)
                        field.setCaretPosition(cursorPos);
                }

                public void mouseReleased(MouseEvent e) {
                    if (it != null) {
                        it.stopped = true;
                        it = null;
                    }
                    continuousEvents = false;
                    if (cachedEvent != null) {
                        fireValueChangedEvent(cachedEvent, true);
//                                          setValue(cachedEvent.getValue());
                                          
                    }
                    cachedEvent = null;
                }
            }
        );

        btnDown.addMouseListener(new MouseAdapter() {
                SpinDownThread it;
                public void mousePressed(MouseEvent e) {
                    if (modelType == DATE_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (((DateDataModel) model).getFormat().startsWith("MM")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(1);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(0);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        } else if (((DateDataModel) model).getFormat().startsWith("dd")) {
                            if (cursorPos <= 2)
                                ((DateDataModel) model).setDateRate(0);
                            else if (cursorPos <= 6 && cursorPos > 2)
                                ((DateDataModel) model).setDateRate(1);
                            else  if (cursorPos > 6)
                                ((DateDataModel) model).setDateRate(2);
                        }
                    } else if (modelType == TIME_DATA_MODEL) {
                        cursorPos = field.getCaretPosition();
                        if (cursorPos <= field.getText().indexOf(":"))
                            ((TimeDataModel) model).setTimeRate(2);
                        else if ((cursorPos <= field.getText().lastIndexOf(":")) && (cursorPos > field.getText().indexOf(":")))
                            ((TimeDataModel) model).setTimeRate(1);
                        else  if (cursorPos > field.getText().lastIndexOf(":"))
                            if (field.getText().lastIndexOf(":") == field.getText().indexOf(":"))
                                ((TimeDataModel) model).setTimeRate(1);
                            else
                                ((TimeDataModel) model).setTimeRate(0);
                    }
                    continuousEvents = true;
                    model.decrease(step);
                    it = new SpinDownThread();
                    it.start();
                    if (modelType != SpinButton.NUMBER_DATA_MODEL)
                        field.setCaretPosition(cursorPos);
                }

                public void mouseReleased(MouseEvent e) {
                    if (it != null) {
                        it.stopped = true;
                        it = null;
                    }
                    continuousEvents = false;

                    if (cachedEvent != null) {
                        fireValueChangedEvent(cachedEvent, true);
//                                          setValue(cachedEvent.getValue());
                    }
                    cachedEvent = null;
                }
            }
        );
    }

    /**
     * Construct a spin button with a Numeric Data model and with the default up and down limits and <em>init</em> initial value.
     */
    public SpinButton(int init) {
        this();
        setModelType(SpinButton.NUMBER_DATA_MODEL);
        setValue(new Integer(init));
        //        field.setText(""+init);
    }

    /**
     * Construct a spin button with a Numeric Data model and with <em>upLimit</em> up limit, <em>downLimit</em> down limit and <em>init</em> initial value.
     */
    public SpinButton(int init, int downLimit, int upLimit) {
        this();
        setModelType(SpinButton.NUMBER_DATA_MODEL);
        setValue(new Integer(init));
        //        field.setText(""+init);
        ((NumberDataModel) model).setMinimumValue(new Double(downLimit));
        ((NumberDataModel) model).setMaximumValue(new Double(upLimit));
    }

    /**
     * Returns the ESlate handle (and constructs plugs and registering Logo primitives).
     */

    public ESlateHandle getESlateHandle() {
        if (handle == null) {
            handle = ESlate.registerPart(this);
            stringSO = new StringSO(handle);
            dateSO = new DateSO(handle);
            timeSO = new DateSO(handle);
            numberSO = new NumberSO(handle, 0.0);
            handle.setInfo(getInfo());
            try {
                handle.setUniqueComponentName(bundleMessages.getString("SpinButton"));
            } catch (RenamingForbiddenException e) {
                e.printStackTrace();
            }
            handle.addPrimitiveGroup("gr.cti.eslate.scripting.logo.SpinButtonPrimitives");

            ///////////////////////////PLUGS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            try {

                textPlug = new MultipleOutputPlug(handle, bundleMessages, "Text", new Color(139, 117, 0),
                            gr.cti.eslate.sharedObject.StringSO.class,
                            stringSO);

                handle.addPlug(textPlug);
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

            try {
                SharedObjectListener sol2 = new SharedObjectListener() {
                        public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                            setValue(((DateSO) e.getSharedObject()).getDate());
                        }
                    };

                datePlug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Date", new Color(34, 139, 134),
                            gr.cti.eslate.sharedObject.DateSO.class,
                            dateSO, sol2);
                datePlug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                             if (e.getType() == Plug.INPUT_CONNECTION) {
                                DateSO so = (DateSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                setValue(so.getDate());
                            }
                        }
                    }
                );
                handle.addPlug(datePlug);
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

            try {
                SharedObjectListener sol3 = new SharedObjectListener() {
                        public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                            setValue(((DateSO) e.getSharedObject()).getDate());
                        }
                    };

                timePlug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Time", new Color(34, 139, 134),
                            gr.cti.eslate.sharedObject.DateSO.class,
                            timeSO, sol3);

                timePlug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                DateSO so = (DateSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                setValue(so.getDate());
                            }
                        }
                    }
                );
                handle.addPlug(timePlug);
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}

            try {
                SharedObjectListener sol4 = new SharedObjectListener() {
                        public synchronized void handleSharedObjectEvent(SharedObjectEvent e) {
                            setValue(((NumberSO) e.getSharedObject()).value());
                        }
                    };

                numberPlug = new MultipleInputMultipleOutputPlug(handle, bundleMessages, "Number", new Color(135, 206, 250),
                            gr.cti.eslate.sharedObject.NumberSO.class,
                            numberSO, sol4);

                numberPlug.addConnectionListener(new ConnectionListener() {
                        public void handleConnectionEvent(ConnectionEvent e) {
                            if (e.getType() == Plug.INPUT_CONNECTION) {
                                NumberSO so = (NumberSO) ((SharedObjectPlug) e.getPlug()).getSharedObject();
                                setValue(so.value());
                            }
                        }
                    }
                );
                handle.addPlug(numberPlug);
            } catch (InvalidPlugParametersException e) {} catch (PlugExistsException e) {}
            //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\///////////////////////////////////////////////////////////////

            adjustPlugVisibility();
        }
        return handle;
    }

    /**
     * Increases the spin value by step.
     */
    public void increase() {
        model.increase(new Double(step).intValue());
        ///      field.setText(model.getStringValue());
    }

    /**
     * Decreases the spin value by step.
     */
    public void decrease() {
        model.decrease(new Double(step).intValue());
        ///      field.setText(model.getStringValue());
    }

    /**
     * Sets the spinButton's data model type.
     * @param s The model type
     */

    public void setModelType(int s) {
        if (modelType == s && model != null) return;
        //      System.out.println("setModelType called");

        /* When changing model, any connected plugs (except the text plug, which is present
         * in every model) must be disconnected, cause the model no longer exists.
         */
        int prevModelType = modelType;

        if (prevModelType == DATE_DATA_MODEL && datePlug != null)
            datePlug.disconnect();
        else if (prevModelType == TIME_DATA_MODEL && timePlug != null)
            timePlug.disconnect();
        else if (prevModelType == NUMBER_DATA_MODEL && numberPlug != null)
            numberPlug.disconnect();

        modelType = s;
        if (s == TIME_DATA_MODEL) {
            model = new TimeDataModel();
            field.setEditable(true);
        } else if (s == DATE_DATA_MODEL) {
            model = new DateDataModel();
            field.setEditable(true);
        } else if (s == NUMBER_DATA_MODEL) {
            model = new NumberDataModel();
            field.setEditable(true);
        } else if (s == ENUMERATED_DATA_MODEL) {
            model = new EnumeratedDataModel();
            field.setEditable(false);
        }
        adjustPlugVisibility();

        model.addSpinModelDataListener(new SpinModelDataListener() {
                public void spinModelDataChanged(SpinModelDataEvent e) {

                    /* If this is an event for the change of the mimimum or maximum value of the
                     * model, then simply forward it.
                     */
                    if (e.getID() != SpinModelDataEvent.VALUE_CHANGED) {
                        fireValueChangedEvent(e, true);
                        return;
                    }

                    if (modelType == ENUMERATED_DATA_MODEL) {
                        if (model.getValue() != null)
                            field.setText(model.getStringValue());
                    } else
                        field.setText(model.getStringValue());
                    
                    if (stringSO!=null)
                        stringSO.setString(model.getStringValue());

                    field.setForeground(Color.black);
                    if (continuousEvents) {
                        if (cachedEvent != null) {
                            fireValueChangedEvent(cachedEvent, false);
                        }
                        cachedEvent = e;
                    } else {
                        fireValueChangedEvent(e, true);
                    }
                }
            }
        );

        field.setText(model.getStringValue());
    }

    private void adjustPlugVisibility() {
        if (handle == null) return;
        if (modelType == TIME_DATA_MODEL) {
            datePlug.setVisible(false);
            timePlug.setVisible(true);
            numberPlug.setVisible(false);
        } else if (modelType == DATE_DATA_MODEL) {
            datePlug.setVisible(true);
            timePlug.setVisible(false);
            numberPlug.setVisible(false);
        } else if (modelType == NUMBER_DATA_MODEL) {
            datePlug.setVisible(false);
            timePlug.setVisible(false);
            numberPlug.setVisible(true);
        } else if (modelType == ENUMERATED_DATA_MODEL) {
            datePlug.setVisible(false);
            timePlug.setVisible(false);
            numberPlug.setVisible(false);
        }
    }

    /**
     * Returns the Data model type.
     * @return	Data model type.
     */

    public int getModelType() {
        return modelType;
    }

    /**
     * Returns the Data model.
     * @return	The Data model.
     */


    public DataModelInterface getModel() {
        return model;
    }

    /**
     * Sets the spinButton's Data model.
     * @param	model The Data model.
     */

    public void setModel(DataModelInterface model) {
        this.model = model;
    }

    /**
     * Sets the spinButton's increasing/decreasing step.
     * @param step The step.
     */

    public void setStep(double step) {
        this.step = step;
    }

    /**
     * Returns the spinButton's increasing/decreasing step.
     * @return	The step.
     */

    public double getStep() {
        return step;
    }

    /**
     * Returns the spinButton's value.
     * @return	the value.
     */

    public Object getValue() {
        return model.getValue();
    }

    /**
     * Sets the spinButton's value.
     * @param value The value.
     */

    public void setValue(Object value) {
        try {
			model.setValue(value);

/*            if (modelType == DATE_DATA_MODEL) {
                model.setValue(value);
                //            if (handle != null)
                //               dateSO.setDate((Date) value);
            } else if (modelType == TIME_DATA_MODEL) {
                model.setValue(value);
                //            if (handle != null)
                //               timeSO.setDate((Date) value);
            } else if (modelType == NUMBER_DATA_MODEL) {
                model.setValue(value);
                //            if (handle != null)
                //               numberSO.setValue((Number) value);
            }
*/
        } catch (Exception exc) {
//System.out.println("Restoring currentValue " + exc.getMessage());
//exc.printStackTrace();
			field.setText(model.getStringValue());
//            exc.printStackTrace();
        }

    }

    private void fireSharedObjectEvent(Object value, boolean isLast) {
        //System.out.println("Firing SharedObjectEvent");
        if (handle == null) return;
        if (modelType == DATE_DATA_MODEL) {
            dateSO.setChanging(!isLast);
            dateSO.setDate((Date) value);
        } else if (modelType == TIME_DATA_MODEL) {
            timeSO.setChanging(!isLast);
            timeSO.setDate((Date) value);
        } else if (modelType == NUMBER_DATA_MODEL) {
            numberSO.setValue((Number) value);
        }
    }

    private void fireValueChangedEvent(SpinModelDataEvent e, boolean isLast) {
        //Thread.currentThread().dumpStack();
        if (valueChangedListener != null) {
            ValueChangedEvent event = new ValueChangedEvent(this, e.getID(), e.getValue(), e.getPreviousValue(), isLast);

            valueChangedListener.valueChanged(event);
        }
        if (e.getID() == ValueChangedEvent.VALUE_CHANGED)
            fireSharedObjectEvent(e.getValue(),isLast);
    }

    public void setMaximumValue(Object value) {
        model.setMaximumValue(value);
    }

    public void setMinimumValue(Object value) {
        model.setMinimumValue(value);
    }

    public Object getMaximumValue() {
        return model.getMaximumValue();
    }

    public Object getMinimumValue() {
        return model.getMinimumValue();
    }

    /**
     * Adds a valueChangeListener (a listener to say when the spinButton's value has changed).
     * @param listener The listener.
     */

    public void addValueChangedListener(ValueChangedListener listener) {
        valueChangedListener.add(listener);
    }

    /**
     * Removes a valueChangeListener (a listener to say when the spinButton's value has changed).
     * @param listener The listener.
     */

    public void removeValueChangedListener(ValueChangedListener listener) {
        valueChangedListener.remove(listener);
    }

    public void setEnabled(boolean enabled) {
        if (enabled == super.isEnabled()) return;
        super.setEnabled(enabled);
        btnUp.setEnabled(enabled);
        btnDown.setEnabled(enabled);
        field.setEnabled(enabled);
    }

    /////////////////AND NOW TO STORE AND RETRIEVE EVERYTHING.... \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Reads the spinButton's stored state and configuration from a microworld file  .
     * @param in The object input .
     *
     */



    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        Object firstObj = in.readObject();

        ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;

        //field.readExternal(in);
        if (fieldMap.containsKey("Font"))
            field.setFont((Font) fieldMap.get("Font", field.getFont()));
        setMaximumSize((Dimension) fieldMap.get("MaximumSize", getMaximumSize()));
        setMinimumSize((Dimension) fieldMap.get("MaximumSize", getMinimumSize()));
        setPreferredSize((Dimension) fieldMap.get("MaximumSize", getPreferredSize()));
        setModelType(fieldMap.get("Model", getModelType()));
        setStep(fieldMap.get("Step", getStep()));
        if (getModelType() == DATE_DATA_MODEL) {
            ((DateDataModel) model).setFormat(fieldMap.get("Format", ((DateDataModel) model).getFormat()));
            ((DateDataModel) model).setMaximumValue((Date) fieldMap.get("MaximumDate", ((DateDataModel) model).getMaximumValue()));
            ((DateDataModel) model).setMinimumValue((Date) fieldMap.get("MinimumDate", ((DateDataModel) model).getMinimumValue()));
            ((DateDataModel) model).setDateRate(fieldMap.get("DateRate", ((DateDataModel) model).getDateRate()));
        } else if (getModelType() == TIME_DATA_MODEL) {
            ((TimeDataModel) model).setFormat(fieldMap.get("Format", ((TimeDataModel) model).getFormat()));
            ((TimeDataModel) model).setMaximumValue((Date) fieldMap.get("MaximumTime", ((TimeDataModel) model).getMaximumValue()));
            ((TimeDataModel) model).setMinimumValue((Date) fieldMap.get("MinimumTime", ((TimeDataModel) model).getMinimumValue()));
            ((TimeDataModel) model).setTimeRate(fieldMap.get("TimeRate", ((TimeDataModel) model).getTimeRate()));
        } else if (getModelType() == NUMBER_DATA_MODEL) {
            ((NumberDataModel) model).setMaximumValue(fieldMap.get("MaximumNumber", ((NumberDataModel) model).getMaximumValue()));
            ((NumberDataModel) model).setMinimumValue(fieldMap.get("MinimumNumber", ((NumberDataModel) model).getMinimumValue()));
        } else if (getModelType() == ENUMERATED_DATA_MODEL)
            ((EnumeratedDataModel) model).setElements((Vector) fieldMap.get("ElementVector", ((EnumeratedDataModel) model).getElements()));
        setValue(fieldMap.get("Value"));

    }

    /**
     * Stores the spinButton's current state and configuration to a microworld file  .
     * @param out The object output .
     *
     */

    public void writeExternal(java.io.ObjectOutput out) throws IOException {

        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 7);

        //field.writeExternal(out);

        if (!(getFont() instanceof UIResource))
            fieldMap.put("Font", field.getFont());
        fieldMap.put("MaximumSize", getMaximumSize());
        fieldMap.put("MaximumSize", getMinimumSize());
        fieldMap.put("MaximumSize", getPreferredSize());
        fieldMap.put("Model", getModelType());
        fieldMap.put("Step", getStep());
        fieldMap.put("Value", getValue());
        if (getModelType() == DATE_DATA_MODEL) {
            fieldMap.put("Format", ((DateDataModel) model).getFormat());
            fieldMap.put("MaximumDate", ((DateDataModel) model).getMaximumValue());
            fieldMap.put("MinimumDate", ((DateDataModel) model).getMinimumValue());
            fieldMap.put("DateRate", ((DateDataModel) model).getDateRate());
        } else if (getModelType() == TIME_DATA_MODEL) {
            fieldMap.put("Format", ((TimeDataModel) model).getFormat());
            fieldMap.put("MaximumTime", ((TimeDataModel) model).getMaximumValue());
            fieldMap.put("MinimumTime", ((TimeDataModel) model).getMinimumValue());
            fieldMap.put("TimeRate", ((TimeDataModel) model).getTimeRate());
        } else if (getModelType() == NUMBER_DATA_MODEL) {
            fieldMap.put("MaximumNumber", ((NumberDataModel) model).getMaximumValue());
            fieldMap.put("MinimumNumber", ((NumberDataModel) model).getMinimumValue());
        } else if (getModelType() == ENUMERATED_DATA_MODEL)
            fieldMap.put("ElementVector", ((EnumeratedDataModel) model).getElements());

        out.writeObject(fieldMap);
    }

    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\////////////////////////////////////////////////////

    private class SpinUpThread extends Thread {
        protected boolean stopped = false;
        public void run() {
            try {
                sleep(500);
            } catch (InterruptedException e) {}

            while (!stopped) {
                if (modelType == DATE_DATA_MODEL && (((DateDataModel) model).dateFormat.toPattern().equals("dd/MM/yyyy") || ((DateDataModel) model).dateFormat.toPattern().equals("dd/MM/yy"))) {
                    cursorPos = field.getCaretPosition();
                    if (cursorPos <= field.getText().indexOf("/"))
                        ((DateDataModel) model).setDateRate(0);
                    else if ((cursorPos <= field.getText().lastIndexOf("/")) && (cursorPos > field.getText().indexOf("/")))
                        ((DateDataModel) model).setDateRate(1);
                    else  if (cursorPos > field.getText().lastIndexOf("/"))
                        ((DateDataModel) model).setDateRate(2);
                } else if (modelType == TIME_DATA_MODEL && (((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm:ss") || ((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm:ss aaa") || ((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm") || ((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm aaa"))) {
                    cursorPos = field.getCaretPosition();
                    if (cursorPos <= field.getText().indexOf(":"))
                        ((TimeDataModel) model).setTimeRate(2);
                    else if ((cursorPos <= field.getText().lastIndexOf(":")) && (cursorPos > field.getText().indexOf(":")))
                        ((TimeDataModel) model).setTimeRate(1);
                    else  if (cursorPos > field.getText().lastIndexOf(":"))
                        if (field.getText().lastIndexOf(":") == field.getText().indexOf(":"))
                            ((TimeDataModel) model).setTimeRate(1);
                        else
                            ((TimeDataModel) model).setTimeRate(0);

                }
                model.increase(step);
                try {
                    sleep(60);
                } catch (InterruptedException e) {}
            }
        }
    };


    private class SpinDownThread extends Thread {
        protected boolean stopped = false;
        public void run() {
            try {
                sleep(500);
            } catch (InterruptedException e) {}

            while (!stopped) {
                if (modelType == DATE_DATA_MODEL && (((DateDataModel) model).dateFormat.toPattern().equals("dd/MM/yyyy") || ((DateDataModel) model).dateFormat.toPattern().equals("dd/MM/yy"))) {
                    cursorPos = field.getCaretPosition();
                    if (cursorPos <= field.getText().indexOf("/"))
                        ((DateDataModel) model).setDateRate(0);
                    else if ((cursorPos <= field.getText().lastIndexOf("/")) && (cursorPos > field.getText().indexOf("/")))
                        ((DateDataModel) model).setDateRate(1);
                    else  if (cursorPos > field.getText().lastIndexOf("/"))
                        ((DateDataModel) model).setDateRate(2);

                } else if (modelType == TIME_DATA_MODEL && (((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm:ss") || ((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm:ss aaa") || ((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm") || ((TimeDataModel) model).timeFormat.toPattern().equals("HH:mm aaa"))) {
                    cursorPos = field.getCaretPosition();
                    if (cursorPos <= field.getText().indexOf(":"))
                        ((TimeDataModel) model).setTimeRate(2);
                    else if ((cursorPos <= field.getText().lastIndexOf(":")) && (cursorPos > field.getText().indexOf(":")))
                        ((TimeDataModel) model).setTimeRate(1);
                    else  if (cursorPos > field.getText().lastIndexOf(":"))
                        if (field.getText().lastIndexOf(":") == field.getText().indexOf(":"))
                            ((TimeDataModel) model).setTimeRate(1);
                        else
                            ((TimeDataModel) model).setTimeRate(0);
                }
                model.decrease(step);
                if (modelType != SpinButton.NUMBER_DATA_MODEL)
                    field.setCaretPosition(cursorPos);
                try {
                    sleep(60);
                } catch (InterruptedException e) {}
            }
        }
    };


    class ArrowButton extends JButton {
        int direction = SwingConstants.NORTH;

        public ArrowButton(int direction) {
            this.direction = direction;
            setRequestFocusEnabled(false);
            setFocusPainted(false);
        }

        public boolean isFocusTraversable() {
            return false;
        }

        public void paint(Graphics g) {
            super.paint(g);
            int w, h, size;

            w = getSize().width;
            h = getSize().height;
            size = Math.min((h - 4) / 3, (w - 4) / 3);
            size = Math.max(size, 2);
            paintTriangle(g, (w - size) / 2, (h - size) / 2,
                size, direction, isEnabled());
        }

        public void paintTriangle(Graphics g, int x, int y, int size,
            int direction, boolean isEnabled) {
            Color oldColor = g.getColor();
            int mid, i, j;

            j = 0;
            size = Math.max(size, 2);
            mid = size / 2;

            g.translate(x, y);
            if (isEnabled)
                g.setColor(UIManager.getColor("controlDkShadow"));
            else
                g.setColor(UIManager.getColor("controlShadow"));

            switch (direction) {
            case NORTH:
                for (i = 0; i < size; i++) {
                    g.drawLine(mid - i, i, mid + i, i);
                }
                if (!isEnabled) {
                    g.setColor(UIManager.getColor("controlLtHighlight"));
                    g.drawLine(mid - i + 2, i, mid + i, i);
                }
                break;

            case SOUTH:
                if (!isEnabled) {
                    g.translate(1, 1);
                    g.setColor(UIManager.getColor("controlLtHighlight"));
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(mid - i, j, mid + i, j);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(UIManager.getColor("controlShadow"));
                }

                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                break;

            case WEST:
                for (i = 0; i < size; i++) {
                    g.drawLine(i, mid - i, i, mid + i);
                }
                if (!isEnabled) {
                    g.setColor(UIManager.getColor("controlLtHighlight"));
                    g.drawLine(i, mid - i + 2, i, mid + i);
                }
                break;

            case EAST:
                if (!isEnabled) {
                    g.translate(1, 1);
                    g.setColor(UIManager.getColor("controlLtHighlight"));
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(j, mid - i, j, mid + i);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(UIManager.getColor("controlShadow"));
                }

                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(j, mid - i, j, mid + i);
                    j++;
                }
                break;
            }
            g.translate(-x, -y);
            g.setColor(oldColor);
        }

        /*      public Dimension getPreferredSize(){
         if (SpinButton.this.getWidth()<=100)
         return new Dimension(10,SpinButton.this.getHeight()/2);
         else{
         return new Dimension(SpinButton.this.getWidth()/10,SpinButton.this.getHeight()/2);
         }
         }
         */
    }

    public void updateUI() {
        boolean opaque = isOpaque();

        super.updateUI();
        setOpaque(opaque);
    }
}

