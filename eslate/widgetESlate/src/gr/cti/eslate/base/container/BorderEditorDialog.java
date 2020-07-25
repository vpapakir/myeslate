package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.NoTopOneLineBevelBorder;
import gr.cti.eslate.utils.OneLineBevelBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


class BorderEditorDialog extends JDialog {
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
//    protected Font greekUIFont14 = new Font("Helvetica", Font.PLAIN, 14);
    Locale locale;
//    boolean localeIsGreek = false;
    static ResourceBundle borderDialogBundle =
      ResourceBundle.getBundle("gr.cti.eslate.base.container.BorderEditorDialogBundle", Locale.getDefault());
    public static final int CANCEL = 1;
    public static final int OK = 0;
    MyList borderList;
    JScrollPane scrollpane;
    JSplitPane splitpane;
    private int splitpaneDividerLocation = -1;
    BorderTreePanel borderTreePanel = null;

    BorderPropertyEditor borderEditor;
    Border currentBorder;
    JPanel borderPropLabelPanel;
    JPanel mainPanel, borderPropertiesPanel;
    SampleBorderPanel sampleBorderPanel;
    JPanel sampleBorderContainerPanel, buttonPanel;
    public static final String BEVELBORDER = "Bevel border";
    public static final String ONELINEBEVELBORDER = "One line bevel border";
    public static final String NOTOPONELINEBEVELBORDER = "One line bevel border without top";
    public static final String SOFTBEVELBORDER = "Soft bevel border";
    public static final String EMPTYBORDER = "Empty border";
    public static final String TITLEDBORDER = "Titled border";
    public static final String ETCHEDBORDER = "Etched border";
    public static final String LINEBORDER = "Line border";
    public static final String MATTEBORDER = "Matte border";
    public static final String COMPOUNDBORDER = "Compound border";
    public static final String NOBORDER = "No border";
    int returnCode = CANCEL;

    LineBorderPanel lineBorderPanel = null;
    OneLineBevelBorderPanel oneLineBevelBorderPanel = null;
    NoTopOneLineBevelBorderPanel noTopOneLineBevelBorderPanel = null;
    BevelBorderPanel bevelBorderPanel = null;
    SoftBevelBorderPanel softBevelBorderPanel = null;
    EtchedBorderPanel etchedBorderPanel = null;
    MatteBorderPanel matteBorderPanel = null;
    EmptyBorderPanel emptyBorderPanel = null;
    TitledBorderPanel titledBorderPanel = null;
    BorderChangeListener borderChangeListener = null;
    JButton okButton, cancelButton;

    public BorderEditorDialog(java.awt.Frame parentFrame, BorderPropertyEditor editor, Border border) {
        super(parentFrame, true);
        borderEditor = editor;
        currentBorder = border;
        borderChangeListener = new BorderChangeListener(this);
//        JFrame contentFrame = (JFrame) javax.swing.SwingUtilities.getAncestorOfClass(JFrame.class, this);
//        contentFrame.setIconImage(ESlateContainer.ESLATE_LOGO.getImage());

//        if (borderDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.BorderEditorDialogBundle_el_GR"))
//            localeIsGreek = true;

        setTitle(borderDialogBundle.getString("DialogTitle"));

        String[] supportedBorders = new String[11];
        supportedBorders[0] = borderDialogBundle.getString(BEVELBORDER);
        supportedBorders[1] = borderDialogBundle.getString(SOFTBEVELBORDER);
        supportedBorders[2] = borderDialogBundle.getString(EMPTYBORDER);
        supportedBorders[3] = borderDialogBundle.getString(LINEBORDER);
        supportedBorders[4] = borderDialogBundle.getString(ETCHEDBORDER);
        supportedBorders[5] = borderDialogBundle.getString(TITLEDBORDER);
        supportedBorders[6] = borderDialogBundle.getString(ONELINEBEVELBORDER);
        supportedBorders[7] = borderDialogBundle.getString(NOTOPONELINEBEVELBORDER);
        supportedBorders[8] = borderDialogBundle.getString(MATTEBORDER);
        supportedBorders[9] = borderDialogBundle.getString(COMPOUNDBORDER);
        supportedBorders[10] = borderDialogBundle.getString(NOBORDER);
        borderList = new MyList(supportedBorders);
//        if (localeIsGreek)
//            borderList.setFont(greekUIFont);
        borderList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        scrollpane = new JScrollPane(borderList);
        splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, scrollpane, null);
        splitpane.setAlignmentY(CENTER_ALIGNMENT);
        Dimension scrollPaneSize = new Dimension(190, 270); //130, 210);
        splitpane.setMaximumSize(scrollPaneSize);
        splitpane.setMinimumSize(scrollPaneSize);
        splitpane.setPreferredSize(scrollPaneSize);

