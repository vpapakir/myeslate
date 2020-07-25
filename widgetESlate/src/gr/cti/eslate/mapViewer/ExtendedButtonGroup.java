package gr.cti.eslate.mapViewer;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * A ButtonGroup that lets the user deselect the selected button.
 * In this button group either one, either none button can be selected.
 */
class ExtendedButtonGroup extends ButtonGroup {
    /**
     * Sets the selected value for the button.
     */
    public void setSelected(ButtonModel m, boolean b) {
        if (b && m != getSelection()) {
            super.setSelected(m,b);
        } else if (!b && m == getSelection()) {
            super.setSelected(null,true);
        }
    }
}
