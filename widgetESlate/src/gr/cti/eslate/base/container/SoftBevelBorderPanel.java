package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.MathEF;


public class SoftBevelBorderPanel extends JPanel {
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int FIELD_HEIGHT = 20;
    public static final Expression FIELD_HEIGHT_EXP = MathEF.constant(FIELD_HEIGHT);
    JComboBox typeBox;
    ColorChoosePanel hightlightOuterPanel;
    ColorChoosePanel hightlightInnerPanel;
    ColorChoosePanel shadowOuterPanel;
    ColorChoosePanel shadowInnerPanel;
//    private boolean localeIsGreek = false;
    private ResourceBundle bevelBorderPanelBundle;
    private static String LOWERED;
    private static String RAISED;
    SoftBevelBorder border;

    public SoftBevelBorderPanel(SoftBevelBorder b, final JPanel samplePanel) {
        super(true);
        this.border = b;
        bevelBorderPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.BevelBorderPanelBundle", Locale.getDefault());
//        if (bevelBorderPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.BevelBorderPanelBundle_el_GR"))
//            localeIsGreek = true;

        LOWERED = bevelBorderPanelBundle.getString("Lowered");
        RAISED = bevelBorderPanelBundle.getString("Raised");
        Object[] o = new Object[] {LOWERED, RAISED};
        typeBox = new JComboBox(o);

        hightlightOuterPanel = new ColorChoosePanel(true,
                                      bevelBorderPanelBundle.getString("HighlightOuterColor"),
                                      border.getHighlightOuterColor(samplePanel));
        hightlightInnerPanel = new ColorChoosePanel(true,
                                      bevelBorderPanelBundle.getString("HighlightInnerColor"),
                                      border.getHighlightInnerColor(samplePanel));
        shadowOuterPanel = new ColorChoosePanel(true,
                                      bevelBorderPanelBundle.getString("ShadowOuterColor"),
                                      border.getShadowOuterColor(samplePanel));
        shadowInnerPanel = new ColorChoosePanel(true,
                                      bevelBorderPanelBundle.getString("ShadowInnerColor"),
                                      border.getShadowInnerColor(samplePanel));

        int maxLabelWidth = SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {hightlightOuterPanel, hightlightInnerPanel, shadowOuterPanel, shadowInnerPanel}, 0, H_GAP);
        ExplicitLayout el = new ExplicitLayout();
        setLayout(el);
        Expression fieldWidth = ComponentEF.preferredWidth(shadowInnerPanel);
        ExplicitConstraints ec1 = new ExplicitConstraints(typeBox);
        ec1.setWidth(fieldWidth);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(typeBox, 0.5d));
        ec1.setY(ContainerEF.top(typeBox));
        ec1.setHeight(FIELD_HEIGHT_EXP);
        add(typeBox, ec1);
        ExplicitConstraints ec2 = new ExplicitConstraints(hightlightOuterPanel);
        ec2.setWidth(fieldWidth);
        ec2.setX(ComponentEF.left(typeBox));
        ec2.setY(ComponentEF.bottom(typeBox).add(V_GAP));
        ec2.setHeight(FIELD_HEIGHT_EXP);
        add(hightlightOuterPanel, ec2);
        ExplicitConstraints ec3 = new ExplicitConstraints(hightlightInnerPanel);
        ec3.setWidth(fieldWidth);
        ec3.setX(ComponentEF.left(typeBox));
        ec3.setY(ComponentEF.bottom(hightlightOuterPanel).add(V_GAP));
        ec3.setHeight(FIELD_HEIGHT_EXP);
        add(hightlightInnerPanel, ec3);
        ExplicitConstraints ec4 = new ExplicitConstraints(shadowOuterPanel);
        ec4.setWidth(fieldWidth);
        ec4.setX(ComponentEF.left(typeBox));
        ec4.setY(ComponentEF.bottom(hightlightInnerPanel).add(V_GAP));
        ec4.setHeight(FIELD_HEIGHT_EXP);
        add(shadowOuterPanel, ec4);
        ExplicitConstraints ec5 = new ExplicitConstraints(shadowInnerPanel);
        ec5.setWidth(fieldWidth);
        ec5.setX(ComponentEF.left(typeBox));
        ec5.setY(ComponentEF.bottom(shadowOuterPanel).add(V_GAP));
        ec5.setHeight(FIELD_HEIGHT_EXP);
        add(shadowInnerPanel, ec5);

        // Initialization
        if (border != null && border.getBevelType() == SoftBevelBorder.RAISED)
            typeBox.setSelectedItem(RAISED);
        else
            typeBox.setSelectedItem(LOWERED);

        typeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) typeBox.getSelectedItem();
                int type = SoftBevelBorder.RAISED;
                if (selectedItem.equals(LOWERED))
                    type = SoftBevelBorder.LOWERED;
                SoftBevelBorder oldBorder = border;
                border = new SoftBevelBorder(type,
                                         border.getHighlightOuterColor(samplePanel),
                                         border.getHighlightInnerColor(samplePanel),
                                         border.getShadowOuterColor(samplePanel),
                                         border.getShadowInnerColor(samplePanel));
                firePropertyChange("Border", oldBorder, border);