        /* The label at the top of the 'borderPropertiesPanel' */
        JLabel borderPropLabel = new JLabel(borderDialogBundle.getString("BorderProperties"));
//        if (localeIsGreek)
//            borderPropLabel.setFont(greekUIFont14);
//        else
//            borderPropLabel.setFont(new Font(borderPropLabel.getFont().getFamily(),
//                                    borderPropLabel.getFont().getStyle(),
//                                    14));
        borderPropLabel.setForeground(new Color(119, 40, 104));
        /* The panel that contains the 'borderPropLabel' */
        borderPropLabelPanel = new JPanel(true);
        borderPropLabelPanel.setLayout(new BoxLayout(borderPropLabelPanel, BoxLayout.X_AXIS));
        borderPropLabelPanel.add(Box.createGlue());
        borderPropLabelPanel.add(borderPropLabel);
        borderPropLabelPanel.add(Box.createGlue());

        /* The panel with the properties of the current border */
        borderPropertiesPanel = new JPanel(true);
        borderPropertiesPanel.setLayout(new BorderLayout(0, 5));
        Dimension propPaneSize = new Dimension(260, 210);
        borderPropertiesPanel.setMaximumSize(propPaneSize);
        borderPropertiesPanel.setMinimumSize(propPaneSize);
        borderPropertiesPanel.setPreferredSize(propPaneSize);
        borderPropertiesPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5,0,5,0)));
        borderPropertiesPanel.add(borderPropLabelPanel, BorderLayout.NORTH);

        /* The sample panel, to which the current border is applied in order to be viewed */
        sampleBorderPanel = new SampleBorderPanel(true);
        /* The container of the sample panel */
        sampleBorderContainerPanel = new JPanel()  {
            public Dimension getPreferredSize() {
                Dimension size = getSize();
                size.height = 60;
                return size;
            }
        };
        sampleBorderContainerPanel.setLayout(new BorderLayout());
        sampleBorderContainerPanel.add(sampleBorderPanel, BorderLayout.CENTER);
        sampleBorderContainerPanel.setBorder(
              new CompoundBorder(
                  new SoftBevelBorder(SoftBevelBorder.LOWERED),
                  new EmptyBorder(5, 10, 5, 10)));

        /* The panel at the top of the dialog. It containes the list of the supported borders,
         * the panel with the current border's properties and the 'sampleBorderContainerPanel'.
         */
        JPanel topSamplePanel = new JPanel(true);
        topSamplePanel.setLayout(new BorderLayout(5,5)); //new BoxLayout(topSamplePanel, BoxLayout.Y_AXIS));
        topSamplePanel.add(splitpane, BorderLayout.CENTER);
        topSamplePanel.add(borderPropertiesPanel, BorderLayout.EAST);
        topSamplePanel.add(sampleBorderContainerPanel, BorderLayout.SOUTH);
        topSamplePanel.setBorder(new EmptyBorder(3,3,3,3));

        /* The button panel */
        okButton = new JButton(borderDialogBundle.getString("OK"));
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        Dimension buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        cancelButton = new JButton(borderDialogBundle.getString("Cancel"));
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JButton applyButton = new JButton(borderDialogBundle.getString("Apply"));
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

/*        okButton.setFont(greekUIFont);
        cancelButton.setFont(greekUIFont);
        applyButton.setFont(greekUIFont);
*/
        buttonPanel = new JPanel(true);
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(applyButton);
        buttonPanel.setBorder(new EmptyBorder(5,5,0,5));

        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(topSamplePanel);
        mainPanel.add(Box.createVerticalStrut(2));
//        mainPanel.add(sampleBorderContainerPanel);
//        mainPanel.add(Box.createVerticalStrut(2));
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        // ESCAPE
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = OK;
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = CANCEL;
                dispose();
            }
        });
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (borderEditor.component != null) {
                    borderEditor.setValue(currentBorder);
                    borderEditor.revalidateComponent();
                }
            }
        });

        borderList.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                /* If a composite border is being edited, then update the tree which
                 * descibes the composite border, as this click may have triggered the change
                 * of an internal border of the composite border.
                 */
                if (splitpane.getBottomComponent() != null) {
                    borderTreePanel.tree.revalidate();
                    borderTreePanel.tree.repaint();
                }
            }
        });

        borderList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;

                String borderName = (String) borderList.getSelectedValue();
