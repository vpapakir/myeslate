package gr.cti.eslate.utils;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.PrintWriter;
import java.io.StringWriter;


public class DetailedErrorDialog extends JDialog
{
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
  
    private static Image eslateLogo = null;
    //protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    //protected Font greekUIFont14 = new Font("Helvetica", Font.PLAIN, 14);
    Locale locale;
    //boolean localeIsGreek = false;
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

    public DetailedErrorDialog() {
        super(new JFrame(), true);
        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
        //contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());
        if (eslateLogo == null) {
          eslateLogo =
            new ImageIcon(getClass().getResource("eslateLogo.gif")).getImage();
        }
        contentFrame.setIconImage(eslateLogo);

        locale = Locale.getDefault();
        detailedErrorDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.utils.DetailedErrorDialogBundle", locale);
        //if (detailedErrorDialogBundle.getClass().getName().equals("gr.cti.eslate.utils.DetailedErrorDialogBundle_el_GR"))
            //localeIsGreek = true;

        setTitle(detailedErrorDialogBundle.getString("Error"));

        errorLabel = new JLabel(javax.swing.UIManager.getIcon("OptionPane.errorIcon"));
        errorLabel.setAlignmentY(CENTER_ALIGNMENT);
        errorLabel.setRequestFocusEnabled(true);

        errorArea = new JTextArea();
        errorArea.setLineWrap(true);
        //errorArea.setBackground(Color.lightGray);
        errorArea.setWrapStyleWord(true);
        //errorArea.setSelectionColor(Color.lightGray);
        errorArea.setEditable(false);
        //errorArea.setSelectedTextColor(errorArea.getForeground());
        errorArea.setMaximumSize(maxErrorAreaSize);
        errorArea.setMinimumSize(new Dimension(200, 20));
        errorArea.setAlignmentY(CENTER_ALIGNMENT);
        errorArea.setRequestFocusEnabled(true);
//        errorArea.setBorder(new javax.swing.border.LineBorder(Color.black));

        JPanel errorPanel = new JPanel(true);
        errorPanel.setLayout(new BorderLayout(10, 0));
        errorPanel.add(errorLabel, BorderLayout.WEST);
        errorPanel.add(errorArea, BorderLayout.CENTER);
        errorPanel.setBorder(new EmptyBorder(5,5,5,5));
        errorPanel.setRequestFocusEnabled(true);

        okButton = new JButton(detailedErrorDialogBundle.getString("OK"));
        //Color color128 = new Color(0, 0, 128);
        //okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);
        okButton.setRequestFocusEnabled(true);

        detailsButton = new JToggleButton(detailedErrorDialogBundle.getString("ShowDetails"));
        detailsButton.setToolTipText(detailedErrorDialogBundle.getString("ShowTip"));
        //detailsButton.setForeground(color128);
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
        //detailsArea.setBackground(Color.lightGray);
        detailsArea.setRequestFocusEnabled(false);

        NoBorderButton copy = new NoBorderButton(new ImageIcon(getClass().getResource("images/copy.gif")));
        //Dimension buttonSize2 = new Dimension(22, 21);
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
//                        int scrollpaneHeight = scrollpane.getPreferredSize().height;
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
        //java.awt.FontMetrics fm = getToolkit().getFontMetrics(errorArea.getFont());
        //int width = fm.stringWidth(errorText);
        Graphics2D g2 = (Graphics2D)getGraphics();
        FontRenderContext frc = g2.getFontRenderContext();
        int width = errorArea.getFont().getStringBounds(errorText, frc).getBounds().width;
        g2.dispose();
        int currentwidth = errorArea.getSize().width;
        if (currentwidth == 0) {
            if (width < 400)
                errorArea.setPreferredSize(new Dimension(width, 20));
            else{
                int height = 20 * (width / 400);
                errorArea.setPreferredSize(new Dimension(400, height));
            }
        }
    }

    public void setDetails(String details) {
        detailsArea.setText(details);
        adjustDetailsButtonStatus();
    }

    public void appendToDetails(String moreDetails) {
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
        detailsArea.append(writer.toString());
        adjustDetailsButtonStatus();
    }

    public void appendThrowableMessage(Throwable thr) {
        String excMsg = thr.getMessage();
        if (excMsg == null) return;
        detailsArea.append(excMsg);
        adjustDetailsButtonStatus();
    }

    protected void adjustDetailsButtonStatus() {
        if (detailsArea.getText() != null && detailsArea.getText().trim().length() != 0)
            detailsButton.setEnabled(true);
        else
            detailsButton.setEnabled(false);
    }
}





