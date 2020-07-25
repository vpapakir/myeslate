package gr.cti.eslate.database;

import com.zookitec.layout.*;

import javax.swing.*;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.awt.*;

class PreferencesDialog extends JDialog {
    public final static int DIALOG_OK = 1;
    public final static int DIALOG_CANCELLED = 0;
    DBTable dbTable = null;
    Database database = null;
    int dialogWidth = 435; //390
    int dialogHeight = 480; //465; //488;
    int entryMaximumHeight = 40;
    int entryVerticalGap = 10;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
//    Font dialogFont;
//    FontMetrics dialogFontMetrics;

    static final int NUM_PREDEFINED_COLORS = 14;

    /* Font attributes.
     */
    JComboBox fontType, fontSize;
    JToggleButton boldButton, italicButton;
//    FontMetrics fm;
//    Rectangle fontDemoRect = null;
//    Rectangle fontDemoRectBackUp = null;
//    int fontDemoWidth;

    JTabbedPane preferencesTabs;
//    boolean firstVisit = true;
    JButton okButton, cancelButton, chooseColor;
    ActionListener okListener, cancelListener;
    WindowListener l;
    KeyAdapter ka;

//1    String dateFormatStr;
//1    String timeFormatStr;

//    boolean highlightNonEditable;
    JComboBox formatsBox, formats2Box, backColorBox, gridColorBox, selectionColorBox, highlightColorBox, activeRecordColorBox;
    JComboBox selectionForegroundColorBox;
    JComboBox integerColorBox, doubleColorBox, floatColorBox, booleanColorBox, stringColorBox, dateColorBox, timeColorBox, urlColorBox;
    JComboBox activeComboBox = null;
    JCheckBox tableScopeForFieldPropertiesChangeBox, tableScopeForColorPropertiesChangeBox, tableScopeForAdvancedPropertiesChangeBox;
//1    JRadioButton rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, /*rb11, rb12,*/ rb13;
    JTextField rowHeightField;
    JLabel fontDemo;
    JCheckBox expandedHeaderBox, displayHorizontalLinesBox, displayVerticalLinesBox, simultaneousRowColumnActivationBox, highlightNonEditableFieldsBox;
    JTextField thousandSeparatorField, decimalSeparatorField;
    JCheckBox onlyIntegerBox, useExponentiationFormatBox, showDecimalSeparatorBox, useThousandSeparatorBox;

    Font tableFont = null;
    int lastValidRowHeight = 20;

/*    boolean updateActiveTableOnly1 = false, updateActiveTableOnly2 = false, updateActiveTableOnly3 = false;
    Color backgroundColor = null, gridColor = null, selectionBackground = null, selectionForeground = null, highlightColor = null;
    Color integerColor = null, doubleColor = null, stringColor = null, booleanColor = null, dateColor = null, timeColor = null;
    Color urlColor = null, floatColor = null, activeRecordColor = null;
    int rowHeight = 10;
    boolean horizontalLinesVisible = true, verticalLinesVisible = true, simultaneousFieldRecordActivation = false;
    boolean headerIconsVisible = false, onlyIntegerPart = false, exponentiationFormatUsed = false, decimalSeparatorAlwaysVisible = false;
    String decimalSeparator = ",", thousandSeparator = ".";
    boolean thousandSeparatorVisible = false;
*/
/*    outputValues.add(new Boolean(tableScopeForColorPropertiesChangeBox.isSelected()));
    outputValues.add("");
    outputValues.add(new Boolean(tableScopeForAdvancedPropertiesChangeBox.isSelected()));
*/
    final Frame dialogFrame;
    static boolean messageDelivered = false;
    int returnCode = DIALOG_CANCELLED;
//    static Array inputValues = new Array();
//1    static Array outputValues = new Array();
    String activeDBTableTitle;

    transient ResourceBundle infoBundle; // = ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());


    protected PreferencesDialog(Frame frame, DBTable dbTable, Database dbComponent) {
        super(frame, true);
        dialogFrame = frame;
        this.dbTable = dbTable;
        this.database = dbComponent;
        activeDBTableTitle = dbTable.table.getTitle();
        infoBundle = Database.infoBundle;
        setTitle(infoBundle.getString("PreferencesDialogMsg1"));
        preferencesTabs = new JTabbedPane();

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancelButton.doClick();
          		javax.swing.ButtonModel bm = cancelButton.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);



/*1        if (infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) {
             dialogFont = new Font("Helvetica", Font.PLAIN, 12);
             dialogWidth = dialogWidth + 20;
        }else
            dialogFont = new Font("Dialog", Font.PLAIN, 12);
        dialogFontMetrics = toolkit.getFontMetrics(dialogFont);
*/

        Color titleBorderColor = new Color(119, 40, 104);

//        getContentPane().setBackground(Color.lightGray);

        /*
         * FIRST TAB: field preferences.
         */
        /* The container Panel
         */
        final JPanel fieldPreferences = new JPanel();
        BoxLayout bl2 = new BoxLayout(fieldPreferences, BoxLayout.Y_AXIS);
        fieldPreferences.setLayout(bl2);
        fieldPreferences.add(Box.createVerticalStrut(entryVerticalGap));

        /* The Date/Time format panel.
         */
        JPanel dateTimeFormatPanel = new JPanel();
        dateTimeFormatPanel.setLayout(new BoxLayout(dateTimeFormatPanel, BoxLayout.Y_AXIS));
        Dimension d3 = new Dimension(dialogWidth, 2*entryMaximumHeight + 45);
        dateTimeFormatPanel.setMaximumSize(d3);
        dateTimeFormatPanel.setMinimumSize(d3);
        dateTimeFormatPanel.setPreferredSize(d3);
        dateTimeFormatPanel.add(Box.createVerticalStrut(3));

        /* The "dateFormatPanel" Box's panel.
         */
        JPanel dateFormatPanel = new JPanel();
        dateFormatPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        formatsBox = new JComboBox();
        formatsBox.setAlignmentY(CENTER_ALIGNMENT);
        formatsBox.setPreferredSize(new Dimension(218, 21));
        formatsBox.setMinimumSize(new Dimension(218, 21));
        formatsBox.setMaximumSize(new Dimension(218, 21));

        int labelMaxWidth = dialogWidth-50-218-20;  //dialogWidth - 50, the width of the LOWERED panels
                                                    //218, the width og the combo box
                                                    //20 the left and rifht panel gaps

        ArrayList a = dbTable.table.getDateFormats();
        String[] b = dbTable.table.getSampleDates();
        for (int i=0; i<a.size(); i++)
            formatsBox.addItem(((String) a.get(i)) + b[i]);
        /* Setting the dispalyed item to the current DBTable's date format.
         */
//1        dateFormatStr = (String) inputValues.at(2);
//1        int index = a.indexOf(dateFormatStr);
//        System.out.println(dateFormatStr + b[index]);
//1        formatsBox.setSelectedItem(dateFormatStr + b[index]);

        dateFormatPanel.add(Box.createHorizontalStrut(10));
        JLabel l2 = new JLabel(infoBundle.getString("PreferencesDialogMsg2"));
        FontMetrics dialogFontMetrics = l2.getFontMetrics(l2.getFont());
        int l2Width = dialogFontMetrics.stringWidth(l2.getText());
        int gap = labelMaxWidth - l2Width;
        Dimension labelDim = new Dimension(labelMaxWidth, 15);
        l2.setMaximumSize(labelDim);
        l2.setMinimumSize(labelDim);
        l2.setPreferredSize(labelDim);

        l2.setAlignmentY(CENTER_ALIGNMENT);
        dateFormatPanel.add(l2);
        dateFormatPanel.add(formatsBox);

        BoxLayout bl3 = new BoxLayout(dateFormatPanel, BoxLayout.X_AXIS);
        dateFormatPanel.setLayout(bl3);
        dateFormatPanel.setMinimumSize(new Dimension(dialogWidth-50, entryMaximumHeight));
        dateFormatPanel.setPreferredSize(new Dimension(dialogWidth-50, entryMaximumHeight));
        dateFormatPanel.setMaximumSize(new Dimension(dialogWidth-50, entryMaximumHeight));
        dateTimeFormatPanel.add(dateFormatPanel);
        dateFormatPanel.setAlignmentX(CENTER_ALIGNMENT);
        dateTimeFormatPanel.add(Box.createVerticalStrut(10)); //entryVerticalGap));

        /* The "timeFormatPanel" Box's panel.
         */
        JPanel timeFormatPanel = new JPanel();
        timeFormatPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        formats2Box = new JComboBox();
        formats2Box.setAlignmentY(CENTER_ALIGNMENT);
        formats2Box.setPreferredSize(new Dimension(218, 21));
        formats2Box.setMinimumSize(new Dimension(218, 21));
        formats2Box.setMaximumSize(new Dimension(218, 21));
        ArrayList a1 = dbTable.table.getTimeFormats();
        for (int i=0; i<a1.size(); i++)
            formats2Box.addItem(a1.get(i));
        /* Setting the displayed item to the current DBTable's time format.
         */
//1        timeFormatStr = (String) inputValues.at(3);
//        System.out.println(timeFormatStr);
//1        formats2Box.setSelectedItem(timeFormatStr);

        timeFormatPanel.add(Box.createHorizontalStrut(10));
        JLabel l3 = new JLabel(infoBundle.getString("PreferencesDialogMsg3"));
        int l3Width = dialogFontMetrics.stringWidth(infoBundle.getString("PreferencesDialogMsg3"));
        timeFormatPanel.add(l3);
        l3.setAlignmentY(CENTER_ALIGNMENT);
        l3.setMaximumSize(labelDim);
        l3.setMinimumSize(labelDim);
        l3.setPreferredSize(labelDim);
        timeFormatPanel.add(formats2Box);

        BoxLayout bl4 = new BoxLayout(timeFormatPanel, BoxLayout.X_AXIS);
        timeFormatPanel.setLayout(bl4);
        timeFormatPanel.setMinimumSize(new Dimension(dialogWidth-50, entryMaximumHeight));
        timeFormatPanel.setPreferredSize(new Dimension(dialogWidth-50, entryMaximumHeight));
        timeFormatPanel.setMaximumSize(new Dimension(dialogWidth-50, entryMaximumHeight));
        dateTimeFormatPanel.add(timeFormatPanel);
        timeFormatPanel.setAlignmentX(CENTER_ALIGNMENT);

        /* Add "dateTimeFormatPanel" to "fieldpreferences" panel.
         */
        fieldPreferences.add(dateTimeFormatPanel);
        /* Set the border of the "dateTimeFormatPanel" panel to the following TitleBorder.
         */
        TitledBorder tb14 = new TitledBorder(new LineBorder(Color.darkGray), infoBundle.getString("PreferencesDialogMsg4"));
        tb14.setTitleColor(titleBorderColor);
        dateTimeFormatPanel.setBorder(tb14);