//                System.out.println("ListSelectionListener borderName: " + borderName);

                /* borderDialogBundle.getString(LINEBORDER) block contains the description about what happens in the
                 * BEVELBORDER, borderDialogBundle.getString(SOFTBEVELBORDER), borderDialogBundle.getString(ETCHEDBORDER), borderDialogBundle.getString(MATTEBORDER) and borderDialogBundle.getString(EMPTYBORDER), too.
                 */
                if (borderName.equals(borderDialogBundle.getString(LINEBORDER))) {
                    checkForOuterBorderChange();
                    /* If the 'borderTreePanel' is not visible, then we are actually editing a
                     * "LineBorder". If however the 'borderTreePanel' is visible, then we are
                     * editing some "LineBorder" inside a composite border, like a TitledBorder
                     * or a CompoundBorder.
                     */
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        /* Only one instance of a 'LineBorderPanel' is allowed. If it does not
                         * exist, create it and initialize it based on the 'currentBorder' (i.e.
                         * the border being edited. If it already exists, then the assign the
                         * values it conveys about to the 'currentBorder'.
                         */
                        if (lineBorderPanel == null) {
                            if (currentBorder == null  || !LineBorder.class.isAssignableFrom(currentBorder.getClass()))
                                currentBorder = new LineBorder(Color.black);
                            lineBorderPanel = new LineBorderPanel((LineBorder) currentBorder, sampleBorderPanel);
                            // Attach listener who adjusts the border of the 'sampleBorderPanel', when the
                            // border settings change
                            lineBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = lineBorderPanel.getLineBorder();
                        }
                        currentBorderChanged(lineBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        /* Find the selected node in the 'tree' of the 'borderTreePanel'. If
                         * its user object is a LineBorder, then we simply initialize or set the
                         * 'lineBorderPanel' and display it. In this case we are simply editing
                         * a "LineBorder" inside a composite border. If however the user object
                         * of the selected node is a border of other type than "LineBorder", then
                         * this sub-border is changed to be a "LineBorder".
                         */
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!LineBorder.class.isAssignableFrom(selectedBorder.getClass())) {
                            selectedBorder = new LineBorder(Color.red);
                            /* If the selected node has children, then remove them. The childen are
                             * there because the border that is substituted by the LineBorder was
                             * a composite one.
                             */
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (lineBorderPanel == null) {
                            lineBorderPanel = new LineBorderPanel((LineBorder) selectedBorder, sampleBorderPanel);
                            // Attach listener who adjusts the border of the 'sampleBorderPanel', when the
                            // border settings change
                            lineBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
//                            System.out.println("selectedNode.border thickness: " + ((LineBorder) selectedBorder).getThickness());
                            lineBorderPanel.setLineBorder((LineBorder) selectedBorder);
                        }
                        currentBorderChanged(lineBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(ONELINEBEVELBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        if (oneLineBevelBorderPanel == null) {
                            if (currentBorder == null || !currentBorder.getClass().getName().equals("gr.cti.eslate.utils.OneLineBevelBorder"))
                                currentBorder = new OneLineBevelBorder(OneLineBevelBorder.RAISED);
                            oneLineBevelBorderPanel = new OneLineBevelBorderPanel((OneLineBevelBorder) currentBorder, sampleBorderPanel);
                            oneLineBevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = oneLineBevelBorderPanel.getOneLineBevelBorder();
                        }
                        currentBorderChanged(oneLineBevelBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!selectedBorder.getClass().getName().equals("gr.cti.eslate.utils.OneLineBevelBorder")) {
                            selectedBorder = new OneLineBevelBorder(OneLineBevelBorder.RAISED);
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (oneLineBevelBorderPanel == null) {
                            oneLineBevelBorderPanel = new OneLineBevelBorderPanel((OneLineBevelBorder) selectedBorder, sampleBorderPanel);
                            oneLineBevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            oneLineBevelBorderPanel.setOneLineBevelBorder((OneLineBevelBorder) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(oneLineBevelBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(NOTOPONELINEBEVELBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        if (noTopOneLineBevelBorderPanel == null) {
                            if (currentBorder == null || !currentBorder.getClass().getName().equals("gr.cti.eslate.utils.NoTopOneLineBevelBorder"))
                                currentBorder = new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED);
                            noTopOneLineBevelBorderPanel = new NoTopOneLineBevelBorderPanel((NoTopOneLineBevelBorder) currentBorder, sampleBorderPanel);
                            noTopOneLineBevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = noTopOneLineBevelBorderPanel.getNoTopOneLineBevelBorder();
                        }
                        currentBorderChanged(noTopOneLineBevelBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!selectedBorder.getClass().getName().equals("gr.cti.eslate.utils.NoTopOneLineBevelBorder")) {
                            selectedBorder = new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED);
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (noTopOneLineBevelBorderPanel == null) {
                            noTopOneLineBevelBorderPanel = new NoTopOneLineBevelBorderPanel((NoTopOneLineBevelBorder) selectedBorder, sampleBorderPanel);
                            noTopOneLineBevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            noTopOneLineBevelBorderPanel.setNoTopOneLineBevelBorder((NoTopOneLineBevelBorder) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(noTopOneLineBevelBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(BEVELBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        if (bevelBorderPanel == null) {
                            if (currentBorder == null || !currentBorder.getClass().getName().equals("javax.swing.border.BevelBorder"))
                                currentBorder = new BevelBorder(BevelBorder.RAISED);
                            bevelBorderPanel = new BevelBorderPanel((BevelBorder) currentBorder, sampleBorderPanel);
                            bevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = bevelBorderPanel.getBevelBorder();
                        }
                        currentBorderChanged(bevelBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!selectedBorder.getClass().getName().equals("javax.swing.border.BevelBorder")) {
                            selectedBorder = new BevelBorder(BevelBorder.RAISED);
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (bevelBorderPanel == null) {
                            bevelBorderPanel = new BevelBorderPanel((BevelBorder) selectedBorder, sampleBorderPanel);
                            bevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            bevelBorderPanel.setBevelBorder((BevelBorder) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(bevelBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(SOFTBEVELBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        if (softBevelBorderPanel == null) {
                            if (currentBorder == null  || !currentBorder.getClass().getName().equals("javax.swing.border.SoftBevelBorder"))
                                currentBorder = new SoftBevelBorder(SoftBevelBorder.RAISED);
                            softBevelBorderPanel = new SoftBevelBorderPanel((SoftBevelBorder) currentBorder, sampleBorderPanel);
                            softBevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = softBevelBorderPanel.getSoftBevelBorder();
                        }
                        currentBorderChanged(softBevelBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!selectedBorder.getClass().getName().equals("javax.swing.border.SoftBevelBorder")) {
                            selectedBorder = new SoftBevelBorder(SoftBevelBorder.RAISED);
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (softBevelBorderPanel == null) {
                            softBevelBorderPanel = new SoftBevelBorderPanel((SoftBevelBorder) selectedBorder, sampleBorderPanel);
                            softBevelBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            softBevelBorderPanel.setSoftBevelBorder((SoftBevelBorder) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(softBevelBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(ETCHEDBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        if (etchedBorderPanel == null) {
                            if (currentBorder == null || !EtchedBorder.class.isAssignableFrom(currentBorder.getClass()))
                                currentBorder = new EtchedBorder();
                            etchedBorderPanel = new EtchedBorderPanel((EtchedBorder) currentBorder, sampleBorderPanel);
                            etchedBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = etchedBorderPanel.getEtchedBorder();
                        }
                        currentBorderChanged(etchedBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!EtchedBorder.class.isAssignableFrom(selectedBorder.getClass())) {
                            selectedBorder = new EtchedBorder();
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (etchedBorderPanel == null) {
                            etchedBorderPanel = new EtchedBorderPanel((EtchedBorder) selectedBorder, sampleBorderPanel);
                            etchedBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            etchedBorderPanel.setEtchedBorder((EtchedBorder) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(etchedBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(MATTEBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) { // 'borderTreePanel' not visible
                        if (matteBorderPanel == null) {
                            if (currentBorder == null || !MatteBorder2.class.isAssignableFrom(currentBorder.getClass()))
                                currentBorder = new MatteBorder2(1,1,1,1, Color.blue);
                            matteBorderPanel = new MatteBorderPanel((MatteBorder2) currentBorder, sampleBorderPanel);
                            matteBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = matteBorderPanel.getMatteBorder();
                        }
                        currentBorderChanged(matteBorderPanel);
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!MatteBorder.class.isAssignableFrom(selectedBorder.getClass())) {
                            selectedBorder = new MatteBorder2(1,1,1,1, Color.blue);;
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (matteBorderPanel == null) {
                            matteBorderPanel = new MatteBorderPanel((MatteBorder2) selectedBorder, sampleBorderPanel);
                            matteBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            matteBorderPanel.setMatteBorder((MatteBorder2) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(matteBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(EMPTYBORDER))) {
                    checkForOuterBorderChange();
                    if (splitpane.getBottomComponent() == null) {
                        if (emptyBorderPanel == null) {
                            if (currentBorder == null || !EmptyBorder.class.isAssignableFrom(currentBorder.getClass()))
                                currentBorder = new EmptyBorder(1,1,1,1);
                            emptyBorderPanel = new EmptyBorderPanel((EmptyBorder) currentBorder, sampleBorderPanel);
                            emptyBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            currentBorder = emptyBorderPanel.getEmptyBorder();
                        }
                        currentBorderChanged(emptyBorderPanel);
                        /* To make the EmptyBorder visible we draw a MatteBorder with gray color
                         * in its place. */
                        Insets insets = currentBorder.getBorderInsets(sampleBorderPanel);
                        sampleBorderPanel.setBorder(new MatteBorder(insets.top, insets.left,
                                                      insets.bottom, insets.right, Color.gray));
                    }else{ // 'borderTreePanel' visible
                        BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                        if (selectedNode == null) return;
                        Border selectedBorder = (Border) selectedNode.getUserObject();
                        if (!selectedBorder.getClass().getName().equals("javax.swing.border.EmptyBorder")) {
                            selectedBorder = new EmptyBorder(1,1,1,1);;
                            if (!selectedNode.isRoot() && selectedNode.getChildCount() != 0) {
                                borderTreePanel.tree.collapsePath(borderTreePanel.tree.getSelectionModel().getSelectionPath());
                                selectedNode.removeAllChildren();
                                borderTreePanel.repaint();
                                borderTreePanel.treeModel.reload();
                            }
                            selectedNode.setUserObject(selectedBorder);
                            currentBorder = borderTreePanel.reconstructBorder();
                        }
                        if (emptyBorderPanel == null) {
                            emptyBorderPanel = new EmptyBorderPanel((EmptyBorder) selectedBorder, sampleBorderPanel);
                            emptyBorderPanel.addPropertyChangeListener(borderChangeListener);
                        }else{
                            emptyBorderPanel.setEmptyBorder((EmptyBorder) selectedBorder, sampleBorderPanel);
                        }
                        currentBorderChanged(emptyBorderPanel);
                    }
                }else if (borderName.equals(borderDialogBundle.getString(TITLEDBORDER))) {
                    titleBorderSelected();
                }else if (borderName.equals(borderDialogBundle.getString(COMPOUNDBORDER))) {
                    compoundBorderSelected();
                }else{
                    if (splitpane.getBottomComponent() != null) {
                        splitpaneDividerLocation = splitpane.getDividerLocation();
                        splitpane.setBottomComponent(null);
                    }
                    sampleBorderPanel.setBorder(null);
                    borderPropertiesPanel.removeAll();
                    sampleBorderPanel.repaint();
                    borderPropertiesPanel.revalidate();
                    borderPropertiesPanel.repaint();
                    currentBorder = null;
                }
            }
        });

        setResizable(true);
        pack();
//        setResizable(false);

        // Initialization
        if (currentBorder == null) {
            borderList.setSelectedValue(borderDialogBundle.getString(NOBORDER), true);
        }else if (LineBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(LINEBORDER), true);
        }else if (MatteBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(MATTEBORDER), true);
        }else if (EmptyBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(EMPTYBORDER), true);
        }else if (SoftBevelBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(SOFTBEVELBORDER), true);
        }else if (OneLineBevelBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(ONELINEBEVELBORDER), true);
        }else if (NoTopOneLineBevelBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(NOTOPONELINEBEVELBORDER), true);
        }else if (BevelBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(BEVELBORDER), true);
        }else if (TitledBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(TITLEDBORDER), true);
        }else if (EtchedBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(ETCHEDBORDER), true);
        }else if (CompoundBorder.class.isAssignableFrom(currentBorder.getClass())) {
            borderList.setSelectedValue(borderDialogBundle.getString(COMPOUNDBORDER), true);
        }

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(okButton);

        borderList.requestFocus();
        ESlateContainerUtils.showDialog(this, null, false);
    }

    public Border getBorder() {
        return currentBorder;
    }

    public int getReturnCode() {
        return returnCode;
    }

    private void currentBorderChanged(JPanel borderPanel) {
        borderPropertiesPanel.removeAll();
        if (borderPanel != null) {
            borderPropertiesPanel.add(borderPropLabelPanel, BorderLayout.NORTH);
            borderPropertiesPanel.add(borderPanel, BorderLayout.CENTER);

        }
        sampleBorderPanel.setBorder(currentBorder);
//        System.out.println("Setting sampleBorderPanel to: " + currentBorder);
        sampleBorderPanel.repaint();
        borderPropertiesPanel.revalidate();
        borderPropertiesPanel.repaint();
    }

    /* if the bottomComponent of the 'splitpane' (the 'borderTreePanel' is
     * visible and the border chosen in the tree is the top-most, then
     * a new 'borderList' selection is interpreted as of the type of
     * the top-most border. Therefore the 'borderTreePanel' is closed
     * i.e. becomes invisible again.
     */
    private void checkForOuterBorderChange() {
        if (splitpane.getBottomComponent() != null) {
            if (borderTreePanel.isOuterBorderSelected() || !borderTreePanel.isSmthSelected()) {
                splitpaneDividerLocation = splitpane.getDividerLocation();
                splitpane.setBottomComponent(null);
            }
        }
    }

    private void compoundBorderSelected() {
        if (splitpane.getBottomComponent() == null) {
            if (borderTreePanel == null) {
                borderTreePanel = new BorderTreePanel(borderList);
            }
            /* If the 'borderTreePanel' is not the bottom panel of the 'splitpane',
             * add it. If it already is (i.e. this happens when the user selected a
             * node in the tree of the borderTreePanel, which is of "TitledBorder"
             * type, don't do anything.
             */
            if (currentBorder == null || !CompoundBorder.class.isAssignableFrom(currentBorder.getClass()))
                currentBorder = new CompoundBorder(new LineBorder(Color.red), new BevelBorder(BevelBorder.RAISED));
            borderTreePanel.setOuterBorder(currentBorder);
            splitpane.setBottomComponent(borderTreePanel);
            if (splitpaneDividerLocation < 10) {
                splitpaneDividerLocation = splitpane.getTopComponent().getSize().height - 40;
            }
            splitpane.setDividerLocation(splitpaneDividerLocation);
//            currentBorder = titledBorderPanel.getTitledBorder();
            currentBorderChanged(null);
        }else{
            BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
            if (selectedNode == null) selectedNode = (BorderTreeNode) borderTreePanel.treeModel.getRoot();
            Border selectedBorder = (Border) selectedNode.getUserObject();
/*            if (borderTreePanel.isOuterBorderSelected()) {
//                titledBorderPanel.setTitledBorder((TitledBorder) selectedBorder);
                currentBorderChanged(null);
                return;
            }
*/
            if (!CompoundBorder.class.isAssignableFrom(selectedBorder.getClass())) {
                selectedBorder = new CompoundBorder(new LineBorder(Color.red), new BevelBorder(BevelBorder.RAISED));
                selectedNode.setUserObject(selectedBorder);
                selectedNode.removeAllChildren();
                borderTreePanel.analyzeBorder(selectedBorder, selectedNode);
                currentBorder = borderTreePanel.reconstructBorder();
                borderTreePanel.treeModel.reload(selectedNode);
            }
//            titledBorderPanel.setTitledBorder((TitledBorder) selectedBorder);
            currentBorderChanged(null);
        }
    }

    private void titleBorderSelected() {
        if (titledBorderPanel == null) {
            if (currentBorder == null || !TitledBorder.class.isAssignableFrom(currentBorder.getClass()))
                currentBorder = new TitledBorder("");
            titledBorderPanel = new TitledBorderPanel((TitledBorder) currentBorder);
            titledBorderPanel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("Border")) {
                        /* If the TitledBorder that changed is the outermost, then change the
                         * value of the 'currentBorder', which holds the whole composite  border.
                         * If however the TitledBorder which changed is a inner border of the
                         * composite outer border, then change the proper border in the hierarchy
                         * and reconstruct the outer-most border ('currentBorder').
                         */
                        if (borderTreePanel.isOuterBorderSelected()  || !borderTreePanel.isSmthSelected()) {
                            currentBorder = (TitledBorder) evt.getNewValue();
                            sampleBorderPanel.setBorder(currentBorder);
                            sampleBorderPanel.repaint();
                        }else{
                            BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
                            if (selectedNode == null) return;
                            selectedNode.setUserObject((TitledBorder) evt.getNewValue());
                            currentBorder = borderTreePanel.reconstructBorder();
                            sampleBorderPanel.setBorder(currentBorder);
                            sampleBorderPanel.repaint();
                        }
                    }
                }
            });
        }

        if (splitpane.getBottomComponent() == null) {
            if (borderTreePanel == null) {
                borderTreePanel = new BorderTreePanel(borderList);
            }
            /* If the 'borderTreePanel' is not the bottom panel of the 'splitpane',
             * add it. If it already is (i.e. this happens when the user selected a
             * node in the tree of the borderTreePanel, which is of "TitledBorder"
             * type, don't do anything.
             */
            currentBorder = titledBorderPanel.getTitledBorder();
            borderTreePanel.setOuterBorder(currentBorder);
            splitpane.setBottomComponent(borderTreePanel);
            if (splitpaneDividerLocation < 10) {
                splitpaneDividerLocation = splitpane.getTopComponent().getSize().height - 40;
            }
            splitpane.setDividerLocation(splitpaneDividerLocation);
            currentBorder = titledBorderPanel.getTitledBorder();
            currentBorderChanged(titledBorderPanel);
        }else{
            BorderTreeNode selectedNode = borderTreePanel.getSelectedNode();
            if (selectedNode == null) selectedNode = (BorderTreeNode) borderTreePanel.treeModel.getRoot();
            Border selectedBorder = (Border) selectedNode.getUserObject();
//            System.out.println("borderTreePanel.isOuterBorderSelected(): " + borderTreePanel.isOuterBorderSelected());
/*            if (borderTreePanel.isOuterBorderSelected()) {
                titledBorderPanel.setTitledBorder((TitledBorder) selectedBorder);
                currentBorder = titledBorderPanel.getTitledBorder();
                currentBorderChanged(titledBorderPanel);
                return;
            }
*/
            if (!TitledBorder.class.isAssignableFrom(selectedBorder.getClass())) {
                selectedBorder = new TitledBorder("");
                selectedNode.setUserObject(selectedBorder);
                selectedNode.removeAllChildren();
                borderTreePanel.analyzeBorder(selectedBorder, selectedNode);
                currentBorder = borderTreePanel.reconstructBorder();
                borderTreePanel.treeModel.reload();
            }
            titledBorderPanel.setTitledBorder((TitledBorder) selectedBorder);
            currentBorderChanged(titledBorderPanel);
        }
    }
}

class SampleBorderPanel extends JPanel {
    public SampleBorderPanel(boolean b) {
        super(b);
    }
    public void paintBorder(java.awt.Graphics g) {
        try{
            super.paintBorder(g);
        }catch (Exception exc) {
            System.out.println("Exception caught while applying the border to the preview area");
            exc.printStackTrace();
        }
    }
}

class BorderTreePanel extends JPanel {
    JTree tree;
    JScrollPane scrollpane;
    DefaultTreeModel treeModel;
    JList borderList;

    public BorderTreePanel(JList bList) { //Border topBorder) {
        this.borderList = bList;
  	    setLayout(new BorderLayout());

        treeModel = new DefaultTreeModel(new BorderTreeNode());
        tree = new JTree(treeModel);
        tree.setCellRenderer(new BorderTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
        scrollpane = new JScrollPane(tree);
        scrollpane.setBackground(Color.white);
        scrollpane.setBorder(new CompoundBorder(scrollpane.getBorder(), new EmptyBorder(2,0,0,0))); //new SoftBevelBorder(SoftBevelBorder.LOWERED));
        add(scrollpane, BorderLayout.CENTER);

        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent ev) {
                if (ev.getNewLeadSelectionPath() == null) return;
                BorderTreeNode btn = (BorderTreeNode) ev.getNewLeadSelectionPath().getLastPathComponent();
                if (btn == null) return;
                Border b = (Border) btn.getUserObject();
                if (b == null)
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.NOBORDER), true);
                else if (SoftBevelBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.SOFTBEVELBORDER), true);
                else if (OneLineBevelBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.ONELINEBEVELBORDER), true);
                else if (NoTopOneLineBevelBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.NOTOPONELINEBEVELBORDER), true);
                else if (BevelBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.BEVELBORDER), true);
                else if (MatteBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.MATTEBORDER), true);
                else if (EmptyBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.EMPTYBORDER), true);
                else if (LineBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.LINEBORDER), true);
                else if (EtchedBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.ETCHEDBORDER), true);
                else if (CompoundBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.COMPOUNDBORDER), true);
                else if (TitledBorder.class.isAssignableFrom(b.getClass()))
                    borderList.setSelectedValue(BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.TITLEDBORDER), true);
            }
        });

    }

    /* Create the whole border tree for the supplied composite border */
    public void setOuterBorder(Border border) {
        if (border == null || (!TitledBorder.class.isAssignableFrom(border.getClass()) &&
            !CompoundBorder.class.isAssignableFrom(border.getClass()))) {
            ((BorderTreeNode) treeModel.getRoot()).removeAllChildren();
            return;
        }

        BorderTreeNode rootNode = new BorderTreeNode(border);
        treeModel.setRoot(rootNode);
        analyzeBorder(border, rootNode);
    }

    /* Recursively analyze the border of the supplied node */
    protected void analyzeBorder(Border border, BorderTreeNode node) {
        if (TitledBorder.class.isAssignableFrom(border.getClass())) {
            Border b = ((TitledBorder) border).getBorder();
//            System.out.println("analyzeBorder b: " + b);
            BorderTreeNode bNode = new BorderTreeNode(b);
            node.add(bNode);
            analyzeBorder(b, bNode);
        }else if (CompoundBorder.class.isAssignableFrom(border.getClass())) {
            Border outBorder = ((CompoundBorder) border).getOutsideBorder();
            BorderTreeNode outBNode = new BorderTreeNode(outBorder);
            Border inBorder = ((CompoundBorder) border).getInsideBorder();
            BorderTreeNode inBNode = new BorderTreeNode(inBorder);
            node.add(outBNode);
            node.add(inBNode);
            analyzeBorder(outBorder, outBNode);
            analyzeBorder(inBorder, inBNode);
        }
    }

    public boolean isOuterBorderSelected() {
//        System.out.println("row: " + tree.getSelectionModel().isRowSelected(0));
        return tree.getSelectionModel().isRowSelected(0);
    }
    public boolean isSmthSelected() {
//        System.out.println("isSmthSelected: " + tree.getSelectionModel().getSelectionPath());
        return (tree.getSelectionModel().getSelectionPath() != null);
    }

    public Border getSelectedBorder() {
        javax.swing.tree.TreePath path = tree.getSelectionModel().getSelectionPath();
        if (path == null) return null;
        return (Border) ((BorderTreeNode) path.getLastPathComponent()).getUserObject();
    }

    public BorderTreeNode getSelectedNode() {
        javax.swing.tree.TreePath path = tree.getSelectionModel().getSelectionPath();
        if (path == null) return null;
        return (BorderTreeNode) path.getLastPathComponent();
    }

    /* This method reconstructs the top-most composite border from the tree of the
     * sub-borders it contains.
     */
    public Border reconstructBorder() {
        BorderTreeNode topNode = (BorderTreeNode) treeModel.getRoot();
        Border newBorder = constructBorder(topNode);
        topNode.setUserObject(newBorder);
        return newBorder;
    }
