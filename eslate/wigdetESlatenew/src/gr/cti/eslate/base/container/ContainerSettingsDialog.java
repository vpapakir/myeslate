package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.sound.ESlateSound;
import gr.cti.eslate.utils.sound.ESlateSoundEvent;
import gr.cti.eslate.utils.sound.ESlateSoundListener;
import gr.cti.eslate.utils.sound.SoundUtils;
import gr.cti.structfile.StructFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.*;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.objectspace.jgl.OrderedMap;
import com.thwt.layout.Anchor;
import com.thwt.layout.LayoutConstraint;
import com.thwt.layout.SmartLayout;


public class ContainerSettingsDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;

    Locale locale;
    ResourceBundle settingsDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    private boolean localeIsGreek = false;
    protected int returnCode = DIALOG_CANCELLED;

    WebFileDialog webFileDialog = null;
    ESlateContainer container;

    OrderedMap installedComponents, componentAvailability;
////nikosM
    OrderedMap webSites, webSiteAvailability/*, webSiteCommonDirs*/;
////nikosM end
//    InstalledComponentStructure installedCompos;

    InstalledComponentTable installedComponentTable;
    InstalledLookAndFeelTable installedLookAndFeelTable;
    JTable webSiteTable;
    JScrollPane scrollPane, scrollPaneForLookAndFeel, scrollPane2;
    JButton addComponent, removeComponent, editComponent, openMicroworldFile, deleteMicroworldFile;
    JButton addLookAndFeel, removeLookAndFeel, editLookAndFeel;
    JButton addSite, removeSite, editSite;
    JLabel preloadlb, preloadSitelb;
    JTextField preloadField, preloadSiteField;
    JButton okButton;
    JTabbedPane tabs;
    SoundSettingsPanel soundPanel;
//    JList systemSoundList, defaultSoundList;
//    SoundMgmtPanel soundControlPanel;
//    SoundThemeControlPanel soundThemeControlPanel;
    int tabHeight = 0;


  public ContainerSettingsDialog(java.awt.Frame f, ESlateContainer container) {
        super(f, true);
        this.container = container;

        locale = Locale.getDefault();
        settingsDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ContainerSettingsDialogBundle", locale);
//        if (settingsDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.ContainerSettingsDialogBundle_el_GR"))
//            localeIsGreek = true;

        setTitle(settingsDialogBundle.getString("DialogTitle"));

////for components
        InstalledComponentTableModel dataModel = new InstalledComponentTableModel(null, settingsDialogBundle);
        // Create the table
        installedComponentTable = new InstalledComponentTable(dataModel);
        installedComponentTable.setColumnSelectionAllowed(false);
//        installedComponentTable.setCellSelectionEnabled(false);
        installedComponentTable.setRowSelectionAllowed(true);
        installedComponentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        installedComponentTable.getTableHeader().setReorderingAllowed(true);
        installedComponentTable.setShowGrid(false);
        installedComponentTable.setRowHeight(18);

        scrollPane = new JScrollPane(installedComponentTable);

        addComponent = new JButton(settingsDialogBundle.getString("AddComponent"));
        removeComponent = new JButton(settingsDialogBundle.getString("RemoveComponent"));
        editComponent = new JButton(settingsDialogBundle.getString("EditComponent"));

/*        if (localeIsGreek) {
            addComponent.setFont(greekUIFont);
            removeComponent.setFont(greekUIFont);
            editComponent.setFont(greekUIFont);
        }
*/
        Color color128 = new Color(0, 0, 128);
        addComponent.setForeground(color128);
        removeComponent.setForeground(color128);
        editComponent.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        addComponent.setMaximumSize(buttonSize);
        addComponent.setPreferredSize(buttonSize);
        addComponent.setMinimumSize(buttonSize);
        removeComponent.setMaximumSize(buttonSize);
        removeComponent.setPreferredSize(buttonSize);
        removeComponent.setMinimumSize(buttonSize);
        editComponent.setMaximumSize(buttonSize);
        editComponent.setPreferredSize(buttonSize);
        editComponent.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        addComponent.setMargin(zeroInsets);
        removeComponent.setMargin(zeroInsets);
        editComponent.setMargin(zeroInsets);

        JPanel tableButtonPanel = new JPanel(true);
        tableButtonPanel.setLayout(new BoxLayout(tableButtonPanel, BoxLayout.Y_AXIS));
//        tableButtonPanel.add(Box.createGlue());
        tableButtonPanel.add(addComponent);
        tableButtonPanel.add(Box.createVerticalStrut(7));
        tableButtonPanel.add(removeComponent);
        tableButtonPanel.add(Box.createVerticalStrut(7));
        tableButtonPanel.add(editComponent);
        tableButtonPanel.add(Box.createGlue());
        tableButtonPanel.setBorder(new EmptyBorder(0, 9, 0, 8));

        JPanel installedComponentPanel = new JPanel(true);
        installedComponentPanel.setLayout(new BorderLayout());
        installedComponentPanel.add(scrollPane, BorderLayout.CENTER);
        installedComponentPanel.add(tableButtonPanel, BorderLayout.EAST);

//        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(settingsDialogBundle.getString("InstalledComponents"));
        tb1.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb1.setTitleFont(greekUIFont);
        installedComponentPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,0,5,0)));

        Dimension dim = new Dimension(550, 200); //130);
        Dimension dim2 = new Dimension(550, 130);
        installedComponentPanel.setMaximumSize(dim);
        installedComponentPanel.setMinimumSize(dim);
        installedComponentPanel.setPreferredSize(dim);
        tabHeight = tabHeight + 200;

////for look-and-feels
        InstalledLookAndFeelTableModel dataLAFModel = new InstalledLookAndFeelTableModel(null, settingsDialogBundle);
        // Create the table
        installedLookAndFeelTable = new InstalledLookAndFeelTable(dataLAFModel);
        installedLookAndFeelTable.setColumnSelectionAllowed(false);
//        installedComponentTable.setCellSelectionEnabled(false);
        installedLookAndFeelTable.setRowSelectionAllowed(true);
        installedLookAndFeelTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        installedLookAndFeelTable.getTableHeader().setReorderingAllowed(true);
        installedLookAndFeelTable.setShowGrid(false);
        installedLookAndFeelTable.setRowHeight(18);

        scrollPaneForLookAndFeel = new JScrollPane(installedLookAndFeelTable);

        addLookAndFeel = new JButton(settingsDialogBundle.getString("AddComponent"));
        removeLookAndFeel = new JButton(settingsDialogBundle.getString("RemoveComponent"));
        editLookAndFeel = new JButton(settingsDialogBundle.getString("EditComponent"));

/*        if (localeIsGreek) {
            addLookAndFeel.setFont(greekUIFont);
            removeLookAndFeel.setFont(greekUIFont);
            editLookAndFeel.setFont(greekUIFont);
        }
*/
        addLookAndFeel.setForeground(color128);
        removeLookAndFeel.setForeground(color128);
        editLookAndFeel.setForeground(color128);
        addLookAndFeel.setMaximumSize(buttonSize);
        addLookAndFeel.setPreferredSize(buttonSize);
        addLookAndFeel.setMinimumSize(buttonSize);
        removeLookAndFeel.setMaximumSize(buttonSize);
        removeLookAndFeel.setPreferredSize(buttonSize);
        removeLookAndFeel.setMinimumSize(buttonSize);
        editLookAndFeel.setMaximumSize(buttonSize);
        editLookAndFeel.setPreferredSize(buttonSize);
        editLookAndFeel.setMinimumSize(buttonSize);
        addLookAndFeel.setMargin(zeroInsets);
        removeLookAndFeel.setMargin(zeroInsets);
        editLookAndFeel.setMargin(zeroInsets);

        JPanel tableLookAndFeelButtonPanel = new JPanel(true);
        tableLookAndFeelButtonPanel.setLayout(new BoxLayout(tableLookAndFeelButtonPanel, BoxLayout.Y_AXIS));
//        tableButtonPanel.add(Box.createGlue());
        tableLookAndFeelButtonPanel.add(addLookAndFeel);
        tableLookAndFeelButtonPanel.add(Box.createVerticalStrut(7));
        tableLookAndFeelButtonPanel.add(removeLookAndFeel);
        tableLookAndFeelButtonPanel.add(Box.createVerticalStrut(7));
        tableLookAndFeelButtonPanel.add(editLookAndFeel);
        tableLookAndFeelButtonPanel.add(Box.createGlue());
        tableLookAndFeelButtonPanel.setBorder(new EmptyBorder(0, 9, 0, 8));

        JPanel installedLookAndFeelPanel = new JPanel(true);
        installedLookAndFeelPanel.setLayout(new BorderLayout());
        installedLookAndFeelPanel.add(scrollPaneForLookAndFeel, BorderLayout.CENTER);
        installedLookAndFeelPanel.add(tableLookAndFeelButtonPanel, BorderLayout.EAST);

        TitledBorder tb2 = new TitledBorder(settingsDialogBundle.getString("InstalledLookAndFeels"));
        tb2.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb2.setTitleFont(greekUIFont);
        installedLookAndFeelPanel.setBorder(new CompoundBorder(tb2, new EmptyBorder(0,0,5,0)));

        installedLookAndFeelPanel.setMaximumSize(dim2);
        installedLookAndFeelPanel.setMinimumSize(dim2);
        installedLookAndFeelPanel.setPreferredSize(dim2);
        tabHeight = tabHeight + dim2.height;

        //WEB SITE PANEL
        TableModel webSiteDataModel = new AbstractTableModel() {
//// nikosM
//            public int getColumnCount() { return 4; }
            public int getColumnCount() { return 3; }
//// nikosM end of change
            public int getRowCount() {
                if (webSites != null)
                    return webSites.size();
                else
                    return 0;
            }
            public Object getValueAt(int row, int col) {
//                System.out.println("row: " + row + ", col: " + col);
                if (col == 0) {
                    Enumeration en = webSites.keys();
                    int i = 0;
                    while (en.hasMoreElements() && i < row) {
                        en.nextElement();
                        i++;
                    }
                    if (!en.hasMoreElements())
                        return null;
                    return en.nextElement();
                }else if (col == 1) {
                    Enumeration en = webSites.keys();
                    int i = 0;
                    while (en.hasMoreElements() && i < row) {
                        en.nextElement();
                        i++;
                    }
                    if (!en.hasMoreElements())
                        return null;
                    return webSites.get(en.nextElement());
////nikosM
/*                }else if (col == 2) {
                    Enumeration enum = webSiteCommonDirs.keys();
                    int i = 0;
                    while (enum.hasMoreElements() && i < row) {
                        enum.nextElement();
                        i++;
                    }
                    if (!enum.hasMoreElements())
                        return null;
                    return webSiteCommonDirs.get(enum.nextElement());
*/
////nikosM
                }else{
                    Enumeration en = webSiteAvailability.keys();
                    int i = 0;
                    while (en.hasMoreElements() && i < row) {
                        en.nextElement();
                        i++;
                    }
                    if (!en.hasMoreElements())
                        return null;
                    return webSiteAvailability.get(en.nextElement());
                }
            }
            public String getColumnName(int column) {
                if (column == 0)
                    return settingsDialogBundle.getString("SiteNameColumn");
                else if (column == 1)
                    return settingsDialogBundle.getString("WebSiteColumn");
////nikosM
//                else if (column == 2)
//                    return settingsDialogBundle.getString("CommonDirColumn");
////nikosM end
                else
                    return settingsDialogBundle.getString("SiteAvailabilityColumn");
            }
            public Class getColumnClass(int c) {
////nikosM
//                if (c == 3)
                if (c == 2)
////nikosM end
                    return java.lang.Boolean.class;
                else
                    return java.lang.String.class;
            }
            public boolean isCellEditable(int row, int c) {
////nikosM
//                if (c == 3)
                if (c == 2)
////nikosM end
                    return true;
                return false;
            }
            public void setValueAt(Object aValue, int row, int column) {
//                System.out.println("setValueAt called: " + aValue.getClass());
////nikosM
//                if (column == 3) {
                if (column == 2) {
////nikosM end
                    Enumeration en = webSiteAvailability.keys();
                    int i = 0;
                    while (en.hasMoreElements() && i < row) {
                        en.nextElement();
                        i++;
                    }
                    if (!en.hasMoreElements())
                        return;
                    webSiteAvailability.put(en.nextElement(), aValue);
                }
            }
         };


        // Create the web site table
        webSiteTable = new FocusTraverableJTable(webSiteDataModel);
        webSiteTable.setColumnSelectionAllowed(false);
        webSiteTable.setCellSelectionEnabled(false);
        webSiteTable.setColumnSelectionAllowed(false);
//nikosM
        webSiteTable.setRowSelectionAllowed(true);
        webSiteTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        webSiteTable.getTableHeader().setReorderingAllowed(false);
        webSiteTable.setShowGrid(false);
        webSiteTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        webSiteTable.getColumn(settingsDialogBundle.getString("SiteNameColumn")).setPreferredWidth(70);
        webSiteTable.getColumn(settingsDialogBundle.getString("WebSiteColumn")).setPreferredWidth(250);
//        webSiteTable.getColumn(settingsDialogBundle.getString("CommonDirColumn")).setPreferredWidth(100);
//        webSiteTable.getColumn(settingsDialogBundle.getString("SiteAvailabilityColumn")).setPreferredWidth(100);
        webSiteTable.getColumn(settingsDialogBundle.getString("SiteAvailabilityColumn")).setPreferredWidth(107);
////nikosM end of change
/*         if (localeIsGreek) {
            webSiteTable.setFont(greekUIFont);
            webSiteTable.getTableHeader().setFont(greekUIFont);
        }
*/
        webSiteTable.setRowHeight(17);

        scrollPane2 = new JScrollPane(webSiteTable);

        addSite = new JButton(settingsDialogBundle.getString("AddSite"));
        removeSite = new JButton(settingsDialogBundle.getString("RemoveSite"));
        editSite = new JButton(settingsDialogBundle.getString("EditSite"));

/*        if (localeIsGreek) {
            addSite.setFont(greekUIFont);
            removeSite.setFont(greekUIFont);
            editSite.setFont(greekUIFont);
        }
*/
        addSite.setForeground(color128);
        removeSite.setForeground(color128);
        editSite.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        addSite.setMaximumSize(buttonSize);
        addSite.setPreferredSize(buttonSize);
        addSite.setMinimumSize(buttonSize);
        removeSite.setMaximumSize(buttonSize);
        removeSite.setPreferredSize(buttonSize);
        removeSite.setMinimumSize(buttonSize);
        editSite.setMaximumSize(buttonSize);
        editSite.setPreferredSize(buttonSize);
        editSite.setMinimumSize(buttonSize);
        addSite.setMargin(zeroInsets);
        removeSite.setMargin(zeroInsets);
        editSite.setMargin(zeroInsets);

        JPanel webSiteButtonPanel = new JPanel(true);
        webSiteButtonPanel.setLayout(new BoxLayout(webSiteButtonPanel, BoxLayout.Y_AXIS));
        webSiteButtonPanel.add(Box.createGlue());
        webSiteButtonPanel.add(addSite);
        webSiteButtonPanel.add(Box.createVerticalStrut(7));
        webSiteButtonPanel.add(removeSite);
        webSiteButtonPanel.add(Box.createVerticalStrut(7));
        webSiteButtonPanel.add(editSite);
        webSiteButtonPanel.add(Box.createGlue());
        webSiteButtonPanel.setBorder(new EmptyBorder(0, 9, 0, 8));

        JPanel webSitePanel = new JPanel(true);
        webSitePanel.setLayout(new BorderLayout());
        webSitePanel.add(scrollPane2, BorderLayout.CENTER);
        webSitePanel.add(webSiteButtonPanel, BorderLayout.EAST);

        TitledBorder tb3 = new TitledBorder(settingsDialogBundle.getString("WebSites"));
        tb3.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb3.setTitleFont(greekUIFont);
        webSitePanel.setBorder(new CompoundBorder(tb3, new EmptyBorder(0,0,5,0)));

        dim = new Dimension(550, 130);
        webSitePanel.setMaximumSize(dim);
        webSitePanel.setMinimumSize(dim);
        webSitePanel.setPreferredSize(dim);
        tabHeight = tabHeight + 130;

        // PRELOAD FILE TEXTFIELD
        preloadlb = new JLabel(settingsDialogBundle.getString("PreloadFile"));
