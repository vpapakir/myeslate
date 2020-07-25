package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class GridDialog extends JDialog {

    Locale locale;
    ResourceBundle gridDialogBundle;
//    protected Font greekUIFont = new Font("Helvetica", Font.PLAIN, 12);
    private boolean localeIsGreek = false;

    ESlateComposer composer;
    Color gridColor;
    JTextField gridStepField;
    JLabel sampleLb;
    JCheckBox showGrid, snapToGrid;
    boolean snapToGridOn = true;

    GridDialog(Frame parentFrame, boolean gridVisible, int gridStep, Color grColor, boolean snToGridOn, ESlateComposer cont, Component comp) {
        super(parentFrame, true);
        this.composer = cont;
        gridColor = grColor;
        this.snapToGridOn = snToGridOn;
        if (composer.microworld != null)
            composer.microworld.checkActionPriviledge(composer.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        /* This is an action controlled by a microworld setting. When the setting forbits
         * the action, there is no way the action can be taked by anyone no matter if the microworld
         * is locked or not.
         */
        if (composer.microworld != null)
            composer.microworld.checkActionPriviledge(composer.microworld.gridMgmtAllowed, "gridMgmtAllowed");

        locale = Locale.getDefault();
        gridDialogBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.GridDialogBundle", locale);
        if (gridDialogBundle.getClass().getName().equals("gr.cti.eslate.base.container.GridDialogBundle_el_GR"))
            localeIsGreek = true;

        setTitle(gridDialogBundle.getString("DialogTitle"));

        showGrid = new JCheckBox(gridDialogBundle.getString("ShowGrid"));
        snapToGrid = new JCheckBox(gridDialogBundle.getString("SnapToGrid"));
/*        if (localeIsGreek) {
            showGrid.setFont(greekUIFont);
            snapToGrid.setFont(greekUIFont);
        }
*/
        FontMetrics fm1 = getToolkit().getFontMetrics(showGrid.getFont());
        int showGridWidth = fm1.stringWidth(showGrid.getText()) + 20;
        int snapToGridWidth = fm1.stringWidth(snapToGrid.getText()) + 20;

        JLabel gridStepLb = new JLabel(gridDialogBundle.getString("GridStep"));
        gridStepField = new JTextField();
/*        if (localeIsGreek) {
            gridStepLb.setFont(greekUIFont);
            gridStepField.setFont(greekUIFont);
        }
*/
        fm1 = getToolkit().getFontMetrics(gridStepLb.getFont());
        int width = fm1.stringWidth(gridStepLb.getText());
        Dimension labelDim = new Dimension(width, 20);
        gridStepLb.setMaximumSize(labelDim);
        gridStepLb.setPreferredSize(labelDim);
        gridStepLb.setMinimumSize(labelDim);

        Dimension d = new Dimension(30, 16);
        gridStepField.setMaximumSize(d);
        gridStepField.setPreferredSize(d);
        gridStepField.setMinimumSize(d);
        gridStepField.setHorizontalAlignment(JTextField.RIGHT);

        JPanel gridStepPanel = new JPanel(true);
        gridStepPanel.setLayout(new BoxLayout(gridStepPanel, BoxLayout.X_AXIS));
        gridStepPanel.add(gridStepLb);
        gridStepPanel.add(Box.createHorizontalStrut(5));
        gridStepPanel.add(gridStepField);
//        gridStepPanel.add(Box.createGlue());

        int gridStepPanelWidth = width + 5 + 20;

        JLabel gridColorLb = new JLabel(gridDialogBundle.getString("GridColor"));
//        if (localeIsGreek) {
//            gridColorLb.setFont(greekUIFont);
//        }
        width = fm1.stringWidth(gridColorLb.getText());
        labelDim = new Dimension(width, 20);
        gridColorLb.setMaximumSize(labelDim);
        gridColorLb.setPreferredSize(labelDim);
        gridColorLb.setMinimumSize(labelDim);

        sampleLb = new JLabel("");
        labelDim = new Dimension(40, 20);
        sampleLb.setOpaque(true);
        sampleLb.setMaximumSize(labelDim);
        sampleLb.setPreferredSize(labelDim);
        sampleLb.setMinimumSize(labelDim);
        sampleLb.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED));

        JButton chooseColor = new JButton(new ImageIcon(getClass().getResource("images/palette.gif")));
        Dimension buttonSize = new Dimension(19, 17);
        chooseColor.setMaximumSize(buttonSize);
        chooseColor.setPreferredSize(buttonSize);
        chooseColor.setMinimumSize(buttonSize);
        chooseColor.setMargin(new Insets(0, 0, 0, 1));
        chooseColor.setFocusPainted(false);

        JPanel gridColorPanel = new JPanel(true);
        gridColorPanel.setLayout(new BoxLayout(gridColorPanel, BoxLayout.X_AXIS));
        gridColorPanel.add(gridColorLb);
        gridColorPanel.add(Box.createHorizontalStrut(5));
        gridColorPanel.add(sampleLb);
        gridColorPanel.add(Box.createHorizontalStrut(5));
        gridColorPanel.add(chooseColor);
//        gridColorPanel.add(Box.createGlue());

        int gridColorPanelWidth = width + 5 + 40 + 5 + 19;

        // Find the maximum width and apply it to all the rows
        int maxWidth = showGridWidth;
        if (snapToGridWidth > maxWidth)
            maxWidth = snapToGridWidth;
        if (gridStepPanelWidth > maxWidth)
            maxWidth = gridStepPanelWidth;
        if (gridColorPanelWidth > maxWidth)
            maxWidth = gridColorPanelWidth;

        Dimension uniformSize = new Dimension(maxWidth, 20);
        showGrid.setMaximumSize(uniformSize);
        showGrid.setMinimumSize(uniformSize);
        showGrid.setPreferredSize(uniformSize);
        snapToGrid.setMaximumSize(uniformSize);
        snapToGrid.setMinimumSize(uniformSize);
        snapToGrid.setPreferredSize(uniformSize);
        gridStepPanel.setMaximumSize(uniformSize);
        gridStepPanel.setMinimumSize(uniformSize);
        gridStepPanel.setPreferredSize(uniformSize);
        gridColorPanel.setMaximumSize(uniformSize);
        gridColorPanel.setMinimumSize(uniformSize);
        gridColorPanel.setPreferredSize(uniformSize);

        showGrid.setAlignmentX(CENTER_ALIGNMENT);
        snapToGrid.setAlignmentX(CENTER_ALIGNMENT);
        gridStepPanel.setAlignmentX(CENTER_ALIGNMENT);
        gridColorPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel gridPanel = new JPanel(true);
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));

        gridPanel.add(showGrid);
        gridPanel.add(Box.createVerticalStrut(5));
        gridPanel.add(snapToGrid);
        gridPanel.add(Box.createVerticalStrut(5));
        gridPanel.add(gridStepPanel);
        gridPanel.add(Box.createVerticalStrut(5));
        gridPanel.add(gridColorPanel);

        Color titleBorderColor = new Color(119, 40, 104);
        TitledBorder tb1 = new TitledBorder(gridDialogBundle.getString("GridSettings"));
        tb1.setTitleColor(titleBorderColor);
