package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

class MicroworldPropertiesDialog extends JDialog implements PropertyListenRequest {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    ResourceBundle bundle;
    ESlateComposer composer;
    JTree propertiesTree;
    JButton okButton, cancelButton, applyButton, profileButton;
    DefaultTreeModel propertiesTreeModel;
    SettingsPanel settingsPanel = null;
    PropertiesPanel propertiesPanel;
    JScrollPane panelScroller;
    /** The node for the information of the microworld */
    DefaultMutableTreeNode infoNode;
    /** The node for the background properties of the microworld */
    DefaultMutableTreeNode bgrNode;
    /** The settings wgich have to do with the microworld */
    private MicroworldSettings microworldSettings;
    /* The array of the view-based settings of the microworld. The array has as many elements as
     * the views of the microworld plus the always existing current view.
     */
    private ViewBaseSettings[] viewBaseSettings;
    /** The panel which displays the Microworld metadata */
    private MetadataPanel metadataPanel = null;
    /** The panel which displays the properties of the microworld's background */
    private MwdBackgroundPropsPanel bgrPropertiesPanel = null;
    /* Determines if the dialog was OKed or CANCELled */
    private int returnCode = DIALOG_CANCELLED;
    /* Means to attach PropertyChangeListeners and fire property changes for the change of
     * the values of the settings and their enabled state
     */
    MyPropertyChangeSupport propChangeSupport = new MyPropertyChangeSupport(this);
    /** The user-made profiles. These standard profiles are the design-time, the runtime
     *  and the currentProfile.
     */
    MicroworldSettingsProfile[] customProfiles = null;
    /** Adjusts whether the properties of the microworld can be edited through this dialog */
    boolean isLocked = false;