//        if (localeIsGreek)
//            preloadlb.setFont(greekUIFont);

        preloadField = new MyJTextField();
//        if (localeIsGreek)
//            preloadField.setFont(greekUIFont);
        dim = new Dimension(300, 20);
        preloadField.setMaximumSize(dim);
        preloadField.setMinimumSize(dim);
        preloadField.setPreferredSize(dim);
        preloadField.setEditable(false);
        preloadField.setBackground(Color.lightGray);
        preloadField.setForeground(Color.black);

        // PRELOAD WEB SITE
        preloadSitelb = new JLabel(settingsDialogBundle.getString("PreloadSite"));
//        if (localeIsGreek)
//            preloadSitelb.setFont(greekUIFont);

        // Set the width of "preloadlb" and "preloadSitelb" to the same value
        FontMetrics fm1 = getToolkit().getFontMetrics(preloadlb.getFont());
        if (fm1.stringWidth(preloadSitelb.getText()) > fm1.stringWidth(preloadlb.getText()))
            dim = new Dimension(fm1.stringWidth(preloadSitelb.getText()), 20);
        else
            dim = new Dimension(fm1.stringWidth(preloadlb.getText()), 20);
        preloadlb.setMaximumSize(dim);
        preloadlb.setPreferredSize(dim);
        preloadlb.setMinimumSize(dim);
        preloadSitelb.setMaximumSize(dim);
        preloadSitelb.setPreferredSize(dim);
        preloadSitelb.setMinimumSize(dim);

        preloadSiteField = new MyJTextField();
//        if (localeIsGreek)
//            preloadSiteField.setFont(greekUIFont);
        dim = new Dimension(300, 20);
        preloadSiteField.setMaximumSize(dim);
        preloadSiteField.setMinimumSize(dim);
        preloadSiteField.setPreferredSize(dim);
        preloadSiteField.setEditable(false);
        preloadSiteField.setBackground(Color.lightGray);
        preloadSiteField.setForeground(Color.black);

        openMicroworldFile = new JButton(new ImageIcon(getClass().getResource("images/openFile.gif")));
        buttonSize = new Dimension(22, 20);
        openMicroworldFile.setMaximumSize(buttonSize);
        openMicroworldFile.setPreferredSize(buttonSize);
        openMicroworldFile.setMinimumSize(buttonSize);
        openMicroworldFile.setMargin(new Insets(0, 0, 1, 1));
        openMicroworldFile.setFocusPainted(false);

        deleteMicroworldFile = new JButton(new ImageIcon(getClass().getResource("images/delete.gif")));
        buttonSize = new Dimension(22, 20);
        deleteMicroworldFile.setMaximumSize(buttonSize);
        deleteMicroworldFile.setPreferredSize(buttonSize);
        deleteMicroworldFile.setMinimumSize(buttonSize);
        deleteMicroworldFile.setMargin(new Insets(0, 0, 1, 1));
        deleteMicroworldFile.setFocusPainted(false);

        JPanel preloadSitePanel = new JPanel(true);
        preloadSitePanel.setLayout(new BoxLayout(preloadSitePanel, BoxLayout.X_AXIS));

        preloadSitePanel.add(preloadSitelb);
        preloadSitePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        preloadSitePanel.add(preloadSiteField);

        JPanel preloadFilePanel = new JPanel(true);
        preloadFilePanel.setLayout(new BoxLayout(preloadFilePanel, BoxLayout.X_AXIS));

        preloadFilePanel.add(preloadlb);
        preloadFilePanel.add(Box.createRigidArea(new Dimension(5, 20)));
        preloadFilePanel.add(preloadField);

        JPanel preloadInfoPanel = new JPanel(true);
        preloadInfoPanel.setLayout(new BoxLayout(preloadInfoPanel, BoxLayout.Y_AXIS));

        preloadInfoPanel.add(preloadSitePanel);
        preloadInfoPanel.add(Box.createVerticalStrut(3));
        preloadInfoPanel.add(preloadFilePanel);

        JPanel preloadButtonPanel = new JPanel(true);
        preloadButtonPanel.setLayout(new BoxLayout(preloadButtonPanel, BoxLayout.X_AXIS));

        preloadButtonPanel.add(openMicroworldFile);
        preloadButtonPanel.add(Box.createHorizontalStrut(5));
        preloadButtonPanel.add(deleteMicroworldFile);

        JPanel preloadPanel = new JPanel(true);
        preloadPanel.setLayout(new BoxLayout(preloadPanel, BoxLayout.X_AXIS));

        preloadPanel.add(Box.createGlue());
        preloadPanel.add(preloadInfoPanel);
//        preloadPanel.add(Box.createRigidArea(new Dimension(5, 20)));
//        preloadPanel.add(preloadField);
//        preloadPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        preloadPanel.add(preloadButtonPanel);
        preloadPanel.add(Box.createGlue());

        TitledBorder tb4 = new TitledBorder(settingsDialogBundle.getString("PreloadMicroworld"));
        tb4.setTitleColor(ESlateContainerUtils.titleBorderColor);
//        if (localeIsGreek)
//            tb4.setTitleFont(greekUIFont);
        preloadPanel.setBorder(new CompoundBorder(tb4, new EmptyBorder(0,0,5,0)));

        tabHeight = tabHeight + 20+20+32+5;
        Dimension dim3 = new Dimension(550, 20+20+32+5);
        preloadPanel.setPreferredSize(dim3);
        preloadPanel.setMaximumSize(dim3);
        preloadPanel.setMinimumSize(dim3);

        // The button panel (OK, CANCEL)
        okButton = new JButton(settingsDialogBundle.getString("OK"));
//        if (localeIsGreek)
//            okButton.setFont(greekUIFont);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(settingsDialogBundle.getString("Cancel"));
//        if (localeIsGreek)
//            cancelButton.setFont(greekUIFont);
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        // The general settings panel
        JPanel genSettingsPanel = new JPanel(true);
        genSettingsPanel.setLayout(new BoxLayout(genSettingsPanel, BoxLayout.Y_AXIS));

        genSettingsPanel.add(installedComponentPanel);
        genSettingsPanel.add(Box.createVerticalStrut(5));
        genSettingsPanel.add(installedLookAndFeelPanel);
        genSettingsPanel.add(Box.createVerticalStrut(5));
        genSettingsPanel.add(webSitePanel);
        genSettingsPanel.add(Box.createVerticalStrut(5));
        genSettingsPanel.add(preloadPanel);
        genSettingsPanel.add(Box.createVerticalStrut(8));

        // THE SOUND SETTINGS PANEL
        soundPanel = new SoundSettingsPanel(this, container);

        tabs = new JTabbedPane();
        tabs.addTab(settingsDialogBundle.getString("GeneralSettings"), genSettingsPanel);
        tabs.addTab(settingsDialogBundle.getString("SoundSettings"), soundPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));// new BorderLayout());
        mainPanel.add(tabs);//, BorderLayout.CENTER);
        mainPanel.add(buttonPanel);//, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                soundPanel.soundControlPanel.stopSound();

                /* If the ContainerSettingsDialog closes while one of the themes in the
                 * SoundThemeControlPanel is modified, but not saved, then save this theme
                 * before exiting the dialog.
                 */
                if (soundPanel.currentThemeFile != null &&
                soundPanel.currentThemeFile.equals(soundPanel.soundThemeControlPanel.themeList.getSelectedValue()) &&
                soundPanel.currentTheme.isModified()) {
                    int option = soundPanel.promptToSaveCurrentTheme();
                    if (option == ESlateContainer.CANCEL)
                        return;
                    if (option == ESlateContainer.SAVE)
                        soundPanel.saveCurrentTheme(false);
                }

                returnCode = DIALOG_OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                soundPanel.soundControlPanel.stopSound();
                returnCode = DIALOG_CANCELLED;
                dispose();
            }
        });

        installedComponentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (installedComponentTable.getRowCount() == 0 ||
                    installedComponentTable.getSelectedRowCount() == 0) {
                    removeComponent.setEnabled(false);
                    editComponent.setEnabled(false);
                }else{
                    addComponent.setNextFocusableComponent(removeComponent);
                    removeComponent.setEnabled(true);
                    editComponent.setEnabled(true);
                }
            }
        });

        installedComponentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2)
                    editComponentAction();
            }
        });

        addComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InstallComponentDialog compDialog = new InstallComponentDialog(ContainerSettingsDialog.this);
//                ContainerSettingsDialog.this.container.modalDialog = compDialog;
                compDialog.showDialog(ContainerSettingsDialog.this);
//                ContainerSettingsDialog.this.container.modalDialog = ContainerSettingsDialog.this;
                if (compDialog.getReturnCode() == compDialog.DIALOG_OK) {
                    String compName = compDialog.getComponentName();
                    String className = compDialog.getClassName();



                    InstalledComponentStructure installedCompos = ((InstalledComponentTableModel) installedComponentTable.getModel()).getCompoStructure();
                    if (installedCompos==null)
                        System.out.println("CompoStructure null!");
                    if (installedCompos.containsComponentName(compName)) {
                        ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg1") + " " + compName + '.', settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (installedCompos.containsComponentClassName(className)) {
                        String compoName = installedCompos.getName(className);
                        ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg2") + " " + className + settingsDialogBundle.getString("SettingsDialogMsg3") + compoName + '.', settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try{
                        boolean visual = false;
                        try{
                            Class cls = Class.forName(className);
                            if (Component.class.isAssignableFrom(cls))
                                visual = true;
                        }catch (Throwable thr) {}
                        installedCompos.add(compName, className, true, visual,null);

                        ((InstalledComponentTableModel) installedComponentTable.getModel()).setCompoStructure(installedCompos);
                        int newRowIndex = ((InstalledComponentTableModel) installedComponentTable.getModel()).getRow(compName);
                        if (newRowIndex != -1) {
                            installedComponentTable.revalidate();
                            installedComponentTable.scrollRectToVisible(installedComponentTable.getCellRect(newRowIndex, 0, true));
                            installedComponentTable.setRowSelectionInterval(newRowIndex, newRowIndex);
                        }else
                            installedComponentTable.revalidate();
                    }catch (DuplicateEntryException exc) {}
                    if (installedComponentTable.getRowCount() > 0)
                        ((FocusTraverableJTable) installedComponentTable).setFocusTraversable(true);
                    else
                        ((FocusTraverableJTable) installedComponentTable).setFocusTraversable(false);
                }
            }
        });

        removeComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = installedComponentTable.getSelectedRow();
                if (selectedRow == -1)
                    return;

                InstalledComponentStructure installedCompos = ((InstalledComponentTableModel) installedComponentTable.getModel()).getCompoStructure();
                String compoName = (String) ((InstalledComponentTableModel) installedComponentTable.getModel()).getValueAt(selectedRow, 0);
//                System.out.println("Removing " + compoName);
//                System.out.println("Before: " + installedCompos);
                installedCompos.remove(compoName);
//                System.out.println("After: " + installedCompos);
                ((InstalledComponentTableModel) installedComponentTable.getModel()).setCompoStructure(installedCompos);
                int newRowIndex = -1;
                if (installedComponentTable.getModel().getRowCount() != 0) {
                    newRowIndex = (selectedRow != ((InstalledComponentTableModel) installedComponentTable.getModel()).getRowCount())? selectedRow:selectedRow-1;
                }
                if (newRowIndex != -1) {
                    installedComponentTable.scrollRectToVisible(installedComponentTable.getCellRect(newRowIndex, 0, true));
                    installedComponentTable.setRowSelectionInterval(newRowIndex, newRowIndex);
                }
                installedComponentTable.revalidate();

                if (installedComponentTable.getRowCount() > 0)
                    ((FocusTraverableJTable) installedComponentTable).setFocusTraversable(true);
                else
                    ((FocusTraverableJTable) installedComponentTable).setFocusTraversable(false);
                if (installedComponentTable.getRowCount() == 0) {
                    removeComponent.setEnabled(false);
                    editComponent.setEnabled(false);
                }
            }
        });

        editComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editComponentAction();
            }
        });
////////////////////////..and here are the L&F listeners\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        installedLookAndFeelTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (installedLookAndFeelTable.getRowCount() == 0 ||
                    installedLookAndFeelTable.getSelectedRowCount() == 0) {
                    removeLookAndFeel.setEnabled(false);
                    editLookAndFeel.setEnabled(false);
                }else{
                    addLookAndFeel.setNextFocusableComponent(removeLookAndFeel);
                    removeLookAndFeel.setEnabled(true);
                    editLookAndFeel.setEnabled(true);
                }
            }
        });
        installedLookAndFeelTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2)
                    editLookAndFeelAction();
            }
        });

        addLookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InstallLookAndFeelDialog lafDialog = new InstallLookAndFeelDialog(ContainerSettingsDialog.this);
//                ContainerSettingsDialog.this.container.modalDialog = lafDialog;
                lafDialog.showDialog(ContainerSettingsDialog.this);
//                ContainerSettingsDialog.this.container.modalDialog = ContainerSettingsDialog.this;
                if (lafDialog.getReturnCode() == lafDialog.DIALOG_OK) {
                    String lafName = lafDialog.getLookAndFeelName();
                    String className = lafDialog.getClassName();

                    InstalledLookAndFeelStructure installedLAFs = ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getLookAndFeelStructure();
                    if (installedLAFs==null)
                        System.out.println("Structure null!");
                    if (lafName==null)
                        System.out.println("lafName null!");

                    if (installedLAFs.containsLookAndFeelName(lafName)) {
                        ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg1") + " " + lafName + '.', settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (installedLAFs.containsLookAndFeelClassName(className)) {
                        String lafsName = installedLAFs.getName(className);
                        ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg2") + " " + className + settingsDialogBundle.getString("SettingsDialogMsg3") + lafsName + '.', settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try{
                        installedLAFs.add(lafName, className, true);
                        ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).setLookAndFeelStructure(installedLAFs);
                        int newRowIndex = ((InstalledComponentTableModel) installedComponentTable.getModel()).getRow(lafName);
                        if (newRowIndex != -1) {
                            installedLookAndFeelTable.revalidate();
                            installedLookAndFeelTable.scrollRectToVisible(installedLookAndFeelTable.getCellRect(newRowIndex, 0, true));
                            installedLookAndFeelTable.setRowSelectionInterval(newRowIndex, newRowIndex);
                        }else
                            installedLookAndFeelTable.revalidate();
                    }catch (DuplicateEntryException exc) {}
                    if (installedLookAndFeelTable.getRowCount() > 0)
                        ((FocusTraverableJTable) installedLookAndFeelTable).setFocusTraversable(true);
                    else
                        ((FocusTraverableJTable) installedLookAndFeelTable).setFocusTraversable(false);
                }
            }
        });

        removeLookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = installedLookAndFeelTable.getSelectedRow();
                if (selectedRow == -1)
                    return;

                InstalledLookAndFeelStructure installedLAFs = ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getLookAndFeelStructure();
                String lafName = (String) ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getValueAt(selectedRow, 0);
