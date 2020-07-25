//Title:        ToggleActionMenuItem
//Version:      1.0
//Copyright:    Copyright (c) 1999
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import javax.swing.*;
import java.beans.*;

public class ToggleActionRadioMenuItem extends JRadioButtonMenuItem
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  public ToggleActionRadioMenuItem() {
  }


   /**
     * Creates a new RadioButtonMenuItem attached to the specified ToggleAction object
     *
     * @param a the ToggleAction
     * @see ToggleAction
     */

  public ToggleActionRadioMenuItem(ToggleAction a){
         //System.out.println("JMenuItem:add(ToggleAction)");
        setText((String)a.getValue(Action.NAME));
        setIcon((Icon)a.getValue(Action.SMALL_ICON));
        setHorizontalTextPosition(JButton.RIGHT);
        setVerticalTextPosition(JButton.CENTER);
        setEnabled(a.isEnabled());
        setSelected(a.isSelected()); //19-1-1999
        addChangeListener(a); //12-1-1999: not using addActionListener, cause it doesn't catch the depressing of a button
        //
        a.addPropertyChangeListener(createToggleActionChangeListener(this));
    }

    ////////////////////////////////////////////

    protected PropertyChangeListener createToggleActionChangeListener(JMenuItem b) {
        return new ToggleActionChangedListener(b);
    }

    private class ToggleActionChangedListener implements PropertyChangeListener {
        JMenuItem menuItem;

        ToggleActionChangedListener(JMenuItem mi) {
            super();
            this.menuItem = mi;
        }
        
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            //System.out.println("JRM:"+propertyName);
            if (propertyName.equals("selected")) {
                Boolean selectedState = (Boolean) e.getNewValue();
                menuItem.getModel().setSelected(selectedState.booleanValue());
            }

            else //got from JMenu
            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            } else if (e.getPropertyName().equals(Action.SMALL_ICON)) {
                Icon icon = (Icon) e.getNewValue();
                menuItem.setIcon(icon);
                menuItem.invalidate();
                menuItem.repaint();
            }
        }
    }

}

