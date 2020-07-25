//!!! when run with Physics, and Logo window load and gets to front of Physics windows, divider goes near the top... if Physics dragged before Logo windows shows, then divider goes quite OK near the bottom (200pixels from top, as set)

//LOGO should display Slider command when it's executed?...
//...or when it's placed in queue?

/*
 27/5/98 : Procedures now work OK (was my parser's problem)...
           Slowdown must have ceased (was doing lot of calls)
 28/05/98 : Now suspending console's thread till some data is available
 29/06/98 : Added executeCommand to run LOGO commands explicitly
 13/07/98 : Revised: now console thread works fine, and there is a command execution queue
 27/08/98 : ExecQueue is max length 1 when interaction with Slider
 28/08/98 : Added "Edit" support, added Greek punctuation (ALT,SHIFT+ALT)
 22/09/98 : Now after exec cursor moves to start of next line after selection, instead of next line after cursor pos
 08/04/99 : when a text area loses focus it hides its caret
 15/04/99 : changed superClass from JSplitPane to JComponent (to also add a JTextField to the GUI)
 ...
 10/05/00 : moved status bar from main Logo class to this class
            showing status bar messages when switching from executing to idle state
 31/05/00 : now the logic of loading the default primitives is not at Logo but at LogoEngine
 30/06/00 : removed call to the console's "close()" method: not needed and was throwing a LanguageException since this console isn't closeable
*/

package gr.cti.eslate.logo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import virtuoso.logo.*;  //LOGO//
import virtuoso.logo.app.InteractiveConsole;  //LOGO//

import jeditSyntax.*;

import gr.cti.eslate.sharedObject.*;
//import gr.cti.eslate.editor.*;
//import gr.cti.utils.ResizableSplitPane;

////////////////////////////////////////////////////////////////////////////////

/**
 * @version     2.0.5, 17-Jan-2008
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 */
public class LogoGUI extends JPanel
{
  /**
   * Serialization version.
   */
  final static long serialVersionUID = 1L;
  
  private int mode = /*NOT_*/USING_SPLIT_PANE;  //3Sep1999

  /////

  public LogoGUI(LogoCallSO callSO)
  {
    this.callSO = callSO;  //before start()!!! (or pass to start immediately? who else calls start? does he have the callSO to give to it? [LOGO?->yes!])

    inputArea.setFont(
      new Font(Logo.defaultFontName, Font.PLAIN, Logo.defaultFontSize)
    );
    
    //add cmdLine and statusBar
    cmdLine = new JTextField();  //15-4-1999
    statusBar = new JLabel();  //10May2000
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.add(cmdLine, BorderLayout.CENTER);
    p.add(statusBar, BorderLayout.SOUTH);

    setLayout(new BorderLayout());   //15-4-1999
    add(getTextAreasTogether(), BorderLayout.CENTER);
    add(p, BorderLayout.SOUTH);

    trackFocus(true);

    start();  //LOGO//

    cmdLine.addActionListener(new CmdLineListener());
    //cmdLine.addFocusListener(new CmdLineListener()); //???add as focus listener
    //inputArea.setTitle(Res.localize("Program"));
    //outputArea.setTitle(Res.localize("Output"));
  }

  //statusBarText property//

  public void setStatusBarText(String message)
  {
    statusBar.setText(message);
  }

  public String getStatusBarText()
  {
    return statusBar.getText();
  }

  //statusBarVisible property//

  /**
   * Specifies whether the status bar should be visible.
   * @param     visible True if yes, false if no.
   */
  public void setStatusBarVisible(boolean visible)
  {
    statusBar.setVisible(visible);
  }

  /**
   * Checks whether the status bar is visible.
   * @return    True if yes, false if no.
   */
  public boolean isStatusBarVisible()
  {
    return statusBar.isVisible();
  }

  //execQueueMaxSize property//

  public void setExecQueueMaxSize(int size)
  {
    execQueueMaxSize = size;
  }

