package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.SoftBevelBorder;

import com.objectspace.jgl.Array;

//import org.gjt.sp.jedit.jEdit;


public class ObjectEventPanel extends JPanel {
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;
    Locale locale;
    ResourceBundle objectEventPanelBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    protected Font greekUIBoldFont = new Font("Helvetica", Font.BOLD, 12);
//    protected Font greekUIBoldItalicFont = new Font("Helvetica", Font.BOLD | Font.ITALIC, 12);
//    private boolean localeIsGreek = false;
//    private final static int COMPONENT_AREA_WIDTH = 150;
    public final static Color SCRIPTLISTENER_EXISTS_COLOR = Color.orange; //white; //new Color(0, 204, 0);
    JScrollPane scrollPane;

    Object component = null;
    ESlateHandle eSlateHandle;
    ESlateComposer composer = null;
    String componentName;
    int labelMaxWidth;
    Array eventPanels = new Array();
//    String pathToJikes = null;
    JPanel eventsPanel = null; // the panel where the 'EventPanel's are added
    JPanel mainPanel = null, noListenerPanel = null;
//    private boolean packed = false;
    private boolean emptyDialog = false;


    public ObjectEventPanel(ESlateComposer composer, ESlateHandle handle, Object compo, String compoName) {
        super(); //new JFrame(), true);
/*        if (handle == null)
            throw new NullPointerException("The handle cannot be null");
        if (compo == null)
            throw new NullPointerException("No component was given");
*/
        this.composer = composer;
        eSlateHandle = handle;
        component = compo;
        componentName = compoName;
        ScriptListener[] scriptListeners = composer.componentScriptListeners.getScriptListeners(compo);


        locale = Locale.getDefault();
        objectEventPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ObjectEventPanelBundle", locale);
//        if (objectEventPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.ObjectEventPanelBundle_el_GR"))
//            localeIsGreek = true;


        if (handle != null) {
            createEventPanels(scriptListeners);
        }else{
            eventsPanel = new JPanel(true);
            eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
            updateEventPanels(null, null);
        }

        scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout()); //BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(mainPanel);
        add(Box.createGlue());