//                System.out.println("Removing " + compoName);
//                System.out.println("Before: " + installedCompos);
                installedLAFs.remove(lafName);
//                System.out.println("After: " + installedCompos);
                ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).setLookAndFeelStructure(installedLAFs);
                int newRowIndex = -1;
                if (installedComponentTable.getModel().getRowCount() != 0) {
                    newRowIndex = (selectedRow != ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getRowCount())? selectedRow:selectedRow-1;
                }
                if (newRowIndex != -1) {
                    installedLookAndFeelTable.scrollRectToVisible(installedLookAndFeelTable.getCellRect(newRowIndex, 0, true));
                    installedLookAndFeelTable.setRowSelectionInterval(newRowIndex, newRowIndex);
                }
                installedLookAndFeelTable.revalidate();

                if (installedLookAndFeelTable.getRowCount() > 0)
                    ((FocusTraverableJTable) installedLookAndFeelTable).setFocusTraversable(true);
                else
                    ((FocusTraverableJTable) installedLookAndFeelTable).setFocusTraversable(false);
                if (installedLookAndFeelTable.getRowCount() == 0) {
                    removeLookAndFeel.setEnabled(false);
                    editLookAndFeel.setEnabled(false);
                }
            }
        });

        editLookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editLookAndFeelAction();
            }
        });

//// nikosM
        webSiteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2)
                    editWebSites();
            }
        });
////nikosM end

        webSiteTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (webSiteTable.getRowCount() == 0 || webSiteTable.getSelectedRowCount() == 0) {
                    removeSite.setEnabled(false);
                    editSite.setEnabled(false);
                }else{
                    removeSite.setEnabled(true);
                    editSite.setEnabled(true);
                }
            }
        });

        addSite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InstallWebSiteDialog webSiteDialog = new InstallWebSiteDialog(ContainerSettingsDialog.this);
//                ContainerSettingsDialog.this.container.modalDialog = webSiteDialog;
                webSiteDialog.showDialog(ContainerSettingsDialog.this);
//                ContainerSettingsDialog.this.container.modalDialog = ContainerSettingsDialog.this;
                if (webSiteDialog.getReturnCode() == webSiteDialog.DIALOG_OK) {
                    String webSiteName = webSiteDialog.getWebSiteName();
                    String address = webSiteDialog.getAddress();
//// nikosM
//                    String commonDir = webSiteDialog.getCommonDir();
////nikosM end of change

                    if (webSites == null)
                        webSites = new OrderedMap(new com.objectspace.jgl.LessString());

                    if (webSites.count(webSiteName) != 0) {
                        ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg4") + webSiteName + settingsDialogBundle.getString("SettingsDialogMsg5"), settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (webSites.countValues(address) != 0) {
                        String key = null;
                        Enumeration keys = webSites.keys();
                        while (keys.hasMoreElements()) {
                            key = (String) keys.nextElement();
                            if (webSites.get(key).equals(address))
                                break;
                        }
                        ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg6") + address + settingsDialogBundle.getString("SettingsDialogMsg7") + key + "\"", settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    webSites.put(webSiteName, address);

                    if (webSiteAvailability == null)
                        webSiteAvailability = new OrderedMap(new com.objectspace.jgl.LessString());
                    webSiteAvailability.put(webSiteName, Boolean.TRUE);
////nikosM
//                    if (webSiteCommonDirs == null)
//                        webSiteCommonDirs = new OrderedMap(new com.objectspace.jgl.LessString());
////nikosM end
//                    if (commonDir.length() == 0)
//                        commonDir = null;
////nikosM
//                    webSiteCommonDirs.put(webSiteName, commonDir);
////nikosM end

                    webSiteTable.revalidate();
                    if (webSiteTable.getSelectedRow() != -1) {
                        removeSite.setEnabled(true);
                        editSite.setEnabled(true);
                    }else{
                        removeSite.setEnabled(false);
                        editSite.setEnabled(false);
                    }
                    if (webSiteTable.getRowCount() > 0)
                        ((FocusTraverableJTable) webSiteTable).setFocusTraversable(true);
                    else
                        ((FocusTraverableJTable) webSiteTable).setFocusTraversable(false);


//                System.out.println("webSites: " + webSites);
//                System.out.println("webSiteAvailability: " + webSiteAvailability);
//                System.out.println("webSiteCommonDirs: " + webSiteCommonDirs);
                }
            }
        });

        removeSite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = webSiteTable.getSelectedRow();
                if (selectedRow == -1)
                    return;
                Enumeration keys = webSites.keys();
                String key = (String) keys.nextElement();
                for (int i=0; i<selectedRow; i++)
                    key = (String) keys.nextElement();
                webSites.remove(key);
                webSiteAvailability.remove(key);
//// nikosM
//                webSiteCommonDirs.remove(key);
////nikosM end


//                System.out.println("webSites: " + webSites);
//                System.out.println("webSiteAvailability: " + webSiteAvailability);
//                System.out.println("webSiteCommonDirs: " + webSiteCommonDirs);
                webSiteTable.revalidate();
                webSiteTable.paintImmediately(webSiteTable.getVisibleRect());
//                System.out.println("rows: " + webSiteTable.getRowCount());
//                System.out.println("selected row: " + webSiteTable.getSelectedRow());
                if (webSiteTable.getRowCount() == 0) {
                    removeSite.setEnabled(false);
                    editSite.setEnabled(false);
                }else{
                    removeSite.setEnabled(true);
                    editSite.setEnabled(true);
                }
                if (webSiteTable.getRowCount() > 0)
                    ((FocusTraverableJTable) webSiteTable).setFocusTraversable(true);
                else
                    ((FocusTraverableJTable) webSiteTable).setFocusTraversable(false);

            }
        });

//nikosM
        editSite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)  {
                    editWebSites();
            }
        });
////nikosM end

        openMicroworldFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (webFileDialog == null) {
                    webFileDialog = new WebFileDialog(ContainerSettingsDialog.this.container, false); //settingsDialogBundle.getString("ChooseMicroworldFile"), ESlateFileDialog.LOAD);
                    webFileDialog.setFileFilter(new MicroworldFileFilter());
                }

                webFileDialog.setDialogTitle(settingsDialogBundle.getString("ChooseMicroworldFile"));
                if (webFileDialog.isShowing()) {
                    return;
                }
/*                webFileDialog.setFile("*.mwd");
                String[] extensions = new String[1];
                extensions[0] = "mwd";
                webFileDialog.setDefaultExtension(extensions);
*/
                int status = webFileDialog.showOpenDialog(ContainerSettingsDialog.this);
                if (status == javax.swing.JFileChooser.CANCEL_OPTION)
                    return;

                if (webFileDialog.getSelectedFile() == null)
                    return;

                java.io.File mwdFile = webFileDialog.getSelectedFile();
                if (webFileDialog.isRemoteFile()) {
//                    System.out.println("website: " + webFileDialog.getWebSite());
//                    System.out.println("webfile: " + webFileDialog.getWebFile());
                    String siteAddress = webFileDialog.getWebSite();
                    Enumeration en = ContainerSettingsDialog.this.container.webSites.keys();
                    String webSiteName = null;
                    boolean foundSiteName = false;
                    while (en.hasMoreElements()) {
                        webSiteName = (String) en.nextElement();
                        if (siteAddress.equals((String) webSites.get(webSiteName))) {
                            foundSiteName = true;
                            break;
                        }
                    }
                    if (!foundSiteName || webSiteName == null)
                        return;

                    preloadSiteField.setText(webSiteName);
                    preloadField.setText(webFileDialog.getWebFile());
                }else{
                    String microworldFileName = webFileDialog.getSelectedFile().getName();
                    if (microworldFileName != null) {
                        String currDirectory = webFileDialog.getCurrentDirectory().getPath() + java.io.File.separator;
                        microworldFileName = currDirectory+microworldFileName;
                        preloadField.setText(microworldFileName);
                        preloadSiteField.setText("");
                    }
                }
            }
        });

        deleteMicroworldFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                preloadSiteField.setText("");
                preloadField.setText("");
            }
        });

        //Initialize the Dialog
        editComponent.setEnabled(false);
        removeComponent.setEnabled(false);
        editSite.setEnabled(false);
        removeSite.setEnabled(false);
        removeLookAndFeel.setEnabled(false);
        editLookAndFeel.setEnabled(false);

        installedComponentTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        installedComponentTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        installedComponentTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        installedComponentTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, ActionEvent.SHIFT_MASK));
        webSiteTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        webSiteTable.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));

        // ENTER
        installedComponentTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = getRootPane().getDefaultButton().getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
        installedComponentTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = getRootPane().getDefaultButton().getModel();
                bm.setPressed(false);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
        installedComponentTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addComponent.requestFocus();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
        installedComponentTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                javax.swing.FocusManager.getCurrentManager().focusPreviousComponent(installedComponentTable);
                cancelButton.requestFocus();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, ActionEvent.SHIFT_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
        webSiteTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = getRootPane().getDefaultButton().getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
        webSiteTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = getRootPane().getDefaultButton().getModel();
                bm.setPressed(false);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
        webSiteTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSite.requestFocus();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
        webSiteTable.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (installedComponentTable.getRowCount() != 0 &&
                    installedComponentTable.getSelectedRowCount() != 0)
                    editComponent.requestFocus();
                else
                    addComponent.requestFocus();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_TAB, ActionEvent.SHIFT_MASK, false), JComponent.WHEN_IN_FOCUSED_WINDOW);

        cancelButton.setNextFocusableComponent(installedComponentTable);
        editComponent.setNextFocusableComponent(webSiteTable);

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

    }

    public void showDialog(Component comp) {
        pack();
        setResizable(false);

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
        installedComponentTable.requestFocus();
/*        if (installedComponentTable.getRowCount() == 0 ||
            installedComponentTable.getSelectedRowCount() == 0) {
            if (webSiteTable.getRowCount() != 0)
                addComponent.setNextFocusableComponent(webSiteTable);
            else
                addComponent.setNextFocusableComponent(addSite);
        }else{
            editComponent.setEnabled(true);
            if (webSiteTable.getRowCount() != 0)
                editComponent.setNextFocusableComponent(webSiteTable);
            else
                editComponent.setNextFocusableComponent(addSite);
        }
*/
//        container.modalDialog = this;
        show();
//        container.modalDialog = null;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setInstalledComponents(InstalledComponentStructure ics) {
        if (ics == null) throw new NullPointerException();
        ((InstalledComponentTableModel) installedComponentTable.getModel()).setCompoStructure(ics);
        installedComponentTable.validateHeader(ics);
    }

    public InstalledComponentStructure getInstalledComponents() {
        return ((InstalledComponentTableModel) installedComponentTable.getModel()).getCompoStructure();
    }

    public void setInstalledLAFs(InstalledLookAndFeelStructure ilafs) {
        if (ilafs == null) throw new NullPointerException();
        ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).setLookAndFeelStructure(ilafs);
        installedLookAndFeelTable.validateHeader(ilafs);
    }

    public InstalledLookAndFeelStructure getInstalledLookAndFeels() {
        return ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getLookAndFeelStructure();
    }

    public void setWebSites(OrderedMap webSites) {
        this.webSites = webSites;
        if (this.webSites.size() > 0)
            ((FocusTraverableJTable) webSiteTable).setFocusTraversable(true);
        else
            ((FocusTraverableJTable) webSiteTable).setFocusTraversable(false);;
    }

    public OrderedMap getWebSites() {
        return webSites;
    }

    public void setWebSiteAvailability(OrderedMap webSiteAvailability) {
        this.webSiteAvailability = webSiteAvailability;
    }

    public OrderedMap getWebSiteAvailability() {
        return webSiteAvailability;
    }

////nikosM
//    public void setWebSiteCommonDirs(OrderedMap commonDirs) {
//        this.webSiteCommonDirs = commonDirs;
//    }

//    public OrderedMap getWebSiteCommonDirs() {
//        return webSiteCommonDirs;
//    }
////nikosM end

    public void setPreloadedMicroworld(String microworldFile) {
        preloadField.setText(microworldFile);
    }

    public void setPreloadedMicroworldSite(String microworldSite) {
        preloadSiteField.setText(microworldSite);
    }

    public String getPreloadedMicroworld() {
        String micrFile = preloadField.getText();
        if (micrFile != null && micrFile.equals(""))
            return null;
        return micrFile;
    }

    public String getPreloadedMicroworldSite() {
        String micrSite = preloadSiteField.getText();
        if (micrSite != null && micrSite.equals(""))
            return null;
        return micrSite;
    }