  public int getExecQueueMaxSize()
  {
    return execQueueMaxSize;
  }

  //////////// 3Sep1999: this is the CORRECT SPLIT PANE DIVIDER POSITIONING routine //////////////////////

  private double splitPaneFraction = 0;  //26May1999: initially no fraction

  public void setSplitPaneDividerLocation(double fraction)
  { //26May1999: tracking divider changes???
    if (fraction != 0) {
      splitPaneFraction = fraction;
      splitPane.setDividerLocation(fraction);
    }
  }

  public void processComponentEvent(ComponentEvent e)
  { //26May1999
    if (e.getID() == ComponentEvent.COMPONENT_RESIZED) {
      //System.out.println("Resized");
      splitPaneFraction = splitPane.getDividerLocation();  //before super.processComponentEvent   super.processComponentEvent(e);
      //the above might throw exception???

      super.processComponentEvent(e);

      if (!(splitPaneFraction >= 0 && splitPaneFraction <= 1))
        splitPaneFraction = 6d / 10d;  //the first time we're getting an "26" value???

      //System.out.println("Split Pane fraction="+splitPaneFraction);

      String text1 = inputArea.getText();  //26May1999:resize-JSplitPane-patch
      String text2 = outputArea.getText();  //26May1999:resize-JSplitPane-patch

      splitPane.setDividerLocation(splitPaneFraction);  //do this only after start (SplitPanel size has been set by now)

      inputArea.setText(text1);  //26May1999:resize-JSplitPane-patch
      outputArea.setText(text2);  //26May1999:resize-JSplitPane-patch
    }
    else
      super.processComponentEvent(e);
  }

  //////////////////////*********//////////////////////




  public void setDividerRatio(double ratio)
  { //19Aug1999
    //System.out.println("divider ratio="ratio);
    if (splitPane != null) {
      splitPane.setResizeWeight(ratio);
      splitPane.setDividerLocation(ratio); //splitPane.setRatio(ratio);
    }
  }


  private JComponent getTextAreasTogether()
  { //3Sep1999
    if (mode == USING_SPLIT_PANE) {
      splitPane = new JSplitPane/*ResizableSplitPane*/( //USING a split pane that tries to keep a steady view ratio between its two members
                    JSplitPane.VERTICAL_SPLIT,  //orientation //15-4-1999: now LogoGUI is not a JSplitPane, but is aggregating one
                    true,  //continuousLayout: redisplay while resizing (slow when using Layout managers)
                    inputArea,  //top
                    outputArea //bottom
                  );
      splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
      splitPane.setOpaque(true);
      splitPane.setContinuousLayout(true);  //if we set continuous layout to false, then moving the bar doesn't resize the text area until we drop the bar at some place
      splitPane.setDividerSize(10);  //big divider to be viewable
      splitPane.setOneTouchExpandable(true);  //Show two small buttons that allow left/middle/right positioning of the spliter
      final double ratio = 6d / 10d;
      setDividerRatio(ratio);
      splitPane.addComponentListener(new ComponentAdapter()
      {
        @Override
        public void componentResized(ComponentEvent e)
        {
          JSplitPane s = (JSplitPane)e.getSource();
          setDividerRatio(ratio);
          s.removeComponentListener(this);
        }
      });
      return splitPane;
    } else {
      splitPane = null;
      JComponent c = new LogoTextAreas();
      c.add(inputArea);
      c.add(outputArea);
      return c;
    }
  }

  ///////////////////

  /**
   * Returns a reference to the currently selected editor.
   * @return    The requested editor.
   */
  public MyTextArea getSelectedArea()
  {
    /*
    JEditorPane pane;
    if (selected != null) {
      pane = selected;
    }else{
      // 2-9-1998:don't rely to other code initing "selected" (was
      // initializing to outputArea, changed that to inputArea anyway)
      pane = inputArea.getSelectedComponent();
    }
    return convertJEditorPaneToMyTextArea(pane);
    */
    if (selected != null) {
      return selected;
    }else{
      return inputArea;
    }
  }