        //Show the dialog
//        adjustDialogSize();
/*        if (getSize().height < 100)
            setSize(new Dimension(220, 600));
*/
/*        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = centerAroundComp.getBounds();
            java.awt.Point compLocation = centerAroundComp.getLocationOnScreen();
    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
            x = compLocation.x + compBounds.width/2 - getSize().width/2;
            y = compLocation.y + compBounds.height/2-getSize().height/2;
            if (x+getSize().width > screenSize.width)
                x = screenSize.width - getSize().width;
            if (y+getSize().height > screenSize.height)
                y = screenSize.height - getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        setLocation(x, y);
        show();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                container.eventPanel = null;
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispatchEvent(new WindowEvent(EventPanel.this, WindowEvent.WINDOW_CLOSING));
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

*/
    }

    void createEventPanels(ScriptListener[] scriptListeners) {
        EventSetDescriptor[] eventDescriptors = null;
        Class<?> compoClass = component.getClass();
        BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //compoClass.getSuperclass());
        if (compoInfo == null) return;

//        System.out.println("compoInfo: " + compoInfo);
        eventDescriptors = compoInfo.getEventSetDescriptors();
//        System.out.println("eventDescriptors: " + eventDescriptors);
//        System.out.println("eventDescriptors.length: " + eventDescriptors.length);

        if (eventDescriptors == null)
            return;

        JLabel lb = new JLabel();
        Font evtLabelFont;
//        if (localeIsGreek)
//            evtLabelFont = lb.getFont(); //greekUIFont; //greekUIBoldFont;
//        else
            evtLabelFont = new Font(lb.getFont().getFamily(), Font.BOLD, lb.getFont().getSize());

        FontMetrics fm = lb.getFontMetrics(evtLabelFont);
        labelMaxWidth = -1; //fm.stringWidth(((JLabel) propertyLabels.at(0)).getText());
        String methodName;
        for (int i=0; i<eventDescriptors.length; i++) {
//            System.out.println("Event : " +  eventDescriptors[i].getName() + ", " + eventDescriptors[i].getDisplayName() + ", " + eventDescriptors[i].getAddListenerMethod());
//            Method[] methods = eventDescriptors[i].getListenerMethods();
            MethodDescriptor[] methodDescriptors = eventDescriptors[i].getListenerMethodDescriptors();
            if (labelMaxWidth == -1)
                labelMaxWidth = fm.stringWidth(methodDescriptors[0].getMethod().getName());
            for (int o=0; o<methodDescriptors.length; o++) {
                if (methodDescriptors[o].getDisplayName() == null || methodDescriptors[o].getDisplayName().trim().length() == 0)
                    methodDescriptors[o].setDisplayName(methodDescriptors[o].getMethod().getName());
                methodName = methodDescriptors[o].getDisplayName();
                if (fm.stringWidth(methodName) > labelMaxWidth)
                    labelMaxWidth = fm.stringWidth(methodName);
            }
        }

        //Construct the dialog UI. Create one panel for each event
        for (int i=0; i<eventDescriptors.length; i++) {
            MethodDescriptor[] methodDescriptors = eventDescriptors[i].getListenerMethodDescriptors();
            for (int k=0; k<methodDescriptors.length; k++) {
                // Find if there exists a listener for this method
                ScriptListener logoScriptListener = null;
                ScriptListener javaScriptListener = null;
                ScriptListener jsScriptListener = null;
                if (scriptListeners != null) {
                    for (int j=0; j<scriptListeners.length; j++) {
//                        System.out.println("scriptListeners[j].getMethodName(): " + scriptListeners[j].getMethodName() +
//                        ", methods[k].getName(): " + methods[k].getName());
                        if (scriptListeners[j].getMethodName().equals(methodDescriptors[k].getMethod().getName())) {
                            if (scriptListeners[j].scriptLanguage == ScriptListener.LOGO)
                                logoScriptListener = scriptListeners[j];
                            else if (scriptListeners[j].scriptLanguage == ScriptListener.JAVA)
                                javaScriptListener = scriptListeners[j];
                            else if (scriptListeners[j].scriptLanguage == ScriptListener.JAVASCRIPT)
                                jsScriptListener = scriptListeners[j];
                            if (logoScriptListener != null && javaScriptListener != null && jsScriptListener != null)
                                break;
                        }
                    }
                }
                EventPanel2 evtPanel = new EventPanel2(this,
                                                     eventDescriptors[i],
                                                     eSlateHandle,
                                                     component,
                                                     methodDescriptors[k],
                                                     labelMaxWidth,
                                                     /*evtLabelFont,*/
                                                     logoScriptListener,
                                                     javaScriptListener,
                                                     jsScriptListener);
                eventPanels.add(evtPanel);
            }
        }

        /* Sort the event panels based on the names of the their methods.
         */
        com.objectspace.jgl.OrderedMap evtPanelMap = new com.objectspace.jgl.OrderedMap(new com.objectspace.jgl.LessString());
        for (int i=0; i<eventPanels.size(); i++)
            evtPanelMap.add(((EventPanel2) eventPanels.at(i)).methodDescriptor.getMethod().getName(), eventPanels.at(i));
        java.util.Enumeration<?> methodNames = evtPanelMap.keys();
        int t = 0;
        while (methodNames.hasMoreElements()) {
            eventPanels.put(t, evtPanelMap.get(methodNames.nextElement()));
            t++;
        }
        evtPanelMap = null;

        // The events panel
        if (eventsPanel == null) {
            eventsPanel = new JPanel(true);
            eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        }
        for (int i=0; i<eventPanels.size(); i++)
            eventsPanel.add((JPanel) eventPanels.at(i));
    }

    /* Update the event panels for the given 'object'. The handle to be passed in the listeners for
     * this object, is the one attached to the 'topObject'.
     */
    void updateEventPanels(Object object, Object topObject) {
//        System.out.println("updateEventPanels -- > object: " + object + ", topObject: " + topObject);
        if (object != null) {
//            System.out.println("updateEventPanels -- > container.componentScriptListeners.getContainingObjectHandle(object): " + container.componentScriptListeners.getContainingObjectHandle(object));
            ESlateHandle h = null;
            if (composer.microworld != null) {
//System.out.println("container.microworld.eslateMwd.getComponentHandle(topObject): " + container.microworld.eslateMwd.getComponentHandle(topObject));
//ESlateHandle h1 = container.microworld.eslateMwd.getComponentHandle("������������ ������");
//System.out.println("h1: " + h1);
//ESlateHandle dbh = h1.getChildByName("���� ���������");
//System.out.println("dbh: " + dbh + ", microworld: " + dbh.getESlateMicroworld());
//System.out.println("container.microworld.eslateMwd: " + container.microworld.eslateMwd);
                h = composer.microworld.eslateMwd.getComponentHandle(topObject);
                if (h == null) {
                    if (topObject == composer.microworld)
                        h = composer.getESlateHandle();
//                        h = container.microworld.eslateMwd.getESlateHandle();
                }
            }
            updateEventPanels(object, h);
        }else
            updateEventPanels(null, null);
    }

    void updateEventPanels(Object object, ESlateHandle handle) {
        /* Don't update the Event dialog, if there is a microworld being loaded. This is because
         * the microworld's script listeners are not properly re-established until the whole
         * microworld is loaded, While the microworld is loaded many 'activeComponentChanged' events
         * are triggered, which should not cause the EventDialog panel to update itself, since the
         * script listeners are not yet ready. We call updateEventPanels() ourselves, when the
         * scriptListeners are re-attached, after the microworld has been loaded.
         */
        if (composer.loadingMwd) {
            return;
        }
//        System.out.println("updateEventPanels() called on object: " + object + " with handle: " + handle);
        if (handle == null || object == null) {
            if (!emptyDialog) {
                eventsPanel.removeAll();
                if (eventPanels != null)
                    eventPanels.clear();
//                mainPanel.removeAll();
                if (noListenerPanel == null) {
                    noListenerPanel = new JPanel(true);
                    noListenerPanel.setLayout(new BoxLayout(noListenerPanel, BoxLayout.Y_AXIS));

                    JPanel innerNoListenerPanel = new JPanel(true);
                    innerNoListenerPanel.setLayout(new BoxLayout(innerNoListenerPanel, BoxLayout.X_AXIS));
                    JLabel noListenerLabel = new JLabel(objectEventPanelBundle.getString("NoComponent"));
                    Font lbFont;
//                    if (localeIsGreek)
//                        lbFont = greekUIBoldItalicFont;
//                    else
                        lbFont = new Font(noListenerLabel.getFont().getFamily(), Font.BOLD | Font.ITALIC, noListenerLabel.getFont().getSize());

                    noListenerLabel.setFont(lbFont);
                    FontMetrics fm = noListenerLabel.getFontMetrics(lbFont);
                    Dimension lbDim = new Dimension(fm.stringWidth(noListenerLabel.getText())+20, 30);
                    noListenerLabel.setMaximumSize(lbDim);
                    noListenerLabel.setMinimumSize(lbDim);
                    noListenerLabel.setPreferredSize(lbDim);
                    noListenerLabel.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
                    noListenerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                    innerNoListenerPanel.add(Box.createGlue());
                    innerNoListenerPanel.add(noListenerLabel);
                    innerNoListenerPanel.add(Box.createGlue());
                    noListenerPanel.add(Box.createVerticalStrut(20));
                    noListenerPanel.add(innerNoListenerPanel);
                    noListenerPanel.add(Box.createGlue());

                }
//                mainPanel.add(noListenerPanel);
                eventsPanel.add(noListenerPanel);
                emptyDialog = true;
                validate();
                repaint();
            }
            return;
        }

        if (emptyDialog) {
            eventsPanel.removeAll();
            eventsPanel.revalidate();
            emptyDialog = false;
        }
        component  = object;
        componentName = handle.getComponentName();
        eSlateHandle = handle;
        ScriptListener[] scriptListeners = composer.componentScriptListeners.getScriptListeners(component);
//System.out.println("ObjectEventPanel getScriptListeners() size: " + scriptListeners.length);

        EventSetDescriptor[] eventDescriptors = null;
        Class<?> compoClass = component.getClass();
//        String[] paths = Introspector.getBeanInfoSearchPath();
        Introspector.setBeanInfoSearchPath(new String[]
                                        {"javax.swing"});
        BeanInfo compoInfo = BeanInfoFactory.getBeanInfo(compoClass); //, compoClass.getSuperclass());
        if (compoInfo == null) return;

        eventDescriptors = compoInfo.getEventSetDescriptors();

        if (eventDescriptors == null)
            return;

        JLabel lb = new JLabel();
//        Font evtLabelFont;
//        if (localeIsGreek)
//            evtLabelFont = greekUIFont; //greekUIBoldFont;
//        else
//            evtLabelFont = new Font(lb.getFont().getFamily(), Font.BOLD, lb.getFont().getSize());

        FontMetrics fm = lb.getFontMetrics(lb.getFont());
        labelMaxWidth = -1; //fm.stringWidth(((JLabel) propertyLabels.at(0)).getText());
        String methodName;
        for (int i=0; i<eventDescriptors.length; i++) {
//            Method[] methods = eventDescriptors[i].getListenerMethods();
//            System.out.println("Event : " +  eventDescriptors[i].getName() + ", " + eventDescriptors[i].getDisplayName() + ", " + eventDescriptors[i].getAddListenerMethod());
            MethodDescriptor[] methodDescriptors = eventDescriptors[i].getListenerMethodDescriptors();
            if (labelMaxWidth == -1)
                labelMaxWidth = fm.stringWidth(methodDescriptors[0].getMethod().getName());
            for (int o=0; o<methodDescriptors.length; o++) {
                if (methodDescriptors[o].getDisplayName() == null || methodDescriptors[o].getDisplayName().trim().length() == 0)
                    methodDescriptors[o].setDisplayName(methodDescriptors[o].getMethod().getName());
                methodName = methodDescriptors[o].getDisplayName();
                if (fm.stringWidth(methodName) > labelMaxWidth)
                    labelMaxWidth = fm.stringWidth(methodName);
            }
        }

        //Construct the dialog UI. Create one panel for each event
        com.objectspace.jgl.OrderedMap slmsMap = new com.objectspace.jgl.OrderedMap(new com.objectspace.jgl.LessString());
        for (int i=0; i<eventDescriptors.length; i++) {
            MethodDescriptor[] methodDescriptors = eventDescriptors[i].getListenerMethodDescriptors();
            for (int k=0; k<methodDescriptors.length; k++) {
                // Find if there exists a listener for this method
                ScriptListener logoScriptListener = null;
                ScriptListener javaScriptListener = null;
                ScriptListener jsScriptListener = null;
                if (scriptListeners != null) {
                    for (int j=0; j<scriptListeners.length; j++) {
//                        System.out.println("scriptListeners[j].getMethodName(): " + scriptListeners[j].getMethodName() +
//                        ", methods[k].getName(): " + methods[k].getName());
                        if (scriptListeners[j].getMethodName().equals(methodDescriptors[k].getMethod().getName())) {
//js                            if (scriptListeners[j].scriptInLogo.booleanValue())
                            if (scriptListeners[j].scriptLanguage == ScriptListener.LOGO)
                                logoScriptListener = scriptListeners[j];
                            else if (scriptListeners[j].scriptLanguage == ScriptListener.JAVA)
                                javaScriptListener = scriptListeners[j];
                            else if (scriptListeners[j].scriptLanguage == ScriptListener.JAVASCRIPT)
                                jsScriptListener = scriptListeners[j];
                            if (javaScriptListener != null && logoScriptListener != null && jsScriptListener != null)
                                break;
                        }
                    }
                }

                SortListenerMethodsStructure slms = new SortListenerMethodsStructure(eventDescriptors[i],
                                                                                     methodDescriptors[k],
                                                                                     logoScriptListener,
                                                                                     javaScriptListener,
                                                                                     jsScriptListener);
                slmsMap.add(methodDescriptors[k].getDisplayName(), slms);
            }
        }

        boolean repaintNeeded = false;
        java.util.Enumeration<?> methodNames = slmsMap.keys();
//        System.out.println("eventPanels.size(): " + eventPanels.size() + ", slmsMap.size(): " + slmsMap.size());
        if (slmsMap.size() < eventPanels.size()) {
            for (int i = eventPanels.size()-1; i >= slmsMap.size(); i--) {
                eventsPanel.remove((EventPanel2) eventPanels.at(i));
                eventPanels.remove(i);
                repaintNeeded = true;
            }
        }
//        System.out.println("eventPanels.size(): " + eventPanels.size() + ", slmsMap.size(): " + slmsMap.size());

        int i = 0;
        while (methodNames.hasMoreElements()) {
            SortListenerMethodsStructure slms = (SortListenerMethodsStructure) slmsMap.get(methodNames.nextElement());
            if (i >= eventPanels.size()) {
                EventPanel2 evtPanel = new EventPanel2(this,
                                                     slms.evtDescriptor,
                                                     eSlateHandle,
                                                     component,
                                                     slms.methodDescriptor,
                                                     labelMaxWidth,
                                                     /*evtLabelFont,*/
                                                     slms.logoScriptListener,
                                                     slms.javaScriptListener,
                                                     slms.jsScriptListener);
                eventPanels.add(evtPanel);
                eventsPanel.add(evtPanel);
                repaintNeeded = true;
            }else{
                //Re-use the existing event panels.
                EventPanel2 evtPanel = (EventPanel2) eventPanels.at(i);
                evtPanel.setEventDescriptor(slms.evtDescriptor);
                evtPanel.setCompoHandle(eSlateHandle);
                evtPanel.setComponent(component);
                evtPanel.setMethodDescriptor(slms.methodDescriptor);
                evtPanel.setLabelMaxWidth(labelMaxWidth);
                /*evtPanel.setEvtLabelFont(evtLabelFont);*/
                evtPanel.setLogoScriptListener(slms.logoScriptListener);
                evtPanel.setJavaScriptListener(slms.javaScriptListener);
                evtPanel.setJSScriptListener(slms.jsScriptListener);
            }
            i++;
        }
        slmsMap = null;

//        adjustDialogSize();
//        System.out.println("repaintNeeded: " + repaintNeeded);
        if (repaintNeeded) {
            eventsPanel.validate();
            eventsPanel.repaint();
        }
    }

