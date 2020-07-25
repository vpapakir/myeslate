package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.utils.TimeCount;
import gr.cti.eslate.base.*;


/**
 * This class describes the Logo primitives implemented by the chronometer
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2007
 * @see gr.cti.eslate.chronometer.Chronometer
 */
public class ChronometerPrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.ChronometerResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("STARTCHRONOMETER", "pSTART", 0);
    myRegisterPrimitive("STOPCHRONOMETER", "pSTOP", 0);
    myRegisterPrimitive("RESETCHRONOMETER", "pRESET", 0);
    myRegisterPrimitive("CHRONOMETERTIME", "pCHRONOMETERTIME", 0);
    myRegisterPrimitive("SETCHRONOMETERTIME", "pSETCHRONOMETERTIME", 1);
    myRegisterPrimitive("CHRONOMETERMILLISECONDS", "pCHRONOMETERMILLISECONDS", 0);
    myRegisterPrimitive("SETCHRONOMETERMILLISECONDS", "pSETCHRONOMETERMILLISECONDS", 1);
    myRegisterPrimitive("CHRONOMETERSECONDS", "pCHRONOMETERSECONDS", 0);
    myRegisterPrimitive("SETCHRONOMETERSECONDS", "pSETCHRONOMETERSECONDS", 1);
    myRegisterPrimitive("CHRONOMETERMINUTES", "pCHRONOMETERMINUTES", 0);
    myRegisterPrimitive("SETCHRONOMETERMINUTES", "pSETCHRONOMETERMINUTES", 1);
    myRegisterPrimitive("CHRONOMETERHOURS", "pCHRONOMETERHOURS", 0);
    myRegisterPrimitive("SETCHRONOMETERHOURS", "pSETCHRONOMETERHOURS", 1);
    myRegisterPrimitive("CHRONOMETERRUNNING", "pCHRONOMETERRUNNING", 0);

    myMachine = (MyMachine)machine;
  }

  /**
   * Start measuring elspased time.
   */
  public final LogoObject pSTART(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).start();
    }
    return LogoVoid.obj;
  }

  /**
   * Stop measuring elspased time.
   */
  public final LogoObject pSTOP(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).stop();
    }
    return LogoVoid.obj;
  }

  /**
   * Reset the chronometer display.
   */
  public final LogoObject pRESET(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).reset();
    }
    return LogoVoid.obj;
  }

  /**
   * Set the elapsed time to a given value.
   */
  public final LogoObject pSETCHRONOMETERTIME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    TimeCount t = null;
    t = ChronometerTimeParse.timeParse(obj[0]);
    if (t == null) {
      throw new LanguageException(resources.getString("badTime"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).setTime(t);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current time.
   */
  public final LogoObject pCHRONOMETERTIME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsChronometer c =
      (AsChronometer)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsChronometer.class
      );
    LogoObject lo[] = new LogoObject[4];
    TimeCount t = c.getTime();
    lo[0] = new LogoWord(t.hour);
    lo[1] = new LogoWord(t.min);
    lo[2] = new LogoWord(t.sec);
    lo[3] = new LogoWord(t.usec / 1000);
    return new LogoList(lo);
  }

  /**
   * Set the elapsed time to a given value in milliseconds.
   */
  public final LogoObject pSETCHRONOMETERMILLISECONDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double s = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).setMilliseconds(s);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current time in milliseconds.
   */
  public final LogoObject pCHRONOMETERMILLISECONDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsChronometer c =
      (AsChronometer)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsChronometer.class
      );
    return new LogoWord(c.getMilliseconds());
  }

  /**
   * Set the elapsed time to a given value in seconds.
   */
  public final LogoObject pSETCHRONOMETERSECONDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double s = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).setMilliseconds(s * 1000.0);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current time in seconds.
   */
  public final LogoObject pCHRONOMETERSECONDS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsChronometer c =
      (AsChronometer)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsChronometer.class
      );
    return new LogoWord(c.getMilliseconds() / 1000.0);
  }

  /**
   * Set the elapsed time to a given value in minutes.
   */
  public final LogoObject pSETCHRONOMETERMINUTES(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double s = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).setMilliseconds(s * 1000.0 * 60.0);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current time in minutes.
   */
  public final LogoObject pCHRONOMETERMINUTES(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsChronometer c =
      (AsChronometer)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsChronometer.class
      );
    return new LogoWord(c.getMilliseconds() / (1000.0 * 60.0));
  }

  /**
   * Set the elapsed time to a given value in hours.
   */
  public final LogoObject pSETCHRONOMETERHOURS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double s = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsChronometer.class);
    for (int i=0; i<v.size(); i++) {
      ((AsChronometer)(v.elementAt(i))).setMilliseconds(s * 1000.0 * 60.0 * 60.0);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current time in hours.
   */
  public final LogoObject pCHRONOMETERHOURS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsChronometer c =
      (AsChronometer)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsChronometer.class
      );
    return new LogoWord(c.getMilliseconds() / (1000.0 * 60.0 * 60.0));
  }

  /**
   * Check whether the chronometer is running.
   */
  public final LogoObject pCHRONOMETERRUNNING(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsChronometer c =
      (AsChronometer)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsChronometer.class
      );
    return new LogoWord(c.isRunning());
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