        JPanel numberPreferences = new JPanel(true);
        numberPreferences.setLayout(new BoxLayout(numberPreferences, BoxLayout.Y_AXIS));

        JPanel int_expPanel = new JPanel(true);
        int_expPanel.setLayout(new BoxLayout(int_expPanel, BoxLayout.X_AXIS));

        onlyIntegerBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg5"));
//        onlyIntegerBox.setFont(dbComponent.UIFont);
//1        onlyIntegerBox.setSelected(((Boolean) inputValues.at(26)).booleanValue());
        onlyIntegerBox.setAlignmentY(CENTER_ALIGNMENT);
        useExponentiationFormatBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg6"));
//        useExponentiationFormatBox.setFont(dbComponent.UIFont);
//1        useExponentiationFormatBox.setSelected(((Boolean) inputValues.at(27)).booleanValue());
        if (useExponentiationFormatBox.isSelected()) {
            onlyIntegerBox.setSelected(false);
            onlyIntegerBox.setEnabled(false);
        }
        useExponentiationFormatBox.setAlignmentY(CENTER_ALIGNMENT);
        FontMetrics UIFontMetrics = getFontMetrics(onlyIntegerBox.getFont()); //dbComponent.UIFont);
        Dimension expDim = new Dimension(UIFontMetrics.stringWidth(infoBundle.getString("PreferencesDialogMsg6")) + 17, 25);
        useExponentiationFormatBox.setMaximumSize(expDim);
        useExponentiationFormatBox.setMinimumSize(expDim);
        useExponentiationFormatBox.setPreferredSize(expDim);
        int_expPanel.add(Box.createHorizontalStrut(5));
        int_expPanel.add(onlyIntegerBox);
        int_expPanel.add(Box.createGlue());
        int_expPanel.add(useExponentiationFormatBox);
        /* PROBLEM: this horizontal strut should be in the code????
         */
        int_expPanel.add(Box.createHorizontalStrut(5));
        Dimension d = new Dimension(dialogWidth-10, 26); //Swing 1.1
        int_expPanel.setMinimumSize(d);
        int_expPanel.setMaximumSize(d);
        int_expPanel.setPreferredSize(d);

        JPanel decimalPanel = new JPanel(true);
        decimalPanel.setLayout(new BoxLayout(decimalPanel, BoxLayout.X_AXIS));

        showDecimalSeparatorBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg7"));
//        showDecimalSeparatorBox.setFont(dbComponent.UIFont);
        if (onlyIntegerBox.isSelected())
            showDecimalSeparatorBox.setEnabled(false);
//1        else
//1            showDecimalSeparatorBox.setSelected(((Boolean) inputValues.at(28)).booleanValue());
        showDecimalSeparatorBox.setAlignmentY(CENTER_ALIGNMENT);
        JLabel l10 = new JLabel(infoBundle.getString("PreferencesDialogMsg8"));
        l10.setAlignmentY(CENTER_ALIGNMENT);
        Dimension d1 = new Dimension(18, 18);
        decimalSeparatorField = new JTextField();
//1        String decSeparator = (String) inputValues.at(29);
//1        if (decSeparator.equals(",") || decSeparator.equals("."))
//1            decSeparator = decSeparator + " ";
//1        decimalSeparatorField.setText(decSeparator);
        decimalSeparatorField.setBackground(Color.lightGray);
        decimalSeparatorField.setHorizontalAlignment(SwingConstants.RIGHT);
        decimalSeparatorField.setFont(new Font("Dialog", Font.BOLD, 16));
//        decimalSeparatorField.setForeground(Color.red);
        decimalSeparatorField.setAlignmentY(CENTER_ALIGNMENT);
        decimalSeparatorField.setMinimumSize(d1);
        decimalSeparatorField.setMaximumSize(d1);
        decimalSeparatorField.setPreferredSize(d1);
        decimalPanel.add(Box.createHorizontalStrut(5));
        decimalPanel.add(showDecimalSeparatorBox);
//        decimalPanel.add(Box.createHorizontalStrut(5));
        decimalPanel.add(Box.createGlue());
        decimalPanel.add(l10);
        decimalPanel.add(decimalSeparatorField);
        decimalPanel.add(Box.createHorizontalStrut(5));
        decimalPanel.setMinimumSize(d);
        decimalPanel.setMaximumSize(d);
        decimalPanel.setPreferredSize(d);
//        decimalPanel.setBorder(new LineBorder(Color.black));

        JPanel thousandPanel = new JPanel(true);
        thousandPanel.setLayout(new BoxLayout(thousandPanel, BoxLayout.X_AXIS));

        useThousandSeparatorBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg9"));
//        useThousandSeparatorBox.setFont(dbComponent.UIFont);
        if (useExponentiationFormatBox.isSelected())
            useThousandSeparatorBox.setEnabled(false);
//1        else
//1            useThousandSeparatorBox.setSelected(((Boolean) inputValues.at(30)).booleanValue());
        useThousandSeparatorBox.setAlignmentY(CENTER_ALIGNMENT);
        JLabel l11 = new JLabel(infoBundle.getString("PreferencesDialogMsg10"));
        l11.setAlignmentY(CENTER_ALIGNMENT);
        thousandSeparatorField = new JTextField();
//1        String thSeparator = (String) inputValues.at(31);
//1        if (thSeparator.equals(",") || thSeparator.equals("."))
//1            thSeparator = thSeparator + " ";
//1        thousandSeparatorField.setText(thSeparator);
        thousandSeparatorField.setBackground(Color.lightGray);
        thousandSeparatorField.setHorizontalAlignment(SwingConstants.RIGHT);
        thousandSeparatorField.setFont(new Font("Dialog", Font.BOLD, 16));
//        thousandSeparatorField.setForeground(Color.red);
        thousandSeparatorField.setAlignmentY(CENTER_ALIGNMENT);
        thousandSeparatorField.setMinimumSize(d1);
        thousandSeparatorField.setMaximumSize(d1);
        thousandSeparatorField.setPreferredSize(d1);
        thousandPanel.add(Box.createHorizontalStrut(5));
        thousandPanel.add(useThousandSeparatorBox);
        thousandPanel.add(Box.createGlue()); //(Box.createHorizontalStrut(17));
        thousandPanel.add(l11);
        thousandPanel.add(thousandSeparatorField);
        thousandPanel.add(Box.createHorizontalStrut(5));
        thousandPanel.setMinimumSize(d);
        thousandPanel.setMaximumSize(d);
        thousandPanel.setPreferredSize(d);

        numberPreferences.add(int_expPanel);
        numberPreferences.add(decimalPanel);
        numberPreferences.add(thousandPanel);
        /* Set the border of the "numberPreferences" panel to the following TitleBorder.
         */
        TitledBorder tb13 = new TitledBorder(new LineBorder(Color.darkGray), infoBundle.getString("PreferencesDialogMsg11"));
        tb13.setTitleColor(titleBorderColor);
        numberPreferences.setBorder(tb13);

        fieldPreferences.add(Box.createVerticalStrut(10));
        fieldPreferences.add(numberPreferences);

        onlyIntegerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (onlyIntegerBox.isSelected()) {
                    if (showDecimalSeparatorBox.isSelected())
                        showDecimalSeparatorBox.setSelected(false);
                    showDecimalSeparatorBox.setEnabled(false);
                }else
                    showDecimalSeparatorBox.setEnabled(true);
            }
        });

        thousandSeparatorField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB || e.isActionKey())
                    return;
//                System.out.println(thousandSeparatorField.getText() + ", " + thousandSeparatorField.getText().length());
                if (e.getKeyChar() == ',' || e.getKeyChar() == '.') {
                    char[] c = new char[2];
                    c[0] = e.getKeyChar();
                    c[1] = ' ';
                    thousandSeparatorField.setText(new String(c));
                }else{
                    char[] c = new char[1];
                    c[0] = e.getKeyChar();
                    thousandSeparatorField.setText(new String(c));
                }
            }
        });
        decimalSeparatorField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB || e.isActionKey())
                    return;
//                System.out.println(decimalSeparatorField.getText() + ", " + decimalSeparatorField.getText().length());
                if (e.getKeyChar() == ',' || e.getKeyChar() == '.') {
                    char[] c = new char[2];
                    c[0] = e.getKeyChar();
                    c[1] = ' ';
                    decimalSeparatorField.setText(new String(c));
                }else{
                    char[] c = new char[1];
                    c[0] = e.getKeyChar();
                    decimalSeparatorField.setText(new String(c));
                }
            }
        });
        thousandSeparatorField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                thousandSeparatorField.setBackground(Color.white);
                thousandSeparatorField.getCaret().setVisible(false);
                thousandSeparatorField.paintImmediately(thousandSeparatorField.getVisibleRect());
            }
            public void focusLost(FocusEvent e) {
                thousandSeparatorField.setBackground(Color.lightGray);
                thousandSeparatorField.paintImmediately(thousandSeparatorField.getVisibleRect());
            }
        });
        decimalSeparatorField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                decimalSeparatorField.setBackground(Color.white);
                decimalSeparatorField.getCaret().setVisible(false);
                decimalSeparatorField.paintImmediately(decimalSeparatorField.getVisibleRect());
            }
            public void focusLost(FocusEvent e) {
                decimalSeparatorField.setBackground(Color.lightGray);
                decimalSeparatorField.paintImmediately(decimalSeparatorField.getVisibleRect());
            }
        });
        useExponentiationFormatBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (useExponentiationFormatBox.isSelected()) {
                    onlyIntegerBox.setSelected(false);
                    onlyIntegerBox.setEnabled(false);
                    useThousandSeparatorBox.setSelected(false);
                    useThousandSeparatorBox.setEnabled(false);
                    showDecimalSeparatorBox.setEnabled(true);
                }else{
                    useThousandSeparatorBox.setEnabled(true);
                    onlyIntegerBox.setEnabled(true);
                }
            }
        });

        // Bottom blank
        fieldPreferences.add(Box.createGlue());
//        fieldPreferences.add(Box.createVerticalStrut(50));//195));

        /* Add the panel which contains the update-all-dbTables CheckBox.
         */
        JPanel setAllTablesPanel1 = new JPanel();
        setAllTablesPanel1.add(Box.createHorizontalStrut(10));
        tableScopeForFieldPropertiesChangeBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg12"));
//        tableScopeForFieldPropertiesChangeBox.setFont(dbComponent.UIFont);
//1        tableScopeForFieldPropertiesChangeBox.setSelected(((Boolean) inputValues.at(4)).booleanValue());
        if (dbComponent != null)
            setAllTablesPanel1.add(tableScopeForFieldPropertiesChangeBox);
        else
            tableScopeForFieldPropertiesChangeBox.setSelected(true);

        BoxLayout bl7 = new BoxLayout(setAllTablesPanel1, BoxLayout.X_AXIS);
        setAllTablesPanel1.setLayout(bl7);
        setAllTablesPanel1.setMaximumSize(new Dimension(dialogWidth, 15));
        setAllTablesPanel1.setMinimumSize(new Dimension(dialogWidth, 15));
        setAllTablesPanel1.setPreferredSize(new Dimension(dialogWidth, 15));
