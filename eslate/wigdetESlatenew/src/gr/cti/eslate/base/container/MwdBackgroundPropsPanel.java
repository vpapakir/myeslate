package gr.cti.eslate.base.container;

import gr.cti.eslate.imageChooser.ImageChooser;
import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;
import gr.cti.eslate.utils.ESlateFileDialog;
import gr.cti.eslate.utils.NewRestorableImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


public class MwdBackgroundPropsPanel extends JPanel {
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;
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
	protected NewRestorableImageIcon microworldIcon = null;

    Locale locale;
    ResourceBundle uiDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    private boolean localeIsGreek = false;

    ESlateFileDialog fileDialog = null;
    JLabel samplelb, bSamplelb, iconSampleLabel;
    SpinField upSpin, downSpin, leftSpin, rightSpin;
    NoBorderButton insetsButton, chooseColor, chooseBorderIconButton;
    JRadioButton colorButton, iconButton;
    JRadioButton noneButton;
    JLabel bgrColorLb/*, bgrIconLb*/;
    JCheckBox bgrNoneButton;
    JRadioButton useOuterBorder, raisedBorder, loweredBorder;
    NoBorderButton bgrChooseColor/*, chooseBgrIconButton*/;
    ImageChooser imgChooser = null;
    JRadioButton centerIconButton, fitIconButton, tileIconButton;
    int backgroundIconDisplayMode = ImageChooser.CENTER_IMAGE;
    NoBorderButton /*saveBgrIconButton, */saveBorderIconButton;
    NoBorderButton /*delBgrIconButton,*/ delBorderIconButton;
	IconSelectionPanel bgrIconPanel = null, microworldIconSelectionPanel = null;
    JComboBox lafBox;
    ESlateComposer composer;
    MicroworldPropertiesDialog parentDialog = null;
    /** Determines whether the UI of the dialog is enabed, in order to change the microworld's
     *  background properties.
     */
    private boolean isLocked = false;

