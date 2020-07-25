package gr.cti.eslate.base.container;

import gr.cti.eslate.imageChooser.ImageChooser;
import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.MathEF;

public class UIDialog extends JDialog {
    public static final int COLOR_BORDER = 1;
    public static final int ICON_BORDER = 2;
    public static final int NO_BORDER = 0;
    public static final int OUTER_BORDER_NONE = 0;
    public static final int OUTER_BORDER_RAISED = 1;
    public static final int OUTER_BORDER_LOWERED = 2;
    public static final int BACKGROUND_NONE = 0;
//    public static final int COLORED_BACKGROUND = 1;
    public static final int ICON_COLORED_BACKGROUND = 2;
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    protected Color borderColor = Color.orange;
    protected NewRestorableImageIcon borderIcon;
    protected int borderType = ICON_BORDER;
//    protected String dialogTitle = "";
    protected Insets borderInsets = new Insets(0, 0, 0, 0);
    protected int returnCode = DIALOG_CANCELLED;
    protected int outerBorderType = OUTER_BORDER_NONE;
    protected int backgroundType = BACKGROUND_NONE;
    protected Color backgroundColor = Color.white;
    protected NewRestorableImageIcon backgroundIcon = null;
    JButton okButton;

    Locale locale;
    ResourceBundle uiDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;

    ESlateFileDialog fileDialog = null;
//    JLabel textlb;
//    JTextField titleField;
    JLabel samplelb, bSamplelb;
//    JLabel upLabel, downLabel, rightLabel, leftLabel;
//    NumericTextField upField, downField, leftField, rightField ;
    SpinField upSpin, downSpin, leftSpin, rightSpin;
    NoBorderButton insetsButton, chooseColor, chooseBorderIconButton;
    JRadioButton colorButton, iconButton;
    JRadioButton noneButton;
    JLabel bgrColorLb, bgrIconLb;
    JCheckBox bgrNoneButton;
    JRadioButton useOuterBorder, raisedBorder, loweredBorder;
    NoBorderButton bgrChooseColor, chooseBgrIconButton;
    ImageChooser imgChooser = null;
    JRadioButton centerIconButton, fitIconButton, tileIconButton;
    int backgroundIconDisplayMode = ImageChooser.CENTER_IMAGE;
    NoBorderButton saveBgrIconButton, saveBorderIconButton;
    NoBorderButton delBgrIconButton, delBorderIconButton;
    /* The top level Frame of the application the UIDialog plays in */
    Frame parentFrame = null;
    ESlateContainer container;

