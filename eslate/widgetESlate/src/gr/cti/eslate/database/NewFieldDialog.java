package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
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
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Enumeration;


class NewFieldDialog extends JDialog {
    public static final int DIALOG_CANCEL = 1;
    public static final int DIALOG_OK = 0;
    int dialogWidth = 482;
    int dialogHeight = 226;

    JTextField fieldName, formula;
    JCheckBox keyBox, editBox, removBox, calculatedBox, storeReferencesToImages;
    JComboBox typeBox;
    JButton ok, cancel, clearEntries;
    JPanel propertiesMore;
    FieldTypeCellRenderer ftcr;
    DBTable dbTable = null;

    boolean editBoxEdited = false;
    boolean removBoxEdited = false;
    boolean isEditingField;
    boolean isEditedFieldCalculated = false;

//    static int clickedButton = 0;
    static Array inputValues = new Array();
    static Array outputValues = new Array();
    transient ResourceBundle infoBundle;
    int returnValue = DIALOG_CANCEL;

    protected NewFieldDialog(Frame frame, DBTable dbTable, boolean editingField, String title) {
        super(frame, true);
        isEditingField = editingField;
        this.dbTable = dbTable;
        infoBundle = Database.infoBundle;
        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	this.getRootPane().WHEN_IN_FOCUSED_WINDOW);

        //setTitle(infoBundle.getString("NewFieldDialogMsg1"));
        setTitle(title);

//        System.out.println(infoBundle.getClass().getName());
        if (infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
            dialogWidth = dialogWidth+65;

//        System.out.println("dialogWidth: " + dialogWidth);

//        getContentPane().setBackground(Color.lightGray);
        Color color128 = new Color(0, 0, 128);
        Color titleBorderColor = new Color(119, 40, 104);
        Color inputTextColor = new Color(18, 102, 99);
        Color inputTextSelColor = Color.black;

        BoxLayout bl4 = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl4);

        JPanel fieldNamePanel = new JPanel();

        JLabel l1 = new JLabel(infoBundle.getString("NewFieldDialogMsg2"));
        Font bigFont =l1.getFont().deriveFont(14f);
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            bigFont = new Font("Dialog", Font.PLAIN, 14);
//        else
//            bigFont = new Font("Helvetica", Font.PLAIN, 14);

        l1.setFont(bigFont);
        fieldName = new JTextField();
        fieldName.setFont(bigFont);
        fieldName.setForeground(inputTextColor);
        fieldName.setSelectionColor(inputTextSelColor);
        fieldName.setPreferredSize(new Dimension(200, 23));

        AbstractTableField f = null;
        if (isEditingField) {
            try{
                f = dbTable.table.getTableField((String) inputValues.at(0));
            }catch (InvalidFieldNameException exc) {
                System.out.println("Serious inconsistency error in NewFieldDialog NewFieldDialog() : 1"); return;
            }
        }

        if (editingField) {
            fieldName.setText((String) inputValues.at(0));
            fieldName.getHorizontalVisibility().setValue(0);
        }

        fieldNamePanel.add(l1);
        fieldNamePanel.add(Box.createHorizontalStrut(3));
        fieldNamePanel.add(fieldName);
        fieldNamePanel.setPreferredSize(new Dimension(dialogWidth-10, 37));
        fieldNamePanel.setMinimumSize(new Dimension(dialogWidth-10, 37));
        fieldNamePanel.setMaximumSize(new Dimension(dialogWidth-10, 37));
        fieldNamePanel.setBorder(new EtchedBorder());

        getContentPane().add(Box.createVerticalStrut(5));
        getContentPane().add(fieldNamePanel);
        getContentPane().add(Box.createVerticalStrut(5));

        JPanel attribPanel = new JPanel();
        attribPanel.add(Box.createVerticalStrut(5));

        if (editingField && ((Boolean) inputValues.at(2)).booleanValue())
            isEditedFieldCalculated = true;

        JPanel chBoxPanel = new JPanel();
        keyBox = new JCheckBox(infoBundle.getString("NewFieldDialogMsg3"));
        if (!editingField)
            keyBox.setEnabled(false);
        else{
            if (dbTable.table.isKeyChangeAllowed() && f.isFieldKeyAttributeChangeAllowed())
                keyBox.setEnabled(true);
            else
                keyBox.setEnabled(false);
            keyBox.setSelected(((Boolean) inputValues.at(1)).booleanValue());
        }