//        setAllTablesPanel1.setBorder(new LineBorder(Color.black));
        fieldPreferences.add(setAllTablesPanel1);
        fieldPreferences.add(Box.createVerticalStrut(3));


        /* Set the border of the "fieldPreferences" panel to the following TitleBorder.
         */
        final TitledBorder tb1; // = new TitledBorder((String) inputValues.at(0));
        if (!tableScopeForFieldPropertiesChangeBox.isSelected())
            tb1 = new TitledBorder(infoBundle.getString("PreferencesDialogMsg13"));
        else
            tb1 = new TitledBorder(infoBundle.getString("PreferencesDialogMsg14") + activeDBTableTitle + "\"");

        tb1.setTitleColor(titleBorderColor);
        fieldPreferences.setBorder(tb1);
        /* Change the title of the "advancedPreferences" pane, whenever the scope
         * of the changes, declared by the status of "tableScopeForAdvancedPropertiesChangeBox", changes.
         */
        tableScopeForFieldPropertiesChangeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableScopeForFieldPropertiesChangeBox.isSelected())
                    tb1.setTitle(infoBundle.getString("PreferencesDialogMsg14") + activeDBTableTitle + "\"");
                else
                    tb1.setTitle(infoBundle.getString("PreferencesDialogMsg13"));
                fieldPreferences.paintImmediately(fieldPreferences.getVisibleRect());
            }
        });

//        BoxLayout bl2 = new BoxLayout(fieldPreferences, BoxLayout.Y_AXIS);
//        fieldPreferences.setLayout(bl2);
        preferencesTabs.addTab(infoBundle.getString("PreferencesDialogMsg15"), fieldPreferences);


        /*
         * SECOND TAB: Colors
         */
        /* The second tab's main panel.
         */
        final JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new ExplicitLayout());

        // GeneralColorPanel
        /* The general color panel (contains backColorPane, gridColorPanel, selectionColorPanel, highlightColorPanel
           activeRecordColorPanel, selectionForegroundColorPanel.
         */
        JPanel generalColorPanel = new JPanel();
        ExplicitLayout el1 = new ExplicitLayout();
        generalColorPanel.setLayout(el1);

        /* Create the radio button group.
         */
        ButtonGroup buttonGroup2 = new ButtonGroup();

        Expression vGapExp = MathEF.constant(5);
        Expression hGapExp = MathEF.constant(0);

        // The background color panel
        ColorRBComboPanel backColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg16"), infoBundle.getString("PreferencesDialogMsg48"), buttonGroup2);
        backColorBox = backColorPanel.getColorBox();
        ExplicitConstraints ec1 = new ExplicitConstraints(backColorPanel);
        ec1.setOriginX(ExplicitConstraints.RIGHT);
        ec1.setX(ContainerEF.widthFraction(generalColorPanel, 0.5).subtract(hGapExp.divide(2)));
        ec1.setY(ContainerEF.top(generalColorPanel));
//        ec1.setWidth(ContainerEF.widthFraction(0.5).subtract(hGapExp.divide(2)));
        generalColorPanel.add(backColorPanel, ec1);

        // The grid color panel
        ColorRBComboPanel gridColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg17"), infoBundle.getString("PreferencesDialogMsg49"), buttonGroup2);
        gridColorBox = gridColorPanel.getColorBox();
        ExplicitConstraints ec7 = new ExplicitConstraints(gridColorPanel);
        ec7.setOriginX(ExplicitConstraints.RIGHT);
        ec7.setX(ContainerEF.right(generalColorPanel));
        ec7.setY(ContainerEF.top(generalColorPanel));
        generalColorPanel.add(gridColorPanel, ec7);

        // The selection background color panel
        ColorRBComboPanel selectionColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg18"), infoBundle.getString("PreferencesDialogMsg50"), buttonGroup2);
        selectionColorBox = selectionColorPanel.getColorBox();
        ExplicitConstraints ec2 = new ExplicitConstraints(selectionColorPanel);
        ec2.setOriginX(ExplicitConstraints.RIGHT);
        ec2.setX(ComponentEF.right(backColorPanel));
        ec2.setY(ComponentEF.bottom(backColorPanel).add(vGapExp));
        generalColorPanel.add(selectionColorPanel, ec2);

        // The selection foreground color panel
        ColorRBComboPanel selectionForegroundColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg62"), infoBundle.getString("PreferencesDialogMsg63"), buttonGroup2);
        selectionForegroundColorBox = selectionForegroundColorPanel.getColorBox();
        ExplicitConstraints ec20 = new ExplicitConstraints(selectionForegroundColorPanel);
        ec20.setOriginX(ExplicitConstraints.RIGHT);
        ec20.setX(ComponentEF.right(gridColorPanel));
        ec20.setY(ComponentEF.bottom(gridColorPanel).add(vGapExp));
        generalColorPanel.add(selectionForegroundColorPanel, ec20);

        // The selection foreground color panel
        ColorRBComboPanel activeRecordColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg64"), infoBundle.getString("PreferencesDialogMsg65"), buttonGroup2);
        activeRecordColorBox = activeRecordColorPanel.getColorBox();
        ExplicitConstraints ec21 = new ExplicitConstraints(activeRecordColorPanel);
        ec21.setOriginX(ExplicitConstraints.RIGHT);
        ec21.setX(ComponentEF.right(backColorPanel));
        ec21.setY(ComponentEF.bottom(selectionColorPanel).add(vGapExp));
        generalColorPanel.add(activeRecordColorPanel, ec21);

        // The highlight (non-editable fields) color panel
        ColorRBComboPanel highlightColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg19"), infoBundle.getString("PreferencesDialogMsg51"), buttonGroup2);
        highlightColorBox = highlightColorPanel.getColorBox();
        ExplicitConstraints ec8 = new ExplicitConstraints(highlightColorPanel);
        ec8.setOriginX(ExplicitConstraints.RIGHT);
        ec8.setX(ComponentEF.right(gridColorPanel));
//        ec8.setX(ComponentEF.left(gridColorPanel));
        ec8.setY(ComponentEF.bottom(selectionForegroundColorPanel).add(vGapExp));
//        ec8.setWidth(ContainerEF.widthFraction(0.5).subtract(hGapExp.divide(2)));
        generalColorPanel.add(highlightColorPanel, ec8);

        /* Add "generalColorPanel" to "colorPanel".
         */
        TitledBorder tb2 = new TitledBorder(new LineBorder(Color.darkGray), infoBundle.getString("PreferencesDialogMsg20"));
        tb2.setTitleColor(titleBorderColor);
        generalColorPanel.setBorder(tb2);
//        generalColorPanel.setAlignmentX(LEFT_ALIGNMENT);
        ExplicitConstraints ec12 = new ExplicitConstraints(generalColorPanel);
        ec12.setX(ContainerEF.left(colorPanel));
        ec12.setY(ContainerEF.top(colorPanel));
        ec12.setWidth(ContainerEF.right(colorPanel).subtract(ContainerEF.left(colorPanel)));
        Insets borderInsets = new Insets(0, 0, 0, 0);
        borderInsets = tb2.getBorderInsets(generalColorPanel, borderInsets);
        ec12.setHeight(GroupEF.preferredHeightSum(new Component[] {backColorPanel, selectionColorPanel, activeRecordColorPanel}).add(vGapExp.multiply(3)).add(borderInsets.top + borderInsets.bottom));
        colorPanel.add(generalColorPanel, ec12);


        // FontColorPanel
        /* The data type font color panel.
         */
        JPanel fontColorPanel = new JPanel();
        ExplicitLayout fontPanelLayout = new ExplicitLayout();
        fontColorPanel.setLayout(fontPanelLayout);
        TitledBorder tb3 = new TitledBorder(new LineBorder(Color.darkGray), infoBundle.getString("PreferencesDialogMsg27"));
        tb3.setTitleColor(titleBorderColor);
        fontColorPanel.setBorder(tb3);

        /* Create a Panel for the Integer Color.
         */
        ColorRBComboPanel integerColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg21"), infoBundle.getString("PreferencesDialogMsg52"), buttonGroup2);
        integerColorBox = integerColorPanel.getColorBox();
        ExplicitConstraints ec3 = new ExplicitConstraints(integerColorPanel);
        ec3.setOriginX(ExplicitConstraints.RIGHT);
        ec3.setX(ContainerEF.widthFraction(fontColorPanel, 0.5).subtract(hGapExp.divide(2)));
        ec3.setY(ContainerEF.top(fontColorPanel));
        fontColorPanel.add(integerColorPanel, ec3);

        /* Create a Panel for the Double Color.
         */
        ColorRBComboPanel doubleColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg22"), infoBundle.getString("PreferencesDialogMsg53"), buttonGroup2);
        doubleColorBox = doubleColorPanel.getColorBox();
        ExplicitConstraints ec9 = new ExplicitConstraints(doubleColorPanel);
        ec9.setOriginX(ExplicitConstraints.RIGHT);
        ec9.setX(ContainerEF.right(fontColorPanel));
        ec9.setY(ContainerEF.top(fontColorPanel));
        fontColorPanel.add(doubleColorPanel, ec9);

        /* Create a Panel for the Float Color.
         */
        ColorRBComboPanel floatColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg60"), infoBundle.getString("PreferencesDialogMsg61"), buttonGroup2);
        floatColorBox = floatColorPanel.getColorBox();
        ExplicitConstraints ec16 = new ExplicitConstraints(floatColorPanel);
        ec16.setOriginX(ExplicitConstraints.RIGHT);
        ec16.setX(ComponentEF.right(integerColorPanel));
        ec16.setY(ComponentEF.bottom(integerColorPanel).add(vGapExp));
        fontColorPanel.add(floatColorPanel, ec16);

        ColorRBComboPanel stringColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg59"), infoBundle.getString("PreferencesDialogMsg58"), buttonGroup2);
        stringColorBox = stringColorPanel.getColorBox();
        ExplicitConstraints ec4 = new ExplicitConstraints(stringColorPanel);
        ec4.setOriginX(ExplicitConstraints.RIGHT);
        ec4.setX(ComponentEF.right(doubleColorPanel));
        ec4.setY(ComponentEF.bottom(doubleColorPanel).add(vGapExp));
        fontColorPanel.add(stringColorPanel, ec4);

        /* Create a Panel for the Time Color.
         */
        ColorRBComboPanel timeColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg25"), infoBundle.getString("PreferencesDialogMsg56"), buttonGroup2);
        timeColorBox = timeColorPanel.getColorBox();
        ExplicitConstraints ec11 = new ExplicitConstraints(timeColorPanel);
        ec11.setOriginX(ExplicitConstraints.RIGHT);
        ec11.setX(ComponentEF.right(integerColorPanel));
        ec11.setY(ComponentEF.bottom(floatColorPanel).add(vGapExp));
        fontColorPanel.add(timeColorPanel, ec11);

        /* Create a Panel for the Date Color.
         */
        ColorRBComboPanel dateColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg24"), infoBundle.getString("PreferencesDialogMsg55"), buttonGroup2);
        dateColorBox = dateColorPanel.getColorBox();
        ExplicitConstraints ec5 = new ExplicitConstraints(dateColorPanel);
        ec5.setOriginX(ExplicitConstraints.RIGHT);
        ec5.setX(ComponentEF.right(doubleColorPanel));
        ec5.setY(ComponentEF.bottom(stringColorPanel).add(vGapExp));
        fontColorPanel.add(dateColorPanel, ec5);

        /* Create a Panel for the URL Color.
         */
        ColorRBComboPanel urlColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg26"), infoBundle.getString("PreferencesDialogMsg57"), buttonGroup2);
        urlColorBox = urlColorPanel.getColorBox();
        ExplicitConstraints ec6 = new ExplicitConstraints(urlColorPanel);
        ec6.setOriginX(ExplicitConstraints.RIGHT);
        ec6.setX(ComponentEF.right(integerColorPanel));
        ec6.setY(ComponentEF.bottom(dateColorPanel).add(vGapExp));
        fontColorPanel.add(urlColorPanel, ec6);

        ColorRBComboPanel booleanColorPanel = new ColorRBComboPanel(this, infoBundle.getString("PreferencesDialogMsg23"), infoBundle.getString("PreferencesDialogMsg54"), buttonGroup2);
        booleanColorBox = booleanColorPanel.getColorBox();
        ExplicitConstraints ec10 = new ExplicitConstraints(booleanColorPanel);
        ec10.setOriginX(ExplicitConstraints.RIGHT);
        ec10.setX(ComponentEF.right(doubleColorPanel));
        ec10.setY(ComponentEF.bottom(timeColorPanel).add(vGapExp));
        fontColorPanel.add(booleanColorPanel, ec10);

        /* Add the "fontColorPanel" to the "colorPanel".
         */
        ExplicitConstraints ec13 = new ExplicitConstraints(fontColorPanel);
        ec13.setX(ContainerEF.left(colorPanel));
        ec13.setY(ComponentEF.bottom(generalColorPanel).add(vGapExp.multiply(2)));
        ec13.setWidth(ContainerEF.right(colorPanel).subtract(ContainerEF.left(colorPanel)));
        borderInsets =tb3.getBorderInsets(fontColorPanel, borderInsets);
        ec13.setHeight(GroupEF.preferredHeightSum(new Component[] {integerColorPanel, floatColorPanel, dateColorPanel, urlColorPanel}).add(vGapExp.multiply(4)).add(borderInsets.top + borderInsets.bottom));
        colorPanel.add(fontColorPanel, ec13);

        // ChooseColor button
        Color color128 = new Color(0, 0, 128);
        chooseColor = new JButton(infoBundle.getString("PreferencesDialogMsg28"));
        chooseColor.setMaximumSize(new Dimension(110, 30));
        chooseColor.setMinimumSize(new Dimension(110, 30));
        chooseColor.setPreferredSize(new Dimension(110, 30));
        chooseColor.setMargin(new Insets(0,0,0,0));
        chooseColor.setForeground(color128);
        chooseColor.setFocusPainted(true);
        chooseColor.setEnabled(false);
        chooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (activeComboBox != null)
                    activeComboBox.hidePopup();