/*    void adjustDialogSize() {
        setResizable(true);
        if (!packed) {
            pack();
            Dimension frameSize1 = getSize();
            setSize(frameSize1.width+10, frameSize1.height); //screenSize.height);
            packed = true;
        }

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            setSize(frameSize.width, screenSize.height);
        }else{
            setSize(frameSize.width, frameSize.height);
        }
    }
*/
/*    public void dispose() {
        super.dispose();
        container.eventPanel = null;
    }
*/
    protected EventPanel2 getEventPanel(String methodName) {
        EventPanel2 evtPanel = null;
        EventPanel2 p;
        for (int i=0; i<eventPanels.size(); i++) {
            p = (EventPanel2) eventPanels.at(i);
            if (p.methodDescriptor.getMethod().getName().equals(methodName)) {
                evtPanel = p;
                break;
            }
        }
        return evtPanel;
    }

/*    protected ScriptListener[] getScriptListeners() {
        //Count the scriptListener
        int count = 0;
        int panelCount = eventPanels.size();
        for (int i=0; i<panelCount; i++) {
            if (((EventPanel) eventPanels.at(i)).currentListener != null)
                count++;
        }

        ScriptListener[] scriptListeners = new ScriptListener[count];
        int k =0;
        EventPanel evtPanel;
        for (int i=0; i<panelCount; i++) {
            evtPanel = (EventPanel) eventPanels.at(i);
            if (evtPanel.currentListener != null) {
                scriptListeners[k++] = new ScriptListener(componentName, evtPanel.method.getName(),
                                                       evtPanel.scriptName, evtPanel.script,
                                                       evtPanel.currentListener,
                                                       evtPanel.listenerClassBytes,
                                                       evtPanel.isScriptInLogo());
            }
        }
        return scriptListeners;
    }
*/
}


