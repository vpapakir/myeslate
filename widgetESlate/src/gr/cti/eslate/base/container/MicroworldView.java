package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;
import gr.cti.eslate.imageChooser.ImageChooser;
import gr.cti.eslate.utils.ESlateFieldMap;
import gr.cti.eslate.utils.ESlateFieldMap2;
import gr.cti.eslate.utils.NewRestorableImageIcon;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.structfile.Entry;
import gr.cti.structfile.StructFile;
import gr.cti.structfile.StructInputStream;

import java.awt.Color;
import java.awt.Insets;
import java.io.BufferedInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.border.Border;


public class MicroworldView implements Externalizable, Cloneable {
    static final long serialVersionUID = 12;
    /* From version "1.0" to "20", the backgroundIcon of the view (if exists), is not
     * stored in the fieldMap, but in a separate file in the structfile. The name of this
     * file (bgrIconFileName) is saved in the fieldMap, instead of the icon itself.
     * 20-->21 The attributes componentInstantiationAllowed, componentRemovalAllowed
               mwdPopupEnabled, componentBarEnabled, nameChangeAllowed, mwdStorageAllowed
               where move to the Microworld class. The method propagatePropsToMicroworld()
               is responsible for moving the values of these properties in the 'currentView'
               of the microworld, to the Microworld class.
     */
    public static final int STR_FORMAT_VERSION = 21;

    String viewName = "";
//    ComponentViewInfo[] componentInfo;
    DesktopItemViewInfo[] desktopItemInfo;

    private Border microworldBorder = null;
    private Insets microworldBorderInsets = null;
    protected Color borderColor = null;
    protected NewRestorableImageIcon borderIcon = null;
    protected int borderType = 0;
    protected Color backgroundColor = null;
    private NewRestorableImageIcon backgroundIcon = null;
    protected String bgrIconFileName = null;
    /* The display mode oscf the icon, which is rendered in the Container's background.
     * Valid values: ImageChooser.NO_IMAGE, ImageChooser.CENTER_IMAGE, ImageChooser.FIT_IMAGE
     *               ImageChooser.TILE_IMAGE.
     */
    protected int backgroundIconDisplayMode = ImageChooser.NO_IMAGE;
    protected int backgroundType = UIDialog.ICON_COLORED_BACKGROUND;
    protected int outerBorderType = 0;

    String activeComponentName = "";
    /* ***  Microworld properties ***
     */
    protected boolean minimizeButtonVisible = true, maximizeButtonVisible = true, closeButtonVisible = true;
    protected boolean controlBarsVisible = true, controlBarTitleActive = true;
    protected boolean helpButtonVisible = true, pinButtonVisible = true, infoButtonVisible = true;
    protected boolean resizeAllowed = true, moveAllowed = true;
// The following properties have been moved to the ESlateMicroworld. They are not view dependent anymore.
//    protected boolean componentInstantiationAllowed = true, componentRemovalAllowed = true;
//    protected boolean mwdPopupEnabled = true, componentBarEnabled = true, nameChangeAllowed = true;
//    protected boolean mwdStorageAllowed = true;
//    protected boolean mwdTitleEnabled = true;

    protected boolean mwdBgrdChangeAllowed = true;
    /** Property which enables/diables the change of the plug connection configuration of the
     *  microworld.
     */
    protected boolean plugConnectionChangeAllowed = true;

    protected boolean menuBarVisible = true, outlineDragEnabled;
    protected boolean componentActivationMethodChangeAllowed = true, componentFrozenStateChangeAllowed = true;
    protected boolean mwdResizable = true, mwdAutoExpandable = false, mwdAutoScrollable = false;
    protected boolean componentsMoveBeyondMwdBounds = true, desktopDraggable = false;
    protected boolean menuSystemHeavyWeight = true;
    protected int viewPositionX = 0, viewPositionY = 0;
    protected int horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    protected int verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
    protected boolean activeComponentHighlighted = true;
    protected int desktopWidth = -1, desktopHeight = -1;
//    protected String modalFrameName = null;

    /* A MicroworldView may save only part of the information that it can record. For example
     * having the background image being saved for every view of the microworld, when this
     * image never changes, is a waste of storage and memory. Therefore an efficient
     * schema has to be developed that will allow for the MicroworldView to record only the
     * necessary information. The rest of the info are not recorded and thus when this view
     * is applied to the microworld, not all the info of the previous view are overidden.
     */
    // The first flag for the schema described above.
    protected boolean microworldInfoSaved = true;

    /* Ensures that the active frame is always with the boundaries of the
     * viewport of the desktop pane's scrollpane, i.e. visible to the user.
     */
    protected boolean ensureActiveComponentVisible = true;
    /** Property that adjusts microworld printing ability in this view.
     */
    boolean mwdPrintAllowed = true;
    /** Determines whether page set-up for printing is enabled in this view.
     */
    protected boolean mwdPageSetupAllowed = true;
    /** Determines whether printing components in this view is enabled.
     */
    protected boolean componentPrintAllowed = true;
    /** Determines whether minimizing components in this view is enabled.
     */
    protected boolean componentMinimizeAllowed = true;
    /** Determines whether maximizing components in this view is enabled.
     */
    protected boolean componentMaximizeAllowed = true;

    /* This variable adjusts whether the background icon of the view will be cached
     * after it is loaded for the first time, or is will be loaded from the structfile,
     * whenever the view is activated.
     */
    protected boolean iconsCached = true;
    /* Check where it is used in MicroworldView */
    boolean oldFormatIconToBeStretched = false;
    /** This HashMap was introduced when the storage format of this class moved from
     *  version 20 to 21. It is used to propagate the values of some attributes which
     *  used to exist in this class to the Microworld class. See STR_FORMAT_VERSION above.
     */
    HashMap propagatableAttributes = new HashMap();

    public MicroworldView() {
    }