    public UIDialog(Frame f, ESlateContainer container) {
        super(f, true);
        parentFrame = f;
        this.container = container;

        locale = Locale.getDefault();
        uiDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.UIDialogBundle", locale);
        if (uiDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.UIDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(uiDialogBundle.getString("MWUI"));

/*        textlb = new JLabel(uiDialogBundle.getString("Title"));
        if (localeIsGreek)
            textlb.setFont(greekUIFont);
        FontMetrics fm1 = getToolkit().getFontMetrics(textlb.getFont());
        Dimension d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("Title")), 20);
        textlb.setMaximumSize(d);
        textlb.setPreferredSize(d);
        textlb.setMinimumSize(d);

        titleField = new JTextField();
        if (localeIsGreek)
            titleField.setFont(greekUIFont);
        d = new Dimension(220, 20);
        titleField.setMaximumSize(d);
        titleField.setPreferredSize(d);
        titleField.setMinimumSize(d);

        JPanel titleFieldPanel = new JPanel(true);
        titleFieldPanel.setLayout(new BoxLayout(titleFieldPanel, BoxLayout.X_AXIS));
        titleFieldPanel.add(Box.createGlue());
        titleFieldPanel.add(textlb);
        titleFieldPanel.add(Box.createHorizontalStrut(5));
        titleFieldPanel.add(titleField);
        titleFieldPanel.add(Box.createGlue());
        titleFieldPanel.setBorder(new EmptyBorder(5, 0, 0, 5));
*/
        colorButton = new JRadioButton(uiDialogBundle.getString("Color"));
        iconButton = new JRadioButton(uiDialogBundle.getString("Image"));
        noneButton = new JRadioButton(uiDialogBundle.getString("None"));
/*        if (localeIsGreek) {
            colorButton.setFont(greekUIFont);
            iconButton.setFont(greekUIFont);
            noneButton.setFont(greekUIFont);
        }
*/

/*        colorButton.setFocusPainted(false);
        iconButton.setFocusPainted(false);
        noneButton.setFocusPainted(false);
*/
        FontMetrics fm1 = getToolkit().getFontMetrics(colorButton.getFont());
        int colorStrLength = fm1.stringWidth(uiDialogBundle.getString("Color"));
        int imageStrLength = fm1.stringWidth(uiDialogBundle.getString("Image"));
        int noneStrLength = fm1.stringWidth(uiDialogBundle.getString("None"));
        int maxLength = colorStrLength;
        if (maxLength < imageStrLength)
            maxLength = imageStrLength;
        if (maxLength < noneStrLength)
            maxLength = noneStrLength;
        Dimension d = new Dimension(maxLength+25, 20);
        colorButton.setMaximumSize(d);
        colorButton.setPreferredSize(d);
        colorButton.setMinimumSize(d);
        iconButton.setMaximumSize(d);
        iconButton.setPreferredSize(d);
        iconButton.setMinimumSize(d);
        noneButton.setMaximumSize(d);
        noneButton.setPreferredSize(d);
        noneButton.setMinimumSize(d);

        ButtonGroup bgrp = new ButtonGroup();
        bgrp.add(colorButton);
        bgrp.add(iconButton);
        bgrp.add(noneButton);

        Insets zeroInsets = new Insets(0,0,0,0);
        chooseColor = new NoBorderButton(new ImageIcon(getClass().getResource("images/palette.gif")));
        chooseBorderIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/openFile.gif")));
        saveBorderIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/saveFile.gif")));
        delBorderIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/delete.gif")));

        fm1 = getToolkit().getFontMetrics(chooseColor.getFont());
        Color color128 = new Color(0, 0, 128);
        Dimension buttonSize = new Dimension(19, 17);
        chooseColor.setMaximumSize(buttonSize);
        chooseColor.setPreferredSize(buttonSize);
        chooseColor.setMinimumSize(buttonSize);
        chooseBorderIconButton.setMaximumSize(buttonSize);
        chooseBorderIconButton.setPreferredSize(buttonSize);
        chooseBorderIconButton.setMinimumSize(buttonSize);
        chooseBorderIconButton.setMargin(new Insets(0, 0, 0, 1));
        saveBorderIconButton.setMaximumSize(buttonSize);
        saveBorderIconButton.setPreferredSize(buttonSize);
        saveBorderIconButton.setMinimumSize(buttonSize);
        delBorderIconButton.setMaximumSize(buttonSize);
        delBorderIconButton.setPreferredSize(buttonSize);
        delBorderIconButton.setMinimumSize(buttonSize);
//        chooseColor.setFocusPainted(false);
//        chooseBorderIconButton.setFocusPainted(false);

        Dimension rigidAreaDim = new Dimension(0, 25);
        JPanel colorPanel = new JPanel(true);
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
        colorPanel.add(Box.createRigidArea(rigidAreaDim));
        colorPanel.add(colorButton);
        colorPanel.add(Box.createRigidArea(rigidAreaDim));
        colorPanel.add(chooseColor);
        colorPanel.add(Box.createRigidArea(rigidAreaDim));
        colorPanel.setAlignmentX(LEFT_ALIGNMENT);
//        colorPanel.setBorder(new EtchedBorder());

        JPanel iconPanel = new JPanel(true);
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
        iconPanel.add(Box.createRigidArea(rigidAreaDim));
        iconPanel.add(iconButton);
        iconPanel.add(Box.createRigidArea(rigidAreaDim));
        iconPanel.add(chooseBorderIconButton);
        iconPanel.add(saveBorderIconButton);
        iconPanel.add(delBorderIconButton);
        iconPanel.add(Box.createRigidArea(rigidAreaDim));
        iconPanel.setAlignmentX(LEFT_ALIGNMENT);
//        iconPanel.setBorder(new javax.swing.border.LineBorder(Color.black));

        JPanel nonePanel = new JPanel(true);
        nonePanel.setLayout(new BoxLayout(nonePanel, BoxLayout.X_AXIS));
        nonePanel.add(Box.createRigidArea(rigidAreaDim));
        nonePanel.add(noneButton);
        nonePanel.add(Box.createRigidArea(rigidAreaDim));
        nonePanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel borderTypePanel = new JPanel(true);
        borderTypePanel.setLayout(new BoxLayout(borderTypePanel, BoxLayout.Y_AXIS));
        borderTypePanel.add(colorPanel);
        borderTypePanel.add(iconPanel);
        borderTypePanel.add(nonePanel);

        samplelb = new JLabel();
        d = new Dimension(70, 70);
        samplelb.setMaximumSize(d);
        samplelb.setPreferredSize(d);
        samplelb.setMinimumSize(d);
        samplelb.setOpaque(true);
        samplelb.setAlignmentX(CENTER_ALIGNMENT);
        samplelb.setAlignmentY(CENTER_ALIGNMENT);
        samplelb.setVerticalAlignment(SwingConstants.CENTER);
        samplelb.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel samplelbPanel = new JPanel(true);
        samplelbPanel.setLayout(new BorderLayout());
        samplelbPanel.add(samplelb, BorderLayout.CENTER);
        samplelbPanel.setMaximumSize(d);
        samplelbPanel.setPreferredSize(d);
        samplelbPanel.setMinimumSize(d);
        samplelbPanel.setOpaque(false);
        samplelbPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(1,1,1,1)));


        upSpin = new SpinField(true, uiDialogBundle.getString("Up"), 0); //JPVSpin.VERTICAL_RIGHT);
        upSpin.setMinValue(0);
        upSpin.setHeight(20);
        downSpin = new SpinField(true, uiDialogBundle.getString("Down"), 0); //JPVSpin.VERTICAL_RIGHT);
        downSpin.setMinValue(0);
        downSpin.setHeight(20);
        leftSpin = new SpinField(true, uiDialogBundle.getString("Left"), 0); //JPVSpin.VERTICAL_RIGHT);
        leftSpin.setHeight(20);
        leftSpin.setMinValue(0);
        rightSpin = new SpinField(true, uiDialogBundle.getString("Right"), 0); //JPVSpin.VERTICAL_RIGHT);
        rightSpin.setMinValue(0);
        rightSpin.setHeight(20);

