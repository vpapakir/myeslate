//Title:        Stage/CustomizerDialog
//Version:      18May2000
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  stage

package gr.cti.eslate.stage.customizers;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import gr.cti.eslate.stage.Res;
import gr.cti.eslate.stage.BaseObject;
import gr.cti.eslate.stage.models.IControlPointContainer;

import gr.cti.utils.Cloning;
import gr.cti.eslate.utils.*;

/**
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 */
public class CustomizerDialog extends JDialog
 implements ICustomizer
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private JPanel panel1 = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel jPanel2;
  private BorderLayout borderLayout2;
  private JPanel jPanel3;
  private JButton btnCancel;
  private JButton btnOK;
  private Component component1;
  private BaseObject object;
  private JComponent[] children;
  private JTabbedPane tabPane;

  public CustomizerDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public CustomizerDialog() {
    this(null, "", false);
  }

  ///

  public CustomizerDialog(BaseObject o, JComponent[] children){
   this();
   for(int i=0;i<children.length;i++)
    try{
     tabPane.add(((ICustomizer)children[i]).getTitle(),children[i]);
    }catch(Exception ex){ //17May2000: in case some page fails
     ex.printStackTrace(); //18May2000: do print a stack trace if some child customizer fails (for debugging reasons)
     children[i]=null;
     tabPane.add(new JPanel()); //add something to keep the one-to-one relationship between the tabs and the children array elements: the page will be removed at "setObject"
    }
   this.children=children;
   setObject(o);
  }

///////////////////////////////

 public void setParent(ICustomizer parent){} //dummy, do nothing

 public void setupWidgetsFromObject(){
  //do for all pages//
  for(int i=0;i<children.length;i++)
   if (children[i]!=null) ((ICustomizer)children[i]).setupWidgetsFromObject();
 }

 ////////////////////

 byte[] storedProperties; //15May2000: temp storage for the properties' ObjectHash of the object, not the object itself

 protected void doBackup(){ //15May2000 //18May2000: renamed to "doBackup"
  storedProperties=Cloning.storeObject(object.getProperties());
 }

 protected void doRestore(){ //18May2000
  //18May2000// object.dispose(); //must dispose the object first, so that its control points are disposed and the loaded ones (from restoreObject) don't have their names changed
  if(object instanceof IControlPointContainer) //18May2000: don't remove the object, else we can't set its name property since it gets unregistered from the namespace at its disposal
   try{
    //System.out.println("removal of the old control points");
    ((IControlPointContainer)object).removeAllControlPoints(); //don't do "setControlPoints(null)" here: must ancestors of SceneObject like Ball etc. will throw errors if the number of the control points passed to them isn't the one they need
   }catch(Exception ex){
    ex.printStackTrace();
   }
  //object.setProperties((ObjectHash)Cloning.restoreObject(storedProperties));
  object.setProperties((StorageStructure )Cloning.restoreObject(storedProperties));
 }

 protected void freeBackup(){
  storedProperties=null; //free the stored properties
 }

 ////////////////////

 public void setObject(Object o){
  object=(BaseObject)o;
  //do for all pages//
  int removed=0; //at first no tabs have been removed
  for(int i=0;i<children.length;i++)
   try {
    ICustomizer child=(ICustomizer)children[i];
    child.setObject(o); //do this BEFORE doing "setParent(this)", so that while new object is being setup, pages don't fire back events to the parent dialog to talk to all pages (some of which are still not setup!)
    child.setParent(this); //17May2000: must do this, so that when a change happens on a child page it can ask the dialog to update all pages (and that page itself, so that other widgets on that page do reflect possible side-effects)
   }
   catch(Exception e){ //catch null-pointer and class-cast exceptions
    children[i]=null;
    tabPane.removeTabAt(i-removed); //14Apr2000: fixed-bug: wasn't counting-in any already removed pages (couldn't remove more than one page cause it was throwing ArrayOutOfBoundsException)
    removed++; //increment after removing the object
   } //don't use remove, just removes the tab's contents, not the tab itself
  doBackup(); //15May2000
  //setupWidgetsFromObject(); //17May2000: don't need to call this cause it will do the same action for a second time: each customizer page does update its widgets when its "setObject(Object)" gets called
 }

 public Object getObject(){
  return object;
 }

///////////////////////////////

  void jbInit() throws Exception {
    jPanel2 = new JPanel();
    borderLayout2 = new BorderLayout();
    jPanel3 = new JPanel();
    btnCancel = new JButton();
    btnOK = new JButton();
    component1 = Box.createHorizontalStrut(8);
    tabPane = new JTabbedPane();
    panel1.setLayout(borderLayout1);
    jPanel2.setLayout(borderLayout2);

    // ESCAPE HANDLER
    getRootPane().registerKeyboardAction(new java.awt.event.ActionListener() { //12May2000 - from G.Tsironis code snippet
     public void actionPerformed(java.awt.event.ActionEvent e) {
      btnCancel.doClick(); //press the cancel button (so that any side-effects are done)
     }
    }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

    btnCancel.setText(Res.localize("Cancel"));
    btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnCancel_actionPerformed(e);
      }
    });

    btnOK.setText(Res.localize("OK"));
    btnOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        btnOK_actionPerformed(e);
      }
    });

    jPanel3.setBorder(BorderFactory.createEtchedBorder());
    this.setTitle(Res.localize("Customization")); //11May2000: localized dialog title

    this.setModal(true);

    tabPane.setMinimumSize(new Dimension(250, 250));
    tabPane.setPreferredSize(new Dimension(250, 250));
    //tabPane.setForeground(Color.blue); //18May2000: set to hyperlink-like color (main reason is to make the page headers different from the page contents)

    getContentPane().add(panel1);
    panel1.add(jPanel2, BorderLayout.SOUTH);
    jPanel2.add(jPanel3, BorderLayout.CENTER);
    jPanel3.add(component1, null);
    jPanel3.add(btnOK, null);
    jPanel3.add(btnCancel, null);
    panel1.add(tabPane, BorderLayout.CENTER);
  }

  public void dispose(){ //15May2000
   freeBackup();
   super.dispose();
  }

  void btnOK_actionPerformed(ActionEvent e) {
   //setVisible(false);
   dispose(); //8May2000: destroy the dialog peer
  }

  void btnCancel_actionPerformed(ActionEvent e) {
   //setVisible(false);
   doRestore();
   dispose(); //8May2000: destroy the dialog peer and dispose of the stored clone
  }

}
