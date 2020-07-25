package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.ESlateOptionPane;
import gr.cti.eslate.utils.NoBorderButton;
import gr.cti.eslate.utils.StorageStructure;
import gr.cti.typeArray.StringBaseArray;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import jeditSyntax.JEditTextArea;
import jeditSyntax.JavaScriptTokenMarker;
import jeditSyntax.JavaTokenMarker;
import jeditSyntax.LogoTokenMarker;
import jeditSyntax.SyntaxDocument;
import jeditSyntax.TextAreaPainter;

class ScriptDialog extends JFrame {
    Locale locale;
    ResourceBundle scriptDialogBundle;
    protected Font greekUIBoldFont = new Font("Helvetica", Font.BOLD, 12);
//    private boolean localeIsGreek = false;
    private final static int COMPONENT_AREA_WIDTH = 150;
    JTextField scriptNameTf;
    JLabel scriptNameLb, varLb1; //, varLb;
    NoBorderButton variableButton;
    JPopupMenu variableMenu;
    JEditTextArea scriptArea;
    private int currentScriptLanguage = ScriptListener.LOGO;
    /* In the case of Java scripts, they have to be writen to a file, so that jikes can
     * compile them. The name of the file has to be the same as the name of the listener
     * class. This variable holds this name and is only valid for Java scripts.
     */
    String listenerClassName = null;
    JSplitPane editorSplitPane = null, hierarchySplitPane = null;
    JTree scriptTree = null;
    JList listenerList = null;
    ScriptListenerListModel listenerListModel = null;
    /* The selected item of the listener list */
//    ScriptListenerNode selectedNode = null;
	ScriptDialogNodeInterface selectedNode = null;
    JLabel rowColumnLabel, lineCountLabel, statusLabel;
    NoBorderButton copy, cut, paste,/* undo, redo,*/ compile, find, findNext, replace; //findPrev
    NoBorderButton newButt, clear, load, save, goToLine, run;
    ImageIcon cutDisabledIcon = new ImageIcon(getClass().getResource("images/cutDisabled.gif"));
    ImageIcon copyDisabledIcon = new ImageIcon(getClass().getResource("images/copyDisabled.gif"));
    ImageIcon pasteDisabledIcon = new ImageIcon(getClass().getResource("images/pasteDisabled.gif"));
	ImageIcon newScriptDisabledIcon = new ImageIcon(getClass().getResource("images/cutDisabled.gif"));
    JComboBox fontType, fontSize;
//    NoBorderButton colors;
    String lineCountText;
    boolean scriptChanged = false;
    /* Marks the current script as a new one, i.e. a script which hasn't yet been attached
     * to the target component.
     */
    boolean newScript = false;
    boolean editorEnabled = true;
    private boolean disableSavePrompt = false;
    private boolean updatingTree = false;
//    String script;
//    String scriptName = null;
//    String noScriptName;
//    ArrayList getterMethods;
    ESlateComposer container;
//    private String initialComponentName = null;
    ESlateFileDialog fileDialog;
    String token = null; // The last token searched in the console
    String line = null; // The line to go to
    ActionListener variableMenuItemListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (editorEnabled) {
                if (scriptArea.getSelectedText() != null) {
                	scriptArea.select(scriptArea.getSelectionStart(), scriptArea.getSelectionEnd());
                    scriptArea.setSelectedText(((JMenuItem) e.getSource()).getText());
                } else{
                    SyntaxDocument document = scriptArea.getDocument();
                    try {
                      document.beginCompoundEdit();
                      document.insertString(scriptArea.getCaretPosition(), ((JMenuItem) e.getSource()).getText(), null);
                    } catch (BadLocationException ex) {
						ex.printStackTrace();
					} finally {
                      document.endCompoundEdit();
                    }
                	
//                	try{
//                        scriptArea.insert(((JMenuItem) e.getSource()).getText(), scriptArea.getCaretPosition());
//                    }catch (BadLocationException exc) {}
                }
            }
        }
    };
    /* The listener that updates the modified state of the document */
    DocumentListener documentListener = null;
    /* The listener that updates the state of the copy/cut/run buttons */
    CaretListener caretListener = null;
    // Whenever this flag is true the listeners of the font compoboxes wil do nothing.
    boolean disableFontListeners = false;
    private ListenerSecurityProxy proxy = null;
    /** The root of the ScriptListenerTree. Immediate child of 'rootNode'.
     */
    ScriptListenerHandleNode rootScriptListenerNode = null;
    /** The root of the whole tree */
    DefaultMutableTreeNode rootNode = null;
    /** The root of the ScriptTree. */
    GlobalScriptTopNode rootScriptNode = null;
	/** The Action for the 'newButt' button. */
	NewScriptAction newScriptAction = new NewScriptAction(this, "NewScriptAction");
	/** The Action for the 'compile' button. */
	CompileAction compileAction = new CompileAction(this, "CompileAction");
	public static final int LOGO=0;
	public static final int JAVASCRIPT=1;
	public static final int JAVA=2;

    public ScriptDialog(ESlateComposer cont) {
        super();
        if (cont.microworld != null)
            cont.microworld.checkActionPriviledge(cont.microworld.componentEventMgmtAllowed, "componentEventMgmtAllowed");

        setIconImage(cont.getAppIcon());
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        container = cont;

        locale = Locale.getDefault();
        scriptDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ScriptDialogBundle", locale);
//        if (scriptDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.ScriptDialogBundle_el_GR"))
//            localeIsGreek = true;

//        noScriptName = scriptDialogBundle.getString("NoScript");

//        setTitle(scriptDialogBundle.getString("DialogTitle1") + method.getName() + "\"" + scriptDialogBundle.getString("DialogTitle2") + eSlateHandle.getComponentName() + "\"");
        setTitle(scriptDialogBundle.getString("DialogTitle"));
        lineCountText = scriptDialogBundle.getString("LineCount");

        // The toolbar
        // Create the menu bar
        Dimension buttonSize = new Dimension(22, 21);
        Insets zeroInsets = new Insets(0, 0, 0, 0);

		newButt = new NoBorderButton();
		newButt.setAction(newScriptAction);
		newButt.setText("");
		newButt.setMargin(zeroInsets);
		newButt.setDisabledIcon(newScriptDisabledIcon);
		newButt.setToolTipText(scriptDialogBundle.getString("NewScriptTip"));
        newButt.setRequestFocusEnabled(false);

        load = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/openFile20x19.gif")));
        load.setMargin(zeroInsets);
        load.setToolTipText(scriptDialogBundle.getString("LoadTip"));
        load.setRequestFocusEnabled(false);

        save = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/save.gif")));
        save.setMargin(zeroInsets);
        save.setToolTipText(scriptDialogBundle.getString("SaveTip"));
        save.setRequestFocusEnabled(false);

        cut = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/cut.gif")));
      	cut.setDisabledIcon(cutDisabledIcon);
        cut.setMargin(zeroInsets);
        cut.setToolTipText(scriptDialogBundle.getString("CutTip"));
        cut.setRequestFocusEnabled(false);

        copy = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/copy.gif")));
      	copy.setDisabledIcon(copyDisabledIcon);
        copy.setMargin(zeroInsets);
        copy.setToolTipText(scriptDialogBundle.getString("CopyTip"));
        copy.setRequestFocusEnabled(false);

        paste = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/paste.gif")));
      	paste.setDisabledIcon(pasteDisabledIcon);
        paste.setMargin(zeroInsets);
        paste.setToolTipText(scriptDialogBundle.getString("PasteTip"));
        paste.setRequestFocusEnabled(false);

//        undo = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/undo.gif")));
////      	undo.setDisabledIcon(pasteDisabledIcon);
//        undo.setMargin(zeroInsets);
//        undo.setToolTipText(scriptDialogBundle.getString("UndoTip"));
//        undo.setRequestFocusEnabled(false);
//
//        redo = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/redo.gif")));
////      	redo.setDisabledIcon(pasteDisabledIcon);
//        redo.setMargin(zeroInsets);
//        redo.setToolTipText(scriptDialogBundle.getString("RedoTip"));
//        redo.setRequestFocusEnabled(false);

        find = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/find.gif")));
        find.setMargin(zeroInsets);
        find.setToolTipText(scriptDialogBundle.getString("FindTip"));
        find.setRequestFocusEnabled(false);

        findNext = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/findNext3.gif")));
        findNext.setMargin(zeroInsets);
        findNext.setToolTipText(scriptDialogBundle.getString("FindNextTip"));
        findNext.setRequestFocusEnabled(false);

/*        findPrev = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/findPrev2.gif")));
        findPrev.setMargin(zeroInsets);
        findPrev.setToolTipText(scriptDialogBundle.getString("FindPrevTip"));
        findPrev.setRequestFocusEnabled(false);
*/
        replace = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/replace2.gif")));
        replace.setMargin(zeroInsets);
        replace.setToolTipText(scriptDialogBundle.getString("ReplaceTip"));
        replace.setRequestFocusEnabled(false);

        goToLine = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/goToLine.gif")));
        goToLine.setMargin(zeroInsets);
        goToLine.setToolTipText(scriptDialogBundle.getString("GoToLineTip"));
        goToLine.setRequestFocusEnabled(false);

        run = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/run.gif")));
        run.setMargin(zeroInsets);
        run.setToolTipText(scriptDialogBundle.getString("RunTip"));
        run.setRequestFocusEnabled(false);

        compile = new NoBorderButton();
		compile.setAction(compileAction);
		compile.setText("");
        compile.setMargin(zeroInsets);
        compile.setToolTipText(scriptDialogBundle.getString("CompileTip"));
        compile.setRequestFocusEnabled(false);

        clear = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/clear.gif")));
        clear.setMargin(zeroInsets);
        clear.setToolTipText(scriptDialogBundle.getString("ClearTip"));
        clear.setRequestFocusEnabled(false);

        cut.setEnabled(false);
        copy.setEnabled(false);
//	      if (getToolkit().getSystemClipboard().getContents(this) == null)
//            paste.setEnabled(false);
//        else
        paste.setEnabled(true);
//        undo.setEnabled(false);
//        redo.setEnabled(false);
        run.setEnabled(false);

        javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(Box.createHorizontalStrut(2));
		toolBar.add(newButt);
        toolBar.add(load);
        toolBar.add(save);
        toolBar.addSeparator();
        toolBar.add(cut);
        toolBar.add(copy);
        toolBar.add(paste);
//        toolBar.addSeparator();
//        toolBar.add(undo);
//        toolBar.add(redo);
        toolBar.addSeparator();
        toolBar.add(find);
//        toolBar.add(findPrev);
        toolBar.add(findNext);
        toolBar.add(replace);
        toolBar.add(goToLine);
        toolBar.addSeparator();
        toolBar.add(run);
        toolBar.add(compile);
        toolBar.add(clear);
        toolBar.add(Box.createGlue());

        String[] fontNames =  GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontType = new JComboBox(fontNames);
        int w = 130;
        int h = compile.getPreferredSize().height;
        FontMetrics fm2 = fontType.getFontMetrics(fontType.getFont());
        int fontNamesLength = fontNames.length;
        if (fontNamesLength > 0) {
            w = fm2.stringWidth(fontNames[0]);
            for (int i=1; i<fontNamesLength; i++) {
                int w2 = fm2.stringWidth(fontNames[i]);
                if (w2 > w) w = w2;
            }
            w = w + 25;
        }
        Dimension d = new Dimension(w, fontType.getPreferredSize().height);
        fontType.setMinimumSize(d);
        fontType.setMaximumSize(d);
        fontType.setPreferredSize(d);
        fontType.setRequestFocusEnabled(false);

		String[] fontSizes = new String[] {"7", "8", "9", "10", "12", "14", "16", "18", "20", "22", "24", "28", "36", "48", "72"};
        fontSize = new JComboBox(fontSizes);
/*        fontSize.addItem("7");
        fontSize.addItem("8");
        fontSize.addItem("9");
        fontSize.addItem("10");
        fontSize.addItem("12");
        fontSize.addItem("14");
        fontSize.addItem("16");
        fontSize.addItem("18");
        fontSize.addItem("20");
        fontSize.addItem("22");
        fontSize.addItem("24");
        fontSize.addItem("28");
        fontSize.addItem("36");
        fontSize.addItem("48");
        fontSize.addItem("72");
*/
        Dimension d1 = new Dimension(42, fontSize.getPreferredSize().height);
        fontSize.setMinimumSize(d1);
        fontSize.setMaximumSize(d1);
        fontSize.setPreferredSize(d1);
        fontSize.setEditable(true);

//        colors = new NoBorderButton(new ImageIcon(ScriptDialog.class.getResource("images/editorColors.gif")));
//        colors.setMargin(zeroInsets);
//        colors.setToolTipText(scriptDialogBundle.getString("ColorsTip"));
//        colors.setRequestFocusEnabled(false);

        javax.swing.JToolBar formatToolBar = new javax.swing.JToolBar();
        formatToolBar.setFloatable(false);
        formatToolBar.add(fontType);
        formatToolBar.add(fontSize);
//        formatToolBar.addSeparator();
//        formatToolBar.add(colors);
        formatToolBar.add(Box.createGlue());

        JPanel barPanel = new JPanel(true);
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.Y_AXIS));
        barPanel.add(Box.createVerticalStrut(1));
        barPanel.add(toolBar);
        barPanel.add(formatToolBar);
        barPanel.add(Box.createVerticalStrut(1));

        scriptNameLb = new JLabel(scriptDialogBundle.getString("ScriptName"));
        scriptNameTf = new JTextField();

        varLb1 = new JLabel(scriptDialogBundle.getString("AvailableVariables")); // + ":  ");
