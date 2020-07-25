package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlatePart;
import gr.cti.eslate.base.container.event.ActiveComponentChangedEvent;
import gr.cti.eslate.base.container.event.ESlateContainerAdapter;
import gr.cti.eslate.base.container.event.MicroworldListener;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.StorageStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Customizer;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import virtuoso.logo.InterpEnviron;
import virtuoso.logo.InterpreterThread;
import virtuoso.logo.MyMachine;
import virtuoso.logo.Tokenizer;

/**
 * User: Yiorgos Tsironis
 */
public class ESlateComposer extends ESlateContainer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6594508243105779791L;
	/* The path to the 'jikes' exe.
     */
    static String pathToJikes = null;
    /**
     * Class with utility methods for the ESlateComposer.
     */
    protected ESlateComposerUtils composerUtils = new ESlateComposerUtils(this);
    /* The one and only one Bean properties window there can exist in an ESlateComposer session.
     */
    BeanInfoDialog propertyEventEditor = null;
    /** The composer's script dialog. */
    protected ScriptDialog scriptDialog = null;
    /* The highlight and font settings of the ScriptDialog for the current microworld.
     */
    StorageStructure scriptDialogFontColorSettings = null;
    /* The main font of the ScriptDialog.
     */
    Font scriptDialogMainFont = null;
    /** Flag that indicates whether the property editors have been initialized or not.
     */
    /* The current directory of the file dialogs of the LogoScriptDialogs */
    String logoScriptDialogCurrentDir = null;
    private boolean propertyEditorsRegistered = false;
    /* The menuPanel contains the "menuBar" and any other auxilliary buttons
     */
    MenuPanel menuPanel;
    /* "northPanel" contains the "menuPanel".
     */
    JPanel northPanel;
    /* The pop-up menu with all the microworld functionality. It is displayed when the
     * user right-clicks inside a microworld.
     */
    JPopupMenu mwdPopupMenu = null;
    /* The pop-up menu with all the visible component functionality. It is displayed when the
     * user right-clicks on a component's titlebar.
     */
    JPopupMenu componentPopupMenu = null;
    /* The pop-up menu with all the invisible component functionality. It is displayed when the
     * user right-clicks on the icon of the invisible component.
     */
    JPopupMenu invisibleComponentPopupMenu = null;
    // Used in "componentPopupMenu".
    protected JCheckBoxMenuItem minimizeButtonVisibleItem, maximizeButtonVisibleItem, closeButtonVisibleItem, compResizable;
    protected JCheckBoxMenuItem plugButtonVisibleItem, helpButtonVisibleItem, infoButtonVisibleItem, compNameChangeableFromCompoBar;
    protected JCheckBoxMenuItem compActivatableByMousePress;
    JMenuItem componentRenameItem, componentRemoveItem, componentCutItem, componentCopyItem;
    protected JMenuItem compHelp, compAbout, componentSettingsItem;
    // Used in "invisibleComponentPopupMenu".
    protected JMenuItem invisibleComponentRenameItem, invisibleComponentRemoveItem, invisibleComponentPlugsItem;
    protected JMenuItem invisibleComponentCutItem, invisibleComponentCopyItem;
    protected JMenuItem invisibleComponentAboutItem, invisibleComponentHelpItem;
    // Actions
    Action componentEditorAction = null;
    Action containerSettingsAction = null, gridEditorAction = null;
    Action pmEditorAction = null, scriptEditorAction = null, viewEditorAction = null;
    /* The 'ESlateContainerAdapter' which updates the 'propertyEventEditor' with the properties of
     * the component which becomes active.
     */
    ESlateContainerAdapter propertyEditorUpdater = new ESlateContainerAdapter() {
        public void activeComponentChanged(ActiveComponentChangedEvent e) {
        }
    };

    public void initialize() {
        super.initialize();
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        add(northPanel, BorderLayout.NORTH);
        northPanel.setVisible(true);
        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        add(southPanel, BorderLayout.SOUTH);
        southPanel.setVisible(true);
        lc.addMouseListener(createContainerRightMouseButtonListener());
    }

    /**
     * @see gr.cti.eslate.base.container.ESlateContainer#initializeActions()
     */
    protected void initializeActions() {
        super.initializeActions();
        // Tool actions
        String actionString = containerBundle.getString("ComponentEditor");
        componentEditorAction = new ComponentEditorAction(this, actionString);
        actionString = containerBundle.getString("MicroworldProperties");
        microworldPropertiesAction = new MicroworldPropertiesAction(this, actionString);
        actionString = "Script Editor";
        scriptEditorAction = new ScriptEditorAction(this, actionString);
        actionString = containerBundle.getString("ViewEditor");
        viewEditorAction = new ViewEditorAction(this, actionString);
        actionString = containerBundle.getString("PreferencesGrid");
        gridEditorAction = new GridEditorAction(this, actionString);
        actionString = containerBundle.getString("PerformanceMonitorEdit");
        pmEditorAction = new PMEditorAction(this, actionString);
        actionString = containerBundle.getString("PreferencesContainer");
        containerSettingsAction = new ContainerSettingsAction(this, actionString);
    }

    /**
     * @see gr.cti.eslate.base.container.ESlateContainer#initLogoEnvironment()
     */
    protected void initLogoEnvironment() {
        super.initLogoEnvironment();
    }

    /**
     * @see gr.cti.eslate.base.container.ESlateContainer#registerContainerActions()
     */
    protected void registerContainerActions() {
        super.registerContainerActions();
        ActionMap containerActionMap = getActionMap();
        InputMap containerInputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Open the component editor
        String actionName = (String) componentEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, componentEditorAction);
        containerInputMap.put((KeyStroke) componentEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the script editor
        actionName = (String) scriptEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, scriptEditorAction);
        containerInputMap.put((KeyStroke) scriptEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the view editor
        actionName = (String) viewEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, viewEditorAction);
        containerInputMap.put((KeyStroke) viewEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Microworld settings
        actionName = (String) microworldPropertiesAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, microworldPropertiesAction);
        containerInputMap.put((KeyStroke) microworldPropertiesAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the E-Slate settings dialog
        actionName = (String) containerSettingsAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, containerSettingsAction);
        containerInputMap.put((KeyStroke) containerSettingsAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the grid dialog
        actionName = (String) gridEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, gridEditorAction);
        containerInputMap.put((KeyStroke) gridEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // Open the performance manager UI
        actionName = (String) pmEditorAction.getValue(AbstractAction.NAME);
        containerActionMap.put(actionName, pmEditorAction);
        containerInputMap.put((KeyStroke) pmEditorAction.getValue(AbstractAction.ACCELERATOR_KEY),
                              actionName
        );

        // View navigation accelerator keys
        registerKeyboardAction(new ViewNavigationListener(this, 1),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 2),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 3),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 4),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 5),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 6),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 7),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 8),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 9),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
        registerKeyboardAction(new ViewNavigationListener(this, 0),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_MASK),
                                  WHEN_IN_FOCUSED_WINDOW);
    }
    
    public void updateBeanInfoPanel() {
        if (loadingMwd) return;
        if (propertyEventEditor != null) {
            int activeComponentIndex = -1;
            ESlateComponent activeComponent = getActiveComponent();
            if (activeComponent != null)
                propertyEventEditor.addSecondLevelNode(activeComponent.getHandle().getComponent(),
                                                       activeComponent.getHandle().getComponentName());
/*II
            if (activeComponent != null)
                activeComponentIndex = mwdComponents.indexOf(activeComponent);
//            System.out.println("activeComponentIndex: " + activeComponentIndex);
            if (activeComponentIndex != -1) {
                ESlateComponent ecomponent = mwdComponents.components.get(activeComponentIndex);
                propertyEventEditor.addSecondLevelNode(ecomponent.object,
                                                       ecomponent.handle.getComponentName());
            }
*/
        }
    }

    /* Resets the tree of the component ComboBox in the propertyEventEditor */
    public void updatePropertyEventEditorTree() {
        if (propertyEventEditor != null) {
/*0            Object[] compos = new Object[components.size()];
            String[] compoNames = new String[components.size()];
            int k=0;
            for (int i=0; i<compos.length; i++) {
                compos
                ESlateHandle h = (ESlateHandle) eSlateHandles.at(i);
                compos[i] = h.getComponent();
                compoNames[i] = h.getComponentName();
            }
            propertyEventEditor.setSecondLevelNodes(compos, compoNames);
*/
            propertyEventEditor.setSecondLevelNodes(mwdComponents.getComponents(), mwdComponents.getComponentNames());
        }
    }

    public void displayComponentEditor() {
        ESlateComponent eslateComponent = getActiveComponent(); //mwdComponents.activeComponent;
/*0        int componentFrameIndex = -1;
        if (activeFrame != null)
            componentFrameIndex = componentFrames.indexOf(activeFrame);
*/
        containerUtils.forceMenuClose();
        super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
        if (propertyEventEditor == null) {
            propertyEventEditor = new BeanInfoDialog(ESlateComposer.this);
//0            int componentCount = componentFrames.size();
            int componentCount = mwdComponents.size();
            if (componentCount > 0) {
//0                    Object[] compos = new Object[eSlateHandles.size()];
//0                    String[] compoNames = new String[eSlateHandles.size()];
/*
                    Object[] compos = new Object[mwdComponents.size()];
                    String[] compoNames = new String[mwdComponents.size()];
                    int k=0;
                    for (int i=0; i<compos.length; i++) {
//0                        ESlateHandle h = (ESlateHandle) eSlateHandles.at(i);
                        ESlateHandle h = mwdComponents.getComponents.get(i).handle;
                        compos[i] = h.getComponent();
                        compoNames[i] = h.getComponentName();
                    }
*/
                ESlateComponent[] components = mwdComponents.getESlateComponents();
                Object[] compos = new Object[mwdComponents.size()];
                String[] compoNames = new String[mwdComponents.size()];
                for (int i=0; i<components.length; i++) {
                    ESlateHandle h = components[i].getHandle();
                    compos[i] = h.getComponent();
                    compoNames[i] = h.getComponentName();
                }

                    propertyEventEditor.setSecondLevelNodes(compos, compoNames);
//0                    if (componentFrameIndex != -1) {
                    if (eslateComponent != null) {
                        propertyEventEditor.selectNode(propertyEventEditor.getSecondLevelNode(
                            eslateComponent.getHandle().getComponentName()));
//0                            ((ESlateHandle) eSlateHandles.at(componentFrameIndex)).getComponentName()));
                    }
            }

            if (microworld != null) {
//                propertyEventEditor.setPropertyEditorEnabledInternal(microworld.componentPropertyMgmtAllowed);
//                propertyEventEditor.setEventEditorEnabledInternal(microworld.componentEventMgmtAllowed);
//                propertyEventEditor.setSoundEditorEnabledInternal(microworld.componentSoundMgmtAllowed);
            }
            splitPane.setContent(containerBundle.getString("ComponentEditorTitle"), propertyEventEditor);
            ESlateComposer.this.addContainerListener(propertyEditorUpdater);
            if (eslateComponent != null && eslateComponent.getFrame() != null) {
                // Scroll the desktop, so that the edited component is visible.
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ESlateComponent eslateComp = getActiveComponent(); //mwdComponents.activeComponent;
                        ensureFrameIsVisible(eslateComp.getFrame(),
                             eslateComp.getFrame().getBounds(),
                             currentView.isMwdAutoExpandable() && currentView.isMwdResizable());
                    }
                });
            }
        }else{
/*            propertyEventEditor.selectNone(true);
            if (componentFrameIndex == -1) {
                propertyEventEditor.emptyPropertiesPanel();
                if (propertyEventEditor.eventEditorPanel != null)
                    propertyEventEditor.eventEditorPanel.updateEventPanels(null, null);
            }
*/
            propertyEventEditor.requestFocus();
        }
        setCursor(Cursor.getDefaultCursor(), false);
    }

    public void displayScriptEditor() {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.isComponentEventMgmtAllowed(), "componentEventMgmtAllowed");
        if (scriptDialog == null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR), false);
            scriptDialog = getScriptDialog();
            scriptDialog.populateTree();
			scriptMap.extractClassFiles();
