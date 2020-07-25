package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.CImageIcon;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Frame;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.ResourceBundle;
import java.util.Enumeration;


class NewFieldDialogNov extends JDialog {
//    int dialogWidth = 482;
//    int dialogHeight = 226;

    public static final int DIALOG_CANCEL = 1;
    public static final int DIALOG_OK = 0;
    NewFieldDialogNov thisDialog;
    JTextField fieldName, formula;
    JCheckBox keyBox, calculatedBox, storeReferencesToImages;
    JComboBox typeBox;
    JButton ok, cancel, clearEntries;
    JPanel AllFieldPropertiesPanel, formulaPanel, fieldPanel, propertiesMore;
    DBTable dbTable;

    int fieldPanelWidth;
    boolean isEditingField;
    boolean isEditedFieldCalculated = false;

//    static int clickedButton = 0;
    static Array inputValues = new Array();
    static Array outputValues = new Array();
    transient ResourceBundle infoBundle;
    int returnValue = DIALOG_CANCEL;

    protected NewFieldDialogNov(Frame frame, DBTable dbTable, boolean editingField, String title) {
        super(frame, true);
        thisDialog = this;
        isEditingField = editingField;
        this.dbTable = dbTable;
        infoBundle = Database.infoBundle;
        //setTitle(infoBundle.getString("NewFieldDialogMsg1"));
        setTitle(title);

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	this.getRootPane().WHEN_IN_FOCUSED_WINDOW);


        Color color128 = new Color(0, 0, 128);
        Color titleBorderColor = new Color(119, 40, 104);
        Color inputTextColor = new Color(18, 102, 99);
        Color inputTextSelColor = Color.black;

        if (editingField && ((Boolean) inputValues.at(2)).booleanValue())
            isEditedFieldCalculated = true;

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        fieldPanel = new JPanel(true);
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));

        JPanel fieldNamePanel = new JPanel(true);
        fieldNamePanel.setLayout(new BorderLayout(5,0));


        String fieldNameString = infoBundle.getString("NewFieldDialogMsg2");
        String fieldTypeString = infoBundle.getString("NewTableDialogMsg3");
        String keyString = infoBundle.getString("NewFieldDialogMsg3");
        String calculatedString = infoBundle.getString("NewFieldDialogMsg6");
        String formulaString = infoBundle.getString("NewFieldDialogMsg12");

