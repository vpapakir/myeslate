/*
?.? -  8Jul1998 - have added CallSO and CommandSO pins (Slider almost done - missing some GUI)
    - 14Jul1998 - now focus lost at edit boxes is same as pressing Enter
    - 25Jul1998 - now using GridBagLayout manager for the content pane
                  focus lost at edit boxes now should now simulate an action OK
    - 27Jul1998 - removed size params from VarsScrollPane constructor
                - added help file setting
    - 27Aug1998 - changed println(ex.getMessage()... to println(ex...
    - 28Aug1998 - now new "val" sets "From=val/2,To=val*2,Step=1"
    - 31Aug1998 - removed support for Logo<->Slider double connection (caused problems with line selection in canvas when Logo uses short buffer)

        ...

6.2 - 24Feb1999 - corrected About dialogs
6.3 - 25Feb1999 - now has VectorSO input&output pins for X&Y range at 2D slider (ranges of slider controls for the respective axis)
                - when some Axis has no assigned slider, the output vector doesn't send 0 for it, but sends the vector's previous value
                - was sending value twice to output vector in some cases, should be fixed now
6.4 - 27Feb1999 - fixed VarPanel.setValue... was using JSlider.getMinimum()&JSlider.getMaximum() instead of ValueSlider.minValue, ValueSlider.maxValue
                - this project was using "8859_7" encoding, changed to "Default"
6.5 -  1Mar1999 - now when an axis selection is changed the appropriate from&to points are transmitted through the from&to pins
6.6 - 27Mar1999 - now can start as an application and also load frames with additional applets specified at the command line
    - 15Apr1999 - internal source change (FocusAdapter instead of FocusListener for FieldsListener)
6.7 - 14May1999 - fixed bug: selecting an axis doesn't change the from&to values to 0 any more
                - made Slider's default size wider (from 400pixels to 500: else has problems with layout when in 2D)
7.0 - 18May1999 - moving to E-Slate II
    - 19May1999 - continued moving to ESlateII
                - now subclassing my ESlateApplet class insted of OldESlateComponent
                - fixed resource bundles, now using statically Slider's resource bundle, was using Canvas's before constructor was called, then used Slider's (so the frame's title when Slider run as an application wrote "Canvas Component")
7.1 -  2Jun1999 - not an applet anymore
7.2 - 11Jun1999 - SliderApplet asks Slider for its preferred size
7.3 -  6Jul1999 - added a 1D-2D mode toggle button
8.0 -  3Aug1999 - moved to package gr.cti.eslate
8.1 - 19Aug1999 - fix: start Slider class instead of SliderApplet class when Slider class is run as an application
                - now Logo<->Slider loop connection mode works OK! (almost, hangs some times)
8.2 - 31Aug1999 - now the Vector pins are added at startup and are kept around all the time (removed "2D" button)
                - now 2D mode is switched depending on the "value" VectorData pin being connected (either input or output)
                - commented out some debug messages displayed when connecting the "call notification" and "call execution" pins
8.3 - 27Nov1999 - added support for the E-Slate info dialog
8.4 - 12Feb2000 - to keep compatibility with old data, overriden readExternal/writeExternal to do nothing!
8.5 - 03Apr2000 - implementing the Externalizable interface (to state that this component isn't serializable)
                - telling Slider's ancestor, BaseComponent, not to write(read) any data during externalization(internalization)
8.6 - 30Jun2000 - added beanInfo and color and black&white icons
*/

//TO DO: vector should be sent only when some slider with an assigned axis changes, not any slider

package gr.cti.eslate.slider;

/*
  focus lost same as enter... is this good? Now to change from/to/step must generate 3 events!!!
  maybe some option in HTML... if set then add focus listeners!!!
*/

//some double execs happen in Slider-Logo double connection. Why?
//isOurCommand didn't work?

//isOurCommand should be removed: would cause first selection
//in TurtlePage to be ignored if slider had moved before...
//should check if the same compo is connected to both our pins
//and only then check for isOurCommand
//or use Kriton's cycle avoidance mechanism???

//don't use enableEvents code: cause ArrayIndexOutOfBounds at BoxLayout

//Problem: min/max/step don't allow editing any more when
//loaded new call: seems to send focus to LOGO?
//try new Call via procedural way... to see if call loaded from Slider proc does the same
//first time don't use demo... to see if first come will be editalbe

import virtuoso.logo.*;
import gr.cti.utils.*;

//AVAKEEO-START//
import gr.cti.eslate.base.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.utils.*; //JNumberField was moved to this package
import gr.cti.eslate.logo.*;
import gr.cti.eslate.birbilis.BaseComponent; //2Jun1999
//AVAKEEO-END//

import com.helplets.awt.PercentLayout;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import java.io.*;

/* 28Aug2000: StorageVersion = 1
              ComponentVersion = 1.0.1
*/


/**
 * @version     2.0.1, 23-Jan-2008
 * @author      George Birbilis
 * @author      Angeliki Oikonomou
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class Slider extends BaseComponent
                    implements SharedObjectListener,
                               ChangeListener, //listens for slider changes
                               java.io.Externalizable //03Apr2000: added this, to eliminate the possibility that Java tries to serialize this component

//TO DO: add support to keep cur Slider GUI settings... (save GUI, then use some func to reconstruct the LogoProcDescription from the saved GUI, or remove use a serializable/externalizable LogoProcDescription)
{

// public final static String VERSION="8.6"; //30Jun2000
 public final static String VERSION="2.0.1"; //28Aug2000
 //public static final String STORAGE_VERSION = "2";
 public static final int STORAGE_VERSION = 2;

 static final long serialVersionUID = 03042000L; //03Apr2000: added serial-version, so that new versions load OK ???

 protected boolean isOldStylePersisting(){
  return true; //03Apr2000: keep compatibility with old persistence data
 }

 // N.Drossos --> 24Aug2000: stores an empty fieldMap to keep compatibility with the new storage mechanism
 public void writeExternal(ObjectOutput out) throws IOException
  {
//      System.out.println("slider WriteExternal start");
      ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
      //if (getBorder() != null) {
          try{
              BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
              fieldMap.put("BorderDescriptor", bd);
          }catch(Throwable thr){
              thr.printStackTrace();
          }
      //}
      out.writeObject(fieldMap);
      out.flush();
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException //19May1999: from old ESlate's loadState
  {
//      System.out.println("slider ReadExternal start");
      Object firstObj = in.readObject();
      if (!ESlateFieldMap2.class.isAssignableFrom(firstObj.getClass())) {
      // oldreadExternal
//          System.out.println("slider empty ReadExternal");
      }else{
          //ESlateFieldMap fieldMap = (ESlateFieldMap) firstObj;
          StorageStructure fieldMap = (StorageStructure) firstObj;

          BorderDescriptor bd = (BorderDescriptor)fieldMap.get("BorderDescriptor");
          //if (bd != null){
              try{
                  setBorder(bd.getBorder());
              }catch(Throwable thr){}
          //}
//          System.out.println("slider new ReadExternal end");
      }
  }


/*
 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {} //to keep compatibility with old data, overriden this to do nothing!
 public void writeExternal(ObjectOutput out) throws IOException {} //to keep compatibility with old data, overriden this to do nothing!

*/

 /**
  * Returns Copyright information.
  * @return	The Copyright information.
  */
 public ESlateInfo getInfo()
 {return new ESlateInfo(Res.localize("title")+VERSION, Res.localizeArray("info"));}

