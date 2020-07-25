//Author: George Birbilis
/*
 28Jul1999 - first creation
  2Dec1999 - using a JCheckedActionPopup instead of a JPopupMenu
           - now the pair component can be an AbstractButton and not any JComponent (when pressed reinvokes the last invoked action from the CheckedActionPopup)
           - now providing extra makePair static method takes an AbstractButton as 1st param and not an Action
  3Dec1999 - now the pair button gets enabled after the 1st menu selection on the popup and not after the 2nd one as was happening before!
  7Dec1999 - now the menu can be hidden OK when the button is pointing up (when the menu is visible that is) and it gets pressed
*/

package gr.cti.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.Action;
import javax.swing.JToolBar;
import javax.swing.AbstractButton;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;

import gr.cti.utils.JCheckedActionPopup;

/**
 * This is a Swing component that shows a down-headed button which when clicked shows a drop-down menu
 */
public class MenuDropButton extends BasicArrowButton //29Ju1l999
                            implements PopupMenuListener, Runnable
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
 /**
  * Constructs a new MenuDropButton instance
  */
 public MenuDropButton(){
  this(null,null);
 }

 public MenuDropButton(AbstractButton pair, JCheckedActionPopup menu){
  super(BasicArrowButton.SOUTH);
  setPair(pair); //first set the button, then the menu!
  setMenu(menu);
  addMouseListener(new Mouser()); //7Dec1999
  //addActionListener(this);
}

////////////////////////////////////

 private static void setup(JToolBar tb, AbstractButton b, JCheckedActionPopup m){ //2Dec1999
  tb.setMaximumSize(new java.awt.Dimension(37,22)); //29Jul1999
  tb.setFloatable(false);
  tb.setBorder(null);
  MenuDropButton mdb=new MenuDropButton(b,m);
  mdb.setMaximumSize(new java.awt.Dimension(7,22)); //29Jul1999
  tb.add(mdb);
 }

 public static JToolBar makePair(Action a, JCheckedActionPopup m){ //2Dec1999
  MyToolBar tb=new MyToolBar(); //use this one, cause the standard toolbar also shows subtitles at buttons, has focus at them, doesn't show hints etc. which is bad
  AbstractButton b=(AbstractButton)tb.add(a);
  setup(tb,b,m);
  return tb;
 }

 public static JToolBar makePair(AbstractButton b, JCheckedActionPopup m){ //2Dec1999: changed parameter from JPopupMenu to JCheckedActionPopup and Action to AbstractButton
  MyToolBar tb=new MyToolBar(); //use this one, cause the standard toolbar also shows subtitles at buttons, has focus at them, doesn't show hints etc. which is bad
  tb.add(b);
  setup(tb,b,m);
  return tb;
 }

////////////////////////////////////

 /**
  * Get the pair button
  * @return the pair button
  */
 public AbstractButton getPair(){
  return pair;
 }

 /**
  * Set the pair button
  * @param newPair the pair button
  */
 public void setPair(AbstractButton newPair){
  if(pair!=null) pair.removeActionListener(invoker); //7Dec1999: bug-fix: was removing ourselves, not the invoker inner-class instance which is the action listener to the pair button
  newPair.addActionListener(invoker);
  pair=newPair;
  enableDisableButton(); //this must be called last, since it acts on the "pair" object field and we must have first called "pair=newPair"  
 }

////////////////////////

 private Object getMenuSelection(){
  return (menu!=null)?menu.getSelection():null;
 }

 private void enableDisableButton(){
  AbstractButton b=getPair();
  if(b!=null)
   b.setEnabled(getMenuSelection()!=null); //enable-disable depending on whether some selection exists at the JCheckActionPopup
 }

////////////////////////

 /**
  * Get the menu
  * @return the drop-down menu
  */
 public JCheckedActionPopup getMenu(){
  return menu;
 }

 /**
  * Set the menu
  * @param newMenu the drop-down menu
  */
 public void setMenu(JCheckedActionPopup newMenu){
  if(newMenu!=null){
   if(menu!=null){ //if we currently had a menu
    menu.setVisible(false); //hide any current menu: do this, cause if any current menu is showing, the property change listener wouldn't be called when the removed menu gets hidden cause we'll unregister as listeners from that old menu
    menu.removePopupMenuListener(this);
   }
   newMenu.addPopupMenuListener(this); //add listener to change drop-down button's direction depending on menu visibility
   //if(newMenu!=null) setHint(newMenu.getName()); //what will happen if getName() returns null?
   newMenu.setInvoker(this); //must provide this, else many popups will be shown when you have many such buttons //also if this is missing there will be no mouse tracking on the items that would show a blue bar as you move the mouse over them
  }
  menu=newMenu;
 }

 private void updown_action(){ //7Dec1999
  if(menu!=null){
   if(menu.isVisible())  /* (getDirection()==BasicArrowButton.NORTH) - this is the same */
   {
    menu.setVisible(false); //hide the menu        //??? it doesn't hide (seems to hide-and-show-again: getting event twice) ???
   }
   else { //if the menu isn't showing...
    Point screen;
    if(pair!=null) screen=pair.getLocationOnScreen(); else screen=getLocationOnScreen();
    menu.setLocation(screen.x,screen.y+getHeight()); //29Jul1999: fixed to start at the same x position as the button //suppose has same height as pair component
    menu.setVisible(true);
   }
  }
 }

 private JCheckedActionPopup menu;
 private AbstractButton pair;

 ///

 public class Mouser extends MouseAdapter{ //7Dec1999: needed to open-close the button
  public void mousePressed(MouseEvent e){
   updown_action();
  }
 }

 public class Invoker implements ActionListener {
  public void actionPerformed(ActionEvent e) {
   if(menu!=null) menu.invokeLastAction();
  }
 }

 private ActionListener invoker=new Invoker();


// CHANGE ARROW BUTTON DIRECTION DEPENDING ON MENU VISIBILITY STATE /////////////////////////////////////////
// CHANGE PAIR BUTTON's enabling depending on menu having a selection ///////////////////////////////////////

    /**
     *  This method is called before the popup menu becomes visible
     */
    public void popupMenuWillBecomeVisible(PopupMenuEvent e){
     setDirection(BasicArrowButton.NORTH);
     repaint(); //must do a repaint!
    }

    /**
     * This method is called before the popup menu becomes invisible
     * Note that a JPopupMenu can become invisible any time
     */
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
     javax.swing.SwingUtilities.invokeLater(this); //enableDisableButton(); //this will get called if the popup shows/hides, so we can enable the button from here if some menu option is now checked
     setDirection(BasicArrowButton.SOUTH);
     repaint(); //must do a repaint!
    }

    public void popupMenuCanceled(PopupMenuEvent e){
     javax.swing.SwingUtilities.invokeLater(this); //enableDisableButton();
    }

   public void run(){enableDisableButton();} //3Dec1999: must use invokeLater to call the enableDisableButton method cause else the selection hasn't been done yet on the menu (it seems to close first, then change its selection)

}
