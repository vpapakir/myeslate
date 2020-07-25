/*
?.?  - 14Aug1998 - changed all println(ex.getMessage()... to println(ex...
 
6.2  - 24Feb1999 - fixed-bug: "Pause" button shows Res.localized message again
                 - corrected About dialogs
                 - made Pause button same height as Run & Stop buttons
6.3  - 27Feb1999 - this project was using "8859_7" encoding, changed to "Default"
6.4  - 27Mar1999 - using JAppletFrame to be able to run as an application
                 - getAppletPath now doesn't throw null pointer exception when Canvas is run as an application
                 - HTML parameter parsing code now doesn't throw a null pointer exception when Canvas is run as an application
                 - now can load extra applets specified at the command line when running as an application
     - 28Mar1999 - internal fix: was using some not needed Java2 methods and couldn't run under Java1.1
     -  2Apr1999 - some changes to MyMachine at Virtuoso, so that when passed a null String as a Primitive Group name to load, not to throw a null pointer exceptions, but Virtuoso's SetupException
     -  8Apr1999 - internal fixes to ComponentPrimitives primitive group implementation
                 - when a text area loses focus it hides its caret
6.5  - 15Apr1999 - now Logo uses a Command Line edit field at its bottom, where new commands can be executed with the ENTER key and where commands sent from the LogoCommand Pin are displayed, can also run from there using ENTER or the Run button on the toolbar/menubar
                 - added a new "Try to unfreeze" menuitem at the "Machine" menu to feed the parser with lots of closing parentheses in case it is frozen waiting at some open parentheses that the user forgot to close
                 - added F1 key for execution from the selected text area (not the command line text field, CR/ENTER is needed there) cause Java on Macs and Unix doesn't seem to grab the CTRL keypress
                 - added F2 key for showing Logo environment info on the Java console
6.6  - 18May1999 - the Logo command 'EACH [PRINT NAME]' should be working again now
7.0  - 18May1999 - moving to E-Slate II
7.01 - 19May1999 - now descending from ESlateApplet (SO's should be inited OK)
7.02 - 22May1999 - now outgoing SOs pass the component as the event source, not its handle
7.1  - 25May1999 - not listening for component notifications any more, but for primitive group notifications instead
                 - listening for Logo moving to other microworld, adjusted scripting mechanism to work with many microworlds
7.2  - 26May1999 - now when resizing does a "trick" to make Swing's JSpitPane behave OK... also keeps last fraction of the textPanes' areas as is and doesn't set to 6/10 (this is done at start)
                 - separated scripting mechanism from the Logo component
                 - Logo has its own primitive group to define its special handling of the "EDALL" primitive (moved from the ComponentPrimitives PrimitiveGroup which belongs to the scripting mechanism)
7.21 - 1Jun1999  - removed a no-more needed inner class (MyComponentStateChangedListener)
7.3  - 2Jun1999  - not an applet anymore
7.4  - 4Jun1999  - now runs in a Frame as a Component and not an Applet when gr.cti.eslate.logo.Logo is run as an application
7.5  - 18Jun1999 - added Font name selector at toolbar
     - 21Jun1999 - added Font selector bar (name, size, style) at logo toolbar
                 - HTML parameter handling code moved to LogoApplet
     - 22Jun1999 - resizing should have the SplitPane keep the last view ratio of its children
     - 23Jun1999 - enabled ThreadPrimitives, also loading List&Sound primitives
     - 22Jul1999 - moved getAppletInfo() method to LogoApplet class
     - 23Jul1999 - now using bigger and "Monospaced" font at Logo's InputArea
                 - made the font toolbar non-floatable
                 - placed the font toolbar separately under the buttons' toolbar
     - 26Jul1999 - added "executeCommand(String)" to Logo so that having a Logo component instance, one can invoke Logo commands
8.0  -  3Aug1999 - moved to package gr.cti.eslate
8.1  - 19Aug1999 - "patch-fixed" the divider location bug: now ResizableScrollPane from birbUtils package starts at 200pixels for divider and then keeps the ratio set by the user (via dragging the divider)
8.2  -  3Sep1999 - monospaced font isn't the default input textarea font anymore [Helvetica is used now] cause it doesn't show accented greek chars
                 - split pane divider is now placed correctly (used decompiled patch-code from older Logo implementation) when loading a saved Logo component
8.3  - 15Sep1999 - Logo now shows the divider at 6/10 when first created (showed at bottom)
     - 20Sep1999 - added run/pause/stop etc. verbs
     - 28Sep1999 - renamed setSuspended/isSuspended to setPaused/isPaused
                 - added "Logo.EXECUTE" scripting command (calls <Logo>.executeLogoCommand)
                 - added togglePausedState verb (to call this from containers' Logo enging using for example ASK "Logo_5 [DO "togglePausedState])
8.4  -  8Oct1999 - added Logo.unpause() verb and Logo.STOP/Logo.PAUSE/Logo.UNPAUSE/Logo.TOGGLEPAUSE scripting commands
8.5  - 14Oct1999 - now using E-Slate's current locale [instead of using Locale.getDefault()]
                 - moved localization code out of main Logo class, to gr.cti.eslate.logo.Res class
8.6  - 15Oct1999 - Logo now has storable "MenuBarVisible", "ToolBarVisible" and "HasFontSelector" properties
8.7  - 19Oct1999 - Logo now has serialVersionUID
8.8  -  4Dec1999 - added a status bar
8.9  -  3Apr2000 - telling Logo's ancestor, BaseComponent, not to write(read) any data during externalization(internalization)
9.0  -  4Apr2000 - added <Exception>.printStackTrace() calls to readExternal/writeExternal methods' try/catch blocks
                 - removed "loadImageIcon" method from Logo class, now using the already existing "loadImageIcon" static method from the gr.cti.eslate.logo.Res class
9.1  -  4Apr2000 - made the Run/Pause/Stop image icons have a transparent background instead of gray
9.2  - 10May2000 - moved status bar from Logo class to LogoGUI class
                 - now showing status bar messages when switching from executing to idle state
                 - added "execQueueMaxSize" storable property for setting the execution queue length
9.3  - 23May2000 - keeping loading compatibility with saved-data from previous beta versions of BaseComponent & Logo
9.4  - 31May2000 - now the logic of loading the default primitives is not at Logo but at LogoEngine
                 - Logo now has beanInfo and an icon
9.5  - 29Jun2000 - removed startup of "ReflectionPrimitives" and "ListPrimitives" (started by the LogoEngine)
                 - removed startup of "SoundPrimitives" (which caused QuickTime for Java to load!)
9.6  - 30Jun2000 - now the LogoGUI class doesn't throw a LanguageException at closing
                 - removed Logo messages regarding "starting wathcing the microworld for new component additions" and "finding preexisting components in the microworld"
 
1.0.7 - 2Apr2001 - Change machine plug from single output to multiple output
*/

//need MACHINE_VARIABLES???
//need MACHINE_PROCEDURES???
//or maybe a DUMP_STATE to show the whole state to the bottom TextArea?

//What happens when clock goes to 0? Can this happen after some time?

package gr.cti.eslate.logo;

import gr.cti.eslate.birbilis.BaseComponent;  //18May1999: ESlateII //2Jun1999: not subclassing JApplet any more
import gr.cti.eslate.base.*;
import gr.cti.eslate.base.MicroworldChangedListener;
import gr.cti.eslate.base.MicroworldChangedEvent;
import gr.cti.eslate.protocol.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.sharedObject.*;  //LogoCommandSO
import gr.cti.eslate.scripting.logo.ComponentPrimitives;

import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.logo.Res;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;

import gr.cti.utils.*;

import virtuoso.logo.*;  //LOGO//

/* 28Aug2000: StorageVersion = 1
             ComponentVersion = 1.0.1
*/


/**
 * Logo component.
 *
 * @version     2.0.6, 23-Jan-2008
 * @author      G. Birbilis
 * @author      G. Tsironis
 * @author      N. Drossos
 * @author      K. Kyrimis
 */
