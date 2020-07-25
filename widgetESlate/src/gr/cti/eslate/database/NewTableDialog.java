package gr.cti.eslate.database;

import gr.cti.eslate.utils.*;
import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.Icon;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Dialog;
import java.awt.event.*;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.awt.Point;
import java.awt.Cursor;
import java.util.ResourceBundle;
import java.util.Enumeration;


class NewTableDialog extends JDialog {
    int dialogWidth = 638;
    int dialogHeight = 500;
    int fieldWidth = dialogWidth-20;

    NewTableDialog thisDialog;
    NewTableScrollPane newFieldsScrollPane;
    NewFieldPanel activeFieldPanel = null;
    Array  newFieldPanelArray = new Array();
    int activeFieldPanelIndex = -1;
    boolean emptyFieldNameExists = true;
    boolean otherEmptyFieldNamesExist = false;
    long timeLastCompoBoxItemChanged = 2000;
    DBTable table;
    JTextField tf1;
    JPanel newFieldsPanel;
    JButton newField, removeField, clearField, cancel, createTable;
    WindowListener l;
    transient ResourceBundle infoBundle;

    static int clickedButton;
    static String tableName;
    static Array fieldNameList = new Array();
    static Array calculatedList = new Array();
    static Array dataTypeList = new Array();
    static Array keyList = new Array();
    static Array editableList = new Array();
    static Array removableList = new Array();
    static Array formulaList = new Array();

    protected NewTableDialog(Frame frame, DBTable tabl, Database dbComponent) {
        super(frame, true);
        thisDialog = this;
        table = tabl;
        infoBundle = dbComponent.infoBundle;
        setTitle(infoBundle.getString("NewTableDialogMsg1"));

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

//        getContentPane().setBackground(Color.lightGray);
        Color titleBackgroundColor = new Color(132, 178, 149); //0,0,128);
        Color titleLabelColor = Color.black; //Color.orange
        Color titleBorderColor = titleLabelColor;

        BoxLayout bl1 = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl1);

        JPanel titlePanel = new JPanel();
        BoxLayout bl11 = new BoxLayout(titlePanel, BoxLayout.Y_AXIS);
        titlePanel.setLayout(bl11);

        JPanel tableNamePanel = new JPanel();
        Font bigFont = new Font("TimesRoman", Font.BOLD, 24);
        JLabel label1 = new JLabel(infoBundle.getString("NewTableDialogMsg2"));
        label1.setFont(bigFont);
        label1.setForeground(titleLabelColor);
        tf1 = new JTextField();
        tf1.setPreferredSize(new Dimension(180, 25));
        tf1.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        tf1.setForeground(new Color(16, 41, 232)); //Color.black); //new Color(0,0,128));
        tf1.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                /* The following if-statement is actually a trick. There was a serious problem with
                 * JComboBoxes in JFC version 0.5.1. Maybe this still exists. Everytime the user
                 * clicked on a field data type JComboBox "combBox1" and its pop-up window became
                 * visible, the JComboBox1 lost its focus. This happened as soon as the pop-up window
                 * was diplayed. The focus was taken either by "tf1", the JTextField for the name of
                 * the field, or by the JComboBox which was active prior to the current one. Because
                 * the focus mechanism is heavily used in this dialog (NewTableDialog), this led to
                 * serious blinking side-effects. For example, after the user had chosed smth from the
                 * pop-up window of the active comboBox, the pop-up window closed and another "newField
                 * Panel" became active (i.e. highlighted). Immediately after this, the irrelevant
                 * "newFieldPanel" would lose focus, which would be acquired again by the proper
                 * "newFieldPanel", i.e. the one that contains the JComboBox which was activated. To
                 * correct this I introduced the following trick: when an item is chosen from the
                 * active JComboBox's pop-up window, the current time is tracked down and stored in
                 * variable "timeLastCompoBoxItemChanged". Next the active JComboBox illegally
                 * looses focus which is gained either by "tf1" or some other JComboBox in another
                 * "newFieldPanel". When this happens the "newFieldPanel" which used to be active
                 * looses highlight. However before this is done in the "focusGained" of the focus
                 * listeners of "tf1" and the other comboBoxes, we check if the time that passed since
                 * the last item change, i.e. the difference:
                 *          "System.currentTimeMillis() - timeLastCompoBoxItemChanged"
                 * is more that a value that makes sense, i.e. a value which represents a time gap
                 * in which a human hand cannot both change the selected item in a pop-up window and
                 * activate another JComboBox or JTextField. If the ellapsed time from the last item
                 * change is bigger than this value, then the activated component proceeds in the
                 * de-activation of the active "newFieldPanel" and the activation of the new one,
                 * if the component is a JComboBox which belongs to the new "newFieldpanel".
                 * Otherwise the component acquires focus but does nothing, waiting for the
                 * JComboBox to get back its focus.
                 * The user can still witness this ungly side-effect if he chooses from a
                 * JComboBox's pop-up window the same item which is currently selected. In this case
                 * no "itemStateChanged" event is launced which means that the value of
                 * "timeLastCompoBoxItemChanged" is not upfated with the current time.
                 */

                ///Removed from JFC version 0.7 and on
                if (System.currentTimeMillis() - timeLastCompoBoxItemChanged > 200) {
//                    System.out.println("In focus gained tf1 " + e.getSource());
                    clearField.setEnabled(false);
                    removeField.setEnabled(false);
                    if (activeFieldPanel != null) {
                        activeFieldPanel.setBackground(getBackground());
                        activeFieldPanel.setBorder(null);
                        activeFieldPanel.paintImmediately(activeFieldPanel.getVisibleRect());
                        activeFieldPanel = null;
                        activeFieldPanelIndex = -1;
                    }
                }
            }
            public void focusLost(FocusEvent e) {
                //System.out.println("In focus lost tf1");
            }
        });
        tf1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (tf1.getText() != null && tf1.getText().trim().length() != 0)
                    createTable.setEnabled(true);
                else
                    createTable.setEnabled(false);
            }
        });

        tableNamePanel.add(label1);
        tableNamePanel.add(tf1);
//        tableNamePanel.setBorder(new LineBorder(titleBorderColor));
        tableNamePanel.setBackground(titleBackgroundColor);
        tableNamePanel.setMaximumSize(new Dimension(dialogWidth, 40));