//        if (localeIsGreek)
//            varLb1.setFont(greekUIBoldFont);
//        else
            varLb1.setFont(new Font(varLb1.getFont().getFamily(), Font.BOLD, varLb1.getFont().getSize()));

        ImageIcon rightArrowIcon = new ImageIcon(getClass().getResource("images/rightArrow.gif"));
        variableButton = new NoBorderButton(rightArrowIcon);
        Dimension buttonDim = new Dimension(rightArrowIcon.getIconWidth()+10, rightArrowIcon.getIconHeight()+8);
        variableButton.setMaximumSize(buttonDim);
        variableButton.setMinimumSize(buttonDim);
        variableButton.setPreferredSize(buttonDim);

        variableMenu = new JPopupMenu();
//        varLb= new JLabel();

        scriptArea = new JEditTextArea();
//        if (localeIsGreek) {
//            scriptNameLb.setFont(greekUIBoldFont);
//            scriptNameTf.setFont(greekUIFont);
//            varLb.setFont(greekUIFont);
//            scriptArea.setFont(greekUIFont);
//        }else
            scriptNameLb.setFont(new Font(scriptNameLb.getFont().getFamily(), Font.BOLD, scriptNameLb.getFont().getSize()));

        JPanel scriptNamePanel = new JPanel(true);
        scriptNamePanel.setLayout(new BoxLayout(scriptNamePanel, BoxLayout.X_AXIS));
        scriptNamePanel.add(scriptNameLb);
        scriptNamePanel.add(Box.createHorizontalStrut(5));
        scriptNamePanel.add(scriptNameTf);
        scriptNamePanel.add(Box.createHorizontalStrut(13));
        scriptNamePanel.add(varLb1);
        scriptNamePanel.add(Box.createHorizontalStrut(2));
        scriptNamePanel.add(variableButton);
        scriptNamePanel.add(Box.createGlue());

/*        JPanel varPanel = new JPanel(true);
        varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.X_AXIS));
        varPanel.add(varLb1); //, BorderLayout.WEST);
        varPanel.add(varLb); //, BorderLayout.CENTER);
        varPanel.add(Box.createGlue());
*/
        JPanel intermediatePanel = new JPanel(true);
        intermediatePanel.setLayout(new BoxLayout(intermediatePanel, BoxLayout.Y_AXIS));
        intermediatePanel.add(scriptNamePanel);
//        intermediatePanel.add(Box.createVerticalStrut(3));
//        intermediatePanel.add(varPanel);
        intermediatePanel.setBorder(new javax.swing.border.EmptyBorder(2,2,2,0));

        JPanel topPanel = new JPanel(true);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(barPanel);
        topPanel.add(Box.createVerticalStrut(1));
        topPanel.add(intermediatePanel);
        topPanel.add(Box.createVerticalStrut(2));

        rowColumnLabel = new JLabel();
        rowColumnLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        rowColumnLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Dimension lbSize = new Dimension(60, 20);
        rowColumnLabel.setMaximumSize(lbSize);
        rowColumnLabel.setMinimumSize(lbSize);
        rowColumnLabel.setPreferredSize(lbSize);

        lineCountLabel = new JLabel();
//        if (localeIsGreek)
//            lineCountLabel.setFont(greekUIFont);
        lineCountLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        lineCountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FontMetrics fm = getToolkit().getFontMetrics(lineCountLabel.getFont());
        int lineCountLabelWidth = fm.stringWidth(lineCountText);
        lbSize = new Dimension(lineCountLabelWidth+35, 20);
        lineCountLabel.setMaximumSize(lbSize);
        lineCountLabel.setMinimumSize(lbSize);
        lineCountLabel.setPreferredSize(lbSize);

        JPanel infoPanel = new JPanel(true);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        Dimension rigidArea = new Dimension(3, 20);
        infoPanel.add(lineCountLabel);
        infoPanel.add(Box.createRigidArea(rigidArea));
        infoPanel.add(rowColumnLabel);
        infoPanel.add(Box.createRigidArea(rigidArea));
//        infoPanel.add(Box.createGlue());

        JPanel editorPanel = new JPanel(true);
        editorPanel.setLayout(new BorderLayout());

        statusLabel = new JLabel() {
            public Dimension getPreferredSize() {
                Dimension prefSize = super.getPreferredSize();
                prefSize.height = 20;
                return prefSize;
            }
        };
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        editorPanel.add(topPanel, BorderLayout.NORTH);
        editorPanel.add(scriptArea, BorderLayout.CENTER); //scrollPane, BorderLayout.CENTER);
//        editorPanel.add(bottomPanel, BorderLayout.SOUTH);
        editorPanel.setBorder(new EmptyBorder(0, 1, 0, 1));

        rootNode = new DefaultMutableTreeNode("Scripts");
        scriptTree = new JTree(new DefaultTreeModel(rootNode)); //createTree(); //new JTree(container.componentScriptListeners.getScriptListenerTree());
        scriptTree.setRootVisible(false);
        scriptTree.setShowsRootHandles(true);
		scriptTree.setExpandsSelectedPaths(true);
        scriptTree.setCellRenderer(new ScriptTreeRenderer());
        scriptTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane treeScrollPane = new JScrollPane(scriptTree);
        treeScrollPane.setBorder(null);

        listenerList = new JList();
        listenerListModel = new ScriptListenerListModel();
        listenerList.setModel(listenerListModel);
        listenerList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listenerList.setCellRenderer(new ListenerListRenderer());
        JScrollPane listScrollPane = new JScrollPane(listenerList);
        listScrollPane.setBorder(null);

        hierarchySplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                         true,
                                         treeScrollPane,
                                         listScrollPane);
//        hierarchySplitPane.setBorder(null);
        editorSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                         true,
                                         hierarchySplitPane,
                                         editorPanel);
//        editorSplitPane.setBorder(null);
        getContentPane().add(editorSplitPane);

        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.cut();
            }
        });
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.copy();
            }
        });
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.paste();
            }
        });

//        undo.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scriptArea.undo();
//            }
//        });
//
//        redo.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scriptArea.redo();
//            }
//        });
//
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveScriptToFile();
            }
        });

        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadScriptFromFile();
            }
        });

        find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.find();
/*                String tok = (String) ESlateOptionPane.showInputDialog(ScriptDialog.this,
                                 scriptDialogBundle.getString("FindWhat"),
                                 scriptDialogBundle.getString("Find"),
                                 JOptionPane.QUESTION_MESSAGE,
                                 null,
                                 null,
                                 token);

                if (tok != null) {
                    token = tok;
                    find(token, 0, true);
                }
*/
            }
        });

        findNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.findNext();
//                if (token == null)
//                    return;
//ce                find(token, scriptArea.getCaretPosition(), true);
            }
        });

/*        findPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (token == null)
                    return;
*/
/*ce                int selectionStart = scriptArea.getSelectionStart();
                int selectionEnd = scriptArea.getSelectionEnd();
                if (selectionStart != selectionEnd) {// There is smth selected
                    String selectedText = scriptArea.getSelectedText();
                    if (selectedText.equals(token))
                        scriptArea.setSelectionEnd(selectionStart);
                }
                find(token, scriptArea.getCaretPosition(), false);
*/
/*            }
        });
*/
        replace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.replace();
            }
        });

        goToLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scriptArea.gotoLine();
/*ce                String l = (String) ESlateOptionPane.showInputDialog(ScriptDialog.this,
                            scriptDialogBundle.getString("GoToLine"),
                            scriptDialogBundle.getString("GoTo"),
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            line);

                if(l != null) {
                    line = l;
                    goToLine(line);
                }
*/
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedNode != null) {
					boolean isScriptListenerNode = isNodeScriptListenerNode(selectedNode);
					String scriptName = selectedNode.getName();
					if (!isScriptListenerNode) {
						scriptName = ((ScriptNode) selectedNode).script.getFullClassName();
					}

					if ((ESlateOptionPane.showConfirmDialog(ScriptDialog.this,
						scriptDialogBundle.getString("RemoveScript?") + '"' + scriptName + '"' + scriptDialogBundle.getString("?"),
						scriptDialogBundle.getString("ConfirmScriptRemoval"),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) == JOptionPane.YES_OPTION) {
						if (isScriptListenerNode) {
							removeComponentScriptListener((ScriptListenerNode) selectedNode);
						}else{
							removeClass((ScriptNode) selectedNode);
						}
					}
                }
            }
        });

        run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedNode == null) return;
                String selectedScript = scriptArea.getSelectedText();
                if (selectedScript == null || selectedScript.trim().length() == 0)
                    return;
                if (currentScriptLanguage == ScriptListener.LOGO)
                    container.evaluateLogoScript(selectedScript);
                else if (currentScriptLanguage == ScriptListener.JAVASCRIPT)
                    container.evaluateJSScript(selectedScript);
            }
        });

        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fontName = (String) ((JComboBox) e.getSource()).getSelectedItem();
//System.out.println("fontType actionLister: " + fontName + ", disableFontListeners: " + disableFontListeners);
                if (disableFontListeners) return;
//Thread.currentThread().dumpStack();
                scriptArea.setFont(new Font(fontName,scriptArea.getFont().getStyle(),scriptArea.getFont().getSize()));
            }
        });

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (disableFontListeners) return;
                int fontSize = new Integer((String) ((JComboBox) e.getSource()).getSelectedItem()).intValue();
                scriptArea.setFont(scriptArea.getFont().deriveFont((float) fontSize));
            }
        });

//        colors.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                scriptArea.config();
//            }
//        });

        //Initialize the dialog
        String fontName = scriptArea.getFont().getName();
        int fntSize = scriptArea.getFont().getSize();
        disableFontListeners = true;
        fontType.setSelectedItem(fontName);
        fontSize.setSelectedItem(new Integer(fntSize).toString());
        disableFontListeners = false;
//        scriptNameTf.setText(scriptName);
//        scriptArea.setText(script);

        variableButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                variableMenu.show(variableButton, variableButton.getWidth(), 0);
            }
        });

//        scriptArea.addUndoredoStatusListener(new UndoRedoStatusListener() {
//            public void undoRedoStatusChanged(UndoRedoStatusEvent evt) {
//                undo.setEnabled(evt.canUndo());
//                redo.setEnabled(evt.canRedo());
//            }
//        });

		ActionMap rootPaneActionMap = getRootPane().getActionMap();
		InputMap rootPaneInputMap = getRootPane().getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT); //WHEN_IN_FOCUSED_WINDOW);

		// Ctrl+N to create a new script
		String actionName = (String) newScriptAction.getValue(AbstractAction.NAME);
		rootPaneActionMap.put(actionName, newScriptAction);
		rootPaneInputMap.put((KeyStroke) newScriptAction.getValue(AbstractAction.ACCELERATOR_KEY),
							  actionName
		);

		ActionMap scriptAreaActionMap = scriptArea.getActionMap();
		InputMap scriptAreaInputMap = scriptArea.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        //F9 to attach the listener
		actionName = (String) compileAction.getValue(AbstractAction.NAME);
		scriptAreaActionMap.put(actionName, compileAction);
		scriptAreaInputMap.put((KeyStroke) compileAction.getValue(AbstractAction.ACCELERATOR_KEY),
							  actionName
		);
