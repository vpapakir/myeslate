package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.panel.PanelComponent;
import gr.cti.eslate.utils.BooleanWrapper;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.typeArray.ObjectBaseArray;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanInfo;
import java.beans.Customizer;
import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.objectspace.jgl.Array;
import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


public class BeanInfoDialog extends JPanel implements Disposable { //JFrame {
    public final int READ_ONLY_PROPERTIES = 0;
    public final int READ_WRITE_PROPERTIES = 1;
    public final int ALL_PROPERTIES = 2;
    public final int EXPERT_PROPERTIES = 3;
    public final int BASIC_PROPERTIES = 4;
    public final int PREFERRED_PROPERTIES = 5;
    public final int BOUND_PROPERTIES = 6;
    public final int CONSTRAINED_PROPERTIES = 7;
    public final int HIDDEN_PROPERTIES = 8;
    /* When 'propertyDisplayMode' equals CUSTOM_PROPERTY_TYPE, the properties to be displayed and
     * edited in the property editor are not specified by 'propertyDisplayMode', but by the
     * 'propertyCategory' attribute.
     */
    public final int CUSTOM_PROPERTY_TYPE = 9;
    public final static Color PROPERTY_LABEL_COLOR = UIManager.getColor("textText");
    private int propertyDisplayMode = 4;
    /* 'propertyCategory' contains the name of the category of properties, which is
     * currently displayed in the property editor. This attribute is valid only
     * when the 'propertyDisplayMode' is equal to 'CUSTOM_PROPERTY_TYPE'
     */
    private String propertyCategory = null;
    /* The ActionListener for the JRadioButtonMenuItems created for the custom property categories
     */
    ActionListener customPropertyTypeListener;

    Locale locale;
    ResourceBundle beanInfoDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    protected Font greekUIBoldFont = new Font("Helvetica", Font.BOLD, 12);
//    protected Font greekUIBoldItalicFont = new Font("Helvetica", Font.BOLD | Font.ITALIC, 12);
    boolean localeIsGreek = false;
    JPanel mainPanel;
    JTabbedPane propertyEventTabs;
    public static final Border showOffBorder = new LineBorder(Color.yellow);

//    Object component = null;
    PropertyEditorPanel propertyEditorPanel;
    ObjectEventPanel eventEditorPanel = null;
    SoundEventPanel soundEventPanel = null;
    /* The menu which adjusts what the object hierarchy tree contains.
     */
    JPopupMenu objectHierarchyMenu;
    JCheckBoxMenuItem objectHierarchyItem2, uiHierarchyItem2, componentHierarchyItem2;
    JMenuItem addComponentItem, removeComponentItem;

    /* Indicates that the 'propertyEditorPanel' is empty (when true).
     */
    boolean emptyDialog = false;
    /* The objects which are part of the 'component' object, for which no registered property
     * descriptor exists.
     */
    ObjectHierarchyTreeNode mwdNode;
    DefaultTreeModel objectHierarchyTreeModel;
    TreeComboBox objectHierarchyComboBox;
    ListEntry currentSelection = null;
    JMenuBar menuBar;
    JMenuItem refreshItem, customizerItem, addComponent, removeComponent;
    JCheckBoxMenuItem objectHierarchyItem, componentHierarchyItem, uiHierarchyItem;
    JMenu propertyTypeMenu;
    JRadioButtonMenuItem readPropertiesItem, readWritePropertiesItem, allPropertiesItem;
    JRadioButtonMenuItem expertPropertiesItem, basicPropertiesItem, preferredPropertiesItem;
    JRadioButtonMenuItem boundPropertiesItem, constrainedPropertiesItem, hiddenPropertiesItem;
    javax.swing.ButtonGroup radioButtonMenuItemGroup = new javax.swing.ButtonGroup();

    ComponentLocationDialog compLocationDialog = null;
    Object container;
    /* 'cacheIntrospectionResults' enables or disables caching of the results of the introspection
     * for these nodes that have been visited. Because caching requires a lot of memory
     * (approximately 600 Kb per node), it is disabled by default.
     */
    boolean cacheIntrospectionResults = false;
    /* 'reselect' forces reselection of a node in the ObjectHierarchyComboBox, no matter
     * if this object was already selected. Basically it cancels checking whether the
     * object to be selected is the already selected object itself. This is needed when
     * there is only one component in the microworld and its properties have to be refreshed.
     */
    private boolean reselect = false;
    /* The handle of the component currently being introspected. If the instrospected component
     * is a nested component in a component hierarchy whose root is an ESlatePart, then 'activeHandle'
     * stores the handle of the root.
     */
    ESlateHandle activeHandle = null;
    /* The component which is currently introspected */
    Object activeComponent = null;
    /* The name of the component which is currently introspected */
    String componentName = null;
    /* The hierarchical path to the component which is currently introspected. This path contains
     * all the methods that need to be invoked and/or the names of the AWT components which have to
     * be visited in order to reach the introspected component, no matter where ir resides in the
     * component hierarchy.
     */
    HierarchicalComponentPath2 componentPath = null;
    /* This property is set while the properties of the active component are refreshed. */
    private boolean refreshingProperties = false;
    /* Ignore any ActionEvents that arrive to the objectHierarchyCompoBox, while this
     * flag is set. This way several unecessary events are missed, resulting into
     * faster introspection.
     */
    boolean ignoreSelectionChange = false;
    /* Adjusts whether addSecondLevelNode() will update the BeanInfoDialog with the
     * properties/events of the supplied component, or the request will be ignored.
     */
    boolean ignoreActivation = false;
    /* This variable is set while the ObjectHierarchyTree is not initialized.
     */
    private boolean treeNotInitialized = true;
    /** The following 3 boolean attributes control which parts of the Component editor are available
     *  for use. The 3 parts are the property editor, the event editor and the sound editor
     */
    boolean propertyEditorEnabled = true, eventEditorEnabled = true, soundEditorEnabled = true;


    /* The 'container' and 'handle' arguments are needed only by the event editor. The property editor
     * can and will work, even if a null 'container' and 'handle' are supplied. If the user wants to
     * use the property editor alone, the only thing required is a null 'container' argument.
     */
    public BeanInfoDialog(Object container) { //, Object handle, Object compo, String compoName) {
        super(true);

        setLayout(new BorderLayout(0,2));
        this.container = container;

        // This is the proper time for the property editor classes to be registered
        ((ESlateComposer) container).registerPropertyEditors();

        customPropertyTypeListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = CUSTOM_PROPERTY_TYPE;
                propertyCategory = ((JRadioButtonMenuItem) e.getSource()).getText();
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        };

        locale = Locale.getDefault();
        beanInfoDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.BeanInfoDialogBundle", locale);
        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
            localeIsGreek = true;

/*JP        if (compo != null)
            setTitle(beanInfoDialogBundle.getString("DialogTitle") + " - " + compoName);
        else
            setTitle(beanInfoDialogBundle.getString("DialogTitle"));
*/
        propertyEditorPanel = new PropertyEditorPanel(this);
/*        propertyEditorPanel.setLayout(new BoxLayout(propertyEditorPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(propertyEditorPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
*/
//        emptyPropertyEditorPanel();
//        removeAllSecondLevelNodes();

        // The node for this object in the object hierarchy tree.
//        mwdNode = new ObjectHierarchyTreeNode(((ESlateContainer) container).microworld, beanInfoDialogBundle.getString("Microworld"), this);
//        mwdNode.accessorMethodName = "getMicroworld";
//        mwdNode.objectHierarchyNode = true;
//        objectHierarchyTreeModel = new DefaultTreeModel(mwdNode);

        /* Create the menu bar.
         */
        menuBar = createMenuBar();

        /* Create the nested objects combo box.
         */
//        emptyDialog = true;
        initializeHierarchyBox(((ESlateComposer) container).getMicroworld());
        objectHierarchyComboBox.setPreferredSize(new Dimension(Short.MAX_VALUE, 20));

        ComboPanel comboBoxPanel = new ComboPanel(this);
        comboBoxPanel.setLayout(new BorderLayout());
        comboBoxPanel.add(objectHierarchyComboBox, BorderLayout.CENTER);

        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout()); //BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//JP        mainPanel.add(menuBar);
//JP        setJMenuBar(menuBar);
        mainPanel.add(comboBoxPanel, BorderLayout.NORTH);

        propertyEventTabs = new JTabbedPane();
        propertyEditorPanel.setName(beanInfoDialogBundle.getString("Properties"));
        propertyEventTabs.add(propertyEditorPanel);

        /* Create the event editor panel */
        if (container != null) {
            try{
                Class containerClass = Class.forName("gr.cti.eslate.base.container.ESlateContainer");
                Class handleClass = Class.forName("gr.cti.eslate.base.ESlateHandle");
                eventEditorPanel = new ObjectEventPanel((ESlateComposer) container, null, null, null);
                soundEventPanel = new SoundEventPanel((ESlateContainer) container);
                eventEditorPanel.setAlignmentX(LEFT_ALIGNMENT);
                eventEditorPanel.setName(beanInfoDialogBundle.getString("Events"));
                propertyEventTabs.add(eventEditorPanel);
                soundEventPanel.setName(beanInfoDialogBundle.getString("Sounds"));
                propertyEventTabs.add(soundEventPanel);
            }catch (ClassNotFoundException exc) {}
        }
        /* Whenever the active tab changes, the active object is introspected for events or
         * properties. This happens because each time only one of the Properties-Events tabs is
         * visible. So each time only the properties or the events of the actibe object are known.
         * When the other tab is selected a new introspection is needed to explore the other side
         * of the object.
         */
        propertyEventTabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateIntrospectedObject(true, propertyEventTabs.getSelectedIndex());
            }
        });


        mainPanel.add(propertyEventTabs, BorderLayout.CENTER);

        MyRootPane rootPane = new MyRootPane();
        rootPane.getContentPane().setLayout(new BorderLayout());
        rootPane.getContentPane().add(menuBar, BorderLayout.NORTH);
        rootPane.getContentPane().add(mainPanel, BorderLayout.CENTER);
        rootPane.setDefaultButton(null);

        add(rootPane, BorderLayout.CENTER);
//        add(menuBar, BorderLayout.NORTH);
//        add(mainPanel, BorderLayout.CENTER);

        createComponentListener();
//JP        createWindowListener();

        /* Create the listener of the object hierarchy combo box.
         */
        createComboBoxListeners();
        selectTopLevelNode();
//        currentSelection = mwdNode.entry;

/*        if (compo != null) {

            if (ESlateContainer.class.isAssignableFrom(container.getClass())) {
                ESlateContainer cont = (ESlateContainer) container;
                Array handles = cont.eSlateHandles;
                Object[] compos = new Object[handles.size()-1];
                String[] compoNames = new String[handles.size()-1];
                int k=0;
                for (int i=0; i<handles.size(); i++) {
                    ESlateHandle h = (ESlateHandle) handles.at(i);
                    if (h.getComponent() == compo) {
                        k=1;
                        continue;
                    }
                    compos[i-k] = h.getComponent();
                    compoNames[i-k] = h.getComponentName();
                }
                setSecondLevelNodes(compos, compoNames);
            }
            addSecondLevelNode(compo, compoName);
        }
*/
    }

    /* This method discovers all the properties of the object attached to the given node.
     * findBeanProperties() is called only from updateIntrospectedObject(), which
     * is only called when the selected item of the ObjectHierarchyComboBox changes.
     * So the policy is that an object does not get introspected for properties, unless
     * this is really needed, i.e. it is selected in the above combo box. The combo box's
     * popup has to be invisible for updateIntrospectedObject() to be called. So
     * the navigation of the tree structure of nodes, does not cause object introspection
     * though the selected node changes. This policy is imposed my the fact that object
     * instrospection is a heavy operation.
     */
    public void findBeanProperties(ObjectHierarchyTreeNode node) {
        if (treeNotInitialized) return;
        PropertyDescriptor[] propDescriptors = null;
        final Object introspectedObj = node.getUserObject();
//        System.out.println("findBeanProperties: " + introspectedObj);
        try{

            Class introspectedObjClass = introspectedObj.getClass();
//            System.out.println("compoClass: " + compoClass +", compoClass.getSuperclass(): " + compoClass.getSuperclass());
/*            String[] paths = Introspector.getBeanInfoSearchPath();
            Introspector.setBeanInfoSearchPath(new String[]
                                            {"javax.swing"});
*/
//            for (int i=0; i<paths.length; i++)
//                System.out.println("path: " + paths[i]);
            BeanInfo objInfo = BeanInfoFactory.getBeanInfo(introspectedObjClass); //, compoClass.getSuperclass());
//            System.out.println("introspectedObjClass: " + introspectedObjClass.getName() + ",  objInfo: " + objInfo);
            node.customizerClass = objInfo.getBeanDescriptor().getCustomizerClass();

            propDescriptors = objInfo.getPropertyDescriptors();
//            System.out.println("objInfo: " + objInfo); // + ", " + (millis2-millis1));
//            System.out.println("propDescriptors.length: " + propDescriptors.length);

            /* Sort the property descriptors based on the display names of the properties.
             */
            com.objectspace.jgl.OrderedMap propDescriptorsMap = new com.objectspace.jgl.OrderedMap(new com.objectspace.jgl.LessString());
            for (int i=0; i<propDescriptors.length; i++) {
//System.out.println("propDescriptors[i].getDisplayName(): " + propDescriptors[i].getDisplayName());
                propDescriptorsMap.add(propDescriptors[i].getDisplayName(), propDescriptors[i]);
            }
            /* If the introspected object is a java.awt.Component descendant and the
             * java.beans.Introspector has not generated Propertydescriptors for the
             * 'bounds' and 'location' properties, then create those descriptors here and
             * and them to the list of the bean's descriptors.
             */
            int extraDescriptors = 0;
            if (java.awt.Component.class.isAssignableFrom(introspectedObjClass)) {
                if (propDescriptorsMap.count("bounds") == 0) {
                    PropertyDescriptor pd = new PropertyDescriptor(
                                                    "bounds",
                                                    introspectedObjClass,
                                                    "getBounds",
                                                    "setBounds");
                    pd.setBound(true);
                    pd.setExpert(true);
                    pd.setDisplayName(beanInfoDialogBundle.getString("Bounds"));
                    pd.setShortDescription(beanInfoDialogBundle.getString("BoundsTip"));
                    propDescriptorsMap.add("bounds", pd);
                    extraDescriptors++;
                }
                if (propDescriptorsMap.count("location") == 0) {
                    PropertyDescriptor pd = new PropertyDescriptor(
                                                    "location",
                                                    introspectedObjClass,
                                                    "getLocation",
                                                    "setLocation");
                    pd.setBound(true);
                    pd.setExpert(true);
                    pd.setDisplayName(beanInfoDialogBundle.getString("Location"));
                    pd.setShortDescription(beanInfoDialogBundle.getString("LocationTip"));
                    propDescriptorsMap.add("location", pd);
                    extraDescriptors++;
                }
                if (extraDescriptors != 0) {
                    propDescriptors = new PropertyDescriptor[propDescriptors.length + extraDescriptors];
                }
            }

            java.util.Enumeration propDispNames = propDescriptorsMap.keys();
            int t = 0;
            while (propDispNames.hasMoreElements()) {
                propDescriptors[t] = (PropertyDescriptor) propDescriptorsMap.get(propDispNames.nextElement());
                t++;
            }
            propDescriptorsMap = null;
        }catch (java.beans.IntrospectionException exc) {
            System.out.println("Introspection exception");
        }


        if (propDescriptors == null)
            return;

        //Create the Icon editor components
        int propDescriptorCount = propDescriptors.length;
//        System.out.println("propDescriptorCount: " + propDescriptorCount);

        node.usablePropertyDescriptors = new PropertyDescriptor[propDescriptorCount];
        node.propertyEditors = new Object[propDescriptorCount];
        node.propertyEditorComponents = new Component[propDescriptorCount];
        node.readOnlyProperty = new boolean[propDescriptorCount];
        /* The property descriptors for which no propertyEditor was located. These descriptors
         * will be gived a second chance with findNestedObjects(), i.e. the will appear in the
         * object hierarchy of this object, so that they can be introspected on their own.
         */
        Array descriptorsWithoutRegisteredPropertyEditors = new Array();

        int k=0;
        boolean objHierarchyVisible = isObjectHierarchyVisible();
//        System.out.println("objHierarchyVisible: " + objHierarchyVisible);
        int count = 0;
        for (int i=0; i<propDescriptorCount; i++) {
            /* For performance reasons skip all the properties which do not belong
             * to the property category currently displayed in the property editor.
             * Each time the category changes, the properties of the Bean have to
             * be looked for again. This check is only performed, when the hierarchy tree
             * does not display the object hierarchy. If the object hierarchy is visible,
             * then we have to check if this property has an editor, so that if it hasn't
             * a node in the introspected object's tree for this property will be created.
             * So if the object hierarchy is not displayed, there is no need to process this
             * descriptor.
             * On the other hand, when the object hierarchy is displayed, the descriptor is
             * processed, but once we know if the property has an editor or not, the processing
             * stops unless this property belongs to the currently displayed property group.
             * This happens at the next "belongsToCurrPropertyGroup()" call.
             */
//            System.out.println("i: " + i + ", propDescriptors[i].getDisplayName(): " +  propDescriptors[i].getDisplayName());
            if (!objHierarchyVisible && !belongsToCurrPropertyGroup(propDescriptors[i])) {
                continue;
            }

//            System.out.println("--> i: " + i + ", propDescriptors[i].getDisplayName(): " +  propDescriptors[i].getDisplayName());
            Class propertyEditorClass = propDescriptors[i].getPropertyEditorClass();
            Method getter = propDescriptors[i].getReadMethod();
            if (getter == null)
                continue;
            count++;

            /* Check if the property descriptor already specifies a property editor for this
             * property. If it does, then this editor is used, unless there are problem
             * instantiating it,. If no editor is specified, then use the registered editor
             * for the type of this property.
             */
            PropertyEditor pe = null;
//            System.out.println(getter.getName() + ",  propertyEditorClass: " + propertyEditorClass);
            if (propertyEditorClass != null) {
                try{
                    pe = (PropertyEditor) propertyEditorClass.newInstance();
                }catch (InstantiationException exc) {
                    System.out.println("InstantiationException while creating an instance of the editor class \"" + propertyEditorClass.getName() + "\". The default editor for this property type will be used, if one exists");
                }catch (IllegalAccessException exc) {
                    System.out.println("IllegalAccessException while creating an instance of the editor class \"" + propertyEditorClass.getName() + "\". The default editor for this property type will be used, if one exists");
                }catch (Throwable thr) {
//                    System.out.println("Exception: " + thr + ", " + thr.getMessage());
                    thr.printStackTrace();
                    System.out.println("Exception while creating an instance of the editor class \"" + propertyEditorClass.getName() + "\". The default editor for this property type will be used, if one exists");
                }
            }
            if (pe == null) {
                Class propertyType = getter.getReturnType();
                /* Find the proper property editor for the class of the property. If the properties
                 * type is primitive, then search fot its equivalent class.
                 */
                if (propertyType == null)
                    continue;
                if (propertyType.getName().equals("int")) {
                    propertyType = Integer.class;
                }
                if (propertyType.getName().equals("boolean")) {
                    propertyType = Boolean.class;
                }
                if (propertyType.getName().equals("double")) {
                    propertyType = Double.class;
                }
                if (propertyType.getName().equals("float")) {
                    propertyType = Float.class;
                }
                if (propertyType.getName().equals("long")) {
                    propertyType = Long.class;
                }
                if (propertyType.getName().equals("short")) {
                    propertyType = Short.class;
                }

                pe = java.beans.PropertyEditorManager.findEditor(propertyType);
            }

            if (pe == null) {
                descriptorsWithoutRegisteredPropertyEditors.add(propDescriptors[i]);
                continue;
            }else{
                try{
                    /* For performancwe reasons skip all the properties which do not belong
                     * to the property category currently displayed in the property editor.
                     * Each time the category changes, the properties of the Bean have to
                     * be looked for again.
                     */
                    if (!belongsToCurrPropertyGroup(propDescriptors[i]))
                        continue;

                    node.usablePropertyDescriptors[k] = propDescriptors[i];
                    node.propertyEditors[k] = pe;

                    /* If this editor is the editor for int/Integer values, then we
                     * have to call its init() method, which initializes ther editor.
                     * The init() method checks if the property's values are enumerated.
                     * If they are, then editing is delegated to a instance of the
                     * EnumeratedValuesEditor. Otherwise the IntPropertyEditor itself
                     * handles the property editing.
                     */
                    if (IntPropertyEditor.class.isAssignableFrom(node.propertyEditors[k].getClass()))
                        ((IntPropertyEditor) node.propertyEditors[k]).init(node.usablePropertyDescriptors[k]);

                    Object val = getter.invoke(introspectedObj, new Object[0]);
//                    System.out.println("getter: " + getter +", val: " + val);
//-                    if (val != null) {
//-                        node.usablePropertyDescriptors[k].setValue(node.usablePropertyDescriptors[k].getName(), val);
//-                    }
//                    System.out.println("isInstance: " + PropertyEditorSupport.class.isInstance(node.propertyEditors[k]));
                    if (PropertyEditorSupport.class.isInstance(node.propertyEditors[k])) {
                        PropertyEditorSupport editor = (PropertyEditorSupport) node.propertyEditors[k];
//                        System.out.println("propertyEditors[k]: " + node.propertyEditors[k] + ", Custom editor: " + editor.getCustomEditor() + ", isInstance component: " + Component.class.isInstance(node.propertyEditors[k]));

                        /* Initialize the property editor using either setAsText() or setValue() */
                        if (editor.getValue() != null &&
                            editor.getAsText() != null && // This implies that get/setAsText() are indeed used.
                            val != null &&
                            !editor.getValue().getClass().isAssignableFrom(val.getClass()) &&
                            String.class.isAssignableFrom(val.getClass()))
                                editor.setAsText((String) val);
                        else
                            editor.setValue(val);

                        /* In the special case of LayoutPropertyEditors, except from the value of the LayoutManager,
                         * we also need the Component.
                         */
                        if (LayoutPropertyEditor.class.isAssignableFrom(editor.getClass())) {
                            if (gr.cti.eslate.panel.PanelComponent.class.isAssignableFrom(introspectedObj.getClass()))
                                ((LayoutPropertyEditor) editor).setComponent(((PanelComponent) introspectedObj).getContentPane());
                            else
                                ((LayoutPropertyEditor) editor).setComponent((Component) introspectedObj);
                        }
                        /* Supply the Component in the case of the InsetsPropertyEditor, too. This enables direct repaint of the
                         * java.awt.Component, whose insets change.
                         */
                        if (InsetsPropertyEditor.class.isAssignableFrom(editor.getClass()))
                            ((InsetsPropertyEditor) editor).setComponent((Component) introspectedObj);
                        /* Supply the Component in the case of the BorderPropertyEditor, too. This enables the 'Apply'
                         * button in the BorderEditorDialog, which applies the current border on the component whose
                         * property is being edited. This enables border testing.
                         */
                        if (BorderPropertyEditor.class.isAssignableFrom(editor.getClass()))
                            ((BorderPropertyEditor) editor).setComponent((Component) introspectedObj);
                        /* In the case of File properties which make use of the FilePropertyEditor, the
                         * selection mode (files, directories, files and directories) and the valid file
                         * extensions can be additionally specified by the component. This info is used to
                         * further configure the FileProperyEditor.
                         */
                        if (FilePropertyEditor.class.isAssignableFrom(editor.getClass())) {
                            /* Check if the component contains a String[] get<property_name>Extensions method.
                             * If a method like this exists, then check if a second method, namely
                             * get<PropertyName>ExtensionDescriptions exists. This should provide a String
                             * array with the descriptions of the extensions. If any of these methods does not
                             * exist, then no extensions are passed to the FilePropertyEditor.
                             */
                            String[] extensions = null, descriptions = null;
                            try{
                                String extGetterName = getter.getName() + "Extensions";
//                                System.out.println("extGetterName: " + extGetterName);
                                Method extGetter = introspectedObj.getClass().getMethod(extGetterName, null);
//                                System.out.println("extGetter: " + extGetter);
                                if (extGetter.getReturnType().equals(String[].class))
                                    extensions = (String[]) extGetter.invoke(introspectedObj, null);

                                String extDescrGetterName = getter.getName() + "ExtensionDescriptions";
                                Method extDescrGetter = introspectedObj.getClass().getMethod(extDescrGetterName, null);
                                if (extDescrGetter.getReturnType().equals(String[].class))
                                    descriptions = (String[]) extDescrGetter.invoke(introspectedObj, null);
                                ((FilePropertyEditor) editor).setExtensions(extensions);
                                ((FilePropertyEditor) editor).setDescriptions(descriptions);
                            }catch (Throwable thr) {
//                                System.out.println("----- FilePropertyEditor 1 -----");
//                                thr.printStackTrace();
                            }
                            // Check if the component contains an int get<property_name>SelectionMode method.
                            int selectionMode = javax.swing.JFileChooser.FILES_ONLY;
                            try{
                                String modeGetterName = getter.getName() + "SelectionMode";
                                Method modeGetter = introspectedObj.getClass().getMethod(modeGetterName, null);
                                if (modeGetter.getReturnType().equals(int.class))
                                    selectionMode = ((Integer) modeGetter.invoke(introspectedObj, null)).intValue();
                                ((FilePropertyEditor) editor).setSelectionMode(selectionMode);
                            }catch (Throwable thr) {
//                                System.out.println("----- FilePropertyEditor 2 -----");
//                                thr.printStackTrace();
                            }
                        }

                        Method setter = node.usablePropertyDescriptors[k].getWriteMethod();
//                        System.out.println("Setter: " +  setter);

                        // First check if the custom editor supports tags
                        String[] tags = editor.getTags();
//                        System.out.println("tags: " + tags + ", editor: " + editor + ", supportsCustomEditor: " + editor.supportsCustomEditor());
                        if (tags != null && !editor.supportsCustomEditor()) {
                            JComboBox tagBox = new JComboBox();
                            node.propertyEditorComponents[k] = tagBox;
                            for (int l=0; l<tags.length; l++)
                                tagBox.addItem(tags[l]);
                            tagBox.setSelectedItem(editor.getAsText());
//                            System.out.println("Selecting editor.getAsText(): " + editor.getAsText() + ", editor.getValue(): " + editor.getValue());
                            if (setter != null) {
                                tagBox.addItemListener(new EditorItemListener(editor, setter) {
                                    public void itemStateChanged(ItemEvent e) {
//                                        System.out.println("Editor propertyChange: " + propertySetterMethod);
                                        if (propertySetterMethod == null)
                                            return;
                                        Object[] args = new Object[1];
//                                        System.out.println("e.getItem(): " + e.getItem() + e.getItem().getClass());
                                        args[0] = e.getItem();
                                        Object newVal = e.getItem();
//                                        System.out.println("newVal: " + newVal);
//                                        System.out.println("propEditor.getValue(): " + propEditor.getValue());
                                        if (propEditor.getValue() != null &&
                                            // The following implies that get/setAsText() are indeed used.
                                            propEditor.getAsText() != null &&
                                            // The last two indicate that to user setAsText() the new value has
                                            // to be of String class and must differ from the class that getValue()
                                            // returns.
                                            !propEditor.getValue().getClass().isAssignableFrom(newVal.getClass()) &&
                                            String.class.isAssignableFrom(newVal.getClass()))
                                                propEditor.setAsText((String) newVal);
                                        else
                                            propEditor.setValue(newVal);
                                    }
                                });

                                editor.addPropertyChangeListener(new EditorPropertyChangeListener(setter) {
                                    public void propertyChange(PropertyChangeEvent e) {
//                                        System.out.println("Editor propertyChange: " + propertySetterMethod);
                                        if (propertySetterMethod == null)
                                            return;
                                        Object[] args = new Object[1];
//                                        System.out.println("Tags e.getNewValue(): " + e.getNewValue()); // + ", class: " + e.getNewValue().getClass());
                                        args[0] = e.getNewValue();
                                        try{
                                            propertySetterMethod.invoke(introspectedObj, args);
                                        }catch (IllegalAccessException exc) {
                                            System.out.println("IllegalAccessException. Unable to set property: " +  e.getPropertyName());
                                        }catch (java.lang.reflect.InvocationTargetException exc) {
                                            System.out.println("InvocationTargetException. Unable to set property: " +  e.getPropertyName() + " --- " + exc.getMessage());
                                        }catch (IllegalArgumentException exc) {
                                            System.out.println("IllegalArgumentException. Unable to set property: " +  e.getPropertyName() + " --- " + exc.getMessage());
                                        }
                                    }
                                });
                                node.readOnlyProperty[k] = false;
                            }else
                                node.readOnlyProperty[k] = true;

                        }else{
                            if (editor.supportsCustomEditor()) {
                                node.propertyEditorComponents[k] = editor.getCustomEditor();
                                /* Hook PropertyChangeListeners so that the editor will notify us
                                 * when a property changes.
                                 */
                                if (setter != null) {
                                    editor.addPropertyChangeListener(new EditorPropertyChangeListener(setter) {
                                        public void propertyChange(PropertyChangeEvent e) {
//                                            System.out.println("Editor propertyChange: " + propertySetterMethod);
                                            if (propertySetterMethod == null)
                                                return;
                                            Object[] args = new Object[1];
//                                            System.out.println("e.getNewValue(): " + e.getNewValue()); // + ", class: " + e.getNewValue().getClass());
                                            args[0] = e.getNewValue();
                                            try{
                                                propertySetterMethod.invoke(introspectedObj, args);
                                            }catch (IllegalAccessException exc) {
                                                System.out.println("IllegalAccessException. Unable to set property: " +  e.getPropertyName());
                                            }catch (java.lang.reflect.InvocationTargetException exc) {
                                                System.out.println("InvocationTargetException. Unable to set property: " +  e.getPropertyName() + " --- " + exc.getMessage());
                                            }catch (IllegalArgumentException exc) {
                                                System.out.println("IllegalArgumentException. Unable to set property: " +  e.getPropertyName() + " --- " + exc.getMessage());
                                            }
                                        }
                                    });
                                    node.readOnlyProperty[k] = false;
                                }else
                                    node.readOnlyProperty[k] = true;
                            }else
                                continue;
                        }
                    }else{
                        continue;
                    }
                    k++;
                }catch (IllegalAccessException exc) {
//                    exc.printStackTrace();
                    System.out.println("Cannot instantiate editor: " +  propertyEditorClass);
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    System.out.println("Cannot get component value for property: " + node.usablePropertyDescriptors[k].getDisplayName());
                }
            }
        }
//        System.out.println("Processed : " + count + " properties. k: " + k);
        // Now we have the actual number of properties that can be displayed
        int propertyCount = k;
//        System.out.println("node: " + node.objectName + ", propertyCount : " + propertyCount + ", node.usablePropertyDescriptors.length: " + node.usablePropertyDescriptors.length);
        /* Truncate the "usablePropertyDescriptors", "propertyEditors"
         * and "propertyEditorComponents" arrays.
         */
        if (propertyCount == 0) {
            node.usablePropertyDescriptors = new PropertyDescriptor[0];
            node.propertyEditors = new Object[0];
            node.propertyEditorComponents = new Component[0];
//?            Objectode.readOnlyProperty = new boolean[0];
            node.readOnlyProperty = new boolean[0];
//            return;
        }else{
//?         Proper[] tmp2 = node.propertyEditors;
            PropertyDescriptor[] tmp1 = node.usablePropertyDescriptors;
            Object[] tmp2 = node.propertyEditors;
            Component[] tmp3 = node.propertyEditorComponents;
            boolean[] tmp4 = node.readOnlyProperty;
            node.usablePropertyDescriptors = new PropertyDescriptor[propertyCount];
            node.propertyEditors = new Object[propertyCount];
            node.propertyEditorComponents = new Component[propertyCount];
            node.readOnlyProperty = new boolean[propertyCount];
            for (int i=0; i<propertyCount; i++) {
                node.usablePropertyDescriptors[i] = tmp1[i];
                node.propertyEditors[i] = tmp2[i];
                node.propertyEditorComponents[i] = tmp3[i];
                node.readOnlyProperty[i] = tmp4[i];
            }
            tmp1 = null; tmp2 = null; tmp3 = null; tmp4 = null;
        }

        /* Find all the custom property categories declared by the properties of this component.
         * If the PropertyDescriptor is an instance of the 'ExtendedPropertyDescriptor'
         * class, then check if any custom property categories should be created in the
         * 'Properties-->Type' JMenu. Fill the 'propertyCategories' Array with the property
         * types of all the component's properties.
         * 'ExtendedPropertyDescriptor' were declared for the sole purpose of defining custom
         * property categories.
         */
        if (!node.introspected) {
            for (int i=0; i<propertyCount; i++) {
                try{
                    String cat = (String) node.usablePropertyDescriptors[i].getValue("propertyCategory");
//                    System.out.println("getValue(): " + cat);
                    if (cat != null && node.propertyCategories.indexOf(cat) == -1)
                        node.propertyCategories.add(cat);
                }catch (Exception exc) {}
/*                if (ExtendedPropertyDescriptor.class.isAssignableFrom(node.usablePropertyDescriptors[i].getClass())) {
                    String cat = ((ExtendedPropertyDescriptor) node.usablePropertyDescriptors[i]).getCategory();
                    System.out.println("getCategory(): " + cat);
                    if (cat != null && node.propertyCategories.indexOf(cat) == -1)
                        node.propertyCategories.add(cat);
                }
*/
            }
        }
        node.descriptorsWithoutRegisteredPropertyEditors = new PropertyDescriptor[descriptorsWithoutRegisteredPropertyEditors.size()];
        node.objHierarchyUnchecked = true;
        for (int i=0; i<descriptorsWithoutRegisteredPropertyEditors.size(); i++) {
            node.descriptorsWithoutRegisteredPropertyEditors[i] = (PropertyDescriptor) descriptorsWithoutRegisteredPropertyEditors.at(i);
        }
        node.introspected = true;
        return;
    }

    /* Checks if the property whose descriptor is provided, belongs to the currently displayed
     * property category in the editor.
     */
    boolean belongsToCurrPropertyGroup(PropertyDescriptor descriptor) {
        switch (propertyDisplayMode) {
            case CUSTOM_PROPERTY_TYPE:
                if (descriptor.getValue("propertyCategory") == null //!ExtendedPropertyDescriptor.class.isAssignableFrom(descriptor.getClass())
                  || descriptor.isHidden())
                    return false;
                if (!descriptor.getValue("propertyCategory").equals(propertyCategory)) // !((ExtendedPropertyDescriptor) descriptor).getCategory().equals(propertyCategory))
                    return false;
                break;
            case READ_ONLY_PROPERTIES:
                if (descriptor.getWriteMethod() != null)
                    return false;
                break;
            case READ_WRITE_PROPERTIES:
                if (descriptor.getWriteMethod() == null || descriptor.isHidden())
                    return false;
                break;
            case EXPERT_PROPERTIES:
                if (descriptor.getWriteMethod() == null || descriptor.isHidden() || !descriptor.isExpert())
                    return false;
                break;
            case BASIC_PROPERTIES:
                if (descriptor.getWriteMethod() == null || descriptor.isHidden() || descriptor.isExpert())
                    return false;
                break;
            case BOUND_PROPERTIES:
                if (descriptor.isHidden() || !descriptor.isBound())
                    return false;
                break;
            case CONSTRAINED_PROPERTIES:
                if (descriptor.isHidden() || !descriptor.isConstrained())
                    return false;
                break;
            case HIDDEN_PROPERTIES:
                if (!descriptor.isHidden())
                    return false;
                break;
            case PREFERRED_PROPERTIES:
                if (descriptor.isHidden())
                    return false;
                if (!ExtendedPropertyDescriptor.class.isAssignableFrom(descriptor.getClass()))
                    return false;
                if (!((ExtendedPropertyDescriptor) descriptor).isPreferred())
                    return false;

                break;
        }

        return true;

    }

    public void refreshPropertyCustomCategories() {
        if (treeNotInitialized) return;
        if (currentSelection == null) return;
        ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
//        System.out.println("refreshPropertyCustomCategories()  selectedNode: " + selectedNode);
        /* First get rid of any old property categories. Remove them from the 'propertyTypeMenu', too.
         */
        while (propertyTypeMenu.getItemCount() > 9)
            propertyTypeMenu.remove(9);
        /* Insert the property categories for the selected node (current component) to the
         * 'propertyTypeMenu'.
         */
//        System.out.println("refreshPropertyCustomCategories selectedNode.propertyCategories.size(): " + selectedNode.propertyCategories.size());
        if (selectedNode.propertyCategories.size() != 0)
            propertyTypeMenu.addSeparator();
        JRadioButtonMenuItem radioItem;
        for (int i=0; i<selectedNode.propertyCategories.size(); i++) {
            radioItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem((String) selectedNode.propertyCategories.at(i)));
//            if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//                radioItem.setFont(greekUIFont);
            radioItem.addActionListener(customPropertyTypeListener);
            radioButtonMenuItemGroup.add(radioItem);
        }
        /* Check if the current 'propertyCategory' is included in the node's propertyCategories Array.
         * If not the set the 'propertyCategory' to BASIC_PROPERTIES.
         */
        if (propertyDisplayMode == CUSTOM_PROPERTY_TYPE && !selectedNode.propertyCategories.contains(propertyCategory)) {
//            System.out.println("Selecting the BASIC properties");
            basicPropertiesItem.doClick();
        }
