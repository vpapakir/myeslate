//Title:        JRadioMenu
//Version:      27Nov1999
//Copyright:    Copyright (c) 1998-1999
//Author:       George Birbilis (birbilis@cti.gr)
//Company:      CTI
//Description:  A Menu with radio buttons


package gr.cti.utils;

import java.beans.*;
import javax.swing.*;

public class JRadioMenu extends JCheckMenu //27Nov1999: extending JCheckMenu to avoid duplication of their common functionality (the one regarding the OnOffButtonModel)
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public JRadioMenu(String title){
  super(title);
  //System.out.println("JRadioMenu(String)");
 }

 public JRadioMenu(String title, boolean canBeTornOff) { //27Nov1999: "canBeTornOff" param is ignored for now, since Swing isn't implementing it yet
  super(title,canBeTornOff);
 }

////////////////////////////////

 public JMenuItem add(JMenuItem mi){
  //System.out.println("adding a JMenuItem from a RadioMenu");
  super.add(mi);
/**/  if(mi instanceof JRadioButtonMenuItem) {
       //System.out.println("JMenuItem:add(adding a RadioButtonMenuItem)");
       group.add(mi); //13-1-1999: other menu items not added to ButtonGroup
      }
  return mi;
 }

 public void remove(JMenuItem mi){
  //System.out.println("removing a JMenuItem from a RadioMenu");
  super.remove(mi);
/**/  if(mi instanceof JRadioButtonMenuItem) group.remove(mi); //13-1-1999: other menu items not added, so also not removed from ButtonGroup
 }

    /**
     * Creates a new RadioButtonMenuItem attached to the specified
     * ToggleAction object and appends it to the end of this menu.
     *
     * @param a the ToggleAction for the RadioButtonMenuItem to be added
     * @see ToggleAction
     */
    public JMenuItem add(ToggleAction a) {
        //System.out.println("JMenuItem:add(ToggleAction)");
        JRadioButtonMenuItem mi = new JRadioButtonMenuItem((String)a.getValue(Action.NAME),
                                     (Icon)a.getValue(Action.SMALL_ICON));
        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setVerticalTextPosition(JButton.CENTER);
        mi.setEnabled(a.isEnabled());
        mi.setSelected(a.isSelected()); //19-1-1999
        mi.addChangeListener(a); //12-1-1999: not using addActionListener, cause it doesn't catch the depressing of a button
        add(mi);
        //
        a.addPropertyChangeListener(createActionChangeListener(mi));
        a.addPropertyChangeListener(createToggleActionChangeListener(mi));
        return mi;
    }

    ////////////////////////////////////////////

    protected PropertyChangeListener createToggleActionChangeListener(JRadioButtonMenuItem b) {
        return new ToggleActionChangedListener(b);
    }

    private class ToggleActionChangedListener implements PropertyChangeListener {
        JRadioButtonMenuItem menuItem;

        ToggleActionChangedListener(JRadioButtonMenuItem mi) {
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
         }
    }

}