    public MwdBackgroundPropsPanel(MicroworldPropertiesDialog dialog) {
        super(true);
        this.parentDialog = dialog;
//        parentFrame = f;
        this.composer = dialog.composer;

        locale = Locale.getDefault();
        uiDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.UIDialogBundle", locale);
//        if (uiDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.UIDialogBundle_el_GR"))
//            localeIsGreek = true;

//        setTitle(uiDialogBundle.getString("MWUI"));

        /* The microworld L&F selection box */
        String[] lafNames = composer.installedLAFs.getNames().toArray();
        lafBox = new JComboBox(lafNames);
        lafBox.setEditable(true);

        JPanel lafPanel = new JPanel(true);
        lafPanel.setLayout(new BorderLayout());
        lafPanel.add(lafBox, BorderLayout.CENTER);

        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb4 = new TitledBorder(uiDialogBundle.getString("L&F"));
        tb4.setTitleColor(titleBorderColor);
        lafPanel.setBorder(new CompoundBorder(tb4, new EmptyBorder(0,5,5,5)));

        colorButton = new JRadioButton(uiDialogBundle.getString("Color"));
        iconButton = new JRadioButton(uiDialogBundle.getString("Image"));
        noneButton = new JRadioButton(uiDialogBundle.getString("None"));

        FontMetrics fm1 = colorButton.getFontMetrics(colorButton.getFont());
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

        fm1 = chooseColor.getFontMetrics(chooseColor.getFont());
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
        Expression maxWidthExp = GroupEF.preferredWidthMax(spins).add(2*SpinField.H_GAP+1);

        JPanel insetsPanel = new JPanel(true);
        ExplicitLayout el = new ExplicitLayout();
        insetsPanel.setLayout(el);
        ExplicitConstraints ec1 = new ExplicitConstraints(upSpin);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(insetsPanel, 0.5d));
        ec1.setWidth(maxWidthExp);
        insetsPanel.add(upSpin, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(downSpin);
        ec2.setX(ComponentEF.left(upSpin));
        ec2.setY(ComponentEF.bottom(upSpin));
        ec2.setWidth(maxWidthExp);
        insetsPanel.add(downSpin, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(leftSpin);
        ec3.setX(ComponentEF.left(downSpin));
        ec3.setY(ComponentEF.bottom(downSpin));
        ec3.setWidth(maxWidthExp);
        insetsPanel.add(leftSpin, ec3);
        ExplicitConstraints ec4 = new ExplicitConstraints(rightSpin);
        ec4.setX(ComponentEF.left(leftSpin));
        ec4.setY(ComponentEF.bottom(leftSpin));
        ec4.setWidth(maxWidthExp);
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
        fm1 = useOuterBorder.getFontMetrics(useOuterBorder.getFont());
        //d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("UseOuterBorder"))+17, 20);
        //useOuterBorder.setMaximumSize(d);
        //useOuterBorder.setMinimumSize(d);
        //useOuterBorder.setPreferredSize(d);
        fm1 = raisedBorder.getFontMetrics(raisedBorder.getFont());
        //d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("Raised"))+17, 20);
        //raisedBorder.setMaximumSize(d);
        //raisedBorder.setMinimumSize(d);
        //raisedBorder.setPreferredSize(d);
        fm1 = loweredBorder.getFontMetrics(loweredBorder.getFont());
        d = new Dimension(fm1.stringWidth(uiDialogBundle.getString("Lowered"))+17, 20);
        //loweredBorder.setMaximumSize(d);
        //loweredBorder.setMinimumSize(d);
        //loweredBorder.setPreferredSize(d);

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
//        bgrIconLb = new JLabel(uiDialogBundle.getString("Image1"));
        bgrNoneButton = new JCheckBox(uiDialogBundle.getString("None"));
/*        if (localeIsGreek) {
            bgrColorLb.setFont(greekUIFont);
            bgrIconLb.setFont(greekUIFont);
            bgrNoneButton.setFont(greekUIFont);
        }
*/
        fm1 = bgrColorLb.getFontMetrics(bgrColorLb.getFont());
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
/*        bgrIconLb.setMaximumSize(d);
        bgrIconLb.setPreferredSize(d);
        bgrIconLb.setMinimumSize(d);
*/
        bgrNoneButton.setMaximumSize(d);
        bgrNoneButton.setPreferredSize(d);
        bgrNoneButton.setMinimumSize(d);

        bgrChooseColor = new NoBorderButton(new ImageIcon(getClass().getResource("images/palette.gif")));

        fm1 = chooseColor.getFontMetrics(chooseColor.getFont());
        bgrChooseColor.setMaximumSize(buttonSize);
        bgrChooseColor.setPreferredSize(buttonSize);
        bgrChooseColor.setMinimumSize(buttonSize);
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

		bgrIconPanel = new IconSelectionPanel(uiDialogBundle.getString("Image1"), d.width);

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
        fm1 = centerIconButton.getFontMetrics(centerIconButton.getFont());
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
        backgroundPanel.setBorder(new CompoundBorder(tb3, new EmptyBorder(0,0,5,0)));

		// The microworld icon panel
		JPanel microworldIconPanel = new JPanel();
		microworldIconPanel.setLayout(new BorderLayout(5, 0));

		microworldIconSelectionPanel = new IconSelectionPanel(uiDialogBundle.getString("SelectIcon"), -1);

		iconSampleLabel = new JLabel();
		d = new Dimension(25, 25);
		iconSampleLabel.setMaximumSize(d);
		iconSampleLabel.setPreferredSize(d);
		iconSampleLabel.setMinimumSize(d);
		iconSampleLabel.setOpaque(true);
		iconSampleLabel.setAlignmentX(CENTER_ALIGNMENT);
		iconSampleLabel.setAlignmentY(CENTER_ALIGNMENT);
		iconSampleLabel.setVerticalAlignment(SwingConstants.CENTER);
		iconSampleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel sampleIconPanel = new JPanel(true);
		sampleIconPanel.setLayout(new BorderLayout());
		sampleIconPanel.add(iconSampleLabel, BorderLayout.CENTER);
//		sampleIconPanel.setMaximumSize(d);
//		sampleIconPanel.setPreferredSize(d);
//		sampleIconPanel.setMinimumSize(d);
		sampleIconPanel.setOpaque(false);
		sampleIconPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(1,1,1,1)));

		microworldIconPanel.add(microworldIconSelectionPanel, BorderLayout.WEST);
		microworldIconPanel.add(sampleIconPanel, BorderLayout.CENTER);

		TitledBorder tb5 = new TitledBorder(uiDialogBundle.getString("MwdIcon"));
		tb5.setTitleColor(titleBorderColor);
		microworldIconPanel.setBorder(new CompoundBorder(tb5, new EmptyBorder(0,0,5,0)));

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

//        mainPanel.add(titleFieldPanel);
//        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lafPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(borderPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(outerBorderPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(backgroundPanel);
		mainPanel.add(Box.createVerticalStrut(5));
		mainPanel.add(microworldIconPanel);

