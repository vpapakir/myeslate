package gr.cti.eslate.base.container;

import gr.cti.eslate.base.ESlateHandle;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.EventSetDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import com.objectspace.jgl.Array;


class LogoScriptDialog extends JFrame {
    Locale locale;
    ResourceBundle logoScriptDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    protected Font greekUIBoldFont = new Font("Helvetica", Font.BOLD, 12);
    private boolean localeIsGreek = false;
    private final static int COMPONENT_AREA_WIDTH = 150;
    JScrollPane scrollPane;
    JTextField scriptNameTf;
    JTextArea scriptArea;
    JLabel rowColumnLabel, lineCountLabel, modifiedLabel;
    NoBorderButton copy, cut, paste, compile;
    ImageIcon cutDisabledIcon = new ImageIcon(getClass().getResource("images/cutDisabled.gif"));
    ImageIcon copyDisabledIcon = new ImageIcon(getClass().getResource("images/copyDisabled.gif"));
    ImageIcon pasteDisabledIcon = new ImageIcon(getClass().getResource("images/pasteDisabled.gif"));
    String lineCountText;
    /* Before adding a new Listener to a component, we have to remove the listener that
     * has possibly been added before. e.g. when the user alters the script for an event,
     * he practically defines a new listener. The previous listener, which carried the old
     * version of the script has to be removed. The old listener is contained in the
     * variable "currentListener".
     */
    java.util.EventListener currentListener = null;
    ScriptListener scriptListener;
    EventSetDescriptor eventDescriptor;
    ESlateHandle eSlateHandle = null;
    HierarchicalComponentPath2 componentPath = null;
    Method method;
    Class listenerClass = null;
    byte[] listenerClassBytes = null;
    String script;
    String scriptName = null;
    LogoScriptHandler logoHandler = null;
    String noScriptName;
    Array getterMethods;
    ESlateComposer composer;
    private String initialComponentName = null;
    ESlateFileDialog fileDialog;
    String token = null; // The last token searched in the console
    String line = null; // The line to go to
    /* The object to which the listener is attached to.
     */
    Object object;