  /**
   * Returns a reference to the last selected editor. Use this method to
   * select an editor for changes in its UI.
   * @return    If an editor is in focus, this editor is returned. If no
   *            editor is in focus, the last editor that was in focus was
   *            returned. If no editor had ever been in focus, the input area
   *            is returned.
   */
  public MyTextArea getLastSelectedArea()
  {
    if (selected != null) {
      return selected;
    }else{
      if (unselected != null) {
        return unselected;
      }else{
        return inputArea;
      }
    }
  }

//  /**
//   * Returns the MyTextArea component containing a given editor pane.
//   * @return    The requested component. If the editor pane is not
//   *            contained in a MyTextArea component, this method returns null.
//   */
//  private MyTextArea convertJEditorPaneToMyTextArea(JEditorPane pane)
//  {
//    if (pane != null) {
//      Component c;
//      for (c=pane; (c != null && !(c instanceof MyTextArea)); c=c.getParent()) {
//      }
//      return (MyTextArea)c;
//    }else{
//      return null;
//    }
//  }

  public void executeSelected()
  {
    // 15-4-1999: if some text area select will run from there,
    // if cursor on the cmd line will run cmd-line
    if (selected != null) {
      // 11-11-1998: both areas are LogoInputAreas for now... cast should be OK
      ((LogoGUI.LogoInputArea)getSelectedArea()).executeSelection();
    }else{
      executeCmdLine();
    }
  }

  public void trackFocus(boolean b)
  {
    if (b) {
      inputArea.addFocusListener(myFocusListener);
      outputArea.addFocusListener(myFocusListener);
      cmdLine.addFocusListener(myFocusListener);
    } else {
      inputArea.removeFocusListener(myFocusListener);
      outputArea.removeFocusListener(myFocusListener);
      cmdLine.removeFocusListener(myFocusListener);
    }
  };

  FocusListener myFocusListener = new FocusListener()
  {
    public void focusGained(FocusEvent e)
    {
      //selected = (JEditorPane)e.getSource();
      Object sel = e.getSource();
      if (sel.equals(cmdLine)) {
        selected = null;
      }else{
        selected = (MyTextArea)sel;
        // 2-9-1998:show Caret (disappears some times)
        //selected.getCaret().setVisible(true);
        selected.setCaretVisible(true);
        Font f = selected.getFont();
        Logo logo = (Logo)(callSO.getHandle().getComponent());
        logo.updateFontToolBar(f.getName(), f.getSize(), f.getStyle());
      }
    }

    public void focusLost(FocusEvent e)
    {
      Object uns = e.getSource();
      if (!uns.equals(cmdLine)) {
        unselected = (MyTextArea)e.getSource();
        // if this is the one that was currently selected, clear the selected
        // var (needed so that executeSelected() can run from the cmdLine if no
        // text area is selected)
        //- if (selected == unselected) {
        //-   selected = null;
        //- }
        // 8-4-1999: hiding he caret at the unselected area, cause Swing showed
        // sometimes carets at both areas and the user didn't know which area
        // he was saving
        // 15-4-1999: unselecting the text area that lost focus, else it shows
        // a blue bkgr with black fgr for the old selection
        //unselected.getCaret().setVisible(false);
        unselected.setCaretVisible(false);
        //MyTextArea mta = convertJEditorPaneToMyTextArea(unselected);
        //mta.unSelect();
        //- unselected.unSelect();
      }
    }
    // 4-9-1998: fixed bug:was doing selected=inputArea which is wrong since
    // going to a menu was setting inputArea as the selected one
  };

