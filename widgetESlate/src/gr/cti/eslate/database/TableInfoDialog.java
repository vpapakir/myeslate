package gr.cti.eslate.database;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Frame;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javax.swing.UIManager;


class TableInfoDialog extends JDialog {
    int dialogWidth = 348;
    int dialogHeight = 331;
    int column1Width;
    int recordWidth, keyFieldRecordWidth, calcFieldRecordWidth;
    int keyFieldCount;
    int calcFieldCount;

    DBTable dbTable;
    FontMetrics fm;
    JPanel firstKeyFieldSubPanel;
    JPanel firstCalcFieldSubPanel;
    JPanel keyFieldPanel, calcFieldPanel;
    Array keyFieldPanelArray = new Array();
    Array calcFieldPanelArray = new Array();
    JButton ok;

    transient ResourceBundle infoBundle;


    protected TableInfoDialog(Frame frame, DBTable table, Database dbComponent) {
        super(frame, true);
//        System.out.println("dbComponent: ");
        this.dbTable = table;
//        System.out.println("dbComponent: ");
//        System.out.println("dbComponent: " + dbComponent);
        infoBundle = Database.infoBundle;

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		ok.doClick();
          		javax.swing.ButtonModel bm = ok.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	this.getRootPane().WHEN_IN_FOCUSED_WINDOW);

        setTitle(infoBundle.getString("TableInfoDialogMsg1") + table.table.getTitle() + "\"");

//        getContentPane().setBackground(Color.lightGray);
        BoxLayout bl0 = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl0);

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        BoxLayout bl1 = new BoxLayout(infoPanel, BoxLayout.Y_AXIS);
        infoPanel.setLayout(bl1);
        infoPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new EmptyBorder(5, 10, 5, 10)));
//        infoPanel.setBackground(Color.lightGray);

        JPanel recordCountPanel = new JPanel(true);
        BoxLayout bl2 = new BoxLayout(recordCountPanel, BoxLayout.X_AXIS);
        recordCountPanel.setLayout(bl2);
//        recordCountPanel.setBorder(new LineBorder(Color.darkGray));
        recordCountPanel.setAlignmentX(LEFT_ALIGNMENT);
        recordCountPanel.setAlignmentY(CENTER_ALIGNMENT);

        JPanel fieldCountPanel = new JPanel(true);
        BoxLayout bl3 = new BoxLayout(fieldCountPanel, BoxLayout.X_AXIS);
        fieldCountPanel.setLayout(bl3);
//        fieldCountPanel.setBorder(new LineBorder(Color.darkGray));
        fieldCountPanel.setAlignmentX(LEFT_ALIGNMENT);
        fieldCountPanel.setAlignmentY(CENTER_ALIGNMENT);

        JPanel selRecordCountPanel = new JPanel(true);
        BoxLayout bl6 = new BoxLayout(selRecordCountPanel, BoxLayout.X_AXIS);
        selRecordCountPanel.setLayout(bl6);
//        selRecordCountPanel.setBorder(new LineBorder(Color.darkGray));
        selRecordCountPanel.setAlignmentX(LEFT_ALIGNMENT);
        selRecordCountPanel.setAlignmentY(CENTER_ALIGNMENT);

        JPanel selFieldCountPanel = new JPanel(true);
        BoxLayout bl7 = new BoxLayout(selFieldCountPanel, BoxLayout.X_AXIS);
        selFieldCountPanel.setLayout(bl7);