//                System.out.println(((ColoredSquare) activeComboBox.getSelectedItem()).getColor());
        		Color color = JColorChooser.showDialog(new JFrame(), infoBundle.getString("PreferencesDialogMsg29"), ((ColoredSquare) activeComboBox.getSelectedItem()).getColor());
        		if (color != null) {
        		    ColoredSquare cq1 = new ColoredSquare(color);
        		    if (activeComboBox.getItemCount() == NUM_PREDEFINED_COLORS) {
        		        activeComboBox.addItem(cq1);
        		        activeComboBox.setSelectedItem(cq1);
        		    }else{
        		        activeComboBox.removeItemAt(activeComboBox.getItemCount()-1);
        		        activeComboBox.addItem(cq1);
        		        activeComboBox.setSelectedItem(cq1);
        		    }
        		}
        	}
        });

        /* Add "chooseColor" to the "colorPanel".
         */
        ExplicitConstraints ec14 = new ExplicitConstraints(chooseColor);
        ec14.setOriginX(ExplicitConstraints.CENTER);
        ec14.setX(ContainerEF.widthFraction(colorPanel, 0.5));
        ec14.setY(ComponentEF.bottom(fontColorPanel).add(2));
        colorPanel.add(chooseColor, ec14);

        tableScopeForColorPropertiesChangeBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg12"));
        ExplicitConstraints ec15 = new ExplicitConstraints(tableScopeForColorPropertiesChangeBox);
        ec15.setX(ContainerEF.left(colorPanel).add(10));
        ec15.setOriginY(ExplicitConstraints.BOTTOM);
        ec15.setY(ContainerEF.bottom(colorPanel).add(1));
        if (dbComponent != null)
            colorPanel.add(tableScopeForColorPropertiesChangeBox, ec15);
        else
            tableScopeForColorPropertiesChangeBox.setSelected(true);

        final TitledBorder tb4; //= new TitledBorder((String) PreferencesDialog.inputValues.at(5));
        if (!tableScopeForColorPropertiesChangeBox.isSelected())
            tb4 = new TitledBorder(infoBundle.getString("PreferencesDialogMsg30"));
        else
            tb4 = new TitledBorder(infoBundle.getString("PreferencesDialogMsg31") + activeDBTableTitle + "\"");

        tb4.setTitleColor(titleBorderColor);
        colorPanel.setBorder(tb4);
        /* Change the title of the "advancedPreferences" pane, whenever the scope
         * of the changes, declared by the status of "tableScopeForAdvancedPropertiesChangeBox", changes.
         */
        tableScopeForColorPropertiesChangeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableScopeForColorPropertiesChangeBox.isSelected())
                    tb4.setTitle(infoBundle.getString("PreferencesDialogMsg31") + activeDBTableTitle + "\"");
                else
                    tb4.setTitle(infoBundle.getString("PreferencesDialogMsg30"));
                colorPanel.paintImmediately(colorPanel.getVisibleRect());
            }
        });

        /* Add the "colorPanel" to the "preferencesTabs" TabbedPane.
         */
        preferencesTabs.addTab(infoBundle.getString("PreferencesDialogMsg32"), colorPanel);

        /*
         * THIRD TAB: Advanced preferences.
         */
        /* The container Panel
         */
        final JPanel advancedPreferences = new JPanel(true);
        BoxLayout bl17 = new BoxLayout(advancedPreferences, BoxLayout.Y_AXIS);
        advancedPreferences.setLayout(bl17);
        advancedPreferences.add(Box.createVerticalStrut(10));

        /* Create the "allFontPanel" with BoxLayout. It contains the "fontPanel" and
         * the "fontDemoPanel"
         */
        JPanel allFontPanel = new JPanel(true);
        BoxLayout bl21 = new BoxLayout(allFontPanel, BoxLayout.Y_AXIS);
        allFontPanel.setLayout(bl21);

        /* The "fontPanel" with BoxLayout.
         */
        JPanel  fontPanel = new JPanel(true);
        BoxLayout bl18 = new BoxLayout(fontPanel, BoxLayout.X_AXIS);
        fontPanel.setLayout(bl18);

        /* Initialize "currentFont", so that the font UI elements will be also initialized.
         */
//1        currentFont = (Font) inputValues.at(19);
        /* Create the fontTypePanel with FlowLayout.
         */
        JPanel fontTypePanel = new JPanel(true);
        BoxLayout bl30 = new BoxLayout(fontTypePanel, BoxLayout.X_AXIS);
        fontTypePanel.setLayout(bl30);

        fontType = new JComboBox();
        //String[] fontNames = toolkit.getFontList();
        String[] fontNames =  GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i=0; i<fontNames.length; i++)
            fontType.addItem(fontNames[i]);

//1        fontType.setSelectedItem(currentFont.getName());

        fontType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String fontName = (String) fontType.getSelectedItem();
                tableFont = new Font(fontName, tableFont.getStyle(), tableFont.getSize());
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(tableFont);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });

        fontType.setPreferredSize(new Dimension(100, 21));
        JLabel lb = new JLabel(infoBundle.getString("PreferencesDialogMsg33"));
//1        lb.setFont(dialogFont);
        int lbWidth = dialogFontMetrics.stringWidth(infoBundle.getString("PreferencesDialogMsg33"));
        fontTypePanel.add(lb);
        fontTypePanel.add(Box.createHorizontalStrut(5));
        fontTypePanel.add(fontType);
        fontTypePanel.setMaximumSize(new Dimension(100+5+lbWidth, 40));
        fontTypePanel.setPreferredSize(new Dimension(100+5+lbWidth, 40));
        fontTypePanel.setMinimumSize(new Dimension(100+5+lbWidth, 40));

        /* Create the "fontSize" panel with FlowLayout.
         */
        JPanel fontSizePanel = new JPanel(true);
        BoxLayout bl31 = new BoxLayout(fontSizePanel, BoxLayout.X_AXIS);
        fontSizePanel.setLayout(bl31);

        lb = new JLabel(infoBundle.getString("PreferencesDialogMsg34"));
//1        lb.setFont(dialogFont);
        lbWidth = dialogFontMetrics.stringWidth(infoBundle.getString("PreferencesDialogMsg34"));
        fontSizePanel.add(lb);
        fontSizePanel.add(Box.createHorizontalStrut(5));
        fontSize = new JComboBox();
        fontSize.addItem("7");
        fontSize.addItem("8");
        fontSize.addItem("9");
        fontSize.addItem("10");
        fontSize.addItem("12");
        fontSize.addItem("14");
        fontSize.addItem("16");
        fontSize.addItem("18");
        fontSize.addItem("20");
        fontSize.addItem("24");