//            propertyCategory = BASIC_PROPERTIES;
    }

    /* updateIntrospectedObject() is the basic method which performs object introspection and presents
     * the results. Because each time only one of the property/event editor is visible, the
     * 'propertyEditorPanelVisible' parameter is used to denote which of the two is visible, so that only
     * the events or properties of the active object will be updated and not both of them.
     */
    public void updateIntrospectedObject(boolean exploreProperties, int selectedTabIndex) { //boolean propertyEditorPanelVisible) {
//        System.out.println("-------------------------- updateIntrospectedObject --------------------");
//        System.out.println("currentSelection: " + currentSelection);
        if (treeNotInitialized || currentSelection == null) {
            if (selectedTabIndex == 2)
                soundEventPanel.clear();
            return;
        }
        ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object();

        /* If this is a second level node, then find its ESlatehandle */
        if (selectedNode.getLevel() == 2)
            activeHandle = ((ESlateComposer) container).getMicroworld().getESlateMicroworld().getComponentHandle(selectedNode.objectName);
        activeComponent = selectedNode.getUserObject();
        componentName = selectedNode.objectName;
//        System.out.println("selectedNode.getLevel(): " + selectedNode.getLevel());
        /* Create the hierarchical path to the currently instrospected component. This path is
         * created from info saved at each individual node.
         */
        int level = selectedNode.getLevel();
        String[] path = null;
        int[] type = null;
        int[] objectIndex = null;
        ObjectHierarchyTreeNode tn = selectedNode;
        /* Special handling if the root node (mwdNode) is selected.
         */
        if (level == 0) {
            path = new String[2];
            type = new int[2];
            objectIndex = new int[2];
            path[1] = tn.accessorMethodName;
            type[1] = HierarchicalComponentPath2.METHOD_NAME;
            objectIndex[1] = -1;
            path[0] = ((ESlateContainer) container).getESlateHandle().getComponentName();
            type[0] = HierarchicalComponentPath2.AWT_COMPONENT_NAME;
            objectIndex[0] = -1;
        }else{
            path = new String[level];
            type = new int[level];
            objectIndex = new int[level];
            for (int l=level-1; l>=0; l--) {
                String accessorMethodName = tn.accessorMethodName;
                if (accessorMethodName == null) {
                    path[l] = tn.objectName;
                    type[l] = HierarchicalComponentPath2.AWT_COMPONENT_NAME;
                }else{
                    path[l] = tn.accessorMethodName;
                    type[l] = HierarchicalComponentPath2.METHOD_NAME;
                }
                objectIndex[l] = tn.objectIndex;
    //System.out.println("l: " + l + ", tn.objectName: " + tn.objectName + ", tn.componentHierarchyNode: " + tn.componentHierarchyNode);
                /* The hierarchical path is constructed from the selected object to the closest
                 * ESlate component in the hierarchy. The closest component may be a top level
                 * component, but it may also be some nested ESlate component.
                 */
                if (tn.componentHierarchyNode && l != 0) {
    //System.out.println("IN");
                    String[] tmpPath = path;
                    int[] tmpType = type;
                    int[] tmpObjectIndex = objectIndex;
                    path = new String[level-l];
                    type = new int[level-l];
                    objectIndex = new int[level-l];
                    for (int k=0; k<path.length; k++) {
                        path[k] = tmpPath[l+k];
                        type[k] = tmpType[l+k];
                        objectIndex[k] = tmpObjectIndex[l+k];
                    }
                    break;
                }
                tn = (ObjectHierarchyTreeNode) tn.getParent();
            }
        }

        componentPath = new HierarchicalComponentPath2(path, type, objectIndex);
//System.out.println("BeanInfoDialog() componentPath: " + componentPath);

        String objectName = selectedNode.objectName;

        /* Create the object and component hierarchies for the 'selectedNode', if it hasn't already been
         * created.
         */
        boolean nodesHaveBeenCreated = false;
//        System.out.println(objectName + ", selectedNode.nestedObjectsFound: " + selectedNode.nestedObjectsFound);
        if (isComponentHierarchyVisible() && !selectedNode.hostedComponentsFound) {
            findHostedComponents(selectedNode); //introspectedObj, node);
            nodesHaveBeenCreated = true;
        }
        if (isObjectHierarchyVisible() && !selectedNode.nestedObjectsFound) {
//            System.out.println("Looking for nested objects");
            findNestedObjects(selectedNode); //introspectedObj, node);
            nodesHaveBeenCreated = true;
        }
        if (isUIHierarchyVisible() && !selectedNode._AWTchildrenFound) {
//            System.out.println("Looking for children");
            findChildUIComponents(selectedNode);
            nodesHaveBeenCreated = true;
        }
        if (nodesHaveBeenCreated)
            ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();

        refreshPropertyCustomCategories();

//        System.out.println("updateIntrospectedObject setSelectedItem");
        objectHierarchyComboBox.setSelectedItem(selectedNode.entry);

        /* If the component has already been introspected, it already exists in the object
         * hierarchy. If not, then introspect it, insert in the object hierarchy tree and
         * display its properties and its nested objects.
         */
//        System.out.println("updateIntrospectedObject --> " + "selectedNode.objectName: " + selectedNode.objectName + "selectedNode.introspected: " + selectedNode.introspected);
        long millis1, millis2;
        if (selectedTabIndex == 0 && exploreProperties && !selectedNode.introspected) {
            findBeanProperties(selectedNode);
//            selectedNode.collapse();
//            ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
            propertyEditorPanel.showProperties(selectedNode);
        }
        if (selectedTabIndex == 1 && eventEditorPanel != null) {
            /* Find the handle of the component which is the first ESlatePart we meet
             * when we traverse the hierarchy upwards starting from this object.
             */
            ObjectHierarchyTreeNode componentNode = selectedNode;
            Object topObject = componentNode.getUserObject();
            if (componentNode == mwdNode)
                topObject = ((ESlateComposer) container).getMicroworld();
            else{
                while (componentNode != null && !componentNode.componentHierarchyNode) {
                    componentNode = (ObjectHierarchyTreeNode) componentNode.getParent();
                    topObject = componentNode.getUserObject();
                }
            }
            if (topObject != null)
                eventEditorPanel.updateEventPanels(selectedNode.getUserObject(), topObject);
            else
                eventEditorPanel.updateEventPanels(null, null);
        }
        if (selectedTabIndex == 2) {// && eventEditorPanel != null) {
            SoundListener[] slisteners = ((ESlateComposer) container).getSoundListenerMap().getSoundListeners(selectedNode.getUserObject());
            ObjectHierarchyTreeNode componentNode = selectedNode;
//            Object topObject = componentNode.getUserObject();
            ESlateHandle compoHandle = null;
            if (componentNode == mwdNode)
                compoHandle = ((ESlateContainer) container).getESlateHandle();// microworld.getESlateMicroworld().getESlateHandle();
            else{
                while (componentNode != null && !componentNode.componentHierarchyNode)
                    componentNode = (ObjectHierarchyTreeNode) componentNode.getParent();
                compoHandle = ((ESlateContainer) container).getMicroworld().getESlateMicroworld().getComponentHandle(componentNode.getUserObject());
            }
            soundEventPanel.initializeSoundEvents(selectedNode.getUserObject(), compoHandle, componentPath, slisteners);
        }
        propertyEditorPanel.adjustSizes();
//        System.out.println("propertyPanels.size(): " + propertyPanels.size());
    }

    public ObjectHierarchyTreeNode getSecondLevelNode(Object object) {
        if (treeNotInitialized) return null;
        if (object == null) return null;
        int topNodeCount = mwdNode.getChildCount();
        ObjectHierarchyTreeNode objNode = null;
        for (int i=0; i < topNodeCount; i++) {
            objNode = (ObjectHierarchyTreeNode) mwdNode.getChildAt(i);
            if (objNode.getUserObject().equals(object))
                return objNode;
        }
        return null;
    }

    public ObjectHierarchyTreeNode getSecondLevelNode(String objectName) {
        if (treeNotInitialized) return null;
        if (objectName == null) return null;
        int topNodeCount = mwdNode.getChildCount();
        ObjectHierarchyTreeNode objNode = null;
        for (int i=0; i < topNodeCount; i++) {
            objNode = (ObjectHierarchyTreeNode) mwdNode.getChildAt(i);
            if (objNode.objectName.equals(objectName))
                return objNode;
        }
        return null;
    }

    public boolean isSecondLevelNode(Object object) {
        if (treeNotInitialized) return false;
        if (object == null) return false;
        int topNodeCount = mwdNode.getChildCount();
        ObjectHierarchyTreeNode objNode = null;
        for (int i=0; i < topNodeCount; i++) {
            objNode = (ObjectHierarchyTreeNode) mwdNode.getChildAt(i);
            if (objNode.getUserObject().equals(object))
                return (objNode.getLevel() == 1);
        }
        return false;
    }

    public boolean isSecondLevelNode(ObjectHierarchyTreeNode node) {
        if (treeNotInitialized) return false;
        if (node == null) return false;
        return (node.getLevel() == 1);
    }

    public int getSecondLevelNodeCount() {
        if (treeNotInitialized) return 0;
        return mwdNode.getChildCount();
    }

    public ObjectHierarchyTreeNode getSecondLevelNode(int index) {
        if (treeNotInitialized) return null;
        if (getSecondLevelNodeCount() <= index) return null;
        return (ObjectHierarchyTreeNode) mwdNode.getChildAt(index);
    }

    public Object[] getSecondLevelNodeObjects() {
        if (treeNotInitialized) return new Object[0];
        Object[] objects = new Object[getSecondLevelNodeCount()];
        for (int i=0; i<objects.length; i++)
            objects[i] = getSecondLevelNode(i).getUserObject();
        return objects;
    }

    public String[] getSecondLevelNodeObjectNames() {
        if (treeNotInitialized) return new String[0];
        String[] names = new String[getSecondLevelNodeCount()];
        for (int i=0; i<names.length; i++)
            names[i] = getSecondLevelNode(i).objectName;
        return names;
    }

    public void removeAllSecondLevelNodes() {
        if (treeNotInitialized) return;
        for (int i=0; i<getSecondLevelNodeCount(); i++) {
            ObjectHierarchyTreeNode node = getSecondLevelNode(i);
//            System.out.println("removeAllSecondLevelNodes --> Removing: " + node.objectName);
            node.removeFromParent();
            i--;
        }
        if (objectHierarchyComboBox != null)
            ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
    }

    public void setSecondLevelNodes(Object[] compos, String[] compoNames) {
        if (treeNotInitialized) return;
        if (compos == null || compoNames == null) return;

        ignoreSelectionChange = true;
        for (int i=0; i<compos.length; i++) {
            Object compo = compos[i];
            String compoName = compoNames[i];
            ObjectHierarchyTreeNode node = getSecondLevelNode(compo);
            if (node == null) {
                node = new ObjectHierarchyTreeNode(compo, compoName, this);
                node.componentHierarchyNode = true;
                node.accessorMethodName = null;
//            }
                mwdNode.add(node);
                node.collapse();
            }
        }
        ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
        ignoreSelectionChange = false;
    }

    public void addSecondLevelNode(Object compo, String compoName) {
        if (treeNotInitialized) return;
        if (compo == null) return;
        if (ignoreActivation) return;
        ignoreSelectionChange = true;
//Thread.currentThread().dumpStack();
//JP        setTitle(beanInfoDialogBundle.getString("DialogTitle") + " - " + compoName);
//        System.out.println("addSecondLevelNode()  compo: " + compo + ", compoName: " + compoName);

        /* If the component has already been introspected, it already exists in the object
         * hierarchy. If not, then introspect it and insert a new second level node for this
         * object in the object hierarchy tree.
         */
        ObjectHierarchyTreeNode node = getSecondLevelNode(compo);
//        System.out.println("addSecondLevelNode() node: " + node);
        if (node == null) {
            node = new ObjectHierarchyTreeNode(compo, compoName, this);
            node.componentHierarchyNode = true;
            node.accessorMethodName = null;
            mwdNode.add(node);

//1            findBeanProperties(node);
            /* Create the UI, object and component hierarchies for the 'node', if it hasn't already been
             * created.
             */
            if (isComponentHierarchyVisible() && !node.hostedComponentsFound) {
//                System.out.println("Looking for nested objects");
                findHostedComponents(node); //introspectedObj, node);
            }
            if (isObjectHierarchyVisible() && !node.nestedObjectsFound) {
//                System.out.println("Looking for nested objects");
                findNestedObjects(node); //introspectedObj, node);
            }
            if (isUIHierarchyVisible() && !node._AWTchildrenFound) {
//                System.out.println("Looking for children");
                findChildUIComponents(node);
            }

            node.collapse();
            ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
        }

        ignoreSelectionChange = false;
        objectHierarchyComboBox.setSelectedItem(node.entry);
        refreshPropertyCustomCategories();
    }

    /* Removes a second-level object from the object hierarchy.
     */
    public void removeSecondLevelNode(Object obj) {
        if (treeNotInitialized) return;
        ObjectHierarchyTreeNode selectedNode = null;
        if (currentSelection != null)
            selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
        ObjectHierarchyTreeNode node = getSecondLevelNode(obj);
        if (node != null) {
            node.removeFromParent();
            ignoreSelectionChange = true;
            ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
            ignoreSelectionChange = false;
            /* The revalidation above causes the mwdNode (root) to be selected. If the
             * removed component was the active one, then another one will be activated and
             * the objectHierarchyComboBox will be updated properly. However, if the removed
             * component was not the active one, then the active component of the microworld
             * won't change and therefore we have to reselect the previously selected node manually.
             */
            if (selectedNode != null && selectedNode != node)
                selectNode(selectedNode);
        }
    }

    public void showDialog(Component centerAroundComp) {
        //Show the dialog
/*JP        pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int newWidth = getSize().width;
        if (newWidth < 268) newWidth = 268;
        int newHeight = screenSize.height;
        setSize(newWidth, newHeight);

//        if (getSize().height >= screenSize.height-30) {
//            setSize(new Dimension(getSize().width, screenSize.height-30));
//        }
//        if (getSize().height < 100)
//            setSize(new Dimension(300, 600));

        int x, y;
        if (centerAroundComp == null || !centerAroundComp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = centerAroundComp.getBounds();
            Point compLocation = centerAroundComp.getLocationOnScreen();
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
        objectHierarchyComboBox.requestFocus();
        show();
*/
    }

    public void selectTopLevelNode() {
        if (treeNotInitialized) return;
        objectHierarchyComboBox.setSelectedIndex(0);
//        selectNode(mwdNode);
    }

    public void removeAllNodes() {
        if (treeNotInitialized) return;
        mwdNode.removeFromParent();
        mwdNode = null;
        treeNotInitialized = true;
        emptyPropertyEditorPanel();
        removeAllSecondLevelNodes();
        eventEditorPanel.updateEventPanels(null, null);
        soundEventPanel.clear();
        objectHierarchyComboBox.clearModel();
        objectHierarchyComboBox.setSelectedIndex(-1);
        currentSelection = null;
        if (objectHierarchyComboBox != null)
            ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
        objectHierarchyComboBox.setEnabled(false);
    }

    void initializeHierarchyBox(Microworld microworld) { //, boolean mwdIsLoading) {
//        if (mwdIsLoading)
//            ignoreSelectionChange = true;
        emptyPropertyEditorPanel();
        removeAllSecondLevelNodes();

        // The node for this object in the object hierarchy tree.
        if (microworld != null) {
            mwdNode = new ObjectHierarchyTreeNode(microworld, beanInfoDialogBundle.getString("Microworld"), this);
            mwdNode.accessorMethodName = "getMicroworld";
            mwdNode.objectHierarchyNode = true;
            objectHierarchyTreeModel = new DefaultTreeModel(mwdNode);
        }
        if (objectHierarchyComboBox == null) {
            /* Create the nested objects combo box.
             */
            if (objectHierarchyTreeModel != null) {
                objectHierarchyComboBox = new TreeComboBox(objectHierarchyTreeModel);
                treeNotInitialized = false;
            }else{
                objectHierarchyComboBox = new TreeComboBox(); //new DefaultTreeModel());
                objectHierarchyComboBox.setEnabled(false);
            }
        }else{
            if (objectHierarchyTreeModel != null) {
                objectHierarchyComboBox.setEnabled(true);
                objectHierarchyComboBox.setModel(objectHierarchyTreeModel);
                treeNotInitialized = false;
            }else{
                objectHierarchyComboBox = new TreeComboBox();//new DefaultTreeModel());
                objectHierarchyComboBox.setEnabled(false);
            }
        }

//        if (mwdIsLoading)
//            ignoreSelectionChange = false;
//        selectTopLevelNode();
    }