class EventPanel2 extends JPanel {
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;
    private final static int EVENT_PANEL_HEIGHT = 26;
    private final static int SCRIPT_LABEL_WIDTH = 100;
    EventSetDescriptor eventDescriptor;
    MethodDescriptor methodDescriptor;
    String[] eventVariables;
    Array getterMethods = new Array();
//    Class listenerClass = null;
//    byte[] listenerClassBytes = null;
//    String scriptName, script;
//    LogoScriptDialog logoScriptDialog;
//    JavaCodeDialog javaCodeDialog;
    String noScriptName;
//    private Boolean scriptInLogo = null;
    ESlateComposer composer;
    ObjectEventPanel objectEventPanel = null;
    /* Before adding a new Listener to a component, we have to remove the listener that
     * has possibly been added before. e.g. when the user alters the script for an event,
     * he practically defines a new listener. The previous listener, which carried the old
     * version of the script has to be removed. The old listener is contained in the
     * variable "currentListener".
     */
//    java.util.EventListener currentListener = null;
///    ScriptListener scriptListener;
    ScriptListener logoScriptListener = null;
    ScriptListener javaScriptListener = null;
    ScriptListener jsScriptListener = null;

    Object component;
    ESlateHandle eSlateHandle;
    Dimension scriptLabelDim = new Dimension(SCRIPT_LABEL_WIDTH, EVENT_PANEL_HEIGHT);
    Dimension buttonDim = new Dimension(25, EVENT_PANEL_HEIGHT-5); //fm.stringWidth(buttonText), EVENT_PANEL_HEIGHT-5);
    ImageIcon buttonIcon = new ImageIcon(getClass().getResource("images/L.gif"));
    ImageIcon javaButtonIcon = new ImageIcon(getClass().getResource("images/J.gif"));
    ImageIcon jsButtonIcon = new ImageIcon(getClass().getResource("images/javascript.gif"));
    JButton javaScriptButton, logoScriptButton, jsScriptButton;
    Insets zeroInsets = new Insets(0, 0, 0, 0);
    SoftBevelBorder scriptLabelBorder = new SoftBevelBorder(SoftBevelBorder.LOWERED);
    private int labelMaxWidth = 0;
    JLabel evtLabel; //, scriptLabel;