//            if (componentScriptListeners != null)
//            scriptDialog.setScriptListenerTree(componentScriptListeners.getScriptListenerTree());
            scriptDialog.showDialog(getBounds());
            setCursor(Cursor.getDefaultCursor(), false);
        }else{
			scriptDialog.bringToFront();
        }
    }

    /**
     * Displays the editor of the microworld views.
     * @return <code>true</code>, if the microworld views were edited. <code>false</code>, if no editing occured.
     */
    public boolean displayViewEditor() {
        if (microworld == null) return false;

        ViewDialog v = new ViewDialog(parentFrame, this);
        if (v.getReturnCode() == ViewDialog.DIALOG_CANCELLED) return false;
        MicroworldViewList views = v.getViews();
        /* Compare the returned views, with the existing views. Remove any of
         * the existing views which do not exist in the list of returned views,
         * renamed the renamed views and add the new views
         */
        mwdViews = views;
        if (menuPanel != null) {
            menuPanel.microworldViewMenu.removeAll();
            for (int i=0; i<mwdViews.viewList.length; i++)
                menuPanel.microworldViewMenu.addItem(mwdViews.viewList[i].viewName);
        }
        return true;
    }

    /**
     * @see ESlateContainer#createComponent(String, java.awt.Point, java.awt.Dimension)
     * @param componentClassName
     * @param location
     * @param size
     */
    public ESlateComponent createComponent(String componentClassName, Point location, Dimension size) {
        ESlateComponent comp = super.createComponent(componentClassName, location, size);
        if (comp != null) {
            addComponentItemToComponentsMenu(comp.handle.getComponentName());
            addComponentItemToHelpMenu(comp.handle);
            // Update the component bar, if it is visible
            if (componentBar != null && componentBar.getParent() != null) {
                ComponentIcon ci = componentBar.addComponentIcon(comp.handle.getComponentName());
                ci.setSelection(true);
            }

        }
        return comp;
    }

    /* Starts a new empty microworld.
     */
    public boolean createNewMicroworld(boolean checkForChanges) {
    	System.out.println("DBG: createNewMicroworld started");
        if (!super.createNewMicroworld(checkForChanges)) return false;
        if (!loadingMwd)
            setMenuBarVisibleInternal(true);

        setMenuSystemHeavyWeight(currentView.menuSystemHeavyWeight);
        setMicroworldChanged(false);
        return true;
    }

    /**
     *
     * @param checkForChanges
     * @see ESlateContainer#closeMicroworld(boolean)
     */
    public boolean closeMicroworld(boolean checkForChanges) {
        if (microworld == null)
            return true;

        if (scriptDialog != null) {
            if (!scriptDialog.promptToSaveScript())
                return false;
        }
        boolean mwdClosed = super.closeMicroworld(checkForChanges);
        // Deselect the first item in the microworldListMenu
        if (menuPanel != null && menuPanel.microworldReopenMenu != null && menuPanel.microworldReopenMenu.getItemCount() > 0) {
            AbbreviatedCheckBoxMenuItem firstItem = (AbbreviatedCheckBoxMenuItem) menuPanel.microworldReopenMenu.getItem(0);
            if (firstItem != null)
                firstItem.setSelected(false);
        }
        return mwdClosed;
    }

    public void applyView(MicroworldView view) {
        if (microworld == null || view == null) return;
        super.applyView(view);
        if (view.microworldInfoSaved) {
            setMenuBarVisibleInternal(view.menuBarVisible);
            setMenuSystemHeavyWeightInternal(view.menuSystemHeavyWeight);
        }
    }

    public final void setMenuSystemHeavyWeight(boolean heavyweight) {
        /* When the microworld is locked, no-one has access to this setting. That does not apply to
         * scripts,cause while a microworld script is being executing the microworld is temporarily
         * unlocked. It's the scripts responsibility to reset the value of this setting.
         */
        if (microworld != null)
            microworld.checkSettingChangePriviledge();
        setMenuSystemHeavyWeightInternal(heavyweight);
    }

	final void setMenuSystemHeavyWeightInternal(boolean heavyweight) {
    	//Removed to use the same method in initialization. Changed the setMicroworldChanged
    	//to the value of the condition.
//        if (currentView.menuSystemHeavyWeight == heavyweight || menuPanel == null) return;
    	constructMenuPanel();
        currentView.menuSystemHeavyWeight = heavyweight;
        menuPanel.microworldMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.printMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.componentMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.componentPrintMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.toolsMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.helpMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
//        menuPanel.componentBarProperties.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.microworldSaveAs.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.microworldLoad.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.componentNew.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.componentsMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.microworldReopenMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.microworldNavigationMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);
        menuPanel.microworldPackMenu.getPopupMenu().setLightWeightPopupEnabled(!heavyweight);

        javax.swing.ToolTipManager.sharedInstance().setLightWeightPopupEnabled(!heavyweight);
        setMicroworldChanged(currentView.menuSystemHeavyWeight == heavyweight || menuPanel == null);
    }
    public final boolean isMenuSystemHeavyWeight() {
        return currentView.menuSystemHeavyWeight;
    }

    public final void setGridVisible(boolean visible, int step, Color color, boolean snapToGrid) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        if (!visible) {
            setGridVisible(false);
        }else{
            setGridVisible(true);
            setGridColor(color);
            setGridStep(step);
            setSnapToGrid(snapToGrid);
        }
    }

    public final void setGridVisible(boolean visible) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        if (lc.gridVisible == visible) return;
        if (!visible)
            setSnapToGrid(false);
        lc.gridVisible = visible;
        setMicroworldChanged(true);
        lc.repaint();
    }

    public boolean isGridVisible() {
        return ((DesktopPane) lc).gridVisible;
    }

    public final void setGridColor(Color color) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        if (((DesktopPane) lc).gridColor.equals(color)) return;
        ((DesktopPane) lc).gridColor = color;
        setMicroworldChanged(true);
        lc.repaint();
    }

    public Color getGridColor() {
        return ((DesktopPane) lc).gridColor;
    }

    public final void setGridStep(int step) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        if (((DesktopPane) lc).gridStep == step) return;
        ((DesktopPane) lc).gridStep = step;
        setMicroworldChanged(true);
        lc.repaint();
    }

    public int getGridStep() {
        return ((DesktopPane) lc).gridStep;
    }

    public final void setSnapToGrid(boolean snap) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        if (lc.snapToGrid == snap) return;
        /* When the grid is not visible, the snap status cannot change.
         */
        if (!isGridVisible()) return;
        lc.snapToGrid = snap;
        setMicroworldChanged(true);
    }

    public boolean isSnapToGrid() {
        return lc.snapToGrid;
    }

    /**
     * @see gr.cti.eslate.base.container.ESlateContainer#getState()
     */
    protected ESlateFieldMap2 getState() {
        ESlateFieldMap2 state = super.getState();
        // Save the font and color settings of the ScriptDialog
//        if (scriptDialog != null) {
//            scriptDialogFontColorSettings = scriptDialog.getEditor().getColorSettings();
//            scriptDialogMainFont = new Font(scriptDialog.getEditor().getFontName(), Font.PLAIN, scriptDialog.getEditor().getFontSize());
//        }
        state.put("Color and font settings of ScriptDialog", scriptDialogFontColorSettings);
        state.put("Main ScriptDialog font", scriptDialogMainFont);
        return state;
    }

    /**
     * Overriden to load the state of the ESlateComposer.
     * @param state the state
     * @see ESlateContainer#microworldLoaded(gr.cti.eslate.utils.StorageStructure)
     */
    protected void microworldLoaded(StorageStructure state) {
        super.microworldLoaded(state);
        setMenuBarVisibleInternal(currentView.menuBarVisible);
        if (state != null) {
            // Restore the font and color settings of the ScriptDialog
            scriptDialogFontColorSettings = (StorageStructure) state.get("Color and font settings of ScriptDialog", (Object) null);
            scriptDialogMainFont = (Font) state.get("Main ScriptDialog font", (Object) null);
//            if (scriptDialog != null && scriptDialogFontColorSettings != scriptDialog.getEditor().getColorSettings()) {
//                scriptDialog.updateColorAndFontSettings(scriptDialogFontColorSettings, scriptDialogMainFont);
//            }
        }

        // If the 'scriptDialog' is already open, then update its contents
        // and also extract the class files of the scripts of 'scriptMap'.
        // This ha to be done here, after the 'currentlyOpenMwdFile' has been
        // assigned, so that the sources of the scripts are loaded.
        if (scriptDialog != null) {
           scriptDialog.populateTree();
           scriptMap.extractClassFiles();
        }
        if (componentBar != null && componentBar.getParent() != null)
            componentBar.synchronizeComponentPanel();
    }
    protected void resetContainerEnv() {
        super.resetContainerEnv();
        if (menuPanel != null) menuPanel.microworldViewMenu.removeAll();
        if (scriptDialog != null) scriptDialog.clearTree();
        scriptDialogFontColorSettings = null;
        scriptDialogMainFont = null;
        setESlateComponentBarEnabled(true);
    }

    protected boolean setESlateLAF(Class lfClass, boolean updateContainerProperties) {
        super.setESlateLAF(lfClass, updateContainerProperties);
        if (propertyEventEditor != null) {
            if (propertyEventEditor.propertyEditorPanel.noPropertyPanel != null)
                SwingUtilities.updateComponentTreeUI(propertyEventEditor.propertyEditorPanel.noPropertyPanel);
            if (propertyEventEditor.eventEditorPanel.noListenerPanel != null)
                SwingUtilities.updateComponentTreeUI(propertyEventEditor.eventEditorPanel.noListenerPanel);
        }
        if (scriptDialog != null)
            SwingUtilities.updateComponentTreeUI(scriptDialog);

        if (mwdPopupMenu != null)
            SwingUtilities.updateComponentTreeUI(mwdPopupMenu);
        if (invisibleComponentPopupMenu != null)
            SwingUtilities.updateComponentTreeUI(invisibleComponentPopupMenu);
        if (componentPopupMenu != null)
            SwingUtilities.updateComponentTreeUI(mwdPopupMenu);
        if (componentBar != null) {
            SwingUtilities.updateComponentTreeUI(componentBar);
            if (componentBar.iconPopupMenu != null)
                SwingUtilities.updateComponentTreeUI(componentBar.iconPopupMenu);
        }
        return true;
    }

    void registerPropertyEditors() {
        if (propertyEditorsRegistered) return;
        propertyEditorsRegistered = true;
        java.beans.PropertyEditorManager.registerEditor(java.lang.String.class, gr.cti.eslate.base.container.StringPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Boolean.class, gr.cti.eslate.base.container.BooleanPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Integer.class, gr.cti.eslate.base.container.IntPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Float.class, gr.cti.eslate.base.container.FloatPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Double.class, gr.cti.eslate.base.container.DoublePropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Long.class, gr.cti.eslate.base.container.LongPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Short.class, gr.cti.eslate.base.container.ShortPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(java.util.Date.class, gr.cti.eslate.base.container.DatePropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Dimension.class, gr.cti.eslate.base.container.DimensionPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Point.class, gr.cti.eslate.base.container.PointPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Rectangle.class, gr.cti.eslate.base.container.RectanglePropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Insets.class, gr.cti.eslate.base.container.InsetsPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(File.class, gr.cti.eslate.base.container.FilePropertyEditor.class);

        Class iconEditorClass = null;
        try{
            iconEditorClass = Class.forName("gr.cti.eslate.imageEditor.ImageEditor");
        }catch (ClassNotFoundException exc) {}
        if (iconEditorClass != null) {
            java.beans.PropertyEditorManager.registerEditor(Icon.class, gr.cti.eslate.base.container.IconPropertyEditor.class);
            java.beans.PropertyEditorManager.registerEditor(java.awt.Image.class, gr.cti.eslate.base.container.ImagePropertyEditor.class);
        }

        java.beans.PropertyEditorManager.registerEditor(Color.class, gr.cti.eslate.base.container.ColorPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(Font.class, gr.cti.eslate.base.container.FontPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(java.awt.LayoutManager.class, gr.cti.eslate.base.container.LayoutPropertyEditor.class);
        java.beans.PropertyEditorManager.registerEditor(javax.swing.border.Border.class, gr.cti.eslate.base.container.BorderPropertyEditor.class);
    }

    public void initCustomizer() {
//II        if (mwdComponents.activeComponent == null) return;
        ESlateComponent activeComponent = null;
        if (activeComponent == null) return;
        Object component = activeComponent.getHandle().getComponent();
//        Class customizerClass = activeFrame.customizerClass;
        Class customizerClass = activeComponent.getCustomizerClass();
        if (customizerClass == null) return;
        if (!Component.class.isAssignableFrom(customizerClass)) {
            ESlateOptionPane.showMessageDialog(this, containerBundle.getString("Customizer1"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
        }
        if (!Customizer.class.isAssignableFrom(customizerClass)) {
            ESlateOptionPane.showMessageDialog(this, containerBundle.getString("Customizer2"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        Component cust = null;
        try{
            cust = (Component) customizerClass.newInstance();
        }catch (InstantiationException exc) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Customizer3") + " \"" + customizerClass.getName() + "\". ");
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            exc.printStackTrace(printWriter);
            dialog.setDetails(exc.getMessage() + '\n' + writer.toString());
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, this, true);
            return;
        }catch (IllegalAccessException exc) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Customizer3") + " \"" + customizerClass.getName() + "\". ");
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            exc.printStackTrace(printWriter);
            dialog.setDetails(exc.getMessage() + '\n' + writer.toString());
//            ESlateContainerUtils.showDialog(dialog, this, true, dialog.okButton);
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, this, true);
            return;
        }catch (Throwable thr) {
            DetailedErrorDialog dialog = new DetailedErrorDialog(parentFrame);
            dialog.setMessage(containerBundle.getString("Customizer3") + " \"" + customizerClass.getName() + "\". ");
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            thr.printStackTrace(printWriter);
            dialog.setDetails(thr.getMessage() + '\n' + writer.toString());
            ESlateContainerUtils.showDetailedErrorDialog(container, dialog, this, true);
            return;
        }
        if (cust == null) return;
        ((Customizer) cust).setObject(component);
        CustomizerDialog dialog = new CustomizerDialog(
                    parentFrame,
                    containerBundle.getString("Customizer") + ": " + component.getClass().getName().substring(component.getClass().getName().lastIndexOf('.') + 1),
                    cust);
        ESlateContainerUtils.showDialog(dialog, this, true);
    }

    protected void addComponentItemToHelpMenu(ESlateHandle h) {
        if (menuPanel == null) return;
        String componentClassName = h.getComponent().getClass().getName();
        String helpItemName = installedComponents.getName(componentClassName);
        if (helpItemName != null)
            menuPanel.helpMenu.addComponentHelpItem(helpItemName, h);
    }

    protected void removeComponentItemToHelpMenu(ESlateHandle h) {
        if (menuPanel == null) return;
        /* Check if there exist other components of the same class as the component
         * being removed in this microworld. If other components exis then let the
         * help menu item for this component class stays on the help menu. Otherwise
         * it is removed.
         */
        String compoClassName = h.getComponent().getClass().getName();
        int sameClassCompoCount = mwdComponents.getComponentsOfClassCount(compoClassName);
        if (sameClassCompoCount == 1)
            menuPanel.helpMenu.removeComponentHelpItem(installedComponents.getName(compoClassName));
    }

    protected void addComponentItemToComponentsMenu(String compoName) {
        if (menuPanel == null) return;

        JCheckBoxMenuItem compoItem = new JCheckBoxMenuItem(compoName);
        menuPanel.componentsMenu.add(compoItem);
//        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            compoItem.setFont(greekUIFont);
        compoItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                String compoName = ((JCheckBoxMenuItem) e.getSource()).getText();
/*0                ESlateInternalFrame fr = null;
                boolean frameFound = false;
                for (int i=0; i<componentFrames.size(); i++) {
                    fr = (ESlateInternalFrame) ESlateContainer.this.componentFrames.at(i);
                    if (fr.getTitle().equals(compoName)) {
                        frameFound = true;
                        break;
                    }
                }
                if (fr != null && frameFound) {
*/
                ESlateComponent compo = mwdComponents.getComponent(compoName);
                if (compo != null) {
                    DesktopItem desktopItem = compo.desktopItem;
                    try{
                        // If the frame is iconified, restore it.
                        if (desktopItem.isIcon()) {
                            /* If the frame was maximized before it was iconified, then maximize it again.
                             * Otherwise restore the frame.
                             */
                            if (desktopItem.getDesktopItemSize().equals(scrollPane.getViewport().getSize()) &&
                                desktopItem.getDesktopItemLocation().equals(scrollPane.getViewport().getViewPosition()))
                                desktopItem.setMaximum(true);
                            else
                                desktopItem.setIcon(false);
                        }
                    }catch (PropertyVetoException exc) {}
                    setActiveComponent(compo, true);
                }
            }
        });
    }

    protected void removeComponentItemFromComponentsMenu(String compoName) {
        if (menuPanel == null) return;
        JCheckBoxMenuItem element = null;
        javax.swing.MenuElement[] elements = menuPanel.componentsMenu.getPopupMenu().getSubElements();
        for (int i=0; i<elements.length; i++) {
            if (((JCheckBoxMenuItem) elements[i]).getText().equals(compoName)) {
                element = (JCheckBoxMenuItem) elements[i];
                break;
            }
        }
        if (element != null)
            menuPanel.componentsMenu.remove(element);
    }

    final void showMicroworldPropertiesDialog() {
          if (microworld == null) return;
          MicroworldPropertiesDialog mwdPropertiesDialog = new MicroworldPropertiesDialog(parentFrame, this);
          /* The microworld settings profile are not read until the first time
           * the ProfileDialog is displayed from the MwdPropertiesDialog. All this time
           * the 'mwdSettingsProfiles' is null and until the custom profiles are read
           * and this variable holds these profiles, no profiles are passed to the
           * MwdPropertiesDialog.
           */
          if (mwdSettingsProfiles != null)
              mwdPropertiesDialog.setCustomProfiles(mwdSettingsProfiles);
          if (microworld.isLocked()) {
              if (!mwdPropertiesDialog.unlockMicroworld())
                  return;
          }

          if (!microworld.isLocked())
              ESlateContainerUtils.showDialog(mwdPropertiesDialog, parentFrame, true);
          else
              return;

          if (mwdPropertiesDialog.getReturnCode() == MicroworldPropertiesDialog.DIALOG_OK) {
              mwdSettingsProfiles = mwdPropertiesDialog.getCustomProfiles();
              updateMicroworldProfilesSection();
          }
    }
    /* Updates only the "Microworld setting profiles" section of the registry.
     */
    private void updateMicroworldProfilesSection() {
        String properties = getContainerPropertiesFileContents();

        /* Update the "Microworld setting profiles" section of the registry.
         */
        int sectionStart = findRegistrySectionStart(properties, "[microworld setting profiles]");
        if (sectionStart != -1)
            properties = truncateRegistrySection(properties, sectionStart);
        else
            sectionStart = 0; //Write the new registry section at the beginning of the registry

        /* Create the string which contains the full path names of all the imported files with
         * microworld setting profiles.
         * Embed this string in the properties string.
         */
        String mwdSettingProfileStr = null;
        if (mwdSettingsProfiles != null && mwdSettingsProfiles.length > 0) {
            mwdSettingProfileStr = "[Microworld Setting Profiles]|";
            int itemCount = mwdSettingsProfiles.length;
            for (int i=0; i<itemCount; i++) {
                String fileName = mwdSettingsProfiles[i].fileName.trim();
                if (fileName != null && fileName.length() != 0)
                    mwdSettingProfileStr = mwdSettingProfileStr + fileName + '|';
            }
            mwdSettingProfileStr = mwdSettingProfileStr + '|';
            StringBuffer propBuff = new StringBuffer(properties);
            propBuff.insert(sectionStart, mwdSettingProfileStr);
            properties = propBuff.toString();

            writeContainerPropertiesFileContents(properties);
        }
    }

    final void adjustContainerSettings() {
        if (microworld != null)
            microworld.checkActionPriviledge(microworld.eslateOptionMgmtAllowed, "eslateOptionMgmtAllowed");

        Frame f = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);

        if (currentSoundTheme == null)
            currentSoundTheme = new SoundTheme(this);
        SoundTheme soundThemeClone = new SoundTheme(currentSoundTheme);

        ContainerSettingsDialog settingsDialog = new ContainerSettingsDialog(f, this);
