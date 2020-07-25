package gr.cti.eslate.database;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import com.objectspace.jgl.Array;
import gr.cti.eslate.database.engine.*;
import gr.cti.typeArray.StringBaseArray;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.*;


class ThJoinDialog extends JDialog {
    int dialogWidth = 548; //528;
    int dialogHeight = 331; //313; //426; //626;
    int tablePanelWidth = (dialogWidth/2) - 63;
    int tablePanelHeight = dialogHeight - 89;
    int tableBoxesWidth = tablePanelWidth - 20;
    int tableBoxesHeight = 20;
    int activeExpression;

    static int clickedButton = 0;
    StringBaseArray table1FieldNames, table2FieldNames;
    Table table1, table2;
    Database dbComponent;
    static Array table1Boxes = new Array();
    static Array table2Boxes = new Array();
    static Array compBoxes = new Array();
    JPanel table1Panel = new JPanel();
    JPanel table2Panel = new JPanel();
    JPanel comparPanel = new JPanel();
    JButton ok, cancel, newExpression, remExpression;
    JComboBox box1, box2, box3;
    JPanel exprPanel;
    JScrollPane thJoinScrollPane;

    transient ResourceBundle infoBundle; // = ResourceBundle.getBundle("gr.cti.eslate.database.InfoBundle", Locale.getDefault());


    protected ThJoinDialog(Frame frame, Table table1, Table table2, Database dbComponent) {
        super(frame, true);
        infoBundle = dbComponent.infoBundle;
        this.dbComponent = dbComponent;
        setTitle(infoBundle.getString("ThJoinDialogMsg1") + table1.getTitle() + " - " + table2.getTitle());

        // ESCAPE HANDLER
        this.getRootPane().registerKeyboardAction(new ActionListener() {
      		public void actionPerformed(ActionEvent e) {
          		cancel.doClick();
          		javax.swing.ButtonModel bm = cancel.getModel();
              bm.setPressed(false);
          }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false),
				  	this.getRootPane().WHEN_IN_FOCUSED_WINDOW);

        table1FieldNames = table1.getFieldNames();
        table2FieldNames = table2.getFieldNames();
        this.table1 = table1;
        this.table2 = table2;
        Color color128 = new Color(0,0,128);
        Color titleBorderColor = new Color(119, 40, 104);
        table1Boxes.clear(); table2Boxes.clear(); compBoxes.clear();

//        getContentPane().setBackground(Color.lightGray);
        BoxLayout bl1 = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        getContentPane().setLayout(bl1);

        exprPanel = new JPanel();
        BoxLayout bl2 = new BoxLayout(exprPanel, BoxLayout.X_AXIS);
        exprPanel.setLayout(bl2);

        BoxLayout bl3 = new BoxLayout(table1Panel, BoxLayout.Y_AXIS);
        table1Panel.setLayout(bl3);
        BoxLayout bl4 = new BoxLayout(comparPanel, BoxLayout.Y_AXIS);
        comparPanel.setLayout(bl4);
        BoxLayout bl5 = new BoxLayout(table2Panel, BoxLayout.Y_AXIS);
        table2Panel.setLayout(bl5);

        createNewExpression();

        table1Panel.setPreferredSize(new Dimension(tablePanelWidth, tablePanelHeight));
        table1Panel.setMaximumSize(new Dimension(tablePanelWidth, tablePanelHeight));
        table1Panel.setBorder(new TitledBorder(table1.getTitle()));
        ((TitledBorder) table1Panel.getBorder()).setTitleColor(titleBorderColor);
        table1Panel.setAlignmentY(TOP_ALIGNMENT);
        comparPanel.setPreferredSize(new Dimension(80, tablePanelHeight));
        comparPanel.setMaximumSize(new Dimension(80, tablePanelHeight));
        comparPanel.setBorder(new EmptyBorder(21, 0, 0, 0));
        comparPanel.setAlignmentY(TOP_ALIGNMENT);
        table2Panel.setPreferredSize(new Dimension(tablePanelWidth, tablePanelHeight));
        table2Panel.setMaximumSize(new Dimension(tablePanelWidth, tablePanelHeight));
        table2Panel.setBorder(new TitledBorder(table2.getTitle()));
        ((TitledBorder) table2Panel.getBorder()).setTitleColor(titleBorderColor);
        table2Panel.setAlignmentY(TOP_ALIGNMENT);


        exprPanel.add(table1Panel);
        exprPanel.add(Box.createHorizontalStrut(10));
        exprPanel.add(comparPanel);
        exprPanel.add(Box.createHorizontalStrut(10));
        exprPanel.add(table2Panel);

        thJoinScrollPane = new ThJoinScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JViewport viewport = thJoinScrollPane.getViewport();
//        viewport.setSize(dialogWidth-20, 200);
        viewport.setView(exprPanel);
        viewport.setPreferredSize(new Dimension(dialogWidth, tablePanelHeight));
        viewport.setMaximumSize(new Dimension(dialogWidth, tablePanelHeight));
        viewport.setMinimumSize(new Dimension(dialogWidth, tablePanelHeight));
        viewport.setViewPosition(new Point(0, 0));

        thJoinScrollPane.setMaximumSize(new Dimension(dialogWidth, tablePanelHeight));
        thJoinScrollPane.setMinimumSize(new Dimension(dialogWidth, tablePanelHeight));
        thJoinScrollPane.setPreferredSize(new Dimension(dialogWidth, tablePanelHeight));
        thJoinScrollPane.setBorder(null); //new EtchedBorder());

        getContentPane().add(thJoinScrollPane);

        JPanel buttonPanel = new JPanel();
        Font buttonFont;
//        if (infoBundle.getClass().getName().equals("gr.cti.eslate.database.InfoBundle_el_GR"))
//            buttonFont = new Font("Helvetica", Font.PLAIN, 12);
//        else
//            buttonFont = new Font("Dialog", Font.PLAIN, 12);

        ok = new JButton(infoBundle.getString("OK"));
        ok.setMaximumSize(new Dimension(123, 30));
        ok.setMinimumSize(new Dimension(123, 30));
//        ok.setFont(buttonFont);
        ok.setEnabled(true);
        ok.setForeground(color128);
        newExpression = new JButton(infoBundle.getString("ThJoinDialogMsg2"));
        newExpression.setMaximumSize(new Dimension(130, 30));
        newExpression.setMinimumSize(new Dimension(130, 30));
//        newExpression.setFont(buttonFont);
        newExpression.setForeground(color128);
        newExpression.setEnabled(true);
        remExpression = new JButton(infoBundle.getString("ThJoinDialogMsg3"));
        remExpression.setMaximumSize(new Dimension(145, 30));
        remExpression.setMinimumSize(new Dimension(145, 30));
//        remExpression.setFont(buttonFont);
        remExpression.setForeground(color128);
        remExpression.setEnabled(true);
        cancel = new JButton(infoBundle.getString("Cancel"));
        cancel.setMaximumSize(new Dimension(123, 30));
        cancel.setMinimumSize(new Dimension(123, 30));
//        cancel.setFont(buttonFont);
        cancel.setForeground(color128);

        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(newExpression);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(remExpression);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(ok);
        buttonPanel.add(Box.createHorizontalStrut(10));

        BoxLayout bl6 = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.setLayout(bl6);
        buttonPanel.setPreferredSize(new Dimension(dialogWidth, 33));
        buttonPanel.setMaximumSize(new Dimension(dialogWidth, 33));

        getContentPane().add(Box.createVerticalStrut(20));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(10));

