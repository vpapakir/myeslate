package gr.cti.eslate.database;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import gr.cti.eslate.database.engine.*;
import gr.cti.typeArray.StringBaseArray;
import gr.cti.typeArray.BoolBaseArray;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

import com.zookitec.layout.*;


class TableSortDialog extends JDialog {
    /**
     * The return code of the dialog when the ok button is pressed.
     * @see #getReturnCode()
     */
    public static final int DIALOG_OK = 0;
    /**
     * The return code of the dialog when it closes in any other way than pressing the ok button.
     * @see #getReturnCode()
     */
    public static final int DIALOG_CANCEL = 1;

    DefaultListModel sortOnFieldListModel, fieldListModel;
    JButton addButton, remButton, cancel;
    JList fieldList, sortOnFieldList;
    Table table = null;

    /** The array of fields the table will be sorted on. */
    TableFieldBaseArray fieldsToSortOn = new TableFieldBaseArray();
    /** The order of the sorting. This array contains one boolean balue for each selected field. */
    BoolBaseArray ascending = new BoolBaseArray();
    int returnCode = DIALOG_CANCEL;
    /** The asecnding and descending icons for the checkboxes of the sort direction. */
    ImageIcon ascIcon = null, descIcon = null;
    /** The array of the sort direction check boxes. */
    ArrayList sortCheckBoxes = new ArrayList();
    /** The panel which contains the sort direction check boxes.*/
    private JPanel sortDirectionPanel = null;

    protected TableSortDialog(Frame frame, DBTable dbTable/*, Database dbComponent*/) {
        super(frame, true);
        this.table = dbTable.table;
        setResizable(true);
        setTitle(Database.infoBundle.getString("TableSortDialogMsg1"));
        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	this.getRootPane().WHEN_IN_FOCUSED_WINDOW);


        fieldsToSortOn.clear();
        ascending.clear();

        Color titleBorderColor = new Color(119, 40, 104);
//        JPanel sortOnFieldsPanel = new JPanel(true);
//        BoxLayout bl5 = new BoxLayout(sortOnFieldsPanel, BoxLayout.Y_AXIS);
//        sortOnFieldsPanel.setLayout(bl5);

        fieldListModel = new DefaultListModel();
        fieldList = new JList(fieldListModel);
        fieldList.setFixedCellHeight(19);

        /* Fill the "fieldListModel". Image fields are excluded, because sort cannot be
         * performed on them.
         */
        StringBaseArray fieldNames = dbTable.table.getFieldNames();
        AbstractTableField fld;
        for (int i=0; i<fieldNames.size(); i++) {
            try{
                fld = dbTable.table.getTableField(fieldNames.get(i));
            }catch (InvalidFieldNameException e) {
                System.out.println("Serious inconsistency error in TableSortDialog TableSosrtDialog() : (1)");
                continue;
            }
            if (fld.getDataType().equals(gr.cti.eslate.database.engine.CImageIcon.class)) {
                fieldNames.remove(i);
                i--;
            }
        }
        // Sort the remaining field names and add them to the 'fieldList'
        String[] fieldNameArr = fieldNames.toArray();
        Arrays.sort(fieldNameArr);
        for (int i =0; i<fieldNameArr.length; i++)
            fieldListModel.addElement(fieldNameArr[i]);

        fieldList.setBackground(Color.white);
        fieldList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (fieldList.isSelectionEmpty())
                    addButton.setEnabled(false);
                else
                    addButton.setEnabled(true);
            }
        });

        fieldList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
//                int index = fieldList.locationToIndex(e.getPoint());
                if (e.getClickCount() == 2)
                    addSortFields();
            }
        });

        JScrollPane scrollPane = new JScrollPane(fieldList);
        TitledBorder tb1 = new TitledBorder(Database.infoBundle.getString("TableSortDialogMsg5") + Database.infoBundle.getString("TableSortDialogMsg4") + dbTable.table.getTitle() + "\"");
        tb1.setTitleColor(titleBorderColor);
        scrollPane.setBorder(tb1);

        addButton = new JButton(new ImageIcon(getClass().getResource("images/arrows/rightArrow.gif"))); //arrob3er.gif")));
        addButton.setAlignmentX(CENTER_ALIGNMENT);
        addButton.setMargin(new Insets(3,2,3,2));
//        addButton.setMaximumSize(new Dimension(48, 35));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSortFields();
            }
        });
        addButton.setEnabled(false);
        remButton = new JButton(new ImageIcon(getClass().getResource("images/arrows/leftArrow.gif"))); //arrob3eg.gif")));
        remButton.setAlignmentX(CENTER_ALIGNMENT);
        remButton.setMargin(new Insets(3,2,3,2));
