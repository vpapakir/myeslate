package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the
 * travel-for-a-given-time control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see gr.cti.eslate.time.Time
 */
public class TimePrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.TimeResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("GOTIME", "pGOTIME", 0);
    myRegisterPrimitive("TIME", "pTIME", 0);
    myRegisterPrimitive("SETTIME", "pSETTIME", 1);
    myRegisterPrimitive("TIMEUNIT", "pTIMEUNIT", 0);
    myRegisterPrimitive("SETTIMEUNIT", "pSETTIMEUNIT", 1);
    myRegisterPrimitive("TIMEUNITS", "pTIMEUNITS", 0);
    myRegisterPrimitive("STOPATLANDMARKS", "pSTOPATLANDMARKS", 0);
    myRegisterPrimitive("SETSTOPATLANDMARKS", "pSETSTOPATLANDMARKS", 1);

    myMachine = (MyMachine)machine;
  }

  /**
   * Start moving.
   */
  public final LogoObject pGOTIME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsTime.class);
    for (int i=0; i<v.size(); i++) {
      ((AsTime)(v.elementAt(i))).go();
    }
    return LogoVoid.obj;
  }

  /**
   * Set the time to travel.
   */
  public final LogoObject pSETTIME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double x;
    try {
      x = obj[0].toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badTime"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsTime.class);
    for (int i=0; i<v.size(); i++) {
      ((AsTime)(v.elementAt(i))).setTime(x);
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the time to travel.
   */
  public final LogoObject pTIME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsTime t = (AsTime)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsTime.class
    );
    return new LogoWord(t.getTime());
  }

  /**
   * Specify the unit in which the time to travel is measured.
   */
  public final LogoObject pSETTIMEUNIT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsTime.class);
    for (int i=0; i<v.size(); i++) {
      try {
        ((AsTime)(v.elementAt(i))).setUnit(s);
      } catch (Exception e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the unit in which the time to travel is measured.
   */
  public final LogoObject pTIMEUNIT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsTime t = (AsTime)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsTime.class
    );
    return new LogoWord(t.getUnit());
  }

  /**
   * Returns the supported units in which the time to travel can be
   * measured.
   */
  public final LogoObject pTIMEUNITS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsTime t = (AsTime)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsTime.class
    );
    String[] s = t.getUnits();
    LogoObject[] lo = new LogoObject[s.length];
    for (int i=0; i<s.length; i++) {
      lo[i] = new LogoWord(s[i]);
    }
    return new LogoList(lo);
  }

  /**
   * Specify whether we should stop at landmarks.
   */
  public final LogoObject pSETSTOPATLANDMARKS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    boolean b = obj[0].toBoolean();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsTime.class);
    for (int i=0; i<v.size(); i++) {
      ((AsTime)(v.elementAt(i))).setStopAtLandmarks(b);
    }
    return LogoVoid.obj;
  }

  /**
   * Checks whether we should stop at landmarks.
   */
  public final LogoObject pSTOPATLANDMARKS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsTime t = (AsTime)myMachine.componentPrimitives.getFirstComponentToTell(
      gr.cti.eslate.scripting.AsTime.class
    );
    return new LogoWord(t.getStopAtLandmarks());
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