        ExplicitLayout mainLayout = new ExplicitLayout();
        setLayout(mainLayout);
        ExplicitConstraints expCon = new ExplicitConstraints(mainPanel);
        expCon.setOriginX(ExplicitConstraints.CENTER);
        expCon.setX(ContainerEF.widthFraction(mainPanel, 0.5d));
        expCon.setOriginY(ExplicitConstraints.CENTER);
        expCon.setY(ContainerEF.heightFraction(mainPanel, 0.5d));
        add(mainPanel, expCon);
        mainLayout.setPreferredLayoutSize(MathEF.constant(mainPanel.getPreferredSize().width),
                                          MathEF.constant(mainPanel.getPreferredSize().height));

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
                Color color = ESlateContainerUtils.showColorDialog(parentDialog, uiDialogBundle.getString("borderColor"), borderColor);
//                Color color = JColorChooser.showDialog(new JFrame(), uiDialogBundle.getString("borderColor"), borderColor);
                if (color != null)
                    borderColor = color;
                samplelb.setBackground(borderColor);
            }
        });
        chooseBorderIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				if (getFileDialog(uiDialogBundle.getString("selectBorderImage")).isShowing()) {
					return;
				}
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

        bgrNoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Background none button was selected");
                backgroundTypeChangedAction();
            }
        });

        bgrChooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Color color = JColorChooser.showDialog(new JFrame(), uiDialogBundle.getString("backgroundColor"), backgroundColor);
                Color color = ESlateContainerUtils.showColorDialog(parentDialog, uiDialogBundle.getString("backgroundColor"), backgroundColor);
                if (color != null)
                    backgroundColor = color;
                bSamplelb.setBackground(backgroundColor);
            }
        });
        bgrIconPanel.chooseBgrIconButton.addActionListener(new ActionListener() {
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
                JDialog dialog = imgChooser.createDialog(parentDialog);
                if (imgChooser.showInDialog(dialog, parentDialog) == ImageChooser.DIALOG_OK) {
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
            }
        });

        bgrIconPanel.saveBgrIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportIcon(backgroundIcon, uiDialogBundle.getString("SaveBgrIcon"));
            }
        });

        bgrIconPanel.delBgrIconButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBackgroundIcon(null);
            }
        });

		microworldIconSelectionPanel.chooseBgrIconButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getFileDialog(uiDialogBundle.getString("selectMicroworldIcon")).isShowing()) {
					return;
				}
				fileDialog.show();

				String imageFileName = fileDialog.getFile();
				if (imageFileName != null) {
					String currDirectory = fileDialog.getDirectory();
					imageFileName = currDirectory+imageFileName;
//                    borderIcon = new RestorableImageIcon(imageFileName);
					setMicroworldIcon(new NewRestorableImageIcon(imageFileName));
				}
			}
		});

		microworldIconSelectionPanel.saveBgrIconButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportIcon(microworldIcon, uiDialogBundle.getString("SaveMwdIcon"));
			}
		});

		microworldIconSelectionPanel.delBgrIconButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMicroworldIcon(null);
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


        //Initialize the UIDialog
        if (composer.currentView.getMicroworldBorder() != null)
            setBorderInsets(composer.currentView.getMicroworldBorderInsets()); //currentView.microworldBorder.getBorderInsets(scrollPane));
        setBorderType(composer.currentView.borderType);
        if (composer.currentView.borderIcon != null) {
            setBorderIcon(composer.currentView.borderIcon);
        }
        if (composer.currentView.borderColor != null)
            setBorderColor(composer.currentView.borderColor);
        setBackgroundType(composer.currentView.backgroundType);
        if (composer.currentView.getBackgroundIcon(composer.mwdViews, composer.currentlyOpenMwdFile) != null) {
            setBackgroundIcon(composer.currentView.getBackgroundIcon(composer.mwdViews, composer.currentlyOpenMwdFile));
            setBackgroundIconDisplayMode(composer.currentView.backgroundIconDisplayMode);
        }else
            setBackgroundIconDisplayMode(ImageChooser.NO_IMAGE);
        if (composer.currentView.backgroundColor != null)
            setBackgroundColor(composer.currentView.backgroundColor);
