package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the master clock
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.3, 23-Jan-2008
 * @see gr.cti.eslate.masterclock.MasterClock
 */
public class MasterClockPrimitives extends PrimitiveGroup
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
   * Register primitives.
   */
  protected void setup(Machine machine, Console console)
    throws SetupException
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
        "gr.cti.eslate.scripting.logo.MasterClockResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("STARTTICK", "pSTARTTICK", 0);
    myRegisterPrimitive("STOPTICK", "pSTOPTICK", 0);
    myRegisterPrimitive("MASTERCLOCKMINIMUMSCALE", "pMASTERCLOCKMINIMUMSCALE", 0);
    myRegisterPrimitive("SETMASTERCLOCKMINIMUMSCALE", "pSETMASTERCLOCKMINIMUMSCALE", 1);
    myRegisterPrimitive("MASTERCLOCKMAXIMUMSCALE", "pMASTERCLOCKMAXIMUMSCALE", 0);
    myRegisterPrimitive("SETMASTERCLOCKMAXIMUMSCALE", "pSETMASTERCLOCKMAXIMUMSCALE", 1);
    myRegisterPrimitive("MASTERCLOCKSCALE", "pMASTERCLOCKSCALE", 0);
    myRegisterPrimitive("SETMASTERCLOCKSCALE", "pSETMASTERCLOCKSCALE", 1);
    myRegisterPrimitive("MASTERCLOCKRUNNING", "pMASTERCLOCKRUNNING", 0);

    myMachine = (MyMachine)machine;
  }

  /**
   * Start sending ticks.
   */
  public final LogoObject pSTARTTICK(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsMasterClock.class);
    for (int i=0; i<v.size(); i++) {
      ((AsMasterClock)(v.elementAt(i))).start();
    }
    return LogoVoid.obj;
  }

  /**
   * Stop sending ticks.
   */
  public final LogoObject pSTOPTICK(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsMasterClock.class);
    for (int i=0; i<v.size(); i++) {
      ((AsMasterClock)(v.elementAt(i))).stop();
    }
    return LogoVoid.obj;
  }

  /**
   * Sets the minimum value of the time scale slider.
   */
  public final LogoObject pSETMASTERCLOCKMINIMUMSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double x;
    try {
      x = obj[0].toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badMinScale"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsMasterClock.class);
    for (int i=0; i<v.size(); i++) {
      ((AsMasterClock)(v.elementAt(i))).setMinimumTimeScale(x);
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the minimum value of the time scale slider.
   */
  public final LogoObject pMASTERCLOCKMINIMUMSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsMasterClock mc =
      (AsMasterClock)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsMasterClock.class
      );
    return new LogoWord(mc.getMinimumTimeScale());
  }

  /**
   * Sets the maximum value of the time scale slider.
   */
  public final LogoObject pSETMASTERCLOCKMAXIMUMSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double x;
    try {
      x = obj[0].toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badMaxScale"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsMasterClock.class);
    for (int i=0; i<v.size(); i++) {
      ((AsMasterClock)(v.elementAt(i))).setMaximumTimeScale(x);
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the maximum value of the time scale slider.
   */
  public final LogoObject pMASTERCLOCKMAXIMUMSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsMasterClock mc =
      (AsMasterClock)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsMasterClock.class
      );
    return new LogoWord(mc.getMaximumTimeScale());
  }

  /**
   * Sets the value of the time scale slider.
   */
  public final LogoObject pSETMASTERCLOCKSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double x;
    try {
      x = obj[0].toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badScale"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsMasterClock.class);
    for (int i=0; i<v.size(); i++) {
      ((AsMasterClock)(v.elementAt(i))).setTimeScale(x);
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the value of the time scale slider.
   */
  public final LogoObject pMASTERCLOCKSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsMasterClock mc =
      (AsMasterClock)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsMasterClock.class
      );
    return new LogoWord(mc.getTimeScale());
  }

  /**
   * Checks whether the master clock is running.
   */
  public final LogoObject pMASTERCLOCKRUNNING(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsMasterClock mc =
      (AsMasterClock)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsMasterClock.class
      );
    return new LogoWord(mc.isRunning());
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