        editBox = new JCheckBox(infoBundle.getString("NewFieldDialogMsg4"));
        if (!editingField)
            editBox.setEnabled(false);
        else{
            if  (f.isFieldEditabilityChangeAllowed())
                editBox.setEnabled(true);
            else
                editBox.setEnabled(false);
            if (isEditedFieldCalculated)
                editBox.setEnabled(false);
            else
                editBox.setSelected(((Boolean) inputValues.at(4)).booleanValue());
        }

        removBox = new JCheckBox(infoBundle.getString("NewFieldDialogMsg5"));
        if (!editingField)
            removBox.setEnabled(false);
        else{
            if  (f.isFieldRemovabilityChangeAllowed())
                removBox.setEnabled(true);
            else
                removBox.setEnabled(false);
            removBox.setSelected(((Boolean) inputValues.at(5)).booleanValue());
        }

        calculatedBox = new JCheckBox(infoBundle.getString("NewFieldDialogMsg6"));
        if (!editingField)
            calculatedBox.setEnabled(false);
        else{
            if  (f.isCalcFieldResetAllowed())
                calculatedBox.setEnabled(true);
            else
                calculatedBox.setEnabled(false);
            calculatedBox.setSelected(isEditedFieldCalculated);
            if (!isEditedFieldCalculated)
                calculatedBox.setEnabled(false);
        }

//        Font labelFont;
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            labelFont = new Font("Dialog", Font.PLAIN, 12);
//        else
//            labelFont = new Font("Helvetica", Font.PLAIN, 12);

//        keyBox.setFont(labelFont);
        keyBox.setHorizontalTextPosition(SwingConstants.LEFT);
        keyBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg7"));
//        editBox.setFont(labelFont);
        editBox.setHorizontalTextPosition(SwingConstants.LEFT);
        editBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg8"));
//        removBox.setFont(labelFont);
        removBox.setHorizontalTextPosition(SwingConstants.LEFT);
        removBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg9"));
//        calculatedBox.setFont(labelFont);
        calculatedBox.setHorizontalTextPosition(SwingConstants.LEFT);
        calculatedBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg10"));