/*    public void selectNone(boolean emptyTree) {
        emptyPropertyEditorPanel();
        if (eventEditorPanel != null)
            eventEditorPanel.updateEventPanels(null, null);
        if (emptyTree)
            removeAllSecondLevelNodes();
        objectHierarchyComboBox.setSelectedIndex(-1);
        currentSelection = null;
    }
*/
    void emptyPropertyEditorPanel() {
        if (emptyDialog) return;

        flushIconPropertyEditorIcons();
//JP        setTitle(beanInfoDialogBundle.getString("DialogTitle"));
//        removeAllSecondLevelNodes();

        propertyEditorPanel.clear();
        emptyDialog = true;

/*        propertyEditorPanel.removeAll();
        if (propertyPanels != null)
            propertyPanels.clear();

        if (noPropertyPanel == null) {
            noPropertyPanel = new JPanel(true);
            noPropertyPanel.setLayout(new BoxLayout(noPropertyPanel, BoxLayout.Y_AXIS));

            JPanel innerNoPropertyPanel = new JPanel(true);
            innerNoPropertyPanel.setLayout(new BoxLayout(innerNoPropertyPanel, BoxLayout.X_AXIS));
            JLabel noPropertyLabel = new JLabel(beanInfoDialogBundle.getString("NoComponent"));
            Font lbFont;
            if (localeIsGreek)
                lbFont = greekUIBoldItalicFont;
            else
                lbFont = new Font(noPropertyLabel.getFont().getFamily(), Font.BOLD | Font.ITALIC, noPropertyLabel.getFont().getSize());

            noPropertyLabel.setFont(lbFont);
            FontMetrics fm = getToolkit().getFontMetrics(lbFont);
            Dimension lbDim = new Dimension(fm.stringWidth(noPropertyLabel.getText())+20, 30);
            noPropertyLabel.setMaximumSize(lbDim);
            noPropertyLabel.setMinimumSize(lbDim);
            noPropertyLabel.setPreferredSize(lbDim);
            noPropertyLabel.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
            noPropertyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            innerNoPropertyPanel.add(Box.createGlue());
            innerNoPropertyPanel.add(noPropertyLabel);
            innerNoPropertyPanel.add(Box.createGlue());
            noPropertyPanel.add(Box.createVerticalStrut(20));
            noPropertyPanel.add(innerNoPropertyPanel);
            noPropertyPanel.add(Box.createGlue());

        }

        propertyEditorPanel.add(noPropertyPanel);
        emptyDialog = true;
        scrollPane.validate();
        propertyEditorPanel.repaint();
*/
    }

    public final void setPropertyEditorEnabled(boolean enabled) {
        if (container != null && ESlateContainer.class.isAssignableFrom(container.getClass())) {
            ESlateComposer cont = (ESlateComposer) container;
            if (cont.getMicroworld() != null)
                cont.getMicroworld().checkActionPriviledge(cont.getMicroworld().isComponentPropertyMgmtAllowed(), "componentPropertyMgmtAllowed");
        }
        setPropertyEditorEnabledInternal(enabled);
    }

    final void setPropertyEditorEnabledInternal(boolean enabled) {
        if (propertyEditorEnabled == enabled) return;
        propertyEditorEnabled = enabled;
        int tabIndex = propertyEventTabs.indexOfComponent(propertyEditorPanel);
        if (!propertyEditorEnabled) {
            if (tabIndex == -1) return;
            propertyEventTabs.removeTabAt(tabIndex);
        }else{
            if (tabIndex != -1) return;
            propertyEventTabs.add(propertyEditorPanel, 0);
        }
    }

    public boolean isPropertyEditorEnabled() {
        return propertyEditorEnabled;
    }

    public final void setEventEditorEnabled(boolean enabled) {
        if (container != null && ESlateComposer.class.isAssignableFrom(container.getClass())) {
            ESlateComposer cont = (ESlateComposer) container;
            if (cont.getMicroworld() != null)
                cont.getMicroworld().checkActionPriviledge(cont.getMicroworld().isComponentEventMgmtAllowed(), "componentEventMgmtAllowed");
        }
        setEventEditorEnabledInternal(enabled);
    }

    final void setEventEditorEnabledInternal(boolean enabled) {
        if (eventEditorEnabled == enabled) return;
        eventEditorEnabled = enabled;
        int tabIndex = propertyEventTabs.indexOfComponent(eventEditorPanel);
        if (!eventEditorEnabled) {
            if (tabIndex == -1) return;
            propertyEventTabs.removeTabAt(tabIndex);
        }else{
            if (tabIndex != -1) return;
            int pos = 0;
            if (propertyEditorEnabled) pos++;
            propertyEventTabs.add(eventEditorPanel, pos);
        }
    }

    public boolean isEventEditorEnabled() {
        return eventEditorEnabled;
    }

    public final void setSoundEditorEnabled(boolean enabled) {
        if (container != null && ESlateComposer.class.isAssignableFrom(container.getClass())) {
            ESlateComposer cont = (ESlateComposer) container;
            if (cont.getMicroworld() != null)
                cont.getMicroworld().checkActionPriviledge(cont.getMicroworld().isComponentSoundMgmtAllowed(), "componentSoundMgmtAllowed");
        }
        setSoundEditorEnabledInternal(enabled);
    }

    final void setSoundEditorEnabledInternal(boolean enabled) {
        if (soundEditorEnabled == enabled) return;
        soundEditorEnabled = enabled;
        int tabIndex = propertyEventTabs.indexOfComponent(soundEventPanel);
        if (!soundEditorEnabled) {
            if (tabIndex == -1) return;
            propertyEventTabs.removeTabAt(tabIndex);
        }else{
            if (tabIndex != -1) return;
            int pos = 0;
            if (propertyEditorEnabled) pos++;
            if (eventEditorEnabled) pos++;
            propertyEventTabs.add(soundEventPanel, pos);
        }
    }

    public boolean isSoundEditorEnabled() {
        return soundEditorEnabled;
    }

    private void createComponentListener() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                propertyEditorPanel.adjustSizes();
            }
        });
    }

    public void disposed() {
        if (ESlateComposer.class.isAssignableFrom(container.getClass())) {
            ((ESlateComposer) container).removeContainerListener(((ESlateComposer) container).propertyEditorUpdater);
            ((ESlateComposer) container).propertyEventEditor = null;
        }
        emptyPropertyEditorPanel();
        if (eventEditorPanel != null)
            eventEditorPanel.updateEventPanels(null, null);
        if (soundEventPanel != null)
            soundEventPanel.clear();
        removeAllSecondLevelNodes();

        locale = null;
        beanInfoDialogBundle = null;
//        greekUIFont = null;
//        greekUIBoldFont = null;
//        greekUIBoldItalicFont = null;
        mainPanel.removeAll();
        propertyEventTabs = null;
        propertyEditorPanel.disposed();
        propertyEditorPanel = null;
        eventEditorPanel = null;
        soundEventPanel = null;
        if (objectHierarchyMenu != null)
            objectHierarchyMenu.removeAll();
        objectHierarchyMenu = null;
        objectHierarchyItem = null;
        uiHierarchyItem = null;
        addComponentItem = null;
        removeComponentItem = null;

        if (mwdNode != null)
            mwdNode.removeFromParent();
        mwdNode = null;
        objectHierarchyTreeModel = null;
        if (objectHierarchyComboBox != null)
            objectHierarchyComboBox.removeAll();
        objectHierarchyComboBox = null;
        currentSelection = null;
        menuBar.removeAll();
        menuBar = null;
        refreshItem = null;
        addComponent = null;
        removeComponent = null;
        objectHierarchyItem = null;
        componentHierarchyItem = null;
        uiHierarchyItem = null;
        if (compLocationDialog != null)
            compLocationDialog.removeAll();
        compLocationDialog = null;
        /* Invalidate the container scrollPane's highlight rectangle, so that
         * the component edited by the Property Editor, won't be highlighted any more.
         */
        ((ESlateComposer) container).getScrollPane().invalidateHighlightRect();
        container = null;
    }

/*JP    private void createWindowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
//                System.out.println("BeanInfoDialog Window listener invoked");
                dispose();
                emptyPropertyEditorPanel();
                if (eventEditorPanel != null)
                    eventEditorPanel.updateEventPanels(null, null);

                locale = null;
                beanInfoDialogBundle = null;
                greekUIFont = null;
                greekUIBoldFont = null;
                greekUIBoldItalicFont = null;
                mainPanel.removeAll();
                scrollPane.removeAll();
                propertyEventTabs = null;
                if (propertyPanels != null)
                    propertyPanels.clear();
                propertyPanels = null;
                propertyEditorPanel = null;
                eventEditorPanel = null;
                if (objectHierarchyMenu != null)
                    objectHierarchyMenu.removeAll();
                objectHierarchyMenu = null;
                objectHierarchyItem = null;
                uiHierarchyItem = null;
                addComponentItem = null;
                removeComponentItem = null;
                if (noPropertyPanel != null)
                    noPropertyPanel.removeAll();
                noPropertyPanel = null;
                if (mwdNode != null)
                    mwdNode.removeFromParent();
                mwdNode = null;
                objectHierarchyTreeModel = null;
                if (objectHierarchyComboBox != null)
                    objectHierarchyComboBox.removeAll();
                objectHierarchyComboBox = null;
                currentSelection = null;
                menuBar.removeAll();
                menuBar = null;
                refreshItem = null;
                addComponent = null;
                removeComponent = null;
                objHierarchyItem = null;
                compHierarchyItem = null;
                if (compLocationDialog != null)
                    compLocationDialog.removeAll();
                compLocationDialog = null;
                container = null;
            }
        });
    }
*/
    public void createComboBoxListeners() {
//        if (treeNotInitialized) return;
        objectHierarchyComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreSelectionChange) return;
//                System.out.println("objectHierarchyComboBox.addActionListener() --> objectHierarchyComboBox.isPopupVisible(): " + objectHierarchyComboBox.isPopupVisible());
                /* When dragging a component, a needless action event is delivered, before the
                 * proper action event for the selection of the component being moved. The
                 * following if-statement filters out the needless event.
                 */
                if (WinComboBoxUI.class.isAssignableFrom(objectHierarchyComboBox.ui.getClass())) {
                    if (((WinComboBoxUI) objectHierarchyComboBox.ui).draggingCompo) return;
                }

                ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
                if (objectHierarchyComboBox.getSelectedIndex() == -1)
                    return;
                Object selectedEntryObject = null;
                if (selectedEntry != null)
                    selectedEntryObject = ((ObjectHierarchyTreeNode) selectedEntry.object).getUserObject();
                Object currentSelectionObject = null;
                if (currentSelection != null)
                    currentSelectionObject = ((ObjectHierarchyTreeNode) currentSelection.object).getUserObject();

                if (currentSelectionObject == selectedEntryObject)
                    return;
//                System.out.println("selectedEntryObject: " + selectedEntryObject.getClass());
/*                System.out.println("reselect == false && selectedEntryObject == currentSelectionObject : " + (reselect == false && selectedEntryObject == currentSelectionObject));
                if (reselect == false && selectedEntryObject == currentSelectionObject) {
                    System.out.println("In here " + "reallySelected: " + ((WinComboBoxUI) objectHierarchyComboBox.ui).reallySelected);
                    if (WinComboBoxUI.class.isAssignableFrom(objectHierarchyComboBox.ui.getClass())) {
                        if (!((WinComboBoxUI) objectHierarchyComboBox.ui).reallySelected)
                            return;
                    }
                }
System.out.println("Here 1");
Thread.currentThread().dumpStack();
*/
                /* Do not allow the selection of the top level node, cause this is an empty
                 * and 'fake' node.
                 */
//System.out.println(", selectedEntry == mwdNode.entry: " + (selectedEntry == mwdNode.entry));
/*                if (selectedEntry == mwdNode.entry) {
                    System.out.println("ROOT node selected");
//                    objectHierarchyComboBox.setSelectedItem(currentSelection);
                    ObjectHierarchyTreeNode previousSelectedNode = null;
                    if (currentSelection != null && currentSelection.object != null)
                        previousSelectedNode = (ObjectHierarchyTreeNode) currentSelection.object;

                    if (!cacheIntrospectionResults && previousSelectedNode != null) {
                        previousSelectedNode.introspected = false;
                        previousSelectedNode.propertyEditorComponents = null;
                        previousSelectedNode.usablePropertyDescriptors = null;
                        previousSelectedNode.descriptorsWithoutRegisteredPropertyEditors = null;
                        previousSelectedNode.objHierarchyUnchecked = true;
                        previousSelectedNode.readOnlyProperty = null;
                        previousSelectedNode.propertyEditors = null;
                        previousSelectedNode.pathToRoot = null;
                        previousSelectedNode.propertyCategories.clear();
                        previousSelectedNode.customizerClass = null;
                    }
                    return;

                }
*/
                Object previousSelectedObject = null;
                ObjectHierarchyTreeNode previousSelectedNode = null;
                if (currentSelection != null && currentSelection.object != null) {
                    previousSelectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                    previousSelectedObject = ((ObjectHierarchyTreeNode) currentSelection.object).getUserObject();
                }

                currentSelection = selectedEntry;
//System.out.println("selectedEntry: " + selectedEntry);
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) selectedEntry.object;
//System.out.println("JUST SELECTED: " + selectedNode.objectName);

                /* Repaint the previously selected component so that the GlassPane will get emptied. */
//                if (previousSelectedObject != null && JComponent.class.isAssignableFrom(previousSelectedObject.getClass())) {
//                    JComponent compo = (JComponent) previousSelectedObject;
//                    compo.repaint();
//                }
                if (!cacheIntrospectionResults && previousSelectedNode != null && previousSelectedNode != selectedNode) {
                    previousSelectedNode.introspected = false;
//                    previousSelectedNode._AWTchildrenFound = false;
//                    previousSelectedNode.nestedObjectsFound = false;
                    previousSelectedNode.propertyEditorComponents = null;
                    previousSelectedNode.usablePropertyDescriptors = null;
                    previousSelectedNode.descriptorsWithoutRegisteredPropertyEditors = null;
                    previousSelectedNode.objHierarchyUnchecked = true;
                    previousSelectedNode.readOnlyProperty = null;
                    previousSelectedNode.propertyEditors = null;
                    previousSelectedNode.pathToRoot = null;
//    previousSelectedNode.sizeEditor = null;
//    Object locationEditor = null;
//    Object locationOnScreenEditor = null;
//    Object boundsEditor = null;
//    ComponentListener listener = null;
                    previousSelectedNode.propertyCategories.clear();
                    previousSelectedNode.customizerClass = null;
                }

                /* Find the top level ESlate component the selected object belongs to
                 * and make it the active one in the microworld.
                 */
                if (!refreshingProperties) {
                    ObjectHierarchyTreeNode compoNode = selectedNode;
                    while (compoNode != null && !compoNode.componentHierarchyNode)
                        compoNode = (ObjectHierarchyTreeNode) compoNode.getParent();
                    if (compoNode != null) {
                        Object component = compoNode.getUserObject();
                        ESlateHandle h = ((ESlateComposer) container).getMicroworld().getESlateMicroworld().getComponentHandle(component);
                        ESlateHandle mwdHandle = ((ESlateComposer) container).getMicroworld().getESlateMicroworld().getESlateHandle();
                        if (h != null) {
                            // Activate the handle of the component in the microworld's golbal handle group.
                            // This will result in the component being activated in every group it may belong
                            // to, i.e. the group of components of a PanelComponent.
                            ((ESlateComposer) container).getMicroworld().getGlobalActivationHandleGroup().setActiveHandle(h);
                            while (h.getParentHandle() != mwdHandle)
                                h = h.getParentHandle();
                            DesktopItem item = ((ESlateComposer) container).getMicroworldComponentIndex().getComponent(h.getComponentName()).getDesktopItem();
                            if (item != null) {
                                try{
                                    /* De-iconify and activate the frame, if it is iconified or inactive */
                                    /* When de-iconifying a component or activating it, the ESlateComposer
                                     * tries to update the BeanInfoDialog with the properties/events of the
                                     * activated component. This is not necessary, when the activation is
                                     * triggered from here. Also this is wrong, when the BeanInfoDialog
                                     * is set to display the properties/events of a nested component and
                                     * as a result the top-level component is activated.
                                     */
                                    ignoreActivation = true;
                                    if (item.isIcon())
                                        item.setIcon(false);
                                    if (!item.isActive())
                                        item.setActive(true);
                                    ignoreActivation = false;
                                }catch (Exception exc) {}
                            }
                        }
                    }
                }

                /* Paint the Glasspane border for the selected component. */
                if (selectedNode != null && Component.class.isAssignableFrom(selectedNode.getUserObject().getClass())) {
                    Object selectedObject =  selectedNode.getUserObject();
                    if (Component.class.isAssignableFrom(selectedObject.getClass())) {
                        final Component compo = (Component) selectedObject;
                        ESlateInternalFrame fr = (ESlateInternalFrame) SwingUtilities.getAncestorOfClass(ESlateInternalFrame.class, compo);
                        Rectangle bounds = compo.getBounds();
                        bounds.x = bounds.x - 1;
                        bounds.y = bounds.y - 1;
                        ESlateComposer cont = (ESlateComposer) container;
                        Rectangle r = SwingUtilities.convertRectangle(compo.getParent(), bounds, cont.getScrollPane());
//                        System.out.println("1. r: " + r);
                        /* If 'compo' exceeds the limits of the InternalFrame it is contained within, or if it is
                         * totally invisible, then only the visible part should be drawn.
                         */
                        if (fr != null && !refreshingProperties) {
                            Rectangle frBounds = SwingUtilities.convertRectangle(fr.getParent(), fr.getBounds(), cont.getScrollPane());
//                            System.out.println("1. frBounds: " + frBounds);
                            r = SwingUtilities.computeIntersection(frBounds.x, frBounds.y, frBounds.width, frBounds.height, r);
//                            System.out.println("2 r: " + r);

                            javax.swing.JScrollBar vbar = cont.getScrollPane().getVerticalScrollBar();
                        }

                        Rectangle redrawRect = r.union(cont.getHighlightRect());
                        redrawRect.x = redrawRect.x-1;
                        redrawRect.y = redrawRect.y-1;
                        redrawRect.width = redrawRect.width+2;
                        redrawRect.height = redrawRect.height+2;
                        cont.setHighlightRect(r);
                        cont.getScrollPane().repaint(redrawRect.x, redrawRect.y, redrawRect.width, redrawRect.height);
/*                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                JInternalFrame fr = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, compo);
                                if (fr != null) {
                                    Component comp = fr.getRootPane().getGlassPane();
                                    Rectangle rect = SwingUtilities.convertRectangle(compo.getParent(), compo.getBounds(), comp);
//                                    System.out.println("rect: " + rect);
                                    java.awt.Graphics gr = comp.getGraphics();
                                    gr.setColor(Color.yellow);
                                    gr.drawRect(rect.x, rect.y, rect.width-1, rect.height-1);
                                }
                            }
                        });
*/
                    }else{
                        if (!((ESlateComposer) container).getHighlightRect().isEmpty()) {
                            ((ESlateComposer) container).getScrollPane().invalidateHighlightRect();
                            ((ESlateComposer) container).getScrollPane().repaint();
                        }
                    }
                }else{
                    if (!((ESlateComposer) container).getHighlightRect().isEmpty()) {
                        ((ESlateComposer) container).getScrollPane().invalidateHighlightRect();
                        ((ESlateComposer) container).getScrollPane().repaint();
                    }
                }