/*        scriptArea.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				if (isSelectedNodeScriptListenerNode()) {
					attachListener((ScriptListenerNode) selectedNode);
				}
            }
        }, KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0, false),
            javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
*/
        // CTRL+F9 to run the selected portion of the script
        scriptArea.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedNode == null) return;
                String selectedScript = scriptArea.getSelectedText();
                if (selectedScript == null || selectedScript.trim().length() == 0)
                    return;
                if (currentScriptLanguage == ScriptListener.LOGO)
                    container.evaluateLogoScript(selectedScript);
                else if (currentScriptLanguage == ScriptListener.JAVASCRIPT) {
//System.out.println("Evaluated selected portion of JS script");
                    container.evaluateJSScript(selectedScript);
                }
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, KeyEvent.CTRL_MASK, false),
            javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        scriptNameTf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (selectedNode != null) {
					selectedNode.setName(scriptNameTf.getText());
//                    selectedNode.listener.scriptName = scriptNameTf.getText();
                }
            }
        });

        scriptTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            boolean selectionCancelled = false;
            public void valueChanged(TreeSelectionEvent e) {
//                for (int i=0; i<e.getPath().getPathCount(); i++)
//                    System.out.print(((ScriptListenerTreeNode) e.getPath().getPathComponent(i)).getNodeName() + "-->");
//                System.out.println();
//                TreePath selectionPath = scriptTree.getSelectionPath();
//                System.out.print("Current selected path: ");
//                if (selectionPath != null) {
//                    for (int i=0; i<selectionPath.getPathCount(); i++)
//                        System.out.print(((ScriptListenerTreeNode) selectionPath.getPathComponent(i)).getNodeName() + "-->");
//                }else
//                    System.out.print("null");
//                System.out.println();
                if (updatingTree) {
                    return;
                }
                if (selectionCancelled) {
                    selectionCancelled = false;
                    return;
                }
                TreePath path = e.getPath();

                if (promptToSaveScript()) {
                    selectionCancelled = false;
//                    ScriptListenerHandleNode topNode = (ScriptListenerHandleNode) path.getPathComponent(0);
                    TreeNode node = (TreeNode) path.getLastPathComponent();
                    if (ScriptListenerTreeNode.class.isAssignableFrom(node.getClass())) {
                        ScriptListenerTreeNode bottomNode = (ScriptListenerTreeNode) path.getLastPathComponent();
//                        if (!scriptTree.isExpanded(path))
//                            scriptTree.expandPath(path);
                        ScriptListenerNode[] nodes = bottomNode.getScriptListenerNodes();
                        listenerList.clearSelection();
                        listenerListModel.clear();
                        if (nodes.length > 0) {
                            listenerListModel.addScriptListenerNodes(nodes);
                            listenerList.addSelectionInterval(0, 0);
                        }else{
							closeEditor();
                            selectedNode = null;
						}
                    }else{
						listenerList.clearSelection();
						listenerListModel.clear();
						if (ScriptNode.class.isAssignableFrom(node.getClass())) {
							// ScriptNode was selected
							ScriptNode scriptNode = (ScriptNode) node;
							// Update the current line for the previously selected node
							updateCurrentLine(selectedNode);
							selectedNode = scriptNode;
							setScript(scriptNode.getScript(), scriptNode.getName(),
									  scriptNode.getScriptLanguage(),
									  null,
									  null, scriptNode.getCurrentLine());
							setEditorEnabled(true);
						}else{
							closeEditor();
							selectedNode = null;
						}
					}
                }else{
                    TreePath p = e.getOldLeadSelectionPath();
                    if (p != null) {
                        selectionCancelled = true;
                        scriptTree.addSelectionPath(p);
                    }
                }
            }
        });

        listenerList.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
//System.out.println("e.getValueIsAdjusting(): " + e.getValueIsAdjusting());
                if (e.getValueIsAdjusting()) return;
                if (updatingTree) return;
                ScriptListenerNode node = (ScriptListenerNode) listenerList.getSelectedValue();
                if (selectedNode == node) {
                    return;
                }
                if (selectedNode != null) {
                    if (!promptToSaveScript()) {
                        // Cancel the new selection
                        listenerList.setSelectedValue(selectedNode, true);
                        return;
                    }
                }
				// Update the current line for the previously selected node
				updateCurrentLine(selectedNode);
                selectedNode = (ScriptListenerNode) node;
                if (selectedNode != null) {
                    setEditorEnabled(true);
                    setScript(selectedNode.getScript(), selectedNode.getName(), selectedNode.getScriptLanguage(), selectedNode.getScriptContainer(), ((ScriptListenerNode) selectedNode).listener.listenerClassName, selectedNode.getCurrentLine());
                    setScriptName(selectedNode.getName()); //listener.scriptName);
                    setEventVariables(selectedNode.getEventVariables()); //.eventVariables);
                    setNewScript(false);
                    setScriptChanged(false);
//                        System.out.println("Selected: " + selectedNode.nodeName);
                }else{
					closeEditor();
                }
            }
        });

        //Initialization
        setEditorEnabled(false);

        // ESCAPE to close the ScriptDialog
		actionName = "WindowCloseAction";
		WindowCloseAction wca = new WindowCloseAction(this, actionName);
		rootPaneActionMap.put(actionName, wca);
		rootPaneInputMap.put((KeyStroke) wca.getValue(AbstractAction.ACCELERATOR_KEY),
							  actionName
		);

        showDialog(container.getBounds());
    }

/*  The ScriptDialog does not close when the ScriptArea has the focus, probably because it
    consumes the 'Escape' KeyEvent. When the 'processKeyEvent' is overriden 'Escape' will
    close the ScriptDialog even when an ESlateOptionPane is open and Escape targets it...
	protected void processKeyEvent(KeyEvent e) {
		super.processKeyEvent(e);
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getModifiers() == 0) {
			dispatchEvent(new java.awt.event.WindowEvent(ScriptDialog.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
		}
	}
*/
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() != WindowEvent.WINDOW_CLOSING) {
            super.processWindowEvent(e);
            if (e.getID() == WindowEvent.WINDOW_OPENED || e.getID() == WindowEvent.WINDOW_DEICONIFIED) {
                scriptArea.requestFocus();
            }
        }else{
            if (!promptToSaveScript())
                return;
//#GV            
//            container.scriptDialogFontColorSettings = scriptArea.getColorSettings();
            container.scriptDialogMainFont = scriptArea.getFont();
            // Separate the ScriptListenerTree from the ScriptTree.
            rootNode.removeAllChildren();
            container.scriptDialog = null;
            dispose();
        }
    }

    // Displays the dialog
    public void showDialog(Rectangle compBounds) {
        pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int x, y;
        if (compBounds == null) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            java.awt.Point compLocation = new java.awt.Point(compBounds.x, compBounds.y);
            x = compLocation.x + compBounds.width/2 - getSize().width/2;
            y = compLocation.y + compBounds.height/2-getSize().height/2;
            if (x+getSize().width > screenSize.width)
                x = screenSize.width - getSize().width;
            if (y+getSize().height > screenSize.height)
                y = screenSize.height - getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        setLocation(x, y);
        scriptArea.requestFocus();
        show();
        hierarchySplitPane.setDividerLocation((double) 2/3);
        editorSplitPane.setDividerLocation((double) 3/9);
    }

	void closeEditor() {
		updateCurrentLine(selectedNode);
		setEventVariables(null);
		setScriptName(null);
		setScript(null, null, -1, null, null, -1);
		setEditorEnabled(false);

	}

	/**
	 * Updates the current line of the specified node. This usually happens
	 * when a script is closed (removed from the 'scriptArea'), so that the
	 * mext time it is opened, the scriptArea oes to this line.
	 * @param node
	 */
	private void updateCurrentLine(ScriptDialogNodeInterface node) {
		if (node == null) return;
		try{
			int currentLine = scriptArea.getLineOfOffset(scriptArea.getCaretPosition());
			selectedNode.setCurrentLine(currentLine);
		}catch (Throwable thr) {thr.printStackTrace();}
	}

    public String getScript() {
        return scriptArea.getText();
    }

    /* This method adds only to Java scripts the code enables the addition and removal of
     * listeners, which get called every time a script starts and finishes execution.
     * Also every method of the listener has to fire those events at exactly the beginning
     * and the end of the method, e.g. the actionPerformed() has to fire an event that the
     * script execution starts befor any of the script is executed and at the end of the
     * script it has to fire an event that informs of its termination.
     */
