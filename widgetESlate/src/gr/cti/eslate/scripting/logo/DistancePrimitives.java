package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the distance
 * control component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jun-2008
 * @see gr.cti.eslate.distance.Distance
 */
public class DistancePrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.DistanceResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("GODISTANCE", "pGODISTANCE", 0);
    myRegisterPrimitive("DISTANCE", "pDISTANCE", 0);
    myRegisterPrimitive("SETDISTANCE", "pSETDISTANCE", 1);
    myRegisterPrimitive("DISTANCEUNIT", "pDISTANCEUNIT", 0);
    myRegisterPrimitive("SETDISTANCEUNIT", "pSETDISTANCEUNIT", 1);
    myRegisterPrimitive("DISTANCEUNITS", "pDISTANCEUNITS", 0);
    myRegisterPrimitive("STOPATLANDMARKS", "pSTOPATLANDMARKS", 0);
    myRegisterPrimitive("SETSTOPATLANDMARKS", "pSETSTOPATLANDMARKS", 1);

    myMachine = (MyMachine)machine;
  }

  /**
   * Start moving.
   */
  public final LogoObject pGODISTANCE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsDistance.class);
    for (int i=0; i<v.size(); i++) {
      ((AsDistance)(v.elementAt(i))).go();
    }
    return LogoVoid.obj;
  }

  /**
   * Set the distance to travel.
   */
  public final LogoObject pSETDISTANCE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double x;
    try {
      x = obj[0].toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badDistance"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsDistance.class);
    for (int i=0; i<v.size(); i++) {
      ((AsDistance)(v.elementAt(i))).setDistance(x);
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the distance to travel.
   */
  public final LogoObject pDISTANCE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsDistance d =
      (AsDistance)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsDistance.class
      );
    return new LogoWord(d.getDistance());
  }

  /**
   * Specify the unit in which the distance to travel is measured.
   */
  public final LogoObject pSETDISTANCEUNIT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsDistance.class);
    for (int i=0; i<v.size(); i++) {
      try {
        ((AsDistance)(v.elementAt(i))).setUnit(s);
      } catch (Exception e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the unit in which the distance to travel is measured.
   */
  public final LogoObject pDISTANCEUNIT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsDistance d =
      (AsDistance)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsDistance.class
      );
    return new LogoWord(d.getUnit());
  }

  /**
   * Returns the supported units in which the distance to travel can be
   * measured.
   */
  public final LogoObject pDISTANCEUNITS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsDistance d =
      (AsDistance)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsDistance.class
      );
    String[] s = d.getUnits();
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
        gr.cti.eslate.scripting.AsDistance.class);
    for (int i=0; i<v.size(); i++) {
      ((AsDistance)(v.elementAt(i))).setStopAtLandmarks(b);
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
    AsDistance d =
      (AsDistance)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsDistance.class
      );
    return new LogoWord(d.getStopAtLandmarks());
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