        SpinField[] spins = new SpinField[] {upSpin, downSpin, leftSpin, rightSpin};
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 45, 20);

        JPanel insetsPanel = new JPanel(true);
        ExplicitLayout el = new ExplicitLayout();
        insetsPanel.setLayout(el);
        ExplicitConstraints ec1 = new ExplicitConstraints(upSpin);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(insetsPanel, 0.5d));
        insetsPanel.add(upSpin, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(downSpin);
        ec2.setX(ComponentEF.left(upSpin));
        ec2.setY(ComponentEF.bottom(upSpin));
        insetsPanel.add(downSpin, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(leftSpin);
        ec3.setX(ComponentEF.left(downSpin));
        ec3.setY(ComponentEF.bottom(downSpin));
        insetsPanel.add(leftSpin, ec3);
        ExplicitConstraints ec4 = new ExplicitConstraints(rightSpin);
        ec4.setX(ComponentEF.left(leftSpin));
        ec4.setY(ComponentEF.bottom(leftSpin));
        insetsPanel.add(rightSpin, ec4);
        el.setPreferredLayoutSize(ec2.getWidth(), MathEF.constant(20*4));

        insetsPanel.setAlignmentY(CENTER_ALIGNMENT);
        borderTypePanel.setAlignmentY(CENTER_ALIGNMENT);
        samplelbPanel.setAlignmentY(CENTER_ALIGNMENT);

        JPanel borderPanel = new JPanel(true);
        borderPanel.setLayout(new BoxLayout(borderPanel, BoxLayout.X_AXIS));

        borderPanel.add(Box.createGlue());
        borderPanel.add(borderTypePanel);
        borderPanel.add(Box.createHorizontalStrut(10));
        borderPanel.add(samplelbPanel);
        borderPanel.add(Box.createHorizontalStrut(10));
        borderPanel.add(insetsPanel);
        borderPanel.add(Box.createGlue());

        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(uiDialogBundle.getString("Border"));
        tb1.setTitleColor(titleBorderColor);
//        if (localeIsGreek)
//            tb1.setTitleFont(greekUIFont);
        borderPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,0,5,0)));

        // The outer border panel
        useOuterBorder = new JRadioButton(uiDialogBundle.getString("UseOuterBorder"));
        raisedBorder = new JRadioButton(uiDialogBundle.getString("Raised"));
        loweredBorder = new JRadioButton(uiDialogBundle.getString("Lowered"));

        ButtonGroup bgrp3 = new ButtonGroup();
        bgrp3.add(useOuterBorder);
        bgrp3.add(raisedBorder);
        bgrp3.add(loweredBorder);

/*        if (localeIsGreek) {
            useOuterBorder.setFont(greekUIFont);
            raisedBorder.setFont(greekUIFont);
            loweredBorder.setFont(greekUIFont);
        }
*/
/*        useOuterBorder.setFocusPainted(false);
        raisedBorder.setFocusPainted(false);
        loweredBorder.setFocusPainted(false);
*/
        fm1 = getToolkit().getFontMetrics(useOuterBorder.getFont());
        d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("UseOuterBorder"))+17, 20);
        useOuterBorder.setMaximumSize(d);
        useOuterBorder.setMinimumSize(d);
        useOuterBorder.setPreferredSize(d);
        fm1 = getToolkit().getFontMetrics(raisedBorder.getFont());
        d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("Raised"))+17, 20);
        raisedBorder.setMaximumSize(d);
        raisedBorder.setMinimumSize(d);
        raisedBorder.setPreferredSize(d);
        fm1 = getToolkit().getFontMetrics(loweredBorder.getFont());
        d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("Lowered"))+17, 20);
        loweredBorder.setMaximumSize(d);
        loweredBorder.setMinimumSize(d);
        loweredBorder.setPreferredSize(d);

        JPanel outerBorderPanel = new JPanel(true);
        outerBorderPanel.setLayout(new BoxLayout(outerBorderPanel, BoxLayout.X_AXIS));
        outerBorderPanel.add(Box.createGlue());
        outerBorderPanel.add(useOuterBorder);
        outerBorderPanel.add(Box.createHorizontalStrut(22)); //8));
        outerBorderPanel.add(raisedBorder);
        outerBorderPanel.add(Box.createHorizontalStrut(22)); //8));
        outerBorderPanel.add(loweredBorder);
        outerBorderPanel.add(Box.createGlue());

        TitledBorder tb2 = new TitledBorder(uiDialogBundle.getString("OuterBorder"));
        tb2.setTitleColor(titleBorderColor);
//        if (localeIsGreek)
//            tb2.setTitleFont(greekUIFont);
        outerBorderPanel.setBorder(new CompoundBorder(tb2, new EmptyBorder(0,0,5,0)));

        // The background panel
        bgrColorLb = new JLabel(uiDialogBundle.getString("Color1"));
        bgrIconLb = new JLabel(uiDialogBundle.getString("Image1"));
        bgrNoneButton = new JCheckBox(uiDialogBundle.getString("None"));
/*        if (localeIsGreek) {
            bgrColorLb.setFont(greekUIFont);
            bgrIconLb.setFont(greekUIFont);
            bgrNoneButton.setFont(greekUIFont);
        }
*/
        fm1 = getToolkit().getFontMetrics(bgrColorLb.getFont());
        colorStrLength = fm1.stringWidth(uiDialogBundle.getString("Color1"));
        imageStrLength = fm1.stringWidth(uiDialogBundle.getString("Image1"));
        noneStrLength = fm1.stringWidth(uiDialogBundle.getString("None"));
        maxLength = colorStrLength;
        if (maxLength < imageStrLength)
            maxLength = imageStrLength;
        if (maxLength < noneStrLength)
            maxLength = noneStrLength;
        d = new Dimension(maxLength+5, 20);
        bgrColorLb.setMaximumSize(d);
        bgrColorLb.setPreferredSize(d);
        bgrColorLb.setMinimumSize(d);
        bgrIconLb.setMaximumSize(d);
        bgrIconLb.setPreferredSize(d);
        bgrIconLb.setMinimumSize(d);
        bgrNoneButton.setMaximumSize(d);
        bgrNoneButton.setPreferredSize(d);
        bgrNoneButton.setMinimumSize(d);

        bgrChooseColor = new NoBorderButton(new ImageIcon(getClass().getResource("images/palette.gif")));
        chooseBgrIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/openFile.gif")));
        saveBgrIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/saveFile.gif")));
        delBgrIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/delete.gif")));

        fm1 = getToolkit().getFontMetrics(chooseColor.getFont());
        bgrChooseColor.setMaximumSize(buttonSize);
        bgrChooseColor.setPreferredSize(buttonSize);
        bgrChooseColor.setMinimumSize(buttonSize);
        chooseBgrIconButton.setMaximumSize(buttonSize);
        chooseBgrIconButton.setPreferredSize(buttonSize);
        chooseBgrIconButton.setMinimumSize(buttonSize);
        chooseBgrIconButton.setMargin(new Insets(0, 0, 0, 1));
        saveBgrIconButton.setMaximumSize(buttonSize);
        saveBgrIconButton.setPreferredSize(buttonSize);
        saveBgrIconButton.setMinimumSize(buttonSize);
        delBgrIconButton.setMaximumSize(buttonSize);
        delBgrIconButton.setPreferredSize(buttonSize);
        delBgrIconButton.setMinimumSize(buttonSize);
