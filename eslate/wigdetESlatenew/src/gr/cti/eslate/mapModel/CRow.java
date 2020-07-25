package gr.cti.eslate.mapModel;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * This class is used by the TableModel in icon declaration of point layers.
 */
public class CRow {
    public CRow(Object value,ImageIcon icon,ImageIcon selIcon,ImageIcon highIcon) {
        this.value=value;
        this.icon=icon;
        this.selIcon=selIcon;
        this.highIcon=highIcon;
    }

    protected Object value;
    protected Icon icon;
    protected Icon selIcon;
    protected Icon highIcon;
}