/*    private String enhanceJavaScript(ScriptListenerNode node, String script) {
        if (node.listener.scriptLanguage != ScriptListener.JAVA) return script;
        retur
    }
*/
    public String getScriptName() {
        return scriptNameTf.getText();
    }

    JEditTextArea getEditor() {
        return scriptArea;
    }

    public void setScript(String script, String scriptName, int language, Object listener, String listenerClassName, final int line) {
//System.out.println("setScript() script: " + script + ", scriptName: " + scriptName);
        if (script == null) {
            if (scriptArea.getDocument() != null)
                scriptArea.getDocument().removeDocumentListener(getDocumentListener());
            scriptArea.removeCaretListener(getCaretListener());
//            scriptArea.close(false);
            scriptArea.setText("");
        }else{
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int editorLanguage = JAVASCRIPT;
            if (language == ScriptListener.JAVA)
                editorLanguage = JAVA;
            else if (language == ScriptListener.LOGO)
                editorLanguage = LOGO;
            if (scriptArea.getDocument() != null)
                scriptArea.getDocument().removeDocumentListener(getDocumentListener());
            scriptArea.removeCaretListener(getCaretListener());
            scriptArea.setText("");
//            scriptArea.close(false);
//            scriptArea.createNewFile(editorLanguage);
            TextAreaPainter tp = scriptArea.getPainter();
            tp.setEOLMarkersPainted(false);
            // If we don't do this, then all instances share the same document!!!
            scriptArea.setDocument(new SyntaxDocument());
            scriptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            if (editorLanguage==JAVA) {
            	scriptArea.setTokenMarker(new JavaTokenMarker());
            }else{
            	if (editorLanguage==JAVASCRIPT) {
            		scriptArea.setTokenMarker(new JavaScriptTokenMarker());
            	}else{
            		if (editorLanguage==LOGO) {
            			scriptArea.setTokenMarker(new LogoTokenMarker());
            		}
            	}
            }

            scriptArea.setText(script);
//            scriptArea.setTitle(scriptName); //selectedNode.getNodeName());
//            scriptArea.clearHistory();
            currentScriptLanguage = language;
            if (currentScriptLanguage == ScriptListener.JAVA && listener != null) { //selectedNode.listener != null) {
                this.listenerClassName = listenerClassName; //selectedNode.listener.listenerClassName;
                this.listenerClassName = listenerClassName.substring(listenerClassName.lastIndexOf('.') + 1);
            }
            scriptArea.getDocument().addDocumentListener(getDocumentListener());
            scriptArea.addCaretListener(getCaretListener());
            cut.setEnabled(false); copy.setEnabled(false); run.setEnabled(false);
            setCursor(Cursor.getDefaultCursor());
//			javax.swing.SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
					try{
						int caretPos = scriptArea.getLineStartOffset(line);
						scriptArea.setCaretPosition(caretPos);
						// Does not work
						scriptArea.gotoLine(line);
					}catch (Throwable thr) {}
//				}
//			});
        }
/*        if (script == null)
            scriptArea.setText("");
        else
            scriptArea.setText(script);
*/
    }

    public void setScriptName(String scriptName) {
        scriptNameTf.setText(scriptName);
    }

    /* Though the script changes are tracked down and at any point we know if
     * the user has changed the script, we need an explicit 'equals()' check
     * so as to avoid some listener re-attachments. This is effective when the
     * script has been changed, but before the re-attachemnt the changes were undone.
     */
    public boolean scriptChanged() {
        if (selectedNode == null || selectedNode == scriptTree.getModel().getRoot())
            return false;
        if (selectedNode.getScriptContainer() == null) return false;
//System.out.println("selectedNode.getNodeName(): " + selectedNode.getNodeName() + ", listener: " + selectedNode.listener);
        String scriptBefore = selectedNode.getScript(); // .listener.script;
        String currentScript = getScript();
        if (scriptBefore != null && scriptBefore.trim().length() == 0)
            scriptBefore = null;
        if (currentScript != null && currentScript.trim().length() == 0)
            currentScript = null;

//System.out.println("scriptBefore.equals(currentScript): " + scriptBefore.equals(currentScript));
//System.out.println("currentScript: " + currentScript.length());
//System.out.println("scriptBefore: " + scriptBefore.length());
//System.out.println("currentScript: " + currentScript);
//System.out.println();
//System.out.println("scriptBefore: " + scriptBefore);

        if ((scriptBefore != null && currentScript != null && !scriptBefore.equals(currentScript))
          || (scriptBefore == null && currentScript != null)
          || (scriptBefore != null && currentScript == null))
              return true;
        return false;
    }

    /* If the script of the selected script listener has changed, then this method
     * asks the user if it wants to be saved. The user can answer with 'yes' or 'no'
     * or he can press 'cancel'. If 'cancel' is chosen, the method returns 'false'.
     * Otherwise it return 'true'.
     */
    boolean promptToSaveScript() {
//System.out.println("promptToSaveScript() disableSavePrompt: " + disableSavePrompt + ", selectedNode: " + selectedNode);
        if (disableSavePrompt) return true;
        if (selectedNode == null) return true;
//Thread.currentThread().dumpStack();
//System.out.println("promptToSaveScript() scriptChanged(): " + scriptChanged() + ", newScript: " + newScript);
        if (scriptChanged() || newScript) {
            // Warn the user about lost changes
            Object[] yes_no_cancel = {scriptDialogBundle.getString("Yes"), scriptDialogBundle.getString("No"), scriptDialogBundle.getString("Cancel")};

            JOptionPane pane = new JOptionPane(scriptDialogBundle.getString("ScriptChanged"),
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_CANCEL_OPTION,
                javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
                yes_no_cancel,
                scriptDialogBundle.getString("Yes"));
            javax.swing.JDialog dialog = pane.createDialog(ScriptDialog.this, scriptDialogBundle.getString("SaveChanges"));
            container.playSystemSound(SoundTheme.QUESTION_SOUND);
            dialog.show();
            Object option = pane.getValue();

//                System.out.println("option: " + option + ", getClass(): " + option.getClass());
            if (option == scriptDialogBundle.getString("Cancel"))
                return false;
            if (option == null || option.toString().equals("-1") || option == scriptDialogBundle.getString("No")) {
				if (isNodeScriptListenerNode(selectedNode)) {
					ScriptListenerNode node = (ScriptListenerNode) selectedNode;
					if (newScript) {
						container.componentScriptListeners.removeScriptListener(node.listener);
						disableSavePrompt = true;
						listenerListModel.removeScriptListenerNode(node);
						disableSavePrompt = false;
						setNewScript(false);
	//                    removeScript(selectedNode);
					}else{
						setScript(node.listener.getScript(),
								  node.getNodeName(),
								  node.listener.scriptLanguage, node.listener, node.listener.listenerClassName, node.getCurrentLine());
					}
					setScriptChanged(false);
					return true;
				}else{
					ScriptNode node = (ScriptNode) selectedNode;
					if (newScript) {
						container.scriptMap.removeScript(node.script);
						updateScriptTree();
						setNewScript(false);
	//                    removeScript(selectedNode);
					}else{
						setScript(node.script.script, node.getName(),
								  node.getScriptLanguage(), null,
								  null,
								  node.getCurrentLine());
					}
					setScriptChanged(false);
					return true;
				}
            }
            if (option == scriptDialogBundle.getString("Yes")) {
				if (isNodeScriptListenerNode(selectedNode)) {
					attachListener((ScriptListenerNode) selectedNode);
				}else{
					compileClass((ScriptNode) selectedNode);
				}
			}
        }
        return true;
    }

    void setEditorEnabled(boolean enabled) {
        if (editorEnabled == enabled) return;
        editorEnabled = enabled;
        String selectedScript = scriptArea.getSelectedText();
        if (editorEnabled && selectedScript != null && selectedScript.length() != 0) {
            copy.setEnabled(true);
            cut.setEnabled(true);
            run.setEnabled(true);
        }else{
            copy.setEnabled(false);
            cut.setEnabled(false);
            run.setEnabled(false);
        }

        paste.setEnabled(editorEnabled);
        if (!editorEnabled)
            compileAction.setEnabled(false);
        else{
            if (scriptChanged || newScript)
                compileAction.setEnabled(true);
            else
                compileAction.setEnabled(false);
        }
        find.setEnabled(editorEnabled);
        findNext.setEnabled(editorEnabled);
//        findPrev.setEnabled(editorEnabled);
        replace.setEnabled(editorEnabled);
        clear.setEnabled(editorEnabled);
        load.setEnabled(editorEnabled);
        save.setEnabled(editorEnabled);
        goToLine.setEnabled(editorEnabled);
        fontType.setEnabled(editorEnabled);
        fontSize.setEnabled(editorEnabled);
        if (!editorEnabled) {
            disableFontListeners = true;
            fontType.setSelectedIndex(-1);
            fontSize.setSelectedIndex(-1);
            disableFontListeners = false;
        }else{
            disableFontListeners = true;
            Font mainFont = container.scriptDialogMainFont;
            if (mainFont != null) {
                fontType.setSelectedItem(mainFont.getName());
                fontSize.setSelectedItem(new Integer(mainFont.getSize()).toString());
            }else{
                fontType.setSelectedItem(scriptArea.getFont().getName());
                fontSize.setSelectedItem(new Integer(scriptArea.getFont().getSize()).toString());
            }
            disableFontListeners = false;
        }
//        colors.setEnabled(editorEnabled);
        if (!editorEnabled) {
            rowColumnLabel.setText("");
            lineCountLabel.setText("");
            statusLabel.setText("");
        }else{
            if (newScript)
                statusLabel.setText(scriptDialogBundle.getString("New script"));
            else{
                if (!scriptChanged)
                    statusLabel.setText("");
                else
                    statusLabel.setText(scriptDialogBundle.getString("Modified"));
            }
        }

        scriptNameTf.setEnabled(editorEnabled);
        scriptNameLb.setEnabled(editorEnabled);
        varLb1.setEnabled(editorEnabled);
        variableButton.setEnabled(editorEnabled);

        scriptArea.setEditable(editorEnabled);
/*        if (!editorEnabled)
            scriptArea.getCaret().deinstall(scriptArea); //.setVisible(editorEnabled);
        else
            scriptArea.getCaret().install(scriptArea); //.setVisible(editorEnabled);
System.out.println("scriptArea.setEditable(" + editorEnabled + ")");
*/
/*ce        if (editorEnabled) {
            if (scriptArea.hasFocus())
                scriptArea.getCaret().setVisible(true);
        }else
            scriptArea.getCaret().setVisible(false);
*/
        if (!editorEnabled) {
            setScriptChanged(false);
            setNewScript(false);
        }
    }

    boolean isEditorEnabled() {
        return editorEnabled;
    }

    void setScriptChanged(boolean changed) {
        if (scriptChanged == changed) return;
//if (changed)
//System.out.println("setScriptChanged(): " + changed);
//Thread.currentThread().dumpStack();
        scriptChanged = changed;
        compileAction.setEnabled(scriptChanged);
        if (!newScript) {
            if (scriptChanged)
                statusLabel.setText(scriptDialogBundle.getString("Modified"));
            else
                statusLabel.setText("");
        }
    }

    boolean isScriptChanged() {
        return scriptChanged;
    }

    void setNewScript(boolean newScript) {
        if (this.newScript == newScript) return;
        this.newScript = newScript;
//        if (newScript == false)
//            Thread.currentThread().dumpStack();
        compileAction.setEnabled(newScript);
        statusLabel.setText(scriptDialogBundle.getString("New script"));
    }

    boolean isNewScript() {
        return newScript;
    }

    void removeScript(ScriptListenerNode node) {
//System.out.println("removeScript() node: " + node.getNodeName());
        String methodName = node.listener.methodName;
        TreeNode parentNode = node.parent;
        while (!ScriptListenerHandleNode.class.isAssignableFrom(parentNode.getClass())) {
            parentNode = parentNode.getParent();
        }
        ESlateHandle handle = ((ScriptListenerHandleNode) parentNode).handle;
        int language = node.listener.scriptLanguage;
        container.componentScriptListeners.removeScriptListener(node.listener);
        if (!newScript && handle != null) {
            updateBeanInfoDialog(handle, methodName, node, language);
        }

        int index = listenerListModel.indexOf(node);
        if (index == -1) return;
        if (node == selectedNode) {
            selectedNode = null;
            int numOfScripts = listenerListModel.getSize();
			// If this was the last listener, close the editor.
            if (numOfScripts == 1) {
				closeEditor();
            }else{
				disableSavePrompt = true;
				// Try to select the next listener. If there is no next listener
				// select the previous one.
				if ((index+1) < numOfScripts) index++;
				else index--;
				listenerList.setSelectedIndex(index);
				disableSavePrompt = false;
            }
        }
		listenerListModel.removeScriptListenerNode(node);
        listenerList.revalidate();
    }

    public ScriptDialogNodeInterface getSelectedNode() {
        return selectedNode;
    }

	boolean isNodeScriptListenerNode(ScriptDialogNodeInterface node) {
		if (node == null) {
			return false;
		}
		return (ScriptListenerNode.class.isAssignableFrom(node.getClass()));
	}

    public void setEventVariables(String[] varNames) {
            variableMenu.removeAll(); //(0);
        if (varNames == null) return;
//        if (selectedNode.listener.scriptLanguage == ScriptListener.LOGO) {
		if (selectedNode.getScriptLanguage() == ScriptListener.LOGO) {
            JMenuItem item = variableMenu.add(new JMenuItem(varNames[0]));
            item.addActionListener(variableMenuItemListener);
            variableMenu.addSeparator();
            for (int i=1; i<varNames.length; i++) {
                item = variableMenu.add(new JMenuItem(varNames[i]));
                item.addActionListener(variableMenuItemListener);
            }
        }else if (selectedNode.getScriptLanguage() == ScriptListener.JAVASCRIPT) {
            for (int i=0; i<5; i++) {
                JMenuItem item = variableMenu.add(new JMenuItem(varNames[i]));
                item.addActionListener(variableMenuItemListener);
            }
            variableMenu.addSeparator();
            for (int i=5; i<varNames.length; i++) {
                JMenuItem item = variableMenu.add(new JMenuItem(varNames[i]));
                item.addActionListener(variableMenuItemListener);
            }
        }else{
            for (int i=0; i<varNames.length; i++) {
                JMenuItem item = variableMenu.add(new JMenuItem(varNames[i]));
                item.addActionListener(variableMenuItemListener);
            }
        }
    }

    protected void saveScriptToFile() {
        int fileDialogMode = java.awt.FileDialog.SAVE;

        if (fileDialog == null)
            fileDialog = new ESlateFileDialog(this, scriptDialogBundle.getString("FileDialogTitle"), fileDialogMode);

        if (fileDialog.isShowing()) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        fileDialog.setTitle(scriptDialogBundle.getString("FileDialogTitle"));
        fileDialog.setMode(fileDialogMode);
        if (container.logoScriptDialogCurrentDir != null)
            fileDialog.setDirectory(container.logoScriptDialogCurrentDir);
        /* Formulate the string to use in fileDialog.setFile(). This string contains all the
         * valid microworld file extensions as they are declared in the container.properties file.
         */
        if (currentScriptLanguage == ScriptListener.LOGO) {
            fileDialog.setFile("*.lgo");
            fileDialog.setDefaultExtension(new String[] {"lgo"});
        }else if (currentScriptLanguage == ScriptListener.JAVASCRIPT) {
            fileDialog.setFile("*.js");
            fileDialog.setDefaultExtension(new String[] {"js"});
        }else if (currentScriptLanguage == ScriptListener.JAVA) {
            fileDialog.setFile("*.java");
            fileDialog.setDefaultExtension(new String[] {"java"});
        }

        /* Center the fileDialog inside the Container.
         */
        fileDialog.pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        Rectangle compBounds = getBounds();
        java.awt.Point compLocation = getLocationOnScreen();
//        System.out.println("compBounds: " + compBounds + ", compLocation: " + compLocation);
        Dimension fileDialogSize = new Dimension(426, 264);
//        System.out.println("File dialog size: " + fileDialogSize);
        x = compLocation.x + compBounds.width/2 - fileDialogSize.width/2;
        y = compLocation.y + compBounds.height/2-fileDialogSize.height/2;
        if (x+fileDialogSize.width > screenSize.width)
            x = screenSize.width - fileDialogSize.width;
        if (y+fileDialogSize.height > screenSize.height)
            y = screenSize.height - fileDialogSize.height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        fileDialog.setLocation(x, y);
        fileDialog.show();

        String fileName = fileDialog.getFile();
        if (fileName == null) {
            setCursor(Cursor.getDefaultCursor());
            return;
        }
        String currDirectory = fileDialog.getDirectory();
        container.logoScriptDialogCurrentDir = currDirectory;
        fileName = currDirectory+fileName;

        FileWriter fw = null;
        try{
            fw = new FileWriter(fileName);
        }catch (IOException e) {
            setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("UnableToSave") + fileName + "\"", scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        BufferedWriter bw = new BufferedWriter(fw, 30000);
        PrintWriter pw = new PrintWriter(bw);
        pw.println(scriptArea.getText());
        pw.close();
        setCursor(Cursor.getDefaultCursor());
    }

	private String saveJavaListenerFile(String[] scriptPackage, String newListenerClassName, String script, boolean updateClassName) {
//        if (listenerFileName == null)
//            return false;
		if (updateClassName) {
			String currentListenerClassName = listenerClassName;

			int index = script.indexOf(currentListenerClassName);
	//System.out.println("currentListenerClassName: " + currentListenerClassName + ", index: " + index);
			if (index != -1) {
				/* If the 'currentListenerClassName' is found in the script, then it'll be replaced
				 * with the 'newListenerClassName'.
				 */
				scriptArea.replaceAll(currentListenerClassName, newListenerClassName);
//	            System.out.println("CALLING scriptArea.replace() " + currentListenerClassName + ", " + newListenerClassName);
				script = scriptArea.getText();
				index = script.indexOf(currentListenerClassName);
				if (index != -1) {
	//                System.out.println("REPLACE FAILED");
	//                System.out.println("index: " + index + ", index + currentListenerClassName.length(): " + index + currentListenerClassName.length());
					scriptArea.select(index, index + currentListenerClassName.length());
					scriptArea.setSelectedText(newListenerClassName);
	//                System.out.println("script.indexOf(currentListenerClassName): " + script.indexOf(currentListenerClassName));

	//                System.out.println("script: " + script);
					script = scriptArea.getText();
				}
			}else{
				/* If the 'currentListenerClassName' is not found in the script, we'll search for
				 * whatever is between the first occurence of 'public class ' and the word after
				 * the next one, and replace it with the 'newListenerClassName'.
				 */
				index = script.indexOf("public class ");
				if (index != -1) {
					index = index + "public class ".length();
					int endIndex = index;
					while (script.charAt(endIndex) == ' ') endIndex++;
					while (script.charAt(endIndex) != ' ') endIndex++;
	//        System.out.println("3 Replacing \"" + script.substring(index, endIndex) + " with " + newListenerClassName);
					scriptArea.select(index, endIndex);
					scriptArea.setSelectedText(newListenerClassName);
					script = scriptArea.getText();
				}
			}
		}

		// The script's .java file will be writen in a directory structure that
        // matches its package.
		File outputDir = ESlateContainerUtils.getScriptDir();
		if (scriptPackage.length > 0) {
			for (int i=0; i<scriptPackage.length; i++) {
				outputDir = new File(outputDir, scriptPackage[i]);
			}
			if (!outputDir.exists() && !outputDir.mkdirs()) {
				System.out.println("Could not create directory " + outputDir);
				return null;
			}
		}
        File listenerFile = new File(outputDir/*ESlateContainerUtils.tmpDir*/, newListenerClassName + ".java");
      	java.io.PrintWriter out = null;
      	try {
//            java.io.FileWriter fout = new java.io.FileWriter(listenerFile);
			java.io.FileOutputStream fout = new java.io.FileOutputStream(listenerFile);
      	    out = new java.io.PrintWriter(new java.io.BufferedOutputStream(fout));
      	} catch (Exception ex) {
            System.err.println("Couldn't open hookup file " + listenerFile.getPath());
      	    System.err.println("   " + ex);
            return null;
      	}
//        StringBuffer buff = new StringBuffer(script);
//        buff.replace(startIndex, endIndex, newListenerClassName);
//        script = buff.toString();
        out.println(script);
        out.close();
        return listenerFile.getPath(); //new String[] {listenerFile.getPath(), script};
    }

    protected void loadScriptFromFile() {
        int fileDialogMode = java.awt.FileDialog.LOAD;

        if (fileDialog == null)
            fileDialog = new ESlateFileDialog(this, scriptDialogBundle.getString("OpenFileDialogTitle"), fileDialogMode);

        if (fileDialog.isShowing()) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        fileDialog.setTitle(scriptDialogBundle.getString("OpenFileDialogTitle"));
        fileDialog.setMode(fileDialogMode);
        if (container.logoScriptDialogCurrentDir != null)
            fileDialog.setDirectory(container.logoScriptDialogCurrentDir);
        /* Formulate the string to use in fileDialog.setFile(). This string contains all the
         * valid microworld file extensions as they are declared in the container.properties file.
         */
        if (currentScriptLanguage == ScriptListener.LOGO) {
            fileDialog.setFile("*.lgo");
            fileDialog.setDefaultExtension(new String[] {"lgo"});
        }else if (currentScriptLanguage == ScriptListener.JAVASCRIPT) {
            fileDialog.setFile("*.js");
            fileDialog.setDefaultExtension(new String[] {"js"});
        }else if (currentScriptLanguage == ScriptListener.JAVA) {
            fileDialog.setFile("*.java");
            fileDialog.setDefaultExtension(new String[] {"java"});
        }

        /* Center the fileDialog inside the Container.
         */
        fileDialog.pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        Rectangle compBounds = getBounds();
        java.awt.Point compLocation = getLocationOnScreen();
//        System.out.println("compBounds: " + compBounds + ", compLocation: " + compLocation);
        Dimension fileDialogSize = new Dimension(426, 264);
//        System.out.println("File dialog size: " + fileDialogSize);
        x = compLocation.x + compBounds.width/2 - fileDialogSize.width/2;
        y = compLocation.y + compBounds.height/2-fileDialogSize.height/2;
        if (x+fileDialogSize.width > screenSize.width)
            x = screenSize.width - fileDialogSize.width;
        if (y+fileDialogSize.height > screenSize.height)
            y = screenSize.height - fileDialogSize.height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        fileDialog.setLocation(x, y);
        fileDialog.show();

        String fileName = fileDialog.getFile();
        if (fileName == null) {
            setCursor(Cursor.getDefaultCursor());
            return;
        }
        String currDirectory = fileDialog.getDirectory();
        container.logoScriptDialogCurrentDir = currDirectory;
        fileName = currDirectory+fileName;

        FileReader fr = null;
        try{
            fr = new FileReader(fileName);

            BufferedReader br = new BufferedReader(fr, 30000);
            String script = new String();
            String line;
            while ((line = br.readLine()) != null)
                script = script + line + '\n';
            br.close();
			if (isNodeScriptListenerNode(selectedNode)) {
				setScript(script, selectedNode.getName(),
						  currentScriptLanguage, selectedNode.getScriptContainer(),
						  ((ScriptListenerNode) selectedNode).listener.listenerClassName,
						  selectedNode.getCurrentLine());
			}else{
				setScript(script, selectedNode.getName(),
						  currentScriptLanguage, null,
						  null,
						  selectedNode.getCurrentLine());
			}
			compileAction.setEnabled(true);
        }catch (IOException e) {
            setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("UnableToLoad") + fileName + "\"", scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
//        PrintWriter pw = new PrintWriter(bw);
//        pw.println(scriptArea.getText());
//        pw.close();
        setCursor(Cursor.getDefaultCursor());
    }

	/** Compiles a script, i.e. a global script and not a listener script.
	 * The process of defining global scripts is different from that for
	 * defining listeners. The differences are:
	 * <ul>
	 * <li> 1. The name of the class of the script does not change from compilation
	 * to compilation. A ClassLoader cannot load the same class more than once.
	 * To achieve that script class names do not change, a new ClassLoader is
	 * used to load the same script after each compilation. See SimpleClassLoader.
	 * <li> 2. The package of the script is adjustable by the user. Any or no
	 * package can be specified. In order for jikes to compile other scripts or
	 * listeners which use this script, the script is writen in a directory
	 * structure that matches its package name. Everytime the ScriptDialog is
	 * displayed, all the Scripts of the ScriptMap are exracted into class files.
	 * See extractClassFiles() of ScriptMap.
	 * <li> 3. Every time a Script is re-compiled any other Script or
	 * ScriptListener(listener) which uses it has to be updated in order
	 * to see the new definition of the Script's class. This is achieved by
	 * replacing the 'listenerLoader' of the SimpleClassLoader with a new
	 * SimpleClassLoader instance, reloading all the java listeners of the
	 * ScriptListeners from their byte arrays, detaching the previous listeners,
	 * creating new listeners and re-attaching them. See
	 * SimpleClassLoader.refreshSharedInstance() and
	 * ScriptListenerMap.reloadAllJavaScriptListeners().
	 * <li> 4. No instance of a Script's class is created after compilation.
	 * Other scripts or listeners created instances on demand.
	 * </ul>
	 */
	protected void compileClass(ScriptNode node) {
		if(node == null) return;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// if the new script is null or empty then remove the current listener, if any exists.
		if (getScript() == null || getScript().trim().length() == 0) {
			setScript(null, null, -1, null, null, -1);
//			removeComponentScriptListener(node);
			setScriptChanged(false);
			setNewScript(false);
			setCursor(Cursor.getDefaultCursor());
			return;
		}

		// if the script hasn't been changed, return.
		if (!scriptChanged() && !newScript) {
			setCursor(Cursor.getDefaultCursor());
			setScriptChanged(false);
			setNewScript(false);
			return;
		}

		String listenerClassName = node.getName();
		String script = getScript();
		String[] scriptPackage = getPackage(script);
		String s = saveJavaListenerFile(scriptPackage, listenerClassName, script, false);
		if (s == null) {
			setCursor(Cursor.getDefaultCursor());
			return;
		}
		String listenerFileName = s; //s[0];
		// Create the full class name of the script
		StringBuffer buff = new StringBuffer();
		for (int i=0; i< scriptPackage.length; i++) {
			buff.append(scriptPackage[i]);
			buff.append('.');
		}
		buff.append(node.getName());
		Class listenerClass = compileJavaScript(node, listenerFileName, false, SimpleClassLoader.getNewInstance(buff.toString()));
		node.script.setPackage(scriptPackage);
		node.script.script = script;
		SimpleClassLoader.refreshSharedInstance();
		container.componentScriptListeners.reloadAllJavaScriptListeners();

		setScriptChanged(false);
		setNewScript(false);
		setCursor(Cursor.getDefaultCursor());
	}

	/** It accepts a script and returns its package as a String array.
	 */
	private String[] getPackage(String script) {
		int index = script.indexOf("package ");
		if (index == -1) return new String[0];
		int packageStart = index + 8;
		int packageEnd = script.indexOf(';');
		String packageName = script.substring(packageStart, packageEnd).trim();
		return breakPackageNameIntoParts(packageName);
	}

	String[] breakPackageNameIntoParts(String packageName) {
		packageName = packageName.trim();
		if (packageName.indexOf(' ') != -1 || packageName.indexOf('\n') != -1) {
			return new String[0];
		}
		StringBaseArray packageMembers = new StringBaseArray();
		StringTokenizer strTokenizer = new StringTokenizer(packageName, ".", false);
		while (strTokenizer.hasMoreTokens()) {
			packageMembers.add(strTokenizer.nextToken());
		}
		return packageMembers.toArray();
	}

	/**
	 * Removes the class of the specified ScriptNode. When removing a class the
	 * listeners of all the ScriptListeners (java listeners) of the microworld
	 * are re-loaded, thus forcing the redefinition of their classes, which
	 * may use the removed class.
	 * @param node The node whose class is removed from the microworld.
	 */
	private void removeClass(ScriptNode node) {
		Script script = node.script;
		// Find the next node to be selected. First we try the next node, if
		// one exists. The we try the prvious ScriptNode. If this fails too,
		// we select the 'rootScriptNode'.
		TreeNode nodeToBeSelected = container.scriptMap.getScriptNode(node, false);
		if (nodeToBeSelected == null) {
			nodeToBeSelected = container.scriptMap.getScriptNode(node, true);
			if (nodeToBeSelected == null) {
				nodeToBeSelected = rootScriptNode;
			}
		}
		TreePath newSelectionPath = null;
		if (nodeToBeSelected != rootScriptNode) {
			newSelectionPath = new TreePath(new Object[] {rootNode, rootScriptNode, nodeToBeSelected});
		}else{
			newSelectionPath = new TreePath(new Object[] {rootNode, rootScriptNode});
		}
		// First select the new ScriptNode
		disableSavePrompt = true;
		scriptTree.setSelectionPath(newSelectionPath);
		disableSavePrompt = false;

		// Remove the ScriptNode
		container.scriptMap.removeScript(script);
		updateScriptTree();
		SimpleClassLoader.removeInstance(script.getFullClassName());

		// Reload all the ScriptListeners
		SimpleClassLoader.refreshSharedInstance();
		container.componentScriptListeners.reloadAllJavaScriptListeners();
	}

    protected void attachListener(ScriptListenerNode node) {
        if(node == null) return;
//System.out.println("node.eventDescriptor: " + node.eventDescriptor);
        boolean listenerActivated = false;
        ScriptListener scriptListener = node.listener;
        EventListener currentListener = (EventListener) scriptListener.listener;
        // Find the ESlate component to which the object this listener is attached to belongs
        TreeNode parentNode = node.parent;
        while (!ScriptListenerHandleNode.class.isAssignableFrom(parentNode.getClass()))
            parentNode = parentNode.getParent();
        ESlateHandle handle = ((ScriptListenerHandleNode) parentNode).handle;
        if (handle != null) {
            String componentName = handle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                ESlateOptionPane.showMessageDialog(ScriptDialog.this,
                        scriptDialogBundle.getString("CompileDenialMsg1") +
                          ((ScriptListenerHandleNode) parentNode).getNodeName() +
                          scriptDialogBundle.getString("CompileDenialMsg2"),
                          scriptDialogBundle.getString("Error"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // if the new script is null or empty then remove the current listener, if any exists.
        if (getScript() == null || getScript().trim().length() == 0) {
            setScript(null, null, -1, null, null, -1);
            removeComponentScriptListener(node);
            setScriptChanged(false);
            setNewScript(false);
            setCursor(Cursor.getDefaultCursor());
            return;
        }

        // if the script hasn't been changed, return.
        if (!scriptChanged() && !newScript) {
//            if (scriptName != getScriptName()) {
//                scriptName = getScriptName();
//            }
            setCursor(Cursor.getDefaultCursor());
            setScriptChanged(false);
            setNewScript(false);
            return;
        }

        String script = getScript();
        String scriptName = getScriptName();
        String methodName = scriptListener.methodName;

        InvocationHandler handler = null;
        Object listener = null;
//        String newScript = script;
        if (scriptListener.getScriptLanguage() == ScriptListener.LOGO) {
//System.out.println("node.eventDescriptor: " + node.eventDescriptor);
            LogoScriptHandler logoHandler = new LogoScriptHandler(methodName, node.eventDescriptor.getListenerType());
            logoHandler.setScript(script);
            listener = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                              new Class[] { node.eventDescriptor.getListenerType() },
                                              logoHandler);

            /* If the logo runtime hasn't already been started, initialize it now.
             */
            if (container.logoMachine == null) {
                container.initLogoEnvironment();
                container.startWatchingMicroworldForPrimitiveGroups();
            }

            /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
             * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
             * However these Java listener's setESlateHandle() has to be called with the
             * microworld's handle.
             */
            ESlateHandle listenerHandle = handle;
            if (gr.cti.eslate.base.container.event.MicroworldListener.class.isAssignableFrom(node.eventDescriptor.getListenerType()))
                listenerHandle = container.microworld.eslateMwd.getESlateHandle();

            logoHandler.setLogoRuntime(listenerHandle,
                                       container.logoMachine,
                                       container.logoEnvironment,
                                       container.logoThread,
                                       container.tokenizer);
            handler = logoHandler;
        }else if (scriptListener.getScriptLanguage() == ScriptListener.JAVASCRIPT) {
            JavascriptHandler jsHandler = new JavascriptHandler(methodName, node.eventDescriptor.getListenerType());
            jsHandler.setScript(script);
            listener = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                              new Class[] { node.eventDescriptor.getListenerType() },
                                              jsHandler);

            /* If the javascript runtime hasn't already been started, initialize it now.
             */
            if (!container.javascriptInUse)
                container.registerJavascriptVariables();

            /* Listeners for the events of the gr.cti.eslate.base.container.Microworld are saved
             * with a HierarchyComponentPath that starts with the handle of the ESlateContainer.
             * However these Java listener's setESlateHandle() has to be called with the
             * microworld's handle.
             */
            ESlateHandle listenerHandle = handle;
            if (gr.cti.eslate.base.container.event.MicroworldListener.class.isAssignableFrom(node.eventDescriptor.getListenerType()))
                listenerHandle = container.microworld.eslateMwd.getESlateHandle();

            jsHandler.setRuntimeInfo(listenerHandle);
            handler = jsHandler;
        }else if (scriptListener.getScriptLanguage() == ScriptListener.JAVA) {
//System.out.println("-------------------------------------------------------------");
//System.out.println("Previous class: " + listenerClassName);
//            enhanceJavaScript(node, script);
			String newListenerClassName = node.eventDescriptor.getListenerType().getName() + '_' + ESlateContainerUtils.createUniqueId(0);
			newListenerClassName = newListenerClassName.substring(newListenerClassName.lastIndexOf('.') + 1);
            String s = saveJavaListenerFile(new String[0], newListenerClassName, script, true);
            if (s == null) {
                setCursor(Cursor.getDefaultCursor());
                return;
            }
            String listenerFileName = s; //s[0];
//            newScript = s[1];
            Class listenerClass = compileJavaScript(node, listenerFileName, true, SimpleClassLoader.getListenerLoader());
            if (listenerClass == null) {
                /* Replace the previous listener name to the script */
                String newListenerName = listenerFileName.substring(listenerFileName.lastIndexOf(System.getProperty("file.separator"))+1, listenerFileName.length()-5);
//System.out.println("REPLACING " + newListenerName + " with " + listenerClassName);
                scriptArea.replaceAll(newListenerName, listenerClassName);
                setCursor(Cursor.getDefaultCursor());
                return;
            }
            listener = instantiateJavaScriptListener(listenerClass, handle);
            if (listener == null) {
                setCursor(Cursor.getDefaultCursor());
                return;
            }
        }

        // Add the instance of the listener class to the component.
        Method addListenerMethod = node.eventDescriptor.getAddListenerMethod();
        try{
			Method removeListenerMethod = node.eventDescriptor.getRemoveListenerMethod();
            if (currentListener != null) {
                // Remove the installed listener
                removeListenerMethod.invoke(node.object, new Object[] {currentListener});
            }
//System.out.println("addListenerMethod: " + addListenerMethod);
//Method[] methods = listener.getClass().getMethods();
//for (int i=0; i<methods.length; i++)
//    System.out.println("method: " + methods[i]);
//System.out.println("listener instanceof horst.webwindow.event.LinkListener.class: " + (horst.webwindow.event.LinkListener.class.isAssignableFrom(listener.getClass())));
//System.out.println("listener instanceof EventListener.class: " + (EventListener.class.isAssignableFrom(listener.getClass())));
//System.out.println("node.object: " + node.object.getClass());
            addListenerMethod.invoke(node.object, new Object[] {listener});
//                currentListener = (java.util.EventListener) listener;
            updateComponentScriptListeners(handle, node, scriptArea.getText(), (EventListener) listener,
                                           scriptListener.listenerClassBytes, handler,
										   addListenerMethod, removeListenerMethod);
            listenerActivated = true;
//                updateEventDialog();
            if (currentScriptLanguage == ScriptListener.JAVA) {
                Class listenerClass = node.listener.listener.getClass();
//System.out.println("1. listenerClassName: " + listenerClassName);
//                String oldClassName = listenerClassName;
                listenerClassName = listenerClass.getName().substring(listenerClass.getName().lastIndexOf('.') + 1);
//System.out.println("2. listenerClassName: " + listenerClassName);
//System.out.println("Calling replace " + oldClassName + ", " + listenerClassName);
//                scriptArea.replace(oldClassName, listenerClassName);
//                int caretPos = scriptArea.getCaretPosition();
//                setScript(newScript, ScriptListener.JAVA);
//                scriptArea.setCaretPosition(caretPos);
//System.out.println("scriptListener.script: " + node.listener.script);
//                System.out.println("new listener class name: " + listenerClassName);
//System.out.println("-------------------------------------------------------------");
//System.out.println();
            }
            setScriptChanged(false);
            setNewScript(false);
            setCursor(Cursor.getDefaultCursor());
        }catch (IllegalAccessException exc) {
            setCursor(Cursor.getDefaultCursor());
            System.out.println("IllegalAccessException while adding/activating listener");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }catch (IllegalArgumentException exc) {
            setCursor(Cursor.getDefaultCursor());
            System.out.println("2. IllegalArgumentException while adding/activating listener");
            exc.printStackTrace();
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }catch (java.lang.reflect.InvocationTargetException exc) {
            setCursor(Cursor.getDefaultCursor());
            exc.printStackTrace();
            System.out.println("InvocationTargetException while adding/activating listener");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }catch (Throwable exc) {
            setCursor(Cursor.getDefaultCursor());
            exc.printStackTrace();
            System.out.println("Exception while adding listener");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }finally{
            if (!listenerActivated)
                container.componentScriptListeners.removeScriptListener(node.listener);
        }
    }

    /* Compiles a Java script, loads the listener's class and returns it */
//    private Class compileJavaScript(ScriptListenerNode node, String listenerFileName) {
	private Class compileJavaScript(ScriptDialogNodeInterface node, String listenerFileName, boolean deleteClassFiles, SimpleClassLoader classLoader) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        scriptArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//System.out.println("SCRIPT: " + script);
//        String formulatedScript = ESlateContainerUtils.formulateScript(script);
//System.out.println("FORMULATED SCRIPT: " + script);
        String listenerClassPathName = ESlateComposerUtils.compileJavaFile(
                                                  this,
                                                  listenerFileName,
												  node.getName(),
//                                                  node.listener.methodName,
                                                  scriptDialogBundle);
//        if (container.containerUtils.existsInTmpDir(listenerFileName))
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        scriptArea.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ESlateContainerUtils.deleteFile(listenerFileName);
        if (listenerClassPathName == null) {
            setCursor(Cursor.getDefaultCursor());
            scriptArea.setCursor(Cursor.getDefaultCursor());
            return null;
        }

//            String listenerClassFileName = listenerFileName.substring(0, listenerFileName.lastIndexOf(".")) + ".class";

        // Load the produced listener class
        try{
//            node.listener.listenerClassBytes = new byte[1][0];
//            node.listener.listenerClassBytes[0] = loader.getByteArray(listenerClassPathName);
			node.resetInnerClasses();
			node.setListenerClassBytes(new byte[1][0]);
			node.setListenerClassBytes(0, classLoader.getByteArray(listenerClassPathName));
			if (deleteClassFiles) {
				ESlateContainerUtils.deleteFile(listenerClassPathName);
			}

//            Class listenerCl = loader.loadClassFromByteArray(node.listener.listenerClassBytes[0]);
			Class listenerCl = classLoader.loadClassFromByteArray(node.getListenerClassBytes(0));
            /* If the listener defined any inner classes, then find there .class files,
             * load them with the class loader and add them to the ScriptListener, so that
             * they are saved.
             */
            File[] innersClassFiles = getInnerClassFiles(listenerClassPathName);
            for (int i=0; i<innersClassFiles.length; i++) {
                byte[] innerClassBytes = classLoader.getByteArray(innersClassFiles[i].getPath());
//                node.listener.addInnerClass(innerClassBytes);
				node.addInnerClass(innersClassFiles[i].getName(), innerClassBytes);
                classLoader.loadClassFromByteArray(innerClassBytes);
            }
            setCursor(Cursor.getDefaultCursor());
            scriptArea.setCursor(Cursor.getDefaultCursor());
            return listenerCl;
        }catch (ClassNotFoundException exc) {
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            System.out.println("ClassNotFoundException while loading listener class");
//            node.listener.listenerClassBytes = null;
			node.setListenerClassBytes(null);
            setCursor(Cursor.getDefaultCursor());
            scriptArea.setCursor(Cursor.getDefaultCursor());
            return null;
        }catch (Throwable exc) {
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            System.out.println("IOException while loading listener class from file " + listenerClassPathName);
            exc.printStackTrace();
//            node.listener.listenerClassBytes = null;
			node.setListenerClassBytes(null);
            setCursor(Cursor.getDefaultCursor());
            scriptArea.setCursor(Cursor.getDefaultCursor());
            return null;
        }
    }

    private EventListener instantiateJavaScriptListener(Class listenerClass, ESlateHandle handle) {
        EventListener hookup = null;
        try{
            hookup = (EventListener) listenerClass.newInstance();
//            System.out.println("hookup: " + hookup);
            return hookup;
//            System.out.println("handleSetterMethod: " + handleSetterMethod);
        }catch (InstantiationException exc) {
            System.out.println("InstantiationException while instantiating listener object");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }catch (IllegalAccessException exc) {
            System.out.println("IllegalAccessException while instantiating listener object or while setting listener's eSlateHandle");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
        }catch (IllegalArgumentException exc) {
            System.out.println("IllegalArgumentException while setting listener's eSlateHandle");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
/*        }catch (java.lang.reflect.InvocationTargetException exc) {
            System.out.println("InvocationTargetException while setting listener's eSlateHandle");
            ESlateOptionPane.showMessageDialog(this, scriptDialogBundle.getString("CompileFailureMessage3"), scriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return null;
*/
        }
    }

    private File[] getInnerClassFiles(String listenerClassPathName) {
        File tmpDir = new File(listenerClassPathName).getParentFile();// listenerClassFileName.substring(0, listenerClassFileName.lastIndexOf(System.getProperty("file.separator")));
        final String strippedListenerClassFileName = listenerClassPathName.substring(0, listenerClassPathName.indexOf(".class"));
        File[] files = tmpDir.listFiles(new java.io.FileFilter() {
            public boolean accept(File pathName) {
//System.out.println("File filter pathName: " + pathName + ", strippedListenerClassFileName: " + strippedListenerClassFileName);
                if (pathName.getPath().indexOf(strippedListenerClassFileName + '$') != -1)
                    return true;
                return false;
            }
        });
//        for (int i=0; i<files.length; i++)
//            System.out.println("file: " + files[i]);
        return files;
    }

    protected void removeComponentScriptListener(ScriptListenerNode node) {
        if (node.listener != null && node.listener.listener != null) {
            try {
                Method removeListenerMethod = node.eventDescriptor.getRemoveListenerMethod();
                removeListenerMethod.invoke(node.object, new Object[] {node.listener.listener});
            }catch (IllegalAccessException exc) {
                System.out.println("IllegalAccessException while removing listener");
            }catch (IllegalArgumentException exc) {
                System.out.println("IllegalArgumentException while removing listener");
                exc.printStackTrace();
            }catch (java.lang.reflect.InvocationTargetException exc) {
                System.out.println("InvocationTargetException while removing listener");
            }
        }
        removeScript(node);
        container.setMicroworldChanged(true);
    }

    private void updateComponentScriptListeners(ESlateHandle handle,
                                                  ScriptListenerNode node,
                                                  String script,
                                                  EventListener newListener,
                                                  byte[][] newListenerClassBytes,
                                                  InvocationHandler handler,
												  Method addListenerMethod, Method removeListenerMethod) throws InvocationTargetException, IllegalAccessException {
        if (handle != null) {
            String componentName = handle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                return;
            }
        }

        String scriptName = getScriptName();
        if (scriptName == null || scriptName.length() == 0)
            setScriptName(node.getNodeName());

        Class listenerClass = node.eventDescriptor.getListenerType();
        if (currentScriptLanguage == ScriptListener.JAVA)
            listenerClass = newListener.getClass();
        ScriptListener newScriptListener = new ScriptListener("", //componentName,
                                                      node.listener.methodName,
                                                      getScriptName(),
                                                      script,
                                                      newListener,
                                                      listenerClass,
//                                                      node.eventDescriptor.getListenerType(),
                                                      newListenerClassBytes,
                                                      node.listener.getScriptLanguage(), //Logo script
                                                      node.listener.pathToComponent,
                                                      handler);
		newScriptListener.addListenerMethod = addListenerMethod;
		newScriptListener.removeListenerMethod = removeListenerMethod;
		newScriptListener.target = node.object;
//        System.out.println("updateComponentScriptListeners() componentPath:" + node.listener.pathToComponent);


        container.componentScriptListeners.putScriptListener(node.object, newScriptListener, handle);
        if (currentScriptLanguage == ScriptListener.JAVA)
            container.microworld.activateListener(newScriptListener, handle);
        else if (currentScriptLanguage == ScriptListener.LOGO)
            container.microworld.activateLogoHandler(newScriptListener);
        else if (currentScriptLanguage == ScriptListener.JAVASCRIPT)
            container.microworld.activateJSHandler(newScriptListener);
        updateBeanInfoDialog(handle, node.listener.methodName, node, node.listener.getScriptLanguage());
    }

    protected void updateBeanInfoDialog(ESlateHandle handle, String methodName, ScriptListenerNode node, int language) {
        if (container.propertyEventEditor == null)
            return;
        ObjectEventPanel eventEditorPanel = container.propertyEventEditor.eventEditorPanel;
        /* The ScriptListeners of the MicroworldEvents (events of gr.cti.eslate.base.container.Microworld)
         * are attached to the handle of the ESlateContainer (there HierarchyComponentPath starts there).
         * However, when they are displayed under the ESlateMicroworld's handle in the ScriptDialog and
         * when they are first created their handle may point to the ESlateMicroworld. If this is the
         * case make the necessary correction below, so that the buttons of the corresponding
         * EventEditorPanel are properly colored.
         */
        if (handle == container.microworld.eslateMwd.getESlateHandle())
            handle = container.getESlateHandle();

        if (eventEditorPanel.eSlateHandle != handle)
            return;

        /* Check if the ESlate component to which (or to an object of which) the listener
         * was attached/dettached is displayed in the Component Editor.
         */
        String componentName = handle.getComponentName();
        if (eventEditorPanel.eSlateHandle.getComponentName().equals(componentName)) {
            EventPanel2 evtPanel = eventEditorPanel.getEventPanel(methodName);
            ScriptListener scriptListener = node.listener;
            if (evtPanel != null) {
                if (language == ScriptListener.LOGO)
                    evtPanel.setLogoScriptListener(scriptListener);
                else if (language == ScriptListener.JAVASCRIPT)
                    evtPanel.setJSScriptListener(scriptListener);
                else if (language == ScriptListener.JAVA)
                    evtPanel.setJavaScriptListener(scriptListener);
            }else
                System.out.println("Serious inconsistency error in ScriptDialog updateBeanInfoDialog(): (1)");
        }
    }

    /* Selects a second level node other than the supplied one ('node'). If 'disableSave'
     * is true, the user will not be prompted to save the script.
     */
    protected void selectMicroworldNode(boolean disableSave) { //ScriptListenerHandleNode node, boolean disableSave) {
//        ScriptListenerHandleNode root = (ScriptListenerHandleNode) scriptTree.getModel().getRoot();
        ScriptListenerHandleNode[] secLevelNodes = rootScriptListenerNode.getHandleNodes(); // root.getHandleNodes();
        disableSavePrompt = disableSave;
//        if (secLevelNodes.length < 1)
            scriptTree.setSelectionRow(rootNode.getIndex(rootScriptListenerNode));
//        else
//            scriptTree.setSelectionRow(1);
/*        if (secLevelNodes.length < 1)
            scriptTree.setSelectionRow(0);
        else
            scriptTree.setSelectionRow(1);
*/
/*        else{
            ScriptListenerHandleNode nextNode = null;
            for (int i=0; i<secLevelNodes.length; i++) {
                if (secLevelNodes[i] != node) {
                    nextNode = secLevelNodes[i];
                    break;
                }
            }
            TreePath path = new TreePath(new Object[] {root, nextNode});
            scriptTree.addSelectionPath(path);
            scriptTree.revalidate();
        }
*/
        disableSavePrompt = false;
    }

    public boolean selectScriptListener(ScriptListener listener, boolean unAttachedListener) {
//System.out.println("selectScriptListener()");
        if (listener == null) return false;
        TreePath path = container.componentScriptListeners.getPath(listener, false);
//System.out.println("path: " + path);

        if (selectedNode != null) {
            if (selectedNode.getScriptContainer() == listener)
                return false;
            if (!promptToSaveScript()) {
                if (unAttachedListener)
                    container.componentScriptListeners.removeScriptListener(listener);
                return false;
            }
        }

        if (path != null) {
            ScriptListenerNode scriptNode = (ScriptListenerNode) path.getLastPathComponent();
//System.out.println("scriptTree.getSelectionPath(): " + scriptTree.getSelectionPath());
            if (!path.equals(scriptTree.getSelectionPath())) {
                /* Remove the last element of the path, because this element is a
                 * ScriptListenerNode, which is not included in the 'scriptTree'.
                 */
                Object[] objects = path.getPath();
                Object[] tmp = new Object[objects.length-1];
                for (int i=0; i<tmp.length; i++)
                    tmp[i] = objects[i];
                path = new TreePath(tmp);

                updateScriptTree();
                scriptTree.addSelectionPath(path);
            }
            if (!listenerListModel.contains(scriptNode)) {
                listenerListModel.addElement(scriptNode);
                listenerList.validate();
            }
            listenerList.setSelectedValue(scriptNode, true);
//            currentScriptLanguage = scriptNode.listener.getScriptLanguage();
//            scriptArea.setTitle(scriptNode.getNodeName());
        }
        return true;
    }

    /* Causes the 'scriptTree' to reload its data, but it keeps its node expansion status.
     * This method is used when the underlying tree data structure has changed. This way
     * the changes are reflected on the tree.
     */
    protected void updateScriptTree() {
        /* Do not allow the prompt-to-save dialog to appear while the script is
         * updated. Reloading the tree model causes the current selection path to
         * be lost. That's why we store it before reloading the model and then we
         * restore it. This should cause any prompt-to-save dialog to appear.
         */
//        boolean tmp = disableSavePrompt;
//        disableSavePrompt = true;
//System.out.println("updateScriptTree()");
//printTree();
        updatingTree = true;
        TreePath selectionPath = scriptTree.getSelectionPath();
        /* Track the expanded paths of the 'scriptTree', so as to restore them, after
         * the model is reloaded.
         */
        Enumeration expandedPaths = scriptTree.getExpandedDescendants(new TreePath(new Object[] {rootNode})); //scriptTree.getModel().getRoot()}));
        ((DefaultTreeModel) scriptTree.getModel()).reload();
        if (expandedPaths != null) {
            while (expandedPaths.hasMoreElements()) {
                TreePath p = (TreePath) expandedPaths.nextElement();
                scriptTree.expandPath(p);
            }
        }
        if (selectionPath != null)
            scriptTree.setSelectionPath(selectionPath);
//System.out.println("updateScriptTree() selectionPath: " + selectionPath);

        updatingTree = false;
//printTree();
    }

/*    protected void setScriptTree(GlobalScriptTopNode topScriptNode) {
        if (container.microworld != null && topScriptListenerNode != null)
            container.microworld.checkActionPriviledge(container.microworld.componentEventMgmtAllowed, "componentEventMgmtAllowed");

        if (topScriptNode == null) {

        }
    }

    protected void setScriptListenerTree(ScriptListenerHandleNode topScriptListenerNode) {
*/        /* Check the priviledge, only when the ScriptDialog is no cleared */
/*        if (container.microworld != null && topScriptListenerNode != null)
            container.microworld.checkActionPriviledge(container.microworld.componentEventMgmtAllowed, "componentEventMgmtAllowed");

//System.out.println("setScriptListenerTree()");
//printTree();
        DefaultTreeModel treeModel = (DefaultTreeModel) scriptTree.getModel();
        if (topScriptListenerNode == null) {
            scriptTree.getSelectionModel().clearSelection();
            treeModel.reload();
//            ScriptListenerHandleNode root = (ScriptListenerHandleNode) treeModel.getRoot();
            Enumeration children = rootScriptListenerNode.children(); // root.children();
            while (children.hasMoreElements())
                treeModel.removeNodeFromParent((MutableTreeNode) children.nextElement());
//            scriptTree.setRootVisible(false);
//            ((DefaultTreeModel) scriptTree.getModel()).setRoot(null);
        }else{
            if (rootScriptListenerNode != topScriptListenerNode) {
                if (rootScriptListenerNode != null)
                    rootScriptListenerNode.removeFromParent();
                rootScriptListenerNode = topScriptListenerNode;
                rootNode.add(rootScriptListenerNode);
            }
//            scriptTree.setRootVisible(true);
//            ((DefaultTreeModel) scriptTree.getModel()).setRoot(topNode);
        }

//printTree();
    }
*/
/*    protected boolean isScriptTreeVisible() {
        return scriptTree.isRootVisible();
    }
*/
    private DocumentListener getDocumentListener() {
        if (documentListener == null) {
            documentListener = new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    setScriptChanged(true);
    //                statusLabel.setText(scriptDialogBundle.getString("Modified"));
    //                compile.setEnabled(true);
                }
                public void removeUpdate(DocumentEvent e) {
                    setScriptChanged(true);
    //                statusLabel.setText(scriptDialogBundle.getString("Modified"));
    //                compile.setEnabled(true);
                }
                public void changedUpdate(DocumentEvent e) {
                    setScriptChanged(true);
    //                statusLabel.setText(scriptDialogBundle.getString("Modified"));
    //                compile.setEnabled(true);
                }
            };
        }
        return documentListener;
    }

    private CaretListener getCaretListener() {
        if (caretListener == null) {
            caretListener = new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    if (!editorEnabled) return;
    //                try{
    //                    int caretPos = e.getDot();
    //                    int line = scriptArea.getLineOffset(caretPos);
    //                    int horPos = caretPos - scriptArea.getLineStartOffset(line);
    //                    rowColumnLabel.setText("(" + (line+1) + ", " + horPos + ")");
    //                    lineCountLabel.setText(lineCountText + scriptArea.getLineCount());
    //System.out.println("scriptArea.getSelectedText(): " + scriptArea.getSelectedText());
                        if (scriptArea.getSelectedText() == null) {
                            if (cut.isEnabled()) {
                                cut.setEnabled(false);
                                copy.setEnabled(false);
                                run.setEnabled(false);
                            }
                        }else{
                            if (!cut.isEnabled()) {
                                cut.setEnabled(true);
                                copy.setEnabled(true);
                                if (currentScriptLanguage != ScriptListener.JAVA)
                                    run.setEnabled(true);
                                else
                                    run.setEnabled(false);
                                if (!paste.isEnabled())
                                    paste.setEnabled(true);
                            }
                        }
    //                }catch (BadLocationException exc) {}
                }
            };
        }
        return caretListener;
    }

    public void updateColorAndFontSettings(StorageStructure settings, Font mainFont) {
//System.out.println("updateColorAndFontSettings(): " + settings + ", mainFont: " + mainFont);
        disableFontListeners = true;
        if (mainFont != null) {
            String fontName = mainFont.getName();
            int fntSize = mainFont.getSize();
            fontType.setSelectedItem(fontName);
            fontSize.setSelectedItem(new Integer(fntSize).toString());
        }else{
            fontType.setSelectedItem("Monospaced");
            fontSize.setSelectedItem("12");
//            scriptArea.setFontName("Monospaced");
//            scriptArea.setFontSize(12);
            container.scriptDialogMainFont = new Font("Monospaced", Font.PLAIN, 12);
        }
        disableFontListeners = false;
//        if (settings != null)
//            scriptArea.setColorSettings(settings);
    }

    public void bringToFront() {
        // ERROR: here we have to deiconify the Console dialog, if it is iconified
        if (getState() == JFrame.ICONIFIED)
            setState(JFrame.NORMAL);
        toFront();
        scriptArea.requestFocus();
    }

    void populateTree() {
		// Before populating the ScriptDialog with the microworld scripts, we
		// must load the scripts from the ScriptUtils.SCRIPT_DIR_NAME directory
		// of the structfile. This folder contains the textual representations of
		// the microworld scripts, which since version xxx are not stored as
		// part of the microworld state.
//System.out.println("ScriptDialog populateTree() scriptsLoaded: " + ScriptUtils.getInstance().scriptsLoaded + ", container.currentlyOpenMwdFile: " + container.currentlyOpenMwdFile);
		if (!ScriptUtils.getInstance().scriptsLoaded) {
			// If this is not a new microworld, which hasn't been saved yet.
			if (container.currentlyOpenMwdFile != null) {
				ScriptUtils.getInstance().loadMicroworldScripts(container, container.currentlyOpenMwdFile);
			}
		}
        rootScriptNode = container.scriptMap.getScriptTree(scriptDialogBundle);
        rootNode.add(rootScriptNode);
        rootScriptListenerNode = container.componentScriptListeners.getScriptListenerTree();
        rootNode.add(rootScriptListenerNode);
        updateScriptTree();
		scriptTree.setSelectionRow(0);
		newScriptAction.setEnabled(true);
//        printTree();
    }

	void clearTree() {
//		scriptTree.getSelectionModel().clearSelection();
		if (rootScriptListenerNode.getParent() != null) {
			rootNode.remove(rootScriptListenerNode);
		}
		rootNode.remove(rootScriptNode);
		updateScriptTree();
		listenerListModel.clear();
		closeEditor();
		selectedNode = null;
		newScriptAction.setEnabled(false);
	}