//        remButton.setMaximumSize(new Dimension(48, 35));
        remButton.setEnabled(false);
        remButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSortFields();
            }
        });


        JPanel arrowButtonPanel = new JPanel();
        arrowButtonPanel.setLayout(new ExplicitLayout());
        EmptyBorder arrowButtonPanelBorder = new EmptyBorder(0, 5, 0, 5);
        arrowButtonPanel.setBorder(arrowButtonPanelBorder);

        ExplicitConstraints addButtonCstrs = new ExplicitConstraints(addButton);
        addButtonCstrs.setX(ContainerEF.left(arrowButtonPanel));
        addButtonCstrs.setY(ContainerEF.top(arrowButtonPanel));
        addButtonCstrs.setWidth(ContainerEF.width(arrowButtonPanel));
        arrowButtonPanel.add(addButton, addButtonCstrs);
        ExplicitConstraints remButtonCstrs = new ExplicitConstraints(remButton);
        remButtonCstrs.setWidth(ContainerEF.width(arrowButtonPanel));
        remButtonCstrs.setX(ComponentEF.left(addButton));
        Expression arrowButtonVertDistExp = MathEF.constant(10);
        remButtonCstrs.setY(ComponentEF.top(addButton).add(ComponentEF.height(addButton).add(arrowButtonVertDistExp)));
        arrowButtonPanel.add(remButton, remButtonCstrs);

        sortOnFieldListModel = new DefaultListModel();
        sortOnFieldList = new JList(sortOnFieldListModel);
        sortOnFieldList.setBackground(Color.white);
        sortOnFieldList.setFixedCellHeight(19);
        sortOnFieldList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (sortOnFieldList.isSelectionEmpty())
                    remButton.setEnabled(false);
                else
                    remButton.setEnabled(true);
            }
        });

        sortOnFieldList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