//        getContentPane().add(Box.createVerticalStrut(5));
        titlePanel.add(tableNamePanel);

        JPanel linePanel = new JPanel();
        linePanel.setBackground(Color.gray);
        linePanel.setBorder(new EtchedBorder());
        linePanel.setMinimumSize(new Dimension(dialogWidth, 2));
        linePanel.setPreferredSize(new Dimension(dialogWidth, 2));
        linePanel.setMaximumSize(new Dimension(dialogWidth, 2));

        titlePanel.add(linePanel);

        JPanel titles = new JPanel();
        JPanel titles1 = new JPanel();
        JPanel titles2 = new JPanel();

//        Font titleFont;
//        if (dbComponent.getLocale().toString().equals("el_GR"))
//            titleFont = new Font("Helvetica", Font.PLAIN, 14);
//        else
//            titleFont = new Font("TimesRoman", Font.PLAIN, 14);

        String fieldNameStr = infoBundle.getString("NewFieldDialogMsg2");
        JLabel fieldNameLabel = new JLabel(fieldNameStr);
        Font titleFont = fieldNameLabel.getFont().deriveFont(fieldNameLabel.getFont().getSize() + 2f);
        fieldNameLabel.setFont(titleFont);
        fieldNameLabel.setForeground(titleLabelColor);
        String calculatedStr = infoBundle.getString("NewFieldDialogMsg6");
        JLabel calcLabel = new JLabel(calculatedStr);
        calcLabel.setFont(titleFont);
        calcLabel.setForeground(titleLabelColor);
        String typeStr = infoBundle.getString("NewTableDialogMsg3");
        JLabel typeLabel = new JLabel(typeStr);
        typeLabel.setFont(titleFont);
        typeLabel.setForeground(titleLabelColor);
        String keyStr = infoBundle.getString("NewFieldDialogMsg3");
        JLabel keyLabel = new JLabel(keyStr);
        keyLabel.setFont(titleFont);
        keyLabel.setForeground(titleLabelColor);
        String editStr = infoBundle.getString("NewFieldDialogMsg4");
        JLabel editLabel = new JLabel(editStr);
        editLabel.setFont(titleFont);
        editLabel.setForeground(titleLabelColor);
        String removStr = infoBundle.getString("NewFieldDialogMsg5");
        JLabel removLabel = new JLabel(removStr);
        removLabel.setFont(titleFont);
        removLabel.setForeground(titleLabelColor);
        String formulaStr = infoBundle.getString("NewFieldDialogMsg12");
        JLabel formulaLabel = new JLabel(formulaStr);
        formulaLabel.setFont(titleFont);
        formulaLabel.setForeground(titleLabelColor);

        /* Centering the titles of the columns.
         */
        int gap = 5;
        int fieldNameFieldWindth = 110;
        int chBoxWidth = 15;
        int combBoxWidth = 170;
        int formulaWidth = 223;
        FontMetrics fm = getToolkit().getFontMetrics(titleFont);
        int fieldNameLabelWidth = fm.stringWidth(fieldNameStr);
        int calcLabelWidth = fm.stringWidth(calculatedStr);
        int typeLabelWidth = fm.stringWidth(typeStr);
        int keyLabelWidth = fm.stringWidth(keyStr);
        int editLabelWidth = fm.stringWidth(editStr);
        int removLabelWidth = fm.stringWidth(removStr);
        int formulaLabelWidth = fm.stringWidth(formulaStr);

        int center = gap + (fieldNameFieldWindth - fieldNameLabelWidth)/2;
        titles1.add(Box.createHorizontalStrut(center)); // (+5) from Swing 0.6.1
        titles1.add(fieldNameLabel);
        center = (fieldNameFieldWindth - fieldNameLabelWidth)/2 + 2*gap + chBoxWidth + ((combBoxWidth - typeLabelWidth)/2);
        titles1.add(Box.createHorizontalStrut(center));// + 15)); // +15 Because of dialog's width expansion
        titles1.add(typeLabel);
        center = ((combBoxWidth - typeLabelWidth)/2) + 2*gap + chBoxWidth + ((chBoxWidth -  editLabelWidth)/2);
        titles1.add(Box.createHorizontalStrut(center));  //+35 Because of dialog's width expansion
        titles1.add(editLabel);
        center = ((chBoxWidth -  editLabelWidth)/2) + 2*gap + chBoxWidth + ((formulaWidth - formulaLabelWidth)/2);
        titles1.add(Box.createHorizontalStrut(center));
        titles1.add(formulaLabel);

        center = gap + fieldNameFieldWindth + gap + ((chBoxWidth - calcLabelWidth)/2);
        titles2.add(Box.createHorizontalStrut(center));
        titles2.add(calcLabel);
        center = ((chBoxWidth - calcLabelWidth)/2) + 2*gap + combBoxWidth + ((chBoxWidth - keyLabelWidth)/2) - 4; // -4, because of some mistake???
        titles2.add(Box.createHorizontalStrut(center));
        titles2.add(keyLabel);
        center = ((chBoxWidth - keyLabelWidth)/2) + 2*gap + chBoxWidth + ((chBoxWidth - removLabelWidth)/2);
//        System.out.println("center: " + center);
        if (center <= 2) center = 3;
        titles2.add(Box.createHorizontalStrut(center));
        titles2.add(removLabel);