//        Font bigFont, labelFont;
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) {
//            bigFont = new Font("Dialog", Font.PLAIN, 14);
//            labelFont = new Font("Dialog", Font.PLAIN, 12);
//        }else{
//            bigFont = new Font("Helvetica", Font.PLAIN, 14);
//            labelFont = new Font("Helvetica", Font.PLAIN, 12);
//        }

//        FontMetrics fmBigFont = getToolkit().getFontMetrics(bigFont);
//        FontMetrics fmLabelFont = getToolkit().getFontMetrics(labelFont);

        int minTextFieldWidth = 200;
        int textFieldHeight = 23;

        String[] typeStrings = new String[7];
        typeStrings[0] = infoBundle.getString("Number");
        typeStrings[1] = infoBundle.getString("Alphanumeric");
        typeStrings[2] = infoBundle.getString("Boolean (Yes/No)");
        typeStrings[3] = infoBundle.getString("CDate");
        typeStrings[4] = infoBundle.getString("CTime");
        typeStrings[5] = infoBundle.getString("URL");
        typeStrings[6] = infoBundle.getString("Image");

        Class[] types = new Class[7];
        types[0] = java.lang.Double.class; //infoBundle.getString("Number");
        types[1] = java.lang.String.class; //infoBundle.getString("Alphanumeric");
        types[2] = java.lang.Boolean.class; //infoBundle.getString("Boolean (Yes/No)");
        types[3] = gr.cti.eslate.database.engine.CDate.class; //infoBundle.getString("CDate");
        types[4] = gr.cti.eslate.database.engine.CTime.class; //infoBundle.getString("CTime");
        types[5] = java.net.URL.class; //infoBundle.getString("URL");
        types[6] = gr.cti.eslate.database.engine.CImageIcon.class; //infoBundle.getString("Image");

        JLabel fieldNameLabel = new JLabel(fieldNameString);
        FontMetrics fmLabelFont = fieldNameLabel.getFontMetrics(fieldNameLabel.getFont());
        int typeBoxWidth = fmLabelFont.stringWidth(typeStrings[0]);
        for (int i=1; i<7; i++) {
            int strWidth = fmLabelFont.stringWidth(typeStrings[i]);
            if (typeBoxWidth < strWidth)
                typeBoxWidth = strWidth;
        }
        typeBoxWidth = typeBoxWidth + 85; //85 is the image width

        int fieldNamePanelWidth = fmLabelFont.stringWidth(fieldNameString) + 5 + minTextFieldWidth + 4 + 5; //4+5 comes from the compound borders surrounding fieldNamePanel
        int fieldPropertiesWidth = fmLabelFont.stringWidth(fieldTypeString) + 5 + typeBoxWidth + 10 + ((fmLabelFont.stringWidth(calculatedString) > fmLabelFont.stringWidth(keyString))? fmLabelFont.stringWidth(calculatedString):fmLabelFont.stringWidth(keyString)) + 10 + 9; //9 is the width of the TitledBorder surrounding AllFieldPropertiesPanel

        fieldPanelWidth = fieldNamePanelWidth;
        if (fieldPanelWidth < fieldPropertiesWidth)
            fieldPanelWidth = fieldPropertiesWidth;

        AbstractTableField f = null;
        if (isEditingField) {
            try{
                f = dbTable.table.getTableField((String) inputValues.at(0));
            }catch (InvalidFieldNameException exc) {
                System.out.println("Serious inconsistency error in NewFieldDialog NewFieldDialog() : 1"); return;
            }
        }
//        JLabel fieldNameLabel = new JLabel(fieldNameString);
//        fieldNameLabel.setFont(labelFont);

        fieldName = new JTextField();
//        fieldName.setFont(bigFont);
        fieldName.setForeground(inputTextColor);
        fieldName.setSelectionColor(inputTextSelColor);

        if (editingField) {
            fieldName.setText((String) inputValues.at(0));
            fieldName.getHorizontalVisibility().setValue(0);
        }

        fieldNamePanel.add(fieldNameLabel, BorderLayout.WEST);
        fieldNamePanel.add(fieldName, BorderLayout.CENTER);

        Dimension d = new Dimension(fieldPanelWidth, 40);
        fieldNamePanel.setPreferredSize(d);
        fieldNamePanel.setMinimumSize(d);
        fieldNamePanel.setMaximumSize(d);
        fieldNamePanel.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2), new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 3, 5, 2))));


//        JLabel typeLabel = new JLabel(fieldTypeString);
//        typeLabel.setFont(labelFont);

        typeBox = new JComboBox();
//        typeBox.addItem("Integer");
        for (int i=0; i<7; i++)
            typeBox.addItem(types[i]);
        typeBox.setAlignmentY(CENTER_ALIGNMENT);
        typeBox.setPreferredSize(new Dimension(typeBoxWidth, 32));
        typeBox.setMaximumSize(new Dimension(typeBoxWidth, 32));