//        if (localeIsGreek)
//            tb1.setTitleFont(greekUIFont);
        gridPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,0,5,0)));

        // The button panel (APPLY, OK, CANCEL)
        JButton okButton = new JButton(gridDialogBundle.getString("OK"));
//        if (localeIsGreek)
//            okButton.setFont(greekUIFont);
        Color color128 = new Color(0, 0, 128);
        okButton.setForeground(color128);
        buttonSize = new Dimension(90, 25);
        okButton.setMaximumSize(buttonSize);
        okButton.setPreferredSize(buttonSize);
        okButton.setMinimumSize(buttonSize);
        Insets zeroInsets = new Insets(0,0,0,0);
        okButton.setMargin(zeroInsets);

        final JButton cancelButton = new JButton(gridDialogBundle.getString("Cancel"));
//        if (localeIsGreek)
//            cancelButton.setFont(greekUIFont);
        cancelButton.setForeground(color128);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setMargin(zeroInsets);

        JButton applyButton = new JButton(gridDialogBundle.getString("Apply"));
//        if (localeIsGreek)
//            applyButton.setFont(greekUIFont);
        applyButton.setForeground(color128);
        applyButton.setMaximumSize(buttonSize);
        applyButton.setPreferredSize(buttonSize);
        applyButton.setMinimumSize(buttonSize);
        applyButton.setMargin(zeroInsets);

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

        // The main panel
        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(gridPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        chooseColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                Color color = JColorChooser.showDialog(new JFrame(), gridDialogBundle.getString("GridColor"), gridColor);
                Color color = ESlateContainerUtils.showColorDialog(GridDialog.this, gridDialogBundle.getString("GridColor"), gridColor);
                if (color != null) {
                    gridColor = color;
                    sampleLb.setBackground(gridColor);
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Color button was selected");
                if (showGrid.isSelected()) {
                    try{
                        composer.setGridVisible(true, new Integer(gridStepField.getText()).intValue(), sampleLb.getBackground(), (snapToGrid.isSelected() && snapToGrid.isEnabled()));
                    }catch (NumberFormatException exc) {
                        ESlateOptionPane.showMessageDialog(GridDialog.this, gridDialogBundle.getString("BadNumber"), gridDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }else{
                    composer.setGridVisible(false, -1, null, false);
                }
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showGrid.isSelected()) {
                    try{
                        composer.setGridVisible(true, new Integer(gridStepField.getText()).intValue(), sampleLb.getBackground(), (snapToGrid.isSelected() && snapToGrid.isEnabled()));
                    }catch (NumberFormatException exc) {
                        ESlateOptionPane.showMessageDialog(GridDialog.this, gridDialogBundle.getString("BadNumber"), gridDialogBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    composer.setGridVisible(false, -1, null, false);
                }
            }
        });

        snapToGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                snapToGridOn = snapToGrid.isSelected();
            }
        });

        showGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean visible = showGrid.isSelected();
                if (!visible) {
                    snapToGrid.setSelected(false);
                    snapToGrid.setEnabled(false);
                }else{
                    snapToGrid.setEnabled(true);
                    snapToGrid.setSelected(snapToGridOn);
                }
            }
        });

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

        //Initialize the GridDialog
        showGrid.setSelected(gridVisible);
        snapToGrid.setSelected(snapToGridOn);
        gridStepField.setText(new Integer(gridStep).toString());
        sampleLb.setBackground(gridColor);
        snapToGrid.setEnabled(gridVisible);

        pack();
        setResizable(false);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x, y;
        if (comp == null || !comp.isVisible()) {
            x = (screenSize.width/2) - (getSize().width/2);
            y = (screenSize.height/2) - (getSize().height/2);
        }else{
            Rectangle compBounds = comp.getBounds();
            java.awt.Point compLocation = comp.getLocationOnScreen();
    //        System.out.println("dbBounds: " + dbBounds + " location: " + database.getLocationOnScreen());
            x = compLocation.x + compBounds.width/2 - getSize().width/2;
            y = compLocation.y + compBounds.height/2-getSize().height/2;
            if (x+getSize().width > screenSize.width)
                x = screenSize.width - getSize().width;
            if (y+getSize().height > screenSize.height)
                y = screenSize.height - getSize().height;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
        setLocation(x, y);
        getRootPane().setDefaultButton(okButton);
        showGrid.requestFocus();
        show();
    }
}