/*        titles1.add(Box.createHorizontalStrut(30+5)); // (+5) from Swing 0.6.1
        titles1.add(fieldNameLabel);
        titles1.add(Box.createHorizontalStrut(3));
        titles2.add(Box.createHorizontalStrut(103+5)); // (+5) from Swing 0.6.1
        titles2.add(calcLabel);
        titles1.add(Box.createHorizontalStrut(89));// + 15)); // +15 Because of dialog's width expansion
        titles1.add(typeLabel);
        titles1.add(Box.createHorizontalStrut(61));
        titles2.add(Box.createHorizontalStrut(100+40)); //+50)); //+50 Because of dialog's width expansion
        titles2.add(keyLabel);
        titles1.add(Box.createHorizontalStrut(5+45));  //+35 Because of dialog's width expansion
        titles1.add(editLabel);
        titles1.add(Box.createHorizontalStrut(85));
        titles2.add(Box.createHorizontalStrut(10));
        titles2.add(removLabel);
        titles1.add(formulaLabel);
*/
        BoxLayout bl2 = new BoxLayout(titles1, BoxLayout.X_AXIS);
        titles1.setLayout(bl2);
        titles1.setMaximumSize(new Dimension(dialogWidth, 15));
        titles1.setBackground(titleBackgroundColor);
        BoxLayout bl3 = new BoxLayout(titles2, BoxLayout.X_AXIS);
        titles2.setLayout(bl3);
        titles2.setMaximumSize(new Dimension(dialogWidth, 15));
        titles2.setBackground(titleBackgroundColor);

        titles.add(titles1);
        titles.add(titles2);
        BoxLayout bl4 = new BoxLayout(titles, BoxLayout.Y_AXIS);
        titles.setLayout(bl4);
//        titles.setBorder(new LineBorder(titleBorderColor));
        titles.setMaximumSize(new Dimension(dialogWidth, 34));

//        getContentPane().add(Box.createVerticalStrut(10));
        titlePanel.add(titles);
        titlePanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        getContentPane().add(titlePanel);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setMinimumSize(new Dimension(dialogWidth, 1));
        emptyPanel.setPreferredSize(new Dimension(dialogWidth, 1));
        emptyPanel.setMaximumSize(new Dimension(dialogWidth, 1));
        getContentPane().add(emptyPanel);

        newFieldsPanel = new JPanel();
        BoxLayout bl5 = new BoxLayout(newFieldsPanel, BoxLayout.Y_AXIS);
        newFieldsPanel.setLayout(bl5);

//        newFieldsPanel.setOpaque(false);

        newFieldsScrollPane = new NewTableScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JViewport viewport = newFieldsScrollPane.getViewport();
//        viewport.setSize(dialogWidth-20, 200);
        viewport.setView(newFieldsPanel);
        viewport.setMaximumSize(new Dimension(dialogWidth, 345));
        viewport.setMinimumSize(new Dimension(dialogWidth, 345));
        viewport.setPreferredSize(new Dimension(dialogWidth, 345));
        viewport.setPreferredSize(new Dimension(dialogWidth, 345));
        viewport.setViewPosition(new Point(0, 0));

        newFieldsScrollPane.setMaximumSize(new Dimension(dialogWidth, 345));
        newFieldsScrollPane.setMinimumSize(new Dimension(dialogWidth, 345));
        newFieldsScrollPane.setPreferredSize(new Dimension(dialogWidth, 345));
        newFieldsScrollPane.setBorder(new EtchedBorder());

        getContentPane().add(newFieldsScrollPane);

        JPanel buttonPanel = new JPanel();

//        Font buttonFont = new Font("Dialog", Font.PLAIN, 12);
        Insets buttonInsets = new Insets(0, 0, 0, 0);
        newField = new JButton(infoBundle.getString("NewFieldDialogMsg1"));
        Dimension buttonDimension = new Dimension(100, 30);
//        newField.setFont(buttonFont);
        newField.setMargin(buttonInsets);
        newField.setForeground(new Color(0,0,128));
        newField.setPreferredSize(buttonDimension);
        newField.setMaximumSize(buttonDimension);
        newField.setMinimumSize(buttonDimension);
        newField.setAlignmentY(CENTER_ALIGNMENT);
        newField.setEnabled(false);
        removeField = new JButton(infoBundle.getString("NewTableDialogMsg4"));
        removeField.setMargin(buttonInsets);
//        removeField.setFont(buttonFont);
        removeField.setForeground(new Color(0,0,128));
        removeField.setPreferredSize(buttonDimension);
        removeField.setMaximumSize(buttonDimension);
        removeField.setMinimumSize(buttonDimension);
        removeField.setAlignmentY(CENTER_ALIGNMENT);
        removeField.setEnabled(false);
        clearField = new JButton(infoBundle.getString("NewTableDialogMsg5"));
        clearField.setMargin(buttonInsets);
//        clearField.setFont(buttonFont);
        buttonDimension = new Dimension(115, 30);
        clearField.setForeground(new Color(0,0,128));
        clearField.setPreferredSize(buttonDimension);
        clearField.setMaximumSize(buttonDimension);
        clearField.setMinimumSize(buttonDimension);
        clearField.setAlignmentY(CENTER_ALIGNMENT);
        clearField.setEnabled(false);
        createTable = new JButton(infoBundle.getString("NewTableDialogMsg6"));
        createTable.setMargin(buttonInsets);
//        createTable.setFont(buttonFont);
        buttonDimension = new Dimension(115, 30);
        createTable.setForeground(new Color(0,0,128));
        createTable.setPreferredSize(buttonDimension);
        createTable.setMaximumSize(buttonDimension);
        createTable.setMinimumSize(buttonDimension);
        createTable.setAlignmentY(CENTER_ALIGNMENT);
        createTable.setEnabled(false);
        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMargin(buttonInsets);
//        cancel.setFont(buttonFont);
        buttonDimension = new Dimension(110, 30);
        cancel.setForeground(new Color(0,0,128));
        cancel.setPreferredSize(buttonDimension);
        cancel.setMaximumSize(buttonDimension);
        cancel.setMinimumSize(buttonDimension);
        cancel.setAlignmentY(CENTER_ALIGNMENT);

//        buttonPanel.add(Box.createHorizontalStrut(45));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(newField);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(removeField);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(clearField);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(createTable);
        buttonPanel.add(Box.createGlue());

        BoxLayout bl6 = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(bl6);
        buttonPanel.setMaximumSize(new Dimension(fieldWidth, 50));
        buttonPanel.setMinimumSize(new Dimension(fieldWidth, 50));

        getContentPane().add(buttonPanel);

        //NewField button
        newField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewFieldPanel newfp = new NewFieldPanel(thisDialog);
                NewFieldPanel oldfp = null;
                if (newFieldPanelArray.size() != 0)
                    oldfp = (NewFieldPanel) newFieldPanelArray.at(newFieldPanelArray.size()-1);
                newFieldPanelArray.add(newfp);
                newFieldsPanel.add(newfp);
                newFieldsPanel.add(Box.createVerticalStrut(4));