//        bgrChooseColor.setFocusPainted(false);
//        chooseBgrIconButton.setFocusPainted(false);

        JPanel bgrColorPanel = new JPanel(true);
        bgrColorPanel.setLayout(new BoxLayout(bgrColorPanel, BoxLayout.X_AXIS));
        bgrColorPanel.add(Box.createRigidArea(rigidAreaDim));
        bgrColorPanel.add(bgrColorLb);
        bgrColorPanel.add(Box.createRigidArea(rigidAreaDim));
        bgrColorPanel.add(bgrChooseColor);
        bgrColorPanel.add(Box.createRigidArea(rigidAreaDim));
        bgrColorPanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel bgrIconPanel = new JPanel(true);
        bgrIconPanel.setLayout(new BoxLayout(bgrIconPanel, BoxLayout.X_AXIS));
        bgrIconPanel.add(Box.createRigidArea(rigidAreaDim));
        bgrIconPanel.add(bgrIconLb);
        bgrIconPanel.add(Box.createRigidArea(rigidAreaDim));
        bgrIconPanel.add(chooseBgrIconButton);
        bgrIconPanel.add(saveBgrIconButton);
        bgrIconPanel.add(delBgrIconButton);
        bgrIconPanel.add(Box.createRigidArea(rigidAreaDim));
        bgrIconPanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel bgrNonePanel = new JPanel(true);
        bgrNonePanel.setLayout(new BoxLayout(bgrNonePanel, BoxLayout.X_AXIS));
        bgrNonePanel.add(Box.createRigidArea(rigidAreaDim));
        bgrNonePanel.add(bgrNoneButton);
        bgrNonePanel.add(Box.createRigidArea(rigidAreaDim));
        bgrNonePanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel backgroundTypePanel = new JPanel(true);
        backgroundTypePanel.setLayout(new BoxLayout(backgroundTypePanel, BoxLayout.Y_AXIS));
        backgroundTypePanel.add(bgrNonePanel);
        backgroundTypePanel.add(bgrColorPanel);
        backgroundTypePanel.add(bgrIconPanel);

        bSamplelb = new JLabel();
        d = new Dimension(70, 70);
        bSamplelb.setMaximumSize(d);
        bSamplelb.setPreferredSize(d);
        bSamplelb.setMinimumSize(d);
        bSamplelb.setOpaque(true);
        bSamplelb.setAlignmentX(CENTER_ALIGNMENT);
        bSamplelb.setAlignmentY(CENTER_ALIGNMENT);
        bSamplelb.setVerticalAlignment(SwingConstants.CENTER);
        bSamplelb.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bSamplelbPanel = new JPanel(true);
        bSamplelbPanel.setLayout(new BorderLayout());
        bSamplelbPanel.add(bSamplelb, BorderLayout.CENTER);
        bSamplelbPanel.setMaximumSize(d);
        bSamplelbPanel.setPreferredSize(d);
        bSamplelbPanel.setMinimumSize(d);
        bSamplelbPanel.setOpaque(false);
        bSamplelbPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(1,1,1,1)));

        centerIconButton = new JRadioButton(uiDialogBundle.getString("Center"));
        fitIconButton = new JRadioButton(uiDialogBundle.getString("Fit"));
        tileIconButton = new JRadioButton(uiDialogBundle.getString("Tile"));
/*        if (localeIsGreek) {
            centerIconButton.setFont(greekUIFont);
            fitIconButton.setFont(greekUIFont);
            tileIconButton.setFont(greekUIFont);
        }
*/
/*        centerIconButton.setFocusPainted(false);
        fitIconButton.setFocusPainted(false);
        tileIconButton.setFocusPainted(false);
*/
        fm1 = getToolkit().getFontMetrics(centerIconButton.getFont());
        int maxWidth = fm1.stringWidth(centerIconButton.getText());
        maxWidth = (maxWidth < fm1.stringWidth(downSpin.label.getText()))? fm1.stringWidth(fitIconButton.getText()):maxWidth;
        maxWidth = (maxWidth < fm1.stringWidth(rightSpin.label.getText()))? fm1.stringWidth(tileIconButton.getText()):maxWidth;

        d = new Dimension(maxWidth+25, 20);
        centerIconButton.setMaximumSize(d);
        centerIconButton.setPreferredSize(d);
        centerIconButton.setMinimumSize(d);
        fitIconButton.setMaximumSize(d);
        fitIconButton.setPreferredSize(d);
        fitIconButton.setMinimumSize(d);
        tileIconButton.setMaximumSize(d);
        tileIconButton.setPreferredSize(d);
        tileIconButton.setMinimumSize(d);

        JPanel centerIconButtonPanel = new JPanel(true);
        centerIconButtonPanel.setLayout(new BoxLayout(centerIconButtonPanel, BoxLayout.X_AXIS));
        centerIconButtonPanel.add(centerIconButton);

        JPanel fitIconButtonPanel = new JPanel(true);
        fitIconButtonPanel.setLayout(new BoxLayout(fitIconButtonPanel, BoxLayout.X_AXIS));
        fitIconButtonPanel.add(fitIconButton);

        JPanel tileIconButtonPanel = new JPanel(true);
        tileIconButtonPanel.setLayout(new BoxLayout(tileIconButtonPanel, BoxLayout.X_AXIS));
        tileIconButtonPanel.add(tileIconButton);

        JPanel imageModePanel = new JPanel(true);
        imageModePanel.setLayout(new BoxLayout(imageModePanel, BoxLayout.Y_AXIS));
        imageModePanel.add(centerIconButtonPanel);
        imageModePanel.add(fitIconButtonPanel);
        imageModePanel.add(tileIconButtonPanel);

        JPanel backgroundPanel = new JPanel(true);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.X_AXIS));

