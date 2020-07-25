package gr.cti.eslate.base.container.internalFrame;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

public class ESlateInternalFrameLayoutCustomizerEditor extends PropertyEditorSupport {
    private JButton button;
    public ESlateInternalFrameLayoutCustomizerEditor() {
        super();
        button=new JButton();
        button.setText(ESlateInternalFrameBeanInfo.bundle.getString("change"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog d=new ESlateInternalFrameLayoutCustomizer((ESlateInternalFrame) getValue());
                Point p=new Point(button.getX(),button.getY());
                SwingUtilities.convertPointToScreen(p,button.getParent());
                d.setLocation(p.x,p.y);
                d.show();
            }
        });
    }
    /**
     * @return The editor is a button which brings up a dialog.
     */
    public Component getCustomEditor() {
        return button;
    }
    public boolean supportsCustomEditor() {
        return true;
    }
}