////nikosM
    void editWebSites() {
                int selectedRow = webSiteTable.getSelectedRow();
                if (selectedRow == -1)
                    return;
                Enumeration keys = webSites.keys();
                String key = (String) keys.nextElement();
                for (int i=0; i<selectedRow; i++)
                    key = (String) keys.nextElement();
                InstallWebSiteDialog webSiteDialog = new InstallWebSiteDialog(ContainerSettingsDialog.this);
                webSiteDialog.setWebSiteName(key);
                webSiteDialog.setAddress((String) webSites.get(key));
                webSiteDialog.showDialog(ContainerSettingsDialog.this);
                if (webSiteDialog.getReturnCode() == webSiteDialog.DIALOG_OK) {
                    String webSiteName = webSiteDialog.getWebSiteName();
                    String address = webSiteDialog.getAddress();
                    if (!webSiteName.equals(key)) {
                        /* Check if the new web site name already exists or this address
                         * is already registered.
                         */
                        if (webSites.count(webSiteName) != 0) {
                            ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg4") + webSiteName + settingsDialogBundle.getString("SettingsDialogMsg5"), settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (!address.equals((String) webSites.get(key)) && webSites.countValues(address) != 0) {
                            String key2 = null;
                            Enumeration keys2 = webSites.keys();
                            while (keys2.hasMoreElements()) {
                                key2 = (String) keys2.nextElement();
                                if (webSites.get(key2).equals(address))
                                    break;
                            }
                            ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg6") + address + settingsDialogBundle.getString("SettingsDialogMsg7") + key2 + "\"", settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        webSites.remove(key);
                        Boolean available =  (Boolean) webSiteAvailability.get(key);
                        webSiteAvailability.remove(key);
                        webSites.add(webSiteName, address);
                        webSiteAvailability.add(webSiteName, available);
                    }else if (!address.equals((String) webSites.get(key))) {
                        /* If the address has been altered, check if there exists another
                         * web site name with the new address.
                         */
                         if (webSites.countValues(address) != 0) {
                            String key2 = null;
                            Enumeration keys2 = webSites.keys();
                            while (keys2.hasMoreElements()) {
                                key2 = (String) keys2.nextElement();
                                if (webSites.get(key2).equals(address))
                                    break;
                            }
                            ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg6") + address + settingsDialogBundle.getString("SettingsDialogMsg7") + key2 + "\"", settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        webSites.put(webSiteName, address);
                    }
                    webSiteTable.invalidate();
                    webSiteTable.paintImmediately(webSiteTable.getVisibleRect());
                }
   }
////nikosM end

    void editComponentAction() {
        int selectedRow = installedComponentTable.getSelectedRow();
        if (selectedRow == -1)
            return;
        String oldCompoName = (String) ((InstalledComponentTableModel) installedComponentTable.getModel()).getValueAt(selectedRow, 0);
        String oldClassName = (String) ((InstalledComponentTableModel) installedComponentTable.getModel()).getValueAt(selectedRow, 1);

        InstallComponentDialog compDialog = new InstallComponentDialog(ContainerSettingsDialog.this);
        compDialog.setComponentName(oldCompoName);
        compDialog.setClassName(oldClassName);
//        container.modalDialog = compDialog;
        compDialog.showDialog(ContainerSettingsDialog.this);
//        container.modalDialog = ContainerSettingsDialog.this;
        if (compDialog.getReturnCode() == compDialog.DIALOG_OK) {
            InstalledComponentStructure installedCompos = ((InstalledComponentTableModel) installedComponentTable.getModel()).getCompoStructure();
            String compoName = compDialog.getComponentName();
            String className = compDialog.getClassName();
            int currRowIndex = ((InstalledComponentTableModel) installedComponentTable.getModel()).getRow(oldCompoName);

            if (!compoName.equals(oldCompoName)) {
                /* Check if the new component name already exists or the classname
                 * is already registered.
                 */
                if (installedCompos.containsComponentName(compoName)) {
                    ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg1") + " " + compoName, settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!className.equals(oldClassName) && installedCompos.containsComponentClassName(className)) {
                    String s = installedCompos.getName(className);
                    ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg2") + " " + className + settingsDialogBundle.getString("SettingsDialogMsg3") + s, settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean b = installedCompos.getAvailability(oldCompoName);
                installedCompos.remove(oldCompoName);
                try{
                    boolean visual = false;
                    try{
                        Class cls = Class.forName(className);
                        if (Component.class.isAssignableFrom(cls))
                            visual = true;
                    }catch (Throwable thr) {}
                    installedCompos.add(compoName, className, b, visual,null);
                    ((InstalledComponentTableModel) installedComponentTable.getModel()).setCompoStructure(installedCompos);
                }catch (DuplicateEntryException exc) {}
            }else{
                /* If the className has been altered, check if there exists another
                 * component name with the new class name.
                 */
                if (!className.equals(oldClassName) && installedCompos.containsComponentClassName(className)) {
                    String s = installedCompos.getName(className);
                    ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg2") + " " + className + settingsDialogBundle.getString("SettingsDialogMsg3") + s, settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                installedCompos.setClassName(compoName, className);
                ((InstalledComponentTableModel) installedComponentTable.getModel()).setCompoStructure(installedCompos);

            }

            // Scroll to the new position of the row
            int rowIndex = ((InstalledComponentTableModel) installedComponentTable.getModel()).getRow(compoName);
            if (rowIndex != -1 && currRowIndex != rowIndex) {
                installedComponentTable.scrollRectToVisible(installedComponentTable.getCellRect(rowIndex, 0, true));
                installedComponentTable.setRowSelectionInterval(rowIndex, rowIndex);
            }

            installedComponentTable.invalidate();
            installedComponentTable.paintImmediately(installedComponentTable.getVisibleRect());
        }
    }

//////////////////edit L&F Action

     void editLookAndFeelAction() {
        int selectedRow = installedLookAndFeelTable.getSelectedRow();
        if (selectedRow == -1)
            return;
        String oldLookAndFeelName = (String) ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getValueAt(selectedRow, 0);
        String oldClassName = (String) ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getValueAt(selectedRow, 1);

        InstallLookAndFeelDialog lafDialog = new InstallLookAndFeelDialog(ContainerSettingsDialog.this);
        lafDialog.setLookAndFeelName(oldLookAndFeelName);
        lafDialog.setClassName(oldClassName);
//        container.modalDialog = lafDialog;
        lafDialog.showDialog(ContainerSettingsDialog.this);
//        container.modalDialog = ContainerSettingsDialog.this;
        if (lafDialog.getReturnCode() == lafDialog.DIALOG_OK) {
            InstalledLookAndFeelStructure installedLAFs = ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getLookAndFeelStructure();
            String lafName = lafDialog.getLookAndFeelName();
            String className = lafDialog.getClassName();
            int currRowIndex = ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getRow(oldLookAndFeelName);

            if (!lafName.equals(oldLookAndFeelName)) {
                /* Check if the new component name already exists or the classname
                 * is already registered.
                 */
                if (installedLAFs.containsLookAndFeelName(lafName)) {
                    ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg1") + " " + lafName, settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!className.equals(oldClassName) && installedLAFs.containsLookAndFeelClassName(className)) {
                    String s = installedLAFs.getName(className);
                    ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg2") + " " + className + settingsDialogBundle.getString("SettingsDialogMsg3") + s, settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean b = installedLAFs.getAvailability(oldLookAndFeelName);
                installedLAFs.remove(oldLookAndFeelName);
                try{
                    installedLAFs.add(lafName, className, b);
                    ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).setLookAndFeelStructure(installedLAFs);
                }catch (DuplicateEntryException exc) {}
            }else{
                /* If the className has been altered, check if there exists another
                 * component name with the new class name.
                 */
                if (!className.equals(oldClassName) && installedLAFs.containsLookAndFeelClassName(className)) {
                    String s = installedLAFs.getName(className);
                    ESlateOptionPane.showMessageDialog(ContainerSettingsDialog.this, settingsDialogBundle.getString("SettingsDialogMsg2") + " " + className + settingsDialogBundle.getString("SettingsDialogMsg3") + s, settingsDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
                installedLAFs.setClassName(lafName, className);
                ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).setLookAndFeelStructure(installedLAFs);

            }

            // Scroll to the new position of the row
            int rowIndex = ((InstalledLookAndFeelTableModel) installedLookAndFeelTable.getModel()).getRow(lafName);
            if (rowIndex != -1 && currRowIndex != rowIndex) {
                installedLookAndFeelTable.scrollRectToVisible(installedLookAndFeelTable.getCellRect(rowIndex, 0, true));
                installedLookAndFeelTable.setRowSelectionInterval(rowIndex, rowIndex);
            }

            installedLookAndFeelTable.invalidate();
            installedLookAndFeelTable.paintImmediately(installedLookAndFeelTable.getVisibleRect());
        }
    }
}

//////////////////////HERE STARTS CODE FOR L&F SETTINGS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

class InstallLookAndFeelDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;

    Locale locale;
    ResourceBundle dialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    protected int returnCode = DIALOG_CANCELLED;

    JLabel namelb, classlb;
    JTextField nameField, classField;
    JButton okButton, cancelButton;
    JPanel mainPanel;

    public InstallLookAndFeelDialog(JDialog d) {
        super(d, true);
        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        locale = Locale.getDefault();
        dialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.InstallComponentDialogBundle", locale);
        if (dialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.InstallComponentDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(dialogBundle.getString("DialogTitle2"));

        namelb = new JLabel(dialogBundle.getString("Name2"));
        nameField = new JTextField();

        classlb = new JLabel(dialogBundle.getString("Class2"));
        classField = new JTextField();

//        if (localeIsGreek) {
//            namelb.setFont(greekUIFont);
//            nameField.setFont(greekUIFont);
//            classlb.setFont(greekUIFont);
//            classField.setFont(greekUIFont);
//        }
        FontMetrics fm1 = getToolkit().getFontMetrics(namelb.getFont());
        int lbLength = fm1.stringWidth(namelb.getText());
        if (lbLength < fm1.stringWidth(classlb.getText()))
            lbLength = fm1.stringWidth(classlb.getText());

        LayoutConstraint c1 = new LayoutConstraint();
        c1.anchorToContainerTop(0);
        c1.anchorToContainerLeft(0);
        c1.setFixedWidth(lbLength);
        c1.setFixedHeight(23);

        LayoutConstraint c2 = new LayoutConstraint();
        c2.anchorToContainerTop(0);
        c2.anchorToContainerRight(0);
        c2.anchorToRightOf(namelb, 3);
        c2.setFixedHeight(23);

        LayoutConstraint c3 = new LayoutConstraint();
        c3.anchorToContainerLeft(0);
        c3.setFixedHeight(23);
        c3.setFixedWidth(lbLength);
        c3.anchorToBottomOf(namelb, 5);

        LayoutConstraint c4 = new LayoutConstraint();
        c4.anchorToContainerRight(0);
        c4.anchorToRightOf(classlb, 3);
        c4.setFixedHeight(23);
        c4.anchorToBottomOf(nameField, 5);

        // The button panel (OK, CANCEL)
        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton = new JButton(dialogBundle.getString("OK"));
        cancelButton = new JButton(dialogBundle.getString("Cancel"));
//        if (localeIsGreek) {
//            okButton.setFont(greekUIFont);
//            cancelButton.setFont(greekUIFont);
//        }

        okButton.setForeground(color128);
        okButton.setMargin(zeroInsets);
        cancelButton.setForeground(color128);
        cancelButton.setMargin(zeroInsets);

        LayoutConstraint constr = new LayoutConstraint();
        constr.addAnchor(new Anchor(Anchor.Container, Anchor.HorizontalCenter, Anchor.Left, Anchor.Right, 5));
        constr.anchorToBottomOf(classField, 10);
        constr.setFixedHeight(25);
        constr.setFixedWidth(90);

		    LayoutConstraint constr2 = new LayoutConstraint();
		    constr2.sameWidthAs(okButton);
		    constr2.sameHeightAs(okButton);
		    constr2.anchorToBottomOf(classField, 10);
		    constr2.addAnchor(new Anchor(Anchor.Container, Anchor.HorizontalCenter, Anchor.Right, Anchor.Left, 5));

        // The main panel
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new SmartLayout()); //BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(namelb, c1);
        mainPanel.add(nameField, c2);
        mainPanel.add(classlb, c3);
        mainPanel.add(classField, c4);
        mainPanel.add(okButton, constr);
		    mainPanel.add(cancelButton, constr2);
//        mainPanel.setBorder(new EmptyBorder(5,2,5,2));
        mainPanel.setBorder(new MatteBorder(5,2,5,2, UIManager.getColor("control")));

        getContentPane().add(mainPanel);

        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                setOKButtonStatus();
            }
        });

        classField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                setOKButtonStatus();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    Class cl = Class.forName(classField.getText());

                    // Check if the given class is a subClass of java.awt.Component.
                    // This check is not needed any more. Non-visual java objects are supported.
/*                    Class superClass = cl.getSuperclass();
                    boolean isComponentSubClass = false;
                    while (superClass != null) {
                        if (superClass.getName().equals("java.awt.Component")) {
                            isComponentSubClass = true;
                            break;
                        }else
                            superClass = superClass.getSuperclass();
                    }
                    if (!isComponentSubClass) {
                        ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg3"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
*/
                    // Check if the given class can be instantiated, i.e. it's not an
                    // interface or an abstract class
                    if (cl.isInterface()) {
                        ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg4"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int modifierMask = cl.getModifiers();
                    if (Modifier.isAbstract(modifierMask)) {
                        ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg4"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!Modifier.isPublic(modifierMask)) {
                        ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg6"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check if the class contains a public zero-argument constuctor.
                    try {
                        Class[] params = new Class[0];
                        java.lang.reflect.Constructor con = cl.getConstructor(params);
                        modifierMask = con.getModifiers();
                        if (!Modifier.isPublic(modifierMask)) {
                            ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg5"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }catch (Exception exc1) {
                        ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg5"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    returnCode = DIALOG_OK;
                    dispose();
                }catch (Exception exc) {
                  ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg1") + " " + classField.getText() + dialogBundle.getString("DialogMsg2"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }catch (Throwable exc) {
                  ESlateOptionPane.showMessageDialog(InstallLookAndFeelDialog.this, dialogBundle.getString("DialogMsg7") + exc.getClass() + " --> " + exc.getMessage(), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_CANCELLED;
                dispose();
            }
        });

        // Initialize the dialog
        okButton.setEnabled(false);
        nameField.requestFocus();

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
    }

    public void showDialog(Component comp) {
//        pack();
        setSize(380, 125);
        setResizable(true);
        nameField.requestFocus();

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
        nameField.requestFocus();
        setVisible(true);
    }

    public void setLookAndFeelName(String name) {
        nameField.setText(name);
        setOKButtonStatus();
    }

    public String getLookAndFeelName() {
        return nameField.getText();
    }

    public void setClassName(String className) {
        classField.setText(className);
        setOKButtonStatus();
    }

    public String getClassName() {
        return classField.getText();
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setOKButtonStatus() {
        String name = nameField.getText();
        String className = classField.getText();
        if (name.trim().length() > 0 && className.trim().length() > 0)
            okButton.setEnabled(true);
        else
            okButton.setEnabled(false);
    }

}


//////////////////////////////////////////////////////////////////////////////////////////
class InstallComponentDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;

    Locale locale;
    ResourceBundle dialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    protected int returnCode = DIALOG_CANCELLED;

    JLabel namelb, classlb;
    JTextField nameField, classField;
    JButton okButton, cancelButton;
    JPanel mainPanel;

    public InstallComponentDialog(JDialog d) {
        super(d, true);
        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        locale = Locale.getDefault();
        dialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.InstallComponentDialogBundle", locale);
        if (dialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.InstallComponentDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(dialogBundle.getString("DialogTitle"));

        namelb = new JLabel(dialogBundle.getString("Name"));
        nameField = new JTextField();

        classlb = new JLabel(dialogBundle.getString("Class"));
        classField = new JTextField();

//        if (localeIsGreek) {
//            namelb.setFont(greekUIFont);
//            nameField.setFont(greekUIFont);
//            classlb.setFont(greekUIFont);
//            classField.setFont(greekUIFont);
//        }
        FontMetrics fm1 = getToolkit().getFontMetrics(namelb.getFont());
        int lbLength = fm1.stringWidth(namelb.getText());
        if (lbLength < fm1.stringWidth(classlb.getText()))
            lbLength = fm1.stringWidth(classlb.getText());

        LayoutConstraint c1 = new LayoutConstraint();
        c1.anchorToContainerTop(0);
        c1.anchorToContainerLeft(0);
        c1.setFixedWidth(lbLength);
        c1.setFixedHeight(23);

        LayoutConstraint c2 = new LayoutConstraint();
        c2.anchorToContainerTop(0);
        c2.anchorToContainerRight(0);
        c2.anchorToRightOf(namelb, 3);
        c2.setFixedHeight(23);

        LayoutConstraint c3 = new LayoutConstraint();
        c3.anchorToContainerLeft(0);
        c3.setFixedHeight(23);
        c3.setFixedWidth(lbLength);
        c3.anchorToBottomOf(namelb, 5);

        LayoutConstraint c4 = new LayoutConstraint();
        c4.anchorToContainerRight(0);
        c4.anchorToRightOf(classlb, 3);
        c4.setFixedHeight(23);
        c4.anchorToBottomOf(nameField, 5);

        // The button panel (OK, CANCEL)
        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton = new JButton(dialogBundle.getString("OK"));
        cancelButton = new JButton(dialogBundle.getString("Cancel"));
//        if (localeIsGreek) {
//            okButton.setFont(greekUIFont);
//            cancelButton.setFont(greekUIFont);
//        }

        okButton.setForeground(color128);
        okButton.setMargin(zeroInsets);
        cancelButton.setForeground(color128);
        cancelButton.setMargin(zeroInsets);

        LayoutConstraint constr = new LayoutConstraint();
        constr.addAnchor(new Anchor(Anchor.Container, Anchor.HorizontalCenter, Anchor.Left, Anchor.Right, 5));
        constr.anchorToBottomOf(classField, 10);
        constr.setFixedHeight(25);
        constr.setFixedWidth(90);

		    LayoutConstraint constr2 = new LayoutConstraint();
		    constr2.sameWidthAs(okButton);
		    constr2.sameHeightAs(okButton);
		    constr2.anchorToBottomOf(classField, 10);
		    constr2.addAnchor(new Anchor(Anchor.Container, Anchor.HorizontalCenter, Anchor.Right, Anchor.Left, 5));

        // The main panel
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new SmartLayout()); //BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(namelb, c1);
        mainPanel.add(nameField, c2);
        mainPanel.add(classlb, c3);
        mainPanel.add(classField, c4);
        mainPanel.add(okButton, constr);
		    mainPanel.add(cancelButton, constr2);
        mainPanel.setBorder(new MatteBorder(5,2,5,2, UIManager.getColor("control")));
//        mainPanel.setBorder(new EmptyBorder(5,2,5,2));

        getContentPane().add(mainPanel);

        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                setOKButtonStatus();
            }
        });

        classField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                setOKButtonStatus();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    Class cl = Class.forName(classField.getText());

                    // Check if the given class is a subClass of java.awt.Component.
                    // This check is not needed any more. Non-visual java objects are supported.
/*                    Class superClass = cl.getSuperclass();
                    boolean isComponentSubClass = false;
                    while (superClass != null) {
                        if (superClass.getName().equals("java.awt.Component")) {
                            isComponentSubClass = true;
                            break;
                        }else
                            superClass = superClass.getSuperclass();
                    }
                    if (!isComponentSubClass) {
                        ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg3"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
*/
                    // Check if the given class can be instantiated, i.e. it's not an
                    // interface or an abstract class
                    if (cl.isInterface()) {
                        ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg4"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int modifierMask = cl.getModifiers();
                    if (Modifier.isAbstract(modifierMask)) {
                        ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg4"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!Modifier.isPublic(modifierMask)) {
                        ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg6"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check if the class contains a public zero-argument constuctor.
                    try {
                        Class[] params = new Class[0];
                        java.lang.reflect.Constructor con = cl.getConstructor(params);
                        modifierMask = con.getModifiers();
                        if (!Modifier.isPublic(modifierMask)) {
                            ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg5"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }catch (Exception exc1) {
                        ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg1") + cl.getName() + dialogBundle.getString("DialogMsg5"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    returnCode = DIALOG_OK;
                    dispose();
                }catch (Exception exc) {
                  ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg1") + " " + classField.getText() + dialogBundle.getString("DialogMsg2"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }catch (Throwable exc) {
                  ESlateOptionPane.showMessageDialog(InstallComponentDialog.this, dialogBundle.getString("DialogMsg7") + exc.getClass() + " --> " + exc.getMessage(), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_CANCELLED;
                dispose();
            }
        });

        // Initialize the dialog
        okButton.setEnabled(false);
        nameField.requestFocus();

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
    }

    public void showDialog(Component comp) {
//        pack();
        setSize(380, 125);
        setResizable(true);
        nameField.requestFocus();

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
        nameField.requestFocus();
        setVisible(true);
    }

    public void setComponentName(String name) {
        nameField.setText(name);
        setOKButtonStatus();
    }

    public String getComponentName() {
        return nameField.getText();
    }

    public void setClassName(String className) {
        classField.setText(className);
        setOKButtonStatus();
    }

    public String getClassName() {
        return classField.getText();
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setOKButtonStatus() {
        String name = nameField.getText();
        String className = classField.getText();
        if (name.trim().length() > 0 && className.trim().length() > 0)
            okButton.setEnabled(true);
        else
            okButton.setEnabled(false);
    }

}

class InstallWebSiteDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;

    Locale locale;
    ResourceBundle dialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    protected int returnCode = DIALOG_CANCELLED;
////nikosM
    JLabel namelb, addresslb/*, commonlb*/;
////nikosM end
    JTextField nameField, addressField, commonField;
    JButton okButton;

    public InstallWebSiteDialog(JDialog d) {
        super(d, true);
        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        locale = Locale.getDefault();
        dialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.InstallWebSiteDialogBundle", locale);
        if (dialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.InstallWebSiteDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(dialogBundle.getString("DialogTitle"));

        namelb = new JLabel(dialogBundle.getString("Name"));
        nameField = new JTextField();
        addresslb = new JLabel(dialogBundle.getString("Address"));
        addressField = new JTextField();
////nikosM
//        commonlb = new JLabel(dialogBundle.getString("CommonFolder"));
//        commonField = new JTextField();
/*
        if (localeIsGreek) {
            namelb.setFont(greekUIFont);
            nameField.setFont(greekUIFont);
            addresslb.setFont(greekUIFont);
            addressField.setFont(greekUIFont);
//            commonlb.setFont(greekUIFont);
//            commonField.setFont(greekUIFont);
        }
*/
        FontMetrics fm1 = getToolkit().getFontMetrics(namelb.getFont());
        int lbLength = fm1.stringWidth(namelb.getText());
        if (lbLength < fm1.stringWidth(addresslb.getText()))
            lbLength = fm1.stringWidth(addresslb.getText());
//        if (lbLength < fm1.stringWidth(commonlb.getText()))
//            lbLength = fm1.stringWidth(commonlb.getText());
////nikosM end

/*        Dimension dim = new Dimension(lbLength, 23);
        namelb.setMaximumSize(dim);
        namelb.setPreferredSize(dim);
        namelb.setMinimumSize(dim);
        addresslb.setMaximumSize(dim);
        addresslb.setPreferredSize(dim);
        addresslb.setMinimumSize(dim);
        commonlb.setMaximumSize(dim);
        commonlb.setPreferredSize(dim);
        commonlb.setMinimumSize(dim);

        dim = new Dimension(250, 23);
        nameField.setMaximumSize(dim);
        nameField.setPreferredSize(dim);
        nameField.setMinimumSize(dim);
        addressField.setMaximumSize(dim);
        addressField.setPreferredSize(dim);
        addressField.setMinimumSize(dim);
        commonField.setMaximumSize(dim);
        commonField.setPreferredSize(dim);
        commonField.setMinimumSize(dim);

        JPanel namePanel = new JPanel(true);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.add(namelb);
        namePanel.add(Box.createRigidArea(new Dimension(10, 23)));
        namePanel.add(nameField);

        JPanel addressPanel = new JPanel(true);
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.X_AXIS));
        addressPanel.add(addresslb);
        addressPanel.add(Box.createRigidArea(new Dimension(10, 23)));
        addressPanel.add(addressField);

        JPanel commonPanel = new JPanel(true);
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.X_AXIS));
        commonPanel.add(commonlb);
        commonPanel.add(Box.createRigidArea(new Dimension(10, 23)));
        commonPanel.add(commonField);

        JPanel componentPanel = new JPanel(true);
        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));
        componentPanel.add(namePanel);
        componentPanel.add(Box.createVerticalStrut(4));
        componentPanel.add(addressPanel);
        componentPanel.add(Box.createVerticalStrut(4));
        componentPanel.add(commonPanel);
        componentPanel.setBorder(new EmptyBorder(5, 4, 0, 4));
*/
        LayoutConstraint c1 = new LayoutConstraint();
        c1.anchorToContainerTop(0);
        c1.anchorToContainerLeft(0);
        c1.setFixedWidth(lbLength);
        c1.setFixedHeight(23);

        LayoutConstraint c2 = new LayoutConstraint();
        c2.anchorToContainerTop(0);
        c2.anchorToContainerRight(0);
        c2.anchorToRightOf(namelb, 3);
        c2.setFixedHeight(23);

        LayoutConstraint c3 = new LayoutConstraint();
        c3.anchorToContainerLeft(0);
        c3.setFixedHeight(23);
        c3.setFixedWidth(lbLength);
        c3.anchorToBottomOf(namelb, 5);

        LayoutConstraint c4 = new LayoutConstraint();
        c4.anchorToContainerRight(0);
        c4.anchorToRightOf(addresslb, 3);
        c4.setFixedHeight(23);
        c4.anchorToBottomOf(nameField, 5);

        LayoutConstraint c5 = new LayoutConstraint();
        c5.anchorToContainerLeft(0);
        c5.setFixedHeight(23);
        c5.setFixedWidth(lbLength);
        c5.anchorToBottomOf(addresslb, 5);

        LayoutConstraint c6 = new LayoutConstraint();
        c6.anchorToContainerRight(0);
//nikosM
//        c6.anchorToRightOf(commonlb, 3);
//nikosM end
        c6.setFixedHeight(23);
        c6.anchorToBottomOf(addressField, 5);

        // The button panel (OK, CANCEL)
        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton = new JButton(dialogBundle.getString("OK"));
//        if (localeIsGreek)
//            okButton.setFont(greekUIFont);
        okButton.setForeground(color128);
        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(dialogBundle.getString("Cancel"));
//        if (localeIsGreek)
//            cancelButton.setFont(greekUIFont);
        cancelButton.setForeground(color128);
        cancelButton.setMargin(zeroInsets);


        LayoutConstraint constr = new LayoutConstraint();
        constr.addAnchor(new Anchor(Anchor.Container, Anchor.HorizontalCenter, Anchor.Left, Anchor.Right, 5));
////nikosM
//        constr.anchorToBottomOf(commonField, 10);
        constr.anchorToBottomOf(addressField, 10);
        constr.setFixedHeight(25);
        constr.setFixedWidth(90);

		    LayoutConstraint constr2 = new LayoutConstraint();
		    constr2.sameWidthAs(okButton);
		    constr2.sameHeightAs(okButton);
//		    constr2.anchorToBottomOf(commonField, 10);
		    constr2.anchorToBottomOf(addressField, 10);
////nikosM end

		    constr2.addAnchor(new Anchor(Anchor.Container, Anchor.HorizontalCenter, Anchor.Right, Anchor.Left, 5));

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new SmartLayout()); //BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(namelb, c1);
        mainPanel.add(nameField, c2);
        mainPanel.add(addresslb, c3);
        mainPanel.add(addressField, c4);
////nikosM
//        mainPanel.add(commonlb, c5);
//        mainPanel.add(commonField, c6);
////nikosM end
        mainPanel.add(okButton, constr);
		    mainPanel.add(cancelButton, constr2);
        mainPanel.setBorder(new MatteBorder(5,2,5,2, UIManager.getColor("control")));
//        mainPanel.setBorder(new EmptyBorder(5,2,5,2));

        getContentPane().add(mainPanel);

        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                setOKButtonStatus();
            }
        });

        addressField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                setOKButtonStatus();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String address = null;
                try{
                    address = addressField.getText();
                    URL server = new URL(address);
                    URLConnection con = server.openConnection();
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setDefaultUseCaches(false);
                    con.setUseCaches(false);
                    con.setIfModifiedSince(0);

/// nikosM code change
                    String cmd = "dir|" + System.getProperty("file.separator");
//                    if (commonField.getText() != null && commonField.getText().trim().length() != 0)
//                        cmd = cmd + commonField.getText().trim();
//// nikosM end of code change
//                    ObjectOutputStream req = new ObjectOutputStream(new BufferedOutputStream(con.getOutputStream()));
//                    req.writeObject(cmd);
//                    req.flush();
//                    req.close();
                    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
                    byte[] actionBytes = cmd.getBytes();
                    dos.writeInt(actionBytes.length);
                    dos.write(actionBytes);
                    dos.close();

                    ObjectInputStream res = new ObjectInputStream (new BufferedInputStream
                                                     (con.getInputStream ()));
                    Object result = res.readObject ();
                    res.close();
//// nikosM code change
                    if (!cmd.equals("dir|"+System.getProperty("file.separator")) && result == null) { //the common folder does not exist
//                        ESlateOptionPane.showMessageDialog(InstallWebSiteDialog.this, dialogBundle.getString("DialogMsg6") + commonField.getText().trim() + "\"" + dialogBundle.getString("DialogMsg7") + address + "\"", dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
///// nikosM end of code change
                        return;
                    }

                }catch (java.net.MalformedURLException exc) {
                    ESlateOptionPane.showMessageDialog(InstallWebSiteDialog.this, dialogBundle.getString("DialogMsg1") + address +"\"", dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                    System.out.println("Malformed URL " + webSite);
                    return;
                }catch (java.net.NoRouteToHostException exc) {
                    ESlateOptionPane.showMessageDialog(InstallWebSiteDialog.this, dialogBundle.getString("DialogMsg2") + address +"\"" + dialogBundle.getString("DialogMsg3"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                    System.out.println("Host " + webSite + " unavailable");
                    return;
                }catch (java.net.UnknownHostException exc) {
                    ESlateOptionPane.showMessageDialog(InstallWebSiteDialog.this, dialogBundle.getString("DialogMsg2") + address +"\"" + dialogBundle.getString("DialogMsg4"), dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                    System.out.println("Host " + webSite + " unavailable");
                    return;
                }catch (java.io.IOException exc) {
                    ESlateOptionPane.showMessageDialog(InstallWebSiteDialog.this, dialogBundle.getString("DialogMsg5") + address +"\"", dialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
//                    System.out.println(exc.getClass() + " Unable to connect to " + webSite);
                    return;
                }catch (ClassNotFoundException exc) {
                    System.out.println("ClassNotFoundException: should not occur");
                    return;
                }
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

        // Initialize the dialog
        okButton.setEnabled(false);

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
    }

    public void showDialog(Component comp) {
//        pack();
        setSize(380, 150);
        setResizable(true);
        nameField.requestFocus();

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
        nameField.requestFocus();
        getRootPane().setDefaultButton(okButton);
        show();
    }

    public void setWebSiteName(String name) {
        nameField.setText(name);
        setOKButtonStatus();
    }

    public String getWebSiteName() {
        return nameField.getText();
    }

    public void setAddress(String address) {
        addressField.setText(address);
        setOKButtonStatus();
    }

    public String getAddress() {
        return addressField.getText().trim();
    }

//// nikosM
//    public void setCommonDir(String commonDir) {
//        commonField.setText(commonDir);
//    }

//    public String getCommonDir() {
//        return commonField.getText().trim();
//    }
////nikosM end of change

    public int getReturnCode() {
        return returnCode;
    }

    public void setOKButtonStatus() {
        String name = nameField.getText();
        String address = addressField.getText();
        if (name.trim().length() > 0 && address.trim().length() > 0)
            okButton.setEnabled(true);
        else
            okButton.setEnabled(false);
    }
}

class MicroworldFileFilter extends FileFilter {
    public boolean accept(java.io.File f) {
        if (f.isDirectory() || f.toString().endsWith(".mwd"))
            return true;
        return false;
    }

    public String getDescription() {
        return "E-Slate microworld files";
    }
}


class FocusTraverableJTable extends JTable {
    boolean focusTraversable = false;

    public FocusTraverableJTable(TableModel model) {
        super(model);
        if (model.getRowCount() > 0)
            focusTraversable = true;
        else
            focusTraversable = false;
    }

    public boolean isFocusTraversable() {
  //      System.out.println("isFocusTraversable returns: " + focusTraversable);
        return focusTraversable;
    }
    public void setFocusTraversable(boolean traversable) {
        focusTraversable = traversable;
    }
}


class InstalledComponentTable extends FocusTraverableJTable {

    boolean disableAction = false;


    public InstalledComponentTable(InstalledComponentTableModel model) {

        super(model);
        if (model.getRowCount() > 0)
            focusTraversable = true;
        else
            focusTraversable = false;

        getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                int colIndex = columnAtPoint(e.getPoint());
                ButtonCellRenderer renderer = (ButtonCellRenderer) getTableHeader().getColumnModel().getColumn(colIndex).getHeaderRenderer();
                javax.swing.ButtonModel bm = renderer.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
                getTableHeader().paintImmediately(getTableHeader().getVisibleRect());
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                for (int i=0; i<getColumnCount(); i++) {
                    ButtonCellRenderer renderer = (ButtonCellRenderer) getTableHeader().getColumnModel().getColumn(i).getHeaderRenderer();
                    if (!disableAction)
                        renderer.setIcon(null);
                    javax.swing.ButtonModel bm = renderer.getModel();
                    bm.setPressed(false);
                    getTableHeader().paintImmediately(getTableHeader().getVisibleRect());
                }

                if (disableAction) {
                    disableAction = false;
                    return;
                }

                // If there is a selected row, get the name of the component in this row, so that
                // the same component gets selected after the sorting takes place.
                String selCompoName = null;
                int selRow = getSelectedRow();
                if (selRow != -1) {
                    selCompoName = (String) getModel().getValueAt(selRow, 0);
                }

                int colIndex = columnAtPoint(e.getPoint());
                String colName = getColumnName(colIndex);
                boolean up = ((InstalledComponentTableModel) getModel()).sortOn(colName);
                ButtonCellRenderer renderer = (ButtonCellRenderer) getTableHeader().getColumnModel().getColumn(colIndex).getHeaderRenderer();
                renderer.setSortDirectionIcon(up);

                // select the component which was selected before sorting the table
                if (selCompoName != null) {
                    int newRowIndex = -1; //((InstalledComponentTableModel) getModel()).compoStructure.getIndex(selCompoName);
                    /*\InstalledComponentStructure.*/CompoEntry entr[] = ((InstalledComponentTableModel) getModel()).entries;
                    for (int i=0; i<entr.length; i++) {
                        if (entr[i].name.equals(selCompoName)) {
                            newRowIndex = i;
                            break;
                        }
                    }
                    if (newRowIndex != -1) {
                        scrollRectToVisible(getCellRect(newRowIndex, 0, true));
                        setRowSelectionInterval(newRowIndex, newRowIndex);
                    }
                }
//                revalidate();
            }

        });
        getTableHeader().addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                disableAction = true;
            }
        });

        for (int i=0; i<model.getColumnCount(); i++) {
            ButtonCellRenderer bcr = new ButtonCellRenderer();
            getColumnModel().getColumn(i).setHeaderRenderer(bcr);
            if (i == 0)
                bcr.setIcon(bcr.UP_ARROW);
            else
                bcr.setIcon(null);
        }
    }

    /* Validates the status of the table's header cell, based on the selected field
     * and the sort direction of the new InstalledComponentStructure.
     */
    protected void validateHeader(InstalledComponentStructure compoStructure) {
        if (compoStructure == null) return;
        int selectedField = compoStructure.getCurrentSortField()-1;
        boolean sortDirectionUp = compoStructure.isSortDirectionUp();
//        System.out.println("selectedField: " + selectedField + ", sortDirectionUp: " + sortDirectionUp);
        for (int i=0; i<getColumnCount(); i++) {
            ButtonCellRenderer bcr = (ButtonCellRenderer) getColumnModel().getColumn(i).getHeaderRenderer();
            if (i == selectedField)
                bcr.setIcon((sortDirectionUp)? bcr.UP_ARROW : bcr.DOWN_ARROW);
            else
                bcr.setIcon(null);
        }
    }
}