//        System.out.println("outerBorderType: " + outerBorderType);
        setOuterBorderType(composer.currentView.outerBorderType);

        String currentLAFName = composer.installedLAFs.getName(composer.microworld.microworldLAFClassName);
        // If the L&F of the microworld is not registered, then display its class name
        if (currentLAFName == null)
            currentLAFName = composer.microworld.microworldLAFClassName;
        setMicroworldLAF(currentLAFName);
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

	public void setMicroworldIcon(NewRestorableImageIcon icon) {
		microworldIcon = icon;
		microworldIconSelectionPanel.saveBgrIconButton.setEnabled(microworldIcon != null);
		microworldIconSelectionPanel.delBgrIconButton.setEnabled(microworldIcon != null);
		iconSampleLabel.setIcon(microworldIcon);
	}

	public NewRestorableImageIcon getMicroworldIcon() {
		return microworldIcon;
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
//        if (!isVisible())
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

            upSpin.setEnabled(true);
            downSpin.setEnabled(true);
            leftSpin.setEnabled(true);
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

            upSpin.setEnabled(true);
            downSpin.setEnabled(true);
            leftSpin.setEnabled(true);
            rightSpin.setEnabled(true);
            borderType = ICON_BORDER;
        }else{
            samplelb.setIcon(null);
            samplelb.setBackground(Color.lightGray);
            chooseColor.setEnabled(false);
            chooseBorderIconButton.setEnabled(false);
            saveBorderIconButton.setEnabled(false);
            delBorderIconButton.setEnabled(false);

            upSpin.setEnabled(false);
            downSpin.setEnabled(false);
            leftSpin.setEnabled(false);
            rightSpin.setEnabled(false);
            borderType = NO_BORDER;
        }
    }

    public void setMicroworldLAF(String laf) {
        lafBox.setSelectedItem(laf);
    }

    public String getMicroworldLAF() {
        return (String) lafBox.getSelectedItem();
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
        bgrIconPanel.saveBgrIconButton.setEnabled(backgroundIcon != null);
        bgrIconPanel.delBgrIconButton.setEnabled(backgroundIcon != null);
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
            bgrIconPanel.chooseBgrIconButton.setEnabled(false);
            bgrIconPanel.saveBgrIconButton.setEnabled(false);
            bgrIconPanel.delBgrIconButton.setEnabled(false);
            bgrColorLb.setEnabled(false);
            bgrIconPanel.bgrIconLb.setEnabled(false);
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
            bgrIconPanel.chooseBgrIconButton.setEnabled(true);
            if (backgroundIcon != null) {
                bgrIconPanel.saveBgrIconButton.setEnabled(true);
                bgrIconPanel.delBgrIconButton.setEnabled(true);
            }else{
                bgrIconPanel.saveBgrIconButton.setEnabled(false);
                bgrIconPanel.delBgrIconButton.setEnabled(false);
            }
            bgrColorLb.setEnabled(true);
            bgrIconPanel.bgrIconLb.setEnabled(true);
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
                fileDialog = new ESlateFileDialog(composer.parentFrame, fileDialogTitle, ESlateFileDialog.SAVE);
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
                DetailedErrorDialog dialog = new DetailedErrorDialog(parentDialog);
                String message = uiDialogBundle.getString("UnableToSaveIcon");
                message = message + " \"" + imageFileName + "\". ";

                dialog.setMessage(message);
                dialog.appendThrowableMessage(thr);
                dialog.createNewLine();
                dialog.appendThrowableStackTrace(thr);
                ESlateContainerUtils.showDetailedErrorDialog(composer, dialog, this, true);
            }
        }
    }

    void applyBackgroundProperties() {
        composer.setMicroworldBorder(getBorderType(), getBorderColor(), getBorderIcon(), getBorderInsets());
        //Set the microworld's outer border
        composer.setOuterBorder(getOuterBorderType());
        //Set the microworld's background
        composer.setMicroworldBackground(getBackgroundType(), getBackgroundColor(), getBackgroundIcon(), getBackgroundIconDisplayMode());
		if (composer.microworld != null) composer.microworld.setMicroworldIcon(microworldIcon);

        String lafName = getMicroworldLAF();
        // If the user has deleted the contents of the 'lafPanel' then reset the microworld's L&F
        if (lafName == null || lafName.trim().length() == 0)
            composer.microworld.setMicroworldLAFClassName(null);
        else{
            // Try to find a registered L&F with this name. If you find one, get its class.
            String lafClassName = composer.installedLAFs.getClassName(lafName);
            // If no L&F is registered with this name, then maybe the name typed by the user is
            // the class name of the desired L&F.
            if (lafClassName == null)
                lafClassName = lafName;

            /* If setting the new L&F does not succeed, then we have to reset the name contained
             * in the 'lafBox', which describes the current L&F.
             */
            composer.microworld.setMicroworldLAFClassName(lafClassName);
            // Get the class name of the current L&F
            lafClassName = composer.microworld.getMicroworldLAFClassName();
            lafName = composer.installedLAFs.getName(lafClassName);
            // If the 'lafClassName' is not registered or is null, then instead of an L&F name
            // display the L&F class name in the 'lafPanel'.
            if (lafName == null)
                lafName = lafClassName;
            setMicroworldLAF(lafName);
        }
    }

    void setLocked(boolean locked) {
        if (isLocked == locked) return;
        isLocked = locked;
        boolean enabled = !locked;
        samplelb.setEnabled(enabled);
        bSamplelb.setEnabled(enabled);
        upSpin.setEnabled(enabled);
        downSpin.setEnabled(enabled);
        leftSpin.setEnabled(enabled);
        rightSpin.setEnabled(enabled);
        chooseColor.setEnabled(enabled);
        chooseBorderIconButton.setEnabled(enabled);
        colorButton.setEnabled(enabled);
        iconButton.setEnabled(enabled);
        noneButton.setEnabled(enabled);
        bgrColorLb.setEnabled(enabled);
        bgrIconPanel.bgrIconLb.setEnabled(enabled);
        bgrNoneButton.setEnabled(enabled);
        useOuterBorder.setEnabled(enabled);
        raisedBorder.setEnabled(enabled);
        loweredBorder.setEnabled(enabled);
        bgrChooseColor.setEnabled(enabled);
        bgrIconPanel.chooseBgrIconButton.setEnabled(enabled);
        centerIconButton.setEnabled(enabled);
        fitIconButton.setEnabled(enabled);
        tileIconButton.setEnabled(enabled);
        bgrIconPanel.saveBgrIconButton.setEnabled(enabled);
        saveBorderIconButton.setEnabled(enabled);
        bgrIconPanel.delBgrIconButton.setEnabled(enabled);
        delBorderIconButton.setEnabled(enabled);
        if (enabled) {
            setBorderIcon(getBorderIcon());
            borderTypeChangedAction();
            setBackgroundIcon(getBackgroundIcon());
            backgroundTypeChangedAction();
        }
    }

	private ESlateFileDialog getFileDialog(String title) {
		if (fileDialog == null) {
			if (ESlateContainer.iconFileDialog != null)
				fileDialog = ESlateContainer.iconFileDialog;
			else{
				fileDialog = new ESlateFileDialog(MwdBackgroundPropsPanel.this.composer.parentFrame, title, ESlateFileDialog.LOAD);
				ESlateContainer.iconFileDialog = fileDialog;
			}
		}
		fileDialog.setFile("*.gif; *.jpg; *.png");
		fileDialog.setTitle(title);
		fileDialog.setMode(ESlateFileDialog.LOAD);
		String[] extensions = new String[3];
		extensions[0] = "gif";
		extensions[1] = "jpg";
		extensions[2] = "png";
		fileDialog.setDefaultExtension(extensions);
		return fileDialog;
	}
}

