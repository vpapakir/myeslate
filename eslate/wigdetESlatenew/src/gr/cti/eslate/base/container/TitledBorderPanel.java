package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.zookitec.layout.ExplicitLayout;


public class TitledBorderPanel extends JPanel {
    ColorChoosePanel colorPanel;
    JLabel titleLabel, justificationLabel, positionLabel, fontLabel;
    JTextField titleFld;
    JComboBox justificationBox, positionBox;
    JButton fontButton;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;
    private ResourceBundle titledBorderPanelBundle;
    TitledBorder border;
    private static String LEFT, RIGHT, CENTER;
    private static String ABOVE_TOP, TOP, BELOW_TOP, ABOVE_BOTTOM, BOTTOM, BELOW_BOTTOM;
    private String oldTitle = null;

    public TitledBorderPanel(TitledBorder b) {
        super(true);
        this.border = b;
        titledBorderPanelBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.TitledBorderPanelBundle", Locale.getDefault());
        if (titledBorderPanelBundle.getClass().getName().equals("gr.cti.eslate.base.container.TitledBorderPanelBundle_el_GR"))
            localeIsGreek = true;

        LEFT = titledBorderPanelBundle.getString("Left");
        RIGHT = titledBorderPanelBundle.getString("Right");
        CENTER = titledBorderPanelBundle.getString("Center");
        ABOVE_TOP = titledBorderPanelBundle.getString("AboveTop");
        TOP = titledBorderPanelBundle.getString("Top");
        BELOW_TOP = titledBorderPanelBundle.getString("BelowTop");
        ABOVE_BOTTOM = titledBorderPanelBundle.getString("AboveBottom");
        BOTTOM = titledBorderPanelBundle.getString("Bottom");
        BELOW_BOTTOM = titledBorderPanelBundle.getString("BelowBottom");

        titleLabel = new JLabel(titledBorderPanelBundle.getString("Title"));
        titleFld = new JTextField();
        Dimension fieldSize = new Dimension(160, 20);
        titleFld.setMinimumSize(fieldSize);
        titleFld.setMaximumSize(fieldSize);
        titleFld.setPreferredSize(fieldSize);

        JPanel titlePanel = new JPanel(true);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(3));
        titlePanel.add(titleFld);

        justificationLabel = new JLabel(titledBorderPanelBundle.getString("Justification"));
        Object[] justifications = new Object[] {LEFT, CENTER, RIGHT};
        justificationBox = new JComboBox(justifications);
//        Dimension fieldSize = new Dimension(100, 20);
        justificationBox.setMinimumSize(fieldSize);
        justificationBox.setMaximumSize(fieldSize);
        justificationBox.setPreferredSize(fieldSize);

        JPanel justificationPanel = new JPanel(true);
        justificationPanel.setLayout(new BoxLayout(justificationPanel, BoxLayout.X_AXIS));
        justificationPanel.add(justificationLabel);
        justificationPanel.add(Box.createHorizontalStrut(3));
        justificationPanel.add(justificationBox);

        positionLabel = new JLabel(titledBorderPanelBundle.getString("Position"));
        Object[] positions = new Object[] {ABOVE_TOP, TOP, BELOW_TOP, ABOVE_BOTTOM, BOTTOM, BELOW_BOTTOM};
        positionBox = new JComboBox(positions);
//        Dimension fieldSize = new Dimension(100, 20);
        positionBox.setMinimumSize(fieldSize);
        positionBox.setMaximumSize(fieldSize);
        positionBox.setPreferredSize(fieldSize);

        JPanel positionPanel = new JPanel(true);
        positionPanel.setLayout(new BoxLayout(positionPanel, BoxLayout.X_AXIS));
        positionPanel.add(positionLabel);
        positionPanel.add(Box.createHorizontalStrut(3));
        positionPanel.add(positionBox);

        fontLabel = new JLabel(titledBorderPanelBundle.getString("Font"));
        fontButton = new JButton(titledBorderPanelBundle.getString("ChooseFont"));
//        Dimension fieldSize = new Dimension(100, 20);
        fontButton.setMinimumSize(fieldSize);
        fontButton.setMaximumSize(fieldSize);
        fontButton.setPreferredSize(fieldSize);