//                System.out.println("ObjectHierarchyComboBox action listener  Selecting: " + selectedNode);
//                System.out.println("selectedNode.introspected: " + selectedNode.introspected);
                /* 'updateIntrospectedObject()' will perform introspection of the object
                 * behind the selected node, only if the pop-up of the Combobox is invisible. This
                 * disables object introspection while traversing the object/component hierarchy. Only
                 * the last selected object (this selection closes the pop-up) will be introspected.
                 * So as the user navigates the component/object hierarchy the selected node changes
                 * whenever he expands/collapses any node. However the expanded/collapsed nodes do
                 * not get introspected. This speeds up the hierarchy traversal. This navigation
                 * does not always end up by selecting a child node. It may end by poping up some other
                 * pop up menu. In these cases, if the selected node is not introspected the propertyEditorPanel
                 * will appear empty.
                 */
                if (!selectedNode.introspected) {
//                    System.out.println("updateIntrospectedObject... " + !objectHierarchyComboBox.isPopupVisible() + ", selectedNode: " + selectedNode.objectName);
                    updateIntrospectedObject(!objectHierarchyComboBox.isPopupVisible(), propertyEventTabs.getSelectedIndex());
                }
            }
        });

        // Right mouse listener of the combo box.
        objectHierarchyComboBox.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e))
                    return;

                showObjectHierarchyMenu(e.getX(), e.getY());
            }
        });
    }

    /* Sets the 'nestedObjectFound' flag of all the nodes of the tree to the specified value */
    void setNestedObjectFoundFlag(ObjectHierarchyTreeNode node, boolean found) {
        if(node.isLeaf()) {
//            System.out.println("Setting nestedObjectsFound to " + found + " for " + node.objectName);
            node.nestedObjectsFound = found;
            return;
        }else{
            int count = node.getChildCount();
            for (int i=0; i<count; i++) {
                ObjectHierarchyTreeNode nd = (ObjectHierarchyTreeNode) node.getChildAt(i);
                setNestedObjectFoundFlag(nd, found);
            }
            node.nestedObjectsFound = found;
//            System.out.println("Setting nestedObjectsFound to " + found + " for " + node.objectName);
        }
    }

    public void setComponentHierarchyVisible(boolean visible) {
        if (treeNotInitialized) return;
        ObjectHierarchyTreeNode selectedNode = null;
        TreeComboBox.TreeToListModel treeModel = (TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel();
        treeModel.displayComponentHierarchy = visible;
        if (currentSelection != null) {
            selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
            if (selectedNode != null && selectedNode != mwdNode && !selectedNode.hostedComponentsFound)
                findHostedComponents(selectedNode);
        }
        treeModel.revalidate();
        if (selectedNode != null)
            selectNode(selectedNode);
    }

    public boolean isComponentHierarchyVisible() {
        if (treeNotInitialized) return false;
        TreeComboBox.TreeToListModel treeModel = (TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel();
        return treeModel.displayComponentHierarchy;
    }

    public void setObjectHierarchyVisible(boolean visible) {
        if (treeNotInitialized) return;
//        System.out.println("currentSelection: " + currentSelection.object);
        ObjectHierarchyTreeNode selectedNode = null;
        TreeComboBox.TreeToListModel treeModel = (TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel();
        treeModel.displayObjectHierarchy = visible;
        if (currentSelection != null) {
            selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
            if (selectedNode != null && selectedNode != mwdNode && !selectedNode.nestedObjectsFound)
                findNestedObjects(selectedNode);
        }
        treeModel.revalidate();
        if (selectedNode != null)
            selectNode(selectedNode);
    }

    public boolean isObjectHierarchyVisible() {
        if (treeNotInitialized) return false;
        TreeComboBox.TreeToListModel treeModel = (TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel();
        return treeModel.displayObjectHierarchy;
    }

    public void setUIHierarchyVisible(boolean visible) {
        if (treeNotInitialized) return;
//        ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
        ObjectHierarchyTreeNode selectedNode = null;
        TreeComboBox.TreeToListModel treeModel = (TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel();
        treeModel.displayUIHierarchy = visible;
        if (currentSelection != null) {
            selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
            if (selectedNode != null && !selectedNode._AWTchildrenFound)
                findChildUIComponents(selectedNode);
        }
        treeModel.revalidate();
        if (selectedNode != null)
            selectNode(selectedNode);
    }

    public boolean isUIHierarchyVisible() {
        if (treeNotInitialized) return false;
        TreeComboBox.TreeToListModel treeModel = (TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel();
        return treeModel.displayUIHierarchy;
    }

    public void removeSelectedComponent() {
        if (treeNotInitialized) return;
        ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
        if (selectedEntry != null && selectedEntry.object != null) {
            if (Component.class.isAssignableFrom(((ObjectHierarchyTreeNode) selectedEntry.object).getUserObject().getClass())) {
                Component compo = (Component) ((ObjectHierarchyTreeNode) selectedEntry.object).getUserObject();

                Container container = compo.getParent();
//                System.out.println("container: " + container);
                ObjectHierarchyTreeNode containerNode = (ObjectHierarchyTreeNode) ((ObjectHierarchyTreeNode) selectedEntry.object).getParent();
                if (containerNode == null) return;
                container.remove(compo);
//                if (containerNode.componentHierarchyNode)
//                    refreshChildComponents(containerNode);
//                else
                    ((ObjectHierarchyTreeNode) selectedEntry.object).removeFromParent();
                selectNode(containerNode);
                container.invalidate();
                container.doLayout();
                container.repaint();
            }
        }
    }

    public void addNewComponent() {
        if (treeNotInitialized) return;
        ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
        if (selectedEntry != null && selectedEntry.object != null) {
            if (Container.class.isAssignableFrom(((ObjectHierarchyTreeNode) selectedEntry.object).getUserObject().getClass())) {
                ObjectHierarchyTreeNode parentNode = (ObjectHierarchyTreeNode) selectedEntry.object;
                Container container = (Container) parentNode.getUserObject();
                if (PanelComponent.class.isAssignableFrom(container.getClass()))
                    container = ((PanelComponent) container).getContentPane();

                ESlateComposer composer = null;
                if (ESlateComposer.class.isAssignableFrom(this.container.getClass())) {
                    composer = (ESlateComposer) this.container;
                }
                AddComponentDialog dialog = new AddComponentDialog(composer.getParentFrame(), composer, container, BeanInfoDialog.this);

                if (dialog.returnCode == AddComponentDialog.OK) {

                    String componentClassName = dialog.getComponentClassName().trim();
                    String cardName = dialog.getCardName();
                    String orientation = dialog.getOrientation();

                    if (componentClassName == null || componentClassName.length() == 0) {
                        ESlateOptionPane.showMessageDialog(this, beanInfoDialogBundle.getString("Message3"), beanInfoDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Class componentClass = null;
                    try{
                        componentClass = Class.forName(componentClassName);
                    }catch (ClassNotFoundException exc) {
                        DetailedErrorDialog errorDialog = new DetailedErrorDialog(composer.getParentFrame());
                        errorDialog.setMessage(beanInfoDialogBundle.getString("Message1") + componentClassName + "\".");
//                        dialog.appendToDetails(containerBundle.getString("ContainerMsg3") + "\n");
                        errorDialog.appendThrowableStackTrace(exc);
                        ESlateContainerUtils.showDetailedErrorDialog(composer, errorDialog, this, true);
                        return;
                    }
                    if (!Component.class.isAssignableFrom(componentClass)) {
                        ESlateOptionPane.showMessageDialog(this, beanInfoDialogBundle.getString("Message1") + componentClassName + "\"." + beanInfoDialogBundle.getString("Message4"), beanInfoDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String componentName = null;
                    int index = container.getComponentCount();
                    if (container.getLayout() != null && CardLayout.class.isAssignableFrom(container.getLayout().getClass())) {
                        if (cardName != null && cardName.trim().length() != 0)
                            componentName = cardName;
                    }
                    try{
                        Component compo = (Component) componentClass.newInstance();
                        // Create a unique name for the new component
                        componentName = createUniqueComponentName(
                                  parentNode,
                                  compo,
                                  componentName);

                        //Add the component to the container
                        if (container.getLayout() == null) {
                            container.add(compo);
                        }else if (CardLayout.class.isAssignableFrom(container.getLayout().getClass())) {
                            container.add(componentName, compo);
                        }else if (BorderLayout.class.isAssignableFrom(container.getLayout().getClass())) {
                            if (orientation != null)
                                container.add(compo, orientation);
                        }else{
                            index = dialog.getIndex();
                            container.add(compo, index);
                        }

                        if ((isComponentHierarchyVisible() && ESlatePart.class.isAssignableFrom(compo.getClass())) ||
                            isUIHierarchyVisible()) {
                            componentName = ((ESlatePart) compo).getESlateHandle().getComponentName();
                            ObjectHierarchyTreeNode newNode = new ObjectHierarchyTreeNode(compo, componentName, this);
                            newNode.accessorMethodName = null;
                            if (Component.class.isAssignableFrom(compo.getClass()))
                                newNode.uiHierarchyNode = true;
                            if (ESlatePart.class.isAssignableFrom(compo.getClass()))
                                newNode.componentHierarchyNode = true;

                            /* We have to find the real index in the 'parentNode' child array, where the
                             * 'newNode' will be inserted. This is because there exist other nodes too, which
                             * are not visible.
                             * To achieve this we find the real index of the currently visible componentHierarchy
                             * node with relative index equal to 'index'. Then we insert the 'newNode' at this
                             * real index.
                             */
    /*                        int realIndex = 0, visibleNodeCount = -1;
                            for (; realIndex< parentNode.getChildCount(); realIndex++) {
                                if (((ObjectHierarchyTreeNode) parentNode.getChildAt(realIndex)).componentHierarchyNode) {
                                    visibleNodeCount++;
                                    if (visibleNodeCount == index) {
                                        break;
                                    }
                                }
                            }
                            System.out.println("index: " + index + ", realIndex: " + realIndex);
    */
                            parentNode.insert(newNode, getNodeIndex(parentNode, index));

                            selectNode(newNode);
                        }
                        container.invalidate();
                        container.doLayout();
                        container.repaint();
                    }catch (InstantiationException exc) {
                        DetailedErrorDialog errorDialog = new DetailedErrorDialog(composer.getParentFrame());
                        errorDialog.setMessage(beanInfoDialogBundle.getString("Message2") + componentClassName + "\".");
//                        dialog.appendToDetails(containerBundle.getString("ContainerMsg3") + "\n");
                        errorDialog.appendThrowableStackTrace(exc);
                        ESlateContainerUtils.showDetailedErrorDialog(((ESlateComposer) container), errorDialog, this, true);
                        return;
                    }catch (IllegalAccessException exc) {
                        DetailedErrorDialog errorDialog = new DetailedErrorDialog(composer.getParentFrame());
                        errorDialog.setMessage(beanInfoDialogBundle.getString("Message2") + componentClassName + "\".");
//                        dialog.appendToDetails(containerBundle.getString("ContainerMsg3") + "\n");
                        errorDialog.appendThrowableStackTrace(exc);
                        ESlateContainerUtils.showDetailedErrorDialog(((ESlateComposer) container), errorDialog, this, true);
                        return;
                    }
                }
            }
        }
    }

    private int getNodeIndex(ObjectHierarchyTreeNode parentNode, int index) {
        /* We have to find the real index in the 'parentNode' child array, where the
         * 'newNode' will be inserted. This is because there exist other nodes too, which
         * are not visible.
         * To achieve this we find the real index of the currently visible componentHierarchy
         * node with relative index equal to 'index'. Then we insert the 'newNode' at this
         * real index.
         */
        int realIndex = 0, visibleNodeCount = -1;
        for (; realIndex< parentNode.getChildCount(); realIndex++) {
            if (((ObjectHierarchyTreeNode) parentNode.getChildAt(realIndex)).uiHierarchyNode) {
                visibleNodeCount++;
                if (visibleNodeCount == index) {
                    break;
                }
            }
        }
//        System.out.println("index: " + index + ", realIndex: " + realIndex);
        return realIndex;
    }


    private String createUniqueComponentName(ObjectHierarchyTreeNode containerNode, Component compo, String proposedName) {
        Array compoNames = new Array();
        Container container = (Container) containerNode.getUserObject();
//        Component[] compos = container.getComponents();
        for (int i=0; i<containerNode.getChildCount(); i++)
            compoNames.add(((ObjectHierarchyTreeNode) containerNode.getChildAt(i)).objectName);

        String componentName = proposedName;
        /* Create a unique (among the other objects of this object) name for this
         * nested onject. The rules according to which this name is produced are:
         * 1  If the object is a descendant of java.awt.Component used the name
         *    returned by its getName() method. If its not a descendant of java.swt.Component
         *    use the class name as its name.
         * 2  If the produced name is already used bu a sibling of this object, then create
         *    a new unique component name by appending a '_' and a number to the class name,
         *    which remains the base of the component.
         */
        if (componentName == null)
            componentName = compo.getClass().getName().substring(compo.getClass().getName().lastIndexOf('.')+1);
        int count = 0;
        while (compoNames.indexOf(componentName) != -1) {
            count++;
            int sepIndex = componentName.indexOf('_');
            if (sepIndex == -1)
                componentName = componentName + '_' + count;
            else
                componentName = componentName.substring(0, sepIndex+1) + count;
        }
        return componentName;
    }

    public JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(null);
        // PROPERTIES MENU
        String menuString = beanInfoDialogBundle.getString("Settings");
        JMenu propertiesMenu = (JMenu) menuBar.add(new JMenu(menuString));
        propertiesMenu.getPopupMenu().setLightWeightPopupEnabled(false);
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            propertiesMenu.setFont(greekUIFont);

        // PROPERTIES-->TYPE
        menuString = beanInfoDialogBundle.getString("PropertyType");
        propertyTypeMenu = (JMenu) propertiesMenu.add(new JMenu(menuString));
        propertyTypeMenu.getPopupMenu().setLightWeightPopupEnabled(false);
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            propertyTypeMenu.setFont(greekUIFont);

        // PROPERTIES-->TYPE-->ALL
        menuString = beanInfoDialogBundle.getString("All");
        allPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            allPropertiesItem.setFont(greekUIFont);

        allPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = ALL_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->READ-WRITE
        menuString = beanInfoDialogBundle.getString("ReadWrite");
        readWritePropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            readWritePropertiesItem.setFont(greekUIFont);

        readWritePropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = READ_WRITE_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->READ ONLY
        menuString = beanInfoDialogBundle.getString("ReadOnly");
        readPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            readPropertiesItem.setFont(greekUIFont);

        readPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = READ_ONLY_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->EXPERT
        menuString = beanInfoDialogBundle.getString("Expert");
        expertPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            expertPropertiesItem.setFont(greekUIFont);

        expertPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = EXPERT_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->BASIC
        menuString = beanInfoDialogBundle.getString("Basic");
        basicPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            basicPropertiesItem.setFont(greekUIFont);

        basicPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = BASIC_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->PREFERRED
        menuString = beanInfoDialogBundle.getString("Preferred");
        preferredPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            preferredPropertiesItem.setFont(greekUIFont);

        preferredPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = PREFERRED_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->BOUND
        menuString = beanInfoDialogBundle.getString("Bound");
	boundPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            boundPropertiesItem.setFont(greekUIFont);

        boundPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = BOUND_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->CONSTRAINED
        menuString = beanInfoDialogBundle.getString("Constrained");
	constrainedPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            constrainedPropertiesItem.setFont(greekUIFont);

        constrainedPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = CONSTRAINED_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        // PROPERTIES-->TYPE-->HIDDEN
        menuString = beanInfoDialogBundle.getString("Hidden");
	hiddenPropertiesItem = (JRadioButtonMenuItem) propertyTypeMenu.add(new JRadioButtonMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            hiddenPropertiesItem.setFont(greekUIFont);

        hiddenPropertiesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyDisplayMode = HIDDEN_PROPERTIES;
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                findBeanProperties(selectedNode);
                propertyEditorPanel.showProperties(selectedNode);
                propertyEditorPanel.adjustSizes();
            }
        });

        radioButtonMenuItemGroup.add(allPropertiesItem);
        radioButtonMenuItemGroup.add(readPropertiesItem);
        radioButtonMenuItemGroup.add(readWritePropertiesItem);
        radioButtonMenuItemGroup.add(expertPropertiesItem);
        radioButtonMenuItemGroup.add(basicPropertiesItem);
        radioButtonMenuItemGroup.add(preferredPropertiesItem);
        radioButtonMenuItemGroup.add(boundPropertiesItem);
        radioButtonMenuItemGroup.add(constrainedPropertiesItem);
        radioButtonMenuItemGroup.add(hiddenPropertiesItem);

        basicPropertiesItem.setSelected(true);

        // PROPERTIES-->REFRESH
        menuString = beanInfoDialogBundle.getString("Refresh");
	refreshItem = (JMenuItem) propertiesMenu.add(new JMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            refreshItem.setFont(greekUIFont);

        refreshItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                refreshingProperties = true;
                String[] pathNodeNames = null;
//                System.out.println("currentSelection: " + currentSelection);
                if (currentSelection != null)
                    pathNodeNames = ((ObjectHierarchyTreeNode) currentSelection.object).getPathNodeNames();
//                System.out.println("pathNodeNames: " + pathNodeNames);
                Object[] compos = getSecondLevelNodeObjects();
                String[] names = getSecondLevelNodeObjectNames();
                emptyPropertyEditorPanel();
                removeAllSecondLevelNodes();

                for (int i=0; i<compos.length; i++)
                    addSecondLevelNode(compos[i], names[i]);
                /* Establish the node selection which existed before refresh took place.
                 */
                if (pathNodeNames != null && pathNodeNames.length > 0) {
                    for (int i=0; i<pathNodeNames.length; i++)
                        System.out.print(pathNodeNames[i] + "->");
                    System.out.println();
                    ObjectHierarchyTreeNode node = null;
                    for (int i=0; i<pathNodeNames.length; i++) {
                        if (i == 0){
                            node = mwdNode;
                            node.expand(true);
                            continue;
                        }else
                            node = node.getChild(pathNodeNames[i]);
                        if (node == null) {
//                            System.out.println("Stopping expansion at: " + pathNodeNames[i]);
                            break;
                        }
                        if (node.entry != null) {
//                            System.out.println("Selecting: " + ((ObjectHierarchyTreeNode) node.entry.object()).objectName);
                            objectHierarchyComboBox.setSelectedItem(node.entry);
                            node.expand(true);
                        }
                    }
                    /* Reselct the node to be selected, cause there is some problem with
                     * this function, dute to the inability to select the root node. The
                     * problem resutls in:
                     * 1. The root node displayed in the ObjectHierarchyComboBox after refresh takes place.
                     * 2. The propertyEditorPanel is empty and displays the 'No active component' sign, as a result
                     *    of the refresh, if only one componetnt exists in the microworld.
                     */
                    if (pathNodeNames != null) {// && pathNodeNames.length == 2 && objectHierarchyComboBox.getItemCount() == 2) {
//                        System.out.println("RESELECTING NODE");
                        reselect = true;
                        selectNode(pathNodeNames);
                        reselect = false;
                    }
                }
                refreshingProperties = false;
            }
        });

        // PROPERTIES-->CUSTOMIZER
        menuString = beanInfoDialogBundle.getString("CustomizerMenu");
	customizerItem = (JMenuItem) propertiesMenu.add(new JMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            customizerItem.setFont(greekUIFont);

        customizerItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (currentSelection == null) return;
                ObjectHierarchyTreeNode selectedNode = (ObjectHierarchyTreeNode) currentSelection.object;
                Class customizerClass = selectedNode.customizerClass;
                if (customizerClass == null) return;
                if (!Component.class.isAssignableFrom(customizerClass)) {
                    ESlateOptionPane.showMessageDialog(BeanInfoDialog.this, beanInfoDialogBundle.getString("Customizer1"), beanInfoDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
/*                    DetailedErrorDialog dialog = new DetailedErrorDialog();
                    dialog.setMessage(beanInfoDialogBundle.getString("Customizer1"));
                    dialog.setDetails("dfgdsgsdfg\nrgdsfgfsdg\ndfgdsgsdfgsdfjkghdfsjkghdkflsjhgdflskhglsdkfhg sdfg hjsdfklg sdflkgh drsfh glsdkfhg sdflhg lsdfhg lsdf glsdfg lsdfgflsd lkfsdhglsdkfhg sdflg dflshgsd lgflsd glsdfhg lfsdhg lfsdhglsdfhg lsdfhgsdflhglsdfhglsdkfhgsdkflg fsdfglsdfhg lsdfhgs xfcgs");
                    ESlateComposerUtils.showDialog(dialog, BeanInfoDialog.this, true);
*/                    return;
                }
                if (!Customizer.class.isAssignableFrom(customizerClass)) {
                    ESlateOptionPane.showMessageDialog(BeanInfoDialog.this, beanInfoDialogBundle.getString("Customizer2"), beanInfoDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Component compo = null;
                try{
                    compo = (Component) customizerClass.newInstance();
                }catch (InstantiationException exc) {
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, BeanInfoDialog.this);
                    DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                    dialog.setMessage(beanInfoDialogBundle.getString("Customizer3") + " \"" + customizerClass.getName() + "\". ");
                    StringWriter writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    exc.printStackTrace(printWriter);
                    dialog.setDetails(exc.getMessage() + '\n' + writer.toString());
//                    ESlateComposerUtils.showDialog(dialog, BeanInfoDialog.this, true);
                    ESlateContainerUtils.showDetailedErrorDialog(((ESlateComposer) container), dialog, BeanInfoDialog.this, true);
                    return;
                }catch (IllegalAccessException exc) {
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, BeanInfoDialog.this);
                    DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                    dialog.setMessage(beanInfoDialogBundle.getString("Customizer3") + " \"" + customizerClass.getName() + "\". ");
                    StringWriter writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    exc.printStackTrace(printWriter);
                    dialog.setDetails(exc.getMessage() + '\n' + writer.toString());
                    ESlateContainerUtils.showDetailedErrorDialog(((ESlateComposer) container), dialog, BeanInfoDialog.this, true);
                    return;
                }catch (Throwable thr) {
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, BeanInfoDialog.this);
                    DetailedErrorDialog dialog = new DetailedErrorDialog(topLevelFrame);
                    dialog.setMessage(beanInfoDialogBundle.getString("Customizer3") + " \"" + customizerClass.getName() + "\". ");
                    StringWriter writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    thr.printStackTrace(printWriter);
                    dialog.setDetails(thr.getMessage() + '\n' + writer.toString());
                    ESlateContainerUtils.showDetailedErrorDialog(((ESlateComposer) container), dialog, BeanInfoDialog.this, true);
                    return;
                }
                if (compo == null) return;
                ((Customizer) compo).setObject(selectedNode.getUserObject());
                Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, BeanInfoDialog.this);
                CustomizerDialog dialog = new CustomizerDialog(
                            topLevelFrame,
                            beanInfoDialogBundle.getString("Customizer") + ": " + selectedNode.getUserObject().getClass().getName().substring(selectedNode.getUserObject().getClass().getName().lastIndexOf('.') + 1),
                            compo);
                ESlateContainerUtils.showDialog(dialog, BeanInfoDialog.this, true);
            }
        });

        propertiesMenu.addSeparator();

        // PROPERTIES-->COMPONENT HIERARCHY
        menuString = beanInfoDialogBundle.getString("ComponentHierachy");
	componentHierarchyItem = (JCheckBoxMenuItem) propertiesMenu.add(new JCheckBoxMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            componentHierarchyItem.setFont(greekUIFont);

        componentHierarchyItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setComponentHierarchyVisible(componentHierarchyItem.isSelected());
            }
        });

        // PROPERTIES-->UI HIERARCHY
        menuString = beanInfoDialogBundle.getString("UIHierachy");
	uiHierarchyItem = (JCheckBoxMenuItem) propertiesMenu.add(new JCheckBoxMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            uiHierarchyItem.setFont(greekUIFont);

        uiHierarchyItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setUIHierarchyVisible(uiHierarchyItem.isSelected());
            }
        });

        // PROPERTIES-->OBJECT HIERARCHY
        menuString = beanInfoDialogBundle.getString("ObjectHierachy");
	objectHierarchyItem = (JCheckBoxMenuItem) propertiesMenu.add(new JCheckBoxMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            objectHierarchyItem.setFont(greekUIFont);

        objectHierarchyItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setObjectHierarchyVisible(objectHierarchyItem.isSelected());
            }
        });

        propertiesMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                refreshItem.setEnabled(!(currentSelection == null));
            }
        });

        // CONTAINER MENU
        menuString = beanInfoDialogBundle.getString("Edit");
        JMenu containerMenu = (JMenu) menuBar.add(new JMenu(menuString));
        containerMenu.getPopupMenu().setLightWeightPopupEnabled(false);
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            containerMenu.setFont(greekUIFont);

        // CONTAINER-->ADD COMPONENT
        menuString = beanInfoDialogBundle.getString("Add");
        addComponent = (JMenuItem) containerMenu.add(new JMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            addComponent.setFont(greekUIFont);

        addComponent.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                addNewComponent();
            }
        });

        // CONTAINER-->REMOVE COMPONENT
        menuString = beanInfoDialogBundle.getString("Remove");
        removeComponent = (JMenuItem) containerMenu.add(new JMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            removeComponent.setFont(greekUIFont);

        removeComponent.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                removeSelectedComponent();
            }
        });

        propertiesMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
                ObjectHierarchyTreeNode node = null;
                if (selectedEntry != null)
                    node = (ObjectHierarchyTreeNode) selectedEntry.object;
                if (selectedEntry != null && node != null) {
                    propertyTypeMenu.setEnabled(true);
                    if (node.customizerClass != null)
                        customizerItem.setEnabled(true);
                    else
                        customizerItem.setEnabled(false);
                }else{
                    customizerItem.setEnabled(false);
                    propertyTypeMenu.setEnabled(false);
                }

                componentHierarchyItem.setSelected(((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).displayComponentHierarchy);
                objectHierarchyItem.setSelected(((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).displayObjectHierarchy);
                uiHierarchyItem.setSelected(((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).displayUIHierarchy);
            }
        });
        containerMenu.addMenuListener(new MenuListener() {
            public void menuDeselected(MenuEvent e) {};
            public void menuCanceled(MenuEvent e) {};
            public void menuSelected(MenuEvent e) {
                ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
                ObjectHierarchyTreeNode node = null;
                if (selectedEntry != null)
                    node = (ObjectHierarchyTreeNode) selectedEntry.object;
                if (selectedEntry != null && node != null) {
                    if (Container.class.isAssignableFrom(node.getUserObject().getClass())) {
                        Container c = (Container) node.getUserObject();
                        if (ESlateInternalFrame.class.isAssignableFrom(c.getClass()))
                            addComponent.setEnabled(false);
                        else
                            addComponent.setEnabled(true);
                    }else{
                        addComponent.setEnabled(false);
                    }
//                    System.out.println(node.getLevel());
                    if (!isSecondLevelNode(node) && Component.class.isAssignableFrom(node.getUserObject().getClass())) {
                        Component comp = (Component) node.getUserObject();
                        // Don't allow the removal of the contents of the ESlateInternalFrame
//                        System.out.println("comp.getParent().getParent().getParent().getParent().getParent(): " + comp.getParent().getParent().getParent().getParent().getParent());
                        boolean isESlateInternalFrameSidePane = false;
                        if (PanelComponent.class.isAssignableFrom(comp.getClass())) {
                            int parentCount = 5;
                            int count = parentCount;
                            for (int i=0; i<count; i++) {
                                if (comp.getParent() != null) {
                                    comp = comp.getParent();
                                    parentCount--;
                                }else
                                    break;
                            }
//                            System.out.println("parentCount: " + parentCount + ", comp: " + comp.getClass());
                            if (parentCount == 0) {
                                if (comp!= null && ESlateInternalFrame.class.isAssignableFrom(comp.getClass()))
                                    isESlateInternalFrameSidePane = true;
                            }
                        }

                        if (ESlateInternalFrame.class.isAssignableFrom(comp.getClass()) || isESlateInternalFrameSidePane)
                            removeComponent.setEnabled(false);
                        else
                            removeComponent.setEnabled(true);
                    }else{
                        removeComponent.setEnabled(false);
                    }
                }else{
                    addComponent.setEnabled(false);
                    removeComponent.setEnabled(false);
                }
            }
        });
        return menuBar;
    }

    /* Parse all the editors and locate the 'Icon' property editors. Call flush() on their
     * 'icon' ImageIcon attribute.
     */
    private void flushIconPropertyEditorIcons() {
        if (objectHierarchyComboBox == null) return;
        ObjectHierarchyTreeNode node = objectHierarchyComboBox.getSelectedNode();
        if (node == null) return;
        if (node.propertyEditorComponents == null) return;
        for (int i=0; i<node.propertyEditorComponents.length; i++) {
            if (IconPropertyEditor.class.isInstance(node.propertyEditors[i])) {
                IconPropertyEditor ipe = (IconPropertyEditor) node.propertyEditors[i];
//                System.out.println("Flushing icon " + ipe._icon + " of IconPropertyEditor: " + ipe);
                if (ipe._icon != null && ImageIcon.class.isAssignableFrom(ipe._icon.getClass())) {
                    ((ImageIcon) ipe._icon).getImage().flush();
                    ipe._icon = null;
                }
                if (ipe._icon != null && NewRestorableImageIcon.class.isAssignableFrom(ipe._icon.getClass())) {
                    ((NewRestorableImageIcon) ipe._icon).getImage().flush();
                    ipe._icon = null;
                }
            }
        }
    }

    private void findChildUIComponents(ObjectHierarchyTreeNode node) {
        if (node == null) return;
        Object object = node.getUserObject();
        if (!Container.class.isAssignableFrom(object.getClass())) return;

        Array objectNames = new Array();
        Container container = (Container) object;
        Component[] components = null;
        /* Special children component handling for JMenu containers. JMenu contain
         * a pop-up menu, which contains all the JMenuItems/JMenus of the JMenu. So
         * instead of using the standard getComponents() method of Container, we use
         * the getMenuComponents() method of JMenu to get the child components.
         */
        if (JMenu.class.isAssignableFrom(container.getClass()))
            components = ((JMenu) container).getMenuComponents();
        else if (PanelComponent.class.isAssignableFrom(container.getClass()))
            components = ((PanelComponent) container).getContentPane().getComponents();
        else if (ESlateInternalFrame.class.isAssignableFrom(container.getClass()))
            components = new Component[0];
        else
            components = container.getComponents();
        for (int i=0; i<components.length; i++) {
            /* Create a unique (among the other objects of this object) name for this
             * nested onject. The rules according to which this name is produced are:
             * 1  If the object is a descendant of java.awt.Component used the name
             *    returned by its getName() method. If its not a descendant of java.swt.Component
             *    use the class name as its name.
             * 2  If the produced name is already used bu a sibling of this object, then create
             *    a new unique component name by appending a '_' and a number to the class name,
             *    which remains the base of the component.
             */
            String compoName = components[i].getName();
            if (compoName == null)
                compoName = components[i].getClass().getName().substring(components[i].getClass().getName().lastIndexOf('.')+1);
            int count = 0;
            while (objectNames.indexOf(compoName) != -1) {
                count++;
                int sepIndex = compoName.indexOf('_');
                if (sepIndex == -1)
                    compoName = compoName + '_' + count;
                else
                    compoName = compoName.substring(0, sepIndex+1) + count;
            }
            ObjectHierarchyTreeNode nd = new ObjectHierarchyTreeNode(components[i], compoName, this);
            nd.accessorMethodName = null;
            nd.uiHierarchyNode = true;
            node.add(nd);
//            System.out.println("Adding node: " + nd);
            objectNames.add(compoName);
        }
        objectNames.clear();
        objectNames = null;
        node._AWTchildrenFound = true;
    }

    private void findHostedComponents(ObjectHierarchyTreeNode node) {
        if (node == null) return;
        Object object = node.getUserObject();
        if (!ESlatePart.class.isAssignableFrom(object.getClass())) return;

        Array objectNames = new Array();
        ESlatePart parent = (ESlatePart) object;
        ESlateHandle[] hostedHandles = parent.getESlateHandle().toArray();
        Object[] components = new Object[hostedHandles.length];
        for (int i=0; i<components.length; i++)
            components[i] = hostedHandles[i].getComponent();
        /* Special children component handling for JMenu containers. JMenu contain
         * a pop-up menu, which contains all the JMenuItems/JMenus of the JMenu. So
         * instead of using the standard getComponents() method of Container, we use
         * the getMenuComponents() method of JMenu to get the child components.
         */
//        if (JMenu.class.isAssignableFrom(container.getClass()))
//            components = ((JMenu) container).getMenuComponents();
//        else if (PanelComponent.class.isAssignableFrom(container.getClass()))
//            components = ((PanelComponent) container).getContentPane().getComponents();
//        else
//here    for (int i=0; i <components.length; i++)
//here      components[i] = container.getComponents();
          for (int i=0; i<components.length; i++) {
            /* Create a unique (among the other objects of this object) name for this
             * nested onject. The rules according to which this name is produced are:
             * 1  If the object is a descendant of java.awt.Component used the name
             *    returned by its getName() method. If its not a descendant of java.swt.Component
             *    use the class name as its name.
             * 2  If the produced name is already used bu a sibling of this object, then create
             *    a new unique component name by appending a '_' and a number to the class name,
             *    which remains the base of the component.
             */
/*//here*/  String compoName = hostedHandles[i].getComponentName(); //((JComponent) components[i]).getName();
            if (compoName == null)
                compoName = components[i].getClass().getName().substring(components[i].getClass().getName().lastIndexOf('.')+1);
            int count = 0;
            while (objectNames.indexOf(compoName) != -1) {
                count++;
                int sepIndex = compoName.indexOf('_');
                if (sepIndex == -1)
                    compoName = compoName + '_' + count;
                else
                    compoName = compoName.substring(0, sepIndex+1) + count;
            }
            ObjectHierarchyTreeNode nd = new ObjectHierarchyTreeNode(components[i], compoName, this);
            nd.accessorMethodName = null;
            nd.componentHierarchyNode = true;
            node.add(nd);
//            System.out.println("Adding node: " + nd);
            objectNames.add(compoName);
        }
        objectNames.clear();
        objectNames = null;
        node.hostedComponentsFound = true;
    }

    protected void refreshChildUIComponents(ObjectHierarchyTreeNode node) {
        Object object = node.getUserObject();
        if (!Container.class.isAssignableFrom(object.getClass())) return;

        /* First remove all the children of this node, which were created by a
         * previous call to 'findChildComponents()'. These nodes are marked with the
         * 'componentHierarchyNode' flag;
         */
//        System.out.println("refreshChildComponents --> node.getChildCount(): " + node.getChildCount());
        ObjectHierarchyTreeNode child;
        for (int i=0; i<node.getChildCount(); i++) {
            child = (ObjectHierarchyTreeNode) node.getChildAt(i);
            if (child.uiHierarchyNode) {
//                System.out.println("Removing: " + child);
                child.removeFromParent();
                i--;
            }
        }
        findChildUIComponents(node);
    }

    public void selectNode(String[] pathNodeNames) {
        if (treeNotInitialized) return;
        if (pathNodeNames == null) return;
        ObjectHierarchyTreeNode target = mwdNode;
        boolean nodeFound = true;
        for (int i=1; i<pathNodeNames.length; i++) {
            String nextNodeName = pathNodeNames[i];
            target = target.getChild(nextNodeName);
            if (target == null) {
                nodeFound = false;
                break;
            }
        }
        if (nodeFound) {
            target.expand(true);
            objectHierarchyComboBox.setSelectedItem(target.entry);
        }
    }

    public void selectNode(ObjectHierarchyTreeNode node) {
        if (treeNotInitialized) return;
        if (node == null) return;
        selectNode(node.getPathNodeNames());
    }

    public void selectNode(ObjectHierarchyTreeNode parent, String childNodeName) {
        if (treeNotInitialized) return;
        if (parent == null || childNodeName == null) return;
        String[] parentNodePathNames = parent.getPathNodeNames();
        String[] nodePathNames = new String[parentNodePathNames.length+1];
        int i;
        for (i=0; i<parentNodePathNames.length; i++)
            nodePathNames[i] = parentNodePathNames[i];
        nodePathNames[i] = childNodeName;
        selectNode(nodePathNames);
    }

    /* Returns the child of 'parent' node, whose user object is 'nodeObject', if exists */
    public ObjectHierarchyTreeNode getChild(ObjectHierarchyTreeNode parent, Object nodeObject) {
        if (treeNotInitialized) return null;
        if (parent == null || nodeObject == null) return null;
        ObjectHierarchyTreeNode child = null, node = null;
        for (int i=0; i<parent.getChildCount(); i++) {
            child = (ObjectHierarchyTreeNode) parent.getChildAt(i);
            if (child.getUserObject().equals(nodeObject)) {
                node = child;
                break;
            }
        }
        return node;
    }

    /* Returns the child of 'parent' node, whose 'objectName' is 'childName', if exists */
    public ObjectHierarchyTreeNode getChild(ObjectHierarchyTreeNode parent, String childName) {
        if (treeNotInitialized) return null;
        if (parent == null || childName == null) return null;
        ObjectHierarchyTreeNode child = null, node = null;
        for (int i=0; i<parent.getChildCount(); i++) {
            child = (ObjectHierarchyTreeNode) parent.getChildAt(i);
            if (child.objectName.equals(childName)) {
                node = child;
                break;
            }
        }
        return node;
    }

    private PropertyDescriptor[] findPropertyDescriptorsWithoutRegisteredEditors(Object object) {
        PropertyDescriptor[] propDescriptors = null;
//        try{

            Class objClass = object.getClass();
//            System.out.println("findPropertyDescriptorsWithoutRegisteredEditors objClass: " + objClass);
            BeanInfo objInfo = BeanInfoFactory.getBeanInfo(objClass); //, compoClass.getSuperclass());
            propDescriptors = objInfo.getPropertyDescriptors();

            /* Sort the property descriptors based on the display names of the properties.
             */
            com.objectspace.jgl.OrderedMap propDescriptorsMap = new com.objectspace.jgl.OrderedMap(new com.objectspace.jgl.LessString());
            for (int i=0; i<propDescriptors.length; i++) {
                propDescriptorsMap.add(propDescriptors[i].getDisplayName(), propDescriptors[i]);
            }
            java.util.Enumeration propDispNames = propDescriptorsMap.keys();
            int t = 0;
            while (propDispNames.hasMoreElements()) {
                propDescriptors[t] = (PropertyDescriptor) propDescriptorsMap.get(propDispNames.nextElement());
                t++;
            }
            propDescriptorsMap = null;
//        }catch (java.beans.IntrospectionException exc) {
//            System.out.println("Introspection exception");
//        }


        if (propDescriptors == null)
            return null;

        int propDescriptorCount = propDescriptors.length;
//        System.out.println("propDescriptorCount: " + propDescriptorCount);

        Array descriptorsWithoutRegisteredPropertyEditors = new Array();
        int k=0;
        for (int i=0; i<propDescriptorCount; i++) {
//          System.out.println("i: " + i + ", propDescriptors[i].getDisplayName(): " +  propDescriptors[i].getDisplayName());
            Class propertyEditorClass = propDescriptors[i].getPropertyEditorClass();
//            System.out.println(propDescriptors[i].getDisplayName() + " propertyEditorClass: " + propertyEditorClass);
            /* Look for a registered editor for this property's data type.
             */
            if (propertyEditorClass == null) {
                // Get the property's data type
                Method getter = propDescriptors[i].getReadMethod();
                if (getter == null) {
                    continue;
                }
                Class propertyType = getter.getReturnType();
                if (propertyType == null)
                    continue;
                if (propertyType.getName().equals("int")) {
                    propertyType = Integer.class;
                }
                if (propertyType.getName().equals("boolean")) {
                    propertyType = Boolean.class;
                }
                if (propertyType.getName().equals("double")) {
                    propertyType = Double.class;
                }
                if (propertyType.getName().equals("float")) {
                    propertyType = Float.class;
                }
                if (propertyType.getName().equals("long")) {
                    propertyType = Long.class;
                }
                if (propertyType.getName().equals("short")) {
                    propertyType = Short.class;
                }
                PropertyEditor regPropEditor = java.beans.PropertyEditorManager.findEditor(propertyType);
//                System.out.println("regPropEditor: " + regPropEditor);
                if (regPropEditor != null)
                    propertyEditorClass = regPropEditor.getClass();
            }
//            System.out.println("findPropertyDescriptorsWithoutRegisteredEditors: " + propertyEditorClass);
            if (propertyEditorClass == null)
                descriptorsWithoutRegisteredPropertyEditors.add(propDescriptors[i]);
        }
        propDescriptors = new PropertyDescriptor[descriptorsWithoutRegisteredPropertyEditors.size()];
        for (int i=0; i<descriptorsWithoutRegisteredPropertyEditors.size(); i++)
            propDescriptors[i] = (PropertyDescriptor) descriptorsWithoutRegisteredPropertyEditors.at(i);

        return propDescriptors;
    }

    private void findNestedObjects(ObjectHierarchyTreeNode node) {
        Object topObject = node.getUserObject();
//        System.out.println("findNestedObjects(): " + node.objectName);
//        System.out.println("objHierarchyUnchecked: " + node.objHierarchyUnchecked);
        Array objectNames = new Array();
        if (node.objHierarchyUnchecked || node.descriptorsWithoutRegisteredPropertyEditors == null) {
            node.descriptorsWithoutRegisteredPropertyEditors = findPropertyDescriptorsWithoutRegisteredEditors(topObject);
            node.objHierarchyUnchecked = false;
        }
//System.out.println("findNestedObjects 1");

        int propDescriptorCount = 0;
        if (node.descriptorsWithoutRegisteredPropertyEditors != null)
            propDescriptorCount = node.descriptorsWithoutRegisteredPropertyEditors.length;
        PropertyDescriptor descriptor;

//System.out.println("findNestedObjects 2");
        for (int i=0; i<propDescriptorCount; i++) {
            descriptor = node.descriptorsWithoutRegisteredPropertyEditors[i];

            // Get the property's data type
            Method getter = descriptor.getReadMethod();
//            System.out.println("getter: " + getter);
            if (getter == null)
                continue;
            Class propertyType = getter.getReturnType();
//            System.out.println("propertyType: " + propertyType);
            if (propertyType == null)
                continue;

            Object[] objects = null;
            IntBaseArray indexArray = new IntBaseArray();
            boolean indexedProperty = false;
//System.out.println("findNestedObjects 3");
            if (!IndexedPropertyDescriptor.class.isAssignableFrom(descriptor.getClass())) {
                try{
                    Object obj = getter.invoke(topObject, new Object[0]);
                    if (obj == null) continue;
//                    System.out.println("obj: " + obj);
                    objects = new Object[1];
                    objects[0] = obj;
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    continue;
                }catch (java.lang.IllegalAccessException exc) {
                    continue;
                }
            }else{
                indexedProperty = true;
                getter = ((IndexedPropertyDescriptor) descriptor).getIndexedReadMethod();
                ObjectBaseArray objectArray = new ObjectBaseArray();
                int index = -1;
                while (true) {
                    index++;
                    try {
                        try{
                            Object obj = getter.invoke(topObject, new Object[]{new Integer(index)});
                            if (obj == null) continue;
                            objectArray.add(obj);
                            indexArray.add(index);
        //                    System.out.println("obj: " + obj);
                        }catch (java.lang.reflect.InvocationTargetException exc) {
                            break;
                        }catch (java.lang.IllegalAccessException exc) {
                            break;
                        }
                    }catch (ArrayIndexOutOfBoundsException exc) {
                        break;
                    }
                    if (objectArray.size() != 0) {
                        objects = new Object[objectArray.size()];
                        for (int m=0; m<objects.length; m++)
                            objects[m] = objectArray.get(m);
                    }
                }
            }

            if (objects == null)
                continue;

//System.out.println("findNestedObjects 4");
            for (int k=0; k<objects.length; k++) {
                Object obj = objects[k];
//System.out.println("findNestedObjects 4.1 obj: " + obj);
                /* Create a unique (among the other objects of this object) name for this
                 * nested onject. The rules according to which this name is produced are:
                 * 1. The object acquires the name of the 'getter' method, without the leading "get",
                 *    if one exists.
                 * 2. If the name produced by (1) is empty then create a new name. The rules for the
                 *    new name are:
                 *    2.1  If the object is a descendant of java.awt.Component used the name
                 *         returned by its getName() method. If its not a descendant of java.swt.Component
                 *         use the class name as its name.
                 *    2.2  If the produced name is already used bu a sibling of this object, then create
                 *         a new unique component name by appending a '_' and a number to the class name,
                 *         which remains the base of the component.
                 */
                String compoName = null;
                if (!indexedProperty) {
                    compoName = descriptor.getDisplayName();
                    if (compoName == null || compoName.length() == 0) {
                        compoName = getter.getName();
                        if (compoName.startsWith("get"))
                            compoName = compoName.substring(3);
                    }
                }
//System.out.println("findNestedObjects 4.2 compoName: " + compoName);
                if (compoName == null || compoName.length() == 0) {
                    if (Component.class.isAssignableFrom(obj.getClass()))
                        compoName = ((java.awt.Component) obj).getName();
                    if (compoName == null)
                        compoName = obj.getClass().getName().substring(obj.getClass().getName().lastIndexOf('.')+1);
                    int count = 0;
//System.out.println("findNestedObjects 4.3");
                    while (objectNames.indexOf(compoName) != -1) {
                        count++;
                        int sepIndex = compoName.indexOf('_');
                        if (sepIndex == -1)
                            compoName = compoName + '_' + count;
                        else
                            compoName = compoName.substring(0, sepIndex+1) + count;
                    }
                }else{
                    compoName = compoName + "(" + obj.getClass().getName().substring(obj.getClass().getName().lastIndexOf('.')+1) + ")";
                }

//System.out.println("findNestedObjects 5");
                ObjectHierarchyTreeNode newNode = new ObjectHierarchyTreeNode(obj, compoName, this);
                newNode.objectHierarchyNode = true;
                newNode.accessorMethodName = getter.getName();
                /* If this is a indexed object (i.e. an object accessed through a method that
                 * accepts a single int parameter and returns an element of an array), then we
                 * have to store the index of the object too along with the name of the method.
                 */
                if (indexedProperty)
                    newNode.objectIndex = indexArray.get(k);
                node.add(newNode);
                objectNames.add(compoName);
//System.out.println("findNestedObjects 6");
            }
///                nestedObjects.addNestedObject(obj);
        }
        objectNames.clear();
        objectNames = null;
//        System.out.println("findNestedObjects setting introspected to true");
        node.nestedObjectsFound = true;
    }

    public void move(ListEntry source, ListEntry target, boolean rearrange) {
        if (treeNotInitialized) return;
        if (source == null || target == null) return;
        /* The topmost node and second level nodes cannot be moved. Also no node can be moved
         * to the topmost node.
         */
        ObjectHierarchyTreeNode sourceNode = (ObjectHierarchyTreeNode) source.object;
        ObjectHierarchyTreeNode targetNode = (ObjectHierarchyTreeNode) target.object;
        String sourceNodeName = sourceNode.objectName;
//        System.out.println("sourceNode level: " + sourceNode.getLevel());
//        System.out.println("targetNode level: " + targetNode.getLevel());
        if (sourceNode.getLevel() <= 1) return;
        if (targetNode.getLevel() == 0) return;

        /* 'targetContainer' is the component on which the moved component was dropped.
         * 'sourceComponent' is the moved component
         */
        Container targetContainer = null;
        Component sourceComponent = null;
        Container sourceComponentContainer = null;
        if (Container.class.isAssignableFrom(targetNode.getUserObject().getClass()))
            targetContainer = (Container) targetNode.getUserObject();
        if (Component.class.isAssignableFrom(sourceNode.getUserObject().getClass()))
            sourceComponent = (Component) sourceNode.getUserObject();

        if (targetContainer == null || sourceComponent == null)
            return;
        sourceComponentContainer = sourceComponent.getParent();

        /* If 'rearrange' is true and the Container of the source component is the same as the
         * Container of the target component, then rearrange the components of the container,
         * instead of moving the source component INTO the target component.
         */
//        System.out.println("rearrange: " + rearrange);
//        System.out.println("sourceComponentContainer == targetContainer.getParent(): " +
//        (sourceComponentContainer == targetContainer.getParent()));
        Component selectedTarget = null;
        if (rearrange && sourceComponentContainer == targetContainer.getParent()) {
            selectedTarget = targetContainer;
            targetNode = (ObjectHierarchyTreeNode) sourceNode.getParent();
            targetContainer = sourceComponentContainer;
        }

        /* Remove the component from the old container and insert it in the new one. The
         * insertion process depends on the layout manager of the new container.
         */
//        BeanInfoDialog dialog = sourceNode.beanInfoDialog;
        if (targetContainer.getLayout() == null) {
            /* Repositioning a component of a container with null layout manager is meaningless.
             */
            if (targetContainer == sourceComponentContainer)
                return;
            sourceComponent.getParent().remove(sourceComponent);
            targetContainer.add(sourceComponent);
            ((ObjectHierarchyTreeNode) sourceNode.getParent()).remove(sourceNode);
            targetNode.add(sourceNode);

            /* If the object is moved to 'targetContainer' from another Container and these java.awt.Containers
             * belong to objects with different ESlateHandles in the microworld, then if 'sourceComponent' has
             * an attached script, the script will be lost. To avoid this we explicitly update the handle of the
             * sourceComponent in the ScriptListenerMap instance.
             */
            updateHandle(sourceComponent);
        }else if (BorderLayout.class.isAssignableFrom(targetContainer.getLayout().getClass())) {
            if (targetContainer == sourceComponentContainer) {
                /* A component is repositioned in the 'targetContainer'*/
                java.util.Hashtable map = ESlateContainerUtils.analyzeBorderLayoutComponents(targetContainer);
                if (map == null) return;
                String selectedTargetConstraint = null;
                java.util.Enumeration enumeration = map.keys();
                while (enumeration.hasMoreElements()) {
                    Object con = enumeration.nextElement();
                    Object o = map.get(con);
                    if (o.equals(selectedTarget)) {
                        selectedTargetConstraint = (String) con;
                        break;
                    }
                }
                if (selectedTargetConstraint == null) return;
                String sourceComponentConstraint = null;
                enumeration = map.keys();
                while (enumeration.hasMoreElements()) {
                    Object con = enumeration.nextElement();
                    Object o = map.get(con);
                    if (o.equals(sourceComponent)) {
                        sourceComponentConstraint = (String) con;
                        break;
                    }
                }
                if (sourceComponentConstraint == null) return;
//                System.out.println("sourceComponentConstraint: " + sourceComponentConstraint);
//                System.out.println("selectedTargetConstraint: " + selectedTargetConstraint);

                /* Change positin between the 'selectedTarget' and 'sourceComponent' */
                targetContainer.remove(selectedTarget);
                targetContainer.remove(sourceComponent);
                targetContainer.add(sourceComponent, selectedTargetConstraint);
                targetContainer.add(selectedTarget, sourceComponentConstraint);
            }else{
                /* A component is inserted to the 'targetContainer' from another Container. */
                // Ask the user where in the BorderLayout container the component will be inserted.
                String dialogMsg = beanInfoDialogBundle.getString("BorderLayoutPos");
                String dialogTitle = beanInfoDialogBundle.getString("BorderLayoutTitle");
                Object[] positions = new Object[5];
                positions[0] = BorderLayout.CENTER;
                positions[1] = BorderLayout.NORTH;
                positions[2] = BorderLayout.SOUTH;
                positions[3] = BorderLayout.WEST;
                positions[4] = BorderLayout.EAST;

                String constraint = (String) ESlateOptionPane.showInputDialog(this,
                                          dialogMsg,
                                          dialogTitle,
                                          JOptionPane.QUESTION_MESSAGE,
                                          null,
                                          positions,
                                          null);
                if (constraint == null) return;

                sourceComponent.getParent().remove(sourceComponent);
                targetContainer.add(sourceComponent, constraint);
                ((ObjectHierarchyTreeNode) sourceNode.getParent()).remove(sourceNode);
                targetNode.add(sourceNode);

                /* If the object is moved to 'targetContainer' from another Container and these java.awt.Containers
                 * belong to objects with different ESlateHandles in the microworld, then if 'sourceComponent' has
                 * an attached script, the script will be lost. To avoid this we explicitly update the handle of the
                 * sourceComponent in the ScriptListenerMap instance.
                 */
                updateHandle(sourceComponent);
            }
        }else if (CardLayout.class.isAssignableFrom(targetContainer.getLayout().getClass())) {
            /* A component of the 'targetContainer' is repositioned inside the 'targetContainer' */
            if (targetContainer == sourceComponentContainer) {
                /* Find the index of the 'selectedTarget' inside the 'targetContainer'. The
                 * 'sourceComponent' will be placed inserted before the 'selectedTarget'.
                 */
                Component[] compos = targetContainer.getComponents();
                int selectedTargetIndex = -1;
                for (int i=0; i<targetContainer.getComponentCount(); i++) {
                    if (compos[i] == selectedTarget) {
                        selectedTargetIndex = i;
                        break;
                    }
                }
                int sourceComponentIndex = -1;
                for (int i=0; i<targetContainer.getComponentCount(); i++) {
                    if (compos[i] == sourceComponent) {
                        sourceComponentIndex = i;
                        break;
                    }
                }
//                System.out.println(" Repos selectedTargetIndex: " + selectedTargetIndex);
//                System.out.println(" Repos sourceComponentIndex: " + sourceComponentIndex);
                if (selectedTargetIndex != -1 && sourceComponentIndex != -1) {
                    /* Place the component in front of the target. */
                    if (sourceComponentIndex < selectedTargetIndex)
                        selectedTargetIndex--;

//                    System.out.println("sourceComponent.isVisible(): " + sourceComponent.isVisible());
                    /* The CardLayout causes all the contents of the 'targetContainer' to become
                     * invisible, if the Card being moved is the visible one. The following if
                     * handles this situation.
                     */
                    if (sourceComponent.isVisible())
                        ((CardLayout) targetContainer.getLayout()).next(targetContainer);
                    targetContainer.remove(sourceComponent);
                    targetContainer.add(sourceComponent, sourceNode.objectName, selectedTargetIndex);
//                    refreshChildComponents(targetNode);
//                    sourceNode = getChild(targetNode, sourceNodeName);
                    targetNode.remove(sourceNode);
//                    targetNode.insert(sourceNode, selectedTargetIndex);
                    targetNode.insert(sourceNode, getNodeIndex(targetNode, selectedTargetIndex));
                }
            }else{
                Array compoNames = new Array();
//                Component[] compos = targetContainer.getComponents();
                for (int i=0; i<targetNode.getChildCount(); i++)
                    compoNames.add(((ObjectHierarchyTreeNode) targetNode.getChildAt(i)).objectName);

                /* Create a unique (among the other objects of this object) name for this
                 * nested onject. The rules according to which this name is produced are:
                 * 1  If the object is a descendant of java.awt.Component used the name
                 *    returned by its getName() method. If its not a descendant of java.swt.Component
                 *    use the class name as its name.
                 * 2  If the produced name is already used bu a sibling of this object, then create
                 *    a new unique component name by appending a '_' and a number to the class name,
                 *    which remains the base of the component.
                 */
                String compoName = sourceComponent.getName();
                if (compoName == null)
                    compoName = sourceComponent.getClass().getName().substring(sourceComponent.getClass().getName().lastIndexOf('.')+1);
                int count = 0;
                while (compoNames.indexOf(compoName) != -1) {
                    count++;
                    int sepIndex = compoName.indexOf('_');
                    if (sepIndex == -1)
                        compoName = compoName + '_' + count;
                    else
                        compoName = compoName.substring(0, sepIndex+1) + count;
                }

                sourceComponent.getParent().remove(sourceComponent);
                targetContainer.add(sourceComponent, compoName);
                ((ObjectHierarchyTreeNode) sourceNode.getParent()).remove(sourceNode);
                targetNode.add(sourceNode);

                /* If the object is moved to 'targetContainer' from another Container and these java.awt.Containers
                 * belong to objects with different ESlateHandles in the microworld, then if 'sourceComponent' has
                 * an attached script, the script will be lost. To avoid this we explicitly update the handle of the
                 * sourceComponent in the ScriptListenerMap instance.
                 */
                updateHandle(sourceComponent);
            }
        }else{
            /* A component of the 'targetContainer' is repositioned inside the 'targetContainer' */
            if (targetContainer == sourceComponentContainer) {
                /* Find the index of the 'selectedTarget' inside the 'targetContainer'. The
                 * 'sourceComponent' will be placed inserted before the 'selectedTarget'.
                 */
                Component[] compos = null;
                if (JMenu.class.isAssignableFrom(targetContainer.getClass()))
                    compos = ((JMenu) targetContainer).getMenuComponents();
                else if (PanelComponent.class.isAssignableFrom(container.getClass()))
                    compos = ((PanelComponent) container).getContentPane().getComponents();
                else
                    compos = targetContainer.getComponents();
                int selectedTargetIndex = -1;
                for (int i=0; i<targetContainer.getComponentCount(); i++) {
                    if (compos[i] == selectedTarget) {
                        selectedTargetIndex = i;
                        break;
                    }
                }
                int sourceComponentIndex = -1;
                for (int i=0; i<targetContainer.getComponentCount(); i++) {
                    if (compos[i] == sourceComponent) {
                        sourceComponentIndex = i;
                        break;
                    }
                }
//                System.out.println(" Repos selectedTargetIndex: " + selectedTargetIndex);
//                System.out.println(" Repos sourceComponentIndex: " + sourceComponentIndex);
                if (selectedTargetIndex != -1 && sourceComponentIndex != -1) {
                    /* Place the component in front of the target. */
                    if (sourceComponentIndex < selectedTargetIndex)
                        selectedTargetIndex--;
                    targetContainer.remove(sourceComponent);
                    targetContainer.add(sourceComponent, selectedTargetIndex);
                    targetNode.remove(sourceNode);
                    targetNode.insert(sourceNode, getNodeIndex(targetNode, selectedTargetIndex));
//                    targetNode.insert(sourceNode, selectedTargetIndex);
//                    sourceNode = getChild(targetNode, sourceNodeName);
                }
            }else{
                /* A component is inserted in the 'targetContainer' from a different container
                 * Ask the user to specify the where in the 'targetContainer' the 'sourceComponent'
                 * should be inserted.
                 */
                int selectedTargetIndex = -1;
    //            System.out.println("targetContainer.getComponentCount(): " + targetContainer.getComponentCount());
                int compoCount = targetContainer.getComponentCount();
                if (compoCount > 0) {
                    if (compLocationDialog == null) {
                        Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);
                        compLocationDialog = new ComponentLocationDialog(topLevelFrame);
                    }

                    String[] compoNames = new String[compoCount];
    //                System.out.println("compoNames.length: " + compoNames.length);
                    int counter = 0;
                    for (int i=0; i<targetNode.getChildCount(); i++) {
                        ObjectHierarchyTreeNode node = (ObjectHierarchyTreeNode) targetNode.getChildAt(i);
                        if (node.uiHierarchyNode) {
                            compoNames[counter] = node.objectName;
    //                        System.out.println("compoNames[" + counter + "]: " + compoNames[counter]);
                            counter++;
                        }
                    }

                    compLocationDialog.setContainer(compoNames);
                    selectedTargetIndex = compLocationDialog.showDialog(this);
                }else
                    selectedTargetIndex = 0;

                if (selectedTargetIndex == -1) return;;
                sourceComponent.getParent().remove(sourceComponent);
                targetContainer.add(sourceComponent, selectedTargetIndex);
                ((ObjectHierarchyTreeNode) sourceNode.getParent()).remove(sourceNode);
//                targetNode.insert(sourceNode, selectedTargetIndex);
                targetNode.insert(sourceNode, getNodeIndex(targetNode, selectedTargetIndex));

                /* If the object is moved to 'targetContainer' from another Container and these java.awt.Containers
                 * belong to objects with different ESlateHandles in the microworld, then if 'sourceComponent' has
                 * an attached script, the script will be lost. To avoid this we explicitly update the handle of the
                 * sourceComponent in the ScriptListenerMap instance.
                 */
                updateHandle(sourceComponent);
            }
        }

        sourceComponentContainer.validate();
        sourceComponentContainer.doLayout();
        sourceComponentContainer.repaint();
        targetContainer.validate();
        targetContainer.doLayout();
        targetContainer.repaint();
        ((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).revalidate();
        selectNode(sourceNode);
    }

    private void updateHandle(Object object) {
        if (container != null && container.getClass().getName().equals("gr.cti.eslate.base.container.ESlateComposer"))
            ((ESlateComposer) container).getScriptListenerMap().updateAWTObjectHandle(object);
    }

    public void showObjectHierarchyMenu(int x, int y) {
        if (treeNotInitialized) return;
        if (objectHierarchyMenu == null)
            getObjectHierarchyMenu();

        componentHierarchyItem2.setSelected(((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).displayComponentHierarchy);
        objectHierarchyItem2.setSelected(((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).displayObjectHierarchy);
        uiHierarchyItem2.setSelected(((TreeComboBox.TreeToListModel) objectHierarchyComboBox.getModel()).displayUIHierarchy);
        ListEntry selectedEntry = (ListEntry) objectHierarchyComboBox.getSelectedItem();
        ObjectHierarchyTreeNode node = (ObjectHierarchyTreeNode) selectedEntry.object;
        if (selectedEntry != null && node != null) {
            if (Container.class.isAssignableFrom(node.getUserObject().getClass())) {
                addComponentItem.setEnabled(true);
            }else{
                addComponentItem.setEnabled(false);
            }
//            System.out.println(node.getLevel());
            if (!isSecondLevelNode(node) && Component.class.isAssignableFrom(node.getUserObject().getClass())) {
                removeComponentItem.setEnabled(true);
            }else{
                removeComponentItem.setEnabled(false);
            }
        }else{
            addComponentItem.setEnabled(false);
            removeComponentItem.setEnabled(false);
        }

        objectHierarchyMenu.show(objectHierarchyComboBox, x, y);
    }

    private void getObjectHierarchyMenu() {
        objectHierarchyMenu = new ESlatePopupMenu();

        // COMPONENT HIERARCHY
        String menuString = beanInfoDialogBundle.getString("ComponentHierachy");
        componentHierarchyItem2 = (JCheckBoxMenuItem) objectHierarchyMenu.add(new JCheckBoxMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            componentHierarchyItem2.setFont(greekUIFont);

        componentHierarchyItem2.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setComponentHierarchyVisible(componentHierarchyItem2.isSelected());
            }
        });

        // UI HIERARCHY
        menuString = beanInfoDialogBundle.getString("UIHierachy");
	uiHierarchyItem2 = (JCheckBoxMenuItem) objectHierarchyMenu.add(new JCheckBoxMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            uiHierarchyItem2.setFont(greekUIFont);

        uiHierarchyItem2.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setUIHierarchyVisible(uiHierarchyItem2.isSelected());
            }
        });

        // OBJECT HIERARCHY
        menuString = beanInfoDialogBundle.getString("ObjectHierachy");
        objectHierarchyItem2 = (JCheckBoxMenuItem) objectHierarchyMenu.add(new JCheckBoxMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            objectHierarchyItem2.setFont(greekUIFont);

        objectHierarchyItem2.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setObjectHierarchyVisible(objectHierarchyItem2.isSelected());
            }
        });

        // ADD COMPONENT
        menuString = beanInfoDialogBundle.getString("Add");
	addComponentItem = (JMenuItem) objectHierarchyMenu.add(new JMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            addComponentItem.setFont(greekUIFont);

        addComponentItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                addNewComponent();
            }
        });

        // COMPONENT HIERARCHY
        menuString = beanInfoDialogBundle.getString("Remove");
	removeComponentItem = (JMenuItem) objectHierarchyMenu.add(new JMenuItem(menuString));
