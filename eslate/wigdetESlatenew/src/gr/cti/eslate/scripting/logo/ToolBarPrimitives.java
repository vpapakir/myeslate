package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.scripting.*;

/**
 * This class describes the Logo primitives implemented by the toolbar
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.4, 23-Jan-2008
 * @see gr.cti.eslate.eslateToolBar.ESlateToolBar
 */
public class ToolBarPrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.ToolBarResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }
    System.out.println("Registering primitives");
    try {
    myRegisterPrimitive("TOOLBAR.SHOWTOOL", "pSHOWTOOL", 1);
    myRegisterPrimitive("TOOLBAR.HIDETOOL", "pHIDETOOL", 1);
    myRegisterPrimitive("TOOLBAR.SHOWGROUP", "pSHOWGROUP", 1);
    myRegisterPrimitive("TOOLBAR.HIDEGROUP", "pHIDEGROUP", 1);
    } catch (Exception e) {
      e.printStackTrace();
    }

    myMachine = (MyMachine)machine;
  }

  /**
   * Specifies that the components in a visual group will be drawn on the
   * toolbar.
   */
  public final LogoObject pSHOWGROUP(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsToolBar.class
      );
    for (int i=0; i<v.size(); i++) {
      try {
        AsToolBar tb = (AsToolBar)(v.elementAt(i));
        tb.setGroupVisible(s, true);
        tb.repaint();
      } catch (Exception e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Specifies that the components in a visual group will not be drawn on the
   * toolbar.
   */
  public final LogoObject pHIDEGROUP(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsToolBar.class
      );
    for (int i=0; i<v.size(); i++) {
      try {
        AsToolBar tb = (AsToolBar)(v.elementAt(i));
        tb.setGroupVisible(s, false);
        tb.repaint();
      } catch (Exception e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Specifies that a component will be drawn on the toolbar.
   */
  public final LogoObject pSHOWTOOL(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsToolBar.class
      );
    for (int i=0; i<v.size(); i++) {
      try {
        AsToolBar tb = (AsToolBar)(v.elementAt(i));
        tb.setToolVisible(s, true);
        tb.repaint();
      } catch (Exception e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  /**
   * Specifies that a component will not be drawn on the toolbar.
   */
  public final LogoObject pHIDETOOL(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsToolBar.class
      );
    for (int i=0; i<v.size(); i++) {
      try {
        AsToolBar tb = (AsToolBar)(v.elementAt(i));
        tb.setToolVisible(s, false);
        tb.repaint();
      } catch (Exception e) {
        throw new LanguageException(e.getMessage());
      }
    }
    return LogoVoid.obj;
  }

  /*
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
