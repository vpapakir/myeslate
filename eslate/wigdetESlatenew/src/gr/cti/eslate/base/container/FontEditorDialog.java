package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class FontEditorDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;

    Locale locale;
    ResourceBundle fontDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    protected int returnCode = DIALOG_CANCELLED;
    int dialogWidth = 300;
    Font font;

    JComboBox fontType, fontSize;
    JLabel fontDemo;
    JToggleButton boldButton, italicButton;
    JPanel fontDemoPanel;
    int totalDialogWidth = 0;

    public FontEditorDialog(java.awt.Dialog parent, Font initialFont) {
        super(parent, true);
        initDialog(initialFont);
    }

    public FontEditorDialog(java.awt.Frame parent, Font initialFont) {
        super(parent, true);
        initDialog(initialFont);
    }

    void initDialog(Font initialFont) {
        this.font = initialFont;

        locale = Locale.getDefault();
        fontDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.FontDialogBundle", locale);
        if (fontDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.FontDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(fontDialogBundle.getString("DialogTitle"));

        /* Create the "allFontPanel" with BoxLayout. It contains the "fontPanel" and
         * the "fontDemoPanel"
         */
        JPanel allFontPanel = new JPanel(true);
        allFontPanel.setLayout(new BoxLayout(allFontPanel, BoxLayout.Y_AXIS));

        /* The "fontPanel" with BoxLayout.
         */
        JPanel  fontPanel = new JPanel(true);
        fontPanel.setLayout(new BoxLayout(fontPanel, BoxLayout.X_AXIS));

        /* Create the fontTypePanel with FlowLayout.
         */
        JPanel fontTypePanel = new JPanel(true);
        fontTypePanel.setLayout(new BoxLayout(fontTypePanel, BoxLayout.X_AXIS));

        fontType = new JComboBox();
        String[] fontNames = Toolkit.getDefaultToolkit().getFontList();
        for (int i=0; i<fontNames.length; i++)
            fontType.addItem(fontNames[i]);

        fontType.setSelectedItem(font.getName());

        fontType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String fontName = (String) fontType.getSelectedItem();
                font = new Font(fontName, font.getStyle(), font.getSize());
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(font);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });

        fontType.setPreferredSize(new Dimension(100, 21));
        JLabel lb = new JLabel(fontDialogBundle.getString("Font"));
//        if (localeIsGreek)
//            lb.setFont(greekUIFont);
        FontMetrics lbFm = getToolkit().getFontMetrics(lb.getFont());
        int lbWidth = lbFm.stringWidth(lb.getText());
        fontTypePanel.add(lb);
        fontTypePanel.add(Box.createHorizontalStrut(5));
        fontTypePanel.add(fontType);
        fontTypePanel.setMaximumSize(new Dimension(100+5+lbWidth, 40));
        fontTypePanel.setPreferredSize(new Dimension(100+5+lbWidth, 40));
        fontTypePanel.setMinimumSize(new Dimension(100+5+lbWidth, 40));
        totalDialogWidth = 100+5+lbWidth;

        /* Create the "fontSize" panel with FlowLayout.
         */
        JPanel fontSizePanel = new JPanel(true);
        fontSizePanel.setLayout(new BoxLayout(fontSizePanel, BoxLayout.X_AXIS));

        lb = new JLabel(fontDialogBundle.getString("Size"));
//        if (localeIsGreek)
//            lb.setFont(greekUIFont);
        lbWidth = lbFm.stringWidth(lb.getText());
        fontSizePanel.add(lb);
        fontSizePanel.add(Box.createHorizontalStrut(5));
        fontSize = new JComboBox();
        fontSize.addItem("7");
        fontSize.addItem("8");
        fontSize.addItem("9");
        fontSize.addItem("10");
        fontSize.addItem("12");
        fontSize.addItem("14");
        fontSize.addItem("16");
        fontSize.addItem("18");
        fontSize.addItem("20");
        fontSize.addItem("24");
        fontSize.addItem("36");
        fontSize.addItem("48");

        fontSize.setSelectedItem(new Integer(font.getSize()).toString());
        fontSize.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int fntSize = new Integer((String) fontSize.getSelectedItem()).intValue();
                font = new Font(font.getName(), font.getStyle(), fntSize);
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(font);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });

        fontSize.setPreferredSize(new Dimension(40, 21));
        fontSizePanel.setMaximumSize(new Dimension(40+lbWidth+5, 40));
        fontSizePanel.setPreferredSize(new Dimension(40+lbWidth+5, 40));
        fontSizePanel.setMinimumSize(new Dimension(40+lbWidth+5, 40));
        fontSizePanel.add(fontSize);
        totalDialogWidth = totalDialogWidth + 40+lbWidth+5;

        /* Create the fontPropertyPanel which holds the "Bold" and "Italic" toggle buttons.
         */
        JPanel fontPropertyPanel = new JPanel(true);
        BoxLayout bl32 = new BoxLayout(fontPropertyPanel, BoxLayout.X_AXIS);
        fontPropertyPanel.setLayout(bl32);

        boldButton = new JToggleButton(new ImageIcon(getClass().getResource("images/bold.gif")), false);
        italicButton = new JToggleButton(new ImageIcon(getClass().getResource("images/italic.gif")), false);

        /* Initialize the state of the toggle buttons, depending on the "currentFont".
         */
        if (font.getStyle() == Font.BOLD)
            boldButton.setSelected(true);
        if (font.getStyle() == Font.ITALIC)
            italicButton.setSelected(true);
        if (font.getStyle() == (Font.BOLD + Font.ITALIC)) {
            boldButton.setSelected(true);
            italicButton.setSelected(true);
        }
        boldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int style = Font.PLAIN;
                if (boldButton.isSelected())
                    style = style + Font.BOLD;
                if (italicButton.isSelected())
                    style = style + Font.ITALIC;
                font = new Font(font.getName(), style, font.getSize());
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(font);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });
        italicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int style = Font.PLAIN;
                if (boldButton.isSelected())
                    style = style + Font.BOLD;
                if (italicButton.isSelected())
                    style = style + Font.ITALIC;
                font = new Font(font.getName(), style, font.getSize());
