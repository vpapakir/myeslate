package gr.cti.eslate.scripting.logo;

import java.util.*;
import virtuoso.logo.*;
import gr.cti.eslate.scripting.*;
import gr.cti.eslate.base.*;
import gr.cti.eslate.utils.*;
import gr.cti.eslate.sharedObject.*;

/**
 * This class describes the Logo primitives implemented by the clock
 * component.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.1, 23-Jan-2008
 * @see gr.cti.eslate.steering.Steering
 */
public class SteeringPrimitives extends PrimitiveGroup
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
        "gr.cti.eslate.scripting.logo.SteeringResource",
        ESlateMicroworld.getCurrentLocale()
      );
    }

    myRegisterPrimitive("SETSTEERINGDIRECTION", "pSETSTEERINGDIRECTION", 1);
    myRegisterPrimitive("STEERINGDIRECTION", "pSTEERINGDIRECTION", 0);
    myRegisterPrimitive("STEERINGGO", "pSTEERINGGO", 0);

    myMachine = (MyMachine)machine;
  }

  /**
   * Set a new direction.
   */
  public final LogoObject pSETSTEERINGDIRECTION(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 1);
    String s = obj[0].toString();
    int dir;
    if (equalString(s, "N")) {
      dir = Direction.N;
    }else{
      if (equalString(s, "NE")) {
        dir = Direction.NE;
      }else{
        if (equalString(s, "E")) {
          dir = Direction.E;
        }else{
          if (equalString(s, "SE")) {
            dir = Direction.SE;
          }else{
            if (equalString(s, "S")) {
              dir = Direction.S;
            }else{
              if (equalString(s, "SW")) {
                dir = Direction.SW;
              }else{
                if (equalString(s, "W")) {
                  dir = Direction.W;
                }else{
                  if (equalString(s, "NW")) {
                    dir = Direction.NW;
                  }else{
                    throw new LanguageException(
                      resources.getString("badDir") + " " +
                      resources.getString("N") + ", " +
                      resources.getString("NE") + ", " +
                      resources.getString("E") + ", " +
                      resources.getString("SE") + ", " +
                      resources.getString("S") + ", " +
                      resources.getString("SW") + ", " +
                      resources.getString("W") + ", " +
                      resources.getString("NW")+"."
                    );
                  }
                }
              }
            }
          }
        }
      }
    }
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSteering.class);
    for (int i=0; i<v.size(); i++) {
      ((AsSteering)(v.elementAt(i))).setDirection(dir);
    }
    return LogoVoid.obj;
  }

  /**
   * Return the current direction.
   */
  public final LogoObject pSTEERINGDIRECTION(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    AsSteering st =
      (AsSteering)myMachine.componentPrimitives.getFirstComponentToTell(
        gr.cti.eslate.scripting.AsSteering.class
      );
    int dir = st.getDirection();
    String s = null;
    switch (dir) {
      case Direction.N:
        s = resources.getString("N");
        break;
      case Direction.NE:
        s = resources.getString("NE");
        break;
      case Direction.E:
        s = resources.getString("E");
        break;
      case Direction.SE:
        s = resources.getString("SE");
        break;
      case Direction.S:
        s = resources.getString("S");
        break;
      case Direction.SW:
        s = resources.getString("SW");
        break;
      case Direction.W:
        s = resources.getString("W");
        break;
      case Direction.NW:
        s = resources.getString("NW");
        break;
    }
    return new LogoWord(s);
  }

  /**
   * Start moving.
   */
  public final LogoObject pSTEERINGGO(InterpEnviron env, LogoObject obj[])
    throws LanguageException
  {
    testNumParams(obj, 0);
    Vector<?> v =
      myMachine.componentPrimitives.getComponentsToTell(
        gr.cti.eslate.scripting.AsSteering.class);
    for (int i=0; i<v.size(); i++) {
      ((AsSteering)(v.elementAt(i))).go();
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

  /**
   * Compare a string with the English and localized version of another
   * string. Comparisons are made ignoring case.
   * @param     s1      The string to compare.
   * @param     s2      The english version of the string to which the first
   *                    string will be compared.
   * @return    True if s1 is equal to either the English or the localized
   *            version of s2, false otherwise.
   */
  private boolean equalString(String s1, String s2)
  {
    if (ESlateStrings.areEqualIgnoreCase(s1, resources.getString(s2),
                                         ESlateMicroworld.getCurrentLocale())) {
      return true;
    }
    if (!ESlateMicroworld.getCurrentLocale().getLanguage().equals("en")) {
      return (s1.equalsIgnoreCase(s2));
    }else{
      return false;
    }
  }
}
