package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.SpinButton;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class SpinField extends JPanel implements LabeledPanel {
    public static final int H_GAP = 3;
    public static final Expression H_GAP_EXP = MathEF.constant(H_GAP);
    JLabel label;
    SpinButton spin;
    private ArrayList numericValueListeners = new ArrayList();
    ExplicitLayout el = new ExplicitLayout();

    public SpinField(boolean doubleBuffered, String labelText) {
        super(doubleBuffered);
        label = new JLabel(labelText);
//        field = new NumericTextField();

/*        Dimension d = new Dimension(20, 16);
        field.setMaximumSize(d);
        field.setPreferredSize(d);
        field.setMinimumSize(d);
        field.setHorizontalAlignment(JTextField.RIGHT);
*/
//        Dimension spinDim = new Dimension(60, 16);
///        spin = new JPVSpin(JPVSpin.VERTICAL_RIGHT);
        spin = new SpinButton();


        setLayout(el); //new BorderLayout(3, 0));
        ExplicitConstraints ec1 = new ExplicitConstraints(label);
        ec1.setX(ContainerEF.left(this));
        ec1.setY(ContainerEF.top(this));
        ec1.setHeight(ContainerEF.height(this));
        ec1.setWidth(MathEF.constant(label.getFontMetrics(label.getFont()).stringWidth(label.getText())));
        add(label, ec1);

        ExplicitConstraints ec2 = new ExplicitConstraints(spin);
        ec2.setX(ComponentEF.right(label).add(H_GAP_EXP));
        ec2.setY(ContainerEF.top(this));
        ec2.setHeight(ContainerEF.height(this));
        ec2.setWidth(MathEF.constant(100)); //40));
        add(spin, ec2);
//        spin.setBorder(new LineBorder(Color.black));

        java.awt.Component[] comps = new java.awt.Component[] {label, spin};
//        Expression[] widthExpr = GroupEF.createExpressions(ComponentEF.preferredWidth(null), comps);
//        Expression[] heightExpr = GroupEF.createExpressions(ComponentEF.preferredHeight(null), comps);
        Expression heightExpr = GroupEF.preferredHeightMax(comps);
        el.setPreferredLayoutSize(ec1.getWidth().add(ec2.getWidth()), heightExpr);
//        spin.setBorder(new javax.swing.border.LineBorder(Color.yellow));
    }

    public SpinField(boolean doubleBuffered, String labelText, int value) {
        this(doubleBuffered, labelText);
        spin.setValue(new Integer(value));
    }

    public void setHeight(int height) {
        Expression heightExp = MathEF.constant(height);
        el.setPreferredLayoutSize(MathEF.constant(el.preferredLayoutSize(this).getWidth()), heightExp);
    }

    public void addValueChangedListener(ValueChangedListener vl) {
        spin.addValueChangedListener(vl);
    }

    public synchronized void removeValueChangedListener(ValueChangedListener vl) {
        spin.removeValueChangedListener(vl);
    }

    public void setEnabled(boolean enabled) {
        label.setEnabled(enabled);
        spin.setEnabled(enabled);
    }

    public void setFont(Font font) {
        if (label == null) return;
        label.setFont(font);
        spin.setFont(font);
    }

    public void setToolTipText(String tip) {
        if (label == null) return;
        label.setToolTipText(tip);
        spin.setToolTipText(tip);
    }

    public void setMaxValue(int value) {
        spin.setMaximumValue(new Integer(value));
    }

    public int getMaxValue() {
        return ((Integer) spin.getMaximumValue()).intValue();
    }

    public void setMinValue(int value) {
        spin.setMinimumValue(new Integer(value));
    }

    public int getMinValue() {
        return ((Integer) spin.getMinimumValue()).intValue();
    }

    public JLabel getLabel() {
        return label;
    }

    public SpinButton getSpinButton() {
        return spin;
    }

    public ExplicitLayout getExplicitLayout() {
        return el;
    }

    public static final int alignLabelsOfLabeledPanels(LabeledPanel[] panels, int initialMaxLabelWidth, int gap) {
        if (panels.length == 0) return -1;
        /* Find the max label width */
        FontMetrics fm = panels[0].getLabel().getFontMetrics(panels[0].getLabel().getFont());
        int maxWidth = initialMaxLabelWidth;
        for (int i=0; i<panels.length; i++) {
            int w = fm.stringWidth(panels[i].getLabel().getText());
            if (w > maxWidth) maxWidth = w;
        }
//System.out.println("alignLabelsOfLabeledPanels() maxWidth:" + maxWidth);
        Expression maxWidthExp = MathEF.constant(maxWidth + gap);
        for (int i=0; i<panels.length; i++) {
            JComponent comp = (JComponent) panels[i];
            ExplicitLayout el = panels[i].getExplicitLayout();
            int width = (int) ComponentEF.preferredWidth((Component) panels[i]).getValue(el);
            int height = (int) ComponentEF.preferredHeight((Component) panels[i]).getValue(el);
            if (comp.getBorder() != null) {
                Insets insets = comp.getBorder().getBorderInsets(comp);
                width = width - insets.right - insets.left;
                height = height - insets.top - insets.bottom;
            }

            ExplicitConstraints ec = el.getConstraints(panels[i].getLabel());
            int w1 = (int) ec.getWidth().getValue(el);
            ec.setWidth(maxWidthExp);
//System.out.println("setPreferredLayoutSize(" + panels[i].getLabel().getText() + "): " + MathEF.constant(width + maxWidth + gap - w1).getValue(el));
            el.setPreferredLayoutSize(MathEF.constant(width + maxWidth + gap - w1), MathEF.constant(height));
        }
        return maxWidth;
    }

    public static final void setSpinFieldSpinSize(SpinField[] fields, int spinWidth, int spinHeight) {
        if (spinWidth > 0) {
            for (int i=0; i<fields.length; i++) {
                JComponent comp = (JComponent) fields[i];
                ExplicitLayout el = (ExplicitLayout) fields[i].getLayout();
                int width = (int) ComponentEF.preferredWidth((Component) fields[i]).getValue(el);
                int height = (int) ComponentEF.preferredHeight((Component) fields[i]).getValue(el);
                if (comp.getBorder() != null) {
                    Insets insets = comp.getBorder().getBorderInsets(comp);
                    width = width - insets.right - insets.left;
                    height = height - insets.top - insets.bottom;
                }
                ExplicitConstraints ec = el.getConstraints(fields[i].spin);
                int w1 = (int) ec.getWidth().getValue(el);
                ec.setWidth(MathEF.constant(spinWidth));
                ec.setHeight(MathEF.constant(spinHeight));
                ec.setY(ContainerEF.height(fields[i].spin.getParent()).subtract(spinHeight).divide(2));
                el.setPreferredLayoutSize(MathEF.constant(width + spinWidth - w1), MathEF.constant(height));
            }
        }else if (spinWidth == 0) {
/*            Expression spinWidthExp = ComponentEF.width(fields[0].label).subtract(SpinField.H_GAP_EXP);
            spinWidth = (int) spinWidthExp.getValue((ExplicitLayout) fields[0].getLayout());
            for (int i=0; i<fields.length; i++) {
                ExplicitLayout el = (ExplicitLayout) fields[i].getLayout();
                ExplicitConstraints ec = el.getConstraints(fields[i].spin);
                ec.setWidth(ContainerEF.width().subtract(spinWidthExp));
                ec.setHeight(MathEF.constant(spinHeight));
            }
*/
        }

//        return maxWidth + spinWidth;
    }

/*
    Dimension preferredSize = new Dimension(100, 30);
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        preferredSize = size;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }
*/

    public static void main(String[] args) {
        SpinField spin = new SpinField(true, "Spin field 1");
        SpinField spin2 = new SpinField(true, "Spin field 2");
        JFrame fr = new JFrame();
        fr.getContentPane().setLayout(null); //new BorderLayout()); //new BoxLayout(fr.getContentPane(), BoxLayout.X_AXIS));
        fr.getContentPane().add(spin); //, BorderLayout.NORTH);
        fr.getContentPane().add(spin2);
        spin.setBounds(0, 0, 200, 21);
        spin2.setBounds(0, 31, 200, 50);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(300, 300);
        fr.setVisible(true);
    }

}