public Slider(){
 super();

 setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));
 procNameLabel=new JLabel();

 try{
  getESlateHandle().setUniqueComponentName(Res.localize("Slider")); //30-8-1998 //14-1-1999: moved here from constructor, cause it showed ...  //18May1999: ESlateII
 }catch(RenamingForbiddenException e){} //19May1999: ESlateII

//14-1-1999:moved to init()// setUniqueComponentName(localize("Slider")); //30-8-1998
  //4-9-1998:removed try-catch, since ESlate1.28 has no problem with calling this in the constructor

 procLabel=new JLabel(Res.localize("Procedure")+":");
 varLabel=new JLabel(Res.localize("Var"));
 fromLabel=new JLabel(Res.localize("From"));
 toLabel=new JLabel(Res.localize("To"));
 stepLabel=new JLabel(Res.localize("Step"));

 Font font=procNameLabel.getFont();
 procNameLabel.setFont(new Font(font.getName(),Font.BOLD,font.getSize()));  //12-7-1998: proc name in Bold

//LOGOCallSO input pin// (from Canvas or Logo)
 try {
  //Pin pin=new SingleInputPin(getESlateHandle(),Res.localize("turtlePage"),new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),this); //18May1999: ESlateII
  Plug plug=new SingleInputPlug(getESlateHandle(),Res.getRBundle(), "turtlePage",new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),this); //18May1999: ESlateII
  plug.setNameLocaleIndependent("Ενημέρωση για LOGO εντολές");
  addPlug(plug);
  plug.addConnectionListener(new ConnectionListener(){ //28-10-1998
    public void handleConnectionEvent(ConnectionEvent coe){
     //System.out.println("LOGOCallSO connected");
     loopLogo=(coe.getPlug().getHandle().getComponent()) instanceof gr.cti.eslate.logo.Logo; //??? if Logo.class missing? //18May1999: ESlateII
     //System.out.println("Other end is "+(loopLogo?"Logo":"not Logo"));
    }
   });
  plug.addDisconnectionListener(new DisconnectionListener(){
    public void handleDisconnectionEvent(DisconnectionEvent e){
    //System.out.println("LOGOCallSO disconnected");
     newCall(null); //always clear if this pin is disconnected
     loopLogo=false; //28-10-1998
    }
   });
 }catch(Exception ex){System.out.println(ex+"\nError creating LogoCallSO input pin");} //14-7-1998: in case the class is missing

 //LOGOCommandSO output pin// (to LOGO)
 try {
  //Pin pin=new SingleOutputPin(getESlateHandle(),Res.localize("logopin"),new Color(160,80,170),Class.forName("gr.cti.eslate.sharedObject.LogoCommandSO"),commandSO); //19May1999: ESlateII
  Plug plug=new SingleOutputPlug(getESlateHandle(),Res.getRBundle(), "logopin",new Color(160,80,170),Class.forName("gr.cti.eslate.sharedObject.LogoCommandSO"),commandSO); //19May1999: ESlateII
  plug.setNameLocaleIndependent("Εντολές LOGO");
  addPlug(plug);
  plug.addConnectionListener(new ConnectionListener(){ //28-10-1998
    public void handleConnectionEvent(ConnectionEvent coe2){
     //System.out.println("LOGOCommandSO connected");
     loopLogo2=(coe2.getPlug().getHandle().getComponent()) instanceof gr.cti.eslate.logo.Logo; //???if Logo.class missing? //18May1999: ESlateII
     //System.out.println("Other end is "+(loopLogo2?"Logo":"not Logo"));
    }
   });
  plug.addDisconnectionListener(new DisconnectionListener(){
    public void handleDisconnectionEvent(DisconnectionEvent e){
     //System.out.println("LOGOCommandSO disconnected");
     newCall(null); //always clear if this pin is disconnected
     loopLogo2=false; //28-10-1998
    }
   });
 }catch(Exception ex){System.out.println(ex+"\nError creating LogoCommandSO ouput pin");} //14-7-1998: in case the class is missing

 makeVectorPins(); //31Aug1999: add the Vector pins from the start (keep them always around)

 ///// GUI /////

   Container c=SLIDER;

   //25-7-1998// --- Gridbag layout start
    GridBagLayout layout=new GridBagLayout();
    c.setLayout(layout);
    GridBagConstraints b=new GridBagConstraints();
    b.gridx=0; //place in first column (only one column exists)
    b.gridy=GridBagConstraints.RELATIVE; //place below previous one
    b.gridwidth=GridBagConstraints.REMAINDER; //take up all the row (1 column)

    b.fill=GridBagConstraints.HORIZONTAL; //expand area for component only in x direction
    b.weighty=0; //don't expand component in y direction
    b.weightx=1; //expand component in x direction
    layout.setConstraints(c.add(procPanel=new ProcPanel()),b); //1st sub-panel
    layout.setConstraints(c.add(new FieldNamesPanel(varLabel,fromLabel,toLabel,stepLabel)),b); //2nd sub-panel

    b.gridheight=GridBagConstraints.REMAINDER; //take up all remaining y space
    b.fill=GridBagConstraints.BOTH; //expand area for component in both x&y directions
    b.weightx=1; //expand component in x direction
    b.weighty=1; //expand component in y direction
    layout.setConstraints(c.add(vspane=new VarsScrollPane()),b); //3rd (last) sub-panel
    //25-7-1998// --- Gridbag layout end

    newCall(null); //!!!

}

