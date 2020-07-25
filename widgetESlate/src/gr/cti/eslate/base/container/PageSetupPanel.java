
//Title:
//Version:
//Copyright:    Copyright (c) 1999
//Author:       George Tsironis
//Company:
//Description:

package gr.cti.eslate.base.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;


public class PageSetupPanel extends JDialog {
    public static final int DIALOG_OK = 1;
    public static final int DIALOG_CANCEL = 0;
    JPanel mainPanel;
    SpinField topSpin, bottomSpin, leftSpin, rightSpin;
    JCheckBox centerBox, fitBox;
    SpinField scale;
    JButton okButton, cancel;
    ResourceBundle bundle;
    int w = 0, h = 0;
    private int returnCode = DIALOG_CANCEL;

    public PageSetupPanel(Frame parentFrame, PageInfo info) {
        super(parentFrame, true);
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.PageSetupPanelBundle", Locale.getDefault());
        setTitle(bundle.getString("DialogTitle"));

        topSpin = new SpinField(true, bundle.getString("Top"), 0);
        topSpin.setMinValue(0);
        topSpin.spin.setStep(0.1d);
        topSpin.setHeight(22);
        topSpin.setAlignmentX(CENTER_ALIGNMENT);

        bottomSpin = new SpinField(true, bundle.getString("Bottom"), 0);
        bottomSpin.setMinValue(0);
        bottomSpin.spin.setStep(0.1d);
        bottomSpin.setHeight(20);
        bottomSpin.setAlignmentX(CENTER_ALIGNMENT);

        leftSpin = new SpinField(true, bundle.getString("Left"), 0);
        leftSpin.setMinValue(0);
        leftSpin.spin.setStep(0.1d);
        leftSpin.setHeight(22);
        leftSpin.setAlignmentX(CENTER_ALIGNMENT);

        rightSpin = new SpinField(true, bundle.getString("Right"), 0);
        rightSpin.setMinValue(0);
        rightSpin.spin.setStep(0.1d);
        rightSpin.setHeight(22);
        rightSpin.setAlignmentX(CENTER_ALIGNMENT);

        SpinField[] spins = new SpinField[] {topSpin, bottomSpin, leftSpin, rightSpin};
        int maxSpinLabelWidth = SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 45, 20);
        int spinWidth = maxSpinLabelWidth + 10 + 45;
//System.out.println("spinWidth: " + spinWidth);
        int spinHeight = 22;
        Border spinBorder = leftSpin.getBorder();
        if (spinBorder != null) {
            Insets i = spinBorder.getBorderInsets(leftSpin);
            spinWidth = spinWidth + i.left + i.right;
            spinHeight = spinHeight + i.top + i.bottom;
        }


        Dimension spinSize = new Dimension(spinWidth, spinHeight);
//System.out.println("spinSize: " + spinSize);
        topSpin.setMaximumSize(spinSize);
        topSpin.setPreferredSize(spinSize);
        topSpin.setMinimumSize(spinSize);
        bottomSpin.setMaximumSize(spinSize);
        bottomSpin.setPreferredSize(spinSize);
        bottomSpin.setMinimumSize(spinSize);
        leftSpin.setMaximumSize(spinSize);
        leftSpin.setPreferredSize(spinSize);
        leftSpin.setMinimumSize(spinSize);
        rightSpin.setMaximumSize(spinSize);
        rightSpin.setPreferredSize(spinSize);
        rightSpin.setMinimumSize(spinSize);

        JPanel margin2Panel = new JPanel();
        TitledBorder marginPanelBorder = new TitledBorder(bundle.getString("Margins"));
        Color titleBorderColor = new Color(119, 40, 104);
        marginPanelBorder.setTitleColor(titleBorderColor);
        margin2Panel.setBorder(marginPanelBorder); //new CompoundBorder(new LineBorder(Color.black), new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black)))); //marginPanelBorder); //new LineBorder(Color.black)); //TitledBorder(bundle.getString("Margins")));
        Insets mrgBorderInsets = margin2Panel.getBorder().getBorderInsets(margin2Panel);

        margin2Panel.setLayout(new BoxLayout(margin2Panel, BoxLayout.Y_AXIS));
        margin2Panel.add(topSpin);
        margin2Panel.add(bottomSpin);
        margin2Panel.add(leftSpin);
        margin2Panel.add(rightSpin);

        Dimension panelDim = new Dimension(spinWidth, 22*4);

        // POSITION PANEL
        centerBox = new JCheckBox(bundle.getString("Center"));
        fitBox = new JCheckBox(bundle.getString("Fit"));
        scale = new SpinField(true, bundle.getString("Scale"), 100);
        scale.setHeight(22);
        scale.setMinValue(1);

        SpinField[] scaleSpin = new SpinField[] {scale};
        int scaleLabelWidth = SpinField.alignLabelsOfLabeledPanels(scaleSpin, 0, 5);
        SpinField.setSpinFieldSpinSize(scaleSpin, 50, 20);
        int scaleWidth = scaleLabelWidth + 5 + 50;
        TitledBorder posPanelBorder = new TitledBorder(bundle.getString("Position"));
        posPanelBorder.setTitleColor(titleBorderColor);

        JPanel positionPanel = new JPanel(true);
        positionPanel.setBorder(posPanelBorder); //new CompoundBorder(new LineBorder(Color.black), new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black))));
        Insets borderInsets = positionPanel.getBorder().getBorderInsets(positionPanel);

