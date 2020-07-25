package gr.cti.eslate.base;

import java.util.*;

import com.ibm.bsf.*;

/**
 * This class extends the BSFManager class, adding the ability to undeclare
 * all declared beans in one fell swoop.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.21, 28-Sep-2007
 */
public class ESlateBSFManager extends BSFManager
{
  /**
   * Construct an ESlateBSFManager instance.
   */
  public ESlateBSFManager()
  {
    super();
  }

  /**
   * Undeclare all declared beans in one fell swoop.
   * @exception BSFException    Thrown if any of the languages that are already
   *                            running decides to throw an exception when asked to
   *                            undeclare this bean.
   */
  @SuppressWarnings("unchecked")
  public void undeclareAllBeans() throws BSFException
  {
    Object[] beans = declaredBeans.toArray();
    int n = beans.length;
    declaredBeans.clear();
    for (int i=0; i<n; i++) {
      objectRegistry.unregister(((BSFDeclaredBean)(beans[i])).name);
    }
    Enumeration enginesEnum = loadedEngines.elements();
    while (enginesEnum.hasMoreElements ()) {
      BSFEngine engine = (BSFEngine) enginesEnum.nextElement();
      for (int i=0; i<n; i++) {
        engine.undeclareBean ((BSFDeclaredBean)(beans[i]));
      }
    }
    for (int i=0; i<n; i++) {
      beans[i] = null;
    }
    beans = null;
  }
}
