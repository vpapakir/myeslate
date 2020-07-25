package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


class ProfileDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int TEXT_FIELD_WIDTH = 180;
    public static final int LIST_WIDTH = 200;
    public static final int LIST_HEIGHT = 100;
    DefaultListModel profileModel = new DefaultListModel();
    JList profileList;
    JButton importButton, exportButton;
    JButton okButton, cancelButton, renameButton, removeButton;
    ResourceBundle bundle;
    int returnCode = DIALOG_CANCELLED;
    ESlateContainer container;

    public ProfileDialog(ESlateContainer container, MicroworldPropertiesDialog parentDialog) {
        super(parentDialog, true);
        this.container = container;
//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(container.getAppIcon());
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ProfileDialogBundle", Locale.getDefault());
        setTitle(bundle.getString("Title"));

        JPanel mainPanel = new JPanel();
        ExplicitLayout el = new ExplicitLayout();
        mainPanel.setLayout(el);

        Expression listWidth = MathEF.constant(LIST_WIDTH);
        Expression listHeight = MathEF.constant(LIST_HEIGHT);
        profileList = new JList();
        profileList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        profileList.setToolTipText(bundle.getString("ListTip"));
        profileList.setModel(profileModel);
        profileList.setCellRenderer(new ProfileListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(profileList);
        ExplicitConstraints ec1 = new ExplicitConstraints(scrollPane);
        ec1.setX(ContainerEF.left(mainPanel));
        ec1.setY(ContainerEF.top(mainPanel));
        ec1.setWidth(listWidth);
        ec1.setHeight(listHeight);

        Insets zeroInsets = new Insets(0, 0, 0, 0);
        importButton = new JButton(bundle.getString("Import"));
        importButton.setToolTipText(bundle.getString("ImportTip"));
        importButton.setForeground(ESlateContainerUtils.color128);
        importButton.setMargin(zeroInsets);
        exportButton = new JButton(bundle.getString("Export"));
        exportButton.setToolTipText(bundle.getString("ExportTip"));
        exportButton.setForeground(ESlateContainerUtils.color128);
        exportButton.setMargin(zeroInsets);
        renameButton = new JButton(bundle.getString("Rename"));
        renameButton.setToolTipText(bundle.getString("RenameTip"));
        renameButton.setForeground(ESlateContainerUtils.color128);
        renameButton.setMargin(zeroInsets);
        removeButton = new JButton(bundle.getString("Remove"));
        removeButton.setToolTipText(bundle.getString("RemoveTip"));
        removeButton.setForeground(ESlateContainerUtils.color128);
        removeButton.setMargin(zeroInsets);

        Expression prefButtonHeight = ComponentEF.preferredHeight(renameButton);
//        Expression buttonWidth = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null),
//                                              new Component[] {renameButton, removeButton, importButton, exportButton}));
        Expression buttonWidth = GroupEF.preferredWidthMax(new Component[] {
        renameButton, removeButton, importButton, exportButton});
        Expression totalButtonHeight = ComponentEF.preferredHeight(importButton).multiply(4);
        Expression totalInterButtonGap = MathEF.constant(V_GAP).multiply(3);
        Expression importButtonY = ContainerEF.top(this).add(listHeight.subtract(totalButtonHeight.add(totalInterButtonGap)).divide(2));

        ExplicitConstraints ec2 = new ExplicitConstraints(renameButton);
        ec2.setX(ComponentEF.right(scrollPane).add(H_GAP));
        ec2.setY(importButtonY); //ComponentEF.top(scrollPane));
        ec2.setWidth(buttonWidth);
        ec2.setHeight(prefButtonHeight);

        ExplicitConstraints ec3 = new ExplicitConstraints(removeButton);
        ec3.setX(ComponentEF.left(renameButton));
        ec3.setY(ComponentEF.bottom(renameButton).add(V_GAP));
        ec3.setWidth(buttonWidth);
        ec3.setHeight(prefButtonHeight);

        ExplicitConstraints ec4 = new ExplicitConstraints(importButton);
        ec4.setX(ComponentEF.left(renameButton));
        ec4.setY(ComponentEF.bottom(removeButton).add(V_GAP));
        ec4.setWidth(buttonWidth);
        ec4.setHeight(prefButtonHeight);

        ExplicitConstraints ec5 = new ExplicitConstraints(exportButton);
        ec5.setX(ComponentEF.left(renameButton));
        ec5.setY(ComponentEF.bottom(importButton).add(V_GAP));
        ec5.setWidth(buttonWidth);
        ec5.setHeight(prefButtonHeight);

        Expression panelWidthExp = listWidth.add(H_GAP).add(buttonWidth);
        if (totalButtonHeight.add(totalInterButtonGap).getValue(el) > listHeight.getValue(el)) {
            listHeight = totalButtonHeight.add(totalInterButtonGap);
            ec1.setHeight(listHeight);
            ec2.setY(ContainerEF.top(mainPanel));
        }
        el.setPreferredLayoutSize(panelWidthExp, listHeight);
        mainPanel.add(scrollPane, ec1);
        mainPanel.add(renameButton, ec2);
        mainPanel.add(removeButton, ec3);
        mainPanel.add(importButton, ec4);
        mainPanel.add(exportButton, ec5);
        mainPanel.setBorder(new EmptyBorder(5, 5, 0, 5));;

        okButton = new JButton(bundle.getString("OK"));
        cancelButton = new JButton(bundle.getString("Cancel"));
        okButton.setForeground(ESlateContainerUtils.color128);
        cancelButton.setForeground(ESlateContainerUtils.color128);
        okButton.setMargin(zeroInsets);
        cancelButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel();
        ExplicitLayout el1 = new ExplicitLayout();
        buttonPanel.setLayout(el1);

        Component[] buttons = new Component[] {okButton, cancelButton};
        Expression butonsWidth = GroupEF.widthSum(buttons);

        Expression buttonWidthExp = MathEF.constant(ESlateContainerUtils.buttonSize.width);
        Expression buttonHeightExp = MathEF.constant(ESlateContainerUtils.buttonSize.height);

        ExplicitConstraints ec21 = new ExplicitConstraints(okButton);
        ec21.setHeight(buttonHeightExp);
        ec21.setWidth(buttonWidthExp);
        ec21.setY(ContainerEF.top(buttonPanel));

        ec21.setX(ContainerEF.width(buttonPanel).subtract(butonsWidth).subtract(H_GAP).divide(2));

        ExplicitConstraints ec22 = new ExplicitConstraints(cancelButton);
        ec22.setHeight(buttonHeightExp);
        ec22.setWidth(buttonWidthExp);
        ec22.setX(ComponentEF.right(okButton).add(H_GAP));
        ec22.setY(ComponentEF.top(okButton));

        buttonPanel.add(okButton, ec21);
        buttonPanel.add(cancelButton, ec22);

        el1.setPreferredLayoutSize(ContainerEF.width(buttonPanel.getParent()), buttonHeightExp);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(5));

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                ProfileDialog.this.container.containerUtils.discardDialogButtonListeners(ProfileDialog.this);
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
                ProfileDialog.this.container.containerUtils.discardDialogButtonListeners(ProfileDialog.this);
            }
        });

        profileList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                int selIndex = profileList.getSelectedIndex();
                MicroworldSettingsProfile pr = (MicroworldSettingsProfile) profileList.getSelectedValue();
                if (selIndex == -1) {
                    renameButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    exportButton.setEnabled(false);
                    okButton.setEnabled(false);
                }else{
                    if (selIndex < 2) {
                        renameButton.setEnabled(false);
                        removeButton.setEnabled(false);
                    }else{
                        renameButton.setEnabled(true);
                        removeButton.setEnabled(true);
                    }
                    exportButton.setEnabled(true);
                    okButton.setEnabled(true);
                }
            }
        });

        renameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MicroworldSettingsProfile profile = (MicroworldSettingsProfile) profileList.getSelectedValue();
                if (profile == null)
                    return;
                String currentName = profile.getName();
                String newName = (String) ESlateOptionPane.showInputDialog(ProfileDialog.this,
                                      bundle.getString("RenameMsg"),
                                      bundle.getString("RenameTitle"),
                                      JOptionPane.QUESTION_MESSAGE,
                                      null,
                                      null,
                                      currentName);
                if (newName == null || newName.trim().length() == 0)
                    return;
                profile.setName(newName);
                profileList.repaint();
            }
        });
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selIndex = profileList.getSelectedIndex();
                if (selIndex <= 2) return;
                ProfileDialog.this.container.containerUtils.selectNewBeforeSelectedEntryIsRemoved(profileList);
                profileModel.remove(selIndex);
                if (profileList.getSelectedIndex() == -1) {
                    removeButton.setEnabled(false);
                    renameButton.setEnabled(false);
                    importButton.setEnabled(false);
                    exportButton.setEnabled(false);
                    okButton.setEnabled(false);
                }
                profileList.repaint();
            }
        });
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MicroworldSettingsProfile currProfile = (MicroworldSettingsProfile) profileList.getSelectedValue();
                if (currProfile == null) return;