/*    public void reportTree() {
        BorderTreeNode topNode = (BorderTreeNode) ((DefaultTreeModel) tree.getModel()).getRoot();
        if (topNode == null) return;
        Border border = (Border) topNode.getUserObject();
        if (border == null) return;
        if (border.getClass().getName().equals("javax.swing.border.TitledBorder")) {
            System.out.println(border);
            Border b = ((TitledBorder) border).getBorder();
            if (b == null) return;
            if (b.getClass().getName().equals("javax.swing.border.TitledBorder")) {
                System.out.println(b);
                System.out.println("23645185437812465812374  " + ((TitledBorder) b).getBorder());
            }
        }
        System.out.println("TopNode: " + topNode.getUserObject());
        System.out.println("topNode.getChildCount(): " + topNode.getChildCount());
        if (topNode.getChildCount() == 0) return;
        BorderTreeNode node = (BorderTreeNode) topNode.getChildAt(0);
        if (node != null) {
            System.out.println("2. node: " + node.getUserObject());
            System.out.println("2. node.getChildCount(): " + node.getChildCount());
            if (node.getChildCount() == 0) return;
            node = (BorderTreeNode) node.getChildAt(0);
            if (node != null) {
                System.out.println("3. node: " + node.getUserObject());
                System.out.println("3. node.getChildCount(): " + node.getChildCount());
            }
        }
    }
*/
    private Border constructBorder(BorderTreeNode node) {
        Border border = (Border) node.getUserObject();
        if (TitledBorder.class.isAssignableFrom(border.getClass())) {
            Border b = new TitledBorder(constructBorder((BorderTreeNode) node.getChildAt(0)),
                                        ((TitledBorder) border).getTitle(),
                                        ((TitledBorder) border).getTitleJustification(),
                                        ((TitledBorder) border).getTitlePosition(),
                                        ((TitledBorder) border).getTitleFont(),
                                        ((TitledBorder) border).getTitleColor());

/*            Border b = constructBorder( (BorderTreeNode) node.getChildAt(0));
            ((TitledBorder) border).setBorder(b);
*/            border = b;

        }else if (CompoundBorder.class.isAssignableFrom(border.getClass())) {
            Border b = new CompoundBorder(constructBorder((BorderTreeNode) node.getChildAt(0)),
                                          constructBorder((BorderTreeNode) node.getChildAt(1)));
            border = b;
//            Border outerBorder = constructBorder( (BorderTreeNode) node.getChildAt(0));
//            Border innerBorder = constructBorder( (BorderTreeNode) node.getChildAt(1));
//            node.setUserObject(new CompoundBorder(outerBorder, innerBorder));
        }
        node.setUserObject(border);
        return border;
    }
}