//        settingsDialog.setInstalledComponents(installedComponentNames);
//        settingsDialog.setComponentAvailability(componentAvailability);
        try{
            settingsDialog.setInstalledComponents(
                        (InstalledComponentStructure) installedComponents.clone());
///// beginning of mantes code

            settingsDialog.setInstalledLAFs(
                        (InstalledLookAndFeelStructure) installedLAFs.clone());
//// end of mantes code
        }catch (CloneNotSupportedException exc) {
            System.out.println("Cannot clone the 'installedComponents' structure");
            return;
        }
        if (preloadMwdSiteAddress != null && preloadMwdSiteAddress.trim().length() != 0) {
            /* Find the user name for the site's address.
             */
            Enumeration enumeration = webSites.keys();
            String webSiteName = null;
            boolean foundSite = false;
            while (enumeration.hasMoreElements()) {
                webSiteName = (String) enumeration.nextElement();
                if (preloadMwdSiteAddress.equals((String) webSites.get(webSiteName))) {
                    foundSite = true;
                    break;
                }
            }
            if (foundSite && webSiteName != null) {
                settingsDialog.setPreloadedMicroworldSite(webSiteName);
                settingsDialog.setPreloadedMicroworld(preloadMwdFileName);
            }
        }else{
            settingsDialog.setPreloadedMicroworld(preloadMwdFileName);
        }
        settingsDialog.setWebSites(webSites);
        settingsDialog.setWebSiteAvailability(webSiteAvailability);
        settingsDialog.soundPanel.setSoundTheme(soundThemeClone, getESlateSoundThemePath());
        settingsDialog.soundPanel.setSoundThemeFiles(soundThemeFiles);

