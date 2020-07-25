package gr.cti.eslate.base.container;

import gr.cti.eslate.spinButton.ValueChangedEvent;
import gr.cti.eslate.spinButton.ValueChangedListener;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


public class LayoutPropertyEditor extends PropertyEditorSupport {
    static final String BORDER = "BorderLayout";
    static final String BOX = "BoxLayout";
    static final String CARD = "CardLayout";
    static final String FLOW = "FlowLayout";
    static final String GRID = "GridLayout";
    static final String NONE = "NullLayout";
    LayoutManager layout;
    PropertyChangeSupport pcs;
    static ResourceBundle layoutPropertyEditorBundle =
        ResourceBundle.getBundle("gr.cti.eslate.base.container.LayoutPropertyEditorBundle", Locale.getDefault());
//    boolean localeIsGreek = false;
    /* The component to which this layout applys.
     */
    Component component = null;
    NoBorderButton editButton;

    public LayoutPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
//        if (layoutPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.LayoutPropertyEditorBundle_el_GR"))
//            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
//        System.out.println("1. LayoutPropertyEditor setValue(): " + value);
        if (value != null && !LayoutManager.class.isInstance(value))
            return;
//        layout = (LayoutManager) value;
        if (layout == null && value == null)
            return;
        if (layout != null && layout.equals(value))
            return;

        LayoutManager tmp = layout;
        layout = (LayoutManager) value;
        if (editButton != null)
            editButton.setText(layoutMgr2String(layout));
//        System.out.println("2. LayoutPropertyEditor setValue(): " + value);
        pcs.firePropertyChange("LayoutManager", tmp, layout);
    }

    public Object getValue() {
        return layout;
    }

    public void setComponent(Component comp) {
        this.component = comp;
    }

    public java.awt.Component getCustomEditor() {
        editButton = new NoBorderButton();
        editButton.setText(layoutMgr2String(layout));
        //layoutPropertyEditorBundle.getString("EditLayout"));
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        editButton.setMargin(zeroInsets);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, editButton);
                new LayoutDialog(topLevelFrame, LayoutPropertyEditor.this, layout);
            }
        });
        return editButton;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public void revalidateComponent() {
        component.invalidate();
        component.doLayout();
        component.repaint();
    }

    public String layoutMgr2String(LayoutManager mgr) {
        if (mgr == null)
            return layoutPropertyEditorBundle.getString(NONE);
        if (BorderLayout.class.isAssignableFrom(mgr.getClass()))
            return layoutPropertyEditorBundle.getString(BORDER);
        if (FlowLayout.class.isAssignableFrom(mgr.getClass()))
            return layoutPropertyEditorBundle.getString(FLOW);
        if (GridLayout.class.isAssignableFrom(mgr.getClass()))
            return layoutPropertyEditorBundle.getString(GRID);
        if (BoxLayout.class.isAssignableFrom(mgr.getClass()))
            return layoutPropertyEditorBundle.getString(BOX);
        if (CardLayout.class.isAssignableFrom(mgr.getClass()))
            return layoutPropertyEditorBundle.getString(CARD);
        return layoutPropertyEditorBundle.getString("UnknownLayout");
    }
}

class LayoutDialog extends JDialog {
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    Locale locale;
//    boolean localeIsGreek = false;
    ResourceBundle layoutDialogBundle;
    JList managerList;
    JScrollPane scrollPane;
    SpinField hGap, vGap, rows, columns;
    JComboBox axis;
    JComboBox alignment;
//    JTextField cardField;
    JButton nextCard, prevCard;
    JPanel selectedCardPanel;
    JPanel alignmentPanel, axisPanel;
    JPanel layoutMgrSettingsPanel;
    JPanel mainPanel;
    LayoutPropertyEditor layoutEditor;
    LayoutManager currentLayout;

    public LayoutDialog(java.awt.Frame parentFrame, LayoutPropertyEditor editor, LayoutManager currentManager) {
        super(parentFrame, true);
        layoutEditor = editor;
        currentLayout = currentManager;
//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());

//        locale = Locale.getDefault();
        layoutDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.LayoutDialogBundle", Locale.getDefault());
//        if (layoutDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.LayoutDialogBundle_el_GR"))
//            localeIsGreek = true;

        setTitle(layoutDialogBundle.getString("DialogTitle"));

        String[] supportedManagers = new String[6];
        supportedManagers[0] = LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.BORDER);
        supportedManagers[1] = LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.BOX);
        supportedManagers[2] = LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.CARD);
        supportedManagers[3] = LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.FLOW);
        supportedManagers[4] = LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.GRID);
        supportedManagers[5] = LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.NONE);
        managerList = new JList(supportedManagers);
        scrollPane = new JScrollPane(managerList);