////////////////////////////////////////////////////////////////

/**
 * Handle an event from some SO
 * @param	soe	The event
 */
public synchronized void handleSharedObjectEvent(SharedObjectEvent soe){

 SharedObject so=soe.getSharedObject();

 if (so instanceof LogoCallSO){ //29-9-1998

  LogoCallSO callSO=(LogoCallSO)(so);
  switch (soe.type){
   case LogoCallSO.NEW_CALL:
    //newCall should handle null passed to it by clearing its display (means no procedure at Slider)
    ProcedureCall call=callSO.get_call(); //28-10-1998

    if (!(loopLogo&&loopLogo2)) newCall(call);
    else //19Aug1999: now Logo<->Slider loop connection mode works OK!
     if (call!=null) //when in Logo<->Slider loop mode, ignore the null calls that Logo sends to notify that a proc finished executing (and thus don't have the slider's display cleared)
      if(currentCall!=null && currentCall.procDef.equals(call.procDef)) newArgs(call.procArgs); else newCall(call); //19Aug1999 (Java uses short circuit if-evaluation, so we're OK and no null-point-exceptions are thrown)
   break;
  }

 }

 else if (so instanceof VectorData){ //29-9-1998
/*
  VectorData newVect = (VectorData)(soe.getSharedObject()); //get input vector
  Vector path = soe.getPath();
  double hor=newVect.getX();
  double ver=newVect.getY();
  varsPanel.newXY(hor,ver);
  vect.setVectorData(hor, ver, path); //copy data to output vector
*/

    VectorData newVect = (VectorData)(soe.getSharedObject()); //get input vector
    //Component c=newVect.getComponent();
    double hor=newVect.getX();
    double ver=newVect.getY();

    //System.out.println(c+" "+vComponent+xComponent+" "+yComponent);
    //System.out.println(newVect+" "+vSO+xSO+" "+ySO);

    if (newVect==vSO){
     //System.out.println("Slider:handleSO got current point value");
     varsPanel.newXY(hor,ver); //risking to send twice, since newXY might??? update vect...
     vect.setVectorData(hor, ver);//, soe.getPath()); //copy data to output vector
    }
    else
    if (newVect==fromSO){
     //System.out.println("Slider:handleSO got xRange value");
     fromPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output xrange
     //varsPanel.newXrange(hor,ver); //vect.x=left range edge, vect.y=right range edge
     /////varsPanel.newFromPoint(hor,ver);
    }
    else
    if (newVect==toSO){
     //System.out.println("Slider:handleSO got yRange value");
     toPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output yrange
     //varsPanel.newYrange(hor,ver); //vect.x=left range edge, vect.y=right range edge
     /////varsPanel.newToPoint(hor,ver);
    }
    else {System.out.println("Internal error at handleSharedObjectEvent!");} //@#%@#%#@$% (won't be inited with data at the first connection, cause our handleSO will be called by the other end before our connection event has been called)

 }

}

////////////////////////////////////////////////////////////////

 boolean dontSend; //15-12-1998
 boolean someSliderChanged; //15-12-1998

 public synchronized void stateChanged(ChangeEvent e){ //!!!synchronized!!!
  if (dontSend) {someSliderChanged=true;return;} //15-12-1998: if changing many sliders in a batch, wait to send one message for all if some slider changed (mark that some slider changed to check at the batch's end)
  ValueSlider slider=(ValueSlider)e.getSource();
  if (!slider.changed){ //if value has changed, then do action anyway (even when dragging the slider)... //14May1999: note: changed flag is used, cause JSlider tends to give redundant change events that would lead to redundant Logo routine reexecutions when dragging the slider
   //...couldn't just check valueIsAdjusting cause when dragging the slider we wouldn't be sending commands to Logo (would send one when releasing the knob)
   //...what's more we ignore the event only when valueIsAdjusting is true and the value is the same as before (some Swing bug?), but
   //...not when value is the same and valueIsAdjusting is false, cause this occurs when user clicks on the knob (to repeat a command without altering a slider value)
   return; //9-11-1998: !!! to avoid double repaints when clicking or dragging slider
   //9-11-1998:don't use this... always just do a "return"//if (slider.getValueIsAdjusting()) return;
  } //notification is called twice: once before slider.setValue and once after it
  else slider.changed=false; //to avoid some false "Change" events from JSlider (needed!!! else double events occur some times cause false is left to true)
  if (is2D
   //&& slider.findVarPanel.seeIfhasAxisAssigned   //fix: need to call updateOutputVector only when a slider with some axis assigned to it changed and not when anyother slider changed
  ) varsPanel.updateOutputVector(); //25-2-1999: moved here from issueNewLogoCommand, cause it was causing value to be sent twice
  issueNewLogoCommand();
 }

////////////////////////////////////////////////////////////////

 public void issueNewLogoCommand(){ //15-12-1998
  String s=getAsLogoCommand();
//  System.out.println("Slider is sending new command:"+s);
  //31-8-1998:removed Logo<->Slider double connectionsupport// isOurCommand=true; //don't handle the LogoCall notification caused by the command we shall send to LOGO
  commandSO.setCommand(s,topLevelClock);
  //The LogoCommandSO passes along the topLevelclock
  //under which the command shall be executed!!!

  //25-2-1999: next line was removed cause was sending twice - also we need to call updateOutputVector only when a slider with some axis assigned to it changed and not when anyother slider changed
//  if (is2D) varsPanel.updateOutputVector(); //29-9-1998 //9-10-1998: if (!is2D) then axisSelector will be null and this will throw a NullPointerException

  Thread.yield(); //16-7-1998: give Logo console&interpeter threads some time to do processing
 }