//                if (samplePanel != null)
//                    samplePanel.setBorder(border);
            }
        });
        hightlightOuterPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color hightlightOuterColor = (Color) evt.getNewValue();
                    if (hightlightOuterColor == null) return;
                    SoftBevelBorder oldBorder = border;
                    border = new SoftBevelBorder(border.getBevelType(),
                                             hightlightOuterColor,
                                             border.getHighlightInnerColor(samplePanel),
                                             border.getShadowOuterColor(samplePanel),
                                             border.getShadowInnerColor(samplePanel));
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
        hightlightInnerPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color hightlightInnerColor = (Color) evt.getNewValue();
                    if (hightlightInnerColor == null) return;
                    SoftBevelBorder oldBorder = border;
                    border = new SoftBevelBorder(border.getBevelType(),
                                             border.getHighlightOuterColor(samplePanel),
                                             hightlightInnerColor,
                                             border.getShadowOuterColor(samplePanel),
                                             border.getShadowInnerColor(samplePanel));
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
        shadowOuterPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color shadowOuterColor = (Color) evt.getNewValue();
                    if (shadowOuterColor == null) return;
                    SoftBevelBorder oldBorder = border;
                    border = new SoftBevelBorder(border.getBevelType(),
                                             border.getHighlightOuterColor(samplePanel),
                                             border.getHighlightInnerColor(samplePanel),
                                             shadowOuterColor,
                                             border.getShadowInnerColor(samplePanel));
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
        shadowInnerPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    Color shadowInnerColor = (Color) evt.getNewValue();
                    if (shadowInnerColor == null) return;
                    SoftBevelBorder oldBorder = border;
                    border = new SoftBevelBorder(border.getBevelType(),
                                             border.getHighlightOuterColor(samplePanel),
                                             border.getHighlightInnerColor(samplePanel),
                                             border.getShadowOuterColor(samplePanel),
                                             shadowInnerColor);
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
    }

    public SoftBevelBorder getSoftBevelBorder() {
        return border;
    }

    public void setSoftBevelBorder(SoftBevelBorder b, java.awt.Component c) {
        border = b;
        if (border.getBevelType() == SoftBevelBorder.RAISED)
            typeBox.setSelectedItem(RAISED);
        else
            typeBox.setSelectedItem(LOWERED);
        hightlightOuterPanel.panel.setBackground(border.getHighlightOuterColor(c));
        hightlightInnerPanel.panel.setBackground(border.getHighlightInnerColor(c));
        shadowOuterPanel.panel.setBackground(border.getShadowOuterColor(c));
        shadowInnerPanel.panel.setBackground(border.getShadowInnerColor(c));
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}