//                int index = sortOnFieldList.locationToIndex(e.getPoint());
                if (e.getClickCount() == 2)
                    removeSortFields();
            }
        });

        JScrollPane scrollPane2 = new JScrollPane(sortOnFieldList);
        TitledBorder tb2 = new TitledBorder(Database.infoBundle.getString("TableSortDialogMsg6"));
        tb2.setTitleColor(titleBorderColor);
        scrollPane2.setBorder(tb2);

        sortDirectionPanel = new JPanel();
        sortDirectionPanel.setLayout(new BoxLayout(sortDirectionPanel, BoxLayout.Y_AXIS));
        sortDirectionPanel.add(Box.createVerticalStrut(2));
        TitledBorder tb3 = new TitledBorder(Database.infoBundle.getString("TableSortDialogMsg7"));
        tb3.setTitleColor(titleBorderColor);
        tb3.setTitleJustification(tb3.CENTER);
        sortDirectionPanel.setBorder(tb3);

        JPanel mainPanel = new JPanel(true);
        ExplicitLayout mainEl = new ExplicitLayout();
        mainPanel.setLayout(mainEl);

        ExplicitConstraints ec1 = new ExplicitConstraints(scrollPane);
        ec1.setX(ContainerEF.left(mainPanel));
        ec1.setY(ContainerEF.top(mainPanel));
        ec1.setHeight(ContainerEF.height(mainPanel));
        Expression exp = ContainerEF.width(mainPanel).subtract(ComponentEF.width(arrowButtonPanel)).subtract(ComponentEF.width(sortDirectionPanel)).divide(2d);
        ec1.setWidth(exp);
        mainPanel.add(scrollPane, ec1);

        ExplicitConstraints ec2 = new ExplicitConstraints(arrowButtonPanel);
        ec2.setOriginY(ExplicitConstraints.CENTER);
        ec2.setY(ContainerEF.heightFraction(mainPanel, 0.5d));
        ec2.setWidth(ComponentEF.preferredWidth(addButton).add(MathEF.constant(arrowButtonPanelBorder.getBorderInsets().left + arrowButtonPanelBorder.getBorderInsets().right)));
        ec2.setOriginX(ExplicitConstraints.RIGHT);
        ec2.setX(ComponentEF.left(scrollPane2));
        ec2.setHeight(ComponentEF.preferredHeight(addButton).add(ComponentEF.preferredHeight(remButton)).add(arrowButtonVertDistExp));
        mainPanel.add(arrowButtonPanel, ec2);

        ExplicitConstraints ec3 = new ExplicitConstraints(scrollPane2);
        ec3.setHeight(ComponentEF.height(scrollPane));
        ec3.setOriginX(ExplicitConstraints.RIGHT);
        ec3.setX(ComponentEF.left(sortDirectionPanel));
        ec3.setWidth(ComponentEF.width(scrollPane));
        mainPanel.add(scrollPane2, ec3);

        ExplicitConstraints ec4 = new ExplicitConstraints(sortDirectionPanel);
        ec4.setHeight(ComponentEF.height(scrollPane));
        int minWidth = tb3.getMinimumSize(sortDirectionPanel).width;
        ec4.setOriginX(ExplicitConstraints.RIGHT);
        ec4.setX(ContainerEF.right(mainPanel));
        ec4.setWidth(MathEF.constant(minWidth));
        mainPanel.add(sortDirectionPanel, ec4);

        // The button panel
        JButton ok = new JButton(dbTable.bundle.getString("OK"));
        ok.setMaximumSize(new Dimension(100, 30));
        ok.setPreferredSize(new Dimension(100, 30));
        ok.setMinimumSize(new Dimension(100, 30));
        ok.setEnabled(true);
        ok.setForeground(new Color(0,0,128));

        cancel = new JButton(dbTable.bundle.getString("Cancel"));
        cancel.setMaximumSize(new Dimension(100, 30));
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.setMinimumSize(new Dimension(100, 30));
        cancel.setEnabled(true);
        cancel.setForeground(new Color(0,0,128));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(ok);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue());
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 3));

        // The panel with all the contents of the dialog
        JPanel rootPanel = new JPanel();
        ExplicitLayout rootEl = new ExplicitLayout();
        rootPanel.setLayout(rootEl);
        rootPanel.setBorder(new EmptyBorder(5, 0, 13, 3));
        ExplicitConstraints butPanelCnstrs = new ExplicitConstraints(buttonPanel);
        butPanelCnstrs.setOriginY(ExplicitConstraints.BOTTOM);
        butPanelCnstrs.setY(ContainerEF.bottom(rootPanel));
        butPanelCnstrs.setWidth(ContainerEF.width(rootPanel));
        rootPanel.add(buttonPanel, butPanelCnstrs);

        ExplicitConstraints mainPanelCnstrs = new ExplicitConstraints(mainPanel);
        mainPanelCnstrs.setOriginY(ExplicitConstraints.BOTTOM);
        mainPanelCnstrs.setY(ComponentEF.top(buttonPanel));
        mainPanelCnstrs.setX(ComponentEF.left(buttonPanel));
        mainPanelCnstrs.setWidth(ContainerEF.width(rootPanel));
        mainPanelCnstrs.setHeight(ContainerEF.height(rootPanel).subtract(ComponentEF.height(buttonPanel)));
        rootPanel.add(mainPanel, mainPanelCnstrs);

        setContentPane(rootPanel);

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    for (int i=0; i<((DefaultListModel) sortOnFieldList.getModel()).size(); i++)
                        fieldsToSortOn.add(table.getTableField((String) sortOnFieldList.getModel().getElementAt(i)));
                }catch (Throwable thr) {
                    thr.printStackTrace();
                    fieldsToSortOn.clear();
                }

                for (int i=0; i<sortCheckBoxes.size(); i++)
                    ascending.add(((JCheckBox) sortCheckBoxes.get(i)).isSelected());
                returnCode = DIALOG_OK;
	            dispose();
	        }
	    });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	            dispose();
	        }
	    });

        /* Add the window listener.
         */
    	addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
