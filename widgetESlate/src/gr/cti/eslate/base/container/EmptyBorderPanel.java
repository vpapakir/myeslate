package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


public class EmptyBorderPanel extends JPanel {
    public static final int FIELD_HEIGHT = 20;
    public static final Expression FIELD_HEIGHT_EXP = MathEF.constant(FIELD_HEIGHT);
    public static final int V_GAP = 5;
    public static final Expression V_GAP_EXP = MathEF.constant(V_GAP);
    SpinField topPanel;
    SpinField bottomPanel;
    SpinField leftPanel;
    SpinField rightPanel;
    private boolean localeIsGreek = false;
    private ResourceBundle emptyBorderPanelBundle;
    EmptyBorder border;

    public EmptyBorderPanel(EmptyBorder b, final JPanel samplePanel) {
        super(true);
        if (b == null) throw new NullPointerException("The supplied MatteBorder cannot be null");
        this.border = b;
        emptyBorderPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.EmptyBorderPanelBundle", Locale.getDefault());
        if (emptyBorderPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.EmptyBorderPanelBundle_el_GR"))
            localeIsGreek = true;

        Insets borderInsets = border.getBorderInsets(samplePanel);
        int top = 1;
        if (border != null) top = borderInsets.top;
        topPanel = new SpinField(true, emptyBorderPanelBundle.getString("Top"), top);

        int bottom = 1;
        if (border != null) bottom = borderInsets.bottom;
        bottomPanel = new SpinField(true, emptyBorderPanelBundle.getString("Bottom"), bottom);

        int left = 1;
        if (border != null) left = borderInsets.left;
        leftPanel = new SpinField(true, emptyBorderPanelBundle.getString("Left"), left);

        int right = 1;
        if (border != null) right = borderInsets.right;
        rightPanel = new SpinField(true, emptyBorderPanelBundle.getString("Right"), right);

        topPanel.setMinValue(0);
        bottomPanel.setMinValue(0);
        leftPanel.setMinValue(0);
        rightPanel.setMinValue(0);

        setLayout(new ExplicitLayout());
        SpinField[] spins = new SpinField[] {topPanel, bottomPanel, leftPanel, rightPanel};
        int labelWidth = SpinField.alignLabelsOfLabeledPanels(spins, -1, 5);
        SpinField.setSpinFieldSpinSize(spins,  50, rightPanel.getPreferredSize().height);
        Expression maxWidth = GroupEF.preferredWidthMax(spins).add(2*SpinField.H_GAP+1);

        ExplicitConstraints ec1 = new ExplicitConstraints(topPanel);
        ec1.setX(ContainerEF.width(this).subtract(maxWidth).divide(2));
        ec1.setY(ContainerEF.top(this));
        ec1.setWidth(maxWidth);
        ec1.setHeight(FIELD_HEIGHT_EXP);
        add(topPanel, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(bottomPanel);
        ec2.setY(ComponentEF.bottom(topPanel).add(V_GAP_EXP));
        ec2.setX(ComponentEF.left(topPanel));
        ec2.setWidth(maxWidth);
        ec2.setHeight(FIELD_HEIGHT_EXP);
        add(bottomPanel, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(leftPanel);
        ec3.setY(ComponentEF.bottom(bottomPanel).add(V_GAP_EXP));
        ec3.setX(ComponentEF.left(topPanel));
        ec3.setWidth(maxWidth);
        ec3.setHeight(FIELD_HEIGHT_EXP);
        add(leftPanel, ec3);
        ExplicitConstraints ec4 = new ExplicitConstraints(rightPanel);
        ec4.setY(ComponentEF.bottom(leftPanel).add(V_GAP_EXP));
        ec4.setX(ComponentEF.left(topPanel));
        ec4.setWidth(maxWidth);
        ec4.setHeight(FIELD_HEIGHT_EXP);
        add(rightPanel, ec4);

        gr.cti.eslate.spinButton.ValueChangedListener l = new gr.cti.eslate.spinButton.ValueChangedListener() {
            public void valueChanged(gr.cti.eslate.spinButton.ValueChangedEvent e) {
                int top = ((Number) e.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                EmptyBorder oldBorder = border;
                border = new EmptyBorder(top, insets.left, insets.bottom, insets.right);
                firePropertyChange("Border", oldBorder, border);
/*                if (samplePanel != null) {
                    MatteBorder b = new MatteBorder(top, insets.left, insets.bottom, insets.right, Color.gray);
                    samplePanel.setBorder(b);
                }
*/
            }
        };
        topPanel.addValueChangedListener(l);

        bottomPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int bottom = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                EmptyBorder oldBorder = border;
                border = new EmptyBorder(insets.top, insets.left, bottom, insets.right);
                firePropertyChange("Border", oldBorder, border);
/*                if (samplePanel != null) {
                    MatteBorder b = new MatteBorder(insets.top, insets.left, bottom, insets.right, Color.gray);
                    samplePanel.setBorder(b);
                }
*/
            }
        });
        leftPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int left = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                EmptyBorder oldBorder = border;
                border = new EmptyBorder(insets.top, left, insets.bottom, insets.right);
                firePropertyChange("Border", oldBorder, border);
/*                if (samplePanel != null) {
                    MatteBorder b = new MatteBorder(insets.top, left, insets.bottom, insets.right, Color.gray);
//                    CompoundBorder b = new CompoundBorder(outerBorder,
//                          new CompoundBorder(border, innerBorder));
                    samplePanel.setBorder(b);
                }
*/
            }
        });
        rightPanel.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int right = ((Number) evt.getValue()).intValue();
                Insets insets = border.getBorderInsets(samplePanel);
                EmptyBorder oldBorder = border;
                border = new EmptyBorder(insets.top, insets.left, insets.bottom, right);
                firePropertyChange("Border", oldBorder, border);
/*                if (samplePanel != null) {
                    MatteBorder b = new MatteBorder(insets.top, insets.left, insets.bottom, right, Color.gray);
//                    CompoundBorder b = new CompoundBorder(outerBorder,
//                          new CompoundBorder(border, innerBorder));
                    samplePanel.setBorder(b);
                }
*/
            }
        });
    }

    public EmptyBorder getEmptyBorder() {
        return border;
    }

    public void setEmptyBorder(EmptyBorder b, java.awt.Component c) {
        border = b;
        Insets insets = border.getBorderInsets(c);
//sp        topPanel.field.setText(insets.top);
//sp        bottomPanel.field.setText(insets.bottom);
//sp        leftPanel.field.setText(insets.left);
//sp        rightPanel.field.setText(insets.right);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

}

