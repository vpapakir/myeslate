package gr.cti.eslate.scripting.logo;

import java.io.*;
import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by E-Slate handles.
 * These primitives are:
 * <BR>
 * <B>SHOWHELP</B>. Displays help for the component.
 * <BR>
 * <B>SHOWINFO</B>. Displays information about the component.
 * <BR>
 * <B>SHOWPLUGVIEW</B>. Displays the plug view window of the microworld to
 * which the component belongs.
 * <BR>
 * <B>PLUGS</B>. Returns a list of the names of the plugs of the component. Each
 * of these names is of the form name1.name2...nameN, where name1 is the name
 * of the top level plug and the remaining names are the names of the sub-plugs
 * in the path leading to the plug.
 * <BR>
 * <B>CONNECT</B> <I>plug1 targetComponent plug2</I>. Connect plug <I>plug1</I>
 * of the component to plug <I>plug2</I> of <I>targetComponent</I>. Each plug is
 * specified as a string containing plug names separated by periods, starting
 * from a top level plug, following subplugs, until the desired plug is reached.
 * E.g.,
 * <PRE>
 * CONNECT "|Data Base.Table1| "Chart "Table
 * </PRE>
 * <BR>
 * <B>DISCONNECT</B> <I>plug1 targetComponent plug2</I>. Same as above,
 * disconnecting instead of connecting the two plugs. E.g.,
 * <PRE>
 * DISCONNECT "|Data Base.Table1| "Chart "Table
 * </PRE>
 * <BR>
 * <B>DISCONNECTPLUG</B> <I>plug</I>. Similar to DISCONNECT, except that
 * only a plug is specified, without a corresponding component
 * and plug. The specified plug is recursively disconnected from all the plugs
 * to which it is connected. E.g.,
 * <PRE>
 * DISCONNECTPLUG "|Data Base.Table1|
 * </PRE>
 * <BR>
 * <B>CONNECTED</B> <I>plug1 targetComponent plug2</I>. Returns whether plug
 * <I>plug1</I> of the component is connected to plug <I>plug2</I> of
 * <I>targetComponent</I>. Each plug is specified as a string containing plug
 * names separated by periods, starting from a top level plug, following
 * subplugs, until the desired plug is reached. E.g.,
 * <PRE>
 * CONNECTED "|Data Base.Table1| "Chart "Table
 * </PRE>
 * <B>LISTCONNECTIONS</B> <I>plug</I>. Returns a list of the connections to a
 * given plug. Each returned connection is a list containg the name of the
 * component to which the given plug is connected and the name of the plug to
 * which the given plug is connected. Plug names are both specified and returned
 * as a string containing plug names separated by periods, starting
 * from a top level plug, following subplugs, until the desired plug is reached.
 * E.g,
 * <PRE>
 * LISTCONNECTIONS "|Data Base.Table1|
 * </PRE>
 * <BR>
 * <B>SETRENAMINGALLOWEDFROMBAR</B> <I>yesno</I>. Sets whether renaming the
 * component from the menu bar is allowed.
 * <BR>
 * <B>RENAMINGALLOWEDFROMBAR</B>. Returns whether renaming the component from
 * the menu bar is allowed.
 * <BR>
 * <B>SETNATIVEPROGRAMFOLDERS</B> <I>[list]</I>. Set the folders where
 * components can look for native programs. The programs are given as a list.
 * If there is only one folder to specify, its name can be given directly,
 * instead of putting it in a list.
 * <BR>
 * <B>NATIVEPROGRAMFOLDERS.</B> Get a list of the folders where components
 * can look for native programs.
 * <BR>
 * <B>NATIVEPROGRAM</B> <I>program</I>. Returns the first occurrence of a
 * native program in the list of folders where components can look for native
 * programs.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.22, 9-Jan-2008
 * @see gr.cti.eslate.base.ESlateHandle
 */
