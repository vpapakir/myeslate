package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


public class MwdPropertiesDialog2 extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    public static final int V_GAP = 5;
    public static final int H_GAP = 5;
    public static final int FIELD_HEIGHT = 25;
    protected int returnCode = DIALOG_CANCELLED;
    String[] scrollBarPolicies = new String[3];

    Locale locale;
    ResourceBundle mwdPropertiesDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;

    MetadataPanel metadataPanel;
    JCheckBox minimizeButtonVisibleBox, closeButtonVisibleBox, maximizeButtonVisibleBox;
    JCheckBox barVisibleBox, componentNameBox;
    JCheckBox helpBox, infoBox, pinBox, resizeBox, moveBox, barRenameBox, activateBox; //freezeBox
    JCheckBox newComponentBox, destroyComponentBox, backgroundChangeBox, storeBox, pinViewBox;
    JCheckBox mwdNameUserDefinedBox, mwdPopupBox, menuBarVisibleBox, outlineDragBox, componentBarBox;
    SpinField widthPanel, heightPanel;
    JCheckBox mwdResizable, mwdAutoExpandable, mwdAutoScrollable, mwdBoundaries, desktopDrag;
    JCheckBox menuSystemHeavyWeight, storeSkinsPerView;
    JCheckBox displayActiveComponentBox, ensureActiveFrameVisibleBox;
    JComboBox verticalSPBox, horizontalSPBox;
    JLabel verticalSPLabel, horizontalSPLabel;

    boolean viewCreationAllowed = true, viewRemovalAllowed = true, viewRenameAllowed = true;
    boolean mwdPrintAllowed = true, viewManagementAllowed = true, eslateOptionManagementAllowed = true;
    boolean componentPropertyMgmtAllowed = true, componentEventMgmtAllowed = true;
    boolean componentSoundMgmtAllowed = true, mwdLAFChangeAllowed = true, consolesAllowed = true;
    boolean componentControlBarMenuEnabled = true, componentBarEnabled = true;
    boolean mwdNameChangeAllowed = true;

    MicroworldSettingsProfile currentProfile = null;
    /** The user-made profiles. These standard profiles are the design-time, the runtime
     *  and the currentProfile.
     */
    MicroworldSettingsProfile[] customProfiles = null;
    ESlateContainer container;
    JButton okButton, profileButton;
    /** When locked the dialog's settings are not editable.
     */
    private boolean isLocked = false;

    public MwdPropertiesDialog2(java.awt.Frame parentFrame, ESlateContainer cont, MicroworldSettingsProfile profile) {
        super(parentFrame, true);
        this.container = cont;
        if (profile == null)
            throw new NullPointerException("Null MicroworldSettingsProfile was supplied");
        this.currentProfile = profile;

        locale = Locale.getDefault();
        mwdPropertiesDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MwdPropertiesDialogBundle", locale);
        if (mwdPropertiesDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.MwdPropertiesDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(mwdPropertiesDialogBundle.getString("Title"));

        scrollBarPolicies[0] = mwdPropertiesDialogBundle.getString("ScrollBarAsNeeded");
        scrollBarPolicies[1] = mwdPropertiesDialogBundle.getString("ScrollBarNever");
        scrollBarPolicies[2] = mwdPropertiesDialogBundle.getString("ScrollBarAlways");

        minimizeButtonVisibleBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MinimizeAllowed"));
        maximizeButtonVisibleBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MaximizeAllowed"));
        closeButtonVisibleBox = new JCheckBox(mwdPropertiesDialogBundle.getString("CloseControlEnabled"));
        barVisibleBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentBarVisible"));
        barRenameBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentRenameThroughBarEnabled"));
        helpBox = new JCheckBox(mwdPropertiesDialogBundle.getString("HelpEnabled"));
        infoBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentInfoEnabled"));
        pinBox = new JCheckBox(mwdPropertiesDialogBundle.getString("PinControlEnabled"));

        JPanel controlBarPropertiesPanel = new JPanel();
        ExplicitLayout el3 = new ExplicitLayout();
        controlBarPropertiesPanel.setLayout(el3);

        Expression fieldHeight = MathEF.constant(FIELD_HEIGHT);
        ExplicitConstraints ec31 = new ExplicitConstraints(minimizeButtonVisibleBox);
        ec31.setX(ContainerEF.left(controlBarPropertiesPanel)); ec31.setY(ContainerEF.top(controlBarPropertiesPanel));
        ec31.setHeight(fieldHeight); ec31.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec32 = new ExplicitConstraints(maximizeButtonVisibleBox);
        ec32.setX(ContainerEF.left(controlBarPropertiesPanel)); ec32.setY(ComponentEF.bottom(minimizeButtonVisibleBox));
        ec32.setHeight(fieldHeight); ec32.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec33 = new ExplicitConstraints(closeButtonVisibleBox);
        ec33.setX(ContainerEF.left(controlBarPropertiesPanel)); ec33.setY(ComponentEF.bottom(maximizeButtonVisibleBox));
        ec33.setHeight(fieldHeight); ec33.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec34 = new ExplicitConstraints(barVisibleBox);
        ec34.setX(ContainerEF.left(controlBarPropertiesPanel)); ec34.setY(ComponentEF.bottom(closeButtonVisibleBox));
        ec34.setHeight(fieldHeight); ec34.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec35 = new ExplicitConstraints(barRenameBox);
        ec35.setX(ContainerEF.left(controlBarPropertiesPanel)); ec35.setY(ComponentEF.bottom(barVisibleBox));
        ec35.setHeight(fieldHeight); ec35.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec36 = new ExplicitConstraints(helpBox);
        ec36.setX(ContainerEF.left(controlBarPropertiesPanel)); ec36.setY(ComponentEF.bottom(barRenameBox));
        ec36.setHeight(fieldHeight); ec36.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec37 = new ExplicitConstraints(infoBox);
        ec37.setX(ContainerEF.left(controlBarPropertiesPanel)); ec37.setY(ComponentEF.bottom(helpBox));
        ec37.setHeight(fieldHeight); ec37.setWidth(ContainerEF.width(controlBarPropertiesPanel));

        ExplicitConstraints ec38 = new ExplicitConstraints(pinBox);
        ec38.setX(ContainerEF.left(controlBarPropertiesPanel)); ec38.setY(ComponentEF.bottom(infoBox));
        ec38.setHeight(fieldHeight); ec38.setWidth(ContainerEF.width(controlBarPropertiesPanel));

/*        controlBarPropertiesPanel.setLayout(new BoxLayout(controlBarPropertiesPanel, BoxLayout.Y_AXIS));
        controlBarPropertiesPanel.add(barVisibleBox);
        controlBarPropertiesPanel.add(minimizeBox);
        controlBarPropertiesPanel.add(maximizeBox);
        controlBarPropertiesPanel.add(closeControlBox);
        controlBarPropertiesPanel.add(pinBox);
        controlBarPropertiesPanel.add(helpBox);
        controlBarPropertiesPanel.add(infoBox);
        controlBarPropertiesPanel.add(barRenameBox);
        controlBarPropertiesPanel.add(Box.createGlue());
*/
        controlBarPropertiesPanel.add(barVisibleBox, ec34);
        controlBarPropertiesPanel.add(minimizeButtonVisibleBox, ec31);
        controlBarPropertiesPanel.add(maximizeButtonVisibleBox, ec32);
        controlBarPropertiesPanel.add(closeButtonVisibleBox, ec33);
        controlBarPropertiesPanel.add(pinBox, ec38);
        controlBarPropertiesPanel.add(helpBox, ec36);
        controlBarPropertiesPanel.add(infoBox, ec37);
        controlBarPropertiesPanel.add(barRenameBox, ec35);

//        Expression maxWidthExp = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), controlBarPropertiesPanel.getComponents()));
        Expression maxWidthExp = GroupEF.preferredWidthMax(controlBarPropertiesPanel.getComponents());
        int maxWidth = (int) maxWidthExp.getValue(el3);
        controlBarPropertiesPanel.setPreferredSize(new Dimension(0, maxWidth));

//        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(mwdPropertiesDialogBundle.getString("ControlBarTitle"));
        tb1.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb1.setTitleFont(greekUIFont);
        controlBarPropertiesPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,5,5,0)));

        componentNameBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentNameChangeAllowed"));
        resizeBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ResizeAllowed"));
        moveBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MoveAllowed"));