/*    public void updateUI() {
        super.updateUI();
        scriptArea.updateUI();
    }
*/

    private void printTree() {
        java.util.Enumeration en = rootNode.breadthFirstEnumeration();
        while (en.hasMoreElements())
            System.out.println(en.nextElement());
    }
}


class ScriptTreeRenderer extends DefaultTreeCellRenderer {
    ImageIcon handleIcon = new ImageIcon(getClass().getResource("images/eslateLogo.gif"));;
    TreeIcon uiIcon = new TreeIcon(this, "UI");
    TreeIcon objectIcon = new TreeIcon(this, "OBJ");

    public ScriptTreeRenderer() {
        setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//System.out.println("value " + value);
        if (ScriptListenerHandleNode.class.isAssignableFrom(value.getClass())) {
            ScriptListenerHandleNode node = (ScriptListenerHandleNode) value;
            setText(node.getNodeName());
            setIcon(handleIcon);
        }else if (ScriptListenerMethodNode.class.isAssignableFrom(value.getClass())) {
            ScriptListenerMethodNode node = (ScriptListenerMethodNode) value;
            setText(node.getNodeName());
            setIcon(objectIcon);
        }else if (ScriptListenerUINode.class.isAssignableFrom(value.getClass())) {
            ScriptListenerUINode node = (ScriptListenerUINode) value;
            setText(node.getNodeName());
            setIcon(uiIcon);
        }else if (GlobalScriptTopNode.class.isAssignableFrom(value.getClass())) {
//System.out.println("Setting text for GlobalScriptTopNode");
            setText((String) ((GlobalScriptTopNode) value).name);
        }
        return this;
    }

}

