package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.MathEF;

public class LineBorderPanel extends JPanel {
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int FIELD_HEIGHT = 20;
    public static final Expression FIELD_HEIGHT_EXP = MathEF.constant(FIELD_HEIGHT);
    ColorChoosePanel colorPanel;
    SpinField thicknessPanel;
    private boolean localeIsGreek = false;
    private ResourceBundle lineBorderPanelBundle;
    LineBorder border;

    public LineBorderPanel(LineBorder b, final JPanel samplePanel) {
        super(true);
        this.border = b;
        lineBorderPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LineBorderPanelBundle", Locale.getDefault());
        if (lineBorderPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.LineBorderPanelBundle_el_GR"))
            localeIsGreek = true;

        colorPanel = new ColorChoosePanel(true, null, border.getLineColor());
        int thickness = 1;
        if (border != null) thickness = border.getThickness();
        thicknessPanel = new SpinField(true, lineBorderPanelBundle.getString("Thickness"), thickness);
        thicknessPanel.setMinValue(0);

        int maxLabelWidth = SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {colorPanel, thicknessPanel}, 0, H_GAP);
        SpinField.setSpinFieldSpinSize(new SpinField[] {thicknessPanel}, 45, FIELD_HEIGHT);
        ExplicitLayout el = new ExplicitLayout();
        setLayout(el);
        Expression fieldWidth = ComponentEF.preferredWidth(colorPanel);
        ExplicitConstraints ec1 = new ExplicitConstraints(colorPanel);
        ec1.setWidth(fieldWidth);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(colorPanel, 0.5d));
        ec1.setY(ContainerEF.top(colorPanel));
        ec1.setHeight(FIELD_HEIGHT_EXP);
        add(colorPanel, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(thicknessPanel);
        ec2.setWidth(fieldWidth);
        ec2.setX(ComponentEF.left(colorPanel));
        ec2.setY(ComponentEF.bottom(colorPanel).add(V_GAP));
        ec2.setHeight(FIELD_HEIGHT_EXP);
        add(thicknessPanel, ec2);

        colorPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    int t = 1;
                    if (border != null)
                        t = border.getThickness();
                    LineBorder oldBorder = border;
                    border = new LineBorder((Color) evt.getNewValue(), t);
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });

        ValueChangedListener l = new ValueChangedListener() {
            public void valueChanged(gr.cti.eslate.spinButton.ValueChangedEvent evt) {
                int t = ((Number) evt.getValue()).intValue();
                Color cl = Color.black;
                if (border != null)
                    cl = border.getLineColor();
                LineBorder oldBorder = border;
                border = new LineBorder(cl, t);
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        };
        thicknessPanel.addValueChangedListener(l);

    }

    public LineBorder getLineBorder() {
        return border;
    }

    public void setLineBorder(LineBorder b) {
        border = b;
        colorPanel.panel.setBackground(border.getLineColor());
        thicknessPanel.spin.setValue(new Integer(border.getThickness()));
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}