//        typeBox.setFont(labelFont);
        typeBox.setLightWeightPopupEnabled(false);
        if (!editingField)
            typeBox.setEnabled(false);
        else{
/*            String s = (String) inputValues.at(3);
System.out.println("newFieldDialogNov inputValues.at(3): " + inputValues.at(3));
*/            /* Translating the DBase to UI data type names.
             */
/*            if (s.equals("Double") || s.equals("Integer"))
                s = infoBundle.getString("Number");
            else if (s.equals("String"))
                s = infoBundle.getString("Alphanumeric");
            else if (s.equals("Boolean"))
                s = infoBundle.getString("Boolean (Yes/No)");
            else
                s = infoBundle.getString(s);

            typeBox.setSelectedItem(s);
*/
            if (dbTable.table.isDataChangeAllowed() && f.isFieldDataTypeChangeAllowed())
                typeBox.setEnabled(true);
            else
                typeBox.setEnabled(false);

            if (isEditedFieldCalculated)
                typeBox.setEnabled(false);
        }
        FieldTypeCellRenderer ftcr = new FieldTypeCellRenderer(typeBox, dbTable, color128, new Color(0, 255, 212), infoBundle);
        typeBox.setRenderer(ftcr);
        typeBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg11"));
        //Display the type of the edited calculated field
        if (editingField)
            ftcr.setOpaqueWhenDisabled(false);

        JPanel chBoxPanel = new JPanel(true);
        chBoxPanel.setLayout(new BoxLayout(chBoxPanel, BoxLayout.Y_AXIS));

        keyBox = new JCheckBox(keyString);
        if (!editingField)
            keyBox.setEnabled(false);
        else{
            if (dbTable.table.isKeyChangeAllowed() && f.isFieldKeyAttributeChangeAllowed())
                keyBox.setEnabled(true);
            else
                keyBox.setEnabled(false);
            keyBox.setSelected(((Boolean) inputValues.at(1)).booleanValue());
        }

        calculatedBox = new JCheckBox(calculatedString);
        if (!editingField)
            calculatedBox.setEnabled(false);
        else{
            calculatedBox.setSelected(isEditedFieldCalculated);
            if  (f.isCalcFieldResetAllowed())
                calculatedBox.setEnabled(true);
            else
                calculatedBox.setEnabled(false);

            if (!isEditedFieldCalculated)
                calculatedBox.setEnabled(false);
        }

//        keyBox.setFont(labelFont);
//        calculatedBox.setFont(labelFont);
        keyBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg7"));
        calculatedBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg10"));
        keyBox.setBorder(null);
        calculatedBox.setBorder(null);

        chBoxPanel.add(keyBox);
        chBoxPanel.add(Box.createVerticalStrut(3));
        chBoxPanel.add(calculatedBox);

        JPanel fieldPropertiesPanel = new JPanel(true);
        fieldPropertiesPanel.setLayout(new BoxLayout(fieldPropertiesPanel, BoxLayout.X_AXIS));
//        fieldPropertiesPanel.add(Box.createGlue());
//        fieldPropertiesPanel.add(typeLabel);
//        fieldPropertiesPanel.add(Box.createHorizontalStrut(5));
        fieldPropertiesPanel.add(typeBox);
        fieldPropertiesPanel.add(Box.createHorizontalStrut(10));
        fieldPropertiesPanel.add(Box.createGlue());
        fieldPropertiesPanel.add(chBoxPanel);
        fieldPropertiesPanel.add(Box.createGlue());

        JPanel formulaLabelPanel = new JPanel(true);
        formulaLabelPanel.setLayout(new BoxLayout(formulaLabelPanel, BoxLayout.X_AXIS));
        JLabel formulaLabel = new JLabel(formulaString);
//        formulaLabel.setFont(labelFont);
        formulaLabelPanel.add(formulaLabel);
        formulaLabelPanel.add(Box.createGlue());

        formula = new JTextField();
//        formula.setFont(bigFont);

        formula.setForeground(inputTextColor);
        formula.setSelectionColor(inputTextSelColor);
        if (!editingField) {
            formula.setEditable(false);
            formula.setBackground(getBackground());
            formula.setToolTipText(infoBundle.getString("NewFieldDialogMsg13"));
            formula.setCaretColor(getBackground());
            formula.setRequestFocusEnabled(false);
        }else{
            if (isEditedFieldCalculated) {
                formula.setText((String) inputValues.at(6));
                formula.getHorizontalVisibility().setValue(0);
                if (f.isCalcFieldFormulaChangeAllowed()) {
                    formula.setEnabled(true);
                    formula.setEditable(true);
                    formula.setBackground(Color.white);
                    formula.setCaretColor(Color.black);
                    formula.setRequestFocusEnabled(true);
                }else{
                    formula.setEnabled(false);
                    formula.setEditable(false);
                    formula.setBackground(getBackground());
                    formula.setCaretColor(getBackground());
                    formula.setRequestFocusEnabled(false);
                }
            }else{
                formula.setEditable(false);
                formula.setBackground(getBackground());
                formula.setToolTipText(infoBundle.getString("NewFieldDialogMsg13"));
                formula.setCaretColor(getBackground());
                formula.setRequestFocusEnabled(false);
            }
        }

        JPanel formulaFieldPanel = new JPanel(true);
        formulaFieldPanel.setLayout(new BorderLayout(5, 0));

        formulaFieldPanel.add(formula, BorderLayout.CENTER);
        d = new Dimension(fieldPanelWidth, 20);
        formulaFieldPanel.setMinimumSize(d);
        formulaFieldPanel.setMaximumSize(d);
        formulaFieldPanel.setPreferredSize(d);

        formulaPanel = new JPanel(true);
        formulaPanel.setLayout(new BoxLayout(formulaPanel, BoxLayout.Y_AXIS));
        formulaPanel.add(formulaLabelPanel);
        formulaPanel.add(formulaFieldPanel);

        storeReferencesToImages = new JCheckBox(infoBundle.getString("NewFieldDialogMsg16"));
