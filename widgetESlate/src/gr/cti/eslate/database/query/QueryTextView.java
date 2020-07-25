package gr.cti.eslate.database.query;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.DefaultListModel;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JList;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import com.thwt.layout.*;

import gr.cti.eslate.jeditlist.JEditList;
import gr.cti.eslate.database.engine.InvalidFieldNameException;
import gr.cti.eslate.database.engine.*;
import gr.cti.eslate.utils.ESlateClipboard;

//
// -------- Solution for minimizing the living objects ----------
//  should be added each time to a new panel the tabs do not contain any kind
//  of panels(null) but the viewer panel hosts each time and a new view
//  according to the table selected(tab selected) and the if the queryPane button
//  is set or not


public class QueryTextView extends JPanel{

    ResourceBundle infoBundle;
    private boolean fromUpdate=false;
    private int selectionIndex;
   // The elements that consist the UI of the QueryText View
    QueryComponent queryComponent;
    private JTextArea queryArea;
    private JPanel buttonsPanel;
    private JEditList fieldList,fieldValuesList;
    // See if it needs to be protected as the changes to their appearance
    // takes place only in this class.
    protected JButton equal, notEqual, less, lessEqual, greater, greaterEqual;
    protected JButton add, or, not, contains, contained;
    protected JButton brackets, quotes, parenths;
    private JPanel firstButtonPanel, secondButtonPanel, thirdButtonPanel;
    // Extra Panels and ScrollPanes needed to handle the work.
    private JScrollPane fieldValuesScrollPane;// stands for scrollpanel2
    private DefaultListModel listModel;
//    private Font dialogFont;
    private GridBagLayout gridbag = new GridBagLayout();
    public JPanel textQueryTopPanel;
    //private MyListHandle myListHandle;


    public QueryTextView(QueryComponent compo, DefaultListModel model) {
        super(true);
        queryComponent=compo;

        infoBundle = ResourceBundle.getBundle("gr.cti.eslate.database.query.InfoBundle", Locale.getDefault());
        if (model == null)
            listModel = new DefaultListModel();
        else{
            listModel = model;
            init();
        }
    }

    private void init() {
        queryArea=new JTextArea();
        buttonsPanel = new JPanel(true);
        fieldList = new JEditList(listModel);
        fieldValuesList = new JEditList();

        initQueryArea();
        initButtonsPanel();
        initFieldList();
        initFieldValuesList();
        updateFieldList(listModel,0);
      //  setFieldListValues(0);

        arrangePanels();
    }