//        selFieldCountPanel.setBorder(new LineBorder(Color.darkGray));
        selFieldCountPanel.setAlignmentX(LEFT_ALIGNMENT);
        selFieldCountPanel.setAlignmentY(CENTER_ALIGNMENT);

        JLabel recordCountLabel = new JLabel(infoBundle.getString("TableInfoDialogMsg2"));
        JLabel fieldCountLabel = new JLabel(infoBundle.getString("TableInfoDialogMsg3"));
        JLabel selRecordCountLabel = new JLabel(infoBundle.getString("TableInfoDialogMsg4"));
        JLabel selFieldCountLabel = new JLabel(infoBundle.getString("TableInfoDialogMsg5"));

        fm = recordCountLabel.getFontMetrics(recordCountLabel.getFont());
        int recordCountLabelWidth = fm.stringWidth(recordCountLabel.getText());
        int fieldCountLabelWidth = fm.stringWidth(fieldCountLabel.getText());
        int selFieldCountLabelWidth = fm.stringWidth(selFieldCountLabel.getText());
        int selRecordCountLabelWidth = fm.stringWidth(selRecordCountLabel.getText());
        column1Width = selRecordCountLabelWidth + 50;

        JLabel recordCountLabel2 = new JLabel(new Integer(table.table.getRecordCount()).toString());
        JLabel fieldCountLabel2 = new JLabel(new Integer(table.table.getFieldCount()).toString());
        JLabel selRecordCountLabel2 = new JLabel(new Integer(table.table.getSelectedSubset().size()).toString());
        int numOfSelectedColumns;
        if (!table.isSimultaneousFieldRecordActivation()) {
            /* No column selection is allowed, so "numOfSelectedColumns" = 0.
             * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
             * to "true", so that the field will be selected. In this case calculate the selected
             * columns.
             */
            if (table.jTable.getColumnSelectionAllowed())
                numOfSelectedColumns = table.jTable.getSelectedColumnCount();
            else
                numOfSelectedColumns = 0;
        }else
            numOfSelectedColumns = table.jTable.getSelectedColumnCount();

        JLabel selFieldCountLabel2 = new JLabel(new Integer(numOfSelectedColumns).toString());

//        recordCountPanel.add(Box.createHorizontalStrut(10));
        recordCountPanel.add(recordCountLabel);
        recordCountPanel.add(Box.createHorizontalStrut(column1Width-recordCountLabelWidth));
        recordCountPanel.add(recordCountLabel2);

//        fieldCountPanel.add(Box.createHorizontalStrut(10));
        fieldCountPanel.add(fieldCountLabel);
        fieldCountPanel.add(Box.createHorizontalStrut(column1Width-fieldCountLabelWidth));
        fieldCountPanel.add(fieldCountLabel2);

//        selRecordCountPanel.add(Box.createHorizontalStrut(10));
        selRecordCountPanel.add(selRecordCountLabel);
        selRecordCountPanel.add(Box.createHorizontalStrut(column1Width-selRecordCountLabelWidth));
        selRecordCountPanel.add(selRecordCountLabel2);

//        selFieldCountPanel.add(Box.createHorizontalStrut(10));
        selFieldCountPanel.add(selFieldCountLabel);
        selFieldCountPanel.add(Box.createHorizontalStrut(column1Width-selFieldCountLabelWidth));
        selFieldCountPanel.add(selFieldCountLabel2);
//        selFieldCountPanel.setMaximumSize(new Dimension(column1Width

