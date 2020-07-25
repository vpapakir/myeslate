package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ESlateConsoles extends PrintStream {
    public static final int JAVA_CONSOLE = 0;
    public static final int JAVASCRIPT_CONSOLE = 1;

    Locale locale;
    ResourceBundle javaOutputConsoleDialogBundle;
    private boolean localeIsGreek = false;

    JFrame consoleDialog = null;
    // The java text area
    JTextArea javaTextArea;
    // The javascript text area
    JTextArea javascriptTextArea;
    // The logo text area
    JTextArea logoTextArea;
    /* The active text area: java, javascript or logo. It is not the text area
     * to which the output is directed. It is the visible text area, the one upon
     * which the tools operate.
     */
    JTextArea activeTextArea;
    // The scrollpane of the javaTextArea
    JScrollPane jScrollPane;
    // The scrollpane of the javascriptTextArea
    JScrollPane jsScrollPane;
    // The scrollpane of the logoTextArea
    JScrollPane logoScrollPane;
    JTabbedPane tabs;
    // The buffer for the java output
    StringBuffer tmpJavaBuffer = new StringBuffer(2000);
    // The buffer for the javascript output
    StringBuffer tmpJavascriptBuffer = new StringBuffer(2000);
    // The logo output console
    private LogoOutputConsole logoConsole = null; //new LogoOutputConsole(null, null);
    private static final String newLineStr = "\n";
    private static final String emptyStr = "";
    private static final String nullStr = "null";
    ESlateFileDialog fileDialog;
    String token = null; // The last token searched in the console
    // The way search is performed in the console
    boolean matchCase = false;
    String line = null; // The line to go to
    /* The output by default goes to the javaTextArea or the tmpJavaBuffer, if the
     * consoleDialog is invisible. The following flag adjusts the output to some
     * other textArea or tmpBuffer. Currently the only other supported is
     * the javascriptTextArea/tmpJavascriptBuffer.
     */
    int activeOutputConsole = JAVA_CONSOLE;
    /* Whether the consoles are enabled or not */
    boolean logoConsoleEnabled = false;
    boolean javaConsoleEnabled = true;
    boolean javascriptConsoleEnabled = false;
    ESlateContainer container = null;
    /* Flags whether the windowListener for the modal dialogs has been attached to the
     * ESlateConsole's frame.
     */
    boolean modalDialogWindowListenerAttached = false;
    // The following three controls are used in the Find dialog
    JCheckBox matchCaseBox;
    JTextField tokenFld;
    JLabel tokenLabel;


    public ESlateConsoles(ESlateContainer container, OutputStream out) {
        super(out);
//System.out.println("ESlateConsoles: " + (System.currentTimeMillis()-container.start));
        this.container = container;
    }

    public void setActiveOutputConsole(int console) {
        if (console == activeOutputConsole) return;
        if (console != JAVA_CONSOLE && console != JAVASCRIPT_CONSOLE) return;
        activeOutputConsole = console;
    }

    public int getActiveOutputConsole() {
        return activeOutputConsole;
    }

    public void println() {
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(newLineStr);
            else
                tmpJavascriptBuffer.append(newLineStr);
            return;
        }

        try{
//            javaTextArea.append(newLineStr);
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      newLineStr,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      newLineStr,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLOcationException");
        }

    }

    public void println(boolean x) {
        String str = x + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLOcationException");
        }
    }

    public void println(char x) {
        String str = x + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(int x) {
        String str = x + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(long x) {
        String str = x + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(float x) {
        String str = x + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(double x) {
        String str = x + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }
        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(char x[]) {
        String str = new String(x) + newLineStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(String x) {
        String str;
        if (x == null)
            str = nullStr + newLineStr;
        else
            str = x + newLineStr;

        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void println(Object x) {
        String str;
        if (x != null)
            str = x.toString() + newLineStr;
        else
            str = nullStr + newLineStr;

        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(boolean b) {
        String str = b + emptyStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(char c) {
        String str = c + emptyStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(int i) {
        String str = i + emptyStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(long l) {
        String str = l + emptyStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(float f) {
        String str = f + emptyStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(double d) {
        String str = d + emptyStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(char s[]) {
        String str = new String(s);
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(String str) {
        if (str == null)
            str = nullStr;
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    public void print(Object obj) {
        String str;
        if (obj == null)
            str = nullStr;
        else
            str = obj.toString();
        if (consoleDialog == null || !consoleDialog.isVisible()) {
            if (activeOutputConsole == JAVA_CONSOLE)
                tmpJavaBuffer.append(str);
            else
                tmpJavascriptBuffer.append(str);
            return;
        }

        try{
            if (activeOutputConsole == JAVA_CONSOLE)
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
            else
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      str,
                      null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
    }

    protected LogoOutputConsole getLogoConsole() {
        if (logoConsole == null)
            logoConsole = new LogoOutputConsole(null, null);
        return logoConsole;
    }

    public final void showConsole(Component comp) {
        if (container.microworld != null)
            container.microworld.checkActionPriviledge(container.microworld.consolesAllowed, "consolesAllowed");

        if (consoleDialog != null && consoleDialog.isVisible())
            return;

        if (consoleDialog == null) {
            createConsoleDialog();
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            int x, y;
            if (comp == null || !comp.isVisible()) {
                x = (screenSize.width/2) - (consoleDialog.getSize().width/2);
                y = (screenSize.height/2) - (consoleDialog.getSize().height/2);
            }else{
                Rectangle compBounds = comp.getBounds();
                java.awt.Point compLocation = comp.getLocationOnScreen();
        //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
                x = compLocation.x + compBounds.width/2 - consoleDialog.getSize().width/2;
                y = compLocation.y + compBounds.height/2-consoleDialog.getSize().height/2;
                if (x+consoleDialog.getSize().width > screenSize.width)
                    x = screenSize.width - consoleDialog.getSize().width;
                if (y+consoleDialog.getSize().height > screenSize.height)
                    y = screenSize.height - consoleDialog.getSize().height;
                if (x < 0) x = 0;
                if (y < 0) y = 0;
            }
            consoleDialog.setLocation(x, y);

            getLogoConsole();
            try{
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      tmpJavaBuffer.toString(),
                      null);
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      tmpJavascriptBuffer.toString(),
                      null);
                logoConsole.consoleDialog = consoleDialog;
                logoConsole.logoTextArea = logoTextArea;
                logoTextArea.getDocument().insertString(
                      logoTextArea.getDocument().getEndPosition().getOffset()-1,
                      logoConsole.outBuffer.toString(),
                      null);
            }catch (Exception exc) {
                System.out.println("BadLocationException");
            }
            tmpJavaBuffer.setLength(0);
            tmpJavascriptBuffer.setLength(0);
            logoConsole.outBuffer.setLength(0);
            consoleDialog.show();
            activeTextArea.requestFocus();
        }else{
            try{
                javaTextArea.getDocument().insertString(
                      javaTextArea.getDocument().getEndPosition().getOffset()-1,
                      tmpJavaBuffer.toString(),
                      null);
                javascriptTextArea.getDocument().insertString(
                      javascriptTextArea.getDocument().getEndPosition().getOffset()-1,
                      tmpJavascriptBuffer.toString(),
                      null);
                logoTextArea.getDocument().insertString(
                      logoTextArea.getDocument().getEndPosition().getOffset()-1,
                      logoConsole.outBuffer.toString(),
                      null);
            }catch (Exception exc) {
                System.out.println("BadLocationException");
            }
            tmpJavaBuffer.setLength(0);
            tmpJavascriptBuffer.setLength(0);
            logoConsole.outBuffer.setLength(0);
            consoleDialog.show();
            activeTextArea.requestFocus();
        }
    }

    public void setLogoConsoleEnabled(boolean enable) {
        if (logoConsoleEnabled == enable) return;
        logoConsoleEnabled = enable;
        if (tabs != null)
            tabs.setEnabledAt(2, logoConsoleEnabled);
    }

    public boolean isLogoConsoleEnabled() {
        return logoConsoleEnabled;
    }

    public void setJavaConsoleEnabled(boolean enable) {
        if (javaConsoleEnabled == enable) return;
        javaConsoleEnabled = enable;
        if (tabs != null)
            tabs.setEnabledAt(0, javaConsoleEnabled);
    }

    public boolean isJavaConsoleEnabled() {
        return javaConsoleEnabled;
    }

    public void setJavascriptConsoleEnabled(boolean enable) {
        if (javascriptConsoleEnabled == enable) return;
        javascriptConsoleEnabled = enable;
        if (tabs != null)
            tabs.setEnabledAt(1, javascriptConsoleEnabled);
    }

    public boolean isJavascriptConsoleEnabled() {
        return javascriptConsoleEnabled;
    }

    protected void createConsoleDialog() {
        consoleDialog = new JFrame(); //JDialog(new JFrame());
        consoleDialog.setIconImage(container.getAppIcon());
        locale = Locale.getDefault();
        javaOutputConsoleDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.JavaOutputConsoleDialogBundle", locale);
        if (javaOutputConsoleDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.JavaOutputConsoleDialogBundle_el_GR"))
            localeIsGreek = true;

        consoleDialog.setTitle(javaOutputConsoleDialogBundle.getString("DialogTitle"));

        // Create the menu bar
        NoBorderButton copy = new NoBorderButton(new ImageIcon(getClass().getResource("images/copy.gif")));
        Dimension buttonSize = new Dimension(22, 21);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        copy.setMargin(zeroInsets);
        copy.setToolTipText(javaOutputConsoleDialogBundle.getString("CopyTip"));
        copy.setRequestFocusEnabled(false);

        NoBorderButton save = new NoBorderButton(new ImageIcon(getClass().getResource("images/save.gif")));
        save.setMargin(zeroInsets);
        save.setToolTipText(javaOutputConsoleDialogBundle.getString("SaveTip"));
        save.setRequestFocusEnabled(false);

        NoBorderButton find = new NoBorderButton(new ImageIcon(getClass().getResource("images/find.gif")));
        find.setMargin(zeroInsets);
        find.setToolTipText(javaOutputConsoleDialogBundle.getString("FindTip"));
        find.setRequestFocusEnabled(false);

        NoBorderButton findNext = new NoBorderButton(new ImageIcon(getClass().getResource("images/findNext2.gif")));
        findNext.setMargin(zeroInsets);
        findNext.setToolTipText(javaOutputConsoleDialogBundle.getString("FindNextTip"));
        findNext.setRequestFocusEnabled(false);

        NoBorderButton findPrev = new NoBorderButton(new ImageIcon(getClass().getResource("images/findPrev2.gif")));
        findPrev.setMargin(zeroInsets);
        findPrev.setToolTipText(javaOutputConsoleDialogBundle.getString("FindPrevTip"));
        findPrev.setRequestFocusEnabled(false);

        NoBorderButton goToLine = new NoBorderButton(new ImageIcon(getClass().getResource("images/goToLine.gif")));
        goToLine.setMargin(zeroInsets);
        goToLine.setToolTipText(javaOutputConsoleDialogBundle.getString("GoToLineTip"));
        goToLine.setRequestFocusEnabled(false);

        NoBorderButton memory = new NoBorderButton(new ImageIcon(getClass().getResource("images/memory.gif")));
        memory.setMargin(zeroInsets);
        memory.setToolTipText(javaOutputConsoleDialogBundle.getString("MemoryTip"));
        memory.setRequestFocusEnabled(false);

        NoBorderButton gc = new NoBorderButton(new ImageIcon(getClass().getResource("images/gc.gif")));
        gc.setMargin(zeroInsets);
        gc.setToolTipText(javaOutputConsoleDialogBundle.getString("GCTip"));
        gc.setRequestFocusEnabled(false);

        NoBorderButton VMinfo = new NoBorderButton(new ImageIcon(getClass().getResource("images/info.gif")));
        VMinfo.setMargin(zeroInsets);
        VMinfo.setToolTipText(javaOutputConsoleDialogBundle.getString("InfoTip"));
        VMinfo.setRequestFocusEnabled(false);

        NoBorderButton clear = new NoBorderButton(new ImageIcon(getClass().getResource("images/clear.gif")));
        clear.setMargin(zeroInsets);
        clear.setToolTipText(javaOutputConsoleDialogBundle.getString("ClearTip"));
        clear.setRequestFocusEnabled(false);

        javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
    	  toolBar.setFloatable(false);

        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(copy);
        toolBar.add(save);
        toolBar.addSeparator();
        toolBar.add(find);
        toolBar.add(findPrev);
        toolBar.add(findNext);
        toolBar.add(goToLine);
        toolBar.addSeparator();
        toolBar.add(memory);
        toolBar.add(gc);
        toolBar.add(VMinfo);
        toolBar.addSeparator();
        toolBar.add(clear);
        toolBar.add(Box.createGlue());

        JPanel barPanel = new JPanel(true);
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.Y_AXIS));
        barPanel.add(Box.createVerticalStrut(1));
        barPanel.add(toolBar);
        barPanel.add(Box.createVerticalStrut(1));

        javaTextArea = new JTextArea();
        javaTextArea.setLineWrap(true);
        javaTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jScrollPane = new JScrollPane(javaTextArea);
//        Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//        if (localeIsGreek)
//            javaTextArea.setFont(greekUIFont);

        javascriptTextArea = new JTextArea();
        javascriptTextArea.setLineWrap(true);
        javascriptTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jsScrollPane = new JScrollPane(javascriptTextArea);
//        if (localeIsGreek)
//            javascriptTextArea.setFont(greekUIFont);

        logoTextArea = new JTextArea();
        logoTextArea.setLineWrap(true);
        logoTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logoScrollPane = new JScrollPane(logoTextArea);
//        if (localeIsGreek)
//            logoTextArea.setFont(greekUIFont);

        activeTextArea = javaTextArea;

        tabs = new JTabbedPane();
        tabs.addTab(javaOutputConsoleDialogBundle.getString("Java console"), jScrollPane);
        tabs.addTab(javaOutputConsoleDialogBundle.getString("Javascript console"), jsScrollPane);
        tabs.addTab(javaOutputConsoleDialogBundle.getString("Logo console"), logoScrollPane);

        tabs.setEnabledAt(0, javaConsoleEnabled);
        tabs.setEnabledAt(1, javascriptConsoleEnabled);
        tabs.setEnabledAt(2, logoConsoleEnabled);

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 0));

        mainPanel.add(barPanel, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
//        mainPanel.add(jScrollPane, BorderLayout.CENTER);

        consoleDialog.getContentPane().add(mainPanel);

        consoleDialog.pack();
        consoleDialog.setSize(new Dimension(500, 600));

        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabs.getSelectedIndex() == 0)
                    activeTextArea = javaTextArea;
                else if (tabs.getSelectedIndex() == 1)
                    activeTextArea = javascriptTextArea;
                else
                    activeTextArea = logoTextArea;
            }
        });

        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectionStart = activeTextArea.getSelectionStart();
                int selectionEnd = activeTextArea.getSelectionEnd();
                if (selectionStart != selectionEnd) // There is smth selected
                    activeTextArea.copy();
                else{
                    activeTextArea.selectAll();
                    activeTextArea.copy();
                    activeTextArea.setSelectionStart(selectionStart);
                    activeTextArea.setSelectionEnd(selectionEnd);
                }
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        });

        find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findAction();
            }
        });

        findNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findNextAction();
            }
        });

        findPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findPrevAction();
            }
        });

        goToLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToLineAction();
            }
        });

        memory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                memReportAction();
            }
        });

        gc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gcAction();
            }
        });

        VMinfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String vendor = System.getProperty("java.vendor");
                String version = System.getProperty("java.version");
                String osName = System.getProperty("os.name");
                String osArch = System.getProperty("os.arch");
                String osVersion = System.getProperty("os.version");
                String output = "E-Slate is executed on " + vendor + "'s Java VM, version " + version + '\n';
                output = output + "OS: " + osName + ", " + osArch + ", " + osVersion + '\n';
                try{
                    activeTextArea.getDocument().insertString(
                          activeTextArea.getDocument().getEndPosition().getOffset()-1,
                          output,
                          null);
                }catch (Exception exc) {
                    System.out.println("BadLocationException");
                }

                // Current classpath
                String classpath = System.getProperty("java.class.path");
                char pathSeparator = System.getProperty("path.separator").charAt(0);
                int pathSeparatorIndex = 0, lastIndex = 0;
                String jarFileName;
                java.io.File jarFile;
                StringBuffer rightClasspath = new StringBuffer();
                while ((pathSeparatorIndex = classpath.indexOf(pathSeparator, pathSeparatorIndex)) != -1) {
                    jarFileName = classpath.substring(lastIndex, pathSeparatorIndex);
                    jarFile = new java.io.File(jarFileName);
//                            System.out.println("Checking: " + jarFileName);
                    if (jarFile.exists()) {
                        try{
                            rightClasspath.append(jarFile.getCanonicalPath());
                            rightClasspath.append(pathSeparator);
                            rightClasspath.append('\n');
                        }catch (java.io.IOException exc) {
                            System.out.println("Exception while printing classpath entry: " + jarFileName);
                        }
                    }

                    pathSeparatorIndex++;
                    lastIndex = pathSeparatorIndex;
                }
                if (lastIndex < classpath.length()-1) {
                    rightClasspath.append(classpath.substring(lastIndex));
                    rightClasspath.append('\n');
                }
