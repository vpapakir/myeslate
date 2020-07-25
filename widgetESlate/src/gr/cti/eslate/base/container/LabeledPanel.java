package gr.cti.eslate.base.container;

import javax.swing.JLabel;

import com.zookitec.layout.ExplicitLayout;


public interface LabeledPanel {
    public JLabel getLabel();
    public ExplicitLayout getExplicitLayout();
}