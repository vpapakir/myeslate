//Title:        utilsBirb
//Version:      2Dec1999
//Copyright:    Copyright (c) 1998
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.event.*;

import javax.swing.*;

public class JCheckedActionPopup extends JPopupMenu
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 public JCheckedActionPopup(){ //1Dec1999
  this("");
 }

 public JCheckedActionPopup(String title){
  super(title);
 }

 public void invokeLastAction(){
  Object source=getLastSource();
  //System.out.println("JCheckedActionPopup: lastSource="+source);
  if(source!=null)
   if (source instanceof AbstractButton) ((AbstractButton)source).doClick(); //reinvoke the menu item
   else if (source instanceof ActionListener) ((ActionListener)source).actionPerformed(new ActionEvent(source,0,"pressed"));
 }

 public Object getLastSource(){
  return rememberer.getLastSource();
 }

////////////////////////////////

 protected ButtonGroup group=new ButtonGroup(); //2Dec1999: don't use the OnOffButtonGroup here

 public ButtonModel getSelection(){ //19-1-1999
  return group.getSelection();
 }                 

 public void setSelection(ButtonModel bm){ //19-1-1999
  group.setSelected(bm,true);
 }

// private void setSelection(JCheckBoxMenuItem m){ //2Dec1999
//  setSelection((ButtonModel)m.getModel());
// }


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
     * Action object and appends it to the end of this menu.
     *
     * @param a the ToggleAction for the CheckBoxMenuItem to be added
     * @see ToggleAction
     */
    public JMenuItem add(Action a) {
        JCheckBoxMenuItem mi = new JCheckBoxMenuItem((String)a.getValue(Action.NAME),
                                     (Icon)a.getValue(Action.SMALL_ICON));
        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setVerticalTextPosition(JButton.CENTER);
        mi.setEnabled(a.isEnabled());
        mi.addActionListener(a);
        add(mi);
        a.addPropertyChangeListener(createActionChangeListener(mi)); //do whatever our parent does when the action is enabled/disabled
        //
//        mi.addActionListener(hider);
        mi.addActionListener(rememberer); //remember last item (do this before adding to the group -> the group might select the action to be the default for "firing buttons" attatched to the popup)
        //
        return mi;
    }

/*
 public class Hider implements ActionListener{
  public void actionPerformed(ActionEvent e){
   setSelection((JCheckBoxMenuItem)e.getSource()); //!!! select the item NOW for popup-hide-listeners to get it !!!
   JCheckedActionPopup.this.setVisible(false); //hide the popup only after we process the new selected item, cause JCheckBoxMenuItems doesn't hide it by default. Since the clicked checkbox'es state has just changed now it's possible for popup-menu-hiding listaners to ask for the latest selection!
  }
 }
*/

 //

// private Hider hider=new Hider();
 private RememberActionHandler rememberer=new RememberActionHandler();

}