//System.out.println("exportButton currProfile.getName(): "+ currProfile.getName() + ", currProfile: " + currProfile);
                MicroworldSettingsProfile profile = new MicroworldSettingsProfile(currProfile);

                /* First let the user specify the name for the stored profile, if this
                 * needs to be done.
                 */
                String currentName = profile.getName();
//System.out.println("currentName: " + currentName + ", profile: " + profile);
                if ((ESlateOptionPane.showConfirmDialog(ProfileDialog.this, bundle.getString("PromptRename") + currentName + "\"" + bundle.getString("QuestionMark"),
                  bundle.getString("PromptRenameTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {
                    String newName = (String) ESlateOptionPane.showInputDialog(ProfileDialog.this,
                                          bundle.getString("NameMsg"),
                                          bundle.getString("MameTitle"),
                                          JOptionPane.QUESTION_MESSAGE,
                                          null,
                                          null,
                                          currentName);
                    if (newName != null)
                        profile.setName(newName);
                }

                String propertyFileName = ProfileDialog.this.container.getSystemFile(true,
                                                          bundle.getString("SaveProfileTitle"),
                                                          profile.getName() + ".ems",
                                                          new String[] {"ems"}
                                                          );
                if (propertyFileName == null) return;
//System.out.println("Exporting profile: " + profile);
                try{
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(propertyFileName));
                    profile.store(bos, profile.getName());
                    bos.flush();
                    bos.close();
                }catch (IOException exc) {
                    ESlateOptionPane.showMessageDialog(ProfileDialog.this, bundle.getString("UnableToStoreSettings") + propertyFileName + '\"', bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    exc.printStackTrace();
                }
            }
        });
        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String propertyFileName = ProfileDialog.this.container.getSystemFile(false,
                                                          bundle.getString("LoadProfileTitle"),
                                                          "*.ems",
                                                          new String[] {"ems"}
                                                          );
                if (propertyFileName == null) return;
                MicroworldSettingsProfile profile = new MicroworldSettingsProfile();
