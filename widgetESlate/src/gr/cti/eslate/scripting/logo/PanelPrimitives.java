package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;

/**
 * This class describes the Logo primitives implemented by the panel
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 24-May-2006
 * @see gr.cti.eslate.panel.PanelComponent
 */
public class PanelPrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.PanelResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("PANEL.LISTCOMPONENTS", "pLISTCOMPONENTS", 0);

    myMachine = (MyMachine)machine;
  }

  /**
   * Get the names of all hosted E-Slate components.
   */
  public final LogoObject pLISTCOMPONENTS(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsPanel p =
      (AsPanel)myMachine.componentPrimitives.getFirstComponentToTell(
        AsPanel.class
      );
    ESlateHandle[] h = p.listHostedESlateHandles();
    int n = h.length;
    LogoObject[] ob = new LogoObject[n];
    for (int i=0; i<n; i++) {
      ob[i] = new LogoWord(h[i].getComponentPathName());
    }
    return new LogoList(ob);
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