//        storeReferencesToImages.setFont(dbTable.dbComponent.UIFont);
        if (editingField)
            storeReferencesToImages.setSelected(((Boolean) inputValues.at(7)).booleanValue());

        JPanel imagePropertiesCore = new JPanel(true);
        imagePropertiesCore.setLayout(new BoxLayout(imagePropertiesCore, BoxLayout.X_AXIS));
        imagePropertiesCore.add(storeReferencesToImages);
        imagePropertiesCore.add(Box.createGlue());
        imagePropertiesCore.setAlignmentY(CENTER_ALIGNMENT);

        JPanel imageProperties = new JPanel(true);
        imageProperties.setLayout(new BoxLayout(imageProperties, BoxLayout.Y_AXIS));
        imageProperties.add(Box.createGlue());
        imageProperties.add(imagePropertiesCore);
        imageProperties.add(Box.createGlue());

        propertiesMore = new JPanel(true);
        propertiesMore.setLayout(new CardLayout());
        propertiesMore.add("Formula", formulaPanel);
        propertiesMore.add("Image", imageProperties);

        AllFieldPropertiesPanel = new JPanel(true);
        AllFieldPropertiesPanel.setLayout(new BoxLayout(AllFieldPropertiesPanel, BoxLayout.Y_AXIS));

        AllFieldPropertiesPanel.add(fieldPropertiesPanel);
        boolean isSelectedTypeImage = isImageFieldTypeSelected();
        if (isEditedFieldCalculated || (editingField && isSelectedTypeImage)) {
            AllFieldPropertiesPanel.add(Box.createVerticalStrut(1));
            if (isSelectedTypeImage) //typeBox.getSelectedItem().toString().equals(infoBundle.getString("Image")))
                ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Image");
            AllFieldPropertiesPanel.add(propertiesMore);
            d = new Dimension(fieldPanelWidth, 156);
        }else
            d = new Dimension(fieldPanelWidth, 105); //146);

        TitledBorder tb = new TitledBorder(infoBundle.getString("NewFieldDialogMsg14"));
//        tb.setTitleFont(labelFont);
        tb.setTitleColor(titleBorderColor);
        AllFieldPropertiesPanel.setBorder(tb);

        fieldPanel.setMinimumSize(d);
        fieldPanel.setPreferredSize(d);
        fieldPanel.setMaximumSize(d);

        fieldPanel.add(fieldNamePanel);
        fieldPanel.add(Box.createVerticalStrut(1));
        fieldPanel.add(AllFieldPropertiesPanel);

        Dimension buttonSize = new Dimension(100, 30);
        ok = new JButton(infoBundle.getString("OK"));
        ok.setMaximumSize(buttonSize);
        ok.setPreferredSize(buttonSize);
        ok.setMinimumSize(buttonSize);
//        ok.setFont(labelFont);
        ok.setEnabled(true);
        ok.setForeground(color128);
        if (!editingField)
            ok.setEnabled(false);

        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(buttonSize);
        cancel.setPreferredSize(buttonSize);
        cancel.setMinimumSize(buttonSize);