//                thisDialog.removeWindowListener(l);
	            dispose();
	        }
	    });

        /* Dispaying the "TableSortDialog".
         */
        pack();
        setSize(500, 300);
        Rectangle dbBounds = dbTable.getBounds();
        java.awt.Point dbLocation = dbTable.getLocationOnScreen();
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

    /**
     * Returns the way the dialog was closed.
     * @see #DIALOG_OK
     * @see #DIALOG_CANCEL
     * @return
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Returns the list of the TableFields on which the table will be sorted.
     * @return
     */
    public TableFieldBaseArray getFieldsToSortOn() {
        return fieldsToSortOn;
    }

    /**
     * Returns the a list with the direction each field should be sorted.
     * @return
     */
    public BoolBaseArray getSortDirections() {
        return ascending;
    }

    private JCheckBox createSortDirectionBox() {
        if (ascIcon == null)
            ascIcon = new ImageIcon(getClass().getResource("images/thumbUp.gif"));
        if (descIcon == null)
            descIcon = new ImageIcon(getClass().getResource("images/thumbDown.gif"));
        JCheckBox box = new JCheckBox(descIcon);
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        box.setSelectedIcon(ascIcon);
        box.setEnabled(true);
        box.setMargin(new Insets(0, 0, 0, 0));
        box.setAlignmentX(CENTER_ALIGNMENT);
        return box;
    }

    /** Moves the selected field names from the 'fieldList' to the 'sortOnFieldList'. */
    private void addSortFields() {
        int[] selectedFieldIndices = fieldList.getSelectedIndices();
        if (selectedFieldIndices.length != 0) {
            fieldList.clearSelection();
            sortOnFieldList.clearSelection();
        }else
            return;

        for (int i=0; i<selectedFieldIndices.length; i++) {
            int index = selectedFieldIndices[i];
            String fieldName = (String) fieldListModel.getElementAt(index);

            sortOnFieldListModel.addElement(fieldName);

            JCheckBox sortDirectionBox = createSortDirectionBox();
            sortDirectionPanel.add(sortDirectionBox);
            sortCheckBoxes.add(sortDirectionBox);
        }

        // Remove the field names from the 'fieldList'
        for (int i=selectedFieldIndices.length-1; i>=0; i--)
            fieldListModel.remove(selectedFieldIndices[i]);

        // New selection for the 'fieldList'
        if (fieldListModel.size() > selectedFieldIndices[0])
            fieldList.setSelectedIndex(selectedFieldIndices[0]);
        else
            fieldList.setSelectedIndex(fieldListModel.size()-1);

        // Select all the moves field names in the 'sortOnFieldList'.
        int[] newSelectionIndicesInFieldList = new int[selectedFieldIndices.length];
        for (int i=0; i<newSelectionIndicesInFieldList.length; i++)
            newSelectionIndicesInFieldList[i] = sortOnFieldListModel.size()-1-i;
        sortOnFieldList.setSelectedIndices(newSelectionIndicesInFieldList);

        // Repaints
        fieldList.revalidate();
        fieldList.repaint();
        sortOnFieldList.revalidate();
        sortOnFieldList.repaint();
        sortDirectionPanel.revalidate();
        sortDirectionPanel.repaint();
    }

    /** Removes the fields which are selected in the 'sortOnFieldList' and adds them to the 'fieldList'. */
    private void removeSortFields() {
        int[] selectedFieldIndices = sortOnFieldList.getSelectedIndices();
        if (selectedFieldIndices.length != 0) {
            fieldList.clearSelection();
            sortOnFieldList.clearSelection();
        }else
            return;

        StringBaseArray removedFieldNames = new StringBaseArray();
        int index = -1;
        for (int i=selectedFieldIndices.length-1; i>=0; i--) {
            index = selectedFieldIndices[i];
            String fieldName = (String) sortOnFieldListModel.getElementAt(index);

            sortOnFieldListModel.remove(index);
            removedFieldNames.add(fieldName);

            JCheckBox checkBox = (JCheckBox) sortCheckBoxes.get(index);
//            System.out.println("index: " + index + ", checkBox==null? " + (checkBox==null));
            sortDirectionPanel.remove(checkBox);
            sortCheckBoxes.remove(index);
        }
        // New selection for the 'sortOnFieldList'
        if (index != -1) {
            if (!sortOnFieldListModel.isEmpty()) {
                if (sortOnFieldListModel.size() == index)
                    sortOnFieldList.setSelectedIndex(index-1);
                else
                    sortOnFieldList.setSelectedIndex(index);
            }
        }

        // 'removeFieldNameOrigSize' is used to mark where the remove fields end in the 'removedFieldNames' array, since
        // all the fields of the 'fieldList' will be added to it.
        int removeFieldNameOrigSize = removedFieldNames.size();
        // 'removedFieldNames' contains all the fields which must be added to the 'fieldList'. The contents of the
        // 'fieldList' must be sorted after the new field names are entered. To do this we add all the existing field
        // names of the 'fieldList' to 'removedFieldNames', then we sort the list and re-fill the 'fieldList'
        for (int i=0; i<fieldListModel.size(); i++) {
            removedFieldNames.add((String) fieldListModel.get(i));
        }

        String[] fieldNameArr = removedFieldNames.toArray();
        Arrays.sort(fieldNameArr);
        fieldListModel.clear();
//        fieldList.setListData(fieldNameArr);
        fieldListModel = new DefaultListModel();
        for (int i=0; i<fieldNameArr.length; i++)
            fieldListModel.addElement(fieldNameArr[i]);
        fieldList.setModel(fieldListModel);

        // Select all the moves field names in the 'fieldList'.
        int[] newSelectionIndicesInFieldList = new int[selectedFieldIndices.length];
        for (int i=0; i<removeFieldNameOrigSize; i++)
            newSelectionIndicesInFieldList[i] = fieldListModel.indexOf(removedFieldNames.get(i));
        fieldList.setSelectedIndices(newSelectionIndicesInFieldList);

        // repaints
        fieldList.repaint();
        sortOnFieldList.repaint();
        sortDirectionPanel.revalidate();
        sortDirectionPanel.repaint();
    }
}