///////////////////////////////////////////////////////////////////////

 boolean is2D; //9-10-1998: default=false

 //GUI objects//
 JLabel procLabel,
        procNameLabel, //others not initialized here cause need Greek (MessagesBundle m must be intantiated)
        trackLabel,
        gridLabel,
        varLabel,
        fromLabel,
        toLabel,
        stepLabel;
 ProcPanel procPanel;
 VarsPanel varsPanel;
 VarsScrollPane vspane;

 private JComponent SLIDER;

//level 0///////////////////////////////////////////
 public JComponent getContent(){
  if (SLIDER==null){
   SLIDER=new JPanel();
  }
  return SLIDER;
 }

 public void makeVectorPins(){ //6Jul1999: moved code from init() here //31Aug1999: now VectorPins are added once to the Slider's pin menu and stay around in both 1D&2D mode (in fact 2D mode entry depends on the "value" vector pin being connected as either an input or an output)
/* //31Aug1999
  Pin[] p=getPins();
  try{for(int i=0;i<p.length;i++)
   if(p[i].getSharedObject() instanceof VectorData) removePin(p[i]);}catch(NoSuchPinException e){}

//Vector input-output pin (from Vector or ???)// --- //29-9-1998
  if (is2D){ //9-10-1998
*/

/*
    try {
      Pin pin = new SingleInputMultipleOutputPin(
		  this, localize("Vector"), Color.green,
		  gr.cti.eslate.sharedObject.VectorData.class, vect);
      addPin(pin);
      pin.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e)
	{
	  // make sure connected component gets value of vector when connected
	  if (e.getType() == Pin.OUTPUT_CONNECTION) {
	    SharedObjectEvent soe =
	      new SharedObjectEvent(Slider.this, vect);
	    ((SharedObjectListener)(e.getPin().getComponent())).handleSharedObjectEvent(soe);
	  }
	}
      });
    } catch (InvalidPinParametersException e) {System.err.println(e.getMessage());
    } catch (PinExistsException e) {System.err.println(e.getMessage());}
*/

 //VECTOR PIN//
    try {
     Plug vPlug = new SingleInputMultipleOutputPlug(
     // getESlateHandle(), Res.localize("Vector"), Color.green,
     getESlateHandle(), Res.getRBundle(), "Vector", Color.green,
		  gr.cti.eslate.sharedObject.VectorData.class, vect, this); //19May1999: ESlateII
     addPlug(vPlug);
     vPlug.setNameLocaleIndependent("Διάνυσμα");
     vPlug.addConnectionListener(new ConnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleConnectionEvent(ConnectionEvent e){
          //System.out.println("Planar: Current point pin connected");
          set2D(true); //31Aug1999: regardless whether the "value" pin was connected as an input or an output, enter 2D mode
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION){
              vSO=((SharedObjectPlug)e.getPlug()).getSharedObject(); //this works only for single-input pin !$#!#^@@ (in the future have SOlisteners for each pin)
              double hor=((VectorData)vSO).getX();
              double ver=((VectorData)vSO).getY();

              varsPanel.newXY(hor,ver); //risking to send twice, since newXY might??? update vect...
              vect.setVectorData(hor, ver);//, soe.getPath()); //copy data to output vector
          }
          /*else
       	  if (type == Plug.OUTPUT_CONNECTION) { // make sure connected component gets value of vector when connected
    	     SharedObjectEvent soe = new SharedObjectEvent(vect); //22May1999: using new SO constructor
    	     ((SharedObjectListener)(e.getPlug().getHandle().getComponent())).handleSharedObjectEvent(soe); //19May1999: ESlateII
      	  }*/
   	     }
        } );
     vPlug.addDisconnectionListener(new DisconnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleDisconnectionEvent(DisconnectionEvent e){
          //System.out.println("Planar: Current point pin disconnected");

          Plug plug=e.getPlug();
          Plug[] providers=plug.getProviders();
          Plug[] dependents=plug.getDependents();
          if ( (providers==null || providers.length==0) && //check for null, just in case the Pin implementation changes in the future to return null instead of [] when we don't have any connected providers/dependents
               (dependents==null || dependents.length==0) )
           set2D(false); //31Aug1999: when all inputs and outputs of "value" pin get disconnected, exit 2D mode

          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION) vSO=null; //free the remote SO reference when input gets disconnected
   	     }
        } );
    } catch (InvalidPlugParametersException e) {System.err.println(e.getMessage());
    } catch (PlugExistsException e) {System.err.println(e.getMessage());}

 //FROM PIN//
    try {
      Plug fromPlug = new SingleInputMultipleOutputPlug(
		  //getESlateHandle(), Res.localize("From"), Color.green,
      getESlateHandle(), Res.getRBundle(), "From", Color.green,
		  gr.cti.eslate.sharedObject.VectorData.class, fromPoint, this); //19May1999: ESlateII
      fromPlug.setNameLocaleIndependent("Από");
      addPlug(fromPlug);
      fromPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e){
          //System.out.println("Planar: from pin connected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION){
              fromSO=((SharedObjectPlug)e.getPlug()).getSharedObject(); //this works only for single-input pin !$#!#^@@ (in the future have SOlisteners for each pin)
              double hor=((VectorData)vSO).getX();
              double ver=((VectorData)vSO).getY();
              fromPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output xrange
          }
          /*else
       	  if (type == Plug.OUTPUT_CONNECTION) { // make sure connected component gets value of vector when connected
    	     SharedObjectEvent soe = new SharedObjectEvent(fromPoint); //22May1999: using new SO constructor
    	     ((SharedObjectListener)(e.getPlug().getHandle().getComponent())).handleSharedObjectEvent(soe); //19May1999: ESlateII
      	  }*/
   	     }
        } );
      fromPlug.addDisconnectionListener(new DisconnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleDisconnectionEvent(DisconnectionEvent e){
          //System.out.println("Planar: from pin disconnected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION) fromSO=null; //free the remote SO reference when input gets disconnected
   	     }
        } );
    } catch (InvalidPlugParametersException e) {System.err.println(e.getMessage());
    } catch (PlugExistsException e) {System.err.println(e.getMessage());}

 //TO PIN//
    try {
      Plug toPlug = new SingleInputMultipleOutputPlug(
		  //getESlateHandle(), Res.localize("To"), Color.green,
      getESlateHandle(), Res.getRBundle(), "To", Color.green,
		  gr.cti.eslate.sharedObject.VectorData.class, toPoint, this); //19May1999: ESlateII
      toPlug.setNameLocaleIndependent("Μέχρι");
      addPlug(toPlug);
      toPlug.addConnectionListener(new ConnectionListener() {
        public void handleConnectionEvent(ConnectionEvent e){
          //System.out.println("Planar: to pin connected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION){
            toSO=((SharedObjectPlug)e.getPlug()).getSharedObject(); //this works only for single-input pin !$#!#^@@ (in the future have SOlisteners for each pin)
            double hor=((VectorData)vSO).getX();
            double ver=((VectorData)vSO).getY();
            toPoint.setVectorData(hor, ver);//, soe.getPath()); //copy data to output yrange
          }
          /*else
       	  if (type == Plug.OUTPUT_CONNECTION) { // make sure connected component gets value of vector when connected
    	     SharedObjectEvent soe = new SharedObjectEvent(toPoint); //22May1999: using new SO constructor
    	     ((SharedObjectListener)(e.getPlug().getHandle().getComponent())).handleSharedObjectEvent(soe); //19May1999: ESlateII
      	  }*/
   	     }
        } );
      toPlug.addDisconnectionListener(new DisconnectionListener() { //on double connections, the first connected as a provider to the other is the one who gets to keep its vector's value
        public void handleDisconnectionEvent(DisconnectionEvent e){
          //System.out.println("Planar: to pin disconnected");
          int type=e.getType();
       	  if (type == Plug.INPUT_CONNECTION) toSO=null; //free the remote SO reference when input gets disconnected
   	     }
        } );
    } catch (InvalidPlugParametersException e) {System.err.println(e.getMessage());
    } catch (PlugExistsException e) {System.err.println(e.getMessage());}

  //31Aug1999//  } //end of "if(is2D)"

 }

 public void set2D(boolean is2D){ //6Jul1999
  this.is2D=is2D;
  //31Aug1999: now vector pins are around all the time// makeVectorPins();
  if(currentCall!=null) newCall(currentCall); //redraw display to show axis selectors in 2D mode (or unshow when returning to 1D mode)
 }