class InstalledLookAndFeelTable extends FocusTraverableJTable {

    boolean disableAction = false;


    public InstalledLookAndFeelTable(InstalledLookAndFeelTableModel model) {

        super(model);
        if (model.getRowCount() > 0)
            focusTraversable = true;
        else
            focusTraversable = false;

        getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                int colIndex = columnAtPoint(e.getPoint());
                ButtonCellRenderer renderer = (ButtonCellRenderer) getTableHeader().getColumnModel().getColumn(colIndex).getHeaderRenderer();
                javax.swing.ButtonModel bm = renderer.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
                getTableHeader().paintImmediately(getTableHeader().getVisibleRect());
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                for (int i=0; i<getColumnCount(); i++) {
                    ButtonCellRenderer renderer = (ButtonCellRenderer) getTableHeader().getColumnModel().getColumn(i).getHeaderRenderer();
                    if (!disableAction)
                        renderer.setIcon(null);
                    javax.swing.ButtonModel bm = renderer.getModel();
                    bm.setPressed(false);
                    getTableHeader().paintImmediately(getTableHeader().getVisibleRect());
                }

                if (disableAction) {
                    disableAction = false;
                    return;
                }

                // If there is a selected row, get the name of the component in this row, so that
                // the same component gets selected after the sorting takes place.
                String selCompoName = null;
                int selRow = getSelectedRow();
                if (selRow != -1) {
                    selCompoName = (String) getModel().getValueAt(selRow, 0);
                }

                int colIndex = columnAtPoint(e.getPoint());
                String colName = getColumnName(colIndex);
                boolean up = ((InstalledLookAndFeelTableModel) getModel()).sortOn(colName);
                ButtonCellRenderer renderer = (ButtonCellRenderer) getTableHeader().getColumnModel().getColumn(colIndex).getHeaderRenderer();
                renderer.setSortDirectionIcon(up);

                // select the component which was selected before sorting the table
                if (selCompoName != null) {
                    int newRowIndex = -1; //((InstalledComponentTableModel) getModel()).compoStructure.getIndex(selCompoName);
                    InstalledLookAndFeelStructure.LAFEntry entr[] = ((InstalledLookAndFeelTableModel) getModel()).entries;
                    for (int i=0; i<entr.length; i++) {
                        if (entr[i].name.equals(selCompoName)) {
                            newRowIndex = i;
                            break;
                        }
                    }
                    if (newRowIndex != -1) {
                        scrollRectToVisible(getCellRect(newRowIndex, 0, true));
                        setRowSelectionInterval(newRowIndex, newRowIndex);
                    }
                }
//                revalidate();
            }

        });
        getTableHeader().addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                disableAction = true;
            }
        });

        for (int i=0; i<model.getColumnCount(); i++) {
            ButtonCellRenderer bcr = new ButtonCellRenderer();
            getColumnModel().getColumn(i).setHeaderRenderer(bcr);
            if (i == 0)
                bcr.setIcon(bcr.UP_ARROW);
            else
                bcr.setIcon(null);
        }
    }

    /* Validates the status of the table's header cell, based on the selected field
     * and the sort direction of the new InstalledComponentStructure.
     */
    protected void validateHeader(InstalledLookAndFeelStructure lafStructure) {
        if (lafStructure == null) return;
        int selectedField = lafStructure.getCurrentSortField()-1;
        boolean sortDirectionUp = lafStructure.isSortDirectionUp();
//        System.out.println("selectedField: " + selectedField + ", sortDirectionUp: " + sortDirectionUp);
        for (int i=0; i<getColumnCount(); i++) {
            ButtonCellRenderer bcr = (ButtonCellRenderer) getColumnModel().getColumn(i).getHeaderRenderer();
            if (i == selectedField)
                bcr.setIcon((sortDirectionUp)? bcr.UP_ARROW : bcr.DOWN_ARROW);
            else
                bcr.setIcon(null);
        }
    }
}


class ButtonCellRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    static ImageIcon UP_ARROW = new ImageIcon(ButtonCellRenderer.class.getResource("images/up3DArrow.gif"));
    static ImageIcon DOWN_ARROW = new ImageIcon(ButtonCellRenderer.class.getResource("images/down3DArrow.gif"));

    public ButtonCellRenderer() {
        super();
        setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    }

    public void setSortDirectionIcon(boolean up) {
        if (up)
            setIcon(UP_ARROW);
        else
            setIcon(DOWN_ARROW);
    }

    public Dimension getPreferredSize() {
        return new Dimension(20, 22);
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        setText(value.toString());
        return this;
    }
}

class MyJTextField extends JTextField {
    public boolean isFocusTraversable() {
        return false;
    }
}

class InstalledComponentTableModel extends AbstractTableModel {
    /*\InstalledComponentStructure.*/CompoEntry[] entries = null;
    InstalledComponentStructure compoStructure = null;
    ResourceBundle bundle = null;
    String componentNameColName;

    public InstalledComponentTableModel(InstalledComponentStructure ics, ResourceBundle bundle) {
        super();
        this.compoStructure = ics;
        if (ics != null)
            entries = ics.getSortedEntries();
        else
            entries = new /*\InstalledComponentStructure.*/CompoEntry[0];
        this.bundle = bundle;
        componentNameColName = bundle.getString("NameColumn");
    }

    public void setCompoStructure(InstalledComponentStructure ics) {
        compoStructure = ics;
        entries = ics.getSortedEntries();
    }

    public InstalledComponentStructure getCompoStructure() {
        return compoStructure;
    }

    public boolean sortOn(String colName) {
        if (colName == null) return false;
//        System.out.println("Sorting on: " + colName);
        int sortField = InstalledComponentStructure.NAME;
        if (colName.equals(bundle.getString("ClassNameColumn")))
            sortField = InstalledComponentStructure.CLASSNAME;
        else if (colName.equals(bundle.getString("AvailabilityColumn")))
            sortField = InstalledComponentStructure.AVAILABILITY;

        boolean up = true;
        int currSortField = compoStructure.getCurrentSortField();
//        System.out.println("sortField: " + sortField + ", currSortField: " + currSortField);
        if (sortField == currSortField) {
            up = !compoStructure.isSortDirectionUp();
            compoStructure.setSortDirection(up);
        }else{
            compoStructure.setSortField(sortField, up);
        }
        entries = compoStructure.getSortedEntries();
        return up;
    }

    public int getRow(String componentName) {
        for (int i=0; i<entries.length; i++) {
            if (entries[i].name.equals(componentName))
                return i;
        }
        return -1;
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        if (entries != null)
            return entries.length;
        else
            return 0;
    }

    /* This getValueAt() does not respect column re-ordering. The columns' positions in the
     * table data model are fixed.
     */
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return entries[row].name;
        }else if (col == 1) {
            return entries[row].className;
        }else{
            return new Boolean(entries[row].availability);
        }
    }
    public String getColumnName(int column) {
        if (column == 0)
            return bundle.getString("NameColumn");
        else if (column == 1)
            return bundle.getString("ClassNameColumn");
        else
            return bundle.getString("AvailabilityColumn");
    }
    public Class getColumnClass(int c) {
        if (c == 2)
            return java.lang.Boolean.class;
        else
            return java.lang.String.class;
    }
    public boolean isCellEditable(int row, int col) {
        if (col == 2)
            return true;
        return false;
    }
    public void setValueAt(Object aValue, int row, int column) {
        String compoName = entries[row].name;
        if (column == 2) {
            boolean val = ((Boolean) aValue).booleanValue();
            compoStructure.setAvailability(compoName, val);
            entries[row].availability = val;
        }else if (column == 1) {
            String newClassName = (String) aValue;
            compoStructure.setClassName(compoName, newClassName);
            entries[row].className = newClassName;
        }else if (column == 0) {
            String newName = (String) aValue;
            compoStructure.setName(compoName, newName);
            entries[row].name = newName;
        }
    }
}

class InstalledLookAndFeelTableModel extends AbstractTableModel {
    InstalledLookAndFeelStructure.LAFEntry[] entries = null;
    InstalledLookAndFeelStructure lafStructure = null;
    ResourceBundle bundle = null;
    String lafNameColName;

    public InstalledLookAndFeelTableModel(InstalledLookAndFeelStructure ilafs, ResourceBundle bundle) {
        super();
        this.lafStructure = ilafs;
        if (ilafs != null)
            entries = ilafs.getSortedEntries();
        else{
            entries = new InstalledLookAndFeelStructure.LAFEntry[0];
        }
        this.bundle = bundle;
        lafNameColName = bundle.getString("NameColumn");
    }

    public void setLookAndFeelStructure(InstalledLookAndFeelStructure ilafs) {
        lafStructure = ilafs;
        entries = ilafs.getSortedEntries();
    }

    public InstalledLookAndFeelStructure getLookAndFeelStructure() {
        return lafStructure;
    }

    public boolean sortOn(String colName) {
        if (colName == null) return false;
//        System.out.println("Sorting on: " + colName);
        int sortField = InstalledLookAndFeelStructure.NAME;
        if (colName.equals(bundle.getString("ClassNameColumn")))
            sortField = InstalledLookAndFeelStructure.CLASSNAME;
        else if (colName.equals(bundle.getString("AvailabilityColumn")))
            sortField = InstalledLookAndFeelStructure.AVAILABILITY;

        boolean up = true;
        int currSortField = lafStructure.getCurrentSortField();
//        System.out.println("sortField: " + sortField + ", currSortField: " + currSortField);
        if (sortField == currSortField) {
            up = !lafStructure.isSortDirectionUp();
            lafStructure.setSortDirection(up);
        }else{
            lafStructure.setSortField(sortField, up);
        }
        entries = lafStructure.getSortedEntries();
        return up;
    }

    public int getRow(String lafName) {
        for (int i=0; i<entries.length; i++) {
            if (entries[i].name.equals(lafName))
                return i;
        }
        return -1;
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        if (entries != null)
            return entries.length;
        else
            return 0;
    }

    /* This getValueAt() does not respect column re-ordering. The columns' positions in the
     * table data model are fixed.
     */
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return entries[row].name;
        }else if (col == 1) {
            return entries[row].className;
        }else{
            return new Boolean(entries[row].availability);
        }
    }
    public String getColumnName(int column) {
        if (column == 0)
            return bundle.getString("NameColumn");
        else if (column == 1)
            return bundle.getString("ClassNameColumn");
        else
            return bundle.getString("AvailabilityColumn");
    }
    public Class getColumnClass(int c) {
        if (c == 2)
            return java.lang.Boolean.class;
        else
            return java.lang.String.class;
    }
    public boolean isCellEditable(int row, int col) {
        if (col == 2)
            return true;
        return false;
    }
    public void setValueAt(Object aValue, int row, int column) {
        String lafName = entries[row].name;
        if (column == 2) {
            boolean val = ((Boolean) aValue).booleanValue();
            lafStructure.setAvailability(lafName, val);
            entries[row].availability = val;
        }else if (column == 1) {
            String newClassName = (String) aValue;
            lafStructure.setClassName(lafName, newClassName);
            entries[row].className = newClassName;
        }else if (column == 0) {
            String newName = (String) aValue;
            lafStructure.setName(lafName, newName);
            entries[row].name = newName;
        }
    }
}

class SoundListCellRenderer extends JPanel implements javax.swing.ListCellRenderer { //DefaultListCellRenderer {
    SoundSettingsPanel ssp = null;
    JLabel textLabel, iconLabel;
    Border noFocusBorder;
    boolean buttonPressed = false;
//    MatteBorder border = null;

    public SoundListCellRenderer(SoundSettingsPanel dialog) {
        ssp = dialog;
        noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        textLabel = new JLabel();
        iconLabel = new JLabel() {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = 20;
                return dim;
            }
        };
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setVerticalAlignment(JLabel.CENTER);
        iconLabel.setLayout(new BorderLayout());
        iconLabel.setOpaque(true);
        textLabel.setOpaque(true);
        setLayout(new BorderLayout(3,0));
        add(iconLabel, BorderLayout.WEST);
        add(textLabel, BorderLayout.CENTER);
        setOpaque(false);
//        border = new MatteBorder(0, 20, 0, 0, ssp.getBackground());
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

//        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        SystemSound sound = (SystemSound) value;
        textLabel.setText(sound.soundName);
        if (sound.soundFileName != null) {
//            setBorder(null);
            iconLabel.setIcon(ssp.soundIcon);
        }else{
            iconLabel.setIcon(null);
//            setBorder(border);
        }

        textLabel.setBackground((isSelected)? list.getSelectionBackground() : list.getBackground());
        textLabel.setForeground((isSelected)? list.getSelectionForeground() : list.getForeground());
	textLabel.setBorder((cellHasFocus) ? javax.swing.UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
        return this;
    }
}


class SoundSettingsPanel extends JPanel {
    ImageIcon soundIcon = new ImageIcon(getClass().getResource("images/sound.gif"));

    ContainerSettingsDialog dialog = null;
    ESlateContainer container = null;
    JList eslateSoundList, mwdSoundList;
    SoundMgmtPanel soundControlPanel;
    SoundThemeControlPanel soundThemeControlPanel;
    private boolean dontProcessEvent = false;
    SystemSound currentSound = null;
    SoundTheme currentTheme = null;
    String currentThemeFile = null;
    boolean skipSavePrompt = false;