    public LogoScriptDialog(ESlateComposer cont, Object obj, ESlateHandle handle,
                            HierarchicalComponentPath2 path, EventSetDescriptor esd, Method meth,
                            String[] variables, ScriptListener scrListener, Array getMethods) {
        super();
        setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        object = obj;
        scriptListener = scrListener;
        eventDescriptor = esd;
        if (scrListener != null)
            scriptName = scrListener.getScriptName();
        getterMethods = getMethods;
        method = meth;
        composer = cont;
        eSlateHandle = handle; //composer.componentScriptListeners.getContainingObjectHandle(object);
        if (path == null)
            throw new NullPointerException("Null component path!");
        componentPath = path;
        initialComponentName = eSlateHandle.getComponentName();
        if (scrListener != null)
            script = scrListener.getScript();
        if (scriptListener != null)
            currentListener = (java.util.EventListener) scriptListener.listener;

System.out.println("LogoScriptDialog was created for object: " + obj + " with handle: "+ handle);
System.out.println("componentPath: " + path);
        if (method == null)
            throw new NullPointerException("No method was given");

        locale = Locale.getDefault();
        logoScriptDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LogoScriptDialogBundle", locale);
        if (logoScriptDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.LogoScriptDialogBundle_el_GR"))
            localeIsGreek = true;

        noScriptName = logoScriptDialogBundle.getString("NoScript");

        setTitle(logoScriptDialogBundle.getString("DialogTitle1") + method.getName() + "\"" + logoScriptDialogBundle.getString("DialogTitle2") + eSlateHandle.getComponentName() + "\"");
        lineCountText = logoScriptDialogBundle.getString("LineCount");

        // The toolbar
        // Create the menu bar
        Dimension buttonSize = new Dimension(22, 21);
        Insets zeroInsets = new Insets(0, 0, 0, 0);

        NoBorderButton load = new NoBorderButton(new ImageIcon(getClass().getResource("images/openFile20x19.gif")));
        load.setMargin(zeroInsets);
        load.setToolTipText(logoScriptDialogBundle.getString("LoadTip"));
        load.setRequestFocusEnabled(false);

        NoBorderButton save = new NoBorderButton(new ImageIcon(getClass().getResource("images/save.gif")));
        save.setMargin(zeroInsets);
        save.setToolTipText(logoScriptDialogBundle.getString("SaveTip"));
        save.setRequestFocusEnabled(false);

        cut = new NoBorderButton(new ImageIcon(getClass().getResource("images/cut.gif")));
      	cut.setDisabledIcon(cutDisabledIcon);
        cut.setMargin(zeroInsets);
        cut.setToolTipText(logoScriptDialogBundle.getString("CutTip"));
        cut.setRequestFocusEnabled(false);

        copy = new NoBorderButton(new ImageIcon(getClass().getResource("images/copy.gif")));
      	copy.setDisabledIcon(copyDisabledIcon);
        copy.setMargin(zeroInsets);
        copy.setToolTipText(logoScriptDialogBundle.getString("CopyTip"));
        copy.setRequestFocusEnabled(false);

        paste = new NoBorderButton(new ImageIcon(getClass().getResource("images/paste.gif")));
      	paste.setDisabledIcon(pasteDisabledIcon);
        paste.setMargin(zeroInsets);
        paste.setToolTipText(logoScriptDialogBundle.getString("PasteTip"));
        paste.setRequestFocusEnabled(false);

        NoBorderButton find = new NoBorderButton(new ImageIcon(getClass().getResource("images/find.gif")));
        find.setMargin(zeroInsets);
        find.setToolTipText(logoScriptDialogBundle.getString("FindTip"));
        find.setRequestFocusEnabled(false);

        NoBorderButton findNext = new NoBorderButton(new ImageIcon(getClass().getResource("images/findNext2.gif")));
        findNext.setMargin(zeroInsets);
        findNext.setToolTipText(logoScriptDialogBundle.getString("FindNextTip"));
        findNext.setRequestFocusEnabled(false);

        NoBorderButton findPrev = new NoBorderButton(new ImageIcon(getClass().getResource("images/findPrev2.gif")));
        findPrev.setMargin(zeroInsets);
        findPrev.setToolTipText(logoScriptDialogBundle.getString("FindPrevTip"));
        findPrev.setRequestFocusEnabled(false);

        NoBorderButton goToLine = new NoBorderButton(new ImageIcon(getClass().getResource("images/goToLine.gif")));
        goToLine.setMargin(zeroInsets);
        goToLine.setToolTipText(logoScriptDialogBundle.getString("GoToLineTip"));
        goToLine.setRequestFocusEnabled(false);

        compile = new NoBorderButton(new ImageIcon(getClass().getResource("images/compile.gif")));
        compile.setMargin(zeroInsets);
        compile.setToolTipText(logoScriptDialogBundle.getString("CompileTip"));
        compile.setRequestFocusEnabled(false);

        NoBorderButton clear = new NoBorderButton(new ImageIcon(getClass().getResource("images/clear.gif")));
        clear.setMargin(zeroInsets);
        clear.setToolTipText(logoScriptDialogBundle.getString("ClearTip"));
        clear.setRequestFocusEnabled(false);

        cut.setEnabled(false);
        copy.setEnabled(false);
//	      if (getToolkit().getSystemClipboard().getContents(this) == null)
//            paste.setEnabled(false);
//        else
        paste.setEnabled(true);

        javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
    	  toolBar.setFloatable(false);

        toolBar.add(Box.createHorizontalStrut(2));
        toolBar.add(load);
        toolBar.add(save);
        toolBar.addSeparator();
        toolBar.add(cut);
        toolBar.add(copy);
        toolBar.add(paste);
        toolBar.addSeparator();
        toolBar.add(find);
        toolBar.add(findPrev);
        toolBar.add(findNext);
        toolBar.add(goToLine);
        toolBar.addSeparator();
        toolBar.add(compile);
        toolBar.add(clear);
        toolBar.add(Box.createGlue());

        JPanel barPanel = new JPanel(true);
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.Y_AXIS));
        barPanel.add(Box.createVerticalStrut(1));
        barPanel.add(toolBar);
        barPanel.add(Box.createVerticalStrut(1));

        JLabel scriptNameLb = new JLabel(logoScriptDialogBundle.getString("ScriptName"));
        scriptNameTf = new JTextField();

        JLabel varLb1 = new JLabel(logoScriptDialogBundle.getString("AvailableVariables") + ":  ");
        varLb1.setFont(varLb1.getFont().deriveFont(Font.BOLD));
//        if (localeIsGreek)
//            varLb1.setFont(greekUIBoldFont);
//        else
//            varLb1.setFont(new Font(varLb1.getFont().getFamily(), Font.BOLD, varLb1.getFont().getSize()));

        String availVarLbText = "";
        if (variables != null && variables.length != 0) {
            for (int i=0; i<variables.length; i++)
                availVarLbText = availVarLbText + variables[i] + ", ";
            if (availVarLbText.length() > 2)
                availVarLbText = availVarLbText.substring(0, availVarLbText.length()-2);
            availVarLbText = availVarLbText + ", source";
        }else
            availVarLbText = "source";

        JLabel varLb= new JLabel(availVarLbText);

