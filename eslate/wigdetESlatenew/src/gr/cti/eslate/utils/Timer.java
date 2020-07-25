package gr.cti.eslate.utils;

/**
 * This class provides methods that help timing sections of code.
 *
 * @author      Kriton Kyrimis
 * @version     2.0.0, 18-May-2006
 */
public class Timer
{
  /**
   * Indicates whether a native method for getting the system time with
   * accuracy is available.
   */
  private static boolean haveNativeTimer;

  /**
   * A native method for getting the system time with accuracy.
   */
  private native int getTickCount();

  static {
    // Attempt to load native method for getting the system time with accuracy.
    try {
      System.loadLibrary("timer");
      haveNativeTimer = true;
    } catch (Throwable th) {
      haveNativeTimer = false;
    }
  };

  /**
   * Time of previous call to timer.
   */
  private long t0;

  /**
   * Construct a new timer.
   */
  public Timer()
  {
    init();
  }

  /**
   * Initialize the timer with the current time.
   */
  public void init()
  {
    t0 = getTime();
  }

  /**
   * Return the time elapsed since the timer was created or the last time
   * <code>init()</code> was called.
   * @return    The elapsed time in milliseconds.
   */
  public long lapse()
  {
    long t1 = getTime();
    long result = t1 - t0;
    //t0 = t1;
    return result;
  }

  /**
   * Get the system time in milliseconds. This method will invoke a native
   * method to get the system time with greater accuracy than
   * System.currentTimeMillis(). If no such method is available,
   * System.currentTimeMillis() will be invoked instead.
   * @return    The system time in milliseconds.
   */
  public long getTime()
  {
    if (haveNativeTimer) {
      return (long)getTickCount();
    }else{
      return System.currentTimeMillis();
    }
  }
}
