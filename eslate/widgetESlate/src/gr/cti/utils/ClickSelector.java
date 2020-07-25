//Title:        utilsBirb
//Version:      27Nov1999
//Copyright:    Copyright (c) 1998-1999
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.Component;
import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.EventListenerList;

import java.awt.ItemSelectable;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ClickSelector
 implements ItemSelectable //a standard interface for components that have selected items (or are themselves selectable, like a JToggleButton)
{

 private Component component;
 private Color normalFgr, normalBkgr, selectionFgr, selectionBkgr;
 private boolean selected;
 private ChangeEvent changeEvent; //changeEvent reusable object
 private EventListenerList listenerList=new EventListenerList(); //use standard Swing listener-list support

 private void init(Component c)
 {
  component=c;
  normalFgr=c.getForeground();
  normalBkgr=c.getBackground();
  c.addMouseListener(new MouseHandler());
 }

 ///////

 public ClickSelector(Component c)
 {
  this.selectionFgr=Color.yellow;
  this.selectionBkgr=Color.red;
  init(c);
 }

 public ClickSelector(Component c, Color selectionFgr)
 {
  this.selectionFgr=selectionFgr;
  this.selectionBkgr=c.getBackground();
  init(c);
 }

 public ClickSelector(Component c, Color selectionFgr, Color selectionBkgr)
 {
  this.selectionFgr=selectionFgr;
  this.selectionBkgr=selectionBkgr;
  init(c);
 }

 public ClickSelector(Component c, Color selectionFgr, Color selectionBkgr, Color normalFgr, Color normalBkgr)
 {
  component=c;
  this.selectionFgr=selectionFgr;
  this.selectionBkgr=selectionBkgr;
  this.normalFgr=normalFgr;
  this.normalBkgr=normalBkgr;
  c.addMouseListener(new MouseHandler());
 }

 public boolean isSelected(){
  return selected;
 }

 public void setSelected(boolean flag){
  selected=flag;
  synchronized(component.getTreeLock()){
   component.setForeground(flag?selectionFgr:normalFgr);
   component.setBackground(flag?selectionBkgr:normalBkgr);
  }

  //Send ChangeEvent
  fireStateChanged();

  //Send ItemEvent
  fireItemStateChanged(
                    new ItemEvent(this,
                                  ItemEvent.ITEM_STATE_CHANGED,
                                  this,
                                  this.isSelected() ?  ItemEvent.SELECTED : ItemEvent.DESELECTED));

  component.repaint();
 }

 public class MouseHandler extends MouseAdapter{
  public void mouseClicked(MouseEvent e) {
   setSelected(!isSelected());
  }
 }


// ItemSelectable ///////////////////////////////////////////////////////////////////////////////////////////

  public Object[] getSelectedObjects() {
   return selected?new Component[]{component}:null;
  }

  public void addItemListener(ItemListener l) {
   listenerList.add(ItemListener.class,l); //don't use l.getClass() else descendent's won't be notified in that case (cause the fireItemStateChanged uses == to compare the "class" field)
  }

  public void removeItemListener(ItemListener l) {
   listenerList.remove(ItemListener.class,l); //don't use l.getClass() else descendent's won't be notified in that case (cause the fireItemStateChanged uses == to compare the "class" field)
  }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireItemStateChanged(ItemEvent event) { //got from javax.swing.AbstractButton's code
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ItemEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ItemListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new ItemEvent(this, //the source
                                      ItemEvent.ITEM_STATE_CHANGED,
                                      component, //the item
                                      event.getStateChange());
                }
                ((ItemListener)listeners[i+1]).itemStateChanged(e);
            }
        }
    }


// support for ChangeListener (from javax.swing.AbstractButton source) /////////////////////////////////////

    /**
     * Adds a ChangeListener to the button.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the button.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireStateChanged() { //got from javax.swing.AbstractButton's code
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }

}