///////////////

 public void demo(){ //DEMO// //6Jul1999: made seperate proc
    procNameLabel.setText("mystery");
    varsPanel.add(new VarPanel("alpha",10)); //varsPanel is autoCreated by VarsScrollPane
    varsPanel.add(new VarPanel("beta",1000));
    varsPanel.add(new VarPanel("c",10));
    varsPanel.add(new VarPanel("d",10));
    varsPanel.add(new VarPanel("epsilon",4));
    varsPanel.add(new VarPanel("f",1));
    varsPanel.add(new VarPanel("g",4));
    varsPanel.add(new VarPanel("h",1));
 }

////////////////////////////

 ProcedureCall currentCall; //6Jul1999: keeping currently visible call, to reuse (pass to newCall(...)) when changing between 1D-2D modes

 public void newArgs(LogoList procArgs){ //19Aug1999
  //System.out.println("Slider::newArgs");
  try{ //5-2-1999: when executing "FD 10 BOX 40" on the same line from Logo and then selecting the BOX shape from Canvas, we get some invalid(?) proc def, so we're catching the exception

   int count=procArgs.length();
   double[] args=new double[count];

   for(int i=0;i<count;i++)
    args[i]=(Double.valueOf(procArgs.pickInPlace(i).toString())).doubleValue(); //toString accuracy problems???

   varsPanel.setValues(args);

  try{
   Slider.this.validate(); Slider.this.repaint();
   procPanel.invalidate(); procPanel.doLayout(); procPanel.repaint(); //27-8-1998
 //  varsPanel.validate(); varsPanel.repaint();
  }catch(Exception e){System.err.println("Exception while redrawing");} //happens some time???

  }catch(Exception e){System.err.println("'FD 10 BOX 40' like statements on the same line not yet supported");}

 }

 ///////////////

 public void newCall(ProcedureCall c){
  //System.out.println("Slider::newCall");
  currentCall=c;
  procNameLabel.setText(""); //reset proc name
  varsPanel.removeAll(); //remove all vars

  try{ //5-2-1999: when executing "FD 10 BOX 40" on the same line from Logo and then selecting the BOX shape from Canvas, we get some invalid(?) proc def, so we're catching the exception

  if (c!=null) {
   //System.out.println("Call context info is coming with the new call...");

   //moved these two in here till find solution: double way Slider-Logo has null sent to Slider (>Logo-Canvas-Slider> wants that)
 //  procNameLabel.setText(""); //reset proc name
 //  varsPanel.removeAll(); //remove all vars
   //!!!caused problem with erasing lines in Canvas, so removed for now

   topLevelClock=c.time;
   //
   //System.out.println("newCall: Setting proc name to "+c.procDef.getName().toString());
   procNameLabel.setText(c.procDef.getName().toString());
   LogoList l=c.procDef.getParams();
   double value;
   for(int i=0;i<l.length();i++){ //6-7-1998: bug fixed, had <= instead of < and that was throwing an Array Out of bounds Exception
    value=(Double.valueOf(c.procArgs.pickInPlace(i).toString())).doubleValue(); //toString accuracy problems???
    varsPanel.add(new VarPanel(l.pickInPlace(i).toString(), value)); //28-8-1998: only value as param
    //vp.v.mySetDoubleValue(value); //14May1999: removed, value setting to the slider is done at VarPanel's constructor
   }
  }
  try{
   Slider.this.validate(); Slider.this.repaint();
   procPanel.invalidate(); procPanel.doLayout(); procPanel.repaint(); //27-8-1998
 //  varsPanel.validate(); varsPanel.repaint();
  }catch(Exception e){System.err.println("Exception while redrawing");} //happens some time???

  }catch(Exception e){System.err.println("'FD 10 BOX 40' like statements on the same line not yet supported");}

 }

 ////////////////////////////

 public String getAsLogoCommand(){
  return procNameLabel.getText()+varsPanel.getValues();
 }

