package gr.cti.eslate.base.container;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.JScrollPane;

/**
 * This class contains all the different microworld settings and is used to record
 * different but reusable setting profiles.
 * Copyright:    Copyright (c) 2001
 * @author George Tsironis
 * @version 1.0
 */

public class MicroworldSettingsProfile {
    private static final String strictKeyValueSeparators = "=:";
    private static final String whiteSpaceChars = " \t\r\n\f";
    private static final String keyValueSeparators = "=:\t\r\n\f";
    private static MicroworldSettingsProfile runtimeProfile = null;
    String profileName = "";
    String fileName = null;

    // MICROWORLD PROPERTIES
    public boolean viewCreationAllowed = true, viewRemovalAllowed = true, viewRenameAllowed = true;
    public boolean viewActivationAllowed = true;
    public boolean mwdPrintAllowed = true, viewMgmtAllowed = true, eslateOptionMgmtAllowed = true;
    public boolean componentPropertyMgmtAllowed = true, componentEventMgmtAllowed = true;
    public boolean componentSoundMgmtAllowed = true, mwdLAFChangeAllowed = true, consolesAllowed = true;
    public boolean eslateComponentBarMenuEnabled = true, eslateComponentBarEnabled = true;
    public boolean mwdNameChangeAllowed = true, componentNameChangeAllowed = true;
    public boolean performanceMgrActive = false;
    public boolean componentBarMenuEnabled = true, mwdLayerMgmtAllowed = true;
    public boolean gridMgmtAllowed = true, mwdPageSetupAllowed = true;
    public boolean componentInstantiationAllowed = true;
    public boolean componentRemovalAllowed = true;
    public boolean mwdBgrdChangeAllowed = true;
    public boolean mwdStorageAllowed = true;
    public boolean mwdAutoBackupEnabled = true;
    public boolean plugConnectionChangeAllowed = true;
    public boolean microworldNameUserDefined = true;
    public boolean mwdPopupEnabled = true;
    public boolean menuBarVisible = true;
//    public boolean componentBarEnabled = true; // The bar with all the microworld components
    public boolean mwdResizable = true;
    public boolean mwdAutoScrollable = false;
    public boolean componentsMoveBeyondMwdBounds = true;
    public boolean mwdAutoExpandable = false;
    public boolean desktopDraggable = false;
    public boolean menuSystemHeavyWeight = false;
    public boolean storeSkinOnAPerViewBasis = false;
    public int horizontalScrollbarPolicy = javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    public int verticalScrollbarPolicy = javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;

    // COMPONENT PROPERTIES
    public boolean resizeAllowed = true;
    public boolean moveAllowed = true;
    public boolean outlineDragEnabled = false;
//    public boolean nameChangeAllowed = true; // component renaming allowed
    public boolean componentActivationMethodChangeAllowed = true;
    public boolean activeComponentHighlighted = true;
    public boolean ensureActiveComponentVisible = true;
    public boolean componentPrintAllowed = true;
    public boolean componentMinimizeAllowed = true;
    public boolean componentMaximizeAllowed = true;

    // COMPONENT BAR PROPERTIES
    public boolean minimizeButtonVisible = true;
    public boolean maximizeButtonVisible = true;
    public boolean closeButtonVisible = true;
    public boolean controlBarTitleActive = true;
    public boolean helpButtonVisible = true;
    public boolean infoButtonVisible = true;
    public boolean plugButtonVisible = true;
    public boolean controlBarsVisible = true;

    /** Creates the default design-time profile */
    MicroworldSettingsProfile() {
    }

    /** Creates the current settings profile of the active microworld */
    public MicroworldSettingsProfile(ESlateContainer container) {
        if (container == null) throw new NullPointerException();
        if (container.currentView == null || container.microworld == null)
            throw new NullPointerException("No open microworld in ESlateContainer");

        MicroworldView view = container.currentView;

        // COMPONENT BAR PROPERTIES
        minimizeButtonVisible = view.minimizeButtonVisible;
        maximizeButtonVisible = view.maximizeButtonVisible;
        closeButtonVisible = view.closeButtonVisible;
        controlBarTitleActive = view.controlBarTitleActive;
        helpButtonVisible = view.helpButtonVisible;
        infoButtonVisible = view.infoButtonVisible;
        plugButtonVisible = view.pinButtonVisible;
        controlBarsVisible = view.controlBarsVisible;

        // COMPONENT PROPERTIES
        resizeAllowed = view.resizeAllowed;
        moveAllowed = view.moveAllowed;
        outlineDragEnabled = view.outlineDragEnabled;
//        nameChangeAllowed = view.nameChangeAllowed;
        componentActivationMethodChangeAllowed = view.componentActivationMethodChangeAllowed;
        activeComponentHighlighted = view.activeComponentHighlighted;
        ensureActiveComponentVisible = view.ensureActiveComponentVisible;
        componentPrintAllowed = view.componentPrintAllowed;
        componentMinimizeAllowed = view.componentMinimizeAllowed;
        componentMaximizeAllowed = view.componentMaximizeAllowed;

        // MICROWORLD PROPERTIES
        mwdBgrdChangeAllowed = view.mwdBgrdChangeAllowed;
        plugConnectionChangeAllowed = view.plugConnectionChangeAllowed;
        menuBarVisible = view.menuBarVisible;
        mwdResizable = view.mwdResizable;
        mwdAutoScrollable = view.mwdAutoScrollable;
        componentsMoveBeyondMwdBounds = view.componentsMoveBeyondMwdBounds;
        mwdAutoExpandable = view.mwdAutoExpandable;
        desktopDraggable = view.desktopDraggable;
        menuSystemHeavyWeight = view.menuSystemHeavyWeight;
        horizontalScrollbarPolicy = view.horizontalScrollbarPolicy;
        verticalScrollbarPolicy = view.verticalScrollbarPolicy;
        componentInstantiationAllowed = container.microworld.componentInstantiationAllowed;
        componentRemovalAllowed = container.microworld.componentRemovalAllowed;
        mwdStorageAllowed = container.microworld.mwdStorageAllowed;
        mwdAutoBackupEnabled = container.microworld.mwdAutoBackupEnabled;
        mwdPopupEnabled = container.microworld.mwdPopupEnabled;
        storeSkinOnAPerViewBasis = container.microworld.storeSkinOnAPerViewBasis;
        microworldNameUserDefined = container.microworld.microworldNameUserDefined;

        mwdPrintAllowed = view.mwdPrintAllowed;
        mwdPageSetupAllowed = view.mwdPageSetupAllowed;
        viewCreationAllowed = container.microworld.viewCreationAllowed;
        viewRemovalAllowed = container.microworld.viewRemovalAllowed;
        viewRenameAllowed = container.microworld.viewRenameAllowed;
        viewActivationAllowed = container.microworld.viewActivationAllowed;
        viewMgmtAllowed = container.microworld.viewMgmtAllowed;
        eslateOptionMgmtAllowed = container.microworld.eslateOptionMgmtAllowed;
        componentPropertyMgmtAllowed = container.microworld.componentPropertyMgmtAllowed;
        componentEventMgmtAllowed = container.microworld.componentEventMgmtAllowed;
        componentSoundMgmtAllowed = container.microworld.componentSoundMgmtAllowed;
        mwdLAFChangeAllowed = container.microworld.mwdLAFChangeAllowed;
        consolesAllowed = container.microworld.consolesAllowed;
        eslateComponentBarMenuEnabled = container.microworld.eslateComponentBarMenuEnabled;
        eslateComponentBarEnabled = container.microworld.eslateComponentBarEnabled;
        mwdNameChangeAllowed = container.microworld.mwdNameChangeAllowed;
        performanceMgrActive = container.microworld.isPerformanceMgrActive();
        componentNameChangeAllowed = container.microworld.componentNameChangeAllowed;
        componentBarMenuEnabled = container.microworld.componentBarMenuEnabled;
        mwdLayerMgmtAllowed = container.microworld.mwdLayerMgmtAllowed;
        gridMgmtAllowed = container.microworld.gridMgmtAllowed;

        ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ProfileDialogBundle", Locale.getDefault());
        profileName = bundle.getString("Current Profile");

    }

