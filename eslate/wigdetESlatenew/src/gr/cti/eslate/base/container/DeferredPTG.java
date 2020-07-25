package gr.cti.eslate.base.container;

/**
 * This class contains all the data required to defer the registration of a
 * performance timer group until after the microworld being monitored has
 * finished loading.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class DeferredPTG
{
  /**
   * The PerformanceTimerGroup that will be registered.
   */
  PerformanceTimerGroup ptg;
  /**
   * The ID of the group under which it will be registered.
   */
  int id;
  /**
   * The component associated with the PerformanceTimerGroup.
   */
  Object compo;

  /**
   * Create a DeferredPTG instance.
   */
  DeferredPTG(PerformanceTimerGroup ptg, int id, Object compo)
  {
    this.ptg = ptg;
    this.id = id;
    this.compo = compo;
  }
}