    public EventPanel2(ObjectEventPanel ed, EventSetDescriptor descr, ESlateHandle handle,
                      Object compo, MethodDescriptor methodDescr, int labelMaxWidth, /*Font evtLabelFont,*/
                      ScriptListener logoListener, ScriptListener javaListener,
                      ScriptListener jsListener) {
        super(true);
        this.objectEventPanel = ed;
        eventDescriptor = descr;
        eSlateHandle = handle;
//        System.out.println("EventPanel2: " + handle.getComponentName());
        component = compo;
        this.methodDescriptor = methodDescr;
//        this.noScriptName = noScriptName;
        this.composer = objectEventPanel.composer;
//        System.out.println("EventPanel for method: " + method.getName());
//        logoScriptListener = ed.container.componentScriptListeners.getScriptListener(component, method.getName(), true);
//        javaScriptListener = ed.container.componentScriptListeners.getScriptListener(component, method.getName(), false);
        this.logoScriptListener = logoListener;
        this.javaScriptListener = javaListener;
        this.jsScriptListener = jsListener;
        this.labelMaxWidth = labelMaxWidth;

//        String mthName = method.getName();
//        System.out.println("1. mthName: " + mthName + ", descr.getClass(): " + descr.getClass());
//        if (ExtendedEventSetDescriptor.class.isAssignableFrom(descr.getClass())) {
//            System.out.println("2. Here " +  descr.getClass());
//            String s = ((ExtendedEventSetDescriptor) descr).getEndUserMethodName(mthName);
//            if (s != null)
//                mthName = s;
//        }
        evtLabel = new JLabel(methodDescriptor.getDisplayName()); //method.getName());
//        evtLabel.setFont(evtLabelFont);
        evtLabel.setForeground(BeanInfoDialog.PROPERTY_LABEL_COLOR);
        Dimension evtLabelDim = new Dimension(labelMaxWidth+5, EVENT_PANEL_HEIGHT);
        evtLabel.setMaximumSize(evtLabelDim);
        evtLabel.setMinimumSize(evtLabelDim);
        evtLabel.setPreferredSize(evtLabelDim);

/*        scriptLabel = new JLabel(""); //scriptName);
        scriptLabel.setMaximumSize(scriptLabelDim);
        scriptLabel.setMinimumSize(scriptLabelDim);
        scriptLabel.setPreferredSize(scriptLabelDim);
        scriptLabel.setBorder(scriptLabelBorder);
*/
        logoScriptButton = new JButton(buttonIcon);
        logoScriptButton.setMargin(zeroInsets);
        logoScriptButton.setMaximumSize(buttonDim);
        logoScriptButton.setMinimumSize(buttonDim);
        logoScriptButton.setPreferredSize(buttonDim);
        logoScriptButton.setFocusPainted(true);
        logoScriptButton.setRequestFocusEnabled(true);
        logoScriptButton.setAlignmentY(CENTER_ALIGNMENT);
//        logoScriptButton.setUI(new BasicButtonUI());
        if (logoScriptListener != null)
            logoScriptButton.setBackground(ObjectEventPanel.SCRIPTLISTENER_EXISTS_COLOR);
        logoScriptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editScriptListener(logoScriptListener, ScriptListener.LOGO);
//                if (eventVariables == null) {
                    /* Find the variables which will be made available to the Logo script.
                     * These variables are the ones that are conveyed by the event object
                     * received by the listener. This will be done only the first time the
                     * listener is created.
                     */
/*                    Class[] paramClasses = EventPanel2.this.methodDescriptor.getMethod().getParameterTypes();
                    Class eventClass = paramClasses[0];
                    Method[] eventMethods = eventClass.getMethods();
                    for (int i=0; i<eventMethods.length; i++) {
                        Class returnType = eventMethods[i].getReturnType();
                        if (Modifier.isPublic(eventMethods[i].getModifiers()) &&
                            returnType != null &&
                            !returnType.getName().equals("void") &&
                            eventMethods[i].getParameterTypes().length == 0) {
                            if (returnType.equals(String.class)) {
                                getterMethods.add(eventMethods[i]);
                            }else if (returnType.isPrimitive() || returnType.isArray()) {
                                getterMethods.add(eventMethods[i]);
                            }
                        }
                    }
                    eventVariables = new String[getterMethods.size()];
                    for (int i=0; i<getterMethods.size(); i++) {
                        if (((Method) getterMethods.at(i)).getName().startsWith("get"))
                            eventVariables[i] = ((Method) getterMethods.at(i)).getName().substring(3);
                        else
                            eventVariables[i] = ((Method) getterMethods.at(i)).getName();
                    }
                }
*/

/*                EditedListener el = container.listenerDialogList.getEditedListener(eSlateHandle.getComponentName(),
                                                                   EventPanel2.this.methodDescriptor.getMethod().getName(),
                                                                   true);
                if (el != null) {
*/                    /* An open editor for this script already exists. Don't create a new one,
                     * but find the existing an bring it to front.
                     */
/*                    JFrame fr = container.listenerDialogList.getEditorForListener(el);
                    fr.toFront();
                    fr.requestFocus();
                }else{
*/                    /* The first time the dialog appears for an event for which no script was previously attached,
                     * name the script in the scriptDialog after the name of the method for the event. However if
                     * this is the first time the scriptDialog appears for this event, but the scriptName was given
                     * during a previous session, then use the scriptName.
                     */
/*                    LogoScriptDialog logoScriptDialog;
                    if (logoScriptListener == null) {
                        logoScriptDialog = new LogoScriptDialog(container,
                                                                component,
                                                                eSlateHandle,
                                                                container.propertyEventEditor.componentPath,
                                                                eventDescriptor,
                                                                EventPanel2.this.methodDescriptor.getMethod(),
                                                                eventVariables,
                                                                logoScriptListener,
                                                                getterMethods);
                    }else{
                        logoScriptDialog = new LogoScriptDialog(container,
                                                                component,
                                                                eSlateHandle,
                                                                container.propertyEventEditor.componentPath,
                                                                eventDescriptor,
                                                                EventPanel2.this.methodDescriptor.getMethod(),
                                                                eventVariables,
                                                                logoScriptListener,
                                                                getterMethods);
                    }
                    Rectangle bounds = objectEventPanel.getBounds();
                    logoScriptDialog.showDialog(bounds);
                    container.listenerDialogList.addListener(eSlateHandle.getComponentName(),
                                                             EventPanel2.this.methodDescriptor.getMethod().getName(),
                                                             true,
                                                             logoScriptDialog);
                }
*/
            }
        });

        jsScriptButton = new JButton(jsButtonIcon);
        jsScriptButton.setMargin(zeroInsets);
        jsScriptButton.setMaximumSize(buttonDim);
        jsScriptButton.setMinimumSize(buttonDim);
        jsScriptButton.setPreferredSize(buttonDim);
        jsScriptButton.setFocusPainted(true);
        jsScriptButton.setRequestFocusEnabled(true);
        jsScriptButton.setAlignmentY(CENTER_ALIGNMENT);
        if (jsScriptListener != null)
            jsScriptButton.setBackground(ObjectEventPanel.SCRIPTLISTENER_EXISTS_COLOR);

        jsScriptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editScriptListener(jsScriptListener, ScriptListener.JAVASCRIPT);
            }
        });

        javaScriptButton = new JButton(javaButtonIcon);
        javaScriptButton.setMargin(zeroInsets);
        javaScriptButton.setMaximumSize(buttonDim);
        javaScriptButton.setMinimumSize(buttonDim);
        javaScriptButton.setPreferredSize(buttonDim);
        javaScriptButton.setAlignmentY(CENTER_ALIGNMENT);
        javaScriptButton.setFocusPainted(true);
        javaScriptButton.setRequestFocusEnabled(true);
        if (javaScriptListener != null)
            javaScriptButton.setBackground(ObjectEventPanel.SCRIPTLISTENER_EXISTS_COLOR);
        javaScriptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editScriptListener(javaScriptListener, ScriptListener.JAVA);