//1        fontSize.setSelectedItem(new Integer(currentFont.getSize()).toString());
        fontSize.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int fntSize = new Integer((String) fontSize.getSelectedItem()).intValue();
                tableFont = new Font(tableFont.getName(), tableFont.getStyle(), fntSize);
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(tableFont);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });

        fontSize.setPreferredSize(new Dimension(40, 21));
        fontSizePanel.setMaximumSize(new Dimension(40+lbWidth+5, 40));
        fontSizePanel.setPreferredSize(new Dimension(40+lbWidth+5, 40));
        fontSizePanel.setMinimumSize(new Dimension(40+lbWidth+5, 40));
        fontSizePanel.add(fontSize);

        /* Create the fontPropertyPanel which holds the "Bold" and "Italic" toggle buttons.
         */
        JPanel fontPropertyPanel = new JPanel(true);
        BoxLayout bl32 = new BoxLayout(fontPropertyPanel, BoxLayout.X_AXIS);
        fontPropertyPanel.setLayout(bl32);

        boldButton = new JToggleButton(new ImageIcon(getClass().getResource("images/bold.gif")), false);
        italicButton = new JToggleButton(new ImageIcon(getClass().getResource("images/italic.gif")), false);

        /* Initialize the state of the toggle buttons, depending on the "currentFont".
         */
/*1        if (currentFont.getStyle() == Font.BOLD)
            boldButton.setSelected(true);
        if (currentFont.getStyle() == Font.ITALIC)
            italicButton.setSelected(true);
        if (currentFont.getStyle() == (Font.BOLD + Font.ITALIC)) {
            boldButton.setSelected(true);
            italicButton.setSelected(true);
        }
1*/
        boldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int style = Font.PLAIN;
                if (boldButton.isSelected())
                    style = style + Font.BOLD;
                if (italicButton.isSelected())
                    style = style + Font.ITALIC;
                tableFont = new Font(tableFont.getName(), style, tableFont.getSize());
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(tableFont);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });
        italicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int style = Font.PLAIN;
                if (boldButton.isSelected())
                    style = style + Font.BOLD;
                if (italicButton.isSelected())
                    style = style + Font.ITALIC;
                tableFont = new Font(tableFont.getName(), style, tableFont.getSize());
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(tableFont);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });


        boldButton.setPreferredSize(new Dimension(53, 25));
        italicButton.setPreferredSize(new Dimension(53, 25));
        boldButton.setAlignmentY(CENTER_ALIGNMENT);
        italicButton.setAlignmentY(CENTER_ALIGNMENT);
        fontPropertyPanel.add(boldButton);
        fontPropertyPanel.add(Box.createHorizontalStrut(5));
        fontPropertyPanel.add(italicButton);
        fontPropertyPanel.setMaximumSize(new Dimension(130, 40));
        fontPropertyPanel.setMinimumSize(new Dimension(130, 40));
        fontPropertyPanel.setPreferredSize(new Dimension(130, 40));

        /* Add "fontTypePanel" and "fontSizePanel" to the "fontPanel".
         */
//        fontPanel.add(Box.createHorizontalStrut(5));
        fontPanel.add(Box.createGlue());
        fontPanel.add(fontTypePanel);
        fontPanel.add(Box.createHorizontalStrut(10));
        fontPanel.add(fontSizePanel);
        fontPanel.add(Box.createHorizontalStrut(10));
        fontPanel.add(fontPropertyPanel);
        fontPanel.add(Box.createGlue());
        fontPanel.setAlignmentY(CENTER_ALIGNMENT);
//        fontPanel.setAlignmentX(CENTER_ALIGNMENT);

        fontPanel.setMaximumSize(new Dimension(dialogWidth, 35));
        fontPanel.setMinimumSize(new Dimension(dialogWidth, 35));
        fontPanel.setPreferredSize(new Dimension(dialogWidth, 35));
        allFontPanel.add(fontPanel);

        /* Create the demo Panel which displays an example of the active font.
         */
        JPanel fontDemoPanel = new JPanel(true);
        fontDemo = new JLabel(infoBundle.getString("PreferencesDialogMsg35"));
        fontDemo.setFont(tableFont);
        fontDemo.setPreferredSize(new Dimension(dialogWidth-25, 35)); //350
        fontDemo.setMaximumSize(new Dimension(dialogWidth-25, 35));
        fontDemo.setMinimumSize(new Dimension(dialogWidth-25, 35));
        fontDemo.setHorizontalAlignment(SwingConstants.CENTER);
        fontDemo.setAlignmentX(CENTER_ALIGNMENT);
        fontDemo.setAlignmentY(CENTER_ALIGNMENT);

        fontDemoPanel.add(fontDemo);
//        fontDemo.setAlignmentX(CENTER_ALIGNMENT);
//        fontDemo.setAlignmentY(CENTER_ALIGNMENT);
//        fontDemoPanel.setMinimumSize(new Dimension(370, 35));
//        fontDemoPanel.setMaximumSize(new Dimension(370, 35));
        fontDemoPanel.setMaximumSize(new Dimension(dialogWidth-20, 35));
        fontDemoPanel.setMinimumSize(new Dimension(dialogWidth-20, 35));
        fontDemoPanel.setPreferredSize(new Dimension(dialogWidth-20, 35));
        allFontPanel.add(fontDemoPanel);

        allFontPanel.setMaximumSize(new Dimension(dialogWidth, 100));
        TitledBorder tb5 = new TitledBorder(new LineBorder(Color.darkGray), infoBundle.getString("PreferencesDialogMsg36"));
        tb5.setTitleColor(titleBorderColor);
        allFontPanel.setBorder(tb5);
        advancedPreferences.add(allFontPanel);
        advancedPreferences.add(Box.createVerticalStrut(15));

        /* Create the "generalPrefPanel" with BoxLayout, which contains panels:
         * "rowHeightPanel", "displayGridPanel", "simultaneousRowColumnSelectionPanel" and
         * "expandedHeaderPanel".
         */
        JPanel generalPrefPanel = new JPanel(true);
        BoxLayout bl20 = new BoxLayout(generalPrefPanel, BoxLayout.Y_AXIS);
        generalPrefPanel.setLayout(bl20);

        generalPrefPanel.add(Box.createVerticalStrut(5));

        /* Create the rowHeightPanel with FlowLayout.
         */
        JPanel rowHeightPanel = new JPanel(true);
        rowHeightPanel.add(Box.createHorizontalStrut(10));
        rowHeightPanel.add(new JLabel(infoBundle.getString("PreferencesDialogMsg37")));
        rowHeightPanel.add(Box.createHorizontalStrut(4));
        rowHeightField = new JTextField() {
            public Dimension getMinimumSize() {
                Dimension dim = super.getMinimumSize();
                dim.width = 35;
                return dim;
            }
        };
        rowHeightField.setHorizontalAlignment(SwingConstants.RIGHT);
        rowHeightField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//1                    rowHeightField.setText(((Integer) inputValues.at(20)).toString());
                    rowHeightField.setText(new Integer(lastValidRowHeight).toString());
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    evaluateRowHeightField();
//                    displayHorizontalLinesBox.requestFocus();

            }
        });
        rowHeightField.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
//                System.out.println("messageDelivered: " + messageDelivered);
                evaluateRowHeightField();
            }
            public void focusGained(FocusEvent e) {
                rowHeightField.selectAll();
            }
        });

        rowHeightField.setToolTipText(infoBundle.getString("PreferencesDialogMsg38"));

        rowHeightPanel.add(rowHeightField);
        rowHeightPanel.add(Box.createHorizontalStrut(280));

        BoxLayout bl25 = new BoxLayout(rowHeightPanel, BoxLayout.X_AXIS);
        rowHeightPanel.setLayout(bl25);
        rowHeightPanel.setMaximumSize(new Dimension(dialogWidth, 15));

        /* Add the "rowHeightPanel" to the "generalPrefPanel" panel.
         */
        generalPrefPanel.add(rowHeightPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));

        Dimension d2 = new Dimension(dialogWidth, 15);

        /* Create the "horLinesPanel".
         */
        JPanel horLinesPanel = new JPanel(true);
        horLinesPanel.add(Box.createHorizontalStrut(10));
        displayHorizontalLinesBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg39"));
//        displayHorizontalLinesBox.setFont(dbComponent.UIFont);
//1        displayHorizontalLinesBox.setSelected(((Boolean) inputValues.at(21)).booleanValue());
        horLinesPanel.add(displayHorizontalLinesBox);

        BoxLayout bl27 = new BoxLayout(horLinesPanel, BoxLayout.X_AXIS);
        horLinesPanel.setLayout(bl27);
        horLinesPanel.setMaximumSize(d2); //new Dimension(dialogWidth, 15));
        horLinesPanel.setMinimumSize(d2);
        horLinesPanel.setPreferredSize(d2);
        generalPrefPanel.add(horLinesPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));

        /* Create the "vertLinesPanel".
         */
        JPanel vertLinesPanel = new JPanel(true);
        vertLinesPanel.add(Box.createHorizontalStrut(10));
        displayVerticalLinesBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg40"));
//        displayVerticalLinesBox.setFont(dbComponent.UIFont);
//        displayVerticalLinesBox.setSelected(((Boolean) inputValues.at(22)).booleanValue());
        vertLinesPanel.add(displayVerticalLinesBox);

        BoxLayout bl28 = new BoxLayout(vertLinesPanel, BoxLayout.X_AXIS);
        vertLinesPanel.setLayout(bl28);
        vertLinesPanel.setMaximumSize(d2); //new Dimension(dialogWidth, 15));
        vertLinesPanel.setMinimumSize(d2);
        vertLinesPanel.setPreferredSize(d2);
        generalPrefPanel.add(vertLinesPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));


        /* Create the "displayGridPanel".
         */
/*        JPanel displayGridPanel = new JPanel(true);
//        displayGridPanel.add(Box.createHorizontalStrut(10));
        displayHorizontalLinesBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg39"));
        displayHorizontalLinesBox.setSelected(((Boolean) inputValues.at(21)).booleanValue());
        displayVerticalLinesBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg40"));
        displayVerticalLinesBox.setSelected(((Boolean) inputValues.at(22)).booleanValue());
        displayGridPanel.add(displayHorizontalLinesBox);
//        displayGridPanel.add(Box.createVerticalStrut(5));
        displayGridPanel.add(displayVerticalLinesBox);

        BoxLayout bl24 = new BoxLayout(displayGridPanel, BoxLayout.Y_AXIS);
        displayGridPanel.setLayout(bl24);
        displayGridPanel.setMaximumSize(d2); //new Dimension(dialogWidth, 15));
        displayGridPanel.setMinimumSize(d2);
        displayGridPanel.setPreferredSize(d2);
        generalPrefPanel.add(displayGridPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));
*/
        /* Create the "simultaneousRowColumnSelectionPanel".
         */
        JPanel simultaneousRowColumnSelectionPanel = new JPanel(true);
        simultaneousRowColumnSelectionPanel.add(Box.createHorizontalStrut(10));
        simultaneousRowColumnActivationBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg41"));
