package gr.cti.eslate.base.container;

import gr.cti.eslate.base.container.internalFrame.ESlateInternalFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class ComponentPropertiesDialog extends JDialog {
    ResourceBundle bundle;
    ESlateContainer container;
    ESlateInternalFrame frame;
    JCheckBox compoBarVisibleBox, resizeBox, componentNameBox;
    JCheckBox minimizeBox, maximizeBox, closeBox, pinBox, infoBox, helpBox, activateBox;
    JComboBox componentChooser;
    JLabel compoChooserLabel;

    public ComponentPropertiesDialog(java.awt.Frame parentFrame, ESlateContainer cont, ESlateInternalFrame frame) {
        super(parentFrame, true);
        this.container = cont;

        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ComponentPropertiesDialogBundle", Locale.getDefault());

        setTitle(bundle.getString("DialogTitle"));

        ESlateComponent[] visualComponents = cont.mwdComponents.getVisualComponents();
        String[] compoNames = new String[visualComponents.length];
        for (int i=0; i<compoNames.length; i++)
            compoNames[i] = visualComponents[i].handle.getComponentName();
		java.util.Arrays.sort(compoNames);

        compoChooserLabel = new JLabel(bundle.getString("Component"));
        componentChooser = new JComboBox(compoNames);

        compoBarVisibleBox = new JCheckBox(bundle.getString("FrameTitleDisplayed"));
        resizeBox = new JCheckBox(bundle.getString("ComponentResizable"));
        minimizeBox = new JCheckBox(bundle.getString("MinimizeVisible"));
        maximizeBox = new JCheckBox(bundle.getString("MaximizeVisible"));
        closeBox = new JCheckBox(bundle.getString("CloseVisible"));
        pinBox = new JCheckBox(bundle.getString("PlugVisible"));
        helpBox = new JCheckBox(bundle.getString("HelpVisible"));
        infoBox = new JCheckBox(bundle.getString("InfoVisible"));
        activateBox = new JCheckBox(bundle.getString("ComponentActivatableByMousePress"));
        componentNameBox = new JCheckBox(bundle.getString("ComponentNameChangeableFromMenuBar"));

        FontMetrics fm1 = compoBarVisibleBox.getFontMetrics(compoBarVisibleBox.getFont());
        int boxWidth = fm1.stringWidth(compoBarVisibleBox.getText());
        if (boxWidth < fm1.stringWidth(resizeBox.getText()))
            boxWidth = fm1.stringWidth(resizeBox.getText());
        if (boxWidth < fm1.stringWidth(minimizeBox.getText()))
            boxWidth = fm1.stringWidth(minimizeBox.getText());
        if (boxWidth < fm1.stringWidth(maximizeBox.getText()))
            boxWidth = fm1.stringWidth(maximizeBox.getText());
        if (boxWidth < fm1.stringWidth(closeBox.getText()))
            boxWidth = fm1.stringWidth(closeBox.getText());
        if (boxWidth < fm1.stringWidth(pinBox.getText()))
            boxWidth = fm1.stringWidth(pinBox.getText());
        if (boxWidth < fm1.stringWidth(helpBox.getText()))
            boxWidth = fm1.stringWidth(helpBox.getText());
        if (boxWidth < fm1.stringWidth(infoBox.getText()))
            boxWidth = fm1.stringWidth(infoBox.getText());
        if (boxWidth < fm1.stringWidth(activateBox.getText()))
            boxWidth = fm1.stringWidth(activateBox.getText());
        if (boxWidth < fm1.stringWidth(componentNameBox.getText()))
            boxWidth = fm1.stringWidth(componentNameBox.getText());
        boxWidth = boxWidth + 35; //Because of the box of the JCheckBox and the TitleBorder
        Dimension boxSize = new Dimension(boxWidth, 20);

        // All the check boxes must have equal size in order to appear vertically alligned
        compoBarVisibleBox.setMaximumSize(boxSize);
        compoBarVisibleBox.setMinimumSize(boxSize);
        compoBarVisibleBox.setPreferredSize(boxSize);
        resizeBox.setMaximumSize(boxSize);
        resizeBox.setMinimumSize(boxSize);
        resizeBox.setPreferredSize(boxSize);
        minimizeBox.setMaximumSize(boxSize);
        minimizeBox.setMinimumSize(boxSize);
        minimizeBox.setPreferredSize(boxSize);
        maximizeBox.setMaximumSize(boxSize);
        maximizeBox.setMinimumSize(boxSize);
        maximizeBox.setPreferredSize(boxSize);
        closeBox.setMaximumSize(boxSize);
        closeBox.setMinimumSize(boxSize);
        closeBox.setPreferredSize(boxSize);
        pinBox.setMaximumSize(boxSize);
        pinBox.setMinimumSize(boxSize);
        pinBox.setPreferredSize(boxSize);
        helpBox.setMaximumSize(boxSize);
        helpBox.setMinimumSize(boxSize);
        helpBox.setPreferredSize(boxSize);
        infoBox.setMaximumSize(boxSize);
        infoBox.setMinimumSize(boxSize);
        infoBox.setPreferredSize(boxSize);
        activateBox.setMaximumSize(boxSize);
        activateBox.setMinimumSize(boxSize);
        activateBox.setPreferredSize(boxSize);
        componentNameBox.setMaximumSize(boxSize);
        componentNameBox.setMinimumSize(boxSize);
        componentNameBox.setPreferredSize(boxSize);

        compoBarVisibleBox.setAlignmentX(CENTER_ALIGNMENT);
        resizeBox.setAlignmentX(CENTER_ALIGNMENT);
        minimizeBox.setAlignmentX(CENTER_ALIGNMENT);
        maximizeBox.setAlignmentX(CENTER_ALIGNMENT);
        closeBox.setAlignmentX(CENTER_ALIGNMENT);
        pinBox.setAlignmentX(CENTER_ALIGNMENT);
        helpBox.setAlignmentX(CENTER_ALIGNMENT);
        infoBox.setAlignmentX(CENTER_ALIGNMENT);
        activateBox.setAlignmentX(CENTER_ALIGNMENT);
        componentNameBox.setAlignmentX(CENTER_ALIGNMENT);

        if (!container.currentView.componentActivationMethodChangeAllowed)
            activateBox.setEnabled(false);

        JPanel componentChoosePanel = new JPanel();
        componentChoosePanel.setLayout(new BoxLayout(componentChoosePanel, BoxLayout.X_AXIS));
        componentChoosePanel.add(Box.createGlue());
        componentChoosePanel.add(compoChooserLabel);
        componentChoosePanel.add(Box.createHorizontalStrut(5));
        componentChoosePanel.add(componentChooser);
        componentChoosePanel.add(Box.createGlue());

        TitledBorder tb = new TitledBorder(bundle.getString("ChooseComponent"));
        Color titleBorderColor = new Color(119, 40, 104);
        tb.setTitleColor(titleBorderColor);
        componentChoosePanel.setBorder(new CompoundBorder(tb, new EmptyBorder(0,0,5,0)));

        JPanel componentPropPanel = new JPanel(true);
        componentPropPanel.setLayout(new BoxLayout(componentPropPanel, BoxLayout.Y_AXIS));

        componentPropPanel.add(compoBarVisibleBox);
        componentPropPanel.add(resizeBox);
        componentPropPanel.add(activateBox);

        TitledBorder tb1 = new TitledBorder(bundle.getString("ComponentProperties"));
        tb1.setTitleColor(titleBorderColor);
        componentPropPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,0,5,0)));

        JPanel componentBarPropPanel = new JPanel(true);
        componentBarPropPanel.setLayout(new BoxLayout(componentBarPropPanel, BoxLayout.Y_AXIS));
        componentBarPropPanel.setAlignmentX(CENTER_ALIGNMENT);

        componentBarPropPanel.add(minimizeBox);
        componentBarPropPanel.add(maximizeBox);
        componentBarPropPanel.add(closeBox);
        componentBarPropPanel.add(pinBox);
        componentBarPropPanel.add(helpBox);
        componentBarPropPanel.add(infoBox);
        componentBarPropPanel.add(componentNameBox);

        TitledBorder tb2 = new TitledBorder(bundle.getString("ComponentBarProperties"));
        tb2.setTitleColor(titleBorderColor);
        componentBarPropPanel.setBorder(new CompoundBorder(tb2, new EmptyBorder(0,0,5,0)));

        Font buttonFont = null;
        final OK_Cancel_ApplyPanel buttonPanel = container.containerUtils.createOKCancelApplyPanel(buttonFont);

        JPanel mainPanel = new JPanel(true);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(componentChoosePanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(componentPropPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(componentBarPropPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(buttonPanel);

        getContentPane().add(mainPanel);

        buttonPanel.getOKButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adjustComponentProperties();
                dispose();
            }
        });
        buttonPanel.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.getApplyButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adjustComponentProperties();
            }
        });

        componentChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String compoName = (String) componentChooser.getSelectedItem();
                ESlateInternalFrame fr = container.mwdComponents.getComponentFrame(compoName);
                setFrame(fr);
            }
        });

        //Initialize the dialog
        componentChooser.setSelectedItem(frame.getTitle());

        // ESCAPE HANDLER
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = buttonPanel.cancelButton.getModel();
                bm.setArmed(true);
                bm.setPressed(true);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.ButtonModel bm = buttonPanel.cancelButton.getModel();
                bm.setPressed(false);
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, true), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Show the dialog
        setResizable(false);
        pack();
        getRootPane().setDefaultButton(buttonPanel.okButton);
        compoBarVisibleBox.requestFocus();
        ESlateContainerUtils.showDialog(this, container, false);
    }

    public void adjustComponentProperties() {
        frame.setTitlePanelVisible(compoBarVisibleBox.isSelected());
        frame.setMinimizeButtonVisible(minimizeBox.isSelected());
        frame.setMaximizeButtonVisible(maximizeBox.isSelected());
        frame.setCloseButtonVisible(closeBox.isSelected());
        frame.setPlugButtonVisible(pinBox.isSelected());
        frame.setHelpButtonVisible(helpBox.isSelected());
        frame.setInfoButtonVisible(infoBox.isSelected());
        frame.setComponentNameChangeableFromMenuBar(componentNameBox.isSelected());
        frame.setResizable(resizeBox.isSelected());
        frame.setComponentActivatedOnMouseClick(activateBox.isSelected());
        container.setMicroworldChanged(true);
    }

    public void setFrame(ESlateInternalFrame fr) {
        if (frame == fr) return;
        frame = fr;
        updateSettings();
    }

    private void updateSettings() {
        minimizeBox.setSelected(frame.isMinimizeButtonVisible());
        maximizeBox.setSelected(frame.isMaximizeButtonVisible());
        closeBox.setSelected(frame.isCloseButtonVisible());
        pinBox.setSelected(frame.isPlugButtonVisible());
        helpBox.setSelected(frame.isHelpButtonVisible());
        infoBox.setSelected(frame.isInfoButtonVisible());
        componentNameBox.setSelected(frame.isComponentNameChangeableFromMenuBar());
        resizeBox.setSelected(frame.isResizable());
        compoBarVisibleBox.setSelected(frame.isTitlePanelVisible());
        activateBox.setSelected(frame.isComponentActivatedOnMouseClick());
    }
}

