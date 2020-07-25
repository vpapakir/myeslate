//Title:        JCheckMenu
//Version:      1Dec1999
//Copyright:    Copyright (c) 1998-1999
//Author:       George Birbilis
//Company:      CTI
//Description:  A menu with Check boxes (only up to one can be selected at a time)

package gr.cti.utils;

import java.beans.*;
import javax.swing.*;

public class JCheckMenu extends JMenu
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public JCheckMenu(){ //1Dec1999
  this("");
 }

 public JCheckMenu(String title){
  super(title);
 }

 public JCheckMenu(String title, boolean canBeTornOff) { //27Nov1999: "canBeTornOff" param is ignored for now, since Swing isn't implementing it yet
  super(title,canBeTornOff);
 }

////////////////////////////////

 //12-1-1999// protected ButtonGroup group=new ButtonGroup(); //27Nov1999: made protected
 protected gr.cti.utils.OnOffButtonGroup group=new gr.cti.utils.OnOffButtonGroup(); //27Nov1999: made protected

 public boolean isSelectionRequired(){ //27Nov1999
  return group.isSelectionRequired();
 }

 public void setSelectionRequired(boolean flag){ //27Nov1999
   group.setSelectionRequired(flag);
 }

 public ButtonModel getSelection(){ //19-1-1999
  return group.getSelection();
 }

 public void setSelection(ButtonModel bm){ //19-1-1999
  group.setSelection(bm);
 }

 public JMenuItem add(JMenuItem mi){
  //System.out.println("adding a JMenuItem from a RadioMenu");
  super.add(mi);
/**/  if(mi instanceof JCheckBoxMenuItem) {
       //System.out.println("JMenuItem:add(adding a CheckBoxMenuItem)");
       group.add(mi); //13-1-1999: other menu items not added to ButtonGroup
      }
  return mi;
 }

 public void remove(JMenuItem mi){
  //System.out.println("removing a JMenuItem from a RadioMenu");
  super.remove(mi);
/**/  if(mi instanceof JCheckBoxMenuItem) group.remove(mi); //13-1-1999: other menu items not added, so also not removed from ButtonGroup
 }

    /**
     * Creates a new CheckBoxMenuItem attached to the specified
     * ToggleAction object and appends it to the end of this menu.
     *
     * @param a the ToggleAction for the CheckBoxMenuItem to be added
     * @see ToggleAction
     */
    public JMenuItem add(ToggleAction a) {
        //System.out.println("JMenuItem:add(ToggleAction)");
        JCheckBoxMenuItem mi = new JCheckBoxMenuItem((String)a.getValue(Action.NAME),
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

    protected PropertyChangeListener createToggleActionChangeListener(JCheckBoxMenuItem b) {
        return new ToggleActionChangedListener(b);
    }

    private class ToggleActionChangedListener implements PropertyChangeListener {
        JCheckBoxMenuItem menuItem;

        ToggleActionChangedListener(JCheckBoxMenuItem mi) {
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