//        if (beanInfoDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BeanInfoDialogBundle_el_GR"))
//            removeComponentItem.setFont(greekUIFont);

        removeComponentItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                removeSelectedComponent();
            }
        });

        objectHierarchyMenu.pack();
    }
}

abstract class EditorPropertyChangeListener implements PropertyChangeListener {
    Method propertySetterMethod;

    public EditorPropertyChangeListener(Method setter) {
        this.propertySetterMethod = setter;
    }
}

abstract class EditorItemListener implements ItemListener {
    Method propertySetterMethod;
    PropertyEditor propEditor;

    public EditorItemListener(PropertyEditor editor, Method setter) {
        this.propertySetterMethod = setter;
        this.propEditor = editor;
    }
}


class TreeComboBox extends JComboBox {
    WinComboBoxUI ui;
    TreeToListModel model = null;
//    javax.swing.plaf.basic.BasicComboBoxUI ui;

    public TreeComboBox() {
        super();
        setRenderer(new ListEntryRenderer());
        ui = new WinComboBoxUI();
        setUI(ui);
    }

    public TreeComboBox(DefaultTreeModel aTreeModel) {
        this();
        setModel(aTreeModel);
//        setRenderer(new ListEntryRenderer());
//        ui = (BasicComboBoxUI) getUI();
//        if (ui.getClass().getName().startsWith("com.sun.java.swing.plaf.windows")) {
//            ui = new WinComboBoxUI();
//            setUI(ui);
//        }
    }