////nikosM
//        settingsDialog.setWebSiteCommonDirs(webSiteCommonDirs);
////nikosM end of change
        settingsDialog.showDialog(this);
        if (settingsDialog.getReturnCode() == settingsDialog.DIALOG_OK) {
//            installedComponentNames = settingsDialog.getInstalledComponents();
//            componentAvailability = settingsDialog.getComponentAvailability();
            installedComponents = settingsDialog.getInstalledComponents();
            installedLAFs = settingsDialog.getInstalledLookAndFeels();
            webSites = settingsDialog.getWebSites();
            webSiteAvailability = settingsDialog.getWebSiteAvailability();
////nikosM
//            webSiteCommonDirs = settingsDialog.getWebSiteCommonDirs();
////nikosM end of change

            String preloadMwdSiteName = settingsDialog.getPreloadedMicroworldSite();
            if (preloadMwdSiteName != null && preloadMwdSiteName.trim().length() != 0) {
                if (webSites.get(preloadMwdSiteName) != null) {
                    preloadMwdFileName = settingsDialog.getPreloadedMicroworld();
                    preloadMwdSiteAddress = (String) webSites.get(preloadMwdSiteName);
                }else{
                    preloadMwdFileName = null;
                    preloadMwdSiteAddress = null;
                }
            }else{
                preloadMwdFileName = settingsDialog.getPreloadedMicroworld();
            }

            if (this instanceof ESlateComposer) {
                ESlateComposer composer = (ESlateComposer) this;
                if (composer.menuPanel != null) {
                    composer.menuPanel.updateLookFeelMenu(installedLAFs);
//                if (composer.menuPanel == null) return;
                    composer.menuPanel.componentNew.valid = false;
                    composer.removeWebSiteMenuItems();
                    composer.createWebSiteMenuItems();
                }
            }

            webServerMicrosHandle.updateWebSiteMirrorRootDirs();

            SoundTheme soundTheme = settingsDialog.soundPanel.getSoundTheme();
//System.out.println("soundTheme != soundThemeClone: " + (soundTheme != soundThemeClone) + ", soundTheme.isModified(): " + soundTheme.isModified());
            if (soundTheme != soundThemeClone || soundTheme.isModified()) {
                currentSoundTheme = soundTheme;
                saveSoundTheme(currentSoundTheme, getESlateSoundThemePath(), settingsDialog.soundPanel.getSoundThemeFileName());
            }
            soundThemeFiles = settingsDialog.soundPanel.getSoundThemeFiles();

            writeContainerProperties();
        }
    }

    /** Adjusts the enabled state of the central component bar of an E-Slate microworld. When the
     *  bar is disabled, the user cannot make it visible and manage components through it. The central
     *  microworld's component bar appears directly under the E-Slate menus. The enabled/disabled
     *  state of the component bar is stored in the Microworld. This method simply 'executes' this
     *  state.
     */
    final void setESlateComponentBarEnabled(boolean enabled) {
        if (menuPanel == null)
            return;
        menuPanel.compoMenuLabel.setVisible(enabled);
//        toggleComponentBarItem.setEnabled(enabled);
        // If the bar is visible and it is disabled, then make it invisible.
        if (!enabled && componentBar != null && componentBar.getParent() != null)
            toggleComponentPanelInternal();
    }

    private void showMicroworldPopup(int x, int y) {
        if (mwdPopupMenu == null)
            getMicroworldPopup();
        mwdPopupMenu.setLightWeightPopupEnabled(!currentView.menuSystemHeavyWeight);
        pack.getPopupMenu().setLightWeightPopupEnabled(!currentView.menuSystemHeavyWeight);

        back.setEnabled(microworldBackAction.isEnabled());
        forward.setEnabled(microworldForwardAction.isEnabled());
        pinViewItem.setEnabled(plugEditorAction.isEnabled());

        if (microworld.eslateComponentBarEnabled) {
            toggleComponentBarItem.setEnabled(true);
            if (componentBar != null && componentBar.getParent() != null)
                toggleComponentBarItem.setText(containerBundle.getString("HideComponentBar"));
            else
                toggleComponentBarItem.setText(containerBundle.getString("ShowComponentBar"));
        }else
            toggleComponentBarItem.setEnabled(false);

        mwdPopupMenu.show(lc, x, y);
    }

    private JPopupMenu getMicroworldPopup () {
        mwdPopupMenu = new ESlatePopupMenu();

        // MICROWORLD-->PASTE
        String menuString = containerBundle.getString("ComponentPaste");
        componentPasteItem = (JMenuItem) mwdPopupMenu.add(new JMenuItem(menuString));
        componentPasteItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                                                        java.awt.Event.CTRL_MASK,
                                                        false));

        componentPasteItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                pasteComponent(true, ((ESlatePopupMenu) mwdPopupMenu).originLoc);
            }
        });

        // MICROWORLD-->COMPONENT BAR
        menuString = containerBundle.getString("ShowComponentBar");
        toggleComponentBarItem = (JMenuItem) mwdPopupMenu.add(new JMenuItem(menuString));
        toggleComponentBarItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                toggleComponentPanel();
            }
        });

        mwdPopupMenu.addSeparator();

        // MICROWORLD-->CLOSE
        JMenuItem close = (JMenuItem) mwdPopupMenu.add(new JMenuItem(microworldCloseAction));
        close.setAccelerator((KeyStroke) microworldCloseAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->BACK
        back = (JMenuItem) mwdPopupMenu.add(new JMenuItem(microworldBackAction));

        // MICROWORLD-->FORWARD
        forward = (JMenuItem) mwdPopupMenu.add(new JMenuItem(microworldForwardAction));

        mwdPopupMenu.addSeparator();

        // MICROWORLD-->PIN VIEW
        menuString = containerBundle.getString("PinView");
        pinViewItem = (JMenuItem) mwdPopupMenu.add(new JMenuItem(plugEditorAction));
        pinViewItem.setAccelerator((KeyStroke) plugEditorAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // MICROWORLD-->PROPERTIES
        JMenuItem mwdProperties = (JMenuItem) mwdPopupMenu.add(new JMenuItem(microworldPropertiesAction));

        // MICROWORLD-->PACK
        menuString = containerBundle.getString("MicroworldPack");
        pack = (JMenu) mwdPopupMenu.add(new JMenu(menuString));

        // MICROWORLD-->PACK-->UP
        menuString = containerBundle.getString("MicroworldPackUp");
	JMenuItem packUp = (JMenuItem) pack.add(new JMenuItem(menuString));
        packUp.addActionListener(microworldPackAction);

        // MICROWORLD-->PACK-->DOWN
        menuString = containerBundle.getString("MicroworldPackDown");
	JMenuItem packDown = (JMenuItem) pack.add(new JMenuItem(menuString));
        packDown.addActionListener(microworldPackAction);

        // MICROWORLD-->PACK-->LEFT
        menuString = containerBundle.getString("MicroworldPackLeft");
	JMenuItem packLeft = (JMenuItem) pack.add(new JMenuItem(menuString));
        packLeft.addActionListener(microworldPackAction);

        // MICROWORLD-->PACK-->RIGHT
        menuString = containerBundle.getString("MicroworldPackRight");
	JMenuItem packRight = (JMenuItem) pack.add(new JMenuItem(menuString));
        packRight.addActionListener(microworldPackAction);

        pack.addSeparator();

        // MICROWORLD-->PACK-->ALL SIDES
        menuString = containerBundle.getString("MicroworldPackAll");
	JMenuItem packAll = (JMenuItem) pack.add(new JMenuItem(menuString));
        packAll.addActionListener(microworldPackAction);

        // MICROWORLD-->INTERFACE
/*        menuString = containerBundle.getString("MicroworldInterface");
	JMenuItem mwdInterface = (JMenuItem) mwdPopupMenu.add(new JMenuItem(menuString));
//        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
//            mwdInterface.setFont(greekUIFont);

        mwdInterface.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                adjustContainerInterface();
            }
        });
*/
        mwdPopupMenu.pack();
        return mwdPopupMenu;
    }

    void showComponentPopup(ESlateInternalFrame frame, int x, int y) {
        if (microworld != null && !microworld.componentBarMenuEnabled)
            return;
        if (componentPopupMenu == null)
            createComponentPopup();
        componentPopupMenu.setLightWeightPopupEnabled(!currentView.menuSystemHeavyWeight);

        componentCutItem.setEnabled(componentCutAction.isEnabled());
        componentCopyItem.setEnabled(componentCopyAction.isEnabled());
        componentRenameItem.setEnabled(componentRenameAction.isEnabled());
		componentSettingsItem.setEnabled(componentSettingsAction.isEnabled());
        componentRemoveItem.setEnabled(componentRemoveAction.isEnabled());
        compResizable.setEnabled(componentResizableAction.isEnabled());
        compActivatableByMousePress.setEnabled(componentActivateOnClickAction.isEnabled());

//0        if (activeFrame == null) {
        ESlateInternalFrame activeFrame = null;
        if (mwdComponents.activeComponent != null)
            activeFrame = mwdComponents.activeComponent.frame;
        if (activeFrame == null) {
            maximizeButtonVisibleItem.setEnabled(false);
            minimizeButtonVisibleItem.setEnabled(false);
            closeButtonVisibleItem.setEnabled(false);
//            compResizable.setEnabled(false);
            plugButtonVisibleItem.setEnabled(false);
            helpButtonVisibleItem.setEnabled(false);
            infoButtonVisibleItem.setEnabled(false);
            compNameChangeableFromCompoBar.setEnabled(false);
//            compActivatableByMousePress.setEnabled(false);
            compHelp.setEnabled(false);
            compAbout.setEnabled(false);
//            componentCopyItem.setEnabled(false);
//            componentCutItem.setEnabled(false);
        }else{
//            componentCopyItem.setEnabled(true);
            compHelp.setEnabled(true);
            compAbout.setEnabled(true);
/*            if (activeFrame.isMaximum())
                compResizable.setEnabled(false);
            else
                compResizable.setEnabled(true);
*/
            compResizable.setSelected(activeFrame.isResizable());

            // Component rename
/*            if (microworld != null && microworld.eslateMwd.isRenamingAllowed())
                componentRenameItem.setEnabled(true);
            else
                componentRenameItem.setEnabled(false);
            // Component remove
            if (microworld != null && container.microworld.componentRemovalAllowed) {
//                componentCutItem.setEnabled(true);
                componentRemoveItem.setEnabled(true);
            }else{
//                componentCutItem.setEnabled(false);
                componentRemoveItem.setEnabled(false);
            }
*/
            if (activeFrame.isTitlePanelVisible()) {
                maximizeButtonVisibleItem.setEnabled(true);
                minimizeButtonVisibleItem.setEnabled(true);
                closeButtonVisibleItem.setEnabled(true);
                plugButtonVisibleItem.setEnabled(true);
                helpButtonVisibleItem.setEnabled(true);
                infoButtonVisibleItem.setEnabled(true);
                compNameChangeableFromCompoBar.setEnabled(true);
//                compActivatableByMousePress.setEnabled(true);
                closeButtonVisibleItem.setSelected(activeFrame.isCloseButtonVisible());
                maximizeButtonVisibleItem.setSelected(activeFrame.isMaximizeButtonVisible());
                minimizeButtonVisibleItem.setSelected(activeFrame.isMinimizeButtonVisible());
                plugButtonVisibleItem.setSelected(activeFrame.isPlugButtonVisible());
                helpButtonVisibleItem.setSelected(activeFrame.isHelpButtonVisible());
                infoButtonVisibleItem.setSelected(activeFrame.isInfoButtonVisible());
                compNameChangeableFromCompoBar.setSelected(activeFrame.isComponentNameChangeableFromMenuBar());
                compActivatableByMousePress.setSelected(activeFrame.isComponentActivatedOnMouseClick());
            }else{
                maximizeButtonVisibleItem.setEnabled(false);
                minimizeButtonVisibleItem.setEnabled(false);
                closeButtonVisibleItem.setEnabled(false);
                plugButtonVisibleItem.setEnabled(false);
                helpButtonVisibleItem.setEnabled(false);
                infoButtonVisibleItem.setEnabled(false);
                closeButtonVisibleItem.setSelected(false);
                maximizeButtonVisibleItem.setSelected(false);
                minimizeButtonVisibleItem.setSelected(false);
                plugButtonVisibleItem.setSelected(false);
                helpButtonVisibleItem.setSelected(false);
                infoButtonVisibleItem.setSelected(false);
                compNameChangeableFromCompoBar.setEnabled(false);
//                compActivatableByMousePress.setEnabled(false);
            }
        }

//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
                componentPopupMenu.show(frame, x, y);
//            }
//        });
    }

    JPopupMenu createComponentPopup () {
        componentPopupMenu = new ESlatePopupMenu();

        // COMPONENT-->RESIZABLE
		compResizable = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(componentResizableAction));

        // COMPONENT-->MINIMIZABLE
        String menuString = containerBundle.getString("MinimizeVisible");
		minimizeButtonVisibleItem = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        minimizeButtonVisibleItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setMinimizeButtonVisible(minimizeButtonVisibleItem.isSelected());
            }
        });

        // COMPONENT-->MAXIMIZABLE
        menuString = containerBundle.getString("MaximizeVisible");
		maximizeButtonVisibleItem = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        maximizeButtonVisibleItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setMaximizeButtonVisible(maximizeButtonVisibleItem.isSelected());
            }
        });

        // COMPONENT-->CLOSABLE
        menuString = containerBundle.getString("CloseVisible");
		closeButtonVisibleItem = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        closeButtonVisibleItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setCloseButtonVisible(closeButtonVisibleItem.isSelected());
            }
        });

        // COMPONENT-->PIN DISPLAY ENABLED
        menuString = containerBundle.getString("PlugVisible");
		plugButtonVisibleItem = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        plugButtonVisibleItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setPlugButtonVisible(plugButtonVisibleItem.isSelected());
            }
        });

        // COMPONENT-->HELP DISPLAY ENABLED
        menuString = containerBundle.getString("HelpVisible");
		helpButtonVisibleItem = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        helpButtonVisibleItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setHelpButtonVisible(helpButtonVisibleItem.isSelected());
            }
        });
        // COMPONENT-->INFO DISPLAY ENABLED
        menuString = containerBundle.getString("InfoVisible");
        infoButtonVisibleItem = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        infoButtonVisibleItem.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setInfoButtonVisible(infoButtonVisibleItem.isSelected());
            }
        });

        // COMPONENT-->CHANGE NAME FROM MENU BAR
        menuString = containerBundle.getString("ComponentNameChangeableFromMenuBar");
        compNameChangeableFromCompoBar = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(menuString));
        compNameChangeableFromCompoBar.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (mwdComponents.activeComponent == null)
                    return;
                mwdComponents.activeComponent.frame.setComponentNameChangeableFromMenuBar(compNameChangeableFromCompoBar.isSelected());
            }
        });

        // COMPONENT-->ACTIVATED ON MOUSE PRESS
        compActivatableByMousePress = (JCheckBoxMenuItem) componentPopupMenu.add(new JCheckBoxMenuItem(componentActivateOnClickAction));
        componentPopupMenu.addSeparator();

        // COMPONENT-->CUT
        componentCutItem = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentCutAction));
        componentCutItem.setAccelerator((KeyStroke) componentCutAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->COPY
        componentCopyItem = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentCopyAction));
        componentCopyItem.setAccelerator((KeyStroke) componentCopyAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->RENAME
        componentRenameItem = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentRenameAction));

		// COMPONENT-->SETTINGS
		componentSettingsItem = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentSettingsAction));
		componentSettingsItem.setAccelerator((KeyStroke) componentSettingsAction.getValue(AbstractAction.ACCELERATOR_KEY));

        componentPopupMenu.addSeparator();

        // COMPONENT-->REMOVE
        componentRemoveItem = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentRemoveAction));

        // COMPONENT-->PROPERTIES