        JPanel fontPanel = new JPanel(true);
        fontPanel.setLayout(new BoxLayout(fontPanel, BoxLayout.X_AXIS));
        fontPanel.add(fontLabel);
        fontPanel.add(Box.createHorizontalStrut(3));
        fontPanel.add(fontButton);

        colorPanel = new ColorChoosePanel(true, null, border.getTitleColor());
        ExplicitLayout el = (ExplicitLayout) colorPanel.getLayout();
        int colorPanelLabelWidth = (int) el.getConstraints(colorPanel.label).getWidth().getValue(el);

        java.awt.FontMetrics fm = getToolkit().getFontMetrics(titleLabel.getFont());

        int maxLabelWidth = fm.stringWidth(titleLabel.getText());
        int w = fm.stringWidth(justificationLabel.getText());
        if (maxLabelWidth < w) maxLabelWidth = w;
        w = fm.stringWidth(positionLabel.getText());
        if (maxLabelWidth < w) maxLabelWidth = w;
        w = fm.stringWidth(fontLabel.getText());
        if (maxLabelWidth < w) maxLabelWidth = w;
//        w = colorPanel.getLabelWidth();
        if (maxLabelWidth < w) maxLabelWidth = w;

        Dimension labelDim = new Dimension(maxLabelWidth+3, 20);
        titleLabel.setMaximumSize(labelDim);
        titleLabel.setMinimumSize(labelDim);
        titleLabel.setPreferredSize(labelDim);
        justificationLabel.setMaximumSize(labelDim);
        justificationLabel.setMinimumSize(labelDim);
        justificationLabel.setPreferredSize(labelDim);
        positionLabel.setMaximumSize(labelDim);
        positionLabel.setMinimumSize(labelDim);
        positionLabel.setPreferredSize(labelDim);
        fontLabel.setMaximumSize(labelDim);
        fontLabel.setMinimumSize(labelDim);
        fontLabel.setPreferredSize(labelDim);
        SpinField.alignLabelsOfLabeledPanels(new LabeledPanel[] {colorPanel}, 0, labelDim.width-colorPanelLabelWidth+3);

        titlePanel.setAlignmentX(CENTER_ALIGNMENT);
        justificationPanel.setAlignmentX(CENTER_ALIGNMENT);
        positionPanel.setAlignmentX(CENTER_ALIGNMENT);
        fontPanel.setAlignmentX(CENTER_ALIGNMENT);
        colorPanel.setAlignmentX(CENTER_ALIGNMENT);

        int maxPanelWidth = (maxLabelWidth+3) + 3 + 160;
        Dimension panelDim = new Dimension(maxPanelWidth, 20);
        titlePanel.setMaximumSize(panelDim);
        titlePanel.setMinimumSize(panelDim);
        titlePanel.setPreferredSize(panelDim);
        justificationPanel.setMaximumSize(panelDim);
        justificationPanel.setMinimumSize(panelDim);
        justificationPanel.setPreferredSize(panelDim);
        positionPanel.setMaximumSize(panelDim);
        positionPanel.setMinimumSize(panelDim);
        positionPanel.setPreferredSize(panelDim);
        fontPanel.setMaximumSize(panelDim);
        fontPanel.setMinimumSize(panelDim);
        fontPanel.setPreferredSize(panelDim);
        colorPanel.setMaximumSize(panelDim);
        colorPanel.setMinimumSize(panelDim);
        colorPanel.setPreferredSize(panelDim);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(Box.createVerticalStrut(3));
        add(justificationPanel);
        add(Box.createVerticalStrut(3));
        add(positionPanel);
        add(Box.createVerticalStrut(3));
        add(fontPanel);
        add(Box.createVerticalStrut(3));
        add(colorPanel);

        titleFld.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
//                Border oldBorder = border;
                String newTitle = ((JTextField) e.getSource()).getText();
                border.setTitle(newTitle);
//                System.out.println("Firing event for new title: " + border.getTitle());
                firePropertyChange("Border", null, border);
            }
        });
        justificationBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String item = (String) justificationBox.getSelectedItem();
                int newJustification = TitledBorder.LEFT;
                if (item == CENTER)
                    newJustification = TitledBorder.CENTER;
                else if (item == RIGHT)
                    newJustification = TitledBorder.RIGHT;