class ScriptListenerListModel extends DefaultListModel {
    public void addScriptListenerNodes(ScriptListenerNode[] nodes) {
        for (int i=0; i<nodes.length; i++)
            addElement(nodes[i]);
    }

    public void addScriptListenerNode(ScriptListenerNode node) {
        addElement(node);
    }

    public ScriptListenerNode getScriptListenerNode(int index) {
        Object obj = super.get(index);
        if (obj != null)
            return (ScriptListenerNode) obj;
        return null;
    }

    public void removeScriptListenerNode(ScriptListenerNode node) {
        removeElement(node);
    }
}

class ListenerListRenderer extends DefaultListCellRenderer {
    ImageIcon jsIcon = new LanguageIcon(this, "JS", false); //new ImageIcon(getClass().getResource("images/js12x12.gif"));;
    ImageIcon jsIconSelected = new LanguageIcon(this, "JS", true); //new ImageIcon(getClass().getResource("images/js12x12.gif"));;
    ImageIcon jIcon = new LanguageIcon(this, "J", false); //new ImageIcon(getClass().getResource("images/j12x12.gif"));;
    ImageIcon jIconSelected = new LanguageIcon(this, "J", true); //new ImageIcon(getClass().getResource("images/j12x12.gif"));;
    ImageIcon lIcon = new LanguageIcon(this, "L", false); //new ImageIcon(getClass().getResource("images/l12x12.gif"));;
    ImageIcon lIconSelected = new LanguageIcon(this, "L", true); //new ImageIcon(getClass().getResource("images/l12x12selected.gif"));
    Font selectionFont = null, normalFont = null;
    Color selectionColor = new Color(0, 0, 128);
//    LineBorder selectionBorder = new LineBorder(new Color(0, 0, 128));