class IconSelectionPanel extends JPanel {
	NoBorderButton saveBgrIconButton, chooseBgrIconButton, delBgrIconButton;
	JLabel bgrIconLb = null;


	public IconSelectionPanel(String message, int labelWidth) {
		super(true);
		bgrIconLb = new JLabel(message);
		chooseBgrIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/openFile.gif")));
		saveBgrIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/saveFile.gif")));
		delBgrIconButton = new NoBorderButton(new ImageIcon(getClass().getResource("images/delete.gif")));

		if (labelWidth == -1) {
			FontMetrics fm = bgrIconLb.getFontMetrics(bgrIconLb.getFont());
			labelWidth = fm.stringWidth(message)+5;
		}
		Dimension labelSize = new Dimension(labelWidth, 20);
        bgrIconLb.setMaximumSize(labelSize);
		bgrIconLb.setPreferredSize(labelSize);
		bgrIconLb.setMinimumSize(labelSize);
		Dimension buttonSize = new Dimension(19, 17);
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

		Dimension rigidAreaDim = new Dimension(0, 25);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Box.createRigidArea(rigidAreaDim));
		add(bgrIconLb);
		add(Box.createRigidArea(rigidAreaDim));
		add(chooseBgrIconButton);
		add(saveBgrIconButton);
		add(delBgrIconButton);
		add(Box.createRigidArea(rigidAreaDim));
		setAlignmentX(LEFT_ALIGNMENT);

	}
}