//                newFieldsPanel.validate();
//                if (newFieldPanelArray.size() > 1)
                newFieldsScrollPane.validate();
//                System.out.println(newfp.getBounds());
                newFieldsPanel.scrollRectToVisible(newfp.getBounds());
                if ((oldfp == null) || (oldfp != null && !oldfp.warningIssued))
                    newfp.fieldNameField.requestFocus();
                newField.setEnabled(false);
                emptyFieldNameExists = true;
/*                if (newFieldPanelArray.size() == 9) {
                    newFieldsScrollPane.validate();
                    newFieldsScrollPane.repaint();
                }
*/            }
        });

        //RemoveField button
        removeField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (activeFieldPanel != null) {
/*                    Component[] c = newFieldsPanel.getComponents();
                    for (int i=0; i<c.length; i++)
                        System.out.println(c[i]);
                    System.out.println("===================================");
*/                    if ((activeFieldPanelIndex+1) == 1)
                        newFieldsPanel.remove(2);
                    else
                        newFieldsPanel.remove((activeFieldPanelIndex+1)*2);
                    newFieldsPanel.remove(activeFieldPanel);
                    newFieldPanelArray.remove(activeFieldPanel);

                    /* If the fieldPanel being removed is the empty one, then set flag
                     * "emptyFieldNameExists" to false.
                     */
                    if (activeFieldPanel.fieldNameField.getText() == null || activeFieldPanel.fieldNameField.getText().trim().length() == 0) {
                        emptyFieldNameExists = false;
                        for (int i=0; i<newFieldPanelArray.size(); i++) {
                            if (((NewFieldPanel) newFieldPanelArray.at(i)).fieldNameField.getText() == null || ((NewFieldPanel) newFieldPanelArray.at(i)).fieldNameField.getText().trim().length() == 0) {
                                emptyFieldNameExists = true;
                                break;
                            }
                        }
                        if (emptyFieldNameExists)
                            newField.setEnabled(false);
                        else
                            newField.setEnabled(true);
                    }

                    /* Find the newFieldPanel to be activated.
                     */
                    if (activeFieldPanelIndex == 0) {
                        if (newFieldPanelArray.size() != 0) {
                            activeFieldPanel = (NewFieldPanel) newFieldPanelArray.at(0);
                        }else{
                            activeFieldPanel = null;
                            activeFieldPanelIndex = -1;
                        }
                    }else{
                        if (activeFieldPanelIndex == newFieldPanelArray.size()) {
                            activeFieldPanel = (NewFieldPanel) newFieldPanelArray.at(newFieldPanelArray.size()-1);
                            activeFieldPanelIndex = newFieldPanelArray.size()-1;
                        }else{
                            activeFieldPanel = (NewFieldPanel) newFieldPanelArray.at(activeFieldPanelIndex);
                            activeFieldPanelIndex--;
                        }
                    }

                    if (activeFieldPanel != null) {
                        activeFieldPanel.setBackground(getBackground());
                        activeFieldPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        activeFieldPanel.fieldNameField.requestFocus();
                    }else{
                        clearField.setEnabled(false);
                        removeField.setEnabled(false);
                        newField.setEnabled(true);
                    }


                    newFieldsScrollPane.validate();
                    if (activeFieldPanel != null)
                        newFieldsPanel.scrollRectToVisible(activeFieldPanel.getBounds());

                    newFieldsPanel.validate();
                    newFieldsPanel.paintImmediately(newFieldsPanel.getVisibleRect());
/*                    c = newFieldsPanel.getComponents();
                    for (int i=0; i<c.length; i++)
                        System.out.println(c[i]);
*/                }
            }
        });

        clearField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activeFieldPanel.clearField();
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thisDialog.removeWindowListener(l);
                thisDialog.clickedButton = 0;
                thisDialog.dispose();
            }
        });

        createTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableName = tf1.getText();
                NewFieldPanel nfp;

//                System.out.println("In NewTableDialog 1");

                fieldNameList.clear();
                calculatedList.clear();
                dataTypeList.clear();
                keyList.clear();
                editableList.clear();
                removableList.clear();
                formulaList.clear();
                for (int i=0; i<newFieldPanelArray.size(); i++) {
                    nfp = (NewFieldPanel) newFieldPanelArray.at(i);

                    if (nfp.fieldNameField.getText() != null && nfp.fieldNameField.getText().trim().length() != 0) {
                        fieldNameList.add(nfp.fieldNameField.getText().trim());
                        calculatedList.add(new Boolean(nfp.calculatedBox.isSelected()));
//                        System.out.println("In createTable: " + nfp.combBox1.getSelectedItem() + nfp.combBox1.getSelectedItem().getClass());
                        Class fldType = (Class) nfp.combBox1.getSelectedItem();
/*                        String type = (String) nfp.combBox1.getSelectedItem();
*/
                        /* Translate the UI name of the data type of the new field
                         * to the proper DBase data type.
                         */
/*                        Enumeration typeKeys = infoBundle.getKeys();
                        String realType = "";
                        while (typeKeys.hasMoreElements()) {
                            realType = (String) typeKeys.nextElement();
                            if (infoBundle.getString(realType).equals(type))
                                break;
                        }

//                      System.out.println(type + ", " + realType);
                        if (realType.equals("Number"))
                            realType = "Double";
                        else if (realType.equals("Alphanumeric"))
                            realType = "String";
                        else if (realType.equals("Boolean (Yes/No)"))
                            realType = "Boolean";
*/
                        dataTypeList.add(fldType); //realType); //nfp.combBox1.getSelectedItem());
                        keyList.add(new Boolean(nfp.keyBox.isSelected()));
                        editableList.add(new Boolean(nfp.editBox.isSelected()));
                        removableList.add(new Boolean(nfp.removBox.isSelected()));
                        formulaList.add(nfp.formulaField.getText());
                    }
                }