    public void setModel(DefaultTreeModel aTreeModel) {
        setModel((model = new TreeToListModel(aTreeModel)));
    }

    public void updateUI() {
//        ui = new WinComboBoxUI();
        if (ui != null && ui.getArrowButton() != null) {
//            ui.getArrowButton().updateUI();
            setUI(ui);
//            Border b = UIManager.getBorder("ComboBox.border");
//            System.out.println("Setting combo's border to: " + b);
//            setBorder(b);
//            ui.updateArrowButton();
        }
        ListEntryRenderer.expandedIcon = (javax.swing.Icon) UIManager.get("Tree.expandedIcon" );
        ListEntryRenderer.collapsedIcon = (javax.swing.Icon) UIManager.get("Tree.collapsedIcon");
    }

    public ObjectHierarchyTreeNode getSelectedNode() {
        ListEntry selectedEntry = (ListEntry) getSelectedItem();
        if (selectedEntry == null) return null;
        return (ObjectHierarchyTreeNode) selectedEntry.object;
    }

    public void clearModel() {
        model.cache.clear();
        model.currentValue = null;
        ((ObjectHierarchyTreeNode) model.source.getRoot()).removeFromParent();
        model.emptied = true;
    }

    class TreeToListModel extends AbstractListModel implements ComboBoxModel,TreeModelListener {
        DefaultTreeModel source;
        boolean invalid = true;
        boolean emptied = false;
        Object currentValue;
        Vector cache = new Vector();
        /* These two boolean variables regulates the display of the two hierarchies that constitute
         * the contents of an object.
         */
        boolean displayUIHierarchy = false;
        boolean displayObjectHierarchy = false;
        boolean displayComponentHierarchy = true;

        public TreeToListModel(DefaultTreeModel aTreeModel) {
            source = aTreeModel;
            aTreeModel.addTreeModelListener(this);
            setRenderer(new ListEntryRenderer());
        }

        public void setSelectedItem(Object anObject) {
            currentValue = anObject;
            fireContentsChanged(this, -1, -1);
        }

        public Object getSelectedItem() {
            return currentValue;
        }

        public int getSize() {
            validate();
            return cache.size();
        }

        public Object getElementAt(int index) {
            return cache.elementAt(index);
        }

        public void treeNodesChanged(TreeModelEvent e) {
            invalid = true;
        }

        public void treeNodesInserted(TreeModelEvent e) {
            invalid = true;
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            invalid = true;
        }

        public void treeStructureChanged(TreeModelEvent e) {
            invalid = true;
        }
        void validate() {
            if(invalid) {
                cache = new Vector();
                if (!emptied) {
                    cacheTree(source.getRoot(),0);
                    if(cache.size() > 0)
                        currentValue = cache.elementAt(0);
                }else
                    currentValue = null;
                invalid = false;
                fireContentsChanged(this, 0, 0);
            }
        }

        void revalidate() {
            invalid = true;
            validate();
        }

        void cacheTree(Object anObject,int level) {
            ObjectHierarchyTreeNode node = (ObjectHierarchyTreeNode) anObject;
            if(source.isLeaf(anObject)) {
                if (node.isVisible()) {
                    if (node.getLevel() > 1) {
                        if (!((displayUIHierarchy && node.uiHierarchyNode)
                        || (displayComponentHierarchy && node.componentHierarchyNode)
                        || (displayObjectHierarchy && node.objectHierarchyNode))) {
                            return;
                        }
                    }
                    addListEntry(anObject,level,false, TreeComboBox.this);
                }
            }else {
                int c = source.getChildCount(anObject);
                int i;
                Object child;

                if (node.isVisible()) {
                    if (node.getLevel() > 1) {
                        if (!((displayUIHierarchy && node.uiHierarchyNode)
                        || (displayComponentHierarchy && node.componentHierarchyNode)
                        || (displayObjectHierarchy && node.objectHierarchyNode))) {
                            return;
                        }
                    }
//                    System.out.println("Adding list entry: " + ((ObjectHierarchyTreeNode) anObject).objectName);
                    addListEntry(anObject,level,true, TreeComboBox.this);
                    level++;

                    for(i=0;i<c;i++) {
                        child = source.getChild(anObject,i);
                        cacheTree(child,level);
                    }

                    level--;
                }
            }
        }

        void addListEntry(Object anObject,int level,boolean isNode, TreeComboBox comboBox) {
            cache.addElement(new ListEntry(anObject,level,isNode, comboBox));
        }
    }
}

class ListEntry {
    Object object;
    int    level;
    boolean isNode;
    JComboBox comboBox;
    /* An entry gets into the 'dragged' state, when it is dragged onto another entry. */
    boolean dragged = false;

    public ListEntry(Object anObject,int aLevel,boolean isNode, JComboBox combo) {
        object = anObject;
        level = aLevel;
        this.isNode = isNode;
        this.comboBox = combo;
        /* ListEntry is the type of the objects that are inserted in the TreeComboBox.
         * Each ListEntry object carries an ObjectHierarchyTreeNode object. From a ListEntry
         * object it is straightforward to get its ObjectHierarchyTreeNode object. The opposite
         * is also needed, so here a the other way link is established.
         */
        if (ObjectHierarchyTreeNode.class.isAssignableFrom(anObject.getClass()))
            ((ObjectHierarchyTreeNode) anObject).entry = this;
    }

    public Object object() {
        return object;
    }

    public int level() {
        return level;
    }

    public boolean isNode() {
        return isNode;
    }
}

class ListEntryRenderer extends JLabel implements ListCellRenderer  {
    static javax.swing.Icon expandedIcon = (javax.swing.Icon) UIManager.get("Tree.expandedIcon" );
    static javax.swing.Icon collapsedIcon = (javax.swing.Icon) UIManager.get("Tree.collapsedIcon");
    static javax.swing.Icon expandedComponentIcon = new ImageIcon(ListEntryRenderer.class.getResource("images/javaComponentMinus.gif"));
    static javax.swing.Icon collapsedComponentIcon = new ImageIcon(ListEntryRenderer.class.getResource("images/javaComponentPlus.gif"));
    public static final int OFFSET = 16;
    static final Border emptyBorder = new EmptyBorder(0,0,0,0);
    static final Border lineBorder = new LineBorder(Color.black);
    static Color secondLevelColor = new Color(95, 201, 136);

    public ListEntryRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(
                                                  JList listbox,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        ListEntry listEntry = (ListEntry)value;
        if (listEntry != null) {
            ObjectHierarchyTreeNode node = (ObjectHierarchyTreeNode) listEntry.object;
            if (isSelected) {
                setBackground(Color.yellow); //UIManager.getColor("ComboBox.selectionBackground"));
                setForeground(Color.black); //UIManager.getColor("ComboBox.selectionForeground"));
                if (node.dataType != null)
                    setText(node.objectName + ' ' + node.dataType);
                else
                    setText(node.objectName);
                listbox.setToolTipText(node.toString());
            }else{
                if (listEntry.level == 1) {
                    setBackground(secondLevelColor);
                }else{
                    setBackground(UIManager.getColor("ComboBox.background"));
                }
                if (listEntry.dragged)
                    setForeground(Color.lightGray);
                else
                    setForeground(UIManager.getColor("ComboBox.foreground"));
                setText(node.objectName);
            }

            Border border;
            if (index == -1 && listEntry.comboBox.isPopupVisible())
                border = emptyBorder;
            else
                border = new EmptyBorder(0, OFFSET * listEntry.level(), 0, 0);
            setBorder(border);

            if (listEntry.comboBox.isPopupVisible()) {
//                System.out.println("Node:" + node.objectName + ", node.isExpanded(): " + node.isExpanded());
//                System.out.println("node.nestedObjectsFound: " + node.nestedObjectsFound + ", node._AWTchildrenFound: " + node._AWTchildrenFound);
//                if (node.isLeaf() && node.introspected)
//                    setIcon(null);
//                else{
                    if (!node.isExpanded() || !(node.nestedObjectsFound || node._AWTchildrenFound || node.hostedComponentsFound)) {
                        if (node.uiHierarchyNode) {
                            setIcon(collapsedComponentIcon);
                        }else{
                            setIcon(collapsedIcon);
                        }
                    }else{
                        if (node.uiHierarchyNode) {
                            setIcon(expandedComponentIcon);
                        }else{
                            setIcon(expandedIcon);
                        }
                    }
//                }
            }else{
                setIcon(null);
            }
        }else{
            setText("");
        }
        return this;
    }
}

class ObjectHierarchyTreeNode extends javax.swing.tree.DefaultMutableTreeNode {
    /* The name of the method of the parent node, through which the component (user object) of this
     * node is accessible from the parent. This method exists only for those nodes which belong
     * to the object hierarchy, nodes whose objects are nested objects of the parent node, accessed
     * though some method of the component of the parent node; that is nodes that are not connected
     * with a AWT Container-Component relationship.
     */
    String accessorMethodName = null;

    String objectName;
    String dataType;
    /* Indicates whether the properties for the object of this node have been introspected */
    boolean introspected = false;
    /* Indicates whether the child AWT components of this object have been tracked down. */
    boolean _AWTchildrenFound = false;
    /* Indicates whether objects which are referenced from this object - and this reference
     * can be located- have been tracked down. */
    boolean nestedObjectsFound = false;
    /* Indicates whether components which are parented by this component have been
     * tracked down.
     */
    boolean hostedComponentsFound = false;

    boolean AWTchildrenFound = false;
    ListEntry entry = null;
    private boolean expanded = true;

    Component[] propertyEditorComponents;
    PropertyDescriptor[] usablePropertyDescriptors;
    PropertyDescriptor[] descriptorsWithoutRegisteredPropertyEditors;
    boolean[] readOnlyProperty;
    Object[] propertyEditors;
    BeanInfoDialog beanInfoDialog;
    String pathToRoot = null;
    /* 'uiHierarchyNode' is true for these nodes of the object hierarchy tree, which
     * are part of the component UI hierarchy tree (getParent(), getComponents() in java.awt.Container).
     * We distinguish these nodes, so as to be able to display:
     * a. all the object hierarchy
     * b. the UI hierarchy only
     * c. the object hierarchy without the component hierarchy.
     */
    boolean uiHierarchyNode = false;
    /* 'componentHierarchyNode' is true for these nodes of the component hierarchy tree.
     */
    boolean componentHierarchyNode = false;
    /* 'objectHierarchyNode' is true for these nodes of the object hierarchy tree.
     */
    boolean objectHierarchyNode = false;
    Object sizeEditor = null;
    Object locationEditor = null;
    Object locationOnScreenEditor = null;
    Object boundsEditor = null;
    ComponentListener listener = null;
    /* Contains the names of all the valid property categories for the properties of the
     * current component(node).
     */
    Array propertyCategories = new Array();
    /* The class of the customizer of the Bean, if one exists. */
    Class customizerClass = null;
    /* This property checks whether the object hierarchy of this node has been explored.
     * This exploration takes place in 'findBeanProperties()' and 'findNestedObjects()'. In 'findBeanProperties()'
     * the exploration may be cancelled if the object hierarcy is not displayed or a particulat category
     * of properties is being serached for.
     */
    boolean objHierarchyUnchecked = true;
    /* The E-Slate component editor tracks nested objects which are indexed, i.e. are returned by
     * methods which accept a single int argument. These methods access arrays. The index variable
     * holds the index of the object described by this node in the array. The object cannot be
     * accessed without this index.
     */
    int objectIndex = -1;



    public ObjectHierarchyTreeNode(Object object, String objectName, BeanInfoDialog dialog) {
        super(object);
        if (objectName == null) objectName = "";
        int parenthesisIndex = objectName.indexOf('(');
        if (parenthesisIndex == -1)
            this.objectName = objectName;
        else{
            this.objectName = objectName.substring(0, parenthesisIndex);
            this.dataType = objectName.substring(parenthesisIndex);
        }
        this.beanInfoDialog = dialog;

        /* If the object for which this node is created is a java.awt.Component descendant
         * add a component listener to it, which will update all these property editors which
         * are used for the component's properties 'size', 'bounds', 'location' and 'locationOnScreen'.
         */
       if (Component.class.isAssignableFrom(object.getClass())) {
            Component comp = (Component) object;
            comp.addComponentListener(listener = new ComponentAdapter() {
                public void componentMoved(ComponentEvent e) {
                    if (locationEditor == null)
                        locationEditor = getEditor("location");
                    if (locationEditor != null) {
                        if (PropertyEditor.class.isAssignableFrom(locationEditor.getClass()))
                            ((PropertyEditor) locationEditor).setValue(((Component) e.getSource()).getLocation());
                    }
                    if (locationOnScreenEditor == null)
                        locationOnScreenEditor = getEditor("locationOnScreen");
                    if (locationOnScreenEditor != null) {
                        if (PropertyEditor.class.isAssignableFrom(locationOnScreenEditor.getClass()))
                            ((PropertyEditor) locationOnScreenEditor).setValue(((Component) e.getSource()).getLocationOnScreen());
                    }
                    if (boundsEditor == null)
                        boundsEditor = getEditor("bounds");
                    if (boundsEditor != null) {
                        if (PropertyEditor.class.isAssignableFrom(boundsEditor.getClass()))
                            ((PropertyEditor) boundsEditor).setValue(((Component) e.getSource()).getBounds());
                    }
                }
                public void componentResized(ComponentEvent e) {
                    if (sizeEditor == null)
                        sizeEditor = getEditor("size");
                    if (sizeEditor != null) {
                        if (PropertyEditor.class.isAssignableFrom(sizeEditor.getClass()))
                            ((PropertyEditor) sizeEditor).setValue(((Component) e.getSource()).getSize());
                    }
                    if (boundsEditor == null)
                        boundsEditor = getEditor("bounds");
                    if (boundsEditor != null) {
                        if (PropertyEditor.class.isAssignableFrom(boundsEditor.getClass()))
                            ((PropertyEditor) boundsEditor).setValue(((Component) e.getSource()).getBounds());
                    }
                }
            });
        }
  }

    public Object getEditor(String propertyName) {
        if (usablePropertyDescriptors == null) return null;
        int i = 0;
        for (; i<usablePropertyDescriptors.length; i++) {
            if (usablePropertyDescriptors[i].getName().equals(propertyName))
                break;
        }
        if (i<usablePropertyDescriptors.length)
            return propertyEditors[i];
        return null;
    }

    public void removeFromParent() {
//        System.out.println("RemoveFromParent() " + objectName);
//        if (Component.class.isAssignableFrom(getUserObject().getClass()))
//            ((Component) getUserObject()).removeComponentListener(listener);
        for (int i=0; i<getChildCount(); i++) {
            ((ObjectHierarchyTreeNode) getChildAt(i)).removeFromParent();
            i--;
        }
        if (listener != null) {
            ((Component) getUserObject()).removeComponentListener(listener);
            listener = null;
        }
        super.removeFromParent();
        objectName = null;
        entry = null;
        propertyEditorComponents = null;
        usablePropertyDescriptors = null;
        readOnlyProperty = null;
        propertyEditors = null;
        pathToRoot = null;
        dataType = null;
        beanInfoDialog = null;
        sizeEditor = null;
        locationEditor = null;
        boundsEditor = null;
        locationOnScreenEditor = null;
        propertyCategories.clear();
        customizerClass = null;
        descriptorsWithoutRegisteredPropertyEditors = null;
        objHierarchyUnchecked = true;
    }

    public void expand(boolean revalidate) {
//        System.out.println("Expanding " + this.objectName);
//        expanded = true;
        /* Check the node parents till the root. If any of them is not expanded, expand it.
         */
        ObjectHierarchyTreeNode parent = (ObjectHierarchyTreeNode) this.getParent();
        if (parent != null && !parent.isExpanded())
            parent.expand(false);


        /* Expand the contents of the node.
         */
        int childCount = getChildCount();
        for (int i=0; i<childCount; i++) {
            ObjectHierarchyTreeNode nd = (ObjectHierarchyTreeNode) getChildAt(i);
//            System.out.println("nd: " + nd.objectName);
            nd.expanded = true;
        }
        if (revalidate && beanInfoDialog.objectHierarchyComboBox != null)
            ((TreeComboBox.TreeToListModel) beanInfoDialog.objectHierarchyComboBox.getModel()).revalidate();
    }

    public void collapse() {
//        expanded = false;
        int childCount = getChildCount();
        for (int i=0; i<childCount; i++) {
            ObjectHierarchyTreeNode nd = (ObjectHierarchyTreeNode) getChildAt(i);
            nd.expanded = false;
        }
        if (beanInfoDialog.objectHierarchyComboBox != null)
            ((TreeComboBox.TreeToListModel) beanInfoDialog.objectHierarchyComboBox.getModel()).revalidate();
    }

    public boolean isExpanded() {
        if (getChildCount() == 0) return true;
        return ((ObjectHierarchyTreeNode) getChildAt(0)).expanded;
    }

    public boolean isVisible() {
        if (isRoot()) return true;
        return ((ObjectHierarchyTreeNode) getParent()).isExpanded();
    }

    public ObjectHierarchyTreeNode getChild(String name) {
//        System.out.println("In getChild(" + name + ") of node " + objectName);
        for (int i=0; i<getChildCount(); i++) {
//            System.out.println("   " + ((ObjectHierarchyTreeNode) getChildAt(i)).objectName);
            if (((ObjectHierarchyTreeNode) getChildAt(i)).objectName.equals(name))
                return (ObjectHierarchyTreeNode) getChildAt(i);
        }
        return null;
    }

    public String[] getPathNodeNames() {
        String[] nodeNames = null;
        if (getParent() != null) {
            nodeNames = new String[getLevel()+1];
            TreeNode[] nodes = getPath();
            for (int i=0; i<nodes.length; i++) {
                nodeNames[i] = ((ObjectHierarchyTreeNode) nodes[i]).objectName;
            }
        }
        return nodeNames;
    }

    public String toString() {
        if (getParent() != null) {
            if (pathToRoot == null) {
                StringBuffer pathToRootBuffer = new StringBuffer();
                TreeNode[] nodes = getPath();
                for (int i=0; i<nodes.length; i++) {
                    pathToRootBuffer.append(((ObjectHierarchyTreeNode) nodes[i]).objectName);
                    pathToRootBuffer.append("->");
                }
                pathToRootBuffer.setLength(pathToRootBuffer.length()-2);
                pathToRoot = pathToRootBuffer.toString();
            }
        }else
            return objectName;
        return pathToRoot;
    }
}

class WinComboBoxUI extends javax.swing.plaf.basic.BasicComboBoxUI { //com.sun.java.swing.plaf.windows.WindowsComboBoxUI {
    JScrollPane myscroller;
    JComboBox mycomboBox;
    boolean myhasEntered = false;
    boolean myisAutoScrolling = false;
    int myscrollDirection = MY_SCROLL_UP;
    static final int MY_SCROLL_UP = 0;
    static final int MY_SCROLL_DOWN = 1;
    boolean myvalueIsAdjusting;
    protected javax.swing.Timer myautoscrollTimer;
    /* Used to indicate when a component drag has been initiated. */
    boolean draggingCompo = false;
    /* When true, the clicked node is really selected, i.e. will be introspected. This happens
     * only when the click occurs anywhere but on the icon of the node. When the node's icon is
     * clicked, then the node is visited just to traverse deeper nodes, so we consider the node
     * not really selected and no introspection takes place.
     */
    boolean reallySelected = false;
    /* 'source' is the entry being dragged. 'target' is the Entry on which 'source' was dropped */
    ListEntry source = null, target = null;

    public JButton getArrowButton() {
        return arrowButton;
    }

    public javax.swing.plaf.basic.BasicComboPopup getPopup() {
        return (javax.swing.plaf.basic.BasicComboPopup) popup;
    }

    public boolean isPopupVisible( JComboBox c ) {
        if (popup == null) return false;
        return popup.isVisible();
    }