//        freezeBox = new JCheckBox(mwdPropertiesDialogBundle.getString("Frozen"));
        activateBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ActivateOnMouseClick"));
        outlineDragBox = new JCheckBox(mwdPropertiesDialogBundle.getString("OutlineDrag"));
        outlineDragBox.setSelected(false);
        outlineDragBox.setEnabled(false);
        displayActiveComponentBox = new JCheckBox(mwdPropertiesDialogBundle.getString("DisplayActiveComponent"));
        displayActiveComponentBox.setToolTipText(mwdPropertiesDialogBundle.getString("DisplayActiveComponentTip"));
        ensureActiveFrameVisibleBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ActiveFrameVisible"));
        ensureActiveFrameVisibleBox.setToolTipText(mwdPropertiesDialogBundle.getString("ActiveFrameVisibleTip"));

        JPanel componentPropertiesPanel = new JPanel();
        ExplicitLayout el2 = new ExplicitLayout();
        componentPropertiesPanel.setLayout(el2);

        ExplicitConstraints ec21 = new ExplicitConstraints(componentNameBox);
        ec21.setX(ContainerEF.left(componentPropertiesPanel));
        ec21.setY(ContainerEF.top(componentPropertiesPanel));
        ec21.setHeight(fieldHeight);
        ec21.setWidth(ContainerEF.width(componentPropertiesPanel));

        ExplicitConstraints ec22 = new ExplicitConstraints(resizeBox);
        ec22.setX(ContainerEF.left(componentPropertiesPanel));
        ec22.setY(ComponentEF.bottom(componentNameBox));
        ec22.setHeight(fieldHeight);
        ec22.setWidth(ContainerEF.width(componentPropertiesPanel));

        ExplicitConstraints ec23 = new ExplicitConstraints(moveBox);
        ec23.setX(ContainerEF.left(componentPropertiesPanel));
        ec23.setY(ComponentEF.bottom(resizeBox));
        ec23.setHeight(fieldHeight);
        ec23.setWidth(ContainerEF.width(componentPropertiesPanel));

        ExplicitConstraints ec24 = new ExplicitConstraints(activateBox);
        ec24.setX(ContainerEF.left(componentPropertiesPanel));
        ec24.setY(ComponentEF.bottom(moveBox));
        ec24.setHeight(fieldHeight);
        ec24.setWidth(ContainerEF.width(componentPropertiesPanel));

        ExplicitConstraints ec25 = new ExplicitConstraints(outlineDragBox);
        ec25.setX(ContainerEF.left(componentPropertiesPanel));
        ec25.setY(ComponentEF.bottom(activateBox));
        ec25.setHeight(fieldHeight);
        ec25.setWidth(ContainerEF.width(componentPropertiesPanel));

        ExplicitConstraints ec26 = new ExplicitConstraints(displayActiveComponentBox);
        ec26.setX(ContainerEF.left(componentPropertiesPanel));
        ec26.setY(ComponentEF.bottom(outlineDragBox));
        ec26.setHeight(fieldHeight);
        ec26.setWidth(ContainerEF.width(componentPropertiesPanel));

        ExplicitConstraints ec27 = new ExplicitConstraints(ensureActiveFrameVisibleBox);
        ec27.setX(ContainerEF.left(componentPropertiesPanel));
        ec27.setY(ComponentEF.bottom(displayActiveComponentBox));
        ec27.setHeight(fieldHeight);
        ec27.setWidth(ContainerEF.width(componentPropertiesPanel));

        componentPropertiesPanel.add(componentNameBox, ec21);
        componentPropertiesPanel.add(resizeBox, ec22);
        componentPropertiesPanel.add(moveBox, ec23);
        componentPropertiesPanel.add(activateBox, ec24);
        componentPropertiesPanel.add(outlineDragBox, ec25);
        componentPropertiesPanel.add(displayActiveComponentBox, ec26);
        componentPropertiesPanel.add(ensureActiveFrameVisibleBox, ec27);

//        maxWidthExp = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), componentPropertiesPanel.getComponents()));
        maxWidthExp = GroupEF.preferredWidthMax(componentPropertiesPanel.getComponents());
        maxWidth = (int) maxWidthExp.getValue(el2);
        componentPropertiesPanel.setPreferredSize(new Dimension(0, maxWidth));

/*        componentPropertiesPanel.setLayout(new BoxLayout(componentPropertiesPanel, BoxLayout.Y_AXIS));
        componentPropertiesPanel.add(resizeBox);
        componentPropertiesPanel.add(moveBox);
//        componentPropertiesPanel.add(freezeBox);
        componentPropertiesPanel.add(activateBox);
        componentPropertiesPanel.add(componentNameBox);
        componentPropertiesPanel.add(outlineDragBox);
        componentPropertiesPanel.add(displayActiveComponentBox);
        componentPropertiesPanel.add(ensureActiveFrameVisibleBox);
*/
        TitledBorder tb2 = new TitledBorder(mwdPropertiesDialogBundle.getString("ComponentPanelTitle"));
        tb2.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb2.setTitleFont(greekUIFont);
        componentPropertiesPanel.setBorder(new CompoundBorder(tb2, new EmptyBorder(0,5,5,0)));

        JPanel mwdPropertiesPanel = createMicroworldPropertiesPanel();

//        metadataPanel = new MetadataPanel(container.microworld);


        JTabbedPane propertiesTab = new JTabbedPane();
        propertiesTab.addTab(mwdPropertiesDialogBundle.getString("Metadata"), metadataPanel);
        propertiesTab.addTab(mwdPropertiesDialogBundle.getString("MicroworldPanel"), mwdPropertiesPanel);
        propertiesTab.addTab(mwdPropertiesDialogBundle.getString("ComponentPanel"), componentPropertiesPanel);
        propertiesTab.addTab(mwdPropertiesDialogBundle.getString("ControlBar"), controlBarPropertiesPanel);
        propertiesTab.setBorder(null);

        // The button panel (OK, CANCEL)
        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton = new JButton(mwdPropertiesDialogBundle.getString("OK"));
//        if (localeIsGreek)
//            okButton.setFont(greekUIFont);
        okButton.setForeground(ESlateContainerUtils.color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(mwdPropertiesDialogBundle.getString("Cancel"));
//        if (localeIsGreek)
//            cancelButton.setFont(greekUIFont);
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JButton applyButton = new JButton(mwdPropertiesDialogBundle.getString("Apply"));
//        if (localeIsGreek)
//            applyButton.setFont(greekUIFont);
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

        profileButton = new JButton(mwdPropertiesDialogBundle.getString("Profiles"));
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

        mainPanel.add(propertiesTab);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        setContentPane(mainPanel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_CANCELLED;
                dispose();
            }
        });
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                container.setMicroworldProperties(MwdPropertiesDialog2.this);
/*                container.setMinimizeAllowed(isMinimizeAllowed());
                container.setMinimizeAllowed(isMinimizeAllowed());
                container.setCloseAllowed(isCloseControlEnabled());
                container.setControlBarTitleActive(isTitleActive());
                container.setHelpButtonVisible(isHelpControlEnabled());
                container.setInfoButtonVisible(isInfoControlEnabled());
                container.setPinButtonVisible(isPinControlEnabled());
                container.setResizeAllowed(isResizeAllowed());
                container.setMoveAllowed(isMoveAllowed());
                container.setControlBarsVisible(isControlBarVisible());
                container.setNameChangeAllowed(isNameChangeAllowed());
                container.setComponentInstantiationAllowed(isNewComponentsAllowed());
                container.setComponentRemovalAllowed(isComponentRemovalAllowed());
                container.setMwdBgrdChangeAllowed(isBgrdChangeAllowed());
                container.setMwdStorageAllowed(isStorageAllowed());
                container.setMwdPinViewEnabled(isPinViewEnabled());
                container.setMwdTitleEnabled(isMwdTitleEnabled());
                container.setMwdPopupEnabled(isMwdPopupEnabled());
                container.setMenuBarVisible(isMenuBarVisible());
                container.setComponentBarEnabled(isComponentBarEnabled());
                container.setMwdResizable(isMwdResizable());
                container.setMicroworldSize(new Dimension(getMicroworldWidth(), getMicroworldHeight()));
                container.setMwdAutoExpandable(isMwdAutoExpandable());
                container.setMwdAutoScrollable(isMwdAutoScrollable());
                container.setComponentsMoveBeyondMwdBounds(isMwdBoundaries());
                container.setDesktopDraggable(isDesktopDraggable());
                container.setMenuSystemHeavyWeight(isMenuSystemHeavyWeight());
                container.setStoreSkinsPerView(isStoreSkinsPerView());
                container.setActiveComponentHighlighted(isActiveComponentHighlighted());
                container.setEnsureActiveComponentAlwaysVisible(isEnsureActiveFrameVisible());

                container.setScrollBarPolicies(getVerticalScrollBarPolicy(), getHorizontalScrollBarPolicy());
*/
/*                boolean scrollPaneNeedsValidation = false;
                if (container.scrollPane.getVerticalScrollBarPolicy() != getVerticalScrollBarPolicy()) {
                    container.scrollPane.setVerticalScrollBarPolicy(getVerticalScrollBarPolicy());
                    scrollPaneNeedsValidation = true;
                }
                if (container.scrollPane.getHorizontalScrollBarPolicy() != getHorizontalScrollBarPolicy()) {
                    container.scrollPane.setHorizontalScrollBarPolicy(getHorizontalScrollBarPolicy());
                    scrollPaneNeedsValidation = true;
                }
                if (scrollPaneNeedsValidation)
                    container.scrollPane.revalidate();
*/            }
        });
        profileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
