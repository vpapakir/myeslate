package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;


public class DetailedErrorDialog extends JDialog {
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    protected Font greekUIFont14 = new Font("Helvetica", Font.PLAIN, 14);
    Locale locale;
    boolean localeIsGreek = false;
    ResourceBundle detailedErrorDialogBundle;
    JScrollPane scrollpane;
    JTextArea errorArea;
    JLabel errorLabel;
    JButton okButton;
    JToggleButton detailsButton;
    JTextArea detailsArea;
    JPanel mainPanel, detailsPanel;
    Dimension maxErrorAreaSize = new Dimension(400, 200);
    int prevHeight = 0;

    public DetailedErrorDialog(java.awt.Frame parentFrame) {
        super(parentFrame, true);
        initDialog();
    }

    public DetailedErrorDialog(java.awt.Dialog parentDialog) {
        super(parentDialog, true);
        initDialog();
    }

    void initDialog() {
//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        if (contentFrame != null)
//            contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());

        locale = Locale.getDefault();
        detailedErrorDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.DetailedErrorDialogBundle", locale);
        if (detailedErrorDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.DetailedErrorDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(detailedErrorDialogBundle.getString("Error"));

        errorLabel = new JLabel(javax.swing.UIManager.getIcon("OptionPane.errorIcon"));
        errorLabel.setAlignmentY(CENTER_ALIGNMENT);
        errorLabel.setRequestFocusEnabled(true);

        errorArea = new JTextArea();
        errorArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        errorArea.setLineWrap(true);
        errorArea.setBackground(UIManager.getColor("control"));
        errorArea.setWrapStyleWord(true);
        errorArea.setSelectionColor(Color.lightGray);
        errorArea.setEditable(false);
        errorArea.setSelectedTextColor(errorArea.getForeground());
        errorArea.setMaximumSize(maxErrorAreaSize);
        errorArea.setMinimumSize(new Dimension(200, 20));
        errorArea.setAlignmentY(CENTER_ALIGNMENT);
        errorArea.setRequestFocusEnabled(true);
//        errorArea.setBorder(new javax.swing.border.LineBorder(Color.black));

        JPanel errorPanel = new JPanel(true);
        errorPanel.setLayout(new BorderLayout(10, 0));
        errorPanel.add(errorLabel, BorderLayout.WEST);
        errorPanel.add(errorArea, BorderLayout.CENTER);
        errorPanel.setBorder(new EmptyBorder(5,5,5,20));
        errorPanel.setRequestFocusEnabled(true);

        okButton = new JButton(detailedErrorDialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);
        okButton.setRequestFocusEnabled(true);

        detailsButton = new JToggleButton(detailedErrorDialogBundle.getString("ShowDetails"));
        detailsButton.setToolTipText(detailedErrorDialogBundle.getString("ShowTip"));
        detailsButton.setForeground(color128);
        detailsButton.setMaximumSize(buttonSize);
        detailsButton.setPreferredSize(buttonSize);
        detailsButton.setMinimumSize(buttonSize);
        detailsButton.setMargin(zeroInsets);
        detailsButton.setEnabled(false);
        detailsButton.setRequestFocusEnabled(true);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(detailsButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.setBorder(new EmptyBorder(5,0,5,5));
        buttonPanel.setRequestFocusEnabled(false);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(errorPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.setRequestFocusEnabled(false);

        detailsArea = new JTextArea() ;
        detailsArea.setEditable(false);
        detailsArea.setBackground(UIManager.getColor("TextArea.background").brighter());
        detailsArea.setRequestFocusEnabled(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        NoBorderButton copy = new NoBorderButton(new ImageIcon(getClass().getResource("images/copy.gif")));
        Dimension buttonSize2 = new Dimension(22, 21);
        copy.setMargin(zeroInsets);
        copy.setToolTipText(detailedErrorDialogBundle.getString("CopyTip"));
        copy.setRequestFocusEnabled(false);
        javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
    	  toolBar.setFloatable(false);
        toolBar.setRequestFocusEnabled(false);

        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(copy);
        toolBar.add(Box.createGlue());

        scrollpane = new JScrollPane(detailsArea);
        scrollpane.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        scrollpane.setRequestFocusEnabled(false);

        detailsPanel = new JPanel(true);
        detailsPanel.setLayout(new BorderLayout(0, 3));
        detailsPanel.add(toolBar, BorderLayout.NORTH);
        detailsPanel.add(scrollpane, BorderLayout.CENTER);
        detailsPanel.setRequestFocusEnabled(false);

        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0,0));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.setRequestFocusEnabled(false);

        getContentPane().add(mainPanel);
        getRootPane().setRequestFocusEnabled(false);

        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectionStart = detailsArea.getSelectionStart();
                int selectionEnd = detailsArea.getSelectionEnd();
                if (selectionStart != selectionEnd) // There is smth selected
                    detailsArea.copy();
                else{
                    detailsArea.selectAll();
                    detailsArea.copy();
                    detailsArea.setSelectionStart(selectionStart);
                    detailsArea.setSelectionEnd(selectionEnd);
                }
            }
        });
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (detailsButton.isSelected()) {
                    int tmp = getSize().height;
                    if (prevHeight == 0) {
                        int dialogHeight = getSize().height;
                        int scrollpaneHeight = scrollpane.getPreferredSize().height;
                        int detailsPanelHeight = detailsPanel.getPreferredSize().height;
//                        System.out.println("scrollpaneHeight: " + scrollpaneHeight);
//                        System.out.println("detailsPanelHeight: " + detailsPanelHeight);
                        Point dialogLocation = getLocation();
                        if (dialogLocation.y + dialogHeight + detailsPanelHeight > Toolkit.getDefaultToolkit().getScreenSize().height) {
                            detailsPanelHeight = Toolkit.getDefaultToolkit().getScreenSize().height - (dialogLocation.y + dialogHeight);
//                            System.out.println("New detailsPanelHeight: " + detailsPanelHeight);
                        }
                        prevHeight = getSize().height + detailsPanelHeight; //65;
                    }
                    setSize(getSize().width, prevHeight);
                    prevHeight = tmp;
                    mainPanel.add(detailsPanel, BorderLayout.CENTER);
                    detailsButton.setText(detailedErrorDialogBundle.getString("HideDetails"));
                    detailsButton.setToolTipText(detailedErrorDialogBundle.getString("HideTip"));

                    scrollpane.invalidate();
                    scrollpane.doLayout();
                    scrollpane.revalidate();

                    synchronized(getTreeLock()) {
                        validateTree();
                    }
                }else{
                    mainPanel.remove(detailsPanel);
                    detailsButton.setText(detailedErrorDialogBundle.getString("ShowDetails"));
                    detailsButton.setToolTipText(detailedErrorDialogBundle.getString("ShowTip"));
                    int tmp = getSize().height;
                    setSize(getSize().width, prevHeight);
                    prevHeight = tmp;
                    mainPanel.invalidate();
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                okButton.requestFocus();
            }
        });
    }

    protected void validateTree() {
    	synchronized(getTreeLock()) {
            super.validateTree();
        }
    }

    public void setIcon(Icon icon) {
        errorLabel.setIcon(icon);
    }

    public void setMessage(String message) {
        adjustErrorAreaSize(message);
        errorArea.setText(message);
    }

    void adjustErrorAreaSize(String errorText) {
        java.awt.FontMetrics fm = errorArea.getFontMetrics(errorArea.getFont());
        int width = fm.stringWidth(errorText);
        int currentwidth = errorArea.getSize().width;
        if (currentwidth == 0) {
            if (width < 400)
                errorArea.setPreferredSize(new Dimension(width, 20));
            else{
                int height = 20 * (int) (width / 400);
                errorArea.setPreferredSize(new Dimension(400, height));
            }
        }
    }

    public void setDetails(String details) {
        details = insertNewLineCharacters(details);
        detailsArea.setText(details);
        adjustDetailsButtonStatus();
    }

    public void appendToDetails(String moreDetails) {
        if (moreDetails == null) return;
        moreDetails = insertNewLineCharacters(moreDetails);
        detailsArea.append(moreDetails);
        adjustDetailsButtonStatus();
    }

    public void createNewLine() {
        detailsArea.append("\n");
    }

    public void appendThrowableStackTrace(Throwable thr) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        thr.printStackTrace(printWriter);
        detailsArea.append(insertNewLineCharacters(writer.toString()));
        adjustDetailsButtonStatus();
    }

    public void appendThrowableMessage(Throwable thr) {
        String excMsg = thr.getMessage();
        if (excMsg == null) return;
        excMsg = insertNewLineCharacters(excMsg);
        detailsArea.append(excMsg);
        adjustDetailsButtonStatus();
    }

    protected void adjustDetailsButtonStatus() {
        if (detailsArea.getText() != null && detailsArea.getText().trim().length() != 0)
            detailsButton.setEnabled(true);
        else
            detailsButton.setEnabled(false);
    }

    /* This method accepts a PrintWriter and inserts a '\r' char after each newline ('\n')
     * char. If this does not happen and the contents of the detailsArea are copied to
     * either the OutLook mail editor or WordPad, then the text appears without any new
     * line characters.
     */
    String insertNewLineCharacters(String inputStr) {
        if (inputStr == null) return null;
        StringBuffer sb = new StringBuffer(inputStr);
        for (int i=0; i<sb.length(); i++) {
            if (sb.charAt(i) == '\n' && ((i+1) < sb.length() && sb.charAt(i+1) != '\r')) {
                sb.insert(i+1, '\r');
//                System.out.println("appendThrowableStackTrace newLineChar at: " + i);
            }
        }
        return sb.toString();
    }
}