//        if (localeIsGreek)
//            managerList.setFont(greekUIFont);

        managerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        hGap = new SpinField(true, layoutDialogBundle.getString("HGap"), 0);
        vGap = new SpinField(true, layoutDialogBundle.getString("VGap"), 0);
        rows = new SpinField(true, layoutDialogBundle.getString("Rows"), 1);
        columns = new SpinField(true, layoutDialogBundle.getString("Columns"), 1);

        SpinField[] spins = new SpinField[] {hGap, vGap, rows, columns};
        SpinField.alignLabelsOfLabeledPanels(spins, 0, 5);
        SpinField.setSpinFieldSpinSize(spins, 45, 20);

        hGap.setMinValue(0);
        vGap.setMinValue(0);
        rows.setMinValue(0);
        columns.setMinValue(0);

        JLabel axisLabel = new JLabel(layoutDialogBundle.getString("Axis"));
        String[] str = new String[2];
        str[0] = layoutDialogBundle.getString("XAXIS");
        str[1] = layoutDialogBundle.getString("YAXIS");
        axis = new JComboBox(str);
        axisPanel = new JPanel(true);
        axisPanel.setLayout(new BoxLayout(axisPanel, BoxLayout.X_AXIS));
        axisPanel.add(axisLabel);
        axisPanel.add(Box.createHorizontalStrut(3));
        axisPanel.add(axis);

        JLabel alignLabel = new JLabel(layoutDialogBundle.getString("Alignment"));
        str = new String[3];
        str[0] = layoutDialogBundle.getString("CENTER");
        str[1] = layoutDialogBundle.getString("LEFT");
        str[2] = layoutDialogBundle.getString("RIGHT");
        alignment = new JComboBox(str);
        alignmentPanel = new JPanel(true);
        alignmentPanel.setLayout(new BoxLayout(alignmentPanel, BoxLayout.X_AXIS));
        alignmentPanel.add(alignLabel);
        alignmentPanel.add(Box.createHorizontalStrut(3));
        alignmentPanel.add(alignment);

        nextCard = new JButton(layoutDialogBundle.getString("NextCard"));
        prevCard = new JButton(layoutDialogBundle.getString("PreviousCard"));
        prevCard.setAlignmentX(CENTER_ALIGNMENT);
        nextCard.setAlignmentX(CENTER_ALIGNMENT);
        Insets zeroInsets = new Insets(0,0,0,0);
        prevCard.setMargin(zeroInsets);
        nextCard.setMargin(zeroInsets);
        java.awt.FontMetrics fm1 = hGap.label.getFontMetrics(hGap.label.getFont());
        int buttonWidth = fm1.stringWidth(layoutDialogBundle.getString("NextCard"));
        int w = fm1.stringWidth(layoutDialogBundle.getString("PreviousCard"));
        if (buttonWidth < w) buttonWidth = w;
        Dimension buttonSize = new Dimension(buttonWidth+8, 20);
        prevCard.setMaximumSize(buttonSize);
        prevCard.setPreferredSize(buttonSize);
        prevCard.setMinimumSize(buttonSize);
        nextCard.setMaximumSize(buttonSize);
        nextCard.setPreferredSize(buttonSize);
        nextCard.setMinimumSize(buttonSize);
        selectedCardPanel = new JPanel(true);
        selectedCardPanel.setLayout(new BoxLayout(selectedCardPanel, BoxLayout.Y_AXIS));
        selectedCardPanel.add(nextCard);
        selectedCardPanel.add(Box.createHorizontalStrut(5));
        selectedCardPanel.add(prevCard);
        selectedCardPanel.add(Box.createGlue());
        selectedCardPanel.setAlignmentX(CENTER_ALIGNMENT);
        Dimension selectedCardPanelSize = new Dimension(buttonWidth+10, 45);
        selectedCardPanel.setMaximumSize(selectedCardPanelSize);
        selectedCardPanel.setPreferredSize(selectedCardPanelSize);
        selectedCardPanel.setMinimumSize(selectedCardPanelSize);

        alignment.setAlignmentX(CENTER_ALIGNMENT);

        final ExplicitLayout el = new ExplicitLayout();
        layoutMgrSettingsPanel = new JPanel(true);
        layoutMgrSettingsPanel.setLayout(el);
        layoutMgrSettingsPanel.setBorder(new EmptyBorder(0, 7, 0, 7));

        JPanel layoutPanel = new JPanel(true);
        layoutPanel.setLayout(new BorderLayout());
        layoutPanel.add(scrollPane, BorderLayout.WEST);
        layoutPanel.add(layoutMgrSettingsPanel, BorderLayout.CENTER);

        Component[] allControls = new Component[] {axisPanel, selectedCardPanel, alignmentPanel, hGap, vGap, rows, columns};
