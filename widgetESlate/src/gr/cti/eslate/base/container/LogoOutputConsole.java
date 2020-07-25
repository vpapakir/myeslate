package gr.cti.eslate.base.container;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import virtuoso.logo.Console;
import virtuoso.logo.LanguageException;

public class LogoOutputConsole extends Console {
    Locale locale;
//    ResourceBundle logoOutputConsoleDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    private boolean localeIsGreek = false;

//    JFrame consoleDialog = null;
//    JTextArea textArea;
    StringBuffer outBuffer = new StringBuffer();
    JTextArea logoTextArea;
    JFrame consoleDialog;

    public LogoOutputConsole(JTextArea logoTextArea, JFrame consoleDialog) {
        this.logoTextArea = logoTextArea;
        this.consoleDialog = consoleDialog;
    }

    public void put(char c) throws LanguageException {
        outBuffer.append(c);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            logoTextArea.getDocument().insertString(
                  logoTextArea.getDocument().getEndPosition().getOffset()-1,
                  new Character(c).toString(),
                  null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public void put(char buf[], int num) throws LanguageException {
        outBuffer.append(buf, 0, num);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            logoTextArea.getDocument().insertString(
                  logoTextArea.getDocument().getEndPosition().getOffset()-1,
                  new String(buf, 0, num),
                  null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public void putLine(String str) {
        str = str + '\n';
        outBuffer.append(str);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            logoTextArea.getDocument().insertString(
                  logoTextArea.getDocument().getEndPosition().getOffset()-1,
                  str,
                  null);
        }catch (Exception exc) {
            System.out.println("1. BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public void put(String str) throws LanguageException {
        outBuffer.append(str);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            logoTextArea.getDocument().insertString(
                  logoTextArea.getDocument().getEndPosition().getOffset()-1,
                  str,
                  null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public String promptGetLine(char prompt) {return "";};
    public boolean charAvail() throws LanguageException {return false;};
    public char getChar() throws LanguageException {return ' ';};
    public int getAvailable(char buf[]) throws LanguageException {return 0;};
    public String getLine() throws LanguageException {return "";};
    public boolean eof() throws LanguageException {return false;};
    public void createEditor(String data) throws LanguageException {};
}

/*public class LogoOutputConsole extends Console {
    Locale locale;
    ResourceBundle logoOutputConsoleDialogBundle;
    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;

    JFrame consoleDialog = null;
    JTextArea textArea;
    StringBuffer outBuffer = new StringBuffer();

    public void put(char c) throws LanguageException {
        outBuffer.append(c);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            textArea.getDocument().insertString(
                  textArea.getDocument().getEndPosition().getOffset()-1,
                  new Character(c).toString(),
                  null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public void put(char buf[], int num) throws LanguageException {
        outBuffer.append(buf, 0, num);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            textArea.getDocument().insertString(
                  textArea.getDocument().getEndPosition().getOffset()-1,
                  new String(buf, 0, num),
                  null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public void putLine(String str) {
        str = str + '\n';
        outBuffer.append(str);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            textArea.getDocument().insertString(
                  textArea.getDocument().getEndPosition().getOffset()-1,
                  str,
                  null);
        }catch (Exception exc) {
            System.out.println("1. BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public void put(String str) throws LanguageException {
        outBuffer.append(str);
        if (consoleDialog == null || !consoleDialog.isVisible())
            return;
        try{
            textArea.getDocument().insertString(
                  textArea.getDocument().getEndPosition().getOffset()-1,
                  str,
                  null);
        }catch (Exception exc) {
            System.out.println("BadLocationException");
        }
//        System.out.println("outBuffer: " + outBuffer);
    }

    public String promptGetLine(char prompt) {return "";};
    public boolean charAvail() throws LanguageException {return false;};
    public char getChar() throws LanguageException {return ' ';};
    public int getAvailable(char buf[]) throws LanguageException {return 0;};
    public String getLine() throws LanguageException {return "";};
    public boolean eof() throws LanguageException {return false;};
    public void createEditor(String data) throws LanguageException {};

    public void showConsole(Component comp) {
        if (consoleDialog != null && consoleDialog.isVisible()) {
            consoleDialog.toFront();
            consoleDialog.requestFocus();
            return;
        }

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
            consoleDialog.show();
        }else{
            textArea.setText(outBuffer.toString());
            consoleDialog.show();
        }
    }

    protected void createConsoleDialog() {
        consoleDialog = new JFrame(); //Dialog(new JFrame());
        consoleDialog.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        locale = Locale.getDefault();
        logoOutputConsoleDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LogoOutputConsoleDialogBundle", locale);
        if (logoOutputConsoleDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.LogoOutputConsoleDialogBundle_el_GR"))
            localeIsGreek = true;

        consoleDialog.setTitle(logoOutputConsoleDialogBundle.getString("DialogTitle"));

        textArea = new JTextArea();
        textArea.setText(outBuffer.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);

        if (localeIsGreek)
            textArea.setFont(greekUIFont);

        // The button panel (OK, CANCEL)
        Color color128 = new Color(0, 0, 128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        JButton close = new JButton(logoOutputConsoleDialogBundle.getString("Close"));
        if (localeIsGreek)
            close.setFont(greekUIFont);
        close.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        close.setMaximumSize(buttonSize);
        close.setPreferredSize(buttonSize);
        close.setMinimumSize(buttonSize);
        close.setMargin(zeroInsets);

        JButton clear = new JButton(logoOutputConsoleDialogBundle.getString("Clear"));
        if (localeIsGreek)
            clear.setFont(greekUIFont);
        clear.setForeground(color128);
        clear.setMaximumSize(buttonSize);
        clear.setPreferredSize(buttonSize);
        clear.setMinimumSize(buttonSize);
        clear.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(close);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(clear);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,8,5));

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 5));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        consoleDialog.getContentPane().add(mainPanel);

        consoleDialog.pack();
        consoleDialog.setSize(new Dimension(300, 400));

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consoleDialog.dispose();
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                outBuffer.setLength(0);
            }
        });
        // ESCAPE HANDLER
        consoleDialog.getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                consoleDialog.dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

    }
}

*/