        newExpression.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    createNewExpression();
                }catch (Exception e1) {System.out.println(e.getClass().getName());}

                thJoinScrollPane.validate();
//                exprPanel.repaint();
                remExpression.setEnabled(true);
                ok.setEnabled(true);


                int k = table1Boxes.size() - 5;
//                System.out.println("table1Boxes.size(): " + table1Boxes.size());
                if (table1Boxes.size() > 5) {
                    int boxHeight = ((JComboBox) table1Boxes.at(5)).getHeight();
                    int newHeight = tablePanelHeight + (k*(boxHeight+15));
//                    System.out.println("table1Panel: " + table1Panel.getHeight());
                    table1Panel.setPreferredSize(new Dimension(tablePanelWidth, newHeight));
                    table1Panel.setMaximumSize(new Dimension(tablePanelWidth, newHeight));
                    comparPanel.setPreferredSize(new Dimension(80, newHeight));
                    comparPanel.setMaximumSize(new Dimension(80, newHeight));
                    table2Panel.setPreferredSize(new Dimension(tablePanelWidth, newHeight));
                    table2Panel.setMaximumSize(new Dimension(tablePanelWidth, newHeight));
//                    System.out.println("table1Panel: " + table1Panel.getHeight());

//                    box1.scrollRectToVisible(box1.getBounds());
//                    box1.requestFocus();

                }

                Rectangle scrollTo = box1.getBounds();
                scrollTo.grow(0, 24);
                    exprPanel.scrollRectToVisible(scrollTo);
                    box1.requestFocus();
            }
        });

        remExpression.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Removing: " +activeExpression);
                table1Panel.remove(2*activeExpression);