//        Expression maxWidth = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), allControls));
        Expression maxWidth = GroupEF.widthMax(allControls);
        el.setPreferredLayoutSize(maxWidth, MathEF.constant(100));

/*        JButton okButton = new JButton(layoutDialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        JButton cancelButton = new JButton(layoutDialogBundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JButton applyButton = new JButton(layoutDialogBundle.getString("Apply"));
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

        if (localeIsGreek) {
            okButton.setFont(greekUIFont);
            cancelButton.setFont(greekUIFont);
            applyButton.setFont(greekUIFont);
        }

        JPanel buttonPanel = new JPanel(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createGlue());

        buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
*/
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(layoutPanel);
        mainPanel.setBorder(new EmptyBorder(3,3,3,0));
//        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        managerList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                String currMgrName = (String) managerList.getSelectedValue();
//                System.out.println("ListSelectionListener currMgrName: " + currMgrName);

                if (currMgrName == null) return;
                if (currentLayout == null && currMgrName.equals(LayoutPropertyEditor.NONE)) return;
                if (currentLayout != null) {
                    if (currentLayout.getClass().getName().indexOf(currMgrName) != -1) return;
                }

//                System.out.println("ListSelectionListener removing all");
                layoutMgrSettingsPanel.removeAll();
                ArrayList currentControls = new ArrayList();
                if (currMgrName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.BORDER))) {
                    currentControls.add(hGap);
                    currentControls.add(vGap);
                    currentLayout = new BorderLayout(((Number) hGap.spin.getValue()).intValue(), ((Number) vGap.spin.getValue()).intValue());
                }else if (currMgrName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.BOX))) {
                    int boxLayoutAxis = BoxLayout.X_AXIS;
                    if (axis.getSelectedItem().equals(layoutDialogBundle.getString("YAXIS")))
                        boxLayoutAxis = BoxLayout.Y_AXIS;;
                    currentLayout = new BoxLayout((java.awt.Container) layoutEditor.component, boxLayoutAxis);
                    currentControls.add(axisPanel);
                }else if (currMgrName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.CARD))) {
                    currentLayout = new CardLayout(((Number) hGap.spin.getValue()).intValue(), ((Number) vGap.spin.getValue()).intValue());
                    currentControls.add(hGap);
                    currentControls.add(vGap);
                    currentControls.add(selectedCardPanel);
                }else if (currMgrName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.FLOW))) {
                    int align;
                    if (alignment.getSelectedItem().equals(layoutDialogBundle.getString("CENTER")))
                        align = FlowLayout.CENTER;
                    else if (alignment.getSelectedItem().equals(layoutDialogBundle.getString("LEFT")))
                        align = FlowLayout.LEFT;
                    else
                        align = FlowLayout.RIGHT;
                    currentLayout = new FlowLayout(align, ((Number) hGap.spin.getValue()).intValue(), ((Number) vGap.spin.getValue()).intValue());
                    currentControls.add(hGap);
                    currentControls.add(vGap);
                    currentControls.add(alignmentPanel);
                }else if (currMgrName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.GRID))) {
                    currentLayout = new GridLayout(((Number) rows.spin.getValue()).intValue(),
                                                   ((Number) columns.spin.getValue()).intValue(),
                                                   ((Number) hGap.spin.getValue()).intValue(),
                                                   ((Number) vGap.spin.getValue()).intValue());
                    currentControls.add(hGap);
                    currentControls.add(vGap);
                    currentControls.add(rows);
                    currentControls.add(columns);
                }else{
                    currentLayout = null;
                }

                if (currentControls.size() > 0) {
                    Component[] controls = new Component[currentControls.size()];
                    for (int i=0; i<controls.length; i++)
                        controls[i] = (Component) currentControls.get(i);

                    Expression maxWidth = GroupEF.preferredWidthMax(controls).add(10);
                    ExplicitConstraints ec1 = new ExplicitConstraints(controls[0]);
                    ec1.setWidth(maxWidth);
                    ec1.setX((ContainerEF.width((Container) controls[0]).add(7+7).subtract(maxWidth)).divide(2));
                    ec1.setY(ContainerEF.top(layoutMgrSettingsPanel));
                    layoutMgrSettingsPanel.add(controls[0], ec1);
                    for (int i=1; i<controls.length; i++) {
                        ExplicitConstraints ec = new ExplicitConstraints(controls[i]);
                        ec.setWidth(maxWidth);
                        ec.setX(ComponentEF.left(controls[0]));
                        ec.setY(ComponentEF.bottom(controls[i-1]).add(5)); //5 is the inter-control vertical gap
                        layoutMgrSettingsPanel.add(controls[i], ec);
                    }
                }

                setResizable(true);
                pack();