    public SoundSettingsPanel(ContainerSettingsDialog dialog, ESlateContainer container) {
        this.dialog = dialog;
        this.container = container;

        eslateSoundList = new JList() {
            protected void processMouseEvent(MouseEvent e) {
                if (e.getX() > 20)
                    super.processMouseEvent(e);
                else{
                    if (e.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
                        int index = eslateSoundList.locationToIndex(e.getPoint());
                        if (index == -1) return;
                        eslateSoundList.setSelectedIndex(index);
                        mwdSoundList.clearSelection();
                        currentSound = (SystemSound) eslateSoundList.getSelectedValue();
                        soundControlPanel.setSound(currentSound);
                        soundControlPanel.playSound();
                    }
                }
            }
        };
        eslateSoundList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eslateSoundList.setCellRenderer(new SoundListCellRenderer(this));
        eslateSoundList.setBorder(new EtchedBorder());

        mwdSoundList = new JList() {
            protected void processMouseEvent(MouseEvent e) {
                if (e.getX() > 20)
                    super.processMouseEvent(e);
                else{
                    if (e.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
                        int index = mwdSoundList.locationToIndex(e.getPoint());
                        if (index == -1) return;
                        mwdSoundList.setSelectedIndex(index);
                        eslateSoundList.clearSelection();
                        currentSound = (SystemSound) mwdSoundList.getSelectedValue();
                        soundControlPanel.setSound(currentSound);
                        soundControlPanel.playSound();
                    }
                }
            }
        };
        mwdSoundList.setBorder(new EtchedBorder());
        mwdSoundList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        mwdSoundList.setCellRenderer(new SoundListCellRenderer(this));

        JPanel eslateSoundPanel = new JPanel();
        eslateSoundPanel.setLayout(new BorderLayout());
        eslateSoundPanel.add(eslateSoundList, BorderLayout.CENTER);
        TitledBorder tb6 = new TitledBorder(dialog.settingsDialogBundle.getString("SystemSounds"));
        tb6.setTitleColor(ESlateContainerUtils.titleBorderColor);
        eslateSoundPanel.setBorder(new CompoundBorder(tb6, new EmptyBorder(0,0,2,0)));

        JPanel mwdSoundPanel = new JPanel();
        mwdSoundPanel.setLayout(new BorderLayout());
        mwdSoundPanel.add(mwdSoundList, BorderLayout.CENTER);
        TitledBorder tb5 = new TitledBorder(dialog.settingsDialogBundle.getString("DefaultSounds"));
        tb5.setTitleColor(ESlateContainerUtils.titleBorderColor);
        mwdSoundPanel.setBorder(new CompoundBorder(tb5, new EmptyBorder(0,0,2,0)));

        JPanel soundListPanel = new JPanel();
        soundListPanel.setLayout(new BoxLayout(soundListPanel, BoxLayout.Y_AXIS));
        soundListPanel.add(eslateSoundPanel); //, BorderLayout.NORTH);
        soundListPanel.add(Box.createVerticalStrut(5));
        soundListPanel.add(mwdSoundPanel); //, BorderLayout.CENTER);

        soundControlPanel = new SoundMgmtPanel(this);
        soundThemeControlPanel = new SoundThemeControlPanel(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createGlue());
        add(soundListPanel);
        add(Box.createVerticalStrut(5));
        add(soundControlPanel);
        add(Box.createVerticalStrut(5));
        add(soundThemeControlPanel);
        add(Box.createGlue());

        soundControlPanel.setSound(null);

        eslateSoundList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
//System.out.println("eslateSoundList dontProcessEvent: " + dontProcessEvent + ", e.getValueIsAdjusting(): " + e.getValueIsAdjusting());
                if (dontProcessEvent || e.getValueIsAdjusting()) return;
                JList list = (JList) e.getSource();
                if (list.getSelectedIndex() != -1) {
                    dontProcessEvent = true;
                    mwdSoundList.clearSelection();
                    dontProcessEvent = false;
                }
                currentSound = (SystemSound) list.getSelectedValue();
                soundControlPanel.setSound(currentSound);
            }
        });

        mwdSoundList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
//System.out.println("mwdSoundList dontProcessEvent: " + dontProcessEvent + ", e.getValueIsAdjusting(): " + e.getValueIsAdjusting());
                if (dontProcessEvent || e.getValueIsAdjusting()) return;
                JList list = (JList) e.getSource();
                if (list.getSelectedIndex() != -1) {
                    dontProcessEvent = true;
                    eslateSoundList.clearSelection();
                    dontProcessEvent = false;
                }
                currentSound = (SystemSound) list.getSelectedValue();
//System.out.println("list.getSelectedIndex(): " + list.getSelectedIndex() + ", currentSound: " + currentSound);
                soundControlPanel.setSound(currentSound);
            }
        });

        eslateSoundList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2)
                    soundControlPanel.loadSound();
            }
        });

        mwdSoundList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2)
                    soundControlPanel.loadSound();
            }
        });

        eslateSoundList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    soundControlPanel.loadSound();
            }
        });

        mwdSoundList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    soundControlPanel.loadSound();
            }
        });
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.height = dialog.tabHeight;
        return dim;
    }

    int promptToSaveCurrentTheme() {
        if (skipSavePrompt) {
          return ESlateContainer.CANCEL;
        }
        String title, msg;
        title = dialog.settingsDialogBundle.getString("SaveSoundThemeTitle");
        msg = dialog.settingsDialogBundle.getString("SaveSoundTheme");
        Object[] yes_no_cancel = {dialog.settingsDialogBundle.getString("Yes"),
                                  dialog.settingsDialogBundle.getString("No"),
                                  dialog.settingsDialogBundle.getString("Cancel")};

        JOptionPane pane = new JOptionPane(msg,
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_CANCEL_OPTION,
            javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
            yes_no_cancel,
            dialog.settingsDialogBundle.getString("Yes"));
        JDialog promptDialog = pane.createDialog(this, title);
        container.playSystemSound(SoundTheme.QUESTION_SOUND);
        promptDialog.show();
        Object option = pane.getValue();
        if (option == dialog.settingsDialogBundle.getString("Cancel"))
            return ESlateContainer.CANCEL;
        if (option == dialog.settingsDialogBundle.getString("Yes"))
            return ESlateContainer.SAVE;
        return ESlateContainer.DONT_SAVE;
    }

    public boolean setSoundTheme(SoundTheme theme, String themeFile) {
        if (currentTheme == theme) return true;

        if (currentTheme != null && currentTheme.isModified()) {
            int option = promptToSaveCurrentTheme();
            if (option == ESlateContainer.CANCEL)
                return false;
            if (option == ESlateContainer.SAVE) {
                if (saveCurrentTheme(false) == null)
                    return false;
            }
        }

        eslateSoundList.setListData(theme.getESlateSounds());
        mwdSoundList.setListData(theme.getMicroworldSounds());
        currentTheme = theme;
//System.out.println("setSoundTheme: " + themeFile);
        currentThemeFile = themeFile;
        return true;
    }

    public SoundTheme getSoundTheme() {
        return currentTheme;
    }

    public String getSoundThemeFileName() {
        return currentThemeFile;
    }

    public void setSoundThemeFiles(ArrayList fileNames) {
        soundThemeControlPanel.setSoundThemes(fileNames);
    }

    public ArrayList getSoundThemeFiles() {
        return soundThemeControlPanel.getSoundThemes();
    }

    public String saveCurrentTheme(boolean saveAsMode) {
        if (currentTheme == null) return null;
        String newThemeFile = currentThemeFile;
        if (newThemeFile == null || saveAsMode)
            newThemeFile = container.getSystemFile(true, dialog.settingsDialogBundle.getString("SelectThemeFile"), null, new String[] {"est"});
        if (newThemeFile == null)
            return null;
        container.saveSoundTheme(currentTheme, newThemeFile, currentThemeFile);
        currentThemeFile = newThemeFile;
        return currentThemeFile;
    }

    // In the following two methods, we explicitly invoke setSound, even
    // though this method is supposed to be called automatically after
    // changing the selected indices of the sound lists. However, this does
    // not happen after double clicking on an item in one of the two lists,
    // when weird things begin to happen, unless we invoke setSound ourselves.

    public void nextSound() {
        if (currentTheme == null)
            return;
        boolean eslateSoundSelected = true;
        int selIndex = eslateSoundList.getSelectedIndex();
        if (selIndex == -1) {
            selIndex = mwdSoundList.getSelectedIndex();
            soundControlPanel.setSound((SystemSound)mwdSoundList.getSelectedValue());
            eslateSoundSelected = false;
        }
        if (selIndex == -1) {
            eslateSoundList.setSelectedIndex(0);
            soundControlPanel.setSound((SystemSound)eslateSoundList.getSelectedValue());
        }else{
            if (eslateSoundSelected) {
                if (eslateSoundList.getSelectedIndex() != eslateSoundList.getModel().getSize()-1) {
                    eslateSoundList.requestFocus();
                    eslateSoundList.setSelectedIndex(selIndex+1);
                    soundControlPanel.setSound((SystemSound)eslateSoundList.getSelectedValue());
                }else{
                    eslateSoundList.clearSelection();
                    mwdSoundList.requestFocus();
                    mwdSoundList.setSelectedIndex(0);
                    soundControlPanel.setSound((SystemSound)mwdSoundList.getSelectedValue());
                }
            }else{
                if (mwdSoundList.getSelectedIndex() != mwdSoundList.getModel().getSize()-1) {
                    mwdSoundList.requestFocus();
                    mwdSoundList.setSelectedIndex(selIndex+1);
                    soundControlPanel.setSound((SystemSound)mwdSoundList.getSelectedValue());
                }else{
                    mwdSoundList.clearSelection();
                    eslateSoundList.requestFocus();
                    eslateSoundList.setSelectedIndex(0);
                    soundControlPanel.setSound((SystemSound)eslateSoundList.getSelectedValue());
                }
            }
        }
    }

    public void prevSound() {
        if (currentTheme == null)
            return;
        boolean eslateSoundSelected = true;
        int selIndex = eslateSoundList.getSelectedIndex();
        if (selIndex == -1) {
            selIndex = mwdSoundList.getSelectedIndex();
            soundControlPanel.setSound((SystemSound)mwdSoundList.getSelectedValue());
            eslateSoundSelected = false;
        }
        if (selIndex == -1) {
            eslateSoundList.setSelectedIndex(0);
            soundControlPanel.setSound((SystemSound)eslateSoundList.getSelectedValue());
        }else{
            if (eslateSoundSelected) {
                if (eslateSoundList.getSelectedIndex() != 0) {
                    eslateSoundList.requestFocus();
                    eslateSoundList.setSelectedIndex(selIndex-1);
                    soundControlPanel.setSound((SystemSound)eslateSoundList.getSelectedValue());
                }else{
                    eslateSoundList.clearSelection();
                    mwdSoundList.requestFocus();
                    mwdSoundList.setSelectedIndex(mwdSoundList.getModel().getSize()-1);
                    soundControlPanel.setSound((SystemSound)mwdSoundList.getSelectedValue());
                }
            }else{
                if (mwdSoundList.getSelectedIndex() != 0) {
                    mwdSoundList.requestFocus();
                    mwdSoundList.setSelectedIndex(selIndex-1);
                    soundControlPanel.setSound((SystemSound)mwdSoundList.getSelectedValue());
                }else{
                    mwdSoundList.clearSelection();
                    eslateSoundList.requestFocus();
                    eslateSoundList.setSelectedIndex(eslateSoundList.getModel().getSize()-1);
                    soundControlPanel.setSound((SystemSound)eslateSoundList.getSelectedValue());
                }
            }
        }
    }

}

class SoundMgmtPanel extends JPanel {
    ImageIcon playSoundInIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/playSoundIn.gif"));
    ImageIcon playSoundIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/playSound.gif"));
    ImageIcon stopSoundInIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/stopSoundIn.gif"));
    ImageIcon stopSoundIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/stopSound.gif"));
    ImageIcon prevSoundInIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/prevSoundIn.gif"));
    ImageIcon prevSoundIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/prevSound.gif"));
    ImageIcon nextSoundInIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/nextSoundIn.gif"));
    ImageIcon nextSoundIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/nextSound.gif"));
    ImageIcon loadSoundIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/openFile.gif"));
    ImageIcon deleteSoundIcon = new ImageIcon(SoundMgmtPanel.class.getResource("images/delete.gif"));

    JButton play, stop, next, prev;
    NoBorderButton load, delete;
    JTextField soundNameField;
    private Insets buttonInsets = new Insets(0, 0, 0, 0);
    private Dimension minSize = null, maxSize = null;
    SystemSound systemSound = null;
    SoundSettingsPanel ssp = null;
    ESlateSound sound = null;
//    String soundFileName = null;

    ESlateSoundListener eslateSoundListener = new ESlateSoundListener() {
        public void soundStopped(ESlateSoundEvent e) {
            stopSound();
//            SoundControlPanel.this.soundPanel.container.soundListenerMap.removePlayingSound((ESlateSound) e.getSource());
        }
    };


    public SoundMgmtPanel(SoundSettingsPanel ssp) {
        this.ssp = ssp;

        play = new JButton(playSoundIcon) {
            public void paint(java.awt.Graphics gr) {
                java.awt.Graphics2D gr2d = (java.awt.Graphics2D) gr;
                gr2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                super.paint(gr);
            }
        };
        play.setDisabledIcon(playSoundInIcon);
        play.setToolTipText(ssp.dialog.settingsDialogBundle.getString("Play"));
        stop = new JButton(stopSoundIcon);
        stop.setDisabledIcon(stopSoundInIcon);
        stop.setToolTipText(ssp.dialog.settingsDialogBundle.getString("Stop"));
        prev = new JButton(prevSoundIcon);
        prev.setDisabledIcon(prevSoundInIcon);
        prev.setToolTipText(ssp.dialog.settingsDialogBundle.getString("Previous"));
        next = new JButton(nextSoundIcon);
        next.setDisabledIcon(nextSoundInIcon);
        next.setToolTipText(ssp.dialog.settingsDialogBundle.getString("Next"));
        load = new NoBorderButton(loadSoundIcon);
        load.setToolTipText(ssp.dialog.settingsDialogBundle.getString("Load"));
        delete = new NoBorderButton(deleteSoundIcon);
        delete.setToolTipText(ssp.dialog.settingsDialogBundle.getString("Delete"));

        play.setMargin(buttonInsets);
        stop.setMargin(buttonInsets);
        prev.setMargin(buttonInsets);
        next.setMargin(buttonInsets);
        load.setMargin(buttonInsets);
        delete.setMargin(buttonInsets);

        Border buttonBorder = new EmptyBorder(0, 0, 0,2);
        play.setBorder(buttonBorder);
        stop.setBorder(buttonBorder);
        prev.setBorder(buttonBorder);
        next.setBorder(buttonBorder);
        play.setFocusPainted(false);
        stop.setFocusPainted(false);
        next.setFocusPainted(false);
        prev.setFocusPainted(false);
        load.setFocusPainted(false);
        delete.setFocusPainted(false);
        play.setRequestFocusEnabled(false);
        stop.setRequestFocusEnabled(false);
        next.setRequestFocusEnabled(false);
        prev.setRequestFocusEnabled(false);
        load.setRequestFocusEnabled(false);
        delete.setRequestFocusEnabled(false);
        play.setHorizontalAlignment(SwingConstants.CENTER);
        stop.setHorizontalAlignment(SwingConstants.CENTER);
        next.setHorizontalAlignment(SwingConstants.CENTER);
        prev.setHorizontalAlignment(SwingConstants.CENTER);

        minSize = new Dimension(300, playSoundInIcon.getIconHeight());
        maxSize = new Dimension(300, playSoundInIcon.getIconHeight()+5);
        soundNameField = new JTextField() {
            public Dimension getMinimumSize() {
                return minSize;
            }
            public Dimension getPreferredSize() {
                return maxSize;
            }
            public Dimension getMaximumSize() {
                return maxSize;
            }
        };

        JPanel controlPanel = new JPanel();/* {
            public Dimension getMinimumSize() {
                return minSize;
            }
            public Dimension getMaximumSize() {
                return maxSize;
            }
        };*/
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.add(Box.createGlue()); //Box.createHorizontalStrut(3));
        controlPanel.add(play);
        controlPanel.add(stop);
        controlPanel.add(prev);
        controlPanel.add(next);
        controlPanel.add(Box.createHorizontalStrut(5));
        controlPanel.add(soundNameField);
        controlPanel.add(Box.createHorizontalStrut(5));
        controlPanel.add(load);
        controlPanel.add(Box.createHorizontalStrut(2));
        controlPanel.add(delete);
        controlPanel.add(Box.createGlue());

        soundNameField.setEditable(false);
        soundNameField.setHorizontalAlignment(JTextField.LEFT);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); //new BorderLayout(0, 3));
        add(controlPanel); //, BorderLayout.SOUTH);
        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(ssp.dialog.settingsDialogBundle.getString("SoundMgmt"));
        tb1.setTitleColor(titleBorderColor);
        setBorder(new CompoundBorder(tb1, new EmptyBorder(0, 0, 2, 0)));

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundMgmtPanel.this.ssp.nextSound();
            }
        });

        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundMgmtPanel.this.ssp.prevSound();
            }
        });
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSound();
            }
        });
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (systemSound == null)
                    return;
                systemSound.setValue(null);
                systemSound.pathToSound = null;