//                System.out.println("In NewTableDialog 2");
                thisDialog.removeWindowListener(l);
                thisDialog.clickedButton = 1;
                thisDialog.dispose();
            }
        });

    	l = new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
                thisDialog.removeWindowListener(l);
                thisDialog.clickedButton = 0;
	            thisDialog.dispose();
	        }
	    };
        thisDialog.addWindowListener(l);

        /* Create the first newFieldPanel.
         */
        newFieldsPanel.add(Box.createVerticalStrut(6));
        NewFieldPanel nfp = new NewFieldPanel(this);
        newFieldPanelArray.add(nfp);
        newFieldsPanel.add(nfp);
        newFieldsPanel.add(Box.createVerticalStrut(4));

        /* Dispaying the "NewTableDialog".
         */
        pack();
        setSize(dialogWidth, dialogHeight);
        setResizable(false);
//        setModal(true);

        dbComponent.statusToolbarController.setMessageLabelColor(Color.white);
        if (dbComponent.activeDBTable != null)
            dbComponent.updateNumOfSelectedRecords(dbComponent.activeDBTable);
        dbComponent.statusToolbarController.getToolbar().paintImmediately(dbComponent.statusToolbarController.getToolbar().getBounds());

        Rectangle dbBounds = dbComponent.getBounds();
        System.out.println("getLocationOnScreen() 8");
        java.awt.Point dbLocation = dbComponent.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + dbComponent.getLocationOnScreen());
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
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        tf1.requestFocus();
        setVisible(true);
    }
}


class NewTableScrollPane extends JScrollPane {

    public NewTableScrollPane(int vpc, int hpc) {
        super(vpc, hpc);
    }

    public JScrollBar createVerticalScrollBar() {
        return new NewTableVerticalScrollBar();
    }
}


class NewTableVerticalScrollBar extends JScrollBar
{
    public int getBlockIncrement(int direction) {
        return 36;
    }

    public int getUnitIncrement(int direction) {
        return 36;
    }

    public NewTableVerticalScrollBar() {
        super(JScrollBar.VERTICAL);
   }
}


class NewFieldPanel extends JPanel {
    JTextField fieldNameField, formulaField;
    JCheckBox calculatedBox, keyBox, editBox, removBox;
    JComboBox combBox1;
    NewTableDialog newTableDialog;
    NewFieldPanel thisFieldPanel;
    boolean warningIssued = false;

    public NewFieldPanel(NewTableDialog dialog) {
        super();
        newTableDialog = dialog;
        thisFieldPanel = this;
//        JPanel newFieldPanel = new JPanel();

        final Color activePanelColor = getBackground();
        final Color inactivePanelColor = getBackground();
        final Color textColor = new Color(16, 41, 232); //0, 122, 212)

        Font fieldNameFont = new Font("TimesRoman", Font.PLAIN, 14);
        fieldNameField = new JTextField();
        fieldNameField.setPreferredSize(new Dimension(110, 25));
        fieldNameField.setMaximumSize(new Dimension(110, 25));
        fieldNameField.setAlignmentY(CENTER_ALIGNMENT);
        fieldNameField.setFont(fieldNameFont);
        fieldNameField.setForeground(textColor);
        fieldNameField.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg7"));
        fieldNameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
//                System.out.println("In here " + fieldNameField.getText());
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (!newTableDialog.otherEmptyFieldNamesExist) {
                        newTableDialog.emptyFieldNameExists = false;
                        newTableDialog.newField.setEnabled(true);
                    }
                    if (calculatedBox.isSelected()) {
                        calculatedBox.setEnabled(true);
                        formulaField.setEnabled(true);
                        formulaField.setEditable(true);
                        formulaField.setBackground(Color.white);
                        formulaField.setCaretColor(Color.black);
                        formulaField.setRequestFocusEnabled(true);
                        formulaField.paintImmediately(formulaField.getVisibleRect());
                    }else{
                        combBox1.setEnabled(true);
                        calculatedBox.setEnabled(true);
                        keyBox.setEnabled(true);
                        editBox.setEnabled(true);
                        editBox.setSelected(true);
                        removBox.setEnabled(true);
                        removBox.setSelected(true);
                        formulaField.setCaretColor(inactivePanelColor);
                        formulaField.setRequestFocusEnabled(false);
                    }
                }else{
//                    System.out.println("Disabling combBox1");
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                    combBox1.setEnabled(false);
                    calculatedBox.setEnabled(false);
                    keyBox.setEnabled(false);
                    editBox.setEnabled(false);
                    removBox.setEnabled(false);
                    formulaField.setEnabled(false);
                    formulaField.setBackground(inactivePanelColor);
                    formulaField.setCaretColor(inactivePanelColor);
                    formulaField.setRequestFocusEnabled(false);
                    formulaField.paintImmediately(formulaField.getVisibleRect());
                    formulaField.setEditable(false);
                }
            }
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                        if (!newTableDialog.otherEmptyFieldNamesExist) {
                            newTableDialog.emptyFieldNameExists = false;
                            newTableDialog.newField.setEnabled(true);
                        }
                        if (calculatedBox.isSelected()) {
                            calculatedBox.setEnabled(true);
                            formulaField.setEnabled(true);
                            formulaField.setEditable(true);
                            formulaField.setBackground(Color.white);
                            formulaField.setCaretColor(Color.black);
                            formulaField.setRequestFocusEnabled(true);
                            formulaField.paintImmediately(formulaField.getVisibleRect());
                        }else{
                            combBox1.setEnabled(true);
                            calculatedBox.setEnabled(true);
                            keyBox.setEnabled(true);
                            editBox.setEnabled(true);
                            editBox.setSelected(true);
                            removBox.setEnabled(true);
                            removBox.setSelected(true);
                            formulaField.setCaretColor(inactivePanelColor);
                            formulaField.setRequestFocusEnabled(false);
                        }
                    }else{
//                        System.out.println("Disabling combBox1");
                        newTableDialog.emptyFieldNameExists = true;
                        newTableDialog.newField.setEnabled(false);
                        combBox1.setEnabled(false);
                        calculatedBox.setEnabled(false);
                        keyBox.setEnabled(false);
                        editBox.setEnabled(false);
                        removBox.setEnabled(false);
                        formulaField.setEnabled(false);
                        formulaField.setBackground(inactivePanelColor);
                        formulaField.setCaretColor(inactivePanelColor);
                        formulaField.setRequestFocusEnabled(false);
                        formulaField.paintImmediately(formulaField.getVisibleRect());
                        formulaField.setEditable(false);
                    }
                }
            }
        });
        fieldNameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                combBox1.hidePopup();
                newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                newTableDialog.clearField.setEnabled(true);
                newTableDialog.removeField.setEnabled(true);
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (newTableDialog.emptyFieldNameExists)
                        newTableDialog.newField.setEnabled(false);
                    else
                        newTableDialog.newField.setEnabled(true);
                }else{
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                }

                /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                 * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                 * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                 * status of the "newField" button.
                 */
                newTableDialog.otherEmptyFieldNamesExist = false;
                for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                    if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