        typeBox = new JComboBox();
//        typeBox.addItem("Integer");
/*        typeBox.addItem(infoBundle.getString("Number"));
        typeBox.addItem(infoBundle.getString("Alphanumeric"));
        typeBox.addItem(infoBundle.getString("Boolean (Yes/No)"));
        typeBox.addItem(infoBundle.getString("Date"));
        typeBox.addItem(infoBundle.getString("Time"));
        typeBox.addItem(infoBundle.getString("URL"));
        typeBox.addItem(infoBundle.getString("Image"));
*/
        typeBox.addItem(String.class);
        typeBox.addItem(Integer.class);
        typeBox.addItem(Float.class);
        typeBox.addItem(Double.class);
        typeBox.addItem(Boolean.class);
        typeBox.addItem(gr.cti.eslate.database.engine.CDate.class);
        typeBox.addItem(gr.cti.eslate.database.engine.CTime.class);
        typeBox.addItem(java.net.URL.class);
        typeBox.addItem(gr.cti.eslate.database.engine.CImageIcon.class);
//        typeBox.setMaximumRowCount(3);
        typeBox.setLightWeightPopupEnabled(false);
        typeBox.setAlignmentY(CENTER_ALIGNMENT);
        typeBox.setPreferredSize(new Dimension(170, 32));
        typeBox.setMaximumSize(new Dimension(170, 32));
//        typeBox.setBackground(Color.lightGray);
        if (!editingField)
            typeBox.setEnabled(false);
        else{
            typeBox.setSelectedItem(String.class);
/*            String s = (String) inputValues.at(3);
*/
            /* Translating the DBase to UI data type names.
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
        ftcr = new FieldTypeCellRenderer(typeBox, dbTable, color128, new Color(0, 255, 212), infoBundle);
        typeBox.setRenderer(ftcr);
        //Display the type of the edited calculated field
        if (editingField)
            ftcr.setOpaqueWhenDisabled(false);

        typeBox.setToolTipText(infoBundle.getString("NewFieldDialogMsg11"));

        chBoxPanel.add(keyBox);
        keyBox.setAlignmentY(CENTER_ALIGNMENT);
        chBoxPanel.add(Box.createHorizontalStrut(3));
        chBoxPanel.add(calculatedBox);
        calculatedBox.setAlignmentY(CENTER_ALIGNMENT);
        chBoxPanel.add(Box.createHorizontalStrut(3));
        chBoxPanel.add(typeBox);
        typeBox.setAlignmentY(CENTER_ALIGNMENT);
        chBoxPanel.add(Box.createHorizontalStrut(3));
        chBoxPanel.add(editBox);
        editBox.setAlignmentY(CENTER_ALIGNMENT);
        chBoxPanel.add(Box.createHorizontalStrut(3));
        chBoxPanel.add(removBox);
        removBox.setAlignmentY(CENTER_ALIGNMENT);

        BoxLayout bl2 = new BoxLayout(chBoxPanel, BoxLayout.X_AXIS);
        chBoxPanel.setLayout(bl2);
        chBoxPanel.setPreferredSize(new Dimension(dialogWidth, 40));

        JPanel formulaPanel = new JPanel();
        BoxLayout bl3 = new BoxLayout(formulaPanel, BoxLayout.X_AXIS);
        formulaPanel.setLayout(bl3);

        JLabel l6 = new JLabel(infoBundle.getString("NewFieldDialogMsg12"));
//        l6.setFont(labelFont);
        formula = new JTextField();
        formula.setFont(formula.getFont().deriveFont(formula.getFont().getSize() + 2f));
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            formula.setFont(new Font("Dialog", Font.PLAIN, 14));
//        else
//            formula.setFont(new Font("Helvetica", Font.PLAIN, 14));

        formula.setForeground(inputTextColor);
        formula.setSelectionColor(inputTextSelColor);
        if (!editingField) {
            formula.setEditable(false);
            formula.setBackground(Color.lightGray);
            formula.setToolTipText(infoBundle.getString("NewFieldDialogMsg13"));
            formula.setCaretColor(Color.lightGray);
            formula.setRequestFocusEnabled(false);
        }else{
            if (isEditedFieldCalculated) {
//                System.out.println("Setting the formula field to: " + (String) inputValues.at(6));
                formula.setText((String) inputValues.at(6));
//                System.out.println(formula.getHorizontalVisibility().getValue());
                formula.getHorizontalVisibility().setValue(0);
                if (f.isCalcFieldFormulaChangeAllowed())
                    formula.setEnabled(true);
                else{
                    formula.setEnabled(false);
                    formula.setEditable(false);
                    formula.setRequestFocusEnabled(false);
                }
//                System.out.println(formula.getHorizontalVisibility().getValue());
            }else{
                formula.setEditable(false);
                formula.setBackground(Color.lightGray);
                formula.setToolTipText(infoBundle.getString("NewFieldDialogMsg13"));
                formula.setCaretColor(Color.lightGray);
                formula.setRequestFocusEnabled(false);
            }
        }

        formula.setMaximumSize(new Dimension(dialogWidth-75, 23));
        formula.setMinimumSize(new Dimension(dialogWidth-75, 23));

        formulaPanel.add(l6);
        formulaPanel.add(Box.createHorizontalStrut(3));
        formulaPanel.add(formula);

        attribPanel.add(chBoxPanel);
        attribPanel.add(Box.createVerticalStrut(10));

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

        attribPanel.add(propertiesMore);
        attribPanel.add(Box.createVerticalStrut(5));

        if (editingField && isImageFieldTypeSelected()) //typeBox.getSelectedItem().toString().equals(infoBundle.getString("Image")))
            ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Image");

        TitledBorder tb = new TitledBorder(infoBundle.getString("NewFieldDialogMsg14"));

//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            tb.setTitleFont(new Font("Dialog", Font.PLAIN, 12));
//        else
//            tb.setTitleFont(new Font("Helvetica", Font.PLAIN, 12));

        tb.setTitleColor(titleBorderColor);
        attribPanel.setBorder(tb);

        BoxLayout bl5 = new BoxLayout(attribPanel, BoxLayout.Y_AXIS);
        attribPanel.setLayout(bl5);
        attribPanel.setMaximumSize(new Dimension(dialogWidth-10, 100));
        attribPanel.setPreferredSize(new Dimension(dialogWidth-10, 100));
        attribPanel.setMinimumSize(new Dimension(dialogWidth-10, 100));

        getContentPane().add(attribPanel);
        getContentPane().add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel();
        Font buttonFont;
//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            buttonFont = new Font("Dialog", Font.PLAIN, 12);
//        else
//            buttonFont = new Font("Helvetica", Font.PLAIN, 12);


        ok = new JButton(infoBundle.getString("OK"));
        ok.setMaximumSize(new Dimension(100, 30));
        ok.setPreferredSize(new Dimension(100, 30));
        ok.setMinimumSize(new Dimension(100, 30));
//        ok.setFont(buttonFont);
        if (!editingField)
            ok.setEnabled(false);
        ok.setForeground(color128);
        clearEntries = new JButton(infoBundle.getString("NewFieldDialogMsg15"));
        clearEntries.setMaximumSize(new Dimension(100, 30));
        clearEntries.setPreferredSize(new Dimension(100, 30));
        clearEntries.setMinimumSize(new Dimension(100, 30));
//        clearEntries.setFont(buttonFont);
        clearEntries.setForeground(color128);
        if (!editingField)
            clearEntries.setEnabled(false);
        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(new Dimension(100, 30));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.setMinimumSize(new Dimension(100, 30));
//        cancel.setFont(buttonFont);
        cancel.setForeground(color128);

        buttonPanel.add(Box.createGlue());

        if (!isEditingField) {
            buttonPanel.add(clearEntries);
            buttonPanel.add(Box.createHorizontalStrut(10));
        }
        buttonPanel.add(ok);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue());

        BoxLayout bl6 = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(bl6);
        buttonPanel.setMaximumSize(new Dimension(dialogWidth, 33));

        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(3));

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
                        editBox.setEnabled(true);
                        if (!editBoxEdited && !isEditingField)
                            editBox.setSelected(true);
                        removBox.setEnabled(true);
                        if (!removBoxEdited && !isEditingField)
                            removBox.setSelected(true);
                        formula.setCaretColor(Color.lightGray);
                        formula.setRequestFocusEnabled(false);
                    }
                }else{
//                    System.out.println("Disabling combBox1");
                    typeBox.setEnabled(false);
                    calculatedBox.setEnabled(false);
                    keyBox.setEnabled(false);
                    editBox.setEnabled(false);
                    removBox.setEnabled(false);
                    formula.setEnabled(false);
                    formula.setBackground(Color.lightGray);
                    formula.setCaretColor(Color.lightGray);
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
                            editBox.setEnabled(true);
                            if (!editBoxEdited && !isEditingField)
                                editBox.setSelected(true);
                            removBox.setEnabled(true);
                            if (!removBoxEdited && !isEditingField)
                                removBox.setSelected(true);
                            formula.setCaretColor(Color.lightGray);
                            formula.setRequestFocusEnabled(false);
                        }
                    }else{
//                        System.out.println("Disabling typeBox");
                        ok.setEnabled(false);
                        clearEntries.setEnabled(false);
                        typeBox.setEnabled(false);
                        calculatedBox.setEnabled(false);
                        keyBox.setEnabled(false);
                        editBox.setEnabled(false);
                        removBox.setEnabled(false);
                        formula.setEnabled(false);
                        formula.setBackground(Color.lightGray);
                        formula.setCaretColor(Color.lightGray);
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
                    if (isEditedFieldCalculated)
                        ftcr.setOpaqueWhenDisabled(false);
                    else
                        ftcr.setOpaqueWhenDisabled(true);

                    typeBox.setEnabled(false);
                    editBox.setSelected(false);
                    removBox.setSelected(false);
                    editBox.setEnabled(false);
                    formula.setEnabled(true);
                    formula.setBackground(Color.white);
                    formula.setEditable(true);
                    formula.setCaretColor(Color.black);
                    formula.setRequestFocusEnabled(true);
                    ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Formula");
                    formula.paintImmediately(formula.getVisibleRect());
                }else{
                    typeBox.setEnabled(true);
                    editBox.setEnabled(true);
                    formula.setEnabled(false);
                    formula.setBackground(Color.lightGray);
                    formula.setEditable(false);
                    formula.setCaretColor(Color.lightGray);
                    formula.setRequestFocusEnabled(false);
                    if (isImageFieldTypeSelected()) //typeBox.getSelectedItem().toString().equals(infoBundle.getString("Image")))
                        ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Image");
                    formula.paintImmediately(formula.getVisibleRect());
                }
            }
        });

        typeBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
//                    System.out.println(ev.getItem());
                    Class selectedType = (Class) ev.getItem();
                    if (CImageIcon.class.isAssignableFrom(selectedType)) //ev.getItem().toString().equals(infoBundle.getString("Image"))) {
                        ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Image");
                }
                if (ev.getStateChange() == ItemEvent.DESELECTED) {
//                    System.out.println(ev.getItem());
                    Class deSelectedType = (Class) ev.getItem();
                    if (CImageIcon.class.isAssignableFrom(deSelectedType)) //ev.getItem().toString().equals(infoBundle.getString("Image"))) {
                        ((CardLayout) propertiesMore.getLayout()).show(propertiesMore, "Formula");
                }
            }
        });

        editBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editBoxEdited = true;
            }
        });

        removBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removBoxEdited = true;
            }
        });

        formula.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
/*                System.out.println("Columns: " + formula.getColumns() +
                                    ", Current caret pos: " + formula.getCaretPosition() +
                                    ", Extent: " + formula.getHorizontalVisibility().getExtent() +
                                    ", scoll offset: " + formula.getScrollOffset() +
                                    ", minimum: " + formula.getHorizontalVisibility().getMinimum() +
                                    ", maximum: " + formula.getHorizontalVisibility().getMaximum());
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    try{
                    System.out.println("Scrolling???  " + formula.modelToView(formula.getCaretPosition()).x);
//                        formula.scrollRectToVisible(formula.modelToView(formula.getCaretPosition()));
//                        formula.setScrollOffset(formula.modelToView(formula.getCaretPosition()).x);
//                          formula.getHorizontalVisibility().setExtent(formula.getCaretPosition());
                    }catch (javax.swing.text.BadLocationException e1) {System.out.println("Bad Location");}
//                    formula.validate();
//                    formula.paintImmediately(formula.getVisibleRect());
                }
*/                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
                calculatedBox.setSelected(false);
                calculatedBox.setEnabled(false);
                typeBox.setEnabled(false);
                editBox.setSelected(false);
                editBox.setEnabled(false);
                editBoxEdited = false;
                removBox.setSelected(false);
                removBox.setEnabled(false);
                removBoxEdited = false;
                formula.setEnabled(false);
                formula.setBackground(Color.lightGray);
                formula.setEditable(false);
                formula.setCaretColor(Color.lightGray);
                formula.setRequestFocusEnabled(false);
                formula.paintImmediately(formula.getVisibleRect());
                ok.setEnabled(false);
                clearEntries.setEnabled(false);
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                thisDialog.removeWindowListener(l);
//                thisDialog.clickedButton = 0;
	            dispose();
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

//                System.out.println(type + ", " + realType);
                if (realType.equals("Number"))
                    realType = "Double";
                else if (realType.equals("Alphanumeric"))
                    realType = "String";
                else if (realType.equals("Boolean (Yes/No)") || realType.equals("PreferencesDialogMsg23"))
                    realType = "Boolean";

//                if (type.equals(infoBundle.getString("Number")))
//                    type = "Double";
*/                outputValues.add(realType);
                outputValues.add(new Boolean(editBox.isSelected()));
                outputValues.add(new Boolean(removBox.isSelected()));
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
        /* Display the "NewFieldDialog".
         */
        this.setResizable(false);

//        System.out.println("dialogWidth: " + dialogWidth);
        setSize(dialogWidth, dialogHeight);
//    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//	    setLocation(screenSize.width/2 - dialogWidth/2, screenSize.height/2 - dialogHeight/2);
        pack();
        fieldName.requestFocus();
        Rectangle dbBounds = dbTable.getBounds();
        System.out.println("getLocationOnScreen() 6");
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