//                System.out.println("New font: " + currentFont.getName() + ", " + currentFont.getStyle() + ", " + currentFont.getSize());
                fontDemo.setFont(font);
                fontDemo.paintImmediately(fontDemo.getVisibleRect());
            }
        });


        boldButton.setPreferredSize(new Dimension(53, 25));
        italicButton.setPreferredSize(new Dimension(53, 25));
        boldButton.setAlignmentY(CENTER_ALIGNMENT);
        italicButton.setAlignmentY(CENTER_ALIGNMENT);
        fontPropertyPanel.add(boldButton);
        fontPropertyPanel.add(Box.createHorizontalStrut(5));
        fontPropertyPanel.add(italicButton);
        fontPropertyPanel.setMaximumSize(new Dimension(130, 40));
        fontPropertyPanel.setMinimumSize(new Dimension(130, 40));
        fontPropertyPanel.setPreferredSize(new Dimension(130, 40));
        totalDialogWidth = totalDialogWidth + 130;

        /* Add "fontTypePanel" and "fontSizePanel" to the "fontPanel".
         */
//        fontPanel.add(Box.createHorizontalStrut(5));
        fontPanel.add(Box.createGlue());
        fontPanel.add(fontTypePanel);
        fontPanel.add(Box.createHorizontalStrut(10));
        totalDialogWidth = totalDialogWidth + 10;
        fontPanel.add(fontSizePanel);
        fontPanel.add(Box.createHorizontalStrut(10));
        totalDialogWidth = totalDialogWidth + 10;
        fontPanel.add(fontPropertyPanel);
        fontPanel.add(Box.createGlue());
        fontPanel.setAlignmentY(CENTER_ALIGNMENT);
//        fontPanel.setAlignmentX(CENTER_ALIGNMENT);

        allFontPanel.add(fontPanel);

        /* Create the demo Panel which displays an example of the active font.
         */
        fontDemoPanel = new JPanel(true);
        fontDemoPanel.setLayout(new BoxLayout(fontDemoPanel, BoxLayout.Y_AXIS));
        fontDemo = new JLabel(fontDialogBundle.getString("Demo"));
        fontDemo.setFont(font);
        fontDemo.setHorizontalAlignment(SwingConstants.CENTER);
        fontDemo.setAlignmentX(CENTER_ALIGNMENT);

        fontDemoPanel.add(Box.createGlue());
        fontDemoPanel.add(fontDemo);
        fontDemoPanel.add(Box.createGlue());
        allFontPanel.add(fontDemoPanel);

        // The button panel (CLOSE)
        final JButton closeButton = new JButton(fontDialogBundle.getString("Close"));
//        if (localeIsGreek)
//            closeButton.setFont(greekUIFont);
        Color color128 = new Color(0, 0, 128);
        closeButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        closeButton.setMaximumSize(buttonSize);
        closeButton.setPreferredSize(buttonSize);
        closeButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        closeButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setMaximumSize(new Dimension(200, 45));

        buttonPanel.setBorder(new EmptyBorder(10,5,5,5));

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(allFontPanel);
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = closeButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = closeButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        // COMBOBOX key listener (needs special listener, because the KeyStroke mechanism fails
        // when it has the focus.
        fontType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (fontType.isPopupVisible()) return;
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });
        fontSize.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (fontSize.isPopupVisible()) return;
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });

        getRootPane().setDefaultButton(closeButton);
    }

    public void showDialog(Component comp) {
        pack();
        int prevFontDemoPanelHeight = fontDemoPanel.getSize().height;
        fontDemoPanel.setMaximumSize(new Dimension(totalDialogWidth, 50)); //35));
        fontDemoPanel.setMinimumSize(new Dimension(totalDialogWidth, 50)); //35));
        fontDemoPanel.setPreferredSize(new Dimension(totalDialogWidth, 50)); //35));
        fontDemoPanel.invalidate();
        fontDemoPanel.revalidate();
        setSize(totalDialogWidth+10, getSize().height + (50-prevFontDemoPanelHeight));
        doLayout();
        validate();
        setResizable(false);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (comp == null || !comp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = comp.getBounds();
            java.awt.Point compLocation = comp.getLocationOnScreen();
    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
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
        fontType.requestFocus();
        show();
    }

    public Font getFont() {
        return font;
    }
}