        scriptArea = new JTextArea();
        scrollPane = new JScrollPane(scriptArea);
        scrollPane.setMinimumSize(new Dimension(200, 300));
        scrollPane.setPreferredSize(new Dimension(400, 300));

/*
        if (localeIsGreek) {
            scriptNameLb.setFont(greekUIBoldFont);
            scriptNameTf.setFont(greekUIFont);
            varLb.setFont(greekUIFont);
            scriptArea.setFont(greekUIFont);
        }else
            scriptNameLb.setFont(new Font(scriptNameLb.getFont().getFamily(), Font.BOLD, scriptNameLb.getFont().getSize()));
*/

        JPanel scriptNamePanel = new JPanel(true);
        scriptNamePanel.setLayout(new BoxLayout(scriptNamePanel, BoxLayout.X_AXIS));
        scriptNamePanel.add(scriptNameLb);
        scriptNamePanel.add(Box.createHorizontalStrut(5));
        scriptNamePanel.add(scriptNameTf);
        scriptNamePanel.add(Box.createGlue());

        JPanel varPanel = new JPanel(true);
        varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.X_AXIS));
        varPanel.add(varLb1);
        varPanel.add(varLb);
        varPanel.add(Box.createGlue());

        JPanel intermediatePanel = new JPanel(true);
        intermediatePanel.setLayout(new BoxLayout(intermediatePanel, BoxLayout.Y_AXIS));
        intermediatePanel.add(scriptNamePanel);
        intermediatePanel.add(Box.createVerticalStrut(3));
        intermediatePanel.add(varPanel);
        intermediatePanel.setBorder(new javax.swing.border.CompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            new javax.swing.border.EmptyBorder(2,2,2,2)
            ));


        JPanel topPanel = new JPanel(true);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(barPanel);
        topPanel.add(Box.createVerticalStrut(3));
        topPanel.add(intermediatePanel);

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
        FontMetrics fm = lineCountLabel.getFontMetrics(lineCountLabel.getFont());
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

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout());

        modifiedLabel = new JLabel() {
            public Dimension getPreferredSize() {
                Dimension prefSize = super.getPreferredSize();
                prefSize.height = 20;
                return prefSize;
            }
        };
        modifiedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modifiedLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(modifiedLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(new EmptyBorder(0, 1, 0, 1));

        getContentPane().add(mainPanel);

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
                String tok = (String) ESlateOptionPane.showInputDialog(LogoScriptDialog.this,
                                 logoScriptDialogBundle.getString("FindWhat"),
                                 logoScriptDialogBundle.getString("Find"),
                                 JOptionPane.QUESTION_MESSAGE,
                                 null,
                                 null,
                                 token);

                if (tok != null) {
                    token = tok;
                    find(token, 0, true);
                }
            }
        });

        findNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (token == null)
                    return;
                find(token, scriptArea.getCaretPosition(), true);
            }
        });

        findPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (token == null)
                    return;
                int selectionStart = scriptArea.getSelectionStart();
                int selectionEnd = scriptArea.getSelectionEnd();
                if (selectionStart != selectionEnd) {// There is smth selected
                    String selectedText = scriptArea.getSelectedText();
                    if (selectedText.equals(token))
                        scriptArea.setSelectionEnd(selectionStart);
                }
                find(token, scriptArea.getCaretPosition(), false);
            }
        });

        goToLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String l = (String) ESlateOptionPane.showInputDialog(LogoScriptDialog.this,
                            logoScriptDialogBundle.getString("GoToLine"),
                            logoScriptDialogBundle.getString("GoTo"),
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            line);

            		if(l != null) {
                    line = l;
                    goToLine(line);
                }
            }
        });

        compile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attachListener2();
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (eSlateHandle != null) {
                    /* Check if the component still exists in the microworld. If not, warn the user
                     * and don't do anything. 'attachListener2()' does check for the same thing, but
                     * it prints a different message.
                     */
                    String componentName = eSlateHandle.getComponentName();
                    if (componentName == null) {
                        /* A null componentName implies that the component no longer exists, i.e. it
                         * has been removed from the microworld.
                         */
                        ESlateOptionPane.showMessageDialog(LogoScriptDialog.this, logoScriptDialogBundle.getString("CompileDenialMsg1") + initialComponentName + logoScriptDialogBundle.getString("CompileDenialMsg3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                setScript("");
                setScriptName(noScriptName);
                attachListener2();
            }
        });

        //Initialize the dialog
        scriptNameTf.setText(scriptName);
        scriptArea.setText(script);

        scriptArea.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                try{
                    int caretPos = e.getDot();
                    int line = scriptArea.getLineOfOffset(caretPos);
                    int horPos = caretPos - scriptArea.getLineStartOffset(line);
                    rowColumnLabel.setText("(" + (line+1) + ", " + horPos + ")");
                    lineCountLabel.setText(lineCountText + scriptArea.getLineCount());
                    if (scriptArea.getSelectedText() == null) {
                        if (cut.isEnabled()) {
                            cut.setEnabled(false);
                            copy.setEnabled(false);
                        }
                    }else{
                        if (!cut.isEnabled()) {
                            cut.setEnabled(true);
                            copy.setEnabled(true);
                            if (!paste.isEnabled())
                                paste.setEnabled(true);
                        }
                    }
                }catch (BadLocationException exc) {}
            }
        });

        scriptArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                modifiedLabel.setText(logoScriptDialogBundle.getString("Modified"));
                compile.setEnabled(true);
            }
            public void removeUpdate(DocumentEvent e) {
                modifiedLabel.setText(logoScriptDialogBundle.getString("Modified"));
                compile.setEnabled(true);
            }
            public void changedUpdate(DocumentEvent e) {
                modifiedLabel.setText(logoScriptDialogBundle.getString("Modified"));
                compile.setEnabled(true);
            }
        });

        //Initialization
        compile.setEnabled(false);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() != WindowEvent.WINDOW_CLOSING)
            super.processWindowEvent(e);
        else{
            String scriptListenerScript = (scriptListener == null)? null:scriptListener.getScript();
            if (scriptListenerScript != null && scriptListenerScript.trim().length() == 0)
                scriptListenerScript = null;
            String scriptAreaContents = scriptArea.getText();
            if (scriptAreaContents != null && scriptAreaContents.trim().length() == 0)
                scriptAreaContents = null;
//            System.out.println("scriptListenerScript: " + scriptListenerScript);

//            System.out.println("scriptAreaContents: " + scriptAreaContents);

            if ((scriptListenerScript != null && scriptAreaContents != null && !scriptListenerScript.equals(scriptAreaContents))
              || (scriptListenerScript == null && scriptAreaContents != null)
              || (scriptListenerScript != null && scriptAreaContents == null)) {
                // Warn the user about lost changes
                Object[] yes_no_cancel = {logoScriptDialogBundle.getString("Yes"), logoScriptDialogBundle.getString("No"), logoScriptDialogBundle.getString("Cancel")};

                JOptionPane pane = new JOptionPane(logoScriptDialogBundle.getString("ScriptChanged"),
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.UIManager.getIcon("OptionPane.questionIcon"),
                    yes_no_cancel,
                    logoScriptDialogBundle.getString("Yes"));
                javax.swing.JDialog dialog = pane.createDialog(LogoScriptDialog.this, logoScriptDialogBundle.getString("SaveChanges"));
                dialog.setVisible(true);
                Object option = pane.getValue();

//                System.out.println("option: " + option + ", getClass(): " + option.getClass());
                if (option == logoScriptDialogBundle.getString("Cancel") || option == null || option.toString().equals("-1"))
                    return;
                if (option == logoScriptDialogBundle.getString("Yes"))
                    attachListener2();
            }
//            container.listenerDialogList.removeListener(LogoScriptDialog.this);
            dispose();
        }

        // ESCAPE
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new java.awt.event.WindowEvent(LogoScriptDialog.this, java.awt.event.WindowEvent.WINDOW_CLOSING));
//                dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
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
        setVisible(true);
    }

    public String getScript() {
        return scriptArea.getText();
    }

    public String getScriptName() {
        return scriptNameTf.getText();
    }

    public void setScript(String script) {
        scriptArea.setText(script);
    }

    public void setScriptName(String scriptName) {
        scriptNameTf.setText(scriptName);
    }

    protected void attachListener2() {
        if (eSlateHandle != null) {
            String componentName = eSlateHandle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                ESlateOptionPane.showMessageDialog(LogoScriptDialog.this, logoScriptDialogBundle.getString("CompileDenialMsg1") + initialComponentName + logoScriptDialogBundle.getString("CompileDenialMsg2"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // if the new script is null or empty then remove the current listener, if any exists.
        if (getScript() == null || getScript().trim().length() == 0) {
            if (currentListener != null) {
                try {
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    removeListenerMethod.invoke(object, new Object[] {currentListener});
                }catch (IllegalAccessException exc) {
                    System.out.println("IllegalAccessException while adding listener");
                }catch (IllegalArgumentException exc) {
                    System.out.println("IllegalArgumentException while adding listener");
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    System.out.println("InvocationTargetException while adding listener");
                }
            }
            script = null;
            listenerClass = null;
            currentListener = null;
            logoHandler = null;
            removeComponentScriptListener(scriptListener);
//            updateEventDialog();
            updateBeanInfoDialog();
            setCursor(Cursor.getDefaultCursor());
            modifiedLabel.setText("");
            setCursor(Cursor.getDefaultCursor());
            compile.setEnabled(false);
            return;
        }

        // if the script hasn't been changed, return.
        if (script != null && getScript() != null && script.equals(getScript())) {
            if (scriptName != getScriptName()) {
                scriptName = getScriptName();
            }
            setCursor(Cursor.getDefaultCursor());
            modifiedLabel.setText("");
            compile.setEnabled(false);
            return;
        }

        script = getScript();
        scriptName = getScriptName();

        logoHandler = new LogoScriptHandler(method.getName(), eventDescriptor.getListenerType());
        logoHandler.setScript(script);
        Object listener = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                          new Class[] { eventDescriptor.getListenerType() },
                                          logoHandler);

        /* If the logo runtime hasn't already been started, initialize it now.
         */
        if (composer.getLogoMachine() == null) {
            composer.initLogoEnvironment();
            composer.startWatchingMicroworldForPrimitiveGroups();
        }
        logoHandler.setLogoRuntime(eSlateHandle,
                                   composer.getLogoMachine(),
                                   composer.getLogoEnvironment(),
                                   composer.getLogoThread(),
                                   composer.getTokenizer());

        // Add the instance of the listener class to the component.
        Method addListenerMethod = eventDescriptor.getAddListenerMethod();
        try{
            if (currentListener != null) {
                // Remove the installed listener
                Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                removeListenerMethod.invoke(object, new Object[] {currentListener});
            }
            addListenerMethod.invoke(object, new Object[] {listener});
            currentListener = (java.util.EventListener) listener;
            updateComponentScriptListeners();
//                updateEventDialog();
            updateBeanInfoDialog();
            modifiedLabel.setText("");
            setCursor(Cursor.getDefaultCursor());
            compile.setEnabled(false);
        }catch (IllegalAccessException exc) {
            setCursor(Cursor.getDefaultCursor());
            System.out.println("IllegalAccessException while adding listener");
            ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }catch (IllegalArgumentException exc) {
            setCursor(Cursor.getDefaultCursor());
            System.out.println("IllegalArgumentException while adding listener");
            ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }catch (java.lang.reflect.InvocationTargetException exc) {
            setCursor(Cursor.getDefaultCursor());
            exc.printStackTrace();
            System.out.println("InvocationTargetException while adding listener");
            ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            script = null;
        }
    }

    protected void attachListener() {
        if (eSlateHandle != null) {
            String componentName = eSlateHandle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                ESlateOptionPane.showMessageDialog(LogoScriptDialog.this, logoScriptDialogBundle.getString("CompileDenialMsg1") + initialComponentName + logoScriptDialogBundle.getString("CompileDenialMsg2"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // if the new script is null or empty then remove the current listener, if any exists.
        if (getScript() == null || getScript().trim().length() == 0) {
            if (currentListener != null) {
                try {
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    removeListenerMethod.invoke(object, new Object[] {currentListener});
                }catch (IllegalAccessException exc) {
                    System.out.println("IllegalAccessException while adding listener");
                }catch (IllegalArgumentException exc) {
                    System.out.println("IllegalArgumentException while adding listener");
                }catch (java.lang.reflect.InvocationTargetException exc) {
                    System.out.println("InvocationTargetException while adding listener");
                }
            }
            script = null;
            listenerClass = null;
            currentListener = null;
            removeComponentScriptListener(scriptListener);
//            updateEventDialog();
            updateBeanInfoDialog();
            setCursor(Cursor.getDefaultCursor());
            modifiedLabel.setText("");
            return;
        }

        // if the script hasn't been changed, return.
        if (script != null && getScript() != null && script.equals(getScript())) {
            if (scriptName != getScriptName()) {
                scriptName = getScriptName();
            }
            setCursor(Cursor.getDefaultCursor());
            modifiedLabel.setText("");
            return;
        }

        script = getScript();
        scriptName = getScriptName();

        String formulatedScript = ESlateComposerUtils.formulateScript(script);

        // Now create the listener's java file
        String listenerFileName = composer.composerUtils.createLogoScriptListener(object, eventDescriptor, method, getterMethods, formulatedScript);

        // Compile the listener's java file
        if (listenerFileName != null) {
            String listenerClassFileName = ESlateComposerUtils.compileJavaFile(
                                                  this,
                                                  listenerFileName,
                                                  scriptName,
                                                  logoScriptDialogBundle);
//                                                  this);
//            String listenerClassFileName = listenerFileName.substring(0, listenerFileName.lastIndexOf(".")) + ".class";
//System.out.println("listenerClassFileName: " + listenerClassFileName);
//            if (composer.containerUtils.existsInTmpDir(listenerFileName))
//                deleteFile(listenerFileName);
            if (listenerClassFileName == null) {
                script = null;
                setCursor(Cursor.getDefaultCursor());
                return;
            }

            SimpleClassLoader loader = SimpleClassLoader.listenerLoader;

            // Load the produced listener class
            try{
                listenerClassBytes = loader.getByteArray(listenerClassFileName);
                deleteFile(listenerClassFileName);
                listenerClass = loader.loadClassFromByteArray(listenerClassBytes);
            }catch (ClassNotFoundException exc) {
                System.out.println("ClassNotFoundException while loading listener class");
                setCursor(Cursor.getDefaultCursor());
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
                return;
            }catch (java.io.IOException exc) {
                System.out.println("IOException while loading listener class from file " + listenerClassFileName);
                exc.printStackTrace();
                setCursor(Cursor.getDefaultCursor());
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
                return;
            }

            //Make an instance of the listener class
            Object hookup = null;
            try{
                hookup = listenerClass.newInstance();
                /* Set the Logo runtime environment of the listener instance. We must call the
                 * setLogoRuntime method. To do it we get the methods fo the listener's class,
                 * secarch for the method with name "setLogoRuntime" and the invoke it"
                 */
                Method[] listenerMethods = listenerClass.getMethods();
                Method logoRuntimeSetterMethod = null;
                for (int i=0; i<listenerMethods.length; i++) {
                    if (listenerMethods[i].getName().equals("setLogoRuntime")) {
                        logoRuntimeSetterMethod = listenerMethods[i];
                        break;
                    }
                }
                /* If the logo runtime hasn't already been started, initialize it now.
                 */
                if (composer.getLogoMachine() == null) {
                    composer.initLogoEnvironment();
                    composer.startWatchingMicroworldForPrimitiveGroups();
                }

                logoRuntimeSetterMethod.invoke(hookup,
                          new Object[] {
                              eSlateHandle,
                              composer.getLogoMachine(),
                              composer.getLogoEnvironment(),
                              composer.getLogoThread(),
                              composer.getTokenizer()}
                );
            }catch (InstantiationException exc) {
                System.out.println("InstantiationException while instantiating listener object");
                setCursor(Cursor.getDefaultCursor());
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
                return;
            }catch (IllegalAccessException exc) {
                System.out.println("IllegalAccessException while instantiating listener object");
                setCursor(Cursor.getDefaultCursor());
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
                return;
            }catch (java.lang.reflect.InvocationTargetException exc) {
                System.out.println("InvocationTargetException while invoking \"setLogoRuntime\" of listener object");
                setCursor(Cursor.getDefaultCursor());
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
                return;
            }

            // Add the instance of the listener class to the component.
            Method addListenerMethod = eventDescriptor.getAddListenerMethod();
            try{
                if (currentListener != null) {
                    // Remove the installed listener
                    Method removeListenerMethod = eventDescriptor.getRemoveListenerMethod();
                    removeListenerMethod.invoke(object, new Object[] {currentListener});
                }
                addListenerMethod.invoke(object, new Object[] {hookup});
                currentListener = (java.util.EventListener) hookup;
                updateComponentScriptListeners();
//                updateEventDialog();
                updateBeanInfoDialog();
                modifiedLabel.setText("");
            }catch (IllegalAccessException exc) {
                System.out.println("IllegalAccessException while adding listener");
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
            }catch (IllegalArgumentException exc) {
                System.out.println("IllegalArgumentException while adding listener");
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
            }catch (java.lang.reflect.InvocationTargetException exc) {
                System.out.println("InvocationTargetException while adding listener");
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("CompileFailureMessage3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                script = null;
            }
        }

        setCursor(Cursor.getDefaultCursor());
    }

    protected void updateComponentScriptListeners() {
        if (eSlateHandle != null) {
            String componentName = eSlateHandle.getComponentName();
            if (componentName == null) {
                /* A null componentName implies that the component no longer exists, i.e. it
                 * has been removed from the microworld.
                 */
                return;
            }
        }
/*        ScriptListener[] componentScriptListeners = (ScriptListener[]) container.componentScriptListeners.getScriptListeners(object);
        if (componentScriptListeners == null)
            componentScriptListeners = new ScriptListener[0];
        int listenerCount = 0;
        if (componentScriptListeners != null)
            listenerCount = componentScriptListeners.length;
        if (scriptListener == null) //A new listener was defined and not an old one redefined
            listenerCount++;
//        System.out.println("listenerCount: " + listenerCount);
*/

        String scriptName = getScriptName();
        if (scriptName == null || scriptName.length() == 0)
            setScriptName(method.getName());

        ScriptListener newScriptListener = new ScriptListener("", //componentName,
                                                      method.getName(),
                                                      getScriptName(),
                                                      script,
                                                      currentListener,
                                                      eventDescriptor.getListenerType(),
                                                      new byte[][] {listenerClassBytes},
                                                      ScriptListener.LOGO, //Logo script
                                                      componentPath,
                                                      logoHandler);
        System.out.println("updateComponentScriptListeners() componentPath:" + componentPath);
        composer.getScriptListenerMap().putScriptListener(object, newScriptListener, eSlateHandle);
/*        ScriptListener[] updatedComponentScriptListeners = new ScriptListener[listenerCount];
        if (scriptListener == null) {
            for (int i=0; i<componentScriptListeners.length; i++)
                updatedComponentScriptListeners[i] = componentScriptListeners[i];
            updatedComponentScriptListeners[updatedComponentScriptListeners.length-1] = newScriptListener;
        }else{
            String methodName = scriptListener.methodName;
            for (int i=0; i<componentScriptListeners.length; i++) {
                if (componentScriptListeners[i].methodName.equals(methodName) && componentScriptListeners[i].scriptInLogo.equals(Boolean.TRUE)) {
                    updatedComponentScriptListeners[i] = newScriptListener;
                }else
                    updatedComponentScriptListeners[i] = componentScriptListeners[i];
            }
        }

        container.componentScriptListeners.put(object, updatedComponentScriptListeners);
        container.microworldChanged = true;
//        System.out.println("updatedComponentScriptListeners: " + updatedComponentScriptListeners.length);
*/
        scriptListener = newScriptListener;
    }

    protected void removeComponentScriptListener(ScriptListener scrListener) {
        composer.getScriptListenerMap().removeScriptListener(scrListener);
/*        String componentName = eSlateHandle.getComponentName();
        if (componentName == null) {
*/            /* A null componentName implies that the component no longer exists, i.e. it
             * has been removed from the microworld.
             */
/*            return;
        }
        ScriptListener[] componentScriptListeners = (ScriptListener[]) container.componentScriptListeners.getScriptListeners(object);
        if (componentScriptListeners == null)
            return;

        int listenerToBeRemovedIndex = -1;
        for (int i=0; i<componentScriptListeners.length; i++) {
            if (componentScriptListeners[i].equals(scrListener))
                listenerToBeRemovedIndex = i;
        }

        if (listenerToBeRemovedIndex == -1)
            return;
        ScriptListener[] updatedComponentScriptListeners = new ScriptListener[componentScriptListeners.length-1];
        int counter = 0;
        for (int i=0; i<componentScriptListeners.length; i++) {
            if (i == listenerToBeRemovedIndex)
                continue;
            updatedComponentScriptListeners[counter] = componentScriptListeners[i];
            counter++;
        }
        container.componentScriptListeners.putScriptListener(object, updatedComponentScriptListeners);
*/
        composer.setMicroworldChanged(true);
        scriptListener = null;
    }

    protected boolean deleteFile(String fileName) {
        if (fileName == null) return false;

        File f = new File(fileName);
        if (f.exists())
            return f.delete();
        return false;
    }

// Invalid since version 0.9.6, which comes without an event dialog.
/*    protected void updateEventDialog() {
        if (container.eventDialog == null)
            return;
        EventDialog eventDialog = container.eventDialog;

        String componentName = eSlateHandle.getComponentName();
        if (eventDialog.eSlateHandle.getComponentName().equals(componentName)) {
            EventPanel evtPanel = eventDialog.getEventPanel(method.getName());
*/
            /* Find the new ScriptListener from the componentScriptListeners array of
             * the Container.
             */
/*            ScriptListener scriptListener = container.componentScriptListeners.getScriptListener(object, method.getName(), true);
            if (evtPanel != null)
                evtPanel.setLogoScriptListener(scriptListener);
            else
                System.out.println("Serious inconsistency error in LogoScriptDialog updateEventDialog(): (1)");
        }
    }
*/
    protected void updateBeanInfoDialog() {
        if (composer.propertyEventEditor == null)
            return;
        ObjectEventPanel eventEditorPanel = composer.propertyEventEditor.eventEditorPanel;

        String componentName = eSlateHandle.getComponentName();
        if (eventEditorPanel.eSlateHandle.getComponentName().equals(componentName)) {
            EventPanel2 evtPanel = eventEditorPanel.getEventPanel(method.getName());
            /* Find the new ScriptListener from the componentScriptListeners array of
             * the composer.
             */
            ScriptListener scriptListener = composer.getScriptListenerMap().getScriptListener(object, method.getName(), ScriptListener.LOGO);
            if (evtPanel != null)
                evtPanel.setLogoScriptListener(scriptListener);
            else
                System.out.println("Serious inconsistency error in LogoScriptDialog updateBeanInfoDialog(): (1)");
        }
    }

    protected void saveScriptToFile() {
        int fileDialogMode = java.awt.FileDialog.SAVE;

        if (fileDialog == null)
            fileDialog = new ESlateFileDialog(new JFrame(), logoScriptDialogBundle.getString("FileDialogTitle"), fileDialogMode);

        if (fileDialog.isShowing()) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        fileDialog.setTitle(logoScriptDialogBundle.getString("FileDialogTitle"));
        fileDialog.setMode(fileDialogMode);
        if (composer.logoScriptDialogCurrentDir != null)
            fileDialog.setDirectory(composer.logoScriptDialogCurrentDir);
        /* Formulate the string to use in fileDialog.setFile(). This string contains all the
         * valid microworld file extensions as they are declared in the container.properties file.
         */
        fileDialog.setFile("*.txt; *.lgo");
        fileDialog.setDefaultExtension(new String[] {"lgo", "txt"});

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
        composer.logoScriptDialogCurrentDir = currDirectory;
        fileName = currDirectory+fileName;

        OutputStreamWriter fw = null;
        try{
            fw = new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8"));
//\            fw = new FileWriter(fileName);
        }catch (IOException e) {
            setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("UnableToSave") + fileName + "\"", logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        BufferedWriter bw = new BufferedWriter(fw, 30000);
        PrintWriter pw = new PrintWriter(bw);
        pw.println(scriptArea.getText());
        pw.close();
        setCursor(Cursor.getDefaultCursor());
    }

    protected void loadScriptFromFile() {
        int fileDialogMode = java.awt.FileDialog.LOAD;

        if (fileDialog == null)
            fileDialog = new ESlateFileDialog(new JFrame(), logoScriptDialogBundle.getString("OpenFileDialogTitle"), fileDialogMode);

        if (fileDialog.isShowing()) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        fileDialog.setTitle(logoScriptDialogBundle.getString("OpenFileDialogTitle"));
        fileDialog.setMode(fileDialogMode);
        if (composer.logoScriptDialogCurrentDir != null)
            fileDialog.setDirectory(composer.logoScriptDialogCurrentDir);
        /* Formulate the string to use in fileDialog.setFile(). This string contains all the
         * valid microworld file extensions as they are declared in the container.properties file.
         */
        fileDialog.setFile("*.lgo; *.txt");
        fileDialog.setDefaultExtension(new String[] {"txt", "lgo"});

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
        composer.logoScriptDialogCurrentDir = currDirectory;
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
            setScript(script);
            compile.setEnabled(true);
        }catch (IOException e) {
            setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("UnableToLoad") + fileName + "\"", logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
//        PrintWriter pw = new PrintWriter(bw);
//        pw.println(scriptArea.getText());
//        pw.close();
        setCursor(Cursor.getDefaultCursor());
    }

    public boolean find(String token, int startPosition, boolean forward) {
        if (token == null) return false;
        if (startPosition < 0 || startPosition > scriptArea.getDocument().getLength())
            return false;
//        scriptArea.setSelectionEnd(scriptArea.getSelectionStart());
        int index = -1;
        if (forward) {
            String text = scriptArea.getText();
            index = text.indexOf(token, startPosition);
        }else{
            try{
                String text = scriptArea.getDocument().getText(0, startPosition);
                index = text.lastIndexOf(token);
            }catch (javax.swing.text.BadLocationException exc) {}
        }
        if (index != -1) {
            scriptArea.setCaretPosition(index);
            scriptArea.setSelectionStart(index);
            scriptArea.setSelectionEnd(index+token.length());
            return true;
        }else{
            if (startPosition == 0 && forward)
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("UnableToFind1") + token + logoScriptDialogBundle.getString("UnableToFind2"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else if (startPosition != 0 && forward)
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("UnableToFind3") + token + "\".", logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else if (!forward)
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("UnableToFind3") + token + "\".", logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void goToLine(String line) {
        if (line == null) return;
        javax.swing.text.Element map = scriptArea.getDocument().getDefaultRootElement();
        try {
            int lineNum = Integer.parseInt(line) - 1;
            int numOfLines = scriptArea.getDocument().getDefaultRootElement().getElementCount();
            if (lineNum >= numOfLines) {
                ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("BadLineNumber2") + numOfLines + logoScriptDialogBundle.getString("BadLineNumber3"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            javax.swing.text.Element element = map.getElement(lineNum);
            if(element != null) {
                scriptArea.setCaretPosition(element.getStartOffset());
                int newLineIndex = scriptArea.getText().indexOf('\n', element.getStartOffset());
                scriptArea.setSelectionEnd(newLineIndex);
            }
        }catch(NumberFormatException nf) {
            ESlateOptionPane.showMessageDialog(this, logoScriptDialogBundle.getString("BadLineNumber1"), logoScriptDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

}