// level 1 ///////////////////////////////////////////
 class ProcPanel extends JPanel {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  //private JToggleButton btn2D;
  int height;

  public ProcPanel() {
   super();

// setBorder(new BevelBorder(BevelBorder.LOWERED)); //16-7-1998: removed cause bevel of panel under it is raised (showed a white strip between them)
   setLayout(new PercentLayout()/*FlowLayout(FlowLayout.LEFT)*/); //16-7-1998: FlowLayout caused proc label not to show up some times

   add("left=1, right=19", procLabel);
   add("left=20",procNameLabel);

/* //31Aug1999: removed, now 2D mode is switched depending on the "value" VectorData pin being connected (either input or output)
   btn2D=new JToggleButton(Res.localize("2D")); //6Jul1999: added a 1D-2D mode toggle button
   btn2D.addChangeListener(new ChangeListener(){
    public void stateChanged(ChangeEvent e){
     set2D(btn2D.isSelected());
    }
   });
   add("right=100",btn2D);
*/
  }

 }

/////////////////////////////////////////////

 class VarsScrollPane extends JScrollPane {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  public VarsScrollPane() {
   super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER); //use never for horz (might not fit if size is too small, but needed for relative layout)
   setVarsPanel(varsPanel=new VarsPanel()); //varsPanel is created here
  }
  public void setVarsPanel(VarsPanel v){setViewportView(v);} //14-10-1998: in case we ever need to show some new VarsPanel

 } //END-CLASS(VarsScrollPane)


// level 2 ///////////////////////////////////////////

class VarsPanel extends JPanel {
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  public VarsPanel() {
   super();
   setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
  }

  public void unSelectAxis(JComponent notThis,int axis){ //9-10-1998
   VarPanel vp;
   for(int i=getComponentCount();i-->0;){ //25-2-1999: optimized loop checking (going bottom->up)
    vp=(VarPanel)getComponent(i);
    if(vp!=notThis && vp.axisSelector.getSelectedAxis()==axis){
     vp.axisSelector.toggleAxis(axis);
     return; //only one at maximum could have had that axis selected, found and changed it, so returning
    }
   }
  }

  public void updateOutputVector(){ //29-9-1998: called when some slider changes
   double x=vect.getX(), //25-2-1999: now initializing to the current x value, in case X axis is not assigned to some slider (was sending 0 for x)
          y=vect.getY(); //25-2-1999: now initializing to the current y value, in case Y axis is not assigned to some slider (was sending 0 for y)
   boolean updateX=false,
           updateY=false; //25-2-1999: initializing to false, and setting only if the respective axis is assigned to some slider
   VarPanel vp;
   for(int i=getComponentCount();i-->0;){ //25-2-1999: optimized loop
    vp=(VarPanel)getComponent(i);
    switch(vp.axisSelector.getSelectedAxis()){
     case AxisSelector2D.X_AXIS: x=vp.v.myGetDoubleValue(); updateX=true; break; //9-10-1998: put a "break" here
     case AxisSelector2D.Y_AXIS: y=vp.v.myGetDoubleValue(); updateY=true; break; //9-10-1998: put a "break" here
    }
   }
   if(updateX || updateY) vect.setVectorData(x,y); //25-2-1999: if no axis is selected then don't send any output value
  } //should only update when one of the selected sliders change and not every time... (maybe call directly from stateChanged when a slider has an assigned axis)

  public void sendFromAndToPoints(){ //1Mar1999: set the fromPoint and toPoint SOs to the most current Range (they'll also output it through any Pin they use)
   double fx=fromPoint.getX(), //initializing to the current values, in case X axis is not assigned to some slider
          fy=fromPoint.getY(); //initializing to the current value, in case Y axis is not assigned to some slider
   double tx=toPoint.getX(), //initializing to the current values, in case X axis is not assigned to some slider
          ty=toPoint.getY(); //initializing to the current value, in case Y axis is not assigned to some slider
   boolean updateX=false,
           updateY=false; //initializing to false, and setting only if the respective axis is assigned to some slider
   VarPanel vp;
   for(int i=getComponentCount();i-->0;){ //optimal loop
    vp=(VarPanel)getComponent(i);
    switch(vp.axisSelector.getSelectedAxis()){
     case AxisSelector2D.X_AXIS: fx=vp.getFrom(); tx=vp.getTo(); updateX=true; break; //9-10-1998: put a "break" here
     case AxisSelector2D.Y_AXIS: fy=vp.getFrom(); ty=vp.getTo(); updateY=true; break; //9-10-1998: put a "break" here
    }
   }
   if(updateX || updateY) {
    fromPoint.setVectorData(fx,fy);
    toPoint.setVectorData(tx,ty); //if no axis is selected then don't send any output value
   }
  } //should only update when at least one slider is selected

  public void newXY(double x,double y){
// System.out.println("Sending "+x+" "+y);
   dontSend=true; //15-12-1998
   for(int i=getComponentCount();i-->0;) //25-2-1999: optimized loop checking (going bottom->up)
    ((VarPanel)getComponent(i)).newXY(x,y);
   dontSend=false; //15-12-1998
   if (someSliderChanged) //send ONLY if AT LEAST one slider changed!
    {someSliderChanged=false; issueNewLogoCommand();} //15-12-1998
  }

/*
  public void newXrange(double fromX,double toX){ //25-2-1999
   for(int i=getComponentCount();i-->0;) //25-2-1999: optimized loop checking (going bottom->up)
    ((VarPanel)getComponent(i)).newXrange(fromX,toX);
  }

  public void newYrange(double fromY,double toY){ //25-2-1999
   for(int i=getComponentCount();i-->0;) //25-2-1999: optimized loop checking (going bottom->up)
    ((VarPanel)getComponent(i)).newYrange(fromY,toY);
  }
*/
  public void newFromPoint(double x,double y){ //25-2-1999
   for(int i=getComponentCount();i-->0;) //25-2-1999: optimized loop checking (going bottom->up)
    ((VarPanel)getComponent(i)).newFromPoint(x,y);
  }

  public void newToPoint(double x,double y){ //25-2-1999
   for(int i=getComponentCount();i-->0;) //25-2-1999: optimized loop checking (going bottom->up)
    ((VarPanel)getComponent(i)).newToPoint(x,y);
  }