//        cancel.setFont(labelFont);
        cancel.setEnabled(true);
        cancel.setForeground(color128);

        clearEntries = new JButton(infoBundle.getString("NewFieldDialogMsg15"));
        clearEntries.setMaximumSize(buttonSize);
        clearEntries.setPreferredSize(buttonSize);
        clearEntries.setMinimumSize(buttonSize);
//        clearEntries.setFont(labelFont);
        clearEntries.setForeground(color128);
        if (!editingField)
            clearEntries.setEnabled(false);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createGlue()); //createHorizontalStrut(15));
        buttonPanel.add(ok);
        buttonPanel.add(Box.createVerticalStrut(6));
        buttonPanel.add(cancel);
        if (!isEditingField) {
            buttonPanel.add(Box.createVerticalStrut(6));
            buttonPanel.add(clearEntries);
        }
        buttonPanel.add(Box.createGlue()); //createHorizontalStrut(15));
        buttonPanel.setBorder(new EmptyBorder(1, 7, 0, 0));

        mainPanel.add(fieldPanel);
        mainPanel.add(buttonPanel);
        mainPanel.setBorder(new MatteBorder(7,7,7,7, UIManager.getColor("control")));

        getContentPane().add(mainPanel);

        /* Dialog logic.
         */
        fieldName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
//                System.out.println("In here " + fieldName.getText());
                if (fieldName.getText() != null && fieldName.getText().trim().length() != 0) {
                    ok.setEnabled(true);
                    clearEntries.setEnabled(true);
                    if (calculatedBox.isSelected()) {
                        if (isEditingField) {
                            if (isEditedFieldCalculated)
                                calculatedBox.setEnabled(true);
                        }else
                            calculatedBox.setEnabled(true);
                        formula.setEnabled(true);
                        formula.setEditable(true);
                        formula.setBackground(Color.white);
                        formula.setCaretColor(Color.black);
                        formula.setRequestFocusEnabled(true);
                        formula.paintImmediately(formula.getVisibleRect());
                    }else{
                        typeBox.setEnabled(true);
                        if (isEditingField) {
                            if (isEditedFieldCalculated)
                                calculatedBox.setEnabled(true);
                        }else
                            calculatedBox.setEnabled(true);
                        keyBox.setEnabled(true);
                        formula.setCaretColor(getBackground());
                        formula.setRequestFocusEnabled(false);
                    }
                }else{
                    typeBox.setEnabled(false);
                    calculatedBox.setEnabled(false);
                    keyBox.setEnabled(false);
                    formula.setEnabled(false);
                    formula.setBackground(getBackground());
                    formula.setCaretColor(getBackground());
                    formula.setRequestFocusEnabled(false);
                    formula.paintImmediately(formula.getVisibleRect());
                    formula.setEditable(false);
                    ok.setEnabled(false);
                    clearEntries.setEnabled(false);
                }
            }
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (fieldName.getText() != null && fieldName.getText().trim().length() != 0) {
                        ok.setEnabled(true);
                        clearEntries.setEnabled(true);
                        if (calculatedBox.isSelected()) {
                        if (!isEditingField && isEditedFieldCalculated)
                                calculatedBox.setEnabled(true);
                            formula.setEnabled(true);
                            formula.setEditable(true);
                            formula.setBackground(Color.white);
                            formula.setCaretColor(Color.black);
                            formula.setRequestFocusEnabled(true);
                            formula.paintImmediately(formula.getVisibleRect());
                        }else{
                            typeBox.setEnabled(true);
                            if (!isEditingField && isEditedFieldCalculated)
                                calculatedBox.setEnabled(true);
                            keyBox.setEnabled(true);
                            formula.setCaretColor(getBackground());
                            formula.setRequestFocusEnabled(false);
                        }
                    }else{
//                        System.out.println("Disabling typeBox");
                        ok.setEnabled(false);
                        clearEntries.setEnabled(false);
                        typeBox.setEnabled(false);
                        calculatedBox.setEnabled(false);
                        keyBox.setEnabled(false);
                        formula.setEnabled(false);
                        formula.setBackground(getBackground());
                        formula.setCaretColor(getBackground());
                        formula.setRequestFocusEnabled(false);
                        formula.paintImmediately(formula.getVisibleRect());
                        formula.setEditable(false);
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (fieldName.getText() != null && fieldName.getText().trim().length()!=0)
                        ok.doClick();
                }
            }
        });

        calculatedBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("In state changed");
                if (calculatedBox.isSelected()) {
                    if (AllFieldPropertiesPanel.getComponentCount() == 1) {
                        AllFieldPropertiesPanel.add(Box.createVerticalStrut(5));
                        AllFieldPropertiesPanel.add(propertiesMore);
                    }
                    formula.setEnabled(true);
                    formula.setBackground(Color.white);
                    formula.setEditable(true);
                    formula.setCaretColor(Color.black);
                    formula.setRequestFocusEnabled(true);
                    ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Formula");
                    Dimension d1 = new Dimension(fieldPanelWidth, 146);
                    fieldPanel.setMinimumSize(d1);
                    fieldPanel.setPreferredSize(d1);
                    fieldPanel.setMaximumSize(d1);
                    thisDialog.pack();
                    typeBox.setEnabled(false);
                    formula.requestFocus();
                }else{
                    formula.setEnabled(false);
                    formula.setBackground(getBackground());
                    formula.setEditable(false);
                    formula.setCaretColor(getBackground());
                    formula.setRequestFocusEnabled(false);
                    typeBox.setEnabled(true);
//                    System.out.println("Selected: " + typeBox.getSelectedItem());
                    if (!isImageFieldTypeSelected()) { //!typeBox.getSelectedItem().toString().equals(infoBundle.getString("Image"))) {
                        AllFieldPropertiesPanel.remove(2);
                        AllFieldPropertiesPanel.remove(1);
                        Dimension d1 = new Dimension(fieldPanelWidth, 105);
                        fieldPanel.setMinimumSize(d1);
                        fieldPanel.setPreferredSize(d1);
                        fieldPanel.setMaximumSize(d1);
                        thisDialog.pack();
                    }else{
                        ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Image");
                    }
                }
            }
        });

        typeBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