//                System.out.println("ListSelectionListener mainPanel: " + mainPanel.getPreferredSize());
                setResizable(false);

                layoutEditor.setValue(currentLayout);
                layoutEditor.revalidateComponent();
            }
        });

        hGap.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newHGap = ((Number) evt.getValue()).intValue();
                setHGap(newHGap);
            }
        });
        vGap.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int newVGap = ((Number) evt.getValue()).intValue();
                setVGap(newVGap);
            }
        });
        rows.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int rowNum = ((Number) evt.getValue()).intValue();
                setRows(rowNum);
            }
        });
        columns.addValueChangedListener(new ValueChangedListener() {
            public void valueChanged(ValueChangedEvent evt) {
                int colNum = ((Number) evt.getValue()).intValue();
                setColumns(colNum);
            }
        });
        alignment.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (currentLayout == null) return;
                int align = FlowLayout.CENTER;
                if (alignment.getSelectedItem().equals(layoutDialogBundle.getString("LEFT")))
                    align = FlowLayout.LEFT;
                else if (alignment.getSelectedItem().equals(layoutDialogBundle.getString("RIGHT")))
                    align = FlowLayout.RIGHT;
                if (FlowLayout.class.isAssignableFrom(currentLayout.getClass())) {
                    ((FlowLayout) currentLayout).setAlignment(align);
                    layoutEditor.revalidateComponent();
                }
            }
        });
        axis.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (currentLayout == null) return;
                int boxLayoutAxis = BoxLayout.X_AXIS;
                if (axis.getSelectedItem().equals(layoutDialogBundle.getString("YAXIS")))
                    boxLayoutAxis = BoxLayout.Y_AXIS;;
                if (BoxLayout.class.isAssignableFrom(currentLayout.getClass())) {
                    currentLayout = new BoxLayout((java.awt.Container)layoutEditor.component, boxLayoutAxis);
                    layoutEditor.setValue(currentLayout);
                    layoutEditor.revalidateComponent();
                }
            }
        });
        nextCard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentLayout == null) return;
                if (CardLayout.class.isAssignableFrom(currentLayout.getClass())) {
                    ((CardLayout) currentLayout).next((java.awt.Container)layoutEditor.component);
                    layoutEditor.revalidateComponent();
                }
            }
        });
        prevCard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentLayout == null) return;
                if (CardLayout.class.isAssignableFrom(currentLayout.getClass())) {
                    ((CardLayout) currentLayout).previous((java.awt.Container)layoutEditor.component);
                    layoutEditor.revalidateComponent();
                }
            }
        });


        // Initialization
        String currentManagerName = null;
        if (currentManager != null) {
            currentManagerName = currentManager.getClass().getName();
            currentManagerName = currentManagerName.substring(currentManagerName.lastIndexOf('.')+1);
        }
        if (currentManagerName == null) {
            managerList.setSelectedValue(LayoutPropertyEditor.NONE, true);
        }else{
            managerList.setSelectedValue(currentManagerName, true);
        }