    /** Creates a MicroworldSettingsProfiles based on the supplied
     *  MicroworldSettings and ViewBaseSettings.
     */
    public MicroworldSettingsProfile(MicroworldSettings microworldSettings, ViewBaseSettings viewBaseSettings, String profName) {
        if (microworldSettings == null || viewBaseSettings == null) throw new NullPointerException();

        // COMPONENT BAR PROPERTIES
        minimizeButtonVisible = viewBaseSettings.minimizeButtonVisible;
        maximizeButtonVisible = viewBaseSettings.maximizeButtonVisible;
        closeButtonVisible = viewBaseSettings.closeButtonVisible;
        controlBarTitleActive = viewBaseSettings.controlBarTitleActive;
        helpButtonVisible = viewBaseSettings.helpButtonVisible;
        infoButtonVisible = viewBaseSettings.infoButtonVisible;
        plugButtonVisible = viewBaseSettings.plugButtonVisible;
        controlBarsVisible = viewBaseSettings.controlBarsVisible;

        // COMPONENT PROPERTIES
        resizeAllowed = viewBaseSettings.resizeAllowed;
        moveAllowed = viewBaseSettings.moveAllowed;
        outlineDragEnabled = viewBaseSettings.outlineDragEnabled;
//        nameChangeAllowed = view.nameChangeAllowed;
        componentActivationMethodChangeAllowed = viewBaseSettings.componentActivationMethodChangeAllowed;
        activeComponentHighlighted = viewBaseSettings.activeComponentHighlighted;
        ensureActiveComponentVisible = viewBaseSettings.ensureActiveComponentVisible;
        componentPrintAllowed = viewBaseSettings.componentPrintAllowed;
        componentMinimizeAllowed = viewBaseSettings.componentMinimizeAllowed;
        componentMaximizeAllowed = viewBaseSettings.componentMaximizeAllowed;

        // MICROWORLD PROPERTIES
        mwdBgrdChangeAllowed = viewBaseSettings.mwdBgrdChangeAllowed;
        plugConnectionChangeAllowed = viewBaseSettings.plugConnectionChangeAllowed;
        menuBarVisible = viewBaseSettings.menuBarVisible;
        mwdResizable = viewBaseSettings.mwdResizable;
        mwdAutoScrollable = viewBaseSettings.mwdAutoScrollable;
        componentsMoveBeyondMwdBounds = viewBaseSettings.componentsMoveBeyondMwdBounds;
        mwdAutoExpandable = viewBaseSettings.mwdAutoExpandable;
        desktopDraggable = viewBaseSettings.desktopDraggable;
        menuSystemHeavyWeight = viewBaseSettings.menuSystemHeavyWeight;
        horizontalScrollbarPolicy = viewBaseSettings.horizontalScrollbarPolicy;
        verticalScrollbarPolicy = viewBaseSettings.verticalScrollbarPolicy;
        componentInstantiationAllowed = microworldSettings.componentInstantiationAllowed;
        componentRemovalAllowed = microworldSettings.componentRemovalAllowed;
        mwdStorageAllowed = microworldSettings.mwdStorageAllowed;
        mwdAutoBackupEnabled = microworldSettings.mwdAutoBackupEnabled;
        mwdPopupEnabled = microworldSettings.mwdPopupEnabled;
        storeSkinOnAPerViewBasis = microworldSettings.storeSkinOnAPerViewBasis;
        microworldNameUserDefined = microworldSettings.microworldNameUserDefined;

        mwdPrintAllowed = viewBaseSettings.mwdPrintAllowed;
        mwdPageSetupAllowed = viewBaseSettings.mwdPageSetupAllowed;
        viewCreationAllowed = microworldSettings.viewCreationAllowed;
        viewRemovalAllowed = microworldSettings.viewRemovalAllowed;
        viewRenameAllowed = microworldSettings.viewRenameAllowed;
        viewActivationAllowed = microworldSettings.viewActivationAllowed;
        viewMgmtAllowed = microworldSettings.viewMgmtAllowed;
        eslateOptionMgmtAllowed = microworldSettings.eslateOptionMgmtAllowed;
        componentPropertyMgmtAllowed = microworldSettings.componentPropertyMgmtAllowed;
        componentEventMgmtAllowed = microworldSettings.componentEventMgmtAllowed;
        componentSoundMgmtAllowed = microworldSettings.componentSoundMgmtAllowed;
        mwdLAFChangeAllowed = microworldSettings.mwdLAFChangeAllowed;
        consolesAllowed = microworldSettings.consolesAllowed;
        eslateComponentBarMenuEnabled = microworldSettings.eslateComponentBarMenuEnabled;
        eslateComponentBarEnabled = microworldSettings.eslateComponentBarEnabled;
        mwdNameChangeAllowed = microworldSettings.mwdNameChangeAllowed;
        performanceMgrActive = microworldSettings.performanceMgrActive;
        componentNameChangeAllowed = microworldSettings.componentNameChangeAllowed;
        componentBarMenuEnabled = microworldSettings.componentBarMenuEnabled;
        mwdLayerMgmtAllowed = microworldSettings.mwdLayerMgmtAllowed;
        gridMgmtAllowed = microworldSettings.gridMgmtAllowed;
        profileName = profName;
    }