//                Border oldBorder = border;
                border.setTitleJustification(newJustification);
                firePropertyChange("Border", null, border);
            }
        });
        positionBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String item = (String) positionBox.getSelectedItem();
                int newPosition = TitledBorder.ABOVE_TOP;
                if (item == TOP)
                    newPosition = TitledBorder.TOP;
                else if (item == BELOW_TOP)
                    newPosition = TitledBorder.BELOW_TOP;
                else if (item == ABOVE_BOTTOM)
                    newPosition = TitledBorder.ABOVE_BOTTOM;
                else if (item == BOTTOM)
                    newPosition = TitledBorder.BOTTOM;
                else if (item == BELOW_BOTTOM)
                    newPosition = TitledBorder.BELOW_BOTTOM;

//                Border oldBorder = border;
                border.setTitlePosition(newPosition);
                firePropertyChange("Border", null, border);
            }
        });
        fontButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame topLevelframe = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, TitledBorderPanel.this);
                Dialog parentDialog = null;
                if (topLevelframe == null)
                    parentDialog = (Dialog) SwingUtilities.getAncestorOfClass(Dialog.class, TitledBorderPanel.this);
                FontEditorDialog fed = null;
                if (topLevelframe != null)
                    fed = new FontEditorDialog(topLevelframe, border.getTitleFont());
                else
                    fed = new FontEditorDialog(parentDialog, border.getTitleFont());
                fed.showDialog(TitledBorderPanel.this);
//              Border oldBorder = border;
                border.setTitleFont(fed.getFont());
                firePropertyChange("Border", null, border);
            }
        });
        colorPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("Color")) {
                    int t = 1;
//                  Border oldBorder = border;
                    border.setTitleColor((Color) evt.getNewValue());
                    firePropertyChange("Border", null, border);
//                    if (samplePanel != null)
//                        samplePanel.setBorder(border);
                }
            }
        });

        // Initialization
        titleFld.setText(border.getTitle());
        if (border.getTitleJustification() == TitledBorder.LEFT)
            justificationBox.setSelectedItem(LEFT);
        else if (border.getTitleJustification() == TitledBorder.CENTER)
            justificationBox.setSelectedItem(CENTER);
        else
            justificationBox.setSelectedItem(RIGHT);
        if (border.getTitlePosition() == TitledBorder.ABOVE_TOP)
            positionBox.setSelectedItem(ABOVE_TOP);
        else if (border.getTitlePosition() == TitledBorder.TOP)
            positionBox.setSelectedItem(TOP);
        else if (border.getTitlePosition() == TitledBorder.BELOW_TOP)
            positionBox.setSelectedItem(BELOW_TOP);
        else if (border.getTitlePosition() == TitledBorder.ABOVE_BOTTOM)
            positionBox.setSelectedItem(ABOVE_BOTTOM);
        else if (border.getTitlePosition() == TitledBorder.BOTTOM)
            positionBox.setSelectedItem(BOTTOM);
        else if (border.getTitlePosition() == TitledBorder.BELOW_BOTTOM)
            positionBox.setSelectedItem(BELOW_BOTTOM);
    }

    public TitledBorder getTitledBorder() {
        return border;
    }

    public void setTitledBorder(TitledBorder b) {
        border = b;
        titleFld.setText(border.getTitle());
        if (border.getTitleJustification() == TitledBorder.LEFT)
            justificationBox.setSelectedItem(LEFT);
        else if (border.getTitleJustification() == TitledBorder.CENTER)
            justificationBox.setSelectedItem(CENTER);
        else
            justificationBox.setSelectedItem(RIGHT);
        if (border.getTitlePosition() == TitledBorder.ABOVE_TOP)
            positionBox.setSelectedItem(ABOVE_TOP);
        else if (border.getTitlePosition() == TitledBorder.TOP)
            positionBox.setSelectedItem(TOP);
        else if (border.getTitlePosition() == TitledBorder.BELOW_TOP)
            positionBox.setSelectedItem(BELOW_TOP);
        else if (border.getTitlePosition() == TitledBorder.ABOVE_BOTTOM)
            positionBox.setSelectedItem(ABOVE_BOTTOM);
        else if (border.getTitlePosition() == TitledBorder.BOTTOM)
            positionBox.setSelectedItem(BOTTOM);
        else if (border.getTitlePosition() == TitledBorder.BELOW_BOTTOM)
            positionBox.setSelectedItem(BELOW_BOTTOM);
        colorPanel.panel.setBackground(border.getTitleColor());
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}