//        bBorderPanel.add(Box.createGlue());
        backgroundPanel.add(backgroundTypePanel);
        backgroundPanel.add(Box.createHorizontalStrut(10));
        backgroundPanel.add(bSamplelbPanel);
        backgroundPanel.add(Box.createHorizontalStrut(10));
        backgroundPanel.add(imageModePanel);
        backgroundPanel.add(Box.createGlue());

        TitledBorder tb3 = new TitledBorder(uiDialogBundle.getString("Background"));
        tb3.setTitleColor(titleBorderColor);
//        if (localeIsGreek)
//            tb3.setTitleFont(greekUIFont);
        backgroundPanel.setBorder(new CompoundBorder(tb3, new EmptyBorder(0,0,5,0)));

        // The button panel (OK, CANCEL)
        okButton = new JButton(uiDialogBundle.getString("OK"));
//        if (localeIsGreek)
//            okButton.setFont(greekUIFont);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(uiDialogBundle.getString("Cancel"));
//        if (localeIsGreek)
//            cancelButton.setFont(greekUIFont);
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//        mainPanel.add(titleFieldPanel);
//        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(borderPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(outerBorderPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(backgroundPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

/*        titleField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                dialogTitle = titleField.getText();
            }
        });
*/
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Color button was selected");
                borderTypeChangedAction();
            }
        });
        iconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Icon button was selected");
                borderTypeChangedAction();
            }
        });

        noneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("None button was selected");
                borderTypeChangedAction();
            }
        });

        chooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = ESlateContainerUtils.showColorDialog(UIDialog.this, uiDialogBundle.getString("borderColor"), borderColor);
//                Color color = JColorChooser.showDialog(new JFrame(), uiDialogBundle.getString("borderColor"), borderColor);
                if (color != null)
                    borderColor = color;
                samplelb.setBackground(borderColor);
            }
        });
        chooseBorderIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileDialog == null) {
                    if (ESlateContainer.iconFileDialog != null)
                        fileDialog = ESlateContainer.iconFileDialog;
                    else{
                        fileDialog = new ESlateFileDialog(parentFrame, uiDialogBundle.getString("selectBorderImage"), ESlateFileDialog.LOAD);
                        ESlateContainer.iconFileDialog = fileDialog;
                    }
                }

                if (fileDialog.isShowing()) {
                    return;
                }
                fileDialog.setFile("*.gif; *.jpg; *.png");
                fileDialog.setTitle(uiDialogBundle.getString("selectBorderImage"));
                fileDialog.setMode(ESlateFileDialog.LOAD);
                String[] extensions = new String[3];
                extensions[0] = "gif";
                extensions[1] = "jpg";
                extensions[2] = "png";
                fileDialog.setDefaultExtension(extensions);
                fileDialog.show();

                String imageFileName = fileDialog.getFile();
                if (imageFileName == null) {
//                    borderIcon = null;
                    setBorderIcon(null);
                }else{
                    String currDirectory = fileDialog.getDirectory();
                    imageFileName = currDirectory+imageFileName;
//                    borderIcon = new RestorableImageIcon(imageFileName);
                    setBorderIcon(new NewRestorableImageIcon(imageFileName));
                }
                samplelb.setIcon(borderIcon);
            }
        });

        saveBorderIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportIcon(borderIcon, uiDialogBundle.getString("SaveBorderIcon"));
            }
        });

        delBorderIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBorderIcon(null);
                colorButton.doClick(); //setSelected(true);
            }
        });

        upSpin.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent e) {
                int top = ((Number) e.getValue()).intValue();
                if (top < 0)
                    borderInsets.top = 0;
                else
                    borderInsets.top = top;
            }
        });

        downSpin.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent e) {
                int bottom = ((Number) e.getValue()).intValue();
                if (bottom < 0)
                    borderInsets.bottom = 0;
                else
                    borderInsets.bottom = bottom;
            }
        });

        leftSpin.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent e) {
                int left = ((Number) e.getValue()).intValue();
                if (left < 0)
                    borderInsets.left = 0;
                else
                    borderInsets.left = left;
            }
        });

        rightSpin.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent e) {
                int right = ((Number) e.getValue()).intValue();
                if (right < 0)
                    borderInsets.right = 0;
                else
                    borderInsets.right = right;
            }
        });