public class Logo extends BaseComponent //2Jun1999: not an Applet any more
      implements LogoPrimitiveGroupHandler,
      PrimitiveGroupAddedListener,  //25May1999
      MicroworldChangedListener,  //25May1999
      SharedObjectListener,
      gr.cti.eslate.scripting.LogoScriptable,  //25May1999
      java.io.Externalizable,
      gr.cti.eslate.scripting.AsLogoRunner,  //8Nov1999
      gr.cti.eslate.scripting.AsLogoNamesMemory //8Nov1999
      //gr.cti.eslate.scripting.AsLogoProceduresMemory //8Nov1999
{

  // final static String VERSION="9.6"; //30Jun2000
  final static String VERSION = "2.0.6";  //31May2001
  public static final int STORAGE_VERSION = 2;

  static final long serialVersionUID = 19101999L;  //19Oct1999: serial-version, so that new versions load OK

  // 23Jul1999: using bigger and "Monospaced" font at Logo's InputArea
  // 3Sep1999: Monospaced font not used for now [using the default JTextArea
  // font, Helvetica], cause Java doesn't show accented greek chars in this
  // font OK (shows small rectangles instead)
  // K.K.: reverted to monospaced/12
  final static String defaultFontName = "Monospaced";
  final static int defaultFontSize = 12;

  private final static Dimension preferredSize = new Dimension(400, 400);

  private Vector<String> availableFontNames;
  private JComboBox fontSelector;
  private JComboBox sizeSelector;
  private JToggleButton boldButton;
  private JToggleButton italicButton;

  private boolean lineNumbersVisibleInProgramArea;
  private boolean lineNumbersVisibleInOutputArea;

  private boolean updatingFontToolBar;

  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  PerformanceTimer saveTimer;
  /**
   * Timer which measures the time required for the construction of the
   * component.
   */
  PerformanceTimer constructorTimer;
  /**
   * Timer which measures the time required for initializing the E-Slate
   * aspect of the component.
   */
  PerformanceTimer initESlateAspectTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  // Field map keys.
  private final static String INPUT_AREA = "InputArea";
  private final static String OUTPUT_AREA = "OutputArea";
  private final static String BUFFER = "Buffer";
  private final static String LEVEL_CLOCK = "LevelClock";
  private final static String INPUT_AREA_FONT = "Font";
  private final static String OUTPUT_AREA_FONT = "Font2";
  private final static String MENU_BAR_VISIBLE = "MenuBarVisible";
  private final static String TOOL_BAR_VISIBLE = "ToolBarVisible";
  private final static String FONT_SELECTOR = "FontSelector";
  private final static String EXEC_QUEUE_MAX_SIZE = "ExecQueueMaxSize";
  private final static String BORDER_DESCRIPTOR = "BorderDescriptor";
  private final static String LINE_NUMBERS_VISIBLE_IN_PROGRAM_AREA =
    "LineNumbersVisibleInProgramArea";
  private final static String LINE_NUMBERS_VISIBLE_IN_OUTPUT_AREA =
    "LineNumbersVisibleInOutputArea";
  private final static String STATUS_BAR_VISIBLE = "StatusBarVisible";
  private final static String MINIMUM_SIZE = "minimumSize";
  private final static String MAXIMUM_SIZE = "maximumSIze";
  private final static String PREFERRED_SIZE = "preferredSize";

  protected boolean isOldStylePersisting()
  {
    return true;  //03Apr2000: keep compatibility with old persistence data
  }

  // LogoScriptable ////////////////////////////////////////////


  public String[] getSupportedPrimitiveGroups()
  {
    String[] s = {"gr.cti.eslate.scripting.logo.LogoPrimitives"};  //26May1999
    return s;
  }

  // AsLogoRunner /////////////////////

  public void queueLogoStatement(String s)
  { //asynchronous, returns immediately, queues statement in a FIFO to execute later
    executeLogoCommand(s);  //temp
  }


  public void doLogoStatement(String s)
  {    //blocking, either places command in FIFO and waits for its turn to execute and return, or spawns another thread to run the command
    executeLogoCommand(s);  //temp
  }


  public String doLogoOperation(String s)
  {  //blocking, returns result as a LogoObject.toString()
    executeLogoCommand(s);  //temp
    return "";
  }

  // AsLogoNamesMemory ////////////////    //8Nov1999

  public String[] getLogoNames()
  { //get all the named values' names
    LogoList nameList = (LogoList)logoGUI.theLOGOmachine.getNameList();
    int count = nameList.length();
    String[] names = new String[count];
    for (int i = count; --i > 0; ) names[i] = nameList.pickInPlace(i).toString();
    return names;
  }

  public String getLogoValue(String name)
  { //get a value as a String, given its name
    return
      logoGUI.theLOGOmachine.resolveName(new CaselessString(name)).toString();
  }

  public MyMachine getLogoMachine()
  {
    return logoGUI.theLOGOmachine;
  }

  /**
   * Set a "named value" given its name and its value (as its String
   * representation).
   */
  public void setLogoValue(String name, String value)
  {
    // Works for lists or need to parse for bounding [] and call
    //  new LogoList(value) then?
    logoGUI.theLOGOmachine.makeName(
      new CaselessString(name), new LogoWord(value)
    );
  }

  // VERBS ////////////////////////////


  public void run()
  { //20Sep1999
    Action(MACHINE_RUN);
  }

  public void stop()
  { //20Sep1999
    Action(MACHINE_STOP);
  }

  public void pause()
  { //20Sep1999
    setPaused(true);
  }

  public void unpause()
  { //8Oct1999
    setPaused(false);
  }

  public void togglePausedState()
  { //28Sep1999: useful if we want to have a container's event handler written in Logo that says ASK "LogoXcomponent [DO "togglePausedState]
    setPaused(!isPaused());
  }

  public void reset()
  { //20Sep1999
    Action(MACHINE_RESET);
  }

  public void clear()
  { //20Sep1999
    Action(FILE_NEW);
  }

  ////////////////////////////////////////

  public void action(int actionID)
  { //20Sep1999
    Action(actionID);
  }

  //////

  public void executeLogoCommand(String s)
  { //26Jul1999: call from outside to invoke a Logo command
    logoGUI.executeCommand(s);
  }

  public void editAll()
  { //31-8-1998 //SCRIPTING//
    logoGUI.executeCommand("POALL");  //first print all
    logoGUI.executeCommand("ERALL");  //then erase them
  }

  //////////////////////////////


  public void setPaused(boolean paused)
  {
    action_Pause.setSelected(paused);
  }
  public boolean isPaused()
  {
    return action_Pause.isSelected();
  }

  //////////////////////////////

  public void LoadPrimitiveGroup(PrimitiveGroup p)
  { //LOGO//
    try {
      logoGUI.theLOGOmachine.installPrimitives(p);
    } catch (Exception e) {
      e.printStackTrace(); System.out.println("Error: Couldn't load the primitives");
    }
    ;  //26May1999: printing-stack-trace
  }

  public void UnLoadPrimitiveGroup(PrimitiveGroup p)
  { //LOGO//
    try {
      logoGUI.theLOGOmachine.unInstallPrimitives(p);
    } catch (Exception e) {
      e.printStackTrace(); System.out.println("Error: Couldn't unload the primitives");
    }
    ;  //26May1999: printing-stack-trace
  }

  ////////////////////////

  /**
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  public ESlateInfo getInfo()
  {
    return new ESlateInfo(Res.localize("title") + VERSION, Res.localizeArray("info"));
  }

  LogoCallSO LOGOcall = new LogoCallSO(getESlateHandle());  //don't forget to instantiate //18May1999: ESlateII
  MachineSO machineSO = new MachineSO(getESlateHandle());

  private void createPins()
  { //3Sep1999
    //LogoCallSO output pin//
    try {
      //Pin pin=new SingleOutputPin(getESlateHandle(),Res.localize("logocallpin"),new Color(190,90,50),Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"),LOGOcall); //18May1999: ESlateII
      Plug plug = new SingleOutputPlug(getESlateHandle(), Res.getRBundle(), "logocallpin", new Color(190, 90, 50), Class.forName("gr.cti.eslate.sharedObject.LogoCallSO"), LOGOcall);  //18May1999: ESlateII
      plug.setNameLocaleIndependent("Ενημέρωση για εντολές");

      addPlug(plug);
    } catch (Exception ex) {
      System.out.println(ex + "\nError creating LogoCallSO output pin");
    } //14-7-1998: in case the class is missing

    //LOGOCommandSO input pin// //31-8-1998: put this after LogoCallSO (better understanding when connecting Slider/Canvas/Logo)
    try {
      Plug plug = new SingleInputPlug(getESlateHandle(), Res.getRBundle(), "commandpin", new Color(160, 80, 170), Class.forName("gr.cti.eslate.sharedObject.LogoCommandSO"), this);  //18May1999: ESlateII
      plug.setNameLocaleIndependent("Εντολές/Μεταβολέας");

      addPlug(plug);
    } catch (Exception ex) {
      System.out.println(ex + "\nError creating LogoCommandSO input pin");
    } //14-7-1998: in case the class is missing

    try {
      Plug plug = new MultipleOutputPlug(getESlateHandle(), Res.getRBundle(), "machinePin", new Color(70, 0, 90), Class.forName("gr.cti.eslate.sharedObject.MachineSO"), machineSO);  //18May1999: ESlateII
      plug.setNameLocaleIndependent("Μηχανή LOGO");

      addPlug(plug);
    } catch (Exception ex) {
      System.out.println(ex + "\nError creating MachineSO output pin");
    } //14-7-1998: in case the class is missing

  }

  ////////////////////////////////////////////////////

  //MenuBarVisible property//


  public void setMenuBarVisible(boolean visible)
  { //15Oct1999
    if (menubar != null) menubar.setVisible(visible);
  }

  public boolean isMenuBarVisible()
  { //15Oct1999
    return (menubar != null) ? menubar.isVisible() : false;
  }

  //ToolBarVisible property//

  public void setToolBarVisible(boolean visible)
  { //15Oct1999
    if (toolbar != null) toolbar.setVisible(visible);
  }

  public boolean isToolBarVisible()
  { //15Oct1999
    return (toolbar != null) ? toolbar.isVisible() : false;
  }

  //StatusBarVisible property//

  /**
   * Specifies whether the status bar should be visible.
   * @param     visible True if yes, false if no.
   */
  public void setStatusBarVisible(boolean visible)
  {
    if (logoGUI != null) {
      logoGUI.setStatusBarVisible(visible);
    }
  }

  /**
   * Checks whether the status bar is visible.
   * @return    True if yes, false if no.
   */
  public boolean isStatusBarVisible()
  {
    if (logoGUI != null) {
      return logoGUI.isStatusBarVisible();
    }else{
      return false;
    }
  }

  //HasFontSelector property//

  public void setHasFontSelector(boolean visible)
  { //15Oct1999
    if (fontToolbar != null) fontToolbar.setVisible(visible);
  }

  public boolean getHasFontSelector()
  { //15Oct1999
    return (fontToolbar != null) ? fontToolbar.isVisible() : false;
  }

  //execQueueMaxSize property//

  public void setExecQueueMaxSize(int size)
  {
    logoGUI.setExecQueueMaxSize(size);
  }

  public int getExecQueueMaxSize()
  {
    return logoGUI.getExecQueueMaxSize();
  }

  ////////////////////////////////////////////////////

  public Logo()
  {
    super();
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(constructorTimer);

    setPreferredSize(Logo.preferredSize);

    setBorder(new NoTopOneLineBevelBorder(NoTopOneLineBevelBorder.RAISED));

    // Do this first of all (instantiate LOGOcall first).
    // This also starts the LOGO machine.
    logoGUI = new LogoGUI(LOGOcall);
    lineNumbersVisibleInProgramArea = logoGUI.inputArea.isLineNumbersVisible();
    lineNumbersVisibleInOutputArea = logoGUI.outputArea.isLineNumbersVisible();

    LOGO.setLayout(new BorderLayout());
    LOGO.add(createMenus(), BorderLayout.NORTH);
    LOGO.add(logoGUI, BorderLayout.CENTER);

    createPins();

    //LOAD THE TELL PRIMITIVE// --- //29-7-1998: this is needed for the querying of components' primitives (at Logo's loading) to work
    try {
      MyMachine machine = logoGUI.theLOGOmachine;
      machineSO.setMachine(machine);
      machine.componentPrimitives = new ComponentPrimitives(getESlateHandle().getESlateMicroworld());  //25May1999: making changes to scripting, so that it works when having many microworlds
      LoadPrimitiveGroup(machine.componentPrimitives);  //18May1999: ESlateII
    }
    catch (Throwable ex) {
      System.out.println("Couldn't load E-Slate's component primitives");
    } //20-8-1998 //8-8-1999: fixed a misspelling at the error message //9Jul1999: catch Errors too apart from Exceptions (Throwable is the parent of both)...

    getESlateHandle().addMicroworldChangedListener(this);  //25May1999: watch for Logo moving to other m/w
    startWatchingMicroworld();
    //31-8-1998: a LogoGUI must have been created first (to setup a LOGO machine)

    pm.stop(constructorTimer);
    pm.constructionEnded(this);
    pm.displayTime(constructorTimer, "", "ms");
  }


  public JComponent getContent()
  {
    if (LOGO == null) LOGO = new JPanel();
    return LOGO;
  }

  public void destroy()
  { //2-10-1998
    logoGUI.finish();
    MyMachine machine = logoGUI.theLOGOmachine;
    machineSO.setMachine(machine);
    machine.componentPrimitives.theESlateMicroworld = null;
    machine.componentPrimitives.tellingComponents = null;
    //super.destroy(); //11-1-1999: placed last, remove destroyCalled() checks
  }

  //////////////


  public void startWatchingMicroworld()
  { //31-8-1998
    //System.out.println("Logo is starting to watch the MicroWorld");

    ESlateMicroworld mw = getESlateHandle().getESlateMicroworld();
    if (mw == null) {
      return;
    }

    // 25May1999: set the machine to the new m/w
    logoGUI.theLOGOmachine.componentPrimitives.theESlateMicroworld = mw;

    //25May1999-removed-nowUsingPrimitiveGroupNotifications// getESlateHandle().getESlateMicroworld().addComponentStateChangedListener(new myComponentStateChangedAdapter()); //27-7-1998: Register as listener for component state events (a LogoGUI must have been created first) //18May1999: ESlateII
    mw.addPrimitiveGroupAddedListener(this);  //25May1999

    //28-7-1998: After registering as listener for initializing components
    //           ask also all the components that have already been started in the past
    //           Suppose we are loading Logo with a bunch of other components
    //           which are on the same HTML page: No init of any component has been
    //           called till all have been created (?), so Logo will listen to all of them (and itself)
    //           being initialized... Components existing previous to this incident should
    //           already have been initialized and be started...
    //           Problem might happen if some compos are stoped... maybe should register
    //           for Creation event... then get all components already created... however
    //           can't get that, can just get ones added in microworld, but don't know
    //           when Kriton gives notification for create... maybe he gives after adding to mw?
    //           ...most possibly...
    //This must be right after the addComponentStateChangedListener...
    try {
      ESlateHandle[] v = mw.getAllHandlesInHierarchy();
      for (int i = 0; i < v.length; i++) {
        ESlateHandle a = v[i];
        //System.out.println("Found a preexisting component named "+a.getComponentName());
        if (/*a.isActive() && //???*/ //3-9-1998:don't use isActive(), failing to ask components for their primitives
          a instanceof gr.cti.eslate.scripting.LogoScriptable)
          loadPrimitiveGroups(((gr.cti.eslate.scripting.LogoScriptable)a).getSupportedPrimitiveGroups());
      }
    } catch (Exception e) {
      System.err.println(e + "\nCouldn't ask existing components for any Logo primitives they might implement");
    }

    try { //27-3-1999: ask any ESlate-independent components registered for scripting, in case they need some PrimitiveGroups
      Object[] o = gr.cti.eslate.scripting.logo.ComponentPrimitives.scriptableObjects.getAllObjects();  //26Jul1999: changed to use the INameService's i/f getAllObjects method: maybe should use one descendent "Logo name service" that wraps both the ESlate naming and the global nameservice in it (an inner class of Logo that can get the current microworld from Logo at any time)
      for (int i = 0; i < o.length; i++) {
        Object a = o[i];
        //System.out.println("Found a preexisting scripting registered object named "+a.name);
        if (a instanceof gr.cti.eslate.scripting.LogoScriptable)
          loadPrimitiveGroups(((gr.cti.eslate.scripting.LogoScriptable)a).getSupportedPrimitiveGroups());
      }
    } catch (Exception e) {
      System.err.println(e + "\nCouldn't ask existing scriptable objects for any Logo primitives they might implement");
    }

  }

  ///////

  public void loadPrimitiveGroups(String[] p)
  { //28-7-1998
    logoGUI.theLOGOmachine.loadPrimitives(p);
  }

  //PrimitiveGroupAddedListener//
  public void primitiveGroupAdded(PrimitiveGroupAddedEvent e)
  { //25May1999
    //System.out.println("Logo: got PrimitiveGroupAddedEvent");
    //e.getSource() gives the handle that requested those prim groups
    loadPrimitiveGroups(e.getNames());
  }

  //MicroworldChangedListener//
  public void microworldChanged(MicroworldChangedEvent e)
  {
    //System.out.println("Logo: moving to new microworld");
    ESlateMicroworld oldMW = e.getOldMicroworld();
    if (oldMW != null) {
      oldMW.removePrimitiveGroupAddedListener(this);
    }
    //...add: remove all primitive groups loaded by ESlate components (since they were from the old microworld)
    startWatchingMicroworld();
  }

  /* //1Jun1999: removed, not using ComponentStateChangeEvents any more (merged with PrimitiveGroupAddedEvents)
   class myComponentStateChangedAdapter extends ComponentStateChangedAdapter{ //27-7-1998
    public void componentInitialized(ComponentStateChangedEvent e){ //28-7-1998: Check for components initializing
                //Kriton should name this ComponentInitializing
     //System.out.println("Logo: A component is initializing...");
     Object o=e.getSource();
     if(o instanceof gr.cti.eslate.scripting.LogoScriptable)
      loadPrimitiveGroups(((gr.cti.eslate.scripting.LogoScriptable)o).getSupportedPrimitiveGroups());
    }
   }
  */

  /////////////////////

  public synchronized void handleSharedObjectEvent(SharedObjectEvent soe)
  {
    LogoCommandSO commandSO = (LogoCommandSO)(soe.getSharedObject());
    // switch (soe.type){     //10May2000: since we only have one plug, removed "switch" statement for more speed when using the Slider-Logo plugging

    //  case LogoCommandSO.NEW_COMMAND:
    //System.out.println(commandSO.getCommand());
    logoGUI.executeCommand_Display(commandSO);
    //   break;
    // }
  }

  /**
   * Save the component's state.
   * @param     out     The stream where the state should be saved.
   */
  public void writeExternal(ObjectOutput out) throws IOException //18May1999: from old ESlate's saveState
  {
    //      System.out.println("write external start");

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    try {
      ESlateFieldMap2 fieldMap = new ESlateFieldMap2(STORAGE_VERSION);
      fieldMap.put(INPUT_AREA, logoGUI.inputArea.getText());
      fieldMap.put(OUTPUT_AREA, logoGUI.outputArea.getText());
      //SAVE_MACHINE_IN_SLT_START: 17-12-1998
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      IOStream ios = new IOStream(new BufferedWriter(new OutputStreamWriter(buffer)));
      logoGUI.theLOGOmachine.printout(ios, null, null, null);
      ios.close();
      // ByteArrayOutputStream is not serializable: so we get an extra third
      // memory copy of our data :-(
      fieldMap.put(BUFFER, buffer.toByteArray());
      //SAVE_MACHINE_IN_SLT_END: 17-12-1998
      //SAVE MACHINE's proc-executions' clock//
      fieldMap.put(LEVEL_CLOCK, logoGUI.theLOGOmachine.level1clock);
      fieldMap.put(INPUT_AREA_FONT, logoGUI.inputArea.getFont());
      fieldMap.put(OUTPUT_AREA_FONT, logoGUI.outputArea.getFont());

      fieldMap.put(MENU_BAR_VISIBLE, isMenuBarVisible());
      fieldMap.put(TOOL_BAR_VISIBLE, isToolBarVisible());
      fieldMap.put(FONT_SELECTOR, getHasFontSelector());
      fieldMap.put(EXEC_QUEUE_MAX_SIZE, getExecQueueMaxSize());
      //if (getBorder() != null) {
      try {
        BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
        fieldMap.put(BORDER_DESCRIPTOR, bd);
      } catch (Throwable thr) {
        thr.printStackTrace();
      }
      fieldMap.put(
        LINE_NUMBERS_VISIBLE_IN_PROGRAM_AREA,
        isLineNumbersVisibleInProgramArea()
      );
      fieldMap.put(
        LINE_NUMBERS_VISIBLE_IN_OUTPUT_AREA, isLineNumbersVisibleInOutputArea()
      );
      fieldMap.put(STATUS_BAR_VISIBLE, isStatusBarVisible());
      fieldMap.put(MINIMUM_SIZE, getMinimumSize());
      fieldMap.put(MAXIMUM_SIZE, getMaximumSize());
      fieldMap.put(PREFERRED_SIZE, getPreferredSize());
      //}
      out.writeObject(fieldMap);
      out.flush();
    } catch (Exception e) {
      System.err.println("Error saving all of Logo component's state");
      e.printStackTrace();  //4Apr2000
    }
    //       System.out.println("write external end");

    // old writeExternal
    /*   try{
        out.writeObject(t.inputArea.getText()); //15-10-1998
        out.writeObject(t.outputArea.getText()); //15-10-1998
    /* was
        if(!saveMachineState(new File(getESlateHandle().getESlateMicroworld().getSaveFile().concat(".machine")))) //16-10-1998 //18May1999: ESlateII
         System.err.println("Logo: Couldn't save machine's state"); //17-10-1998
    was*/
    //SAVE_MACHINE_IN_SLT_START: 17-12-1998
    /*    ByteArrayOutputStream buffer=new ByteArrayOutputStream();
        IOStream ios=new IOStream(new BufferedWriter(new OutputStreamWriter(buffer)));
        t.theLOGOmachine.printout(ios, null, null, null);
        ios.close();
        out.writeObject(buffer.toByteArray()); //ByteArrayOutputStream is not serializable: so we get an extra third memory copy of our data :-(
        //SAVE_MACHINE_IN_SLT_END: 17-12-1998
     
        //SAVE MACHINE's proc-executions' clock//
        out.writeObject(new Integer(t.theLOGOmachine.level1clock));
     
        out.writeObject(t.inputArea.getFont()); //3Sep1999
     
        out.writeObject(new Boolean(isMenuBarVisible())); //15Oct1999
        out.writeObject(new Boolean(isToolBarVisible())); //15Oct1999
        out.writeObject(new Boolean(getHasFontSelector())); //15Oct1999
        out.writeObject(new Integer(getExecQueueMaxSize())); //10May2000
     
       }catch(Exception e){
        System.err.println("Error saving all of Logo component's state");
        e.printStackTrace(); //4Apr2000
       }*/

    pm.stop(saveTimer);
    pm.displayTime(saveTimer, getESlateHandle(), "", "ms");
  }

  /**
   * Load the component's state.
   * @param     in      The stream from where the state should be loaded.
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException //18May1999: from old ESlate's loadState
  {
    //      System.out.println("logo read external start");

    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    Object firstObj = in.readObject();
    if (!StorageStructure.class.isAssignableFrom(firstObj.getClass())) {
      // oldreadExternal
      //          System.out.println("logo old read external start");
      try {
        if (firstObj instanceof gr.cti.utils.ObjectHash)
          firstObj = in.readObject();  //23May2000: compatibility with previous beta versions: ignore any ObjectHash saved by the BaseComponent ancestor

        //28-1-1999:this blocks the component// while(!appletHasDoneStartup); //1-12-1998: wait till applet has started //27-1-1999: !!!
        logoGUI.inputArea.setText((String)firstObj);  //15-10-1998
        logoGUI.outputArea.setText((String)in.readObject());  //15-10-1998
        //LOAD_MACHINE_FROM_SLT_START: 17-12-1998
        ByteArrayInputStream buffer = new ByteArrayInputStream((byte[])in.readObject());
        InterpreterThread interpreterthread = new InterpreterThread(
          logoGUI.theLOGOmachine,
          new IOStream(new BufferedReader(new InputStreamReader(buffer)))
        );
        interpreterthread.start();
        //LOAD_MACHINE_FROM_SLT_END: 17-12-1998
        //LOAD MACHINE's proc-executions' clock//
        logoGUI.theLOGOmachine.level1clock =
          ((Integer)in.readObject()).intValue();  //13-1-1998

        Font f = (Font)in.readObject();
        String fName = matchFontName(f.getFamily());
        int fSize = f.getSize();
        /*
        Class c =
          logoGUI.inputArea.getSelectedComponent().getEditorKit().getClass();
        logoGUI.inputArea.setFontName(c, fName);
        logoGUI.inputArea.setFontSize(c, fSize);
        */
        int style = f.getStyle();
        logoGUI.inputArea.setFont(new Font(fName, style, fSize));
        updateFontToolBar(fName, fSize, style);

        setMenuBarVisible(((Boolean)in.readObject()).booleanValue());  //15Oct1999
        setToolBarVisible(((Boolean)in.readObject()).booleanValue());  //15Oct1999
        setHasFontSelector(((Boolean)in.readObject()).booleanValue());  //15Oct1999

        setExecQueueMaxSize(((Integer)in.readObject()).intValue());  //10May2000
      }
      catch (Exception e) {
        System.err.println("Error loading all of Logo component's saved state");
        e.printStackTrace();  //4Apr2000
      }
      //          System.out.println("logo old read external end");
    }
    else {   // new readExternal
      //          System.out.println("logo new read external start");
      StorageStructure fieldMap = (StorageStructure) firstObj;

      logoGUI.inputArea.setText((String)fieldMap.get(INPUT_AREA));  //15-10-1998
      logoGUI.outputArea.setText((String)fieldMap.get(OUTPUT_AREA));  //15-10-1998

      //LOAD_MACHINE_FROM_SLT_START: 17-12-1998
      ByteArrayInputStream buffer = new ByteArrayInputStream((byte[])fieldMap.get(BUFFER));
      InterpreterThread interpreterthread = new InterpreterThread(
        logoGUI.theLOGOmachine,
        new IOStream(new BufferedReader(new InputStreamReader(buffer)))
      );
      interpreterthread.start();
      //LOAD_MACHINE_FROM_SLT_END: 17-12-1998

      //LOAD MACHINE's proc-executions' clock//
      logoGUI.theLOGOmachine.level1clock = fieldMap.get(LEVEL_CLOCK, 1);
      Font f = (Font)fieldMap.get(INPUT_AREA_FONT);
      String fName = matchFontName(f.getFamily());
      int fSize = f.getSize();
      /*
      Class c =
        logoGUI.inputArea.getSelectedComponent().getEditorKit().getClass();
      logoGUI.inputArea.setFontName(c, fName);
      logoGUI.inputArea.setFontSize(c, fSize);
      */
      int style = f.getStyle();
      logoGUI.inputArea.setFont(new Font(fName, style, fSize));
      updateFontToolBar(fName, fSize, style);

      f = (Font)fieldMap.get(OUTPUT_AREA_FONT);
      if (f != null) {
        fName = matchFontName(f.getFamily());
        fSize = f.getSize();
        /*
        Class c =
          logoGUI.outputArea.getSelectedComponent().getEditorKit().getClass();
        logoGUI.outputArea.setFontName(c, fName);
        logoGUI.outputArea.setFontSize(c, fSize);
        */
        logoGUI.outputArea.setFont(new Font(fName, Font.PLAIN, fSize));
      }

      setMenuBarVisible(fieldMap.get(MENU_BAR_VISIBLE, true));  //15Oct1999
      setToolBarVisible(fieldMap.get(TOOL_BAR_VISIBLE, true));  //15Oct1999
      setHasFontSelector(fieldMap.get(FONT_SELECTOR, true));  //15Oct1999

      setExecQueueMaxSize(fieldMap.get(EXEC_QUEUE_MAX_SIZE, -1));  //10May2000

      BorderDescriptor bd = (BorderDescriptor)fieldMap.get(BORDER_DESCRIPTOR);
      // if (bd != null){
      try {
        setBorder(bd.getBorder());
      } catch (Throwable thr) {}
      // }
      //          System.out.println("logo new read external end");
      setLineNumbersVisibleInProgramArea(
        fieldMap.get(LINE_NUMBERS_VISIBLE_IN_PROGRAM_AREA, false)
      );
      setLineNumbersVisibleInOutputArea(
        fieldMap.get(LINE_NUMBERS_VISIBLE_IN_OUTPUT_AREA, false)
      );
      setStatusBarVisible(fieldMap.get(STATUS_BAR_VISIBLE, true));
      setMinimumSize(fieldMap.get(MINIMUM_SIZE, getMinimumSize()));
      setMaximumSize(fieldMap.get(MAXIMUM_SIZE, getMaximumSize()));
      setPreferredSize(fieldMap.get(PREFERRED_SIZE, Logo.preferredSize));
    }

    /*   try{
        Object o=in.readObject(); if(o instanceof gr.cti.utils.ObjectHash) o=in.readObject(); //23May2000: compatibility with previous beta versions: ignore any ObjectHash saved by the BaseComponent ancestor
        t.inputArea.setText((String)o); //15-10-1998
        t.outputArea.setText((String)in.readObject()); //15-10-1998
    /*was
        if(!loadMachineState(new File(getESlateHandle().getESlateMicroworld().getLoadFile().concat(".machine")))) //16-10-1998 //18May1999: ESlateII
         System.err.println("Logo: Couldn't load machine's state"); //17-10-1998
    was*/

    /*    //LOAD_MACHINE_FROM_SLT_START: 17-12-1998
        ByteArrayInputStream buffer=new ByteArrayInputStream((byte[])in.readObject());
        InterpreterThread interpreterthread = new InterpreterThread(t.theLOGOmachine,
         new IOStream(new BufferedReader(new InputStreamReader(buffer))));
        //interpreterthread.setPriority(t._priority);
        interpreterthread.start();
        //LOAD_MACHINE_FROM_SLT_END: 17-12-1998
     
        //LOAD MACHINE's proc-executions' clock//
        t.theLOGOmachine.level1clock=((Integer)in.readObject()).intValue(); //13-1-1998
     
        Font f = (Font)in.readObject();
        String fName = matchFontName(f.getFamily());
        int fSize = f.getSize();
        Class c = 
          logoGUI.inputArea.getSelectedComponent().getEditorKit().getClass();
        int style = f.getStyle();
        logoGUI.inputArea.setFontName(c, fName);
        logoGUI.inputArea.setFontSize(c, fSize);
        updateFontToolBar(fName, fSize, style);
     
        setMenuBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
        setToolBarVisible(((Boolean)in.readObject()).booleanValue()); //15Oct1999
        setHasFontSelector(((Boolean)in.readObject()).booleanValue()); //15Oct1999
     
        setExecQueueMaxSize(((Integer)in.readObject()).intValue()); //10May2000
     
       }catch(Exception e){
        System.err.println("Error loading all of Logo component's saved state");
        e.printStackTrace(); //4Apr2000
       }
       */

    pm.stop(loadTimer);
    pm.displayTime(loadTimer, getESlateHandle(), "", "ms");
  }

  //level 0///////////////////////////////////////////
  JComponent LOGO;
  LogoGUI logoGUI;

  public boolean saveMachineState(File file)
  { //16-10-1998
    try {
      IOWriteFile iowritefile = new IOWriteFile(file, false);
      logoGUI.theLOGOmachine.printout(iowritefile, null, null, null);
      iowritefile.close();
      return true;
    } catch (LanguageException e) {
      System.err.println(e);
      return false;
    }
  }

  public boolean loadMachineState(File file)
  { //16-10-1998
    try {
      InterpreterThread interpreterthread = new InterpreterThread(
        logoGUI.theLOGOmachine,
        new IOStream(new BufferedReader(new FileReader(file)))
      );
      //interpreterthread.setPriority(logoGUI._priority);
      interpreterthread.start();
      return true;
    } catch (FileNotFoundException e) {
      System.err.println(e);
      return false;
    }
  }

  // MENUS ///////////////////////////////////////////////////////////

  public final static int FILE_NEW = 0x00;
  public final static int FILE_OPEN = 0x01;
  public final static int FILE_SAVE = 0x02;
  public final static int FILE_PRINT = 0x03;

  public final static int EDIT_CUT = 0x10;
  public final static int EDIT_COPY = 0x11;
  public final static int EDIT_PASTE = 0x12;
  public final static int EDIT_CLEAR = 0x13;
  public final static int EDIT_SELECTALL = 0x14;

  //27-7-1998//
  public final static int MACHINE_LOADSTATE = 0x20;
  public final static int MACHINE_SAVESTATE = 0x21;
  public final static int MACHINE_RESET = 0x22;
  public final static int MACHINE_UNFREEZE = 0x23;  //15-4-1999

  //11-11-1998//
  public final static int MACHINE_RUN = 0x30;
  public final static int MACHINE_PAUSE = 0x31;
  public final static int MACHINE_STOP = 0x32;

  public final static int SEARCH_FIND = 0x41;
  public final static int SEARCH_FIND_NEXT = 0x42;
  public final static int SEARCH_REPLACE = 0x43;
  public final static int SEARCH_GOTO_LINE = 0x44;

  private JPanel createMenus()
  { //15Oct1999: made private
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());  //don't use BoxLayout: places menus/buttons at the center of the menubar/toolbar
    p.add(BorderLayout.NORTH, menubar = new LogoMenuBar());  //15Oct1999: keeping menubar reference, to use with setMenuBarVisible()/isMenuBarVisible()
    p.add(BorderLayout.SOUTH, toolbar = createToolBar());  //15Oct1999: keeping toolbar reference, to use with setToolBarVisible()/isToolbarVisible() //both LogoToolBar and FontToolBar exists in it
    return p;
  }

  private JPanel createToolBar()
  { //15Oct1999: putting all toolbars in one panel, needed so that ToolBarVisible property works consistently (and if hidden, hides the fontToolbar too [if that one is visible] which is a child of it and has a separate visible property [if ToolBarVisible=true and HasFontSelector=true, only then the font tool bar will show up])
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(BorderLayout.NORTH, new LogoToolBar());
    p.add(BorderLayout.SOUTH, fontToolbar = createFontToolBar());  //15Oct1999: keeping fontToolbar reference, to use with setHasFontSelector()/getHasFontSelector()
    return p;
  }

  //////////////

  public void Action(int actionID)
  {
    ESlateFileDialog filedialog;
    String s, s1;
    //  if (!AppletHasDoneStartup) return;
    //NEW caused some bugs when other pages hadn't finished loading
    //same might happen with OPEN file... so better ignore actions
    //if applet hasn't finished its startup (that is loading any initial pages)
    try {
      switch (actionID) {
          //FILE//
        case FILE_NEW:
          Action(MACHINE_RESET);   //27-7-1998
          logoGUI.inputArea.setText("");  //31-8-1998
          logoGUI.outputArea.setText("");  //31-8-1998
          break;
        case FILE_OPEN:  //if selection loads into selection else opens a new page//
          //swing// JFileChooser chooser=new JFileChooser();
          //swing// if(chooser.showDialog(Logo.this)==0) /*loadImage(chooser.getSelectedFile().getName())*/;

          //27-7-1998: adapted from TurtleTracks' GuiConsole

          filedialog = new ESlateFileDialog(new Frame(), Res.localize("Open text file"), FileDialog.LOAD);  //1-9-1998: added FileDialog.LOAD //14-1-1999: using ESlate's FileDialog
          filedialog.setDirectory(curDIR);  //1-9-1998 //17-10-1998: using curDIR
          filedialog.setFile("*.lgo");  //29-12-1998: don't have filters, but this should show only .lgo files
          filedialog.setDefaultExtension("lgo");  //14-1-1999
          logoGUI.trackFocus(false);  //31-8-1998
          filedialog.show();
          logoGUI.trackFocus(true);  //31-8-1998
          s1 = filedialog.getFile();
          if (s1 == null)
            return ;
          try {
            File file = new File(filedialog.getDirectory(), s1);
            FileReader filereader = new FileReader(file);
            char ac[] = new char[4096];
            StringBuffer stringbuffer = new StringBuffer();
            do {
              int i = filereader.read(ac);
              if (i == -1)
                break;
              stringbuffer.append(ac, 0, i);
            } while (true);
            //createEditor(stringbuffer.toString());
            filereader.close();  //26-8-1998
            String text = stringbuffer.toString();
            MyTextArea a = logoGUI.getSelectedArea();  //don't call t.getSelectedArea() during changes cause focus moves arround
            if (a.getSelectedText() != null) a.replaceSelection(text);  //27-8-1998
            else a.setText(text);  //26-8-1998
            curDIR = filedialog.getDirectory();  //17-10-1998: if success keep directory
          }
          catch (IOException ioexception) {
            //                this$0.putLine("Couldn't read file: " + ioexception.toString());
          }

          return ;
        case FILE_SAVE:  //if selection saves it else saves whole text//
          //...dialog...Res.localize("Save text file")...
          filedialog = new ESlateFileDialog(new Frame(), Res.localize("Save text file"), FileDialog.SAVE);  //1-9-1998: added FileDialog.SAVE //14-1-1999: using ESlate's FileDialog
          filedialog.setDirectory(curDIR);  //1-9-1998 //17-10-1998: using curDIR
          filedialog.setFile("*.lgo");  //29-12-1998: don't have filters, but this should show only .lgo files
          filedialog.setDefaultExtension("lgo");  //14-1-1999
          logoGUI.trackFocus(false);  //31-8-1998
          filedialog.show();
          logoGUI.trackFocus(true);  //31-8-1998
          s1 = filedialog.getFile();
          if (s1 == null)
            return ;
          //14-1-1999:using ESlateFileDialog// if (!s1.toLowerCase().endsWith(".lgo")) s1=s1.concat(".lgo"); //29-12-1998: appending ".lgo" extension if it's missing
          try {
            File file = new File(filedialog.getDirectory(), s1);
            FileWriter filewriter = new FileWriter(file);
            MyTextArea a = logoGUI.getSelectedArea();  //don't call t.getSelectedArea() during changes cause focus moves arround
            if ((s = a.getSelectedText()) == null) s = a.getText();  //27-8-1998
            filewriter.write(s, 0, s.length());
            filewriter.close();  //26-8-1998
            curDIR = filedialog.getDirectory();  //17-10-1998: if success keep directory
          }
          catch (IOException ioexception) {
            System.err.println("Couldn't read file");
            //should show warning dialog here
          }

          return ;
        case FILE_PRINT:  //prints selected text//
          logoGUI.getSelectedArea().print();
          break;

          //EDIT//
        case EDIT_CUT:
          logoGUI.getSelectedArea().cut();
          break;
        case EDIT_COPY:
          logoGUI.getSelectedArea().copy();
          break;
        case EDIT_PASTE:
          logoGUI.getSelectedArea().paste();
          break;
        case EDIT_CLEAR:
          logoGUI.getSelectedArea().replaceSelection("");
          break;
        case EDIT_SELECTALL:
          logoGUI.getSelectedArea().selectAll();
          logoGUI.getSelectedArea().requestFocus();
          logoGUI.getSelectedArea().grabFocus();
          break;

        case SEARCH_FIND:
          logoGUI.getSelectedArea().find();
          break;
        case SEARCH_FIND_NEXT:
          logoGUI.getSelectedArea().findNext();
          break;
        case SEARCH_REPLACE:
          logoGUI.getSelectedArea().replace();
          break;
        case SEARCH_GOTO_LINE:
          logoGUI.getSelectedArea().gotoLine();
          break;

          //MACHINE// --- //25-7-1998
        case MACHINE_LOADSTATE:
          filedialog = new ESlateFileDialog(new Frame(), Res.localize("Load machine state"), FileDialog.LOAD);  //1-9-1998: added FileDialog.LOAD //14-1-1999: using ESlate's FileDialog
          filedialog.setDirectory(curDIR);  //1-9-1998 //17-10-1998: using curDIR
          filedialog.setFile("*.machine");  //29-12-1998: don't have filters, but this should show only .machine files
          filedialog.setDefaultExtension("machine");  //14-1-1999
          logoGUI.trackFocus(false);  //31-8-1998
          filedialog.show();
          logoGUI.trackFocus(true);  //31-8-1998
          String s2 = filedialog.getFile();
          if (s2 == null) return ;
          if (loadMachineState(new File(filedialog.getDirectory(), s2))) //16-10-1998
            curDIR = filedialog.getDirectory();  //17-10-1998: if success keep directory
          return ;
        case MACHINE_SAVESTATE:
          filedialog = new ESlateFileDialog(new Frame(), Res.localize("Save machine state"), FileDialog.SAVE);  //1-9-1998: added FileDialog.SAVE, Res.localized dialog caption //14-1-1999: using ESlate's FileDialog
          filedialog.setDirectory(curDIR);  //1-9-1998 //17-10-1998: using curDIR
          filedialog.setFile("*.machine");  //29-12-1998: don't have filters, but this should show only ".machine" files
          filedialog.setDefaultExtension("machine");  //14-1-1999
          logoGUI.trackFocus(false);  //31-8-1998
          filedialog.show();
          logoGUI.trackFocus(true);  //31-8-1998
          String s3 = filedialog.getFile();
          if (s3 == null) return ;
          //14-1-1999:using ESlateFileDialog//            if (!s3.toLowerCase().endsWith(".machine")) s3=s3.concat(".machine"); //29-12-1998: appending ".machine" extension if it's missing
          if (saveMachineState(new File(filedialog.getDirectory(), s3))) //16-10-1998
            curDIR = filedialog.getDirectory();  //17-10-1998: if success keep directory
          return ;
        case MACHINE_RESET:
          setPaused(false);
          logoGUI.theLOGOmachine.terminateAllThreads();
          logoGUI.theLOGOmachine.eraseAll();
          //     gr.cti.eslate.logo.ComponentPrimitives.loadAllComponentsPrimitives(t.theLOGOmachine); //8-4-1999: reload all the scriptable objects' primitives
          break;
        case MACHINE_UNFREEZE:
          logoGUI.executeCommand("]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");  //give the parser lots of closing parentheses (Virtuoso's parser has a bug that if stuck on an empty parenthesis, can't be reset with MACHINE_RESET (would need to make a new machine but would have to reload all primitives etc.)
        case MACHINE_RUN:
          // if (!isPaused()) //15-1-1999: if paused, Run not accepted?
          setPaused(false);  //15-2-1999: now poping-up the Pause button if is is depressed
          logoGUI.executeSelected();  //15-4-1999: moved selection execution logic to LogoGUI (so that it can also run its cmdLine if the cursor is at the command-line text field)
          break;
          //    case MACHINE_PAUSE:
          //     if (!paused) {t.theLOGOmachine.suspend(); paused=true;} //don't allow many calls to suspend (seem to be placed in a stack and would need as many unsuspend calls)
          //     break; //11-11-1998
        case MACHINE_STOP:
          logoGUI.theLOGOmachine.terminateAllThreads();
          break;  //11-11-1998
        default:
          System.err.println("Unknown Action!!!");
          break;
      }
    } catch (Exception e) {
      System.err.println(e + " Exception at Action()");
    }
  }

  class LogoMenuListener implements ActionListener
  { //this listener calls Logo.Action
    int menuID;
    public LogoMenuListener(int menuID)
    {
      super(); this.menuID = menuID;
    }
    public void actionPerformed(ActionEvent e)
    {
      Action(menuID);
    }
  }

  JMenuItem menuItem(String item, char mnemonic, int menuID)
  {
    JMenuItem mi;
    String text;
    try {
      text = Res.localize(item);
    } catch (MissingResourceException e) {
      text = item;
    }
    ImageIcon i = Res.loadImageIcon("images/" + item + ".gif", text);
    if (i != null) {
      mi = new JMenuItem(text, i);
      mi.setHorizontalTextPosition(JButton.RIGHT);
    } else mi = new JMenuItem(text);
    if (mnemonic != ' ') mi.setMnemonic(mnemonic);
    mi.addActionListener(new LogoMenuListener(menuID));  //
    mi.setRequestFocusEnabled(false);
    return mi;
  }

  /**
   * Creates the font selection tool bar.
   * @return    The created tool bar.
   */
  @SuppressWarnings(value={"unchecked"})
  private JToolBar createFontToolBar() // 23Jul1999
  {
    JToolBar ftb = new JToolBar();

    availableFontNames = MyTextArea.getAvailableFontNames();
    // "Helvetica" can be an alias that does not appear in the list.
    // Put it in, anyway.
    if (!availableFontNames.contains(defaultFontName)) {
      availableFontNames.add(defaultFontName);
      Collections.sort(availableFontNames);
    }
    fontSelector = new JComboBox(availableFontNames);
    fontSelector.setToolTipText(Res.localize("Font"));
    fontSelector.setMaximumSize(fontSelector.getPreferredSize());
    fontSelector.setRequestFocusEnabled(false);
    fontSelector.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt)
      {
        fontNamesItemStateChanged(evt);
      }
    });
    ftb.add(fontSelector);

    ftb.addSeparator();

    sizeSelector = new JComboBox(new Object[] {
      new Integer(10), new Integer(11), new Integer(12), new Integer(13),
      new Integer(14), new Integer(15), new Integer(16), new Integer(17),
      new Integer(18), new Integer(19), new Integer(20), new Integer(21),
      new Integer(22), new Integer(23), new Integer(24), new Integer(25),
      new Integer(26), new Integer(27), new Integer(28), new Integer(29),
      new Integer(30)
    });
    sizeSelector.setToolTipText(Res.localize("Font size"));
    sizeSelector.setSelectedItem(new Integer(defaultFontSize));
    sizeSelector.setEditable(true);
    Dimension s = new Dimension(48, sizeSelector.getPreferredSize().height);
    sizeSelector.setMinimumSize(s);
    sizeSelector.setMaximumSize(s);
    sizeSelector.setPreferredSize(s);
    sizeSelector.setRequestFocusEnabled(false);
    sizeSelector.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent evt)
      {
        fontSizesItemStateChanged(evt);
      }
    });
    ftb.add(sizeSelector);

    ftb.addSeparator();

    boldButton = new JToggleButton(Res.localize("B"));
    boldButton.setFont(new Font("serif", Font.BOLD, 16));
    boldButton.setToolTipText(Res.localize("Bold"));
    boldButton.setSelected(false);
    boldButton.setRequestFocusEnabled(false);
    boldButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        fontStyleSelectionChanged();
      }
    });
    ftb.add(boldButton);

    italicButton = new JToggleButton(Res.localize("I"));
    italicButton.setFont(new Font("serif", Font.ITALIC, 16));
    italicButton.setToolTipText(Res.localize("Italic"));
    italicButton.setSelected(false);
    italicButton.setRequestFocusEnabled(false);
    italicButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
      {
        fontStyleSelectionChanged();
      }
    });
    ftb.add(italicButton);

    ftb.setFloatable(false);

    updateFontToolBar(defaultFontName, defaultFontSize, Font.PLAIN);

    return ftb;
  }

  /**
   * Returns the currently selected font style, based on the selection status
   * of the "bold" and "italic" buttons.
   * @return    One of <code>Font.PLAIN, Font.ITALIC, Font.BOLD,
   * Font.ITALIC|Font.BOLD</code>.
   */
  private int getFontStyle()
  {
    boolean bold = boldButton.isSelected();
    boolean italic = italicButton.isSelected();
    int style;
    if (!bold && !italic) {
      style = Font.PLAIN;
    }else{
      style = 0;
      if (bold) {
        style |= Font.BOLD;
      }
      if (italic) {
        style |= Font.ITALIC;
      }
    }
    return style;
  }

  /**
   * Updates the font name and font size in the font selection tool bar.
   * @param     name    The new font name.
   * @param     size    The new font size.
   * @param     style   The new font style;
   */
  void updateFontToolBar(String name, int size, int style)
  {
    updatingFontToolBar = true;
    fontSelector.setSelectedItem(name);
    sizeSelector.setSelectedItem(new Integer(size));
    boolean bold;
    boolean italic;
    if (style == Font.PLAIN) {
      bold = false;
      italic = false;
    }else{
      if ((style & Font.BOLD) != 0) {
        bold = true;
      }else{
        bold = false;
      }
      if ((style & Font.ITALIC) != 0) {
        italic = true;
      }else{
        italic = false;
      }
    }
    boldButton.setSelected(bold);
    italicButton.setSelected(italic);
    updatingFontToolBar = false;
  }

  /**
   * Action performed when a font name is selected in the font selection combo
   * box.
   * @param     evt     The event sent when a font name is selected in the
   *                    font selection combo box.
   */
  private void fontNamesItemStateChanged(ItemEvent evt)
  {
    /*
    JEditorPane pane = logoGUI.inputArea.getSelectedComponent();
    if (pane != null) {
      Class c = pane.getEditorKit().getClass();
      logoGUI.inputArea.setFontName(c, (String)(evt.getItem()));
    }
    */
    if (updatingFontToolBar) {
      return;
    }
    MyTextArea area = logoGUI.getLastSelectedArea();
    Font f = area.getFont();
    area.setFont(new Font((String)evt.getItem(), getFontStyle(), f.getSize()));
  }

  /**
   * Action performed when a font size is selected in the font selection combo
   * box.
   * @param     evt     The event sent when a font name is selected in the
   *                    font selection combo box.
   */
  private void fontSizesItemStateChanged(ItemEvent evt)
  {
    /*
    JEditorPane pane = logoGUI.inputArea.getSelectedComponent();
    if (pane != null) {
      int size = Integer.parseInt(evt.getItem().toString());
      if (size > 0) {
        Class c = pane.getEditorKit().getClass();
        logoGUI.inputArea.setFontSize(c, size);
      }else{
        sizeSelector.setSelectedItem(new Integer(logoGUI.inputArea.fontSize));
      }
    }
    */
    if (updatingFontToolBar) {
      return;
    }
    int size = Integer.parseInt(evt.getItem().toString());
    if (size > 0) {
      MyTextArea area = logoGUI.getLastSelectedArea();
      Font f = area.getFont();
      area.setFont(new Font(f.getName(), getFontStyle(), size));
    }
  }

  /**
   * Matches a font name with one of the available fonts in the system.
   * @return    The name of the matching font. If no matching font is found,
   *            the original name is returned;
   */
  private String matchFontName(String name)
  {
    int nFonts = availableFontNames.size();
    // Find a font with the same name.
    // E.g., match "monospaced" with "Monospaced".
    for (int i=0; i<nFonts; i++) {
      String s = (String)(availableFontNames.get(i));
      if (s.equalsIgnoreCase(name)) {
        return s;
      }
    }
    if (name != null) {
      String lCaseName = name.toLowerCase();
      // Find a font with whose name the name of the given font begins.
      // E.g., match "Helvetica Greek" with "Helvetica".
      for (int i=0; i<nFonts; i++) {
        String s = (String)(availableFontNames.get(i));
        if (lCaseName.startsWith(s.toLowerCase())) {
          return s;
        }
      }
      // Find a font whose name begins with the name of the given font.
      // E.g., match "Helvetica" with "Helvetica Greek".
      for (int i=0; i<nFonts; i++) {
        String s = (String)(availableFontNames.get(i));
        if (s.toLowerCase().startsWith(lCaseName)) {
          return s;
        }
      }
    }
    return name;
  }

  /**
   * Invoked when the user toggles the "bold" or "italic" button.
   */
  private void fontStyleSelectionChanged()
  {
    if (updatingFontToolBar) {
      return;
    }
    MyTextArea area = logoGUI.getLastSelectedArea();
    Font f = area.getFont();
    area.setFont(new Font(f.getName(), getFontStyle(), f.getSize()));
  }

  class LogoMenuBar extends JMenuBar
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public LogoMenuBar()
    {
      super();
      setAlignmentX(LEFT_ALIGNMENT);
      setAlignmentY(TOP_ALIGNMENT);

      JMenu file = add(new JMenu(Res.localize("File")));
      file.setRequestFocusEnabled(false);
      file.setMnemonic('F');
      file.add(menuItem("New", 'N', FILE_NEW));
      file.add(menuItem("Open Text", 'O', FILE_OPEN));
      file.add(menuItem("Save Text", 'S', FILE_SAVE));
      file.add(menuItem("Print", 'P', FILE_PRINT));

      JMenu edit = add(new JMenu(Res.localize("Edit")));
      edit.setRequestFocusEnabled(false);
      edit.setMnemonic('E');
      edit.add(menuItem("Cut", 't', EDIT_CUT));
      edit.add(menuItem("Copy", 'C', EDIT_COPY));
      edit.add(menuItem("Paste", 'P', EDIT_PASTE));
      edit.add(menuItem("Clear", 'a', EDIT_CLEAR));
      edit.add(new JSeparator());
      edit.add(menuItem("Select All", 'l', EDIT_SELECTALL));

      JMenu search = add(new JMenu(Res.localize("Search")));
      search.setRequestFocusEnabled(false);
      search.setMnemonic('S');
      search.add(menuItem("Find", 'F', SEARCH_FIND));
      search.add(menuItem("Find Next", 'N', SEARCH_FIND_NEXT));
      search.add(menuItem("Replace", 'R', SEARCH_REPLACE));
      search.add(new JSeparator());
      search.add(menuItem("Go To Line", 'G', SEARCH_GOTO_LINE));

      //27-7-1998//
      JRadioMenu machine = (JRadioMenu)add(new JRadioMenu(Res.localize("LOGO Machine")));  //15-1-1999: using JRadioMenu //15-2-1999: Java-bug?-fix: don't assign to a JMenu variable: Java has a bug? and calls the add(Action) method on the JRadioMenu instead of calling add(ToggleAction)
      machine.setRequestFocusEnabled(false);
      //  machine.add(new JSeparator()); //15-2-1999: removed
      machine.add(menuItem("Load State", 'L', MACHINE_LOADSTATE));
      machine.add(menuItem("Save State", 'V', MACHINE_SAVESTATE));
      machine.add(menuItem("Reset Environment", 'E', MACHINE_RESET));
      machine.add(menuItem("Try to unfreeze", 'F', MACHINE_UNFREEZE));  //15-4-1999
      machine.add(new JSeparator());

      machine.add(menuItem("Run", 'R', MACHINE_RUN));      //11-11-1998
      //  machine.add(menuItem("Pause",'P',MACHINE_PAUSE)); //11-11-1998
      machine.add(action_Pause);  //15-1-1999
      //machine.add(action_Test); //15-2-1999
      machine.add(menuItem("Stop", 'S', MACHINE_STOP));    //11-11-1998

      //  menuBar.setAlignmentX(LEFT_ALIGNMENT);
      /* //not needed (ESlate component has its own help button)
        JMenu help = add(new JMenu(Res.localize("Help")));
        help.setMnemonic('H');
        help.add(menuItem("About",'A',HELP_ABOUT));
      */
    }
  }

  //15-1-1999 //21Jun1999: using MyToolBar
  class LogoToolBar extends MyToolBar
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    public JButton addTool(String name, int menuID)
    {
      String text = Res.localize(name);  //17-2-1999: removed try-catch for MissingResourceException, since Res.localize handles that
      JButton b = (JButton) add(
        new JButton(
          Res.loadImageIcon("images/" + name + ".gif", text)
        )
      );
      b.setToolTipText(text);
      b.setMargin(new Insets(0, 0, 0, 0));  //don't remove else buttons will be very big
      //b.getAccessibleContext().setAccessibleName(name);
      b.setFocusPainted(false);  //27-8-1998: don't draw focus rect
      b.setRequestFocusEnabled(false);  //2-9-1998
      b.addActionListener(new LogoMenuListener(menuID));  //
      return b;
    }

    public LogoToolBar()
    {
      super();
      setFloatable(false);  //fixed-place toolbar
      addTool("New", FILE_NEW);
      addTool("Open Text", FILE_OPEN);
      addTool("Save Text", FILE_SAVE);
      addTool("Print", FILE_PRINT);
      addSeparator();
      addTool("Cut", EDIT_CUT);
      addTool("Copy", EDIT_COPY);
      addTool("Paste", EDIT_PASTE);
      addSeparator();                  //11-11-1998//

      JButton b = addTool("Run", MACHINE_RUN);      //11-11-1998//
      //   addTool("Pause",MACHINE_PAUSE); //11-11-1998//

      // We can't simply add(action_Pause), as the button that will be added
      // will be too large.
      ToggleActionButton tb = new LogoToggleActionButton(action_Pause, b);
      add(tb);

      //add(action_Test); //15-2-1999
      addTool("Stop", MACHINE_STOP);    //11-11-1998//

      addSeparator();

      addTool("Find", SEARCH_FIND);
      addTool("Find Next", SEARCH_FIND_NEXT);
      addTool("Replace", SEARCH_REPLACE);
      addTool("Go To Line", SEARCH_GOTO_LINE);
      //addSeparator();


      /* //23Jul1999: decided to place FontToolbar as a separate toolbar below this one and not into this one
         FontToolBar ftb=new FontToolBar(t.inputArea);
         ftb.setBorder(null);
         ftb.setFloatable(false); //23Jul1999
         add(ftb);
      */

      //addSeparator();
    }


  }

  String curDIR;  //the current directory (not private, so that we can access it from LogoApplet)

  ////////////////////////

  abstract class LogoToggleAction extends ToggleAction
  {
    public LogoToggleAction(String name)
    {
      String text = Res.localize(name);
      putValue(NAME, text);
      //putValue(SHORT_DESCRIPTION,text); //?
      //putValue(LONG_DESCRIPTION,text); //?
      ImageIcon icon = Res.loadImageIcon("images/" + name + ".gif", text);
      if (icon != null) putValue(SMALL_ICON, icon);  //check for null else will throw exception
    }

  }

  /*
   LogoToggleAction //TEST//
    action_Test=new LogoToggleAction("Test"){
     public void act(boolean selected){
      System.out.println(selected);
     }
    };
  */

  LogoToggleAction
  action_Pause = new LogoToggleAction("Pause")
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    boolean paused = false;
    public void act(boolean selected)
    {
      if (selected) { //don't write by mistake (selected=true) !!!
        if (!paused) {
          logoGUI.theLOGOmachine.suspend(); paused = true;
        }
      } else {
        if (paused) {
          logoGUI.theLOGOmachine.unsuspend(); paused = false;
        }
      }
    }
  };

  //////////////

  /**
   * ToggleActionButton that has the same size as the JButtons in the Logo
   * tool bar.
   */
  class LogoToggleActionButton extends ToggleActionButton
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    
    private JButton b;
    private boolean initted = false;

    /**
     * Create a LogoToggleActionButton.
     * @param   action  The action assocuated with the button.
     * @param   b       A button whose size will be mirrored by this button.
     */
    LogoToggleActionButton(ToggleAction action, JButton b)
    {
      super(action);
      initted = true;
      this.b = b;
      setText(null);
      setToolTipText((String)(action.getValue(LogoToggleAction.NAME)));
      setMargin(new Insets(0, 0, 0, 0));
      setFocusPainted(false);
      setPreferredSize(b.getPreferredSize());
      setMinimumSize(b.getPreferredSize());
      setMaximumSize(b.getPreferredSize());
    }

    /**
     * Ensure that the button has the same size as that of the button whose
     * size it is mirroring, each time the Look and Feel changes.
     */
    public void updateUI()
    {
      super.updateUI();
      if (initted) {
        setPreferredSize(b.getPreferredSize());
        setMinimumSize(b.getPreferredSize());
        setMaximumSize(b.getPreferredSize());
      }
    }
  }

  private transient JMenuBar menubar;  //15Oct1999
  private transient JPanel toolbar;  //15Oct1999
  private transient JToolBar fontToolbar;  //15Oct1999

  //////////////

  public static void main(String[] args)
  {
    JComponentFrame.startJComponent("gr.cti.eslate.logo.Logo", Res.localize("title"), new String[]{});
    JComponentFrame.startJComponents(args, args);  //27-3-1999: start any additional applets requested by the user
    //gr.cti.utils.JAppletFrame.startApplet("gr.cti.eslate.logo.LogoApplet","Logo",new String[]{});
    //gr.cti.utils.JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
  }

  /**
   * Specifies whether line numbers should be displayed in the program area.
   * @param     visible True if yes, false if no.
   */
  public void setLineNumbersVisibleInProgramArea(boolean visible)
  {
    lineNumbersVisibleInProgramArea = visible;
    logoGUI.inputArea.setLineNumbersVisible(visible);
  }

  /**
   * Checks whether line numbers are displayed in the program area.
   * @return    True if yes, false if no.
   */
  public boolean isLineNumbersVisibleInProgramArea()
  {
    return lineNumbersVisibleInProgramArea;
  }

  /**
   * Specifies whether line numbers should be displayed in the output area.
   * @param     visible True if yes, false if no.
   */
  public void setLineNumbersVisibleInOutputArea(boolean visible)
  {
    lineNumbersVisibleInOutputArea = visible;
    logoGUI.outputArea.setLineNumbersVisible(visible);
  }

  /**
   * Checks whether line numbers are displayed in the output area.
   * @return    True if yes, false if no.
   */
  public boolean isLineNumbersVisibleInOutputArea()
  {
    return lineNumbersVisibleInOutputArea;
  }

  /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm)
  {
    if (perfListener == null) {
      perfListener = new PerformanceAdapter() {
        public void performanceManagerStateChanged(PropertyChangeEvent e)
        {
          boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
          // When the Performance Manager is enabled, try to attach the
          // timers.
          if (enabled) {
            attachTimers();
          }
        }
      };
      pm.addPerformanceListener(perfListener);
    }
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  protected void attachTimers()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    boolean pmEnabled = pm.isEnabled();

    // If the performance manager is disabled, install a listener which will
    // re-invoke this method when the performance manager is enabled.
    if (!pmEnabled && (perfListener == null)) {
      createPerformanceManagerListener(pm);
    }

    // Do nothing if the PerformanceManager is disabled.
    if (!pmEnabled) {
      return;
    }

    boolean timersCreated = (loadTimer != null);
    // If the component's timers have not been constructed yet, then
    // construct them. During construction, the timers are also attached.
    if (!timersCreated) {
      // Get the performance timer group for this component.
      PerformanceTimerGroup compoTimerGroup = pm.getPerformanceTimerGroup(this);
      // Construct and attach the component's timers.
      constructorTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("ConstructorTimer"), true
      );
      loadTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("LoadTimer"), true
      );
      saveTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("SaveTimer"), true
      );
      initESlateAspectTimer = (PerformanceTimer)pm.createPerformanceTimerGroup(
        compoTimerGroup, Res.localize("InitESlateAspectTimer"), true
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.CONSTRUCTOR, constructorTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.LOAD_STATE, loadTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.SAVE_STATE, saveTimer, this
      );
      pm.registerPerformanceTimerGroup(
        PerformanceManager.INIT_ESLATE_ASPECT, initESlateAspectTimer, this
      );
    }
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The component's E-Slate handle.
   */
  public ESlateHandle getESlateHandle()
  {
    ESlateHandle h;
    if (avHandle == null) {
      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      pm.init(initESlateAspectTimer);

      h = super.getESlateHandle();
      h.addESlateListener(new ESlateAdapter(){
        public void handleDisposed(HandleDisposalEvent e)
        {
          pmCleanup();
        }
      });

      pm.eSlateAspectInitEnded(this);
      pm.eSlateAspectInitEnded(this);
      pm.displayTime(initESlateAspectTimer, avHandle, "", "ms");
    }else{
      h = avHandle;
    }
    return h;
  }

  /**
   * Performance manager-related cleanup.
   */
  private void pmCleanup()
  {
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }

}