//                ssp.currentTheme.modified = true;
                play.setEnabled(false);
                delete.setEnabled(false);
                soundNameField.setText("");
                SoundMgmtPanel.this.ssp.mwdSoundList.repaint();
                SoundMgmtPanel.this.ssp.eslateSoundList.repaint();
            }
        });
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playSound();
            }
        });
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopSound();
            }
        });
    }

    void loadSound() {
        ssp.dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SoundFileChooser sfc = ssp.container.getSoundFileChooser();
        ssp.dialog.setCursor(Cursor.getDefaultCursor());
        if (sfc.isShowing()) {
            return;
        }
        int status = sfc.showOpenDialog(ssp.container.parentComponent);
        if (status == javax.swing.JFileChooser.CANCEL_OPTION)
            return;

        if (sfc.getSelectedFile() == null)
            return;

        File soundFile = sfc.getSelectedFile();
        String pathToSound = soundFile.getAbsolutePath();
        String soundName = soundFile.getName();
        systemSound.setValue(soundName);
        systemSound.pathToSound = pathToSound;
//System.out.println("loadSound() systemSound: " + systemSound.getKey());
//        ssp.currentTheme.modified = true;
        play.setEnabled(true);
        delete.setEnabled(true);
        soundNameField.setText(soundName);
//System.out.println("loadSound() currentSoundTheme: " + ssp.currentTheme);
    }

    void playSound() {
        if (systemSound == null) {
            play.setEnabled(false);
            return;
        }
        if (systemSound.pathToSound != null)
            sound = SoundUtils.playSound(systemSound.pathToSound);
        else{
            String fileName = (String) systemSound.getValue();
            if (fileName == null) {
                play.setEnabled(false);
                return;
            }
            Vector<String> path = new Vector<String>();
            path.add("sounds");
            path.add(fileName);
            try{
                StructFile themeStructFile = new StructFile(ssp.currentThemeFile, StructFile.OLD);
                sound = SoundUtils.playSound(themeStructFile,
                                             ssp.currentThemeFile,
                                             path);
                themeStructFile.close();
            }catch (Throwable thr) {
                DetailedErrorDialog dialog = new DetailedErrorDialog(ssp.dialog);
                ResourceBundle bundle = ssp.container.containerBundle;
                dialog.setMessage(bundle.getString("ContainerMsg64") + systemSound.getKey() + bundle.getString("ContainerMsg65") + bundle.getString("Quote"));
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDetailedErrorDialog(ssp.container, dialog, ssp.dialog, true);
            }
        }
        sound.addSoundListener(eslateSoundListener);
        play.setEnabled(false);
        stop.setEnabled(true);
    }

    void stopSound() {
        if (sound == null) return;
        SoundUtils.stopSound(sound);
        sound.removeSoundListener(eslateSoundListener);
        sound = null;
        stop.setEnabled(false);
        play.setEnabled(true);
    }

    public void setSound(SystemSound sound) {
        if (this.sound != null)
            stopSound();
/*if (sound != null)
    System.out.println("SoundMgmtPanel Setting sound: " + sound.getKey());
else
    System.out.println("SoundMgmtPanel Setting null sound ");
*/
        this.systemSound = sound;
        String soundFileName = null;
        if (sound != null)
            soundFileName = (String) sound.getValue();
        if (sound == null) {
          play.setEnabled(false);
          stop.setEnabled(false);
          next.setEnabled(false);
          prev.setEnabled(false);
          load.setEnabled(false);
          delete.setEnabled(false);
          soundNameField.setEnabled(false);
        }else{
            if (soundFileName != null) {
                play.setEnabled(true);
                stop.setEnabled(false);
                next.setEnabled(true);
                prev.setEnabled(true);
                soundNameField.setEnabled(true);
                soundNameField.setText((String) sound.getValue());
                load.setEnabled(true);
                delete.setEnabled(true);
            }else{
                play.setEnabled(false);
                stop.setEnabled(false);
                next.setEnabled(true);
                prev.setEnabled(true);
                soundNameField.setEnabled(true);
                soundNameField.setText("");
                load.setEnabled(true);
                delete.setEnabled(false);
            }
        }
    }
}

class SoundThemeControlPanel extends JPanel {
    SoundSettingsPanel ssp;
    JList themeList;
    JButton newTheme, deleteTheme, loadTheme, saveThemeAs;
    DefaultListModel themeListModel;
    boolean deletingTheme = false; // Flag which is true while a theme is being deleted

    public SoundThemeControlPanel(SoundSettingsPanel ssp) {
        this.ssp = ssp;

        themeList = new JList();
        themeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        themeListModel = new DefaultListModel();
        themeList.setModel(themeListModel);

        JScrollPane scrollPane = new JScrollPane(themeList);
        scrollPane.setBorder(new EtchedBorder());

        newTheme = new JButton(ssp.dialog.settingsDialogBundle.getString("NewTheme"));
        deleteTheme = new JButton(ssp.dialog.settingsDialogBundle.getString("DeleteTheme"));
        loadTheme = new JButton(ssp.dialog.settingsDialogBundle.getString("LoadTheme"));
        saveThemeAs = new JButton(ssp.dialog.settingsDialogBundle.getString("SaveThemeAs"));

        FontMetrics fm = newTheme.getFontMetrics(newTheme.getFont());
        int maxWidth = fm.stringWidth(newTheme.getText());
        int w = fm.stringWidth(deleteTheme.getText());
        if (w > maxWidth) maxWidth = w;
        w = fm.stringWidth(loadTheme.getText());
        if (w > maxWidth) maxWidth = w;
        w = fm.stringWidth(saveThemeAs.getText());
        if (w > maxWidth) maxWidth = w;
        maxWidth = maxWidth + 10;

        Dimension buttonDim = new Dimension(maxWidth, 25);
        newTheme.setMinimumSize(buttonDim);
        newTheme.setPreferredSize(buttonDim);
        newTheme.setMaximumSize(buttonDim);
        loadTheme.setMinimumSize(buttonDim);
        loadTheme.setPreferredSize(buttonDim);
        loadTheme.setMaximumSize(buttonDim);
        deleteTheme.setMinimumSize(buttonDim);
        deleteTheme.setPreferredSize(buttonDim);
        deleteTheme.setMaximumSize(buttonDim);
        saveThemeAs.setMinimumSize(buttonDim);
        saveThemeAs.setPreferredSize(buttonDim);
        saveThemeAs.setMaximumSize(buttonDim);

        Insets buttonInsets = new Insets(0, 0, 0, 0);
        newTheme.setMargin(buttonInsets);
        deleteTheme.setMargin(buttonInsets);
        loadTheme.setMargin(buttonInsets);
        saveThemeAs.setMargin(buttonInsets);

        Color color128 = new Color(0, 0, 128);
        newTheme.setForeground(color128);
        deleteTheme.setForeground(color128);
        loadTheme.setForeground(color128);
        saveThemeAs.setForeground(color128);

        newTheme.setToolTipText(ssp.dialog.settingsDialogBundle.getString("NewThemeTip"));
        deleteTheme.setToolTipText(ssp.dialog.settingsDialogBundle.getString("DeleteThemeTip"));
        loadTheme.setToolTipText(ssp.dialog.settingsDialogBundle.getString("LoadThemeTip"));
        saveThemeAs.setToolTipText(ssp.dialog.settingsDialogBundle.getString("SaveThemeAsTip"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(newTheme);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(loadTheme);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(saveThemeAs);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteTheme);
        buttonPanel.add(Box.createGlue());

        setLayout(new BorderLayout(0, 5)); // new BoxLayout(this, BoxLayout.Y_AXIS));//; //
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
//        add(Box.createVerticalStrut(3));

        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(ssp.dialog.settingsDialogBundle.getString("SoundThemes"));
        tb1.setTitleColor(titleBorderColor);
        setBorder(new CompoundBorder(tb1, new EmptyBorder(0, 0, 3, 0)));

        newTheme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundSettingsPanel ssp = SoundThemeControlPanel.this.ssp;
                String soundThemeFileName = ssp.container.getSystemFile(true, ssp.dialog.settingsDialogBundle.getString("SelectThemeFile"), null, new String[] {"est"});
                if (soundThemeFileName == null)
                    return;
                try{
                    StructFile themeFile = new StructFile(soundThemeFileName, StructFile.NEW);
                    ObjectOutputStream oos = new ObjectOutputStream(themeFile.openOutputFile("SoundList"));
                    SoundTheme theme = new SoundTheme(ssp.container);
                    oos.writeObject(theme);
                    oos.flush();
                    oos.close();
                    themeFile.createDirectory("sounds");
                    themeFile.close();
                    ssp.setSoundTheme(theme, soundThemeFileName);
                    themeListModel.addElement(soundThemeFileName);
                    // We have already prompted to save the current theme.
                    ssp.skipSavePrompt = true;
                    themeList.setSelectedValue(soundThemeFileName, true);
                    ssp.skipSavePrompt = false;
                    themeList.repaint();
                }catch (Throwable thr) {
                    DetailedErrorDialog dialog = new DetailedErrorDialog(ssp.dialog);
                    dialog.setMessage(ssp.dialog.settingsDialogBundle.getString("UnableToCreateNewTheme") + soundThemeFileName + "\"");
                    dialog.appendThrowableStackTrace(thr);
                    ESlateContainerUtils.showDetailedErrorDialog(ssp.container, dialog, ssp.dialog, true);
                }
            }
        });

        saveThemeAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String themeFile = SoundThemeControlPanel.this.ssp.saveCurrentTheme(true);
                int index = themeListModel.indexOf(themeFile);
                if (index == -1)
                    themeListModel.addElement(themeFile);
                themeList.setSelectedValue(themeFile, true);
//                soundThemes.add(SoundThemeControlPanel.this.ssp.currentTheme);
                themeList.repaint();
            }
        });

        loadTheme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SoundSettingsPanel ssp = SoundThemeControlPanel.this.ssp;
                String soundThemeFileName = ssp.container.getSystemFile(false, ssp.dialog.settingsDialogBundle.getString("SelectThemeFile"), null, new String[] {"est"});
                if (soundThemeFileName != null) {
                    int index = themeListModel.indexOf(soundThemeFileName);
                    if (index != -1) {
                        themeList.setSelectedValue(soundThemeFileName, true);
                        return;
                    }
                    themeListModel.addElement(soundThemeFileName);
                    themeList.setSelectedValue(soundThemeFileName, true);
                    themeList.repaint();
                }
            }
        });

        deleteTheme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = themeList.getSelectedIndex();
                if (selectedIndex == -1)
                    return;

                SoundSettingsPanel ssp = SoundThemeControlPanel.this.ssp;
                  if (ssp.currentTheme != null && ssp.currentTheme.isModified()) {
                      int option = ssp.promptToSaveCurrentTheme();
                      if (option == ESlateContainer.CANCEL)
                          return;
                      if (option == ESlateContainer.SAVE) {
                          if (ssp.saveCurrentTheme(false) == null);
//                              return false;
                      }
                  }

                deletingTheme = true;
                int newSelectionIndex = 0;
                if (selectedIndex == themeListModel.size()-1) {
                    newSelectionIndex = selectedIndex-1;
                }else{
                    newSelectionIndex = selectedIndex+1;
                }
                themeList.setSelectedIndex(newSelectionIndex);

                /* When the themeList get's empty -the last theme removed-
                 * set the theme of the SoundSettingsPanel to a new, empty theme.
                 */
                if (newSelectionIndex == -1)  // Nothing selected in the themeList
                    ssp.setSoundTheme(new SoundTheme(ssp.container), null);

                themeListModel.remove(selectedIndex);
                themeList.repaint();
                deletingTheme = false;
            }
        });

        themeList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                JList list = (JList) e.getSource();
                String soundThemeFileName = (String) list.getSelectedValue();
                SoundSettingsPanel ssp = SoundThemeControlPanel.this.ssp;
                if (list.getSelectedIndex() == -1 && !deletingTheme) {
                    themeList.setSelectedValue(ssp.currentThemeFile, true);
                    return;
                }

                if (ssp.currentThemeFile != null && ssp.currentThemeFile.equals(soundThemeFileName)) {
                    return;
                }

                if (soundThemeFileName == null) { // Nothing selected in the themeList
                    ssp.setSoundTheme(new SoundTheme(ssp.container), null);
                    return;
                }
                SoundTheme newTheme = null;
                try{
                    StructFile soundThemeStructFile = new StructFile(soundThemeFileName, StructFile.OLD);
                    ObjectInputStream ois = new ObjectInputStream(soundThemeStructFile.openInputFile("SoundList"));
                    newTheme = (SoundTheme) ois.readObject();
                    ois.close();
                    soundThemeStructFile.close();
                }catch (Throwable thr) {
                    DetailedErrorDialog dialog = new DetailedErrorDialog(ssp.dialog);
                    dialog.setMessage(ssp.dialog.settingsDialogBundle.getString("UnableToReadTheme") + soundThemeFileName + "\"");
                    dialog.appendThrowableStackTrace(thr);
                    ESlateContainerUtils.showDetailedErrorDialog(ssp.container, dialog, ssp.dialog, true);
                }
                if (newTheme == null)
                    themeList.setSelectedValue(ssp.currentThemeFile, true);
                else{
                    if (!ssp.setSoundTheme(newTheme, soundThemeFileName))
                        themeList.setSelectedValue(ssp.currentThemeFile, true);
                }
//System.out.println("Selected theme: " + ssp.getSoundTheme());
            }
        });
   }

   public void setSoundThemes(ArrayList soundThemeFiles) {
      for (int i=0; i<soundThemeFiles.size(); i++)
          themeListModel.addElement(soundThemeFiles.get(i));
   }

   public ArrayList getSoundThemes() {
      ArrayList themeFiles = new ArrayList();
      for (int i=0; i<themeListModel.size(); i++)
          themeFiles.add(themeListModel.get(i));
      return themeFiles;
   }
}