  public String getValues(){ //values list with " " between them and a " " at start
   Component[] vp=getComponents();
   String s=new String();
   int count=vp.length; //for speed
   for(int i=0;i<count;i++)
    s=s+" "+((VarPanel)vp[i]).v.getValueAsText(); //19Aug1999: only VarPanel components added to VarPanel (assume this for speed gain, to avoid "instanceof" checks)
   return s;
  }

  public void setValues(double[] values){ //19Aug1999
   Component[] vp=getComponents();
   //String s=new String();
   int count=vp.length; //for speed

   if(count!=values.length){
    System.err.println("Internal error at Slider::setValues!");
    return;
   }

   for(int i=0;i<count;i++)
    ((VarPanel)vp[i]).setValue(values[i]); //only VarPanel components added to VarPanel (assume this for speed gain)
  }

 } //END-CLASS(VarsPanel)

// level 3 ///////////////////////////////////////////
 class VarPanel extends JPanel
  implements PropertyChangeListener //29-9-1998: for AxisSelector
 {
   /**
    * Serialization version.
    */
   final static long serialVersionUID = 1L;
  private JNumberField f,t,s;
  public AxisSelector2D axisSelector;
  public ValueSlider v;

  public void newXY(double x,double y){ //29-9-1998 (a separate newX & newY proc wouldn't be better, cause we do get both X&Y from vector input and loop through all varpanels to check if they are X or Y axis handlers... doing newX then newY in the same loop is the same as doing newXY [even faster cause we got one proc call])
   switch(axisSelector.getSelectedAxis()){
    case AxisSelector2D.X_AXIS: setValue(x); break;
    case AxisSelector2D.Y_AXIS: setValue(y); break;
   }
  }

  public void newXY_noAutoRange(double x,double y){ //14May1999: needed so that when selecting an axis at 2D mode, not to auto-adjust the existing ranges of the slider
   switch(axisSelector.getSelectedAxis()){
    case AxisSelector2D.X_AXIS: v.mySetDoubleValue(x); break; //this sets the slider's value without using "setValue", so that from&to don't get changed
    case AxisSelector2D.Y_AXIS: v.mySetDoubleValue(y); break; //this sets the slider's value without using "setValue", so that from&to don't get changed
   }
  }

/*
  public void newXrange(double fromX,double toX){ //25-2-1999
   if(axisSelector.getSelectedAxis()==AxisSelector2D.X_AXIS)
    setRange(fromX,toX);
  }

  public void newYrange(double fromY,double toY){ //25-2-1999
   if(axisSelector.getSelectedAxis()==AxisSelector2D.Y_AXIS)
    setRange(fromY,toY);
  }
*/

  public void newFromPoint(double x,double y){ //25-2-1999
   switch(axisSelector.getSelectedAxis()){
    case AxisSelector2D.X_AXIS: setFrom(x);
    case AxisSelector2D.Y_AXIS: setFrom(y);
   }
  }

  public void newToPoint(double x,double y){ //25-2-1999
   switch(axisSelector.getSelectedAxis()){
    case AxisSelector2D.X_AXIS: setTo(x);
    case AxisSelector2D.Y_AXIS: setTo(y);
   }
  }

  public void setValue(double value){ //29-9-1998
   if(v.minValue<=value && v.maxValue>=value) //if value in range... //26-2-1999: fixed to use minValue,maxValue (instead of the getMinimum,getMaximum which return the JSlider's internal integer bounds - ValueSlider does a special integer<->double mapping)
    v.mySetDoubleValue(value);
   else{
    f.setNumber((int)(value/2));
    v.setMinValueMaxStep((int)(value/2),value,value*2,1); //28-8-1998
    t.setText(NumberUtil.num2str(value*2));
    s.setNumber(1);
   }
  }

/*
  public void setRange(double from,double to){ //25-2-1999
   double value=v.myGetDoubleValue();
   if (value<from || value>to) //if reversed from,to?
    value=(from+to)/2;
   f.setNumber(from);
   t.setNumber(to);
   v.setMinValueMaxStep(from,value,to,1); //why change the step to 1? (and not use minValue,maxValue - should set up mapping too)
  }
*/

  public void setFrom(double from){ //25-2-1999
    f.setNumber(from);
    v.setMinimum(from); //this is overriden by ValueSlider to accept doubles and map them to ints
  }

  public void setTo(double to){ //25-2-1999
   t.setNumber(to);
   v.setMaximum(to); //this is overriden by ValueSlider to accept doubles and map them to ints
  }

  public double getFrom(){ //1Mar1999
   return f.getdouble();
  }

  public double getTo(){ //1Mar1999
   return t.getdouble();
  }

  public VarPanel(String VarName,double value) { //28-8-1998: added value as param
   super();
   //
   JLabel l=new JLabel(VarName);
   l.setHorizontalAlignment(SwingConstants.RIGHT); //12-7-1998: Right Align the var name

   Font font=l.getFont();
   l.setFont(new Font(font.getName(),Font.BOLD,font.getSize())); //12-7-1998: var name in Bold

//   FontMetrics metrics=Toolkit.getDefaultToolkit().getFontMetrics(font);
//   Dimension d=new Dimension(metrics.stringWidth("XXXXX"),metrics.getHeight());
//   l.setSize(d); l.setPreferredSize(d); l.setMaximumSize(d);

   //15-1-1999: there was some problem caused by Visual Cafe having placed INIT_CONTROLS blocks in several components!!!
   setLayout(new PercentLayout()); //!!!

   if (Slider.this.is2D){ //9-10-1998
    add("left=1",axisSelector=new AxisSelector2D()); //29-9-1998
    add("right=14",l); //right align
   }else add("left=1 right=14",l); //right align //left not 0: too close to border

   double from=(int)(value/2); //14May1999: using vars, so that following code is more readable and maintainable
   double to=value*2;
   double step=1;

   add("left=15, right=30"    ,f      =new JNumberField(gr.cti.utils.NumberUtil.num2str(from))); //28-8-1998
   add("left=31, right=69",v=new ValueSlider(from,value,to,step)); //28-8-1998
   add("left=69, right=83",t=new JNumberField(gr.cti.utils.NumberUtil.num2str(to))); //28-8-1998
   add("left=85, right=99",s=new JNumberField(gr.cti.utils.NumberUtil.num2str(step))); //11-9-1998: changed from 100 to 99

//   f.setColumns(4);
//   t.setColumns(4);
//   s.setColumns(4);
   f.setValidChars(JNumberField.FLOAT_NUMBER);
   t.setValidChars(JNumberField.FLOAT_NUMBER);
   s.setValidChars(JNumberField.FLOAT_DIGITS); //'-' not allowed
//   f.setEditable(true); t.setEditable(true); s.setEditable(true); //???
   //
   FieldsListener listener=new FieldsListener();
   f.addActionListener(listener);
   t.addActionListener(listener);
   s.addActionListener(listener);
   //
   f.addFocusListener(listener); //14-7-1998
   t.addFocusListener(listener); //14-7-1998
   s.addFocusListener(listener); //14-7-1998
   //
   v.addChangeListener(Slider.this);
   if (is2D) axisSelector.addPropertyChangeListener(this); //29-9-1998 //9-10-1998: if (!is2D) then the axisSelector var is null
  }

   public void propertyChange(PropertyChangeEvent evt){ //29-9-1998: called on some change of AxisSelector
    varsPanel.unSelectAxis(this,axisSelector.getSelectedAxis()); //9-10-1998: unselect at other VarPanels any same axis as the one just selected at this VarPanel
    varsPanel.sendFromAndToPoints(); //if we were X and changed to Y will have unselected the old one that was Y (so there will be only X - us)... So need to update both X&Y fields at fromPoint&toPoint
    //14May1999: when selecting an axis, don't change the current value! //newXY_noAutoRange(vect.getX(),vect.getY()); //assuming the event came from the axisSelector, make the new vector out of the selected x&y axis and send it //14May1999: bug-fix: now using newXY_noAutoRange instead of newXY, cause the old one made the ranges to change (getXY was setting the slider's value via "setValue" and caused the from/to values to be set accordingly, now "newXY_noAutoRange" sets the slider's value without calling our "setValue" method [which also updates the from&to values])

    v.changed=true; //14May1999: must do before stateChanged, else the event will be ignored (changed flag is used, cause JSlider tends to give redundant change events that would lead to redundant Logo routine reexecutions when dragging the slider)
    Slider.this.stateChanged(new ChangeEvent(v)); //14May1999: notify planar on the new value (?needed only if some point is selected and we're playing with its value... otherwise we're always adding new points to the planar when sending values via the slider)
   }

  ////

/*
  public VectorData getRange(){ //25-2-1999
   switch(axisSelector.getSelectedAxis()){
    case AxisSelector2D.X_AXIS: return xrange;
    case AxisSelector2D.Y_AXIS: return yrange;
   }
   return null;
  }
*/

  class FieldsListener extends FocusAdapter //14-7-1998: implementing FocusListener //15-4-1999: extending FocusAdapter, since we are not implementing FocusGained
                       implements ActionListener
  {
   //ActionListener//
   public void actionPerformed(ActionEvent e){
    double newval;
    JNumberField field=(JNumberField)e.getSource();
    try{
     newval=field.getdouble(); //an exception might be raised here...
     if (field==f) {
      v.setMinimum(newval); //this is overriden by ValueSlider to accept doubles and map them to ints
      if (is2D) { //25-2-1999
       //VectorData range=getRange();
       //if (range!=null) range.setVectorData(newval,range.getY()); //set range's FROM element
       switch(axisSelector.getSelectedAxis()){ //update FROM point
        case AxisSelector2D.X_AXIS: fromPoint.setVectorData(newval,fromPoint.getY()); break;
        case AxisSelector2D.Y_AXIS: fromPoint.setVectorData(fromPoint.getX(),newval); break;
       }
      }
     }else
     if (field==t) {
      v.setMaximum(newval); //this is overriden by ValueSlider to accept doubles and map them to ints
      if (is2D) { //25-2-1999
       //VectorData range=getRange();
       //if (range!=null) range.setVectorData(range.getX(),newval); //set range's TO element
       switch(axisSelector.getSelectedAxis()){ //update TO point
        case AxisSelector2D.X_AXIS: toPoint.setVectorData(newval,toPoint.getY()); break;
        case AxisSelector2D.Y_AXIS: toPoint.setVectorData(toPoint.getX(),newval); break;
       }
      }
     }else
     if (field==s) v.setStep(newval);
    }catch(Exception ex){ //...and caught here
      if (field==f) f.setText(NumberUtil.num2str(v.minValue)); else
      if (field==t) t.setText(NumberUtil.num2str(v.maxValue)); else
      if (field==s) s.setText(NumberUtil.num2str(v.stepValue)); }
   }
   //FocusListener// --- //14-7-98
   public void focusLost(FocusEvent event){
    //System.out.println("Field lost focus");
    actionPerformed(new ActionEvent(event.getSource(),0,null)); //25-7-1998: fixed bug: need to pass an event (not null) cause we are calling e.getSource() at actionEvent()
    //doing the above cause we want focus lost to do same as enter pressed
   }

  }

 } //END-CLASS(VarPanel)