  //LOGO-start//
  public void start()
  {
    execQueue.removeAllElements();  //12-7-1998
    theLOGOconsole = new LOGOconsole();
    theLOGOmachine = new MyMachine(callSO, theLOGOconsole, null);
    theLOGOmachine.loadDefaultPrimitives();  //31May2000: moved logic that loads default primitives to the LogoEngine
    try {
      theLOGOmachine.prepare();
    } catch (virtuoso.logo.SetupException e) {
      System.err.println("Logo: Couldn't setup the LogoMachine");
      e.printStackTrace();
    }
    theLOGOconsoleThread = new Thread(theLOGOconsole);
    theLOGOconsoleThread.start();  //calls theLOGOconsole.run()
  }


  public void finish()
  {
    try {
      //theLOGOconsoleThread.stop();
      theLOGOconsole.goodbye();
      //30Jun2000: don't call close(): doesn't do any cleanups and throws a LanguageException [on purpose] since this console can't be closed [isn't a window]// theLOGOconsole.close();
      theLOGOmachine.tearDown();
    } catch (Exception e) {}

  }
  //LOGO-end//

  public void executeCmdLine()
  { //15-4-1999
    executeCommand(cmdLine.getText());
    cmdLine.setText("");
  }

  private class CmdLineListener extends FocusAdapter implements ActionListener
  { //15-4-1999: when enter is pressed, or focus is lost at cmd line (if added as focus listener too), execute the command
    public void actionPerformed(ActionEvent e)
    {
      executeCmdLine();
    }
    public void focusLost(FocusEvent e)
    {
      executeCmdLine();
    }
  }

  public void executeCommand_Display(LogoCommandSO commandSO)
  {
    String s = commandSO.getCommand();
    // System.out.println(s+"--");
    /* //15-4-1999: removed, now showing received command at cmdLine
      if (inputArea.getSelectedText()!=null)
       inputArea.changeSelection(s); //31-8-1998: using changeSelection instead of replaceSelection (wasn't staying selected)
      else inputArea.insertAtNewLineSelected(s); //27-8-1998: removed +'n'
    */
    cmdLine.setText(s);  //15-4-1999: showing received command at cmdLine and not into the input text area (it risked to get mixed with Logo procedure bodies if the current selection was changed while playing with the Slider)
    executeCommand(s, new Integer(commandSO.getTopLevelClock()));
  }
  //LOGO isn't really executing the command at that old clock
  //it's the LogoCallSO shared object that locks the clock and ignores
  //the clock value from LOGO (otherwise we'd need to save the clock, set
  //the clock to the old val-1 and after exec from Logo restore the previous clock value,
  //but it's pretty problematic to access to all InterperterThreads running to touch their
  //TopLevelClock value (which I added!!!)
  //!!!Don't unlock the clock here: exec is done in a separate thread and might have not yet been started!!!

  public void executeCommand(String s)
  {
    executeCommand(s, null);  //13-7-1998
  }

