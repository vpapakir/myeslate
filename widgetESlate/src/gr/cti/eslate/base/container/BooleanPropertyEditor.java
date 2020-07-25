package gr.cti.eslate.base.container;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


public class BooleanPropertyEditor extends PropertyEditorSupport {
    Boolean oldValue;
    Boolean value;
    PropertyChangeSupport pcs;
    BooleanCustomEditor boolEditor;

    public BooleanPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
        boolEditor = new BooleanCustomEditor(pcs);
/*        textF1ield = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Integer tmp = value;
                    try{
                        value = new Integer(textField.getText());
                        oldValue = tmp;
                        pcs.firePropertyChange("Int", oldValue, value);
                    }catch (Exception exc) {
                        textField.setText(tmp.toString());
                    }
                }
            }
        });
*/        
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public java.awt.Component getCustomEditor() {
        return boolEditor;
    }

    public void setValue(Object value) {
        if (!Boolean.class.isInstance(value))
            return;
        this.value = (Boolean) value;
        boolEditor.setStatus(this.value.booleanValue());
    }

    public Object getValue() {
        return new Boolean(boolEditor.getStatus());
    }

    public boolean supportsCustomEditor() {
        return true;
    }
}

class BooleanCustomEditor extends JPanel {
//    JRadioButton trueButton, falseButton;
    private String trueStr, falseStr;
//0.9.8.2    private NoBorderButton button;
    private JCheckBox checkbox;
    PropertyChangeSupport pcs;
    private ResourceBundle booleanPropertyEditorBundle;
    private boolean mousePressedOnLabel = false;
    private static java.awt.Insets buttonInsets = new java.awt.Insets(1, 2, 1, 2);

    public BooleanCustomEditor(PropertyChangeSupport pchs) {
        super(true);
        this.pcs = pchs;

        booleanPropertyEditorBundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.BooleanPropertyEditorBundle", Locale.getDefault());
//        if (booleanPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.DimensionPropertyEditorBundle_el_GR"))
        trueStr = booleanPropertyEditorBundle.getString("True");
        falseStr = booleanPropertyEditorBundle.getString("False");

//0.9.8.2        button = new NoBorderButton(trueStr);
//0.9.8.2        button.setMargin(buttonInsets);
        checkbox = new JCheckBox();
        checkbox.addItemListener(new ItemListener()  {
            public void itemStateChanged(ItemEvent evt)  {
                if (evt.getStateChange() == ItemEvent.SELECTED)  {
                    setStatus(Boolean.TRUE.booleanValue());
                    pcs.firePropertyChange("Boolean", Boolean.FALSE, Boolean.TRUE);
                } else {
                    setStatus(Boolean.FALSE.booleanValue());
                    pcs.firePropertyChange("Boolean", Boolean.TRUE, Boolean.FALSE);
                }
            }
        });
/* 0.9.8.2        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkbox.isSelected()) {
                    setStatus(false);
                    pcs.firePropertyChange("Boolean", Boolean.TRUE, Boolean.FALSE);
                }else{
                    setStatus(true);
                    pcs.firePropertyChange("Boolean", Boolean.FALSE, Boolean.TRUE);
                }
            }
        });
*/
/*        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePressedOnLabel = true;
            }
            public void mouseReleased(MouseEvent e) {
                if (!mousePressedOnLabel) return;
                mousePressedOnLabel = false;
                if (!label.getVisibleRect().contains(e.getPoint())) return;

                if (checkbox.isSelected()) {
                    setStatus(false);
                    pcs.firePropertyChange("Boolean", Boolean.TRUE, Boolean.FALSE);
                }else{
                    setStatus(true);
                    pcs.firePropertyChange("Boolean", Boolean.FALSE, Boolean.TRUE);
                }
            }
        });
*/
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(checkbox);
//0.9.8.2        add(button);
/*        trueButton = new JRadioButton("True");
        falseButton = new JRadioButton("False");

        add(trueButton);
        add(Box.createHorizontalStrut(10));
        add(falseButton);

        ButtonGroup bg = new ButtonGroup();
        bg.add(trueButton);
        bg.add(falseButton);

        trueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pcs.firePropertyChange("Boolean", Boolean.FALSE, Boolean.TRUE);
            }
        });
        falseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pcs.firePropertyChange("Boolean", Boolean.TRUE, Boolean.FALSE);
            }
        });
*/
    }

    public void setStatus(boolean status) {
//        if (checkbox == null) return;

        if (status) {
            if (checkbox.isSelected() != status)
                checkbox.setSelected(true);
//0.9.8.2            button.setText(trueStr);
//            trueButton.setSelected(true);
//            falseButton.setSelected(false);
        }else{
            if (checkbox.isSelected() != status)
                checkbox.setSelected(false);
//0.9.8.2            button.setText(falseStr);
//            trueButton.setSelected(false);
//            falseButton.setSelected(true);
        }
    }

    public boolean getStatus() {
        return checkbox.isSelected();
//        return trueButton.isSelected();
    }

    public void setEnabled(boolean enabled) {
        checkbox.setEnabled(enabled);
//0.9.8.2        button.setEnabled(enabled);
//        trueButton.setEnabled(enabled);
//        falseButton.setEnabled(enabled);
    }
}