    public ListenerListRenderer() {
        normalFont = getFont();
        selectionFont = normalFont.deriveFont(Font.ITALIC);
    }

    public java.awt.Component getListCellRendererComponent(JList list,
                                                           Object value,
                                                           int index,
                                                           boolean isSelected,
                                                           boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ScriptListenerNode node = (ScriptListenerNode) value;
        if (node.listener.getScriptLanguage() == ScriptListener.LOGO) {
            if (isSelected)
                setIcon(lIconSelected);
            else
                setIcon(lIcon);
        }else if (node.listener.getScriptLanguage() == ScriptListener.JAVASCRIPT) {
            if (isSelected)
                setIcon(jsIconSelected);
            else
                setIcon(jsIcon);
        }else{
            if (isSelected)
                setIcon(jIconSelected);
            else
                setIcon(jIcon);
        }
        setText(node.nodeName);
        setBackground(list.getBackground());
        if (isSelected) {
            setForeground(selectionColor);
//            setFont(selectionFont);
        }else{
//          setFont(normalFont);
            setForeground(list.getForeground());
        }
        return this;
    }
}

/* Used to create the 'J', 'L' and 'JS' icons and their selected variations for the
 * listener list.
 */
class LanguageIcon extends ImageIcon {
    int height = 15;
    int width = 15;
    int strWidth = 0, strHeight = 0;
    String lang = "";
    boolean selected = false;
    Font font = new Font("Times New Roman", Font.BOLD, 12);
    static final Color selectionColor = new Color(0, 0, 128); //255, 74, 49);
    RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);;

    public LanguageIcon(Component comp, String lang, boolean selected) {
        this.lang = lang;
        this.selected = selected;
        if (lang.length() == 2) {
            font = font.deriveFont((float)10);
        }
        FontMetrics fm = comp.getFontMetrics(font);
        strWidth = fm.stringWidth(lang);
        strHeight = fm.getHeight()-2; //-2 is needed only for 'Times New Roman' font
        hints.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D gr = (Graphics2D) g;
        gr.addRenderingHints(hints);
        if (selected) {
            gr.setColor(selectionColor);
            gr.fillOval(x, y, width, height);
        }

        int startX = x + width/2 - strWidth/2;
        int startY = y + height/2 + strHeight/4;
        if (selected)
            gr.setColor(Color.white);
        else
            gr.setColor(selectionColor);
        Font oldFont = gr.getFont();
        gr.setFont(font);
        gr.drawString(lang, startX, startY+2); //+2 is needed only for 'Times New Roman' font
        gr.setFont(oldFont);
    }
}

