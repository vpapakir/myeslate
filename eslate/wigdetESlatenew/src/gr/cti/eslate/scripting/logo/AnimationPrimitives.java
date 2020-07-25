package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.animation.Animation;
import gr.cti.eslate.base.ESlateMicroworld;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;

/**
 * This class describes the Logo primitives implemented by the animation
 * component.
 *
 * @author	Augustine Grillakis
 * @version	1.0.0, 24-Jun-2002
 * @see	gr.cti.eslate.animation.Animation
 */
public class AnimationPrimitives extends PrimitiveGroup
{
  /**
   * Localized resources.
   */
  private static ResourceBundle resources = null;

  /**
   * Required for scripting.
   */
  MyMachine myMachine;

  protected void setup(Machine machine, Console console)
    throws SetupException
  {
    if (resources == null) {
      resources = ResourceBundle.getBundle(
	"gr.cti.eslate.scripting.logo.AnimationPrimitivesResource",
	Locale.getDefault()
      );
    }
    myRegisterPrimitive("PLAYFROMFRAME", "pPLAYFROMFRAME", 1);
    myRegisterPrimitive("PLAYFROMFRAMETOFRAME", "pPLAYFROMFRAMETOFRAME", 2);
    myRegisterPrimitive("PLAYFROMLABEL", "pPLAYFROMLABEL", 1);
    myRegisterPrimitive("PLAYFROMLABELTOLABEL", "pPLAYFROMLABELTOLABEL", 2);
    myRegisterPrimitive("GOTOFRAME", "pGOTOFRAME", 1);

    myMachine = (MyMachine)machine;
  }

  public final LogoObject pPLAYFROMFRAME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    int s = obj[0].toInteger();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.animation.Animation.class
      );
    for (int i=0; i<v.size(); i++) {
      ((Animation)(v.elementAt(i))).playFromFrame(s);
    }
    return LogoVoid.obj;
  }

  public final LogoObject pPLAYFROMFRAMETOFRAME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 2);
    int s1 = obj[0].toInteger();
    int s2 = obj[1].toInteger();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.animation.Animation.class
      );
    for (int i=0; i<v.size(); i++) {
      ((Animation)(v.elementAt(i))).playFromFrameToFrame(s1, s2);
    }
    return LogoVoid.obj;
  }

  public final LogoObject pPLAYFROMLABEL(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.animation.Animation.class
      );
    for (int i=0; i<v.size(); i++) {
      ((Animation)(v.elementAt(i))).playFromLabel(s);
    }
    return LogoVoid.obj;
  }

  public final LogoObject pPLAYFROMLABELTOLABEL(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 2);
    String s1 = obj[0].toString();
    String s2 = obj[1].toString();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.animation.Animation.class
      );
    for (int i=0; i<v.size(); i++) {
      ((Animation)(v.elementAt(i))).playFromLabelToLabel(s1, s2);
    }
    return LogoVoid.obj;
  }

  public final LogoObject pGOTOFRAME(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    int s = obj[0].toInteger();
    Vector v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.animation.Animation.class
      );
    for (int i=0; i<v.size(); i++) {
      ((Animation)(v.elementAt(i))).goToFrame(s);
    }
    return LogoVoid.obj;
  }

  /**
   * Register a LOGO primitive using both a default english name and
   * a localized name.
   * @param     pName   The name of the primitive.
   * @param     method  The name of the method implementing the primitive.
   * @param     nArgs   The number of arguments of the method implementing the
   *                    primitive.
   * @throws    SetupException
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