//        simultaneousRowColumnActivationBox.setFont(dbComponent.UIFont);
//1        simultaneousRowColumnActivationBox.setSelected(((Boolean) inputValues.at(23)).booleanValue());
        simultaneousRowColumnSelectionPanel.add(simultaneousRowColumnActivationBox);

        BoxLayout bl23 = new BoxLayout(simultaneousRowColumnSelectionPanel, BoxLayout.X_AXIS);
        simultaneousRowColumnSelectionPanel.setLayout(bl23);
        simultaneousRowColumnSelectionPanel.setMaximumSize(d2); //new Dimension(dialogWidth, 15));
        simultaneousRowColumnSelectionPanel.setMinimumSize(d2);
        simultaneousRowColumnSelectionPanel.setPreferredSize(d2);
        generalPrefPanel.add(simultaneousRowColumnSelectionPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));

        /* Create the "expandedHeaderPanel".
         */
        JPanel expandedHeaderPanel = new JPanel(true);
        expandedHeaderBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg42"));
//        expandedHeaderBox.setFont(dbComponent.UIFont);
//1        expandedHeaderBox.setSelected(((Boolean) inputValues.at(24)).booleanValue());
        expandedHeaderPanel.add(Box.createHorizontalStrut(10));
        expandedHeaderPanel.add(expandedHeaderBox);

        BoxLayout bl22 = new BoxLayout(expandedHeaderPanel, BoxLayout.X_AXIS);
        expandedHeaderPanel.setLayout(bl22);
        expandedHeaderPanel.setMaximumSize(d2); //new Dimension(dialogWidth, 15));
        expandedHeaderPanel.setMinimumSize(d2);
        expandedHeaderPanel.setPreferredSize(d2);
        generalPrefPanel.add(expandedHeaderPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));

        /* Create the "highlightNonEditableFieldsPanel".
         */
        JPanel highlightNonEditableFieldsPanel = new JPanel(true);
        highlightNonEditableFieldsBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg43"));
//        highlightNonEditableFieldsBox.setFont(dbComponent.UIFont);
//1        highlightNonEditableFieldsBox.setSelected(((Boolean) inputValues.at(1)).booleanValue());
        highlightNonEditableFieldsPanel.add(Box.createHorizontalStrut(10));
        highlightNonEditableFieldsPanel.add(highlightNonEditableFieldsBox);

        BoxLayout bl26 = new BoxLayout(highlightNonEditableFieldsPanel, BoxLayout.X_AXIS);
        highlightNonEditableFieldsPanel.setLayout(bl26);
        highlightNonEditableFieldsPanel.setMaximumSize(d2); //new Dimension(dialogWidth, 15));
        highlightNonEditableFieldsPanel.setMinimumSize(d2);
        highlightNonEditableFieldsPanel.setPreferredSize(d2);
        generalPrefPanel.add(highlightNonEditableFieldsPanel);
        generalPrefPanel.add(Box.createVerticalStrut(10));

        /* Add "generalPrefPanel" to "advancedPreferences" panel.
         */
        generalPrefPanel.setMaximumSize(new Dimension(dialogWidth, 180)); //155
        generalPrefPanel.setMinimumSize(new Dimension(dialogWidth, 180));
        generalPrefPanel.setPreferredSize(new Dimension(dialogWidth, 180));
        TitledBorder tb6 = new TitledBorder(new LineBorder(Color.darkGray), infoBundle.getString("PreferencesDialogMsg44"));
        tb6.setTitleColor(titleBorderColor);
        generalPrefPanel.setBorder(tb6);
        advancedPreferences.add(generalPrefPanel);
        //Bottom blank
        advancedPreferences.add(Box.createGlue());
//        advancedPreferences.add(Box.createVerticalStrut(52)); //52));

        /* Add the panel which contains the update-all-dbTables CheckBox.
         */
        JPanel setAllTablesPanel3 = new JPanel(true);
        setAllTablesPanel3.add(Box.createHorizontalStrut(10));
        tableScopeForAdvancedPropertiesChangeBox = new JCheckBox(infoBundle.getString("PreferencesDialogMsg12"));
//        tableScopeForAdvancedPropertiesChangeBox.setFont(dbComponent.UIFont);
//        tableScopeForAdvancedPropertiesChangeBox.setSelected(((Boolean) inputValues.at(25)).booleanValue());
        if (dbComponent != null)
            setAllTablesPanel3.add(tableScopeForAdvancedPropertiesChangeBox);
        else
            tableScopeForAdvancedPropertiesChangeBox.setSelected(true);

        BoxLayout bl19 = new BoxLayout(setAllTablesPanel3, BoxLayout.X_AXIS);
        setAllTablesPanel3.setLayout(bl19);
        setAllTablesPanel3.setMaximumSize(new Dimension(dialogWidth, 15));
        setAllTablesPanel3.setMinimumSize(new Dimension(dialogWidth, 15));
        setAllTablesPanel3.setPreferredSize(new Dimension(dialogWidth, 15));
//        setAllTablesPanel3.setBorder(new LineBorder(Color.black));
        advancedPreferences.add(setAllTablesPanel3);
        advancedPreferences.add(Box.createVerticalStrut(3));

        final TitledBorder tb7;
        if (!tableScopeForAdvancedPropertiesChangeBox.isSelected())
            tb7 = new TitledBorder(infoBundle.getString("PreferencesDialogMsg45"));
        else
            tb7 = new TitledBorder(infoBundle.getString("PreferencesDialogMsg46") + activeDBTableTitle + "\"");
        tb7.setTitleColor(titleBorderColor);
        advancedPreferences.setBorder(tb7);

        /* Change the title of the "advancedPreferences" pane, whenever the scope
         * of the changes, declared by the status of "tableScopeForAdvancedPropertiesChangeBox", changes.
         */
        tableScopeForAdvancedPropertiesChangeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tableScopeForAdvancedPropertiesChangeBox.isSelected())
                    tb7.setTitle(infoBundle.getString("PreferencesDialogMsg46") + activeDBTableTitle + "\"");
                else
                    tb7.setTitle(infoBundle.getString("PreferencesDialogMsg45"));
                advancedPreferences.paintImmediately(advancedPreferences.getVisibleRect());
            }
        });

        /* Add the "advancedPreferences" panel to the "preferencesTabs" TabbedPane.
         */
        preferencesTabs.addTab(infoBundle.getString("PreferencesDialogMsg47"), advancedPreferences);

        preferencesTabs.setSelectedIndex(0);

        BoxLayout bl5 = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl5);

        /* Add the "preferencesTabs" to the JDialog.
         */
        getContentPane().add(preferencesTabs);
        getContentPane().add(Box.createVerticalStrut(8));

        /* Adding OK and Cancel buttons in a new panel "buttons".
         */
        JPanel buttons = new JPanel(true);
        BoxLayout bl6 = new BoxLayout(buttons, BoxLayout.X_AXIS);
        buttons.setLayout(bl6);
        buttons.setMaximumSize(new Dimension(dialogWidth, 40));
        buttons.setMinimumSize(new Dimension(dialogWidth, 40));
        buttons.setPreferredSize(new Dimension(dialogWidth, 40));

//        ((FlowLayout) buttons.getLayout()).setHgap(15);
        okButton = new JButton(infoBundle.getString("OK"));
        okButton.setMaximumSize(new Dimension(100, 30));
        okButton.setMinimumSize(new Dimension(100, 30));
        okButton.setPreferredSize(new Dimension(100, 30));
//1        okButton.setFont(dialogFont);
        okButton.setForeground(color128);
        okButton.setFocusPainted(true);
        okButton.setSelected(true);
//        okButton.setRequestFocusEnabled(false);
        cancelButton = new JButton(infoBundle.getString("Cancel"));
        cancelButton.setFocusPainted(true);
        cancelButton.setMaximumSize(new Dimension(100, 30));
        cancelButton.setMinimumSize(new Dimension(100, 30));
        cancelButton.setPreferredSize(new Dimension(100, 30));
//1        cancelButton.setFont(dialogFont);
        cancelButton.setForeground(color128);
//        cancelButton.setRequestFocusEnabled(false);

        /* Center the buttons.
         */
        buttons.add(Box.createHorizontalStrut(dialogWidth/2-100-8)); //148
        buttons.add(okButton);
        buttons.add(Box.createHorizontalStrut(15));
        buttons.add(cancelButton);

        /* Adding "buttons" panel to the dialog.
         */
        getContentPane().add(buttons);
        getContentPane().add(Box.createVerticalStrut(7));

        /* Creating the ActionListeners for the two buttons.
         */
        okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!evaluateRowHeightField()) {
//                    System.out.println("1messageDelivered: " + messageDelivered);
                    return;
                }
//                System.out.println("Returning back color: " + ((ColoredSquare) backColorBox.getSelectedItem()).getColor());
                okButton.removeActionListener(okListener);
                cancelButton.removeActionListener(cancelListener);
                removeWindowListener(l);
                returnCode = DIALOG_OK;
/*1                outputValues.clear();
                outputValues.add("");
                outputValues.add(new Boolean(highlightNonEditableFieldsBox.isSelected())); //highlightNonEditable));
                outputValues.add(((String) formatsBox.getSelectedItem()).substring(0, (((String) formatsBox.getSelectedItem()).indexOf('('))-3)); //3 blanks
                outputValues.add(formats2Box.getSelectedItem());
                outputValues.add(new Boolean(tableScopeForFieldPropertiesChangeBox.isSelected()));
                outputValues.add("");
                outputValues.add(((ColoredSquare) backColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) gridColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) selectionColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) highlightColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) integerColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) doubleColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) stringColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) booleanColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) dateColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) timeColorBox.getSelectedItem()).getColor());
                outputValues.add(((ColoredSquare) urlColorBox.getSelectedItem()).getColor());
                outputValues.add(new Boolean(tableScopeForColorPropertiesChangeBox.isSelected()));
                outputValues.add("");
                outputValues.add(currentFont);
                outputValues.add(rowHeightField.getText());
                outputValues.add(new Boolean(displayHorizontalLinesBox.isSelected()));
                outputValues.add(new Boolean(displayVerticalLinesBox.isSelected()));
                outputValues.add(new Boolean(simultaneousRowColumnActivationBox.isSelected()));
                outputValues.add(new Boolean(expandedHeaderBox.isSelected()));
                outputValues.add(new Boolean(tableScopeForAdvancedPropertiesChangeBox.isSelected()));
                outputValues.add(new Boolean(onlyIntegerBox.isSelected()));
                outputValues.add(new Boolean(useExponentiationFormatBox.isSelected()));
                outputValues.add(new Boolean(showDecimalSeparatorBox.isSelected()));
                outputValues.add(decimalSeparatorField.getText().trim());
                outputValues.add(new Boolean(useThousandSeparatorBox.isSelected()));
                outputValues.add(thousandSeparatorField.getText().trim());
1*/
                dispose();
            }
        };
        okButton.addActionListener(okListener);
        cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton.removeActionListener(okListener);
                cancelButton.removeActionListener(cancelListener);
                removeWindowListener(l);
                dispose();
            }
        };
        cancelButton.addActionListener(cancelListener);

        /* Adding a WindowListener to "PreferencesDialog".
         */
    	l = new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
                okButton.removeActionListener(okListener);
                cancelButton.removeActionListener(cancelListener);
                removeWindowListener(l);
	            dispose();
	        }
	    };
        addWindowListener(l);

        ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