//        System.out.println("Initialization currentManager: " + currentManagerName);
        if (currentManagerName != null) {
            if (currentManagerName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.BORDER))) {
                hGap.spin.setValue(new Integer(((BorderLayout) currentManager).getHgap()));
                vGap.spin.setValue(new Integer(((BorderLayout) currentManager).getVgap()));
/*sp                hGap.field.setText(((BorderLayout) currentManager).getHgap());
                vGap.field.setText(((BorderLayout) currentManager).getVgap());
*/
                layoutMgrSettingsPanel.add(hGap);
                layoutMgrSettingsPanel.add(vGap);
            }else if (currentManagerName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.BOX))) {
//                System.out.println("BOX currentManager: " + currentManager);
                layoutMgrSettingsPanel.add(axisPanel);
                float xAlignment = ((BoxLayout) currentManager).getLayoutAlignmentX((java.awt.Container) editor.component);
                if (xAlignment == 0.5f)
                    axis.setSelectedItem(layoutDialogBundle.getString("XAXIS"));
                else
                    axis.setSelectedItem(layoutDialogBundle.getString("YAXIS"));
//                System.out.println("X Alignment: " + ((BoxLayout) currentManager).getLayoutAlignmentX((java.awt.Container) editor.component));
//                System.out.println("Y Alignment: " + ((BoxLayout) currentManager).getLayoutAlignmentY((java.awt.Container) editor.component));
            }else if (currentManagerName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.CARD))) {
                layoutMgrSettingsPanel.add(hGap);
                layoutMgrSettingsPanel.add(vGap);
                layoutMgrSettingsPanel.add(selectedCardPanel);
                hGap.spin.setValue(new Integer(((CardLayout) currentManager).getHgap()));
                vGap.spin.setValue(new Integer(((CardLayout) currentManager).getVgap()));
/*sp                hGap.field.setText(((CardLayout) currentManager).getHgap());
                vGap.field.setText(((CardLayout) currentManager).getVgap());
*/
            }else if (currentManagerName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.FLOW))) {
                layoutMgrSettingsPanel.add(hGap);
                layoutMgrSettingsPanel.add(vGap);
                layoutMgrSettingsPanel.add(alignmentPanel);
                hGap.spin.setValue(new Integer(((FlowLayout) currentManager).getHgap()));
                vGap.spin.setValue(new Integer(((FlowLayout) currentManager).getVgap()));
/*                hGap.field.setText(((FlowLayout) currentManager).getHgap());
                vGap.field.setText(((FlowLayout) currentManager).getVgap());
*/
                int allign = ((FlowLayout) currentManager).getAlignment();
                if (allign == FlowLayout.CENTER)
                    alignment.setSelectedIndex(0);
                else if (allign == FlowLayout.LEFT)
                    alignment.setSelectedIndex(1);
                else
                    alignment.setSelectedIndex(2);
            }else if (currentManagerName.equals(LayoutPropertyEditor.layoutPropertyEditorBundle.getString(LayoutPropertyEditor.GRID))) {
                layoutMgrSettingsPanel.add(hGap);
                layoutMgrSettingsPanel.add(vGap);
                layoutMgrSettingsPanel.add(rows);
                layoutMgrSettingsPanel.add(columns);
                hGap.spin.setValue(new Integer(((GridLayout) currentManager).getHgap()));
                vGap.spin.setValue(new Integer(((GridLayout) currentManager).getVgap()));
                rows.spin.setValue(new Integer(((GridLayout) currentManager).getRows()));
                columns.spin.setValue(new Integer(((GridLayout) currentManager).getColumns()));
/*                hGap.field.setText(((GridLayout) currentManager).getHgap());
                vGap.field.setText(((GridLayout) currentManager).getVgap());
                rows.field.setText(((GridLayout) currentManager).getRows());
                columns.field.setText(((GridLayout) currentManager).getColumns());
*/
            }
            layoutMgrSettingsPanel.add(Box.createGlue());
        }

        // ESCAPE
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        // COMBOBOX key listener (needs special listener, because the KeyStroke mechanism fails
        // when it has the focus.
        axis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (axis.isPopupVisible()) return;
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });
        alignment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (alignment.isPopupVisible()) return;
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
                    dispose();
            }
        });

        setResizable(true);
        pack();
        setResizable(false);
        managerList.requestFocus();
        ESlateContainerUtils.showDialog(this, null, false);
    }

    public void setHGap(int gap) {
        if (BorderLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((BorderLayout) currentLayout).setHgap(gap);
        else if (CardLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((CardLayout) currentLayout).setHgap(gap);
        else if (FlowLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((FlowLayout) currentLayout).setHgap(gap);
        else if (GridLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((GridLayout) currentLayout).setHgap(gap);
        layoutEditor.revalidateComponent();
    }

    public void setVGap(int gap) {
        if (BorderLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((BorderLayout) currentLayout).setVgap(gap);
        else if (CardLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((CardLayout) currentLayout).setVgap(gap);
        else if (FlowLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((FlowLayout) currentLayout).setVgap(gap);
        else if (GridLayout.class.isAssignableFrom(currentLayout.getClass()))
            ((GridLayout) currentLayout).setVgap(gap);
        layoutEditor.revalidateComponent();
    }

    public void setRows(int rowNum) {
        if (GridLayout.class.isAssignableFrom(currentLayout.getClass())) {
            ((GridLayout) currentLayout).setRows(rowNum);
            layoutEditor.revalidateComponent();
        }
    }

    public void setColumns(int colNum) {
        if (GridLayout.class.isAssignableFrom(currentLayout.getClass())) {
            ((GridLayout) currentLayout).setColumns(colNum);
            layoutEditor.revalidateComponent();
        }
    }
}


