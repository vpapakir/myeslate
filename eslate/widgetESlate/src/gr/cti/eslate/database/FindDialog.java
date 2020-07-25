package gr.cti.eslate.database;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import com.objectspace.jgl.Array;
import com.objectspace.jgl.IntArray;
import gr.cti.typeArray.IntBaseArray;
import gr.cti.eslate.database.engine.CellAddress;

class FindDialog extends JDialog {
    int dialogWidth = 500;
    int dialogHeight = 175;
    Color titleBorderColor = new Color(119, 40, 104);
    JCheckBox caseBox, columnBox, rowBox;
    JRadioButton up, down;
    JTextField findWhatField;
    JButton findFirstButton, findNextButton;
    DBTable dbTable;
    boolean findWhatFieldEmpty = true;

    JButton closeButton;

    protected FindDialog(Frame frame, DBTable table) {
        super(frame, true);
        this.dbTable = table;

//        getContentPane().setBackground(Color.lightGray);

        JPanel findWhatPanel = new JPanel(true);
        findWhatPanel.setLayout(new BoxLayout(findWhatPanel, BoxLayout.X_AXIS));
        JLabel findWhatLabel = new JLabel(dbTable.bundle.getString("Find What"));
        findWhatField = new JTextField();
        Dimension d = new Dimension(225, 23); //265, 23);
//        findWhatField.setMaximumSize(d);
//        findWhatField.setMinimumSize(d);
//        findWhatField.setPreferredSize(d);

//        findWhatPanel.add(Box.createHorizontalStrut(5));
        findWhatPanel.add(findWhatLabel);
        findWhatPanel.add(Box.createHorizontalStrut(5));
        findWhatPanel.add(findWhatField);
        findWhatPanel.setBorder(new EmptyBorder(0, 2, 2, 2));
//        findWhatPanel.add(Box.createGlue());
//        findWhatPanel.setBorder(new LineBorder(Color.black)); //0, 0, 0, 10));

        JPanel upDownPanel = new JPanel(true);
        upDownPanel.setLayout(new BoxLayout(upDownPanel, BoxLayout.Y_AXIS));
        up = new JRadioButton(dbTable.bundle.getString("Up"), false);
        down = new JRadioButton(dbTable.bundle.getString("Down"), true);
        ButtonGroup bgr = new ButtonGroup();
        bgr.add(up);
        bgr.add(down);

        upDownPanel.add(up);
        upDownPanel.add(down);
        upDownPanel.add(Box.createGlue());
        TitledBorder tb1 = new TitledBorder(dbTable.bundle.getString("Direction"));
        tb1.setTitleColor(titleBorderColor);
        upDownPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0, 5, 0, 5)));

        JPanel paramPanel = new JPanel(true);
        paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
        TitledBorder tb2 = new TitledBorder(dbTable.bundle.getString("Search Parameters"));
        tb2.setTitleColor(titleBorderColor);
        paramPanel.setBorder(new CompoundBorder(tb2, new EmptyBorder(0, 5, 0, 5)));
        caseBox = new JCheckBox(dbTable.bundle.getString("Match Case"));
//        caseBox.setFont(dbTable.dbComponent.UIFont);
        caseBox.setFocusPainted(false);
        columnBox = new JCheckBox(dbTable.bundle.getString("Search Selected Columns"));