    protected javax.swing.plaf.basic.ComboPopup createPopup() {
        final WinComboPopup popup = new WinComboPopup(comboBox);
        popup.getAccessibleContext().setAccessibleParent(comboBox);
        popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {}
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                /* The CompoBox's popup may close without the user selecting smth in it,
                 * e.g it may close by selecting some other popup. In this case make sure
                 * that the selected item in the popup gets introspected.
                 */
//                System.out.println("mycomboBox.getSelectedIndex(): " + mycomboBox.getSelectedIndex());
//                System.out.println("list.getSelectedIndex(): " + popup.getList().getSelectedIndex());
//                if (mycomboBox.getSelectedIndex() != popup.getList().getSelectedIndex()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {

//                            System.out.println("Popup menu listener");

                            reallySelected = true;
                            mycomboBox.setSelectedIndex(popup.getList().getSelectedIndex());
                            reallySelected = false;
                        }
                    });
//                }
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });
        return popup;
    }

    class WinComboPopup extends javax.swing.plaf.basic.BasicComboPopup {
        Border border = new javax.swing.border.LineBorder(Color.black);
        private boolean dontHide = false;

        public WinComboPopup(JComboBox c) {
            super(c);
            uninstallListMouseListener();
//            uninstallListMouseMotionListener();
            uninstallMouseMotionListener();
//            System.out.println("Creating WinComboPopup");
            installNewListListener();
            installNewListMouseMotionListener();

            /* Find the JScrollPane which contains the JList.
             */
            Container container = getList();
            while (container != null && !JScrollPane.class.isAssignableFrom(container.getClass()))
                container = container.getParent();
            if (container != null) {
                myscroller = (JScrollPane) container;
            }

            mycomboBox = c;
        }

        public void uninstallListMouseListener() {
            getList().removeMouseListener(listMouseListener);
        }
/*        public void uninstallListMouseMotionListener() {
            getList().removeMouseMotionListener( listMouseMotionListener );
        }
*/        public void uninstallMouseMotionListener() {
            getList().removeMouseMotionListener( mouseMotionListener );
        }

        protected void togglePopup() {
            if (isVisible()) {
                hide();
            }else{
                if (getSize().height == 0) {// The first time the popup is displayed
                    show();
//                    getList().invalidate();
//                    getList().revalidate();
//                    getList().repaint();
                    myscroller.invalidate();
//                    myscroller.revalidate();
//                    myscroller.repaint();
                }else{ // The rest of the times the popup is displayed, maintain its height
                    setProperHeight();
//                    System.out.println("togglePopup --> New popup size: " + getSize());
                    getList().ensureIndexIsVisible(getList().getSelectedIndex() );
                    show(mycomboBox, 0, mycomboBox.getSize().height);
                }
            }
        }

        protected void setProperHeight() {
            Point comboLocation = mycomboBox.getLocation();
            /* The location of the list is the location of the comboB box plus its height.
             */
            Point listLocationOnScreen = new Point(comboLocation.x, comboLocation.y+mycomboBox.getSize().height);
            SwingUtilities.convertPointToScreen(listLocationOnScreen, mycomboBox);
            int restScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().height - listLocationOnScreen.y - 50;
            Dimension listSize = getList().getSize();
            Dimension popupSize = getSize();
//            System.out.println("listSize: " + listSize + ", popupSize: " + popupSize);
//            System.out.println("restScreenHeight: " + restScreenHeight);
            if (popupSize.height < listSize.height) {
                if (listSize.height < restScreenHeight) {
                    popupSize.height = listSize.height;
                }else{
                    popupSize.height = restScreenHeight;
                }
            }else{
                if (listSize.height != 0) {
                    int requiredListHeight = 0;
                    if (getList().getCellBounds(0, 0) != null)
                        requiredListHeight = ((TreeComboBox.TreeToListModel) getList().getModel()).getSize() * getList().getCellBounds(0, 0).height;
//                    System.out.println("requiredListSize: " + requiredListHeight);
                    if (requiredListHeight < restScreenHeight) {
                        popupSize.height = requiredListHeight;
                    }else{
                        popupSize.height = restScreenHeight;
                    }
                }
            }
            popupSize.width = mycomboBox.getSize().width;
            setPopupSize(popupSize);
            setSize(popupSize);
//            System.out.println("New popup size: " + popupSize + ", getSize(): " + getSize());
            Dimension myscrollerSize = new Dimension(popupSize.width, popupSize.height-2);
            myscroller.setMaximumSize( myscrollerSize );
            myscroller.setPreferredSize( myscrollerSize );
            myscroller.setMinimumSize( myscrollerSize );
            myscroller.invalidate();
//            myscroller.doLayout();
//            myscroller.repaint();
            synchronized(getTreeLock()) {
                validateTree();
            }
        }

        protected void updateListBoxSelectionForEvent(MouseEvent anEvent,boolean shouldScroll) {
            Point location = anEvent.getPoint();
            if ( list == null )
                return;
            int index = getList().locationToIndex(location);
            if ( index == -1 ) {
                if ( location.y < 0 )
                    index = 0;
                else
                    index = mycomboBox.getModel().getSize() - 1;
            }
            if (getList().getSelectedIndex() != index ) {
//                System.out.println("updateListBoxSelectionForEvent setSelectedIndex: " + index);
                getList().setSelectedIndex(index);
                if ( shouldScroll )
                    getList().ensureIndexIsVisible(index);
            }
        }

        //===================================================================
        // begin Autoscroll methods
        //

        /**
         * Called by BasicComboPopup$InvocationMouseMotionHandler to handle auto-
         * scrolling the list.
         */
        protected void startAutoScrolling( int direction ) {
            if ( isAutoScrolling ) {
                myautoscrollTimer.stop();
            }

            isAutoScrolling = true;

            if ( direction == MY_SCROLL_UP ) {
                myscrollDirection = MY_SCROLL_UP;
                Point convertedPoint = SwingUtilities.convertPoint( scroller, new Point( 1, 1 ), list );
                int top = list.locationToIndex( convertedPoint );
                myvalueIsAdjusting = true;
                list.setSelectedIndex( top );
                myvalueIsAdjusting = false;

                ActionListener timerAction = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        autoScrollUp();
                    }
                };

                myautoscrollTimer = new javax.swing.Timer( 100, timerAction );
            }
            else if ( direction == MY_SCROLL_DOWN ) {
                myscrollDirection = MY_SCROLL_DOWN;
                Dimension size = scroller.getSize();
                Point convertedPoint = SwingUtilities.convertPoint( myscroller,
                                                                    new Point( 1, (size.height - 1) - 2 ),
                                                                    list );
                int bottom = list.locationToIndex( convertedPoint );
                myvalueIsAdjusting = true;
                list.setSelectedIndex( bottom );
                myvalueIsAdjusting = false;

                ActionListener timerAction = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        autoScrollDown();
                    }
                };

                myautoscrollTimer = new javax.swing.Timer( 100, timerAction );
            }
            myautoscrollTimer.start();
        }

        protected void stopAutoScrolling() {
            isAutoScrolling = false;

            if ( myautoscrollTimer != null ) {
                myautoscrollTimer.stop();
                myautoscrollTimer = null;
            }
        }

        protected void autoScrollUp() {
            int index = list.getSelectedIndex();
            if ( index > 0 ) {
                myvalueIsAdjusting = true;
                list.setSelectedIndex( index - 1 );
                myvalueIsAdjusting = false;
                list.ensureIndexIsVisible( index - 1 );
            }
        }

        protected void autoScrollDown() {
            int index = list.getSelectedIndex();
            int lastItem = list.getModel().getSize() - 1;
            if ( index < lastItem ) {
                myvalueIsAdjusting = true;
                list.setSelectedIndex( index + 1 );
                myvalueIsAdjusting = false;
                list.ensureIndexIsVisible( index + 1 );
            }
        }

        //
        // end Autoscroll methods
        //=================================================================

        public void installNewListMouseMotionListener() {
            getList().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
/*                public void mouseMoved( MouseEvent anEvent ) {
                    Point location = anEvent.getPoint();
                    Rectangle r = new Rectangle();
                    list.computeVisibleRect( r );
                    if ( r.contains( location ) ) {
                        myvalueIsAdjusting = true;
                        System.out.println("Calling updateListBoxSelectionForEvent");
//                        updateListBoxSelectionForEvent( anEvent, false );
                        myvalueIsAdjusting = false;
                    }
                }
*/                public void mouseDragged( MouseEvent e ) {
                    if ( isVisible() ) {
                        /* Component dragging is allowed only when the TreeComboBox shows the component
                         * hierarchy and not both the component and object hierarchy or just the object
                         * hierarchy.
                         */
                        if (!dontHide && ((TreeComboBox.TreeToListModel) ((TreeComboBox) mycomboBox).getModel()).displayUIHierarchy &&
                            !((TreeComboBox.TreeToListModel) ((TreeComboBox) mycomboBox).getModel()).displayObjectHierarchy) {
                            getList().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                            draggingCompo = true;
                            source.dragged = true;
                        }else
                            draggingCompo = false;

                        Point convertedPoint = SwingUtilities.convertPoint((Component) e.getSource(),
                                                                           e.getPoint(), getList());
                        MouseEvent newEvent = new MouseEvent((Component)e.getSource(),
                                                             e.getID(),
                                                             e.getWhen(),
                                                             e.getModifiers(),
                                                             convertedPoint.x,
                                                             convertedPoint.y,
                                                             e.getModifiers(),
                                                             e.isPopupTrigger() );

//                        MouseEvent newEvent = convertMouseEvent( e );
                        Rectangle r = new Rectangle();
                        getList().computeVisibleRect( r );

                        if ( newEvent.getPoint().y >= r.y && newEvent.getPoint().y <= r.y + r.height - 1 ) {
                            myhasEntered = true;
                            if ( myisAutoScrolling ) {
                                stopAutoScrolling();
                            }
                            Point location = newEvent.getPoint();
                            if ( r.contains( location ) ) {
                                myvalueIsAdjusting = true;
//                                System.out.println("mouseDragged --> Calling updateListBoxSelectionForEvent");
                                updateListBoxSelectionForEvent( newEvent, false );
                                target = (ListEntry) getList().getSelectedValue();
                                myvalueIsAdjusting = false;
                            }
                        }
                        else {
                            if ( myhasEntered ) {
                                int directionToScroll = newEvent.getPoint().y < r.y ? MY_SCROLL_UP : MY_SCROLL_DOWN;
                                if ( myisAutoScrolling && myscrollDirection != directionToScroll ) {
                                    stopAutoScrolling();
                                    startAutoScrolling( directionToScroll );
                                }
                                else if ( !myisAutoScrolling ) {
                                    startAutoScrolling( directionToScroll );
                                }
                            }
                            else {
                                if ( e.getPoint().y < 0 ) {
                                    myhasEntered = true;
                                    startAutoScrolling( MY_SCROLL_UP );
                                }
                            }
                        }
                    }
                }
            });
        }

        public void installNewListListener() {
            getList().addMouseListener(new MouseAdapter() {
                public void mousePressed( MouseEvent evt ) {
                    if (SwingUtilities.isLeftMouseButton(evt)) {
                        Point p = evt.getPoint();
                        int selectedIndex = getList().getSelectedIndex();
                        ListEntry entry = (ListEntry) getList().getSelectedValue();
                        source = entry;
    //                    mycomboBox.setSelectedIndex( list.getSelectedIndex() );
                        ListEntryRenderer selectedCompo = (ListEntryRenderer) ((ListEntryRenderer) getList().getCellRenderer()).
                                                  getListCellRendererComponent(getList(),
                                                                               getList().getSelectedValue(),
                                                                               selectedIndex,
                                                                               true,
                                                                               true);
//                        System.out.println("1. selectedCompo: " + selectedCompo);
                        Rectangle cellBounds = getList().getCellBounds(selectedIndex, selectedIndex);
                        cellBounds.x = cellBounds.x + entry.level() * ListEntryRenderer.OFFSET;
                        cellBounds.width = cellBounds.width - entry.level() * ListEntryRenderer.OFFSET;
    //                    System.out.println("cellBounds: " + cellBounds);
//                        System.out.println("Point: " + p);
                        Point viewPosition = myscroller.getViewport().getViewPosition();
//                        System.out.println("1. selectedCompo.getIcon(): " + selectedCompo.getIcon());
                        if (selectedCompo.getIcon() != null && p.x > cellBounds.x && p.x < cellBounds.x + selectedCompo.getIcon().getIconWidth()) { //8 is the icon width
                            boolean expanded = ((ObjectHierarchyTreeNode) entry.object).isExpanded();
                            if (expanded)
                                ((ObjectHierarchyTreeNode) entry.object).collapse();
                            else
                                ((ObjectHierarchyTreeNode) entry.object).expand(true);

                            mycomboBox.setSelectedIndex( selectedIndex );
                            setProperHeight();
                            myscroller.getViewport().setViewPosition(viewPosition);
                            dontHide = true;
    //                        System.out.println("Expand/Collapse tree");
                        }
//                        System.out.println("Mouse pressed end");
                    }
                }
                public void mouseReleased(MouseEvent evt) {
                    boolean dragTookPlace = false;
                    if (!dontHide && source != null && target != null && draggingCompo) {
                        getList().setCursor(java.awt.Cursor.getDefaultCursor());
                        ObjectHierarchyTreeNode sourceNode = (ObjectHierarchyTreeNode) source.object;
                        ObjectHierarchyTreeNode targetNode = (ObjectHierarchyTreeNode) target.object;

//                        System.out.println("sourceNode: " + sourceNode);
//                        System.iut.println("targetNode: " + targetNode);
//                        System.out.println("Source: " + sourceNode.objectName);
//                        System.out.println("Target: " + targetNode.objectName);
                        if (!(sourceNode.getUserObject() == targetNode.getUserObject())) {
                            draggingCompo = false;
                            sourceNode.beanInfoDialog.move(source, target, evt.isShiftDown());
                            source.dragged = false;
                            dragTookPlace = true;
                        }
/*                            draggingCompo = false;
                            source.dragged = false;
                            if (!dontHide) {
                                int selectedIndex = getList().getSelectedIndex();
//                                System.out.println("1. Selecting: " + selectedIndex);
                                mycomboBox.setSelectedIndex(selectedIndex);
                                hide();
                            }else
                                dontHide = false;
                        }
*/
                        target = null;
                        dontHide = false;
                        source.dragged = false;
//                        hide();
                        draggingCompo = false;
                        source = null;
                    }

                    if (!dragTookPlace && SwingUtilities.isLeftMouseButton(evt)) {
                        if (!dontHide) {
                            int selectedIndex = getList().getSelectedIndex();
//                                System.out.println("2. Selecting: " + selectedIndex);
                            hide();
                            /* This is now done in the PopupMenuListener, which is triggered
                             * when the popup menu is about to become invisible.
                             */
//                            reallySelected = true;
//                            mycomboBox.setSelectedIndex(selectedIndex);
//                            reallySelected = false;
                        }else
                            dontHide = false;
                    }
                }
            });
        }
    }

}


class AddComponentDialog extends JDialog {
    public static final int OK = 0;
    public static final int CANCEL = 1;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    Locale locale;
    boolean localeIsGreek = false;
    ResourceBundle dialogBundle;
    JComboBox componentNameBox;
    JTextField cardNameField;
    JComboBox orientation;
    LabeledPanelDesc cardNamePanel, orientationPanel;
    SpinField indexPanel;
    int returnCode = CANCEL;
    JButton cancelButton;
    ESlateComposer composer;

    public AddComponentDialog(java.awt.Frame parentFrame, ESlateComposer composer, Container container, BeanInfoDialog beanInfoDialog) {
        super(parentFrame, true);
        this.composer = composer;

        locale = Locale.getDefault();
        dialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.AddComponentDialogBundle", locale);
        if (dialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.AddComponentDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(dialogBundle.getString("DialogTitle"));

        JLabel componentNameLabel = new JLabel(dialogBundle.getString("ComponentName"));
        componentNameBox = new JComboBox();

        componentNameBox.setEditable(true);
        if (composer != null) {
            //Initialization
            CompoEntry[] menuEntries = null;
            menuEntries = composer.getInstalledComponents().getSortedEntries(true);
            int l = menuEntries.length;
            for (int i=0; i < l; i++)
                componentNameBox.addItem(menuEntries[i].name);
            ComponentTypeCellRenderer ftcr = new ComponentTypeCellRenderer(componentNameBox, composer, menuEntries);
            componentNameBox.setRenderer(ftcr);
        }

        LabeledPanelDesc componentNamePanel = new LabeledPanelDesc(true, componentNameLabel, componentNameBox); //new JPanel(true);

        indexPanel = new SpinField(true, dialogBundle.getString("Index"), container.getComponentCount());
        indexPanel.add(Box.createGlue());
        indexPanel.setMaxValue(container.getComponentCount());
        indexPanel.setMinValue(0);

        JLabel cardNameLabel = new JLabel(dialogBundle.getString("CardName"));
        cardNameField = new JTextField();

        cardNamePanel = new LabeledPanelDesc(true, cardNameLabel, cardNameField); //JPanel(true);

        JLabel orientationLabel = new JLabel(dialogBundle.getString("Orientation"));
        orientation = new JComboBox();
        orientation.addItem("CENTER");
        orientation.addItem("NORTH");
        orientation.addItem("SOUTH");
        orientation.addItem("WEST");
        orientation.addItem("EAST");

        orientationPanel = new LabeledPanelDesc(true, orientationLabel, orientation); //new JPanel(true);

        JButton okButton = new JButton(dialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        cancelButton = new JButton(dialogBundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {componentNamePanel, indexPanel, cardNamePanel, orientationPanel}, 0, 5);
        SpinField.setSpinFieldSpinSize(new SpinField[] {indexPanel}, 60, 20);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        JPanel compoPanel = new JPanel(true);
        ExplicitLayout el = new ExplicitLayout();
        compoPanel.setLayout(el);
        ArrayList<Component> currentControls = new ArrayList<Component>();
        currentControls.add(componentNamePanel);
        if (container.getLayout() != null) {
            if (CardLayout.class.isAssignableFrom(container.getLayout().getClass())) {
                currentControls.add(cardNamePanel);
            }else if (BorderLayout.class.isAssignableFrom(container.getLayout().getClass())) {
                currentControls.add(orientationPanel);
            }else{
                currentControls.add(indexPanel);
            }
        }

        // Layout the controls of the 'compoPanel'
        Component[] controls = new Component[currentControls.size()];
        for (int i=0; i<controls.length; i++)
            controls[i] = currentControls.get(i);

//        Expression maxWidth = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), controls));
//        Expression maxHeight = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredHeight(null), controls));
//        Expression sumHeight = MathEF.sum(GroupEF.createExpressions(ComponentEF.preferredHeight(null), controls));
        Expression maxWidth = GroupEF.preferredWidthMax(controls);
        Expression maxHeight = GroupEF.preferredHeightMax(controls);
        Expression sumHeight = GroupEF.preferredHeightSum(controls);

        ExplicitConstraints ec1 = new ExplicitConstraints(controls[0]);
        ec1.setWidth(ContainerEF.width(compoPanel));
        ec1.setHeight(maxHeight);
        ec1.setX(ContainerEF.left(compoPanel));
        ec1.setY(ContainerEF.top(compoPanel));
        compoPanel.add(controls[0], ec1);
        for (int i=1; i<controls.length; i++) {
            ExplicitConstraints ec = new ExplicitConstraints(controls[i]);
            ec.setWidth(ContainerEF.width(compoPanel));
            ec.setX(ComponentEF.left(controls[0]));
            ec.setY(ComponentEF.bottom(controls[i-1]).add(5)); //5 is the inter-control vertical gap
            ec.setHeight(maxHeight);
            compoPanel.add(controls[i], ec);
        }

        compoPanel.setBorder(new EmptyBorder(5, 4, 6, 4)); //new LineBorder(Color.red)); //
        compoPanel.setAlignmentX(CENTER_ALIGNMENT);
        if (maxWidth.getValue(el) < 350)
            maxWidth = MathEF.constant(350);
        el.setPreferredLayoutSize(maxWidth, sumHeight.add(5));
        el.setMaximumLayoutSize(MathEF.constant(1000), sumHeight.add(5));

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(compoPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createGlue());

        getContentPane().add(mainPanel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("OK clicked");
                if (!cancelButton.hasFocus())
                    returnCode = OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // COMBOBOX key listener (needs special listener, because the KeyStroke mechanism fails
        // when it has the focus.
        orientation.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (orientation.isPopupVisible()) return;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });
        // ESCAPE
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("In VK_ESCAPE keystroke");
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        getRootPane().setDefaultButton(okButton);

//        setResizable(false);
        pack();
        componentNameBox.requestFocus();
        ESlateContainerUtils.showDialog(this, beanInfoDialog, false);
    }

    public String getComponentClassName() {
        /* Normally the comboBox conains the user name of the selected component. So
         * the class name is looked for, and if it is found it is returned. However
         * since the comboBox is editable, the user may have entered the class name
         * for some component. In this case this class name is returned.
         */
        String compoName = (String) componentNameBox.getSelectedItem();
        String className = composer.getInstalledComponents().getClassName(compoName);
        if (className == null)
            return compoName;
        else
            return className;
    }

    public String getCardName() {
        return cardNameField.getText();
    }

    public String getOrientation() {
        String selectedItem = (String) orientation.getSelectedItem();
        if (orientation == null)
            return null;
        if (selectedItem.equals("CENTER"))
            return BorderLayout.CENTER;
        else if (selectedItem.equals("NORTH"))
            return BorderLayout.NORTH;
        else if (selectedItem.equals("SOUTH"))
            return BorderLayout.SOUTH;
        else if (selectedItem.equals("WEST"))
            return BorderLayout.WEST;
        else if (selectedItem.equals("EAST"))
            return BorderLayout.EAST;

        return null;
    }

    public int getIndex() {
        return ((Number) indexPanel.getSpinButton().getValue()).intValue();
    }

    class LabeledPanelDesc extends JPanel implements LabeledPanel {
        JLabel label;
        Component comp;
        ExplicitLayout el = new ExplicitLayout();

        public LabeledPanelDesc(boolean b, JLabel l, Component comp) {
            super(b);
            this.label = l;
            setLayout(el);
            ExplicitConstraints ec1 = new ExplicitConstraints(label);
            ec1.setX(ContainerEF.left(label));
            ec1.setY(ContainerEF.top(label));
            add(label, ec1);
            ExplicitConstraints ec2 = new ExplicitConstraints(comp);
            ec2.setX(ComponentEF.right(label));
            ec2.setY(ContainerEF.top(this));
            ec2.setWidth(ContainerEF.width(this).subtract(ComponentEF.width(label)));
            add(comp, ec2);

            int height = l.getPreferredSize().height;
            if (comp.getPreferredSize().height > height)
                height = comp.getPreferredSize().height;
            setPreferredSize(new Dimension(0, height));
        }

        public JLabel getLabel() {
            return label;
        }

        public ExplicitLayout getExplicitLayout() {
            return el;
        }

    }
}


/* This dialog is used to specify where a compnent should be inserted in a BoxLayout or
 * FlowLayout Container. The location of the new component is specified relatively to the
 * location of a existing component, i.e. before component with name 'yyy'.
 */
class ComponentLocationDialog extends JDialog {
    public static final int OK = 0;
    public static final int CANCEL = 1;
    int selectedComponentIndex = -1;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    Locale locale;
    boolean localeIsGreek = false;
    ResourceBundle componentLocationDialogBundle;
    JComboBox componentNameBox, beforeAfterBox;
    JPanel boxPanel, topPanel;
    JButton cancelButton;
    int returnCode = CANCEL;
    String[] compoNames;

    public ComponentLocationDialog(java.awt.Frame parentFrame) {
        super(parentFrame, true);

        locale = Locale.getDefault();
        componentLocationDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ComponentLocationDialogBundle", locale);
        if (componentLocationDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.ComponentLocationDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(componentLocationDialogBundle.getString("DialogTitle"));

        JLabel qLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
        qLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel msg = new JLabel(componentLocationDialogBundle.getString("SupplyPosition"));

        Object[] ba = new Object[2];
        ba[0] = componentLocationDialogBundle.getString("Before");
        ba[1] = componentLocationDialogBundle.getString("After");
        beforeAfterBox = new JComboBox(ba);

        componentNameBox = new JComboBox();

/*
        if (localeIsGreek) {
            qLabel.setFont(greekUIFont);
            beforeAfterBox.setFont(greekUIFont);
            componentNameBox.setFont(greekUIFont);
        }
*/

        Dimension comboSize = new Dimension(140, 20);
        beforeAfterBox.setMaximumSize(comboSize);
        beforeAfterBox.setMinimumSize(comboSize);
        beforeAfterBox.setPreferredSize(comboSize);
        componentNameBox.setMaximumSize(comboSize);
        componentNameBox.setMinimumSize(comboSize);
        componentNameBox.setPreferredSize(comboSize);

        boxPanel = new JPanel(true);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        boxPanel.add(qLabel);
        boxPanel.add(beforeAfterBox);
        boxPanel.add(Box.createVerticalStrut(2));
        boxPanel.add(componentNameBox);

        topPanel = new JPanel(true);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(qLabel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(boxPanel);

        JButton okButton = new JButton(componentLocationDialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(75, 23);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        cancelButton = new JButton(componentLocationDialogBundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

/*
        if (localeIsGreek) {
            okButton.setFont(greekUIFont);
            cancelButton.setFont(greekUIFont);
        }
*/

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(topPanel);
        mainPanel.add(buttonPanel);
        mainPanel.setBorder(new EmptyBorder(3,3,3,3));

        getContentPane().add(mainPanel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("OK clicked");
                if (!cancelButton.hasFocus())
                    returnCode = OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // COMBOBOX key listener (needs special listener, because the KeyStroke mechanism fails
        // when it has the focus.
        componentNameBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (componentNameBox.isPopupVisible()) return;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });
        beforeAfterBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (beforeAfterBox.isPopupVisible()) return;
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });
        // ESCAPE
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        getRootPane().setDefaultButton(okButton);
    }

    public int showDialog(Component centerAroundComp) {
        setResizable(false);
        pack();
        beforeAfterBox.requestFocus();
        ESlateContainerUtils.showDialog(this, centerAroundComp, false);

        if (returnCode == CANCEL)
            return -1;
        else{
            boolean before = false;
            if (beforeAfterBox.getSelectedItem().equals(componentLocationDialogBundle.getString("Before")))
                before = true;
            String selectedCompo = (String) componentNameBox.getSelectedItem();
            if (selectedCompo == null || selectedCompo.trim().length() == 0)
                return -1;

            int selectedCompoIndex = -1;
            for (int i=0; i<compoNames.length; i++) {
                if (compoNames[i].equals(selectedCompo)) {
                    selectedCompoIndex = i;
                    break;
                }
            }

            if (!before) selectedCompoIndex++;

            return selectedCompoIndex;
        }
    }

    public void setContainer(String[] compoNames) {
        this.compoNames = compoNames;
        componentNameBox.removeAllItems();
        componentNameBox.setModel(new javax.swing.DefaultComboBoxModel(compoNames));
    }
}

class ComboPanel extends JPanel {
    BeanInfoDialog beanInfoDialog;
    public ComboPanel(BeanInfoDialog dialog) {
        super(true);
        beanInfoDialog = dialog;
    }

//    public Dimension getPreferredSize() {
//        return new Dimension(beanInfoDialog.getSize().width, 20);
//    }
    public Dimension getMinimumSize() {
        return new Dimension(10, 20);//beanInfoDialog.getSize().width, 20);
    }
//    public Dimension getMaximumSize() {
//        return new Dimension(beanInfoDialog.getSize().width, 20);
//    }
}

/* This class is a descedant of the JRootPane, whose sole purpose is to change the
 * way the default button of the JRootPane works. In a normal JRootPane the default
 * button is always pressed when <ENTER> is pressed, no matter which UI element of the
 * JRootPane has the focus. In the BeanInfoDialo this is not the desired behaviour, since
 * for example when the user writes smth in a text field and then hits <ENTER>, then
 * the action of the last clicked button of the dialog is triggered at the at user's surprise.
 * This JRootPane descendant will execute the action of the default button, only if the
 * default button is the focus owner.
 */
class MyRootPane extends JRootPane {
    MyDefaultAction myDefaultPressAction, myDefaultReleaseAction;
    JButton myDefaultButton;

