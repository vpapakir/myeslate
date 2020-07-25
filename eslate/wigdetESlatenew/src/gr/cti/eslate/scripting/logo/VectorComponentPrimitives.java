package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the vector
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.2, 23-Jan-2008
 * @see gr.cti.eslate.vector.VectorComponent
 */
public class VectorComponentPrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.VectorResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("SETVECTOR", "pSETVECTOR", 1);
    myRegisterPrimitive("VECTOR", "pVECTOR", 0);
    myRegisterPrimitive("SETVECTORNORTH", "pSETVECTORNORTH", 1);
    myRegisterPrimitive("VECTORNORTH", "pVECTORNORTH", 0);
    myRegisterPrimitive("SETVECTOREAST", "pSETVECTOREAST", 1);
    myRegisterPrimitive("VECTOREAST", "pVECTOREAST", 0);
    myRegisterPrimitive("SETVECTORPOLAR", "pSETVECTORPOLAR", 1);
    myRegisterPrimitive("VECTORPOLAR", "pVECTORPOLAR", 0);
    myRegisterPrimitive("SETVECTORLENGTH", "pSETVECTORLENGTH", 1);
    myRegisterPrimitive("VECTORLENGTH", "pVECTORLENGTH", 0);
    myRegisterPrimitive("SETVECTORANGLE", "pSETVECTORANGLE", 1);
    myRegisterPrimitive("VECTORANGLE", "pVECTORANGLE", 0);
    myRegisterPrimitive("SETVECTORSCALE", "pSETVECTORSCALE", 1);
    myRegisterPrimitive("VECTORSCALE", "pVECTORSCALE", 0);
    myRegisterPrimitive("SETVECTORPRECISION", "pSETVECTORPRECISION", 1);
    myRegisterPrimitive("VECTORPRECISION", "pVECTORPRECISION", 0);

    myMachine = (MyMachine)machine;
  }

  /**
   * Set the value of the vector using Cartesian coordinates.
   */
  public final LogoObject pSETVECTOR(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    if (obj[0].length() != 2) {
      throw new LanguageException(resources.getString("badCoords"));
    }
    double x, y;
    try {
      x = ((LogoList)obj[0]).pickInPlace(0).toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badX"));
    }
    try {
      y = ((LogoList)obj[0]).pickInPlace(1).toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badY"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setNE(x, y);
    }
    return LogoVoid.obj;
  }

  /**
   * Get the Cartesian coordinates of the vector.
   */
  public final LogoObject pVECTOR(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    LogoObject result[] = new LogoObject[2];
    result[0] = new LogoWord(vc.getEast());
    result[1] = new LogoWord(vc.getNorth());
    return new LogoList(result);
  }

  /**
   * Set the value of the vector's vertical component.
   */
  public final LogoObject pSETVECTORNORTH(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double y = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setNorth(y);
    }
    return LogoVoid.obj;
  }

  /**
   * Get vector's vertical component.
   */
  public final LogoObject pVECTORNORTH(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    return new LogoWord(vc.getNorth());
  }

  /**
   * Set the value of the vector's horizontal component.
   */
  public final LogoObject pSETVECTOREAST(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double x = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setEast(x);
    }
    return LogoVoid.obj;
  }

  /**
   * Get vector's horizontal component.
   */
  public final LogoObject pVECTOREAST(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    return new LogoWord(vc.getEast());
  }

  /**
   * Set the value of the vector using polar coordinates.
   */
  public final LogoObject pSETVECTORPOLAR(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    if (obj[0].length() != 2) {
      throw new LanguageException(resources.getString("badCoords"));
    }
    double l, a;
    try {
      l = ((LogoList)obj[0]).pickInPlace(0).toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badL"));
    }
    try {
      a = ((LogoList)obj[0]).pickInPlace(1).toNumber();
    } catch (LanguageException le) {
      throw new LanguageException(resources.getString("badA"));
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setLA(l, a);
    }
    return LogoVoid.obj;
  }

  /**
   * Get the polar coordinates of the vector.
   */
  public final LogoObject pVECTORPOLAR(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    LogoObject result[] = new LogoObject[2];
    result[0] = new LogoWord(vc.getLength());
    result[1] = new LogoWord(vc.getAngle());
    return new LogoList(result);
  }

  /**
   * Set the value of the vector's length.
   */
  public final LogoObject pSETVECTORLENGTH(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double l = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setLength(l);
    }
    return LogoVoid.obj;
  }

  /**
   * Get vector's length.
   */
  public final LogoObject pVECTORLENGTH(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    return new LogoWord(vc.getLength());
  }

  /**
   * Set the value of the vector's angle.
   */
  public final LogoObject pSETVECTORANGLE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double a = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setAngle(a);
    }
    return LogoVoid.obj;
  }

  /**
   * Get vector's angle.
   */
  public final LogoObject pVECTORANGLE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    return new LogoWord(vc.getAngle());
  }

  /**
   * Set the vector's scale.
   */
  public final LogoObject pSETVECTORSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    double s = obj[0].toNumber();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setScale(s);
    }
    return LogoVoid.obj;
  }

  /**
   * Get vector's scale.
   */
  public final LogoObject pVECTORSCALE(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    return new LogoWord(vc.getScale());
  }

  /**
   * Sets the number of digits displayed after the decimal point.
   */
  public final LogoObject pSETVECTORPRECISION(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    int p = obj[0].toInteger();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class);
    for (int i=0; i<v.size(); i++) {
      ((AsVectorComponent)(v.elementAt(i))).setPrecision(p);
    }
    return LogoVoid.obj;
  }

  /**
   * Returns the number of digits displayed after the decimal point.
   */
  public final LogoObject pVECTORPRECISION(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsVectorComponent vc =
      (AsVectorComponent)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsVectorComponent.class
      );
    return new LogoWord(vc.getPrecision());
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