//                System.out.println("Classpath : ");
//                System.out.println(rightClasspath);
                try{
                    activeTextArea.getDocument().insertString(
                          activeTextArea.getDocument().getEndPosition().getOffset()-1,
                          "Classpath: \n" + rightClasspath.toString(),
                          null);
                }catch (Exception exc) {
                    System.out.println("BadLocationException");
                }
            }
        });


        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAction();
            }
        });

        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findNextAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findPrevAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goToLineAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gcAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                memReportAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        consoleDialog.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAction();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // ESCAPE HANDLER
        consoleDialog.getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                consoleDialog.dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void bringToFront() {
        if (consoleDialog == null) return;
        // ERROR: here we have to deiconify the Console dialog, if it is iconified
        if (consoleDialog.getState() == JFrame.ICONIFIED)
            consoleDialog.setState(JFrame.NORMAL);
        consoleDialog.toFront();
        consoleDialog.requestFocus();
    }

    protected void saveConsoleToFile() {
        int fileDialogMode = java.awt.FileDialog.SAVE;

        if (fileDialog == null)
            fileDialog = new ESlateFileDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("FileDialogTitle"), fileDialogMode);

        if (fileDialog.isShowing()) {
            return;
        }
        consoleDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        fileDialog.setTitle(javaOutputConsoleDialogBundle.getString("FileDialogTitle"));
        fileDialog.setMode(fileDialogMode);
        /* Formulate the string to use in fileDialog.setFile(). This string contains all the
        v * valid microworld file extensions as they are declared in the container.properties file.
         */
        fileDialog.setFile("*.txt");
        fileDialog.setDefaultExtension("txt");

        /* Center the fileDialog inside the Container.
         */
        fileDialog.pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        Rectangle compBounds = consoleDialog.getBounds();
        java.awt.Point compLocation = consoleDialog.getLocationOnScreen();
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
            consoleDialog.setCursor(Cursor.getDefaultCursor());
            return;
        }
        String currDirectory = fileDialog.getDirectory();
        fileName = currDirectory+fileName;

        OutputStreamWriter fw = null;
        try{
            fw = new OutputStreamWriter(new FileOutputStream(fileName));
        }catch (IOException e) {
            consoleDialog.setCursor(Cursor.getDefaultCursor());
            ESlateOptionPane.showMessageDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("UnableToSave") + fileName + "\"", javaOutputConsoleDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        BufferedWriter bw = new BufferedWriter(fw, 30000);
        PrintWriter pw = new PrintWriter(bw);
        pw.println(activeTextArea.getText());
        pw.close();
        consoleDialog.setCursor(Cursor.getDefaultCursor());
    }

    public boolean find(String token, int startPosition, boolean forward) {
        if (token == null) return false;
        if (startPosition < 0 || startPosition > activeTextArea.getDocument().getLength())
            return false;
        if (!matchCase)
            token = token.toLowerCase();
//        activeTextArea.setSelectionEnd(activeTextArea.getSelectionStart());
        int index = -1;
        if (forward) {
            String text = activeTextArea.getText();
            if (!matchCase)
                text = text.toLowerCase();
            index = text.indexOf(token, startPosition);
        }else{
            try{
                String text = activeTextArea.getDocument().getText(0, startPosition);
                if (!matchCase)
                    text = text.toLowerCase();
                index = text.lastIndexOf(token);
            }catch (javax.swing.text.BadLocationException exc) {}
        }
        if (index != -1) {
            activeTextArea.setCaretPosition(index);
            activeTextArea.setSelectionStart(index);
            activeTextArea.setSelectionEnd(index+token.length());
            return true;
        }else{
            if (startPosition == 0 && forward)
                ESlateOptionPane.showMessageDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("UnableToFind1") + token + javaOutputConsoleDialogBundle.getString("UnableToFind2"), javaOutputConsoleDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else if (startPosition != 0 && forward)
                ESlateOptionPane.showMessageDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("UnableToFind3") + token + "\".", javaOutputConsoleDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
            else if (!forward)
                ESlateOptionPane.showMessageDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("UnableToFind3") + token + "\".", javaOutputConsoleDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public void goToLine(String line) {
        if (line == null) return;
        javax.swing.text.Element map = activeTextArea.getDocument().getDefaultRootElement();
        try {
            int lineNum = Integer.parseInt(line) - 1;
            int numOfLines = activeTextArea.getDocument().getDefaultRootElement().getElementCount();
            if (lineNum >= numOfLines) {
                ESlateOptionPane.showMessageDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("BadLineNumber2") + tabs.getTitleAt(tabs.getSelectedIndex()) + javaOutputConsoleDialogBundle.getString("BadLineNumber3") + numOfLines + javaOutputConsoleDialogBundle.getString("BadLineNumber4"), javaOutputConsoleDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            javax.swing.text.Element element = map.getElement(lineNum);
            if(element != null) {
                activeTextArea.setCaretPosition(element.getStartOffset());
                int newLineIndex = activeTextArea.getText().indexOf('\n', element.getStartOffset());
                activeTextArea.setSelectionEnd(newLineIndex);
            }
        }catch(NumberFormatException nf) {
            ESlateOptionPane.showMessageDialog(consoleDialog, javaOutputConsoleDialogBundle.getString("BadLineNumber1"), javaOutputConsoleDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findAction() {
        if (matchCaseBox == null) {
            matchCaseBox = new JCheckBox(javaOutputConsoleDialogBundle.getString("MatchCase"));
            tokenFld = new javax.swing.JTextField();
            tokenLabel = new JLabel(javaOutputConsoleDialogBundle.getString("FindWhat"));
        }
        tokenFld.setText(token);
        matchCaseBox.setSelected(matchCase);
        Object[] comps = new Object[] {tokenLabel, tokenFld, matchCaseBox};
        int val = JOptionPane.showOptionDialog(consoleDialog, comps,
                         javaOutputConsoleDialogBundle.getString("Find"),
                         JOptionPane.OK_CANCEL_OPTION,
                         JOptionPane.QUESTION_MESSAGE,
                         null,
                         null,
                         null);

        if (val == JOptionPane.OK_OPTION) {
            token = tokenFld.getText();
            matchCase = matchCaseBox.isSelected();
            find(token, 0, true);
        }

/*        String tok = (String) ESlateOptionPane.showInputDialog(consoleDialog,
                         javaOutputConsoleDialogBundle.getString("FindWhat"),
                         javaOutputConsoleDialogBundle.getString("Find"),
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

    private void findNextAction() {
        if (token == null)
            return;
        find(token, activeTextArea.getCaretPosition(), true);
    }

    private void findPrevAction() {
        if (token == null)
            return;
        int selectionStart = activeTextArea.getSelectionStart();
        int selectionEnd = activeTextArea.getSelectionEnd();
        if (selectionStart != selectionEnd) {// There is smth selected
            String selectedText = activeTextArea.getSelectedText();
            if (!matchCase)
                selectedText = selectedText.toLowerCase();
            if (selectedText.equals(token))
                activeTextArea.setSelectionEnd(selectionStart);
        }
        find(token, activeTextArea.getCaretPosition(), false);
    }

    private void goToLineAction() {
        String l = (String) ESlateOptionPane.showInputDialog(consoleDialog,
                    javaOutputConsoleDialogBundle.getString("GoToLine"),
                    javaOutputConsoleDialogBundle.getString("GoTo"),
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    line);

        if(l != null) {
            line = l;
            goToLine(line);
        }
    }

    public void clearAction() {
        activeTextArea.setText("");
    }

    private void gcAction() {
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        long usedMemBefore = rt.totalMemory() - rt.freeMemory();
        rt.gc();
        long usedMemAfter = rt.totalMemory() - rt.freeMemory();
        long freed = usedMemBefore-usedMemAfter;
        long freed2 = freed / 1000;
        if (freed - (freed2*1000) > 500)
            freed2 = freed2 + 1;
        NumberFormat nf = NumberFormat.getInstance();
        if (nf instanceof DecimalFormat)
            ((DecimalFormat) nf).setPositiveSuffix(" Kb");
        String freedStr = nf.format(freed2);
        System.out.print("GC freed " + freedStr + ".\n");
    }

    private void memReportAction() {
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        long total = rt.totalMemory();
        long total2 = total / 1000;
        if (total - (total2*1000) > 500)
            total2 = total2 + 1;
        NumberFormat nf = NumberFormat.getInstance();
        if (nf instanceof DecimalFormat)
            ((DecimalFormat) nf).setPositiveSuffix(" Kb");
        String totalStr = nf.format(total2);

        long free = rt.freeMemory();
        long free2 = free / 1000;
        if (free - (free2*1000) > 500)
            free2 = free2 + 1;
        String freeStr = nf.format(free2);

        long used = total2 - free2;
        String usedStr = nf.format(used);

//                System.out.println("MEMORY STATUS. Total heap size: " + totalStr + ".  Free memory: " + freeStr + ".  Used memory: " + usedStr + ".");
//        try{
//            activeTextArea.getDocument().insertString(
//                  activeTextArea.getDocument().getEndPosition().getOffset()-1,
        System.out.print("MEMORY STATUS. Total heap size: " + totalStr + ".  Free memory: " + freeStr + ".  Used memory: " + usedStr + ".\n");
//                  null);
            if (activeTextArea != null)
                goToLine(new Integer(activeTextArea.getDocument().getDefaultRootElement().getElementCount()).toString());
//        }catch (Exception exc) {
//            System.out.println("BadLocationException");
//        }
    }

    private void saveAction() {
        saveConsoleToFile();
    }
}