    private void arrangePanels() {
        fieldValuesScrollPane = new JScrollPane();
        fieldValuesScrollPane.getViewport().setView(fieldValuesList);

        JScrollPane fieldlistScrollPane = new JScrollPane();
        fieldlistScrollPane.getViewport().setView(fieldList);

        final JScrollPane queryAreaScrollPane = new JScrollPane(queryArea);
        queryAreaScrollPane.setBorder(null);

        // Place the queryArea's scrollPane inside a BorderLayout JPanel.
        JPanel textAreaPanel = new JPanel(true);
        textAreaPanel.setLayout(new BorderLayout());
        textAreaPanel.setBackground(Color.lightGray);
        textAreaPanel.add(queryAreaScrollPane, BorderLayout.CENTER);

        Dimension textAreaPanelDimension = new Dimension(100,75);
        textAreaPanel.setMaximumSize(textAreaPanelDimension);
        textAreaPanel.setMinimumSize(textAreaPanelDimension);
        textAreaPanel.setPreferredSize(textAreaPanelDimension);

        textQueryTopPanel = new JPanel(true);
        textQueryTopPanel.setLayout(new SmartLayout());
        textQueryTopPanel.add(buttonsPanel, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.FixedDimensionAnchor(Anchor.Width, 150),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.FractionAnchor(null, Anchor.HorizontalCenter, 0.0, Anchor.Right, Anchor.HorizontalCenter, 0.0, 0)));
        textQueryTopPanel.add(fieldlistScrollPane, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Left, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.EdgeAnchor(buttonsPanel, Anchor.Left, Anchor.Left, Anchor.Right, 3)));
        textQueryTopPanel.add(fieldValuesScrollPane, new com.thwt.layout.LayoutConstraint(
					new com.thwt.layout.ContainerAnchor(Anchor.Right, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Top, 5),
					new com.thwt.layout.ContainerAnchor(Anchor.Bottom, 5),
					new com.thwt.layout.EdgeAnchor(buttonsPanel, Anchor.Right, Anchor.Right, Anchor.Left, 3)));


        textQueryTopPanel.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.blue);
        this.add(textQueryTopPanel, BorderLayout.CENTER);
        this.add(textAreaPanel, BorderLayout.SOUTH);
        setVisible(true);

    }




    private void initQueryArea() {
        //Change all the buttons. Now they are local to the class

        queryArea.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        queryArea.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        queryArea.setVisible(true);
        queryArea.setRows(3);
        queryArea.setToolTipText(infoBundle.getString("queryAreaTip"));



        queryArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String s = queryArea.getSelectedText();
                if (s == null || s.length() == 0) {
                    if (parenths.isEnabled()) {
                        parenths.setEnabled(false);
                        brackets.setEnabled(false);
                        quotes.setEnabled(false);
                    }
                } else {
                    if (!parenths.isEnabled()) {
                        parenths.setEnabled(true);
                        brackets.setEnabled(true);
                        quotes.setEnabled(true);
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                queryArea.getCaret().setVisible(true);
            }
        });

        queryArea.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                String s = queryArea.getSelectedText();
                if (s == null || s.length() == 0) {
                    if (parenths.isEnabled()) {
                        parenths.setEnabled(false);
                        brackets.setEnabled(false);
                        quotes.setEnabled(false);
                    }
                } else {
                    if (!parenths.isEnabled()) {
                        parenths.setEnabled(true);
                        brackets.setEnabled(true);
                        quotes.setEnabled(true);
                    }
                }
            }
        });

        queryArea.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
