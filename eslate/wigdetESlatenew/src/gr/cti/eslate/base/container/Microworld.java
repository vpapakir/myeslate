package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ActivationHandleGroup;
import gr.cti.eslate.base.ActiveHandleEvent;
import gr.cti.eslate.base.ActiveHandleListener;
import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.base.ESlateMicroworld;
import gr.cti.eslate.base.NameUsedException;
import gr.cti.eslate.base.RenamingForbiddenException;
import gr.cti.eslate.base.container.event.MicroworldAdapter;
import gr.cti.eslate.base.container.event.MicroworldComponentEvent;
import gr.cti.eslate.base.container.event.MicroworldEvent;
import gr.cti.eslate.base.container.event.MicroworldListener;
import gr.cti.eslate.base.container.event.MicroworldViewEvent;
import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.StructFile;
import gr.cti.typeArray.IntBaseArray;

import java.awt.Color;
import java.awt.Font;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.objectspace.jgl.Array;

/**
 * Title: Microworld
 * Description:  This class encapsulates the ESlateMicroworld that is currently
 *               running inside E-Slate.
 * @author George Tsironis
 * @version 1.0
 */

public final class Microworld {
    public static final int FORMAT_VERSION = 1;
    static final long serialVersionUID = 12;

    /** The category id for Geography microworlds.*/
    public static final int GEOGRAPHY = 1;
    /** The category id for Geometry microworlds.*/
    public static final int GEOMETRY = 2;
    /** The category id for History microworlds.*/
    public static final int HISTORY = 3;
    /** The category id for Mathematics microworlds.*/
    public static final int MATHEMATICS = 4;
    /** The category id for Physics microworlds.*/
    public static final int PHYSICS = 5;
    /** The category id for cross-subject microworlds.*/
    public static final int CROSS_SUBJECT = 6;
    /** The category id for microworlds with user-defined categories.*/
    public static final int CUSTOM_CATEGORY = 7;

    ESlateMicroworld eslateMwd = null;
    ESlateContainer container = null;
    private transient ArrayList mwdListeners = new ArrayList();
    private int listenerCount = 0;
    /** The message that is displayed at the top of the dialog which reports the
     *  progress of microworld loading.
     */
    private String mwdLoadProgressMsg = null;
    /** Flag that determines if the microworld load progress dialog will display
     *  information regarding the loading process, like which component is being loaded.
     */
    private boolean mwdLoadProgressInfoDisplayed = true;
    /** The message that is displayed at the top of the dialog which reports the
     *  progress of microworld saving.
     */
    private String mwdSaveProgressMsg = null;
    /** Flag that determines if the microworld save progress dialog will display
     *  information regarding the storage process, like which component is being saved.
     */
    private boolean mwdSaveProgressInfoDisplayed = true;
    /** The font of title the of the progress dialog for load and save */
    private Font progressDialogTitleFont = null;
    /** The foreground color of the title of the progress dialog for load and save */
    private Color progressDialogTitleColor = new Color(0, 0, 128);
    /* Flag that signals that a call to a priviledged API is internal and not external.
     * This flag guarantees that E-Slate itself and other trusted entities, like the
     * microworld scripts, will have access to the priviledged API, when this access is
     * blocked by the microworld properties.
     */
    private boolean internalCall = false;
    private final String microworldSecurityKey = createUniqueId(-5);

    /** Used to disable firing microworld events while a microworld is closing. Events
     *  about components being deactivated or closed should not be send while the
     *  microworld is closing. Microworld close supercedes and implies the rest. This
     *  flag is set just after the microworldClosing event is sent.
     */
    private boolean listenersDisabled = false;
    /** Property that determines whether the 4 SkinPanes of the ESlateInternalFrame
     *  will be stored/restored on a view basis, or they will be stored/restored only
     *  when the microworld is saved/loaded.
     */
/**/    boolean storeSkinOnAPerViewBasis = false;
    /** Property that determines whether the microworld name is automatically determined by the
     *  file name of the microworld (add cannot be manipulated by the end-user), or is user-defined.
     */
/**/    boolean microworldNameUserDefined = true;
    /** Property that enables/disables the creation of new views in the microworld.
     */
/**/    boolean viewCreationAllowed = true;
    /** Property that enables/disables the removal of views from the microworld.
     */
/**/    boolean viewRemovalAllowed = true;
    /** Property that enables/disables the change of the names of the views of the microworld.
     */
/**/    boolean viewRenameAllowed = true;
    /** Property that enables/disables the activation of views in the microworld.
     */
/**/    boolean viewActivationAllowed = true;
    /** Property that enables/disables the use of the View Manager dialog.
     */
/**/    boolean viewMgmtAllowed = true;
    /** Property that enables/disables the use of the ESlate Oprions dialog.
     */
/**/    boolean eslateOptionMgmtAllowed = true;
    /** Property that enables/disables the use of the Property Editor to edit
     *  component properties in the microworld.
     */
/**/    boolean componentPropertyMgmtAllowed = true;
    /** Property which enables/disables the use of the Event Editor to edit the
     *  scripts attached to the events of the microworld's components.
     */
/**/    boolean componentEventMgmtAllowed = true;
    /** Property that enables/disables the editor for the sounds attached to the
     *  microworld's components events.
     */
/**/    boolean componentSoundMgmtAllowed = true;
    /** Property that enables/disables the change of the LAF of the microworld.
     */
/**/    boolean mwdLAFChangeAllowed = true;
    /** Property that enables/disables the display of ESlate's consoles.
     */
/**/    boolean consolesAllowed = true;
    /** Property that enables/disables the right-click pop-up menu of the eslate component bar.
     */
/**/    boolean eslateComponentBarMenuEnabled = true;
    /** Property that enables/disables the appearance of the eslate's component bar.
     */
/**/    boolean eslateComponentBarEnabled = true;
    /** Property that enables/disables microworld renaming.
     */
/**/    boolean mwdNameChangeAllowed = true;
    /** Property that enables/disables component renaming in the microworld.
     */
/**/    boolean componentNameChangeAllowed = true;
    /** Property that enables/disables the right-click pop-up menu on the menu bars of the
     *  components.
     */
/**/    boolean componentBarMenuEnabled = true;
    /** Property which enables/disables the microworld Layer Management dialog and the
     *  change of the layers of the components.
     */
/**/    boolean mwdLayerMgmtAllowed = true;
    /** Property which enables/disables the grid management dialog of the E-Slate desktop.
     */
/**/    boolean gridMgmtAllowed = true;
    /** Property which enables/disables the creation of new components in the microworld.
     */
/**/    boolean componentInstantiationAllowed = true;
    /** Property which enables/disables the removal of components from the microworld.
     */
/**/    boolean componentRemovalAllowed = true;
    /** Property which enables/disables microworld saving.
     */
/**/    boolean mwdStorageAllowed = true;
    /** Property that enables/disables the right-click pop-up menu on the ESlate desktop.
     */
/**/    boolean mwdPopupEnabled = true;
    /** Property which enables/disables the E-Slate Performance framework.
     */
//    boolean performanceMgrActive = false;
    /** This property may have a value for these microworlds that demand a certain L&F.
     *  These microworlds, when loaded apply the L&F they need to the whole E-Slate. This
     *  happens if the L&F is available, i.e. it's jar exists.
     */
    String microworldLAFClassName = null;
    /** Flag that marks a microworld as locked or not. The initial value is false, so whenever a
     *  microworld is restored, no locking occurs while loading. The locked state of the microworld
     *  is restored after loading has finished.
     */
    private boolean isLocked = false;
    /** The password of the microworld.
     */
    private String password = null;
    /** Flag that marks the Microworld as 'dirty',  i.e. one or more attributes changed.
     */
    boolean modified = false;
    /** The metadata of the microworld.
     */
    private MicroworldMetadata metadata = new MicroworldMetadata();
    static ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MicroworldBundle", Locale.getDefault());
    static ResourceBundle mwdPropertiesDialogBundle = null;
    /** Flag which informs if this microworld's file has been backed-up, during a Save operation.
     *  Back-up is supported only in Save operations and not Save As operations, since Save As is
     *  a kind of back-up.
     */
    boolean backupExists = false;
    boolean mwdAutoBackupEnabled = true;
    /** This is the handle activation group which contains all the components of the microworld
     *  at any depth.
     */
    ActivationHandleGroup globalHandleGroup = null;
    boolean discardComponentActivationEvent = false;
	/** The icon of the microworld. This is displayed at the top-left corner of the microworld's window
	 */
	NewRestorableImageIcon microworldIcon = null;


