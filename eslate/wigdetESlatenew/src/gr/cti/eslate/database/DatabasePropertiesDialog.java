package gr.cti.eslate.database;

import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Frame;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.beans.PropertyVetoException;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.database.engine.CTime;
import gr.cti.eslate.database.engine.Table;
import gr.cti.eslate.database.engine.AbstractTableField;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.InvalidFieldIndexException;
import com.objectspace.jgl.HashMap;
import com.objectspace.jgl.HashMapIterator;
import com.objectspace.jgl.Pair;


class DatabasePropertiesDialog extends JDialog {
    DatabasePropertiesDialog thisDialog;
    JTextField nameField, surnameField;
    JLabel dateLabel, lastModifiedLabel;
    JCheckBox tableRemoval, tableAddition, tableExportation, displayHiddenTables;
    JCheckBox recordAddition, recordRemoval, fieldAddition, fieldRemoval, fieldReorder, dataChange, keyChange, hiddenChange, displayHiddenFields, editFieldProperties;
    JCheckBox tableRename;
    JCheckBox dataChange2, fieldRemoval2, keyChange2, calculatedReset, typeChange, hiddenChange2, formulaChange;
    JButton ok, cancel, protect; //, tableProperties;
    JPanel propertiesPanel;
    DatabasePropertiesTreePanel databasePropertiesTreePanel;

    transient ResourceBundle infoBundle;
    Database database;
    HashMap dbProperties;
    private static String password = "";

    protected DatabasePropertiesDialog(Frame frame, Database db) {
        super(frame, true);
        thisDialog = this;
        infoBundle = db.infoBundle;
        this.database = db;
        setTitle(infoBundle.getString("DatabasePropertiesDialogMsg1"));

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);


        Color color128 = new Color(0, 0, 128);
        Color titleBorderColor = new Color(119, 40, 104);

/*
        Font dialogFont, titleFont;
        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) {
            dialogFont = new Font("Dialog", Font.PLAIN, 12);
            titleFont = new Font("Dialog", Font.BOLD, 16);
        }else{
            dialogFont = new Font("Helvetica", Font.PLAIN, 12);
            titleFont = new Font("Helvetica", Font.BOLD, 16);
        }
*/

//        FontMetrics fm = getToolkit().getFontMetrics(dialogFont);
//        FontMetrics fmBig = getToolkit().getFontMetrics(titleFont);

        Dimension d = new Dimension(150, 23);
        JLabel nameLabel = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg2"));
//        nameLabel.setFont(dialogFont);

        nameField = new JTextField();
//        nameField.setFont(dialogFont);
        nameField.setMaximumSize(d);
        nameField.setMinimumSize(d);
        nameField.setPreferredSize(d);

        JPanel namePanel = new JPanel(true);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.add(Box.createGlue());
        namePanel.add(nameLabel);
        namePanel.add(Box.createHorizontalStrut(5));
        namePanel.add(nameField);

        JLabel surnameLabel = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg3"));
//        surnameLabel.setFont(dialogFont);

        surnameField = new JTextField();
//        surnameField.setFont(dialogFont);
        surnameField.setMaximumSize(d);
        surnameField.setMinimumSize(d);
        surnameField.setPreferredSize(d);

        JPanel surnamePanel = new JPanel(true);
        surnamePanel.setLayout(new BoxLayout(surnamePanel, BoxLayout.X_AXIS));
        surnamePanel.add(Box.createGlue());
        surnamePanel.add(surnameLabel);
        surnamePanel.add(Box.createHorizontalStrut(5));
        surnamePanel.add(surnameField);

        JLabel dateCreated = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg4"));
//        dateCreated.setFont(dialogFont);

        dateLabel = new JLabel();
//        dateLabel.setFont(dialogFont);
        dateLabel.setBorder(nameField.getBorder());
        dateLabel.setMinimumSize(d);
        dateLabel.setMaximumSize(d);
        dateLabel.setPreferredSize(d);

        JPanel datePanel = new JPanel(true);
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.add(Box.createGlue());
        datePanel.add(dateCreated);
        datePanel.add(Box.createHorizontalStrut(5));
        datePanel.add(dateLabel);

        JLabel lastModified = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg6"));
//        lastModified.setFont(dialogFont);

        lastModifiedLabel = new JLabel();
//        lastModifiedLabel.setFont(dialogFont);
        lastModifiedLabel.setBorder(nameField.getBorder());
        lastModifiedLabel.setMinimumSize(d);
        lastModifiedLabel.setMaximumSize(d);
        lastModifiedLabel.setPreferredSize(d);

        JPanel lastModifiedPanel = new JPanel(true);
        lastModifiedPanel.setLayout(new BoxLayout(lastModifiedPanel, BoxLayout.X_AXIS));
        lastModifiedPanel.add(Box.createGlue());
        lastModifiedPanel.add(lastModified);
        lastModifiedPanel.add(Box.createHorizontalStrut(5));
        lastModifiedPanel.add(lastModifiedLabel);

        JPanel firstColumn = new JPanel(true);
        firstColumn.setLayout(new GridLayout(2,1,5,5));
        firstColumn.add(namePanel);
        firstColumn.add(datePanel);

        JPanel secondColumn = new JPanel(true);
        secondColumn.setLayout(new GridLayout(2,1,5,5));
        secondColumn.add(surnamePanel);
        secondColumn.add(lastModifiedPanel);

        JPanel creatorPanel = new JPanel(true);
        creatorPanel.setLayout(new BoxLayout(creatorPanel, BoxLayout.X_AXIS));

        creatorPanel.add(firstColumn);
        creatorPanel.add(Box.createHorizontalStrut(10));
        creatorPanel.add(secondColumn);

        TitledBorder tb = new TitledBorder(infoBundle.getString("DatabasePropertiesDialogMsg8"));
        tb.setTitleColor(titleBorderColor);

//        if (!infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR")) //jTable.dbComponent.getLocale().toString().equals("el_GR")) {
//            tb.setTitleFont(new Font("Dialog", Font.PLAIN, 12));
//        else
//            tb.setTitleFont(new Font("Helvetica", Font.PLAIN, 12));

        creatorPanel.setBorder(new CompoundBorder(tb, new EmptyBorder(0, 0, 3, 0)));

        databasePropertiesTreePanel = new DatabasePropertiesTreePanel(database);
        d = new Dimension(200, 300);
        databasePropertiesTreePanel.setPreferredSize(d);
        databasePropertiesTreePanel.setMinimumSize(d);

        // Database properties
        tableRemoval = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg9"));
//        tableRemoval.setFont(dialogFont);
        tableRemoval.setAlignmentY(BOTTOM_ALIGNMENT);

        tableAddition = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg10"));
//        tableAddition.setFont(dialogFont);
        tableAddition.setAlignmentY(BOTTOM_ALIGNMENT);

        tableExportation = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg11"));
//        tableExportation.setFont(dialogFont);
        tableExportation.setAlignmentY(BOTTOM_ALIGNMENT);

