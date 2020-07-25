package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;

import gr.cti.eslate.navigator.models.INavigator;

/**
 * This class describes the Logo primitives implemented by the navigator
 * component.
 *
 * @author      George Birbilis
 * @author      Kriton Kyrimis
 * @version     3.0.4, 11-Dec-2007
 */
public class NavigatorPrimitives extends PrimitiveGroup
{

  MyMachine myMachine;
  Console console;

  protected void setup(Machine machine, Console console) throws SetupException
  {
    registerPrimitive("NAVIGATOR.SETLOCATION", "pSETLOCATION", 1);
    registerPrimitive("NAVIGATOR.LOCATION", "pLOCATION", 0);
    // 24Jun2000: renamed from "GOFORWARD" to "FORWARD"
    registerPrimitive("NAVIGATOR.FORWARD", "pFORWARD", 0);
    // 24Jun2000: renamed from "GOBACK" to "BACK"
    registerPrimitive("NAVIGATOR.BACK", "pBACK", 0);
    // Calling this GOHOME, since HOME might be considered by the user as a
    // function returning the current HOME setting .
    registerPrimitive("NAVIGATOR.GOHOME", "pGOHOME", 0);
    registerPrimitive("NAVIGATOR.STOP", "pSTOP", 0); // 15Mar2000
    registerPrimitive("NAVIGATOR.REFRESH", "pREFRESH", 0); //24Jun2000        
        //...//
    myMachine = (MyMachine)machine;
    this.console = console;
    if (console != null) {
      console.putSetupMessage("Loaded ESlate's Navigator component primitives");
    }
    /**/
  }

  // Utility method //

  private Vector<?> getINavigators()
  {
    return myMachine.componentPrimitives.getComponentsToTell(
      gr.cti.eslate.navigator.models.INavigator.class
    );
  }

  // SETLOCATION //

  public final LogoObject pSETLOCATION(InterpEnviron interpenviron,
                                       LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 1);
    String location = alogoobject[0].toString();
    Vector<?> v = getINavigators();
    boolean errors = false;
    for (int i=0; i<v.size(); i++) {
      try {
       ((INavigator)v.elementAt(i)).setCurrentLocation(location);
      } catch (Exception e) {
        errors = true;
      }
      if(errors) {
        console.putLine(
          "Some components failed to navigate to the given location"
        );
      }
    }
    return LogoVoid.obj;
  }

  // LOCATION //

  public final LogoObject pLOCATION(InterpEnviron interpenviron,
                                    LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 0);
    Vector<?> v = getINavigators();
    String location;
    try {
      location = ((INavigator)v.firstElement()).getCurrentLocation();
    } catch (NoSuchElementException e) {
      throw new LanguageException("There is no object to TELL this to");
    }
    return new LogoWord(location);
  }

  // FORWARD //

  public final LogoObject pFORWARD(InterpEnviron interpenviron,
                                   LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 0);
    Vector<?> v = getINavigators();
    boolean errors = false;
    for (int i=0; i<v.size(); i++) {
      try {
        ((INavigator)v.elementAt(i)).forward();
      } catch(Exception e) {
        errors = true;
      }
      if (errors) {
        console.putLine("Some components failed to navigate forward");
      }
    }
    return LogoVoid.obj;
  }

  // BACK //

  public final LogoObject pBACK(InterpEnviron interpenviron,
                                LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 0);
    Vector<?> v = getINavigators();
    boolean errors = false;
    for (int i=0; i<v.size(); i++) {
      try {
        ((INavigator)v.elementAt(i)).back();
      } catch(Exception e) {
        errors = true;
      }
      if (errors) {
        console.putLine("Some components failed to navigate back");
      }
    }
    return LogoVoid.obj;
  }

  // GOHOME //

  public final LogoObject pGOHOME(InterpEnviron interpenviron,
                                  LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 0);
    Vector<?> v = getINavigators();
    boolean errors = false;
    for (int i=0; i<v.size(); i++) {
      try {
        ((INavigator)v.elementAt(i)).home();
      } catch (Exception e) {
        errors = true;
      }
      if (errors) {
        console.putLine("Some components failed to navigate to GOHOME");
      }
    }
    return LogoVoid.obj;
  }

  // STOP //

  public final LogoObject pSTOP(InterpEnviron interpenviron,
                                LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 0);
    Vector<?> v = getINavigators();
    for(int i=0; i<v.size(); i++) {
      ((INavigator)v.elementAt(i)).stop();
    }
    return LogoVoid.obj;
  }

  // REFRESH //

  public final LogoObject pREFRESH(InterpEnviron interpenviron,
                                   LogoObject alogoobject[])
    throws LanguageException
  {
    testNumParams(alogoobject, 0);
    Vector<?> v = getINavigators();
    for (int i=0; i<v.size(); i++) {
      ((INavigator)v.elementAt(i)).refresh();
    }
    return LogoVoid.obj;
  }

}
