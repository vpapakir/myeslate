//Title:        utilsBirb/DelayClosingMenu
//Version:      15Mar2000
//Copyright:    Copyright (c) 2000
//Author:       George Birbilis
//Company:      CTI
//Description:  My utilities

package gr.cti.utils;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DelayClosingMenu extends JMenu implements Runnable
{
 /**
  * Serialization version.
  */
 final static long serialVersionUID = 1L;
  
 private int myDelay;

 public void setMyDelay(int delay){
  myDelay=delay;
 }

 public int getMyDelay(){
  return myDelay;
 }

 //CONSTRUCTORs//

 private void init(){
  //addMouseListener(new MouseHandler());
 }

 public DelayClosingMenu() {
  super();
  init();
 }

 public DelayClosingMenu(String title) {
  super(title);
  init();
 }

 public DelayClosingMenu(String title, boolean isTearOff) {
  super(title,isTearOff);
  init();
 }

 ////////////////////////

 private int reentrances=0;
 private boolean open;

 public void menuSelectionChanged(boolean isIncluded) {
  if(isIncluded) {
   open=true;
   new Thread(this).start();
   super.menuSelectionChanged(true);
  }
  else
   if(open) MenuSelectionManager.defaultManager().setSelectedPath(buildMenuElementArray(this)); //reopen
   else super.menuSelectionChanged(false);
 }

 public void run(){
  reentrances++;
  try{wait(5000);}catch(Exception e){}
  reentrances--;
  if(reentrances!=0) open=false;
 }

 ////////////////////////

 class MouseHandler extends MouseAdapter{
  public void mouseClicked(MouseEvent e){
   System.out.println("mh1");
   boolean flag=!isSelected();
   setSelected(flag); //toggle opened state using clicks
   setPopupMenuVisible(flag); //toggle popup visibility
   System.out.println("mh2");
  }
 }

//////////////////////

    /*
     * Build an array of menu elements - from my PopupMenu to the root
     * JMenuBar
     */
    private MenuElement[] buildMenuElementArray(JMenu leaf) {
        ArrayList<MenuElement> elements = new ArrayList<MenuElement>();
        java.awt.Component current = leaf.getPopupMenu();
        JPopupMenu pop;
        JMenu menu;
        JMenuBar bar;

        while (true) {
            if (current instanceof JPopupMenu) {
                pop = (JPopupMenu) current;
                elements.add(0, pop);
                current = pop.getInvoker();
            } else if (current instanceof JMenu) {
                menu = (JMenu) current;
                elements.add(0, menu);
                current = menu.getParent();
            } else if (current instanceof JMenuBar) {
                bar = (JMenuBar) current;
                elements.add(0, bar);
                int nElements = elements.size();
                MenuElement me[] = new MenuElement[nElements];
                for (int i=0; i<nElements; i++) {
                  me[i] = elements.get(i);
                }
                return me;
            }
        }
    }

}