//                System.out.println("Received key event " + e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    okButton.removeActionListener(okListener);
                    cancelButton.removeActionListener(cancelListener);
                    removeWindowListener(l);
                    returnCode = DIALOG_OK;
	                dispose();
                }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
                    okButton.removeActionListener(okListener);
                    cancelButton.removeActionListener(cancelListener);
                    removeWindowListener(l);
	                dispose();
                }
            }
        };
        okButton.addKeyListener(ka);

        /* Dispaying the "PreferencesDialog".
         */
        setResizable(false);
//        setModal(true);
        pack();
        setSize(dialogWidth, dialogHeight);
    }

    void showDialog(Frame frame) {
        if (database != null) {
            database.updateNumOfSelectedRecords(database.activeDBTable);
            if (database.statusToolbarController != null && database.statusToolbarController.getToolbar() != null) {
                database.statusToolbarController.setMessageLabelColor(Color.white);
                database.statusToolbarController.getToolbar().paintImmediately(database.statusToolbarController.getToolbar().getBounds());
            }
        }

        Rectangle bounds = null;
        java.awt.Point location = null;
        System.out.println("getLocationOnScreen() 10");
        if (database != null) {
            bounds = database.getBounds();
            location = database.getLocationOnScreen();
        }else{
            bounds = dbTable.getBounds();
            location = dbTable.getLocationOnScreen();
        }
//        System.out.println("dbBounds: " + dbBounds + " location: " + dbComponent.getLocationOnScreen());
        int x = location.x + bounds.width/2 - getSize().width/2;
        int y = location.y + bounds.height/2-getSize().height/2;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (x+getSize().width > screenSize.width)
            x = screenSize.width - getSize().width;
        if (y+getSize().height > screenSize.height)
            y = screenSize.height - getSize().height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        setLocation(x, y);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setVisible(true);
    }

    protected boolean evaluateRowHeightField() {
//        if (messageDelivered) {
//            messageDelivered = false;
//            return false;
//        }

        int i = 0;
        try{
            i = Integer.parseInt(rowHeightField.getText());
        }catch (NumberFormatException ex) {
//            messageDelivered = true;
//            JOptionPane.showMessageDialog(dialogFrame, "Invalid row height \"" + rowHeightField.getText() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
//1            rowHeightField.setText(((Integer) inputValues.at(20)).toString());
            rowHeightField.setText(new Integer(lastValidRowHeight).toString());
            return false;
        }
        if (i<0) {
//            messageDelivered = true;
//            JOptionPane.showMessageDialog(dialogFrame, "Invalid row height \"" + rowHeightField.getText() + "\". Row height cannot have a negative value", "Error", JOptionPane.ERROR_MESSAGE);
//1            rowHeightField.setText(((Integer) inputValues.at(20)).toString());
            rowHeightField.setText(new Integer(lastValidRowHeight).toString());
            return false;
        }
        if (i<7) {
//            messageDelivered = true;
//            JOptionPane.showMessageDialog(dialogFrame, "Invalid row height \"" + rowHeightField.getText() + "\". Row height cannot be less than \"7\"", "Error", JOptionPane.ERROR_MESSAGE);
//1            rowHeightField.setText(((Integer) inputValues.at(20)).toString());
            rowHeightField.setText(new Integer(lastValidRowHeight).toString());
            return false;
        }
        if (i > 299) {
//            messageDelivered = true;
//            JOptionPane.showMessageDialog(dialogFrame, "Invalid row height \"" + rowHeightField.getText() + "\". Row height cannot be greater than \"299\"", "Error", JOptionPane.ERROR_MESSAGE);
//1            rowHeightField.setText(((Integer) inputValues.at(20)).toString());
            rowHeightField.setText(new Integer(lastValidRowHeight).toString());
            return false;
        }

        return true;
    }

    public Color getBackgroundColor() {
        return ((ColoredSquare) backColorBox.getSelectedItem()).getColor();
    }

    public void setBackgroundColor(Color backgroundColor) {
        ColoredSquare cq = new ColoredSquare(backgroundColor);
        backColorBox.setSelectedItem(cq);
        if (backColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) backColorBox.getSelectedItem()).getColor())) {
            backColorBox.addItem(cq);
            backColorBox.setSelectedItem(cq);
        }
    }

    public Color getGridColor() {
        return ((ColoredSquare) gridColorBox.getSelectedItem()).getColor();
    }

    public void setGridColor(Color gridColor) {
        ColoredSquare cq = new ColoredSquare(gridColor);
        gridColorBox.setSelectedItem(cq);
        if (!cq.getColor().equals(((ColoredSquare) gridColorBox.getSelectedItem()).getColor())) {
            gridColorBox.addItem(cq);
            gridColorBox.setSelectedItem(cq);
        }
    }

    public Color getSelectionBackground() {
        return ((ColoredSquare) selectionColorBox.getSelectedItem()).getColor();
    }

    public void setSelectionBackground(Color selectionBackground) {
        ColoredSquare cq = new ColoredSquare(selectionBackground);
        selectionColorBox.setSelectedItem(cq);
        if (selectionColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) selectionColorBox.getSelectedItem()).getColor())) {
            selectionColorBox.addItem(cq);
            selectionColorBox.setSelectedItem(cq);
        }
    }

    public Color getSelectionForeground() {
        return ((ColoredSquare) selectionForegroundColorBox.getSelectedItem()).getColor();
    }

    public void setSelectionForeground(Color selectionForeground) {
        ColoredSquare cq = new ColoredSquare(selectionForeground);
        selectionForegroundColorBox.setSelectedItem(cq);
        if (selectionForegroundColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) selectionForegroundColorBox.getSelectedItem()).getColor())) {
            selectionForegroundColorBox.addItem(cq);
            selectionForegroundColorBox.setSelectedItem(cq);
        }
    }

    public Color getHighlightColor() {
        return ((ColoredSquare) highlightColorBox.getSelectedItem()).getColor();
    }

    public void setHighlightColor(Color highlightColor) {
        ColoredSquare cq = new ColoredSquare(highlightColor);
        highlightColorBox.setSelectedItem(cq);
        if (highlightColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) highlightColorBox.getSelectedItem()).getColor())) {
            highlightColorBox.addItem(cq);
            highlightColorBox.setSelectedItem(cq);
        }
    }

    public Color getIntegerColor() {
        return ((ColoredSquare) integerColorBox.getSelectedItem()).getColor();
    }

    public void setIntegerColor(Color integerColor) {
        ColoredSquare cq = new ColoredSquare(integerColor);
        integerColorBox.setSelectedItem(cq);
        if (integerColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) integerColorBox.getSelectedItem()).getColor())) {
            integerColorBox.addItem(cq);
            integerColorBox.setSelectedItem(cq);
        }
    }

    public Color getDoubleColor() {
        return ((ColoredSquare) doubleColorBox.getSelectedItem()).getColor();
    }

    public void setDoubleColor(Color doubleColor) {
        ColoredSquare cq = new ColoredSquare(doubleColor);
        doubleColorBox.setSelectedItem(cq);
        if (doubleColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) doubleColorBox.getSelectedItem()).getColor())) {
            doubleColorBox.addItem(cq);
            doubleColorBox.setSelectedItem(cq);
        }
    }

    public Color getStringColor() {
        return ((ColoredSquare) stringColorBox.getSelectedItem()).getColor();
    }

    public void setStringColor(Color stringColor) {
        ColoredSquare cq = new ColoredSquare(stringColor);
        stringColorBox.setSelectedItem(cq);
        if (stringColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) stringColorBox.getSelectedItem()).getColor())) {
            stringColorBox.addItem(cq);
            stringColorBox.setSelectedItem(cq);
        }
    }

    public Color getBooleanColor() {
        return ((ColoredSquare) booleanColorBox.getSelectedItem()).getColor();
    }

    public void setBooleanColor(Color booleanColor) {
        ColoredSquare cq = new ColoredSquare(booleanColor);
        booleanColorBox.setSelectedItem(cq);
        if (stringColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) booleanColorBox.getSelectedItem()).getColor())) {
            booleanColorBox.addItem(cq);
            booleanColorBox.setSelectedItem(cq);
        }
    }

    public Color getDateColor() {
        return ((ColoredSquare) dateColorBox.getSelectedItem()).getColor();
    }

    public void setDateColor(Color dateColor) {
        ColoredSquare cq = new ColoredSquare(dateColor);
        dateColorBox.setSelectedItem(cq);
        if (dateColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) dateColorBox.getSelectedItem()).getColor())) {
            dateColorBox.addItem(cq);
            dateColorBox.setSelectedItem(cq);
        }
    }

    public Color getTimeColor() {
        return ((ColoredSquare) timeColorBox.getSelectedItem()).getColor();
    }

    public void setTimeColor(Color timeColor) {
        ColoredSquare cq = new ColoredSquare(timeColor);
        timeColorBox.setSelectedItem(cq);
        if (timeColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) timeColorBox.getSelectedItem()).getColor())) {
            timeColorBox.addItem(cq);
            timeColorBox.setSelectedItem(cq);
        }
    }

    public Color getUrlColor() {
        return ((ColoredSquare) urlColorBox.getSelectedItem()).getColor();
    }

    public void setUrlColor(Color urlColor) {
        ColoredSquare cq = new ColoredSquare(urlColor);
        urlColorBox.setSelectedItem(cq);
        if (urlColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) urlColorBox.getSelectedItem()).getColor())) {
            urlColorBox.addItem(cq);
            urlColorBox.setSelectedItem(cq);
        }
    }

    public Color getFloatColor() {
        return ((ColoredSquare) floatColorBox.getSelectedItem()).getColor();
    }

    public void setFloatColor(Color floatColor) {
        ColoredSquare cq = new ColoredSquare(floatColor);
        floatColorBox.setSelectedItem(cq);
        if (floatColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) floatColorBox.getSelectedItem()).getColor())) {
            floatColorBox.addItem(cq);
            floatColorBox.setSelectedItem(cq);
        }
    }

    public Font getTableFont() {
        return tableFont;
    }

    public void setTableFont(Font tableFont) {
        this.tableFont = tableFont;
        fontType.setSelectedItem(tableFont.getName());
        fontSize.setSelectedItem(new Integer(tableFont.getSize()).toString());
        /* Initialize the state of the toggle buttons, depending on the "currentFont".
         */
        if (tableFont.getStyle() == Font.BOLD)
            boldButton.setSelected(true);
        if (tableFont.getStyle() == Font.ITALIC)
            italicButton.setSelected(true);
        if (tableFont.getStyle() == (Font.BOLD + Font.ITALIC)) {
            boldButton.setSelected(true);
            italicButton.setSelected(true);
        }
    }

    public int getRowHeight() {
        return Integer.valueOf(rowHeightField.getText()).intValue();
//        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        rowHeightField.setText(new Integer(rowHeight).toString());
        lastValidRowHeight = rowHeight;
    }

    public boolean isHorizontalLinesVisible() {
        return displayHorizontalLinesBox.isSelected();
    }

    public void setHorizontalLinesVisible(boolean horizontalLinesVisible) {
        displayHorizontalLinesBox.setSelected(horizontalLinesVisible);
    }

    public boolean isVerticalLinesVisible() {
        return displayVerticalLinesBox.isSelected();
    }

    public void setVerticalLinesVisible(boolean verticalLinesVisible) {
        displayVerticalLinesBox.setSelected(verticalLinesVisible);
    }

    public boolean isSimultaneousFieldRecordActivation() {
        return simultaneousRowColumnActivationBox.isSelected();
    }

    public void setSimultaneousFieldRecordActivation(boolean simultaneousFieldRecordActivation) {
        simultaneousRowColumnActivationBox.setSelected(simultaneousFieldRecordActivation);
    }

    public boolean isHeaderIconsVisible() {
        return expandedHeaderBox.isSelected();
    }

    public void setHeaderIconsVisible(boolean headerIconsVisible) {
        expandedHeaderBox.setSelected(headerIconsVisible);
    }

    public boolean isOnlyIntegerPart() {
        return onlyIntegerBox.isSelected();
    }

    public void setOnlyIntegerPart(boolean onlyIntegerPart) {
        onlyIntegerBox.setSelected(onlyIntegerPart);
    }

    public boolean isExponentiationFormatUsed() {
        return useExponentiationFormatBox.isSelected();
    }

    public void setExponentiationFormatUsed(boolean exponentiationFormatUsed) {
        useExponentiationFormatBox.setSelected(exponentiationFormatUsed);
    }

    public boolean isDecimalSeparatorAlwaysVisible() {
        return showDecimalSeparatorBox.isSelected();
    }

    public void setDecimalSeparatorAlwaysVisible(boolean decimalSeparatorAlwaysVisible) {
        showDecimalSeparatorBox.setSelected(decimalSeparatorAlwaysVisible);
    }

    public String getDecimalSeparator() {
        return decimalSeparatorField.getText();
    }

    public void setDecimalSeparator(String decimalSeparator) {
        if (decimalSeparator.equals(",") || decimalSeparator.equals("."))
            decimalSeparator = decimalSeparator + " ";
        decimalSeparatorField.setText(decimalSeparator);
    }

    public String getThousandSeparator() {
        return thousandSeparatorField.getText();
    }

    public void setThousandSeparator(String thousandSeparator) {
        if (thousandSeparator.equals(",") || thousandSeparator.equals("."))
            thousandSeparator = thousandSeparator + " ";
        thousandSeparatorField.setText(thousandSeparator);
    }

    public boolean isThousandSeparatorVisible() {
        return useThousandSeparatorBox.isSelected();
    }

    public void setThousandSeparatorVisible(boolean thousandSeparatorVisible) {
        useThousandSeparatorBox.setSelected(thousandSeparatorVisible);
    }

    public String getDateFormatStr() {
        return ((String) formatsBox.getSelectedItem()).substring(0, (((String) formatsBox.getSelectedItem()).indexOf('('))-3); //3 blanks;
    }

    public void setDateFormatStr(String dateFormatStr) {
        ArrayList dateFormats = dbTable.table.getDateFormats();
        String[] sampleDates = dbTable.table.getSampleDates();
        int index = dateFormats.indexOf(dateFormatStr);
//        System.out.println(dateFormatStr + b[index]);
        formatsBox.setSelectedItem(dateFormatStr + sampleDates[index]);
    }

    public String getTimeFormatStr() {
        return (String) formats2Box.getSelectedItem();
    }

    public void setTimeFormatStr(String timeFormatStr) {
        /* Setting the displayed item to the current DBTable's time format.
         */
        formats2Box.setSelectedItem(timeFormatStr);
    }

    public boolean isTableScopeForFieldPropertiesChange() {
        return tableScopeForFieldPropertiesChangeBox.isSelected();
    }

    public void setTableScopeForFieldPropertiesChange(boolean tableScopeForFieldPropertiesChange) {
        tableScopeForFieldPropertiesChangeBox.setSelected(tableScopeForFieldPropertiesChange);
    }

    public boolean isTableScopeForColorPropertiesChange() {
        return tableScopeForColorPropertiesChangeBox.isSelected();
    }

    public void setTableScopeForColorPropertiesChange(boolean tableScopeForColorPropertiesChange) {
        tableScopeForColorPropertiesChangeBox.setSelected(tableScopeForColorPropertiesChange);
    }

    public boolean isTableScopeForAdvancedPropertiesChange() {
        return tableScopeForAdvancedPropertiesChangeBox.isSelected();
    }

    public void setTableScopeForAdvancedPropertiesChange(boolean tableScopeForAdvancedPropertiesChange) {
        tableScopeForAdvancedPropertiesChangeBox.setSelected(tableScopeForAdvancedPropertiesChange);
    }

    public boolean isHighlightNonEditable() {
        return highlightNonEditableFieldsBox.isSelected();
    }

    public void setHighlightNonEditable(boolean highlightNonEditable) {
        highlightNonEditableFieldsBox.setSelected(highlightNonEditable);
    }

    public Color getActiveRecordColor() {
        return ((ColoredSquare) activeRecordColorBox.getSelectedItem()).getColor();
    }

    public void setActiveRecordColor(Color activeRecordColor) {
        ColoredSquare cq = new ColoredSquare(activeRecordColor);
        activeRecordColorBox.setSelectedItem(cq);
        if (activeRecordColorBox.getSelectedIndex() == 0 && !cq.getColor().equals(((ColoredSquare) activeRecordColorBox.getSelectedItem()).getColor())) {
            activeRecordColorBox.addItem(cq);
            activeRecordColorBox.setSelectedItem(cq);
        }
    }

    JComboBox createColorComboBox() {
        JComboBox box = new JComboBox();

        box.addItem(ColoredSquare.getWhiteColoredSquare());
        box.addItem(ColoredSquare.getBlackColoredSquare());
        box.addItem(ColoredSquare.getDarkGrayColoredSquare());
        box.addItem(ColoredSquare.getGrayColoredSquare());
        box.addItem(ColoredSquare.getLightGrayColoredSquare());
        box.addItem(ColoredSquare.getColor128ColoredSquare());
        box.addItem(ColoredSquare.getBlueColoredSquare());
        box.addItem(ColoredSquare.getCyanColoredSquare());
        box.addItem(ColoredSquare.getGreenColoredSquare());
        box.addItem(ColoredSquare.getMagentaColoredSquare());
        box.addItem(ColoredSquare.getOrangeColoredSquare());
        box.addItem(ColoredSquare.getPinkColoredSquare());
        box.addItem(ColoredSquare.getRedColoredSquare());
        box.addItem(ColoredSquare.getYellowColoredSquare());

        box.setMaximumSize(new Dimension(65,20));
        box.setPreferredSize(new Dimension(65,20));
        return box;
    }
}