//        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(recordCountPanel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(fieldCountPanel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(selRecordCountPanel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(selFieldCountPanel);

        if (dbTable.table.hasKey()) {
            keyFieldPanel = new JPanel(true);
            BoxLayout bl8 = new BoxLayout(keyFieldPanel, BoxLayout.X_AXIS);
            keyFieldPanel.setLayout(bl8);
            keyFieldPanel.setAlignmentX(LEFT_ALIGNMENT);
            keyFieldPanel.setAlignmentY(CENTER_ALIGNMENT);
//            keyFieldPanel.add(Box.createHorizontalStrut(10));
            keyFieldPanel.add(createKeyFieldPanel());
            infoPanel.add(Box.createVerticalStrut(3));
            infoPanel.add(keyFieldPanel);
        }

        // Check if there exist calculated fields.
        boolean calcFieldsExist = false;
        TableFieldBaseArray tableFields = dbTable.table.getFields();
        for (int i=0; i<tableFields.size(); i++) {
            if (tableFields.get(i).isCalculated()) {
                calcFieldsExist = true;
                break;
            }
        }
        if (calcFieldsExist) {
            calcFieldPanel = new JPanel(true);
            BoxLayout bl9 = new BoxLayout(calcFieldPanel, BoxLayout.X_AXIS);
            calcFieldPanel.setLayout(bl9);
            calcFieldPanel.setAlignmentX(LEFT_ALIGNMENT);
            calcFieldPanel.setAlignmentY(CENTER_ALIGNMENT);
//            calcFieldPanel.add(Box.createHorizontalStrut(10));
            calcFieldPanel.add(createCalcFieldPanel());
            infoPanel.add(Box.createVerticalStrut(3));
            infoPanel.add(calcFieldPanel);
//            infoPanel.add(Box.createVerticalStrut(10));
        }

        recordWidth = (keyFieldRecordWidth > calcFieldRecordWidth)? keyFieldRecordWidth:calcFieldRecordWidth;
        if (recordWidth != 0) {
            recordCountPanel.setMaximumSize(new Dimension(20+recordWidth, 20));
            recordCountPanel.setPreferredSize(new Dimension(20+recordWidth, 20));
            recordCountPanel.setMinimumSize(new Dimension(20+recordWidth, 20));
            fieldCountPanel.setMaximumSize(new Dimension(20+recordWidth, 20));
            fieldCountPanel.setPreferredSize(new Dimension(20+recordWidth, 20));
            fieldCountPanel.setMinimumSize(new Dimension(20+recordWidth, 20));
            selRecordCountPanel.setMaximumSize(new Dimension(20+recordWidth, 20));
            selRecordCountPanel.setPreferredSize(new Dimension(20+recordWidth, 20));
            selRecordCountPanel.setMinimumSize(new Dimension(20+recordWidth, 20));
            selFieldCountPanel.setMaximumSize(new Dimension(20+recordWidth, 20));
            selFieldCountPanel.setPreferredSize(new Dimension(20+recordWidth, 20));
            selFieldCountPanel.setMinimumSize(new Dimension(20+recordWidth, 20));
        }

        if (keyFieldRecordWidth < calcFieldRecordWidth) {
            if (firstKeyFieldSubPanel != null)
                firstKeyFieldSubPanel.add(Box.createHorizontalStrut(calcFieldRecordWidth-keyFieldRecordWidth));
            for (int i=0; i<keyFieldPanelArray.size(); i++)
                ((JPanel) keyFieldPanelArray.at(i)).add(Box.createHorizontalStrut(calcFieldRecordWidth-keyFieldRecordWidth));
        }else if (keyFieldRecordWidth > calcFieldRecordWidth) {
            if (firstCalcFieldSubPanel != null)
                firstCalcFieldSubPanel.add(Box.createHorizontalStrut(keyFieldRecordWidth-calcFieldRecordWidth));
            for (int i=0; i<calcFieldPanelArray.size(); i++)
                ((JPanel) calcFieldPanelArray.at(i)).add(Box.createHorizontalStrut(keyFieldRecordWidth-calcFieldRecordWidth));
        }

//        else if (keyFieldRecordWidth > recordWidth)
//            firstCalcFieldSubPanel.add(Box.createHorizontalStrut(recordWidth-keyFieldRecordWidth));

        int keyFieldPanelHeight = 22 + ((keyFieldCount == 0)? 0:(keyFieldCount-1)*14);
        int calcFieldPanelHeight = 22 + ((calcFieldCount == 0)? 0:(calcFieldCount-1)*14);
        if (keyFieldPanel != null) {
            keyFieldPanel.setMaximumSize(new Dimension(20+recordWidth, keyFieldPanelHeight));
            keyFieldPanel.setPreferredSize(new Dimension(20+recordWidth, keyFieldPanelHeight));
            keyFieldPanel.setMinimumSize(new Dimension(20+recordWidth, keyFieldPanelHeight));
        }
        if (calcFieldPanel != null) {
            calcFieldPanel.setMaximumSize(new Dimension(20+recordWidth, calcFieldPanelHeight));
            calcFieldPanel.setPreferredSize(new Dimension(20+recordWidth, calcFieldPanelHeight));
            calcFieldPanel.setMinimumSize(new Dimension(20+recordWidth, calcFieldPanelHeight));
        }