//        columnBox.setFont(dbTable.dbComponent.UIFont);
        columnBox.setFocusPainted(false);
        rowBox = new JCheckBox(dbTable.bundle.getString("Search Selected Rows"));
        rowBox.setFocusPainted(false);

        paramPanel.add(caseBox);
        paramPanel.add(columnBox);
        paramPanel.add(rowBox);
        paramPanel.add(Box.createGlue());

        JPanel findPropertiesPanel = new JPanel(true);
        findPropertiesPanel.setLayout(new BoxLayout(findPropertiesPanel, BoxLayout.X_AXIS));

        findPropertiesPanel.add(upDownPanel);
        findPropertiesPanel.add(Box.createHorizontalStrut(20));
        findPropertiesPanel.add(paramPanel);

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(findWhatPanel);
        mainPanel.add(Box.createGlue()); //createVerticalStrut(10));
        mainPanel.add(findPropertiesPanel);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        d = new Dimension(105, 100);
        buttonPanel.setMaximumSize(d);
        buttonPanel.setMinimumSize(d);
        buttonPanel.setPreferredSize(d);
  //      buttonPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        d = new Dimension(105, 28);
        Insets nullinsets = new Insets(0, 0, 0, 0);
        findFirstButton = new JButton(dbTable.bundle.getString("Find First"));
        findFirstButton.setMaximumSize(d);
        findFirstButton.setMinimumSize(d);
        findFirstButton.setPreferredSize(d);
        findFirstButton.setEnabled(false);
        findFirstButton.setFocusPainted(false);
        findFirstButton.setMargin(nullinsets);
        findNextButton = new JButton(dbTable.bundle.getString("Find Next"));
        findNextButton.setMaximumSize(d);
        findNextButton.setMinimumSize(d);
        findNextButton.setPreferredSize(d);
        findNextButton.setEnabled(false);
        findNextButton.setFocusPainted(false);
        findNextButton.setMargin(nullinsets);
        closeButton = new JButton(dbTable.bundle.getString("Close"));
        closeButton.setMaximumSize(d);
        closeButton.setMinimumSize(d);
        closeButton.setPreferredSize(d);
        closeButton.setFocusPainted(false);
        closeButton.setMargin(nullinsets);

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(findFirstButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(findNextButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createGlue());

        up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!findWhatFieldEmpty) {
                    findNextButton.setEnabled(true);
                }
            }
        });

        down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!findWhatFieldEmpty) {
                    findNextButton.setEnabled(true);
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DBTable.SearchManager findManager = dbTable.getSearchManager();
                findManager.upwards = up.isSelected();
                findManager.ignoreCase = !caseBox.isSelected();
                findManager.selectedFieldsOnly = columnBox.isSelected();
                findManager.selectedRecordsOnly = rowBox.isSelected();
                String token = findWhatField.getText();
                if (token != null && token.trim().length() == 0)
                    token = null;
                findManager.setToken(token);
                dbTable.tipManager.resetTip();
                dispose();
            }
        });

/*1        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!findWhatFieldEmpty) {
                    findFirstButton.setEnabled(true);
                    findNextButton.setEnabled(true);
                    //dbTable.dbComponent.standardToolBar.findPrev.setEnabled(true);
                    dbTable.findPrevAction.setEnabled(true);
                    //dbTable.dbComponent.standardToolBar.findNext.setEnabled(true);
                    dbTable.findNextAction.setEnabled(true);
                }
            }
        };

        caseBox.addActionListener(al);
        columnBox.addActionListener(al);
        rowBox.addActionListener(al);
1*/
        findFirstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DBTable.SearchManager findManager = dbTable.getSearchManager();
                findManager.upwards = up.isSelected();
                findManager.ignoreCase = !caseBox.isSelected();
                findManager.selectedFieldsOnly = columnBox.isSelected();
                findManager.selectedRecordsOnly = rowBox.isSelected();
                String token = findWhatField.getText();
                if (token != null && token.trim().length() == 0)
                    token = null;
                findManager.setToken(token);
                CellAddress cell = dbTable.getSearchManager().find(0, 0, true);
                if (cell == null) {
                    findNextButton.setEnabled(false);
                    findFirstButton.setEnabled(false);
                    dbTable.tipManager.showTip('\"' + token + '\"' + dbTable.bundle.getString("Not Found"), 50, 20);
                }else{
                    findNextButton.setEnabled(true);
                    findFirstButton.setEnabled(true);
                    dbTable.setActiveCell(cell.recordIndex, cell.fieldIndex, true);
                }
            }
        });


        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DBTable.SearchManager findManager = dbTable.getSearchManager();
                findManager.upwards = up.isSelected();
                findManager.ignoreCase = !caseBox.isSelected();
                findManager.selectedFieldsOnly = columnBox.isSelected();
                findManager.selectedRecordsOnly = rowBox.isSelected();
                String token = findWhatField.getText();
                if (token != null && token.trim().length() == 0)
                    token = null;
                findManager.setToken(token);
                CellAddress cell = dbTable.getSearchManager().find(dbTable.activeRow, dbTable.jTable.getSelectedColumn(), false);
                if (cell == null) {
                    findNextButton.setEnabled(false);
                }else{
                    findNextButton.setEnabled(true);
                    dbTable.setActiveCell(cell.recordIndex, cell.fieldIndex, true);
                }
            }
        });


        findWhatField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (findWhatField.getText() != null && findWhatField.getText().length() != 0) {
                    findWhatFieldEmpty = false;
                    findFirstButton.setEnabled(true);
                    findNextButton.setEnabled(true);
                }else{
                    findWhatFieldEmpty = true;
                    findFirstButton.setEnabled(false);
                    findNextButton.setEnabled(false);
                }
            }
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (findWhatField.getText() != null && findWhatField.getText().length() != 0) {
                        findFirstButton.setEnabled(true);
                        findNextButton.setEnabled(true);
                    }else{
                        findFirstButton.setEnabled(false);
                        findWhatFieldEmpty = true;
                        findNextButton.setEnabled(false);
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (findWhatField.getText() != null && findWhatField.getText().length()!=0)
                        findFirstButton.doClick();
                }
            }
        });


        JPanel contentPanel = new JPanel(true);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        contentPanel.setBorder(new MatteBorder(10, 10, 10, 10, UIManager.getColor("control")));
        contentPanel.add(mainPanel);
        contentPanel.add(Box.createHorizontalStrut(5));
        contentPanel.add(buttonPanel);

        getContentPane().add(contentPanel);

        setTitle(dbTable.bundle.getString("Find in Table"));
        setResizable(false);
