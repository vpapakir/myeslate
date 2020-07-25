package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the registry
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 25-May-2006
 * @see gr.cti.eslate.registry.Registry
 */
public class RegistryPrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.RegistryResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("REGISTRY.REGISTER", "pREGISTER", 3);
    myRegisterPrimitive("REGISTRY.UNREGISTER", "pUNREGISTER", 1);
    myRegisterPrimitive("REGISTRY.SETCOMMENT", "pSETCOMMENT", 2);
    myRegisterPrimitive("REGISTRY.LOOKUP", "pLOOKUP", 1);

    myMachine = (MyMachine)machine;
  }

  /**
   * Register a variable.
   */
  public final LogoObject pREGISTER(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 3);
    String name = obj[0].toString();
    Object value = obj[1].toString();
    boolean persistent = obj[2].toBoolean();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsRegistry.class);
    for (int i=0; i<v.size(); i++) {
      ((AsRegistry)(v.elementAt(i))).registerVariable(name, value, persistent);
    }
    return LogoVoid.obj;
  }

  /**
   * Unregister a variable.
   */
  public final LogoObject pUNREGISTER(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String name = obj[0].toString();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsRegistry.class);
    for (int i=0; i<v.size(); i++) {
      ((AsRegistry)(v.elementAt(i))).unregisterVariable(name);
    }
    return LogoVoid.obj;
  }

  /**
   * Associate a comment with a variable.
   */
  public final LogoObject pSETCOMMENT(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 2);
    String name = obj[0].toString();
    String comment = obj[1].toString();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsRegistry.class);
    for (int i=0; i<v.size(); i++) {
      ((AsRegistry)(v.elementAt(i))).setComment(name, comment);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current time.
   */
  public final LogoObject pLOOKUP(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String name = obj[0].toString();
    AsRegistry r =
      (AsRegistry)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsRegistry.class
      );
    return new LogoWord(r.lookupVariable(name).toString());
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