class BorderTreeNode extends DefaultMutableTreeNode {

    public BorderTreeNode() {
        super();
    }

    public BorderTreeNode(Border obj) {
        super(obj);
    }
}

class BorderTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {// implements javax.swing.tree.DefaultTreeCellRenderer {
    static Color selectedForeground = Color.white;
    static Color selectedBackground = new Color(0, 0, 128);
    static Color foreground = Color.black;
    static Color background = Color.white;
    static Color calcForeground = new Color(244,9,123);

    public BorderTreeCellRenderer() {
        super();
        setOpaque(true);
    }

    public Component getTreeCellRendererComponent (JTree tree,
                                                Object value,            // value to display
                                                boolean isSelected,      // is the cell selected
                                                boolean expanded,    // the table and the cell have the focus
                                                boolean leaf,
                                                int row,
                                                boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        BorderTreeNode node = (BorderTreeNode) value;

        Object o = node.getUserObject();
        if (o == null) {
            setText("");
            return this;
        }
        setText(o.getClass().getName().substring(o.getClass().getName().lastIndexOf('.')+1));
        if (isSelected) {
            setForeground(selectedForeground);
            setBackground(selectedBackground);
        }else{
            setForeground(foreground);
            setBackground(background);
        }
        return this;
    }
}

class BorderChangeListener implements PropertyChangeListener {
//    JPanel borderPropertiesPanel = null;
    BorderEditorDialog borderEditorDialog = null;

