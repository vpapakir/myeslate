//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  Your description


package gr.cti.utils;

import java.beans.*;
import javax.swing.*;

public class ToggleActionButton extends JToggleButton
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public ToggleActionButton() {
  }

    /**
     * Add a new JToggleButton which dispatches the action.
     *
     * @param a the ToggleAction object to add as a new toolbar item
     */

    public ToggleActionButton(ToggleAction a) { //Birb: unfortunately can't override add, as JToolBar's add returns JButton and not AbstractButton
        setText((String)a.getValue(Action.NAME));
        setIcon((Icon)a.getValue(Action.SMALL_ICON));
        setHorizontalTextPosition(AbstractButton.CENTER);
        setVerticalTextPosition(AbstractButton.BOTTOM);
        setEnabled(a.isEnabled());
        setSelected(a.isSelected()); //19-1-1999
        addChangeListener(a); //12-1-1999: not using addActionListener, cause it doesn't catch the depressing of a button
        a.addPropertyChangeListener(createToggleActionChangeListener(this));
    }

    ////////////////////////////////////////////

    protected PropertyChangeListener createToggleActionChangeListener(JToggleButton b) {
     return new ToggleActionChangedListener(b);
    }

    private class ToggleActionChangedListener implements PropertyChangeListener {
        JToggleButton button;

        ToggleActionChangedListener(JToggleButton b) {
            super();
            this.button = b;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            //System.out.println("JTTB:"+propertyName);
            if (propertyName.equals("selected")) {
                Boolean selectedState = (Boolean) e.getNewValue();
                button.getModel().setSelected(selectedState.booleanValue());
                button.repaint();
            }

            else //Birb: copied the following from JToolBar's impl (if JToolBar was using AbstractButton wouldn't need to copy these)

            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                button.setText(text);
                button.repaint();
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                button.setEnabled(enabledState.booleanValue());
                button.repaint();
            } else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
                Icon icon = (Icon) e.getNewValue();
                button.setIcon(icon);
                button.invalidate();
                button.repaint();
            }

        }
    }



}