  /* //25-11-1998//
   (??? When executing manually many commands (many lines) and we then move the slider, the proc
   lines are removed from the stack and control goes to slider [this might cause a bug... however
   low probability, only in case the Logo parser hasn't finished parsing some long proc definition
   ... we have to do a big selection,press CTRL, then go to the Slider as fast as possible and
   do some dragging...
   --->Maybe should not parse a command block here, but at the getline method of the console
       implementation... should put the whole block as one entry in the execQueue instead of
       putting one for each line (the Slider generates only single line commands when reexecuting
       some proc at past time... e.g. BOX 10 20 30)
  */
  public void executeCommand(String s, Integer clock)
  {
    synchronized (execQueue) { //12-7-1998
      if (clock != null) execQueue.removeAllElements();  //27-8-1998: Queue max length=1 at Slider/Logo interaction (only for commands scheduled to execute at a certain clock -> when user moves slider, the current execution queue gets emptied)
      for (StringTokenizer t = new StringTokenizer(s, "\n\r"); t.hasMoreTokens(); )
        execQueue.addElement(new Command(t.nextToken(), clock));  //13-7-1998
    }


    if (execQueueMaxSize >= 0) execQueue.setSize(execQueueMaxSize);  //10May2000: setting to maximum queue size if one available (if max queue size == 0, then the Logo console selected-commands-execution if effectively disable) [after this, we could also do trimToSize to change the internal array of the Vector, but that would take longer and eat up more temporary memory]

    if (execQueue.size() != 0) { //15-2-1999: moved out of syncronized block, because there was sometimes a deadlock with execQueue.firstElement() call at LOGOConsole.getLine() method
      //System.out.println("executeCommand: trying to wake up console in case it is asleep");
      theLOGOconsole.wakeUp();  //13-7-1998 //is this still causing a deadlock in the Logo<->Slider loop???
    }

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public class LogoInputArea extends MyTextArea
  {
    /**
     * Serialization version.
     */
    final static long serialVersionUID = 1L;
    String type;

    public LogoInputArea(String type)
    {
      super(type);
      this.type = type;
      //javax.swing.text.Keymap km = getSelectedComponent().getKeymap();
      Action a;
      //KeyStroke ks;
      InputHandler ih = getInputHandler();

      //ks = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0, false);
      a  = new AbstractAction()
      {
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent evt)
        {
          LogoInputArea lia = (LogoInputArea)evt.getSource();
          lia.executeSelection();
        }
      };
      //km.addActionForKeyStroke(ks, a);
      ih.addKeyBinding("INSERT", a);

      //ks = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false);
      a = new AbstractAction()
      {
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent evt)
        {
          LogoInputArea lia = (LogoInputArea)evt.getSource();
          lia.executeSelection();
        }
      };
      //km.addActionForKeyStroke(ks, a);
      ih.addKeyBinding("F1", a);

      //ks = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false);
      a = new AbstractAction()
      {
        /**
         * Serialization version.
         */
        final static long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent evt)
        {
          Enumeration<Command> e = execQueue.elements();
          while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
          }
          System.out.println(
            "--Name List--\n" + theLOGOmachine.getNameList()
          );
          System.out.println(
            "--Proc List--\n" + theLOGOmachine.getProcList()
          );
          System.out.println(
            "--Fileid List--\n" + theLOGOmachine.getFileidList()
          );
          //System.out.println(
          //  "--thread--\n"+(InterpEnviron.thread())._symStack.getNames()
          //);
          //...should make machine._threads public to do that...
          // then get the thread be name
        }
      };
      //km.addActionForKeyStroke(ks, a);
      ih.addKeyBinding("F2", a);

      // ---
