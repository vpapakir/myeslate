package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Color;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;

public class MatteBorderPanel extends JPanel {
    public static final int V_GAP = 5;
    public static final Expression V_GAP_EXP = MathEF.constant(V_GAP);
    IconChoosePanel iconPanel;
    ColorChoosePanel colorPanel;
    SpinField topPanel;
    SpinField bottomPanel;
    SpinField leftPanel;
    SpinField rightPanel;
    private boolean localeIsGreek = false;
    private ResourceBundle matteBorderPanelBundle;
    MatteBorder2 border;

    public MatteBorderPanel(MatteBorder2 b, final JPanel samplePanel) {
        super(true);
        if (b == null) throw new NullPointerException("The supplied MatteBorder cannot be null");
        this.border = b;
        matteBorderPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MatteBorderPanelBundle", Locale.getDefault());
        if (matteBorderPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.MatteBorderPanelBundle_el_GR"))
            localeIsGreek = true;

        colorPanel = new ColorChoosePanel(true, null, border.getBorderColor());

        iconPanel =  new IconChoosePanel(true, null, border.getBorderIcon());
//        Dimension buttonSize = new Dimension(20, 20);
/*        iconPanel.openFile.setMaximumSize(buttonSize);
        iconPanel.openFile.setMinimumSize(buttonSize);
        iconPanel.openFile.setPreferredSize(buttonSize);
        iconPanel.clearIcon.setMaximumSize(buttonSize);
        iconPanel.clearIcon.setMinimumSize(buttonSize);
        iconPanel.clearIcon.setPreferredSize(buttonSize);
        iconPanel.editIcon.setMaximumSize(buttonSize);
        iconPanel.editIcon.setMinimumSize(buttonSize);
        iconPanel.editIcon.setPreferredSize(buttonSize);
*/
        Insets borderInsets = border.getBorderInsets(samplePanel);
        int top = 1;
        if (border != null) top = borderInsets.top;
        topPanel = new SpinField(true, matteBorderPanelBundle.getString("Top"), top);

        int bottom = 1;
        if (border != null) bottom = borderInsets.bottom;
        bottomPanel = new SpinField(true, matteBorderPanelBundle.getString("Bottom"), bottom);

        int left = 1;
        if (border != null) left = borderInsets.left;
        leftPanel = new SpinField(true, matteBorderPanelBundle.getString("Left"), left);

        int right = 1;
        if (border != null) right = borderInsets.right;
        rightPanel = new SpinField(true, matteBorderPanelBundle.getString("Right"), right);

        topPanel.setMinValue(0);
        bottomPanel.setMinValue(0);
        leftPanel.setMinValue(0);
        rightPanel.setMinValue(0);

        int labelWidth = SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {colorPanel, iconPanel, rightPanel, leftPanel, topPanel, bottomPanel}, -1, 5);
        SpinField.setSpinFieldSpinSize(new SpinField[] {rightPanel, leftPanel, topPanel, bottomPanel},  45, 20);

//        Expression maxWidth = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null),
//            new java.awt.Component[] {colorPanel, iconPanel, rightPanel, leftPanel, topPanel, bottomPanel}));
        Expression maxWidth = GroupEF.preferredWidthMax(new java.awt.Component[] {
            colorPanel, iconPanel, rightPanel, leftPanel, topPanel, bottomPanel});
        Expression heightExp = MathEF.constant(20);
        ExplicitLayout layout = new ExplicitLayout();
        setLayout(layout);
        ExplicitConstraints ec1 = new ExplicitConstraints(colorPanel);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(colorPanel, 0.5d));
        ec1.setHeight(heightExp);
        ec1.setWidth(maxWidth);
        add(colorPanel, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(iconPanel);
        ec2.setHeight(heightExp);
        ec2.setX(ComponentEF.left(colorPanel));
        ec2.setY(ComponentEF.bottom(colorPanel).add(V_GAP_EXP));
        ec2.setWidth(maxWidth);
        add(iconPanel, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(topPanel);
        ec3.setHeight(heightExp);
        ec3.setX(ComponentEF.left(colorPanel));
        ec3.setY(ComponentEF.bottom(iconPanel).add(V_GAP_EXP));
        ec3.setWidth(maxWidth);
        add(topPanel, ec3);
        ExplicitConstraints ec4 = new ExplicitConstraints(bottomPanel);
        ec4.setHeight(heightExp);
        ec4.setX(ComponentEF.left(colorPanel));
        ec4.setY(ComponentEF.bottom(topPanel).add(V_GAP_EXP));
        ec4.setWidth(maxWidth);
        add(bottomPanel, ec4);
        ExplicitConstraints ec5 = new ExplicitConstraints(leftPanel);
        ec5.setHeight(heightExp);
        ec5.setX(ComponentEF.left(colorPanel));
        ec5.setY(ComponentEF.bottom(bottomPanel).add(V_GAP_EXP));
        ec5.setWidth(maxWidth);
        add(leftPanel, ec5);
        ExplicitConstraints ec6 = new ExplicitConstraints(rightPanel);
        ec6.setHeight(heightExp);
        ec6.setX(ComponentEF.left(colorPanel));
        ec6.setY(ComponentEF.bottom(leftPanel).add(V_GAP_EXP));
        ec6.setWidth(maxWidth);
        add(rightPanel, ec6);


        colorPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color color = (Color) evt.getNewValue();
                    if (color == null) return;
                    Insets insets = border.getBorderInsets(samplePanel);
                    MatteBorder oldBorder = border;
                    border = new MatteBorder2(insets.top, insets.left, insets.bottom, insets.right, color);
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
        iconPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Icon")) {
                    Icon icon = (Icon) evt.getNewValue();
                    Color borderColor = colorPanel.getColor();
                    Insets insets = border.getBorderInsets(samplePanel);
                    MatteBorder oldBorder = border;
                    if (icon == null) {
                        colorPanel.setEnabled(true);
                        border = new MatteBorder2(insets.top, insets.left, insets.bottom, insets.right, borderColor);
                    }else{
                        colorPanel.setEnabled(false);
                        border = new MatteBorder2(insets.top, insets.left, insets.bottom, insets.right, icon);
                    }
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
        topPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int top = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                MatteBorder oldBorder = border;
                if (border.usesColor)
                    border = new MatteBorder2(top, insets.left, insets.bottom, insets.right,
                                              border.getBorderColor());
                else
                    border = new MatteBorder2(top, insets.left, insets.bottom, insets.right,
                                              border.getBorderIcon());
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        });
        bottomPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int bottom = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                MatteBorder oldBorder = border;
                if (border.usesColor)
                    border = new MatteBorder2(insets.top, insets.left, bottom, insets.right,
                                              border.getBorderColor());
                else
                    border = new MatteBorder2(insets.top, insets.left, bottom, insets.right,
                                              border.getBorderIcon());
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        });
        leftPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int left = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                MatteBorder oldBorder = border;
                if (border.usesColor)
                    border = new MatteBorder2(insets.top, left, insets.bottom, insets.right,
                                              border.getBorderColor());
                else
                    border = new MatteBorder2(insets.top, left, insets.bottom, insets.right,
                                              border.getBorderIcon());
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        });
        rightPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int right = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                MatteBorder oldBorder = border;
                if (border.usesColor)
                    border = new MatteBorder2(insets.top, insets.left, insets.bottom, right,
                                              border.getBorderColor());
                else
                    border = new MatteBorder2(insets.top, insets.left, insets.bottom, right,
                                              border.getBorderIcon());
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        });
    }

    public MatteBorder2 getMatteBorder() {
        return border;
    }

    public void setMatteBorder(MatteBorder2 b, java.awt.Component c) {
        border = b;
        if (border.usesColor) {
            colorPanel.panel.setBackground(border.getBorderColor());
        }else{
            iconPanel.icon = border.getBorderIcon();
            colorPanel.setEnabled(false);
        }
        Insets insets = border.getBorderInsets(c);
        topPanel.spin.setValue(new Integer(insets.top));
        bottomPanel.spin.setValue(new Integer(insets.bottom));
        leftPanel.spin.setValue(new Integer(insets.left));
        rightPanel.spin.setValue(new Integer(insets.right));
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}

class MatteBorder2 extends MatteBorder {
    boolean usesColor = false;

    public MatteBorder2(Icon tileIcon) {
        super(tileIcon);
        usesColor = false;
    }

    public MatteBorder2(int top, int left, int bottom, int right, Color color) {
        super(top, left, bottom, right, color);
        usesColor = true;
    }

    public MatteBorder2(int top, int left, int bottom, int right, Icon tileIcon) {
        super(top, left, bottom, right, tileIcon);
        usesColor = false;
    }

    public Color getBorderColor() {
        return color;
    }

    public Icon getBorderIcon() {
        return tileIcon;
    }

    public MatteBorder toMatteBorder() {
        if (usesColor)
            return new MatteBorder(top, left, bottom, right, color);
        else
            return new MatteBorder(top, left, bottom, right, tileIcon);
    }
}