//////////////////////////////////

  private VectorData vect=new VectorData(getESlateHandle()); //19May1999: ESlateII
  private VectorData fromPoint=new VectorData(getESlateHandle()); //24-2-1999 //25-2-1999: renamed to fromPoint
  private VectorData toPoint=new VectorData(getESlateHandle()); //24-2-1999 //25-2-1999: renamed to toPoint
  private SharedObject vSO,fromSO,toSO; //25-2-1999: used to detect from where a sharedObjectChanged event is coming

  LogoCommandSO commandSO=new LogoCommandSO(getESlateHandle()); //contains the command sent to LOGO
  //31-8-1998// boolean isOurCommand=false; //init to false so that we shall handle LogoCall notifications

  int topLevelClock=-1; //!!!
  boolean loopLogo=false; //28-10-1998
  boolean loopLogo2=false; //28-10-1998

/////////////////////////////////////////////////

  public static void main(String[] args) {
   JComponentFrame.startJComponent("gr.cti.eslate.slider.Slider",Res.localize("title"),new String[]{}); //19Aug1999: fixed to start Slider instead of SliderApplet
   JComponentFrame.startJComponents(args,args); //27-3-1999: start any additional applets requested by the user
  }

  public Dimension getPreferredSize(){return new Dimension(500,200);} //14May1999: made wider (from 400 to 500)

}
