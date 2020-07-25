/*
 ??? - 29Jul1998 - added Logo Scripting support
 ...
 6.1 - 24Feb1999 - corrected About dialogs
                 - changed the deprecated Pin.getMyComponent() to Pin.getComponent()
 6.2 -  8Apr1999 - now can run as an application and start others too specified at the command line
 6.3 - 26Apr1999 - removed the destroy() method that had been added by G.Tsironis to call ESlateComponent.destroyCalled()
                 - now runs OK as an application: was instantiating the Turtle component instead of the Palette
 7.0 - 26Apr1999 - moving to ESlateII
 7.1 - 14May1999 - now can run as an application (was throwing null pointer exception)
 7.2 - 19May1999 - now messages bundle loading only statically and not twice, first statically, then again in the constructor
                 - now extends ESlateApplet instead of OldESlateComponent
                 - made higher
 7.3 - 25May1999 - using the new scripting mechanism
 7.4 -  2Jun1999 - not an Applet anymore
 7.5 -  4Jun1999 - catching an exception thrown at destruction when we get a change event from the color chooser
 7.6 - 11Jun1999 - bug-fix: now can run as an application again
                 - PaletteApplet asks Palette for its preferred size
 8.0 -  3Aug1999 - moved to package gr.cti.eslate
     -  1Sep1999 - changed PalettePrimitives code to make use of LogoAWT convertions' utility class (code is smaller and more clear now)
 8.1 - 28Jun2000 - changed the PalettePrimitives code to make use of "getFirstComponentToTell" utility method of the LogoEngine and made its source code simpler
     - 29Jun2000 - added beanInfo and icon
                 - moved some applet specific code from the Palette class to PaletteApplet class
                 - fixed the "createPlugs" method so that the "plug connectionEvent" code is now compatible with the latest E-Slate version
                 - removed the color chooser preview area
                 - made the preferred size much smaller
                 - fixed Greek resource bundle: now component showing with a localized name
*/

package gr.cti.eslate.palette;

//GREEK-START//
import java.util.ResourceBundle;
import java.util.Locale;
//GREEK-END//

//AVAKEEO-START//
import gr.cti.eslate.base.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.scripting.*; //SCRIPTING//
import gr.cti.eslate.birbilis.BaseComponent;
//AVAKEEO-END//

import javax.swing.*;
import javax.swing.event.*;

import java.io.*;

import java.awt.*;

import gr.cti.utils.JComponentFrame;

/* 28Aug2000: StorageVersion = 1
              ComponentVersion = 1.0.1
*/


/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class Palette extends BaseComponent //2Jun1999
                    implements Externalizable,
                               SharedObjectListener, //E-SLATE//
                               LogoScriptable,       //LOGO SCRIPTING//
                               AsPalette             //SCRIPTING//
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
// public final static String VERSION="8.1"; //29Jun2000
 public final static String VERSION="2.0.1"; //28Aug2000
 //public static final String STORAGE_VERSION = "1";
 public static final int STORAGE_VERSION = 1;

 public String[] getSupportedPrimitiveGroups(){ //SCRIPTING//
  String[] s={"gr.cti.eslate.scripting.logo.PalettePrimitives"};
  return s;
 }

//////////// LOCALIZATION-START /////////////////////////////////////////////// //8-4-1999: got from Canvas' implementation

 transient static protected ResourceBundle m; //GREEK// //made static

 public static String localize(String s){ //made static
  try{return m.getString(s);}
  catch(Exception e){
   //System.out.println("Couldn't localize "+s);
   return s;
  }
 }

 static{ //made this static: localize(...) is used at variables' initialization that are set before the constructor gets called
  try{
   m = ResourceBundle.getBundle("gr.cti.eslate.palette.MessagesBundle", Locale.getDefault());
  }catch(/*MissingResource*/Exception e){ //catching all Exceptions cause in MS-JVM some "bad path" is thrown when a ResourceBundle is missing
   System.err.println("Couldn't find messages resource");
  } //if localized bundle is not found it shall load MessagesBundle.class
 }