    class MyDefaultAction extends javax.swing.AbstractAction {
        JButton owner;
        JRootPane root;
        boolean press;
        MyDefaultAction(JRootPane root, boolean press) {
            this.root = root;
            this.press = press;
        }
        public void setOwner(JButton owner) {
            this.owner = owner;
        }
        public void actionPerformed(ActionEvent e) {
//                System.out.println("MyDefaultAction actionPerformed() press: " + press);
//                System.out.println("owner.hasFocus(): " + owner.hasFocus());
            if (owner.hasFocus() && owner != null && SwingUtilities.getRootPane(owner) == root) {
                ButtonModel model = owner.getModel();
                if (press) {
                    model.setArmed(true);
                    model.setPressed(true);
                } else {
                    model.setPressed(false);
                }
            }
        }
        public boolean isEnabled() {
            return owner.getModel().isEnabled();
        }
    };

    public void setDefaultButton(JButton defaultButton) {
        JButton oldDefault = this.defaultButton;

        if (oldDefault != defaultButton) {
            this.myDefaultButton = defaultButton;

            if (defaultButton != null) {
                if (myDefaultPressAction == null) {
                    myDefaultPressAction = new MyDefaultAction(this, true);
                    myDefaultReleaseAction = new MyDefaultAction(this, false);
                   // Eventually we should get the KeyStroke from the UI
                   // but hardcode it for now....
                    registerKeyboardAction(myDefaultPressAction,
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);

                    registerKeyboardAction(myDefaultReleaseAction,
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);

                    registerKeyboardAction(myDefaultPressAction,
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                                                          InputEvent.CTRL_MASK, false),
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);

                    registerKeyboardAction(myDefaultReleaseAction,
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                                                          InputEvent.CTRL_MASK, true),
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);
                }
                myDefaultPressAction.setOwner(defaultButton);
                myDefaultReleaseAction.setOwner(defaultButton);
            } else {
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false));
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true));
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                                                                InputEvent.CTRL_MASK, false));
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                                                                InputEvent.CTRL_MASK, true));
                myDefaultPressAction = null;
                myDefaultReleaseAction = null;
            }

            if (oldDefault != null) {
                oldDefault.repaint();
            }
            if (defaultButton != null) {
                defaultButton.repaint();
            }
        }

        firePropertyChange("defaultButton", oldDefault, defaultButton);
    }
}

/* The panel included in the Properties tab of the BeanInfoDialog. */
class PropertyEditorPanel extends JPanel {
    private final static int COMPONENT_AREA_WIDTH = 150;
    BeanInfoDialog beanEditor = null;
    JScrollPane scrollPane;
    JPanel mainPanel;
    MsgPanel msgPanel;
    /* The panel displayed in the 'PropertyEditorPanel', when it is empty.
     */
    JPanel noPropertyPanel = null;
    Array propertyPanels;
    int labelMaxWidth;
    /* The last highlighted PropertyPanel */
    PropertyPanel highlightedPanel = null;
    /* This mouse motion listener is added to the mainPanel of the PropertyEditorPanel
     * It clears the message area, whenever the mouse passes over an area of the
     * PropertyEditorPanel which is not covered by a PropertyPanel.
     */
    java.awt.event.MouseMotionAdapter mma1 = new java.awt.event.MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
            if (highlightedPanel != null) {
                highlightedPanel.setHighlighted(false);
                setMessage(null);
            }
        }
    };
    /* This mouse motion listener is added to all the components inside a PropertyPanel.
     * Whenever the mouse moves over such a component, the message area displays the
     * long description for the property which corresponds to this PropertyPanel. The
     * listener also highlights the name of the property in the PropertyEditorPanel.
     */
    java.awt.event.MouseMotionAdapter mma2 = new java.awt.event.MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
            Component comp = (Component) e.getSource();
            PropertyPanel p = (PropertyPanel) SwingUtilities.getAncestorOfClass(PropertyPanel.class, comp);
            if (p == null) return;
            if (highlightedPanel != null && highlightedPanel != p)
                highlightedPanel.setHighlighted(false);
            if (!p.isHighlighted()) {
                p.setHighlighted(true);
                highlightedPanel = p;
                setMessage(p.helpMessage);
            }
        }
    };

    public PropertyEditorPanel(BeanInfoDialog dialog) {
        super();
        beanEditor = dialog;
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        msgPanel = new MsgPanel();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(scrollPane); //, BorderLayout.CENTER);
        add(msgPanel); //, BorderLayout.SOUTH);

        mainPanel.addMouseMotionListener(mma1);
    }

    public void setMessage(String msg) {
        msgPanel.setMessage(msg);
    }

    public void clear() {
        removeAll();
        mainPanel.removeAll();
        if (propertyPanels != null)
            propertyPanels.clear();

        if (noPropertyPanel == null) {
            noPropertyPanel = new JPanel(true);
            noPropertyPanel.setLayout(new BoxLayout(noPropertyPanel, BoxLayout.Y_AXIS));

            JPanel innerNoPropertyPanel = new JPanel(true);
            innerNoPropertyPanel.setLayout(new BoxLayout(innerNoPropertyPanel, BoxLayout.X_AXIS));
            JLabel noPropertyLabel = new JLabel(beanEditor.beanInfoDialogBundle.getString("NoComponent"));
            Font lbFont;
            if (beanEditor.localeIsGreek)
                lbFont = noPropertyLabel.getFont(); //beanEditor.greekUIBoldItalicFont;
            else
                lbFont = new Font(noPropertyLabel.getFont().getFamily(), Font.BOLD | Font.ITALIC, noPropertyLabel.getFont().getSize());

            noPropertyLabel.setFont(lbFont);
            FontMetrics fm = noPropertyLabel.getFontMetrics(lbFont);
            Dimension lbDim = new Dimension(fm.stringWidth(noPropertyLabel.getText())+20, 30);
            noPropertyLabel.setMaximumSize(lbDim);
            noPropertyLabel.setMinimumSize(lbDim);
            noPropertyLabel.setPreferredSize(lbDim);
            noPropertyLabel.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
            noPropertyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            innerNoPropertyPanel.add(Box.createGlue());
            innerNoPropertyPanel.add(noPropertyLabel);
            innerNoPropertyPanel.add(Box.createGlue());
            noPropertyPanel.add(Box.createVerticalStrut(20));
            noPropertyPanel.add(innerNoPropertyPanel);
            noPropertyPanel.add(Box.createGlue());

        }

        mainPanel.add(noPropertyPanel);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.validate();
        repaint();
    }

    /* Fills the PropertyEditorPanel with the properties of the Object of the given node. */
    protected void showProperties(ObjectHierarchyTreeNode node) {
        /* If the dialog was emptied, then remove the 'noPropertyPanel' from the 'propertyEditorPanel'
         * before proceeding.
         */
        if (beanEditor.emptyDialog) {
            removeAll();
            mainPanel.removeAll();
            revalidate();
            beanEditor.emptyDialog = false;
            add(scrollPane, BorderLayout.CENTER);
            add(msgPanel, BorderLayout.SOUTH);
        }

        int propertyCount = 0;
        if (node.usablePropertyDescriptors != null)
            propertyCount = node.usablePropertyDescriptors.length;
        Array previousPropertyPanels = propertyPanels;
        propertyPanels = new Array();
//        Array propertyLabels = new Array();
        Dimension editorComponentDim = new Dimension(COMPONENT_AREA_WIDTH, PropertyPanel.PROPERTY_PANEL_HEIGHT);

        /* Re-use the existing property panels, if any exist.
         */
        boolean reuse = false;
        if (previousPropertyPanels != null)
            reuse = true;

        int i=0, displayedPropertyCount = 0;
        for (i=0; i<propertyCount; i++) {
//-
            displayedPropertyCount++;
            if (reuse && 0<previousPropertyPanels.size()) {
                PropertyPanel propertyPanel = (PropertyPanel) previousPropertyPanels.at(0);
                previousPropertyPanels.remove(0);
//System.out.print(propertyPanel.getPropertyName() + " --> ");
                propertyPanel.setPropertyName(node.usablePropertyDescriptors[i].getDisplayName());
//System.out.println(propertyPanel.getPropertyName() + ", ");
                propertyPanel.setEditorComponent(node.propertyEditorComponents[i]);
                propertyPanel.setEnabled(!node.readOnlyProperty[i]);
                propertyPanel.setToolTipText(node.usablePropertyDescriptors[i].getShortDescription());
                propertyPanels.add(propertyPanel);
            }else{
                if (reuse) reuse = !reuse; // Buy some more time from comparisons

//System.out.print(node.usablePropertyDescriptors[i].getDisplayName() + ", ");

                PropertyPanel propPanel = new PropertyPanel(mma2);
                propPanel.setPropertyName(node.usablePropertyDescriptors[i].getDisplayName());
//                if (beanEditor.localeIsGreek)
//                    propPanel.propertyLabel.setFont(beanEditor.greekUIFont); //greekUIBoldFont);
//                else{
//                    Font currFont = propPanel.propertyLabel.getFont();
//                    propPanel.propertyLabel.setFont(new Font(currFont.getFamily(), Font.PLAIN, currFont.getSize())); //Font.BOLD, currFont.getSize()));
//                }

                propPanel.setEditorComponent(node.propertyEditorComponents[i]);
                propPanel.setEnabled(!node.readOnlyProperty[i]);
                propPanel.setToolTipText(node.usablePropertyDescriptors[i].getShortDescription());
                propertyPanels.add(propPanel);
/*                LayoutConstraint c = new LayoutConstraint();
                c.anchorToContainerLeft(1);
                c.anchorToContainerRight(1);
                if (propertyPanels.size() == 1)
                    c.anchorToContainerTop(1);
                else{
                    System.out.println("i: " + i);
                    c.anchorToBottomOf((Component) propertyPanels.at(propertyPanels.size()-1), 1);
                }
*/
                mainPanel.add(propPanel); //(JPanel) propertyPanels.at(i));
            }
        }
        /* If all the previous panels were reused, then it is most probable that the property panels
         * for the new component are less that the panels for the previous component (i.e. the
         * panels in the 'previousPropertyPanels' Array). This means that we have to remove the
         * exceeding (not re-used) panels of the 'previousPropertyPanels' Array from the 'properiesPanel'.
         */
        i = displayedPropertyCount;
        if (reuse) {
            for (int k=0;k<previousPropertyPanels.size(); k++) {
                PropertyPanel propPanel = (PropertyPanel) previousPropertyPanels.at(k);
                mainPanel.remove(propPanel);
            }
        }
        if (previousPropertyPanels != null)
            previousPropertyPanels.clear();

        if (propertyPanels.size() != 0) {
            // Find the label with the maximum width
            FontMetrics fm = ((PropertyPanel) propertyPanels.at(0)).propertyLabel.getFontMetrics(((PropertyPanel) propertyPanels.at(0)).propertyLabel.getFont());
            labelMaxWidth = fm.stringWidth(((PropertyPanel) propertyPanels.at(0)).propertyLabel.getText());
            for (i=1; i<displayedPropertyCount; i++) {
                int width = fm.stringWidth(((PropertyPanel) propertyPanels.at(i)).propertyLabel.getText());
                if (width > labelMaxWidth)
                    labelMaxWidth = width;
            }

            labelMaxWidth = labelMaxWidth + 10;

            Dimension labelDimension = new Dimension(labelMaxWidth, PropertyPanel.PROPERTY_PANEL_HEIGHT);
            for (i=0; i<displayedPropertyCount; i++) {
                ((PropertyPanel) propertyPanels.at(i)).propertyLabel.setMaximumSize(labelDimension);
                ((PropertyPanel) propertyPanels.at(i)).propertyLabel.setMinimumSize(labelDimension);
                ((PropertyPanel) propertyPanels.at(i)).propertyLabel.setPreferredSize(labelDimension);
            }
        }
    }

    void adjustSizes() {
        Dimension newDim;
//                System.out.println("Scrollbar visible? " + scrollPane.getVerticalScrollBar().isVisible());
        if (scrollPane.getVerticalScrollBar().isVisible())
            newDim = new Dimension(getSize().width-24, PropertyPanel.PROPERTY_PANEL_HEIGHT);
        else
            newDim = new Dimension(getSize().width-14, PropertyPanel.PROPERTY_PANEL_HEIGHT);

        if (propertyPanels != null) {
//            if (newDim.width > 300) {
                for (int i=0; i<propertyPanels.size(); i++) {
                    ((PropertyPanel) propertyPanels.at(i)).preferredSize.width = newDim.width;
                    ((PropertyPanel) propertyPanels.at(i)).validateHeight();
                    ((PropertyPanel) propertyPanels.at(i)).invalidate();
                }
/*            }else{
                Dimension minDim = new Dimension(300, newDim.height);
                for (int i=0; i<propertyPanels.size(); i++) {
                    ((PropertyPanel) propertyPanels.at(i)).preferredSize.width = newDim.width;
                    ((PropertyPanel) propertyPanels.at(i)).validateHeight();
                    ((PropertyPanel) propertyPanels.at(i)).revalidate();
                }
            }
*/
        }

//        Dimension newScrollPaneDim = new Dimension(getSize().width-14, getSize().height); //-100);
//        scrollPane.setPreferredSize(newScrollPaneDim);
        beanEditor.mainPanel.revalidate();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    public void disposed() {
        mainPanel.removeMouseMotionListener(mma1);
        if (noPropertyPanel != null)
            noPropertyPanel.removeAll();
        noPropertyPanel = null;
        if (propertyPanels != null)
            propertyPanels.clear();
        propertyPanels = null;
        scrollPane.removeAll();
    }

    class PropertyPanel extends JPanel {
        public final static int PROPERTY_PANEL_HEIGHT = 26;
        Component editorComponent = null;
        JLabel propertyLabel = null;
        Dimension minimumSize = new Dimension(30, 10);
        Dimension maximumSize = new Dimension();
        String helpMessage = null;
        java.awt.event.MouseMotionListener mml = null;
        boolean highlighted=false;

        public PropertyPanel(java.awt.event.MouseMotionListener mma) {
            super(true);
            mml = mma;
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(3,3,0,3));
            setAlignmentX(RIGHT_ALIGNMENT);
        }

        Dimension preferredSize = new Dimension();
        public Dimension getPreferredSize() {
            return preferredSize;
        }
        public Dimension getMinimumSize() {
            return minimumSize;
        }
        public Dimension getMaximumSize() {
            maximumSize = super.getMaximumSize();
            maximumSize.height = preferredSize.height;
            return maximumSize;
        }
        public void validate() {
            super.validate();
            validateHeight();
        }
        public void validateHeight() {
            if (getComponentCount() > 1) {
                preferredSize.height = getComponent(1).getPreferredSize().height+3; // 3 is the top empty border size
            }else
                preferredSize.height = PROPERTY_PANEL_HEIGHT;
//            System.out.println("preferredSize.height: " + preferredSize.height);
        }

        public void setPropertyName(String propertyName) {
            if (propertyLabel == null) {
                propertyLabel = new JLabel(propertyName);
                propertyLabel.setForeground(BeanInfoDialog.PROPERTY_LABEL_COLOR);
                add(propertyLabel, BorderLayout.WEST);
                propertyLabel.addMouseMotionListener(mml);
            }else
                propertyLabel.setText(propertyName);
        }

        public String getPropertyName() {
            if (propertyLabel == null)
                return null;
            return propertyLabel.getText();
        }

        public void setEditorComponent(Component comp) {
//            System.out.println("setEditorComponent: " + comp);
            if (editorComponent != null) {
                unregMouseMotionListener(editorComponent);
                remove(editorComponent);
            }
            editorComponent = comp;
            add(editorComponent, BorderLayout.CENTER);
            regMouseMotionListener(editorComponent);
        }

        /* Register the mouse motion listener to all the components which exist
         * inside the given component.
         */
        private void regMouseMotionListener(Component component) {
            component.addMouseMotionListener(mml);
            if (component instanceof Container) {
                Component[] comps = ((Container) component).getComponents();
                for (int i=0; i<comps.length; i++)
                    regMouseMotionListener(comps[i]);
            }
        }

        /* Unregister the mouse motion listener from all the components which exist
         * inside the given component.
         */
        private void unregMouseMotionListener(Component component) {
            component.removeMouseMotionListener(mml);
            if (component instanceof Container) {
                Component[] comps = ((Container) component).getComponents();
                for (int i=0; i<comps.length; i++)
                    unregMouseMotionListener(comps[i]);
            }
        }

        public void setEnabled(boolean enabled) {
            if (editorComponent != null)
                editorComponent.setEnabled(enabled);
            if (propertyLabel != null)
                propertyLabel.setEnabled(enabled);
        }

        public void setToolTipText(String tip) {
            helpMessage = tip;
/*            propertyLabel.setToolTipText(tip);
            if (JComponent.class.isAssignableFrom(editorComponent.getClass()))
                ((JComponent) editorComponent).setToolTipText(tip);
*/
        }

        public void setHighlighted(boolean highlight) {
            if (highlight) {
                propertyLabel.setForeground(UIManager.getColor("textHighlightText"));
                propertyLabel.setBackground(UIManager.getColor("controlShadow"));
                propertyLabel.setOpaque(true);
            } else {
                propertyLabel.setForeground(BeanInfoDialog.PROPERTY_LABEL_COLOR);
                propertyLabel.setOpaque(false);
            }
            propertyLabel.repaint();
            highlighted=highlight;
        }

        public boolean isHighlighted() {
            return highlighted;
        }
    }

    /* The Panel at the bottom of the PropertyEditorPanel, which displays the long description
     * message for each property, as the cursor moves above each property's panel.
     */
    class MsgPanel extends JPanel {
        JLabel msgLabel;

        public MsgPanel() {
            msgLabel = new JLabel(" ");

            setBackground(new Color(233, 233, 233)); //Color.white);
            setLayout(new BorderLayout());
            add(msgLabel, BorderLayout.CENTER);
            setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));
        }

        public void setMessage(String msg) {
            if (msg == null || msg.trim().length() == 0)
                msgLabel.setText(" ");
            else
                msgLabel.setText(msg);
        }

        public Dimension getMaximumSize() {
            Dimension maxSize = super.getMaximumSize();
            maxSize.height = 19;
            return maxSize;
        }

        public Dimension getPreferredSize() {
            Dimension prefSize = super.getPreferredSize();
            prefSize.height = 19;
            return prefSize;
        }
    }
}

class ComponentTypeCellRenderer extends javax.swing.DefaultListCellRenderer { //BasicListCellRenderer  {
    final static Image NO_IMAGE = Toolkit.getDefaultToolkit().createImage(ComponentTypeCellRenderer.class.getResource("images/16x16EmptyImage.gif"));
    JComboBox combobox;
//    InstalledComponentStructure installedComponents;
    CompoEntry[] menuEntries = null;
    ESlateComposer container;
    final static Border emptyBorder = new EmptyBorder(0, 2, 0, 0);
    int iconsNotFound = 0;
    boolean iconsRetrieved = false;
    BooleanWrapper bWrapper = new BooleanWrapper(false);
    JWindow waitDialog = null;

    public ComponentTypeCellRenderer(JComboBox box, ESlateComposer container, CompoEntry[] entries) {
        this.container = container;
        this.combobox = box;
        menuEntries = entries;
    }

    public Component getListCellRendererComponent(
          JList listbox,
          Object value,
          int index,
          boolean isSelected,
          boolean cellHasFocus)
    {
        super.getListCellRendererComponent(listbox, value, index, isSelected, cellHasFocus);
        setBorder(emptyBorder);
//        System.out.println("ComponentTypeCellRenderer value: " + value);
//        System.out.println("index: " + index);
        if (index < 0 || index > menuEntries.length) return this;
        String className = menuEntries[index].className; //installedComponents.getClassName((String) value);
        if (className != null) {
            try{
                Class cls = Class.forName(className);
                java.awt.Image img = BeanInfoFactory.get16x16BeanIcon(cls, bWrapper);
                // Check if the requested icon was already registered in the BeanInfoFactory
                // or the BeanInfoFactory had to find and register it.
                if (!iconsRetrieved && !bWrapper.getValue())
                    iconsNotFound++;

//                System.out.println("cls: " + cls.getName() + ", img: " + img);
                if (img != null) {
                    setImage(img);
                    int imgWidth = img.getWidth(this);
                    setIconTextGap(10 + (16-imgWidth));
                }else{
                    setImage(null);
                    setIconTextGap(10);
                }
                /* If more than 3 bean icons were found not registred in the BeanInfoFactory,
                 * then probably all no icon has been registred in the BeanInfoFactory yet.
                 * In order for the JComboBox's popup to be displayed all the icons of the
                 * components that are registered to E-Slate will have to be acquired. This
                 * takes time when the BeanInfoFactory is empty. In this case display a
                 * wait message while instructing the BeanInfoFactory to load the icons of
                 * all the registered components.
                 */
                if (iconsNotFound > 3) {
                    /* The wait dialog is constructed in a thread */
                    Thread thr =  new Thread() {
                        public void run() {
                            waitDialog = new JWindow();
                            JLabel waitLb = new JLabel(container.getContainerBundle().getString("Loading icons"));
                            Font f = waitLb.getFont();
                            waitLb.setFont(f.deriveFont(Font.BOLD));
//                            waitLb.setForeground(new Color(0, 0, 128));
                            waitLb.setAlignmentX(0.5f);
                            FontMetrics fm = waitLb.getFontMetrics(waitLb.getFont());
                            int width = fm.stringWidth(waitLb.getText());
                            int height = fm.getHeight();

                            JPanel mainPanel = new JPanel();
                            mainPanel.setBorder(new javax.swing.border.CompoundBorder(
                                    new gr.cti.eslate.utils.OneLineBevelBorder(gr.cti.eslate.utils.OneLineBevelBorder.RAISED),
                                    new javax.swing.border.EmptyBorder(0, 2, 2, 2)));
                            mainPanel.add(waitLb, BorderLayout.CENTER);
//                            mainPanel.setOpaque(false);
                            Dimension dim = new Dimension(width+20, height+11);
                            mainPanel.setMaximumSize(dim);
                            mainPanel.setPreferredSize(dim);
                            mainPanel.setMinimumSize(dim);
                            waitDialog.setContentPane(mainPanel);
                            Point p = combobox.getLocationOnScreen();
                            p.y = p.y + combobox.getHeight();
                            waitDialog.setLocation(p);
                            waitDialog.setSize(dim);
                            /* There is a chance that 'getBeanIcons()' has finished before
                             * the thread dispays the WaitDialog. This may occur if the
                             * number of registered components is small. We can check for
                             * this case with the value of the 'iconsNotFound' variable.
                             * If this is 0, then don't display the dialog.
                             */
                            if (iconsNotFound != 0) {
                                waitDialog.setVisible(true);
//System.out.println("mainPanel.getVisibleRect(): " + mainPanel.getVisibleRect());
                                mainPanel.paintImmediately(mainPanel.getVisibleRect());
//                                waitLb.paintImmediately(waitLb.getVisibleRect());
//                                waitDialog.validate();
//                                waitDialog.doLayout();
                            }else{
                                waitDialog.dispose();
                                waitDialog = null;
                            }
                        }
                     };
                    thr.start();
                    getBeanIcons();
                    iconsRetrieved = true;
                    if (waitDialog != null) {
                        waitDialog.dispose();
                        waitDialog = null;
                    }
                    iconsNotFound = 0;
                }
            }catch (Throwable thr) {
                //thr.printStackTrace();
                setImage(null);
            }
        }
        if(isSelected) {
//            setBackground(selectedCellBackground);
//            setForeground(selectedCellForeground);
            listbox.setToolTipText(menuEntries[index].name);
        } else {
//            setBackground(defaultCellBackground);
//            setForeground(defaultCellForeground);
        }
        setText(menuEntries[index].name);
        return this;
    }

    // Forces all the bean icons to be registered to the BeanInfoFactory
    private void getBeanIcons() {
        for (int i=0; i<menuEntries.length; i++) {
            String className = menuEntries[i].className; //installedComponents.getClassName((String) value);
            if (className != null) {
                try{
                    Class cls = Class.forName(className);
                    BeanInfoFactory.get16x16BeanIcon(cls);
                }catch (Throwable thr) {
                    //thr.printStackTrace();
                }
            }
        }
    }

    public void setIcon(ImageIcon icon) {
        super.setIcon(icon);
    }

    public void setImage(Image image) {
        if (getIcon() == null)
            setIcon(new ImageIcon());
        if (image == null)
            ((ImageIcon) getIcon()).setImage(NO_IMAGE);
        else
            ((ImageIcon) getIcon()).setImage(image);
    }
}