//                        System.out.println("Continuing...");
                        continue;
                    }
                    if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                        newTableDialog.otherEmptyFieldNamesExist = true;
//                        System.out.println("Breaking at " + i);
                        break;
                    }
                }

//                System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                    if (newTableDialog.activeFieldPanel != null) {
                        newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                        newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2, 2, 2, 2)); //null);
                        newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                        newTableDialog.activeFieldPanel = null;
                    }

                    newTableDialog.activeFieldPanel = thisFieldPanel;
                    thisFieldPanel.setBackground(activePanelColor);
                    setBorder(new BevelBorder(BevelBorder.LOWERED));
                    thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                }
            }
            public void focusLost(FocusEvent e) {
//                System.out.println("In focus lost fieldName");
                if (fieldNameField.getText() == null || fieldNameField.getText().trim().length() == 0) {
                    /* Disable the rest of the components of the newFieldPanel.
                     */
                    calculatedBox.setSelected(false);
                    calculatedBox.setEnabled(false);
                    keyBox.setSelected(false);
                    keyBox.setEnabled(false);
                    editBox.setSelected(false);
                    editBox.setEnabled(false);
                    removBox.setSelected(false);
                    removBox.setEnabled(false);
                    combBox1.setEnabled(false);
                    formulaField.setEnabled(false);
                    formulaField.setEditable(false);
                    formulaField.setText("");
                    /* Disable the newField button.
                     */
                    newTableDialog.newField.setEnabled(false);
                }else{
/* Check if the name of the field is unique in the jTable.
                     */
                    Array a = newTableDialog.newFieldPanelArray;
                    boolean sameFieldNameFound = false;
                    for (int i=0; i<a.size(); i++) {
                        if (((NewFieldPanel) a.at(i)).equals(thisFieldPanel))
                            continue;
                        if (((NewFieldPanel) a.at(i)).fieldNameField.getText().trim().equals(fieldNameField.getText().trim())) {
                            sameFieldNameFound = true;
                            break;
                        }
                    }

                    if (sameFieldNameFound) {
//                        System.out.println("Error");
                        // PENDING
                        if (!warningIssued) {
                            warningIssued = true;
                            ESlateOptionPane.showMessageDialog(new JFrame(), newTableDialog.infoBundle.getString("NewTableDialogMsg8") + fieldNameField.getText() + newTableDialog.infoBundle.getString("NewTableDialogMsg9"),
                            newTableDialog.infoBundle.getString("Warning"), JOptionPane.WARNING_MESSAGE);
                            fieldNameField.requestFocus();
                        }
                    }else
                        warningIssued = false;
                }
            }
        });

        calculatedBox = new JCheckBox("");
        calculatedBox.setAlignmentY(CENTER_ALIGNMENT);
        calculatedBox.setEnabled(false);
        calculatedBox.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg10"));
        calculatedBox.setMaximumSize(new Dimension(15,15));
        calculatedBox.setMinimumSize(new Dimension(15,15));
        calculatedBox.setPreferredSize(new Dimension(15,15));
        calculatedBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (calculatedBox.isSelected()) {
//1.0.1                    ((javax.swing.basic.BasicComboBoxRenderer) combBox1.getRenderer()).setBackground(Color.lightGray);
                    combBox1.setEnabled(false);
                    editBox.setSelected(false);
                    removBox.setSelected(false);
//                    keyBox.setSelected(false);
//                    keyBox.setEnabled(false);
                    editBox.setEnabled(false);
//                    removBox.setEnabled(false);
                    formulaField.setEnabled(true);
                    formulaField.setBackground(Color.white);
                    formulaField.setEditable(true);
                    formulaField.setCaretColor(Color.black);
                    formulaField.setRequestFocusEnabled(true);
                    formulaField.paintImmediately(formulaField.getVisibleRect());
                }else{
                    combBox1.setEnabled(true);
//                    keyBox.setEnabled(true);
                    editBox.setEnabled(true);
//                    removBox.setEnabled(true);
                    formulaField.setEnabled(false);
                    formulaField.setBackground(inactivePanelColor);
                    formulaField.setEditable(false);
                    formulaField.setCaretColor(inactivePanelColor);
                    formulaField.setRequestFocusEnabled(false);
                    formulaField.paintImmediately(formulaField.getVisibleRect());
                }
            }
        });
        calculatedBox.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                newTableDialog.clearField.setEnabled(true);
                newTableDialog.removeField.setEnabled(true);
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (newTableDialog.emptyFieldNameExists)
                        newTableDialog.newField.setEnabled(false);
                    else
                        newTableDialog.newField.setEnabled(true);
                }else{
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                }

                /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                 * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                 * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                 * status of the "newField" button.
                 */
                newTableDialog.otherEmptyFieldNamesExist = false;
                for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                    if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
                        continue;
                    }
                    if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                        newTableDialog.otherEmptyFieldNamesExist = true;
                        break;
                    }
                }

//                System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                    if (newTableDialog.activeFieldPanel != null) {
                        newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                        newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2,2,2,2));
                        newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                        newTableDialog.activeFieldPanel = null;
                    }

                    newTableDialog.activeFieldPanel = thisFieldPanel;
                    thisFieldPanel.setBackground(activePanelColor);
                    setBorder(new BevelBorder(BevelBorder.LOWERED));
                    thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                }
            }
            public void focusLost(FocusEvent e) {}

        });
        calculatedBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                combBox1.hidePopup();
            }
        });


        combBox1 = new JComboBox();