    /** Creates a copy of the given MicroworldSettingsProfile */
    public MicroworldSettingsProfile(MicroworldSettingsProfile profile) {
        // COMPONENT BAR PROPERTIES
        minimizeButtonVisible = profile.minimizeButtonVisible;
        maximizeButtonVisible = profile.maximizeButtonVisible;
        closeButtonVisible = profile.closeButtonVisible;
        controlBarTitleActive = profile.controlBarTitleActive;
        helpButtonVisible = profile.helpButtonVisible;
        infoButtonVisible = profile.infoButtonVisible;
        plugButtonVisible = profile.plugButtonVisible;
        controlBarsVisible = profile.controlBarsVisible;

        // COMPONENT PROPERTIES
        resizeAllowed = profile.resizeAllowed;
        moveAllowed = profile.moveAllowed;
        outlineDragEnabled = profile.outlineDragEnabled;
//        nameChangeAllowed = profile.nameChangeAllowed;
        componentActivationMethodChangeAllowed = profile.componentActivationMethodChangeAllowed;
        activeComponentHighlighted = profile.activeComponentHighlighted;
        ensureActiveComponentVisible = profile.ensureActiveComponentVisible;
        componentPrintAllowed = profile.componentPrintAllowed;
        componentMinimizeAllowed = profile.componentMinimizeAllowed;
        componentMaximizeAllowed = profile.componentMaximizeAllowed;

        // MICROWORLD PROPERTIES
        componentInstantiationAllowed = profile.componentInstantiationAllowed;
        componentRemovalAllowed = profile.componentRemovalAllowed;
        mwdBgrdChangeAllowed = profile.mwdBgrdChangeAllowed;
        mwdStorageAllowed = profile.mwdStorageAllowed;
        mwdAutoBackupEnabled = profile.mwdAutoBackupEnabled;
        plugConnectionChangeAllowed = profile.plugConnectionChangeAllowed;
        microworldNameUserDefined = profile.microworldNameUserDefined;
        mwdPopupEnabled = profile.mwdPopupEnabled;
        menuBarVisible = profile.menuBarVisible;
        mwdResizable = profile.mwdResizable;
        mwdAutoScrollable = profile.mwdAutoScrollable;
        componentsMoveBeyondMwdBounds = profile.componentsMoveBeyondMwdBounds;
        mwdAutoExpandable = profile.mwdAutoExpandable;
        desktopDraggable = profile.desktopDraggable;
        menuSystemHeavyWeight = profile.menuSystemHeavyWeight;
        storeSkinOnAPerViewBasis = profile.storeSkinOnAPerViewBasis;
        horizontalScrollbarPolicy = profile.horizontalScrollbarPolicy;
        verticalScrollbarPolicy = profile.verticalScrollbarPolicy;

        mwdPrintAllowed = profile.mwdPrintAllowed;
        mwdPageSetupAllowed = profile.mwdPageSetupAllowed;
        viewCreationAllowed = profile.viewCreationAllowed;
        viewRemovalAllowed = profile.viewRemovalAllowed;
        viewRenameAllowed = profile.viewRenameAllowed;
        viewActivationAllowed = profile.viewActivationAllowed;
        viewMgmtAllowed = profile.viewMgmtAllowed;
        eslateOptionMgmtAllowed = profile.eslateOptionMgmtAllowed;
        componentPropertyMgmtAllowed = profile.componentPropertyMgmtAllowed;
        componentEventMgmtAllowed = profile.componentEventMgmtAllowed;
        componentSoundMgmtAllowed = profile.componentSoundMgmtAllowed;
        mwdLAFChangeAllowed = profile.mwdLAFChangeAllowed;
        consolesAllowed = profile.consolesAllowed;
        eslateComponentBarMenuEnabled = profile.eslateComponentBarMenuEnabled;
        eslateComponentBarEnabled = profile.eslateComponentBarEnabled;
        mwdNameChangeAllowed = profile.mwdNameChangeAllowed;
        performanceMgrActive = profile.performanceMgrActive;
        componentNameChangeAllowed = profile.componentNameChangeAllowed;
        componentBarMenuEnabled = profile.componentBarMenuEnabled;
        mwdLayerMgmtAllowed = profile.mwdLayerMgmtAllowed;
        gridMgmtAllowed = profile.gridMgmtAllowed;

        profileName = profile.getName();
    }

    public void setName(String name) {
        profileName = name;
    }

    public String getName() {
        return profileName;
    }