@SuppressWarnings("unchecked")
public class ESlatePrimitives extends PrimitiveGroup
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = null;

  /**
   * Required for scripting.
   */
  MyMachine myMachine;

  /**
   * Connect two plugs.
   */
  private final static int CONNECT = 0;
  /**
   * Disconnect two plugs.
   */
  private final static int DISCONNECT = 1;

  /**
   * Register primitives.
   */
  protected void setup(Machine machine, virtuoso.logo.Console console)
    throws SetupException
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.scripting.logo.ESlateResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("SHOWHELP", "pSHOWHELP", 0);
    myRegisterPrimitive("SHOWINFO", "pSHOWINFO", 0);
    myRegisterPrimitive("SHOWPLUGVIEW", "pSHOWPLUGVIEW", 0);
    myRegisterPrimitive("PLUGS", "pPLUGS", 0);
    myRegisterPrimitive("CONNECT", "pCONNECT", 3);
    myRegisterPrimitive("DISCONNECT", "pDISCONNECT", 3);
    myRegisterPrimitive("DISCONNECTPLUG", "pDISCONNECTPLUG", 1);
    myRegisterPrimitive("CONNECTED", "pCONNECTED", 3);
    myRegisterPrimitive("LISTCONNECTIONS", "pLISTCONNECTIONS", 1);
    myRegisterPrimitive("SETRENAMINGALLOWEDFROMBAR",
                        "pSETRENAMINGALLOWEDFROMBAR", 1);
    myRegisterPrimitive("RENAMINGALLOWEDFROMBAR", "pRENAMINGALLOWEDFROMBAR", 0);
    myRegisterPrimitive("SETNATIVEPROGRAMFOLDERS",
                        "pSETNATIVEPROGRAMFOLDERS", 1);
    myRegisterPrimitive("NATIVEPROGRAMFOLDERS", "pNATIVEPROGRAMFOLDERS", 0);
    myRegisterPrimitive("NATIVEPROGRAM", "pNATIVEPROGRAM", 1);

    myMachine = (MyMachine)machine;
  }

  /**
   * Show help for an E-Slate component.
   */
  public final LogoObject pSHOWHELP(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    for (int i=0; i<v.size(); i++) {
      ESlateHandle h = getHandle(v.elementAt(i));
      if (h != null) {
        try {
          h.showHelpWindow();
          continue;
        }catch (Exception e) {
          throw new LanguageException(e.getMessage());
        }
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Show information about an E-Slate component.
   */
  public final LogoObject pSHOWINFO(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    for (int i=0; i<v.size(); i++) {
      ESlateHandle h = getHandle(v.elementAt(i));
      if (h != null) {
        try {
          h.showInfoDialog();
          continue;
        }catch (Exception e) {
          throw new LanguageException(e.getMessage());
        }
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Show the plug view window.
   */
  public final LogoObject pSHOWPLUGVIEW(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    try {
      ESlateHandle h = getHandle(v.firstElement());
      h.getESlateMicroworld().showPlugViewWindow();
      return LogoVoid.obj;
    }catch (Exception e) {
      throw new LanguageException(e.getMessage());
    }
  }

  /**
   * Get the plugs of an E-Slate component.
   */
  public final LogoObject pPLUGS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    try {
      ArrayList<String> plugArray = new ArrayList<String>();
      ESlateHandle h = getHandle(v.firstElement());
      Plug[] plugs = h.getPlugs();
      for (int i=0; i<plugs.length; i++) {
        traverse(plugs[i], "", plugArray);
      }
      int nPlugs = plugArray.size();
      LogoObject[] p = new LogoObject[nPlugs];
      for (int i=0; i<nPlugs; i++) {
        p[i] = new LogoWord(plugArray.get(i));
      }
      return new LogoList(p);
    }catch (Exception e) {
      throw new LanguageException(e.getMessage());
    }
  }

  /**
   * Set the folders where components can look for native programs.
   */
  public final LogoObject pSETNATIVEPROGRAMFOLDERS(InterpEnviron env,
                                                LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    //Vector v =
    //  myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    File[] f;
    if (obj[0] instanceof LogoList) {
      LogoList l = (LogoList)obj[0];
      int length = l.length();
      f = new File[length];
      for (int i=0; i<length; i++) {
        f[i] = new File(l.pickInPlace(i).toString());
      }
    }else{
      f = new File[1];
      f[0] = new File(obj[0].toString());
    }
    try {
      ESlate.setNativeProgramFolders(f);
    } catch (IllegalArgumentException iae) {
      throw new LanguageException(iae.getMessage());
    }
    return LogoVoid.obj;
  }

  /**
   * Get the folders where components can look for native programs.
   */
  public final LogoObject pNATIVEPROGRAMFOLDERS(InterpEnviron env,
                                                LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    //Vector v =
    //  myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    File[] folders = ESlate.getNativeProgramFolders();
    int nFolders = folders.length;
    LogoObject[] f = new LogoObject[nFolders];
    for (int i=0; i<nFolders; i++) {
      f[i] = new LogoWord(folders[i].getAbsolutePath());
    }
    return new LogoList(f);
  }

  /**
   * Returns the first occurrence of a native program in the list of folders
   * where components can look for native programs.
   */
  public final LogoObject pNATIVEPROGRAM(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    //Vector v =
    //  myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    String prog = obj[0].toString();
    File f = ESlate.getNativeProgram(prog);
    if (f == null) {
      throw new LanguageException(
        resources.getString("progNotFound1") +
        prog +
        resources.getString("progNotFound2")
      );
    }
    return new LogoWord(f.getAbsolutePath());
  }

  /**
   * Traverses the tree of sub-plugs of a given plug, filling a vector with the
   * names of the plug and its sub-plugs. Each name is of the form
   * name1.name2...nameN, where name1 is the name of the top level plug and
   * the renaming names are the names of the sub-plugs in the path leading to
   * the plug.
   * @param     plug    The plug whose subplug tree will be traversed.
   * @param     path    The plug path to be prepended to the name of the plug.
   *                    Invoke this method with path="" for a top level plug,
   *                    and for an Nth level plug with
   *                    path=name1.name2...nameN-1.
   * @param     a       The array list that will be filled with the names of the
   *                    plugs encountered during traversing. Invoke this method
   *                    with v=new Vector() for a top level plug.
   */
  private void traverse(Plug plug, String path, ArrayList<String> a)
  {
    if (!path.equals("")) {
      path = path + ".";
    }
    path = path + plug.getName();
    a.add(path);
    Plug[] plugs = plug.getChildPlugs();
    for (int i=0; i<plugs.length; i++) {
      traverse(plugs[i], path, a);
    }
  }

  /**
   * Connects two plugs.
   */
  public final LogoObject pCONNECT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    return connectDisconnect(env, obj, CONNECT);
  }

  /**
   * Disconnects a plug from all the plugs to which it is connected or
   * from a particular plug.
   */
  public final LogoObject pDISCONNECT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    return connectDisconnect(env, obj, DISCONNECT);
  }

  /**
   * Connects or disconnects two plugs.
   * @param     what    One of CONNECT or DISCONNECT.
   */
  private LogoObject connectDisconnect(InterpEnviron env, LogoObject obj[],
                                       int what)
    throws LanguageException
  {
    testNumParams(obj, 3);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    ESlateMicroworld mw = getHandle(v.firstElement()).getESlateMicroworld();
    String targetName = obj[1].toString();
    ESlateHandle targetHandle = mw.getESlateHandle().getChildHandle(targetName);
    if (targetHandle == null) {
      throw new LanguageException(
        resources.getString("noComponent") + " " + targetName
      );
    }
//    if (obj[2].length() < 1) {
//      throw new LanguageException(
//        resources.getString("noPlug") + " " + targetName
//      );
//    }
    Plug p2 = getPlug(targetHandle, obj[2]);
    if (p2 == null) {
      throw new LanguageException(
        resources.getString("component") + " " + targetName + " " +
        resources.getString("dontHavePlug")
      );
    }
    for (int i=0; i<v.size(); i++) {
      ESlateHandle sourceHandle = getHandle(v.elementAt(i));
      String sourceName = sourceHandle.getComponentName();
//      if (obj[0].length() < 1) {
//        throw new LanguageException(
//        resources.getString("noPlug") + " " + sourceName
//      );
//      }
      Plug p1 = getPlug(sourceHandle, obj[0]);
      if (p1 == null) {
        throw new LanguageException(
          resources.getString("component") + " " + sourceName + " " +
          resources.getString("dontHavePlug")
        );
      }
      if (what == CONNECT) {
        if (p1.containsDependent(p2) || p2.containsDependent(p1)) {
          throw new LanguageException(
            resources.getString("alreadyConnected")
          );
        }
      }else{
        if (!p1.containsDependent(p2) && !p2.containsDependent(p1)) {
          throw new LanguageException(
            resources.getString("notConnected")
          );
        }
      }
      if (what == CONNECT &&
          Plug.plugCompatibility(p1, p2) != Plug.COMPATIBLE &&
          Plug.plugCompatibility(p2, p1) != Plug.COMPATIBLE) {
        throw new LanguageException(
          resources.getString("incompatible")
        );
      }
      ESlateMicroworld.toggleConnect(p1, p2);
    }
    return LogoVoid.obj;
  }

  /**
   * Disconnect a plug from all the plugs to which it is connected.
   */
  public LogoObject pDISCONNECTPLUG(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    for (int i=0; i<v.size(); i++) {
      ESlateHandle h = getHandle(v.elementAt(i));
      String name = h.getComponentName();
      Plug p = getPlug(h, obj[0]);
      if (p == null) {
        throw new LanguageException(
          resources.getString("component") + " " + name + " " +
          resources.getString("dontHavePlug")
        );
      }
      p.disconnect();
    }
    return LogoVoid.obj;
  }

  /**
   * Returns a plug from a list of names.
   * @param     h       The E-Slate handle of the component owining the plug.
   * @param     obj     A LogoWord containing a string with the
   *                    names of the various plugs in the component's plug tree,
   *                    from the top level plug to the actual plug separated by
   *                    periods.
   */
  private Plug getPlug(ESlateHandle h, LogoObject obj)
  {
    Plug p = null;
    StringTokenizer st = new StringTokenizer(obj.toString(), ".");
    for (int i=0; st.hasMoreTokens(); i++) {
      if (i==0) {
        p = h.getPlug(st.nextToken());
      }else{
        p = p.getPlug(st.nextToken());
      }
      if (p == null) {
        break;
      }
    }
    return p;
  }

  /**
   * Returns the E-Slate handle of a given component, regardless of the
   * micorowrld in which it exists.
   * @param     component       The component whose handle should be returned.
   * @return    The requested handle.
   */
  private ESlateHandle getHandle(Object component)
  {
    ESlateMicroworld[] mWorlds = ESlateMicroworld.getMicroworlds();
    for (int j=0; j<mWorlds.length; j++) {
      ESlateHandle h = mWorlds[j].getComponentHandle(component);
      if (h != null) {
        return h;
      }
    }
    return null;
  }

  /**
   * Returns whether two plugs are connected.
   */
  public final LogoObject pCONNECTED(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 3);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    ESlateHandle sourceHandle;
    try {
      sourceHandle = getHandle(v.firstElement());
    }catch (Exception e) {
      throw new LanguageException(e.getMessage());
    }
    String sourceName = sourceHandle.getComponentName();
    Plug p1 = getPlug(sourceHandle, obj[0]);
    if (p1 == null) {
      throw new LanguageException(
        resources.getString("component") + " " + sourceName + " " +
        resources.getString("dontHavePlug")
      );
    }
    String targetName = obj[1].toString();
    ESlateMicroworld mw = sourceHandle.getESlateMicroworld();
    ESlateHandle targetHandle = mw.getESlateHandle().getChildHandle(targetName);
    if (targetHandle == null) {
      throw new LanguageException(
        resources.getString("noComponent") + " " + targetName
      );
    }
    Plug p2 = getPlug(targetHandle, obj[2]);
    if (p2 == null) {
      throw new LanguageException(
        resources.getString("component") + " " + targetName + " " +
        resources.getString("dontHavePlug")
      );
    }
    // Last two checks should be redundant, but we play it safe.
    if (p1.containsProvider(p2) || p1.containsDependent(p2) ||
        p2.containsProvider(p1) || p2.containsDependent(p1)) {
      return new LogoWord(true);
    }else{
      return new LogoWord(false);
    }
  }

  /**
   * Returns a list of the connections to a plug. This list is in the form of
   * [component_name plug_name] pairs.
   */
  public final LogoObject pLISTCONNECTIONS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    ESlateHandle sourceHandle;
    try {
      sourceHandle = getHandle(v.firstElement());
    }catch (Exception e) {
      throw new LanguageException(e.getMessage());
    }
    String sourceName = sourceHandle.getComponentName();
    Plug p = getPlug(sourceHandle, obj[0]);
    if (p == null) {
      throw new LanguageException(
        resources.getString("component") + " " + sourceName + " " +
        resources.getString("dontHavePlug")
      );
    }
    ArrayList<Plug> conn = new ArrayList<Plug>();
    Plug[] providers = p.getProviders();
    for (int i=0; i<providers.length; i++) {
      conn.add(providers[i]);
    }
    Plug[] dependents = p.getDependents();
    for (int i=0; i<dependents.length; i++) {
      conn.add(dependents[i]);
    }
    int nConn = conn.size();
    LogoObject[] lo = new LogoObject[nConn];
    for (int i=0; i<nConn; i++) {
      LogoObject[] pair = new LogoObject[2];
      Plug plug = conn.get(i);
      pair[0] = new LogoWord(plug.getHandle().getComponentName());
      pair[1] = new LogoWord(plugName(plug));
      lo[i] = new LogoList(pair);
    }
    return new LogoList(lo);
  }

  /**
   * Returns the full path name of a plug in "." notation.
   * @param     plug    The plug whose full path name will be returned.
   * @return    The requested name.
   */
  private String plugName(Plug plug)
  {
    String name = "";
    do {
      if (name.equals("")) {
        name = plug.getName();
      }else{
        name = plug.getName() + "." + name;
      }
      plug = plug.getParentPlug();
    }while (plug != null);
    return name;
  }

  /**
   * Sets whether renaming the component from the menu bar is allowed.
   */
  public final LogoObject pSETRENAMINGALLOWEDFROMBAR(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    boolean b = obj[0].toBoolean();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    for (int i=0; i<v.size(); i++) {
      ESlateHandle h = getHandle(v.elementAt(i));
      if (h != null) {
        try {
          h.getMenuPanel().setRenamingAllowed(b);
          continue;
        }catch (Exception e) {
          throw new LanguageException(e.getMessage());
        }
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Returns whether renaming the component from the menu bar is allowed.
   */
  public final LogoObject pRENAMINGALLOWEDFROMBAR(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(java.lang.Object.class);
    try {
      boolean b;
      ESlateHandle h = getHandle(v.firstElement());
      b = h.getMenuPanel().isRenamingAllowed();
      return new LogoWord(b);
    }catch (Exception e) {
      throw new LanguageException(e.getMessage());
    }
  }

  /**
   * Register a LOGO primitive using both a default english name and
   * a localized name.
   * @param     pName   The name of the primitive.
   * @param     method  The name of the method implementing the primitive.
   * @param     nArgs   The number of arguments of the method implementing the
   *                    primitive.
   */
  private void myRegisterPrimitive(String pName, String method, int nArgs)
    throws SetupException
  {
    // Register localized primitive name.
    registerPrimitive(resources.getString(pName), method, nArgs);
    // Register default english primitive name.
    if (!ESlateMicroworld.getCurrentLocale().getLanguage().equals("en")) {
      registerPrimitive(pName, method, nArgs);
    }
  }

}