//        combBox1.addItem("Integer");
/*        combBox1.addItem(newTableDialog.infoBundle.getString("Number"));
        combBox1.addItem(newTableDialog.infoBundle.getString("Alphanumeric"));
        combBox1.addItem(newTableDialog.infoBundle.getString("Boolean (Yes/No)"));
        combBox1.addItem(newTableDialog.infoBundle.getString("Date"));
        combBox1.addItem(newTableDialog.infoBundle.getString("Time"));
        combBox1.addItem(newTableDialog.infoBundle.getString("URL"));
        combBox1.addItem(newTableDialog.infoBundle.getString("Image"));
*/
        combBox1.addItem(Double.class);
        combBox1.addItem(String.class);
        combBox1.addItem(Boolean.class);
        combBox1.addItem(gr.cti.eslate.database.engine.CDate.class);
        combBox1.addItem(gr.cti.eslate.database.engine.CTime.class);
        combBox1.addItem(java.net.URL.class);
        combBox1.addItem(gr.cti.eslate.database.engine.CImageIcon.class);
        combBox1.setAlignmentY(CENTER_ALIGNMENT);
        combBox1.setPreferredSize(new Dimension(170, 32));
        combBox1.setMaximumSize(new Dimension(170, 32));
//        combBox1.setBackground(inactivePanelColor);
        combBox1.setEnabled(false);
        FieldTypeCellRenderer ftcr = new FieldTypeCellRenderer(combBox1, dialog.table, textColor, activePanelColor, newTableDialog.infoBundle);
        combBox1.setRenderer(ftcr);
        combBox1.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg11"));

        combBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                /* Refer to the comments in the "focusGained" method of JTextField "tf1" in
                 * the body of "NewTableDialof() constructor for explanation.
                 */
//Removed from JFC version 0.7
                newTableDialog.timeLastCompoBoxItemChanged = System.currentTimeMillis();
                Class selectedFieldType = (Class) combBox1.getSelectedItem();
//                if (combBox1.getSelectedItem().equals(newTableDialog.infoBundle.getString("Image"))) {
                if (CImageIcon.class.isAssignableFrom(selectedFieldType)) {
                    keyBox.setSelected(false);
                    keyBox.setEnabled(false);
                }else
                    keyBox.setEnabled(true);
            }
        });
        combBox1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {

                /* Refer to the comments in the "focusGained" method of JTextField "tf1" in
                 * the body of "NewTableDialog() constructor for explanation.
                 */
//Removed from JFC version 0.7
                if (System.currentTimeMillis() - newTableDialog.timeLastCompoBoxItemChanged > 200) {
                    newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                    newTableDialog.clearField.setEnabled(true);
                    newTableDialog.removeField.setEnabled(true);
                    if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                        if (newTableDialog.emptyFieldNameExists)
                            newTableDialog.newField.setEnabled(false);
                        else
                            newTableDialog.newField.setEnabled(true);
                    }else{
                        newTableDialog.emptyFieldNameExists = true;
                        newTableDialog.newField.setEnabled(false);
                    }

                    /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                     * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                     * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                     * status of the "newField" button.
                     */
                    newTableDialog.otherEmptyFieldNamesExist = false;
                    for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                        if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
                            continue;
                        }
                        if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                            newTableDialog.otherEmptyFieldNamesExist = true;
                            break;
                        }
                    }

//                    System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                    if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                        if (newTableDialog.activeFieldPanel != null) {
                            newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                            newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2,2,2,2));
                            newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                            newTableDialog.activeFieldPanel = null;
                        }

                        newTableDialog.activeFieldPanel = thisFieldPanel;
                        thisFieldPanel.setBackground(activePanelColor);
                        setBorder(new BevelBorder(BevelBorder.LOWERED));
                        thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                    }
                }
            }
        });

        keyBox = new JCheckBox("");
        keyBox.setAlignmentY(CENTER_ALIGNMENT);
        keyBox.setEnabled(false);
        keyBox.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg12"));
        keyBox.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                newTableDialog.clearField.setEnabled(true);
                newTableDialog.removeField.setEnabled(true);
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (newTableDialog.emptyFieldNameExists)
                        newTableDialog.newField.setEnabled(false);
                    else
                        newTableDialog.newField.setEnabled(true);
                }else{
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                }

                /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                 * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                 * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                 * status of the "newField" button.
                 */
                newTableDialog.otherEmptyFieldNamesExist = false;
                for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                    if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
                        continue;
                    }
                    if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                        newTableDialog.otherEmptyFieldNamesExist = true;
                        break;
                    }
                }

//                System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                    if (newTableDialog.activeFieldPanel != null) {
                        newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                        newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2,2,2,2));
                        newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                        newTableDialog.activeFieldPanel = null;
                    }

                    newTableDialog.activeFieldPanel = thisFieldPanel;
                    thisFieldPanel.setBackground(activePanelColor);
                    setBorder(new BevelBorder(BevelBorder.LOWERED));
                    thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                }
            }
            public void focusLost(FocusEvent e) {}

        });
        keyBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                combBox1.hidePopup();
            }
        });

        editBox = new JCheckBox("");
        editBox.setAlignmentY(CENTER_ALIGNMENT);
        editBox.setEnabled(false);
        editBox.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg13"));
        editBox.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                newTableDialog.clearField.setEnabled(true);
                newTableDialog.removeField.setEnabled(true);
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (newTableDialog.emptyFieldNameExists)
                        newTableDialog.newField.setEnabled(false);
                    else
                        newTableDialog.newField.setEnabled(true);
                }else{
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                }

                /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                 * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                 * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                 * status of the "newField" button.
                 */
                newTableDialog.otherEmptyFieldNamesExist = false;
                for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                    if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
                        continue;
                    }
                    if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                        newTableDialog.otherEmptyFieldNamesExist = true;
                        break;
                    }
                }

