package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.*;

/**
 * This is the actual timer of the performance framework.
 * The PerformanceTimer has methods that control the performance monitoring
 * process.
 *
 * @author      George Tsironis
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class PerformanceTimer extends PerformanceTimerGroup
{
  /**
   * The timer used to make time measurements.
   */
  private Timer timer = new Timer();
  /**
   * The time accumulated by previous measurements.
   */
  private long totalTime = 0L;
  /**
   * Specifies whether the timer is running.
   */
  private boolean running = true;

  /**
   * Constructs a new PerformanceTimer.
   */
  PerformanceTimer()
  {
    super();
    // Change the time mode to ELAPSED, which is a more reasonable default for
    // PerformanceTimers.
    timeMode = ELAPSED;
  }

  /**
   * Constructs a new PerformanceTimer with the specified name.
   * @param     name    The name of the new PerformanceTimer.
   */
  PerformanceTimer(String name)
  {
    super(name);
    // Change the time mode to ELAPSED, which is a more reasonable default for
    // PerformanceTimers.
    timeMode = ELAPSED;
  }

  /**
   * Constructs a new PerformanceTimer with the specified internal code.
   * @param     id      The id of the new PerformanceTimer.
   */
  PerformanceTimer(int id)
  {
    super(id);
    // Change the time mode to ELAPSED, which is a more reasonable default for
    // PerformanceTimers.
    timeMode = ELAPSED;
  }

  /**
   * Constructs a new PerformanceTimer with the specified internal code  and
   * name.
   * @param     name    The name of the new PerformanceTimer.
   * @param     id      The id of the new PerformanceTimer.
   */
  PerformanceTimer(int id, String name)
  {
    super(id, name);
    // Change the time mode to ELAPSED, which is a more reasonable default for
    // PerformanceTimers.
    timeMode = ELAPSED;
  }

  /**
   * Starts the timer, after reseting it first. If the timer is inactive, this
   * method does nothing.
   */
  void init()
  {
    if (active) {
      totalTime = 0L;
      running = true;
      timer.init();
    }
  }

  /**
   * Starts the timer, without reseting it. If the timer is inactive, this
   * method does nothing.
   */
  void start()
  {
    if (active) {
      running = true;
      timer.init();
    }
  }

  /**
   * Stops the timer. If the timer is inactive, this method does nothing.
   */
  void stop()
  {
    if (active) {
      if (running) {
        totalTime += timer.lapse();
      }
      running = false;
    }
  }

  /**
   * Resets the timer to zero. If the timer is inactive, this method does
   * nothing. If the timer is running, it is not stopped.
   */
  void reset()
  {
    if (active) {
      totalTime = 0L;
      if (running) {
        timer.init();
      }
    }
  }

  /**
   * Returns the time measured by the PerformaceTimer, i.e., the elapsed time
   * since it was initialized. The time is reported regardless of whether the
   * PerformaceTimer is still counting.
   * @return    The time measured by the PerformaceTimer. If the timer is
   *            inactive, this method returns 0.
   */
  long getElapsedTime()
  {
    if (active) {
      if (running) {
        return totalTime + timer.lapse();
      }else{
        return totalTime;
      }
    }else{
      return 0L;
    }
  }

  /**
   * Specifies whether the PerformanceTimer is active or not. Deactivating a
   * running timer will also stop the timer (implied call to
   * <code>stop()</code>. Activating a deactivated timer will also reset and
   * start the timer(implied call to <code>init()</code>.
   * @param     active  The new activation state of the timer.
   */
  protected void setActive(boolean active)
  {
    if (!active && this.active) {
      stop();
    }else{
      if (active && !this.active) {
        // Set this.active so that init() will work.
        this.active = active;
        init();
      }
    }
    super.setActive(active);
  }
}