        positionPanel.setLayout(new BoxLayout(positionPanel, BoxLayout.Y_AXIS));
        positionPanel.add(centerBox);
        positionPanel.add(fitBox);
        positionPanel.add(scale);
        positionPanel.add(Box.createGlue());

        int maxPosWidth = centerBox.getPreferredSize().width;
        if (fitBox.getPreferredSize().width > maxPosWidth)
            maxPosWidth = fitBox.getPreferredSize().width;
        if (scaleWidth > maxPosWidth)
            maxPosWidth = scaleWidth;

        if (panelDim.width < maxPosWidth)
            panelDim.width = maxPosWidth;

        panelDim.width = panelDim.width + borderInsets.left + borderInsets.right;
        panelDim.height = panelDim.height + borderInsets.top + borderInsets.bottom;

/*        if (scaleWidth < panelDim.width) {
            Dimension scaleDim = new Dimension(scaleWidth, 22);
            scale.setPreferredSize(scaleDim);
            scale.setMaximumSize(scaleDim);
            scale.setMinimumSize(scaleDim);
        }
*/
        margin2Panel.setMaximumSize(panelDim);
        margin2Panel.setPreferredSize(panelDim);
        margin2Panel.setMinimumSize(panelDim);
        positionPanel.setMaximumSize(panelDim);
        positionPanel.setPreferredSize(panelDim);
        positionPanel.setMinimumSize(panelDim);

        //BUTTON PANEL
        Insets zeroInsets = new Insets(0,0,0,0);
        Dimension buttonSize = new Dimension(90, 25);

        okButton = new JButton(bundle.getString("OK"));
        cancel = new JButton(bundle.getString("Cancel"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        cancel.setForeground(color128);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        cancel.setMaximumSize(buttonSize);
        cancel.setPreferredSize(buttonSize);
        cancel.setMinimumSize(buttonSize);
        okButton.setMargin(zeroInsets);
        cancel.setMargin(zeroInsets);

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancel);
        buttonPanel.add(Box.createGlue());

        mainPanel = new JPanel();
        mainPanel.setBorder(new MatteBorder(5, 0, 5, 0, UIManager.getColor("control")));
        mainPanel.setLayout(new BorderLayout(0, 8));
        mainPanel.add(margin2Panel, BorderLayout.WEST);
        mainPanel.add(positionPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        setContentPane(mainPanel);
        pack();
        setResizable(false);

        centerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centerBoxStateChanged();
            }
        });

        fitBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fitBoxStateChanged();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_CANCEL;
                dispose();
            }
        });

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancel.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancel.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);


        setPageInfo(info);
    }

    public void setPageInfo(PageInfo setup) {
        topSpin.spin.setValue(new Double(setup.getTopMargin()));
        bottomSpin.spin.setValue(new Double(setup.getBottomMargin()));
        leftSpin.spin.setValue(new Double(setup.getLeftMargin()));
        rightSpin.spin.setValue(new Double(setup.getRightMargin()));
        setScale(setup.getScale());
        setFitBox(setup.isFitToPage());
        setCenterBox(setup.isCenterOnPage());
    }

    public PageInfo getPageInfo() {
        PageInfo pi = new PageInfo();
        pi.setTopMargin(((Number) topSpin.spin.getValue()).floatValue());
        pi.setBottomMargin(((Number) bottomSpin.spin.getValue()).floatValue());
        pi.setLeftMargin(((Number) leftSpin.spin.getValue()).floatValue());
        pi.setRightMargin(((Number) rightSpin.spin.getValue()).floatValue());

        pi.setFitToPage(fitBox.isSelected());
        pi.setCenterOnPage(centerBox.isSelected());
//        System.out.println("New scale: " + scale.field.getValue());
        pi.setScale(((Number)scale.spin.getValue()).intValue());
        return pi;
    }

    private void setFitBox(boolean fit) {
        if (!fitBox.isEnabled()) return;
        if (fit == fitBox.isSelected()) return;
        fitBox.setSelected(fit);
        fitBoxStateChanged();
    }

    private void fitBoxStateChanged() {
        if (fitBox.isSelected()) {
            scale.setEnabled(false);
            centerBox.setEnabled(false);
            centerBox.setSelected(false);
        }else{
            scale.setEnabled(true);
            centerBox.setEnabled(true);
        }
    }

    private void setCenterBox(boolean center) {
        if (!centerBox.isEnabled()) return;
        if (center == centerBox.isSelected()) return;
        centerBox.setSelected(center);
        centerBoxStateChanged();
    }

    private void centerBoxStateChanged() {
        if (centerBox.isSelected()) {
            fitBox.setEnabled(false);
            fitBox.setSelected(false);
            topSpin.setEnabled(false);
            bottomSpin.setEnabled(false);
            rightSpin.setEnabled(false);
            leftSpin.setEnabled(false);
        }else{
            topSpin.setEnabled(true);
            bottomSpin.setEnabled(true);
            rightSpin.setEnabled(true);
            leftSpin.setEnabled(true);
            fitBox.setEnabled(true);
        }
    }

    private void setScale(int s) {
        if (!scale.isEnabled()) return;
        if (((Number) scale.spin.getValue()).intValue() == s) return;
        scale.spin.setValue(new Integer(s));
    }

    public int getReturnCode() {
        return returnCode;
    }
}