//System.out.println("propertyFileName: " + propertyFileName);
                try{
                    File f = new File(propertyFileName);
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
                    profile.load(bis);
                    bis.close();
                    if (profile.getName().trim().length() == 0)
                        profile.setName(f.getName());
                    profile.fileName = f.getAbsolutePath();
//System.out.println("Imported profile: " + profile);
                    profileModel.addElement(profile);
                    profileList.setSelectedValue(profile, true);
                    profileList.repaint();
                }catch (IOException exc) {
                    ESlateOptionPane.showMessageDialog(ProfileDialog.this, bundle.getString("UnableToLoadSettings") + propertyFileName + '\"', bundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    exc.printStackTrace();
                }
            }
        });

        // ESCAPE HANDLER
        container.containerUtils.attachDialogButtonListener(this, cancelButton, java.awt.event.KeyEvent.VK_ESCAPE);
        // ENTER HANDLER
        container.containerUtils.attachDialogButtonListener(this, okButton, java.awt.event.KeyEvent.VK_ENTER);

        // Initialization
        MicroworldSettingsProfile runtimePr = MicroworldSettingsProfile.getRuntimeProfile();
        runtimePr.setName(bundle.getString("Runtime Profile"));
        profileModel.addElement(runtimePr);
        MicroworldSettingsProfile pr = new MicroworldSettingsProfile();
        pr.setName(bundle.getString("Design-time Profile"));
        profileModel.addElement(pr);
        pr = new MicroworldSettingsProfile(parentDialog.getProfile());
//System.out.println("Profile dialog current profile: " + parentDialog.getProfile());
//System.out.println();
        pr.setName(bundle.getString("Current Profile"));
        profileModel.addElement(pr);

        setResizable(false);
        okButton.setEnabled(false);
        renameButton.setEnabled(false);
        removeButton.setEnabled(false);
        exportButton.setEnabled(false);
        profileList.setSelectedIndex(2);
    }

    public MicroworldSettingsProfile getSelectedProfile() {
        return (MicroworldSettingsProfile) profileList.getSelectedValue();
    }

    /** The non-standard profiles are the profiles which have been recorded or imported from
     *  files. These do not include the design-time, the runtime and the current profile of
     *  the MwdPropertiesDialog, which are the top 3 profiles in the ProfileDialog.
     */
    public MicroworldSettingsProfile[] getNonStandardProfiles() {
        MicroworldSettingsProfile[] profiles = new MicroworldSettingsProfile[profileModel.size()-3];
//System.out.println("profiles.length: " + profiles.length);
        for (int i=0; i<profiles.length; i++) {
//System.out.println("profileModel.get(i+3): " + profileModel.get(i+3));
            profiles[i] = (MicroworldSettingsProfile) profileModel.get(i+3);
        }
        return profiles;
    }

    /** Sets the non-standard profiles of the ProfileDialog */
    public void setNonStandardProfiles(MicroworldSettingsProfile[] profiles) {
        if (profiles == null) return;
        for (int i=0; i<profiles.length; i++)
            profileModel.addElement(profiles[i]);
//for (int i=0; i<profileModel.size(); i++)
//    System.out.println("profileModel.get(" + i + "): " + profileModel.get(i));
    }

    public int getReturnCode() {
        return returnCode;
    }

    class ProfileListCellRenderer extends javax.swing.DefaultListCellRenderer {
        public java.awt.Component getListCellRendererComponent(JList list,
                                                               Object value,
                                                               int index,
                                                               boolean isSelected,
                                                               boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            MicroworldSettingsProfile profile = (MicroworldSettingsProfile) value;
            setText(profile.getName());
            return this;
        }
    }

}