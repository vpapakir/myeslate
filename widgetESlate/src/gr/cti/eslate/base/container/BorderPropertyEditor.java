package gr.cti.eslate.base.container;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;
import javax.swing.border.Border;


public class BorderPropertyEditor extends PropertyEditorSupport {
    Border border;
    PropertyChangeSupport pcs;
//    Locale locale;
    private static ResourceBundle borderPropertyEditorBundle =
        ResourceBundle.getBundle("gr.cti.eslate.base.container.BorderPropertyEditorBundle", Locale.getDefault());
//    boolean localeIsGreek = false;
    /* The component to which this layout applys.
     */
    Component component = null;
    NoBorderButton editButton;

    public BorderPropertyEditor() {
        super();
        pcs = new PropertyChangeSupport(this);
//        locale = Locale.getDefault();
//        if (borderPropertyEditorBundle.getClass().getName().equals("gr.cti.eslate.base.container.BorderPropertyEditorBundle_el_GR"))
//            localeIsGreek = true;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }


    public void setValue(Object value) {
        if (value != null && !Border.class.isInstance(value))
            return;
//        layout = (LayoutManager) value;
        if (border == null && value == null)
            return;
        if (border != null && border.equals(value))
            return;

        Border tmp = border;
        border = (Border) value;
        if (editButton != null) {
            editButton.setText(border2String(border));
        }
//        System.out.println("2. LayoutPropertyEditor setValue(): " + value);
        pcs.firePropertyChange("Border", tmp, border);
    }

    public Object getValue() {
        return border;
    }

    public void setComponent(Component comp) {
        this.component = comp;
    }

    public java.awt.Component getCustomEditor() {
        editButton = new NoBorderButton();
        editButton.setText(border2String(border)); //borderPropertyEditorBundle.getString("EditBorder"));
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        editButton.setMargin(zeroInsets);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Frame topLevelFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, editButton);
                BorderEditorDialog bed = new BorderEditorDialog(topLevelFrame, BorderPropertyEditor.this, border);
                if (bed.getReturnCode() == BorderEditorDialog.OK) {
                    setValue(bed.getBorder());
                    revalidateComponent();
                }
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

    public String border2String(Border border) {
        if (border == null)
            return borderPropertyEditorBundle.getString("NoBorder");
        if (javax.swing.border.LineBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.LINEBORDER);
        if (javax.swing.border.MatteBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.MATTEBORDER);
        if (javax.swing.border.EmptyBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.EMPTYBORDER);
        if (gr.cti.eslate.utils.OneLineBevelBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.ONELINEBEVELBORDER);
        if (gr.cti.eslate.utils.NoTopOneLineBevelBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.NOTOPONELINEBEVELBORDER);
        if (javax.swing.border.SoftBevelBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.SOFTBEVELBORDER);
        if (javax.swing.border.BevelBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.BEVELBORDER);
        if (javax.swing.border.EtchedBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.ETCHEDBORDER);
        if (javax.swing.border.TitledBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.TITLEDBORDER);
        if (javax.swing.border.CompoundBorder.class.isAssignableFrom(border.getClass()))
            return BorderEditorDialog.borderDialogBundle.getString(BorderEditorDialog.COMPOUNDBORDER);
        return borderPropertyEditorBundle.getString("UnknownBorder");
    }
}

