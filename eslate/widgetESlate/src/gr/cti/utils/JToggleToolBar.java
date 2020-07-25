//Title:        JToggleToolBar
//Version:      27Nov1999
//Copyright:    Copyright (c) 1998-1999
//Author:       George Birbilis (birbilis@cti.gr)
//Company:      CTI
//Description:  A ToolBar with radio buttons


package gr.cti.utils;

import java.awt.*;
import java.beans.*;
import javax.swing.*;

public class JToggleToolBar extends JToolBar
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 
//12-1-1999// ButtonGroup group=new ButtonGroup();
 gr.cti.utils.OnOffButtonGroup group=new gr.cti.utils.OnOffButtonGroup();

 public boolean isSelectionRequired(){ //27Nov1999
  return group.isSelectionRequired();
 }

 public void setSelectionRequired(boolean flag){ //27Nov1999
   group.setSelectionRequired(flag);
 }

 public ButtonModel getSelection(){ //15-2-1999 //???
  return group.getSelection();
 }

 public void setSelection(ButtonModel bm){ //15-2-1999 //???
  group.setSelection(bm);
 }

 public Component add(Component c, boolean grouped){ //28Jul1999: allow user to specify whether he wants to add a JToggleButton ungrouped
  super.add(c);
  if (grouped && c instanceof JToggleButton) group.add((AbstractButton)c);
  return c;
 }

 public Component add(Component c){ //change the ToolBar ancestor behaviour
  return add(c,true); //default="grouped"
 }

 public void remove(Component c){ //change the ToolBar ancestor behaviour
  super.remove(c);
/**/  if (c instanceof JToggleButton) group.remove((AbstractButton)c);
 }

    /**
     * Add a new JToggleButton which dispatches the action.
     *
     * @param a the ToggleAction object to add as a new toolbar item
     */
    public AbstractButton add(ToggleAction a) { //Birb: unfortunately can't override add, as JToolBar's add returns JButton and not AbstractButton
        JToggleButton b = new JToggleButton((String)a.getValue(Action.NAME),
                                (Icon)a.getValue(Action.SMALL_ICON));
        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setVerticalTextPosition(AbstractButton.BOTTOM);
        b.setEnabled(a.isEnabled());
        b.setSelected(a.isSelected()); //19-1-1999
        b.addChangeListener(a); //12-1-1999: not using addActionListener, cause it doesn't catch the depressing of a button
      //a.addPropertyChangeListener(createActionChangeListener(b)); //Birb: unfortunately can't use this, as JToolBar uses JButton instead of AbstractButton
        a.addPropertyChangeListener(createToggleActionChangeListener(b));
        return b;
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