    /* Construct a new MicroworldView based on the supplied view. This means that
     * the new view is initialized based on the parameters of the given view. Some
     * parameters are directly taken from the ESlateContainer.
     */
    MicroworldView(ESlateContainer container, MicroworldView view) {
//        this.container = container;
        if (view != null) {
            minimizeButtonVisible = view.minimizeButtonVisible;
            maximizeButtonVisible = view.maximizeButtonVisible;
            closeButtonVisible = view.closeButtonVisible;
            controlBarsVisible = view.controlBarsVisible;
    //        nameChangeAllowed = view.nameChangeAllowed;
            controlBarTitleActive = view.controlBarTitleActive;
            helpButtonVisible = view.helpButtonVisible;
            pinButtonVisible = view.pinButtonVisible;
            infoButtonVisible = view.infoButtonVisible;
            resizeAllowed = view.resizeAllowed;
            moveAllowed = view.moveAllowed;
    //        componentInstantiationAllowed = view.componentInstantiationAllowed;
    //        componentRemovalAllowed = view.componentRemovalAllowed;
            mwdBgrdChangeAllowed = view.mwdBgrdChangeAllowed;
    //        mwdStorageAllowed = view.mwdStorageAllowed;
            plugConnectionChangeAllowed = view.plugConnectionChangeAllowed;
    //        mwdTitleEnabled = view.mwdTitleEnabled;

            microworldBorder = view.microworldBorder;
            microworldBorderInsets = view.microworldBorderInsets;
            borderColor = view.borderColor;
            borderIcon = view.borderIcon;
            borderType = view.borderType;
            backgroundColor = view.backgroundColor;
            backgroundIcon = view.backgroundIcon;
            backgroundIconDisplayMode = view.backgroundIconDisplayMode;
            backgroundType = view.backgroundType;
            outerBorderType = view.outerBorderType;
            menuBarVisible = view.menuBarVisible;
//        mwdPopupEnabled = view.mwdPopupEnabled;
            outlineDragEnabled = view.outlineDragEnabled;
//        componentBarEnabled = view.componentBarEnabled;
            componentFrozenStateChangeAllowed = view.componentFrozenStateChangeAllowed;
            componentActivationMethodChangeAllowed = view.componentActivationMethodChangeAllowed;
            menuSystemHeavyWeight = view.menuSystemHeavyWeight;
            mwdResizable = view.mwdResizable;
            mwdAutoExpandable = view.mwdAutoExpandable;
            mwdAutoScrollable = view.mwdAutoScrollable;
            componentsMoveBeyondMwdBounds = view.componentsMoveBeyondMwdBounds;
            desktopDraggable = view.desktopDraggable;
            activeComponentHighlighted = view.activeComponentHighlighted;

            horizontalScrollbarPolicy = view.horizontalScrollbarPolicy;
            verticalScrollbarPolicy = view.verticalScrollbarPolicy;
            microworldInfoSaved = view.microworldInfoSaved;
            iconsCached = view.iconsCached;
            bgrIconFileName = view.bgrIconFileName;

            ensureActiveComponentVisible = view.ensureActiveComponentVisible;
            mwdPrintAllowed = view.mwdPrintAllowed;
            mwdPageSetupAllowed = view.mwdPageSetupAllowed;
            componentPrintAllowed = view.componentPrintAllowed;
            componentMinimizeAllowed = view.componentMinimizeAllowed;
            componentMaximizeAllowed = view.componentMaximizeAllowed;
            propagatableAttributes = view.propagatableAttributes;
        }

        viewPositionX = container.scrollPane.getViewport().getViewPosition().x;
        viewPositionY = container.scrollPane.getViewport().getViewPosition().y;

//        createComponentInfos(container);

        if (view == null || view.desktopWidth == -1 || view.desktopHeight == -1) {
            java.awt.Dimension d = container.getMicroworldSize();
            desktopWidth = d.width;
            desktopHeight = d.height;
        }else{
            desktopWidth = view.desktopWidth;
            desktopHeight = view.desktopHeight;
        }


//0        if (container.activeFrame != null)
        if (container.mwdComponents.activeComponent != null) {
            activeComponentName = container.mwdComponents.activeComponent.handle.getComponentName();
//System.out.println("Recording view activeComponentName: " + activeComponentName);
        }

/*      The following was excluded, cause there can be multiple modal frames in a microworld
        ESlateComponent modalComponent = container.mwdComponents.getModalFrame();
        if (modalComponent != null)
            modalFrameName = modalComponent.handle.getComponentName();
        else
            modalFrameName = null;
*/
    }

//    ComponentViewInfo[] createComponentInfos(ESlateContainer container) {
    DesktopItemViewInfo[] createDesktopItemInfos(ESlateContainer container, boolean recordSkinState) {
//0        Array compoFrames = new Array();
//0        Array compos = new Array();
//0        container.arrangeMicroworldComponentBasedOnSelectionHistory(compos, compoFrames);
        ESlateComponent[] components = container.arrangeMicroworldComponentBasedOnSelectionHistory();
//0        int frameNum = compoFrames.size();
        int componentCount = components.length;
        DesktopItemViewInfo[] infos = new DesktopItemViewInfo[componentCount];
        for (int i=0; i<componentCount; i++) {
//System.out.println("Storing state of component: " + components[i].getName());
            if (components[i].frame != null)
                infos[i] = new ComponentViewInfo(components[i].frame, recordSkinState);
            else
                infos[i] = new InvisibleComponentViewInfo(components[i].icon);
        }
        desktopItemInfo = infos;
        return infos;
    }