///                ProfileDialog dialog = new ProfileDialog(container, MwdPropertiesDialog2.this);
                /* The first time the ProfileDialog appears, any pre-registered (in some other
                 * E-Slate session) custom profiles have not been read yet. So read them here
                 * and populate the 'customProfiles' array, before it is given to the
                 * ProfileDialog.
                 */
/*                if (customProfiles == null) {
//System.out.println("Loading custom profiles");
                    String[] customProfileFileNames = container.readMicroworldSettingsProfiles();
//System.out.println("customProfileFileNames: " + customProfileFileNames);
                    loadCustomProfiles(customProfileFileNames);
                }
                dialog.setNonStandardProfiles(customProfiles);
                ESlateContainerUtils.showDialog(dialog, MwdPropertiesDialog2.this, true);

                if (dialog.getReturnCode() != dialog.DIALOG_OK)
                    return;
                MicroworldSettingsProfile prof = dialog.getSelectedProfile();
                if (prof != null)
                    setProfile(prof);
                MicroworldSettingsProfile[] nonStandardProfiles = dialog.getNonStandardProfiles();
                customProfiles = nonStandardProfiles;
*/            }
        });

        minimizeButtonVisibleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMinimizeButtonVisible(minimizeButtonVisibleBox.isSelected());
            }
        });
        maximizeButtonVisibleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMaximizeButtonVisible(maximizeButtonVisibleBox.isSelected());
            }
        });
        closeButtonVisibleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setCloseButtonVisible(closeButtonVisibleBox.isSelected());
            }
        });
        barVisibleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setControlBarVisible(barVisibleBox.isSelected());
            }
        });
        componentNameBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setComponentNameChangeAllowed(componentNameBox.isSelected());
            }
        });
        helpBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setHelpControlEnabled(helpBox.isSelected());
            }
        });
        infoBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setInfoControlEnabled(infoBox.isSelected());
            }
        });
        pinBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setPinControlEnabled(pinBox.isSelected());
            }
        });
        resizeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setResizeAllowed(resizeBox.isSelected());
            }
        });
        moveBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMoveAllowed(moveBox.isSelected());
            }
        });