    public BorderChangeListener(BorderEditorDialog dialog) {
        if (dialog == null) throw new NullPointerException();
        borderEditorDialog = dialog;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("Border")) {
            /* If the 'borderTreePanel' is not visible, then the border that changes is
             * the total edited border. However if the 'borderTreePanel' is visible, then
             * the border that changes is a sub-border of a more composite border, i.e.
             * a TitledBorder or a CompoundBorder.
             */
            if (borderEditorDialog.splitpane.getBottomComponent() == null) {
                borderEditorDialog.currentBorder = (Border) evt.getNewValue();
                /* Empty border is invisible, so we draw it as a dark gray MatteBorder */
                if (borderEditorDialog.currentBorder.getClass().getName().equals("javax.swing.border.EmptyBorder")) {
                    Insets insets = borderEditorDialog.currentBorder.getBorderInsets(borderEditorDialog.sampleBorderPanel);
                    MatteBorder b = new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, Color.gray);
                    borderEditorDialog.sampleBorderPanel.setBorder(b);
                }else{
                    borderEditorDialog.sampleBorderPanel.setBorder(borderEditorDialog.currentBorder);
                }
                borderEditorDialog.sampleBorderPanel.repaint();
            }else{
                BorderTreeNode selectedNode = borderEditorDialog.borderTreePanel.getSelectedNode();
                if (selectedNode == null) return;
                selectedNode.setUserObject((Border) evt.getNewValue());
                borderEditorDialog.currentBorder = borderEditorDialog.borderTreePanel.reconstructBorder();
//                System.out.println("Border reconstructed " + borderEditorDialog.currentBorder);
                /* Empty border is invisible, so we draw it as a dark gray MatteBorder */
                if (borderEditorDialog.currentBorder.getClass().getName().equals("javax.swing.border.EmptyBorder")) {
                    Insets insets = borderEditorDialog.currentBorder.getBorderInsets(borderEditorDialog.sampleBorderPanel);
                    MatteBorder b = new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, Color.gray);
                    borderEditorDialog.sampleBorderPanel.setBorder(b);
                }else{
                    borderEditorDialog.sampleBorderPanel.setBorder(borderEditorDialog.currentBorder);
                }
//                System.out.println("Repainting sampleBorderPanel");
                borderEditorDialog.sampleBorderPanel.repaint();
            }
        }
    }
}

class MyList extends JList {
    public MyList(Object[] data) {
        super(data);
    }

    public void setSelectedValue(Object value, boolean b) {
        boolean eventToBeSend = true;
        if (getSelectedValue() != null && getSelectedValue().equals(value))
            eventToBeSend = false;
        super.setSelectedValue(value, b);
        if (!eventToBeSend) {
            fireSelectionValueChanged(-1, getSelectedIndex(), false);
        }
    }
}