//        System.out.println(firstKeyFieldSubPanel.getWidth() + ", " + firstCalcFieldSubPanel.getWidth());

        mainPanel.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
        mainPanel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        mainPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(true);
        ok = new JButton(infoBundle.getString("OK"));
        ok.setMaximumSize(new Dimension(100, 30));
        ok.setPreferredSize(new Dimension(100, 30));
        ok.setMinimumSize(new Dimension(100, 30));
        ok.setEnabled(true);
        ok.setForeground(new Color(0,0,128));
//        buttonPanel.add(Box.createHorizontalStrut(200));
        buttonPanel.add(ok);
        buttonPanel.setMaximumSize(new Dimension(100, 40));
        buttonPanel.setPreferredSize(new Dimension(100, 40));
        buttonPanel.setMinimumSize(new Dimension(100, 40));

//        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(10));

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	            dispose();
	        }
	    });

        /* Dispaying the "TableInfoDialog".
         */
        setResizable(false);
//        setModal(true);
        dialogHeight = 10+2+10+(4*20) + keyFieldPanelHeight + 10 + calcFieldPanelHeight + 10 + 10 + 2 + 10 + 40 + 10 + 20;
        setSize(55+recordWidth, dialogHeight);
//        setSize(dialogWidth, dialogHeight);
//    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//	    setLocation(screenSize.width/2 - dialogWidth/2, screenSize.height/2 - dialogHeight/2);
        pack();
        Rectangle bounds = null;
        java.awt.Point location = null;
        System.out.println("getLocationOnScreen() 12");
        if (dbComponent != null) {
            bounds = dbComponent.getBounds();
            location = dbComponent.getLocationOnScreen();
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
        setVisible(true);
    }

    protected JPanel createEmptyPanel(String attrib, boolean keyFields) {
        JPanel panel = new JPanel(true);
        BoxLayout bl12 = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(bl12);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setAlignmentY(CENTER_ALIGNMENT);

        panel.add(Box.createHorizontalStrut(column1Width));

        JLabel l = new JLabel(attrib);
        panel.add(l);
        if (keyFields)
            keyFieldPanelArray.add(panel);
        else
            calcFieldPanelArray.add(panel);
        return panel;
    }


    protected JPanel createKeyFieldPanel() {
        JPanel keyFieldPanel = new JPanel(true);
        BoxLayout bl5 = new BoxLayout(keyFieldPanel, BoxLayout.Y_AXIS);
        keyFieldPanel.setLayout(bl5);

        firstKeyFieldSubPanel = new JPanel(true);
        BoxLayout bl15 = new BoxLayout(firstKeyFieldSubPanel, BoxLayout.X_AXIS);
        firstKeyFieldSubPanel.setLayout(bl15);
        firstKeyFieldSubPanel.setAlignmentX(LEFT_ALIGNMENT);
        firstKeyFieldSubPanel.setAlignmentY(CENTER_ALIGNMENT);

        TableFieldBaseArray fields = dbTable.table.getFields();
        ArrayList keyFieldNames = new ArrayList();
        for (int i=0; i<fields.size(); i++) {
            if (dbTable.table.isPartOfTableKey(fields.get(i))/*fields.get(i).isKey()*/)
                keyFieldNames.add(fields.get(i).getName());
        }

        keyFieldCount = keyFieldNames.size();

        JLabel keyFieldLabel = new JLabel(infoBundle.getString("TableInfoDialogMsg6"));
        int keyFieldLabelWidth = fm.stringWidth(keyFieldLabel.getText());
        JLabel keyFieldLabel2 = new JLabel(infoBundle.getString("TableInfoDialogMsg7"));

        if (keyFieldNames.size() != 0)
            keyFieldLabel2.setText((String) keyFieldNames.get(0));

        int keyFieldLabel2Width = fm.stringWidth(keyFieldLabel2.getText());
        keyFieldRecordWidth = column1Width + keyFieldLabel2Width;
//        keyFieldSubPanel.add(Box.createHorizontalStrut(10));
        firstKeyFieldSubPanel.add(keyFieldLabel);
        firstKeyFieldSubPanel.add(Box.createHorizontalStrut(column1Width-keyFieldLabelWidth));
        firstKeyFieldSubPanel.add(keyFieldLabel2);

        keyFieldPanel.add(firstKeyFieldSubPanel);

        for (int i=1; i<keyFieldNames.size(); i++) {
            keyFieldLabel2Width = fm.stringWidth((String) keyFieldNames.get(i));
            if (keyFieldRecordWidth < (keyFieldLabel2Width+column1Width))
                keyFieldRecordWidth = keyFieldLabel2Width+column1Width;
            keyFieldPanel.add(createEmptyPanel((String) keyFieldNames.get(i), true));
        }

//        keyFieldPanel.setBorder(new EtchedBorder());
        return keyFieldPanel;

    }


    protected JPanel createCalcFieldPanel() {
        JPanel calcFieldPanel = new JPanel(true);
        BoxLayout bl5 = new BoxLayout(calcFieldPanel, BoxLayout.Y_AXIS);
        calcFieldPanel.setLayout(bl5);

        firstCalcFieldSubPanel = new JPanel(true);
        BoxLayout bl15 = new BoxLayout(firstCalcFieldSubPanel, BoxLayout.X_AXIS);
        firstCalcFieldSubPanel.setLayout(bl15);
        firstCalcFieldSubPanel.setAlignmentX(LEFT_ALIGNMENT);
        firstCalcFieldSubPanel.setAlignmentY(CENTER_ALIGNMENT);

        TableFieldBaseArray fields = dbTable.table.getFields();
        ArrayList calcFieldNames = new ArrayList();
        for (int i=0; i<fields.size(); i++) {
            if (((AbstractTableField) fields.get(i)).isCalculated())
                calcFieldNames.add(((AbstractTableField) fields.get(i)).getName());
        }

        calcFieldCount = calcFieldNames.size();

        JLabel calcFieldLabel = new JLabel(infoBundle.getString("TableInfoDialogMsg8"));
        int calcFieldLabelWidth = fm.stringWidth(calcFieldLabel.getText());
        JLabel calcFieldLabel2 = new JLabel(infoBundle.getString("TableInfoDialogMsg7"));

        if (calcFieldNames.size() != 0)
            calcFieldLabel2.setText((String) calcFieldNames.get(0));

        int calcFieldLabel2Width = fm.stringWidth(calcFieldLabel2.getText());
        calcFieldRecordWidth = calcFieldLabel2Width+column1Width;

//        firstCalcFieldSubPanel.add(Box.createHorizontalStrut(10));
        firstCalcFieldSubPanel.add(calcFieldLabel);
        firstCalcFieldSubPanel.add(Box.createHorizontalStrut(column1Width-calcFieldLabelWidth));
        firstCalcFieldSubPanel.add(calcFieldLabel2);

        calcFieldPanel.add(firstCalcFieldSubPanel);

        for (int i=1; i<calcFieldNames.size(); i++) {
            calcFieldLabel2Width = fm.stringWidth((String) calcFieldNames.get(i));
            if (calcFieldRecordWidth < (calcFieldLabel2Width+column1Width))
                calcFieldRecordWidth = calcFieldLabel2Width+column1Width;

            calcFieldPanel.add(createEmptyPanel((String) calcFieldNames.get(i), false));
        }

//        calcFieldPanel.setBorder(new EtchedBorder());
        return calcFieldPanel;

    }
}