    public static MicroworldSettingsProfile getRuntimeProfile() {
        if (runtimeProfile == null)
            runtimeProfile = new RuntimeSettingsProfile();

        return new MicroworldSettingsProfile(runtimeProfile);
    }

    public static MicroworldSettingsProfile getDesignTimeProfile() {
        MicroworldSettingsProfile profile = new MicroworldSettingsProfile();
        ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ProfileDialogBundle", Locale.getDefault());
        profile.profileName = bundle.getString("Design-time Profile");
        return profile;
    }

    public void store(OutputStream out, String header) throws IOException {
        TreeMap microworldSettings = new TreeMap(Collator.getInstance());
        TreeMap componentSettings = new TreeMap(Collator.getInstance());
        TreeMap componentBarSettings = new TreeMap(Collator.getInstance());

        ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MicroworldPropertiesDialogBundle", Locale.getDefault());
        String microworldProp = bundle.getString("MicroworldPanel") + "-";
        String componentProp = bundle.getString("ComponentPanel") + "-";
        String componentBarProp = bundle.getString("ControlBar") + "-";

        // MICROWORLD PROPERTIES
        microworldSettings.put(microworldProp + bundle.getString("componentInstantiationAllowed"), new Boolean(componentInstantiationAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentRemovalAllowed"), new Boolean(componentRemovalAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdBgrdChangeAllowed"), new Boolean(mwdBgrdChangeAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdStorageAllowed"), new Boolean(mwdStorageAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdAutoBackupEnabled"), new Boolean(mwdAutoBackupEnabled).toString());
        microworldSettings.put(microworldProp + bundle.getString("plugConnectionChangeAllowed"), new Boolean(plugConnectionChangeAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("microworldNameUserDefined"), new Boolean(microworldNameUserDefined).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdPopupEnabled"), new Boolean(mwdPopupEnabled).toString());
        microworldSettings.put(microworldProp + bundle.getString("menuBarVisible"), new Boolean(menuBarVisible).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdResizable"), new Boolean(mwdResizable).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdAutoScrollable"), new Boolean(mwdAutoScrollable).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentsMoveBeyondMwdBounds"), new Boolean(componentsMoveBeyondMwdBounds).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdAutoExpandable"), new Boolean(mwdAutoExpandable).toString());
        microworldSettings.put(microworldProp + bundle.getString("desktopDraggable"), new Boolean(desktopDraggable).toString());
        microworldSettings.put(microworldProp + bundle.getString("menuSystemHeavyWeight"), new Boolean(menuSystemHeavyWeight).toString());
        microworldSettings.put(microworldProp + bundle.getString("storeSkinOnAPerViewBasis"), new Boolean(storeSkinOnAPerViewBasis).toString());
        String horizontalSPValue = bundle.getString("ScrollBarAsNeeded");
        if (horizontalScrollbarPolicy == JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)
            horizontalSPValue = bundle.getString("ScrollBarAlways");
        else if (horizontalScrollbarPolicy == JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
            horizontalSPValue = bundle.getString("ScrollBarNever");
        microworldSettings.put(microworldProp + bundle.getString("horizontalScrollbarPolicy"), horizontalSPValue);
        String verticalSPValue = bundle.getString("ScrollBarAsNeeded");
        if (verticalScrollbarPolicy == javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS)
            verticalSPValue = bundle.getString("ScrollBarAlways");
        else if (verticalScrollbarPolicy == javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER)
            verticalSPValue = bundle.getString("ScrollBarNever");
        microworldSettings.put(microworldProp + bundle.getString("verticalScrollbarPolicy"), verticalSPValue);
        microworldSettings.put(microworldProp + bundle.getString("mwdPrintAllowed"), new Boolean(mwdPrintAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdPageSetupAllowed"), new Boolean(mwdPageSetupAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("viewCreationAllowed"), new Boolean(viewCreationAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("viewRemovalAllowed"), new Boolean(viewRemovalAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("viewRenameAllowed"), new Boolean(viewRenameAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("viewActivationAllowed"), new Boolean(viewActivationAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("viewMgmtAllowed"), new Boolean(viewMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("eslateOptionMgmtAllowed"), new Boolean(eslateOptionMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentPropertyMgmtAllowed"), new Boolean(componentPropertyMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentEventMgmtAllowed"), new Boolean(componentEventMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentSoundMgmtAllowed"), new Boolean(componentSoundMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdLAFChangeAllowed"), new Boolean(mwdLAFChangeAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("consolesAllowed"), new Boolean(consolesAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("eslateComponentBarMenuEnabled"), new Boolean(eslateComponentBarMenuEnabled).toString());
        microworldSettings.put(microworldProp + bundle.getString("eslateComponentBarEnabled"), new Boolean(eslateComponentBarEnabled).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdNameChangeAllowed"), new Boolean(mwdNameChangeAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentNameChangeAllowed"), new Boolean(componentNameChangeAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("componentBarMenuEnabled"), new Boolean(componentBarMenuEnabled).toString());
        microworldSettings.put(microworldProp + bundle.getString("mwdLayerMgmtAllowed"), new Boolean(mwdLayerMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("gridMgmtAllowed"), new Boolean(gridMgmtAllowed).toString());
        microworldSettings.put(microworldProp + bundle.getString("performanceMgrActive"), new Boolean(performanceMgrActive).toString());

        // COMPONENT PROPERTIES
        componentSettings.put(componentProp + bundle.getString("resizeAllowed"), new Boolean(resizeAllowed).toString());
        componentSettings.put(componentProp + bundle.getString("moveAllowed"), new Boolean(moveAllowed).toString());
        componentSettings.put(componentProp + bundle.getString("outlineDragEnabled"), new Boolean(outlineDragEnabled).toString());
        componentSettings.put(componentProp + bundle.getString("componentActivationMethodChangeAllowed"), new Boolean(componentActivationMethodChangeAllowed).toString());
        componentSettings.put(componentProp + bundle.getString("activeComponentHighlighted"), new Boolean(activeComponentHighlighted).toString());
        componentSettings.put(componentProp + bundle.getString("ensureActiveComponentVisible"), new Boolean(ensureActiveComponentVisible).toString());
        componentSettings.put(componentProp + bundle.getString("componentPrintAllowed"), new Boolean(componentPrintAllowed).toString());
        componentSettings.put(componentProp + bundle.getString("componentMinimizeAllowed"), new Boolean(componentMinimizeAllowed).toString());
        componentSettings.put(componentProp + bundle.getString("componentMaximizeAllowed"), new Boolean(componentMaximizeAllowed).toString());

        // COMPONENT BAR PROPERTIES
        componentBarSettings.put(componentBarProp + bundle.getString("minimizeButtonVisible"), new Boolean(minimizeButtonVisible).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("maximizeButtonVisible"), new Boolean(maximizeButtonVisible).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("closeButtonVisible"), new Boolean(closeButtonVisible).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("controlBarTitleActive"), new Boolean(controlBarTitleActive).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("helpButtonVisible"), new Boolean(helpButtonVisible).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("infoButtonVisible"), new Boolean(infoButtonVisible).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("plugButtonVisible"), new Boolean(plugButtonVisible).toString());
        componentBarSettings.put(componentBarProp + bundle.getString("controlBarsVisible"), new Boolean(controlBarsVisible).toString());


        PrintWriter pw = new PrintWriter(new BufferedOutputStream(out));
        StringBuffer buff = new StringBuffer();
        pw.println('#' + header);
        pw.println('#' + new java.util.Date().toString());
//        Enumeration enum = propertyNames();
        Iterator iter = microworldSettings.keySet().iterator();
        while (iter.hasNext()) {
            String propName = (String) iter.next();
            String value = (String) microworldSettings.get(propName);
            buff.append(propName);
            buff.append('=');
            buff.append(value);
            pw.println(buff.toString());
            buff.setLength(0);
        }
        pw.println();
        iter = componentSettings.keySet().iterator();
        while (iter.hasNext()) {
            String propName = (String) iter.next();
            String value = (String) componentSettings.get(propName);
            buff.append(propName);
            buff.append('=');
            buff.append(value);
            pw.println(buff.toString());
            buff.setLength(0);
        }
        pw.println();
        iter = componentBarSettings.keySet().iterator();
        while (iter.hasNext()) {
            String propName = (String) iter.next();
            String value = (String) componentBarSettings.get(propName);
            buff.append(propName);
            buff.append('=');
            buff.append(value);
            pw.println(buff.toString());
            buff.setLength(0);
        }
        pw.flush();
        pw.close();
    }

    public synchronized void load(InputStream inStream) throws IOException {
        HashMap settings = new HashMap();
        readSettings(inStream, settings);

//System.out.println("Read in settings: ");
//Object[] keys = settings.keySet().toArray();
//for (int i=0; i<keys.length; i++)
//    System.out.println("key: " + keys[i] + ", value: " + settings.get(keys[i]));

        ResourceBundle bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MicroworldPropertiesDialogBundle", Locale.getDefault());
        String microworldProp = bundle.getString("MicroworldPanel") + "-";
        String componentProp = bundle.getString("ComponentPanel") + "-";
        String componentBarProp = bundle.getString("ControlBar") + "-";

        // MICROWORLD PROPERTIES
        String value = (String) settings.get(microworldProp + bundle.getString("componentInstantiationAllowed"));
        if (value != null)
            componentInstantiationAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentInstantiationAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("componentRemovalAllowed"));
        if (value != null)
            componentRemovalAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentRemovalAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("mwdBgrdChangeAllowed"));
        if (value != null)
            mwdBgrdChangeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdBgrdChangeAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("mwdStorageAllowed"));
        if (value != null)
            mwdStorageAllowed = new Boolean(value.toLowerCase()).booleanValue();
        value = (String) settings.get(microworldProp + bundle.getString("mwdAutoBackupEnabled"));
        if (value != null)
            mwdAutoBackupEnabled = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdStorageAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("plugConnectionChangeAllowed"));
        if (value != null)
            plugConnectionChangeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find plugConnectionChangeAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("microworldNameUserDefined"));
        if (value != null)
            microworldNameUserDefined = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find microworldNameUserDefined");
        value = (String) settings.get(microworldProp + bundle.getString("mwdPopupEnabled"));
        if (value != null)
            mwdPopupEnabled = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdPopupEnabled");
        value = (String) settings.get(microworldProp + bundle.getString("menuBarVisible"));
        if (value != null)
            menuBarVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find menuBarVisible");
        value = (String) settings.get(microworldProp + bundle.getString("mwdResizable"));
        if (value != null)
            mwdResizable = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdResizable");
        value = (String) settings.get(microworldProp + bundle.getString("mwdAutoScrollable"));
        if (value != null)
            mwdAutoScrollable = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdAutoScrollable");
        value = (String) settings.get(microworldProp + bundle.getString("componentsMoveBeyondMwdBounds"));
        if (value != null)
            componentsMoveBeyondMwdBounds = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentsMoveBeyondMwdBounds");
        value = (String) settings.get(microworldProp + bundle.getString("mwdAutoExpandable"));
        if (value != null)
            mwdAutoExpandable = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdAutoExpandable");
        value = (String) settings.get(microworldProp + bundle.getString("desktopDraggable"));
        if (value != null)
            desktopDraggable = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find desktopDraggable");
        value = (String) settings.get(microworldProp + bundle.getString("menuSystemHeavyWeight"));
        if (value != null)
            menuSystemHeavyWeight = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find menuSystemHeavyWeight");
        value = (String) settings.get(microworldProp + bundle.getString("storeSkinOnAPerViewBasis"));
        if (value != null)
            storeSkinOnAPerViewBasis = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find storeSkinOnAPerViewBasis");
        value = (String) settings.get(microworldProp + bundle.getString("horizontalScrollbarPolicy"));
        if (value != null) {
            horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
            if (value.equals(bundle.getString("ScrollBarAlways")))
                horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
            else if (value.equals(bundle.getString("ScrollBarNever")))
                horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        }
        value = (String) settings.get(microworldProp + bundle.getString("verticalScrollbarPolicy"));
        if (value != null) {
            verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
            if (value.equals(bundle.getString("ScrollBarAlways")))
                verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
            else if (value.equals(bundle.getString("ScrollBarNever")))
                verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
        }
        value = (String) settings.get(microworldProp + bundle.getString("mwdPrintAllowed"));
        if (value != null)
            mwdPrintAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdPrintAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("mwdPageSetupAllowed"));
        if (value != null)
            mwdPageSetupAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdPageSetupAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("viewCreationAllowed"));
        if (value != null)
            viewCreationAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find viewCreationAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("viewRemovalAllowed"));
        if (value != null)
            viewRemovalAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find viewRemovalAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("viewRenameAllowed"));
        if (value != null)
            viewRenameAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find viewRenameAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("viewActivationAllowed"));
        if (value != null)
            viewActivationAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find viewActivationAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("viewMgmtAllowed"));
        if (value != null)
            viewMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find viewMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("eslateOptionMgmtAllowed"));
        if (value != null)
            eslateOptionMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find eslateOptionMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("componentPropertyMgmtAllowed"));
        if (value != null)
            componentPropertyMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentPropertyMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("componentEventMgmtAllowed"));
        if (value != null)
            componentEventMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentEventMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("componentSoundMgmtAllowed"));
        if (value != null)
            componentSoundMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentSoundMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("mwdLAFChangeAllowed"));
        if (value != null)
            mwdLAFChangeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdLAFChangeAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("consolesAllowed"));
        if (value != null)
            consolesAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find consolesAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("eslateComponentBarMenuEnabled"));
        if (value != null)
            eslateComponentBarMenuEnabled = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find eslateComponentBarMenuEnabled");
        value = (String) settings.get(microworldProp + bundle.getString("eslateComponentBarEnabled"));
        if (value != null)
            eslateComponentBarEnabled = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find eslateComponentBarEnabled");
        value = (String) settings.get(microworldProp + bundle.getString("mwdNameChangeAllowed"));
        if (value != null)
            mwdNameChangeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdNameChangeAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("componentNameChangeAllowed"));
        if (value != null)
            componentNameChangeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentNameChangeAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("componentBarMenuEnabled"));
        if (value != null)
            componentBarMenuEnabled = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentBarMenuEnabled");
        value = (String) settings.get(microworldProp + bundle.getString("mwdLayerMgmtAllowed"));
        if (value != null)
            mwdLayerMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find mwdLayerMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("gridMgmtAllowed"));
        if (value != null)
            gridMgmtAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find gridMgmtAllowed");
        value = (String) settings.get(microworldProp + bundle.getString("performanceMgrActive"));
        if (value != null)
            performanceMgrActive = new Boolean(value.toLowerCase()).booleanValue();

        // COMPONENT PROPERTIES
        value = (String) settings.get(componentProp + bundle.getString("resizeAllowed"));
        if (value != null)
            resizeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find resizeAllowed");
        value = (String) settings.get(componentProp + bundle.getString("moveAllowed"));
        if (value != null)
            moveAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find moveAllowed");
        value = (String) settings.get(componentProp + bundle.getString("outlineDragEnabled"));
        if (value != null)
            outlineDragEnabled = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find outlineDragEnabled");
        value = (String) settings.get(componentProp + bundle.getString("componentActivationMethodChangeAllowed"));
        if (value != null)
            componentActivationMethodChangeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentActivationMethodChangeAllowed");
        value = (String) settings.get(componentProp + bundle.getString("activeComponentHighlighted"));
        if (value != null)
            activeComponentHighlighted = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find activeComponentHighlighted");
        value = (String) settings.get(componentProp + bundle.getString("ensureActiveComponentVisible"));
        if (value != null)
            ensureActiveComponentVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find ensureActiveComponentVisible");
        value = (String) settings.get(componentProp + bundle.getString("componentPrintAllowed"));
        if (value != null)
            componentPrintAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentPrintAllowed");
        value = (String) settings.get(componentProp + bundle.getString("componentMinimizeAllowed"));
        if (value != null)
            componentMinimizeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentMinimizeAllowed");
        value = (String) settings.get(componentProp + bundle.getString("componentMaximizeAllowed"));
        if (value != null)
            componentMaximizeAllowed = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find componentMaximizeAllowed");

        // COMPONENT BAR PROPERTIES
        value = (String) settings.get(componentBarProp + bundle.getString("minimizeButtonVisible"));
        if (value != null)
            minimizeButtonVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find minimizeButtonVisible");
        value = (String) settings.get(componentBarProp + bundle.getString("maximizeButtonVisible"));
        if (value != null)
            maximizeButtonVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find maximizeButtonVisible");
        value = (String) settings.get(componentBarProp + bundle.getString("closeButtonVisible"));
        if (value != null)
            closeButtonVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find closeButtonVisible");
        value = (String) settings.get(componentBarProp + bundle.getString("controlBarTitleActive"));