/*                if (EventPanel2.this.container.javaEditor != null && !EventPanel2.this.container.javaEditor.codeView.isVisible())
                    EventPanel2.this.container.javaEditor = null;
*/
                /* Check if there has been initiated a JavaCodeDialog for this event of this component.
                 * If it has, then don't create a new one, but instead activate the old one. If it
                 * hasn't then create a new one. The history of the JavaCodeDialogs is saved in the
                 * HashMap 'javaCodeDialogMap' of ObjectEventPanel. The history lasts as long as the one
                 * and only one JavaEditor Frame exists. When is is disposed, the history is cleared.
                 */
/*                JavaCodeDialog jcd = null, o;
                Enumeration javaCodeDialogs = EventPanel2.this.container.javaCodeDialogMap.keys();
                while (javaCodeDialogs.hasMoreElements()) {
                    o = (JavaCodeDialog) javaCodeDialogs.nextElement();
                    if (o.eSlateHandle.equals(eSlateHandle) && o.method.equals(EventPanel2.this.methodDescriptor.getMethod())) {
                        jcd = o;
                        break;
                    }
                }
                if (jcd == null) {
                    EventPanel2.this.objectEventPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    new JavaCodeDialog(
                                    component,
                                    eSlateHandle,
                                    container.propertyEventEditor.componentPath,
                                    eventDescriptor,
                                    EventPanel2.this.methodDescriptor.getMethod(),
                                    container,
                                    javaScriptListener);
                    EventPanel2.this.objectEventPanel.setCursor(Cursor.getDefaultCursor());
                }else{
                    jcd.activate();
                }
*/
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createRigidArea(new Dimension(5, EVENT_PANEL_HEIGHT)));
        add(evtLabel);
        add(Box.createHorizontalGlue());
