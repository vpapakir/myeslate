package gr.cti.eslate.base.container;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;


public class ColorPropertyEditor extends PropertyEditorSupport {
    Color color;
    PropertyChangeSupport pcs;
    static JColorChooser colorChooser = null;
    static JDialog colorChooserDialog = null;
    static boolean dialogWasCancelled = false;
    JButton button;
    ResourceBundle colorEditorBundle;

    public ColorPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        colorEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.ColorPropertyEditorBundle", Locale.getDefault());
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (!Color.class.isInstance(value))
            return;
        color = (Color) value;
//        ((JButton) getCustomEditor()).setBackground(color);
    }

    public Object getValue() {
        return color;
    }

    public java.awt.Component getCustomEditor() {
        button = new JButton() {
			public void updateUI() {
				setBorder(new LineBorder(UIManager.getColor("controlShadow")));
			}
        };
        button.setUI(new BasicButtonUI());
        button.setBorder(new LineBorder(UIManager.getColor("controlShadow")));
        Dimension buttonSize = new Dimension(120, 22);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setBackground(color);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color oldColor = color;
                if (colorChooser == null) {
                    colorChooser = new JColorChooser(oldColor);
                    Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, button);
                    colorChooserDialog = colorChooser.createDialog(topLevelFrame,
                              colorEditorBundle.getString("SelectColor"),
                              true,
                              colorChooser,
                              null,
                              new ActionListener() {
                                  public void actionPerformed(ActionEvent e) {
                                      dialogWasCancelled = true;
                                  }
                              });
                }
                if (color != null)
                    colorChooser.setColor(color);
                colorChooserDialog.show();
                if (!dialogWasCancelled) {
                    color = colorChooser.getColor();
                    button.setBackground(color);
                    pcs.firePropertyChange("Color", oldColor, color);
                }else
                    dialogWasCancelled = false;
/*                Color returnedColor = JColorChooser.showDialog(new JFrame(), "Select color", color);
                if (color != null) {
                    color = returnedColor;
                    ((JButton) e.getSource()).setBackground(color);
                    pcs.firePropertyChange("Color", oldColor, color);
                }
*/
            }
        });
        return button;
    }

    public String getAsText() {
        return null;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}