//        setSize(dialogWidth, dialogHeight);
//    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//	    setLocation(screenSize.width/2 - dialogWidth/2, screenSize.height/2 - dialogHeight/2);
        pack();

        /* Resize the findWhatField, so that its height is 23
         */
        d = new Dimension(findPropertiesPanel.getSize().width-findWhatLabel.getSize().width-5, 23);
        findWhatField.setMaximumSize(d);
        findWhatField.setMinimumSize(d);
        findWhatField.setPreferredSize(d);
        doLayout();
        pack();
        findWhatField.requestFocus();

        java.awt.Rectangle dbBounds = dbTable.getBounds();
        System.out.println("getLocationOnScreen() 3");
        java.awt.Point dbLocation = dbTable.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + dbTable.dbComponent.getLocationOnScreen());
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

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		dispose();
          		javax.swing.ButtonModel bm = closeButton.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Initialization
        initializeDialog();
    }


    public void display(DBTable dbTable) {
        findNextButton.setEnabled(true);
        findFirstButton.setEnabled(true);

        this.dbTable = dbTable;
        initializeDialog();

        pack();
        findWhatField.requestFocus();
        java.awt.Rectangle dbBounds = dbTable.getBounds();
        System.out.println("getLocationOnScreen() 4");
        java.awt.Point dbLocation = dbTable.getLocationOnScreen();
//        System.out.println("dbBounds: " + dbBounds + " location: " + dbTable.dbComponent.getLocationOnScreen());
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

    private void initializeDialog() {
        rowBox.setSelected(dbTable.getSearchManager().selectedRecordsOnly);
        columnBox.setSelected(dbTable.getSearchManager().selectedFieldsOnly);
        findWhatField.setText(dbTable.getSearchManager().getToken());
        caseBox.setSelected(!dbTable.getSearchManager().ignoreCase);
        up.setSelected(dbTable.getSearchManager().upwards);

        findWhatField.selectAll();

        if (dbTable.table.getSelectedRecordCount() == 0) {
            rowBox.setSelected(false);
            rowBox.setEnabled(false);
        }else
            rowBox.setEnabled(true);

        int numOfSelectedColumns;
        if (!dbTable.isSimultaneousFieldRecordActivation()) {
           /* No column selection is allowed, so "numOfSelectedColumns" = 0.
            * However, when a field header is clicked, we explicitly set "columnSelectionAllowed"
            * to "true", so that the field will be selected. In this case calculate the selected
            * columns.
            */
           if (dbTable.jTable.getColumnSelectionAllowed())
               numOfSelectedColumns = dbTable.jTable.getSelectedColumnCount();
           else
               numOfSelectedColumns = 0;
        }else
           numOfSelectedColumns = dbTable.jTable.getSelectedColumnCount();

        if (numOfSelectedColumns == 0) {
            columnBox.setEnabled(false);
            columnBox.setSelected(false);
        }else
            columnBox.setEnabled(true);
    }
}


