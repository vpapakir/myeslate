package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.OneLineBevelBorder;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.MathEF;


public class OneLineBevelBorderPanel extends JPanel {
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int FIELD_HEIGHT = 20;
    public static final Expression FIELD_HEIGHT_EXP = MathEF.constant(FIELD_HEIGHT);
    JComboBox typeBox;
    ColorChoosePanel hightlightPanel;
//    ColorChoosePanel hightlightInnerPanel;
    ColorChoosePanel shadowPanel;
//    ColorChoosePanel shadowInnerPanel;
    private boolean localeIsGreek = false;
    private ResourceBundle bundle;
    private static String LOWERED;
    private static String RAISED;
    OneLineBevelBorder border;

    public OneLineBevelBorderPanel(OneLineBevelBorder b, final JPanel samplePanel) {
        super(true);
        this.border = b;
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.OneLineBevelBorderPanelBundle", Locale.getDefault());
        if (bundle.getClass().getName().equals("gr.cti.eslate.base.container.OneLineBevelBorderPanelBundle_el_GR"))
            localeIsGreek = true;

        LOWERED = bundle.getString("Lowered");
        RAISED = bundle.getString("Raised");
        Object[] o = new Object[] {LOWERED, RAISED};
        typeBox = new JComboBox(o);

        hightlightPanel = new ColorChoosePanel(true,
                                               bundle.getString("HighlightColor"),
                                               border.getHighlightOuterColor(samplePanel));
        shadowPanel = new ColorChoosePanel(true,
                                           bundle.getString("ShadowColor"),
                                           border.getShadowOuterColor(samplePanel));

        int maxLabelWidth = SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {hightlightPanel, shadowPanel}, 0, H_GAP);
        ExplicitLayout el = new ExplicitLayout();
        setLayout(el);
        Expression fieldWidth = ComponentEF.preferredWidth(shadowPanel);
        ExplicitConstraints ec1 = new ExplicitConstraints(typeBox);
        ec1.setWidth(fieldWidth);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(typeBox, 0.5d));
        ec1.setY(ContainerEF.top(typeBox));
        ec1.setHeight(FIELD_HEIGHT_EXP);
        add(typeBox, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(hightlightPanel);
        ec2.setWidth(fieldWidth);
        ec2.setX(ComponentEF.left(typeBox));
        ec2.setY(ComponentEF.bottom(typeBox).add(V_GAP));
        ec2.setHeight(FIELD_HEIGHT_EXP);
        add(hightlightPanel, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(shadowPanel);
        ec3.setWidth(fieldWidth);
        ec3.setX(ComponentEF.left(typeBox));
        ec3.setY(ComponentEF.bottom(hightlightPanel).add(V_GAP));
        ec3.setHeight(FIELD_HEIGHT_EXP);
        add(shadowPanel, ec3);

        // Initialization
        if (border != null && border.getBevelType() == OneLineBevelBorder.RAISED)
            typeBox.setSelectedItem(RAISED);
        else
            typeBox.setSelectedItem(LOWERED);

        typeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) typeBox.getSelectedItem();
                int type = OneLineBevelBorder.RAISED;
                if (selectedItem.equals(LOWERED))
                    type = OneLineBevelBorder.LOWERED;
                OneLineBevelBorder oldBorder = border;
                border = new OneLineBevelBorder(type,
                                         border.getHighlightOuterColor(samplePanel),
                                         border.getShadowOuterColor(samplePanel));
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        });
        hightlightPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color hightlightColor = (Color) evt.getNewValue();
                    if (hightlightColor == null) return;
                    OneLineBevelBorder oldBorder = border;
                    border = new OneLineBevelBorder(border.getBevelType(),
                                             hightlightColor,
                                             border.getShadowOuterColor(samplePanel));
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
        shadowPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color shadowColor = (Color) evt.getNewValue();
                    if (shadowColor == null) return;
                    OneLineBevelBorder oldBorder = border;
                    border = new OneLineBevelBorder(border.getBevelType(),
                                             border.getHighlightOuterColor(samplePanel),
                                             shadowColor);
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
    }

    public OneLineBevelBorder getOneLineBevelBorder() {
        return border;
    }

    public void setOneLineBevelBorder(OneLineBevelBorder b, java.awt.Component c) {
        border = b;
        if (border.getBevelType() == OneLineBevelBorder.RAISED)
            typeBox.setSelectedItem(RAISED);
        else
            typeBox.setSelectedItem(LOWERED);
        hightlightPanel.panel.setBackground(border.getHighlightOuterColor(c));
        shadowPanel.panel.setBackground(border.getShadowOuterColor(c));
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

}
