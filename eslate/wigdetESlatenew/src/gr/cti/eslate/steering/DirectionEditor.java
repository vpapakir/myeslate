package gr.cti.eslate.steering;

import java.util.*;

import gr.cti.eslate.base.*;
import gr.cti.eslate.sharedObject.*;
import gr.cti.eslate.utils.*;

/**
 * Custom editor for the Steering component's "direction" property.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 22-May-2006
 */
public class DirectionEditor extends TaggedIntegerPropertyEditor
{
  /**
   * Construct the custom editor.
   */
  public DirectionEditor()
  {
    super("direction", getDirs(), getDirNames());
  }

  /**
   * Returns an array with the possible directions.
   * @return    The requested array.
   */
  private static int[] getDirs()
  {
    int[] dir = new int[8];
    dir[0] = Direction.N;
    dir[1] = Direction.NE;
    dir[2] = Direction.E;
    dir[3] = Direction.SE;
    dir[4] = Direction.S;
    dir[5] = Direction.SW;
    dir[6] = Direction.W;
    dir[7] = Direction.NW;
    return dir;
  }

  /**
   * Returns an array with the names of the possible directions.
   * @return    The requested array.
   */
  private static String[] getDirNames()
  {
    ResourceBundle resources = ResourceBundle.getBundle(
      "gr.cti.eslate.steering.SteeringResource",
      ESlateMicroworld.getCurrentLocale()
    );
    String[] name = new String[8];
    name[0] = resources.getString("N");
    name[1] = resources.getString("NE");
    name[2] = resources.getString("E");
    name[3] = resources.getString("SE");
    name[4] = resources.getString("S");
    name[5] = resources.getString("SW");
    name[6] = resources.getString("W");
    name[7] = resources.getString("NW");
    return name;
  }
}
