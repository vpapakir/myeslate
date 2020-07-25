package gr.cti.eslate.base.container;

/**
 * Time descriptor for a given class. The descriptor encapsulates accumulated
 * time measurements for some aspect of the class.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class ClassTimer
{
  /**
   * Number of measurements.
   */
  int measurements = 0;
  /**
   * Total time measured.
   */
  long totalTime = 0L;
  /**
   * Specifies if the descriptor is enabled.
   */
  boolean enabled = false;
  /**
   * The name of the class.
   */
  String className;

  /**
   * Construct a ClassTimer instance.
   * @param     className       The name of the class.
   */
  ClassTimer(String className)
  {
    super();
    this.className = className;
  }

  /**
   * Add a time measurement to the descriptor.
   * @param     time    The time measurement.
   */
  void addTime(long time)
  {
    totalTime += time;
    measurements++;
  }

  /**
   * Clears accumulated measurements.
   */
  void clear()
  {
    totalTime = 0L;
    measurements = 0;
  }
}