//                    System.out.println(ev.getItem());
//                    if (ev.getItem().toString().equals(infoBundle.getString("Image"))) {
                    Class selectedType = (Class) ev.getItem();
                    if (CImageIcon.class.isAssignableFrom(selectedType)) {
                        AllFieldPropertiesPanel.add(Box.createVerticalStrut(5));
                        AllFieldPropertiesPanel.add(propertiesMore);
                        ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Image");
                        Dimension d1 = new Dimension(fieldPanelWidth, 146);
                        fieldPanel.setMinimumSize(d1);
                        fieldPanel.setPreferredSize(d1);
                        fieldPanel.setMaximumSize(d1);
                        thisDialog.pack();
                    }
                }
                if (ev.getStateChange() == ItemEvent.DESELECTED) {
//                    System.out.println(ev.getItem());
//                    if (ev.getItem().toString().equals(infoBundle.getString("Image"))) {
                    Class deSelectedType = (Class) ev.getItem();
                    if (CImageIcon.class.isAssignableFrom(deSelectedType)) {
//                        System.out.println("Removing 2 and 1 AllFieldPropertiesPanel: " + AllFieldPropertiesPanel);
                        AllFieldPropertiesPanel.remove(2);
                        AllFieldPropertiesPanel.remove(1);
                        Dimension d1 = new Dimension(fieldPanelWidth, 105);
                        fieldPanel.setMinimumSize(d1);
                        fieldPanel.setPreferredSize(d1);
                        fieldPanel.setMaximumSize(d1);
                        thisDialog.pack();
                    }
                }
            }
        });

        formula.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (fieldName.getText() != null && fieldName.getText().trim().length()!=0 && formula.getText().trim().length() != 0)
                        ok.doClick();
                }
            }
        });

        clearEntries.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fieldName.setText("");
                fieldName.requestFocus();
                keyBox.setSelected(false);
                keyBox.setEnabled(false);
                typeBox.setEnabled(false);
                formula.setEnabled(false);
                formula.setBackground(getBackground());
                formula.setEditable(false);
                formula.setCaretColor(getBackground());
                formula.setRequestFocusEnabled(false);