//                System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                    if (newTableDialog.activeFieldPanel != null) {
                        newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                        newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2,2,2,2));
                        newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                        newTableDialog.activeFieldPanel = null;
                    }

                    newTableDialog.activeFieldPanel = thisFieldPanel;
                    thisFieldPanel.setBackground(activePanelColor);
                    setBorder(new BevelBorder(BevelBorder.LOWERED));
                    thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                }
            }
            public void focusLost(FocusEvent e) {}

        });
        editBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                combBox1.hidePopup();
            }
        });

        removBox = new JCheckBox("");
        removBox.setAlignmentY(CENTER_ALIGNMENT);
        removBox.setEnabled(false);
        removBox.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg14"));
        removBox.setMaximumSize(new Dimension(15,15));
        removBox.setMinimumSize(new Dimension(15,15));
        removBox.setPreferredSize(new Dimension(15,15));
        removBox.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                newTableDialog.clearField.setEnabled(true);
                newTableDialog.removeField.setEnabled(true);
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (newTableDialog.emptyFieldNameExists)
                        newTableDialog.newField.setEnabled(false);
                    else
                        newTableDialog.newField.setEnabled(true);
                }else{
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                }

                /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                 * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                 * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                 * status of the "newField" button.
                 */
                newTableDialog.otherEmptyFieldNamesExist = false;
                for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                    if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
                        continue;
                    }
                    if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                        newTableDialog.otherEmptyFieldNamesExist = true;
                        break;
                    }
                }

//                System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                    if (newTableDialog.activeFieldPanel != null) {
                        newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                        newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2,2,2,2));
                        newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                        newTableDialog.activeFieldPanel = null;
                    }

                    newTableDialog.activeFieldPanel = thisFieldPanel;
                    thisFieldPanel.setBackground(activePanelColor);
                    setBorder(new BevelBorder(BevelBorder.LOWERED));
                    thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                }
            }
            public void focusLost(FocusEvent e) {}

        });
        removBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                combBox1.hidePopup();
            }
        });

        formulaField = new JTextField();
        formulaField.setPreferredSize(new Dimension(223, 25));
        formulaField.setMaximumSize(new Dimension(223, 25));
        formulaField.setAlignmentY(CENTER_ALIGNMENT);
        formulaField.setEnabled(false);
        formulaField.setEditable(false);
        formulaField.setBackground(inactivePanelColor);
        formulaField.setForeground(textColor);
        formulaField.setFont(fieldNameFont);
        formulaField.setToolTipText(newTableDialog.infoBundle.getString("NewTableDialogMsg15"));
        formulaField.setCaretColor(inactivePanelColor);
        formulaField.setRequestFocusEnabled(false);
        formulaField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                newTableDialog.activeFieldPanelIndex = newTableDialog.newFieldPanelArray.indexOf(thisFieldPanel);
                newTableDialog.clearField.setEnabled(true);
                newTableDialog.removeField.setEnabled(true);
                if (fieldNameField.getText() != null && fieldNameField.getText().trim().length() != 0) {
                    if (newTableDialog.emptyFieldNameExists)
                        newTableDialog.newField.setEnabled(false);
                    else
                        newTableDialog.newField.setEnabled(true);
                }else{
                    newTableDialog.emptyFieldNameExists = true;
                    newTableDialog.newField.setEnabled(false);
                }

                /* Check if other "newFieldPanel"s with empty "fieldNameField"s exist. If so,
                 * then set variable "otherEmptyFieldNamesExist" so that the "KeyTyped" events
                 * in this "newFieldPanel"'s "fieldNameField" component won't change the disabled
                 * status of the "newField" button.
                 */
                newTableDialog.otherEmptyFieldNamesExist = false;
                for (int i=0; i<newTableDialog.newFieldPanelArray.size(); i++) {
                    if (((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).equals(thisFieldPanel)) {
                        continue;
                    }
                    if (!((NewFieldPanel) newTableDialog.newFieldPanelArray.at(i)).calculatedBox.isEnabled()) {
                        newTableDialog.otherEmptyFieldNamesExist = true;
                        break;
                    }
                }

//                System.out.println("Active panel index: " + newTableDialog.activeFieldPanelIndex);
                if (newTableDialog.activeFieldPanel == null || newTableDialog.activeFieldPanel != thisFieldPanel) {
                    if (newTableDialog.activeFieldPanel != null) {
                        newTableDialog.activeFieldPanel.setBackground(inactivePanelColor);
                        newTableDialog.activeFieldPanel.setBorder(new EmptyBorder(2,2,2,2));
                        newTableDialog.activeFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                        newTableDialog.activeFieldPanel = null;
                    }

                    newTableDialog.activeFieldPanel = thisFieldPanel;
                    thisFieldPanel.setBackground(activePanelColor);
                    setBorder(new BevelBorder(BevelBorder.LOWERED));
                    thisFieldPanel.paintImmediately(thisFieldPanel.getVisibleRect());
                }
            }
            public void focusLost(FocusEvent e) {}

        });


        /* Column blanks.
         */
        int name_calc = 5;
        int calc_type = 3;
        int type_key = 5;
        int key_edit = 3;
        int edit_remov = 3;
        int remov_form = 5;

        add(Box.createHorizontalStrut(3));
        add(fieldNameField);
        add(Box.createHorizontalStrut(name_calc));
        add(calculatedBox);
        add(Box.createHorizontalStrut(calc_type));
        add(combBox1);
        add(Box.createHorizontalStrut(type_key));
        add(keyBox);
        add(Box.createHorizontalStrut(key_edit));
        add(editBox);
        add(Box.createHorizontalStrut(edit_remov));
        add(removBox);
        add(Box.createHorizontalStrut(remov_form));
        add(formulaField);

        BoxLayout bl5 = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(bl5);
        setMinimumSize(new Dimension(dialog.fieldWidth-9, 34));
        setPreferredSize(new Dimension(dialog.fieldWidth-9, 34));
        setMaximumSize(new Dimension(dialog.fieldWidth-9, 34));
        setAlignmentY(CENTER_ALIGNMENT);
//        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setOpaque(true);
        setBorder(new EmptyBorder(2, 2, 2, 2));

    }

    protected void clearField() {
        fieldNameField.setText("");
        calculatedBox.setSelected(false);
        calculatedBox.setEnabled(false);
        editBox.setSelected(false);
        editBox.setEnabled(false);
        keyBox.setSelected(false);
        keyBox.setEnabled(false);
        removBox.setSelected(false);
        removBox.setEnabled(false);
        combBox1.setEnabled(false);
        formulaField.setText("");
        formulaField.setEnabled(false);
        formulaField.setBackground(getBackground());
        formulaField.setCaretColor(getBackground());
        formulaField.setEditable(false);
        formulaField.setRequestFocusEnabled(false);
        newTableDialog.clearField.setEnabled(false);
        newTableDialog.newField.setEnabled(false);
        fieldNameField.requestFocus();
    }
}