    MicroworldPropertiesDialog(java.awt.Frame parentFrame, ESlateComposer comp) {
        super(parentFrame, true);
        this.composer = comp;
//        parentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MicroworldPropertiesDialogBundle", Locale.getDefault());
        setTitle(bundle.getString("Title"));

        initializeMicroworldAndViewSettings();
        propertiesTreeModel = createPropertiesTreeModel(); //new DefaultTreeModel();
        propertiesTree = new JTree(propertiesTreeModel);
        propertiesTree.setRootVisible(false);
        propertiesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//        propertiesTree.setCellRenderer(new NodeRenderer());

        propertiesPanel = new PropertiesPanel(true);
        panelScroller = new JScrollPane(propertiesPanel);

        settingsPanel = new SettingsPanel(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setLeftComponent(propertiesTree);
        splitPane.setRightComponent(panelScroller);

        JPanel treePanel = new JPanel(true);
        ExplicitLayout el = new ExplicitLayout();
        el.setPreferredLayoutSize(MathEF.constant(650), MathEF.constant(500));
        treePanel.setLayout(el);

        ExplicitConstraints ec1 = new ExplicitConstraints(splitPane);
        ec1.setX(ContainerEF.left(treePanel)); ec1.setY(ContainerEF.top(treePanel));
        ec1.setHeight(ContainerEF.height(treePanel)); ec1.setWidth(ContainerEF.width(treePanel));
        treePanel.add(splitPane, ec1);
        treePanel.setAlignmentX(CENTER_ALIGNMENT);
//        treePanel.setBorder(new javax.swing.border.LineBorder(Color.red));

        // The button panel (OK, CANCEL, APPLY, PROFILES)
        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton = new JButton(bundle.getString("OK"));
        okButton.setForeground(ESlateContainerUtils.color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        okButton.setMargin(zeroInsets);

        cancelButton = new JButton(bundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        applyButton = new JButton(bundle.getString("Apply"));
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

        profileButton = new JButton(bundle.getString("Profiles"));
        profileButton.setForeground(color128);
        profileButton.setMargin(zeroInsets);
        Insets borderInsets = profileButton.getBorder().getBorderInsets(profileButton);
        int buttonWidth = profileButton.getFontMetrics(profileButton.getFont()).stringWidth(profileButton.getText()) +
              borderInsets.left + borderInsets.right + 6;
        Dimension pButtonDim = new Dimension(buttonWidth, buttonSize.height);
        profileButton.setPreferredSize(pButtonDim);
        profileButton.setMinimumSize(pButtonDim);
        profileButton.setMaximumSize(pButtonDim);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(7));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(7));
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createHorizontalStrut(7));
        buttonPanel.add(profileButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(treePanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);

        splitPane.setDividerLocation(200); //0.3d);
        createListeners();
        propertiesTree.setSelectionRow(0);
    }

    private void createListeners() {
        propertiesTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                /* Remove all the PropertyChangeListeners added to the previously selected group */
                TreePath oldPath = e.getOldLeadSelectionPath();
                if (oldPath != null) {
                    DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) oldPath.getLastPathComponent();
                    if (SettingNodeGroup.class.isAssignableFrom(prevNode.getClass())) {
                        SettingNodeGroup oldGroup = (SettingNodeGroup) prevNode;
                        settingsPanel.clear();
//System.out.println("Removing all propertyChangeListeners for group: " + oldGroup.getUserObject());
                        if (oldGroup.propertyListenRequest != null)
                            oldGroup.propertyListenRequest.removeAllPropertyChangeListeners();
                    }
                }

                TreePath path = e.getNewLeadSelectionPath();
//System.out.println("path: " + path);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (SettingNodeGroup.class.isAssignableFrom(node.getClass())) {
                    SettingNodeGroup group = (SettingNodeGroup) node;
//System.out.println("Selected : " + group.getUserObject());
                    settingsPanel.populatePanel(group.getSettingNodes());
                    if (!propertiesPanel.containsSettingsPanel())
                        propertiesPanel.setPanel(settingsPanel);
                    panelScroller.revalidate();
                }else if (node.getUserObject().equals(bundle.getString("MicroworldInfo"))) {
                    if (metadataPanel == null)
                        initializeMetadataPanel();
                    propertiesPanel.setPanel(metadataPanel);
                    panelScroller.revalidate();
                }else if (node.getUserObject().equals(bundle.getString("MicroworldBackground"))) {
                    if (bgrPropertiesPanel == null)
                        initializeBgrPropertiesPanel();
                    propertiesPanel.setPanel(bgrPropertiesPanel);
                    bgrPropertiesPanel.setLocked(!composer.currentView.mwdBgrdChangeAllowed);
                    panelScroller.revalidate();
                }else{
                    propertiesPanel.clear();
                    panelScroller.revalidate();
                }
                propertiesPanel.repaint();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                applyMicroworldProperties();
                dispose();
                composer.containerUtils.discardDialogButtonListeners(MicroworldPropertiesDialog.this);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_CANCELLED;
                dispose();
                composer.containerUtils.discardDialogButtonListeners(MicroworldPropertiesDialog.this);
            }
        });
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyMicroworldProperties();
            }
        });
        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProfileDialog dialog = new ProfileDialog(composer, MicroworldPropertiesDialog.this);
                /* The first time the ProfileDialog appears, any pre-registered (in some other
                 * E-Slate session) custom profiles have not been read yet. So read them here
                 * and populate the 'customProfiles' array, before it is given to the
                 * ProfileDialog.
                 */
                if (customProfiles == null) {
//System.out.println("Loading custom profiles");
                    String[] customProfileFileNames = composer.readMicroworldSettingsProfiles();
//System.out.println("customProfileFileNames: " + customProfileFileNames);
                    loadCustomProfiles(customProfileFileNames);
                }
                dialog.setNonStandardProfiles(customProfiles);
                ESlateContainerUtils.showDialog(dialog, MicroworldPropertiesDialog.this, true);

                if (dialog.getReturnCode() != dialog.DIALOG_OK)
                    return;
                MicroworldSettingsProfile prof = dialog.getSelectedProfile();
                if (prof != null)
                    setProfile(prof);
                MicroworldSettingsProfile[] nonStandardProfiles = dialog.getNonStandardProfiles();
                customProfiles = nonStandardProfiles;
            }
        });

        // The following listeners do not get executed as long as the tree has the focus
        // ESCAPE HANDLER
        composer.containerUtils.attachDialogButtonListener(this, cancelButton, java.awt.event.KeyEvent.VK_ESCAPE);
        // ENTER HANDLER
        composer.containerUtils.attachDialogButtonListener(this, okButton, java.awt.event.KeyEvent.VK_ENTER);
    }

    private void initializeMicroworldAndViewSettings() {
        microworldSettings = new MicroworldSettings(composer.microworld);

        viewBaseSettings = new ViewBaseSettings[composer.mwdViews.size()+1];
        viewBaseSettings[0] = new ViewBaseSettings(composer.currentView, composer);
        for (int i=1; i<viewBaseSettings.length; i++)
            viewBaseSettings[i] = new ViewBaseSettings(composer.mwdViews.viewList[i-1], composer);
    }

    private DefaultTreeModel createPropertiesTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Properties");
        DefaultTreeModel model = new DefaultTreeModel(root);

        /* Populate the tree */
        infoNode = new DefaultMutableTreeNode(bundle.getString("MicroworldInfo"), false);
        root.add(infoNode);

        bgrNode = new DefaultMutableTreeNode(bundle.getString("MicroworldBackground"), false);
        root.add(bgrNode);

        SettingNodeGroup group1 = new SettingNodeGroup(null, "Settings", this);
        root.add(group1);
        SettingNodeGroup mwdSettingsGrp = new SettingNodeGroup(microworldSettings, "Microworld Settings", this);
        group1.add(mwdSettingsGrp);

        SettingNodeGroup uiSettingsGrp = new SettingNodeGroup(microworldSettings, "UI Settings", this);
        SettingNodeGroup mwdViewSettingsGrp = new SettingNodeGroup(microworldSettings, "View Settings", this);
        SettingNodeGroup mwdStorageSettingsGrp = new SettingNodeGroup(microworldSettings, "Storage Settings", this);
        mwdSettingsGrp.add(uiSettingsGrp);
        mwdSettingsGrp.add(mwdViewSettingsGrp);
        mwdSettingsGrp.add(mwdStorageSettingsGrp);

        SettingNodeGroup popupMenuSettingsGrp = new SettingNodeGroup(microworldSettings, "Popup-Menus settings", this);
        SettingNodeGroup componentMgrSettingsGrp = new SettingNodeGroup(microworldSettings, "Component mgmt settings", this);
        uiSettingsGrp.add(popupMenuSettingsGrp);
        uiSettingsGrp.add(componentMgrSettingsGrp);

        Method getterMethod, setterMethod;
        Field enabledField;
        SettingNode node;
        // Microworld settings
        node = createSettingNode("componentNameChangeAllowed", microworldSettings, boolean.class);
        mwdSettingsGrp.addSettingNode(node);
        node = createSettingNode("microworldNameUserDefined", microworldSettings, boolean.class);
        mwdSettingsGrp.addSettingNode(node);
        node = createSettingNode("componentInstantiationAllowed", microworldSettings, boolean.class);
        mwdSettingsGrp.addSettingNode(node);
        node = createSettingNode("componentRemovalAllowed", microworldSettings, boolean.class);
        mwdSettingsGrp.addSettingNode(node);
        node = createSettingNode("mwdNameChangeAllowed", microworldSettings, boolean.class);
        mwdSettingsGrp.addSettingNode(node);
        node = createSettingNode("performanceMgrActive", microworldSettings, boolean.class);
        mwdSettingsGrp.addSettingNode(node);

        // Microworld - UI settings
        node = createSettingNode("mwdLayerMgmtAllowed", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);
        node = createSettingNode("viewMgmtAllowed", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);
        node = createSettingNode("gridMgmtAllowed", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);
        node = createSettingNode("eslateOptionMgmtAllowed", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);
        node = createSettingNode("mwdLAFChangeAllowed", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);
        node = createSettingNode("eslateComponentBarEnabled", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);
        node = createSettingNode("consolesAllowed", microworldSettings, boolean.class);
        uiSettingsGrp.addSettingNode(node);

        // Microworld - UI - Popup-Menus settings
        node = createSettingNode("mwdPopupEnabled", microworldSettings, boolean.class);
        popupMenuSettingsGrp.addSettingNode(node);
        node = createSettingNode("eslateComponentBarMenuEnabled", microworldSettings, boolean.class);
        popupMenuSettingsGrp.addSettingNode(node);
        node = createSettingNode("componentBarMenuEnabled", microworldSettings, boolean.class);
        popupMenuSettingsGrp.addSettingNode(node);

        // Microworld - UI - Component manager settings
        node = createSettingNode("componentPropertyMgmtAllowed", microworldSettings, boolean.class);
        componentMgrSettingsGrp.addSettingNode(node);
        node = createSettingNode("componentEventMgmtAllowed", microworldSettings, boolean.class);
        componentMgrSettingsGrp.addSettingNode(node);
        node = createSettingNode("componentSoundMgmtAllowed", microworldSettings, boolean.class);
        componentMgrSettingsGrp.addSettingNode(node);

        // Microworld - UI - View settings
        node = createSettingNode("storeSkinOnAPerViewBasis", microworldSettings, boolean.class);
        mwdViewSettingsGrp.addSettingNode(node);
        node = createSettingNode("viewCreationAllowed", microworldSettings, boolean.class);
        mwdViewSettingsGrp.addSettingNode(node);
        node = createSettingNode("viewRemovalAllowed", microworldSettings, boolean.class);
        mwdViewSettingsGrp.addSettingNode(node);
        node = createSettingNode("viewRenameAllowed", microworldSettings, boolean.class);
        mwdViewSettingsGrp.addSettingNode(node);
        node = createSettingNode("viewActivationAllowed", microworldSettings, boolean.class);
        mwdViewSettingsGrp.addSettingNode(node);

        //Microworld - Storage settings
        node = createSettingNode("mwdStorageAllowed", microworldSettings, boolean.class);
        mwdStorageSettingsGrp.addSettingNode(node);
        node = createSettingNode("mwdAutoBackupEnabled", microworldSettings, boolean.class);
        mwdStorageSettingsGrp.addSettingNode(node);

        // Microworld view based settings
        SettingNodeGroup viewBasedSettingsGrp = new SettingNodeGroup(null, "View Based Settings", this);
        group1.add(viewBasedSettingsGrp);

        for (int i=0; i<viewBaseSettings.length; i++) {
            ViewBaseSettings viewSettings = viewBaseSettings[i];

            SettingNodeGroup viewSettingsGrp = new SettingNodeGroup(viewSettings, "View", '\"' + viewSettings.view.viewName + '\"', this);
            viewBasedSettingsGrp.add(viewSettingsGrp);

            SettingNodeGroup microworldViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Microworld View Based Settings", this);
            SettingNodeGroup componentViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Component View Based Settings", this);
            viewSettingsGrp.add(microworldViewBasedSettingsGrp);
            viewSettingsGrp.add(componentViewBasedSettingsGrp);

            SettingNodeGroup microworldUIViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Microworld UI View Based Settings", this);
            SettingNodeGroup microworldPrintViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Microworld Print View Based Settings", this);
            microworldViewBasedSettingsGrp.add(microworldUIViewBasedSettingsGrp);
            microworldViewBasedSettingsGrp.add(microworldPrintViewBasedSettingsGrp);

            SettingNodeGroup microworldUIMenusViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Microworld UI Menus View Based Settings", this);
            SettingNodeGroup microworldUIDesktopViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Microworld UI Desktop View Based Settings", this);
            microworldUIViewBasedSettingsGrp.add(microworldUIMenusViewBasedSettingsGrp);
            microworldUIViewBasedSettingsGrp.add(microworldUIDesktopViewBasedSettingsGrp);

            SettingNodeGroup componentBarViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Component Bar View Based Settings", this);
            componentViewBasedSettingsGrp.add(componentBarViewBasedSettingsGrp);

            SettingNodeGroup componentBarButtonViewBasedSettingsGrp = new SettingNodeGroup(viewSettings, "Component Bar Button View Based Settings", this);
            componentViewBasedSettingsGrp.add(componentBarButtonViewBasedSettingsGrp);

            // View based microworld settings
            node = createSettingNode("plugConnectionChangeAllowed", viewSettings, boolean.class);
            microworldViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("mwdBgrdChangeAllowed", viewSettings, boolean.class);
            microworldUIViewBasedSettingsGrp.addSettingNode(node);

            // View based microworld UI menus settings
            node = createSettingNode("menuSystemHeavyWeight", viewSettings, boolean.class);
            microworldUIMenusViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("menuBarVisible", viewSettings, boolean.class);
            microworldUIMenusViewBasedSettingsGrp.addSettingNode(node);

            // View based microworld UI desktop settings
            node = createSettingNode("mwdAutoExpandable", viewSettings, boolean.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("mwdAutoScrollable", viewSettings, boolean.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("horizontalScrollbarPolicy", viewSettings, int.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("verticalScrollbarPolicy", viewSettings, int.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("desktopDraggable", viewSettings, boolean.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("mwdResizable", viewSettings, boolean.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("componentsMoveBeyondMwdBounds", viewSettings, boolean.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("desktopWidth", viewSettings, int.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("desktopHeight", viewSettings, int.class);
            microworldUIDesktopViewBasedSettingsGrp.addSettingNode(node);

            // View based microworld Print settings
            node = createSettingNode("mwdPageSetupAllowed", viewSettings, boolean.class);
            microworldPrintViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("mwdPrintAllowed", viewSettings, boolean.class);
            microworldPrintViewBasedSettingsGrp.addSettingNode(node);

            // View based component settings
            node = createSettingNode("componentPrintAllowed", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("componentMinimizeAllowed", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("componentMaximizeAllowed", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("resizeAllowed", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("moveAllowed", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("ensureActiveComponentVisible", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("componentActivationMethodChangeAllowed", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("activeComponentHighlighted", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("outlineDragEnabled", viewSettings, boolean.class);
            componentViewBasedSettingsGrp.addSettingNode(node);

            // View based component bar settings
            node = createSettingNode("controlBarTitleActive", viewSettings, boolean.class);
            componentBarViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("controlBarsVisible", viewSettings, boolean.class);
            componentBarViewBasedSettingsGrp.addSettingNode(node);

            // View based component bar button settings
            node = createSettingNode("minimizeButtonVisible", viewSettings, boolean.class);
            componentBarButtonViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("maximizeButtonVisible", viewSettings, boolean.class);
            componentBarButtonViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("closeButtonVisible", viewSettings, boolean.class);
            componentBarButtonViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("helpButtonVisible", viewSettings, boolean.class);
            componentBarButtonViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("plugButtonVisible", viewSettings, boolean.class);
            componentBarButtonViewBasedSettingsGrp.addSettingNode(node);
            node = createSettingNode("infoButtonVisible", viewSettings, boolean.class);
            componentBarButtonViewBasedSettingsGrp.addSettingNode(node);
        }
        return model;
    }

    SettingNode createSettingNode(String property, Object target, Class propertyClass) {
        try{
            String getterPrefix = "is";
            if (!boolean.class.isAssignableFrom(propertyClass))
                getterPrefix = "get";
            String upperPropertyName = property.toUpperCase();
            StringBuffer buff = new StringBuffer(property);
            buff.setCharAt(0, upperPropertyName.charAt(0));
            String capitalizedProperty = buff.toString();
            Method getterMethod = target.getClass().getMethod(getterPrefix+capitalizedProperty, new Class[] {});
            Method setterMethod = target.getClass().getMethod("set"+capitalizedProperty, new Class[] {propertyClass});
            Field enabledField = target.getClass().getField(property+"Enabled");
            SettingNode node = new SettingNode(property, getterMethod, setterMethod, enabledField, this, target);
            return node;
        }catch (Throwable thr) {thr.printStackTrace();}
        return null;
    }

    ResourceBundle getBundle() {
        return bundle;
    }

    public int getReturnCode() {
        return returnCode;
    }

    void applyMicroworldProperties() {
        /* If the microworld is locked, then unlock it temporarily, so that the peroperties
         * can be changed.
         */
        boolean isMwdLocked = composer.microworld.isLocked();
        if (isMwdLocked) composer.microworld.setLocked(false);

        if (metadataPanel != null)
            metadataPanel.applyMicroworldMetadata();

        if (bgrPropertiesPanel != null)
            bgrPropertiesPanel.applyBackgroundProperties();

        composer.microworld.setEslateComponentBarMenuEnabled(microworldSettings.eslateComponentBarMenuEnabled);
        composer.microworld.setComponentBarMenuEnabled(microworldSettings.componentBarMenuEnabled);
        composer.microworld.setMwdPopupEnabled(microworldSettings.mwdPopupEnabled);
        composer.microworld.setMwdLayerMgmtAllowed(microworldSettings.mwdLayerMgmtAllowed);
        composer.microworld.setViewMgmtAllowed(microworldSettings.viewMgmtAllowed);
        composer.microworld.setGridMgmtAllowed(microworldSettings.gridMgmtAllowed);
        composer.microworld.setEslateOptionMgmtAllowed(microworldSettings.eslateOptionMgmtAllowed);
        composer.microworld.setComponentPropertyMgmtAllowed(microworldSettings.componentPropertyMgmtAllowed);
        composer.microworld.setComponentEventMgmtAllowed(microworldSettings.componentEventMgmtAllowed);
        composer.microworld.setComponentSoundMgmtAllowed(microworldSettings.componentSoundMgmtAllowed);
        composer.microworld.setMwdLAFChangeAllowed(microworldSettings.mwdLAFChangeAllowed);
        composer.microworld.setESlateComponentBarEnabled(microworldSettings.eslateComponentBarEnabled);
        composer.microworld.setConsolesAllowed(microworldSettings.consolesAllowed);
        composer.microworld.setComponentNameChangeAllowed(microworldSettings.componentNameChangeAllowed);
        composer.microworld.setMicroworldNameUserDefined(microworldSettings.microworldNameUserDefined);
        composer.microworld.setComponentInstantiationAllowed(microworldSettings.componentInstantiationAllowed);
        composer.microworld.setComponentRemovalAllowed(microworldSettings.componentRemovalAllowed);
        composer.microworld.setMwdNameChangeAllowed(microworldSettings.mwdNameChangeAllowed);
        composer.microworld.setPerformanceMgrActive(microworldSettings.performanceMgrActive);
        composer.microworld.setStoreSkinsPerView(microworldSettings.storeSkinOnAPerViewBasis);
        composer.microworld.setViewCreationAllowed(microworldSettings.viewCreationAllowed);
        composer.microworld.setViewRemovalAllowed(microworldSettings.viewRemovalAllowed);
        composer.microworld.setViewRenameAllowed(microworldSettings.viewRenameAllowed);
        composer.microworld.setViewActivationAllowed(microworldSettings.viewActivationAllowed);
        composer.microworld.setMwdAutoBackupEnabled(microworldSettings.mwdAutoBackupEnabled);

        for (int i=0; i<viewBaseSettings.length; i++)
            viewBaseSettings[i].applyViewSettings();

        composer.microworld.setMwdStorageAllowed(microworldSettings.mwdStorageAllowed);

        if (isMwdLocked)
            composer.microworld.setLocked(true);
    }

    private void loadCustomProfiles(String[] profileFileNames) {
        if (profileFileNames == null) {
            customProfiles = new MicroworldSettingsProfile[0];
            return;
        }
        ArrayList prs = new ArrayList();
        for (int i=0; i<profileFileNames.length; i++) {
            if (profileFileNames == null) continue;
            MicroworldSettingsProfile profile = new MicroworldSettingsProfile();
//            System.out.println("propertyFileName: " + profileFileNames[i]);
            try{
                File f = new File(profileFileNames[i]);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
                profile.load(bis);
                bis.close();
                if (profile.getName().trim().length() == 0)
                    profile.setName(f.getName());
                profile.fileName = f.getAbsolutePath();
                prs.add(profile);
            }catch (IOException exc) {
                System.out.println("Unable to restore microworld settings profile from file: " + profileFileNames[i] + ". Exception: " + exc.getClass());
//                    ESlateOptionPane.showMessageDialog(ProfileDialog.this, bundle.getString("UnableToLoadSettings") + propertyFileName + '\"', bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                    exc.printStackTrace();
            }
        }

        customProfiles = new MicroworldSettingsProfile[prs.size()];
        for (int i=0; i<prs.size(); i++)
            customProfiles[i] = (MicroworldSettingsProfile) prs.get(i);
    }

    private void setProfile(MicroworldSettingsProfile prof) {
        microworldSettings.setEslateComponentBarMenuEnabled(prof.eslateComponentBarMenuEnabled);
        microworldSettings.setComponentBarMenuEnabled(prof.componentBarMenuEnabled);
        microworldSettings.setMwdPopupEnabled(prof.mwdPopupEnabled);
        microworldSettings.setMwdLayerMgmtAllowed(prof.mwdLayerMgmtAllowed);
        microworldSettings.setViewMgmtAllowed(prof.viewMgmtAllowed);
        microworldSettings.setGridMgmtAllowed(prof.gridMgmtAllowed);
        microworldSettings.setEslateOptionMgmtAllowed(prof.eslateOptionMgmtAllowed);
        microworldSettings.setComponentPropertyMgmtAllowed(prof.componentPropertyMgmtAllowed);
        microworldSettings.setComponentEventMgmtAllowed(prof.componentEventMgmtAllowed);
        microworldSettings.setComponentSoundMgmtAllowed(prof.componentSoundMgmtAllowed);
        microworldSettings.setMwdLAFChangeAllowed(prof.mwdLAFChangeAllowed);
        microworldSettings.setEslateComponentBarEnabled(prof.eslateComponentBarEnabled);
        microworldSettings.setConsolesAllowed(prof.consolesAllowed);
        microworldSettings.setComponentNameChangeAllowed(prof.componentNameChangeAllowed);
        microworldSettings.setMicroworldNameUserDefined(prof.microworldNameUserDefined);
        microworldSettings.setMwdStorageAllowed(prof.mwdStorageAllowed);
        microworldSettings.setMwdAutoBackupEnabled(prof.mwdAutoBackupEnabled);
        microworldSettings.setComponentInstantiationAllowed(prof.componentInstantiationAllowed);
        microworldSettings.setComponentRemovalAllowed(prof.componentRemovalAllowed);
        microworldSettings.setMwdNameChangeAllowed(prof.mwdNameChangeAllowed);
        microworldSettings.setPerformanceMgrActive(prof.performanceMgrActive);
        microworldSettings.setStoreSkinOnAPerViewBasis(prof.storeSkinOnAPerViewBasis);
        microworldSettings.setViewCreationAllowed(prof.viewCreationAllowed);
        microworldSettings.setViewRemovalAllowed(prof.viewRemovalAllowed);
        microworldSettings.setViewRenameAllowed(prof.viewRenameAllowed);
        microworldSettings.setViewActivationAllowed(prof.viewActivationAllowed);

        ViewBaseSettings currViewSettings = viewBaseSettings[0];
        currViewSettings.setMinimizeButtonVisible(prof.minimizeButtonVisible);
        currViewSettings.setMaximizeButtonVisible(prof.maximizeButtonVisible);
        currViewSettings.setCloseButtonVisible(prof.closeButtonVisible);
        currViewSettings.setControlBarsVisible(prof.controlBarsVisible);
        currViewSettings.setControlBarTitleActive(prof.controlBarTitleActive);
        currViewSettings.setHelpButtonVisible(prof.helpButtonVisible);
        currViewSettings.setPlugButtonVisible(prof.plugButtonVisible);
        currViewSettings.setInfoButtonVisible(prof.infoButtonVisible);
        currViewSettings.setResizeAllowed(prof.resizeAllowed);
        currViewSettings.setMoveAllowed(prof.moveAllowed);
        currViewSettings.setMwdBgrdChangeAllowed(prof.mwdBgrdChangeAllowed);
        currViewSettings.setPlugConnectionChangeAllowed(prof.plugConnectionChangeAllowed);
        currViewSettings.setMenuBarVisible(prof.menuBarVisible);
        currViewSettings.setOutlineDragEnabled(prof.outlineDragEnabled);
        currViewSettings.setComponentActivationMethodChangeAllowed(prof.componentActivationMethodChangeAllowed);
        currViewSettings.setMwdResizable(prof.mwdResizable);
        currViewSettings.setMwdAutoExpandable(prof.mwdAutoExpandable);
        currViewSettings.setMwdAutoScrollable(prof.mwdAutoScrollable);
        currViewSettings.setComponentsMoveBeyondMwdBounds(prof.componentsMoveBeyondMwdBounds);
        currViewSettings.setDesktopDraggable(prof.desktopDraggable);
        currViewSettings.setMenuSystemHeavyWeight(prof.menuSystemHeavyWeight);
        currViewSettings.setHorizontalScrollbarPolicy(prof.horizontalScrollbarPolicy);
        currViewSettings.setVerticalScrollbarPolicy(prof.verticalScrollbarPolicy);
        currViewSettings.setActiveComponentHighlighted(prof.activeComponentHighlighted);
        currViewSettings.setEnsureActiveComponentVisible(prof.ensureActiveComponentVisible);
        currViewSettings.setMwdPrintAllowed(prof.mwdPrintAllowed);
        currViewSettings.setMwdPageSetupAllowed(prof.mwdPageSetupAllowed);
        currViewSettings.setMwdPrintAllowed(prof.mwdPrintAllowed);
        currViewSettings.setComponentPrintAllowed(prof.componentPrintAllowed);
        currViewSettings.setComponentMinimizeAllowed(prof.componentMinimizeAllowed);
        currViewSettings.setComponentMaximizeAllowed(prof.componentMaximizeAllowed);
//        currViewSettings.setDesktopWidth(prof.desktopWidth);
//        currViewSettings.setDesktopHeight(prof.desktopHeight);
    }

    protected MicroworldSettingsProfile getProfile() {
        return new MicroworldSettingsProfile(composer);
    }

    void setCustomProfiles(MicroworldSettingsProfile[] profiles) {
        customProfiles = profiles;
    }

    MicroworldSettingsProfile[] getCustomProfiles() {
        return customProfiles;
    }

    void setLocked(boolean locked) {
        if (isLocked == locked) return;
        isLocked = locked;
        if (metadataPanel != null)
            metadataPanel.setLocked(locked);
        settingsPanel.isLocked = locked;
    }

    private void initializeMetadataPanel() {
        if (metadataPanel != null) return;
        metadataPanel = new MetadataPanel(this);
    }

    private void initializeBgrPropertiesPanel() {
        if (bgrPropertiesPanel != null) return;
        bgrPropertiesPanel = new MwdBackgroundPropsPanel(this);
    }

    boolean unlockMicroworld() {
        initializeMetadataPanel();
        return metadataPanel.unlockMicroworld();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propChangeSupport.removePropertyChangeListener(l);
    }

    public void removeAllPropertyChangeListeners() {
        propChangeSupport.removeAllPropertyChangeListeners();
    }
}

class SettingNode extends DefaultMutableTreeNode {
    String settingName = null, settingLocalizedName;
    Method setterMethod = null, getterMethod = null;
    Field enabledField = null;
//    Object setting = null;
    MicroworldPropertiesDialog dialog = null;
    /* The object on which the setter and getter methods are called */
    Object target = null;
    Class settingType = null;
    SettingNodeGroup group = null;

    public SettingNode(String settingName, Method getterMethod, Method setterMethod, Field enabledField, MicroworldPropertiesDialog dialog, Object target) {
        super();
        this.settingName = settingName;
        this.getterMethod = getterMethod;
        this.setterMethod = setterMethod;
        this.enabledField = enabledField;
        this.dialog = dialog;
        this.target = target;
        settingLocalizedName = dialog.getBundle().getString(settingName);
        setUserObject(settingLocalizedName);
        setAllowsChildren(false);
        settingType = getterMethod.getReturnType();
    }

    public Object getCurrentValue() {
        try{
            return getterMethod.invoke(target, new Object[]{});
        }catch (Throwable thr) {
            thr.printStackTrace();
            return null;
        }
    }

    public void setCurrentValue(Object value) {
        try{
//System.out.println("SettingNode setCurrentValue()" + setterMethod.getName() + ", " + " value: " + value + ", target:  " + target);
            setterMethod.invoke(target, new Object[]{value});
        }catch (Throwable thr) {
            thr.printStackTrace();
        }
    }

    public void setEnabled(boolean enabled) {
        try{
            enabledField.setBoolean(target, enabled);
        }catch(Throwable thr) {thr.printStackTrace();}
    }

    public boolean isEnabled() {
        try{
            return enabledField.getBoolean(target) && !dialog.isLocked;
        }catch (Throwable thr) {thr.printStackTrace();}
        return false;
    }
}

class SettingNodeGroup extends DefaultMutableTreeNode {
//    String groupName = null;
//    Object setting = null;
    MicroworldPropertiesDialog dialog = null;
    ArrayList settingNodes = new ArrayList();
    PropertyListenRequest propertyListenRequest = null;

    public SettingNodeGroup(PropertyListenRequest propertyListenRequest, String groupName, MicroworldPropertiesDialog dialog) {
        super();
//        this.groupName = groupName;
        this.dialog = dialog;
        this.propertyListenRequest = propertyListenRequest;
        setUserObject(dialog.getBundle().getString(groupName));
        setAllowsChildren(true);
    }

    public SettingNodeGroup(PropertyListenRequest propertyListenRequest, String groupNamePart1, String groupNamePart2, MicroworldPropertiesDialog dialog) {
        super();
//        this.groupName = groupNamePart1;
        this.dialog = dialog;
        this.propertyListenRequest = propertyListenRequest;
        setUserObject(dialog.getBundle().getString(groupNamePart1) + ' ' + groupNamePart2);
        setAllowsChildren(true);
    }

    public void addSettingNode(SettingNode node) {
        settingNodes.add(node);
        node.group = this;
    }

    public ArrayList getSettingNodes() {
        return settingNodes;
    }

    public int getSettingNodeCount() {
        return settingNodes.size();
    }
}


class SettingsPanel extends JPanel {
    public static final Expression V_GAP_TOP = MathEF.constant(10);
    public static final Expression V_GAP = MathEF.constant(2);
    public static final Expression H_GAP = MathEF.constant(10);
    ExplicitLayout layout = new ExplicitLayout();
    Component[] uiElements = new Component[0];
    MicroworldPropertiesDialog dialog = null;
    boolean isLocked = false;

    public SettingsPanel(MicroworldPropertiesDialog dialog) {
        super(true);
        this.dialog = dialog;
        setLayout(layout);
    }

    public void clear() {
        /* Remove all the existing UI elements in the SettingsPanel. There are listeners
         * attached to each such element, which must be removed, or else the elements will not
         * be garbage collected, as these listeners have references to the SettingNode for which
         * each element is created.
         */
        for (int i=0; i<uiElements.length; i++) {
            if (CheckBoxEditor.class.isAssignableFrom(uiElements[i].getClass())) {
                EventListener[] listeners = uiElements[i].getListeners(BoxActionListener.class);
                for (int k=0; k<listeners.length; k++) {
                    BoxActionListener l = (BoxActionListener) listeners[k];
                    ((CheckBoxEditor) uiElements[i]).removeActionListener(l);
                    l.node = null;
                }
            }else if (SpinFieldEditor.class.isAssignableFrom(uiElements[i].getClass())) {
                EventListener[] listeners = uiElements[i].getListeners(SpinValueChangedListener.class);
                for (int k=0; k<listeners.length; k++) {
                    SpinValueChangedListener l = (SpinValueChangedListener) listeners[k];
                    ((SpinFieldEditor) uiElements[i]).removeValueChangedListener(l);
                    l.node = null;
                }
            }else{
                EventListener[] listeners = uiElements[i].getListeners(ComboBoxActionListener.class);
                for (int k=0; k<listeners.length; k++) {
                    ComboBoxActionListener l = (ComboBoxActionListener) listeners[k];
                    ((LabelComboBoxEditor) uiElements[i]).removeActionListener(l);
                    l.node = null;
                }
            }
        }
        removeAll();
    }

    public void populatePanel(ArrayList settingNodes) {
        clear();
        // Create the UI elements for the supplied list of SettingNodes
        Component previousUIElement = null;
        uiElements = new Component[settingNodes.size()];
        String fontName = null, htmlPrefix = null, htmlSuffix = null;
        ArrayList comboEditors = new ArrayList(), spinEditors = new ArrayList();
        for (int i=0; i<settingNodes.size(); i++) {
            SettingNode node = (SettingNode) settingNodes.get(i);
            javax.swing.JComponent settingEditor = null;
            if (boolean.class.isAssignableFrom(node.settingType)) {
                CheckBoxEditor box = new CheckBoxEditor(node.settingLocalizedName);
                settingEditor = box;
                box.setSelected(((Boolean) node.getCurrentValue()).booleanValue());
                box.setEnabled(node.isEnabled());
                box.addActionListener(new BoxActionListener(node));
                node.group.propertyListenRequest.addPropertyChangeListener(new SettingListener(node, box));
            }else if (int.class.isAssignableFrom(node.settingType)) {
                if (node.settingName.toLowerCase().indexOf("policy") == -1) {
                    /* For all the settings of int type, except the verical and horizontal scrollbar
                     * policy settings
                     */
                    Integer value = (Integer) node.getCurrentValue();
                    SpinFieldEditor spin = new SpinFieldEditor(true, node.settingLocalizedName, value.intValue());
                    spin.setMinValue(-1);
                    spin.setEnabled(node.isEnabled());
                    settingEditor = spin;
                    spinEditors.add(spin);
                    spin.addValueChangedListener(new SpinValueChangedListener(node));
                    node.group.propertyListenRequest.addPropertyChangeListener(new SettingListener(node, spin));
                }else{
                    /*  For the visibility policy settings of the vertical and horizontal scrollbar
                     *  of the E-Slate, create LabelComboBox editors.
                     */
                    Integer value = (Integer) node.getCurrentValue();
                    LabelComboBoxEditor box = new LabelComboBoxEditor(node.settingName, node.settingLocalizedName, value.intValue(), dialog);
                    box.setEnabled(node.isEnabled());
                    settingEditor = box;
                    comboEditors.add(box);

                    box.addActionListener(new ComboBoxActionListener(node));
                    node.group.propertyListenRequest.addPropertyChangeListener(new SettingListener(node, box));
                }
            }

            /* Set the tooltip text for the new setting editor */
            if (fontName == null) {
                fontName = settingEditor.getFont().getFontName();
                htmlPrefix = "<html><font face=\"" + fontName + "\" size=\"2\">";
                htmlSuffix = "</font></html>";
            }
            settingEditor.setToolTipText(htmlPrefix + dialog.bundle.getString(node.settingName+"Tip") + htmlSuffix);

            /* Align the new setting editor according to the previous setting editor. The first setting
             * editor is alligned according to the SettingsPane(the container) below(outside the for loop).
             */
            ExplicitConstraints ec = new ExplicitConstraints(settingEditor);
            if (previousUIElement == null) {
//                    ec.setY(ContainerEF.top().add(V_GAP_TOP));
//                    ec.setX(ContainerEF.left().add(H_GAP));
            }else{
                ec.setY(ComponentEF.bottom(previousUIElement).add(V_GAP));
                ec.setX(ComponentEF.left(previousUIElement));
            }
            add(settingEditor, ec);
            uiElements[i] = settingEditor;
            previousUIElement = settingEditor;
        }
        allignComboEditors(comboEditors);
        allignSpinEditors(spinEditors);
        comboEditors.clear();
        spinEditors.clear();

//        Expression maxElementWidth = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), uiElements));
        Expression maxElementWidth = GroupEF.preferredWidthMax(uiElements);
        maxElementWidth = maxElementWidth.add(H_GAP).add(H_GAP);
//        Expression elementTotalHeight = MathEF.sum(GroupEF.createExpressions(ComponentEF.preferredHeight(null), uiElements));
        Expression elementTotalHeight = GroupEF.preferredHeightSum(uiElements);
        elementTotalHeight.add(V_GAP_TOP);
        elementTotalHeight.add(V_GAP.multiply(settingNodes.size()));
        layout.setPreferredLayoutSize(maxElementWidth, elementTotalHeight);

        /* Align the first setting editor according to the SettingsPanel */
        if (uiElements.length > 0) {
            ExplicitConstraints ec = layout.getConstraints(uiElements[0]);
            Container parent = uiElements[0].getParent();
            ec.setX(ContainerEF.width(parent).subtract(maxElementWidth).divide(2d).add(H_GAP));
            ec.setY(ContainerEF.height(parent).subtract(elementTotalHeight).divide(4d));
        }

        revalidate();
        repaint();
    }

    private void allignComboEditors(ArrayList editors) {
        if (editors.size() == 0) return;
        JLabel[] labels = new JLabel[editors.size()];
        int maxWidth = -1;
        FontMetrics fm = null;

        for (int i=0; i<editors.size(); i++) {
            labels[i] = ((LabelComboBoxEditor) editors.get(i)).l;
            if (fm == null)
                fm = labels[i].getFontMetrics(labels[i].getFont());
            int w = fm.stringWidth(labels[i].getText());
            if (w > maxWidth) maxWidth = w;
        }
        Dimension size = labels[0].getPreferredSize();
        size.width = maxWidth;
        for (int i=0; i<labels.length; i++) {
            labels[i].setMinimumSize(size);
            labels[i].setPreferredSize(size);
            labels[i].setMaximumSize(size);
        }
    }

    private void allignSpinEditors(ArrayList editors) {
        SpinField[] spins = new SpinField[editors.size()];
        for (int i=0; i<spins.length; i++)
            spins[i] = (SpinField) editors.get(i);
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 65, 20);
    }

    class SettingListener implements PropertyChangeListener {
        SettingNode node = null;
        SettingEditor editor = null;

        public SettingListener(SettingNode node, SettingEditor editor) {
            this.node = node;
            this.editor = editor;
        }

        public void propertyChange(PropertyChangeEvent evt) {
//System.out.println("Changed property: " + evt.getPropertyName() + ", settingName: " + node.settingName);
            if (evt.getPropertyName().equals(node.settingName))
                editor.setValue(evt.getNewValue());
            else if (evt.getPropertyName().equals(node.settingName+"Enabled")) {
                boolean enabled = ((Boolean) evt.getNewValue()).booleanValue();
                editor.setEnabled(enabled);
                if (node.isEnabled() != enabled)
                    node.setEnabled(enabled);
            }
        }
    }

    /* Special ActionListener which contains a reference to a SettingNode, whose
     * 'setCurrentValue()' is called every time the element's selection changes. This listener
     * is used for JCheckBoxes.
     */
    class BoxActionListener implements ActionListener {
        SettingNode node = null;
        public BoxActionListener(SettingNode node) {
            this.node = node;
        }
        public void actionPerformed(ActionEvent e) {
            boolean selected = ((JCheckBox) e.getSource()).isSelected();
            node.setCurrentValue(new Boolean(selected));
        }
    }

    /* Special ActionListener which contains a reference to a SettingNode, whose
     * 'setCurrentValue()' is called every time the element's selection changes. This listener
     * is used for LabelTextFields.
     */
/*    class FieldActionListener implements ActionListener {
        SettingNode node = null;
        public FieldActionListener(SettingNode node) {
            this.node = node;
        }
        public void actionPerformed(ActionEvent e) {
            String val = ((JTextField) e.getSource()).getText();
            try{
                Integer value = new Integer(val);
                node.setCurrentValue(value);
            }catch (Throwable thr) {}
        }
    }
*/
    /* Special ValueChangedListener which contains a reference to a SettingNode, whose
     * 'setCurrentValue()' is called every time the element's selection changes. This listener
     * is used for SpinFields.
     */
    class SpinValueChangedListener implements ValueChangedListener {
        SettingNode node = null;
        public SpinValueChangedListener(SettingNode node) {
            this.node = node;
        }
        public void valueChanged(ValueChangedEvent e) {
            int val = ((Number) e.getValue()).intValue();
//            String val = ((JTextField) e.getSource()).getText();
            try{
                Integer value = new Integer(val);
                node.setCurrentValue(value);
            }catch (Throwable thr) {}
        }
    }

    /* Special ActionListener which contains a reference to a SettingNode, whose
     * 'setCurrentValue()' is called every time the element's selection changes. This listener
     * is used for LabelComboBoxes.
     */
    class ComboBoxActionListener implements ActionListener {
        SettingNode node = null;
        public ComboBoxActionListener(SettingNode node) {
            this.node = node;
        }
        public void actionPerformed(ActionEvent e) {
            String val = (String) ((JComboBox) e.getSource()).getSelectedItem();
            Integer newValue;
            if (node.settingName.toLowerCase().indexOf("horizontal") != -1) {
                if (val.equals(dialog.getBundle().getString("ScrollBarAsNeeded")))
                    newValue = new Integer(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                else if (val.equals(dialog.getBundle().getString("ScrollBarNever")))
                    newValue = new Integer(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                else
                    newValue = new Integer(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            }else{
                if (val.equals(dialog.getBundle().getString("ScrollBarAsNeeded")))
                    newValue = new Integer(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                else if (val.equals(dialog.getBundle().getString("ScrollBarNever")))
                    newValue = new Integer(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                else
                    newValue = new Integer(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            }
            node.setCurrentValue(newValue);
        }
    }

    class CheckBoxEditor extends JCheckBox implements SettingEditor {
        public CheckBoxEditor(String text) {
            super(text);
        }

        public void setValue(Object value) {
            setSelected(((Boolean) value).booleanValue());
        }
    }

    class SpinFieldEditor extends SpinField implements SettingEditor {
        public SpinFieldEditor(boolean doubleBuffered, String labelText, int value) {
            super(doubleBuffered, labelText);
            spin.setMaximumValue(new Integer(5000));
            spin.setValue(new Integer(value));
        }

        public void setValue(Object value) {
            setValue((Number) value);
        }
    }

/*    class LabelTextFieldEditor extends JPanel implements SettingEditor {
        JLabel l;
        JTextField tf;

        public LabelTextFieldEditor(String settingName, String value) {
            super();
            l = new JLabel(settingName);
            tf = new JTextField();
            tf.setText(value);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(l);
            add(Box.createHorizontalStrut(4));
            add(tf);
            add(Box.createGlue());
        }

        public void addActionListener(ActionListener l) {
            tf.addActionListener(l);
        }

        public void removeActionListener(ActionListener l) {
            tf.removeActionListener(l);
        }

        public Font getFont() {
            if (tf == null) return super.getFont();
            return tf.getFont();
        }

        public void setValue(Object value) {
            tf.setText(value.toString());
        }

        public void setEnabled(boolean enabled) {
            l.setEnabled(enabled);
            tf.setEnabled(enabled);
        }
    }
*/
    class LabelComboBoxEditor extends JPanel implements SettingEditor {
        JLabel l;
        JComboBox cb;
        String settingName;

        public LabelComboBoxEditor(String settingName, String localizedSettingName, int value, MicroworldPropertiesDialog dialog) {
            super();
            this.settingName = settingName;
            l = new JLabel(localizedSettingName);
            String[] scrollBarPolicies = new String[3];
            scrollBarPolicies[0] = dialog.getBundle().getString("ScrollBarAsNeeded");
            scrollBarPolicies[1] = dialog.getBundle().getString("ScrollBarNever");
            scrollBarPolicies[2] = dialog.getBundle().getString("ScrollBarAlways");
            cb = new JComboBox(scrollBarPolicies);
            setComboValue(value);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(l);
            add(Box.createHorizontalStrut(8));
            add(cb);
            add(Box.createGlue());
        }

        public void addActionListener(ActionListener l) {
            cb.addActionListener(l);
        }

        public void removeActionListener(ActionListener l) {
            cb.removeActionListener(l);
        }

        public void setToolTipText(String tip) {
            l.setToolTipText(tip);
            cb.setToolTipText(tip);
        }

        public Font getFont() {
            if (cb == null) return super.getFont();
            return cb.getFont();
        }

        public void setEnabled(boolean enabled) {
            l.setEnabled(enabled);
            cb.setEnabled(enabled);
        }

        private void setComboValue(int value) {
            if (settingName.indexOf("vertical") == -1) {
                if (value == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
                    cb.setSelectedIndex(0);
                else if (value == JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
                    cb.setSelectedIndex(1);
                else
                    cb.setSelectedIndex(2);
            }else{
//System.out.println("Vertical value: " + value + ", VERTICAL_SCROLLBAR_AS_NEEDED: " + JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED + ", VERTICAL_SCROLLBAR_NEVER: " + JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                if (value == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED)
                    cb.setSelectedIndex(0);
                else if (value == JScrollPane.VERTICAL_SCROLLBAR_NEVER)
                    cb.setSelectedIndex(1);
                else
                    cb.setSelectedIndex(2);
            }
        }

        public void setValue(Object value) {
            int val = ((Integer) value).intValue();
            setComboValue(val);
        }
    }

    interface SettingEditor {
        public void setValue(Object value);
        public void setEnabled(boolean enabled);
    }
}


class MicroworldSettings implements PropertyListenRequest {
    /* Settings */
    boolean eslateComponentBarMenuEnabled = true;
    boolean componentBarMenuEnabled = true;
    boolean mwdPopupEnabled = true;
    boolean mwdLayerMgmtAllowed = true;
    boolean viewMgmtAllowed = true;
    boolean gridMgmtAllowed = true;
    boolean eslateOptionMgmtAllowed = true;
    boolean componentPropertyMgmtAllowed = true;
    boolean componentEventMgmtAllowed = true;
    boolean componentSoundMgmtAllowed = true;
    boolean mwdLAFChangeAllowed = true;
    boolean eslateComponentBarEnabled = true;
    boolean consolesAllowed = true;
    boolean componentNameChangeAllowed = true;
    boolean microworldNameUserDefined = true;
    boolean mwdStorageAllowed = true;
    boolean mwdAutoBackupEnabled = true;
    boolean componentInstantiationAllowed = true;
    boolean componentRemovalAllowed = true;
    boolean mwdNameChangeAllowed = true;
    boolean performanceMgrActive = false;
    boolean storeSkinOnAPerViewBasis = false;
    boolean viewCreationAllowed = true;
    boolean viewRemovalAllowed = true;
    boolean viewRenameAllowed = true;
    boolean viewActivationAllowed = true;

    /* Settings enabled state */
    public boolean eslateComponentBarMenuEnabledEnabled = true;
    public boolean componentBarMenuEnabledEnabled = true;
    public boolean mwdPopupEnabledEnabled = true;
    public boolean mwdLayerMgmtAllowedEnabled = true;
    public boolean viewMgmtAllowedEnabled = true;
    public boolean gridMgmtAllowedEnabled = true;
    public boolean eslateOptionMgmtAllowedEnabled = true;
    public boolean componentPropertyMgmtAllowedEnabled = true;
    public boolean componentEventMgmtAllowedEnabled = true;
    public boolean componentSoundMgmtAllowedEnabled = true;
    public boolean mwdLAFChangeAllowedEnabled = true;
    public boolean eslateComponentBarEnabledEnabled = true;
    public boolean consolesAllowedEnabled = true;
    public boolean componentNameChangeAllowedEnabled = true;
    public boolean microworldNameUserDefinedEnabled = true;
    public boolean mwdStorageAllowedEnabled = true;
    public boolean mwdAutoBackupEnabledEnabled = true;
    public boolean componentInstantiationAllowedEnabled = true;
    public boolean componentRemovalAllowedEnabled = true;
    public boolean mwdNameChangeAllowedEnabled = true;
    public boolean performanceMgrActiveEnabled = true;
    public boolean storeSkinOnAPerViewBasisEnabled = true;
    public boolean viewCreationAllowedEnabled = true;
    public boolean viewRemovalAllowedEnabled = true;
    public boolean viewRenameAllowedEnabled = true;
    public boolean viewActivationAllowedEnabled = true;
    /* Means to attach PropertyChangeListeners and fire property changes for the change of
     * the values of the settings and their enabled state
     */
    MyPropertyChangeSupport propChangeSupport = new MyPropertyChangeSupport(this);

    MicroworldSettings(Microworld microworld) {
        eslateComponentBarMenuEnabled =  microworld.eslateComponentBarMenuEnabled;
        componentBarMenuEnabled = microworld.componentBarMenuEnabled;
        mwdPopupEnabled = microworld.mwdPopupEnabled;
        mwdLayerMgmtAllowed = microworld.mwdLayerMgmtAllowed;
        viewMgmtAllowed = microworld.viewMgmtAllowed;
        gridMgmtAllowed = microworld.gridMgmtAllowed;
        eslateOptionMgmtAllowed = microworld.eslateOptionMgmtAllowed;
        componentPropertyMgmtAllowed = microworld.componentPropertyMgmtAllowed;
        componentEventMgmtAllowed = microworld.componentEventMgmtAllowed;
        componentSoundMgmtAllowed = microworld.componentSoundMgmtAllowed;
        mwdLAFChangeAllowed = microworld.mwdLAFChangeAllowed;
        eslateComponentBarEnabled = microworld.eslateComponentBarEnabled;
        consolesAllowed = microworld.consolesAllowed;
        componentNameChangeAllowed = microworld.componentNameChangeAllowed;
        microworldNameUserDefined = microworld.microworldNameUserDefined;
        mwdStorageAllowed = microworld.mwdStorageAllowed;
        mwdAutoBackupEnabled = microworld.mwdAutoBackupEnabled;
        componentInstantiationAllowed = microworld.componentInstantiationAllowed;
        componentRemovalAllowed = microworld.componentRemovalAllowed;
        mwdNameChangeAllowed = microworld.mwdNameChangeAllowed;
        performanceMgrActive = microworld.isPerformanceMgrActive();
        storeSkinOnAPerViewBasis = microworld.storeSkinOnAPerViewBasis;
        viewCreationAllowed = microworld.viewCreationAllowed;
        viewRemovalAllowed = microworld.viewRemovalAllowed;
        viewRenameAllowed = microworld.viewRenameAllowed;
        viewActivationAllowed = microworld.viewActivationAllowed;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propChangeSupport.removePropertyChangeListener(l);
    }

    public void removeAllPropertyChangeListeners() {
        propChangeSupport.removeAllPropertyChangeListeners();
    }

    public boolean isComponentNameChangeAllowed() {
        return componentNameChangeAllowed;
    }
    public void setComponentNameChangeAllowed(boolean allowed) {
        if (componentNameChangeAllowed == allowed) return;
        componentNameChangeAllowed = allowed;
        propChangeSupport.firePropertyChange("componentNameChangeAllowed", !allowed, allowed);
    }

    public boolean isEslateComponentBarMenuEnabled() {
        return eslateComponentBarMenuEnabled;
    }
    public void setEslateComponentBarMenuEnabled(boolean enabled) {
        if (eslateComponentBarMenuEnabled == enabled) return;
        eslateComponentBarMenuEnabled = enabled;
        propChangeSupport.firePropertyChange("eslateComponentBarMenuEnabled", !enabled, enabled);
    }

    public boolean isComponentBarMenuEnabled() {
        return componentBarMenuEnabled;
    }
    public void setComponentBarMenuEnabled(boolean enabled) {
        if (componentBarMenuEnabled == enabled) return;
        componentBarMenuEnabled = enabled;
        propChangeSupport.firePropertyChange("componentBarMenuEnabled", !enabled, enabled);
    }

    public boolean isMwdPopupEnabled() {
        return mwdPopupEnabled;
    }
    public void setMwdPopupEnabled(boolean enabled) {
        if (mwdPopupEnabled == enabled) return;
        mwdPopupEnabled = enabled;
        propChangeSupport.firePropertyChange("mwdPopupEnabled", !enabled, enabled);
    }

    public boolean isMwdLayerMgmtAllowed() {
        return mwdLayerMgmtAllowed;
    }
    public void setMwdLayerMgmtAllowed(boolean allowed) {
        if (mwdLayerMgmtAllowed == allowed) return;
        mwdLayerMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("mwdLayerMgmtAllowed", !allowed, allowed);
    }

    public boolean isViewMgmtAllowed() {
        return viewMgmtAllowed;
    }
    public void setViewMgmtAllowed(boolean allowed) {
        if (viewMgmtAllowed == allowed) return;
        viewMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("viewMgmtAllowed", !allowed, allowed);
    }

    public boolean isGridMgmtAllowed() {
        return gridMgmtAllowed;
    }
    public void setGridMgmtAllowed(boolean allowed) {
        if (gridMgmtAllowed == allowed) return;
        gridMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("gridMgmtAllowed", !allowed, allowed);
    }

    public boolean isEslateOptionMgmtAllowed() {
        return eslateOptionMgmtAllowed;
    }
    public void setEslateOptionMgmtAllowed(boolean allowed) {
        if (eslateOptionMgmtAllowed == allowed) return;
        eslateOptionMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("eslateOptionMgmtAllowed", !allowed, allowed);
    }

    public boolean isComponentPropertyMgmtAllowed() {
        return componentPropertyMgmtAllowed;
    }
    public void setComponentPropertyMgmtAllowed(boolean allowed) {
        if (componentPropertyMgmtAllowed == allowed) return;
        componentPropertyMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("componentPropertyMgmtAllowed", !allowed, allowed);
    }

    public boolean isComponentEventMgmtAllowed() {
        return componentEventMgmtAllowed;
    }
    public void setComponentEventMgmtAllowed(boolean allowed) {
        if (componentEventMgmtAllowed == allowed) return;
        componentEventMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("componentEventMgmtAllowed", !allowed, allowed);
    }

    public boolean isComponentSoundMgmtAllowed() {
        return componentSoundMgmtAllowed;
    }
    public void setComponentSoundMgmtAllowed(boolean allowed) {
        if (componentSoundMgmtAllowed == allowed) return;
        componentSoundMgmtAllowed = allowed;
        propChangeSupport.firePropertyChange("componentSoundMgmtAllowed", !allowed, allowed);
    }

    public boolean isMwdLAFChangeAllowed() {
        return mwdLAFChangeAllowed;
    }
    public void setMwdLAFChangeAllowed(boolean allowed) {
        if (mwdLAFChangeAllowed == allowed) return;
        mwdLAFChangeAllowed = allowed;
        propChangeSupport.firePropertyChange("mwdLAFChangeAllowed", !allowed, allowed);
    }

    public boolean isEslateComponentBarEnabled() {
        return eslateComponentBarEnabled;
    }
    public void setEslateComponentBarEnabled(boolean allowed) {
        if (eslateComponentBarEnabled == allowed) return;
        eslateComponentBarEnabled = allowed;
        propChangeSupport.firePropertyChange("eslateComponentBarEnabled", !allowed, allowed);
    }

    public boolean isConsolesAllowed() {
        return consolesAllowed;
    }
    public void setConsolesAllowed(boolean allowed) {
        if (consolesAllowed == allowed) return;
        consolesAllowed = allowed;
        propChangeSupport.firePropertyChange("consolesAllowed", !allowed, allowed);
    }

    public boolean isMicroworldNameUserDefined() {
        return microworldNameUserDefined;
    }
    public void setMicroworldNameUserDefined(boolean userDefined) {
        if (microworldNameUserDefined == userDefined) return;
        microworldNameUserDefined = userDefined;
        mwdNameChangeAllowedEnabled = microworldNameUserDefined;
        propChangeSupport.firePropertyChange("mwdNameChangeAllowedEnabled", !userDefined, userDefined);
        propChangeSupport.firePropertyChange("microworldNameUserDefined", !userDefined, userDefined);
    }

    public boolean isPerformanceMgrActive() {
        return performanceMgrActive;
    }
    public void setPerformanceMgrActive(boolean active) {
        if (performanceMgrActive == active) return;
        performanceMgrActive = active;
        propChangeSupport.firePropertyChange("performanceMgrActive", !active, active);
    }

    public boolean isMwdStorageAllowed() {
        return mwdStorageAllowed;
    }
    public void setMwdStorageAllowed(boolean allowed) {
        if (mwdStorageAllowed == allowed) return;
        mwdStorageAllowed = allowed;
        propChangeSupport.firePropertyChange("mwdAutoBackupEnabledEnabled", !allowed, allowed);
        propChangeSupport.firePropertyChange("mwdStorageAllowed", !allowed, allowed);
    }

    public boolean isMwdAutoBackupEnabled() {
        return mwdAutoBackupEnabled;
    }
    public void setMwdAutoBackupEnabled(boolean enabled) {
        if (mwdAutoBackupEnabled == enabled) return;
        mwdAutoBackupEnabled = enabled;
        propChangeSupport.firePropertyChange("mwdAutoBackupEnabled", !enabled, enabled);
    }

    public boolean isComponentInstantiationAllowed() {
        return componentInstantiationAllowed;
    }
    public void setComponentInstantiationAllowed(boolean allowed) {
        if (componentInstantiationAllowed == allowed) return;
        componentInstantiationAllowed = allowed;
        propChangeSupport.firePropertyChange("componentInstantiationAllowed", !allowed, allowed);
    }

    public boolean isComponentRemovalAllowed() {
        return componentRemovalAllowed;
    }
    public void setComponentRemovalAllowed(boolean allowed) {
        if (componentRemovalAllowed == allowed) return;
        componentRemovalAllowed = allowed;
        propChangeSupport.firePropertyChange("componentRemovalAllowed", !allowed, allowed);
    }

    public boolean isMwdNameChangeAllowed() {
        return mwdNameChangeAllowed;
    }
    public void setMwdNameChangeAllowed(boolean allowed) {
        if (mwdNameChangeAllowed == allowed) return;
        mwdNameChangeAllowed = allowed;
        propChangeSupport.firePropertyChange("mwdNameChangeAllowed", !allowed, allowed);
    }

    public boolean isStoreSkinOnAPerViewBasis() {
        return storeSkinOnAPerViewBasis;
    }
    public void setStoreSkinOnAPerViewBasis(boolean perView) {
        if (storeSkinOnAPerViewBasis == perView) return;
        storeSkinOnAPerViewBasis = perView;
        propChangeSupport.firePropertyChange("storeSkinOnAPerViewBasis", !perView, perView);
    }

    public boolean isViewCreationAllowed() {
        return viewCreationAllowed;
    }
    public void setViewCreationAllowed(boolean allowed) {
        if (viewCreationAllowed == allowed) return;
        viewCreationAllowed = allowed;
        propChangeSupport.firePropertyChange("viewCreationAllowed", !allowed, allowed);
    }

    public boolean isViewRemovalAllowed() {
        return viewRemovalAllowed;
    }
    public void setViewRemovalAllowed(boolean allowed) {
        if (viewRemovalAllowed == allowed) return;
        viewRemovalAllowed = allowed;
        propChangeSupport.firePropertyChange("viewRemovalAllowed", !allowed, allowed);
    }

    public boolean isViewRenameAllowed() {
        return viewRenameAllowed;
    }
    public void setViewRenameAllowed(boolean allowed) {
        if (viewRenameAllowed == allowed) return;
        viewRenameAllowed = allowed;
        propChangeSupport.firePropertyChange("viewRenameAllowed", !allowed, allowed);
    }

    public boolean isViewActivationAllowed() {
        return viewActivationAllowed;
    }
    public void setViewActivationAllowed(boolean allowed) {
        if (viewActivationAllowed == allowed) return;
        viewActivationAllowed = allowed;
        propChangeSupport.firePropertyChange("viewActivationAllowed", !allowed, allowed);
    }
}


class ViewBaseSettings implements PropertyListenRequest {
    ESlateComposer composer;
    /* View-based settings */
    boolean minimizeButtonVisible = true;
    boolean maximizeButtonVisible = true;
    boolean closeButtonVisible = true;
    boolean controlBarsVisible = true;
    boolean controlBarTitleActive = true;
    boolean helpButtonVisible = true;
    boolean plugButtonVisible = true;
    boolean infoButtonVisible = true;
    boolean resizeAllowed = true;
    boolean moveAllowed = true;
    boolean mwdBgrdChangeAllowed = true;
    boolean plugConnectionChangeAllowed = true;
    boolean menuBarVisible = true;
    boolean outlineDragEnabled = false;
    boolean componentActivationMethodChangeAllowed = true;
    boolean mwdResizable = true;
    boolean mwdAutoExpandable = false;
    boolean mwdAutoScrollable = false;
    boolean componentsMoveBeyondMwdBounds = true;
    boolean desktopDraggable = false;
    boolean menuSystemHeavyWeight = false;
    int horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    int verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
    boolean activeComponentHighlighted = true;
    boolean ensureActiveComponentVisible = true;
    boolean mwdPrintAllowed = true;
    boolean mwdPageSetupAllowed = true;
    boolean componentPrintAllowed = true;
    boolean componentMinimizeAllowed = true;
    boolean componentMaximizeAllowed = true;
    int desktopWidth = -1;
    int desktopHeight = -1;

    /* View-based settings enabled state */
    public boolean minimizeButtonVisibleEnabled = true;
    public boolean maximizeButtonVisibleEnabled = true;
    public boolean closeButtonVisibleEnabled = true;
    public boolean controlBarsVisibleEnabled = true;
    public boolean controlBarTitleActiveEnabled = true;
    public boolean helpButtonVisibleEnabled = true;
    public boolean plugButtonVisibleEnabled = true;
    public boolean infoButtonVisibleEnabled = true;
    public boolean resizeAllowedEnabled = true;
    public boolean moveAllowedEnabled = true;
    public boolean mwdBgrdChangeAllowedEnabled = true;
    public boolean plugConnectionChangeAllowedEnabled = true;
    public boolean menuBarVisibleEnabled = true;
    public boolean outlineDragEnabledEnabled = false;
    public boolean componentActivationMethodChangeAllowedEnabled = true;
    public boolean mwdResizableEnabled = true;
    public boolean mwdAutoExpandableEnabled = true;
    public boolean mwdAutoScrollableEnabled = false;
    public boolean componentsMoveBeyondMwdBoundsEnabled = true;
    public boolean desktopDraggableEnabled = true;
    public boolean menuSystemHeavyWeightEnabled = true;
    public boolean horizontalScrollbarPolicyEnabled = true;
    public boolean verticalScrollbarPolicyEnabled = true;
    public boolean activeComponentHighlightedEnabled = true;
    public boolean ensureActiveComponentVisibleEnabled = true;
    public boolean mwdPrintAllowedEnabled = true;
    public boolean mwdPageSetupAllowedEnabled = true;
    public boolean componentPrintAllowedEnabled = true;
    public boolean componentMinimizeAllowedEnabled = true;
    public boolean componentMaximizeAllowedEnabled = true;
    public boolean desktopWidthEnabled = true;
    public boolean desktopHeightEnabled = true;

    // Flags that track if a setting has been edited
    boolean minimizeButtonVisibleChanged = false;
    boolean maximizeButtonVisibleChanged = false;
    boolean closeButtonVisibleChanged = false;
    boolean controlBarsVisibleChanged = false;
    boolean controlBarTitleActiveChanged = false;
    boolean helpButtonVisibleChanged = false;
    boolean plugButtonVisibleChanged = false;
    boolean infoButtonVisibleChanged = false;
    boolean resizeAllowedChanged = false;
    boolean moveAllowedChanged = false;
    boolean mwdBgrdChangeAllowedChanged = false;
    boolean plugConnectionChangeAllowedChanged = false;
    boolean menuBarVisibleChanged = false;
    boolean outlineDragEnabledChanged = false;
    boolean componentActivationMethodChangeAllowedChanged = false;
    boolean mwdResizableChanged = false;
    boolean mwdAutoExpandableChanged = false;
    boolean mwdAutoScrollableChanged = false;
    boolean componentsMoveBeyondMwdBoundsChanged = false;
    boolean desktopDraggableChanged = false;
    boolean menuSystemHeavyWeightChanged = false;
    boolean horizontalScrollbarPolicyChanged = false;
    boolean verticalScrollbarPolicyChanged = false;
    boolean activeComponentHighlightedChanged = false;
    boolean ensureActiveComponentVisibleChanged = false;
    boolean mwdPrintAllowedChanged = false;
    boolean mwdPageSetupAllowedChanged = false;
    boolean componentPrintAllowedChanged = false;
    boolean componentMinimizeAllowedChanged = false;
    boolean componentMaximizeAllowedChanged = false;
    boolean desktopWidthChanged = false;
    boolean desktopHeightChanged = false;

    /* The view to which these settings apply */
    MicroworldView view = null;
    /* Means to attach PropertyChangeListeners and fire property changes for the change of
     * the values of the settings and their enabled state
     */
    MyPropertyChangeSupport propChangeSupport = new MyPropertyChangeSupport(this);

    ViewBaseSettings(MicroworldView view, ESlateComposer composer) {
        this.view = view;
        this.composer = composer;
        minimizeButtonVisible = view.minimizeButtonVisible;
        maximizeButtonVisible = view.maximizeButtonVisible;
        closeButtonVisible = view.closeButtonVisible;
        controlBarsVisible = view.controlBarsVisible;
        controlBarTitleActive = view.controlBarTitleActive;
        helpButtonVisible = view.helpButtonVisible;
        plugButtonVisible = view.pinButtonVisible;
        infoButtonVisible = view.infoButtonVisible;
        resizeAllowed = view.resizeAllowed;
        moveAllowed = view.moveAllowed;
        mwdBgrdChangeAllowed = view.mwdBgrdChangeAllowed;
        plugConnectionChangeAllowed = view.plugConnectionChangeAllowed;
        menuBarVisible = view.menuBarVisible;
        outlineDragEnabled = view.outlineDragEnabled;
        componentActivationMethodChangeAllowed = view.componentActivationMethodChangeAllowed;
        mwdResizable = view.mwdResizable;
        mwdAutoExpandable = view.mwdAutoExpandable;
        mwdAutoScrollable = view.mwdAutoScrollable;
        componentsMoveBeyondMwdBounds = view.componentsMoveBeyondMwdBounds;
        desktopDraggable = view.desktopDraggable;
        menuSystemHeavyWeight = view.menuSystemHeavyWeight;
        horizontalScrollbarPolicy = view.horizontalScrollbarPolicy;
        verticalScrollbarPolicy = view.verticalScrollbarPolicy;
        activeComponentHighlighted = view.activeComponentHighlighted;
        ensureActiveComponentVisible = view.ensureActiveComponentVisible;
        mwdPrintAllowed = view.mwdPrintAllowed;
        mwdPageSetupAllowed = view.mwdPageSetupAllowed;
        componentPrintAllowed = view.componentPrintAllowed;
        componentMinimizeAllowed = view.componentMinimizeAllowed;
        componentMaximizeAllowed = view.componentMaximizeAllowed;
        // The desktop size is taken directly from the ESlateContainer desktop instead of the view, cause the view
        // does not always get update when the desktop size changes. This happens for example when the desktop changes
        // size as the result of a component moveing across is boundaries
        desktopHeight = composer.lc.getSize().height; //view.desktopHeight
        desktopWidth = composer.lc.getSize().width; ////view.desktopWidth
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propChangeSupport.removePropertyChangeListener(l);
    }

    public void removeAllPropertyChangeListeners() {
        propChangeSupport.removeAllPropertyChangeListeners();
    }

    public boolean isMinimizeButtonVisible() {
        return minimizeButtonVisible;
    }
    public void setMinimizeButtonVisible(boolean visible) {
        if (minimizeButtonVisible == visible) return;
        minimizeButtonVisible = visible;
        minimizeButtonVisibleChanged = true;
        propChangeSupport.firePropertyChange("minimizeButtonVisible", !visible, visible);
    }

    public boolean isMaximizeButtonVisible() {
        return maximizeButtonVisible;
    }
    public void setMaximizeButtonVisible(boolean visible) {
        if (maximizeButtonVisible == visible) return;
        maximizeButtonVisible = visible;
        maximizeButtonVisibleChanged = true;
        propChangeSupport.firePropertyChange("maximizeButtonVisible", !visible, visible);
    }

    public boolean isCloseButtonVisible() {
        return closeButtonVisible;
    }
    public void setCloseButtonVisible(boolean visible) {
        if (closeButtonVisible == visible) return;
        closeButtonVisible = visible;
        closeButtonVisibleChanged = true;
        propChangeSupport.firePropertyChange("closeButtonVisible", !visible, visible);
    }

    public boolean isControlBarsVisible() {
        return controlBarsVisible;
    }
    public void setControlBarsVisible(boolean visible) {
        if (controlBarsVisible == visible) return;
        controlBarsVisible = visible;
        controlBarsVisibleChanged = true;
        propChangeSupport.firePropertyChange("controlBarsVisible", !visible, visible);
        minimizeButtonVisibleEnabled = visible;
        propChangeSupport.firePropertyChange("minimizeButtonVisibleEnabled", !visible, visible);
        maximizeButtonVisibleEnabled = visible;
        propChangeSupport.firePropertyChange("maximizeButtonVisibleEnabled", !visible, visible);
        closeButtonVisibleEnabled = visible;
        propChangeSupport.firePropertyChange("closeButtonVisibleEnabled", !visible, visible);
        helpButtonVisibleEnabled = visible;
        propChangeSupport.firePropertyChange("helpButtonVisibleEnabled", !visible, visible);
        plugButtonVisibleEnabled = visible;
        propChangeSupport.firePropertyChange("plugButtonVisibleEnabled", !visible, visible);
        infoButtonVisibleEnabled = visible;
        propChangeSupport.firePropertyChange("infoButtonVisibleEnabled", !visible, visible);
    }

    public boolean isControlBarTitleActive() {
        return controlBarTitleActive;
    }
    public void setControlBarTitleActive(boolean active) {
        if (controlBarTitleActive == active) return;
        controlBarTitleActive = active;
        controlBarTitleActiveChanged = true;
        propChangeSupport.firePropertyChange("controlBarTitleActive", !active, active);
    }

    public boolean isHelpButtonVisible() {
        return helpButtonVisible;
    }
    public void setHelpButtonVisible(boolean visible) {
        if (helpButtonVisible == visible) return;
        helpButtonVisible = visible;
        helpButtonVisibleChanged = true;
        propChangeSupport.firePropertyChange("helpButtonVisible", !visible, visible);
    }

    public boolean isPlugButtonVisible() {
        return plugButtonVisible;
    }
    public void setPlugButtonVisible(boolean visible) {
        if (plugButtonVisible == visible) return;
        plugButtonVisible = visible;
        plugButtonVisibleChanged = true;
        propChangeSupport.firePropertyChange("plugButtonVisible", !visible, visible);
    }

    public boolean isInfoButtonVisible() {
        return infoButtonVisible;
    }
    public void setInfoButtonVisible(boolean visible) {
        if (infoButtonVisible == visible) return;
        infoButtonVisible = visible;
        infoButtonVisibleChanged = true;
        propChangeSupport.firePropertyChange("infoButtonVisible", !visible, visible);
    }

    public boolean isResizeAllowed() {
        return resizeAllowed;
    }
    public void setResizeAllowed(boolean allowed) {
        if (resizeAllowed == allowed) return;
        resizeAllowed = allowed;
        resizeAllowedChanged = true;
        propChangeSupport.firePropertyChange("resizeAllowed", !allowed, allowed);
    }

    public boolean isMoveAllowed() {
        return moveAllowed;
    }
    public void setMoveAllowed(boolean allowed) {
        if (moveAllowed == allowed) return;
        moveAllowed = allowed;
        moveAllowedChanged = true;
        propChangeSupport.firePropertyChange("moveAllowed", !allowed, allowed);
    }

    public boolean isMwdBgrdChangeAllowed() {
        return mwdBgrdChangeAllowed;
    }
    public void setMwdBgrdChangeAllowed(boolean allowed) {
        if (mwdBgrdChangeAllowed == allowed) return;
        mwdBgrdChangeAllowed = allowed;
        mwdBgrdChangeAllowedChanged = true;
        propChangeSupport.firePropertyChange("mwdBgrdChangeAllowed", !allowed, allowed);
    }

    public boolean isPlugConnectionChangeAllowed() {
        return plugConnectionChangeAllowed;
    }
    public void setPlugConnectionChangeAllowed(boolean allowed) {
        if (plugConnectionChangeAllowed == allowed) return;
        plugConnectionChangeAllowed = allowed;
        plugConnectionChangeAllowedChanged = true;
        propChangeSupport.firePropertyChange("plugConnectionChangeAllowed", !allowed, allowed);
    }

    public boolean isMenuBarVisible() {
        return menuBarVisible;
    }
    public void setMenuBarVisible(boolean visible) {
        if (menuBarVisible == visible) return;
        menuBarVisible = visible;
        menuBarVisibleChanged = true;
        propChangeSupport.firePropertyChange("menuBarVisible", !visible, visible);
    }

    public boolean isOutlineDragEnabled() {
        return outlineDragEnabled;
    }
    public void setOutlineDragEnabled(boolean enabled) {
        if (outlineDragEnabled == enabled) return;
        outlineDragEnabled = enabled;
        outlineDragEnabledChanged = true;
        propChangeSupport.firePropertyChange("outlineDragEnabled", !enabled, enabled);
    }

    public boolean isComponentActivationMethodChangeAllowed() {
        return componentActivationMethodChangeAllowed;
    }
    public void setComponentActivationMethodChangeAllowed(boolean allowed) {
        if (componentActivationMethodChangeAllowed == allowed) return;
        componentActivationMethodChangeAllowed = allowed;
        componentActivationMethodChangeAllowedChanged = true;
        propChangeSupport.firePropertyChange("componentActivationMethodChangeAllowed", !allowed, allowed);
    }

    public boolean isMwdResizable() {
        return mwdResizable;
    }
    public void setMwdResizable(boolean resizable) {
        if (mwdResizable == resizable) return;
        mwdResizable = resizable;
        mwdResizableChanged = true;
        propChangeSupport.firePropertyChange("mwdResizable", !resizable, resizable);
        setMwdAutoExpandable(false);
        mwdAutoExpandableEnabled = resizable;
        propChangeSupport.firePropertyChange("mwdAutoExpandableEnabled", !mwdAutoExpandableEnabled, mwdAutoExpandableEnabled);
        desktopHeightEnabled = resizable;
        propChangeSupport.firePropertyChange("desktopHeightEnabled", !desktopHeightEnabled, desktopHeightEnabled);
        desktopWidthEnabled = resizable;
        propChangeSupport.firePropertyChange("desktopWidthEnabled", !desktopWidthEnabled, desktopWidthEnabled);
    }

    public boolean isMwdAutoExpandable() {
        return mwdAutoExpandable;
    }
    public void setMwdAutoExpandable(boolean expandable) {
        if (mwdAutoExpandable == expandable) return;
        mwdAutoExpandable = expandable;
        mwdAutoExpandableChanged = true;
        propChangeSupport.firePropertyChange("mwdAutoExpandable", !expandable, expandable);
    }

    public boolean isMwdAutoScrollable() {
        return mwdAutoScrollable;
    }
    public void setMwdAutoScrollable(boolean scrollable) {
        if (mwdAutoScrollable == scrollable) return;
        mwdAutoScrollable = scrollable;
        mwdAutoScrollableChanged = true;
        propChangeSupport.firePropertyChange("mwdAutoScrollable", !scrollable, scrollable);
    }

    public boolean isComponentsMoveBeyondMwdBounds() {
        return componentsMoveBeyondMwdBounds;
    }
    public void setComponentsMoveBeyondMwdBounds(boolean beyond) {
        if (componentsMoveBeyondMwdBounds == beyond) return;
        componentsMoveBeyondMwdBounds = beyond;
        componentsMoveBeyondMwdBoundsChanged = true;
        propChangeSupport.firePropertyChange("componentsMoveBeyondMwdBounds", !beyond, beyond);
        setMwdAutoExpandable(false);
        mwdAutoExpandableEnabled = beyond;
        propChangeSupport.firePropertyChange("mwdAutoExpandableEnabled", !mwdAutoExpandableEnabled, mwdAutoExpandableEnabled);
    }

    public boolean isDesktopDraggable() {
        return desktopDraggable;
    }
    public void setDesktopDraggable(boolean draggable) {
        if (desktopDraggable == draggable) return;
        desktopDraggable = draggable;
        desktopDraggableChanged = true;
        propChangeSupport.firePropertyChange("desktopDraggable", !draggable, draggable);
    }

    public boolean isMenuSystemHeavyWeight() {
        return menuSystemHeavyWeight;
    }
    public void setMenuSystemHeavyWeight(boolean heavyweight) {
        if (menuSystemHeavyWeight == heavyweight) return;
        menuSystemHeavyWeight = heavyweight;
        menuSystemHeavyWeightChanged = true;
        propChangeSupport.firePropertyChange("menuSystemHeavyWeight", !heavyweight, heavyweight);
    }

    public int getHorizontalScrollbarPolicy() {
        return horizontalScrollbarPolicy;
    }
    public void setHorizontalScrollbarPolicy(int policy) {
        if (horizontalScrollbarPolicy == policy) return;
        int prevPolicy = horizontalScrollbarPolicy;
        horizontalScrollbarPolicy = policy;
        horizontalScrollbarPolicyChanged = true;
        propChangeSupport.firePropertyChange("horizontalScrollbarPolicy", prevPolicy, policy);
    }

    public int getVerticalScrollbarPolicy() {
        return verticalScrollbarPolicy;
    }
    public void setVerticalScrollbarPolicy(int policy) {
        if (verticalScrollbarPolicy == policy) return;
        int prevPolicy = verticalScrollbarPolicy;
        verticalScrollbarPolicy = policy;
        verticalScrollbarPolicyChanged = true;
        propChangeSupport.firePropertyChange("verticalScrollbarPolicy", prevPolicy, policy);
    }

    public boolean isActiveComponentHighlighted() {
        return activeComponentHighlighted;
    }
    public void setActiveComponentHighlighted(boolean highlighted) {
        if (activeComponentHighlighted == highlighted) return;
        activeComponentHighlighted = highlighted;
        activeComponentHighlightedChanged = true;
        propChangeSupport.firePropertyChange("activeComponentHighlighted", !highlighted, highlighted);
    }

    public boolean isEnsureActiveComponentVisible() {
        return ensureActiveComponentVisible;
    }
    public void setEnsureActiveComponentVisible(boolean visible) {
        if (ensureActiveComponentVisible == visible) return;
        ensureActiveComponentVisible = visible;
        ensureActiveComponentVisibleChanged = true;
        propChangeSupport.firePropertyChange("ensureActiveComponentVisible", !visible, visible);
    }

    public boolean isMwdPrintAllowed() {
        return mwdPrintAllowed;
    }
    public void setMwdPrintAllowed(boolean allowed) {
        if (mwdPrintAllowed == allowed) return;
        mwdPrintAllowed = allowed;
        mwdPrintAllowedChanged = true;
        propChangeSupport.firePropertyChange("mwdPrintAllowed", !allowed, allowed);
    }

    public boolean isMwdPageSetupAllowed() {
        return mwdPageSetupAllowed;
    }
    public void setMwdPageSetupAllowed(boolean allowed) {
        if (mwdPageSetupAllowed == allowed) return;
        mwdPageSetupAllowed = allowed;
        mwdPageSetupAllowedChanged = true;
        propChangeSupport.firePropertyChange("mwdPageSetupAllowed", !allowed, allowed);
    }

    public boolean isComponentPrintAllowed() {
        return componentPrintAllowed;
    }
    public void setComponentPrintAllowed(boolean allowed) {
        if (componentPrintAllowed == allowed) return;
        componentPrintAllowed = allowed;
        componentPrintAllowedChanged = true;
        propChangeSupport.firePropertyChange("componentPrintAllowed", !allowed, allowed);
    }

    public boolean isComponentMinimizeAllowed() {
        return componentMinimizeAllowed;
    }
    public void setComponentMinimizeAllowed(boolean allowed) {
        if (componentMinimizeAllowed == allowed) return;
        componentMinimizeAllowed = allowed;
        componentMinimizeAllowedChanged = true;
        propChangeSupport.firePropertyChange("componentMinimizeAllowed", !allowed, allowed);
    }

    public boolean isComponentMaximizeAllowed() {
        return componentMaximizeAllowed;
    }
    public void setComponentMaximizeAllowed(boolean allowed) {
        if (componentMaximizeAllowed == allowed) return;
        componentMaximizeAllowed = allowed;
        componentMaximizeAllowedChanged = true;
        propChangeSupport.firePropertyChange("componentMaximizeAllowed", !allowed, allowed);
    }

    public int getDesktopWidth() {
        return desktopWidth;
    }
    public void setDesktopWidth(int width) {
        if (desktopWidth == width) return;
        int prevWidth  = desktopWidth;
        desktopWidth = width;
        desktopWidthChanged = true;
        propChangeSupport.firePropertyChange("componentMaximizeAllowed", prevWidth, desktopWidth);
    }

    public int getDesktopHeight() {
        return desktopHeight;
    }
    public void setDesktopHeight(int height) {
        if (desktopHeight == height) return;
        int prevHeight  = desktopWidth;
        desktopHeight = height;
        desktopHeightChanged = true;
        propChangeSupport.firePropertyChange("componentMaximizeAllowed", prevHeight, desktopHeight);
    }

    void applyViewSettings() {
        if (view == composer.currentView) {
            if (minimizeButtonVisibleChanged)
                composer.setMinimizeButtonVisible(minimizeButtonVisible);
            if (maximizeButtonVisibleChanged)
                composer.setMaximizeButtonVisible(maximizeButtonVisible);
            if (closeButtonVisibleChanged)
                composer.setCloseButtonVisible(closeButtonVisible);
            if (controlBarsVisibleChanged)
                composer.setControlBarsVisible(controlBarsVisible);
            if (controlBarTitleActiveChanged)
                composer.setControlBarTitleActive(controlBarTitleActive);
            if (helpButtonVisibleChanged)
                composer.setHelpButtonVisible(helpButtonVisible);
            if (plugButtonVisibleChanged)
                composer.setPinButtonVisible(plugButtonVisible = true);
            if (infoButtonVisibleChanged)
                composer.setInfoButtonVisible(infoButtonVisible);
            if (resizeAllowedChanged)
                composer.setResizeAllowed(resizeAllowed);
            if (moveAllowedChanged)
                composer.setMoveAllowed(moveAllowed);
            if (mwdBgrdChangeAllowedChanged)
                composer.setMwdBgrdChangeAllowed(mwdBgrdChangeAllowed);
            if (plugConnectionChangeAllowedChanged)
                composer.setPlugConnectionChangeAllowed(plugConnectionChangeAllowed);
            if (menuBarVisibleChanged)
                composer.setMenuBarVisible(menuBarVisible);
            if (outlineDragEnabledChanged)
                composer.setOutlineDragEnabled(outlineDragEnabled);
            if (componentActivationMethodChangeAllowedChanged)
                composer.setComponentActivationMethodChangeAllowed(componentActivationMethodChangeAllowed);
            if (mwdResizableChanged)
                composer.setMwdResizable(mwdResizable);
            if (mwdAutoExpandableChanged)
                composer.setMwdAutoExpandable(mwdAutoExpandable);
            if (mwdAutoScrollableChanged)
                composer.setMwdAutoScrollable(mwdAutoScrollable);
            if (componentsMoveBeyondMwdBoundsChanged)
                composer.setComponentsMoveBeyondMwdBounds(componentsMoveBeyondMwdBounds);
            if (desktopDraggableChanged)
                composer.setDesktopDraggable(desktopDraggable);
            if (menuSystemHeavyWeightChanged)
                composer.setMenuSystemHeavyWeight(menuSystemHeavyWeight);
            if (horizontalScrollbarPolicyChanged || verticalScrollbarPolicyChanged)
                composer.setScrollBarPolicies(verticalScrollbarPolicy, horizontalScrollbarPolicy);
            if (activeComponentHighlightedChanged)
                composer.setActiveComponentHighlighted(activeComponentHighlighted);
            if (ensureActiveComponentVisibleChanged)
                composer.setEnsureActiveComponentAlwaysVisible(ensureActiveComponentVisible);
            if (mwdPrintAllowedChanged)
                composer.setMicroworldPrintAllowed(mwdPrintAllowed);
            if (mwdPageSetupAllowedChanged)
                composer.setPageSetupAllowed(mwdPageSetupAllowed);
            if (componentPrintAllowedChanged)
                composer.setComponentPrintAllowed(componentPrintAllowed);
            if (componentMinimizeAllowedChanged)
                composer.setComponentMinimizeAllowed(componentMinimizeAllowed);
            if (componentMaximizeAllowedChanged)
                composer.setComponentMaximizeAllowed(componentMaximizeAllowed);
            composer.setMicroworldSize(new Dimension(desktopWidth, desktopHeight));
        }else{
            if (minimizeButtonVisibleChanged)
                view.minimizeButtonVisible = minimizeButtonVisible;
            if (maximizeButtonVisibleChanged)
                view.maximizeButtonVisible = maximizeButtonVisible;
            if (closeButtonVisibleChanged)
                view.closeButtonVisible = closeButtonVisible;
            if (controlBarsVisibleChanged)
                view.controlBarsVisible = controlBarsVisible;
            if (controlBarTitleActiveChanged)
                view.controlBarTitleActive = controlBarTitleActive;
            if (helpButtonVisibleChanged)
                view.helpButtonVisible = helpButtonVisible;
            if (plugButtonVisibleChanged)
                view.pinButtonVisible= plugButtonVisible;
            if (infoButtonVisibleChanged)
                view.infoButtonVisible = infoButtonVisible;
            if (resizeAllowedChanged)
                view.resizeAllowed = resizeAllowed;
            if (moveAllowedChanged)
                view.moveAllowed = moveAllowed;
            if (mwdBgrdChangeAllowedChanged)
                view.mwdBgrdChangeAllowed = mwdBgrdChangeAllowed;
            if (plugConnectionChangeAllowedChanged)
                view.plugConnectionChangeAllowed = plugConnectionChangeAllowed;
            if (menuBarVisibleChanged)
                view.menuBarVisible = menuBarVisible;
            if (outlineDragEnabledChanged)
                view.outlineDragEnabled = outlineDragEnabled;
            if (componentActivationMethodChangeAllowedChanged)
                view.componentActivationMethodChangeAllowed = componentActivationMethodChangeAllowed;
            if (mwdResizableChanged)
                view.mwdResizable = mwdResizable;
            if (mwdAutoExpandableChanged)
                view.mwdAutoExpandable = mwdAutoExpandable;
            if (mwdAutoScrollableChanged)
                view.mwdAutoScrollable = mwdAutoScrollable;
            if (componentsMoveBeyondMwdBoundsChanged)
                view.componentsMoveBeyondMwdBounds = componentsMoveBeyondMwdBounds;
            if (desktopDraggableChanged)
                view.desktopDraggable = desktopDraggable;
            if (menuSystemHeavyWeightChanged)
                view.menuSystemHeavyWeight = menuSystemHeavyWeight;
            if (horizontalScrollbarPolicyChanged)
                view.horizontalScrollbarPolicy = horizontalScrollbarPolicy;
            if (verticalScrollbarPolicyChanged)
                view.verticalScrollbarPolicy = verticalScrollbarPolicy;
            if (activeComponentHighlightedChanged)
                view.activeComponentHighlighted = activeComponentHighlighted;
            if (ensureActiveComponentVisibleChanged)
                view.ensureActiveComponentVisible = ensureActiveComponentVisible;
            if (mwdPrintAllowedChanged)
                view.mwdPrintAllowed = mwdPrintAllowed;
            if (mwdPageSetupAllowedChanged)
                view.mwdPageSetupAllowed = mwdPageSetupAllowed;
            if (componentPrintAllowedChanged)
                view.componentPrintAllowed = componentPrintAllowed;
            if (componentMinimizeAllowedChanged)
                view.componentMinimizeAllowed = componentMinimizeAllowed;
            if (componentMaximizeAllowedChanged)
                view.componentMaximizeAllowed = componentMaximizeAllowed;
            if (desktopWidthChanged)
                view.desktopWidth = desktopWidth;
            if (desktopHeightChanged)
                view.desktopHeight = desktopHeight;

            /* Some of the view settings are also stored on a component-basis, cause they can
             * be edited on a component basis, i.e. whether the component is resizable. So the
             * changes have also to be propagated inside the components of the microworld. The
             * individual settings are stored in DesktopItemInfos and this is the place where the
             * changes have to be propagated.
             */
            DesktopItemViewInfo[] desktopItemInfo = view.desktopItemInfo;
            for (int i=0; i<desktopItemInfo.length; i++) {
                if (ComponentViewInfo.class.isAssignableFrom(desktopItemInfo[i].getClass())) {
                    ComponentViewInfo componentInfo = (ComponentViewInfo) desktopItemInfo[i];
                    StorageStructure state = componentInfo.getState();
                    if (controlBarsVisibleChanged)
                        state.put("TitlePanelVisible", controlBarsVisible);
                    if (minimizeButtonVisibleChanged)
                        state.put("MinimizeButtonVisible", minimizeButtonVisible);
                    if (maximizeButtonVisibleChanged)
                        state.put("MaximizeButtonVisible", maximizeButtonVisible);
                    if (closeButtonVisibleChanged)
                        state.put("CloseButtonVisible", closeButtonVisible);
                    if (controlBarTitleActiveChanged)
                        state.put("ComponentNameChangeableFromMenuBar", controlBarTitleActive);
                    if (helpButtonVisibleChanged)
                        state.put("HelpButtonVisible", helpButtonVisible);
                    if (plugButtonVisibleChanged)
                        state.put("PlugButtonVisible", plugButtonVisible);
                    if (infoButtonVisibleChanged)
                        state.put("InfoButtonVisible", infoButtonVisible);
                    if (resizeAllowedChanged)
                        state.put("Resizable", resizeAllowed);
                    if (componentMinimizeAllowedChanged)
                        state.put("Iconifiable", componentMinimizeAllowed);
                    if (componentMaximizeAllowedChanged)
                        state.put("Maximizable", componentMaximizeAllowed);
                }else{ //InvisibleComponentViewInfo
                    InvisibleComponentViewInfo componentInfo = (InvisibleComponentViewInfo) desktopItemInfo[i];
                    StorageStructure state = componentInfo.getState();
                    if (resizeAllowedChanged)
                        state.put("Resizable", resizeAllowed);
                    if (componentMinimizeAllowedChanged)
                        state.put("Iconifiable", componentMinimizeAllowed);
                    if (componentMaximizeAllowedChanged)
                        state.put("Maximizable", componentMaximizeAllowed);
                }
            }
        }
    }
}

interface PropertyListenRequest {
    public void addPropertyChangeListener(PropertyChangeListener l);
    public void removePropertyChangeListener(PropertyChangeListener l);
    public void removeAllPropertyChangeListeners();
}

class MyPropertyChangeSupport extends PropertyChangeSupport {
    ArrayList listeners = new ArrayList();

    public MyPropertyChangeSupport(Object source) {
        super(source);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        super.addPropertyChangeListener(l);
        if (listeners.indexOf(l) == -1)
            listeners.add(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        super.removePropertyChangeListener(l);
        listeners.remove(l);
    }

    public void removeAllPropertyChangeListeners() {
        for (int i=listeners.size()-1; i>=0; i--)
            removePropertyChangeListener((PropertyChangeListener) listeners.get(i));
    }

}

class PropertiesPanel extends JPanel {
    JPanel child = null;

    public PropertiesPanel(boolean b) {
        super(b);
        setLayout(new BorderLayout());
    }

    public void setPanel(JPanel p) {
        removeAll();
        add(p, BorderLayout.CENTER);
        child = p;
    }

    public boolean containsSettingsPanel() {
        if (child == null) return false;
        if (SettingsPanel.class.isAssignableFrom(child.getClass()))
            return true;
        return false;
    }

    public void clear() {
        removeAll();
        child = null;
    }
}
/*class NodeRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
    Dimension size = new Dimension(0, 0);
*/
/*    public Dimension getPreferredSize() {
        if (size == null) {
            size = super.getPreferredSize();
        }

    }
*/
/*    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        System.out.println("value:" + value.getClass());
        if (SettingNode.class.isAssignableFrom(comp.getClass())) {
//            size.height = super.getPreferredSize().;
//            size.y = 0;
            setPreferredSize(size);
        }
        return comp;
    }
}
*/