        displayHiddenTables = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg12"));
//        displayHiddenTables.setFont(dialogFont);
        displayHiddenTables.setAlignmentY(BOTTOM_ALIGNMENT);

        FontMetrics fm = tableRemoval.getFontMetrics(tableRemoval.getFont());
        int width = fm.stringWidth(tableRemoval.getText());
        int nextChBoxWidth = fm.stringWidth(tableAddition.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(tableExportation.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(displayHiddenTables.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        d = new Dimension(width+17, 27);

        JPanel tableRemovalPanel = new JPanel(true);
        tableRemovalPanel.setLayout(new BoxLayout(tableRemovalPanel, BoxLayout.X_AXIS));
        tableRemovalPanel.add(tableRemoval);
        tableRemovalPanel.setMaximumSize(d);
        tableRemovalPanel.setMinimumSize(d);
        tableRemovalPanel.setPreferredSize(d);

        JPanel tableAdditionPanel = new JPanel(true);
        tableAdditionPanel.setLayout(new BoxLayout(tableAdditionPanel, BoxLayout.X_AXIS));
        tableAdditionPanel.add(tableAddition);
        tableAdditionPanel.setMaximumSize(d);
        tableAdditionPanel.setMinimumSize(d);
        tableAdditionPanel.setPreferredSize(d);

        JPanel tableExportationPanel = new JPanel(true);
        tableExportationPanel.setLayout(new BoxLayout(tableExportationPanel, BoxLayout.X_AXIS));
        tableExportationPanel.add(tableExportation);
        tableExportationPanel.setMaximumSize(d);
        tableExportationPanel.setMinimumSize(d);
        tableExportationPanel.setPreferredSize(d);

        JPanel displayHiddenTablesPanel = new JPanel(true);
        displayHiddenTablesPanel.setLayout(new BoxLayout(displayHiddenTablesPanel, BoxLayout.X_AXIS));
        displayHiddenTablesPanel.add(displayHiddenTables);
        displayHiddenTablesPanel.setMaximumSize(d);
        displayHiddenTablesPanel.setMinimumSize(d);
        displayHiddenTablesPanel.setPreferredSize(d);

        JLabel databasePropertiesLabel = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg24"));
        databasePropertiesLabel.setFont(databasePropertiesLabel.getFont().deriveFont(Font.BOLD)/*titleFont*/);
        databasePropertiesLabel.setForeground(new Color(0, 0, 128));
        databasePropertiesLabel.setAlignmentY(CENTER_ALIGNMENT);

        JPanel titlePanel = new JPanel(true);
        d = new Dimension(1000, 30);
        titlePanel.setMaximumSize(d);

        titlePanel.add(Box.createGlue());
        titlePanel.add(databasePropertiesLabel);
        titlePanel.add(Box.createGlue());
//        titlePanel.setBorder(new LineBorder(Color.black));

        JPanel databasePropertiesPanel = new JPanel(true);
        databasePropertiesPanel.setLayout(new BoxLayout(databasePropertiesPanel, BoxLayout.Y_AXIS));
        databasePropertiesPanel.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(0,10,0,10))); // new CompoundBorder(new CompoundBorder(databasePropertiesTreePanel.scrollpane.getBorder(), new SoftBevelBorder(SoftBevelBorder.LOWERED)), new EmptyBorder(0,10,0,10)));

        databasePropertiesPanel.add(titlePanel);
        databasePropertiesPanel.add(tableRemovalPanel);
        databasePropertiesPanel.add(tableAdditionPanel);
        databasePropertiesPanel.add(tableExportationPanel);
        databasePropertiesPanel.add(displayHiddenTablesPanel);

        //Table properties...
        tableRename = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg37"));
//        tableRename.setFont(dialogFont);
        tableRename.setAlignmentY(BOTTOM_ALIGNMENT);

        recordAddition = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg17"));
//        recordAddition.setFont(dialogFont);
        recordAddition.setAlignmentY(BOTTOM_ALIGNMENT);

        recordRemoval = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg18"));
//        recordRemoval.setFont(dialogFont);
        recordRemoval.setAlignmentY(BOTTOM_ALIGNMENT);

        fieldAddition = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg19"));
//        fieldAddition.setFont(dialogFont);
        fieldAddition.setAlignmentY(BOTTOM_ALIGNMENT);

        fieldRemoval = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg20"));
//        fieldRemoval.setFont(dialogFont);
        fieldRemoval.setAlignmentY(BOTTOM_ALIGNMENT);

        fieldReorder = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg21"));
//        fieldReorder.setFont(dialogFont);
        fieldReorder.setAlignmentY(BOTTOM_ALIGNMENT);

        editFieldProperties = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg36"));
//        editFieldProperties.setFont(dialogFont);
        editFieldProperties.setAlignmentY(BOTTOM_ALIGNMENT);

        dataChange = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg22"));
//        dataChange.setFont(dialogFont);
        dataChange.setAlignmentY(BOTTOM_ALIGNMENT);

        keyChange = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg23"));
//        keyChange.setFont(dialogFont);
        keyChange.setAlignmentY(BOTTOM_ALIGNMENT);

        hiddenChange = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg32"));
//        hiddenChange.setFont(dialogFont);
        hiddenChange.setAlignmentY(BOTTOM_ALIGNMENT);

        displayHiddenFields = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg33"));
//        displayHiddenFields.setFont(dialogFont);
        displayHiddenFields.setAlignmentY(BOTTOM_ALIGNMENT);

        width = fm.stringWidth(tableRemoval.getText());
        nextChBoxWidth = fm.stringWidth(tableRename.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(recordAddition.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(recordRemoval.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(fieldAddition.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(fieldRemoval.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(fieldReorder.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(editFieldProperties.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(dataChange.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(keyChange.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(hiddenChange.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(displayHiddenFields.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        d = new Dimension(width+17, 27);

        JPanel tableRenamePanel = new JPanel(true);
        tableRenamePanel.setLayout(new BoxLayout(tableRenamePanel, BoxLayout.X_AXIS));
        tableRenamePanel.add(tableRename);
        tableRenamePanel.setMaximumSize(d);
        tableRenamePanel.setMinimumSize(d);
        tableRenamePanel.setPreferredSize(d);

        JPanel recordAdditionPanel = new JPanel(true);
        recordAdditionPanel.setLayout(new BoxLayout(recordAdditionPanel, BoxLayout.X_AXIS));
        recordAdditionPanel.add(recordAddition);
        recordAdditionPanel.setMaximumSize(d);
        recordAdditionPanel.setMinimumSize(d);
        recordAdditionPanel.setPreferredSize(d);

        JPanel recordRemovalPanel = new JPanel(true);
        recordRemovalPanel.setLayout(new BoxLayout(recordRemovalPanel, BoxLayout.X_AXIS));
        recordRemovalPanel.add(recordRemoval);
        recordRemovalPanel.setMaximumSize(d);
        recordRemovalPanel.setMinimumSize(d);
        recordRemovalPanel.setPreferredSize(d);

        JPanel fieldAdditionPanel = new JPanel(true);
        fieldAdditionPanel.setLayout(new BoxLayout(fieldAdditionPanel, BoxLayout.X_AXIS));
        fieldAdditionPanel.add(fieldAddition);
        fieldAdditionPanel.setMaximumSize(d);
        fieldAdditionPanel.setMinimumSize(d);
        fieldAdditionPanel.setPreferredSize(d);

        JPanel fieldRemovalPanel = new JPanel(true);
        fieldRemovalPanel.setLayout(new BoxLayout(fieldRemovalPanel, BoxLayout.X_AXIS));
        fieldRemovalPanel.add(fieldRemoval);
        fieldRemovalPanel.setMaximumSize(d);
        fieldRemovalPanel.setMinimumSize(d);
        fieldRemovalPanel.setPreferredSize(d);

        JPanel fieldReorderPanel = new JPanel(true);
        fieldReorderPanel.setLayout(new BoxLayout(fieldReorderPanel, BoxLayout.X_AXIS));
        fieldReorderPanel.add(fieldReorder);
        fieldReorderPanel.setMaximumSize(d);
        fieldReorderPanel.setMinimumSize(d);
        fieldReorderPanel.setPreferredSize(d);

        JPanel fieldPropertiesEditPanel = new JPanel(true);
        fieldPropertiesEditPanel.setLayout(new BoxLayout(fieldPropertiesEditPanel, BoxLayout.X_AXIS));
        fieldPropertiesEditPanel.add(editFieldProperties);
        fieldPropertiesEditPanel.setMaximumSize(d);
        fieldPropertiesEditPanel.setMinimumSize(d);
        fieldPropertiesEditPanel.setPreferredSize(d);

        JPanel dataChangePanel = new JPanel(true);
        dataChangePanel.setLayout(new BoxLayout(dataChangePanel, BoxLayout.X_AXIS));
        dataChangePanel.add(dataChange);
        dataChangePanel.setMaximumSize(d);
        dataChangePanel.setMinimumSize(d);
        dataChangePanel.setPreferredSize(d);
        dataChangePanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel keyChangePanel = new JPanel(true);
        keyChangePanel.setLayout(new BoxLayout(keyChangePanel, BoxLayout.X_AXIS));
        keyChangePanel.add(keyChange);
        keyChangePanel.setMaximumSize(d);
        keyChangePanel.setMinimumSize(d);
        keyChangePanel.setPreferredSize(d);

        JPanel hiddenChangePanel = new JPanel(true);
        hiddenChangePanel.setLayout(new BoxLayout(hiddenChangePanel, BoxLayout.X_AXIS));
        hiddenChangePanel.add(hiddenChange);
        hiddenChangePanel.setMaximumSize(d);
        hiddenChangePanel.setMinimumSize(d);
        hiddenChangePanel.setPreferredSize(d);

        JPanel displayHiddenFieldsPanel = new JPanel(true);
        displayHiddenFieldsPanel.setLayout(new BoxLayout(displayHiddenFieldsPanel, BoxLayout.X_AXIS));
        displayHiddenFieldsPanel.add(displayHiddenFields);
        displayHiddenFieldsPanel.setMaximumSize(d);
        displayHiddenFieldsPanel.setMinimumSize(d);
        displayHiddenFieldsPanel.setPreferredSize(d);

        JLabel tablePropertiesLabel = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg25"));
        tablePropertiesLabel.setFont(tablePropertiesLabel.getFont().deriveFont(Font.BOLD)/*titleFont*/);
        tablePropertiesLabel.setForeground(new Color(0, 0, 128));
        tablePropertiesLabel.setAlignmentY(CENTER_ALIGNMENT);

        FontMetrics fmBig = tablePropertiesLabel.getFontMetrics(tablePropertiesLabel.getFont());
        JPanel tableTitlePanel = new JPanel(true);
        width = fmBig.stringWidth(tablePropertiesLabel.getText());
        d = new Dimension(width, 30);
        tableTitlePanel.setMaximumSize(d);
        tableTitlePanel.setMinimumSize(d);
        tableTitlePanel.setPreferredSize(d);

        tableTitlePanel.add(tablePropertiesLabel);

        JPanel tablePropertiesPanel = new JPanel(true);
        tablePropertiesPanel.setLayout(new BoxLayout(tablePropertiesPanel, BoxLayout.Y_AXIS));
        tablePropertiesPanel.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(0,10,0,10)));

        tablePropertiesPanel.add(tableTitlePanel);
        tablePropertiesPanel.add(tableRenamePanel);
        tablePropertiesPanel.add(recordAdditionPanel);
        tablePropertiesPanel.add(recordRemovalPanel);
        tablePropertiesPanel.add(fieldAdditionPanel);
        tablePropertiesPanel.add(fieldRemovalPanel);
        tablePropertiesPanel.add(fieldReorderPanel);
        tablePropertiesPanel.add(fieldPropertiesEditPanel);
        tablePropertiesPanel.add(dataChangePanel);
        tablePropertiesPanel.add(keyChangePanel);
        tablePropertiesPanel.add(hiddenChangePanel);
        tablePropertiesPanel.add(displayHiddenFieldsPanel);


        //Field properties...
        dataChange2 = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg26"));
//        dataChange2.setFont(dialogFont);
        dataChange2.setAlignmentY(BOTTOM_ALIGNMENT);

        fieldRemoval2 = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg27"));
//        fieldRemoval2.setFont(dialogFont);
        fieldRemoval2.setAlignmentY(BOTTOM_ALIGNMENT);

        keyChange2 = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg28"));
//        keyChange2.setFont(dialogFont);
        keyChange2.setAlignmentY(BOTTOM_ALIGNMENT);

        typeChange = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg29"));
//        typeChange.setFont(dialogFont);
        typeChange.setAlignmentY(BOTTOM_ALIGNMENT);

        calculatedReset = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg30"));
//        calculatedReset.setFont(dialogFont);
        calculatedReset.setAlignmentY(BOTTOM_ALIGNMENT);

        hiddenChange2 = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg34"));
//        hiddenChange2.setFont(dialogFont);
        hiddenChange2.setAlignmentY(BOTTOM_ALIGNMENT);

        formulaChange = new JCheckBox(infoBundle.getString("DatabasePropertiesDialogMsg35"));
//        formulaChange.setFont(dialogFont);
        formulaChange.setAlignmentY(BOTTOM_ALIGNMENT);

        width = fm.stringWidth(dataChange2.getText());
        nextChBoxWidth = fm.stringWidth(fieldRemoval2.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(keyChange2.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(typeChange.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(calculatedReset.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(hiddenChange2.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        nextChBoxWidth = fm.stringWidth(formulaChange.getText());
        if (width < nextChBoxWidth)
            width = nextChBoxWidth;
        d = new Dimension(width+17, 27);

        JPanel dataChange2Panel = new JPanel(true);
        dataChange2Panel.setLayout(new BoxLayout(dataChange2Panel, BoxLayout.X_AXIS));
        dataChange2Panel.add(dataChange2);
        dataChange2Panel.setMaximumSize(d);
        dataChange2Panel.setMinimumSize(d);
        dataChange2Panel.setPreferredSize(d);

        JPanel fieldRemoval2Panel = new JPanel(true);
        fieldRemoval2Panel.setLayout(new BoxLayout(fieldRemoval2Panel, BoxLayout.X_AXIS));
        fieldRemoval2Panel.add(fieldRemoval2);
        fieldRemoval2Panel.setMaximumSize(d);
        fieldRemoval2Panel.setMinimumSize(d);
        fieldRemoval2Panel.setPreferredSize(d);

        JPanel keyChange2Panel = new JPanel(true);
        keyChange2Panel.setLayout(new BoxLayout(keyChange2Panel, BoxLayout.X_AXIS));
        keyChange2Panel.add(keyChange2);
        keyChange2Panel.setMaximumSize(d);
        keyChange2Panel.setMinimumSize(d);
        keyChange2Panel.setPreferredSize(d);

        JPanel typeChangePanel = new JPanel(true);
        typeChangePanel.setLayout(new BoxLayout(typeChangePanel, BoxLayout.X_AXIS));
        typeChangePanel.add(typeChange);
        typeChangePanel.setMaximumSize(d);
        typeChangePanel.setMinimumSize(d);
        typeChangePanel.setPreferredSize(d);

        JPanel calculatedResetPanel = new JPanel(true);
        calculatedResetPanel.setLayout(new BoxLayout(calculatedResetPanel, BoxLayout.X_AXIS));
        calculatedResetPanel.add(calculatedReset);
        calculatedResetPanel.setMaximumSize(d);
        calculatedResetPanel.setMinimumSize(d);
        calculatedResetPanel.setPreferredSize(d);

        JPanel hiddenChange2Panel = new JPanel(true);
        hiddenChange2Panel.setLayout(new BoxLayout(hiddenChange2Panel, BoxLayout.X_AXIS));
        hiddenChange2Panel.add(hiddenChange2);
        hiddenChange2Panel.setMaximumSize(d);
        hiddenChange2Panel.setMinimumSize(d);
        hiddenChange2Panel.setPreferredSize(d);

        JPanel formulaChangePanel = new JPanel(true);
        formulaChangePanel.setLayout(new BoxLayout(formulaChangePanel, BoxLayout.X_AXIS));
        formulaChangePanel.add(formulaChange);
        formulaChangePanel.setMaximumSize(d);
        formulaChangePanel.setMinimumSize(d);
        formulaChangePanel.setPreferredSize(d);

        JLabel fieldPropertiesLabel = new JLabel(infoBundle.getString("DatabasePropertiesDialogMsg31"));
        fieldPropertiesLabel.setFont(fieldPropertiesLabel.getFont().deriveFont(Font.BOLD)/*titleFont*/);
        fieldPropertiesLabel.setForeground(new Color(0, 0, 128));
        fieldPropertiesLabel.setAlignmentY(CENTER_ALIGNMENT);

        JPanel fieldTitlePanel = new JPanel(true);
        width = fmBig.stringWidth(fieldPropertiesLabel.getText());
        d = new Dimension(width, 30);
        fieldTitlePanel.setMaximumSize(d);
        fieldTitlePanel.setMinimumSize(d);
        fieldTitlePanel.setPreferredSize(d);

        fieldTitlePanel.add(fieldPropertiesLabel);

        JPanel fieldPropertiesPanel = new JPanel(true);
        fieldPropertiesPanel.setLayout(new BoxLayout(fieldPropertiesPanel, BoxLayout.Y_AXIS));
        fieldPropertiesPanel.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(0,10,0,10)));

        fieldPropertiesPanel.add(fieldTitlePanel);
        fieldPropertiesPanel.add(dataChange2Panel);
        fieldPropertiesPanel.add(fieldRemoval2Panel);
        fieldPropertiesPanel.add(keyChange2Panel);
        fieldPropertiesPanel.add(typeChangePanel);
        fieldPropertiesPanel.add(calculatedResetPanel);
        fieldPropertiesPanel.add(hiddenChange2Panel);
        fieldPropertiesPanel.add(formulaChangePanel);


        propertiesPanel = new JPanel(true);
        propertiesPanel.setLayout(new CardLayout());
        propertiesPanel.add("Database", databasePropertiesPanel);
        propertiesPanel.add("Table", tablePropertiesPanel);
        propertiesPanel.add("Field", fieldPropertiesPanel);

        JPanel workingPanel = new JPanel(true);
        workingPanel.setLayout(new BoxLayout(workingPanel, BoxLayout.X_AXIS));
        workingPanel.setBorder(new EmptyBorder(0, 3, 7, 3));

        workingPanel.add(databasePropertiesTreePanel);
        workingPanel.add(Box.createHorizontalStrut(5));
        workingPanel.add(propertiesPanel);

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new CompoundBorder(new EmptyBorder(0,2,2,2), new BevelBorder(BevelBorder.RAISED)));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(creatorPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(workingPanel);

        Insets zeroInsets = new Insets(0, 0, 0, 0);
        d = new Dimension(110, 30);
        ok = new JButton(infoBundle.getString("Close"));
        ok.setMaximumSize(d);
        ok.setPreferredSize(d);
        ok.setMinimumSize(d);
//        ok.setFont(dialogFont);
        ok.setEnabled(true);
        ok.setForeground(color128);
        ok.setMargin(zeroInsets);

        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(d);
        cancel.setPreferredSize(d);
        cancel.setMinimumSize(d);
//        cancel.setFont(dialogFont);
        cancel.setEnabled(true);
        cancel.setForeground(color128);
        cancel.setMargin(zeroInsets);

        protect = new JButton(infoBundle.getString("DatabasePropertiesDialogMsg13"));
//        protect.setFont(dialogFont);
        protect.setForeground(color128);
        d = new Dimension(110, 30);
        protect.setMaximumSize(d);
        protect.setMinimumSize(d);
        protect.setPreferredSize(d);
        protect.setMargin(zeroInsets);

/*        tableProperties = new JButton(infoBundle.getString("DatabasePropertiesDialogMsg15"));
        tableProperties.setFont(dialogFont);
        tableProperties.setForeground(color128);
        tableProperties.setMaximumSize(d);
        tableProperties.setMinimumSize(d);
        tableProperties.setPreferredSize(d);
        tableProperties.setMargin(zeroInsets);
*/
        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(ok);
//        buttonPanel.add(Box.createHorizontalStrut(6));
//        buttonPanel.add(tableProperties);
        buttonPanel.add(Box.createHorizontalStrut(6));
        buttonPanel.add(protect);
        buttonPanel.add(Box.createHorizontalStrut(6));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue());

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(12));


        //Initialization
        databasePropertiesTreePanel.tree.setSelectionRow(0);
        dbProperties = databasePropertiesTreePanel.dbProperties;
        tableRemoval.setRequestFocusEnabled(false);
        tableAddition.setRequestFocusEnabled(false);
        tableExportation.setRequestFocusEnabled(false);
        displayHiddenTables.setRequestFocusEnabled(false);
        tableRename.setRequestFocusEnabled(false);
        recordAddition.setRequestFocusEnabled(false);
        recordRemoval.setRequestFocusEnabled(false);
        fieldAddition.setRequestFocusEnabled(false);
        fieldRemoval.setRequestFocusEnabled(false);
        fieldReorder.setRequestFocusEnabled(false);
        editFieldProperties.setRequestFocusEnabled(false);
        dataChange.setRequestFocusEnabled(false);
        keyChange.setRequestFocusEnabled(false);
        hiddenChange.setRequestFocusEnabled(false);
        displayHiddenFields.setRequestFocusEnabled(false);
        dataChange2.setRequestFocusEnabled(false);
        fieldRemoval2.setRequestFocusEnabled(false);
        keyChange2.setRequestFocusEnabled(false);
        calculatedReset.setRequestFocusEnabled(false);
        typeChange.setRequestFocusEnabled(false);
        hiddenChange2.setRequestFocusEnabled(false);
        formulaChange.setRequestFocusEnabled(false);
        if (database.db == null) {
            nameField.setEnabled(false);
            nameField.setBackground(Color.lightGray);
            nameField.setForeground(Color.black);
            surnameField.setEnabled(false);
            surnameField.setBackground(Color.lightGray);
            surnameField.setForeground(Color.black);
            tableRemoval.setEnabled(false);
            tableAddition.setEnabled(false);
            tableExportation.setEnabled(false);
            displayHiddenTables.setEnabled(false);
            tableRename.setEnabled(false);
            recordAddition.setEnabled(false);
            recordRemoval.setEnabled(false);
            fieldAddition.setEnabled(false);
            fieldRemoval.setEnabled(false);
            fieldReorder.setEnabled(false);
            editFieldProperties.setEnabled(false);
            dataChange.setEnabled(false);
            keyChange.setEnabled(false);
            hiddenChange.setEnabled(false);
            displayHiddenFields.setEnabled(false);
            dataChange2.setEnabled(false);
            fieldRemoval2.setEnabled(false);
            keyChange2.setEnabled(false);
            calculatedReset.setEnabled(false);
            typeChange.setEnabled(false);
            hiddenChange2.setEnabled(false);
            formulaChange.setEnabled(false);
            protect.setEnabled(false);
//            tableProperties.setEnabled(false);
        }else{
            String creatorName, creatorSurname;
            creatorName = database.db.getDatabaseCreatorName();
            creatorSurname = database.db.getDatabaseCreatorSurname();
            if ((creatorName != null && creatorName.length() != 0) ||
                (creatorSurname != null && creatorSurname.length() != 0)) {
                if (creatorName != null)
                    nameField.setText(creatorName);
                if (creatorSurname != null)
                    surnameField.setText(creatorSurname);
            }
            if (database.db.getCreationDate() != null)
                dateLabel.setText(" " + database.db.getCreationDate() + "  " + new CTime(database.db.getCreationDate()));
            if (database.db.getLastModified() != null)
                lastModifiedLabel.setText(" " + database.db.getLastModified().toString() + "  " + new CTime(database.db.getLastModified()));

            tableRemoval.setSelected(((Boolean) dbProperties.get("TableRemoval")).booleanValue());
            tableAddition.setSelected(((Boolean) dbProperties.get("TableAddition")).booleanValue());
            tableExportation.setSelected(((Boolean) dbProperties.get("TableExportation")).booleanValue());
            displayHiddenTables.setSelected(((Boolean) dbProperties.get("DisplayHiddenTables")).booleanValue());

            if (database.db.isLocked()) {
                protect.setEnabled(true);
                protect.setText(infoBundle.getString("DatabasePropertiesDialogMsg14"));
                nameField.setEnabled(false);
                nameField.setBackground(Color.lightGray);
                nameField.setForeground(Color.black);
                surnameField.setEnabled(false);
                surnameField.setBackground(Color.lightGray);
                surnameField.setForeground(Color.black);
                tableRemoval.setEnabled(false);
                tableAddition.setEnabled(false);
                tableExportation.setEnabled(false);
                displayHiddenTables.setEnabled(false);
                tableRename.setEnabled(false);
                recordAddition.setEnabled(false);
                recordRemoval.setEnabled(false);
                fieldAddition.setEnabled(false);
                fieldRemoval.setEnabled(false);
                fieldReorder.setEnabled(false);
                editFieldProperties.setEnabled(false);
                dataChange.setEnabled(false);
                keyChange.setEnabled(false);
                hiddenChange.setEnabled(false);
                displayHiddenFields.setEnabled(false);
                dataChange2.setEnabled(false);
                fieldRemoval2.setEnabled(false);
                keyChange2.setEnabled(false);
                calculatedReset.setEnabled(false);
                typeChange.setEnabled(false);
                hiddenChange2.setEnabled(false);
                formulaChange.setEnabled(false);
            }else{
                protect.setEnabled(true);
                protect.setText(infoBundle.getString("DatabasePropertiesDialogMsg13"));

                /* The creator name and surname are set only once and can never change
                 */
                if ((creatorName != null && creatorName.length() != 0) ||
                    (creatorSurname != null && creatorSurname.length() != 0)) {
                    nameField.setEnabled(false);
                    nameField.setBackground(Color.lightGray);
                    nameField.setForeground(Color.black);
                    surnameField.setEnabled(false);
                    surnameField.setBackground(Color.lightGray);
                    surnameField.setForeground(Color.black);
                }else{
                    nameField.setEnabled(true);
                    nameField.setBackground(Color.white);
                    surnameField.setEnabled(true);
                    surnameField.setBackground(Color.white);
                }

                tableRemoval.setEnabled(true);
                tableAddition.setEnabled(true);
                tableExportation.setEnabled(true);
                displayHiddenTables.setEnabled(true);
                tableRename.setEnabled(true);
                recordAddition.setEnabled(true);
                recordRemoval.setEnabled(true);
                fieldAddition.setEnabled(true);
                fieldRemoval.setEnabled(true);
                fieldReorder.setEnabled(true);
                editFieldProperties.setEnabled(true);
                dataChange.setEnabled(true);
                keyChange.setEnabled(true);
                hiddenChange.setEnabled(true);
                displayHiddenFields.setEnabled(true);
                dataChange2.setEnabled(true);
                fieldRemoval2.setEnabled(true);
                keyChange2.setEnabled(true);
                calculatedReset.setEnabled(true);
                typeChange.setEnabled(true);
                hiddenChange2.setEnabled(true);
                formulaChange.setEnabled(true);
            }
        }

        databasePropertiesTreePanel.tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent ev) {
                DatabasePropertiesTreeNode dptn = (DatabasePropertiesTreeNode) ev.getNewLeadSelectionPath().getLastPathComponent();
                if (dptn.type == 1)
                    ((CardLayout) propertiesPanel.getLayout()).show(propertiesPanel, "Database");
                else if (dptn.type == 2) {
                    ((CardLayout) propertiesPanel.getLayout()).show(propertiesPanel, "Table");
                    String tableName =  ev.getNewLeadSelectionPath().getPathComponent(1).toString();
//                    CTable dbTable = database.db.getCTable(tableName);
                    HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                    if (((Boolean) tableProperties.get("TableRename")).booleanValue())
                        tableRename.setSelected(true);
                    else
                        tableRename.setSelected(false);
                    if (((Boolean) tableProperties.get("RecordAddition")).booleanValue())
                        recordAddition.setSelected(true);
                    else
                        recordAddition.setSelected(false);
                    if (((Boolean) tableProperties.get("RecordRemoval")).booleanValue())
                        recordRemoval.setSelected(true);
                    else
                        recordRemoval.setSelected(false);
                    if (((Boolean) tableProperties.get("FieldAddition")).booleanValue())
                        fieldAddition.setSelected(true);
                    else
                        fieldAddition.setSelected(false);
                    if (((Boolean) tableProperties.get("FieldRemoval")).booleanValue())
                        fieldRemoval.setSelected(true);
                    else
                        fieldRemoval.setSelected(false);
                    if (((Boolean) tableProperties.get("FieldReordering")).booleanValue())
                        fieldReorder.setSelected(true);
                    else
                        fieldReorder.setSelected(false);
                    if (((Boolean) tableProperties.get("FieldPropertiesEditing")).booleanValue())
                        editFieldProperties.setSelected(true);
                    else
                        editFieldProperties.setSelected(false);
                    if (((Boolean) tableProperties.get("DataChange")).booleanValue())
                        dataChange.setSelected(true);
                    else
                        dataChange.setSelected(false);
                    if (((Boolean) tableProperties.get("KeyChange")).booleanValue())
                        keyChange.setSelected(true);
                    else
                        keyChange.setSelected(false);
                    if (((Boolean) tableProperties.get("HiddedChange")).booleanValue())
                        hiddenChange.setSelected(true);
                    else
                        hiddenChange.setSelected(false);
                    if (((Boolean) tableProperties.get("DisplayHiddenFields")).booleanValue())
                        displayHiddenFields.setSelected(true);
                    else
                        displayHiddenFields.setSelected(false);
                }else{
                    ((CardLayout) propertiesPanel.getLayout()).show(propertiesPanel, "Field");
                    String tableName =  ev.getNewLeadSelectionPath().getPathComponent(1).toString();
                    String fieldName = ev.getNewLeadSelectionPath().getPathComponent(2).toString();
                    HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                    HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);

                    if (!((Boolean) tableProperties.get("DataChange")).booleanValue())
                        dataChange2.setEnabled(false);
                    else{
                        if (database.db.isLocked())
                            dataChange2.setEnabled(true);
                        else
                            dataChange2.setEnabled(true);
                    }

                    if (!((Boolean) tableProperties.get("KeyChange")).booleanValue()) {
                        keyChange2.setEnabled(false);
                    }else{
                        if (database.db.isLocked()) {
                            keyChange2.setEnabled(false);
                        }else
                            keyChange2.setEnabled(true);
                    }

                    if (((Boolean) fieldProperties.get("DataChange")).booleanValue() &&
                        ((Boolean) tableProperties.get("DataChange")).booleanValue())
                        dataChange2.setSelected(true);
                    else
                        dataChange2.setSelected(false);
                    if (((Boolean) fieldProperties.get("Removability")).booleanValue())
                        fieldRemoval2.setSelected(true);
                    else
                        fieldRemoval2.setSelected(false);
                    if (((Boolean) fieldProperties.get("KeyChange")).booleanValue() &&
                        ((Boolean) tableProperties.get("KeyChange")).booleanValue())
                        keyChange2.setSelected(true);
                    else
                        keyChange2.setSelected(false);

                    if (((Boolean) fieldProperties.get("CalculatedReset")).booleanValue())
                        calculatedReset.setSelected(true);
                    else
                        calculatedReset.setSelected(false);
                    if (((Boolean) fieldProperties.get("DataTypeChange")).booleanValue())
                        typeChange.setSelected(true);
                    else
                        typeChange.setSelected(false);
                    if (((Boolean) fieldProperties.get("HiddenChange")).booleanValue())
                        hiddenChange2.setSelected(true);
                    else
                        hiddenChange2.setSelected(false);
                    if (((Boolean) fieldProperties.get("FormulaChange")).booleanValue())
                        formulaChange.setSelected(true);
                    else
                        formulaChange.setSelected(false);

                    try{
                        Table ctable = database.db.getTable(tableName);
                        AbstractTableField f = ctable.getTableField(fieldName);

                        if (!f.isCalculated()) {
                            formulaChange.setEnabled(false);
                            calculatedReset.setEnabled(false);
                            if (!database.db.isLocked()) {
                                if (((Boolean) tableProperties.get("DataChange")).booleanValue())
                                    dataChange2.setEnabled(true);
                                else
                                    dataChange2.setEnabled(false);
                                typeChange.setEnabled(true);
                            }else{
                                dataChange2.setEnabled(false);
                                typeChange.setEnabled(false);
                            }
                        }else{
                            typeChange.setEnabled(false);
                            dataChange2.setEnabled(false);
                            if (!database.db.isLocked()) {
                                calculatedReset.setEnabled(true);
                                formulaChange.setEnabled(true);
                            }else{
                                calculatedReset.setEnabled(false);
                                formulaChange.setEnabled(false);
                            }
                        }
                    }catch (InvalidFieldNameException exc) {
                        System.out.println("Serious inconsistency error in DatabasePropertiesDialog DatabasePropertiesDialog() : (1)");
                    }
                }

            }
        });

        //Logic
        tableRemoval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dbProperties.put("TableRemoval", new Boolean(tableRemoval.isSelected()));
                dbProperties.put("Modified", Boolean.TRUE);
            }
        });
        tableAddition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dbProperties.put("TableAddition", new Boolean(tableAddition.isSelected()));
                dbProperties.put("Modified", Boolean.TRUE);
            }
        });
        tableExportation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dbProperties.put("TableExportation", new Boolean(tableExportation.isSelected()));
                dbProperties.put("Modified", Boolean.TRUE);
            }
        });
        displayHiddenTables.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dbProperties.put("DisplayHiddenTables", new Boolean(displayHiddenTables.isSelected()));
                dbProperties.put("Modified", Boolean.TRUE);
            }
        });
        tableRename.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("TableRename", new Boolean(tableRename.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        recordAddition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("RecordAddition", new Boolean(recordAddition.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        recordRemoval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("RecordRemoval", new Boolean(recordRemoval.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        fieldAddition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("FieldAddition", new Boolean(fieldAddition.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        fieldRemoval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("FieldRemoval", new Boolean(fieldRemoval.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        fieldReorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("FieldReordering", new Boolean(fieldReorder.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        editFieldProperties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("FieldPropertiesEditing", new Boolean(editFieldProperties.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        dataChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("DataChange", new Boolean(dataChange.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
                HashMap fieldHash = (HashMap) ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).get("Fields");
                HashMapIterator iterator = fieldHash.begin();

                if (dataChange.isSelected())
                    dataChange2.setEnabled(true);
                else{
                    dataChange2.setEnabled(false);
                    while (!iterator.atEnd()) {
                        HashMap fieldProperties = (HashMap) ((Pair) iterator.get()).second;
                        fieldProperties.put("DataChange", Boolean.FALSE);
                        fieldProperties.put("Modified", Boolean.TRUE);
                        iterator.advance();
                    }
                }
            }
        });
        keyChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("KeyChange", new Boolean(keyChange.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
                HashMap fieldHash = (HashMap) ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).get("Fields");
                HashMapIterator iterator = fieldHash.begin();

                if (keyChange.isSelected())
                    keyChange2.setEnabled(true);
                else{
                    keyChange2.setEnabled(false);
                    while (!iterator.atEnd()) {
                        HashMap fieldProperties = (HashMap) ((Pair) iterator.get()).second;
                        fieldProperties.put("KeyChange", Boolean.FALSE);
                        fieldProperties.put("Modified", Boolean.TRUE);
                        iterator.advance();
                    }
                }
            }
        });
        hiddenChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("HiddedChange", new Boolean(hiddenChange.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });
        displayHiddenFields.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("DisplayHiddenFields", new Boolean(displayHiddenFields.isSelected()));
                ((HashMap) ((HashMap) dbProperties.get("Tables")).get(databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString())).put("Modified", Boolean.TRUE);
            }
        });

        keyChange2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
//                System.out.println("jTable name: " + tableName);
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
//                System.out.println("Table hash: " + tableProperties);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
//                System.out.println("Field hash: " + fieldProperties);
                fieldProperties.put("KeyChange", new Boolean(keyChange2.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
//                System.out.println("Field hash: " + fieldProperties);
            }
        });
        dataChange2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
                fieldProperties.put("DataChange", new Boolean(dataChange2.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
            }
        });
        fieldRemoval2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
                fieldProperties.put("Removability", new Boolean(fieldRemoval2.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
            }
        });
        calculatedReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
                fieldProperties.put("CalculatedReset", new Boolean(calculatedReset.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
            }
        });
        typeChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
                fieldProperties.put("DataTypeChange", new Boolean(typeChange.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
            }
        });
        hiddenChange2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
                fieldProperties.put("HiddenChange", new Boolean(hiddenChange2.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
            }
        });
        formulaChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String tableName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                HashMap tableProperties = (HashMap) ((HashMap) dbProperties.get("Tables")).get(tableName);
                String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent().toString();
                HashMap fieldProperties = (HashMap) ((HashMap) tableProperties.get("Fields")).get(fieldName);
                fieldProperties.put("FormulaChange", new Boolean(formulaChange.isSelected()));
                fieldProperties.put("Modified", Boolean.TRUE);
            }
        });

        protect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (database.db.isLocked()) {
                    PasswordDialog pw = new PasswordDialog(database.topLevelFrame, database, "Database password", "DatabasePropertiesDialogMsg16", "");
                    if (pw.clickedButton == 0)
                        return;

                    password = pw.getPassword();
                    database.db.unlock(password);
                    if (database.db.isLocked())
                        ESlateOptionPane.showMessageDialog(database, infoBundle.getString("DatabaseMsg134"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    else{
                        protect.setText(infoBundle.getString("DatabasePropertiesDialogMsg13"));

                        /* The creator name and surname are set only once and can never change
                         */
                        String creatorName = database.db.getDatabaseCreatorName();
                        String creatorSurname = database.db.getDatabaseCreatorSurname();
                        if ((creatorName == null || creatorName.length() == 0) &&
                            (creatorSurname == null || creatorSurname.length() == 0)) {
                            nameField.setEnabled(true);
                            nameField.setBackground(Color.white);
                            surnameField.setEnabled(true);
                            surnameField.setBackground(Color.white);
                        }else{
                            nameField.setEnabled(false);
                            nameField.setBackground(Color.lightGray);
                            nameField.setForeground(Color.black);
                            surnameField.setEnabled(false);
                            surnameField.setBackground(Color.lightGray);
                            surnameField.setForeground(Color.black);
                        }

                        DatabasePropertiesTreeNode dptn = (DatabasePropertiesTreeNode) databasePropertiesTreePanel.tree.getSelectionPath().getLastPathComponent();
                        if (dptn.type >= 3) {
                            String tableName =  databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(1).toString();
                            String fieldName = databasePropertiesTreePanel.tree.getSelectionPath().getPathComponent(2).toString();
                            try{
                                Table ctable = database.db.getTable(tableName);
                                AbstractTableField f = ctable.getTableField(fieldName);

                                if (!f.isCalculated()) {
                                    formulaChange.setEnabled(false);
                                    calculatedReset.setEnabled(false);
                                    if (!database.db.isLocked()) {
                                        dataChange2.setEnabled(true);
                                        typeChange.setEnabled(true);
                                    }else{
                                        dataChange2.setEnabled(false);
                                        typeChange.setEnabled(false);
                                    }
                                }else{
                                    typeChange.setEnabled(false);
                                    dataChange2.setEnabled(false);
                                    if (!database.db.isLocked()) {
                                        calculatedReset.setEnabled(true);
                                        formulaChange.setEnabled(true);
                                    }else{
                                        calculatedReset.setEnabled(false);
                                        formulaChange.setEnabled(false);
                                    }
                                }
                            }catch (InvalidFieldNameException exc) {
                                System.out.println("Serious inconsistency error in DatabasePropertiesDialog DatabasePropertiesDialog() : (2)");
                            }
                        }
                        tableRemoval.setEnabled(true);
                        tableAddition.setEnabled(true);
                        tableExportation.setEnabled(true);
                        displayHiddenTables.setEnabled(true);
                        tableRename.setEnabled(true);
                        recordAddition.setEnabled(true);
                        recordRemoval.setEnabled(true);
                        fieldAddition.setEnabled(true);
                        fieldRemoval.setEnabled(true);
                        fieldReorder.setEnabled(true);
                        editFieldProperties.setEnabled(true);
                        dataChange.setEnabled(true);
                        keyChange.setEnabled(true);
                        hiddenChange.setEnabled(true);
                        displayHiddenFields.setEnabled(true);
//                        dataChange2.setEnabled(true);
                        fieldRemoval2.setEnabled(true);
                        keyChange2.setEnabled(true);
//                        calculatedReset.setEnabled(true);
//                        typeChange.setEnabled(true);
                        hiddenChange2.setEnabled(true);
//                        formulaChange.setEnabled(true);
                    }
                }else{
                    String pass = "";
                    if (database.db.checkPassword(password))
                        pass = password;
                    PasswordDialog pw = new PasswordDialog(database.topLevelFrame, database, "Database password", "DatabasePropertiesDialogMsg16", pass);
                    if (pw.clickedButton == 0)
                        return;

                    String password = pw.getPassword();
                    database.db.protect(password);
                    if (database.db.isProtected()) {
                        protect.setEnabled(true);
                        protect.setText(infoBundle.getString("DatabasePropertiesDialogMsg14"));
                        nameField.setEnabled(false);
                        nameField.setBackground(Color.lightGray);
                        nameField.setForeground(Color.black);
                        surnameField.setEnabled(false);
                        surnameField.setBackground(Color.lightGray);
                        surnameField.setForeground(Color.black);
                        tableRemoval.setEnabled(false);
                        tableAddition.setEnabled(false);
                        tableExportation.setEnabled(false);
                        displayHiddenTables.setEnabled(false);
                        tableRename.setEnabled(false);
                        recordAddition.setEnabled(false);
                        recordRemoval.setEnabled(false);
                        fieldAddition.setEnabled(false);
                        fieldRemoval.setEnabled(false);
                        fieldReorder.setEnabled(false);
                        editFieldProperties.setEnabled(false);
                        dataChange.setEnabled(false);
                        keyChange.setEnabled(false);
                        hiddenChange.setEnabled(false);
                        displayHiddenFields.setEnabled(false);
                        dataChange2.setEnabled(false);
                        fieldRemoval2.setEnabled(false);
                        keyChange2.setEnabled(false);
                        calculatedReset.setEnabled(false);
                        typeChange.setEnabled(false);
                        hiddenChange2.setEnabled(false);
                        formulaChange.setEnabled(false);
                        ESlateOptionPane.showMessageDialog(database, infoBundle.getString("DatabaseMsg135"), "", JOptionPane.INFORMATION_MESSAGE);
                    }else
                        ESlateOptionPane.showMessageDialog(database, infoBundle.getString("DatabaseMsg136"), infoBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (database.db != null) {
                    database.db.setDatabaseCreatorName(nameField.getText());
                    database.db.setDatabaseCreatorSurname(surnameField.getText());
                    if (((Boolean) dbProperties.get("Modified")).booleanValue()) {
                        database.db.setTableRemovalAllowed(tableRemoval.isSelected());
                        database.db.setTableAdditionAllowed(tableAddition.isSelected());
                        database.db.setTableExportationAllowed(tableExportation.isSelected());
                        database.db.setHiddenTablesDisplayed(displayHiddenTables.isSelected());
                    }

                    //CTables
                    HashMap tableHash = (HashMap) dbProperties.get("Tables");
                    for (int i=0; i<database.db.getTableCount(); i++) {
                        Table ctable = database.db.getTableAt(i);
                        String tableName = ctable.getTitle();

                        HashMap tableProperties = (HashMap) tableHash.get(tableName);
                        if (((Boolean) tableProperties.get("Modified")).booleanValue()) {
                            try{
                                ctable.setTableRenamingAllowed(((Boolean) tableProperties.get("TableRename")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setRecordAdditionAllowed(((Boolean) tableProperties.get("RecordAddition")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }

                            try{
                                ctable.setRecordRemovalAllowed(((Boolean) tableProperties.get("RecordRemoval")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setFieldAdditionAllowed(((Boolean) tableProperties.get("FieldAddition")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setFieldRemovalAllowed(((Boolean) tableProperties.get("FieldRemoval")).booleanValue());
//                            System.out.println("dbTable.isFieldRemovalAllowed(): " + dbTable.isFieldRemovalAllowed());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setFieldReorderingAllowed(((Boolean) tableProperties.get("FieldReordering")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setFieldPropertyEditingAllowed(((Boolean) tableProperties.get("FieldPropertiesEditing")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setKeyChangeAllowed(((Boolean) tableProperties.get("KeyChange")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setDataChangeAllowed(((Boolean) tableProperties.get("DataChange")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setHiddenAttributeChangeAllowed(((Boolean) tableProperties.get("HiddedChange")).booleanValue());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                            try{
                                ctable.setHiddedFieldsDisplayed(((Boolean) tableProperties.get("DisplayHiddenFields")).booleanValue());
//                            System.out.println("dbTable.isHiddedFieldsDisplayed(): " + dbTable.isHiddenFieldsDisplayed());
                            }catch (PropertyVetoException exc) {
                                ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                            }
                        }

                        HashMap fieldHash = (HashMap) tableProperties.get("Fields");
                        for (int k=0; k<ctable.getFieldCount(); k++) {
                            try{
                                AbstractTableField f = ctable.getTableField(k);
                                String fieldName = f.getName();
                                HashMap fieldProperties = (HashMap) fieldHash.get(fieldName);

                                if (((Boolean) fieldProperties.get("Modified")).booleanValue()) {
                                    try{
                                        ctable.setFieldEditabilityChangeAllowed(fieldName, ((Boolean) fieldProperties.get("DataChange")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                    try{
                                        ctable.setFieldRemovabilityChangeAllowed(fieldName, ((Boolean) fieldProperties.get("Removability")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                    try{
                                        ctable.setFieldDataTypeChangeAllowed(fieldName, ((Boolean) fieldProperties.get("DataTypeChange")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                    try{
                                        ctable.setCalcFieldResetAllowed(fieldName, ((Boolean) fieldProperties.get("CalculatedReset")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                    try{
                                        ctable.setFieldKeyAttributeChangeAllowed(fieldName, ((Boolean) fieldProperties.get("KeyChange")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                    try{
                                        ctable.setFieldHiddenAttributeChangeAllowed(fieldName, ((Boolean) fieldProperties.get("HiddenChange")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                    try{
                                        ctable.setCalcFieldFormulaChangeAllowed(fieldName, ((Boolean) fieldProperties.get("FormulaChange")).booleanValue());
                                    }catch (PropertyVetoException exc) {
                                        ESlateOptionPane.showMessageDialog(database, exc.getMessage(), database.warningStr, JOptionPane.WARNING_MESSAGE);
                                    }
                                }
                            }catch (InvalidFieldIndexException exc) {
                                System.out.println("Serious inconsistency error in in DatabasePropertiesDialog DatabasePropertiesDialog() : (3)");
                            }catch (InvalidFieldNameException exc) {
                                System.out.println("Serious inconsistency error in in DatabasePropertiesDialog DatabasePropertiesDialog() : (4)");
                            }
                        }
                    }
                }
                dispose();
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        /* Display the "DatabasePropertiesDialog".
         */
        setResizable(false);

        pack();
        Rectangle dbBounds = database.getBounds();
System.out.println("getLocationOnScreen() 1");
        java.awt.Point dbLocation = database.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
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
}