    public Microworld(ESlateMicroworld mwd, ESlateContainer eSlateContainer) {
        eslateMwd = mwd;
        createGlobalHandleActivationGroup(eslateMwd.getESlateHandle());
        metadata.setTitle(eslateMwd.getName());
        this.container = eSlateContainer;
        progressDialogTitleFont = UIManager.getFont("Label.font");
        progressDialogTitleFont = progressDialogTitleFont.deriveFont(Font.BOLD);

        addMicroworldListener(new MicroworldAdapter() {
            public void componentActivated(MicroworldComponentEvent e) {
                /* Don't call updateBeanInfoPanel(), for ActiveComponentChangedEvents which
                 * are delivered while a microworld is being loaded.
                 */
                if (container.loadingMwd) return;
                if (container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) container;
                    if (composer.propertyEventEditor == null) return;
                    if (composer.propertyEventEditor.ignoreActivation) return;
                    discardComponentActivationEvent = false;
    //System.out.println("MICROWORLD COMPONENT ACTIVATION LISTENER component: " + e.getComponent());
                    SwingUtilities.invokeLater(new Runnable() {
                          public void run() {
                              if (discardComponentActivationEvent) return;
                              ((ESlateComposer) Microworld.this.container).updateBeanInfoPanel();
                          }
                    });
                }
            }
            public void componentClosed(MicroworldComponentEvent e) {
                if (container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) container;
                    int componentCount = container.getTopLevelComponentCount();
                    ESlateComponent removedComponent = e.getComponent();
                    /* Update the object hierarchy tree in the propertyEventEditor.
                     */
                    if (composer.propertyEventEditor != null) {
                        composer.propertyEventEditor.removeSecondLevelNode(removedComponent.object);
                        if (componentCount == 0)
                            composer.propertyEventEditor.selectTopLevelNode();
                    }
                }
            }
            public void microworldClosed(MicroworldEvent e) {
                if (container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) container;
                    /* Disable the ESlateContainer's propertyEventEditor.
                     */
                    if (composer.propertyEventEditor != null) {
                        composer.propertyEventEditor.removeAllNodes();
                        composer.propertyEventEditor.setEnabled(false);
                    }
                }
            }
            public void microworldCreated(MicroworldEvent e) {
                if (container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) container;
                    /* Initialize the ESlateComposer's propertyEventEditor with the new microworld.
                     * This happens only when a new, empty microworld is created. If a microworld is
                     * loaded, the microworldLoaded() will initialize the propertyEventEditor, after
                     * the scripts which may have been attatched to the microworld are loaded.
                     */
                    if (composer.propertyEventEditor != null && !container.loadingMwd) {
                        composer.propertyEventEditor.initializeHierarchyBox(e.getMicroworld());
                    }
                }
            }
            public void microworldLoaded(MicroworldEvent e) {
                if (container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) container;
                    /* Initialize the ESlateContainer's propertyEventEditor with the new microworld.
                     */
                    if (composer.propertyEventEditor != null) {
                        composer.propertyEventEditor.initializeHierarchyBox(e.getMicroworld());
                        composer.updatePropertyEventEditorTree();
                        composer.updateBeanInfoPanel();
                    }
                }
            }
        });

    }

    public ESlateMicroworld getESlateMicroworld() {
        return eslateMwd;
    }

    public synchronized void addMicroworldListener(MicroworldListener mwdlistener) {
        if (mwdListeners.indexOf(mwdlistener) == -1) {
            mwdListeners.add(mwdlistener);
            listenerCount++;
        }
//System.out.println("addMicroworldListener() called listenerCount: " + listenerCount);
    }

    public synchronized void removeMicroworldListener(MicroworldListener mwdlistener) {
        int index = mwdListeners.indexOf(mwdlistener);
        if (index != -1) {
            mwdListeners.remove(index);
            listenerCount--;
        }
    }

    void removeMicroworldListeners() {
        mwdListeners.clear();
        listenerCount = 0;
    }

    void disableMicroworldListeners() {
        listenersDisabled = true;
    }

    protected void fireMicroworldCreated(MicroworldEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).microworldCreated(e);
    }

    protected void fireMicroworldLoaded(MicroworldEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).microworldLoaded(e);
    }

    protected void fireMicroworldClosed(MicroworldEvent e) {
        if (listenerCount == 0) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).microworldClosed(e);
    }

    protected void fireMicroworldClosing(MicroworldEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).microworldClosing(e);
    }

    protected void fireComponentActivated(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentActivated(e);
    }

    protected void fireComponentDeactivated(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentDeactivated(e);
    }

    protected void fireComponentIconified(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentIconified(e);
    }

    protected void fireComponentRestored(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentRestored(e);
    }

    protected void fireComponentMaximized(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentMaximized(e);
    }

    protected void fireComponentClosing(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentClosing(e);
    }

    protected void fireComponentClosed(MicroworldComponentEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).componentClosed(e);
    }

    protected void fireActiveViewChanged(MicroworldViewEvent e) {
        if (listenerCount == 0 || listenersDisabled) return;
        ArrayList listeners;
        synchronized(this) {listeners = (ArrayList) mwdListeners.clone();}
        for (int i=0; i<listeners.size(); i++)
            ((MicroworldListener) listeners.get(i)).activeViewChanged(e);
    }

    /** Sets the message that is displayed at the top of the dialog which reports the
     *  progress of microworld loading. If the message is null, then the default message
     *  appears.
     */
    public void setMwdLoadProgressMsg(String msg) {
        mwdLoadProgressMsg = msg;
    }

    public String getMwdLoadProgressMsg() {
        return mwdLoadProgressMsg;
    }

    /** Determines whether the microworld load progress dialog will display
     *  information regarding the loading process, like which component is being loaded.
     */
    public void setMwdLoadProgressInfoDisplayed(boolean displayed) {
        mwdLoadProgressInfoDisplayed = displayed;
    }

    public boolean isMwdLoadProgressInfoDisplayed() {
        return mwdLoadProgressInfoDisplayed;
    }

    /** Sets the message that is displayed at the top of the dialog which reports the
     *  progress of microworld saving. If the message is null, then the default message
     *  appears.
     */
    public void setMwdSaveProgressMsg(String msg) {
        mwdSaveProgressMsg = msg;
    }

    public String getMwdSaveProgressMsg() {
        return mwdSaveProgressMsg;
    }

    /** Determines whether the microworld save progress dialog will display
     *  information regarding the storage process, like which component is being saved.
     */
    public void setMwdSaveProgressInfoDisplayed(boolean displayed) {
        mwdSaveProgressInfoDisplayed = displayed;
    }

    public boolean isMwdSaveProgressInfoDisplayed() {
        return mwdSaveProgressInfoDisplayed;
    }

    /** Sets the font of the progress dialog's title, for microworld loading and saving.
     */
    public void setProgressDialogTitleFont(Font f) {
        progressDialogTitleFont = f;
    }

    public Font getProgressDialogTitleFont() {
        return progressDialogTitleFont;
    }

    /** Sets the foreground color of the progress dialog's title, for microworld loading and saving.
     */
    public void setProgressDialogTitleColor(Color foreground) {
        progressDialogTitleColor = foreground;
    }

    public Color getProgressDialogTitleColor() {
        return progressDialogTitleColor;
    }

    public void setMicroworldLAFClassName(String className) {
//System.out.println("setMicroworldLAFClassName(" + className + ") --- microworldLAFClassName: " + microworldLAFClassName);
        if (className != null && className.trim().length() == 0)
            className = null;
        if (microworldLAFClassName != null && microworldLAFClassName.equals(className))
            return;
        if (microworldLAFClassName == null && className == null)
            return;
        if (className == null && microworldLAFClassName != null) {
            microworldLAFClassName = null;
            return;
        }
        Class lafClass = null;
        try{
            lafClass = Class.forName(className);
        }catch (Throwable thr) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, container.containerBundle.getString("ContainerMsg66") + '\"' + className + "\"", container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        if (lafClass != null) {
            /* If the current L&F of the E-Slate is not the specified one, then try to change it.
             * If the change is successful, the this will be the microworld's L&F. If the current
             * L&F is the specified one, then just assign it to the microworld.
             */
            if (!className.equals(UIManager.getLookAndFeel().getClass().getName())) {
                if (container.setESlateLAF(lafClass, false)) {
                    microworldLAFClassName = className;
                }
            }else
                microworldLAFClassName = className;
        }
    }

    public String getMicroworldLAFClassName() {
        return microworldLAFClassName;
    }

    public StorageStructure getMicroworldProperties() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(FORMAT_VERSION, 5);
        fieldMap.put("L&F", microworldLAFClassName);
        fieldMap.put("LoadMsg", mwdLoadProgressMsg);
        fieldMap.put("LoadProgress", mwdLoadProgressInfoDisplayed);
        fieldMap.put("SaveMsg", mwdSaveProgressMsg);
        fieldMap.put("SaveProgress", mwdSaveProgressInfoDisplayed);
        fieldMap.put("TitleFont", progressDialogTitleFont);
        fieldMap.put("TitleColor", progressDialogTitleColor);
        fieldMap.put("TitleColor", progressDialogTitleColor);

        fieldMap.put("StoreSkins", storeSkinOnAPerViewBasis);
        fieldMap.put("ViewCreationAllowed", viewCreationAllowed);
        fieldMap.put("ViewRemovalAllowed", viewRemovalAllowed);
        fieldMap.put("ViewRenameAllowed", viewRenameAllowed);
        fieldMap.put("ViewActivationAllowed", viewActivationAllowed);
        fieldMap.put("ViewMgmtAllowed", viewMgmtAllowed);
        fieldMap.put("ESlateOptionMgmtAllowed", eslateOptionMgmtAllowed);
        fieldMap.put("ComponentPropertyMgmtAllowed", componentPropertyMgmtAllowed);
        fieldMap.put("ComponentEventMgmtAllowed", componentEventMgmtAllowed);
        fieldMap.put("ComponentSoundMgmtAllowed", componentSoundMgmtAllowed);
        fieldMap.put("MwdLAFChangeAllowed", mwdLAFChangeAllowed);
        fieldMap.put("ConsolesAllowed", consolesAllowed);
        fieldMap.put("ESlateComponentBarMenuEnabled", eslateComponentBarMenuEnabled);
        fieldMap.put("ESlateComponentBarEnabled", eslateComponentBarEnabled);
        fieldMap.put("MwdNameChangeAllowed", mwdNameChangeAllowed);
        fieldMap.put("ComponentNameChangeAllowed", componentNameChangeAllowed);
        fieldMap.put("ComponentBarMenuEnabled", componentBarMenuEnabled);
        fieldMap.put("MwdLayerMgmtAllowed", mwdLayerMgmtAllowed);
        fieldMap.put("GridMgmtAllowed", gridMgmtAllowed);
        fieldMap.put("ComponentInstantiationAllowed", componentInstantiationAllowed);
        fieldMap.put("ComponentRemovalAllowed", componentRemovalAllowed);
        fieldMap.put("MwdStorageAllowed", mwdStorageAllowed);
        fieldMap.put("MwdAutoBackupEnabled", mwdAutoBackupEnabled);
        fieldMap.put("MwdPopupEnabled", mwdPopupEnabled);
        fieldMap.put("MicroworldNameUserDefined", microworldNameUserDefined);
        fieldMap.put("PerformanceMgrActive", PerformanceManager.getPerformanceManager().isEnabled());

        /* The password is save in String format. This is not safe. In Java 1.4
         * the Chipher, ChipherInputStream and CipherOutputStream classes will be
         * used to encrypt the password before saving it.
         */
        fieldMap.put("Subtitle", password);
		fieldMap.put("Microworld Icon", microworldIcon);

        return fieldMap;
    }

    public void applyMicroworldProperties(StorageStructure fieldMap) {
        setMicroworldLAFClassName(fieldMap.get("L&F", (String)null));
        setMwdLoadProgressMsg(fieldMap.get("LoadMsg", (String)null));
        setMwdLoadProgressInfoDisplayed(fieldMap.get("LoadProgress", true));
        setMwdSaveProgressMsg(fieldMap.get("SaveMsg", (String)null));
        setMwdSaveProgressInfoDisplayed(fieldMap.get("SaveProgress", true));
        Font f = (Font) fieldMap.get("TitleFont", (Font)null);
        Color color = fieldMap.get("TitleColor", (Color)null);
        if (f != null)
            setProgressDialogTitleFont(f);
        if (color != null)
            setProgressDialogTitleColor(color);
        password = (String) fieldMap.get("Subtitle");
//System.out.println("password: " + password);
        storeSkinOnAPerViewBasis = fieldMap.get("StoreSkins", false);
        viewCreationAllowed = fieldMap.get("ViewCreationAllowed", true);
        viewRemovalAllowed = fieldMap.get("ViewRemovalAllowed", true);
        viewRenameAllowed = fieldMap.get("ViewRenameAllowed", true);
        viewActivationAllowed = fieldMap.get("ViewActivationAllowed", true);
        viewMgmtAllowed = fieldMap.get("ViewMgmtAllowed", true);
        eslateOptionMgmtAllowed = fieldMap.get("ESlateOptionMgmtAllowed", true);
        setComponentPropertyMgmtAllowed(fieldMap.get("ComponentPropertyMgmtAllowed", true));
        setComponentEventMgmtAllowed(fieldMap.get("ComponentEventMgmtAllowed", true));
        setComponentSoundMgmtAllowed(fieldMap.get("ComponentSoundMgmtAllowed", true));
        mwdLAFChangeAllowed = fieldMap.get("MwdLAFChangeAllowed", true);
        setConsolesAllowed(fieldMap.get("ConsolesAllowed", true));
        setEslateComponentBarMenuEnabled(fieldMap.get("ESlateComponentBarMenuEnabled", true));
        setESlateComponentBarEnabled(fieldMap.get("ESlateComponentBarEnabled", true));
        setMwdNameChangeAllowed(fieldMap.get("MwdNameChangeAllowed", true));
        setComponentNameChangeAllowed(fieldMap.get("ComponentNameChangeAllowed", true));
        componentBarMenuEnabled = fieldMap.get("ComponentBarMenuEnabled", true);
        mwdLayerMgmtAllowed = fieldMap.get("MwdLayerMgmtAllowed", true);
        gridMgmtAllowed = fieldMap.get("GridMgmtAllowed", true);
        componentInstantiationAllowed = fieldMap.get("ComponentInstantiationAllowed", true);
        setComponentRemovalAllowed(fieldMap.get("ComponentRemovalAllowed", true));
        mwdStorageAllowed = fieldMap.get("MwdStorageAllowed", true);
        mwdAutoBackupEnabled = fieldMap.get("MwdAutoBackupEnabled", true);
        mwdPopupEnabled = fieldMap.get("MwdPopupEnabled", true);
        microworldNameUserDefined = fieldMap.get("MicroworldNameUserDefined", true);
        setPerformanceMgrActiveInternal(fieldMap.get("PerformanceMgrActive", false));
        NewRestorableImageIcon mic=(NewRestorableImageIcon) fieldMap.get("Microworld Icon");
        if (mic!=null) {
        	setMicroworldIcon(mic);
        } else {
        	// container.parentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        }
    }

    /** This method sets the 'locked' state of the microworld after microworld
     *  loading has finished. The locked state of the microworld cannot be determined
     *  earlier, because the microworld may get locked before its properties are
     *  loaded and set.
     */
    void updateLockedStatusAfterMwdLoading() {
        if (password != null && password.trim().length() != 0)
            isLocked = true;
        else
            isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    void setLocked(boolean locked) {
        if (this.isLocked == locked) return;
        this.isLocked = locked;
        // Code to set the ESlateMicroworld in locked state.
    }

    /** Sets the title of the microworld. Returns true if the title was set successfully.
     *  Returns false if the new title is the same or if the new title is rejected.
     */
    public boolean setTitle(String title) {
        String _title = metadata.getTitle();
        if (_title != null && _title.equals(title)) return false;
        if (_title == null && title == null) return false;
        try{
//System.out.println("eslateMwd.setName(): " + title);
            eslateMwd.setName(title);
            metadata.setTitle(title);
            container.setContainerTitle(title);
//System.out.println("eslateMwd.getName(): " + eslateMwd.getName());
            modified = true;
        }catch (NameUsedException exc) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, exc.getMessage(), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }catch (RenamingForbiddenException exc) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, exc.getMessage(), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }catch (Throwable exc) {
            ESlateOptionPane.showMessageDialog(container.parentComponent, exc.getMessage(), container.containerBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    /** Returns the title of the microworld.
     */
    public String getTitle() {
        return eslateMwd.getName();
    }

    /** Sets the subject of the microworld.
     */
    public void setSubject(String subject) {
        String _subject = metadata.getSubject();
        if (_subject != null && _subject.equals(subject)) return;
        if (_subject == null && subject == null) return;
        metadata.setSubject(subject);
        modified = true;
    }
    /** Returns the subject of the microworld.
     */
    public String getSubject() {
        return metadata.getSubject();
    }

    /** Sets the company that owns of the microworld.
     */
    public void setCompany(String company) {
        String _company = metadata.getCompany();
        if (_company != null && _company.equals(company)) return;
        if (_company == null && company == null) return;
        metadata.setCompany(company);
        modified = true;
    }
    /** Returns the company that owns the microworld.
     */
    public String getCompany() {
        return metadata.getCompany();
    }

    /** Sets the keyworrds for this microworld. The keywords are given in the form of
     *  a simple String, containings any number of keywords, separated in some way.
     */
    public void setKeywords(String keywords) {
        String _keywords = metadata.getKeywords();
        if (_keywords != null && _keywords.equals(keywords)) return;
        if (_keywords == null && keywords == null) return;
        metadata.setKeywords(keywords);
        modified = true;
    }
    /** Returns the keywords for the microworld.
     */
    public String getKeywords() {
        return metadata.getKeywords();
    }

    /** Sets the authors of the microworld.
     */
    public void setAuthors(MicroworldAuthor[] authors) {
/*        if (authorInfo.length != 3) throw new IllegalArgumentException("The ArrayList array with the author info must contain exactly 3 ArrayLists");
        ArrayList a1 = authorInfo[0];
        ArrayList a2 = authorInfo[1];
        ArrayList a3 = authorInfo[2];
        int size = a1.size();
        if (size != a2.size() || size != a3.size())
            throw new IllegalArgumentException("The ArrayLists with the author info do not match in size");
        for (int i=0; i<size; i++)
            if (a2[i] == null)
                throw new IllegalArgumentException("The ArrayList with the author surnames contains null elements");
*/
        if (metadata.getAuthors() == authors) return;
        metadata.setAuthors(authors);
        modified = true;
    }
    /** Returns the authors of the microworld.
     */
    public MicroworldAuthor[] getAuthors() {
        return metadata.getAuthors();
    }

    /** Sets the category of the microworld. There are some predefined categories,
     *  like 'Geography', 'Mathematics',... but custom category names are allowed too.
     *  See category identifiers in Microworld class.
     */
    public void setCategory(int category) {
        int _category = metadata.getCategory();
        if (category == _category) return;
        if (category < GEOGRAPHY || category > CUSTOM_CATEGORY)
            throw new IllegalArgumentException("Invalid category id: " + category);
        metadata.setCategory(category);
        modified = true;
    }
    /** Returns the category id of the microworld.
     */
    public int getCategory() {
        return metadata.getCategory();
    }

    /** Sets the name of the category of the Microworld. If the category name is one of the
     *  predefined localized catefory names, then the 'category' of the microworld is also
     *  adjusted. If the category name is not among the known ones, then the category id
     *  of the microworld is set to CUSTOM_CATEGORY.
     */
    public void setCategoryName(String name) {
        String _categoryName = metadata.getCategoryName();
        if (_categoryName != null && _categoryName.equals(name)) return;
        if (_categoryName == null && name == null) return;
        metadata.setCategoryName(name);
        if (bundle.getString("Geography").equals(name))
            metadata.setCategory(GEOGRAPHY);
        else if (bundle.getString("Geometry").equals(name))
            metadata.setCategory(GEOMETRY);
        else if (bundle.getString("History").equals(name))
            metadata.setCategory(HISTORY);
        else if (bundle.getString("Mathematics").equals(name))
            metadata.setCategory(MATHEMATICS);
        else if (bundle.getString("Physics").equals(name))
            metadata.setCategory(PHYSICS);
        else if (bundle.getString("CrossSubject").equals(name))
            metadata.setCategory(CROSS_SUBJECT);
        else
            metadata.setCategory(CUSTOM_CATEGORY);

        modified = true;
    }

    /** Returns the category name of the microworld.
     */
    public String getCategoryName() {
        String c = getCategoryName(metadata.getCategory());
        if (c.equals("Custom"))
            return metadata.getCategoryName();
        return c;
    }

    /** Returns the category names for all the standard microworld categories, the
     *  Microworld class supports.
     *  @return The localized category name, or null if an invalid category id is given
     */
    public static final String getCategoryName(int category) {
        switch (category) {
            case GEOGRAPHY:
                return bundle.getString("Geography");
            case GEOMETRY:
                return bundle.getString("Geometry");
            case HISTORY:
                return bundle.getString("History");
            case MATHEMATICS:
                return bundle.getString("Mathematics");
            case PHYSICS:
                return bundle.getString("Physics");
            case CROSS_SUBJECT:
                return bundle.getString("CrossSubject");
            case CUSTOM_CATEGORY:
                return "Custom";
        }
        return null;
    }

    /** Sets the comments for the microworld.
     */
    public void setComments(String comments) {
        String _comments = metadata.getComments();
        if (_comments != null && _comments.equals(comments)) return;
        if (_comments == null && comments == null) return;
        metadata.setComments(comments);
        modified = true;
    }
    /** Returns the comments for the microworld.
     */
    public String getComments() {
        return metadata.getComments();
    }

    void saveMetadata(StructFile structFile, String entryName) {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(structFile.openOutputFile(entryName));
            metadata.writeExternal(oos);
            oos.flush();
            oos.close();
        }catch (Throwable thr) {
            System.out.println("Unable to save the microworld metadata");
            thr.printStackTrace();
        }
    }

    void loadMetadata(StructFile structFile, String entryName) {
        try{
            ObjectInputStream ois = new ObjectInputStream(structFile.openInputFile(entryName));
            metadata.readExternal(ois);
            ois.close();
            // Set the proper name of the ESlateMicroworld here. This was done by the
            // platform in its loadMicroworld() method. However with the ESR2
            // constructor, there are components which get instantiated and restored
            // before loadMicroworld() is called. If the components have part of their
            // state in private files in the struct file, then they can't access them
            // (if the microworld has a name other than the default one), cause the
            // name of the top-level dir is the microworld's name, which has yet been
            // restored. So we restore it here.
			boolean renamingAllowed = eslateMwd.isRenamingAllowed();
			if (!renamingAllowed) {
				eslateMwd.setRenamingAllowed(true, microworldSecurityKey);
			}
            eslateMwd.getESlateHandle().setComponentName(metadata.getTitle());
			if (!renamingAllowed) {
				eslateMwd.setRenamingAllowed(false, microworldSecurityKey);
			}
        }catch (Throwable thr) {
            System.out.println("Unable to load the microworld metadata");
            thr.printStackTrace();
        }
    }

    String getSubTitle() { //Returns the microworld's password
        return password;
    }

    void setSubTitle(String subtitle) {
        if (password != null && password.equals(subtitle)) return;
        if (password == null && subtitle == null) return;
        password = subtitle;
        modified = true;
    }

    /** Enables/disables the editing of the names of the components of the microworld.
     */
    public final void setComponentNameChangeAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        setComponentNameChangeAllowedInternal(allowed);
    }
    final void setComponentNameChangeAllowedInternal(boolean allowed) {
        if (componentNameChangeAllowed == allowed)
            return;
        try{
            eslateMwd.setRenamingAllowed(allowed, microworldSecurityKey);
            componentNameChangeAllowed = allowed;
//            System.out.println("microworldChanged 7");
            container.setMicroworldChanged(true);
        }catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public boolean isComponentNameChangeAllowed() {
        if (internalCall) return true;
        return componentNameChangeAllowed;
    }

    /** Determines whether new components can be added to the microworld.
     */
    public final void setComponentInstantiationAllowed(boolean componentInstantiationAllowed) {
        checkSettingChangePriviledge();
        setComponentInstantiationAllowedInternal(componentInstantiationAllowed);
    }
    final void setComponentInstantiationAllowedInternal(boolean componentInstantiationAllowed) {
        if (this.componentInstantiationAllowed != componentInstantiationAllowed) {
            this.componentInstantiationAllowed = componentInstantiationAllowed;
//            System.out.println("microworldChanged 8");
            container.setMicroworldChanged(true);
        }
    }
    public boolean isComponentInstantiationAllowed() {
        if (internalCall) return true;
        return componentInstantiationAllowed;
    }

    /** Determines whether component removal is allowed in the microworld.
     */
    public final void setComponentRemovalAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        setComponentRemovalAllowedInternal(allowed);
    }
    final void setComponentRemovalAllowedInternal(boolean allowed) {
        if (componentRemovalAllowed != allowed) {
            componentRemovalAllowed = allowed;
            //Disable the close buttons on the control bars
            if (!componentRemovalAllowed) {
                ESlateInternalFrame[] frames = container.mwdComponents.getComponentFrames();
                for (int i=0; i<frames.length; i++)
                    frames[i].setClosable(componentRemovalAllowed);
//0                for (int i=0; i<componentFrames.size(); i++)
//0                    ((ESlateInternalFrame) componentFrames.at(i)).setFrameClosable(currentView.componentRemovalAllowed);
            }
//            System.out.println("microworldChanged 9");
            container.setMicroworldChanged(true);
        }
    }
    public boolean isComponentRemovalAllowed() {
        if (internalCall) return true;
        return componentRemovalAllowed;
    }

    /** Enables or disables the right-click pop-up of the E-Slate desktop.
     */
    public final void setMwdPopupEnabled(boolean mwdPopupEnabled) {
        checkSettingChangePriviledge();
        setMwdPopupEnabledInternal(mwdPopupEnabled);
    }
    final void setMwdPopupEnabledInternal(boolean mwdPopupEnabled) {
        if (this.mwdPopupEnabled == mwdPopupEnabled) return;
        this.mwdPopupEnabled = mwdPopupEnabled;
        container.setMicroworldChanged(true);
    }
    public boolean isMwdPopupEnabled() {
        if (internalCall) return true;
        return mwdPopupEnabled;
    }

    /** Determines whether the central E-Slate component bar is enabled in this microworld.
     */
    public final void setESlateComponentBarEnabled(boolean enabled) {
        checkSettingChangePriviledge();
        setESlateComponentBarEnabledInternal(enabled);
    }
    final void setESlateComponentBarEnabledInternal(boolean enabled) {
        if (!(container instanceof ESlateComposer)) return;
        ESlateComposer composer = (ESlateComposer) container;
        if (eslateComponentBarEnabled == enabled || composer.menuPanel == null) return;
        eslateComponentBarEnabled = enabled;
        composer.setESlateComponentBarEnabled(eslateComponentBarEnabled);
        container.setMicroworldChanged(true);
    }

    public boolean isESlateComponentBarEnabled() {
        if (internalCall) return true;
        return eslateComponentBarEnabled;
    }

    /** Determines if the microworld can be stored.
     */
    public final void setMwdStorageAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        setMwdStorageAllowedInternal(allowed);
    }
    final void setMwdStorageAllowedInternal(boolean allowed) {
        if (mwdStorageAllowed != allowed) {
            mwdStorageAllowed = allowed;
            if (!mwdStorageAllowed) {
                internalCall = true;
                container.saveMicroworld(true);
                internalCall = false;
                container.setMicroworldChanged(false);
            }else{
                container.setMicroworldChanged(true);
//                System.out.println("microworldChanged 11");
            }
        }
    }
    public boolean isMwdStorageAllowed() {
        if (internalCall) return true;
        return mwdStorageAllowed;
    }

    /** Determines if the microworld can be stored.
     */
    public final void setMwdAutoBackupEnabled(boolean enabled) {
        checkSettingChangePriviledge();
        setMwdAutoBackupEnabledInternal(enabled);
    }
    final void setMwdAutoBackupEnabledInternal(boolean enabled) {
        if (mwdAutoBackupEnabled != enabled) {
            mwdAutoBackupEnabled = enabled;
            container.setMicroworldChanged(true);
        }
    }
    public boolean isMwdAutoBackupEnabled() {
        return mwdAutoBackupEnabled;
    }


    /** Adjusts whether the skins of the components will be stored for each
     *  view of the microworld. This setting is by default 'false', for
     *  performance reasons, so by default a component can only have one skin,
     *  for all the views of the microworld. Saving skins for each view
     *  degradates the view switching performance.
     */
    public final void setStoreSkinsPerView(boolean store) {
        checkSettingChangePriviledge();
        if (storeSkinOnAPerViewBasis == store) return;
        storeSkinOnAPerViewBasis = store;
        container.setMicroworldChanged(true);
    }

    public boolean isStoreSkinsPerView() {
        if (internalCall) return true;
        return storeSkinOnAPerViewBasis;
    }

    /** Adjusts whether the name of the microworld is user defined, or is automatically set to the
     *  name of the file which stores the microworld. In the latter case the user cannot set the
     *  microworld name.
     */
    public final void setMicroworldNameUserDefined(boolean userDefined) {
        checkSettingChangePriviledge();
        setMicroworldNameUserDefinedInternal(userDefined);
    }
    final void setMicroworldNameUserDefinedInternal(boolean userDefined) {
        if (microworldNameUserDefined == userDefined) return;
        microworldNameUserDefined = userDefined;
        if (userDefined)
            container.setContainerTitle(eslateMwd.getName());
        else{
            if (container.currentlyOpenMwdFileName != null)
                container.setContainerTitle(ESlateContainerUtils.getFileNameFromPath(container.currentlyOpenMwdFileName, true));
        }
        container.setMicroworldChanged(true);
    }
    public boolean isMicroworldNameUserDefined() {
        if (internalCall) return true;
        return microworldNameUserDefined;
    }

    /** Enables/Disables the E-Slate Performance Manager.
     */
    public final void setPerformanceMgrActive(boolean active) {
        checkSettingChangePriviledge();
        setPerformanceMgrActiveInternal(active);
    }
    final void setPerformanceMgrActiveInternal(boolean active) {
//System.out.println("setPerformanceMgrActive() active: " + active); // + ", performanceMgrActive: " + performanceMgrActive);
//        if (performanceMgrActive == active) return;
//        performanceMgrActive = active;
        PerformanceManager.getPerformanceManager().setEnabled(active);
//System.out.println("setPerformanceMgrActiveInternal() getPerformanceTimerGroup(ESlate): " + PerformanceManager.getPerformanceManager().getPerformanceTimerGroup(container.getESlateHandle()));
        container.setMicroworldChanged(true);
    }
    public boolean isPerformanceMgrActive() {
        return PerformanceManager.getPerformanceManager().isEnabled(); //performanceMgrActive;
    }

    /** Determines whether new views can be created in this microworld.
     */
    public final void setViewCreationAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (viewCreationAllowed == allowed) return;
        viewCreationAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public boolean isViewCreationAllowed() {
        if (internalCall) return true;
        return viewCreationAllowed;
    }

    /** Determines whether existing views can be removed from this microworld.
     */
    public final void setViewRemovalAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (viewRemovalAllowed == allowed) return;
        viewRemovalAllowed = allowed;
        container.setMicroworldChanged(true);
    }

    public boolean isViewRemovalAllowed() {
        if (internalCall) return true;
        return viewRemovalAllowed;
    }

    /** Determines whether view renaming is allowed in the microworld.
     */
    public final void setViewRenameAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (viewRenameAllowed == allowed) return;
        viewRenameAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public boolean isViewRenameAllowed() {
        if (internalCall) return true;
        return viewRenameAllowed;
    }

    /** Determines whether manual (through the E-Slate UI) view activation is allowed in
     *  the microworld.
     */
    public final void setViewActivationAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (viewActivationAllowed == allowed) return;
        viewActivationAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public boolean isViewActivationAllowed() {
        if (internalCall) return true;
        return viewActivationAllowed;
    }

    /** Determines whether view management through the E-Slate UI (corresponding dialog)
     *  is allowed in the microworld.
     */
    public final void setViewMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (viewMgmtAllowed == allowed) return;
        viewMgmtAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public final boolean isViewMgmtAllowed() {
        if (internalCall) return true;
        return viewMgmtAllowed;
    }

    /** Determines whether the E-Slate options dialog is enabled in this microworld.
     */
    public final void setEslateOptionMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (eslateOptionMgmtAllowed == allowed) return;
        eslateOptionMgmtAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public final boolean isEslateOptionMgmtAllowed() {
        if (internalCall) return true;
        return eslateOptionMgmtAllowed;
    }

    /** Determines whether component properties can be edited in this microworld with the
     *  aid of the E-Slate component editor.
     */
    public final void setComponentPropertyMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (componentPropertyMgmtAllowed == allowed) return;
        componentPropertyMgmtAllowed = allowed;
        if (container instanceof ESlateComposer) {
            ESlateComposer composer = (ESlateComposer) container;
            if (composer.propertyEventEditor != null)
                composer.propertyEventEditor.setPropertyEditorEnabledInternal(componentPropertyMgmtAllowed);
        }
        container.setMicroworldChanged(true);
    }
    public final boolean isComponentPropertyMgmtAllowed() {
        if (internalCall) return true;
        return componentPropertyMgmtAllowed;
    }

    /** Determines whether the events of the components of this microworld can be edited
     *  with the aid of the E-Slate component editor.
     */
    public final void setComponentEventMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (componentEventMgmtAllowed == allowed) return;
        componentEventMgmtAllowed = allowed;
        if (container instanceof ESlateComposer) {
            ESlateComposer composer = (ESlateComposer) container;
            if (composer.propertyEventEditor != null)
                composer.propertyEventEditor.setEventEditorEnabledInternal(componentEventMgmtAllowed);
        }
        container.setMicroworldChanged(true);
    }
    public final boolean isComponentEventMgmtAllowed() {
        if (internalCall) return true;
        return componentPropertyMgmtAllowed;
    }

    /** Determines whether the sounds attached to events of the components of this
     *  microworld can be edited with the aid of the E-Slate component editor.
     */
    public final void setComponentSoundMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (componentSoundMgmtAllowed == allowed) return;
        componentSoundMgmtAllowed = allowed;
        if (container instanceof ESlateComposer) {
            ESlateComposer composer = (ESlateComposer) container;
            if (composer.propertyEventEditor != null)
                composer.propertyEventEditor.setSoundEditorEnabledInternal(componentSoundMgmtAllowed);
        }
        container.setMicroworldChanged(true);
    }
    public final boolean isComponentSoundMgmtAllowed() {
        if (internalCall) return true;
        return componentPropertyMgmtAllowed;
    }

    /** Determines whether the Look and Feel of the microworld can be changed.
     */
    public final void setMwdLAFChangeAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (mwdLAFChangeAllowed == allowed) return;
        mwdLAFChangeAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public final boolean isMwdLAFChangeAllowed() {
        if (internalCall) return true;
        return mwdLAFChangeAllowed;
    }

    /** Determines whether the E-Slate consoles can be shown while this microworld
     *  is executing.
     */
    public final void setConsolesAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (consolesAllowed == allowed) return;
        consolesAllowed = allowed;
        if (!allowed)
            container.useStandardJavaOutput();
        container.setMicroworldChanged(true);
    }
    public final boolean isConsolesAllowed() {
        if (internalCall) return true;
        return consolesAllowed;
    }

    /** Determines whether right-click pop-up menu of the central E-Slate component bar is
     *  enabled in this microworld.
     */
    public final void setEslateComponentBarMenuEnabled(boolean allowed) {
        if (!(container instanceof ESlateComposer)) return;
        ESlateComposer composer = (ESlateComposer) container;
        checkSettingChangePriviledge();
        if (eslateComponentBarMenuEnabled == allowed) return;
        eslateComponentBarMenuEnabled = allowed;
        if (composer.componentBar != null)
            composer.componentBar.setIconPopupMenuEnabled(eslateComponentBarMenuEnabled);
        container.setMicroworldChanged(true);
    }
    public final boolean isEslateComponentBarMenuEnabled() {
        if (internalCall) return true;
        return eslateComponentBarMenuEnabled;
    }

    /** Determines whether the name of this microworld can be edited.
     */
    public final void setMwdNameChangeAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (mwdNameChangeAllowed == allowed) return;
        try{
            eslateMwd.setMicroworldRenamingAllowed(mwdNameChangeAllowed, microworldSecurityKey);
            mwdNameChangeAllowed = allowed;
            // Code to tell the microworld that its name is locked.
            container.setMicroworldChanged(true);
        }catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public final boolean isMwdNameChangeAllowed() {
        if (internalCall) return true;
        return mwdNameChangeAllowed;
    }

    /** Determines whether the pop-up menu which is displayed when the components' title
     *  bar is right-clicked, is enabled.
     */
    public final void setComponentBarMenuEnabled(boolean allowed) {
        checkSettingChangePriviledge();
        if (componentBarMenuEnabled == allowed) return;
        componentBarMenuEnabled = allowed;
        container.setMicroworldChanged(true);
    }
    public final boolean isComponentBarMenuEnabled() {
        if (internalCall) return true;
        return componentBarMenuEnabled;
    }

    /** Determines whether the component layers of the microworld can be edited, or the
     *  components can change layers.
     */
    public final void setMwdLayerMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (mwdLayerMgmtAllowed == allowed) return;
        mwdLayerMgmtAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public final boolean isMwdLayerMgmtAllowed() {
        if (internalCall) return true;
        return mwdLayerMgmtAllowed;
    }

    /** Determines whether the parameters of the grid of the E-Slate desktop can be edited.
     */
    public final void setGridMgmtAllowed(boolean allowed) {
        checkSettingChangePriviledge();
        if (gridMgmtAllowed == allowed) return;
        gridMgmtAllowed = allowed;
        container.setMicroworldChanged(true);
    }
    public final boolean isGridMgmtAllowed() {
        if (internalCall) return true;
        return gridMgmtAllowed;
    }

	/** Sets the icon of the microworld. This icon is displayed at the top-left corner
	 *  of every window opened by the microworld.
	 */
	public final void setMicroworldIcon(NewRestorableImageIcon icon) {
		microworldIcon = icon;
		if (icon!=null)
			icon.setSaveFormat(NewRestorableImageIcon.PNG);
		if (container != null && container.parentFrame != null) {
			java.awt.Image img = null;
			if (icon != null) img = icon.getImage();
			container.parentFrame.setIconImage(img);
		}
	}

	public NewRestorableImageIcon getMicroworldIcon() {
		return microworldIcon;
	}

	java.awt.Image getMicroworldIconImage() {
		if (microworldIcon == null) return null;
		return microworldIcon.getImage();
	}


    public void attachScriptListeners(Array objectMap, Array handleMap, Array listenerMap, IntBaseArray listenerIndices) {
         for (int i=0; i<objectMap.size(); i++) {
            Object component = objectMap.at(i);
            ScriptListener[] scriptListeners = (ScriptListener[]) listenerMap.at(i);

            // Add each script listener to the component
            for (int k=0; k<scriptListeners.length; k++) {
                ScriptListener sl = scriptListeners[k];
                if (sl.getScriptLanguage() == ScriptListener.JAVA) {
                    /* If the scriptListener does not carry a Logo script, but rather a
                     * java program, then we have to set the preset variables of this program.
                     * This is down by calling the "setESlateHandle" of the listener instace,
                     * passing to it the component's handle. To do it we get the methods for
                     * the listener's class, search for the method with name "setESlateHandle"
                     * and then invoke it.
                     */
                    try{
                        ESlateHandle hdl = (ESlateHandle) handleMap.at(i);
                        /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
                         * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
                         * However these Java listener's setESlateHandle() has to be called with the
                         * microworld's handle.
                         */
                        if (MicroworldListener.class.isAssignableFrom(sl.getListener().getClass()))
                            hdl = container.getMicroworld().getESlateMicroworld().getESlateHandle();

                        activateListener(scriptListeners[k], hdl);
                    }catch (IllegalAccessException exc) {
                        System.out.println("IllegalAccessException while instantiating listener object or while setting listener's eSlateHandle");
                        continue;
                    }catch (IllegalArgumentException exc) {
                        System.out.println("IllegalArgumentException while setting listener's eSlateHandle");
                        continue;
                    }catch (java.lang.reflect.InvocationTargetException exc) {
                        System.out.println("InvocationTargetException while setting listener's eSlateHandle");
                        continue;
                    }
                }else if (sl.getScriptLanguage() == ScriptListener.LOGO) { // If this is a Logo script listener
                    /* If the logo runtime hasn't already been started, initialize it now.
                     */
                    if (container.logoMachine == null) {
                        container.initLogoEnvironment();
                        container.startWatchingMicroworldForPrimitiveGroups();
                    }
                    ESlateHandle listenerHandle = (ESlateHandle) handleMap.at(i);
                    /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
                     * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
                     * However these Java listener's setESlateHandle() has to be called with the
                     * microworld's handle.
                     */
                    if (MicroworldListener.class.isAssignableFrom(sl.getListener().getClass()))
                        listenerHandle = container.microworld.eslateMwd.getESlateHandle();
                    sl.logoHandler.setLogoRuntime(listenerHandle,
                                                  container.logoMachine,
                                                  container.logoEnvironment,
                                                  container.logoThread,
                                                  container.tokenizer);
                    container.microworld.activateLogoHandler(sl);
                }else if (sl.getScriptLanguage() == ScriptListener.JAVASCRIPT) { // If this is a JS script listener
                    /* If the javascript runtime hasn't already been started, initialize it now.
                     */
                    if (!container.javascriptInUse)
                        container.registerJavascriptVariables();
                    /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
                     * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
                     * However these Java listener's setESlateHandle() has to be called with the
                     * microworld's handle.
                     */
                    ESlateHandle listenerHandle = (ESlateHandle) handleMap.at(i);
                    if (MicroworldListener.class.isAssignableFrom(sl.getListener().getClass()))
                        listenerHandle = container.microworld.eslateMwd.getESlateHandle();
                    sl.jsHandler.setRuntimeInfo(listenerHandle);
                    container.microworld.activateJSHandler(sl);
                }
            }
        }  // scriptListeners' binding

        // Update the ScriptDialog, if it is shown.
//        if (container.scriptDialog != null && container.microworld.componentEventMgmtAllowed)
//            container.scriptDialog.setScriptListenerTree(getScriptListenerTree());
    }
    /** Activates a Java script listener by passing to it its ESlateHandle and the proper
     *  ListenerSecurityProxy.
     */
    final void activateListener(ScriptListener scriptListener, ESlateHandle handle) throws InvocationTargetException, IllegalAccessException {
        /* If the ScriptListener to be activated is not one of the microworld's ScriptListeners,
         * then don't activate it.
         */
        if (container.componentScriptListeners.indexOf(scriptListener) == -1)
            return;

        /* Set the eSlateHandle variable of the listener instance. We must call the
         * setESlateHandle method. To do it we get the methods fo the listener's class,
         * search for the method with name "setESlateHandle" and the invoke it"
         */
        EventListener listener = (EventListener) scriptListener.getListener();
        Class listenerClass = listener.getClass();
        Method[] listenerMethods = listenerClass.getMethods();
        Method handleSetterMethod = null;
        for (int i=0; i<listenerMethods.length; i++) {
            if (listenerMethods[i].getName().equals("setESlateHandle")) {
                handleSetterMethod = listenerMethods[i];
                break;
            }
        }
        /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
         * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
         * However these Java listener's setESlateHandle() has to be called with the
         * microworld's handle.
         */
        if (gr.cti.eslate.base.container.event.MicroworldListener.class.isAssignableFrom(listenerClass))
            handle = container.microworld.eslateMwd.getESlateHandle();

        try{
            if (handleSetterMethod.getParameterTypes().length == 2) {
                internalCall = true;
                ListenerSecurityProxy proxy = createListenerSecurityProxy();
                handleSetterMethod.invoke(listener, new Object[] {handle, proxy});
            }else
                handleSetterMethod.invoke(listener, new Object[] {handle});
        }finally{
            internalCall = false;
        }
    }

    /** Activates a Logo event handler, by assigning it a proper ListenerSecurityProxy.
     */
    final void activateLogoHandler(ScriptListener scriptListener) {
        /* If the ScriptListener to be activated is not one of the microworld's ScriptListeners,
         * then don't activate it.
         */
        if (container.componentScriptListeners.indexOf(scriptListener) == -1)
            return;
        try{
            internalCall = true;
            scriptListener.logoHandler.setSecurityProxy(createListenerSecurityProxy());
        }finally{
            internalCall = false;
        }
    }

    /** Activates a JS event handler, by assigning it a proper ListenerSecurityProxy.
     */
    final void activateJSHandler(ScriptListener scriptListener) {
        /* If the ScriptListener to be activated is not one of the microworld's ScriptListeners,
         * then don't activate it.
         */
        if (container.componentScriptListeners.indexOf(scriptListener) == -1)
            return;
        try{
            internalCall = true;
            scriptListener.jsHandler.setSecurityProxy(createListenerSecurityProxy());
        }finally{
            internalCall = false;
        }
    }

    private final ListenerSecurityProxy createListenerSecurityProxy() {
        if (!internalCall) return null;
        ListenerSecurityProxy proxy = new ListenerSecurityProxy(container, microworldSecurityKey);
        return proxy;
    }

    final void grantAccess(String key) {
        if (microworldSecurityKey.equals(key))
            internalCall = true;
        else
            internalCall = false;
    }

    final void revokeAccess() {
        internalCall = false;
    }

    private String createUniqueId(int offset) {
        java.util.Date now = new java.util.Date();
        java.util.Random rand = new java.util.Random(now.getTime());
        return new Integer(rand.nextInt()).toString();
//        long id = (now.getTime()+(offset*10))/10;
//        return Long.toHexString(id);
    }

    public final void checkActionPriviledge(boolean setting, String settingName) {
        if (internalCall) return;
        if (container.internalFunction) return;
        if (!setting) {
            if (mwdPropertiesDialogBundle == null)
                mwdPropertiesDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MicroworldPropertiesDialogBundle", Locale.getDefault());
            throw new InsufficientPriviledgeException(bundle.getString("InsufficientPriviledge") + ". " +
                          bundle.getString("Setting") + " \"" +
                          mwdPropertiesDialogBundle.getString(settingName) + "\" " +
                          bundle.getString("BlocksAccess") + '.');
        }
    }

    final void checkSettingChangePriviledge() {
        if (internalCall) return;
        if (container.internalFunction) return;
        if (isLocked())
            throw new MicroworldLockedException(container.containerBundle.getString("MicroworldLockedException"));
    }

    /*    final String getMicroworldSecurityKey() {
        return microworldSecurityKey;
    }
*/
    private void createGlobalHandleActivationGroup(ESlateHandle mwdHandle) {
        globalHandleGroup = new ActivationHandleGroup(mwdHandle, new Class[] {Object.class}, ActivationHandleGroup.ALL_CHILDREN);
        globalHandleGroup.addActiveHandleListener(new ActiveHandleListener() {
            public void activeHandleChanged(ActiveHandleEvent e) {
                ESlateHandle activeHandle = e.getNewActiveHandle();
//                System.out.println("GlobalHandleGroup activeHandleChanged(): " + activeHandle);
                String[] path = new String[activeHandle.nestingDepth()];
                ESlateHandle parentHandle = activeHandle;
/*                for (int i=path.length-1; i>=0; i--) {
                    path[i] = parentHandle.getComponentName();
                    System.out.print(path[i] + "-->");
                    parentHandle = parentHandle.getParentHandle();
                }
                System.out.println();
*/
                if (container instanceof ESlateComposer) {
                    ESlateComposer composer = (ESlateComposer) container;
                    if (composer.propertyEventEditor != null) {
                        composer.propertyEventEditor.selectNode(path);
                        discardComponentActivationEvent = true;
                    }
                }
//                    container.propertyEditorUpdater
            }
        });
    }

    public ActivationHandleGroup getGlobalActivationHandleGroup() {
        return globalHandleGroup;
    }
}