/*        menuString = containerBundle.getString("ComponentProperties");
	      JMenuItem properties = (JMenuItem) componentPopupMenu.add(new JMenuItem(menuString));
        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            properties.setFont(greekUIFont);
        properties.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                displayPropertiesDialog();
            }
        });
        // COMPONENT-->EVENTS
        menuString = containerBundle.getString("ComponentEvents");
	      JMenuItem compoEvents = (JMenuItem) componentPopupMenu.add(new JMenuItem(menuString));
        if (containerBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerBundle_el_GR"))
            compoEvents.setFont(greekUIFont);
        compoEvents.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                displayEventDialog();
            }
        });
*/
        // COMPONENT-->HELP
        compHelp = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentHelpAction));

        // COMPONENT-->ABOUT
        compAbout = (JMenuItem) componentPopupMenu.add(new JMenuItem(componentInfoAction));

        return componentPopupMenu;
    }

    boolean isInvisibleComponentPopupVisible() {
        return invisibleComponentPopupMenu.isVisible();
    }

    void showInvisibleComponentPopup(Component component, int x, int y) {
    	getInvisibleComponentPopup();

        invisibleComponentPopupMenu.setLightWeightPopupEnabled(!currentView.menuSystemHeavyWeight);

        invisibleComponentCutItem.setEnabled(componentCopyAction.isEnabled());
        invisibleComponentRenameItem.setEnabled(componentRenameAction.isEnabled());
        invisibleComponentRemoveItem.setEnabled(componentRemoveAction.isEnabled());

        invisibleComponentPopupMenu.show(component, x, y);
    }

    private JPopupMenu getInvisibleComponentPopup () {
        invisibleComponentPopupMenu = new ESlatePopupMenu();

        // COMPONENT-->CUT
        invisibleComponentCutItem = (JMenuItem) invisibleComponentPopupMenu.add(new JMenuItem(componentCutAction));
        invisibleComponentCutItem.setAccelerator((KeyStroke) componentCutAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->COPY
        invisibleComponentCopyItem = (JMenuItem) invisibleComponentPopupMenu.add(new JMenuItem(componentCopyAction));
        invisibleComponentCopyItem.setAccelerator((KeyStroke) componentCopyAction.getValue(AbstractAction.ACCELERATOR_KEY));

        // COMPONENT-->RENAME
        invisibleComponentRenameItem = (JMenuItem) invisibleComponentPopupMenu.add(new JMenuItem(componentRenameAction));

        // COMPONENT-->REMOVE
        invisibleComponentRemoveItem = (JMenuItem) invisibleComponentPopupMenu.add(new JMenuItem(componentRemoveAction));

        invisibleComponentPopupMenu.addSeparator();

        // COMPONENT-->PINS
        String menuString = containerBundle.getString("ComponentPins");
        invisibleComponentPlugsItem = (JMenuItem) invisibleComponentPopupMenu.add(container.mwdComponents.activeComponent.getHandle().getPlugMenu());
        invisibleComponentPlugsItem.setText(menuString);
        invisibleComponentPlugsItem.setIcon(new ImageIcon(ESlatePart.class.getResource("plug1.gif")));

        // COMPONENT-->HELP
        invisibleComponentHelpItem = (JMenuItem) invisibleComponentPopupMenu.add(new JMenuItem(componentHelpAction));
        invisibleComponentHelpItem.setIcon(new ImageIcon(ESlatePart.class.getResource("help1.gif")));

        // COMPONENT-->INFO
        invisibleComponentAboutItem = (JMenuItem) invisibleComponentPopupMenu.add(new JMenuItem(componentInfoAction));
        invisibleComponentAboutItem.setIcon(new ImageIcon(ESlatePart.class.getResource("info1.gif")));

        invisibleComponentPopupMenu.pack();
        return invisibleComponentPopupMenu;
    }

    private MouseAdapter createContainerRightMouseButtonListener() {
        MouseAdapter ml = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                DesktopPane pane = (DesktopPane) e.getSource();
                if (pane.isModalFrameVisible()) return;
                /* When there is no open microworld or the menu bar is closed,
                 * the microworld pop-up is not displayed.
                 */
                if (microworld == null || !microworld.mwdPopupEnabled)
                    return;

                if (!SwingUtilities.isRightMouseButton(e))
                    return;

                showMicroworldPopup(e.getX(), e.getY());
            }
        };
        return ml;
    }

    /**
     * Returns the structure which stores all the SoundListeners of a microworld.
     */
    protected SoundListenerMap getSoundListenerMap() {
        return soundListenerMap;
    }

    protected ContainerScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * @see ESlateContainer#parentFrame
     */
    protected Frame getParentFrame() {
        return parentFrame;
    }

    /**
     * @see ESlateContainer#logoMachine
     */
    protected MyMachine getLogoMachine() {
        return logoMachine;
    }

    /**
     * @see ESlateContainer#logoEnvironment
     */
    protected InterpEnviron getLogoEnvironment() {
        return logoEnvironment;
    }

    /**
     * @see ESlateContainer#logoThread
     */
    protected InterpreterThread getLogoThread() {
        return logoThread;
    }

    /**
     * @see ESlateContainer#tokenizer
     */
    protected Tokenizer getTokenizer() {
        return tokenizer;
    }

    /**
     * @see gr.cti.eslate.base.container.ESlateContainer#getScriptListenerMap()
     */
    public ScriptListenerMap getScriptListenerMap() {
        return super.getScriptListenerMap();
    }

    protected ScriptMap getScriptMap() {
        return scriptMap;
    }

    ScriptDialog getScriptDialog() {
        if (scriptDialog == null)
            scriptDialog = new ScriptDialog(this);

//        if (scriptDialogFontColorSettings != scriptDialog.getEditor().getColorSettings()) {
//            scriptDialog.updateColorAndFontSettings(scriptDialogFontColorSettings, scriptDialogMainFont);
//        }
        return scriptDialog;
    }

    /**
     * @see gr.cti.eslate.base.container.ESlateContainer#getAppIcon()
     */
    protected java.awt.Image getAppIcon() {
        return super.getAppIcon();
    }

    /**
     * @see ESlateContainer#installedComponents
     */
    protected InstalledComponentStructure getInstalledComponents() {
        return installedComponents;
    }

    protected Rectangle getHighlightRect() {
        return super.getHighlightRect();
    }

    protected void setHighlightRect(Rectangle rect) {
        super.setHighlightRect(rect);    
    }

    protected void componentNameChanged(String newName, String oldName) {
        super.componentNameChanged(newName, oldName);

        // Update the component icon on the component bar, if the bar is visible
        if (componentBar != null && componentBar.getParent() != null)
            componentBar.setIconName(newName, oldName);
//                System.out.println("newName: " + newName + ", oldName: " + oldName);
        if (newName.equals(oldName))
            return;

        // Find the menu item named "oldName" and change its name to "newName"
        if (menuPanel != null) {
            javax.swing.MenuElement[] elements = menuPanel.componentsMenu.getPopupMenu().getSubElements();
            JCheckBoxMenuItem targetItem = null;
            for (int i=0; i<elements.length; i++) {
                if (((JCheckBoxMenuItem) elements[i]).getText().equals(oldName)) {
                    targetItem = (JCheckBoxMenuItem) elements[i];
                    break;
                }
            }

            if (targetItem != null)
                targetItem.setText(newName);
            else{
                if (!loadingMwd)
                    System.out.println("Serious inconsistency error in ComponentNameChangedListener componentNameChanged(): Can't find menu item to rename");
            }
        }

        /* If the splitpane's left panel is visible and contains the property editor,
         * then update the name of the component in the hierarchy tree combo box.
         */
        if (splitPane.isLeftPanelClosed()) return;
        Component comp = splitPane.getContent();
        if (comp.getClass().equals(BeanInfoDialog.class)) {
            ObjectHierarchyTreeNode node = ((BeanInfoDialog) comp).getSecondLevelNode(oldName);
            if (node != null) {
                node.objectName = newName;
                ((BeanInfoDialog) comp).objectHierarchyComboBox.repaint();
            }
        }
        ESlateComponent eslateComponent = mwdComponents.getComponent(newName);
        //The component is null when restoring from a .comp file because it has not been added yet
        if (eslateComponent!=null) {
	        ScriptListenerHandleNode handleNode = getScriptListenerMap().findHandleNode(eslateComponent.getHandle());
	        if (handleNode != null) {
	            if (scriptDialog != null)
	                scriptDialog.updateScriptTree();
	        }
        }
    }

    final void createWebSiteMenuItems() {
        if (menuPanel == null) return;
        // Count the available web sites.
        int availableCount = 0;
        Enumeration e = webSiteAvailability.keys();
        while (e.hasMoreElements()) {
            if (((Boolean) webSiteAvailability.get(e.nextElement())).booleanValue())
                availableCount++;
        }

        /* If there exist available web sites, then before adding them to the
         * SAVE AS and LOAD menus, add a menu separator.
         */
        if (availableCount != 0) {
            if (menuPanel.microworldLoad.getItemCount() == 2) {
                menuPanel.microworldLoad.addSeparator();
                menuPanel.microworldSaveAs.addSeparator();
            }
        }

        Enumeration enumeration = webSites.keys();
        String webSiteName, address;
        boolean available;
        while (enumeration.hasMoreElements()) {
            webSiteName = (String) enumeration.nextElement();
            address = (String) webSites.get(webSiteName);
            available = ((Boolean) webSiteAvailability.get(webSiteName)).booleanValue();
            if (available) {
                // Add the web site to the LOAD menu
                JMenuItem mi = (JMenuItem) menuPanel.microworldLoad.add(new JMenuItem(webSiteName));

                mi.addActionListener(new WebSiteAction(address) {
                    public void actionPerformed(ActionEvent e) {
                        WebFileDialog webFileDialog = webServerMicrosHandle.openWebFileDialog(this.address);
                        if (webFileDialog != null) {
                            webFileDialog.setDialogTitle(containerBundle.getString("ContainerMsg8"));
                            int whichButtonPressed=webFileDialog.showOpenDialog(parentComponent);
                            if ((webFileDialog.getSelectedFile() != null)&&(whichButtonPressed!=webFileDialog.CANCEL_OPTION)) {
//                                setLoadingMwd(true);
                                try {
                                    if (webFileDialog.isRemoteFile()) {
                                        updatingHistoryFlag=true;
                                        if (!webServerMicrosHandle.openRemoteMicroWorld(webFileDialog.getWebSite(),webFileDialog.getWebFile(),true))
                                            ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg12"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                                      updatingHistoryFlag=false;
                                    }else{
                                        if (load(webFileDialog.getSelectedFile().toString(), true, true, 3000)) {
                                            openFileRemote = false;
                                            container.webServerMicrosHandle.webSite = null;
                                            currentlyOpenMwdFileName = webFileDialog.getSelectedFile().toString();
                                            if (!microworld.microworldNameUserDefined)
                                                setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                                            if (!soundListenerMap.containsListenerFor(MicroworldListener.class, "microworldLoaded"))
                                                playSystemSound(SoundTheme.MWD_OPENED_SOUND);
                                        }else;
                                    }
                                } finally {
//                                    setLoadingMwd(false);
                                }
                            }
                        }
                    }
                });

                // Add the web site to the SAVE AS menu
                mi = (JMenuItem) menuPanel.microworldSaveAs.add(new JMenuItem(webSiteName));

                mi.addActionListener(new WebSiteAction(address) {
                    public void actionPerformed(ActionEvent e) {
                        WebFileDialog webFileDialog = webServerMicrosHandle.openWebFileDialog(this.address);
                        if (webFileDialog != null) {
                            webFileDialog.setDialogTitle(containerBundle.getString("ContainerMsg7"));
                        int whichButtonPressed=webFileDialog.showSaveDialog(container.parentComponent);
                        if ((webFileDialog.getSelectedFile() != null)&&(whichButtonPressed!=webFileDialog.CANCEL_OPTION)) {
                                if (webFileDialog.isRemoteFile()) {
                                    String serverFileName;
                                    try{
                                        serverFileName = webFileDialog.getSelectedFile().getCanonicalPath();
                                    }catch (IOException exc) {
                                        serverFileName = webFileDialog.getSelectedFile().toString();
                                    }
                                    String webFile = webServerMicrosHandle.getWebDirNameFromRoot(serverFileName, webFileDialog.getWebSiteMirror());
                                    if (!webServerMicrosHandle.saveFileToServer(webFileDialog.getWebSite(), webFile))
                                        ESlateOptionPane.showMessageDialog(parentComponent, containerBundle.getString("ContainerMsg11"), containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                                }else{
                                    if (saveAs(webFileDialog.getSelectedFile().toString(), true)) {
                                        currentlyOpenMwdFileName = webFileDialog.getSelectedFile().toString();
                                        if (!microworld.microworldNameUserDefined)
                                            setContainerTitle(ESlateContainerUtils.getFileNameFromPath(currentlyOpenMwdFileName, true));
                                        openFileRemote = false;
                                        webServerMicrosHandle.webSite = null;
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /* Removes the web site menu items from the LOAD and SAVE menus.
     */
    final void removeWebSiteMenuItems() {
        if (menuPanel == null) return;
        int itemCount = menuPanel.microworldLoad.getItemCount();
        for (int i=itemCount-1; i>=2; i--) {
            menuPanel.microworldLoad.remove(i);
        }

        itemCount = menuPanel.microworldSaveAs.getItemCount();
        for (int i=itemCount-1; i>=2; i--) {
            menuPanel.microworldSaveAs.remove(i);
        }
    }

	private void constructMenuPanel() {
		//Thread.currentThread().dumpStack();
        if (menuPanel != null) return;
        menuPanel = new MenuPanel(this);
//System.out.println("constructMenuPanel()");
        microworldList.addMicroworldReopenPopupMenuListener();
        menuPanel.updateLookFeelMenu(installedLAFs);
        menuPanel.toolsJavaConsoleEnabled.setSelected(javaConsoleEnabled);
        microworldList.populateMicroworldReopenMenu();
        createWebSiteMenuItems();
        String[] compoNames = mwdComponents.getComponentNames();
        for (int i=0; i<compoNames.length; i++)
            addComponentItemToComponentsMenu(compoNames[i]);
        for (int i=0; i<mwdComponents.size(); i++)
            addComponentItemToHelpMenu(mwdComponents.components.get(i).handle);

        menuPanel.microworldViewMenu.removeAll();
        for (int i=0; i<mwdViews.viewList.length; i++)
            menuPanel.microworldViewMenu.addItem(mwdViews.viewList[i].viewName);
    }

    public final void setMenuBarVisible(boolean visible) {
    	System.out.println("DBG setMenuBarVisible: "+visible);
    	//Thread.currentThread().dumpStack();
//        boolean menuVisible = (menuPanel.getParent() != null);

        /* When the microworld is locked, no-one has access to this setting.
         */
        if (microworld != null)
            microworld.checkSettingChangePriviledge();
        setMenuBarVisibleInternal(visible);
    }
    final void setMenuBarVisibleInternal(boolean visible) {
    	System.out.println("DBG: setMenuBarVisibleInternal started");
        //if (visible == menuBarVisible) return;
//System.out.pr���intln("setMenuBarVisible(" + visible + ") called getMicroworldVisibleAreaSize(): " + getMicroworldVisibleAreaSize());
//        System.out.println("setMenuBarVisible()");
        constructMenuPanel();
        menuBarVisible = visible;
        if (currentView != null)
            currentView.menuBarVisible = visible;
        if (!visible)
            northPanel.remove(menuPanel);
        else
            northPanel.add(menuPanel, 0);
        synchronized(getTreeLock()) {
            validateTree();
        }

//        System.out.println("called setMenuBarVisible(" + visible + ")");
//System.out.println("Done. setMenuBarVisible(" + visible + ") called getMicroworldVisibleAreaSize(): " + getMicroworldVisibleAreaSize());
        if (microworld != null)
            setMicroworldChanged(true);
    }
    public final boolean isMenuBarVisible() {
        return menuBarVisible;
//        return (menuPanel.getParent() != null);
    }

    public ESlateComposerUtils getESlateComposerUtilities() {
        return composerUtils;
    }

    /**
     * Starts the ESlate's design-time environment.
     * @param args
     * @see ESlateContainer#startESlate(String[], boolean)
     */
    public static void main(String[] args) {
        startESlate(args, false);
    }
}

class ViewNavigationListener implements ActionListener {
    int pos = -1;
    ESlateComposer composer;

    public ViewNavigationListener(ESlateComposer composer, int pos) {
        if (pos == 0)
            this.pos = 10;
        else
            this.pos = pos;

        this.composer = composer;
    }

    public void actionPerformed(ActionEvent e) {
        if (composer.menuPanel == null) return;
        ViewMenu viewMenu = composer.menuPanel.microworldViewMenu;
        if (viewMenu.getItemCount() < pos) return;
        viewMenu.getItem(pos-1).doClick();
    }
}
