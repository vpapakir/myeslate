//Title:        OnOffButtonGroup
//Version:      1Dec1999
//Copyright:    Copyright (c) 1998-1999
//Author:       George Birbilis
//Company:      CTI
//Description:  A Button-Group that allows all buttons to be deselected at a time
//              (normal ButtonGroup has a bug(?) in its setSelected method)

package gr.cti.utils;

import javax.swing.*;

public class OnOffButtonGroup extends ButtonGroup
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private boolean selectionRequired; //27Nov1999

 public OnOffButtonGroup(){ //required!
  super();
 }

 public OnOffButtonGroup(boolean selectionRequired){
  super();
  setSelectionRequired(selectionRequired);
 }

 public void setSelection(ButtonModel m){ //19-1-1999: pass null to deselect all
  setSelected(m,true);
 }

    /**
     * Sets the selected value for the button.
     */
    public void setSelected(ButtonModel m, boolean b) {

        if(!b && m==getSelection()){
         //m.setSelected(false); //not needed: next one will also deselect current selection
         super.setSelected(null,true); //must give true (ancestor code is checking for 2nd-param=="true" and selection!="null")
        }
        else super.setSelected(m,b);

    }

 //SelectionRequired property//

 public boolean isSelectionRequired(){ //27Nov1999
  return selectionRequired;
 }

 public void setSelectionRequired(boolean flag){ //27Nov1999
  selectionRequired=flag;
  forceSelectionIfNeeded();
 }

 /////////////

 public void add(AbstractButton b){ //27Nov1999
  super.add(b);
  forceSelectionIfNeeded();
 }

 public void remove(AbstractButton b){ //27Nov1999
  super.remove(b);
  forceSelectionIfNeeded();
 }

 //

 protected boolean forceSelectionIfNeeded(){ //27Nov1999
  if(selectionRequired && getSelection()==null){
   if(buttons.size()>0) { //1Dec1999: fixed bug: before calling Vector::firstElement we must ensure there are elements in the Vector else we'll have an exception thrown!
    AbstractButton b=(AbstractButton)buttons.firstElement(); //get first button
    b.setSelected(true); //if there are any AbstractButtons, select the first one
    return true; //return true if did force a new selection
   }
  }
  return false; //return false if a new selection wasn't required, or we couldn't force a selection cause there were no members
 }

}