/* Used to create the 'UI' and 'OBJ' icons used for the ScriptListenerUINodes and
 * ScriptListenerMethodNodes of the script listener tree, respectively.
 */
class TreeIcon extends ImageIcon {
    int height = 15;
    int width = 18;
    int strWidth = 0, strHeight = 0;
    String lang = "";
    Font font = new Font("Times New Roman", Font.BOLD, 11);
    static final Color color = new Color(0, 0, 128);

    public TreeIcon(Component comp, String lang) {
        this.lang = lang;
        FontMetrics fm = comp.getFontMetrics(font);
        strWidth = fm.stringWidth(lang);
        if (strWidth > width)
            width = strWidth+2;
        strHeight = fm.getHeight()-2; //-2 is needed only for 'Times New Roman' font
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D gr = (Graphics2D) g;

        int startX = x + width/2 - strWidth/2;
        int startY = y + height/2 + strHeight/4;
        gr.setColor(color);
        Font oldFont = gr.getFont();
        gr.setFont(font);
        gr.drawString(lang, startX, startY+2); //+2 is needed only for 'Times New Roman' font
        gr.setFont(oldFont);
    }
}

/* This class takes as input the ScriptListenerMap of the microworld and creates
 * a tree which contains all these handles, for which actions (event scripts) have
 * been defined. The top level is the microworld. The nodes of the tree are handles.
 * The leafs of the tree are either handles or Java objects (visible or not) for
 * which actions have been defined.
 */
class ScriptTree {
    ScriptListenerMap scriptMap;

    public ScriptTree(ScriptListenerMap scriptMap) {
        this.scriptMap = scriptMap;
    }
}

class NewScriptAction extends AbstractAction {
	ScriptDialog scriptDialog = null;

	public NewScriptAction(ScriptDialog dialog, String title) {
		super(title, new ImageIcon(ScriptDialog.class.getResource("images/new20x19.gif")));
		this.scriptDialog = dialog;
		putValue(AbstractAction.ACCELERATOR_KEY,
				 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
										java.awt.Event.CTRL_MASK,
										false)
		);
	}
	public void actionPerformed(ActionEvent e) {
		String[] packageAndName = showNewClassDialog();
		String[] packageName = scriptDialog.breakPackageNameIntoParts(packageAndName[0]);
		String className = packageAndName[1].trim();
		String classType = packageAndName[2];
		String fullClassName = packageAndName[0].trim() + '.' + className;
		if (className == null || className.length() == 0 || className.indexOf(' ') != -1) return;
//System.out.println("className: " + className);
		if (scriptDialog.container.scriptMap.containsScript(fullClassName)) {
			ESlateOptionPane.showMessageDialog(scriptDialog,
					scriptDialog.scriptDialogBundle.getString("ClassNameExists") + fullClassName + "\"",
					scriptDialog.scriptDialogBundle.getString("Error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int type = Script.PLAIN_CLASS;
		if (classType.equals(scriptDialog.scriptDialogBundle.getString("Singleton Class"))) type = Script.SINGLETON;

		Script s = new Script(packageName, className, type); // Script.SINGLETON);
		scriptDialog.container.scriptMap.addScript(s);
		ScriptNode sn = scriptDialog.container.scriptMap.getScriptNode(s);
		scriptDialog.updateScriptTree();
		TreePath scriptNodePath = new TreePath(new Object[] {scriptDialog.rootNode, scriptDialog.rootScriptNode, sn});
		scriptDialog.scriptTree.expandPath(scriptNodePath);
		scriptDialog.scriptTree.setSelectionPath(scriptNodePath);
		scriptDialog.setNewScript(true);
	}

	public String[] showNewClassDialog() {
		JTextField packageField = new JTextField();
		JTextField classField = new JTextField();

		Object[]      message = new Object[9];
		message[0] = scriptDialog.scriptDialogBundle.getString("Class Package");
		message[1] = packageField;
		message[2] = scriptDialog.scriptDialogBundle.getString("Class Name");
		message[3] = classField;

		message[4] = scriptDialog.scriptDialogBundle.getString("Class Type");
		JComboBox cb = new JComboBox();
		cb.addItem(scriptDialog.scriptDialogBundle.getString("Plain Class"));
		cb.addItem(scriptDialog.scriptDialogBundle.getString("Singleton Class"));
		message[5] = cb;
		JLabel lb1 = new JLabel(scriptDialog.scriptDialogBundle.getString("Notes1"));
		Font f = lb1.getFont();
		f = f.deriveFont(f.getSize2D()-2);
		lb1.setFont(f);
		JLabel lb2 = new JLabel(scriptDialog.scriptDialogBundle.getString("Notes2"));
		lb2.setFont(f);
		message[6] = lb1;
		message[7] = lb2;
		message[8] = new Box(BoxLayout.Y_AXIS);

		// Options
		String[] options = {
			scriptDialog.scriptDialogBundle.getString("OK"),
			scriptDialog.scriptDialogBundle.getString("Cancel"),
		};
		int result = JOptionPane.showOptionDialog(
			scriptDialog,                             // the parent that the dialog blocks
			message, // the dialog message array
			scriptDialog.scriptDialogBundle.getString("NewScriptTitle"), // the title of the dialog window
			JOptionPane.DEFAULT_OPTION,                 // option type
			JOptionPane.INFORMATION_MESSAGE,            // message type
			new ImageIcon(),                                       // optional icon, use null to use the default icon
			options,                                    // options string array, will be made into buttons
			options[0]                                  // option that should be made into a default button
		);
		switch(result) {
		   case 0: // yes
			   return new String[] {packageField.getText(), classField.getText(), (String) cb.getSelectedItem()};
		   case 1: // cancel
			   return null;
		   default:
			   return null;
		}

	}
}

class CompileAction extends AbstractAction {
	ScriptDialog scriptDialog = null;

	public CompileAction(ScriptDialog dialog, String title) {
		super(title, new ImageIcon(ScriptDialog.class.getResource("images/compile.gif")));
		this.scriptDialog = dialog;
		putValue(AbstractAction.ACCELERATOR_KEY,
				 KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9,
										0,
										false)
		);
	}

	public void actionPerformed(ActionEvent e) {
		if (scriptDialog.isNodeScriptListenerNode(scriptDialog.selectedNode)) {
			scriptDialog.attachListener((ScriptListenerNode) scriptDialog.selectedNode);
		}else{
			scriptDialog.compileClass((ScriptNode) scriptDialog.selectedNode);
		}
	}

}