//////////// LOCALIZATION-END ///////////////////////////////////////////////////////////////////////////////////////////////////////

 /**
  * Returns Copyright information.
  * @return	The Copyright information.
  */
 public ESlateInfo getInfo()
 {return new ESlateInfo(localize("title")+VERSION,m.getStringArray("info"));}

 public PaletteColorChange colors; //don't make this private: inner classes shall throw Illegal Access Error if using JARs

 public JComponent getContent(){
  if(gui==null) gui=new JPanel();
  return gui;
 }

 public Palette(){
  super();

  setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));

  try{
   setUniqueComponentName(localize("name"));
  }catch(RenamingForbiddenException e){}

  createPlugs();
  createGUI();
 }

 private void createGUI(){ //29Jun2000: moved GUI creation code to separate routine
  chooserPanel=new JPanel();
  chooserPanel.setLayout(new BoxLayout(chooserPanel,BoxLayout.Y_AXIS));
  fgrChooser=new MyJColorChooser(PaletteColorChange.NEW_FGR,colors.get_fgrColor());
  fillChooser=new MyJColorChooser(PaletteColorChange.NEW_FILL,colors.get_fillColor());
  bkgrChooser=new MyJColorChooser(PaletteColorChange.NEW_BKGR,colors.get_bkgrColor());
  //
  radioPanel=new JPanel();
  radioPanel.setLayout(new BoxLayout(radioPanel,BoxLayout.X_AXIS));
  group=new ButtonGroup();
 // radioPanel.add(new JLabel(localize("color")));
  //
  button1=new MyRadioButton(localize("fgrColor"),fgrChooser);
  group.add(button1);
  radioPanel.add(button1);
  ///
  button=new MyRadioButton(localize("fillColor"),fillChooser);
  group.add(button);
  radioPanel.add(button);
  ///
  button=new MyRadioButton(localize("bkgrColor"),bkgrChooser);
  group.add(button);
  radioPanel.add(button);
  ///
  gui.setLayout(new BoxLayout(gui,BoxLayout.Y_AXIS));
  gui.add(radioPanel,"North");
  gui.add(chooserPanel,"Center");
  ///
  button1.setSelected(true); //this shall add the JColorChooser, so add radioPanel should be done first
 }

 protected void createPlugs(){ //29Jun2000
  colors=new PaletteColorChange(getESlateHandle(),Color.black,Color.white,Color.red); //26Apr1999: changed getComponent() to getHandle() //19May1999: changed getHandle to getESlateHandle
  try{
   //Plug plug=new SingleOutputPlug(this.getESlateHandle(), localize("pin"),Color.gray,Class.forName("gr.cti.eslate.sharedObject.PaletteColorChange"),colors); //26Apr1999: changed getComponent() to getHandle() //19May1999: changed getHandle to getESlateHandle
   SharedObjectPlug plug=new SingleOutputPlug(this.getESlateHandle(), null, localize("pin"),Color.gray,Class.forName("gr.cti.eslate.sharedObject.PaletteColorChange"),colors); //26Apr1999: changed getComponent() to getHandle() //19May1999: changed getHandle to getESlateHandle
   plug.setNameLocaleIndependent("Χρώματα");
   addPlug(plug);
   /**/ //not needed? - we are listeners all the time cause we made the sharedObject//
   plug.addDisconnectionListener(new DisconnectionListener(){
     public void handleDisconnectionEvent(DisconnectionEvent e){
        colors.removeSharedObjectChangedListener(Palette.this);
     }
    });
   /**/
   plug.addConnectionListener(new ConnectionListener(){
    public void handleConnectionEvent(ConnectionEvent e){ //24-2-1999: changed the deprecated Pin.getMyComponent() to Pin.getComponent()
     //special case: registering as listeners of our own shared object (otherwise ESlate does that for us automatically)
     colors.addSharedObjectChangedListener(Palette.this);
     SharedObjectEvent soe=new SharedObjectEvent(colors,PaletteColorChange.NEW_FGR); //22May1999: using new SO constructor
     ((SharedObjectPlug)e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe); //26Apr1999: changed getComponent() to getHandle()
     soe=new SharedObjectEvent(colors,PaletteColorChange.NEW_BKGR); //22May1999: using new SO constructor
     ((SharedObjectPlug)e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe); //26Apr1999: changed getComponent() to getHandle()
     soe=new SharedObjectEvent(colors,PaletteColorChange.NEW_FILL); //22May1999: using new SO constructor
     ((SharedObjectPlug)e.getPlug()).getSharedObjectListener().handleSharedObjectEvent(soe); //26Apr1999: changed getComponent() to getHandle()
    }
   });
   ///
  }catch(Exception e){System.out.println(e.getMessage()+"\nError creating the PaletteChange pin");} //14-7-1998: in case some class is missing
 }

 public synchronized void handleSharedObjectEvent(SharedObjectEvent soe){
  PaletteColorChange Colors=(PaletteColorChange)(soe.getSharedObject());
  switch (soe.type){
 /*
   case PaletteColorChange.NEW_FGR:
    fgrChooser.setColor(Colors.get_fgrColor());
    break;
   case PaletteColorChange.NEW_BKGR:
    bkgrChooser.setColor(Colors.get_bkgrColor());
    break;
   case PaletteColorChange.NEW_FILL:
    fillChooser.setColor(Colors.get_fillColor());
    break;
 */
   case PaletteColorChange.NEW_COLOR: //Set selected color of currently visible color chooser
    System.out.println("New Color: "+Colors.get_color());
    ((JColorChooser)chooserPanel.getComponent(0)).setColor(Colors.get_color());
    //this must be firing (through the color pin) an event that a color changed
    break;
  }
 }

