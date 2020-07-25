package gr.cti.eslate.base.container;

import gr.cti.eslate.base.*;

/**
 * This class contains all the data required to defer the time display of a
 * performance timer group whose registration has been deferred until after
 * the microworld being monitored has finished loading.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
class DisplayData
{
  /**
   * Type of time display.
   */
  int type;
  /**
   * The PerformanceTimerGroup whose time display is being deferred.
   */
  PerformanceTimerGroup ptg;
  /**
   * The text to print before the displayed time.
   */
  String prefix;
  /**
   * The text to print after the elapsed time.
   */
  String suffix;
  /**
   * The E-Slate handle to which the the PerformanceTimer belongs.
   */
  ESlateHandle h;
  /**
   * Policy used for accumulative time display.
   */
  int policy;
  /**
   * Time to display.
   */
  long time1;
  /**
   * Second time to display (optional).
   */
  long time2;

  /**
   * Display elapsed time.
   */
  final static int ELAPSED_TIME = 0;
  /**
   * Display elapsed time for a PerformanceTimer belonging to an E-Slate
   * handle.
   */
  final static int ELAPSED_TIME_H = 1;
  /**
   * Display accumulative time.
   */
  final static int ACCUMULATIVE_TIME = 2;
  /**
   * Display accumulative time for a PerformanceTimerGroup belonging to an
   * E-Slate handle.
   */
  final static int ACCUMULATIVE_TIME_H = 3;
  /**
   * Display time.
   */
  final static int TIME = 4;
  /**
   * Display time for a PerformanceTimerGroup belonging to an E-Slate handle.
   */
  final static int TIME_H = 5;

  /**
   * Create a DisplayData instance.
   * @param     type    Type of time display.
   * @param     ptg     The PerformanceTimerGroup whose time display is being
   *                    deferred.
   * @param     prefix  The text to print before the displayed time.
   * @param     suffix  The text to print after the elapsed time.
   * @param     h       The E-Slate handle to which the the PerformanceTimer
   *                    belongs.
   * @param     policy  Policy used for accumulative time display.
   */
  DisplayData(int type, PerformanceTimerGroup ptg, String prefix,
              String suffix, ESlateHandle h, int policy,
              long time1, long time2)
  {
    this.type = type;
    this.ptg = ptg;
    this.prefix = prefix;
    this.suffix = suffix;
    this.h = h;
    this.policy = policy;
    this.time1 = time1;
    this.time2 = time2;
  }
}