//                System.out.println("Here1");
                table1Panel.validate();
                table1Panel.remove(2*activeExpression);
                comparPanel.remove(2*activeExpression);
                comparPanel.remove(2*activeExpression);
//                System.out.println("Here2");
                comparPanel.validate();
                table2Panel.remove(2*activeExpression);
                table2Panel.remove(2*activeExpression);
//                System.out.println("Here3");
                table2Panel.validate();
                exprPanel.validate();
                exprPanel.repaint();
                table1Boxes.remove(activeExpression);
//                System.out.println("table1Boxes: " + table1Boxes);
                compBoxes.remove(activeExpression);
                table2Boxes.remove(activeExpression);
                JComboBox boxToAcquireFocus = null;;
                if (table1Boxes.size() != 0) {
                    if (activeExpression == table1Boxes.size()) {
//                        System.out.println(activeExpression + ", " + ((JComboBox) table1Boxes.at(activeExpression-1)));
                        boxToAcquireFocus = (JComboBox) table1Boxes.at(activeExpression-1);
                        boxToAcquireFocus.requestFocus();
                    }else{
//                        System.out.println(activeExpression + ", " + ((JComboBox) table1Boxes.at(activeExpression)));
                        boxToAcquireFocus = (JComboBox) table1Boxes.at(activeExpression);
                        boxToAcquireFocus.requestFocus();
                    }
                }else{
                    ok.setEnabled(false);
                    remExpression.setEnabled(false);
                }

                int k = table1Boxes.size() - 6;
                if (table1Boxes.size() > 6) {
                    int boxHeight = ((JComboBox) table1Boxes.at(6)).getHeight();
                    int newHeight = tablePanelHeight + (k*(boxHeight+15));
                    table1Panel.setPreferredSize(new Dimension(tablePanelWidth, newHeight));
                    table1Panel.setMaximumSize(new Dimension(tablePanelWidth, newHeight));
                    comparPanel.setPreferredSize(new Dimension(80, newHeight));
                    comparPanel.setMaximumSize(new Dimension(80, newHeight));
                    table2Panel.setPreferredSize(new Dimension(tablePanelWidth, newHeight));
                    table2Panel.setMaximumSize(new Dimension(tablePanelWidth, newHeight));

                    if (boxToAcquireFocus != null)
                        exprPanel.scrollRectToVisible(boxToAcquireFocus.getBounds());
                    thJoinScrollPane.validate();
                    thJoinScrollPane.repaint();

                    if (activeExpression > 6)
                        ((JComboBox) table1Boxes.at(activeExpression-6)).scrollRectToVisible(((JComboBox) table1Boxes.at(activeExpression-6)).getBounds());
                    else
                        ((JComboBox) table1Boxes.at(0)).scrollRectToVisible(((JComboBox) table1Boxes.at(0)).getBounds());

                }else{
                    table1Panel.setPreferredSize(new Dimension(tablePanelWidth, tablePanelHeight));
                    table1Panel.setMaximumSize(new Dimension(tablePanelWidth, tablePanelHeight));
                    comparPanel.setPreferredSize(new Dimension(80, tablePanelHeight));
                    comparPanel.setMaximumSize(new Dimension(80, tablePanelHeight));
                    table2Panel.setPreferredSize(new Dimension(tablePanelWidth, tablePanelHeight));
                    table2Panel.setMaximumSize(new Dimension(tablePanelWidth, tablePanelHeight));

                    thJoinScrollPane.validate();
                    thJoinScrollPane.repaint();
                }

            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickedButton = 0;
	            dispose();
	        }
	    });

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickedButton = 1;
	            dispose();
	        }
	    });

        /* Add the window listener.
         */
    	addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
//                thisDialog.removeWindowListener(l);
                clickedButton = 0;
	            dispose();
	        }
	    });

        /* Dispaying the "ThJoinDialog".
         */
        setResizable(false);
//        setModal(true);
        setSize(dialogWidth, dialogHeight);