//AVAKEEO-END//

  /**
   * Save the component's state.
   * @param	out	The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput out) throws IOException //26Apr1999: from old ESlate's saveState
  {
     try{
       ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
       fieldMap.put("Fgr", fgrChooser.getColor());
       fieldMap.put("Bkgr", bkgrChooser.getColor());
       fieldMap.put("Fill", fillChooser.getColor());
       //if (getBorder() != null) {
            try{
                BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
                fieldMap.put("BorderDescriptor", bd);
            }catch(Throwable thr){
                thr.printStackTrace();
            }
       // }
        out.writeObject(fieldMap);
        out.flush();
      }catch(Exception e){throw new IOException(e+"\nError saving Palette state");} //26Apr1999: now throwind an IOException, instead of printing a message to the error stream

/*   try{
    oo.writeObject(fgrChooser.getColor());
    oo.writeObject(bkgrChooser.getColor());
    oo.writeObject(fillChooser.getColor());
   }catch(Exception e){throw new IOException(e+"\nError saving Palette state");} //26Apr1999: now throwind an IOException, instead of printing a message to the error stream*/
  }

  /**
   * Load the component's state.
   * @param	in	The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException //26Apr1999: from old ESlate's loadState
  {
      Object firstObj = in.readObject();
      if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
      // oldreadExternal
          try{
              fgrChooser.setColor((Color)firstObj);
              bkgrChooser.setColor((Color)in.readObject());
              fillChooser.setColor((Color)in.readObject());
          }catch(Exception e){throw new IOException(e+"\nError loading Palette state");} //26Apr1999: now throwind an IOException, instead of printing a message to the error stream
      }else{
          //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
          StorageStructure fieldMap = (StorageStructure) firstObj;


          fgrChooser.setColor((Color)fieldMap.get("Fgr"));
          bkgrChooser.setColor((Color)fieldMap.get("Bkgr"));
          fillChooser.setColor((Color)fieldMap.get("Fill"));
          BorderDescriptor bd = (BorderDescriptor)fieldMap.get("BorderDescriptor");
         //   if (bd != null){
                try{
                    setBorder(bd.getBorder());
                }catch(Throwable thr){}
         //   }
        }



   /*try{
    fgrChooser.setColor((Color)oi.readObject());
    bkgrChooser.setColor((Color)oi.readObject());
    fillChooser.setColor((Color)oi.readObject());
   }catch(Exception e){throw new IOException(e+"\nError loading Palette state");} //26Apr1999: now throwind an IOException, instead of printing a message to the error stream*/
  }

//AsPalette-START// --- //28-7-1998

 //foregroundColor property//
 public Color getForegroundColor(){return fgrChooser.getColor();}
 public void setForegroundColor(Color c){fgrChooser.setColor(c);} //this will fire an event through the color pin

 //backgroundColor property//
 public Color getBackgroundColor(){return bkgrChooser.getColor();}
 public void setBackgroundColor(Color c){bkgrChooser.setColor(c);} //this will fire an event through the color pin

 //fillColor property//
 public Color getFillColor(){return fillChooser.getColor();}
 public void setFillColor(Color c){fillChooser.setColor(c);} //this will fire an event through the color pin

//AsPalette-END//

 ButtonGroup group;
 MyRadioButton button1,button;
 JPanel gui,radioPanel,chooserPanel;
 JColorChooser fgrChooser,bkgrChooser,fillChooser; //28-7-1998

/*
 public void destroy(){ //removed: added to ESlate (since its ver 1.14)
  super.destroy();
  try{
   for (Enumeration e = getPins().elements(); e.hasMoreElements() ;) removePin((Pin)e.nextElement()); //remove all pins
  }catch(NoSuchPinException e){}
 }
*/

////////////////

 public class MyRadioButton
  extends JRadioButton
  implements ChangeListener
 {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  JComponent x;

  public MyRadioButton(String caption,JComponent x){
   super(caption);
   this.x=x;
   addChangeListener(this);
  }

  public void stateChanged(ChangeEvent e){
   if (isSelected()) chooserPanel.add(x); else chooserPanel.remove(x);
   chooserPanel.validate();
   chooserPanel.repaint();
  }

 }

////////////////

  public class MyJColorChooser extends JColorChooser
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    int change;

    public MyJColorChooser(int changeToDo,Color c)
    {
      super();
      change=changeToDo;
      this.setColor(c);
      setPreviewPanel(new JPanel());

      getSelectionModel().addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          Color c=getColor();
          switch (change) {
            case PaletteColorChange.NEW_FGR:
              colors.set_fgrColor(c);
              break;
            case PaletteColorChange.NEW_BKGR:
              colors.set_bkgrColor(c);
              break;
            case PaletteColorChange.NEW_FILL:
              colors.set_fillColor(c);
              break;
          }
        }
      });
    }
  }

/////////////////////

  public static void main(String[] args) {
   JComponentFrame.startJComponent("gr.cti.eslate.palette.Palette",localize("title"),new String[]{}); //26Apr1999: bug-fix: was instantiating the Turtle component instead of the Palette
   JComponentFrame.startJComponents(args,args); //27-3-1999: start any additional components requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(430,300);} //14May1999: made wider&higher //19May1999: made higher //29Jun2000: set to a lower height since the preview panel has been removed

}