    public void componentRenamed(String oldName, String newName) {
        if (activeComponentName != null && activeComponentName.equals(oldName))
            activeComponentName = newName;

/*        if (modalFrameName != null && modalFrameName.equals(oldName))
            modalFrameName = newName;
*/
    }

/*    public void componentRemoved(String componentName) {
        if (activeComponentName != null && activeComponentName.equals(componentName))
            activeComponentName = null;
    }

    public void addComponentInfo(ESlateInternalFrame frame) {
        int m = 1;
        if (componentInfo != null) m = componentInfo.length+1;
        ComponentViewInfo[] newInfo = new ComponentViewInfo[m];
        newInfo[newInfo.length-1] = new ComponentViewInfo(frame);
        componentInfo = newInfo;
    }
*/
    public void removeComponentInfo(String componentName) {
        if (desktopItemInfo == null) return;
        boolean componentInfoFound = false;
        for (int i=0; i<desktopItemInfo.length; i++) {
            if (desktopItemInfo[i].componentName.equals(componentName)) {
                DesktopItemViewInfo[] newInfo = new DesktopItemViewInfo[desktopItemInfo.length-1];
                for (int k=0; k<desktopItemInfo.length; k++) {
                    if (k < i)
                        newInfo[k] = desktopItemInfo[k];
                    else if (k > i)
                        newInfo[k-1] = desktopItemInfo[k];
                }
                componentInfoFound = true;
                desktopItemInfo = newInfo;
                break;
            }
        }
/*        if (modalFrameName != null && modalFrameName.equals(componentName))
            modalFrameName = null;
*/
//        if (!componentInfoFound)
//            System.out.println("No componentInfo found for component \"" + componentName + "\" in view \"" + viewName + "\"");
//            System.out.println("Inconsistency error in MicroworldView removeComponentInfo()");
    }

    public DesktopItemViewInfo getDesktopItemViewInfo(String componentName) {
        if (desktopItemInfo == null) return null;
        for (int i=0; i<desktopItemInfo.length; i++) {
            if (desktopItemInfo[i].componentName.equals(componentName))
                return desktopItemInfo[i];
        }
        return null;
    }

    public void renameComponent(ESlateComponent ecomponent, String oldName, String newName) {
        DesktopItemViewInfo info = getDesktopItemViewInfo(oldName);
        if (info != null) {
            info.componentName = newName;
            if (InvisibleComponentViewInfo.class.isAssignableFrom(info.getClass()))
                ((InvisibleComponentViewInfo) info).iconState.put("Height", ecomponent.icon.getSize().height);
        }else
            System.out.println("Inconsistency error in MicroworldView renameComponent()");
    }

    public void setMicroworldInfoSaved(boolean save) {
        microworldInfoSaved = save;
    }

    public boolean isMicroworldInfoSaved() {
        return microworldInfoSaved;
    }

    /** Adjusts whether the background icon of the view will be cached after it is first
     *  loaded from the icon's file in the structured storage file, or if the icon will
     *  be always read from the icon file, when the view is activated. Caching results in
     *  faster view activation, while loading each time has memory usage benefits, especially
     *  when the icons are large. The default value is 'true'.
     */
    public void setIconsCached(boolean cached) {
        if (cached == iconsCached) return;
        iconsCached = cached;
        if (!iconsCached)
            backgroundIcon = null;
    }

    /** Reports whether the view's background icons is cached after it is first loaded, or
     *  it is loaded every time the view is activated. The background icon of a view is
     *  loaded the first time the view is activated.
     */
    public boolean isIconsCached() {
        return iconsCached;
    }