/*        upField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try{
                    int top = new Integer(upField.getText()).intValue();
                    if (top < 0) {
                        borderInsets.top = 0;
                        upField.setText(0);
                    }
                    borderInsets.top = top;
                    upField.setText(top);
//                    upField.setValue(top);
                }catch (Exception exc) {
                    borderInsets.top = 0;
                    upField.setText(0);
                }
            }
        });
        upSpin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int val = upField.getValue();
                val += ((e.getModifiers() == JPVSpin.INCREMENT) ? 1 : -1);
                if (val >= 0) {
                    upField.setText(val);
                    borderInsets.top = val;
                }
            }
        });

        downField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try{
                    int bottom = new Integer(downField.getText()).intValue();
                    if (bottom < 0) {
                        borderInsets.bottom = 0;
                        downField.setText(0);
                    }
                    borderInsets.bottom = bottom;
//                    downField.setValue(bottom);
                    downField.setText(bottom);
                }catch (Exception exc) {
                    borderInsets.bottom = 0;
                    downField.setText(0);
                }
            }
        });
        downSpin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int val = downField.getValue();
                val += ((e.getModifiers() == JPVSpin.INCREMENT) ? 1 : -1);
                if (val >= 0) {
                    downField.setText(val);
                    borderInsets.bottom = val;
                }
            }
        });

        leftField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try{
                    int left = new Integer(leftField.getText()).intValue();
                    if (left < 0) {
                        borderInsets.left = 0;
                        leftField.setText(0);
                    }
                    borderInsets.left = left;
//                    leftField.setValue(left);
                    leftField.setText(left);
                }catch (Exception exc) {
                    borderInsets.left = 0;
                    leftField.setText(0);
                }
            }
        });
        leftSpin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int val = leftField.getValue();
                val += ((e.getModifiers() == JPVSpin.INCREMENT) ? 1 : -1);
                if (val >= 0) {
                    leftField.setText(val);
                    borderInsets.left = val;
                }
            }
        });

        rightField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try{
                    int right = new Integer(rightField.getText()).intValue();
                    if (right < 0) {
                        borderInsets.right = 0;
                        rightField.setText(0);
                    }
                    borderInsets.right = right;
//                    rightField.setValue(right);
                    rightField.setText(right);
                }catch (Exception exc) {
                    borderInsets.right = 0;
                    rightField.setText(0);
                }
            }
        });
        rightSpin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int val = rightField.getValue();
                val += ((e.getModifiers() == JPVSpin.INCREMENT) ? 1 : -1);
                if (val >= 0) {
                    rightField.setText(val);
                    borderInsets.right = val;
                }
            }
        });
*/
/*        bColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Background color button was selected");
                backgroundTypeChangedAction();
            }
        });

        bIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Background icon button was selected");
                backgroundTypeChangedAction();
            }
        });
*/
        bgrNoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Background none button was selected");
                backgroundTypeChangedAction();
            }
        });

        bgrChooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Color color = JColorChooser.showDialog(new JFrame(), uiDialogBundle.getString("backgroundColor"), backgroundColor);
                Color color = ESlateContainerUtils.showColorDialog(UIDialog.this, uiDialogBundle.getString("backgroundColor"), backgroundColor);
                if (color != null)
                    backgroundColor = color;
                bSamplelb.setBackground(backgroundColor);
            }
        });
        chooseBgrIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (imgChooser == null)
                    imgChooser = new ImageChooser();
                if (backgroundIcon != null)
                    imgChooser.setDisplayOption(backgroundIconDisplayMode);
                else
                    imgChooser.setDisplayOption(ImageChooser.NO_IMAGE);
                if (ESlateContainer.iconFileDialog != null) {
                    imgChooser.setCurrentDirectory(ESlateContainer.iconFileDialog.getDirectory());
                }
                JDialog dialog = imgChooser.createDialog(UIDialog.this);
                if (imgChooser.showInDialog(dialog, UIDialog.this) == imgChooser.DIALOG_OK) {
                    if (imgChooser.getDisplayOption() == ImageChooser.NO_IMAGE ||
                       imgChooser.getPathToImage() == null) {
//                        backgroundIcon = null;
                        setBackgroundIcon(null);
                        bSamplelb.setIcon(null);
                        backgroundTypeChangedAction();
                    }else{
                        backgroundIconDisplayMode = imgChooser.getDisplayOption();
//                        backgroundIcon = new RestorableImageIcon(imgChooser.getPathToImage());
                        setBackgroundIcon(new NewRestorableImageIcon(imgChooser.getPathToImage()));
                        bSamplelb.setIcon(backgroundIcon);
                        backgroundTypeChangedAction();
                    }
                    if (imgChooser.getCurrentDirectory() != null) {
                        if (ESlateContainer.iconFileDialog == null)
                            ESlateContainer.iconFileDialog = new ESlateFileDialog(new JFrame());
                        ESlateContainer.iconFileDialog.setDirectory(imgChooser.getCurrentDirectory());
                    }
                }


/*                if (fileDialog == null)
                    fileDialog = new ESlateFileDialog(new JFrame(), uiDialogBundle.getString("selectBackgroundImage"), ESlateFileDialog.LOAD);

                if (fileDialog.isShowing()) {
                    return;
                }
                fileDialog.setFile("*.gif; *.jpg");
                String[] extensions = new String[2];
                extensions[0] = "gif";
                extensions[1] = "jpg";
                fileDialog.setDefaultExtension(extensions);
                fileDialog.show();

                String imageFileName = fileDialog.getFile();
                if (imageFileName == null) {
                    backgroundIcon = null;
                }else{
                    String currDirectory = fileDialog.getDirectory();
                    imageFileName = currDirectory+imageFileName;
                    backgroundIcon = new ImageIcon(imageFileName);
                }
                bSamplelb.setIcon(backgroundIcon);
*/
            }
        });

        saveBgrIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportIcon(backgroundIcon, uiDialogBundle.getString("SaveBgrIcon"));
            }
        });

        delBgrIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBackgroundIcon(null);;
            }
        });

        useOuterBorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outerBorderTypeChangedAction();
            }
        });

        raisedBorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outerBorderTypeChangedAction();
            }
        });

        loweredBorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outerBorderTypeChangedAction();
            }
        });

        centerIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundIconDisplayMode = ImageChooser.CENTER_IMAGE;
                tileIconButton.setSelected(false);
                fitIconButton.setSelected(false);
            }
        });

        fitIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundIconDisplayMode = ImageChooser.FIT_IMAGE;
                centerIconButton.setSelected(false);
                tileIconButton.setSelected(false);
            }
        });

        tileIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundIconDisplayMode = ImageChooser.TILE_IMAGE;
                centerIconButton.setSelected(false);
                fitIconButton.setSelected(false);
            }
        });


        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_CANCELLED;
                dispose();
            }
        });

        //Initialize the UIDialog
        if (borderType == ICON_BORDER)
            iconButton.setSelected(true);
        else if (borderType == COLOR_BORDER)
            colorButton.setSelected(true);
        else
            noneButton.setSelected(true);
        borderTypeChangedAction();

        if (outerBorderType == OUTER_BORDER_RAISED)
            raisedBorder.setSelected(true);
        else if (outerBorderType == OUTER_BORDER_LOWERED)
            loweredBorder.setSelected(true);
        else
            useOuterBorder.setSelected(true);

        if (backgroundType == ICON_COLORED_BACKGROUND)
            bgrNoneButton.setSelected(false);