//                formula.paintImmediately(formula.getVisibleRect());

                if (calculatedBox.isSelected()) {
                    AllFieldPropertiesPanel.remove(2);
                    AllFieldPropertiesPanel.remove(1);
                    Dimension d1 = new Dimension(fieldPanelWidth, 105);
                    fieldPanel.setMinimumSize(d1);
                    fieldPanel.setPreferredSize(d1);
                    fieldPanel.setMaximumSize(d1);
                    ok.setEnabled(false);
                    clearEntries.setEnabled(false);
                    thisDialog.pack();
                }
                calculatedBox.setSelected(false);
                calculatedBox.setEnabled(false);
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                thisDialog.removeWindowListener(l);
//                thisDialog.clickedButton = 0;
	            thisDialog.dispose();
	        }
	    });

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnValue = DIALOG_OK;
                outputValues.clear();
                outputValues.add(fieldName.getText());
                outputValues.add(new Boolean(keyBox.isSelected()));
                outputValues.add(new Boolean(calculatedBox.isSelected()));
                String realType = "";
/*                String type = (String) typeBox.getSelectedItem();

                Enumeration typeKeys = infoBundle.getKeys();
                while (typeKeys.hasMoreElements()) {
                    realType = (String) typeKeys.nextElement();
//                    (String) infoBundle.getString((String) typeKeys.nextElement());
                    if (infoBundle.getString(realType).equals(type))
                        break;
                }

System.out.println(type + ", " + realType);
                if (realType.equals("Number"))
                    realType = "Double";
                else if (realType.equals("Alphanumeric"))
                    realType = "String";
                else if (realType.equals("Boolean (Yes/No)") || realType.equals("PreferencesDialogMsg23"))
                    realType = "Boolean";

//                if (type.equals(infoBundle.getString("Number")))
//                    type = "Double";
*/                outputValues.add(realType);

                // Is field editable?
                if (calculatedBox.isSelected())
                    outputValues.add(new Boolean(false));
                else
                    outputValues.add(new Boolean(true));

                // Is field removable?
                if (calculatedBox.isSelected())
                    outputValues.add(new Boolean(false));
                else
                    outputValues.add(new Boolean(true));
                outputValues.add(formula.getText());

                // Is field's data externally stored
                if (storeReferencesToImages.isSelected())
                    outputValues.add(new Boolean(true));
                else
                    outputValues.add(new Boolean(false));

//                thisDialog.removeWindowListener(l);
//                clickedButton = 1;
	            dispose();
	        }
	    });
    }

    void showDialog() {
        /* Add the window listener.
         */
/*    	addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
//                thisDialog.removeWindowListener(l);
                thisDialog.clickedButton = 0;
	            thisDialog.dispose();
	        }
	    });
*/
        /* Display the "NewFieldDialogNov".
         */
        setResizable(false);

        pack();
//        Rectangle dbBounds = jTable.dbComponent.getBounds();
//        setLocation(dbBounds.width/2 - getSize().width/2, dbBounds.height/2 - getSize().height/2);
        fieldName.requestFocus();
        Rectangle dbBounds = dbTable.getBounds();
        System.out.println("getLocationOnScreen() 7");
        java.awt.Point dbLocation = dbTable.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + jTable.dbComponent.getLocationOnScreen());
        int x = dbLocation.x + dbBounds.width/2 - getSize().width/2;
        int y = dbLocation.y + dbBounds.height/2-getSize().height/2;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if (x+getSize().width > screenSize.width)
            x = screenSize.width - getSize().width;
        if (y+getSize().height > screenSize.height)
            y = screenSize.height - getSize().height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        setLocation(x, y);
        setVisible(true);
    }

    public Class getFieldType() {
        return (Class) typeBox.getSelectedItem();
    }

    public void setFieldType(Class type) {
        typeBox.setSelectedItem(type);
    }

    public int getReturnValue() {
        return returnValue;
    }

    boolean isImageFieldTypeSelected() {
        Class selectedType = (Class) typeBox.getSelectedItem();
        if (selectedType == null) return false;
        return (CImageIcon.class.isAssignableFrom(selectedType));
    }
}