    public StorageStructure recordState() {
        ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STR_FORMAT_VERSION, 43);
        fieldMap.put("View Name", viewName);
        fieldMap.put("Component Infos", desktopItemInfo);
        fieldMap.put("Microworld Info Saved", microworldInfoSaved);
//System.out.println("MicroworldView microworldInfoSaved: " + microworldInfoSaved + " for view: " + viewName);
        if (microworldInfoSaved) {
            fieldMap.put("Microworld Border Insets", microworldBorderInsets);
            fieldMap.put("Microworld Border Color", borderColor);
            fieldMap.put("Border Icon", borderIcon);
            fieldMap.put("Border Type", borderType);
            fieldMap.put("Background Color", backgroundColor);
            fieldMap.put("Cache Icons", iconsCached);
//            fieldMap.put("Background Icon", backgroundIcon);
            fieldMap.put("Background Icon File Name", bgrIconFileName);
            fieldMap.put("Background Icon Display Mode", backgroundIconDisplayMode);
            fieldMap.put("Background Type", backgroundType);
            fieldMap.put("Outer Border Type", outerBorderType);
//System.out.println("Storing activeComponentName: " + activeComponentName);
            fieldMap.put("Active Component Name", activeComponentName);
            fieldMap.put("Minimize Allowed", minimizeButtonVisible);
            fieldMap.put("Maximize Allowed", maximizeButtonVisible);
            fieldMap.put("Close Allowed", closeButtonVisible);
            fieldMap.put("Control Bars Visible", controlBarsVisible);
//            fieldMap.put("Name Change Allowed", nameChangeAllowed);
            fieldMap.put("Control Bar Title Active", controlBarTitleActive);
            fieldMap.put("Help Button Visible", helpButtonVisible);
            fieldMap.put("Pin Button Visible", pinButtonVisible);
            fieldMap.put("Info Button Visible", infoButtonVisible);
            fieldMap.put("Resize Allowed", resizeAllowed);
            fieldMap.put("Move Allowed", moveAllowed);
//            fieldMap.put("Component Instantiation Allowed", componentInstantiationAllowed);
//            fieldMap.put("Component Removal Allowed", componentRemovalAllowed);
            fieldMap.put("Microworld Background Change Allowed", mwdBgrdChangeAllowed);
//            fieldMap.put("Microworld Storage Allowed", mwdStorageAllowed);
            fieldMap.put("Microworld Pin View Enabled", plugConnectionChangeAllowed);
//            fieldMap.put("Title Enabled", mwdTitleEnabled);
//            fieldMap.put("Microworld Pop-up Enabled", mwdPopupEnabled);
            fieldMap.put("Menu Bar Visible", menuBarVisible);
            fieldMap.put("Outline Drag Enabled", outlineDragEnabled);
//            fieldMap.put("Component Bar Enabled", componentBarEnabled);
            fieldMap.put("Component Activation Change Allowed", componentActivationMethodChangeAllowed);
            fieldMap.put("Component Frozen State Change Allowed", componentFrozenStateChangeAllowed);
            fieldMap.put("Microworld Resizable", mwdResizable);
            fieldMap.put("Microworld Auto-Expandable", mwdAutoExpandable);
            fieldMap.put("Microworld Auto-Scrollable", mwdAutoScrollable);
            fieldMap.put("Components beyond bounds", componentsMoveBeyondMwdBounds);
            fieldMap.put("Desktop Draggable", desktopDraggable);
            fieldMap.put("Heavyweight Menu System", menuSystemHeavyWeight);
            fieldMap.put("View position X", viewPositionX);
            fieldMap.put("View position Y", viewPositionY);
            fieldMap.put("Horizontal Scroll Bar Policy", horizontalScrollbarPolicy);
            fieldMap.put("Vertical Scroll Bar Policy", verticalScrollbarPolicy);
            fieldMap.put("Active Component Highlighted", activeComponentHighlighted);
//            fieldMap.put("Modal frame name", modalFrameName);
            fieldMap.put("EnsureActiveComponentVisible", ensureActiveComponentVisible);
            fieldMap.put("1001", mwdPrintAllowed);
            fieldMap.put("1002", mwdPageSetupAllowed);
            fieldMap.put("1003", componentPrintAllowed);
            fieldMap.put("1004", componentMinimizeAllowed);
            fieldMap.put("1005", componentMaximizeAllowed);
            fieldMap.put("1006", desktopWidth);
            fieldMap.put("1007", desktopHeight);
        }
        return fieldMap;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        StorageStructure fieldMap = recordState();
        out.writeObject(fieldMap);
//        System.out.println("MicroworldView writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(fieldMap));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//System.out.println("readExternal() of MICROWORLDVIEW");
//        ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
        StorageStructure fieldMap = (StorageStructure) in.readObject();
        applyState(fieldMap);
    }

    public void applyState(StorageStructure fieldMap) {
        String dataVersionStr = fieldMap.getDataVersion();
        int dataVersion = fieldMap.getDataVersionID();
        if (dataVersion == -1)
            dataVersion = 10;
/*        try{
            dataVersion = new Integer(dataVersionStr).intValue();
        }catch (Throwable thr) {
        }
*/
//System.out.println("Putting dataVersion: " + dataVersion);
        propagatableAttributes.put("dataVersion", new Integer(dataVersion));

        viewName = fieldMap.get("View Name", "");
// System.out.println("RESTORING VIEW " + viewName);
        desktopItemInfo = (DesktopItemViewInfo[]) fieldMap.get("Component Infos");
/*for (int i=0; i<desktopItemInfo.length; i++)
    System.out.println("Component: " + desktopItemInfo[i].componentName + ", selected: " + ((ComponentViewInfo) desktopItemInfo[i]).frameState.get("Selected"));
 System.out.println("===================================");
*/
//        System.out.println("MicroworldView readexternal() componentInfo: " + componentInfo);
//        if (componentInfo != null) System.out.println("componentInfo.length: " + componentInfo.length);
        microworldInfoSaved = fieldMap.get("Microworld Info Saved", true);
//System.out.println("MicroworldView readExternal() microworldInfoSaved: " + microworldInfoSaved + ", for view: " + viewName);
        if (microworldInfoSaved) {
            microworldBorderInsets = (Insets) fieldMap.get("Microworld Border Insets");
            borderColor = (Color) fieldMap.get("Microworld Border Color");
            borderIcon = (NewRestorableImageIcon) fieldMap.get("Border Icon");
            borderType = fieldMap.get("Border Type", 0);
            backgroundColor = (Color) fieldMap.get("Background Color");
            backgroundIconDisplayMode = fieldMap.get("Background Icon Display Mode", ImageChooser.NO_IMAGE);
//System.out.println("DataVersion "+dataVersion);            
            if (dataVersion == 10) {
                backgroundIcon = (NewRestorableImageIcon) fieldMap.get("Background Icon");
                /* Before version 20, the bgr icons are saved as they were read from the
                 * image file. If the image had to be stretched to be applied to the
                 * microworld, then instead of the stretched image, the normal one was
                 * saved and stretching was performed everytime the microworld was loaded.
                 * After version 20m, the stretched image is saved, so that microworld
                 * loading won't have the overhead of re-stretching the original image.
                 * So when an old format bgr icon is loaded and the icon display mode is
                 * FIT_IMAGE, it has to be stretched.
                 */
                oldFormatIconToBeStretched = true;
            }else
                bgrIconFileName = (String) fieldMap.get("Background Icon File Name");
            iconsCached = fieldMap.get("Cache Icons", true);
            backgroundType = fieldMap.get("Background Type", UIDialog.ICON_COLORED_BACKGROUND);
            outerBorderType = fieldMap.get("Outer Border Type", 0);
            activeComponentName = (String) fieldMap.get("Active Component Name");
//System.out.println("VIEW Restored activeComponentName: " + activeComponentName);
            minimizeButtonVisible = fieldMap.get("Minimize Allowed", true);
            maximizeButtonVisible = fieldMap.get("Maximize Allowed", true);
            closeButtonVisible = fieldMap.get("Close Allowed", true);
            controlBarsVisible = fieldMap.get("Control Bars Visible", true);
            if (fieldMap.containsKey("Name Change Allowed"))
                propagatableAttributes.put("nameChangeAllowed", new Boolean(fieldMap.get("Name Change Allowed", true)));
            controlBarTitleActive = fieldMap.get("Control Bar Title Active", true);
            helpButtonVisible = fieldMap.get("Help Button Visible", true);
            pinButtonVisible = fieldMap.get("Pin Button Visible", true);
            infoButtonVisible = fieldMap.get("Info Button Visible", true);
            resizeAllowed = fieldMap.get("Resize Allowed", true);
            moveAllowed = fieldMap.get("Move Allowed", true);
            if (fieldMap.containsKey("Component Instantiation Allowed"))
                propagatableAttributes.put("componentInstantiationAllowed", new Boolean(fieldMap.get("Component Instantiation Allowed", true)));
            if (fieldMap.containsKey("Component Removal Allowed"))
                propagatableAttributes.put("componentRemovalAllowed", new Boolean(fieldMap.get("Component Removal Allowed", true)));
//            componentRemovalAllowed = fieldMap.get("Component Removal Allowed", true);
            mwdBgrdChangeAllowed = fieldMap.get("Microworld Background Change Allowed", true);
            if (fieldMap.containsKey("Microworld Storage Allowed"))
                propagatableAttributes.put("mwdStorageAllowed", new Boolean(fieldMap.get("Microworld Storage Allowed", true)));
            plugConnectionChangeAllowed = fieldMap.get("Microworld Pin View Enabled", true);
//            mwdTitleEnabled = fieldMap.get("Title Enabled", true);
            if (fieldMap.containsKey("Title Enabled"))
                propagatableAttributes.put("microworldNameUserDefined", new Boolean(fieldMap.get("Title Enabled", true)));
            if (fieldMap.containsKey("Microworld Pop-up Enabled"))
                propagatableAttributes.put("mwdPopupEnabled", new Boolean(fieldMap.get("Microworld Pop-up Enabled", true)));
//            mwdPopupEnabled = fieldMap.get("Microworld Pop-up Enabled", true);
            menuBarVisible = fieldMap.get("Menu Bar Visible", true);
            outlineDragEnabled = fieldMap.get("Outline Drag Enabled", true);
            if (fieldMap.containsKey("Component Bar Enabled"))
                propagatableAttributes.put("componentBarEnabled", new Boolean(fieldMap.get("Component Bar Enabled", true)));
//            componentBarEnabled = fieldMap.get("Component Bar Enabled", true);
            componentActivationMethodChangeAllowed = fieldMap.get("Component Activation Change Allowed", true);
            componentFrozenStateChangeAllowed = fieldMap.get("Component Frozen State Change Allowed", true);
            mwdResizable = fieldMap.get("Microworld Resizable", true);
            mwdAutoExpandable = fieldMap.get("Microworld Auto-Expandable", false);
            mwdAutoScrollable = fieldMap.get("Microworld Auto-Scrollable", false);
            componentsMoveBeyondMwdBounds = fieldMap.get("Components beyond bounds", true);
            desktopDraggable = fieldMap.get("Desktop Draggable", false);
            menuSystemHeavyWeight = fieldMap.get("Heavyweight Menu System", false);
            viewPositionX = fieldMap.get("View position X", 0);
            viewPositionY = fieldMap.get("View position Y", 0);
            horizontalScrollbarPolicy = fieldMap.get("Horizontal Scroll Bar Policy", JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            verticalScrollbarPolicy = fieldMap.get("Vertical Scroll Bar Policy", JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            activeComponentHighlighted = fieldMap.get("Active Component Highlighted", true);
            // Not used
            String modalFrameName = fieldMap.get("Modal frame name", (String) null);

            ensureActiveComponentVisible = fieldMap.get("EnsureActiveComponentVisible", false);
            mwdPrintAllowed = fieldMap.get("1001", true);
            mwdPageSetupAllowed = fieldMap.get("1002", true);
            componentPrintAllowed = fieldMap.get("1003", true);
            componentMinimizeAllowed = fieldMap.get("1004", true);
            componentMaximizeAllowed = fieldMap.get("1005", true);
            desktopWidth = fieldMap.get("1006", -1);
            desktopHeight = fieldMap.get("1007", -1);
        }
    }

    /* Method which was introduced when the storage format of the MicroworldView class
     * changed from 20 to 21. This method moves the values of some attributes that
     * belonged to this class to the Microworld class, where they now belong. The values
     * of these attributes are stored in the 'propagatableAttributes' hashmap by readExternal().
     */
    void propagatePropsToMicroworld(Microworld microworld) {
        int dataVersion = ((Integer) propagatableAttributes.get("dataVersion")).intValue();
        /* Attribute propagation should occur anly if the version of the MicroworldView's
         * storage format is equal or less than 0.
         */
        if (dataVersion > 20) return;
        boolean nameChangeAllowed = ((Boolean) propagatableAttributes.get("nameChangeAllowed")).booleanValue();
        microworld.setComponentNameChangeAllowedInternal(nameChangeAllowed);
        boolean componentInstantiationAllowed = ((Boolean) propagatableAttributes.get("componentInstantiationAllowed")).booleanValue();
        microworld.setComponentInstantiationAllowedInternal(componentInstantiationAllowed);
        boolean componentRemovalAllowed = ((Boolean) propagatableAttributes.get("componentRemovalAllowed")).booleanValue();
        microworld.setComponentRemovalAllowedInternal(componentRemovalAllowed);
        boolean mwdPopupEnabled = ((Boolean) propagatableAttributes.get("mwdPopupEnabled")).booleanValue();
        microworld.setMwdPopupEnabledInternal(mwdPopupEnabled);
        boolean componentBarEnabled = ((Boolean) propagatableAttributes.get("componentBarEnabled")).booleanValue();
        microworld.setESlateComponentBarEnabledInternal(componentBarEnabled);
        boolean mwdStorageAllowed = ((Boolean) propagatableAttributes.get("mwdStorageAllowed")).booleanValue();
        microworld.setMwdStorageAllowedInternal(mwdStorageAllowed);
        boolean microworldNameUserDefined = ((Boolean) propagatableAttributes.get("microworldNameUserDefined")).booleanValue();
        microworld.setMicroworldNameUserDefinedInternal(microworldNameUserDefined);
    }

    protected void setMicroworldBorder(ESlateContainer container, Border b) {
        if (container == null) return;
        microworldBorder = b;
        if (b != null)
            microworldBorderInsets = b.getBorderInsets(container.scrollPane);
        else
            microworldBorderInsets = null;
//        System.out.println("view: " + viewName + ", microworldBorderInsets: " + microworldBorderInsets);
    }

    protected Border getMicroworldBorder() {
        return microworldBorder;
    }

    protected Insets getMicroworldBorderInsets() {
        return microworldBorderInsets;
    }

    public Object clone() {
        Object o = null;
        try{
            o = super.clone();
        }catch (CloneNotSupportedException e) {}
        if (desktopItemInfo != null)
            ((MicroworldView) o).desktopItemInfo = (DesktopItemViewInfo[]) ((MicroworldView) o).desktopItemInfo.clone();
        return o;
    }

    public String getName() {
        return viewName;
    }

    protected void setBackgroundIcon(NewRestorableImageIcon icon) {
        if (icon == backgroundIcon) return;
        backgroundIcon = icon;
        bgrIconFileName = null;
    }

    /* Returns the bgr icon of the view. The view bgr icon is not loaded when the microworld is
     * first loaded. Rather it is loaded on demand, when the view is firstly activated. The
     * icons are saved in separate files in the structfile of the microworld under the
     * directory "Microworld View Icons". If the icon has already been loaded, it is simply
     * returned.
     */
    protected NewRestorableImageIcon getBackgroundIcon(MicroworldViewList mwdViewList, StructFile structfile) {
        if (backgroundIcon != null)
            return backgroundIcon;

        String iconFileName = bgrIconFileName;
        if (iconFileName == null || iconFileName.trim().length() == 0)
            return null;
        if (structfile == null || !structfile.isOpen()) {
            System.out.println("Cannot read the background icon of the view: " + viewName + ", because the structfile not specified or is not open.");
            return null;
        }

        String iconDirName = MicroworldViewList.VIEW_ICON_DIR_NAME; //"Microworld View Icons";
        Entry iconDirEntry = null;
        try {
            structfile.changeDir(structfile.getRootEntry());
            iconDirEntry = structfile.findEntry(iconDirName);
        }catch (Throwable thr) {
            System.out.println("Cannot get the view bgr icon, cause there is not directory " + iconDirName + " in the structfile.");
            return null;
        }

        try{
            structfile.changeDir(iconDirEntry);
        }catch (Throwable thr) {
            System.out.println("Cannot get the view bgr icon. Error while changing dir to "+ iconDirName + ". Message: " + thr.getMessage());
            return null;
        }

        Entry iconFileEntry = null;
        try {
            iconFileEntry = structfile.findEntry(iconFileName);
        }catch (Throwable thr) {
            System.out.println("Cannot get the view bgr icon. There is no file " + iconFileName + " in the directory " + iconDirName + " of the structfile.");
            return null;
        }

        NewRestorableImageIcon icon = null;
        try{
//			int buffSize = 50000;
            BufferedInputStream bis = new BufferedInputStream(new StructInputStream(structfile, iconFileEntry), 8096);
//long st1 = System.currentTimeMillis();
            icon = new NewRestorableImageIcon(bis, "");
//System.out.println("getBackgroundIcon() 1.2: " + (System.currentTimeMillis()-st1));
            bis.close();
        }catch (Throwable thr) {
            System.out.println("Cannot get the bgr icon of view \"" + viewName + "\". Error while reading the icon file " + iconFileName + ". Message: " + thr.getMessage());
        }
        if (iconsCached)
            backgroundIcon = icon;
        /* If there exist any other views in the Microworld's view list with the same
         * 'bgrIconFileName', then if they are cache-enabled, assign their background
         * icons to this view's background icon. This way the icons won't get loaded
         * again when these views are activated and most importantly the memory won't be
         * loaded with extra copies of the same icon.
         */
        for (int i=0; i<mwdViewList.viewList.length; i++) {
            MicroworldView view = mwdViewList.viewList[i];
            if (view.iconsCached && view.backgroundIcon == null && bgrIconFileName.equals(view.bgrIconFileName))
                view.backgroundIcon = icon;
        }

        return icon;
    }

    public final boolean isMwdPrintAllowed() {
        return mwdPrintAllowed;
    }

    public final boolean isComponentPrintAllowed() {
        return componentPrintAllowed;
    }

    public final boolean isMwdPageSetupAllowed() {
        return mwdPageSetupAllowed;
    }

    public final boolean isComponentMaximizeAllowed() {
        return componentMaximizeAllowed;
    }

    public final boolean isComponentMinimizeAllowed() {
        return componentMinimizeAllowed;
    }

    public boolean isMwdAutoExpandable() {
        return mwdAutoExpandable;
    }

    public boolean isMwdResizable() {
        return mwdResizable;
    }
}

abstract class DesktopItemViewInfo {
//    static final long serialVersionUID = 12;
//    public static final String STR_FORMAT_VERSION = "1.0";
    String componentName;
//    public abstract ESlateFieldMap getState();
    public abstract StorageStructure getState();
//    boolean isIcon = false;
//    boolean isMaximum = false;
//    int width, height, xLocation, yLocation;
//    boolean resizable = true;
//    boolean closable = true;
//    boolean iconifiable = true;
//    boolean maximizable = true;
//    int layer;

}


/* This class stores information about the components in the microworld, which are
 * displayed inside frames. Typically these are the visible components. Most of
 * this information has to do with the frames the components are hosted in. This
 * class is only used as a placeholder for this information and this happens only
 * when the microworld is saved/loaded. While the microworld is running all this
 * information is actually stored inside the ESlateInternalFrames.
 */
class ComponentViewInfo extends DesktopItemViewInfo implements Externalizable, Cloneable {
    static final long serialVersionUID = 12;
//    public static final String STR_FORMAT_VERSION = "2.0";
    boolean isIcon = false;
    boolean isMaximum = false;
    int width, height, xLocation, yLocation;
    boolean resizable = true;
    boolean closable = true;
    boolean iconifiable = true;
    boolean maximizable = true;
    int layer;
    boolean barVisibilityFrameState = true;
    boolean helpButtonVisibilityFrameState = true;
    boolean infoButtonVisibilityFrameState = true;
    boolean pinButtonVisibilityFrameState = true;
    boolean activeTitleFrameState = true;
    boolean isFrozen = false;
    boolean componentActivatedOnMousePress = true;
    /* When the new ESlateInternalFrame with 'periblhma' was introduced, it had
     * it implemented the Externalizable i/f. So the ComponentViewInfo does not have
     * to record all the info about the ESlateInternalFrame anymore. Rather it asks
     * the frame to record its state and return it in an ESlateFieldMap, which is what
     * the ComponentViewInfo stores. When a MicroworldView is re-applied, the
     * ComponentViewInfo for each frame, provides the 'frameState' ESlateFieldMap
     * to the frame, which resets itself according to the info in the 'framestate'.
     */

//    public ESlateFieldMap frameState = null;
    public StorageStructure frameState = null;

    public ComponentViewInfo() {
        componentName="";
//        layer = 0;
    }

    public ComponentViewInfo(ESlateInternalFrame fr, boolean recordSkinState) {
        frameState = fr.recordState(recordSkinState);
//System.out.println("new ComponentViewInfo for frame: " + fr.getTitle() + ", selected: " + fr.isSelected() + ", recorded selected state: " + frameState.get("Selected"));
        componentName = fr.getTitle();
/*        componentName = fr.getTitle();// .eSlateHandle.getComponentName();
        closable = fr.isClosable();
        iconifiable = fr.isIconifiable();
        maximizable = fr.isMaximizable();
        barVisibilityFrameState = fr.isTitlePanelVisible();
        helpButtonVisibilityFrameState = fr.isHelpButtonVisible();
        infoButtonVisibilityFrameState = fr.isInfoButtonVisible();
        pinButtonVisibilityFrameState = fr.isPlugButtonVisible();
        activeTitleFrameState = fr.isComponentNameChangeableFromMenuBar();
        resizable = fr.isResizable();
        isIcon = fr.isIcon();
        isMaximum = fr.isMaximum();
//if        isFrozen = fr.isFrozen();
        componentActivatedOnMousePress = fr.isComponentActivatedOnMouseClick();

        width = fr.getRealSize().width;
        height = fr.getRealSize().height;
        xLocation = fr.getDesktopItemRestoreLocation().x;
        yLocation = fr.getDesktopItemRestoreLocation().y;
        if (!fr.isTitlePanelVisible()) {
            height = height+18;
            yLocation = yLocation-18;
        }

        layer = fr.getLayer();
*/
    }

    public void writeExternal(ObjectOutput out) throws IOException {
/*        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 19);
        fieldMap.put("Component name", componentName);
        fieldMap.put("Frame Closable State", closable);
        fieldMap.put("Frame Iconifiable State", iconifiable);
        fieldMap.put("Frame Maximizable State", maximizable);
        fieldMap.put("Frame Bar Visibility State", barVisibilityFrameState);
        fieldMap.put("Help Button Visibility State", helpButtonVisibilityFrameState);
        fieldMap.put("Info Button Visibility State", infoButtonVisibilityFrameState);
        fieldMap.put("Pin Button Visibility State", pinButtonVisibilityFrameState);
        fieldMap.put("Frame Active Title State", activeTitleFrameState);
        fieldMap.put("Frame Resizable", resizable);
        fieldMap.put("Is Icon", isIcon);
        fieldMap.put("Is Maximum", isMaximum);
        fieldMap.put("Width", width);
        fieldMap.put("Height", height);
        fieldMap.put("X Location", xLocation);
        fieldMap.put("Y Location", yLocation);
        fieldMap.put("Is Frozen", isFrozen);
        fieldMap.put("Component Activated On Mouse Press", componentActivatedOnMousePress);
        fieldMap.put("Layer", layer);

        out.writeObject(fieldMap);
*/
        out.writeObject(frameState);
//        System.out.println("ComponentViewInfo writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(frameState));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
        StorageStructure fieldMap = (StorageStructure) in.readObject();
//System.out.println("ComponentViewInfo readExternal() fieldMap.getDataVersion(): " + fieldMap.getDataVersion());
        if (fieldMap.getDataVersion().equals("1.0")) {
            componentName = (String) fieldMap.get("Component name");
            closable = fieldMap.get("Frame Closable State", true);
            iconifiable = fieldMap.get("Frame Iconifiable State", true);
            maximizable = fieldMap.get("Frame Maximizable State", true);
            barVisibilityFrameState = fieldMap.get("Frame Bar Visibility State", true);
            helpButtonVisibilityFrameState = fieldMap.get("Help Button Visibility State", true);
            infoButtonVisibilityFrameState = fieldMap.get("Info Button Visibility State", true);
            pinButtonVisibilityFrameState = fieldMap.get("Pin Button Visibility State", true);
            activeTitleFrameState = fieldMap.get("Frame Active Title State", true);
            resizable = fieldMap.get("Frame Resizable", true);
            isIcon = fieldMap.get("Is Icon", false);
            isMaximum = fieldMap.get("Is Maximum", false);
            width = fieldMap.get("Width", 0);
            height = fieldMap.get("Height", 0);
            xLocation = fieldMap.get("X Location", 0);
            yLocation = fieldMap.get("Y Location", 0);
            isFrozen = fieldMap.get("Is Frozen", false);
            componentActivatedOnMousePress = fieldMap.get("Component Activated On Mouse Press", true);
            layer = fieldMap.get("Layer", 0);
            populateFrameState();
        }else{
            frameState = fieldMap;
            componentName = (String) fieldMap.get("Title");
        }
//        System.out.println(frameState);
    }

    /* Reads all the data of an old version ComponentViewInfo and creates the
     * 'frameState' ESlateFieldMap with all the available information.
     */
    private void populateFrameState() {
        frameState = new ESlateFieldMap("1.0.0");
        frameState.put("Title", componentName);
        if (!barVisibilityFrameState) {
            /* In microworlds which were stored with a version of ESlate prior to 1.8, the
             * components' bounds which had their title bar invisible were wrong. The
             * bounds included the title bar, although it was not visible. The following
             * line corrects this problem, so that these components are restored at their
             * proper position and size in the 1.8.* version of ESlate.
             */
            frameState.put("Bounds",new java.awt.Rectangle(xLocation, yLocation+18, width, height-18));
        }else
            frameState.put("Bounds",new java.awt.Rectangle(xLocation, yLocation, width, height));
        frameState.put("Restore Location",new java.awt.Point(xLocation, yLocation));
        frameState.put("Restore Size",new java.awt.Dimension(width, height));
        frameState.put("Closable",closable);
        frameState.put("Iconifiable",iconifiable);
        frameState.put("Maximizable",maximizable);
        frameState.put("TitlePanelVisible",barVisibilityFrameState);
        frameState.put("HelpButtonVisible",helpButtonVisibilityFrameState);
        frameState.put("InfoButtonVisible",infoButtonVisibilityFrameState);
        frameState.put("PlugButtonVisible",pinButtonVisibilityFrameState);
        frameState.put("ComponentNameChangeableFromMenuBar", activeTitleFrameState);
        frameState.put("Resizable",resizable);
        frameState.put("Icon",isIcon);
        frameState.put("Maximum",isMaximum);
        frameState.put("ComponentActivatedOnMouseClick",componentActivatedOnMousePress);
        frameState.put("Layer", layer);
    }

    public Object clone() {
        Object o = null;
        try{
            o = super.clone();
        }catch (CloneNotSupportedException e) {}
        return o;
    }

//    public ESlateFieldMap getState() {
    public StorageStructure getState() {
        return frameState;
    }
}


/* This class stores information about the invisible components in the microworld, which are
 * displayed using UILessComponentIcons. This class is only used as a placeholder for this
 * information and this happens only when the microworld is saved/loaded. While the microworld
 * is running all this information is actually stored inside the UILessComponentIcons.
 */
class InvisibleComponentViewInfo extends DesktopItemViewInfo implements Externalizable, Cloneable {
    static final long serialVersionUID = 12;
//    public static final String STR_FORMAT_VERSION = "1.0";
//    boolean componentNameEditableFromUI = true;
//    ESlateFieldMap iconState = null;
    StorageStructure iconState = null;

    public InvisibleComponentViewInfo() {
        componentName="";
//        layer = 0;
    }

    public InvisibleComponentViewInfo(UILessComponentIcon icon) {
        iconState = icon.recordState();
        componentName = icon.handle.getComponentName();
/*        componentName = icon.handle.getComponentName();
        closable = icon.isClosable();
        iconifiable = icon.isIconifiable();
        maximizable = icon.isMaximizable();
        isIcon = icon.isIcon();
        componentNameEditableFromUI = icon.isNameEditable();
        resizable = icon.isDesktopItemResizable();

        width = icon.getSize().width;
        height = icon.getSize().height;
        xLocation = icon.getLocation().x;
        yLocation = icon.getLocation().y;
//        System.out.println("InvisibleComponentViewInfo width: " + width + ", height: " + height);
//        System.out.println("InvisibleComponentViewInfo xLocation: " + xLocation + ", yLocation: " + yLocation);

        layer = icon.getLayer();
*/
    }

    public void writeExternal(ObjectOutput out) throws IOException {
/*        ESlateFieldMap fieldMap = new ESlateFieldMap(STR_FORMAT_VERSION, 19);
        fieldMap.put("Component name", componentName);
        fieldMap.put("Closable", closable);
        fieldMap.put("Iconifiable", iconifiable);
        fieldMap.put("Maximizable", maximizable);
        fieldMap.put("Name Editable", componentNameEditableFromUI);
        fieldMap.put("Resizable", resizable);
        fieldMap.put("Is Icon", isIcon);
        fieldMap.put("Is Maximum", isMaximum);
        fieldMap.put("Width", width);
        fieldMap.put("Height", height);
        fieldMap.put("X Location", xLocation);
        fieldMap.put("Y Location", yLocation);
        fieldMap.put("Layer", layer);
*/
        out.writeObject(iconState);
//        System.out.println("InvisibleComponentViewInfo writeExternal() size: " + ESlateContainerUtils.getFieldMapContentLength(iconState));
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        iconState = (ESlateFieldMap) in.readObject();;
        iconState = (StorageStructure) in.readObject();;
        componentName = (String) iconState.get("Component name");
/*        ESlateFieldMap fieldMap = (ESlateFieldMap) in.readObject();
        componentName = (String) fieldMap.get("Component name");
        closable = fieldMap.get("Closable", true);
        iconifiable = fieldMap.get("Iconifiable", true);
        maximizable = fieldMap.get("Maximizable", true);
        componentNameEditableFromUI = fieldMap.get("Name Editable", true);
        resizable = fieldMap.get("Resizable", true);
        isIcon = fieldMap.get("Is Icon", true);
        isMaximum = fieldMap.get("Is Maximum", false);
        width = fieldMap.get("Width", 0);
        height = fieldMap.get("Height", 0);
        xLocation = fieldMap.get("X Location", 0);
        yLocation = fieldMap.get("Y Location", 0);
        layer = fieldMap.get("Layer", 0);
*/
//        System.out.println("readExternal() xLocation: " + xLocation + ", yLocation: " + yLocation);
//        System.out.println("readExternal() width: " + width + ", height: " + height);
    }

    public Object clone() {
        Object o = null;
        try{
            o = super.clone();
        }catch (CloneNotSupportedException e) {}
        return o;
    }

//    public ESlateFieldMap getState() {
    public StorageStructure getState() {
        return iconState;
    }
}