class ColorRBComboPanel extends JPanel {
    JRadioButton rb = null;
    static ImageIcon selectedRadioIcon = null, unselectedRadioIcon = null;
    PreferencesDialog prefDialog = null;
    JComboBox colorBox = null;

    public ColorRBComboPanel(PreferencesDialog dialog, String text, String tip, ButtonGroup butGroup) {
        this.prefDialog = dialog;
        BoxLayout bl58 = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(bl58);
        setAlignmentX(RIGHT_ALIGNMENT);
        rb = new JRadioButton(text, getUnselectedRadioIcon()); //infoBundle.getString("PreferencesDialogMsg24"), rbIcon);
        rb.setSelectedIcon(getSelectedRadioIcon());
        rb.setBorder(new LineBorder(Color.red));
        rb.setAlignmentY(CENTER_ALIGNMENT);
        rb.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                prefDialog.chooseColor.setEnabled(true);
                if (prefDialog.activeComboBox != null)
                    prefDialog.activeComboBox.hidePopup();
                rb.doClick();
                prefDialog.activeComboBox = colorBox;
            }
            public void focusLost(FocusEvent e) {}
        });
        rb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (rb.isSelected())
                    getSelectedRadioIcon().setImageObserver(rb);
            }
        });
        getSelectedRadioIcon().setImageObserver(rb);
        butGroup.add(rb);
        add(rb);
//        rb.setMargin(new Insets(0, 0, 0, 0));
        colorBox = prefDialog.createColorComboBox();
        rb.setToolTipText(tip); //1 infoBundle.getString("PreferencesDialogMsg55"));
        colorBox.setToolTipText(tip); //1 infoBundle.getString("PreferencesDialogMsg55"));

        colorBox.setMaximumSize(new Dimension(63, 20));
        colorBox.setMinimumSize(new Dimension(63, 20));
        colorBox.setPreferredSize(new Dimension(63, 20));
        colorBox.setAlignmentY(CENTER_ALIGNMENT);
        colorBox.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                rb.setSelected(true);
                prefDialog.chooseColor.setEnabled(true);
                prefDialog.activeComboBox = colorBox;
            }
            public void focusLost(FocusEvent e) {}
        });
        add(Box.createHorizontalStrut(3));
        add(colorBox);
    }

    static ImageIcon getUnselectedRadioIcon() {
        if (unselectedRadioIcon == null) {
            unselectedRadioIcon = new ImageIcon(ColorRBComboPanel.class.getResource("images/lightGray.gif"));
            Image unselectedRadioIconImage = unselectedRadioIcon.getImage();
            unselectedRadioIconImage = unselectedRadioIconImage.getScaledInstance(10, 12,Image.SCALE_DEFAULT);
            unselectedRadioIcon.setImage(unselectedRadioIconImage);
        }
        return unselectedRadioIcon;
    }

    static ImageIcon getSelectedRadioIcon() {
        /* Create the ImageIcon used as the Set state of the radio buttons.
         */
        if (selectedRadioIcon == null)
            selectedRadioIcon = new ImageIcon(ColorRBComboPanel.class.getResource("images/animatedbullet.gif"));
        return selectedRadioIcon;
    }

    JComboBox getColorBox() {
        return colorBox;
    }

    JRadioButton getRadioButton() {
        return rb;
    }
}