/*        freezeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setComponentFrozenStateChangeAllowed(freezeBox.isSelected());
            }
        });
*/        activateBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setComponentActivationMethodChangeAllowed(activateBox.isSelected());
            }
        });
        barRenameBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setTitleActive(barRenameBox.isSelected());
            }
        });
        outlineDragBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setOutlineDragEnabled(outlineDragBox.isSelected());
            }
        });
        displayActiveComponentBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setActiveComponentHighlighted(displayActiveComponentBox.isSelected());
            }
        });
        ensureActiveFrameVisibleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEnsureActiveFrameVisible(ensureActiveFrameVisibleBox.isSelected());
            }
        });
        newComponentBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setNewComponentsAllowed(newComponentBox.isSelected());
            }
        });
        destroyComponentBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setComponentRemovalAllowed(destroyComponentBox.isSelected());
            }
        });
        backgroundChangeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBgrdChangeAllowed(backgroundChangeBox.isSelected());
            }
        });
        storeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setStorageAllowed(storeBox.isSelected());
            }
        });
        pinViewBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setPinViewEnabled(pinViewBox.isSelected());
            }
        });
        mwdNameUserDefinedBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMwdNameUserDefined(mwdNameUserDefinedBox.isSelected());
            }
        });
        mwdPopupBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMwdPopupEnabled(mwdPopupBox.isSelected());
            }
        });
        menuBarVisibleBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMenuBarVisible(menuBarVisibleBox.isSelected());
            }
        });
        componentBarBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setComponentBarEnabled(componentBarBox.isSelected());
            }
        });
        mwdResizable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMwdResizable(mwdResizable.isSelected());
            }
        });
        mwdAutoExpandable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMwdAutoExpandable(mwdAutoExpandable.isSelected());
            }
        });
        mwdAutoScrollable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMwdAutoScrollable(mwdAutoScrollable.isSelected());
            }
        });
        mwdBoundaries.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMwdBoundaries(mwdBoundaries.isSelected());
            }
        });
        desktopDrag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDesktopDraggable(desktopDrag.isSelected());
            }
        });
        horizontalSPBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = horizontalSPBox.getSelectedIndex();
                if (selectedIndex == 0)
                    currentProfile.horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
                else if (selectedIndex == 2)
                    currentProfile.horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
                else if (selectedIndex == 1)
                    currentProfile.horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
            }
        });
        verticalSPBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = verticalSPBox.getSelectedIndex();
                if (selectedIndex == 0)
                    currentProfile.verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
                else if (selectedIndex == 2)
                    currentProfile.verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
                else if (selectedIndex == 1)
                    currentProfile.verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
            }
        });
        menuSystemHeavyWeight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMenuSystemHeavyWeight(menuSystemHeavyWeight.isSelected());
            }
        });
        storeSkinsPerView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setStoreSkinsPerView(storeSkinsPerView.isSelected());
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        setProfile(currentProfile);
    }

    public void showDialog(Component comp) {
        pack();
        setResizable(true);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (comp == null || !comp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = comp.getBounds();
            java.awt.Point compLocation = comp.getLocationOnScreen();
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
        getRootPane().setDefaultButton(okButton);
        newComponentBox.requestFocus();
//        container.modalDialog = this;
        setVisible(true);
//        container.modalDialog = null;
    }

    private void setMinimizeButtonVisible(boolean visible) {
        currentProfile.minimizeButtonVisible = visible;
//        if (currentProfile.controlBarsVisible) //barVisible
            minimizeButtonVisibleBox.setSelected(visible);
    }
    public boolean isMinimizeButtonVisible() {
        return currentProfile.minimizeButtonVisible;
    }

    private void setMaximizeButtonVisible(boolean visible) {
        currentProfile.maximizeButtonVisible = visible;
//        if (currentProfile.controlBarsVisible) //barVisible)
            maximizeButtonVisibleBox.setSelected(visible);
    }
    public boolean isMaximizeButtonVisible() {
        return currentProfile.maximizeButtonVisible;
    }

    private void setCloseButtonVisible(boolean visible) {
        currentProfile.closeButtonVisible = visible;
//        if (currentProfile.controlBarsVisible) //barVisible)
            closeButtonVisibleBox.setSelected(visible);
    }
    public boolean isCloseButtonVisible() {
        return currentProfile.closeButtonVisible; //closeControlEnabled;
    }

    private void setTitleActive(boolean active) {
        currentProfile.controlBarTitleActive = active; //titleActive = active;
//        if (currentProfile.controlBarsVisible) //barVisible)
            barRenameBox.setSelected(active);
    }
    public boolean isTitleActive() {
        return currentProfile.controlBarTitleActive; //titleActive;
    }

    private void setHelpControlEnabled(boolean enabled) {
        currentProfile.helpButtonVisible = enabled; //helpControlEnabled = enabled;
//        if (currentProfile.controlBarsVisible) //barVisible)
            helpBox.setSelected(enabled);
    }
    public boolean isHelpControlEnabled() {
        return currentProfile.helpButtonVisible; //helpControlEnabled;
    }

    private void setInfoControlEnabled(boolean enabled) {
        currentProfile.infoButtonVisible = enabled; //infoControlEnabled = enabled;
//        if (currentProfile.controlBarsVisible) //barVisible)
            infoBox.setSelected(enabled);
    }
    public boolean isInfoControlEnabled() {
        return currentProfile.infoButtonVisible; //infoControlEnabled;
    }

    private void setPinControlEnabled(boolean enabled) {
        currentProfile.plugButtonVisible = enabled; //pinControlEnabled = enabled;
//        if (currentProfile.controlBarsVisible) //barVisible)
            pinBox.setSelected(enabled);
    }
    public boolean isPinControlEnabled() {
        return currentProfile.plugButtonVisible; //pinControlEnabled;
    }

    private void setResizeAllowed(boolean allowed) {
        currentProfile.resizeAllowed = allowed;
        resizeBox.setSelected(allowed);
    }
    public boolean isResizeAllowed() {
        return currentProfile.resizeAllowed; //resizeBox.isSelected();
    }

    private void setMoveAllowed(boolean allowed) {
        currentProfile.moveAllowed = allowed;
        moveBox.setSelected(allowed);
    }
    public boolean isMoveAllowed() {
        return currentProfile.moveAllowed;// moveBox.isSelected();
    }

/*    public void setComponentFrozenStateChangeAllowed(boolean allowed) {
        freezeBox.setSelected(allowed);
    }
    public boolean isComponentFrozenStateChangeAllowed() {
        return freezeBox.isSelected();
    }
*/
    private void setComponentActivationMethodChangeAllowed(boolean allowed) {
        currentProfile.componentActivationMethodChangeAllowed = allowed;
        activateBox.setSelected(allowed);
    }
    public boolean isComponentActivationMethodChangeAllowed() {
        return currentProfile.componentActivationMethodChangeAllowed; //activateBox.isSelected();
    }

    private void setComponentNameChangeAllowed(boolean allowed) {
        currentProfile.componentNameChangeAllowed = allowed;
        componentNameBox.setSelected(allowed);
    }
    public boolean isComponentNameChangeAllowed() {
        return currentProfile.componentNameChangeAllowed; //componentNameBox.isSelected();
    }

    private void setOutlineDragEnabled(boolean enabled) {
        outlineDragBox.setSelected(enabled);
    }
    public boolean isOutlineDragEnabled() {
        return outlineDragBox.isSelected();
    }

    private void setActiveComponentHighlighted(boolean highlighted) {
        currentProfile.activeComponentHighlighted = highlighted;
        displayActiveComponentBox.setSelected(highlighted);
    }
    public boolean isActiveComponentHighlighted() {
        return currentProfile.activeComponentHighlighted; //displayActiveComponentBox.isSelected();
    }

    private void setEnsureActiveFrameVisible(boolean ensure) {
        currentProfile.ensureActiveComponentVisible = ensure;
        ensureActiveFrameVisibleBox.setSelected(ensure);
    }
    public boolean isEnsureActiveFrameVisible() {
        return currentProfile.ensureActiveComponentVisible; //ensureActiveFrameVisibleBox.isSelected();
    }

    private void setNewComponentsAllowed(boolean allowed) {
        currentProfile.componentInstantiationAllowed = allowed;
        newComponentBox.setSelected(allowed);
    }
    public boolean isNewComponentsAllowed() {
        return currentProfile.componentInstantiationAllowed; //newComponentBox.isSelected();
    }

    private void setComponentRemovalAllowed(boolean allowed) {
        currentProfile.componentRemovalAllowed = allowed;
        destroyComponentBox.setSelected(allowed);
/*        if (!allowed) {
            setCloseButtonVisible(false);
            closeButtonVisibleBox.setEnabled(false);
        }else{
            setCloseButtonVisible(true);
            closeButtonVisibleBox.setEnabled(true);
        }
*/
    }
    public boolean isComponentRemovalAllowed() {
        return currentProfile.componentRemovalAllowed; //destroyComponentBox.isSelected();
    }

    private void setBgrdChangeAllowed(boolean allowed) {
        currentProfile.mwdBgrdChangeAllowed = allowed;
        backgroundChangeBox.setSelected(allowed);
    }
    public boolean isBgrdChangeAllowed() {
        return currentProfile.mwdBgrdChangeAllowed; //backgroundChangeBox.isSelected();
    }

    private void setStorageAllowed(boolean allowed) {
        currentProfile.mwdStorageAllowed = allowed;
        storeBox.setSelected(allowed);
    }
    public boolean isStorageAllowed() {
        return currentProfile.mwdStorageAllowed; //storeBox.isSelected();
    }

    private void setPinViewEnabled(boolean enabled) {
        currentProfile.plugConnectionChangeAllowed = enabled;
        pinViewBox.setSelected(enabled);
    }
    public boolean isPinViewEnabled() {
        return currentProfile.plugConnectionChangeAllowed; //pinViewBox.isSelected();
    }
    private void setMwdNameUserDefined(boolean defined) {
        currentProfile.microworldNameUserDefined = defined;
        mwdNameUserDefinedBox.setSelected(defined);
    }
    public boolean isMwdNameUserDefined() {
        return currentProfile.microworldNameUserDefined; //!mwdTitleBox.isSelected();
    }
    private void setMwdPopupEnabled(boolean enabled) {
        currentProfile.mwdPopupEnabled = enabled;
        mwdPopupBox.setSelected(enabled);
    }
    public boolean isMwdPopupEnabled() {
        return currentProfile.mwdPopupEnabled; //mwdPopupBox.isSelected();
    }
    private void setMenuBarVisible(boolean visible) {
        currentProfile.menuBarVisible = visible;
        menuBarVisibleBox.setSelected(visible);
    }
    public boolean isMenuBarVisible() {
        return currentProfile.menuBarVisible; //menuBarVisibleBox.isSelected();
    }
    private void setComponentBarEnabled(boolean enabled) {
        currentProfile.eslateComponentBarEnabled = enabled;
        componentBarBox.setSelected(enabled);
    }
    public boolean isComponentBarEnabled() {
        return currentProfile.eslateComponentBarEnabled; //componentBarBox.isSelected();
    }
    private void setMwdResizable(boolean resizable) {
        currentProfile.mwdResizable = resizable;
        mwdResizable.setSelected(resizable);
        widthPanel.setEnabled(resizable);
        heightPanel.setEnabled(resizable);
        setMwdAutoExpandable(false);
        mwdAutoExpandable.setEnabled(resizable && isMwdBoundaries());
    }
    public boolean isMwdResizable() {
        return currentProfile.mwdResizable; //mwdResizable.isSelected();
    }
    private void setMwdAutoExpandable(boolean expandable) {
        if (!mwdBoundaries.isSelected()) {
            currentProfile.mwdAutoExpandable = expandable;
            mwdAutoExpandable.setSelected(expandable);
        }
    }
    public boolean isMwdAutoExpandable() {
        return currentProfile.mwdAutoExpandable; //mwdAutoExpandable.isSelected();
    }
    private void setMwdAutoScrollable(boolean scrollable) {
        /* In java 1.3 auto-scrolling has been cancelled, because of the repaint problems
         * while scrolling a desktop pane. A bug fix is expected from SUN.
         */
        if (System.getProperty("java.version").startsWith("1.3"))
            return;
        currentProfile.mwdAutoScrollable = scrollable;
        mwdAutoScrollable.setSelected(scrollable);
    }
    public boolean isMwdAutoScrollable() {
        return currentProfile.mwdAutoScrollable; //mwdAutoScrollable.isSelected();
    }
    private void setMwdBoundaries(boolean beyondBoundaries) {
        mwdBoundaries.setSelected(beyondBoundaries);
        currentProfile.componentsMoveBeyondMwdBounds = beyondBoundaries;
        mwdAutoExpandable.setEnabled(isMwdResizable() && isMwdBoundaries());
        if (!beyondBoundaries) {
            mwdAutoExpandable.setSelected(false);
            currentProfile.mwdAutoExpandable = false;
        }
    }
    public boolean isMwdBoundaries() {
        return currentProfile.componentsMoveBeyondMwdBounds; //mwdBoundaries.isSelected();
    }
    private void setDesktopDraggable(boolean draggable) {
        currentProfile.desktopDraggable = draggable;
        desktopDrag.setSelected(draggable);
    }
    public boolean isDesktopDraggable() {
        return currentProfile.desktopDraggable; //desktopDrag.isSelected();;
    }
    private void setVerticalScrollBarPolicy(int policy) {
        if (policy == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED) {
            verticalSPBox.setSelectedIndex(0);
            currentProfile.verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
        }else if (policy == JScrollPane.VERTICAL_SCROLLBAR_ALWAYS) {
            verticalSPBox.setSelectedIndex(2);
            currentProfile.verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        }else{
            verticalSPBox.setSelectedIndex(1);
            currentProfile.verticalScrollbarPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
        }
    }
    public int getVerticalScrollBarPolicy() {
        return currentProfile.verticalScrollbarPolicy;
/*        int selIndex = verticalSPBox.getSelectedIndex();
        int policy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
        if (selIndex == 0) {
            policy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
        }else if (selIndex == 2) {
            policy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        }
        return policy;
*/
    }
    private void setHorizontalScrollBarPolicy(int policy) {
        if (policy == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
            horizontalSPBox.setSelectedIndex(0);
            currentProfile.horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        }else if (policy == JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS) {
            horizontalSPBox.setSelectedIndex(2);
            currentProfile.horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
        }else{
            horizontalSPBox.setSelectedIndex(1);
            currentProfile.horizontalScrollbarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        }
    }
    public int getHorizontalScrollBarPolicy() {
        return currentProfile.horizontalScrollbarPolicy;
/*        int selIndex = horizontalSPBox.getSelectedIndex();
        int policy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
        if (selIndex == 0)
            policy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        else if (selIndex == 2)
            policy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
        return policy;
*/
    }

    private void setMenuSystemHeavyWeight(boolean heavyweight) {
        currentProfile.menuSystemHeavyWeight = heavyweight;
        menuSystemHeavyWeight.setSelected(heavyweight);
    }
    public boolean isMenuSystemHeavyWeight() {
        return currentProfile.menuSystemHeavyWeight; //menuSystemHeavyWeight.isSelected();
    }

    private void setStoreSkinsPerView(boolean perView) {
        currentProfile.storeSkinOnAPerViewBasis = perView;
        storeSkinsPerView.setSelected(perView);
    }
    public boolean isStoreSkinsPerView() {
        return currentProfile.storeSkinOnAPerViewBasis; //storeSkinsPerView.isSelected();
    }

    private void setControlBarVisible(boolean visible) {
        if (!visible) {
//            minimizeBox.setSelected(false);
            setMinimizeButtonVisible(false);
            minimizeButtonVisibleBox.setEnabled(false);
//            closeControlBox.setSelected(false);
            setCloseButtonVisible(false);
            closeButtonVisibleBox.setEnabled(false);
//            maximizeBox.setSelected(false);
            setMaximizeButtonVisible(false);
            maximizeButtonVisibleBox.setEnabled(false);
//            helpBox.setSelected(false);
            setHelpControlEnabled(false);
            helpBox.setEnabled(false);
//            infoBox.setSelected(false);
            setInfoControlEnabled(false);
            infoBox.setEnabled(false);
//            pinBox.setSelected(false);
            setPinControlEnabled(false);
            pinBox.setEnabled(false);
//            barRenameBox.setSelected(false);
            setTitleActive(false);
            barRenameBox.setEnabled(false);
        }else{
            minimizeButtonVisibleBox.setEnabled(true);
//            minimizeBox.setSelected(currentProfile.minimizeAllowed);// minimizedAllowed);
            if (destroyComponentBox.isSelected())
                closeButtonVisibleBox.setEnabled(true);
            else
                closeButtonVisibleBox.setEnabled(false);
//            closeControlBox.setSelected(currentProfile.closeAllowed); //closeControlEnabled);
            maximizeButtonVisibleBox.setEnabled(true);
//            maximizeBox.setSelected(currentProfile.maximizeAllowed); // maximizedAllowed);
            helpBox.setEnabled(true);
//            helpBox.setSelected(currentProfile.helpButtonVisible);// helpControlEnabled);
            infoBox.setEnabled(true);
//            infoBox.setSelected(currentProfile.infoButtonVisible);// infoControlEnabled);
            pinBox.setEnabled(true);
//            pinBox.setSelected(currentProfile.pinButtonVisible); // pinControlEnabled);
            barRenameBox.setEnabled(true);
//            barRenameBox.setSelected(currentProfile.controlBarTitleActive);// titleActive);
        }
//        barVisible = visible;
        currentProfile.controlBarsVisible = visible;
        barVisibleBox.setSelected(visible);
    }

    public boolean isControlBarVisible() {
        return currentProfile.controlBarsVisible; //barVisible;
    }

    // METADATA panel
    public void setMicroworldTitle(String title) {
        metadataPanel.titleFld.setText(title);
    }
    public String getMicroworldTitle() {
        return metadataPanel.titleFld.getText();
    }

    public void setMicroworldSubject(String subject) {
        metadataPanel.subjectFld.setText(subject);
    }
    public String getMicroworldSubject() {
        return metadataPanel.subjectFld.getText();
    }

    public void setMicroworldCompany(String company) {
        metadataPanel.companyFld.setText(company);
    }
    public String getMicroworldCompany() {
        return metadataPanel.companyFld.getText();
    }

    public void setMicroworldkeywords(String keywords) {
        metadataPanel.keywordFld.setText(keywords);
    }
    public String getMicroworldkeywords() {
        return metadataPanel.keywordFld.getText();
    }

    public void setMicroworldAuthors(MicroworldAuthor[] authors) {
        ((AuthorListModel) metadataPanel.authorsList.getModel()).setAuthors(authors);
    }
    public MicroworldAuthor[] getMicroworldAuthors() {
        return ((AuthorListModel) metadataPanel.authorsList.getModel()).getAuthors();
    }

    public void setMicroworldCategory(String category) {
        metadataPanel.categoryBox.setSelectedItem(category);
    }
    public String getMicroworldCategory() {
        return (String) metadataPanel.categoryBox.getSelectedItem();
    }

    public void setMicroworldComments(String comments) {
        metadataPanel.commentsArea.setText(comments);
    }
    public String getMicroworldComments() {
        return metadataPanel.commentsArea.getText();
    }

    void setPassword(String password) {
        metadataPanel.setPassword(password);
    }

    String getPassword() {
        return metadataPanel.getPassword();
    }

    void setLocked(boolean locked) {
        if (isLocked == locked) return;
        isLocked = locked;
        metadataPanel.setLocked(locked);
        if (locked) {
            minimizeButtonVisibleBox.setEnabled(false);
            closeButtonVisibleBox.setEnabled(false);
            maximizeButtonVisibleBox.setEnabled(false);
            barVisibleBox.setEnabled(false);
            componentNameBox.setEnabled(false);
            helpBox.setEnabled(false);
            infoBox.setEnabled(false);
            pinBox.setEnabled(false);
            resizeBox.setEnabled(false);
            moveBox.setEnabled(false);
            barRenameBox.setEnabled(false);
            activateBox.setEnabled(false);
            newComponentBox.setEnabled(false);
            destroyComponentBox.setEnabled(false);
            backgroundChangeBox.setEnabled(false);
            storeBox.setEnabled(false);
            pinViewBox.setEnabled(false);
            mwdNameUserDefinedBox.setEnabled(false);
            mwdPopupBox.setEnabled(false);
            menuBarVisibleBox.setEnabled(false);
            outlineDragBox.setEnabled(false);
            componentBarBox.setEnabled(false);
            widthPanel.setEnabled(false);
            heightPanel.setEnabled(false);
            mwdResizable.setEnabled(false);
            mwdAutoExpandable.setEnabled(false);
            mwdAutoScrollable.setEnabled(false);
            mwdBoundaries.setEnabled(false);
            desktopDrag.setEnabled(false);
            menuSystemHeavyWeight.setEnabled(false);
            storeSkinsPerView.setEnabled(false);
            displayActiveComponentBox.setEnabled(false);
            ensureActiveFrameVisibleBox.setEnabled(false);
            verticalSPBox.setEnabled(false);
            horizontalSPBox.setEnabled(false);
            verticalSPLabel.setEnabled(false);
            horizontalSPLabel.setEnabled(false);
        }else{
            minimizeButtonVisibleBox.setEnabled(true);
            maximizeButtonVisibleBox.setEnabled(true);
            resizeBox.setEnabled(true);
            moveBox.setEnabled(true);
            componentNameBox.setEnabled(true);
            newComponentBox.setEnabled(true);
            destroyComponentBox.setEnabled(true);
            setComponentRemovalAllowed(container.microworld.componentRemovalAllowed);
            backgroundChangeBox.setEnabled(true);
            storeBox.setEnabled(true);
            pinViewBox.setEnabled(true);

            barVisibleBox.setEnabled(true);
            setControlBarVisible(container.currentView.controlBarsVisible);

            mwdNameUserDefinedBox.setEnabled(true);
            mwdPopupBox.setEnabled(true);
            menuBarVisibleBox.setEnabled(true);
            outlineDragBox.setEnabled(false);
            componentBarBox.setEnabled(true);
            activateBox.setEnabled(true);
            mwdAutoExpandable.setEnabled(true);
            mwdResizable.setEnabled(true);
            setMwdResizable(container.currentView.mwdResizable);
            if (!System.getProperty("java.version").startsWith("1.3"))
                mwdAutoScrollable.setEnabled(true);
            mwdBoundaries.setEnabled(true);
            desktopDrag.setEnabled(true);
            menuSystemHeavyWeight.setEnabled(true);
            storeSkinsPerView.setEnabled(true);
            verticalSPBox.setEnabled(true);
            horizontalSPBox.setEnabled(true);
            verticalSPLabel.setEnabled(true);
            horizontalSPLabel.setEnabled(true);
            displayActiveComponentBox.setEnabled(true);
            ensureActiveFrameVisibleBox.setEnabled(true);
        }
    }

    public int getReturnCode() {
        return returnCode;
    }

    public int getMicroworldWidth() {
        return ((Integer) widthPanel.spin.getValue()).intValue();
    }

    public int getMicroworldHeight() {
        return ((Integer) heightPanel.spin.getValue()).intValue();
    }

    public JPanel createMicroworldPropertiesPanel() {
        // General microworld settings
        newComponentBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentCreationEnabled"));
        destroyComponentBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentRemovalEnabled"));
        backgroundChangeBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MwdBackgroundChangeEnabled"));
        storeBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MwdStorageEnabled"));
        pinViewBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MwdPinViewEnabled"));
        mwdPopupBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MwdPopupEnabled"));
        menuBarVisibleBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MenuBarVisible"));
        mwdNameUserDefinedBox = new JCheckBox(mwdPropertiesDialogBundle.getString("MicroworldNameUserDefined"));
        componentBarBox = new JCheckBox(mwdPropertiesDialogBundle.getString("ComponentBarEnabled"));
        // Microworld size settings
        widthPanel = new SpinField(true, mwdPropertiesDialogBundle.getString("Width"), container.getMicroworldSize().width);
        heightPanel = new SpinField(true, mwdPropertiesDialogBundle.getString("Height"), container.getMicroworldSize().height);
        mwdResizable = new JCheckBox(mwdPropertiesDialogBundle.getString("Resizable"));
        mwdAutoExpandable = new JCheckBox(mwdPropertiesDialogBundle.getString("AutoExpandable"));
        mwdAutoScrollable = new JCheckBox(mwdPropertiesDialogBundle.getString("AutoScrollable"));
        mwdBoundaries = new JCheckBox(mwdPropertiesDialogBundle.getString("ExceedBoundaries"));
        desktopDrag = new JCheckBox(mwdPropertiesDialogBundle.getString("DesktopDrag"));
        verticalSPBox = new JComboBox(scrollBarPolicies);
        horizontalSPBox = new JComboBox(scrollBarPolicies);
        menuSystemHeavyWeight = new JCheckBox(mwdPropertiesDialogBundle.getString("MenuSystemHeavyWeight"));
        storeSkinsPerView = new JCheckBox(mwdPropertiesDialogBundle.getString("StoreSkinsPerView"));

        SpinField[] fields = new SpinField[] {heightPanel, widthPanel};
        SpinField.alignLabelsOfLabeledPanels(fields, 0, 5);
        SpinField.setSpinFieldSpinSize(fields, 55, 20);

        widthPanel.setMinValue(0);
        heightPanel.setMinValue(0);


        // General microworld properties panel
        JPanel mwdGeneralPropertiesPanel = new JPanel();
/*        mwdGeneralPropertiesPanel.setLayout(new BoxLayout(mwdGeneralPropertiesPanel, BoxLayout.Y_AXIS));
        mwdGeneralPropertiesPanel.add(newComponentBox);
        mwdGeneralPropertiesPanel.add(destroyComponentBox);
        mwdGeneralPropertiesPanel.add(backgroundChangeBox);
        mwdGeneralPropertiesPanel.add(storeBox);
        mwdGeneralPropertiesPanel.add(pinViewBox);
        mwdGeneralPropertiesPanel.add(mwdPopupBox);
        mwdGeneralPropertiesPanel.add(menuBarVisibleBox);
        mwdGeneralPropertiesPanel.add(mwdTitleBox);
        mwdGeneralPropertiesPanel.add(componentBarBox);
        mwdGeneralPropertiesPanel.add(menuSystemHeavyWeight);
        mwdGeneralPropertiesPanel.add(storeSkinsPerView);
*/
        mwdGeneralPropertiesPanel.setLayout(new ExplicitLayout());
        ExplicitConstraints ec11 = new ExplicitConstraints(newComponentBox);
        ec11.setY(ContainerEF.top(mwdGeneralPropertiesPanel));
        ec11.setX(ContainerEF.left(mwdGeneralPropertiesPanel));
        ec11.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec11.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(newComponentBox, ec11);

        ExplicitConstraints ec12 = new ExplicitConstraints(destroyComponentBox);
        ec12.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec12.setY(ComponentEF.bottom(newComponentBox));
        ec12.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(destroyComponentBox, ec12);

        ExplicitConstraints ec13 = new ExplicitConstraints(backgroundChangeBox);
        ec13.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec13.setY(ComponentEF.bottom(destroyComponentBox));
        ec13.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(backgroundChangeBox, ec13);

        ExplicitConstraints ec14 = new ExplicitConstraints(storeBox);
        ec14.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec14.setY(ComponentEF.bottom(backgroundChangeBox));
        ec14.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(storeBox, ec14);

        ExplicitConstraints ec15 = new ExplicitConstraints(pinViewBox);
        ec15.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec15.setY(ComponentEF.bottom(storeBox));
        ec15.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(pinViewBox, ec15);

        ExplicitConstraints ec16 = new ExplicitConstraints(mwdPopupBox);
        ec16.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec16.setY(ComponentEF.bottom(pinViewBox));
        ec16.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(mwdPopupBox, ec16);

        ExplicitConstraints ec17 = new ExplicitConstraints(menuBarVisibleBox);
        ec17.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec17.setY(ComponentEF.bottom(mwdPopupBox));
        ec17.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(menuBarVisibleBox, ec17);

        ExplicitConstraints ec18 = new ExplicitConstraints(mwdNameUserDefinedBox);
        ec18.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec18.setY(ComponentEF.bottom(menuBarVisibleBox));
        ec18.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(mwdNameUserDefinedBox, ec18);

        ExplicitConstraints ec19 = new ExplicitConstraints(componentBarBox);
        ec19.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec19.setY(ComponentEF.bottom(mwdNameUserDefinedBox));
        ec19.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(componentBarBox, ec19);

        ExplicitConstraints ec110 = new ExplicitConstraints(menuSystemHeavyWeight);
        ec110.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec110.setY(ComponentEF.bottom(componentBarBox));
        ec110.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(menuSystemHeavyWeight, ec110);

        ExplicitConstraints ec111 = new ExplicitConstraints(storeSkinsPerView);
        ec111.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec111.setY(ComponentEF.bottom(menuSystemHeavyWeight));
        ec111.setWidth(ContainerEF.width(mwdGeneralPropertiesPanel));
        mwdGeneralPropertiesPanel.add(storeSkinsPerView, ec111);

        TitledBorder tb3 = new TitledBorder(mwdPropertiesDialogBundle.getString("MicroworldGeneralSettings"));
        tb3.setTitleColor(ESlateContainerUtils.titleBorderColor); //barVisible.titleBorderColor);
//        if (localeIsGreek)
//            tb3.setTitleFont(greekUIFont);
        mwdGeneralPropertiesPanel.setBorder(new CompoundBorder(tb3, new EmptyBorder(0,5,5,0)));

        // Microworld size settings panel
/*        JPanel verticalSPPanel = new JPanel(true);
        verticalSPPanel.setLayout(new BoxLayout(verticalSPPanel, BoxLayout.X_AXIS));
*/
        verticalSPLabel = new JLabel(mwdPropertiesDialogBundle.getString("VerticalSP"));
/*        verticalSPPanel.add(verticalSPLabel);
        verticalSPPanel.add(Box.createHorizontalStrut(5));
        verticalSPPanel.add(verticalSPBox);

        JPanel horizontalSPPanel = new JPanel(true);
        horizontalSPPanel.setLayout(new BoxLayout(horizontalSPPanel, BoxLayout.X_AXIS));
*/
        horizontalSPLabel = new JLabel(mwdPropertiesDialogBundle.getString("HorizontalSP"));
/*        horizontalSPPanel.add(horizontalSPLabel);
        horizontalSPPanel.add(Box.createHorizontalStrut(5));
        horizontalSPPanel.add(horizontalSPBox);
*/
        // Ensure that the labels of the two Combo box panels have equal width
/*        fm = getToolkit().getFontMetrics(verticalSPLabel.getFont());
        int verticalSPLabelWidth = fm.stringWidth(verticalSPLabel.getText());
        if (fm.stringWidth(horizontalSPLabel.getText()) > verticalSPLabelWidth)
            verticalSPLabelWidth = fm.stringWidth(horizontalSPLabel.getText());
        Dimension labelDim = new Dimension(verticalSPLabelWidth, 20);
        horizontalSPLabel.setMaximumSize(labelDim);
        horizontalSPLabel.setMinimumSize(labelDim);
        horizontalSPLabel.setPreferredSize(labelDim);
        verticalSPLabel.setMaximumSize(labelDim);
        verticalSPLabel.setMinimumSize(labelDim);
        verticalSPLabel.setPreferredSize(labelDim);
*/
        //Tips
        widthPanel.setToolTipText(mwdPropertiesDialogBundle.getString("WidthTip"));
        heightPanel.setToolTipText(mwdPropertiesDialogBundle.getString("HeightTip"));
        mwdResizable.setToolTipText(mwdPropertiesDialogBundle.getString("ResizableTip"));
        mwdAutoExpandable.setToolTipText(mwdPropertiesDialogBundle.getString("AutoExpandableTip"));
        mwdAutoScrollable.setToolTipText(mwdPropertiesDialogBundle.getString("AutoScrollableTip"));
        mwdBoundaries.setToolTipText(mwdPropertiesDialogBundle.getString("ExceedBoundariesTip"));
        desktopDrag.setToolTipText(mwdPropertiesDialogBundle.getString("DesktopDragTip"));
        verticalSPLabel.setToolTipText(mwdPropertiesDialogBundle.getString("VerticalSPTip"));
        verticalSPBox.setToolTipText(mwdPropertiesDialogBundle.getString("VerticalSPTip"));
        horizontalSPLabel.setToolTipText(mwdPropertiesDialogBundle.getString("HorizontalSPTip"));
        horizontalSPBox.setToolTipText(mwdPropertiesDialogBundle.getString("HorizontalSPTip"));
        menuSystemHeavyWeight.setToolTipText(mwdPropertiesDialogBundle.getString("MenuSystemHeavyWeight"));
        storeSkinsPerView.setToolTipText(mwdPropertiesDialogBundle.getString("StoreSkinsPerViewTip"));

        /* In java 1.3 auto-scrolling has been cancelled, because of the repaint problems
         * while scrolling a desktop pane. A bug fix is expected from SUN.
         */
        if (System.getProperty("java.version").startsWith("1.3"))
            mwdAutoScrollable.setEnabled(false);

        JPanel mwdSizePropertiesPanel = new JPanel();
        ExplicitLayout mwdSizePanelLayout = new ExplicitLayout();
        mwdSizePropertiesPanel.setLayout(mwdSizePanelLayout);

        Expression fieldHeight = MathEF.constant(FIELD_HEIGHT);
        ExplicitConstraints ec1 = new ExplicitConstraints(widthPanel);
        ec1.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec1.setY(ContainerEF.top(mwdSizePropertiesPanel));
        ec1.setHeight(fieldHeight);
        ec1.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(widthPanel, ec1);

        ExplicitConstraints ec2 = new ExplicitConstraints(heightPanel);
        ec2.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec2.setY(ComponentEF.bottom(widthPanel));
        ec2.setHeight(fieldHeight);
        ec2.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(heightPanel, ec2);

        ExplicitConstraints ec3 = new ExplicitConstraints(mwdResizable);
        ec3.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec3.setY(ComponentEF.bottom(heightPanel));
        ec3.setHeight(fieldHeight);
        ec3.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(mwdResizable, ec3);

        ExplicitConstraints ec4 = new ExplicitConstraints(mwdAutoExpandable);
        ec4.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec4.setY(ComponentEF.bottom(mwdResizable));
        ec4.setHeight(fieldHeight);
        ec4.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(mwdAutoExpandable, ec4);

        ExplicitConstraints ec5 = new ExplicitConstraints(mwdAutoScrollable);
        ec5.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec5.setY(ComponentEF.bottom(mwdAutoExpandable));
        ec5.setHeight(fieldHeight);
        ec5.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(mwdAutoScrollable, ec5);

        ExplicitConstraints ec6 = new ExplicitConstraints(mwdBoundaries);
        ec6.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec6.setY(ComponentEF.bottom(mwdAutoScrollable));
        ec6.setHeight(fieldHeight);
        ec6.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(mwdBoundaries, ec6);

        ExplicitConstraints ec7 = new ExplicitConstraints(desktopDrag);
        ec7.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec7.setY(ComponentEF.bottom(mwdBoundaries));
        ec7.setHeight(fieldHeight);
        ec7.setWidth(ContainerEF.width(mwdSizePropertiesPanel));
        mwdSizePropertiesPanel.add(desktopDrag, ec7);

        ExplicitConstraints ec8 = new ExplicitConstraints(horizontalSPLabel);
        ec8.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec8.setY(ComponentEF.bottom(desktopDrag));
        ec8.setHeight(fieldHeight);
        mwdSizePropertiesPanel.add(horizontalSPLabel, ec8);

        ExplicitConstraints ec9 = new ExplicitConstraints(horizontalSPBox);
        ec9.setX(ComponentEF.right(horizontalSPLabel).add(H_GAP));
        Expression boxGap = MathEF.constant(4);
        ec9.setY(ComponentEF.top(horizontalSPLabel).add(boxGap));
        ec9.setHeight(fieldHeight.subtract(boxGap));
        ec9.setWidth(ContainerEF.width(mwdSizePropertiesPanel).subtract(ComponentEF.width(horizontalSPLabel)).subtract(H_GAP));
        mwdSizePropertiesPanel.add(horizontalSPBox, ec9);

        ExplicitConstraints ec10 = new ExplicitConstraints(verticalSPLabel);
        ec10.setX(ContainerEF.left(mwdSizePropertiesPanel));
        ec10.setY(ComponentEF.bottom(horizontalSPLabel));
        ec10.setHeight(fieldHeight);
        mwdSizePropertiesPanel.add(verticalSPLabel, ec10);

        ExplicitConstraints ec101 = new ExplicitConstraints(verticalSPBox);
        ec101.setX(ComponentEF.left(horizontalSPBox));
        ec101.setY(ComponentEF.top(verticalSPLabel).add(boxGap));
        ec101.setHeight(fieldHeight.subtract(boxGap));
        ec101.setWidth(ContainerEF.width(mwdSizePropertiesPanel).subtract(ComponentEF.width(horizontalSPLabel)).subtract(H_GAP));
        mwdSizePropertiesPanel.add(verticalSPBox, ec101);

/*        Component[] mwdSizeControls = new Component[] {widthPanel, heightPanel, mwdResizable,
                                                       mwdAutoExpandable, mwdAutoScrollable,
                                                       mwdBoundaries, desktopDrag};
        Expression maxWidth = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), mwdSizeControls));
        mwdSizePropertiesPanel.setPreferredSize(new Dimension((int)maxWidth.getValue(mwdSizePanelLayout), 10));
*/
/*        JPanel mwdSizePropertiesPanel = new JPanel();
        mwdSizePropertiesPanel.setLayout(new BoxLayout(mwdSizePropertiesPanel, BoxLayout.Y_AXIS));
        mwdSizePropertiesPanel.add(widthPanel);
        mwdSizePropertiesPanel.add(heightPanel);
        mwdSizePropertiesPanel.add(mwdResizable);
        mwdSizePropertiesPanel.add(mwdAutoExpandable);
        mwdSizePropertiesPanel.add(mwdAutoScrollable);
        mwdSizePropertiesPanel.add(mwdBoundaries);
        mwdSizePropertiesPanel.add(desktopDrag);
        mwdSizePropertiesPanel.add(verticalSPPanel);
        mwdSizePropertiesPanel.add(horizontalSPPanel);
        mwdSizePropertiesPanel.add(Box.createGlue());

        widthPanel.setAlignmentY(CENTER_ALIGNMENT);
        widthPanel.setAlignmentX(LEFT_ALIGNMENT);
        heightPanel.setAlignmentY(CENTER_ALIGNMENT);
        heightPanel.setAlignmentX(LEFT_ALIGNMENT);
        verticalSPPanel.setAlignmentY(CENTER_ALIGNMENT);
        verticalSPPanel.setAlignmentX(LEFT_ALIGNMENT);
        horizontalSPPanel.setAlignmentY(CENTER_ALIGNMENT);
        horizontalSPPanel.setAlignmentX(LEFT_ALIGNMENT);
        mwdResizable.setAlignmentX(LEFT_ALIGNMENT);
        mwdAutoExpandable.setAlignmentX(LEFT_ALIGNMENT);
        mwdAutoScrollable.setAlignmentX(LEFT_ALIGNMENT);
        mwdBoundaries.setAlignmentX(LEFT_ALIGNMENT);
        desktopDrag.setAlignmentX(LEFT_ALIGNMENT);
        menuSystemHeavyWeight.setAlignmentX(LEFT_ALIGNMENT);
        storeSkinsPerView.setAlignmentX(LEFT_ALIGNMENT);
*/
        TitledBorder tb4 = new TitledBorder(mwdPropertiesDialogBundle.getString("MicroworldSizeSettings"));
        tb4.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb4.setTitleFont(greekUIFont);
        mwdSizePropertiesPanel.setBorder(new CompoundBorder(tb4, new EmptyBorder(0,5,5,0)));

        JPanel mwdPropertiesPanel = new JPanel();
/*        mwdPropertiesPanel.setLayout(new java.awt.GridLayout(2, 1, 5, 5));
        mwdPropertiesPanel.add(mwdGeneralPropertiesPanel);
        mwdPropertiesPanel.add(mwdSizePropertiesPanel);
*/
        mwdPropertiesPanel.setLayout(new BoxLayout(mwdPropertiesPanel, BoxLayout.Y_AXIS));

        /* Find the maximum width of the controls of the panel and the total height of the
         * panel. Then set the preferred size of the panel, so that the BoxLayout of the
         * container will allocate the proper size for it.
         */
//        Expression maxWidthExp = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), mwdGeneralPropertiesPanel.getComponents()));
        Expression maxWidthExp = GroupEF.preferredWidthMax(mwdGeneralPropertiesPanel.getComponents());
        Expression heightExp = GroupEF.heightSum(mwdGeneralPropertiesPanel.getComponents());
        int maxWidth = (int) maxWidthExp.getValue((ExplicitLayout) mwdGeneralPropertiesPanel.getLayout());
        int height = (int) heightExp.getValue((ExplicitLayout) mwdGeneralPropertiesPanel.getLayout());
        Insets bi = mwdGeneralPropertiesPanel.getBorder().getBorderInsets(mwdGeneralPropertiesPanel);
        maxWidth = maxWidth + bi.left + bi.right;
        height = height + bi.top + bi.bottom;

        mwdGeneralPropertiesPanel.setPreferredSize(new Dimension(maxWidth, height));
        mwdPropertiesPanel.add(mwdGeneralPropertiesPanel);
        mwdPropertiesPanel.add(Box.createVerticalStrut(5));

        /* Find the maximum width of the controls of the panel and the total height of the
         * panel. Then set the preferred size of the panel, so that the BoxLayout of the
         * container will allocate the proper size for it.
         */
        Component[] mwdSizeControls = new Component[] {widthPanel, heightPanel, mwdResizable,
                                                       mwdAutoExpandable, mwdAutoScrollable,
                                                       mwdBoundaries, desktopDrag,
                                                       verticalSPLabel, horizontalSPLabel};
//        maxWidthExp = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), mwdSizeControls));
        maxWidthExp = GroupEF.preferredWidthMax(mwdSizeControls);
        heightExp = GroupEF.heightSum(mwdSizeControls);
        maxWidth = (int) maxWidthExp.getValue((ExplicitLayout) mwdSizePropertiesPanel.getLayout());
        height = (int) heightExp.getValue((ExplicitLayout) mwdSizePropertiesPanel.getLayout());
        bi = mwdSizePropertiesPanel.getBorder().getBorderInsets(mwdSizePropertiesPanel);
        maxWidth = maxWidth + bi.left + bi.right;
        height = height + bi.top + bi.bottom;

        mwdSizePropertiesPanel.setPreferredSize(new Dimension(maxWidth, height));
        mwdPropertiesPanel.add(mwdSizePropertiesPanel);

        return mwdPropertiesPanel;
    }

    public MicroworldSettingsProfile getProfile() {
         return currentProfile;
    }

    public void setProfile(MicroworldSettingsProfile profile) {
        if (profile == null) return;
        currentProfile = profile;
//System.out.println("setProfile() " + profile);
        setMinimizeButtonVisible(currentProfile.minimizeButtonVisible);
        setMaximizeButtonVisible(currentProfile.maximizeButtonVisible);
        setCloseButtonVisible(currentProfile.closeButtonVisible);
        setTitleActive(currentProfile.controlBarTitleActive);
        setHelpControlEnabled(currentProfile.helpButtonVisible);
        setInfoControlEnabled(currentProfile.infoButtonVisible);
        setPinControlEnabled(currentProfile.plugButtonVisible);
        setResizeAllowed(currentProfile.resizeAllowed);
        setMoveAllowed(currentProfile.moveAllowed);
        setComponentActivationMethodChangeAllowed(currentProfile.componentActivationMethodChangeAllowed);
        setComponentNameChangeAllowed(currentProfile.componentNameChangeAllowed);
        setActiveComponentHighlighted(currentProfile.activeComponentHighlighted);
        setEnsureActiveFrameVisible(currentProfile.ensureActiveComponentVisible);
        setNewComponentsAllowed(currentProfile.componentInstantiationAllowed);
        setComponentRemovalAllowed(currentProfile.componentRemovalAllowed);
        setBgrdChangeAllowed(currentProfile.mwdBgrdChangeAllowed);
        setStorageAllowed(currentProfile.mwdStorageAllowed);
        setPinViewEnabled(currentProfile.plugConnectionChangeAllowed);
        setMwdNameUserDefined(currentProfile.microworldNameUserDefined);
        setMwdPopupEnabled(currentProfile.mwdPopupEnabled);
        setMenuBarVisible(currentProfile.menuBarVisible);
        setComponentBarEnabled(currentProfile.eslateComponentBarEnabled);
        setMwdAutoExpandable(currentProfile.mwdAutoExpandable);
        setMwdResizable(currentProfile.mwdResizable);
        setMwdAutoScrollable(currentProfile.mwdAutoScrollable);
        setMwdBoundaries(currentProfile.componentsMoveBeyondMwdBounds);
        setDesktopDraggable(currentProfile.desktopDraggable);
        setVerticalScrollBarPolicy(currentProfile.verticalScrollbarPolicy);
        setHorizontalScrollBarPolicy(currentProfile.horizontalScrollbarPolicy);
        setMenuSystemHeavyWeight(currentProfile.menuSystemHeavyWeight);
        setStoreSkinsPerView(currentProfile.storeSkinOnAPerViewBasis);
        setControlBarVisible(currentProfile.controlBarsVisible);
    }

    void setCustomProfiles(MicroworldSettingsProfile[] profiles) {
        customProfiles = profiles;
    }

    MicroworldSettingsProfile[] getCustomProfiles() {
        return customProfiles;
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

}

/*class FixedHeightPanel extends JPanel {
    int height = 20;

    public FixedHeightPanel(int height) {
        super();
        this.height = height;
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.height = height;
        return dim;
    }

    public Dimension getMaximumSize() {
        Dimension dim = super.getMaximumSize();
        dim.height = height;
        return dim;
    }

}
*/



