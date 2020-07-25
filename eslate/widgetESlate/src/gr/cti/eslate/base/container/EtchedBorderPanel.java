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
import javax.swing.border.EtchedBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.MathEF;

public class EtchedBorderPanel extends JPanel {
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int FIELD_HEIGHT = 20;
    public static final Expression FIELD_HEIGHT_EXP = MathEF.constant(FIELD_HEIGHT);
    JComboBox typeBox;
    ColorChoosePanel hightlightPanel;
    ColorChoosePanel shadowPanel;
    private boolean localeIsGreek = false;
    private ResourceBundle etchedBorderPanelBundle;
    private static String LOWERED;
    private static String RAISED;
    EtchedBorder border;

    public EtchedBorderPanel(EtchedBorder b, final JPanel samplePanel) {
        super(true);
        this.border = b;
        etchedBorderPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.EtchedBorderPanelBundle", Locale.getDefault());
        if (etchedBorderPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.EtchedBorderPanelBundle_el_GR"))
            localeIsGreek = true;

        LOWERED = etchedBorderPanelBundle.getString("Lowered");
        RAISED = etchedBorderPanelBundle.getString("Raised");
        Object[] o = new Object[] {LOWERED, RAISED};
        typeBox = new JComboBox(o);

        hightlightPanel = new ColorChoosePanel(true,
                                      etchedBorderPanelBundle.getString("HighlightColor"),
                                      border.getHighlightColor(samplePanel));
        shadowPanel = new ColorChoosePanel(true,
                                      etchedBorderPanelBundle.getString("ShadowColor"),
                                      border.getShadowColor(samplePanel));

        int maxLabelWidth = SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {hightlightPanel, shadowPanel}, 0, H_GAP);
        ExplicitLayout el = new ExplicitLayout();
        setLayout(el);
        Expression fieldWidth = ComponentEF.preferredWidth(shadowPanel);
        ExplicitConstraints ec1 = new ExplicitConstraints(typeBox);
        ec1.setWidth(fieldWidth);
        ec1.setOriginX(ExplicitConstraints.CENTER);
        ec1.setX(ContainerEF.widthFraction(this, 0.5d));
        ec1.setY(ContainerEF.top(this));
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
        if (border != null && border.getEtchType() == EtchedBorder.RAISED)
            typeBox.setSelectedItem(RAISED);
        else
            typeBox.setSelectedItem(LOWERED);

        typeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) typeBox.getSelectedItem();
                int type = EtchedBorder.RAISED;
                if (selectedItem.equals(LOWERED))
                    type = EtchedBorder.LOWERED;
                EtchedBorder oldBorder = border;
                border = new EtchedBorder(type,
                                         border.getHighlightColor(samplePanel),
                                         border.getShadowColor(samplePanel));
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
                    EtchedBorder oldBorder = border;
                    border = new EtchedBorder(border.getEtchType(),
                                             hightlightColor,
                                             border.getShadowColor(samplePanel));
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
                    EtchedBorder oldBorder = border;
                    border = new EtchedBorder(border.getEtchType(),
                                             border.getHighlightColor(samplePanel),
                                             shadowColor);
                    firePropertyChange("Border", oldBorder, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });
    }

    public EtchedBorder getEtchedBorder() {
        return border;
    }

    public void setEtchedBorder(EtchedBorder b, java.awt.Component c) {
        border = b;
        if (border.getEtchType() == EtchedBorder.RAISED)
            typeBox.setSelectedItem(RAISED);
        else
            typeBox.setSelectedItem(LOWERED);
        hightlightPanel.panel.setBackground(border.getHighlightColor(c));
        shadowPanel.panel.setBackground(border.getHighlightColor(c));
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}