//      a = new AbstractAction()
//      {
//        /**
//         * Serialization version.
//         */
//        final static long serialVersionUID = 1L;
//
//        public void actionPerformed(ActionEvent evt)
//        {
//          getSelectedArea().copy();
//        }
//      };
//      ih.addKeyBinding("C+C", a);
//
//      a = new AbstractAction()
//      {
//        /**
//         * Serialization version.
//         */
//        final static long serialVersionUID = 1L;
//
//        public void actionPerformed(ActionEvent evt)
//        {
//          getSelectedArea().paste();
//        }
//      };
//      ih.addKeyBinding("C+V", a);
//
//      a = new AbstractAction()
//      {
//        /**
//         * Serialization version.
//         */
//        final static long serialVersionUID = 1L;
//
//        public void actionPerformed(ActionEvent evt)
//        {
//          getSelectedArea().cut();
//        }
//      };
//      ih.addKeyBinding("C+X", a);
    }

    public void executeSelection()
    { //11-11-1998
      String s = getSelectedText();  //returns nil if there is no text selected
      if (s == null) {
        selectCurrentLine(); s = getSelectedText();
      } //returns null if there is no text selected
      if (s != null) {
        //exec LOGO commands from s//
        moveToStartOfLineAfterSelection();
        unSelect();  //flash selection effect (LOGO should take some time to exec... else use some delay before this)
        //22-9-1998:removed:now moving to line after selection// moveToNextLineStart();
        executeCommand(s);  //exec now: commands are put in a queue
      }

    }

  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //LOGO-start//
  public class LOGOconsole extends InteractiveConsole
  {

    //30Jun2000: removed "close()" method: keep the default behaviour from our parent class: it throws a LanguageException with a message "Can't close console" to the user

    public synchronized void wakeUp()
    {
      notifyAll();
    }

    public boolean eof()throws LanguageException
    { //The console always has more data to give (that is can ask the user if more data needed)
      //System.out.println("eof");
      return false/*!charAvail()*/;  //always false else if user gives partial declarations won't work
    }

    ///////////////


    private String cInputing = Res.localize("ConsoleInputing");  //localize the message once and not in the "getLine" routine which gets called very often    [!!! in the future, if Java has locale-change notifications, should have some way to rebuild this constant based on the new locale that the user has set]
    private String cExecuting = Res.localize("ConsoleExecuting");  //localize the message once and not in the "getLine" routine which gets called very often  [!!! in the future, if Java has locale-change notifications, should have some way to rebuild this constant based on the new locale that the user has set]

    public synchronized String getLine()throws LanguageException
    {
      //System.out.println("getLine");

      inputArea.setCaretColor(Color.red);  //suspended waiting for input
      setStatusBarText(cInputing);  //10May2000
      //System.out.println("stop");

      while (execQueue.size() == 0) {
        //System.out.println("No input available: putting to sleep... waiting for a call to this.wakeUp()");
        //Toolkit.getDefaultToolkit().beep(); //14-7-1998: too frequent beeps get annoying!!!
        try {
          theLOGOconsole.wait();
        } catch (InterruptedException ex) {
          //System.out.println("WakenUp: input came in");
        }

      } //13-7-1998

      inputArea.setCaretColor(Color.blue);  //processing input (can still accept more input)
      setStatusBarText(cExecuting);  //10May2000
      //System.out.println("exec");

      try {
        Command c = execQueue.firstElement();
        String s = (String)c.commandString;
        callSO.executeAtForcedClock = c.clock;  //lock clock: exec at certain time
        try {
          execQueue.removeElementAt(0);
        } catch (Exception e) {
          throw new LanguageException("Couldn't remove command read from queue");
        }
        return s;
      } catch (NoSuchElementException e) {
        throw new LanguageException("Couldn't read command from queue");
      }
    }

    public String promptGetLine(char c)
    { //called by READLIST, READWORD and also by Virtuoso itself to get more commands
      //System.out.println("promptGetLine");
      try {
        return getLine();
      } catch (Exception e) {
        return "";
      }
    }

    public int getAvailable(char ac[])throws LanguageException
    { //never called???
      //System.out.println("getAvailable");
      //    int i;
      //    for (i=0;bufferPos<buffer.length && i<ac.length;i++) ac[i]=buffer[bufferPos++];
      //    return i;
      return 0;
    }
    public char getChar()throws LanguageException
    { //USED BY "READCHARACTER" primitive: maybe could block and wait for a key to be pressed, then return it (could make games that way) ???
      //System.out.println("getChar");
      //    return buffer[bufferPos++];
      throw new LanguageException("getChar???");
    }
    public boolean charAvail()throws LanguageException
    {
      /**/System.out.println("charAvail");
      //    try{return (bufferPos<buffer.length);}
      //    catch(NullPointerException e){return false;} //in case buffer hasn't been allocated yet
      throw new LanguageException("charAvail???");
    }

    ///////////////

    public void put(String s)throws LanguageException
    {
      //System.out.println("put");
      //outputArea.appendSelected(s); //15-4-1999: not appending as selected any more (Swing's latest versions show blue bkgr+black fgr for selection)
      outputArea.appendScrolling(s);  //15-4-1999
    }

    public void put(char c)throws LanguageException
    {
      //System.out.println("put(char)");
      //outputArea.insert(String.valueOf(c),outputArea.getCaretPosition()); //15-4-1999: using appendScrolling to add to end of output text area
      outputArea.appendScrolling(String.valueOf(c));  //15-4-1999
    }

    public void put(char ac[], int i)throws LanguageException
    {
      //System.out.println("put(char[],int)");
      //outputArea.insert(new String(ac,0,i),outputArea.getCaretPosition()); //15-4-1999: using appendScrolling to add to end of output text area
      outputArea.appendScrolling(new String(ac, 0, i));  //15-4-1999
    }

    public void putLine(String s)
    {
      //System.out.println("putLine");
      //outputArea.appendSelected(s+'\n'); //15-4-1999: not appending as selected any more (Swing's latest versions show blue bkgr+black fgr for selection)
      outputArea.appendScrolling(s + '\n');  //15-4-1999: appending the new text and scrolling the view to its end (to show the new text)
    }

    ///////////////


    public void createEditor(String s)throws LanguageException
    {
      //    System.out.println("createEditor("+s+')');
      try { //30-8-1998
        StringTokenizer st = new StringTokenizer(s);
        st.nextToken();
        executeCommand("ERP \"" + st.nextToken());

        //s=s.replace((char)13,' '); //20-10-1998: CR's were given as CRLF and messed up when EDITing a proc (adding things at the end of a line)
        s = trim13(s);  //20-10-1998: CR's were given as CRLF and messed up when EDITing a proc (adding things at the end of a line) [this is better than replacing CR with a space]

        //TEST// char[] a=s.toCharArray();
        //TEST// for (int i=0;i<a.length;i++) System.out.println(a[i]+" "+(int)a[i]);

        inputArea.insertAtNewLineSelected(s);  //31-8-1998 //20-10-1998: changed '\n'+s to s
      }
      catch (Exception e) {
        System.err.println("Error getting proc name from:\n" + s);
      }
    }

    public void goodbye()
    {
      putLine("Goodbye!");
    }
  }
  //LOGO-end//

  private class Command
  {
    public String commandString;
    public Integer clock;  //can be null if want to do normal exec and not at fixed clock
    public Command(String commandString, Integer clock)
    {
      this.commandString = commandString;
      this.clock = clock;
    }
  }

  private static final String trim13(String s)
  {
    char[] in = s.toCharArray();
    char[] out = new char[in.length];
    int chars = 0;
    for (int i = 0; i < in.length; i++) {
      char c = in[i];
      if (c != (char)13) out[chars++] = c;  //use post-increment: first set, then augment (at finish chars has the total count)
    }

    return String.copyValueOf(out, 0, chars);  //copy the chars to a string
  }

  ////////////////////////////////////////////////////////////////////////////////


  private Vector<Command> execQueue = new Vector<Command>();  //12-7-1998
  private int execQueueMaxSize = -1;  //10May2000: unlimited queue size by default

  private JSplitPane/*ResizableSplitPane*/ splitPane;
  private static final int USING_SPLIT_PANE = 0;
  //private static final int NOT_USING_SPLIT_PANE = 1;

  private JTextField cmdLine;  //15-4-1999
  private JLabel statusBar;  //10May2000

  //private char[] buffer;
  //private int bufferPos;

  //private JEditorPane selected = null;
  private MyTextArea selected = null;
  private MyTextArea unselected = null;

  private LogoCallSO callSO;

  public Thread theLOGOconsoleThread;
  public MyMachine theLOGOmachine;
  public LOGOconsole theLOGOconsole;

  public LogoInputArea/*MyTextArea*/ outputArea = new LogoInputArea(MyTextArea.PLAIN_TEXT);  //31-8-1998: should work as INPUT too!!!
  public LogoInputArea inputArea = new LogoInputArea(MyTextArea.LOGO);

}