//System.out.println("value: " + value);
        if (value != null)
            controlBarTitleActive = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find controlBarTitleActive");
        value = (String) settings.get(componentBarProp + bundle.getString("helpButtonVisible"));
        if (value != null)
            helpButtonVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find helpButtonVisible");
        value = (String) settings.get(componentBarProp + bundle.getString("infoButtonVisible"));
        if (value != null)
            infoButtonVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find infoButtonVisible");
        value = (String) settings.get(componentBarProp + bundle.getString("plugButtonVisible"));
        if (value != null)
            plugButtonVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find plugButtonVisible");
        value = (String) settings.get(componentBarProp + bundle.getString("controlBarsVisible"));
        if (value != null)
            controlBarsVisible = new Boolean(value.toLowerCase()).booleanValue();
//        else System.out.println("Cound not find controlBarsVisible");
    }

    private void readSettings(InputStream inStream, HashMap settings) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream)); //, "8859_1"

        /* Read the header of the properties file */
        String line = in.readLine();
        if (line == null)
            return;
        if (line.length() > 1)
            profileName = line.substring(1).trim();

	while (true) {
            // Get next line
            line = in.readLine();
            if (line == null)
                return;

            if (line.length() > 0) {
                // Continue lines that end in slashes if they are not comments
                char firstChar = line.charAt(0);
                if ((firstChar != '#') && (firstChar != '!')) {
                    while (continueLine(line)) {
                        String nextLine = in.readLine();
                        if(nextLine == null)
                            nextLine = new String("");
                        String loppedLine = line.substring(0, line.length()-1);
                        // Advance beyond whitespace on new line
                        int startIndex=0;
                        for(startIndex=0; startIndex<nextLine.length(); startIndex++)
                            if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1)
                                break;
                        nextLine = nextLine.substring(startIndex,nextLine.length());
                        line = new String(loppedLine+nextLine);
                    }

                    // Find start of key
                    int len = line.length();
                    int keyStart;
                    for(keyStart=0; keyStart<len; keyStart++) {
                        if(whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
                            break;
                    }

                    // Blank lines are ignored
                    if (keyStart == len)
                        continue;

                    // Find separation between key and value
                    int separatorIndex;
                    for(separatorIndex=keyStart; separatorIndex<len; separatorIndex++) {
                        char currentChar = line.charAt(separatorIndex);
                        if (currentChar == '\\')
                            separatorIndex++;
                        else if(keyValueSeparators.indexOf(currentChar) != -1)
                            break;
                    }

                    // Skip over whitespace after key if any
                    int valueIndex;
                    for (valueIndex=separatorIndex; valueIndex<len; valueIndex++)
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;

                    // Skip over one non whitespace key value separators if any
                    if (valueIndex < len)
                        if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1)
                            valueIndex++;

                    // Skip over white space after other separators if any
                    while (valueIndex < len) {
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;
                        valueIndex++;
                    }
                    String key = line.substring(keyStart, separatorIndex);
                    String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

                    // Convert then store key and value
                    key = loadConvert(key);
                    value = loadConvert(value);
                    settings.put(key, value);
                }
            }
	}
    }
    /*
     * Returns true if the given line is a line that must
     * be appended to the next line
     */
    private boolean continueLine (String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while((index >= 0) && (line.charAt(index--) == '\\'))
            slashCount++;
        return (slashCount % 2 == 1);
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars
     * and changes special saved chars to their original forms
     */
    private String loadConvert (String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for(int x=0; x<len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if(aChar == 'u') {
                    // Read the xxxx
                    int value=0;
		    for (int i=0; i<4; i++) {
		        aChar = theString.charAt(x++);
		        switch (aChar) {
		          case '0': case '1': case '2': case '3': case '4':
		          case '5': case '6': case '7': case '8': case '9':
		             value = (value << 4) + aChar - '0';
			     break;
			  case 'a': case 'b': case 'c':
                          case 'd': case 'e': case 'f':
			     value = (value << 4) + 10 + aChar - 'a';
			     break;
			  case 'A': case 'B': case 'C':
                          case 'D': case 'E': case 'F':
			     value = (value << 4) + 10 + aChar - 'A';
			     break;
			  default:
                              throw new IllegalArgumentException(
                                           "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char)value);
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("[Profile Name \"");
        buff.append(profileName + "\" " + super.toString());
        buff.append("] [componentInstantiationAllowed ");
        buff.append(componentInstantiationAllowed);
        buff.append("] [componentRemovalAllowed ");
        buff.append(componentRemovalAllowed);
        buff.append("] [mwdBgrdChangeAllowed ");
        buff.append(mwdBgrdChangeAllowed);
        buff.append("] [mwdStorageAllowed ");
        buff.append(mwdStorageAllowed);
        buff.append("] [mwdAutoBackupEnabled ");
        buff.append(mwdAutoBackupEnabled);
        buff.append("] [mwdPinViewEnabled ");
        buff.append(plugConnectionChangeAllowed);
        buff.append("] [microworldNameUserDefined ");
        buff.append(microworldNameUserDefined);
        buff.append("] [mwdPopupEnabled ");
        buff.append(mwdPopupEnabled);
        buff.append("] [menuBarVisible ");
        buff.append(menuBarVisible);
        buff.append("] [mwdResizable ");
        buff.append(mwdResizable);
        buff.append("] [mwdAutoScrollable ");
        buff.append(mwdAutoScrollable);
        buff.append("] [componentsMoveBeyondMwdBounds ");
        buff.append(componentsMoveBeyondMwdBounds);
        buff.append("] [mwdAutoExpandable ");
        buff.append(mwdAutoExpandable);
        buff.append("] [desktopDraggable ");
        buff.append(desktopDraggable);
        buff.append("] [menuSystemHeavyWeight ");
        buff.append(menuSystemHeavyWeight);
        buff.append("] [storeSkinOnAPerViewBasis ");
        buff.append(storeSkinOnAPerViewBasis);
        buff.append("] [horizontalScrollbarPolicy ");
        buff.append(horizontalScrollbarPolicy);
        buff.append("] [verticalScrollbarPolicy ");
        buff.append(verticalScrollbarPolicy);
        buff.append("] [mwdPrintAllowed ");
        buff.append(mwdPrintAllowed);
        buff.append("] [mwdPageSetupAllowed ");
        buff.append(mwdPageSetupAllowed);
        buff.append("] [viewCreationAllowed ");
        buff.append(viewCreationAllowed);
        buff.append("] [viewRemovalAllowed ");
        buff.append(viewRemovalAllowed);
        buff.append("] [viewRenameAllowed ");
        buff.append(viewRenameAllowed);
        buff.append("] [viewActivationAllowed ");
        buff.append(viewActivationAllowed);
        buff.append("] [viewMgmtAllowed ");
        buff.append(viewMgmtAllowed);
        buff.append("] [eslateOptionMgmtAllowed ");
        buff.append(eslateOptionMgmtAllowed);
        buff.append("] [componentPropertyMgmtAllowed ");
        buff.append(componentPropertyMgmtAllowed);
        buff.append("] [componentEventMgmtAllowed ");
        buff.append(componentEventMgmtAllowed);
        buff.append("] [componentSoundMgmtAllowed ");
        buff.append(componentSoundMgmtAllowed);
        buff.append("] [mwdLAFChangeAllowed ");
        buff.append(mwdLAFChangeAllowed);
        buff.append("] [consolesAllowed ");
        buff.append(consolesAllowed);
        buff.append("] [eslateComponentBarMenuEnabled ");
        buff.append(eslateComponentBarMenuEnabled);
        buff.append("] [eslateComponentBarEnabled ");
        buff.append(eslateComponentBarEnabled);
        buff.append("] [mwdNameChangeAllowed ");
        buff.append(mwdNameChangeAllowed);
        buff.append("] [componentNameChangeAllowed ");
        buff.append(componentNameChangeAllowed);
        buff.append("] [componentBarMenuEnabled ");
        buff.append(componentBarMenuEnabled);
        buff.append("] [mwdLayerMgmtAllowed ");
        buff.append(mwdLayerMgmtAllowed);
        buff.append("] [gridMgmtAllowed ");
        buff.append(gridMgmtAllowed);
        buff.append("] [performanceMgrActive ");
        buff.append(performanceMgrActive);
        buff.append("] ----- ");


        // COMPONENT PROPERTIES
        buff.append("[resizeAllowed ");
        buff.append(resizeAllowed);
        buff.append("] [moveAllowed ");
        buff.append(moveAllowed);
        buff.append("] [outlineDragEnabled ");
        buff.append(outlineDragEnabled);
        buff.append("] [componentActivationMethodChangeAllowed ");
        buff.append(componentActivationMethodChangeAllowed);
        buff.append("] [activeComponentHighlighted ");
        buff.append(activeComponentHighlighted);
        buff.append("] [ensureActiveComponentVisible ");
        buff.append(ensureActiveComponentVisible);
        buff.append("] [componentPrintAllowed ");
        buff.append(componentPrintAllowed);
        buff.append("] [componentMinimizeAllowed ");
        buff.append(componentMinimizeAllowed);
        buff.append("] [componentMaximizeAllowed ");
        buff.append(componentMaximizeAllowed);
        buff.append("] ----- ");

        // COMPONENT BAR PROPERTIES
        buff.append("[minimizeAllowed ");
        buff.append(minimizeButtonVisible);
        buff.append("] [maximizeAllowed ");
        buff.append(maximizeButtonVisible);
        buff.append("] [closeAllowed ");
        buff.append(closeButtonVisible);
        buff.append("] [controlBarTitleActive ");
        buff.append(controlBarTitleActive);
        buff.append("] [helpButtonVisible ");
        buff.append(helpButtonVisible);
        buff.append("] [infoButtonVisible ");
        buff.append(infoButtonVisible);
        buff.append("] [plugButtonVisible ");
        buff.append(plugButtonVisible);
        buff.append("] [controlBarsVisible ");
        buff.append(controlBarsVisible);
        buff.append(']');

        return buff.toString();
    }
}

class RuntimeSettingsProfile extends MicroworldSettingsProfile {
    RuntimeSettingsProfile() {
        // COMPONENT BAR PROPERTIES
        minimizeButtonVisible = true;
        maximizeButtonVisible = true;
        closeButtonVisible = true;
        controlBarTitleActive = false;
        helpButtonVisible = false;
        infoButtonVisible = false;
        plugButtonVisible = false;
        controlBarsVisible = false;

        // COMPONENT PROPERTIES
        resizeAllowed = true;
        moveAllowed = true;
        outlineDragEnabled = false;
//        nameChangeAllowed = false; // component renaming allowed
        componentActivationMethodChangeAllowed = false;
        activeComponentHighlighted = false;
        ensureActiveComponentVisible = false;
        componentPrintAllowed = true;
        componentMinimizeAllowed = true;
        componentMaximizeAllowed = true;

        // MICROWORLD PROPERTIES
        componentInstantiationAllowed = false;
        componentRemovalAllowed = false;
        mwdBgrdChangeAllowed = false;
        mwdStorageAllowed = true;
        mwdAutoBackupEnabled = false;
        plugConnectionChangeAllowed = false;
        microworldNameUserDefined = true;
        mwdPopupEnabled = false;
        menuBarVisible = false;
        mwdResizable = true;
        mwdAutoScrollable = false;
        componentsMoveBeyondMwdBounds = true;
        mwdAutoExpandable = false;
        desktopDraggable = false;
        menuSystemHeavyWeight = false;
        storeSkinOnAPerViewBasis = false;
        horizontalScrollbarPolicy = javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        verticalScrollbarPolicy = javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER;

        mwdPrintAllowed = true;
        mwdPageSetupAllowed = true;
        viewCreationAllowed = false;
        viewRemovalAllowed = false;
        viewRenameAllowed = false;
        viewActivationAllowed = false;
        viewMgmtAllowed = false;
        eslateOptionMgmtAllowed = false;
        componentPropertyMgmtAllowed = false;
        componentEventMgmtAllowed = false;
        componentSoundMgmtAllowed = false;
        mwdLAFChangeAllowed = false;
        consolesAllowed = true;
        eslateComponentBarMenuEnabled = false;
        eslateComponentBarEnabled = false;
        mwdNameChangeAllowed = false;
        componentNameChangeAllowed = false;
        componentBarMenuEnabled = false;
        mwdLayerMgmtAllowed = false;
        gridMgmtAllowed = false;
        performanceMgrActive = false;

        profileName = ResourceBundle.getBundle("gr.cti.eslate.base.container.ProfileDialogBundle", Locale.getDefault()).getString("Runtime Profile");
    }
}