///        add(scriptLabel);
        add(Box.createRigidArea(new Dimension(5, EVENT_PANEL_HEIGHT)));
        add(logoScriptButton);
        add(Box.createRigidArea(new Dimension(5, EVENT_PANEL_HEIGHT)));
        add(jsScriptButton);
        add(Box.createRigidArea(new Dimension(5, EVENT_PANEL_HEIGHT)));
        add(javaScriptButton);
        add(Box.createRigidArea(new Dimension(5, EVENT_PANEL_HEIGHT)));

//        if (scriptInLogo != null) {
//            logoScriptButton.setEnabled(scriptInLogo.booleanValue());
//            javaScriptButton.setEnabled(!scriptInLogo.booleanValue());
//        }
    }

//    public Boolean isScriptInLogo() {
//        return scriptInLogo;
//    }

    public void setMethodDescriptor(MethodDescriptor methodDescr) {
        this.methodDescriptor = methodDescr;
//        String mthName = method.getName();
//        if (ExtendedEventSetDescriptor.class.isAssignableFrom(eventDescriptor.getClass())) {
//            String s = ((ExtendedEventSetDescriptor) eventDescriptor).getEndUserMethodName(mthName);
//            if (s != null)
//                mthName = s;
//        }
        evtLabel.setText(methodDescriptor.getDisplayName());
    }

    public void setEventDescriptor(EventSetDescriptor evtDescr) {
        this.eventDescriptor = evtDescr;
    }

    public void setCompoHandle(ESlateHandle handle) {
        this.eSlateHandle = handle;
    }

    public void setComponent(Object component) {
        this.component = component;
    }

    public void setLabelMaxWidth(int labelMaxWidth) {
        this.labelMaxWidth = labelMaxWidth;
        Dimension evtLabelDim = new Dimension(labelMaxWidth+5, EVENT_PANEL_HEIGHT);
        evtLabel.setMaximumSize(evtLabelDim);
        evtLabel.setMinimumSize(evtLabelDim);
        evtLabel.setPreferredSize(evtLabelDim);
    }

    public void setEvtLabelFont(Font f) {
        evtLabel.setFont(f);
    }

    public void setLogoScriptListener(ScriptListener scrListener) {
//        System.out.println("Event Dialog setLogoScriptListener(): " + scrListener);
        this.logoScriptListener = scrListener;
        if (logoScriptListener != null) {
            logoScriptButton.setBackground(ObjectEventPanel.SCRIPTLISTENER_EXISTS_COLOR);
        }else{
            JButton b = new JButton();
            logoScriptButton.setBackground(b.getBackground());
        }
    }

    public void setJavaScriptListener(ScriptListener scrListener) {
        this.javaScriptListener = scrListener;
        if (javaScriptListener != null) {
            javaScriptButton.setBackground(ObjectEventPanel.SCRIPTLISTENER_EXISTS_COLOR);
        }else{
            JButton b = new JButton();
            javaScriptButton.setBackground(b.getBackground());
        }
    }

    public void setJSScriptListener(ScriptListener scrListener) {
        this.jsScriptListener = scrListener;
        if (jsScriptListener != null) {
            jsScriptButton.setBackground(ObjectEventPanel.SCRIPTLISTENER_EXISTS_COLOR);
        }else{
            JButton b = new JButton();
            jsScriptButton.setBackground(b.getBackground());
        }
    }

    public int getPanelWidth() {
        int width = SCRIPT_LABEL_WIDTH; // script name label width
        width = width + labelMaxWidth + 5; // event name label width
        width = width + buttonDim.width; //logoScriptButton width
        width = width + buttonDim.width; //javaScriptButton width
        width = width + buttonDim.width; //jsScriptButton width
        width = width + 5; // RigidArea
        width = width + 5; // RigidArea
        width = width + 5; // RigidArea
        width = width + 5; // RigidArea
        width = width + 5; // RigidArea
        return width;
    }

    public int getPanelHeight() {
        return EVENT_PANEL_HEIGHT;
    }

    private void editScriptListener(ScriptListener scriptListener, int language) {
        boolean newListener = false;

        /* First check if the listener currently edited in the 'scriptDialog' is
         * the ScriptListener for the current event. If it is and no ScriptListener
         * has been attached to this event, then don't create an empty ScriptListener.
         */
        if (composer.scriptDialog != null) {
            ScriptDialogNodeInterface selectedNode = composer.scriptDialog.getSelectedNode();
            if (selectedNode != null && ScriptListenerNode.class.isAssignableFrom(selectedNode.getClass())) {
				ScriptListenerNode node = (ScriptListenerNode) selectedNode;
                ESlateHandle handle = node.getParentHandleNode().handle;
                if (handle == objectEventPanel.eSlateHandle &&
                    node.listener.pathToComponent.equals(composer.propertyEventEditor.componentPath) &&
                    node.listener.methodName.equals(methodDescriptor.getMethod().getName()) &&
                    node.listener.scriptLanguage == language)
                        scriptListener = node.listener;
            }
        }

        String scriptListenerClassName = null; // Only for Java scripts
        /* If this is the first time a listener is created for this event, then
         * create an empty ScriptListener, add it to the ScriptListenerMap of
         * the ESlateContainer and then open it in the ScriptDialog.
         */
        if (scriptListener == null) {
            newListener = true;
            scriptListener = ScriptListener.createEmptyScriptListener(
                                      methodDescriptor.getMethod().getName(),
                                      eventDescriptor.getListenerType(),
                                      language,
                                      composer.propertyEventEditor.componentPath);
            scriptListener.scriptName = methodDescriptor.getDisplayName();
            if (language == ScriptListener.JAVA) {
                String[] s = ESlateComposerUtils.initializeJavaScriptListener(objectEventPanel.component,
                                                                               eventDescriptor,
                                                                               methodDescriptor.getMethod());
                /* In the case of Java scripts, the initial script is autogenerated, so the listener
                 * does not have an empty script, as is the case of new JS and Logo ScriptListeners.
                 */
                scriptListener.setScript(s[0]);
                scriptListenerClassName = s[1];
                /* In the case of Java scripts, the listener's class is not the
                 * 'eventDescriptor.getListenerType()', because the listener is auto-generated
                 * and it is a descendant of the 'eventDescriptor.getListenerType()'. The
                 * following statements update the new ScriptListener with the proper values.
                 */
                scriptListener.listenerClassName = scriptListenerClassName;
                scriptListener.listenerSuperIFName = eventDescriptor.getListenerType().getName();
            }
            composer.componentScriptListeners.addScriptListener(objectEventPanel.component,
                                                                 scriptListener,
                                                                 objectEventPanel.eSlateHandle);
        }else{
            scriptListenerClassName = scriptListener.listenerClassName.substring(scriptListener.listenerClassName.lastIndexOf('.')+1);
//            scriptListenerFileName = scriptListenerFileName + ".java";
        }

        composer.displayScriptEditor();
        if (composer.scriptDialog.selectScriptListener(scriptListener, newListener)) {
            // If this is a new listener, set the corresponding flag in the ScriptDialog
            if (newListener)
                composer.scriptDialog.setNewScript(true);
        }
/*        if (container.scriptDialog == null) {
            container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
            container.getScriptDialog(); // Initialize a ScriptDialog
            if (container.scriptDialog.selectScriptListener(scriptListener, newListener)) {
                // If this is a new listener, set the corresponding flag in the ScriptDialog
                if (newListener)
                    container.scriptDialog.setNewScript(true);
            }
            container.scriptDialog.showDialog(container.getBounds());
            container.setCursor(Cursor.getDefaultCursor(), false);
        }else{
//            if (!container.scriptDialog.isScriptTreeVisible())
//                container.scriptDialog.setScriptListenerTree(container.componentScriptListeners.getScriptListenerTree());
            if (container.scriptDialog.selectScriptListener(scriptListener, newListener)) {
                // If this is a new listener, set the corresponding flag in the ScriptDialog
                if (newListener)
                    container.scriptDialog.setNewScript(true);
            }
            container.scriptDialog.bringToFront();
//            container.scriptDialog.requestFocus();
        }
*/
        composer.scriptDialog.listenerClassName = scriptListenerClassName;
    }
}


class SortListenerMethodsStructure {
    EventSetDescriptor evtDescriptor;
    MethodDescriptor methodDescriptor;
    ScriptListener logoScriptListener;
    ScriptListener javaScriptListener;
    ScriptListener jsScriptListener;

    protected SortListenerMethodsStructure(EventSetDescriptor evtDescriptor,
                                           MethodDescriptor methodDescriptor,
                                           ScriptListener logoScriptListener,
                                           ScriptListener javaScriptListener,
                                           ScriptListener jsScriptListener) {
        this.evtDescriptor = evtDescriptor;
        this.methodDescriptor = methodDescriptor;
        this.logoScriptListener = logoScriptListener;
        this.javaScriptListener = javaScriptListener;
        this.jsScriptListener = jsScriptListener;
    }
}