//    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//	    setLocation(screenSize.width/2 - dialogWidth/2, screenSize.height/2 - dialogHeight/2);
        pack();
        Rectangle dbBounds = dbComponent.getBounds();
        System.out.println("getLocationOnScreen() 14");
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
        setVisible(true);
    }


    protected JComboBox createTable1FieldNamesBox() {
        JComboBox box = new JComboBox();

        for (int i=0; i<table1FieldNames.size(); i++)
            box.addItem((String) table1FieldNames.get(i));

        return box;
    }


    protected JComboBox createTable2FieldNamesBox() {
        JComboBox box = new JComboBox();

        String fieldName = (String) box1.getSelectedItem();
        AbstractTableField f;
        try{
            f = table1.getTableField(fieldName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in ThJoinDialog ThJoinDialog() : (2)"); return null;}
        String fieldType = f.getDataType().getName();
        TableFieldBaseArray table2Fields = table2.getFields();
        for (int i=0; i< table2Fields.size(); i++) {
            if (((AbstractTableField) table2Fields.get(i)).getDataType().getName().equals(fieldType)) /*&& f.isDate() == ((TableField) table2Fields.get(i)).isDate())*/
                box.addItem(((AbstractTableField) table2Fields.get(i)).getName());
        }

        return box;
    }


    protected JComboBox createComparatorsBox(String fieldName) {
        AbstractTableField f;
        try{
            f = table1.getTableField(fieldName);
        }catch (InvalidFieldNameException e) {System.out.println("Serious inconsistency error in ThJoinDialog createComparatorsBox() : (1)"); return null;}

        HashMap comparators = dbComponent.getDBase().getComparatorsForDataType(f.getDataType());

        JComboBox box = new JComboBox();
        Iterator iter = comparators.keySet().iterator();
        while (iter.hasNext())
            box.addItem((String) iter.next());
//        for (int i=0; i<comparators.size(); i++)
//            box.addItem((String) comparators.get(i));

        return box;
    }


    protected void createNewExpression() {
        box1 = createTable1FieldNamesBox();
        table1Boxes.add(box1);

        box1.setPreferredSize(new Dimension(tableBoxesWidth, tableBoxesHeight));

        box1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                activeExpression = table1Boxes.indexOf(e.getSource());
//                System.out.println(activeExpression);
            }
        });
        box1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {

                String fieldName = (String) ((JComboBox) table1Boxes.at(activeExpression)).getSelectedItem();
                JComboBox secBox = (JComboBox) compBoxes.at(activeExpression);
                JComboBox thirdBox = (JComboBox) table2Boxes.at(activeExpression);

//                System.out.println("fieldName: " + fieldName);
                AbstractTableField f;
                try{
                    f = table1.getTableField(fieldName);
                }catch (InvalidFieldNameException e1) {System.out.println("Serious inconsistency error in ThJoinDialog ThJoinDialog() : (2)"); return;}
                String fieldType = f.getDataType().getName();

                HashMap comparators = dbComponent.getDBase().getComparatorsForDataType(f.getDataType());
                secBox.removeAllItems();
                Iterator iter = comparators.keySet().iterator();
                while (iter.hasNext())
                    secBox.addItem((String) iter.next());
//                for (int i=0; i<comparators.size(); i++)
//                    secBox.addItem(comparators.get(i));

                thirdBox.removeAllItems();
                TableFieldBaseArray table2Fields = table2.getFields();
                for (int i=0; i< table2Fields.size(); i++) {
                    if (((AbstractTableField) table2Fields.get(i)).getDataType().getName().equals(fieldType)) /* && f.isDate() == ((TableField) table2Fields.get(i)).isDate())*/
                        thirdBox.addItem(((AbstractTableField) table2Fields.get(i)).getName());
                }
            }
        });

        box2 = createComparatorsBox((String) box1.getSelectedItem());
        compBoxes.add(box2);
        box2.setPreferredSize(new Dimension(80, tableBoxesHeight));
        box2.setMaximumSize(new Dimension(80, tableBoxesHeight));
        box2.setMinimumSize(new Dimension(80, tableBoxesHeight));
        box2.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                activeExpression = compBoxes.indexOf(e.getSource());
//                System.out.println(activeExpression);
            }
        });

        box3 = createTable2FieldNamesBox();
        table2Boxes.add(box3);
        box3.setPreferredSize(new Dimension(tableBoxesWidth, tableBoxesHeight));
        box3.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                activeExpression = table2Boxes.indexOf(e.getSource());
//                System.out.println(activeExpression);
            }
        });

        table1Panel.add(box1);
        table1Panel.add(Box.createVerticalStrut(15));
        comparPanel.add(box2);
        comparPanel.add(Box.createVerticalStrut(15));
        table2Panel.add(box3);
        table2Panel.add(Box.createVerticalStrut(15));

        box1.requestFocus();
    }

}


class ThJoinScrollPane extends JScrollPane {
    public ThJoinScrollPane(int vsp, int hsp) {
        super(vsp, hsp);
    }

    public JScrollBar createVerticalScrollBar() {
        return new ThJoinVerticalScrollBar();
    }
}


class ThJoinVerticalScrollBar extends JScrollBar
{
    public int getBlockIncrement(int direction) {
        return 36;
    }

    public int getUnitIncrement(int direction) {
        return 36;
    }

    public ThJoinVerticalScrollBar() {
        super(JScrollBar.VERTICAL);
    }
}