/*        else if (backgroundType == COLORED_BACKGROUND)
            bColorButton.setSelected(true);
*/        else
            bgrNoneButton.setSelected(true);
        backgroundTypeChangedAction();

//        setDialogTitle(dialogTitle);
        setBorderInsets(borderInsets);

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorButton.requestFocus();
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void showDialog(Component comp) {
        pack();
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
        getRootPane().setDefaultButton(okButton);
        colorButton.requestFocus();
        show();
    }

/*    public void setDialogTitle(String title) {
        dialogTitle = title;
//        titleField.setText(dialogTitle);
    }

    public String getDialogTitle() {
        return dialogTitle;
    }
*/
    public void setBorderIcon(NewRestorableImageIcon icon) {
        borderIcon = icon;
        saveBorderIconButton.setEnabled(borderIcon != null);
        delBorderIconButton.setEnabled(borderIcon != null);
        if (borderType == ICON_BORDER)
            samplelb.setIcon(borderIcon);
    }

    public NewRestorableImageIcon getBorderIcon() {
        return borderIcon;
    }

    public void setBorderColor(Color color) {
        borderColor = color;
        if (borderType == COLOR_BORDER)
            samplelb.setBackground(color);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderType(int borderType) {
        this.borderType = borderType;
        if (borderType == ICON_BORDER)
            iconButton.setSelected(true);
        else if (borderType == COLOR_BORDER)
            colorButton.setSelected(true);
        else{
            noneButton.setSelected(true);
            this.borderType = 0;
        }
        if (!isVisible())
            borderTypeChangedAction();
    }

    public int getBorderType() {
        return borderType;
    }

    public void setBorderInsets(Insets insets) {
        borderInsets = insets;
        upSpin.spin.setValue(new Integer(insets.top));
        downSpin.spin.setValue(new Integer(insets.bottom));
        rightSpin.spin.setValue(new Integer(insets.right));
        leftSpin.spin.setValue(new Integer(insets.left));
/*        upSpin.field.setText(insets.top);
        downSpin.field.setText(insets.bottom);
        rightSpin.field.setText(insets.right);
        leftSpin.field.setText(insets.left);
*/
    }

    public Insets getBorderInsets() {
        return borderInsets;
    }

    public int getReturnCode() {
        return returnCode;
    }

    protected void borderTypeChangedAction() {
        if (colorButton.isSelected()) {
            samplelb.setIcon(null);
            samplelb.setBackground(borderColor);
            chooseColor.setEnabled(true);
            chooseBorderIconButton.setEnabled(false);
            saveBorderIconButton.setEnabled(false);
            delBorderIconButton.setEnabled(false);
            borderType = COLOR_BORDER;
/*            upLabel.setEnabled(true);
            downLabel.setEnabled(true);
            rightLabel.setEnabled(true);
            leftLabel.setEnabled(true);
            upField.setEnabled(true);
*/
            upSpin.setEnabled(true);
///            downField.setEnabled(true);
            downSpin.setEnabled(true);
///            leftField.setEnabled(true);
            leftSpin.setEnabled(true);
///            rightField.setEnabled(true);
            rightSpin.setEnabled(true);
        }else if (iconButton.isSelected()) {
            samplelb.setIcon(borderIcon);
            samplelb.setBackground(Color.lightGray);
            chooseColor.setEnabled(false);
            chooseBorderIconButton.setEnabled(true);
            if (borderIcon != null) {
                saveBorderIconButton.setEnabled(true);
                delBorderIconButton.setEnabled(true);
            }else{
                saveBorderIconButton.setEnabled(false);
                delBorderIconButton.setEnabled(false);
            }
/*            upLabel.setEnabled(true);
            downLabel.setEnabled(true);
            rightLabel.setEnabled(true);
            leftLabel.setEnabled(true);
            upField.setEnabled(true);
*/
            upSpin.setEnabled(true);
///            downField.setEnabled(true);
            downSpin.setEnabled(true);
///            leftField.setEnabled(true);
            leftSpin.setEnabled(true);
///            rightField.setEnabled(true);
            rightSpin.setEnabled(true);
            borderType = ICON_BORDER;
        }else{
            samplelb.setIcon(null);
            samplelb.setBackground(Color.lightGray);
            chooseColor.setEnabled(false);
            chooseBorderIconButton.setEnabled(false);
            saveBorderIconButton.setEnabled(false);
            delBorderIconButton.setEnabled(false);
/*            upLabel.setEnabled(false);
            downLabel.setEnabled(false);
            rightLabel.setEnabled(false);
            leftLabel.setEnabled(false);
            upField.setEnabled(false);
*/
            upSpin.setEnabled(false);
///            downField.setEnabled(false);
            downSpin.setEnabled(false);
///            leftField.setEnabled(false);
            leftSpin.setEnabled(false);
///            rightField.setEnabled(false);
            rightSpin.setEnabled(false);
            borderType = NO_BORDER;
        }
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        if (backgroundType == ICON_COLORED_BACKGROUND)
            bSamplelb.setBackground(color);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundIcon(NewRestorableImageIcon icon) {
        backgroundIcon = icon;
        saveBgrIconButton.setEnabled(backgroundIcon != null);
        delBgrIconButton.setEnabled(backgroundIcon != null);
        if (backgroundType == ICON_COLORED_BACKGROUND)
            bSamplelb.setIcon(backgroundIcon);
    }

    public NewRestorableImageIcon getBackgroundIcon() {
        return backgroundIcon;
    }

    public void setBackgroundIconDisplayMode(int mode) {
        if (mode != ImageChooser.CENTER_IMAGE && mode != ImageChooser.FIT_IMAGE && mode != ImageChooser.TILE_IMAGE)
            return;
        backgroundIconDisplayMode = mode;
        if (backgroundIconDisplayMode == ImageChooser.CENTER_IMAGE) {
            centerIconButton.setSelected(true);
            fitIconButton.setSelected(false);
            tileIconButton.setSelected(false);
        }else if (backgroundIconDisplayMode == ImageChooser.TILE_IMAGE) {
            centerIconButton.setSelected(false);
            fitIconButton.setSelected(false);
            tileIconButton.setSelected(true);
        }else if (backgroundIconDisplayMode == ImageChooser.FIT_IMAGE) {
            centerIconButton.setSelected(false);
            fitIconButton.setSelected(true);
            tileIconButton.setSelected(false);
        }
    }

    public int getBackgroundIconDisplayMode() {
        return backgroundIconDisplayMode;
    }


    public void setBackgroundType(int backgroundType) {
        this.backgroundType = backgroundType;
        if (backgroundType == ICON_COLORED_BACKGROUND)
            bgrNoneButton.setSelected(false);
/*        else if (backgroundType == COLORED_BACKGROUND)
            bColorButton.setSelected(true);
*/        else{
            bgrNoneButton.setSelected(true);
            this.backgroundType = 0;
        }
        if (!isVisible())
            backgroundTypeChangedAction();
    }

    public int getBackgroundType() {
        return backgroundType;
    }

    protected void backgroundTypeChangedAction() {
        if (bgrNoneButton.isSelected()) {
            bSamplelb.setIcon(null);
            bSamplelb.setBackground(Color.lightGray);
            bgrChooseColor.setEnabled(false);
            chooseBgrIconButton.setEnabled(false);
            saveBgrIconButton.setEnabled(false);
            delBgrIconButton.setEnabled(false);
            bgrColorLb.setEnabled(false);
            bgrIconLb.setEnabled(false);
            tileIconButton.setEnabled(false);
            centerIconButton.setEnabled(false);
            fitIconButton.setEnabled(false);
            tileIconButton.setSelected(false);
            centerIconButton.setSelected(false);
            fitIconButton.setSelected(false);
            backgroundType = BACKGROUND_NONE;
        }else{
            bSamplelb.setIcon(backgroundIcon);
            bSamplelb.setBackground(backgroundColor);
            bgrChooseColor.setEnabled(true);
            chooseBgrIconButton.setEnabled(true);
            if (backgroundIcon != null) {
                saveBgrIconButton.setEnabled(true);
                delBgrIconButton.setEnabled(true);
            }else{
                saveBgrIconButton.setEnabled(false);
                delBgrIconButton.setEnabled(false);
            }
            bgrColorLb.setEnabled(true);
            bgrIconLb.setEnabled(true);
            if (backgroundIcon != null) {
                tileIconButton.setEnabled(true);
                centerIconButton.setEnabled(true);
                fitIconButton.setEnabled(true);
                if (backgroundIconDisplayMode == ImageChooser.TILE_IMAGE) {
                    fitIconButton.setSelected(false);
                    centerIconButton.setSelected(false);
                    tileIconButton.setSelected(true);
                }else if (backgroundIconDisplayMode == ImageChooser.FIT_IMAGE) {
                    fitIconButton.setSelected(true);
                    centerIconButton.setSelected(false);
                    tileIconButton.setSelected(false);
                }else if (backgroundIconDisplayMode == ImageChooser.CENTER_IMAGE) {
                    fitIconButton.setSelected(false);
                    centerIconButton.setSelected(true);
                    tileIconButton.setSelected(false);
                }
            }else{
                tileIconButton.setSelected(false);
                centerIconButton.setSelected(false);
                fitIconButton.setSelected(false);
                tileIconButton.setEnabled(false);
                centerIconButton.setEnabled(false);
                fitIconButton.setEnabled(false);
            }
            backgroundType = ICON_COLORED_BACKGROUND;
        }
    }

    public void setOuterBorderType(int outerBorderType) {
        if (outerBorderType >=0 && outerBorderType<=2)
            this.outerBorderType = outerBorderType;
        else
            this.outerBorderType = OUTER_BORDER_NONE;
        if (!isVisible())
            outerBorderTypeChangedAction();
    }

    public int getOuterBorderType() {
        return outerBorderType;
    }

    protected void outerBorderTypeChangedAction() {
        if (useOuterBorder.isSelected())
            outerBorderType = OUTER_BORDER_NONE;
        else if (raisedBorder.isSelected())
            outerBorderType = OUTER_BORDER_RAISED;
        else if (loweredBorder.isSelected())
            outerBorderType = OUTER_BORDER_LOWERED;
    }

    void exportIcon(NewRestorableImageIcon icon, String fileDialogTitle) {
        if (fileDialog == null) {
            if (ESlateContainer.iconFileDialog != null)
                fileDialog = ESlateContainer.iconFileDialog;
            else{
                fileDialog = new ESlateFileDialog(parentFrame, fileDialogTitle, ESlateFileDialog.SAVE);
                ESlateContainer.iconFileDialog = fileDialog;
            }
        }

        if (fileDialog.isShowing()) {
            return;
        }
        fileDialog.setTitle(fileDialogTitle);
        fileDialog.setFile("*.png");
        fileDialog.setMode(ESlateFileDialog.SAVE);
        String[] extensions = new String[1];
        extensions[0] = "png";
        fileDialog.setDefaultExtension(extensions);
        fileDialog.show();

        String imageFileName = fileDialog.getFile();
        if (imageFileName == null) {
            return;
        }else{
            String currDirectory = fileDialog.getDirectory();
            imageFileName = currDirectory+imageFileName;
            try{
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFileName));
                icon.saveImage(NewRestorableImageIcon.PNG, bos);
                bos.close();
            }catch (Throwable thr) {
                DetailedErrorDialog dialog = new DetailedErrorDialog(this);
                String message = uiDialogBundle.getString("UnableToSaveIcon");
                message = message + " \"" + imageFileName + "\". ";

                dialog.setMessage(message);
                dialog.appendThrowableMessage(thr);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDetailedErrorDialog(container, dialog, this, true);
            }
        }
    }
}