//                System.out.println("Clipboard contents: " + ESlateClipboard.getContents());
                JTextArea source = (JTextArea) e.getSource();

                if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
                    // If the ESlateClipboard is empty, then proceed an copy in the
                    //  TextArea anything that is in the System Clipboard.

                    if (ESlateClipboard.getContents() == null) {
                        return;
                    }
                    // Special translation for the 2 boolean strings

                    String contents = ESlateClipboard.getContents().toString();
                    if (contents.toLowerCase().equals("true")) {
                        contents = queryComponent.infoBundle.getString("true");
                    } else {
                         if (contents.toLowerCase().equals("false")) {
                            contents = queryComponent.infoBundle.getString("false");
                         }
                    }

                    java.awt.datatransfer.Clipboard clipboard = getToolkit().getSystemClipboard();
                    clipboard.setContents(new java.awt.datatransfer.StringSelection(contents),
                                          queryComponent.defaultClipboardOwner);
                }

                if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
                    String selectedText = source.getSelectedText();
                    if (selectedText != null && selectedText.length() != 0) {
                        try {
                            ESlateClipboard.setContents(selectedText);
                        } catch (java.io.NotSerializableException exc) {
                        }
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_X && e.isControlDown()) {
                    String selectedText = source.getSelectedText();
                    if (selectedText != null && selectedText.length() != 0) {
                        try{
                            ESlateClipboard.setContents(selectedText);
                        } catch (java.io.NotSerializableException exc) {
                        }
                    }
                }

                String s = queryArea.getSelectedText();
                if (s == null || s.length() == 0) {
                    if (parenths.isEnabled()) {
                        parenths.setEnabled(false);
                        brackets.setEnabled(false);
                        quotes.setEnabled(false);
                    }
                    queryArea.validate();
                    queryArea.paintImmediately(queryArea.getVisibleRect());
                } else {
                    if (!parenths.isEnabled()) {
                        parenths.setEnabled(true);
                        brackets.setEnabled(true);
                        quotes.setEnabled(true);
                    }
                }
                // Change when they move to the toolbar
                String s1 = queryArea.getText();
                if (s1 == null || s1.length() == 0) {
                    queryComponent.execute.setEnabled(false);
                    queryComponent.clearQuery.setEnabled(false);
                }else{
                    queryComponent.execute.setEnabled(true);
                    queryComponent.clearQuery.setEnabled(true);
                }
            }
            // Change when they move to the toolbar
            public void keyTyped(KeyEvent e) {
                String s1 = queryArea.getText();
                if (s1 == null || s1.length() == 0) {
                    queryComponent.execute.setEnabled(false);
                    queryComponent.clearQuery.setEnabled(false);
                } else {
                    queryComponent.execute.setEnabled(true);
                    queryComponent.clearQuery.setEnabled(true);
                }
            }

            public void keyReleased(KeyEvent e) {
                return;
            }
        });
    }

    private void initButtonsPanel() {
//        Font dialogFont;

/*
        if (queryComponent.eSlateHandle.getLocale().getCountry().equals("GR")) {
            dialogFont = new Font("Helvetica", Font.BOLD, 12);
        } else {
            dialogFont = new Font("Dialog", Font.PLAIN, 12);
        }
*/

        //Color color128 = new Color(104,43,10);
        equal = new JButton("=");
        notEqual = new JButton("!=");
        less = new JButton("<");
        lessEqual = new JButton("<=");
        greater = new JButton(">");
        greaterEqual = new JButton(">=");
        add = new JButton(infoBundle.getString("AND"));
        or = new JButton(infoBundle.getString("OR"));
        not = new JButton(infoBundle.getString("NOT"));
//System.out.println("Contains? " + infoBundle.getString("Contains"));
        contains = new JButton(infoBundle.getString("Contains"));
        contained = new JButton(infoBundle.getString("Contained"));
        brackets = new JButton("[ ]");
        quotes = new JButton("\" \"");
        parenths = new JButton("( )");

        firstButtonPanel = new JPanel(true);
        secondButtonPanel = new JPanel(true);
        thirdButtonPanel = new JPanel(true);

//        setButtonsFont(dialogFont);
        setButtonsSize();
        addToolTipsToButtons();
        assignListenersToButtons();
        setButtonPanelAppearance();

    }

    private void addToolTipsToButtons() {
        equal.setToolTipText(infoBundle.getString("Equaltip"));
        notEqual.setToolTipText(infoBundle.getString("NotEqualtip"));
        less.setToolTipText(infoBundle.getString("Lesstip"));
        lessEqual.setToolTipText(infoBundle.getString("LessEqualtip"));
        greater.setToolTipText(infoBundle.getString("Greatertip"));
        greaterEqual.setToolTipText(infoBundle.getString("GreaterEqualtip"));
        add.setToolTipText(infoBundle.getString("ANDtip"));
        or.setToolTipText(infoBundle.getString("ORtip"));
        not.setToolTipText(infoBundle.getString("NOTtip"));
        contains.setToolTipText(infoBundle.getString("Containstip"));
        contained.setToolTipText(infoBundle.getString("Containedtip"));
        brackets.setToolTipText(infoBundle.getString("Bracketstip"));
        quotes.setToolTipText(infoBundle.getString("Quotestip"));
        parenths.setToolTipText(infoBundle.getString("Parenthstip"));
   }

    private void initFieldList() {
//        fieldList.setFont(dialogFont);
        fieldList.setBackground(Color.white);
        fieldList.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fieldList.setReorderingEnabled(false);
        fieldList.setKeystrokeJumptoEnabled(true);
        fieldList.setToolTipText(queryComponent.infoBundle.getString("fieldListTip"));

        fieldList.addMouseListener(new MouseAdapter() {
            long pressTime = System.currentTimeMillis();
            public void mousePressed(MouseEvent e) {

                long pressTimeBck = pressTime;
                pressTime = System.currentTimeMillis();
                if (pressTime - pressTimeBck < 300) {
//                  System.out.println("In mouse pressed");
                    int index = fieldList.locationToIndex(e.getPoint());
//                  System.out.println("index = "+index);
                    String selectedText = queryArea.getSelectedText();
                    String txt = queryArea.getText();
//                  System.out.println("selected Text :"+selectedText);
//                  System.out.println("the txt :"+txt);
                    if (selectedText == null) {
                        if (txt == null || txt.length() == 0 || txt.charAt(txt.length()-1) == ' ') {
                             // Change when they move to the toolbar
                            queryComponent.execute.setEnabled(true);
                            queryComponent.clearQuery.setEnabled(true);
                             // Lookk
                            //queryArea.insert(queryComponent.fieldListModel.elementAt(index).toString(), queryArea.getCaretPosition());
                            queryArea.insert(listModel.elementAt(index).toString(), queryArea.getCaretPosition());
                        }else
                            //queryArea.insert(' ' + queryComponent.fieldListModel.elementAt(index).toString(), queryComponent.queryArea.getCaretPosition());
                            queryArea.insert(' ' + listModel.elementAt(index).toString(), queryArea.getCaretPosition());
                    } else {
                        int selectionStart = queryArea.getSelectionStart();
                        int selectionEnd = queryArea.getSelectionEnd();

                        if (selectionStart == 0 || txt.charAt(selectionStart-1) == ' ') {
                           // queryArea.replaceRange(queryComponent.fieldListModel.elementAt(index).toString(), selectionStart, selectionEnd);
                            queryArea.replaceRange(listModel.elementAt(index).toString(), selectionStart, selectionEnd);
                        } else {
                           // queryArea.replaceRange(' ' + queryComponent.fieldListModel.elementAt(index).toString(), selectionStart, selectionEnd);
                            queryArea.replaceRange(' ' + listModel.elementAt(index).toString(), selectionStart, selectionEnd);
                        }

                       // queryArea.setCaretPosition(selectionStart + queryComponent.fieldListModel.elementAt(index).toString().length());
                        queryArea.setCaretPosition(selectionStart + listModel.elementAt(index).toString().length());
                        brackets.setEnabled(false);
                        quotes.setEnabled(false);
                        parenths.setEnabled(false);
                    }
                  }
            }
        });

       fieldList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                if (fieldList==null) {
                    return;
                }
                queryComponent.proceed = false;

                int k;
               // System.out.println("listModel Capacity :\t"+listModel.getSize());
               // System.out.println("e.getLastIndex():\t"+e.getLastIndex());
                String fieldName;
                if (listModel.getSize() > 0) {
                    if (fieldList.getSelectedIndex()<0) {
                        if (selectionIndex != -1 && fromUpdate) {
                            fieldName = (String) listModel.get(selectionIndex);
                            fromUpdate = false;
                        } else {
                            fieldName = (String) listModel.get(0);
                        }

                    } else {
                        fieldName = (String) listModel.get(fieldList.getSelectedIndex());
                    }
                    Object[] values;
                    try{
                        Class fieldType;
                        try {
                            fieldType = queryComponent.getDB().getActiveTable().getTableField(fieldName).getDataType();
                        } catch (NullPointerException p) {
                            return;
                        }
                      //  System.out.println("fieldType : "+fieldType);
                        if (fieldType.equals(java.lang.Boolean.class)) {
                            values = new Object[2];
                            values[0] = queryComponent.infoBundle.getString("true");
                            values[1] = queryComponent.infoBundle.getString("false");
                        } else {
                            try {
                                values = queryComponent.db.getActiveTable().getUniqueFieldValues(fieldName, false);
                            } catch (InvalidFieldNameException ex) {
                                return;
                            }
                        }
                    } catch (InvalidFieldNameException exc) {

                        try {
System.out.println("FieldValues: " + queryComponent.db.getActiveTable().getUniqueFieldValues(fieldName, false).getClass());
                            values = queryComponent.db.getActiveTable().getUniqueFieldValues(fieldName, false);
                        } catch (InvalidFieldNameException ex) {
                            return;
                        }
                    }
                     fieldValuesList.setCellRenderer(new MyCellRenderer(queryComponent.db.getActiveTable()));
                     fieldValuesList.setListData(values);
                }

                if (fieldValuesList.getModel().getSize() > 0)
                    fieldValuesList.ensureIndexIsVisible(0);

                if (fieldValuesScrollPane==null) {
                    return;
                } else {
                    fieldValuesScrollPane.getViewport().setView(fieldValuesList);
                    fieldValuesScrollPane.getViewport().setView(fieldValuesList);
                }
            }
        });
    }

    private void initFieldValuesList() {
        fieldValuesList.setFont(new Font("Helvetica", Font.PLAIN, 12));
        fieldValuesList.setBackground(Color.white);
        fieldValuesList.getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        fieldValuesList.setCellRenderer(new MyCellRenderer(queryComponent.db.getActiveTable()));
        fieldValuesList.setKeystrokeJumptoEnabled(true);
//        fieldValuesList.setReorderingEnabled(false);

       // fieldValuesList.addListSelectionListener(new ListSelectionListener() {
        //    public void valueChanged(ListSelectionEvent e) {
         //   }
        //});

        fieldValuesList.addMouseListener(new MouseAdapter() {
            long pressTime = System.currentTimeMillis();
            public void mousePressed(MouseEvent e) {
                long pressTimeBck = pressTime;
                pressTime = System.currentTimeMillis();
                if (pressTime - pressTimeBck < 300) {
                    int index = fieldValuesList.locationToIndex(e.getPoint());

                    String selectedText = queryArea.getSelectedText();

                    String txt = queryArea.getText();
                    Object value = fieldValuesList.getModel().getElementAt(index);
                    String valueStr;
                    if (value.getClass().equals(Double.class) || value.getClass().equals(Float.class))
                        valueStr = queryComponent.getDB().getActiveTable().getNumberFormat().format((Number) value);
                    else
                        valueStr = value.toString();

                    if (selectedText == null) {
                        if (txt == null || txt.length() == 0 || txt.charAt(txt.length()-1) == ' ') {

                            queryComponent.execute.setEnabled(true);
                            queryComponent.clearQuery.setEnabled(true);
                            queryArea.insert(valueStr, queryArea.getCaretPosition());
                        }else
                            queryArea.insert(' ' + valueStr, queryArea.getCaretPosition());

                    }else{

                        int selectionStart = queryArea.getSelectionStart();
                        int selectionEnd = queryArea.getSelectionEnd();

                        if (selectionStart == 0 || txt.charAt(selectionStart-1) == ' '){
                            queryArea.replaceRange(valueStr, selectionStart, selectionEnd);
                        } else {
                            queryArea.replaceRange(' ' + valueStr, selectionStart, selectionEnd);
                        }

                        queryArea.setCaretPosition(selectionStart + valueStr.length());
                        brackets.setEnabled(false);
                        quotes.setEnabled(false);
                        parenths.setEnabled(false);
                    }
                }
            }
        });

    }
      private void setButtonsFont(Font font) {
        equal.setFont(font);
        notEqual.setFont(font);
        less.setFont(font);
        lessEqual.setFont(font);
        greater.setFont(font);
        greaterEqual.setFont(font);
        add.setFont(font);
        or.setFont(font);
        not.setFont(font);
        contains.setFont(font);
        contained.setFont(font);
        brackets.setFont(font);
        quotes.setFont(font);
        parenths.setFont(font);
    }

    private void setButtonsSize() {
        //Dimension buttonDimension = new Dimension(30, 20);
        //Dimension buttonDimension2 = new Dimension(30, 23);
        Dimension buttonDimension = new Dimension(20, 20);
        Dimension buttonDimension2 = new Dimension(20, 23);
        Insets insets = new Insets(1, 1, 1, 1);

        equal.setMargin(insets);
        equal.setMinimumSize(buttonDimension);
        equal.setMaximumSize(buttonDimension);
        equal.setPreferredSize(buttonDimension);

        notEqual.setMargin(insets);
        notEqual.setMinimumSize(buttonDimension);
        notEqual.setMaximumSize(buttonDimension);
        notEqual.setPreferredSize(buttonDimension);

        parenths.setMargin(insets);
        parenths.setMinimumSize(buttonDimension);
        parenths.setMaximumSize(buttonDimension);
        parenths.setPreferredSize(buttonDimension);

        less.setMargin(insets);
        less.setMinimumSize(buttonDimension);
        less.setMaximumSize(buttonDimension);
        less.setPreferredSize(buttonDimension);

        lessEqual.setMargin(insets);
        lessEqual.setMinimumSize(buttonDimension);
        lessEqual.setMaximumSize(buttonDimension);
        lessEqual.setPreferredSize(buttonDimension);

        greater.setMargin(insets);
        greater.setMinimumSize(buttonDimension);
        greater.setMaximumSize(buttonDimension);
        greater.setPreferredSize(buttonDimension);

        greaterEqual.setMargin(insets);
        greaterEqual.setMinimumSize(buttonDimension);
        greaterEqual.setMaximumSize(buttonDimension);
        greaterEqual.setPreferredSize(buttonDimension);

        contains.setMinimumSize(buttonDimension2);
        contains.setMaximumSize(buttonDimension2);
        contains.setPreferredSize(buttonDimension2);
        contains.setMargin(insets);

        contained.setMinimumSize(buttonDimension2);
        contained.setMaximumSize(buttonDimension2);
        contained.setPreferredSize(buttonDimension2);
        contained.setMargin(insets);

        add.setMargin(insets);
        add.setMinimumSize(buttonDimension2);
        add.setMaximumSize(buttonDimension2);
        add.setPreferredSize(buttonDimension2);

        or.setMargin(insets);
        or.setMinimumSize(buttonDimension2);
        or.setMaximumSize(buttonDimension2);
        or.setPreferredSize(buttonDimension2);

        not.setMargin(insets);
        not.setMinimumSize(buttonDimension2);
        not.setMaximumSize(buttonDimension2);
        not.setPreferredSize(buttonDimension2);

        brackets.setMargin(insets);
        quotes.setMargin(insets);
    }

    private void assignListenersToButtons() {
        ActionListener firstActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String oper = ((JButton) e.getSource()).getText();
                if (oper.equals(infoBundle.getString("Contains")) || oper.equals(infoBundle.getString("Contained")))
                    oper = oper.toLowerCase();
                if (oper.equals(infoBundle.getString("OR")))
                    oper = infoBundle.getString("LowerOR");
                if (oper.equals(infoBundle.getString("AND")))
                    oper = infoBundle.getString("LowerAND");
                if (oper.equals(infoBundle.getString("NOT")))
                    oper = infoBundle.getString("LowerNOT");

                String selectedText = queryArea.getSelectedText();

                String txt = queryArea.getText();
                if (selectedText == null) {
                    if (txt == null || txt.length() == 0 || txt.charAt(txt.length()-1) == ' ') {
                        queryComponent.execute.setEnabled(true);
                        queryComponent.clearQuery.setEnabled(true);
                        queryArea.insert(oper, queryArea.getCaretPosition());
                    } else {
                        queryArea.insert(' ' + oper, queryArea.getCaretPosition());
                    }

                } else {
                    int selectionStart = queryArea.getSelectionStart();
                    int selectionEnd = queryArea.getSelectionEnd();

                    if (selectionStart == 0 || txt.charAt(selectionStart-1) == ' ') {
                        queryArea.replaceRange(oper, selectionStart, selectionEnd);
                    } else {
                        queryArea.replaceRange(' ' + oper, selectionStart, selectionEnd);
                    }

                    queryArea.setCaretPosition(selectionStart + oper.length());
                    brackets.setEnabled(false);
                    quotes.setEnabled(false);
                    parenths.setEnabled(false);
                }
                queryArea.requestFocus();
            }
        };

        equal.addActionListener(firstActionListener);
        notEqual.addActionListener(firstActionListener);
        greater.addActionListener(firstActionListener);
        greaterEqual.addActionListener(firstActionListener);
        less.addActionListener(firstActionListener);
        lessEqual.addActionListener(firstActionListener);
        contains.addActionListener(firstActionListener);
        contained.addActionListener(firstActionListener);
        add.addActionListener(firstActionListener);
        or.addActionListener(firstActionListener);
        not.addActionListener(firstActionListener);

        ActionListener secondActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String oper = ((JButton) e.getSource()).getText().toLowerCase();
                String selectedText = queryArea.getSelectedText();

                String txt = queryArea.getText();
                if (selectedText != null) {
                    int selectionStart = queryArea.getSelectionStart();
                    int selectionEnd = queryArea.getSelectionEnd();
                    queryArea.insert(oper.substring(0,1), selectionStart);
                    queryArea.insert(oper.substring(2), selectionEnd+1);
                    queryArea.setSelectionStart(selectionStart+1);
                    queryArea.setSelectionEnd(selectionEnd+1);
                }
            }
        };

        parenths.addActionListener(secondActionListener);
        brackets.addActionListener(secondActionListener);
        quotes.addActionListener(secondActionListener);
    }

    private void setFirstButtonsPanelAppearance() {

        firstButtonPanel.setLayout(gridbag);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.insets = new Insets(1, 1, 1, 1);
        gridbag.setConstraints(equal, c);
        firstButtonPanel.add(equal);
        gridbag.setConstraints(notEqual, c);
        firstButtonPanel.add(notEqual);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(parenths, c);
        firstButtonPanel.add(parenths);

        c.gridwidth = 1;
        gridbag.setConstraints(less, c);
        firstButtonPanel.add(less);
        gridbag.setConstraints(lessEqual, c);
        firstButtonPanel.add(lessEqual);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(brackets, c);
        firstButtonPanel.add(brackets);

        c.gridwidth = 1;
        gridbag.setConstraints(greater, c);
        firstButtonPanel.add(greater);
        gridbag.setConstraints(greaterEqual, c);
        firstButtonPanel.add(greaterEqual);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(quotes, c);
        firstButtonPanel.add(quotes);


    }
    private void setSecondButtonsPanelAppearance() {

        GridBagLayout gridbag3 = new GridBagLayout();
        secondButtonPanel.setLayout(gridbag3);
        GridBagConstraints c3 = new GridBagConstraints();
        c3.fill = GridBagConstraints.BOTH;
        c3.weightx = 1.0;
        c3.gridwidth = 1;
        c3.insets = new Insets(1,1,1,1);
        gridbag3.setConstraints(contains, c3);
        secondButtonPanel.add(contains);
        c3.gridwidth = GridBagConstraints.REMAINDER;
        c3.insets = new Insets(1,1,1,1);
        gridbag3.setConstraints(contained, c3);
        secondButtonPanel.add(contained);
    }

    private void setThirdButtonsPanelAppearance() {

        GridBagLayout gridbag2 = new GridBagLayout();
        thirdButtonPanel.setLayout(gridbag2);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.BOTH;
        c2.weightx = 1.0;
        c2.gridwidth = 1;
        c2.insets = new Insets(1,1,1,1);
        gridbag2.setConstraints(add, c2);
        thirdButtonPanel.add(add);
        gridbag2.setConstraints(or, c2);
        thirdButtonPanel.add(or);
        c2.gridwidth = GridBagConstraints.REMAINDER;
        gridbag2.setConstraints(not, c2);
        thirdButtonPanel.add(not);

    }

    private void setButtonsPanelDimension() {
        Dimension d = new Dimension(190, 72);
        firstButtonPanel.setMaximumSize(d);
        firstButtonPanel.setMinimumSize(d);
        firstButtonPanel.setPreferredSize(d);

        Dimension d2 = new Dimension(190, 25);
        secondButtonPanel.setMaximumSize(d2);
        secondButtonPanel.setMinimumSize(d2);
        secondButtonPanel.setPreferredSize(d2);

        thirdButtonPanel.setMaximumSize(d2);
        thirdButtonPanel.setMinimumSize(d2);
        thirdButtonPanel.setPreferredSize(d2);
    }


    private void setButtonPanelAppearance() {
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        setFirstButtonsPanelAppearance();
        setSecondButtonsPanelAppearance();
        setThirdButtonsPanelAppearance();
        setButtonsPanelDimension();
        buttonsPanel.add(firstButtonPanel);
        buttonsPanel.add(secondButtonPanel);
        buttonsPanel.add(thirdButtonPanel);
        buttonsPanel.add(Box.createGlue());
    }

    //Exported Utilities
    public String getQueryAreaText() {
        return queryArea.getText();
    }
    public void setQueryAreaText(String newText) {
        queryArea.setText(newText);
    }
    public int getFieldListSelectionIndex() {
        return fieldList.getSelectedIndex();
    }

    public void clearQueryArea() {
        queryArea.setText("");
    }
    // Imported
    public void loadNewInfo(QueryTextViewInfo newInfo) {
        listModel=newInfo.myListModel;
        newInfo.fieldListSelectedIndex = updateFieldList(listModel, newInfo.fieldListSelectedIndex);
        updateQueryArea(newInfo.queryAreaText);
        validate();
        doLayout();
        repaint();
        requestFocus();
        fieldList.requestFocus();
    }

    private int updateFieldList(DefaultListModel myListModel, int newIndex) {
        fromUpdate=true;
        selectionIndex=newIndex;
        fieldList.setListData(myListModel.toArray());
//System.out.println("updateFieldList() myListModel size: " + myListModel.getSize() + ", newIndex: " + newIndex);
        fieldList.setSelectedIndex(newIndex);
        setFieldListValues(newIndex);
        return fieldList.getSelectedIndex();
   }

    private void updateQueryArea(String newText) {
        clearQueryArea();
        queryArea.insert(newText,queryArea.getCaretPosition());
    }
    private void setFieldListValues(int index) {
        if (listModel.getSize() == 0) return;
        if (index == -1) return;
        try {
             Class fieldType = queryComponent.db.getActiveTable().getTableField((String) listModel.get(index)).getDataType();
                   if (fieldType.equals(BooleanTableField.DATA_TYPE)) {
                        Object[] val = new Object[2];
                        val[0] = infoBundle.getString("true");
                        val[1] = infoBundle.getString("false");
                        fieldValuesList.setListData(val);
                    } else {
                        try{
                             fieldValuesList.setListData(queryComponent.db.getActiveTable().getUniqueFieldValues((String) listModel.get(index), false));
                        } catch (InvalidFieldNameException ex) {
                        }
                    }
        } catch (InvalidFieldNameException exc) {
        }
    }
    public void destroy() {
        fieldList=null;
        fieldValuesList=null;
        queryArea=null;
    }

}

